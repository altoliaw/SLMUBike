package com.Resource;

import org.igfay.jfig.*;
import org.igfay.util.*;

public class EnvironmentSource {
	private JFigLocator obj_JFigLocator;
	public void EnvironmentSource(String str_Path){
		try{
			JFig.initialize();
			this.obj_JFigLocator=new JFigLocator(str_Path);
		}
		catch(Exception obj_Ex){								
		}
	}
		
	public int GetInteger(String[] strarr_Arg){
		int int_Value=0;
		try{
			JFig.getInstance();
				
		}
		catch(Exception obj_Ex){
				
		}
		return int_Value;
	}	
}
