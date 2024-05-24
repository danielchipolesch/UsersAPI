package br.com.cotiinformatica.infrastructure.components;
import java.time.Instant;
import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.cotiinformatica.domain.entities.MessageLogger;
import br.com.cotiinformatica.domain.entities.User;
import br.com.cotiinformatica.infrastructure.repositories.MessageLoggerRepository;

@Component
public class RabbitMQConsumerComponent {
    @Autowired MessageLoggerRepository messageLoggerRepository;
    @Autowired MailMessageComponent mailMessageComponent;
    @Autowired ObjectMapper objectMapper;

    @RabbitListener(queues = { "${queue.name}" })
    public void proccessMessage(@Payload String message) {

        MessageLogger messageLogger = new MessageLogger();
        messageLogger.setId(UUID.randomUUID());
        messageLogger.setCreatedAt(Instant.now());

        try {
            //deserializar os dados do usuário gravado na fila
            User user = objectMapper.readValue(message, User.class);

            //Criando o conteúdo do email
            String to = user.getEmail();
            String subject = "Parabéns, seu cadastro foi realizado com sucesso!";
            String body = "Olá, " + user.getName() + "\nSua conta de usuário foi criada no sistema!\n\nAtt\nEquipe COTI";

            //Enviando o email
            mailMessageComponent.send(to, subject, body);

            messageLogger.setStatus("SUCESSO");
            messageLogger.setUser(user);
            messageLogger.setMessage("Email de boas vindas enviado com sucesso para: " + user.getEmail());
        }
        catch(Exception e) {
            messageLogger.setStatus("ERRO");
            messageLogger.setMessage(e.getMessage());
        }
        finally {
            messageLoggerRepository.save(messageLogger);
        }
    }
}