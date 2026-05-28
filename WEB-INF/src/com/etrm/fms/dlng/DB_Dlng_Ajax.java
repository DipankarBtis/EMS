package com.etrm.fms.dlng;

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

@WebServlet("/servlet/DB_Dlng_Ajax")
public class DB_Dlng_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Dlng_Ajax.java";
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
						checkTruckTransporterNMisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("isExistABBR"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkTruckTransporterABBRisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("isExistLicenseNo"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkDriverLicenseNoisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsExistType"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkTruckTypeisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsExistTRUCK"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkTruckisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsExistCheckpost"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkCheckPostisExist(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsLinkedTruckTransEffDt"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkLinkedTruckTransEffDt(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsLinkedDriverTransEffDt"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkLinkedDriverTransEffDt(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsLinkedDriverTruckEffDt"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkLinkedDriverTruckEffDt(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("fetchTaxID"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						fetchTaxID(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("TRUCKCAPDTL"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						fetchTruckCapDtl(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					/*else if(setCallType.equalsIgnoreCase("CUSTPLANTLIST"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						fetchCustPlantList(request, response);
						json = new Gson().toJson(AllJsonArray);
					}*/
					else if(setCallType.equalsIgnoreCase("IsSameCustPlantCheckPost"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						sameCustPlantCheckPost(request, response);
						json = new Gson().toJson(AllJsonArray);
					}
					else if(setCallType.equalsIgnoreCase("IsCFormNoExist"))
					{
						AllJsonArray = new JSONArray();
						allDetail = new JSONObject();
						checkCFormNoExist(request, response);
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
    
    private void sameCustPlantCheckPost(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="sameCustPlantCheckPost()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String customer_cd=request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
   			String customer_plant=request.getParameter("customer_plant")==null?"":request.getParameter("customer_plant");
   			String comp_cd=request.getParameter("comp_cd")==null?"":request.getParameter("comp_cd");
   			String checkPost=request.getParameter("checkPost")==null?"":request.getParameter("checkPost");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isLinkedExist=0;
			
			int cnt=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_LINK_CHECKPOST_PLANT A "
					+ "WHERE COMPANY_CD =? AND COUNTERPARTY_CD=? AND PLANT_SEQ_NO=? AND CHKPOST_CD=? "
					+ "AND (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
					+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHKPOST_CD=B.CHKPOST_CD) "
					+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
					+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
					+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND COMPANY_CD !=? AND COUNTERPARTY_CD!=? AND PLANT_SEQ_NO!=? AND CHKPOST_CD!=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, customer_cd);
			stmt.setString(++cnt, customer_plant);
			stmt.setString(++cnt, checkPost);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, customer_cd);
				stmt.setString(++cnt, customer_plant);
				stmt.setString(++cnt, checkPost);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isLinkedExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("CUSTPLANT",isLinkedExist);
			
			isExist.add(jsonobj);
			allDetail.put("CHECKPOSTPLANT", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
   /* private void fetchCustPlantList(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="fetchEventDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String customer_cd=request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
   			String comp_cd=request.getParameter("comp_cd")==null?"":request.getParameter("comp_cd");
   			
   			Vector VPLANT_SEQ_NO=new Vector();
   			Vector VPLANT_NAME=new Vector();
   			Vector VPLANT_ABBR=new Vector();
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			JSONArray plantDtlArray = new JSONArray();
			jsonobj = new JSONObject();
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,PLANT_NAME,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD) "
					+ "ORDER BY SEQ_NO";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, "C");
			stmt.setString(2, customer_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VPLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VPLANT_NAME.add(rset.getString(4)==null?"":rset.getString(4));
				VPLANT_ABBR.add(rset.getString(5)==null?"":rset.getString(5));
			}
			jsonobj.put("PLANT_SEQ", VPLANT_SEQ_NO);
			jsonobj.put("PLANT_NM", VPLANT_NAME);
			jsonobj.put("PLANT_ABBR", VPLANT_ABBR);
			
			plantDtlArray.add(jsonobj);
			
			rset.close();
			stmt.close();
			
			plantDtl.add(jsonobj);
			allDetail.put("PLANT_DTL", plantDtlArray);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}*/

	private void fetchTruckCapDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchEventDetail()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String truck_type=request.getParameter("truck_type")==null?"":request.getParameter("truck_type");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray TruckCapDtl = new JSONArray();
			JSONArray TruckCapArray = new JSONArray();
			
			queryString="SELECT TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP "
					+ "FROM FMS_TRUCK_TYPE_MST "
					+ "WHERE TRUCK_TYPE=? ";
			stmt = dbcon.prepareStatement(queryString);
			stmt.setString(1, truck_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				String truck_vol_m3=rset.getString(1)==null?"":rset.getString(1);
				String truck_vol_mt=rset.getString(2)==null?"":rset.getString(2);
				String truck_load_cap=rset.getString(3)==null?"":rset.getString(3);
				
				jsonobj.put("TRUCK_VOL_M3", truck_vol_m3);
				jsonobj.put("TRUCK_VOL_MT", truck_vol_mt);
				jsonobj.put("TRUCK_LOAD_CAP", truck_load_cap);
				
				TruckCapArray.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			TruckCapDtl.add(jsonobj);
			allDetail.put("TRUCK_CAP_DTL", TruckCapArray);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			
			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray taxId = new JSONArray();
			
			queryString = "SELECT NVL(STAT_CD,?),STAT_NM,STAT_TYPE,STATUS,REMARK "
					+ "FROM FMS_GOVT_STAT_TAX "
					+ "ORDER BY STAT_CD";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, "0");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String stat_cd=rset.getString(1)==null?"0":rset.getString(1);
				
				queryString1 = "SELECT STAT_NO, TO_CHAR(EFF_DT,'DD/MM/YYYY'), REMARK "
						+ "FROM FMS_TRUCK_TRANSPORTER_TAX "
						+ "WHERE TRUCK_TRANS_CD=? AND STAT_CD=? ";
				stmt1=dbcon.prepareStatement(queryString1);
				stmt1.setString(1, truck_trans_cd);
				stmt1.setString(2, stat_cd);
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
    
    private void checkLinkedDriverTruckEffDt(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="checkLinkedDriverTruckEffDt()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
   			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
   			String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isLinkedExist=0;
			
			//CHECKING ON EFF_DT,TRUCK_CD and DRIVER_CD
			int cnt=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_DRIVER_LINK "
					+ "WHERE TRUCK_CD =? AND DRIVER_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_CD !=? AND DRIVER_CD !=? AND EFF_DT != TO_DATE(?,'DD/MM/YYYY') ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++cnt, truck_cd);
			stmt.setString(++cnt, driver_cd);
			stmt.setString(++cnt, eff_dt);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(++cnt, truck_cd);
				stmt.setString(++cnt, driver_cd);
				stmt.setString(++cnt, eff_dt);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isLinkedExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("TRUCK_EFFDT",isLinkedExist);
			
			isExist.add(jsonobj);
			allDetail.put("LINKED_DRIVER_TRUCK_EFFDT", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void checkLinkedDriverTransEffDt(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="checkLinkedDriverTransEffDt()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
   			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
   			String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isLinkedExist=0;
			
			//CHECKING ON EFF_DT,TRUCK_TRANS_CD and Driver CD
			int cnt=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_DRIVER_TRANS_LINK "
					+ "WHERE TRUCK_TRANS_CD =? AND DRIVER_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_TRANS_CD !=? AND DRIVER_CD !=? AND EFF_DT != TO_DATE(?,'DD/MM/YYYY') ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++cnt, truck_trans_cd);
			stmt.setString(++cnt, driver_cd);
			stmt.setString(++cnt, eff_dt);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(++cnt, truck_trans_cd);
				stmt.setString(++cnt, driver_cd);
				stmt.setString(++cnt, eff_dt);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isLinkedExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("TRANS_EFFDT",isLinkedExist);
			
			isExist.add(jsonobj);
			allDetail.put("LINKED_DRIVER_TRANS_EFFDT", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void checkLinkedTruckTransEffDt(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="checkLinkedTruckTransEffDt()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
   			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
   			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isLinkedExist=0;
			
			//CHECKING ON EFF_DT,TRUCK_TRANS_CD and TRUCK_CD
			int cnt=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_TRANSPORTER_LINK "
					+ "WHERE TRUCK_TRANS_CD =? AND TRUCK_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_TRANS_CD !=? AND TRUCK_CD !=? AND EFF_DT != TO_DATE(?,'DD/MM/YYYY') ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++cnt, truck_trans_cd);
			stmt.setString(++cnt, truck_cd);
			stmt.setString(++cnt, eff_dt);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(++cnt, truck_trans_cd);
				stmt.setString(++cnt, truck_cd);
				stmt.setString(++cnt, eff_dt);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isLinkedExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("TRANS_EFFDT",isLinkedExist);
			
			isExist.add(jsonobj);
			allDetail.put("LINKED_TRUCK_TRANS_EFFDT", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void checkCheckPostisExist(HttpServletRequest request, HttpServletResponse response) 
    {
    	String function_nm="checkCheckPostisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String checkpost_name=request.getParameter("checkpost_name")==null?"":request.getParameter("checkpost_name");
   			String state_cd=request.getParameter("state_cd")==null?"":request.getParameter("state_cd");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isCheckPostExist=0;
			
			checkpost_name=checkpost_name.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_CHECKPOST_MST "
					+ "WHERE UPPER(TRIM(CHKPOST_NAME)) =? AND STATE_CODE=? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND CHKPOST_NAME !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, checkpost_name);
			stmt.setString(2, state_cd);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(3, checkpost_name);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isCheckPostExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("CHECKPOST",isCheckPostExist);
			
			isExist.add(jsonobj);
			allDetail.put("CHECKPOST_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void checkTruckisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTruckisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
   			String truck_reg_no=request.getParameter("truck_reg_no")==null?"":request.getParameter("truck_reg_no");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isTruckExist=0;
			
			truck_reg_no=truck_reg_no.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_MST "
					+ "WHERE UPPER(TRIM(TRUCK_REG_NUM)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_REG_NUM !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, truck_reg_no);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, truck_reg_no);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isTruckExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("TRUCK",isTruckExist);
			
			isExist.add(jsonobj);
			allDetail.put("TRUCK_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkTruckTypeisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTruckTypeisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String truck_type=request.getParameter("truck_type")==null?"":request.getParameter("truck_type");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isTypeExist=0;
			
			truck_type=truck_type.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_TYPE_MST "
					+ "WHERE UPPER(TRIM(TRUCK_TYPE)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_TYPE !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, truck_type);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, truck_type);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isTypeExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("TYPE",isTypeExist);
			
			isExist.add(jsonobj);
			allDetail.put("TRUCK_TYPE_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkDriverLicenseNoisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkDriverLicenseNoisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String driver_cd=request.getParameter("driver_cd")==null?"0":request.getParameter("driver_cd");
   			String license_no=request.getParameter("license_no")==null?"":request.getParameter("license_no");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isLicenseExist=0;
			
			license_no=license_no.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_DRIVER_MST "
					+ "WHERE UPPER(TRIM(LICENCE_NO)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND LICENCE_NO !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, license_no);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, license_no);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isLicenseExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("LICENSE_NO",isLicenseExist);
			
			isExist.add(jsonobj);
			allDetail.put("DRIVER_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkTruckTransporterNMisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTruckTransporterNMisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"0":request.getParameter("truck_trans_cd");
   			String truck_trans_nm=request.getParameter("truck_trans_nm")==null?"":request.getParameter("truck_trans_nm");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isNameExist=0;
			
			truck_trans_nm=truck_trans_nm.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST "
					+ "WHERE UPPER(TRIM(TRUCK_TRANS_NAME)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_TRANS_NAME !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, truck_trans_nm);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, truck_trans_nm);
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
			allDetail.put("TRUCK_TRANS_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkTruckTransporterABBRisExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkTruckTransporterABBRisExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"0":request.getParameter("truck_trans_cd");
   			String truck_trans_abbr=request.getParameter("truck_trans_abbr")==null?"":request.getParameter("truck_trans_abbr");
   			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isAbbrExist=0;
			
			truck_trans_abbr=truck_trans_abbr.trim().toUpperCase();
			
			//CHECKING ON NAME
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST "
					+ "WHERE UPPER(TRIM(TRUCK_TRANS_ABBR)) =? ";
			if(opration.equals("MODIFY"))
			{
				queryString+="AND TRUCK_TRANS_ABBR !=? ";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, truck_trans_abbr);
			if(opration.equals("MODIFY"))
			{
				stmt.setString(2, truck_trans_abbr);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isAbbrExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("ABBR",isAbbrExist);
			
			isExist.add(jsonobj);
			allDetail.put("TRUCK_TRANS_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void checkCFormNoExist(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="checkCFormNoExist()";

   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			
   			String comp_cd=request.getParameter("comp_cd")==null?"":request.getParameter("comp_cd");
   			String cform_no=request.getParameter("cform_no")==null?"":request.getParameter("cform_no");
   			
			JSONObject jsonobj = new JSONObject();
			JSONArray isExist = new JSONArray();
			
			int isNoExist=0;
			
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_CFORM_MST "
					+ "WHERE COMPANY_CD =? AND CFORM_NO=? ";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, cform_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				isNoExist+=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			jsonobj.put("CFOMNO",isNoExist);
			
			isExist.add(jsonobj);
			allDetail.put("CFOMNO_DTL", isExist);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
