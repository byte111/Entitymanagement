package com.entitymanagement.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.entitymanagement.schema.*;
import com.entitymanagement.utility.Utils;
import com.entitymanagement.vo.AttributeValue;
import com.entitymanagement.vo.ProductAttributeVO;

@Repository
public class CatalogOperationsDAO extends JdbcDaoSupport  {

	@Autowired 
	DataSource dataSource;

	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
		jdbcTemplate = createJdbcTemplate(dataSource);
	}

	private  JdbcTemplate jdbcTemplate = null;



	public static String insertProductType = "INSERT INTO producttype(producttypeid,producttypename,producttypedescription) VALUES (?,?,?)";

	//public static String getLastProductTypeId =  "select max(producttypeid) from producttype";

	//public static String getLastProductId =  "select max(productid) from product";

	public static String insertProduct =  "INSERT INTO product(productid,producttypeid,productname,productdescription) VALUES (?,?,?,?)";

	public static String insertAttrbute =  "INSERT INTO attribute(ATTRIBUTEID,ATTRIBUTENAME,ATTRIBUTEDESCRIPTION) VALUES (?,?,?)";

	public static String insertAttrbuteValue =  "INSERT INTO attributevalue(ATTRIBUTEVALID,ATTRIBUTEVAL,ISSUBENTITY) VALUES (?,?,?)";

	public static String insertProductAttrbute =  "INSERT INTO productspecificattribute(PRODUCTID,ATTRIBUTEID,ATTRIBUTEVALID) VALUES (?,?,?)";


	public static String getAllProducts = "SELECT P.PRODUCTID AS PRODUCTID,P.PRODUCTNAME AS PRODUCTNAME, P.PRODUCTDESCRIPTION AS PRODUCTDESCRIPTION,"+
			"PT.PRODUCTTYPEID as PRODUCTTYPEID,PT.PRODUCTTYPENAME as PRODUCTTYPENAME ,PT.PRODUCTTYPEDESCRIPTION as PRODUCTTYPEDESCRIPTION "+
			"from product P, producttype PT "+
			"WHERE P.PRODUCTTYPEID = PT.PRODUCTTYPEID ";
	
	public static String getAttributeForProduct = "select * from ( "+
			" select PA.PRODUCTID as PRODUCTID ,PA.ATTRIBUTEID as ATTRIBUTEID,PA.ATTRIBUTEVALID as ATTRIBUTEVALID ,"
			+ "A.ATTRIBUTENAME as ATTRIBUTENAME ,A.ATTRIBUTEDESCRIPTION as ATTRIBUTEDESCRIPTION ,AV.ATTRIBUTEVAL as ATTRIBUTEVAL,"+
			" AV.ISSUBENTITY as  ISSUBENTITY from productspecificattribute PA , attribute A,attributevalue AV where "+
			" PA.attributeid = A.attributeid and " +
			" PA.attributevalid = AV.attributevalid)G1 where G1.productid = ? ";

	
	public static String getProduct = "SELECT P.PRODUCTID AS PRODUCTID,P.PRODUCTNAME AS PRODUCTNAME, P.PRODUCTDESCRIPTION AS PRODUCTDESCRIPTION,"+
			"PT.PRODUCTTYPEID as PRODUCTTYPEID,PT.PRODUCTTYPENAME as PRODUCTTYPENAME ,PT.PRODUCTTYPEDESCRIPTION as PRODUCTTYPEDESCRIPTION "+
			"from product P, producttype PT "+
			"WHERE P.PRODUCTTYPEID = PT.PRODUCTTYPEID and "
			+ " P.PRODUCTID = ? ";
	public static String checkExistingAttributes = "SELECT ATTRIBUTEID , ATTRIBUTENAME , ATTRIBUTEDESCRIPTION FROM  ATTRIBUTE WHERE attributename = ? ";
	public static String checkExistingAttributeValues ="SELECT ATTRIBUTEVALID,ATTRIBUTEVAL,ISSUBENTITY FROM  attributevalue WHERE ATTRIBUTEVAL = ? and ISSUBENTITY= ? ";
	
	public static String checkExistingProductValues ="select productid,producttypeid,productname,productdescription from product where productid = ? ";
	
	public Catalog getAllProductDetails() throws Exception
	{
		try
		{
			List<Map<String, Object>> productdetsailslist = new  ArrayList<Map<String, Object>>();
			productdetsailslist = jdbcTemplate.queryForList(getAllProducts);


			Iterator it= productdetsailslist.iterator();
			List<Product> productdetsailslistresult = new ArrayList<Product>();
			Catalog catalog = new ObjectFactory().createCatalog();
			while(it.hasNext())
			{
				Map<String, Object> map = (Map<String, Object>) it.next();
				Product temp = new  ObjectFactory().createProduct();				

				temp.setProductid(map.get("PRODUCTID").toString());
				temp.setProductname(map.get("PRODUCTNAME").toString());
				temp.setProductdescription(map.get("PRODUCTDESCRIPTION").toString());


				Producttype prodtype = new ObjectFactory().createProducttype();
				prodtype.setProducttypeid(map.get("PRODUCTTYPEID").toString());
				prodtype.setProducttypename(map.get("PRODUCTTYPENAME").toString());

				if(map.get("PRODUCTTYPEDESCRIPTION")!=null)
				{
					prodtype.setProducttypedescription(map.get("PRODUCTTYPEDESCRIPTION").toString());
				}
				temp.setProducttype(prodtype);

				// get attribute list for every product

				List<Map<String, Object>> atributedetsailslist = new  ArrayList<Map<String, Object>>();
				atributedetsailslist = jdbcTemplate.queryForList(getAttributeForProduct, new Object[]{temp.getProductid()});

				Iterator it1= atributedetsailslist.iterator();

				List<Simpletypeattribute> atributedetsailslistresult = new ArrayList<Simpletypeattribute>();
				while(it1.hasNext())
				{
					Map<String, Object> map1 = (Map<String, Object>) it1.next();
					if("N".equals(map1.get("ISSUBENTITY")) )
					{
						Simpletypeattribute simpletypeattrb = new ObjectFactory().createSimpletypeattribute();

						simpletypeattrb.setAttributeid(map1.get("ATTRIBUTEID").toString());
						simpletypeattrb.setAttributename(map1.get("ATTRIBUTENAME").toString());
						simpletypeattrb.setAttributevalue(map1.get("ATTRIBUTEVAL").toString());
						if(map1.get("ATTRIBUTEDESCRIPTION")!=null){
							simpletypeattrb.setAttributedescription(map1.get("ATTRIBUTEDESCRIPTION").toString());
						}

						atributedetsailslistresult.add(simpletypeattrb);
					}
					//else store it in complextype attribute which needs further processing

				}

				temp.getSimpletypeattribute().addAll(atributedetsailslistresult);
				productdetsailslistresult.add(temp);

			}			

			catalog.getProduct().addAll(productdetsailslistresult);
			return catalog;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	
	
	public Catalog getProductDetails(String productId) throws Exception
	{
		try
		{
			Catalog catalog = new ObjectFactory().createCatalog();
			Product product  = jdbcTemplate.queryForObject(getProduct, new Object[]{productId}, new RowMapper<Product>(){

				@Override
				public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
					Product product = new Product();
					product.setProductid(rs.getString("PRODUCTID"));
					product.setProductname(rs.getString("PRODUCTNAME"));
					product.setProductdescription(rs.getString("PRODUCTDESCRIPTION"));


					Producttype prodtype = new ObjectFactory().createProducttype();
					prodtype.setProducttypeid(rs.getString("PRODUCTTYPEID"));
					prodtype.setProducttypename(rs.getString("PRODUCTTYPENAME"));					
					prodtype.setProducttypedescription(rs.getString("PRODUCTTYPEDESCRIPTION"));
					
					product.setProducttype(prodtype);
					return product;
				}});
			
			
			// get attribute list for every product

			List<Map<String, Object>> atributedetsailslist = new  ArrayList<Map<String, Object>>();
			atributedetsailslist = jdbcTemplate.queryForList(getAttributeForProduct, new Object[]{product.getProductid()});

			Iterator it1= atributedetsailslist.iterator();

			List<Simpletypeattribute> atributedetsailslistresult = new ArrayList<Simpletypeattribute>();
			while(it1.hasNext())
			{
				Map<String, Object> map1 = (Map<String, Object>) it1.next();
				if("N".equals(map1.get("ISSUBENTITY")) )
				{
					Simpletypeattribute simpletypeattrb = new ObjectFactory().createSimpletypeattribute();

					simpletypeattrb.setAttributeid(map1.get("ATTRIBUTEID").toString());
					simpletypeattrb.setAttributename(map1.get("ATTRIBUTENAME").toString());
					simpletypeattrb.setAttributevalue(map1.get("ATTRIBUTEVAL").toString());
					if(map1.get("ATTRIBUTEDESCRIPTION")!=null){
						simpletypeattrb.setAttributedescription(map1.get("ATTRIBUTEDESCRIPTION").toString());
					}

					atributedetsailslistresult.add(simpletypeattrb);
				}
				//else store it in complextype attribute which needs further processing

			}

			product.getSimpletypeattribute().addAll(atributedetsailslistresult);
			catalog.getProduct().add(product);
			return catalog;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	
	
	
	public void addProduct(Catalog catalog) throws SQLException, Exception
	{
		try{
			//System.out.println("dataSource2="+dataSource+"jdbcTemplate = "+jdbcTemplate);

			// check if product type already exists , if not then insert		
			String productTypeId = addProductType(catalog);

			// insert product		
			String productId = addProduct(catalog,productTypeId);

			// add corresponding attributes

			addAttributeList(catalog,productId);
		}
		catch(Exception e)
		{
			throw new Exception(e);
		}	

	}

	private void addAttributeList_org(Catalog catalog,String productId) throws Exception
	{
		List<Simpletypeattribute> simpletypeattrblist = catalog.getProduct().get(0).getSimpletypeattribute();

		List<Complextypeattribute> complextypeattrblist = catalog.getProduct().get(0).getComplextypeattribute();

		List<Simpletypeattribute> simpletypeattrbtoaddlist = new LinkedList<Simpletypeattribute>();

		List<Complextypeattribute> complextypeattrbtoaddlist = new LinkedList<Complextypeattribute>();

		List<AttributeValue> attrbvaltoadd = new LinkedList<AttributeValue>();

		List<ProductAttributeVO> productattrblist = new LinkedList<ProductAttributeVO>();

		if(simpletypeattrblist!=null && simpletypeattrblist.size() > 0)
		{
			for(Simpletypeattribute attr : simpletypeattrblist)
			{
				ProductAttributeVO prodAttributeobj = new ProductAttributeVO();
				prodAttributeobj.setProductId(productId);
				Simpletypeattribute temp  = checkExistingAttributes(catalog, attr.getAttributename());
				if( temp == null)
				{
					//add it in list to insert this attribute 
					attr.setAttributeid(Utils.getNewId());
					prodAttributeobj.setAttriuteId(attr.getAttributeid());
					simpletypeattrbtoaddlist.add(attr);
				}
				else{
					prodAttributeobj.setAttriuteId(temp.getAttributeid());
				}



				//check if the value is existing , if not then insert 
				AttributeValue attrbval =  checkExistingAttributeValues(catalog,attr.getAttributevalue(),"N");

				if(attrbval == null)
				{
					attrbval = new AttributeValue();
					attrbval.setAttributevalueid(Utils.getNewId());
					attrbval.setAttributevalue(attr.getAttributevalue());
					attrbval.setIsSubEntity("N");

					attrbvaltoadd.add(attrbval);
				}
				prodAttributeobj.setAttributeValueId(attrbval.getAttributevalueid());




				productattrblist.add(prodAttributeobj);
			}

			// batch update for all attributes
			if(simpletypeattrbtoaddlist.size() > 0){
				addAttributesSimple(simpletypeattrbtoaddlist);
			}
			// bath update for attributre values
			if(attrbvaltoadd.size() > 0)
			{
				addAttributeValues(attrbvaltoadd);
			}

			// batch update in the productattribute table
			if(productattrblist.size() > 0)
			{
				List<ProductAttributeVO> productattrblisttoadd = new LinkedList<ProductAttributeVO>();
				for(ProductAttributeVO prodattr : productattrblist)
				{
					if(checkExistingProductAttributeValues(prodattr.getAttriuteId(),prodattr.getAttributeValueId()) == null){
						productattrblisttoadd.add(prodattr);
					}				

				}
				if(productattrblisttoadd.size() > 0){
					addProductAttribute(productattrblisttoadd);
				}
			}

		}

		
	}
	
	private void addAttributeList(Catalog catalog,String productId) throws Exception
	{
		List<Simpletypeattribute> simpletypeattrblist = catalog.getProduct().get(0).getSimpletypeattribute();

		List<Complextypeattribute> complextypeattrblist = catalog.getProduct().get(0).getComplextypeattribute();

		List<Simpletypeattribute> simpletypeattrbtoaddlist = new LinkedList<Simpletypeattribute>();

		List<Complextypeattribute> complextypeattrbtoaddlist = new LinkedList<Complextypeattribute>();

		List<AttributeValue> attrbvaltoadd = new LinkedList<AttributeValue>();

		List<ProductAttributeVO> productattrblist = new LinkedList<ProductAttributeVO>();

		getSimpleTypeAttributeList(catalog, productId, simpletypeattrblist, simpletypeattrbtoaddlist, attrbvaltoadd, productattrblist);
		getComplexTypeAttributeList(catalog, productId, complextypeattrblist, complextypeattrbtoaddlist, attrbvaltoadd, productattrblist);
		
		
					// batch update for all attributes
					if(simpletypeattrbtoaddlist.size() > 0){
						addAttributesSimple(simpletypeattrbtoaddlist);
					}
					
					if(complextypeattrbtoaddlist.size() > 0)
					{
						addAttributesComplex(complextypeattrbtoaddlist);
					}
					
					// bath update for attributre values
					if(attrbvaltoadd.size() > 0)
					{
						addAttributeValues(attrbvaltoadd);
					}

					// batch update in the productattribute table
					if(productattrblist.size() > 0)
					{
						List<ProductAttributeVO> productattrblisttoadd = new LinkedList<ProductAttributeVO>();
						for(ProductAttributeVO prodattr : productattrblist)
						{
							if(checkExistingProductAttributeValues(prodattr.getAttriuteId(),prodattr.getAttributeValueId()) == null){
								productattrblisttoadd.add(prodattr);
							}				

						}
						if(productattrblisttoadd.size() > 0){
							addProductAttribute(productattrblisttoadd);
						}
					}
		
	}
	
	private void getSimpleTypeAttributeList(Catalog catalog,String productId,List<Simpletypeattribute> simpletypeattrblist,
			List<Simpletypeattribute> simpletypeattrbtoaddlist,List<AttributeValue> attrbvaltoadd,List<ProductAttributeVO> productattrblist) throws Exception{
		if(simpletypeattrblist!=null && simpletypeattrblist.size() > 0)
		{
			for(Simpletypeattribute attr : simpletypeattrblist)
			{
				if(attr.getAttributevalue() == null || attr.getAttributevalue().trim().length() == 0){
					System.out.println("Ignoring attribute " + attr.getAttributename() + " as there is no corresponding value associated.");
					continue;
				}
				ProductAttributeVO prodAttributeobj = new ProductAttributeVO();
				prodAttributeobj.setProductId(productId);
				Simpletypeattribute temp  = checkExistingAttributes(catalog, attr.getAttributename());
				if( temp == null)
				{
					//add it in list to insert this attribute 
					attr.setAttributeid(Utils.getNewId());
					prodAttributeobj.setAttriuteId(attr.getAttributeid());
					simpletypeattrbtoaddlist.add(attr);
				}
				else{
					prodAttributeobj.setAttriuteId(temp.getAttributeid());
				}



				//check if the value is existing , if not then insert 
				AttributeValue attrbval =  checkExistingAttributeValues(catalog,attr.getAttributevalue(),"N");

				if(attrbval == null)
				{
					attrbval = new AttributeValue();
					attrbval.setAttributevalueid(Utils.getNewId());
					attrbval.setAttributevalue(attr.getAttributevalue());
					attrbval.setIsSubEntity("N");

					attrbvaltoadd.add(attrbval);
				}
				prodAttributeobj.setAttributeValueId(attrbval.getAttributevalueid());




				productattrblist.add(prodAttributeobj);
			}

			

		}
	}
	private void getComplexTypeAttributeList(Catalog catalog,String productId, List<Complextypeattribute> complextypeattrblist,
			List<Complextypeattribute> complextypeattrbtoaddlist,List<AttributeValue> attrbvaltoadd ,List<ProductAttributeVO> productattrblist) throws Exception{
				
		if(complextypeattrblist !=null && complextypeattrblist.size() > 0)
		{
			for(Complextypeattribute attr : complextypeattrblist)
			{
				if(attr.getAttributevalue() == null ){
					System.out.println("Ignoring attribute " + attr.getAttributename() + " as there is no corresponding value associated.");
					continue;
				}
				ProductAttributeVO prodAttributeobj = new ProductAttributeVO();
				prodAttributeobj.setProductId(productId);
				Simpletypeattribute temp  = checkExistingAttributes(catalog, attr.getAttributeid());

				if( temp == null)
				{
					//add it in list to insert this attribute 
					attr.setAttributeid(Utils.getNewId());
					prodAttributeobj.setAttriuteId(attr.getAttributeid());
					complextypeattrbtoaddlist.add(attr);
				}
				else{
					prodAttributeobj.setAttriuteId(temp.getAttributeid());
				}

				//check if the value is existing , if not then insert 

				AttributeValue attrbval =  checkExistingAttributeValues(catalog,attr.getAttributevalue(),"Y");

				if(attrbval == null)
				{
					// this attribute aka product does not exist in attributevalue table, so check if there is an entry in product table with the same attrval value
					Product prod = checkExistingProductValues(attr.getAttributevalue().getProductid());
					
					if(prod == null) {
						// this means that in the sub-entity we tried to insert a value which does not exists in product table. stop execution
						
						throw new Exception("Provided attribute " + attr.getAttributename() + " does not exist.");
					}
					attrbval = new AttributeValue();
					attrbval.setAttributevalueid(Utils.getNewId());
					attrbval.setAttributevalue(attr.getAttributevalue().getProductid());
					attrbval.setIsSubEntity("Y");			

					attrbvaltoadd.add(attrbval);
				}
				prodAttributeobj.setAttributeValueId(attrbval.getAttributevalueid());
				// make an entry in productspecificattribute table
				productattrblist.add(prodAttributeobj);
			}
		}
	}
	private  String addProductType(Catalog catalog) throws SQLException, Exception
	{
		try{
			String productTypeId = isProductTypeExists(catalog);
			if(productTypeId == null)
			{
				String producttypeid = Utils.getNewId();

				if(producttypeid == null) throw new Exception("Error in forming a new id for product type.");
				productTypeId = producttypeid;
				int	producttypeinsertresult = jdbcTemplate.execute(insertProductType, new PreparedStatementCallback<Integer>() {


					@Override
					public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						ps.setString(1, producttypeid);
						ps.setString(2, catalog.getProduct().get(0).getProducttype().getProducttypename());
						ps.setString(3, catalog.getProduct().get(0).getProducttype().getProducttypedescription());

						return  ps.executeUpdate();
					}	
				});
			}

			return productTypeId;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	private String addProduct(Catalog catalog,String productTypeId) throws Exception
	{
		try{
			String productid = Utils.getNewId();
			if(productid == null) throw new Exception("Error in forming new id for product.");

			int	productinsertresult = jdbcTemplate.execute(insertProduct, new PreparedStatementCallback<Integer>() {


				@Override
				public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

					ps.setString(1, productid);
					ps.setString(2, productTypeId);
					ps.setString(3, catalog.getProduct().get(0).getProductname());
					ps.setString(4, catalog.getProduct().get(0).getProductdescription());

					return  ps.executeUpdate();
				}	
			});
			return productid;
		}
		catch(Exception e ){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*private String getLastId(String sql) throws Exception
	{
		ResultSet  result = null;
		final String lastId = null;
		try
		{
			SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql);
			System.out.println("rowset=" + rowset.getString(1));
			result = jdbcTemplate.execute(sql, new PreparedStatementCallback<ResultSet>() {

				@Override
				public ResultSet doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {	

					ResultSet  temprs = null;
					temprs = ps.executeQuery();	

					if(temprs.next())
					{
						lastId = temprs.getString(1);
						return temprs;
					}
					else return null;
				}
			});		
			throw new Exception();
		}

		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
		finally
		{
			if(result!=null)
			{
				try{
					result.close();
				}
				catch(Exception e){

				}
			}
		}
	}*/

	private String isProductTypeExists(Catalog catalog) throws SQLException, Exception
	{

		String sqlSelect = "SELECT * FROM  producttype WHERE producttypename = ? ";

		try
		{
			Producttype prodType = new ObjectFactory().createProducttype();
			prodType = jdbcTemplate.queryForObject(sqlSelect, new Object[]{catalog.getProduct().get(0).getProducttype().getProducttypename()}, new RowMapper<Producttype>(){

				@Override
				public Producttype mapRow(ResultSet rs, int rowNum) throws SQLException {
					Producttype prodType =new ObjectFactory().createProducttype();
					prodType.setProducttypeid(rs.getString("PRODUCTTYPEID"));
					return prodType;
				}});

			if(prodType.getProducttypeid()!=null && prodType.getProducttypeid().trim().length() > 0) return prodType.getProducttypeid().trim();
			return null;
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}


	}

	private void addAttributesSimple(List<Simpletypeattribute> attrblist) throws Exception
	{

		try{


			int[]	productinsertresult = jdbcTemplate.batchUpdate(insertAttrbute, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {


					Simpletypeattribute simpleattrb = attrblist.get(i);
					ps.setString(1, simpleattrb.getAttributeid());
					ps.setString(2, simpleattrb.getAttributename());
					ps.setString(3, simpleattrb.getAttributedescription());
				}

				@Override
				public int getBatchSize() {
					return attrblist.size();
				}
			});
		}
		catch(Exception e ){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	

	private void addAttributesComplex(List<Complextypeattribute> attrblist) throws Exception
	{

		try{


			int[]	productinsertresult = jdbcTemplate.batchUpdate(insertAttrbute, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {


					Complextypeattribute complexattrb = attrblist.get(i);
					ps.setString(1, complexattrb.getAttributeid());
					ps.setString(2, complexattrb.getAttributename());
					ps.setString(3, complexattrb.getAttributedescription());
				}

				@Override
				public int getBatchSize() {
					return attrblist.size();
				}
			});
		}
		catch(Exception e ){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	private void addAttributeValues(List<AttributeValue> attrbvallist) throws Exception
	{

		try{


			int[]	productinsertresult = jdbcTemplate.batchUpdate(insertAttrbuteValue, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {


					AttributeValue attrbval = attrbvallist.get(i);
					ps.setString(1, attrbval.getAttributevalueid());
					ps.setString(2, attrbval.getAttributevalue());
					ps.setString(3, attrbval.getIsSubEntity());
				}

				@Override
				public int getBatchSize() {
					return attrbvallist.size();
				}
			});
		}
		catch(Exception e ){
			e.printStackTrace();
			throw new Exception(e);
		}
	}


	private void addProductAttribute(List<ProductAttributeVO> prodattrblist) throws Exception
	{

		try{


			int[]	productinsertresult = jdbcTemplate.batchUpdate(insertProductAttrbute, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {


					ProductAttributeVO prodattr = prodattrblist.get(i);
					ps.setString(1, prodattr.getProductId());
					ps.setString(2, prodattr.getAttriuteId());
					ps.setString(3, prodattr.getAttributeValueId());
				}

				@Override
				public int getBatchSize() {
					return prodattrblist.size();
				}
			});
		}
		catch(Exception e ){
			e.printStackTrace();
			throw new Exception(e);
		}
	}



	private Simpletypeattribute checkExistingAttributes(Catalog catalog,String attributename) throws Exception
	{
		try
		{
			Simpletypeattribute attribute = new ObjectFactory().createSimpletypeattribute();
			attribute = jdbcTemplate.queryForObject(checkExistingAttributes, new Object[]{attributename}, new RowMapper<Simpletypeattribute>(){

				@Override
				public Simpletypeattribute mapRow(ResultSet rs, int rowNum) throws SQLException {
					Simpletypeattribute attribute =new ObjectFactory().createSimpletypeattribute();
					attribute.setAttributeid(rs.getString("ATTRIBUTEID"));
					attribute.setAttributename(rs.getString("ATTRIBUTENAME"));
					attribute.setAttributedescription(rs.getString("ATTRIBUTEDESCRIPTION"));
					return attribute;
				}});

			return attribute;
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}


	private AttributeValue checkExistingAttributeValues(Catalog catalog,String attributeval,String isSubEntity) throws Exception
	{
		try
		{
			AttributeValue attrbval = new AttributeValue();
			attrbval = jdbcTemplate.queryForObject(checkExistingAttributeValues, new Object[]{attributeval,isSubEntity}, new RowMapper<AttributeValue>(){

				@Override
				public AttributeValue mapRow(ResultSet rs, int rowNum) throws SQLException {
					AttributeValue attrbval = new AttributeValue();
					attrbval.setAttributevalueid(rs.getString("ATTRIBUTEVALID"));
					attrbval.setAttributevalue(rs.getString("ATTRIBUTEVAL"));
					attrbval.setIsSubEntity(rs.getString("ISSUBENTITY"));
					return attrbval;
				}});

			return attrbval;
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}
	
	private AttributeValue checkExistingAttributeValues(Catalog catalog,Product attributeval,String isSubEntity) throws Exception
	{
		try
		{
			AttributeValue attrbval = new AttributeValue();
			attrbval = jdbcTemplate.queryForObject(checkExistingAttributeValues, new Object[]{attributeval.getProductid(),isSubEntity}, new RowMapper<AttributeValue>(){

				@Override
				public AttributeValue mapRow(ResultSet rs, int rowNum) throws SQLException {
					AttributeValue attrbval = new AttributeValue();
					attrbval.setAttributevalueid(rs.getString("ATTRIBUTEVALID"));
					attrbval.setAttributevalue(rs.getString("ATTRIBUTEVAL"));
					attrbval.setIsSubEntity(rs.getString("ISSUBENTITY"));
					return attrbval;
				}});

			return attrbval;
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}
	
	
	private Product checkExistingProductValues(String productId) throws Exception
	{
		try
		{
			return jdbcTemplate.queryForObject(checkExistingProductValues, new Object[]{productId}, new RowMapper<Product>(){

				@Override
				public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
					Product product = new ObjectFactory().createProduct();
					product.setProductid(rs.getString("PRODUCTID"));
					product.setProductname(rs.getString("PRODUCTNAME"));
					product.setProductdescription(rs.getString("PRODUCTDESCRIPTION"));
					
					return product;
				}});			
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}
	
	
	private ProductAttributeVO checkExistingProductAttributeValues(String attributeid,String attributevalueid) throws Exception
	{
		String sqlSelect = "SELECT * FROM  productspecificattribute WHERE ATTRIBUTEID = ? and ATTRIBUTEVALID= ? ";

		try
		{
			ProductAttributeVO proattrb = new ProductAttributeVO();
			proattrb = jdbcTemplate.queryForObject(sqlSelect, new Object[]{attributeid,attributevalueid}, new RowMapper<ProductAttributeVO>(){

				@Override
				public ProductAttributeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					ProductAttributeVO proattrb = new ProductAttributeVO();
					proattrb.setAttributeValueId(rs.getString("ATTRIBUTEVALID"));
					proattrb.setAttriuteId(rs.getString("ATTRIBUTEID"));
					proattrb.setProductId(rs.getString("PRODUCTID"));
					return proattrb;
				}});

			return proattrb;
		}
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}
}
