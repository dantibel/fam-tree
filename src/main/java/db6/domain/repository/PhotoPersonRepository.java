package db6.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import db6.domain.PhotoPerson;

@RepositoryRestResource
public interface PhotoPersonRepository extends JpaRepository<PhotoPerson, Long> {
}