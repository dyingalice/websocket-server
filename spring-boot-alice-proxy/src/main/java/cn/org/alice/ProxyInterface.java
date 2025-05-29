package cn.org.alice;

import cn.org.alice.bean.Pair;
import cn.org.alice.service.TemplateService;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Getter
@Builder
public class ProxyInterface implements Serializable {

    /***
     * 方法信息
     */
    private Method method;

    /**
     * 参数
     */
    private Object[] args;

    /**
     * 方法信息与值的集
     */
    private List<Pair<Parameter, Object>> pairs;

    /**
     * 模板
     */
    private TemplateService template;

    /**
     * 返回值
     */
    private Class<?> returnType;

    /**
     * 代理类
     */
    private Object proxy;

    /**
     * 声明类
     */
    private Class<?> clazz;

}
