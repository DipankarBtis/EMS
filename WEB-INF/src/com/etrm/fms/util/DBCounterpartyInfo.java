package com.etrm.fms.util;

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

import com.google.gson.Gson;

@WebServlet("/servlet/DBCounterpartyInfo")
public class DBCounterpartyInfo extends HttpServlet {

	static String db_src_file_name="DBCounterpartyInfo.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
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
    			Context envContext  = (Context)initContext.lookup("java:/comp/env");
    			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
    			Properties prop = System.getProperties();
    			
    			if (ds != null) 
    			{
    				dbcon = ds.getConnection();       
    				if(dbcon != null)  
    				{
    					setCallType = request.getParameter("setCallType");
    					
    					if(setCallType.equalsIgnoreCase("fetchCounterpartyDetails"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchCounterpartyDetails(request,response);
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
    
    private void fetchCounterpartyDetails(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchCounterpartyDetails()";
		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			
			JSONArray counterpartyInfo = new JSONArray();
			
			String query = "SELECT COUNTERPARTY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'), "
					+ "COUNTERPARTY_NM, "
					+ "COUNTERPARTY_ABBR, "
					+ "PAN_NO, "
					+ "PAN_ISSUE_DT, "
					+ "CATEGORY, "
					+ "WEB_ADDR, "
					+ "NOTES, "
					+ "STATUS, "
					+ "KYC, "
					+ "IGX, "
					+ "ENT_BY, "
					+ "ENT_DT, "
					+ "MODIFY_BY, "
					+ "MODIFY_DT, "
					+ "SAP_CODE, "
					+ "NCF_CATEGORY "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
					+ "ORDER BY COUNTERPARTY_NM";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				JSONObject jsonobj = new JSONObject();

			    // Create variables first
			    String counterpartyCd   = rset.getString(1)  == null ? "0" : rset.getString(1);
			    String effDt            = rset.getString(2)  == null ? ""  : rset.getString(2);
			    String counterpartyNm   = rset.getString(3)  == null ? ""  : rset.getString(3);
			    String counterpartyAbbr = rset.getString(4)  == null ? ""  : rset.getString(4);
			    String panNo            = rset.getString(5)  == null ? ""  : rset.getString(5);
			    String panIssueDt       = rset.getString(6)  == null ? ""  : rset.getString(6);
			    String category         = rset.getString(7)  == null ? ""  : rset.getString(7);
			    String webAddr          = rset.getString(8)  == null ? ""  : rset.getString(8);
			    String notes            = rset.getString(9)  == null ? ""  : rset.getString(9);
			    String status           = rset.getString(10) == null ? ""  : rset.getString(10);
			    String kyc              = rset.getString(11) == null ? ""  : rset.getString(11);
			    String igx              = rset.getString(12) == null ? ""  : rset.getString(12);
			    String entBy            = rset.getString(13) == null ? ""  : rset.getString(13);
			    String entDt            = rset.getString(14) == null ? ""  : rset.getString(14);
			    String modifyBy         = rset.getString(15) == null ? ""  : rset.getString(15);
			    String modifyDt         = rset.getString(16) == null ? ""  : rset.getString(16);
			    String sapCode          = rset.getString(17) == null ? ""  : rset.getString(17);
			    String ncfCategory      = rset.getString(18) == null ? ""  : rset.getString(18);

			    // Now store in JSON
			    jsonobj.put("COUNTERPARTY_CD", counterpartyCd);
			    jsonobj.put("EFF_DT", effDt);
			    jsonobj.put("COUNTERPARTY_NM", counterpartyNm);
			    jsonobj.put("COUNTERPARTY_ABBR", counterpartyAbbr);
			    jsonobj.put("PAN_NO", panNo);
			    jsonobj.put("PAN_ISSUE_DT", panIssueDt);
			    jsonobj.put("CATEGORY", category);
			    jsonobj.put("WEB_ADDR", webAddr);
			    jsonobj.put("NOTES", notes);
			    jsonobj.put("STATUS", status);
			    jsonobj.put("KYC", kyc);
			    jsonobj.put("IGX", igx);
			    jsonobj.put("ENT_BY", entBy);
			    jsonobj.put("ENT_DT", entDt);
			    jsonobj.put("MODIFY_BY", modifyBy);
			    jsonobj.put("MODIFY_DT", modifyDt);
			    jsonobj.put("SAP_CODE", sapCode);
			    jsonobj.put("NCF_CATEGORY", ncfCategory);

			    counterpartyInfo.add(jsonobj);
			}
			rset.close();
			stmt.close();
			allDetail.put("COUNTERPARTY_INFO", counterpartyInfo);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
