package cn.org.alice;

import cn.org.alice.anno.Proxy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * 接口扫描
 */
public class ProxyInterfaceScanner extends ClassPathBeanDefinitionScanner {

    public ProxyInterfaceScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @SneakyThrows
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinition : beanDefinitionHolders) {
            definition = (GenericBeanDefinition) beanDefinition.getBeanDefinition();
            //注入interface时确保注入的是代理
            definition.getConstructorArgumentValues().addGenericArgumentValue(Class.forName(definition.getBeanClassName()));
            definition.setBeanClass(ProxyInterfaceFactoryBean.class);
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }

    @Override
    protected void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(Proxy.class));
        this.addIncludeFilter((mr, mrf) -> mr.getAnnotationMetadata().isInterface());
    }

}