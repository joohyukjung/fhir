package ca.uhn.fhir.jpa.entity.myhw.tbo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ca.uhn.fhir.jpa.starter.util.RSAUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnRsapubkeyTbo.java
 * @Description : 암호화 관리 테이블 Entity
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
@Getter
@Entity
@Table(name = "TFN_RSAPUBKEY")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TfnRsapubkeyTbo {
	
	@Id
	private String pvsnInstCd; // 제공기관코드
	
	private String rlsCtfk; // 공개암호화key
	
	private String vldDate; // 유효일자

	// Builder custom
	public static class TfnRsapubkeyTboBuilder {
		public TfnRsapubkeyTboBuilder rlsCtfk(String pubKeyXml) throws InvalidKeySpecException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException {
			
			// Agent에게 전달받은 XML을 PublicKey로 Parsing
			PublicKey publicKey = RSAUtil.toPublicKeyFromXml(pubKeyXml);
						
			// DB에 저장하기 위해 PublicKey를 Base64로 인코딩
			this.rlsCtfk = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			
			return this;
		}
	}
}
