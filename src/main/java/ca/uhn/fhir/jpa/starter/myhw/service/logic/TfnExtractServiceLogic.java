package ca.uhn.fhir.jpa.starter.myhw.service.logic;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnExtractRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnExtractTbo;
import ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException;
import ca.uhn.fhir.jpa.starter.myhw.service.TfnExtractService;
import ca.uhn.fhir.jpa.starter.vo.extract.RequestExtract;
import ca.uhn.fhir.jpa.starter.vo.extract.ResponseExtract;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : TfnExtractServiceLogic.java
 * @Description : 리소스 추출 대상 API
 * @Modification
 *
 *               <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.22 강정호     최초작성
 *               </pre>
 *
 * @author 강정호
 * @since 2022.02.22
 * @version 1.0
 * @see
 */
@RequiredArgsConstructor
@Service
public class TfnExtractServiceLogic implements TfnExtractService {

  private final TfnExtractRepository tfnExtractRepository;

  @Override
  public List<ResponseExtract> getPatientOfProvideInstitutionCode(RequestExtract requestExtract) throws ResourceNotFoundException {
    if (tfnExtractRepository.findByPvsnInstCd(requestExtract.getProvideInstitutionCode()).isEmpty()) {
      throw new ResourceNotFoundException("Cannot get Data Cause by Not found Provide Institution Code : " + requestExtract.getProvideInstitutionCode()); 
    }
    
    List<TfnExtractTbo> tfnExtractTbos = tfnExtractRepository.findByPvsnInstCd(requestExtract.getProvideInstitutionCode());
    return ResponseExtract.fromList(tfnExtractTbos);
  }

  @Override
  @Transactional
  public String deletePatientOfProvideInstitutionCode(RequestExtract requestExtract) throws ResourceNotFoundException {
    if (tfnExtractRepository.findByPvsnInstCd(requestExtract.getProvideInstitutionCode()).isEmpty()) {
      throw new ResourceNotFoundException("Cannot delete Data Cause by Not found Provide Institution Code : " + requestExtract.getProvideInstitutionCode()); 
    }
    
    tfnExtractRepository.deleteByPvsnInstCd(requestExtract.getProvideInstitutionCode());
    return "delete success";
  }
}
