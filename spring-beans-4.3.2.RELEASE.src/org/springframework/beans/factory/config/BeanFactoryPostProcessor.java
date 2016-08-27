package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryPostProcessor
{
  public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory paramConfigurableListableBeanFactory)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanFactoryPostProcessor
 * JD-Core Version:    0.6.2
 */