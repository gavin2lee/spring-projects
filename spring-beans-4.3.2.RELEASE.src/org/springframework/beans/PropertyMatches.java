/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.FieldCallback;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public abstract class PropertyMatches
/*     */ {
/*     */   public static final int DEFAULT_MAX_DISTANCE = 2;
/*     */   private final String propertyName;
/*     */   private String[] possibleMatches;
/*     */ 
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass)
/*     */   {
/*  60 */     return forProperty(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass, int maxDistance)
/*     */   {
/*  70 */     return new BeanPropertyMatches(propertyName, beanClass, maxDistance, null);
/*     */   }
/*     */ 
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass)
/*     */   {
/*  79 */     return forField(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass, int maxDistance)
/*     */   {
/*  89 */     return new FieldPropertyMatches(propertyName, beanClass, maxDistance, null);
/*     */   }
/*     */ 
/*     */   private PropertyMatches(String propertyName, String[] possibleMatches)
/*     */   {
/* 106 */     this.propertyName = propertyName;
/* 107 */     this.possibleMatches = possibleMatches;
/*     */   }
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/* 114 */     return this.propertyName;
/*     */   }
/*     */ 
/*     */   public String[] getPossibleMatches()
/*     */   {
/* 121 */     return this.possibleMatches;
/*     */   }
/*     */ 
/*     */   public abstract String buildErrorMessage();
/*     */ 
/*     */   protected void appendHintMessage(StringBuilder msg)
/*     */   {
/* 131 */     msg.append("Did you mean ");
/* 132 */     for (int i = 0; i < this.possibleMatches.length; i++) {
/* 133 */       msg.append('\'');
/* 134 */       msg.append(this.possibleMatches[i]);
/* 135 */       if (i < this.possibleMatches.length - 2) {
/* 136 */         msg.append("', ");
/*     */       }
/* 138 */       else if (i == this.possibleMatches.length - 2) {
/* 139 */         msg.append("', or ");
/*     */       }
/*     */     }
/* 142 */     msg.append("'?");
/*     */   }
/*     */ 
/*     */   private static int calculateStringDistance(String s1, String s2)
/*     */   {
/* 153 */     if (s1.length() == 0) {
/* 154 */       return s2.length();
/*     */     }
/* 156 */     if (s2.length() == 0) {
/* 157 */       return s1.length();
/*     */     }
/* 159 */     int[][] d = new int[s1.length() + 1][s2.length() + 1];
/*     */ 
/* 161 */     for (int i = 0; i <= s1.length(); i++) {
/* 162 */       d[i][0] = i;
/*     */     }
/* 164 */     for (int j = 0; j <= s2.length(); j++) {
/* 165 */       d[0][j] = j;
/*     */     }
/*     */ 
/* 168 */     for (int i = 1; i <= s1.length(); i++) {
/* 169 */       char s_i = s1.charAt(i - 1);
/* 170 */       for (int j = 1; j <= s2.length(); j++)
/*     */       {
/* 172 */         char t_j = s2.charAt(j - 1);
/*     */         int cost;
/*     */         int cost;
/* 173 */         if (s_i == t_j) {
/* 174 */           cost = 0;
/*     */         }
/*     */         else {
/* 177 */           cost = 1;
/*     */         }
/* 179 */         d[i][j] = Math.min(Math.min(d[(i - 1)][j] + 1, d[i][(j - 1)] + 1), d[(i - 1)][(j - 1)] + cost);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 184 */     return d[s1.length()][s2.length()];
/*     */   }
/*     */ 
/*     */   private static class FieldPropertyMatches extends PropertyMatches
/*     */   {
/*     */     private FieldPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance)
/*     */     {
/* 240 */       super(calculateMatches(propertyName, beanClass, maxDistance), null);
/*     */     }
/*     */ 
/*     */     private static String[] calculateMatches(String propertyName, Class<?> beanClass, final int maxDistance) {
/* 244 */       final List candidates = new ArrayList();
/* 245 */       ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback()
/*     */       {
/*     */         public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 248 */           String possibleAlternative = field.getName();
/* 249 */           if (PropertyMatches.calculateStringDistance(this.val$propertyName, possibleAlternative) <= maxDistance)
/* 250 */             candidates.add(possibleAlternative);
/*     */         }
/*     */       });
/* 254 */       Collections.sort(candidates);
/* 255 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     public String buildErrorMessage()
/*     */     {
/* 261 */       String propertyName = getPropertyName();
/* 262 */       String[] possibleMatches = getPossibleMatches();
/* 263 */       StringBuilder msg = new StringBuilder();
/* 264 */       msg.append("Bean property '");
/* 265 */       msg.append(propertyName);
/* 266 */       msg.append("' has no matching field. ");
/*     */ 
/* 268 */       if (!ObjectUtils.isEmpty(possibleMatches)) {
/* 269 */         appendHintMessage(msg);
/*     */       }
/* 271 */       return msg.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class BeanPropertyMatches extends PropertyMatches
/*     */   {
/*     */     private BeanPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance)
/*     */     {
/* 190 */       super(calculateMatches(propertyName, 
/* 191 */         BeanUtils.getPropertyDescriptors(beanClass), 
/* 191 */         maxDistance), null);
/*     */     }
/*     */ 
/*     */     private static String[] calculateMatches(String propertyName, PropertyDescriptor[] propertyDescriptors, int maxDistance)
/*     */     {
/* 203 */       List candidates = new ArrayList();
/* 204 */       for (PropertyDescriptor pd : propertyDescriptors) {
/* 205 */         if (pd.getWriteMethod() != null) {
/* 206 */           String possibleAlternative = pd.getName();
/* 207 */           if (PropertyMatches.calculateStringDistance(propertyName, possibleAlternative) <= maxDistance) {
/* 208 */             candidates.add(possibleAlternative);
/*     */           }
/*     */         }
/*     */       }
/* 212 */       Collections.sort(candidates);
/* 213 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     public String buildErrorMessage()
/*     */     {
/* 219 */       String propertyName = getPropertyName();
/* 220 */       String[] possibleMatches = getPossibleMatches();
/* 221 */       StringBuilder msg = new StringBuilder();
/* 222 */       msg.append("Bean property '");
/* 223 */       msg.append(propertyName);
/* 224 */       msg.append("' is not writable or has an invalid setter method. ");
/*     */ 
/* 226 */       if (ObjectUtils.isEmpty(possibleMatches)) {
/* 227 */         msg.append("Does the parameter type of the setter match the return type of the getter?");
/*     */       }
/*     */       else {
/* 230 */         appendHintMessage(msg);
/*     */       }
/* 232 */       return msg.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyMatches
 * JD-Core Version:    0.6.2
 */