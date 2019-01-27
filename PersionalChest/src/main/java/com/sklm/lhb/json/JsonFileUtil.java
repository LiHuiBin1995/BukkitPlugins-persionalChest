package com.sklm.lhb.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonFileUtil {

	/**
	 * 生成.json格式文件
	 * @param jsonString    json内容
	 * @param filePath      文件路径
	 * @param fileName      json文件名称
	 * @return  如果文件创建成功返回true，否则返回false
	 */
	public static boolean createJsonFile(String jsonString, String filePath, String fileName,String charset) {
		boolean flag = true;
		String fullPath = filePath +File.separator+ fileName + ".json";
		 try {
			File file = new File(fullPath);
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(file.exists()) {
				file.delete();
				file.createNewFile();
			}else {
				file.createNewFile();
			}
			
			//格式化json字符串
			jsonString = JsonFormatTool.formatJson(jsonString);
			
			Writer write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),charset));
			write.write(jsonString);
			write.flush();
			write.close();
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 读取json文件
	 * @param jsonPath    文件路径
	 * @param jsonName    文件名称
	 * @return 返回JSONArray，如果发生 异常则返回null
	 */
	public static JSONArray readJsonToArray(String jsonPath, String jsonName,String charset) {
		String path = jsonPath+"\\"+jsonName+".json";
		try {
			JSONParser parse = new JSONParser();
			File jsonFile = new File(path);
			if(!jsonFile.exists()) {
				JsonFileUtil.createJsonFile("[]", jsonPath, jsonName,charset);
			}
			JSONArray jsonArray = (JSONArray) parse.parse(new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), charset)));
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取json文件
	 * @param jsonPath    文件路径
	 * @param jsonName    文件名称
	 * @return  返回JSONObject，如果发生 异常则返回null
	 */
	public static JSONObject readJsonToObject(String jsonPath, String jsonName,String charset) {
		String path = jsonPath+"\\"+jsonName+".json";
		try {
			JSONParser parse = new JSONParser();
			File jsonFile = new File(path);
			if(!jsonFile.exists()) {
				JsonFileUtil.createJsonFile("[]", path, jsonName,charset);
			}
			JSONObject jsonObject = (JSONObject) parse.parse(new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), charset)));
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
}
