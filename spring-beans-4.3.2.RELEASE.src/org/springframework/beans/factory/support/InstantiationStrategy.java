package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract interface InstantiationStrategy
{
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory)
    throws BeansException;

  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Constructor<?> paramConstructor, Object[] paramArrayOfObject)
    throws BeansException;

  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.InstantiationStrategy
 * JD-Core Version:    0.6.2
 */