package br.com.alurafood.pagamentos.producer;

import br.com.alurafood.pagamentos.dto.PagamentoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpUnsupportedEncodingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static br.com.alurafood.pagamentos.configuracao.RabbitConfig.PAGAMENTO_REQUEST_EXCHANGE;
import static br.com.alurafood.pagamentos.configuracao.RabbitConfig.PAGAMENTO_REQUEST_ROUT_KEY;

@Component
public class PagamentoRequestProducer {

    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(PagamentoRequestProducer.class);

    public PagamentoRequestProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendSubscription(PagamentoDto notification) {
        logger.info("Enviando a notificação para a exchange: {} ", PAGAMENTO_REQUEST_EXCHANGE);
        rabbitTemplate.send(PAGAMENTO_REQUEST_EXCHANGE, PAGAMENTO_REQUEST_ROUT_KEY, buildNotificationMessage(notification));
    }

    private Message buildNotificationMessage(Object object) {
        return MessageBuilder
                .withBody(convertObjectToByte(object))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
    }

    private byte[] convertObjectToByte(Object object) {
        try {
            return objectMapper.writeValueAsString(object).getBytes();
        } catch (JsonProcessingException e) {
            logger.error("Não foi possível converter o objeto para byte {}", e.getMessage());
            throw new AmqpUnsupportedEncodingException(e);
        }
    }
}