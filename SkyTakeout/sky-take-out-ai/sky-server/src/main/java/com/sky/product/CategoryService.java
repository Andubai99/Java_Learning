package com.sky.product;

import com.sky.common.BusinessException;
import com.sky.common.PageResult;
import com.sky.store.InMemorySkyStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final InMemorySkyStore store;
    private final ProductCache productCache;

    public CategoryService(InMemorySkyStore store, ProductCache productCache) {
        this.store = store;
        this.productCache = productCache;
    }

    public Category save(CategoryCommand command) {
        Category category = new Category();
        category.setId(store.nextId());
        category.setType(command.type());
        category.setName(command.name());
        category.setSort(command.sort() == null ? 0 : command.sort());
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        store.categories().add(category);
        productCache.evictAll();
        return category;
    }

    public PageResult<Category> page(Integer type, String name, int page, int pageSize) {
        List<Category> records = store.categories().stream()
                .filter(item -> type == null || Objects.equals(item.getType(), type))
                .filter(item -> name == null || item.getName().contains(name))
                .sorted(Comparator.comparing(Category::getSort).thenComparing(Category::getId))
                .toList();
        return new PageResult<>(records.size(), records.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList());
    }

    public List<Category> list(Integer type) {
        return store.categories().stream()
                .filter(item -> item.getStatus() == 1)
                .filter(item -> type == null || Objects.equals(item.getType(), type))
                .sorted(Comparator.comparing(Category::getSort).thenComparing(Category::getId))
                .toList();
    }

    public Category update(Long id, CategoryCommand command) {
        Category category = findById(id);
        category.setType(command.type());
        category.setName(command.name());
        category.setSort(command.sort());
        category.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
        return category;
    }

    public void status(Long id, int status) {
        Category category = findById(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        productCache.evictAll();
    }

    public void delete(Long id) {
        Category category = findById(id);
        boolean used = store.dishes().stream().anyMatch(dish -> Objects.equals(dish.getCategoryId(), category.getId()))
                || store.setmeals().stream().anyMatch(setmeal -> Objects.equals(setmeal.getCategoryId(), category.getId()));
        if (used) {
            throw new BusinessException("分类下存在商品，不能删除");
        }
        store.categories().remove(category);
        productCache.evictAll();
    }

    private Category findById(Long id) {
        return store.categories().stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("分类不存在"));
    }
}
