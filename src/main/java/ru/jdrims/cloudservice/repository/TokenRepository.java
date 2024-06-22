package ru.jdrims.cloudservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.jdrims.cloudservice.entities.TokenEntity;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}