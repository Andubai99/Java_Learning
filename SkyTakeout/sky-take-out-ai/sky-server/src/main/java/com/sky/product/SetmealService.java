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
public class SetmealService {
    private final InMemorySkyStore store;
    private final ProductCache productCache;

    public SetmealService(InMemorySkyStore store, ProductCache productCache) {
        this.store = store;
        this.productCache = productCache;
    }

    public Setmeal save(SetmealCommand command) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(store.nextId());
        fill(setmeal, command);
        setmeal.setStatus(0);
        store.setmeals().add(setmeal);
        productCache.evictAll();
        return setmeal;
    }

    public PageResult<Setmeal> page(String name, Long categoryId, int page, int pageSize) {
        List<Setmeal> records = store.setmeals().stream()
                .filter(item -> name == null || item.getName().contains(name))
                .filter(item -> categoryId == null || Objects.equals(item.getCategoryId(), categoryId))
                .sorted(Comparator.comparing(Setmeal::getId))
                .toList();
        return new PageResult<>(records.size(), records.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList());
    }

    public List<Setmeal> userList(Long categoryId) {
        return productCache.cachedList("setmeal:" + categoryId, () -> store.setmeals().stream()
                .filter(item -> item.getStatus() == 1)
                .filter(item -> categoryId == null || Objects.equals(item.getCategoryId(), categoryId))
                .sorted(Comparator.comparing(Setmeal::getId))
                .toList());
    }

    public Setmeal byId(Long id) {
        return store.setmeals().stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("套餐不存在"));
    }

    public Setmeal update(Long id, SetmealCommand command) {
        Setmeal setmeal = byId(id);
        fill(setmeal, command);
        setmeal.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
        return setmeal;
    }

    public void status(Long id, int status) {
        Setmeal setmeal = byId(id);
        setmeal.setStatus(status);
        setmeal.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
    }

    public void delete(String ids) {
        List<Long> parsed = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        boolean enabled = store.setmeals().stream()
                .filter(item -> parsed.contains(item.getId()))
                .anyMatch(item -> item.getStatus() == 1);
        if (enabled) {
            throw new BusinessException("已启售套餐不能删除");
        }
        store.setmeals().removeIf(item -> parsed.contains(item.getId()));
        productCache.evictAll();
    }

    private static void fill(Setmeal setmeal, SetmealCommand command) {
        setmeal.setName(command.name());
        setmeal.setCategoryId(command.categoryId());
        setmeal.setPrice(command.price());
        setmeal.setImage(command.image());
        setmeal.setDescription(command.description());
        setmeal.setDishIds(command.dishIds());
    }
}
