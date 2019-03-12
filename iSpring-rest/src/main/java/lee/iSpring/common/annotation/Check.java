package lee.iSpring.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {

	/** 是否跳过检过 */
	public boolean passCheck() default false;
	/** 描述 */
	public String describe() default "";

}
