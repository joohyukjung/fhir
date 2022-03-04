package ca.uhn.fhir.jpa.entity.myhw.tbo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnPnstkeyId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @PackageName : ca.uhn.fhir.jpa.entity.myhw.tbo
 * @ClassName : TfnPnstkeyTbo.java
 * @Description : 
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022. 2. 28. jhkang     최초 작성
 *               </pre>
 *
 * @author jhkang
 * @since 2022. 2. 28.
 * @version 1.0
 * @see
 */
@Getter
@Entity
@Table(name = "TFN_PNSTKEY")
@IdClass(TfnPnstkeyId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TfnPnstkeyTbo {
  
  // FIXME NotNUll , NotBlank 등 DB 테이블 컬럼과 동기화 필요 
  
  @Id
  @NotNull(message = "pvsnInstCd must not be null")
  private String pvsnInstCd;
  
  @Id
  @NotNull(message = "dtlSn must not be null")
  private String dtlSn;
  
  @NotNull(message = "secKeyVal must not be null")
  private String secKeyVal;
  
  @NotNull(message = "useYn must not be null")
  private String useYn;
  
  @NotNull(message = "yldYn must not be null")
  private String yldYn;
  
  @NotNull(message = "issuYmd must not be null")
  private String issuYmd;
  
  @NotNull(message = "vldYmd must not be null")
  private String vldYmd;
  
  private String frstRegDt;
  
  private String frstRgtrId;
  
  private String frstRgtrIpAddr;
  
  private String lastMdfcnDt;
  
  private String lastMdfrId;
  
  private String lastMdfrIpAddr;

}
