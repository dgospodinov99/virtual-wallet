package com.team01.web.virtualwallet.controllers.MVC;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CustomErrorController implements ErrorController {

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,
                              Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("error", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error404";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error401";
            }  else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error400";
            }    else if (statusCode == HttpStatus.CONFLICT.value()) {
                return "error409";
            }   else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error500";
            }

        }
        return getErrorPath();
    }

    public String getErrorPath() {
        return "/error";
    }
}