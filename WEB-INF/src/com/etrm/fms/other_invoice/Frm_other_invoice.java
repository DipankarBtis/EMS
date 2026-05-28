package com.etrm.fms.other_invoice;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

//Coded By          : Deep Tank,Arth Patel
//Code Reviewed by	:  
//CR Date			: 07/10/2025
//Status	  		: Developing

@WebServlet("/servlet/Frm_other_invoice")
public class Frm_other_invoice extends HttpServlet
{
	public static String frm_src_file_name ="Frm_other_invoice.java";

	public static  Connection dbcon;
	
	public static String servletName = "Frm_other_invoice";
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
						
						if(option.equalsIgnoreCase("VENDOR_MST"))   //Deep20251006
						{
							InsertUpdateVendorDetail(request);
						}
						else if(option.equalsIgnoreCase("COST_RECHARGE"))  //Deep20251014
						{
							InsertUpdateCostRecharge(request);
						}
						else if(option.equalsIgnoreCase("COST_RECHARGE_APPROVAL"))  //Deep20251016
						{
							ChkApprCostRecharge(request);
						}
						else if(option.equalsIgnoreCase("COST_RECHARGE_HPPL")) //AP20251014
						{
							InsertUpdateCostRechargeHppl(request);
						}
						else if(option.equalsIgnoreCase("COST_RECHARGE_HPPL_APPROVAL"))  //AP20251016
						{
							ChkApprCostRechargeHPPL(request);
						}
						else if(option.equalsIgnoreCase("HPPL_SHIPPING_AGENT")) //AP20251104
						{
							InsertUpdateHpplShippingAgent(request);
						}
						else if(option.equalsIgnoreCase("HPPL_SHIPPING_AGENT_APPROVAL"))  //AP20251105
						{
							ChkApprCostRechargeHPPLShippingAgent(request);
						}
						else if(option.equalsIgnoreCase("HPPL_SEIPL")) //Deep20251106
						{
							InsertUpdateHpplSeipl(request);
						}
						else if(option.equalsIgnoreCase("HPPL_SEIPL_APPROVAL"))  //Deep20251107
						{
							ChkApprHPPLSEIPL(request);
						}
						else if(option.equalsIgnoreCase("NPR"))  //AP20251110
						{
							InsertUpdateNPR(request);
						}
						else if(option.equalsIgnoreCase("NPR_APPROVAL"))  //AP20251110
						{
							ChkApprNPR(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_HSA_CR_DR")) //AP20251117 //DT20260131
						{
							InsertUpdateCreditDebitDetail(request);
						}
						else if(option.equalsIgnoreCase("HSA_CRDR_APPROVAL")) //AP20251117 //DT20260131
						{
							ChkApprCreditDebitDtls(request);
						}
						else if(option.equalsIgnoreCase("AHPL_SHARE"))   //Deep20251114
						{
							InsertUpdateAhplShare(request);
						}
						else if(option.equalsIgnoreCase("AHPL_SHARE_APPROVAL"))   //Deep20251115
						{
							ChkApprAhplShare(request);
						}
						else if(option.equalsIgnoreCase("SCRAP_FIXED_ASSET"))  //Deep20251118
						{
							InsertUpdateScrapFixedAsset(request);
						}
						else if(option.equalsIgnoreCase("SFA_APPROVAL"))  //Deep20251120
						{
							ChkApprSFA(request);
						}
						else if(option.equalsIgnoreCase("GA_INVOICE"))  //Deep20251230
						{
							InsertUpdateGaInvoice(request);
						}
						else if(option.equalsIgnoreCase("GA_APPROVAL"))  //Deep20251231
						{
							ChkApprGA(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_AHPL_CR_DR")) //DT20260202
						{
							InsertUpdateAhplCrDr(request);
						}
						else if(option.equalsIgnoreCase("AHPL_CRDR_APPROVAL")) //DT20260203
						{
							ChkApprAhplCrDr(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_PFA_CR_DR")) //DT20260216
						{
							InsertUpdatePFACrDr(request);
						}
						else if(option.equalsIgnoreCase("PFA_CRDR_APPROVAL")) //DT20260220
						{
							ChkApprPFACrDr(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_COSTRH_CR_DR")) //ajay20260221
						{
							InsertUpdateCostRHCrDr(request);
						}
						else if(option.equalsIgnoreCase("COSTRH_CRDR_APPROVAL")) //ajay20260221
						{
							ChkApprCostRHCrDr(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_COSTR_CR_DR")) //ajay20260221
						{
							InsertUpdateCostRCrDr(request);
						}
						else if(option.equalsIgnoreCase("COSTR_CRDR_APPROVAL")) //ajay20260221
						{
							ChkApprCostRCrDr(request);
						}
						else if(option.equalsIgnoreCase("GENERATE_SCRAP_CR_DR")) //DT20260317
						{
							InsertUpdateScrapCrDr(request);
						}
						else if(option.equalsIgnoreCase("SFA_CRDR_APPROVAL")) //DT20260318
						{
							ChkApprSFACrDr(request);
						}
						else if(option.equalsIgnoreCase("RE_EXPORT_INV"))  //ajay20260319
						{
							InsertUpdateRXP(request);
						}
						else if(option.equalsIgnoreCase("RE_EXPORT_APPROVAL"))  //ajay20260321
						{
							ChkApprRXP(request);
						}
					}
					
					dbcon.close();
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
					msg="Error In Exception!";
				}
				finally
				{
					if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
					if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);}}
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
	
	
	//Deep20251006
	private void InsertUpdateVendorDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateVendorDetail()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			operation = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String entity_type = request.getParameter("entity_tpye")==null?"":request.getParameter("entity_tpye");
			vendor_cd = request.getParameter("vendor_cd")==null?"0":request.getParameter("vendor_cd");
			String name = request.getParameter("name")==null?"":request.getParameter("name");
			String abbr = request.getParameter("abbr")==null?"":request.getParameter("abbr");
			String service_no = request.getParameter("service_no")==null?"":request.getParameter("service_no");
			String service_dt = request.getParameter("service_dt")==null?"":request.getParameter("service_dt");
			String pan_no = request.getParameter("pan_no")==null?"":request.getParameter("pan_no");
			String pan_dt = request.getParameter("pan_dt")==null?"":request.getParameter("pan_dt");
			String notes = request.getParameter("notes")==null?"":request.getParameter("notes");
			String web_addr=request.getParameter("web_addr")==null?"Y":request.getParameter("web_addr");
			String active_flag=request.getParameter("active_flag")==null?"Y":request.getParameter("active_flag");
			String business_flag = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String gst_tin_no = request.getParameter("gst_tin_no")==null?"":request.getParameter("gst_tin_no");
			String gst_tin_dt = request.getParameter("gst_tin_dt")==null?"":request.getParameter("gst_tin_dt");
			String cst_tin_no = request.getParameter("cst_tin_no")==null?"":request.getParameter("cst_tin_no");
			String cst_tin_dt = request.getParameter("cst_tin_dt")==null?"":request.getParameter("cst_tin_dt");
			String tan_no = request.getParameter("tan_no")==null?"":request.getParameter("tan_no");
			String tan_dt = request.getParameter("tan_dt")==null?"":request.getParameter("tan_dt");
			String gstin_no = request.getParameter("gstin_no")==null?"":request.getParameter("gstin_no");
			String gstin_dt = request.getParameter("gstin_dt")==null?"":request.getParameter("gstin_dt");
			String payee_nm = request.getParameter("payee_nm")==null?"":request.getParameter("payee_nm");
			String payee_acc_no = request.getParameter("payee_acc_no")==null?"":request.getParameter("payee_acc_no");
			String ifsc = request.getParameter("ifsc")==null?"":request.getParameter("ifsc");
			
			String reg_eff_dt[] = request.getParameterValues("reg_eff_dt");
			String address_type[] = request.getParameterValues("address_type");
			String address[] = request.getParameterValues("address");
			String city[] = request.getParameterValues("city");
			String state[] = request.getParameterValues("state");
			String zone[] = request.getParameterValues("zone");
			String pin[] = request.getParameterValues("pin");
			String country[] = request.getParameterValues("country");
			String phone[] = request.getParameterValues("phone");
			String alt_phone[] = request.getParameterValues("alt_phone");
			String cell[] = request.getParameterValues("cell");
			String fax1[] = request.getParameterValues("fax1");
			String fax2[] = request.getParameterValues("fax2");
			String email[] = request.getParameterValues("email");
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			
			
			if(business_flag.equals("B"))
			{
				payee_nm = "";
				payee_acc_no = "";
				ifsc = "";
			}
			else 
			{
				gstin_no = "";
				gstin_dt = "";
			}
			
			name = escObj.replaceSingleQuotes(name);
			abbr = escObj.replaceSingleQuotes(abbr);
			notes = escObj.replaceSingleQuotes(notes);

			if(operation.equalsIgnoreCase("INSERT"))
			{
				String query = "SELECT MAX(ENTITY_CD) "
						+ "FROM FMS_OTH_ENTITY_MST WHERE ENTITY_TYPE = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, entity_type);
				rset = stmt.executeQuery();
				int ID = 1;
				if(rset.next())
				{
					ID = Integer.parseInt(rset.getString(1)==null?"0":rset.getString(1));
					ID = ID + 1;
				}
				else
				{
					ID = 1;
				}
				rset.close();
				stmt.close();
				vendor_cd = ID+"";
				
				old_value="";
				
				int cnt = 0;  
				query1 = "INSERT INTO FMS_OTH_ENTITY_MST (ENTITY_CD,ENTITY_TYPE,EFF_DT,ENTITY_NAME,ENTITY_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,"
						+ "CST_TIN_DT,PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,ADDL_NO,ADDL_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,ACTIVE_FLAG,"
						+ "BUSINESS_FLAG,PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,ENT_BY,ENT_DT,ENT_PROFILE) "
						+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),"
						+ "?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,SYSDATE,?)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++cnt, vendor_cd);
				stmt1.setString(++cnt, entity_type);
				stmt1.setString(++cnt, eff_dt);
				stmt1.setString(++cnt, name);
				stmt1.setString(++cnt, abbr);
				stmt1.setString(++cnt, gst_tin_no);
				stmt1.setString(++cnt, gst_tin_dt);
				stmt1.setString(++cnt, cst_tin_no);
				stmt1.setString(++cnt, cst_tin_dt);
				stmt1.setString(++cnt, pan_no);
				stmt1.setString(++cnt, pan_dt);
				stmt1.setString(++cnt, tan_no);
				stmt1.setString(++cnt, tan_dt);
				stmt1.setString(++cnt, service_no);
				stmt1.setString(++cnt, service_dt);
				stmt1.setString(++cnt, gstin_no);
				stmt1.setString(++cnt, gstin_dt);
				stmt1.setString(++cnt, web_addr);
				stmt1.setString(++cnt, notes);
				stmt1.setString(++cnt, active_flag);
				stmt1.setString(++cnt, business_flag);
				stmt1.setString(++cnt, payee_acc_no);
				stmt1.setString(++cnt, ifsc);
				stmt1.setString(++cnt, payee_nm);
				stmt1.setString(++cnt, emp_cd);
				stmt1.setString(++cnt, comp_cd);
		   		stmt1.executeUpdate();
		   		
		   		stmt1.close();
			}
			else
			{
				int count = 0;
				String queryString = "SELECT COUNT(*) "
						+ "FROM FMS_OTH_ENTITY_MST "
						+ "WHERE ENTITY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE = ?";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, vendor_cd);
				stmt.setString(2, eff_dt);
				stmt.setString(3, entity_type);
				rset = stmt.executeQuery();
	
		   		if(rset.next())
		   		{
		   			count = rset.getInt(1);
		   		}
		   		rset.close();
		   		stmt.close();
		   		
		   		if(count==0)
			   	{
		   			int cnt = 0;  
					query1 = "INSERT INTO FMS_OTH_ENTITY_MST (ENTITY_CD,ENTITY_TYPE,EFF_DT,ENTITY_NAME,ENTITY_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,"
							+ "CST_TIN_DT,PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,ADDL_NO,ADDL_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,ACTIVE_FLAG,"
							+ "BUSINESS_FLAG,PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,ENT_BY,ENT_DT,ENT_PROFILE) "
							+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),"
							+ "?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, entity_type);
					stmt1.setString(++cnt, eff_dt);
					stmt1.setString(++cnt, name);
					stmt1.setString(++cnt, abbr);
					stmt1.setString(++cnt, gst_tin_no);
					stmt1.setString(++cnt, gst_tin_dt);
					stmt1.setString(++cnt, cst_tin_no);
					stmt1.setString(++cnt, cst_tin_dt);
					stmt1.setString(++cnt, pan_no);
					stmt1.setString(++cnt, pan_dt);
					stmt1.setString(++cnt, tan_no);
					stmt1.setString(++cnt, tan_dt);
					stmt1.setString(++cnt, service_no);
					stmt1.setString(++cnt, service_dt);
					stmt1.setString(++cnt, gstin_no);
					stmt1.setString(++cnt, gstin_dt);
					stmt1.setString(++cnt, web_addr);
					stmt1.setString(++cnt, notes);
					stmt1.setString(++cnt, active_flag);
					stmt1.setString(++cnt, business_flag);
					stmt1.setString(++cnt, payee_acc_no);
					stmt1.setString(++cnt, ifsc);
					stmt1.setString(++cnt, payee_nm);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   	}
		   		else
		   		{		   			
		   			int upCnt=0;
		   			query1 = "UPDATE FMS_OTH_ENTITY_MST SET ENTITY_NAME = ?, ENTITY_ABBR = ?, GST_TIN_NO = ?, GST_TIN_DT = TO_DATE(?, 'DD/MM/YYYY'), "
		   					+ "CST_TIN_NO = ?, CST_TIN_DT = TO_DATE(?, 'DD/MM/YYYY'), PAN_NO = ?, PAN_ISSUE_DT = TO_DATE(?, 'DD/MM/YYYY'), TAN_NO = ?, "
		   					+ "TAN_ISSUE_DT = TO_DATE(?, 'DD/MM/YYYY'), ADDL_NO = ?, ADDL_ISSUE_DT = TO_DATE(?, 'DD/MM/YYYY'), GSTIN_NO = ?, "
		   					+ "GSTIN_DT = TO_DATE(?, 'DD/MM/YYYY'), WEB_ADDR = ?, NOTES = ?, ACTIVE_FLAG = ?, BUSINESS_FLAG = ?, PAYEE_ACCOUNT_NO = ?, "
		   					+ "IFSC = ?,PAYEE_NM = ?, MODIFY_BY = ?, MODIFY_DT = SYSDATE, MOD_PROFILE = ? "
		   					+ "WHERE ENTITY_CD = ? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE = ?"; 
		   			stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++upCnt, name);
					stmt1.setString(++upCnt, abbr);
					stmt1.setString(++upCnt, gst_tin_no);
					stmt1.setString(++upCnt, gst_tin_dt);
					stmt1.setString(++upCnt, cst_tin_no);
					stmt1.setString(++upCnt, cst_tin_dt);
					stmt1.setString(++upCnt, pan_no);
					stmt1.setString(++upCnt, pan_dt);
					stmt1.setString(++upCnt, tan_no);
					stmt1.setString(++upCnt, tan_dt);
					stmt1.setString(++upCnt, service_no);
					stmt1.setString(++upCnt, service_dt);
					stmt1.setString(++upCnt, gstin_no);
					stmt1.setString(++upCnt, gstin_dt);
					stmt1.setString(++upCnt, web_addr);
					stmt1.setString(++upCnt, notes);
					stmt1.setString(++upCnt, active_flag);
					stmt1.setString(++upCnt, business_flag);
					stmt1.setString(++upCnt, payee_acc_no);
					stmt1.setString(++upCnt, ifsc);
					stmt1.setString(++upCnt, payee_nm);
					stmt1.setString(++upCnt, emp_cd);
					stmt1.setString(++upCnt, comp_cd);
					stmt1.setString(++upCnt, vendor_cd);
					stmt1.setString(++upCnt, eff_dt);
					stmt1.setString(++upCnt, entity_type);
					stmt1.executeUpdate();
					
			   		stmt1.close();
		   		}	
			}
			
			if(business_flag.equals("C"))
			{
				new_value="VD="+vendor_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#GSTTIN="+gst_tin_no+"#GSTTINDT="+
						gst_tin_dt+"#CSTTIN="+cst_tin_no+"#CSTTINDT="+cst_tin_dt+"#TANNO="+tan_no+"#TANDT="+tan_dt+"#SERVICENO="+service_no+"#SERVICEDT="+service_dt+
						"#GSTIN="+gstin_no+"#GSTINDT="+gstin_dt+"#WEBADDR="+web_addr+"#NOTES="+notes+"#BUSINESSFLAG="+business_flag+"#PAYEEACC="+payee_acc_no+"#IFSC="+ifsc+
						"#PAYEENAME"+payee_nm;
//						+"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+
//						"#FAX2="+fax2+"#CELL="+cell+"#EMAIL="+email+"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt;
			}
			else 
			{
				new_value="VD="+vendor_cd+"#NAME="+name+"#ABBR="+abbr+"#PANNO="+pan_no+"#PANDT="+pan_dt+"#GSTTIN="+gst_tin_no+"#GSTTINDT="+
						gst_tin_dt+"#CSTTIN="+cst_tin_no+"#CSTTINDT="+cst_tin_dt+"#TANNO="+tan_no+"#TANDT="+tan_dt+"#SERVICENO="+service_no+"#SERVICEDT="+service_dt+
						"#GSTIN="+gstin_no+"#GSTINDT="+gstin_dt+"#WEBADDR="+web_addr+"#NOTES="+notes+"#BUSINESSFLAG="+business_flag;
//						+"#ADD="+address+"#CITY="+city+"#STATE="+state+"#ZONE="+zone+"#PIN="+pin+"#COUNTRY="+country+"#PH="+phone+"#FAX1="+fax1+"#FAX2="+fax2+"#CELL="+cell+"#EMAIL="+email+
//						"#ALTPH="+alt_phone+"#REFFDT="+reg_eff_dt;
			}
			
			for (int j = 0; j < reg_eff_dt.length; j++) 
			{
				String query = "SELECT COUNT(*) "
						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
						+ "WHERE ENTITY_CD = ? AND ADDRESS_TYPE=? "
						+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, vendor_cd);
				stmt.setString(2, address_type[j]);
				stmt.setString(3, reg_eff_dt[j]);
				stmt.setString(4, entity_type);
				rset = stmt.executeQuery();
				int count = 0;
				if(rset.next())
			    {
					count = rset.getInt(1);
			    }
				rset.close();
				stmt.close();
				
				if(count>0)
		    	{
					int upCnt=0;
				    query1 = "UPDATE FMS_OTH_ENTITY_ADDR_MST "
				    		+ "SET ADDR=?, CITY=?, PIN=?, STATE=?, ZONE=?,"
				    		+ "COUNTRY=?, PHONE=?, MOBILE=?, ALT_PHONE=?, FAX_1=?, FAX_2=?,"
				    		+ "EMAIL=?, MODIFY_DT=SYSDATE, MODIFY_BY=?, MOD_PROFILE = ? "
				    		+ "WHERE ENTITY_CD = ? AND ADDRESS_TYPE=? "
				    		+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY') AND ENTITY_TYPE = ?";
				    stmt1=dbcon.prepareStatement(query1);
				    stmt1.setString(++upCnt, address[j]);
				    stmt1.setString(++upCnt, city[j]);
				    stmt1.setString(++upCnt, pin[j]);
				    stmt1.setString(++upCnt, state[j]);
				    stmt1.setString(++upCnt, zone[j]);
				    stmt1.setString(++upCnt, country[j]);
				    stmt1.setString(++upCnt, phone[j]);
				    stmt1.setString(++upCnt, cell[j]);
				    stmt1.setString(++upCnt, alt_phone[j]);
				    stmt1.setString(++upCnt, fax1[j]);
				    stmt1.setString(++upCnt, fax2[j]);
				    stmt1.setString(++upCnt, email[j]);
				    stmt1.setString(++upCnt, emp_cd);
				    stmt1.setString(++upCnt, comp_cd);
				    stmt1.setString(++upCnt, vendor_cd);
				    stmt1.setString(++upCnt, address_type[j]);
				    stmt1.setString(++upCnt, reg_eff_dt[j]);
				    stmt1.setString(++upCnt, entity_type);
				    stmt1.executeUpdate();
				    
				    stmt1.close();
			    }
				else
				{
					int cnt = 0;
					query1="INSERT INTO FMS_OTH_ENTITY_ADDR_MST(ENTITY_CD,ENTITY_TYPE,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,"
							+ "STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,ENT_PROFILE) "
							+ "VALUES(?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,SYSDATE,?) "; 
					 stmt1=dbcon.prepareStatement(query1);
					 stmt1.setString(++cnt, vendor_cd);
					 stmt1.setString(++cnt, entity_type);
					 stmt1.setString(++cnt, reg_eff_dt[j]);
				     stmt1.setString(++cnt, address_type[j]);
				     stmt1.setString(++cnt, address[j]);
				     stmt1.setString(++cnt, city[j]);
				     stmt1.setString(++cnt, pin[j]);
				     stmt1.setString(++cnt, state[j]);
				     stmt1.setString(++cnt, zone[j]);
				     stmt1.setString(++cnt, country[j]);
				     stmt1.setString(++cnt, phone[j]);
				     stmt1.setString(++cnt, cell[j]);
				     stmt1.setString(++cnt, alt_phone[j]);
				     stmt1.setString(++cnt, fax1[j]);
				     stmt1.setString(++cnt, fax2[j]);
				     stmt1.setString(++cnt, email[j]);
				     stmt1.setString(++cnt, emp_cd);
				     stmt1.setString(++cnt, comp_cd);
				     stmt1.executeUpdate();
					    
					 stmt1.close();
				}
			}
			
			if(operation.equalsIgnoreCase("INSERT"))
			{
				operation="MODIFY";
				msg = "Successful! - Vendor "+name+" Added!";
				msg_type="S";
			}
			else
			{
				msg = "Successful! - Vendor "+name+" updated!";
				msg_type="S";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_vendor_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&opration="+operation+"&entity_role="+entity_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Vendor Addition/Modification Failed!";			
			url=CommonVariable.errorpage_url+"?e="+e;
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
	//Deep20251006
	
	//Deep20251014
	private void InsertUpdateCostRecharge(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCostRecharge()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String exchng_rate = request.getParameter("exchng_rate") == null ? "" : request.getParameter("exchng_rate");
			String exchng_eff_dt = request.getParameter("exchng_eff_dt") == null ? "" : request.getParameter("exchng_eff_dt");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String sale_unit = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String tax_inr = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String net_amt = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String pacer_no = request.getParameter("pacer_no") == null ? "" : request.getParameter("pacer_no");
			String vendor_inv_ref_no = request.getParameter("vendor_inv_ref") == null ? "" : request.getParameter("vendor_inv_ref");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String sale_price = "",exchng_cd="";
			String tax_cd = "I";
			
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			boolean isOperated=false,isOperated1=false;
			int count=0;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		
			   		msg = "Successful! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
//			   		msg = "Successful! - Cost Recharge Invoice with Invoice No. "+inv_no+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					//msg = "Failed! - Cost Recharge Invoice with Invoice No. "+inv_no+" Already Generated!";
					msg_type="E";
				}
		   		
				if(isOperated)
				{
					int cnt=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
			   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,PACER_NO,VENDOR_SUPP_INV_REF,"
			   				+ "ENT_BY,ENT_DT,INV_FLAG) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
			   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,"
			   				+ "?,SYSDATE,?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, pacer_no);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, SALE_AMT=?, EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,INVOICE_CATEGORY=?,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = ? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
					isOperated1=true;

					stmt1.close();

					msg = "Successful! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
//					msg = "Successful! - Cost Recharge Invoice with Invoice No. "+inv_no+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
//					msg = "Failed! - Cost Recharge Invoice with Invoice No. "+inv_no+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated1)
				{
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_DTL SET SEQ_NO=?, ITEM_DESCRIPTION=?, SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SAC_CODE=?,TAX_CD=?,PACER_NO=?,"
							+ "VENDOR_SUPP_INV_REF=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,FINANCIAL_YEAR=?,INVOICE_SEQ=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = ? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, pacer_no);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			
			if(operation.equals("INSERT")) 
			{
				operation="MODIFY";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_oth_inv_cost_recharge.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - RCM Invoice(Cost Recharge) Addition/Modification Failed!";			
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
	
	private void ChkApprCostRecharge(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCostRecharge()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
//				msg = "Successful! - Cost Recharge Invoice with Invoice No. "+inv_no+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND INVOICE_ID_SEQ=? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
				
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
//					msg = "Successful! - Cost Recharge Invoice with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - RCM Invoice(Cost Recharge) Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_costr_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - RCM Invoice(Cost Recharge) Check/Approve Failed!";			
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
	
	private void InsertUpdateCostRechargeHppl(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCostRechargeHppl()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String exchng_rate = request.getParameter("exchng_rate") == null ? "" : request.getParameter("exchng_rate");
			String exchng_eff_dt = request.getParameter("exchng_eff_dt") == null ? "" : request.getParameter("exchng_eff_dt");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_inr = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String total_amt_inr = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String tax_inr = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String inv_cur_cd = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String vendor_inv_ref_no = request.getParameter("vendor_inv_ref") == null ? "" : request.getParameter("vendor_inv_ref");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String purchase_no = request.getParameter("purchase_no") == null ? "" : request.getParameter("purchase_no");
			String ref_no = request.getParameter("ref_no") == null ? "" : request.getParameter("ref_no");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String tax_cd = "I";
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}

			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			if (inv_cur_cd.equals("1")) 
			{
				exchng_rate = "";
				exchng_eff_dt = "";
			}
			boolean isOperated=false;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				int count=0;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,PERIOD_START_DT,PERIOD_END_DT, "
							+ "EXCHG_RATE_DT, EXCHG_RATE_VALUE, GROSS_AMT, TAX_AMT_INR,NET_PAYABLE,INVOICE_RAISED_IN, SALE_PRICE_UNIT, "
							+ "ENT_BY, ENT_DT,REMARK,REMARK1,SALE_AMT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?, ?, ?,?,?,?,?, TO_DATE(?, 'DD/MM/YYYY'), TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),"
							+ "TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?,?, "
							+ "?, SYSDATE,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setInt(++cnt, inv_seq1);
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_inr);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, "F");
					
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		operation="MODIFY";
			   		
					msg = "Successful! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
//			   		msg = "Successful! - Cost Recharge HPPL Invoice with Invoice No. "+inv_no+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
//					msg = "Failed! - Cost Recharge HPPL Invoice with Invoice No. "+inv_no+" Already Generated!";
					msg_type="E";
				}
		   		
				
				if(isOperated)
				{
					int cnt=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,VENDOR_SUPP_INV_REF,"
			   				+ "ENT_BY,ENT_DT,PURCHASE_NO,REFERENCE_NO,INV_FLAG) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
			   				+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?) ";
			   		
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, purchase_no);
					stmt1.setString(++cnt, ref_no);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),INVOICE_CATEGORY=?, "
							+ "SALE_PRICE_UNIT=?, SALE_AMT=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
							//+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_inr);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					//stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					//stmt1.setString(++cnt, period_start);
					//stmt1.setString(++cnt, period_end);
					stmt1.executeUpdate();
					isOperated=true;
					
					msg = "Successful! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
//					msg = "Successful! - Cost Recharge HPPL Invoice with Invoice No. "+inv_no+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
//					msg = "Failed! - Cost Recharge HPPL Invoice with Invoice No. "+inv_no+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated)
				{
					int cnt=0;
					
					query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "SALE_PRICE_UNIT=?, ITEM_DESCRIPTION=?,TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SAC_CODE=?,PURCHASE_NO=?,"
							+ "REFERENCE_NO=?,TAX_CD=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,FINANCIAL_YEAR=?,INVOICE_SEQ=? "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, purchase_no);
					stmt1.setString(++cnt, ref_no);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, comp_cd);
					//stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.executeUpdate();
					
			   		stmt1.close();
				}
			}
			
			dbcon.commit();
			url = "../other_invoice/frm_other_invoice_cost_recharge_hppl.jsp?msg="+msg+"&msg_type="+msg_type+"&vend_cd="+vend_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Cost Recharge HPPL Addition/Modification Failed!";			
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
	
	private void ChkApprCostRechargeHPPL(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCostRechargeHPPL()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND INVOICE_ID_SEQ=? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
				
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
//					msg = "Successful! - Cost Recharge HPPL Invoice with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Cost Recharge HPPL Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../other_invoice/rpt_costr_hppl_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Cost Recharge HPPL Check/Approval Failed!";			
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
	
	private void InsertUpdateHpplShippingAgent(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHpplShippingAgent()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String importer_nm = request.getParameter("importer_nm") == null ? "" : request.getParameter("importer_nm");
			String vessel_agent_nm = request.getParameter("vessel_agent_nm") == null ? "" : request.getParameter("vessel_agent_nm");
			String berthing_hours = request.getParameter("berthing_hours") == null ? "" : request.getParameter("berthing_hours");
			String berthing_slots_no = request.getParameter("berthing_slots_no") == null ? "" : request.getParameter("berthing_slots_no");
			String vessel_cd = request.getParameter("vessel_cd") == null ? "" : request.getParameter("vessel_cd");
			String vessel_nm = request.getParameter("vessel_nm") == null ? "" : request.getParameter("vessel_nm");
			String vessel_flag = request.getParameter("vessel_flag") == null ? "" : request.getParameter("vessel_flag");
			String vessel_item = request.getParameter("vessel_item") == null ? "" : request.getParameter("vessel_item");
			String exchng_rate = request.getParameter("exchng_rate") == null ? "" : request.getParameter("exchng_rate");
			String exchng_eff_dt = request.getParameter("exchng_eff_dt") == null ? "" : request.getParameter("exchng_eff_dt");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String inv_due_dt = request.getParameter("inv_due_dt") == null ? "" : request.getParameter("inv_due_dt");
			String inv_qty = request.getParameter("inv_qty") == null ? "" : request.getParameter("inv_qty");
			String grt = request.getParameter("grt") == null ? "" : request.getParameter("grt");
			String rate = request.getParameter("rate") == null ? "" : request.getParameter("rate");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_usd = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String total_amt_usd = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String tax_usd = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String inv_cur_cd = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String vendor_inv_ref_no = request.getParameter("vendor_inv_ref") == null ? "" : request.getParameter("vendor_inv_ref");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String remark3 = request.getParameter("remark3") == null ? "" : request.getParameter("remark3");
			String remark4 = request.getParameter("remark4") == null ? "" : request.getParameter("remark4");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String tax_cd = "I";
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}

			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			if (inv_cur_cd.equals("2")) 
			{
				exchng_rate = "";
				exchng_eff_dt = "";
			}
			boolean isOperated=false;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{	
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				int count=0;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,DUE_DT,PERIOD_START_DT,PERIOD_END_DT, "
							+ "EXCHG_RATE_DT, EXCHG_RATE_VALUE, GROSS_AMT, TAX_AMT_INR,NET_PAYABLE,INVOICE_RAISED_IN, SALE_PRICE_UNIT, "
							+ "ENT_BY, ENT_DT,REMARK,REMARK1,REMARK2,REMARK3,SALE_AMT,INVOICE_CATEGORY,SALE_PRICE,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?, ?, ?,?,?,?,?, TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'), TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),"
							+ "TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?,?, "
							+ "?, SYSDATE,?,?,?,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setInt(++cnt, inv_seq1);
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, inv_due_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_usd);
					stmt1.setString(++cnt, tax_usd);
					stmt1.setString(++cnt, total_amt_usd);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, remark4);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
			   		stmt1.executeUpdate();
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		operation="MODIFY";
			   		
					msg = "Successful! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				
				if(isOperated)
				{
					int cnt=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,VENDOR_SUPP_INV_REF,"
			   				+ "ENT_BY,ENT_DT,SALE_PRICE,VESSEL_CD,VESSEL_AGENT,VESSEL_FLAG,GRT,IMPORTER,QUANTITY,HRS_BERTHING,TIME_SLOTS_BERTHING,INV_FLAG) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
			   				+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?,?,?,?,?,?,?,?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, vessel_cd);
					stmt1.setString(++cnt, vessel_agent_nm);
					stmt1.setString(++cnt, vessel_flag);
					stmt1.setString(++cnt, grt);
					stmt1.setString(++cnt, importer_nm);
					stmt1.setString(++cnt, inv_qty);
					stmt1.setString(++cnt, berthing_hours);
					stmt1.setString(++cnt, berthing_slots_no);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'),INVOICE_CATEGORY=?, "
							+ "SALE_PRICE=?,SALE_PRICE_UNIT=?, SALE_AMT=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, REMARK2=?, REMARK3=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
							//+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, inv_due_dt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_usd);
					stmt1.setString(++cnt, tax_usd);
					stmt1.setString(++cnt, total_amt_usd);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, remark4);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					//stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					//stmt1.setString(++cnt, period_start);
					//stmt1.setString(++cnt, period_end);
					stmt1.executeUpdate();
					isOperated=true;
					
					msg = "Successful! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated)
				{
					int cnt=0;
					
					query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, ITEM_DESCRIPTION=?,TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),SAC_CODE=?,"
							+ "VESSEL_CD=?,VESSEL_AGENT=?,VESSEL_FLAG=?,GRT=?,IMPORTER=?,QUANTITY=?,HRS_BERTHING=?,TIME_SLOTS_BERTHING=?,"
							+ "TAX_CD=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,FINANCIAL_YEAR=?,INVOICE_SEQ=? "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, vessel_cd);
					stmt1.setString(++cnt, vessel_agent_nm);
					stmt1.setString(++cnt, vessel_flag);
					stmt1.setString(++cnt, grt);
					stmt1.setString(++cnt, importer_nm);
					stmt1.setString(++cnt, inv_qty);
					stmt1.setString(++cnt, berthing_hours);
					stmt1.setString(++cnt, berthing_slots_no);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, comp_cd);
					//stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.executeUpdate();
					
			   		stmt1.close();
				}
			}
			
			dbcon.commit();
			url = "../other_invoice/frm_oth_inv_shipping_agent.jsp?msg="+msg+"&msg_type="+msg_type+"&vend_cd="+vend_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Berthing Invoice(HPPL Shipping Agent) Addition/Modification Failed!";			
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
	
	private void ChkApprCostRechargeHPPLShippingAgent(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCostRechargeHPPLShippingAgent()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND INVOICE_ID_SEQ=? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
				
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Berthing Invoice(HPPL Shipping Agent) Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
			}
			
			url = "../other_invoice/rpt_hsa_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Berthing Invoice(HPPL Shipping Agent) Check/Approval Failed!";			
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
	
	private void InsertUpdateHpplSeipl(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHpplSeipl()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String exchng_rate = request.getParameter("exchng_rate") == null ? "" : request.getParameter("exchng_rate");
			String exchng_eff_dt = request.getParameter("exchng_eff_dt") == null ? "" : request.getParameter("exchng_eff_dt");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String due_dt = request.getParameter("due_dt") == null ? "" : request.getParameter("due_dt");
			String rate = request.getParameter("rate") == null ? "" : request.getParameter("rate");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String total_amt_inr = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String tax_amt = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String inv_cur_cd = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String remark3 = request.getParameter("remark3") == null ? "" : request.getParameter("remark3");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String exchng_cd = request.getParameter("exchng_nm") == null ? "" : request.getParameter("exchng_nm");
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			
			String discharge_port[] = request.getParameterValues("dis_port");
			String cargo_ref[] = request.getParameterValues("cargo_ref");
			String cargo_dt[] = request.getParameterValues("cargo_dt");
			String cargo_type = request.getParameter("cargo_type") == null ? "" : request.getParameter("cargo_type");
			String ship_cd[] = request.getParameterValues("ship_cd");
			String qty_mmbtu[] = request.getParameterValues("qty_mmbtu");
			String cargo_rate[] = request.getParameterValues("cargo_rate");
			String cargo_amt[] = request.getParameterValues("cargo_amt");
			
			String tax_cd = "I";
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}

			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			boolean isOperated=false;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{		
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				int count=0;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "DUE_DT,PERIOD_START_DT,PERIOD_END_DT,EXCHG_RATE_CD, EXCHG_RATE_DT, EXCHG_RATE_VALUE, GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
							+ "INVOICE_RAISED_IN, SALE_PRICE_UNIT,ENT_BY,ENT_DT,REMARK,REMARK1,REMARK2,SALE_AMT,INVOICE_CATEGORY,SALE_PRICE,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),"
							+ "TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,"
							+ " ?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setInt(++cnt, inv_seq1);
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, due_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, "F");
			   		stmt1.executeUpdate();
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		operation="MODIFY";
			   		
					msg = "Successful! - PFA Fees Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - PFA Fees Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				
				if(isOperated)
				{
					int seq_no=1;
					for (int j = 0; j < cargo_rate.length; j++) 
					{
						if (!cargo_rate[j].equals("0.0000") && !cargo_rate[j].equals("")) 
						{
							int cnt = 0;
							
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,REFERENCE_NO,VESSEL_CD,QUANTITY,CARGO_DT,"
									+ "CARGO_AMOUNT,CARGO_TYPE,DISCHARGE_PORT,ENT_BY,ENT_DT,INV_FLAG)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY')," 
									+ "?,?,?,?,SYSDATE,?) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, inv_seq1 + "");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, cargo_rate[j]);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, cargo_ref[j]);
							stmt1.setString(++cnt, ship_cd[j]);
							stmt1.setString(++cnt, qty_mmbtu[j]);
							stmt1.setString(++cnt, cargo_dt[j]);
							stmt1.setString(++cnt, cargo_amt[j]);
							stmt1.setString(++cnt, cargo_type);
							stmt1.setString(++cnt, discharge_port[j]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}	
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'),INVOICE_CATEGORY=?, "
							+ "SALE_PRICE=?,SALE_PRICE_UNIT=?, SALE_AMT=?, EXCHG_RATE_CD = ?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, REMARK2=?, VENDOR_TYPE=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, due_dt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.executeUpdate();
					isOperated=true;
					
					msg = "Successful! - PFA Fees Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - PFA Fees Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated)
				{
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG = 'F'";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, inv_seq);
					stmt2.executeUpdate();
					
					stmt2.close();
					
					
					int seq_no=1;
					for (int j = 0; j < cargo_rate.length; j++) 
					{
						if (!cargo_rate[j].equals("0.0000") && !cargo_rate[j].equals("")) 
						{
							int cnt = 0;
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,REFERENCE_NO,VESSEL_CD,QUANTITY,CARGO_DT,"
									+ "CARGO_AMOUNT,CARGO_TYPE,DISCHARGE_PORT,ENT_BY,ENT_DT,INV_FLAG)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY')," 
									+ "?,?,?,?,SYSDATE,?) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, cargo_rate[j]);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, cargo_ref[j]);
							stmt1.setString(++cnt, ship_cd[j]);
							stmt1.setString(++cnt, qty_mmbtu[j]);
							stmt1.setString(++cnt, cargo_dt[j]);
							stmt1.setString(++cnt, cargo_amt[j]);
							stmt1.setString(++cnt, cargo_type);
							stmt1.setString(++cnt, discharge_port[j]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
				}
			}
			
			dbcon.commit();
			url = "../other_invoice/frm_oth_inv_hppl_seipl.jsp?msg="+msg+"&msg_type="+msg_type+"&vend_cd="+vend_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - PFA Fees Invoice(HPPL-SEIPL) Addition/Modification Failed!";			
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
	
	private void ChkApprHPPLSEIPL(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprHPPLSEIPL()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - PFA Fees Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND INVOICE_ID_SEQ=? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
				
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? " 
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - PFA Fees Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - PFA Fees Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_costr_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Cost Recharge Check/Approve Failed!";			
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
	
	//AP20251110
	private void InsertUpdateNPR(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateNPR()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String exchng_rate = request.getParameter("exchng_rate") == null ? "" : request.getParameter("exchng_rate");
			String exchng_eff_dt = request.getParameter("exchng_eff_dt") == null ? "" : request.getParameter("exchng_eff_dt");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String sale_unit = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String tax_inr = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String net_amt = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String pacer_no = request.getParameter("pacer_no") == null ? "" : request.getParameter("pacer_no");
			String vendor_inv_ref_no = request.getParameter("vendor_inv_ref") == null ? "" : request.getParameter("vendor_inv_ref");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");

			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);

			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			String sale_price = "",exchng_cd="";
			String tax_cd = "I";
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			boolean isOperated=false,isOperated1=false;
			int count=0;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		
			   		msg = "Successful! - NPR Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - NPR Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				if(isOperated)
				{
					int cnt=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
			   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,PACER_NO,VENDOR_SUPP_INV_REF,"
			   				+ "ENT_BY,ENT_DT,INV_FLAG) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
			   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,"
			   				+ "?,SYSDATE,?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, pacer_no);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, "F");
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, SALE_AMT=?, EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,INVOICE_CATEGORY=?,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_eff_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					//stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					//stmt1.setString(++cnt, period_start);
					//stmt1.setString(++cnt, period_end);
					stmt1.executeUpdate();
					isOperated1=true;

					stmt1.close();

					msg = "Successful! - NPR Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - NPR Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated1)
				{
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_DTL SET SEQ_NO=?, ITEM_DESCRIPTION=?, SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, TAX_STRUCT_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SAC_CODE=?, "
							+ "TAX_CD=?, PACER_NO=?, VENDOR_SUPP_INV_REF=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?  "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ?  AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, "1");
					stmt1.setString(++cnt, desc_item);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, tax_struct_cd);
					stmt1.setString(++cnt, tax_struct_dt);
					stmt1.setString(++cnt, sac_cd);
					stmt1.setString(++cnt, tax_cd);
					stmt1.setString(++cnt, pacer_no);
					stmt1.setString(++cnt, vendor_inv_ref_no);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			
			if(operation.equals("INSERT")) 
			{
				operation="MODIFY";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_oth_inv_npr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - NPR Addition/Modification Failed!";			
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
	
	private void ChkApprNPR(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprNPR()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - NPR Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND INVOICE_ID_SEQ=? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
				
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = 'F'";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - NPR Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - NPR Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_npr_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - NPR Check/Approve Failed!";			
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
	
	private void InsertUpdateAhplShare(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAhplShare()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String desc_item = request.getParameter("desc_item") == null ? "" : request.getParameter("desc_item");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String due_dt = request.getParameter("due_dt") == null ? "" : request.getParameter("due_dt");
			String rate = request.getParameter("rate") == null ? "" : request.getParameter("rate");
			String sale_amt = request.getParameter("amount") == null ? "" : request.getParameter("amount");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String total_amt_inr = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String tax_amt = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String inv_cur_cd = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String remark2 = request.getParameter("remark2") == null ? "" : request.getParameter("remark2");
			String remark3 = request.getParameter("remark3") == null ? "" : request.getParameter("remark3");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String gross_revenue = request.getParameter("gross_revenue") == null ? "" : request.getParameter("gross_revenue");
			String gross_less = request.getParameter("gross_less") == null ? "" : request.getParameter("gross_less");
			
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String gross_sign[] = request.getParameterValues("gross_sign");
			String gross_amount[] = request.getParameterValues("gross_amount");
			String des[] = request.getParameterValues("des");
			String less_sign[] = request.getParameterValues("less_sign");
			String less_amount[] = request.getParameterValues("less_amount");
			String less_des[] = request.getParameterValues("less_des");
			
			String tax_cd = "C";
			

			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			boolean isOperated=false;
			int inv_seq1 = 0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				int count=0;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? "
							+ "AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "DUE_DT,PERIOD_START_DT,PERIOD_END_DT,GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
							+ "INVOICE_RAISED_IN, SALE_PRICE_UNIT,ENT_BY,ENT_DT,REMARK,REMARK1,REMARK2,SALE_AMT,INVOICE_CATEGORY,SALE_PRICE,VENDOR_TYPE,INV_FLAG) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),"
							+ "TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),TO_DATE(?, 'DD/MM/YYYY'),?,?,?,"
							+ " ?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setInt(++cnt, inv_seq1);
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, due_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
			   		stmt1.executeUpdate();
			   		stmt1.close();
			   		
			   		isOperated=true;
			   		operation="MODIFY";
			   		
					msg = "Successful! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				
				if(isOperated)
				{
					int seq_no=1;
					if(gross_sign!=null)
					{
						for (int j = 0; j < gross_sign.length; j++) 
						{
							int cnt = 0;
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,INV_FLAG,"
									+ "ENT_BY,ENT_DT)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, inv_seq1 + "");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, gross_revenue);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, gross_sign[j]);
							stmt1.setString(++cnt, des[j]);
							stmt1.setString(++cnt, gross_amount[j]);
							stmt1.setString(++cnt, "G");
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, emp_cd);
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else 
					{
						int cnt = 0;
						query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
								+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_FLAG,INV_FLAG,ENT_BY,ENT_DT)"
								+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE) ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, seq_no++);
						stmt1.setString(++cnt, inv_seq1 + "");
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item);
						stmt1.setString(++cnt, gross_revenue);
						stmt1.setString(++cnt, inv_cur_cd);
						stmt1.setString(++cnt, tax_struct_cd);
						stmt1.setString(++cnt, tax_struct_dt);
						stmt1.setString(++cnt, fin_yr);
						stmt1.setString(++cnt, sac_cd);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, "G");
						stmt1.setString(++cnt, "F");
						stmt1.setString(++cnt, emp_cd);
						stmt1.executeUpdate();
						stmt1.close();
					}
					
					
					if(less_sign!=null)
					{
						for (int j = 0; j < less_sign.length; j++) 
						{
							int cnt = 0;
							
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,INV_FLAG,"
									+ "ENT_BY,ENT_DT)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, inv_seq1 + "");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, gross_less);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, less_sign[j]);
							stmt1.setString(++cnt, less_des[j]);
							stmt1.setString(++cnt, less_amount[j]);
							stmt1.setString(++cnt, "L");
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, emp_cd);
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else 
					{
						int cnt = 0;
						query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
								+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_FLAG,INV_FLAG,ENT_BY,ENT_DT)"
								+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE) ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, seq_no++);
						stmt1.setString(++cnt, inv_seq1 + "");
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item);
						stmt1.setString(++cnt, gross_less);
						stmt1.setString(++cnt, inv_cur_cd);
						stmt1.setString(++cnt, tax_struct_cd);
						stmt1.setString(++cnt, tax_struct_dt);
						stmt1.setString(++cnt, fin_yr);
						stmt1.setString(++cnt, sac_cd);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, "L");
						stmt1.setString(++cnt, "F");
						stmt1.setString(++cnt, emp_cd);
						stmt1.executeUpdate();
						stmt1.close();
					}
					
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? "
						+ "AND INVOICE_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				stmt.setString(7, "F");
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'),INVOICE_CATEGORY=?, "
							+ "SALE_PRICE=?,SALE_PRICE_UNIT=?, SALE_AMT=?, GROSS_AMT=?, TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, "
							+ "REMARK=?, REMARK1=?, REMARK2=?, VENDOR_TYPE=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,FINANCIAL_YEAR=?,INVOICE_SEQ=?,"
							+ "PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  "
							+ "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INV_FLAG = ? "
							+ "AND SUPPLIER_CD = ? AND VENDOR_CD = ?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, due_dt);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, inv_cur_cd);
					stmt1.setString(++cnt, sale_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, total_amt_inr);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, remark3);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.executeUpdate();
					isOperated=true;
					
					msg = "Successful! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated)
				{
					
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG = ?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, inv_seq);
					stmt2.setString(6, "F");
					stmt2.executeUpdate();
					
					stmt2.close();

					int seq_no=1;
					if(gross_sign!=null)
					{
						for (int j = 0; j < gross_sign.length; j++) 
						{
							int cnt = 0;
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,INV_FLAG,"
									+ "ENT_BY,ENT_DT)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, gross_revenue);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, gross_sign[j]);
							stmt1.setString(++cnt, des[j]);
							stmt1.setString(++cnt, gross_amount[j]);
							stmt1.setString(++cnt, "G");
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, emp_cd);
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else 
					{
						int cnt = 0;
						query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
								+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_FLAG,INV_FLAG,ENT_BY,ENT_DT)"
								+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE) ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, seq_no++);
						stmt1.setString(++cnt, new_invoice_seq);
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item);
						stmt1.setString(++cnt, gross_revenue);
						stmt1.setString(++cnt, inv_cur_cd);
						stmt1.setString(++cnt, tax_struct_cd);
						stmt1.setString(++cnt, tax_struct_dt);
						stmt1.setString(++cnt, new_financial_year);
						stmt1.setString(++cnt, sac_cd);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, "G");
						stmt1.setString(++cnt, "F");
						stmt1.setString(++cnt, emp_cd);
						stmt1.executeUpdate();
						stmt1.close();
					}
					
					
					if(less_sign!=null)
					{
						for (int j = 0; j < less_sign.length; j++) 
						{
							int cnt = 0;
							
							query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
									+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,INV_FLAG,"
									+ "ENT_BY,ENT_DT)"
									+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
									+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, seq_no++);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item);
							stmt1.setString(++cnt, gross_less);
							stmt1.setString(++cnt, inv_cur_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_cd);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, less_sign[j]);
							stmt1.setString(++cnt, less_des[j]);
							stmt1.setString(++cnt, less_amount[j]);
							stmt1.setString(++cnt, "L");
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, emp_cd);
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else 
					{
						int cnt = 0;
						query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
								+ "CARGO_AMOUNT,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_FLAG,INV_FLAG,ENT_BY,ENT_DT)"
								+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
								+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,SYSDATE) ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, seq_no++);
						stmt1.setString(++cnt, new_invoice_seq);
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item);
						stmt1.setString(++cnt, gross_less);
						stmt1.setString(++cnt, inv_cur_cd);
						stmt1.setString(++cnt, tax_struct_cd);
						stmt1.setString(++cnt, tax_struct_dt);
						stmt1.setString(++cnt, new_financial_year);
						stmt1.setString(++cnt, sac_cd);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, "L");
						stmt1.setString(++cnt, "F");
						stmt1.setString(++cnt, emp_cd);
						stmt1.executeUpdate();
						stmt1.close();
					}
				}
			}
			
			dbcon.commit();
			url = "../other_invoice/frm_oth_inv_ahpl_share.jsp?msg="+msg+"&msg_type="+msg_type+"&vend_cd="+vend_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - AHPL Invoice (AHPL Revenue Share) Addition/Modification Failed!";			
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
	
	private void ChkApprAhplShare(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprAhplShare()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, "F");
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, "F");
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, "F");
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, "F");
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - AHPL Revenue Share Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_ahpl_share_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - AHPL Revenue Share Check/Approve Failed!";			
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
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String vendor_nm = request.getParameter("vendor_nm")==null?"":request.getParameter("vendor_nm");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String qty_mmbtu = request.getParameter("qty_mmbtu")==null?"":request.getParameter("qty_mmbtu");
			String rate = request.getParameter("rate")==null?"":request.getParameter("rate");
			String price_cd = request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
			String grt = request.getParameter("grt")==null?"":request.getParameter("grt");
			String berthing_hrs = request.getParameter("berthing_hrs")==null?"":request.getParameter("berthing_hrs");
			String berthing_slots = request.getParameter("berthing_slots")==null?"":request.getParameter("berthing_slots");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("main_tax_struct_cd")==null?"":request.getParameter("main_tax_struct_cd");
			String tax_struct_info = request.getParameter("tax_struct_info")==null?"":request.getParameter("tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String exchg_rate_type = request.getParameter("exchg_rate_type")==null?"":request.getParameter("exchg_rate_type");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			//NEW VALUES
			String new_qty_mmbtu = request.getParameter("new_qty_mmbtu")==null?"":request.getParameter("new_qty_mmbtu");
			String new_rate = request.getParameter("new_rate")==null?"":request.getParameter("new_rate");
			String new_grt = request.getParameter("new_grt")==null?"":request.getParameter("new_grt");
			String new_berthing_hrs = request.getParameter("new_berthing_hrs")==null?"":request.getParameter("new_berthing_hrs");
			String new_berthing_slots = request.getParameter("new_berthing_slots")==null?"":request.getParameter("new_berthing_slots");
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_cd = request.getParameter("new_tax_cd")==null?"":request.getParameter("new_tax_cd");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String new_invoice_amt = request.getParameter("new_invoice_amt")==null?"":request.getParameter("new_invoice_amt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");
			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, "S");
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					int cnt1=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
			   				+ "ENT_BY,ENT_DT,SALE_PRICE,GRT,QUANTITY,HRS_BERTHING,TIME_SLOTS_BERTHING,INV_FLAG,REF_NO,CRITERIA,SAC_CODE) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
			   				+ "?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?,?,?,?,?,?,?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, "1");
					stmt1.setString(++cnt1, inv_seq+"");
					stmt1.setString(++cnt1, inv_no);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, fin_yr);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, grt);
					stmt1.setString(++cnt1, qty_mmbtu);
					stmt1.setString(++cnt1, berthing_hrs);
					stmt1.setString(++cnt1, berthing_slots);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, sel_inv_no);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
							+ "QUANTITY,SALE_PRICE,SALE_PRICE_UNIT,"
							+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
							+ "ENT_BY,ENT_DT,GRT,HRS_BERTHING,TIME_SLOTS_BERTHING) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,SYSDATE,?,?,?)";
					int stcount=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_type);
					stmt1.setString(++stcount, inv_seq+"");
					stmt1.setString(++stcount, fin_yr);
					stmt1.setString(++stcount, "1");
					stmt1.setString(++stcount, new_qty_mmbtu);
					stmt1.setString(++stcount, new_rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_grt);
					stmt1.setString(++stcount, new_berthing_hrs);
					stmt1.setString(++stcount, new_berthing_slots);
					
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					stmt1.executeUpdate();

					stmt1.close();
			   			
					int cnt1=0;
	   				query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,TAX_STRUCT_CD=?,TAX_CD=?,GRT=?,QUANTITY=?,HRS_BERTHING=?,"
							+ "TIME_SLOTS_BERTHING=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?, SAC_CODE=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, grt);
					stmt1.setString(++cnt1, qty_mmbtu);
					stmt1.setString(++cnt1, berthing_hrs);
					stmt1.setString(++cnt1, berthing_slots);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, new_financial_year);
					stmt1.setString(++cnt1, new_invoice_seq);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, exist_fin_yr);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
							
					int stcount=0;
					query1="UPDATE FMS_OTH_INV_CRDR_REF SET QUANTITY=?,SALE_PRICE=?,SALE_PRICE_UNIT=?,INVOICE_RAISED_IN=?,GROSS_AMT=?,"
							+ "TAX_AMT=?,TAX_STRUCT_CD=?,NET_PAYABLE_AMT=?,GRT=?,HRS_BERTHING=?,TIME_SLOTS_BERTHING=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? ";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, new_qty_mmbtu);
					stmt1.setString(++stcount, new_rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, new_grt);
					stmt1.setString(++stcount, new_berthing_hrs);
					stmt1.setString(++stcount, new_berthing_slots);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_financial_year);
					stmt1.setString(++stcount, new_invoice_seq);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_seq);
					stmt1.setString(++stcount, exist_fin_yr);
					stmt1.setString(++stcount, invoice_type);
					stmt1.executeUpdate();
					
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_oth_inv_hsa_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	private void ChkApprCreditDebitDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCreditDebitDtls()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_HSA_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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
	
	private void InsertUpdateScrapFixedAsset(HttpServletRequest request) throws SQLException 
	{

		String function_nm="InsertUpdateScrapFixedAsset()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String gate_pass = request.getParameter("gate_pass") == null ? "" : request.getParameter("gate_pass");
			String sale_no = request.getParameter("sales_agr") == null ? "" : request.getParameter("sales_agr");
			String sale_unit = request.getParameter("currency") == null ? "" : request.getParameter("currency");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String tax_inr = request.getParameter("tax_amt") == null ? "" : request.getParameter("tax_amt");
			String total_tax = request.getParameter("total_tax") == null ? "" : request.getParameter("total_tax");
			String net_amt = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String total_cess = request.getParameter("total_cess") == null ? "" : request.getParameter("total_cess");
			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String sac_code[] = request.getParameterValues("sac_code");
			String desc_item[] = request.getParameterValues("desc_item");
			String uom[] = request.getParameterValues("uom");
			String qty[] = request.getParameterValues("qty");
			String rate[] = request.getParameterValues("rate");
			String amount[] = request.getParameterValues("amount");
			
			String tax_struct_cd1[] = request.getParameterValues("tax_cd1");
			String tax_struct_dt1[] = request.getParameterValues("tax_dt1");
			String tax_struct_nm1[] = request.getParameterValues("tax_struct_nm1");
			String tax_amt[] = request.getParameterValues("tax_amount");
			String cess_per[] = request.getParameterValues("cess_per");
			String cess_amt[] = request.getParameterValues("cess_amt");
			
			String sale_price = "";
			String tax_cd = "I";
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}
			if(invoice_category.equals("P"))
			{
				tax_inr = total_tax;
			}
			int inv_seq1 = 0;
			boolean isOperated=false,isOperated1=false;
			int count=0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,ENT_BY,ENT_DT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG,REMARK) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,"
							+ "?,?,?,SYSDATE,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, remark1);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		isOperated=true;
			   		
			   		msg = "Successful! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				if(isOperated)
				{
					if(invoice_category.equals("S"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,GATE_PASS_NO,SALE_NO,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, inv_seq1+"");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, gate_pass);
							stmt1.setString(++cnt, sale_no);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
					}
					else if(invoice_category.equals("P"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							if(tax_struct_nm1[i].contains("CGST"))
							{
								tax_cd = "C";
							}
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,GATE_PASS_NO,SALE_NO,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG,CESS_RATE,CESS_AMOUNT,TOTAL_CESS_AMOUNT,ITEM_TAX_AMT) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?,?,?,?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, inv_seq1+"");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, tax_struct_cd1[i]);
							stmt1.setString(++cnt, tax_struct_dt1[i]);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, gate_pass);
							stmt1.setString(++cnt, sale_no);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, cess_per[i]);
							stmt1.setString(++cnt, cess_amt[i]);
							stmt1.setString(++cnt, total_cess);
							stmt1.setString(++cnt, tax_amt[i]);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
					}
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}

					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, SALE_AMT=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,INVOICE_CATEGORY=?,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),REMARK=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = ? AND VENDOR_TYPE = ?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_inr);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, "V");
					stmt1.executeUpdate();
					isOperated1=true;

					stmt1.close();

					msg = "Successful! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated1)
				{
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG = ?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, inv_seq);
					stmt2.setString(6, "F");
					stmt2.executeUpdate();
					
					stmt2.close();
					
					if(invoice_category.equals("S"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,GATE_PASS_NO,SALE_NO,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, gate_pass);
							stmt1.setString(++cnt, sale_no);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
					}
					else if(invoice_category.equals("P"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							if(tax_struct_nm1[i].contains("CGST"))
							{
								tax_cd = "C";
							}
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,GATE_PASS_NO,SALE_NO,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG,CESS_RATE,CESS_AMOUNT,TOTAL_CESS_AMOUNT,ITEM_TAX_AMT) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?,?,?,?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, tax_struct_cd1[i]);
							stmt1.setString(++cnt, tax_struct_dt1[i]);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, gate_pass);
							stmt1.setString(++cnt, sale_no);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, cess_per[i]);
							stmt1.setString(++cnt, cess_amt[i]);
							stmt1.setString(++cnt, total_cess);
							stmt1.setString(++cnt, tax_amt[i]);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
							
						}
					}
				}
			}
			
			if(operation.equals("INSERT")) 
			{
				operation="MODIFY";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_oth_inv_scrap_fixed_asset.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Scrap Fixed Asset Addition/Modification Failed!";			
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
	
	private void ChkApprSFA(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprSFA()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, "F");
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, "F");
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, "F");
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, "F");
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Scrap Fixed Asset Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_ahpl_share_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - Scrap Fixed Asset Check/Approve Failed!";			
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
	
	private void InsertUpdateGaInvoice(HttpServletRequest request) throws SQLException 
	{

		String function_nm="InsertUpdateGaInvoice()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String tax_struct_cd = request.getParameter("tax_cd") == null ? "" : request.getParameter("tax_cd");
			String tax_struct_dt = request.getParameter("tax_dt") == null ? "" : request.getParameter("tax_dt");
			String tax_struct_nm = request.getParameter("tax_struct_nm") == null ? "" : request.getParameter("tax_struct_nm");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String sale_unit = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String total_tax = request.getParameter("total_tax") == null ? "" : request.getParameter("total_tax");
			String net_amt = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String supp_state_tin = request.getParameter("supp_state_tin") == null ? "" : request.getParameter("supp_state_tin");
			String bu_seq = request.getParameter("bu_unit") == null ? "" : request.getParameter("bu_unit");
			
			String sac_code[] = request.getParameterValues("sac_code");
			String desc_item[] = request.getParameterValues("desc_item");
			String amount[] = request.getParameterValues("amount");
			String tax_struct_cd1[] = request.getParameterValues("tax_cd1");
			String tax_struct_dt1[] = request.getParameterValues("tax_dt1");
			String tax_amt[] = request.getParameterValues("tax_amount");
			String tax_amt1[] = request.getParameterValues("tax_amount2");
			String item_total[] = request.getParameterValues("total_line_amt");
			String state = utilBean.getStateName(dbcon, supp_state_tin);
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String sale_price = "";
			String tax_cd = "I";
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			if(tax_struct_nm.contains("CGST")) 
			{
				tax_cd = "C";
			}
			int inv_seq1 = 0;
			boolean isOperated=false,isOperated1=false;
			double tax_inr = 0;
			int count=0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				if(!state.equals("Gujarat"))
				{
					query+="AND BU_UNIT = ? ";
				}
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				if(!state.equals("Gujarat"))
				{
					stmt.setString(6, bu_seq);
				}
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					if(!state.equals("Gujarat"))
					{
						query+="AND BU_UNIT = ? ";
					}
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					if(!state.equals("Gujarat"))
					{
						stmt.setString(5, bu_seq);
					}
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_AMT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,ENT_BY,ENT_DT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG,REMARK,BU_UNIT,BU_STATE_TIN) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,SYSDATE,?,?,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, total_tax);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, bu_seq);
					stmt1.setString(++cnt, supp_state_tin);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		isOperated=true;
			   		
			   		msg = "Successful! - G&A Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - G&A Invoice Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				if(isOperated)
				{
					for(int i = 0; i<amount.length;i++)
					{
						if(tax_struct_nm.contains("CGST")) 
						{
							tax_inr = Double.parseDouble(tax_amt[i]) + Double.parseDouble(tax_amt1[i]);
						}
						else 
						{
							tax_inr = Double.parseDouble(tax_amt[i]);
						}
						int cnt=0;
				   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
				   				+ "TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_AMT,ITEM_TAX_AMT,ITEM_TOTAL_AMT,BU_UNIT,BU_STATE_TIN,"
				   				+ "ENT_BY,ENT_DT,INV_FLAG) "
				   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
				   				+ "?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
				   				+ "?,SYSDATE,?) ";
				   		stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, +i+1);
						stmt1.setString(++cnt, inv_seq1+"");
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item[i]);
						stmt1.setString(++cnt, tax_struct_cd1[i]);
						stmt1.setString(++cnt, tax_struct_dt1[i]);
						stmt1.setString(++cnt, fin_yr);
						stmt1.setString(++cnt, sac_code[i]);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, amount[i]);
						stmt1.setDouble(++cnt, tax_inr);
						stmt1.setString(++cnt, item_total[i]);
						stmt1.setString(++cnt, bu_seq);
						stmt1.setString(++cnt, supp_state_tin);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, "F");
						stmt1.executeUpdate();
				   		
				   		stmt1.close();
					}
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F' ";
				if(!state.equals("Gujarat"))
				{
					query+="AND BU_UNIT = ? ";
				}
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				if(!state.equals("Gujarat"))
				{
					stmt.setString(7, bu_seq);
				}
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						if(!state.equals("Gujarat"))
						{
							query+="AND BU_UNIT = ? ";
						}
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						if(!state.equals("Gujarat"))
						{
							stmt.setString(5, bu_seq);
						}
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}

					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?, SALE_AMT=?, GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,INVOICE_CATEGORY=?,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),REMARK=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = ? AND VENDOR_TYPE = ?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, sale_price);
					stmt1.setString(++cnt, sale_unit);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, total_tax);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, "V");
					stmt1.executeUpdate();
					isOperated1=true;

					stmt1.close();

					msg = "Successful! - GNA Invoice Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - GNA Invoice Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated1)
				{
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG = ?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, inv_seq);
					stmt2.setString(6, "F");
					stmt2.executeUpdate();
					
					stmt2.close();
					
					for(int i = 0; i<amount.length;i++)
					{
						if(tax_struct_nm.contains("CGST")) 
						{
							tax_inr = Double.parseDouble(tax_amt[i]) + Double.parseDouble(tax_amt1[i]);
						}
						else 
						{
							tax_inr = Double.parseDouble(tax_amt[i]);
						}
						int cnt=0;
						query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
				   				+ "TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,SAC_CODE,TAX_CD,ITEM_AMT,ITEM_TAX_AMT,ITEM_TOTAL_AMT,BU_UNIT,BU_STATE_TIN,"
				   				+ "ENT_BY,ENT_DT,INV_FLAG) "
				   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
				   				+ "?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,"
				   				+ "?,SYSDATE,?) ";
				   		stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, supp_cd);
						stmt1.setString(++cnt, vend_cd);
						stmt1.setString(++cnt, inv_type);
						stmt1.setString(++cnt, inv_dt);
						stmt1.setInt(++cnt, +i+1);
						stmt1.setString(++cnt, new_invoice_seq);
						stmt1.setString(++cnt, inv_no);
						stmt1.setString(++cnt, desc_item[i]);
						stmt1.setString(++cnt, tax_struct_cd1[i]);
						stmt1.setString(++cnt, tax_struct_dt1[i]);
						stmt1.setString(++cnt, new_financial_year);
						stmt1.setString(++cnt, sac_code[i]);
						stmt1.setString(++cnt, tax_cd);
						stmt1.setString(++cnt, amount[i]);
						stmt1.setDouble(++cnt, tax_inr);
						stmt1.setString(++cnt, item_total[i]);
						stmt1.setString(++cnt, bu_seq);
						stmt1.setString(++cnt, supp_state_tin);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, "F");
						stmt1.executeUpdate();
				   		
				   		stmt1.close();
					}
				}
			}
			
			if(operation.equals("INSERT")) 
			{
				operation="MODIFY";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_oth_inv_ga_inv.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - GNA Invoice Addition/Modification Failed!";			
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
	
	private void ChkApprGA(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprGA()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String bu_seq = request.getParameter("bu_seq") == null ? "" : request.getParameter("bu_seq");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ? AND BU_UNIT = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, "F");
				stmt.setString(9, bu_seq);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - GNA Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ? AND BU_UNIT = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, "F");
				stmt.setString(8, bu_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=? AND BU_UNIT=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, "F");
					stmt.setString(11, bu_seq);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=? AND BU_UNIT=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, "F");
					stmt1.setString(8, bu_seq);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - GNA Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - GNA Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_ga_inv_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - GNA Invoice Check/Approve Failed!";			
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
	
	
	private void InsertUpdateAhplCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAhplCrDr()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String price_cd = request.getParameter("price_cd")==null?"1":request.getParameter("price_cd");
			String rate = request.getParameter("rate")==null?"0":request.getParameter("rate");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_struct_info = request.getParameter("new_tax_struct_info")==null?"":request.getParameter("new_tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String desc = request.getParameter("desc") == null ? "" : request.getParameter("desc");
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			//NEW VALUES
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_eff_dt = request.getParameter("new_tax_dt")==null?"":request.getParameter("new_tax_dt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, "S");
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					int cnt1=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
			   				+ "ENT_BY,ENT_DT,SALE_PRICE,INV_FLAG,REF_NO,CRITERIA,SAC_CODE,TAX_EFF_DT,ITEM_DESCRIPTION) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
			   				+ "?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, "1");
					stmt1.setString(++cnt1, inv_seq+"");
					stmt1.setString(++cnt1, inv_no);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, fin_yr);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, sel_inv_no);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, desc);
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
							+ "SALE_PRICE,SALE_PRICE_UNIT,"
							+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
							+ "ENT_BY,ENT_DT) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,"
							+ "?,?,?,?,?,"
							+ "?,SYSDATE)";
					int stcount=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_type);
					stmt1.setString(++stcount, inv_seq+"");
					stmt1.setString(++stcount, fin_yr);
					stmt1.setString(++stcount, "1");
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					stmt1.executeUpdate();

					stmt1.close();
			   			
					int cnt1=0;
	   				query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,TAX_STRUCT_CD=?,TAX_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?, SAC_CODE=?, ITEM_DESCRIPTION=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, new_financial_year);
					stmt1.setString(++cnt1, new_invoice_seq);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, desc);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, exist_fin_yr);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					int stcount=0;
					query1="UPDATE FMS_OTH_INV_CRDR_REF SET SALE_PRICE=?,SALE_PRICE_UNIT=?,INVOICE_RAISED_IN=?,GROSS_AMT=?,"
							+ "TAX_AMT=?,TAX_STRUCT_CD=?,NET_PAYABLE_AMT=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? ";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_financial_year);
					stmt1.setString(++stcount, new_invoice_seq);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_seq);
					stmt1.setString(++stcount, exist_fin_yr);
					stmt1.setString(++stcount, invoice_type);
					stmt1.executeUpdate();
					
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_oth_inv_ahpl_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	private void ChkApprAhplCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprAhplCrDr()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_AHPL_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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
	
	
	private void InsertUpdatePFACrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdatePFACrDr()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String price_cd = request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String exchng_cd = request.getParameter("exchng_cd")==null?"":request.getParameter("exchng_cd");
			String exchng_dt = request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String gross_amt1 = request.getParameter("gross_amt1")==null?"":request.getParameter("gross_amt1");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_struct_info = request.getParameter("new_tax_struct_info")==null?"":request.getParameter("new_tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String desc = request.getParameter("desc") == null ? "" : request.getParameter("desc");
			String criteri_formula = request.getParameter("criteri_formula")==null ? "" :request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			String ship_cd[] = request.getParameterValues("ship_cd");
			String cargo_ref[] = request.getParameterValues("cargo_ref");
			String cargo_dt[] = request.getParameterValues("cargo_dt");
			String cargo_type[] = request.getParameterValues("cargo_type");
			String qty_mmbtu[] = request.getParameterValues("qty_mmbtu");
			String cargo_rate[] = request.getParameterValues("cargo_rate");
			String cargo_amt[] = request.getParameterValues("cargo_amt");
			
			String new_qty_mmbtu[] = request.getParameterValues("new_qty_mmbtu");
			String new_cargo_rate[] = request.getParameterValues("new_cargo_rate");
			String new_cargo_amt[] = request.getParameterValues("new_cargo_amt");
			
			//NEW VALUES
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_exchng_rate = request.getParameter("new_exchng_rate")==null?"":request.getParameter("new_exchng_rate");
			String new_gross_amt1 = request.getParameter("new_gross_amt1")==null?"":request.getParameter("new_gross_amt1");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_eff_dt = request.getParameter("new_tax_dt")==null?"":request.getParameter("new_tax_dt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, gross_amt1);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, "S");
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					if(isOperated)
					{
						for(int i=0;i<ship_cd.length;i++)
						{
							int cnt1=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
					   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
					   				+ "ENT_BY,ENT_DT,SALE_PRICE,INV_FLAG,REF_NO,CRITERIA,SAC_CODE,TAX_EFF_DT,ITEM_DESCRIPTION,REFERENCE_NO,VESSEL_CD,"
					   				+ "QUANTITY,CARGO_DT,CARGO_AMOUNT,CARGO_TYPE) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
					   				+ "?,?,?,?,"
					   				+ "?,SYSDATE,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt1, comp_cd);
							stmt1.setString(++cnt1, supp_cd);
							stmt1.setString(++cnt1, vendor_cd);
							stmt1.setString(++cnt1, invoice_type);
							stmt1.setString(++cnt1, invoice_dt);
							stmt1.setInt(++cnt1, i+1);
							stmt1.setString(++cnt1, inv_seq+"");
							stmt1.setString(++cnt1, inv_no);
							stmt1.setString(++cnt1, price_cd);
							stmt1.setString(++cnt1, tax_struct_cd);
							stmt1.setString(++cnt1, fin_yr);
							stmt1.setString(++cnt1, tax_cd);
							stmt1.setString(++cnt1, emp_cd);
							stmt1.setString(++cnt1, cargo_rate[i]);
							stmt1.setString(++cnt1, inv_flag);
							stmt1.setString(++cnt1, sel_inv_no);
							stmt1.setString(++cnt1, criteri_formula);
							stmt1.setString(++cnt1, sac_cd);
							stmt1.setString(++cnt1, tax_eff_dt);
							stmt1.setString(++cnt1, desc);
							stmt1.setString(++cnt1, cargo_ref[i]);
							stmt1.setString(++cnt1, ship_cd[i]);
							stmt1.setString(++cnt1, qty_mmbtu[i]);
							stmt1.setString(++cnt1, cargo_dt[i]);
							stmt1.setString(++cnt1, cargo_amt[i]);
							stmt1.setString(++cnt1, cargo_type[i]);

							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
							
						for(int i=0;i<ship_cd.length;i++)
						{
							int stcount=0;
							query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
									+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
									+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
									+ "ENT_BY,ENT_DT,QUANTITY,CARGO_DT,CARGO_AMOUNT,REFERENCE_NO,EXCHG_RATE_VALUE,EXCHG_RATE_CD,EXCHG_RATE_DT) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?, "
									+ "?,?,?,?,?,"
									+ "?,SYSDATE,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'))";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++stcount, comp_cd);
							stmt1.setString(++stcount, supp_cd);
							stmt1.setString(++stcount, vendor_cd);
							stmt1.setString(++stcount, invoice_type);
							stmt1.setString(++stcount, inv_seq+"");
							stmt1.setString(++stcount, fin_yr);
							stmt1.setInt(++stcount, i+1);
							stmt1.setString(++stcount, new_cargo_rate[i]);
							stmt1.setString(++stcount, price_cd);
							stmt1.setString(++stcount, new_gross_amt);
							stmt1.setString(++stcount, invoice_raised_in);
							stmt1.setString(++stcount, new_gross_amt1);
							stmt1.setString(++stcount, new_tax_amt);
							stmt1.setString(++stcount, new_tax_struct_cd);
							stmt1.setString(++stcount, new_net_payable);
							stmt1.setString(++stcount, emp_cd);
							stmt1.setString(++stcount, new_qty_mmbtu[i]);
							stmt1.setString(++stcount, cargo_dt[i]);
							stmt1.setString(++stcount, new_cargo_amt[i]);
							stmt1.setString(++stcount, cargo_ref[i]);
							stmt1.setString(++stcount, new_exchng_rate);
							stmt1.setString(++stcount, exchng_cd);
							stmt1.setString(++stcount, exchng_dt);
							
							stmt1.executeUpdate();
							stmt1.close();
						}
					
					}
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_AMT=?, "
							+ "SALE_PRICE_UNIT=?, GROSS_AMT=?, EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'),EXCHG_RATE_VALUE=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt,  gross_amt);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt1);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					stmt1.executeUpdate();

					stmt1.close();
			   			
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG IN ('CR','DR')";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, invoice_seq);
					stmt2.executeUpdate();
					stmt2.close();
					
					for(int i=0;i<ship_cd.length;i++)
					{
						int cnt1=0;
				   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
				   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
				   				+ "ENT_BY,ENT_DT,SALE_PRICE,INV_FLAG,REF_NO,CRITERIA,SAC_CODE,TAX_EFF_DT,ITEM_DESCRIPTION,REFERENCE_NO,VESSEL_CD,"
				   				+ "QUANTITY,CARGO_DT,CARGO_AMOUNT,CARGO_TYPE) "
				   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
				   				+ "?,?,?,?,"
				   				+ "?,SYSDATE,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?) ";
				   		stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, supp_cd);
						stmt1.setString(++cnt1, vendor_cd);
						stmt1.setString(++cnt1, invoice_type);
						stmt1.setString(++cnt1, invoice_dt);
						stmt1.setInt(++cnt1, i+1);
						stmt1.setString(++cnt1, new_invoice_seq);
						stmt1.setString(++cnt1, inv_no);
						stmt1.setString(++cnt1, price_cd);
						stmt1.setString(++cnt1, tax_struct_cd);
						stmt1.setString(++cnt1, new_financial_year);
						stmt1.setString(++cnt1, tax_cd);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, cargo_rate[i]);
						stmt1.setString(++cnt1, inv_flag);
						stmt1.setString(++cnt1, sel_inv_no);
						stmt1.setString(++cnt1, criteri_formula);
						stmt1.setString(++cnt1, sac_cd);
						stmt1.setString(++cnt1, tax_eff_dt);
						stmt1.setString(++cnt1, desc);
						stmt1.setString(++cnt1, cargo_ref[i]);
						stmt1.setString(++cnt1, ship_cd[i]);
						stmt1.setString(++cnt1, qty_mmbtu[i]);
						stmt1.setString(++cnt1, cargo_dt[i]);
						stmt1.setString(++cnt1, cargo_amt[i]);
						stmt1.setString(++cnt1, cargo_type[i]);

						stmt1.executeUpdate();
				   		
				   		stmt1.close();
					}
						
					query2="DELETE FROM FMS_OTH_INV_CRDR_REF "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, invoice_seq);
					stmt2.executeUpdate();
					stmt2.close();
					
					for(int i=0;i<ship_cd.length;i++)
					{
						int stcount=0;
						query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
								+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
								+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
								+ "ENT_BY,ENT_DT,QUANTITY,CARGO_DT,CARGO_AMOUNT,REFERENCE_NO,EXCHG_RATE_VALUE,EXCHG_RATE_CD,EXCHG_RATE_DT) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?, "
								+ "?,?,?,?,?,"
								+ "?,SYSDATE,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'))";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++stcount, comp_cd);
						stmt1.setString(++stcount, supp_cd);
						stmt1.setString(++stcount, vendor_cd);
						stmt1.setString(++stcount, invoice_type);
						stmt1.setString(++stcount, new_invoice_seq);
						stmt1.setString(++stcount, new_financial_year);
						stmt1.setInt(++stcount, i+1);
						stmt1.setString(++stcount, new_cargo_rate[i]);
						stmt1.setString(++stcount, price_cd);
						stmt1.setString(++stcount, new_gross_amt);
						stmt1.setString(++stcount, invoice_raised_in);
						stmt1.setString(++stcount, new_gross_amt1);
						stmt1.setString(++stcount, new_tax_amt);
						stmt1.setString(++stcount, new_tax_struct_cd);
						stmt1.setString(++stcount, new_net_payable);
						stmt1.setString(++stcount, emp_cd);
						stmt1.setString(++stcount, new_qty_mmbtu[i]);
						stmt1.setString(++stcount, cargo_dt[i]);
						stmt1.setString(++stcount, new_cargo_amt[i]);
						stmt1.setString(++stcount, cargo_ref[i]);
						stmt1.setString(++stcount, new_exchng_rate);
						stmt1.setString(++stcount, exchng_cd);
						stmt1.setString(++stcount, exchng_dt);
						
						stmt1.executeUpdate();
						stmt1.close();
					}
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_oth_inv_hppl_seipl_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	
	private void ChkApprPFACrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprPFACrDr()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_PFA_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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
	
	//ajay20260221
	private void InsertUpdateCostRHCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCostRHCrDr()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String price_cd = request.getParameter("price_cd")==null?"1":request.getParameter("price_cd");
			String rate = request.getParameter("rate")==null?"0":request.getParameter("rate");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_struct_info = request.getParameter("new_tax_struct_info")==null?"":request.getParameter("new_tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String desc = request.getParameter("desc") == null ? "" : request.getParameter("desc");
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			//NEW VALUES
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_eff_dt = request.getParameter("new_tax_dt")==null?"":request.getParameter("new_tax_dt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, "S");
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					int cnt1=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
			   				+ "ENT_BY,ENT_DT,SALE_PRICE,INV_FLAG,REF_NO,CRITERIA,SAC_CODE,TAX_EFF_DT,ITEM_DESCRIPTION) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
			   				+ "?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, "1");
					stmt1.setString(++cnt1, inv_seq+"");
					stmt1.setString(++cnt1, inv_no);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, fin_yr);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, sel_inv_no);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, desc);
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
							+ "SALE_PRICE,SALE_PRICE_UNIT,"
							+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
							+ "ENT_BY,ENT_DT) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,"
							+ "?,?,?,?,?,"
							+ "?,SYSDATE)";
					int stcount=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_type);
					stmt1.setString(++stcount, inv_seq+"");
					stmt1.setString(++stcount, fin_yr);
					stmt1.setString(++stcount, "1");
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					stmt1.executeUpdate();

					stmt1.close();
			   			
					int cnt1=0;
	   				query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,TAX_STRUCT_CD=?,TAX_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?, SAC_CODE=?, ITEM_DESCRIPTION=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, new_financial_year);
					stmt1.setString(++cnt1, new_invoice_seq);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, desc);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, exist_fin_yr);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					int stcount=0;
					query1="UPDATE FMS_OTH_INV_CRDR_REF SET SALE_PRICE=?,SALE_PRICE_UNIT=?,INVOICE_RAISED_IN=?,GROSS_AMT=?,"
							+ "TAX_AMT=?,TAX_STRUCT_CD=?,NET_PAYABLE_AMT=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? ";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_financial_year);
					stmt1.setString(++stcount, new_invoice_seq);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_seq);
					stmt1.setString(++stcount, exist_fin_yr);
					stmt1.setString(++stcount, invoice_type);
					stmt1.executeUpdate();
					
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_other_inv_cost_recharge_hppl_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	private void ChkApprCostRHCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCostRHCrDr()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_COSTRH_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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
	
	private void InsertUpdateCostRCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCostRCrDr()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String price_cd = request.getParameter("price_cd")==null?"1":request.getParameter("price_cd");
			String rate = request.getParameter("rate")==null?"0":request.getParameter("rate");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String tax_amt = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_struct_info = request.getParameter("new_tax_struct_info")==null?"":request.getParameter("new_tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String remark2 = request.getParameter("remark2")==null?"":request.getParameter("remark2");
			String invoice_id_seq = request.getParameter("invoice_id_seq")==null?"":request.getParameter("invoice_id_seq");
			String invoice_no = request.getParameter("invoice_no")==null?"":request.getParameter("invoice_no");
			String sac_cd = request.getParameter("sac_cd") == null ? "" : request.getParameter("sac_cd");
			String desc = request.getParameter("desc") == null ? "" : request.getParameter("desc");
			String criteri_formula = request.getParameter("criteri_formula")==null?"":request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			String exchng_cd = request.getParameter("exchng_cd")==null?"":request.getParameter("exchng_cd");
			String exchng_dt = request.getParameter("exchng_dt")==null?"":request.getParameter("exchng_dt");
			String exchng_rate = request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String gross_amt1 = request.getParameter("gross_amt1")==null?"":request.getParameter("gross_amt1");
			
			
			//NEW VALUES
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_eff_dt = request.getParameter("new_tax_dt")==null?"":request.getParameter("new_tax_dt");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			String new_exchng_rate = request.getParameter("new_exchng_rate")==null?"":request.getParameter("new_exchng_rate");
			String new_gross_amt1 = request.getParameter("new_gross_amt1")==null?"":request.getParameter("new_gross_amt1");
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,REMARK1,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY, "
							+ "EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,TO_DATE(?,'DD/MM/YYYY'),?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, "S");
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_dt);
					stmt1.setString(++cnt, exchng_rate);
					if(!price_cd.equals("1"))
					{
						stmt1.setString(++cnt, gross_amt1);
					}
					else
					{
						stmt1.setString(++cnt, gross_amt);
					}
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					int cnt1=0;
			   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,"
			   				+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,TAX_CD,"
			   				+ "ENT_BY,ENT_DT,SALE_PRICE,INV_FLAG,REF_NO,CRITERIA,SAC_CODE,TAX_EFF_DT,ITEM_DESCRIPTION) "
			   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?, "
			   				+ "?,?,?,?,"
			   				+ "?,SYSDATE,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?) ";
			   		stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, "1");
					stmt1.setString(++cnt1, inv_seq+"");
					stmt1.setString(++cnt1, inv_no);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, fin_yr);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, sel_inv_no);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, desc);
					stmt1.executeUpdate();
			   		
			   		stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
							+ "SALE_PRICE,SALE_PRICE_UNIT,"
							+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
							+ "ENT_BY,ENT_DT,EXCHG_RATE_VALUE,EXCHG_RATE_CD,EXCHG_RATE_DT,SALE_AMT) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,"
							+ "?,?,?,?,?,"
							+ "?,SYSDATE,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					int stcount=0;
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_type);
					stmt1.setString(++stcount, inv_seq+"");
					stmt1.setString(++stcount, fin_yr);
					stmt1.setString(++stcount, "1");
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					if(!price_cd.equals("1"))
					{
						stmt1.setString(++stcount, new_gross_amt1);
					}
					else
					{
						stmt1.setString(++stcount, new_gross_amt);
					}
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_exchng_rate);
					stmt1.setString(++stcount, exchng_cd);
					stmt1.setString(++stcount, exchng_dt);
					stmt1.setString(++stcount, new_gross_amt);
					
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,GROSS_AMT=?, "
							+ "TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, REMARK1=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "SALE_AMT=?, EXCHG_RATE_CD=?, EXCHG_RATE_DT=TO_DATE(?,'DD/MM/YYYY'), EXCHG_RATE_VALUE=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR') ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, rate);
					stmt1.setString(++cnt, price_cd);
					if(!price_cd.equals("1"))
					{
						stmt1.setString(++cnt, gross_amt1);
					}
					else
					{
						stmt1.setString(++cnt, gross_amt);
					}
					stmt1.setString(++cnt, tax_amt);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, remark2);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, exchng_cd);
					stmt1.setString(++cnt, exchng_dt);
					stmt1.setString(++cnt, exchng_rate);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					
					stmt1.executeUpdate();

					stmt1.close();
			   			
					int cnt1=0;
	   				query1="UPDATE FMS_OTH_INVOICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_PRICE=?, "
							+ "SALE_PRICE_UNIT=?,TAX_STRUCT_CD=?,TAX_CD=?,TAX_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?, SAC_CODE=?, ITEM_DESCRIPTION=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt1, invoice_dt);
					stmt1.setString(++cnt1, rate);
					stmt1.setString(++cnt1, price_cd);
					stmt1.setString(++cnt1, tax_struct_cd);
					stmt1.setString(++cnt1, tax_cd);
					stmt1.setString(++cnt1, tax_eff_dt);
					stmt1.setString(++cnt1, emp_cd);
					stmt1.setString(++cnt1, criteri_formula);
					stmt1.setString(++cnt1, inv_flag);
					stmt1.setString(++cnt1, new_financial_year);
					stmt1.setString(++cnt1, new_invoice_seq);
					stmt1.setString(++cnt1, sac_cd);
					stmt1.setString(++cnt1, desc);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, supp_cd);
					stmt1.setString(++cnt1, vendor_cd);
					stmt1.setString(++cnt1, invoice_seq);
					stmt1.setString(++cnt1, exist_fin_yr);
					stmt1.setString(++cnt1, invoice_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					//NEW VALUE FMS_INV_CRDR_REF
					int stcount=0;
					query1="UPDATE FMS_OTH_INV_CRDR_REF SET SALE_PRICE=?,SALE_PRICE_UNIT=?,INVOICE_RAISED_IN=?,GROSS_AMT=?,"
							+ "TAX_AMT=?,TAX_STRUCT_CD=?,NET_PAYABLE_AMT=?,MODIFY_BY=?, MODIFY_DT=SYSDATE,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,EXCHG_RATE_VALUE=?,EXCHG_RATE_CD=?,EXCHG_RATE_DT=TO_DATE(?, 'DD/MM/YYYY'),SALE_AMT=?"
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? ";
	   				stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++stcount, rate);
					stmt1.setString(++stcount, price_cd);
					stmt1.setString(++stcount, invoice_raised_in);
					if(!price_cd.equals("1"))
					{
						stmt1.setString(++stcount, new_gross_amt1);
					}
					else
					{
						stmt1.setString(++stcount, new_gross_amt);
					}
					stmt1.setString(++stcount, new_tax_amt);
					stmt1.setString(++stcount, new_tax_struct_cd);
					stmt1.setString(++stcount, new_net_payable);
					stmt1.setString(++stcount, emp_cd);
					stmt1.setString(++stcount, new_financial_year);
					stmt1.setString(++stcount, new_invoice_seq);
					stmt1.setString(++stcount, new_exchng_rate);
					stmt1.setString(++stcount, exchng_cd);
					stmt1.setString(++stcount, exchng_dt);
					stmt1.setString(++stcount, new_gross_amt);
					stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, supp_cd);
					stmt1.setString(++stcount, vendor_cd);
					stmt1.setString(++stcount, invoice_seq);
					stmt1.setString(++stcount, exist_fin_yr);
					stmt1.setString(++stcount, invoice_type);
					stmt1.executeUpdate();
					
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_oth_inv_cost_recharge_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	private void ChkApprCostRCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprCostRCrDr()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_COSTR_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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
	
	private void InsertUpdateScrapCrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateScrapCrDr()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String month =request.getParameter("month")==null?"":request.getParameter("month");
			String year =request.getParameter("year")==null?"":request.getParameter("year");
			
			String supp_cd = request.getParameter("supplier_cd")==null?"":request.getParameter("supplier_cd");
			String supp_nm = request.getParameter("supplier_nm")==null?"":request.getParameter("supplier_nm");
			String vendor_cd = request.getParameter("vendor_cd")==null?"":request.getParameter("vendor_cd");
			String invoice_type = request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			String period_start_dt = request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
			String period_end_dt = request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			
			String invoice_seq = request.getParameter("invoice_seq")==null?"":request.getParameter("invoice_seq");
			String financial_year = request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
			String invoice_dt = request.getParameter("invoice_dt")==null?"":request.getParameter("invoice_dt");
			String invoice_due_dt = request.getParameter("invoice_due_dt")==null?"":request.getParameter("invoice_due_dt");
			String price_cd = request.getParameter("price_cd")==null?"":request.getParameter("price_cd");
			String gross_amt = request.getParameter("gross_amt")==null?"":request.getParameter("gross_amt");
			String invoice_raised_in = request.getParameter("invoice_raised_in")==null?"":request.getParameter("invoice_raised_in"); 
			String total_tax = request.getParameter("tax_amt")==null?"":request.getParameter("tax_amt");
			String tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String tax_struct_dt = request.getParameter("new_tax_dt") == null ? "" : request.getParameter("new_tax_dt");
			String tax_struct_info = request.getParameter("new_tax_struct_info")==null?"":request.getParameter("new_tax_struct_info");
			String net_payable = request.getParameter("net_payable")==null?"":request.getParameter("net_payable");
			String remark1 = request.getParameter("remark1")==null?"":request.getParameter("remark1");
			String criteri_formula = request.getParameter("criteri_formula")==null ? "" :request.getParameter("criteri_formula");
			String exist_fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			
			String sac_code[] = request.getParameterValues("item_hsn");
			String desc_item[] = request.getParameterValues("item_des");
			String uom[] = request.getParameterValues("item_unit");
			String qty[] = request.getParameterValues("item_qty");
			String rate[] = request.getParameterValues("item_price");
			String amount[] = request.getParameterValues("item_amt");
			String tax_amt[] = request.getParameterValues("item_tax_amt");
			
			String new_rate[] = request.getParameterValues("new_item_price");
			String new_qty[] = request.getParameterValues("new_item_qty");
			String new_amt[] = request.getParameterValues("new_item_amt");
			String new_tax_amt1[] = request.getParameterValues("new_item_tax_amt");
			
			String tax_struct_cd1[] = request.getParameterValues("new_item_tax_cd");
			String tax_struct_dt1[] = request.getParameterValues("new_item_tax_dt");
			String tax_struct_nm1[] = request.getParameterValues("new_item_tax_struct_info");
			
			
			//NEW VALUES
			String new_gross_amt = request.getParameter("new_gross_amt")==null?"":request.getParameter("new_gross_amt");
			String new_tax_amt = request.getParameter("new_tax_amt")==null?"":request.getParameter("new_tax_amt");
			String new_tax_struct_cd = request.getParameter("new_tax_struct_cd")==null?"":request.getParameter("new_tax_struct_cd");
			String new_net_payable = request.getParameter("new_net_payable")==null?"":request.getParameter("new_net_payable");			
			String fin_yr = request.getParameter("exist_fin_yr") == null ? "" : request.getParameter("exist_fin_yr");
			
			month= invoice_dt.split("/")[1];
			year=invoice_dt.split("/")[2];
			period_start_dt = utilDate.getFirstDateOfMonth(month, year);
			period_end_dt = utilDate.getLastDateOfMonth(month, year);
			
			String inv_no="";
			
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			
			String tax_cd = "I";
			
			if(tax_struct_info.contains("CGST")) 
			{
				tax_cd = "C";
			}
			
			String new_financial_year=financial_year;
			String new_invoice_seq=invoice_seq;
			int inv_seq1 = 0;
			boolean isOperated = false;
			if(opration.equals("INSERT"))
			{
				fin_yr=utilDate.getFinancialYear(invoice_dt);
				new_financial_year=fin_yr;
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, invoice_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, invoice_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				if(count==0)
				{
					
					int inv_seq=0;
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, invoice_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq = rset.getInt(1);
					}
					else 
					{
						inv_seq = 1;
					}
					rset.close();
					stmt.close();

					invoice_seq=""+inv_seq;
					new_invoice_seq=invoice_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
							+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE_UNIT,SALE_AMT,GROSS_AMT,TAX_AMT_INR,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,REMARK,ENT_BY,ENT_DT,INV_FLAG,VENDOR_TYPE,REF_NO,CRITERIA,DUE_DT,INVOICE_CATEGORY) "
							+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "?,?,?,?,SYSDATE,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, total_tax);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, sel_inv_no);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt, invoice_category);
			   		stmt1.executeUpdate();
					isOperated=true;
					stmt1.close();
					
					if(isOperated)
					{
						if(invoice_category.equals("S"))
						{
							for(int i=0;i<desc_item.length;i++)
							{
								cnt=0;
						   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
						   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,"
						   				+ "ENT_BY,ENT_DT,INV_FLAG) "
						   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
						   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,"
						   				+ "?,SYSDATE,?) ";
						   		stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(++cnt, comp_cd);
								stmt1.setString(++cnt, supp_cd);
								stmt1.setString(++cnt, vendor_cd);
								stmt1.setString(++cnt, invoice_type);
								stmt1.setString(++cnt, invoice_dt);
								stmt1.setInt(++cnt, i+1);
								stmt1.setString(++cnt, inv_seq+"");
								stmt1.setString(++cnt, inv_no);
								stmt1.setString(++cnt, desc_item[i]);
								stmt1.setString(++cnt, rate[i]);
								stmt1.setString(++cnt, price_cd);
								stmt1.setString(++cnt, tax_struct_cd);
								stmt1.setString(++cnt, tax_struct_dt);
								stmt1.setString(++cnt, fin_yr);
								stmt1.setString(++cnt, sac_code[i]);
								stmt1.setString(++cnt, qty[i]);
								stmt1.setString(++cnt, uom[i]);
								stmt1.setString(++cnt, tax_cd);
								stmt1.setString(++cnt, amount[i]);
								stmt1.setString(++cnt, emp_cd);
								stmt1.setString(++cnt, inv_flag);
								stmt1.executeUpdate();
						   		
						   		stmt1.close();
							}
							
							for(int i=0;i<desc_item.length;i++)
							{
								int stcount=0;
								query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
										+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
										+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
										+ "ENT_BY,ENT_DT,QUANTITY,ITEM_AMT,ITEM_DESCRIPTION) "
										+ "VALUES(?,?,?,?,?,?,?,"
										+ "?,?,?, "
										+ "?,?,?,?,?,"
										+ "?,SYSDATE,?,?,?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(++stcount, comp_cd);
								stmt1.setString(++stcount, supp_cd);
								stmt1.setString(++stcount, vendor_cd);
								stmt1.setString(++stcount, invoice_type);
								stmt1.setString(++stcount, inv_seq+"");
								stmt1.setString(++stcount, fin_yr);
								stmt1.setInt(++stcount, i+1);
								stmt1.setString(++stcount, new_rate[i]);
								stmt1.setString(++stcount, price_cd);
								stmt1.setString(++stcount, new_gross_amt);
								stmt1.setString(++stcount, invoice_raised_in);
								stmt1.setString(++stcount, new_gross_amt);
								stmt1.setString(++stcount, new_tax_amt);
								stmt1.setString(++stcount, new_tax_struct_cd);
								stmt1.setString(++stcount, new_net_payable);
								stmt1.setString(++stcount, emp_cd);
								stmt1.setString(++stcount, new_qty[i]);
								stmt1.setString(++stcount, new_amt[i]);
								stmt1.setString(++stcount, desc_item[i]);
								
								stmt1.executeUpdate();
								stmt1.close();
							}
						}
						else if(invoice_category.equals("P"))
						{
							for(int i=0;i<desc_item.length;i++)
							{
								if(tax_struct_nm1[i].contains("CGST"))
								{
									tax_cd = "C";
								}
								
								cnt=0;
						   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
						   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,"
						   				+ "ENT_BY,ENT_DT,INV_FLAG,ITEM_TAX_AMT) "
						   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
						   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,"
						   				+ "?,SYSDATE,?,?) ";
						   		stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(++cnt, comp_cd);
								stmt1.setString(++cnt, supp_cd);
								stmt1.setString(++cnt, vendor_cd);
								stmt1.setString(++cnt, invoice_type);
								stmt1.setString(++cnt, invoice_dt);
								stmt1.setInt(++cnt, i+1);
								stmt1.setString(++cnt, inv_seq+"");
								stmt1.setString(++cnt, inv_no);
								stmt1.setString(++cnt, desc_item[i]);
								stmt1.setString(++cnt, rate[i]);
								stmt1.setString(++cnt, price_cd);
								stmt1.setString(++cnt, tax_struct_cd1[i]);
								stmt1.setString(++cnt, tax_struct_dt1[i]);
								stmt1.setString(++cnt, fin_yr);
								stmt1.setString(++cnt, sac_code[i]);
								stmt1.setString(++cnt, qty[i]);
								stmt1.setString(++cnt, uom[i]);
								stmt1.setString(++cnt, tax_cd);
								stmt1.setString(++cnt, amount[i]);
								stmt1.setString(++cnt, emp_cd);
								stmt1.setString(++cnt, inv_flag);
								stmt1.setString(++cnt, tax_amt[i]);
								stmt1.executeUpdate();
						   		
						   		stmt1.close();
							}
							
							for(int i=0;i<desc_item.length;i++)
							{
								int stcount=0;
								query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
										+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
										+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
										+ "ENT_BY,ENT_DT,QUANTITY,ITEM_AMT,ITEM_TAX_AMT,ITEM_DESCRIPTION) "
										+ "VALUES(?,?,?,?,?,?,?,"
										+ "?,?,?, "
										+ "?,?,?,?,?,"
										+ "?,SYSDATE,?,?,?,?)";
								stmt1=dbcon.prepareStatement(query1);
								stmt1.setString(++stcount, comp_cd);
								stmt1.setString(++stcount, supp_cd);
								stmt1.setString(++stcount, vendor_cd);
								stmt1.setString(++stcount, invoice_type);
								stmt1.setString(++stcount, inv_seq+"");
								stmt1.setString(++stcount, fin_yr);
								stmt1.setInt(++stcount, i+1);
								stmt1.setString(++stcount, new_rate[i]);
								stmt1.setString(++stcount, price_cd);
								stmt1.setString(++stcount, new_gross_amt);
								stmt1.setString(++stcount, invoice_raised_in);
								stmt1.setString(++stcount, new_gross_amt);
								stmt1.setString(++stcount, new_tax_amt);
								stmt1.setString(++stcount, tax_struct_cd1[i]);
								stmt1.setString(++stcount, new_net_payable);
								stmt1.setString(++stcount, emp_cd);
								stmt1.setString(++stcount, new_qty[i]);
								stmt1.setString(++stcount, new_amt[i]);
								stmt1.setString(++stcount, new_tax_amt1[i]);
								stmt1.setString(++stcount, desc_item[i]);
								
								stmt1.executeUpdate();
								stmt1.close();
							}
						}
					
					}
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Generated!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Already Generated!";
					msg_type="E";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				int count=0;
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG IN ('CR','DR')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, invoice_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vendor_cd);
				stmt.setString(6, invoice_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(invoice_dt);
					
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, invoice_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}
					
					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'),DUE_DT=TO_DATE(?,'DD/MM/YYYY'), SALE_AMT=?, "
							+ "SALE_PRICE_UNIT=?, GROSS_AMT=?, TAX_AMT_INR=?, NET_PAYABLE=?, INVOICE_RAISED_IN=?, REMARK=?, MODIFY_BY=?, MODIFY_DT=SYSDATE, "
							+ "CRITERIA=?,INV_FLAG=?,FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG IN ('CR','DR')";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, invoice_dt);
					stmt1.setString(++cnt, invoice_due_dt);
					stmt1.setString(++cnt,  gross_amt);
					stmt1.setString(++cnt, price_cd);
					stmt1.setString(++cnt,  gross_amt);
					stmt1.setString(++cnt, total_tax);
					stmt1.setString(++cnt, net_payable);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, criteri_formula);
					stmt1.setString(++cnt, inv_flag);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start_dt);
					stmt1.setString(++cnt, period_end_dt);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vendor_cd);
					stmt1.setString(++cnt, invoice_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, invoice_type);
					stmt1.executeUpdate();

					stmt1.close();
			   			
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG IN ('CR','DR')";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, invoice_seq);
					stmt2.executeUpdate();
					stmt2.close();
					
					query2="DELETE FROM FMS_OTH_INV_CRDR_REF "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, invoice_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, invoice_seq);
					stmt2.executeUpdate();
					stmt2.close();
					
					if(invoice_category.equals("S"))
					{
						for(int i=0;i<desc_item.length;i++)
						{
							cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vendor_cd);
							stmt1.setString(++cnt, invoice_type);
							stmt1.setString(++cnt, invoice_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, new_invoice_seq+"");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, price_cd);
							stmt1.setString(++cnt, tax_struct_cd);
							stmt1.setString(++cnt, tax_struct_dt);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, inv_flag);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
						
						for(int i=0;i<desc_item.length;i++)
						{
							int stcount=0;
							query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
									+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
									+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
									+ "ENT_BY,ENT_DT,QUANTITY,ITEM_AMT,ITEM_DESCRIPTION) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?, "
									+ "?,?,?,?,?,"
									+ "?,SYSDATE,?,?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++stcount, comp_cd);
							stmt1.setString(++stcount, supp_cd);
							stmt1.setString(++stcount, vendor_cd);
							stmt1.setString(++stcount, invoice_type);
							stmt1.setString(++stcount, new_invoice_seq);
							stmt1.setString(++stcount, new_financial_year);
							stmt1.setInt(++stcount, i+1);
							stmt1.setString(++stcount, new_rate[i]);
							stmt1.setString(++stcount, price_cd);
							stmt1.setString(++stcount, new_gross_amt);
							stmt1.setString(++stcount, invoice_raised_in);
							stmt1.setString(++stcount, new_gross_amt);
							stmt1.setString(++stcount, new_tax_amt);
							stmt1.setString(++stcount, new_tax_struct_cd);
							stmt1.setString(++stcount, new_net_payable);
							stmt1.setString(++stcount, emp_cd);
							stmt1.setString(++stcount, new_qty[i]);
							stmt1.setString(++stcount, new_amt[i]);
							stmt1.setString(++stcount, desc_item[i]);
							
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
					else if(invoice_category.equals("P"))
					{
						for(int i=0;i<desc_item.length;i++)
						{
							if(tax_struct_nm1[i].contains("CGST"))
							{
								tax_cd = "C";
							}
							
							cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,TAX_EFF_DT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,TAX_CD,ITEM_AMT,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG,ITEM_TAX_AMT) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vendor_cd);
							stmt1.setString(++cnt, invoice_type);
							stmt1.setString(++cnt, invoice_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, price_cd);
							stmt1.setString(++cnt, tax_struct_cd1[i]);
							stmt1.setString(++cnt, tax_struct_dt1[i]);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, tax_cd);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, inv_flag);
							stmt1.setString(++cnt, tax_amt[i]);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
						
						for(int i=0;i<desc_item.length;i++)
						{
							int stcount=0;
							query1="INSERT INTO FMS_OTH_INV_CRDR_REF(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,"
									+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
									+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
									+ "ENT_BY,ENT_DT,QUANTITY,ITEM_AMT,ITEM_TAX_AMT,ITEM_DESCRIPTION) "
									+ "VALUES(?,?,?,?,?,?,?,"
									+ "?,?,?, "
									+ "?,?,?,?,?,"
									+ "?,SYSDATE,?,?,?,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++stcount, comp_cd);
							stmt1.setString(++stcount, supp_cd);
							stmt1.setString(++stcount, vendor_cd);
							stmt1.setString(++stcount, invoice_type);
							stmt1.setString(++stcount, new_invoice_seq);
							stmt1.setString(++stcount, new_financial_year);
							stmt1.setInt(++stcount, i+1);
							stmt1.setString(++stcount, new_rate[i]);
							stmt1.setString(++stcount, price_cd);
							stmt1.setString(++stcount, new_gross_amt);
							stmt1.setString(++stcount, invoice_raised_in);
							stmt1.setString(++stcount, new_gross_amt);
							stmt1.setString(++stcount, new_tax_amt);
							stmt1.setString(++stcount, tax_struct_cd1[i]);
							stmt1.setString(++stcount, new_net_payable);
							stmt1.setString(++stcount, emp_cd);
							stmt1.setString(++stcount, new_qty[i]);
							stmt1.setString(++stcount, new_amt[i]);
							stmt1.setString(++stcount, new_tax_amt1[i]);
							stmt1.setString(++stcount, desc_item[i]);
							
							stmt1.executeUpdate();
							stmt1.close();
						}
					}
						
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Not available for Modification!";
					msg_type="E";
				}
			}	
			
			url = "../other_invoice/frm_oth_inv_scrap_fixed_asset_crdr.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+opration+"&invoice_type="+invoice_type+"&invoice_seq="+invoice_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_yr+"&cr_dr_type="+inv_flag+"&month="+month+"&year="+year+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg="Error in Exception! Insert / Update Other Invoice Credit/Debit Note Detail Failed";
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
	
	private void ChkApprSFACrDr(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprSFACrDr()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accroid = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_flag = request.getParameter("cr_dr_type")==null?"":request.getParameter("cr_dr_type");
			String supp_nm = request.getParameter("supp_nm")==null?"":request.getParameter("supp_nm");
			String sel_inv_no =request.getParameter("sel_inv_no")==null?"":request.getParameter("sel_inv_no");
			String invdtl="";
			if(inv_flag.equals("CR"))
			{
				invdtl="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{
				invdtl="Debit Note";
			}
			if(operation.equalsIgnoreCase("CHECK"))
			{			
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, inv_flag);
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, inv_flag);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, inv_flag);
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, inv_flag);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - "+invdtl+" against Invoice "+sel_inv_no+" for "+supp_nm+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			url = "../other_invoice/rpt_view_chk_aprv_SFA_crdr_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accroid="+accroid+"&operation="+operation+"&invoice_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+
					"&financial_year="+fin_year+"&cr_dr_type="+inv_flag+"&sel_inv_no="+sel_inv_no+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - CR/DR Check/Approve Failed!";			
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


    private void InsertUpdateRXP(HttpServletRequest request) throws SQLException 
	{

		String function_nm="InsertUpdateRXP()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String vend_cd = request.getParameter("vend_cd") == null ? "" : request.getParameter("vend_cd");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String period_start = request.getParameter("period_start") == null ? "" : request.getParameter("period_start");
			String period_end = request.getParameter("period_end") == null ? "" : request.getParameter("period_end");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String inv_dt = request.getParameter("inv_dt") == null ? "" : request.getParameter("inv_dt");
			String sale_unit = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String gross_amt = request.getParameter("total_amount") == null ? "" : request.getParameter("total_amount");
			String net_amt = request.getParameter("total_amt_inr") == null ? "" : request.getParameter("total_amt_inr");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String exist_fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String invoice_raised_in = request.getParameter("invoice_raised_in") == null ? "" : request.getParameter("invoice_raised_in");
			String invoice_category = request.getParameter("invoice_category") == null ? "" : request.getParameter("invoice_category");
			String total_cess = request.getParameter("total_cess") == null ? "" : request.getParameter("total_cess");
			String fin_yr = request.getParameter("financial_yr") == null ? "" : request.getParameter("financial_yr");
			String remark1 = request.getParameter("remark1") == null ? "" : request.getParameter("remark1");
			String port_of_loading = request.getParameter("port_of_loading") == null ? "" : request.getParameter("port_of_loading");
			String per_carriage = request.getParameter("per_carriage") == null ? "" : request.getParameter("per_carriage");
			String final_destination = request.getParameter("final_destination") == null ? "" : request.getParameter("final_destination");
			String reference_no = request.getParameter("reference_no") == null ? "" : request.getParameter("reference_no");
			String port_of_discharge = request.getParameter("port_of_discharge") == null ? "" : request.getParameter("port_of_discharge");
			String country_of_origin = request.getParameter("country_of_origin") == null ? "" : request.getParameter("country_of_origin");
			String pay_terms = request.getParameter("pay_terms") == null ? "" : request.getParameter("pay_terms");
			String month= inv_dt.split("/")[1];
			String year=inv_dt.split("/")[2];
			period_start = utilDate.getFirstDateOfMonth(month, year);
			period_end = utilDate.getLastDateOfMonth(month, year);
			
			String sac_code[] = request.getParameterValues("sac_code");
			String desc_item[] = request.getParameterValues("desc_item");
			String uom[] = request.getParameterValues("uom");
			String qty[] = request.getParameterValues("qty");
			String rate[] = request.getParameterValues("rate");
			String amount[] = request.getParameterValues("amount");
			String dimensions[] = request.getParameterValues("dimensions");
			String net_wt[] = request.getParameterValues("net_wt");
			String gross_wt[] = request.getParameterValues("gross_wt");
			String pack_dtls[] = request.getParameterValues("pack_dtls");
			String sale_price = "";
			String new_financial_year=fin_yr;
			String new_invoice_seq=inv_seq;
			
			int inv_seq1 = 0;
			boolean isOperated=false,isOperated1=false;
			int count=0;
			
			if(operation.equalsIgnoreCase("INSERT"))
			{				
				fin_yr=utilDate.getFinancialYear(inv_dt);
				new_financial_year=fin_yr;
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE INVOICE_SEQ = ? AND COMPANY_CD=? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, inv_seq);
				stmt.setString(2, comp_cd);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_type);
				stmt.setString(5, supp_cd);
				rset = stmt.executeQuery();
				
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				else 
				{
					count = 1;
				}
				rset.close();
				stmt.close();
				
				if(count==0) 
				{
					query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, inv_type);
					stmt.setString(4, supp_cd);
					rset = stmt.executeQuery();
					if (rset.next()) 
					{
						inv_seq1 = rset.getInt(1);
					}
					else 
					{
						inv_seq1 = 1;
					}
					rset.close();
					stmt.close();
					
					inv_seq=""+inv_seq1;
					new_invoice_seq=inv_seq;
					
					int cnt=0;
					query1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,"
							+ "INVOICE_SEQ,INVOICE_NO,INVOICE_DT,PERIOD_START_DT,PERIOD_END_DT,SALE_AMT,GROSS_AMT,"
							+ "NET_PAYABLE,INVOICE_RAISED_IN,ENT_BY,ENT_DT,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG,REMARK) "
							+ "VALUES(?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,SYSDATE,?,?,?,?)";
						
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, fin_yr);
					stmt1.setString(++cnt, inv_seq1+"");
					stmt1.setString(++cnt, inv_no);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, "V");
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, remark1);
			   		stmt1.executeUpdate();
			   		
			   		stmt1.close();
			   		isOperated=true;
			   		
			   		msg = "Successful! - ReExport Invoice from "+supp_abbr+" to "+ vend_abbr+" Generated!";
					msg_type="S";
				}
				else 
				{
					msg = "Failed! - ReExport Invoice from "+supp_abbr+" to "+ vend_abbr+" Already Generated!";
					msg_type="E";
				}
		   		
				if(isOperated)
				{
					if(invoice_category.equals("S"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,ITEM_AMT,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG,"
					   				+ "PER_CARRIAGE,FINAL_DESTINATION,COUNTRY_OF_ORIGIN,PORT_OF_DISCHARGE,PORT_OF_LOADING,"
					   				+ "PAY_TERMS,ITEM_DIMENSIONS,ITEM_NET_WT,ITEM_GROSS_WT,ITEM_PACK_DTLS,REFERENCE_NO) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?, "
					   				+ "?,?,?,?,?,?,?, "
					   				+ "?,SYSDATE,?,"
					   				+ "?,?,?,?,?,"
					   				+ "?,?,?,?,?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, +i+1);
							stmt1.setString(++cnt, inv_seq1+"");
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, fin_yr);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, per_carriage);
							stmt1.setString(++cnt, final_destination);
							stmt1.setString(++cnt, country_of_origin);
							stmt1.setString(++cnt, port_of_discharge);
							stmt1.setString(++cnt, port_of_loading);
							stmt1.setString(++cnt, pay_terms);
							stmt1.setString(++cnt, dimensions[i]);
							stmt1.setString(++cnt, net_wt[i]);
							stmt1.setString(++cnt, gross_wt[i]);
							stmt1.setString(++cnt, pack_dtls[i]);
							stmt1.setString(++cnt, reference_no);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
					}
				}
				inv_seq=""+inv_seq1;
			}
			else if(operation.equals("MODIFY")) 
			{
				
				query = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? AND INVOICE_SEQ = ? AND INV_FLAG = 'F'";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, exist_fin_yr);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, vend_cd);
				stmt.setString(6, inv_seq);
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0) 
				{
					String temp_fiscal_yr=utilDate.getFinancialYear(inv_dt);
					if(!exist_fin_yr.equals(temp_fiscal_yr) && !temp_fiscal_yr.equals("") && !exist_fin_yr.equals(""))
					{
						new_financial_year=temp_fiscal_yr;
						
						query = "SELECT NVL(MAX(INVOICE_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, new_financial_year);
						stmt.setString(3, inv_type);
						stmt.setString(4, supp_cd);
						rset = stmt.executeQuery();
						if (rset.next()) 
						{
							inv_seq1 = rset.getInt(1);
						}
						else 
						{
							inv_seq1 = 1;
						}
						rset.close();
						stmt.close();
						
						new_invoice_seq = ""+inv_seq1;
					}

					int cnt=0;
					query1="UPDATE FMS_OTH_INVOICE_MST SET INVOICE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "SALE_AMT=?, GROSS_AMT=?, "
							+ "NET_PAYABLE=?, INVOICE_RAISED_IN=?, MODIFY_BY=?, MODIFY_DT=SYSDATE,INVOICE_CATEGORY=?,"
							+ "FINANCIAL_YEAR=?,INVOICE_SEQ=?,PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY'),PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY'),REMARK=? "
							+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? "
							+ "AND INVOICE_TYPE = ? AND INV_FLAG = ? AND VENDOR_TYPE = ?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, inv_dt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, gross_amt);
					stmt1.setString(++cnt, net_amt);
					stmt1.setString(++cnt, invoice_raised_in);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, invoice_category);
					stmt1.setString(++cnt, new_financial_year);
					stmt1.setString(++cnt, new_invoice_seq);
					stmt1.setString(++cnt, period_start);
					stmt1.setString(++cnt, period_end);
					stmt1.setString(++cnt, remark1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, vend_cd);
					stmt1.setString(++cnt, inv_seq);
					stmt1.setString(++cnt, exist_fin_yr);
					stmt1.setString(++cnt, inv_type);
					stmt1.setString(++cnt, "F");
					stmt1.setString(++cnt, "V");
					stmt1.executeUpdate();
					isOperated1=true;

					stmt1.close();

					msg = "Successful! - ReExport Invoice from "+supp_abbr+" to "+ vend_abbr+" Modified!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - ReExport Invoice from "+supp_abbr+" to "+ vend_abbr+" not found for Modification!";
					msg_type="E";
				}
				
				if(isOperated1)
				{
					query2="DELETE FROM FMS_OTH_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
							+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? AND INV_FLAG = ?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, exist_fin_yr);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, supp_cd);
					stmt2.setString(5, inv_seq);
					stmt2.setString(6, "F");
					stmt2.executeUpdate();
					
					stmt2.close();
					
					if(invoice_category.equals("S"))
					{
						for(int i = 0; i<amount.length;i++)
						{
							int cnt=0;
					   		query1 = "INSERT INTO FMS_OTH_INVOICE_DTL(COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_SEQ,INVOICE_NO,ITEM_DESCRIPTION,"
					   				+ "SALE_PRICE,SALE_PRICE_UNIT,FINANCIAL_YEAR,HSN_CODE,QUANTITY,UAM_NO,ITEM_AMT,"
					   				+ "ENT_BY,ENT_DT,INV_FLAG,"
					   				+ "PER_CARRIAGE,FINAL_DESTINATION,COUNTRY_OF_ORIGIN,PORT_OF_DISCHARGE,PORT_OF_LOADING,"
					   				+ "PAY_TERMS,ITEM_DIMENSIONS,ITEM_NET_WT,ITEM_GROSS_WT,ITEM_PACK_DTLS,REFERENCE_NO) "
					   				+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,"
					   				+ "?,?,?,?,?,?,?,"
					   				+ "?,SYSDATE,?,"
					   				+ "?,?,?,?,?,"
					   				+ "?,?,?,?,?,?) ";
					   		stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++cnt, comp_cd);
							stmt1.setString(++cnt, supp_cd);
							stmt1.setString(++cnt, vend_cd);
							stmt1.setString(++cnt, inv_type);
							stmt1.setString(++cnt, inv_dt);
							stmt1.setInt(++cnt, i+1);
							stmt1.setString(++cnt, new_invoice_seq);
							stmt1.setString(++cnt, inv_no);
							stmt1.setString(++cnt, desc_item[i]);
							stmt1.setString(++cnt, rate[i]);
							stmt1.setString(++cnt, sale_unit);
							stmt1.setString(++cnt, new_financial_year);
							stmt1.setString(++cnt, sac_code[i]);
							stmt1.setString(++cnt, qty[i]);
							stmt1.setString(++cnt, uom[i]);
							stmt1.setString(++cnt, amount[i]);
							stmt1.setString(++cnt, emp_cd);
							stmt1.setString(++cnt, "F");
							stmt1.setString(++cnt, per_carriage);
							stmt1.setString(++cnt, final_destination);
							stmt1.setString(++cnt, country_of_origin);
							stmt1.setString(++cnt, port_of_discharge);
							stmt1.setString(++cnt, port_of_loading);
							stmt1.setString(++cnt, pay_terms);
							stmt1.setString(++cnt, dimensions[i]);
							stmt1.setString(++cnt, net_wt[i]);
							stmt1.setString(++cnt, gross_wt[i]);
							stmt1.setString(++cnt, pack_dtls[i]);
							stmt1.setString(++cnt, reference_no);
							stmt1.executeUpdate();
					   		
					   		stmt1.close();
						}
					}
				}
			}
			
			if(operation.equals("INSERT")) 
			{
				operation="MODIFY";
			}
			
			dbcon.commit();
			
			url = "../other_invoice/frm_oth_inv_re_export.jsp?msg="+msg+"&msg_type="+msg_type+"&vendor_cd="+vendor_cd+"&accord="+accord+"&operation="+operation+"&inv_type="+inv_type+"&invoice_seq="+inv_seq+"&supp_cd="+supp_cd+"&fin_year="+fin_yr+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - ReExport Addition/Modification Failed!";			
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
	
	private void ChkApprRXP(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ChkApprRXP()";

		msg="";
		msg_type="";
		url="";
		String operation="";
		String vendor_cd="0";
		
		try
		{
			operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			String inv_no = request.getParameter("inv_no") == null ? "" : request.getParameter("inv_no");
			String accord = request.getParameter("accord") == null ? "" : request.getParameter("accord");
			String rd = request.getParameter("rd")==null?"":request.getParameter("rd");
			String inv_type = request.getParameter("inv_type") == null ? "" : request.getParameter("inv_type");
			String inv_seq = request.getParameter("inv_seq") == null ? "" : request.getParameter("inv_seq");
			String fin_year = request.getParameter("fin_year") == null ? "" : request.getParameter("fin_year");
			String supp_cd = request.getParameter("supp_cd") == null ? "" : request.getParameter("supp_cd");
			String inv_id_seq = request.getParameter("invoice_id_seq") == null ? "" : request.getParameter("invoice_id_seq");
			String vend_abbr = request.getParameter("vend_abbr") == null ? "" : request.getParameter("vend_abbr");
			String supp_abbr = request.getParameter("supp_abbr") == null ? "" : request.getParameter("supp_abbr");
			
			
			if(operation.equalsIgnoreCase("CHECK"))
			{				   
				String query="UPDATE FMS_OTH_INVOICE_MST SET CHECKED_FLAG=?,CHECKED_BY=?,CHECKED_DT=SYSDATE "
						+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? "
						+ "AND SUPPLIER_CD=? AND INV_FLAG = ?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, rd);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, inv_seq);
				stmt.setString(5, inv_type);
				stmt.setString(6, fin_year);
				stmt.setString(7, supp_cd);
				stmt.setString(8, "F");
				stmt.executeUpdate();

				stmt.close();
				
				msg = "Successful! - ReExport Invoice from "+supp_abbr+" to "+ vend_abbr+" Checked!";
				msg_type="S";
			}
			else if(operation.equalsIgnoreCase("APPROVE")) 
			{
				int count_inv_id_seq=0;
				String query0 = "SELECT COUNT(INVOICE_SEQ) "
						+ "FROM FMS_OTH_INVOICE_MST "
						+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
						+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_ID_SEQ = ? AND INV_FLAG = ?";
				stmt = dbcon.prepareStatement(query0);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fin_year);
				stmt.setString(3, inv_type);
				stmt.setString(4, supp_cd);
				stmt.setString(5, inv_seq);
				stmt.setString(6, inv_id_seq);
				stmt.setString(7, "F");
				rset = stmt.executeQuery();
				if (rset.next()) 
				{
					count_inv_id_seq = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count_inv_id_seq==0)
				{
					String query="UPDATE FMS_OTH_INVOICE_MST SET APPROVED_FLAG=?,APPROVED_BY=?,APPROVED_DT=SYSDATE,INVOICE_NO=?,INVOICE_ID_SEQ=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, rd);
					stmt.setString(2, emp_cd);
					stmt.setString(3, inv_no);
					stmt.setString(4, inv_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, inv_seq);
					stmt.setString(7, inv_type);
					stmt.setString(8, fin_year);
					stmt.setString(9, supp_cd);
					stmt.setString(10, "F");
					stmt.executeUpdate();
					stmt.close();
					
					String query1="UPDATE FMS_OTH_INVOICE_DTL SET INVOICE_NO=? "
							+ "WHERE COMPANY_CD=? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, inv_no);
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, inv_seq);
					stmt1.setString(4, inv_type);
					stmt1.setString(5, fin_year);
					stmt1.setString(6, supp_cd);
					stmt1.setString(7, "F");
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - ReExport Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" Approved!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - ReExport Invoice from "+supp_abbr+" to "+vend_abbr+" with Invoice No. "+inv_no+" is Already Allocated, Please assign different Invoice No!";
					msg_type="E";
				}
				
				
			}
			
			url = "../other_invoice/rpt_re_export_chk_aprv_dtl.jsp?vendor_cd="+vendor_cd+"&inv_no="+inv_no+"&accord="+accord+"&operation="+operation+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			msg = "Error in Exception! - ReExport Check/Approve Failed!";			
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
	
	
}
