package com.joecorelibrary.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joecorelibrary.util.okhttp.utils.Exceptions;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	public static final String TAG = "JSONUtils";

	public static String toJson(Object obj){
		return new Gson().toJson(obj);
	}

	public static <T> T fromJson(String json, TypeToken<T> token){
		return new Gson().fromJson(json,token.getType());
	}

	public static <T> T fromJson(String json, Class<T> clazz){
		return new Gson().fromJson(json, clazz);
	}

	public static String getString(String json, String key){
		try{
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.optString(key, "");
		}catch (JSONException e){
			return "";
		}
	}

	public static int getInt(String json, String key){
		try{
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.optInt(key, -1);
		}catch (JSONException e){
			return -1;
		}
	}

	public static boolean getBoolean(String json, String key){
		try{
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.optBoolean(key, false);
		}catch (JSONException e){
			return false;
		}
	}
}