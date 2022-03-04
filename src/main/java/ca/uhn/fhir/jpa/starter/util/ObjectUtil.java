package ca.uhn.fhir.jpa.starter.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 객체 공통유틸
 * 
 * @author smlee
 *
 */
public class ObjectUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtil.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Object의 value가 비어있는지 체크
	 * TODO StringUtils로 대체하기
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmptyObject(Object obj) {
		Map<String, Object> map = objectMapper.convertValue(obj, Map.class);

		int key_count = map.keySet().size();
		int count = 0;
		
		for(String key : map.keySet()) {
			if (map.get(key).equals("")) {
				count += 1;
			}
		}
		
		if (key_count == count) {
			return true;
		} else {
			return false;
		}
	}

}
