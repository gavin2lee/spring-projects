package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface DestructionAwareBeanPostProcessor extends BeanPostProcessor
{
  public abstract void postProcessBeforeDestruction(Object paramObject, String paramString)
    throws BeansException;

  public abstract boolean requiresDestruction(Object paramObject);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
 * JD-Core Version:    0.6.2
 */