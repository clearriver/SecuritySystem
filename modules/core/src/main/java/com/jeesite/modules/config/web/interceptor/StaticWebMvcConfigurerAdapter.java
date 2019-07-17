package com.jeesite.modules.config.web.interceptor;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticWebMvcConfigurerAdapter implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String gitPath = path.getParentFile().getParentFile().getParent() + File.separator + "static" + File.separator ;
		System.out.println("gitPath==>"+gitPath);
		registry.addResourceHandler("/web/**").addResourceLocations(gitPath);
//		registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
	}
}
