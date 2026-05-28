package com.etrm.fms.credit_risk;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;


//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 18/10/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_CreditRisk")
public class Frm_CreditRisk extends HttpServlet 
{
	static String frm_src_file_name="Frm_CreditRisk.java";
	public static Connection dbcon;
	
	public static String servletName = "Frm_CreditRisk";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	
	
	public static String form_id = "0";
	public static String form_nm = "";
	public static String mod_cd = "0";
	public static String mod_nm = "";
	public static String u = "";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		String function_nm="doPost()";
		HttpSession session = request.getSession();
		if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid")=="")
		{
			url="../sess/Expire.jsp";
		}
		else
		{
			try
			{
				Context Context = new InitialContext();
				if(Context == null) 
				{
					throw new Exception("Boom - No Context");
				}
				
				Context envContext  = (Context)Context.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
				
				if(ds != null)
				{
					dbcon = ds.getConnection();				
				}
				else
				{
					System.out.println("Data Source Not Found");
				}
				if(dbcon != null)
				{
					dbcon.setAutoCommit(false);
					
					form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
					form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
					mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
					mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("PRE-RECEIPT_SECURITY"))
					{
						InsertUpdatePreReceiptSecurityDetail(request);
					}
					else if(option.equalsIgnoreCase("SECURITY_MST"))
					{
						InsertUpdateSecurityDetails(request);
					}
					else if(option.equalsIgnoreCase("CREDIT_EXCEED_CONFIG"))
					{
						InsertUpdateCreditExceedConfigDetail(request);
					}
					else if(option.equalsIgnoreCase("CREDIT_RATING_DTLS"))//AP20230929
					{
						InsertUpdateCreditRating(request);
					}
					else if(option.equalsIgnoreCase("CREDIT_LIMIT_DTLS"))//AP20230930
					{
						InsertUpdateCreditLimit(request);
					}
					else if(option.equalsIgnoreCase("PRE_DEAL_CHECK_REQ"))//AP20230930
					{
						InsertPreDealCheckReq(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url;
			}
			finally
			{
				if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}
		try 
		{
			response.sendRedirect(url);
		}
		catch(IOException e) 
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertPreDealCheckReq(HttpServletRequest request)throws SQLException 
	{
		String function_nm="InsertPreDealCheckReq()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String counterparty_cd = request.getParameter("counterparty")==null?"":request.getParameter("counterparty");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String sell_buy = request.getParameter("sellBuy")==null?"":request.getParameter("sellBuy");
			String start_dt = request.getParameter("startDt")==null?"":request.getParameter("startDt");
			String end_dt = request.getParameter("endDt")==null?"":request.getParameter("endDt");
			String value = request.getParameter("value")==null?"":request.getParameter("value");
			String currency = request.getParameter("currency")==null?"":request.getParameter("currency");
			String delv_term = request.getParameter("dlv_terms")==null?"":request.getParameter("dlv_terms");
			String spot_term = request.getParameter("spotTerm")==null?"":request.getParameter("spotTerm");
			String cont_comp = request.getParameter("cont_comp")==null?"":request.getParameter("cont_comp");
			String payment_term = request.getParameter("paymentTerm")==null?"":request.getParameter("paymentTerm");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String operation = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String app_countptyCd = request.getParameter("app_coutpty_cd")==null?"":request.getParameter("app_coutpty_cd");
			String app_req_id = request.getParameter("app_request_id")==null?"":request.getParameter("app_request_id");
			String app_seq_no = request.getParameter("app_seq_no")==null?"":request.getParameter("app_seq_no");
			String app_status = request.getParameter("app_status")==null?"":request.getParameter("app_status");
			String app_remark = request.getParameter("app_msg")==null?"":request.getParameter("app_msg");
			String req_id = "";
			String seq_no ="";
			String status = "";
			int count = 0;
			if(operation.equals("MODIFY"))
			{
				queryString="SELECT NVL(COUNT(*),0) FROM FMS_PRE_DEAL_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? AND REQUEST_ID=?";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, app_countptyCd);
				stmt.setString(3, app_seq_no);
				stmt.setString(4, app_req_id);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
					if(count>0)
					{
						query = "UPDATE FMS_PRE_DEAL_DTL SET STATUS=?,APRV_MSG=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,APRV_BY=?,APRV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? AND REQUEST_ID=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, app_status);
						stmt1.setString(2, app_remark);
						stmt1.setString(3, emp_cd);
						stmt1.setString(4, emp_cd);
						stmt1.setString(5, comp_cd);
						stmt1.setString(6, app_countptyCd);
						stmt1.setString(7, app_seq_no);
						stmt1.setString(8, app_req_id);
						stmt1.executeUpdate();
						stmt1.close();
						msg = "Successful! - "+counterparty_nm+" Pre Deal Credit Check Request Modified Successfully!";
						msg_type="S";
					}
				}
				rset.close();
				stmt.close();
			}
			else if(operation.equals("INSERT"))
			{
				queryString = "SELECT MAX(NVL(SEQ_NO,1)+1) FROM FMS_PRE_DEAL_DTL WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=rset.getString(1)==null?"1":rset.getString(1);
				}
				rset.close();
				stmt.close();
				
				String queryString1 = "SELECT MAX(NVL(REQUEST_ID,1)+1) FROM FMS_PRE_DEAL_DTL WHERE COMPANY_CD=?";
				stmt1=dbcon.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					req_id=rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				query = "INSERT INTO FMS_PRE_DEAL_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,VALUE,CURRENCY,START_DT,END_DT,BUY_SELL,DLV_TERMS,SPOT_TERMS,PAYMENT_TERMS,REQUEST_ID,REQUEST_BY,REQUEST_DT,REQUEST_MSG,STATUS,ENT_DT,ENT_BY,SHELL_CONT) "
						+ "values(?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE,?,?,SYSDATE,?,?)";
				stmt2=dbcon.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, seq_no);
				stmt2.setString(4, value);
				stmt2.setString(5, currency);
				stmt2.setString(6, start_dt);
				stmt2.setString(7, end_dt);
				stmt2.setString(8, sell_buy);
				stmt2.setString(9, delv_term);
				stmt2.setString(10, spot_term);
				stmt2.setString(11, payment_term);
				stmt2.setString(12, req_id);
				stmt2.setString(13, emp_cd);
				stmt2.setString(14, remark);
				stmt2.setString(15, "P");
				stmt2.setString(16, emp_cd);
				stmt2.setString(17, cont_comp);
				stmt2.executeUpdate();
				stmt2.close();
				msg = "Successful! - "+counterparty_nm+" Pre Deal Credit Check Request Inserted Successfully!";
				msg_type="S";
			}
			url = "../credit_risk/frm_pre_deal_creditCheck.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Pre Deal Credit Check Detail submission Failed!";
		}
	}
	
	private void InsertUpdateCreditLimit(HttpServletRequest request)throws SQLException 
	{
		String function_nm="InsertUpdateCreditLimit()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String entity = request.getParameter("entity1")==null?"":request.getParameter("entity1");
			String entity_cd = request.getParameter("entity_cd1")==null?"":request.getParameter("entity_cd1");
			String operation = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String counterparty_cd = "";
			String bank_cd = "";
			if(entity.equals("C"))
			{
				counterparty_cd = entity_cd;
				bank_cd = "0";
			}
			else if(entity.equals("B"))
			{
				counterparty_cd = "0";
				bank_cd = entity_cd;
			}
			String bank_abbr = ""+utilBean.getBankABBR(dbcon,bank_cd);
			String counterparty_abbr="";
			if(clearance.equals("I"))
			{
				counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,counterparty_cd); 
			}
			else
			{
				counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			}
			
			String limit_id = request.getParameter("limit_id_dtl")==null?"":request.getParameter("limit_id_dtl");
			String limit_type = request.getParameter("limit_type")==null?"":request.getParameter("limit_type");
			String limit_action = request.getParameter("limit_action")==null?"":request.getParameter("limit_action");
			String limit_category = request.getParameter("limit_category")==null?"":request.getParameter("limit_category");
			String limit_amount = request.getParameter("limit_amount")==null?"":request.getParameter("limit_amount");
			String limit_eff_date = request.getParameter("limit_eff_date")==null?"":request.getParameter("limit_eff_date");
			String limit_exp_date = request.getParameter("limit_exp_date")==null?"":request.getParameter("limit_exp_date");
			String limit_review_date = request.getParameter("limit_next_review_date")==null?"":request.getParameter("limit_next_review_date");
			String limit_remark = request.getParameter("limit_remark")==null?"":request.getParameter("limit_remark");
			String limit_seq_no = request.getParameter("cl_seq_no")==null?"":request.getParameter("cl_seq_no");
			String limit_ref_no = request.getParameter("cl_ref_no")==null?"":request.getParameter("cl_ref_no");
			
			old_value = request.getParameter("CR_Limit_old_value")==null?"":request.getParameter("CR_Limit_old_value");
						
			if(operation.equals("INACTIVE"))
			{
				query="UPDATE FMS_LIMIT_DTL SET IS_ACTIVE=?,INACTIVATION_DT=SYSDATE,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? AND LIMIT_ID=? AND SEQ_NO=? AND REF_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, "N");
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				//stmt.setString(3, comp_cd);
				stmt.setString(4, counterparty_cd);
				stmt.setString(5, bank_cd);
				stmt.setString(6, limit_id);
				stmt.setString(7, limit_seq_no);
				stmt.setString(8, limit_ref_no);
				stmt.executeUpdate();
				stmt.close();
				
				msg = "Successful! - "+limit_ref_no+" Credit Limit Modified Successfully!";
				msg_type="S";
				new_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+clearance+"#LIMIT_REF_NO="+limit_ref_no+"#LIMIT_TYPE="+limit_type+"#LIMIT_ACTION="+limit_action+"#LIMIT_CATE="+limit_category+"#LIMIT_AMOUNT="+limit_amount+"#EFF_DT="+limit_eff_date+
						"#EXP_DT="+limit_exp_date+"#REVIEW_DT="+limit_review_date+"#LIMIT_STATUS="+"N"+
						"#LIMIT_REMARKS="+limit_remark+"#ENTITY="+entity+"#RD="+"L";			
			}
			if(operation.equals("MODIFY"))
			{
				if(limit_action.equals("Adjust Usage") && Double.parseDouble(limit_amount)>0)
				{
					limit_amount=""+(Double.parseDouble(limit_amount)*(-1));
				}
					
				query="UPDATE FMS_LIMIT_DTL SET LIMIT_TYPE=?,ACTION_TYPE=?,CATEGORY=?,AMT=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),EXP_DT=TO_DATE(?,'DD/MM/YYYY'), "
						+ "REVIEW_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?, MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND LIMIT_ID=? AND SEQ_NO=? AND REF_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, limit_type);
				stmt.setString(2, limit_action);
				stmt.setString(3, limit_category);
				stmt.setString(4, limit_amount);
				stmt.setString(5, limit_eff_date);
				stmt.setString(6, limit_exp_date);
				stmt.setString(7, limit_review_date);
				stmt.setString(8, limit_remark);
				stmt.setString(9, emp_cd);
				stmt.setString(10, comp_cd);
				//stmt.setString(10, comp_cd);
				stmt.setString(11, counterparty_cd);
				stmt.setString(12, bank_cd);
				stmt.setString(13, limit_id);
				stmt.setString(14, limit_seq_no);
				stmt.setString(15, limit_ref_no);
				stmt.executeUpdate();
				stmt.close();
				
				msg = "Successful! - "+limit_ref_no+" Credit Limit Modified Successfully!";
				msg_type="S";
				new_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+clearance+"#LIMIT_REF_NO="+limit_ref_no+"#LIMIT_TYPE="+limit_type+"#LIMIT_ACTION="+limit_action+"#LIMIT_CATE="+limit_category+"#LIMIT_AMOUNT="+limit_amount+"#EFF_DT="+limit_eff_date+
						"#EXP_DT="+limit_exp_date+"#REVIEW_DT="+limit_review_date+"#LIMIT_STATUS="+"Y"+
						"#LIMIT_REMARKS="+limit_remark+"#ENTITY="+entity+"#RD="+"L";
			}
			else if(operation.equals("INSERT"))
			{
				String seq_no = "";
				String ref_no = "";
				query="SELECT MAX(NVL(SEQ_NO,1)+1) FROM FMS_LIMIT_DTL WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND LIMIT_ID=? ";
				stmt = dbcon.prepareStatement(query);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, bank_cd);
				stmt.setString(3, limit_id);
				rset = stmt.executeQuery();
				if(rset.next()) 
				{
					seq_no = rset.getString(1)==null?"1":rset.getString(1);
				}
				rset.close();
				stmt.close();
				
				if(counterparty_cd.equals("0") && !bank_cd.equals("0"))
				{
					ref_no = bank_abbr+"-R"+limit_id+"-L"+seq_no;
				}
				else
				{
					ref_no = counterparty_abbr+"-R"+limit_id+"-L"+seq_no;
				}
				if(limit_action.equals("Adjust Usage") && Double.parseDouble(limit_amount)>0)
				{
					limit_amount=""+(Double.parseDouble(limit_amount)*(-1));
				}
				query1="INSERT INTO FMS_LIMIT_DTL(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,SEQ_NO,REF_NO,LIMIT_TYPE,ACTION_TYPE,CATEGORY,AMT,AMT_UNIT,EFF_DT,EXP_DT,REVIEW_DT,IS_ACTIVE,REMARKS,ENT_BY,ENT_DT,GX,ENT_PROFILE) "
						+ "VALUES(?,?,?,?,?,?,?,?,?, "
						+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, bank_cd);
				stmt1.setString(3, limit_id);
				stmt1.setString(4, seq_no);
				stmt1.setString(5, ref_no);
				stmt1.setString(6, limit_type);
				stmt1.setString(7, limit_action);
				stmt1.setString(8, limit_category);
				stmt1.setString(9, limit_amount);
				stmt1.setString(10, "1");
				stmt1.setString(11, limit_eff_date);
				stmt1.setString(12, limit_exp_date);
				stmt1.setString(13, limit_review_date);
				stmt1.setString(14, "Y");
				stmt1.setString(15, limit_remark);
				stmt1.setString(16, emp_cd);
				stmt1.setString(17, clearance);
				stmt1.setString(18, comp_cd);
				stmt1.executeQuery();
				stmt1.close();
				
				msg = "Successful! - "+ref_no+" Credit Limit Inserted Successfully!";
				msg_type="S";
				new_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+clearance+"#LIMIT_REF_NO="+ref_no+"#LIMIT_TYPE="+limit_type+"#LIMIT_ACTION="+limit_action+"#LIMIT_CATE="+limit_category+"#LIMIT_AMOUNT="+limit_amount+"#EFF_DT="+limit_eff_date+
						"#EXP_DT="+limit_exp_date+"#REVIEW_DT="+limit_review_date+"#LIMIT_STATUS="+"Y"+
						"#LIMIT_REMARKS="+limit_remark+"#ENTITY="+entity+"#RD="+"L";			
			}
			
			url = "../credit_risk/frm_credit_limit.jsp?msg="+msg+"&msg_type="+msg_type+"&entity="+entity+"&entity_cd="+entity_cd+"&clearance="+clearance+commonUrl_pra;
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
			}
						
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Credit Limit Insert/Update submission Failed!";

		}
	}

	private void InsertUpdateCreditRating(HttpServletRequest request)throws SQLException  
	{
		String function_nm="InsertUpdateCreditRating()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String entity = request.getParameter("entity")==null?"":request.getParameter("entity");
			String entity_cd = request.getParameter("entity_cd")==null?"":request.getParameter("entity_cd");
			String operation = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String old_value = request.getParameter("CR_Rating_old_value")==null?"":request.getParameter("CR_Rating_old_value");
			String rate_source = request.getParameter("rate_source")==null?"":request.getParameter("rate_source");
			String new_value = "";			
			
			String counterparty_cd = "";
			String bank_cd = "";
			if(entity.equals("C"))
			{
				counterparty_cd = entity_cd;
				bank_cd = "0";
			}
			else if(entity.equals("B"))
			{
				counterparty_cd = "0";
				bank_cd = entity_cd;
			}
			String bank_abbr = ""+utilBean.getBankABBR(dbcon,bank_cd);
			String counterparty_abbr="";
			if(clearance.equals("I"))
			{
				counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,counterparty_cd); 
			}
			else
			{
				counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			}
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String credit_rating = request.getParameter("credit_rating")==null?"":request.getParameter("credit_rating");
			String cr_status = request.getParameter("cr_status")==null?"":request.getParameter("cr_status");
			String cr_remark = request.getParameter("cr_remark")==null?"":request.getParameter("cr_remark");
			String parent_entity = request.getParameter("parent_entity")==null?"":request.getParameter("parent_entity");
			String ownership = request.getParameter("ownership")==null?"":request.getParameter("ownership");
			String pEntryDate = request.getParameter("pEntryDate")==null?"":request.getParameter("pEntryDate");
			String pExitDate = request.getParameter("pExitDate")==null?"":request.getParameter("pExitDate");
			String p_cr_status = request.getParameter("p_cr_status")==null?"":request.getParameter("p_cr_status");
			String p_cr_remark = request.getParameter("p_cr_remark")==null?"":request.getParameter("p_cr_remark");
			String cr_ref_no = request.getParameter("cr_ref_no")==null?"":request.getParameter("cr_ref_no");
			String limit_id_rating = request.getParameter("limit_id_rating")==null?"":request.getParameter("limit_id_rating");
			String limit_id = "";
			String ref_no = "";
			String sysdate=utilDate.getSysdate();
			
			if(operation.equals("MODIFY"))
			{
				if(!limit_id_rating.equals("") && !entity_cd.equals(""))
				{
					String dtl_limit_id = "";
					int chkCount = 0;
					
					query1 = "SELECT NVL(COUNT(*),0) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=? AND PARENT_OWNSHIP_CD IS NOT NULL AND PARENT_EXIT_DT<TO_DATE(?,'DD/MM/YYYY')";
					stmt1=dbcon.prepareStatement(query1);
					//stmt1.setString(1, comp_cd);
					stmt1.setString(1, counterparty_cd);
					stmt1.setString(2, bank_cd);
					stmt1.setString(3, clearance);
					stmt1.setString(4, limit_id_rating);
					stmt1.setString(5, sysdate);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						chkCount=rset1.getInt(1);
						if(chkCount>0)
						{
							int cnt=0;
							query2 = "SELECT MAX(NVL(LIMIT_ID,1)+1) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=?";
							stmt2=dbcon.prepareStatement(query2);
							//stmt2.setString(1, comp_cd);
							stmt2.setString(1, counterparty_cd);
							stmt2.setString(2, bank_cd);
							stmt2.setString(3, clearance);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								limit_id=rset2.getString(1)==null?"1":rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							if(counterparty_cd.equals("0") && !bank_cd.equals("0"))
							{
								ref_no = bank_abbr+"-R"+limit_id;
							}
							else
							{
								ref_no = counterparty_abbr+"-R"+limit_id;
							}
							String creditR="";
							if(rd.equals("P"))
							{
								
								queryString="SELECT CREDIT_RATING FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=?";
								stmt3=dbcon.prepareStatement(queryString);
								//stmt3.setString(1, comp_cd);
								stmt3.setString(1, counterparty_cd);
								stmt3.setString(2, bank_cd);
								stmt3.setString(3, clearance);
								stmt3.setString(4, limit_id_rating);
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									creditR = rset3.getString(1)==null?"":rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								query="INSERT INTO FMS_LIMIT_MST(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,PARENT_OWNSHIP_CD,"
										+ "PARENT_OWNSHIP,PARENT_ENT_DT,PARENT_EXIT_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,GX,ENT_PROFILE) "
										+ "VALUES(?,?,?,?,SYSDATE,?,"
										+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE,?,?)";
							}
							else
							{
								query="INSERT INTO FMS_LIMIT_MST(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,GX,ENT_PROFILE) "
										+ "VALUES(?,?,?,?,SYSDATE,?,?,?,?,SYSDATE,?,?)";
							}
							stmt=dbcon.prepareStatement(query);
							if(rd.equals("P"))
							{
								stmt.setString(++cnt, counterparty_cd);
								stmt.setString(++cnt, bank_cd);
								stmt.setString(++cnt, limit_id);
								stmt.setString(++cnt, creditR);
								stmt.setString(++cnt, parent_entity);
								stmt.setString(++cnt, ownership);
								stmt.setString(++cnt, pEntryDate);
								stmt.setString(++cnt, pExitDate);
								stmt.setString(++cnt, ref_no);
								stmt.setString(++cnt, p_cr_status);
								stmt.setString(++cnt, p_cr_remark);
								stmt.setString(++cnt, emp_cd);
								stmt.setString(++cnt, clearance);
								stmt.setString(++cnt, comp_cd);
							}
							else
							{
								stmt.setString(++cnt, counterparty_cd);
								stmt.setString(++cnt, bank_cd);
								stmt.setString(++cnt, limit_id);
								stmt.setString(++cnt, credit_rating);
								stmt.setString(++cnt, ref_no);
								stmt.setString(++cnt, cr_status);
								stmt.setString(++cnt, cr_remark);
								stmt.setString(++cnt, emp_cd);
								stmt.setString(++cnt, clearance);
								stmt.setString(++cnt, comp_cd);
							}
							stmt.executeQuery();
							
							stmt.close();
							
							msg = "Successful! - "+ref_no+" Credit Rating Inserted Successfully!";
							msg_type="S";
						}
						else
						{
							int recCount = 0;
							queryString = "SELECT NVL(COUNT(*),0) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=? ";
							stmt3=dbcon.prepareStatement(queryString);
							//stmt3.setString(1, comp_cd);
							stmt3.setString(1, counterparty_cd);
							stmt3.setString(2, bank_cd);
							stmt3.setString(3, clearance);
							stmt3.setString(4, limit_id_rating);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								recCount=rset3.getInt(1);
								if(recCount>0)
								{
									String query="";
									int cnt=0;
									if(rd.equals("P"))
									{
										query="UPDATE FMS_LIMIT_MST SET PARENT_OWNSHIP_CD=?,PARENT_OWNSHIP=?,PARENT_ENT_DT=TO_DATE(?,'DD/MM/YYYY'),PARENT_EXIT_DT=TO_DATE(?,'DD/MM/YYYY'),STATUS=?,REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE "//COMPANY_CD=? AND "
											+ "COUNTERPARTY_CD=? AND BANK_CD=? AND REF_NO=? AND GX=?";
									}
									else
									{
										query="UPDATE FMS_LIMIT_MST SET CREDIT_RATING=?,STATUS=?,REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,RATING_EFF_DT=SYSDATE,MOD_PROFILE=? "
											+ "WHERE "//COMPANY_CD=? AND "
											+ "COUNTERPARTY_CD=? AND BANK_CD=? AND REF_NO=? AND GX=?";
									}
									stmt=dbcon.prepareStatement(query);
									if(rd.equals("P"))
									{
										stmt.setString(++cnt, parent_entity);
										stmt.setString(++cnt, ownership);
										stmt.setString(++cnt, pEntryDate);
										stmt.setString(++cnt, pExitDate);
										stmt.setString(++cnt, p_cr_status);
										stmt.setString(++cnt, p_cr_remark);
										stmt.setString(++cnt, emp_cd);
										stmt.setString(++cnt, comp_cd);
										//stmt.setString(++cnt, comp_cd);
										stmt.setString(++cnt, counterparty_cd);
										stmt.setString(++cnt, bank_cd);
										stmt.setString(++cnt, cr_ref_no);
										stmt.setString(++cnt, clearance);
									}
									else
									{
										stmt.setString(++cnt, credit_rating);
										stmt.setString(++cnt, cr_status);
										stmt.setString(++cnt, cr_remark);
										stmt.setString(++cnt, emp_cd);
										stmt.setString(++cnt, comp_cd);
										//stmt.setString(++cnt, comp_cd);
										stmt.setString(++cnt, counterparty_cd);
										stmt.setString(++cnt, bank_cd);
										stmt.setString(++cnt, cr_ref_no);
										stmt.setString(++cnt, clearance);
									}
									stmt.executeUpdate();
									stmt.close();
									msg = "Successful! - "+cr_ref_no+" Credit Rating Modified Successfully!";
									msg_type="S";
								}
								else
								{
									msg = "No Record Found For Modification";
									msg_type="E";
								}
							}
							else
							{
								msg = "No Record Found For Modification";
								msg_type="E";
							}
							rset3.close();
							stmt3.close();
						}
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					msg = "No Record Found For Modification";
					msg_type="E";
				}
				
				new_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+clearance+"#REF_NO="+cr_ref_no+"#RATE_SOURCE="+rate_source+"#CREDIT_RATING="+credit_rating+"#CR_STATUS="+cr_status+"#CR_REMARK="+cr_remark+
						"#PARENT_ENTITY="+parent_entity+"#OWNERSHIP="+ownership+"#PENTRYDATE="+pEntryDate+
						"#PEXITDATE="+pExitDate+"#PCR_STATUS="+p_cr_status+"#PCR_REMARK="+p_cr_remark+"#ENTITY="+entity+"#RD="+rd;			
			}
			else if(operation.equals("INSERT"))
			{
				int count = 0;
				query2="SELECT NVL(COUNT(*),0) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? ";
				stmt=dbcon.prepareStatement(query2);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, bank_cd);
				stmt.setString(3, clearance);
				rset= stmt.executeQuery();
				if(rset.next())
				{
					count = rset.getInt(1);
					
					if(count == 0)
					{
						query1="SELECT MAX(NVL(LIMIT_ID,1)+1) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
								+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? ";
						stmt1=dbcon.prepareStatement(query1);
						//stmt1.setString(1, comp_cd);
						stmt1.setString(1, counterparty_cd);
						stmt1.setString(2, bank_cd);
						stmt1.setString(3, clearance);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) 
						{
							limit_id = rset1.getString(1)==null?"1":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						if(counterparty_cd.equals("0") && !bank_cd.equals("0"))
						{
							ref_no = bank_abbr+"-R"+limit_id;
						}
						else
						{
							ref_no = counterparty_abbr+"-R"+limit_id;
						}
						int cnt=0;
						if(rd.equals("P"))
						{
							credit_rating = "";
							query2="INSERT INTO FMS_LIMIT_MST(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,PARENT_OWNSHIP_CD,"
									+ "PARENT_OWNSHIP,PARENT_ENT_DT,PARENT_EXIT_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,GX,ENT_PROFILE) "
									+ "VALUES(?,?,?,?,?,?,"
									+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE,?,?)";
						}
						else
						{
							query2="INSERT INTO FMS_LIMIT_MST(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,GX,ENT_PROFILE) "
									+ "VALUES(?,?,?,?,SYSDATE,?,?,?,?,SYSDATE,?,?)";
						}
						stmt2=dbcon.prepareStatement(query2);
						if(rd.equals("P"))
						{
							stmt2.setString(++cnt, counterparty_cd);
							stmt2.setString(++cnt, bank_cd);
							stmt2.setString(++cnt, limit_id);
							stmt2.setString(++cnt, credit_rating);
							stmt2.setString(++cnt, "");
							stmt2.setString(++cnt, parent_entity);
							stmt2.setString(++cnt, ownership);
							stmt2.setString(++cnt, pEntryDate);
							stmt2.setString(++cnt, pExitDate);
							stmt2.setString(++cnt, ref_no);
							stmt2.setString(++cnt, p_cr_status);
							stmt2.setString(++cnt, p_cr_remark);
							stmt2.setString(++cnt, emp_cd);
							stmt2.setString(++cnt, clearance);
							stmt2.setString(++cnt, comp_cd);
						}
						else
						{
							stmt2.setString(++cnt, counterparty_cd);
							stmt2.setString(++cnt, bank_cd);
							stmt2.setString(++cnt, limit_id);
							stmt2.setString(++cnt, credit_rating);
							stmt2.setString(++cnt, ref_no);
							stmt2.setString(++cnt, cr_status);
							stmt2.setString(++cnt, cr_remark);
							stmt2.setString(++cnt, emp_cd);
							stmt2.setString(++cnt, clearance);
							stmt2.setString(++cnt, comp_cd);
						}
						stmt2.executeQuery();
						stmt2.close();
						
						//String bank_nm = utilBean.getBankName(bank_cd, comp_cd);
						msg = "Successful! - "+ref_no+" Credit Rating Inserted Successfully!";
						msg_type="S";
					}
					else
					{
						msg = "Credit Rating Is Already Exist for this";
						if(entity.equals("C"))
						{
							msg += " Counterparty !";
						}
						else if(entity.equals("B"))
						{
							msg += " Bank !";
						}
						msg_type="E";
					}
				}
				else
				{
					msg = "Credit Rating Is Already Exist for this";
					if(entity.equals("C"))
					{
						msg += " Counterparty !";
					}
					else if(entity.equals("B"))
					{
						msg += " Bank !";
					}
					msg_type="E";
				}
				rset.close();
				stmt.close();
				
				new_value = "CP="+counterparty_cd+"#BANK_CD="+bank_cd+"#GX="+clearance+"#REF_NO="+ref_no+"#RATE_SOURCE="+rate_source+"#CREDIT_RATING="+credit_rating+"#CR_STATUS="+cr_status+"#CR_REMARK="+cr_remark+
						"#PARENT_ENTITY="+parent_entity+"#OWNERSHIP="+ownership+"#PENTRYDATE="+pEntryDate+
						"#PEXITDATE="+pExitDate+"#PCR_STATUS="+p_cr_status+"#PCR_REMARK="+p_cr_remark+"#ENTITY="+entity+"#RD="+rd;			
			}
			url = "../credit_risk/frm_credit_limit.jsp?msg="+msg+"&msg_type="+msg_type+"&entity="+entity+"&entity_cd="+entity_cd+"&clearance="+clearance+commonUrl_pra;
			
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
			}			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Credit Rating Insert/Update submission Failed!";
		}
		
	}

	@SuppressWarnings({ "unlikely-arg-type", "null" })
	private void InsertUpdateSecurityDetails(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSecurityDetails()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String cancel_flg = request.getParameter("cancel_flg")==null?"":request.getParameter("cancel_flg");
			String pcg_cancel_flg = request.getParameter("pcg_cancel_flg")==null?"":request.getParameter("pcg_cancel_flg");
			String adv_cancel_flg = request.getParameter("adv_cancel_flg")==null?"":request.getParameter("adv_cancel_flg");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String counterpty_cd = request.getParameter("counterpty_cd")==null?"":request.getParameter("counterpty_cd");
			String ref_no = request.getParameter("ref_no")==null?"":request.getParameter("ref_no");
			String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
			String deal_type = request.getParameter("deal_type")==null?"":request.getParameter("deal_type");
			String[] deal_no = request.getParameterValues("chk_deal")==null?null:request.getParameterValues("chk_deal");
			String currency = request.getParameter("currency")==null?"":request.getParameter("currency");
			String variation = request.getParameter("value_variation")==null?"":request.getParameter("value_variation");
			String value = request.getParameter("value")==null?"":request.getParameter("value");
			String fluctuation = request.getParameter("value_fluctuation")==null?"":request.getParameter("value_fluctuation");
			String issuing_bank_cd = request.getParameter("issuing_bankName")==null?"":request.getParameter("issuing_bankName");
			String issuing_bank_ref = request.getParameter("issuing_bankRef")==null?"":request.getParameter("issuing_bankRef");
			String advising_bank_cd = request.getParameter("advising_bankName")==null?"":request.getParameter("advising_bankName");
			String advising_bank_ref = request.getParameter("advising_bankRef")==null?"":request.getParameter("advising_bankRef");
			String confirming_bank_cd = request.getParameter("confirming_bankName")==null?"":request.getParameter("confirming_bankName");
			String confirming_bank_ref = request.getParameter("confirming_bankRef")==null?"":request.getParameter("confirming_bankRef");
			String received_dt = request.getParameter("received_dt")==null?"":request.getParameter("received_dt");
			String issuance_dt = request.getParameter("issuance_dt")==null?"":request.getParameter("issuance_dt");
			String expire_dt = request.getParameter("expire_dt")==null?"":request.getParameter("expire_dt");
			String tenor = request.getParameter("tenor")==null?"":request.getParameter("tenor");
			String review_dt = request.getParameter("review_dt")==null?"":request.getParameter("review_dt");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String new_counptyCd = request.getParameter("counterparty")==null?"":request.getParameter("counterparty");
			String new_sec_type = request.getParameter("new_sec_type")==null?"":request.getParameter("new_sec_type");
			String new_reciept_dt = request.getParameter("new_reciept_dt")==null?"":request.getParameter("new_reciept_dt");
			String new_currency = request.getParameter("new_currency")==null?"":request.getParameter("new_currency");
			String new_sec_value = request.getParameter("new_value")==null?"":request.getParameter("new_value");
			String new_remark = request.getParameter("new_remark")==null?"":request.getParameter("new_remark");
			String new_category = request.getParameter("new_category")==null?"":request.getParameter("new_category");
			String sec_type = request.getParameter("sec_type")==null?"":request.getParameter("sec_type");
			String[] new_deal_no = request.getParameterValues("new_chk_deal");
			String mseq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String lc_seq_rev_no = request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");
			String lc_inorder_hist = request.getParameter("lc_inorder_hist")==null?"":request.getParameter("lc_inorder_hist");
			String lc_sec_type = request.getParameter("lc_sec_type")==null?"":request.getParameter("lc_sec_type");
			String status = "";
			if(cancel_flg.equals("Y"))
			{
				status = request.getParameter("status2")==null?"":request.getParameter("status2");
			}
			else
			{
				status = request.getParameter("status")==null?"":request.getParameter("status");
			}
			if(!expire_dt.equals(""))
			{
				String sysdate= ""+utilDate.getSysdate();
				String[] split_sys_date = sysdate.split("/");
				String splited_sysdate = split_sys_date[2]+"-"+split_sys_date[1]+"-"+split_sys_date[0];

				String[] split_exp_date = expire_dt.split("/");
				String splited_expdate = split_exp_date[2]+"-"+split_exp_date[1]+"-"+split_exp_date[0];

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				Date sys_date = sdf.parse(splited_sysdate);  
				Date exp_Date = sdf.parse(splited_expdate);  
				if(sys_date.compareTo(exp_Date) > 0)   
				{  
					status="E";
				}   
			}
			String agmt = "";
			String agmt_rev = "";
			String cont = "";
			String cont_rev = "";
			String cont_type =  "";
			String countpty_cd = "";//HP20230914
			String new_countpty_cd = "";//HP20230914
			String new_agmt = "";
			String new_agmt_rev = "";
			String new_cont = "";
			String new_cont_rev = "";
			String new_cont_type =  "";
			String new_deal_type="";
			
			String lc_split_value = "";
			String pcg_split_value = "";
			String new_split_value = "";
			String[] split_sec = request.getParameterValues("split_value");
			String[] pcg_split_sec = request.getParameterValues("pcg_split_value");
			String[] new_split_sec = request.getParameterValues("new_split_value");
			
			
			String pcg_status = "";
			String pcg_counterpty_cd = request.getParameter("pcg_counterpty_cd")==null?"":request.getParameter("pcg_counterpty_cd");
			String pcg_sec_category = request.getParameter("pcg_sec_category")==null?"":request.getParameter("pcg_sec_category");
			String pcg_deal_type = request.getParameter("pcg_deal_type")==null?"":request.getParameter("pcg_deal_type");
			String pcg_deal_no[] = request.getParameterValues("pcg_chk_deal");
			String pcg_value = request.getParameter("pcg_value")==null?"":request.getParameter("pcg_value");
			String pcg_currency = request.getParameter("pcg_currency")==null?"":request.getParameter("pcg_currency");
			String pcg_guarantor_cd = request.getParameter("pcg_guarantor_name")==null?"":request.getParameter("pcg_guarantor_name");
			String pcg_received_dt = request.getParameter("pcg_received_dt")==null?"":request.getParameter("pcg_received_dt");
			String pcg_review_dt = request.getParameter("pcg_review_dt")==null?"":request.getParameter("pcg_review_dt");
			String pcg_issuance_dt = request.getParameter("pcg_issuance_dt")==null?"":request.getParameter("pcg_issuance_dt");
			String pcg_expire_dt = request.getParameter("pcg_expire_dt")==null?"":request.getParameter("pcg_expire_dt");
			String pcg_renew_dt = request.getParameter("pcg_renew_dt")==null?"":request.getParameter("pcg_renew_dt");
			String pcg_tenor = request.getParameter("pcg_tenor")==null?"":request.getParameter("pcg_tenor");
			String pcg_remark = request.getParameter("pcg_remark")==null?"":request.getParameter("pcg_remark");
			String pcg_ref_no = request.getParameter("pcg_ref_no")==null?"":request.getParameter("pcg_ref_no");
			String pcg_seq_no = request.getParameter("pcg_seq_no")==null?"":request.getParameter("pcg_seq_no");
			String pcg_seq_rev_no = request.getParameter("pcg_seq_rev_no")==null?"":request.getParameter("pcg_seq_rev_no");
			String pcg_sec_type = request.getParameter("pcg_sec_type")==null?"":request.getParameter("pcg_sec_type");
			if(pcg_cancel_flg.equals("Y"))
			{
				pcg_status = request.getParameter("pcg_status2")==null?"":request.getParameter("pcg_status2");
			}
			else
			{
				pcg_status = request.getParameter("pcg_status")==null?"":request.getParameter("pcg_status");
			}
			if(!pcg_expire_dt.equals(""))
			{
				String sysdate= ""+utilDate.getSysdate();
				String[] split_sys_date = sysdate.split("/");
				String splited_sysdate = split_sys_date[2]+"-"+split_sys_date[1]+"-"+split_sys_date[0];

				String[] split_exp_date = pcg_expire_dt.split("/");
				String splited_expdate = split_exp_date[2]+"-"+split_exp_date[1]+"-"+split_exp_date[0];

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				Date sys_date = sdf.parse(splited_sysdate);  
				Date exp_Date = sdf.parse(splited_expdate);  
				if(sys_date.compareTo(exp_Date) > 0)   
				{  
					pcg_status="E";
				}   
			}
			String pcg_agmt = "";
			String pcg_agmt_rev = "";
			String pcg_cont = "";
			String pcg_cont_rev = "";
			String pcg_cont_type =  "";
			String pcg_countpty_cd = "";//HP20230914
			
			String adv_counterpty_cd = request.getParameter("adv_counterpty_cd")==null?"":request.getParameter("adv_counterpty_cd");
			String adv_sec_category = request.getParameter("adv_sec_category")==null?"":request.getParameter("adv_sec_category");
			String adv_deal_type = request.getParameter("adv_deal_type")==null?"":request.getParameter("adv_deal_type");
			String adv_deal_no[] = request.getParameterValues("adv_chk_deal");
			String adv_value = request.getParameter("adv_value")==null?"":request.getParameter("adv_value");
			String adv_currency = request.getParameter("adv_currency")==null?"":request.getParameter("adv_currency");
			String adv_received_dt = request.getParameter("adv_received_dt")==null?"":request.getParameter("adv_received_dt");
			String adv_iss_date = request.getParameter("adv_iss_date")==null?"":request.getParameter("adv_iss_date");
			String adv_pg_ref = request.getParameter("adv_pg_ref")==null?"":request.getParameter("adv_pg_ref");
			String adv_remark = request.getParameter("adv_remark")==null?"":request.getParameter("adv_remark");
			String adv_ref_no = request.getParameter("adv_ref_no")==null?"":request.getParameter("adv_ref_no");
			String adv_seq_no = request.getParameter("adv_seq_no")==null?"":request.getParameter("adv_seq_no");
			String adv_seq_rev_no = request.getParameter("adv_seq_rev_no")==null?"":request.getParameter("adv_seq_rev_no");
			String adv_status = "";
			if(adv_cancel_flg.equals("Y"))
			{
				adv_status = request.getParameter("adv_status2")==null?"":request.getParameter("adv_status2");
			}
			else
			{
				adv_status = request.getParameter("adv_status")==null?"":request.getParameter("adv_status");
			}
			String adv_agmt = "";
			String adv_agmt_rev = "";
			String adv_cont = "";
			String adv_cont_rev = "";
			String adv_cont_type =  "";
			String adv_countpty_cd = "";//HP20230914
			
			String seq_no = "";
			String seq_rev_no = "";
			String map_seq_no = "";
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,new_counptyCd);
			
			String counterparty_nm = "";
			String counterparty_new_nm = "";
			String pcg_counterparty_nm = "";
			String adv_counterparty_nm = "";
			if(clearance.equals("K"))
			{
				counterparty_nm = ""+utilBean.getCounterpartyName(dbcon,counterpty_cd);
				counterparty_new_nm = ""+utilBean.getCounterpartyName(dbcon,new_counptyCd);
				pcg_counterparty_nm = ""+utilBean.getCounterpartyName(dbcon,pcg_counterpty_cd);
				adv_counterparty_nm = ""+utilBean.getCounterpartyName(dbcon,adv_counterpty_cd);
			}
			else if(clearance.equals("I"))
			{
				counterparty_nm = ""+utilBean.getGasExchangeName(dbcon,counterpty_cd);
				counterparty_new_nm = ""+utilBean.getGasExchangeName(dbcon,new_counptyCd);
				pcg_counterparty_nm = ""+utilBean.getGasExchangeName(dbcon,pcg_counterpty_cd);
				adv_counterparty_nm = ""+utilBean.getGasExchangeName(dbcon,adv_counterpty_cd);
			}			
			String lc_counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterpty_cd);
			String pcg_counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,pcg_counterpty_cd);
			String adv_counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,adv_counterpty_cd);
			if(clearance.equals("I")) //HP20230914
			{
				counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,new_counptyCd);
				lc_counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,counterpty_cd);
				pcg_counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,pcg_counterpty_cd);
				adv_counterparty_abbr = ""+utilBean.getGasExchangeAbbr(dbcon,adv_counterpty_cd);
			}
			String iss_bank_nm = ""+utilBean.getBankName(dbcon,issuing_bank_cd);
			String advising_bank_nm = ""+utilBean.getBankName(dbcon,advising_bank_cd);
			String confirming_bank_nm = ""+utilBean.getBankName(dbcon,confirming_bank_cd);
			String pcg_guarantor_nm = ""+utilBean.getCounterpartyName(dbcon,pcg_guarantor_cd);
			String sysdate=utilDate.getSysdate();

			if(sec_type.equals("LC") || sec_type.equals("BG"))
			{
				old_value=request.getParameter("lc_old_value")==null?"":request.getParameter("lc_old_value");
			}
			else if(sec_type.equals("PCG"))
			{
				old_value=request.getParameter("pcg_old_value")==null?"":request.getParameter("pcg_old_value");
			}
			if(sec_type.equals("ADV"))
			{
				old_value=request.getParameter("adv_old_value")==null?"":request.getParameter("adv_old_value");
			}
			
			String new_split_by_flag = "P"; //Added By AP20260321
			String lc_split_by_flag = "P"; //Added By AP20260321
			String pcg_split_by_flag = "P"; //Added By AP20260321
			
			String operation = "";
			if(opration.equals("RESTATE"))
			{
				if(sec_type.equals("LC") || sec_type.equals("BG"))
				{
					String lc_dealNo1 = "";
					if(deal_no != null)
					{
						for(int i=0;i<deal_no.length;i++)
						{
							String[] deal_info = deal_no[i].split("/");
							cont_type =  deal_info[1];
							agmt = deal_info[2];
							agmt_rev = deal_info[3];
							cont = deal_info[4];
							cont_rev = deal_info[5];
							if(lc_dealNo1.equals(""))
							{
								lc_dealNo1+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
							}
							else
							{
								lc_dealNo1+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
							}
							if(lc_dealNo1==null)
							{
								lc_dealNo1+="";
							}
						}
					}
					
					query="UPDATE FMS_SECURITY_MST SET STATUS=?,CANCEL_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "R");
					stmt.setString(2, comp_cd);
					stmt.setString(3, counterpty_cd);
					stmt.setString(4, ref_no);
					stmt.setString(5, lc_seq_rev_no);
					stmt.setString(6, clearance);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, mseq_no);
					stmt1.setString(4, lc_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, counterpty_cd);
					stmt2.setString(4, mseq_no);
					stmt2.setString(5, lc_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					operation = "MODIFY";
					
					new_value = "CP="+counterpty_cd+"#NAME="+counterparty_nm+"#ABBR="+lc_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+deal_type+"#DEAL_NO="+lc_dealNo1+"#VALUE="+value+"#CURRENCY="+currency+"#FLUCTUATION="+fluctuation+"#VARIATION="+variation+
							   "#ISS_BANK_CD="+issuing_bank_cd+"#ISS_BANK_NAME="+iss_bank_nm+"#ISS_BANK_REF="+issuing_bank_ref+"#ADV_BANK_CD="+advising_bank_cd+"#ADV_BANK_NAME="+advising_bank_nm+"#ADV_BANK_REF="+advising_bank_ref+"#CONF_BANK_CD="+confirming_bank_cd+"#CONF_BANK_NAME="+confirming_bank_nm+
							   "#CONF_BANK_REF="+confirming_bank_ref+"#RECIEVED_DT="+received_dt+"#REVIEW_DT="+review_dt+"#ISSUANCE_DT="+issuance_dt+"#EXPIRY_DT="+expire_dt+"#STATUS="+"R"+"#TENOR="+tenor+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+ref_no+"#CANCEL_DT="+sysdate+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+ref_no+" Restated for "+counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,counterparty_nm,lc_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
					
					try
					{
						new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
					}
					catch(Exception infoLogger)
					{
						new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
					}
					
					query="SELECT MAX(SEQ_REV_NO) "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterpty_cd);
					stmt.setString(3, mseq_no);
					stmt.setString(4, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_rev_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					String sec_ref_no= lc_counterparty_abbr+"-S-"+mseq_no+"-V"+seq_rev_no;
					
					query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,CURRENCY,VALUE,"
							+ "RECEIPT_DT,REMARKS,STATUS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,DEAL_TYPE,VARIATION_VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,"
							+ "CONFIRM_BANK_CD,CONFIRM_BANK_REF,ISSUE_DT,EXPIRE_DT,TENOR,REVIEW_DT,RENEW_DT,INORDER_HIST) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),SYSDATE,?)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterpty_cd);
					stmt.setString(3, mseq_no);
					stmt.setString(4, sec_ref_no);
					stmt.setString(5, sec_category);
					stmt.setString(6, sec_type);
					stmt.setString(7, currency);
					stmt.setString(8, value);
					stmt.setString(9, received_dt);
					stmt.setString(10, remark);
					stmt.setString(11, "A");
					stmt.setString(12, emp_cd);
					stmt.setString(13, seq_rev_no);
					stmt.setString(14, clearance);
					stmt.setString(15, deal_type);
					stmt.setString(16, variation);
					stmt.setString(17, fluctuation);
					stmt.setString(18, issuing_bank_cd);
					stmt.setString(19, issuing_bank_ref);
					stmt.setString(20, advising_bank_cd);
					stmt.setString(21, advising_bank_ref);
					stmt.setString(22, confirming_bank_cd);
					stmt.setString(23, confirming_bank_ref);
					stmt.setString(24, expire_dt);
					stmt.setString(25, tenor);
					stmt.setString(26, review_dt);
					stmt.setString(27, lc_inorder_hist);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo1 = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, mseq_no);
					stmt1.setString(4, seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo1 = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo1);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, counterpty_cd);
					stmt2.setString(4, mseq_no);
					stmt2.setString(5, seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					String lc_dealNo = "";
					if(deal_no != null)
					{
						for(int i=0;i<deal_no.length;i++)
						{
							String[] deal_info = deal_no[i].split("/");
							cont_type =  deal_info[1];
							agmt = deal_info[2];
							agmt_rev = deal_info[3];
							cont = deal_info[4];
							cont_rev = deal_info[5];
							lc_split_value = split_sec[i];
							
							if(clearance.equals("I"))
							{
								countpty_cd= deal_info[6];//HP20230914
							}
							
							if(lc_split_value.equals(""))
							{
								lc_split_by_flag="";
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND SEC_REF_NO=? AND GX=?";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterpty_cd);
							stmt.setString(3, sec_ref_no);
							stmt.setString(4, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?,?,SYSDATE,?,?,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterpty_cd);
							stmt.setString(3, agmt);
							stmt.setString(4, agmt_rev);
							stmt.setString(5, cont);
							stmt.setString(6, cont_rev);
							stmt.setString(7, mseq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, sec_ref_no);
							stmt.setString(10, cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, countpty_cd);
							stmt.setString(15, lc_split_value);
							stmt.setString(16, lc_split_by_flag);
							stmt.executeQuery();
							stmt.close();
							
							operation = "INSERT";
							
							if(lc_dealNo.equals(""))
							{
								lc_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
							}
							else
							{
//								lc_dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								lc_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
							}
							if(lc_dealNo==null)
							{
								lc_dealNo+="";
							}
						}
					}
					old_value = "";
					new_value = "CP="+counterpty_cd+"#NAME="+counterparty_nm+"#ABBR="+lc_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+deal_type+"#DEAL_NO="+lc_dealNo+"#VALUE="+value+"#CURRENCY="+currency+"#FLUCTUATION="+fluctuation+"#VARIATION="+variation+
							   "#ISS_BANK_CD="+issuing_bank_cd+"#ISS_BANK_NAME="+iss_bank_nm+"#ISS_BANK_REF="+issuing_bank_ref+"#ADV_BANK_CD="+advising_bank_cd+"#ADV_BANK_NAME="+advising_bank_nm+"#ADV_BANK_REF="+advising_bank_ref+"#CONF_BANK_CD="+confirming_bank_cd+"#CONF_BANK_NAME="+confirming_bank_nm+
							   "#CONF_BANK_REF="+confirming_bank_ref+"#RECIEVED_DT="+received_dt+"#REVIEW_DT="+review_dt+"#ISSUANCE_DT="+issuance_dt+"#EXPIRY_DT="+expire_dt+"#STATUS="+"A"+"#TENOR="+tenor+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+sec_ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+sec_ref_no+" Added for "+counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,counterparty_nm,lc_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
				}
				else if(sec_type.equals("PCG"))
				{
					String pcg_dealNo1="";
					if(pcg_deal_no!=null)
					{	
						for(int i=0;i<pcg_deal_no.length;i++)
						{
							String[] deal_info = pcg_deal_no[i].split("/");
							pcg_cont_type =  deal_info[1];
							pcg_agmt = deal_info[2];
							pcg_agmt_rev = deal_info[3];
							pcg_cont = deal_info[4];
							pcg_cont_rev = deal_info[5];
							if(pcg_dealNo1.equals(""))
							{
								pcg_dealNo1+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt,pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
							}
							else
							{
								pcg_dealNo1+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt,pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
							}
							if(pcg_dealNo1==null)
							{
								pcg_dealNo1+="";
							}
						}
					}
					query="UPDATE FMS_SECURITY_MST SET STATUS=?,CANCEL_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "R");
					stmt.setString(2, comp_cd);
					stmt.setString(3, pcg_counterpty_cd);
					stmt.setString(4, pcg_ref_no);
					stmt.setString(5, pcg_seq_rev_no);
					stmt.setString(6, clearance);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, pcg_counterpty_cd);
					stmt1.setString(3, pcg_seq_no);
					stmt1.setString(4, pcg_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, pcg_counterpty_cd);
					stmt2.setString(4, pcg_seq_no);
					stmt2.setString(5, pcg_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					operation = "MODIFY";
					
					new_value = "CP="+pcg_counterpty_cd+"#NAME="+pcg_counterparty_nm+"#ABBR="+pcg_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+pcg_sec_category+"#DEAL_TYPE="+pcg_deal_type+"#DEAL_NO="+pcg_dealNo1+"#VALUE="+pcg_value+"#CURRENCY="+pcg_currency+"#FLUCTUATION="+""+"#VARIATION="+""+
							   "#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
							   "#RECIEVED_DT="+pcg_received_dt+"#REVIEW_DT="+pcg_review_dt+"#ISSUANCE_DT="+pcg_issuance_dt+"#EXPIRY_DT="+pcg_expire_dt+"#STATUS="+"R"+"#TENOR="+pcg_tenor+"#REMARK="+pcg_remark+"#GUARANTOR_CD="+pcg_guarantor_cd+"#GUARANTOR_NM="+pcg_guarantor_nm+"#SEC_REF_NO="+pcg_ref_no+"#CANCEL_DT="+sysdate+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+pcg_ref_no+" Restated for "+pcg_counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,pcg_counterparty_nm,pcg_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
					
					try
					{
						new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
					}
					catch(Exception infoLogger)
					{
						new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
					}
					
					query="SELECT MAX(SEQ_REV_NO) "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, pcg_counterpty_cd);
					stmt.setString(3, pcg_seq_no);
					stmt.setString(4, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_rev_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					String pcg_sec_ref_no= pcg_counterparty_abbr+"-S-"+pcg_seq_no+"-V"+seq_rev_no;
					
					query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,CURRENCY,VALUE,"
							+ "RECEIPT_DT,REMARKS,STATUS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,DEAL_TYPE,"
							+ "ISSUE_DT,EXPIRE_DT,TENOR,REVIEW_DT,RENEW_DT,GUARANTOR_CD) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?,"
							+ "SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),SYSDATE,?)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, pcg_counterpty_cd);
					stmt.setString(3, pcg_seq_no);
					stmt.setString(4, pcg_sec_ref_no);
					stmt.setString(5, pcg_sec_category);
					stmt.setString(6, sec_type);
					stmt.setString(7, pcg_currency);
					stmt.setString(8, pcg_value);
					stmt.setString(9, pcg_received_dt);
					stmt.setString(10, pcg_remark);
					stmt.setString(11, "A");
					stmt.setString(12, emp_cd);
					stmt.setString(13, seq_rev_no);
					stmt.setString(14, clearance);
					stmt.setString(15, pcg_deal_type);
					stmt.setString(16, pcg_expire_dt);
					stmt.setString(17, pcg_tenor);
					stmt.setString(18, pcg_review_dt);
					stmt.setString(19, pcg_guarantor_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo1 = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, pcg_counterpty_cd);
					stmt1.setString(3, pcg_seq_no);
					stmt1.setString(4, seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo1 = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo1);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, pcg_counterpty_cd);
					stmt2.setString(4, pcg_seq_no);
					stmt2.setString(5, seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					String pcg_dealNo="";
					if(pcg_deal_no!=null)
					{	
						for(int i=0;i<pcg_deal_no.length;i++)
						{
							String[] deal_info = pcg_deal_no[i].split("/");
							pcg_cont_type =  deal_info[1];
							pcg_agmt = deal_info[2];
							pcg_agmt_rev = deal_info[3];
							pcg_cont = deal_info[4];
							pcg_cont_rev = deal_info[5];
							pcg_split_value = pcg_split_sec[i];
							
							if(clearance.equals("I"))
							{
								pcg_countpty_cd= deal_info[6];//HP20230914
							}
							
							if(pcg_split_value.equals(""))
							{
								pcg_split_by_flag="";
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
									+ "AND SEC_REF_NO=? AND GX=?";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, pcg_counterpty_cd);
							stmt.setString(3, pcg_sec_ref_no);
							stmt.setString(4, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?,?,SYSDATE,?,?,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, pcg_counterpty_cd);
							stmt.setString(3, pcg_agmt);
							stmt.setString(4, pcg_agmt_rev);
							stmt.setString(5, pcg_cont);
							stmt.setString(6, pcg_cont_rev);
							stmt.setString(7, pcg_seq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, pcg_sec_ref_no);
							stmt.setString(10, pcg_cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, pcg_countpty_cd);
							stmt.setString(15, pcg_split_value);
							stmt.setString(16, pcg_split_by_flag);
							stmt.executeQuery();
							stmt.close();
							
							operation = "INSERT";
							
							if(pcg_dealNo.equals(""))
							{
								pcg_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt,pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
							}
							else
							{
//								pcg_dealNo+=", "+utilBean.getDisplayDealMapping(pcg_agmt, pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type);
								pcg_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt,pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
							}
							if(pcg_dealNo==null)
							{
								pcg_dealNo+="";
							}
							
						}
					}
					old_value="";
					new_value = "CP="+pcg_counterpty_cd+"#NAME="+pcg_counterparty_nm+"#ABBR="+pcg_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+pcg_sec_category+"#DEAL_TYPE="+pcg_deal_type+"#DEAL_NO="+pcg_dealNo+"#VALUE="+pcg_value+"#CURRENCY="+pcg_currency+"#FLUCTUATION="+""+"#VARIATION="+""+
							   "#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
							   "#RECIEVED_DT="+pcg_received_dt+"#REVIEW_DT="+pcg_review_dt+"#ISSUANCE_DT="+pcg_issuance_dt+"#EXPIRY_DT="+pcg_expire_dt+"#STATUS="+"A"+"#TENOR="+pcg_tenor+"#REMARK="+pcg_remark+"#GUARANTOR_CD="+pcg_guarantor_cd+"#GUARANTOR_NM="+pcg_guarantor_nm+"#SEC_REF_NO="+pcg_sec_ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					
					msg = "Successful! - "+sec_type+" Security "+pcg_sec_ref_no+" Added for "+pcg_counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,pcg_counterparty_nm,pcg_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
				}
				else if(sec_type.equals("ADV"))
				{
					String adv_dealNo1="";
					if(adv_deal_no!=null)
					{
						for(int i=0;i<adv_deal_no.length;i++)
						{
							String[] deal_info = adv_deal_no[i].split("/");
							adv_cont_type =  deal_info[1];
							adv_agmt = deal_info[2];
							adv_agmt_rev = deal_info[3];
							adv_cont = deal_info[4];
							adv_cont_rev = deal_info[5];
							
							if(adv_dealNo1.equals(""))
							{
								adv_dealNo1+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt,adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							else
							{
//								adv_dealNo1+=", "+utilBean.getDisplayDealMapping(adv_agmt, adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type);
								adv_dealNo1+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt,adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							if(adv_dealNo1==null)
							{
								adv_dealNo1+="";
							}
						}
					}
					query="UPDATE FMS_SECURITY_MST SET STATUS=?,CANCEL_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "R");
					stmt.setString(2, comp_cd);
					stmt.setString(3, adv_counterpty_cd);
					stmt.setString(4, adv_ref_no);
					stmt.setString(5, adv_seq_rev_no);
					stmt.setString(6, clearance);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
					 		+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, adv_counterpty_cd);
					stmt1.setString(3, adv_seq_no);
					stmt1.setString(4, adv_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, adv_counterpty_cd);
					stmt2.setString(4, adv_seq_no);
					stmt2.setString(5, adv_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					operation = "MODIFY";
					
					new_value = "CP="+adv_counterpty_cd+"#NAME="+adv_counterparty_nm+"#ABBR="+adv_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+adv_sec_category+"#DEAL_TYPE="+adv_deal_type+"#DEAL_NO="+adv_dealNo1+"#VALUE="+adv_value+"#CURRENCY="+adv_currency+
							"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
						    "#RECIEVED_DT="+adv_received_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+adv_iss_date+"#EXPIRY_DT="+""+"#STATUS="+"R"+"#TENOR="+""+"#REMARK="+adv_remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+adv_ref_no+"#CANCEL_DT="+sysdate+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+adv_ref_no+" Restated for "+adv_counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,adv_counterparty_nm,adv_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
					
					try
					{
						new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
					}
					catch(Exception infoLogger)
					{
						new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
					}
					
					query="SELECT MAX(SEQ_REV_NO) "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, adv_counterpty_cd);
					stmt.setString(3, adv_seq_no);
					stmt.setString(4, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_rev_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					String adv_sec_ref_no= adv_counterparty_abbr+"-S-"+adv_seq_no+"-V"+seq_rev_no;
					
					query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,CURRENCY,VALUE,"
							+ "RECEIPT_DT,REMARKS,STATUS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,DEAL_TYPE,RENEW_DT,PG_REF) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?,SYSDATE,? )";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, adv_counterpty_cd);
					stmt.setString(3, adv_seq_no);
					stmt.setString(4, adv_sec_ref_no);
					stmt.setString(5, adv_sec_category);
					stmt.setString(6, sec_type);
					stmt.setString(7, adv_currency);
					stmt.setString(8, adv_value);
					stmt.setString(9, adv_received_dt);
					stmt.setString(10, adv_remark);
					stmt.setString(11, "A");
					stmt.setString(12, emp_cd);
					stmt.setString(13, seq_rev_no);
					stmt.setString(14, clearance);
					stmt.setString(15, adv_deal_type);
					stmt.setString(16, adv_pg_ref);
					stmt.executeUpdate();
					stmt.close();
					
					String dtl_seqNo1 = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, adv_counterpty_cd);
					stmt1.setString(3, adv_seq_no);
					stmt1.setString(4, seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo1 = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo1);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, adv_counterpty_cd);
					stmt2.setString(4, adv_seq_no);
					stmt2.setString(5, seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					String adv_dealNo="";
					if(adv_deal_no!=null)
					{
						for(int i=0;i<adv_deal_no.length;i++)
						{
							String[] deal_info = adv_deal_no[i].split("/");
							adv_cont_type =  deal_info[1];
							adv_agmt = deal_info[2];
							adv_agmt_rev = deal_info[3];
							adv_cont = deal_info[4];
							adv_cont_rev = deal_info[5];
							if(clearance.equals("I"))
							{
								adv_countpty_cd= deal_info[6];//HP20230914
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
									+ "AND SEC_REF_NO=? AND GX=? ";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, adv_counterpty_cd);
							stmt.setString(3, adv_sec_ref_no);
							stmt.setString(4, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?,?,SYSDATE,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, adv_counterpty_cd);
							stmt.setString(3, adv_agmt);
							stmt.setString(4, adv_agmt_rev);
							stmt.setString(5, adv_cont);
							stmt.setString(6, adv_cont_rev);
							stmt.setString(7, adv_seq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, adv_sec_ref_no);
							stmt.setString(10, adv_cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, adv_countpty_cd);
							stmt.executeQuery();
							stmt.close();
							
							operation = "INSERT";
							
							if(adv_dealNo.equals(""))
							{
								adv_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt,adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							else
							{
//								adv_dealNo+=", "+utilBean.getDisplayDealMapping(adv_agmt, adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type);
								adv_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt,adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							if(adv_dealNo==null)
							{
								adv_dealNo+="";
							}
						}
					}
					old_value = "";
					new_value = "CP="+adv_counterpty_cd+"#NAME="+adv_counterparty_nm+"#ABBR="+adv_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+adv_sec_category+"#DEAL_TYPE="+adv_deal_type+"#DEAL_NO="+adv_dealNo+"#VALUE="+adv_value+"#CURRENCY="+adv_currency+
							"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
						    "#RECIEVED_DT="+adv_received_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+adv_iss_date+"#EXPIRY_DT="+""+"#STATUS="+"A"+"#TENOR="+""+"#REMARK="+adv_remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+adv_sec_ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+adv_sec_ref_no+" Added for "+adv_counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,adv_counterparty_nm,adv_counterparty_abbr,operation,msg,emp_cd,new_value,old_value);
				}
			}
			else if(opration.equals("MODIFY"))
			{
				if(sec_type.equals("LC") || sec_type.equals("BG"))
				{
					int num=0;
					query="UPDATE FMS_SECURITY_MST SET SEC_TYPE=?,SEC_CATEGORY=?,DEAL_TYPE=?,CURRENCY=?,"
							+ "VARIATION_VALUE=?,VALUE=?,VALUE_FLUC=?,ISS_BANK_CD=?,ISS_BANK_REF=?,"
							+ "ADV_BANK_CD=?,ADV_BANK_REF=?,CONFIRM_BANK_CD=?,CONFIRM_BANK_REF=?,RECEIPT_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "ISSUE_DT=TO_DATE(?,'DD/MM/YYYY'), EXPIRE_DT=TO_DATE(?,'DD/MM/YYYY'),TENOR=?,REVIEW_DT=TO_DATE(?,'DD/MM/YYYY'),STATUS=?,MODIFY_DT=SYSDATE , MODIFY_BY=? ,REMARKS=? ";
					if(status.equals("C"))
					{
						query+=",CANCEL_DT=SYSDATE ";
					}
					else 
					{
						query+=",CANCEL_DT=? ";
					}					
					if(status.equals("O"))
					{
						query+=",INORDER_HIST=?,APRV_DT=SYSDATE,APRV_BY=?";
					}
					query+=	"WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEC_REF_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt = dbcon.prepareStatement(query);
					stmt.setString(++num, lc_sec_type);
					stmt.setString(++num, sec_category);
					stmt.setString(++num, deal_type);
					stmt.setString(++num, currency);
					stmt.setString(++num, variation);
					stmt.setString(++num, value);
					stmt.setString(++num, fluctuation);
					stmt.setString(++num, issuing_bank_cd);
					stmt.setString(++num, issuing_bank_ref);
					stmt.setString(++num, advising_bank_cd);
					stmt.setString(++num, advising_bank_ref);
					stmt.setString(++num, confirming_bank_cd);
					stmt.setString(++num, confirming_bank_ref);
					stmt.setString(++num, received_dt);
					stmt.setString(++num, issuance_dt);
					stmt.setString(++num, expire_dt);
					stmt.setString(++num, tenor);
					stmt.setString(++num, review_dt);
					stmt.setString(++num, status);
					stmt.setString(++num, emp_cd);
					stmt.setString(++num, remark);
					
					if(!status.equals("C"))
					{
						stmt.setString(++num, "");
					}
					if(status.equals("O"))
					{
						stmt.setString(++num, "Y");
						stmt.setString(++num, emp_cd);
					}
					stmt.setString(++num, comp_cd);
					stmt.setString(++num, counterpty_cd);
					stmt.setString(++num, ref_no);
					stmt.setString(++num, clearance);
					stmt.executeUpdate();
					stmt.close();
					
					String sec_ref_no="";
					query="SELECT SEC_REF_NO "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterpty_cd);
					stmt.setString(3, mseq_no);
					stmt.setString(4, lc_seq_rev_no);
					stmt.setString(5, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						sec_ref_no = rset.getString(1);
					}
					rset.close();
					stmt.close();
					if(sec_ref_no.equals(ref_no))
					{
						query = "DELETE FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND SEQ_NO=? AND SEC_REF_NO=? AND GX=?";
						//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterpty_cd);
						stmt.setString(3, mseq_no);
						stmt.setString(4, sec_ref_no);
						stmt.setString(5, clearance);
						stmt.executeUpdate();
						stmt.close();
					}
					String lc_dealNo = "";
					String lc_deal_dtl = "";
					if(deal_no != null)
					{
						for(int i=0;i<deal_no.length;i++)
						{
							String[] deal_info = deal_no[i].split("/");
							cont_type =  deal_info[1];
							agmt = deal_info[2];
							agmt_rev = deal_info[3];
							cont = deal_info[4];
							cont_rev = deal_info[5];
							lc_split_value = split_sec[i];
							
							if(clearance.equals("I"))
							{
								countpty_cd= deal_info[6];//HP20230914
							}
							
							if(lc_split_value.equals(""))
							{
								lc_split_by_flag="";
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterpty_cd);
							stmt.setString(3, ref_no);
							stmt.setString(4, lc_seq_rev_no);
							stmt.setString(5, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY ) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterpty_cd);
							stmt.setString(3, agmt);
							stmt.setString(4, agmt_rev);
							stmt.setString(5, cont);
							stmt.setString(6, cont_rev);
							stmt.setString(7, mseq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, ref_no);
							stmt.setString(10, cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, lc_seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, countpty_cd);
							stmt.setString(15, lc_split_value);
							stmt.setString(16, lc_split_by_flag);
							stmt.executeQuery();
							stmt.close();
							if(lc_dealNo.equals(""))
							{
								lc_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
								lc_deal_dtl+= agmt+"@"+agmt_rev+"@"+cont+"@"+cont_rev+"@"+cont_type+"@"+counterpty_cd;
							}
							else
							{
								//lc_dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								lc_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,agmt_rev, cont, cont_rev, cont_type, "");
								lc_deal_dtl+= ", "+agmt+"@"+agmt_rev+"@"+cont+"@"+cont_rev+"@"+cont_type+"@"+counterpty_cd;
							}
							if(lc_dealNo==null)
							{
								lc_dealNo+="";
								lc_deal_dtl+="";
							}
						}
					}
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
				 			+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, mseq_no);
					stmt1.setString(4, lc_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, counterpty_cd);
					stmt2.setString(4, mseq_no);
					stmt2.setString(5, lc_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					new_value = "CP="+counterpty_cd+"#NAME="+counterparty_nm+"#ABBR="+lc_counterparty_abbr+"#SEC_TYPE="+lc_sec_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+deal_type+"#DEAL_NO="+lc_dealNo+"#VALUE="+value+"#CURRENCY="+currency+"#FLUCTUATION="+fluctuation+"#VARIATION="+variation+
							   "#ISS_BANK_CD="+issuing_bank_cd+"#ISS_BANK_NAME="+iss_bank_nm+"#ISS_BANK_REF="+issuing_bank_ref+"#ADV_BANK_CD="+advising_bank_cd+"#ADV_BANK_NAME="+advising_bank_nm+"#ADV_BANK_REF="+advising_bank_ref+"#CONF_BANK_CD="+confirming_bank_cd+"#CONF_BANK_NAME="+confirming_bank_nm+
							   "#CONF_BANK_REF="+confirming_bank_ref+"#RECIEVED_DT="+received_dt+"#REVIEW_DT="+review_dt+"#ISSUANCE_DT="+issuance_dt+"#EXPIRY_DT="+expire_dt+"#STATUS="+status+"#TENOR="+tenor+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+ref_no+"#CANCEL_DT="+""+"#DEAL_DTL="+lc_deal_dtl+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+ref_no+" Updated for "+counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,counterparty_nm,lc_counterparty_abbr,opration,msg,emp_cd,new_value,old_value);
				}
				else if(sec_type.equals("PCG"))
				{
					int num=0;
					query="UPDATE FMS_SECURITY_MST SET SEC_CATEGORY=?,DEAL_TYPE=?,CURRENCY=?,"
							+ "VALUE=?,RECEIPT_DT=TO_DATE(?,'DD/MM/YYYY'),ISSUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "EXPIRE_DT=TO_DATE(?,'DD/MM/YYYY'),TENOR=?,REVIEW_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "MODIFY_DT=SYSDATE , MODIFY_BY=? ,REMARKS=?,STATUS=?,GUARANTOR_CD=? ";
					if(pcg_status.equals("C"))
					{
						query+=",CANCEL_DT=SYSDATE ";
					}else 
					{
						query+=",CANCEL_DT=? ";
					}
					if(pcg_status.equals("O"))
					{
						query+=",INORDER_HIST=?,APRV_DT=SYSDATE ,APRV_BY=?";
					}
					query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEC_REF_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++num, pcg_sec_category);
					stmt.setString(++num, pcg_deal_type);
					stmt.setString(++num, pcg_currency);
					stmt.setString(++num, pcg_value);
					stmt.setString(++num, pcg_received_dt);
					stmt.setString(++num, pcg_issuance_dt);
					stmt.setString(++num, pcg_expire_dt);
					stmt.setString(++num, pcg_tenor);
					stmt.setString(++num, pcg_review_dt);
					stmt.setString(++num, emp_cd);
					stmt.setString(++num, pcg_remark);
					stmt.setString(++num, pcg_status);
					stmt.setString(++num, pcg_guarantor_cd);
					
					if(!pcg_status.equals("C"))
					{
						stmt.setString(++num, "");
					}
					if(pcg_status.equals("O"))
					{
						stmt.setString(++num, "Y");
						stmt.setString(++num, emp_cd);
					}
					stmt.setString(++num, comp_cd);
					stmt.setString(++num, pcg_counterpty_cd);
					stmt.setString(++num, pcg_ref_no);
					stmt.setString(++num, clearance);
					stmt.executeUpdate();
					stmt.close();
					String pcg_sec_ref_no="";
					query="SELECT SEC_REF_NO "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, pcg_counterpty_cd);
					stmt.setString(3, pcg_seq_no);
					stmt.setString(4, pcg_seq_rev_no);
					stmt.setString(5, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						pcg_sec_ref_no = rset.getString(1);
					}
					rset.close();
					stmt.close();
					if(pcg_sec_ref_no.equals(pcg_ref_no))
					{
						query = "DELETE FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND SEQ_NO=? AND SEC_REF_NO=? AND GX=?";
						//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, pcg_counterpty_cd);
						stmt.setString(3, pcg_seq_no);
						stmt.setString(4, pcg_sec_ref_no);
						stmt.setString(5, clearance);
						stmt.executeUpdate();
						stmt.close();
					}
					String pcg_dealNo="";
					if(pcg_deal_no!=null)
					{	
						for(int i=0;i<pcg_deal_no.length;i++)
						{
							String[] deal_info = pcg_deal_no[i].split("/");
							pcg_cont_type =  deal_info[1];
							pcg_agmt = deal_info[2];
							pcg_agmt_rev = deal_info[3];
							pcg_cont = deal_info[4];
							pcg_cont_rev = deal_info[5];
							pcg_split_value = pcg_split_sec[i];
							
							if(clearance.equals("I"))
							{
								pcg_countpty_cd= deal_info[6];//HP20230914
							}
							
							if(pcg_split_value.equals(""))
							{
								pcg_split_by_flag="";
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
									+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, pcg_counterpty_cd);
							stmt.setString(3, pcg_ref_no);
							stmt.setString(4, pcg_seq_rev_no);
							stmt.setString(5, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, pcg_counterpty_cd);
							stmt.setString(3, pcg_agmt);
							stmt.setString(4, pcg_agmt_rev);
							stmt.setString(5, pcg_cont);
							stmt.setString(6, pcg_cont_rev);
							stmt.setString(7, pcg_seq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, pcg_ref_no);
							stmt.setString(10, pcg_cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, pcg_seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, pcg_countpty_cd);
							stmt.setString(15, pcg_split_value);
							stmt.setString(16, pcg_split_by_flag);
							stmt.executeQuery();
							stmt.close();
							if(pcg_dealNo.equals(""))
							{
								pcg_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt, pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
							}
							else
							{
								//pcg_dealNo+=", "+utilBean.getDisplayDealMapping(pcg_agmt, pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type);
								pcg_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, pcg_agmt, pcg_agmt_rev, pcg_cont, pcg_cont_rev, pcg_cont_type, "");
								
							}
							if(pcg_dealNo==null)
							{
								pcg_dealNo+="";
							}
						}
					}
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
				 			+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, pcg_counterpty_cd);
					stmt1.setString(3, pcg_seq_no);
					stmt1.setString(4, pcg_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, pcg_counterpty_cd);
					stmt2.setString(4, pcg_seq_no);
					stmt2.setString(5, pcg_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					new_value = "CP="+pcg_counterpty_cd+"#NAME="+pcg_counterparty_nm+"#ABBR="+pcg_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+pcg_sec_category+"#DEAL_TYPE="+pcg_deal_type+"#DEAL_NO="+pcg_dealNo+"#VALUE="+pcg_value+"#CURRENCY="+pcg_currency+"#FLUCTUATION="+""+"#VARIATION="+""+
							   "#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
							   "#RECIEVED_DT="+pcg_received_dt+"#REVIEW_DT="+pcg_review_dt+"#ISSUANCE_DT="+pcg_issuance_dt+"#EXPIRY_DT="+pcg_expire_dt+"#STATUS="+pcg_status+"#TENOR="+pcg_tenor+"#REMARK="+pcg_remark+"#GUARANTOR_CD="+pcg_guarantor_cd+"#GUARANTOR_NM="+pcg_guarantor_nm+"#SEC_REF_NO="+pcg_ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+pcg_ref_no+" Updated for "+pcg_counterparty_nm+" !";
					msg_type="S";
					
					SecurityMailBody(comp_cd,pcg_counterparty_nm, pcg_counterparty_abbr, opration,msg,emp_cd,new_value,old_value);
				}
				else if(sec_type.equals("ADV"))
				{
					int num=0;
					query="UPDATE FMS_SECURITY_MST SET SEC_CATEGORY=?,DEAL_TYPE=?,CURRENCY=?,"
							+ "VALUE=?,RECEIPT_DT=TO_DATE(?,'DD/MM/YYYY'),PG_REF=?, "
							+ "MODIFY_DT=SYSDATE , MODIFY_BY=? ,REMARKS=?,STATUS=? ";
					if(adv_status.equals("C"))
					{
						query+=",CANCEL_DT=SYSDATE ";
					}else {
						query+=",CANCEL_DT=? ";
					}
					if(adv_status.equals("O"))
					{
						query+=",INORDER_HIST=?,APRV_DT=SYSDATE,APRV_BY=?";
					}
					query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEC_REF_NO=? AND GX=? ";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++num, adv_sec_category);
					stmt.setString(++num, adv_deal_type);
					stmt.setString(++num, adv_currency);
					stmt.setString(++num, adv_value);
					stmt.setString(++num, adv_received_dt);
					stmt.setString(++num, adv_pg_ref);
					stmt.setString(++num, emp_cd);
					stmt.setString(++num, adv_remark);
					stmt.setString(++num, adv_status);
					if(!adv_status.equals("C"))
					{
						stmt.setString(++num, "");
					}
					if(adv_status.equals("O"))
					{
						stmt.setString(++num, "Y");
						stmt.setString(++num, emp_cd);
					}
					stmt.setString(++num, comp_cd);
					stmt.setString(++num, adv_counterpty_cd);
					stmt.setString(++num, adv_ref_no);
					stmt.setString(++num, clearance);
					stmt.executeUpdate();
					stmt.close();
					
					String adv_sec_ref_no="";
					query="SELECT SEC_REF_NO "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, adv_counterpty_cd);
					stmt.setString(3, adv_seq_no);
					stmt.setString(4, adv_seq_rev_no);
					stmt.setString(5, clearance);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						adv_sec_ref_no = rset.getString(1);
					}
					rset.close();
					stmt.close();
					
					if(adv_sec_ref_no.equals(adv_ref_no))
					{
						query = "DELETE FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND SEQ_NO=? AND SEC_REF_NO=? AND GX=?";
						//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, adv_counterpty_cd);
						stmt.setString(3, adv_seq_no);
						stmt.setString(4, adv_sec_ref_no);
						stmt.setString(5, clearance);
						stmt.executeUpdate();
						stmt.close();
					}
					
					String adv_dealNo = "";
					if(adv_deal_no!=null)
					{
						for(int i=0;i<adv_deal_no.length;i++)
						{
							String[] deal_info = adv_deal_no[i].split("/");
							adv_cont_type =  deal_info[1];
							adv_agmt = deal_info[2];
							adv_agmt_rev = deal_info[3];
							adv_cont = deal_info[4];
							adv_cont_rev = deal_info[5];
							if(clearance.equals("I"))
							{
								adv_countpty_cd= deal_info[6];//HP20230914
							}
							
							query="SELECT MAX(MAP_SEQ_NO) "
									+ "FROM FMS_SECURITY_DEAL_MAP "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
									+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
							//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, adv_counterpty_cd);
							stmt.setString(3, adv_ref_no);
							stmt.setString(4, adv_seq_rev_no);
							stmt.setString(5, clearance);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								map_seq_no=""+(rset.getInt(1)+1);
							}
							else
							{
								map_seq_no="1";
							}
							rset.close();
							stmt.close();
							
							query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
									+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?)";
							//HP20230914 ADDED ENTITY_CD COLUMN
							//HP20230913 ADDED GX COLUMN
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, adv_counterpty_cd);
							stmt.setString(3, adv_agmt);
							stmt.setString(4, adv_agmt_rev);
							stmt.setString(5, adv_cont);
							stmt.setString(6, adv_cont_rev);
							stmt.setString(7, adv_seq_no);
							stmt.setString(8, map_seq_no);
							stmt.setString(9, adv_ref_no);
							stmt.setString(10, adv_cont_type);
							stmt.setString(11, emp_cd);
							stmt.setString(12, adv_seq_rev_no);
							stmt.setString(13, clearance);
							stmt.setString(14, adv_countpty_cd);
							stmt.executeQuery();
							stmt.close();
							
							if(adv_dealNo.equals(""))
							{
//								//adv_dealNo+=utilBean.getDisplayDealMapping(adv_agmt, adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type);
								adv_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt, adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							else
							{
								adv_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, adv_agmt, adv_agmt_rev, adv_cont, adv_cont_rev, adv_cont_type, "");
							}
							if(adv_dealNo==null)
							{
								adv_dealNo+="";
							}
						}
					}
					
					String dtl_seqNo = "";
					query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
							+ "FROM LOG_FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
				 			+ "AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, adv_counterpty_cd);
					stmt1.setString(3, adv_seq_no);
					stmt1.setString(4, adv_seq_rev_no);
					stmt1.setString(5, clearance);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) 
					{
						dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
							+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
							+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
							+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
							+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
							+ "FROM FMS_SECURITY_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, dtl_seqNo);
					stmt2.setString(2, comp_cd);
					stmt2.setString(3, adv_counterpty_cd);
					stmt2.setString(4, adv_seq_no);
					stmt2.setString(5, adv_seq_rev_no);
					stmt2.setString(6, clearance);
					stmt2.executeQuery();
					stmt2.close();
					
					new_value = "CP="+adv_counterpty_cd+"#NAME="+adv_counterparty_nm+"#ABBR="+adv_counterparty_abbr+"#SEC_TYPE="+sec_type+"#SEC_CATEGORY="+adv_sec_category+"#DEAL_TYPE="+adv_deal_type+"#DEAL_NO="+adv_dealNo+"#VALUE="+adv_value+"#CURRENCY="+adv_currency+
								"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+"#CONF_BANK_REF="+""+
							    "#RECIEVED_DT="+adv_received_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+adv_iss_date+"#EXPIRY_DT="+""+"#STATUS="+adv_status+"#TENOR="+""+"#REMARK="+adv_remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+adv_ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
					
					msg = "Successful! - "+sec_type+" Security "+adv_ref_no+" Updated for "+adv_counterparty_nm+" !";
					msg_type="S";
					SecurityMailBody(comp_cd,adv_counterparty_nm, adv_counterparty_abbr, opration,msg,emp_cd,new_value,old_value);
				}
			}
			else if(opration.equals("INSERT"))
			{
				query="SELECT MAX(SEQ_NO)"
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=?";
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, new_counptyCd);
				stmt.setString(3, clearance);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset.close();
				stmt.close();
				ref_no=counterparty_abbr+"-S-"+seq_no;
				String new_dealNo="";
				if(new_deal_no != null)
				{
					for(int i=0;i<new_deal_no.length;i++)
					{
						query="SELECT MAX(MAP_SEQ_NO) "
								+ "FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND SEQ_NO=? AND GX=?";
						//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_counptyCd);
						stmt.setString(3, seq_no);
						stmt.setString(4, clearance);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							map_seq_no=""+(rset.getInt(1)+1);
						}
						else
						{
							map_seq_no="1";
						}
						rset.close();
						stmt.close();
						
						String[] new_deal_info = new_deal_no[i].split("-");
						if(clearance.equals("I"))
						{
							new_countpty_cd=new_deal_info[0];//HP20230914
						}
						new_cont_type =  new_deal_info[1];
						new_agmt = new_deal_info[2];
						new_agmt_rev = new_deal_info[3];
						new_cont = new_deal_info[4];
						new_cont_rev = new_deal_info[5];
						
						if(new_cont_type.equals("G") || new_cont_type.equals("P") || new_cont_type.equals("O") || new_cont_type.equals("Q"))
						{
							new_deal_type="LTCORA";
						}
						else if(new_cont_type.equals("N") || new_cont_type.equals("E") || new_cont_type.equals("F") || new_cont_type.equals("W"))
						{
							new_deal_type="LNG";
						}
						else if(new_cont_type.equals("C") || new_cont_type.equals("R"))
						{
							new_deal_type="GTA";
						}
						else if(new_cont_type.equals("B") || new_cont_type.equals("M"))
						{
							new_deal_type="Truck Service";
						}
						else
						{
							new_deal_type="GAS";
						}
						
						/*if(new_cont_type.equals("G") || new_cont_type.equals("P") || new_cont_type.equals("O") || new_cont_type.equals("Q"))
						{
							new_deal_type="LTCORA";
						}
						else
						{
							new_deal_type="GAS";
						}*/
						
						if(!new_sec_type.equals("ADV"))
						{
							new_split_value = new_split_sec[i];
						}
						
						if(new_split_value.equals(""))
						{
							new_split_by_flag="";
						}
						
						query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
								+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX,ENTITY_CD,SHARE_PERCENT,SPLIT_BY ) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
						//HP20230914 ADDED ENTITY_CD COLUMN
						//HP20230913 ADDED GX COLUMN
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_counptyCd);
						stmt.setString(3, new_agmt);
						stmt.setString(4, new_agmt_rev);
						stmt.setString(5, new_cont);
						stmt.setString(6, new_cont_rev);
						stmt.setString(7, seq_no);
						stmt.setString(8, map_seq_no);
						stmt.setString(9, ref_no);
						stmt.setString(10, new_cont_type);
						stmt.setString(11, emp_cd);
						stmt.setString(12, "0");
						stmt.setString(13, clearance);
						stmt.setString(14, new_countpty_cd);
						stmt.setString(15, new_split_value);
						stmt.setString(16, new_split_by_flag);
						stmt.executeQuery();
						stmt.close();
						
						if(new_dealNo.equals(""))
						{
							new_dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						}
						else
						{
							new_dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						}
						if(new_dealNo==null)
						{
							new_dealNo+="";
						}
					}
				}
				
				query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,CURRENCY,VALUE,"
						+ "RECEIPT_DT,REMARKS,STATUS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,DEAL_TYPE) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?)";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, new_counptyCd);
				stmt.setString(3, seq_no);
				stmt.setString(4, ref_no);
				stmt.setString(5, new_category);
				stmt.setString(6, new_sec_type);
				stmt.setString(7, new_currency);
				stmt.setString(8, new_sec_value);
				stmt.setString(9, new_reciept_dt);
				stmt.setString(10, new_remark);
				stmt.setString(11, "A");
				stmt.setString(12, emp_cd);
				stmt.setString(13, "0");
				stmt.setString(14, clearance);
				stmt.setString(15, new_deal_type);
				stmt.executeUpdate();
				stmt.close();
				
				String dtl_seqNo = "";
				query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
						+ "FROM LOG_FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, new_counptyCd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, "0");
				stmt1.setString(5, clearance);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, dtl_seqNo);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, new_counptyCd);
				stmt2.setString(4, seq_no);
				stmt2.setString(5, "0");
				stmt2.setString(6, clearance);
				stmt2.executeQuery();
				stmt2.close();
				
				old_value = "";
				
				new_value = "CP="+new_counptyCd+"#NAME="+counterparty_new_nm+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+new_sec_type+"#SEC_CATEGORY="+new_category+"#DEAL_TYPE="+""+"#DEAL_NO="+new_dealNo+"#VALUE="+new_sec_value+"#CURRENCY="+new_currency+
							"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
						    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+new_reciept_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+"A"+"#TENOR="+""+"#REMARK="+new_remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+ref_no+"#CANCEL_DT="+""+"#GX="+clearance+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
				
				msg = "Successful! - "+new_sec_type+" Security "+ref_no+" Added for "+counterparty_new_nm+" !";
				msg_type="S";
				
				SecurityMailBody(comp_cd,counterparty_new_nm, counterparty_abbr, opration,msg,emp_cd,new_value,old_value);
			}
			
			if(!sec_type.equals("ADV") && !new_sec_type.equals("ADV"))
			{
				url = "../credit_risk/frm_collateral_management.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&clearance="+clearance+commonUrl_pra;
			}
			else 
			{
				url = "../credit_risk/frm_advance_booking.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&clearance="+clearance+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Security Detail Insert/Update submission Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdatePreReceiptSecurityDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatePreReceiptSecurityDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String counterparty_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"0":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");
			String sec_category = request.getParameter("sec_category")==null?"":request.getParameter("sec_category");
			
			String security_type=request.getParameter("security_type")==null?"":request.getParameter("security_type");
			String received_dt=request.getParameter("received_dt")==null?"":request.getParameter("received_dt");
			String currency=request.getParameter("currency")==null?"":request.getParameter("currency");
			String amount=request.getParameter("amount")==null?"":request.getParameter("amount");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			
			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String seq_rev_no=request.getParameter("seq_rev_no")==null?"":request.getParameter("seq_rev_no");	
			String map_seq_no=request.getParameter("map_seq_no")==null?"":request.getParameter("map_seq_no");
			String security_ref=request.getParameter("security_ref")==null?"":request.getParameter("security_ref");
			String deal_no = request.getParameter("deal_no")==null?"":request.getParameter("deal_no");
			String gx=request.getParameter("gx")==null?"":request.getParameter("gx");
			
			old_value=request.getParameter("pre_old_value")==null?"":request.getParameter("pre_old_value");
			String deal_type="";
			if(contract_type.equals("G") || contract_type.equals("P") || contract_type.equals("O") || contract_type.equals("Q"))
			{
				deal_type="LTCORA";
			}
			else if(contract_type.equals("N") || contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W"))
			{
				deal_type="LNG";
			}
			else if(contract_type.equals("C") || contract_type.equals("R"))
			{
				deal_type="GTA";
			}
			else if(contract_type.equals("B") || contract_type.equals("M"))
			{
				deal_type="Truck Service";
			}
			else
			{
				deal_type="GAS";
			}
			
			if(opration.equals("MODIFY"))
			{
				query="UPDATE FMS_SECURITY_MST SET SEC_TYPE=?,CURRENCY=?,VALUE=?,"
						+ "RECEIPT_DT=TO_DATE(?,'DD/MM/YYYY'),REMARKS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";					
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, security_type);
				stmt.setString(2, currency);
				stmt.setString(3, amount);
				stmt.setString(4, received_dt);
				stmt.setString(5, remark);
				stmt.setString(6, emp_cd);
				stmt.setString(7, comp_cd);
				stmt.setString(8, counterparty_cd);
				stmt.setString(9, seq_no);
				stmt.setString(10, seq_rev_no);
				stmt.setString(11, gx);
				stmt.executeQuery();
				stmt.close();
				
				String dtl_seqNo = "";
				query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) FROM LOG_FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";  
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, seq_rev_no);
				stmt1.setString(5, gx);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1,dtl_seqNo);
				stmt2.setString(2,comp_cd);
				stmt2.setString(3,counterparty_cd);
				stmt2.setString(4,seq_no);
				stmt2.setString(5,seq_rev_no);
				stmt2.setString(6,gx);
				stmt2.executeQuery();
				stmt2.close();
				
				new_value = "CP="+counterparty_cd+"#NAME="+counterparty_name+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+security_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+amount+"#CURRENCY="+currency+
						"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
					    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+received_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+"P"+"#TENOR="+""+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+security_ref+"#CANCEL_DT="+""+"#GX="+gx+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
				
				msg = "Successful! - "+security_type+" Security Modification Successfully!";
				msg_type="S";
			}
			else
			{
				query="SELECT MAX(SEQ_NO) "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=?";
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, gx);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset.close();
				stmt.close();
				
				query="SELECT NVL(MAX(SEQ_REV_NO),-1) "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=? "
						+ "AND SEQ_NO=?";
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, gx);
				stmt.setString(4, seq_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_rev_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_rev_no="0";
				}
				rset.close();
				stmt.close();
				
				security_ref=counterparty_abbr+"-S-"+seq_no;
				
				query="INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,CURRENCY,"
						+ "VALUE,RECEIPT_DT,STATUS,REMARKS,ENT_BY,ENT_DT,SEQ_REV_NO,GX,DEAL_TYPE) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?)";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1,comp_cd);
				stmt.setString(2,counterparty_cd);
				stmt.setString(3,seq_no);
				stmt.setString(4,security_ref);
				stmt.setString(5,sec_category);
				stmt.setString(6,security_type);
				stmt.setString(7,currency);
				stmt.setString(8,amount);
				stmt.setString(9,received_dt);
				stmt.setString(10,"P");
				stmt.setString(11,remark);
				stmt.setString(12,emp_cd);
				stmt.setString(13,seq_rev_no);
				stmt.setString(14,gx);
				stmt.setString(15,deal_type);
				stmt.executeQuery();
				stmt.close();
				
				query="SELECT MAX(MAP_SEQ_NO) "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GX=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=?";
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, gx);
				stmt.setString(4, seq_no);
				stmt.setString(5, seq_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					map_seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					map_seq_no="1";
				}
				rset.close();
				stmt.close();
				
				query="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SEQ_NO,"
						+ "MAP_SEQ_NO,SEC_REF_NO,CONTRACT_TYPE,ENT_BY,ENT_DT,SEQ_REV_NO,GX) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?)";
				//HP20230913 ADDED GX COLUMN
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1,comp_cd);
				stmt.setString(2,counterparty_cd);
				stmt.setString(3,agmt_no);
				stmt.setString(4,agmt_rev_no);
				stmt.setString(5,cont_no);
				stmt.setString(6,cont_rev_no);
				stmt.setString(7,seq_no);
				stmt.setString(8,map_seq_no);
				stmt.setString(9,security_ref);
				stmt.setString(10,contract_type);
				stmt.setString(11,emp_cd);
				stmt.setString(12,seq_rev_no);
				stmt.setString(13,gx);
				stmt.executeQuery();
				stmt.close();
				
				//deal_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
				deal_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no,agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
				
				
				String dtl_seqNo = "";
				query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) FROM LOG_FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, seq_rev_no);
				stmt1.setString(5, gx);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					dtl_seqNo = rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE, ADDED SEQ_REV_NO IN WHERE CLAUSE
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1,dtl_seqNo);
				stmt2.setString(2,comp_cd);
				stmt2.setString(3,counterparty_cd);
				stmt2.setString(4,seq_no);
				stmt2.setString(5,seq_rev_no);
				stmt2.setString(6,gx);
				stmt2.executeQuery();
				stmt2.close();
				
				msg = "Successful! - "+security_type+" Security Inserted Successfully!";
				msg_type="S";
				
				old_value="";
				
				new_value = "CP="+counterparty_cd+"#NAME="+counterparty_name+"#ABBR="+counterparty_abbr+"#SEC_TYPE="+security_type+"#SEC_CATEGORY="+sec_category+"#DEAL_TYPE="+""+"#DEAL_NO="+deal_no+"#VALUE="+amount+"#CURRENCY="+currency+
						"#FLUCTUATION="+""+"#VARIATION="+""+"#ISS_BANK_CD="+""+"#ISS_BANK_NAME="+""+"#ISS_BANK_REF="+""+"#ADV_BANK_CD="+""+"#ADV_BANK_NAME="+""+"#ADV_BANK_REF="+""+"#CONF_BANK_CD="+""+"#CONF_BANK_NAME="+""+
					    "#CONF_BANK_REF="+""+"#RECIEVED_DT="+received_dt+"#REVIEW_DT="+""+"#ISSUANCE_DT="+""+"#EXPIRY_DT="+""+"#STATUS="+"P"+"#TENOR="+""+"#REMARK="+remark+"#GUARANTOR_CD="+""+"#GUARANTOR_NM="+""+"#SEC_REF_NO="+security_ref+"#CANCEL_DT="+""+"#GX="+gx+"#REVERSAL="+""+"#SAP_APPROVAL="+"";
			}
			
			SecurityMailBody(comp_cd,counterparty_name, counterparty_abbr, opration,msg,emp_cd,new_value,old_value);
			url = "../credit_risk/frm_pre_receipt_security.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&sec_category="+sec_category+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Pre-receipt Security Insert/Update submission Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void SecurityMailBody(String comp_cd,String cp_name,String cp_abbr,String operation,String msg,String emp_cd,String new_values, String old_values) throws Exception
	{
		String function_nm="SecurityMailBody()";
		String mailBody="";
		try
		{
			String security_details="";
			String sec_ref_no="" , old_sec_ref_no="";
			String sec_type="", old_sec_type="";
			String status="" , old_status="";
			String gx="", old_gx = "";
			String cp="",old_cp="";
			String sec_value="" , old_sec_value="";
			String reversal="", sap_approval="";
			String old_reversal="", old_sap_approval="";
			String currency="" , old_currency="";
			String deal_no="" , old_deal_no="";
			String sec_category="" , old_sec_category="";
			
			if(!new_values.equals(""))
			{
				
				String name="",old_name="";
				String abbr="",old_abbr="";
				String deal_type="" , old_deal_type="";
				String fluctuation="" , old_fluctuation="";
				String variation="" , old_variation="";
				String iss_bank_cd="" , old_iss_bank_cd="";
				String iss_bank_nm="" , old_iss_bank_nm="";
				String iss_bank_ref="" , old_iss_bank_ref="";
				String adv_bank_cd="" , old_adv_bank_cd="";
				String adv_bank_nm="" , old_adv_bank_nm="";
				String adv_bank_ref="" , old_adv_bank_ref="";
				String conf_bank_cd="" , old_conf_bank_cd="";
				String conf_bank_nm="" , old_conf_bank_nm="";
				String conf_bank_ref="" , old_conf_bank_ref="";
				String received_dt="" , old_received_dt="";
				String review_dt="" , old_review_dt="";
				String iss_date="" , old_iss_date="";
				String expire_dt="" , old_expire_dt="";
				String tenor="" , old_tenor="";
				String remark="" , old_remark="";
				String guarantor_cd="" , old_guarantor_cd="";
				String guarantor_nm="" , old_guarantor_nm="";
				String deal_dtl="";
				
				String split_New_Value[] = new_values.split("#");
				for(int i=0; i<split_New_Value.length; i++)
				{
					if(split_New_Value[i].startsWith("CP="))
					{
						String temp[] = split_New_Value[i].split("CP=");
						if(temp.length>0)
						{
							cp=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("NAME="))
					{
						String temp[] = split_New_Value[i].split("NAME=");
						if(temp.length>0)
						{
							name=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ABBR="))
					{
						String temp[] = split_New_Value[i].split("ABBR=");
						if(temp.length>0)
						{
							abbr=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_TYPE="))
					{
						String temp[] = split_New_Value[i].split("SEC_TYPE=");
						if(temp.length>0)
						{
							sec_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_CATEGORY="))
					{
						String temp[] = split_New_Value[i].split("SEC_CATEGORY=");
						if(temp.length>0)
						{
							sec_category=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_TYPE="))
					{
						String temp[] = split_New_Value[i].split("DEAL_TYPE=");
						if(temp.length>0)
						{
							deal_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_NO="))
					{
						String temp[] = split_New_Value[i].split("DEAL_NO=");
						if(temp.length>0)
						{
							deal_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("VALUE="))
					{
						String temp[] = split_New_Value[i].split("VALUE=");
						if(temp.length>0)
						{
							sec_value=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CURRENCY="))
					{
						String temp[] = split_New_Value[i].split("CURRENCY=");
						if(temp.length>0)
						{
							currency=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("FLUCTUATION="))
					{
						String temp[] = split_New_Value[i].split("FLUCTUATION=");
						if(temp.length>0)
						{
							fluctuation=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("VARIATION="))
					{
						String temp[] = split_New_Value[i].split("VARIATION=");
						if(temp.length>0)
						{
							variation=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_CD=");
						if(temp.length>0)
						{
							iss_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_NAME="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_NAME=");
						if(temp.length>0)
						{
							iss_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISS_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("ISS_BANK_REF=");
						if(temp.length>0)
						{
							iss_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_CD=");
						if(temp.length>0)
						{
							adv_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_NAME"))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_NAME=");
						if(temp.length>0)
						{
							adv_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ADV_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("ADV_BANK_REF=");
						if(temp.length>0)
						{
							adv_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_CD="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_CD=");
						if(temp.length>0)
						{
							conf_bank_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_NAME="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_NAME=");
						if(temp.length>0)
						{
							conf_bank_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONF_BANK_REF="))
					{
						String temp[] = split_New_Value[i].split("CONF_BANK_REF=");
						if(temp.length>0)
						{
							conf_bank_ref=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("RECIEVED_DT="))
					{
						String temp[] = split_New_Value[i].split("RECIEVED_DT=");
						if(temp.length>0)
						{
							received_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REVIEW_DT="))
					{
						String temp[] = split_New_Value[i].split("REVIEW_DT=");
						if(temp.length>0)
						{
							review_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ISSUANCE_DT="))
					{
						String temp[] = split_New_Value[i].split("ISSUANCE_DT=");
						if(temp.length>0)
						{
							iss_date=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("EXPIRY_DT="))
					{
						String temp[] = split_New_Value[i].split("EXPIRY_DT=");
						if(temp.length>0)
						{
							expire_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("STATUS="))
					{
						String temp[] = split_New_Value[i].split("STATUS=");
						if(temp.length>0)
						{
							status=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("TENOR="))
					{
						String temp[] = split_New_Value[i].split("TENOR=");
						if(temp.length>0)
						{
							tenor=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REMARK="))
					{
						String temp[] = split_New_Value[i].split("REMARK=");
						if(temp.length>0)
						{
							remark=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GUARANTOR_CD="))
					{
						String temp[] = split_New_Value[i].split("GUARANTOR_CD=");
						if(temp.length>0)
						{
							guarantor_cd=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GUARANTOR_NM="))
					{
						String temp[] = split_New_Value[i].split("GUARANTOR_NM=");
						if(temp.length>0)
						{
							guarantor_nm=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SEC_REF_NO="))
					{
						String temp[] = split_New_Value[i].split("SEC_REF_NO=");
						if(temp.length>0)
						{
							sec_ref_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DEAL_DTL="))
					{
						String temp[] = split_New_Value[i].split("DEAL_DTL=");
						if(temp.length>0)
						{
							deal_dtl=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GX="))
					{
						String temp[] = split_New_Value[i].split("GX=");
						if(temp.length>0)
						{
							gx=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("REVERSAL="))
					{
						String temp[] = split_New_Value[i].split("REVERSAL=");
						if(temp.length>0)
						{
							reversal=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SAP_APPROVAL="))
					{
						String temp[] = split_New_Value[i].split("SAP_APPROVAL=");
						if(temp.length>0)
						{
							sap_approval=temp[1];
						}
					}
				}
				if(!old_values.equals(""))
				{
					String split_Old_Value[] = old_values.split("#");
					for(int i=0; i<split_Old_Value.length; i++)
					{
						if(split_Old_Value[i].startsWith("CP="))
						{
							String temp[] = split_Old_Value[i].split("CP=");
							if(temp.length>0)
							{
								old_cp=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("NAME="))
						{
							String temp[] = split_Old_Value[i].split("NAME=");
							if(temp.length>0)
							{
								old_name=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ABBR="))
						{
							String temp[] = split_Old_Value[i].split("ABBR=");
							if(temp.length>0)
							{
								old_abbr=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_TYPE="))
						{
							String temp[] = split_Old_Value[i].split("SEC_TYPE=");
							if(temp.length>0)
							{
								old_sec_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_CATEGORY="))
						{
							String temp[] = split_Old_Value[i].split("SEC_CATEGORY=");
							if(temp.length>0)
							{
								old_sec_category=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DEAL_TYPE="))
						{
							String temp[] = split_Old_Value[i].split("DEAL_TYPE=");
							if(temp.length>0)
							{
								old_deal_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DEAL_NO="))
						{
							String temp[] = split_Old_Value[i].split("DEAL_NO=");
							if(temp.length>0)
							{
								old_deal_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("VALUE="))
						{
							String temp[] = split_Old_Value[i].split("VALUE=");
							if(temp.length>0)
							{
								old_sec_value=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CURRENCY="))
						{
							String temp[] = split_Old_Value[i].split("CURRENCY=");
							if(temp.length>0)
							{
								old_currency=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("FLUCTUATION="))
						{
							String temp[] = split_Old_Value[i].split("FLUCTUATION=");
							if(temp.length>0)
							{
								old_fluctuation=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("VARIATION="))
						{
							String temp[] = split_Old_Value[i].split("VARIATION=");
							if(temp.length>0)
							{
								old_variation=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_CD=");
							if(temp.length>0)
							{
								old_iss_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_NAME="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_NAME=");
							if(temp.length>0)
							{
								old_iss_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISS_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("ISS_BANK_REF=");
							if(temp.length>0)
							{
								old_iss_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_CD=");
							if(temp.length>0)
							{
								old_adv_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_NAME"))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_NAME=");
							if(temp.length>0)
							{
								old_adv_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ADV_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("ADV_BANK_REF=");
							if(temp.length>0)
							{
								old_adv_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_CD="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_CD=");
							if(temp.length>0)
							{
								old_conf_bank_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_NAME="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_NAME=");
							if(temp.length>0)
							{
								old_conf_bank_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONF_BANK_REF="))
						{
							String temp[] = split_Old_Value[i].split("CONF_BANK_REF=");
							if(temp.length>0)
							{
								old_conf_bank_ref=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("RECIEVED_DT="))
						{
							String temp[] = split_Old_Value[i].split("RECIEVED_DT=");
							if(temp.length>0)
							{
								old_received_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REVIEW_DT="))
						{
							String temp[] = split_Old_Value[i].split("REVIEW_DT=");
							if(temp.length>0)
							{
								old_review_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ISSUANCE_DT="))
						{
							String temp[] = split_Old_Value[i].split("ISSUANCE_DT=");
							if(temp.length>0)
							{
								old_iss_date=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("EXPIRY_DT="))
						{
							String temp[] = split_Old_Value[i].split("EXPIRY_DT=");
							if(temp.length>0)
							{
								old_expire_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("STATUS="))
						{
							String temp[] = split_Old_Value[i].split("STATUS=");
							if(temp.length>0)
							{
								old_status=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("TENOR="))
						{
							String temp[] = split_Old_Value[i].split("TENOR=");
							if(temp.length>0)
							{
								old_tenor=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REMARK="))
						{
							String temp[] = split_Old_Value[i].split("REMARK=");
							if(temp.length>0)
							{
								old_remark=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GUARANTOR_CD="))
						{
							String temp[] = split_Old_Value[i].split("GUARANTOR_CD=");
							if(temp.length>0)
							{
								old_guarantor_cd=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GUARANTOR_NM="))
						{
							String temp[] = split_Old_Value[i].split("GUARANTOR_NM=");
							if(temp.length>0)
							{
								old_guarantor_nm=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SEC_REF_NO="))
						{
							String temp[] = split_Old_Value[i].split("SEC_REF_NO=");
							if(temp.length>0)
							{
								old_sec_ref_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GX="))
						{
							String temp[] = split_Old_Value[i].split("GX=");
							if(temp.length>0)
							{
								old_gx=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("REVERSAL="))
						{
							String temp[] = split_Old_Value[i].split("REVERSAL=");
							if(temp.length>0)
							{
								old_reversal=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SAP_APPROVAL="))
						{
							String temp[] = split_Old_Value[i].split("SAP_APPROVAL=");
							if(temp.length>0)
							{
								old_sap_approval=temp[1];
							}
						}
					}
				}
				
				
				if(status.equals("A"))
				{
					status="Pending for Amendment";
				}
				else if(status.equals("P"))
				{
					status="Pending";
				}
				else if(status.equals("O"))
				{
					status="In Order";
				}
				else if(status.equals("C"))
				{
					status="Cancelled";
				}
				else if(status.equals("R"))
				{
					status="Restated";
				}
				
				if(old_status.equals("A"))
				{
					old_status="Pending for Amendment";
				}
				else if(old_status.equals("P"))
				{
					old_status="Pending";
				}
				else if(old_status.equals("O"))
				{
					old_status="In Order";
				}
				else if(old_status.equals("C"))
				{
					old_status="Cancelled";
				}
				else if(old_status.equals("R"))
				{
					old_status="Restated";
				}
				
				if(sec_category.equals("R"))
				{
					sec_category="Incoming";
				}
				else if(sec_category.equals("I"))
				{
					sec_category="Outgoing";
				}
				
				if(old_sec_category.equals("R"))
				{
					old_sec_category="Incoming";
				}
				else if(old_sec_category.equals("I"))
				{
					old_sec_category="Outgoing";
				}
				
				if(currency.equals("1"))
				{
					currency="INR";
				}
				else if(currency.equals("2"))
				{
					currency="USD";
				}
				if(old_currency.equals("1"))
				{
					old_currency="INR";
				}
				else if(old_currency.equals("2"))
				{
					old_currency="USD";
				}
				
				if(!cp.equals(""))
				{
					
					if(old_values.equals("")) 
					{
						security_details="Counterparty Name : "+name+"<br>"
								+ "Abbreviation : "+abbr+"<br>"
								+ "Security Type : "+sec_type+"<br>"
								+ "Category : "+sec_category+"<br>"
								+ "Contract# : "+deal_no+"<br>"
								+ "Value : "+sec_value+"<br>"
								+ "Currency : "+currency+"<br>"
								+ "Recieved Date : "+received_dt+"<br>"
								+ "Status : "+status+"<br>"
								+ "Remark : "+remark;
					}
					else
					{
						if(!name.equals(old_name))
						{
							security_details+="Name : "+name+"<br>";
						}
						if(!abbr.equals(old_abbr))
						{
							security_details+="Abbreviation : "+abbr+"<br>";
						}
						if(!sec_type.equals(old_sec_type))
						{
							security_details+="Security Type : "+sec_type+"<br>";
						}
						if(!sec_category.equals(old_sec_category))
						{
							security_details+="Category : "+sec_category+"<br>";
						}
						if(!deal_type.equals(old_deal_type))
						{
							security_details+="Deal Type : "+deal_type+"<br>";
						}
						if(!deal_no.equals(old_deal_no))
						{
							security_details+="Contract# : "+deal_no+"<br>";
						}
						if(!sec_value.equals(old_sec_value))
						{
							security_details+="Value : "+sec_value+"<br>";
						}
						if(!currency.equals(old_currency))
						{
							security_details+="Currency : "+currency+"<br>";
						}
						if(!guarantor_nm.equals(old_guarantor_nm))
						{
							security_details+="Guarantor Name : "+guarantor_nm+"<br>";
						}
						if(!fluctuation.equals(old_fluctuation))
						{
							security_details+="Fluctuation : "+fluctuation+"<br>";
						}
						if(!variation.equals(old_variation))
						{
							security_details+="Variation : "+variation+"<br>";
						}
						if(!iss_bank_nm.equals(old_iss_bank_nm))
						{
							security_details+="Issuing Bank Name : "+iss_bank_nm+"<br>";
						}
						if(!iss_bank_ref.equals(old_iss_bank_ref))
						{
							security_details+="Issuing Bank Reference : "+iss_bank_ref+"<br>";
						}
						if(!adv_bank_nm.equals(old_adv_bank_nm))
						{
							security_details+="Advising Bank Name : "+adv_bank_nm+"<br>";
						}
						if(!adv_bank_ref.equals(old_adv_bank_ref))
						{
							security_details+="Advising Bank Reference  : "+adv_bank_ref+"<br>";
						}
						if(!conf_bank_nm.equals(old_conf_bank_nm))
						{
							security_details+="Confirming Bank Name : "+conf_bank_nm+"<br>";
						}
						if(!conf_bank_ref.equals(old_conf_bank_ref))
						{
							security_details+="Confirming Bank Reference : "+conf_bank_ref+"<br>";
						}
						if(!received_dt.equals(old_received_dt))
						{
							security_details+="Received Date : "+received_dt+"<br>";
						}
						if(!review_dt.equals(old_review_dt))
						{
							security_details+="Review Date : "+review_dt+"<br>";
						}
						if(!iss_date.equals(old_iss_date))
						{
							security_details+="Issuance Date : "+iss_date+"<br>";
						}
						if(!expire_dt.equals(old_expire_dt))
						{
							security_details+="Expire Date : "+expire_dt+"<br>";
						}
						if(!status.equals(old_status))
						{
							security_details+="Status : "+status+"<br>";
						}
						if(!tenor.equals(old_tenor))
						{
							security_details+="Tenor : "+tenor+"<br>";
						}
						if(!remark.equals(old_remark))
						{
							security_details+="Remark : "+remark+"<br>";
						}
					}
				}
			}
			if(!security_details.isEmpty() && sap_approval.equals("") && reversal.equals(""))
			{
				String subject="Security "+sec_ref_no+" for "+cp_name+" : ";
				if(operation.equals("INSERT"))
				{
					subject+= "Inserted Successfully!";
				}
				else
				{
					subject+= "Modified Successfully!";
				}
				
				mailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Security <b>"+sec_ref_no+" for "+cp_name+"</b> ";
						if(operation.equals("MODIFY"))
						{
							mailBody+= "<font style='background:#00cc00' color='white'>Modified</font> Successfully ";
						}
						else
						{
							mailBody+= "<font style='background:#00cc00' color='white'>Inserted</font> Successfully ";
						}
				mailBody+="by "+utilBean.getEmpName(dbcon,emp_cd)+" ";
				mailBody+="on "+utilDate.getSysdateWithTime24hr()+"";
				mailBody+= "</span><br><br>";
				
				if(!security_details.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Security Detail :<br></b>"+security_details+"</span><br><br>";
				}
				
				
				mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
						+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
						+ "</html>";
				
				String to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Collateral Audit Report", "Risk Mgmt", "NA", "On-Event");
				String cc_mail_list=utilBean.getCcMailReceipentList(dbcon,comp_cd,"Collateral Audit Report", "Risk Mgmt", "NA", "On-Event");
				
				if(!to_mail_list.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list, subject, mailBody, "", cc_mail_list, "");
				}
			}
			
			if(sec_type.equals("ADV") && status.equals("In Order") && gx.equals("I"))
			{
				String subject="";
				if(sap_approval.equals("") && reversal.equals("Y"))
				{
					subject="IGX Advance "+sec_ref_no+" - Reversed by Fin. Ops.!";
				}
				else if(sap_approval.equals("Y") && reversal.equals(""))
				{
					subject="IGX Advance "+sec_ref_no+" - Approved by Fin. Ops.!";
				}
				else if(sap_approval.equals("") && reversal.equals(""))
				{
					subject="IGX Advance "+sec_ref_no+" - Approved";
				}
				
				mailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients, <br>";
				mailBody+= "</span><br>";
				
				
				if(sap_approval.equals("") && reversal.equals("Y"))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance Reversal is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> by Fin. Ops. for SAP P80 Posting! ";
				}
				else if(sap_approval.equals("Y") && reversal.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> by Fin. Ops. for SAP P80 Posting! ";
				}
				else if(sap_approval.equals("") && reversal.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following IGX Advance is ";
					mailBody+= "<font style='background:#00cc00' color='white'>Approved</font> ";
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'> ! Please Process SAP Posting Approval!! ";
				}
				
				mailBody+="<br><br><br><table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";border-collapse: collapse;' border='2'>"
						+ "<tr><td><b>Counterparty</b>&nbsp;</td><td>&nbsp;"+utilBean.getGasExchangeName(dbcon,cp)+"&nbsp;</td></tr>"
						+ "<tr><td><b>Advance#</b>&nbsp;</td><td>&nbsp;"+sec_ref_no+"&nbsp;</td></tr>"
						+ "<tr><td><b>Value</b>&nbsp;</td><td>&nbsp;"+sec_value+"&nbsp;"+currency+"&nbsp;</td></tr>"
						+ "<tr><td><b>Contract#</b>&nbsp;</td><td>&nbsp;"+deal_no+"&nbsp;</td></tr>"
						+ "<tr><td><b>Category</b>&nbsp;</td><td>&nbsp;"+sec_category+"&nbsp;</td></tr>"
						+ "</table>";
				
				mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
						+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
						+ "</html>";
				
				String to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Advance Approval Notification", "Risk Mgmt", "NA", "On-Event");
				String cc_mail_list=utilBean.getCcMailReceipentList(dbcon,comp_cd,"Advance Approval Notification", "Risk Mgmt", "NA", "On-Event");
				
				if(!to_mail_list.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list, subject, mailBody, "", cc_mail_list, "");
				}
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Mail Delivery Failed!";

		}
	}
	
	private void InsertUpdateCreditExceedConfigDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateCreditExceedConfigDetail()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String cancel_flg = request.getParameter("cancel_flg")==null?"":request.getParameter("cancel_flg");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"0":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"0":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_start_dt=request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String cont_end_dt = request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String status="";
			if(cancel_flg.equals("Y"))
			{
				status=request.getParameter("status2")==null?"":request.getParameter("status2");
			}
			else
			{
				status=request.getParameter("status")==null?"":request.getParameter("status");
			}
			String credit_exceed_value=request.getParameter("credit_exceed_value")==null?"":request.getParameter("credit_exceed_value");
			String currency=request.getParameter("currency")==null?"":request.getParameter("currency");
			String mapping=counterparty_abbr+"-"+utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
			
			if(opration.equals("MODIFY"))
			{
				query="UPDATE FMS_CREDIT_EXCEED_DAYS SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
						+ "REMARK=?,STATUS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,VALUE=?,CURRENCY=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, from_dt);
				stmt.setString(2, to_dt);
				stmt.setString(3, remark);
				stmt.setString(4, status);
				stmt.setString(5, emp_cd);
				stmt.setString(6, credit_exceed_value);
				stmt.setString(7, currency);
				stmt.setString(8, comp_cd);
				stmt.setString(9, counterparty_cd);
				stmt.setString(10, agmt_no);
				stmt.setString(11, cont_no);
				stmt.setString(12, contract_type);
				stmt.setString(13, seq_no);
				stmt.executeUpdate();
				stmt.close();
				msg = "Successful! - "+mapping+" Credit Exceed Modified Successfully!";
				msg_type="S";
			}
			else
			{
				query="SELECT NVL(MAX(SEQ_NO),0) "
						+ "FROM FMS_CREDIT_EXCEED_DAYS "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset.close();
				stmt.close();
				
				query="INSERT INTO FMS_CREDIT_EXCEED_DAYS(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,"
						+ "FROM_DT,TO_DT,REMARK,STATUS,ENT_BY,ENT_DT,VALUE,CURRENCY) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?)";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, seq_no);
				stmt.setString(9, from_dt);
				stmt.setString(10, to_dt);
				stmt.setString(11, remark);
				stmt.setString(12, status);
				stmt.setString(13, emp_cd);
				stmt.setString(14, credit_exceed_value);
				stmt.setString(15, currency);
				stmt.executeUpdate();
				stmt.close();
				msg = "Successful! - "+mapping+" Credit Exceed Days Inserted Successfully!";
				msg_type="S";
			}
			
			url = "../credit_risk/frm_credit_exceed_days.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&start_dt="+cont_start_dt+"&end_dt="+cont_end_dt+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url;
			msg = "Error in Exception! Credit Exceed Days Insert/Update submission Failed!";
		}
	
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
}
