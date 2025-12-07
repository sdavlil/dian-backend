package com.dian.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorBase;

    private Double iva;
    private Double retencion;

    @Column(nullable = false)
    private String medioPago; // PSE, Nequi, Banco

    private String estadoPago; // Pendiente, Aprobado, Rechazado
}
