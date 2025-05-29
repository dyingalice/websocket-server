package cn.org.alice;

import java.lang.reflect.Method;

/**
 * ProxyInterface处理类
 */
public interface ProxyInterfaceExecutor {

    /**
     * 处理
     *
     * @param proxy
     * @param method
     * @param args
     * @param clazz
     * @return
     * @throws Throwable
     */
    Object executor(Object proxy, Method method, Object[] args, Class<?> clazz) throws Throwable;

}
