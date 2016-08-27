package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

public abstract interface BeanDefinitionRegistry extends AliasRegistry
{
  public abstract void registerBeanDefinition(String paramString, BeanDefinition paramBeanDefinition)
    throws BeanDefinitionStoreException;

  public abstract void removeBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract BeanDefinition getBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract boolean containsBeanDefinition(String paramString);

  public abstract String[] getBeanDefinitionNames();

  public abstract int getBeanDefinitionCount();

  public abstract boolean isBeanNameInUse(String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionRegistry
 * JD-Core Version:    0.6.2
 */