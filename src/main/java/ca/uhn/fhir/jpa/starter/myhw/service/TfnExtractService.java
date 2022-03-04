package ca.uhn.fhir.jpa.starter.myhw.service;

import java.util.List;
import ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException;
import ca.uhn.fhir.jpa.starter.vo.extract.RequestExtract;
import ca.uhn.fhir.jpa.starter.vo.extract.ResponseExtract;

/**
 * @InterfaceName : TfnExtractService.java
 * @Description : 리소스 추출 대상 조회/삭제
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
public interface TfnExtractService {

  /**
   * 
   * @param requestExtract
   * @return
   */
  List<ResponseExtract> getPatientOfProvideInstitutionCode(RequestExtract requestExtract) throws ResourceNotFoundException;

  /**
   * 
   * @param requestExtract
   * @return
   * @throws ResourceNotFoundException
   */
  String deletePatientOfProvideInstitutionCode(RequestExtract requestExtract) throws ResourceNotFoundException;
}
