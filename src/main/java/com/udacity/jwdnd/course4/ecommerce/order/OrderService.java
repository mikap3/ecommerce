package com.udacity.jwdnd.course4.ecommerce.order;

import com.udacity.jwdnd.course4.ecommerce.user.User;
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

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order submitOrderOfUser(User user) {
        Order order = Order.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("submitted order_id={}", order.getId());
        return order;
    }
}
