package ca.uhn.fhir.jpa.starter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.code.PatternCode;

/**
 * @ClassName : DateUtil.java
 * @Description : Date 데이터타입 관련 공통함수
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.03 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.03
 * @version 1.0
 * @see
 */
public class DateUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	
//	public static String getLocalDateStringNow(String pattern) {
//		LocalDate now = LocalDate.now(); 
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//		String stringNow = now.format(formatter);
//		
//		return stringNow;
//	}
	
	public static Date getDateNow(String pattern) throws Exception {
		Date now = new Date();
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat(pattern) ;
		String nowString = simpleDataFormat.format(now);
		Date nowDate = simpleDataFormat.parse(nowString);
		
		return nowDate;
	}	
	
	public static String getDateStringNow(String pattern){
		Date now = new Date();
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat(pattern) ;
		String nowString = simpleDataFormat.format(now);
		
		return nowString;
	}
	
	
	// "20220209094700" -> "2022-02-09 09:47:00"
	// "2022-02-09 09:47:00" -> "20220209094700" 
	public static String convertDateFormat(String date, String inputDatePattern, String outputDatePattern) throws ParseException {	

		return new SimpleDateFormat(outputDatePattern).format(new SimpleDateFormat(inputDatePattern).parse(date));
	}
}
