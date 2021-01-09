package com.udacity.jwdnd.course4.ecommerce.cart;

import com.udacity.jwdnd.course4.ecommerce.item.Item;
import com.udacity.jwdnd.course4.ecommerce.user.User;
import com.udacity.jwdnd.course4.ecommerce.item.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ItemService itemService;

    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    public CartService(CartRepository cartRepository, ItemService itemService) {
        this.cartRepository = cartRepository;
        this.itemService = itemService;
    }

    public Cart addToCart(User user, ModifyCartRequest modifyCartRequest) {
        Item item = itemService.findItemById(modifyCartRequest.getItemId());
        Cart cart = user.getCart();
        IntStream.range(0, modifyCartRequest.getQuantity()).forEach(i -> cart.addItem(item));
        cartRepository.save(cart);
        logger.info("added items to cart_id={}", cart.getId());
        return cart;
    }

    public Cart removeFromCart(User user, ModifyCartRequest modifyCartRequest) {
        Item item = itemService.findItemById(modifyCartRequest.getItemId());
        Cart cart = user.getCart();
        IntStream.range(0, modifyCartRequest.getQuantity()).forEach(i -> cart.removeItem(item));
        cartRepository.save(cart);
        logger.info("removed items from cart_id={}", cart.getId());
        return cart;
    }
}
