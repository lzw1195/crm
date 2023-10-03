package com.yjxxt.crm.annoation;

import java.lang.annotation.*;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission  {
    String code() default "";
}
