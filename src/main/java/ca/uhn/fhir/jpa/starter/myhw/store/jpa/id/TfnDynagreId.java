package ca.uhn.fhir.jpa.starter.myhw.store.jpa.id;

import java.io.Serializable;

/**
 * @ClassName : TfnDynagreId.java
 * @Description : 활용동의 PK
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.01.26 이선민     최초작성
 * 2022.02.14 이선민     테이블 수정(주민번호 삭제)
 * </pre>
 *
 * @author 이선민
 * @since 2022.01.26
 * @version 1.0
 * @see
 */
public class TfnDynagreId implements Serializable{
	
	private String utilUserId; // 활용사용자ID
	private String utilServiceCd; // 활용서비스ID
	private String dtstCd; // 데이터셋코드
	
	public TfnDynagreId() {
	}

	
	public TfnDynagreId(String utilUserId, String utilizationServiceCode, String dtstCd) {
		this.utilUserId = utilUserId;
		this.utilServiceCd = utilizationServiceCode;
		this.dtstCd = dtstCd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtstCd == null) ? 0 : dtstCd.hashCode());
		result = prime * result + ((utilServiceCd == null) ? 0 : utilServiceCd.hashCode());
		result = prime * result + ((utilUserId == null) ? 0 : utilUserId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TfnDynagreId other = (TfnDynagreId) obj;
		if (dtstCd == null) {
			if (other.dtstCd != null)
				return false;
		} else if (!dtstCd.equals(other.dtstCd))
			return false;
		if (utilServiceCd == null) {
			if (other.utilServiceCd != null)
				return false;
		} else if (!utilServiceCd.equals(other.utilServiceCd))
			return false;
		if (utilUserId == null) {
			if (other.utilUserId != null)
				return false;
		} else if (!utilUserId.equals(other.utilUserId))
			return false;
		return true;
	}

	
	
	

	
}
