package com.entitymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entitymanagement.db.CatalogOperationsDAO;
import com.entitymanagement.schema.Catalog;

@Service
public class CatalogService {
	
	
	@Autowired
	CatalogOperationsDAO catalogDao;
	
	@Transactional
	public void addProduct(Catalog catalog) throws  Exception
	{
		try{
		validateCatalogData(catalog);
		catalogDao.addProduct(catalog);
		}
		catch(Exception e)
		{
			throw new Exception(e);
		}
		
	}
	
	private void validateCatalogData(Catalog catalog) throws Exception
	{
		try{
		
		if(catalog.getProduct().size() == 0) throw new Exception("No product provided.");
		if(catalog.getProduct().get(0).getProductname() == null || catalog.getProduct().get(0).getProductname().trim().length() == 0) throw new Exception("Product name cannot be null or empty");
		if(catalog.getProduct().get(0).getProducttype() == null) throw new Exception("Product type cannot be null or empty");
		if(catalog.getProduct().get(0).getProducttype().getProducttypename() == null || catalog.getProduct().get(0).getProducttype().getProducttypename().trim().length() ==0) throw new Exception("Product type name cannot be null or empty");
		
		
		}
		catch(Exception e)
		{
			throw new Exception(e);
		}
	}
	
	public Catalog getAllProducts() throws Exception
	{
		try{
		return catalogDao.getAllProductDetails();
		}
		 catch(Exception e)
		{
			 throw new Exception(e);
		}
	}
	
	
	public Catalog getProductDetails(String productId) throws Exception
	{
		try{
			if(productId == null || productId.trim().length() == 0) throw new Exception("product id cannot be null or empty..");
			
			return catalogDao.getProductDetails(productId);
		}
		 catch(Exception e)
		{
			 throw new Exception(e);
		}
	}
	
}
