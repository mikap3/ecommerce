package com.udacity.jwdnd.course4.ecommerce.order;

import com.udacity.jwdnd.course4.ecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
