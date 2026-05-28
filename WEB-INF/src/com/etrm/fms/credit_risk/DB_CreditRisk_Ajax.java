package com.etrm.fms.credit_risk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Vector;

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

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.google.gson.Gson;

@WebServlet("/servlet/DB_CreditRisk_Ajax")
public class DB_CreditRisk_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_CreditRisk_Ajax.java";
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
	static TaxCalculator TaxCalc = new TaxCalculator();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	
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
				Context envContext  = (Context)initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
				Properties prop = System.getProperties();
				
				if (ds != null) 
				{
					dbcon = ds.getConnection();       
					if(dbcon != null)  
					{			
						setCallType = request.getParameter("setCallType");
						if(setCallType.equalsIgnoreCase("CONTRACT_PLANT_BU_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchContPlantBuDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("TAX_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchTaxDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("SUB_TAX_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchSubTaxDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("FETCH_RECEIPT_VOUCHER_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchReceiptVoucherDtl(request, response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("FETCH_SELECTED_RECEIPT_VOUCHER_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchSelectedReceiptVoucherDtl(request, response);
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
    
    private void fetchContPlantBuDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchContPlantBuDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String cont_mapping=request.getParameter("cont_mapping")==null?"":request.getParameter("cont_mapping");
   			String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
   			
   			String counterparty_cd ="";
   			String agmt ="";
			String agmt_rev = "";
			String cont = "";
			String cont_rev = "";
			String cont_type= "";
			
			if(!cont_mapping.equals(""))
			{
				String[] split = cont_mapping.split("-");
				if(split.length==6)
				{
					counterparty_cd=split[0];
					cont_type=split[1];
					agmt=split[2];
					agmt_rev=split[3];
					cont=split[4];
					cont_rev=split[5];
				}
			}
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			
			JSONArray buDtl = new JSONArray();
			
			if(clearance.equals("I"))
			{
				if(cont_type.equals("I"))
				{
					queryString="SELECT B.GX_BU_SEQ_NO,B.GX_COUNTERPARTY_CD "
							+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_GX_BU B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
				}
				else
				{
					queryString="SELECT B.GX_BU_SEQ_NO,B.GX_COUNTERPARTY_CD "
							+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_GX_BU B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
				}
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt);
				stmt.setString(4, cont);
				stmt.setString(5, cont_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					jsonobj = new JSONObject();
					
					String plant_seq=rset.getString(1)==null?"":rset.getString(1);
					String gx_counterparty=rset.getString(2)==null?"":rset.getString(2);
					String plant_nm="";
					String plant_abbr="";
					//System.out.println(counterparty_cd+" - "+plant_seq+" - "+plant_nm+" - "+plant_abbr);
					
					plant_nm=utilBean.getCounterpartyBuPlantABBR(dbcon, gx_counterparty, comp_cd, plant_seq, "G");
					plant_abbr=utilBean.getCounterpartyPlantABBR(dbcon, gx_counterparty, comp_cd, plant_seq, "G");
					
					jsonobj.put("SEQ_NO",plant_seq);
					jsonobj.put("PLANT_NM",plant_nm);
					jsonobj.put("PLANT_ABBR",plant_abbr);
					
					plantDtl.add(jsonobj);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String entity="";
				if(cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("P") || cont_type.equals("G"))
				{
					if(cont_type.equals("O") || cont_type.equals("Q") )
					{
						entity="C";
					}
					else
					{
						entity="T";
					}
					queryString="SELECT B.PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_MST A, FMS_LTCORA_CONT_PLANT B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE";
				}
				else if(cont_type.equals("D") || cont_type.equals("T"))
				{
					entity="T";
					queryString="SELECT B.PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_PLANT B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
				}
				else
				{
					entity="C";
					queryString="SELECT B.PLANT_SEQ_NO "
							+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_PLANT B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
				}
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt);
				stmt.setString(4, cont);
				stmt.setString(5, cont_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					jsonobj = new JSONObject();
					
					String plant_seq=rset.getString(1)==null?"":rset.getString(1);
					String plant_nm="";
					String plant_abbr="";
					//System.out.println(counterparty_cd+" - "+plant_seq+" - "+plant_nm+" - "+plant_abbr);
					{
						plant_nm=utilBean.getCounterpartyPlantName(dbcon, counterparty_cd, comp_cd, plant_seq, entity);
						plant_abbr=utilBean.getCounterpartyPlantABBR(dbcon, counterparty_cd, comp_cd, plant_seq, entity);
					}
					
					jsonobj.put("SEQ_NO",plant_seq);
					jsonobj.put("PLANT_NM",plant_nm);
					jsonobj.put("PLANT_ABBR",plant_abbr);
					
					plantDtl.add(jsonobj);
				}
				rset.close();
				stmt.close();
			}
			
			allDetail.put("PLANT_DTL", plantDtl);
			
			if(cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("P") || cont_type.equals("G"))
			{
				queryString="SELECT B.PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_MST A, FMS_LTCORA_CONT_BU B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE";
			}
			else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("I"))
			{
				queryString="SELECT B.PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_BU B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
			}
			else
			{
				queryString="SELECT B.PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_BU B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
			}
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt);
			stmt.setString(4, cont);
			stmt.setString(5, cont_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				String plant_seq=rset.getString(1)==null?"":rset.getString(1);
				String plant_nm=utilBean.getCounterpartyPlantName(dbcon, comp_cd, comp_cd, plant_seq, "B");
				String plant_abbr=utilBean.getCounterpartyPlantABBR(dbcon, comp_cd, comp_cd, plant_seq, "B");
				//System.out.println(counterparty_cd+" - "+plant_seq+" - "+plant_nm+" - "+plant_abbr);
				
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
    
    private void fetchTaxDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchTaxDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
   			String plant_seq=request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
   			String bu_unit=request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
   			String invoice_amt=request.getParameter("value")==null?"":request.getParameter("value");
   			String received_dt=request.getParameter("received_dt")==null?"":request.getParameter("received_dt");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray taxDtl = new JSONArray();
			JSONArray subTaxDtl = new JSONArray();
			
			Vector temp = new Vector();
			temp=TaxCalc.ServiceGrossAndTaxAmountCalculationWithInfo(dbcon,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, received_dt, "RV", invoice_amt);
			
			String tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
			String tax_struct_cd = ""+temp.elementAt(1);
			String tax_struct_dt = ""+temp.elementAt(2);
			String tax_info = ""+temp.elementAt(3);
			String tax_struct_dtl = ""+temp.elementAt(4);
			String tax_factor = ""+temp.elementAt(5);
			String gross_amt = ""+temp.elementAt(7);
			
			jsonobj = new JSONObject();
			jsonobj.put("TAX_AMT",tax_amt);
			jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
			jsonobj.put("TAX_STRUCT_DTL",tax_struct_dtl);
			jsonobj.put("GROSS_AMT",gross_amt);
			//jsonobj.put("GROSS_AMT",gross_amt);
			taxDtl.add(jsonobj);
			allDetail.put("TOTAL_TAX_DTL", taxDtl);
			
			Vector VMULTI_TAX_STRUCT=new Vector();
			VMULTI_TAX_STRUCT.add(temp.elementAt(6));
			
			if(VMULTI_TAX_STRUCT.size()>0)
			{
				for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
				{
					Vector temp1 =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
					
					for(int j=0;j<((Vector) temp1.elementAt(0)).size(); j++)
					{
						jsonobj = new JSONObject();
						taxDtl = new JSONArray();
						jsonobj.put("SUB_TAX_AMT",((Vector) temp1.elementAt(2)).elementAt(j));
						jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
						jsonobj.put("SUB_TAX_STRUCT_DTL",((Vector) temp1.elementAt(1)).elementAt(j));
						jsonobj.put("SUB_TAX_CODE",((Vector) temp1.elementAt(0)).elementAt(j));
						jsonobj.put("SUB_TAX_BASE_AMT",((Vector) temp1.elementAt(3)).elementAt(j));
						taxDtl.add(jsonobj);
						subTaxDtl.add(taxDtl);
					}
				}
			}
			allDetail.put("SUB_TAX_DTL", subTaxDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchSubTaxDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchSubTaxDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
   			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
   			String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
   			String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray taxDtl = new JSONArray();
			JSONArray subTaxDtl = new JSONArray();
			
			queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,TAX_STRUCT_CD "
					+ "FROM FMS_SECURITY_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, seq_no);
			stmt1.setString(4, seq_rev_no);
			stmt1.setString(5, clearance);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String taxCode=rset1.getString(1)==null?"":rset1.getString(1);
				String tax_descr=rset1.getString(2)==null?"":rset1.getString(2);
				String tax_amt = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
				String tax_base_amt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
				String tax_struct_cd=rset1.getString(5)==null?"":rset1.getString(5);
				
				jsonobj = new JSONObject();
				taxDtl = new JSONArray();
				jsonobj.put("SUB_TAX_AMT",tax_amt);
				jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
				jsonobj.put("SUB_TAX_STRUCT_DTL",tax_descr);
				jsonobj.put("SUB_TAX_CODE",taxCode);
				jsonobj.put("SUB_TAX_BASE_AMT",tax_base_amt);
				taxDtl.add(jsonobj);
				subTaxDtl.add(taxDtl);
			}
			rset1.close();
			stmt1.close();
			
			allDetail.put("SUB_TAX_DTL", subTaxDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchReceiptVoucherDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchReceiptVoucherDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String cont_mapping=request.getParameter("cont_mapping")==null?"":request.getParameter("cont_mapping");
   			String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
   			
   			String counterparty_cd ="";
   			String agmt ="";
			String agmt_rev = "";
			String cont = "";
			String cont_rev = "";
			String cont_type= "";
			
			if(!cont_mapping.equals(""))
			{
				String[] split = cont_mapping.split("-");
				if(split.length==6)
				{
					counterparty_cd=split[0];
					cont_type=split[1];
					agmt=split[2];
					agmt_rev=split[3];
					cont=split[4];
					cont_rev=split[5];
				}
			}
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray voucherDtl = new JSONArray();
			
			queryString="SELECT C.SEC_INT_REF "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? AND C.FILE_TYPE=? "
					+ "AND B.CONTRACT_TYPE=? AND B.AGMT_NO=? AND B.CONT_NO=? AND C.SEC_INT_REF IS NOT NULL "
					+ "AND A.CR_DR=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
					+ "AND (SELECT NVL(COUNT(*),0) FROM FMS_SECURITY_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.RECPT_SEC_REF=C.SEC_INT_REF) = 0 ";
			int st=0;
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++st, comp_cd);
			stmt.setString(++st, counterparty_cd);
			stmt.setString(++st, clearance);
			stmt.setString(++st, "PDF");
			stmt.setString(++st, cont_type);
			stmt.setString(++st, agmt);
			stmt.setString(++st, cont);
			stmt.setString(++st, "CR");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				String receipt_no=rset.getString(1)==null?"":rset.getString(1);
				
				jsonobj.put("RECEIPT_VOUCHER",receipt_no);
				
				voucherDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			allDetail.put("RECEIPT_VOUCHER_DTL", voucherDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchSelectedReceiptVoucherDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchSelectedReceiptVoucherDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String clearance=request.getParameter("clearance")==null?"":request.getParameter("clearance");
   			String receipt_voucher=request.getParameter("receipt_voucher")==null?"":request.getParameter("receipt_voucher");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray plantDtl = new JSONArray();
			JSONArray taxDtl = new JSONArray();
			JSONArray subTaxDtl = new JSONArray();
			
			queryString="SELECT A.PLANT_SEQ,A.BU_UNIT,A.GROSS_AMT, (SELECT NVL(SUM(AMOUNT),0) FROM FMS_INV_ADV_DTL E, FMS_INVOICE_MST F "
					+ "WHERE A.COMPANY_CD=E.COMPANY_CD AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=? "
					+ "AND F.COMPANY_CD=E.COMPANY_CD AND F.BU_STATE_TIN=E.BU_STATE_TIN AND F.INVOICE_SEQ=E.INVOICE_SEQ "
					+ "AND F.FINANCIAL_YEAR=E.FINANCIAL_YEAR) AS ADJUSTED_GROSS "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
					+ "AND A.CR_DR=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
			int st=0;
			stmt=dbcon.prepareStatement(queryString);
			stmt.setString(++st, "GROSS");
			stmt.setString(++st, comp_cd);
			stmt.setString(++st, clearance);
			stmt.setString(++st, "PDF");
			stmt.setString(++st, receipt_voucher);
			stmt.setString(++st, "CR");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				jsonobj = new JSONObject();
				
				String plant=rset.getString(1)==null?"":rset.getString(1);
				String bu=rset.getString(2)==null?"":rset.getString(2);
				String gross=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				String adjusted_gross=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				
				double rem=rset.getDouble(3)-rset.getDouble(4);
				
				jsonobj.put("PLANT_SEQ",plant);
				jsonobj.put("BU_SEQ",bu);
				jsonobj.put("GROSS",gross);
				jsonobj.put("AGJUSTED_GROSS",adjusted_gross);
				jsonobj.put("BALANCE",nf.format(rem));
				plantDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT D.TAX_CODE,D.TAX_DESCR,D.TAX_AMT,D.TAX_BASE_AMT,D.TAX_STRUCT_CD, (SELECT NVL(SUM(AMOUNT),0) FROM FMS_INV_ADV_DTL E, FMS_INVOICE_MST F "
					+ "WHERE A.COMPANY_CD=E.COMPANY_CD AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=(SELECT REGEXP_SUBSTR(D.TAX_DESCR, '[^ ]+', 1, 1) AS INV_COMPO FROM DUAL) "
					+ "AND F.COMPANY_CD=E.COMPANY_CD AND F.BU_STATE_TIN=E.BU_STATE_TIN AND F.INVOICE_SEQ=E.INVOICE_SEQ "
					+ "AND F.FINANCIAL_YEAR=E.FINANCIAL_YEAR) AS ADJUSTED_TAX "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
					+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
					+ "AND A.CR_DR=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
					+ "";
			st=0;
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(++st, comp_cd);
			stmt1.setString(++st, clearance);
			stmt1.setString(++st, "PDF");
			stmt1.setString(++st, receipt_voucher);
			stmt1.setString(++st, "CR");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String taxCode=rset1.getString(1)==null?"":rset1.getString(1);
				String tax_descr=rset1.getString(2)==null?"":rset1.getString(2);
				String tax_amt = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
				String tax_base_amt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
				String tax_struct_cd=rset1.getString(5)==null?"":rset1.getString(5);
				String adjusted_tax=rset1.getString(6)==null?"":nf.format(rset1.getDouble(6));
				double rem=rset1.getDouble(3)-rset1.getDouble(6);
				
				String taxStructDtl=utilBean.getTaxDescr(dbcon, tax_struct_cd);
				
				jsonobj = new JSONObject();
				taxDtl = new JSONArray();
				jsonobj.put("SUB_TAX_AMT",tax_amt);
				jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
				jsonobj.put("TAX_STRUCT_DTL",taxStructDtl);
				jsonobj.put("SUB_TAX_STRUCT_DTL",tax_descr);
				jsonobj.put("SUB_TAX_CODE",taxCode);
				jsonobj.put("SUB_TAX_BASE_AMT",tax_base_amt);
				jsonobj.put("AGJUSTED_GROSS",adjusted_tax);
				jsonobj.put("BALANCE",nf.format(rem));
				taxDtl.add(jsonobj);
				subTaxDtl.add(taxDtl);
			}
			rset1.close();
			stmt1.close();
			
			allDetail.put("PLANT_DTL", plantDtl);
			allDetail.put("TAX_DTL", subTaxDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
