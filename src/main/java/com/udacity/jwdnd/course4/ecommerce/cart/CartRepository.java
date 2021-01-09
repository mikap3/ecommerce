package com.udacity.jwdnd.course4.ecommerce.cart;

import com.udacity.jwdnd.course4.ecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
