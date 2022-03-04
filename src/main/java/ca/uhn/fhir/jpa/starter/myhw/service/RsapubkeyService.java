package ca.uhn.fhir.jpa.starter.myhw.service;

import ca.uhn.fhir.jpa.starter.vo.RsaPublicKeyDto;

public interface RsapubkeyService {
	RsaPublicKeyDto createRsapubkey(String provideInstitutionCode, RsaPublicKeyDto rsaPublicKeyDto);

	RsaPublicKeyDto getRsapubkey(String provideInstitutionCode);

	RsaPublicKeyDto updateRsapubkey(String provideInstitutionCode, RsaPublicKeyDto rsaPublicKeyDto);
}
