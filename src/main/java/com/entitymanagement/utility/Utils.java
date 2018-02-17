package com.entitymanagement.utility;

public class Utils {

	
	public static String getNewId()
	{
		
		return new java.util.Date().getTime()+"";
	}
	
	public static void main(String[] args) {
		System.out.println(getNewId());
	}
}
