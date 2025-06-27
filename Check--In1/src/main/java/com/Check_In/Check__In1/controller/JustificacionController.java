package com.Check_In.Check__In1.controller;

import com.Check_In.Check__In1.entity.EstadoJustificacion;
import com.Check_In.Check__In1.entity.Justificacion;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.service.JustificacionService;
import com.Check_In.Check__In1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/justificacion")
public class JustificacionController {

    @Autowired
    private JustificacionService justificacionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createjustificacion(@RequestBody Justificacion justificacion, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        String role = user.getRole().getNombre();

        if (!role.equalsIgnoreCase("APRENDIZ") && !role.equalsIgnoreCase("INSTRUCTOR")) {
            return ResponseEntity.status(400).body("No puedes crear una justificacion");
        }

        justificacion.setUser(user);
        justificacion.setEstado(EstadoJustificacion.EnProceso);
        return ResponseEntity.ok(justificacionService.saveJustificacion(justificacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readJustificacion(@PathVariable int id, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<Justificacion> justificacionOpt = justificacionService.getJustificacionById(id);

        if (userOpt.isEmpty() || justificacionOpt.isEmpty())
            return  ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        Justificacion justificacion = justificacionOpt.get();

        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR") || justificacion.getUser().getId() == user.getRole().getId()) {
            return ResponseEntity.ok(justificacion);
        }

        return ResponseEntity.status(400).body("No puedes crear una justificacion ya que no tienes permiso para hacerlo");
    }

    @GetMapping("/administrador-ver-justificaciones")
    public ResponseEntity<?> readAllJustificaciones(@RequestParam int userId) {
        Optional<User> administrador = userService.getUserById(userId);
        if (administrador.isEmpty() || !administrador.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.status(404).body("Acceso denegado");
        }
        return ResponseEntity.ok(justificacionService.getAllJustificacion());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJustificacion(@PathVariable int id, @RequestBody Justificacion newJustificacion, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<Justificacion> justificacionOpt = justificacionService.getJustificacionById(id);

        if (userOpt.isEmpty() || justificacionOpt.isEmpty())
            return   ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        Justificacion justificacion = justificacionOpt.get();
        String role = user.getRole().getNombre();

        if (role.equalsIgnoreCase("ADMINISTRADOR") || justificacion.getUser().getId() == user.getId()) {
            if (newJustificacion.getMotivo() != null) justificacion.setMotivo(newJustificacion.getMotivo());
            if (newJustificacion.getFecha() != null) justificacion.setFecha(newJustificacion.getFecha());
            if (newJustificacion.getArchivo() != null) justificacion.setArchivo(newJustificacion.getArchivo());
            if (role.equalsIgnoreCase("ADMINISTRADOR") && newJustificacion.getEstado() != null) justificacion.setEstado(newJustificacion.getEstado());
            return ResponseEntity.ok(justificacionService.saveJustificacion(justificacion));
        }

        return ResponseEntity.status(403).body("No puedes actualizar una justificacion debido a que no tienes permiso para hacerlo");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteJustificacion(@PathVariable int id, @RequestParam int userId) {
        Optional<User> administrador = userService.getUserById(userId);
        if (administrador.isEmpty() || !administrador.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        justificacionService.deleteJustificacion(id);
        return ResponseEntity.ok("Justificación eliminada");
    }

    @GetMapping("/filtrar/nombre")
    public ResponseEntity<?> filtrarPorNombre(@RequestParam String nombre, @RequestParam int userId) {
        Optional<User> administrador = userService.getUserById(userId);
        if (administrador.isEmpty() || !administrador.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.status(403).body("Solo el administrador tiene permiso para filtrar por nombre");
        }

        return ResponseEntity.ok(justificacionService.getJustificacionByUserNombre(nombre));
    }

    @GetMapping("/filtrar/fecha")
    public ResponseEntity<?> filtrarPorFecha(@RequestParam String fecha, @RequestParam int userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return  ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        List<Justificacion> justificacion = justificacionService.getJustificacionByFecha(LocalDate.parse(fecha));

        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return  ResponseEntity.ok(justificacion);
        } else {
            return ResponseEntity.ok(justificacion.stream().filter(j -> j.getUser().getId() == user.getId()).toList());
        }
    }
}