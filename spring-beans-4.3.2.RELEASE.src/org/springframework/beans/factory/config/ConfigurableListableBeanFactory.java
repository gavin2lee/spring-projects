package org.springframework.beans.factory.config;

import java.util.Iterator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public abstract interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory
{
  public abstract void ignoreDependencyType(Class<?> paramClass);

  public abstract void ignoreDependencyInterface(Class<?> paramClass);

  public abstract void registerResolvableDependency(Class<?> paramClass, Object paramObject);

  public abstract boolean isAutowireCandidate(String paramString, DependencyDescriptor paramDependencyDescriptor)
    throws NoSuchBeanDefinitionException;

  public abstract BeanDefinition getBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;

  public abstract Iterator<String> getBeanNamesIterator();

  public abstract void clearMetadataCache();

  public abstract void freezeConfiguration();

  public abstract boolean isConfigurationFrozen();

  public abstract void preInstantiateSingletons()
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * JD-Core Version:    0.6.2
 */