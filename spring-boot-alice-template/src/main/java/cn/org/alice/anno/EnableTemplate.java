package cn.org.alice.anno;

import cn.org.alice.config.BeanConfiguration;
import cn.org.alice.service.impl.TemplateServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动模板引擎
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({
        TemplateServiceImpl.class,
        BeanConfiguration.class,
})
public @interface EnableTemplate {
}
