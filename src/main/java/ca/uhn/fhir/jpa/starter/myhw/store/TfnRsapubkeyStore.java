package ca.uhn.fhir.jpa.starter.myhw.store;

import java.util.List;

import ca.uhn.fhir.jpa.starter.vo.agreement.RsaVO;

public interface TfnRsapubkeyStore {
	
	List<RsaVO> selectAllKey();

}
