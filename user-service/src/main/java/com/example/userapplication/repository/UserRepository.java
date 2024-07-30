package com.example.userapplication.repository;

import com.example.userapplication.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their authentication ID.
     *
     * @param  authId  the authentication ID of the user
     * @return         an optional containing the user if found, otherwise empty
     */
    Optional<User> findUserByAuthId(String authId);

    Optional<User> findUserByEmailId(String emailId);
}
