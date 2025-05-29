package cn.org.alice.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板引擎所用的key
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TplKey {

    /**
     * 模板引擎所需的key
     *
     * @return
     */
    String value();

}
