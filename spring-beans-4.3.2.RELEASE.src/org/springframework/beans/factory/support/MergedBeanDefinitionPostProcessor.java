package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor
{
  public abstract void postProcessMergedBeanDefinition(RootBeanDefinition paramRootBeanDefinition, Class<?> paramClass, String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
 * JD-Core Version:    0.6.2
 */