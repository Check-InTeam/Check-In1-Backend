package com.Check_In.Check__In1.repository;

import com.Check_In.Check__In1.entity.Justificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JustificacionRepository extends JpaRepository<Justificacion, Integer> {

    List<Justificacion> findByFecha(LocalDate fecha);
    List<Justificacion> findByUser_Nombre(String nombre);
}
