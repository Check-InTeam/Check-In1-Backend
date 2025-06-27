package com.Check_In.Check__In1.controller;

import com.Check_In.Check__In1.entity.Reportes;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.service.ReporteService;
import com.Check_In.Check__In1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    public ReporteService reporteService;

    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity<?> createReporte(@RequestBody Reportes reporte, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("El usuario no pudo ser encontrado");

        User user = userOpt.get();
        String role = user.getRole().getNombre();

        if (!role.equalsIgnoreCase("INSTRUCTOR")) {
            return ResponseEntity.status(403).body("Únicamente los instructores pueden crear un reporte");
        }

        reporte.setUser(user);
        reporte.setFechaCreacion(LocalDateTime.now());

        Reportes newReporte = reporteService.saveReporte(reporte);
        return ResponseEntity.ok(newReporte);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readReporte(@PathVariable int id, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<Reportes> reportesOpt = reporteService.getReporteById(id);

        if (userOpt.isEmpty() || reportesOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El reporte o el usuario no fueron encontrados");
        }

        User user = userOpt.get();
        Reportes reporte = reportesOpt.get();

        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR") ||
                (user.getRole().getNombre().equalsIgnoreCase("APRENDIZ") && reporte.getUser().getId() == user.getId()) ||
                (user.getRole().getNombre().equalsIgnoreCase("INSTRUCTOR") && reporte.getUser().getId() == user.getId())) {
            return ResponseEntity.ok(reporte);
        }

        return ResponseEntity.status(403).body("No tiene permiso para ver este reporte");
    }

    @GetMapping("/administrador")
    public ResponseEntity<?> readAllReportes(@RequestParam int userId) {
        Optional<User> adminOpt = userService.getUserById(userId);

        if (adminOpt.isEmpty() || !adminOpt.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.status(403).body("Únicamente el administrador puede ver todos los reportes");
        }

        List<Reportes> reportes = reporteService.getAllReportes();
        return ResponseEntity.ok(reportes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReporte(@PathVariable int id, @RequestBody Reportes reporteUpdate, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<Reportes> reportesOpt = reporteService.getReporteById(id);

        if (userOpt.isEmpty() || reportesOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El reporte o el usuario no fueron encontrados");
        }

        User user = userOpt.get();
        Reportes reporteNew = reportesOpt.get();
        String role = user.getRole().getNombre();

        if (!role.equalsIgnoreCase("INSTRUCTOR") && !role.equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.status(403).body("No tiene permiso para actualizar este reporte");
        }

        if (reporteUpdate.getTitulo() != null) reporteNew.setTitulo(reporteUpdate.getTitulo());
        if (reporteUpdate.getDescripcion() != null) reporteNew.setDescripcion(reporteUpdate.getDescripcion());
        if (reporteUpdate.getFechaCreacion() != null) reporteNew.setFechaCreacion(reporteUpdate.getFechaCreacion());
        if (reporteUpdate.getTipo() != null) reporteNew.setTipo(reporteUpdate.getTipo());
        reporteNew.setNumeroFallas(reporteUpdate.getNumeroFallas());
        reporteNew.setAdvertencia(reporteUpdate.isAdvertencia());

        Reportes actualizado = reporteService.saveReporte(reporteNew);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReporte(@PathVariable int id, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El usuario no fue encontrado");
        }

        User user = userOpt.get();
        String role = user.getRole().getNombre();

        if (!role.equalsIgnoreCase("ADMINISTRADOR") && !role.equalsIgnoreCase("INSTRUCTOR")) {
            return ResponseEntity.status(403).body("No puedes eliminar reportes ya que no tienes permisos");
        }

        Optional<Reportes> reportesOpt = reporteService.getReporteById(id);
        if (reportesOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El reporte no pudo ser encontrado");
        }

        Reportes reporte = reportesOpt.get();

        if (role.equalsIgnoreCase("INSTRUCTOR") && reporte.getUser().getId() != user.getId()) {
            return ResponseEntity.status(403).body("No tiene permiso para eliminar este reporte");
        }

        reporteService.deleteReporteById(id);
        return ResponseEntity.ok("El reporte fue eliminado correctamente");
    }

    @GetMapping("/filtrar/nombre")
    public ResponseEntity<?> filtrarNombre(@RequestParam String nombre, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("El usuario no fue encontrado");

        User user = userOpt.get();
        String role = user.getRole().getNombre();

        if (!role.equalsIgnoreCase("ADMINISTRADOR") && !role.equalsIgnoreCase("INSTRUCTOR")) {
            return ResponseEntity.status(403).body("No tienes permiso para filtrar reportes");
        }

        return ResponseEntity.ok(reporteService.getReportesByUserNombre(nombre));
    }

    @GetMapping("/mis-reportes")
    public ResponseEntity<?> misReportes(@RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("El usuario no fue encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("APRENDIZ")) {
            return ResponseEntity.status(403).body("Únicamente los aprendices pueden ver sus propios reportes");
        }

        return ResponseEntity.ok(reporteService.getReportesByUserId(userId));
    }
}
