package com.mytest.MyApp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("secret.properties")
public class MainConfig {
}
