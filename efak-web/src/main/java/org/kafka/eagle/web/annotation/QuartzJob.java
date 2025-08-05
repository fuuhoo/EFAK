package org.kafka.eagle.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzJob {
    String group() default "DEFAULT_GROUP";
    String cron() default "";
}
