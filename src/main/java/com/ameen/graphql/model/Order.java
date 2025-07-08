package com.ameen.graphql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long totalPrice;
    private Integer quantity;
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id_fk")
    private Book book;
    private Boolean isActive;
    private Boolean deletedFlag;
    private Timestamp createdAt;
    private Long createdBy;
    private Timestamp modifiedAt;
    private Long modifiedBy;

}
