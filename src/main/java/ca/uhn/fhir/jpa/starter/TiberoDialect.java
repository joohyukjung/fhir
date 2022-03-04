package ca.uhn.fhir.jpa.starter;

import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.SQL2008StandardLimitHandler;

/**
 * Hibernate, JPA 사용시 TiberoDB 접속을 위한 Dialect 생성
 * 
 * @author smlee
 *
 */
public class TiberoDialect extends Oracle12cDialect {
//	@Override
//	public String getQuerySequencesString() {
//		return "select sequence_name from all_sequences";
//	}

//	@Override
//	public LimitHandler getLimitHandler() {
//		return SQL2008StandardLimitHandler.INSTANCE;
//	}
}
