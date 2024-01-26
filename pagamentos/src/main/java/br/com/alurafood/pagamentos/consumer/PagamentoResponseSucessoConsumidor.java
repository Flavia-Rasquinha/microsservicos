package br.com.alurafood.pagamentos.consumer;

import br.com.alurafood.pagamentos.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.LocalDateTime;

public class PagamentoResponseSucessoConsumidor {

    @Autowired
    private PagamentoService pagamentoService;

    @RabbitListener(queues = {"pagamento-response-sucesso-queue"})
    public void receive(@Payload Message message) {
        System.out.println("Message " + message + "" + LocalDateTime.now());
        String payload = String.valueOf(message.getPayload());

        pagamentoService.sucessoPagamento(payload);
    }
}
