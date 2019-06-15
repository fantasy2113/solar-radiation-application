package de.josmer.dwdcdc.app.spring;

import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T get(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static Object get(String beanName) {
        return context.getBean(beanName);
    }

    public static ISolRadCrawler getCrawler(String beanName) {
        return (ISolRadCrawler) context.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}

