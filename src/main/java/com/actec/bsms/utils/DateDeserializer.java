/**
 * 
 */
package com.actec.bsms.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> Title: </p>
 * 
 * <p> Description: 对java.util.Date字段的反序列化</p>
 * 
 * <p> Copyright: Copyright (c) 2013 by WITEC </p>
 * 
 * <p> Company: WITEC </p>
 * 
 * @author:  Lee Jiang
 * @version: 1.0
 * @date:    2013年9月18日  下午3:25:03
 * 
 */
public class DateDeserializer extends JsonDeserializer<Date> {
	private final Logger logger = LoggerFactory.getLogger(DateDeserializer.class);
	private final static String FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
	    // TODO Auto-generated method stub
		String val = jsonParser.getText();
		logger.debug("DateDeserializer Date str=" + val);
		DateFormat formatter = new SimpleDateFormat(FORMAT);
		Date date = null;
		try {
			date = formatter.parse(val);
		} catch (ParseException pex) {
			logger.error("Failed to parse date " + val, pex);
		}

		return date;
    }

}
