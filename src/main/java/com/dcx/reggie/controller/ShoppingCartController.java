package com.dcx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcx.reggie.common.BaseContext;
import com.dcx.reggie.common.R;
import com.dcx.reggie.entity.ShoppingCart;
import com.dcx.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cartServiceOne  = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne  != null){
            Integer number = cartServiceOne .getNumber();
            cartServiceOne .setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne );
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne  = shoppingCart;
        }

        return R.success(cartServiceOne);
    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }


    @DeleteMapping("/clean")
    public R<String> clean(){


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}
