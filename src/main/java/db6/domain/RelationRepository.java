package db6.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByPerson1(Person person1);
    List<Relation> findByPerson2(Person person2);
}

