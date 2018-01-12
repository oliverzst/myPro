/**
 * 
 */
package com.actec.bsms.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> Title: </p>
 * 
 * <p> Description: 对java.util.Date字段的序列化</p>
 * 
 * <p> Copyright: Copyright (c) 2013 by WITEC </p>
 * 
 * <p> Company: WITEC </p>
 * 
 * @author: Lee Jiang
 * @version: 1.0
 * @date: 2013年9月18日 下午3:22:30
 * 
 */
public class DateSerializer extends JsonSerializer<Date> {
	private final Logger logger = LoggerFactory.getLogger(DateDeserializer.class);
	private final static String FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void serialize(Date date, JsonGenerator jsonGen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
		// TODO Auto-generated method stub
		DateFormat formatter = new SimpleDateFormat(FORMAT);
		String formattedDate = formatter.format(date);
		logger.debug("DateSerializer formattedDate=" + formattedDate);
		jsonGen.writeString(formattedDate);
	}

}
