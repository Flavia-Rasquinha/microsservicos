package br.com.alurafood.pagamentos.configuracao;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAGAMENTO_REQUEST_ROUT_KEY = "pagamento-request-rout-key";
    public static final String PAGAMENTO_REQUEST_EXCHANGE = "pagamento-request-exchange";
    public static final String PAGAMENTO_REQUEST_QUEUE = "pagamento-request-queue";

    private CachingConnectionFactory connectionFactory;

    public RabbitConfig(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .nonDurable(PAGAMENTO_REQUEST_QUEUE)
                .build();
    }

    @Bean
    public Exchange notificationExchange() {
        return ExchangeBuilder.topicExchange(PAGAMENTO_REQUEST_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(PAGAMENTO_REQUEST_ROUT_KEY)
                .noargs();
    }
}
