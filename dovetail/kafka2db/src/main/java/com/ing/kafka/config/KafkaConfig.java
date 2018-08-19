package com.ing.kafka.config;

import java.util.Collection;
import java.util.List;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {
	
	@Autowired
	private KafkaTemplate template;
	
	@KafkaListener(topics = "foo")
	//@SendTo("#{myBean.replyTopic}") // config time SpEL
	public Collection<String> replyingBatchListener(List<String> in) {
		System.out.println(in);
		return in;
	}
	
	@Bean
	public NewTopic test() {
		System.out.println(template.getDefaultTopic());
		 template.send("foo", "bar");
		 return new NewTopic("replyTopic", 10, (short) 2);
	}

	@Bean
	public NewTopic createTopic() {
	    return new NewTopic("foo", 10, (short) 2);
	}

	@Bean
	public NewTopic topic2() {
	    return new NewTopic("bar", 10, (short) 2);
	}
}
