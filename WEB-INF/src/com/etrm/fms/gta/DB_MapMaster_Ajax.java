package com.etrm.fms.gta;

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

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_MapMaster_Ajax")
public class DB_MapMaster_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_MapMaster_Ajax.java";
	static Connection dbcon;
	static PreparedStatement stmt;
	static PreparedStatement stmt1;
	static PreparedStatement stmt2;
	static PreparedStatement stmt3;
	static PreparedStatement stmt4;
	static PreparedStatement stmt5;
	static PreparedStatement stmt6;
	static PreparedStatement stmt7;
	static PreparedStatement stmt8; 
	static PreparedStatement stmt9; 
	static PreparedStatement stmt10;
	static PreparedStatement stmt11;
	static ResultSet rset;
	static ResultSet rset1;
	static ResultSet rset2;
	static ResultSet rset3;
	static ResultSet rset4;
	static ResultSet rset5;
	static public String queryString;
	static public String queryString1;
	static public String queryString2;
	static public String setCallType="";
	static public String json = "";
	
	static JSONObject multipleData = new JSONObject(); 
	static JSONObject allDetail = new JSONObject();
	static JSONArray AllJsonArray = new JSONArray();
    
	static DateUtil dateUtil = new DateUtil();
	static UtilBean utilBean = new UtilBean();
    
    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	synchronized(this)
    	{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String function_nm="doPost()";
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
						if(setCallType.equalsIgnoreCase("CUST_PLANT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchCustPlantDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("TRANS_PLANT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchTransporterPlantDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("COUNTERPARTY_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchCounterpartyDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("PLANT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchPlantDtl(request,response);
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
				if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try {rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt9 != null){try{stmt9.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt10 != null){try{stmt10.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt11 != null){try{stmt11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	
		    	if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	}
    
    private void fetchCustPlantDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchCustPlantDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			queryString = "SELECT SEQ_NO,PLANT_NAME,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, "C");
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("SEQ_NO",rset.getString(1)==null?"":rset.getString(1));
				jsonobj.put("PLANT_NM",rset.getString(2)==null?"":rset.getString(2));
				jsonobj.put("PLANT_ABBR",rset.getString(3)==null?"":rset.getString(3));
				
				plantDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("PLANT_DTL", plantDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchPlantDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchPlantDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
   			String entity=request.getParameter("entity")==null?"":request.getParameter("entity");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			queryString = "SELECT SEQ_NO,PLANT_NAME,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, entity);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("SEQ_NO",rset.getString(1)==null?"":rset.getString(1));
				jsonobj.put("PLANT_NM",rset.getString(2)==null?"":rset.getString(2));
				jsonobj.put("PLANT_ABBR",rset.getString(3)==null?"":rset.getString(3));
				
				plantDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("PLANT_DTL", plantDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchTransporterPlantDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchTransporterPlantDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String transporter_cd=request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			queryString = "SELECT SEQ_NO,PLANT_NAME,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, transporter_cd);
			stmt.setString(2, "R");
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("SEQ_NO",rset.getString(1)==null?"":rset.getString(1));
				jsonobj.put("PLANT_NM",rset.getString(2)==null?"":rset.getString(2));
				jsonobj.put("PLANT_ABBR",rset.getString(3)==null?"":rset.getString(3));
				
				plantDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("TRANS_PLANT_DTL", plantDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchCounterpartyDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchCounterpartyDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			utilBean.getEffectiveEntityCounterpartyList(dbcon,comp_cd, contract_type);
			for(int i=0; i<utilBean.getCOUNTERPARTY_CD().size();i++)
			{
				jsonobj = new JSONObject();
				
				jsonobj.put("COUNTERPARTY_CD",utilBean.getCOUNTERPARTY_CD().elementAt(i));
				jsonobj.put("COUNTERPARTY_NM",utilBean.getCOUNTERPARTY_NM().elementAt(i));
				jsonobj.put("COUNTERPARTY_ABBR",utilBean.getCOUNTERPARTY_ABBR().elementAt(i));
				
				plantDtl.add(jsonobj);
			}
			
			allDetail.put("COUNTERPARTY_DTL", plantDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
