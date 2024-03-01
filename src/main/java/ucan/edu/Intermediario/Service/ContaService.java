/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucan.edu.Intermediario.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ucan.edu.Intermediario.Entity.Conta;
import ucan.edu.Intermediario.Entity.Transaccao;

/**
 *
 * @author elietevilinga
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ContaService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    public void consultaConta(String numeroConta) {

        String contaObtida = obterBancoDaConta(numeroConta);
        switch (contaObtida) {
            case "bancoa":
                kafkaTemplate.send("consulta", numeroConta);
                break;
            case "bancob":
                kafkaTemplate.send("consultaB", numeroConta);
                break;
            case "bancoc":
                kafkaTemplate.send("consultaC", numeroConta);
                break;
            default:
                log.error("Numero Conta: {} Desconhecido", numeroConta);
        }

    }

    @KafkaListener(topics = "consultar", groupId = "minha-consulta", containerFactory = "jsonContainerFactory")
    public void consumirMensagem(String contaJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Conta conta = objectMapper.readValue(contaJson, Conta.class);
            log.info("A Conta é {}", conta);

        } catch (JsonProcessingException e) {
            log.error("Erro ao desserializar a mensagem JSON", e);
        }
    }

    @KafkaListener(topics = "historicoRecebe", groupId = "recebe", containerFactory = "jsonContainerFactory")
    public void consumeMensagem(String contaJson) {

        log.info("O historico da transaccao de conta é {}", contaJson);

    }

    public String obterBancoDaConta(String numeroContaDestino) {

        if (numeroContaDestino.startsWith("0001")) {
            return "bancoa";
        } else if (numeroContaDestino.startsWith("0002")) {
            return "bancob";
        } else if (numeroContaDestino.startsWith("0003")) {
            return "bancoc";
        } else {
            return null;
        }
    }

}
