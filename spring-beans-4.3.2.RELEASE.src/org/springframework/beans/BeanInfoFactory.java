package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public abstract interface BeanInfoFactory
{
  public abstract BeanInfo getBeanInfo(Class<?> paramClass)
    throws IntrospectionException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.BeanInfoFactory
 * JD-Core Version:    0.6.2
 */