package de.gravitex.banking_core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.gravitex.banking_core.formatter.DefaultValueFormatter;
import de.gravitex.banking_core.formatter.base.ValueFormatter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PresentMe {

	Class<? extends ValueFormatter> valueFormatter() default DefaultValueFormatter.class;

	boolean sortMe() default false;
	
	boolean filterMe() default false;

	int order();
}