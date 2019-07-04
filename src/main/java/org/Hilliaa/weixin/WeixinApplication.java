package org.Hilliaa.weixin;

import org.Hilliaa.weixin.domain.InMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@SpringBootApplication
public class WeixinApplication {
	
	
	@Bean
	public XmlMapper xmlMapper() {
		XmlMapper mapper = new XmlMapper();
		return mapper;
	}
	
	@Bean
	public RedisTemplate<String, ? extends InMessage> inMessageTemplate(
			@Autowired RedisConnectionFactory connectionFactory){
		
		RedisTemplate<String, ? extends InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
		
		
	}

	public static void main(String[] args) {
		SpringApplication.run(WeixinApplication.class, args);
	}

}
