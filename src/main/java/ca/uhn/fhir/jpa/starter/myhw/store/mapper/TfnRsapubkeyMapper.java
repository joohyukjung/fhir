package ca.uhn.fhir.jpa.starter.myhw.store.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagreTbo;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnRsapubkeyTbo;
import ca.uhn.fhir.jpa.starter.vo.agreement.AgreementDatasetVO;
import ca.uhn.fhir.jpa.starter.vo.agreement.RsaVO;

/**
 * @ClassName : TfnRsapubkeyMapper.java
 * @Description : TfnRsapubkey 매핑
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.17 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.17
 * @version 1.0
 * @see
 */
@Mapper(componentModel = "spring")
public interface TfnRsapubkeyMapper {

	TfnRsapubkeyMapper INSTANCE = Mappers.getMapper(TfnRsapubkeyMapper.class);
	
	/**
	 * @param tbos
	 * @return
	 */
	@IterableMapping(qualifiedByName = "tboToRsaVO")
	List<RsaVO> tboListToRsaVOList(List<TfnRsapubkeyTbo> tbos);
	
	@Named("tboToRsaVO")
	@Mapping(source="pvsnInstCd", target="provideInstitutionCode")
	@Mapping(source="rlsCtfk", target="pubKey")
	@Mapping(source="vldDate", target="validDate")
	RsaVO tboToRsaVO(TfnRsapubkeyTbo tbo);
}
