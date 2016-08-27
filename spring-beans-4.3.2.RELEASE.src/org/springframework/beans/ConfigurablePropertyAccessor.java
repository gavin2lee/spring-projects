package org.springframework.beans;

import org.springframework.core.convert.ConversionService;

public abstract interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter
{
  public abstract void setConversionService(ConversionService paramConversionService);

  public abstract ConversionService getConversionService();

  public abstract void setExtractOldValueForEditor(boolean paramBoolean);

  public abstract boolean isExtractOldValueForEditor();

  public abstract void setAutoGrowNestedPaths(boolean paramBoolean);

  public abstract boolean isAutoGrowNestedPaths();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.ConfigurablePropertyAccessor
 * JD-Core Version:    0.6.2
 */