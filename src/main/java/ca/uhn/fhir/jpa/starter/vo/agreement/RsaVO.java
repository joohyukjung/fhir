package ca.uhn.fhir.jpa.starter.vo.agreement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class RsaVO {

	private String provideInstitutionCode; // 제공기관코드

	private String pubKey; // 공개암호화key

	private String validDate; // 유효일자

}
