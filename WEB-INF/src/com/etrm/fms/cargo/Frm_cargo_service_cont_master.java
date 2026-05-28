package com.etrm.fms.cargo;

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

@WebServlet("/servlet/Frm_cargo_service_cont_master")
public class Frm_cargo_service_cont_master extends HttpServlet
{
	static String frm_src_file_name="Frm_cargo_service_cont_master.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_cargo_service_cont_master";
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
	private static String query3 = null;
	private static String query4 = null;
	private static String query5 = null;
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
					
					if(option.equalsIgnoreCase("CARGO_SVC_CONT_MST"))
					{
						InsertUpdateCargoSvcContractDetail(request);
					}
					else if(option.equalsIgnoreCase("SVC_BILLING_DTL"))
					{
						InsertUpdateSvcBillingDetail(request);
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
	
	private void InsertUpdateSvcBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateSvcBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String entity_type=request.getParameter("entity_type")==null?"":request.getParameter("entity_type");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String final_rate_unit=request.getParameter("final_rate_unit")==null?"2":request.getParameter("final_rate_unit");
			String provisional_rate_unit=request.getParameter("provisional_rate_unit")==null?"2":request.getParameter("provisional_rate_unit");
			
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
			
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !cont_no.equals("") && !entity_type.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_CARGO_SVC_CONT_SVC_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND ENTITY_TYPE=? AND CONTRACT_TYPE=? ";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, cont_no);
				stmt3.setString(4, entity_type);
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
							+ "FROM FMS_CARGO_SVC_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND ENTITY_TYPE=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					stmt.setString(4, entity_type);
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
						query1="UPDATE FMS_CARGO_SVC_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND ENTITY_TYPE=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
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
						stmt1.setString(++cnt, entity_type);
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
						query1="INSERT INTO FMS_CARGO_SVC_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,CONT_NO,ENTITY_TYPE,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, cont_no);
						stmt1.setString(++cnt1, entity_type);
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
			
			url = "../cargo/frm_svc_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&provisional_rate_unit="+provisional_rate_unit+"&final_rate_unit="+final_rate_unit+
					"&cont_no="+cont_no+"&entity_type="+entity_type+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! CN Billing Details Insert/Update Failed";
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
	
	private void InsertUpdateCargoSvcContractDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCargoSvcContractDetail()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String entity_type=request.getParameter("entity_type")==null?"":request.getParameter("entity_type");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String contratct_number = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_status=request.getParameter("cont_status")==null?"Y":request.getParameter("cont_status");
			String prov_svc_rate=request.getParameter("prov_svc_rate")==null?"":request.getParameter("prov_svc_rate");
			String prov_svc_rate_unit1=request.getParameter("prov_svc_rate_unit1")==null?"":request.getParameter("prov_svc_rate_unit1");
			String prov_svc_rate_unit2=request.getParameter("prov_svc_rate_unit2")==null?"":request.getParameter("prov_svc_rate_unit2");
			String final_svc_rate=request.getParameter("final_svc_rate")==null?"":request.getParameter("final_svc_rate");
			String final_svc_rate_unit1=request.getParameter("final_svc_rate_unit1")==null?"":request.getParameter("final_svc_rate_unit1");
			String final_svc_rate_unit2=request.getParameter("final_svc_rate_unit2")==null?"":request.getParameter("final_svc_rate_unit2");
			String signing_dt=request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time=request.getParameter("signing_time")==null?"00:00":request.getParameter("signing_time");

			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String[] chk_plant = request.getParameterValues("chk_plant");
			
			String entity_name ="";
			String contract_type = "";
			
			if(entity_type.equals("S"))
			{
				entity_name="Surveyor Agent";
				contract_type="Y";
			}
			else if(entity_type.equals("V"))
			{
				entity_name="Vessel Agent";
				contract_type="A";
			}
			else if(entity_type.equals("H"))
			{
				entity_name="CH Agent";
				contract_type="H";
			}
			
			if(opration.equals("INSERT"))
			{
				String year ="";
				
				if(entity_type.equals("S"))
				{
					year = "3"+ent_dt.substring(8,ent_dt.length());
				}
				else if(entity_type.equals("V"))
				{
					year = "2"+ent_dt.substring(8,ent_dt.length());
				}
				else if(entity_type.equals("H"))
				{
					year = "1"+ent_dt.substring(8,ent_dt.length());
				}
				
				int cont = Integer.parseInt(year) * 1000;
				
				query="SELECT MAX(CONT_NO) FROM FMS_CARGO_SVC_CONT_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND ENTITY_TYPE=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, entity_type);
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
			}	
			
			String cont_name = counterparty_abbr+"-"+comp_abbr+"-"+contract_type+cont_no;
			
			//DISPLAY CONTRACT NAME
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !cont_no.equals("") && !entity_type.equals(""))
			{
				if(opration.equals("MODIFY"))
				{
					int updateCnt=0;
					
					query="UPDATE FMS_CARGO_SVC_CONT_MST SET CONT_REF_NO=?,START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "PROV_SVC_RATE=?,PROV_SVC_RATE_UNIT1=?,PROV_SVC_RATE_UNIT2=?,"
							+ "FINAL_SVC_RATE=?,FINAL_SVC_RATE_UNIT1=?,FINAL_SVC_RATE_UNIT2=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,CONT_STATUS=?,"
							+ "SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), SIGNING_TIME=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++updateCnt, cont_ref_no);
					stmt1.setString(++updateCnt, start_dt);
					stmt1.setString(++updateCnt, end_dt);
					stmt1.setString(++updateCnt, prov_svc_rate);
					stmt1.setString(++updateCnt, prov_svc_rate_unit1);
					stmt1.setString(++updateCnt, prov_svc_rate_unit2);
					stmt1.setString(++updateCnt, final_svc_rate);
					stmt1.setString(++updateCnt, final_svc_rate_unit1);
					stmt1.setString(++updateCnt, final_svc_rate_unit2);
					stmt1.setString(++updateCnt, emp_cd);
					stmt1.setString(++updateCnt, cont_status);
					stmt1.setString(++updateCnt, signing_dt);
					stmt1.setString(++updateCnt, signing_time);
					stmt1.setString(++updateCnt, comp_cd);
					stmt1.setString(++updateCnt, counterparty_cd);
					stmt1.setString(++updateCnt, entity_type);
					stmt1.setString(++updateCnt, contratct_number);
					stmt1.setString(++updateCnt, contract_type);
					stmt1.executeUpdate();
					stmt1.close();
				}
				else if(opration.equals("INSERT"))
				{
					int insCnt=0;
					
					query ="INSERT INTO FMS_CARGO_SVC_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,CONT_REF_NO,"
							+ "CONT_NAME,CONT_STATUS,START_DT,END_DT,PROV_SVC_RATE,PROV_SVC_RATE_UNIT1,PROV_SVC_RATE_UNIT2,"
							+ "FINAL_SVC_RATE,FINAL_SVC_RATE_UNIT1,FINAL_SVC_RATE_UNIT2,ENT_DT,ENT_BY,SIGNING_DT,SIGNING_TIME) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,SYSDATE,?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, counterparty_cd);
					stmt1.setString(++insCnt, entity_type);
					stmt1.setString(++insCnt, cont_no);
					stmt1.setString(++insCnt, contract_type);
					stmt1.setString(++insCnt, cont_ref_no);
					stmt1.setString(++insCnt, cont_name);
					stmt1.setString(++insCnt, cont_status);
					stmt1.setString(++insCnt, start_dt);
					stmt1.setString(++insCnt, end_dt);
					stmt1.setString(++insCnt, prov_svc_rate);
					stmt1.setString(++insCnt, prov_svc_rate_unit1);
					stmt1.setString(++insCnt, prov_svc_rate_unit2);
					stmt1.setString(++insCnt, final_svc_rate);
					stmt1.setString(++insCnt, final_svc_rate_unit1);
					stmt1.setString(++insCnt, final_svc_rate_unit2);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, signing_dt);
					stmt1.setString(++insCnt, signing_time);
					stmt1.executeUpdate();
					stmt1.close();
				}
				
				
				
				//ENTITY PLANT
				query1 = "DELETE FROM FMS_CARGO_SVC_CONT_SVC_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ENTITY_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_CARGO_SVC_CONT_SVC_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE AND  "
						+ "A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE )";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, entity_type);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, contract_type);
				rset1 = stmt1.executeQuery();
				
				rset1.close();
				stmt1.close();
				
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_CARGO_SVC_CONT_SVC_BU(COMPANY_CD,"
								+ "COUNTERPARTY_CD,"
								+ "ENTITY_TYPE,"
								+ "CONT_NO,"
								+ "CONTRACT_TYPE,"
								+ "PLANT_SEQ_NO,"
								+ "ENT_BY,"
								+ "ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, entity_type);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//BUSINESS UNIT
				query1= "DELETE FROM FMS_CARGO_SVC_CONT_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ENTITY_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_CARGO_SVC_CONT_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE AND A.CONT_NO=B.CONT_NO  "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE )";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, entity_type);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, contract_type);
				rset1 = stmt1.executeQuery();
				
				rset1.close();
				stmt1.close();
				
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_CARGO_SVC_CONT_BU(COMPANY_CD,"
								+ "COUNTERPARTY_CD,"
								+ "ENTITY_TYPE,"
								+ "CONT_NO,"
								+ "CONTRACT_TYPE,"
								+ "PLANT_SEQ_NO,"
								+ "ENT_BY,"
								+ "ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE)";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, entity_type);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Service Contract "+cont_name_map+" for "+counterparty_abbr+" Added Successfully!";
					msg_type="S";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Service Contract "+cont_name_map+" for "+counterparty_abbr+" Modified Successfully!";
					msg_type="S";
				}
			}
			else
			{
				msg = "Failed! - Contract "+cont_name_map+" for "+counterparty_abbr+" Insertion/Modification Failed!";
				msg_type="E";
			}
			
			url = "../cargo/frm_cargo_service_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&entity_type="+entity_type+"&contract_type="+contract_type+"&cont_no="+cont_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Cargo Service Contract Detail submission Failed!";
		}
	}
}
