package com.futechsoft.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.futechsoft.framework.security.auth.CsrfSecurityRequestMatcher;
import com.futechsoft.framework.security.auth.CustomAuthenticationProvider;
import com.futechsoft.framework.security.auth.CustomFilterInvocationSecurityMetadataSource;
import com.futechsoft.framework.security.auth.CustomUserDetailsService;
import com.futechsoft.framework.security.filter.AjaxSecurityFilter;
import com.futechsoft.framework.security.handler.CustomAccessDeniedHandler;
import com.futechsoft.framework.security.handler.CustomLogoutSuccessHandler;
import com.futechsoft.framework.security.handler.LoginFailureHandler;
import com.futechsoft.framework.security.handler.LoginSuccessHandler;

@Configuration 
@EnableWebSecurity 
//@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

 
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    	authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
    	authenticationManagerBuilder.userDetailsService(userDetailsService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      
      
        web.ignoring()
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
      // web.ignoring() .antMatchers("/resources/**");
       
      // web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
        		.and()
        		 .anonymous()
                 .authorities("ROLE_ANONYMOUS")
                 .and()
        		.csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher())
        		.and()
                .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin() 
                .loginPage("/login/loginPage")          
                .loginProcessingUrl("/auth")
                .usernameParameter("userId")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll() 
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())     // 인증 거부 관련 처리
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            .and()
                .addFilterBefore(customFilterSecurityInterceptor(),FilterSecurityInterceptor.class)
                .addFilterAfter(ajaxSecurityFilter(), ExceptionTranslationFilter.class);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
    	
    	CustomAccessDeniedHandler accessDeniedHandler =new CustomAccessDeniedHandler();
        accessDeniedHandler.setRedirect(false);
        accessDeniedHandler.setErrorPage("/accessdenied");
        return accessDeniedHandler;
    }
    
    
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
    	LoginSuccessHandler loginSuccessHandler =new LoginSuccessHandler();
    	loginSuccessHandler.setLoginId("userId");
    	loginSuccessHandler.setDefaultUrl("/main");
        return loginSuccessHandler;
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
    	LoginFailureHandler loginFailureHandler =  new LoginFailureHandler();
    	loginFailureHandler.setLoginId("userId");
    	loginFailureHandler.setLoginPwd("password");
    	loginFailureHandler.setErrorMsg("ERRORMSG");
    	loginFailureHandler.setDefaultFailureUrl("/login/loginPage?error");
        return loginFailureHandler;
    }
    
    
    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
    	CustomLogoutSuccessHandler customLogoutSuccessHandler = new CustomLogoutSuccessHandler();
    	customLogoutSuccessHandler.setDefaultUrl("/login/loginPage");
        return customLogoutSuccessHandler;
    }
    
    
    @Bean
    public  CustomUserDetailsService userDetailsService(){
    	return new CustomUserDetailsService();
    }

    @Bean
    public  BCryptPasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }
  
    @Bean 
    public CustomAuthenticationProvider customAuthenticationProvider() { 
    	return new CustomAuthenticationProvider(); 
    }
    
   // @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource());   // 권한정보 셋팅
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());    // 인증매니저
        return filterSecurityInterceptor;
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters());
        return affirmativeBased;
    }


    @Bean
    public List<AccessDecisionVoter<? extends Object>> accessDecisionVoters() {
        return Arrays.asList(new RoleVoter(), new AuthenticatedVoter(), new WebExpressionVoter() );
    }


   
    @Bean
    public CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource() throws Exception {
        return new CustomFilterInvocationSecurityMetadataSource();
    }

    
    @Bean
    public CsrfSecurityRequestMatcher csrfSecurityRequestMatcher() { 
    	return new CsrfSecurityRequestMatcher(); 
    }
   
    
    private AjaxSecurityFilter ajaxSecurityFilter() { 
    	AjaxSecurityFilter ajaxSecurityFilter = new AjaxSecurityFilter();
    	ajaxSecurityFilter.setAjaxHeader("AJAX");
    	return ajaxSecurityFilter;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    	
}