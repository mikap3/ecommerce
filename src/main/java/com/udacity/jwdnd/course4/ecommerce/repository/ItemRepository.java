package com.udacity.jwdnd.course4.ecommerce.repository;

import com.udacity.jwdnd.course4.ecommerce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);
}
