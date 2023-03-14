package com.dcx.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dcx.reggie.dto.SetmealDto;
import com.dcx.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);

}
