package com.project.shopapp.repository;

import com.project.shopapp.model.Order;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(Integer userId);
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.active = 1")
    Order findByIdAndActive(@Param("id") Integer id);
}
