package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* 
 * 自定义注解，用于标识某个方法需要 公共字段 自动填充处理
 * @Target 表示该注解可以用于哪些地方
 * @Retention 表示该注解在什么范围内有效
 * @interface 表示该注解是一个接口
 * value 表示该注解的值
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /*
    * 自定义注解，用于标识某个方法需要 公共字段 自动填充处理
    * */
   OperationType value();
}
