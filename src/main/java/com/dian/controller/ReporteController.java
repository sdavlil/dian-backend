package com.dian.controller;

import com.dian.model.Transaccion;
import com.dian.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reporte")
public class ReporteController {

    private final TransaccionService transaccionService;

    public ReporteController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // ---- ENDPOINTS DE CADA MÓDULO -----------------------------------

    @PostMapping("/ecommerce")
    public ResponseEntity<Transaccion> reporteEcommerce(@RequestBody Transaccion transaccion) {
        transaccion.setMedioPago("E-COMMERCE");
        Transaccion procesada = transaccionService.procesarTransaccion(transaccion);
        return ResponseEntity.ok(procesada);
    }

    @PostMapping("/pse")
    public ResponseEntity<Transaccion> reportePSE(@RequestBody Transaccion transaccion) {
        transaccion.setMedioPago("PSE");
        Transaccion procesada = transaccionService.procesarTransaccion(transaccion);
        return ResponseEntity.ok(procesada);
    }

    @PostMapping("/banco")
    public ResponseEntity<Transaccion> reporteBanco(@RequestBody Transaccion transaccion) {
        transaccion.setMedioPago("BANCO");
        Transaccion procesada = transaccionService.procesarTransaccion(transaccion);
        return ResponseEntity.ok(procesada);
    }

    @PostMapping("/nequi")
    public ResponseEntity<Transaccion> reporteNequi(@RequestBody Transaccion transaccion) {
        transaccion.setMedioPago("NEQUI");
        Transaccion procesada = transaccionService.procesarTransaccion(transaccion);
        return ResponseEntity.ok(procesada);
    }

    // ---- ENDPOINTS DE CONSULTA POR ORIGEN ----------------------------

    @GetMapping("/{medioPago}")
    public ResponseEntity<List<Transaccion>> obtenerPorOrigen(@PathVariable String medioPago) {
        List<Transaccion> lista = transaccionService.obtenerPorMedioPago(medioPago.toUpperCase());
        return ResponseEntity.ok(lista);
    }
}
