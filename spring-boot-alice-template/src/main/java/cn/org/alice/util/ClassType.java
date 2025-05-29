package cn.org.alice.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 * @author 爱莉丝
 * @since 2021/9/10 10:35
 * @apiNote 获取泛型类型
 **/
public interface ClassType<T> {

    default Class<T> realize(Class<?> clazz, Integer index) {
        return (Class<T>) realizeType(clazz, index);
    }

    default Class<T> realize(Class<?> base, Class<?> clazz, Integer index) {
        return (Class<T>) realizeType(base, clazz, index);
    }

    default Class<T> realize(Class<?> base, Class<?> clazz) {
        return (Class<T>) realizeType(base, clazz);
    }

    default Class<T> realize(Class<?> clazz) {
        return realize(clazz, 0);
    }

    default Type realizeType(Class<?> clazz, Integer index) {
        return realizeType(this.getClass(), clazz, index);
    }

    default Type realizeType(Class<?> base, Class<?> clazz) {
        return realizeType(base, clazz, 0);
    }

    default Class<?> realizeType(Class<?> base, Class<?> clazz, Integer index) {
        // 遍历所有接口
        for (Type type : base.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                if (pt.getRawType() == clazz) {
                    Type[] args = pt.getActualTypeArguments();
                    if (index >= 0 && index < args.length) {
                        return (Class<?>) args[index];
                    }
                }
            }
            // 递归检查接口的父接口
            for (Class<?> iface : base.getInterfaces()) {
                Class<?> result = realizeType(iface, clazz, index);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    default Class<T> inherit(Integer index) {
        return (Class<T>) inheritType(index);
    }

    default Class<T> inherit(Class<?> clazz) {
        return (Class<T>) inheritType(clazz);
    }

    default Class<T> inherit() {
        return inherit(0);
    }

    default Type inheritType(Integer index) {
        return inheritType(this.getClass(), index);
    }

    default Type inheritType(Class<?> clazz) {
        return inheritType(clazz, 0);
    }

    default Type inheritType(Class<?> clazz, Integer index) {
        Type type = clazz.getGenericSuperclass();
        String typeName = type.getTypeName();
        Assert.isTrue(!StringUtils.equals("java.lang.Object", typeName), "没有子类,请定义子类");
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return types[index];
    }

}
