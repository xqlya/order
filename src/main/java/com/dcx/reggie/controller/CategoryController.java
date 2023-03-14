package com.dcx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dcx.reggie.common.R;
import com.dcx.reggie.entity.Category;
import com.dcx.reggie.entity.DishFlavor;
import com.dcx.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> getPage(int page,int pageSize){
        Page<Category> categoryPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<Category>();
        qw.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,qw);
        return R.success(categoryPage);
    }
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

        //categoryService.removeById(id);
        categoryService.remove(id);

        return R.success("分类信息删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> getList(Category category){
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(category.getType() != null,Category::getType,category.getType());
        qw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(qw);

        return R.success(list);
    }
}
