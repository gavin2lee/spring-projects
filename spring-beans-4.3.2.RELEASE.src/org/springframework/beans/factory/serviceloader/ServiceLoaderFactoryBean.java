/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ 
/*    */ public class ServiceLoaderFactoryBean extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader)
/*    */   {
/* 35 */     return serviceLoader;
/*    */   }
/*    */ 
/*    */   public Class<?> getObjectType()
/*    */   {
/* 40 */     return ServiceLoader.class;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean
 * JD-Core Version:    0.6.2
 */