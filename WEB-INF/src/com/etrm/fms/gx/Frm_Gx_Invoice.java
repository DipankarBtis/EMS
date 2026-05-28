package com.etrm.fms.gx;

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

@WebServlet("/servlet/Frm_Gx_Invoice")
@MultipartConfig(fileSizeThreshold=1024*1024*20, 	// 20 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_Gx_Invoice extends HttpServlet
{
	static String db_src_file_name="Frm_Gx_Invoice.java";
	public static  Connection dbcon;

	public static  String servletName = "Frm_Gx_Invoice";
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
	static DataBean_Gx_Invoice DBgx = new DataBean_Gx_Invoice();
	
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
						u=request.getParameter("u")==null?"":request.getParameter("u");
	
						emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
						comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
						comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
						emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
						ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
	
						new_value="";
						old_value="";
	
						option=request.getParameter("option")==null?"":request.getParameter("option");
	
						commonUrl_pra = "&u="+u;
	
						if(option.equalsIgnoreCase("GX_TXN_INVOICE"))
						{
							InsertUpdateGxTransactionInvoiceDetail(request);
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
						else if(option.equalsIgnoreCase("INVOICE_SAP_APPROVE"))
						{
							InsertUpdateSapInvoiceApprove(request);
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

	private void InsertUpdateGxTransactionInvoiceDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateGxTransactionInvoiceDetail()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String gx_counterparty_cd = request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String billing_cycle = request.getParameter("billing_cycle")==null?"":request.getParameter("billing_cycle");
			String type = request.getParameter("type")==null?"":request.getParameter("type");

			String gx_bu_contact_person = request.getParameter("gx_bu_contact_person")==null?"":request.getParameter("gx_bu_contact_person");
			String gx_bu_unit = request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");

			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String gx_invoice_type = request.getParameter("gx_invoice_type")==null?"":request.getParameter("gx_invoice_type");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
			
			//String deal_no=contract_type+""+cont_no;
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, "");

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
			String sys_tds_amt = request.getParameter("sys_tds_amt")==null?"":request.getParameter("sys_tds_amt");
			String sys_tds_factor = request.getParameter("sys_tds_factor")==null?"":request.getParameter("sys_tds_factor");
			String sys_tds_cd = request.getParameter("sys_tds_cd")==null?"":request.getParameter("sys_tds_cd");
			String sys_tds_dt = request.getParameter("sys_tds_dt")==null?"":request.getParameter("sys_tds_dt");

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
			String party_tds_amt = request.getParameter("party_tds_amt")==null?"":request.getParameter("party_tds_amt");
			String party_tds_factor = request.getParameter("party_tds_factor")==null?"":request.getParameter("party_tds_factor");
			String party_tds_cd = request.getParameter("party_tds_cd")==null?"":request.getParameter("party_tds_cd");
			String party_tds_dt = request.getParameter("party_tds_dt")==null?"":request.getParameter("party_tds_dt");

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
			
			String sys_flag="";
			String party_flag="";
			
			String msgInfo=counterparty_abbr+" "+deal_no;
			
			int count=0;
			if(gx_invoice_type.equals("S")) //SYSTEM GENERATED
			{
				count=InvoiceDetailCount(invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, bu_unit, contract_type, gx_counterparty_cd, gx_bu_unit, sys_invoice_seq, submitted_fiscal_yr, gx_invoice_type);
			}
			else if(gx_invoice_type.equals("P"))
			{
				count=InvoiceDetailCount(invoice_type, comp_cd, counterparty_cd, cont_no, agmt_no, bu_unit, contract_type, gx_counterparty_cd, gx_bu_unit, party_invoice_seq, submitted_fiscal_yr, gx_invoice_type);
			}

			String new_invoice_seq=sys_invoice_seq;
			
			if(opration.equals("INSERT"))
			{
				int rem_count=0;
				if(count==0)
				{
					query="SELECT COUNT(*) "
							+ "FROM FMS_GX_TXN_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, gx_bu_unit);
					stmt.setString(6, bu_unit);
					stmt.setString(7, contract_type);
					stmt.setString(8, gx_counterparty_cd);
					stmt.setString(9, invoice_type);
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
								+ "FROM FMS_GX_TXN_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND CONTRACT_TYPE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, sys_financial_year);
						stmt.setString(3, gx_counterparty_cd);
						stmt.setString(4, invoice_type);
						stmt.setString(5, contract_type);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							inv_seq = ""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
		
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
							
							system_invoice_no=invoice_prefix+"E"+contract_type+invoice_type+utilBean.PrePaddingZero(sys_invoice_seq, 4)+"/"+fin_yr;//E IS DEFAULT
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
								+ "FROM FMS_GX_TXN_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND CONTRACT_TYPE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, sys_financial_year);
						stmt.setString(3, gx_counterparty_cd);
						stmt.setString(4, invoice_type);
						stmt.setString(5, contract_type);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							inv_seq = ""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						
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
							
							system_invoice_no=invoice_prefix+"E"+contract_type+invoice_type+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr;//E IS DEFAULT
						}
						
						//FOR SG
						query="UPDATE FMS_GX_TXN_SG_INV_MST SET FINANCIAL_YEAR=?,INVOICE_SEQ=?,SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, sys_financial_year);
					    stmt1.setString(2, new_invoice_seq);
					    stmt1.setString(3, system_invoice_no);
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, counterparty_cd);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, agmt_no);
						stmt1.setString(8, gx_bu_unit);
						stmt1.setString(9, bu_unit);
						stmt1.setString(10, contract_type);
						stmt1.setString(11, gx_counterparty_cd);
						stmt1.setString(12, invoice_type);
						stmt1.setString(13, sys_invoice_seq);
						stmt1.setString(14, submitted_fiscal_yr);
						stmt1.executeUpdate();
						stmt1.close();
						
						query3="UPDATE FMS_GX_TXN_SG_INV_TAX_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, new_invoice_seq);
					    stmt3.setString(2, sys_financial_year);
						stmt3.setString(3, comp_cd);
						stmt3.setString(4, contract_type);
						stmt3.setString(5, gx_counterparty_cd);
						stmt3.setString(6, invoice_type);
						stmt3.setString(7, sys_invoice_seq);
						stmt3.setString(8, submitted_fiscal_yr);
						stmt3.executeUpdate();
						stmt3.close();
						
						//FOR PG
						query="UPDATE FMS_GX_TXN_PG_INV_MST SET FINANCIAL_YEAR=?,INVOICE_SEQ=?,SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, sys_financial_year);
					    stmt1.setString(2, new_invoice_seq);
					    stmt1.setString(3, system_invoice_no);
						stmt1.setString(4, comp_cd);
						stmt1.setString(5, counterparty_cd);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, agmt_no);
						stmt1.setString(8, gx_bu_unit);
						stmt1.setString(9, bu_unit);
						stmt1.setString(10, contract_type);
						stmt1.setString(11, gx_counterparty_cd);
						stmt1.setString(12, invoice_type);
						stmt1.setString(13, sys_invoice_seq);
						stmt1.setString(14, submitted_fiscal_yr);
						stmt1.executeUpdate();
						stmt1.close();
						
						query3="UPDATE FMS_GX_TXN_PG_INV_TAX_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, new_invoice_seq);
					    stmt3.setString(2, sys_financial_year);
						stmt3.setString(3, comp_cd);
						stmt3.setString(4, contract_type);
						stmt3.setString(5, gx_counterparty_cd);
						stmt3.setString(6, invoice_type);
						stmt3.setString(7, sys_invoice_seq);
						stmt3.setString(8, submitted_fiscal_yr);
						stmt3.executeUpdate();
						stmt3.close();
						
						///FILE TABLE
						query1="UPDATE FMS_GX_FFLOW_INV_FILE_DTL SET INVOICE_SEQ=?,FINANCIAL_YEAR=? "
			        			+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
					        	+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
					        	+ "AND GX_COUNTERPARTY_CD=?";
			        	 stmt1=dbcon.prepareStatement(query1);
			        	 stmt1.setString(1, new_invoice_seq);
			        	 stmt1.setString(2, sys_financial_year);
			        	 stmt1.setString(3, comp_cd);
			        	 stmt1.setString(4, contract_type);
			        	 stmt1.setString(5, invoice_type);
			        	 stmt1.setString(6, sys_invoice_seq);
			        	 stmt1.setString(7, submitted_fiscal_yr);
			        	 stmt1.setString(8, gx_counterparty_cd);
			        	 stmt1.executeUpdate();
			        	 
			        	 stmt1.close();
			        	 
			        	 sys_invoice_seq=new_invoice_seq;
			        	 party_invoice_seq=new_invoice_seq;
							
			        	 party_financial_year=sys_financial_year;
			        	 
			        	 party_system_invoice_no=system_invoice_no;
					}
				}
				
				if(gx_invoice_type.equals("S")) //SYSTEM GENERATED
				{
					if(count > 0)
					{
						query="UPDATE FMS_GX_TXN_SG_INV_MST SET BU_CONTACT_PERSON_CD=?, GX_CONTACT_PERSON_CD=?, INVOICE_NO=?, "
								+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
								+ "ALLOC_QTY=?, TXN_RATE=?, RATE_UNIT=?, SALE_AMT=?, "
								+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
								+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
								+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, ADJUST_SIGN=?, ADJUST_AMT=?, "
								+ "NET_PAYABLE_AMT=?,FINANCIAL_YEAR=?,TCS_TDS=?,"
								+ "TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,"
								+ "TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, bu_contact_person);
						stmt1.setString(2, gx_bu_contact_person);
						stmt1.setString(3, sys_invoice_no);
						stmt1.setString(4, sys_invoice_dt);
						stmt1.setString(5, sys_invoice_due_dt);
						stmt1.setString(6, sys_alloc_qty);
						stmt1.setString(7, sys_price);
						stmt1.setString(8, sys_price_cd);
						stmt1.setString(9, sys_gross_amt);
						stmt1.setString(10, sys_exchng_cd);
						stmt1.setString(11, sys_exchng_dt);
						stmt1.setString(12, sys_exchng_rate);
						stmt1.setString(13, sys_invoice_raised_in);
						stmt1.setString(14, sys_gross_amt1);
						stmt1.setString(15, sys_tax_amt);
						stmt1.setString(16, sys_tax_cd);
						stmt1.setString(17, sys_tax_dt);
						stmt1.setString(18, sys_invoice_amt);
						stmt1.setString(19, sys_adj_plusmin);
						stmt1.setString(20, sys_adj_amt);
						stmt1.setString(21, sys_net_payable);
						stmt1.setString(22, sys_financial_year);
						stmt1.setString(23, applicable_abbr);
						stmt1.setString(24, sys_tds_amt);
						stmt1.setString(25, sys_tds_factor);
						stmt1.setString(26, sys_tds_cd);
						stmt1.setString(27, sys_tds_dt);
						stmt1.setString(28, system_invoice_no);
						stmt1.setString(29, comp_cd);
						stmt1.setString(30, counterparty_cd);
						stmt1.setString(31, cont_no);
						stmt1.setString(32, agmt_no);
						stmt1.setString(33, gx_bu_unit);
						stmt1.setString(34, bu_unit);
						stmt1.setString(35, contract_type);
						stmt1.setString(36, gx_counterparty_cd);
						stmt1.setString(37, invoice_type);
						stmt1.setString(38, sys_invoice_seq);
						stmt1.setString(39, sys_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(SG) Remittance for "+msgInfo+" Modified!";
						msg_type="S";
						
						stmt1.close();
						
						//UPDATING PARTY INVOICE NO WHEN SYS INVOICE NO GET CHANGED
						if(!sys_invoice_no.equals(temp_party_invoice_no) && !temp_party_invoice_no.equals(""))
						{
							query1="UPDATE FMS_GX_TXN_PG_INV_MST SET INVOICE_NO=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, party_invoice_no);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							stmt1.setString(4, cont_no);
							stmt1.setString(5, agmt_no);
							stmt1.setString(6, gx_bu_unit);
							stmt1.setString(7, bu_unit);
							stmt1.setString(8, contract_type);
							stmt1.setString(9, gx_counterparty_cd);
							stmt1.setString(10, invoice_type);
							stmt1.setString(11, party_invoice_seq);
							stmt1.setString(12, party_financial_year);
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else
					{
						query="INSERT INTO FMS_GX_TXN_SG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
								+ "BU_CONTACT_PERSON_CD,GX_BU_UNIT,GX_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
								+ "DUE_DT,"
								+ "ALLOC_QTY,TXN_RATE,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
								+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
								+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,GX_COUNTERPARTY_CD,INVOICE_TYPE,"
								+ "TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,SYS_INV_NO) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,SYSDATE,?,?,?,"
								+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, agmt_rev);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, cont_rev);
						stmt1.setString(7, contract_type);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, bu_contact_person);
						stmt1.setString(10, gx_bu_unit);
						stmt1.setString(11, gx_bu_contact_person);
						stmt1.setString(12, sys_invoice_seq);
						stmt1.setString(13, sys_invoice_no);
						stmt1.setString(14, sys_invoice_dt);
						stmt1.setString(15, sys_invoice_due_dt);
						stmt1.setString(16, sys_alloc_qty);
						stmt1.setString(17, sys_price);
						stmt1.setString(18, sys_price_cd);
						stmt1.setString(19, sys_gross_amt);
						stmt1.setString(20, sys_exchng_cd);
						stmt1.setString(21, sys_exchng_dt);
						stmt1.setString(22, sys_exchng_rate);
						stmt1.setString(23, sys_invoice_raised_in);
						stmt1.setString(24, sys_gross_amt1);
						stmt1.setString(25, sys_tax_amt);
						stmt1.setString(26, sys_tax_cd);
						stmt1.setString(27, sys_tax_dt);
						stmt1.setString(28, sys_invoice_amt);
						stmt1.setString(29, sys_adj_plusmin);
						stmt1.setString(30, sys_adj_amt);
						stmt1.setString(31, sys_net_payable);
						stmt1.setString(32, emp_cd);
						stmt1.setString(33, sys_financial_year);
						stmt1.setString(34, gx_counterparty_cd);
						stmt1.setString(35, invoice_type);
						stmt1.setString(36, applicable_abbr);
						stmt1.setString(37, sys_tds_amt);
						stmt1.setString(38, sys_tds_factor);
						stmt1.setString(39, sys_tds_cd);
						stmt1.setString(40, sys_tds_dt);
						stmt1.setString(41, system_invoice_no);
						
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(SG) Remittance for "+msgInfo+" Submitted!";
						msg_type="S";
						
						stmt1.close();
					}
					
					int tax_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_GX_TXN_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query1);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
					stmt2.setString(3, gx_counterparty_cd);
					stmt2.setString(4, invoice_type);
					stmt2.setString(5, sys_invoice_seq);
					stmt2.setString(6, sys_financial_year);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						tax_count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(tax_count>0)
					{
						query2="DELETE FROM FMS_GX_TXN_SG_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt3=dbcon.prepareStatement(query2);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, contract_type);
						stmt3.setString(3, gx_counterparty_cd);
						stmt3.setString(4, invoice_type);
						stmt3.setString(5, sys_invoice_seq);
						stmt3.setString(6, sys_financial_year);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					
					if(sys_sub_tax_code!=null)
					{
						for(int i=0; i<sys_sub_tax_code.length;i++)
						{
							queryString="INSERT INTO FMS_GX_TXN_SG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "GX_COUNTERPARTY_CD,INVOICE_TYPE,"
									+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?, "
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE)";
							stmt4=dbcon.prepareStatement(queryString);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, contract_type);
							stmt4.setString(3, sys_invoice_seq);
							stmt4.setString(4, sys_financial_year);
							stmt4.setString(5, gx_counterparty_cd);
							stmt4.setString(6, invoice_type);
							stmt4.setString(7, sys_tax_cd);
							stmt4.setString(8, sys_sub_tax_code[i]);
							stmt4.setString(9, sys_tax_dt);
							stmt4.setString(10, sys_sub_tax_struct[i]);
							stmt4.setString(11, sys_sub_tax_amt[i]);
							stmt4.setString(12, sys_sub_tax_base_amt[i]);
							stmt4.setString(13, emp_cd);
							stmt4.executeUpdate();
							
							stmt4.close();
						}
					}
				}
				else if(gx_invoice_type.equals("P")) //PARTY GENERATED
				{
					if(count > 0)
					{
						query="UPDATE FMS_GX_TXN_PG_INV_MST SET BU_CONTACT_PERSON_CD=?, GX_CONTACT_PERSON_CD=?, INVOICE_NO=?, "
								+ "INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), DUE_DT=TO_DATE(?,'DD/MM/YYYY'), "
								+ "ALLOC_QTY=?, TXN_RATE=?, RATE_UNIT=?, SALE_AMT=?, "
								+ "EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, "
								+ "INVOICE_RAISED_IN=?,GROSS_AMT=?, TAX_AMT=?, TAX_STRUCT_CD=?, "
								+ "TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), INVOICE_AMT=?, ADJUST_SIGN=?, ADJUST_AMT=?, "
								+ "NET_PAYABLE_AMT=?,FINANCIAL_YEAR=?,TCS_TDS=?,"
								+ "TDS_AMT=?,TDS_FACTOR=?,TDS_STRUCT_CD=?,"
								+ "TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SYS_INV_NO=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, bu_contact_person);
						stmt1.setString(2, gx_bu_contact_person);
						stmt1.setString(3, party_invoice_no);
						stmt1.setString(4, party_invoice_dt);
						stmt1.setString(5, party_invoice_due_dt);
						stmt1.setString(6, party_alloc_qty);
						stmt1.setString(7, party_price);
						stmt1.setString(8, party_price_cd);
						stmt1.setString(9, party_gross_amt);
						stmt1.setString(10, party_exchng_cd);
						stmt1.setString(11, party_exchng_dt);
						stmt1.setString(12, party_exchng_rate);
						stmt1.setString(13, party_invoice_raised_in);
						stmt1.setString(14, party_gross_amt1);
						stmt1.setString(15, party_tax_amt);
						stmt1.setString(16, party_tax_cd);
						stmt1.setString(17, party_tax_dt);
						stmt1.setString(18, party_invoice_amt);
						stmt1.setString(19, party_adj_plusmin);
						stmt1.setString(20, party_adj_amt);
						stmt1.setString(21, party_net_payable);
						stmt1.setString(22, party_financial_year);
						stmt1.setString(23, applicable_abbr);
						stmt1.setString(24, party_tds_amt);
						stmt1.setString(25, party_tds_factor);
						stmt1.setString(26, party_tds_cd);
						stmt1.setString(27, party_tds_dt);
						stmt1.setString(28, party_system_invoice_no);
						stmt1.setString(29, comp_cd);
						stmt1.setString(30, counterparty_cd);
						stmt1.setString(31, cont_no);
						stmt1.setString(32, agmt_no);
						stmt1.setString(33, gx_bu_unit);
						stmt1.setString(34, bu_unit);
						stmt1.setString(35, contract_type);
						stmt1.setString(36, gx_counterparty_cd);
						stmt1.setString(37, invoice_type);
						stmt1.setString(38, party_invoice_seq);
						stmt1.setString(39, party_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(PG) Remittance for "+msgInfo+" Modified!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						query="INSERT INTO FMS_GX_TXN_PG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
								+ "BU_CONTACT_PERSON_CD,GX_BU_UNIT,GX_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
								+ "DUE_DT,"
								+ "ALLOC_QTY,TXN_RATE,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
								+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
								+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,FINANCIAL_YEAR,GX_COUNTERPARTY_CD,INVOICE_TYPE,"
								+ "TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,SYS_INV_NO) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,SYSDATE,?,?,?,"
								+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, agmt_rev);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, cont_rev);
						stmt1.setString(7, contract_type);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, bu_contact_person);
						stmt1.setString(10, gx_bu_unit);
						stmt1.setString(11, gx_bu_contact_person);
						stmt1.setString(12, party_invoice_seq);
						stmt1.setString(13, party_invoice_no);
						stmt1.setString(14, party_invoice_dt);
						stmt1.setString(15, party_invoice_due_dt);
						stmt1.setString(16, party_alloc_qty);
						stmt1.setString(17, party_price);
						stmt1.setString(18, party_price_cd);
						stmt1.setString(19, party_gross_amt);
						stmt1.setString(20, party_exchng_cd);
						stmt1.setString(21, party_exchng_dt);
						stmt1.setString(22, party_exchng_rate);
						stmt1.setString(23, party_invoice_raised_in);
						stmt1.setString(24, party_gross_amt1);
						stmt1.setString(25, party_tax_amt);
						stmt1.setString(26, party_tax_cd);
						stmt1.setString(27, party_tax_dt);
						stmt1.setString(28, party_invoice_amt);
						stmt1.setString(29, party_adj_plusmin);
						stmt1.setString(30, party_adj_amt);
						stmt1.setString(31, party_net_payable);
						stmt1.setString(32, emp_cd);
						stmt1.setString(33, party_financial_year);
						stmt1.setString(34, gx_counterparty_cd);
						stmt1.setString(35, invoice_type);
						stmt1.setString(36, applicable_abbr);
						stmt1.setString(37, party_tds_amt);
						stmt1.setString(38, party_tds_factor);
						stmt1.setString(39, party_tds_cd);
						stmt1.setString(40, party_tds_dt);
						stmt1.setString(41, party_system_invoice_no);
						
						stmt1.executeUpdate();

						msg = "Successful! - IGX Transaction(PG) Remittance for "+msgInfo+" Submitted!";
						msg_type="S";
						
						stmt1.close();
					}
					
					int tax_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_GX_TXN_PG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query1);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
					stmt2.setString(3, gx_counterparty_cd);
					stmt2.setString(4, invoice_type);
					stmt2.setString(5, party_invoice_seq);
					stmt2.setString(6, party_financial_year);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						tax_count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					if(tax_count>0)
					{
						query2="DELETE FROM FMS_GX_TXN_PG_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
						stmt3=dbcon.prepareStatement(query2);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, contract_type);
						stmt3.setString(3, gx_counterparty_cd);
						stmt3.setString(4, invoice_type);
						stmt3.setString(5, party_invoice_seq);
						stmt3.setString(6, party_financial_year);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					
					if(party_sub_tax_code!=null)
					{
						for(int i=0; i<party_sub_tax_code.length;i++)
						{
							queryString="INSERT INTO FMS_GX_TXN_PG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "GX_COUNTERPARTY_CD,INVOICE_TYPE,"
									+ "TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,?,"
									+ "?,?, "
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE)";
							stmt4=dbcon.prepareStatement(queryString);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, contract_type);
							stmt4.setString(3, party_invoice_seq);
							stmt4.setString(4, party_financial_year);
							stmt4.setString(5, gx_counterparty_cd);
							stmt4.setString(6, invoice_type);
							stmt4.setString(7, party_tax_cd);
							stmt4.setString(8, party_sub_tax_code[i]);
							stmt4.setString(9, party_tax_dt);
							stmt4.setString(10, party_sub_tax_struct[i]);
							stmt4.setString(11, party_sub_tax_amt[i]);
							stmt4.setString(12, party_sub_tax_base_amt[i]);
							stmt4.setString(13, emp_cd);
							stmt4.executeUpdate();
							
							stmt4.close();
						}
					}
				}
			}
			/*else if(opration.equals("CHECK"))
			{
				if(gx_invoice_type.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=sys_chk;

					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_SG_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, sys_chk);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, sys_invoice_seq);
						stmt1.setString(13, sys_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(SG) Remittance for "+msgInfo+" Checked!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - IGX Transaction(SG) Remittance for "+msgInfo+" Checking Failed!";
						msg_type="E";
					}
				}
				else if(gx_invoice_type.equals("P")) //PARTY GENERATED
				{
					party_flag=party_chk;
					
					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_PG_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, party_chk);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, party_invoice_seq);
						stmt1.setString(13, party_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(PG) Remittance for "+msgInfo+" Checked!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - IGX Transaction(PG) Remittance for "+msgInfo+" Checking Failed!";
						msg_type="E";
					}
				}
			}
			else if(opration.equals("AUTHORIZE"))
			{
				if(gx_invoice_type.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=sys_auth;

					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_SG_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, sys_auth);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, sys_invoice_seq);
						stmt1.setString(13, sys_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(SG) Remittance for "+msgInfo+" Authorization Updated!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - IGX Transaction(SG) Remittance for "+msgInfo+" Authorization Failed!";
						msg_type="E";
					}
				}
				else if(gx_invoice_type.equals("P")) //PARTY GENERATED
				{
					party_flag=party_auth;

					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_PG_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, party_auth);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, party_invoice_seq);
						stmt1.setString(13, party_financial_year);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(PG) Remittance for "+msgInfo+" Authorization updated!";
						msg_type="S";
						
						stmt1.close();
					}
					else
					{
						msg = "Failed! - IGX Transaction(PG) Remittance for "+msgInfo+" Authorization Failed!";
						msg_type="E";
					}
				}
			}*/
			else if(opration.equals("CHECK") || opration.equals("AUTHORIZE")) 
			{
			    String flag = "";
			    String table = "";
			    String invoiceSeq = "";
			    String financialYear = "";
			    String InvTypeAbbr = "";

			    if(gx_invoice_type.equals("S")) 
			    { // SYSTEM GENERATED
			    	InvTypeAbbr="SG";
			        flag = opration.equals("CHECK") ? sys_chk : sys_auth;
			        table = "FMS_GX_TXN_SG_INV_MST";
			        invoiceSeq = sys_invoice_seq;
			        financialYear = sys_financial_year;
			        sys_flag=flag;
			    } 
			    else if(gx_invoice_type.equals("P")) 
			    { // PARTY GENERATED
			    	InvTypeAbbr="PG";
			        flag = opration.equals("CHECK") ? party_chk : party_auth;
			        table = "FMS_GX_TXN_PG_INV_MST";
			        invoiceSeq = party_invoice_seq;
			        financialYear = party_financial_year;
			        party_flag=flag;
			    }

			    if(count > 0) 
			    {
			        query = "UPDATE "+ table +" SET "
			        		+ (opration.equals("CHECK") ? "CHECKED_FLAG" : "AUTHORIZED_FLAG") + "=?, " 
			        		+ (opration.equals("CHECK") ? "CHECKED_BY" : "AUTHORIZED_BY") + "=?, " 
			        		+ (opration.equals("CHECK") ? "CHECKED_DT" : "AUTHORIZED_DT") + "=SYSDATE " 
			        		+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? " 
			        		+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? " 
			        		+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? " 
			        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
			        stmt = dbcon.prepareStatement(query);
			        stmt.setString(1, flag);
			        stmt.setString(2, emp_cd);
			        stmt.setString(3, comp_cd);
			        stmt.setString(4, counterparty_cd);
			        stmt.setString(5, cont_no);
			        stmt.setString(6, agmt_no);
			        stmt.setString(7, gx_bu_unit);
			        stmt.setString(8, bu_unit);
			        stmt.setString(9, contract_type);
			        stmt.setString(10, gx_counterparty_cd);
			        stmt.setString(11, invoice_type);
			        stmt.setString(12, invoiceSeq);
			        stmt.setString(13, financialYear);
			        stmt.executeUpdate();

			        msg = "Successful! - IGX Transaction("+InvTypeAbbr+") Remittance for " + msgInfo + " " + (opration.equals("CHECK") ? "Checked" : "Authorization Updated") + "!";
			        msg_type = "S";

			        stmt.close();
			    } 
			    else 
			    {
			        msg = "Failed! - IGX Transaction("+InvTypeAbbr+") Remittance for " + msgInfo + " " + (opration.equals("CHECK") ? "Checking Failed" : "Authorization Failed") + "!";
			        msg_type = "E";
			    }
			}
			else if(opration.equals("APPROVE"))
			{
				if(final_aprv.equals("S")) //SYSTEM GENERATED
				{
					sys_flag=inv_aprv_flag;
					
					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_SG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, inv_aprv_flag);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, sys_invoice_seq);
						stmt1.setString(13, sys_financial_year);
						stmt1.executeUpdate();
						
						stmt1.close();

						query2="UPDATE FMS_GX_TXN_PG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, "");
						stmt2.setString(2, "");
						stmt2.setString(3, "");
						stmt2.setString(4, comp_cd);
						stmt2.setString(5, counterparty_cd);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, agmt_no);
						stmt2.setString(8, gx_bu_unit);
						stmt2.setString(9, bu_unit);
						stmt2.setString(10, contract_type);
						stmt2.setString(11, gx_counterparty_cd);
						stmt2.setString(12, invoice_type);
						stmt2.setString(13, party_invoice_seq);
						stmt2.setString(14, party_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();

						msg = "Successful! - IGX Transaction(SG) Remittance for "+msgInfo+" Approval Updated!";
						msg_type="S";
					}
					else
					{
						msg = "Failed! - IGX Transaction(SG) Remittance for "+msgInfo+" Approval Failed!";
						msg_type="E";
					}
				}
				else if(final_aprv.equals("P")) //PARTY GENERATED
				{
					party_flag=inv_aprv_flag;
					
					if(count > 0)
					{
						query1="UPDATE FMS_GX_TXN_PG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, inv_aprv_flag);
						stmt1.setString(2, emp_cd);
						stmt1.setString(3, comp_cd);
						stmt1.setString(4, counterparty_cd);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, agmt_no);
						stmt1.setString(7, gx_bu_unit);
						stmt1.setString(8, bu_unit);
						stmt1.setString(9, contract_type);
						stmt1.setString(10, gx_counterparty_cd);
						stmt1.setString(11, invoice_type);
						stmt1.setString(12, party_invoice_seq);
						stmt1.setString(13, party_financial_year);
						stmt1.executeUpdate();
						
						stmt1.close();

						query="UPDATE FMS_GX_TXN_SG_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? ";
						stmt2=dbcon.prepareStatement(query);
						stmt2.setString(1, "");
						stmt2.setString(2, "");
						stmt2.setString(3, "");
						stmt2.setString(4, comp_cd);
						stmt2.setString(5, counterparty_cd);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, agmt_no);
						stmt2.setString(8, gx_bu_unit);
						stmt2.setString(9, bu_unit);
						stmt2.setString(10, contract_type);
						stmt2.setString(11, gx_counterparty_cd);
						stmt2.setString(12, invoice_type);
						stmt2.setString(13, sys_invoice_seq);
						stmt2.setString(14, sys_financial_year);
						stmt2.executeUpdate();
						
						stmt2.close();
						msg = "Successful! - IGX Transaction(PG) Remittance for "+msgInfo+" Approval Updated!";
						msg_type="S";
					}
					else
					{
						msg = "Failed! - IGX Transaction(PG) Remittance for "+msgInfo+" Approval Failed!";
						msg_type="E";
					}
				}
			}
			
			String sell_buy="Sell";
			if(contract_type.equals("I"))
			{
				sell_buy="Buy";
			}
			else
			{
				sell_buy="Sell";
			}
			if(opration.equals("APPROVE"))
			{
				if(final_aprv.equals("S"))
				{
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, sys_invoice_no,sell_buy, sys_invoice_due_dt, sys_invoice_amt, opration, final_aprv,sys_flag,sys_price_cd,sys_invoice_dt,gx_counterparty_cd);
				}
				else if(final_aprv.equals("P"))
				{
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, party_invoice_no,sell_buy, party_invoice_due_dt, party_invoice_amt, opration, final_aprv,party_flag,sys_price_cd,party_invoice_dt,gx_counterparty_cd);
				}
			}
			else
			{
				if(gx_invoice_type.equals("S"))
				{
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, sys_invoice_no, sell_buy, sys_invoice_due_dt, sys_invoice_amt, opration, gx_invoice_type,sys_flag,sys_price_cd,sys_invoice_dt,gx_counterparty_cd);
				}
				else if(gx_invoice_type.equals("P"))
				{
					InvoiceMailBody(comp_cd,counterparty_nm, deal_no, party_invoice_no, sell_buy, party_invoice_due_dt, party_invoice_amt, opration, gx_invoice_type,party_flag,sys_price_cd,party_invoice_dt,gx_counterparty_cd);
				}
			}
			
			url = "../gx/frm_prepare_gx_transaction_invoice.jsp?counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
					"&contract_type="+contract_type+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev+"&invoice_type="+invoice_type+
					"&gx_bu_unit="+gx_bu_unit+"&bu_unit="+bu_unit+"&activity_type="+activity_type+"&type="+type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - IGX Transaction Remittance Insert/Update Failed!";			
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
	
	private int InvoiceDetailCount(String invoice_type,String comp_cd,String counterparty_cd,String cont_no,String agmt_no,String bu_unit,String contract_type,
			String gx_counterparty_cd,String gx_bu_unit,String invoice_seq,String financial_year,String gx_invoice_type) throws SQLException
	{
		String function_nm="InvoiceDetailCount()";
		int count=0;
		try
		{
			query="SELECT COUNT(*) ";		
			if(gx_invoice_type.equals("P"))
			{
				query+="FROM FMS_GX_TXN_PG_INV_MST ";
			}
			else
			{
				query+="FROM FMS_GX_TXN_SG_INV_MST ";
			}
			query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND GX_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
			stmt_tmp=dbcon.prepareStatement(query);
			stmt_tmp.setString(1, comp_cd);
			stmt_tmp.setString(2, counterparty_cd);
			stmt_tmp.setString(3, cont_no);
			stmt_tmp.setString(4, agmt_no);
			stmt_tmp.setString(5, gx_bu_unit);
			stmt_tmp.setString(6, bu_unit);
			stmt_tmp.setString(7, contract_type);
			stmt_tmp.setString(8, gx_counterparty_cd);
			stmt_tmp.setString(9, invoice_type);
			stmt_tmp.setString(10, financial_year);
			stmt_tmp.setString(11, invoice_seq);
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

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String gx_counterparty_cd = request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);

			String mapping_id = request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String gx_bu_unit = request.getParameter("gx_bu_unit")==null?"":request.getParameter("gx_bu_unit");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");

			String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"":request.getParameter("bu_contact_person");
			
			String activity_type = request.getParameter("activity_type")==null?"":request.getParameter("activity_type");
			
			//String deal_no=contract_type+""+cont_no;
			String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, "");

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
					+ "FROM FMS_GX_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
					+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, invoice_seq);
			stmt.setString(3, invoice_no);
			stmt.setString(4, invoice_type);
			stmt.setString(5, financial_year);
			stmt.setString(6, contract_type);
			stmt.setString(7, gx_counterparty_cd);
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
					query1="UPDATE FMS_GX_FFLOW_INV_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, chk);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, gx_counterparty_cd);
					stmt1.executeUpdate();
					msg = "Successful! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Checked!";
					msg_type="S";
					
					stmt1.close();
				}
				else
				{
					msg = "Failed! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Checking Failed!";
					msg_type="E";
				}
			}
			else if(opration_type.equals("AUTHORIZE"))
			{
				sys_flag=auth;
				if(count > 0)
				{
					query1="UPDATE FMS_GX_FFLOW_INV_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, auth);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, gx_counterparty_cd);
					stmt1.executeUpdate();
					msg = "Successful! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Authorization Updated";
					msg_type="S";
					
					stmt1.close();
				}
				else
				{
					msg = "Failed! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Authorization Failed!";
					msg_type="E";
				}
			}
			else if(opration_type.equals("APPROVE"))
			{
				sys_flag=aprv;
				if(count > 0)
				{
					query1="UPDATE FMS_GX_FFLOW_INV_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, aprv);
					stmt1.setString(2, emp_cd);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, invoice_no);
					stmt1.setString(6, invoice_type);
					stmt1.setString(7, financial_year);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, gx_counterparty_cd);
					stmt1.executeUpdate();
					msg = "Successful! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Approval Updated!";
					msg_type="S";
					
					stmt1.close();
				}
				else
				{
					msg = "Failed! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Approval Failed!";
					msg_type="E";
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
									+ "FROM FMS_GX_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? AND GX_COUNTERPARTY_CD=? "
									+ "AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, gx_counterparty_cd);
							stmt.setString(3, new_financial_year);
							stmt.setString(4, invoice_type);
							stmt.setString(5, contract_type);
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
								
								invoice_no=invoice_prefix+"E"+contract_type+invoice_type+utilBean.PrePaddingZero(new_invoice_seq, 4)+"/"+fin_yr; //E IS DEFAULT
							}
						}
						
						query1="UPDATE FMS_GX_FFLOW_INV_MST SET COUNTERPARTY_CD=?,AGMT_NO=?,AGMT_REV=?,"
								+ "CONT_NO=?,CONT_REV=?,CONTRACT_TYPE=?,BU_UNIT=?,BU_CONTACT_PERSON_CD=?,"
								+ "GX_BU_UNIT=?,GX_CONTACT_PERSON_CD=?,INVOICE_REF=?,INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "INVOICE_CATEGORY=?,NUM_LINE=?,LINKED_INVOICE=?,"
								+ "NOTE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,INVOICE_TYPE=?,FINANCIAL_YEAR=?,"
								+ "OTHER_INV_STR=?,GROSS_AMT_USD=?,EXCHG_RATE_VALUE=?,GROSS_AMT_INR=?,"
								+ "TAX_AMT=?,INVOICE_AMT=?,ADJUST_AMT=?,NET_PAYABLE_AMT=?,INVOICE_RAISED_IN=?,"
								+ "AMT_WORD=?,TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TDS_AMT=?,TDS_STRUCT_CD=?,TDS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TCS_AMT=?,TCS_STRUCT_CD=?,TCS_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "TCS_TDS=?,ALLOC_QTY=?,INVOICE_SEQ=?,INVOICE_NO=? "
								+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? "
								+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
								+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, counterparty_cd);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, cont_rev);
						stmt1.setString(6, contract_type);
						stmt1.setString(7, bu_unit);
						stmt1.setString(8, bu_contact_person);
						stmt1.setString(9, gx_bu_unit);
						stmt1.setString(10, contact_person);
						stmt1.setString(11, invoice_ref);
						stmt1.setString(12, invoice_dt);
						stmt1.setString(13, invoice_category);
						stmt1.setString(14, no_of_line);
						stmt1.setString(15, linked_invoice);
						stmt1.setString(16, note);
						stmt1.setString(17, emp_cd);
						stmt1.setString(18, invoice_type);
						//stmt1.setString(19, financial_year);
						stmt1.setString(19, new_financial_year);
						stmt1.setString(20, subject_line);
						stmt1.setString(21, gross_amt_usd);
						stmt1.setString(22, exchange_rate);
						stmt1.setString(23, gross_amt_inr);
						stmt1.setString(24, tax_amt);
						stmt1.setString(25, invoice_amt);
						stmt1.setString(26, adjust_amt);
						stmt1.setString(27, net_amt);
						stmt1.setString(28, invoice_raised_in);
						stmt1.setString(29, amt_in_word);
						stmt1.setString(30, tax_cd);
						stmt1.setString(31, tax_dt);
						stmt1.setString(32, tds_amt);
						stmt1.setString(33, tds_cd);
						stmt1.setString(34, tds_dt);
						stmt1.setString(35, tcs_amt);
						stmt1.setString(36, tcs_cd);
						stmt1.setString(37, tcs_dt);
						stmt1.setString(38, tcs_tds);
						stmt1.setString(39, alloc_qty);
						stmt1.setString(40, new_invoice_seq);
						stmt1.setString(41, invoice_no);
						stmt1.setString(42, comp_cd);
						stmt1.setString(43, invoice_seq);
						stmt1.setString(44, invoice_type);
						stmt1.setString(45, financial_year);
						stmt1.setString(46, contract_type);
						stmt1.setString(47, gx_counterparty_cd);
						stmt1.executeUpdate();
						msg = "Successful! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Modified!";
						msg_type="S";
						
						stmt1.close();
					}

					query2="DELETE FROM FMS_GX_FFLOW_INV_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? "
							+ "AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
							+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, invoice_seq);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, contract_type);
					stmt2.setString(6, gx_counterparty_cd);
					stmt2.executeUpdate();
					stmt2.close();

					if(item_seq != null)
					{
						for(int i=0; i<item_seq.length; i++)
						{
							query1="INSERT INTO FMS_GX_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,GX_COUNTERPARTY_CD,CONTRACT_TYPE,"
									+ "INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
									+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, gx_counterparty_cd);
							stmt1.setString(4, contract_type);
							//stmt1.setString(5, invoice_seq);
							stmt1.setString(5, new_invoice_seq);
							stmt1.setString(6, invoice_no);
							stmt1.setString(7, item_seq[i]);
							stmt1.setString(8, item_note[i]);
							stmt1.setString(9, unit[i]);
							stmt1.setString(10, qty[i]);
							stmt1.setString(11, rate[i]);
							stmt1.setString(12, amount[i]);
							stmt1.setString(13, emp_cd);
							//stmt1.setString(14, financial_year);
							stmt1.setString(14, new_financial_year);
							stmt1.setString(15, invoice_type);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
					}
				}
				else
				{
					financial_year=dateUtil.getFinancialYear(invoice_dt);
					
					queryString="SELECT NVL(MAX(INVOICE_SEQ),0) "
							+ "FROM FMS_GX_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND GX_COUNTERPARTY_CD=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, gx_counterparty_cd);
					stmt.setString(3, financial_year);
					stmt.setString(4, invoice_type);
					stmt.setString(5, contract_type);
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
						
						invoice_no=invoice_prefix+"E"+contract_type+invoice_type+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr; //E IS DEFAULT
					}
					
					new_invoice_seq=invoice_seq;
					new_financial_year=financial_year;
					
					query1="INSERT INTO FMS_GX_FFLOW_INV_MST(COMPANY_CD,COUNTERPARTY_CD,GX_COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "BU_UNIT,BU_CONTACT_PERSON_CD,GX_BU_UNIT,GX_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_REF,INVOICE_DT,"
							+ "INVOICE_CATEGORY,NUM_LINE,LINKED_INVOICE,"
							+ "NOTE,ENT_BY,ENT_DT,DUE_DT,INVOICE_TYPE,FINANCIAL_YEAR,OTHER_INV_STR,"
							+ "GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,INVOICE_RAISED_IN,AMT_WORD,"
							+ "TAX_STRUCT_CD,TAX_EFF_DT,TDS_AMT,TDS_STRUCT_CD,TDS_EFF_DT,TCS_AMT,TCS_STRUCT_CD,TCS_EFF_DT,TCS_TDS,"
							+ "ALLOC_QTY) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,"
							+ "?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, gx_counterparty_cd);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev);
					stmt1.setString(6, cont_no);
					stmt1.setString(7, cont_rev);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, bu_unit);
					stmt1.setString(10, bu_contact_person);
					stmt1.setString(11, gx_bu_unit);
					stmt1.setString(12, contact_person);
					stmt1.setString(13, invoice_seq);
					stmt1.setString(14, invoice_no);
					stmt1.setString(15, invoice_ref);
					stmt1.setString(16, invoice_dt);
					stmt1.setString(17, invoice_category);
					stmt1.setString(18, no_of_line);
					stmt1.setString(19, linked_invoice);
					stmt1.setString(20, note);
					stmt1.setString(21, emp_cd);
					stmt1.setString(22, invoice_due_dt);
					stmt1.setString(23, invoice_type);
					stmt1.setString(24, financial_year);
					stmt1.setString(25, subject_line);
					stmt1.setString(26, gross_amt_usd);
					stmt1.setString(27, exchange_rate);
					stmt1.setString(28, gross_amt_inr);
					stmt1.setString(29, tax_amt);
					stmt1.setString(30, invoice_amt);
					stmt1.setString(31, adjust_amt);
					stmt1.setString(32, net_amt);
					stmt1.setString(33, invoice_raised_in);
					stmt1.setString(34, amt_in_word);
					stmt1.setString(35, tax_cd);
					stmt1.setString(36, tax_dt);
					stmt1.setString(37, tds_amt);
					stmt1.setString(38, tds_cd);
					stmt1.setString(39, tds_dt);
					stmt1.setString(40, tcs_amt);
					stmt1.setString(41, tcs_cd);
					stmt1.setString(42, tcs_dt);
					stmt1.setString(43, tcs_tds);
					stmt1.setString(44, alloc_qty);
					
					stmt1.executeUpdate();
					msg = "Successful! - IGX Transaction(FFLOW) Remittance for "+msgInfo+" Inserted!";
					msg_type="S";
					
					stmt1.close();

					if(item_seq != null)
					{
						for(int i=0; i<item_seq.length; i++)
						{
							query2="INSERT INTO FMS_GX_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,GX_COUNTERPARTY_CD,CONTRACT_TYPE,"
									+ "INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,"
									+ "UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE) "
									+ "VALUES(?,?,?,?,"
									+ "?,?,?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?)";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, gx_counterparty_cd);
							stmt2.setString(4, contract_type);
							stmt2.setString(5, invoice_seq);
							stmt2.setString(6, invoice_no);
							stmt2.setString(7, item_seq[i]);
							stmt2.setString(8, item_note[i]);
							stmt2.setString(9, unit[i]);
							stmt2.setString(10, qty[i]);
							stmt2.setString(11, rate[i]);
							stmt2.setString(12, amount[i]);
							stmt2.setString(13, emp_cd);
							stmt2.setString(14, financial_year);
							stmt2.setString(15, invoice_type);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
				
				int tax_count=0;
				query1="SELECT COUNT(*) "
						+ "FROM FMS_GX_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, gx_counterparty_cd);
				stmt1.setString(4, invoice_type);
				stmt1.setString(5, invoice_seq);
				stmt1.setString(6, financial_year);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(tax_count>0)
				{
					query2="DELETE FROM FMS_GX_FFLOW_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, contract_type);
					stmt2.setString(3, gx_counterparty_cd);
					stmt2.setString(4, invoice_type);
					stmt2.setString(5, invoice_seq);
					stmt2.setString(6, financial_year);
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
							rset3 = stmt3.executeQuery();
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
						query3="INSERT INTO FMS_GX_FFLOW_INV_TAX_DTL(COMPANY_CD,GX_COUNTERPARTY_CD,CONTRACT_TYPE,"
								+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
								+ "TAX_EFF_DT,TAX_DESCR,"
								+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,"
								+ "?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,SYSDATE)";
						stmt3=dbcon.prepareStatement(query3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, gx_counterparty_cd);
						stmt3.setString(3, contract_type);
						//stmt3.setString(4, invoice_seq);
						stmt3.setString(4, new_invoice_seq);
						stmt3.setString(5, invoice_type);
						//stmt3.setString(6, financial_year);
						stmt3.setString(6, new_financial_year);
						stmt3.setString(7, tax_cd);
						stmt3.setString(8, ""+VTEMP_TAX_CODE.elementAt(i));
						stmt3.setString(9, tax_dt);
						stmt3.setString(10, ""+VTEMP_TAX_DESCR.elementAt(i));
						stmt3.setString(11, nf.format(taxAmt));
						stmt3.setString(12, "");
						stmt3.setString(13, emp_cd);
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
							query3="INSERT INTO FMS_GX_FFLOW_INV_TAX_DTL(COMPANY_CD,GX_COUNTERPARTY_CD,CONTRACT_TYPE,"
									+ "INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
									+ "TAX_EFF_DT,TAX_DESCR,"
									+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT) "
									+ "VALUES(?,?,?,"
									+ "?,?,?,?,?,"
									+ "TO_DATE(?,'DD/MM/YYYY'),?,"
									+ "?,?,?,SYSDATE)";
							stmt3=dbcon.prepareStatement(query3);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, gx_counterparty_cd);
							stmt3.setString(3, contract_type);
							//stmt3.setString(4, invoice_seq);
							stmt3.setString(4, new_invoice_seq);
							stmt3.setString(5, invoice_type);
							//stmt3.setString(6, financial_year);
							stmt3.setString(6, new_financial_year);
							stmt3.setString(7, tax_cd);
							stmt3.setString(8, sub_tax_code[i]);
							stmt3.setString(9, tax_dt);
							stmt3.setString(10, sub_tax_struct[i]);
							stmt3.setString(11, sub_tax_amt[i]);
							stmt3.setString(12, "");
							stmt3.setString(13, emp_cd);
							stmt3.executeUpdate();
							
							stmt3.close();
						}
					}
				}
			}
			
			FFlowInvoiceMailBody(comp_cd,counterparty_nm, deal_no, invoice_ref,invoice_due_dt, invoice_amt, opration_type, invoice_type,sys_flag,invoice_raised_in,invoice_dt, gx_counterparty_cd);

			if(opration.equals("INSERT"))
			{
				opration="MODIFY";
			}
			url = "../gx/frm_gx_f_flow_invoice.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&gx_counterparty_cd="+gx_counterparty_cd+
					"&month="+month+"&year="+year+"&invoice_type="+invoice_type+"&opration="+opration+
					"&mapping_id="+mapping_id+"&gx_bu_unit="+gx_bu_unit+"&bu_unit="+bu_unit+"&inv_no="+invoice_no+"&inv_seq="+invoice_seq+
					"&financial="+financial_year+"&activity_type="+activity_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - IGX Transaction(FFLOW) Remittance Insert/Update Failed!";

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
			String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			
			String gx_counterparty_cd = request.getParameter("file_gx_counterparty_cd")==null?"":request.getParameter("file_gx_counterparty_cd");
			String invoice_seq = request.getParameter("file_invoice_seq")==null?"":request.getParameter("file_invoice_seq");
			String financial_year = request.getParameter("file_financial_year")==null?"":request.getParameter("file_financial_year");
			String invoice_title = request.getParameter("invoice_title")==null?"":request.getParameter("invoice_title");
			String invoice_type = request.getParameter("file_invoice_type")==null?"":request.getParameter("file_invoice_type");
			String contract_type = request.getParameter("file_contract_type")==null?"":request.getParameter("file_contract_type");
			
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

	        String sub_folder="gx";
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
		        		+ "FROM FMS_GX_FFLOW_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? "
		        		+ "AND CONTRACT_TYPE=?";
		        stmt=dbcon.prepareStatement(query);
		        stmt.setString(1, comp_cd);
		        stmt.setString(2, gx_counterparty_cd);
		        stmt.setString(3, invoice_type);
		        stmt.setString(4, invoice_seq);
		        stmt.setString(5, financial_year);
		        stmt.setString(6, invoice_title);
		        stmt.setString(7, contract_type);
		        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	count=rset.getInt(1);
		        }
		        rset.close();
		        stmt.close();
		        
		        if(count > 0)
		        {
		        	 query1="UPDATE FMS_GX_FFLOW_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		        			+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
				        	+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
				        	+ "AND INV_TITLE=? AND GX_COUNTERPARTY_CD=?";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, file_name);
		        	 stmt1.setString(2, emp_cd);
		        	 stmt1.setString(3, comp_cd);
		        	 stmt1.setString(4, contract_type);
		        	 stmt1.setString(5, invoice_type);
		        	 stmt1.setString(6, invoice_seq);
		        	 stmt1.setString(7, financial_year);
		        	 stmt1.setString(8, invoice_title);
		        	 stmt1.setString(9, gx_counterparty_cd);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
		        else
		        {
		        	query1="INSERT INTO FMS_GX_FFLOW_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
		        			+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE,GX_COUNTERPARTY_CD) "
		        			+ "VALUES(?,?,?,?,?,"
		        			+ "?,?,SYSDATE,?,?)";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, comp_cd);
		        	 stmt1.setString(2, contract_type);
		        	 stmt1.setString(3, invoice_seq);
		        	 stmt1.setString(4, financial_year);
		        	 stmt1.setString(5, invoice_title);
		        	 stmt1.setString(6, file_name);
		        	 stmt1.setString(7, emp_cd);
		        	 stmt1.setString(8, invoice_type);
		        	 stmt1.setString(9, gx_counterparty_cd);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
	        }
	        else
	        {
		        int count=0;
		        query="SELECT COUNT(*) "
		        		+ "FROM FMS_GX_TXN_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? "
		        		+ "AND CONTRACT_TYPE=?";
		        stmt=dbcon.prepareStatement(query);
		        stmt.setString(1, comp_cd);
		        stmt.setString(2, gx_counterparty_cd);
		        stmt.setString(3, invoice_type);
		        stmt.setString(4, invoice_seq);
		        stmt.setString(5, financial_year);
		        stmt.setString(6, invoice_title);
		        stmt.setString(7, contract_type);
		        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	count=rset.getInt(1);
		        }
		        rset.close();
		        stmt.close();
		        
		        if(count > 0)
		        {
		        	 query1="UPDATE FMS_GX_TXN_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		        			+ "WHERE COMPANY_CD=? AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
				        	+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? "
				        	+ "AND CONTRACT_TYPE=?";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, file_name);
		        	 stmt1.setString(2, emp_cd);
		        	 stmt1.setString(3, comp_cd);
		        	 stmt1.setString(4, gx_counterparty_cd);
		        	 stmt1.setString(5, invoice_type);
		        	 stmt1.setString(6, invoice_seq);
		        	 stmt1.setString(7, financial_year);
		        	 stmt1.setString(8, invoice_title);
		        	 stmt1.setString(9, contract_type);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
		        else
		        {
		        	query1="INSERT INTO FMS_GX_TXN_INV_FILE_DTL(COMPANY_CD,GX_COUNTERPARTY_CD,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
		        			+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE,CONTRACT_TYPE) "
		        			+ "VALUES(?,?,?,?,?,"
		        			+ "?,?,SYSDATE,?,?)";
		        	 stmt1=dbcon.prepareStatement(query1);
		        	 stmt1.setString(1, comp_cd);
		        	 stmt1.setString(2, gx_counterparty_cd);
		        	 stmt1.setString(3, invoice_seq);
		        	 stmt1.setString(4, financial_year);
		        	 stmt1.setString(5, invoice_title);
		        	 stmt1.setString(6, file_name);
		        	 stmt1.setString(7, emp_cd);
		        	 stmt1.setString(8, invoice_type);
		        	 stmt1.setString(9, contract_type);
		        	 stmt1.executeUpdate();
		        	 
		        	 stmt1.close();
		        }
	        }
	        
	        msg = "Successful! - IGX Transaction("+upload_inv_type+") Remittance Received File Uploaded!";
			msg_type="S";
			
			url = "../gx/frm_gx_transaction_invoice_preparation.jsp?msg="+msg+"&msg_type="+msg_type+
					"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - IGX Transaction Remittance Received File Upload Failed!";
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
	
	private String InvoiceMailBody(String company_cd,String trader,String cont_no,String inv_no,String sell_buy,String due_dt,String inv_amount,
			String activity_type,String inv_title,String flag,String price_cd,String invoice_dt,String gx_counterparty_cd) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
			String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
			String gx_counterparty_abbr=utilBean.getGasExchangeAbbr(dbcon, gx_counterparty_cd);
			String mail_subject=company_abbr+"/"+gx_counterparty_abbr+"/Transaction Fee("+inv_title+"G) Remittance/"+inv_no+"";
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Approved</font>, Please start Fin Ops Finalization Process!</span><br><br>";
					
					mail_subject+=" - IRP Approved!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Approve!</span><br><br>";
					mail_subject+=" - IRP Approve Rejected!";
				}
			}
			else if(activity_type.equals("APPROVE"))
			{
				if(flag.equals("A"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Fin Ops Finalized</font>, You may proceed for PDF generation!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalized!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while Fin Ops Finalization!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalization Rejected!";
				}
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Checked</font>, Please start IRP Approve Process!</span><br><br>";
					
					mail_subject+=" IRP Checked OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Transaction Fee Remittance("+inv_title+"G) is "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;On behalf of Counterparty</b>&nbsp;</td><td>&nbsp;"+trader+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Counterparty</b>&nbsp;</td><td>&nbsp;"+gx_counterparty_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+cont_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Sell/Buy</b>&nbsp;</td><td>&nbsp;"+sell_buy+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,price_cd)+"</td></tr>"
					+ "</table>"
					+ "<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";
			
			String to_mail_list="";
			String cc_mail_list="";
			
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
				}
				
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
			}
			else if(activity_type.equals("APPROVE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
				}
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
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
			
			
			email_body=email_body.replaceAll("\n", "<br>");
			email_body="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body+"</span>"
					+ "</html>";
			
			if(!email_to.equals("") && !email_body.equals("") && attachment!=null)
			{
				mailDelv.sendMailWithMultipleAttachment(comp_cd,email_to, subject, email_body, attachment, email_cc, email_bcc);
			}
			
			msg="Mail Sent for "+subject;
			msg_type="S";
				
			
			url = "../gx/frm_gx_invoice_remittance_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - IGX Transaction Remittance Email Send Failed!";
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
	
	private void InsertUpdateSapInvoiceApprove(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSapInvoiceApprove()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String gx_counterparty_cd = request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String type_flag = request.getParameter("type_flag")==null?"":request.getParameter("type_flag");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String sap_approval_flag = request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
			String xmlfile_nm ="";
			
			if(!financial_year.equals("") && !invoice_seq.equals("") && !contract_type.equals(""))
			{
				if(type_flag.equals("S"))
				{
					query ="UPDATE FMS_GX_TXN_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
					sap_approval_flag="Y";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, financial_year);
					stmt.setString(5, invoice_seq);
					stmt.setString(6, contract_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					query1 ="UPDATE FMS_GX_TXN_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "");
					stmt1.setString(2, "");
					stmt1.setString(3, "");
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, financial_year);
					stmt1.setString(6, invoice_seq);
					stmt1.setString(7, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else if(type_flag.equals("P"))
				{
					query1 ="UPDATE FMS_GX_TXN_SG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=? "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, "");
					stmt1.setString(2, "");
					stmt1.setString(3, "");
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, financial_year);
					stmt1.setString(6, invoice_seq);
					stmt1.setString(7, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					query ="UPDATE FMS_GX_TXN_PG_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
					sap_approval_flag="Y";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, financial_year);
					stmt.setString(5, invoice_seq);
					stmt.setString(6, contract_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else if(type_flag.equals("FF"))
				{
					query ="UPDATE FMS_GX_FFLOW_INV_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND  GX_COUNTERPARTY_CD=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
					sap_approval_flag="Y";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, financial_year);
					stmt.setString(5, gx_counterparty_cd);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				msg = "Successful! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") SAP Approved Successfully!";
				msg_type="S";
				
				String workDir=CommonVariable.work_dir+comp_cd;
				String sapxml_dir="";
				sapxml_dir=CommonVariable.sap_xml;
				String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");
				
				DBgx.setCallFlag("GENERATE_GX_SAP_XML");
				DBgx.setRequest(request);
				DBgx.setContract_type(contract_type);
				DBgx.setFinancial_year(financial_year);
				DBgx.setInvoice_seq(invoice_seq);
				DBgx.setCounterparty_cd(counterparty_cd);
				DBgx.setType_flag(type_flag);
				DBgx.setInvoice_no(invoice_no);
				DBgx.setInvoice_type(invoice_type);
				DBgx.setComp_cd(comp_cd);
				DBgx.setFile_path(appPath);
				DBgx.setSap_approval_flag(sap_approval_flag);
				DBgx.setGx_counterparty_cd(gx_counterparty_cd);
				DBgx.setEmp_cd(emp_cd);
				DBgx.init();
				
				xmlfile_nm = DBgx.getXmlfile_name();
	
				InvoiceMailBody(comp_cd, invoice_no, counterparty_cd, gx_counterparty_cd); //JD
				//generatePurchaseInvoiceXML(request);
			}
			else
			{	
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") SAP Approval Failed!";
				msg_type="E";
			}
			
			url = "../gx/rpt_view_gx_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
					"&contract_type="+contract_type+"&type_flag="+type_flag+"&invoice_type="+invoice_type+"&gx_counterparty_cd="+gx_counterparty_cd+
					"&counterparty_cd="+counterparty_cd+"&invoice_no="+invoice_no+"&sap_approval_flag="+sap_approval_flag+
					"&xmlfile_nm="+xmlfile_nm+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - IGX Transaction Remittance SAP Approval Failed!";
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
	
	private void InvoiceMailBody(String company_cd, String inv_no,String conterparty_cd, String gx_counterparty_cd) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
            String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
            String gx_counterparty_abbr=utilBean.getGasExchangeAbbr(dbcon, gx_counterparty_cd);
			String mail_subject=company_abbr+"/"+gx_counterparty_abbr+"/Remittance for Invoice "+inv_no+" - Approved by Fin. Ops.!";
			
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Gx Remittance is "
					+ "<font style='background:"+highlight_aprv+"' color='white'>Approved</font> by Fin. Ops. for SAP P80 posting!</span><br><br>";
					
			mailBody+="<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>On behalf of Counterparty</b>&nbsp;</td><td>&nbsp;"+utilBean.getCounterpartyName(dbcon,conterparty_cd)+"&nbsp;</td></tr>"
					+ "<tr><td><b>Counterparty</b>&nbsp;</td><td>&nbsp;"+gx_counterparty_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "</table>"
					+ "<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";
			
			String to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Fin. Ops. Approval Notification", "GX", "NA", "On-Event");
			String cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Fin. Ops. Approval Notification", "GX", "NA", "On-Event");
			
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
	}
	
	private String FFlowInvoiceMailBody(String company_cd,String trader,String cont_no,String inv_no,String due_dt,
			String inv_amount,String activity_type,String inv_type,String flag,String invoice_raised_in,String invoice_dt,String gx_counterparty_cd) throws Exception
	{
		String function_nm="FFlowInvoiceMailBody()";
		String mailBody="";
		try
		{
			String inv_nm="";
            if(inv_type.equals("CR"))
			{
            	inv_nm="Credit Note";
			}
			else if(inv_type.equals("DR"))
			{
				inv_nm="Debit Note";
			}
			else if(inv_type.equals("LP"))
			{
				inv_nm="Late Payment";
			}
			else if(inv_type.equals("OR"))
			{
				inv_nm="Other";
			}
			else
			{
				inv_nm="";
			}
            
            String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
            String gx_counterparty_abbr=utilBean.getGasExchangeAbbr(dbcon, gx_counterparty_cd);
			String mail_subject=company_abbr+"/"+gx_counterparty_abbr+"/"+inv_nm+" Remittance/"+inv_no+"";
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Approved</font>, Please start Fin Ops Finalization Process!</span><br><br>";
					
					mail_subject+=" - IRP Approved!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Approve!</span><br><br>";
					mail_subject+=" - IRP Approve Rejected!";
				}
			}
			else if(activity_type.equals("APPROVE"))
			{
				if(flag.equals("A"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Fin Ops Finalized</font>, You may proceed for PDF generation!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalized!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while Fin Ops Finalization!</span><br><br>";
					
					mail_subject+=" - Fin Ops Finalization Rejected!";
				}
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>Checked</font>, Please start IRP Approve Process!</span><br><br>";
					
					mail_subject+=" IRP Checked OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" Remittance is "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;On behalf of Counterparty</b>&nbsp;</td><td>&nbsp;"+trader+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Counterparty</b>&nbsp;</td><td>&nbsp;"+gx_counterparty_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+cont_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Date</b>&nbsp;</td><td>&nbsp;"+invoice_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Due Date</b>&nbsp;</td><td>&nbsp;"+due_dt+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Invoice Amount</b>&nbsp;</td><td>&nbsp;"+inv_amount+"&nbsp;"+utilBean.getRateUnitNm(dbcon,invoice_raised_in)+"</td></tr>"
					+ "</table>"
					+ "<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";
			
			String to_mail_list="";
			String cc_mail_list="";
			
			if(activity_type.equals("AUTHORIZE"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
				}
				
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
			}
			else if(activity_type.equals("APPROVE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Approval Notification", "GX", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Auth Notification", "GX", "NA", "On-Event");
				}
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Remittance Check Notification", "GX", "NA", "On-Event");
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
}
