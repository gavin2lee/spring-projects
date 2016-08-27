package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

public abstract interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
{
  public abstract void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry paramBeanDefinitionRegistry)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
 * JD-Core Version:    0.6.2
 */