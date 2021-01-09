package com.udacity.jwdnd.course4.ecommerce.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.udacity.jwdnd.course4.ecommerce.cart.Cart;
import com.udacity.jwdnd.course4.ecommerce.item.Item;
import com.udacity.jwdnd.course4.ecommerce.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CustomerOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonProperty
    private User user;

    @Column
    @JsonProperty
    private BigDecimal total;

    public static Order createFromCart(Cart cart) {
        Order order = new Order();
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotal(cart.getTotal());
        User user = cart.getUser();
        user.getOrders().add(order);
        order.setUser(user);
        return order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
