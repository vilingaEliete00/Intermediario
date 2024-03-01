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
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ucan.edu.Intermediario.DTO.TransaccaoDTO;
import ucan.edu.Intermediario.Entity.Conta;
import ucan.edu.Intermediario.Entity.Transaccao;

/**
 *
 * @author elietevilinga
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class TransaccaoService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplates;
    private final KafkaTemplate<String, TransaccaoDTO> kafkaTempl;

    @Autowired
    private ContaService contaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "meu-topico", groupId = "my-group", containerFactory = "jsonContainerFactory")
    public void consumirMensagem(String transaccaoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TransaccaoDTO transaccao = objectMapper.readValue(transaccaoJson, TransaccaoDTO.class);


            String numeroContaDestino = transaccao.getNumeroContaDestino();
            String bancoDestino = obterBancoDaConta(transaccao.getNumeroContaDestino());
            String bancoOrigem = obterBancoDaConta(transaccao.getNumeroContaOrigem());
            if (bancoDestino != null) {
                log.info("A transação será enviada para o banco: {}", bancoDestino);
                kafkaTemplate.send("topico-" + bancoDestino, transaccao);
            } else {

                if (bancoOrigem != null) {
                    switch (bancoOrigem) {
                        case "bancoa":
                            kafkaTemplate.send("verificarBancoA", "Este numero de conta, não faz referência a nenhum banco existente");
                            break;
                        case "bancob":
                            kafkaTemplate.send("verificarBancoB", "Este numero de conta, não faz referência a nenhum banco existente");
                            break;
                    }
                }

            }
        } catch (JsonProcessingException e) {

        }
    }

    private void enviarMensagemBanco(TransaccaoDTO transaccao) {

        try {
            log.info("Transacao Recebida: {}", transaccao);
            kafkaTemplate.send("meu-intermediario", transaccao);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o Banco", e);

        }
    }

    //Consultar Historico de conta
    public void consultaHistorico(String numeroConta) {
        String contaObtida = obterBancoDaConta(numeroConta);
        switch (contaObtida) {
            case "bancoa":
                kafkaTemplate.send("historico", numeroConta);
                break;
            case "bancob":
                kafkaTemplate.send("historicoB", numeroConta);
                break;
            case "bancoc":
                kafkaTemplate.send("historicoC", numeroConta);
                break;
            default:
                log.error("Numero Conta: {} Desconhecido", numeroConta);
        }

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

    @KafkaListener(topics = "topico-intermed", groupId = "inter", containerFactory = "jsonContainerFactory")
    public void consumirNumeroConta(String transaccaoJson) throws ObjectNotFoundException, JsonProcessingException {

        TransaccaoDTO transacao = objectMapper.readValue(transaccaoJson, TransaccaoDTO.class);
        String bancoOrigem = obterBancoDaConta(transacao.getNumeroContaOrigem());
        if (bancoOrigem != null) {
            switch (bancoOrigem) {
                case "bancoa":
                    kafkaTemplate.send("resposta-transa", transacao);
                    break;
                case "bancob":
                    kafkaTemplate.send("resposta-transacao", transacao);
                    break;
                case "bancoc":
                    kafkaTemplate.send("resposta-transac", transacao);
                    break;
                default:
                    log.error("Banco de destino desconhecido para a transação com conta de origem: {}", transacao.getNumeroContaOrigem());
            }
        } else {
            log.error("Banco de destino desconhecido para a transação com conta de origem: {}", transacao.getNumeroContaOrigem());
        }

    }

    @KafkaListener(topics = "topico-intermedia", groupId = "inter", containerFactory = "jsonContainerFactory")
    public void consumirNumeroContaC(String transaccaoJson) throws ObjectNotFoundException, JsonProcessingException {

        TransaccaoDTO transacao = objectMapper.readValue(transaccaoJson, TransaccaoDTO.class);
        String bancoOrigem = obterBancoDaConta(transacao.getNumeroContaOrigem());
        if (bancoOrigem != null) {
            switch (bancoOrigem) {
                case "bancoa":
                    kafkaTemplate.send("resposta-transa", transacao);
                    break;
                case "bancob":
                    kafkaTemplate.send("resposta-transacao", transacao);
                    break;
                case "bancoc":
                    kafkaTemplate.send("resposta-transac", transacao);
                    break;
                default:
                    log.error("Banco de destino desconhecido para a transação com conta de origem: {}", transacao.getNumeroContaOrigem());
            }
        } else {
            log.error("Banco de destino desconhecido para a transação com conta de origem: {}", transacao.getNumeroContaOrigem());
        }
        //kafkaTempl.send("resposta-transac", transacao);
    }

    @KafkaListener(topics = "topico-intermedian", groupId = "inter", containerFactory = "jsonContainerFactory")
    public void consumirNumeroContaB(String transaccaoJson) throws ObjectNotFoundException, JsonProcessingException {

        TransaccaoDTO transacao = objectMapper.readValue(transaccaoJson, TransaccaoDTO.class);
        String bancoOrigem = obterBancoDaConta(transacao.getNumeroContaOrigem());
        if (bancoOrigem != null) {
            switch (bancoOrigem) {
                case "bancoa":
                    kafkaTemplate.send("resposta-transa", transacao);
                    break;
                case "bancob":
                    kafkaTemplate.send("resposta-transacao", transacao);
                    break;
                case "bancoc":
                    kafkaTemplate.send("resposta-transac", transacao);
                    break;
                default:

            }
        } else {

        }
    }

}
