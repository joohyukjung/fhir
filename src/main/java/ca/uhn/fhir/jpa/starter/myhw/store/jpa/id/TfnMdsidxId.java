package ca.uhn.fhir.jpa.starter.myhw.store.jpa.id;

import java.io.Serializable;

public class TfnMdsidxId implements Serializable{

	private String utilUserId; // 활용사용자ID

	private String pvsnInstCd; // 제공기관코드

	private String patId; // 제공기관 환자ID

	public TfnMdsidxId() {
		
	}
	
	public TfnMdsidxId(String utilUserId, String pvsnInstCd, String patId) {
		this.utilUserId = utilUserId;
		this.pvsnInstCd = pvsnInstCd;
		this.patId = patId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patId == null) ? 0 : patId.hashCode());
		result = prime * result + ((pvsnInstCd == null) ? 0 : pvsnInstCd.hashCode());
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
		TfnMdsidxId other = (TfnMdsidxId) obj;
		if (patId == null) {
			if (other.patId != null)
				return false;
		} else if (!patId.equals(other.patId))
			return false;
		if (pvsnInstCd == null) {
			if (other.pvsnInstCd != null)
				return false;
		} else if (!pvsnInstCd.equals(other.pvsnInstCd))
			return false;
		if (utilUserId == null) {
			if (other.utilUserId != null)
				return false;
		} else if (!utilUserId.equals(other.utilUserId))
			return false;
		return true;
	}

}
