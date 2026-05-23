package com.sky.employee;

import com.sky.common.BusinessException;
import com.sky.common.JwtTokenService;
import com.sky.common.PageResult;
import com.sky.common.TokenSubject;
import com.sky.store.InMemorySkyStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {
    private final InMemorySkyStore store;
    private final JwtTokenService jwtTokenService;

    public EmployeeService(InMemorySkyStore store, JwtTokenService jwtTokenService) {
        this.store = store;
        this.jwtTokenService = jwtTokenService;
    }

    public EmployeeLoginVO login(EmployeeLoginCommand command) {
        Employee employee = store.employees().stream()
                .filter(item -> Objects.equals(item.getUsername(), command.username()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("账号不存在"));
        if (!Objects.equals(employee.getPassword(), command.password())) {
            throw new BusinessException("密码错误");
        }
        if (employee.getStatus() != 1) {
            throw new BusinessException("账号已禁用");
        }
        String token = jwtTokenService.createToken(new TokenSubject(employee.getId(), "employee"));
        return new EmployeeLoginVO(employee.getId(), employee.getUsername(), employee.getName(), token);
    }

    public PageResult<Employee> page(String name, int page, int pageSize) {
        List<Employee> records = store.employees().stream()
                .filter(item -> name == null || item.getName().contains(name) || item.getUsername().contains(name))
                .sorted(Comparator.comparing(Employee::getId))
                .toList();
        return new PageResult<>(records.size(), records.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList());
    }

    public Employee save(EmployeeCommand command) {
        boolean exists = store.employees().stream().anyMatch(item -> Objects.equals(item.getUsername(), command.username()));
        if (exists) {
            throw new BusinessException("员工账号已存在");
        }
        Employee employee = new Employee();
        employee.setId(store.nextId());
        fill(employee, command);
        employee.setPassword(command.password() == null || command.password().isBlank() ? "123456" : command.password());
        employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        store.employees().add(employee);
        return employee;
    }

    public Employee update(Long id, EmployeeCommand command) {
        Employee employee = findById(id);
        fill(employee, command);
        employee.setUpdateTime(LocalDateTime.now());
        return employee;
    }

    public Employee findById(Long id) {
        return store.employees().stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("员工不存在"));
    }

    public void status(Long id, int status) {
        Employee employee = findById(id);
        employee.setStatus(status);
        employee.setUpdateTime(LocalDateTime.now());
    }

    private static void fill(Employee employee, EmployeeCommand command) {
        employee.setUsername(command.username());
        employee.setName(command.name());
        employee.setPhone(command.phone());
    }
}
