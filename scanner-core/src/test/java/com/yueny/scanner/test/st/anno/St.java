package com.yueny.scanner.test.st.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 22:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=TYPE)
@St
public @interface St {
}
