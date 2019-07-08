package com.sev7e0.data;

import java.lang.annotation.*;

/**
 * @HotSpot annotation
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HotSpot {

    /**
     * kafka topic name
     *
     * @return
     */
    String name() default "hotspot";
}
