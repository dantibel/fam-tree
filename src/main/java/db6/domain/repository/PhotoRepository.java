package db6.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import db6.domain.Photo;

@RepositoryRestResource
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}