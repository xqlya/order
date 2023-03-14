package com.dcx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcx.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
