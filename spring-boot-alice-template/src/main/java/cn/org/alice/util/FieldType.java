package cn.org.alice.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 * @author 爱莉丝
 * @since 2021/10/18 17:36
 * @apiNote 字段类型
 **/
public interface FieldType {

    /**
     * 根据字段获取其泛型类型。如果字段是泛型类型，返回其实际类型参数中的第一个类类型；否则返回null。
     *
     * @param field 需要检查的字段
     * @return 字段的泛型类型中的第一个类类型，如果没有泛型类型或不是类类型则返回null
     */
    default Class<?> actualType(Field field) {
        // 获取字段的泛型类型
        Type type = field.getGenericType();
        // 如果泛型类型是参数化类型（即泛型类型被实际类型参数化了）
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            // 获取参数化类型的实际类型参数数组
            Type[] actualTypes = paramType.getActualTypeArguments();
            // 遍历实际类型参数数组
            for (Type item : actualTypes) {
                // 如果类型是类类型
                if (item instanceof Class) {
                    // 返回该类类型
                    return (Class<?>) item;
                }
            }
        }
        // 如果没有找到类类型，返回null
        return null;
    }


}
