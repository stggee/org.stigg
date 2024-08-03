package com.stigg.lernwortspiel.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;


@Configuration(proxyBeanMethods = false)
@EnableJdbcHttpSession
public class SessionConfig {

}