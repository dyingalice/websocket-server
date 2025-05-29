package cn.org.alice;

import cn.org.alice.anno.Proxy;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 统一代理类, 再分支
 */
public class ProxyInterfaceInvocationHandler implements InvocationHandler {

    private final ApplicationContext applicationContext;

    private final Class<?> clazz;

    private final ConcurrentHashMap<Method, MethodHandle> methodHandleMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Method, Boolean> methodEnableMap = new ConcurrentHashMap<>();

    ProxyInterfaceInvocationHandler(ApplicationContext applicationContext, Class<?> clazz) {
        this.applicationContext = applicationContext;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Proxy annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Proxy.class);
        //如果没有Proxy标识, 如果是默认方法, 如果不是接口, 则执行默认方法
        if (disable(annotation, method)) {
            return invokeDefaultMethod(proxy, method, args);
        }
        return applicationContext.getBean(ProxyInterfaceExecutor.class).executor(proxy, method, args, clazz);
    }

    private Boolean disable(Proxy annotation, Method method) {
        return methodEnableMap.computeIfAbsent(method, new DisableFunction(annotation));
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        MethodHandle defaultMethodHandle = methodHandleMap.computeIfAbsent(method, new MethodFunction(proxy));
        return defaultMethodHandle.invokeWithArguments(args);
    }

    static class DisableFunction implements Function<Method, Boolean> {

        private final Annotation annotation;

        public DisableFunction(Annotation annotation) {
            this.annotation = annotation;
        }

        @Override
        public Boolean apply(Method method) {
            return Objects.isNull(annotation) || method.isDefault() || (!method.getDeclaringClass().isInterface());
        }
    }

    static class MethodFunction implements Function<Method, MethodHandle> {

        private final Object proxy;

        public MethodFunction(Object proxy) {
            this.proxy = proxy;
        }

        @SneakyThrows
        @Override
        public MethodHandle apply(Method method) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            return lookup
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy);
        }
    }

}