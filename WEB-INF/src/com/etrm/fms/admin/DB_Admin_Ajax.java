package com.etrm.fms.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.etrm.fms.util.EncryptTest;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Admin_Ajax")
public class DB_Admin_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Admin_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	public static String queryString, queryString1, queryString2;
	
	public static String setCallType="";
	public static String json = "";
	
	public static String emp_cd="";
	public static String comp_cd="";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
    @SuppressWarnings("unused")
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   	{
    	synchronized(this)
    	{
	    	String function_nm="doPost()";
	    	
	   		response.setContentType("application/json");
	   		response.setCharacterEncoding("UTF-8");
	   		
	   		HttpSession session = request.getSession();
	   		try 
	   		{
	   			Context initContext = new InitialContext();
				if(initContext == null ) 
				{
					throw new Exception("Boom - No Context");
				}
				Context envContext  = (Context)initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
				Properties prop = System.getProperties();
				
				if (ds != null) 
				{
					dbcon = ds.getConnection();       
					if(dbcon != null)  
					{
			   			setCallType = request.getParameter("setCallType");
			   			
			   			emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
						comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
						
						if(setCallType.equalsIgnoreCase("IsExistUserID"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							checkUserIDisExist(request, response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("IsExistEmailID"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							checkEmailIDisExist(request, response);
							json = new Gson().toJson(AllJsonArray);
						}
						
						dbcon.close();
						dbcon = null;
					}				
				}
	   		}
			catch(Exception e) 
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		    }
	   		finally 
			{
				if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try {rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
	   		
	   		try {
	   		response.getWriter().write(json);
	   		} catch(IOException e) {
	   			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	   		}
    	}
   	}
    
    private void checkUserIDisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkUserIDisExist()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String user_id=request.getParameter("user_id")==null?"":request.getParameter("user_id");
   			String user_cd=request.getParameter("user_cd")==null?"":request.getParameter("user_cd");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isUserIDExist=0;
			
			user_id=user_id.trim().toUpperCase();
			
			if(opration.equals("INSERT") && user_cd.equals("0"))
			{
				//CHECKING ON USER ID
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_EMP_MST "
						+ "WHERE UPPER(TRIM(EMP_UID)) =? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, user_id);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					isUserIDExist+=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			
			jsonobj.put("USER_ID",isUserIDExist);
			
			isExist.add(jsonobj);
			allDetail.put("USER_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    private void checkEmailIDisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkEmailIDisExist()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String email_id=request.getParameter("email_id")==null?"":request.getParameter("email_id");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isEmail_idExist=0;
			
			if(opration.equals("INSERT") && !email_id.equals(""))
			{
				//CHECKING ON USER ID
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMAIL_ID =? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, email_id);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					isEmail_idExist+=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}

			jsonobj.put("EMAIL_ID",isEmail_idExist);
			
			isExist.add(jsonobj);
			allDetail.put("USER_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
