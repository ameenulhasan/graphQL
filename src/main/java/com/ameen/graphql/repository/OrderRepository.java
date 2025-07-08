package com.ameen.graphql.repository;

import com.ameen.graphql.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o " +
            "WHERE (:date IS NOT NULL AND o.date = :date) " +
            "   OR (:date IS NULL AND o.date LIKE CONCAT('%', :month, '%'))")
    List<Order> findByDateMonth(@Param("date") String date, @Param("month") String month);

    @Query("SELECT o FROM Order o JOIN FETCH o.user u JOIN FETCH o.book b WHERE o.id = :orderId AND o.isActive = true")
    Optional<Order> findByIdWithUserAndBook(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.isActive = true")
    List<Order> findByUserId(@Param("userId") Long userId);

}
