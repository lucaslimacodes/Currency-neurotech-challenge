package com.neurotech.currencyAPI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "Cambio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cambio {

    // yyyy-MM-dd
    @Id
    @Column(name = "dataCambio")
    private Date dataCambio;

    @Column(name = "cotacaoCompra",nullable = false)
    private float cotacaoCompra;

    @Column(name = "cotacaoVenda",nullable = false)
    private float cotacaoVenda;
}
