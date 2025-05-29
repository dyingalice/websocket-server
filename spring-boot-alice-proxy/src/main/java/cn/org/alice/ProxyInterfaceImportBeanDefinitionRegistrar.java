package cn.org.alice;

import cn.org.alice.anno.ProxyScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 代理类注入
 */
public class ProxyInterfaceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ProxyScan.class.getName());
        List<Object> basePackages = attributes.get("value");
        ProxyInterfaceScanner scanner = new ProxyInterfaceScanner(registry);
        basePackages.forEach(e -> scanner.scan((String[]) e));
    }

}
