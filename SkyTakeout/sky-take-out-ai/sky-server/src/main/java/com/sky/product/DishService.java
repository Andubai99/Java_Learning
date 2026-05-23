package com.sky.product;

import com.sky.common.BusinessException;
import com.sky.common.PageResult;
import com.sky.store.InMemorySkyStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DishService {
    private final InMemorySkyStore store;
    private final ProductCache productCache;

    public DishService(InMemorySkyStore store, ProductCache productCache) {
        this.store = store;
        this.productCache = productCache;
    }

    public Dish save(DishCommand command) {
        Dish dish = new Dish();
        dish.setId(store.nextId());
        fill(dish, command);
        dish.setStatus(0);
        store.dishes().add(dish);
        productCache.evictAll();
        return dish;
    }

    public PageResult<Dish> page(String name, Long categoryId, int page, int pageSize) {
        List<Dish> records = store.dishes().stream()
                .filter(item -> name == null || item.getName().contains(name))
                .filter(item -> categoryId == null || Objects.equals(item.getCategoryId(), categoryId))
                .sorted(Comparator.comparing(Dish::getId))
                .toList();
        return new PageResult<>(records.size(), records.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList());
    }

    public List<Dish> userList(Long categoryId) {
        return productCache.cachedList("dish:" + categoryId, () -> store.dishes().stream()
                .filter(item -> item.getStatus() == 1)
                .filter(item -> categoryId == null || Objects.equals(item.getCategoryId(), categoryId))
                .sorted(Comparator.comparing(Dish::getId))
                .toList());
    }

    public Dish byId(Long id) {
        return store.dishes().stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("菜品不存在"));
    }

    public Dish update(Long id, DishCommand command) {
        Dish dish = byId(id);
        fill(dish, command);
        dish.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
        return dish;
    }

    public void status(Long id, int status) {
        Dish dish = byId(id);
        dish.setStatus(status);
        dish.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
    }

    public void delete(String ids) {
        List<Long> parsed = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        boolean enabled = store.dishes().stream()
                .filter(item -> parsed.contains(item.getId()))
                .anyMatch(item -> item.getStatus() == 1);
        if (enabled) {
            throw new BusinessException("已启售菜品不能删除");
        }
        store.dishes().removeIf(item -> parsed.contains(item.getId()));
        productCache.evictAll();
    }

    private static void fill(Dish dish, DishCommand command) {
        dish.setName(command.name());
        dish.setCategoryId(command.categoryId());
        dish.setPrice(command.price());
        dish.setImage(command.image());
        dish.setDescription(command.description());
        dish.setFlavors(command.flavors());
    }
}
