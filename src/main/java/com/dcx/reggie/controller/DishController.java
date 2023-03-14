package com.dcx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcx.reggie.common.R;
import com.dcx.reggie.dto.DishDto;
import com.dcx.reggie.entity.Category;
import com.dcx.reggie.entity.Dish;
import com.dcx.reggie.service.CategoryService;
import com.dcx.reggie.service.DishFlavorService;
import com.dcx.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

//        log.info("菜品信息：" + dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> getPage(int page,int pageSize,String name){
        Page<Dish> dishPage = new Page<>();
        Page<DishDto>  dishDtoPage   = new Page<>();
        LambdaQueryWrapper<Dish> dw = new LambdaQueryWrapper<>();
        dw.like(name != null, Dish::getName,name);
        dw.orderByDesc(Dish::getUpdateTime);
        Page<Dish> pageList = dishService.page(dishPage, dw);

        System.out.println(dishPage.getCountId());
        System.out.println(dishPage.getRecords());
        System.out.println(dishPage.getPages());
        System.out.println(dishPage.getSize());
        System.out.println(dishPage.getTotal());

//        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = pageList.getRecords();
        List<DishDto> list =  records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    @RequestMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish != null, Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }

}
