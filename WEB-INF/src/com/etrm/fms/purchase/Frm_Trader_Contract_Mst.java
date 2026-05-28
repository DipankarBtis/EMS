package com.etrm.fms.purchase;

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

@WebServlet("/servlet/Frm_Trader_Contract_Mst")
public class Frm_Trader_Contract_Mst extends HttpServlet
{
	static String db_src_file_name="Frm_Trader_Contract_Mst.java";
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
	
	private static ResultSet rset = null;
	private static ResultSet rset0 = null;
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
					
					if(option.equalsIgnoreCase("TRADER_CONT_MST"))
					{
						InsertUpdateTraderContractDetail(request);
					}
					else if(option.equalsIgnoreCase("TRADER_CONT_FCC"))
					{
						FCCforTraderContractDetail(request);
					}
					else if(option.equalsIgnoreCase("VARIABLE_DCQ"))
					{
						InsertUpdateVariableDcqDetail(request);
					}
					else if(option.equalsIgnoreCase("TRADER_CONT_LINK_TRANS"))
					{
						TraderContractLinkTrans(request);
					}
					else if(option.equalsIgnoreCase("TRADER_BILLING_DTL"))
					{
						InsertUpdateTraderBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("TRADER_CONT_LIABILITY_CLAUSE"))
					{
						InsertUpdateContLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("BUY_CLOSURE"))
					{
						ApproveRejectBuyContClosure(request);
					}
					else if(option.equalsIgnoreCase("REOPEN_CLOSURE"))
					{
						ApproveRejectBuyContReOpen(request);
					}
					if(option.equalsIgnoreCase("CONTRACT_DURATION_MODIFICTION"))
					{
						modifyContractDuration(request);
					}
					else if(option.equalsIgnoreCase("CONTRACT_DURATION_MODIFICATION_APPROVE"))
					{
						modifyContractDurationApproval(request);
					}
					else if(option.equalsIgnoreCase("MAP_PUR_CTR_MST"))		//PB 20260312: for adding the CTR
					{
						insertModifyPurCtrDtl(request);
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
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
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
			String user_cd = (String)session.getAttribute("user_cd")==null?"":(String)session.getAttribute("user_cd");
			
			String agmt_no[] = request.getParameterValues("agmt_no");
			String agmt_rev[] = request.getParameterValues("agmt_rev");
			String cont_no[] = request.getParameterValues("cont_no");
			String cont_rev[] = request.getParameterValues("cont_rev");
			String cont_type[] = request.getParameterValues("cont_type");
			
			String new_start_dt[] = request.getParameterValues("new_start_dt");
			String new_end_dt[] = request.getParameterValues("new_end_dt");
			
			if(cont_no != null)
			{
				for(int i=0;i<cont_no.length; i++)
				{
					String cont_name_map = counterparty_abbr+"-"+cont_type[i]+agmt_no[i]+"-"+cont_no[i];
					query1="SELECT COUNT(*) FROM FMS_TRADER_CONT_MST "
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
							query = "UPDATE FMS_TRADER_CONT_MST SET CHANGE_START_DT=TO_DATE(?,'DD/MM/YYYY'), CHANGE_END_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_BY=?,MODIFY_DT=SYSDATE "
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
							
							InsertPurchaseLog(customer_cd,agmt_no[i],agmt_rev[i],cont_no[i],cont_rev[i],cont_type[i],emp_cd);
						}
					}
					rset1.close();
					stmt1.close();
					
					msg="Trader Contract "+cont_name_map+" Duration Change Request initiated";
					msg_type="S";
					dbcon.commit();
				}
			}
			
			url="../purchase/frm_trader_contract_duration_modification.jsp?customer_cd="+customer_cd+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
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
						query = "UPDATE FMS_TRADER_CONT_MST SET CHANGE_DATE_REQ=?,CHANGE_START_DT=?,CHANGE_END_DT=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
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
						InsertPurchaseLog(customer_cd,agmt_no[i],agmt_rev[i],cont_no[i],cont_rev[i],cont_type[i],emp_cd);
						
						dbcon.commit();
						msg = "Trader Contract Duration Change Request for "+cont_name_map+" Rejected !!!";
						msg_type="S";
					}
					else
					{
						query = "UPDATE FMS_TRADER_CONT_MST SET CHANGE_DATE_APPROVE=?,CHANGE_DATE_REQ=?,"
								+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),CHANGE_START_DT=?,CHANGE_END_DT=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
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
						
						InsertPurchaseLog(customer_cd,agmt_no[i],agmt_rev[i],cont_no[i],cont_rev[i],cont_type[i],emp_cd);
						
						dbcon.commit();
						msg = "Trader Contract Duration Change Request for "+cont_name_map+" Approved !";
						msg_type="S";
					}
				}
			}
			url="../purchase/frm_trader_contract_duration_modification.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Sale Price Change Modification Failed";
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
	
	private void ApproveRejectBuyContReOpen(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ApproveRejectBuyContReOpen()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
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
			String trader_cd=request.getParameter("trader_cd")==null?"":request.getParameter("trader_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon, trader_cd);
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, trader_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(opration.equals("REJECT"))
			{
				if(!trader_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
				{
					String query = "UPDATE FMS_TRADER_CONT_MST A "
							+ "SET A.CLOSURE_REQUEST_FLAG=? "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
							+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query);
					stmt1.setString(1,"X");
					stmt1.setString(2,comp_cd);
					stmt1.setString(3,trader_cd);
					stmt1.setString(4,agmt_no);
					stmt1.setString(5,agmt_rev_no);
					stmt1.setString(6,cont_no);
					stmt1.setString(7,contract_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					InsertPurchaseLog(trader_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,emp_cd);
					
					msg = "Successful! "+cont_name_map+" Re-Open Request Rejected Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! -  "+cont_name_map+" Re-Open Request Rejection submission Failed!";
					msg_type = "E";
				}
				
			}
			else if(opration.equals("APPROVE"))
			{
				if(!trader_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
				{
					//incrementing the new contract revision no.
					int new_cont_rev=0;
					String query1="SELECT COUNT(*) FROM FMS_TRADER_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
					stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(1,comp_cd);
					stmt1.setString(2,trader_cd);
					stmt1.setString(3,agmt_no);
					stmt1.setString(4,agmt_rev_no);
					stmt1.setString(5,cont_no);
					stmt1.setString(6,contract_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						new_cont_rev=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+contract_type+cont_no+"-REV"+new_cont_rev;
					
					//inserting the data to the database 
					String query="INSERT INTO FMS_TRADER_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
							+ "CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,"
							+ "QUANTITY_UNIT,RATE,RATE_UNIT,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,"
							+ "BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,"
							+ "SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE, "
							+ "REMARK,ENT_DT,ENT_BY,CONT_NAME,CONTRACT_TYPE,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
							+ "DDA_DT,DDA_TIME,TXN_UNIT, MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
							+ "OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
							+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DAY_DEF_CLAUSE,"
							+ "MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,?,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,"
							+ "SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,POST_MARGIN,"
							+ "TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
							+ "MDCQ_FLAG,MDCQ_PERCENTAGE,REMARK,SYSDATE,?,?,CONTRACT_TYPE,?,TXN_CHARGE,BUYER_FORNGT_NOM,"
							+ "SELLER_FORNGT_NOM, DDA_DT,DDA_TIME,TXN_UNIT, MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
							+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,"
							+ "LIABILITY_CLAUSE, BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
							+ "DAY_DEF_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,cont_name);
					stmt.setString(4,"R");
					stmt.setString(5,comp_cd);
					stmt.setString(6,agmt_no);
					stmt.setString(7,agmt_rev_no);
					stmt.setString(8,cont_no);
					stmt.setString(9,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					//Now INSERT the new revision value data in the impacted tables 
					
					//TRADER PLANT
					query="INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
							+ "CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT,SPLIT_VALUE) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
							+ "CONT_NO, ?, CONTRACT_TYPE,PLANT_SEQ_NO, ?, SYSDATE,SPLIT_VALUE "
							+ "FROM FMS_TRADER_CONT_PLANT A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,trader_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					//PLANT CHARGE
					query="INSERT INTO FMS_TRADER_CONT_PLANT_CHRG(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, "
							+ "CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, EFF_DT, CHARGE_ABBR, CHARGE_RATE,ENT_BY, ENT_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, ?, CONTRACT_TYPE,PLANT_SEQ_NO,"
							+ "EFF_DT, CHARGE_ABBR, CHARGE_RATE,?, SYSDATE "
							+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,trader_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					//OTHER TRADER PLANT
					query="INSERT INTO FMS_TRADER_CONT_SPLIT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,"
							+ "CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT,SPLIT_VALUE,SPLIT_TRADER_CD) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,?, "
							+ "CONTRACT_TYPE,PLANT_SEQ_NO,?, SYSDATE,SPLIT_VALUE,SPLIT_TRADER_CD "
							+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD  "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,trader_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					//BUSINESS UNIT
					query="INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE,PLANT_SEQ_NO, ?, SYSDATE "
							+ "FROM FMS_TRADER_CONT_BU A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,trader_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					//GX BUSINESS UNIT
					query="INSERT INTO FMS_TRADER_CONT_GX_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, GX_BU_SEQ_NO, ENT_BY, ENT_DT,GX_COUNTERPARTY_CD) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, ?, CONTRACT_TYPE, GX_BU_SEQ_NO, ?, SYSDATE,GX_COUNTERPARTY_CD "
							+ "FROM FMS_TRADER_CONT_GX_BU A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_GX_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,""+new_cont_rev);
					stmt.setString(2,emp_cd);
					stmt.setString(3,comp_cd);
					stmt.setString(4,trader_cd);
					stmt.setString(5,agmt_no);
					stmt.setString(6,agmt_rev_no);
					stmt.setString(7,cont_no);
					stmt.setString(8,contract_type);
					stmt.executeUpdate();
					stmt.close();
					
					InsertPurchaseLog(trader_cd,agmt_no,agmt_rev_no,cont_no,""+new_cont_rev,contract_type,emp_cd);
					
					msg = "Successful! "+cont_name_map+" Re-Open Request Approval Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! -  "+cont_name_map+" Re-Open Request Approval submission Failed!";
					msg_type = "E";
				}
			}
			
			url = "../purchase/frm_pur_reopen_cont.jsp?msg=" + msg + "&msg_type=" + msg_type+ commonUrl_pra;
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Contract Re-Open Request Approve/Reject Failed";
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
	
	private void ApproveRejectBuyContClosure(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ApproveRejectBuyContClosure()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		String closure_request_flag=request.getParameter("closure_request_flag")==null?"":request.getParameter("closure_request_flag");
		try
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
			String trader_cd=request.getParameter("trader_cd")==null?"":request.getParameter("trader_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String closure_dt=request.getParameter("closure_dt")==null?"":request.getParameter("closure_dt");
			String closure_qty=request.getParameter("closure_qty")==null?"":request.getParameter("closure_qty");
			String closure_note=request.getParameter("closure_note")==null?"":request.getParameter("closure_note");
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, trader_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(opration.equals("REJECT"))
			{
				if(!trader_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
				{
					String fcc_flg="";
					String fcc_by="";
					String fcc_dt="";
					String cont_status="";
					
					//finding the state of the previous revision
					String query1="SELECT FCC_FLAG,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY HH24:MI:SS'),CONT_STATUS "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(CONT_REV)-1 "
							+ "FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt = dbcon.prepareStatement(query1);
					stmt.setString(1,comp_cd);
					stmt.setString(2, trader_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev_no);
					stmt.setString(5, cont_no);
					stmt.setString(6, contract_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						fcc_flg=rset.getString(1)==null?"":rset.getString(1);
						fcc_by=rset.getString(2)==null?"":rset.getString(2);
						fcc_dt=rset.getString(3)==null?"":rset.getString(3);
						cont_status=rset.getString(4)==null?"":rset.getString(4);
					}
					rset.close();
					stmt.close();
					
					String query = "UPDATE FMS_TRADER_CONT_MST A "
							+ "SET A.FCC_FLAG=?,A.FCC_BY=?,A.FCC_DATE=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),A.CONT_STATUS=?,A.CLOSURE_REQUEST_FLAG=? "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
							+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query);
					stmt1.setString(1,fcc_flg);
					stmt1.setString(2,fcc_by);
					stmt1.setString(3,fcc_dt);
					stmt1.setString(4,cont_status);
					stmt1.setString(5,"N");
					stmt1.setString(6,comp_cd);
					stmt1.setString(7,trader_cd);
					stmt1.setString(8,agmt_no);
					stmt1.setString(9,agmt_rev_no);
					stmt1.setString(10,cont_no);
					stmt1.setString(11,contract_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					InsertPurchaseLog(trader_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,emp_cd);
					
					if(closure_request_flag.equals("R"))
					{
						msg = "Successful! "+cont_name_map+" Termination Rejected Successfully!";
					}
					else if(closure_request_flag.equals("Y"))
					{
						msg = "Successful! "+cont_name_map+" Closure Rejected Successfully!";
					}
					msg_type = "S";
				}
				else
				{
					if(closure_request_flag.equals("R"))
					{
						msg = "Failed! -  "+cont_name_map+" Terminate Rejection submission Failed!";
					}
					else if(closure_request_flag.equals("Y"))
					{
						msg = "Failed! -  "+cont_name_map+" closure Rejection submission Failed!";
					}
					msg_type = "E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(!trader_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
				{
					String cont_status="";
					if(closure_request_flag.equals("R"))
					{
						cont_status="T";
					}
					else if(closure_request_flag.equals("Y"))
					{
						cont_status="C";
					}
					
					String query = "UPDATE FMS_TRADER_CONT_MST A "
							+ "SET A.CLOSURE_REQUEST_FLAG=?,A.CLOSURE_ALLOC_QTY=?,A.CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "A.CONT_STATUS=?,A.CLOSURE_REMARK=?,A.FCC_FLAG=?,A.FCC_BY=?,A.FCC_DATE=SYSDATE "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
							+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query);
					stmt1.setString(1,"A");
					stmt1.setString(2,closure_qty);
					stmt1.setString(3,closure_dt);
					stmt1.setString(4,cont_status);
					stmt1.setString(5,closure_note);
					stmt1.setString(6,"Y");
					stmt1.setString(7,emp_cd);
					stmt1.setString(8,comp_cd);
					stmt1.setString(9,trader_cd);
					stmt1.setString(10,agmt_no);
					stmt1.setString(11,agmt_rev_no);
					stmt1.setString(12,cont_no);
					stmt1.setString(13,contract_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					InsertPurchaseLog(trader_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,emp_cd);
					
					if(closure_request_flag.equals("R"))
					{
						msg = "Successful! "+cont_name_map+" Terminated Successfully!";
					}
					else if(closure_request_flag.equals("Y"))
					{
						msg = "Successful! "+cont_name_map+" Closed Successfully!";
					}
					msg_type = "S";
				}
				else
				{
					if(closure_request_flag.equals("R"))
					{
						msg = "Failed! -  "+cont_name_map+" Termination submission Failed!";
					}
					else if(closure_request_flag.equals("Y"))
					{
						msg = "Failed! -  "+cont_name_map+" closure submission Failed!";
					}
					msg_type = "E";
				}
			}
			url = "../purchase/frm_pur_closure_request.jsp?msg=" + msg + "&msg_type=" + msg_type+ commonUrl_pra;
			 
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Contract Closure Approve/Reject Failed";
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
		String function_nm="InsertUpdateContLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance"); 
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
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
			
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
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
					query="UPDATE FMS_TRADER_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE,"
							+ "LQ_DAMG_FROM=TO_DATE(?,'DD/MM/YYYY'),LQ_DAMG_TO=TO_DATE(?,'DD/MM/YYYY'),TAKE_PAY_FROM=TO_DATE(?,'DD/MM/YYYY'),TAKE_PAY_TO=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?";
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
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+cont_name_map+" Liability Detail Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_TRADER_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT,LQ_DAMG_FROM,LQ_DAMG_TO,TAKE_PAY_FROM,TAKE_PAY_TO) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'))";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
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
					
					msg = "Successful! -  "+cont_name_map+" Liability Detail submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! -  "+cont_name_map+" Liability Detail submission Failed!";
				msg_type = "E";
			}
			
			url = "../purchase/frm_trader_cont_liability_clause.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&tcq="+cont_tcq+"&dcq="+cont_dcq+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+"&rate="+cont_price+"&rate_unit="+cont_price_unit+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Contract Liability Insert/Update Failed";
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
	
	private void InsertUpdateTraderContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTraderContractDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
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
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String post_margin = request.getParameter("post_margin")==null?"":request.getParameter("post_margin");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String rate = request.getParameter("rate")==null?"":request.getParameter("rate");
			String rate_unit = request.getParameter("rate_unit")==null?"":request.getParameter("rate_unit");
			String transportation_charges = request.getParameter("transportation_charges")==null?"":request.getParameter("transportation_charges");
			String tcq = request.getParameter("tcq")==null?"":request.getParameter("tcq");
			String quantity_unit = request.getParameter("quantity_unit")==null?"":request.getParameter("quantity_unit");
			String dcq = request.getParameter("dcq")==null?"":request.getParameter("dcq");
			String mdcq = request.getParameter("mdcq")==null?"":request.getParameter("mdcq");
			String split_type_flag = request.getParameter("split_type_flag")==null?"":request.getParameter("split_type_flag");
			String split_enabled = request.getParameter("split_enabled")==null?"N":request.getParameter("split_enabled");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_status_flg = request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg"); 
			String txn_charges = request.getParameter("txn_charges")==null?"":request.getParameter("txn_charges");
			String txn_unit = request.getParameter("txn_unit")==null?"":request.getParameter("txn_unit");
			
			String DealEnterDtTime = ent_dt+" "+ent_time;
			String operation = "";
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			String tmp_chk_plant = request.getParameter("tmp_chk_plant")==null?"":request.getParameter("tmp_chk_plant");
			String[] split_value = request.getParameterValues("split_value");
			
			String[] oth_trd_cd = request.getParameterValues("oth_trd_cd");
			String[] oth_split_value = request.getParameterValues("oth_split_value");
			String[] oth_plant_seq_no = request.getParameterValues("oth_plant_seq_no");
			
			String[] charge_abbr = request.getParameterValues("charge_abbr");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
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
			
			//String temp_deal_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);

			String[] formula_id = request.getParameterValues("formula_id");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
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
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String buy_clause_no = request.getParameter("buy_clause_no")==null?"":request.getParameter("buy_clause_no");
			String terminator_flg = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"":request.getParameter("day_def_clause_no");
			String measure_clause_no = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String spec_clause_no = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			//PB20250423: for Cancellation,Closure and Termination of the contract 
			String change_request = request.getParameter("change_request")==null?"":request.getParameter("change_request");
			String cancel_dt = request.getParameter("cancel_dt")==null?"":request.getParameter("cancel_dt");
			String cancel_note = request.getParameter("cancel_note")==null?"":request.getParameter("cancel_note");
			String closure_req_flag = request.getParameter("closure_req_flag")==null?"":request.getParameter("closure_req_flag");
			String new_start_dt = request.getParameter("new_start_dt")==null?"":request.getParameter("new_start_dt");
			String new_end_dt = request.getParameter("new_end_dt")==null?"":request.getParameter("new_end_dt");
			
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
				if(!ent_dt.equals(""))
				{
					String year = "8"+ent_dt.substring(8,ent_dt.length());
					if(contract_type.equals("I"))
					{
						year = "9"+ent_dt.substring(8,ent_dt.length());
					}
					else if(contract_type.equals("T"))
					{
						year = "6"+ent_dt.substring(8,ent_dt.length());
					}
					int cont = Integer.parseInt(year) * 1000;
					query="SELECT MAX(CONT_NO) FROM FMS_TRADER_CONT_MST "
							+ "WHERE CONT_NO LIKE ? AND COMPANY_CD=? "
							+ "AND CONTRACT_TYPE=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, year+"%");
					stmt.setString(2, comp_cd);
					stmt.setString(3, contract_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						if(rset.getInt(1) == 0)
						{
							cont_no = ""+(cont+1);
						}
						else
						{
							cont_no = ""+(rset.getInt(1)+1);
						}
					}
					else
					{
						cont_no = ""+(cont+1);
					}
					rset.close();
					stmt.close();
					
					cont_rev_no="0";
					agmt_no="0";
					agmt_rev_no="0";
				}
			}
			else if(opration.equals("MODIFY"))
			{
				if(cont_status_flg.equals("Y"))
				{
					/*query="SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST "
							+ "WHERE CONT_NO = '"+cont_no+"' AND COMPANY_CD='"+comp_cd+"'";
					rset=stmt.executeQuery(query);
					if(rset.next())
					{
						cont_rev_no = ""+(rset.getInt(1)+1);
						
					}
					else
					{
						cont_rev_no = ""+(Integer.parseInt(cont_rev_no) +1);
					}*/
					
					//cont_status_flg="P";
				}
			}
			
			//PB20250423: for increasing the revision of the contract if closure/terminate request is generated...
			if(change_request.equals("CLOSURE_OR_TERMINATE") || change_request.equals("CONTRACT_DATE"))
			{
				cont_rev_no = ""+(Integer.parseInt(cont_rev_no)+1);	//increasing the contract revision 
				cont_status_flg="P";
			}
			
			// This is Contract Name - Don't Change it.
			String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+contract_type+cont_no+"-REV"+cont_rev_no;
			// Display Deal Number
			String temp_deal_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			/*
			 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
				CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
				CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
			*/
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_TRADER_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
			
				if(rev_count > 0)
				{
					int ctn=0;
					query1 ="UPDATE FMS_TRADER_CONT_MST SET CONT_REF_NO=?,TRADE_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "SIGNING_TIME=?,START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "AGMT_TYPE=?,TCQ=?,DCQ=?,QUANTITY_UNIT=?,RATE=?,RATE_UNIT=?, "
							+ "POST_MARGIN=?,TRANSPORTATION_CHARGE=?,SPLIT_FLAG=?,SPLIT_TYPE=?,"
							+ "BUYER_NOM_FLAG=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM_FLAG=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF_FLAG=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ_FLAG=?,MDCQ_PERCENTAGE=?,"
							+ "REMARK=?,CONT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
							+ "CONT_STATUS=?,TXN_CHARGE=?,BUYER_FORNGT_NOM=?,SELLER_FORNGT_NOM=?,"
							+ "DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,TXN_UNIT=?, "
							+ "MEASUREMENT =?,MEAS_STANDARD=?, MEAS_TEMPERATURE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "OFF_SPEC_GAS=?,SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,LIABILITY=?,LIABILITY_CLAUSE=?, "
							+ "BILLING_FLAG=?,BILLING_CLAUSE=?,TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?, "
							+ "DAY_DEF_CLAUSE=?,MEASUREMENT_CLAUSE=?,OFF_SPEC_GAS_CLAUSE=? ";
					if(change_request.equals("CANCEL"))
					{
						query1+=",CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),CLOSURE_REMARK=? ";
					}
					else if(change_request.equals("CONTRACT_REOPEN"))
					{
						query1+=",CLOSURE_REQUEST_FLAG=? ";
					}
					query1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++ctn, cont_ref_no);
					stmt1.setString(++ctn, trade_ref_no);
					stmt1.setString(++ctn, signing_dt);
					stmt1.setString(++ctn, signing_time);
					stmt1.setString(++ctn, start_dt);
					stmt1.setString(++ctn, end_dt);
					stmt1.setString(++ctn, agreement_base);
					stmt1.setString(++ctn, agreement_type);
					stmt1.setString(++ctn, tcq);
					stmt1.setString(++ctn, dcq);
					stmt1.setString(++ctn, quantity_unit);
					stmt1.setString(++ctn, rate);
					stmt1.setString(++ctn, rate_unit);
					stmt1.setString(++ctn, post_margin);
					stmt1.setString(++ctn, transportation_charges);
					stmt1.setString(++ctn, split_enabled);
					stmt1.setString(++ctn, split_type_flag);
					stmt1.setString(++ctn, buyer_nom);
					stmt1.setString(++ctn, buy_m);
					stmt1.setString(++ctn, buy_w);
					stmt1.setString(++ctn, buy_d);
					stmt1.setString(++ctn, seller_nom);
					stmt1.setString(++ctn, sel_m);
					stmt1.setString(++ctn, sel_w);
					stmt1.setString(++ctn, sel_d);
					stmt1.setString(++ctn, day_def);
					stmt1.setString(++ctn, day_time_from);
					stmt1.setString(++ctn, day_time_to);
					stmt1.setString(++ctn, "");
					stmt1.setString(++ctn, mdcq);
					stmt1.setString(++ctn, remark);
					stmt1.setString(++ctn, cont_name);
					stmt1.setString(++ctn, emp_cd);
					stmt1.setString(++ctn, cont_status_flg);
					stmt1.setString(++ctn, txn_charges);
					stmt1.setString(++ctn, buy_f);
					stmt1.setString(++ctn, sel_f);
					stmt1.setString(++ctn, dda_dt);
					stmt1.setString(++ctn, dda_time);
					stmt1.setString(++ctn, txn_unit);
					stmt1.setString(++ctn, measurement_flg);
					stmt1.setString(++ctn, meas_standard);
					stmt1.setString(++ctn, meas_temperature);
					stmt1.setString(++ctn, pressure_min_bar);
					stmt1.setString(++ctn, pressure_max_bar);
					stmt1.setString(++ctn, off_spec_gas_flg);
					stmt1.setString(++ctn, spec_gas_energy_base);
					stmt1.setString(++ctn, spec_gas_min_energy);
					stmt1.setString(++ctn, spec_gas_max_energy);
					stmt1.setString(++ctn, liability_flg);
					stmt1.setString(++ctn, liability_clause);
					stmt1.setString(++ctn, billing_flag);
					stmt1.setString(++ctn, buy_clause_no);
					stmt1.setString(++ctn, terminator_flg);
					stmt1.setString(++ctn, terminate_clause);
					stmt1.setString(++ctn, terminate_planed);
					stmt1.setString(++ctn, terminate_force);
					stmt1.setString(++ctn, day_def_clause_no);
					stmt1.setString(++ctn, measure_clause_no);
					stmt1.setString(++ctn, spec_clause_no);
					if(change_request.equals("CANCEL"))
					{
						stmt1.setString(++ctn, cancel_dt);
						stmt1.setString(++ctn, cancel_note);
					}
					else if(change_request.equals("CONTRACT_REOPEN"))
					{
						stmt1.setString(++ctn,closure_req_flag);
					}
					stmt1.setString(++ctn, comp_cd);
					stmt1.setString(++ctn, counterparty_cd);
					stmt1.setString(++ctn, cont_no);
					stmt1.setString(++ctn, cont_rev_no);
					stmt1.setString(++ctn, agmt_no);
					stmt1.setString(++ctn, agmt_rev_no);
					stmt1.setString(++ctn, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				else
				{
					int ctn=0;
					query1 = "INSERT INTO FMS_TRADER_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,"
							+ "START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
							+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
							+ "REMARK,ENT_DT,ENT_BY,CONT_NAME,CONTRACT_TYPE,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
							+ "DDA_DT,DDA_TIME,TXN_UNIT,"
							+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
							+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DAY_DEF_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE ";
					if(change_request.equals("CLOSURE_OR_TERMINATE"))
					{
						query1+=",CLOSURE_REQUEST_FLAG ";
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						query1+=",CHANGE_DATE_REQ,CHANGE_START_DT,CHANGE_END_DT ";
					}
					query1+= ") "
							+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY HH24:MI'),?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,? ";
					if(change_request.equals("CLOSURE_OR_TERMINATE"))
					{
						query1+=",?";
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						query1+=",?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY')";
					}
					query1+= ")";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++ctn, comp_cd);
					stmt1.setString(++ctn, counterparty_cd);
					stmt1.setString(++ctn, agmt_no);
					stmt1.setString(++ctn, agmt_rev_no);
					stmt1.setString(++ctn, cont_no);
					stmt1.setString(++ctn, cont_rev_no);
					stmt1.setString(++ctn, cont_ref_no);
					stmt1.setString(++ctn, trade_ref_no);
					stmt1.setString(++ctn, signing_dt);
					stmt1.setString(++ctn, signing_time);
					stmt1.setString(++ctn, start_dt);
					stmt1.setString(++ctn, end_dt);
					stmt1.setString(++ctn, agreement_base);
					stmt1.setString(++ctn, agreement_type);
					stmt1.setString(++ctn, tcq);
					stmt1.setString(++ctn, dcq);
					stmt1.setString(++ctn, quantity_unit);
					stmt1.setString(++ctn, rate);
					stmt1.setString(++ctn, rate_unit);
					stmt1.setString(++ctn, post_margin);
					stmt1.setString(++ctn, transportation_charges);
					stmt1.setString(++ctn, split_enabled);
					stmt1.setString(++ctn, split_type_flag);
					stmt1.setString(++ctn, buyer_nom);
					stmt1.setString(++ctn, buy_m);
					stmt1.setString(++ctn, buy_w);
					stmt1.setString(++ctn, buy_d);
					stmt1.setString(++ctn, seller_nom);
					stmt1.setString(++ctn, sel_m);
					stmt1.setString(++ctn, sel_w);
					stmt1.setString(++ctn, sel_d);
					stmt1.setString(++ctn, day_def);
					stmt1.setString(++ctn, day_time_from);
					stmt1.setString(++ctn, day_time_to);
					stmt1.setString(++ctn, "");
					stmt1.setString(++ctn, mdcq);
					stmt1.setString(++ctn, remark);
					stmt1.setString(++ctn, DealEnterDtTime);
					stmt1.setString(++ctn, emp_cd);
					stmt1.setString(++ctn, cont_name);
					stmt1.setString(++ctn, contract_type);
					stmt1.setString(++ctn, cont_status_flg);
					stmt1.setString(++ctn, txn_charges);
					stmt1.setString(++ctn, buy_f);
					stmt1.setString(++ctn, sel_f);
					stmt1.setString(++ctn, dda_dt);
					stmt1.setString(++ctn, dda_time);
					stmt1.setString(++ctn, txn_unit);
					stmt1.setString(++ctn, measurement_flg);
					stmt1.setString(++ctn, meas_standard);
					stmt1.setString(++ctn, meas_temperature);
					stmt1.setString(++ctn, pressure_min_bar);
					stmt1.setString(++ctn, pressure_max_bar);
					stmt1.setString(++ctn, off_spec_gas_flg);
					stmt1.setString(++ctn, spec_gas_energy_base);
					stmt1.setString(++ctn, spec_gas_min_energy);
					stmt1.setString(++ctn, spec_gas_max_energy);
					stmt1.setString(++ctn, liability_flg);
					stmt1.setString(++ctn, liability_clause);
					stmt1.setString(++ctn, billing_flag);
					stmt1.setString(++ctn, buy_clause_no);
					stmt1.setString(++ctn, terminator_flg);
					stmt1.setString(++ctn, terminate_clause);
					stmt1.setString(++ctn, terminate_planed);
					stmt1.setString(++ctn, terminate_force);
					stmt1.setString(++ctn, day_def_clause_no);
					stmt1.setString(++ctn, measure_clause_no);
					stmt1.setString(++ctn, spec_clause_no);
					if(change_request.equals("CLOSURE_OR_TERMINATE"))
					{
						stmt1.setString(++ctn, closure_req_flag);
					}
					if(change_request.equals("CONTRACT_DATE"))
					{
						stmt1.setString(++ctn, "Y");
						stmt1.setString(++ctn, new_start_dt);
						stmt1.setString(++ctn, new_end_dt);
					}
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				//ADD LOG IN LOG TABLE
				int log_seq_no=1;
				queryString = "SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_TRADER_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=?";
				stmt0=dbcon.prepareStatement(queryString);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, cont_no);
				stmt0.setString(4, agmt_no);
				stmt0.setString(5, contract_type);
				rset0 = stmt0.executeQuery();
				if(rset0.next())
				{
					log_seq_no = (rset0.getInt(1)+1);
				}
				rset0.close();
				stmt0.close();
				
				InsertPurchaseLog(counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,emp_cd);
				
				if(cont_rev_no.equals("0")) //TO MAINTAIN PRICE HISTROY
				{
					String seq_no="0";
					int price_count=0;
					query1="SELECT COUNT(*) "
							+ "FROM FMS_TRADER_CONT_PRICE_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND SEQ_NO=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, seq_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						price_count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(price_count >0)
					{
						query2="UPDATE FMS_TRADER_CONT_PRICE_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY'),OLD_PRICE=?,NEW_PRICE=?, "
								+ "MODIFIY_BY=?,MODIFIY_DT=SYSDATE  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=? AND FLAG=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, start_dt);
						stmt2.setString(2, "");
						stmt2.setString(3, rate);
						stmt2.setString(4, emp_cd);
						stmt2.setString(5, comp_cd);
						stmt2.setString(6, counterparty_cd);
						stmt2.setString(7, agmt_no);
						stmt2.setString(8, cont_no);
						stmt2.setString(9, contract_type);
						stmt2.setString(10, seq_no);
						stmt2.setString(11, "R");
						stmt2.executeQuery();
						
						stmt2.close();
					}
					else
					{	
						query2="INSERT INTO FMS_TRADER_CONT_PRICE_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "EFF_DT,SEQ_NO,OLD_PRICE,NEW_PRICE,ENT_BY,ENT_DT,FLAG,PRICE_UNIT) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE,?,?)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, start_dt);
						stmt2.setString(9, seq_no);
						stmt2.setString(10, "");
						stmt2.setString(11, rate);
						stmt2.setString(12, emp_cd);
						stmt2.setString(13, "R");
						stmt2.setString(14, rate_unit);
						stmt2.executeQuery();
						
						stmt2.close();
					}
				}
				
				//TRANSPORTER PLANT
				query1 = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CONT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				rset1 = stmt1.executeQuery();
				int count = 0;
				if(rset1.next())
				{
					 count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				if(count>0)
				{
					query2 = "DELETE FROM FMS_TRADER_CONT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(trans_cd!=null)
				{
					for(int i=0;i<trans_cd.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, trans_cd[i]);
						stmt2.setString(9, trans_plant_seq_no[i]);
						stmt2.setString(10, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				//TRADER PLANT
				query1 = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
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
					query2 = "DELETE FROM FMS_TRADER_CONT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
								+ "CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT,SPLIT_VALUE) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,SYSDATE,?) ";						
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, split_value[i]);
						stmt2.executeUpdate();
						stmt2.close();
						
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
											+ "FROM FMS_TRADER_CONT_PLANT_CHRG "
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
										query="UPDATE FMS_TRADER_CONT_PLANT_CHRG SET CHARGE_RATE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
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
										query2 = "INSERT INTO FMS_TRADER_CONT_PLANT_CHRG(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
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
						String temp_chk_plant[]=tmp_chk_plant.split("@");
						for(int m=0; m<temp_chk_plant.length; m++)
						{
							if(!temp_chk_plant[m].equals(chk_plant[i]) && !chk_plant[i].equals(""))
							{
								updateTraderContractBillingPlant(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_plant[m], chk_plant[i], chk_plant.length);
							}
						}
					}
				}
				
				//OTHER TRADER PLANT
				//NOTE : COUNTERPARTY CD IS NOT REQUIRED IN WHERE CLAUSE DUE TO CONT NO WILL NOT BE SAME FOR COUNTERPARTY WISE
				query1 = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
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
					query2 = "DELETE FROM FMS_TRADER_CONT_SPLIT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(oth_trd_cd!=null)
				{
					for(int i=0;i<oth_trd_cd.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_SPLIT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
								+ "CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT,SPLIT_VALUE,SPLIT_TRADER_CD) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, oth_plant_seq_no[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, oth_split_value[i]);
						stmt2.setString(11, oth_trd_cd[i]);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						if(charge_abbr!=null)
						{
							for(int j=0; j<charge_abbr.length; j++)
							{
								String chrgAbbr=charge_abbr[j];
								String effDt = request.getParameterValues("oth_eff_dt_"+chrgAbbr)[i];
								String chrgRate = request.getParameterValues("oth_"+chrgAbbr)[i];
								
								if(!chrgRate.equals("") && !effDt.equals(""))
								{
									query="SELECT COUNT(*) "
											+ "FROM FMS_TRADER_CONT_PLANT_CHRG "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
											+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
											+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND CHARGE_ABBR=? ";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, comp_cd);
									stmt.setString(2, oth_trd_cd[i]);
									stmt.setString(3, cont_no);
									stmt.setString(4, agmt_no);
									stmt.setString(5, agmt_rev_no);
									stmt.setString(6, contract_type);
									stmt.setString(7, oth_plant_seq_no[i]);
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
										query="UPDATE FMS_TRADER_CONT_PLANT_CHRG SET CHARGE_RATE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
												+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
												+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
												+ "AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND CHARGE_ABBR=? ";
										stmt=dbcon.prepareStatement(query);
										stmt.setString(1, chrgRate);
										stmt.setString(2, emp_cd);
										stmt.setString(3, comp_cd);
										stmt.setString(4, oth_trd_cd[i]);
										stmt.setString(5, cont_no);
										stmt.setString(6, agmt_no);
										stmt.setString(7, agmt_rev_no);
										stmt.setString(8, contract_type);
										stmt.setString(9, oth_plant_seq_no[i]);
										stmt.setString(10, effDt);
										stmt.setString(11, chrgAbbr);
										stmt.executeUpdate();
										stmt.close();
									}
									else
									{
										query2 = "INSERT INTO FMS_TRADER_CONT_PLANT_CHRG(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
												+ "CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, EFF_DT, CHARGE_ABBR, CHARGE_RATE,ENT_BY, ENT_DT) "
												+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE) ";						
										stmt2=dbcon.prepareStatement(query2);
										stmt2.setString(1, comp_cd);
										stmt2.setString(2, oth_trd_cd[i]);
										stmt2.setString(3, agmt_no);
										stmt2.setString(4, agmt_rev_no);
										stmt2.setString(5, cont_no);
										stmt2.setString(6, cont_rev_no);
										stmt2.setString(7, contract_type);
										stmt2.setString(8, oth_plant_seq_no[i]);
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
				}
				
				//BUSINESS UNIT
				query1= "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
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
					query2 = "DELETE FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?, ?, ?,?,?,?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_bu_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//FOR LINK TRANSPORTER
				if(formula_id != null)
				{
					if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
					{
						count=0;
						query = "SELECT COUNT(*) "
								+ "FROM FMS_TRADER_CONT_LINK_TRANS "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? ";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, cont_rev_no);
						stmt1.setString(5, agmt_no);
						stmt1.setString(6, agmt_rev_no);
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
							query2 = "DELETE FROM FMS_TRADER_CONT_LINK_TRANS "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
									+ "AND AGMT_NO=? AND AGMT_REV=?";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, cont_no);
							stmt2.setString(4, cont_rev_no);
							stmt2.setString(5, agmt_no);
							stmt2.setString(6, agmt_rev_no);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
						for(int i=0; i<formula_id.length; i++)
						{
							query2 = "INSERT INTO FMS_TRADER_CONT_LINK_TRANS(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, FORMULA_ID, ENT_BY, ENT_DT) "
									+ "VALUES(?,?, ?, ?,?,?,?,?,SYSDATE) ";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_no);
							stmt2.setString(4, agmt_rev_no);
							stmt2.setString(5, cont_no);
							stmt2.setString(6, cont_rev_no);
							stmt2.setString(7, formula_id[i]);
							stmt2.setString(8, emp_cd);
							stmt2.executeUpdate();
							
							stmt2.close();
						}
					}
				}
				
				//GX BUSINESS UNIT
				if(!gx_counterparty_cd.equals(""))
				{
					query1 = "SELECT COUNT(*) "
							+ "FROM FMS_TRADER_CONT_GX_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					stmt1.setString(7, contract_type);
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
						query2 = "DELETE FROM FMS_TRADER_CONT_GX_BU "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, cont_rev_no);
						stmt2.setString(5, agmt_no);
						stmt2.setString(6, agmt_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.executeQuery();
						
						stmt2.close();
					}
					if(chk_gx_bu_plant!=null)
					{
						for(int i=0;i<chk_gx_bu_plant.length;i++)
						{
							query2 = "INSERT INTO FMS_TRADER_CONT_GX_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, GX_BU_SEQ_NO, ENT_BY, ENT_DT,"
									+ "GX_COUNTERPARTY_CD) "
									+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,"
									+ "?) ";
							stmt2=dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_no);
							stmt2.setString(4, agmt_rev_no);
							stmt2.setString(5, cont_no);
							stmt2.setString(6, cont_rev_no);
							stmt2.setString(7, contract_type);
							stmt2.setString(8, chk_gx_bu_plant[i]);
							stmt2.setString(9, emp_cd);
							stmt2.setString(10, gx_counterparty_cd);
							stmt2.executeQuery();
							
							stmt2.close();
						 }
					}
				}
				
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
					msg = "Successful! - New Trader Contract "+temp_deal_no+" for "+counterparty_abbr+" Added Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Trader Contract "+temp_deal_no+" for "+counterparty_abbr+" Modified Successfully!";
					if(change_request.equals("CANCEL"))
					{
						msg = "Successful! - Trader Contract "+temp_deal_no+" for "+counterparty_abbr+" is Canceled Successfully!";
					}
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Contract "+temp_deal_no+" for "+counterparty_abbr+" Modification Failed!";
				if(change_request.equals("CANCEL"))
				{
					msg = "Failed! - Contract "+temp_deal_no+" for "+counterparty_abbr+" Cancellation Failed!";
				}
				msg_type="E";
			}
			
			//<!--Harsh Maheta 20230904 : Added for new values to show in Deal audit history-->//

			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);

			String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			new_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#RATE="+rate+"#RATEUNIT="+rate_unit+"#TCQ="+tcq+"#DCQ="+dcq+"#QUNIT="+quantity_unit+"#MDCQ="+mdcq+"#GXFEE="+txn_charges+"#GXFEEUNIT="+txn_unit+"#POSTMARG="+post_margin+"#CONT_STATUS="+cont_status;
			
			url = "../purchase/frm_trader_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&clearance="+clearance+"&contract_type="+contract_type+commonUrl_pra;
			
			PurchaseMailBody(comp_cd,cp_name, cp_abbr, operation,msg,emp_cd,new_value,old_value);
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract Modification Failed!";
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
	
	private void FCCforTraderContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="FCCforTraderContractDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String fcc_flg=request.getParameter("fcc_flg")==null?"N":request.getParameter("fcc_flg");
			String cont_status_flg = fcc_flg;
			
			//String temp_deal_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
			String temp_deal_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			query ="UPDATE FMS_TRADER_CONT_MST SET FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE, CONT_STATUS=? "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt=dbcon.prepareStatement(query);
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
			
			//ADD LOG IN LOG TABLE
			int log_seq_no=1;
			queryString = "SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt0=dbcon.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, counterparty_cd);
			stmt0.setString(3, cont_no);
			stmt0.setString(4, agmt_no);
			stmt0.setString(5, contract_type);
			rset0 = stmt0.executeQuery();
			if(rset0.next())
			{
				log_seq_no = (rset0.getInt(1)+1);
			}
			rset0.close();
			stmt0.close();
			
			InsertPurchaseLog(counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,emp_cd);
			
			if(cont_rev_no.equals("0") && fcc_flg.equals("Y"))
			{
				query1="UPDATE FMS_TRADER_CONT_PRICE_DTL SET FLAG=?,APPROVE_BY=?,APPROVE_DT=SYSDATE,"
						+ "MODIFIY_BY=?,MODIFIY_DT=SYSDATE  "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND SEQ_NO=? AND FLAG=?";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, "Y");
				stmt1.setString(2, emp_cd);
				stmt1.setString(3, emp_cd);
				stmt1.setString(4, comp_cd);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, agmt_no);
				stmt1.setString(7, cont_no);
				stmt1.setString(8, contract_type);
				stmt1.setString(9, "0");
				stmt1.setString(10, "R");
				stmt1.executeQuery();
				
				stmt1.close();
			}
			
			if(fcc_flg.equals("Y"))
			{
				msg = "Successful! - FCC Approved for "+counterparty_abbr+" Contract "+temp_deal_no+" !";
			}
			else
			{
				msg = "Successful! - FCC Disapproved for "+counterparty_abbr+" Contract "+temp_deal_no+" !";
			}
			msg_type="S";
			
			url = "../purchase/frm_trader_contract_mst_fcc.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&clearance="+clearance+"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract FCC Update Failed!";
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
							+ "FROM FMS_TRADER_CONT_DCQ_DTL "
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
					
					query1="INSERT INTO FMS_TRADER_CONT_DCQ_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
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
					query1="UPDATE FMS_TRADER_CONT_DCQ_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'), TO_DT=TO_DATE(?,'DD/MM/YYYY'), "
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
			
			url = "../purchase/frm_pur_contract_dcq_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
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
	
	private void TraderContractLinkTrans(HttpServletRequest request) throws SQLException 
	{
		String function_nm="TraderContractLinkTrans()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");

			String[] formula_id = request.getParameterValues("formula_id");
			
			if(formula_id != null)
			{
				if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
				{
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_TRADER_CONT_LINK_TRANS "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					stmt.setString(4, cont_rev_no);
					stmt.setString(5, agmt_no);
					stmt.setString(6, agmt_rev_no);
					rset = stmt.executeQuery();
					count = 0;
					if(rset.next())
					{
						 count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count>0)
					{
						query1 = "DELETE FROM FMS_TRADER_CONT_LINK_TRANS "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, cont_rev_no);
						stmt1.setString(5, agmt_no);
						stmt1.setString(6, agmt_rev_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					for(int i=0; i<formula_id.length; i++)
					{
						query1 = "INSERT INTO FMS_TRADER_CONT_LINK_TRANS(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, FORMULA_ID, ENT_BY, ENT_DT) "
								+ "VALUES(?,?, ?, ?,?,?,?,?,SYSDATE) ";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, agmt_rev_no);
						stmt1.setString(5, cont_no);
						stmt1.setString(6, cont_rev_no);
						stmt1.setString(7, formula_id[i]);
						stmt1.setString(8, emp_cd);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - Link Transporter Added Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - Add Link Transporter Failed!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - Add Link Transporter Failed!";
				msg_type="E";
			}
			
			url = "../purchase/frm_trader_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Link Transporter Failed!";
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
	
	private void InsertUpdateTraderBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTraderBillingDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);

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
			String holidayState_map = request.getParameter("holidayState_map")==null?"":request.getParameter("holidayState_map");
			String split_flag = request.getParameter("split_flag")==null?"":request.getParameter("split_flag");
			
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
			
			//String temp_deal_no = utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
			String temp_deal_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			String[] cust_plant_map = holidayState_map.split("@@");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				int queryCnt=0;
				String queryString="SELECT PLANT_SEQ_NO,COUNTERPARTY_CD "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? ";
				
				if(split_flag.equals("Y"))
				{
					queryString+="UNION ALL ";
					queryString+="SELECT PLANT_SEQ_NO,SPLIT_TRADER_CD AS COUNTERPARTY_CD "
							+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? ";
				}
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(++queryCnt, comp_cd);
				stmt3.setString(++queryCnt, counterparty_cd);
				stmt3.setString(++queryCnt, cont_no);
				stmt3.setString(++queryCnt, agmt_no);
				stmt3.setString(++queryCnt, contract_type);
				if(split_flag.equals("Y"))
				{
					stmt3.setString(++queryCnt, comp_cd);
					stmt3.setString(++queryCnt, counterparty_cd);
					stmt3.setString(++queryCnt, cont_no);
					stmt3.setString(++queryCnt, agmt_no);
					stmt3.setString(++queryCnt, contract_type);
				}
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String plant_seq = rset3.getString(1)==null?"":rset3.getString(1);
					String counterPty_cd = rset3.getString(2)==null?"":rset3.getString(2);
					String state_map = "";
					String contpty_cd = "";
					for(int i=0; i<cust_plant_map.length; i++)
					{
						if(cust_plant_map[i].startsWith(plant_seq) && cust_plant_map[i].split("//")[1].equals(counterPty_cd))
						{
							counterPty_cd = cust_plant_map[i].split("//")[1];
							state_map = cust_plant_map[i].split("//")[2];
						}
					}
					String SplitFlg="";
					if(!counterPty_cd.equals(counterparty_cd))
					{
						SplitFlg="Y";
					}
					else
					{
						SplitFlg="";
					}
					
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_TRADER_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterPty_cd);
					stmt.setString(3, cont_no);
					//stmt.setString(4, cont_rev_no);
					stmt.setString(4, agmt_no);
					//stmt.setString(5, agmt_rev_no);
					stmt.setString(5, contract_type);
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
						query1="UPDATE FMS_TRADER_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=?  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt1=dbcon.prepareStatement(query1);
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
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterPty_cd);
						stmt1.setString(++cnt, cont_no);
						//stmt1.setString(++cnt, cont_rev_no);
						stmt1.setString(++cnt, agmt_no);
						//stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query1="INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,SPLIT_FLAG) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterPty_cd);
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
						stmt1.setString(++cnt1, billing_days);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.setString(++cnt1, SplitFlg);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - Billing Detail Submitted for "+counterparty_abbr+" contract "+temp_deal_no+" !";
					msg_type = "S";
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - Billing Detail Submission Failed for "+counterparty_abbr+" contract "+temp_deal_no+" !";
				msg_type = "E";
			}
			
			url = "../purchase/frm_trader_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Billing Detail Submission Failed!";
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
	
	private void PurchaseMailBody(String comp_cd,String cp_name,String cp_abbr,String operation,String msg,String emp_cd,String new_values, String old_values) throws Exception
	{
		String function_nm="PurchaseMailBody()";
		String mailBody="";
		try
		{
			String contractDetail="";
			String cont_no = "",old_cont_no = "";
			String cont_ref_no = "",old_cont_ref_no = "";

			if(!new_values.equals("new_values"));
			{
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
								if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")) 
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
								if(contract_type.equals("X")||contract_type.equals("I")) 
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
						if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")) 
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
				
				String to_mail_list_purchase = utilBean.getToMailReceipentList(dbcon,comp_cd,"Deal Audit Notification", "Risk Mgmt", "NA", "On-Event");
				
				String cc_mail_list_purchase=utilBean.getCcMailReceipentList(dbcon,comp_cd,"Deal Audit Notification", "Risk Mgmt", "NA", "On-Event");
				
				if(!to_mail_list_purchase.equals("") && !mailBody.equals(""))
				{
					mailDelv.sendMail(comp_cd,to_mail_list_purchase, subject, mailBody, "", cc_mail_list_purchase, "");
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void InsertPurchaseLog(String counterparty_cd,String agmt_no,String agmt_rev_no,String cont_no,String cont_rev_no,String contract_type,String emp_cd) throws SQLException
	{
		String function_nm="InsertPurchaseLog()";
		try
		{
			//ADD LOG IN LOG TABLE
			int log_seq_no=1;
			queryString = "SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt0=dbcon.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, counterparty_cd);
			stmt0.setString(3, cont_no);
			stmt0.setString(4, agmt_no);
			stmt0.setString(5, contract_type);
			rset0 = stmt0.executeQuery();
			if(rset0.next())
			{
				log_seq_no = (rset0.getInt(1)+1);
			}
			rset0.close();
			stmt0.close();
			
			query="INSERT INTO LOG_TRADER_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT,"
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,"
					+ "BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,"
					+ "MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,"
					+ "SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
					+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE "
					+ ",CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,CHANGE_DATE_REQ,CHANGE_DATE_APPROVE,CHANGE_START_DT,CHANGE_END_DT) "	//PB20250418: for closure and cancellation
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,"
					+ "AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,?,?,SYSDATE,"
					+ "CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,"
					+ "BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,"
					+ "MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,"
					+ "SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
					+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,"
					+ "BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE "
					+ ",CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,CHANGE_DATE_REQ,CHANGE_DATE_APPROVE,CHANGE_START_DT,CHANGE_END_DT "	//PB20250418: for closure and cancellation
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setInt(1, log_seq_no);
			stmt.setString(2, emp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, cont_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, agmt_no);
			stmt.setString(8, agmt_rev_no);
			stmt.setString(9, contract_type);
			stmt.executeQuery();
			
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	private void updateTraderContractBillingPlant(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String plant_seq, String new_plant_seq, int new_plant_cnt) throws Exception
	{
		String function_nm="updateTraderContractBillingPlant()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? ";
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
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? ";
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
			
			String hol_state=utilBean.getState_TIN(dbcon, comp_cd, counterpty_cd, "T", new_plant_seq);
			if(hol_state.equals(""))
			{
				hol_state="24";
			}
			if(billing_count > 0 && new_billing_count == 0)
			{
				if(new_plant_cnt==1)
				{
					query ="UPDATE FMS_TRADER_BILLING_DTL A SET PLANT_SEQ_NO=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_NO=? "
							+ "AND CONTRACT_TYPE=? ";
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
					query="INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,SPLIT_FLAG,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,SPLIT_FLAG,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,? "
							+ "FROM FMS_TRADER_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_TRADER_BILLING_DTL B "
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
				query ="UPDATE FMS_TRADER_BILLING_DTL A SET HOLIDAY_STATE=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND HOLIDAY_STATE IS NULL";
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
	
	private void insertModifyPurCtrDtl(HttpServletRequest request) throws Exception
	{
		String function_nm="insertModifyPurCtrDtl()";
		try
		{
			String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
			String mole_cd=request.getParameter("mole_cd")==null?"":request.getParameter("mole_cd");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String product_cd=request.getParameter("prod_cd")==null?"":request.getParameter("prod_cd");
			
			String trader_cd_ctr=request.getParameter("trader_cd_ctr")==null?"":request.getParameter("trader_cd_ctr");
			String bu_unit_ctr=request.getParameter("bu_unit_ctr")==null?"":request.getParameter("bu_unit_ctr");
			String plant_seq_ctr=request.getParameter("plant_seq_ctr")==null?"":request.getParameter("plant_seq_ctr");
			String ctr_no=request.getParameter("ctr_no")==null?"":request.getParameter("ctr_no");
			String temp_ctr_contract_dtl=request.getParameter("temp_ctr_contract_dtl")==null?"":request.getParameter("temp_ctr_contract_dtl");
			
			String countpty_cd="";
			String agmt_no="";
			String cont_no="";
			String cont_type="";
			String cargo_no="";
			if(!temp_ctr_contract_dtl.equals(""))
			{
				String cont_dtl [] = temp_ctr_contract_dtl.split("@@");
				countpty_cd=cont_dtl[0];
				agmt_no=cont_dtl[1];
				cont_no=cont_dtl[2];
				cont_type=cont_dtl[3];
				cargo_no=cont_dtl[4];
			}
			
			String molecule_cd=mole_cd;
			/*
			String product_cd="";
			 * if(!mole_cd.equals("")) { String temp[]=mole_cd.split("-");
			 * molecule_cd=temp[0]; product_cd=temp[1]; }
			 */
			
			if(!trader_cd_ctr.equals("") && !countpty_cd.equals("") && !agmt_no.equals("") && !cont_no.equals("") && !cont_type.equals("") && !cargo_no.equals("")
				&& !bu_unit_ctr.equals("") && !plant_seq_ctr.equals("") && !ctr_no.equals("") && !molecule_cd.equals("") && !product_cd.equals("") && !eff_dt.equals(""))
			{
				if(opration.equals("INSERT"))
				{
					String query="INSERT INTO FMS_BUY_CTR_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO, "
							+ "CONTRACT_TYPE,CARGO_NO,CTR_REF,EFF_DT,PROD_CD,MOLE_CD,PLANT_SEQ_NO, "
							+ "BU_SEQ,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,?, "
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?, "
							+ "?,SYSDATE,?) ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, trader_cd_ctr);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, cont_type);
					stmt.setString(6, cargo_no);
					stmt.setString(7, ctr_no);
					stmt.setString(8, eff_dt);
					stmt.setString(9, product_cd);
					stmt.setString(10, molecule_cd);
					stmt.setString(11, plant_seq_ctr);
					stmt.setString(12, bu_unit_ctr);
					stmt.setString(13, emp_cd);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - CTR Added Successfully!!!";
					msg_type="S";
				}
				else if(opration.equals("MODIFY"))
				{
					//for modifying the CTR 
					String query1="UPDATE FMS_BUY_CTR_MST "
							+ "SET CTR_REF=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?  "
							+ "AND BU_SEQ=? AND PLANT_SEQ_NO=? ";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, ctr_no);
					stmt.setString(2, comp_cd);
					stmt.setString(3, trader_cd_ctr);
					stmt.setString(4, agmt_no);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_type);
					stmt.setString(7, cargo_no);
					stmt.setString(8, bu_unit_ctr);
					stmt.setString(9, plant_seq_ctr);
					stmt.executeUpdate();
					
					stmt.close();
					
					int count=0;
					String queryString="SELECT COUNT(*) "
							+ "FROM FMS_BUY_CTR_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND CTR_REF=? "
							+ "AND BU_SEQ=? AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, trader_cd_ctr);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, cont_type);
					stmt.setString(6, cargo_no);
					stmt.setString(7, ctr_no);
					stmt.setString(8, bu_unit_ctr);
					stmt.setString(9, plant_seq_ctr);
					stmt.setString(10, eff_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					//Modification
					if(count>0)
					{
						String query="UPDATE FMS_BUY_CTR_MST "
								+ "SET PROD_CD=?,MOLE_CD=?, "
								+ "MODIFY_BY=?, MODIFY_DT=SYSDATE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND CTR_REF=? "
								+ "AND BU_SEQ=? AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, product_cd);
						stmt.setString(2, molecule_cd);
						stmt.setString(3, emp_cd);
						stmt.setString(4, comp_cd);
						stmt.setString(5, trader_cd_ctr);
						stmt.setString(6, agmt_no);
						stmt.setString(7, cont_no);
						stmt.setString(8, cont_type);
						stmt.setString(9, cargo_no);
						stmt.setString(10, ctr_no);
						stmt.setString(11, bu_unit_ctr);
						stmt.setString(12, plant_seq_ctr);
						stmt.setString(13, eff_dt);
						stmt.executeUpdate();
						
						stmt.close();
					}
					else
					{
						String query="INSERT INTO FMS_BUY_CTR_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO, "
							+ "CONTRACT_TYPE,CARGO_NO,CTR_REF,EFF_DT,PROD_CD,MOLE_CD,PLANT_SEQ_NO, "
							+ "BU_SEQ,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,?, "
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?, "
							+ "?,SYSDATE,?) ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, trader_cd_ctr);
						stmt.setString(3, agmt_no);
						stmt.setString(4, cont_no);
						stmt.setString(5, cont_type);
						stmt.setString(6, cargo_no);
						stmt.setString(7, ctr_no);
						stmt.setString(8, eff_dt);
						stmt.setString(9, product_cd);
						stmt.setString(10, molecule_cd);
						stmt.setString(11, plant_seq_ctr);
						stmt.setString(12, bu_unit_ctr);
						stmt.setString(13, emp_cd);
						stmt.executeUpdate();
						
						stmt.close();
					}
					msg = "Successful! - CTR Modified Successfully!!!";
					msg_type="S";
				}
			}
			else
			{
				msg = "Failed! - Error in inserting CTR!!!";
				msg_type="E";
			}
			
			url="../purchase/fms_purchase_ctr_mst.jsp?trader_cd="+trader_cd_ctr+"&from_dt="+from_dt+"&to_dt="+to_dt+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - CTR Details Submission Failed!";
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
