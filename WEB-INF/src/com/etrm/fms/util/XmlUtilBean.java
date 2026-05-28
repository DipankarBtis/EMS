package com.etrm.fms.util;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

public class XmlUtilBean 
{
	String db_src_file_name="XmlUtilBean.java";
	
	public DocumentBuilderFactory dcoumentBuilderFactory()
	{
		String function_nm="dcoumentBuilderFactory()";
		DocumentBuilderFactory dbFactory = null;
		try
		{
			dbFactory = DocumentBuilderFactory.newInstance();
			//dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return dbFactory;
	}

	/*public TransformerFactory transformerFactory()
	{
		TransformerFactory transferFactory = null;
		 try
		 {
			 transferFactory = TransformerFactory.newInstance();
			 transferFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			 //transferFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			 //transferFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }

		 return transferFactory;
	}*/
}
