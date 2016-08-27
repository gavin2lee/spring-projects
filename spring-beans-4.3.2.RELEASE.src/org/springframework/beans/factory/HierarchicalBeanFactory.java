package org.springframework.beans.factory;

public abstract interface HierarchicalBeanFactory extends BeanFactory
{
  public abstract BeanFactory getParentBeanFactory();

  public abstract boolean containsLocalBean(String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.HierarchicalBeanFactory
 * JD-Core Version:    0.6.2
 */