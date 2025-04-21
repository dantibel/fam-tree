package db6.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import db6.domain.Person;
import db6.domain.Relation;

@RepositoryRestResource
public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByPerson1(Person person1);
    List<Relation> findByPerson2(Person person2);

    @Query("SELECT r FROM Relation r WHERE r.person1 = :parent AND type = 'CHILD'")
    List<Relation> findChildrenOf(@Param("parent") Person parent);

    @Query("SELECT r FROM Relation r WHERE r.person2 = :child AND type = 'CHILD'")
    List<Relation> findParentsOf(@Param("child") Person child);

    @Query("SELECT r FROM Relation r WHERE (r.person1 = :spouse OR r.person2 = :spouse) AND type = 'SPOUSE'")
    List<Relation> findSpouseOf(@Param("spouse") Person spouse);

    @Modifying
    @Query("DELETE FROM Relation r WHERE r.person1 = :person OR r.person2 = :person")
    void deleteByPerson(@Param("person") Person person);
}
