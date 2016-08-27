package org.springframework.beans;

public abstract interface PropertyValues
{
  public abstract PropertyValue[] getPropertyValues();

  public abstract PropertyValue getPropertyValue(String paramString);

  public abstract PropertyValues changesSince(PropertyValues paramPropertyValues);

  public abstract boolean contains(String paramString);

  public abstract boolean isEmpty();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyValues
 * JD-Core Version:    0.6.2
 */