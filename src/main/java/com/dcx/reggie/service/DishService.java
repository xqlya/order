package com.dcx.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dcx.reggie.dto.DishDto;
import com.dcx.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

}
