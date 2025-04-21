package db6.service;

import db6.domain.Parents;
import db6.domain.Person;
import db6.domain.Relation;
import db6.domain.Person.Gender;
import db6.domain.repository.PersonRepository;
import db6.domain.repository.RelationRepository;
import jakarta.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service wrapping repositories and providing methods for family tree operations
@Service
public class FamilyTreeService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RelationRepository relationRepository;

    public Optional<Person> getPerson(Long id) {
        return personRepository.findById(id);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public List<Person> getChildren(Long parentId) {
        // Get the parent
        Person parent = personRepository.findById(parentId).orElse(null);
        if (parent == null) {
            return List.of();
        }

        // Get the parent's children
        return relationRepository.findByPerson1(parent).stream()
            .filter(relation -> relation.getType().equals(Relation.Type.CHILD))
            .map(relation -> personRepository.findById(relation.getPerson2().getId()).orElse(null))
            .collect(Collectors.toList());
    }

    public Parents getParents(Long childId) {
        // Get the child
        Person child = personRepository.findById(childId).orElse(null);
        if (child == null) {
            return null;
        }

        // Get all the child's parents
        List<Person> parents = relationRepository.findByPerson2(child).stream()
            .filter(relation -> relation.getType().equals(Relation.Type.CHILD))
            .map(relation -> personRepository.findById(relation.getPerson1().getId()).orElse(null))
            .collect(Collectors.toList());

        assert(parents.size() <= 2);
        if (parents.isEmpty()) {
            return new Parents();
        }

        // Determine father and mother
        Person father, mother;
        if (parents.get(0).getGender() == Gender.MALE) {
            father = parents.get(0);
            mother = parents.size() > 1 ? parents.get(1) : null;
        } else {
            mother = parents.get(0);
            father = parents.size() > 1 ? parents.get(1) : null;
        }
        return new Parents(father, mother);
    }

    // Delete a person that may have parents or childern but not both
    @Transactional
    public boolean deletePerson(Long id) {
        // Check if person exists
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isEmpty()) {
            return false;
        }

        // Abort deletion if person has both parents and children
        if (getParents(id).hasAny() && !getChildren(id).isEmpty()) {
            return false;
        }

        // Delete all relations with this person
        relationRepository.deleteByPerson(personOpt.get());

        // Delete the person
        personRepository.deleteById(id);
        return true;
    }

    public void savePortrait(Long personId, MultipartFile file) throws IOException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        Photo photo = new Photo();
        photo.setData(file.getBytes());
        photo.setFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());

        photo = photoRepository.save(photo);
        person.setPortrait(photo);

        personRepository.save(person);
    }

    public boolean updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id).map(
            person -> {
                person.setFirstName(updatedPerson.getFirstName());
                person.setMiddleName(updatedPerson.getMiddleName());
                person.setLastName(updatedPerson.getLastName());
                person.setBirthDate(updatedPerson.getBirthDate());
                person.setDeathDate(updatedPerson.getDeathDate());
                person.setGender(updatedPerson.getGender());
                personRepository.save(person);
                return true;
            }
        ).orElse(false);
    }
}