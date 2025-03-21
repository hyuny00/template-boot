package com.futechsoft.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	 
	
	@Bean
	public SessionRegistry sessionRegistry() { 
	    return new SessionRegistryImpl(); 
	}
	
	
	@Bean
	@ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
    public PropertiesConfiguration propertiesConfiguration( @Value("${spring.config.location}") String path) throws ConfigurationException {
	    PropertiesConfiguration configuration = new PropertiesConfiguration( new File(path) );
	    configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
        return configuration;
    }
	
	
	@Bean
    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new XssEscapeServletFilter());
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/*");

        return filterRegistration;
    }
	
	/* spring.http.encoding.force=false
	 @Bean 
	    public FilterRegistrationBean encodingFilterBean() {
	        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
	        filter.setForceEncoding(true);
	        filter.setEncoding("MS949");
	        registrationBean.setFilter(filter);
	        registrationBean.addUrlPatterns("/ms949filterUrl/*");
	        return registrationBean;
	    }
	*/
/*
	@Bean
	  public ViewResolver viewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    
	    resolver.setPrefix("/WEB-INF/view/");
	    resolver.setSuffix(".jsp");
	    
	    return resolver;
	  }	
	*/	
	 

	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	    registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/"); 
	    registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/"); 
	    registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/"); 
	    
		/*
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(60 * 60 * 24 * 365); 
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/").setCachePeriod(60 * 60 * 24 * 365); 
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365); 
       */
		//registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(60 * 60 * 24 * 365); 
	}
	
	/*
	addViewControllers()에서 루트 경로 설정을 삭제하면, 컨트롤러에서 / URL을 처리할 수 있습니다.
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "index.jsp");
    }
  */

}
