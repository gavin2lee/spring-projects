package org.springframework.beans;

import java.beans.PropertyDescriptor;

public abstract interface BeanWrapper extends ConfigurablePropertyAccessor
{
  public abstract void setAutoGrowCollectionLimit(int paramInt);

  public abstract int getAutoGrowCollectionLimit();

  public abstract Object getWrappedInstance();

  public abstract Class<?> getWrappedClass();

  public abstract PropertyDescriptor[] getPropertyDescriptors();

  public abstract PropertyDescriptor getPropertyDescriptor(String paramString)
    throws InvalidPropertyException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.BeanWrapper
 * JD-Core Version:    0.6.2
 */