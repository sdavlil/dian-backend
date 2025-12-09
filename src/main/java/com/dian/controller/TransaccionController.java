package com.dian.controller;

import com.dian.model.Transaccion;
import com.dian.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaccion")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // 🔹 1. Crear transacción (YA EXISTENTE)
    @PostMapping
    public ResponseEntity<Transaccion> crearTransaccion(@RequestBody Transaccion transaccion) {
        Transaccion transaccionProcesada = transaccionService.procesarTransaccion(transaccion);
        return ResponseEntity.ok(transaccionProcesada);
    }

    // 🔹 2. Listar todas las transacciones
    @GetMapping
    public ResponseEntity<List<Transaccion>> obtenerTransacciones() {
        return ResponseEntity.ok(transaccionService.obtenerTodas());
    }

    // 🔹 3. Obtener transacción por ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> obtenerPorId(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.obtenerPorId(id);
        return ResponseEntity.ok(transaccion);
    }

    // 🔹 4. Aprobar transacción
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Transaccion> aprobarTransaccion(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.actualizarEstado(id, "Aprobado");
        return ResponseEntity.ok(transaccion);
    }

    // 🔹 5. Rechazar transacción
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Transaccion> rechazarTransaccion(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.actualizarEstado(id, "Rechazado");
        return ResponseEntity.ok(transaccion);
    }
}
