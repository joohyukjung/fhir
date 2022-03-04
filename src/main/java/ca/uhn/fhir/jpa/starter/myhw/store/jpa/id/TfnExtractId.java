package ca.uhn.fhir.jpa.starter.myhw.store.jpa.id;

import java.io.Serializable;

/**
 * @ClassName : TfnExtractId.java
 * @Description : 리소스 추출대상 테이블 PK
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.14 이선민     최초작성
 * 2022.02.22 강정호     추출대상 API 기능개발 분업
 *               </pre>
 *
 * @author 이선민
 * @since 2022.02.14
 * @version 1.0
 * @see
 */
public class TfnExtractId implements Serializable {

  private String utilUserId; /* 활용사용자ID */
  private String utilServiceCd; /* 활용서비스ID */
  private String pvsnInstCd; /* 제공기관코드 */


  public TfnExtractId() {}

  public TfnExtractId(String utilUserId, String utilServiceCd, String pvsnInstCd) {
    this.utilUserId = utilUserId;
    this.utilServiceCd = utilServiceCd;
    this.pvsnInstCd = pvsnInstCd;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((utilUserId == null) ? 0 : utilUserId.hashCode());
    result = prime * result + ((utilServiceCd == null) ? 0 : utilServiceCd.hashCode());
    result = prime * result + ((pvsnInstCd == null) ? 0 : pvsnInstCd.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TfnExtractId other = (TfnExtractId) obj;
    if (utilUserId == null) {
      if (other.utilUserId != null)
        return false;
    } else if (!utilUserId.equals(other.utilUserId))
      return false;
    if (utilServiceCd == null) {
      if (other.utilServiceCd != null)
        return false;
    } else if (!utilServiceCd.equals(other.utilServiceCd))
      return false;
    if (pvsnInstCd == null) {
      if (other.pvsnInstCd != null)
        return false;
    } else if (!pvsnInstCd.equals(other.pvsnInstCd))
      return false;
    return true;
  }


}
