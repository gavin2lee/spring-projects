/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ 
/*    */ public class ServiceFactoryBean extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader)
/*    */   {
/* 37 */     Iterator it = serviceLoader.iterator();
/* 38 */     if (!it.hasNext())
/*    */     {
/* 40 */       throw new IllegalStateException("ServiceLoader could not find service for type [" + 
/* 40 */         getServiceType() + "]");
/*    */     }
/* 42 */     return it.next();
/*    */   }
/*    */ 
/*    */   public Class<?> getObjectType()
/*    */   {
/* 47 */     return getServiceType();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceFactoryBean
 * JD-Core Version:    0.6.2
 */