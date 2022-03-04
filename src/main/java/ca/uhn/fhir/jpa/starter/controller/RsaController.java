package ca.uhn.fhir.jpa.starter.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.jpa.starter.myhw.service.logic.TfnRsapubkeyService;
import ca.uhn.fhir.jpa.starter.vo.RsaPublicKeyDto;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RequestMapping("/rsa")
@RestController
public class RsaController {
	
	private final TfnRsapubkeyService tfnRsapubkeyService;
	
	@PostMapping("/{id}")
	public ResponseEntity<RsaPublicKeyDto> createRsaPublicKey(@PathVariable("id") String provideInstitutionCode,
			@Valid @RequestBody RsaPublicKeyDto rsaPublicKeyDto) {
		return ResponseEntity.ok(tfnRsapubkeyService.createRsapubkey(provideInstitutionCode, rsaPublicKeyDto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RsaPublicKeyDto> getRsaPublicKey(@PathVariable("id") String provideInstitutionCode) {
		return ResponseEntity.ok(tfnRsapubkeyService.getRsapubkey(provideInstitutionCode));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<RsaPublicKeyDto> updateRsaPublicKey(@PathVariable("id") String provideInstitutionCode,
			@Valid @RequestBody RsaPublicKeyDto rsaPublicKeyDto) {
		return ResponseEntity.ok(tfnRsapubkeyService.updateRsapubkey(provideInstitutionCode, rsaPublicKeyDto));
	}
}
