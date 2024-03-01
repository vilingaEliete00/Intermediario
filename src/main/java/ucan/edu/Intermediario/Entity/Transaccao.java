/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucan.edu.Intermediario.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author elietevilinga
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaccao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = false)
    private double montante;
    @ManyToOne
    @JoinColumn(name = "id_contaOrigem", referencedColumnName = "id")
    private Conta contaOrigem;
    @ManyToOne
    @JoinColumn(name = "id_contaDestino", referencedColumnName = "id")
    private Conta contaDestino;
    @Column(unique = false)
    private Date data;
    @Column(unique = false)
    private Time hora;
    @Column(unique = false)
    private String nome;
    @Column(unique = false)
    private int tipo;
    @Column(unique = false)
    private String estado;
    @Column(unique = false)
    private String descricao;
}
