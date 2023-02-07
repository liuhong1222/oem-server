package com.credit.oem.admin.common.annotation;

import java.lang.annotation.*;

/**
 * @author chenzj
 * @since 2018/8/9
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
    String value() default "";

    int order();
}
