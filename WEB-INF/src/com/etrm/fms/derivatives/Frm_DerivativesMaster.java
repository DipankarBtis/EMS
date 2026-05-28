package com.etrm.fms.derivatives;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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

@WebServlet("/servlet/Frm_DerivativesMaster")
public class Frm_DerivativesMaster extends HttpServlet
{
	static String frm_src_file_name="Frm_DerivativesMaster.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_DerivativesMaster";
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
					
					if(option.equalsIgnoreCase("HEDGE_AGREEMENT_MST"))
					{
						InsertUpdateHedgeAgreement(request);
					}
					else if(option.equalsIgnoreCase("HEDGE_AGREEMENT_BILLING_DTL"))
					{
						InsertUpdateHedgeAgreementBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("HEDGE_CONT_MST"))
					{
						InsertUpdateHedgeContDetail(request);
					}
					else if(option.equalsIgnoreCase("HEDGE_CONT_BILLING_DTL"))
					{
						InsertUpdateHedgeContBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("HEDGE_INSTRUMENT_DTL"))
					{
						InsertUpdateHedgeInstrumentDtls(request);
					}
					else if(option.equalsIgnoreCase("DIRECT_EXPOSURE"))
					{
						InsertUpdateDirectExposureDtls(request);
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
	
	private void InsertUpdateDirectExposureDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateDirectExposureDtls()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String exposure_value=request.getParameter("exposure_value")==null?"":request.getParameter("exposure_value");
			String hedge_dt=request.getParameter("hedge_dt")==null?"":request.getParameter("hedge_dt");
			if(!exposure_value.equals(""))
			{
				query1="INSERT INTO FMS_DERV_HEDGE_EXPOSURE_DTL(COMPANY_CD,HEDGE_DT,BALANCE_QTY,ENT_BY,ENT_DT,FLAG) "
					+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,'L')";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, hedge_dt);
				stmt1.setString(3, exposure_value);
				stmt1.setString(4, emp_cd);
				stmt1.executeUpdate();
				
				stmt1.close();
			}
				
			msg = "Successful! - Exposure Value added!";
			msg_type="S";
			
			url = "../derivatives/frm_direct_exposure_entry.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Exposure Insert/Update Failed!";
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
	
	private void InsertUpdateHedgeAgreement(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHedgeAgreement()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map="";
		try
		{
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals(""))
			{
				status="A";
			}
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String[] chk_plant = request.getParameterValues("chk_plant");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String billing_clause = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");

			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				query="SELECT MAX(AGMT_NO) "
						+ "FROM FMS_DERV_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
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
							+ "FROM FMS_DERV_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agreement_type);
					stmt.setString(4, agmt_no);
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
			
			String agmt_name = comp_abbr+"-"+counterparty_abbr+"-"+agreement_type+agmt_no+"-REV"+agmt_rev_no;
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_DERV_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
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
					int upCnt=0;
					
					query ="UPDATE FMS_DERV_AGMT_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),STATUS=?,"
							+ "BILLING_FLAG=?,BILLING_CLAUSE=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "MODIFY_DT=SYSDATE, MODIFY_BY=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++upCnt, agmt_ref_no);
					stmt0.setString(++upCnt, signing_dt);
					stmt0.setString(++upCnt, start_dt);
					stmt0.setString(++upCnt, end_dt);
					stmt0.setString(++upCnt, status);
					stmt0.setString(++upCnt, billing_flag);
					stmt0.setString(++upCnt, billing_clause);
					stmt0.setString(++upCnt, rev_eff_dt);
					stmt0.setString(++upCnt, emp_cd);
					stmt0.setString(++upCnt, comp_cd);
					stmt0.setString(++upCnt, counterparty_cd);
					stmt0.setString(++upCnt, agmt_no);
					stmt0.setString(++upCnt, agmt_rev_no);
					stmt0.setString(++upCnt, agreement_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int cnt = 0;
					query = "INSERT INTO FMS_DERV_AGMT_MST"
							+ "(COMPANY_CD,"
							+ "COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,"
							+ "SIGNING_DT,START_DT,END_DT,STATUS,ENT_BY,ENT_DT,REV_DT,"
							+ "BILLING_FLAG,BILLING_CLAUSE) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "SYSDATE,"
							+ "TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++cnt, comp_cd);
					stmt0.setString(++cnt, counterparty_cd);
					stmt0.setString(++cnt, agreement_type);
					stmt0.setString(++cnt, agmt_no);
					stmt0.setString(++cnt, agmt_rev_no);
					stmt0.setString(++cnt, agmt_name);
					stmt0.setString(++cnt, agmt_ref_no);
					stmt0.setString(++cnt, signing_dt);
					stmt0.setString(++cnt, start_dt);
					stmt0.setString(++cnt, end_dt);
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, billing_clause);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				
				int count = 0;
				
				//TRADER PLANT
				query = "DELETE FROM FMS_DERV_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND "
						+ "EXISTS (SELECT * "
						+ "FROM FMS_DERV_AGMT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV) ";
				
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agreement_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				stmt1.executeUpdate();
				stmt1.close();
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_DERV_AGMT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agreement_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//BUSINESS UNIT
				query = "DELETE FROM FMS_DERV_AGMT_BU A  "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND "
						+ "EXISTS (SELECT * "
						+ "FROM FMS_DERV_AGMT_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV) ";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agreement_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				stmt_temp.executeUpdate();
				stmt_temp.close();
				
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_DERV_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agreement_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Agreement "+agmt_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Agreement "+agmt_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Agreement "+agmt_name_map+" for "+counterparty_abbr+" Modification Failed!";
				msg_type="E";
			}
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			new_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#AGMTNAME="+agmt_name+"#AGMTNO="+agmt_no+"#AGMTREFNO="+agmt_ref_no+"#AGMTTYPE="+agreement_type+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#STARTDT="+start_dt+"#ENDDT="+end_dt;
			
			url = "../derivatives/frm_derivatives_agmt_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agreement_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+agmt_name_map+" Insert/Update Failed";
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
	
	private void InsertUpdateHedgeAgreementBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHedgeAgreementBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map ="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"U":request.getParameter("agreement_type");
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
			
			String[] cust_plant_map = holidayState_map.split("@@");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			//agmt_name_map = counterparty_abbr+"-"+agreement_type+agmt_no+"-"+agmt_rev_no;
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);

			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_DERV_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "
						+ "AND AGMT_TYPE=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				//stmt3.setString(4, agmt_rev_no);
				stmt3.setString(4, agreement_type);
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
							+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					//stmt.setString(4, agmt_rev_no);
					stmt.setString(4, agreement_type);
					stmt.setString(5, plant_seq);
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
						query="UPDATE FMS_DERV_AGMT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
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
						//stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+"  Modified Successfully!";
						msg_type = "S";
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_DERV_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
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
						
						msg = "Successful! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+" submitted Successfully!";
						msg_type = "S";
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../derivatives/frm_derivatives_agmt_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! "+agmt_name_map+" Billing Detail Insert/Update Failed";

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
	
	private void InsertUpdateHedgeContDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHedgeContDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = "";
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String trade_ref_no = request.getParameter("trade_ref_no")==null?"":request.getParameter("trade_ref_no");
			String contract_type = request.getParameter("contract_type")==null?"V":request.getParameter("contract_type");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String trade_dt = request.getParameter("trade_dt")==null?"":request.getParameter("trade_dt");
			String trade_time = request.getParameter("trade_time")==null?"":request.getParameter("trade_time");
			String dda_dt = request.getParameter("dda_dt")==null?"":request.getParameter("dda_dt");
			String dda_time = request.getParameter("dda_time")==null?"":request.getParameter("dda_time");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String agreement_type = request.getParameter("agreement_type")==null?"U":request.getParameter("agreement_type");
			
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String billing_clause = request.getParameter("billing_clause")==null?"":request.getParameter("billing_clause");
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			String tmp_chk_plant = request.getParameter("tmp_chk_plant")==null?"":request.getParameter("tmp_chk_plant");
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String cont_status_flg = request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg");
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String no_of_instrument = request.getParameter("instrument_number")==null?"":request.getParameter("instrument_number");
			String change_request = request.getParameter("change_request")==null?"":request.getParameter("change_request");
			String cancel_note = request.getParameter("cancel_note")==null?"":request.getParameter("cancel_note");
			String[] instru_no = request.getParameterValues("instru_no");
			String today_dt=utilDate.getSysdate();
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				String year = ent_dt.substring(8,ent_dt.length()); //Removed Prefix number after deciding with JD Mam.
				int cont = Integer.parseInt(year) * 10000;
				query="SELECT MAX(CONT_NO) FROM FMS_DERV_CONT_MST "
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
				cont_rev_no="0";
				
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(CONT_REV) "
							+ "FROM FMS_DERV_CONT_MST "
							+ "WHERE CONT_NO= ? AND COMPANY_CD=? "
							+ "AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no);
					stmt.setString(2, comp_cd);
					stmt.setString(3, contract_type);
					stmt.setString(4, counterparty_cd);
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
					cont_status_flg = "P";
					
				}
			}
			
			// DONT CHANGE - THIS IS CONTRACT NAME
			String cont_name = comp_abbr+"-"+counterparty_abbr+"-HEDGE"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;
			// DISPLAY CONTRACT NAME 
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_DERV_CONT_MST "
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
					int upCnt=0;
					
					query ="UPDATE FMS_DERV_CONT_MST SET CONT_REF_NO=?, DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "SIGNING_TIME=?, TRADE_DT=TO_DATE(?,'DD/MM/YYYY'),TRADE_TIME=?,NO_OF_INSTRUMENT=?,REMARKS=?,"
							+ "BILLING_FLAG=?, BILLING_CLAUSE=?,MODIFY_DT=SYSDATE,MODIFY_BY=? ";
					if(change_request.equals("CANCEL"))
					{
						query+=",CLOSE_EFF_DT=SYSDATE,CLOSURE_REMARK=?,CONT_STATUS=? ";
					}
						query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++upCnt, cont_ref_no);
					stmt1.setString(++upCnt, dda_dt);
					stmt1.setString(++upCnt, dda_time);
					stmt1.setString(++upCnt, signing_dt);
					stmt1.setString(++upCnt, signing_time);
					stmt1.setString(++upCnt, trade_dt);
					stmt1.setString(++upCnt, trade_time);
					stmt1.setString(++upCnt, no_of_instrument);
					stmt1.setString(++upCnt, remark);
					stmt1.setString(++upCnt, billing_flag);
					stmt1.setString(++upCnt, billing_clause);
					stmt1.setString(++upCnt, emp_cd);
					if(change_request.equals("CANCEL"))
					{
						stmt1.setString(++upCnt, cancel_note);
						stmt1.setString(++upCnt, cont_status_flg);
					}
					stmt1.setString(++upCnt, comp_cd);
					stmt1.setString(++upCnt, counterparty_cd);
					stmt1.setString(++upCnt, cont_no);
					stmt1.setString(++upCnt, cont_rev_no);
					stmt1.setString(++upCnt, agmt_no);
					stmt1.setString(++upCnt, agmt_rev_no);
					stmt1.setString(++upCnt, contract_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					if(change_request.equals("CANCEL"))
					{
						int up_Cnt=0;
						query1 ="UPDATE FMS_DERV_INSTRUMENT_MST SET STATUS=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt2 = dbcon.prepareStatement(query1);
						stmt2.setString(++up_Cnt, "X");
						stmt2.setString(++up_Cnt, comp_cd);
						stmt2.setString(++up_Cnt, counterparty_cd);
						stmt2.setString(++up_Cnt, cont_no);
						stmt2.setString(++up_Cnt, cont_rev_no);
						stmt2.setString(++up_Cnt, agmt_no);
						stmt2.setString(++up_Cnt, agmt_rev_no);
						stmt2.setString(++up_Cnt, contract_type);
						stmt2.executeUpdate();
						stmt2.close();
					}
				}
				else
				{
					int insCnt=0;
					
					query ="INSERT INTO FMS_DERV_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
							+ "DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,TRADE_DT,TRADE_TIME,ENT_DT,ENT_BY,"
							+ "BILLING_FLAG,BILLING_CLAUSE,REV_DT,REMARKS) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, counterparty_cd);
					stmt1.setString(++insCnt, agreement_type);
					stmt1.setString(++insCnt, agmt_no);
					stmt1.setString(++insCnt, agmt_rev_no);
					stmt1.setString(++insCnt, contract_type);
					stmt1.setString(++insCnt, cont_no);
					stmt1.setString(++insCnt, cont_rev_no);
					stmt1.setString(++insCnt, cont_name);
					stmt1.setString(++insCnt, cont_ref_no);
					stmt1.setString(++insCnt, cont_status_flg);
					stmt1.setString(++insCnt, dda_dt);
					stmt1.setString(++insCnt, dda_time);
					stmt1.setString(++insCnt, signing_dt);
					stmt1.setString(++insCnt, signing_time);
					stmt1.setString(++insCnt, trade_dt);
					stmt1.setString(++insCnt, trade_time);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, billing_flag);
					stmt1.setString(++insCnt, billing_clause);
					stmt1.setString(++insCnt, rev_eff_dt);
					stmt1.setString(++insCnt, remark);
					stmt1.executeUpdate();
					stmt1.close();
				}
				
				//TRADER PLANT
				query1 = "DELETE FROM FMS_DERV_CONT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=?  "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?  "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_DERV_CONT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ ")";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.executeUpdate();
				stmt1.close();
				
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_DERV_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
								+ "CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
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
						stmt2.executeUpdate();
						
						stmt2.close();
						
						String temp_chk_plant[]=tmp_chk_plant.split("@");
						for(int m=0; m<temp_chk_plant.length; m++)
						{
							if(!temp_chk_plant[m].equals(chk_plant[i]) && !chk_plant[i].equals(""))
							{
								updateDerivativesContractBillingPlant(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_plant[m], chk_plant[i], chk_plant.length);
							}
						}
					 }
				}
				
				//BUSINESS UNIT
				query1="DELETE FROM FMS_DERV_CONT_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.CONT_REV=?  "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=?  "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_DERV_CONT_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONT_REV=B.CONT_REV AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV  "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ ")";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.executeUpdate();
				stmt1.close();
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_DERV_CONT_BU(COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?, ?,?,?,?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
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
						stmt2.executeUpdate();
						
						stmt2.close();
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
					msg = "Successful! - New Derivative (Hedge) Deal "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Derivative (Hedge) Deal "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
				
				int insCnt=0;
				int log_seq_no=0;
				
				String query="SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_DERV_CONT_MST "
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
					log_seq_no = (rset.getInt(1)+1);
				}
				else
				{
					log_seq_no = 0;
				}
				rset.close();
				stmt.close();
				
				query1 ="INSERT INTO LOG_DERV_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,LOG_SEQ_NO,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
						+ "DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,TRADE_DT,TRADE_TIME,ENT_DT,ENT_BY,"
						+ "BILLING_FLAG,BILLING_CLAUSE,REV_DT,REMARKS,LOG_ENT_DT";
				if(change_request.equals("CANCEL"))
				{
					query1+=",CLOSE_EFF_DT,CLOSURE_REMARK ";
				}
				query1+= ") "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE";
				if(change_request.equals("CANCEL"))
				{
					query1+=",SYSDATE,? ";
				}
				query1+=")";
				stmt1 = dbcon.prepareStatement(query1);
				stmt1.setString(++insCnt, comp_cd);
				stmt1.setString(++insCnt, counterparty_cd);
				stmt1.setString(++insCnt, agreement_type);
				stmt1.setString(++insCnt, agmt_no);
				stmt1.setString(++insCnt, agmt_rev_no);
				stmt1.setString(++insCnt, contract_type);
				stmt1.setString(++insCnt, cont_no);
				stmt1.setString(++insCnt, cont_rev_no);
				stmt1.setString(++insCnt, ""+log_seq_no);
				stmt1.setString(++insCnt, cont_name);
				stmt1.setString(++insCnt, cont_ref_no);
				stmt1.setString(++insCnt, cont_status_flg);
				stmt1.setString(++insCnt, dda_dt);
				stmt1.setString(++insCnt, dda_time);
				stmt1.setString(++insCnt, signing_dt);
				stmt1.setString(++insCnt, signing_time);
				stmt1.setString(++insCnt, trade_dt);
				stmt1.setString(++insCnt, trade_time);
				stmt1.setString(++insCnt, emp_cd);
				stmt1.setString(++insCnt, billing_flag);
				stmt1.setString(++insCnt, billing_clause);
				stmt1.setString(++insCnt, rev_eff_dt);
				stmt1.setString(++insCnt, remark);
				if(change_request.equals("CANCEL"))
				{
					stmt1.setString(++insCnt, cancel_note);
				}
				stmt1.executeUpdate();
				stmt1.close();
				
				if(change_request.equals("CANCEL"))
				{
					for(int i=0; i<instru_no.length; i++)
					{
						int insCnt1=0;
						int log_seq_no1=0;
						
						String query1="SELECT MAX(LOG_SEQ_NO) "
								+ "FROM LOG_DERV_INSTRUMENT_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? ";
								//+ "AND AGMT_REV=? AND CONT_REV=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, agreement_type);
						stmt1.setString(5, agmt_no);
						stmt1.setString(6, contract_type);
						stmt1.setString(7, instru_no[i]);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							log_seq_no1 = (rset1.getInt(1)+1);
						}
						else
						{
							log_seq_no1 = 0;
						}
						rset1.close();
						stmt1.close();
						
						query1="INSERT INTO LOG_DERV_INSTRUMENT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_TYPE,CONTRACT_TYPE,"
								+ "CONT_NO,AGMT_REV,CONT_REV,LOG_SEQ_NO,INSTRUMENT_NO,INSTRUMENT_TYPE,BUY_SELL,STATUS,QTY,QTY_UNIT,"
								+ "RATE,RATE_UNIT,PRODUCT_NM,CURVE_NM,PROJ_METHOD,CONT_DD_MM_YR,PRICE_START_DT,PRICE_END_DT,CONV_FACTOR,"
								+ "ENT_BY,ENT_DT,LOG_ENT_DT) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_TYPE,CONTRACT_TYPE,CONT_NO,AGMT_REV,CONT_REV,"
								+ "?,INSTRUMENT_NO,INSTRUMENT_TYPE,BUY_SELL,'X',QTY,QTY_UNIT,RATE,RATE_UNIT,PRODUCT_NM,CURVE_NM,PROJ_METHOD,"
								+ "CONT_DD_MM_YR,PRICE_START_DT,PRICE_END_DT,CONV_FACTOR,?,SYSDATE,SYSDATE "
								+ "FROM FMS_DERV_INSTRUMENT_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_TYPE=? AND CONTRACT_TYPE=? "
								+ "AND CONT_NO=? AND INSTRUMENT_NO=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++insCnt1, ""+log_seq_no1);
						stmt1.setString(++insCnt1, emp_cd);
						stmt1.setString(++insCnt1, comp_cd);
						stmt1.setString(++insCnt1, counterparty_cd);
						stmt1.setString(++insCnt1, agmt_no);
						stmt1.setString(++insCnt1, agreement_type);
						stmt1.setString(++insCnt1, contract_type);
						stmt1.setString(++insCnt1, cont_no);
						stmt1.setString(++insCnt1, instru_no[i]);
						
						stmt1.executeUpdate();
						stmt1.close();
					}
				}
			}
			else
			{
				msg = "Failed! - Derivative (Hedge) Deal "+cont_name_map+" for "+counterparty_abbr+" Modification Failed!";
				msg_type="E";
			}
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			new_value ="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time
					+"#SIGNING_DT="+signing_dt+"#SIGNING_TIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#TRADE_DT="+trade_dt+"#TRADE_TIME="+trade_time+"#CONT_STATUS="+cont_status_flg;
			
			url = "../derivatives/frm_derivatives_deal_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Confirmation Notice Insert/Update Failed";
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
	
	private void InsertUpdateHedgeContBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHedgeContBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agreement_type=request.getParameter("agreement_type")==null?"U":request.getParameter("agreement_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"V":request.getParameter("contract_type");
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
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_DERV_CONT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
						+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, cont_no);
				stmt3.setString(4, agmt_no);
				stmt3.setString(5, contract_type);
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
							+ "FROM FMS_DERV_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
							+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					//stmt.setString(4, cont_rev_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, agmt_rev_no);
					stmt.setString(6, contract_type);
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
						query1="UPDATE FMS_DERV_CONT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
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
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, cont_no);
						//stmt1.setString(++cnt, cont_rev_no);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" Modified Successfully!";
						msg_type = "S";
					}
					else
					{
						int cnt1=0;
						query1="INSERT INTO FMS_DERV_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, agreement_type);
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
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" submitted Successfully!";
						msg_type = "S";
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../derivatives/frm_derivatives_cont_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Hedge Contract Billing Details Insert/Update Failed";
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

	private void InsertUpdateHedgeInstrumentDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateHedgeInstrumentDtls()";
		msg="";
		msg_type="";
		url="";
		String instrument_no = "";
		String instrument_no_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");

			instrument_no=request.getParameter("instrument_no")==null?"":request.getParameter("instrument_no");
			String instrument_type=request.getParameter("instrument_type")==null?"":request.getParameter("instrument_type");
			String buy_sell=request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
			String status=request.getParameter("inst_status_flag")==null?"":request.getParameter("inst_status_flag");
			String qty=request.getParameter("qty")==null?"":request.getParameter("qty");
			String qty_unit_nm=request.getParameter("qty_unit")==null?"":request.getParameter("qty_unit");
			String qty_unit="";
			if(qty_unit_nm.equals("MMBTU")){qty_unit="1";}
			if(qty_unit_nm.equals("TBTU")){qty_unit="2";}
			if(qty_unit_nm.equals("SCM")){qty_unit="3";}
			if(qty_unit_nm.equals("MMSCM")){qty_unit="4";}
			if(qty_unit_nm.equals("MT")){qty_unit="5";}
			if(qty_unit_nm.equals("BBL")){qty_unit="6";}
			
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String rate_unit=request.getParameter("rate_unit")==null?"":request.getParameter("rate_unit");
			String product_nm=request.getParameter("product_nm")==null?"":request.getParameter("product_nm");
			String curve_nm=request.getParameter("curve_nm")==null?"":request.getParameter("curve_nm");
			String proj_method=request.getParameter("proj_method")==null?"":request.getParameter("proj_method");
			String cont_month=request.getParameter("cont_month")==null?"":request.getParameter("cont_month");
			String cont_year=request.getParameter("cont_year")==null?"":request.getParameter("cont_year");
			String price_start_dt=request.getParameter("price_start_dt")==null?"":request.getParameter("price_start_dt");
			String price_end_dt=request.getParameter("price_end_dt")==null?"":request.getParameter("price_end_dt");
			String instrument_number=request.getParameter("instrument_number")==null?"0":request.getParameter("instrument_number");

			String conv_factor = request.getParameter("conv_factor")==null?"":request.getParameter("conv_factor");
			
			String cont_dd_mm_yr = "01/"+cont_month+"/"+cont_year;
			
			instrument_no_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, instrument_no);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !instrument_no.equals("") && !cont_no.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_DERV_INSTRUMENT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND AGMT_REV=? AND CONT_REV=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, instrument_no);
				stmt.setString(8, agmt_rev_no);
				stmt.setString(9, cont_rev_no);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count == 0) 
				{
					int upCnt=0;
					
					int added_instr_no = Integer.parseInt(instrument_number)+1;
					
					query ="UPDATE FMS_DERV_CONT_MST SET NO_OF_INSTRUMENT=?,CONT_STATUS=?,"
							+ "MODIFY_DT=SYSDATE,MODIFY_BY=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++upCnt, ""+added_instr_no);
					stmt1.setString(++upCnt, "A");
					stmt1.setString(++upCnt, emp_cd);
					stmt1.setString(++upCnt, comp_cd);
					stmt1.setString(++upCnt, counterparty_cd);
					stmt1.setString(++upCnt, cont_no);
					stmt1.setString(++upCnt, cont_rev_no);
					stmt1.setString(++upCnt, agmt_no);
					stmt1.setString(++upCnt, agmt_rev_no);
					stmt1.setString(++upCnt, contract_type);
					stmt1.executeUpdate();
					stmt1.close();
				}
				
				if(count > 0)
				{
					int upCnt=0;
					
					query1="UPDATE FMS_DERV_INSTRUMENT_MST SET INSTRUMENT_TYPE=?,BUY_SELL=?,STATUS=?,QTY=?,QTY_UNIT=?,"
							+ "RATE=?,RATE_UNIT=?,PRODUCT_NM=?,CURVE_NM=?,PROJ_METHOD=?,CONT_DD_MM_YR=TO_DATE(?,'DD/MM/YYYY'),"
							+ "PRICE_START_DT=TO_DATE(?,'DD/MM/YYYY'),PRICE_END_DT=TO_DATE(?,'DD/MM/YYYY'),CONV_FACTOR=?,MODIFY_DT=SYSDATE,MODIFY_BY=?"
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND AGMT_REV=? AND CONT_REV=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++upCnt, instrument_type);
					stmt1.setString(++upCnt, buy_sell);
					stmt1.setString(++upCnt, status);
					stmt1.setString(++upCnt, qty);
					stmt1.setString(++upCnt, qty_unit);
					stmt1.setString(++upCnt, rate);
					stmt1.setString(++upCnt, rate_unit);
					stmt1.setString(++upCnt, product_nm);
					stmt1.setString(++upCnt, curve_nm);
					stmt1.setString(++upCnt, proj_method);
					stmt1.setString(++upCnt, cont_dd_mm_yr);
					stmt1.setString(++upCnt, price_start_dt);
					stmt1.setString(++upCnt, price_end_dt);
					stmt1.setString(++upCnt, conv_factor);
					stmt1.setString(++upCnt, emp_cd);
					stmt1.setString(++upCnt, comp_cd);
					stmt1.setString(++upCnt, counterparty_cd);
					stmt1.setString(++upCnt, cont_no);
					stmt1.setString(++upCnt, agreement_type);
					stmt1.setString(++upCnt, agmt_no);
					stmt1.setString(++upCnt, contract_type);
					stmt1.setString(++upCnt, instrument_no);
					stmt1.setString(++upCnt, agmt_rev_no);
					stmt1.setString(++upCnt, cont_rev_no);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+instrument_no_map+" Instrument Data for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int insCnt=0;
					
					query1="INSERT INTO FMS_DERV_INSTRUMENT_MST(COMPANY_CD,"
							+ "COUNTERPARTY_CD,"
							+ "AGMT_NO,"
							+ "AGMT_TYPE,"
							+ "CONTRACT_TYPE,"
							+ "CONT_NO,"
							+ "AGMT_REV,"
							+ "CONT_REV,"
							+ "INSTRUMENT_NO,"
							+ "INSTRUMENT_TYPE,"
							+ "BUY_SELL,"
							+ "STATUS,"
							+ "QTY,"
							+ "QTY_UNIT,"
							+ "RATE,"
							+ "RATE_UNIT,"
							+ "PRODUCT_NM,"
							+ "CURVE_NM,"
							+ "PROJ_METHOD,"
							+ "CONT_DD_MM_YR,"
							+ "PRICE_START_DT,"
							+ "PRICE_END_DT,"
							+ "CONV_FACTOR,"
							+ "ENT_BY,ENT_DT) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, counterparty_cd);
					stmt1.setString(++insCnt, agmt_no);
					stmt1.setString(++insCnt, agreement_type);
					stmt1.setString(++insCnt, contract_type);
					stmt1.setString(++insCnt, cont_no);
					stmt1.setString(++insCnt, agmt_rev_no);
					stmt1.setString(++insCnt, cont_rev_no);
					stmt1.setString(++insCnt, instrument_no);
					stmt1.setString(++insCnt, instrument_type);
					stmt1.setString(++insCnt, buy_sell);
					stmt1.setString(++insCnt, status);
					stmt1.setString(++insCnt, qty);
					stmt1.setString(++insCnt, qty_unit);
					stmt1.setString(++insCnt, rate);
					stmt1.setString(++insCnt, rate_unit);
					stmt1.setString(++insCnt, product_nm);
					stmt1.setString(++insCnt, curve_nm);
					stmt1.setString(++insCnt, proj_method);
					stmt1.setString(++insCnt, cont_dd_mm_yr);
					stmt1.setString(++insCnt, price_start_dt);
					stmt1.setString(++insCnt, price_end_dt);
					stmt1.setString(++insCnt, conv_factor);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+instrument_no_map+" Instrument Data for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				
				int insCnt=0;
				int log_seq_no=0;
				
				String query="SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_DERV_INSTRUMENT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? ";
						//+ "AND AGMT_REV=? AND CONT_REV=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, instrument_no);
				//stmt.setString(8, agmt_rev_no);
				//stmt.setString(9, cont_rev_no);
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
				
				query1="INSERT INTO LOG_DERV_INSTRUMENT_MST(COMPANY_CD,"
						+ "COUNTERPARTY_CD,"
						+ "AGMT_NO,"
						+ "AGMT_TYPE,"
						+ "CONTRACT_TYPE,"
						+ "CONT_NO,"
						+ "AGMT_REV,"
						+ "CONT_REV,"
						+ "LOG_SEQ_NO,"
						+ "INSTRUMENT_NO,"
						+ "INSTRUMENT_TYPE,"
						+ "BUY_SELL,"
						+ "STATUS,"
						+ "QTY,"
						+ "QTY_UNIT,"
						+ "RATE,"
						+ "RATE_UNIT,"
						+ "PRODUCT_NM,"
						+ "CURVE_NM,"
						+ "PROJ_METHOD,"
						+ "CONT_DD_MM_YR,"
						+ "PRICE_START_DT,"
						+ "PRICE_END_DT,"
						+ "CONV_FACTOR,"
						+ "ENT_BY,ENT_DT,LOG_ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,SYSDATE)";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(++insCnt, comp_cd);
				stmt1.setString(++insCnt, counterparty_cd);
				stmt1.setString(++insCnt, agmt_no);
				stmt1.setString(++insCnt, agreement_type);
				stmt1.setString(++insCnt, contract_type);
				stmt1.setString(++insCnt, cont_no);
				stmt1.setString(++insCnt, agmt_rev_no);
				stmt1.setString(++insCnt, cont_rev_no);
				stmt1.setString(++insCnt, ""+log_seq_no);
				stmt1.setString(++insCnt, instrument_no);
				stmt1.setString(++insCnt, instrument_type);
				stmt1.setString(++insCnt, buy_sell);
				stmt1.setString(++insCnt, status);
				stmt1.setString(++insCnt, qty);
				stmt1.setString(++insCnt, qty_unit);
				stmt1.setString(++insCnt, rate);
				stmt1.setString(++insCnt, rate_unit);
				stmt1.setString(++insCnt, product_nm);
				stmt1.setString(++insCnt, curve_nm);
				stmt1.setString(++insCnt, proj_method);
				stmt1.setString(++insCnt, cont_dd_mm_yr);
				stmt1.setString(++insCnt, price_start_dt);
				stmt1.setString(++insCnt, price_end_dt);
				stmt1.setString(++insCnt, conv_factor);
				stmt1.setString(++insCnt, emp_cd);
				stmt1.executeUpdate();
				stmt1.close();
			}
			else
			{
				msg = "Failed! - "+instrument_no_map+" Instrument Data for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../derivatives/frm_derivatives_deal_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&contract_type="+contract_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Hedge Contract Billing Details Insert/Update Failed";
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
	
	private void updateDerivativesContractBillingPlant(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String plant_seq, String new_plant_seq, int new_plant_cnt) throws Exception
	{
		String function_nm="updateDerivativesContractBillingPlant()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_DERV_CONT_BILLING_DTL A "
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
					+ "FROM FMS_DERV_CONT_BILLING_DTL A "
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
					query ="UPDATE FMS_DERV_CONT_BILLING_DTL A SET PLANT_SEQ_NO=? "
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
					query="INSERT INTO FMS_DERV_CONT_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,AGMT_TYPE,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,AGMT_TYPE,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,? "
							+ "FROM FMS_DERV_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_DERV_CONT_BILLING_DTL B "
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
				query ="UPDATE FMS_DERV_CONT_BILLING_DTL A SET HOLIDAY_STATE=? "
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
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
	}
}
