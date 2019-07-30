package com.fonsview.metadata.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;


public class ReflectIUtils {

	
	public static void compareEntityFiled(Object entity,Object entityMirror) throws Exception{
		
		for (Class<?> cls = entity.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
			Field[] fields = cls.getDeclaredFields();
			if (null == fields || fields.length == 0) {
                continue;
            }
			for (Field f : fields) {
				if(Modifier.isStatic(f.getModifiers())) {
                    continue; //排除static
                }
				if(Modifier.isFinal(f.getModifiers())) {
                    continue; //排除final
                }
				
				//System.out.println(f.getGenericType());
				
				 Object entityValue = getEntityFieldValue(entity,f.getName());
				 Object mirrorValue = getEntityFieldValue(entityMirror,f.getName());
				 
				if(null==entityValue && null==mirrorValue){
					continue;
				}else if(null!=entityValue && null!=mirrorValue){
					if(entityValue.equals(mirrorValue)){
						continue;
					}else{
						//changed;		
						setEntityFieldValue(entityMirror,f.getName(),mirrorValue); //激活下写方法						
					}
					
				}else{
					//changed 
					setEntityFieldValue(entityMirror,f.getName(),mirrorValue); //激活下写方法
				}
				
			}
		}
			
	}
	
	
	/**
	 * 设置Java Bean 某一个Field值
	 * @param entity
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	private static void setEntityFieldValue(Object entity,String field,Object value) throws Exception{
		for (Class<?> cls = entity.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
			Field[] fields = cls.getDeclaredFields();
			if (null == fields || fields.length == 0) {
                continue;
            }
			for (Field f : fields) {
				if(Modifier.isStatic(f.getModifiers())) {
                    continue; //排除static
                }
				if(Modifier.isFinal(f.getModifiers())) {
                    continue; //排除final
                }
				
				if(f.getName().equals(field)){
					PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), cls);				
					Method wMethod = descriptor.getWriteMethod();					
					wMethod.invoke(entity, value); //激活下写方法
					return;
				}
			}			
		}
		
	}
	
	
	/**
	 * 获取 Java Bean 某一个 Field的值
	 * @param entity
	 * @param field
	 * @return
	 * @throws Exception
	 */
	private static Object getEntityFieldValue(Object entity,String field) throws Exception{
		for (Class<?> cls = entity.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
			Field[] fields = cls.getDeclaredFields();
			if (null == fields || fields.length == 0) {
                continue;
            }
			for (Field f : fields) {
				if(Modifier.isStatic(f.getModifiers())) {
                    continue; //排除static
                }
				if(Modifier.isFinal(f.getModifiers())) {
                    continue; //排除final
                }
				
				if(f.getName().equals(field)){
					PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), cls);
					Method rMethod = descriptor.getReadMethod();		
					Object entityValue = rMethod.invoke(entity);
					return entityValue;
				}	
			}			
		}
		return null;
	}
	
	
	/**
	 * 用于重写JAVA BEAN的to String方法
	 * @param javaBean
	 * @return
	 * @throws Exception
	 */
	public static String toString(Object javaBean) throws Exception{
		
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Class<?> cls = javaBean.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
			Field[] fields = cls.getDeclaredFields();
			if (null == fields || fields.length == 0) {
                continue;
            }
			for (Field f : fields) {
				if(i!=0) {
                    sb.append(",");
                }
				if(Modifier.isStatic(f.getModifiers())) {
                    continue; //排除static
                }
				if(Modifier.isFinal(f.getModifiers())) {
                    continue; //排除final
                }
				
				PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), cls);
				Method rMethod = descriptor.getReadMethod();		
				Object value = rMethod.invoke(javaBean);
				
				sb.append(f.getName()).append(":<").append(value).append(">");
				i++;					
			}			
		}
		return sb.toString();
	}
	
	/**
	 * 比较两个对象指定字段是否都相等  true 相等   false 不相等
	 * @param o1
	 * @param o2
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static boolean compareEntityFields(Object o1,Object o2,List<String> fieldNames) throws Exception{
		if(o1==null || o2==null || fieldNames==null || fieldNames.size()==0) {
			return true;
		}
		for(String fieldName : fieldNames){
			Object value1 = getEntityFieldValue(o1,fieldName);
			Object value2 = getEntityFieldValue(o2,fieldName);
			if(null==value1 && null==value2){
				continue;
			}else if(null!=value1 && null!=value2){
				if(value1.equals(value2)){
					continue;
				}else{
					return false;				
				}
			}else{
				return false;
			}
		}
		return true;
	}
	
}
