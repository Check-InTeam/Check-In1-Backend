package com.Check_In.Check__In1.service;

import com.Check_In.Check__In1.entity.Justificacion;
import com.Check_In.Check__In1.repository.JustificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JustificacionService {

    @Autowired
    JustificacionRepository justificacionRepository;

    public List<Justificacion> getAllJustificacion(){
        return justificacionRepository.findAll();
    }

    public Optional<Justificacion> getJustificacionById(int id){
        return justificacionRepository.findById(id);
    }

    public List<Justificacion> getJustificacionByFecha(LocalDate fecha){
        return justificacionRepository.findByFecha(fecha);
    }

    public List<Justificacion> getJustificacionByUserNombre(String nombre){
        return justificacionRepository.findByUser_Nombre(nombre);
    }

    public Justificacion saveJustificacion(Justificacion justificacion) {
        return justificacionRepository.save(justificacion);
    }

    public void deleteJustificacion(int id) {
        justificacionRepository.deleteById(id);
    }
}
