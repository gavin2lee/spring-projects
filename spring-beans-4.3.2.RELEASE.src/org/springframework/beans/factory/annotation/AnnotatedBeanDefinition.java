package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

public abstract interface AnnotatedBeanDefinition extends BeanDefinition
{
  public abstract AnnotationMetadata getMetadata();

  public abstract MethodMetadata getFactoryMethodMetadata();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
 * JD-Core Version:    0.6.2
 */