package ca.uhn.fhir.jpa.starter.vo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnRsapubkeyTbo;
import ca.uhn.fhir.jpa.starter.util.RSAUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class RsaPublicKeyDto {

	@NotBlank
	private String pubKeyXml;
	
	@Size(min = 8, max = 8)
	private String effectiveDate;
	
	@AssertTrue(message = "yyyyMMdd형식에 맞지 않습니다")
	private boolean isEffectiveDateValidation() {
		try {
			LocalDate.parse(getEffectiveDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static RsaPublicKeyDto from(TfnRsapubkeyTbo tfnRsapubkeyTbo) {
		if(tfnRsapubkeyTbo == null) return null;
		
		String pubKeyXml = RSAUtil.toXmlFromBase64(tfnRsapubkeyTbo.getRlsCtfk());
		return RsaPublicKeyDto.builder()
				.pubKeyXml(pubKeyXml)
				.effectiveDate(tfnRsapubkeyTbo.getVldDate())
				.build();
	}
}
