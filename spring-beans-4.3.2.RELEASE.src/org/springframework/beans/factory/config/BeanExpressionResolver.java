package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanExpressionResolver
{
  public abstract Object evaluate(String paramString, BeanExpressionContext paramBeanExpressionContext)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanExpressionResolver
 * JD-Core Version:    0.6.2
 */