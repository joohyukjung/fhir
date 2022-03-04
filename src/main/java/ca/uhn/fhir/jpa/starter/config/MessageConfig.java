package ca.uhn.fhir.jpa.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import ca.uhn.fhir.jpa.starter.myhw.message.MyhwMessageSource;

/**
 * @ClassName : MessageConfig.java
 * @Description : 예외처리 메시지
 * @Modification
 *
 * <pre>
 * 수정일              수정자        수정내용
 * --------- ------- ------------------------
 * 2022.02.07 이선민     최초작성
 * </pre>
 *
 * @author 이선민
 * @since 2022.02.07
 * @version 1.0
 * @see
 */
@Configuration
public class MessageConfig {

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/myhw/message/message_command");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(60);
		
		return messageSource;
	}
	
	@Bean
	public MyhwMessageSource myhwMessageSource() {
		MyhwMessageSource messageSource = new MyhwMessageSource();
		messageSource.setReloadableResourceBundleMessageSource(messageSource());
		
		return messageSource;
	}
	
	@Bean
	public MessageSourceAccessor messaSourceAccessor() {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource());
		
		return messageSourceAccessor;
	}
}
