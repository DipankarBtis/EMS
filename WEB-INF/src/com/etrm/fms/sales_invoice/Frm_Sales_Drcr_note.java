package com.etrm.fms.sales_invoice;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_Sales_Drcr_note")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_Sales_Drcr_note extends HttpServlet
{
	static String db_src_file_name="Frm_Sales_Drcr_note.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Sales_Drcr_note";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	//private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
	private static String query4 = null;
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
	static DateUtil dateUtil = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	static NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		synchronized(this)
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
						
						if(option.equalsIgnoreCase("SET_DRCR_NOTE"))     //Deep20250820
						{
							SetDrCrNote(request);
						}														//Deep20250820
						else if(option.equalsIgnoreCase("DRCR_NOTE"))     
						{
							InsertUpdateDrCrNote(request);						//Deep20250829
						}	
						else if(option.equalsIgnoreCase("DRCR_NOTE_APPROVAL"))
						{
							InsertUpdateDrCrNoteApproval(request);			//Deep20250902
						}
						else if(option.equalsIgnoreCase("SEND_INVOICE_MAIL"))
						{
							SendInvoiceMail(request);   //Deep20250902
						}
						
					}
					
					dbcon.close();
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
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
			
			try {
			response.sendRedirect(url);
			}catch(IOException e) {
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}
	
	
	//Deep20250820
	private void SetDrCrNote(HttpServletRequest request) throws SQLException     
	{
		String function_nm="SetDrCrNote()";
		msg="";
		msg_type="";
		url="";
		
		try 
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String[] inv_no = request.getParameterValues("inv_no");
			String index = request.getParameter("index")==null?"":request.getParameter("index");
			String[] invoice_seq = request.getParameterValues("invoice_seq");
			String[] invoice_dt = request.getParameterValues("invoice_dt");
			String[] drcr_ref = request.getParameterValues("drcr_ref");
			String criteria[] = request.getParameterValues("criteriaDesc");
			String drcr_flag = request.getParameter("dr_cr_flag")==null?"":request.getParameter("dr_cr_flag");
			String[] remark = request.getParameterValues("remark");
			
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
		
			String inv_seq = null,fin_yr=null,cont_type=null;
				
			 int countRowSeq=0,count=0;
			
				if (inv_no[Integer.parseInt(index)].equals(drcr_ref[Integer.parseInt(index)])) 
				{
					query2 = "SELECT FINANCIAL_YEAR,CONTRACT_TYPE " 
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
					stmt = dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, inv_no[Integer.parseInt(index)]);
					rset = stmt.executeQuery();
					if (rset.next()) {
						fin_yr = rset.getString(1);
						cont_type = rset.getString(2);
					}
					rset.close();
					stmt.close();
				}
				else 
				{
					query2 = "SELECT FINANCIAL_YEAR,CONTRACT_TYPE " 
							+ "FROM FMS_SALES_DR_CR_MST "
							+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
					stmt = dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, inv_no[Integer.parseInt(index)]);
					rset = stmt.executeQuery();
					if (rset.next()) {
						fin_yr = rset.getString(1);
						cont_type = rset.getString(2);
					}
					rset.close();
					stmt.close();
				}
			 
			 
			String query = "SELECT COUNT(DR_CR_SEQ) "
					+ "FROM FMS_SALES_DR_CR_NOTE "
					+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, fin_yr);

			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(count==0)
			{
				inv_seq="1"; 
			}
			else 
			{
				query2="SELECT MAX(DR_CR_SEQ) "
						+ "FROM FMS_SALES_DR_CR_NOTE "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? ";
				stmt=dbcon.prepareStatement(query2);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_yr);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					inv_seq = ""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
			}
			
						
			
			countRowSeq=1;
			query1 = "INSERT INTO FMS_SALES_DR_CR_NOTE(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,DR_CR_REF_NO,"
					+ "CONTRACT_TYPE,CRITERIA,DR_CR_FLAG,REMARK,ENT_BY,ENT_DT,DR_CR_SEQ) VALUES(?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,"
					+ "SYSDATE,?) ";
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(countRowSeq++, comp_cd);
				stmt1.setString(countRowSeq++, counterparty_cd);
				stmt1.setString(countRowSeq++, fin_yr);
				stmt1.setString(countRowSeq++, invoice_seq[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, inv_no[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, invoice_dt[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, drcr_ref[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, cont_type);
				stmt1.setString(countRowSeq++, criteria[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, drcr_flag);
				stmt1.setString(countRowSeq++, remark[Integer.parseInt(index)]);
				stmt1.setString(countRowSeq++, emp_cd);
				stmt1.setString(countRowSeq++, inv_seq);
				stmt1.executeUpdate();
				stmt1.close();

				String invdtl="";
				if (drcr_flag.equals("DR")) 
				{
					if (cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X")) 
					{
						invdtl = "RLNG Debit Note on";
					} 
					else if (cont_type.equals("Q") || cont_type.equals("O")) {
						invdtl = "LTCORA(sell) Dredit Note on";
					} 
				}
				else 
				{
					if (cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X")) 
					{
						invdtl = "RLNG Credit Note on";
					} 
					else if (cont_type.equals("Q") || cont_type.equals("O")) 
					{
						invdtl = "LTCORA(sell) Credit Note on";
					} 
				}

				msg = "Successful! - "+invdtl+" "+inv_no[Integer.parseInt(index)]+" for selected Criteria Submitted!";
				msg_type="S";

				
				String filenm="frm_set_debit_credit_note.jsp";
				url = "../sales_invoice/"+filenm+"?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&billing_cycle="
						+billing_cycle+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
				dbcon.commit();	
				
		} 
		catch (Exception e) 
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - Set Debit/credit Note Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	//Deep20250820
	
	
	
	//Deep20250829
	private void InsertUpdateDrCrNote(HttpServletRequest request) throws SQLException 
	{
		
		String function_nm="InsertUpdateDrCrNote()";
		msg="";
		msg_type="";
		url="";
		
		try {
			double item_amt=0,item_diff_amt=0,item_diff_value = 0;
			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String financial_year = request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
	
			String criteria_desc = request.getParameter("criteria_desc")==null?"":request.getParameter("criteria_desc");
			String criteria = request.getParameter("criteria")==null?"":request.getParameter("criteria");

			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			
			String drcr_invoice_no = request.getParameter("drcr_invoice_no")==null?"":request.getParameter("drcr_invoice_no");
			String drcr_seq = request.getParameter("drcr_seq")==null?"":request.getParameter("drcr_seq");
			String drcr_invoice_dt = request.getParameter("drcr_invoice_dt")==null?"":request.getParameter("drcr_invoice_dt");
			String drcr_invoice_due_dt = request.getParameter("drcr_invoice_due_dt")==null?"":request.getParameter("drcr_invoice_due_dt");
			String drcr_ref = request.getParameter("drcr_ref")==null?"":request.getParameter("drcr_ref");
			String drcr_finyr = dateUtil.getFinancialYear(drcr_invoice_dt);
			String drcr_flag = request.getParameter("drcr_flag")==null?"":request.getParameter("drcr_flag");
			String drcr_gross_amt1 = request.getParameter("drcr_gross_amt1")==null?"":request.getParameter("drcr_gross_amt1");
			String drcr_tax_amt = request.getParameter("drcr_tax_amt")==null?"":request.getParameter("drcr_tax_amt");
			String drcr_net_payable = request.getParameter("drcr_net_payable")==null?"":request.getParameter("drcr_net_payable");
			String drcr_remark = request.getParameter("drcr_remark")==null?"":request.getParameter("drcr_remark");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String price = request.getParameter("price")==null?"":request.getParameter("price");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			
			String drcr_alloc_qty = request.getParameter("drcr_alloc_qty")==null?"":request.getParameter("drcr_alloc_qty");
			String drcr_price = request.getParameter("drcr_price")==null?"":request.getParameter("drcr_price");
			String drcr_exchng_rate = request.getParameter("drcr_exchng_rate")==null?"":request.getParameter("drcr_exchng_rate");
			
			
			
			String int_rate_type = "";
			String int_rate = "";
			String int_amt = "";
			
			String drcr_chk_flag = "";
			String drcr_chk_by = "";
			String drcr_chk_dt = "";
			
			String drcr_auth_flag = "";
			String drcr_auth_by = "";
			String drcr_auth_dt = "";
			
			String drcr_appr_flag = "";
			String drcr_appr_by = "";
			String drcr_appr_dt = "";
			String drcr_inv_id_seq = "";
			
			String drcr_pay_recv_amt = "";
			String drcr_payment_dt = "";
			
			String drcr_pay_remark = "";
			String drcr_pay_cnt = "";
			String drcr_pay_in_by = "";
			String drcr_pay_in_dt = "";
			String drcr_pay_up_by = "";
			String drcr_pay_up_dt = "";
			String drcr_pdf_inv_dtl = "";
			
			String drcr_pint_by_ori = "";
			String drcr_pint_dt_ori = "";
			String drcr_pint_by_dub = "";
			String drcr_pint_dt_dub = "";
			String drcr_pint_by_tri = "";
			String drcr_pint_dt_tri = "";
			String drcr_sun_flag = "";
			String drcr_sun_by = "";
			String drcr_sun_dt = "";
		
//			int count=0;
//			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
//			String exchange_rate = request.getParameter("exchange_rate")==null?"":request.getParameter("exchange_rate");
//			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
//			String adjust_amt = request.getParameter("adjust_amt")==null?"":request.getParameter("adjust_amt");
//			String invoice_raised_in  = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
//			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
//
//
//			String temp_tax_cd = request.getParameter("temp_tax_cd")==null?"":request.getParameter("temp_tax_cd");
//			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
//			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
//			
//			String[] sub_tax_struct = request.getParameterValues("drcr_sub_tax_struct");
//			String[] sub_tax_amt = request.getParameterValues("drcr_sub_tax_amt");
//			String[] sub_tax_code = request.getParameterValues("drcr_sub_tax_code");
//			String[] sub_tax_base_amt = request.getParameterValues("drcr_sub_tax_base_amt");
			
			
			
			if(opration.equals("INSERT")) {
				
//				String query = "SELECT COUNT(INVOICE_NO) "
//							+ "FROM FMS_SALES_DR_CR_MST "
//							+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
//							+ "AND INVOICE_NO = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ?";
//						stmt=dbcon.prepareStatement(query);
//						stmt.setString(1, comp_cd);
//						stmt.setString(2, counterparty_cd);
//						stmt.setString(3, invoice_no);
//						stmt.setString(4, invoice_seq);
//						stmt.setString(5, financial_year);
//						rset=stmt.executeQuery();
//						if(rset.next())
//						{
//							count=rset.getInt(1);
//						}
//						rset.close();
//						stmt.close();
//
//						if(count==0)
//						{
//							drcr_invoice_seq="1"; 
//						}
//						else 
//						{
//							query2="SELECT MAX(DR_CR_SEQ) "
//									+ "FROM FMS_SALES_DR_CR_MST "
//									+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
//									+ "AND INVOICE_NO = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ?";
//							stmt=dbcon.prepareStatement(query2);
//							stmt.setString(1, comp_cd);
//							stmt.setString(2, counterparty_cd);
//							stmt.setString(3, invoice_no);
//							stmt.setString(4, invoice_seq);
//							stmt.setString(5, financial_year);
//							rset=stmt.executeQuery();
//							if(rset.next())
//							{
//								drcr_invoice_seq = ""+(rset.getInt(1)+1);
//							}
//							rset.close();
//							stmt.close();
//						}
				
				query1  = "INSERT INTO FMS_SALES_DR_CR_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,FREQ,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INV_DUE_DT,CRITERIA,DR_CR_SEQ,DR_CR_FLAG,DR_CR_NO,"
						+ "DR_CR_REF_NO,DR_CR_FIN_YR,DR_CR_DT,DR_CR_DUE_DT,PERIOD_START_DT,PERIOD_END_DT,TOTAL_GROSS,TOTAL_TAX,TOTAL_AMT,INT_RATE_TYPE,INT_RATE,"
						+ "INT_AMT,REMARK,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,"
						+ "APPROVED_DT,ENT_BY,ENT_DT,INVOICE_ID_SEQ,PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_CNT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_INSERT_BY,PAY_INSERT_DT,"
						+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,CARGO_NO,INV_FLAG) "
						+ "VALUES(?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,"
						+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, financial_year);				
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, cont_rev);
				stmt1.setString(8, contract_type);
				stmt1.setString(9, bu_unit);
				stmt1.setString(10, bu_state_tin);
				stmt1.setString(11, bu_contact_person);
				stmt1.setString(12, plant_seq);
				stmt1.setString(13, contact_person);
				stmt1.setString(14, billing_cycle);
				stmt1.setString(15, invoice_seq);
				stmt1.setString(16, invoice_no);
				stmt1.setString(17, invoice_dt);
				stmt1.setString(18, invoice_due_dt);
				stmt1.setString(19, criteria);
				stmt1.setString(20, drcr_seq);
				stmt1.setString(21, drcr_flag);
				stmt1.setString(22, drcr_invoice_no);
				stmt1.setString(23, drcr_ref);
				stmt1.setString(24, drcr_finyr);
				stmt1.setString(25, drcr_invoice_dt);
				stmt1.setString(26, drcr_invoice_due_dt);
				stmt1.setString(27, period_start_dt);
				stmt1.setString(28, period_end_dt);
				stmt1.setString(29, drcr_gross_amt1);
				stmt1.setString(30, drcr_tax_amt);
				stmt1.setString(31, drcr_net_payable);
				stmt1.setString(32, int_rate_type);
				stmt1.setString(33, int_rate);
				stmt1.setString(34, int_amt);
				stmt1.setString(35, drcr_remark);
				stmt1.setString(36, drcr_chk_flag);
				stmt1.setString(37, drcr_chk_by);
				stmt1.setString(38, drcr_chk_dt);
				stmt1.setString(39, drcr_auth_flag);
				stmt1.setString(40, drcr_auth_by);
				stmt1.setString(41, drcr_auth_dt);
				stmt1.setString(42, drcr_appr_flag);
				stmt1.setString(43, drcr_appr_by);
				stmt1.setString(44, drcr_appr_dt);
				stmt1.setString(45, emp_cd);
				stmt1.setString(46, drcr_inv_id_seq);
				stmt1.setString(47, drcr_pay_recv_amt);
				stmt1.setString(48, drcr_payment_dt);
				stmt1.setString(49, drcr_pay_remark);
				stmt1.setString(50, drcr_pay_cnt);
				stmt1.setString(51, drcr_pay_up_by);
				stmt1.setString(52, drcr_pay_up_dt);
				stmt1.setString(53, drcr_pay_in_by);
				stmt1.setString(54, drcr_pay_in_dt);
				stmt1.setString(55, drcr_pdf_inv_dtl);
				stmt1.setString(56, drcr_pint_by_ori);
				stmt1.setString(57, drcr_pint_dt_ori);
				stmt1.setString(58, drcr_pint_by_dub);
				stmt1.setString(59, drcr_pint_dt_dub);
				stmt1.setString(60, drcr_pint_by_tri);
				stmt1.setString(61, drcr_pint_dt_tri);
				stmt1.setString(62, drcr_sun_flag);
				stmt1.setString(63, drcr_sun_by);
				stmt1.setString(64, drcr_sun_dt);
				stmt1.setString(65, cargo_no);
				stmt1.setString(66, inv_flag);
				stmt1.executeUpdate();
				stmt1.close();
				
				
				msg = "Successful! - Debit/Credit Note for "+counterparty_abbr+" On Invoice "+invoice_no+" Generated!";
				msg_type="S";

				if(criteria != null)
				{
					if(criteria.equals("1")) {
						if (drcr_flag.equals("DR")) 
						{
							item_amt = Double.parseDouble(exchng_rate);
							item_diff_amt = Double.parseDouble(drcr_exchng_rate);
							item_diff_value = item_diff_amt - item_amt;
						}
						else
						{
							item_amt = Double.parseDouble(exchng_rate);
							item_diff_amt = Double.parseDouble(drcr_exchng_rate);
							item_diff_value =item_amt -  item_diff_amt;
						}
					 }
					 else if(criteria.equals("2")) {
						if (drcr_flag.equals("DR")) 
						{
							item_amt = Double.parseDouble(price);
							item_diff_amt = Double.parseDouble(drcr_price);
							item_diff_value = item_diff_amt - item_amt;
						}
						else 
						{
							item_amt = Double.parseDouble(price);
							item_diff_amt = Double.parseDouble(drcr_price);
							item_diff_value =item_amt -  item_diff_amt;
						}
					 }
					 else if(criteria.equals("3")) {
						 if (drcr_flag.equals("DR")) 
						 {
							item_amt = Double.parseDouble(alloc_qty);
							item_diff_amt = Double.parseDouble(drcr_alloc_qty);
							item_diff_value = item_diff_amt - item_amt;
						}
						else 
						{
							item_amt = Double.parseDouble(price);
							item_diff_amt = Double.parseDouble(drcr_price);
							item_diff_value =item_amt -  item_diff_amt;
						}
					 }
					
//					if(item_diff_value.tostr.contains())
					
						int cnt = 0;
						query2="INSERT INTO FMS_SALES_DR_CR_DTL(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "BU_UNIT,PLANT_SEQ,INVOICE_SEQ,INVOICE_NO,INVOICE_ID_SEQ,DR_CR_REF_NO,DR_CR_FLAG,DR_CR_SEQ,DR_CR_NO,CRITERIA,ITEM_DESCRIPTION,"
								+ "LINE_SEQ,ITEM_CD,ITEM_EFF_DT,ITEM_AMT,ITEM_DIFF_AMT,ITEM_DIFF_VALUE) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,SYSDATE,?,?,?)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, financial_year);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, cont_rev);
						stmt2.setString(8, contract_type);
						stmt2.setString(9, bu_unit);
						stmt2.setString(10, plant_seq);
						stmt2.setString(11, invoice_seq);
						stmt2.setString(12, invoice_no);
						stmt2.setString(13, drcr_inv_id_seq);
						stmt2.setString(14, drcr_ref);
						stmt2.setString(15, drcr_flag);
						stmt2.setString(16, drcr_seq);
						stmt2.setString(17, drcr_invoice_no);
						stmt2.setString(18, criteria);
						stmt2.setString(19, criteria_desc);
						stmt2.setInt(20, ++cnt);
						stmt2.setString(21, criteria);
						stmt2.setDouble(22, item_amt);
						stmt2.setDouble(23, item_diff_amt);
						stmt2.setDouble(24, item_diff_value);
						stmt2.executeUpdate();
						
						stmt2.close();
				}
			}	
			
			String filenm="frm_generate_debit_credit_note.jsp";
			url = "../sales_invoice/"+filenm+"?counterparty_cd="+counterparty_cd+"&billing_cycle="
					+billing_cycle+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
			
			
		}
		catch(Exception e) {
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - Debit/credit Note Generation Failed";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}	
	}

	private void InsertUpdateDrCrNoteApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDrCrNoteApproval()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String activityFlag = request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String drcr_invoice_no = request.getParameter("drcr_invoice_no")==null?"":request.getParameter("drcr_invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String drcr_flag = request.getParameter("drcr_flag")==null?"":request.getParameter("drcr_flag");
			String drcr_seq = request.getParameter("drcr_seq")==null?"":request.getParameter("drcr_seq");
			
			String type ="";
			
			
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no = deal_no;
			String invdtl="Sales";
			if(inv_flag.equals("UG"))
			{
				invdtl="LTCORA(Sell) SUG";
			}
			else if(inv_flag.equals("ST"))
			{
				invdtl="LTCORA(Sell) STORAGE";
			}
			else if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				invdtl="LTCORA(Sell)";
			}
			
			if(activityFlag.equals("CHECK"))
			{
				String query="UPDATE FMS_SALES_DR_CR_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? AND DR_CR_FLAG =? AND DR_CR_SEQ =?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, counterparty_cd);
				stmt.setString(5, cont_no);
				stmt.setString(6, agmt_no);
				stmt.setString(7, plant_seq);
				stmt.setString(8, bu_unit);
				stmt.setString(9, contract_type);
				stmt.setString(10, bu_state_tin);
				stmt.setString(11, period_start_dt);
				stmt.setString(12, period_end_dt);
				stmt.setString(13, financial_year);
				stmt.setString(14, invoice_seq);
				stmt.setString(15, cargo_no);
				stmt.setString(16, inv_flag);
				stmt.setString(17, drcr_flag);
				stmt.setString(18, drcr_seq);
				
				stmt.executeUpdate();
				
				stmt.close();
				
				if(drcr_flag.equals("DR")) {
					type = "Debit Note";
				}
				else {
					type = "Credit Note";
				}
				
				msg = "Successful! - "+invdtl+" "+type+" for "+counterparty_abbr+" "+temp_deal_no+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, deal_no, drcr_invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl);
			}
			else if(activityFlag.equals("APPROVE"))
			{
				int count_inv_id_seq=0;
				String contType="";
				String invSeries="";
				if(contract_type.equals("Q") || contract_type.equals("O"))
				{
					contType="'Q','O'";
					invSeries="L";
				}
				else
				{
					contType="'S','L','X'";
					invSeries="S";
				}
				String query="SELECT COUNT(*) "
						+ "FROM FMS_SALES_DR_CR_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") ";
				String temp_query=query;
				stmt=dbcon.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_id_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					query1="UPDATE FMS_SALES_DR_CR_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
							+ "DR_CR_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? AND DR_CR_FLAG =? AND DR_CR_SEQ =? ";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, drcr_invoice_no);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, counterparty_cd);
					stmt.setString(7, cont_no);
					stmt.setString(8, agmt_no);
					stmt.setString(9, plant_seq);
					stmt.setString(10, bu_unit);
					stmt.setString(11, contract_type);
					stmt.setString(12, bu_state_tin);
					stmt.setString(13, period_start_dt);
					stmt.setString(14, period_end_dt);
					stmt.setString(15, financial_year);
					stmt.setString(16, invoice_seq);
					stmt.setString(17, cargo_no);
					stmt.setString(18, inv_flag);
					stmt.setString(19, drcr_flag);
					stmt.setString(20, drcr_seq);
					stmt.executeUpdate();
					stmt.close();
					
					query1="UPDATE FMS_SALES_DR_CR_DTL SET DR_CR_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND DR_CR_FLAG =? AND DR_CR_SEQ =? ";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, drcr_invoice_no);
					stmt.setString(2, invoice_id_seq);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, cont_no);
					stmt.setString(6, agmt_no);
					stmt.setString(7, plant_seq);
					stmt.setString(8, bu_unit);
					stmt.setString(9, contract_type);
					stmt.setString(10, invoice_seq);
					stmt.setString(11, drcr_flag);
					stmt.setString(12, drcr_seq);
					stmt.executeUpdate();
					stmt.close();
					
					
					if(drcr_flag.equals("DR")) {
						type = "Debit Note";
					}
					else {
						type = "Credit Note";
					}
					
					msg = "Successful! - "+invdtl+" "+type+" for "+counterparty_abbr+" "+temp_deal_no+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
					
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, drcr_invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl);
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice "+drcr_invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../sales_invoice/rpt_drcr_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&cargo_no="+cargo_no+"&accroid="+accroid+
					"&inv_flag="+inv_flag+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Sales Invoice Approval Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	
	private String InvoiceMailBody(String company_cd,String customer,String cont_no,String inv_no,String inv_period,String due_dt,String inv_amount,
			String activity_type,String flag,String inv_type,String contract_ref,String subject_line,String invoice_raised_in, String invoice_dt,String invdtl) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
			String inv_nm="";
            if(inv_type.equals("CR"))
			{
            	//inv_nm="Credit Note";
            	inv_nm=subject_line;
			}
			else if(inv_type.equals("DR"))
			{
				//inv_nm="Debit Note";
				inv_nm=subject_line;
			}
			else if(inv_type.equals("CCR"))
			{
            	//inv_nm="Credit Note";
            	inv_nm=subject_line;
			}
			else if(inv_type.equals("CDR"))
			{
				//inv_nm="Debit Note";
				inv_nm=subject_line;
			}
			else if(inv_type.equals("LP"))
			{
				//inv_nm="Late Payment Invoice";
				inv_nm=subject_line;
			}
			else if(inv_type.equals("OR"))
			{
				//inv_nm="Other";
				inv_nm=subject_line;
			}
			else if(inv_type.equals("S"))
			{
				//inv_nm="Other";
				inv_nm=subject_line;
			}
			else
			{
				//inv_nm="Sell";
				inv_nm=invdtl;
			}
            
            String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
			String mail_subject=company_abbr+"/"+customer+"("+cont_no+") "+inv_nm+" Invoice ("+inv_period+")";
			if(!inv_no.equals(""))
			{
				mail_subject+=" "+inv_no;
			}
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			if(activity_type.equals("APPROVE"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Invoice is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Fin Ops Finalized</font>, You may proceed for PDF generation!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalized!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Invoice is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while Fin Ops Finalization!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalization Rejected!";
				}
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Invoice is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Checked</font>, Please start Fin Ops Finalization Process!</span><br><br>";
					
					mail_subject+=" IRP Checked OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Invoice is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Invoice "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Customer</b>&nbsp;</td><td>&nbsp;"+customer+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+cont_no+"&nbsp;["+contract_ref+"]&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Period</b>&nbsp;</td><td>&nbsp;"+inv_period+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "</table>"
					+ "<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";
			
			String to_mail_list="";
			String cc_mail_list="";
			
			if(activity_type.equals("APPROVE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Sell", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Sell", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Sell", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Sell", "NA", "On-Event");
				}
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Sell", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Sell", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Sell", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Sell", "NA", "On-Event");
			}
			
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_list, mail_subject, mailBody, "", cc_mail_list, "");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
		
		return mailBody;
	}
	
	private void SendInvoiceMail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="SendInvoiceMail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			
			String[] email_from = request.getParameterValues("email_from");
			String[] email_to = request.getParameterValues("email_to");
			String[] email_cc = request.getParameterValues("email_cc");
			String[] email_bcc = request.getParameterValues("email_bcc");
			String[] subject = request.getParameterValues("subject");
			String[] index = request.getParameterValues("index");
			String[] email_body = request.getParameterValues("email_body");
			
			if(email_to != null)
			{
				for(int i=0;i<email_to.length;i++)
				{
					email_body[i]=email_body[i].replaceAll("\n", "<br>");
					email_body[i]="<html>"
							+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body[i]+"</span>"
							+ "</html>";
					
					if(!email_to[i].equals("") && !email_body[i].equals(""))
					{
						String[] attachment = request.getParameterValues("attachment"+index[i]);
						if(attachment != null)
						{
							mailDelv.sendMailWithMultipleAttachment(comp_cd,email_to[i], subject[i], email_body[i], attachment, email_cc[i], email_bcc[i]);
						}
					}
					
					msg="Mail Sent for "+subject[i];
					msg_type="S";
					
					try
					{
						new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
					}
					catch(Exception infoLogger)
					{
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
					}
				}
			}
			
			url = "../sales_invoice/frm_drcr_sign_pdf_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Send Invoice Mail Failed";
			
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	private String extractFileName(Part part) 
    {		
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        //System.out.println("items*******"+items.length);
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
           	//System.out.println("s*****"+s);
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }       
        }
        return filenm;
    }
}
