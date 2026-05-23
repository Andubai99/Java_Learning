package com.sky.store;

import com.sky.address.AddressBook;
import com.sky.cart.ShoppingCartItem;
import com.sky.employee.Employee;
import com.sky.product.Category;
import com.sky.product.Dish;
import com.sky.product.Setmeal;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemorySkyStore {
    private final AtomicLong ids = new AtomicLong(100);
    private final List<Employee> employees = new CopyOnWriteArrayList<>();
    private final List<Category> categories = new CopyOnWriteArrayList<>();
    private final List<Dish> dishes = new CopyOnWriteArrayList<>();
    private final List<Setmeal> setmeals = new CopyOnWriteArrayList<>();
    private final List<AddressBook> addressBooks = new CopyOnWriteArrayList<>();
    private final List<ShoppingCartItem> shoppingCartItems = new CopyOnWriteArrayList<>();
    private volatile int shopStatus = 1;

    @PostConstruct
    void seed() {
        if (!employees.isEmpty()) {
            return;
        }
        Employee admin = new Employee();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setName("系统管理员");
        admin.setStatus(1);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        employees.add(admin);

        Category dishCategory = new Category(1L, 1, "热销菜品", 1, 1);
        Category setmealCategory = new Category(2L, 2, "商务套餐", 2, 1);
        categories.add(dishCategory);
        categories.add(setmealCategory);

        Dish rice = new Dish(10L, "招牌黄焖鸡米饭", dishCategory.getId(), new BigDecimal("28.00"), "", "经典热销单品", 1);
        rice.getFlavors().add("辣度:不辣,微辣,中辣");
        Dish soup = new Dish(11L, "番茄牛腩汤", dishCategory.getId(), new BigDecimal("32.00"), "", "酸甜浓郁", 1);
        dishes.add(rice);
        dishes.add(soup);

        Setmeal setmeal = new Setmeal(20L, "双人工作餐", setmealCategory.getId(), new BigDecimal("58.00"), "", "两份主食加汤品", 1);
        setmeal.getDishIds().add(rice.getId());
        setmeal.getDishIds().add(soup.getId());
        setmeals.add(setmeal);
    }

    public Long nextId() {
        return ids.incrementAndGet();
    }

    public List<Employee> employees() {
        return employees;
    }

    public List<Category> categories() {
        return categories;
    }

    public List<Dish> dishes() {
        return dishes;
    }

    public List<Setmeal> setmeals() {
        return setmeals;
    }

    public List<AddressBook> addressBooks() {
        return addressBooks;
    }

    public void removeAddressBook(Long id) {
        addressBooks.removeIf(address -> Objects.equals(address.getId(), id));
    }

    public List<ShoppingCartItem> shoppingCartItems() {
        return shoppingCartItems;
    }

    public void removeShoppingCartItem(Long id) {
        shoppingCartItems.removeIf(item -> Objects.equals(item.getId(), id));
    }

    public void removeShoppingCartItems(Long userId) {
        shoppingCartItems.removeIf(item -> Objects.equals(item.getUserId(), userId));
    }

    public int shopStatus() {
        return shopStatus;
    }

    public void shopStatus(int shopStatus) {
        this.shopStatus = shopStatus;
    }
}
