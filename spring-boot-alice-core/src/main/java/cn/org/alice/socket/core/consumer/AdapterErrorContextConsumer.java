package cn.org.alice.socket.core.consumer;

import cn.org.alice.AdapterContextConsumer;
import cn.org.alice.bean.Pair;
import cn.org.alice.socket.core.anno.WssControllerAdvice;
import cn.org.alice.socket.core.anno.WssExceptionHandler;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.anno.Void;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdapterErrorContextConsumer extends AdapterContextConsumer<WssControllerAdvice, Class<? extends Throwable>, Void, WssExceptionHandler, Pair<String, Throwable>> {

    @Override
    protected Class<? extends Throwable> defaultKey(Class<? extends Throwable> currentKey) {
        return Exception.class;
    }

    @Override
    protected List<Class<? extends Throwable>> key(Void base, WssExceptionHandler type) {
        return Arrays.stream(type.value()).collect(Collectors.toList());
    }

    @Override
    protected Object[] args(List<Pair<Parameter, Class<?>>> mapping, Pair<String, Throwable> source) {
        return mapping.stream()
                .map(e -> {
                    if (exception(source.getValue(), e.getKey().getType())) {
                        return source.getValue();
                    }
                    if (Objects.nonNull(e.getKey().getAnnotation(WssPrimary.class))) {
                        return source.getKey();
                    }
                    return null;
                })
                .toArray();
    }

    private boolean exception(Throwable error, Class<?> target) {
        int index = ExceptionUtils.indexOfThrowable(error, target);
        return index != -1;
    }

}
