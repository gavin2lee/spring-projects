package org.springframework.beans;

import java.beans.PropertyEditor;

public abstract interface PropertyEditorRegistry
{
  public abstract void registerCustomEditor(Class<?> paramClass, PropertyEditor paramPropertyEditor);

  public abstract void registerCustomEditor(Class<?> paramClass, String paramString, PropertyEditor paramPropertyEditor);

  public abstract PropertyEditor findCustomEditor(Class<?> paramClass, String paramString);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyEditorRegistry
 * JD-Core Version:    0.6.2
 */