package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnRsapubkeyTbo;

/**
 * @InterfaceName : TfnRsapubkeyRepository.java
 * @Description : 암호화 관리 테이블 Repository
 * @Modification
 * 
 *                <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.02.17 이선민     최초작성
 * 
 *               </pre>
 *
 * @author 이선민
 * @since 2022.02.17
 * @version 1.0
 * @see
 */
public interface TfnRsapubkeyRepository extends JpaRepository<TfnRsapubkeyTbo, String>{

	List<TfnRsapubkeyTbo> findAll();
}
