package com.Check_In.Check__In1.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
    @Table(name = "justificacion")
public class Justificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDate fecha;


    private String archivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoJustificacion estado = EstadoJustificacion.EnProceso;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public EstadoJustificacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoJustificacion estado) {
        this.estado = estado;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
