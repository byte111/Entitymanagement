package com.entitymanagement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class EntityManagementApplication {

	
	@RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home(HttpServletRequest req) {
		
       // return "Nothing here. Go to <a href='/sample'>/sample</a>";
		
		return "EntityManagement is loaded. You can perform operations as below: <br/>"+
		" Get All Product Details - /getproducts <br/>"+
		" To add a new product  - /addproduct <br/>" ;
	
    }

	
	public static void main(String[] args) {
		SpringApplication.run(EntityManagementApplication.class, args);
	}
}
