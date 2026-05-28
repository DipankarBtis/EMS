package com.etrm.fms.contract_master;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 18/10/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_ContracMaster")
public class Frm_ContracMaster extends HttpServlet
{
	static String db_src_file_name="Frm_ContracMaster.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_ContracMaster";
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
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	public static String sysdate = utilDate.getSysdate();
	
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
					
					if(option.equalsIgnoreCase("AGREEMENT_MST") || option.equalsIgnoreCase("DLNG_AGREEMENT_MST"))
					{
						InsertUpdateAgreementDetail(request);
					}
					/*
					 * else if(option.equalsIgnoreCase("DLNG_AGREEMENT_MST")) {
					 * InsertUpdateDlngAgreementDetail(request); }
					 */
					else if(option.equalsIgnoreCase("TRADER_CONT_LINK_TRANS"))
					{
						//TraderContractLinkTrans(request);
					}
					else if(option.equalsIgnoreCase("AGREEMENT_BILLING_DTL"))
					{
						InsertUpdateAgreementBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("SUPPLY_CONT_MST") || option.equalsIgnoreCase("DLNG_CONT_MST"))
					{
						InsertUpdateSupplyContractDetail(request);
					}
					else if(option.equalsIgnoreCase("SUPPLY_CONT_FCC") || option.equalsIgnoreCase("DLNG_CONT_FCC"))
					{
						FCCforSupplyContractDetail(request);
					}
					else if(option.equalsIgnoreCase("CONTRACT_BILLING_DTL"))
					{
						InsertUpdateContractBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("VARIABLE_DCQ"))
					{
						InsertUpdateVariableDcqDetail(request);
					}
					else if(option.equalsIgnoreCase("Exp_sales")) //Method created by Arth Patel On 27th june 2023 
					{
						InsertUpdateExpSalesDetail(request);
					}
					else if(option.equalsIgnoreCase("AGMT_LIABILITY_CLAUSE"))
					{
						InsertUpdateAgmtLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("CONT_LIABILITY_CLAUSE"))
					{
						InsertUpdateContLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("REJECT_CLOSURE"))
					{
						RejectClosureRequest(request);
					}
					else if(option.equalsIgnoreCase("REOPEN"))
					{
						RejectOrApporveReopenRequest(request);
					}
					else if(option.equalsIgnoreCase("Activate_AGMT"))
					{
						RejectOrApproveActivationRequest(request);
					}
					if(option.equalsIgnoreCase("CONTRACT_DURATION_MODIFICTION") || option.equalsIgnoreCase("DLNG_CONTRACT_DURATION_MODIFICTION"))
					{
						modifyContractDuration(request);
					}
					else if(option.equalsIgnoreCase("CONTRACT_DURATION_MODIFICATION_APPROVE") || option.equalsIgnoreCase("DLNG_CONTRACT_DURATION_MODIFICATION_APPROVE"))
					{
						modifyContractDurationApproval(request);
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void modifyContractDuration(HttpServletRequest request) throws SQLException 
	{
		String function_nm="modifyContractDuration()";

		String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
		try
		{
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,customer_cd);
			HttpSession session = request.getSession();
			
			String agmt_no[] = request.getParameterValues("agmt_no");
			String agmt_rev[] = request.getParameterValues("agmt_rev");
			String cont_no[] = request.getParameterValues("cont_no");
			String cont_rev[] = request.getParameterValues("cont_rev");
			String cont_type[] = request.getParameterValues("cont_type");
			String segment[] = request.getParameterValues("segment");
			
			String new_start_dt[] = request.getParameterValues("new_start_dt");
			String new_end_dt[] = request.getParameterValues("new_end_dt");
			
			if(cont_no != null)
			{
				for(int i=0;i<cont_no.length; i++)
				{
					String cont_name_map = counterparty_abbr+"-"+cont_type[i]+agmt_no[i]+"-"+cont_no[i];
					query1="SELECT COUNT(*) FROM FMS_SUPPLY_CONT_MST "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=? AND CHANGE_DATE_REQ=? "
							+ "AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) ";
					stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, agmt_no[i]);
					stmt1.setString(3, agmt_rev[i]);
					stmt1.setString(4, cont_no[i]);
					stmt1.setString(5, customer_cd);
					stmt1.setString(6, cont_type[i]);
					stmt1.setString(7, cont_rev[i]);
					stmt1.setString(8, "Y");
					stmt1.setString(9, "N");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						if(rset1.getInt(1) > 0)
						{
							query = "UPDATE FMS_SUPPLY_CONT_MST SET CHANGE_START_DT=TO_DATE(?,'DD/MM/YYYY'), CHANGE_END_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_BY=?,MODIFY_DT=SYSDATE "
									+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
									+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
									+ "AND CONT_REV=? AND CHANGE_DATE_REQ=? "
									+ "AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) ";
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, new_start_dt[i]);
							stmt.setString(2, new_end_dt[i]);
							stmt.setString(3, emp_cd);
							stmt.setString(4, comp_cd);
							stmt.setString(5, agmt_no[i]);
							stmt.setString(6, agmt_rev[i]);
							stmt.setString(7, cont_no[i]);
							stmt.setString(8, customer_cd);
							stmt.setString(9, cont_type[i]);
							stmt.setString(10, cont_rev[i]);
							stmt.setString(11, "Y");
							stmt.setString(12, "N");
							stmt.executeUpdate();
							
							stmt.close();
							
							generateSupplyContractLog(customer_cd,cont_no[i],cont_rev[i],agmt_no[i],agmt_rev[i],cont_type[i],emp_cd);
						}
					}
					rset1.close();
					stmt1.close();
					
					msg="Contract "+cont_name_map+" Duration Change Request initiated";
					msg_type="S";
					dbcon.commit();
				}
			}
			
			if(option.equalsIgnoreCase("DLNG_CONTRACT_DURATION_MODIFICTION"))
			{
				url="../contract_master/frm_dlng_cont_duration_modification.jsp?customer_cd="+customer_cd+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url="../contract_master/frm_contract_duration_modification.jsp?customer_cd="+customer_cd+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Duration Change Request initiation Failed";
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
	
	private void modifyContractDurationApproval(HttpServletRequest request) throws SQLException 
	{
		String function_nm="modifyContractDurationApproval()";

		String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");

		try
		{
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,customer_cd);
			HttpSession session = request.getSession();
			String user_cd = (String)session.getAttribute("user_cd")==null?"":(String)session.getAttribute("user_cd");
			
			String agmt_no[] = request.getParameterValues("agmt_no");
			String agmt_rev[] = request.getParameterValues("agmt_rev");
			String cont_no[] = request.getParameterValues("cont_no");
			String cont_rev[] = request.getParameterValues("cont_rev");
			String cont_type[] = request.getParameterValues("cont_type");
			String segment[] = request.getParameterValues("segment");
			String temp_start_dt[] = request.getParameterValues("start_dt");
			
			String new_start_dt[] = request.getParameterValues("new_start_dt");
			String new_end_dt[] = request.getParameterValues("new_end_dt");
			
			String action = request.getParameter("action")==null?"":request.getParameter("action");
			
			if(cont_no != null)
			{
				for(int i=0;i<cont_no.length; i++)
				{
					String cont_name_map = counterparty_abbr+"-"+cont_type[i]+agmt_no[i]+"-"+cont_no[i];
					if(action.equals("reject"))
					{
						query = "UPDATE FMS_SUPPLY_CONT_MST SET CHANGE_DATE_REQ=?,CHANGE_START_DT=?,CHANGE_END_DT=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "N");
						stmt1.setString(2, "");
						stmt1.setString(3, "");
						stmt1.setString(4, emp_cd);
						stmt1.setString(5, comp_cd);
						stmt1.setString(6, agmt_no[i]);
						stmt1.setString(7, agmt_rev[i]);
						stmt1.setString(8, cont_no[i]);
						stmt1.setString(9, cont_rev[i]);
						stmt1.setString(10, customer_cd);
						stmt1.setString(11, cont_type[i]);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						generateSupplyContractLog(customer_cd,cont_no[i],cont_rev[i],agmt_no[i],agmt_rev[i],cont_type[i],emp_cd);
						
						dbcon.commit();
						if(segment[i].equals("DLNG"))
						{
							msg = "DLNG Contract Duration Change Request for "+cont_name_map+" Rejected !!!";
						}
						else
						{
							msg = "Sale Contract Duration Change Request for "+cont_name_map+" Rejected !!!";
						}
						msg_type="S";
					}
					else
					{
						query = "UPDATE FMS_SUPPLY_CONT_MST SET CHANGE_DATE_APPROVE=?,CHANGE_DATE_REQ=?,"
								+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),CHANGE_START_DT=?,CHANGE_END_DT=?,"
								+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, "Y");
						stmt1.setString(2, "N");
						stmt1.setString(3, new_start_dt[i]);
						stmt1.setString(4, new_end_dt[i]);
						stmt1.setString(5, "");
						stmt1.setString(6, "");
						stmt1.setString(7, emp_cd);
						stmt1.setString(8, comp_cd);
						stmt1.setString(9, agmt_no[i]);
						stmt1.setString(10, agmt_rev[i]);
						stmt1.setString(11, cont_no[i]);
						stmt1.setString(12, cont_rev[i]);
						stmt1.setString(13, customer_cd);
						stmt1.setString(14, cont_type[i]);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						generateSupplyContractLog(customer_cd,cont_no[i],cont_rev[i],agmt_no[i],agmt_rev[i],cont_type[i],emp_cd);
						
						if(!temp_start_dt[i].equals(new_start_dt[i]) && !new_start_dt[i].equals(""))
						{
							updateSupplyContractBillingEffectiveDate(customer_cd, cont_no[i], agmt_no[i], agmt_rev[i], cont_type[i], temp_start_dt[i], new_start_dt[i]);
						}
						
						dbcon.commit();
						if(segment[i].equals("DLNG"))
						{
							msg = "DLNG Contract Duration Change Request for "+cont_name_map+" Approved !!!";
						}
						else
						{
							msg = "Sale Contract Duration Change Request for "+cont_name_map+" Approved !";
						}
						msg_type="S";
					}
				}
			}
			
			if(option.equalsIgnoreCase("DLNG_CONTRACT_DURATION_MODIFICATION_APPROVE"))
			{
				url="../contract_master/frm_dlng_cont_duration_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url="../contract_master/frm_contract_duration_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Duration Change Modification Failed";
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
	
	private void RejectOrApproveActivationRequest(HttpServletRequest request) throws SQLException
	{
		String function_nm="RejectOrApproveActivationRequest()";
		msg="";
		msg_type="";
		url="";
		try
		{
			/*	
			 Note:
			 	REOPEN_REQUEST_FLAG = 'Y'		REQUEST FOR ACTIVATION GENERATED 
			 	REOPEN_REQUEST_FLAG = 'N'		REQUEST FOR ACTIVATION REJECTED 
			 	REOPEN_REQUESST_FLAG = 'A'		REQUEST FOR ACTIVATION APPROVED 
			 	REOPEN_APPROVE_FLAG = 'Y'		REQUEST FOR ACTIVATION APPROVED
			 	REOPEN_APPROVE_FLAG = 'N'		REQUEST FOR ACTIVATION REJECTED
		    */
			
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String activate_note = request.getParameter("activate_note")==null?"":request.getParameter("activate_note");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String segment = request.getParameter("segment")==null?"0":request.getParameter("segment");
			String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
			
			String disp_name_map=utilBean.NewAgmtMappingId(comp_cd,customer_cd, agmt_no, agmt_rev_no, agmt_type);
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon, customer_cd);
			
			String reopen_request_flag="";
			String reopen_approve_flag="";
			if(opration.equals("APPROVE"))
			{
				reopen_request_flag="A";
				reopen_approve_flag="Y";
			}
			else if(opration.equals("REJECT"))
			{
				reopen_request_flag="N";
				reopen_approve_flag="N";
			}
			
			if(!customer_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agmt_type.equals(""))
			{
				int ctn=0;
				query="UPDATE FMS_AGMT_MST A "
					+ "SET REOPEN_APPROVAL_FLAG=?, REOPEN_APPROVAL_DT=SYSDATE,REOPEN_APPROVE_BY=?, "
					+ "REOPEN_REQUEST_FLAG=?,REMARK=? "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.AGMT_NO=? AND A.AGMT_TYPE=? "
					+ "AND A.AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE) AND A.REOPEN_REQUEST_FLAG=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(++ctn, reopen_approve_flag);
				stmt.setString(++ctn, emp_cd);
				stmt.setString(++ctn, reopen_request_flag);
				stmt.setString(++ctn, activate_note);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, customer_cd);
				stmt.setString(++ctn, agmt_no);
				stmt.setString(++ctn, agmt_type);
				stmt.setString(++ctn,"Y");
				stmt.executeUpdate();
				stmt.close();
				
				if(opration.equals("APPROVE"))
				{
					msg = "Successful!  Agreement ("+disp_name_map+") activation for "+counterparty_abbr+" submitted Successfully!";
				}
				else if(opration.equals("REJECT"))
				{
					msg = "Successful!  Agreement ("+disp_name_map+") activation rejection for "+counterparty_abbr+" submitted Successfully!";
				}
				msg_type = "S";
			}
			else
			{
				msg = "Failed!  Contract ("+disp_name_map+") Re-Open Rejection for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../contract_master/frm_cont_reopen_req.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+"&segmentType="+segmentType+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Activation Failed";
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
	
	private void RejectOrApporveReopenRequest(HttpServletRequest request) throws SQLException
	{
		String function_nm="RejectOrApporveReopenRequest()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cont_type = request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			//String delta_tcq_sign = request.getParameter("delta_tcq_sign")==null?"":request.getParameter("delta_tcq_sign");
			String delta_tcq = request.getParameter("delta_tcq")==null?"":request.getParameter("delta_tcq");
			String segment = request.getParameter("segment")==null?"0":request.getParameter("segment");
			String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
			
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, customer_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,customer_cd);
			
			if(opration.equals("REJECT"))
			{
				/*
			 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
				CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
				CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
				*/
				if(!comp_cd.equals("") && !customer_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_type.equals(""))
				{
					query = "UPDATE FMS_SUPPLY_CONT_MST A SET A.CLOSURE_REQUEST_FLAG=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=?   "
							+ "AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV)   "
							+ "FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO  "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,"X");
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//For storing the  contract reopen request reject log
					generateSupplyContractLog(customer_cd,cont_no,cont_rev_no,agmt_no,agmt_rev_no,cont_type,emp_cd);
					
					msg = "Successful!  Contract ("+cont_name_map+") Re-Open rejection for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed!  Contract ("+cont_name_map+") Re-Open Rejection for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(!comp_cd.equals("") && !customer_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_type.equals(""))
				{
					int  rev=0;
					queryString = "SELECT COUNT(*) FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=?   "
							+ "AND A.CONT_NO=? AND A.CONTRACT_TYPE=? ";
					
					stmt1 = dbcon.prepareStatement(queryString);
					stmt1.setString(1,comp_cd);
					stmt1.setString(2,customer_cd);
					stmt1.setString(3,agmt_no);
					stmt1.setString(4,agmt_rev_no);
					stmt1.setString(5,cont_no);
					stmt1.setString(6,cont_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						rev = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+cont_type+cont_no+"-REV"+rev;
					
					if(cont_type.equals("S"))
					{
						cont_name = counterparty_abbr+"-"+comp_abbr+"-FGSA"+agmt_no+"-REV"+agmt_rev_no+" "+cont_type+cont_no+"-REV"+rev;			
					}
					else if(cont_type.equals("F"))
					{
						cont_name = counterparty_abbr+"-"+comp_abbr+"-FLSA"+agmt_no+"-REV"+agmt_rev_no+" "+cont_type+cont_no+"-REV"+rev;			
					}
					
					query2="INSERT INTO FMS_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO"
							+ ",SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,POST_MARGIN,TRANSPORTATION_CHARGE"
							+ ",BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM"
							+ ",DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,REMARK,ENT_DT,ENT_BY,CONT_NAME,CONTRACT_TYPE,CONT_STATUS"
							+ ",IS_ALLOCATED,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG"
							+ ",TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,MEASUREMENT,MEAS_STANDARD"
							+ ",MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY"
							+ ",LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,"
							+ "MODIFY_BY,MODIFY_DT ";
					if(Double.doubleToRawLongBits(Double.parseDouble(delta_tcq))!=Double.doubleToRawLongBits(0))
					{
						query2+=",CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK";
					}
					query2+= ") "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,?,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ"
							+ ",QUANTITY_UNIT,RATE,RATE_UNIT,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM"
							+ ",SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,REMARK,ENT_DT,ENT_BY,?,CONTRACT_TYPE"
							+ ",?,IS_ALLOCATED,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_QTY"
							+ ",PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR"
							+ ",OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED"
							+ ",TERMINATE_FORCE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,?,SYSDATE ";
					if(Double.doubleToRawLongBits(Double.parseDouble(delta_tcq))!=Double.doubleToRawLongBits(0))
					{
						query2+=",CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK ";
					}
					query2+="FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.AGMT_NO = B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt2 = dbcon.prepareStatement(query2);
					stmt2.setString(1,""+rev);
					stmt2.setString(2,cont_name);
					stmt2.setString(3,"R");
					stmt2.setString(4,emp_cd);
					stmt2.setString(5,comp_cd);
					stmt2.setString(6,customer_cd);
					stmt2.setString(7,agmt_no);
					stmt2.setString(8,agmt_rev_no);
					stmt2.setString(9,cont_no);
					stmt2.setString(10,cont_type);
					stmt2.executeUpdate();
					stmt2.close();
					
					//for storing contract reopen request approve logs 
					generateSupplyContractLog(customer_cd,cont_no,""+rev,agmt_no,agmt_rev_no,cont_type,emp_cd);
					
					//Liability details 
					query="INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV, "
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER, "
							+ "TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP, "
							+ "MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT)  "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,?,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,"
							+ "LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,"
							+ "TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
							+ "MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,SYSDATE,?,SYSDATE  "
							+ "FROM FMS_SUPPLY_CONT_LIABILITY A  "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=?  "
							+ "AND A.CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV)  "
							+ "FROM FMS_SUPPLY_CONT_LIABILITY B  "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD   "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO  "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//Transporter Plant
					query="INSERT INTO FMS_SUPPLY_CONT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV, "
							+ "CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE, TRANSPORTER_CD, "
							+ "PLANT_SEQ_NO, ?, SYSDATE FROM FMS_SUPPLY_CONT_TRANSPTR A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_SUPPLY_CONT_TRANSPTR B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//Customer Plant
					query="INSERT INTO FMS_SUPPLY_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
							+ "CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE, PLANT_SEQ_NO, "
							+ "?, SYSDATE "
							+ "FROM FMS_SUPPLY_CONT_PLANT A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_PLANT B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//Business unit
					query="INSERT INTO FMS_SUPPLY_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, "
							+ "CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE, "
							+ "PLANT_SEQ_NO, ?, SYSDATE "
							+ "FROM FMS_SUPPLY_CONT_BU A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_SUPPLY_CONT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE )";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//GX Business unit 
					query = "INSERT INTO FMS_SUPPLY_CONT_GX_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
							+ "CONTRACT_TYPE,GX_BU_SEQ_NO, ENT_BY, ENT_DT,GX_COUNTERPARTY_CD) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE, GX_BU_SEQ_NO, "
							+ "?,SYSDATE,GX_COUNTERPARTY_CD "
							+ "FROM FMS_SUPPLY_CONT_GX_BU A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_GX_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE )";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,customer_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//THE TRUCK AND FILLING STATION IS HANDLED FOR DLNG ONLY
					if(cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
					{
						//for truck trans dtl
						query="INSERT INTO FMS_SUPPLY_CONT_TRUCK_TRANS(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV,  "
								+ "CONTRACT_TYPE, TRANSPORTER_CD, ENT_BY, ENT_DT)  "
								+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO,?,CONTRACT_TYPE,TRANSPORTER_CD,?,SYSDATE  "
								+ "FROM FMS_SUPPLY_CONT_TRUCK_TRANS A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=?  "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_TRUCK_TRANS B  "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1,""+rev);
						stmt.setString(2,emp_cd);
						stmt.setString(3,comp_cd);
						stmt.setString(4,customer_cd);
						stmt.setString(5,agmt_no);
						stmt.setString(6,agmt_rev_no);
						stmt.setString(7,cont_no);
						stmt.setString(8,cont_type);
						stmt.executeUpdate();
						stmt.close();
						
						//for filling station 
						query="INSERT INTO FMS_SUPPLY_CONT_FILLING_STN(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,  "
								+ "CONTRACT_TYPE, FILL_STATION_CD, ENT_BY, ENT_DT)  "
								+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO,?,CONTRACT_TYPE, FILL_STATION_CD,?, SYSDATE "
								+ "FROM FMS_SUPPLY_CONT_FILLING_STN A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=?  "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_FILLING_STN B  "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1,""+rev);
						stmt.setString(2,emp_cd);
						stmt.setString(3,comp_cd);
						stmt.setString(4,customer_cd);
						stmt.setString(5,agmt_no);
						stmt.setString(6,agmt_rev_no);
						stmt.setString(7,cont_no);
						stmt.setString(8,cont_type);
						stmt.executeUpdate();
						stmt.close();
					}
					
					msg = "Successful!  Contract ("+cont_name_map+") Re-Open approval for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed!  Contract ("+cont_name_map+") Re-Open for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
				
			}
			if(cont_type.equals("F")||cont_type.equals("E")||cont_type.equals("W"))
			{
				url = "../contract_master/frm_dlng_cont_reopen_req.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+"&segmentType="+segmentType+commonUrl_pra;
			}
			else
			{
				url = "../contract_master/frm_cont_reopen_req.jsp?msg="+msg+"&msg_type="+msg_type+"&segment="+segment+"&segmentType="+segmentType+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Contract Re-Open Failed";
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
	
	private void RejectClosureRequest(HttpServletRequest request) throws SQLException
	{
		String function_nm="RejectClosureRequest()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_type = request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, customer_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,customer_cd);
			
			if(!comp_cd.equals("") && !customer_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_type.equals(""))
			{
				String tcq_modification_flag="";
				String price_modification_flag="";
				String change_date_req_flag="";
				String fcc_flag="";
				String fcc_by="";
				String fcc_dt="";
				String cont_status="";
				String tcq_sign="";
				String tcq_req_close="";
				String tcq_req_qty="";
				String price_approve_flag="";
				
				/*
			 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
				CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
				CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
				*/
				
				query="SELECT TCQ_REQUEST_FLAG,PRICE_REQUEST_FLAG,CHANGE_DATE_REQ,FCC_FLAG,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY HH24:MI:SS'), "
						+ "CONT_STATUS,TCQ_SIGN,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY, "
						+ "PRICE_APPROVE_FLAG "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=?  "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV)  "
						+ "FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)-1";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1,comp_cd);
				stmt.setString(2,customer_cd);
				stmt.setString(3,agmt_no);
				stmt.setString(4,agmt_rev_no);
				stmt.setString(5,cont_no);
				stmt.setString(6,cont_type);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					tcq_modification_flag = rset.getString(1)==null?"":rset.getString(1);
					price_modification_flag = rset.getString(2)==null?"":rset.getString(2);
					change_date_req_flag = rset.getString(3)==null?"":rset.getString(3);
					fcc_flag = rset.getString(4)==null?"":rset.getString(4);
					fcc_by = rset.getString(5)==null?"":rset.getString(5);
					fcc_dt = rset.getString(6)==null?"":rset.getString(6);
					cont_status = rset.getString(7)==null?"":rset.getString(7);
					tcq_sign = rset.getString(8)==null?"":rset.getString(8);
					tcq_req_close = rset.getString(9)==null?"":rset.getString(9);
					tcq_req_qty = rset.getString(10)==null?"":rset.getString(10);
					price_approve_flag = rset.getString(11)==null?"":rset.getString(11);
				}
				rset.close();
				stmt.close();
				
				int ctn=0;
				query1="UPDATE FMS_SUPPLY_CONT_MST A SET A.CLOSURE_REQUEST_FLAG=?,A.TCQ_REQUEST_FLAG=?,A.TCQ_SIGN=?, "
						+ "A.TCQ_REQUEST_CLOSE=?,A.TCQ_REQUEST_QTY=?,A.PRICE_REQUEST_FLAG=?,A.PRICE_APPROVE_FLAG=?, "
						+ "A.CHANGE_DATE_REQ=?,A.FCC_FLAG=?,A.FCC_BY=?,A.FCC_DATE=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),A.CONT_STATUS=? "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=?  "
						+ "AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV)  "
						+ "FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++ctn, "N");
				stmt1.setString(++ctn, tcq_modification_flag);
				stmt1.setString(++ctn, tcq_sign);
				stmt1.setString(++ctn, tcq_req_close);
				stmt1.setString(++ctn, tcq_req_qty);
				stmt1.setString(++ctn, price_modification_flag);
				stmt1.setString(++ctn, price_approve_flag);
				stmt1.setString(++ctn, change_date_req_flag);
				stmt1.setString(++ctn, fcc_flag);
				stmt1.setString(++ctn, fcc_by);
				stmt1.setString(++ctn, fcc_dt);
				stmt1.setString(++ctn, cont_status);
				stmt1.setString(++ctn,comp_cd);
				stmt1.setString(++ctn,customer_cd);
				stmt1.setString(++ctn,agmt_no);
				stmt1.setString(++ctn,agmt_rev_no);
				stmt1.setString(++ctn,cont_no);
				stmt1.setString(++ctn,cont_type);
				stmt1.executeUpdate();
				stmt1.close();
				
				//For storing the closure request reject logs... 
				generateSupplyContractLog(customer_cd,cont_no,cont_rev_no,agmt_no,agmt_rev_no,cont_type,emp_cd);
				
				msg = "Successful!  Contract ("+cont_name_map+") Closure rejection for "+counterparty_abbr+" submitted Successfully!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed!  Contract ("+cont_name_map+") Closure Rejection for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			if(cont_type.equals("F")||cont_type.equals("E")||cont_type.equals("W"))
			{
				url = "../contract_master/frm_dlng_cont_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url = "../contract_master/frm_cont_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Contract Closure Failed";
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
	
	private void InsertUpdateContLiabilityDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCnLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		String cont_type_nm = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String ld_price_per = request.getParameter("ld_price_per")==null?"":request.getParameter("ld_price_per");
			String ld_promise = request.getParameter("ld_promise")==null?"":request.getParameter("ld_promise");
			String ld_low_per = request.getParameter("ld_low_per")==null?"":request.getParameter("ld_low_per");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String ld_chk = request.getParameter("ld_chk")==null?"":request.getParameter("ld_chk");
			
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String top_price_per = request.getParameter("top_price_per")==null?"":request.getParameter("top_price_per");
			String top_promise = request.getParameter("top_promise")==null?"":request.getParameter("top_promise");
			String top_per = request.getParameter("top_per")==null?"":request.getParameter("top_per");
			String top_chk = request.getParameter("top_chk")==null?"":request.getParameter("top_chk");
			String top_obligation = request.getParameter("top_obligation")==null?"":request.getParameter("top_obligation");
			String top_remark = request.getParameter("top_remark")==null?"":request.getParameter("top_remark");
			
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String mug_price_per = request.getParameter("mug_price_per")==null?"":request.getParameter("mug_price_per");
			String mug_period_per = request.getParameter("mug_period_per")==null?"":request.getParameter("mug_period_per");
			String recovery_day = request.getParameter("recovery_day")==null?"":request.getParameter("recovery_day");
			String mug_remark = request.getParameter("mug_remark")==null?"":request.getParameter("mug_remark");
			
			String ld_from = request.getParameter("ld_from")==null?"":request.getParameter("ld_from");
			String ld_to = request.getParameter("ld_to")==null?"":request.getParameter("ld_to");
			String top_from = request.getParameter("top_from")==null?"":request.getParameter("top_from");
			String top_to = request.getParameter("top_to")==null?"":request.getParameter("top_to");
			String cont_price = request.getParameter("cont_price")==null?"":request.getParameter("cont_price");
			String cont_price_unit = request.getParameter("cont_price_unit")==null?"":request.getParameter("cont_price_unit");
			String cont_tcq = request.getParameter("cont_tcq")==null?"":request.getParameter("cont_tcq");
			String cont_dcq = request.getParameter("cont_dcq")==null?"":request.getParameter("cont_dcq");
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			cont_type_nm = utilBean.getContractTypeName(contract_type);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_SUPPLY_CONT_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_rev_no);
				stmt.setString(7, contract_type);
				rset = stmt.executeQuery();
				count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					int cnt = 0;
					query="UPDATE FMS_SUPPLY_CONT_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE, "
							+ "LQ_DAMG_FROM=TO_DATE(?,'DD/MM/YYYY'),LQ_DAMG_TO=TO_DATE(?,'DD/MM/YYYY'),TAKE_PAY_FROM=TO_DATE(?,'DD/MM/YYYY'),TAKE_PAY_TO=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=?";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, ld_from);
					stmt1.setString(++cnt, ld_to);
					stmt1.setString(++cnt, top_from);
					stmt1.setString(++cnt, top_to);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+cont_type_nm+" Contract ("+cont_name_map+") Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT,LQ_DAMG_FROM,LQ_DAMG_TO,TAKE_PAY_FROM,TAKE_PAY_TO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'))";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, ld_from);
					stmt1.setString(++cnt, ld_to);
					stmt1.setString(++cnt, top_from);
					stmt1.setString(++cnt, top_to);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+cont_type_nm+" Contract ("+cont_name_map+") Liability Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! "+cont_type_nm+" Contract ("+cont_name_map+") Liability Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../contract_master/frm_cont_liability_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+"&tcq="+cont_tcq+"&dcq="+cont_dcq+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&contract_type="+contract_type+"&start_dt="+start_dt+"&rate="+cont_price+"&rate_unit="+cont_price_unit+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Gas Supply Contract Liability Insert/Update Failed";
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
	
	private void InsertUpdateAgmtLiabilityDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAgmtLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"M":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String ld_price_per = request.getParameter("ld_price_per")==null?"":request.getParameter("ld_price_per");
			String ld_promise = request.getParameter("ld_promise")==null?"":request.getParameter("ld_promise");
			String ld_low_per = request.getParameter("ld_low_per")==null?"":request.getParameter("ld_low_per");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String ld_chk = request.getParameter("ld_chk")==null?"":request.getParameter("ld_chk");
			
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String top_price_per = request.getParameter("top_price_per")==null?"":request.getParameter("top_price_per");
			String top_promise = request.getParameter("top_promise")==null?"":request.getParameter("top_promise");
			String top_per = request.getParameter("top_per")==null?"":request.getParameter("top_per");
			String top_chk = request.getParameter("top_chk")==null?"":request.getParameter("top_chk");
			String top_obligation = request.getParameter("top_obligation")==null?"":request.getParameter("top_obligation");
			String top_remark = request.getParameter("top_remark")==null?"":request.getParameter("top_remark");
			
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String mug_price_per = request.getParameter("mug_price_per")==null?"":request.getParameter("mug_price_per");
			String mug_period_per = request.getParameter("mug_period_per")==null?"":request.getParameter("mug_period_per");
			String recovery_day = request.getParameter("recovery_day")==null?"":request.getParameter("recovery_day");
			String mug_remark = request.getParameter("mug_remark")==null?"":request.getParameter("mug_remark");
			
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_SUPPLY_AGMT_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				rset = stmt.executeQuery();
				count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					int cnt = 0;
					query="UPDATE FMS_SUPPLY_AGMT_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_SUPPLY_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT) "
							+ "VALUES(?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE)";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! - "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../contract_master/frm_agmt_liability_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Liability Insert/Update Failed";
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
	
	// Below Method is created by Arth Patel on 27th june 2023 for Contract Wise report
	private void InsertUpdateExpSalesDetail(HttpServletRequest request)throws SQLException 
	{
		String function_nm="InsertUpdateExpSalesDetail()";
		msg="";
		msg_type="";
		url="";
		approved_flag="";
		
		try
		{
			String[] counterparty_cd = request.getParameterValues("counterpartyCd");
			String[] agmt_no = request.getParameterValues("agmt_no");
			String[] agmt_rev = request.getParameterValues("agmt_rev");
			String[] cont_no = request.getParameterValues("cont_no");
			String[] cont_rev = request.getParameterValues("cont_rev");
			String[] contract_type = request.getParameterValues("contract_type");
			String[] Month = request.getParameterValues("Month");
			String[] Year = request.getParameterValues("Year");
			String[] tcq = request.getParameterValues("temp_tcq");
			String[] supplied_qty = request.getParameterValues("temp_supplied_qty");
			String[] balance_qty = request.getParameterValues("temp_balance_qty");
			String[] sales_rate = request.getParameterValues("temp_sales_rate");
			String[] rate_unit = request.getParameterValues("temp_rate_unit");
			String[] sales_amt_inr = request.getParameterValues("temp_sales_amt_inr");
			String[] sales_amt_usd = request.getParameterValues("temp_sales_amt_usd");
			String[] approve_flag = request.getParameterValues("approve_flag");
			String formCd = request.getParameter("formCd")==null?"":request.getParameter("formCd");
			String formNm = request.getParameter("formNm")==null?"":request.getParameter("formNm");
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			
			if(counterparty_cd != null) 
				//if(!counterparty_cd.equals("")) 
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					query = "INSERT INTO FMS_EXP_SALES_DTLS(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MONTH,YEAR,"
						  + "TCQ,SUPPLIED_QTY,BALANCE_QTY,EXP_SALES_RATE,RATE_UNIT,EXP_SALES_AMT_INR,EXP_SALES_AMT_USD,"
						  + "APPROVE_FLAG,APPROVE_BY,APPROVE_DT) "
						  + "VALUES(?,?,?,?,?,?,?,"
						  + "?,?,?,?,?,?,"
						  + "?,?,?,"
						  + "?,?, SYSDATE)";
					stmt = dbcon.prepareStatement(query); 
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd[i]);
					stmt.setString(3, agmt_no[i]);
					stmt.setString(4, agmt_rev[i]);
					stmt.setString(5, cont_no[i]);
					stmt.setString(6, cont_rev[i]);
					stmt.setString(7, contract_type[i]);
					stmt.setString(8, month);
					stmt.setString(9, year);
					stmt.setString(10, tcq[i]);
					stmt.setString(11, supplied_qty[i]);
					stmt.setString(12, balance_qty[i]);
					stmt.setString(13, sales_rate[i]);
					stmt.setString(14, rate_unit[i]);
					stmt.setString(15, sales_amt_inr[i]);
					stmt.setString(16, sales_amt_usd[i]);
					stmt.setString(17, approve_flag[i]);
					stmt.setString(18, emp_cd);
					stmt.executeUpdate();
					
					stmt.close();
				}
			}
			
		    msg = "Approved Sales Figure Inserted Successfully !";
			approved_flag = "Y";
			
			url = "../contract_master/rpt_contract_wise.jsp?msg="+msg+"&approved_flag="+approved_flag+"&month="+month+"&year="+year+commonUrl_pra;;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Expected Sales Figure submission Failed";
		}
		
	}
	
	private void InsertUpdateAgreementDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAgreementDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String mdcq = request.getParameter("mdcq")==null?"":request.getParameter("mdcq");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals("")) {
				status="A";
			}
			
			String[] chk_plant = request.getParameterValues("chk_plant");

			String[] chk_fill_station = request.getParameterValues("chk_fill_station");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");

			String[] chk_truck_trans = request.getParameterValues("chk_truck_trans");
			String[] truck_trans_cd = request.getParameterValues("truck_trans_cd");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
			String measurement = request.getParameter("measurementCheckbox")==null?"":request.getParameter("measurementCheckbox");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			String off_spec_gas = request.getParameter("off_spec_gas_checkbox")==null?"":request.getParameter("off_spec_gas_checkbox");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			String liability = request.getParameter("liability_checkbox")==null?"":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			String billing_clause = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String terminate_flag = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			String reopen_request_flag = request.getParameter("reopen_request_flag")==null?"":request.getParameter("reopen_request_flag");
			String reopen_request_by = request.getParameter("reopen_request_by")==null?"":request.getParameter("reopen_request_by");
			String reopen_request_dt = request.getParameter("reopen_request_dt")==null?"":request.getParameter("reopen_request_dt");
			String reopen_approval_flag = request.getParameter("reopen_approval_flag")==null?"":request.getParameter("reopen_approval_flag");
			String reopen_approve_by = request.getParameter("reopen_approve_by")==null?"":request.getParameter("reopen_approve_by");
			String reopen_approval_dt = request.getParameter("reopen_approval_dt")==null?"":request.getParameter("reopen_approval_dt");
			//String reopen_remark = request.getParameter("reopen_remark")==null?"":request.getParameter("reopen_remark");
			boolean reactivation_flag=false;
			if(reopen_approval_flag.equals("Y"))
			{
				reactivation_flag=true;
				reopen_request_flag="";
				reopen_request_by="";
				reopen_request_dt="";
				reopen_approval_flag="";
				reopen_approve_by="";
				reopen_approval_dt="";
			}
			String meas_clause = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String demurage_clause = request.getParameter("demurrage_clause_no")==null?"":request.getParameter("demurrage_clause_no");
			String spec_clause = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			String buy_clause_no = request.getParameter("buy_clause_no")==null?"":request.getParameter("buy_clause_no");
			String sell_clause_no = request.getParameter("sell_clause_no")==null?"":request.getParameter("sell_clause_no");
			String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"":request.getParameter("day_def_clause_no");
			String mdcq_clause_no = request.getParameter("mdcq_clause_no")==null?"":request.getParameter("mdcq_clause_no");
			
			String buy_m = "N";
			String buy_f ="N";
			String buy_w ="N";
			String buy_d = "N";
			
			if(buyer_nom.equalsIgnoreCase("Y"))
			{
				if(chk_buyer_nom!=null)
				{
					for(int i=0;i<chk_buyer_nom.length;i++)
					{
						if(chk_buyer_nom[i].equalsIgnoreCase("M"))
						{
							buy_m = "Y"; 
			    		}
						else if(chk_buyer_nom[i].equalsIgnoreCase("F"))
						{
							buy_f = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("W"))
						{
							buy_w = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("D"))
						{
							buy_d = "Y";
						}
					}
				}
			}
			
			String sel_m = "N";
			String sel_f = "N";
			String sel_w ="N";
			String sel_d = "N";
			
			if(seller_nom.equalsIgnoreCase("Y"))
			{
				if(chk_seller_nom!=null)
				{
					for(int i=0;i<chk_seller_nom.length;i++)
					{
						if(chk_seller_nom[i].equalsIgnoreCase("M"))
						{
							sel_m = "Y"; 
			    		}
						else if(chk_seller_nom[i].equalsIgnoreCase("F"))
						{
							sel_f = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("W"))
						{
							sel_w = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("D"))
						{
							sel_d = "Y";
						}
					}
				}	
			}
			
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			if(opration.equals("INSERT"))
			{
				query="SELECT MAX(AGMT_NO) FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					agmt_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					agmt_no = "1";
				}
				agmt_rev_no="0";
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_type);
					stmt.setString(4, agmt_rev_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						agmt_rev_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						agmt_rev_no = "0";
					}
					rset.close();
					stmt.close();
				}
			}
			
			String cont_name = comp_abbr+"-"+counterparty_abbr+"-"+agmt_type+agmt_no+"-REV"+agmt_rev_no;
			cont_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agmt_type);
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
			
				if(rev_count > 0)
				{
					int count=0;
					query ="UPDATE FMS_AGMT_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "AGMT_TYP=?,BUYER_NOM=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ=?,MDCQ_PERCENTAGE=?,"
							//+ "REMARK=?,CONT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "CONT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),BUYER_FORNGT_NOM=?,"
							+ "SELLER_FORNGT_NOM=?,BUYER_NOM_CUTOFF=?, "
							+ "MEASUREMENT=?,MEAS_STANDARD=?,MEAS_TEMPERATURE=?,OFF_SPEC_GAS=?,"
							+ "SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,"
							+ "LIABILITY=?,LIABILITY_CLAUSE=?,"
							+ "TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "MEAS_CLAUSE=?, SPEC_CLAUSE=?,BILLING_CLAUSE=? ";
					if(reopen_request_flag.equals("Y"))
					{
						query+=",REOPEN_REQUEST_FLAG=?,REOPEN_REQUEST_BY=?,REOPEN_REQUEST_DT=SYSDATE ";
					}
					if(reactivation_flag)
					{
						query+=",REOPEN_REQUEST_FLAG=?,REOPEN_REQUEST_BY=?,REOPEN_REQUEST_DT=?,REOPEN_APPROVAL_FLAG=?,REOPEN_APPROVE_BY=?,REOPEN_APPROVAL_DT=?,REMARK=? ";
					}
					query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++count, agmt_ref_no);
					stmt0.setString(++count, signing_dt);
					stmt0.setString(++count, start_dt);
					stmt0.setString(++count, end_dt);
					stmt0.setString(++count, agreement_base);
					stmt0.setString(++count, agreement_type);
					stmt0.setString(++count, buyer_nom);
					stmt0.setString(++count, buy_m);
					stmt0.setString(++count, buy_w);
					stmt0.setString(++count, buy_d);
					stmt0.setString(++count, seller_nom);
					stmt0.setString(++count, sel_m);
					stmt0.setString(++count, sel_w);
					stmt0.setString(++count, sel_d);
					stmt0.setString(++count, day_def);
					stmt0.setString(++count, day_time_from);
					stmt0.setString(++count, day_time_to);
					stmt0.setString(++count, "");
					stmt0.setString(++count, mdcq);
					//stmt0.setString(++count, remark);
					stmt0.setString(++count, cont_name);
					stmt0.setString(++count, emp_cd);
					stmt0.setString(++count, status);
					stmt0.setString(++count, billing_flag);
					stmt0.setString(++count, rev_eff_dt);
					stmt0.setString(++count, buy_f);
					stmt0.setString(++count, sel_f);
					stmt0.setString(++count, buy_nom_cutoff);
					stmt0.setString(++count, measurement);
					stmt0.setString(++count, meas_standard);
					stmt0.setString(++count, meas_temperature);
					stmt0.setString(++count, off_spec_gas);
					stmt0.setString(++count, spec_gas_energy_base);
					stmt0.setString(++count, spec_gas_min_energy);
					stmt0.setString(++count, spec_gas_max_energy);
					stmt0.setString(++count, liability);
					stmt0.setString(++count, liability_clause);
					stmt0.setString(++count, terminate_flag);
					stmt0.setString(++count, terminate_clause);
					stmt0.setString(++count, terminate_planed);
					stmt0.setString(++count, terminate_force);
					stmt0.setString(++count, pressure_min_bar);
					stmt0.setString(++count, pressure_max_bar);
					stmt0.setString(++count, meas_clause);
					stmt0.setString(++count, spec_clause);
					stmt0.setString(++count, billing_clause);
					if(reopen_request_flag.equals("Y"))
					{
						stmt0.setString(++count, reopen_request_flag);
						stmt0.setString(++count, emp_cd);
					}
					if(reactivation_flag)
					{
						stmt0.setString(++count, reopen_request_flag);
						stmt0.setString(++count, reopen_request_by);
						stmt0.setString(++count, reopen_request_dt);
						stmt0.setString(++count, reopen_approval_flag);
						stmt0.setString(++count, reopen_approve_by);
						stmt0.setString(++count, reopen_approval_dt);
						stmt0.setString(++count, "");
					}
					stmt0.setString(++count, comp_cd);
					stmt0.setString(++count, counterparty_cd);
					stmt0.setString(++count, agmt_no);
					stmt0.setString(++count, agmt_rev_no);
					stmt0.setString(++count, agmt_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int cnt=0;
					query = "INSERT INTO FMS_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_REF_NO,SIGNING_DT,"
							+ "START_DT,END_DT,AGMT_BASE,AGMT_TYP,STATUS, "
							+ "BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
							//+ "REMARK,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
							+ "ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
							+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
							+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
							+ "LIABILITY,LIABILITY_CLAUSE,BILLING_CLAUSE,"
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
							+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,"
							+ "REOPEN_APPROVAL_DT,MEAS_CLAUSE,SPEC_CLAUSE )"
							+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							//+ "?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,?,?,?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++cnt, comp_cd);
					stmt0.setString(++cnt, counterparty_cd);
					stmt0.setString(++cnt, agmt_type);
					stmt0.setString(++cnt, agmt_no);
					stmt0.setString(++cnt, agmt_rev_no);
					stmt0.setString(++cnt, agmt_ref_no);
					stmt0.setString(++cnt, signing_dt);
					stmt0.setString(++cnt, start_dt);
					stmt0.setString(++cnt, end_dt);
					stmt0.setString(++cnt, agreement_base);
					stmt0.setString(++cnt, agreement_type);
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, buyer_nom);
					stmt0.setString(++cnt, buy_m);
					stmt0.setString(++cnt, buy_w);
					stmt0.setString(++cnt, buy_d);
					stmt0.setString(++cnt, seller_nom);
					stmt0.setString(++cnt, sel_m);
					stmt0.setString(++cnt, sel_w);
					stmt0.setString(++cnt, sel_d);
					stmt0.setString(++cnt, day_def);
					stmt0.setString(++cnt, day_time_from);
					stmt0.setString(++cnt, day_time_to);
					stmt0.setString(++cnt, "");
					stmt0.setString(++cnt, mdcq);
					//stmt0.setString(++cnt, remark);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, cont_name);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, buy_f);
					stmt0.setString(++cnt, sel_f);
					stmt0.setString(++cnt, buy_nom_cutoff);
					stmt0.setString(++cnt, measurement);
					stmt0.setString(++cnt, meas_standard);
					stmt0.setString(++cnt, meas_temperature);
					stmt0.setString(++cnt, pressure_min_bar);
					stmt0.setString(++cnt, pressure_max_bar);
					stmt0.setString(++cnt, off_spec_gas);
					stmt0.setString(++cnt, spec_gas_energy_base);
					stmt0.setString(++cnt, spec_gas_min_energy);
					stmt0.setString(++cnt, spec_gas_max_energy);
					stmt0.setString(++cnt, liability);
					stmt0.setString(++cnt, liability_clause);
					stmt0.setString(++cnt, billing_clause);
					stmt0.setString(++cnt, terminate_flag);
					stmt0.setString(++cnt, terminate_clause);
					stmt0.setString(++cnt, terminate_planed);
					stmt0.setString(++cnt, terminate_force);
					stmt0.setString(++cnt, reopen_request_flag);
					stmt0.setString(++cnt, reopen_request_by);
					stmt0.setString(++cnt, reopen_request_dt);
					stmt0.setString(++cnt, reopen_approval_flag);
					stmt0.setString(++cnt, reopen_approve_by);
					stmt0.setString(++cnt, reopen_approval_dt);
					stmt0.setString(++cnt, meas_clause);
					stmt0.setString(++cnt, spec_clause);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				int count = 0;
				
				if(option.equalsIgnoreCase("AGREEMENT_MST"))
				{
					//TRANSPORTER PLANT
					query = "SELECT COUNT(*) "
							+ "FROM FMS_AGMT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, counterparty_cd);
					stmt0.setString(3, agmt_type);
					stmt0.setString(4, agmt_no);
					stmt0.setString(5, agmt_rev_no);
					rset0 = stmt0.executeQuery();
					if(rset0.next())
					{
						 count = rset0.getInt(1);
					}
					rset0.close();
					stmt0.close();
					
					if(count>0)
					{
						query = "DELETE FROM FMS_AGMT_TRANSPTR "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					if(trans_cd!=null)
					{
						for(int i=0;i<trans_cd.length;i++)
						{
							query = "INSERT INTO FMS_AGMT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, agmt_type);
							stmt1.setString(4, agmt_no);
							stmt1.setString(5, agmt_rev_no);
							stmt1.setString(6, trans_cd[i]);
							stmt1.setString(7, trans_plant_seq_no[i]);
							stmt1.setString(8, emp_cd);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
					}
				}
				else if(option.equalsIgnoreCase("DLNG_AGREEMENT_MST"))
				{
					//TRUCK TRANSPORTER PLANT
					query = "SELECT COUNT(*) "
							+ "FROM FMS_AGMT_TRUCK_TRANS "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, counterparty_cd);
					stmt0.setString(3, agmt_type);
					stmt0.setString(4, agmt_no);
					stmt0.setString(5, agmt_rev_no);
					rset0 = stmt0.executeQuery();
					if(rset0.next())
					{
						 count = rset0.getInt(1);
					}
					rset0.close();
					stmt0.close();
					
					if(count>0)
					{
						query = "DELETE FROM FMS_AGMT_TRUCK_TRANS "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					if(chk_truck_trans!=null)
					{
						for(int i=0;i<chk_truck_trans.length;i++)
						{
							query = "INSERT INTO FMS_AGMT_TRUCK_TRANS(COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, agmt_type);
							stmt1.setString(4, agmt_no);
							stmt1.setString(5, agmt_rev_no);
							stmt1.setString(6, chk_truck_trans[i]);
							stmt1.setString(7, emp_cd);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
					}
				}
				
				//TRADER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				rset1 = stmt1.executeQuery();
				count = 0;
				if(rset1.next())
				{
					 count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_AGMT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agmt_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				rset_temp = stmt_temp.executeQuery();
				count = 0;
				if(rset_temp.next())
				{
					 count = rset_temp.getInt(1);
				}
				rset_temp.close();
				stmt_temp.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				if(option.equalsIgnoreCase("DLNG_AGREEMENT_MST"))
				{
					//FILLING STATION
					query = "SELECT COUNT(*) "
							+ "FROM FMS_AGMT_FILLING_STN "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_type);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					rset1 = stmt1.executeQuery();
					count = 0;
					if(rset1.next())
					{
						 count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					if(count>0)
					{
						query = "DELETE FROM FMS_AGMT_FILLING_STN "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					if(chk_fill_station!=null)
					{
						for(int i=0;i<chk_fill_station.length;i++)
						{
							query = "INSERT INTO FMS_AGMT_FILLING_STN(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, FILL_STATION_CD, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
							stmt2 = dbcon.prepareStatement(query);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_type);
							stmt2.setString(4, agmt_no);
							stmt2.setString(5, agmt_rev_no);
							stmt2.setString(6, chk_fill_station[i]);
							stmt2.setString(7, emp_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						 }
					}
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Agreement "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Agreement "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Agreement "+cont_name_map+" Modification for "+counterparty_abbr+" Failed!";
				msg_type="E";
			}
			
			if(option.equalsIgnoreCase("AGREEMENT_MST"))
			{
				url = "../contract_master/frm_agmt_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
				
			}
			else if(option.equalsIgnoreCase("DLNG_AGREEMENT_MST"))
			{
				url = "../contract_master/frm_dlng_agmt_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
				
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+cont_name_map+" Insert/Update Failed";
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
	

	private void InsertUpdateDlngAgreementDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDlngAgreementDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String mdcq = request.getParameter("mdcq")==null?"":request.getParameter("mdcq");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals("")) {
				status="A";
			}
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
			String measurement = request.getParameter("measurementCheckbox")==null?"":request.getParameter("measurementCheckbox");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			String off_spec_gas = request.getParameter("off_spec_gas_checkbox")==null?"":request.getParameter("off_spec_gas_checkbox");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			String liability = request.getParameter("liability_checkbox")==null?"":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			String billing_clause = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String terminate_flag = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			String reopen_request_flag = request.getParameter("reopen_request_flag")==null?"":request.getParameter("reopen_request_flag");
			String reopen_request_by = request.getParameter("reopen_request_by")==null?"":request.getParameter("reopen_request_by");
			String reopen_request_dt = request.getParameter("reopen_request_dt")==null?"":request.getParameter("reopen_request_dt");
			String reopen_approval_flag = request.getParameter("reopen_approval_flag")==null?"":request.getParameter("reopen_approval_flag");
			String reopen_approve_by = request.getParameter("reopen_approve_by")==null?"":request.getParameter("reopen_approve_by");
			String reopen_approval_dt = request.getParameter("reopen_approval_dt")==null?"":request.getParameter("reopen_approval_dt");
			String reopen_remark = request.getParameter("reopen_remark")==null?"":request.getParameter("reopen_remark");
			
			String meas_clause = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String demurage_clause = request.getParameter("demurrage_clause_no")==null?"":request.getParameter("demurrage_clause_no");
			String spec_clause = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			String buy_m = "N";
			String buy_f ="N";
			String buy_w ="N";
			String buy_d = "N";
			
			if(buyer_nom.equalsIgnoreCase("Y"))
			{
				if(chk_buyer_nom!=null)
				{
					for(int i=0;i<chk_buyer_nom.length;i++)
					{
						if(chk_buyer_nom[i].equalsIgnoreCase("M"))
						{
							buy_m = "Y"; 
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("F"))
						{
							buy_f = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("W"))
						{
							buy_w = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("D"))
						{
							buy_d = "Y";
						}
					}
				}
			}
			
			String sel_m = "N";
			String sel_f = "N";
			String sel_w ="N";
			String sel_d = "N";
			
			if(seller_nom.equalsIgnoreCase("Y"))
			{
				if(chk_seller_nom!=null)
				{
					for(int i=0;i<chk_seller_nom.length;i++)
					{
						if(chk_seller_nom[i].equalsIgnoreCase("M"))
						{
							sel_m = "Y"; 
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("F"))
						{
							sel_f = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("W"))
						{
							sel_w = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("D"))
						{
							sel_d = "Y";
						}
					}
				}	
			}
			
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			if(opration.equals("INSERT"))
			{
				query="SELECT MAX(AGMT_NO) FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					agmt_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					agmt_no = "1";
				}
				agmt_rev_no="0";
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_type);
					stmt.setString(4, agmt_rev_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						agmt_rev_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						agmt_rev_no = "0";
					}
					rset.close();
					stmt.close();
				}
			}
			
			String cont_name = comp_abbr+"-"+counterparty_abbr+"-"+agmt_type+agmt_no+"-REV"+agmt_rev_no;
			cont_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agmt_type);
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(rev_count > 0)
				{
					
					query ="UPDATE FMS_AGMT_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "AGMT_TYP=?,BUYER_NOM=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ=?,MDCQ_PERCENTAGE=?,"
							+ "REMARK=?,CONT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),BUYER_FORNGT_NOM=?,"
							+ "SELLER_FORNGT_NOM=?,BUYER_NOM_CUTOFF=?, "
							+ "MEASUREMENT=?,MEAS_STANDARD=?,MEAS_TEMPERATURE=?,OFF_SPEC_GAS=?,"
							+ "SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,"
							+ "LIABILITY=?,LIABILITY_CLAUSE=?,"
							+ "TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "MEAS_CLAUSE=?, SPEC_CLAUSE=?,BILLING_CLAUSE=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, agmt_ref_no);
					stmt0.setString(2, signing_dt);
					stmt0.setString(3, start_dt);
					stmt0.setString(4, end_dt);
					stmt0.setString(5, agreement_base);
					stmt0.setString(6, agreement_type);
					stmt0.setString(7, buyer_nom);
					stmt0.setString(8, buy_m);
					stmt0.setString(9, buy_w);
					stmt0.setString(10, buy_d);
					stmt0.setString(11, seller_nom);
					stmt0.setString(12, sel_m);
					stmt0.setString(13, sel_w);
					stmt0.setString(14, sel_d);
					stmt0.setString(15, day_def);
					stmt0.setString(16, day_time_from);
					stmt0.setString(17, day_time_to);
					stmt0.setString(18, "");
					stmt0.setString(19, mdcq);
					stmt0.setString(20, remark);
					stmt0.setString(21, cont_name);
					stmt0.setString(22, emp_cd);
					stmt0.setString(23, status);
					stmt0.setString(24, billing_flag);
					stmt0.setString(25, rev_eff_dt);
					stmt0.setString(26, buy_f);
					stmt0.setString(27, sel_f);
					stmt0.setString(28, buy_nom_cutoff);
					stmt0.setString(29, measurement);
					stmt0.setString(30, meas_standard);
					stmt0.setString(31, meas_temperature);
					stmt0.setString(32, off_spec_gas);
					stmt0.setString(33, spec_gas_energy_base);
					stmt0.setString(34, spec_gas_min_energy);
					stmt0.setString(35, spec_gas_max_energy);
					stmt0.setString(36, liability);
					stmt0.setString(37, liability_clause);
					stmt0.setString(38, terminate_flag);
					stmt0.setString(39, terminate_clause);
					stmt0.setString(40, terminate_planed);
					stmt0.setString(41, terminate_force);
					stmt0.setString(42, pressure_min_bar);
					stmt0.setString(43, pressure_max_bar);
					stmt0.setString(44, meas_clause);
					stmt0.setString(45, spec_clause);
					stmt0.setString(46, billing_clause);
					stmt0.setString(47, comp_cd);
					stmt0.setString(48, counterparty_cd);
					stmt0.setString(49, agmt_no);
					stmt0.setString(50, agmt_rev_no);
					stmt0.setString(51, agmt_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int cnt=0;
					query = "INSERT INTO FMS_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_REF_NO,SIGNING_DT,"
							+ "START_DT,END_DT,AGMT_BASE,AGMT_TYP,STATUS, "
							+ "BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
							+ "REMARK,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
							+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
							+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
							+ "LIABILITY,LIABILITY_CLAUSE,BILLING_CLAUSE,"
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
							+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,"
							+ "REOPEN_APPROVAL_DT,MEAS_CLAUSE,SPEC_CLAUSE )"
							+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,?,?,?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++cnt, comp_cd);
					stmt0.setString(++cnt, counterparty_cd);
					stmt0.setString(++cnt, agmt_type);
					stmt0.setString(++cnt, agmt_no);
					stmt0.setString(++cnt, agmt_rev_no);
					stmt0.setString(++cnt, agmt_ref_no);
					stmt0.setString(++cnt, signing_dt);
					stmt0.setString(++cnt, start_dt);
					stmt0.setString(++cnt, end_dt);
					stmt0.setString(++cnt, agreement_base);
					stmt0.setString(++cnt, agreement_type);
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, buyer_nom);
					stmt0.setString(++cnt, buy_m);
					stmt0.setString(++cnt, buy_w);
					stmt0.setString(++cnt, buy_d);
					stmt0.setString(++cnt, seller_nom);
					stmt0.setString(++cnt, sel_m);
					stmt0.setString(++cnt, sel_w);
					stmt0.setString(++cnt, sel_d);
					stmt0.setString(++cnt, day_def);
					stmt0.setString(++cnt, day_time_from);
					stmt0.setString(++cnt, day_time_to);
					stmt0.setString(++cnt, "");
					stmt0.setString(++cnt, mdcq);
					stmt0.setString(++cnt, remark);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, cont_name);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, buy_f);
					stmt0.setString(++cnt, sel_f);
					stmt0.setString(++cnt, buy_nom_cutoff);
					stmt0.setString(++cnt, measurement);
					stmt0.setString(++cnt, meas_standard);
					stmt0.setString(++cnt, meas_temperature);
					stmt0.setString(++cnt, pressure_min_bar);
					stmt0.setString(++cnt, pressure_max_bar);
					stmt0.setString(++cnt, off_spec_gas);
					stmt0.setString(++cnt, spec_gas_energy_base);
					stmt0.setString(++cnt, spec_gas_min_energy);
					stmt0.setString(++cnt, spec_gas_max_energy);
					stmt0.setString(++cnt, liability);
					stmt0.setString(++cnt, liability_clause);
					stmt0.setString(++cnt, billing_clause);
					stmt0.setString(++cnt, terminate_flag);
					stmt0.setString(++cnt, terminate_clause);
					stmt0.setString(++cnt, terminate_planed);
					stmt0.setString(++cnt, terminate_force);
					stmt0.setString(++cnt, reopen_request_flag);
					stmt0.setString(++cnt, reopen_request_by);
					stmt0.setString(++cnt, reopen_request_dt);
					stmt0.setString(++cnt, reopen_approval_flag);
					stmt0.setString(++cnt, reopen_approve_by);
					stmt0.setString(++cnt, reopen_approval_dt);
					stmt0.setString(++cnt, meas_clause);
					stmt0.setString(++cnt, spec_clause);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				
				//TRANSPORTER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, agmt_type);
				stmt0.setString(4, agmt_no);
				stmt0.setString(5, agmt_rev_no);
				rset0 = stmt0.executeQuery();
				int count = 0;
				if(rset0.next())
				{
					count = rset0.getInt(1);
				}
				rset0.close();
				stmt0.close();
				
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_type);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				if(trans_cd!=null)
				{
					for(int i=0;i<trans_cd.length;i++)
					{
						query = "INSERT INTO FMS_AGMT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE) ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, trans_cd[i]);
						stmt1.setString(7, trans_plant_seq_no[i]);
						stmt1.setString(8, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
				}
				
				//TRADER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				rset1 = stmt1.executeQuery();
				count = 0;
				if(rset1.next())
				{
					count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_AGMT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agmt_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				rset_temp = stmt_temp.executeQuery();
				count = 0;
				if(rset_temp.next())
				{
					count = rset_temp.getInt(1);
				}
				rset_temp.close();
				stmt_temp.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				//BILLING DETAIL
				String old_rev_no = ""+(Integer.parseInt(agmt_rev_no)-1);
				int count_bill=0;
				query="SELECT COUNT(*) "
						+ "FROM FMS_AGMT_BILLING_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt2 = dbcon.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, agmt_type);
				stmt2.setString(4, agmt_no);
				stmt2.setString(5, agmt_rev_no);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					count_bill=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				if(count_bill==0)
				{
					query="INSERT INTO FMS_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
							+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
							+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,?,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
							+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
							+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,SYSDATE,'"+emp_cd+"' "
							+ "FROM FMS_AGMT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt3 = dbcon.prepareStatement(query);
					stmt3.setString(1, agmt_rev_no);
					stmt3.setString(2, comp_cd);
					stmt3.setString(3, counterparty_cd);
					stmt3.setString(4, agmt_type);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, old_rev_no);
					stmt3.executeUpdate();
					
					stmt3.close();
				}
				
				query = "INSERT INTO FMS_SUPPLY_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE, "
						+ "LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT, "
						+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT)  "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,?,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE, "
						+ "LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT, "
						+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,SYSDATE,?,SYSDATE  "
						+ "FROM FMS_SUPPLY_AGMT_LIABILITY A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND NOT EXISTS( "
						+ "SELECT * FROM FMS_SUPPLY_AGMT_LIABILITY B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND AGMT_REV=? "
						+ ")";
				stmt3 = dbcon.prepareStatement(query);
				stmt3.setString(1, agmt_rev_no);
				stmt3.setString(2, emp_cd);
				stmt3.setString(3, comp_cd);
				stmt3.setString(4, counterparty_cd);
				stmt3.setString(5, agmt_type);
				stmt3.setString(6, agmt_no);
				stmt3.setString(7, old_rev_no);
				stmt3.setString(8, agmt_rev_no);
				stmt3.executeUpdate();
				stmt3.close();
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Agreement "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Agreement "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Agreement "+cont_name_map+" Modification for "+counterparty_abbr+" Failed!";
				msg_type="E";
			}
			
			url = "../contract_master/frm_agmt_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+cont_name_map+" Insert/Update Failed";
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
	
	private void InsertUpdateAgreementBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAgreementBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map ="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			
			String freq=request.getParameter("freq")==null?"F":request.getParameter("freq");
			String billing_flag=request.getParameter("billing_flag")==null?"Y":request.getParameter("billing_flag");
			String due_date=request.getParameter("due_date")==null?"":request.getParameter("due_date");
			String sec_due_date=request.getParameter("sec_due_date")==null?"":request.getParameter("sec_due_date");
			String inv_currency=request.getParameter("inv_currency")==null?"":request.getParameter("inv_currency");
			String payment_currency=request.getParameter("payment_currency")==null?"":request.getParameter("payment_currency");
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String plusmin=request.getParameter("plusmin")==null?"":request.getParameter("plusmin");
			String modeper=request.getParameter("modeper")==null?"":request.getParameter("modeper");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exch_calc_base=request.getParameter("exch_calc_base")==null?"":request.getParameter("exch_calc_base");
			String inv_criteria=request.getParameter("inv_criteria")==null?"":request.getParameter("inv_criteria");
			String exchg_rate_note=request.getParameter("exchg_rate_note")==null?"":request.getParameter("exchg_rate_note");
			
			String due_dt_in=request.getParameter("due_dt_in")==null?"":request.getParameter("due_dt_in");
			String exclude_sat=request.getParameter("exclude_sat")==null?"N":request.getParameter("exclude_sat");
			String exchg_val=request.getParameter("exchg_val")==null?"":request.getParameter("exchg_val");
			String holidayState_map = request.getParameter("holidayState_map")==null?"":request.getParameter("holidayState_map");
			
			String[] sat=request.getParameterValues("sat");
			String exclude_sat_map = "";
			if(exclude_sat.equals("Y"))
			{
				if(sat!=null)
				{
					for(int i=0; i<sat.length; i++)
					{
						 if (sat[i].contains("Y")) 
						 {
			                if (exclude_sat_map.length() > 0) 
			                {
			                	exclude_sat_map+="-"; 
			                }
			                exclude_sat_map+=sat[i].charAt(0);
						 }
					}
				}
			}
			else
			{
				exclude_sat_map = "";
			}
			
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			cont_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
			
			String[] cust_plant_map = holidayState_map.split("@@");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND AGMT_TYPE=?";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, agmt_rev_no);
				stmt3.setString(5, agreement_type);
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String plant_seq = rset3.getString(1)==null?"":rset3.getString(1);
					String state_map = "";
					for(int i=0; i<cust_plant_map.length; i++)
					{
						if(cust_plant_map[i].startsWith(plant_seq))
						{
							state_map = cust_plant_map[i].split("//")[1];
						}
					}
					
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_AGMT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev_no);
					stmt.setString(5, agreement_type);
					stmt.setString(6, plant_seq);
					rset = stmt.executeQuery();
					count = 0;
					if(rset.next())
					{
						 count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count > 0)
					{
						int cnt=0;
						query="UPDATE FMS_AGMT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt, freq);
						stmt1.setString(++cnt, billing_flag);
						stmt1.setString(++cnt, due_date);
						stmt1.setString(++cnt, sec_due_date);
						stmt1.setString(++cnt, inv_currency);
						stmt1.setString(++cnt, payment_currency);
						stmt1.setString(++cnt, rate);
						stmt1.setString(++cnt, plusmin);
						stmt1.setString(++cnt, modeper);
						stmt1.setString(++cnt, exchng_rate);
						stmt1.setString(++cnt, exch_calc_base);
						stmt1.setString(++cnt, inv_criteria);
						stmt1.setString(++cnt, exchg_rate_note);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, due_dt_in);
						stmt1.setString(++cnt, exclude_sat);
						stmt1.setString(++cnt, exchg_val);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?)";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, agmt_no);
						stmt1.setString(++cnt1, agmt_rev_no);
						stmt1.setString(++cnt1, agreement_type);
						stmt1.setString(++cnt1, freq);
						stmt1.setString(++cnt1, billing_flag);
						stmt1.setString(++cnt1, due_date);
						stmt1.setString(++cnt1, sec_due_date);
						stmt1.setString(++cnt1, inv_currency);
						stmt1.setString(++cnt1, payment_currency);
						stmt1.setString(++cnt1, rate);
						stmt1.setString(++cnt1, plusmin);
						stmt1.setString(++cnt1, modeper);
						stmt1.setString(++cnt1, exchng_rate);
						stmt1.setString(++cnt1, exch_calc_base);
						stmt1.setString(++cnt1, inv_criteria);
						stmt1.setString(++cnt1, exchg_rate_note);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, due_dt_in);
						stmt1.setString(++cnt1, exclude_sat);
						stmt1.setString(++cnt1, exchg_val);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - "+cont_name_map+" Billing Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Billing Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../contract_master/frm_agmt_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! "+cont_name_map+" Billing Detail Insert/Update Failed";

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
	
	private void InsertUpdateSupplyContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSupplyContractDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		String cont_type_nm = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = "";
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");

			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String trade_ref_no = request.getParameter("trade_ref_no")==null?"":request.getParameter("trade_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String dda_dt = request.getParameter("dda_dt")==null?"":request.getParameter("dda_dt");
			String dda_time = request.getParameter("dda_time")==null?"":request.getParameter("dda_time");
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String temp_start_dt = request.getParameter("temp_start_dt")==null?"":request.getParameter("temp_start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String post_margin = request.getParameter("post_margin")==null?"":request.getParameter("post_margin");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String rate = request.getParameter("rate")==null?"":request.getParameter("rate");
			String rate_unit = request.getParameter("rate_unit")==null?"":request.getParameter("rate_unit");
			String tcq = request.getParameter("tcq")==null?"":request.getParameter("tcq");
			String quantity_unit = request.getParameter("quantity_unit")==null?"":request.getParameter("quantity_unit");
			String dcq = request.getParameter("dcq")==null?"":request.getParameter("dcq");
			String mdcq = request.getParameter("mdcq")==null?"":request.getParameter("mdcq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_status_flg = request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg");
			String is_allocated = request.getParameter("is_allocated")==null?"N":request.getParameter("is_allocated");
			String txn_charges = request.getParameter("txn_charges")==null?"":request.getParameter("txn_charges");
			String txn_unit = request.getParameter("txn_unit")==null?"":request.getParameter("txn_unit");
			String is_inv_submitted = request.getParameter("is_inv_submitted")==null?"":request.getParameter("is_inv_submitted");
			
			String tcq_sign = request.getParameter("tcq_sign")==null?"":request.getParameter("tcq_sign");
			String var_tcq = request.getParameter("var_tcq")==null?"":request.getParameter("var_tcq");
			String change_request = request.getParameter("change_request")==null?"":request.getParameter("change_request");
			String contdt_change_request_flag = request.getParameter("contdt_change_request_flag")==null?"":request.getParameter("contdt_change_request_flag");
			
			String DealEnterDtTime = ent_dt+" "+ent_time;
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			String tmp_chk_plant = request.getParameter("tmp_chk_plant")==null?"":request.getParameter("tmp_chk_plant");
			String[] chk_fill_station = request.getParameterValues("chk_fill_station");
			String[] charge_abbr = request.getParameterValues("charge_abbr");
			//String[] transportation_charges = request.getParameterValues("transportation_charges");
			//String[] marketing_margin = request.getParameterValues("marketing_margin");
			//String[] other_charges = request.getParameterValues("other_charges");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] chk_truck_trans = request.getParameterValues("chk_truck_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String[] chk_gx_bu_plant = request.getParameterValues("chk_gx_bu_plant");
			String gx_counterparty_cd=request.getParameter("gx_counterparty_cd")==null?"":request.getParameter("gx_counterparty_cd");
			
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String measurement_flg = request.getParameter("measurementCheckbox")==null?"":request.getParameter("measurementCheckbox");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			String off_spec_gas_flg = request.getParameter("off_spec_gas_checkbox")==null?"":request.getParameter("off_spec_gas_checkbox");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			String liability_flg = request.getParameter("liability_checkbox")==null?"":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String billing_clause_no = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String terminator_flg = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			String measure_clause_no = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String spec_clause_no = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
			String cancel_note = request.getParameter("cancel_note")==null?"":request.getParameter("cancel_note");
			String closure_mmbtu = request.getParameter("closure_mmbtu")==null?"":request.getParameter("closure_mmbtu");
			String closure_eff_dt = request.getParameter("closure_eff_dt")==null?"":request.getParameter("closure_eff_dt");
			String cont_status_remark = request.getParameter("cont_status_remark")==null?"":request.getParameter("cont_status_remark");
			String delta_tcq_sign1 = request.getParameter("delta_tcq_sign1")==null?"":request.getParameter("delta_tcq_sign1");
			
			String adv_adjust = request.getParameter("adv_adjust")==null?"":request.getParameter("adv_adjust");
			String new_start_dt = request.getParameter("new_start_dt")==null?"":request.getParameter("new_start_dt");
			String new_end_dt = request.getParameter("new_end_dt")==null?"":request.getParameter("new_end_dt");
			
			closure_mmbtu = delta_tcq_sign1.equals("-")?delta_tcq_sign1+closure_mmbtu:closure_mmbtu;
			String buy_m = "N";
			String buy_f ="N";
			String buy_w ="N";
			String buy_d = "N";
			
			if(buyer_nom.equalsIgnoreCase("Y"))
			{
				if(chk_buyer_nom!=null)
				{
					for(int i=0;i<chk_buyer_nom.length;i++)
					{
						if(chk_buyer_nom[i].equalsIgnoreCase("M"))
						{
							buy_m = "Y"; 
			    		}
						else if(chk_buyer_nom[i].equalsIgnoreCase("F"))
						{
							buy_f = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("W"))
						{
							buy_w = "Y";
						}
						else if(chk_buyer_nom[i].equalsIgnoreCase("D"))
						{
							buy_d = "Y";
						}
					}
				}
			}
			
			String sel_m = "N";
			String sel_f = "N";
			String sel_w ="N";
			String sel_d = "N";
			
			if(seller_nom.equalsIgnoreCase("Y"))
			{
				if(chk_seller_nom!=null)
				{
					for(int i=0;i<chk_seller_nom.length;i++)
					{
						if(chk_seller_nom[i].equalsIgnoreCase("M"))
						{
							sel_m = "Y"; 
			    		}
						else if(chk_seller_nom[i].equalsIgnoreCase("F"))
						{
							sel_f = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("W"))
						{
							sel_w = "Y";
						}
						else if(chk_seller_nom[i].equalsIgnoreCase("D"))
						{
							sel_d = "Y";
						}
					}
				}	
			}
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				
				if(contract_type.equals("S")) //"S" is Not DLNG Contract
				{
					query="SELECT MAX(CONT_NO) "
							+ "FROM FMS_SUPPLY_CONT_MST "
							+ "WHERE AGMT_NO=? AND CONTRACT_TYPE=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, agmt_no);
					stmt.setString(2, contract_type);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						cont_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						cont_no = "1";
					}
					cont_rev_no="0";
					
					rset.close();
					stmt.close();
					//agmt_no="0";
					//agmt_rev_no="0";
				}
				else if(!ent_dt.equals(""))
				{
					String year = ent_dt.substring(8,ent_dt.length());
					
					int cnt = 0;
					
					if(contract_type.equals("L") || contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W"))
					{
						if(!contract_type.equals("F"))
						{
							agmt_no="0";
							agmt_rev_no="0";
						}
						
						int cont = 0;
						if(contract_type.equals("L"))
						{
							year="2"+year;
							cont = Integer.parseInt(year) * 1000;
						}
						else
						{
							//year=year;
							cont = Integer.parseInt(year) * 10000;
						}
						
						
						query="SELECT NVL(MAX(CONT_NO),?) "
								+ "FROM FMS_SUPPLY_CONT_MST "
								+ "WHERE ";
						if(option.equalsIgnoreCase("SUPPLY_CONT_MST")) //HM20250207 : As per discussion Currently for Gas Contract only contract no generating based on CP, will need to removed 
						{
							query+= "AGMT_NO=? AND ";
						}
						query+= "CONT_NO LIKE ? AND CONTRACT_TYPE=? "
								+ "AND COMPANY_CD=?";
						if(option.equalsIgnoreCase("SUPPLY_CONT_MST"))
						{
							query+= " AND COUNTERPARTY_CD=? ";
						}
						stmt = dbcon.prepareStatement(query);
						stmt.setInt(++cnt, cont);
						if(option.equalsIgnoreCase("SUPPLY_CONT_MST")) //HM20250207 : As per discussion Currently for Gas Contract only contract no generating based on CP, will need to removed 
						{
							stmt.setString(++cnt, agmt_no);//
						}
						stmt.setString(++cnt, year+"%");
						stmt.setString(++cnt, contract_type);
						stmt.setString(++cnt, comp_cd);
						if(option.equalsIgnoreCase("SUPPLY_CONT_MST")) //HM20250207 : As per discussion Currently for Gas Contract only contract no generating based on CP, will need to removed 
						{
							stmt.setString(++cnt, counterparty_cd);
						}
						rset=stmt.executeQuery();
						if(rset.next())
						{
							cont_no = ""+(rset.getInt(1)+1);
						}
						else
						{
							cont_no = ""+(cont+1);
						}
						cont_rev_no="0";
						stmt.close();
						rset.close();
						
					}
					else if(contract_type.equals("X"))
					{
						agmt_no="0";
						agmt_rev_no="0";
						
						int cont = 0;
						
						year="1"+year;
						cont = Integer.parseInt(year) * 1000;
						
						/*if(contract_type.equals("X"))
						{
							year="1"+year;
							cont = Integer.parseInt(year) * 1000;
						}
						else
						{
							//year=year;
							cont = Integer.parseInt(year) * 10000;
						}*/
						
						query="SELECT NVL(MAX(CONT_NO),?) "
								+ "FROM FMS_SUPPLY_CONT_MST "
								+ "WHERE AGMT_NO=? AND CONT_NO LIKE ? AND CONTRACT_TYPE=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? ";
						stmt = dbcon.prepareStatement(query);
						stmt.setInt(1, cont);
						stmt.setString(2, agmt_no);
						stmt.setString(3, year+"%");
						stmt.setString(4, contract_type);
						stmt.setString(5, comp_cd);
						stmt.setString(6, counterparty_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							cont_no = ""+(rset.getInt(1)+1);
						}
						else
						{
							cont_no = ""+(cont+1);
						}
						cont_rev_no="0";
						rset.close();
						stmt.close();
					}
				}
			}
			
			/*
		 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
			CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
			CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
			*/
			if(change_request.equals("TCQ") || change_request.equals("PRICE") || change_request.equals("CONTRACT_DATE") || change_request.equals("CLOSURE") || change_request.equals("TERMINATE"))
			{
				cont_rev_no=""+(Integer.parseInt(cont_rev_no)+1);
				
				cont_status_flg="P";
			}
			
			String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+contract_type+cont_no+"-REV"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			cont_type_nm = utilBean.getContractTypeName(contract_type);
			
			if(contract_type.equals("S"))
			{
				cont_name = counterparty_abbr+"-"+comp_abbr+"-FGSA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;			
			}
			else if(contract_type.equals("F"))
			{
				cont_name = counterparty_abbr+"-"+comp_abbr+"-FLSA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;			
			}
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_SUPPLY_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
			
				if(rev_count > 0)
				{
					int st_count=0;
					query ="UPDATE FMS_SUPPLY_CONT_MST SET CONT_REF_NO=?,TRADE_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "SIGNING_TIME=?,START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "AGMT_TYPE=?,TCQ=?,DCQ=?,QUANTITY_UNIT=?,RATE=?,RATE_UNIT=?, "
							+ "POST_MARGIN=?,TRANSPORTATION_CHARGE=?,"
							+ "BUYER_NOM_FLAG=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM_FLAG=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF_FLAG=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ_FLAG=?,MDCQ_PERCENTAGE=?,"
							+ "REMARK=?,CONT_NAME=?,CONTRACT_TYPE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
							+ "CONT_STATUS=?,IS_ALLOCATED=?,BUYER_FORNGT_NOM=?,SELLER_FORNGT_NOM=?,"
							+ "DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,TXN_CHARGE=?,BUYER_NOM_CUTOFF=?,"
							+ "TXN_UNIT=?,ADV_ADJUST=? ";
					//if(contdt_change_request_flag.equals("Y"))	
					/*if(contdt_change_request_flag.equals("Y") && !change_request.equals("CLOSURE") && !change_request.equals("REOPEN") && !change_request.equals("TERMINATE"))		//PB20250320: Added && condition as it was setting the CHANGE_DATE_REQ to N when contract is closed.
					{
						query+=",CHANGE_DATE_REQ=? ";
					}*/
					query+=	",MEASUREMENT =?,MEAS_STANDARD=?, MEAS_TEMPERATURE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "OFF_SPEC_GAS=?,SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,LIABILITY=?,LIABILITY_CLAUSE=?, "
							+ "BILLING_FLAG=?,BILLING_CLAUSE=?,TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?, "
							+ "MEASUREMENT_CLAUSE=?,OFF_SPEC_GAS_CLAUSE=? ";
					if(change_request.equals("CANCEL"))
					{
						query+=",CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),CLOSURE_REMARK=? ";
					}
					if(change_request.equals("REOPEN"))
					{
						query+=",CLOSURE_REQUEST_FLAG=? ";
					}
					query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++st_count, cont_ref_no);
					stmt1.setString(++st_count, trade_ref_no);
					stmt1.setString(++st_count, signing_dt);
					stmt1.setString(++st_count, signing_time);
					stmt1.setString(++st_count, start_dt);
					stmt1.setString(++st_count, end_dt);
					stmt1.setString(++st_count, agreement_base);
					stmt1.setString(++st_count, agreement_type);
					stmt1.setString(++st_count, tcq);
					stmt1.setString(++st_count, dcq);
					stmt1.setString(++st_count, quantity_unit);
					stmt1.setString(++st_count, rate);
					stmt1.setString(++st_count, rate_unit);
					stmt1.setString(++st_count, post_margin);
					stmt1.setString(++st_count, "");
					stmt1.setString(++st_count, buyer_nom);
					stmt1.setString(++st_count, buy_m);
					stmt1.setString(++st_count, buy_w);
					stmt1.setString(++st_count, buy_d);
					stmt1.setString(++st_count, seller_nom);
					stmt1.setString(++st_count, sel_m);
					stmt1.setString(++st_count, sel_w);
					stmt1.setString(++st_count, sel_d);
					stmt1.setString(++st_count, day_def);
					stmt1.setString(++st_count, day_time_from);
					stmt1.setString(++st_count, day_time_to);
					stmt1.setString(++st_count, "");
					stmt1.setString(++st_count, mdcq);
					stmt1.setString(++st_count, remark);
					stmt1.setString(++st_count, cont_name);
					stmt1.setString(++st_count, contract_type);
					stmt1.setString(++st_count, emp_cd);
					stmt1.setString(++st_count, cont_status_flg);
					stmt1.setString(++st_count, is_allocated);
					stmt1.setString(++st_count, buy_f);
					stmt1.setString(++st_count, sel_f);
					stmt1.setString(++st_count, dda_dt);
					stmt1.setString(++st_count, dda_time);
					stmt1.setString(++st_count, txn_charges);
					stmt1.setString(++st_count, buy_nom_cutoff);
					stmt1.setString(++st_count, txn_unit);
					stmt1.setString(++st_count, adv_adjust);
					/*if(contdt_change_request_flag.equals("Y") && !change_request.equals("CLOSURE") && !change_request.equals("REOPEN") && !change_request.equals("TERMINATE"))
					{
						stmt1.setString(++st_count, "N");
					}*/
					stmt1.setString(++st_count, measurement_flg);
					stmt1.setString(++st_count, meas_standard);
					stmt1.setString(++st_count, meas_temperature);
					stmt1.setString(++st_count, pressure_min_bar);
					stmt1.setString(++st_count, pressure_max_bar);
					stmt1.setString(++st_count, off_spec_gas_flg);
					stmt1.setString(++st_count, spec_gas_energy_base);
					stmt1.setString(++st_count, spec_gas_min_energy);
					stmt1.setString(++st_count, spec_gas_max_energy);
					stmt1.setString(++st_count, liability_flg);
					stmt1.setString(++st_count, liability_clause);
					stmt1.setString(++st_count, billing_flag);
					stmt1.setString(++st_count, billing_clause_no);
					stmt1.setString(++st_count, terminator_flg);
					stmt1.setString(++st_count, terminate_clause);
					stmt1.setString(++st_count, terminate_planed);
					stmt1.setString(++st_count, terminate_force);
					stmt1.setString(++st_count, measure_clause_no);
					stmt1.setString(++st_count, spec_clause_no);
					if(change_request.equals("CANCEL"))
					{
						stmt1.setString(++st_count, sysdate);
						stmt1.setString(++st_count, cancel_note);
					}
					if(change_request.equals("REOPEN"))
					{
						stmt1.setString(++st_count,"O");
					}
					stmt1.setString(++st_count, comp_cd);
					stmt1.setString(++st_count, counterparty_cd);
					stmt1.setString(++st_count, cont_no);
					stmt1.setString(++st_count, cont_rev_no);
					stmt1.setString(++st_count, agmt_no);
					stmt1.setString(++st_count, agmt_rev_no);
					stmt1.setString(++st_count, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else
				{
					int count=0;
					query = "INSERT INTO FMS_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,"
							+ "START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
							+ "POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
							+ "REMARK,ENT_DT,ENT_BY,CONT_NAME,CONTRACT_TYPE,CONT_STATUS,IS_ALLOCATED,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
							+ "DDA_DT,DDA_TIME,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,ADV_ADJUST ";
					if(change_request.equals("TCQ"))
					{
						query+=",TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_QTY";
					}
					if(change_request.equals("PRICE"))
					{
						query+=",PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG";
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						query+=",CHANGE_DATE_REQ,CHANGE_START_DT,CHANGE_END_DT ";
					}
					/*if(change_request.equals("CANCEL"))
					{
						query+=", CLOSE_EFF_DT,CLOSURE_REMARK ";
					}*/
					if(change_request.equals("CLOSURE"))
					{
						query+=",CLOSURE_REQUEST_FLAG ";
					}
					if(change_request.equals("TERMINATE"))
					{
						query+=",CLOSURE_REQUEST_FLAG ";
					}
					query+= ",MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
							+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE "
							+ ",CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK ) ";		//PB20250417
					query+= "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY HH24:MI'),?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?";					//PB20250417			
					if(change_request.equals("TCQ"))
					{
						query+=",?,?,?";
					}
					if(change_request.equals("PRICE"))
					{
						query+=",?,?";
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						query+=",?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY')";
					}
					/*if(change_request.equals("CANCEL"))
					{
						query+=", TO_DATE(?,'DD/MM/YYYY'),? ";
					}*/
					if(change_request.equals("CLOSURE"))
					{
						query+=",? ";
					}
					if(change_request.equals("TERMINATE"))
					{
						query+=",? ";
					}
					query+= ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",TO_DATE(?,'DD/MM/YYYY'),?,?) ";		//PB20250417
					
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, counterparty_cd);
					stmt1.setString(++count, agmt_no);
					stmt1.setString(++count, agmt_rev_no);
					stmt1.setString(++count, cont_no);
					stmt1.setString(++count, cont_rev_no);
					stmt1.setString(++count, cont_ref_no);
					stmt1.setString(++count, trade_ref_no);
					stmt1.setString(++count, signing_dt);
					stmt1.setString(++count, signing_time);
					stmt1.setString(++count, start_dt);
					stmt1.setString(++count, end_dt);
					stmt1.setString(++count, agreement_base);
					stmt1.setString(++count, agreement_type);
					stmt1.setString(++count, tcq);
					stmt1.setString(++count, dcq);
					stmt1.setString(++count, quantity_unit);
					stmt1.setString(++count, rate);
					stmt1.setString(++count, rate_unit);
					stmt1.setString(++count, post_margin);
					stmt1.setString(++count, "");
					stmt1.setString(++count, buyer_nom);
					stmt1.setString(++count, buy_m);
					stmt1.setString(++count, buy_w);
					stmt1.setString(++count, buy_d);
					stmt1.setString(++count, seller_nom);
					stmt1.setString(++count, sel_m);
					stmt1.setString(++count, sel_w);
					stmt1.setString(++count, sel_d);
					stmt1.setString(++count, day_def);
					stmt1.setString(++count, day_time_from);
					stmt1.setString(++count, day_time_to);
					stmt1.setString(++count, "");
					stmt1.setString(++count, mdcq);
					stmt1.setString(++count, remark);
					stmt1.setString(++count, DealEnterDtTime);
					stmt1.setString(++count, emp_cd);
					stmt1.setString(++count, cont_name);
					stmt1.setString(++count, contract_type);
					stmt1.setString(++count, cont_status_flg);
					stmt1.setString(++count, is_allocated);
					stmt1.setString(++count, buy_f);
					stmt1.setString(++count, sel_f);
					stmt1.setString(++count, dda_dt);
					stmt1.setString(++count, dda_time);
					stmt1.setString(++count, txn_charges);
					stmt1.setString(++count, buy_nom_cutoff);
					stmt1.setString(++count, txn_unit);
					stmt1.setString(++count, adv_adjust);
					if(change_request.equals("TCQ"))
					{
						stmt1.setString(++count, tcq_sign);
						stmt1.setString(++count, "Y");
						stmt1.setString(++count, var_tcq);
					}
					if(change_request.equals("PRICE"))
					{
						stmt1.setString(++count, "Y");
						stmt1.setString(++count, "N");
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						stmt1.setString(++count, "Y");
						stmt1.setString(++count, new_start_dt);
						stmt1.setString(++count, new_end_dt);
					}
					/*if(change_request.equals("CANCEL"))
					{
						stmt1.setString(++count,sysdate);
						stmt1.setString(++count,cancel_note);
					}*/
					if(change_request.equals("CLOSURE"))
					{
						stmt1.setString(++count,"Y");
					}
					if(change_request.equals("TERMINATE"))
					{
						stmt1.setString(++count,"R");
					}
					stmt1.setString(++count, measurement_flg);
					stmt1.setString(++count, meas_standard);
					stmt1.setString(++count, meas_temperature);
					stmt1.setString(++count, pressure_min_bar);
					stmt1.setString(++count, pressure_max_bar);
					stmt1.setString(++count, off_spec_gas_flg);
					stmt1.setString(++count, spec_gas_energy_base);
					stmt1.setString(++count, spec_gas_min_energy);
					stmt1.setString(++count, spec_gas_max_energy);
					stmt1.setString(++count, liability_flg);
					stmt1.setString(++count, liability_clause);
					stmt1.setString(++count, billing_flag);
					stmt1.setString(++count, billing_clause_no);
					stmt1.setString(++count, terminator_flg);
					stmt1.setString(++count, terminate_clause);
					stmt1.setString(++count, terminate_planed);
					stmt1.setString(++count, terminate_force);
					stmt1.setString(++count, measure_clause_no);
					stmt1.setString(++count, spec_clause_no);
					stmt1.setString(++count, closure_eff_dt);
					stmt1.setString(++count, closure_mmbtu);
					stmt1.setString(++count, cont_status_remark);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					if(liability_flg.equals("Y") && !change_request.equals("TCQ") && !change_request.equals("PRICE") && !change_request.equals("CONTRACT_DATE") && !contdt_change_request_flag.equals("Y") && !change_request.equals("CANCEL") && !change_request.equals("CLOSURE") && !change_request.equals("TERMINATE"))
					{
						query1="INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,?,?,?,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,SYSDATE,?,REV_DT "
								+ "FROM FMS_SUPPLY_AGMT_LIABILITY A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
								+ "AND EXISTS("
								+ "SELECT * "
								+ "FROM FMS_SUPPLY_AGMT_LIABILITY B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
								+ ")";
						
						stmt0 = dbcon.prepareStatement(query1);
						stmt0.setString(1, cont_no);
						stmt0.setString(2, cont_rev_no);
						stmt0.setString(3, contract_type);
						stmt0.setString(4, emp_cd);
						stmt0.setString(5, comp_cd);
						stmt0.setString(6, counterparty_cd);
						stmt0.setString(7, agmt_no);
						stmt0.executeUpdate();
						
						stmt0.close();
					}		
				}
				
				generateSupplyContractLog(counterparty_cd, cont_no, cont_rev_no, agmt_no, agmt_rev_no, contract_type, emp_cd);
				
				if(!temp_start_dt.equals(start_dt) && !start_dt.equals("") && is_inv_submitted.equals("0"))
				{
					updateSupplyContractBillingEffectiveDate(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_start_dt, start_dt);
				}				
				
				// JD : Why this change required?
				/*
				if(billing_flag.equals("Y") && !change_request.equals("TCQ") && !change_request.equals("PRICE") && !change_request.equals("CONTRACT_DATE") && !contdt_change_request_flag.equals("Y"))
				{
					query="INSERT INTO FMS_SUPPLY_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
							+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
							+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EFF_DT,EXCHG_VAL) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,?,?,?,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
							+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
							+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,SYSDATE,?,DUE_DT_IN,EXCLUDE_SAT,SYSDATE,EXCHG_VAL "
							+ "FROM FMS_AGMT_BILLING_DTL A "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND "
							+ "EXISTS("
							+ "SELECT * FROM "
							+ "FMS_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ " AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ ")";
					stmt3 = dbcon.prepareStatement(query);
					stmt3.setString(1, cont_no);
					stmt3.setString(2, cont_rev_no);
					stmt3.setString(3, contract_type);
					stmt3.setString(4, emp_cd);
					stmt3.setString(5, comp_cd);
					stmt3.setString(6, counterparty_cd);
					stmt3.setString(7, agmt_no);
					stmt3.setString(8, agmt_rev_no);
					stmt3.executeUpdate();
					
					stmt3.close();
				}*/	
				int count = 0;
				
				if(option.equalsIgnoreCase("SUPPLY_CONT_MST"))
				{
					//TRANSPORTER PLANT
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SUPPLY_CONT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					rset2 = stmt2.executeQuery();
					count = 0;
					if(rset2.next())
					{
						 count = rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					if(count>0)
					{
						query = "DELETE FROM FMS_SUPPLY_CONT_TRANSPTR "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, cont_no);
						stmt3.setString(4, cont_rev_no);
						stmt3.setString(5, agmt_no);
						stmt3.setString(6, agmt_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					if(trans_cd!=null)
					{
						for(int i=0;i<trans_cd.length;i++)
						{
							query = "INSERT INTO FMS_SUPPLY_CONT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt3 = dbcon.prepareStatement(query);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							stmt3.setString(8, trans_cd[i]);
							stmt3.setString(9, trans_plant_seq_no[i]);
							stmt3.setString(10, emp_cd);
							stmt3.executeUpdate();
							
							stmt3.close();
						}
					}
				}
				else if(option.equalsIgnoreCase("DLNG_CONT_MST"))
				{
					//TRUCK TRANSPORTER PLANT
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SUPPLY_CONT_TRUCK_TRANS "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					rset2 = stmt2.executeQuery();
					count = 0;
					if(rset2.next())
					{
						 count = rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					if(count>0)
					{
						query = "DELETE FROM FMS_SUPPLY_CONT_TRUCK_TRANS "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, cont_no);
						stmt3.setString(4, cont_rev_no);
						stmt3.setString(5, agmt_no);
						stmt3.setString(6, agmt_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					if(chk_truck_trans!=null)
					{
						for(int i=0;i<chk_truck_trans.length;i++)
						{
							query = "INSERT INTO FMS_SUPPLY_CONT_TRUCK_TRANS(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt3 = dbcon.prepareStatement(query);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							stmt3.setString(8, chk_truck_trans[i]);
							stmt3.setString(9, emp_cd);
							stmt3.executeUpdate();
							
							stmt3.close();
						}
					}
				}
				
				//CUSTOMER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt4 = dbcon.prepareStatement(query);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, counterparty_cd);
				stmt4.setString(3, cont_no);
				stmt4.setString(4, cont_rev_no);
				stmt4.setString(5, agmt_no);
				stmt4.setString(6, agmt_rev_no);
				stmt4.setString(7, contract_type);
				rset4 = stmt4.executeQuery();
				count = 0;
				if(rset4.next())
				{
					 count = rset4.getInt(1);
				}
				rset4.close();
				stmt4.close();
				
				if(count>0)
				{
					query = "DELETE FROM FMS_SUPPLY_CONT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt3 = dbcon.prepareStatement(query);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, cont_rev_no);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmt_rev_no);
					stmt3.setString(7, contract_type);
					stmt3.executeUpdate();
					
					stmt3.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_SUPPLY_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,SYSDATE) ";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, agmt_no);
						stmt3.setString(4, agmt_rev_no);
						stmt3.setString(5, cont_no);
						stmt3.setString(6, cont_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.setString(8, chk_plant[i]);
						stmt3.setString(9, emp_cd);
						stmt3.executeUpdate();
						stmt3.close();
						
						if(option.equalsIgnoreCase("SUPPLY_CONT_MST"))
						{
							if(charge_abbr!=null)
							{
								for(int j=0; j<charge_abbr.length; j++)
								{
									String chrgAbbr=charge_abbr[j];
									String effDt = request.getParameterValues("eff_dt_"+chrgAbbr)[i];
									String chrgRate = request.getParameterValues(chrgAbbr)[i];
									
									if(!chrgRate.equals("") && !effDt.equals(""))
									{
										query="SELECT COUNT(*) "
												+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
												+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
												+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND CHARGE_ABBR=? ";
										stmt=dbcon.prepareStatement(query);
										stmt.setString(1, comp_cd);
										stmt.setString(2, counterparty_cd);
										stmt.setString(3, cont_no);
										stmt.setString(4, agmt_no);
										stmt.setString(5, agmt_rev_no);
										stmt.setString(6, contract_type);
										stmt.setString(7, chk_plant[i]);
										stmt.setString(8, effDt);
										stmt.setString(9, chrgAbbr);
										rset = stmt.executeQuery();
										count = 0;
										if(rset.next())
										{
											 count = rset.getInt(1);
										}
										rset.close();
										stmt.close();
										
										if(count > 0)
										{
											query="UPDATE FMS_SUPPLY_CONT_PLANT_CHRG SET CHARGE_RATE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
													+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
													+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
													+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND CHARGE_ABBR=? ";
											stmt=dbcon.prepareStatement(query);
											stmt.setString(1, chrgRate);
											stmt.setString(2, emp_cd);
											stmt.setString(3, comp_cd);
											stmt.setString(4, counterparty_cd);
											stmt.setString(5, cont_no);
											stmt.setString(6, agmt_no);
											stmt.setString(7, agmt_rev_no);
											stmt.setString(8, contract_type);
											stmt.setString(9, chk_plant[i]);
											stmt.setString(10, effDt);
											stmt.setString(11, chrgAbbr);
											stmt.executeUpdate();
											stmt.close();
										}
										else
										{
											query2 = "INSERT INTO FMS_SUPPLY_CONT_PLANT_CHRG(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
													+ "CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, EFF_DT, CHARGE_ABBR, CHARGE_RATE,ENT_BY, ENT_DT) "
													+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE) ";						
											stmt2=dbcon.prepareStatement(query2);
											stmt2.setString(1, comp_cd);
											stmt2.setString(2, counterparty_cd);
											stmt2.setString(3, agmt_no);
											stmt2.setString(4, agmt_rev_no);
											stmt2.setString(5, cont_no);
											stmt2.setString(6, cont_rev_no);
											stmt2.setString(7, contract_type);
											stmt2.setString(8, chk_plant[i]);
											stmt2.setString(9, effDt);
											stmt2.setString(10, chrgAbbr);
											stmt2.setString(11, chrgRate);
											stmt2.setString(12, emp_cd);
											stmt2.executeUpdate();
											stmt2.close();
										}
									}
								}
							}
						}
						String temp_chk_plant[]=tmp_chk_plant.split("@");
						for(int m=0; m<temp_chk_plant.length; m++)
						{
							if(!temp_chk_plant[m].equals(chk_plant[i]) && !chk_plant[i].equals(""))
							{
								updateContractBillingPlant(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_plant[m], chk_plant[i], chk_plant.length);
							}
						}
					}
				}
				
				if(option.equalsIgnoreCase("DLNG_CONT_MST"))
				{
					//FILLING STATION
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SUPPLY_CONT_FILLING_STN "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt4 = dbcon.prepareStatement(query);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterparty_cd);
					stmt4.setString(3, cont_no);
					stmt4.setString(4, cont_rev_no);
					stmt4.setString(5, agmt_no);
					stmt4.setString(6, agmt_rev_no);
					stmt4.setString(7, contract_type);
					rset4 = stmt4.executeQuery();
					count = 0;
					if(rset4.next())
					{
						 count = rset4.getInt(1);
					}
					rset4.close();
					stmt4.close();
					
					if(count>0)
					{
						query = "DELETE FROM FMS_SUPPLY_CONT_FILLING_STN "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, cont_no);
						stmt3.setString(4, cont_rev_no);
						stmt3.setString(5, agmt_no);
						stmt3.setString(6, agmt_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					if(chk_fill_station!=null)
					{
						for(int i=0;i<chk_fill_station.length;i++)
						{
							query = "INSERT INTO FMS_SUPPLY_CONT_FILLING_STN(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, FILL_STATION_CD, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt3 = dbcon.prepareStatement(query);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							stmt3.setString(8, chk_fill_station[i]);
							stmt3.setString(9, emp_cd);
							stmt3.executeUpdate();
							stmt3.close();
						}
					}
				}
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_SUPPLY_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt5 = dbcon.prepareStatement(query);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, cont_no);
				stmt5.setString(4, cont_rev_no);
				stmt5.setString(5, agmt_no);
				stmt5.setString(6, agmt_rev_no);
				stmt5.setString(7, contract_type);
				rset5 = stmt5.executeQuery();
				count = 0;
				if(rset5.next())
				{
					 count = rset5.getInt(1);
				}
				rset5.close();
				stmt5.close();
				
				if(count>0)
				{
					query = "DELETE FROM FMS_SUPPLY_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt3 = dbcon.prepareStatement(query);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, cont_rev_no);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmt_rev_no);
					stmt3.setString(7, contract_type);
					stmt3.executeUpdate();
					
					stmt3.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_SUPPLY_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE) ";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, agmt_no);
						stmt3.setString(4, agmt_rev_no);
						stmt3.setString(5, cont_no);
						stmt3.setString(6, cont_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.setString(8, chk_bu_plant[i]);
						stmt3.setString(9, emp_cd);
						stmt3.executeUpdate();
						
						stmt3.close();
					 }
				}
				
				//GX BUSINESS UNIT
				if(!gx_counterparty_cd.equals(""))
				{
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SUPPLY_CONT_GX_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt6 = dbcon.prepareStatement(query);
					stmt6.setString(1, comp_cd);
					stmt6.setString(2, counterparty_cd);
					stmt6.setString(3, cont_no);
					stmt6.setString(4, cont_rev_no);
					stmt6.setString(5, agmt_no);
					stmt6.setString(6, agmt_rev_no);
					stmt6.setString(7, contract_type);
					rset6 = stmt6.executeQuery();
					count = 0;
					if(rset6.next())
					{
						 count = rset6.getInt(1);
					}
					rset6.close();
					stmt6.close();
					
					if(count>0)
					{
						query = "DELETE FROM FMS_SUPPLY_CONT_GX_BU "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, cont_no);
						stmt3.setString(4, cont_rev_no);
						stmt3.setString(5, agmt_no);
						stmt3.setString(6, agmt_rev_no);
						stmt3.setString(7, contract_type);
						stmt3.executeUpdate();
						
						stmt3.close();
					}
					if(chk_gx_bu_plant!=null)
					{
						for(int i=0;i<chk_gx_bu_plant.length;i++)
						{
							query = "INSERT INTO FMS_SUPPLY_CONT_GX_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, GX_BU_SEQ_NO, ENT_BY, ENT_DT,"
									+ "GX_COUNTERPARTY_CD) "
									+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,"
									+ "?) ";
							stmt3 = dbcon.prepareStatement(query);
							stmt3.setString(1, comp_cd);
							stmt3.setString(2, counterparty_cd);
							stmt3.setString(3, agmt_no);
							stmt3.setString(4, agmt_rev_no);
							stmt3.setString(5, cont_no);
							stmt3.setString(6, cont_rev_no);
							stmt3.setString(7, contract_type);
							stmt3.setString(8, chk_gx_bu_plant[i]);
							stmt3.setString(9, emp_cd);
							stmt3.setString(10, gx_counterparty_cd);
							stmt3.executeUpdate();
							
							stmt3.close();
						 }
					}
				}
				
				String old_rev_no = ""+(Integer.parseInt(cont_rev_no)-1);
				query="INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,"
						+ "LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,"
						+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,?,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,"
						+ "LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,"
						+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,SYSDATE,?,SYSDATE "
						+ "FROM FMS_SUPPLY_CONT_LIABILITY A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
						+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=? AND NOT EXISTS("
						+ "SELECT * "
						+ "FROM FMS_SUPPLY_CONT_LIABILITY B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND CONT_REV=? "
						+ ")";
				stmt3 = dbcon.prepareStatement(query);
				stmt3.setString(1, cont_rev_no);
				stmt3.setString(2, emp_cd);
				stmt3.setString(3, comp_cd);
				stmt3.setString(4, counterparty_cd);
				stmt3.setString(5, agmt_no);
				stmt3.setString(6, agmt_rev_no);
				stmt3.setString(7, cont_no);
				stmt3.setString(8, contract_type);
				stmt3.setString(9, old_rev_no);
				stmt3.setString(10, cont_rev_no);
				stmt3.executeUpdate();
				
				if(opration.equals("INSERT"))
				{
					operation="INSERT";
				}
				else
				{
					operation="MODIFY";
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New "+cont_type_nm+" Contract "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - "+cont_type_nm+" Contract "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - "+cont_type_nm+" Contract "+cont_name_map+" Modification for "+counterparty_abbr+" Failed!";
				msg_type="E";
			}
			
			//<!--Harsh Maheta 20230903 : Added for new values to show in Deal audit history-->//
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			new_value ="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYPE="+agreement_type+"#AGMTBASE="+agreement_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#RATE="+rate+"#RATEUNIT="+rate_unit+"#TCQ="+tcq+"#DCQ="+dcq+"#QTYMOD="+var_tcq+"#QUNIT="+quantity_unit+"#MDCQ="+mdcq+"#GXFEE="+txn_charges+"#GXFEEUNIT="+txn_unit+"#POSTMARG="+post_margin+"#CONT_STATUS="+cont_status;
			
			if(option.equalsIgnoreCase("DLNG_CONT_MST"))
			{
				url = "../contract_master/frm_dlng_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+
						"&contract_type="+contract_type+commonUrl_pra;
				
			}
			else if(option.equalsIgnoreCase("SUPPLY_CONT_MST"))
			{
				url = "../contract_master/frm_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+
						"&contract_type="+contract_type+commonUrl_pra;
			}

			ContractMstMailBody(comp_cd,cp_name, cp_abbr, operation,msg,emp_cd,new_value,old_value);
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract "+cont_name_map+" Insert/Update Failed!";
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
	
	private void InsertUpdateContractBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateContractBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		String cont_type_nm = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");
			
			String freq=request.getParameter("freq")==null?"F":request.getParameter("freq");
			String billing_flag=request.getParameter("billing_flag")==null?"Y":request.getParameter("billing_flag");
			String billing_days=request.getParameter("billing_days")==null?"":request.getParameter("billing_days");
			String due_date=request.getParameter("due_date")==null?"":request.getParameter("due_date");
			String sec_due_date=request.getParameter("sec_due_date")==null?"":request.getParameter("sec_due_date");
			String inv_currency=request.getParameter("inv_currency")==null?"":request.getParameter("inv_currency");
			String payment_currency=request.getParameter("payment_currency")==null?"":request.getParameter("payment_currency");
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String plusmin=request.getParameter("plusmin")==null?"":request.getParameter("plusmin");
			String modeper=request.getParameter("modeper")==null?"":request.getParameter("modeper");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exch_calc_base=request.getParameter("exch_calc_base")==null?"":request.getParameter("exch_calc_base");
			String inv_criteria=request.getParameter("inv_criteria")==null?"":request.getParameter("inv_criteria");
			String exchg_rate_note=request.getParameter("exchg_rate_note")==null?"":request.getParameter("exchg_rate_note");
			
			String due_dt_in=request.getParameter("due_dt_in")==null?"":request.getParameter("due_dt_in");
			String exclude_sat=request.getParameter("exclude_sat")==null?"N":request.getParameter("exclude_sat");
			String exchg_val=request.getParameter("exchg_val")==null?"":request.getParameter("exchg_val");
			String holidayState_map = request.getParameter("holidayState_map")==null?"":request.getParameter("holidayState_map");
			
			String[] sat=request.getParameterValues("sat");
			String exclude_sat_map = "";
			
			String[] cform_flag=request.getParameterValues("cform_flag");
			String[] cform_bu_seq=request.getParameterValues("cform_bu_seq");
			String[] cform_plant_seq=request.getParameterValues("cform_plant_seq");

			if(exclude_sat.equals("Y"))
			{	
				if(sat!=null)
				{
					for(int i=0; i<sat.length; i++)
					{
						 if (sat[i].contains("Y")) 
						 {
			                if (exclude_sat_map.length() > 0) 
			                {
			                	exclude_sat_map+="-"; 
			                }
			                exclude_sat_map+=sat[i].charAt(0);
						 }
					}
				}
			}
			else
			{
				exclude_sat_map = "";
			}
			
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			cont_type_nm = utilBean.getContractTypeName(contract_type);
			
			String[] cust_plant_map = holidayState_map.split("@@");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
					
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, cont_no);
				stmt3.setString(4, cont_rev_no);
				stmt3.setString(5, agmt_no);
				stmt3.setString(6, agmt_rev_no);
				stmt3.setString(7, contract_type);
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String plant_seq = rset3.getString(1)==null?"":rset3.getString(1);
					String state_map = "";
					for(int i=0; i<cust_plant_map.length; i++)
					{
						if(cust_plant_map[i].startsWith(plant_seq))
						{
							state_map = cust_plant_map[i].split("//")[1];
						}
					}
				
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SUPPLY_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND CONTRACT_TYPE=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, eff_dt);
					stmt.setString(7, plant_seq);
					rset = stmt.executeQuery();
					count = 0;
					if(rset.next())
					{
						 count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count > 0)
					{
						int cnt=0;
						query="UPDATE FMS_SUPPLY_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ_NO=?";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt, freq);
						stmt1.setString(++cnt, billing_flag);
						stmt1.setString(++cnt, due_date);
						stmt1.setString(++cnt, sec_due_date);
						stmt1.setString(++cnt, inv_currency);
						stmt1.setString(++cnt, payment_currency);
						stmt1.setString(++cnt, rate);
						stmt1.setString(++cnt, plusmin);
						stmt1.setString(++cnt, modeper);
						stmt1.setString(++cnt, exchng_rate);
						stmt1.setString(++cnt, exch_calc_base);
						stmt1.setString(++cnt, inv_criteria);
						stmt1.setString(++cnt, exchg_rate_note);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, due_dt_in);
						stmt1.setString(++cnt, exclude_sat);
						stmt1.setString(++cnt, billing_days);
						stmt1.setString(++cnt, exchg_val);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, cont_no);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, eff_dt);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_SUPPLY_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,"
								+ "EFF_DT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?)";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, agmt_no);
						stmt1.setString(++cnt1, agmt_rev_no);
						stmt1.setString(++cnt1, cont_no);
						stmt1.setString(++cnt1, cont_rev_no);
						stmt1.setString(++cnt1, contract_type);
						stmt1.setString(++cnt1, freq);
						stmt1.setString(++cnt1, billing_flag);
						stmt1.setString(++cnt1, due_date);
						stmt1.setString(++cnt1, sec_due_date);
						stmt1.setString(++cnt1, inv_currency);
						stmt1.setString(++cnt1, payment_currency);
						stmt1.setString(++cnt1, rate);
						stmt1.setString(++cnt1, plusmin);
						stmt1.setString(++cnt1, modeper);
						stmt1.setString(++cnt1, exchng_rate);
						stmt1.setString(++cnt1, exch_calc_base);
						stmt1.setString(++cnt1, inv_criteria);
						stmt1.setString(++cnt1, exchg_rate_note);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, due_dt_in);
						stmt1.setString(++cnt1, exclude_sat);
						stmt1.setString(++cnt1, eff_dt);
						stmt1.setString(++cnt1, billing_days);
						stmt1.setString(++cnt1, exchg_val);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! "+cont_type_nm+" Contract ("+cont_name_map+") Billing Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				rset3.close();
				stmt3.close();
				
				//if(contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W"))
				{
					if(cform_flag!=null)
					{
						for(int i=0; i<cform_flag.length; i++)
						{
							if(!cform_flag[i].equals("0"))
							{
								int count=0;
								query = "SELECT COUNT(*) "
										+ "FROM FMS_SUPPLY_CFORM_DTL "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
										+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
										+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_SEQ=? "
										+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
								stmt = dbcon.prepareStatement(query);
								stmt.setString(1, comp_cd);
								stmt.setString(2, counterparty_cd);
								stmt.setString(3, cont_no);
								stmt.setString(4, agmt_no);
								stmt.setString(5, contract_type);
								stmt.setString(6, cform_plant_seq[i]);
								stmt.setString(7, cform_bu_seq[i]);
								stmt.setString(8, eff_dt);
								rset = stmt.executeQuery();
								count = 0;
								if(rset.next())
								{
									 count = rset.getInt(1);
								}
								rset.close();
								stmt.close();
								
								if(count > 0)
								{
									int cnt=0;
									query="UPDATE FMS_SUPPLY_CFORM_DTL SET CFORM_FLAG=?,MOD_DT=SYSDATE,MOD_BY=? "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
											+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
											+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_SEQ=? "
											+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
									stmt1 =dbcon.prepareStatement(query);
									stmt1.setString(++cnt, cform_flag[i]);
									stmt1.setString(++cnt, emp_cd);
									stmt1.setString(++cnt, comp_cd);
									stmt1.setString(++cnt, counterparty_cd);
									stmt1.setString(++cnt, cont_no);
									stmt1.setString(++cnt, agmt_no);
									stmt1.setString(++cnt, contract_type);
									stmt1.setString(++cnt, cform_plant_seq[i]);
									stmt1.setString(++cnt, cform_bu_seq[i]);
									stmt1.setString(++cnt, eff_dt);
									stmt1.executeUpdate();
									
									stmt1.close();
								}
								else
								{
									String commodity_type="";
									
									if(contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W")) 
									{
										commodity_type="DLNG";
									}
									else
									{
										commodity_type="RLNG";
									}
									
									int cnt1=0;
									query="INSERT INTO FMS_SUPPLY_CFORM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
											+ "PLANT_SEQ,BU_SEQ,COMMODITY_TYPE,CFORM_FLAG,ENT_DT,ENT_BY,EFF_DT) "
											+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,TO_DATE(?,'DD/MM/YYYY'))";
									stmt1 =dbcon.prepareStatement(query);
									stmt1.setString(++cnt1, comp_cd);
									stmt1.setString(++cnt1, counterparty_cd);
									stmt1.setString(++cnt1, agmt_no);
									stmt1.setString(++cnt1, agmt_rev_no);
									stmt1.setString(++cnt1, cont_no);
									stmt1.setString(++cnt1, cont_rev_no);
									stmt1.setString(++cnt1, contract_type);
									stmt1.setString(++cnt1, cform_plant_seq[i]);
									stmt1.setString(++cnt1, cform_bu_seq[i]);
									stmt1.setString(++cnt1, commodity_type);
									stmt1.setString(++cnt1, cform_flag[i]);
									stmt1.setString(++cnt1, emp_cd);
									stmt1.setString(++cnt1, eff_dt);
									stmt1.executeUpdate();
									
									stmt1.close();
								}
							}
						}
					}
				}
			}
			else
			{
				msg = "Failed! "+cont_type_nm+" Contract ("+cont_name_map+") Billing Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			if(contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W")) 
			{
				url = "../contract_master/frm_dlng_contract_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			}
			else
			{
				url = "../contract_master/frm_contract_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Gas Supply Contract Billing Detail Insert/Update Failed!";
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
	
	private void FCCforSupplyContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="FCCforSupplyContractDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		String cont_type_nm = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String fcc_flg=request.getParameter("fcc_flg")==null?"N":request.getParameter("fcc_flg");
			String cont_status_flg = fcc_flg;
			
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			cont_type_nm = utilBean.getContractTypeName(contract_type);
			
			query ="UPDATE FMS_SUPPLY_CONT_MST SET FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE, CONT_STATUS=? "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, fcc_flg);
			stmt.setString(2, emp_cd);
			stmt.setString(3, cont_status_flg);
			stmt.setString(4, comp_cd);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, cont_no);
			stmt.setString(7, cont_rev_no);
			stmt.setString(8, agmt_no);
			stmt.setString(9, agmt_rev_no);
			stmt.executeUpdate();
			
			stmt.close();
			
			generateSupplyContractLog(counterparty_cd, cont_no, cont_rev_no, agmt_no, agmt_rev_no, contract_type, emp_cd);
			
			if(fcc_flg.equals("Y"))
			{
				msg = "Successful! "+cont_type_nm+" Contract ("+cont_name_map+") for "+counterparty_abbr+" FCC Approved!";
			}
			else
			{
				msg = "Successful! "+cont_type_nm+" Contract ("+cont_name_map+") for "+counterparty_abbr+" FCC Disapproved!";
			}
			msg_type="S";
			
			if(option.equalsIgnoreCase("DLNG_CONT_FCC"))
			{
				url = "../contract_master/frm_dlng_fcc_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+
						"&contract_type="+contract_type+commonUrl_pra;
			}
			else if(option.equalsIgnoreCase("SUPPLY_CONT_FCC"))
			{
				url = "../contract_master/frm_contract_mst_fcc.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+
						"&contract_type="+contract_type+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Gas Supply Contract FCC Check Failed";
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
	
	private void InsertUpdateVariableDcqDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateVariableDcqDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String start_dt=request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String end_dt=request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String dcq = request.getParameter("dcq")==null?"":request.getParameter("dcq");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String status_flag = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String sequence_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");

			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(opration.equals("INSERT")) 
			{
				if(from_dt!=null)
				{
					
					String seq_no="1";
					query="SELECT MAX(SEQ_NO) "
							+ "FROM FMS_SUPPLY_CONT_DCQ_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?";
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
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_SUPPLY_CONT_DCQ_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "SEQ_NO,FROM_DT,TO_DT,DCQ,REMARK,"
							+ "STATUS,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev_no);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, seq_no);
					stmt1.setString(9, from_dt);
					stmt1.setString(10, to_dt);
					stmt1.setString(11, dcq);
					stmt1.setString(12, remark);
					stmt1.setString(13, status_flag);
					stmt1.setString(14, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - "+cont_name_map+" Variable DCQ Data for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				
				}
				else
				{
					msg = "Failed! - Data submission Failed!";
					msg_type = "E";
				}
			}
			else if (opration.equals("MODIFY"))
			{
				if(from_dt!=null)
				{
					query1="UPDATE FMS_SUPPLY_CONT_DCQ_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'), TO_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "DCQ=?, REMARK=?, STATUS=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND SEQ_NO=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, from_dt);
					stmt1.setString(2, to_dt);
					stmt1.setString(3, dcq);
					stmt1.setString(4, remark);
					stmt1.setString(5, status_flag);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, counterparty_cd);
					stmt1.setString(8, agmt_no);
					stmt1.setString(9, cont_no);
					stmt1.setString(10, contract_type);
					stmt1.setString(11, sequence_no);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - "+cont_name_map+" Variable DCQ Data for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - Data Modification Failed!";
					msg_type = "E";
				}
			}
			
			url = "../contract_master/frm_contract_dcq_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&start_dt="+start_dt+"&end_dt="+end_dt+
					"&contract_type="+contract_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Variable DCQ Insert/Update Failed";
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
	
	private void ContractMstMailBody(String comp_cd,String cp_name,String cp_abbr,String operation,String msg,String emp_cd,String new_values, String old_values) throws Exception
	{
		String function_nm="ContractMstMailBody()";
		String mailBody="";
		try
		{
			String contractDetail="";
			String cont_no = "",old_cont_no = "";
			String cont_ref_no = "",old_cont_ref_no = "";

			if(!new_values.equals(""))
			{
				//System.out.println(old_values);
				
				String cp="",old_cp="";
				String name="",old_name="";
				String abbr="",old_abbr="";
				String cont_name = "",old_cont_name = "";
				String contract_type="",old_contract_type="";
				String trade_ref_no="",old_trade_ref_no="";
				String dda_dt="",old_dda_dt="";
				String dda_time="",old_dda_time="";
				String signing_dt="",old_signing_dt="";
				String signing_time="",old_signing_time="";
				String txn_charges="",old_txn_charges="";
				String txn_unit="",old_txn_unit="";
				String post_margin="",old_post_margin="";
				
				String ent_dt="",old_ent_dt="";
				String ent_time="",old_ent_time="";
				String agmt_type="",old_agmt_type="";
				String agmt_base="",old_agmt_base="";
				String start_dt="",old_start_dt="";
				String end_dt="",old_end_dt="";
				String rate="",old_rate="";
				String rate_unit="",old_rate_unit="";
				String tcq="",old_tcq="";
				String dcq="",old_dcq="";
				String var_qty="",old_var_qty="";
				String quantity_unit="",old_quantity_unit="";
				String mdcq_percentage="",old_mdcq_percentage="";
				String cont_status="";String old_cont_status="";
				String cont_type_nm = "";
				
				String split_New_Value[] = new_values.split("#");
				for(int i=0; i<split_New_Value.length; i++)
				{
					if(split_New_Value[i].startsWith("CP=")){
						String temp[] = split_New_Value[i].split("CP=");
						if(temp.length>0){
							cp=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("NAME=")){
						String temp[] = split_New_Value[i].split("NAME=");
						if(temp.length>0){
							name=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ABBR=")){
						String temp[] = split_New_Value[i].split("ABBR=");
						if(temp.length>0){
							abbr=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONTNAME=")){
						String temp[] = split_New_Value[i].split("CONTNAME=");
						if(temp.length>0){
							cont_name=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONTNO=")){
						String temp[] = split_New_Value[i].split("CONTNO=");
						if(temp.length>0){
							cont_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONTREFNO=")){
						String temp[] = split_New_Value[i].split("CONTREFNO=");
						if(temp.length>0){
							cont_ref_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONTTYPE=")){
						String temp[] = split_New_Value[i].split("CONTTYPE=");
						if(temp.length>0){
							contract_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("TRADE_REFNO=")){
						String temp[] = split_New_Value[i].split("TRADE_REFNO=");
						if(temp.length>0){
							trade_ref_no=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DDADT=")){
						String temp[] = split_New_Value[i].split("DDADT=");
						if(temp.length>0){
							dda_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DDATIME=")){
						String temp[] = split_New_Value[i].split("DDATIME=");
						if(temp.length>0){
							dda_time=temp[1];
						}
					}
					
					if(split_New_Value[i].startsWith("SIGNDT=")){
						String temp[] = split_New_Value[i].split("SIGNDT=");
						if(temp.length>0){
							signing_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("SIGNTIME=")){
						String temp[] = split_New_Value[i].split("SIGNTIME=");
						if(temp.length>0){
							signing_time=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ENTDT=")){
						String temp[] = split_New_Value[i].split("ENTDT=");
						if(temp.length>0){
							ent_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ENTTIME=")){
						String temp[] = split_New_Value[i].split("ENTTIME=");
						if(temp.length>0){
							ent_time=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("AGMTTYPE=")){
						String temp[] = split_New_Value[i].split("AGMTTYPE=");
						if(temp.length>0){
							agmt_type=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("AGMTBASE=")){
						String temp[] = split_New_Value[i].split("AGMTBASE=");
						if(temp.length>0){
							agmt_base=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("STARTDT=")){
						String temp[] = split_New_Value[i].split("STARTDT=");
						if(temp.length>0){
							start_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("ENDDT=")){
						String temp[] = split_New_Value[i].split("ENDDT=");
						if(temp.length>0){
							end_dt=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("RATE=")){
						String temp[] = split_New_Value[i].split("RATE=");
						if(temp.length>0){
							rate=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("RATEUNIT=")){
						String temp[] = split_New_Value[i].split("RATEUNIT=");
						if(temp.length>0){
							rate_unit=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("TCQ=")){
						String temp[] = split_New_Value[i].split("TCQ=");
						if(temp.length>0){
							tcq=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("DCQ=")){
						String temp[] = split_New_Value[i].split("DCQ=");
						if(temp.length>0){
							dcq=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("QTYMOD=")){
						String temp[] = split_New_Value[i].split("QTYMOD=");
						if(temp.length>0){
							var_qty=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("QUNIT=")){
						String temp[] = split_New_Value[i].split("QUNIT=");
						if(temp.length>0){
							quantity_unit=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("MDCQ=")){
						String temp[] = split_New_Value[i].split("MDCQ=");
						if(temp.length>0){
							mdcq_percentage=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GXFEE=")){
						String temp[] = split_New_Value[i].split("GXFEE=");
						if(temp.length>0){
							txn_charges=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("GXFEEUNIT=")){
						String temp[] = split_New_Value[i].split("GXFEEUNIT=");
						if(temp.length>0){
							txn_unit=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("POSTMARG=")){
						String temp[] = split_New_Value[i].split("POSTMARG=");
						if(temp.length>0){
							post_margin=temp[1];
						}
					}
					if(split_New_Value[i].startsWith("CONT_STATUS=")){
						String temp[] = split_New_Value[i].split("CONT_STATUS=");
						if(temp.length>0){
							cont_status=temp[1];
						}
					}
				}
				
				if(!old_values.equals(""))
				{
					String split_Old_Value[] = old_values.split("#");
					for(int i=0; i<split_Old_Value.length; i++)
					{
						if(split_Old_Value[i].startsWith("CP=")){
							String temp[] = split_Old_Value[i].split("CP=");
							if(temp.length>0){
								old_cp=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("NAME=")){
							String temp[] = split_Old_Value[i].split("NAME=");
							if(temp.length>0){
								old_name=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ABBR=")){
							String temp[] = split_Old_Value[i].split("ABBR=");
							if(temp.length>0){
								old_abbr=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONTNAME=")){
							String temp[] = split_Old_Value[i].split("CONTNAME=");
							if(temp.length>0){
								old_cont_name=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONTNO=")){
							String temp[] = split_Old_Value[i].split("CONTNO=");
							if(temp.length>0){
								old_cont_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONTREFNO=")){
							String temp[] = split_Old_Value[i].split("CONTREFNO=");
							if(temp.length>0){
								old_cont_ref_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONTTYPE=")){
							String temp[] = split_Old_Value[i].split("CONTTYPE=");
							if(temp.length>0){
								old_contract_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("TRADE_REFNO=")){
							String temp[] = split_Old_Value[i].split("TRADE_REFNO=");
							if(temp.length>0){
								old_trade_ref_no=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DDADT=")){
							String temp[] = split_Old_Value[i].split("DDADT=");
							if(temp.length>0){
								old_dda_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DDATIME=")){
							String temp[] = split_Old_Value[i].split("DDATIME=");
							if(temp.length>0){
								old_dda_time=temp[1];
							}
						}
						
						if(split_Old_Value[i].startsWith("SIGNDT=")){
							String temp[] = split_Old_Value[i].split("SIGNDT=");
							if(temp.length>0){
								old_signing_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("SIGNTIME=")){
							String temp[] = split_Old_Value[i].split("SIGNTIME=");
							if(temp.length>0){
								old_signing_time=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ENTDT=")){
							String temp[] = split_Old_Value[i].split("ENTDT=");
							if(temp.length>0){
								old_ent_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ENTTIME=")){
							String temp[] = split_Old_Value[i].split("ENTTIME=");
							if(temp.length>0){
								old_ent_time=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("AGMTTYPE=")){
							String temp[] = split_Old_Value[i].split("AGMTTYPE=");
							if(temp.length>0){
								old_agmt_type=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("AGMTBASE=")){
							String temp[] = split_Old_Value[i].split("AGMTBASE=");
							if(temp.length>0){
								old_agmt_base=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("STARTDT=")){
							String temp[] = split_Old_Value[i].split("STARTDT=");
							if(temp.length>0){
								old_start_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("ENDDT=")){
							String temp[] = split_Old_Value[i].split("ENDDT=");
							if(temp.length>0){
								old_end_dt=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("RATE=")){
							String temp[] = split_Old_Value[i].split("RATE=");
							if(temp.length>0){
								old_rate=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("RATEUNIT=")){
							String temp[] = split_Old_Value[i].split("RATEUNIT=");
							if(temp.length>0){
								old_rate_unit=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("TCQ=")){
							String temp[] = split_Old_Value[i].split("TCQ=");
							if(temp.length>0){
								old_tcq=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("DCQ=")){
							String temp[] = split_Old_Value[i].split("DCQ=");
							if(temp.length>0){
								old_dcq=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("QTYMOD=")){
							String temp[] = split_Old_Value[i].split("QTYMOD=");
							if(temp.length>0){
								old_var_qty=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("QUNIT=")){
							String temp[] = split_Old_Value[i].split("QUNIT=");
							if(temp.length>0){
								old_quantity_unit=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("MDCQ=")){
							String temp[] = split_Old_Value[i].split("MDCQ=");
							if(temp.length>0){
								old_mdcq_percentage=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GXFEE=")){
							String temp[] = split_Old_Value[i].split("GXFEE=");
							if(temp.length>0){
								old_txn_charges=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("GXFEEUNIT=")){
							String temp[] = split_Old_Value[i].split("GXFEEUNIT=");
							if(temp.length>0){
								old_txn_unit=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("POSTMARG=")){
							String temp[] = split_Old_Value[i].split("POSTMARG=");
							if(temp.length>0){
								old_post_margin=temp[1];
							}
						}
						if(split_Old_Value[i].startsWith("CONT_STATUS=")){
							String temp[] = split_Old_Value[i].split("CONT_STATUS=");
							if(temp.length>0){
								old_cont_status=temp[1];
							}
						}
					}
				}
				if(agmt_base.equals("X")) 
				{
					agmt_base="Ex-Terminal";
				}
				else if (agmt_base.equals("D"))
				{
					agmt_base="Delivery";
				}
				if(agmt_type.equals("0")) 
				{
					agmt_type="Term";
				}
				else if (agmt_type.equals("1"))
				{
					agmt_type="Spot";
				}

				if(old_agmt_base.equals("X")) 
				{
					old_agmt_base="Ex-Terminal";
				}
				else if (old_agmt_base.equals("D"))
				{
					old_agmt_base="Delivery";
				}
				if(old_agmt_type.equals("0")) 
				{
					old_agmt_type="Term";
				}
				else if (old_agmt_type.equals("1"))
				{
					old_agmt_type="Spot";
				}
				
				if(rate_unit.equals("1")) 
				{
					rate_unit="INR/MMBTU";
				}
				else if (rate_unit.equals("2"))
				{
					rate_unit="USD/MMBTU";
				}
				
				if(quantity_unit.equals("1")) 
				{
					quantity_unit="MMBTU";
				}
				else if (quantity_unit.equals("2"))
				{
					quantity_unit="TBTU";
				}
				if(txn_unit.equals("1")) 
				{
					txn_unit="INR/MMBTU";
				}
				else if (txn_unit.equals("2"))
				{
					txn_unit="USD/MMBTU";
				}
				
				if(contract_type.equals("D")) 
				{
					cont_type_nm="Domestic NG";
				}
				else if (contract_type.equals("I"))
				{
					cont_type_nm="IGX";
				}
				else if (contract_type.equals("S"))
				{
					cont_type_nm="Supply Notice";
				}
				else if (contract_type.equals("L"))
				{
					cont_type_nm="Letter of Agreement";
				}
				else if (contract_type.equals("X"))
				{
					cont_type_nm="IGX";
				}
				else if (contract_type.equals("F"))
				{
					cont_type_nm="DLNG Supply Notice";
				}
				else if (contract_type.equals("E"))
				{
					cont_type_nm="DLNG Letter of Agreement";
				}
				else if (contract_type.equals("W"))
				{
					cont_type_nm="DLNG IGX";
				}
				
				if(!cp.equals(""))
				{
					if(old_values.equals("")) 
					{
						cont_status = "New";
						
						contractDetail="Name : "+name+"<br>"
								+ "Abbreviation : "+abbr+"<br>"
								+ "Contract Ref#: "+cont_ref_no+"<br>"
								+ "Trader Ref#: "+trade_ref_no+"<br>"
								+ "Contract Type : "+cont_type_nm+"<br>"
								+ "DDA Date: "+dda_dt+" "+dda_time+"<br>"
								+ "Signing Date : "+signing_dt+" "+signing_time+"<br>"
								+ "Deal Enter Date : "+ent_dt+" "+ent_time+"<br>";
								if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")||contract_type.equals("F")||contract_type.equals("E")||contract_type.equals("W")) 
								{
									contractDetail+= "Agreement Type : "+agmt_type+"<br>"
													+ "Agreement Base 	: "+agmt_base+"<br>";
								}
								contractDetail+= "Start Date : "+start_dt+"<br>"
								+ "End Date : "+end_dt+"<br>"
								+ "Gas Price: "+rate+" "+rate_unit+"<br>"
								+ "TCQ : "+tcq+" "+quantity_unit+"<br>"
								+ "DCQ : "+dcq+"<br>"
								//+ "Quantity Modification : "+var_qty+"<br>"
								+ "MDCQ(%) : "+mdcq_percentage+"<br>";
								if(contract_type.equals("X")||contract_type.equals("I")||contract_type.equals("W")) 
								{
									contractDetail+= "GX Transaction Fee : "+txn_charges+" "+txn_unit+"<br>"
													+ "Post Trade Margin(%) : "+post_margin+"<br>";
								}
					}
					else
					{
						//deal_status="";
						
						if(!name.equals(old_name)){
							contractDetail+="Name : "+name+"<br>";
						}
						if(!abbr.equals(old_abbr)) {
							contractDetail+= "Abbreviation : "+abbr+"<br>";
						}
						if(!cont_ref_no.equals(old_cont_ref_no)) {
							contractDetail += "Contract Ref#: "+cont_ref_no+"<br>";
						}
						if(!contract_type.equals(old_contract_type)) {
							contractDetail += "Contract Type : "+cont_type_nm+"<br>";
						}
						if(!trade_ref_no.equals(old_trade_ref_no)) {
							contractDetail += "Trader Ref#: "+trade_ref_no+"<br>";
						}
						if(!dda_dt.equals(old_dda_dt) || !dda_time.equals(old_dda_time)) {
							contractDetail +=  "DDA Date: "+dda_dt+" "+dda_time+"<br>";
						}
						if(!signing_dt.equals(old_signing_dt) || !signing_time.equals(old_signing_time)) {
							contractDetail += "Signing Date : "+signing_dt+" "+signing_time+"<br>";
						}
						if(!ent_dt.equals(old_ent_dt)||!ent_time.equals(old_ent_time)) {
							contractDetail += "Deal Enter Date : "+ent_dt+" "+ent_time+"<br>";
						}
						if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")||contract_type.equals("F")||contract_type.equals("E")||contract_type.equals("W")) 
						{
							if(!agmt_type.equals(old_agmt_type)) {
								contractDetail += "Agreement Type : "+agmt_type+"<br>";
							}
							if(!agmt_base.equals(old_agmt_base)) {
								contractDetail += "Agreement Base : "+agmt_base+"<br>";
							}
						}
						if(!start_dt.equals(old_start_dt)) {
							contractDetail += "Start Date : "+start_dt+"<br>";
						}
						if(!end_dt.equals(old_end_dt)) {
							contractDetail+="End Date : "+end_dt+"<br>";
						}
						if(!rate.equals(old_rate)) {
							contractDetail+= "Gas Price: "+rate+" "+rate_unit+"<br>";
						}
						if(!tcq.equals(old_tcq)) {
							contractDetail+= "TCQ : "+tcq+" "+quantity_unit+"<br>";
						}
						if(!dcq.equals(old_dcq)) {
							contractDetail+=  "DCQ : "+dcq+"<br>";
						}
//						if(!var_qty.equals(var_qty)) {
//							contractDetail+=  "Quantity Modification : "+var_qty+"<br>";
//						}
						if(!mdcq_percentage.equals(old_mdcq_percentage)) {
							contractDetail+=  "MDCQ(%) : "+mdcq_percentage+"<br>";
						}
						if(contract_type.equals("X")||contract_type.equals("I"))
						{
							if(!txn_charges.equals(old_txn_charges)) {
								contractDetail+=  "GX Transaction Fee : "+txn_charges+" "+txn_unit+"<br>";
							}
							if(!post_margin.equals(old_post_margin)) {
								contractDetail+=  "Post Trade Margin(%) : "+post_margin+"<br>";
							}
						}
					}
				}
			}
			if(!contractDetail.isEmpty()) 
			{
				String subject="Contract# "+cont_no+" ["+cont_ref_no+"]"+" for Counterparty "+cp_abbr+" - "+cp_name+" : ";
				if(operation.equals("MODIFY"))
				{
					subject+= "Modified Successfully!";
				}
				else
				{
					subject+= "Inserted Successfully!";
				}
				
				mailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Contract# "+cont_no+" ["+cont_ref_no+"]"+" for Counterparty <b>"+cp_abbr+" - "+cp_name+"</b> ";
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
				
				if(!contractDetail.equals(""))
				{
					mailBody+="<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'><b>Contract Detail :<br></b>"+contractDetail+"</span><br><br>";
				}
				
				mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
						+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
						+ "</html>";
				
				String to_mail_list_contractMst = utilBean.getToMailReceipentList(dbcon,comp_cd,"Deal Audit Notification", "Risk Mgmt", "NA", "On-Event");
				
				String cc_mail_list_contractMst=utilBean.getCcMailReceipentList(dbcon,comp_cd,"Deal Audit Notification", "Risk Mgmt", "NA", "On-Event");
				
				if(!to_mail_list_contractMst.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list_contractMst, subject, mailBody, "", cc_mail_list_contractMst, "");
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
		
	private void updateSupplyContractBillingEffectiveDate(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String start_dt, String new_start_dt) throws Exception
	{
		String function_nm="updateSupplyContractBillingEffectiveDate()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, start_dt);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int new_billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_start_dt);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(billing_count > 0 && new_billing_count == 0)
			{
				query ="UPDATE FMS_SUPPLY_BILLING_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, new_start_dt);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, start_dt);
				stmt.setString(6, agmt_no);
				stmt.setString(7, agmt_rev_no);
				stmt.setString(8, contract_type);
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void generateSupplyContractLog(String counterparty_cd,String cont_no,String cont_rev_no, String agmt_no, String agmt_rev_no, String contract_type, String emp_cd) throws Exception
	{
		String function_nm="generateSupplyContractLog()";
		try
		{
			//ADD LOG IN LOG TABLE
			int log_seq_no=1;
			query = "SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt1 =dbcon.prepareStatement(query);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, cont_no);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, contract_type);
			rset1 = stmt1.executeQuery();
			if(rset1.next())
			{
				log_seq_no = (rset1.getInt(1)+1);
			}
			rset1.close();
			stmt1.close();
			
			query="INSERT INTO LOG_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT, "
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
					+ "MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,"
					+ "DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,"
					+ "TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ, "
					+ "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR, "
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, LIABILITY, "
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG ,TERMINATE_CLAUSE,TERMINATE_PLANED , "
					+ "TERMINATE_FORCE,SF_GEN_DT ,CLOSE_EFF_DT ,CLOSURE_REQUEST_FLAG ,CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST,CHANGE_DATE_APPROVE,CHANGE_START_DT,CHANGE_END_DT) "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,?,?,SYSDATE,"
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,"
					+ "SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
					+ "MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,"
					+ "PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,MEASUREMENT,MEASUREMENT_CLAUSE,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR, "
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, LIABILITY, "
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG ,TERMINATE_CLAUSE,TERMINATE_PLANED , "
					+ "TERMINATE_FORCE,SF_GEN_DT ,CLOSE_EFF_DT ,CLOSURE_REQUEST_FLAG ,CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST,CHANGE_DATE_APPROVE,CHANGE_START_DT,CHANGE_END_DT "
					+ "FROM FMS_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			stmt0 = dbcon.prepareStatement(query);
			stmt0.setInt(1, log_seq_no);
			stmt0.setString(2, emp_cd);
			stmt0.setString(3, comp_cd);
			stmt0.setString(4, counterparty_cd);
			stmt0.setString(5, cont_no);
			stmt0.setString(6, cont_rev_no);
			stmt0.setString(7, agmt_no);
			stmt0.setString(8, agmt_rev_no);
			stmt0.setString(9, contract_type);
			stmt0.executeUpdate();
			
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void updateContractBillingPlant(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String plant_seq, String new_plant_seq ,int new_plant_cnt) throws Exception
	{
		String function_nm="updateContractBillingPlant()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int new_billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String hol_state=utilBean.getState_TIN(dbcon, comp_cd, counterpty_cd, "C", new_plant_seq);
			if(hol_state.equals(""))
			{
				hol_state="24";
			}
			if(billing_count > 0 && new_billing_count == 0)
			{
				if(new_plant_cnt==1)
				{
					query ="UPDATE FMS_SUPPLY_BILLING_DTL A SET PLANT_SEQ_NO=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_NO=? "
							+ "AND CONTRACT_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, new_plant_seq);
					stmt.setString(2, comp_cd);
					stmt.setString(3, counterpty_cd);
					stmt.setString(4, cont_no);
					stmt.setString(5, plant_seq);
					stmt.setString(6, agmt_no);
					stmt.setString(7, contract_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else
				{
					query="INSERT INTO FMS_SUPPLY_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EFF_DT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EFF_DT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,? "
							+ "FROM FMS_SUPPLY_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_SUPPLY_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, new_plant_seq);
					stmt0.setString(2, hol_state);
					stmt0.setString(3, comp_cd);
					stmt0.setString(4, counterpty_cd);
					stmt0.setString(5, cont_no);
					stmt0.setString(6, agmt_no);
					stmt0.setString(7, agmt_rev_no);
					stmt0.setString(8, contract_type);
					stmt0.setString(9, plant_seq);
					stmt0.setString(10, new_plant_seq);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
			}
			else
			{
				query ="UPDATE FMS_SUPPLY_BILLING_DTL A SET HOLIDAY_STATE=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO) AND HOLIDAY_STATE IS NULL";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, hol_state);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, new_plant_seq);
				stmt.setString(6, agmt_no);
				stmt.setString(7, contract_type);
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
}
