package cn.org.alice;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * FactoryBean
 *
 * @param <T>
 */
public class ProxyInterfaceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    /**
     * 接口的类型
     */
    private final Class<T> clazz;

    private ApplicationContext applicationContext;

    public ProxyInterfaceFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getObject() {
        //封装代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new ProxyInterfaceInvocationHandler(applicationContext, clazz)
        );
    }

    @Override
    public Class<T> getObjectType() {
        return clazz;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}