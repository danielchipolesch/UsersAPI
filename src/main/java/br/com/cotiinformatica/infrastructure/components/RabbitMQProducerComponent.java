package br.com.cotiinformatica.infrastructure.components;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.cotiinformatica.domain.entities.User;

@Component
public class RabbitMQProducerComponent {
	@Autowired RabbitTemplate rabbitTemplate;
	@Autowired ObjectMapper objectMapper;
	@Autowired Queue queue;
	
	public void sendMessage(User user) throws Exception {
		String json = objectMapper.writeValueAsString(user);
		rabbitTemplate.convertAndSend(queue.getName(), json);
	}
}