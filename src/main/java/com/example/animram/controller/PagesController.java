package com.example.animram.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
	public class PagesController {
		
		 @GetMapping("/")
		    public String index() {
		        return "pages/index";
		    }
		 
		 @GetMapping("/top")
		    public String index2() {
		        return "animals/top";
		}
		 
		 /*@GetMapping("/shop")
		 	public String shopIndex() {
			 	return "shops/shop_index";
		 }*/
		 
		
}
