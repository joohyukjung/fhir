package ca.uhn.fhir.jpa.starter.myhw.store.mapper;

import java.util.Date;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.starter.util.DateUtil;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;

/**
 * @ClassName : TfnDynagreMapper.java
 * @Description : TfnDynagre 매핑
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.03 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.03
 * @version 1.0
 * @see
 */
@Mapper(componentModel = "spring")
public interface TfnDynagreMapper {
	
	TfnDynagreMapper INSTANCE = Mappers.getMapper(TfnDynagreMapper.class);
	
	/**
	 * VO -> Entity 
	 * @param vo
	 * @return
	 */
	@Mapping(source="userId", target="utilUserId") 
	@Mapping(source="utilizationServiceCode", target="utilServiceCd")	
	@Mapping(source="agreementStatus", target="agreStcd")
	@Mapping(source="agreementDateTime", target="agreDt")
	@Mapping(source="withdrawDateTime", target="agreWhdwDt")
	@Mapping(source="datasetCode", target="dtstCd")
	@Mapping(source="datasetName", target="dtstNm")	
	TfnDynagreTbo agreementDatasetVoToTbo(AgreementDatasetVO vo) throws Exception;

	default String getStringNow() {
		return DateUtil.getDateStringNow("yyyyMMdd");
	}
	
	default Date getDateNow() throws Exception{
		return DateUtil.getDateNow("yyyyMMdd");
	}
	
	
	/**
	 * Entity -> VO
	 * @param tbos
	 * @return
	 */
	@IterableMapping(qualifiedByName = "tboToAgreementDatasetVO")
	List<AgreementDatasetVO> tboListToAgreementDatasetVoList(List<TfnDynagreTbo> tbos);
	
	@Named("tboToAgreementDatasetVO")
	@Mapping(source="utilUserId", target="userId")
	@Mapping(source="utilServiceCd", target="utilizationServiceCode")	
	@Mapping(source="agreStcd", target="agreementStatus")
	@Mapping(source="agreDt", target="agreementDateTime")
	@Mapping(source="agreWhdwDt", target="withdrawDateTime")
	@Mapping(source="dtstCd", target="datasetCode")
	@Mapping(source="dtstNm", target="datasetName")
	AgreementDatasetVO tboToAgreementDatasetVO(TfnDynagreTbo tbo);	
}
