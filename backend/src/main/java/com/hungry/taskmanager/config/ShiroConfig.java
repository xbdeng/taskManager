package com.hungry.taskmanager.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import com.hungry.taskmanager.shiro.cache.RedisCacheManager;
import com.hungry.taskmanager.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * 用来整合shiro框架相关的配置类
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    //1.创建shiroFilter  //负责拦截所有请求
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //配置系统受限资源
        //配置系统公共资源
        LinkedHashMap<String, String> filterChainDefinitions = new LinkedHashMap<>(10); //设置容量

        // 登录接口和注册放开
        filterChainDefinitions.put("/api/user/login", "anon");
        filterChainDefinitions.put("/api/user/register", "anon");
        filterChainDefinitions.put("/swagger-ui/**", "anon");
        filterChainDefinitions.put("/swagger-resources/**", "anon");
        filterChainDefinitions.put("/v2/api-docs", "anon");
        // 其他请求拦截 （认证授权）
        filterChainDefinitions.put("/**", "authc");

        //默认认证界面路径
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitions);
        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(realm);

        return defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean
    public Realm getRealm() {
        CustomerRealm customerRealm = new CustomerRealm();

        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);


        //开启缓存管理
//        customerRealm.setCacheManager(new RedisCacheManager());
//        customerRealm.setCachingEnabled(true);//开启全局缓存
//        customerRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
//        customerRealm.setAuthenticationCacheName("authenticationCache");
//        customerRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
//        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }

}
