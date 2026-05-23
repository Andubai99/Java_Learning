package com.sky.cart;

import com.sky.store.InMemorySkyStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartService {
    private final InMemorySkyStore store;

    public ShoppingCartService(InMemorySkyStore store) {
        this.store = store;
    }

    public ShoppingCartItem addItem(Long userId, CartItemCommand command) {
        ShoppingCartItem item = store.shoppingCartItems().stream()
                .filter(existing -> Objects.equals(existing.getUserId(), userId))
                .filter(existing -> Objects.equals(existing.getDishId(), command.dishId()))
                .filter(existing -> Objects.equals(existing.getSetmealId(), command.setmealId()))
                .findFirst()
                .orElseGet(() -> createItem(userId, command));
        item.setNumber(item.getNumber() + 1);
        item.setAmount(item.getUnitAmount().multiply(BigDecimal.valueOf(item.getNumber())));
        return item;
    }

    public void decreaseItem(Long userId, CartItemCommand command) {
        store.shoppingCartItems().stream()
                .filter(existing -> Objects.equals(existing.getUserId(), userId))
                .filter(existing -> Objects.equals(existing.getDishId(), command.dishId()))
                .filter(existing -> Objects.equals(existing.getSetmealId(), command.setmealId()))
                .findFirst()
                .ifPresent(item -> {
                    item.setNumber(item.getNumber() - 1);
                    item.setAmount(item.getUnitAmount().multiply(BigDecimal.valueOf(Math.max(item.getNumber(), 0))));
                    if (item.getNumber() <= 0) {
                        store.removeShoppingCartItem(item.getId());
                    }
                });
    }

    public List<ShoppingCartItem> list(Long userId) {
        return store.shoppingCartItems().stream()
                .filter(item -> Objects.equals(item.getUserId(), userId))
                .toList();
    }

    public void clear(Long userId) {
        store.removeShoppingCartItems(userId);
    }

    private ShoppingCartItem createItem(Long userId, CartItemCommand command) {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setId(store.nextId());
        item.setUserId(userId);
        item.setDishId(command.dishId());
        item.setSetmealId(command.setmealId());
        item.setType(command.type());
        item.setName(command.name());
        item.setImage(command.image());
        item.setUnitAmount(command.amount());
        item.setCreateTime(LocalDateTime.now());
        store.shoppingCartItems().add(item);
        return item;
    }
}
