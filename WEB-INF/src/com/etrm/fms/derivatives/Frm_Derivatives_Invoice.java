package com.etrm.fms.derivatives;

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

import com.etrm.fms.accounting.DataBean_Accounting;
import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_Derivatives_Invoice")
@MultipartConfig(fileSizeThreshold=1024*1024*20, 	// 20 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100) 		// 100 MB

public class Frm_Derivatives_Invoice extends HttpServlet
{
	static String frm_src_file_name="Frm_Derivatives_Invoice.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Derivatives_Invoice";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	public static String approved_flag ="";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt0 = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt5 = null;
	private static PreparedStatement stmt6 = null;
	private static PreparedStatement stmt_temp = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset0 = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset5 = null;
	private static ResultSet rset6 = null;
	private static ResultSet rset_temp = null;
	
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
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	static NumberFormat nf3 = new DecimalFormat("###########0.000000");
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	static DB_Derivatives_Invoice DBDerv = new DB_Derivatives_Invoice();
	
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
						
						if(option.equalsIgnoreCase("GEN_DERV_INV"))
						{
							InsertGenDervInv(request);
						}
						if(option.equalsIgnoreCase("DERV_INVOICE_APPROVAL"))
						{
							InsertUpdateDerivativesInvoiceApproval(request);
						}
						if(option.equalsIgnoreCase("DERV_INVOICE_SAP_APPROVE"))
						{
							InsertUpdateSapInvoiceApprove(request);
						}
						else if(option.equalsIgnoreCase("SEND_DERV_INVOICE_MAIL"))
						{
							SendDervInvoiceMail(request);
						}
						else if(option.equalsIgnoreCase("INVOICE_PDF_UPLOAD"))
						{
							InvoiceFileUpload(request);
						}
						if(option.equalsIgnoreCase("GEN_DERV_CR_DR"))
						{
							InsertGenDervCRDR(request);
						}
						if(option.equalsIgnoreCase("DERV_CRDR_INVOICE_APPROVAL"))
						{
							InsertUpdateDerivativesCrDrInvoiceApproval(request);
						}
						if(option.equalsIgnoreCase("DERV_CRDR_SAP_APPROVE"))
						{
							InsertUpdateSapCrdrApprove(request);
						}
						else if(option.equalsIgnoreCase("SEND_CRDR_DERV_INVOICE_MAIL"))
						{
							SendDervCrdrMail(request);
						}
						else if(option.equalsIgnoreCase("CRDR_PDF_UPLOAD"))
						{
							CrdrFileUpload(request);
						}
						else if(option.equalsIgnoreCase("DERV_INVOICE_CHANGE_ACTION"))
						{
							InsertUpdateDervInvoiceChangeAction(request);
						}
					}
					
					dbcon.close();
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
				}
				finally
				{
					if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
					if(rset0 != null){try {rset0.close();}catch(SQLException e){System.out.println("rset0 is not close " + e);}}
					if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
					if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
					if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
					if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
					if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
					if(rset6 != null){try {rset6.close();}catch(SQLException e){System.out.println("rset6 is not close " + e);}}
					if(rset_temp != null){try {rset_temp.close();}catch(SQLException e){System.out.println("rset_temp is not close " + e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
					if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
					if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
					if(stmt6 != null){try {stmt6.close();}catch(SQLException e){System.out.println("stmt6 is not close " + e);}}
					if(stmt_temp != null){try {stmt_temp.close();}catch(SQLException e){System.out.println("stmt_temp is not close " + e);}}
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
			
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String bu_state_tin = request.getParameter("file_bu_state_tin")==null?"":request.getParameter("file_bu_state_tin");
			String invoice_seq = request.getParameter("file_invoice_seq")==null?"":request.getParameter("file_invoice_seq");
			String financial_year = request.getParameter("file_financial_year")==null?"":request.getParameter("file_financial_year");
			String invoice_title = request.getParameter("invoice_title")==null?"":request.getParameter("invoice_title");
			String invoice_type = request.getParameter("file_invoice_type")==null?"":request.getParameter("file_invoice_type");
			
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

	        String sub_folder="derivatives";
	        String filePath1=filePath+File.separator+sub_folder;
	        File sub_folderDir = new File(filePath1);
	        if(!sub_folderDir.exists())
	        {
	        	sub_folderDir.mkdir();
	        }

	        String sub_folder2="remittance";
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
	        
	        
	        int count=0;
	        query="SELECT COUNT(*) "
	        		+ "FROM FMS_DERV_INV_FILE_DTL "
	        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
 	        		+ "AND PDF_TYPE='P' AND INV_TYPE=? ";
	        stmt=dbcon.prepareStatement(query);
	        stmt.setString(1, comp_cd);
	        stmt.setString(2, bu_state_tin);
	        stmt.setString(3, invoice_seq);
	        stmt.setString(4, financial_year);
	        stmt.setString(5, invoice_type);
	        rset=stmt.executeQuery();
	        if(rset.next())
	        {
	        	count=rset.getInt(1);
	        }
	        rset.close();
	        stmt.close();
	        
	        if(count > 0)
	        {
	        	 query1="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
	        			 + "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
	        	 stmt1=dbcon.prepareStatement(query1);
	        	 stmt1.setString(1, file_name);
		         stmt1.setString(2, emp_cd);
	        	 stmt1.setString(3, comp_cd);
		         stmt1.setString(4, bu_state_tin);
		         stmt1.setString(5, invoice_seq);
		         stmt1.setString(6, financial_year);
		         stmt1.setString(7, "P");
		         stmt1.setString(8, invoice_type);
	        	 stmt1.executeUpdate();
	        	 
	        	 stmt1.close();
	        }
	        else
	        {
	        	query1="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
	        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
	        			+ "VALUES(?,?,?,?,?,"
	        			+ "?,?,SYSDATE,?)";
	        	stmt1=dbcon.prepareStatement(query1);
	        	stmt1.setString(1, comp_cd);
		        stmt1.setString(2, bu_state_tin);
		        stmt1.setString(3, invoice_seq);
		        stmt1.setString(4, financial_year);
		        stmt1.setString(5, "P");
		        stmt1.setString(6, file_name);
		        stmt1.setString(7, emp_cd);
		        stmt1.setString(8, invoice_type);
	        	stmt1.executeUpdate();
	        	 
	        	stmt1.close();
	        }
	        
	        msg = "Successful! - Derivatives Remittance Received File Uploaded!";
			msg_type="S";
			
			
			url = "../derivatives/frm_derivatives_invoice_generation.jsp?msg="+msg+"&msg_type="+msg_type+"&month="+month+"&year="+year+"&accroid="+accroid+
					"&billing_cycle="+billing_cycle+commonUrl_pra;
			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - Purchase Remittance Received File Upload Failed!";

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
	
	private void InsertUpdateSapInvoiceApprove(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSapInvoiceApprove();";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String sap_approval_flag = request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
			String xmlfile_nm ="";
			
			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
					query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG='F' ";
					sap_approval_flag="Y";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, financial_year);
					stmt.setString(5, invoice_seq);
					stmt.setString(6, bu_state_tin);
					stmt.setString(7, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
					
				msg = "Successful! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
				
				String workDir=CommonVariable.work_dir+comp_cd;
				String sapxml_dir="";
				sapxml_dir=CommonVariable.sap_xml;
				String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");
				
				DBDerv.setCallFlag("DERV_SAP_XML");
				DBDerv.setRequest(request);
				DBDerv.setCont_type(contract_type);
				DBDerv.setFy_year(financial_year);
				DBDerv.setInvoice_seq(invoice_seq);
				DBDerv.setCounterparty_cd(counterparty_cd);
				DBDerv.setInvoice_no(invoice_no);
				DBDerv.setInv_type(invoice_type);
				DBDerv.setComp_cd(comp_cd);
				DBDerv.setFile_path(appPath);
				DBDerv.setBu_state_tin(bu_state_tin);
				DBDerv.setSap_approval_flag(sap_approval_flag);
				DBDerv.setEmp_cd(emp_cd);
				DBDerv.init();
				
				xmlfile_nm = DBDerv.getXmlfile_name();
				
				SapInvoiceMailBody(invoice_no, counterparty_cd, invoice_type);
				//generatePurchaseInvoiceXML(request);
			}
			else
			{	
				sap_approval_flag="N";
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			
			url = "../derivatives/rpt_view_derivatives_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
					"&contract_type="+contract_type+"&invoice_type="+invoice_type+
					"&counterparty_cd="+counterparty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+"&sap_approval_flag="+sap_approval_flag+
					"&xmlfile_nm="+xmlfile_nm+"&accroid="+accroid+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg="Error In Exception! - Insert/Update SAP Approval Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void InsertUpdateDerivativesInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDerivativesInvoiceApproval()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String[] agmt_no = request.getParameterValues("agmtNo");
			String[] agmt_rev = request.getParameterValues("agmtRev");
			String[] cont_no = request.getParameterValues("contNo");
			String[] cont_rev = request.getParameterValues("contRev");
			String[] instrument_no = request.getParameterValues("instrumentNo");
			String[] contract_type = request.getParameterValues("contType");
			String financial_curve = request.getParameter("financeCurve")==null?"":request.getParameter("financeCurve");
			String[] buy_sell = request.getParameterValues("buySell");
			String[] alloc_qty = request.getParameterValues("qty");
			String[] buy_rate = request.getParameterValues("buy_rate");
			String[] buy_amt = request.getParameterValues("buy_amt");
			String[] sell_rate = request.getParameterValues("sell_rate");
			String[] sell_amt = request.getParameterValues("sell_amt");
			String[] total_amt = request.getParameterValues("total_amt");
			String[] cont_ref = request.getParameterValues("cont_ref");
			String[] period_start_dt = request.getParameterValues("frm_dt");
			String[] period_end_dt = request.getParameterValues("to_dt");
			String buy_rate_unit = "2";
			String sell_rate_unit = "2";
			
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			
			
			String billing_cycle = request.getParameter("billing_cycle")==null?"12":request.getParameter("billing_cycle");
			
			String contact_person = request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
			String invoice_type = request.getParameter("inv_type_flg")==null?"":request.getParameter("inv_type_flg");
			String invoice_ref_no = request.getParameter("inv_ref")==null?"":request.getParameter("inv_ref");
			String inv_amt = request.getParameter("inv_amt")==null?"":request.getParameter("inv_amt");
	
			String invoice_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
			String invoice_due_dt = request.getParameter("due_dt")==null?"":request.getParameter("due_dt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"2":request.getParameter("invoice_raised_in"); 
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String activityFlag = request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String all_contract_type="";
			String all_cont_no="";
			String all_cont_rev="";
			String all_agmt_no="";
			String all_agmt_rev="";
			String all_instrument_no="";
			String all_buy_sell="";
			String all_deal_no="";
			String all_deal_no_without_ref="";
			String all_period_start_dt="";
			String all_period_end_dt="";
			String invdtl="";
			if(invoice_type.equals("R"))
			{
				invdtl="Derivatives Remittance";
			}
			else if(invoice_type.equals("I"))
			{
				invdtl="Derivatives Invoice";
			}
			int count_inv_id_seq=0;
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			
			if(activityFlag.equals("CHECK"))
			{
				for(int i=0; i<cont_no.length; i++)
				{
					financial_year=utilDate.getFinancialYear(period_end_dt[i]);
					query="UPDATE FMS_DERV_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG='F' ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, cont_no[i]);
					stmt.setString(6, agmt_no[i]);
					stmt.setString(7, plant_seq);
					stmt.setString(8, bu_plant_seq);
					stmt.setString(9, contract_type[i]);
					stmt.setString(10, bu_state_tin);
					stmt.setString(11, period_start_dt[i]);
					stmt.setString(12, period_end_dt[i]);
					stmt.setString(13, financial_year);
					stmt.setString(14, invoice_seq);
					stmt.setString(15, instrument_no[i]);
					stmt.setString(16, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					if(all_cont_no.equals(""))
			        {
		                all_agmt_no = agmt_no[i];
		                all_agmt_rev = agmt_rev[i];
		                all_cont_no = cont_no[i];
		                all_cont_rev = cont_rev[i];
		                all_contract_type = contract_type[i];
		                all_instrument_no = instrument_no[i];
		                all_buy_sell = buy_sell[i];
		                all_deal_no_without_ref = deal_no;
		                all_deal_no = deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = period_start_dt[i];
		                all_period_end_dt = period_end_dt[i];
			        }
		          	else
		          	{
		          		all_agmt_no = all_agmt_no+", "+agmt_no[i];
		                all_agmt_rev = all_agmt_rev+", "+agmt_rev[i];
		                all_cont_no = all_cont_no+", "+cont_no[i];
		                all_cont_rev = all_cont_rev+", "+cont_rev[i];
		                all_contract_type = all_contract_type+", "+contract_type[i];
		                all_instrument_no = all_instrument_no+", "+instrument_no[i];
		                all_deal_no_without_ref = all_deal_no_without_ref+", "+deal_no;
		                all_deal_no = all_deal_no+", "+deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = all_period_start_dt+", "+period_start_dt[i];
		                all_period_end_dt = all_period_end_dt+", "+period_end_dt[i];
		      		}
				}
				
				msg = "Successful! - Invoice for "+counterparty_abbr+" Check ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
			}
			else if(activityFlag.equals("AUTHORIZE"))
			{
				for(int i=0; i<cont_no.length; i++)
				{
					financial_year=utilDate.getFinancialYear(period_end_dt[i]);
					query="UPDATE FMS_DERV_INVOICE_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG='F' ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, cont_no[i]);
					stmt.setString(6, agmt_no[i]);
					stmt.setString(7, plant_seq);
					stmt.setString(8, bu_plant_seq);
					stmt.setString(9, contract_type[i]);
					stmt.setString(10, bu_state_tin);
					stmt.setString(11, period_start_dt[i]);
					stmt.setString(12, period_end_dt[i]);
					stmt.setString(13, financial_year);
					stmt.setString(14, invoice_seq);
					stmt.setString(15, instrument_no[i]);
					stmt.setString(16, invoice_type);
					stmt.executeUpdate();
					
					stmt.close();
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					if(all_cont_no.equals(""))
			        {
		                all_agmt_no = agmt_no[i];
		                all_agmt_rev = agmt_rev[i];
		                all_cont_no = cont_no[i];
		                all_cont_rev = cont_rev[i];
		                all_contract_type = contract_type[i];
		                all_instrument_no = instrument_no[i];
		                all_buy_sell = buy_sell[i];
		                all_deal_no_without_ref = deal_no;
		                all_deal_no = deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = period_start_dt[i];
		                all_period_end_dt = period_end_dt[i];
			        }
		          	else
		          	{
		          		all_agmt_no = all_agmt_no+", "+agmt_no[i];
		                all_agmt_rev = all_agmt_rev+", "+agmt_rev[i];
		                all_cont_no = all_cont_no+", "+cont_no[i];
		                all_cont_rev = all_cont_rev+", "+cont_rev[i];
		                all_contract_type = all_contract_type+", "+contract_type[i];
		                all_instrument_no = all_instrument_no+", "+instrument_no[i];
		                all_deal_no_without_ref = all_deal_no_without_ref+", "+deal_no;
		                all_deal_no = all_deal_no+", "+deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = all_period_start_dt+", "+period_start_dt[i];
		                all_period_end_dt = all_period_end_dt+", "+period_end_dt[i];
		      		}
				}
				msg = "Successful! - Invoice for "+counterparty_abbr+" Authorize ";
				if (rd.equals("Y"))
				{
					msg+="Approved!";
				}
				else
				{
					msg+="Rejected!";
				}	
				msg_type="S";
				
				InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
			}
			else if(activityFlag.equals("APPROVE"))
			{
				count_inv_id_seq=0;
				String contType="";
				String invSeries="";
				contType="'V'";
				invSeries="V";
				
				query="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") AND INV_TYPE=? AND INV_FLAG='F' ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_id_seq);
				stmt.setString(5, invoice_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					for(int i=0; i<cont_no.length; i++)
					{
						financial_year=utilDate.getFinancialYear(period_end_dt[i]);
						query="UPDATE FMS_DERV_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
								+ "INVOICE_NO=?,INVOICE_ID_SEQ=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG='F' ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, rd);
						stmt.setString(2, emp_cd);
						stmt.setString(3, invoice_no);
						stmt.setString(4, invoice_id_seq);
						stmt.setString(5, comp_cd);
						stmt.setString(6, counterparty_cd);
						stmt.setString(7, cont_no[i]);
						stmt.setString(8, agmt_no[i]);
						stmt.setString(9, plant_seq);
						stmt.setString(10, bu_plant_seq);
						stmt.setString(11, contract_type[i]);
						stmt.setString(12, bu_state_tin);
						stmt.setString(13, period_start_dt[i]);
						stmt.setString(14, period_end_dt[i]);
						stmt.setString(15, financial_year);
						stmt.setString(16, invoice_seq);
						stmt.setString(17, instrument_no[i]);
						stmt.setString(18, invoice_type);
						stmt.executeUpdate();
						
						stmt.close();
						
						String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
						if(all_cont_no.equals(""))
				        {
			                all_agmt_no = agmt_no[i];
			                all_agmt_rev = agmt_rev[i];
			                all_cont_no = cont_no[i];
			                all_cont_rev = cont_rev[i];
			                all_contract_type = contract_type[i];
			                all_instrument_no = instrument_no[i];
			                all_buy_sell = buy_sell[i];
			                all_deal_no_without_ref = deal_no;
			                all_deal_no = deal_no+" ["+cont_ref[i]+"] ";
			                all_period_start_dt = period_start_dt[i];
			                all_period_end_dt = period_end_dt[i];
				        }
			          	else
			          	{
			          		all_agmt_no = all_agmt_no+", "+agmt_no[i];
			                all_agmt_rev = all_agmt_rev+", "+agmt_rev[i];
			                all_cont_no = all_cont_no+", "+cont_no[i];
			                all_cont_rev = all_cont_rev+", "+cont_rev[i];
			                all_contract_type = all_contract_type+", "+contract_type[i];
			                all_instrument_no = all_instrument_no+", "+instrument_no[i];
			                all_deal_no_without_ref = all_deal_no_without_ref+", "+deal_no;
			                all_deal_no = all_deal_no+", "+deal_no+" ["+cont_ref[i]+"] ";
			                all_period_start_dt = all_period_start_dt+", "+period_start_dt[i];
			                all_period_end_dt = all_period_end_dt+", "+period_end_dt[i];
			      		}
					}
					
					msg = "Successful! - Invoice "+invoice_no+" for "+counterparty_abbr+" ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="S";
					
					InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
				}
				else
				{
					msg = "Failed! - Derivatives Invoice "+invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../derivatives/rpt_derv_view_chk_aprv_dtl.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+all_contract_type+"&cont_no="+all_cont_no+"&month="+month+"&year="+year+"&inv_dt="+invoice_dt+
					"&cont_rev="+all_cont_rev+"&agmt_no="+all_agmt_no+"&agmt_rev="+all_agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+all_period_start_dt+"&temp_period_start_dt="+all_period_start_dt+
					"&temp_period_end_dt="+all_period_end_dt+"&period_end_dt="+all_period_end_dt+"&bu_unit="+bu_plant_seq+"&billing_cycle="+billing_cycle+"&instrument_no="+all_instrument_no+"&inv_type="+invoice_type+
					"&inv_seq="+invoice_seq+"&financial_curve="+financial_curve+"&exist_financial_year="+financial_year+"&buy_sell="+all_buy_sell+"&bu_state_tin="+bu_state_tin+"&operation="+opration+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Invoice/Remittance Generation Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertGenDervInv(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertGenDervInv()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String[] agmt_no = request.getParameterValues("agmtNo");
			String[] agmt_rev = request.getParameterValues("agmtRev");
			String[] cont_no = request.getParameterValues("contNo");
			String[] cont_rev = request.getParameterValues("contRev");
			String[] instrument_no = request.getParameterValues("instrumentNo");
			String[] contract_type = request.getParameterValues("contType");
			String financial_curve = request.getParameter("financeCurve")==null?"":request.getParameter("financeCurve");
			String[] buy_sell = request.getParameterValues("buySell");
			String[] alloc_qty = request.getParameterValues("qty");
			String[] buy_rate = request.getParameterValues("buy_rate");
			String[] buy_amt = request.getParameterValues("buy_amt");
			String[] sell_rate = request.getParameterValues("sell_rate");
			String[] sell_amt = request.getParameterValues("sell_amt");
			String[] total_amt = request.getParameterValues("total_amt");
			String[] cont_ref = request.getParameterValues("cont_ref");
			String[] period_start_dt = request.getParameterValues("frm_dt");
			String[] period_end_dt = request.getParameterValues("to_dt");
			String buy_rate_unit = "2";
			String sell_rate_unit = "2";
			
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("fy_year")==null?"":request.getParameter("fy_year");
			
			
			String billing_cycle = request.getParameter("billing_cycle")==null?"12":request.getParameter("billing_cycle");
			
			String contact_person = request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
			String invoice_type = request.getParameter("inv_type_flg")==null?"":request.getParameter("inv_type_flg");
			String invoice_ref_no = request.getParameter("inv_ref")==null?"":request.getParameter("inv_ref");
			String inv_amt = request.getParameter("grand_total_amt")==null?"":request.getParameter("grand_total_amt");
	
			String invoice_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
			String invoice_due_dt = request.getParameter("due_dt")==null?"":request.getParameter("due_dt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"2":request.getParameter("invoice_raised_in"); 
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String accroid ="";
			
			String all_contract_type="";
			String all_cont_no="";
			String all_cont_rev="";
			String all_agmt_no="";
			String all_agmt_rev="";
			String all_instrument_no="";
			String all_buy_sell="";
			String all_deal_no_without_ref="";
			String all_deal_no="";
			String all_period_start_dt="";
			String all_period_end_dt="";
			
			String invdtl="";
			if(invoice_type.equals("R"))
			{
				invdtl="Derivatives Remittance";
			}
			else if(invoice_type.equals("I"))
			{
				invdtl="Derivatives Invoice";
			}
			
			String old_inv_type_flg="";
			String invoice_seq = "";
			String inv_seq="";
			if(opration.equals("INSERT"))
			{
				inv_seq="1";
				query="SELECT MAX(INVOICE_SEQ) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=? ";
				stmt=dbcon.prepareStatement(query);
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
			}
			else if(opration.equals("MODIFY"))
			{
				invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
				old_inv_type_flg=request.getParameter("old_inv_type_flg")==null?"":request.getParameter("old_inv_type_flg");
				financial_year=request.getParameter("exist_financial_year")==null?"":request.getParameter("exist_financial_year");
				accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			}
			
			int gen_success=0;
			for(int i=0; i<cont_no.length; i++)
			{
				financial_year=utilDate.getFinancialYear(period_end_dt[i]);
				if(opration.equals("INSERT"))
				{
					int count=0;
					int cnt=0;
					query="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ=? AND BU_UNIT=? AND BU_STATE_TIN=? AND INSTRUMENT_NO=? "
							+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND INV_TYPE=? AND INV_FLAG='F' ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, counterparty_cd);
					stmt.setString(++cnt, cont_no[i]);
					stmt.setString(++cnt, agmt_no[i]);
					stmt.setString(++cnt, contract_type[i]);
					stmt.setString(++cnt, plant_seq);
					stmt.setString(++cnt, bu_plant_seq);
					stmt.setString(++cnt, bu_state_tin);
					stmt.setString(++cnt, instrument_no[i]);
					stmt.setString(++cnt, period_start_dt[i]);
					stmt.setString(++cnt, period_end_dt[i]);
					stmt.setString(++cnt, invoice_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count==0)
					{
						invoice_seq=inv_seq;
						
						int cnt1=0;
						
						query1="INSERT INTO FMS_DERV_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
								+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_REF_NO,INV_TYPE,"
								+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
								+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
								+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,REMARK_1,REMARK_2,INSTRUMENT_NO,INV_FLAG) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,?,?,"
								+ "?,?,?,?,SYSDATE,?,?,?,'F')";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, financial_year);
						stmt1.setString(++cnt1, agmt_no[i]);
						stmt1.setString(++cnt1, agmt_rev[i]);
						stmt1.setString(++cnt1, cont_no[i]);
						stmt1.setString(++cnt1, cont_rev[i]);
						stmt1.setString(++cnt1, contract_type[i]);
						stmt1.setString(++cnt1, bu_plant_seq);
						stmt1.setString(++cnt1, bu_contact_person);
						stmt1.setString(++cnt1, bu_state_tin);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, contact_person);
						stmt1.setString(++cnt1, invoice_seq);
						stmt1.setString(++cnt1, invoice_no);
						stmt1.setString(++cnt1, invoice_dt);
						stmt1.setString(++cnt1, invoice_ref_no);
						stmt1.setString(++cnt1, invoice_type);
						stmt1.setString(++cnt1, billing_cycle);
						stmt1.setString(++cnt1, period_start_dt[i]);
						stmt1.setString(++cnt1, period_end_dt[i]);
						stmt1.setString(++cnt1, invoice_due_dt);
						stmt1.setString(++cnt1, alloc_qty[i]);
						stmt1.setString(++cnt1, sell_rate[i]);
						stmt1.setString(++cnt1, sell_rate_unit);
						stmt1.setString(++cnt1, sell_amt[i]);
						stmt1.setString(++cnt1, buy_rate[i]);
						stmt1.setString(++cnt1, buy_rate_unit);
						stmt1.setString(++cnt1, buy_amt[i]);
						stmt1.setString(++cnt1, invoice_raised_in);
						stmt1.setString(++cnt1, total_amt[i]);
						stmt1.setString(++cnt1, total_amt[i]);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, remark1);
						stmt1.setString(++cnt1, remark2);
						stmt1.setString(++cnt1, instrument_no[i]);
						stmt1.executeUpdate();
						stmt1.close();
						
						gen_success++;
					}
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					if(all_cont_no.equals(""))
			        {
		                all_agmt_no = agmt_no[i];
		                all_agmt_rev = agmt_rev[i];
		                all_cont_no = cont_no[i];
		                all_cont_rev = cont_rev[i];
		                all_contract_type = contract_type[i];
		                all_instrument_no = instrument_no[i];
		                all_buy_sell = buy_sell[i];
		                all_deal_no_without_ref = deal_no;
		                all_deal_no = deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = period_start_dt[i];
		                all_period_end_dt = period_end_dt[i];
			        }
		          	else
		          	{
		          		all_agmt_no = all_agmt_no+"@@"+agmt_no[i];
		                all_agmt_rev = all_agmt_rev+"@@"+agmt_rev[i];
		                all_cont_no = all_cont_no+"@@"+cont_no[i];
		                all_cont_rev = all_cont_rev+"@@"+cont_rev[i];
		                all_contract_type = all_contract_type+"@@"+contract_type[i];
		                all_instrument_no = all_instrument_no+"@@"+instrument_no[i];
		                all_buy_sell = all_buy_sell+"@@"+buy_sell[i];
		                all_deal_no_without_ref = all_deal_no_without_ref+", "+deal_no;
		                all_deal_no = all_deal_no+", "+deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = all_period_start_dt+"@@"+period_start_dt[i];
		                all_period_end_dt = all_period_end_dt+"@@"+period_end_dt[i];
		      		}
				}
				else if(opration.equals("MODIFY"))
				{
					int cnt1=0;
					query1="UPDATE FMS_DERV_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_BY=?,MODIFY_DT=SYSDATE,INV_TYPE=?,FINANCIAL_YEAR=?,"
							+ "INVOICE_REF_NO=?,ALLOC_QTY=?,SALE_PRICE=?,SALE_AMT=?,BUY_PRICE=?,BUY_AMT=?,INVOICE_AMT=?,NET_PAYABLE_AMT=?,REMARK_1=?,REMARK_2=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND BU_UNIT=? AND PLANT_SEQ=? "
							+ "AND INV_TYPE=? AND FINANCIAL_YEAR=? AND FREQ=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND INV_FLAG='F' ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, invoice_due_dt);
					stmt1.setString(++cnt1, period_start_dt[i]);
					stmt1.setString(++cnt1, period_end_dt[i]);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, financial_year);
					stmt1.setString(++cnt1, invoice_ref_no);
					stmt1.setString(++cnt1, alloc_qty[i]);
					stmt1.setString(++cnt1, sell_rate[i]);
					stmt1.setString(++cnt1, sell_amt[i]);
					stmt1.setString(++cnt1, buy_rate[i]);
					stmt1.setString(++cnt1, buy_amt[i]);
					stmt1.setString(++cnt1, total_amt[i]);
					stmt1.setString(++cnt1, total_amt[i]);
					stmt1.setString(++cnt1, remark1);
					stmt1.setString(++cnt1, remark2);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, counterparty_cd);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, bu_state_tin);
					stmt1.setString(++cnt1, bu_plant_seq);
					stmt1.setString(++cnt1, plant_seq);
					stmt1.setString(++cnt1, old_inv_type_flg);
					stmt1.setString(++cnt1, financial_year);
					stmt1.setString(++cnt1, billing_cycle);
					stmt1.setString(++cnt1, agmt_no[i]);
					stmt1.setString(++cnt1, cont_no[i]);
					stmt1.setString(++cnt1, contract_type[i]);
					stmt1.setString(++cnt1, instrument_no[i]);
					stmt1.executeUpdate();
					stmt1.close();
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					if(all_cont_no.equals(""))
			        {
		                all_agmt_no = agmt_no[i];
		                all_agmt_rev = agmt_rev[i];
		                all_cont_no = cont_no[i];
		                all_cont_rev = cont_rev[i];
		                all_contract_type = contract_type[i];
		                all_instrument_no = instrument_no[i];
		                all_buy_sell = buy_sell[i];
		                all_deal_no_without_ref = deal_no;
		                all_deal_no = deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = period_start_dt[i];
		                all_period_end_dt = period_end_dt[i];
			        }
		          	else
		          	{
		          		all_agmt_no = all_agmt_no+", "+agmt_no[i];
		                all_agmt_rev = all_agmt_rev+", "+agmt_rev[i];
		                all_cont_no = all_cont_no+", "+cont_no[i];
		                all_cont_rev = all_cont_rev+", "+cont_rev[i];
		                all_contract_type = all_contract_type+", "+contract_type[i];
		                all_instrument_no = all_instrument_no+", "+instrument_no[i];
		                all_deal_no_without_ref = all_deal_no_without_ref+", "+deal_no;
		                all_deal_no = all_deal_no+", "+deal_no+" ["+cont_ref[i]+"] ";
		                all_period_start_dt = all_period_start_dt+", "+period_start_dt[i];
		                all_period_end_dt = all_period_end_dt+", "+period_end_dt[i];
		      		}
				}
			}
			
			if(opration.equals("INSERT"))
			{
				if(gen_success==cont_no.length)
				{
					InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, "INSERT","",invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
					msg = "Successful! - Derivatives Invoice Generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Invoice Already Generated!";
					msg_type="E";
				}
				
				url = "../derivatives/frm_derv_inv_prep.jsp?counterparty_cd="+counterparty_cd+"&cont_type="+all_contract_type+"&cont_no="+all_cont_no+"&month="+month+"&year="+year+
						"&cont_rev="+all_cont_rev+"&agmt_no="+all_agmt_no+"&agmt_rev="+all_agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+all_period_start_dt+
						"&period_end_dt="+all_period_end_dt+"&bu_plant_seq="+bu_plant_seq+"&billing_cycle="+billing_cycle+"&instrument_no="+all_instrument_no+
						"&inv_seq="+invoice_seq+"&financial_curve="+financial_curve+"&fy_year="+financial_year+"&buy_sell="+all_buy_sell+"&bu_state_tin="+bu_state_tin+
						"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else if(opration.equals("MODIFY"))
			{
				InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, "MODIFY","",invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
				msg = "Successful! - Derivatives Invoice Modified!";
				msg_type="S";
				
				url = "../derivatives/frm_modify_derivatives_invoice.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+all_contract_type+"&cont_no="+all_cont_no+"&month="+month+"&year="+year+"&inv_dt="+invoice_dt+
						"&cont_rev="+all_cont_rev+"&agmt_no="+all_agmt_no+"&agmt_rev="+all_agmt_rev+"&plant_seq="+plant_seq+"&period_start_dt="+all_period_start_dt+"&temp_period_start_dt="+all_period_start_dt+
						"&temp_period_end_dt="+all_period_end_dt+"&period_end_dt="+all_period_end_dt+"&bu_unit="+bu_plant_seq+"&billing_cycle="+billing_cycle+"&instrument_no="+all_instrument_no+"&inv_type="+invoice_type+
						"&inv_seq="+invoice_seq+"&financial_curve="+financial_curve+"&exist_financial_year="+financial_year+"&buy_sell="+all_buy_sell+"&bu_state_tin="+bu_state_tin+"&operation="+opration+"&accroid="+accroid+
						"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Invoice/Remittance Generation Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void SendDervInvoiceMail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="SendDervInvoiceMail()";
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
			//String[] pdf_type = request.getParameterValues("pdf_type");
			
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			
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
						
							queryString="SELECT COUNT(*) "
		            		  		+ "FROM FMS_DERV_INVOICE_MST A, FMS_DERV_INV_FILE_DTL B "
		            		  		+ "WHERE A.COMPANY_CD=? AND A.BU_STATE_TIN=? AND A.INVOICE_SEQ=? AND A.FINANCIAL_YEAR=? AND B.PDF_TYPE=? "
		            		  		+ "AND A.INV_FLAG=? AND A.INV_TYPE=? AND A.INV_TYPE=B.INV_TYPE "
		            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, bu_state_tin);
							stmt.setString(3, invoice_seq);
							stmt.setString(4, financial_year);
							stmt.setString(5, "O");
							stmt.setString(6, "F");
							stmt.setString(7, invoice_type);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								entryExist=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(entryExist > 0)
							{
								queryString="UPDATE FMS_DERV_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
			            		  		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? "
			            		  		+ "AND INV_TYPE=?";
								stmt=dbcon.prepareStatement(queryString);
								stmt.setString(1, "Y");
								stmt.setString(2, emp_cd);
								stmt.setString(3, comp_cd);
								stmt.setString(4, bu_state_tin);
								stmt.setString(5, invoice_seq);
								stmt.setString(6, financial_year);
								stmt.setString(7, "O");
								stmt.setString(8, invoice_type);
								stmt.executeUpdate();
								entryUpdated++;
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
						new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
					}
				}
			}
			
			url = "../derivatives/frm_derivatives_sign_pdf_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Send Invoice Mail Failed";
			
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
	
	private String InvoiceMailBody(String company_cd,String customer,String cont_no,String inv_no,String inv_period,String due_dt,String inv_amount,
			String activity_type,String flag,String inv_type,String deal_contract_ref,String subject_line,String invoice_raised_in, String invoice_dt,String invdtl) throws Exception
	{
		String function_nm="InvoiceMailBody()";
		String mailBody="";
		try
		{
			String inv_nm=invdtl;
            
            String company_abbr=utilBean.getCompanyAbbr(dbcon, company_cd);
			String mail_subject=company_abbr+"/"+customer+"("+cont_no+") "+inv_nm+" ("+inv_period+")";
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
			/*else if(activity_type.equals("AUTHORIZE"))
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
			}*/
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Approved</font>";
							mailBody+= ", Please start Fin Ops Finalization Process!";
							mailBody+= "</span><br><br>";
					
					mail_subject+=" IRP Approved OK!";
				}
				else
				{
					mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" is "
							+ "<font style='background:"+highlight_reje+"' color='white'>Rejected</font> while IRP Checking!</span><br><br>";
					
					mail_subject+=" IRP Checking Rejected!";
				}
			}
			else if(activity_type.equals("MODIFY"))
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Modified</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Modified!";
			}
			else if(activity_type.equals("INSERT"))
			{
				mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following "+inv_nm+" "
						+ "<font style='background:"+highlight_aprv+"' color='white'>IRP Generated</font> in System, Please start IRP checking Process!</span><br><br>";
				
				mail_subject+=" IRP Generated!";
			}
			
			mailBody+= "<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";padding:2px;' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>&nbsp;Legal Entity</b>&nbsp;</td><td>&nbsp;"+company_abbr+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Customer</b>&nbsp;</td><td>&nbsp;"+customer+"&nbsp;</td></tr>"
					+ "<tr><td><b>&nbsp;Contract#</b>&nbsp;</td><td>&nbsp;"+deal_contract_ref+"&nbsp;</td></tr>"
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
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Derivatives", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Derivatives", "NA", "On-Event");
			}
			else if(activity_type.equals("AUTHORIZE"))
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Auth Notification", "Derivatives", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Auth Notification", "Derivatives", "NA", "On-Event");
			}
			else if(activity_type.equals("CHECK"))
			{
				if(flag.equals("Y"))
				{
					to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Derivatives", "NA", "On-Event");
					cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Approval Notification", "Derivatives", "NA", "On-Event");
				}
				to_mail_list += ","+utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Derivatives", "NA", "On-Event");
				cc_mail_list += ","+utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Derivatives", "NA", "On-Event");
			}
			else
			{
				to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Derivatives", "NA", "On-Event");
				cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Check Notification", "Derivatives", "NA", "On-Event");
			}
			
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_list, mail_subject, mailBody, "", cc_mail_list, "");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
		
		return mailBody;
	}
	
	private void SapInvoiceMailBody(String inv_no,String conterparty_cd,String invoice_type) throws Exception
	{
		String function_nm="SapInvoiceMailBody();";
		String mailBody="";
		try
		{
			String mail_subject="";
			String inv="";
			if(invoice_type.equals("R"))
			{
				inv="Remittance";
				mail_subject="Derivatives Remittance "+inv_no+" - Approved by Fin. Ops.!";
			}
			else
			{
				inv="Invoice";
				mail_subject="Derivatives Invoice "+inv_no+" - Approved by Fin. Ops.!";
			}
			
			String highlight_aprv="#00cc00";
			String highlight_reje="red";
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Dear Recipients,</span><br><br>";
			mailBody+= "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Following Derivatives "+inv+" is "
					+ "<font style='background:"+highlight_aprv+"' color='white'>Approved</font> by Fin. Ops. for SAP P80 posting!</span><br><br>";
					
			mailBody+="<table bordercolor='black' style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";' border='1' cellpadding='0' cellspacing='0'>"
					+ "<tr><td><b>Customer</b>&nbsp;</td><td>&nbsp;"+utilBean.getCounterpartyName(dbcon,conterparty_cd)+"&nbsp;</td></tr>"
					+ "<tr><td><b>Invoice#</b>&nbsp;</td><td>&nbsp;"+inv_no+"&nbsp;</td></tr>"
					+ "</table>";
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+="</html>";
			
			String to_mail_list = utilBean.getToMailReceipentList(dbcon,comp_cd,"Invoice Fin. Ops. Approval Notification", "Derivatives", "NA", "On-Event");
			String cc_mail_list = utilBean.getCcMailReceipentList(dbcon,comp_cd,"Invoice Fin. Ops. Approval Notification", "Derivatives", "NA", "On-Event");
			
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_list, mail_subject, mailBody, "", cc_mail_list, "");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void InsertGenDervCRDR(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertGenDervCRDR()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev = request.getParameterValues("agmt_rev");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev = request.getParameterValues("cont_rev");
			String[] instrument_no = request.getParameterValues("instrument_no");
			String[] contract_type = request.getParameterValues("cont_type");
			String financial_curve = request.getParameter("financeCurve")==null?"":request.getParameter("financeCurve");
			String[] buy_sell = request.getParameterValues("buySell");
			String[] alloc_qty = request.getParameterValues("qty");
			String[] buy_rate = request.getParameterValues("buy_rate");
			String[] buy_amt = request.getParameterValues("buy_amt");
			String[] sell_rate = request.getParameterValues("sell_rate");
			String[] sell_amt = request.getParameterValues("sell_amt");
			String[] total_amt = request.getParameterValues("total_amt");
			String[] period_start_dt = request.getParameterValues("period_st_dt");
			String[] period_end_dt = request.getParameterValues("period_end_dt");
			String[] new_alloc_qty = request.getParameterValues("new_qty");
			String[] new_buy_rate = request.getParameterValues("new_buy_rate");
			String[] new_buy_amt = request.getParameterValues("new_buy_amt");
			String[] new_sell_rate = request.getParameterValues("new_sell_rate");
			String[] new_sell_amt = request.getParameterValues("new_sell_amt");
			String[] new_total_amt = request.getParameterValues("new_total_amt");
			String buy_rate_unit = "2";
			String sell_rate_unit = "2";
			
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			
			String billing_cycle = request.getParameter("billing_cycle")==null?"12":request.getParameter("billing_cycle");
			
			String contact_person = request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_ref_no = request.getParameter("inv_ref")==null?"":request.getParameter("inv_ref");
			String inv_amt = request.getParameter("grand_total_amt")==null?"":request.getParameter("grand_total_amt");
	
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"2":request.getParameter("invoice_raised_in"); 
			String remark1 = request.getParameter("remark")==null?"":request.getParameter("remark");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
			String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			
			String accroid ="";
			
			String all_contract_type="";
			String all_cont_no="";
			String all_cont_rev="";
			String all_agmt_no="";
			String all_agmt_rev="";
			String all_instrument_no="";
			String all_buy_sell="";
			String all_deal_no_without_ref="";
			String all_deal_no="";
			String all_period_start_dt="";
			String all_period_end_dt="";
			
			String invdtl="";
			if(invoice_type.equals("R"))
			{
				invdtl="Derivatives Remittance";
			}
			else if(invoice_type.equals("I"))
			{
				invdtl="Derivatives Invoice";
			}
			
			String crdrdtl="";
			if(crdr_type.equals("DR"))
			{
				crdrdtl="Debit Note";
			}
			else if(crdr_type.equals("CR"))
			{
				crdrdtl="Credit Note";
			}
			
			String old_inv_type_flg="";
			String invoice_seq = "";
			String inv_seq="";
			if(opration.equals("INSERT"))
			{
				inv_seq="1";
				query="SELECT MAX(INVOICE_SEQ) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=?";
				stmt=dbcon.prepareStatement(query);
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
			}
			else if(opration.equals("MODIFY"))
			{
				invoice_seq=request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
				old_inv_type_flg=request.getParameter("old_inv_type_flg")==null?"":request.getParameter("old_inv_type_flg");
				financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
				accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			}
			
			int gen_success=0;
			int mod_success=0;
			if(opration.equals("INSERT"))
			{
				//financial_year=utilDate.getFinancialYear(period_end_dt[i]);
				for(int i=0; i<instrument_no.length; i++)
				{
					int count=0;
					int cnt=0;
					query="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ=? AND BU_UNIT=? AND BU_STATE_TIN=? AND INSTRUMENT_NO=? "
							+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND INV_TYPE=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++cnt, comp_cd);
					stmt.setString(++cnt, counterparty_cd);
					stmt.setString(++cnt, cont_no[i]);
					stmt.setString(++cnt, agmt_no[i]);
					stmt.setString(++cnt, contract_type[i]);
					stmt.setString(++cnt, plant_seq);
					stmt.setString(++cnt, bu_plant_seq);
					stmt.setString(++cnt, bu_state_tin);
					stmt.setString(++cnt, instrument_no[i]);
					stmt.setString(++cnt, period_start_dt[i]);
					stmt.setString(++cnt, period_end_dt[i]);
					stmt.setString(++cnt, invoice_type);
					stmt.setString(++cnt, crdr_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count==0)
					{
						invoice_seq=inv_seq;
						
						int cnt1=0;
						
						query1="INSERT INTO FMS_DERV_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
								+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_REF_NO,INV_TYPE,"
								+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
								+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
								+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,REMARK_1,INSTRUMENT_NO,"
								+ "CRITERIA,INV_FLAG,REF_NO) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?,?,?,"
								+ "?,?,?,?,SYSDATE,?,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, financial_year);
						stmt1.setString(++cnt1, agmt_no[i]);
						stmt1.setString(++cnt1, agmt_rev[i]);
						stmt1.setString(++cnt1, cont_no[i]);
						stmt1.setString(++cnt1, cont_rev[i]);
						stmt1.setString(++cnt1, contract_type[i]);
						stmt1.setString(++cnt1, bu_plant_seq);
						stmt1.setString(++cnt1, bu_contact_person);
						stmt1.setString(++cnt1, bu_state_tin);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, contact_person);
						stmt1.setString(++cnt1, invoice_seq);
						stmt1.setString(++cnt1, invoice_no);
						stmt1.setString(++cnt1, invoice_dt);
						stmt1.setString(++cnt1, invoice_ref_no);
						stmt1.setString(++cnt1, invoice_type);
						stmt1.setString(++cnt1, billing_cycle);
						stmt1.setString(++cnt1, period_start_dt[i]);
						stmt1.setString(++cnt1, period_end_dt[i]);
						stmt1.setString(++cnt1, invoice_due_dt);
						stmt1.setString(++cnt1, alloc_qty[i]);
						stmt1.setString(++cnt1, sell_rate[i]);
						stmt1.setString(++cnt1, sell_rate_unit);
						stmt1.setString(++cnt1, sell_amt[i]);
						stmt1.setString(++cnt1, buy_rate[i]);
						stmt1.setString(++cnt1, buy_rate_unit);
						stmt1.setString(++cnt1, buy_amt[i]);
						stmt1.setString(++cnt1, invoice_raised_in);
						stmt1.setString(++cnt1, total_amt[i]);
						stmt1.setString(++cnt1, total_amt[i]);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, remark1);
						stmt1.setString(++cnt1, instrument_no[i]);
						stmt1.setString(++cnt1, criteri_formula);
						stmt1.setString(++cnt1, crdr_type);
						stmt1.setString(++cnt1, sel_inv_no);
						stmt1.executeUpdate();
						stmt1.close();
						
						gen_success++;
					}
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int cnt=0;
				query="DELETE FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND REF_NO=? AND FINANCIAL_YEAR=? AND INV_FLAG IN ('CR','DR')";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, counterparty_cd);
				stmt.setString(++cnt, sel_inv_no);
				stmt.setString(++cnt, financial_year);
				stmt.executeUpdate();
				stmt.close();
				
				for(int i=0; i<instrument_no.length; i++)
				{
					int cnt1=0;
					
					query1="INSERT INTO FMS_DERV_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
							+ "BU_CONTACT_PERSON_CD,BU_STATE_TIN,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_REF_NO,INV_TYPE,"
							+ "FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
							+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
							+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,REMARK_1,INSTRUMENT_NO,"
							+ "CRITERIA,INV_FLAG,REF_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,?,"
							+ "?,?,?,?,SYSDATE,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, counterparty_cd);
					stmt1.setString(++cnt1, financial_year);
					stmt1.setString(++cnt1, agmt_no[i]);
					stmt1.setString(++cnt1, agmt_rev[i]);
					stmt1.setString(++cnt1, cont_no[i]);
					stmt1.setString(++cnt1, cont_rev[i]);
					stmt1.setString(++cnt1, contract_type[i]);
					stmt1.setString(++cnt1, bu_plant_seq);
					stmt1.setString(++cnt1, bu_contact_person);
					stmt1.setString(++cnt1, bu_state_tin);
					stmt1.setString(++cnt1, plant_seq);
					stmt1.setString(++cnt1, contact_person);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, invoice_no);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, invoice_ref_no);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, billing_cycle);
					stmt1.setString(++cnt1, period_start_dt[i]);
					stmt1.setString(++cnt1, period_end_dt[i]);
					stmt1.setString(++cnt1, invoice_due_dt);
					stmt1.setString(++cnt1, alloc_qty[i]);
					stmt1.setString(++cnt1, sell_rate[i]);
					stmt1.setString(++cnt1, sell_rate_unit);
					stmt1.setString(++cnt1, sell_amt[i]);
					stmt1.setString(++cnt1, buy_rate[i]);
					stmt1.setString(++cnt1, buy_rate_unit);
					stmt1.setString(++cnt1, buy_amt[i]);
					stmt1.setString(++cnt1, invoice_raised_in);
					stmt1.setString(++cnt1, total_amt[i]);
					stmt1.setString(++cnt1, total_amt[i]);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, remark1);
					stmt1.setString(++cnt1, instrument_no[i]);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, crdr_type);
					stmt1.setString(++cnt1, sel_inv_no);
					stmt1.executeUpdate();
					stmt1.close();
					
					mod_success++;
				}
			}
			
			int cnt=0;
			query="DELETE FROM FMS_DERV_INV_CRDR_REF "
				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_SEQ=? "
				+ "AND BU_STATE_TIN=? AND FINANCIAL_YEAR=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, counterparty_cd);
			stmt.setString(++cnt, invoice_seq);
			stmt.setString(++cnt, bu_state_tin);
			stmt.setString(++cnt, financial_year);
			stmt.executeUpdate();
			stmt.close();
			
			for(int i=0; i<instrument_no.length; i++)
			{
				int cnt1=0;
				
				query1="INSERT INTO FMS_DERV_INV_CRDR_REF(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "BU_STATE_TIN,INVOICE_SEQ,INV_TYPE,"
						+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
						+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,ENT_BY,ENT_DT,INSTRUMENT_NO) "
						+ "VALUES(?,?,?,?,?,?,?,?,"
						+ "?,?,?,"
						+ "?,?,?,?,?,?,?,"
						+ "?,?,?,?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++cnt1, comp_cd);
				stmt1.setString(++cnt1, counterparty_cd);
				stmt1.setString(++cnt1, financial_year);
				stmt1.setString(++cnt1, agmt_no[i]);
				stmt1.setString(++cnt1, agmt_rev[i]);
				stmt1.setString(++cnt1, cont_no[i]);
				stmt1.setString(++cnt1, cont_rev[i]);
				stmt1.setString(++cnt1, contract_type[i]);
				stmt1.setString(++cnt1, bu_state_tin);
				stmt1.setString(++cnt1, invoice_seq);
				stmt1.setString(++cnt1, invoice_type);
				stmt1.setString(++cnt1, new_alloc_qty[i]);
				stmt1.setString(++cnt1, new_sell_rate[i]);
				stmt1.setString(++cnt1, sell_rate_unit);
				stmt1.setString(++cnt1, new_sell_amt[i]);
				stmt1.setString(++cnt1, new_buy_rate[i]);
				stmt1.setString(++cnt1, buy_rate_unit);
				stmt1.setString(++cnt1, new_buy_amt[i]);
				stmt1.setString(++cnt1, invoice_raised_in);
				stmt1.setString(++cnt1, new_total_amt[i]);
				stmt1.setString(++cnt1, new_total_amt[i]);
				stmt1.setString(++cnt1, emp_cd);
				stmt1.setString(++cnt1, instrument_no[i]);
				stmt1.executeUpdate();
				stmt1.close();
				
			}
			
			if(opration.equals("INSERT"))
			{
				if(gen_success==cont_no.length)
				{
					//InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, "INSERT","",invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
					opration="MODIFY";
					msg = "Successful! - Derivatives "+crdrdtl+" Generated!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+crdrdtl+" Already Generated!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Successful! - Derivatives "+crdrdtl+" Updated!";
				msg_type="S";
			}
			
			url = "../derivatives/frm_generate_derv_crdr.jsp?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&operation="+opration+
					"&sel_inv_no="+sel_inv_no+"&invoice_seq="+invoice_seq+"&financial_year="+financial_year+"&crdr_type="+crdr_type+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Invoice/Remittance Generation Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateDerivativesCrDrInvoiceApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDerivativesCrDrInvoiceApproval()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_nm = utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String[] agmt_no = request.getParameterValues("agmtNo");
			String[] agmt_rev = request.getParameterValues("agmtRev");
			String[] cont_no = request.getParameterValues("contNo");
			String[] cont_rev = request.getParameterValues("contRev");
			String[] instrument_no = request.getParameterValues("instrumentNo");
			String[] contract_type = request.getParameterValues("contType");
			String financial_curve = request.getParameter("financeCurve")==null?"":request.getParameter("financeCurve");
			String[] buy_sell = request.getParameterValues("buySell");
			String[] alloc_qty = request.getParameterValues("qty");
			String[] buy_rate = request.getParameterValues("buy_rate");
			String[] buy_amt = request.getParameterValues("buy_amt");
			String[] sell_rate = request.getParameterValues("sell_rate");
			String[] sell_amt = request.getParameterValues("sell_amt");
			String[] total_amt = request.getParameterValues("total_amt");
			String[] cont_ref = request.getParameterValues("cont_ref");
			String[] period_start_dt = request.getParameterValues("frm_dt");
			String[] period_end_dt = request.getParameterValues("to_dt");
			String buy_rate_unit = "2";
			String sell_rate_unit = "2";
			
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			
			String billing_cycle = request.getParameter("billing_cycle")==null?"12":request.getParameter("billing_cycle");
			
			String contact_person = request.getParameter("contact_person")==null?"0":request.getParameter("contact_person");
			String bu_contact_person = request.getParameter("bu_contact_person")==null?"0":request.getParameter("bu_contact_person");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_ref_no = request.getParameter("inv_ref")==null?"":request.getParameter("inv_ref");
			String inv_amt = request.getParameter("inv_amt")==null?"":request.getParameter("inv_amt");
	
			String invoice_dt = request.getParameter("inv_dt")==null?"":request.getParameter("inv_dt");
			String invoice_due_dt = request.getParameter("due_dt")==null?"":request.getParameter("due_dt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"2":request.getParameter("invoice_raised_in"); 
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String activityFlag = request.getParameter("activityFlag")==null?"":request.getParameter("activityFlag");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
			String sel_inv_no = request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			
			String all_contract_type="";
			String all_cont_no="";
			String all_cont_rev="";
			String all_agmt_no="";
			String all_agmt_rev="";
			String all_instrument_no="";
			String all_buy_sell="";
			String all_deal_no="";
			String all_deal_no_without_ref="";
			String all_period_start_dt="";
			String all_period_end_dt="";
			String invdtl="";
			if(invoice_type.equals("R"))
			{
				invdtl="Derivatives Remittance";
			}
			else if(invoice_type.equals("I"))
			{
				invdtl="Derivatives Invoice";
			}
			
			String crdrdtl="";
			if(crdr_type.equals("DR"))
			{
				crdrdtl="Debit Note";
			}
			else if(crdr_type.equals("CR"))
			{
				crdrdtl="Credit Note";
			}
			int count_inv_id_seq=0;
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			
			if(operation.equals("CHECK"))
			{
				int count=0;
				for(int i=0; i<cont_no.length; i++)
				{
					//financial_year=utilDate.getFinancialYear(period_end_dt[i]);
					query="UPDATE FMS_DERV_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG=? AND REF_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, cont_no[i]);
					stmt.setString(6, agmt_no[i]);
					stmt.setString(7, plant_seq);
					stmt.setString(8, bu_plant_seq);
					stmt.setString(9, contract_type[i]);
					stmt.setString(10, bu_state_tin);
					stmt.setString(11, period_start_dt[i]);
					stmt.setString(12, period_end_dt[i]);
					stmt.setString(13, financial_year);
					stmt.setString(14, invoice_seq);
					stmt.setString(15, instrument_no[i]);
					stmt.setString(16, invoice_type);
					stmt.setString(17, crdr_type);
					stmt.setString(18, sel_inv_no);
					count=stmt.executeUpdate();
					
					stmt.close();
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					
				}
				
				if(count>0)
				{
					msg = "Successful! - "+crdrdtl+" Invoice for "+counterparty_abbr+" Check ";
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
					msg = "Failed! - "+crdrdtl+" Invoice for "+counterparty_abbr+" Check ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="E";
				}
				//InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
			}
			else if(operation.equals("AUTHORIZE"))
			{
				int count=0;
				for(int i=0; i<cont_no.length; i++)
				{
					//financial_year=utilDate.getFinancialYear(period_end_dt[i]);
					query="UPDATE FMS_DERV_INVOICE_MST SET AUTHORIZED_FLAG=?,AUTHORIZED_BY=?,AUTHORIZED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG=? AND REF_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, cont_no[i]);
					stmt.setString(6, agmt_no[i]);
					stmt.setString(7, plant_seq);
					stmt.setString(8, bu_plant_seq);
					stmt.setString(9, contract_type[i]);
					stmt.setString(10, bu_state_tin);
					stmt.setString(11, period_start_dt[i]);
					stmt.setString(12, period_end_dt[i]);
					stmt.setString(13, financial_year);
					stmt.setString(14, invoice_seq);
					stmt.setString(15, instrument_no[i]);
					stmt.setString(16, invoice_type);
					stmt.setString(17, crdr_type);
					stmt.setString(18, sel_inv_no);
					count=stmt.executeUpdate();
					
					stmt.close();
					
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					
				}
				
				if(count>0)
				{
					msg = "Successful! - "+crdrdtl+" Invoice for "+counterparty_abbr+" Authorize ";
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
					msg = "Failed! - "+crdrdtl+" Invoice for "+counterparty_abbr+" Authorize ";
					if (rd.equals("Y"))
					{
						msg+="Approved!";
					}
					else
					{
						msg+="Rejected!";
					}	
					msg_type="E";
				}
				//InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
			}
			else if(operation.equals("APPROVE"))
			{
				count_inv_id_seq=0;
				String contType="";
				String invSeries="";
				contType="'V'";
				invSeries="V";
				
				query="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") AND INV_TYPE=? AND INV_FLAG=? AND REF_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				stmt.setString(4, invoice_id_seq);
				stmt.setString(5, invoice_type);
				stmt.setString(6, crdr_type);
				stmt.setString(7, sel_inv_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count_inv_id_seq=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				int count=0;
				if(count_inv_id_seq==0)
				{
					for(int i=0; i<cont_no.length; i++)
					{
						financial_year=utilDate.getFinancialYear(period_end_dt[i]);
						
						query="UPDATE FMS_DERV_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,"
								+ "INVOICE_NO=?,INVOICE_ID_SEQ=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG=? AND REF_NO=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, rd);
						stmt.setString(2, emp_cd);
						stmt.setString(3, invoice_no);
						stmt.setString(4, invoice_id_seq);
						stmt.setString(5, comp_cd);
						stmt.setString(6, counterparty_cd);
						stmt.setString(7, cont_no[i]);
						stmt.setString(8, agmt_no[i]);
						stmt.setString(9, plant_seq);
						stmt.setString(10, bu_plant_seq);
						stmt.setString(11, contract_type[i]);
						stmt.setString(12, bu_state_tin);
						stmt.setString(13, period_start_dt[i]);
						stmt.setString(14, period_end_dt[i]);
						stmt.setString(15, financial_year);
						stmt.setString(16, invoice_seq);
						stmt.setString(17, instrument_no[i]);
						stmt.setString(18, invoice_type);
						stmt.setString(19, crdr_type);
						stmt.setString(20, sel_inv_no);
						count=stmt.executeUpdate();
						
						stmt.close();
						
						String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no[i], "", cont_no[i], "", contract_type[i], instrument_no[i]);
					}
					
					if(count>0)
					{
						msg = "Successful! - "+crdrdtl+" Invoice "+invoice_no+" for "+counterparty_abbr+" ";
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
						msg = "Failed! - "+crdrdtl+" Invoice "+invoice_no+" for "+counterparty_abbr+" ";
						msg_type="E";
					}
					//InvoiceMailBody(comp_cd,counterparty_nm, all_deal_no_without_ref, invoice_no, utilDate.getDateFormatDD_MOM_YY(period_start_dt[0])+" - "+utilDate.getDateFormatDD_MOM_YY(period_end_dt[0]), invoice_due_dt,inv_amt, activityFlag,rd,invoice_type,all_deal_no,"",invoice_raised_in,invoice_dt,invdtl);
				}
				else
				{
					msg = "Failed! - Derivatives "+crdrdtl+" Invoice "+invoice_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../derivatives/rpt_derv_view_chk_aprv_crdr_dtl.jsp?counterparty_cd="+counterparty_cd+"&month="+month+"&year="+year+"&operation="+operation+
					"&sel_inv_no="+sel_inv_no+"&invoice_seq="+invoice_seq+"&financial_year="+financial_year+"&crdr_type="+crdr_type+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Invoice/Remittance Generation Failed!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateSapCrdrApprove(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSapCrdrApprove();";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String sap_approval_flag = request.getParameter("sap_approval_flag")==null?"":request.getParameter("sap_approval_flag");
			String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
			String xmlfile_nm ="";
			
			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
					query ="UPDATE FMS_DERV_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG=? ";
					sap_approval_flag="Y";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, "Y");
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, financial_year);
					stmt.setString(5, invoice_seq);
					stmt.setString(6, bu_state_tin);
					stmt.setString(7, invoice_type);
					stmt.setString(8, crdr_type);
					stmt.executeUpdate();
					
					stmt.close();
					
				msg = "Successful! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
				
				String workDir=CommonVariable.work_dir+comp_cd;
				String sapxml_dir="";
				sapxml_dir=CommonVariable.sap_xml;
				String appPath = request.getServletContext().getRealPath(workDir+"/"+sapxml_dir+"/");
				
				DBDerv.setCallFlag("CRDR_DERV_SAP_XML");
				DBDerv.setRequest(request);
				DBDerv.setCont_type(contract_type);
				DBDerv.setFy_year(financial_year);
				DBDerv.setInvoice_seq(invoice_seq);
				DBDerv.setCounterparty_cd(counterparty_cd);
				DBDerv.setInvoice_no(invoice_no);
				DBDerv.setInv_type(invoice_type);
				DBDerv.setCrdr_type(crdr_type);
				DBDerv.setComp_cd(comp_cd);
				DBDerv.setFile_path(appPath);
				DBDerv.setBu_state_tin(bu_state_tin);
				DBDerv.setSap_approval_flag(sap_approval_flag);
				DBDerv.setEmp_cd(emp_cd);
				DBDerv.init();
				
				xmlfile_nm = DBDerv.getXmlfile_name();
				
				//SapInvoiceMailBody(invoice_no, counterparty_cd, invoice_type);
				//generatePurchaseInvoiceXML(request);
			}
			else
			{	
				sap_approval_flag="N";
				msg = "Failed! - "+utilBean.getCounterpartyABBR(dbcon,counterparty_cd)+" Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			
			url = "../derivatives/rpt_view_derv_crdr_invoice_sap_approval.jsp?financial_year="+financial_year+"&invoice_seq="+invoice_seq+
					"&contract_type="+contract_type+"&invoice_type="+invoice_type+
					"&counterparty_cd="+counterparty_cd+"&invoice_no="+invoice_no+"&bu_state_tin="+bu_state_tin+"&sap_approval_flag="+sap_approval_flag+
					"&xmlfile_nm="+xmlfile_nm+"&accroid="+accroid+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			msg="Error In Exception! - Insert/Update SAP Approval Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	private void SendDervCrdrMail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="SendDervCrdrMail()";
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
			String crdr_type = request.getParameter("crdr_type")==null?"":request.getParameter("crdr_type");
			
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
						
							queryString="SELECT COUNT(*) "
		            		  		+ "FROM FMS_DERV_INVOICE_MST A, FMS_DERV_INV_FILE_DTL B "
		            		  		+ "WHERE A.COMPANY_CD=? AND A.BU_STATE_TIN=? AND A.INVOICE_SEQ=? AND A.FINANCIAL_YEAR=? AND B.PDF_TYPE=? "
		            		  		+ "AND A.INV_FLAG=? AND A.INV_TYPE=? AND A.INV_TYPE=B.INV_TYPE "
		            		  		+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
		            		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, bu_state_tin);
							stmt.setString(3, invoice_seq);
							stmt.setString(4, financial_year);
							stmt.setString(5, pdf_type[i]);
							stmt.setString(6, crdr_type);
							stmt.setString(7, invoice_type);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								entryExist=rset.getInt(1);
							}
							rset.close();
							stmt.close();
							
							if(entryExist > 0)
							{
								queryString="UPDATE FMS_DERV_INV_FILE_DTL SET EMAIL_SENT=?,EMAIL_SENT_BY=?,EMAIL_SENT_DT=SYSDATE "
			            		  		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? "
			            		  		+ "AND INV_TYPE=?";
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
					
					msg="Mail Sent for "+subject[i];
					msg_type="S";
					
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
			
			url = "../derivatives/frm_derivatives_crdr_sign_pdf_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Send Invoice Mail Failed";
			
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
	
	private void CrdrFileUpload(HttpServletRequest request) throws SQLException
	{
		String function_nm="CrdrFileUpload()";
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
			
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			String bu_state_tin = request.getParameter("file_bu_state_tin")==null?"":request.getParameter("file_bu_state_tin");
			String invoice_seq = request.getParameter("file_invoice_seq")==null?"":request.getParameter("file_invoice_seq");
			String financial_year = request.getParameter("file_financial_year")==null?"":request.getParameter("file_financial_year");
			String invoice_title = request.getParameter("invoice_title")==null?"":request.getParameter("invoice_title");
			String invoice_type = request.getParameter("file_invoice_type")==null?"":request.getParameter("file_invoice_type");
			
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

	        String sub_folder="derivatives";
	        String filePath1=filePath+File.separator+sub_folder;
	        File sub_folderDir = new File(filePath1);
	        if(!sub_folderDir.exists())
	        {
	        	sub_folderDir.mkdir();
	        }

	        String sub_folder2="remittance";
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
	        
	        
	        int count=0;
	        query="SELECT COUNT(*) "
	        		+ "FROM FMS_DERV_INV_FILE_DTL "
	        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
 	        		+ "AND PDF_TYPE='P' AND INV_TYPE=? ";
	        stmt=dbcon.prepareStatement(query);
	        stmt.setString(1, comp_cd);
	        stmt.setString(2, bu_state_tin);
	        stmt.setString(3, invoice_seq);
	        stmt.setString(4, financial_year);
	        stmt.setString(5, invoice_type);
	        rset=stmt.executeQuery();
	        if(rset.next())
	        {
	        	count=rset.getInt(1);
	        }
	        rset.close();
	        stmt.close();
	        
	        if(count > 0)
	        {
	        	 query1="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
	        			 + "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
	        	 stmt1=dbcon.prepareStatement(query1);
	        	 stmt1.setString(1, file_name);
		         stmt1.setString(2, emp_cd);
	        	 stmt1.setString(3, comp_cd);
		         stmt1.setString(4, bu_state_tin);
		         stmt1.setString(5, invoice_seq);
		         stmt1.setString(6, financial_year);
		         stmt1.setString(7, "P");
		         stmt1.setString(8, invoice_type);
	        	 stmt1.executeUpdate();
	        	 
	        	 stmt1.close();
	        }
	        else
	        {
	        	query1="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
	        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
	        			+ "VALUES(?,?,?,?,?,"
	        			+ "?,?,SYSDATE,?)";
	        	stmt1=dbcon.prepareStatement(query1);
	        	stmt1.setString(1, comp_cd);
		        stmt1.setString(2, bu_state_tin);
		        stmt1.setString(3, invoice_seq);
		        stmt1.setString(4, financial_year);
		        stmt1.setString(5, "P");
		        stmt1.setString(6, file_name);
		        stmt1.setString(7, emp_cd);
		        stmt1.setString(8, invoice_type);
	        	stmt1.executeUpdate();
	        	 
	        	stmt1.close();
	        }
	        
	        msg = "Successful! - Derivatives Remittance Received File Uploaded!";
			msg_type="S";
			
			
			url = "../derivatives/frm_derivatives_CRDR_preparation.jsp?msg="+msg+"&msg_type="+msg_type+"&month="+month+"&year="+year+"&accroid="+accroid+
					"&billing_cycle="+billing_cycle+commonUrl_pra;
			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
	        msg = "Error in Exception! - Purchase Remittance Received File Upload Failed!";

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
	
	private void InsertUpdateDervInvoiceChangeAction(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateDervInvoiceChangeAction()";
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
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			
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
		        stmt.setString(5, segment+invtype);
		        stmt.setString(6, change_type);
		        stmt.setString(7, "R");
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
			        stmt.setString(5, segment+invtype);
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
						stmt2.setString(5, segment+invtype);
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
		        stmt.setString(5, segment+invtype);
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
			        stmt2.setString(7, segment+invtype);
			        stmt2.setString(8, change_type);
			        stmt2.setString(9, "R");
			        stmt2.setString(10, seq_no);
			        int upcount= stmt2.executeUpdate();
					stmt2.close();
					
					if(upcount > 0 && action_flag.equals("A"))
					{
						int reqCount=0;
						String query3="SELECT COUNT(*) "
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
				        stmt3.setString(5, segment+invtype);
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
								+ "FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TYPE=? AND PDF_TYPE IN ('O') ";
						stmt=dbcon.prepareStatement(queryString);
					    stmt.setString(1, comp_cd);
					    stmt.setString(2, bu_state_tin);
					    stmt.setString(3, invoice_seq);
					    stmt.setString(4, financial_year);
					    stmt.setString(5, invtype);
					    rset=stmt.executeQuery();
				    	while(rset.next())
				    	{
				    		String pdf_type=rset.getString(1)==null?"":rset.getString(1);
				    		String pdf_file_name=rset.getString(2)==null?"":rset.getString(2);
				    		
				    		boolean action_done=false;
				    		if(reqCount==1)
				    		{
				    			String new_pdf_file_name="";
				    			if(invtype.equals("I"))
				    			{
						    		String unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.derv_inv_path+""+pdf_file_name;
						    		String signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_derv_inv_path+""+pdf_file_name;
						    		
						    		File unsigned_file_exist = new File(unsigned_file_path);
						    		File signed_file_exist = new File(signed_file_path);
						    		String canonicalPath_unsigned_file = unsigned_file_exist.getCanonicalPath();
						    		String canonicalPath_signed_file = signed_file_exist.getCanonicalPath();
						    		
						    		String sysdateWithTime=utilDate.getSysdateWithTime24hr();
						    		String[] splitSys = sysdateWithTime.split(" ");
									String datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
									
									new_pdf_file_name=pdf_file_name.replace(".pdf", "")+"_"+datetime+".pdf";
									
									String new_unsigned_file_path = appPath+File.separator+main_folder+""+CommonVariable.derv_inv_path+""+new_pdf_file_name;
						    		String new_signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.signed_derv_inv_path+""+new_pdf_file_name;
									
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
				    			}
				    			else if(invtype.equals("R")) 
				    			{
						    		String signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.derv_remittance_path+""+pdf_file_name;
						    		
						    		File signed_file_exist = new File(signed_file_path);
						    		String canonicalPath_signed_file = signed_file_exist.getCanonicalPath();
						    		
						    		String sysdateWithTime=utilDate.getSysdateWithTime24hr();
						    		String[] splitSys = sysdateWithTime.split(" ");
									String datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
									
									new_pdf_file_name=pdf_file_name.replace(".pdf", "")+"_"+datetime+".pdf";
									
						    		String new_signed_file_path = appPath+File.separator+main_folder+""+CommonVariable.derv_remittance_path+""+new_pdf_file_name;
									
						    		File new_signed_file = new File(new_signed_file_path);
						    		
						    		if(!canonicalPath_signed_file.startsWith(appPath))
							        {
							        	throw new IOException("Entry is outside of the target directory!");
							        }
							        else if(signed_file_exist.exists())
						    		{
							        	Path temp = Files.copy(Paths.get(signed_file_path),Paths.get(new_signed_file_path),StandardCopyOption.REPLACE_EXISTING); 
						    		}
				    			}
					    		
					    		
				    			query1="UPDATE FMS_DERV_INV_FILE_DTL SET PDF_TYPE=?,FILE_NAME=? "
				    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TYPE=? AND PDF_TYPE=? ";
				    			stmt1=dbcon.prepareStatement(query1);
				    			stmt1.setString(1, "1"+pdf_type);
								stmt1.setString(2, new_pdf_file_name);
							    stmt1.setString(3, comp_cd);
							    stmt1.setString(4, bu_state_tin);
							    stmt1.setString(5, invoice_seq);
							    stmt1.setString(6, financial_year);
							    stmt1.setString(7, invtype);
							    stmt1.setString(8, pdf_type);
							    stmt1.executeUpdate();
								stmt1.close();
								
								action_done=true;
				    		}
				    		else if(reqCount > 1)
				    		{
				    			query1="DELETE FROM FMS_DERV_INV_FILE_DTL "
				    					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TYPE=? AND PDF_TYPE=? ";
				    			stmt1=dbcon.prepareStatement(query1);
				    			stmt1.setString(1, comp_cd);
							    stmt1.setString(2, bu_state_tin);
							    stmt1.setString(3, invoice_seq);
							    stmt1.setString(4, financial_year);
							    stmt1.setString(5, invtype);
							    stmt1.setString(6, pdf_type);
							    stmt1.executeUpdate();
								stmt1.close();
								
								action_done=true;
				    		}
				    		
							if(!pdf_type.equals("") && action_done)
							{
								int cont=0;
								query1="UPDATE FMS_DERV_INVOICE_MST SET ";
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
										+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=?";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(++cont, comp_cd);
								stmt1.setString(++cont, financial_year);
								stmt1.setString(++cont, invoice_seq);
								stmt1.setString(++cont, bu_state_tin);
								stmt1.setString(++cont, invtype);
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
			
			url = "../derivatives/frm_derv_inv_chng_req_aprv.jsp?month="+month+"&year="+year+"&accroid="+accroid+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! Derivative Invoice Change Action Failed!";
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
