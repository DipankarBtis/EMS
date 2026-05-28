package com.etrm.fms.ltcora;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 07/06/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_LtcoraMaster")
public class Frm_LtcoraMaster extends HttpServlet
{
	static String frm_src_file_name="Frm_LtcoraMaster.java";
	public static  Connection dbcon;
	
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
					
					if(option.equalsIgnoreCase("LTCORA_AGREEMENT_MST"))
					{
						InsertUpdateLtcoraAgreementDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_AGREEMENT_BILLING_DTL"))
					{
						InsertUpdateLtcoraAgreementBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CONTRACT_MST"))
					{
						InsertUpdateLtcoraContractDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CN_BILLING_DTL"))
					{
						InsertUpdateLtcoraCnBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CN_CARGO_DTL"))
					{
						InsertUpdateLtcoraCargoCnDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CN_CARGO_ADQ_DTL"))
					{
						InsertUpdateLtcoraCargoCnAdqDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CONT_CARGO_CSOC"))
					{
						InsertUpdateLtcoraCargoCnVarCSOCDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_AGMT_LIABILITY_CLAUSE"))
					{
						InsertUpdateLtcoraAgmtLiabilityDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CN_LIABILITY_CLAUSE"))
					{
						InsertUpdateLtcoraCnLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CARGO_MODREQ"))
					{
						InsertUpdateReqModdtls(request);
					}
					else if(option.equalsIgnoreCase("VARIABLE_TERIFF"))
					{
						InsertUpdateVariableTariffDetail(request);
					}
					else if(option.equalsIgnoreCase("LTCORA_CARGO_CLOSURE_REQUEST"))
					{
						InsertUpdateClosureRequest(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_REJECT_CLOSURE_REQUEST"))
					{
						ApproveRejectClosureRequest(request);
					}
					else if(option.equalsIgnoreCase("APPROVE_REJECT_CLOSURE_TERMINATION"))
					{
						ApproveRejectContClosureRequest(request);
					}
					else if(option.equalsIgnoreCase("REOPEN"))
					{
						ApproveRejectContReopenRequest(request);
					}
					else if(option.equalsIgnoreCase("REOPEN_CLOSED_CARGO"))
					{
						ReopenClosedCargo(request);
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}

	private void ApproveRejectContReopenRequest(HttpServletRequest request) throws SQLException
	{
		String function_nm="ApproveRejectContReopenRequest()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		String counterparty_abbr="";
		try
		{
			String customer_cd=request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agmt_type=request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cont_type=request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
			String buy_sale=request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
			
			//For purchase side
			String trader_cd=request.getParameter("trader_cd")==null?"":request.getParameter("trader_cd");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			
			String counterparty_cd="";
			if(agmt_type.equals("A"))
			{
				counterparty_cd=customer_cd;
			}
			else if (agmt_type.equals("L"))
			{
				counterparty_cd=trader_cd;
				cont_type=contract_type;
			}
			
			cont_name_map=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
			counterparty_abbr=utilBean.getCounterpartyABBR(dbcon, counterparty_cd);
			
			if(opration.equals("REJECT"))
			{
				if(!comp_cd.equals("")&&!counterparty_cd.equals("")&&!agmt_no.equals("")&&!agmt_rev_no.equals("")&&!agmt_type.equals("")&&!cont_no.equals("")&&!cont_rev_no.equals("")&&!cont_type.equals("")&&!buy_sale.equals(""))
				{
					int s_count=0;
					String query2="UPDATE FMS_LTCORA_CONT_MST A "
							+ "SET A.CLOSURE_REQUEST_FLAG=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query2);
					stmt1.setString(++s_count,"X");
					stmt1.setString(++s_count,emp_cd);
					stmt1.setString(++s_count,comp_cd);
					stmt1.setString(++s_count,counterparty_cd);
					stmt1.setString(++s_count,buy_sale);
					stmt1.setString(++s_count,agmt_type);
					stmt1.setString(++s_count,agmt_no);
					stmt1.setString(++s_count,agmt_rev_no);
					stmt1.setString(++s_count,cont_no);
					stmt1.setString(++s_count,cont_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					//for generating logs
					generateLtcoraContLogs(counterparty_cd,buy_sale,agmt_no,agmt_rev_no,agmt_type,cont_no,cont_rev_no,cont_type,emp_cd);
					
					msg = "Successful! - "+cont_name_map+" Contract Re-Open Request Rejection for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Contract Re-Open Request Rejection for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(!comp_cd.equals("")&&!counterparty_cd.equals("")&&!agmt_no.equals("")&&!agmt_rev_no.equals("")&&!agmt_type.equals("")&&!cont_no.equals("")&&!cont_rev_no.equals("")&&!cont_type.equals("")&&!buy_sale.equals(""))
				{
					int s_count=0;
					int new_cont_rev=0;
					String query1="SELECT COUNT(*) FROM FMS_LTCORA_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? ";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(++s_count, comp_cd);
					stmt.setString(++s_count, counterparty_cd);
					stmt.setString(++s_count, agmt_no);
					stmt.setString(++s_count, agmt_rev_no);
					stmt.setString(++s_count, agmt_type);
					stmt.setString(++s_count, cont_no);
					stmt.setString(++s_count, cont_type);
					stmt.setString(++s_count, buy_sale);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						new_cont_rev=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					//New Contract Name
					String cont_name = counterparty_abbr+"-"+comp_abbr+"-LTCORA"+agmt_no+"-REV"+agmt_rev_no+" "+cont_type+cont_no+"-REV"+new_cont_rev;
					
					s_count=0;
					//Inserting the new revision entry in the table 
					String query="INSERT INTO FMS_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV, "
							+ "CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,START_DT, "
							+ "END_DT,AGMT_BASE,CONT_STATUS,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM, SELLER_NOM, "
							+ "SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE, "
							+ "ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,"
							+ "BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE, "
							+ "PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, "
							+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO,LTCORA_TARIFF, "
							+ "LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,?,CONTRACT_TYPE,CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME, "
							+ "START_DT,END_DT,AGMT_BASE,?,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM, "
							+ "SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,SYSDATE,?,?,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM, "
							+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE, "
							+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
							+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE, "
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,"
							+ "TARIFF_DISCOUNT,VOL_DISCOUNT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
							+ "AND CONT_NO=? AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND CONTRACT_TYPE=? ";
					stmt1=dbcon.prepareStatement(query);
					stmt1.setString(++s_count, ""+new_cont_rev); 
					stmt1.setString(++s_count, "R"); 
					stmt1.setString(++s_count, emp_cd); 
					stmt1.setString(++s_count, cont_name); 
					stmt1.setString(++s_count, comp_cd); 
					stmt1.setString(++s_count, counterparty_cd); 
					stmt1.setString(++s_count, buy_sale); 
					stmt1.setString(++s_count, agmt_no); 
					stmt1.setString(++s_count, agmt_rev_no); 
					stmt1.setString(++s_count, agmt_type); 
					stmt1.setString(++s_count, cont_no); 
					stmt1.setString(++s_count, cont_type); 
					stmt1.executeUpdate();
					stmt1.close();
					
					//handling the revision effects in the other tables...
					//TRADER PLANT
					int ctn=0;
					query="INSERT INTO FMS_LTCORA_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
							+ "PLANT_SEQ_NO,ENT_BY, ENT_DT,BUY_SALE) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,?,CONTRACT_TYPE,"
							+ "PLANT_SEQ_NO,?, SYSDATE,BUY_SALE "
							+ "FROM FMS_LTCORA_CONT_PLANT A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++ctn, ""+new_cont_rev);
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, counterparty_cd);
					stmt.setString(++ctn, buy_sale);
					stmt.setString(++ctn, agmt_no);
					stmt.setString(++ctn, agmt_rev_no);
					stmt.setString(++ctn, agmt_type);
					stmt.setString(++ctn, cont_no);
					stmt.setString(++ctn, cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//TRANSPORTER PLANT
					ctn=0;
					query="INSERT INTO FMS_LTCORA_CONT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV,"
							+ "CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE)"
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_REV, CONT_NO,?, CONTRACT_TYPE, TRANSPORTER_CD,"
							+ "PLANT_SEQ_NO, ?, SYSDATE,AGMT_TYPE "
							+ "FROM FMS_LTCORA_CONT_TRANSPTR A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND BUY_SALE=? AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_TRANSPTR B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++ctn, ""+new_cont_rev);
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, counterparty_cd);
					stmt.setString(++ctn, buy_sale);
					stmt.setString(++ctn, agmt_no);
					stmt.setString(++ctn, agmt_rev_no);
					stmt.setString(++ctn, agmt_type);
					stmt.setString(++ctn, cont_no);
					stmt.setString(++ctn, cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//BUSINESS UNIT
					ctn=0;
					query="INSERT INTO FMS_LTCORA_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "PLANT_SEQ_NO,ENT_BY, ENT_DT,BUY_SALE) "
							+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,?,CONTRACT_TYPE,"
							+ "PLANT_SEQ_NO,?, SYSDATE,BUY_SALE "
							+ "FROM FMS_LTCORA_CONT_BU A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++ctn, ""+new_cont_rev);
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, counterparty_cd);
					stmt.setString(++ctn, buy_sale);
					stmt.setString(++ctn, agmt_no);
					stmt.setString(++ctn, agmt_rev_no);
					stmt.setString(++ctn, agmt_type);
					stmt.setString(++ctn, cont_no);
					stmt.setString(++ctn, cont_type);
					stmt.executeUpdate();
					stmt.close();
					
					//STORAGE DETAILS
					ctn=0;
					query="INSERT INTO FMS_LTCORA_CONT_STRG_CRG(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_TYPE,AGMT_REV,CONT_NO, "
							+ "CONT_REV,CONTRACT_TYPE,SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_TYPE,AGMT_REV,CONT_NO,?,CONTRACT_TYPE, "
							+ "SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_DT,ENT_BY,SYSDATE,? "
							+ "FROM FMS_LTCORA_CONT_STRG_CRG A "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_STRG_CRG B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)"
							+ "AND NOT EXISTS(SELECT * FROM FMS_LTCORA_CONT_STRG_CRG B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND CONT_REV=? AND A.BUY_SALE=B.BUY_SALE)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(++ctn, ""+new_cont_rev);
					stmt.setString(++ctn, emp_cd);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, counterparty_cd);
					stmt.setString(++ctn, buy_sale);
					stmt.setString(++ctn, agmt_type);
					stmt.setString(++ctn, agmt_no);
					stmt.setString(++ctn, agmt_rev_no);
					stmt.setString(++ctn, cont_no);
					stmt.setString(++ctn, cont_type);
					stmt.setString(++ctn, ""+new_cont_rev);
					stmt.executeUpdate();
					stmt.close();
					
					//for generating logs
					generateLtcoraContLogs(counterparty_cd,buy_sale,agmt_no,agmt_rev_no,agmt_type,cont_no,""+new_cont_rev,cont_type,emp_cd);
					
					msg = "Successful! - "+cont_name_map+" Contract Re-Open Request Approval for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Contract Re-Open Request Approval for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			
			if(agmt_type.equals("A"))
			{
				url = "../contract_master/frm_cont_reopen_req.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else if(agmt_type.equals("L"))
			{
				url = "../purchase/frm_pur_reopen_cont.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Re-Open Operation submittion Failed";
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
	
	private void ApproveRejectContClosureRequest(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ApproveRejectContClosureRequest()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		String counterparty_abbr="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String customer_cd=request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cont_type=request.getParameter("cont_type")==null?"":request.getParameter("cont_type");
			String agmt_type=request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String buy_sale=request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String clsr_type=request.getParameter("clsr_type")==null?"":request.getParameter("clsr_type");
			String closure_note=request.getParameter("closure_note")==null?"":request.getParameter("closure_note");
			String closure_eff_dt=request.getParameter("closure_eff_dt")==null?"":request.getParameter("closure_eff_dt");
			
			//Below are fetched from Purchase side 
			String trader_cd = request.getParameter("trader_cd")==null?"":request.getParameter("trader_cd");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String closure_request_flag = request.getParameter("closure_request_flag")==null?"":request.getParameter("closure_request_flag");
			String closure_dt = request.getParameter("closure_dt")==null?"":request.getParameter("closure_dt");
			
			String counterparty_cd = "";
			if(agmt_type.equals("A"))
			{
				counterparty_cd=customer_cd;
			}
			else if(agmt_type.equals("L"))
			{
				counterparty_cd=trader_cd;
				cont_type=contract_type;
				clsr_type=closure_request_flag;
				closure_eff_dt=closure_dt;
			}
			
			cont_name_map=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
			counterparty_abbr=utilBean.getCounterpartyABBR(dbcon, counterparty_cd);
			
			if(opration.equals("REJECT"))
			{
				if(!comp_cd.equals("")&&!counterparty_cd.equals("")&&!agmt_no.equals("")&&!agmt_rev_no.equals("")&&!agmt_type.equals("")&&!cont_no.equals("")&&!cont_rev_no.equals("")&&!cont_type.equals("")&&!buy_sale.equals(""))
				{
					String cont_status="";
					String fcc_flg="";
					String fcc_by="";
					String fcc_dt="";
					int s_count=0;
					//Fetching the previous data 
					String query1="SELECT CONT_STATUS,FCC_FLAG,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(CONT_REV)-1 FROM FMS_LTCORA_CONT_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++s_count,comp_cd);
					stmt1.setString(++s_count,counterparty_cd);
					stmt1.setString(++s_count,buy_sale);
					stmt1.setString(++s_count,agmt_type);
					stmt1.setString(++s_count,agmt_no);
					stmt1.setString(++s_count,agmt_rev_no);
					stmt1.setString(++s_count,cont_no);
					stmt1.setString(++s_count,cont_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						cont_status=rset1.getString(1)==null?"":rset1.getString(1);
						fcc_flg=rset1.getString(2)==null?"":rset1.getString(2);
						fcc_by=rset1.getString(3)==null?"":rset1.getString(3);
						fcc_dt=rset1.getString(4)==null?"":rset1.getString(4);
					}
					rset1.close();
					stmt1.close();
					
					s_count=0;
					String query2="UPDATE FMS_LTCORA_CONT_MST A "
							+ "SET A.CONT_STATUS=?, A.FCC_FLAG=?,A.FCC_BY=?,A.FCC_DATE=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),A.CLOSURE_REQUEST_FLAG=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query2);
					stmt1.setString(++s_count,cont_status);
					stmt1.setString(++s_count,fcc_flg);
					stmt1.setString(++s_count,fcc_by);
					stmt1.setString(++s_count,fcc_dt);
					stmt1.setString(++s_count,"N");
					stmt1.setString(++s_count,comp_cd);
					stmt1.setString(++s_count,counterparty_cd);
					stmt1.setString(++s_count,buy_sale);
					stmt1.setString(++s_count,agmt_type);
					stmt1.setString(++s_count,agmt_no);
					stmt1.setString(++s_count,agmt_rev_no);
					stmt1.setString(++s_count,cont_no);
					stmt1.setString(++s_count,cont_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					//for generating logs
					generateLtcoraContLogs(counterparty_cd,buy_sale,agmt_no,agmt_rev_no,agmt_type,cont_no,cont_rev_no,cont_type,emp_cd);
					
					msg = "Successful! - "+cont_name_map+" Contract Closure Request Rejection for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Contract Closure Request Rejection for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(!comp_cd.equals("")&&!counterparty_cd.equals("")&&!agmt_no.equals("")&&!agmt_rev_no.equals("")&&!agmt_type.equals("")&&!cont_no.equals("")&&!cont_rev_no.equals("")&&!cont_type.equals("")&&!buy_sale.equals(""))
				{
					String cont_status="";
					if(clsr_type.equals("Y"))
					{
						cont_status="C";
					}
					else if(clsr_type.equals("R"))
					{
						cont_status="T";
					}
					
					int s_count=0;
					String query2="UPDATE FMS_LTCORA_CONT_MST A "
							+ "SET A.CONT_STATUS=?, A.CLOSURE_REQUEST_FLAG=?,A.CLOSURE_REMARK=?,A.CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=dbcon.prepareStatement(query2);
					stmt1.setString(++s_count,cont_status);
					stmt1.setString(++s_count,"A");
					stmt1.setString(++s_count,closure_note);
					stmt1.setString(++s_count,closure_eff_dt);
					stmt1.setString(++s_count,"Y");
					stmt1.setString(++s_count,emp_cd);
					stmt1.setString(++s_count,comp_cd);
					stmt1.setString(++s_count,counterparty_cd);
					stmt1.setString(++s_count,buy_sale);
					stmt1.setString(++s_count,agmt_type);
					stmt1.setString(++s_count,agmt_no);
					stmt1.setString(++s_count,agmt_rev_no);
					stmt1.setString(++s_count,cont_no);
					stmt1.setString(++s_count,cont_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					//for generating logs
					generateLtcoraContLogs(counterparty_cd,buy_sale,agmt_no,agmt_rev_no,agmt_type,cont_no,cont_rev_no,cont_type,emp_cd);
					
					msg = "Successful! - "+cont_name_map+" Contract Closure Request Approval for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Contract Closure Request Approval for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			
			if(agmt_type.equals("A"))
			{
				url = "../contract_master/frm_cont_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else if(agmt_type.equals("L"))
			{
				url = "../purchase/frm_pur_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Closure Request submittion Failed";
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
	
	private void ReopenClosedCargo(HttpServletRequest request) throws SQLException
	{
		String function_nm="ReopenClosedCargo()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String buy_sale = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
			String agreement_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String cargo_no = request.getParameter("cargoNo")==null?"":request.getParameter("cargoNo");
			String cargo_ref = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String sug_per = request.getParameter("sug_per")==null?"":request.getParameter("sug_per");
			String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
			int no_cargo = Integer.parseInt(request.getParameter("no_cargo")==null?"0":request.getParameter("no_cargo"));
			
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			
			String cargo_cont_status = "";
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals("") && !cargo_no.equals(""))
			{
				cargo_cont_status="O";
				query="UPDATE FMS_LTCORA_CONT_CARGO_DTL A "
						+ "SET A.CLOSURE_REQUEST_FLAG=?, A.CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),A.CLOSURE_ALLOC_QTY=?,"
						+ "A.MODIFY_BY=?,A.MODIFY_DT=SYSDATE,A.CARGO_STATUS=?  "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? "
						+ "AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.CONT_NO=? AND A.CONT_REV=? AND A.CARGO_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1,cargo_cont_status);
				stmt.setString(2,"");
				stmt.setString(3,"");
				stmt.setString(4,emp_cd);
				stmt.setString(5,"Y");
				stmt.setString(6,comp_cd);
				stmt.setString(7,counterparty_cd);
				stmt.setString(8, buy_sale);
				stmt.setString(9,agreement_type);
				stmt.setString(10,agmt_no);
				stmt.setString(11,agmt_rev_no);
				stmt.setString(12,contract_type);
				stmt.setString(13,cont_no);
				stmt.setString(14,cont_rev_no);
				stmt.setString(15,cargo_no);
				stmt.executeUpdate();
				stmt.close();
				
				msg = "Successful! - "+cont_name_map+" Cargo Re-Open for "+counterparty_abbr+" submitted Successfully!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Cargo Re-Open for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../ltcora/frm_ltcora_sell_cargo_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&cont_no="+cont_no+
					"&disp_cont_no="+disp_cont_no+"&cont_ref_no="+cont_ref_no+"&buy_sell="+buy_sale+"&cont_rev="+cont_rev_no+"&start_dt="+start_dt+"&end_dt="+end_dt+
					"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+"&agmt_rev="+agmt_rev_no+"&agmt_type="+agreement_type+"&sug_per="+sug_per+commonUrl_pra;;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Re-Open Operation submittion Failed";
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
	
	private void ApproveRejectClosureRequest(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ApproveRejectClosureRequest()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String clsr_dt = request.getParameter("clsr_dt")==null?"":request.getParameter("clsr_dt");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			String cargo_ref = request.getParameter("cargo_ref")==null?"":request.getParameter("cargo_ref");
			
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String cargo_cont_status = "";
			
			if(opration.equals("REJECT"))
			{
				if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals("") && !cargo_no.equals(""))
				{
					cargo_cont_status="X";
					query="UPDATE FMS_LTCORA_CONT_CARGO_DTL A "
							+ "SET A.CLOSURE_REQUEST_FLAG=?, A.CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),A.CLOSURE_ALLOC_QTY=?,"
							+ "A.MODIFY_BY=?,A.MODIFY_DT=SYSDATE  "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? "
							+ "AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.CONT_NO=? AND A.CONT_REV=? AND A.CARGO_NO=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,cargo_cont_status);
					stmt.setString(2,"");
					stmt.setString(3,"");
					stmt.setString(4,emp_cd);
					stmt.setString(5,comp_cd);
					stmt.setString(6,counterparty_cd);
					stmt.setString(7, buy_sale);
					stmt.setString(8,agreement_type);
					stmt.setString(9,agmt_no);
					stmt.setString(10,agmt_rev_no);
					stmt.setString(11,contract_type);
					stmt.setString(12,cont_no);
					stmt.setString(13,cont_rev_no);
					stmt.setString(14,cargo_no);
					stmt.executeUpdate();
					stmt.close();
					
					msg = "Successful! - "+cont_name_map+" Closure Request Rejection for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Closure Request Rejection for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			else if(opration.equals("APPROVE"))
			{
				if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals("") && !cargo_no.equals(""))
				{
					cargo_cont_status="A";
					query="UPDATE FMS_LTCORA_CONT_CARGO_DTL A "
							+ "SET A.CLOSURE_REQUEST_FLAG=?, "
							+ "A.CLOSURE_ALLOC_QTY=?,"
							+ "A.MODIFY_BY=?,A.MODIFY_DT=SYSDATE,A.CARGO_STATUS=?  "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? "
							+ "AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.CONT_NO=? AND A.CONT_REV=? AND A.CARGO_NO=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1,cargo_cont_status);
					stmt.setString(2,alloc_qty);
					stmt.setString(3,emp_cd);
					stmt.setString(4,"C");
					stmt.setString(5,comp_cd);
					stmt.setString(6,counterparty_cd);
					stmt.setString(7, buy_sale);
					stmt.setString(8,agreement_type);
					stmt.setString(9,agmt_no);
					stmt.setString(10,agmt_rev_no);
					stmt.setString(11,contract_type);
					stmt.setString(12,cont_no);
					stmt.setString(13,cont_rev_no);
					stmt.setString(14,cargo_no);
					stmt.executeUpdate();
					stmt.close();
					
					msg = "Successful! - "+cont_name_map+" Closure Request Approval for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Failed! - "+cont_name_map+" Closure Request Approval for "+counterparty_abbr+" submission Failed!";
					msg_type = "E";
				}
			}
			url = "../ltcora/frm_ltcora_cargo_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale+
					"&closure_eff_dt="+clsr_dt+"&cargo_cont_status="+cargo_cont_status+"&cargo_ref="+cargo_ref+"&cargo_no="+cargo_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Closure Request submittion Failed";
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
	
	private void InsertUpdateClosureRequest(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateClosureRequest()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String cargo_ref = request.getParameter("cargo_ref")==null?"":request.getParameter("cargo_ref");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String clsr_dt = request.getParameter("clsr_dt")==null?"":request.getParameter("clsr_dt");
			String alloc_qty = request.getParameter("alloc_qty")==null?"":request.getParameter("alloc_qty");
			
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String clsr_flag = "R";
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals("") && !cargo_no.equals(""))
			{
				query="UPDATE FMS_LTCORA_CONT_CARGO_DTL A "
						+ "SET A.CLOSURE_REQUEST_FLAG=?, A.CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),A.CLOSURE_ALLOC_QTY=?,"
						+ "A.MODIFY_BY=?,A.MODIFY_DT=SYSDATE  "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? "
						+ "AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.CONT_NO=? AND A.CONT_REV=? AND A.CARGO_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1,"R");
				stmt.setString(2,clsr_dt);
				stmt.setString(3,alloc_qty);
				stmt.setString(4,emp_cd);
				stmt.setString(5,comp_cd);
				stmt.setString(6,counterparty_cd);
				stmt.setString(7, buy_sale);
				stmt.setString(8,agreement_type);
				stmt.setString(9,agmt_no);
				stmt.setString(10,agmt_rev_no);
				stmt.setString(11,contract_type);
				stmt.setString(12,cont_no);
				stmt.setString(13,cont_rev_no);
				stmt.setString(14,cargo_no);
				stmt.executeUpdate();
				
				stmt.close();
				msg = "Successful! - "+cont_name_map+" Closure Request for "+counterparty_abbr+" submitted Successfully!";
				msg_type = "S";
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Closure Request for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			url = "../ltcora/frm_ltcora_cargo_closure_request.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&contract_type="+contract_type+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale+
					"&closure_eff_dt="+clsr_dt+"&cargo_number="+cargo_ref+"&closure_flag="+clsr_flag+"&closure_qty="+alloc_qty+"&cargo_no="+cargo_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Closure Request submittion Failed";
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
	
	private void InsertUpdateVariableTariffDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateVariableTariffDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String storage_tariff_unit = request.getParameter("storage_tariff_unit")==null?"":request.getParameter("storage_tariff_unit");
			String storage_tariff = request.getParameter("storage_tariff")==null?"":request.getParameter("storage_tariff");
			
			String[] from_days = request.getParameterValues("from_days");
			String[] to_days = request.getParameterValues("to_days");
			String[] tariff_value = request.getParameterValues("tariff_value");
			String till_end = request.getParameter("till_end")==null?"N":request.getParameter("till_end");
			
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+agmt_no+"-"+cont_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(from_days!=null)
			{
				query="DELETE FROM FMS_LTCORA_CONT_STRG_CRG A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_STRG_CRG B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO)";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, buy_sale);
				stmt.setString(7, agreement_type);
				stmt.executeUpdate();
				
				stmt.close();
				
				for(int i=0; i<from_days.length;i++)
				{
					String seq_no="1";
					query="SELECT MAX(SEQ_NO) "
							+ "FROM FMS_LTCORA_CONT_STRG_CRG "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND BUY_SALE=? AND AGMT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, buy_sale);
					stmt1.setString(7, agreement_type);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						seq_no=""+(rset1.getInt(1)+1);
					}
					rset1.close();
					stmt1.close();
					
					 String till_end_value = "";
					 if(i == from_days.length - 1)
					 {
						 till_end_value=till_end;
					 }
					 else
					 {
						 till_end_value="N";
					 }
					 
					 //System.out.println("till_end_value....."+till_end_value);
					
					query="INSERT INTO FMS_LTCORA_CONT_STRG_CRG(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_TYPE,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_DT,ENT_BY,TILL_END) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?)";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, buy_sale);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agreement_type);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, cont_no);
					stmt2.setString(8, cont_rev_no);
					stmt2.setString(9, contract_type);
					stmt2.setString(10, seq_no);
					stmt2.setString(11, from_days[i]);
					if(till_end_value.equals("Y"))
					{
						stmt2.setString(12, "");
					}
					else
					{
						stmt2.setString(12, to_days[i]);
					}
					stmt2.setString(13, tariff_value[i]);
					stmt2.setString(14, emp_cd);
					stmt2.setString(15, till_end_value);
					stmt2.executeUpdate();
					
					stmt2.close();
					
					msg = "Successful! - "+cont_name_map+" Variable Tariff Data for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Variable Tariff Data for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../ltcora/frm_ltcora_variable_tariff_config.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+"&storage_tariff="+storage_tariff+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+"&storage_tariff_unit="+storage_tariff_unit+
					"&end_dt="+end_dt+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - "+cont_name_map+" Variable Teriff Insert/Update Failed";
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
	
	private void InsertUpdateReqModdtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateReqModdtls()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String cargo_ref = request.getParameter("cargo_ref")==null?"":request.getParameter("cargo_ref");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String ltcora_tariff = request.getParameter("ltcora_tariff")==null?"":request.getParameter("ltcora_tariff");
			String ltcora_tariff_unit = request.getParameter("ltcora_tariff_unit")==null?"":request.getParameter("ltcora_tariff_unit");
			String sug_per = request.getParameter("sug_per")==null?"":request.getParameter("sug_per");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			
			if(opration.equals("APPROVE"))
			{
				int count=0;
				query="UPDATE FMS_LTCORA_CONT_CARGO_MOD SET APPROVAL_FLAG=?,APPROVE_BY=?,APPROVAL_DT=SYSDATE "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
				// AND AGMT_REV=?  AND CONT_REV=?
				stmt = dbcon.prepareStatement(query);
				stmt.setString(++count, "Y");
				stmt.setString(++count, emp_cd);
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, counterparty_cd);
				stmt.setString(++count, buy_sale);
				stmt.setString(++count, agreement_type);
				stmt.setString(++count, agmt_no);
				//stmt.setString(++count, agmt_rev_no);
				stmt.setString(++count, contract_type);
				stmt.setString(++count, cont_no);
				stmt.setString(++count, cargo_no);
				//stmt.setString(++count, cont_rev_no);
				stmt.executeUpdate();
				
				// Update CARGO_STATUS at FMS_LTCORA_CONT_CARGO_DTL 				
				count=0;
				query1="UPDATE FMS_LTCORA_CONT_CARGO_DTL SET CARGO_STATUS=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
				// AND AGMT_REV=? AND CONT_REV=?
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(++count, "Y");
				stmt1.setString(++count, comp_cd);
				stmt1.setString(++count, counterparty_cd);
				stmt1.setString(++count, buy_sale);
				stmt1.setString(++count, agreement_type);
				stmt1.setString(++count, agmt_no);
				//stmt1.setString(++count, agmt_rev_no);
				stmt1.setString(++count, contract_type);
				stmt1.setString(++count, cont_no);
				stmt1.setString(++count, cargo_no);
				//stmt1.setString(++count, cont_rev_no);
				stmt1.executeUpdate();
				
				stmt1.close();
				stmt.close();
				
				msg_type="S";
				msg = "Successful! - Cargo Modification Request for "+cont_name_map+" Approved for "+counterparty_abbr+" Successfully!";
			}
			else if(opration.equals("INSERT"))
			{
				int cnt=0;
				query2="SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=?"
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
						+ "AND CARGO_NO=?";
				// AND AGMT_REV=? AND CONT_REV=? 
				stmt2 = dbcon.prepareStatement(query2);
				stmt2.setString(++cnt, comp_cd);
				stmt2.setString(++cnt, counterparty_cd);
				stmt2.setString(++cnt, buy_sale);
				stmt2.setString(++cnt, agreement_type);
				stmt2.setString(++cnt, agmt_no);
				//stmt2.setString(++cnt, agmt_rev_no);
				stmt2.setString(++cnt, contract_type);
				stmt2.setString(++cnt, cont_no);
				//stmt2.setString(++cnt, cont_rev_no);
				stmt2.setString(++cnt, cargo_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count=rset2.getInt(1);
					
					if(count>0)
					{
						int cnt1=0;
						query="UPDATE FMS_LTCORA_CONT_CARGO_MOD SET LTCORA_TARIFF=?,LTCORA_TARIFF_UNIT=?,SUG=?, "
								+ "MODIFY_BY=?,MODIFY_DT=SYSDATE, APPROVAL_FLAG=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
							// AND AGMT_REV=? AND CONT_REV=?							
							stmt = dbcon.prepareStatement(query);
							stmt.setString(++cnt1, ltcora_tariff);
							stmt.setString(++cnt1, ltcora_tariff_unit);
							stmt.setString(++cnt1, sug_per);
							stmt.setString(++cnt1, emp_cd);
							stmt.setString(++cnt1, "N");
							stmt.setString(++cnt1, comp_cd);
							stmt.setString(++cnt1, counterparty_cd);
							stmt.setString(++cnt1, buy_sale);
							stmt.setString(++cnt1, agreement_type);
							stmt.setString(++cnt1, agmt_no);
							//stmt.setString(++cnt1, agmt_rev_no);
							stmt.setString(++cnt1, contract_type);
							stmt.setString(++cnt1, cont_no);
							stmt.setString(++cnt1, cargo_no);
							//stmt.setString(++cnt1, cont_rev_no);
							stmt.executeUpdate();
							
							stmt.close();
					}
					else
					{
						int count1=0;
						query="INSERT INTO FMS_LTCORA_CONT_CARGO_MOD(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,"
								+ "CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(++count1, comp_cd);
						stmt.setString(++count1, counterparty_cd);
						stmt.setString(++count1, buy_sale);
						stmt.setString(++count1, agreement_type);
						stmt.setString(++count1, agmt_no);
						stmt.setString(++count1, agmt_rev_no);
						stmt.setString(++count1, contract_type);
						stmt.setString(++count1, cont_no);
						stmt.setString(++count1, cont_rev_no);
						stmt.setString(++count1, cargo_no);
						stmt.setString(++count1, ltcora_tariff);
						stmt.setString(++count1, ltcora_tariff_unit);
						stmt.setString(++count1, sug_per);
						stmt.setString(++count1, emp_cd);
						stmt.executeUpdate();
						
						stmt.close();
					}
					
					int count1=0;
					query1="UPDATE FMS_LTCORA_CONT_CARGO_DTL SET CARGO_STATUS=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? AND CONT_REV=?";
					stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++count1, "N");
					stmt1.setString(++count1, comp_cd);
					stmt1.setString(++count1, counterparty_cd);
					stmt1.setString(++count1, buy_sale);
					stmt1.setString(++count1, agreement_type);
					stmt1.setString(++count1, agmt_no);
					stmt1.setString(++count1, agmt_rev_no);
					stmt1.setString(++count1, contract_type);
					stmt1.setString(++count1, cont_no);
					stmt1.setString(++count1, cargo_no);
					stmt1.setString(++count1, cont_rev_no);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				rset2.close();
				stmt2.close();
				
				msg_type="S";
				msg = "Successful! - Cargo Modification Request "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
				opration="MODIFY";
			}
			
			url = "../ltcora/frm_ltcora_cargo_request_modification.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agreement_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&buy_sale="+buy_sale+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+"&cargo_number="+cargo_ref+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Cargo Modification Request Insert/Update Failed";
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

	private void InsertUpdateLtcoraAgreementDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraAgreementDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals("")) {
				status="A";
			}
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String mdcq_flag = request.getParameter("mdcq_flag")==null?"N":request.getParameter("mdcq_flag");
			String mdcq_percent = request.getParameter("mdcq_percent")==null?"":request.getParameter("mdcq_percent");
			String mdcq_clause_no = request.getParameter("mdcq_clause_no")==null?"":request.getParameter("mdcq_clause_no");
		
			String billing_flag = request.getParameter("billing_flag")==null?"N":request.getParameter("billing_flag");
			String billing_clause_no = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_clause_no = request.getParameter("buy_clause_no")==null?"":request.getParameter("buy_clause_no");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String sell_clause_no = request.getParameter("sell_clause_no")==null?"":request.getParameter("sell_clause_no");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_clause_no = request.getParameter("day_clause_no")==null?"":request.getParameter("day_clause_no");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
			String measurementCheckbox = request.getParameter("measurementCheckbox")==null?"N":request.getParameter("measurementCheckbox");
			String measure_clause_no = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			
			String off_spec_gas_checkbox = request.getParameter("off_spec_gas_checkbox")==null?"N":request.getParameter("off_spec_gas_checkbox");
			String spec_clause_no = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			
			String liability_checkbox = request.getParameter("liability_checkbox")==null?"N":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			
			String terminator_checkbox = request.getParameter("terminator_checkbox")==null?"N":request.getParameter("terminator_checkbox");
			String terminate_clause_no = request.getParameter("terminate_clause_no")==null?"":request.getParameter("terminate_clause_no");
			String terminate_planed = request.getParameter("terminate_planed")==null?"N":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"N":request.getParameter("terminate_force");
			
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
				query="SELECT MAX(AGMT_NO) FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND BUY_SALE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, buy_sale);
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
					query="SELECT MAX(AGMT_REV) "
							+ "FROM FMS_LTCORA_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND BUY_SALE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_type);
					stmt.setString(4, agmt_no);
					stmt.setString(5, buy_sale);
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
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !buy_sale.equals("") && !agmt_type.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND BUY_SALE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
			
				if(rev_count > 0)
				{
					query ="UPDATE FMS_LTCORA_AGMT_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "AGMT_TYP=?,BUYER_NOM=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ=?,MDCQ_PERCENTAGE=?,"
							+ "REMARK=?,AGMT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),BUYER_FORNGT_NOM=?,"
							+ "SELLER_FORNGT_NOM=?,BUYER_NOM_CUTOFF=?,BUYER_NOM_CLAUSE=?,SELLER_NOM_CLAUSE=?,DAY_DEF_CLAUSE=?,"
							+ "MDCQ_CLAUSE=?,MEASUREMENT=?,MEAS_CLAUSE=?,MEAS_STANDARD=?,MEAS_TEMPERATURE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?,"
							+ "OFF_SPEC_GAS=?,SPEC_CLAUSE=?,SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,"
							+ "LIABILITY=?,LIAB_CLAUSE=?,BILLING_CLAUSE=?,"
							+ "TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND BUY_SALE=?";
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
					stmt0.setString(18, mdcq_flag);
					stmt0.setString(19, mdcq_percent);
					stmt0.setString(20, remark);
					stmt0.setString(21, cont_name);
					stmt0.setString(22, emp_cd);
					stmt0.setString(23, status);
					stmt0.setString(24, billing_flag);
					stmt0.setString(25, rev_eff_dt);
					stmt0.setString(26, buy_f);
					stmt0.setString(27, sel_f);
					stmt0.setString(28, buy_nom_cutoff);
					stmt0.setString(29, buy_clause_no);
					stmt0.setString(30, sell_clause_no);
					stmt0.setString(31, day_clause_no);
					stmt0.setString(32, mdcq_clause_no);
					stmt0.setString(33, measurementCheckbox);
					stmt0.setString(34, measure_clause_no);
					stmt0.setString(35, meas_standard);
					stmt0.setString(36, meas_temperature);
					stmt0.setString(37, pressure_min_bar);
					stmt0.setString(38, pressure_max_bar);
					stmt0.setString(39, off_spec_gas_checkbox);
					stmt0.setString(40, spec_clause_no);
					stmt0.setString(41, spec_gas_energy_base);
					stmt0.setString(42, spec_gas_min_energy);
					stmt0.setString(43, spec_gas_max_energy);
					stmt0.setString(44, liability_checkbox);
					stmt0.setString(45, liability_clause);
					stmt0.setString(46, billing_clause_no);
					stmt0.setString(47, terminator_checkbox);
					stmt0.setString(48, terminate_clause_no);
					stmt0.setString(49, terminate_planed);
					stmt0.setString(50, terminate_force);
					stmt0.setString(51, comp_cd);
					stmt0.setString(52, counterparty_cd);
					stmt0.setString(53, agmt_no);
					stmt0.setString(54, agmt_rev_no);
					stmt0.setString(55, agmt_type);
					stmt0.setString(56, buy_sale);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					query = "INSERT INTO FMS_LTCORA_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_REF_NO,SIGNING_DT,"
							+ "START_DT,END_DT,AGMT_BASE,AGMT_TYP,STATUS, "
							+ "BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
							+ "REMARK,ENT_DT,ENT_BY,AGMT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
							+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,"
							+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
							+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
							+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE) "
							+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "?,SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, counterparty_cd);
					stmt0.setString(3, agmt_type);
					stmt0.setString(4, agmt_no);
					stmt0.setString(5, agmt_rev_no);
					stmt0.setString(6, agmt_ref_no);
					stmt0.setString(7, signing_dt);
					stmt0.setString(8, start_dt);
					stmt0.setString(9, end_dt);
					stmt0.setString(10, agreement_base);
					stmt0.setString(11, agreement_type);
					stmt0.setString(12, status);
					stmt0.setString(13, buyer_nom);
					stmt0.setString(14, buy_m);
					stmt0.setString(15, buy_w);
					stmt0.setString(16, buy_d);
					stmt0.setString(17, seller_nom);
					stmt0.setString(18, sel_m);
					stmt0.setString(19, sel_w);
					stmt0.setString(20, sel_d);
					stmt0.setString(21, day_def);
					stmt0.setString(22, day_time_from);
					stmt0.setString(23, day_time_to);
					stmt0.setString(24, mdcq_flag);
					stmt0.setString(25, mdcq_percent);
					stmt0.setString(26, remark);
					stmt0.setString(27, emp_cd);
					stmt0.setString(28, cont_name);
					stmt0.setString(29, billing_flag);
					stmt0.setString(30, rev_eff_dt);
					stmt0.setString(31, buy_f);
					stmt0.setString(32, sel_f);
					stmt0.setString(33, buy_nom_cutoff);
					stmt0.setString(34, buy_sale);
					stmt0.setString(35, buy_clause_no);
					stmt0.setString(36, sell_clause_no);
					stmt0.setString(37, day_clause_no);
					stmt0.setString(38, mdcq_clause_no);
					stmt0.setString(39, measurementCheckbox);
					stmt0.setString(40, measure_clause_no);
					stmt0.setString(41, meas_standard);
					stmt0.setString(42, meas_temperature);
					stmt0.setString(43, pressure_min_bar);
					stmt0.setString(44, pressure_max_bar);
					stmt0.setString(45, off_spec_gas_checkbox);
					stmt0.setString(46, spec_clause_no);
					stmt0.setString(47, spec_gas_energy_base);
					stmt0.setString(48, spec_gas_min_energy);
					stmt0.setString(49, spec_gas_max_energy);
					stmt0.setString(50, liability_checkbox);
					stmt0.setString(51, liability_clause);
					stmt0.setString(52, billing_clause_no);
					stmt0.setString(53, terminator_checkbox);
					stmt0.setString(54, terminate_clause_no);
					stmt0.setString(55, terminate_planed);
					stmt0.setString(56, terminate_force);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				
				int count=0;
				//TRADER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				stmt1.setString(6, buy_sale);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					 count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_LTCORA_AGMT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.setString(6, buy_sale);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_LTCORA_AGMT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT,BUY_SALE) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.setString(8, buy_sale);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agmt_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				stmt_temp.setString(6, buy_sale);
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
					query = "DELETE FROM FMS_LTCORA_AGMT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.setString(6, buy_sale);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_LTCORA_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT,BUY_SALE) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.setString(8, buy_sale);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				// LTCORA(SELL) Delivery Point
				if("C".equals(buy_sale))
				{
					//TRANSPORTER PLANT
					query = "SELECT COUNT(*) "
							+ "FROM FMS_LTCORA_AGMT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, comp_cd);
					stmt0.setString(2, counterparty_cd);
					stmt0.setString(3, agmt_type);
					stmt0.setString(4, agmt_no);
					stmt0.setString(5, agmt_rev_no);
					stmt0.setString(6, buy_sale);
					rset0 = stmt0.executeQuery();
					count = 0;
					if(rset0.next())
					{
						 count = rset0.getInt(1);
					}
					rset0.close();
					stmt0.close();
					
					if(count>0)
					{
						query = "DELETE FROM FMS_LTCORA_AGMT_TRANSPTR "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, buy_sale);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					if(trans_cd!=null)
					{
						for(int i=0;i<trans_cd.length;i++)
						{
							query = "INSERT INTO FMS_LTCORA_AGMT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, buy_sale);
							stmt1.setString(4, agmt_type);
							stmt1.setString(5, agmt_no);
							stmt1.setString(6, agmt_rev_no);
							stmt1.setString(7, trans_cd[i]);
							stmt1.setString(8, trans_plant_seq_no[i]);
							stmt1.setString(9, emp_cd);
							stmt1.executeUpdate();
							
							stmt1.close();
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
					msg = "Successful! - Agreement "+cont_name_map+" for "+counterparty_abbr+" Modified Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Agreement "+cont_name_map+" Modification for "+counterparty_abbr+" Failed!";
				msg_type="E";
			}
			
			if("C".equals(buy_sale)) 
			{
				url = "../ltcora/frm_ltcora_agmt_sell_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+commonUrl_pra;
			}
			else if ("T".equals(buy_sale))
			{
				url = "../ltcora/frm_ltcora_agmt_buy_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+cont_name_map+" Insert/Update Failed";
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
	
	private void InsertUpdateLtcoraAgreementBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraAgreementBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map ="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			
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
			
			String billing_days=request.getParameter("billing_days")==null?"":request.getParameter("billing_days");
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
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals("") && !buy_sale.equals(""))
			{
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "
						+ "AND AGMT_TYPE=? AND BUY_SALE=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				//stmt3.setString(4, agmt_rev_no);
				stmt3.setString(4, agreement_type);
				stmt3.setString(5, buy_sale);
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
							+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND BUY_SALE=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					//stmt.setString(4, agmt_rev_no);
					stmt.setString(4, agreement_type);
					stmt.setString(5, buy_sale);
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
						query="UPDATE FMS_LTCORA_AGMT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_TYPE=? AND BUY_SALE=? AND PLANT_SEQ_NO=?";
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
						stmt1.setString(++cnt, billing_days);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, agmt_no);
						//stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, buy_sale);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_LTCORA_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BUY_SALE,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?)";
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
						stmt1.setString(++cnt1, buy_sale);
						stmt1.setString(++cnt1, billing_days);
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
			
			url = "../ltcora/frm_ltcora_agmt_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&buy_sale="+buy_sale+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! "+cont_name_map+" Billing Detail Insert/Update Failed";

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
	
	private void InsertUpdateLtcoraContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraContractDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			/*
			CLOSURE_REQUEST_FLAG = 'Y' IF CLOSURE REQUEST IS GENERATED
			CLOSURE_REQUEST_FLAG = 'N' IF CLOSURE REQUEST IS DECLINED
			CLOSURE_REQUEST_FLAG = 'A' IF CLOSURE REQUEST IS APPROVED 
			CLOSURE_REQUEST_FLAG = 'T' IF TERMINATE REQUEST IS GENERATED 
			CLOSURE_REQUEST_FLAG = 'O' IF CONTRACT REOPEN REQUEST IS GENERATED
			CLOSURE_REQUEST_FLAG = 'X' IF CONTRACT REOPEN REQUEST IS REJECTED
			*/			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
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
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String cont_status_flg=request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg");
			
			if(cont_status_flg.equals(""))
			{
				cont_status_flg="F";
			}
			if(status.equals("")) 
			{
				status="F";
			}
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			String tmp_chk_plant = request.getParameter("tmp_chk_plant")==null?"":request.getParameter("tmp_chk_plant");
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String[] chk_fill_station = request.getParameterValues("chk_fill_station");
			String[] charge_abbr = request.getParameterValues("charge_abbr");
			String[] chk_truck_trans = request.getParameterValues("chk_truck_trans");
			
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String mdcq_flag = request.getParameter("mdcq_flag")==null?"N":request.getParameter("mdcq_flag");
			String mdcq_percent = request.getParameter("mdcq_percent")==null?"":request.getParameter("mdcq_percent");
			String mdcq_clause_no = request.getParameter("mdcq_clause_no")==null?"":request.getParameter("mdcq_clause_no");
		
			String billing_flag = request.getParameter("billing_flag")==null?"N":request.getParameter("billing_flag");
			String billing_clause_no = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_clause_no = request.getParameter("buy_clause_no")==null?"":request.getParameter("buy_clause_no");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String sell_clause_no = request.getParameter("sell_clause_no")==null?"":request.getParameter("sell_clause_no");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_clause_no = request.getParameter("day_clause_no")==null?"":request.getParameter("day_clause_no");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			
			String measurementCheckbox = request.getParameter("measurementCheckbox")==null?"N":request.getParameter("measurementCheckbox");
			String measure_clause_no = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			
			String off_spec_gas_checkbox = request.getParameter("off_spec_gas_checkbox")==null?"N":request.getParameter("off_spec_gas_checkbox");
			String spec_clause_no = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			
			String liability_checkbox = request.getParameter("liability_checkbox")==null?"N":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			
			String terminator_checkbox = request.getParameter("terminator_checkbox")==null?"N":request.getParameter("terminator_checkbox");
			String terminate_clause_no = request.getParameter("terminate_clause_no")==null?"":request.getParameter("terminate_clause_no");
			String terminate_planed = request.getParameter("terminate_planed")==null?"N":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"N":request.getParameter("terminate_force");
			
			String no_of_cargo = request.getParameter("no_of_cargo")==null?"":request.getParameter("no_of_cargo");
			String ltcora_tariff = request.getParameter("ltcora_tariff")==null?"":request.getParameter("ltcora_tariff");
			String ltcora_tariff_unit = request.getParameter("ltcora_tariff_unit")==null?"":request.getParameter("ltcora_tariff_unit");
			String sug = request.getParameter("sug")==null?"":request.getParameter("sug");
			String tariff_discount = request.getParameter("tariff_discount")==null?"":request.getParameter("tariff_discount");
			String vol_discount = request.getParameter("vol_discount")==null?"":request.getParameter("vol_discount");
			String adv_adjust = request.getParameter("adv_adjust")==null?"":request.getParameter("adv_adjust");
			String tlu_flag = request.getParameter("tlu_flag")==null?"":request.getParameter("tlu_flag");
			
			String[] dlng_chk_plant = request.getParameterValues("dlng_chk_plant");
			
			String extend_storage = request.getParameter("extend_storage")==null?"":request.getParameter("extend_storage");
			String discount_days = request.getParameter("discount_days")==null?"":request.getParameter("discount_days");
			String storage_tariff = request.getParameter("storage_tariff")==null?"":request.getParameter("storage_tariff");
			String storage_tariff_unit = request.getParameter("storage_tariff_unit")==null?"":request.getParameter("storage_tariff_unit");
			
			String fcc_flg=request.getParameter("fcc_flg")==null?"":request.getParameter("fcc_flg");
			
			String change_request=request.getParameter("change_request")==null?"":request.getParameter("change_request");
			String cancel_dt=request.getParameter("cancel_dt")==null?"":request.getParameter("cancel_dt");
			String cancel_note=request.getParameter("cancel_note")==null?"":request.getParameter("cancel_note");
			String closure_req_flag = request.getParameter("closure_req_flag")==null?"":request.getParameter("closure_req_flag");
			String closure_alloc_qty = request.getParameter("closure_alloc_qty")==null?"":request.getParameter("closure_alloc_qty");
			
			if(change_request.equals("CLOSE"))
			{
				closure_req_flag="Y";
			}
			else if(change_request.equals("TERMINATE"))
			{
				closure_req_flag="R";
			}
			else if(change_request.equals("REOPEN"))
			{
				closure_req_flag="O";
			}
			if(!fcc_flg.equals("")) 
			{
				status = fcc_flg;
			}
			
			if(!fcc_flg.equals("") && cont_status_flg.equals("R"))
			{
				cont_status_flg=fcc_flg;
			}
			
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
				if(!ent_dt.equals(""))
				{
					String year = ent_dt.substring(8,ent_dt.length());
					
					/*
					 * if(contract_type.equals("G")) { year="4"+year; } else
					 * if(contract_type.equals("P")) { year="5"+year; }
					 */	
					
					int cont = Integer.parseInt(year) * 1000;
					query="SELECT NVL(MAX(CONT_NO),?) "
							+ "FROM FMS_LTCORA_CONT_MST "
							+ "WHERE CONT_NO LIKE ? AND CONTRACT_TYPE=? "
							+ "AND COMPANY_CD=? AND AGMT_TYPE=? AND BUY_SALE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setInt(1, cont);
					stmt.setString(2, year+"%");
					stmt.setString(3, contract_type);
					stmt.setString(4, comp_cd);
					stmt.setString(5, agreement_type);
					stmt.setString(6, buy_sale);
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
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(CONT_REV) "
							+ "FROM FMS_LTCORA_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, contract_type);
					stmt.setString(4, cont_no);
					stmt.setString(5, buy_sale);
					stmt.setString(6, agreement_type);
					stmt.setString(7, agmt_no);
					stmt.setString(8, agmt_rev_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						cont_rev_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						cont_rev_no = "0";
					}
					rset.close();
					stmt.close();
					status = "P";
					
				}
			}
			
			if(change_request.equals("TERMINATE")||change_request.equals("CLOSE"))
			{
				cont_rev_no=""+(Integer.parseInt(cont_rev_no)+1);
				cont_status_flg="P";
			}
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			String cont_name = counterparty_abbr+"-"+comp_abbr+"-LTCORA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;			
		
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") 
					&& !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals("") && !agreement_type.equals("") && !buy_sale.equals(""))
			{
				
				int rev_count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? "
						+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
				stmt.setString(7, cont_no);
				stmt.setString(8, cont_rev_no);
				stmt.setString(9, contract_type);
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
					
					query ="UPDATE FMS_LTCORA_CONT_MST SET CONT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'),SIGNING_TIME=?,"
							+ "DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?, "
							+ "BUYER_NOM=?,BUYER_MONTH_NOM=?,BUYER_WEEK_NOM=?,BUYER_DAILY_NOM=?, "
							+ "SELLER_NOM=?,SELLER_MONTH_NOM=?,SELLER_WEEK_NOM=?,SELLER_DAILY_NOM=?, "
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,MDCQ=?,MDCQ_PERCENTAGE=?,"
							+ "CONT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,CONT_STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),BUYER_FORNGT_NOM=?,"
							+ "SELLER_FORNGT_NOM=?,BUYER_NOM_CUTOFF=?,BUYER_NOM_CLAUSE=?,SELLER_NOM_CLAUSE=?,DAY_DEF_CLAUSE=?,"
							+ "MDCQ_CLAUSE=?,MEASUREMENT=?,MEAS_CLAUSE=?,MEAS_STANDARD=?,MEAS_TEMPERATURE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?,"
							+ "OFF_SPEC_GAS=?,SPEC_CLAUSE=?,SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,"
							+ "LIABILITY=?,LIAB_CLAUSE=?,BILLING_CLAUSE=?,"
							+ "TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?,"
							+ "NO_OF_CARGO=?,LTCORA_TARIFF=?,LTCORA_TARIFF_UNIT=?,SUG=?,TARIFF_DISCOUNT=?,VOL_DISCOUNT=?,"
							+ "EXTEND_STORAGE=?,DISCOUNT_DAYS=?,STORAGE_TARIFF=?,STORAGE_TARIFF_UNIT=?,FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE,ADV_ADJUST=?,TLU_FLAG=? ";
					if(change_request.equals("CANCEL"))
					{
						query+=",CLOSE_EFF_DT=TO_DATE(?,'DD/MM/YYYY'),CLOSURE_REMARK=? ";
					}
					if(change_request.equals("REOPEN"))
					{
						query+=",CLOSURE_REQUEST_FLAG=? ";
					}
					query+="WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND BUY_SALE=? "
							+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++st_count, cont_ref_no);
					stmt0.setString(++st_count, signing_dt);
					stmt0.setString(++st_count, signing_time);
					stmt0.setString(++st_count, dda_dt);
					stmt0.setString(++st_count, dda_time);
					stmt0.setString(++st_count, start_dt);
					stmt0.setString(++st_count, end_dt);
					stmt0.setString(++st_count, agreement_base);
					stmt0.setString(++st_count, buyer_nom);
					stmt0.setString(++st_count, buy_m);
					stmt0.setString(++st_count, buy_w);
					stmt0.setString(++st_count, buy_d);
					stmt0.setString(++st_count, seller_nom);
					stmt0.setString(++st_count, sel_m);
					stmt0.setString(++st_count, sel_w);
					stmt0.setString(++st_count, sel_d);
					stmt0.setString(++st_count, day_def);
					stmt0.setString(++st_count, day_time_from);
					stmt0.setString(++st_count, day_time_to);
					stmt0.setString(++st_count, mdcq_flag);
					stmt0.setString(++st_count, mdcq_percent);
					stmt0.setString(++st_count, cont_name);
					stmt0.setString(++st_count, emp_cd);
					//stmt0.setString(++st_count, status);
					stmt0.setString(++st_count, cont_status_flg);
					stmt0.setString(++st_count, billing_flag);
					stmt0.setString(++st_count, rev_eff_dt);
					stmt0.setString(++st_count, buy_f);
					stmt0.setString(++st_count, sel_f);
					stmt0.setString(++st_count, buy_nom_cutoff);
					stmt0.setString(++st_count, buy_clause_no);
					stmt0.setString(++st_count, sell_clause_no);
					stmt0.setString(++st_count, day_clause_no);
					stmt0.setString(++st_count, mdcq_clause_no);
					stmt0.setString(++st_count, measurementCheckbox);
					stmt0.setString(++st_count, measure_clause_no);
					stmt0.setString(++st_count, meas_standard);
					stmt0.setString(++st_count, meas_temperature);
					stmt0.setString(++st_count, pressure_min_bar);
					stmt0.setString(++st_count, pressure_max_bar);
					stmt0.setString(++st_count, off_spec_gas_checkbox);
					stmt0.setString(++st_count, spec_clause_no);
					stmt0.setString(++st_count, spec_gas_energy_base);
					stmt0.setString(++st_count, spec_gas_min_energy);
					stmt0.setString(++st_count, spec_gas_max_energy);
					stmt0.setString(++st_count, liability_checkbox);
					stmt0.setString(++st_count, liability_clause);
					stmt0.setString(++st_count, billing_clause_no);
					stmt0.setString(++st_count, terminator_checkbox);
					stmt0.setString(++st_count, terminate_clause_no);
					stmt0.setString(++st_count, terminate_planed);
					stmt0.setString(++st_count, terminate_force);
					stmt0.setString(++st_count, no_of_cargo);
					stmt0.setString(++st_count, ltcora_tariff);
					stmt0.setString(++st_count, ltcora_tariff_unit);
					stmt0.setString(++st_count, sug);
					stmt0.setString(++st_count, tariff_discount);
					stmt0.setString(++st_count, vol_discount);
					stmt0.setString(++st_count, extend_storage);
					stmt0.setString(++st_count, discount_days);
					stmt0.setString(++st_count, storage_tariff);
					stmt0.setString(++st_count, storage_tariff_unit);
					stmt0.setString(++st_count, fcc_flg);
					stmt0.setString(++st_count, emp_cd);
					stmt0.setString(++st_count, adv_adjust);
					stmt0.setString(++st_count, tlu_flag);
					
					if(change_request.equals("CANCEL"))
					{
						stmt0.setString(++st_count, cancel_dt);
						stmt0.setString(++st_count, cancel_note);
					}
					if(change_request.equals("REOPEN"))
					{
						stmt0.setString(++st_count, closure_req_flag);
					}
					stmt0.setString(++st_count, comp_cd);
					stmt0.setString(++st_count, counterparty_cd);
					stmt0.setString(++st_count, agmt_no);
					stmt0.setString(++st_count, agmt_rev_no);
					stmt0.setString(++st_count, agreement_type);
					stmt0.setString(++st_count, buy_sale);
					stmt0.setString(++st_count, cont_no);
					stmt0.setString(++st_count, cont_rev_no);
					stmt0.setString(++st_count, contract_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int st_count=0;
					query = "INSERT INTO FMS_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,"
							+ "START_DT,END_DT,AGMT_BASE,CONT_STATUS, "
							+ "BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
							+ "ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
							+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,"
							+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
							+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
							+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
							+ "NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,"
							+ "EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT,ADV_ADJUST,TLU_FLAG ";
					if(change_request.equals("TERMINATE")||change_request.equals("CLOSE"))
					{
						query+=",CLOSURE_REQUEST_FLAG ";
					}
					query+= ") "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,"
							+ "SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,"
							+ "?,?,?,"
							+ "?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,? ";
					if(change_request.equals("TERMINATE")||change_request.equals("CLOSE"))
					{
						query+=",?";
					}
					query+= ")";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++st_count, comp_cd);
					stmt0.setString(++st_count, counterparty_cd);
					stmt0.setString(++st_count, agreement_type);
					stmt0.setString(++st_count, agmt_no);
					stmt0.setString(++st_count, agmt_rev_no);
					stmt0.setString(++st_count, cont_no);
					stmt0.setString(++st_count, cont_rev_no);
					stmt0.setString(++st_count, contract_type);
					stmt0.setString(++st_count, cont_ref_no);
					stmt0.setString(++st_count, signing_dt);
					stmt0.setString(++st_count, signing_time);
					stmt0.setString(++st_count, dda_dt);
					stmt0.setString(++st_count, dda_time);
					stmt0.setString(++st_count, start_dt);
					stmt0.setString(++st_count, end_dt);
					stmt0.setString(++st_count, agreement_base);
					//stmt0.setString(++st_count, status);
					stmt0.setString(++st_count, cont_status_flg);
					stmt0.setString(++st_count, buyer_nom);
					stmt0.setString(++st_count, buy_m);
					stmt0.setString(++st_count, buy_w);
					stmt0.setString(++st_count, buy_d);
					stmt0.setString(++st_count, seller_nom);
					stmt0.setString(++st_count, sel_m);
					stmt0.setString(++st_count, sel_w);
					stmt0.setString(++st_count, sel_d);
					stmt0.setString(++st_count, day_def);
					stmt0.setString(++st_count, day_time_from);
					stmt0.setString(++st_count, day_time_to);
					stmt0.setString(++st_count, mdcq_flag);
					stmt0.setString(++st_count, mdcq_percent);
					stmt0.setString(++st_count, emp_cd);
					stmt0.setString(++st_count, cont_name);
					stmt0.setString(++st_count, billing_flag);
					stmt0.setString(++st_count, rev_eff_dt);
					stmt0.setString(++st_count, buy_f);
					stmt0.setString(++st_count, sel_f);
					stmt0.setString(++st_count, buy_nom_cutoff);
					stmt0.setString(++st_count, buy_sale);
					stmt0.setString(++st_count, buy_clause_no);
					stmt0.setString(++st_count, sell_clause_no);
					stmt0.setString(++st_count, day_clause_no);
					stmt0.setString(++st_count, mdcq_clause_no);
					stmt0.setString(++st_count, measurementCheckbox);
					stmt0.setString(++st_count, measure_clause_no);
					stmt0.setString(++st_count, meas_standard);
					stmt0.setString(++st_count, meas_temperature);
					stmt0.setString(++st_count, pressure_min_bar);
					stmt0.setString(++st_count, pressure_max_bar);
					stmt0.setString(++st_count, off_spec_gas_checkbox);
					stmt0.setString(++st_count, spec_clause_no);
					stmt0.setString(++st_count, spec_gas_energy_base);
					stmt0.setString(++st_count, spec_gas_min_energy);
					stmt0.setString(++st_count, spec_gas_max_energy);
					stmt0.setString(++st_count, liability_checkbox);
					stmt0.setString(++st_count, liability_clause);
					stmt0.setString(++st_count, billing_clause_no);
					stmt0.setString(++st_count, terminator_checkbox);
					stmt0.setString(++st_count, terminate_clause_no);
					stmt0.setString(++st_count, terminate_planed);
					stmt0.setString(++st_count, terminate_force);
					stmt0.setString(++st_count, no_of_cargo);
					stmt0.setString(++st_count, ltcora_tariff);
					stmt0.setString(++st_count, ltcora_tariff_unit);
					stmt0.setString(++st_count, sug);
					stmt0.setString(++st_count, tariff_discount);
					stmt0.setString(++st_count, vol_discount);
					stmt0.setString(++st_count, extend_storage);
					stmt0.setString(++st_count, discount_days);
					stmt0.setString(++st_count, storage_tariff);
					stmt0.setString(++st_count, storage_tariff_unit);
					stmt0.setString(++st_count, adv_adjust);					
					stmt0.setString(++st_count, tlu_flag);					
					if(change_request.equals("CLOSE"))
					{
						stmt0.setString(++st_count,closure_req_flag);
					}
					else if(change_request.equals("TERMINATE"))
					{
						stmt0.setString(++st_count,closure_req_flag);
					}
					stmt0.executeUpdate();
					
					stmt0.close();
					
					if(liability_checkbox.equals("Y") && !rev_chk.equals("Y"))
					{
						query1="INSERT INTO FMS_LTCORA_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT,BUY_SALE) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,?,?,?,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,SYSDATE,?,REV_DT,BUY_SALE "
								+ "FROM FMS_LTCORA_AGMT_LIABILITY A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? "
								+ "AND A.AGMT_TYPE=? AND A.BUY_SALE=? AND EXISTS("
								+ "SELECT * "
								+ "FROM FMS_LTCORA_AGMT_LIABILITY B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE "
								+ ")";
						
						stmt0 = dbcon.prepareStatement(query1);
						stmt0.setString(1, cont_no);
						stmt0.setString(2, cont_rev_no);
						stmt0.setString(3, contract_type);
						stmt0.setString(4, emp_cd);
						stmt0.setString(5, comp_cd);
						stmt0.setString(6, counterparty_cd);
						stmt0.setString(7, agmt_no);
						stmt0.setString(8, agmt_rev_no);
						stmt0.setString(9, agreement_type);
						stmt0.setString(10, buy_sale);
						stmt0.executeUpdate();
						
						stmt0.close();
					}
					
				}
				
				int count=0;
				//TRADER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? "
						+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agreement_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				stmt1.setString(6, buy_sale);
				stmt1.setString(7, cont_no);
				stmt1.setString(8, cont_rev_no);
				stmt1.setString(9, contract_type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					 count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_LTCORA_CONT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? "
							+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agreement_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.setString(6, buy_sale);
					stmt2.setString(7, cont_no);
					stmt2.setString(8, cont_rev_no);
					stmt2.setString(9, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_LTCORA_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "PLANT_SEQ_NO,ENT_BY, ENT_DT,BUY_SALE) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agreement_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, cont_rev_no);
						stmt2.setString(8, contract_type);
						stmt2.setString(9, chk_plant[i]);
						stmt2.setString(10, emp_cd);
						stmt2.setString(11, buy_sale);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						String temp_chk_plant[]=tmp_chk_plant.split("@");
						for(int m=0; m<temp_chk_plant.length; m++)
						{
							if(!temp_chk_plant[m].equals(chk_plant[i]) && !chk_plant[i].equals(""))
							{
								updateContractBillingPlant(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_plant[m], chk_plant[i], chk_plant.length, buy_sale, agreement_type);
							}
						}
					 }
				}
				
				//TRANSPORTER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
				stmt2 = dbcon.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, cont_no);
				stmt2.setString(4, cont_rev_no);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, agmt_rev_no);
				stmt2.setString(7, contract_type);
				stmt2.setString(8, buy_sale);
				stmt2.setString(9, "A");
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
					query = "DELETE FROM FMS_LTCORA_CONT_TRANSPTR "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
					stmt3 = dbcon.prepareStatement(query);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, cont_rev_no);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmt_rev_no);
					stmt3.setString(7, contract_type);
					stmt3.setString(8, buy_sale);
					stmt3.setString(9, "A");
					stmt3.executeUpdate();
					
					stmt3.close();
				}
				if(trans_cd!=null)
				{
					for(int i=0;i<trans_cd.length;i++)
					{
						query = "INSERT INTO FMS_LTCORA_CONT_TRANSPTR(COMPANY_CD, COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?) ";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, buy_sale);
						stmt3.setString(4, agmt_no);
						stmt3.setString(5, agmt_rev_no);
						stmt3.setString(6, cont_no);
						stmt3.setString(7, cont_rev_no);
						stmt3.setString(8, contract_type);
						stmt3.setString(9, trans_cd[i]);
						stmt3.setString(10, trans_plant_seq_no[i]);
						stmt3.setString(11, emp_cd);
						stmt3.setString(12, "A");
						stmt3.executeUpdate();
						
						stmt3.close();
					}
				}
				
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? "
						+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agreement_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				stmt_temp.setString(6, buy_sale);
				stmt_temp.setString(7, cont_no);
				stmt_temp.setString(8, cont_rev_no);
				stmt_temp.setString(9, contract_type);
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
					query = "DELETE FROM FMS_LTCORA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? "
							+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agreement_type);
					stmt2.setString(4, agmt_no);
					stmt2.setString(5, agmt_rev_no);
					stmt2.setString(6, buy_sale);
					stmt2.setString(7, cont_no);
					stmt2.setString(8, cont_rev_no);
					stmt2.setString(9, contract_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_LTCORA_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "PLANT_SEQ_NO,ENT_BY, ENT_DT,BUY_SALE)"
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agreement_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, cont_rev_no);
						stmt2.setString(8, contract_type);
						stmt2.setString(9, chk_bu_plant[i]);
						stmt2.setString(10, emp_cd);
						stmt2.setString(11, buy_sale);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				String old_rev_no = ""+(Integer.parseInt(cont_rev_no)-1);
				
				query="INSERT INTO FMS_LTCORA_CONT_STRG_CRG(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_TYPE,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_TYPE,AGMT_REV,CONT_NO,?,CONTRACT_TYPE,"
						+ "SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_DT,ENT_BY,SYSDATE,? "
						+ "FROM FMS_LTCORA_CONT_STRG_CRG A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
						+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=? AND A.BUY_SALE=? AND NOT EXISTS("
						+ "SELECT * "
						+ "FROM FMS_LTCORA_CONT_STRG_CRG B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND CONT_REV=? AND A.BUY_SALE=B.BUY_SALE "
						+ ")";
				stmt4 = dbcon.prepareStatement(query);
				stmt4.setString(1, cont_rev_no);
				stmt4.setString(2, emp_cd);
				stmt4.setString(3, comp_cd);
				stmt4.setString(4, counterparty_cd);
				stmt4.setString(5, agreement_type);
				stmt4.setString(6, agmt_no);
				stmt4.setString(7, agmt_rev_no);
				stmt4.setString(8, cont_no);
				stmt4.setString(9, contract_type);
				stmt4.setString(10, old_rev_no);
				stmt4.setString(11, buy_sale);
				stmt4.setString(12, cont_rev_no);
				stmt4.executeUpdate();
				stmt4.close();
				
				if(tlu_flag.equals("Y"))
				{
					if(dlng_chk_plant!=null)
					{
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
						
						for(int i=0;i<dlng_chk_plant.length;i++)
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
							stmt3.setString(8, dlng_chk_plant[i]);
							stmt3.setString(9, emp_cd);
							stmt3.executeUpdate();
							stmt3.close();
							
							//DLNG-LTCORA Charges (TLU Services)
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
												+ "FROM FMS_LTCORA_CONT_PLANT_CHRG "
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
										stmt.setString(7, dlng_chk_plant[i]);
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
											query="UPDATE FMS_LTCORA_CONT_PLANT_CHRG SET CHARGE_RATE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
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
											stmt.setString(9, dlng_chk_plant[i]);
											stmt.setString(10, effDt);
											stmt.setString(11, chrgAbbr);
											stmt.executeUpdate();
											stmt.close();
										}
										else
										{
											query2 = "INSERT INTO FMS_LTCORA_CONT_PLANT_CHRG(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
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
											stmt2.setString(8, dlng_chk_plant[i]);
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
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New LTCORA Contract "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - LTCORA Contract "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
				
				int st_count=0;
				int log_seq_no=0;
				
				String query="SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_LTCORA_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, cont_no);
				stmt.setString(5, buy_sale);
				stmt.setString(6, agreement_type);
				stmt.setString(7, agmt_no);
				stmt.setString(8, agmt_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					log_seq_no = (rset.getInt(1)+1);
				}
				else
				{
					log_seq_no = 0;
				}
				rset.close();
				stmt.close();
				query1 = "INSERT INTO LOG_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,LOG_SEQ_NO, "
						+ "CONTRACT_TYPE,CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,START_DT,END_DT,AGMT_BASE,CONT_STATUS,BUYER_NOM, "
						+ "BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF, "
						+ "DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM, "
						+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,MEASUREMENT, "
						+ "MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE, "
						+ "SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE, "
						+ "TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT, "
						+ "EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT,LOG_BY,LOG_DT,FCC_FLAG,FCC_BY,FCC_DATE, "
						+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,MODIFY_BY,MODIFY_DT,ADV_ADJUST,TLU_FLAG) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,?,CONTRACT_TYPE,CONT_REF_NO, "
						+ "SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,START_DT,END_DT,AGMT_BASE,CONT_STATUS,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM, "
						+ "BUYER_DAILY_NOM,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ, "
						+ "MDCQ_PERCENTAGE,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE, "
						+ "BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE, "
						+ "PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY, "
						+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO, "
						+ "LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF, "
						+ "STORAGE_TARIFF_UNIT,?,SYSDATE,FCC_FLAG,FCC_BY,FCC_DATE,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT, "
						+ "CLOSURE_REMARK,MODIFY_BY,MODIFY_DT,ADV_ADJUST,TLU_FLAG  "
						+ "FROM FMS_LTCORA_CONT_MST A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
						+ "AND BUY_SALE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=?";
				stmt0 = dbcon.prepareStatement(query1);
				stmt0.setString(++st_count, ""+log_seq_no);
				stmt0.setString(++st_count, emp_cd);
				stmt0.setString(++st_count, comp_cd);
				stmt0.setString(++st_count, counterparty_cd);
				stmt0.setString(++st_count, agmt_no);
				stmt0.setString(++st_count, agmt_rev_no);
				stmt0.setString(++st_count, agreement_type);
				stmt0.setString(++st_count, buy_sale);
				stmt0.setString(++st_count, cont_no);
				stmt0.setString(++st_count, contract_type);
				stmt0.setString(++st_count,cont_rev_no);
				stmt0.executeUpdate();
				stmt0.close();
				
				/*query1 = "INSERT INTO LOG_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,LOG_SEQ_NO,"
						+ "CONTRACT_TYPE,"
						+ "CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,"
						+ "START_DT,END_DT,AGMT_BASE,CONT_STATUS, "
						+ "BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
						+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
						+ "ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,"
						+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,"
						+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
						+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
						+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
						+ "NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,"
						+ "EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT,LOG_BY,LOG_DT,FCC_FLAG,FCC_BY,FCC_DATE,"
						+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,"
						+ "?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
						+ "?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,"
						+ "SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,"
						+ "?,?,?,?,?,"
						+ "?,?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,?,?,"
						+ "?,?,?,?,?,SYSDATE,?,?,SYSDATE, "
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
				stmt0 = dbcon.prepareStatement(query1);
				stmt0.setString(++st_count, comp_cd);
				stmt0.setString(++st_count, counterparty_cd);
				stmt0.setString(++st_count, agreement_type);
				stmt0.setString(++st_count, agmt_no);
				stmt0.setString(++st_count, agmt_rev_no);
				stmt0.setString(++st_count, cont_no);
				stmt0.setString(++st_count, cont_rev_no);
				stmt0.setString(++st_count, ""+log_seq_no);
				stmt0.setString(++st_count, contract_type);
				stmt0.setString(++st_count, cont_ref_no);
				stmt0.setString(++st_count, signing_dt);
				stmt0.setString(++st_count, signing_time);
				stmt0.setString(++st_count, dda_dt);
				stmt0.setString(++st_count, dda_time);
				stmt0.setString(++st_count, start_dt);
				stmt0.setString(++st_count, end_dt);
				stmt0.setString(++st_count, agreement_base);
				//stmt0.setString(++st_count, status);
				stmt0.setString(++st_count, cont_status_flg);
				stmt0.setString(++st_count, buyer_nom);
				stmt0.setString(++st_count, buy_m);
				stmt0.setString(++st_count, buy_w);
				stmt0.setString(++st_count, buy_d);
				stmt0.setString(++st_count, seller_nom);
				stmt0.setString(++st_count, sel_m);
				stmt0.setString(++st_count, sel_w);
				stmt0.setString(++st_count, sel_d);
				stmt0.setString(++st_count, day_def);
				stmt0.setString(++st_count, day_time_from);
				stmt0.setString(++st_count, day_time_to);
				stmt0.setString(++st_count, mdcq_flag);
				stmt0.setString(++st_count, mdcq_percent);
				stmt0.setString(++st_count, emp_cd);
				stmt0.setString(++st_count, cont_name);
				stmt0.setString(++st_count, billing_flag);
				stmt0.setString(++st_count, rev_eff_dt);
				stmt0.setString(++st_count, buy_f);
				stmt0.setString(++st_count, sel_f);
				stmt0.setString(++st_count, buy_nom_cutoff);
				stmt0.setString(++st_count, buy_sale);
				stmt0.setString(++st_count, buy_clause_no);
				stmt0.setString(++st_count, sell_clause_no);
				stmt0.setString(++st_count, day_clause_no);
				stmt0.setString(++st_count, mdcq_clause_no);
				stmt0.setString(++st_count, measurementCheckbox);
				stmt0.setString(++st_count, measure_clause_no);
				stmt0.setString(++st_count, meas_standard);
				stmt0.setString(++st_count, meas_temperature);
				stmt0.setString(++st_count, pressure_min_bar);
				stmt0.setString(++st_count, pressure_max_bar);
				stmt0.setString(++st_count, off_spec_gas_checkbox);
				stmt0.setString(++st_count, spec_clause_no);
				stmt0.setString(++st_count, spec_gas_energy_base);
				stmt0.setString(++st_count, spec_gas_min_energy);
				stmt0.setString(++st_count, spec_gas_max_energy);
				stmt0.setString(++st_count, liability_checkbox);
				stmt0.setString(++st_count, liability_clause);
				stmt0.setString(++st_count, billing_clause_no);
				stmt0.setString(++st_count, terminator_checkbox);
				stmt0.setString(++st_count, terminate_clause_no);
				stmt0.setString(++st_count, terminate_planed);
				stmt0.setString(++st_count, terminate_force);
				stmt0.setString(++st_count, no_of_cargo);
				stmt0.setString(++st_count, ltcora_tariff);
				stmt0.setString(++st_count, ltcora_tariff_unit);
				stmt0.setString(++st_count, sug);
				stmt0.setString(++st_count, tariff_discount);
				stmt0.setString(++st_count, vol_discount);
				stmt0.setString(++st_count, extend_storage);
				stmt0.setString(++st_count, discount_days);
				stmt0.setString(++st_count, storage_tariff);
				stmt0.setString(++st_count, storage_tariff_unit);
				stmt0.setString(++st_count, emp_cd);
				stmt0.setString(++st_count, fcc_flg);
				stmt0.setString(++st_count, emp_cd);
				stmt0.setString(++st_count, closure_req_flag);
				stmt0.setString(++st_count, closure_alloc_qty);
				stmt0.setString(++st_count, cancel_dt);
				stmt0.setString(++st_count, cancel_note);
				stmt0.executeUpdate();
				
				stmt0.close();*/
			}
			else
			{
				msg = "Failed! - LTCORA Contract "+cont_name_map+" Modification for "+counterparty_abbr+" Failed!";
				msg_type="E";
			}
			
			if(buy_sale.equals("C"))
			{
				url = "../ltcora/frm_ltcora_cont_sell_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agreement_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&buy_sale="+buy_sale+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&contract_type="+contract_type+commonUrl_pra;

			}
			else if(buy_sale.equals("T"))
			{
				url = "../ltcora/frm_ltcora_cont_buy_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
						"&agreement_type="+agreement_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&buy_sale="+buy_sale+
						"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&contract_type="+contract_type+commonUrl_pra;
			}

			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - LTCORA Contract "+cont_name_map+" Insert/Update Failed!";
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
	
	private void InsertUpdateLtcoraCnBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraCnBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
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
			String exchg_val=request.getParameter("exchg_val")==null?"":request.getParameter("exchg_val");
			String exch_calc_base=request.getParameter("exch_calc_base")==null?"":request.getParameter("exch_calc_base");
			String inv_criteria=request.getParameter("inv_criteria")==null?"":request.getParameter("inv_criteria");
			String exchg_rate_note=request.getParameter("exchg_rate_note")==null?"":request.getParameter("exchg_rate_note");
			
			String due_dt_in=request.getParameter("due_dt_in")==null?"":request.getParameter("due_dt_in");
			String exclude_sat=request.getParameter("exclude_sat")==null?"N":request.getParameter("exclude_sat");
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
			
			String[] cust_plant_map = holidayState_map.split("@@");
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND AGMT_TYPE=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
						+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, buy_sale);
				stmt3.setString(4, cont_no);
				stmt3.setString(5, agmt_no);
				stmt3.setString(6, agreement_type);
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
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND BUY_SALE=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agreement_type);
					stmt.setString(5, buy_sale);
					stmt.setString(6, cont_no);
					stmt.setString(7, contract_type);
					stmt.setString(8, plant_seq);
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
						query="UPDATE FMS_LTCORA_CONT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_TYPE=? AND BUY_SALE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
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
						stmt1.setString(++cnt, billing_days);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, buy_sale);
						stmt1.setString(++cnt, cont_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" LTCORA CN/Period Billing Details for "+counterparty_abbr+" Modified Successfully!";
						msg_type = "S";
					}
					else
					{
						int insCount=0;
						
						query="INSERT INTO FMS_LTCORA_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BUY_SALE,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?)";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++insCount, comp_cd);
						stmt1.setString(++insCount, counterparty_cd);
						stmt1.setString(++insCount, agmt_no);
						stmt1.setString(++insCount, agmt_rev_no);
						stmt1.setString(++insCount, agreement_type);
						stmt1.setString(++insCount, cont_no);
						stmt1.setString(++insCount, cont_rev_no);
						stmt1.setString(++insCount, contract_type);
						stmt1.setString(++insCount, freq);
						stmt1.setString(++insCount, billing_flag);
						stmt1.setString(++insCount, due_date);
						stmt1.setString(++insCount, sec_due_date);
						stmt1.setString(++insCount, inv_currency);
						stmt1.setString(++insCount, payment_currency);
						stmt1.setString(++insCount, rate);
						stmt1.setString(++insCount, plusmin);
						stmt1.setString(++insCount, modeper);
						stmt1.setString(++insCount, exchng_rate);
						stmt1.setString(++insCount, exch_calc_base);
						stmt1.setString(++insCount, inv_criteria);
						stmt1.setString(++insCount, exchg_rate_note);
						stmt1.setString(++insCount, emp_cd);
						stmt1.setString(++insCount, due_dt_in);
						stmt1.setString(++insCount, exclude_sat);
						stmt1.setString(++insCount, exchg_val);
						stmt1.setString(++insCount, buy_sale);
						stmt1.setString(++insCount, billing_days);
						stmt1.setString(++insCount, exclude_sat_map);
						stmt1.setString(++insCount, plant_seq);
						stmt1.setString(++insCount, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" LTCORA CN/Period Billing Details for "+counterparty_abbr+" Inserted Successfully!";
						msg_type = "S";
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" LTCORA CN/Period Billing Details for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../ltcora/frm_ltcora_cont_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&buy_sale="+buy_sale+"&agreement_type="+agreement_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! LTCORA CN/Period Billing Details Insert/Update Failed";
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
	
	private void InsertUpdateLtcoraCargoCnDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraCargoCnDetail()";
		
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
			String sug_per = request.getParameter("sug_per")==null?"0.00":request.getParameter("sug_per");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			
			String no_cargo = request.getParameter("no_cargo")==null?"0":request.getParameter("no_cargo");
			
			String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			
			String[] cargo_number = request.getParameterValues("cargo_number");
			String[] vessel_cd = request.getParameterValues("vessel_cd");
			String[] cargo_no = request.getParameterValues("cargo_no");
			String[] supplier_cd = request.getParameterValues("supplier_cd");
			String[] window_start_dt = request.getParameterValues("window_start_dt");
			String[] window_end_dt = request.getParameterValues("window_end_dt");
			String[] actual_recpt_dt = request.getParameterValues("actual_recpt_dt");
			String[] edq_qty = request.getParameterValues("edq_qty");
			String[] adq_qty = request.getParameterValues("adq_qty");
			String[] supp_qty = request.getParameterValues("supp_qty");
			String[] csoc_qty = request.getParameterValues("csoc_qty");
			String[] boe_qty = request.getParameterValues("boe_qty");
			String[] attach_lng_cargo = request.getParameterValues("attach_cargo_mapping");
			
			String[] boe_no = request.getParameterValues("boe_no");
			String[] boe_dt = request.getParameterValues("boe_dt");
			String[] qq_no = request.getParameterValues("qq_no");
			String[] qq_dt = request.getParameterValues("qq_dt");
			
			String[] storage_days = request.getParameterValues("storage_days");
			String[] storage_ext_days = request.getParameterValues("storage_ext_days");
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, "");
			
			if(counterparty_cd!=null)
			{
				String cargo_msg="";
				for(int i=0; i<cargo_number.length;i++)
				{
					int selCnt=0;
					
					if(!cargo_number[i].equals("")) 
					{
						//System.out.println("cargo_number[i]==="+cargo_number[i]);
						
						query1="SELECT COUNT(*) "
								+ "FROM FMS_LTCORA_CONT_CARGO_DTL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND BUY_SALE=? AND AGMT_TYPE=?"
								+ "AND AGMT_NO=? AND AGMT_REV=?"
								+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
								+ "AND CONT_REV=? AND CARGO_NO=?";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, counterparty_cd);
						stmt1.setString(++selCnt, buy_sell);
						stmt1.setString(++selCnt, agmt_type);
						stmt1.setString(++selCnt, agmt_no);
						stmt1.setString(++selCnt, agmt_rev);
						stmt1.setString(++selCnt, contract_type);
						stmt1.setString(++selCnt, cont_no);
						stmt1.setString(++selCnt, cont_rev);
						stmt1.setString(++selCnt, cargo_no[i]);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							int count=rset1.getInt(1);
							
							if(count>0)
							{
								int st_count=0;
								
								query ="UPDATE FMS_LTCORA_CONT_CARGO_DTL SET SHIP_CD=?,SUPP_CD=?,EDQ_FROM_DT=TO_DATE(?,'DD/MM/YYYY'),EDQ_TO_DT=TO_DATE(?,'DD/MM/YYYY'), "
										+ "ACTUAL_RECPT_DT=TO_DATE(?,'DD/MM/YYYY'),EDQ_QTY=?,CSOC_QTY=?,BOE_QTY=?,BOE_NO=?,BOE_DT=TO_DATE(?,'DD/MM/YYYY'),"
										+ "QQ_NO=?,QQ_DT=TO_DATE(?,'DD/MM/YYYY'),"
										+ "STORAGE_DAYS=?,STORAGE_EXT_DAYS=?,"
										+ "MODIFY_DT=SYSDATE,MODIFY_BY=?,CARGO_REF=?";
								if("T".equals(buy_sell)) 
								{
									query	+= ",ATTACH_LNG_CARGO=? ";
								}
								query	+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND BUY_SALE=? AND AGMT_TYPE=?"
										+ "AND AGMT_NO=? AND AGMT_REV=?"
										+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
										+ "AND CONT_REV=? AND CARGO_NO=?";
								stmt0 = dbcon.prepareStatement(query);
								stmt0.setString(++st_count, vessel_cd[i]);
								stmt0.setString(++st_count, supplier_cd[i]);
								stmt0.setString(++st_count, window_start_dt[i]);
								stmt0.setString(++st_count, window_end_dt[i]);
								stmt0.setString(++st_count, actual_recpt_dt[i]);
								stmt0.setString(++st_count, edq_qty[i]);
								stmt0.setString(++st_count, csoc_qty[i]);
								stmt0.setString(++st_count, boe_qty[i]);
								stmt0.setString(++st_count, boe_no[i]);
								stmt0.setString(++st_count, boe_dt[i]);
								stmt0.setString(++st_count, qq_no[i]);
								stmt0.setString(++st_count, qq_dt[i]);
								stmt0.setString(++st_count, storage_days[i]);
								stmt0.setString(++st_count, storage_ext_days[i]);
								stmt0.setString(++st_count, emp_cd);
								stmt0.setString(++st_count, cargo_number[i]);
								if("T".equals(buy_sell)) 
								{
									stmt0.setString(++st_count, attach_lng_cargo[i]);
								}
								stmt0.setString(++st_count, comp_cd);
								stmt0.setString(++st_count, counterparty_cd);
								stmt0.setString(++st_count, buy_sell);
								stmt0.setString(++st_count, agmt_type);
								stmt0.setString(++st_count, agmt_no);
								stmt0.setString(++st_count, agmt_rev);
								stmt0.setString(++st_count, contract_type);
								stmt0.setString(++st_count, cont_no);
								stmt0.setString(++st_count, cont_rev);
								stmt0.setString(++st_count, cargo_no[i]);
								stmt0.executeUpdate();
								
								stmt0.close();
								cargo_msg +=" "+cargo_no[i];
								msg = "Successful! - LTCORA "+cont_name_map+" Cargo/s ("+cargo_msg+")Details for "+counterparty_abbr+" Submitted Successfully!";
								msg_type="S";
							}
							else 
							{
								int insCnt=0;
								
								query = "INSERT INTO FMS_LTCORA_CONT_CARGO_DTL (COMPANY_CD,"
										+ "COUNTERPARTY_CD,"
										+ "BUY_SALE,"
										+ "AGMT_TYPE,"
										+ "AGMT_NO,"
										+ "AGMT_REV,"
										+ "CONTRACT_TYPE,"
										+ "CONT_NO,"
										+ "CONT_REV,"
										+ "CARGO_NO,"
										+ "CARGO_REF,"
										+ "SHIP_CD,"
										+ "SUPP_CD,"
										+ "EDQ_FROM_DT,"
										+ "EDQ_TO_DT,"
										+ "ACTUAL_RECPT_DT,"
										+ "EDQ_QTY,"
										+ "CSOC_QTY,"
										+ "BOE_QTY,"
										+ "BOE_NO,"
										+ "BOE_DT,"
										+ "QQ_NO,"
										+ "QQ_DT,"
										+ "STORAGE_DAYS,"
										+ "STORAGE_EXT_DAYS,"
										+ "ENT_BY,"
										+ "ENT_DT,"
										+ "CARGO_STATUS";
								if("T".equals(buy_sell))
								{
									query	+= ",ATTACH_LNG_CARGO";
								}
								query	+= ")"
										+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
										+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
										+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?";
								if("T".equals(buy_sell))
								{
									query	+=  ",?";
								}
								query	+= ")";
								stmt2 = dbcon.prepareStatement(query);
								stmt2.setString(++insCnt, comp_cd);
								stmt2.setString(++insCnt, counterparty_cd);
								stmt2.setString(++insCnt, buy_sell);
								stmt2.setString(++insCnt, agmt_type);
								stmt2.setString(++insCnt, agmt_no);
								stmt2.setString(++insCnt, agmt_rev);
								stmt2.setString(++insCnt, contract_type);
								stmt2.setString(++insCnt, cont_no);
								stmt2.setString(++insCnt, cont_rev);
								stmt2.setString(++insCnt, cargo_no[i]);
								stmt2.setString(++insCnt, cargo_number[i]);
								stmt2.setString(++insCnt, vessel_cd[i]);
								stmt2.setString(++insCnt, supplier_cd[i]);
								stmt2.setString(++insCnt, window_start_dt[i]);
								stmt2.setString(++insCnt, window_end_dt[i]);
								stmt2.setString(++insCnt, actual_recpt_dt[i]);
								stmt2.setString(++insCnt, edq_qty[i]);
								stmt2.setString(++insCnt, csoc_qty[i]);
								stmt2.setString(++insCnt, boe_qty[i]);
								stmt2.setString(++insCnt, boe_no[i]);
								stmt2.setString(++insCnt, boe_dt[i]);
								stmt2.setString(++insCnt, qq_no[i]);
								stmt2.setString(++insCnt, qq_dt[i]);
								stmt2.setString(++insCnt, storage_days[i]);
								stmt2.setString(++insCnt, storage_ext_days[i]);
								stmt2.setString(++insCnt, emp_cd);
								stmt2.setString(++insCnt, "Y");
								if("T".equals(buy_sell)) 
								{
									stmt2.setString(++insCnt, attach_lng_cargo[i]);
								}
								stmt2.executeUpdate();
								
								stmt2.close();
								cargo_msg +=" "+cargo_no[i];
								msg = "Successful! - LTCORA "+cont_name_map+" Cargo/s ("+cargo_msg+") Details for "+counterparty_abbr+"  Submitted Successfully!";
								msg_type="S";
							}
						}
						rset1.close();
						stmt1.close();
						
						if(!adq_qty[i].equals("") && !adq_qty[i].isEmpty())
						{
							Integer selCntInt=0;
							query1="SELECT COUNT(*) "
									+ "FROM FMS_LTCORA_CONT_CARGO_ADQ "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND BUY_SALE=? AND AGMT_TYPE=?"
									+ "AND AGMT_NO=? AND AGMT_REV=?"
									+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
									+ "AND CONT_REV=? AND CARGO_NO=? ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(++selCntInt, comp_cd);
							stmt1.setString(++selCntInt, counterparty_cd);
							stmt1.setString(++selCntInt, buy_sell);
							stmt1.setString(++selCntInt, agmt_type);
							stmt1.setString(++selCntInt, agmt_no);
							stmt1.setString(++selCntInt, agmt_rev);
							stmt1.setString(++selCntInt, contract_type);
							stmt1.setString(++selCntInt, cont_no);
							stmt1.setString(++selCntInt, cont_rev);
							stmt1.setString(++selCntInt, cargo_no[i]);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								int count=rset1.getInt(1);
								
								int daysToAdd =0;

								daysToAdd = utilDate.getDays(window_end_dt[i], window_start_dt[i]);

								if(count<=0)
								{
									for (int l = 0; l < daysToAdd; l++) 
									{
										int insCnt=0;
										
										String adqDt=  utilDate.getDate(window_start_dt[i], ""+l);
										
										String adqQty = (adqDt.equals(actual_recpt_dt[i])) ? adq_qty[i] : "";
										
										query = "INSERT INTO FMS_LTCORA_CONT_CARGO_ADQ (COMPANY_CD,"
												+ "COUNTERPARTY_CD,"
												+ "BUY_SALE,"
												+ "AGMT_TYPE,"
												+ "AGMT_NO,"
												+ "AGMT_REV,"
												+ "CONTRACT_TYPE,"
												+ "CONT_NO,"
												+ "CONT_REV,"
												+ "CARGO_NO,"
												+ "ADQ_DT,"
												+ "ADQ_QTY,"
												+ "ENT_BY,"
												+ "ENT_DT)"
												+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
												+ "TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE)";
										stmt2 = dbcon.prepareStatement(query);
										stmt2.setString(++insCnt, comp_cd);
										stmt2.setString(++insCnt, counterparty_cd);
										stmt2.setString(++insCnt, buy_sell);
										stmt2.setString(++insCnt, agmt_type);
										stmt2.setString(++insCnt, agmt_no);
										stmt2.setString(++insCnt, agmt_rev);
										stmt2.setString(++insCnt, contract_type);
										stmt2.setString(++insCnt, cont_no);
										stmt2.setString(++insCnt, cont_rev);
										stmt2.setString(++insCnt, cargo_no[i]);
										stmt2.setString(++insCnt, adqDt);
										stmt2.setString(++insCnt, adqQty);
										stmt2.setString(++insCnt, emp_cd);
										stmt2.executeUpdate();
										
										stmt2.close();
									}
								}
								else
								{
									int st_count=0;
									int insCnt=0;
									
									query ="DELETE FROM FMS_LTCORA_CONT_CARGO_ADQ "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND BUY_SALE=? AND AGMT_TYPE=?"
											+ "AND AGMT_NO=? AND AGMT_REV=?"
											+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
											+ "AND CONT_REV=? AND CARGO_NO=? ";
									stmt0 = dbcon.prepareStatement(query);
									stmt0.setString(++st_count, comp_cd);
									stmt0.setString(++st_count, counterparty_cd);
									stmt0.setString(++st_count, buy_sell);
									stmt0.setString(++st_count, agmt_type);
									stmt0.setString(++st_count, agmt_no);
									stmt0.setString(++st_count, agmt_rev);
									stmt0.setString(++st_count, contract_type);
									stmt0.setString(++st_count, cont_no);
									stmt0.setString(++st_count, cont_rev);
									stmt0.setString(++st_count, cargo_no[i]);
									stmt0.executeUpdate();
									stmt0.close();
									
									for (int l = 0; l < daysToAdd; l++) 
									{
										int insCnt1=0;
										
										String adqDt=  utilDate.getDate(window_start_dt[i], ""+l);
										
										String adqQty = (adqDt.equals(actual_recpt_dt[i])) ? adq_qty[i] : "";
										
										query1 = "INSERT INTO FMS_LTCORA_CONT_CARGO_ADQ (COMPANY_CD,"
												+ "COUNTERPARTY_CD,"
												+ "BUY_SALE,"
												+ "AGMT_TYPE,"
												+ "AGMT_NO,"
												+ "AGMT_REV,"
												+ "CONTRACT_TYPE,"
												+ "CONT_NO,"
												+ "CONT_REV,"
												+ "CARGO_NO,"
												+ "ADQ_DT,"
												+ "ADQ_QTY,"
												+ "ENT_BY,"
												+ "ENT_DT)"
												+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
												+ "TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE)";
										stmt2 = dbcon.prepareStatement(query1);
										stmt2.setString(++insCnt1, comp_cd);
										stmt2.setString(++insCnt1, counterparty_cd);
										stmt2.setString(++insCnt1, buy_sell);
										stmt2.setString(++insCnt1, agmt_type);
										stmt2.setString(++insCnt1, agmt_no);
										stmt2.setString(++insCnt1, agmt_rev);
										stmt2.setString(++insCnt1, contract_type);
										stmt2.setString(++insCnt1, cont_no);
										stmt2.setString(++insCnt1, cont_rev);
										stmt2.setString(++insCnt1, cargo_no[i]);
										stmt2.setString(++insCnt1, adqDt);
										stmt2.setString(++insCnt1, adqQty);
										stmt2.setString(++insCnt1, emp_cd);
										stmt2.executeUpdate();
										
										stmt2.close();
									}
								}
							}
							rset1.close();
							stmt1.close();
						}
					}
					
					int insCnt=0;
					int selCnt1=0;
					int log_seq_no=0;
					
					String query1="SELECT MAX(LOG_SEQ_NO) "
							+ "FROM LOG_LTCORA_CONT_CARGO_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND BUY_SALE=? AND AGMT_TYPE=?"
							+ "AND AGMT_NO=? AND AGMT_REV=?"
							+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
							+ "AND CONT_REV=? AND CARGO_NO=?";
					stmt1 = dbcon.prepareStatement(query1);
					stmt1.setString(++selCnt1, comp_cd);
					stmt1.setString(++selCnt1, counterparty_cd);
					stmt1.setString(++selCnt1, buy_sell);
					stmt1.setString(++selCnt1, agmt_type);
					stmt1.setString(++selCnt1, agmt_no);
					stmt1.setString(++selCnt1, agmt_rev);
					stmt1.setString(++selCnt1, contract_type);
					stmt1.setString(++selCnt1, cont_no);
					stmt1.setString(++selCnt1, cont_rev);
					stmt1.setString(++selCnt1, cargo_no[i]);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						log_seq_no = (rset1.getInt(1)+1);
					}
					else
					{
						log_seq_no = 0;
					}
					rset1.close();
					stmt1.close();
					
					query = "INSERT INTO LOG_LTCORA_CONT_CARGO_DTL (COMPANY_CD,"
							+ "COUNTERPARTY_CD,"
							+ "BUY_SALE,"
							+ "AGMT_TYPE,"
							+ "AGMT_NO,"
							+ "AGMT_REV,"
							+ "CONTRACT_TYPE,"
							+ "CONT_NO,"
							+ "CONT_REV,"
							+ "CARGO_NO,"
							+ "CARGO_REF,"
							+ "SHIP_CD,"
							+ "SUPP_CD,"
							+ "EDQ_FROM_DT,"
							+ "EDQ_TO_DT,"
							+ "ACTUAL_RECPT_DT,"
							+ "EDQ_QTY,"
							+ "CSOC_QTY,"
							+ "BOE_QTY,"
							+ "BOE_NO,"
							+ "BOE_DT,"
							+ "QQ_NO,"
							+ "QQ_DT,"
							+ "STORAGE_DAYS,"
							+ "STORAGE_EXT_DAYS,"
							+ "ENT_BY,"
							+ "ENT_DT,"
							+ "CARGO_STATUS";
					if("T".equals(buy_sell))
					{
						query	+= ",ATTACH_LNG_CARGO";
					}
					query	+= ",LOG_SEQ_NO,LOG_BY,LOG_DT)"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?";
					if("T".equals(buy_sell))
					{
						query	+=  ",?";
					}
					query	+= ",?,?,SYSDATE)";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(++insCnt, comp_cd);
					stmt2.setString(++insCnt, counterparty_cd);
					stmt2.setString(++insCnt, buy_sell);
					stmt2.setString(++insCnt, agmt_type);
					stmt2.setString(++insCnt, agmt_no);
					stmt2.setString(++insCnt, agmt_rev);
					stmt2.setString(++insCnt, contract_type);
					stmt2.setString(++insCnt, cont_no);
					stmt2.setString(++insCnt, cont_rev);
					stmt2.setString(++insCnt, cargo_no[i]);
					stmt2.setString(++insCnt, cargo_number[i]);
					stmt2.setString(++insCnt, vessel_cd[i]);
					stmt2.setString(++insCnt, supplier_cd[i]);
					stmt2.setString(++insCnt, window_start_dt[i]);
					stmt2.setString(++insCnt, window_end_dt[i]);
					stmt2.setString(++insCnt, actual_recpt_dt[i]);
					stmt2.setString(++insCnt, edq_qty[i]);
					stmt2.setString(++insCnt, csoc_qty[i]);
					stmt2.setString(++insCnt, boe_qty[i]);
					stmt2.setString(++insCnt, boe_no[i]);
					stmt2.setString(++insCnt, boe_dt[i]);
					stmt2.setString(++insCnt, qq_no[i]);
					stmt2.setString(++insCnt, qq_dt[i]);
					stmt2.setString(++insCnt, storage_days[i]);
					stmt2.setString(++insCnt, storage_ext_days[i]);
					stmt2.setString(++insCnt, emp_cd);
					stmt2.setString(++insCnt, "Y");
					if("T".equals(buy_sell)) 
					{
						stmt2.setString(++insCnt, attach_lng_cargo[i]);
					}
					stmt2.setString(++insCnt, ""+log_seq_no);
					stmt2.setString(++insCnt, emp_cd);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
			}
			else
			{
				msg="Failed! - Data Submission Failed!";
				msg_type="E";
			}
			
			if("C".equals(buy_sell))
			{
				url = "../ltcora/frm_ltcora_sell_cargo_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&cont_no="+cont_no+
						"&disp_cont_no="+disp_cont_no+"&cont_ref_no="+cont_ref_no+"&buy_sell="+buy_sell+"&cont_rev="+cont_rev+"&start_dt="+start_dt+"&end_dt="+end_dt+
						"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&sug_per="+sug_per+commonUrl_pra;
				
			}
			else if("T".equals(buy_sell))
			{
				url = "../ltcora/frm_ltcora_cargo_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&cont_no="+cont_no+
						"&disp_cont_no="+disp_cont_no+"&cont_ref_no="+cont_ref_no+"&buy_sell="+buy_sell+"&cont_rev="+cont_rev+"&start_dt="+start_dt+"&end_dt="+end_dt+
						"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&sug_per="+sug_per+commonUrl_pra;
			}
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - LTCORA Cargo Contract Insert/Update Failed!";
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
	
	private void InsertUpdateLtcoraCargoCnAdqDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraCargoCnAdqDetail()";
		
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
			
			String no_cargo = request.getParameter("no_cargo")==null?"0":request.getParameter("no_cargo");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			
			String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			
			String[] window_dt = request.getParameterValues("window_dt");
			String[] adq_mmbtu = request.getParameterValues("adq_mmbtu");
			
			String window_start_dt = request.getParameter("window_start_dt")==null?"":request.getParameter("window_start_dt");
			String window_end_dt = request.getParameter("window_end_dt")==null?"":request.getParameter("window_end_dt");
			
			String cargo_ref_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			
			if(counterparty_cd!=null)
			{
				for(int i=0; i<window_dt.length;i++)
				{
					int selCnt=0;
					
					if(!window_dt[i].equals("")) 
					{
						query1="SELECT COUNT(*) "
								+ "FROM FMS_LTCORA_CONT_CARGO_ADQ "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND BUY_SALE=? AND AGMT_TYPE=?"
								+ "AND AGMT_NO=? AND AGMT_REV=?"
								+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
								+ "AND CONT_REV=? AND CARGO_NO=? AND ADQ_DT=TO_DATE(?,'DD/MM/YYYY') ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, counterparty_cd);
						stmt1.setString(++selCnt, buy_sell);
						stmt1.setString(++selCnt, agmt_type);
						stmt1.setString(++selCnt, agmt_no);
						stmt1.setString(++selCnt, agmt_rev);
						stmt1.setString(++selCnt, contract_type);
						stmt1.setString(++selCnt, cont_no);
						stmt1.setString(++selCnt, cont_rev);
						stmt1.setString(++selCnt, cargo_no);
						stmt1.setString(++selCnt, window_dt[i]);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							int count=rset1.getInt(1);
							
							if(count>0)
							{
								int st_count=0;
								
								query ="UPDATE FMS_LTCORA_CONT_CARGO_ADQ SET ADQ_QTY=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND BUY_SALE=? AND AGMT_TYPE=?"
										+ "AND AGMT_NO=? AND AGMT_REV=?"
										+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
										+ "AND CONT_REV=? AND CARGO_NO=? AND ADQ_DT=TO_DATE(?,'DD/MM/YYYY')";
								stmt0 = dbcon.prepareStatement(query);
								stmt0.setString(++st_count, adq_mmbtu[i]);
								stmt0.setString(++st_count, emp_cd);
								stmt0.setString(++st_count, comp_cd);
								stmt0.setString(++st_count, counterparty_cd);
								stmt0.setString(++st_count, buy_sell);
								stmt0.setString(++st_count, agmt_type);
								stmt0.setString(++st_count, agmt_no);
								stmt0.setString(++st_count, agmt_rev);
								stmt0.setString(++st_count, contract_type);
								stmt0.setString(++st_count, cont_no);
								stmt0.setString(++st_count, cont_rev);
								stmt0.setString(++st_count, cargo_no);
								stmt0.setString(++st_count, window_dt[i]);
								stmt0.executeUpdate();
								
								stmt0.close();
								
								msg = "Successful! - "+cargo_ref_no+" Cargo/s Details for "+counterparty_abbr+" Submitted Successfully!";
								msg_type="S";
							}
							else 
							{
								int insCnt=0;
								
								query = "INSERT INTO FMS_LTCORA_CONT_CARGO_ADQ (COMPANY_CD,"
										+ "COUNTERPARTY_CD,"
										+ "BUY_SALE,"
										+ "AGMT_TYPE,"
										+ "AGMT_NO,"
										+ "AGMT_REV,"
										+ "CONTRACT_TYPE,"
										+ "CONT_NO,"
										+ "CONT_REV,"
										+ "CARGO_NO,"
										+ "ADQ_DT,"
										+ "ADQ_QTY,"
										+ "ENT_BY,"
										+ "ENT_DT)"
										+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
										+ "TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE)";
								stmt2 = dbcon.prepareStatement(query);
								stmt2.setString(++insCnt, comp_cd);
								stmt2.setString(++insCnt, counterparty_cd);
								stmt2.setString(++insCnt, buy_sell);
								stmt2.setString(++insCnt, agmt_type);
								stmt2.setString(++insCnt, agmt_no);
								stmt2.setString(++insCnt, agmt_rev);
								stmt2.setString(++insCnt, contract_type);
								stmt2.setString(++insCnt, cont_no);
								stmt2.setString(++insCnt, cont_rev);
								stmt2.setString(++insCnt, cargo_no);
								stmt2.setString(++insCnt, window_dt[i]);
								stmt2.setString(++insCnt, adq_mmbtu[i]);
								stmt2.setString(++insCnt, emp_cd);
								stmt2.executeUpdate();
								
								stmt2.close();
								
								msg = "Successful! - "+cargo_ref_no+" Cargo/s Details for "+counterparty_abbr+" Submitted Successfully!";
								msg_type="S";
							}
						}
						rset1.close();
						stmt1.close();
					}
				}
			}
			else
			{
				msg="Failed! - Data Submission Failed!";
				msg_type="E";
			}
			
			//cargo_ref_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			
			url = "../ltcora/frm_ltcora_cardo_adq_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&cargoRef="+cargo_ref_no+"&counterparty_cd="+counterparty_cd+
					"&window_start_dt="+window_start_dt+"&window_end_dt="+window_end_dt+"&cont_no="+cont_no+
					"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev+"&cargo_no="+cargo_no+
					"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
					"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - LTCORA Cargo Contract Insert/Update Failed!";
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
	
	private void InsertUpdateLtcoraCargoCnVarCSOCDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraCargoCnVarCSOCDetail()";
		
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
			String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			
			String no_cargo = request.getParameter("no_cargo")==null?"0":request.getParameter("no_cargo");
			String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			
			String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			
			String[] from_dt = request.getParameterValues("from_dt");
			String[] to_dt = request.getParameterValues("to_dt");
			String[] csoc = request.getParameterValues("csoc");
			String[] remark = request.getParameterValues("remark");
			
			String cargo_ref_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			
			if(counterparty_cd!=null && from_dt!=null)
			{
				int delCnt=0;
				
				query="DELETE FROM FMS_LTCORA_CONT_CARGO_CSOC "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=?"
						+ "AND AGMT_NO=? AND AGMT_REV=?"
						+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
						+ "AND CONT_REV=? AND CARGO_NO=? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(++delCnt, comp_cd);
				stmt.setString(++delCnt, counterparty_cd);
				stmt.setString(++delCnt, buy_sell);
				stmt.setString(++delCnt, agmt_type);
				stmt.setString(++delCnt, agmt_no);
				stmt.setString(++delCnt, agmt_rev);
				stmt.setString(++delCnt, contract_type);
				stmt.setString(++delCnt, cont_no);
				stmt.setString(++delCnt, cont_rev);
				stmt.setString(++delCnt, cargo_no);
				stmt.executeUpdate();
				
				stmt.close();
				
				for(int i=0; i<from_dt.length;i++)
				{
					int selCnt=0;
					int insCnt=0;
					
					if(!from_dt[i].equals("")) 
					{
						String seq_no="1";
						
						query="SELECT MAX(CSOC_SEQ_NO) "
								+ "FROM FMS_LTCORA_CONT_CARGO_CSOC "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND BUY_SALE=? AND AGMT_TYPE=?"
								+ "AND AGMT_NO=? AND AGMT_REV=?"
								+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
								+ "AND CONT_REV=? AND CARGO_NO=? ";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, counterparty_cd);
						stmt1.setString(++selCnt, buy_sell);
						stmt1.setString(++selCnt, agmt_type);
						stmt1.setString(++selCnt, agmt_no);
						stmt1.setString(++selCnt, agmt_rev);
						stmt1.setString(++selCnt, contract_type);
						stmt1.setString(++selCnt, cont_no);
						stmt1.setString(++selCnt, cont_rev);
						stmt1.setString(++selCnt, cargo_no);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							seq_no=""+(rset1.getInt(1)+1);
						}
						rset1.close();
						stmt1.close();
						
						query="INSERT INTO FMS_LTCORA_CONT_CARGO_CSOC(COMPANY_CD,"
								+ "COUNTERPARTY_CD,"
								+ "BUY_SALE,"
								+ "AGMT_TYPE,"
								+ "AGMT_NO,"
								+ "AGMT_REV,"
								+ "CONTRACT_TYPE,"
								+ "CONT_NO,"
								+ "CONT_REV,"
								+ "CARGO_NO,"
								+ "CSOC_SEQ_NO,"
								+ "FROM_DT,"
								+ "TO_DT,"
								+ "CSOC,"
								+ "REMARK,"
								+ "ENT_BY,"
								+ "ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE)";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(++insCnt, comp_cd);
						stmt2.setString(++insCnt, counterparty_cd);
						stmt2.setString(++insCnt, buy_sell);
						stmt2.setString(++insCnt, agmt_type);
						stmt2.setString(++insCnt, agmt_no);
						stmt2.setString(++insCnt, agmt_rev);
						stmt2.setString(++insCnt, contract_type);
						stmt2.setString(++insCnt, cont_no);
						stmt2.setString(++insCnt, cont_rev);
						stmt2.setString(++insCnt, cargo_no);
						stmt2.setString(++insCnt, seq_no);
						stmt2.setString(++insCnt, from_dt[i]);
						stmt2.setString(++insCnt, to_dt[i]);
						stmt2.setString(++insCnt, csoc[i]);
						stmt2.setString(++insCnt, remark[i]);
						stmt2.setString(++insCnt, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						msg = "Successful! - "+cargo_ref_no+" Variable CSOC Data for "+counterparty_abbr+" submitted Successfully!";
						msg_type = "S";
					}
				}
			}
			else
			{
				msg="Failed! - "+cont_ref_no+" Variable CSOC Data for "+counterparty_abbr+" submitted Failed!";
				msg_type="E";
			}
			
			//String cargo_ref_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, cargo_no);
			
			url = "../ltcora/frm_ltcora_cargo_var_csoc_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&cargoRef="+cargo_ref_no+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_ref_no="+cont_ref_no+"&cont_rev="+cont_rev+"&cargo_no="+cargo_no+
					"&cont_name="+cont_name+"&no_cargo="+no_cargo+"&agmt_no="+agmt_no+"&contract_type="+contract_type+
					"&agmt_rev="+agmt_rev+"&agmt_type="+agmt_type+"&buy_sell="+buy_sell+"&start_dt="+start_dt+"&end_dt="+end_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - LTCORA Cargo Contract Insert/Update Failed!";
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
	
	private void InsertUpdateLtcoraAgmtLiabilityDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraAgmtLiabilityDetail()";
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
			String buy_sale=request.getParameter("buy_sale")==null?"T":request.getParameter("buy_sale");

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
			String makeup_liab_on = request.getParameter("makeup_liab_on")==null?"P":request.getParameter("makeup_liab_on");
			String recovery_day = request.getParameter("recovery_day")==null?"":request.getParameter("recovery_day");
			String mug_remark = request.getParameter("mug_remark")==null?"":request.getParameter("mug_remark");
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_AGMT_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND BUY_SALE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, buy_sale);
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
					query="UPDATE FMS_LTCORA_AGMT_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND BUY_SALE=?";
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
					stmt1.setString(++cnt, makeup_liab_on);
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, buy_sale);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_LTCORA_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT,BUY_SALE) "
							+ "VALUES(?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE,?)";
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
					stmt1.setString(++cnt, makeup_liab_on);
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, buy_sale);
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
			
			url = "../ltcora/frm_ltcora_agmt_liability_clause.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&buy_sale="+buy_sale+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Liability Insert/Update Failed";
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
	
	private void InsertUpdateLtcoraCnLiabilityDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateLtcoraCnLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"M":request.getParameter("agreement_type");
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
			String buy_sale=request.getParameter("buy_sale")==null?"T":request.getParameter("buy_sale");
			String makeup_liab_on=request.getParameter("makeup_liab_on")==null?"P":request.getParameter("makeup_liab_on");
			
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND BUY_SALE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, buy_sale);
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
					query="UPDATE FMS_LTCORA_CONT_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND BUY_SALE=?";
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
					stmt1.setString(++cnt, makeup_liab_on);
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, buy_sale);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+cont_name_map+" Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_LTCORA_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT,BUY_SALE) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE,?)";
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
					stmt1.setString(++cnt, makeup_liab_on);
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, buy_sale);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! -  "+cont_name_map+" Liability Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! -  "+cont_name_map+" Liability Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../ltcora/frm_ltcora_cont_liability_clause.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&buy_sale="+buy_sale+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Liability Insert/Update Failed";
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
	
	//PB20250430: for maintaining the logs
	public void generateLtcoraContLogs(String counterparty_cd,String buy_sale,String agmt_no,String agmt_rev_no,String agmt_type,String cont_no,String cont_rev_no,String cont_type,String emp_cd) throws Exception
	{
		String function_nm="generateLtcoraContLogs()";
		try
		{
			int log_seq_no=0;
			String query="SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_LTCORA_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_type);
			stmt.setString(4, cont_no);
			stmt.setString(5, buy_sale);
			stmt.setString(6, agmt_type);
			stmt.setString(7, agmt_no);
			stmt.setString(8, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				log_seq_no = (rset.getInt(1)+1);
			}
			else
			{
				log_seq_no = 0;
			}
			rset.close();
			stmt.close();
			
			int ctn=0;
			String query3="INSERT INTO LOG_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,LOG_SEQ_NO, "
					+ "CONTRACT_TYPE,CONT_REF_NO,SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,START_DT,END_DT,AGMT_BASE,CONT_STATUS,BUYER_NOM, "
					+ "BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF, "
					+ "DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM, "
					+ "SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,MEASUREMENT, "
					+ "MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE, "
					+ "SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE, "
					+ "TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT, "
					+ "EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,STORAGE_TARIFF_UNIT,LOG_BY,LOG_DT,FCC_FLAG,FCC_BY,FCC_DATE, "
					+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,MODIFY_BY,MODIFY_DT,ADV_ADJUST,TLU_FLAG ) "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,?,CONTRACT_TYPE,CONT_REF_NO, "
					+ "SIGNING_DT,SIGNING_TIME,DDA_DT,DDA_TIME,START_DT,END_DT,AGMT_BASE,CONT_STATUS,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM, "
					+ "BUYER_DAILY_NOM,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ, "
					+ "MDCQ_PERCENTAGE,ENT_DT,ENT_BY,CONT_NAME,BILLING_FLAG,REV_DT,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,BUY_SALE,"
					+ "BUYER_NOM_CLAUSE,SELLER_NOM_CLAUSE,DAY_DEF_CLAUSE,MDCQ_CLAUSE,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,"
					+ "PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
					+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,NO_OF_CARGO,"
					+ "LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF,"
					+ "STORAGE_TARIFF_UNIT,?,SYSDATE,FCC_FLAG,FCC_BY,FCC_DATE,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,"
					+ "CLOSURE_REMARK,MODIFY_BY,MODIFY_DT,ADV_ADJUST,TLU_FLAG "
					+ "FROM FMS_LTCORA_CONT_MST A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
					+ "AND BUY_SALE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=?";
			stmt=dbcon.prepareStatement(query3);
			stmt.setString(++ctn, ""+log_seq_no);
			stmt.setString(++ctn, emp_cd);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, counterparty_cd);
			stmt.setString(++ctn, agmt_no);
			stmt.setString(++ctn, agmt_rev_no);
			stmt.setString(++ctn, agmt_type);
			stmt.setString(++ctn, buy_sale);
			stmt.setString(++ctn, cont_no);
			stmt.setString(++ctn, cont_type);
			stmt.setString(++ctn, cont_rev_no);
			stmt.executeUpdate();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
	}
	
	//AP20260307
	private void updateContractBillingPlant(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String plant_seq, String new_plant_seq ,int new_plant_cnt, String buy_sell, String agreement_type) throws Exception
	{
		String function_nm="updateContractBillingPlant()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, buy_sell);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int new_billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, buy_sell);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String hol_state=utilBean.getState_TIN(dbcon, comp_cd, counterpty_cd, buy_sell, new_plant_seq);
			if(hol_state.equals(""))
			{
				hol_state="24";
			}
			
			if(billing_count > 0 && new_billing_count == 0)
			{
				if(new_plant_cnt==1)
				{
					query ="UPDATE FMS_LTCORA_CONT_BILLING_DTL A SET PLANT_SEQ_NO=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_NO=? "
							+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, new_plant_seq);
					stmt.setString(2, comp_cd);
					stmt.setString(3, counterpty_cd);
					stmt.setString(4, cont_no);
					stmt.setString(5, plant_seq);
					stmt.setString(6, agmt_no);
					stmt.setString(7, contract_type);
					stmt.setString(8, buy_sell);
					stmt.setString(9, agreement_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else
				{
					query="INSERT INTO FMS_LTCORA_CONT_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,BUY_SALE,AGMT_TYPE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,?,BUY_SALE,AGMT_TYPE "
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND BUY_SALE=? AND AGMT_TYPE=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=? AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE)";
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
					stmt0.setString(10, buy_sell);
					stmt0.setString(11, agreement_type);
					stmt0.setString(12, new_plant_seq);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
			}
			else
			{
				query ="UPDATE FMS_LTCORA_CONT_BILLING_DTL A SET HOLIDAY_STATE=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? AND HOLIDAY_STATE IS NULL";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, hol_state);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, new_plant_seq);
				stmt.setString(6, agmt_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, buy_sell);
				stmt.setString(9, agreement_type);
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
	}
}
