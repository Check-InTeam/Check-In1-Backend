package com.Check_In.Check__In1.service;

import com.Check_In.Check__In1.entity.CarnetDigital;
import com.Check_In.Check__In1.repository.CarnetDigitalRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarnetDigitalService {

    @Autowired
    private CarnetDigitalRepository carnetDigitalRepository;

    public List<CarnetDigital> getAllCarnetDigital(){
        return carnetDigitalRepository.findAll();
    }

    public Optional<CarnetDigital> getCarnetDigitalById(int id){
        return carnetDigitalRepository.findById(id);
    }

    public List<CarnetDigital> getCarnetDigitalByUserId(int UserId){
        return carnetDigitalRepository.findByUserId(UserId);
    }

    public List<CarnetDigital> getCarnetsByNombreCompleto(String nombreCompleto) {
        return carnetDigitalRepository.findByNombreCompleto(nombreCompleto);
    }

    public CarnetDigital saveCarnet(CarnetDigital carnetDigital) {
        return carnetDigitalRepository.save(carnetDigital);
    }

    public void deleteCarnet(int id) {
        carnetDigitalRepository.deleteById(id);
    }

}
