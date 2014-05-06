package com.Resource;

import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException; 



public class EnvironmentSource {
	 private XmlResourceParser obj_MyXml;
     private HashMap <String, String> obj_EnviromentSetting;
     
     public EnvironmentSource(XmlResourceParser obj_MyXml){    
    	 this.obj_MyXml=obj_MyXml;
    	 this.obj_EnviromentSetting =new HashMap<String,String>();
    	 this.ParserAndStore();
     }
     //parser
     private void ParserAndStore(){
    	 try {
             int int_EventType;
             ArrayList<ObjectStack> obj_Stack =new ArrayList<ObjectStack>();
             do{ 
            	obj_MyXml.next();
             	int_EventType = this.obj_MyXml.getEventType();
           
              switch(int_EventType){
				case XmlPullParser.START_DOCUMENT:
				//"START_DOCUMENT\n";				
				break;
				case XmlPullParser.END_DOCUMENT:
				//"END_DOCUMENT\n";
				break;
				case XmlPullParser.START_TAG:		        	  
				//"START_TAG: " + myxml.getName() +String.valueOf(myxml.getAttributeCount())+ "\n";
				// check attribute
					String str_Key="";
					String str_Value="";
					for(int i=0;i<this.obj_MyXml.getAttributeCount();i++){
						if(((this.obj_MyXml.getAttributeName(i)).toUpperCase()).equals("KEY")){
							str_Key=this.obj_MyXml.getAttributeValue(i);						
						}
						else if(((this.obj_MyXml.getAttributeName(i)).toUpperCase()).equals("VALUE")){
							str_Value=this.obj_MyXml.getAttributeValue(i);						
						}					
					}
					//Put it into Stack
					ObjectStack obj_NewObjectStack=new ObjectStack();
					if(!obj_Stack.isEmpty()){
						ObjectStack obj_LastObjectStack=obj_Stack.get((obj_Stack.size()-1));					
						obj_LastObjectStack.Copy(obj_NewObjectStack);
						obj_NewObjectStack.str_Tag=this.obj_MyXml.getName();
						if(obj_NewObjectStack.str_SumTag.equals("")){
							obj_NewObjectStack.str_SumTag=this.obj_MyXml.getName();
						}
						else{
							obj_NewObjectStack.str_SumTag=obj_NewObjectStack.str_SumTag+"/"+this.obj_MyXml.getName();
						}
						if(obj_NewObjectStack.str_Key.equals("")){
							obj_NewObjectStack.str_Key=str_Key;
						}
						else{
							obj_NewObjectStack.str_Key=obj_NewObjectStack.str_Key+"/"+str_Key;
						}						
						obj_NewObjectStack.str_Value=str_Value;
					}
					else{					
						obj_NewObjectStack.str_Tag=this.obj_MyXml.getName();
						obj_NewObjectStack.str_SumTag=this.obj_MyXml.getName();
						obj_NewObjectStack.str_Key=str_Key;
						obj_NewObjectStack.str_Value=str_Value;
					}
					//加入Object into List				
					obj_Stack.add(obj_NewObjectStack);
					//check HashMap
					if(this.obj_EnviromentSetting.containsKey(obj_NewObjectStack.str_Key)==false && !str_Key.equals("")){
						//加入元素進HashMap中 
						this.obj_EnviromentSetting.put(obj_NewObjectStack.str_Key,obj_NewObjectStack.str_Value);
					}
				break;
				case XmlPullParser.END_TAG:
				//"END_TAG: " + myxml.getName() + "\n";
				//Pop the last element
					int int_Size=obj_Stack.size();
					ObjectStack obj_StackContainer=obj_Stack.get((int_Size-1));
					if(obj_StackContainer.str_Tag.equals(this.obj_MyXml.getName())){
						obj_Stack.remove((int_Size-1));
					}					
				break;
				case XmlPullParser.TEXT:
				//"TEXT: " + myxml.getText() + "\n";
				break;
 	          }
             }while (int_EventType != XmlPullParser.END_DOCUMENT);         
         } 
         catch (XmlPullParserException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }catch (Exception obj_Ex){         	         
         }    	 
     }
     
     public String SearchValue(String str_Key){
    	 String str_Result="";
    	 if(this.obj_EnviromentSetting.containsKey(str_Key)){
    		 str_Result= this.obj_EnviromentSetting.get(str_Key);    		 
    	 }
    	 return str_Result;    	 
     }
     
     public class ObjectStack{
    	 public String str_Tag;
    	 public String str_SumTag;
    	 public String str_Key;
    	 public String str_Value;
    	 public ObjectStack(){
    		 this.str_Tag="";
        	 this.str_SumTag="";
        	 this.str_Key="";
        	 this.str_Value="";     	    		 
    	 }
    	 public ObjectStack(String str_Tag,String str_SumTag,String str_Key,String str_Value){
    		 this.str_Tag=str_Tag;
        	 this.str_SumTag=str_SumTag;
        	 this.str_Key=str_Key;
        	 this.str_Value=str_Value;     		 
    	 }
    	 public void Copy(ObjectStack obj_Stack){ 
    		 obj_Stack.str_Tag= str_Tag;
    		 obj_Stack.str_SumTag= str_SumTag;
    		 obj_Stack.str_Key= str_Key;
    		 obj_Stack.str_Value= str_Value;
    	 }    	 
     }
}
