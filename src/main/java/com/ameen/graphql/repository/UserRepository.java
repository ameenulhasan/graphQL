package com.ameen.graphql.repository;

import com.ameen.graphql.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    User findByMail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isActive = true")
    Optional<User> findByIdIsActive(Long id);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.orders o " +
            "LEFT JOIN FETCH o.book " +
            "WHERE u.isActive = true AND u.deletedFlag = false")
    List<User> findAllActiveUsersWithOrdersAndBooks();

    @Query("""
    SELECT DISTINCT u FROM User u 
    JOIN u.orders o 
    JOIN o.book b 
    WHERE u.isActive = true 
      AND o.isActive = true 
      AND b.isActive = true 
      AND LOWER(u.name) LIKE LOWER(CONCAT(:search, '%'))
      ORDER BY u.id DESC
""")
    Page<User> findByNameAndIsActiveTrue(String search, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.id DESC")
    Page<User> findByIsActiveTrue(Pageable pageable);

}
