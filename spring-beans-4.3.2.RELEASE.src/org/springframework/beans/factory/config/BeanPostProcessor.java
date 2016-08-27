package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanPostProcessor
{
  public abstract Object postProcessBeforeInitialization(Object paramObject, String paramString)
    throws BeansException;

  public abstract Object postProcessAfterInitialization(Object paramObject, String paramString)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanPostProcessor
 * JD-Core Version:    0.6.2
 */