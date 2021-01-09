package com.udacity.jwdnd.course4.ecommerce.repository;

import com.udacity.jwdnd.course4.ecommerce.entity.Cart;
import com.udacity.jwdnd.course4.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
