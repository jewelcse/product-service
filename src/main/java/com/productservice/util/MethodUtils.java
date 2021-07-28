package com.productservice.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;


public class MethodUtils {

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
				errorJSON.put("success","False");
				errorJSON.put("message",ex.getMessage());
		    	errorJSON.put("status_code",status.value());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	return errorJSON.toString();
	}

		public static Object prepareErrorJSON(HttpStatus status, String localizedMessage) {
			JSONObject errorJSON=new JSONObject();
	    	System.out.println("MethodUtils");
	    	try {
				errorJSON.put("success","False");
				errorJSON.put("message","Invalid input");
		    	errorJSON.put("status_code",status.value());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	return errorJSON.toString();
		}

	public static Date getDate() {
		long millis=System.currentTimeMillis();
		java.sql.Date date=new java.sql.Date(millis);
		return date;
	}

	public static Object getWrapperDoublePrice(double productPrice) {
		Double pp = productPrice;
		return pp;
	}
}
