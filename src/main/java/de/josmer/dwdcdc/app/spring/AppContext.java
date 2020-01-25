package de.josmer.dwdcdc.app.spring;

import de.josmer.dwdcdc.app.Application;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.library.interfaces.ISolRadCrawler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component(BeanNames.APP_CONTEXT)
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

    public static IIrradiationCaching getIIrradiationCaching() {
        return Application.isDemoMode() ? (IIrradiationCaching) appContext.getBean(BeanNames.NO_IRRADIATION_CACHING)
                : (IIrradiationCaching) appContext.getBean(BeanNames.IRRADIATION_RAM_CACHING);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
