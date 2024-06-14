package com.libreforge.integration.data.user.repository;

import com.libreforge.integration.data.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Tag JPA Repository to manage {@link User} entity
 * @author Maksym Khudiakov
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
