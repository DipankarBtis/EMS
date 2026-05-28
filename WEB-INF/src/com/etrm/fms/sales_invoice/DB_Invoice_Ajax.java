package com.etrm.fms.sales_invoice;

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

@WebServlet("/servlet/DB_Invoice_Ajax")
public class DB_Invoice_Ajax extends HttpServlet
{
	static String db_src_file_name="DB_Invoice_Ajax.java";
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
						if(setCallType.equalsIgnoreCase("FETCH_RECE_VOUC_BALANCE"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchReceiptVoucherBalance(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("FETCH_EXISTING_RECE_VOUC_DTL"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							fetchExistingReceiptVoucherDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("CALC_EXISTING_TAX_DTL_FOR_CR_DR"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							calcExistingTaxDtl(request,response);
							json = new Gson().toJson(AllJsonArray);
						}
						else if(setCallType.equalsIgnoreCase("BANK_DTL_FOR_CR_DR"))
						{
							AllJsonArray = new JSONArray();
							allDetail = new JSONObject();
							getBankDtlForCrDr(request,response);
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
    
    private void fetchReceiptVoucherBalance(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchReceiptVoucherBalance()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String receipt_voucher=request.getParameter("receipt_voucher")==null?"":request.getParameter("receipt_voucher");
   			String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
   			String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
   			String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
   			String operation=request.getParameter("operation")==null?"":request.getParameter("operation");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray advDtl = new JSONArray();
			
			if(operation.equals("MODIFY"))
			{
				queryString="SELECT A.GROSS_AMT,(NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL E WHERE A.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=? "
						+ "AND NOT (E.BU_STATE_TIN=? AND E.FINANCIAL_YEAR=? AND E.INVOICE_SEQ=?)),0) + "
						+ "NVL((SELECT H.GROSS_AMT FROM FMS_SECURITY_MST H WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF),0)) "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
					+ "WHERE A.COMPANY_CD=? AND C.SEC_INT_REF=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
			}
			else
			{
				queryString="SELECT A.GROSS_AMT,(NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL E WHERE A.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=?),0) + "
							+ "NVL((SELECT H.GROSS_AMT FROM FMS_SECURITY_MST H WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF),0)) "
						+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
						+ "WHERE A.COMPANY_CD=? AND C.SEC_INT_REF=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
			}
			stmt=dbcon.prepareStatement(queryString);
			int st=0;
			stmt.setString(++st, "GROSS");
			if(operation.equals("MODIFY"))
			{
				stmt.setString(++st, bu_state_tin);
				stmt.setString(++st, financial_year);
				stmt.setString(++st, invoice_seq);
			}
			stmt.setString(++st, comp_cd);
			stmt.setString(++st, receipt_voucher);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				jsonobj = new JSONObject();
				
				String gross_amt=rset.getString(1)==null?"":nf.format(rset.getDouble(1));
				String adjust_amt=rset.getString(2)==null?"":nf.format(rset.getDouble(2));
				double balance_amt=rset.getDouble(1)-rset.getDouble(2);
				String inv_component="GROSS";
				
				jsonobj.put("VOUCHER_NO",receipt_voucher);
				jsonobj.put("INV_COMPONENT",inv_component);
				jsonobj.put("ADV_GROSS_AMT",gross_amt);
				jsonobj.put("ADJUSTED_AMT",adjust_amt);
				jsonobj.put("BALANCE_AMT",nf.format(balance_amt));
				
				advDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
			
			if(operation.equals("MODIFY"))
			{
				queryString="SELECT D.TAX_AMT, D.TAX_DESCR, (NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL E WHERE A.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=(SELECT REGEXP_SUBSTR(D.TAX_DESCR, '[^ ]+', 1, 1) AS INV_COMPO FROM DUAL)"
						+ "AND NOT (E.BU_STATE_TIN=? AND E.FINANCIAL_YEAR=? AND E.INVOICE_SEQ=?)),0) + "
						+ "NVL((SELECT G.TAX_AMT FROM FMS_SECURITY_MST H,FMS_SECURITY_TAX_DTL G WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF "
						+ "AND G.TAX_DESCR LIKE (SELECT REGEXP_SUBSTR(D.TAX_DESCR, '[^ ]+', 1, 1) || '%' AS INV_COMPO FROM DUAL) AND H.COMPANY_CD=G.COMPANY_CD AND H.COUNTERPARTY_CD=G.COUNTERPARTY_CD "
						+ "AND H.SEQ_NO=G.SEQ_NO AND H.SEQ_REV_NO=G.SEQ_REV_NO AND H.GX=G.GX "
						+ "),0)) "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
					+ "WHERE A.COMPANY_CD=? AND C.SEC_INT_REF=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
					+ "";
			}
			else
			{
				queryString="SELECT D.TAX_AMT, D.TAX_DESCR, (NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL E WHERE A.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.SEC_INT_REF=E.SEC_INT_REF AND E.INV_COMPONENT=(SELECT REGEXP_SUBSTR(D.TAX_DESCR, '[^ ]+', 1, 1) AS INV_COMPO FROM DUAL)),0) + "
							+ "NVL((SELECT G.TAX_AMT FROM FMS_SECURITY_MST H,FMS_SECURITY_TAX_DTL G WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF "
							+ "AND G.TAX_DESCR LIKE (SELECT REGEXP_SUBSTR(D.TAX_DESCR, '[^ ]+', 1, 1) || '%' AS INV_COMPO FROM DUAL) AND H.COMPANY_CD=G.COMPANY_CD AND H.COUNTERPARTY_CD=G.COUNTERPARTY_CD "
							+ "AND H.SEQ_NO=G.SEQ_NO AND H.SEQ_REV_NO=G.SEQ_REV_NO AND H.GX=G.GX "
							+ "),0)) "
						+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
						+ "WHERE A.COMPANY_CD=? AND C.SEC_INT_REF=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
						+ "";
			}
			stmt=dbcon.prepareStatement(queryString);
			st=0;
			if(operation.equals("MODIFY"))
			{
				stmt.setString(++st, bu_state_tin);
				stmt.setString(++st, financial_year);
				stmt.setString(++st, invoice_seq);
			}
			stmt.setString(++st, comp_cd);
			stmt.setString(++st, receipt_voucher);			
			rset=stmt.executeQuery();
			while(rset.next())
			{
				jsonobj = new JSONObject();
				
				String tax_amt=rset.getString(1)==null?"":nf.format(rset.getDouble(1));
				String inv_component="";
				String tax_descr=rset.getString(2)==null?"":rset.getString(2);
				if(!tax_descr.equals(""))
				{
					String split[] = tax_descr.split(" ");
					inv_component=split[0];
				}
				String adjust_amt=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				double balance_amt=rset.getDouble(1)-rset.getDouble(3);
				
				jsonobj.put("VOUCHER_NO",receipt_voucher);
				jsonobj.put("INV_COMPONENT",inv_component);
				jsonobj.put("ADV_TAX_AMT",tax_amt);
				jsonobj.put("ADJUSTED_AMT",adjust_amt);
				jsonobj.put("BALANCE_AMT",nf.format(balance_amt));
				
				advDtl.add(jsonobj);
			}
			rset.close();
			stmt.close();
						
			allDetail.put("ADV_DTL", advDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void fetchExistingReceiptVoucherDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="fetchExistingReceiptVoucherDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String receipt_voucher=request.getParameter("receipt_voucher")==null?"":request.getParameter("receipt_voucher");
   			String invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
   			String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
   			String bu_state_tin=request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			JSONObject jsonobj = new JSONObject();
			JSONArray advDtl = new JSONArray();
			
			queryString1="SELECT INV_COMPONENT,AMOUNT "
					+ "FROM FMS_INV_ADV_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND SEC_INT_REF=? "
					+ "ORDER BY SEC_INT_REF";
			stmt1=dbcon.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			stmt1.setString(5, receipt_voucher);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				jsonobj = new JSONObject();
				
				String inv_component=rset1.getString(1)==null?"":rset1.getString(1);
				String amt=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
				
				jsonobj.put("VOUCHER_NO",receipt_voucher);
				jsonobj.put("INV_COMPONENT",inv_component);
				jsonobj.put("ADJUSTED_AMT",amt);
				
				advDtl.add(jsonobj);
			}
			rset1.close();
			stmt1.close();
						
			allDetail.put("ADV_DTL", advDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    private void calcExistingTaxDtl(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="calcExistingTaxDtl()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			
   			String taxStruct_cd=request.getParameter("tax_struct_cd")==null?"":request.getParameter("tax_struct_cd");
   			String grossAmt=request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray taxDtl = new JSONArray();
			JSONArray subTaxDtl = new JSONArray();
			
			Vector temp = new Vector();
			temp=TaxCalc.TaxAmountCalculation(dbcon, taxStruct_cd, grossAmt);
			
			String tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
			String tax_struct_cd = ""+temp.elementAt(1);
			String tax_struct_dt = ""+temp.elementAt(2);
			String tax_info = ""+temp.elementAt(3);
			String tax_struct_dtl = ""+temp.elementAt(4);
			String tax_factor = ""+temp.elementAt(5);
			
			jsonobj = new JSONObject();
			jsonobj.put("TAX_AMT",tax_amt);
			jsonobj.put("TAX_STRUCT_CD",tax_struct_cd);
			jsonobj.put("TAX_STRUCT_DTL",tax_struct_dtl);
			jsonobj.put("TAX_STRUCT_DT",tax_struct_dt);
			jsonobj.put("TAX_FACTOR",tax_factor);
		
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
						jsonobj.put("SUB_TAX_STRUCT_CD",tax_struct_cd);
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
    
    private void getBankDtlForCrDr(HttpServletRequest request,HttpServletResponse response) throws SQLException
   	{
    	String function_nm="getBankDtlForCrDr()";
   		try
   		{
   			allDetail.clear();
   			AllJsonArray.clear();
   			HttpSession session = request.getSession();
   			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
   			String inv_dt=request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
   			String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
   			
   			JSONObject jsonobj = new JSONObject();
			JSONArray bankDtl = new JSONArray();
			
			String bank_formula="";
			String remark_1="";
			queryString2="SELECT * "
					+ "FROM ( SELECT * "
						+ "FROM ( SELECT COUNTERPARTY_CD, ENTITY, TO_CHAR(EFF_DT,'DD/MM/YYYY') AS EFF_DT, BANK_NAME, ACCOUNT_ID, IFSC_CODE, BRANCH_NAME, STATE_NM, 1 AS PRIORITY "
							+ "FROM FMS_ENTITY_BANK_MST A "
							+ "WHERE ENTITY = ? "
							+ "AND COUNTERPARTY_CD = ? "
							+ "AND COMPANY_CD = ? "
							+ "AND CATEGORY = ? "
							+ "AND EFF_DT = ( SELECT MAX(B.EFF_DT) "
							+ "FROM FMS_ENTITY_BANK_MST B "
							+ "WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
							+ "AND A.ENTITY = B.ENTITY "
							+ "AND A.COMPANY_CD = B.COMPANY_CD "
							+ "AND A.CATEGORY = B.CATEGORY "
							+ "AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY') ) "
						+ "UNION ALL SELECT COUNTERPARTY_CD, ENTITY, TO_CHAR(EFF_DT,'DD/MM/YYYY') AS EFF_DT, BANK_NAME, ACCOUNT_ID, IFSC_CODE, BRANCH_NAME, STATE_NM, 2 AS PRIORITY "
							+ "FROM FMS_ENTITY_BANK_MST A "
							+ "WHERE ENTITY = ? "
							+ "AND COUNTERPARTY_CD = ? "
							+ "AND COMPANY_CD = ? "
							+ "AND CATEGORY = ? "
							+ "AND EFF_DT = ( SELECT MAX(B.EFF_DT) "
							+ "FROM FMS_ENTITY_BANK_MST B "
							+ "WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
							+ "AND A.ENTITY = B.ENTITY "
							+ "AND A.COMPANY_CD = B.COMPANY_CD "
							+ "AND A.CATEGORY = B.CATEGORY "
							+ "AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY') ) ) "
						+ "ORDER BY PRIORITY ) "
					+ "WHERE ROWNUM = 1 ";
			int selCnt=0;
			stmt2 = dbcon.prepareStatement(queryString2);
			stmt2.setString(++selCnt, "C"); // preferred entity
			stmt2.setString(++selCnt, counterparty_cd);
			stmt2.setString(++selCnt, comp_cd);
			stmt2.setString(++selCnt, "RLNG");
			stmt2.setString(++selCnt, inv_dt);
			stmt2.setString(++selCnt, "B");// fallback 'B'
			stmt2.setString(++selCnt, comp_cd);
			stmt2.setString(++selCnt, comp_cd);
			stmt2.setString(++selCnt, "RLNG");
			stmt2.setString(++selCnt, inv_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				String bank_eff_dt = rset2.getString(3)==null?"":rset2.getString(3);
				String bank_name=rset2.getString(4)==null?"":rset2.getString(4);
				String bank_account_no=rset2.getString(5)==null?"":rset2.getString(5);
				String ifsc_code=rset2.getString(6)==null?"":rset2.getString(6);
				String bank_branch=rset2.getString(7)==null?"":rset2.getString(7);
				String bank_state=rset2.getString(8)==null?"":rset2.getString(8);
				
				String codeType = "IFSC Code";
				
				/*if(category.equals("DERV"))
				{
					codeType = "Swift Code";
				}*/
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", "+codeType+" : "+ifsc_code;
			}
			rset2.close();
			stmt2.close();
				
			/*queryString2="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.CATEGORY=B.CATEGORY)";
			stmt2=dbcon.prepareStatement(queryString2);
			stmt2.setString(1, "B");
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, "RLNG");
			stmt2.setString(5, inv_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				String bank_eff_dt = rset2.getString(3)==null?"":rset2.getString(3);
				String bank_name=rset2.getString(4)==null?"":rset2.getString(4);
				String bank_account_no=rset2.getString(5)==null?"":rset2.getString(5);
				String ifsc_code=rset2.getString(6)==null?"":rset2.getString(6);
				String bank_branch=rset2.getString(7)==null?"":rset2.getString(7);
				String bank_state=rset2.getString(8)==null?"":rset2.getString(8);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset2.close();
			stmt2.close();*/
			
			remark_1 ="Please pay the invoiced amount by wire transfer at our Bank Account : "+bank_formula;
			
			jsonobj = new JSONObject();
			jsonobj.put("REMARK",remark_1);
		
			bankDtl.add(jsonobj);
			
			allDetail.put("BANK_DTL", bankDtl);
			AllJsonArray.add(allDetail);
   		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
}
