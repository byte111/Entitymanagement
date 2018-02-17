package com.entitymanagement;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entitymanagement.schema.Catalog;
import com.entitymanagement.service.CatalogService;

@RestController
public class CatalogController {

	/*@Autowired
    DataSource dataSource;*/
	
	@Autowired
	CatalogService catalogSerivce;
	
	
	@RequestMapping("/catalogtest")
	public Map<String,String> sample(@RequestParam(value="name", defaultValue="World") String name) {
		Map<String,String> result = new HashMap<>();
		result.put("message", String.format("Catalog is loading..., %s", name));
		return result;
	}

	@POST
	@RequestMapping("/addproduct")
	@Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	@Produces({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	public Response addProduct(@RequestBody Catalog catalog) {
			
		try{
			catalogSerivce.addProduct(catalog);
			return  Response.status(HttpStatus.CREATED.value()).entity(catalog).build();
		}
		catch(Exception e)
		{
			return Response.status(HttpStatus.BAD_REQUEST.value()).entity(e.getMessage()).build();
		}
		
	}
	
	@GET
	@RequestMapping("/getproducts")
	@Produces({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	public Response getAllProductDetails()
	{
		try{
			Catalog data = catalogSerivce.getAllProducts();
			return  Response.status(HttpStatus.OK.value()).entity(data).build();
		}
		catch(Exception e){
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).entity(e.getMessage()).build();
		}
	}
	
	
	@GET
	@RequestMapping("/getproducts/{productid}")
	@Produces({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	public Response getProductDetails(@PathVariable("productid") String productid)
	{
		try{
			Catalog data = catalogSerivce.getProductDetails(productid);
			return  Response.status(HttpStatus.OK.value()).entity(data).build();
		}
		catch(Exception e){
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).entity(e.getMessage()).build();
		}
	}
	
}
