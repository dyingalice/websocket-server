package cn.org.alice.service.impl;

import cn.org.alice.service.TemplateService;
import lombok.SneakyThrows;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

/**
 * 需要改变对象内的值, 因此使用多例模式
 */
@Scope("prototype")
public class TemplateServiceImpl implements TemplateService {

    /**
     * 单例模式下, 存在并发问题, 使用ThreadLocal解决
     */
    private final ThreadLocal<VelocityContext> velocityContext = ThreadLocal.withInitial(VelocityContext::new);

    @Override
    public TemplateService put(String key, Object value) {
        velocityContext.get().put(key, value);
        return this;
    }

    @SneakyThrows
    @Override
    public String value(String expression) {
        StringWriter writer = new StringWriter();
        VelocityContext velocityContext = this.velocityContext.get();
        Velocity.evaluate(velocityContext, writer, "", expression);
        return writer.toString();
    }

    @Override
    public void close() {
        velocityContext.remove();
    }

}
