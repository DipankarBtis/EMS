package com.etrm.fms.remittance;

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

@WebServlet("/servlet/Frm_Remittance")
@MultipartConfig(fileSizeThreshold=1024*1024*20, 	// 20 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_Remittance extends HttpServlet
{
	static String db_src_file_name="Frm_Remittance.java";
	public static  Connection dbcon;

	public static String servletName = "Frm_Remittance";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";

	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
	private static String query4 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt_tmp = null;

	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset_tmp = null;

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
	
						if(option.equalsIgnoreCase("TRADER_INVOICE"))
						{
							InsertUpdateTraderInvoiceDetail(request);
						}
						else if(option.equalsIgnoreCase("F_FLOW_INVOICE"))
						{
							InsertUpdateFFlowInvoiceDetail(request);
						}
						else if(option.equalsIgnoreCase("INVOICE_PDF_UPLOAD"))
						{
							InvoiceFileUpload(request);
						}
						else if(option.equalsIgnoreCase("SEND_INVOICE_MAIL"))
						{
							SendInvoiceMail(request);
						}
						else if(option.equalsIgnoreCase("CONFIG_PD_BOND"))
						{
							InsertUpdatePDBondDetail(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_CR_DR"))
						{
							InsertUpdateCreditDebitDetail(request);
						}
						else if(option.equalsIgnoreCase("PURCHASE_CRDR_CHK_APRV"))
						{
							ChkAprvCreditDebitDetail(request);
						}
						else if(option.equalsIgnoreCase("CRDR_SEND_INVOICE_MAIL"))
						{
							SendCrdrInvoiceMail(request);
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
					if(rset_tmp != null){try {rset_tmp.close();}catch(SQLException e){System.out.println("rset_tmp is not close " + e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
					if(stmt_tmp != null){try {stmt_tmp.close();}catch(SQLException e){System.out.println("stmt_tmp is not close " + e);}}
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

	private void InsertUpdateTraderInvoiceDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTraderInvoiceDetail()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String qty_mmbtu = request.getParameter("qty_mmbtu")==null?"":request.getParameter("qty_mmbtu");
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String submission_from = request.getParameter("submission_from")==null?"":request.getParameter("submission_from");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String entity = request.getParameter("entity")==null?"":request.getParameter("entity");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String boe_no = request.getParameter("boe_no")==null?"":request.getParameter("boe_no");
			String boe_nm = request.getParameter("boe_nm")==null?"":request.getParameter("boe_nm");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String mapping_id = request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
			String contRef = request.getParameter("contRef")==null?"":request.getParameter("contRef");
			
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
			
			String deal_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);

			String sys_invoice_no = request.getParameter("sys_invoice_no")==null?"":request.getParameter("sys_invoice_no");
			String system_invoice_no = request.getParameter("system_invoice_no")==null?"":request.getParameter("system_invoice_no");
			String sys_invoice_seq = request.getParameter("sys_invoice_seq")==null?"":request.getParameter("sys_invoice_seq");
			String sys_financial_year = request.getParameter("sys_financial_year")==null?"":request.getParameter("sys_financial_year");
			String sys_invoice_dt = request.getParameter("sys_invoice_dt")==null?"":request.getParameter("sys_invoice_dt");
			String sys_invoice_due_dt = request.getParameter("sys_invoice_due_dt")==null?"":request.getParameter("sys_invoice_due_dt");
			String sys_alloc_qty = request.getParameter("sys_alloc_qty")==null?"":request.getParameter("sys_alloc_qty");
			String sys_price = request.getParameter("sys_price")==null?"":request.getParameter("sys_price");
			String sys_price_cd = request.getParameter("sys_price_cd")==null?"":request.getParameter("sys_price_cd");
			String sys_gross_amt = request.getParameter("sys_gross_amt")==null?"":request.getParameter("sys_gross_amt");
			String sys_exchng_rate = request.getParameter("sys_exchng_rate")==null?"":request.getParameter("sys_exchng_rate");
			String sys_exchng_cd = request.getParameter("sys_exchng_cd")==null?"":request.getParameter("sys_exchng_cd");
			String sys_exchng_dt = request.getParameter("sys_exchng_dt")==null?"":request.getParameter("sys_exchng_dt");
			String sys_gross_amt1 = request.getParameter("sys_gross_amt1")==null?"":request.getParameter("sys_gross_amt1");
			String sys_invoice_raised_in = request.getParameter("sys_invoice_raised_in")==null?"":request.getParameter("sys_invoice_raised_in");
			String sys_tax_amt = request.getParameter("sys_tax_amt")==null?"":request.getParameter("sys_tax_amt");
			String sys_tax_cd = request.getParameter("sys_tax_cd")==null?"":request.getParameter("sys_tax_cd");
			String sys_tax_dt = request.getParameter("sys_tax_dt")==null?"":request.getParameter("sys_tax_dt");
			String sys_invoice_amt = request.getParameter("sys_invoice_amt")==null?"":request.getParameter("sys_invoice_amt");
			String sys_adj_plusmin = request.getParameter("sys_adj_plusmin")==null?"":request.getParameter("sys_adj_plusmin");
			String sys_adj_amt = request.getParameter("sys_adj_amt")==null?"":request.getParameter("sys_adj_amt");
			String sys_net_payable = request.getParameter("sys_net_payable")==null?"":request.getParameter("sys_net_payable");
			String sys_chk = request.getParameter("sys_chk")==null?"":request.getParameter("sys_chk");
			String sys_auth = request.getParameter("sys_auth")==null?"":request.getParameter("sys_auth");
			String sys_tcs_amt = request.getParameter("sys_tcs_amt")==null?"":request.getParameter("sys_tcs_amt");
			String sys_tcs_factor = request.getParameter("sys_tcs_factor")==null?"":request.getParameter("sys_tcs_factor");
			String sys_tcs_cd = request.getParameter("sys_tcs_cd")==null?"":request.getParameter("sys_tcs_cd");
			String sys_tcs_dt = request.getParameter("sys_tcs_dt")==null?"":request.getParameter("sys_tcs_dt");
			String sys_tds_amt = request.getParameter("sys_tds_amt")==null?"":request.getParameter("sys_tds_amt");
			String sys_tds_factor = request.getParameter("sys_tds_factor")==null?"":request.getParameter("sys_tds_factor");
			String sys_tds_cd = request.getParameter("sys_tds_cd")==null?"":request.getParameter("sys_tds_cd");
			String sys_tds_dt = request.getParameter("sys_tds_dt")==null?"":request.getParameter("sys_tds_dt");
			String sys_cif_amt = request.getParameter("sys_cif_amt")==null?"":request.getParameter("sys_cif_amt");
			String sys_assessable_amt = request.getParameter("sys_assessable_amt")==null?"":request.getParameter("sys_assessable_amt");
			String sys_remark = request.getParameter("sys_remark")==null?"":request.getParameter("sys_remark");
			String sys_diff_price = request.getParameter("sys_diff_price")==null?"":request.getParameter("sys_diff_price");
			String sys_cd_paid_amt = request.getParameter("sys_cd_paid_amt")==null?"":request.getParameter("sys_cd_paid_amt");
			String sys_sug_percent = request.getParameter("sys_sug_percent")==null?"":request.getParameter("sys_sug_percent");
			String sys_sug_qty = request.getParameter("sys_sug_qty")==null?"":request.getParameter("sys_sug_qty");
			String sys_qty_unit = request.getParameter("sys_qty_unit")==null?"1":request.getParameter("sys_qty_unit");
			String sys_transportation_tariff = request.getParameter("sys_transportation_tariff")==null?"":request.getParameter("sys_transportation_tariff");
			String sys_transportation_amount = request.getParameter("sys_transportation_amount")==null?"":request.getParameter("sys_transportation_amount");
			String sys_marketing_margin = request.getParameter("sys_marketing_margin")==null?"":request.getParameter("sys_marketing_margin");
			String sys_marketing_margin_amount = request.getParameter("sys_marketing_margin_amount")==null?"":request.getParameter("sys_marketing_margin_amount");
			String sys_other_charges_amount = request.getParameter("sys_other_charges_amount")==null?"":request.getParameter("sys_other_charges_amount");
			String sys_other_charges = request.getParameter("sys_other_charges")==null?"":request.getParameter("sys_other_charges");
			
			String temp_party_invoice_no = request.getParameter("temp_party_invoice_no")==null?"":request.getParameter("temp_party_invoice_no");
			String party_invoice_no = request.getParameter("party_invoice_no")==null?"":request.getParameter("party_invoice_no");
			String party_system_invoice_no = request.getParameter("party_system_invoice_no")==null?"":request.getParameter("party_system_invoice_no");
			String party_invoice_seq = request.getParameter("party_invoice_seq")==null?"":request.getParameter("party_invoice_seq");
			String party_financial_year = request.getParameter("party_financial_year")==null?"":request.getParameter("party_financial_year");
			String party_invoice_dt = request.getParameter("party_invoice_dt")==null?"":request.getParameter("party_invoice_dt");
			String party_invoice_due_dt = request.getParameter("party_invoice_due_dt")==null?"":request.getParameter("party_invoice_due_dt");
			String party_alloc_qty = request.getParameter("party_alloc_qty")==null?"":request.getParameter("party_alloc_qty");
			String party_price = request.getParameter("party_price")==null?"":request.getParameter("party_price");
			String party_price_cd = request.getParameter("party_price_cd")==null?"":request.getParameter("party_price_cd");
			String party_gross_amt = request.getParameter("party_gross_amt")==null?"":request.getParameter("party_gross_amt");
			String party_exchng_rate = request.getParameter("party_exchng_rate")==null?"":request.getParameter("party_exchng_rate");
			String party_exchng_cd = request.getParameter("party_exchng_cd")==null?"":request.getParameter("party_exchng_cd");
			String party_exchng_dt = request.getParameter("party_exchng_dt")==null?"":request.getParameter("party_exchng_dt");
			String party_gross_amt1 = request.getParameter("party_gross_amt1")==null?"":request.getParameter("party_gross_amt1");
			String party_invoice_raised_in = request.getParameter("party_invoice_raised_in")==null?"":request.getParameter("party_invoice_raised_in");
			String party_tax_amt = request.getParameter("party_tax_amt")==null?"":request.getParameter("party_tax_amt");
			String party_tax_cd = request.getParameter("party_tax_cd")==null?"":request.getParameter("party_tax_cd");
			String party_tax_dt = request.getParameter("party_tax_dt")==null?"":request.getParameter("party_tax_dt");
			String party_invoice_amt = request.getParameter("party_invoice_amt")==null?"":request.getParameter("party_invoice_amt");
			String party_adj_plusmin = request.getParameter("party_adj_plusmin")==null?"":request.getParameter("party_adj_plusmin");
			String party_adj_amt = request.getParameter("party_adj_amt")==null?"":request.getParameter("party_adj_amt");
			String party_net_payable = request.getParameter("party_net_payable")==null?"":request.getParameter("party_net_payable");
			String party_chk = request.getParameter("party_chk")==null?"":request.getParameter("party_chk");
			String party_auth = request.getParameter("party_auth")==null?"":request.getParameter("party_auth");
			String party_tcs_amt = request.getParameter("party_tcs_amt")==null?"":request.getParameter("party_tcs_amt");
			String party_tcs_factor = request.getParameter("party_tcs_factor")==null?"":request.getParameter("party_tcs_factor");
			String party_tcs_cd = request.getParameter("party_tcs_cd")==null?"":request.getParameter("party_tcs_cd");
			String party_tcs_dt = request.getParameter("party_tcs_dt")==null?"":request.getParameter("party_tcs_dt");
			String party_tds_amt = request.getParameter("party_tds_amt")==null?"":request.getParameter("party_tds_amt");
			String party_tds_factor = request.getParameter("party_tds_factor")==null?"":request.getParameter("party_tds_factor");
			String party_tds_cd = request.getParameter("party_tds_cd")==null?"":request.getParameter("party_tds_cd");
			String party_tds_dt = request.getParameter("party_tds_dt")==null?"":request.getParameter("party_tds_dt");
			String party_cif_amt = request.getParameter("party_cif_amt")==null?"":request.getParameter("party_cif_amt");
			String party_assessable_amt = request.getParameter("party_assessable_amt")==null?"":request.getParameter("party_assessable_amt");
			String party_remark = request.getParameter("party_remark")==null?"":request.getParameter("party_remark");
			String party_diff_price = request.getParameter("party_diff_price")==null?"":request.getParameter("party_diff_price");
			String party_cd_paid_amt = request.getParameter("party_cd_paid_amt")==null?"":request.getParameter("party_cd_paid_amt");
			String party_sug_percent = request.getParameter("party_sug_percent")==null?"":request.getParameter("party_sug_percent");
			String party_sug_qty = request.getParameter("party_sug_qty")==null?"":request.getParameter("party_sug_qty");
			String party_qty_unit = request.getParameter("party_qty_unit")==null?"1":request.getParameter("party_qty_unit");
			String party_transportation_tariff = request.getParameter("party_transportation_tariff")==null?"":request.getParameter("party_transportation_tariff");
			String party_transportation_amount = request.getParameter("party_transportation_amount")==null?"":request.getParameter("party_transportation_amount");
			String party_marketing_margin = request.getParameter("party_marketing_margin")==null?"":request.getParameter("party_marketing_margin");
			String party_marketing_margin_amount = request.getParameter("party_marketing_margin_amount")==null?"":request.getParameter("party_marketing_margin_amount");
			String party_other_charges_amount = request.getParameter("party_other_charges_amount")==null?"":request.getParameter("party_other_charges_amount");
			String party_other_charges = request.getParameter("party_other_charges")==null?"":request.getParameter("party_other_charges");
			
			String applicable_flag = request.getParameter("applicable_flag")==null?"":request.getParameter("applicable_flag");
			String applicable_abbr = request.getParameter("applicable_abbr")==null?"":request.getParameter("applicable_abbr");
			String inv_aprv_flag = request.getParameter("inv_aprv_flag")==null?"":request.getParameter("inv_aprv_flag");
			String final_aprv = request.getParameter("final_aprv")==null?"":request.getParameter("final_aprv");
			
			String submitted_fiscal_yr = request.getParameter("submitted_fiscal_yr")==null?"":request.getParameter("submitted_fiscal_yr");
			
			String[] sys_sub_tax_struct = request.getParameterValues("sys_sub_tax_struct");
			String[] sys_sub_tax_amt = request.getParameterValues("sys_sub_tax_amt");
			String[] sys_sub_tax_code = request.getParameterValues("sys_sub_tax_code");
			String[] sys_sub_tax_base_amt = request.getParameterValues("sys_sub_tax_base_amt");
			
			String[] party_sub_tax_struct = request.getParameterValues("party_sub_tax_struct");
			String[] party_sub_tax_amt = request.getParameterValues("party_sub_tax_amt");
			String[] party_sub_tax_code = request.getParameterValues("party_sub_tax_code");
			String[] party_sub_tax_base_amt = request.getParameterValues("party_sub_tax_base_amt");
			
			String final_tds_amt = request.getParameter("final_tds_amt")==null?"":request.getParameter("final_tds_amt");
			String isTDSalrted = request.getParameter("isTDSalrted")==null?"":request.getParameter("isTDSalrted");
			String new_tds_factor = request.getParameter("new_tds_factor")==null?"":request.getParameter("new_tds_factor");
			String new_tds_struct_cd = request.getParameter("new_tds_struct_cd")==null?"":request.getParameter("new_tds_struct_cd");
			String new_applicable_abbr = request.getParameter("new_applicable_abbr")==null?"":request.getParameter("new_applicable_abbr");
			
			String sys_flag="";
			String party_flag="";
			
			String msgInfo=counterparty_abbr+" "+deal_no;
			String msg_content="";
			String invtypmsg="";
			String tmpmsg="";
			String temp_invoice_type=invoice_type;
			if(opration.equals("APPROVE"))
			{
				temp_invoice_type=final_aprv;
			}
			
			if(contract_type.equals("N") && inv_flag.equals("P"))
			{
				tmpmsg="Cargo Provisional";
			}
			else if(contract_type.equals("N") && inv_flag.equals("PF"))
			{
				tmpmsg="Cargo Proforma";
			}
			else if(contract_type.equals("N") && inv_flag.equals("F"))
			{
				tmpmsg="Cargo Final";
			}
			else if(contract_type.equals("N") && inv_flag.equals("CP"))
			{
				tmpmsg="Provisional Custom Duty";
			}
			else if(contract_type.equals("N") && inv_flag.equals("CF"))
			{
				tmpmsg="Final Custom Duty";
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				if(inv_flag.equals("UG")) 
				{
					tmpmsg="LTCORA SUG Purchase";
				}
				else
				{
					tmpmsg="LTCORA Purchase";
				}
			}
			else if(contract_type.equals("Y"))
			{
				tmpmsg="Surveyor Service";
			}
			else if(contract_type.equals("A"))
			{
				tmpmsg="Vessel Agent Service";
			}
			else if(contract_type.equals("H"))
			{
				if(inv_flag.equals("P"))
				{
					tmpmsg="Custom House Agent Provisional Service";
				}
				else
				{
					tmpmsg="Custom House Agent Service";
				}
			}
			else
			{
				tmpmsg="Purchase";
			}
			
			int count=0;
			if(temp_invoice_type.equals("S")) //SYSTEM GENERATED
			{
				invtypmsg="(SG)";
				//count=InvoiceDetailCount(temp_invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, plant_seq, bu_unit, contract_type, billing_cycle, period_start_dt, period_end_dt, sys_invoice_seq, sys_financial_year, inv_flag, cargo_no, boe_no);
				count=InvoiceDetailCount(temp_invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, plant_seq, bu_unit, contract_type, billing_cycle, period_start_dt, period_end_dt, sys_invoice_seq, submitted_fiscal_yr, inv_flag, cargo_no, boe_no);
			}
			else if(temp_invoice_type.equals("P"))
			{
				invtypmsg="(PG)";
				//count=InvoiceDetailCount(temp_invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, plant_seq, bu_unit, contract_type, billing_cycle, period_start_dt, period_end_dt, party_invoice_seq, party_financial_year, inv_flag, cargo_no, boe_no);
				count=InvoiceDetailCount(temp_invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, plant_seq, bu_unit, contract_type, billing_cycle, period_start_dt, period_end_dt, party_invoice_seq, submitted_fiscal_yr, inv_flag, cargo_no, boe_no);
			}
			
			msg_content=tmpmsg+invtypmsg+" Remittance";
			
			String new_invoice_seq=sys_invoice_seq;

			if(opration.equals("INSERT"))
			{
				int rem_count=0;
				if(count==0)
				{
					query="SELECT COUNT(*) "
							+ "FROM FMS_PUR_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
					if(contract_type.equals("N")) {
						query+="AND CARGO_NO=? AND BOE_NO=? ";
					}else if(contract_type.equals("G") || contract_type.equals("P")) {
						query+="AND CARGO_NO=? ";
					}
					int stcount=0;
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++stcount, comp_cd);
					stmt.setString(++stcount, counterparty_cd);
					stmt.setString(++stcount, cont_no);
					stmt.setString(++stcount, agmt_no);
					stmt.setString(++stcount, plant_seq);
					stmt.setString(++stcount, bu_unit);
					stmt.setString(++stcount, contract_type);
					stmt.setString(++stcount, billing_cycle);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
					stmt.setString(++stcount, inv_flag);
					if(contract_type.equals("N")) {
						stmt.setString(++stcount, cargo_no);
						stmt.setString(++stcount, boe_no);
					}else if(contract_type.equals("G") || contract_type.equals("P")) {
						stmt.setString(++stcount, cargo_no);
					}
					rset=stmt.executeQuery();
					if(rset.next())
					{
						rem_count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(rem_count==0)
					{
						String inv_seq="1";
						query="SELECT MAX(INVOICE_SEQ) "
								+ "FROM FMS_PUR_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND CONTRACT_TYPE=? AND INV_FLAG=? ";
						stmt4=dbcon.prepareStatement(query);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, sys_financial_year);
						stmt4.setString(3, contract_type);
						stmt4.setString(4, inv_flag);
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							inv_seq = ""+(rset4.getInt(1)+1);
						}
						rset4.close();
						stmt4.close();
		
						sys_invoice_seq = inv_seq;
						new_invoice_seq=sys_invoice_seq;
						
						String fin_yr="";
						if(!sys_financial_year.equals(""))
						{
							String[] temp = sys_financial_year.split("-");
							fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
						}
						
						if(!sys_invoice_seq.equals("") && !contract_type.equals(""))
						{
							String invoice_prefix=utilBean.getInvoicePrefix(dbcon,comp_cd);
						
							if(contract_type.equals("N") && !cargo_no.equals(""))
							{
								if(inv_flag.equals("P") || inv_flag.equals("PF"))
								{
									system_invoice_no=invoice_prefix+""+inv_flag+""+contract_type+"S"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("F"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("CF"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"CD"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("CP"))
								{
									system_invoice_no=invoice_prefix+"P"+contract_type+"CD"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
							}
							else if(contract_type.equals("G") || contract_type.equals("P"))
							{
								if(inv_flag.equals("UG"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"LUG"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
								else
								{
									system_invoice_no=invoice_prefix+""+contract_type+"L"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
							}
							else if(contract_type.equals("H") || contract_type.equals("Y") || contract_type.equals("A") )
							{
								if (contract_type.equals("H") && inv_flag.equals("P"))
								{	
									system_invoice_no=invoice_prefix+"P"+contract_type+"F"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;
								}
								else
								{	
									system_invoice_no=invoice_prefix+""+contract_type+"F"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;	
								}
							}
							else
							{
								system_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;	
							}
						}
						
						party_system_invoice_no=system_invoice_no;
					}
				}
				else
				{
					if(!sys_financial_year.equals(submitted_fiscal_yr) && !sys_financial_year.equals("") && !submitted_fiscal_yr.equals(""))
					{
						String inv_seq="1";
						query="SELECT MAX(INVOICE_SEQ) "
								+ "FROM FMS_PUR_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND CONTRACT_TYPE=? AND INV_FLAG=? ";
						stmt4=dbcon.prepareStatement(query);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, sys_financial_year);
						stmt4.setString(3, contract_type);
						stmt4.setString(4, inv_flag);
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							inv_seq = ""+(rset4.getInt(1)+1);
						}
						rset4.close();
						stmt4.close();
						
						new_invoice_seq = inv_seq;
						
						String fin_yr="";
						if(!sys_financial_year.equals(""))
						{
							String[] temp = sys_financial_year.split("-");
							fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
						}
						
						if(!new_invoice_seq.equals("") && !contract_type.equals(""))
						{
							String invoice_prefix=utilBean.getInvoicePrefix(dbcon,comp_cd);
						
							if(contract_type.equals("N") && !cargo_no.equals(""))
							{
								if(inv_flag.equals("P") || inv_flag.equals("PF"))
								{
									system_invoice_no=invoice_prefix+""+inv_flag+""+contract_type+"S"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("F"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("CF"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"CD"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
								else if(inv_flag.equals("CP"))
								{
									system_invoice_no=invoice_prefix+"P"+contract_type+"CD"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
							}
							else if(contract_type.equals("G") || contract_type.equals("P"))
							{
								if(inv_flag.equals("UG"))
								{
									system_invoice_no=invoice_prefix+""+contract_type+"LUG"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
								else
								{
									system_invoice_no=invoice_prefix+""+contract_type+"L"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
							}
							else if(contract_type.equals("H") || contract_type.equals("Y") || contract_type.equals("A") )
							{
								if (contract_type.equals("H") && inv_flag.equals("P"))
								{	
									system_invoice_no=invoice_prefix+"P"+contract_type+"F"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
								}
								else
								{	
									system_invoice_no=invoice_prefix+""+contract_type+"F"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;	
								}
							}
							else
							{
								system_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;	
							}
						}
						
						//FOR SG
						query1="UPDATE FMS_PUR_SG_INV_MST SET FINANCIAL_YEAR=?,INVOICE_SEQ=?,SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, sys_financial_year);
					    stmt1.setString(++st_count, new_invoice_seq);
					    stmt1.setString(++st_count, system_invoice_no);
					    stmt1.setString(++st_count, comp_cd);
					    stmt1.setString(++st_count, counterparty_cd);
					    stmt1.setString(++st_count, cont_no);
					    stmt1.setString(++st_count, agmt_no);
					    stmt1.setString(++st_count, plant_seq);
					    stmt1.setString(++st_count, bu_unit);
					    stmt1.setString(++st_count, contract_type);
					    stmt1.setString(++st_count, billing_cycle);
					    stmt1.setString(++st_count, period_start_dt);
					    stmt1.setString(++st_count, period_end_dt);
					    stmt1.setString(++st_count, sys_invoice_seq);
					    stmt1.setString(++st_count, submitted_fiscal_yr);
					    stmt1.setString(++st_count, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++st_count, cargo_no);
							stmt1.setString(++st_count, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++st_count, cargo_no);
						}
						stmt1.executeUpdate();
						stmt1.close();
						
						query3="UPDATE FMS_PUR_SG_INV_TAX_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, new_invoice_seq);
						stmt3.setString(2, sys_financial_year);
						stmt3.setString(3, comp_cd);
						stmt3.setString(4, contract_type);
						stmt3.setString(5, sys_invoice_seq);
						stmt3.setString(6, submitted_fiscal_yr);
						stmt3.setString(7, inv_flag);
						stmt3.executeQuery();
						stmt3.close();
						
						if(submission_from.equals("SERVICE") && !mapping_id.equals(""))
						{
							query="UPDATE FMS_PUR_INV_MERGE_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND INV_FLAG=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, new_invoice_seq);
							stmt.setString(2, sys_financial_year);
							stmt.setString(3, comp_cd);
							stmt.setString(4, contract_type);
							stmt.setString(5, submitted_fiscal_yr);
							stmt.setString(6, sys_invoice_seq);
							stmt.setString(7, inv_flag);
							stmt.executeUpdate();
							stmt.close();
						}
						
						///FOR PG
						query1="UPDATE FMS_PUR_PG_INV_MST SET FINANCIAL_YEAR=?,INVOICE_SEQ=?,SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, sys_financial_year);
					    stmt1.setString(++st_count, new_invoice_seq);
					    stmt1.setString(++st_count, system_invoice_no);
					    stmt1.setString(++st_count, comp_cd);
					    stmt1.setString(++st_count, counterparty_cd);
					    stmt1.setString(++st_count, cont_no);
					    stmt1.setString(++st_count, agmt_no);
					    stmt1.setString(++st_count, plant_seq);
					    stmt1.setString(++st_count, bu_unit);
					    stmt1.setString(++st_count, contract_type);
					    stmt1.setString(++st_count, billing_cycle);
					    stmt1.setString(++st_count, period_start_dt);
					    stmt1.setString(++st_count, period_end_dt);
					    stmt1.setString(++st_count, sys_invoice_seq);
					    stmt1.setString(++st_count, submitted_fiscal_yr);
					    stmt1.setString(++st_count, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++st_count, cargo_no);
							stmt1.setString(++st_count, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++st_count, cargo_no);
						}
						stmt1.executeUpdate();
						stmt1.close();
						
						query3="UPDATE FMS_PUR_PG_INV_TAX_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, new_invoice_seq);
						stmt3.setString(2, sys_financial_year);
						stmt3.setString(3, comp_cd);
						stmt3.setString(4, contract_type);
						stmt3.setString(5, sys_invoice_seq);
						stmt3.setString(6, submitted_fiscal_yr);
						stmt3.setString(7, inv_flag);
						stmt3.executeQuery();
						stmt3.close();
						
						///FILE TABLE
						query1="UPDATE FMS_PUR_INV_FILE_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=?  "
			 	        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
			        	stmt1=dbcon.prepareStatement(query1);
			        	stmt1.setString(1, new_invoice_seq);
			        	stmt1.setString(2, sys_financial_year);
			        	stmt1.setString(3, comp_cd);
				        stmt1.setString(4, contract_type);
				        stmt1.setString(5, sys_invoice_seq);
				        stmt1.setString(6, submitted_fiscal_yr);
				        stmt1.setString(7, inv_flag);
				        stmt1.executeUpdate();
			        	stmt1.close();
						
						sys_invoice_seq=new_invoice_seq;
						party_invoice_seq=new_invoice_seq;
						
						party_financial_year=sys_financial_year;
						
						party_system_invoice_no=system_invoice_no;
					}
				}
				
				if(invoice_type.equals("S")) //SYSTEM GENERATED
				{
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_SG_INV_MST SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, INVOICE_NO=?, "
								+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), FREQ=?, DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
								+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
								+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
								+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
								+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, ADJUST_SIGN=?, ADJUST_AMT=?, "
								+ "NET_PAYABLE_AMT=?,"
								+ "TCS_AMT=?,TCS_FACTOR=?,TCS_STRUCT_CD=?,"
								+ "TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),TCS_CERT_FLAG=?,TCS_TDS=?,"
								+ "TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,"
								+ "TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SYS_INV_NO=?,CIF_AMT=?,ASSESSABLE_AMT=?,REMARK=?,DIFF_PRICE=?,CD_PAID_AMT=?,"
								+ "SUG_PERCENT=?,SUG_QTY=?,QTY_UNIT=?,"
								+ "TRANSPORTATION_CHARGE=?,TRANSPORTATION_AMOUNT=?,MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, bu_contact_person);
					    stmt1.setString(++st_count, contact_person);
					    stmt1.setString(++st_count, sys_invoice_no);
					    stmt1.setString(++st_count, sys_invoice_dt);
					    stmt1.setString(++st_count, billing_cycle);
					    stmt1.setString(++st_count, sys_invoice_due_dt);
					    stmt1.setString(++st_count, sys_alloc_qty);
					    stmt1.setString(++st_count, sys_price);
					    stmt1.setString(++st_count, sys_price_cd);
					    stmt1.setString(++st_count, sys_gross_amt);
					    stmt1.setString(++st_count, sys_exchng_cd);
					    stmt1.setString(++st_count, sys_exchng_dt);
					    stmt1.setString(++st_count, sys_exchng_rate);
					    stmt1.setString(++st_count, sys_invoice_raised_in);
					    stmt1.setString(++st_count, sys_gross_amt1);
					    stmt1.setString(++st_count, sys_tax_amt);
					    stmt1.setString(++st_count, sys_tax_cd);
					    stmt1.setString(++st_count, sys_tax_dt);
					    stmt1.setString(++st_count, sys_invoice_amt);
					    stmt1.setString(++st_count, sys_adj_plusmin);
					    stmt1.setString(++st_count, sys_adj_amt);
					    stmt1.setString(++st_count, sys_net_payable);
					    stmt1.setString(++st_count, sys_tcs_amt);
					    stmt1.setString(++st_count, sys_tcs_factor);
					    stmt1.setString(++st_count, sys_tcs_cd);
					    stmt1.setString(++st_count, sys_tcs_dt);
					    stmt1.setString(++st_count, applicable_flag);
					    stmt1.setString(++st_count, applicable_abbr);
					    stmt1.setString(++st_count, sys_tds_amt);
					    stmt1.setString(++st_count, sys_tds_factor);
					    stmt1.setString(++st_count, sys_tds_cd);
					    stmt1.setString(++st_count, sys_tds_dt);
					    stmt1.setString(++st_count, system_invoice_no);
					    stmt1.setString(++st_count, sys_cif_amt);
					    stmt1.setString(++st_count, sys_assessable_amt);
					    stmt1.setString(++st_count, sys_remark);
					    stmt1.setString(++st_count, sys_diff_price);
					    stmt1.setString(++st_count, sys_cd_paid_amt);
					    stmt1.setString(++st_count, sys_sug_percent);
					    stmt1.setString(++st_count, sys_sug_qty);
					    stmt1.setString(++st_count, sys_qty_unit);
					    stmt1.setString(++st_count, sys_transportation_tariff);
					    stmt1.setString(++st_count, sys_transportation_amount);
					    stmt1.setString(++st_count, sys_marketing_margin);
					    stmt1.setString(++st_count, sys_marketing_margin_amount);
					    stmt1.setString(++st_count, sys_other_charges);
					    stmt1.setString(++st_count, sys_other_charges_amount);
					    stmt1.setString(++st_count, comp_cd);
					    stmt1.setString(++st_count, counterparty_cd);
					    stmt1.setString(++st_count, cont_no);
					    stmt1.setString(++st_count, agmt_no);
					    stmt1.setString(++st_count, plant_seq);
					    stmt1.setString(++st_count, bu_unit);
					    stmt1.setString(++st_count, contract_type);
					    stmt1.setString(++st_count, billing_cycle);
					    stmt1.setString(++st_count, period_start_dt);
					    stmt1.setString(++st_count, period_end_dt);
					    stmt1.setString(++st_count, sys_invoice_seq);
					    stmt1.setString(++st_count, sys_financial_year);
					    stmt1.setString(++st_count, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++st_count, cargo_no);
							stmt1.setString(++st_count, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++st_count, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Modified!";
						msg_type="S";
						
						stmt1.close();
						
						//UPDATING PARTY INVOICE NO WHEN SYS INVOICE NO GET CHANGED
						if(!sys_invoice_no.equals(temp_party_invoice_no) && !temp_party_invoice_no.equals(""))
						{
							query1="UPDATE FMS_PUR_PG_INV_MST SET INVOICE_NO=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
									+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
							if(contract_type.equals("N")) {
								query1+="AND CARGO_NO=? AND BOE_NO=? ";
							}else if(contract_type.equals("G") || contract_type.equals("P")) {
								query1+="AND CARGO_NO=? ";
							}
							st_count=0;
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++st_count, party_invoice_no);
							stmt1.setString(++st_count, comp_cd);
							stmt1.setString(++st_count, counterparty_cd);
							stmt1.setString(++st_count, cont_no);
							stmt1.setString(++st_count, agmt_no);
							stmt1.setString(++st_count, plant_seq);
							stmt1.setString(++st_count, bu_unit);
							stmt1.setString(++st_count, contract_type);
							stmt1.setString(++st_count, billing_cycle);
							stmt1.setString(++st_count, period_start_dt);
							stmt1.setString(++st_count, period_end_dt);
							stmt1.setString(++st_count, party_invoice_seq);
							stmt1.setString(++st_count, party_financial_year);
							stmt1.setString(++st_count, inv_flag);
							if(contract_type.equals("N")) {
								stmt1.setString(++st_count, cargo_no);
								stmt1.setString(++st_count, boe_no);
							}else if(contract_type.equals("G") || contract_type.equals("P")) {
								stmt1.setString(++st_count, cargo_no);
							}
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else
					{
						if(rem_count==0)
						{
							query1="INSERT INTO FMS_PUR_SG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
									+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
									+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
									+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
									+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
									+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,"
									+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,TCS_CERT_FLAG,TCS_TDS,"
									+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,SYS_INV_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
									+ "SUG_PERCENT,SUG_QTY,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT ";
							if(contract_type.equals("N"))
							{
								query1+=",CARGO_NO,BOE_NO";	
							}else if(contract_type.equals("G") || contract_type.equals("P")) {
								query1+=",CARGO_NO";	
							}
							query1+= ") "
									+ "VALUES(?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
									+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
									+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
									+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
									+ "?,?,?,?,?,SYSDATE,?,"
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,? ";
							if(contract_type.equals("N"))
							{
								query1+=",?,?";	
							}else if(contract_type.equals("G") || contract_type.equals("P")) {
								query1+=",?";	
							}
							query1+= ") ";
							int st_count=0;
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++st_count, comp_cd);
							stmt1.setString(++st_count, counterparty_cd);
							stmt1.setString(++st_count, agmt_no);
							stmt1.setString(++st_count, agmt_rev);
							stmt1.setString(++st_count, cont_no);
							stmt1.setString(++st_count, cont_rev);
							stmt1.setString(++st_count, contract_type);
							stmt1.setString(++st_count, bu_unit);
							stmt1.setString(++st_count, bu_contact_person);
							stmt1.setString(++st_count, plant_seq);
							stmt1.setString(++st_count, contact_person);
							stmt1.setString(++st_count, sys_invoice_seq);
							stmt1.setString(++st_count, sys_invoice_no);
							stmt1.setString(++st_count, sys_invoice_dt);
							stmt1.setString(++st_count, billing_cycle);
							stmt1.setString(++st_count, period_start_dt);
							stmt1.setString(++st_count, period_end_dt);
							stmt1.setString(++st_count, sys_invoice_due_dt);
							stmt1.setString(++st_count, sys_alloc_qty);
							stmt1.setString(++st_count, sys_price);
							stmt1.setString(++st_count, sys_price_cd);
							stmt1.setString(++st_count, sys_gross_amt);
							stmt1.setString(++st_count, sys_exchng_cd);
							stmt1.setString(++st_count, sys_exchng_dt);
							stmt1.setString(++st_count, sys_exchng_rate);
							stmt1.setString(++st_count, sys_invoice_raised_in);
							stmt1.setString(++st_count, sys_gross_amt1);
							stmt1.setString(++st_count, sys_tax_amt);
							stmt1.setString(++st_count, sys_tax_cd);
							stmt1.setString(++st_count, sys_tax_dt);
							stmt1.setString(++st_count, sys_invoice_amt);
							stmt1.setString(++st_count, sys_adj_plusmin);
							stmt1.setString(++st_count, sys_adj_amt);
							stmt1.setString(++st_count, sys_net_payable);
							stmt1.setString(++st_count, emp_cd);
							stmt1.setString(++st_count, sys_financial_year);
							stmt1.setString(++st_count, sys_tcs_amt);
							stmt1.setString(++st_count, sys_tcs_factor);
							stmt1.setString(++st_count, sys_tcs_cd);
							stmt1.setString(++st_count, sys_tcs_dt);
							stmt1.setString(++st_count, applicable_flag);
							stmt1.setString(++st_count, applicable_abbr);
							stmt1.setString(++st_count, sys_tds_amt);
							stmt1.setString(++st_count, sys_tds_factor);
							stmt1.setString(++st_count, sys_tds_cd);
							stmt1.setString(++st_count, sys_tds_dt);
							stmt1.setString(++st_count, system_invoice_no);
							stmt1.setString(++st_count, inv_flag);
							stmt1.setString(++st_count, sys_cif_amt);
							stmt1.setString(++st_count, sys_assessable_amt);
							stmt1.setString(++st_count, sys_remark);
							stmt1.setString(++st_count, sys_diff_price);
							stmt1.setString(++st_count, sys_cd_paid_amt);
							stmt1.setString(++st_count, sys_sug_percent);
							stmt1.setString(++st_count, sys_sug_qty);
							stmt1.setString(++st_count, sys_qty_unit);
							stmt1.setString(++st_count, sys_transportation_tariff);
						    stmt1.setString(++st_count, sys_transportation_amount);
						    stmt1.setString(++st_count, sys_marketing_margin);
						    stmt1.setString(++st_count, sys_marketing_margin_amount);
						    stmt1.setString(++st_count, sys_other_charges);
						    stmt1.setString(++st_count, sys_other_charges_amount);
							if(contract_type.equals("N"))
							{
								stmt1.setString(++st_count, cargo_no);
								stmt1.setString(++st_count, boe_no);
							}else if(contract_type.equals("G") || contract_type.equals("P")) {
								stmt1.setString(++st_count, cargo_no);
							}
							stmt1.executeUpdate();
							msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Submitted!";
							msg_type="S";
							
							stmt1.close();
						}
						else
						{
							msg = "Failed! - "+msg_content+" for "+msgInfo+" Already Generated!";
							msg_type="E";
						}
					}
					
					int tax_count=0;
					query2="SELECT COUNT(*) "
							+ "FROM FMS_PUR_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
					stmt2.setString(3, sys_invoice_seq);
					stmt2.setString(4, sys_financial_year);
					stmt2.setString(5, inv_flag);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						tax_count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(tax_count>0)
					{
						query3="DELETE FROM FMS_PUR_SG_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, contract_type);
						stmt3.setString(3, sys_invoice_seq);
						stmt3.setString(4, sys_financial_year);
						stmt3.setString(5, inv_flag);
						stmt3.executeQuery();
						
						stmt3.close();
					}
					
					if(sys_sub_tax_code!=null)
					{
						for(int i=0; i<sys_sub_tax_code.length;i++)
						{
							query4="INSERT INTO FMS_PUR_SG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,INV_FLAG) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE,?)";
							stmt4=dbcon.prepareStatement(query4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, contract_type);
							stmt4.setString(3, sys_invoice_seq);
							stmt4.setString(4, sys_financial_year);
							stmt4.setString(5, sys_tax_cd);
							stmt4.setString(6, sys_sub_tax_code[i]);
							stmt4.setString(7, sys_tax_dt);
							stmt4.setString(8, sys_sub_tax_struct[i]);
							stmt4.setString(9, sys_sub_tax_amt[i]);
							stmt4.setString(10, sys_sub_tax_base_amt[i]);
							stmt4.setString(11, emp_cd);
							stmt4.setString(12, inv_flag);
							stmt4.executeUpdate();
							
							stmt4.close();
						}
					}
				}
				else if(invoice_type.equals("P")) //PARTY GENERATED
				{	
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_PG_INV_MST SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, INVOICE_NO=?, "
								+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), FREQ=?, DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
								+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
								+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
								+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
								+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, ADJUST_SIGN=?, ADJUST_AMT=?, "
								+ "NET_PAYABLE_AMT=?,"
								+ "TCS_AMT=?,TCS_FACTOR=?,TCS_STRUCT_CD=?,"
								+ "TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),TCS_CERT_FLAG=?,TCS_TDS=?,"
								+ "TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,"
								+ "TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SYS_INV_NO=?,CIF_AMT=?,ASSESSABLE_AMT=?,REMARK=?,DIFF_PRICE=?,CD_PAID_AMT=?,"
								+ "SUG_PERCENT=?,SUG_QTY=?,QTY_UNIT=?,"
								+ "TRANSPORTATION_CHARGE=?,TRANSPORTATION_AMOUNT=?,MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, bu_contact_person);
						stmt1.setString(++st_count, contact_person);
						stmt1.setString(++st_count, party_invoice_no);
						stmt1.setString(++st_count, party_invoice_dt);
						stmt1.setString(++st_count, billing_cycle);
						stmt1.setString(++st_count, party_invoice_due_dt);
						stmt1.setString(++st_count, party_alloc_qty);
						stmt1.setString(++st_count, party_price);
						stmt1.setString(++st_count, party_price_cd);
						stmt1.setString(++st_count, party_gross_amt);
						stmt1.setString(++st_count, party_exchng_cd);
						stmt1.setString(++st_count, party_exchng_dt);
						stmt1.setString(++st_count, party_exchng_rate);
						stmt1.setString(++st_count, party_invoice_raised_in);
						stmt1.setString(++st_count, party_gross_amt1);
						stmt1.setString(++st_count, party_tax_amt);
						stmt1.setString(++st_count, party_tax_cd);
						stmt1.setString(++st_count, party_tax_dt);
						stmt1.setString(++st_count, party_invoice_amt);
						stmt1.setString(++st_count, party_adj_plusmin);
						stmt1.setString(++st_count, party_adj_amt);
						stmt1.setString(++st_count, party_net_payable);
						stmt1.setString(++st_count, party_tcs_amt);
						stmt1.setString(++st_count, party_tcs_factor);
						stmt1.setString(++st_count, party_tcs_cd);
						stmt1.setString(++st_count, party_tcs_dt);
						stmt1.setString(++st_count, applicable_flag);
						stmt1.setString(++st_count, applicable_abbr);
						stmt1.setString(++st_count, party_tds_amt);
						stmt1.setString(++st_count, party_tds_factor);
						stmt1.setString(++st_count, party_tds_cd);
						stmt1.setString(++st_count, party_tds_dt);
						stmt1.setString(++st_count, party_system_invoice_no);
						stmt1.setString(++st_count, party_cif_amt);
						stmt1.setString(++st_count, party_assessable_amt);
						stmt1.setString(++st_count, party_remark);
						stmt1.setString(++st_count, party_diff_price);
						stmt1.setString(++st_count, party_cd_paid_amt);
						stmt1.setString(++st_count, party_sug_percent);
						stmt1.setString(++st_count, party_sug_qty);
						stmt1.setString(++st_count, party_qty_unit);
						stmt1.setString(++st_count, party_transportation_tariff);
					    stmt1.setString(++st_count, party_transportation_amount);
					    stmt1.setString(++st_count, party_marketing_margin);
					    stmt1.setString(++st_count, party_marketing_margin_amount);
					    stmt1.setString(++st_count, party_other_charges);
					    stmt1.setString(++st_count, party_other_charges_amount);
						stmt1.setString(++st_count, comp_cd);
						stmt1.setString(++st_count, counterparty_cd);
						stmt1.setString(++st_count, cont_no);
						stmt1.setString(++st_count, agmt_no);
						stmt1.setString(++st_count, plant_seq);
						stmt1.setString(++st_count, bu_unit);
						stmt1.setString(++st_count, contract_type);
						stmt1.setString(++st_count, billing_cycle);
						stmt1.setString(++st_count, period_start_dt);
						stmt1.setString(++st_count, period_end_dt);
						stmt1.setString(++st_count, party_invoice_seq);
						stmt1.setString(++st_count, party_financial_year);
						stmt1.setString(++st_count, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++st_count, cargo_no);
							stmt1.setString(++st_count, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++st_count, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Modified!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						query1="INSERT INTO FMS_PUR_PG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
								+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
								+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
								+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
								+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
								+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,"
								+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,TCS_CERT_FLAG,TCS_TDS,"
								+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,SYS_INV_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
								+ "SUG_PERCENT,SUG_QTY,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT ";
						if(contract_type.equals("N"))
						{
							query1+=",CARGO_NO,BOE_NO";	
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+=",CARGO_NO";	
						}
						query1+= ") "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,SYSDATE,?,"
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,? ";
						if(contract_type.equals("N"))
						{
							query1+=",?,?";	
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+=",?";
						}
						query1+= ") ";
						int st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, comp_cd);
						stmt1.setString(++st_count, counterparty_cd);
						stmt1.setString(++st_count, agmt_no);
						stmt1.setString(++st_count, agmt_rev);
						stmt1.setString(++st_count, cont_no);
						stmt1.setString(++st_count, cont_rev);
						stmt1.setString(++st_count, contract_type);
						stmt1.setString(++st_count, bu_unit);
						stmt1.setString(++st_count, bu_contact_person);
						stmt1.setString(++st_count, plant_seq);
						stmt1.setString(++st_count, contact_person);
						stmt1.setString(++st_count, party_invoice_seq);
						stmt1.setString(++st_count, party_invoice_no);
						stmt1.setString(++st_count, party_invoice_dt);
						stmt1.setString(++st_count, billing_cycle);
						stmt1.setString(++st_count, period_start_dt);
						stmt1.setString(++st_count, period_end_dt);
						stmt1.setString(++st_count, party_invoice_due_dt);
						stmt1.setString(++st_count, party_alloc_qty);
						stmt1.setString(++st_count, party_price);
						stmt1.setString(++st_count, party_price_cd);
						stmt1.setString(++st_count, party_gross_amt);
						stmt1.setString(++st_count, party_exchng_cd);
						stmt1.setString(++st_count, party_exchng_dt);
						stmt1.setString(++st_count, party_exchng_rate);
						stmt1.setString(++st_count, party_invoice_raised_in);
						stmt1.setString(++st_count, party_gross_amt1);
						stmt1.setString(++st_count, party_tax_amt);
						stmt1.setString(++st_count, party_tax_cd);
						stmt1.setString(++st_count, party_tax_dt);
						stmt1.setString(++st_count, party_invoice_amt);
						stmt1.setString(++st_count, party_adj_plusmin);
						stmt1.setString(++st_count, party_adj_amt);
						stmt1.setString(++st_count, party_net_payable);
						stmt1.setString(++st_count, emp_cd);
						stmt1.setString(++st_count, party_financial_year);
						stmt1.setString(++st_count, party_tcs_amt);
						stmt1.setString(++st_count, party_tcs_factor);
						stmt1.setString(++st_count, party_tcs_cd);
						stmt1.setString(++st_count, party_tcs_dt);
						stmt1.setString(++st_count, applicable_flag);
						stmt1.setString(++st_count, applicable_abbr);
						stmt1.setString(++st_count, party_tds_amt);
						stmt1.setString(++st_count, party_tds_factor);
						stmt1.setString(++st_count, party_tds_cd);
						stmt1.setString(++st_count, party_tds_dt);
						stmt1.setString(++st_count, party_system_invoice_no);
						stmt1.setString(++st_count, inv_flag);
						stmt1.setString(++st_count, party_cif_amt);
						stmt1.setString(++st_count, party_assessable_amt);
						stmt1.setString(++st_count, party_remark);
						stmt1.setString(++st_count, party_diff_price);
						stmt1.setString(++st_count, party_cd_paid_amt);
						stmt1.setString(++st_count, party_sug_percent);
						stmt1.setString(++st_count, party_sug_qty);
						stmt1.setString(++st_count, party_qty_unit);
						stmt1.setString(++st_count, party_transportation_tariff);
					    stmt1.setString(++st_count, party_transportation_amount);
					    stmt1.setString(++st_count, party_marketing_margin);
					    stmt1.setString(++st_count, party_marketing_margin_amount);
					    stmt1.setString(++st_count, party_other_charges);
					    stmt1.setString(++st_count, party_other_charges_amount);
						if(contract_type.equals("N"))
						{
							stmt1.setString(++st_count, cargo_no);
							stmt1.setString(++st_count, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++st_count, cargo_no);
						}
						stmt1.executeUpdate();

						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Submitted!";
						msg_type="S";
						
						stmt1.close();
					}
					
					int tax_count=0;
					query2="SELECT COUNT(*) "
							+ "FROM FMS_PUR_PG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
					stmt2.setString(3, party_invoice_seq);
					stmt2.setString(4, party_financial_year);
					stmt2.setString(5, inv_flag);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						tax_count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(tax_count>0)
					{
						query3="DELETE FROM FMS_PUR_PG_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, contract_type);
						stmt3.setString(3, party_invoice_seq);
						stmt3.setString(4, party_financial_year);
						stmt3.setString(5, inv_flag);
						stmt3.executeQuery();
						
						stmt3.close();
					}
					
					if(party_sub_tax_code!=null)
					{
						for(int i=0; i<party_sub_tax_code.length;i++)
						{
							query4="INSERT INTO FMS_PUR_PG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,INV_FLAG) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE,?)";
							stmt4=dbcon.prepareStatement(query4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, contract_type);
							stmt4.setString(3, party_invoice_seq);
							stmt4.setString(4, party_financial_year);
							stmt4.setString(5, party_tax_cd);
							stmt4.setString(6, party_sub_tax_code[i]);
							stmt4.setString(7, party_tax_dt);
							stmt4.setString(8, party_sub_tax_struct[i]);
							stmt4.setString(9, party_sub_tax_amt[i]);
							stmt4.setString(10, party_sub_tax_base_amt[i]);
							stmt4.setString(11, emp_cd);
							stmt4.setString(12, inv_flag);
							stmt4.executeUpdate();
							
							stmt4.close();
						}
					}	
				}
				
				if(submission_from.equals("SERVICE") && !mapping_id.equals(""))
				{
					int merge_count=0;
					query="SELECT COUNT(*) "
							+ "FROM FMS_PUR_INV_MERGE_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INV_FLAG=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, contract_type);
					stmt.setString(3, sys_financial_year);
					stmt.setString(4, sys_invoice_seq);
					stmt.setString(5, inv_flag);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						merge_count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(merge_count > 0)
					{
						query="DELETE FROM FMS_PUR_INV_MERGE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND INV_FLAG=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, contract_type);
						stmt.setString(3, sys_financial_year);
						stmt.setString(4, sys_invoice_seq);
						stmt.setString(5, inv_flag);
						stmt.executeUpdate();
						
						stmt.close();
					}
					
					query="INSERT INTO FMS_PUR_INV_MERGE_DTL(COMPANY_CD,CONTRACT_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INV_FLAG,"
							+ "MERGE_SEQ,CONT_MAPPING,ENT_BY,ENT_DT) "
							+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, contract_type);
					stmt.setString(3, sys_financial_year);
					stmt.setString(4, sys_invoice_seq);
					stmt.setString(5, inv_flag);
					stmt.setString(6, "1");
					stmt.setString(7, mapping_id);
					stmt.setString(8, emp_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
				}
			}
			else if(opration.equals("CHECK"))
			{
				if(invoice_type.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=sys_chk;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_SG_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, sys_chk);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, sys_invoice_seq);
						stmt1.setString(++stcount, sys_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Checked!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Checking Failed!";
						msg_type="E";
					}
				}
				else if(invoice_type.equals("P")) //PARTY GENERATED
				{
					party_flag=party_chk;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_PG_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, party_chk);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, party_invoice_seq);
						stmt1.setString(++stcount, party_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Checked!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Check Failed!";
						msg_type="E";
					}
				}
			}
			else if(opration.equals("AUTHORIZE"))
			{
				if(invoice_type.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=sys_auth;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_SG_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, sys_auth);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, sys_invoice_seq);
						stmt1.setString(++stcount, sys_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Authorization updated!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Authorization Failed!";
						msg_type="E";
					}
				}
				else if(invoice_type.equals("P")) //PARTY GENERATED
				{
					party_flag=party_auth;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_PG_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, party_auth);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, party_invoice_seq);
						stmt1.setString(++stcount, party_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Authorization updated!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Authorization Failed!";
						msg_type="E";
					}
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(final_aprv.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=inv_aprv_flag;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_SG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE ";
								if(isTDSalrted.equals("Y") && inv_aprv_flag.equals("A"))
								{
									query1+=",TCS_TDS=?,TDS_AMT=?,TDS_STRUCT_CD=?,TDS_FACTOR=?,TDS_ALTED='Y' ";
								}
								query1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, inv_aprv_flag);
						stmt1.setString(++stcount, emp_cd);
						if(isTDSalrted.equals("Y") && inv_aprv_flag.equals("A"))
						{
							stmt1.setString(++stcount, new_applicable_abbr);
							stmt1.setString(++stcount, final_tds_amt);
							stmt1.setString(++stcount, new_tds_struct_cd);
							stmt1.setString(++stcount, new_tds_factor);
						}
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, sys_invoice_seq);
						stmt1.setString(++stcount, sys_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						
						stmt1.close();

						query2="UPDATE FMS_PUR_PG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query2+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query2+="AND CARGO_NO=? ";
						}
						stcount=0;
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, comp_cd);
						stmt2.setString(++stcount, counterparty_cd);
						stmt2.setString(++stcount, cont_no);
						stmt2.setString(++stcount, agmt_no);
						stmt2.setString(++stcount, plant_seq);
						stmt2.setString(++stcount, bu_unit);
						stmt2.setString(++stcount, contract_type);
						stmt2.setString(++stcount, billing_cycle);
						stmt2.setString(++stcount, period_start_dt);
						stmt2.setString(++stcount, period_end_dt);
						stmt2.setString(++stcount, party_invoice_seq);
						stmt2.setString(++stcount, party_financial_year);
						stmt2.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt2.setString(++stcount, cargo_no);
							stmt2.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt2.setString(++stcount, cargo_no);
						}
						stmt2.executeUpdate();
						
						stmt2.close();

						msg = "Successful! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Approval updated!";
						msg_type="S";
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Approval Failed!";
						msg_type="E";
					}
				}
				else if(final_aprv.equals("P")) //PARTY GENERATED
				{
					party_flag=inv_aprv_flag;
					
					if(count > 0)
					{
						query1="UPDATE FMS_PUR_PG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE ";
						if(isTDSalrted.equals("Y") && inv_aprv_flag.equals("A"))
						{
							query1+=",TCS_TDS=?,TDS_AMT=?,TDS_STRUCT_CD=?,TDS_FACTOR=?,TDS_ALTED='Y' ";
						}
						query1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query1+="AND CARGO_NO=? AND BOE_NO=? ";						
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query1+="AND CARGO_NO=? ";
						}
						int stcount=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, inv_aprv_flag);
						stmt1.setString(++stcount, emp_cd);
						if(isTDSalrted.equals("Y") && inv_aprv_flag.equals("A"))
						{
							stmt1.setString(++stcount, new_applicable_abbr);
							stmt1.setString(++stcount, final_tds_amt);
							stmt1.setString(++stcount, new_tds_struct_cd);
							stmt1.setString(++stcount, new_tds_factor);
						}
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, counterparty_cd);
						stmt1.setString(++stcount, cont_no);
						stmt1.setString(++stcount, agmt_no);
						stmt1.setString(++stcount, plant_seq);
						stmt1.setString(++stcount, bu_unit);
						stmt1.setString(++stcount, contract_type);
						stmt1.setString(++stcount, billing_cycle);
						stmt1.setString(++stcount, period_start_dt);
						stmt1.setString(++stcount, period_end_dt);
						stmt1.setString(++stcount, party_invoice_seq);
						stmt1.setString(++stcount, party_financial_year);
						stmt1.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt1.setString(++stcount, cargo_no);
							stmt1.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt1.setString(++stcount, cargo_no);
						}
						stmt1.executeUpdate();
						
						stmt1.close();

						query2="UPDATE FMS_PUR_SG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
								+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
						if(contract_type.equals("N")) {
							query2+="AND CARGO_NO=? AND BOE_NO=? ";
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							query2+="AND CARGO_NO=? ";
						}
						stcount=0;
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, "");
						stmt2.setString(++stcount, comp_cd);
						stmt2.setString(++stcount, counterparty_cd);
						stmt2.setString(++stcount, cont_no);
						stmt2.setString(++stcount, agmt_no);
						stmt2.setString(++stcount, plant_seq);
						stmt2.setString(++stcount, bu_unit);
						stmt2.setString(++stcount, contract_type);
						stmt2.setString(++stcount, billing_cycle);
						stmt2.setString(++stcount, period_start_dt);
						stmt2.setString(++stcount, period_end_dt);
						stmt2.setString(++stcount, sys_invoice_seq);
						stmt2.setString(++stcount, sys_financial_year);
						stmt2.setString(++stcount, inv_flag);
						if(contract_type.equals("N")) {
							stmt2.setString(++stcount, cargo_no);
							stmt2.setString(++stcount, boe_no);
						}else if(contract_type.equals("G") || contract_type.equals("P")) {
							stmt2.setString(++stcount, cargo_no);
						}
						stmt2.executeUpdate();
						
						stmt2.close();
						msg = "Successful - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Approval updated!";
						msg_type="S";
					}
					else
					{
						msg = "Failed! - "+msg_content+" "+system_invoice_no+" for "+msgInfo+" Approval Failed!";
						msg_type="E";
					}
				}
			}
			
			String remittnaceNo=""+system_invoice_no;
			if(!boe_nm.equals("")) {
				remittnaceNo=boe_nm+": "+system_invoice_no;
			}
			
			if(temp_invoice_type.equals("S"))
			{
				invoice_seq=sys_invoice_seq;
				
				InvoiceMailBody(comp_cd,counterparty_nm, counterparty_abbr,contract_type,deal_no+" ["+contRef+"]", sys_invoice_no, period_start_dt+" - "+period_end_dt, sys_invoice_due_dt, sys_invoice_amt, sys_net_payable, opration, temp_invoice_type,sys_flag,sys_invoice_raised_in,sys_invoice_dt,remittnaceNo,inv_flag,msg_content);
			}
			else if(temp_invoice_type.equals("P"))
			{
				invoice_seq=party_invoice_seq;
				
				InvoiceMailBody(comp_cd,counterparty_nm, counterparty_abbr,contract_type,deal_no+" ["+contRef+"]", party_invoice_no, period_start_dt+" - "+period_end_dt, party_invoice_due_dt, party_invoice_amt, party_net_payable, opration, temp_invoice_type,party_flag,party_invoice_raised_in,party_invoice_dt,remittnaceNo,inv_flag,msg_content);
			}
			
			String filenm="frm_prepare_seller_payment.jsp";
			String temp_financial_year=financial_year;
			if(submission_from.equals("SERVICE"))
			{
				filenm="frm_prepare_service_payment.jsp";
				temp_financial_year=sys_financial_year;
			}
			else if(inv_flag.equals("UG"))
			{
				filenm="frm_prepare_sug_payment.jsp";
			}
			
			url = "../remittance/"+filenm+"?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&activity_type="+activity_type+"&qty_mmbtu="+qty_mmbtu+
					"&accroid="+accroid+"&cargo_no="+cargo_no+"&boe_no="+boe_no+"&inv_flag="+inv_flag+"&mapping_id="+mapping_id+"&entity="+entity+
					//"&financial_year="+financial_year+"&invoice_seq="+invoice_seq+
					"&financial_year="+temp_financial_year+"&invoice_seq="+invoice_seq+ //NEED TO CHECK LATTER TIME
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Inser/Update Purchase Remittance Failed!";
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

	private int InvoiceDetailCount(String invoice_type,String comp_cd,String counterparty_cd,String cont_no,String agmt_no,String plant_seq,String bu_unit,String contract_type,String billing_cycle,
			String period_start_dt,String period_end_dt,String invoice_seq,String financial_year,String inv_flag,String cargo_no,String boe_no) throws SQLException
	{
		String function_nm="InvoiceDetailCount()";
		int count=0;
		try
		{
			query="SELECT COUNT(*) ";
			if(invoice_type.equals("P"))
			{
				query+="FROM FMS_PUR_PG_INV_MST ";
			}
			else
			{
				query+="FROM FMS_PUR_SG_INV_MST ";
			}
			query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_SEQ=? "
					+ "AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
			if(contract_type.equals("N")) {
				query+="AND CARGO_NO=? AND BOE_NO=? ";
			}else if(contract_type.equals("G") || contract_type.equals("P")) {
				query+="AND CARGO_NO=? ";
			}
			int stcount=0;
			stmt_tmp=dbcon.prepareStatement(query);
			stmt_tmp.setString(++stcount, comp_cd);
			stmt_tmp.setString(++stcount, counterparty_cd);
			stmt_tmp.setString(++stcount, cont_no);
			stmt_tmp.setString(++stcount, agmt_no);
			stmt_tmp.setString(++stcount, plant_seq);
			stmt_tmp.setString(++stcount, bu_unit);
			stmt_tmp.setString(++stcount, contract_type);
			stmt_tmp.setString(++stcount, billing_cycle);
			stmt_tmp.setString(++stcount, period_start_dt);
			stmt_tmp.setString(++stcount, period_end_dt);
			stmt_tmp.setString(++stcount, invoice_seq);
			stmt_tmp.setString(++stcount, financial_year);
			stmt_tmp.setString(++stcount, inv_flag);
			if(contract_type.equals("N")) {
				stmt_tmp.setString(++stcount, cargo_no);
				stmt_tmp.setString(++stcount, boe_no);
			}else if(contract_type.equals("G") || contract_type.equals("P")) {
				stmt_tmp.setString(++stcount, cargo_no);
			}
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				count=rset_tmp.getInt(1);
			}
			rset_tmp.close();
			stmt_tmp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
		
		return count;
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
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String entity = request.getParameter("entity")==null?"":request.getParameter("entity");
			String mapping_id = request.getParameter("mapping_id")==null?"0":request.getParameter("mapping_id");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String address_type = request.getParameter("address_type")==null?"":request.getParameter("address_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String cont_ref = request.getParameter("cont_ref")==null?"":request.getParameter("cont_ref");
					
			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
			
			//String deal_no=contract_type+""+cont_no;
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
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
			String invoice_head = request.getParameter("invoice_head")==null?"":request.getParameter("invoice_head");

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
			String qty_unit = request.getParameter("qty_unit")==null?"":request.getParameter("qty_unit");
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

			String temp_tax_cd = request.getParameter("temp_tax_cd")==null?"":request.getParameter("temp_tax_cd");
			String tax_cd = request.getParameter("tax_cd")==null?"":request.getParameter("tax_cd");
			String tax_dt = request.getParameter("tax_dt")==null?"":request.getParameter("tax_dt");
			
			String[] sub_tax_struct = request.getParameterValues("sub_tax_struct");
			String[] sub_tax_amt = request.getParameterValues("sub_tax_amt");
			String[] sub_tax_code = request.getParameterValues("sub_tax_code");
			String[] sub_tax_base_amt = request.getParameterValues("sub_tax_base_amt");
			
			String sys_flag="";
			String msgInfo=counterparty_abbr+" "+deal_no;

			int count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
					+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, invoice_seq);
			stmt.setString(3, invoice_no);
			stmt.setString(4, invoice_type);
			stmt.setString(5, financial_year);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;

			if(opration_type.equals("CHECK"))
			{
				sys_flag=chk;
				if(count > 0)
				{
					query1="UPDATE FMS_PUR_FFLOW_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, chk);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					msg = "Successful - Purchase(FFLOW) Remittance "+invoice_no+" for "+msgInfo+" Checked!";
					msg_type="S";
				}
			}
			else if(opration_type.equals("AUTHORIZE"))
			{
				sys_flag=auth;
				if(count > 0)
				{
					query1="UPDATE FMS_PUR_FFLOW_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, auth);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					msg = "Successful - Purchase(FFLOW) Remittance "+invoice_no+" for "+msgInfo+" Authorization updated!";
					msg_type="S";
				}
			}
			else if(opration_type.equals("APPROVE"))
			{
				sys_flag=aprv;
				if(count > 0)
				{
					query1="UPDATE FMS_PUR_FFLOW_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, aprv);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					msg = "Successful - Purchase(FFLOW) Remittance "+invoice_no+" for "+msgInfo+" Approval updated!";
					msg_type="S";
				}
			}
			else
			{
				if(opration.equals("MODIFY"))
				{
					if(count > 0)
					{
						String temp_fiscal_yr=dateUtil.getFinancialYear(invoice_dt);
						if(!financial_year.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !financial_year.equals(""))
						{
							new_financial_year=temp_fiscal_yr;
							String fin_yr="";
							if(!new_financial_year.equals(""))
							{
								String[] temp = new_financial_year.split("-");
								fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
							}
							
							queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
									+ "FROM FMS_PUR_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? "
									+ "AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, new_financial_year);
							stmt.setString(3, invoice_type);
							stmt.setString(4, contract_type);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								new_invoice_seq=""+(rset.getInt(1)+1);
							}
							rset.close();
							stmt.close();
							
							if(!new_invoice_seq.equals("") && !contract_type.equals("") && (!invoice_type.equals("") && !invoice_type.equals("0")))
							{
								String invoice_prefix=utilBean.getInvoicePrefix(dbcon,comp_cd);
								
								invoice_no=invoice_prefix+""+contract_type+invoice_type+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;
							}
						}
						
						query1="UPDATE FMS_PUR_FFLOW_INV_MST SET COUNTERPARTY_CD=?,AGMT_NO=?,AGMT_REV=?,"
								+ "CONT_NO=?,CONT_REV=?,CONTRACT_TYPE=?,BU_UNIT=?,BU_CONTACT_PERSON_CD=?,"
								+ "ADDR_FLAG=?,CONTACT_PERSON_CD=?,INVOICE_REF=?,INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "INVOICE_CATEGORY=?,FREQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),NUM_LINE=?,LINKED_INVOICE=?,"
								+ "NOTE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,INVOICE_TYPE=?,FINANCIAL_YEAR=?,"
								+ "OTHER_INV_STR=?,GROSS_AMT_USD=?,EXCHG_RATE_VALUE=?,GROSS_AMT_INR=?,"
								+ "TAX_AMT=?,INVOICE_AMT=?,ADJUST_AMT=?,NET_PAYABLE_AMT=?,INVOICE_RAISED_IN=?,"
								+ "AMT_WORD=?,TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TDS_AMT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TCS_AMT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TCS_TDS=?,ALLOC_QTY=?,CARGO_NO=?,INV_HEAD=?,QTY_UNIT=?,DUE_DT=TO_DATE(?,'DD/MM/YYYY'),INVOICE_SEQ=?,INVOICE_NO=? "
								+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? "
								+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
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
						stmt1.setString(11, invoice_ref);
						stmt1.setString(12, invoice_dt);
						stmt1.setString(13, invoice_category);
						stmt1.setString(14, billing_cycle);
						stmt1.setString(15, period_start_dt);
						stmt1.setString(16, period_end_dt);
						stmt1.setString(17, no_of_line);
						stmt1.setString(18, linked_invoice);
						stmt1.setString(19, note);
						stmt1.setString(20, emp_cd);
						stmt1.setString(21, invoice_type);
						//stmt1.setString(22, financial_year);
						stmt1.setString(22, new_financial_year);
						stmt1.setString(23, subject_line);
						stmt1.setString(24, gross_amt_usd);
						stmt1.setString(25, exchange_rate);
						stmt1.setString(26, gross_amt_inr);
						stmt1.setString(27, tax_amt);
						stmt1.setString(28, invoice_amt);
						stmt1.setString(29, adjust_amt);
						stmt1.setString(30, net_amt);
						stmt1.setString(31, invoice_raised_in);
						stmt1.setString(32, amt_in_word);
						stmt1.setString(33, tax_cd);
						stmt1.setString(34, tax_dt);
						stmt1.setString(35, tds_amt);
						stmt1.setString(36, tds_cd);
						stmt1.setString(37, tds_dt);
						stmt1.setString(38, tcs_amt);
						stmt1.setString(39, tcs_cd);
						stmt1.setString(40, tcs_dt);
						stmt1.setString(41, tcs_tds);
						stmt1.setString(42, alloc_qty);
						stmt1.setString(43, cargo_no);
						stmt1.setString(44, invoice_head);
						stmt1.setString(45, qty_unit);
						stmt1.setString(46, invoice_due_dt);
						stmt1.setString(47, new_invoice_seq);
						stmt1.setString(48, invoice_no);
						stmt1.setString(49, comp_cd);
						stmt1.setString(50, invoice_seq);
						stmt1.setString(51, invoice_type);
						stmt1.setString(52, financial_year);
						stmt1.setString(53, contract_type);
						stmt1.executeUpdate();
						
						stmt1.close();
						msg = "Successful - Purchase(FFLOW) Remittance "+invoice_no+" for "+msgInfo+" Modified!";
						msg_type="S";
					}
					
					query2="UPDATE FMS_PUR_FFLOW_INV_FILE_DTL SET FINANCIAL_YEAR=?,INVOICE_SEQ=? "
			        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
			        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, new_financial_year);
					stmt2.setString(2, new_invoice_seq);
					stmt2.setString(3, comp_cd);
					stmt2.setString(4, contract_type);
					stmt2.setString(5, invoice_type);
					stmt2.setString(6, invoice_seq);
					stmt2.setString(7, financial_year);
					stmt2.executeUpdate();
					stmt2.close();

					query2="DELETE FROM FMS_PUR_FFLOW_INV_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, invoice_seq);
					//stmt2.setString(3, invoice_no);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();

					if(item_seq != null)
					{
						for(int i=0; i<item_seq.length; i++)
						{
							query3="INSERT INTO FMS_PUR_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
									+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
									+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE,CARGO_NO) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?)";
							stmt3=dbcon.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev);
							stmt3.setString(7, contract_type);
							stmt3.setString(8, bu_unit);
							stmt3.setString(9, bu_contact_person);
							stmt3.setString(10, address_type);
							stmt3.setString(11, contact_person);
							//stmt3.setString(12, invoice_seq);
							stmt3.setString(12, new_invoice_seq);
							stmt3.setString(13, invoice_no);
							stmt3.setString(14, item_seq[i]);
							stmt3.setString(15, item_note[i]);
							stmt3.setString(16, unit[i]);
							stmt3.setString(17, qty[i]);
							stmt3.setString(18, rate[i]);
							stmt3.setString(19, amount[i]);
							stmt3.setString(20, emp_cd);
							//stmt3.setString(21, financial_year);
							stmt3.setString(21, new_financial_year);
							stmt3.setString(22, invoice_type);
							stmt3.setString(23, cargo_no);
							stmt3.executeUpdate();
							
							stmt3.close();
						}
					}
				}
				else
				{
					financial_year=dateUtil.getFinancialYear(invoice_dt);
					
					queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
							+ "FROM FMS_PUR_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, invoice_type);
					stmt.setString(4, contract_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						invoice_seq=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					String fin_yr="";
					if(!financial_year.equals(""))
					{
						String[] temp = financial_year.split("-");
						fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
					}
					
					if(!invoice_seq.equals("") && !contract_type.equals("") && (!invoice_type.equals("") && !invoice_type.equals("0")))
					{
						String invoice_prefix=utilBean.getInvoicePrefix(dbcon,comp_cd);
						
						invoice_no=invoice_prefix+""+contract_type+invoice_type+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
					}
					
					new_invoice_seq=invoice_seq;
					new_financial_year=financial_year;
					
					query1="INSERT INTO FMS_PUR_FFLOW_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_REF,INVOICE_DT,"
							+ "INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,NUM_LINE,LINKED_INVOICE,"
							+ "NOTE,ENT_BY,ENT_DT,DUE_DT,INVOICE_TYPE,FINANCIAL_YEAR,OTHER_INV_STR,"
							+ "GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,INVOICE_RAISED_IN,AMT_WORD,"
							+ "TAX_STRUCT_CD,TAX_EFF_DT,TDS_AMT,TDS_STRUCT_CD,TDS_EFF_DT,TCS_AMT,TCS_STRUCT_CD,TCS_EFF_DT,TCS_TDS,"
							+ "ALLOC_QTY,CARGO_NO,INV_HEAD,QTY_UNIT) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?)";
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
					stmt1.setString(14, invoice_ref);
					stmt1.setString(15, invoice_dt);
					stmt1.setString(16, invoice_category);
					stmt1.setString(17, billing_cycle);
					stmt1.setString(18, period_start_dt);
					stmt1.setString(19, period_end_dt);
					stmt1.setString(20, no_of_line);
					stmt1.setString(21, linked_invoice);
					stmt1.setString(22, note);
					stmt1.setString(23, emp_cd);
					stmt1.setString(24, invoice_due_dt);
					stmt1.setString(25, invoice_type);
					stmt1.setString(26, financial_year);
					stmt1.setString(27, subject_line);
					stmt1.setString(28, gross_amt_usd);
					stmt1.setString(29, exchange_rate);
					stmt1.setString(30, gross_amt_inr);
					stmt1.setString(31, tax_amt);
					stmt1.setString(32, invoice_amt);
					stmt1.setString(33, adjust_amt);
					stmt1.setString(34, net_amt);
					stmt1.setString(35, invoice_raised_in);
					stmt1.setString(36, amt_in_word);
					stmt1.setString(37, tax_cd);
					stmt1.setString(38, tax_dt);
					stmt1.setString(39, tds_amt);
					stmt1.setString(40, tds_cd);
					stmt1.setString(41, tds_dt);
					stmt1.setString(42, tcs_amt);
					stmt1.setString(43, tcs_cd);
					stmt1.setString(44, tcs_dt);
					stmt1.setString(45, tcs_tds);
					stmt1.setString(46, alloc_qty);
					stmt1.setString(47, cargo_no);
					stmt1.setString(48, invoice_head);
					stmt1.setString(49, qty_unit);
					stmt1.executeUpdate();
					
					stmt1.close();
					msg = "Successful - Purchase(FFLOW) Remittance "+invoice_no+" for "+msgInfo+" Generated!";
					msg_type="S";

					if(item_seq != null)
					{
						for(int i=0; i<item_seq.length; i++)
						{
							query2="INSERT INTO FMS_PUR_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
									+ "BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
									+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE,CARGO_NO) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?)";
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
							stmt2.setString(23, cargo_no);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
				
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
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
					query2="DELETE FROM FMS_PUR_FFLOW_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
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
						
						query3="INSERT INTO FMS_PUR_FFLOW_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,"
								+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
								+ "TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,"
								+ "?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, contract_type);
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
							query2="INSERT INTO FMS_PUR_FFLOW_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,"
									+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
									+ "TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,"
									+ "?,?,?,?,?,"
									+ "TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, contract_type);
							//stmt2.setString(3, invoice_seq);
							stmt2.setString(3, new_invoice_seq);
							stmt2.setString(4, invoice_type);
							//stmt2.setString(5, financial_year);
							stmt2.setString(5, new_financial_year);
							stmt2.setString(6, tax_cd);
							stmt2.setString(7, sub_tax_code[i]);
							stmt2.setString(8, tax_dt);
							stmt2.setString(9, sub_tax_struct[i]);
							stmt2.setString(10, sub_tax_amt[i]);
							stmt2.setString(11, "");
							stmt2.setString(12, emp_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
			}
			
			FFlowInvoiceMailBody(comp_cd,counterparty_nm, counterparty_abbr,deal_no, cont_ref, invoice_ref, period_start_dt+" - "+period_end_dt, invoice_due_dt, invoice_amt, net_amt, opration_type, invoice_type,sys_flag,invoice_raised_in,invoice_dt,invoice_no,invoice_head);

			if(opration.equals("INSERT"))
			{
				opration="MODIFY";
			}
			url = "../remittance/frm_purchase_f_flow_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&invoice_type="+invoice_type+"&opration="+opration+
					"&mapping_id="+mapping_id+"&address_type="+address_type+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&entity="+entity+
					"&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+"&accroid="+accroid+
					"&financial="+financial_year+"&activity_type="+activity_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception - Purchase(FFLOW) Remittance Insert/Update Failed!";
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
	
	private void InvoiceFileUpload(HttpServletRequest request) throws SQLException
	{
		String function_nm="InvoiceFileUpload()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String month=request.getParameter("month")==null?"":request.getParameter("month");
			String year=request.getParameter("year")==null?"":request.getParameter("year");
			String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			
			String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String filter_counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String submission_from=request.getParameter("submission_from")==null?"":request.getParameter("submission_from");
			
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String contract_type = request.getParameter("file_contract_type")==null?"":request.getParameter("file_contract_type");
			String invoice_seq = request.getParameter("file_invoice_seq")==null?"":request.getParameter("file_invoice_seq");
			String financial_year = request.getParameter("file_financial_year")==null?"":request.getParameter("file_financial_year");
			String invoice_title = request.getParameter("invoice_title")==null?"":request.getParameter("invoice_title");
			String invoice_type = request.getParameter("file_invoice_type")==null?"":request.getParameter("file_invoice_type");
			String inv_flag = request.getParameter("file_inv_flag")==null?"":request.getParameter("file_inv_flag");
			
			String upload_inv_type = request.getParameter("upload_inv_type")==null?"":request.getParameter("upload_inv_type");
			
			String appPath = request.getServletContext().getRealPath("");

			String main_folder="";
			if(!comp_cd.equals(""))
			{
				main_folder="work_data"+comp_cd;
			}
			String filePath=appPath+File.separator+main_folder;
			File main_folderDir = new File(filePath);
	        if(!main_folderDir.exists())
	        {
	        	main_folderDir.mkdir();
	        }

	        String sub_folder="purchase";
	        String filePath1=filePath+File.separator+sub_folder;
	        File sub_folderDir = new File(filePath1);
	        if(!sub_folderDir.exists())
	        {
	        	sub_folderDir.mkdir();
	        }

	        String sub_folder2="invoice";
	        if(upload_inv_type.equals("FFLOW"))
	        {
	        	sub_folder2="f_flow_invoice";
	        }
	        String filePath2=filePath1+File.separator+sub_folder2;
	        File sub_folderDir2 = new File(filePath2);
	        if(!sub_folderDir2.exists())
	        {
	        	sub_folderDir2.mkdir();
	        }
	        
	        String file_name="";
	        String fileName="";
	        for (Part part : request.getParts()) 
	        {
	        	fileName = extractFileName(part);
	        	// refines the fileName in case it is an absolute path
			    fileName = new File(fileName).getName();
			    if(!fileName.equals("") ){
			    	file_name=fileName;
			    	part.write(filePath2 +File.separator+ fileName);
			    } 
	        }
	        
	        if(upload_inv_type.equals("FFLOW"))
	        {
	        	int count=0;
		        query="SELECT COUNT(*) "
		        		+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
		        stmt=dbcon.prepareStatement(query);
		        stmt.setString(1, comp_cd);
		        stmt.setString(2, contract_type);
		        stmt.setString(3, invoice_type);
		        stmt.setString(4, invoice_seq);
		        stmt.setString(5, financial_year);
		        stmt.setString(6, invoice_title);
		        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	count=rset.getInt(1);
		        }
		        rset.close();
		        stmt.close();
		        
		        if(count > 0)
		        {
		        	 query1="UPDATE FMS_PUR_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		        			+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
				        	+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, file_name);
			         stmt1.setString(2, emp_cd);
		        	 stmt1.setString(3, comp_cd);
			         stmt1.setString(4, contract_type);
			         stmt1.setString(5, invoice_type);
			         stmt1.setString(6, invoice_seq);
			         stmt1.setString(7, financial_year);
			         stmt1.setString(8, invoice_title);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
		        else
		        {
		        	query1="INSERT INTO FMS_PUR_FFLOW_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
		        			+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
		        			+ "VALUES(?,?,?,?,?,"
		        			+ "?,?,SYSDATE,?)";
		        	stmt1=dbcon.prepareStatement(query1);
		        	stmt1.setString(1, comp_cd);
			        stmt1.setString(2, contract_type);
			        stmt1.setString(3, invoice_seq);
			        stmt1.setString(4, financial_year);
			        stmt1.setString(5, invoice_title);
			        stmt1.setString(6, file_name);
			        stmt1.setString(7, emp_cd);
			        stmt1.setString(8, invoice_type);
		        	stmt1.executeUpdate();
		        	 
		        	stmt1.close();
		        }
	        }
	        else
	        {
		        int count=0;
		        query="SELECT COUNT(*) "
		        		+ "FROM FMS_PUR_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
		        stmt=dbcon.prepareStatement(query);
		        stmt.setString(1, comp_cd);
		        stmt.setString(2, contract_type);
		        stmt.setString(3, invoice_seq);
		        stmt.setString(4, financial_year);
		        stmt.setString(5, invoice_title);
		        stmt.setString(6, inv_flag);
		        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	count=rset.getInt(1);
		        }
		        rset.close();
		        stmt.close();
		        
		        if(count > 0)
		        {
		        	 query1="UPDATE FMS_PUR_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		 	        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, file_name);
			         stmt1.setString(2, emp_cd);
		        	 stmt1.setString(3, comp_cd);
			         stmt1.setString(4, contract_type);
			         stmt1.setString(5, invoice_seq);
			         stmt1.setString(6, financial_year);
			         stmt1.setString(7, invoice_title);
			         stmt1.setString(8, inv_flag);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
		        else
		        {
		        	query1="INSERT INTO FMS_PUR_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
		        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_FLAG) "
		        			+ "VALUES(?,?,?,?,?,"
		        			+ "?,?,SYSDATE,?)";
		        	stmt1=dbcon.prepareStatement(query1);
		        	stmt1.setString(1, comp_cd);
			        stmt1.setString(2, contract_type);
			        stmt1.setString(3, invoice_seq);
			        stmt1.setString(4, financial_year);
			        stmt1.setString(5, invoice_title);
			        stmt1.setString(6, file_name);
			        stmt1.setString(7, emp_cd);
			        stmt1.setString(8, inv_flag);
		        	stmt1.executeUpdate();
		        	 
		        	stmt1.close();
		        }
	        }
	        
	        msg = "Successful! - Purchase("+upload_inv_type+") Remittance Received File Uploaded!";
			msg_type="S";
			
			String filenm="frm_purchase_invoice.jsp";
			if(inv_flag.equals("UG")) {
				filenm="frm_purchase_ltcora_sug_invoice.jsp";
			}
			if(inv_flag.equals("CR") || inv_flag.equals("DR"))
			{
				filenm="frm_purchase_crdr_preparation.jsp";
			}
			
			if(submission_from.equals("SERVICE"))
			{
				url = "../remittance/frm_purchase_service_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&accroid="+accroid+
						"&counterparty_cd="+filter_counterparty_cd+commonUrl_pra;
			}
			else
			{
				url = "../remittance/"+filenm+"?msg="+msg+"&msg_type="+msg_type+"&month="+month+"&year="+year+"&accroid="+accroid+
						"&billing_cycle="+billing_cycle+commonUrl_pra;
			}
			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - Purchase Remittance Received File Upload Failed!";

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
	
	private String InvoiceMailBody(String company_cd,String trader,String trader_abbr,String cont_type,String cont_no,String inv_no,String inv_period,String due_dt,
			String inv_amount, String net_payable,String activity_type,String inv_title,String flag,String invoice_raised_in,String invoice_dt,String remittanceNo, String inv_flag,String msg_content) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
			String invoice_type_nm=msg_content;
			String lineitem="Contract#";
			String inv_lineitem="Invoice";
			if(inv_flag.equals("CP") || inv_flag.equals("CF"))
			{
				inv_lineitem="Custom Duty";
			}
			
			String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
			
			String mail_subject=company_abbr+"/"+trader_abbr+"/"+cont_no+"/"+msg_content+"("+inv_period+")/"+inv_no+"";
			
			if(cont_type.equals("Y") || cont_type.equals("A") || cont_type.equals("H"))
			{
				mail_subject=company_abbr+"/"+trader_abbr+"/"+cont_no+"/"+msg_content+"("+invoice_dt+")/"+inv_no+"";
			}
			
			if(cont_type.equals("N")){
				lineitem="Cargo#";
			}
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Approved</font>, Please start Fin Ops Finalization Process!</span><br><br>";
					
					mail_subject+=" - IRP Approved!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Approve!</span><br><br>";
					mail_subject+=" - IRP Approve Rejected!";
				}
			}
			else if(activity_type.equals("APPROVE"))
			{
				if(flag.equals("A"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Fin Ops Finalized</font>, You may proceed for PDF generation!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalized!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while Fin Ops Finalization!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalization Rejected!";
				}
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Checked</font>";
					if(!inv_flag.equals("PF")) {
						mailBody+= ", Please start IRP Approve Process!";
					}
					mailBody+= "</span><br><br>";
					
					mail_subject+=" IRP Checked OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+invoice_type_nm+" is "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Trader</b>&nbsp;</td><td>&nbsp;"+trader+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;"+lineitem+"</b>&nbsp;</td><td>&nbsp;"+cont_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;"+inv_lineitem+"#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Remittance#</b>&nbsp;</td><td>&nbsp;"+remittanceNo+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;"+inv_lineitem+" Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>";
			if(!cont_type.equals("Y") && !cont_type.equals("A") && !cont_type.equals("H"))
			{
				mailBody+= "<tr><td><b>&nbsp;"+inv_lineitem+" Period</b>&nbsp;</td><td>&nbsp;"+inv_period+"&nbsp;</td></tr>";
			}
			mailBody+= "<tr><td><b>&nbsp;"+inv_lineitem+" Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;"+inv_lineitem+" Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "<tr><td><b>&nbsp;Net Payable</b>&nbsp;</td><td>&nbsp;"+net_payable+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "</table>";
			mailBody+= CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_list="";
			String cc_mail_list="";
			
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
				}
				
				to_mail_list=to_mail_list.equals("")?"":to_mail_list+",";
				to_mail_list += utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
				
				cc_mail_list=cc_mail_list.equals("")?"":cc_mail_list+",";
				cc_mail_list += utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
			}
			else if(activity_type.equals("APPROVE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
				}
				
				to_mail_list=to_mail_list.equals("")?"":to_mail_list+",";
				to_mail_list += utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
				
				cc_mail_list=cc_mail_list.equals("")?"":cc_mail_list+",";
				cc_mail_list += utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
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
	
	private String FFlowInvoiceMailBody(String company_cd,String trader,String trader_abbr,String cont_no,String cont_ref,String inv_no,String inv_period,String due_dt,
			String inv_amount,String net_payable,String activity_type,String inv_type,String flag,String invoice_raised_in,String invoice_dt,String remittanceNo,String inv_head) throws Exception
	{
		String function_nm="FFlowInvoiceMailBody()";
		String mailBody="";
		try
		{
			String inv_nm="";
            if(inv_type.equals("CR"))
			{
            	inv_nm="Purchase Credit Note Remittance";
			}
			else if(inv_type.equals("DR"))
			{
				inv_nm="Purchase Debit Note Remittance";
			}
			else if(inv_type.equals("LP"))
			{
				inv_nm="Purchase Late Payment Remittance";
			}
			else if(inv_type.equals("OR"))
			{
				inv_nm="Purchase Other Remittance";
			}
			else
			{
				inv_nm="Purchase Remittance";
			}
            
			//String mail_subject=inv_nm+" Remittance "+inv_no+"";
            String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
			String mail_subject=company_abbr+"/"+trader_abbr+"/"+cont_no+"/"+inv_nm+"("+inv_period+")/"+inv_no+"";
			
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Approved</font>, Please start Fin Ops Finalization Process!</span><br><br>";
					
					mail_subject+=" - IRP Approved!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Approve!</span><br><br>";
					mail_subject+=" - IRP Approve Rejected!";
				}
			}
			else if(activity_type.equals("APPROVE"))
			{
				if(flag.equals("A"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Fin Ops Finalized</font>, You may proceed for PDF generation!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalized!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while Fin Ops Finalization!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalization Rejected!";
				}
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Checked</font>";
					mailBody+= "</span><br><br>";
					
					mail_subject+=" IRP Checked OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Trader</b>&nbsp;</td><td>&nbsp;"+trader+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+cont_no+" ["+cont_ref+"]&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Sub Invoice Head#</b>&nbsp;</td><td>&nbsp;"+utilBean.getSubInvoiceHeaderNameByType("T", inv_head)+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Remittance#</b>&nbsp;</td><td>&nbsp;"+remittanceNo+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Period</b>&nbsp;</td><td>&nbsp;"+inv_period+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "<tr><td><b>&nbsp;Net Payable</b>&nbsp;</td><td>&nbsp;"+net_payable+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "</table>";
			mailBody+= CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_list="";
			String cc_mail_list="";
			
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
				}
				
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
			}
			else if(activity_type.equals("APPROVE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "Purchase", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "Purchase", "NA", "On-Event");
				}
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "Purchase", "NA", "On-Event");
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
			
			String gemail_from = request.getParameter("email_from")==null?"":request.getParameter("email_from");
			String email_to = request.getParameter("email_to")==null?"":request.getParameter("email_to");
			String email_cc = request.getParameter("email_cc")==null?"":request.getParameter("email_cc");
			String email_bcc = request.getParameter("email_bcc")==null?"":request.getParameter("email_bcc");
			String subject = request.getParameter("subject")==null?"":request.getParameter("subject");
			String[] attachment = request.getParameterValues("attachment");
			String email_body = request.getParameter("email_body")==null?"":request.getParameter("email_body");
			
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type"); 
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag"); 
			boolean isMailSent=false;
			int entryUpdated=0;
			email_body=email_body.replaceAll("\n", "<br>");
			email_body="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body+"</span>"
					+ "</html>";
			
			if(!email_to.equals("") && !email_body.equals("") && attachment!=null)
			{
				isMailSent=mailDelv.sendMailWithMultipleAttachment(comp_cd,email_to, subject, email_body, attachment, email_cc, email_bcc);
			}
			
			if(isMailSent)
			{
				int entryExist=0;
				
				queryString="SELECT COUNT(*) "
        		  		+ "FROM FMS_PUR_INV_FILE_DTL "
        		  		+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, invoice_seq);
				stmt.setString(3, financial_year);
				stmt.setString(4, contract_type);
				stmt.setString(5, inv_flag);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					entryExist=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				
				if(entryExist > 0)
				{
					
					  queryString="UPDATE FMS_PUR_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
							  + "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? "; 
					  stmt=dbcon.prepareStatement(queryString); 
					  stmt.setString(1, "Y");
					  stmt.setString(2, emp_cd); 
					  stmt.setString(3, comp_cd); 
					  stmt.setString(4,contract_type); 
					  stmt.setString(5, invoice_seq); 
					  stmt.setString(6,financial_year); 
					  stmt.setString(7, inv_flag); 
					  stmt.executeUpdate();
					  entryUpdated++;
					 
				}
			}
			
			msg="Mail Sent for "+subject;
			msg_type="S";
				
			if(entryUpdated>0)
			{
				dbcon.commit();
			}
			
			url = "../remittance/frm_purchase_invoice_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - Purchase Remittance Email Send Failed!";
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
	
	private void InsertUpdatePDBondDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdatePDBondDetail()";
		
		try
		{
			String month=request.getParameter("month")==null?"":request.getParameter("month");
			String year=request.getParameter("year")==null?"":request.getParameter("year");
			String billing_cycle=request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String pd_bond_sign = request.getParameter("pd_bond_sign")==null?"":request.getParameter("pd_bond_sign");
			String pd_bond_value = request.getParameter("pd_bond_value")==null?"":request.getParameter("pd_bond_value");
			String pd_bond_value_unit = request.getParameter("pd_bond_value_unit")==null?"":request.getParameter("pd_bond_value_unit");
			
			String seq_no="1";
			query="SELECT NVL(MAX(SEQ_NO),0) "
					+ "FROM FMS_CUSTOM_PD_BOND_DTL "
					+ "WHERE COMPANY_CD=? AND CAL_YEAR=? ";
			stmt=dbcon.prepareStatement(query);
       	 	stmt.setString(1, comp_cd);
	        stmt.setString(2, year);
	        rset=stmt.executeQuery();
	        if(rset.next())
	        {
	        	seq_no=""+rset.getInt(1)+1;
	        }
	        rset.close();
	        stmt.close();
	        
	        if(pd_bond_sign.equals("-"))
	        {
	        	pd_bond_value=pd_bond_sign+""+pd_bond_value;
	        }
	        
	        query="INSERT INTO FMS_CUSTOM_PD_BOND_DTL(COMPANY_CD,CAL_YEAR,SEQ_NO,PD_BOND,PD_BOND_UNIT,ENT_BY,ENT_DT) "
	        		+ "VALUES(?,?,?,?,?,?,SYSDATE)";
	        stmt=dbcon.prepareStatement(query);
       	 	stmt.setString(1, comp_cd);
	        stmt.setString(2, year);
	        stmt.setString(3, seq_no);
	        stmt.setString(4, pd_bond_value);
	        stmt.setString(5, pd_bond_value_unit);
	        stmt.setString(6, emp_cd);
	        stmt.executeUpdate();
	        
	        stmt.close();
	        
	        msg = "Successful - PD Bond Value for YEAR "+year+" Updated Successfully!";
			msg_type="S";
			
			url = "../remittance/frm_purchase_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&month="+month+"&year="+year+"&accroid="+accroid+
					"&billing_cycle="+billing_cycle+commonUrl_pra;
			
			dbcon.commit();
	        
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - PD Bond Value Insert/Update Failed!";
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
			String invoice_type =request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
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
			String billing_cycle = request.getParameter("billing_freq")==null?"":request.getParameter("billing_freq");
			String inv_flag = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
			
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
			String invoice_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			String sug_qty = request.getParameter("sug_qty")==null?"":request.getParameter("sug_qty");
			String sug_percentage = request.getParameter("sug_percent")==null?"":request.getParameter("sug_percent");
			
			String tcs_struct_cd = request.getParameter("tcs_struct_cd")==null?"":request.getParameter("tcs_struct_cd");
			String tcs_struct_dt = request.getParameter("tcs_struct_dt")==null?"":request.getParameter("tcs_struct_dt");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_factor = request.getParameter("tds_factor")==null?"":request.getParameter("tds_factor");
			String tds_struct_cd = request.getParameter("tds_struct_cd")==null?"":request.getParameter("tds_struct_cd");
			String tds_struct_dt = request.getParameter("tds_struct_dt")==null?"":request.getParameter("tds_struct_dt");
			String applicable_flag = request.getParameter("applicable_flag")==null?"":request.getParameter("applicable_flag");
			String applicable_abbr = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			
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
			String invoice_ref = request.getParameter("invoice_ref")==null?"":request.getParameter("invoice_ref");
			
			String chk = request.getParameter("chk")==null?"":request.getParameter("chk");
			String auth = request.getParameter("auth")==null?"":request.getParameter("auth");
			String aprv = request.getParameter("aprv")==null?"":request.getParameter("aprv");
			
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
			String new_tcs_struct_cd = request.getParameter("new_tcs_struct_cd")==null?"":request.getParameter("new_tcs_struct_cd");
			String new_tcs_struct_dt = request.getParameter("new_tcs_struct_dt")==null?"":request.getParameter("new_tcs_struct_dt");
			String new_tds_amt = request.getParameter("new_tds_amt")==null?"":request.getParameter("new_tds_amt");
			String new_tds_factor = request.getParameter("new_tds_factor")==null?"":request.getParameter("new_tds_factor");
			String new_tds_struct_cd = request.getParameter("new_tds_struct_cd")==null?"":request.getParameter("new_tds_struct_cd");
			String new_tds_struct_dt = request.getParameter("new_tds_struct_dt")==null?"":request.getParameter("new_tds_struct_dt");
			
			String[] new_sub_tax_struct = request.getParameterValues("new_sub_tax_struct");
			String[] new_sub_tax_amt = request.getParameterValues("new_sub_tax_amt");
			String[] new_sub_tax_code = request.getParameterValues("new_sub_tax_code");
			String[] new_sub_tax_base_amt = request.getParameterValues("new_sub_tax_base_amt");
			/////
			//String sys_invoice_seq="";
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
			
			String table_name="FMS_PUR_SG_INV_MST"; 
			if(sgpg_type.equals("PG"))
			{
				table_name="FMS_PUR_PG_INV_MST"; 
			}
			financial_year=dateUtil.getFinancialYear(invoice_dt);
			if(opration.equals("PREPARE"))
			{
				int count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND REF_NO=? "
						+ "UNION ALL "
						+ "SELECT COUNT(*) "
						+ "FROM FMS_PUR_PG_INV_MST "
						+ "WHERE COMPANY_CD=? AND REF_NO=? ";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, sel_inv_no);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, sel_inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(count==0)
				{
					financial_year=dateUtil.getFinancialYear(invoice_dt);
					new_financial_year=financial_year;
					
					String inv_seq="1";
					//if(sgpg_type.equals("SG")) 
					{
						query="SELECT MAX(SEQ) FROM "
								+ "(SELECT MAX(INVOICE_SEQ) AS SEQ "
								+ "FROM FMS_PUR_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND CONTRACT_TYPE=? "
								+ "UNION ALL "
								+ "SELECT MAX(INVOICE_SEQ) AS SEQ "
								+ "FROM FMS_PUR_PG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND CONTRACT_TYPE=?) ";
						stmt4=dbcon.prepareStatement(query);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, financial_year);
						stmt4.setString(3, contract_type);
						stmt4.setString(4, comp_cd);
						stmt4.setString(5, financial_year);
						stmt4.setString(6, contract_type);
						//stmt4.setString(4, sel_inv_no);
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							inv_seq = ""+(rset4.getInt(1)+1);
						}
						rset4.close();
						stmt4.close();
					}
					
					
					invoice_seq = inv_seq;
					new_invoice_seq=invoice_seq;
					
					String fin_yr="";
					if(!financial_year.equals(""))
					{
						String[] temp = financial_year.split("-");
						fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
					}
					
					if(!financial_year.equals("") && !contract_type.equals(""))
					{
						String invoice_prefix=utilBean.getInvoicePrefix(dbcon,comp_cd);
					
						invoice_no=invoice_prefix+""+contract_type+"S"+invoice_type+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;	
					}
					
					int ins_cnt=0;
					query1="INSERT INTO "+table_name+"(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
							+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
							+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
							+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
							+ "INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,"
							+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,TCS_CERT_FLAG,TCS_TDS,"
							+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,SYS_INV_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
							+ "SUG_PERCENT,SUG_QTY,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,CRITERIA,REF_NO,CARGO_NO ";
					query1+= ") "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,SYSDATE,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,? ";
					query1+= ") ";
					int st_count=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++st_count, comp_cd);
					stmt1.setString(++st_count, counterparty_cd);
					stmt1.setString(++st_count, agmt_no);
					stmt1.setString(++st_count, agmt_rev);
					stmt1.setString(++st_count, cont_no);
					stmt1.setString(++st_count, cont_rev);
					stmt1.setString(++st_count, contract_type);
					stmt1.setString(++st_count, bu_unit);
					stmt1.setString(++st_count, bu_contact_person);
					stmt1.setString(++st_count, plant_seq);
					stmt1.setString(++st_count, contact_person);
					stmt1.setString(++st_count, invoice_seq);
					stmt1.setString(++st_count, invoice_ref);
					stmt1.setString(++st_count, invoice_dt);
					stmt1.setString(++st_count, billing_cycle);
					stmt1.setString(++st_count, period_start_dt);
					stmt1.setString(++st_count, period_end_dt);
					stmt1.setString(++st_count, invoice_due_dt);
					stmt1.setString(++st_count, alloc_qty);
					stmt1.setString(++st_count, price);
					stmt1.setString(++st_count, price_cd);
					stmt1.setString(++st_count, gross_amt);
					stmt1.setString(++st_count, exchng_cd);
					stmt1.setString(++st_count, exchng_dt);
					stmt1.setString(++st_count, exchng_rate);
					stmt1.setString(++st_count, invoice_raised_in);
					stmt1.setString(++st_count, gross_amt1);
					stmt1.setString(++st_count, tax_amt);
					stmt1.setString(++st_count, tax_cd);
					stmt1.setString(++st_count, tax_dt);
					stmt1.setString(++st_count, invoice_amt);
					stmt1.setString(++st_count, net_payable);
					stmt1.setString(++st_count, emp_cd);
					stmt1.setString(++st_count, financial_year);
					stmt1.setString(++st_count, tcs_amt);
					stmt1.setString(++st_count, tcs_factor);
					stmt1.setString(++st_count, tcs_struct_cd);
					stmt1.setString(++st_count, tcs_struct_dt);
					stmt1.setString(++st_count, applicable_flag);
					stmt1.setString(++st_count, applicable_abbr);
					stmt1.setString(++st_count, tds_amt);
					stmt1.setString(++st_count, tds_factor);
					stmt1.setString(++st_count, tds_struct_cd);
					stmt1.setString(++st_count, tds_struct_dt);
					stmt1.setString(++st_count, invoice_no);
					stmt1.setString(++st_count, inv_flag);
					stmt1.setString(++st_count, "");//cif_amt
					stmt1.setString(++st_count, "");//assessable_amt
					stmt1.setString(++st_count, remark1);
					stmt1.setString(++st_count, "");//diff_price
					stmt1.setString(++st_count, "");//cd_paid_amt
					stmt1.setString(++st_count, "");//sug_percent
					stmt1.setString(++st_count, sug_qty);
					stmt1.setString(++st_count, "");//qty_unit
					stmt1.setString(++st_count, transportation_tariff);
				    stmt1.setString(++st_count, transportation_amount);
				    stmt1.setString(++st_count, marketing_margin);
				    stmt1.setString(++st_count, marketing_margin_amount);
				    stmt1.setString(++st_count, other_charges);
				    stmt1.setString(++st_count, other_charges_amount);
				    stmt1.setString(++st_count, criteri_formula);
				    stmt1.setString(++st_count, sel_inv_no);
				    stmt1.setString(++st_count, cargo_no);
				    ins_cnt=stmt1.executeUpdate();
				    isOperated=true;
					stmt1.close();
					
					if(ins_cnt>0)
					{
						msg = "Successful! - "+invdtl+" with "+invoice_no+" Submitted!";
						msg_type="S";
						
						int cnt=0;

						query2="INSERT INTO FMS_PUR_INV_CRDR_REF(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,"
								+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,"
								+ "TAX_AMT,TAX_STRUCT_CD,INVOICE_AMT,NET_PAYABLE_AMT,TCS_CERT_FLAG,TCS_TDS,";
							if(applicable_abbr.equals("TCS"))
							{
								query2+= "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,";
							}
							else if(applicable_abbr.equals("TDS"))
							{
								query2+= "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,";
							}
							query2+= "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,"
							+ "OTHER_CHARGES,OTHER_CHARGES_AMT,ENT_BY,ENT_DT,IS_SGPG,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,";
							if(applicable_abbr.equals("TCS"))
							{
								query2+= "?,?,?,TO_DATE(?,'DD/MM/YYYY'),";
							}
							else if(applicable_abbr.equals("TDS"))
							{
								query2+= "?,?,?,TO_DATE(?,'DD/MM/YYYY'),";
							}
							query2+= "?,?,?,?,"
							+ "?,?,?,SYSDATE,?,?)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(++cnt, comp_cd);
						stmt2.setString(++cnt, counterparty_cd);
						stmt2.setString(++cnt, agmt_no);
						stmt2.setString(++cnt, cont_no);
						stmt2.setString(++cnt, contract_type);
						stmt2.setString(++cnt, financial_year);
						stmt2.setString(++cnt, invoice_seq);
						stmt2.setString(++cnt, new_alloc_qty);
						stmt2.setString(++cnt, new_price);
						stmt2.setString(++cnt, price_cd);
						stmt2.setString(++cnt, new_gross_amt);
						stmt2.setString(++cnt, exchng_cd);
						stmt2.setString(++cnt, new_exchng_rate);
						stmt2.setString(++cnt, invoice_raised_in);
						stmt2.setString(++cnt, new_gross_amt1);
						stmt2.setString(++cnt, new_tax_amt);
						stmt2.setString(++cnt, new_tax_cd);
						stmt2.setString(++cnt, new_invoice_amt);
						stmt2.setString(++cnt, new_net_payable);
						stmt2.setString(++cnt, applicable_flag);
						stmt2.setString(++cnt, applicable_abbr);
						if(applicable_abbr.equals("TCS"))
						{
							stmt2.setString(++cnt, new_tcs_amt);
							stmt2.setString(++cnt, new_tcs_factor);
							stmt2.setString(++cnt, new_tcs_struct_cd);
							stmt2.setString(++cnt, new_tcs_struct_dt);
						}
						else if(applicable_abbr.equals("TDS"))
						{
							stmt2.setString(++cnt, new_tds_amt);
							stmt2.setString(++cnt, new_tds_factor);
							stmt2.setString(++cnt, new_tds_struct_cd);
							stmt2.setString(++cnt, new_tds_struct_dt);
						}
						stmt2.setString(++cnt, new_transportation_tariff);
						stmt2.setString(++cnt, new_transportation_amount);
						stmt2.setString(++cnt, new_marketing_margin);
						stmt2.setString(++cnt, new_marketing_margin_amount);
						stmt2.setString(++cnt, new_other_charges);
						stmt2.setString(++cnt, new_other_charges_amount);
						stmt2.setString(++cnt, emp_cd);
						stmt2.setString(++cnt, sgpg_type);
						stmt2.setString(++cnt, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					else
					{
						msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Submitted!";
						msg_type="E";
					}
				}
			}
			else if(opration.equals("MODIFY"))
			{
				
				int count=0;
				query="SELECT COUNT(*) "
						+ "FROM "+table_name+" "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND CONTRACT_TYPE=? AND INVOICE_SEQ=? AND SYS_INV_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, contract_type);
				stmt.setString(4, invoice_seq);
				stmt.setString(5, invoice_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0)
				{
					 int upd_cnt=0;
					 int update_cnt=0;
					 query1="UPDATE "+table_name+" SET BU_CONTACT_PERSON_CD=?, CONTACT_PERSON_CD=?, INVOICE_NO=?, "
					 		+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
					 		+ "ALLOC_QTY=?, SALE_PRICE=?, SALE_PRICE_UNIT=?, SALE_AMT=?, "
					 		+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
					 		+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
					 		+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, "
					 		+ "NET_PAYABLE_AMT=?,"
					 		+ "TCS_AMT=?,TCS_FACTOR=?,TCS_STRUCT_CD=?,"
					 		+ "TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),TCS_CERT_FLAG=?,TCS_TDS=?,"
					 		+ "TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,"
					 		+ "TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),REMARK=?,INV_FLAG=?,"
					 		+ "TRANSPORTATION_CHARGE=?,TRANSPORTATION_AMOUNT=?,MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,CRITERIA=? "
					 		+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND SYS_INV_NO=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=? AND REF_NO=? AND INV_FLAG IN ('CR','DR')";
					 stmt1=dbcon.prepareStatement(query1);
					 stmt1.setString(++upd_cnt, bu_contact_person);
					 stmt1.setString(++upd_cnt, contact_person);
					 stmt1.setString(++upd_cnt, invoice_ref);
					 stmt1.setString(++upd_cnt, invoice_dt);
					 stmt1.setString(++upd_cnt, invoice_due_dt);
					 stmt1.setString(++upd_cnt, alloc_qty);
					 stmt1.setString(++upd_cnt, price);
					 stmt1.setString(++upd_cnt, price_cd);
					 stmt1.setString(++upd_cnt, gross_amt);
					 stmt1.setString(++upd_cnt, exchng_cd);
					 stmt1.setString(++upd_cnt, exchng_dt);
					 stmt1.setString(++upd_cnt, exchng_rate);
					 stmt1.setString(++upd_cnt, invoice_raised_in);
					 stmt1.setString(++upd_cnt, gross_amt1);
					 stmt1.setString(++upd_cnt, tax_amt);
					 stmt1.setString(++upd_cnt, tax_cd);
					 stmt1.setString(++upd_cnt, tax_dt);
					 stmt1.setString(++upd_cnt, invoice_amt);
					 stmt1.setString(++upd_cnt, net_payable);
					 stmt1.setString(++upd_cnt, tcs_amt);
					 stmt1.setString(++upd_cnt, tcs_factor);
					 stmt1.setString(++upd_cnt, tcs_struct_cd);
					 stmt1.setString(++upd_cnt, tcs_struct_dt);
					 stmt1.setString(++upd_cnt, applicable_flag);
					 stmt1.setString(++upd_cnt, applicable_abbr);
					 stmt1.setString(++upd_cnt, tds_amt);
					 stmt1.setString(++upd_cnt, tds_factor);
					 stmt1.setString(++upd_cnt, tds_struct_cd);
					 stmt1.setString(++upd_cnt, tds_struct_dt);
					 stmt1.setString(++upd_cnt, remark1);
					 stmt1.setString(++upd_cnt, inv_flag);
					 stmt1.setString(++upd_cnt, transportation_tariff);
					 stmt1.setString(++upd_cnt, transportation_amount);
					 stmt1.setString(++upd_cnt, marketing_margin);
					 stmt1.setString(++upd_cnt, marketing_margin_amount);
					 stmt1.setString(++upd_cnt, other_charges);
					 stmt1.setString(++upd_cnt, other_charges_amount);
					 stmt1.setString(++upd_cnt, criteri_formula);
					 stmt1.setString(++upd_cnt, comp_cd);
					 stmt1.setString(++upd_cnt, invoice_seq);
					 stmt1.setString(++upd_cnt, invoice_no);
					 stmt1.setString(++upd_cnt, financial_year);
					 stmt1.setString(++upd_cnt, contract_type);
					 stmt1.setString(++upd_cnt, sel_inv_no);
					 update_cnt=stmt1.executeUpdate();
					 isOperated=true;
					 stmt1.close();
					 
					 if(update_cnt>0)
					 {
						 msg = "Successful! - "+invdtl+" with "+invoice_no+" Modified!";
						 msg_type="S";
						 
						 int cnt=0;
						 query2="UPDATE FMS_PUR_INV_CRDR_REF SET ALLOC_QTY=?,SALE_PRICE=?,SALE_AMT=?,EXCHG_RATE_CD=?,EXCHG_RATE_VALUE=?,"
						 		+ "INVOICE_RAISED_IN=?,GROSS_AMT=?,TAX_AMT=?,TAX_STRUCT_CD=?,INVOICE_AMT=?,NET_PAYABLE_AMT=?,TCS_CERT_FLAG=?,"
						 		+ "TCS_TDS=?,TCS_AMT=?,TCS_FACTOR=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
						 		+ "TRANSPORTATION_CHARGE=?,TRANSPORTATION_AMOUNT=?,MARKET_MARGIN=?,MARKET_MARGIN_AMT=?,OTHER_CHARGES=?,OTHER_CHARGES_AMT=?,"
						 		+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
						 		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND IS_SGPG=?";
						 stmt2=dbcon.prepareStatement(query2);
						 stmt2.setString(++cnt, new_alloc_qty);
						 stmt2.setString(++cnt, new_price);
						 stmt2.setString(++cnt, new_gross_amt);
						 stmt2.setString(++cnt, exchng_cd);
						 stmt2.setString(++cnt, new_exchng_rate);
						 stmt2.setString(++cnt, invoice_raised_in);
						 stmt2.setString(++cnt, new_gross_amt1);
						 stmt2.setString(++cnt, new_tax_amt);
						 stmt2.setString(++cnt, new_tax_cd);
						 stmt2.setString(++cnt, new_invoice_amt);
						 stmt2.setString(++cnt, new_net_payable);
						 stmt2.setString(++cnt, applicable_flag);
						 stmt2.setString(++cnt, applicable_abbr);
						 stmt2.setString(++cnt, new_tcs_amt);
						 stmt2.setString(++cnt, new_tcs_factor);
						 stmt2.setString(++cnt, new_tcs_struct_cd);
						 stmt2.setString(++cnt, new_tcs_struct_dt);
						 stmt2.setString(++cnt, new_tds_amt);
						 stmt2.setString(++cnt, new_tds_factor);
						 stmt2.setString(++cnt, new_tds_struct_cd);
						 stmt2.setString(++cnt, new_tds_struct_dt);
						 stmt2.setString(++cnt, new_transportation_tariff);
						 stmt2.setString(++cnt, new_transportation_amount);
						 stmt2.setString(++cnt, new_marketing_margin);
						 stmt2.setString(++cnt, new_marketing_margin_amount);
						 stmt2.setString(++cnt, new_other_charges);
						 stmt2.setString(++cnt, new_other_charges_amount);
						 stmt2.setString(++cnt, emp_cd);
						 stmt2.setString(++cnt, comp_cd);
						 stmt2.setString(++cnt, contract_type);
						 stmt2.setString(++cnt, financial_year);
						 stmt2.setString(++cnt, invoice_seq);
						 stmt2.setString(++cnt, sgpg_type);
						 stmt2.executeUpdate();
						 stmt2.close();
					 }
					 else
					 {
						 msg = "Failed! - "+invdtl+" with "+invoice_no+" Modification Failed!";
						 msg_type="E";
					 }
				}
			}
			else if(opration.equals("CHECK"))
			{
				int chk_cnt=0;
				query1="UPDATE "+table_name+" SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, chk);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				chk_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(chk_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Checked!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Checked!";
					msg_type="E";
				}
			}
			else if(opration.equals("AUTHORIZE"))
			{
				int auth_cnt=0;
				query1="UPDATE "+table_name+" SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, auth);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				auth_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(auth_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Authorized!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Authorized!";
					msg_type="E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				int aprv_cnt=0;
				query1="UPDATE "+table_name+" SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, aprv);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				aprv_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(aprv_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Approved!";
					msg_type="E";
				}
			}
			
			if(isOperated)
			{
				String tbl_name="FMS_PUR_SG_INV_TAX_DTL"; 
				if(sgpg_type.equals("PG"))
				{
					tbl_name="FMS_PUR_PG_INV_TAX_DTL"; 
				}
				
				int tax_count=0;
				query2="SELECT COUNT(*) "
						+ "FROM "+tbl_name+" "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, contract_type);
				stmt2.setString(3, invoice_seq);
				stmt2.setString(4, financial_year);
				stmt2.setString(5, inv_flag);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					tax_count=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				
				if(tax_count>0)
				{
					query3="DELETE FROM "+tbl_name+" "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
					stmt3=dbcon.prepareStatement(query3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, contract_type);
					stmt3.setString(3, invoice_seq);
					stmt3.setString(4, financial_year);
					stmt3.setString(5, inv_flag);
					stmt3.executeQuery();
					
					stmt3.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query4="INSERT INTO "+tbl_name+"(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,INV_FLAG) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE,?)";
						stmt4=dbcon.prepareStatement(query4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, contract_type);
						stmt4.setString(3, invoice_seq);
						stmt4.setString(4, financial_year);
						stmt4.setString(5, tax_cd);
						stmt4.setString(6, sub_tax_code[i]);
						stmt4.setString(7, tax_dt);
						stmt4.setString(8, sub_tax_struct[i]);
						stmt4.setString(9, sub_tax_amt[i]);
						stmt4.setString(10, sub_tax_base_amt[i]);
						stmt4.setString(11, emp_cd);
						stmt4.setString(12, inv_flag);
						stmt4.executeUpdate();
						
						stmt4.close();
					}
				}
				
				tax_count=0;
				query2="SELECT COUNT(*) "
						+ "FROM FMS_PUR_INV_CRDR_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
				stmt2=dbcon.prepareStatement(query2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, contract_type);
				stmt2.setString(3, invoice_seq);
				stmt2.setString(4, financial_year);
				stmt2.setString(5, inv_flag);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					tax_count=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				
				if(tax_count>0)
				{
					query3="DELETE FROM FMS_PUR_INV_CRDR_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=?";
					stmt3=dbcon.prepareStatement(query3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, contract_type);
					stmt3.setString(3, invoice_seq);
					stmt3.setString(4, financial_year);
					stmt3.setString(5, inv_flag);
					stmt3.executeQuery();
					
					stmt3.close();
				}
				
				if(sub_tax_code!=null)
				{
					for(int i=0; i<sub_tax_code.length;i++)
					{
						query4="INSERT INTO FMS_PUR_INV_CRDR_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
								+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,INV_FLAG,IS_SGPG) "
								+ "VALUES(?,?,?,?,"
								+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE,?,?)";
						stmt4=dbcon.prepareStatement(query4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, contract_type);
						stmt4.setString(3, invoice_seq);
						stmt4.setString(4, financial_year);
						stmt4.setString(5, new_tax_cd);
						stmt4.setString(6, new_sub_tax_code[i]);
						stmt4.setString(7, tax_dt);
						stmt4.setString(8, new_sub_tax_struct[i]);
						stmt4.setString(9, new_sub_tax_amt[i]);
						stmt4.setString(10, new_sub_tax_base_amt[i]);
						stmt4.setString(11, emp_cd);
						stmt4.setString(12, inv_flag);
						stmt4.setString(13, sgpg_type);
						stmt4.executeUpdate();
						
						stmt4.close();
					}
				}
			}
			
			
			url = "../remittance/frm_generate_purchase_crdr.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+
					"&cargo_no="+cargo_no+"&inv_flag="+inv_flag+"&accroid="+accroid+"&crdr_gen_type="+crdr_gen_type+"&sel_inv_no="+sel_inv_no+"&invoice_seq="+invoice_seq+
					"&crdr_no="+invoice_no+"&financial_year="+financial_year+"&sgpg_type="+sgpg_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Inser/Update Purchase CR/DR Failed!";
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
	
	private void SendCrdrInvoiceMail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="SendCrdrInvoiceMail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			
			String gemail_from = request.getParameter("email_from")==null?"":request.getParameter("email_from");
			String email_to = request.getParameter("email_to")==null?"":request.getParameter("email_to");
			String email_cc = request.getParameter("email_cc")==null?"":request.getParameter("email_cc");
			String email_bcc = request.getParameter("email_bcc")==null?"":request.getParameter("email_bcc");
			String subject = request.getParameter("subject")==null?"":request.getParameter("subject");
			String[] attachment = request.getParameterValues("attachment");
			String email_body = request.getParameter("email_body")==null?"":request.getParameter("email_body");
			
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type"); 
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt"); 
			financial_year=dateUtil.getFinancialYear(invoice_dt);
			int entryUpdated=0;
			email_body=email_body.replaceAll("\n", "<br>");
			email_body="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body+"</span>"
					+ "</html>";
			boolean isMailSent=false;
			if(!email_to.equals("") && !email_body.equals("") && attachment!=null)
			{
				isMailSent=mailDelv.sendMailWithMultipleAttachment(comp_cd,email_to, subject, email_body, attachment, email_cc, email_bcc);
			}
			
			if(isMailSent)
			{
				int entryExist=0;
				
				queryString="SELECT COUNT(*) "
        		  		+ "FROM FMS_PUR_INV_FILE_DTL "
        		  		+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, invoice_seq);
				stmt.setString(3, financial_year);
				stmt.setString(4, contract_type);
				stmt.setString(5, invoice_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					entryExist=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				
				if(entryExist > 0)
				{
					
					  queryString="UPDATE FMS_PUR_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
							  + "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? "; 
					  stmt=dbcon.prepareStatement(queryString); 
					  stmt.setString(1, "Y");
					  stmt.setString(2, emp_cd); 
					  stmt.setString(3, comp_cd); 
					  stmt.setString(4,contract_type); 
					  stmt.setString(5, invoice_seq); 
					  stmt.setString(6,financial_year); 
					  stmt.setString(7, invoice_type); 
					  stmt.executeUpdate();
					  entryUpdated++;
					 
				}
			}
			
			msg="Mail Sent for "+subject;
			msg_type="S";
			
			if(entryUpdated>0)
			{
				dbcon.commit();
			}
			
			msg="Mail Sent for "+subject;
			msg_type="S";
				
			
			url = "../remittance/frm_purchase_crdr_invoice_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - Purchase Remittance Email Send Failed!";
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
	
	private void ChkAprvCreditDebitDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkAprvCreditDebitDetail()";
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
			String invoice_type =request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
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
			String billing_cycle = request.getParameter("billing_freq")==null?"":request.getParameter("billing_freq");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String sgpg_type = request.getParameter("sgpg_type")==null?"":request.getParameter("sgpg_type");
			
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
			String invoice_no = request.getParameter("crdr_no")==null?"":request.getParameter("crdr_no");
			String contract_ref = request.getParameter("contract_ref")==null?"":request.getParameter("contract_ref");
			String sug_qty = request.getParameter("sug_qty")==null?"":request.getParameter("sug_qty");
			String sug_percentage = request.getParameter("sug_percent")==null?"":request.getParameter("sug_percent");
			
			String tcs_struct_cd = request.getParameter("tcs_struct_cd")==null?"":request.getParameter("tcs_struct_cd");
			String tcs_struct_dt = request.getParameter("tcs_struct_dt")==null?"":request.getParameter("tcs_struct_dt");
			
			String tds_amt = request.getParameter("tds_amt")==null?"":request.getParameter("tds_amt");
			String tds_factor = request.getParameter("tds_factor")==null?"":request.getParameter("tds_factor");
			String tds_struct_cd = request.getParameter("tds_struct_cd")==null?"":request.getParameter("tds_struct_cd");
			String tds_struct_dt = request.getParameter("tds_struct_dt")==null?"":request.getParameter("tds_struct_dt");
			String applicable_flag = request.getParameter("applicable_flag")==null?"":request.getParameter("applicable_flag");
			String applicable_abbr = request.getParameter("tcs_tds")==null?"":request.getParameter("tcs_tds");
			
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
			String invoice_ref = request.getParameter("invoice_ref")==null?"":request.getParameter("invoice_ref");
			
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			
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
			String new_tcs_struct_cd = request.getParameter("new_tcs_struct_cd")==null?"":request.getParameter("new_tcs_struct_cd");
			String new_tcs_struct_dt = request.getParameter("new_tcs_struct_dt")==null?"":request.getParameter("new_tcs_struct_dt");
			String new_tds_amt = request.getParameter("new_tds_amt")==null?"":request.getParameter("new_tds_amt");
			String new_tds_factor = request.getParameter("new_tds_factor")==null?"":request.getParameter("new_tds_factor");
			String new_tds_struct_cd = request.getParameter("new_tds_struct_cd")==null?"":request.getParameter("new_tds_struct_cd");
			String new_tds_struct_dt = request.getParameter("new_tds_struct_dt")==null?"":request.getParameter("new_tds_struct_dt");
			
			String[] new_sub_tax_struct = request.getParameterValues("new_sub_tax_struct");
			String[] new_sub_tax_amt = request.getParameterValues("new_sub_tax_amt");
			String[] new_sub_tax_code = request.getParameterValues("new_sub_tax_code");
			String[] new_sub_tax_base_amt = request.getParameterValues("new_sub_tax_base_amt");
			/////
			//String sys_invoice_seq="";
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
			
			String table_name="FMS_PUR_SG_INV_MST"; 
			if(sgpg_type.equals("PG"))
			{
				table_name="FMS_PUR_PG_INV_MST"; 
			}
			financial_year=dateUtil.getFinancialYear(invoice_dt);
			if(opration.equals("CHECK"))
			{
				int chk_cnt=0;
				query1="UPDATE "+table_name+" SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, rd);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				chk_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(chk_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Checked!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Checked!";
					msg_type="E";
				}
			}
			else if(opration.equals("AUTHORIZE"))
			{
				int auth_cnt=0;
				query1="UPDATE "+table_name+" SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, rd);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				auth_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(auth_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Authorized!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Authorized!";
					msg_type="E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				int aprv_cnt=0;
				query1="UPDATE "+table_name+" SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? AND SYS_INV_NO=? AND REF_NO=?";
				
				int stcount=0;
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++stcount, rd);
				stmt1.setString(++stcount, emp_cd);
				stmt1.setString(++stcount, comp_cd);
				stmt1.setString(++stcount, counterparty_cd);
				stmt1.setString(++stcount, contract_type);
				stmt1.setString(++stcount, plant_seq);
				stmt1.setString(++stcount, bu_unit);
				stmt1.setString(++stcount, invoice_seq);
				stmt1.setString(++stcount, financial_year);
				stmt1.setString(++stcount, inv_flag);
				stmt1.setString(++stcount, invoice_no);
				stmt1.setString(++stcount, sel_inv_no);
				aprv_cnt=stmt1.executeUpdate();
				stmt1.close();
				
				if(aprv_cnt>0)
				{
					msg = "Successful! - "+invdtl+" with "+invoice_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" with "+invoice_no+" Not Approved!";
					msg_type="E";
				}
			}
			
			url = "../remittance/rpt_view_purchase_crdr_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+period_start_dt+
					"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&billing_cycle="+billing_cycle+"&operation="+operation+"&activityFlag="+operation+
					"&cargo_no="+cargo_no+"&inv_flag="+inv_flag+"&accroid="+accroid+"&crdr_gen_type="+crdr_gen_type+"&sel_inv_no="+sel_inv_no+"&invoice_seq="+invoice_seq+
					"&crdr_no="+invoice_no+"&financial_year="+financial_year+"&sgpg_type="+sgpg_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Inser/Update Purchase CR/DR Failed!";
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
