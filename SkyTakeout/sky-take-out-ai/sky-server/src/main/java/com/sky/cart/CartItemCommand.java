package com.sky.cart;

import java.math.BigDecimal;

public record CartItemCommand(Long dishId, Long setmealId, String type, String name, String image, BigDecimal amount) {
}
