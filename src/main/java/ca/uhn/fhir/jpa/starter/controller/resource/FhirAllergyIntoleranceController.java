package ca.uhn.fhir.jpa.starter.controller.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.exception.AgreementException;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnDynagreServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnMdsidxServiceLogic;
import ca.uhn.fhir.jpa.starter.myhw.service.resource.logic.AllergyIntoLeranceService;
import ca.uhn.fhir.jpa.starter.util.AgreementUtil;
import ca.uhn.fhir.jpa.starter.util.JsonUtil;
import ca.uhn.fhir.jpa.starter.util.ResourceUtil;
import ca.uhn.fhir.jpa.starter.util.ValidationUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.UserAgreementVO;
import ca.uhn.fhir.jpa.starter.vo.code.AgreementCode;
import ca.uhn.fhir.jpa.starter.vo.code.DatasetCode;
import ca.uhn.fhir.jpa.starter.vo.common.DatasetSearchVO;
import ca.uhn.fhir.jpa.starter.vo.common.ResultResourceListVO;
import ca.uhn.fhir.jpa.starter.vo.index.FhirVisitPatientDatasetVO;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.to.BaseController;
import ca.uhn.fhir.to.model.HomeRequest;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName : FhirInfoController.java
 * @Description : 의료데이터 조회(mediDataLinkService)
 * @Modification
 * 
 *               <pre>
 *  수정일    		수정자 	   수정내용
 * ----------- ------- ------------------------
 * 2022.02.07  	 강정호    	최초작성
 * 2022.03.02	최철호	Resource Service 적용
 *               </pre>
 * 
 * @author jhkang
 * @since 2022-03-02
 * @version 1.0
 */

@Validated
@RestController
@RequiredArgsConstructor
public class FhirAllergyIntoleranceController extends BaseController {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FhirInfoController.class);

	private final TfnDynagreServiceLogic agreementService;
	private final AllergyIntoLeranceService allergyIntoLeranceService;

	private CaptureInterceptor interceptor = new CaptureInterceptor();

	/**
	 * 의료정보 조회 API - 알러지 및 부작용 조회(getAllergyIntoranceList)
	 * 
	 * @param theServletRequest
	 * @param theRequest
	 * @param theResponse
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/allergyIntolerance" })
	public List<Map<String, Object>> getAllergyIntoranceList(HttpServletRequest theServletRequest,
			HomeRequest theRequest, HttpServletResponse theResponse, @Valid @RequestBody DatasetSearchVO condition)
			throws Exception {

		LOGGER.info("FhirAllergyIntoleranceController getAllergyIntoranceList Start :::: {}", LocalDateTime.now());
		LOGGER.info("FhirAllergyIntoleranceController getAllergyIntoranceList condition :::: {}", JsonUtil.toJsonString(condition));

		// Agreement Check
		UserAgreementVO userAgreementVO = new UserAgreementVO(condition.getUserId(), AgreementCode.AGREEMENT.getCode(),
				condition.getResidentRegistrationNumber());
		List<AgreementDatasetVO> agreementList = agreementService.getAgreementDatasetByCondition(userAgreementVO);
		if (! AgreementUtil.checkAgreement(agreementList, DatasetCode.ALLERGYINTOLERANCE.getDatasetCode())) {
			throw new AgreementException(condition.getUserId(), DatasetCode.ALLERGYINTOLERANCE);
		}
		
		// Create FHIR Client & Parser
		IGenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		IParser parser = getContext(theRequest).newJsonParser();

		return allergyIntoLeranceService.getResourceList(client, parser, condition);
	}

}
