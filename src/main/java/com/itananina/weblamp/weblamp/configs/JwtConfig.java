package com.itananina.weblamp.weblamp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/configs/secrets.properties")
public class JwtConfig {
}
