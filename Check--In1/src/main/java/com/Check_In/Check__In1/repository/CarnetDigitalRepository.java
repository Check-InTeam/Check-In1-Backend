package com.Check_In.Check__In1.repository;

import com.Check_In.Check__In1.entity.CarnetDigital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarnetDigitalRepository extends JpaRepository<CarnetDigital, Integer> {
    List<CarnetDigital> findByUserId(int userId);

    List<CarnetDigital> findByNombreCompleto(String nombreCompleto);
}

