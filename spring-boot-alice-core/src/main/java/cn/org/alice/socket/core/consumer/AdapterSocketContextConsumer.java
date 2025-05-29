package cn.org.alice.socket.core.consumer;

import cn.org.alice.AdapterContextConsumer;
import cn.org.alice.bean.Pair;
import cn.org.alice.socket.core.anno.WssClientController;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.anno.WssRequestBody;
import cn.org.alice.socket.core.anno.WssRequestMapping;
import cn.org.alice.socket.core.exception.SocketException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Pair<String, String> key=用户ID, value=发送的内容
 */
public class AdapterSocketContextConsumer extends AdapterContextConsumer<WssClientController, String, WssRequestMapping, WssRequestMapping, Pair<String, String>> {

    @Autowired
    private ObjectMapper om;

    @Override
    protected String defaultKey(String currentKey) {
        throw new SocketException("404 link -> " + currentKey);
    }

    @Override
    protected List<String> key(WssRequestMapping base, WssRequestMapping type) {
        if (Objects.nonNull(base) && base.value().length > 0) {
            return Arrays.stream(base.value())
                    .flatMap(x -> Arrays.stream(type.value()).map(y -> pathComparison(x, y)))
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(type.value()).collect(Collectors.toList());
        }
    }

    private String pathComparison(String path1, String path2) {
        String newPath = pathComparison(path1) + pathComparison(path2);
        newPath = newPath.replace("//", "/");
        // 去掉路径末尾的 '/' 字符（如果长度大于1）
        if (newPath.length() > 1 && newPath.endsWith("/")) {
            newPath = newPath.substring(0, newPath.length() - 1);
        }
        return newPath;
    }

    public static String pathComparison(String path) {
        // 去掉路径前的所有 '/' 字符
        String newPath = StringUtils.trimLeadingCharacter(path, '/');
        // 在路径前添加一个 '/' 字符
        newPath = "/" + newPath;
        // 替换路径中的所有 "//" 为 "/"
        return newPath.replace("//", "/");
    }

    @SneakyThrows
    private Object readValue(String json, Class<?> value) {
        return om.readValue(json, value);
    }

    @Override
    protected Object[] args(List<Pair<Parameter, Class<?>>> mapping, Pair<String, String> source) {
        return mapping.stream()
                .map(e -> {
                    if (Objects.nonNull(e.getKey().getAnnotation(WssRequestBody.class))) {
                        return readValue(source.getValue(), e.getValue());
                    }
                    if (Objects.nonNull(e.getKey().getAnnotation(WssPrimary.class))) {
                        return source.getKey();
                    }
                    return null;
                })
                .toArray();
    }

}
