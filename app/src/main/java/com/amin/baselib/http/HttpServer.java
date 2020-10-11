package com.amin.baselib.http;

/**
 * Created by  on 2017/3/29.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HttpServer {
    String value() default "hghHkgQ6whyqn09IrWKTCFVbaOpj/l5tlrkWeLXzImE=";
}
