package cn.org.alice.config;

import net.vidageek.mirror.dsl.Mirror;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public Mirror mirror() {
        return new Mirror();
    }
}
