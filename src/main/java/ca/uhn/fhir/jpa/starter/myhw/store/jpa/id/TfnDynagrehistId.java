package ca.uhn.fhir.jpa.starter.myhw.store.jpa.id;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnDynagreId.java
 * @Description : 활용동의 PK
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.14 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.14
 * @version 1.0
 * @see
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class TfnDynagrehistId implements Serializable{
	private String utilUserId; // 활용사용자ID	
	private String utilServiceCd; // 활용서비스ID
	private String dtstCd; // 데이터셋코드
	private String dtstNm; // 데이터셋명		
	private String agreStcd; // 동의상태코드
	private String regDt; // 등록일자
	
}
