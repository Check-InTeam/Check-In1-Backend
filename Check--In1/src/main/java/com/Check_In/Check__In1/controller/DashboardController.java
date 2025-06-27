package com.Check_In.Check__In1.controller;

import com.Check_In.Check__In1.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/administrador/dashboard")
    public String adminDashboard(HttpSession session) {

        User user = (User) session.getAttribute("usuarioLogueado");

        if (user == null || !"ADMINISTRADOR".equalsIgnoreCase(user.getRole().getNombre())) {
            return "redirect:/login";
        }

        return "administrador/dashboard";
    }

    @GetMapping("/instructor/dashboard")
    public String instructorDashboard(HttpSession session) {
        User user = (User) session.getAttribute("usuarioLogueado");

        if (user == null || !"INSTRUCTOR".equalsIgnoreCase(user.getRole().getNombre())) {
            return "redirect:/login";
        }

        return "instructor/dashboard";
    }

    @GetMapping("/aprendiz/dashboard")
    public String aprendizDashboard(HttpSession session) {
        User user = (User) session.getAttribute("usuarioLogueado");

        if (user == null || !"APRENDIZ".equalsIgnoreCase(user.getRole().getNombre())) {
            return "redirect:/login";
        }

        return "aprendiz/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
