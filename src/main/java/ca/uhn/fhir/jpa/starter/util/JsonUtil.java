package ca.uhn.fhir.jpa.starter.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonUtil {
	private static Gson gson = Converters.registerZonedDateTime(new GsonBuilder()).setPrettyPrinting().create();

	private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
	private static volatile ObjectMapper objectMapper = null;
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		//
		return gson.fromJson(json, clazz);
	}

	public static ObjectMapper mapper() {
		return objectMapper == null ? defaultObjectMapper : objectMapper;
	}
	
	public static ObjectWriter writer() {
		return mapper().writer().withDefaultPrettyPrinter();
	}
	
	public static <T> String toJsonString(T model) throws JsonProcessingException {
		ObjectWriter ow = writer().withDefaultPrettyPrinter();
		
		return ow.writeValueAsString(model);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> toHashMap(T obj) {
		return mapper().convertValue(obj, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> toHashMap(List<T> obj) {
		return mapper().convertValue(obj, Map.class);
	}
}
