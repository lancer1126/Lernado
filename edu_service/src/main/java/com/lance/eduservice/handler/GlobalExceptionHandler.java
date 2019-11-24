package com.lance.eduservice.handler;

import com.lance.common.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e)
    {
        e.printStackTrace();
        return R.error().message("出现异常");
    }
}
