package com.dcx.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@RestController
@Slf4j
public class GlobalExceptionHandler {
    /**
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        boolean duplicate_entry = ex.getMessage().contains("Duplicate entry");
        if(duplicate_entry){
            String[] s = ex.getMessage().split(" ");
            return R.error(s[2]+"重复,添加失败");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
