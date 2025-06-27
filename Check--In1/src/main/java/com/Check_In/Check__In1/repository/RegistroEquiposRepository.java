package com.Check_In.Check__In1.repository;

import com.Check_In.Check__In1.entity.RegistroEquipos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroEquiposRepository extends JpaRepository<RegistroEquipos, Integer> {

    List<RegistroEquipos> findByMarca(String marca);
    List<RegistroEquipos> findBySerialEquipo(String serial_equipo);
    List<RegistroEquipos> findByUser_Nombre(String nombre);
}
