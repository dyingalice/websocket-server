package cn.org.alice.anno;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

/***
 * @author 爱莉丝
 * @since 2021/10/16 12:48
 * @apiNote 代理标记-标识实际的注解
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
@Validated
public @interface Proxy {

    /***
     * @author 爱莉丝
     * @since 2021/10/16 12:49
     * @apiNote 实际生效的注解
     **/
    Class<? extends Annotation> value();
}
