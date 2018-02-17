//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.27 at 07:47:15 PM IST 
//


package com.entitymanagement.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for product complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="product">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productdescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="simpletypeattribute" type="{http://www.gxs.com/catalog/}simpletypeattribute" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="complextypeattribute" type="{http://www.gxs.com/catalog/}complextypeattribute" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="producttype" type="{http://www.gxs.com/catalog/}producttype" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "product", propOrder = {
    "productid",
    "productname",
    "productdescription",
    "simpletypeattribute",
    "complextypeattribute",
    "producttype"
})
public class Product {

    protected String productid;
    protected String productname;
    protected String productdescription;
    protected List<Simpletypeattribute> simpletypeattribute;
    protected List<Complextypeattribute> complextypeattribute;
    protected Producttype producttype;

    /**
     * Gets the value of the productid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductid() {
        return productid;
    }

    /**
     * Sets the value of the productid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductid(String value) {
        this.productid = value;
    }

    /**
     * Gets the value of the productname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductname() {
        return productname;
    }

    /**
     * Sets the value of the productname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductname(String value) {
        this.productname = value;
    }

    /**
     * Gets the value of the productdescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductdescription() {
        return productdescription;
    }

    /**
     * Sets the value of the productdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductdescription(String value) {
        this.productdescription = value;
    }

    /**
     * Gets the value of the simpletypeattribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the simpletypeattribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimpletypeattribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Simpletypeattribute }
     * 
     * 
     */
    public List<Simpletypeattribute> getSimpletypeattribute() {
        if (simpletypeattribute == null) {
            simpletypeattribute = new ArrayList<Simpletypeattribute>();
        }
        return this.simpletypeattribute;
    }

    /**
     * Gets the value of the complextypeattribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complextypeattribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplextypeattribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Complextypeattribute }
     * 
     * 
     */
    public List<Complextypeattribute> getComplextypeattribute() {
        if (complextypeattribute == null) {
            complextypeattribute = new ArrayList<Complextypeattribute>();
        }
        return this.complextypeattribute;
    }

    /**
     * Gets the value of the producttype property.
     * 
     * @return
     *     possible object is
     *     {@link Producttype }
     *     
     */
    public Producttype getProducttype() {
        return producttype;
    }

    /**
     * Sets the value of the producttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Producttype }
     *     
     */
    public void setProducttype(Producttype value) {
        this.producttype = value;
    }

}