package com.example.timesheet_server.config;

import com.example.timesheet_server.security.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class FilterConfig {
    @Value("${services.auth.login}")
    private String authLoginUrl;

    @Bean
    public FilterRegistrationBean<LoginFilter> LoginFilter() {
        final FilterRegistrationBean<LoginFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginFilter());
        filterFilterRegistrationBean.setInitParameters(Collections.singletonMap("authLoginUrl", authLoginUrl));
        // The difference between /* & /** is that the second matches the entire directory tree, including subdirectories, where as /* only matches at the level it's specified at.
        filterFilterRegistrationBean.addUrlPatterns("/timesheet-server/*");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}
