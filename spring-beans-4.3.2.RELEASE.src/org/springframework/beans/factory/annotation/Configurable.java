package org.springframework.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Configurable
{
  public abstract String value();

  public abstract Autowire autowire();

  public abstract boolean dependencyCheck();

  public abstract boolean preConstruction();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.Configurable
 * JD-Core Version:    0.6.2
 */