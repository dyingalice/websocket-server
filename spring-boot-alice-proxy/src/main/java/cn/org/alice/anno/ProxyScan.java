package cn.org.alice.anno;

import cn.org.alice.ProxyInterfaceExecutorImpl;
import cn.org.alice.ProxyInterfaceImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ProxyInterfaceImportBeanDefinitionRegistrar.class, ProxyInterfaceExecutorImpl.class})
@EnableTemplate
@Inherited
public @interface ProxyScan {

    String[] value() default {};
}
