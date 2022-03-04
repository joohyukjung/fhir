package ca.uhn.fhir.jpa.entity.myhw.tbo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnExtractId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName : TfnExtractTbo.java
 * @Description : 리소스 추출대상 테이블 Entity
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.14 이선민     최초작성
 * 2022.02.22 강정호     기능 인수인계
 *               </pre>
 *
 * @author 이선민
 * @since 2022.02.14
 * @version 1.0
 * @see
 */
@Getter
@Entity
@Table(name = "TFN_EXTRACT")
@IdClass(TfnExtractId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TfnExtractTbo {

  @Id
  @NotBlank(message = "utilUserId must not be blank")
  private String utilUserId; /* 활용사용자ID */

  @Id
  @NotBlank(message = "utilServiceCd must not be blank")
  private String utilServiceCd; /* 활용서비스ID */
  
  @Id
  @NotBlank(message = "pvsnInstCd must not be blank")
  private String pvsnInstCd; /* 제공기관코드 */

  // TODO 주민번호 패턴
  @NotNull(message = "rrno must not be null")
  private String rrno; /* 암호화주민번호 */

  @NotBlank(message = "rlsCtfkCd must not be blank")
  private String rlsCtfkCd; /* 공개암호화key코드 */

  private String agreStcd; /* 동의상태코드 */
  
  private String regDt; /* 등록일시 */
}
