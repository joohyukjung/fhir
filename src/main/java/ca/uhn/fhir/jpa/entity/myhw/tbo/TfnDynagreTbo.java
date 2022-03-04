package ca.uhn.fhir.jpa.entity.myhw.tbo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnDynagreId;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.code.PatternCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnDynagreTbo.java
 * @Description : 활용동의 Entity
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     테이블 수정(주민번호 삭제)
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
//@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Entity(name = "TFN_DYNAGRE")
@IdClass(TfnDynagreId.class)
@Table(name = "TFN_DYNAGRE")
public class TfnDynagreTbo {

	@Id
	@NotBlank(message = "utilUserId must not be blank")
	private String utilUserId; // 활용사용자ID
	
	@Id
	@NotBlank(message = "utilServiceCd must not be blank")
	private String utilServiceCd; // 활용서비스ID
	
	@Id
	@NotBlank(message = "dtstCd must not be blank")
	private String dtstCd; // 데이터셋코드

	private String dtstNm; // 데이터셋명
		
	@NotBlank(message = "agreStcd must not be blank")
	private String agreStcd; // 동의상태코드
	
	private String agreDt; // 동의일시
	
	private String agreWhdwDt; // 동의철회일시
		
	// Builder custom
	public static class TfnDynagreTboBuilder {
		public TfnDynagreTboBuilder agreDt(String agreementDateTime) throws ParseException {
			if(agreementDateTime == null) return this;
			this.agreDt = DateUtil.convertDateFormat(agreementDateTime, PatternCode.API_DATE_PATTERN.getPattern(), PatternCode.DB_DATE_PATTERN.getPattern());
			return this;
		}
		
		public TfnDynagreTboBuilder agreWhdwDt(String withdrawDateTime) throws InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException, ParseException {
			if(withdrawDateTime == null) return this;
			this.agreWhdwDt = DateUtil.convertDateFormat(withdrawDateTime, PatternCode.API_DATE_PATTERN.getPattern(), PatternCode.DB_DATE_PATTERN.getPattern());
			return this;
		}
	}

	

}
