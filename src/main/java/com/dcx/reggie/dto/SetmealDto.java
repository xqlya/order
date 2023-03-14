package com.dcx.reggie.dto;


import com.dcx.reggie.entity.Setmeal;
import com.dcx.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
