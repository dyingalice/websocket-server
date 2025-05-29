package cn.org.alice;


import cn.org.alice.util.ClassType;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface Executor<T extends Annotation> extends ClassType<T> {

    Object invoke(T annotation, ProxyInterface proxyInterface) throws Throwable;

    default Object done(Annotation annotation, ProxyInterface proxyInterface) throws Throwable {
        return invoke((T) annotation, proxyInterface);
    }
}
