package com.udcm.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * json解析工具类
 * @author makefu
 * @date 2016年5月10日
 *
 */
public class JsonUtils {
	
	private static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	/** 
     * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。 
     * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。 
     * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。 
     * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。 
     * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。 
     * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。 
     */  
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);//支持 \n \t 
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,true);//支持   双反斜杠的转义
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	
	/**
	 * 解析json数组，如: [{...}]
	 * @param json
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> parseValueList(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return new ArrayList<T>();
		}
		try {
			return objectMapper.readValue(json, getCollectionType(ArrayList.class, clazz));
		} catch (Exception e) {
			log.error(json, e);
			return null;
		}
		
	}
	
	/**
	 * 解析json数组，如: [{...}]
	 * @param json
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> json2ObjList(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return new ArrayList<T>();
		}
		try {
			return objectMapper.readValue(json, getCollectionType(ArrayList.class, clazz));
		} catch (Exception e) {
			log.error(json, e);
			return null;
		}
		
	}
	
	

	/**
	 * 解析集合类型json
	 * 
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	
	
	
	
	/**
	 * json转字符串
	 * 
	 * @param jsonStr
	 * @param classType
	 * @return
	 */
	public static <T> T json2Obj(String jsonStr, Class<T> classType) {
		if(StringUtils.isBlank(jsonStr)){
			return null;
		}
		
		try {
			return objectMapper.readValue(jsonStr, classType);
		} catch (Exception e) {
			log.error(jsonStr, e);
			return null;
		} 
		
	
		
	}
	
	
	
	/**
	 * 解析JSON对象 {...}
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T parseValue(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isNotBlank(json)) {			
			return objectMapper.readValue(json, clazz);
		} else {
			return null;
		}
	}
	

	
	
	/**
	 * 将对象转换成json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String obj2Json(Object obj) {
		if(obj == null){
			return null;
		}
		
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	   
	}
	
	
	/**
	 * 获取ObjectMapper
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return objectMapper;		
	}
	
	/**
	 * 是否有效的json格式
	 * @param json
	 * @return
	 */
	public static boolean isValidJson(String json){
		try {
			if(StringUtils.isEmpty(json)) {
                return false;
            }
			JsonNode jsonNode = objectMapper.readTree(json);
			return jsonNode!=null && (jsonNode.isObject()||jsonNode.isArray());
		} catch (IOException e) {
			return false;
		}
	}	
	
}
