package wrh.learning.simpleorm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Column {

    /**
     * customized column name
     *
     * @return column name
     */
    String name() default "";

    /**
     * is column unique?
     *
     * @return unique or not
     */
    boolean unique() default false;

    /**
     * is column nullable?
     *
     * @return nullable or not?
     */
    boolean notNull() default false;

}
