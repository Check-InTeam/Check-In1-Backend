package com.Check_In.Check__In1.controller;

import com.Check_In.Check__In1.entity.RegistroEquipos;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.service.RegistroEquiposService;
import com.Check_In.Check__In1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/registroEquipos")
public class RegistroEquiposController {

    @Autowired
    private RegistroEquiposService registroEquiposService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if(userOpt.isEmpty()) return ResponseEntity.status(404).body("El usuario no fue encontrado");

        User user = userOpt.get();
        String role = user.getRole().getNombre();

        if (role.equalsIgnoreCase("ADMINISTRADOR")) {
            return ResponseEntity.ok(registroEquiposService.getAllRegistroEquipos());
        } else {
            return ResponseEntity.ok(registroEquiposService.getRegistroEquiposByUserNombre(user.getNombre()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<RegistroEquipos> registroEquiposOpt = registroEquiposService.getRegistroEquiposById(id);

        if (userOpt.isEmpty() || registroEquiposOpt.isEmpty())
            return ResponseEntity.status(404).body("No encontrado");

        User user = userOpt.get();
        RegistroEquipos registroEquipos = registroEquiposOpt.get();

        if (user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR") || registroEquipos.getUser().getId() == user.getId()) {
            return ResponseEntity.ok(registroEquipos);
        } else {
            return ResponseEntity.status(403).body("No tienes permiso para observar");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RegistroEquipos registroEquipos, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("Usuario no encontrado");

        User user = userOpt.get();
        registroEquipos.setUser(user);
        return ResponseEntity.ok(registroEquiposService.saveRegistroEquipos(registroEquipos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody RegistroEquipos nuevo,  @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        Optional<RegistroEquipos> registroEquiposOpt = registroEquiposService.getRegistroEquiposById(id);

        if (userOpt.isEmpty() || registroEquiposOpt.isEmpty())
            return ResponseEntity.status(404).body("No encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para actualizar");
        }

        RegistroEquipos actual = registroEquiposOpt.get();
        actual.setTipo(nuevo.getTipo());
        actual.setMarca(nuevo.getMarca());
        actual.setSerialEquipo(nuevo.getSerialEquipo());
        actual.setDescripcion(nuevo.getDescripcion());

        return ResponseEntity.ok(registroEquiposService.saveRegistroEquipos(actual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("Usuario no fue encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes ningun permiso para eliminar");
        }

        registroEquiposService.deleteRegistroEquiposById(id);
        return ResponseEntity.ok("Registro eliminado");
    }

    @GetMapping("/buscar/marca")
    public ResponseEntity<?> buscarByMarca(@RequestParam String marca, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(404).body("Usuario no fue encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para buscar");
        }

        return ResponseEntity.ok(registroEquiposService.getRegistroEquiposByMarca(marca));
    }

    @GetMapping("/buscar/numero-serial")
    public ResponseEntity<?> buscarBySerial(@RequestParam String serialEquipo, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty())
            return  ResponseEntity.status(404).body("Usuario no fue encontrado");

        User user = userOpt.get();
        if (!user.getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para buscar");
        }

        return ResponseEntity.ok(registroEquiposService.getRegistroEquiposBySerialEquipo(serialEquipo));
    }
}
