package com.dcx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcx.reggie.dto.DishDto;
import com.dcx.reggie.entity.Dish;
import com.dcx.reggie.entity.DishFlavor;
import com.dcx.reggie.mapper.DishMapper;
import com.dcx.reggie.service.DishFlavorService;
import com.dcx.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(qw);
        dishDto.setFlavors(list);


        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //修改dishDto对应表数据
        this.updateById(dishDto);

        //删除菜品口味表数据
        Long id = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> dw = new LambdaQueryWrapper<>();
        dw.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(dw);

        //
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);



    }
}
