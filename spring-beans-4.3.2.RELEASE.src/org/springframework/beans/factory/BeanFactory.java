package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

public abstract interface BeanFactory
{
  public static final String FACTORY_BEAN_PREFIX = "&";

  public abstract Object getBean(String paramString)
    throws BeansException;

  public abstract <T> T getBean(String paramString, Class<T> paramClass)
    throws BeansException;

  public abstract <T> T getBean(Class<T> paramClass)
    throws BeansException;

  public abstract Object getBean(String paramString, Object[] paramArrayOfObject)
    throws BeansException;

  public abstract <T> T getBean(Class<T> paramClass, Object[] paramArrayOfObject)
    throws BeansException;

  public abstract boolean containsBean(String paramString);

  public abstract boolean isSingleton(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract boolean isPrototype(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract boolean isTypeMatch(String paramString, ResolvableType paramResolvableType)
    throws NoSuchBeanDefinitionException;

  public abstract boolean isTypeMatch(String paramString, Class<?> paramClass)
    throws NoSuchBeanDefinitionException;

  public abstract Class<?> getType(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract String[] getAliases(String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanFactory
 * JD-Core Version:    0.6.2
 */