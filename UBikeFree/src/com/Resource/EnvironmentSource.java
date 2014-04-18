package com.Resource;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EnvironmentSource {
	public static class Resource{
		public ArrayList<String> str_resource=new ArrayList<String>();
		public File obj_File;
		public Resource(){
			try{
				obj_File=new File("resource.xml");
				DocumentBuilderFactory obj_dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder obj_db = obj_dbf.newDocumentBuilder();
				Document obj_doc = obj_db.parse(obj_File);
				obj_doc.getDocumentElement().normalize();
				System.out.println("Root element " + obj_doc.getDocumentElement().getNodeName());
				NodeList nodeLst = obj_doc.getElementsByTagName("unit");
				System.out.println("Information of all employees");				
			}
			catch(Exception obj_Ex){
				obj_Ex.printStackTrace();
			}			
		}		
	}	
}
