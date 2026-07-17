package com.purificadora.app.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("activePath")
    public String activePath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
