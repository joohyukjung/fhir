package ca.uhn.fhir.jpa.starter.myhw.store.jpa;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.uhn.fhir.jpa.dao.data.myhw.repository.TfnRsapubkeyRepository;
import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnRsapubkeyTbo;
import ca.uhn.fhir.jpa.starter.myhw.store.TfnRsapubkeyStore;
import ca.uhn.fhir.jpa.starter.myhw.store.mapper.TfnRsapubkeyMapper;
import ca.uhn.fhir.jpa.starter.vo.agreement.RsaVO;

@Repository
public class TfnRsapubkeyJpaStore implements TfnRsapubkeyStore{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TfnRsapubkeyJpaStore.class);

	@Autowired
	private TfnRsapubkeyRepository rsaRepository;
	
	@Override
	public List<RsaVO> selectAllKey() {

		List<TfnRsapubkeyTbo> tboList = rsaRepository.findAll();
		
		LOGGER.info(" selectAllKey ::: tboList {}", tboList);
		
		List<RsaVO> result = TfnRsapubkeyMapper.INSTANCE.tboListToRsaVOList(tboList); // MAPSTRUCT
		
		LOGGER.info(" selectAllKey ::: result {}", result);
		
		return result;
	}
}
