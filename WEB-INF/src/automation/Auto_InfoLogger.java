package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import com.etrm.fms.util.escapeSingleQuotes;


public class Auto_InfoLogger 
{
	String db_src_file_name="Auto_InfoLogger.java";
	Connection conn; 
	PreparedStatement stmt;
	ResultSet rset;
	String queryString="";
	String queryString1="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	public void InsertInfoLogger(String user_cd,String comp_cd,String username,String ip,String form_id,String form_nm,String module_cd,String module_nm,String old_value,String new_value,String msg)
	{
		String function_nm="InsertInfoLogger()";
		try
		{
			Timestamp ts = new Timestamp(System.currentTimeMillis());
	        String tsString = ts.toString().substring(0, 19);
	        String tsDate = tsString.substring(0, 10);
	        String time = tsString.substring(11, 19);
	        
	        conn=new Auto_DB_Connection().db_conn();     
	        
	        if(conn != null)
	        {
		        	//stmt = conn.createStatement();
		           
		        synchronized(this)
	        	{
	        		msg=escObj.replaceSingleQuotes(msg);
	        		
			        queryString = "INSERT INTO FMS_ALL_LOG(LOG_DT, LOG_TIME, LOG_UID, LOG_MACH_ID, REMARKS, EMP_CD, COMPANY_CD,FORM_CD, FORM_NAME,"
			        		+ "MODULE_CD,MODULE_NAME,OLD_VALUE, NEW_VALUE) "
			        		+ "VALUES(to_date(?,'yyyy-mm-dd'),?,?,?,?,?,?,?,?,"
			        		+ "?,?,?,?)";
			        stmt=conn.prepareStatement(queryString);
			        stmt.setString(1, tsDate);
			        stmt.setString(2, time);
			        stmt.setString(3, username);
			        stmt.setString(4, ip);
			        stmt.setString(5, msg);
			        stmt.setString(6, user_cd);
			        stmt.setString(7, comp_cd);
			        stmt.setString(8, form_id);
			        stmt.setString(9, form_nm);
			        stmt.setString(10, module_cd);
			        stmt.setString(11, module_nm);
			        stmt.setString(12, old_value);
			        stmt.setString(13, new_value);
			        stmt.executeUpdate();
			        
			        //conn.commit(); //SYSTEMERR#24050033 
			    }
		        conn.close();
    			conn = null;
		    }
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
		{
			try
			{
				if(conn != null){conn.close();}
				if(stmt != null){stmt.close();}
				if(rset != null){rset.close();}
			}
			catch(Exception e1)
			{
				new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e1);
			}
		}
	}
}
