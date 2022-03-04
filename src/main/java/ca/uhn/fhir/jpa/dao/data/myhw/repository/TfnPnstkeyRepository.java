package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnPnstkeyTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnPnstkeyId;

/**
 * 
 * @PackageName : ca.uhn.fhir.jpa.dao.data.myhw.repository
 * @ClassName : TfnPnstkeyRepository.java
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
public interface TfnPnstkeyRepository extends JpaRepository<TfnPnstkeyTbo, TfnPnstkeyId>{
  
  /**
   * 
   * @param pvsnInstCd
   * @return
   */
  TfnPnstkeyTbo findByPvsnInstCd(String pvsnInstCd);
}
