package de.gravitex.banking_core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.gravitex.banking_core.entity.annotation.util.EditType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnableEdit {

	EditType type();
}