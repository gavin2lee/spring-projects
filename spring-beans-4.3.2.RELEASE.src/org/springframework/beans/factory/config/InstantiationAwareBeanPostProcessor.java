package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

public abstract interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor
{
  public abstract Object postProcessBeforeInstantiation(Class<?> paramClass, String paramString)
    throws BeansException;

  public abstract boolean postProcessAfterInstantiation(Object paramObject, String paramString)
    throws BeansException;

  public abstract PropertyValues postProcessPropertyValues(PropertyValues paramPropertyValues, PropertyDescriptor[] paramArrayOfPropertyDescriptor, Object paramObject, String paramString)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
 * JD-Core Version:    0.6.2
 */