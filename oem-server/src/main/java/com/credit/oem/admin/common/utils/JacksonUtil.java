/**
 * 
 */
package com.credit.oem.admin.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author SongBang
 * jackson utils
 */
public class JacksonUtil {

	/**
	 * object to string
	 * @param obj object
	 * @return String
	 * @throws Exception exception
	 */
	public static String obj2Str(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	/**
	 * convert json string to object
	 * @param jsonStr
	 * @param clazz class
	 * @return
	 * @throws Exception
	 */
	public static <T> T str2Obj(String jsonStr,Class<T> clazz)throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonStr, clazz);
	}
	
	
	/**
	 * convert json string to object
	 * @param jsonStr
	 * @param type TypeReference
	 * @return
	 * @throws Exception
	 */
	public static <T> T str2Obj(String jsonStr, TypeReference<T> type) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonStr, type);
	}
}
