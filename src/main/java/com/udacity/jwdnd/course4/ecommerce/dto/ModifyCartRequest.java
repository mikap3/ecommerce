package com.udacity.jwdnd.course4.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ModifyCartRequest {

    @NotNull
    @JsonProperty
    private Long itemId;

    @Positive
    @JsonProperty
    private Integer quantity;

    public ModifyCartRequest(@NotNull Long itemId, @Positive Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public ModifyCartRequest() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
