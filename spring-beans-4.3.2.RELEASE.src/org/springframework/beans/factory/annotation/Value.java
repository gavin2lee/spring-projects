package org.springframework.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value
{
  public abstract String value();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.Value
 * JD-Core Version:    0.6.2
 */