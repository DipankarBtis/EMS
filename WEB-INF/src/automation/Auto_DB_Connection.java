package automation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class Auto_DB_Connection
{
	String dbline="";
	String username="";
	String password="";
	
	public Connection db_conn()
	{
		Connection conn=null;
		try
		{
			getDatabaseInfo();
			Class.forName("oracle.jdbc.driver.OracleDriver");
            conn=DriverManager.getConnection(dbline,username,password);
		}
		catch (Exception e) 
		{
			StackTraceElement[] StackTrace = e.getStackTrace();
			for(StackTraceElement st : StackTrace)  
	        {  
	        	System.out.println(st);
	        }
		}
		return conn;
	}
	
	public void getDatabaseInfo() 
    {
		FileInputStream f1 = null;
		DataInputStream in = null;
		InputStreamReader isr = null;
    	try
		{
			String strline = "";
			
			File fsetup=new File("util/Setup.txt");
			String mail_list_path=fsetup.getAbsolutePath();
			f1 = new FileInputStream(mail_list_path);
			in = new DataInputStream(f1);
			isr = new InputStreamReader(in);
			try(BufferedReader br = new BufferedReader(isr))
			{
				while((strline = br.readLine())!=null)
				{				
					if(strline.startsWith("dbline"))
					{
						String  tmp[]=strline.split("dbline:");
						dbline = tmp[1].toString();
					}
					else if(strline.startsWith("username"))
					{
						String  tmp[]=strline.split("username:");
						username = tmp[1].toString();
					}
					else if(strline.startsWith("password"))
					{
						String  tmp[]=strline.split("password:");
						password = tmp[1].toString();
					}
				}
			}
		}
		catch (Exception e) 
		{
			StackTraceElement[] StackTrace = e.getStackTrace();
			for(StackTraceElement st : StackTrace)  
	        {  
	        	System.out.println(st);
	        }
		}
    	finally
    	{
    		try
			{
    			if(f1!=null) {f1.close();}
    			if(in!=null) {in.close();}
    			if(isr!=null) {isr.close();}
    		}
    		catch (Exception e) 
    		{
    			StackTraceElement[] StackTrace = e.getStackTrace();
				for(StackTraceElement st : StackTrace)  
		        {  
		        	System.out.println(st);
		        }
    		}
    	}
	}
}
