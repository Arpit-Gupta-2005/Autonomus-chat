package com.example.Anonymous.chat.repository;

import com.example.Anonymous.chat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByDisplayName(String displayName);

    boolean existsByEmail(String email);

    boolean existsByDisplayName(String displayName);
}
