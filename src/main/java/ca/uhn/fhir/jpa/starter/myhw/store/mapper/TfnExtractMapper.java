package ca.uhn.fhir.jpa.starter.myhw.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnExtractTbo;
import ca.uhn.fhir.jpa.starter.vo.index.VisitPatientIndexVO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TfnExtractMapper {
	TfnExtractMapper INSTANCE = Mappers.getMapper(TfnExtractMapper.class);
	
	@Mapping(source="userId", target="utilUserId")
	@Mapping(source="utilizationServiceCode", target="utilServiceCd")
	@Mapping(source="provideInstitutionCode", target="pvsnInstCd")
	TfnExtractTbo visitPatientIndexVoToTbo(VisitPatientIndexVO vo) throws Exception;

}
