package com.udacity.jwdnd.course4.ecommerce.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.udacity.jwdnd.course4.ecommerce.item.Item;
import com.udacity.jwdnd.course4.ecommerce.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty
    private Long id;

    @ManyToMany
    @JsonProperty
    private List<Item> items;

    @OneToOne(mappedBy = "cart")
    @JsonProperty
    private User user;

    @Transient
    @JsonProperty
    public BigDecimal getTotal() {
        return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(Item item) {
        if (items == null)
            items = new ArrayList<>();
        items.add(item);
    }

    public void removeItem(Item item) {
        if (items == null)
            return;
        items.remove(item);
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
}
