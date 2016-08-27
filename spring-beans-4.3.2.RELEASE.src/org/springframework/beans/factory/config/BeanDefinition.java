package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

public abstract interface BeanDefinition extends AttributeAccessor, BeanMetadataElement
{
  public static final String SCOPE_SINGLETON = "singleton";
  public static final String SCOPE_PROTOTYPE = "prototype";
  public static final int ROLE_APPLICATION = 0;
  public static final int ROLE_SUPPORT = 1;
  public static final int ROLE_INFRASTRUCTURE = 2;

  public abstract String getParentName();

  public abstract void setParentName(String paramString);

  public abstract String getBeanClassName();

  public abstract void setBeanClassName(String paramString);

  public abstract String getFactoryBeanName();

  public abstract void setFactoryBeanName(String paramString);

  public abstract String getFactoryMethodName();

  public abstract void setFactoryMethodName(String paramString);

  public abstract String getScope();

  public abstract void setScope(String paramString);

  public abstract boolean isLazyInit();

  public abstract void setLazyInit(boolean paramBoolean);

  public abstract String[] getDependsOn();

  public abstract void setDependsOn(String[] paramArrayOfString);

  public abstract boolean isAutowireCandidate();

  public abstract void setAutowireCandidate(boolean paramBoolean);

  public abstract boolean isPrimary();

  public abstract void setPrimary(boolean paramBoolean);

  public abstract ConstructorArgumentValues getConstructorArgumentValues();

  public abstract MutablePropertyValues getPropertyValues();

  public abstract boolean isSingleton();

  public abstract boolean isPrototype();

  public abstract boolean isAbstract();

  public abstract int getRole();

  public abstract String getDescription();

  public abstract String getResourceDescription();

  public abstract BeanDefinition getOriginatingBeanDefinition();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanDefinition
 * JD-Core Version:    0.6.2
 */