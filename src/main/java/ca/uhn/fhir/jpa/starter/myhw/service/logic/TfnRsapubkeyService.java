package ca.uhn.fhir.jpa.starter.myhw.service.logic;

import org.springframework.stereotype.Service;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnRsapubkeyRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnRsapubkeyTbo;
import ca.uhn.fhir.jpa.starter.myhw.exception.DuplicateException;
import ca.uhn.fhir.jpa.starter.myhw.exception.ResourceNotFoundException;
import ca.uhn.fhir.jpa.starter.myhw.service.RsapubkeyService;
import ca.uhn.fhir.jpa.starter.vo.RsaPublicKeyDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TfnRsapubkeyService implements RsapubkeyService{
	
	private final TfnRsapubkeyRepository tfnRsapubkeyRepository;

	@Override
	public RsaPublicKeyDto createRsapubkey(String provideInstitutionCode, RsaPublicKeyDto rsaPublicKeyDto) {
		this.tfnRsapubkeyRepository.findById(provideInstitutionCode)
			.ifPresent(key -> {
				throw new DuplicateException("Already RSA public key");
			});
		
		try {		
			TfnRsapubkeyTbo tfnRsapubkeyTbo = TfnRsapubkeyTbo.builder()
					.pvsnInstCd(provideInstitutionCode)
					.rlsCtfk(rsaPublicKeyDto.getPubKeyXml())
					.vldDate(rsaPublicKeyDto.getEffectiveDate())
					.build();
			
			return RsaPublicKeyDto.from(tfnRsapubkeyRepository.save(tfnRsapubkeyTbo));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public RsaPublicKeyDto getRsapubkey(String provideInstitutionCode) {
		TfnRsapubkeyTbo tfnRsapubkeyTbo = tfnRsapubkeyRepository.findById(provideInstitutionCode)
				.orElseThrow(ResourceNotFoundException::new);	
		
		return RsaPublicKeyDto.from(tfnRsapubkeyTbo);
	}

	@Override
	public RsaPublicKeyDto updateRsapubkey(String provideInstitutionCode, RsaPublicKeyDto rsaPublicKeyDto) {
		this.tfnRsapubkeyRepository.findById(provideInstitutionCode).orElseThrow(() -> new ResourceNotFoundException("Not found RSA public key"));
		
		try {
			TfnRsapubkeyTbo tfnRsapubkeyTbo = TfnRsapubkeyTbo.builder()
					.pvsnInstCd(provideInstitutionCode)
					.rlsCtfk(rsaPublicKeyDto.getPubKeyXml())
					.vldDate(rsaPublicKeyDto.getEffectiveDate())
					.build();
			return RsaPublicKeyDto.from(this.tfnRsapubkeyRepository.save(tfnRsapubkeyTbo));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
