package com.sky.employee;

import com.sky.common.PageResult;
import com.sky.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/employee")
public class AdminEmployeeController {
    private final EmployeeService employeeService;

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginCommand command) {
        return Result.success(employeeService.login(command));
    }

    @GetMapping("/page")
    public Result<PageResult<Employee>> page(@RequestParam(required = false) String name,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(employeeService.page(name, page, pageSize));
    }

    @PostMapping
    public Result<Employee> save(@RequestBody EmployeeCommand command) {
        return Result.success(employeeService.save(command));
    }

    @PutMapping
    public Result<Employee> update(@RequestParam Long id, @RequestBody EmployeeCommand command) {
        return Result.success(employeeService.update(id, command));
    }

    @GetMapping("/{id}")
    public Result<Employee> byId(@PathVariable Long id) {
        return Result.success(employeeService.findById(id));
    }

    @PostMapping("/status/{status}")
    public Result<Void> status(@PathVariable int status, @RequestParam Long id) {
        employeeService.status(id, status);
        return Result.success();
    }
}
