package com.sky.aspect;

import com.sky.constant.AutoFillConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import java.time.LocalDateTime;
import java.lang.reflect.Method;

/*
* 自定义切面类
* 实现公共字段自动填充功能
* */
@Aspect
@Component
@Slf4j

public class AutoFillAspect {
    /*切面：通知+切入点*/
    /*
    切入点
    定义切入点：哪些点需要被拦截
    @Pointcut execution(* com.sky.mapper.*.*(..)) 表示对com.sky.mapper包下的所有方法进行拦截
    @Pointcut("@annotation(com.sky.annotation.AutoFill)") //表示对有AutoFill注解的方法进行拦截
    AutoFillPointCut(JoinPoint joinPoint) { //表示连接点对象
    * */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)") //定义对哪些类的哪些方法做拦截
    public void AutoFillPointCut() {

    }

    /*
    通知,在通知中进行公共字段赋值
    @Before("AutoFillPointCut(joinPoint)") 表示在方法执行之前执行
    * */
    @Before("AutoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始公共字段自动填充...");
        
        //1. 获取当前被拦截的方法上的数据库操作类型
        /* 签名信息（Method Signature）是指方法的详细信息，包括方法名称、返回类型、参数类型等。 */
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //获取方法上的注解对象
        OperationType operationType = autoFill.value(); //获取数据库操作类型
        
        //2. 获取当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];//获取第一个参数,约定好的，用object的原因是可能是员工，可能是菜品
        
        //3. 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        
        //4. 根据不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT) {
            //为4个公共字段赋值，用反射来获取方法
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("公共字段自动填充失败：{}", e.getMessage());
            }
        } else if(operationType == OperationType.UPDATE) {
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                
                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("公共字段自动填充失败：{}", e.getMessage());
            }
        }
    }
}
