package db6.service;

import db6.domain.Parents;
import db6.domain.Person;
import db6.domain.PersonRepository;
import db6.domain.Relation;
import db6.domain.RelationRepository;
import db6.domain.Person.Gender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyTreeService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RelationRepository relationRepository;

    public Person getRootPerson() {
        // Fetch the root person (e.g., the logged-in user or a specific person)
        return personRepository.findById(3L).orElse(null); // Replace 1L with dynamic logic
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
            return null;
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
}