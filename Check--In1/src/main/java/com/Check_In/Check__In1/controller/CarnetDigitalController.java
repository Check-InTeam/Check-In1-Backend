package com.Check_In.Check__In1.controller;


import com.Check_In.Check__In1.entity.CarnetDigital;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.service.CarnetDigitalService;
import com.Check_In.Check__In1.service.UserService;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carnet")
public class CarnetDigitalController {

    @Autowired
    private CarnetDigitalService  carnetDigitalService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");
        User user = userOpt.get();

        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.ok(carnetDigitalService.getAllCarnetDigital());
        } else {
            return ResponseEntity.ok(carnetDigitalService.getCarnetDigitalByUserId(user.getId()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<CarnetDigital> carnetOpt = carnetDigitalService.getCarnetDigitalById(id);

        if (userOpt.isEmpty() || carnetOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        CarnetDigital carnet = carnetOpt.get();


        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR") || carnet.getUser().getId() == user.getId()) {
            return ResponseEntity.ok(carnet);
        } else {
            return ResponseEntity.status(403).body("No tienes permisos para ver este carnet");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCarnetDigital(@RequestBody CarnetDigital carnetDigital, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        carnetDigital.setUser(user);
        return ResponseEntity.ok(carnetDigitalService.saveCarnet(carnetDigital));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarnetDigital(@PathVariable int id, @RequestBody CarnetDigital nuevo, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<CarnetDigital>  carnetOpt = carnetDigitalService.getCarnetDigitalById(id);

        if (userOpt.isEmpty() || carnetOpt.isEmpty()) return ResponseEntity.status(404).body("No encontrado");

        User user = userOpt.get();
        CarnetDigital actual = carnetOpt.get();

        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR") && actual.getUser().getId() == user.getId()) {
            return ResponseEntity.status(403).body("No tienes permiso para actualizar el carnet");
        }

        actual.setNombreCompleto(nuevo.getNombreCompleto());
        actual.setFicha(nuevo.getFicha());
        actual.setPrograma(nuevo.getPrograma());
        actual.setJornada(nuevo.getJornada());
        actual.setFoto(nuevo.getFoto());

        return ResponseEntity.ok(carnetDigitalService.saveCarnet(actual));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteCarnetDigital(@PathVariable int id, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para eliminar el carnet");
        }

        carnetDigitalService.deleteCarnet(id);
        return ResponseEntity.ok("Carnet eliminado correctamente");
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombreCompleto, @RequestParam int userId){
        Optional<User>  userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("Solo el administrador puede buscar por el nombre");
        }

        return ResponseEntity.ok(carnetDigitalService.getCarnetsByNombreCompleto(nombreCompleto));
    }
}
