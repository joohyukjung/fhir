package ca.uhn.fhir.jpa.starter.myhw.store.jpa.id;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @PackageName : ca.uhn.fhir.jpa.starter.myhw.store.jpa.id
 * @ClassName : TfnPnstkeyId.java
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TfnPnstkeyId implements Serializable {
  
  private String pvsnInstCd;
  
  private String dtlSn;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pvsnInstCd == null) ? 0 : pvsnInstCd.hashCode());
    result = prime * result + ((dtlSn == null) ? 0 : dtlSn.hashCode());
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
    TfnPnstkeyId other = (TfnPnstkeyId) obj;
    if (pvsnInstCd == null) {
      if (other.pvsnInstCd != null)
        return false;
    } else if (!pvsnInstCd.equals(other.pvsnInstCd))
      return false;
    if (dtlSn == null) {
      if (other.dtlSn != null)
        return false;
    } else if (!dtlSn.equals(other.dtlSn))
      return false;
    return true;
  }
}
