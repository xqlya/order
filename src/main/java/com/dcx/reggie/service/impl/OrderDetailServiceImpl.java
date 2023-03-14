package com.dcx.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcx.reggie.entity.OrderDetail;
import com.dcx.reggie.mapper.OrderDetailMapper;
import com.dcx.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}