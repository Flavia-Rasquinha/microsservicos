package br.com.alurafood.pedidos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAGAMENTO_RESPONSE_SUCESSO_ROUT_KEY = "pagamento-response-sucesso-rout-key";
    public static final String PAGAMENTO_RESPONSE_SUCESSO_EXCHANGE = "pagamento-response-sucesso-exchange";
    public static final String PAGAMENTO_RESPONSE_SUCESSO_QUEUE = "pagamento-response-sucesso-queue";

    public static final String PAGAMENTO_RESPONSE_ERRO_ROUT_KEY = "pagamento-response-erro-rout-key";
    public static final String PAGAMENTO_RESPONSE_ERRO_EXCHANGE = "pagamento-response-erro-exchange";
    public static final String PAGAMENTO_RESPONSE_ERRO_QUEUE = "pagamento-response-erro-queue";
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
    public Exchange notificationExchange() {
        return ExchangeBuilder.topicExchange(PAGAMENTO_RESPONSE_SUCESSO_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(PAGAMENTO_RESPONSE_SUCESSO_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(PAGAMENTO_RESPONSE_SUCESSO_ROUT_KEY)
                .noargs();
    }

    @Bean
    public Exchange pagamentoResponseErroExchange() {
        return ExchangeBuilder.topicExchange(PAGAMENTO_RESPONSE_ERRO_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue pagamentoResponseErroQueue() {
        return new Queue(PAGAMENTO_RESPONSE_ERRO_QUEUE, true);
    }

    @Bean
    public Binding pagamentoResponseErroBinding() {
        return BindingBuilder
                .bind(pagamentoResponseErroQueue())
                .to(pagamentoResponseErroExchange())
                .with(PAGAMENTO_RESPONSE_ERRO_ROUT_KEY)
                .noargs();
    }
}
