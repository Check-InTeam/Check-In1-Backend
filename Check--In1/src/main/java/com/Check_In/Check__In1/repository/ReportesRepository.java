package com.Check_In.Check__In1.repository;

import com.Check_In.Check__In1.entity.Reportes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportesRepository extends JpaRepository<Reportes, Integer> {

    List<Reportes> findByUserId(int userId);
    List<Reportes> findByUserNombre(String nombre);

    int countByUser_Nombre(String nombre);
}
