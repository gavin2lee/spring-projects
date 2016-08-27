package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract interface BeanNameGenerator
{
  public abstract String generateBeanName(BeanDefinition paramBeanDefinition, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanNameGenerator
 * JD-Core Version:    0.6.2
 */