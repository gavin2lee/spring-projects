package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public abstract interface AutowireCandidateResolver
{
  public abstract boolean isAutowireCandidate(BeanDefinitionHolder paramBeanDefinitionHolder, DependencyDescriptor paramDependencyDescriptor);

  public abstract Object getSuggestedValue(DependencyDescriptor paramDependencyDescriptor);

  public abstract Object getLazyResolutionProxyIfNecessary(DependencyDescriptor paramDependencyDescriptor, String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.AutowireCandidateResolver
 * JD-Core Version:    0.6.2
 */