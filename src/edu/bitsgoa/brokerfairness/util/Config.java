/**
 * 
 */
package edu.bitsgoa.brokerfairness.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Reads values from a config.properties file
 * @author Adarsh Srivastava
 */
public class Config 
{	
	/**
	 * returns the string value of the value against field key
	 * @param field key to look for in config file
	 * @return the value against as a String
	 */
	public static String getValue(String field)
	{
		Properties prop = new Properties();
    	InputStream input = null;
    	String out="";
    	try{

    		String filename = "config.properties";
    		input=new FileInputStream(filename);
    		prop.load(input);
    		out= prop.getProperty(field);
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
        }
    	finally{
        	if(input!=null){
        		try{
        			input.close();
        		}
        		catch (IOException e){
        			e.printStackTrace();
        		}
        	}
        }
		return out;	
	}
	/**
	 * returns the mips values as an array from the config file
	 * @return an array of mips values
	 */
	public static int[] getMips()
	{
		String mips[]=Config.getValue("mips").split(",");
		int mipi[]=new int[mips.length];
		for( int i=0; i< mips.length; i++)
			mipi[i]=Integer.parseInt(mips[i]);
		return mipi;
	}

}
