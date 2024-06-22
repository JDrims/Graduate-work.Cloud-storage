package ru.jdrims.cloudservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.jdrims.cloudservice.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
}