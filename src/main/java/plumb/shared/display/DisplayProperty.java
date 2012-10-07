/**
 * 
 */
package plumb.shared.display;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import plumb.shared.validation.ValidationType;

/**
 * @author bkhadige
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface DisplayProperty {
	
	int size() default 0;
	
	String label() default "";
	
	String key() default "";
	
	int order() default 0;
	
	ValidationType[] validation() default {};

}
