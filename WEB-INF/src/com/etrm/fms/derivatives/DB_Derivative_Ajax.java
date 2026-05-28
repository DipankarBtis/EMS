package com.etrm.fms.derivatives;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Derivative_Ajax")
public class DB_Derivative_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Derivative_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	//public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
	
	static DateUtil dateUtil = new DateUtil();
    
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	synchronized (this) 
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
    					
    					if(setCallType.equalsIgnoreCase("fetchCurveAndProjMethod"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchCurveAndProjMethod(request,response);
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
    		
    		try {
    		response.getWriter().write(json);
    		} catch(IOException e) {
    			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    		}
		}
	}
    
    private void fetchCurveAndProjMethod(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchCurveAndProjMethod()";
		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
			String product_nm=request.getParameter("product_nm")==null?"":request.getParameter("product_nm");
			String cont_month=request.getParameter("cont_month")==null?"":request.getParameter("cont_month");
			String cont_year=request.getParameter("cont_year")==null?"":request.getParameter("cont_year");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray curveandprojm_dtl = new JSONArray();
			
			String queryString = "SELECT CURVE_TYPE"
					+ "	FROM FMS_PROD_CURVE_MAP "
					+ "WHERE PROD_TYPE=?";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, product_nm);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String curve_type=rset.getString(1)==null?"":rset.getString(1);
				
				String ProjMethod="";
				String price_start_dt="";
				String price_end_dt="";
				
				jsonobj.put("CURVE_TYPE",curve_type);
				
				if(curve_type.equals("ICE_JKM") || curve_type.equals("ICE_WIM") || curve_type.equals("JKM_PLATTS_SETTLEMENT") || curve_type.equals("WIM_PLATTS_SETTLEMENT"))
				{
					ProjMethod="JKM/WIM Trade Month";
				}
				else if(curve_type.equals("ICE_BRENT") || curve_type.equals("ICE_DATED_BRENT") || curve_type.equals("DATEDBRENT_PLATTS_SETTLEMENT")) 
				{
					ProjMethod="Calendar Month";
				}
				else
				{
					ProjMethod="Calendar Month";
				}
				
				if(ProjMethod.equals("Calendar Month"))
				{
					if(!cont_month.equals("00"))
					{
						price_start_dt="01/"+cont_month+"/"+cont_year;
						price_end_dt = dateUtil.getLastDateOfMonth(cont_month, cont_year);
					}
				}
				else
				{
					if(!cont_month.equals("00"))
					{
						String queryString1 = "SELECT TO_CHAR(SETTLE_START_DT,'DD/MM/YYYY'), "
								+ "TO_CHAR(SETTLE_END_DT,'DD/MM/YYYY') "
								+ "FROM FMS_CURVE_SETTLE_CALND "
								+ "WHERE CURVE_NM=? ";
						if(!cont_year.equals("0")) 
						{
							queryString1 += "AND CONT_MONTH >= TO_DATE(?,'DD/MM/YYYY') AND CONT_MONTH <= TO_DATE(?,'DD/MM/YYYY')";
						}
						queryString1 += "ORDER BY CONT_MONTH DESC";
						stmt1 = dbcon.prepareStatement(queryString1);
						stmt1.setString(1, curve_type);
						if(!cont_year.equals("0")) 
						{
							stmt1.setString(2, "01/"+cont_month+"/"+cont_year);
							stmt1.setString(3, dateUtil.getLastDateOfMonth(cont_month, cont_year));
						}
						rset1=stmt1.executeQuery();
						if(rset1.next()) 
						{
							price_start_dt = (rset1.getString(1)==null?"":rset1.getString(1));
							price_end_dt = (rset1.getString(2)==null?"":rset1.getString(2));
						}
						rset1.close();
						stmt1.close();
					}
				}

				int count1=0;
				
				String curve_unit ="";
				
				String queryString2 = "SELECT CURVE_UNIT "
						+ "FROM FMS_SPOT_PRICE_DTL "
						+ "WHERE CURVE_NM=?";
				queryString2+="AND ACTUAL_CURVE=? "; 
				stmt2 =dbcon.prepareStatement(queryString2);
				stmt2.setString(++count1, product_nm);
				stmt2.setString(++count1, curve_type);
				rset2=stmt2.executeQuery();
				if(rset2.next()) 
				{
					curve_unit= (rset2.getString(1)==null?"":rset2.getString(1));
				}
				rset2.close();
				stmt2.close();
				
				jsonobj.put("PRICE_START_DT",price_start_dt);
				jsonobj.put("PRICE_END_DT",price_end_dt);
				jsonobj.put("CURVE_UNIT",curve_unit.toUpperCase());
				
				jsonobj.put("PROJMETHOD",ProjMethod);
				
				curveandprojm_dtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			allDetail.put("CURVEANDPROJM_DTL", curveandprojm_dtl);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
