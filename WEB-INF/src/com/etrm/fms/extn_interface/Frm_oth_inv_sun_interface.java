package com.etrm.fms.extn_interface;

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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Deep Tank
//Code Reviewed by	:  
//CR Date			: 05/01/2026
//Status	  		: Developing

@WebServlet("/servlet/Frm_oth_inv_sun_interface")
public class Frm_oth_inv_sun_interface extends HttpServlet
{
	static String frm_src_file_name="Frm_oth_inv_sun_interface";
	public static  Connection dbcon;
	
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
	private static PreparedStatement stmt0 = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt5 = null;
	private static PreparedStatement stmt6 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset0 = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset5 = null;
	private static ResultSet rset6 = null;
	
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
	static DataBean_oth_inv_sun_interface DBsun = new DataBean_oth_inv_sun_interface();
	
	public static NumberFormat nf = new DecimalFormat("###########0.00");
	public static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
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
					
					if(option.equalsIgnoreCase("SUN_ENTITY_ACC_CD"))
					{
						InsertUpdateSunEntityAccountCode(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_OTH_INVOICE_SUN_XML"))
					{
						ApproveSunXML(request);
					}
					else if(option.equalsIgnoreCase("GENERATE_SUN_XML"))
					{
						GenerateSunXML(request);
					}
					else if(option.equalsIgnoreCase("RE-APPROVE_OTH_INVOICE_SUN_XML"))
					{
						ReapproveInvoice(request);
					}
					else if(option.equalsIgnoreCase("SUN_XML"))
					{
						GenSunXMl(request);
					}
					
					else if(option.equalsIgnoreCase("RE_APP_SUN_XML"))
					{
						ReappSunXMl(request);
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
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
				if(stmt6 != null){try {stmt6.close();}catch(SQLException e){System.out.println("stmt6 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}
		try 
		{
			response.sendRedirect(url);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateSunEntityAccountCode(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSunEntityAccountCode()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String index_arr = request.getParameter("index")==null?"":request.getParameter("index");
			String [] vendor_cd = request.getParameterValues("vendor_cd");
			String [] sun_account = request.getParameterValues("sun_account");
			String [] index = index_arr.split(",");
			
			if(index!=null && vendor_cd!=null)
			{
				for(int i=0;i<index.length;i++)
				{
					int indx = Integer.parseInt(index[i]);
					if(!sun_account[i].equals(""))
					{
						String queryString="UPDATE FMS_OTH_ENTITY_MST "
								+ "SET ACCOUNT_CODE=? "
								+ "WHERE ENTITY_CD=? AND ENTITY_TYPE=? "
								+ "AND ACTIVE_FLAG='Y'";
						stmt1=dbcon.prepareStatement(queryString);
						stmt1.setString(1, sun_account[i]);
						stmt1.setString(2, vendor_cd[indx]);
						stmt1.setString(3, "V");
						stmt1.executeUpdate();
						stmt1.close();
					}
				}
				msg = "Successful! Insert/Update operation is done successfully for Other Entity Sun Account Code!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - Insert/Update Other Entity Sun Account Code failed!";
				msg_type="E";
			}
			
			url="../extn_interface/frm_oth_entity_sun_account_code.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Insert/Update failed for Other Entity Sun Account Code!";
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
	
	private void ApproveSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="ApproveSunXML()";
		try
		{
			String supp_cd = request.getParameter("supp_cd")==null?"":request.getParameter("supp_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String bu_seq = request.getParameter("bu_seq")==null?"":request.getParameter("bu_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");

			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
				int ctn=0;
				query ="UPDATE FMS_OTH_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND SUPPLIER_CD=? AND INVOICE_NO=? AND INV_FLAG=? ";
				if(invoice_type.equals("GA"))
				{
					query += "AND BU_UNIT=? AND BU_STATE_TIN=? ";
				}
				stmt = dbcon.prepareStatement(query);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, emp_cd);
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, financial_year);
				stmt.setString(++ctn, invoice_type);
				stmt.setString(++ctn, invoice_seq);
				stmt.setString(++ctn, supp_cd);
				stmt.setString(++ctn, invoice_no);
				stmt.setString(++ctn, inv_flag);
				if(invoice_type.equals("GA"))
				{
					stmt.setString(++ctn, bu_seq);
					stmt.setString(++ctn, bu_state_tin);
				}
				
				stmt.executeUpdate();
				
				stmt.close();
					
				msg = "Successful! - Sun Approval for Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Sun Approval for Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_oth_invoice_sun_acc_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Other Invoice Sun Approval failed! ";
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
	
	private void GenerateSunXML(HttpServletRequest request) throws SQLException
	{
		String function_nm="GenerateSunXML()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			if(!from_dt.equals("")&&!to_dt.equals(""))
			{
				DBsun.setCallFlag("GENERATE_OTH_INV_SUN_XML");
				DBsun.setFrom_dt(from_dt);
				DBsun.setTo_dt(to_dt);
				DBsun.setComp_cd(comp_cd);
				DBsun.setRequest(request);
				DBsun.setEmp_cd(emp_cd);
				DBsun.init();
				
				msg=DBsun.getMsg();
				msg_type=DBsun.getMsg_type();
			}
			else
			{
				msg = "Failed! - SUN XML for other Invoice generation failed!";
				msg_type="E";
			}
			url="../extn_interface/frm_oth_inv_sun_xml_gen.jsp?"+"&msg="+msg+"&msg_type="+msg_type+"&from_dt="+from_dt
					+"&to_dt="+to_dt+commonUrl_pra;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Generation of Other Invoice 	SUN XML failed! ";
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
	
	
	public void ReapproveInvoice(HttpServletRequest request) throws SQLException
	{
		String function_nm="ReapproveInvoice()";
		try
		{
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String segment = request.getParameter("segment")==null?"":request.getParameter("segment");
			String bu_seq = request.getParameter("bu_seq")==null?"":request.getParameter("bu_seq");
			String bu_state_tin = request.getParameter("bu_state_tin")==null?"":request.getParameter("bu_state_tin");
			String inv_flag = request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");
			String supp_cd = request.getParameter("supp_cd")==null?"":request.getParameter("supp_cd");

			if(!financial_year.equals("") && !invoice_seq.equals(""))
			{
				int del_count=0;
				query1="SELECT COUNT(PDF_TYPE) FROM FMS_OTH_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND INVOICE_SEQ=? "
						+ "AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND PDF_TYPE=? ";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, invoice_type);
				stmt1.setString(3, invoice_seq);
				stmt1.setString(4, financial_year);
				stmt1.setString(5, supp_cd);
				stmt1.setString(6, "S");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					del_count=rset1.getInt(1);
				}
				stmt1.close();
				
				if(del_count==1)
				{
					query2="DELETE FROM FMS_OTH_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND INVOICE_SEQ=? "
							+ "AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND PDF_TYPE=? ";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, invoice_type);
					stmt2.setString(3, invoice_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, supp_cd);
					stmt2.setString(6, "S");
					stmt2.executeUpdate();
					stmt2.close();
					
					int ctn=0;
					query ="UPDATE FMS_OTH_INVOICE_MST SET SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND INVOICE_SEQ=? AND SUPPLIER_CD=? AND INVOICE_NO=? AND INV_FLAG=? AND SAP_APPROVAL=? AND FIN_SYS=? ";
					if(invoice_type.equals("GA"))
					{
						query += "AND BU_UNIT=? AND BU_STATE_TIN=? ";
					}
					stmt = dbcon.prepareStatement(query);
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, financial_year);
					stmt.setString(++ctn, invoice_type);
					stmt.setString(++ctn, invoice_seq);
					stmt.setString(++ctn, supp_cd);
					stmt.setString(++ctn, invoice_no);
					stmt.setString(++ctn, inv_flag);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "S");
					if(invoice_type.equals("GA"))
					{
						stmt.setString(++ctn, bu_seq);
						stmt.setString(++ctn, bu_state_tin);
					}
					
					stmt.executeUpdate();
					stmt.close();
					
					msg="Re-approval for Invoice("+invoice_no+") Approved Successfully!";
					msg_type="S";
				}
				else
				{
					msg="No XML generated post Approval, Re-approval not applicable for Invoice("+invoice_no+")!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - Invoice("+invoice_no+") Re-approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_oth_invoice_sun_acc_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+
					"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Other Invoice Re-Approval failed! ";
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
	
	private void GenSunXMl(HttpServletRequest request) throws SQLException
	{
		String function_nm="GenSunXMl()";
		try
		{
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			
			if(!invoice_no.equals(""))
			{
				int ctn=0;
				query ="UPDATE FMS_OTH_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=?  ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, emp_cd);
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, invoice_no);
				stmt.executeUpdate();
				
				stmt.close();
					
				msg = "Successful! - Sun Approval for Invoice("+invoice_no+") Approved Successfully!";
				msg_type="S";
				
				
				DBsun.setCallFlag("SUN_XML_DATA");
				DBsun.setComp_cd(comp_cd);
				DBsun.setRequest(request);
				DBsun.setInvoice_no(invoice_no);
				DBsun.setEmp_cd(emp_cd);
				DBsun.init();
				
				msg=DBsun.getMsg();
				msg_type=DBsun.getMsg_type();
			}
			else
			{
				msg = "Failed! - Sun Approval for Invoice("+invoice_no+") Approval Failed!";
				msg_type="E";
			}
			url = "../extn_interface/frm_oth_inv_sun_xml_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&accroid="+accroid+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Other Invoice Sun Approval failed! ";
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
	
	private void ReappSunXMl(HttpServletRequest request) throws SQLException
	{
		String function_nm="ReappSunXMl()";
		try
		{
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String accroid = request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String inv_seq="",fin_yr="",supp_cd="",inv_type="";
			
			if(!invoice_no.equals(""))
			{
				String queryString="SELECT INVOICE_SEQ,FINANCIAL_YEAR,SUPPLIER_CD,INVOICE_TYPE FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt3=dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, invoice_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					inv_seq=rset3.getString(1)==null?"":rset3.getString(1);
					fin_yr=rset3.getString(2)==null?"":rset3.getString(2);
					supp_cd=rset3.getString(3)==null?"":rset3.getString(3);
					inv_type=rset3.getString(4)==null?"":rset3.getString(4);
				}
				rset3.close();
				stmt3.close();
				
				int del_count=0;
				query1="SELECT COUNT(PDF_TYPE) FROM FMS_OTH_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND INVOICE_SEQ=? "
						+ "AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND PDF_TYPE=? ";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, inv_seq);
				stmt1.setString(4, fin_yr);
				stmt1.setString(5, supp_cd);
				stmt1.setString(6, "S");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					del_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(del_count>0)
				{
					int ctn=0;
					query ="UPDATE FMS_OTH_INVOICE_MST SET SAP_APPROVAL=?,SAP_APPROVED_BY=?,SAP_APPROVED_DT=SYSDATE,FIN_SYS=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_NO=?  ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, "S");
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, invoice_no);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Sun RE-Approval for Invoice("+invoice_no+") Approved Successfully!";
					msg_type="S";
					
					DBsun.setCallFlag("SUN_XML_DATA");
					DBsun.setComp_cd(comp_cd);
					DBsun.setRequest(request);
					DBsun.setInvoice_no(invoice_no);
					DBsun.setEmp_cd(emp_cd);
					DBsun.init();
					
//					msg=DBsun.getMsg();
//					msg_type=DBsun.getMsg_type();
				}
				else
				{
					msg="No XML generated post Approval, Re-approval not applicable for Invoice("+invoice_no+")!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - Sun RE-Approval for Invoice("+invoice_no+") Failed!";
				msg_type="E";
			}
			
			url = "../extn_interface/frm_oth_inv_sun_xml_approval.jsp?msg="+msg+"&msg_type="+msg_type+"&accroid="+accroid+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Other Invoice Sun Approval failed! ";
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