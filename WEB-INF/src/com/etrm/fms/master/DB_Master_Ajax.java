package com.etrm.fms.master;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_Master_Ajax")
public class DB_Master_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Master_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5;
	static ResultSet rset,rset1,rset2,rset3,rset4,rset5;
	//public static String queryString, queryString1, queryString2;
	public static String setCallType="";
	public static String json = "";
	
	static JSONObject multipleData = new JSONObject();
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
	
	static UtilBean utilBean = new UtilBean();
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
    					
    					if(setCallType.equalsIgnoreCase("fetchTaxID"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchTaxID(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchBuTaxID"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchBuTaxID(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchTaxStructDtl"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchTaxStructDtl(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("TransPlantDtl"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchTransPlantDtl(request,response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistNM"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkCounterpartyNMisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistABBR"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkCounterpartyABBRisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistSAPCode"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkCounterpartySAP_CODEisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistSector"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkSectorNMisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistSectorABBR"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkSectorABBRisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistProductName"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkProductNameisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("IsExistProductABBR"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						checkProductAbbrisExist(request, response);
    						json = new Gson().toJson(AllJsonArray);
    					}
    					else if(setCallType.equalsIgnoreCase("fetchBankDtl"))
    					{
    						AllJsonArray = new JSONArray();
    						allDetail = new JSONObject();
    						fetchBankDtl(request,response);
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
    
    private void fetchTaxID(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchTaxID()";
		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
			String plant_seq_no=request.getParameter("plant_seq_no")==null?"":request.getParameter("plant_seq_no");
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String entity=request.getParameter("entity")==null?"":request.getParameter("entity");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray taxId = new JSONArray();
			
			String queryString = "SELECT NVL(STAT_CD,?),STAT_NM,STAT_TYPE,STATUS,REMARK "
					+ "FROM FMS_GOVT_STAT_TAX "
					+ "ORDER BY STAT_CD";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, "0");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String stat_cd=rset.getString(1)==null?"0":rset.getString(1);
				
				String queryString1 = "SELECT STAT_NO, TO_CHAR(EFF_DT,'DD/MM/YYYY'), REMARK "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND PLANT_SEQ_NO=? AND STAT_CD=? AND COMPANY_CD=?";
				stmt1=dbcon.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity);
				stmt1.setString(3, plant_seq_no);
				stmt1.setString(4, stat_cd);
				stmt1.setString(5, comp_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					jsonobj = new JSONObject();
					
					jsonobj.put("STAT_CD",stat_cd);
					jsonobj.put("STAT_NO",rset1.getString(1)==null?"":rset1.getString(1));
					jsonobj.put("STAT_EFF_DT",rset1.getString(2)==null?"":rset1.getString(2));
					jsonobj.put("STAT_REMARK",rset1.getString(3)==null?"":rset1.getString(3));
					
					taxId.add(jsonobj);
				}
				else
				{
					jsonobj = new JSONObject();
					
					jsonobj.put("STAT_CD",stat_cd);
					jsonobj.put("STAT_NO","");
					jsonobj.put("STAT_EFF_DT","");
					jsonobj.put("STAT_REMARK","");
					
					taxId.add(jsonobj);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			allDetail.put("TAX_ID", taxId);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchBuTaxID(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
    	String function_nm="fetchBuTaxID()";

		try
		{
			allDetail.clear();
			AllJsonArray.clear();
			
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
			String plant_seq_no=request.getParameter("plant_seq_no")==null?"":request.getParameter("plant_seq_no");
			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String entity=request.getParameter("entity")==null?"":request.getParameter("entity");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray taxId = new JSONArray();
			
			String queryString = "SELECT NVL(STAT_CD,?),STAT_NM,STAT_TYPE,STATUS,REMARK "
					+ "FROM FMS_GOVT_STAT_TAX "
					+ "ORDER BY STAT_CD";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, "0");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String stat_cd=rset.getString(1)==null?"0":rset.getString(1);
				
				String queryString1 = "SELECT STAT_NO, TO_CHAR(EFF_DT,'DD/MM/YYYY'), REMARK "
						+ "FROM FMS_COUNTERPARTY_BU_TAX "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? "
						+ "AND PLANT_SEQ_NO=? AND STAT_CD= ? AND COMPANY_CD=?";
				stmt1=dbcon.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity);
				stmt1.setString(3, plant_seq_no);
				stmt1.setString(4, stat_cd);
				stmt1.setString(5, comp_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					jsonobj = new JSONObject();
					
					jsonobj.put("STAT_CD",stat_cd);
					jsonobj.put("STAT_NO",rset1.getString(1)==null?"":rset1.getString(1));
					jsonobj.put("STAT_EFF_DT",rset1.getString(2)==null?"":rset1.getString(2));
					jsonobj.put("STAT_REMARK",rset1.getString(3)==null?"":rset1.getString(3));
					
					taxId.add(jsonobj);
				}
				else
				{
					jsonobj = new JSONObject();
					
					jsonobj.put("STAT_CD",stat_cd);
					jsonobj.put("STAT_NO","");
					jsonobj.put("STAT_EFF_DT","");
					jsonobj.put("STAT_REMARK","");
					
					taxId.add(jsonobj);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			allDetail.put("TAX_ID", taxId);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchTaxStructDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchTaxStructDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String struct_cd=request.getParameter("struct_cd")==null?"":request.getParameter("struct_cd");
			String struct_app_dt=request.getParameter("struct_app_dt")==null?"":request.getParameter("struct_app_dt");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray taxId = new JSONArray();
			
			String queryString = "SELECT NVL(TAX_STR_CD,?),TAX_CODE,FACTOR,TAX_ON,TAX_ON_CD,"
					+ "SAP_TAX_CODE,SAP_GL "
					+ "FROM FMS_TAX_STRUCTURE_DTL "
					+ "WHERE TAX_STR_CD=? AND APP_DATE=TO_DATE(?,'DD/MM/YYYY')"
					+ "ORDER BY TAX_STR_CD";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, "0");
			stmt.setString(2, struct_cd);
			stmt.setString(3, struct_app_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("TAX_STR_CD",rset.getString(1)==null?"":rset.getString(1));
				jsonobj.put("TAX_CODE",rset.getString(2)==null?"":rset.getString(2));
				jsonobj.put("FACTOR",rset.getString(3)==null?"":rset.getString(3));
				jsonobj.put("TAX_ON",rset.getString(4)==null?"":rset.getString(4));
				jsonobj.put("TAX_ON_CD",rset.getString(5)==null?"":rset.getString(5));
				jsonobj.put("SAP_TAX_CODE",rset.getString(6)==null?"":rset.getString(6));
				jsonobj.put("SAP_GL",rset.getString(7)==null?"":rset.getString(7));
				
				taxId.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("STRUCT_DTL", taxId);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchTransPlantDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchTransPlantDtl()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
   			String entity=request.getParameter("entity")==null?"0":request.getParameter("entity");
   			
			
			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			String sysDate = dateUtil.getSysdate();
			String cpStatus = (String) utilBean.getCounterpartyDetails(dbcon, counterparty_cd, sysDate).get("status");
			String max_eff_dt = (String) utilBean.getCounterpartyDetails(dbcon, counterparty_cd, sysDate).get("effDt");
			
			String queryString = "SELECT PLANT_ABBR,SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS=? ";
			queryString+= "ORDER BY COUNTERPARTY_CD,PLANT_NAME";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, entity);
			stmt.setString(2, comp_cd);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("PLANT_ABBR",rset.getString(1)==null?"":rset.getString(1));
				jsonobj.put("SEQ_NO",rset.getString(2)==null?"":rset.getString(2));
				
				plantDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("PLANT_DTL", plantDtl);
			allDetail.put("CP_STATUS", cpStatus);
			allDetail.put("MAX_EFF_DT", max_eff_dt);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkCounterpartyNMisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkCounterpartyNMisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
   			String counterparty_nm=request.getParameter("counterparty_nm")==null?"":request.getParameter("counterparty_nm");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isNameExist=0;
			
			counterparty_nm=counterparty_nm.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_COUNTERPARTY_MST "
					+ "WHERE UPPER(TRIM(COUNTERPARTY_NM)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND COUNTERPARTY_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, counterparty_nm);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isNameExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String queryString1="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_OWNER_MST "
					+ "WHERE COMPANY_CD=? AND UPPER(TRIM(COMPANY_NM)) =? ";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_nm);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isNameExist+=rset1.getInt(1);
			}
			rset1.close();
			stmt1.close();
			
			String queryString2="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_EXCHG_MST "
					+ "WHERE UPPER(TRIM(COUNTERPARTY_NM)) =? ";
			stmt2=dbcon.prepareStatement(queryString2);
			stmt2.setString(1, counterparty_nm);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				isNameExist+=rset2.getInt(1);
			}
			rset2.close();
			stmt2.close();
			
			
			jsonobj.put("NAME",isNameExist);
			
			isExist.add(jsonobj);
			allDetail.put("COUNTERPARTY_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkCounterpartyABBRisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkCounterpartyABBRisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
   			String counterparty_abbr=request.getParameter("counterparty_abbr")==null?"":request.getParameter("counterparty_abbr");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isAbbrExist=0;
			
			counterparty_abbr=counterparty_abbr.trim().toUpperCase();
			
			
			//CHECKING ON ABBR
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_COUNTERPARTY_MST "
					+ "WHERE UPPER(TRIM(COUNTERPARTY_ABBR)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND COUNTERPARTY_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, counterparty_abbr);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isAbbrExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String queryString1="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_OWNER_MST "
					+ "WHERE COMPANY_CD=? AND UPPER(TRIM(COMPANY_ABBR))=? ";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_abbr);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isAbbrExist+=rset1.getInt(1);
			}
			rset1.close();
			stmt1.close();
			
			String queryString2="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_EXCHG_MST "
					+ "WHERE UPPER(TRIM(COUNTERPARTY_ABBR)) =? ";
			stmt2=dbcon.prepareStatement(queryString2);
			stmt2.setString(1, counterparty_abbr);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				isAbbrExist+=rset2.getInt(1);
			}
			rset2.close();
			stmt2.close();
			
			jsonobj.put("ABBR",isAbbrExist);
			
			isExist.add(jsonobj);
			allDetail.put("COUNTERPARTY_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkCounterpartySAP_CODEisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkCounterpartySAP_CODEisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
   			String sap_code=request.getParameter("sap_code")==null?"":request.getParameter("sap_code");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isSapCodeExist=0;
			
			sap_code=sap_code.trim().toUpperCase();
			
			
			//CHECKING ON ABBR
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_COUNTERPARTY_MST "
					+ "WHERE UPPER(TRIM(SAP_CODE)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND COUNTERPARTY_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, sap_code);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isSapCodeExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String queryString1="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_OWNER_MST "
					+ "WHERE COMPANY_CD=? AND UPPER(TRIM(SAP_CODE)) =? ";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, sap_code);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				isSapCodeExist+=rset1.getInt(1);
			}
			rset1.close();
			stmt1.close();
			
			String queryString2="SELECT COUNT(*) "
					+ "FROM FMS_COMPANY_EXCHG_MST "
					+ "WHERE UPPER(TRIM(SAP_CODE)) =? ";
			stmt2=dbcon.prepareStatement(queryString2);
			stmt2.setString(1, sap_code);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				isSapCodeExist+=rset2.getInt(1);
			}
			rset2.close();
			stmt2.close();
			
			jsonobj.put("SAP_CODE",isSapCodeExist);
			
			isExist.add(jsonobj);
			allDetail.put("COUNTERPARTY_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    private void checkSectorNMisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkSectorNMisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			
   			String sector_cd=request.getParameter("sector_cd")==null?"0":request.getParameter("sector_cd");
   			String sector_nm=request.getParameter("sector_nm")==null?"":request.getParameter("sector_nm");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isNameExist=0;
			
			sector_nm=sector_nm.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_SECTOR_MST "
					+ "WHERE UPPER(TRIM(SECTOR_NAME)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND SECTOR_CD !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, sector_nm);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, sector_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isNameExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("SECTOR_NAME",isNameExist);
			
			isExist.add(jsonobj);
			allDetail.put("COUNTERPARTY_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    private void checkSectorABBRisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="checkSectorABBRisExist()";
    	
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String sector_cd=request.getParameter("sector_cd")==null?"0":request.getParameter("sector_cd");
    		String sector_abbr=request.getParameter("sector_abbr")==null?"":request.getParameter("sector_abbr");
    		String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray isExist = new JSONArray();
    		
    		int isNameExist=0;
    		
    		sector_abbr=sector_abbr.trim().toUpperCase();
    		
    		//CHECKING ON ABBR
    		
    		String queryString="SELECT COUNT(*) "
    				+ "FROM FMS_SECTOR_MST "
    				+ "WHERE UPPER(TRIM(SECTOR_ABBR)) =? ";
    		if(opration.equals("MODIFY"))
    		{
    			queryString+="AND SECTOR_CD !=? ";
    		}
    		stmt=dbcon.prepareStatement(queryString);
    		stmt.setString(1, sector_abbr);
    		if(opration.equals("MODIFY"))
    		{
    			stmt.setString(2, sector_cd);
    		}
    		rset=stmt.executeQuery();
    		if(rset.next())
    		{
    			isNameExist+=rset.getInt(1);
    		}
    		rset.close();
    		stmt.close();
    		
    		jsonobj.put("SECTOR_ABBR",isNameExist);
    		
    		isExist.add(jsonobj);
    		allDetail.put("COUNTERPARTY_DTL", isExist);
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }

    private void checkProductNameisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="checkProductNameisExist()";
    	
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String product_cd=request.getParameter("product_cd")==null?"0":request.getParameter("product_cd");
    		String molecule_cd=request.getParameter("molecule_cd")==null?"0":request.getParameter("molecule_cd");
    		String molecule_nm=request.getParameter("molecule_nm")==null?"":request.getParameter("molecule_nm");
    		String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray isExist = new JSONArray();
    		
    		int isNameExist=0;
    		
    		molecule_nm=molecule_nm.trim().toUpperCase();
    		
    		//CHECKING ON ABBR
    		
    		String queryString="SELECT COUNT(*) "
    				+ "FROM FMS_PRODUCT_MOLECULE_MST "
    				+ "WHERE UPPER(TRIM(MOLE_NM)) =? "
    				+ "AND PROD_CD=? ";
    		if(opration.equals("MODIFY"))
    		{
    			queryString+="AND MOLE_CD !=? ";
    		}
    		stmt=dbcon.prepareStatement(queryString);
    		stmt.setString(1, molecule_nm);
    		stmt.setString(2, product_cd);
    		if(opration.equals("MODIFY"))
    		{
    			stmt.setString(3, molecule_cd);
    		}
    		rset=stmt.executeQuery();
    		if(rset.next())
    		{
    			isNameExist+=rset.getInt(1);
    		}
    		rset.close();
    		stmt.close();
    		
    		jsonobj.put("MOLE_NAME",isNameExist);
    		
    		isExist.add(jsonobj);
    		allDetail.put("MOLE_DTL", isExist);
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }

    private void checkProductAbbrisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="checkProductAbbrisExist()";
    	
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String product_cd=request.getParameter("product_cd")==null?"0":request.getParameter("product_cd");
    		String molecule_cd=request.getParameter("molecule_cd")==null?"0":request.getParameter("molecule_cd");
    		String molecule_abbr=request.getParameter("molecule_abbr")==null?"":request.getParameter("molecule_abbr");
    		String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray isExist = new JSONArray();
    		
    		int isNameExist=0;
    		
    		molecule_abbr=molecule_abbr.trim().toUpperCase();
    		
    		//CHECKING ON ABBR
    		
    		String queryString="SELECT COUNT(*) "
    				+ "FROM FMS_PRODUCT_MOLECULE_MST "
    				+ "WHERE UPPER(TRIM(MOLE_ABBR)) =? "
    				+ "AND PROD_CD=? ";
    		if(opration.equals("MODIFY"))
    		{
    			queryString+="AND MOLE_CD !=? ";
    		}
    		stmt=dbcon.prepareStatement(queryString);
    		stmt.setString(1, molecule_abbr);
    		stmt.setString(2, product_cd);
    		if(opration.equals("MODIFY"))
    		{
    			stmt.setString(3, molecule_cd);
    		}
    		rset=stmt.executeQuery();
    		if(rset.next())
    		{
    			isNameExist+=rset.getInt(1);
    		}
    		rset.close();
    		stmt.close();
    		
    		jsonobj.put("MOLE_ABBR",isNameExist);
    		
    		isExist.add(jsonobj);
    		allDetail.put("MOLE_DTL", isExist);
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
    
    private void fetchBankDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
    	String function_nm="fetchBankDtl()";
    	
    	try
    	{
    		allDetail.clear();
    		AllJsonArray.clear();
    		
    		HttpSession session = request.getSession();
    		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
    		
    		String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
    		String entity=request.getParameter("entity")==null?"":request.getParameter("entity");
    		String bank_category=request.getParameter("bank_category")==null?"":request.getParameter("bank_category");
    		
    		JSONObject jsonobj = new JSONObject();
    		JSONArray bankDtl = new JSONArray();

    		String bank_eff_dt = "";
    		String bank_name="";
    		String bank_account_no="";
    		String ifsc_code="";
    		String bank_branch="";
    		String bank_state="";
			
    		String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM,CATEGORY  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, entity);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, bank_category);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				bank_name=rset.getString(4)==null?"":rset.getString(4);
				bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				bank_branch=rset.getString(7)==null?"":rset.getString(7);
				bank_state=rset.getString(8)==null?"":rset.getString(8);
				bank_category=rset.getString(9)==null?"":rset.getString(9);
			}
			rset.close();
			stmt.close();
    		
			allDetail.put("BANK_EFF_DT",bank_eff_dt);
			allDetail.put("BANK_NAME",bank_name);
			allDetail.put("BANK_ACCOUNT_NO",bank_account_no);
			allDetail.put("IFSC_CODE",ifsc_code);
			allDetail.put("BANK_BRANCH",bank_branch);
			allDetail.put("BANK_STATE",bank_state);
			allDetail.put("BANK_CATEGORY",bank_category);
			
    		AllJsonArray.add(allDetail);
    	}
    	catch(Exception e)
    	{
    		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    }
}
