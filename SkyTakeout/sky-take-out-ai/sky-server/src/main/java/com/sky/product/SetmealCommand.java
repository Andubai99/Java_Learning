package com.sky.product;

import java.math.BigDecimal;
import java.util.List;

public record SetmealCommand(String name, Long categoryId, BigDecimal price, String image, String description, List<Long> dishIds) {
}
