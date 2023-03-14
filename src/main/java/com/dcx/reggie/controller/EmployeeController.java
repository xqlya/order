package com.dcx.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcx.reggie.common.R;
import com.dcx.reggie.entity.Employee;
import com.dcx.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.查验username在数据库中是否存在
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(qw);

        //3.如果没有查询到则返回错误
        if(emp == null){
            return  R.error("登录失败");
        }
        //4.查验password 是否正确
        if(!emp.getPassword().equals(password)){
            return  R.error("登录失败");
        }
        //5.看登录人员状态是否被禁
        if(emp.getStatus() == 0){
            return  R.error("账号禁用");
        }
        //6.登录成功，将员工id存入session 并且返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("logout")
    public R logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return new R().success("已经退出！");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long employeeId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);

        employeeService.save(employee);
        return R.success("成功添加");
    }

    @GetMapping("/page")
    public R<Page> getPage(int page,int pageSize,String name){
        Page<Employee> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        employeeService.page(iPage,qw);
        return R.success(iPage);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("启用成功！！");
        Long updateUser = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(updateUser);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);

        return R.success("员工信息修改成功！");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        if(emp != null){
            return R.success(emp);
        }
        return R.error("数据错误");
    }
}
