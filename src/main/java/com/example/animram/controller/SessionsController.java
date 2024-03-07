package com.example.animram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
	public class SessionsController {

	    @GetMapping("/login")
	    public String index() {
	        return "sessions/new";
	    }

	    @GetMapping("/login-failure")
	    public String loginFailure(Model model) {
	        model.addAttribute("hasMessage", true);
	        model.addAttribute("class", "alert-danger");
	        model.addAttribute("message", "Emailまたはパスワードに誤りがあります。");

	        return "sessions/new";
	    }

	    @GetMapping("/logout-complete")
	    public String logoutComplete(Model model) {
	        model.addAttribute("hasMessage", true);
	        model.addAttribute("class", "alert-info");
	        model.addAttribute("message", "ログアウトしました。");

	        return "redirect:/login";
	    }
	}

