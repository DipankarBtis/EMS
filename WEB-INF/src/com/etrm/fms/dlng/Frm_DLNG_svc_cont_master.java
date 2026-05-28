package com.etrm.fms.dlng;

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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Arth Patel
//Code Reviewed by	:  
//CR Date			: 01/02/2025
//Status	  		: Developing

@WebServlet("/servlet/Frm_DLNG_svc_cont_master")
public class Frm_DLNG_svc_cont_master extends HttpServlet
{
	static String frm_src_file_name="Frm_DLNG_svc_cont_master.java";
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
					
					if(option.equalsIgnoreCase("TMSA_SVC_AGMT"))
					{
						InsertUpdateLtcoraAgreementDetail(request);
					}
					else if(option.equalsIgnoreCase("TMSA_BILLING_DTL"))
					{
						InsertUpdateTMSAAgreementBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("SO_SVC_CONT"))
					{
						InsertUpdateSOContractDetail(request);
					}
					else if(option.equalsIgnoreCase("SVC_CONTRACT_BILLING_DTL"))
					{
						InsertUpdateContractBillingDetail(request);
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
	
	private void InsertUpdateContractBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateContractBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map ="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
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
			
			String billing_days=request.getParameter("billing_days")==null?"":request.getParameter("billing_days");
			
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
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			String[] cust_plant_map = holidayState_map.split("@@");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_SVC_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
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
							+ "FROM FMS_SVC_CONT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? "
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
						query="UPDATE FMS_SVC_CONT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? "
								+ "AND AGMT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
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
						stmt1.setString(++cnt, cont_no);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.setString(++cnt, eff_dt);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_SVC_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP"
								+ ",PLANT_SEQ_NO,HOLIDAY_STATE,EFF_DT) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'))";
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
						stmt1.setString(++cnt1, exchg_val);
						stmt1.setString(++cnt1, billing_days);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.setString(++cnt1, eff_dt);
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
			
			url = "../dlng/frm_dlng_so_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
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

	private void InsertUpdateSOContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSOContractDetail()";
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
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String dda_dt = request.getParameter("dda_dt")==null?"":request.getParameter("dda_dt");
			String dda_time = request.getParameter("dda_time")==null?"":request.getParameter("dda_time");
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String temp_start_dt = request.getParameter("temp_start_dt")==null?"":request.getParameter("temp_start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String mmcq = request.getParameter("mmcq")==null?"":request.getParameter("mmcq");
			String mmcq_flag = request.getParameter("mmcq_flag")==null?"":request.getParameter("mmcq_flag");
			String mmcq_clause_no = request.getParameter("mmcq_clause_no")==null?"":request.getParameter("mmcq_clause_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_status_flg = request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg");
			String is_allocated = request.getParameter("is_allocated")==null?"N":request.getParameter("is_allocated");
			String change_request = request.getParameter("change_request")==null?"":request.getParameter("change_request");
			String contdt_change_request_flag = request.getParameter("contdt_change_request_flag")==null?"":request.getParameter("contdt_change_request_flag");
			String DealEnterDtTime = ent_dt+" "+ent_time;
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String agmt_no=request.getParameter("agmt_no").equals("")?"0":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no").equals("")?"0":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String billing_clause_no = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"N":request.getParameter("day_def_clause_no");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			String transport_qty = request.getParameter("transport_qty")==null?"":request.getParameter("transport_qty");
			String entry_point = request.getParameter("entry_point")==null?"":request.getParameter("entry_point");
			String exit_point = request.getParameter("exit_point")==null?"":request.getParameter("exit_point");
			String allowed_lay_time_hrs = request.getParameter("allowed_lay_time_hrs")==null?"":request.getParameter("allowed_lay_time_hrs");
			String allowed_lay_time_min = request.getParameter("allowed_lay_time_min")==null?"":request.getParameter("allowed_lay_time_min");
			String layover_charge = request.getParameter("layover_charge")==null?"":request.getParameter("layover_charge");
			String layover_hours = request.getParameter("layover_hours")==null?"":request.getParameter("layover_hours");
			String transport_management_charge = request.getParameter("transport_management_charge")==null?"":request.getParameter("transport_management_charge");
			String transport_management_charge_unit = request.getParameter("transport_management_charge_unit")==null?"":request.getParameter("transport_management_charge_unit");
			String transport_management_charge_eff_dt = request.getParameter("transport_management_charge_eff_dt")==null?"":request.getParameter("transport_management_charge_eff_dt");
			String qty_opt = request.getParameter("qty_opt")==null?"":request.getParameter("qty_opt");
			String qty_opt_frim = request.getParameter("qty_opt_frim")==null?"":request.getParameter("qty_opt_frim");
			String qty_opt_re = request.getParameter("qty_opt_re")==null?"":request.getParameter("qty_opt_re");
			String fcc_flg=request.getParameter("fcc_flg")==null?"":request.getParameter("fcc_flg");
			String sales_cont_map = request.getParameter("sales_cont_map")==null?"":request.getParameter("sales_cont_map");
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			
			if(!fcc_flg.equals("")) 
			{
				cont_status_flg = fcc_flg;
			}
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				
				String year = ent_dt.substring(8,ent_dt.length());
					
				
				int cont = Integer.parseInt(year) * 10000;
				query="SELECT NVL(MAX(CONT_NO),?) "
						+ "FROM FMS_SVC_CONT_MST "
						+ "WHERE CONT_NO LIKE ? AND COMPANY_CD=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setInt(1, cont);
				stmt.setString(2, year+"%");
				stmt.setString(3, comp_cd);
				stmt.setString(4, contract_type);
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
			
			if(change_request.equals("CONTRACT_DATE"))
			{
				cont_rev_no=""+(Integer.parseInt(cont_rev_no)+1);
				
				cont_status_flg="P";
			}
			
			String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+contract_type+cont_no+"-REV"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			cont_type_nm = utilBean.getContractTypeName(contract_type);
			
			if(contract_type.equals("B"))
			{
				cont_name = counterparty_abbr+"-"+comp_abbr+"-TMSA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;			
			}
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_SVC_CONT_MST "
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
					query ="UPDATE FMS_SVC_CONT_MST SET CONT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'),SIGNING_TIME=?,"
							+ "DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),DCQ=?,"
							+ "CONT_STATUS=?,IS_ALLOCATED=?,FILL_STATION_CD=?,PLANT_SEQ_NO=?,"
							+ "ALW_LAYTIME_HRS=?,ALW_LAYTIME_MNS=?,LAYOVER_CHARGE_INR=?,LAYOVER_HRS=?,"
							+ "DAY_DEF_FLAG=?,DAY_START_TIME=?,DAY_END_TIME=?,DAY_DEF_CLAUSE=?,MMCQ_FLAG=?,MMCQ_PERCENTAGE=?,MMCQ_CLAUSE=?,"
							+ "CONT_NAME=?,CONTRACT_TYPE=?,MODIFY_DT=SYSDATE,MODIFY_BY=? ";
					if(contdt_change_request_flag.equals("Y"))
					{
						query+=",CHANGE_DATE_REQ=? ";
					}
					query+=	",BILLING_FLAG=?,BILLING_CLAUSE=?,TRANSPORT_MGMT_CHARGE=?,TRANSPORT_MGMT_UNIT=?,EFF_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "QTY_OPTION=?,QTY_OPTION_FIRM=?,QTY_OPTION_RE=?,FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE ";
					query+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++st_count, cont_ref_no);
					stmt1.setString(++st_count, signing_dt);
					stmt1.setString(++st_count, signing_time);
					stmt1.setString(++st_count, dda_dt);
					stmt1.setString(++st_count, dda_time);
					stmt1.setString(++st_count, start_dt);
					stmt1.setString(++st_count, end_dt);
					stmt1.setString(++st_count, transport_qty);
					stmt1.setString(++st_count, cont_status_flg);
					stmt1.setString(++st_count, is_allocated);
					stmt1.setString(++st_count, entry_point);
					stmt1.setString(++st_count, exit_point);
					stmt1.setString(++st_count, allowed_lay_time_hrs);
					stmt1.setString(++st_count, allowed_lay_time_min);
					stmt1.setString(++st_count, layover_charge);
					stmt1.setString(++st_count, layover_hours);
					stmt1.setString(++st_count, day_def);
					stmt1.setString(++st_count, day_time_from);
					stmt1.setString(++st_count, day_time_to);
					stmt1.setString(++st_count, day_def_clause_no);
					stmt1.setString(++st_count, mmcq_flag);
					stmt1.setString(++st_count, mmcq);
					stmt1.setString(++st_count, mmcq_clause_no);
					stmt1.setString(++st_count, cont_name);
					stmt1.setString(++st_count, contract_type);
					stmt1.setString(++st_count, emp_cd);
					if(contdt_change_request_flag.equals("Y"))
					{
						stmt1.setString(++st_count, "N");
					}
					stmt1.setString(++st_count, billing_flag);
					stmt1.setString(++st_count, billing_clause_no);
					stmt1.setString(++st_count, transport_management_charge);
					stmt1.setString(++st_count, transport_management_charge_unit);
					stmt1.setString(++st_count, transport_management_charge_eff_dt);
					stmt1.setString(++st_count, qty_opt);
					stmt1.setString(++st_count, qty_opt_frim);
					stmt1.setString(++st_count, qty_opt_re);
					stmt1.setString(++st_count, fcc_flg);
					stmt1.setString(++st_count, emp_cd);
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
					query = "INSERT INTO FMS_SVC_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,"
							+ "START_DT,END_DT,DCQ,CONT_STATUS,IS_ALLOCATED,FILL_STATION_CD,PLANT_SEQ_NO,"
							+ "ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,LAYOVER_CHARGE_INR,LAYOVER_HRS,"
							+ "DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DAY_DEF_CLAUSE,MMCQ_FLAG,MMCQ_PERCENTAGE,MMCQ_CLAUSE,"
							+ "ENT_DT,ENT_BY ";
					if(change_request.equals("CONTRACT_DATE"))
					{
						query+=",CHANGE_DATE_REQ";
					}
					query+= ",BILLING_FLAG,BILLING_CLAUSE,TRANSPORT_MGMT_CHARGE,TRANSPORT_MGMT_UNIT,EFF_DT,QTY_OPTION,QTY_OPTION_FIRM,QTY_OPTION_RE ) ";
					query+= "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,"
							+ "?,?,?,?,"
							+ "?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY HH24:MI'),? ";
					if(change_request.equals("CONTRACT_DATE"))
					{
						query+=",?";
					}
					query+= ",?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?) ";
					
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, counterparty_cd);
					stmt1.setString(++count, agmt_no);
					stmt1.setString(++count, agmt_rev_no);
					stmt1.setString(++count, cont_no);
					stmt1.setString(++count, cont_rev_no);
					stmt1.setString(++count, contract_type);
					stmt1.setString(++count, cont_name);
					stmt1.setString(++count, cont_ref_no);
					stmt1.setString(++count, dda_dt);
					stmt1.setString(++count, dda_time);
					stmt1.setString(++count, signing_dt);
					stmt1.setString(++count, signing_time);
					stmt1.setString(++count, start_dt);
					stmt1.setString(++count, end_dt);
					stmt1.setString(++count, transport_qty);
					stmt1.setString(++count, cont_status_flg);
					stmt1.setString(++count, is_allocated);
					stmt1.setString(++count, entry_point);
					stmt1.setString(++count, exit_point);
					stmt1.setString(++count, allowed_lay_time_hrs);
					stmt1.setString(++count, allowed_lay_time_min);
					stmt1.setString(++count, layover_charge);
					stmt1.setString(++count, layover_hours);
					stmt1.setString(++count, day_def);
					stmt1.setString(++count, day_time_from);
					stmt1.setString(++count, day_time_to);
					stmt1.setString(++count, day_def_clause_no);
					stmt1.setString(++count, mmcq_flag);
					stmt1.setString(++count, mmcq);
					stmt1.setString(++count, mmcq_clause_no);
					stmt1.setString(++count, DealEnterDtTime);
					stmt1.setString(++count, emp_cd);
					if(change_request.equals("CONTRACT_DATE"))
					{
						stmt1.setString(++count, "Y");
					}
					stmt1.setString(++count, billing_flag);
					stmt1.setString(++count, billing_clause_no);
					stmt1.setString(++count, transport_management_charge);
					stmt1.setString(++count, transport_management_charge_unit);
					stmt1.setString(++count, transport_management_charge_eff_dt);
					stmt1.setString(++count, qty_opt);
					stmt1.setString(++count, qty_opt_frim);
					stmt1.setString(++count, qty_opt_re);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					/*if(billing_flag.equals("Y") && !change_request.equals("CONTRACT_DATE") && !contdt_change_request_flag.equals("Y"))
					{
						query="INSERT INTO FMS_SVC_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
								+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
								+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,?,?,?,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
								+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
								+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,SYSDATE,?,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS "
								+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND "
								+ "EXISTS("
								+ "SELECT * FROM "
								+ "FMS_AGMT_SVC_BILLING_DTL B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ ")";
						stmt3 = dbcon.prepareStatement(query);
						stmt3.setString(1, cont_no);
						stmt3.setString(2, cont_rev_no);
						stmt3.setString(3, contract_type);
						stmt3.setString(4, emp_cd);
						stmt3.setString(5, comp_cd);
						stmt3.setString(6, counterparty_cd);
						stmt3.setString(7, "T");
						stmt3.setString(8, agmt_no);
						stmt3.setString(9, agmt_rev_no);
						stmt3.executeUpdate();
						
						stmt3.close();
					}*/
				}
				
				if(!customer_cd.equals("") && !sales_cont_map.equals(""))
				{
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_SVC_CONT_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					stmt1.setString(7, contract_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(count>0)
					{
						query="UPDATE FMS_SVC_CONT_MAP SET CUSTOMER_CD=?,SELL_CONT_MAP=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=?";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, customer_cd);
						stmt2.setString(2, sales_cont_map);
						stmt2.setString(3, comp_cd);
						stmt2.setString(4, counterparty_cd);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, agmt_no);
						stmt2.setString(8, agmt_rev_no);
						stmt2.setString(9, contract_type);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					else
					{
						
						query="INSERT INTO FMS_SVC_CONT_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE)";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, customer_cd);
						stmt2.setString(9, sales_cont_map);
						stmt2.setString(10, emp_cd);
						
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				
				int count=0;
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_SVC_CONT_BU "
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
					query = "DELETE FROM FMS_SVC_CONT_BU "
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
						query = "INSERT INTO FMS_SVC_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
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
			
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			new_value ="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#DCQ="+transport_qty+"#MMCQ="+mmcq+"#CONT_STATUS="+cont_status;
			
			url = "../dlng/frm_dlng_cont_service_order.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+
					"&contract_type="+contract_type+commonUrl_pra;
			
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Contract "+cont_name_map+" Insert/Update Failed!";
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
	
	private void InsertUpdateTMSAAgreementBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateTMSAAgreementBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map ="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
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
			
			String billing_days=request.getParameter("billing_days")==null?"":request.getParameter("billing_days");
			
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
						+ "FROM FMS_AGMT_SVC_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
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
							+ "FROM FMS_AGMT_SVC_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
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
						query="UPDATE FMS_AGMT_SVC_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=?  "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=? ";
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
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_AGMT_SVC_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
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
			
			url = "../dlng/frm_dlng_tmsa_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
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
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals("")) 
			{
				status="A";
			}
			String[] chk_plant = request.getParameterValues("chk_plant");
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String mmcq_flag = request.getParameter("mmcq_flag")==null?"N":request.getParameter("mmcq_flag");
			String mmcq_percent = request.getParameter("mmcq_percent")==null?"":request.getParameter("mmcq_percent");
			String mmcq_clause_no = request.getParameter("mmcq_clause_no")==null?"":request.getParameter("mmcq_clause_no");
			String billing_flag = request.getParameter("billing_flag")==null?"N":request.getParameter("billing_flag");
			String billing_clause_no = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_clause_no = request.getParameter("day_clause_no")==null?"":request.getParameter("day_clause_no");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			if(opration.equals("INSERT"))
			{
				query="SELECT MAX(AGMT_NO) FROM FMS_AGMT_SVC_MST "
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
					query="SELECT MAX(AGMT_REV) "
							+ "FROM FMS_AGMT_SVC_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_type);
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
			
			String cont_name = comp_abbr+"-"+counterparty_abbr+"-"+agmt_type+agmt_no+"-REV"+agmt_rev_no;
			cont_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agmt_type);
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_type.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_AGMT_SVC_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? ";
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
					int cnt=0;
					query ="UPDATE FMS_AGMT_SVC_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,MMCQ_FLAG=?,MMCQ_PERCENTAGE=?,"
							+ "AGMT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),DAY_DEF_CLAUSE=?,"
							+ "MMCQ_CLAUSE=?,BILLING_CLAUSE=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++cnt, agmt_ref_no);
					stmt0.setString(++cnt, signing_dt);
					stmt0.setString(++cnt, start_dt);
					stmt0.setString(++cnt, end_dt);
					stmt0.setString(++cnt, day_def);
					stmt0.setString(++cnt, day_time_from);
					stmt0.setString(++cnt, day_time_to);
					stmt0.setString(++cnt, mmcq_flag);
					stmt0.setString(++cnt, mmcq_percent);
					stmt0.setString(++cnt, cont_name);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, day_clause_no);
					stmt0.setString(++cnt, mmcq_clause_no);
					stmt0.setString(++cnt, billing_clause_no);
					stmt0.setString(++cnt, comp_cd);
					stmt0.setString(++cnt, counterparty_cd);
					stmt0.setString(++cnt, agmt_no);
					stmt0.setString(++cnt, agmt_rev_no);
					stmt0.setString(++cnt, agmt_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int cnt=0;
					query = "INSERT INTO FMS_AGMT_SVC_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_REF_NO,SIGNING_DT,"
							+ "START_DT,END_DT,STATUS, "
							+ "DAY_DEF,DAY_START_TIME,DAY_END_TIME,MMCQ_FLAG,MMCQ_PERCENTAGE,"
							+ "ENT_DT,ENT_BY,AGMT_NAME,BILLING_FLAG,REV_DT,"
							+ "DAY_DEF_CLAUSE,MMCQ_CLAUSE,BILLING_CLAUSE) "
							+ "VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,?,"
							+ "SYSDATE,?,?,?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?)";
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
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, day_def);
					stmt0.setString(++cnt, day_time_from);
					stmt0.setString(++cnt, day_time_to);
					stmt0.setString(++cnt, mmcq_flag);
					stmt0.setString(++cnt, mmcq_percent);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, cont_name);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, day_clause_no);
					stmt0.setString(++cnt, mmcq_clause_no);
					stmt0.setString(++cnt, billing_clause_no);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				
				int count=0;
				//CUSTOMER PLANT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_AGMT_SVC_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					 count = rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_AGMT_SVC_PLANT "
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
						query = "INSERT INTO FMS_AGMT_SVC_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
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
						+ "FROM FMS_AGMT_SVC_BU "
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
					query = "DELETE FROM FMS_AGMT_SVC_BU "
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
						query = "INSERT INTO FMS_AGMT_SVC_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
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
			
			url = "../dlng/frm_dlng_tmsa_agmt.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+commonUrl_pra;
			
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
}
