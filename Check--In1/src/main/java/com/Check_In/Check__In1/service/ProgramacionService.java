package com.Check_In.Check__In1.service;


import com.Check_In.Check__In1.entity.Programacion;
import com.Check_In.Check__In1.repository.ProgramacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramacionService {

    @Autowired
    private ProgramacionRepository programacionRepository;

    public List<Programacion> getAllProgramacion() {
        return programacionRepository.findAll();
    }

    public Optional<Programacion> getProgramacionById(int id) {
        return programacionRepository.findById(id);
    }

    public Programacion saveProgramacion(Programacion programacion) {
        return programacionRepository.save(programacion);
    }

    public void deleteProgramacion(int id) {
        programacionRepository.deleteById(id);
    }

    public List<Programacion> getProgramacionByFicha(String ficha) {
        return programacionRepository.findByFicha(ficha);
    }

    public List<Programacion> getProgramacionByAmbiente(String ambiente) {
        return programacionRepository.findByAmbiente(ambiente);
    }

    public List<Programacion> getProgramacionByNombreAsignatura(String nombre_asignatura) {
        return programacionRepository.findByNombreAsignatura(nombre_asignatura);
    }



}
