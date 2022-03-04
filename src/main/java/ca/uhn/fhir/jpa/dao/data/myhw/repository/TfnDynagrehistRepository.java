package ca.uhn.fhir.jpa.dao.data.myhw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.uhn.fhir.jpa.entity.myhw.tbo.TfnDynagrehistTbo;

/**
 * @InterfaceName : TfnDynagrehistRepository.java
 * @Description : 활용동의 이력
 * @Modification
 * 
 *                <pre>
 * 수정일      수정자          수정내용
 * --------- ------- ------------------------
 * 2022.02.23 이선민     최초작성
 * 
 *               </pre>
 *
 * @author 이선민
 * @since 2022.02.23
 * @version 1.0
 * @see
 */
public interface TfnDynagrehistRepository extends JpaRepository<TfnDynagrehistTbo, String>{

	
}
