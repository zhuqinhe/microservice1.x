package com.reminder.utils;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

	// json转换Bean工具
	public static ObjectMapper jsonMap = new ObjectMapper();
	static {
		// json字符串中有的属性，在Bean中没有，转换成Bean时不报错
		jsonMap.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		jsonMap.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); //属性为NULL不序列化

	}

	/**
	 * 将对象转换成json字符串
	 */
	public static String obj2Json(Object obj) {
		StringWriter writer = new StringWriter();
		JsonGenerator json = null;
		String re = null;
		try {
			json = new JsonFactory().createJsonGenerator(writer);
			jsonMap.writeValue(json, obj);
			re = writer.toString();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (json != null) {
					json.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return re;
	}

	/**
	 * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * 转换为普通JavaBean：readValue(json,Student.class)
	 * 转换为List:readValue(json,List.class).但是如果我们想把json转换为特定类型的List，比如List<Student>，就不能直接进行转换了。
	 * 因为readValue(json,List.class)返回的其实是List<Map>类型，你不能指定readValue()的第二个参数是List<Student>.class，所以不能直接转换。
	 * 我们可以把readValue()的第二个参数传递为Student[].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List。
	 * 转换为Map：readValue(json,Map.class) 我们使用泛型，得到的也是泛型
	 *
	 * @param jsonStr
	 *            要转换的JavaBean类型
	 * @param valueType
	 *            原始json字符串数据
	 * @return JavaBean对象
	 */
	public static <T> T json2Object(String jsonStr, Class<T> valueType) {
		if (StringUtils.isEmpty(jsonStr)) {
			return null;
		}
		if (jsonMap == null) {
			jsonMap = new ObjectMapper();
		}
		try {
			return jsonMap.readValue(jsonStr, valueType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}

		return null;
	}

}
