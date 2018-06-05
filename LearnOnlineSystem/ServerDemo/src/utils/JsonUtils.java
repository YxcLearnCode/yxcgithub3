package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static JsonNode getNode(String data) {
		JsonNode node = null;
		try {
			node = mapper.readTree(data);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
	
	//ObjectתJson
	public static String ObjectToJson(Object obj) 
	{
		try {
        	return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ת��Json����");
		}        
        return null;
	}
	
	
	//JsonתObject
	public static <T> T JsonToObject(String jsonStr,Class<T> objecttype) 
	{
        try {
        	return mapper.readValue(jsonStr, objecttype);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ת��Object����");
		}        
        return null;
	}
	
	//JsonתList
	public static <T> List<T> JsonToList(String jsonStr,Class<T> objecttype ) 
	{
		try {
        	JavaType javaType = getCollectionType(ArrayList.class, objecttype );
        	return mapper.readValue(jsonStr, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ת��List����");
		}      
        return null;
	}
	
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

}
