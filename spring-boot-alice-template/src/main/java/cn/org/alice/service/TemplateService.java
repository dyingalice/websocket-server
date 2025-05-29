package cn.org.alice.service;

import cn.org.alice.anno.TplKey;
import cn.org.alice.bean.Pair;
import cn.org.alice.exception.TemplateException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface TemplateService {

    /**
     * 向容器内添加值
     */
    TemplateService put(String key, Object value);

    /**
     * 向容器内添加值
     */
    default TemplateService put(Map<String, Object> params) {
        params.keySet().forEach(key -> this.put(key, params.get(key)));
        return this;
    }

    /**
     * 向容器内添加值
     */
    default TemplateService put(Supplier<Map<String, Object>> supplier) {
        return this.put(supplier.get());
    }

    /**
     * 向容器内添加值
     */
    default TemplateService put(Method method, Object[] args) {
        pair(method, args).forEach((e) -> {
            TplKey tplKey = e.getKey().getAnnotation(TplKey.class);
            if (Objects.nonNull(tplKey)) {
                this.put(tplKey.value(), e.getValue());
            }
        });
        return this;
    }

    /**
     * 获取方法上的参数印射值
     */
    default List<Pair<Parameter, Object>> pair(Method method, Object[] args) {
        Parameter[] ps = method.getParameters();
        List<Pair<Parameter, Object>> list = Lists.newArrayList();
        for (int i = 0; i < ps.length; i++) {
            list.add(new Pair<>(ps[i], args[i]));
        }
        return list;
    }

    /**
     * 获取对应的注解的参数值
     */
    default <A extends Annotation> List<Pair<Parameter, Object>> values(List<Pair<Parameter, Object>> pairs, Class<A> clazz) {
        List<Pair<Parameter, Object>> collect = pairs.stream()
                .filter(e -> e.getKey().isAnnotationPresent(clazz))
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取对应的注解的参数值
     */
    default <A extends Annotation> Pair<Parameter, Object> valuesFirst(List<Pair<Parameter, Object>> pvs, Class<A> clazz) {
        List<Pair<Parameter, Object>> collect = pvs.stream()
                .filter(e -> e.getKey().isAnnotationPresent(clazz))
                .collect(Collectors.toList());
        return collect.stream().findFirst().orElse(null);
    }

    /**
     * 根据表达式获取值
     */
    String value(String expression);

    /**
     * 返回null值
     */
    default String valueNull(String expression) {
        String value = value(expression);
        if (Objects.equals(expression, value)) {
            return null;
        }
        return value;
    }

    Pattern r = Pattern.compile("(?<=\\{)[^}]*(?=})");

    /**
     * 表达式替换占位符
     */
    default String value(String expression, String start, String end) {
        String exp = expression
                .replaceAll(Matcher.quoteReplacement(start), Matcher.quoteReplacement("${"))
                .replaceAll(Matcher.quoteReplacement(end), Matcher.quoteReplacement("}"));
        String result = this.value(exp);
        if (StringUtils.equals(exp, result)) {
            Matcher m = r.matcher(exp);
            while (m.find()) {
                throw new TemplateException(m.group(0) + " is empty ");
            }
        }
        return result;
    }

    /**
     * 根据表达式获取值
     */
    default String value(Supplier<String> supplier) {
        return this.value(supplier.get());
    }

    /**
     * 根据表达式获取值
     */
    default List<String> values(String[] expressions) {
        return this.values(Arrays.asList(expressions));
    }

    /**
     * 根据表达式获取值
     */
    default List<String> values(List<String> expressions) {
        return expressions.stream().map(this::value).collect(Collectors.toList());
    }

    /**
     * 根据表达式获取值
     */
    default List<String> values(Supplier<List<String>> supplier) {
        return this.values(supplier.get());
    }

    /**
     * 排接key值
     */
    default String key(List<String> keys, String split) {
        return StringUtils.join(keys.toArray(), split);
    }

    /**
     * 排接key值
     */
    default String key(String[] keys, String split) {
        return key(Arrays.asList(keys), split);
    }

    /**
     * 关闭资源
     */
    void close();

}
