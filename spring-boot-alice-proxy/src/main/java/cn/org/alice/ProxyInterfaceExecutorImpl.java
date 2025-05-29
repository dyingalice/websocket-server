package cn.org.alice;

import cn.org.alice.anno.Proxy;
import cn.org.alice.bean.Pair;
import cn.org.alice.service.TemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 处理器实现类
 */
public class ProxyInterfaceExecutorImpl implements ProxyInterfaceExecutor, InitializingBean {

    @Autowired(required = false)
    private List<Executor<? extends Annotation>> list;

    private Map<Class<? extends Annotation>, Executor<? extends Annotation>> map;

    @Autowired
    private TemplateService templateService;

    /**
     * 分支处理
     */
    @Override
    public Object executor(Object proxy, Method method, Object[] args, Class<?> clazz) throws Throwable {
        try {
            templateService.put(method, args);
            List<Pair<Parameter, Object>> pvs = templateService.pair(method, args);
            Proxy proxyAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Proxy.class);
            Class<? extends Annotation> annotationClass = proxyAnnotation.value();
            Annotation annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), annotationClass);
            return map.get(annotationClass).done(annotation, ProxyInterface.builder()
                    .method(method)
                    .args(args)
                    .clazz(clazz)
                    .pairs(pvs)
                    .proxy(proxy)
                    .template(templateService)
                    .returnType(method.getReturnType())
                    .build());
        } finally {
            templateService.close();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (CollectionUtils.isEmpty(list)) {
            map = Collections.emptyMap();
        } else {
            map = list.stream().collect(Collectors.toMap(e -> e.realize(Executor.class), Function.identity()));
        }
    }
}
