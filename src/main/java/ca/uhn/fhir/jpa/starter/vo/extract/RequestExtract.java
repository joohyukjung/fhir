package ca.uhn.fhir.jpa.starter.vo.extract;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @PackageName : ca.uhn.fhir.jpa.starter.vo.extract
 * @ClassName : RequestExtract.java
 * @Description :
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022. 2. 25. jhkang     최초 작성
 *               </pre>
 *
 * @author jhkang
 * @since 2022. 2. 25.
 * @version 1.0
 * @see
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestExtract {

  private String provideInstitutionCode; /* 제공기관 코드 */

  @Builder
  public RequestExtract(String provideInstitutionCode) {
    this.provideInstitutionCode = provideInstitutionCode;
  }
}
