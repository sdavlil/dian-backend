package com.dian.service;

import com.dian.model.Transaccion;
import com.dian.repository.TransaccionRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final WebClient webClient;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
        this.webClient = WebClient.create("http://localhost:8001"); // FastAPI
    }

    // 🔹 LLAMAR AL MICROSERVICIO FASTAPI PARA CALCULAR IVA Y RETENCIÓN
    private Map<String, Double> calcularImpuestos(Double valorBase, String medioPago) {
        return webClient.post()
                .uri("/calcular-impuestos")
                .bodyValue(Map.of(
                        "valorBase", valorBase,
                        "medioPago", medioPago
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Double>>() {})
                .block();
    }

    // 🔹 PROCESAR LA TRANSACCIÓN COMPLETA
    public Transaccion procesarTransaccion(Transaccion transaccion) {

        Map<String, Double> calculos = calcularImpuestos(
                transaccion.getValorBase(),
                transaccion.getMedioPago()
        );

        transaccion.setIva(calculos.get("iva"));
        transaccion.setRetencion(calculos.get("retencion"));
        transaccion.setEstadoPago("Pendiente");

        return transaccionRepository.save(transaccion);
    }

    // 🔹 Obtener todas
    public List<Transaccion> obtenerTodas() {
        return transaccionRepository.findAll();
    }

    // 🔹 Obtener por ID
    public Transaccion obtenerPorId(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }

    // 🔹 Cambiar estado
    public Transaccion actualizarEstado(Long id, String nuevoEstado) {
        Transaccion t = obtenerPorId(id);
        t.setEstadoPago(nuevoEstado);
        return transaccionRepository.save(t);
    }

    // 🔹 Obtener por medio de pago
    public List<Transaccion> obtenerPorMedioPago(String medioPago) {
        return transaccionRepository.findByMedioPagoIgnoreCase(medioPago);
    }
}
