package com.actec.bsms;

import com.actec.bsms.backend.ServiceManager;
import com.actec.bsms.config.JerseyConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.log4j.Logger;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;

@EnableAutoConfiguration
@SpringBootApplication
@ServletComponentScan(value = "com.actec.bsms")
@ComponentScan
public class BsmsApplication {

	private static Logger logger = Logger.getLogger(BsmsApplication.class);

	/**
	 * Start
	 */
	public static void main(String[] args) {
		SpringApplication.run(BsmsApplication.class, args);
		ServiceManager.contextInitialized();
		logger.info("SpringBoot Start Success");
	}

	/**
	 * Jersey配置
	 */
	@Bean
	public ServletRegistrationBean jerseyServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rs/*");
		// our rest resources will be available in the path /rest/*
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
		return registration;
	}

	/**
	 * FastJson配置
	 */
	@Bean
	public HttpMessageConverters fastJsonConverters(){
		FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastConf = new FastJsonConfig();

		fastConf.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConverter.setFastJsonConfig(fastConf);

		HttpMessageConverter<?> converter = fastJsonConverter;
		return new HttpMessageConverters(converter);
	}

//	@Bean
//	public EmbeddedServletContainerFactory servletContainer() {
//
//		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//
//			@Override
//			protected void postProcessContext(Context context) {
//
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//		return tomcat;
//	}
//
//	private Connector initiateHttpConnector() {
//		//Http 8168端口请求重定向到Https 8188端口
//		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//		connector.setScheme("http");
//		connector.setPort(8168);
//		connector.setSecure(false);
//		connector.setRedirectPort(8188);
//		return connector;
//	}

}
