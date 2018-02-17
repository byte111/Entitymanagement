create table producttype
(
  producttypeid varchar2(40) primary key,
  producttypename varchar2(50),
  producttypedescription varchar2(200)
);



create table product
(
  productid varchar2(40) primary key, 
  producttypeid varchar2(40) REFERENCES producttype(producttypeid),
  productname varchar2(50),
  productdescription varchar2(200)
);


create table attribute
(
attributeid varchar2(40) primary key,
attributename varchar2(40),
attributedescription varchar2(200)
);


create table attributevalue
(
  attributevalid varchar2(40) primary key,
  attributeval varchar2(50),
  issubentity varchar2(1)
);

create table producttypeattribute
(
  producttypeid varchar2(40) REFERENCES producttype(producttypeid),
  attributeid varchar2(40)  REFERENCES attribute(attributeid),
  attributevalid varchar2(40) REFERENCES attributevalue(attributevalid)
);

create table productspecificattribute
(
  
  productid varchar2(40) REFERENCES product(productid),
attributeid varchar2(40)  REFERENCES attribute(attributeid),
  attributevalid varchar2(40) REFERENCES attributevalue(attributevalid)
);


