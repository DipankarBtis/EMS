package com.etrm.fms.inventory;

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

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Inventory_Ajax")
public class DB_Inventory_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Inventory_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	String function_nm="doPost()";
    	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
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
					
					if(setCallType.equalsIgnoreCase("IsExistNM"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkTankNameisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsExistPitag"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkTankPiTagisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
				}
			}
		}
		catch(Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	    }
		finally 
		{
			if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
			if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
			if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
			if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
			if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
			if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
			if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
			if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
			if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
			if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
			if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
			if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
			if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
		}
		
		try
		{
			response.getWriter().write(json);
		}
		catch(IOException e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
    
    private void checkTankNameisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTankNameisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String tank_cd=request.getParameter("tank_cd")==null?"0":request.getParameter("tank_cd");
   			String tank_nm=request.getParameter("tank_nm")==null?"":request.getParameter("tank_nm");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isNameExist=0;
			
			tank_nm=tank_nm.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TANK_MST "
					+ "WHERE UPPER(TRIM(TANK_NAME)) =? AND COMPANY_CD=? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TANK_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, tank_nm);
			stmt.setString(2, comp_cd);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(3, tank_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isNameExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("NAME",isNameExist);
			
			isExist.add(jsonobj);
			allDetail.put("TANK_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkTankPiTagisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTankPiTagisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String tank_cd=request.getParameter("tank_cd")==null?"0":request.getParameter("tank_cd");
   			String tank_pi_tag=request.getParameter("tank_pi_tag")==null?"":request.getParameter("tank_pi_tag");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isPiTagExist=0;
			
			tank_pi_tag=tank_pi_tag.trim().toUpperCase();
			
			
			//CHECKING ON PI TAG

			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TANK_MST "
					+ "WHERE UPPER(TRIM(TANK_PI_TAG)) =? AND COMPANY_CD=? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TANK_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, tank_pi_tag);
			stmt.setString(2, comp_cd);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(3, tank_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isPiTagExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("PI_TAG",isPiTagExist);
			
			isExist.add(jsonobj);
			allDetail.put("TANK_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
