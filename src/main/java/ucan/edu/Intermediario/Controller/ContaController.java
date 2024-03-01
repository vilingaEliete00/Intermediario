/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucan.edu.Intermediario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucan.edu.Intermediario.Service.ContaService;
import ucan.edu.Intermediario.Service.TransaccaoService;

/**
 *
 * @author elietevilinga
 */
@RestController
@RequestMapping("/api")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private TransaccaoService transaService;

    @PostMapping("/consultar")
    public ResponseEntity<String> consultarConta(@RequestParam String numeroConta) {
        contaService.consultaConta(numeroConta);
        return ResponseEntity.ok("Solicitação de consulta de conta enviada para o Kafka.");
    }

    @PostMapping("/historico")
    public ResponseEntity<String> consultarHistorico(@RequestParam String numeroConta) {
        transaService.consultaHistorico(numeroConta);
        return ResponseEntity.ok("Solicitação de Historico de conta enviada para o Kafka.");
    }
}
