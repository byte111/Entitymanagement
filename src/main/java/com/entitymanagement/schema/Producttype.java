//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.27 at 07:47:15 PM IST 
//


package com.entitymanagement.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for producttype complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="producttype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="producttypeid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="producttypename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="producttypedescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "producttype", propOrder = {
    "producttypeid",
    "producttypename",
    "producttypedescription"
})
public class Producttype {

    protected String producttypeid;
    protected String producttypename;
    protected String producttypedescription;

    /**
     * Gets the value of the producttypeid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducttypeid() {
        return producttypeid;
    }

    /**
     * Sets the value of the producttypeid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducttypeid(String value) {
        this.producttypeid = value;
    }

    /**
     * Gets the value of the producttypename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducttypename() {
        return producttypename;
    }

    /**
     * Sets the value of the producttypename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducttypename(String value) {
        this.producttypename = value;
    }

    /**
     * Gets the value of the producttypedescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducttypedescription() {
        return producttypedescription;
    }

    /**
     * Sets the value of the producttypedescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducttypedescription(String value) {
        this.producttypedescription = value;
    }

}
