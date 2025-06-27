package com.Check_In.Check__In1.service;

import com.Check_In.Check__In1.entity.Reportes;
import com.Check_In.Check__In1.entity.TipoReporte;
import com.Check_In.Check__In1.entity.User;
import com.Check_In.Check__In1.repository.ReportesRepository;
import com.Check_In.Check__In1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReportesRepository reportesRepository;

    @Autowired
    private UserRepository userRepository;

    public void registrarFallaYGenerarReporte(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("El usuario no existe"));

        int totalFallas = reportesRepository.countByUser_Nombre(user.getNombre());

        totalFallas += 1;

        Reportes reporte = new Reportes();
        reporte.setUser(user);
        reporte.setTitulo("Reporte de Fallas");
        reporte.setDescripcion("El usuario tiene " + totalFallas + " fallas acumuladas.");
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setTipo(TipoReporte.Fallas);
        reporte.setNumeroFallas(totalFallas);
        reporte.setAdvertencia(totalFallas >= 3);

        reportesRepository.save(reporte);

    }

    public Reportes saveReporte(Reportes reporte) {
        return reportesRepository.save(reporte);
    }

    public Optional<Reportes> getReporteById(int id) {
        return  reportesRepository.findById(id);
    }

    public List<Reportes> getAllReportes() {
        return reportesRepository.findAll();
    }

    public void deleteReporteById(int id) {
        reportesRepository.deleteById(id);
    }

    public List<Reportes> getReportesByUserId(int userId) {
        return reportesRepository.findByUserId(userId);
    }

    public List<Reportes> getReportesByUserNombre(String nombre) {
        return reportesRepository.findByUserNombre(nombre);
    }
}
