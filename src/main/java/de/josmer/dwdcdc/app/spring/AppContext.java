package de.josmer.dwdcdc.app.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import de.josmer.dwdcdc.library.interfaces.ISolRadCrawler;

@Component
public class AppContext implements ApplicationContextAware {

	private static ApplicationContext appContext;

	public static <T> T getBean(Class<T> beanClass) {
		return appContext.getBean(beanClass);
	}

	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}

	public static ISolRadCrawler getCrawler(String beanName) {
		return (ISolRadCrawler) appContext.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
}
