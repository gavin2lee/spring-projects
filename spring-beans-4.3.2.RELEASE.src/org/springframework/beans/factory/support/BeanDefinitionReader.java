package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract interface BeanDefinitionReader
{
  public abstract BeanDefinitionRegistry getRegistry();

  public abstract ResourceLoader getResourceLoader();

  public abstract ClassLoader getBeanClassLoader();

  public abstract BeanNameGenerator getBeanNameGenerator();

  public abstract int loadBeanDefinitions(Resource paramResource)
    throws BeanDefinitionStoreException;

  public abstract int loadBeanDefinitions(Resource[] paramArrayOfResource)
    throws BeanDefinitionStoreException;

  public abstract int loadBeanDefinitions(String paramString)
    throws BeanDefinitionStoreException;

  public abstract int loadBeanDefinitions(String[] paramArrayOfString)
    throws BeanDefinitionStoreException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionReader
 * JD-Core Version:    0.6.2
 */