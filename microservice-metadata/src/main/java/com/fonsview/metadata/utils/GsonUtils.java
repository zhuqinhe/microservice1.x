package com.fonsview.metadata.utils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonUtils {

	/**
	 * 返回支持数字类型Gson
	 * @return
	 */
	public static Gson gson4Num() {
		JsonSerializer<Number> numberJsonSerializer = new JsonSerializer<Number>() {
		    @Override
		    public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
//		        return new JsonPrimitive(String.valueOf(src));
		        if (src.floatValue() == 0.0) {
                    return new JsonPrimitive(0.0);
                } else if (src.doubleValue() < 0.01) {
                    return new JsonPrimitive(src);
                } else {
                	return new JsonPrimitive((new BigDecimal(src.floatValue())).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
		    }
		};
		Gson gson = new GsonBuilder()
//		        .registerTypeAdapter(Integer.class, numberJsonSerializer)
//		        .registerTypeAdapter(Long.class, numberJsonSerializer)
		        .registerTypeAdapter(Float.class, numberJsonSerializer)
		        .registerTypeAdapter(Double.class, numberJsonSerializer)
		        .create();
		
		return gson;
	}
	
}
