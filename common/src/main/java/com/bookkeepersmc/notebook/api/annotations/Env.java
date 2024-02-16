package com.bookkeepersmc.notebook.api.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PACKAGE})
@Documented
public @interface Env {
    Enviroment value();
}
