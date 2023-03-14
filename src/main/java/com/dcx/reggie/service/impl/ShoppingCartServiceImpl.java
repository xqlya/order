package com.dcx.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcx.reggie.entity.ShoppingCart;
import com.dcx.reggie.mapper.ShoppingCartMapper;
import com.dcx.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
