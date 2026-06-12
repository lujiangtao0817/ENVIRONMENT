package com.neusoft.environment.config;

import com.neusoft.environment.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // 公开接口
            .antMatchers("/api/auth/**").permitAll()
            // 公众监督员接口
            .antMatchers("/api/supervisor/**").hasAnyAuthority("ROLE_0", "ROLE_2")
            // 网格员接口
            .antMatchers("/api/gridman/**").hasAnyAuthority("ROLE_1", "ROLE_2")
            // 管理员接口
            .antMatchers("/api/admin/**").hasAuthority("ROLE_2")
            // 决策者/大屏接口
            .antMatchers("/api/statistics/**").hasAnyAuthority("ROLE_2", "ROLE_3")
            // 公开接口
            .antMatchers("/api/announcement/list").permitAll()
            .antMatchers("/api/policy/list", "/api/policy/detail/**").permitAll()
            .antMatchers("/api/dict/**").permitAll()
            // 公告管理（管理员）
            .antMatchers("/api/announcement/admin/**").hasAuthority("ROLE_2")
            // 投诉管理
            .antMatchers("/api/complaint/admin/**").hasAuthority("ROLE_2")
            // 操作日志（管理员）
            .antMatchers("/api/log/**").hasAuthority("ROLE_2")
            // 巡检记录 + 投诉 + 通知（监督员+网格员+管理员）
            .antMatchers("/api/inspection/**", "/api/complaint/**", "/api/notification/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
