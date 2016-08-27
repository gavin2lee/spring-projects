package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;

public abstract interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor
{
  public abstract Class<?> predictBeanType(Class<?> paramClass, String paramString)
    throws BeansException;

  public abstract Constructor<?>[] determineCandidateConstructors(Class<?> paramClass, String paramString)
    throws BeansException;

  public abstract Object getEarlyBeanReference(Object paramObject, String paramString)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor
 * JD-Core Version:    0.6.2
 */