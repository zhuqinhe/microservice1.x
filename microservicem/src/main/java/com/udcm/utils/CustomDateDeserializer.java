package com.udcm.utils;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = jp.getText();
		if(StringUtils.isBlank(value)||"NaN".equals(value)){
			return null;
		}		
		if(value.contains("/")){
			return DateUtils.parse(value, DateUtils.Pattern.yyyy1MM1dd_HH_mm_ss);
		}else if(value.contains("-")){
			return DateUtils.parse(value, DateUtils.Pattern.yyyy_MM_dd_HH_mm_ss);
		}else {
			return new Date(Long.valueOf(value));
		}
	}


}
