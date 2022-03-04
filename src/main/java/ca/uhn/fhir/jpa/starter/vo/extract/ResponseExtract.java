package ca.uhn.fhir.jpa.starter.vo.extract;

import java.util.ArrayList;
import java.util.List;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnExtractTbo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnExtractDto.java
 * @Description :
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.14 이선민     최초작성
 * 2022.02.22 강정호     기능개발 인수인계
 *               </pre>
 *
 * @author 이선민
 * @since 2022.02.14
 * @version 1.0
 * @see
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseExtract {

  private String userId; /* MH사용자ID */

  private String serviceUID; /* 활용서비스UID */

  private String provideInstitutionCode; /* 제공기관 코드 */

  private String residentRegistrationNumber; /* 주민번호 */

  private String releaseKey; /* 공개암호화 키 */

  private String agreementCode; /* 동의상태 */

  private String registerDate; /* 등록 일자 */

  @Builder
  public ResponseExtract(String userId, String serviceUID, String provideInstitutionCode,
      String residentRegistrationNumber, String releaseKey, String agreementCode, String registerDate) {
    this.userId = userId;
    this.serviceUID = serviceUID;
    this.provideInstitutionCode = provideInstitutionCode;
    this.residentRegistrationNumber = residentRegistrationNumber;
    this.releaseKey = releaseKey;
    this.agreementCode = agreementCode;
    this.registerDate = registerDate;
  };

  /**
   * Table Object to Response Data Object
   * @param tfnExtractTbo
   * @return
   */
  public static ResponseExtract from(TfnExtractTbo tfnExtractTbo) {
    return ResponseExtract.builder()
        .userId(tfnExtractTbo.getUtilUserId())
        .serviceUID(tfnExtractTbo.getUtilServiceCd())
        .provideInstitutionCode(tfnExtractTbo.getPvsnInstCd())
        .residentRegistrationNumber(tfnExtractTbo.getRrno())
        .releaseKey(tfnExtractTbo.getRlsCtfkCd())
        .agreementCode(tfnExtractTbo.getAgreStcd())
        .registerDate(tfnExtractTbo.getRegDt())
        .build();
  };


  /**
   * List<Table Object> to List<Response Data Object>
   * @param tfnExtractTboList
   * @return
   */
  public static List<ResponseExtract> fromList(List<TfnExtractTbo> tfnExtractTbos) {
    List<ResponseExtract> result = new ArrayList<ResponseExtract>();
    
    for (TfnExtractTbo tfnExtractTbo : tfnExtractTbos) {
      result.add(ResponseExtract.from(tfnExtractTbo));
    }
    
    return result;
  }
}
