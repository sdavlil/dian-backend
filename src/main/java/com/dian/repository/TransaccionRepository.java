package com.dian.repository;

import com.dian.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByMedioPagoIgnoreCase(String medioPago);
}
