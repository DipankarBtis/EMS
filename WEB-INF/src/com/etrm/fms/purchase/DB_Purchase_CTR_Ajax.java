package com.etrm.fms.purchase;

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

import automation.Auto_SystemErrorLogger;

@WebServlet("/servlet/DB_Purchase_CTR_Ajax")
public class DB_Purchase_CTR_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Purchase_CTR_Ajax.java";
	
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
						if(setCallType.equalsIgnoreCase("BUY_CONT_BU_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchBuyContBuDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						if(setCallType.equalsIgnoreCase("BUY_CONT_PLANT_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchBuyContPlantDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						if(setCallType.equalsIgnoreCase("CHECK_CTR"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							checkCTR(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						if(setCallType.equalsIgnoreCase("CHECK_CTR_REF"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							checkCTR_Ref(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						if(setCallType.equalsIgnoreCase("CHECK_CTR_NOM"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							checkCTR_NOM(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						if(setCallType.equalsIgnoreCase("MOLECULE"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchMolecule(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
					}
					dbcon.close();
					dbcon = null;
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
	
	private void fetchBuyContBuDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
		String function_nm="fetchBuyContBuDtl()";
   		try
   		{
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null ?"":(String)session.getAttribute("comp_cd");
   			String trader_cd = request.getParameter("trader_cd")==null||request.getParameter("trader_cd").equals("undefined")?"0":request.getParameter("trader_cd");
   			String agmt_no = request.getParameter("agmt_no")==null||request.getParameter("agmt_no").equals("undefined")?"0":request.getParameter("agmt_no");
   			String cont_no = request.getParameter("cont_no")==null||request.getParameter("cont_no").equals("undefined")?"0":request.getParameter("cont_no");
   			String cont_type = request.getParameter("cont_type")==null||request.getParameter("cont_type").equals("undefined")?"":request.getParameter("cont_type");
   			String cargo_no = request.getParameter("cargo_no")==null||request.getParameter("cargo_no").equals("undefined")?"0":request.getParameter("cargo_no");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray buDtl = new JSONArray();
			
			String query2="SELECT DISTINCT A.PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_BU A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_BU C "
					+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO  "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO) "
					+ "UNION "
					+ "SELECT DISTINCT A.PLANT_SEQ_NO  "
					+ "FROM FMS_LTCORA_CONT_BU A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
					+ "AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_LTCORA_CONT_BU C "
					+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO  "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
					+ "AND A.AGMT_TYPE=C.AGMT_TYPE AND A.BUY_SALE=C.BUY_SALE) ";
			stmt=dbcon.prepareStatement(query2);
			stmt.setString(1, comp_cd);
			stmt.setString(2, trader_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, comp_cd);
			stmt.setString(7, trader_cd);
			stmt.setString(8, agmt_no);
			stmt.setString(9, cont_no);
			stmt.setString(10, cont_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq=rset.getString(1)==null?"":rset.getString(1);
				
				jsonobj = new JSONObject();
				
				String plant_abbr=""+utilBean.getCounterpartyPlantABBR(dbcon, comp_cd, comp_cd, plant_seq, "B");
				String plant_nm=""+utilBean.getCounterpartyPlantName(dbcon, comp_cd, comp_cd, plant_seq, "B");
				
				jsonobj.put("SEQ_NO",plant_seq);
				jsonobj.put("PLANT_NM",plant_nm);
				jsonobj.put("PLANT_ABBR",plant_abbr);
				buDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("BU_DTL", buDtl);
			AllJsonArray.add(allDetail);
   		}
   		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
   	}
	
	private void fetchBuyContPlantDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
		String function_nm="fetchBuyContPlantDtl()";
   		try
   		{
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
//   			String trader_cd = request.getParameter("trader_cd")==null?"0":request.getParameter("trader_cd");
//   			String agmt_no = request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
//   			String cont_no = request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
//   			String cont_type = request.getParameter("cont_type")==null?"0":request.getParameter("cont_type");
//   			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
   			
   			String trader_cd = request.getParameter("trader_cd")==null||request.getParameter("trader_cd").equals("undefined")?"0":request.getParameter("trader_cd");
   			String agmt_no = request.getParameter("agmt_no")==null||request.getParameter("agmt_no").equals("undefined")?"0":request.getParameter("agmt_no");
   			String cont_no = request.getParameter("cont_no")==null||request.getParameter("cont_no").equals("undefined")?"0":request.getParameter("cont_no");
   			String cont_type = request.getParameter("cont_type")==null||request.getParameter("cont_type").equals("undefined")?"":request.getParameter("cont_type");
   			String cargo_no = request.getParameter("cargo_no")==null||request.getParameter("cargo_no").equals("undefined")?"0":request.getParameter("cargo_no");
   			
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			//for plant
			String query1="SELECT DISTINCT A.PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_PLANT C "
					+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO) "
					+ "UNION "
					+ "SELECT DISTINCT A.PLANT_SEQ_NO  "
					+ "FROM FMS_LTCORA_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
					+ "AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_LTCORA_CONT_PLANT C "
					+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
					+ "AND A.AGMT_TYPE=C.AGMT_TYPE AND A.BUY_SALE=C.BUY_SALE)  ";
			stmt=dbcon.prepareStatement(query1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, trader_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, comp_cd);
			stmt.setString(7, trader_cd);
			stmt.setString(8, agmt_no);
			stmt.setString(9, cont_no);
			stmt.setString(10, cont_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq=rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=""+utilBean.getCounterpartyPlantABBR(dbcon, trader_cd, comp_cd, plant_seq, "T");
				String plant_nm=""+utilBean.getCounterpartyPlantName(dbcon, trader_cd, comp_cd, plant_seq, "T");
				
				jsonobj.put("SEQ_NO",plant_seq);
				jsonobj.put("PLANT_NM",plant_nm);
				jsonobj.put("PLANT_ABBR",plant_abbr);
				
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
	
	private void checkCTR(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
		String function_nm="checkCTR()";
		try
		{
			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
//   			String trader_cd = request.getParameter("trader_cd")==null?"0":request.getParameter("trader_cd");
//   			String agmt_no = request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
//   			String cont_no = request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
//   			String cont_type = request.getParameter("cont_type")==null?"0":request.getParameter("cont_type");
//   			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
   			String trader_cd = request.getParameter("trader_cd")==null||request.getParameter("trader_cd").equals("undefined")?"0":request.getParameter("trader_cd");
   			String agmt_no = request.getParameter("agmt_no")==null||request.getParameter("agmt_no").equals("undefined")?"0":request.getParameter("agmt_no");
   			String cont_no = request.getParameter("cont_no")==null||request.getParameter("cont_no").equals("undefined")?"0":request.getParameter("cont_no");
   			String cont_type = request.getParameter("cont_type")==null||request.getParameter("cont_type").equals("undefined")?"":request.getParameter("cont_type");
   			String cargo_no = request.getParameter("cargo_no")==null||request.getParameter("cargo_no").equals("undefined")?"0":request.getParameter("cargo_no");
   			String plant_seq = request.getParameter("plant_seq")==null||request.getParameter("plant_seq").equals("undefined")?"0":request.getParameter("plant_seq");
   			String bu_seq = request.getParameter("bu_seq")==null||request.getParameter("bu_seq").equals("undefined")?"0":request.getParameter("bu_seq");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray chkCtr = new JSONArray();
   			
			int count=0;
			String query="SELECT COUNT(*) "
					+ "FROM FMS_BUY_CTR_MST  "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CARGO_NO=? AND  PLANT_SEQ_NO=? AND BU_SEQ=?  ";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, trader_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, cargo_no);
			stmt.setString(7, plant_seq);
			stmt.setString(8, bu_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
				jsonobj.put("count",count);
				chkCtr.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("chkCtr", chkCtr);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void checkCTR_Ref(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
		String function_nm="checkCTR_Ref()";
		try
		{
			//String ctr_ref = request.getParameter("ctr_ref")==null?"":request.getParameter("ctr_ref");
			String ctr_ref = request.getParameter("ctr_ref")==null||request.getParameter("ctr_ref").equals("undefined")?"":request.getParameter("ctr_ref");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray chkCtrRef = new JSONArray();
   			
			int count=0;
			String query="SELECT COUNT(*) "
					+ "FROM FMS_BUY_CTR_MST "
					+ "WHERE UPPER(CTR_REF)=UPPER(?) " ;
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, ctr_ref);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
				jsonobj.put("count",count);
				chkCtrRef.add(jsonobj);
			}
			rset.close();
			stmt.close();
			allDetail.put("chkCtrRef", chkCtrRef);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void checkCTR_NOM(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
		String function_nm="checkCTR_Ref()";
		try
		{
			//String ctr_ref = request.getParameter("ctr_ref")==null?"":request.getParameter("ctr_ref");
			String ctr_ref = request.getParameter("ctr_ref")==null||request.getParameter("ctr_ref").equals("undefined")?"":request.getParameter("ctr_ref");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray chkCtrNom = new JSONArray();
			
			int count=0;
			
			String query="SELECT COMPANY_CD,COUNTERPARTY_cD,AGMT_NO,CONT_NO,CONTRACT_TYPE,CARGO_NO, "
					+ "PLANT_SEQ_NO,BU_SEQ "
					+ "FROM FMS_BUY_CTR_MST A "
					+ "WHERE CTR_REF=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT)  "
					+ "FROM FMS_BUY_CTR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD  "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
					+ "AND A.CARGO_NO=B.CARGO_NO AND A.CTR_REF=B.CTR_REF AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO  "
					+ "AND A.BU_SEQ=B.BU_SEQ) ";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1,ctr_ref);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String comp_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt_no=rset.getString(3)==null?"":rset.getString(3);
				String cont_no=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cargo_no=rset.getString(6)==null?"":rset.getString(6);
				String plant_seq_no=rset.getString(7)==null?"":rset.getString(7);
				String bu_seq=rset.getString(8)==null?"":rset.getString(8);
				
				int nom_count=0;
				int sell_nom_count=0;
				int alloc_count=0;
				
				String query1="SELECT SUBDATA_TYPE,TO_CHAR(GAS_DAY,'DD/MM/YYYY'),NOMINATION_QTY,CONFIRM_QTY,SCHEDULE_QTY,ALLOCATION_QTY "
						+ "FROM FMS_AZS_BUS_TRADE "
						+ "WHERE CTR_NO=? AND REPORT_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')  "
						+ "ORDER BY SUBDATA_TYPE DESC ";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1,ctr_ref);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String sub_data_type=rset1.getString(1)==null?"":rset1.getString(1);
					String gas_dt=rset1.getString(2)==null?"":rset1.getString(2);
					double nom_qty=rset1.getDouble(3);
					double confirm_qty=rset1.getDouble(4);
					double sch_qty=rset1.getDouble(5);
					double alloc_qty=rset1.getDouble(6);
					
					//for LNG Cargo
					if(cont_type.equals("N"))
					{
					}
					//for LTCORA and other purchase 
					else 
					{
						String query2="SELECT COUNT(*) "
								+ "FROM FMS_BUY_DAILY_BUYER_NOM "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CARGO_NO=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PLANT_SEQ=? AND BU_SEQ=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_type);
						stmt2.setString(6, cargo_no);
						stmt2.setString(7, gas_dt);
						stmt2.setString(8, plant_seq_no);
						stmt2.setString(9, bu_seq);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							nom_count=rset2.getInt(1);
						}
						rset2.close();
						stmt2.close();
						
						String query3="SELECT COUNT(*) "
								+ "FROM FMS_BUY_DAILY_SELLER_NOM "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CARGO_NO=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PLANT_SEQ=? AND BU_SEQ=? ";
						stmt2=dbcon.prepareStatement(query3);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_type);
						stmt2.setString(6, cargo_no);
						stmt2.setString(7, gas_dt);
						stmt2.setString(8, plant_seq_no);
						stmt2.setString(9, bu_seq);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							sell_nom_count=rset2.getInt(1);
						}
						rset2.close();
						stmt2.close();
						
						String query4="SELECT COUNT(*) "
								+ "FROM FMS_BUY_DAILY_ALLOCATION "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CARGO_NO=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PLANT_SEQ=? AND BU_SEQ=? ";
						stmt2=dbcon.prepareStatement(query3);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_type);
						stmt2.setString(6, cargo_no);
						stmt2.setString(7, gas_dt);
						stmt2.setString(8, plant_seq_no);
						stmt2.setString(9, bu_seq);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							alloc_count=rset2.getInt(1);
						}
						rset2.close();
						stmt2.close();
					}
				}
				rset1.close();
				stmt1.close();
				
				count=nom_count+sell_nom_count+alloc_count;
				jsonobj.put("count",count);
				chkCtrNom.add(jsonobj);
			}
			rset.close();
			stmt.close();

			allDetail.put("chkCtrNom", chkCtrNom);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void fetchMolecule(HttpServletRequest request,HttpServletResponse response) throws SQLException
	{
		String function_nm="fetchMolecule()";
		try
		{
			HttpSession session = request.getSession();
			String comp_cd = (String)session.getAttribute("comp_cd")==null ?"":(String)session.getAttribute("comp_cd");
			String prod_cd = request.getParameter("prod_cd")==null||request.getParameter("prod_cd").equals("undefined")?"0":request.getParameter("prod_cd");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray molDtl = new JSONArray();
			
			String query2="SELECT MOLE_CD,MOLE_ABBR "
					+ "FROM FMS_PRODUCT_MOLECULE_MST "
					+ "WHERE PROD_CD=?  "
					+ "AND MOLE_FLAG=? ";
			stmt=dbcon.prepareStatement(query2);
			stmt.setString(1, prod_cd);
			stmt.setString(2, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String mole_cd=rset.getString(1)==null?"":rset.getString(1);
				String mole_abbr=rset.getString(2)==null?"":rset.getString(2);
				
				jsonobj = new JSONObject();
				
				jsonobj.put("SEQ_NO",mole_cd);
				//jsonobj.put("PLANT_NM",plant_nm);
				jsonobj.put("MOLE_ABBR",mole_abbr);
				molDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("MOLE_DTL", molDtl);
			AllJsonArray.add(allDetail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	
}