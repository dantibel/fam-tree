package db6.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PhotoPersonRepository extends JpaRepository<PhotoPerson, Long> {
}