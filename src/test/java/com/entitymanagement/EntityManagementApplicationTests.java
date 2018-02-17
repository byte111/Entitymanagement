package com.entitymanagement;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.entitymanagement.schema.Catalog;
import com.entitymanagement.schema.Complextypeattribute;
import com.entitymanagement.schema.ObjectFactory;
import com.entitymanagement.schema.Product;
import com.entitymanagement.schema.Producttype;
import com.entitymanagement.schema.Simpletypeattribute;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityManagementApplicationTests {

	@Test
	public void contextLoads() {
		
	}

	public static  void testCreateProduct()
	{
		try{
		ObjectMapper mapper = new ObjectMapper();
		
		JAXBContext context = JAXBContext.newInstance(Catalog.class);
		Marshaller m  = context.createMarshaller();
		
		Catalog catalog = new ObjectFactory().createCatalog();
		Product product = new ObjectFactory().createProduct();
		
		Producttype producttype = new ObjectFactory().createProducttype();
		
		producttype.setProducttypename("Electronics");
		producttype.setProducttypedescription("Electronic items ");
		product.setProductname("testproduct2");
		product.setProductdescription("test product2 description ");
		product.setProducttype(producttype);
		catalog.getProduct().add(product);
		//System.out.println(catalog.getProduct().size());
		java.util.List<Simpletypeattribute> simpletypeattributeslist = product.getSimpletypeattribute();
		
		
		Simpletypeattribute attr1 = new Simpletypeattribute();
		attr1.setAttributename("price");
		attr1.setAttributevalue("1600");
		
		simpletypeattributeslist.add(attr1);
		
		Simpletypeattribute attr2 = new Simpletypeattribute();
		attr2.setAttributename("color");
		attr2.setAttributevalue("Black");
		
		simpletypeattributeslist.add(attr2);
		Complextypeattribute complexattrb1 = new Complextypeattribute();
		
		complexattrb1.setAttributename("complex attrb1 ");
		
		Product test  = new Product();
		test.setProductid("1517116915013");
		test.setProductname("testproduct1");
		test.setProductdescription("test product description ");
		
		Producttype test1 = new Producttype();
		test1.setProducttypeid("1517116914990");
		test1.setProducttypename("Electronics");
		test1.setProducttypedescription("Electronic items ");
		test.setProducttype(test1);
		complexattrb1.setAttributevalue(test);
		product.getComplextypeattribute().add(complexattrb1);
		
		//mapper.writeValue(new File("C:\\Temp\\hackathon\\catalog.txt"), catalog);
		m.marshal(catalog, new FileOutputStream(new File("C:\\Temp\\hackathon\\catalog.txt")));
		System.out.println(catalog.getProduct().size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		testCreateProduct();
	}
}
