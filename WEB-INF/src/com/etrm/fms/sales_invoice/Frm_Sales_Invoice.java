package com.etrm.fms.sales_invoice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

@WebServlet("/servlet/Frm_Sales_Invoice")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_Sales_Invoice extends HttpServlet
{
	static String db_src_file_name="Frm_Sales_Invoice.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Sales_Invoice";
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
						
						if(option.equalsIgnoreCase("SALES_INVOICE"))
						{
							InsertUpdateSalesInvoiceDetail(request);
						}
						else if(option.equalsIgnoreCase("SALES_INVOICE_APPROVAL"))
						{
							InsertUpdateSalesInvoiceApproval(request);
						}
						else if(option.equalsIgnoreCase("F_FLOW_INVOICE"))
						{
							InsertUpdateFFlowInvoiceDetail(request);
						}
						else if(option.equalsIgnoreCase("F_FLOW_INVOICE_APPROVAL"))
						{
							InsertUpdateFFlowSalesInvoiceApproval(request);
						}
						else if(option.equalsIgnoreCase("SEND_INVOICE_MAIL"))
						{
							SendInvoiceMail(request);
						}
						else if(option.equalsIgnoreCase("DELETE_F_FLOW_ANNEXURE"))
						{
							DeleteFFlowAnnexure(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_CR_DR"))
						{
							InsertUpdateCreditDebitDetail(request);
						}
						else if(option.equalsIgnoreCase("CRDR_INVOICE_APPROVAL"))
						{
							InsertUpdateCrDrInvoiceApproval(request);
						}
						else if(option.equalsIgnoreCase("LATE_PAY_INVOICE"))
						{
							InsertUpdateLpInvoiceDetail(request);
						}
						else if(option.equalsIgnoreCase("LP_INVOICE_APPROVAL"))
						{
							InsertUpdateLpInvoiceApproval(request);
						}
						else if(option.equalsIgnoreCase("DELETE_CRDR_ANNEXURE"))
						{
							DeleteCRDRAnnexure(request);
						}
						else if(option.equalsIgnoreCase("SALES_INVOICE_CHANGE_ACTION"))
						{
							InsertUpdateSalesInvoiceChangeAction(request);
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
	
	private void InsertUpdateSalesInvoiceDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSalesInvoiceDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
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
			String temp_period_start_dt = request.getParameter("temp_period_start_dt")==null?"":request.getParameter("temp_period_start_dt");
			String temp_period_end_dt = request.getParameter("temp_period_end_dt")==null?"":request.getParameter("temp_period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String exist_financial_year = request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String price = request.getParameter("price")==null?"":request.getParameter("price");
			String price_cd = request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exchng_cd = request.getParameter("exchng_cd")==null?"":request.getParameter("exchng_cd");
			String exchng_dt = request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			String gross_amt1 = request.getParameter("gross_amt1")==null?"":request.getParameter("gross_amt1");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String exchg_rate_type = request.getParameter("exchg_rate_type")==null?"":request.getParameter("exchg_rate_type");
			String tcs_tds = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			String tcs_amt = request.getParameter("tcs_amt")==null?"":request.getParameter("tcs_amt");
			String tcs_factor = request.getParameter("tcs_factor")==null?"":request.getParameter("tcs_factor");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			String sug_qty = request.getParameter("sug_qty")==null?"":request.getParameter("sug_qty");
			String sug_percentage = request.getParameter("sug_percent")==null?"":request.getParameter("sug_percent");
			
			String tcs_struct_cd = request.getParameter("tcs_struct_cd")==null?"":request.getParameter("tcs_struct_cd");
			String tcs_struct_dt = request.getParameter("tcs_struct_dt")==null?"":request.getParameter("tcs_struct_dt");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_factor = request.getParameter("tds_factor")==null?"":request.getParameter("tds_factor");
			String tds_struct_cd = request.getParameter("tds_struct_cd")==null?"":request.getParameter("tds_struct_cd");
			String tds_struct_dt = request.getParameter("tds_struct_dt")==null?"":request.getParameter("tds_struct_dt");
			
			String transportation_amount = request.getParameter("transportation_amount")==null?"":request.getParameter("transportation_amount");
			String transportation_tariff = request.getParameter("transportation_tariff")==null?"":request.getParameter("transportation_tariff");
			String marketing_margin_amount = request.getParameter("marketing_margin_amount")==null?"":request.getParameter("marketing_margin_amount");
			String marketing_margin = request.getParameter("marketing_margin")==null?"":request.getParameter("marketing_margin");
			String other_charges_amount = request.getParameter("other_charges_amount")==null?"":request.getParameter("other_charges_amount");
			String other_charges = request.getParameter("other_charges")==null?"":request.getParameter("other_charges");
			
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

			String[] allocation_dt = request.getParameterValues("allocation_dt");
			String[] dailyExchngRate = request.getParameterValues("dailyExchngRate");
			String[] salesPrice = request.getParameterValues("salesPrice");
			String[] salesPriceCd = request.getParameterValues("salesPriceCd");
			String[] allocation_qty = request.getParameterValues("allocation_qty");
			String[] amount_usd = request.getParameterValues("amount_usd");
			String[] amount_inr = request.getParameterValues("amount_inr");
			
			String[] sub_tax_struct = request.getParameterValues("sub_tax_struct");
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			String[] disc_day_flag = request.getParameterValues("disc_day_flag");
			String[] storage_inventory = request.getParameterValues("storage_inventory");
			String[] storage_charge = request.getParameterValues("storage_charge");
			String[] user_define = request.getParameterValues("user_define");
			String[] storage_dt = request.getParameterValues("storage_dt");
			String[] offtake_qty = request.getParameterValues("offtake_qty");
			String rate_type = request.getParameter("rate_type")==null?"":request.getParameter("rate_type");
			
			String advance_adj_flag = request.getParameter("advance_adj_flag")==null?"":request.getParameter("advance_adj_flag");
			String adjAdvFlag = request.getParameter("adjAdvFlag")==null?"":request.getParameter("adjAdvFlag");
			String[] receipt_voucher = request.getParameterValues("receipt_voucher");
			String[] gross_adjust = request.getParameterValues("gross_adjust");
			
			String new_invoice_seq=invoice_seq;
			
			boolean isOperated=false;
			if(opration.equals("INSERT"))
			{
				int count=0;
				String query="SELECT COUNT(*) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND PLANT_SEQ=? AND BU_UNIT=? AND BU_STATE_TIN=? AND CARGO_NO=? "
						+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, plant_seq);
				stmt.setString(7, bu_unit);
				stmt.setString(8, bu_state_tin);
				stmt.setString(9, cargo_no);
				stmt.setString(10, period_start_dt);
				stmt.setString(11, period_end_dt);
				stmt.setString(12, inv_flag);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					String inv_seq="1";
					query2="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=?";
					stmt=dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						inv_seq = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();

					invoice_seq=inv_seq;
					new_invoice_seq=invoice_seq;
					
					query1="INSERT INTO FMS_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
							+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_DT,"
							+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
							+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
							+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
							+ "INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,REMARK_1,REMARK_2,EXCHG_RATE_TYPE,"
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,INVOICE_NO,INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
							+ "TCS_STRUCT_CD,TCS_EFF_DT,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TDS_EFF_DT,"
							+ "MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,CARGO_NO,INV_FLAG,SUG_QTY,SUG_PERCENT) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,SYSDATE,?,?,?,?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, bu_unit);
					stmt1.setString(9, bu_contact_person);
					stmt1.setString(10, bu_state_tin);
					stmt1.setString(11, plant_seq);
					stmt1.setString(12, contact_person);
					stmt1.setString(13, invoice_seq);
					stmt1.setString(14, invoice_dt);
					stmt1.setString(15, billing_cycle);
					stmt1.setString(16, period_start_dt);
					stmt1.setString(17, period_end_dt);
					stmt1.setString(18, invoice_due_dt);
					stmt1.setString(19, alloc_qty);
					stmt1.setString(20, price);
					stmt1.setString(21, price_cd);
					stmt1.setString(22, gross_amt);
					stmt1.setString(23, exchng_cd);
					stmt1.setString(24, exchng_dt);
					stmt1.setString(25, exchng_rate);
					stmt1.setString(26, invoice_raised_in);
					stmt1.setString(27, gross_amt1);
					stmt1.setString(28, tax_amt);
					stmt1.setString(29, tax_cd);
					stmt1.setString(30, tax_dt);
					stmt1.setString(31, invoice_amt);
					stmt1.setString(32, net_payable);
					stmt1.setString(33, emp_cd);
					stmt1.setString(34, financial_year);
					stmt1.setString(35, remark1);
					stmt1.setString(36, remark2);
					stmt1.setString(37, exchg_rate_type);
					stmt1.setString(38, tcs_tds);
					stmt1.setString(39, tcs_amt);
					stmt1.setString(40, tcs_factor);
					stmt1.setString(41, invoice_no);
					stmt1.setString(42, invoice_id_seq);
					stmt1.setString(43, transportation_tariff);
					stmt1.setString(44, transportation_amount);
					stmt1.setString(45, tcs_struct_cd);
					stmt1.setString(46, tcs_struct_dt);
					stmt1.setString(47, tds_amt);
					stmt1.setString(48, tds_factor);
					stmt1.setString(49, tds_struct_cd);
					stmt1.setString(50, tds_struct_dt);
					stmt1.setString(51, marketing_margin);
					stmt1.setString(52, marketing_margin_amount);
					stmt1.setString(53, other_charges);
					stmt1.setString(54, other_charges_amount);
					stmt1.setString(55, cargo_no);
					stmt1.setString(56, inv_flag);
					stmt1.setString(57, sug_qty);
					stmt1.setString(58, sug_percentage);
					stmt1.executeUpdate();
					isOperated=true;
					
					stmt1.close();
					msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query2="SELECT COUNT(*) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? "
						//+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CARGO_NO=? "
						+ "AND INV_FLAG=? ";
				stmt=dbcon.prepareStatement(query2);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, plant_seq);
				stmt.setString(6, bu_unit);
				stmt.setString(7, contract_type);
				stmt.setString(8, bu_state_tin);
				//stmt.setString(9, period_start_dt);
				//stmt.setString(10, period_end_dt);
				stmt.setString(9, invoice_seq);
				//stmt.setString(12, financial_year);
				stmt.setString(10, exist_financial_year);
				stmt.setString(11, cargo_no);
				stmt.setString(12, inv_flag);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
		
				if(!financial_year.equals(exist_financial_year) && !financial_year.equals("") && !exist_financial_year.equals(""))
				{
					String inv_seq="1";
					query2="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=?";
					stmt=dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						inv_seq = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					new_invoice_seq=inv_seq;
				}
				
				if(count > 0)
				{
					query1="UPDATE FMS_INVOICE_MST SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, "
							+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), FREQ=?, DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
							+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
							+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
							+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, "
							+ "NET_PAYABLE_AMT=?,FINANCIAL_YEAR=?,REMARK_1=?,REMARK_2=?,EXCHG_RATE_TYPE=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,TCS_TDS=?,TCS_AMT=?,TCS_FACTOR=?,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=?,TRANSPORTATION_CHARGE=?,"
							+ "TRANSPORTATION_AMOUNT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TDS_GROSS_AMT=?,TDS_GROSS_PERCENT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,INVOICE_SEQ=?,SUG_QTY=?,SUG_PERCENT=? ";
					if(inv_flag.equals("ST"))
					{
						query1+= ",PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
					}
					query1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? ";
					if(!inv_flag.equals("ST"))
					{
						query1+= "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
					}
					int stcount=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, bu_contact_person);
					stmt1.setString(++stcount, contact_person);
					stmt1.setString(++stcount, invoice_dt);
					stmt1.setString(++stcount, billing_cycle);
					stmt1.setString(++stcount, invoice_due_dt);
					stmt1.setString(++stcount, alloc_qty);
					stmt1.setString(++stcount, price);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, gross_amt);
					stmt1.setString(++stcount, exchng_cd);
					stmt1.setString(++stcount, exchng_dt);
					stmt1.setString(++stcount, exchng_rate);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, gross_amt1);
					stmt1.setString(++stcount, tax_amt);
					stmt1.setString(++stcount, tax_cd);
					stmt1.setString(++stcount, tax_dt);
					stmt1.setString(++stcount, invoice_amt);
					stmt1.setString(++stcount, net_payable);
					stmt1.setString(++stcount, financial_year);
					stmt1.setString(++stcount, remark1);
					stmt1.setString(++stcount, remark2);
					stmt1.setString(++stcount, exchg_rate_type);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, tcs_tds);
					stmt1.setString(++stcount, tcs_amt);
					stmt1.setString(++stcount, tcs_factor);
					stmt1.setString(++stcount, invoice_no);
					stmt1.setString(++stcount, invoice_id_seq);
					stmt1.setString(++stcount, transportation_tariff);
					stmt1.setString(++stcount, transportation_amount);
					stmt1.setString(++stcount, tcs_struct_cd);
					stmt1.setString(++stcount, tcs_struct_dt);
					stmt1.setString(++stcount, tds_amt);
					stmt1.setString(++stcount, tds_factor);
					stmt1.setString(++stcount, tds_struct_cd);
					stmt1.setString(++stcount, tds_struct_dt);
					stmt1.setString(++stcount, marketing_margin);
					stmt1.setString(++stcount, marketing_margin_amount);
					stmt1.setString(++stcount, other_charges);
					stmt1.setString(++stcount, other_charges_amount);
					stmt1.setString(++stcount, new_invoice_seq);
					stmt1.setString(++stcount, sug_qty);
					stmt1.setString(++stcount, sug_percentage);
					if(inv_flag.equals("ST"))
					{
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
					}
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, counterparty_cd);
					stmt1.setString(++stcount, cont_no);
					stmt1.setString(++stcount, agmt_no);
					stmt1.setString(++stcount, plant_seq);
					stmt1.setString(++stcount, bu_unit);
					stmt1.setString(++stcount, contract_type);
					stmt1.setString(++stcount, bu_state_tin);
					stmt1.setString(++stcount, invoice_seq);
					//stmt1.setString(++stcount, financial_year);
					stmt1.setString(++stcount, exist_financial_year);
					stmt1.setString(++stcount, cargo_no);
					stmt1.setString(++stcount, inv_flag);
					if(!inv_flag.equals("ST"))
					{
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
					}
					
					stmt1.executeUpdate();
					isOperated=true;
					
					stmt1.close();
					msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" not found for Modification!";
					msg_type="E";
				}
			}
			
			if(isOperated)
			{
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				stmt1.setString(3, invoice_seq);
				//stmt1.setString(4, financial_year);
				stmt1.setString(4, exist_financial_year);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(tax_count>0)
				{
					query2="DELETE FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, bu_state_tin);
					stmt2.setString(3, invoice_seq);
					//stmt2.setString(4, financial_year);
					stmt2.setString(4, exist_financial_year);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query2="INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						//stmt2.setString(3, invoice_seq);
						stmt2.setString(3, new_invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, tax_cd);
						stmt2.setString(6, sub_tax_code[i]);
						stmt2.setString(7, tax_dt);
						stmt2.setString(8, sub_tax_struct[i]);
						stmt2.setString(9, sub_tax_amt[i]);
						stmt2.setString(10, sub_tax_base_amt[i]);
						stmt2.setString(11, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(exchg_rate_type.equals("A"))
				{
					if(allocation_dt != null && price_cd.equals("2"))
					{
						for(int i=0;i<allocation_dt.length; i++)
						{
							int count=0;
							query2="SELECT COUNT(*) "
									+ "FROM FMS_INVOICE_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
									+ "AND ALLOCATION_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? ";
							stmt=dbcon.prepareStatement(query2);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, cont_no);
							stmt.setString(4, agmt_no);
							stmt.setString(5, plant_seq);
							stmt.setString(6, bu_unit);
							stmt.setString(7, contract_type);
							stmt.setString(8, bu_state_tin);
							stmt.setString(9, invoice_seq);
							//stmt.setString(10, financial_year);
							stmt.setString(10, exist_financial_year);
							stmt.setString(11, allocation_dt[i]);
							stmt.setString(12, cargo_no);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_INVOICE_DTL SET DAILY_QTY=?,SALE_PRICE=?,"
										+ "SALE_PRICE_UNIT=?,AMT_INR=?,AMT_USD=?,"
										+ "EXCHG_RATE_CD=?,EXCHG_RATE_VALUE=?,"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,INVOICE_SEQ=?,FINANCIAL_YEAR=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
										+ "AND ALLOCATION_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? ";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, allocation_qty[i]);
								stmt1.setString(2, salesPrice[i]);
								stmt1.setString(3, salesPriceCd[i]);
								stmt1.setString(4, amount_inr[i]);
								stmt1.setString(5, amount_usd[i]);
								stmt1.setString(6, exchng_cd);
								stmt1.setString(7, dailyExchngRate[i]);
								stmt1.setString(8, emp_cd);
								stmt1.setString(9, new_invoice_seq);
								stmt1.setString(10, financial_year);
								stmt1.setString(11, comp_cd);
								stmt1.setString(12, counterparty_cd);
								stmt1.setString(13, cont_no);
								stmt1.setString(14, agmt_no);
								stmt1.setString(15, plant_seq);
								stmt1.setString(16, bu_unit);
								stmt1.setString(17, contract_type);
								stmt1.setString(18, bu_state_tin);
								stmt1.setString(19, invoice_seq);
								//stmt1.setString(20, financial_year);
								stmt1.setString(20, exist_financial_year);
								stmt1.setString(21, allocation_dt[i]);
								stmt1.setString(22, cargo_no);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query2="INSERT INTO FMS_INVOICE_DTL(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
										+ "CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,ALLOCATION_DT,DAILY_QTY,SALE_PRICE,SALE_PRICE_UNIT,"
										+ "AMT_INR,AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_VALUE,ENT_BY,ENT_DT,CARGO_NO) "
										+ "VALUES(?,?,?,?,?,?,?,"
										+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
										+ "?,?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=dbcon.prepareStatement(query2);
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
								stmt1.setString(11, plant_seq);
								//stmt1.setString(12, invoice_seq);
								stmt1.setString(12, new_invoice_seq);
								stmt1.setString(13, allocation_dt[i]);
								stmt1.setString(14, allocation_qty[i]);
								stmt1.setString(15, salesPrice[i]);
								stmt1.setString(16, salesPriceCd[i]);
								stmt1.setString(17, amount_inr[i]);
								stmt1.setString(18, amount_usd[i]);
								stmt1.setString(19, exchng_cd);
								stmt1.setString(20, dailyExchngRate[i]);
								stmt1.setString(21, emp_cd);
								stmt1.setString(22, cargo_no);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						}
					}
				}
				
				//INSERT/UPDATE STORAGE DETAIL 
				if(inv_flag.equals("ST"))
				{
					int st_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INV_STORAGE_CRG_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					//stmt1.setString(4, financial_year);
					stmt1.setString(4, exist_financial_year);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						st_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(st_count>0)
					{
						query2="DELETE FROM FMS_INV_STORAGE_CRG_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						//stmt2.setString(4, financial_year);
						stmt2.setString(4, exist_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					if(storage_dt != null)
					{
						for(int i=0;i<storage_dt.length; i++)
						{
							String st_rate="";
							if(rate_type.equals("U"))
							{
								st_rate=user_define[i];
							}
							else
							{
								st_rate=storage_charge[i];
							}
							
							query2="INSERT INTO FMS_INV_STORAGE_CRG_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "STORAGE_DT,OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT) "
									+ "VALUES(?,?,?,?,"
									+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, storage_dt[i]);
							stmt2.setString(6, storage_inventory[i]);
							stmt2.setString(7, offtake_qty[i]);
							stmt2.setString(8, st_rate);
							stmt2.setString(9, rate_type);
							stmt2.setString(10, disc_day_flag[i]);
							stmt2.executeUpdate();
							stmt2.close();
						}
					}
				}
				
				//Advance adjust
				if(advance_adj_flag.equals("Y") && adjAdvFlag.equals("Y"))
				{
					int adv_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INV_ADV_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					//stmt1.setString(4, financial_year);
					stmt1.setString(4, exist_financial_year);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						adv_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(adv_count>0)
					{
						query2="DELETE FROM FMS_INV_ADV_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						//stmt2.setString(4, financial_year);
						stmt2.setString(4, exist_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					if(receipt_voucher!=null)
					{
						for(int i=0; i<receipt_voucher.length;i++)
						{
							query2="INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,SYSDATE)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, receipt_voucher[i]);
							stmt2.setString(6, "GROSS");
							stmt2.setString(7, gross_adjust[i]);
							stmt2.setString(8, emp_cd);
							stmt2.executeUpdate();
							stmt2.close();
							
							if(sub_tax_code!=null)
							{
								for(int j=0; j<sub_tax_code.length;j++)
								{
									String taxAbbr="";
									String taxStruct=sub_tax_struct[j];
									if(!taxStruct.equals(""))
									{
										String[] split=taxStruct.split(" ");
										taxAbbr=split[0];
									}
									
									String[] adv_tax = request.getParameterValues(taxAbbr+"_adjust");
									if(adv_tax!=null)
									{
										query2="INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
												+ "SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT) "
												+ "VALUES(?,?,?,?,"
												+ "?,?,?,?,SYSDATE)";
										stmt2=dbcon.prepareStatement(query2);
										stmt2.setString(1, comp_cd);
										stmt2.setString(2, bu_state_tin);
										//stmt2.setString(3, invoice_seq);
										stmt2.setString(3, new_invoice_seq);
										stmt2.setString(4, financial_year);
										stmt2.setString(5, receipt_voucher[i]);
										stmt2.setString(6, taxAbbr.toUpperCase());
										stmt2.setString(7, adv_tax[i]);
										stmt2.setString(8, emp_cd);
										stmt2.executeUpdate();
										stmt2.close();
									}
								}
							}
						}
					}
				}
			}
			
			InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, period_start_dt+" - "+period_end_dt, invoice_due_dt,invoice_amt, opration,"","",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,"");
			
			String filenm="frm_generate_sales_invoice.jsp";
			if(inv_flag.equals("UG"))
			{
				filenm="frm_generate_ltcora_sug_invoice.jsp";
			}
			else if(inv_flag.equals("ST"))
			{
				filenm="frm_generate_ltcora_storage_invoice.jsp";
			}
			url = "../sales_invoice/"+filenm+"?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+
					"&temp_period_start_dt="+temp_period_start_dt+"&temp_period_end_dt="+temp_period_end_dt+"&cargo_no="+cargo_no+
					"&inv_flag="+inv_flag+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Sales/LTCORA Invoice Detail Failed";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateSalesInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSalesInvoiceApproval()";
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
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			
			String final_tds_amt = request.getParameter("final_tds_amt")==null?"":request.getParameter("final_tds_amt");
			String isTDSalrted = request.getParameter("isTDSalrted")==null?"":request.getParameter("isTDSalrted");
			String new_tds_factor = request.getParameter("new_tds_factor")==null?"":request.getParameter("new_tds_factor");
			String new_tds_struct_cd = request.getParameter("new_tds_struct_cd")==null?"":request.getParameter("new_tds_struct_cd");
			String new_applicable_abbr = request.getParameter("new_applicable_abbr")==null?"":request.getParameter("new_applicable_abbr");
			
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
				String query="UPDATE FMS_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? ";
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
				stmt.executeUpdate();
				
				stmt.close();
				msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,"");
			}
			else if(activityFlag.equals("APPROVE"))
			{
				int count_inv_id_seq=0;
				
				String query = "SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
						 + "SELECT COUNT(*) AS CNT FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "	
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
						 + "UNION ALL "
						 + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_SVC_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + ") A";
				String temp_query=query;
				stmt=dbcon.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_no);
				stmt.setString(5, comp_cd);
				stmt.setString(6, financial_year);
				stmt.setString(7, bu_state_tin);
				stmt.setString(8, invoice_no);
				stmt.setString(9, comp_cd);
				stmt.setString(10, financial_year);
				stmt.setString(11, bu_state_tin);
				stmt.setString(12, invoice_no);
				stmt.setString(13, comp_cd);
				stmt.setString(14, financial_year);
				stmt.setString(15, bu_state_tin);
				stmt.setString(16, invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					query1="UPDATE FMS_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=? ";
					if(isTDSalrted.equals("Y") && rd.equals("Y"))
					{
						query1+=",TCS_TDS=?,TDS_GROSS_AMT=?,TDS_STRUCT_CD=?,TDS_GROSS_PERCENT=?,TDS_ALTED='Y' ";
					}
					query1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? ";
					int stcount=0;
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(++stcount, rd);
					stmt.setString(++stcount, emp_cd);
					stmt.setString(++stcount, invoice_no);
					stmt.setString(++stcount, invoice_id_seq);
					if(isTDSalrted.equals("Y") && rd.equals("Y"))
					{
						stmt.setString(++stcount, new_applicable_abbr);
						stmt.setString(++stcount, final_tds_amt);
						stmt.setString(++stcount, new_tds_struct_cd);
						stmt.setString(++stcount, new_tds_factor);
					}
					stmt.setString(++stcount, comp_cd);
					stmt.setString(++stcount, counterparty_cd);
					stmt.setString(++stcount, cont_no);
					stmt.setString(++stcount, agmt_no);
					stmt.setString(++stcount, plant_seq);
					stmt.setString(++stcount, bu_unit);
					stmt.setString(++stcount, contract_type);
					stmt.setString(++stcount, bu_state_tin);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
					stmt.setString(++stcount, financial_year);
					stmt.setString(++stcount, invoice_seq);
					stmt.setString(++stcount, cargo_no);
					stmt.setString(++stcount, inv_flag);
					stmt.executeUpdate();
					
					stmt.close();
					msg = "Successful! - "+invdtl+" Invoice "+invoice_no+" for "+counterparty_abbr+" "+temp_deal_no+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!"+(isTDSalrted.equals("Y")?" TDS Overwritten":"");
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
					
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,"");
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice "+invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../sales_invoice/rpt_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
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
			String activity_type,String flag,String inv_type,String contract_ref,String subject_line,String invoice_raised_in, String invoice_dt,String invdtl,
			String ori_inv_no) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
			String inv_nm="";
            if(inv_type.equals("CR"))
			{
            	//inv_nm="Credit Note";
            	inv_nm=subject_line.equals("")?"Credit Note":subject_line;
			}
			else if(inv_type.equals("DR"))
			{
				//inv_nm="Debit Note";
				inv_nm=subject_line.equals("")?"Debit Note":subject_line;
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
			else if(inv_type.equals("LP"))//FFLOW Generated Late payment
			{
				//inv_nm="Late Payment Invoice";
				inv_nm=subject_line;
			}
			else if(inv_type.equals("S-LP"))//Sales Invoice : Late Payement
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
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+cont_no+"&nbsp;["+contract_ref+"]&nbsp;</td></tr>";
					
					if(!ori_inv_no.equals(""))
					{
						mailBody+= "<tr><td><b>&nbsp;Ref. Invoice#</b>&nbsp;</td><td>&nbsp;"+ori_inv_no+"&nbsp;</td></tr>";
					}
					mailBody+= "<tr><td><b>&nbsp;Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>";
					mailBody+= "<tr><td><b>&nbsp;Invoice Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>";
					mailBody+= "<tr><td><b>&nbsp;Invoice Period</b>&nbsp;</td><td>&nbsp;"+inv_period+"&nbsp;</td></tr>";
					if(!inv_type.equals("S-LP"))
					{
						mailBody+= "<tr><td><b>&nbsp;Invoice Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>";
					}
					mailBody+= "<tr><td><b>&nbsp;Invoice Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
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
	
	private void InsertUpdateFFlowInvoiceDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateFFlowInvoiceDetail()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String opration_type = request.getParameter("opration_type")==null?"":request.getParameter("opration_type");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String mapping_id = request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String sub_invoice_type = request.getParameter("sub_invoice_type")==null?"":request.getParameter("sub_invoice_type");
			String invoice_category = request.getParameter("invoice_category")==null?"P":request.getParameter("invoice_category");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_ref = request.getParameter("invoice_ref")==null?"":request.getParameter("invoice_ref");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String linked_invoice = request.getParameter("linked_invoice")==null?"":request.getParameter("linked_invoice");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String no_of_line = request.getParameter("no_of_line")==null?"":request.getParameter("no_of_line");
			String note = request.getParameter("note")==null?"":request.getParameter("note");
			String subject_line = request.getParameter("subject_line")==null?"":request.getParameter("subject_line");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");

			String chk = request.getParameter("chk")==null?"":request.getParameter("chk");
			String auth = request.getParameter("auth")==null?"":request.getParameter("auth");
			String aprv = request.getParameter("aprv")==null?"":request.getParameter("aprv");

			String[] item_seq = request.getParameterValues("item_seq");
			String[] item_note = request.getParameterValues("item_note");
			String[] unit = request.getParameterValues("unit");
			String[] qty = request.getParameterValues("qty");
			String[] rate = request.getParameterValues("rate");
			String[] amount = request.getParameterValues("amount");

			String invoice_raised_in  = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String gross_amt_usd = request.getParameter("gross_amt_usd")==null?"":request.getParameter("gross_amt_usd");
			String exchange_rate = request.getParameter("exchange_rate")==null?"":request.getParameter("exchange_rate");
			String gross_amt_inr = request.getParameter("gross_amt_inr")==null?"":request.getParameter("gross_amt_inr");
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String adjust_amt = request.getParameter("adjust_amt")==null?"":request.getParameter("adjust_amt");
			String net_amt = request.getParameter("net_amt")==null?"":request.getParameter("net_amt");
			String amt_in_word = request.getParameter("amt_in_word")==null?"":request.getParameter("amt_in_word");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_cd = request.getParameter("tds_cd")==null?"":request.getParameter("tds_cd");
			String tds_dt = request.getParameter("tds_dt")==null?"":request.getParameter("tds_dt");
			String tcs_amt = request.getParameter("tcs_amt")==null?"":request.getParameter("tcs_amt");
			String tcs_cd = request.getParameter("tcs_cd")==null?"":request.getParameter("tcs_cd");
			String tcs_dt = request.getParameter("tcs_dt")==null?"":request.getParameter("tcs_dt");
			
			String tcs_tds = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");

			String temp_tax_cd = request.getParameter("temp_tax_cd")==null?"":request.getParameter("temp_tax_cd");
			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
			
			String[] sub_tax_struct = request.getParameterValues("sub_tax_struct");
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			/*String deal_no=contract_type+""+cont_no+"-"+cont_rev;
			if(contract_type.equals("S"))
			{
				deal_no=contract_type+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;
			}
			String temp_deal_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev, cont_no, cont_rev, contract_type);
			*/
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no =deal_no;

			int count=0;
			String query="SELECT COUNT(*) "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, invoice_seq);
			stmt.setString(3, bu_state_tin);
			stmt.setString(4, invoice_type);
			stmt.setString(5, financial_year);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;

			if(opration.equals("MODIFY"))
			{
				if(count > 0)
				{
					String temp_fiscal_yr=dateUtil.getFinancialYear(invoice_dt);
					if(!financial_year.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !financial_year.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
								+ "FROM FMS_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? "
								+ "AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE=? AND BU_STATE_TIN=?";
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, bu_state_tin);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							new_invoice_seq=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
					}
					
					query1="UPDATE FMS_FFLOW_INV_MST SET COUNTERPARTY_CD=?,AGMT_NO=?,AGMT_REV=?,"
							+ "CONT_NO=?,CONT_REV=?,CONTRACT_TYPE=?,BU_UNIT=?,BU_CONTACT_PERSON_CD=?,"
							+ "ADDR_FLAG=?,CONTACT_PERSON_CD=?,INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "INVOICE_CATEGORY=?,FREQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),NUM_LINE=?,LINKED_INVOICE=?,"
							+ "NOTE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,INVOICE_TYPE=?,FINANCIAL_YEAR=?,"
							+ "OTHER_INV_STR=?,GROSS_AMT_USD=?,EXCHG_RATE_VALUE=?,GROSS_AMT_INR=?,"
							+ "TAX_AMT=?,INVOICE_AMT=?,ADJUST_AMT=?,NET_PAYABLE_AMT=?,INVOICE_RAISED_IN=?,"
							+ "AMT_WORD=?,TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TDS_GROSS_AMT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TCS_AMT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TCS_TDS=?,ALLOC_QTY=?,DUE_DT=TO_DATE(?,'DD/MM/YYYY'),SUB_INV_TYPE=?,"
							+ "INVOICE_SEQ=?,CARGO_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, counterparty_cd);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_rev);
					stmt1.setString(6, contract_type);
					stmt1.setString(7, bu_unit);
					stmt1.setString(8, bu_contact_person);
					stmt1.setString(9, address_type);
					stmt1.setString(10, contact_person);
					stmt1.setString(11, invoice_dt);
					stmt1.setString(12, invoice_category);
					stmt1.setString(13, billing_cycle);
					stmt1.setString(14, period_start_dt);
					stmt1.setString(15, period_end_dt);
					stmt1.setString(16, no_of_line);
					stmt1.setString(17, linked_invoice);
					stmt1.setString(18, note);
					stmt1.setString(19, emp_cd);
					stmt1.setString(20, invoice_type);
					//stmt1.setString(21, financial_year);
					stmt1.setString(21, new_financial_year);
					stmt1.setString(22, subject_line);
					stmt1.setString(23, gross_amt_usd);
					stmt1.setString(24, exchange_rate);
					stmt1.setString(25, gross_amt_inr);
					stmt1.setString(26, tax_amt);
					stmt1.setString(27, invoice_amt);
					stmt1.setString(28, adjust_amt);
					stmt1.setString(29, net_amt);
					stmt1.setString(30, invoice_raised_in);
					stmt1.setString(31, amt_in_word);
					stmt1.setString(32, tax_cd);
					stmt1.setString(33, tax_dt);
					stmt1.setString(34, tds_amt);
					stmt1.setString(35, tds_cd);
					stmt1.setString(36, tds_dt);
					stmt1.setString(37, tcs_amt);
					stmt1.setString(38, tcs_cd);
					stmt1.setString(39, tcs_dt);
					stmt1.setString(40, tcs_tds);
					stmt1.setString(41, alloc_qty);
					stmt1.setString(42, invoice_due_dt);
					stmt1.setString(43, sub_invoice_type);
					stmt1.setString(44, new_invoice_seq);
					stmt1.setString(45, cargo_no);
					stmt1.setString(46, comp_cd);
					stmt1.setString(47, invoice_seq);
					stmt1.setString(48, bu_state_tin);
					stmt1.setString(49, invoice_type);
					stmt1.setString(50, financial_year);
					stmt1.executeUpdate();
					
					stmt1.close();
					msg = "Successful! - Freeflow Invoice for "+counterparty_abbr+" "+temp_deal_no+" Modified!";
					msg_type="S";
				}

				query2="DELETE FROM FMS_FFLOW_INV_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=?";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, invoice_seq);
				stmt2.setString(3, bu_state_tin);
				stmt2.setString(4, invoice_type);
				stmt2.setString(5, financial_year);
				stmt2.executeUpdate();
				
				stmt2.close();

				if(item_seq != null)
				{
					for(int i=0; i<item_seq.length; i++)
					{
						query1="INSERT INTO FMS_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
								+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE,BU_STATE_TIN,CARGO_NO) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,SYSDATE,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, agmt_rev);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, cont_rev);
						stmt1.setString(7, contract_type);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, bu_contact_person);
						stmt1.setString(10, address_type);
						stmt1.setString(11, contact_person);
						//stmt1.setString(12, invoice_seq);
						stmt1.setString(12, new_invoice_seq);
						stmt1.setString(13, invoice_no);
						stmt1.setString(14, item_seq[i]);
						stmt1.setString(15, item_note[i]);
						stmt1.setString(16, unit[i]);
						stmt1.setString(17, qty[i]);
						stmt1.setString(18, rate[i]);
						stmt1.setString(19, amount[i]);
						stmt1.setString(20, emp_cd);
						//stmt1.setString(21, financial_year);
						stmt1.setString(21, new_financial_year);
						stmt1.setString(22, invoice_type);
						stmt1.setString(23, bu_state_tin);
						stmt1.setString(24, cargo_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
			}
			else
			{
				financial_year=dateUtil.getFinancialYear(invoice_dt);
				
				queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
						+ "FROM FMS_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? AND BU_STATE_TIN=?";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, invoice_type);
				stmt.setString(4, bu_state_tin);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					invoice_seq=""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
				
				new_invoice_seq=invoice_seq;
				new_financial_year=financial_year;
				
				query1="INSERT INTO FMS_FFLOW_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
						+ "INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,NUM_LINE,LINKED_INVOICE,"
						+ "NOTE,ENT_BY,ENT_DT,DUE_DT,INVOICE_TYPE,FINANCIAL_YEAR,OTHER_INV_STR,"
						+ "GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,INVOICE_RAISED_IN,AMT_WORD,"
						+ "TAX_STRUCT_CD,TAX_EFF_DT,BU_STATE_TIN,TDS_GROSS_AMT,TDS_STRUCT_CD,TDS_EFF_DT,TCS_AMT,TCS_STRUCT_CD,TCS_EFF_DT,TCS_TDS,"
						+ "ALLOC_QTY,SUB_INV_TYPE,CARGO_NO) "
						+ "VALUES(?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,"
						+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,?,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev);
				stmt1.setString(5, cont_no);
				stmt1.setString(6, cont_rev);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, bu_unit);
				stmt1.setString(9, bu_contact_person);
				stmt1.setString(10, address_type);
				stmt1.setString(11, contact_person);
				stmt1.setString(12, invoice_seq);
				stmt1.setString(13, invoice_no);
				stmt1.setString(14, invoice_dt);
				stmt1.setString(15, invoice_category);
				stmt1.setString(16, billing_cycle);
				stmt1.setString(17, period_start_dt);
				stmt1.setString(18, period_end_dt);
				stmt1.setString(19, no_of_line);
				stmt1.setString(20, linked_invoice);
				stmt1.setString(21, note);
				stmt1.setString(22, emp_cd);
				stmt1.setString(23, invoice_due_dt);
				stmt1.setString(24, invoice_type);
				stmt1.setString(25, financial_year);
				stmt1.setString(26, subject_line);
				stmt1.setString(27, gross_amt_usd);
				stmt1.setString(28, exchange_rate);
				stmt1.setString(29, gross_amt_inr);
				stmt1.setString(30, tax_amt);
				stmt1.setString(31, invoice_amt);
				stmt1.setString(32, adjust_amt);
				stmt1.setString(33, net_amt);
				stmt1.setString(34, invoice_raised_in);
				stmt1.setString(35, amt_in_word);
				stmt1.setString(36, tax_cd);
				stmt1.setString(37, tax_dt);
				stmt1.setString(38, bu_state_tin);
				stmt1.setString(39, tds_amt);
				stmt1.setString(40, tds_cd);
				stmt1.setString(41, tds_dt);
				stmt1.setString(42, tcs_amt);
				stmt1.setString(43, tcs_cd);
				stmt1.setString(44, tcs_dt);
				stmt1.setString(45, tcs_tds);
				stmt1.setString(46, alloc_qty);
				stmt1.setString(47, sub_invoice_type);
				stmt1.setString(48, cargo_no);
				stmt1.executeUpdate();
				
				stmt1.close();
				msg = "Successful! - Freeflow Invoice for "+counterparty_abbr+" "+temp_deal_no+" Generated!";
				msg_type="S";

				if(item_seq != null)
				{
					for(int i=0; i<item_seq.length; i++)
					{
						query2="INSERT INTO FMS_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
								+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE,BU_STATE_TIN,CARGO_NO) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,SYSDATE,?,?,?,?)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, bu_unit);
						stmt2.setString(9, bu_contact_person);
						stmt2.setString(10, address_type);
						stmt2.setString(11, contact_person);
						stmt2.setString(12, invoice_seq);
						stmt2.setString(13, invoice_no);
						stmt2.setString(14, item_seq[i]);
						stmt2.setString(15, item_note[i]);
						stmt2.setString(16, unit[i]);
						stmt2.setString(17, qty[i]);
						stmt2.setString(18, rate[i]);
						stmt2.setString(19, amount[i]);
						stmt2.setString(20, emp_cd);
						stmt2.setString(21, financial_year);
						stmt2.setString(22, invoice_type);
						stmt2.setString(23, bu_state_tin);
						stmt2.setString(24, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
			}
			
			int tax_count=0;
			query1="SELECT COUNT(*) "
					+ "FROM FMS_FFLOW_INV_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_TYPE=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
			stmt1=dbcon.prepareStatement(query1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, invoice_type);
			stmt1.setString(4, invoice_seq);
			stmt1.setString(5, financial_year);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				tax_count=rset1.getInt(1);
			}
			rset1.close();
			stmt1.close();
			
			if(tax_count>0)
			{
				query2="DELETE FROM FMS_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, bu_state_tin);
				stmt2.setString(3, invoice_type);
				stmt2.setString(4, invoice_seq);
				stmt2.setString(5, financial_year);
				stmt2.executeUpdate();
				
				stmt2.close();
			}
			
			if(!tax_cd.equals("") && !tax_dt.equals("") && !tax_amt.equals("") && !temp_tax_cd.equals(tax_cd))
			{
				double tax_factor=0;
				Vector VTEMP_TAX_FACTOR=new Vector();
				Vector VTEMP_TAX_CODE=new Vector();
				Vector VTEMP_TAX_DESCR=new Vector();
				
				query2="SELECT A.TAX_CODE,A.FACTOR,B.TAX_ALIAS_CODE,A.TAX_ON_CD,A.TAX_ON "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_TAX_MST B "
						+ "WHERE A.TAX_STR_CD=? "
						+ "AND A.APP_DATE=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.TAX_CODE=B.TAX_CODE";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, tax_cd);
				stmt2.setString(2, tax_dt);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String tax_code=rset2.getString(1)==null?"":rset2.getString(1);
					double factor = rset2.getDouble(2);
					String temp_factor=rset2.getString(2)==null?"":rset2.getString(2);
					String tax_alise_code=rset2.getString(3)==null?"":rset2.getString(3);
					String tax_on_cd = rset2.getString(4)==null?"":rset2.getString(4);
					String tax_on = rset2.getString(5)==null?"":rset2.getString(5);
					
					String tax_sht_nm = tax_alise_code+" "+temp_factor+"%";
					
					if(tax_on.equals("2"))
					{
						double tax_on_factor = 0;
						query3="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? "
								+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') AND TAX_CODE=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, tax_cd);
						stmt3.setString(2, tax_dt);
						stmt3.setString(3, tax_on_cd);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							tax_on_factor = rset3.getDouble(1);
						}
						rset3.close();
						stmt3.close();
						
						query4="SELECT TAX_ALIAS_CODE "
								+ "FROM FMS_TAX_MST A "
								+ "WHERE TAX_CODE=? ";
						stmt4=dbcon.prepareStatement(query4);
						stmt4.setString(1, tax_on_cd);
						rset4 = stmt4.executeQuery();
						if(rset4.next())
						{
							tax_sht_nm+=" on "+(rset4.getString(1)==null?"":rset4.getString(1));
						}
						rset4.close();
						stmt4.close();
						
						tax_factor+=((tax_on_factor/100) * (factor/100)) * 100;
						
						VTEMP_TAX_FACTOR.add(((tax_on_factor/100) * (factor/100)) * 100);
					}
					else
					{
						tax_factor+=factor;
						VTEMP_TAX_FACTOR.add(factor);
					}
					
					VTEMP_TAX_CODE.add(tax_code);
					VTEMP_TAX_DESCR.add(tax_sht_nm);
				}
				rset2.close();
				stmt2.close();
				
				for(int i=0;i<VTEMP_TAX_CODE.size();i++)
				{
					double taxAmt=0;
					if(tax_factor>0)
					{
						taxAmt=(Double.parseDouble(""+VTEMP_TAX_FACTOR.elementAt(i))/tax_factor) * Double.parseDouble(tax_amt);
					}
					
					query3="INSERT INTO FMS_FFLOW_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,"
							+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
							+ "TAX_EFF_DT,TAX_DESCR,"
							+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
							+ "VALUES(?,?,"
							+ "?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,SYSDATE)";
					stmt3=dbcon.prepareStatement(query3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, bu_state_tin);
					//stmt3.setString(3, invoice_seq);
					stmt3.setString(3, new_invoice_seq);
					stmt3.setString(4, invoice_type);
					//stmt3.setString(5, financial_year);
					stmt3.setString(5, new_financial_year);
					stmt3.setString(6, tax_cd);
					stmt3.setString(7, ""+VTEMP_TAX_CODE.elementAt(i));
					stmt3.setString(8, tax_dt);
					stmt3.setString(9, ""+VTEMP_TAX_DESCR.elementAt(i));
					stmt3.setString(10, nf.format(taxAmt));
					stmt3.setString(11, "");
					stmt3.setString(12, emp_cd);
					stmt3.executeUpdate();
					
					stmt3.close();
				}
			}
			else
			{
				if(sub_tax_amt!=null)
				{
					for(int i=0;i<sub_tax_amt.length;i++)
					{
						query3="INSERT INTO FMS_FFLOW_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,"
								+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
								+ "TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,"
								+ "?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, bu_state_tin);
						//stmt3.setString(3, invoice_seq);
						stmt3.setString(3, new_invoice_seq);
						stmt3.setString(4, invoice_type);
						//stmt3.setString(5, financial_year);
						stmt3.setString(5, new_financial_year);
						stmt3.setString(6, tax_cd);
						stmt3.setString(7, sub_tax_code[i]);
						stmt3.setString(8, tax_dt);
						stmt3.setString(9, sub_tax_struct[i]);
						stmt3.setString(10, sub_tax_amt[i]);
						stmt3.setString(11, "");
						stmt3.setString(12, emp_cd);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
				}
			}
			
			if(!invoice_seq.equals(""))
			{
				String appPath = request.getServletContext().getRealPath("");

				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}

				String sub_folder="unsigned_pdf";
				String sub_folder2="freeflow_annexure";
				String folderPath=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(folderPath);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }
		        
		        if(!financial_year.equals(new_financial_year) && !financial_year.equals("") && !new_financial_year.equals(""))
		        {
		        	query4="UPDATE FMS_FFLOW_INV_ANNEXURE_DTL SET FINANCIAL_YEAR=?,INVOICE_SEQ=? "
		    				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND INVOICE_TYPE=? ";
		    		stmt4=dbcon.prepareStatement(query4);
		    		stmt4.setString(1, new_financial_year);
		    		stmt4.setString(2, new_invoice_seq);
		    		stmt4.setString(3, comp_cd);
			    	stmt4.setString(4, bu_state_tin);
			    	stmt4.setString(5, invoice_seq);
			    	stmt4.setString(6, financial_year);
			    	stmt4.setString(7, invoice_type);
			    	stmt4.executeUpdate();
		    		
		    		stmt4.close();
		    		
		    		String final_folder=invoice_type+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq;
		    		String new_final_folder=invoice_type+"_"+new_financial_year+"_"+bu_state_tin+"_"+new_invoice_seq;
		    		
		    		String realPath = request.getRealPath("");
		    		
			        File final_folderDir = new File(folderPath+File.separator+final_folder);
			        File new_final_folderDir = new File(folderPath+File.separator+new_final_folder);
			       
			        String canonicalPath_files = final_folderDir.getCanonicalPath();
			        if(!canonicalPath_files.startsWith(realPath))
			        {
			        	throw new IOException("Entry is outside of the target directory!");
			        }
			        else if(final_folderDir.exists())
			        {
			        	canonicalPath_files = new_final_folderDir.getCanonicalPath();
			        	if(!canonicalPath_files.startsWith(realPath))
				        {
				        	throw new IOException("Entry is outside of the target directory!");
				        }
			        	else if(!new_final_folderDir.exists())
			        	{
			        		boolean renameTo = final_folderDir.renameTo(new_final_folderDir);
			        	}
			        }
		        }
		        
		        String final_folder=invoice_type+"_"+new_financial_year+"_"+bu_state_tin+"_"+new_invoice_seq;
		        File final_folderDir = new File(folderPath+File.separator+final_folder);
		        String canonicalPath_final_folderDir= final_folderDir.getCanonicalPath();
		        
		        String file_name="";
		        String fileName="";
		        for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	// refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName();
				    if(!fileName.equals("") )
				    {
				    	file_name=fileName;
				    	
				    	if(!canonicalPath_final_folderDir.startsWith(appPath))
				        {
				    		throw new IOException("Entry is outside of the target directory!");
				        }
				        else if(!final_folderDir.exists())
				        {
				        	final_folderDir.mkdir();
				        }
				    	
				    	part.write(final_folderDir +File.separator+ fileName);
				    	
				    	String annexure_seq="";
				    	queryString="SELECT MAX(NVL(ANNEXURE_SEQ,0)) "
								+ "FROM FMS_FFLOW_INV_ANNEXURE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND INVOICE_TYPE=? ";
				    	stmt2=dbcon.prepareStatement(queryString);
				    	stmt2.setString(1, comp_cd);
				    	stmt2.setString(2, bu_state_tin);
				    	//stmt2.setString(3, invoice_seq);
				    	//stmt2.setString(4, financial_year);
				    	stmt2.setString(3, new_invoice_seq);
				    	stmt2.setString(4, new_financial_year);
				    	stmt2.setString(5, invoice_type);
				    	rset2=stmt2.executeQuery();
				    	if(rset2.next())
				    	{
				    		annexure_seq=""+(rset2.getInt(1)+1);
				    	}
				    	else
				    	{
				    		annexure_seq="1";
				    	}
				    	rset2.close();
				    	stmt2.close();
				    	
				    	query3="SELECT ANNEXURE_SEQ "
								+ "FROM FMS_FFLOW_INV_ANNEXURE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND INVOICE_TYPE=? AND UPPER(FILE_NAME)=? ";
				    	stmt3=dbcon.prepareStatement(query3);
				    	stmt3.setString(1, comp_cd);
				    	stmt3.setString(2, bu_state_tin);
				    	//stmt3.setString(3, invoice_seq);
				    	//stmt3.setString(4, financial_year);
				    	stmt3.setString(3, new_invoice_seq);
				    	stmt3.setString(4, new_financial_year);
				    	stmt3.setString(5, invoice_type);
				    	stmt3.setString(6, fileName.toUpperCase());
				    	rset3=stmt3.executeQuery();
				    	if(rset3.next())
				    	{
				    		annexure_seq=rset3.getString(1)==null?"":rset3.getString(1);
				    		
				    		query4="UPDATE FMS_FFLOW_INV_ANNEXURE_DTL SET FILE_NAME=? "
				    				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
				 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
				 	        		+ "AND INVOICE_TYPE=? AND ANNEXURE_SEQ=? ";
				    		stmt4=dbcon.prepareStatement(query4);
				    		stmt4.setString(1, fileName);
				    		stmt4.setString(2, comp_cd);
					    	stmt4.setString(3, bu_state_tin);
					    	//stmt4.setString(4, invoice_seq);
					    	//stmt4.setString(5, financial_year);
					    	stmt4.setString(4, new_invoice_seq);
					    	stmt4.setString(5, new_financial_year);
					    	stmt4.setString(6, invoice_type);
					    	stmt4.setString(7, annexure_seq);
				    		stmt4.executeUpdate();
				    		
				    		stmt4.close();
				    	}
				    	else
				    	{
					    	if(!annexure_seq.equals(""))
					    	{
					    		query4="INSERT INTO FMS_FFLOW_INV_ANNEXURE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,ANNEXURE_SEQ,"
					        			+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
					        			+ "VALUES(?,?,?,?,?,"
					        			+ "?,?,SYSDATE,?)";
					    		stmt4=dbcon.prepareStatement(query4);
					    		stmt4.setString(1, comp_cd);
						    	stmt4.setString(2, bu_state_tin);
						    	//stmt4.setString(3, invoice_seq);
						    	//stmt4.setString(4, financial_year);
						    	stmt4.setString(3, new_invoice_seq);
						    	stmt4.setString(4, new_financial_year);
						    	stmt4.setString(5, annexure_seq);
						    	stmt4.setString(6, fileName);
						    	stmt4.setString(7, emp_cd);
						    	stmt4.setString(8, invoice_type);
					    		stmt4.executeUpdate();
					    		
					    		stmt4.close();
					    	}
				    	}
				    	rset3.close();
				    	stmt3.close();
				    } 
		        }
			}
			
			InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, period_start_dt+" - "+period_end_dt, invoice_due_dt,invoice_amt, opration,"",invoice_type,contract_ref,subject_line,invoice_raised_in,invoice_dt,"","");

			if(opration.equals("INSERT"))
			{
				opration="MODIFY";
			}
			url = "../sales_invoice/frm_f_flow_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&invoice_type="+invoice_type+"&opration="+opration+
					"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+
					"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&inv_no="+invoice_no+"&inv_seq="+new_invoice_seq+//"&inv_seq="+invoice_seq+
					"&financial="+new_financial_year+"&activity_type="+activity_type+"&bu_state_tin="+bu_state_tin+"&accroid="+accroid+commonUrl_pra;
					//"&financial="+financial_year+"&activity_type="+activity_type+"&bu_state_tin="+bu_state_tin+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Insert / Update FFlow Invoice Detail Failed";
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
	
	private void DeleteFFlowAnnexure(HttpServletRequest request) throws SQLException
	{
		String function_nm="DeleteFFlowAnnexure()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String mapping_id = request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			String annexure_seq = request.getParameter("annexure_seq")==null?"":request.getParameter("annexure_seq");
			String annexure_folder = request.getParameter("annexure_folder")==null?"":request.getParameter("annexure_folder");
			
			String appPath = request.getServletContext().getRealPath("");

			String main_folder="";
			if(!comp_cd.equals(""))
			{
				main_folder=CommonVariable.work_dir+comp_cd;
			}

	        String final_folder=annexure_folder;
	        
			
	        String file_nm="";
	        queryString="SELECT FILE_NAME "
					+ "FROM FMS_FFLOW_INV_ANNEXURE_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
 	        		+ "AND INVOICE_TYPE=? AND ANNEXURE_SEQ=?";
	        stmt=dbcon.prepareStatement(queryString);
	        stmt.setString(1, comp_cd);
	        stmt.setString(2, bu_state_tin);
	        stmt.setString(3, invoice_seq);
	        stmt.setString(4, financial_year);
	        stmt.setString(5, invoice_type);
	        stmt.setString(6, annexure_seq);
	        rset=stmt.executeQuery();
	    	if(rset.next())
	    	{
	    		file_nm=rset.getString(1)==null?"":rset.getString(1);
	    		
	    		File final_file = new File(appPath+File.separator+main_folder+""+CommonVariable.freeflow_annexure_path+""+final_folder+File.separator+file_nm);
	    		String canonicalPath_final_file = final_file.getCanonicalPath();
	    		
	    		if(!canonicalPath_final_file.startsWith(appPath))
		        {
		        	throw new IOException("Entry is outside of the target directory!");
		        }
		        else if(final_file.exists())
	    		{
		        	boolean isDeleted = final_file.delete();
	    			
	    			query1="DELETE FROM FMS_FFLOW_INV_ANNEXURE_DTL "
	    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	     	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	     	        		+ "AND INVOICE_TYPE=? AND ANNEXURE_SEQ=?";
	    			stmt1=dbcon.prepareStatement(query1);
	    			stmt1.setString(1, comp_cd);
	    			stmt1.setString(2, bu_state_tin);
	    			stmt1.setString(3, invoice_seq);
	    			stmt1.setString(4, financial_year);
	    			stmt1.setString(5, invoice_type);
	    			stmt1.setString(6, annexure_seq);
	    			stmt1.executeQuery();
	    			
	    			stmt1.close();
	    			
	    			msg = "Successful! - Annexure File Deleted Successfully!";
					msg_type="S";
	    		}
	    		else
	    		{
	    			msg = "Failed! - Annexure File Deletion Falied!";
					msg_type="E";
	    		}
	    	}
	    	else
    		{
    			msg = "Failed! - Annexure File Deletion Falied!";
				msg_type="E";
    		}
	    	rset.close();
	    	stmt.close();
	    		
			url = "../sales_invoice/frm_f_flow_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&invoice_type="+invoice_type+"&opration="+opration+
					"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+
					"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+
					"&financial="+financial_year+"&activity_type="+activity_type+"&bu_state_tin="+bu_state_tin+"&accroid="+accroid+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Delete FFlow Invoice Annexure Failed";
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
	
	private void InsertUpdateFFlowSalesInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateFFlowSalesInvoiceApproval()";
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
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String subject_line = request.getParameter("subject_line")==null?"":request.getParameter("subject_line");
			
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			
			/*String deal_no=contract_type+""+cont_no+"-"+cont_rev;
			if(contract_type.equals("S"))
			{
				deal_no=contract_type+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;
			}*/
			//String temp_deal_no=utilBean.getDisplayDealMapping(agmt_no, agmt_rev, cont_no, cont_rev, contract_type);
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no=deal_no;
			
			if(activityFlag.equals("CHECK"))
			{
				String query="UPDATE FMS_FFLOW_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, invoice_seq);
				stmt.setString(5, bu_state_tin);
				stmt.setString(6, invoice_type);
				stmt.setString(7, financial_year);
				stmt.executeUpdate();
				
				stmt.close();
				msg = "Successful! - Freeflow Invoice for "+counterparty_abbr+" "+temp_deal_no+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";	
			}
			else if(activityFlag.equals("APPROVE"))
			{
				int count_inv_id_seq=0;
				String query = "SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
						 + "SELECT COUNT(*) AS CNT FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "	
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
						 + "UNION ALL "
						 + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_SVC_INVOICE_MST "
						 	+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + ") A";
				String temp_query=query;
				stmt=dbcon.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_no);
				stmt.setString(5, comp_cd);
				stmt.setString(6, financial_year);
				stmt.setString(7, bu_state_tin);
				stmt.setString(8, invoice_no);
				stmt.setString(9, comp_cd);
				stmt.setString(10, financial_year);
				stmt.setString(11, bu_state_tin);
				stmt.setString(12, invoice_no);
				stmt.setString(13, comp_cd);
				stmt.setString(14, financial_year);
				stmt.setString(15, bu_state_tin);
				stmt.setString(16, invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();

				if(count_inv_id_seq==0)
				{
					query1="UPDATE FMS_FFLOW_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE, "
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=?";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, invoice_no);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_type);
					stmt.setString(9, financial_year);
					stmt.executeUpdate();
					
					stmt.close();
					msg = "Successful! - Freeflow Invoice "+invoice_no+" for "+counterparty_abbr+" "+temp_deal_no+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Freeflow Invoice "+invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, period_start_dt+" - "+period_end_dt, invoice_due_dt,invoice_amt, activityFlag,rd,invoice_type,contract_ref,subject_line,invoice_raised_in,invoice_dt,"","");
			
			url = "../sales_invoice/rpt_f_flow_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&address_type="+address_type+"&period_start_dt="+period_start_dt+"&invoice_type="+invoice_type+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&cargo_no="+cargo_no+"&accroid="+accroid+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - FFlow Sales Invoice Approval Modification Failed";
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
			String[] pdf_type = request.getParameterValues("pdf_type");
			
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String mail_inv_type = request.getParameter("mail_inv_type")==null?"":request.getParameter("mail_inv_type");
			
			int entryUpdated=0;
			if(email_to != null)
			{
				for(int i=0;i<email_to.length;i++)
				{
					email_body[i]=email_body[i].replaceAll("\n", "<br>");
					email_body[i]="<html>"
							+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body[i]+"</span>"
							+ "</html>";
					
					boolean isMailSent=false;
					if(!email_to[i].equals("") && !email_body[i].equals(""))
					{
						String[] attachment = request.getParameterValues("attachment"+index[i]);
						if(attachment != null)
						{
							isMailSent=mailDelv.sendMailWithMultipleAttachment(comp_cd,email_to[i], subject[i], email_body[i], attachment, email_cc[i], email_bcc[i]);
						}
					}
					
					if(isMailSent)
					{
						int entryExist=0;
						
						if(mail_inv_type.equals("F"))
						{
							queryString="SELECT COUNT(*) "
									+ "FROM FMS_FFLOW_INV_MST A , FMS_FFLOW_INV_FILE_DTL B "
		            		  		+ "WHERE A.COMPANY_CD=? AND A.BU_STATE_TIN=? AND A.INVOICE_SEQ=? AND A.FINANCIAL_YEAR=? AND A.INVOICE_TYPE=? AND B.PDF_TYPE=? "
		            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, bu_state_tin);
							stmt.setString(3, invoice_seq);
							stmt.setString(4, financial_year);
							stmt.setString(5, invoice_type);
							stmt.setString(6, pdf_type[i]);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								entryExist=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(entryExist > 0)
							{
								queryString="UPDATE FMS_FFLOW_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
			            		  		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INVOICE_TYPE=?";
								stmt=dbcon.prepareStatement(queryString);
								stmt.setString(1, "Y");
								stmt.setString(2, emp_cd);
								stmt.setString(3, comp_cd);
								stmt.setString(4, bu_state_tin);
								stmt.setString(5, invoice_seq);
								stmt.setString(6, financial_year);
								stmt.setString(7, pdf_type[i]);
								stmt.setString(8, invoice_type);
								stmt.executeUpdate();
								entryUpdated++;
							}
						}
						else
						{
							queryString="SELECT COUNT(*) "
		            		  		+ "FROM FMS_INVOICE_MST A, FMS_INV_FILE_DTL B "
		            		  		+ "WHERE A.COMPANY_CD=? AND A.BU_STATE_TIN=? AND A.INVOICE_SEQ=? AND A.FINANCIAL_YEAR=? AND B.PDF_TYPE=? "
		            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, bu_state_tin);
							stmt.setString(3, invoice_seq);
							stmt.setString(4, financial_year);
							stmt.setString(5, pdf_type[i]);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								entryExist=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(entryExist > 0)
							{
								queryString="UPDATE FMS_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
			            		  		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
								stmt=dbcon.prepareStatement(queryString);
								stmt.setString(1, "Y");
								stmt.setString(2, emp_cd);
								stmt.setString(3, comp_cd);
								stmt.setString(4, bu_state_tin);
								stmt.setString(5, invoice_seq);
								stmt.setString(6, financial_year);
								stmt.setString(7, pdf_type[i]);
								stmt.executeUpdate();
								entryUpdated++;
							}
						}
					}
					msg="Mail Sent for "+subject[i];
					msg_type="S";
					
					if(entryUpdated>0)
					{
						dbcon.commit();
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
			}
			
			url = "../sales_invoice/frm_sign_pdf_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
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
	
	private void InsertUpdateCreditDebitDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCreditDebitDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String crdr_gen_type =request.getParameter("crdr_gen_type")==null?"":request.getParameter("crdr_gen_type");
			
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
			String inv_flag = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String price = request.getParameter("price")==null?"":request.getParameter("price");
			String price_cd = request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exchng_cd = request.getParameter("exchng_cd")==null?"":request.getParameter("exchng_cd");
			String exchng_dt = request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			String gross_amt1 = request.getParameter("gross_amt1")==null?"":request.getParameter("gross_amt1");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String exchg_rate_type = request.getParameter("exchg_rate_type")==null?"":request.getParameter("exchg_rate_type");
			String tcs_tds = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			String tcs_amt = request.getParameter("tcs_amt")==null?"":request.getParameter("tcs_amt");
			String tcs_factor = request.getParameter("tcs_factor")==null?"":request.getParameter("tcs_factor");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			String sug_qty = request.getParameter("sug_qty")==null?"":request.getParameter("sug_qty");
			String sug_percentage = request.getParameter("sug_percent")==null?"":request.getParameter("sug_percent");
			
			String tcs_struct_cd = request.getParameter("tcs_struct_cd")==null?"":request.getParameter("tcs_struct_cd");
			String tcs_struct_dt = request.getParameter("tcs_struct_dt")==null?"":request.getParameter("tcs_struct_dt");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_factor = request.getParameter("tds_factor")==null?"":request.getParameter("tds_factor");
			String tds_struct_cd = request.getParameter("tds_struct_cd")==null?"":request.getParameter("tds_struct_cd");
			String tds_struct_dt = request.getParameter("tds_struct_dt")==null?"":request.getParameter("tds_struct_dt");
			
			String transportation_amount = request.getParameter("transportation_amount")==null?"":request.getParameter("transportation_amount");
			String transportation_tariff = request.getParameter("transportation_tariff")==null?"":request.getParameter("transportation_tariff");
			String marketing_margin_amount = request.getParameter("marketing_margin_amount")==null?"":request.getParameter("marketing_margin_amount");
			String marketing_margin = request.getParameter("marketing_margin")==null?"":request.getParameter("marketing_margin");
			String other_charges_amount = request.getParameter("other_charges_amount")==null?"":request.getParameter("other_charges_amount");
			String other_charges = request.getParameter("other_charges")==null?"":request.getParameter("other_charges");
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String imb_qty = request.getParameter("imb_qty")==null?"":request.getParameter("imb_qty");
			String imb_amt = request.getParameter("imb_amt")==null?"":request.getParameter("imb_amt");
			String ship_or_pay_qty = request.getParameter("ship_or_pay_qty")==null?"":request.getParameter("ship_or_pay_qty");
			String ship_or_pay_amt = request.getParameter("ship_or_pay_amt")==null?"":request.getParameter("ship_or_pay_amt");
			String ovrun_qty = request.getParameter("ovrun_qty")==null?"":request.getParameter("ovrun_qty");
			String ovrun_amt = request.getParameter("ovrun_amt")==null?"":request.getParameter("ovrun_amt");
			
			String[] sub_tax_struct = request.getParameterValues("sub_tax_struct");
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			//NEW VALUES
			String new_alloc_qty = request.getParameter("new_alloc_qty")==null?"":request.getParameter("new_alloc_qty");
			String new_price = request.getParameter("new_price")==null?"":request.getParameter("new_price");
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_exchng_rate = request.getParameter("new_exchng_rate")==null?"":request.getParameter("new_exchng_rate");
			String new_gross_amt1 = request.getParameter("new_gross_amt1")==null?"":request.getParameter("new_gross_amt1");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_cd = request.getParameter("new_tax_cd")==null?"":request.getParameter("new_tax_cd");
			String new_invoice_amt = request.getParameter("new_invoice_amt")==null?"":request.getParameter("new_invoice_amt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");
			String new_transportation_amount = request.getParameter("new_transportation_amount")==null?"":request.getParameter("new_transportation_amount");
			String new_transportation_tariff = request.getParameter("new_transportation_tariff")==null?"":request.getParameter("new_transportation_tariff");
			String new_marketing_margin_amount = request.getParameter("new_marketing_margin_amount")==null?"":request.getParameter("new_marketing_margin_amount");
			String new_marketing_margin = request.getParameter("new_marketing_margin")==null?"":request.getParameter("new_marketing_margin");
			String new_other_charges_amount = request.getParameter("new_other_charges_amount")==null?"":request.getParameter("new_other_charges_amount");
			String new_other_charges = request.getParameter("new_other_charges")==null?"":request.getParameter("new_other_charges");
			String new_tcs_amt = request.getParameter("new_tcs_amt")==null?"":request.getParameter("new_tcs_amt");
			String new_tcs_factor = request.getParameter("new_tcs_factor")==null?"":request.getParameter("new_tcs_factor");
			String new_tds_amt = request.getParameter("new_tds_amt")==null?"":request.getParameter("new_tds_amt");
			String new_tds_factor = request.getParameter("new_tds_factor")==null?"":request.getParameter("new_tds_factor");
			String new_tds_struct_cd = request.getParameter("new_tds_struct_cd")==null?"":request.getParameter("new_tds_struct_cd");
			String new_tds_struct_dt = request.getParameter("new_tds_struct_dt")==null?"":request.getParameter("new_tds_struct_dt");
			
			String[] new_sub_tax_struct = request.getParameterValues("new_sub_tax_struct");
			String[] new_sub_tax_amt = request.getParameterValues("new_sub_tax_amt");
			String[] new_sub_tax_code = request.getParameterValues("new_sub_tax_code");
			String[] new_sub_tax_base_amt = request.getParameterValues("new_sub_tax_base_amt");
			/////
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no = deal_no;
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			
			boolean isOperated = false;
			if(opration.equals("PREPARE"))
			{
				int count=0;
				
				if(crdr_gen_type.equals("CRDR_IMB"))
				{
					/* AS DISCUSSIED WITH VIJAY ON 20260219, N NUMBER OF TIME USER WILL CREATE IMB CR/DR
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND REF_NO=? "
							+ "AND (CRITERIA LIKE '%IMB%' OR CRITERIA LIKE '%SHIP%' OR CRITERIA LIKE '%UNAUTH%') ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, sel_inv_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					*/
				}
				else
				{
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND REF_NO=? "
							+ "AND CRITERIA NOT LIKE '%IMB%' AND CRITERIA NOT LIKE '%SHIP%' AND CRITERIA NOT LIKE '%UNAUTH%' ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, sel_inv_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
				}
				
				if(count==0)
				{
					financial_year=dateUtil.getFinancialYear(invoice_dt);
					new_financial_year=financial_year;
					
					String inv_seq="1";
					query2="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=?";
					stmt=dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						inv_seq = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();

					invoice_seq=inv_seq;
					new_invoice_seq=invoice_seq;
					
					query1="INSERT INTO FMS_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
							+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_DT,"
							+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
							+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
							+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
							+ "INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,REMARK_1,REMARK_2,EXCHG_RATE_TYPE,"
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,INVOICE_NO,INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
							+ "TCS_STRUCT_CD,TCS_EFF_DT,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TDS_EFF_DT,"
							+ "MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,CARGO_NO,INV_FLAG,SUG_QTY,SUG_PERCENT,REF_NO,CRITERIA,"
							+ "IMB_AMT,IMB_QTY,SHIPAY_AMT,SHIPAY_QTY,OVRUN_AMT,OVRUN_QTY) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,SYSDATE,?,?,?,?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, bu_unit);
					stmt1.setString(9, bu_contact_person);
					stmt1.setString(10, bu_state_tin);
					stmt1.setString(11, plant_seq);
					stmt1.setString(12, contact_person);
					stmt1.setString(13, invoice_seq);
					stmt1.setString(14, invoice_dt);
					stmt1.setString(15, billing_cycle);
					stmt1.setString(16, period_start_dt);
					stmt1.setString(17, period_end_dt);
					stmt1.setString(18, invoice_due_dt);
					stmt1.setString(19, alloc_qty);
					stmt1.setString(20, price);
					stmt1.setString(21, price_cd);
					stmt1.setString(22, gross_amt);
					stmt1.setString(23, exchng_cd);
					stmt1.setString(24, exchng_dt);
					stmt1.setString(25, exchng_rate);
					stmt1.setString(26, invoice_raised_in);
					stmt1.setString(27, gross_amt1);
					stmt1.setString(28, tax_amt);
					stmt1.setString(29, tax_cd);
					stmt1.setString(30, tax_dt);
					stmt1.setString(31, invoice_amt);
					stmt1.setString(32, net_payable);
					stmt1.setString(33, emp_cd);
					stmt1.setString(34, financial_year);
					stmt1.setString(35, remark1);
					stmt1.setString(36, remark2);
					stmt1.setString(37, exchg_rate_type);
					stmt1.setString(38, tcs_tds);
					stmt1.setString(39, tcs_amt);
					stmt1.setString(40, tcs_factor);
					stmt1.setString(41, invoice_no);
					stmt1.setString(42, invoice_id_seq);
					stmt1.setString(43, transportation_tariff);
					stmt1.setString(44, transportation_amount);
					stmt1.setString(45, tcs_struct_cd);
					stmt1.setString(46, tcs_struct_dt);
					stmt1.setString(47, tds_amt);
					stmt1.setString(48, tds_factor);
					stmt1.setString(49, tds_struct_cd);
					stmt1.setString(50, tds_struct_dt);
					stmt1.setString(51, marketing_margin);
					stmt1.setString(52, marketing_margin_amount);
					stmt1.setString(53, other_charges);
					stmt1.setString(54, other_charges_amount);
					stmt1.setString(55, cargo_no);
					stmt1.setString(56, inv_flag);
					stmt1.setString(57, sug_qty);
					stmt1.setString(58, sug_percentage);
					stmt1.setString(59, sel_inv_no);
					stmt1.setString(60, criteri_formula);
					stmt1.setString(61, imb_amt);
					stmt1.setString(62, imb_qty);
					stmt1.setString(63, ship_or_pay_amt);
					stmt1.setString(64, ship_or_pay_qty);
					stmt1.setString(65, ovrun_amt);
					stmt1.setString(66, ovrun_qty);
					stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					if(!crdr_gen_type.equals("CRDR_IMB"))
					{
						//NEW VALUE FMS_INV_CRDR_REF
						query1="INSERT INTO FMS_INV_CRDR_REF(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,"
								+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,INVOICE_AMT,NET_PAYABLE_AMT,"
								+ "ENT_BY,ENT_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,"
								+ "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,?,?,"
								+ "?,SYSDATE,?,?,?,?,?,"
								+ "?,?,?,?,?,?)";
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, bu_state_tin);
						stmt1.setString(++stcount, invoice_seq);
						stmt1.setString(++stcount, financial_year);
						stmt1.setString(++stcount, new_alloc_qty);
						stmt1.setString(++stcount, new_price);
						stmt1.setString(++stcount, price_cd);
						stmt1.setString(++stcount, new_gross_amt);
						stmt1.setString(++stcount, exchng_cd);
						stmt1.setString(++stcount, exchng_dt);
						stmt1.setString(++stcount, new_exchng_rate);
						stmt1.setString(++stcount, invoice_raised_in);
						stmt1.setString(++stcount, new_gross_amt1);
						stmt1.setString(++stcount, new_tax_amt);
						stmt1.setString(++stcount, new_tax_cd);
						stmt1.setString(++stcount, new_invoice_amt);
						stmt1.setString(++stcount, new_net_payable);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, tcs_tds);
						stmt1.setString(++stcount, new_tcs_amt);
						stmt1.setString(++stcount, new_tcs_factor);
						stmt1.setString(++stcount, new_tds_amt);
						stmt1.setString(++stcount, new_tds_factor);
						stmt1.setString(++stcount, new_transportation_tariff);
						stmt1.setString(++stcount, new_transportation_amount);
						stmt1.setString(++stcount, new_marketing_margin);
						stmt1.setString(++stcount, new_marketing_margin_amount);
						stmt1.setString(++stcount, new_other_charges);
						stmt1.setString(++stcount, new_other_charges_amount);
						stmt1.executeUpdate();
						stmt1.close();
					}
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+counterparty_abbr+" "+temp_deal_no+" Generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Credit/Debit Note against Invoice "+sel_inv_no+" for "+counterparty_abbr+" "+temp_deal_no+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				String query="SELECT COUNT(*) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					String temp_fiscal_yr=dateUtil.getFinancialYear(invoice_dt);
					if(!financial_year.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !financial_year.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
								+ "FROM FMS_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND BU_STATE_TIN=?";
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, bu_state_tin);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							new_invoice_seq=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
					}
					
					query1="UPDATE FMS_INVOICE_MST SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, "
							+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), FREQ=?, DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
							+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
							+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
							+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, "
							+ "NET_PAYABLE_AMT=?,FINANCIAL_YEAR=?,REMARK_1=?,REMARK_2=?,EXCHG_RATE_TYPE=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,TCS_TDS=?,TCS_AMT=?,TCS_FACTOR=?,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=?,TRANSPORTATION_CHARGE=?,"
							+ "TRANSPORTATION_AMOUNT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TDS_GROSS_AMT=?,TDS_GROSS_PERCENT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,INVOICE_SEQ=?,SUG_QTY=?,SUG_PERCENT=?,"
							+ "INV_FLAG=?,REF_NO=?,CRITERIA=?,IMB_AMT=?,IMB_QTY=?,SHIPAY_AMT=?,SHIPAY_QTY=?,OVRUN_AMT=?,OVRUN_QTY=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? "
							+ "";
							//+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							//+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							//+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							//+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, bu_contact_person);
					stmt1.setString(2, contact_person);
					stmt1.setString(3, invoice_dt);
					stmt1.setString(4, billing_cycle);
					stmt1.setString(5, invoice_due_dt);
					stmt1.setString(6, alloc_qty);
					stmt1.setString(7, price);
					stmt1.setString(8, price_cd);
					stmt1.setString(9, gross_amt);
					stmt1.setString(10, exchng_cd);
					stmt1.setString(11, exchng_dt);
					stmt1.setString(12, exchng_rate);
					stmt1.setString(13, invoice_raised_in);
					stmt1.setString(14, gross_amt1);
					stmt1.setString(15, tax_amt);
					stmt1.setString(16, tax_cd);
					stmt1.setString(17, tax_dt);
					stmt1.setString(18, invoice_amt);
					stmt1.setString(19, net_payable);
					stmt1.setString(20, new_financial_year);
					stmt1.setString(21, remark1);
					stmt1.setString(22, remark2);
					stmt1.setString(23, exchg_rate_type);
					stmt1.setString(24, emp_cd);
					stmt1.setString(25, tcs_tds);
					stmt1.setString(26, tcs_amt);
					stmt1.setString(27, tcs_factor);
					stmt1.setString(28, invoice_no);
					stmt1.setString(29, invoice_id_seq);
					stmt1.setString(30, transportation_tariff);
					stmt1.setString(31, transportation_amount);
					stmt1.setString(32, tcs_struct_cd);
					stmt1.setString(33, tcs_struct_dt);
					stmt1.setString(34, tds_amt);
					stmt1.setString(35, tds_factor);
					stmt1.setString(36, tds_struct_cd);
					stmt1.setString(37, tds_struct_dt);
					stmt1.setString(38, marketing_margin);
					stmt1.setString(39, marketing_margin_amount);
					stmt1.setString(40, other_charges);
					stmt1.setString(41, other_charges_amount);
					stmt1.setString(42, new_invoice_seq);
					stmt1.setString(43, sug_qty);
					stmt1.setString(44, sug_percentage);
					stmt1.setString(45, inv_flag);
					stmt1.setString(46, sel_inv_no);
					stmt1.setString(47, criteri_formula);
					stmt1.setString(48, imb_amt);
					stmt1.setString(49, imb_qty);
					stmt1.setString(50, ship_or_pay_amt);
					stmt1.setString(51, ship_or_pay_qty);
					stmt1.setString(52, ovrun_amt);
					stmt1.setString(53, ovrun_qty);
					stmt1.setString(54, comp_cd);
					stmt1.setString(55, invoice_seq);
					stmt1.setString(56, financial_year);
					stmt1.setString(57, bu_state_tin);
					stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					if(!crdr_gen_type.equals("CRDR_IMB"))
					{
						//NEW VALUE FMS_INV_CRDR_REF
						query1="UPDATE FMS_INV_CRDR_REF SET ALLOC_QTY=?,SALE_PRICE=?,SALE_PRICE_UNIT=?,SALE_AMT=?,EXCHG_RATE_CD=?,EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'),EXCHG_RATE_VALUE=?,"
								+ "INVOICE_RAISED_IN=?,GROSS_AMT=?,TAX_AMT=?,TAX_STRUCT_CD=?,INVOICE_AMT=?,NET_PAYABLE_AMT=?,"
								+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,TCS_TDS=?,TCS_AMT=?,TCS_FACTOR=?,TDS_GROSS_AMT=?,TDS_GROSS_PERCENT=?,"
								+ "TRANSPORTATION_CHARGE=?,TRANSPORTATION_AMOUNT=?,MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,INVOICE_SEQ=?,FINANCIAL_YEAR=? "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, new_alloc_qty);
						stmt1.setString(++stcount, new_price);
						stmt1.setString(++stcount, price_cd);
						stmt1.setString(++stcount, new_gross_amt);
						stmt1.setString(++stcount, exchng_cd);
						stmt1.setString(++stcount, exchng_dt);
						stmt1.setString(++stcount, new_exchng_rate);
						stmt1.setString(++stcount, invoice_raised_in);
						stmt1.setString(++stcount, new_gross_amt1);
						stmt1.setString(++stcount, new_tax_amt);
						stmt1.setString(++stcount, new_tax_cd);
						stmt1.setString(++stcount, new_invoice_amt);
						stmt1.setString(++stcount, new_net_payable);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, tcs_tds);
						stmt1.setString(++stcount, new_tcs_amt);
						stmt1.setString(++stcount, new_tcs_factor);
						stmt1.setString(++stcount, new_tds_amt);
						stmt1.setString(++stcount, new_tds_factor);
						stmt1.setString(++stcount, new_transportation_tariff);
						stmt1.setString(++stcount, new_transportation_amount);
						stmt1.setString(++stcount, new_marketing_margin);
						stmt1.setString(++stcount, new_marketing_margin_amount);
						stmt1.setString(++stcount, new_other_charges);
						stmt1.setString(++stcount, new_other_charges_amount);
						stmt1.setString(++stcount, new_invoice_seq);
						stmt1.setString(++stcount, new_financial_year);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, bu_state_tin);
						stmt1.setString(++stcount, invoice_seq);
						stmt1.setString(++stcount, financial_year);
						stmt1.executeUpdate();
						stmt1.close();
					}
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+counterparty_abbr+" "+temp_deal_no+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+counterparty_abbr+" "+temp_deal_no+" not found for Modification!";
					msg_type="E";
				}
			}
			
			if(isOperated)
			{
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				stmt1.setString(3, invoice_seq);
				stmt1.setString(4, financial_year);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(tax_count>0)
				{
					query2="DELETE FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, bu_state_tin);
					stmt2.setString(3, invoice_seq);
					stmt2.setString(4, financial_year);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query2="INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						//stmt2.setString(3, invoice_seq);
						stmt2.setString(3, new_invoice_seq);
						stmt2.setString(4, new_financial_year);
						stmt2.setString(5, tax_cd);
						stmt2.setString(6, sub_tax_code[i]);
						stmt2.setString(7, tax_dt);
						stmt2.setString(8, sub_tax_struct[i]);
						stmt2.setString(9, sub_tax_amt[i]);
						stmt2.setString(10, sub_tax_base_amt[i]);
						stmt2.setString(11, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(!crdr_gen_type.equals("CRDR_IMB"))
				{
					//FOR NEW
					tax_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INV_CRDR_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					stmt1.setString(4, financial_year);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						tax_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(tax_count>0)
					{
						query2="DELETE FROM FMS_INV_CRDR_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					if(new_sub_tax_code!=null)
					{
						for(int i=0; i<new_sub_tax_code.length;i++)
						{
							query2="INSERT INTO FMS_INV_CRDR_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, new_financial_year);
							stmt2.setString(5, new_tax_cd);
							stmt2.setString(6, new_sub_tax_code[i]);
							stmt2.setString(7, tax_dt);
							stmt2.setString(8, new_sub_tax_struct[i]);
							stmt2.setString(9, new_sub_tax_amt[i]);
							stmt2.setString(10, new_sub_tax_base_amt[i]);
							stmt2.setString(11, emp_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
			}
			
			if(!invoice_seq.equals("") && crdr_gen_type.equals("CRDR_IMB"))
			{
				String appPath = request.getServletContext().getRealPath("");

				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}

				String sub_folder="unsigned_pdf";
				String sub_folder2="crdr_annexure";
				String folderPath=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(folderPath);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }
		        
		        if(!financial_year.equals(new_financial_year) && !financial_year.equals("") && !new_financial_year.equals(""))
		        {
		        	query4="UPDATE FMS_INV_FILE_DTL SET FINANCIAL_YEAR=?,INVOICE_SEQ=? "
		    				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
		    		stmt4=dbcon.prepareStatement(query4);
		    		stmt4.setString(1, new_financial_year);
		    		stmt4.setString(2, new_invoice_seq);
		    		stmt4.setString(3, comp_cd);
			    	stmt4.setString(4, bu_state_tin);
			    	stmt4.setString(5, invoice_seq);
			    	stmt4.setString(6, financial_year);
			    	stmt4.setString(7, "A");
			    	stmt4.executeUpdate();
		    		stmt4.close();
		    		
		    		String final_folder=comp_cd+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq;
		    		String new_final_folder=comp_cd+"_"+new_financial_year+"_"+bu_state_tin+"_"+new_invoice_seq;
		    		
		    		String realPath = request.getRealPath("");
		    		
			        File final_folderDir = new File(folderPath+File.separator+final_folder);
			        File new_final_folderDir = new File(folderPath+File.separator+new_final_folder);
			       
			        String canonicalPath_files = final_folderDir.getCanonicalPath();
			        if(!canonicalPath_files.startsWith(realPath))
			        {
			        	throw new IOException("Entry is outside of the target directory!");
			        }
			        else if(final_folderDir.exists())
			        {
			        	canonicalPath_files = new_final_folderDir.getCanonicalPath();
			        	if(!canonicalPath_files.startsWith(realPath))
				        {
				        	throw new IOException("Entry is outside of the target directory!");
				        }
			        	else if(!new_final_folderDir.exists())
			        	{
			        		boolean renameTo = final_folderDir.renameTo(new_final_folderDir);
			        	}
			        }
		        }
		        
		        String final_folder=comp_cd+"_"+new_financial_year+"_"+bu_state_tin+"_"+new_invoice_seq;
		        File final_folderDir = new File(folderPath+File.separator+final_folder);
		        String canonicalPath_final_folderDir= final_folderDir.getCanonicalPath();
		        
		        String file_name="";
		        String fileName="";
		        for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	// refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName();
				    if(!fileName.equals("") )
				    {
				    	file_name=fileName;
				    	
				    	if(!canonicalPath_final_folderDir.startsWith(appPath))
				        {
				    		throw new IOException("Entry is outside of the target directory!");
				        }
				        else if(!final_folderDir.exists())
				        {
				        	final_folderDir.mkdir();
				        }
				    	
				    	part.write(final_folderDir +File.separator+ fileName);
				    
				    	query4="INSERT INTO FMS_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
			        			+ "FILE_NAME,ENT_BY,ENT_DT,PDF_TYPE) "
			        			+ "VALUES(?,?,?,?,"
			        			+ "?,?,SYSDATE,?)";
			    		stmt4=dbcon.prepareStatement(query4);
			    		stmt4.setString(1, comp_cd);
				    	stmt4.setString(2, bu_state_tin);
				    	//stmt4.setString(3, invoice_seq);
				    	//stmt4.setString(4, financial_year);
				    	stmt4.setString(3, new_invoice_seq);
				    	stmt4.setString(4, new_financial_year);
				    	stmt4.setString(5, fileName);
				    	stmt4.setString(6, emp_cd);
				    	stmt4.setString(7, "A");
			    		stmt4.executeUpdate();
			    		
			    		stmt4.close();
				    } 
		        }
			}
			
			if(isOperated)
			{
				InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, period_start_dt+" - "+period_end_dt, invoice_due_dt,invoice_amt, opration,"","",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,sel_inv_no);
			}
			
			url = "../sales_invoice/frm_generate_sales_crdr.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+
					"&cargo_no="+cargo_no+
					"&inv_flag="+inv_flag+"&accroid="+accroid+"&crdr_gen_type="+crdr_gen_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Credit/Debit Note Detail Failed";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateCrDrInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCrDrInvoiceApproval()";
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
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no = deal_no;
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			
			if(activityFlag.equals("CHECK"))
			{
				String query="UPDATE FMS_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, bu_state_tin);
				stmt.setString(5, financial_year);
				stmt.setString(6, invoice_seq);
				stmt.setString(7, inv_flag);
				stmt.executeUpdate();
				
				stmt.close();
				msg = "Successful! - "+invdtl+" for "+counterparty_abbr+" "+temp_deal_no+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,sel_inv_no);
			}
			else if(activityFlag.equals("APPROVE"))
			{
				int count_inv_id_seq=0;
				String query = "SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
						 + "SELECT COUNT(*) AS CNT FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "	
			             + ") A";
				String temp_query=query;
				stmt=dbcon.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_no);
				stmt.setString(5, comp_cd);
				stmt.setString(6, financial_year);
				stmt.setString(7, bu_state_tin);
				stmt.setString(8, invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					query1="UPDATE FMS_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, invoice_no);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, bu_state_tin);
					stmt.setString(7, financial_year);
					stmt.setString(8, invoice_seq);
					stmt.setString(9, inv_flag);
					stmt.executeUpdate();
					
					stmt.close(); 
					msg = "Successful! - "+invdtl+" "+invoice_no+" for "+counterparty_abbr+" "+temp_deal_no+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
					
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(period_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(period_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"",contract_ref,"",invoice_raised_in,invoice_dt,invdtl,sel_inv_no);
				}
				else
				{
					msg = "Failed! - "+invdtl+" "+invoice_no+" is Already Allocated, Please assign different Credit/Debit No!";
					msg_type="E";
				}
			}
			
			url = "../sales_invoice/rpt_view_chk_aprv_crdr_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
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
			msg="Error In Exception! Credit/Debit Note Approval Failed";
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
	
	private void InsertUpdateLpInvoiceDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLpInvoiceDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
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
			String temp_period_start_dt = request.getParameter("temp_period_start_dt")==null?"":request.getParameter("temp_period_start_dt");
			String temp_period_end_dt = request.getParameter("temp_period_end_dt")==null?"":request.getParameter("temp_period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String exist_financial_year = request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String price = request.getParameter("price")==null?"0":request.getParameter("price");
			String price_cd = request.getParameter("price_cd")==null?"0":request.getParameter("price_cd");
			String gross_amt = request.getParameter("gross_amt")==null?"0":request.getParameter("gross_amt");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exchng_cd = request.getParameter("exchng_cd")==null?"":request.getParameter("exchng_cd");
			String exchng_dt = request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			String gross_amt1 = request.getParameter("gross_amt1")==null?"0":request.getParameter("gross_amt1");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String exchg_rate_type = request.getParameter("exchg_rate_type")==null?"":request.getParameter("exchg_rate_type");
			String tcs_tds = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			String tcs_amt = request.getParameter("tcs_amt")==null?"":request.getParameter("tcs_amt");
			String tcs_factor = request.getParameter("tcs_factor")==null?"":request.getParameter("tcs_factor");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			String sug_qty = request.getParameter("sug_qty")==null?"":request.getParameter("sug_qty");
			String sug_percentage = request.getParameter("sug_percent")==null?"":request.getParameter("sug_percent");
			
			String tcs_struct_cd = request.getParameter("tcs_struct_cd")==null?"":request.getParameter("tcs_struct_cd");
			String tcs_struct_dt = request.getParameter("tcs_struct_dt")==null?"":request.getParameter("tcs_struct_dt");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_factor = request.getParameter("tds_factor")==null?"":request.getParameter("tds_factor");
			String tds_struct_cd = request.getParameter("tds_struct_cd")==null?"":request.getParameter("tds_struct_cd");
			String tds_struct_dt = request.getParameter("tds_struct_dt")==null?"":request.getParameter("tds_struct_dt");
			
			String transportation_amount = request.getParameter("transportation_amount")==null?"":request.getParameter("transportation_amount");
			String transportation_tariff = request.getParameter("transportation_tariff")==null?"":request.getParameter("transportation_tariff");
			String marketing_margin_amount = request.getParameter("marketing_margin_amount")==null?"":request.getParameter("marketing_margin_amount");
			String marketing_margin = request.getParameter("marketing_margin")==null?"":request.getParameter("marketing_margin");
			String other_charges_amount = request.getParameter("other_charges_amount")==null?"":request.getParameter("other_charges_amount");
			String other_charges = request.getParameter("other_charges")==null?"":request.getParameter("other_charges");

			String ori_invoice_seq = request.getParameter("ori_invoice_seq")==null?"":request.getParameter("ori_invoice_seq");
			String ori_invoice_no = request.getParameter("ori_invoice_no")==null?"":request.getParameter("ori_invoice_no");
			String discount_days = request.getParameter("discount_days")==null?"":request.getParameter("discount_days");
			
			String cont_start_dt = request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String cont_end_dt = request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			String int_total_percentage = request.getParameter("int_total_percentage")==null?"":request.getParameter("int_total_percentage");
			String lp_financial_year = request.getParameter("lp_financial_year")==null?"":request.getParameter("lp_financial_year");
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			
			String temp_deal_no = deal_no;
			String invdtl="Sales";
			String subject_line = "Late Payment";
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

			String[] allocation_dt = request.getParameterValues("allocation_dt");
			String[] dailyExchngRate = request.getParameterValues("dailyExchngRate");
			String[] salesPrice = request.getParameterValues("salesPrice");
			String[] salesPriceCd = request.getParameterValues("salesPriceCd");
			String[] allocation_qty = request.getParameterValues("allocation_qty");
			String[] amount_usd = request.getParameterValues("amount_usd");
			String[] amount_inr = request.getParameterValues("amount_inr");
			
			String[] sub_tax_struct = request.getParameterValues("sub_tax_struct");
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			String[] disc_day_flag = request.getParameterValues("disc_day_flag");
			String[] storage_inventory = request.getParameterValues("storage_inventory");
			String[] storage_charge = request.getParameterValues("storage_charge");
			String[] user_define = request.getParameterValues("user_define");
			String[] storage_dt = request.getParameterValues("storage_dt");
			String[] offtake_qty = request.getParameterValues("offtake_qty");
			String rate_type = request.getParameter("rate_type")==null?"":request.getParameter("rate_type");
			
			String advance_adj_flag = request.getParameter("advance_adj_flag")==null?"":request.getParameter("advance_adj_flag");
			String adjAdvFlag = request.getParameter("adjAdvFlag")==null?"":request.getParameter("adjAdvFlag");
			String[] receipt_voucher = request.getParameterValues("receipt_voucher");
			String[] gross_adjust = request.getParameterValues("gross_adjust");
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			
			boolean isOperated=false;
			if(opration.equals("INSERT"))
			{
				int count=0;
				financial_year = dateUtil.getFinancialYear(invoice_dt);
				 	
				String query="SELECT COUNT(*) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND PLANT_SEQ=? AND BU_UNIT=? AND BU_STATE_TIN=? AND CARGO_NO=? "
						+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_FLAG=? AND REF_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, plant_seq);
				stmt.setString(7, bu_unit);
				stmt.setString(8, bu_state_tin);
				stmt.setString(9, cargo_no);
				stmt.setString(10, cont_start_dt);
				stmt.setString(11, cont_end_dt);
				stmt.setString(12, inv_flag);
				stmt.setString(13, ori_invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					String inv_seq="1";
					query2="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=?";
					stmt=dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						inv_seq = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();

					invoice_seq=inv_seq;
					new_invoice_seq=invoice_seq;
					
					int insCnt=0;
					
					query1="INSERT INTO FMS_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
							+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_DT,"
							+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
							+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
							+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
							+ "INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,REMARK_1,REMARK_2,EXCHG_RATE_TYPE,"
							+ "TCS_TDS,TCS_AMT,TCS_FACTOR,INVOICE_NO,INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
							+ "TCS_STRUCT_CD,TCS_EFF_DT,TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TDS_EFF_DT,"
							+ "MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,CARGO_NO,INV_FLAG,SUG_QTY,SUG_PERCENT,"
							+ "REF_NO,DISCOUNT_DAYS,INT_RATE) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,SYSDATE,?,?,?,?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, counterparty_cd);
					stmt1.setString(++insCnt, agmt_no);
					stmt1.setString(++insCnt, agmt_rev);
					stmt1.setString(++insCnt, cont_no);
					stmt1.setString(++insCnt, cont_rev);
					stmt1.setString(++insCnt, contract_type);
					stmt1.setString(++insCnt, bu_unit);
					stmt1.setString(++insCnt, bu_contact_person);
					stmt1.setString(++insCnt, bu_state_tin);
					stmt1.setString(++insCnt, plant_seq);
					stmt1.setString(++insCnt, contact_person);
					stmt1.setString(++insCnt, invoice_seq);
					stmt1.setString(++insCnt, invoice_dt);
					stmt1.setString(++insCnt, billing_cycle);
					stmt1.setString(++insCnt, cont_start_dt);
					stmt1.setString(++insCnt, cont_end_dt);
					stmt1.setString(++insCnt, invoice_due_dt);
					stmt1.setString(++insCnt, alloc_qty);
					stmt1.setString(++insCnt, price);
					stmt1.setString(++insCnt, price_cd);
					stmt1.setString(++insCnt, gross_amt);
					stmt1.setString(++insCnt, exchng_cd);
					stmt1.setString(++insCnt, exchng_dt);
					stmt1.setString(++insCnt, exchng_rate);
					stmt1.setString(++insCnt, invoice_raised_in);
					stmt1.setString(++insCnt, gross_amt1);
					stmt1.setString(++insCnt, tax_amt);
					stmt1.setString(++insCnt, tax_cd);
					stmt1.setString(++insCnt, tax_dt);
					stmt1.setString(++insCnt, invoice_amt);
					stmt1.setString(++insCnt, net_payable);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, financial_year);
					stmt1.setString(++insCnt, remark1);
					stmt1.setString(++insCnt, remark2);
					stmt1.setString(++insCnt, exchg_rate_type);
					stmt1.setString(++insCnt, tcs_tds);
					stmt1.setString(++insCnt, tcs_amt);
					stmt1.setString(++insCnt, tcs_factor);
					stmt1.setString(++insCnt, invoice_no);
					stmt1.setString(++insCnt, invoice_id_seq);
					stmt1.setString(++insCnt, transportation_tariff);
					stmt1.setString(++insCnt, transportation_amount);
					stmt1.setString(++insCnt, tcs_struct_cd);
					stmt1.setString(++insCnt, tcs_struct_dt);
					stmt1.setString(++insCnt, tds_amt);
					stmt1.setString(++insCnt, tds_factor);
					stmt1.setString(++insCnt, tds_struct_cd);
					stmt1.setString(++insCnt, tds_struct_dt);
					stmt1.setString(++insCnt, marketing_margin);
					stmt1.setString(++insCnt, marketing_margin_amount);
					stmt1.setString(++insCnt, other_charges);
					stmt1.setString(++insCnt, other_charges_amount);
					stmt1.setString(++insCnt, cargo_no);
					stmt1.setString(++insCnt, inv_flag);
					stmt1.setString(++insCnt, sug_qty);
					stmt1.setString(++insCnt, sug_percentage);
					stmt1.setString(++insCnt, ori_invoice_no);
					stmt1.setString(++insCnt, discount_days);
					stmt1.setString(++insCnt, int_total_percentage);
					stmt1.executeUpdate();
					isOperated=true;
					
					stmt1.close();
					msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				
				String temp_fiscal_yr=dateUtil.getFinancialYear(invoice_dt);
				
				if(!temp_fiscal_yr.equals(lp_financial_year) && !financial_year.equals("") && !lp_financial_year.equals(""))
				{
					new_financial_year=temp_fiscal_yr;
				
					String inv_seq="1";
					query2="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=?";
					stmt=dbcon.prepareStatement(query2);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						inv_seq = ""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					new_invoice_seq=inv_seq;
				}
				
				query2="SELECT COUNT(*) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CARGO_NO=? "
						+ "AND INV_FLAG=? AND REF_NO=? ";
				stmt=dbcon.prepareStatement(query2);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, plant_seq);
				stmt.setString(6, bu_unit);
				stmt.setString(7, contract_type);
				stmt.setString(8, bu_state_tin);
				stmt.setString(9, cont_start_dt);
				stmt.setString(10, cont_end_dt);
				stmt.setString(11, invoice_seq);
				//stmt.setString(12, financial_year);
				stmt.setString(12, lp_financial_year);
				stmt.setString(13, cargo_no);
				stmt.setString(14, inv_flag);
				stmt.setString(15, ori_invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					int upCnt = 0;
					query1="UPDATE FMS_INVOICE_MST SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, "
							+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), FREQ=?, DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
							+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
							+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
							+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, "
							+ "NET_PAYABLE_AMT=?,FINANCIAL_YEAR=?,REMARK_1=?,REMARK_2=?,EXCHG_RATE_TYPE=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,TCS_TDS=?,TCS_AMT=?,TCS_FACTOR=?,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=?,TRANSPORTATION_CHARGE=?,"
							+ "TRANSPORTATION_AMOUNT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "TDS_GROSS_AMT=?,TDS_GROSS_PERCENT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,INVOICE_SEQ=?,SUG_QTY=?,SUG_PERCENT=?,DISCOUNT_DAYS=?,INT_RATE=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++upCnt, bu_contact_person);
					stmt1.setString(++upCnt, contact_person);
					stmt1.setString(++upCnt, invoice_dt);
					stmt1.setString(++upCnt, billing_cycle);
					stmt1.setString(++upCnt, invoice_due_dt);
					stmt1.setString(++upCnt, alloc_qty);
					stmt1.setString(++upCnt, price);
					stmt1.setString(++upCnt, price_cd);
					stmt1.setString(++upCnt, gross_amt);
					stmt1.setString(++upCnt, exchng_cd);
					stmt1.setString(++upCnt, exchng_dt);
					stmt1.setString(++upCnt, exchng_rate);
					stmt1.setString(++upCnt, invoice_raised_in);
					stmt1.setString(++upCnt, gross_amt1);
					stmt1.setString(++upCnt, tax_amt);
					stmt1.setString(++upCnt, tax_cd);
					stmt1.setString(++upCnt, tax_dt);
					stmt1.setString(++upCnt, invoice_amt);
					stmt1.setString(++upCnt, net_payable);
					stmt1.setString(++upCnt, new_financial_year);
					stmt1.setString(++upCnt, remark1);
					stmt1.setString(++upCnt, remark2);
					stmt1.setString(++upCnt, exchg_rate_type);
					stmt1.setString(++upCnt, emp_cd);
					stmt1.setString(++upCnt, tcs_tds);
					stmt1.setString(++upCnt, tcs_amt);
					stmt1.setString(++upCnt, tcs_factor);
					stmt1.setString(++upCnt, invoice_no);
					stmt1.setString(++upCnt, invoice_id_seq);
					stmt1.setString(++upCnt, transportation_tariff);
					stmt1.setString(++upCnt, transportation_amount);
					stmt1.setString(++upCnt, tcs_struct_cd);
					stmt1.setString(++upCnt, tcs_struct_dt);
					stmt1.setString(++upCnt, tds_amt);
					stmt1.setString(++upCnt, tds_factor);
					stmt1.setString(++upCnt, tds_struct_cd);
					stmt1.setString(++upCnt, tds_struct_dt);
					stmt1.setString(++upCnt, marketing_margin);
					stmt1.setString(++upCnt, marketing_margin_amount);
					stmt1.setString(++upCnt, other_charges);
					stmt1.setString(++upCnt, other_charges_amount);
					stmt1.setString(++upCnt, new_invoice_seq);
					stmt1.setString(++upCnt, sug_qty);
					stmt1.setString(++upCnt, sug_percentage);
					stmt1.setString(++upCnt, discount_days);
					stmt1.setString(++upCnt, int_total_percentage);
					stmt1.setString(++upCnt, comp_cd);
					stmt1.setString(++upCnt, counterparty_cd);
					stmt1.setString(++upCnt, cont_no);
					stmt1.setString(++upCnt, agmt_no);
					stmt1.setString(++upCnt, plant_seq);
					stmt1.setString(++upCnt, bu_unit);
					stmt1.setString(++upCnt, contract_type);
					stmt1.setString(++upCnt, bu_state_tin);
					stmt1.setString(++upCnt, cont_start_dt);
					stmt1.setString(++upCnt, cont_end_dt);
					stmt1.setString(++upCnt, invoice_seq);
					//stmt1.setString(++upCnt, financial_year);
					stmt1.setString(++upCnt, lp_financial_year);
					stmt1.setString(++upCnt, cargo_no);
					stmt1.setString(++upCnt, inv_flag);
					stmt1.executeUpdate();
					isOperated=true;
					
					stmt1.close();
					msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" "+temp_deal_no+" not found for Modification!";
					msg_type="E";
				}
			}
			
			if(isOperated)
			{
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				stmt1.setString(3, invoice_seq);
				//stmt1.setString(4, financial_year);
				stmt1.setString(4, lp_financial_year);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(tax_count>0)
				{
					query2="DELETE FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, bu_state_tin);
					stmt2.setString(3, invoice_seq);
					//stmt2.setString(4, financial_year);
					stmt2.setString(4, lp_financial_year);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query2="INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						//stmt2.setString(3, invoice_seq);
						stmt2.setString(3, new_invoice_seq);
						stmt2.setString(4, financial_year);
						stmt2.setString(5, tax_cd);
						stmt2.setString(6, sub_tax_code[i]);
						stmt2.setString(7, tax_dt);
						stmt2.setString(8, sub_tax_struct[i]);
						stmt2.setString(9, sub_tax_amt[i]);
						stmt2.setString(10, sub_tax_base_amt[i]);
						stmt2.setString(11, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				/*if(exchg_rate_type.equals("A"))
				{
					if(allocation_dt != null && price_cd.equals("2"))
					{
						for(int i=0;i<allocation_dt.length; i++)
						{
							int count=0;
							query2="SELECT COUNT(*) "
									+ "FROM FMS_INVOICE_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
									+ "AND ALLOCATION_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? ";
							stmt=dbcon.prepareStatement(query2);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, cont_no);
							stmt.setString(4, agmt_no);
							stmt.setString(5, plant_seq);
							stmt.setString(6, bu_unit);
							stmt.setString(7, contract_type);
							stmt.setString(8, bu_state_tin);
							stmt.setString(9, invoice_seq);
							//stmt.setString(10, financial_year);
							stmt.setString(10, exist_financial_year);
							stmt.setString(11, allocation_dt[i]);
							stmt.setString(12, cargo_no);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(count > 0)
							{
								query1="UPDATE FMS_INVOICE_DTL SET DAILY_QTY=?,SALE_PRICE=?,"
										+ "SALE_PRICE_UNIT=?,AMT_INR=?,AMT_USD=?,"
										+ "EXCHG_RATE_CD=?,EXCHG_RATE_VALUE=?,"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,INVOICE_SEQ=?,FINANCIAL_YEAR=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
										+ "AND ALLOCATION_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? ";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(1, allocation_qty[i]);
								stmt1.setString(2, salesPrice[i]);
								stmt1.setString(3, salesPriceCd[i]);
								stmt1.setString(4, amount_inr[i]);
								stmt1.setString(5, amount_usd[i]);
								stmt1.setString(6, exchng_cd);
								stmt1.setString(7, dailyExchngRate[i]);
								stmt1.setString(8, emp_cd);
								stmt1.setString(9, new_invoice_seq);
								stmt1.setString(10, financial_year);
								stmt1.setString(11, comp_cd);
								stmt1.setString(12, counterparty_cd);
								stmt1.setString(13, cont_no);
								stmt1.setString(14, agmt_no);
								stmt1.setString(15, plant_seq);
								stmt1.setString(16, bu_unit);
								stmt1.setString(17, contract_type);
								stmt1.setString(18, bu_state_tin);
								stmt1.setString(19, invoice_seq);
								//stmt1.setString(20, financial_year);
								stmt1.setString(20, exist_financial_year);
								stmt1.setString(21, allocation_dt[i]);
								stmt1.setString(22, cargo_no);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
							else
							{
								query2="INSERT INTO FMS_INVOICE_DTL(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
										+ "CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,ALLOCATION_DT,DAILY_QTY,SALE_PRICE,SALE_PRICE_UNIT,"
										+ "AMT_INR,AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_VALUE,ENT_BY,ENT_DT,CARGO_NO) "
										+ "VALUES(?,?,?,?,?,?,?,"
										+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
										+ "?,?,?,?,?,?,"
										+ "?,?,SYSDATE,?)";
								stmt1=dbcon.prepareStatement(query2);
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
								stmt1.setString(11, plant_seq);
								//stmt1.setString(12, invoice_seq);
								stmt1.setString(12, new_invoice_seq);
								stmt1.setString(13, allocation_dt[i]);
								stmt1.setString(14, allocation_qty[i]);
								stmt1.setString(15, salesPrice[i]);
								stmt1.setString(16, salesPriceCd[i]);
								stmt1.setString(17, amount_inr[i]);
								stmt1.setString(18, amount_usd[i]);
								stmt1.setString(19, exchng_cd);
								stmt1.setString(20, dailyExchngRate[i]);
								stmt1.setString(21, emp_cd);
								stmt1.setString(22, cargo_no);
								stmt1.executeUpdate();
								
								stmt1.close();
							}
						}
					}
				}
				
				//INSERT/UPDATE STORAGE DETAIL 
				if(inv_flag.equals("ST"))
				{
					int st_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INV_STORAGE_CRG_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					//stmt1.setString(4, financial_year);
					stmt1.setString(4, exist_financial_year);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						st_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(st_count>0)
					{
						query2="DELETE FROM FMS_INV_STORAGE_CRG_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						//stmt2.setString(4, financial_year);
						stmt2.setString(4, exist_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					if(storage_dt != null)
					{
						for(int i=0;i<storage_dt.length; i++)
						{
							String st_rate="";
							if(rate_type.equals("U"))
							{
								st_rate=user_define[i];
							}
							else
							{
								st_rate=storage_charge[i];
							}
							
							query2="INSERT INTO FMS_INV_STORAGE_CRG_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "STORAGE_DT,OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT) "
									+ "VALUES(?,?,?,?,"
									+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, storage_dt[i]);
							stmt2.setString(6, storage_inventory[i]);
							stmt2.setString(7, offtake_qty[i]);
							stmt2.setString(8, st_rate);
							stmt2.setString(9, rate_type);
							stmt2.setString(10, disc_day_flag[i]);
							stmt2.executeUpdate();
							stmt2.close();
						}
					}
				}
				
				//Advance adjust
				if(advance_adj_flag.equals("Y") && adjAdvFlag.equals("Y"))
				{
					int adv_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_INV_ADV_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, invoice_seq);
					//stmt1.setString(4, financial_year);
					stmt1.setString(4, exist_financial_year);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						adv_count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(adv_count>0)
					{
						query2="DELETE FROM FMS_INV_ADV_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, bu_state_tin);
						stmt2.setString(3, invoice_seq);
						//stmt2.setString(4, financial_year);
						stmt2.setString(4, exist_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					if(receipt_voucher!=null)
					{
						for(int i=0; i<receipt_voucher.length;i++)
						{
							query2="INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,SYSDATE)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, receipt_voucher[i]);
							stmt2.setString(6, "GROSS");
							stmt2.setString(7, gross_adjust[i]);
							stmt2.setString(8, emp_cd);
							stmt2.executeUpdate();
							stmt2.close();
							
							if(sub_tax_code!=null)
							{
								for(int j=0; j<sub_tax_code.length;j++)
								{
									String taxAbbr="";
									String taxStruct=sub_tax_struct[j];
									if(!taxStruct.equals(""))
									{
										String[] split=taxStruct.split(" ");
										taxAbbr=split[0];
									}
									
									String[] adv_tax = request.getParameterValues(taxAbbr+"_adjust");
									if(adv_tax!=null)
									{
										query2="INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
												+ "SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT) "
												+ "VALUES(?,?,?,?,"
												+ "?,?,?,?,SYSDATE)";
										stmt2=dbcon.prepareStatement(query2);
										stmt2.setString(1, comp_cd);
										stmt2.setString(2, bu_state_tin);
										//stmt2.setString(3, invoice_seq);
										stmt2.setString(3, new_invoice_seq);
										stmt2.setString(4, financial_year);
										stmt2.setString(5, receipt_voucher[i]);
										stmt2.setString(6, taxAbbr.toUpperCase());
										stmt2.setString(7, adv_tax[i]);
										stmt2.setString(8, emp_cd);
										stmt2.executeUpdate();
										stmt2.close();
									}
								}
							}
						}
					}
				}*/
			}
			
			InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, cont_start_dt+" - "+cont_end_dt, invoice_due_dt,invoice_amt, opration,"","S-LP",contract_ref,subject_line,invoice_raised_in,invoice_dt,invdtl,ori_invoice_no);
			
			String filenm="frm_generate_lp_invoice.jsp";
			/*if(inv_flag.equals("UG"))
			{
				filenm="frm_generate_ltcora_sug_invoice.jsp";
			}
			else if(inv_flag.equals("ST"))
			{
				filenm="frm_generate_ltcora_storage_invoice.jsp";
			}*/
			url = "../sales_invoice/"+filenm+"?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+
					"&temp_period_start_dt="+temp_period_start_dt+"&temp_period_end_dt="+temp_period_end_dt+"&cargo_no="+cargo_no+
					"&inv_flag="+inv_flag+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Sales/LTCORA Invoice Detail Failed";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	

	private void InsertUpdateLpInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLpInvoiceApproval()";
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
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_amt = request.getParameter("invoice_amt")==null?"":request.getParameter("invoice_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			
			String cont_start_dt = request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String cont_end_dt = request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			
			String ori_inv_seq = request.getParameter("ori_inv_seq")==null?"":request.getParameter("ori_inv_seq");
			String ori_inv_no = request.getParameter("ori_inv_no")==null?"":request.getParameter("ori_inv_no");
			String ori_inv_flag = request.getParameter("ori_inv_flag")==null?"":request.getParameter("ori_inv_flag");
			String exist_fin_yr = request.getParameter("exist_fin_yr")==null?"":request.getParameter("exist_fin_yr");
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			String temp_deal_no = deal_no;
			String invdtl="Sales";
			String subject_line = "Late Payment";
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
				String query="UPDATE FMS_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? AND REF_NO=? ";
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
				stmt.setString(11, cont_start_dt);
				stmt.setString(12, cont_end_dt);
				stmt.setString(13, financial_year);
				stmt.setString(14, invoice_seq);
				stmt.setString(15, cargo_no);
				stmt.setString(16, inv_flag);
				stmt.setString(17, ori_inv_no);
				stmt.executeUpdate();
				
				stmt.close();
				msg = "Successful! - "+invdtl+" Late Payment Invoice for "+counterparty_abbr+" "+temp_deal_no+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(cont_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(cont_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"S-LP",contract_ref,subject_line,invoice_raised_in,invoice_dt,invdtl,ori_inv_no);
			}
			else if(activityFlag.equals("APPROVE"))
			{
				int count_inv_id_seq=0;
				
				String query = "SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
						 + "SELECT COUNT(*) AS CNT FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "	
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + "UNION ALL "
			             + "SELECT COUNT(*) AS CNT FROM FMS_DLNG_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? AND INVOICE_NO=? "
			             + ") A";
				String temp_query=query;
				stmt=dbcon.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_no);
				stmt.setString(5, comp_cd);
				stmt.setString(6, financial_year);
				stmt.setString(7, bu_state_tin);
				stmt.setString(8, invoice_no);
				stmt.setString(9, comp_cd);
				stmt.setString(10, financial_year);
				stmt.setString(11, bu_state_tin);
				stmt.setString(12, invoice_no);
				stmt.setString(13, comp_cd);
				stmt.setString(14, financial_year);
				stmt.setString(15, bu_state_tin);
				stmt.setString(16, invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					query1="UPDATE FMS_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
							+ "INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? AND REF_NO=? ";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, invoice_no);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, counterparty_cd);
					stmt.setString(7, cont_no);
					stmt.setString(8, agmt_no);
					stmt.setString(9, plant_seq);
					stmt.setString(10, bu_unit);
					stmt.setString(11, contract_type);
					stmt.setString(12, bu_state_tin);
					stmt.setString(13, cont_start_dt);
					stmt.setString(14, cont_end_dt);
					stmt.setString(15, financial_year);
					stmt.setString(16, invoice_seq);
					stmt.setString(17, cargo_no);
					stmt.setString(18, inv_flag);
					stmt.setString(19, ori_inv_no);
					stmt.executeUpdate();
					
					stmt.close();
					msg = "Successful! - "+invdtl+" Late Payment Invoice "+invoice_no+" for "+counterparty_abbr+" "+temp_deal_no+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
					
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_no, dateUtil.getDateFormatDD_MOM_YY(cont_start_dt)+" - "+dateUtil.getDateFormatDD_MOM_YY(cont_end_dt), invoice_due_dt,invoice_amt, activityFlag,rd,"S-LP",contract_ref,subject_line,invoice_raised_in,invoice_dt,invdtl,ori_inv_no);
				}
				else
				{
					msg = "Failed! - "+invdtl+" Late Payment Invoice "+invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../sales_invoice/rpt_view_lp_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&cargo_no="+cargo_no+"&accroid="+accroid+
					"&inv_flag="+inv_flag+"&ori_inv_seq="+ori_inv_seq+"&financial_year="+financial_year+"&bu_state_tin="+bu_state_tin+"&invoice_seq="+invoice_seq+
					"&cont_start_dt="+cont_start_dt+"&cont_end_dt="+cont_end_dt+"&ori_inv_no="+ori_inv_no+"&ori_inv_flag="+ori_inv_flag+"&exist_fin_yr="+exist_fin_yr+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
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
	
	private void DeleteCRDRAnnexure(HttpServletRequest request) throws SQLException
	{
		String function_nm="DeleteCRDRAnnexure()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String crdr_gen_type =request.getParameter("crdr_gen_type")==null?"":request.getParameter("crdr_gen_type");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String mapping_id = request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			
			String att_file_name = request.getParameter("att_file_name")==null?"":request.getParameter("att_file_name");
			String annexure_folder = request.getParameter("annexure_folder")==null?"":request.getParameter("annexure_folder");
			
			String appPath = request.getServletContext().getRealPath("");

			String main_folder="";
			if(!comp_cd.equals(""))
			{
				main_folder=CommonVariable.work_dir+comp_cd;
			}

	        String final_folder=annexure_folder;
	        
			
	        int count=0;
	        queryString="SELECT COUNT(*) "
					+ "FROM FMS_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
					+ "AND PDF_TYPE=? AND FILE_NAME=?";
	        stmt=dbcon.prepareStatement(queryString);
	        stmt.setString(1, comp_cd);
	        stmt.setString(2, bu_state_tin);
	        stmt.setString(3, invoice_seq);
	        stmt.setString(4, financial_year);
	        stmt.setString(5, "A");
	        stmt.setString(6, att_file_name);
	        rset=stmt.executeQuery();
	    	if(rset.next())
	    	{
	    		count=rset.getInt(1);
	    	}
	    	rset.close();
	    	stmt.close();
	    	
	    	if(count > 0)
	    	{
	    		File final_file = new File(appPath+File.separator+main_folder+""+CommonVariable.crdr_annexure_path+""+final_folder+File.separator+att_file_name);
	    		String canonicalPath_final_file = final_file.getCanonicalPath();
	    		
	    		if(!canonicalPath_final_file.startsWith(appPath))
		        {
		        	throw new IOException("Entry is outside of the target directory!");
		        }
		        else if(final_file.exists())
	    		{
		        	boolean isDeleted = final_file.delete();
	    			
	    			query1="DELETE FROM FMS_INV_FILE_DTL "
	    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	     	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	     	        		+ "AND PDF_TYPE=? AND FILE_NAME=?";
	    			stmt1=dbcon.prepareStatement(query1);
	    			stmt1.setString(1, comp_cd);
	    			stmt1.setString(2, bu_state_tin);
	    			stmt1.setString(3, invoice_seq);
	    			stmt1.setString(4, financial_year);
	    			stmt1.setString(5, "A");
	    			stmt1.setString(6, att_file_name);
	    			stmt1.executeQuery();
	    			
	    			stmt1.close();
	    			
	    			msg = "Successful! - Annexure File Deleted Successfully!";
					msg_type="S";
	    		}
	    		else
	    		{
	    			msg = "Failed! - Annexure File Deletion Falied!";
					msg_type="E";
	    		}
	    	}
	    	else
    		{
    			msg = "Failed! - Annexure File Deletion Falied!";
				msg_type="E";
    		}
	    
	    		
	    	url = "../sales_invoice/frm_generate_sales_crdr.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+
					"&cargo_no="+cargo_no+
					"&inv_flag="+inv_flag+"&accroid="+accroid+"&crdr_gen_type="+crdr_gen_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Delete FFlow Invoice Annexure Failed";
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
	
	private void InsertUpdateSalesInvoiceChangeAction(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateSalesInvoiceChangeAction()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String action_flag = request.getParameter("action_flag")==null?"":request.getParameter("action_flag");
			String change_type = request.getParameter("change_type")==null?"":request.getParameter("change_type");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String invtype = request.getParameter("invtype")==null?"":request.getParameter("invtype");
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			if(invtype.equals("P"))
			{
				if(action_flag.equals("R"))
				{
					int isExist=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND INVOICE_TYPE=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_FFLOW_INV_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
							+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE AND A.INVOICE_TYPE=B.INVOICE_TYPE)";	
			        stmt=dbcon.prepareStatement(queryString);
			        stmt.setString(1, comp_cd);
			        stmt.setString(2, bu_state_tin);
			        stmt.setString(3, invoice_seq);
			        stmt.setString(4, financial_year);
			        stmt.setString(5, segment);
			        stmt.setString(6, change_type);
			        stmt.setString(7, "R");
			        stmt.setString(8, invoice_type);
			        rset=stmt.executeQuery();
			        rset=stmt.executeQuery();
			    	if(rset.next())
			    	{
			    		isExist=rset.getInt(1);
			    	}
			    	rset.close();
			    	stmt.close();
			    	
			    	if(isExist==0)
			    	{
						queryString="SELECT MAX(NVL(SEQ_NO,0)) "
								+ "FROM FMS_FFLOW_INV_CHANGE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND SEGMENT=? AND CHANGE_TYPE=? AND INVOICE_TYPE=? ";
				        stmt=dbcon.prepareStatement(queryString);
				        stmt.setString(1, comp_cd);
				        stmt.setString(2, bu_state_tin);
				        stmt.setString(3, invoice_seq);
				        stmt.setString(4, financial_year);
				        stmt.setString(5, segment);
				        stmt.setString(6, change_type);
				        stmt.setString(7, invoice_type);
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
				    	
				    	if(!seq_no.equals(""))
				    	{
					    	query2="INSERT INTO FMS_FFLOW_INV_CHANGE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "SEGMENT,CHANGE_TYPE,SEQ_NO,FLAG,REQ_BY,REQ_DT,INVOICE_TYPE) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE,?)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							stmt2.setString(3, invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, segment);
							stmt2.setString(6, change_type);
							stmt2.setString(7, seq_no);
							stmt2.setString(8, action_flag);
							stmt2.setString(9, emp_cd);
							stmt2.setString(10, invoice_type);
							stmt2.executeUpdate();
							
							stmt2.close();
							
							msg = "Successful! - Re-Print PDF Request for "+invoice_no+" Generated Successfully!";
							msg_type="S";
				    	}
				    	else
				    	{
				    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" Generation Failed!";
							msg_type="E";
				    	}
			    	}
			    	else
			    	{
			    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" Already Generated!";
						msg_type="E";
			    	}
				}
				else if(action_flag.equals("A") || action_flag.equals("X"))
				{
					String appPath = request.getServletContext().getRealPath("");
	
					String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_CHANGE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND SEQ_NO=? AND INVOICE_TYPE=? ";
			        stmt=dbcon.prepareStatement(queryString);
			        stmt.setString(1, comp_cd);
			        stmt.setString(2, bu_state_tin);
			        stmt.setString(3, invoice_seq);
			        stmt.setString(4, financial_year);
			        stmt.setString(5, segment);
			        stmt.setString(6, change_type);
			        stmt.setString(7, "R");
			        stmt.setString(8, seq_no);
			        stmt.setString(9, invoice_type);
			        rset=stmt.executeQuery();
			    	if(rset.next())
			    	{
			    		count=rset.getInt(1);
			    	}
			    	rset.close();
			    	stmt.close();
			    	
			    	if(count > 0)
			    	{
			    		query2="UPDATE FMS_FFLOW_INV_CHANGE_DTL SET FLAG=?,APRV_BY=?,APRV_DT=SYSDATE "
			    				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND SEQ_NO=? AND INVOICE_TYPE=? ";
			    		stmt2=dbcon.prepareStatement(query2);
			    		stmt2.setString(1, action_flag);
				        stmt2.setString(2, emp_cd);
				        stmt2.setString(3, comp_cd);
				        stmt2.setString(4, bu_state_tin);
				        stmt2.setString(5, invoice_seq);
				        stmt2.setString(6, financial_year);
				        stmt2.setString(7, segment);
				        stmt2.setString(8, change_type);
				        stmt2.setString(9, "R");
				        stmt2.setString(10, seq_no);
				        stmt2.setString(11, invoice_type);
				        int upcount= stmt2.executeUpdate();
						stmt2.close();
						
						if(upcount > 0 && action_flag.equals("A"))
						{
							int reqCount=0;
							query3="SELECT COUNT(*) "
									+ "FROM FMS_FFLOW_INV_CHANGE_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
									+ "AND SEGMENT=? AND CHANGE_TYPE=? "
									+ "AND FLAG IN (?,?) AND INVOICE_TYPE=? ";
					        stmt3=dbcon.prepareStatement(query3);
					        stmt3.setString(1, comp_cd);
					        stmt3.setString(2, bu_state_tin);
					        stmt3.setString(3, invoice_seq);
					        stmt3.setString(4, financial_year);
					        stmt3.setString(5, segment);
					        stmt3.setString(6, change_type);
					        stmt3.setString(7, "A");
					        stmt3.setString(8, "P");
					        stmt3.setString(9, invoice_type);
					        rset3=stmt3.executeQuery();
					    	if(rset3.next())
					    	{
					    		reqCount=rset3.getInt(1);
					    	}
					    	rset3.close();
					    	stmt3.close();
							
							queryString="SELECT PDF_TYPE,FILE_NAME "
									+ "FROM FMS_FFLOW_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? AND PDF_TYPE IN ('O','D','T') ";
							stmt=dbcon.prepareStatement(queryString);
						    stmt.setString(1, comp_cd);
						    stmt.setString(2, bu_state_tin);
						    stmt.setString(3, invoice_seq);
						    stmt.setString(4, financial_year);
						    stmt.setString(5, invoice_type);
						    rset=stmt.executeQuery();
					    	while(rset.next())
					    	{
					    		String pdf_type=rset.getString(1)==null?"":rset.getString(1);
					    		String pdf_file_name=rset.getString(2)==null?"":rset.getString(2);
					    		
					    		boolean action_done=false;
					    		if(reqCount==1)
					    		{
						    		String unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.freeflow_inv_path+""+pdf_file_name;
						    		String signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_freeflow_inv_path+""+pdf_file_name;
						    		
						    		File unsigned_file_exist = new File(unsigned_file_path);
						    		File signed_file_exist = new File(signed_file_path);
						    		String canonicalPath_unsigned_file = unsigned_file_exist.getCanonicalPath();
						    		String canonicalPath_signed_file = signed_file_exist.getCanonicalPath();
						    		
						    		String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
						    		String[] splitSys = sysdateWithTime.split(" ");
									String datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
									
									String new_pdf_file_name=pdf_file_name.replace(".pdf", "")+"_"+datetime+".pdf";
									
									String new_unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.freeflow_inv_path+""+new_pdf_file_name;
						    		String new_signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_freeflow_inv_path+""+new_pdf_file_name;
									
									File new_unsigned_file = new File(new_unsigned_file_path);
						    		File new_signed_file = new File(new_signed_file_path);
						    		
						    		if(!canonicalPath_unsigned_file.startsWith(appPath))
							        {
							        	throw new IOException("Entry is outside of the target directory!");
							        }
							        else if(unsigned_file_exist.exists())
						    		{
							        	Path temp = Files.copy(Paths.get(unsigned_file_path),Paths.get(new_unsigned_file_path),StandardCopyOption.REPLACE_EXISTING); 
						    		}
						    		
						    		if(!canonicalPath_signed_file.startsWith(appPath))
							        {
							        	throw new IOException("Entry is outside of the target directory!");
							        }
							        else if(signed_file_exist.exists())
						    		{
							        	Path temp = Files.copy(Paths.get(signed_file_path),Paths.get(new_signed_file_path),StandardCopyOption.REPLACE_EXISTING); 
						    		}
						    		
					    			query1="UPDATE FMS_FFLOW_INV_FILE_DTL SET PDF_TYPE=?,FILE_NAME=? "
					    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
											+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? AND PDF_TYPE=? ";
					    			stmt1=dbcon.prepareStatement(query1);
					    			stmt1.setString(1, "1"+pdf_type);
									stmt1.setString(2, new_pdf_file_name);
								    stmt1.setString(3, comp_cd);
								    stmt1.setString(4, bu_state_tin);
								    stmt1.setString(5, invoice_seq);
								    stmt1.setString(6, financial_year);
								    stmt1.setString(7, invoice_type);
								    stmt1.setString(8, pdf_type);
								    stmt1.executeUpdate();
									stmt1.close();
									
									action_done=true;
					    		}
					    		else if(reqCount > 1)
					    		{
					    			query1="DELETE FROM FMS_FFLOW_INV_FILE_DTL "
					    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
											+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? AND PDF_TYPE=? ";
					    			stmt1=dbcon.prepareStatement(query1);
					    			stmt1.setString(1, comp_cd);
								    stmt1.setString(2, bu_state_tin);
								    stmt1.setString(3, invoice_seq);
								    stmt1.setString(4, financial_year);
								    stmt1.setString(5, invoice_type);
								    stmt1.setString(6, pdf_type);
								    stmt1.executeUpdate();
									stmt1.close();
									
									action_done=true;
					    		}
					    		
								if(!pdf_type.equals("") && action_done)
								{
									int cont=0;
									query1="UPDATE FMS_FFLOW_INV_MST SET ";
										if(pdf_type.equals("O"))
										{
											query1+= "PRINT_BY_ORI=NULL,PRINT_DT_ORI=NULL ";
										}
										else if(pdf_type.equals("T"))
										{
											query1+= "PRINT_BY_TRI=NULL,PRINT_DT_TRI=NULL ";
										}
										else if(pdf_type.equals("D"))
										{
											query1+= "PRINT_BY_DUP=NULL,PRINT_DT_DUP=NULL ";
										}
										query1+= "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
											+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INVOICE_TYPE=? ";
									stmt1=dbcon.prepareStatement(query1);
									stmt1.setString(++cont, comp_cd);
									stmt1.setString(++cont, financial_year);
									stmt1.setString(++cont, invoice_seq);
									stmt1.setString(++cont, bu_state_tin);
									stmt1.setString(++cont, invoice_type);
									stmt1.executeUpdate();
									stmt1.close();
								}
					    	}
					    	rset.close();
					    	stmt.close();
						}
						
						msg = "Successful! - Re-Print PDF Request for "+invoice_no+" "+(action_flag.equals("A")?"Approved":"Rejected")+" Successfully!";
						msg_type="S";
			    	}
			    	else
			    	{
			    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" "+(action_flag.equals("A")?"Approval":"Rejection")+" Failed!";
						msg_type="E";
			    	}
				}
			}
			else
			{
				if(action_flag.equals("R"))
				{
					int isExist=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_INVOICE_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
							+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE)";	
			        stmt=dbcon.prepareStatement(queryString);
			        stmt.setString(1, comp_cd);
			        stmt.setString(2, bu_state_tin);
			        stmt.setString(3, invoice_seq);
			        stmt.setString(4, financial_year);
			        stmt.setString(5, segment);
			        stmt.setString(6, change_type);
			        stmt.setString(7, "R");
			        rset=stmt.executeQuery();
			        rset=stmt.executeQuery();
			    	if(rset.next())
			    	{
			    		isExist=rset.getInt(1);
			    	}
			    	rset.close();
			    	stmt.close();
			    	
			    	if(isExist==0)
			    	{
						queryString="SELECT MAX(NVL(SEQ_NO,0)) "
								+ "FROM FMS_INVOICE_CHANGE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND SEGMENT=? AND CHANGE_TYPE=?";
				        stmt=dbcon.prepareStatement(queryString);
				        stmt.setString(1, comp_cd);
				        stmt.setString(2, bu_state_tin);
				        stmt.setString(3, invoice_seq);
				        stmt.setString(4, financial_year);
				        stmt.setString(5, segment);
				        stmt.setString(6, change_type);
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
				    	
				    	if(!seq_no.equals(""))
				    	{
					    	query2="INSERT INTO FMS_INVOICE_CHANGE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "SEGMENT,CHANGE_TYPE,SEQ_NO,FLAG,REQ_BY,REQ_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, bu_state_tin);
							stmt2.setString(3, invoice_seq);
							stmt2.setString(4, financial_year);
							stmt2.setString(5, segment);
							stmt2.setString(6, change_type);
							stmt2.setString(7, seq_no);
							stmt2.setString(8, action_flag);
							stmt2.setString(9, emp_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
							
							msg = "Successful! - Re-Print PDF Request for "+invoice_no+" Generated Successfully!";
							msg_type="S";
				    	}
				    	else
				    	{
				    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" Generation Failed!";
							msg_type="E";
				    	}
			    	}
			    	else
			    	{
			    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" Already Generated!";
						msg_type="E";
			    	}
				}
				else if(action_flag.equals("A") || action_flag.equals("X"))
				{
					String appPath = request.getServletContext().getRealPath("");
	
					String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_CHANGE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND SEQ_NO=? ";
			        stmt=dbcon.prepareStatement(queryString);
			        stmt.setString(1, comp_cd);
			        stmt.setString(2, bu_state_tin);
			        stmt.setString(3, invoice_seq);
			        stmt.setString(4, financial_year);
			        stmt.setString(5, segment);
			        stmt.setString(6, change_type);
			        stmt.setString(7, "R");
			        stmt.setString(8, seq_no);
			        rset=stmt.executeQuery();
			    	if(rset.next())
			    	{
			    		count=rset.getInt(1);
			    	}
			    	rset.close();
			    	stmt.close();
			    	
			    	if(count > 0)
			    	{
			    		query2="UPDATE FMS_INVOICE_CHANGE_DTL SET FLAG=?,APRV_BY=?,APRV_DT=SYSDATE "
			    				+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND SEQ_NO=? ";
			    		stmt2=dbcon.prepareStatement(query2);
			    		stmt2.setString(1, action_flag);
				        stmt2.setString(2, emp_cd);
				        stmt2.setString(3, comp_cd);
				        stmt2.setString(4, bu_state_tin);
				        stmt2.setString(5, invoice_seq);
				        stmt2.setString(6, financial_year);
				        stmt2.setString(7, segment);
				        stmt2.setString(8, change_type);
				        stmt2.setString(9, "R");
				        stmt2.setString(10, seq_no);
				        int upcount= stmt2.executeUpdate();
						stmt2.close();
						
						if(upcount > 0 && action_flag.equals("A"))
						{
							int reqCount=0;
							query3="SELECT COUNT(*) "
									+ "FROM FMS_INVOICE_CHANGE_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
									+ "AND SEGMENT=? AND CHANGE_TYPE=? "
									+ "AND FLAG IN (?,?) ";
					        stmt3=dbcon.prepareStatement(query3);
					        stmt3.setString(1, comp_cd);
					        stmt3.setString(2, bu_state_tin);
					        stmt3.setString(3, invoice_seq);
					        stmt3.setString(4, financial_year);
					        stmt3.setString(5, segment);
					        stmt3.setString(6, change_type);
					        stmt3.setString(7, "A");
					        stmt3.setString(8, "P");
					        rset3=stmt3.executeQuery();
					    	if(rset3.next())
					    	{
					    		reqCount=rset3.getInt(1);
					    	}
					    	rset3.close();
					    	stmt3.close();
							
							queryString="SELECT PDF_TYPE,FILE_NAME "
									+ "FROM FMS_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE IN ('O','D','T') ";
							stmt=dbcon.prepareStatement(queryString);
						    stmt.setString(1, comp_cd);
						    stmt.setString(2, bu_state_tin);
						    stmt.setString(3, invoice_seq);
						    stmt.setString(4, financial_year);
						    rset=stmt.executeQuery();
					    	while(rset.next())
					    	{
					    		String pdf_type=rset.getString(1)==null?"":rset.getString(1);
					    		String pdf_file_name=rset.getString(2)==null?"":rset.getString(2);
					    		
					    		boolean action_done=false;
					    		if(reqCount==1)
					    		{
						    		String unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.sales_inv_path+""+pdf_file_name;
						    		String signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_sales_inv_path+""+pdf_file_name;
						    		
						    		File unsigned_file_exist = new File(unsigned_file_path);
						    		File signed_file_exist = new File(signed_file_path);
						    		String canonicalPath_unsigned_file = unsigned_file_exist.getCanonicalPath();
						    		String canonicalPath_signed_file = signed_file_exist.getCanonicalPath();
						    		
						    		String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
						    		String[] splitSys = sysdateWithTime.split(" ");
									String datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
									
									String new_pdf_file_name=pdf_file_name.replace(".pdf", "")+"_"+datetime+".pdf";
									
									String new_unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.sales_inv_path+""+new_pdf_file_name;
						    		String new_signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_sales_inv_path+""+new_pdf_file_name;
									
									File new_unsigned_file = new File(new_unsigned_file_path);
						    		File new_signed_file = new File(new_signed_file_path);
						    		
						    		if(!canonicalPath_unsigned_file.startsWith(appPath))
							        {
							        	throw new IOException("Entry is outside of the target directory!");
							        }
							        else if(unsigned_file_exist.exists())
						    		{
							        	Path temp = Files.copy(Paths.get(unsigned_file_path),Paths.get(new_unsigned_file_path),StandardCopyOption.REPLACE_EXISTING); 
						    		}
						    		
						    		if(!canonicalPath_signed_file.startsWith(appPath))
							        {
							        	throw new IOException("Entry is outside of the target directory!");
							        }
							        else if(signed_file_exist.exists())
						    		{
							        	Path temp = Files.copy(Paths.get(signed_file_path),Paths.get(new_signed_file_path),StandardCopyOption.REPLACE_EXISTING); 
						    		}
						    		
					    			query1="UPDATE FMS_INV_FILE_DTL SET PDF_TYPE=?,FILE_NAME=? "
					    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
											+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
					    			stmt1=dbcon.prepareStatement(query1);
					    			stmt1.setString(1, "1"+pdf_type);
									stmt1.setString(2, new_pdf_file_name);
								    stmt1.setString(3, comp_cd);
								    stmt1.setString(4, bu_state_tin);
								    stmt1.setString(5, invoice_seq);
								    stmt1.setString(6, financial_year);
								    stmt1.setString(7, pdf_type);
								    stmt1.executeUpdate();
									stmt1.close();
									
									action_done=true;
					    		}
					    		else if(reqCount > 1)
					    		{
					    			query1="DELETE FROM FMS_INV_FILE_DTL "
					    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
											+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
					    			stmt1=dbcon.prepareStatement(query1);
					    			stmt1.setString(1, comp_cd);
								    stmt1.setString(2, bu_state_tin);
								    stmt1.setString(3, invoice_seq);
								    stmt1.setString(4, financial_year);
								    stmt1.setString(5, pdf_type);
								    stmt1.executeUpdate();
									stmt1.close();
									
									action_done=true;
					    		}
					    		
								if(!pdf_type.equals("") && action_done)
								{
									int cont=0;
									query1="UPDATE FMS_INVOICE_MST SET ";
										if(pdf_type.equals("O"))
										{
											query1+= "PRINT_BY_ORI=NULL,PRINT_DT_ORI=NULL ";
										}
										else if(pdf_type.equals("T"))
										{
											query1+= "PRINT_BY_TRI=NULL,PRINT_DT_TRI=NULL ";
										}
										else if(pdf_type.equals("D"))
										{
											query1+= "PRINT_BY_DUP=NULL,PRINT_DT_DUP=NULL ";
										}
										query1+= "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
											+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
									stmt1=dbcon.prepareStatement(query1);
									stmt1.setString(++cont, comp_cd);
									stmt1.setString(++cont, financial_year);
									stmt1.setString(++cont, invoice_seq);
									stmt1.setString(++cont, bu_state_tin);
									stmt1.executeUpdate();
									stmt1.close();
								}
					    	}
					    	rset.close();
					    	stmt.close();
						}
						
						msg = "Successful! - Re-Print PDF Request for "+invoice_no+" "+(action_flag.equals("A")?"Approved":"Rejected")+" Successfully!";
						msg_type="S";
			    	}
			    	else
			    	{
			    		msg = "Failed! - Re-Print PDF Request for "+invoice_no+" "+(action_flag.equals("A")?"Approval":"Rejection")+" Failed!";
						msg_type="E";
			    	}
				}
			}
			
			url = "../sales_invoice/frm_sales_inv_chng_req_aprv.jsp?month="+month+"&year="+year+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Sales Invoice Change Action Failed!";
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
}
