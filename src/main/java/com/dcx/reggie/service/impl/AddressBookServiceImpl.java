package com.dcx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcx.reggie.entity.AddressBook;
import com.dcx.reggie.mapper.AddressBookMapper;
import com.dcx.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
