package br.com.alurafood.pedidos.consumer;

import br.com.alurafood.pedidos.producer.PagamentoErroProducer;
import br.com.alurafood.pedidos.producer.PagamentoSucessoProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PagamentoRequestConsumidor {
    @Autowired
    private PagamentoErroProducer erroProducer;
    @Autowired
    private PagamentoSucessoProducer sucessoProducer;

    @RabbitListener(queues = {"pagamento-request-queue"})
    public void receberMensagem(@Payload Message message){
        System.out.println(message);
        if(new Random().nextBoolean()){
            sucessoProducer.gerarResposta("Mensagem de sucesso Pagamento " + message );
        }else {
            erroProducer.gerarResposta("ERRO no pagamento " + message);
        }

    }
}
