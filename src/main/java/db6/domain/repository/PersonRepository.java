package db6.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import db6.domain.Person;

@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person, Long> {
    //Optional<Person> findById(Long id);
}
