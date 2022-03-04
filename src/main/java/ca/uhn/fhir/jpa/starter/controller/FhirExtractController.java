package ca.uhn.fhir.jpa.starter.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnExtractServiceLogic;
import ca.uhn.fhir.jpa.starter.vo.extract.RequestExtract;
import ca.uhn.fhir.jpa.starter.vo.extract.ResponseExtract;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirExtractController.java
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
@Validated
@RequiredArgsConstructor
@RequestMapping("/extract")
@RestController
public class FhirExtractController {

  private final TfnExtractServiceLogic tfnExtractService;

  /**
   * 
   * @param requestExtract
   * @return
   */
  @PostMapping
  public ResponseEntity<List<ResponseExtract>> getPatientOfProvideInstitutionCode(
      @Valid @RequestBody RequestExtract requestExtract) {
    return ResponseEntity.ok(tfnExtractService.getPatientOfProvideInstitutionCode(requestExtract));
  }
  
  /**
   * 
   * @param requestExtract
   * @return
   */
  @DeleteMapping
  public ResponseEntity<String> deletePatientOfProvideInstitutionCode(
      @Valid @RequestBody RequestExtract requestExtract) {
    return ResponseEntity.ok(tfnExtractService.deletePatientOfProvideInstitutionCode(requestExtract));
  }
}
