package cn.org.alice;

import cn.org.alice.bean.Pair;
import cn.org.alice.util.ClassType;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 适配器
 * @param <G>
 * @param <K>
 * @param <B>
 * @param <T>
 * @param <P>
 */
public abstract class AdapterContext<G extends Annotation, K, B extends Annotation, T extends Annotation, P> implements
        ApplicationContextAware,
        InitializingBean,
        ClassType {

    //spring容器
    private ApplicationContext applicationContext;

    //映射关系, K 注解上的标识, B 类上的注解, T 方法上的注解
    protected Map<K, Mapping<B, T>> mapping;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化数据
     */
    @Override
    public void afterPropertiesSet() {
        Collection<Object> values = applicationContext.getBeansWithAnnotation(bean()).values();
        mapping = values.stream()
                .flatMap(this::mapping)
                .flatMap(e -> {
                    List<K> keys = key(e.getBase(), e.getType());
                    return keys.stream().map(y -> new Pair<>(y, e));
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    //被标识的类对象
    private Class<G> bean() {
        return (Class<G>) inherit(0);
    }

    //基础注解
    private B base(Object source) {
        return AnnotationUtils.findAnnotation(source.getClass(), (Class<? extends B>) inherit(2));
    }

    //方法上的注解
    private T type(Method method) {
        return AnnotationUtils.findAnnotation(method, (Class<? extends T>) inherit(3));
    }

    //方法映射关系
    private List<Pair<Parameter, Class<?>>> mapping(Method method) {
        Parameter[] ps = method.getParameters();
        Class<?>[] cs = method.getParameterTypes();
        List<Pair<Parameter, Class<?>>> list = Lists.newArrayList();
        for (int i = 0; i < ps.length; ++i) {
            list.add(new Pair<>(ps[i], cs[i]));
        }
        return list;
    }

    //映射关系:对象-方法-注解-注解
    private Stream<Mapping<B, T>> mapping(Object source) {
        Method[] allDeclaredMethods = ReflectionUtils.getDeclaredMethods(source.getClass());
        return Arrays.stream(allDeclaredMethods)
                .filter(method -> Objects.nonNull(type(method)))
                .map(method -> {
                    Mapping<B, T> mapping = new Mapping<>();
                    mapping.setBase(base(source));
                    mapping.setType(type(method));
                    mapping.setSource(source);
                    mapping.setMethod(method);
                    return mapping;
                });
    }

    //map为空时,设置初始的分支类型
    protected abstract K defaultKey(K currentKey);

    //根据注解封装key集合
    protected abstract List<K> key(B base, T type);

    //封装参数集
    protected abstract Object[] args(List<Pair<Parameter, Class<?>>> mapping, P source);

    //主流程
    public Object apply(K key, P source) {
        //通过key获取映射关系
        Mapping<B, T> mapping = this.mapping.get(key);
        //设置默认值
        if (Objects.isNull(mapping)) {
            mapping = this.mapping.get(defaultKey(key));
        }
        //如果是空值
        if (Objects.isNull(mapping)) {
            return null;
        }
        //封装请求参数
        Object[] args = this.args(mapping(mapping.getMethod()), source);
        //返回值
        return ReflectionUtils.invokeMethod(mapping.getMethod(), mapping.getSource(), args);
    }

    @Data
    public static class Mapping<B extends Annotation, T extends Annotation> implements Serializable {
        //类上的注解
        private B base;
        //方法上的注解
        private T type;
        //源类
        private Object source;
        //匹配的方法
        private Method method;
    }

}
