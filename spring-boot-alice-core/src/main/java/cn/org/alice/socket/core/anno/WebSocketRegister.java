package cn.org.alice.socket.core.anno;

import cn.org.alice.anno.Proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Proxy(WebSocketRegister.class)
public @interface WebSocketRegister {
}
