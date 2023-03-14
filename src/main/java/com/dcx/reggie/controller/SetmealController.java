package com.dcx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcx.reggie.common.R;
import com.dcx.reggie.dto.SetmealDto;
import com.dcx.reggie.entity.Category;
import com.dcx.reggie.entity.Setmeal;
import com.dcx.reggie.service.CategoryService;
import com.dcx.reggie.service.SetmealDishService;
import com.dcx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;




    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> get(int page,int pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }


    @DeleteMapping()
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功菜单成功！");
    }

    @GetMapping("/list")
    public R<Setmeal> get(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() !=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.getOne(queryWrapper);
        return R.success(setmeal);
    }
}
