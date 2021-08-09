package com.productservice.util;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;


public class MethodUtils {
	private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";


	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
	private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");
	
	private MethodUtils() {
	}

	public static String toSlug(String input) {
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		slug = EDGESDHASHES.matcher(slug).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}


		public static String prepareErrorJSON(HttpStatus status, Exception ex) {
	    	JSONObject errorJSON=new JSONObject();
	    	try {
				errorJSON.put("success",false);
				errorJSON.put("message",ex.getMessage());
		    	errorJSON.put("statusCode",status.value());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	return errorJSON.toString();
	}

		public static Object prepareErrorJSON(HttpStatus status, String localizedMessage) {
			JSONObject errorJSON=new JSONObject();
	    	System.out.println("MethodUtils");
	    	try {
				errorJSON.put("success",false);
				errorJSON.put("message","Invalid input");
		    	errorJSON.put("statusCode",status.value());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	return errorJSON.toString();
		}

	public static String prepareSuccessJSON(HttpStatus status, String message) {
		JSONObject successJSON=new JSONObject();
		try {
			successJSON.put("success",true);
			successJSON.put("message",message);
			successJSON.put("statusCode",status.value());
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return successJSON.toString();
	}

	public static Date getDate() {
		long millis=System.currentTimeMillis();
		java.sql.Date date=new java.sql.Date(millis);
		return date;
	}

	public static String getLocalDateTime(){
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
		String formatDateTime = now.format(formatter);
		return formatDateTime;
	}

	public static Object getWrapperDoublePrice(double productPrice) {
		Double pp = productPrice;
		return pp;
	}
}
