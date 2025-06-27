package com.Check_In.Check__In1.controller;

import com.Check_In.Check__In1.entity.Programacion;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.service.ProgramacionService;
import com.Check_In.Check__In1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/programacion")
public class ProgramacionController {

    @Autowired
    private ProgramacionService programacionService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<List<Programacion>> listProgramacion(
            @RequestParam(required = false) String ficha,
            @RequestParam(required = false) String ambiente,
            @RequestParam(required = false) String nombreAsignatura)
    {
        if (ficha != null){
            return ResponseEntity.ok(programacionService.getProgramacionByFicha(ficha));
        } else if(ambiente != null){
            return ResponseEntity.ok(programacionService.getProgramacionByAmbiente(ambiente));
        } else if(nombreAsignatura != null){
            return ResponseEntity.ok(programacionService.getProgramacionByNombreAsignatura(nombreAsignatura));
        } else  {
            return ResponseEntity.ok(programacionService.getAllProgramacion());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readProgramacion(@PathVariable int id){
        Optional<Programacion> programacion = programacionService.getProgramacionById(id);
        return programacion.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProgramacion(@RequestBody Programacion programacion, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para crear una programacion");
        }

        programacion.setUser(userOpt.get());

        Programacion nueva = programacionService.saveProgramacion(programacion);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProgramacion(@PathVariable int id, @RequestBody Programacion programacionUpdate, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tienes permiso para editar una programación");
        }

        Optional<Programacion> actualOpt = programacionService.getProgramacionById(id);
        if (actualOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Programacion actual = actualOpt.get();
        actual.setNombreAsignatura(programacionUpdate.getNombreAsignatura());
        actual.setDescripcion(programacionUpdate.getDescripcion());
        actual.setFicha(programacionUpdate.getFicha());
        actual.setFechaInicio(programacionUpdate.getFechaInicio());
        actual.setFechaFin(programacionUpdate.getFechaFin());
        actual.setHoraInicio(programacionUpdate.getHoraInicio());
        actual.setHoraFin(programacionUpdate.getHoraFin());
        actual.setAmbiente(programacionUpdate.getAmbiente());

        return ResponseEntity.ok(programacionService.saveProgramacion(actual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgramacion(@PathVariable int id, @RequestParam int userId){
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().getNombre().equalsIgnoreCase("ADMINISTRADOR")){
            return ResponseEntity.status(403).body("No tiene permisos para eliminar una programación");
        }

        Optional<Programacion> programacion = programacionService.getProgramacionById(id);
        if (programacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        programacionService.deleteProgramacion(id);
        return ResponseEntity.ok("Programacion eliminada correctamente");
    }
}

