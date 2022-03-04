package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnExtractTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.jpa.id.TfnExtractId;

/**
 * @InterfaceName : TfnExtractRepository.java
 * @Description :
 * @Modification
 * 
 *               <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.02.22 강정호     최초작성
 *               </pre>
 *
 * @author 강정호
 * @since 2022.02.22
 * @version 1.0
 * @see
 */
public interface TfnExtractRepository extends JpaRepository<TfnExtractTbo, TfnExtractId> {

  /**
   * 
   * @param pvsnInstCd
   * @return
   */
  List<TfnExtractTbo> findByPvsnInstCd(String pvsnInstCd);

  /**
   * 
   * @param pvsnInstCd
   * @return
   */
  @Transactional
  List<TfnExtractTbo> deleteByPvsnInstCd(String pvsnInstCd);

}
