package com.sky.cart;

import com.sky.store.InMemorySkyStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartServiceTest {

    @Test
    void addingSameDishAccumulatesQuantityAndAmount() {
        ShoppingCartService service = new ShoppingCartService(new InMemorySkyStore());

        service.addItem(3L, new CartItemCommand(10L, null, "dish", "米饭", "", new BigDecimal("2.50")));
        service.addItem(3L, new CartItemCommand(10L, null, "dish", "米饭", "", new BigDecimal("2.50")));

        ShoppingCartItem item = service.list(3L).get(0);
        assertEquals(2, item.getNumber());
        assertEquals(new BigDecimal("5.00"), item.getAmount());
    }
}
