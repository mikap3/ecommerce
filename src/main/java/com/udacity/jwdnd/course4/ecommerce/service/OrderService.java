package com.udacity.jwdnd.course4.ecommerce.service;

import com.udacity.jwdnd.course4.ecommerce.entity.User;
import com.udacity.jwdnd.course4.ecommerce.entity.UserOrder;
import com.udacity.jwdnd.course4.ecommerce.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<UserOrder> findOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public UserOrder submitOrderOfUser(User user) {
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("submitted order_id={}", order.getId());
        return order;
    }
}
