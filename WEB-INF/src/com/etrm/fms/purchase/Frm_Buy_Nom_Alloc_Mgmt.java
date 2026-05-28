package com.etrm.fms.purchase;

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

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 18/10/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_Buy_Nom_Alloc_Mgmt")
public class Frm_Buy_Nom_Alloc_Mgmt extends HttpServlet
{
	static String db_src_file_name="Frm_Buy_Nom_Alloc_Mgmt.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Buy_Nom_Alloc_Mgmt";
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
					
					if(option.equalsIgnoreCase("BUYER_NOM"))
					{
						InsertUpdateBuyerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("WEEKLY_BUYER_NOM"))
					{
						InsertUpdateWeeklyBuyerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("SELLER_NOM"))
					{
						InsertUpdateSellerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("WEEKLY_SELLER_NOM"))
					{
						InsertUpdateWeeklySellerNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("DAILY_ALLOC"))
					{
						InsertUpdateDailyAllocationDetail(request);
					}
					else if(option.equalsIgnoreCase("PERIODIC_ALLOC"))
					{
						InsertUpdatePeriodicAllocationDetail(request);
					}
					else if(option.equalsIgnoreCase("DAILY_ALLOC_BKP"))//NOT IN USED
					{
						InsertUpdateDailyAllocationDetail_BKP(request);
					}
					else if(option.equalsIgnoreCase("BUYER_NOM_TO_TRD_RMK"))
					{
						InsertUpdateBuyerNomToTrdRmk(request);
					}
					else if(option.equalsIgnoreCase("SEND_MAIL"))
					{
						SendMail(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				url=CommonVariable.errorpage_url+"?e="+e;
				msg = "Error in Exception !";
			}
			finally
			{
				if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
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
	
	private void SendMail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="SendMail()";
		try
		{
			String[] email_from = request.getParameterValues("email_from");
			String[] email_to = request.getParameterValues("email_to");
			String[] email_cc = request.getParameterValues("email_cc");
			String[] subject = request.getParameterValues("subject");
			String[] attachment = request.getParameterValues("attachment");
			String[] email_body = request.getParameterValues("email_body");
			
			if(email_to != null)
			{
				for(int i=0;i<email_to.length;i++)
				{
					email_body[i]=email_body[i].replaceAll("\n", "<br>");
					email_body[i]="<html>"
							+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body[i]+"</span>"
							+ "</html>";
					
					if(!email_to[i].equals("") && !email_body[i].equals(""))
					{
						mailDelv.sendMail(comp_cd,email_to[i], subject[i], email_body[i], attachment[i], email_cc[i], "");
					}
					
					msg="Mail Sent...";
					msg_type="S";
				}
			}
			
			url = "../purchase/frm_buy_nom_to_trader_send_mail.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
		}
	}

	private void InsertUpdateBuyerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateBuyerNominationDetail()";
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			String[] cargo_no=request.getParameterValues("cargo_no");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					if(cargo_no[i].equals("")) 
					{
						cargo_no[i] = "0";
					}
					
					int rev_no=-1;
					String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_BUY_DAILY_BUYER_NOM "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no[i]);
					stmt.setString(2, agmt_no[i]);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd[i]);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, trans_plantseq[i]);
					stmt.setString(7, plant_seq[i]);
					stmt.setString(8, contract_type[i]);
					stmt.setString(9, gas_dt);
					stmt.setString(10, bu_plant_seq[i]);
					stmt.setString(11, cargo_no[i]);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						rev_no = rset.getInt(1)+1;
					}
					else
					{
						rev_no = rev_no + 1;
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_BUY_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd[i]);
					stmt1.setString(3, agmt_no[i]);
					stmt1.setString(4, agmt_rev_no[i]);
					stmt1.setString(5, cont_no[i]);
					stmt1.setString(6, cont_rev_no[i]);
					stmt1.setInt(7, rev_no);
					stmt1.setString(8, plant_seq[i]);
					stmt1.setString(9, trans_cd[i]);
					stmt1.setString(10, trans_plantseq[i]);
					stmt1.setString(11, gas_dt);
					stmt1.setString(12, contract_type[i]);
					stmt1.setString(13, gen_dt);
					stmt1.setString(14, gen_time[i]);
					stmt1.setString(15, base[i]);
					stmt1.setString(16, gcv[i]);
					stmt1.setString(17, ncv[i]);
					stmt1.setString(18, qty_mmbtu[i]);
					stmt1.setString(19, qty_scm[i]);
					stmt1.setString(20, emp_cd);
					stmt1.setString(21, bu_plant_seq[i]);
					stmt1.setString(22, cargo_no[i]);
					stmt1.executeQuery();
					
					stmt1.close();
				}
			}
			
			msg = "Successful! - Buyer Nomination Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/frm_buy_daily_buyer_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
	
	private void InsertUpdateWeeklyBuyerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateWeeklyBuyerNominationDetail()";
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String nomination_freq = request.getParameter("nomination_freq")==null?"":request.getParameter("nomination_freq");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
			
			String[] week_gas_dt = request.getParameterValues("week_gas_dt");
			String[] index = request.getParameterValues("index");
			String[] ivalue = request.getParameterValues("ivalue");
			
			if(week_gas_dt!=null)
			{
				for(int i=0; i<week_gas_dt.length; i++)
				{
					for(int j=1; j<=Integer.parseInt(index[i]); j++)
					{
						String bu_plant_seq=request.getParameter("bu_plant_seq"+ivalue[i]+""+j)==null?"0":request.getParameter("bu_plant_seq"+ivalue[i]+""+j);
						String[] buPl = request.getParameterValues("bu_plant_seq"+ivalue[i]+""+j); 
						if(buPl != null)
						{
							String index1 = request.getParameter("index1"+ivalue[i]+""+j)==null?"0":request.getParameter("index1"+ivalue[i]+""+j);
							for(int m=1; m<=Integer.parseInt(index1); m++)
							{
								String[] plant=request.getParameterValues("plant_seq"+ivalue[i]+""+j+""+m);
								if(plant != null)
								{
									String gen_dt = request.getParameter("gen_dt"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gen_dt"+ivalue[i]+""+j+""+m);
									String gen_time = request.getParameter("gen_time"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gen_time"+ivalue[i]+""+j+""+m);
									String base = request.getParameter("base"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("base"+ivalue[i]+""+j+""+m);
									String gcv = request.getParameter("gcv"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gcv"+ivalue[i]+""+j+""+m);
									String ncv = request.getParameter("ncv"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("ncv"+ivalue[i]+""+j+""+m);
									String qty_mmbtu = request.getParameter("qty_mmbtu"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("qty_mmbtu"+ivalue[i]+""+j+""+m);
									String qty_scm = request.getParameter("qty_scm"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("qty_scm"+ivalue[i]+""+j+""+m);
									
									String trans_cd=request.getParameter("transporter_cd"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("transporter_cd"+ivalue[i]+""+j+""+m);
									String trans_plantseq=request.getParameter("transporter_plant_seq"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("transporter_plant_seq"+ivalue[i]+""+j+""+m);
									String plant_seq=request.getParameter("plant_seq"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("plant_seq"+ivalue[i]+""+j+""+m);
									
									
									int rev_no=-1;
									String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
											+ "FROM FMS_BUY_DAILY_BUYER_NOM "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
											+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND BU_SEQ=?"
											+ " AND CARGO_NO=?";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, cont_no);
									stmt.setString(2, agmt_no);
									stmt.setString(3, comp_cd);
									stmt.setString(4, counterparty_cd);
									stmt.setString(5, trans_cd);
									stmt.setString(6, trans_plantseq);
									stmt.setString(7, plant_seq);
									stmt.setString(8, contract_type);
									stmt.setString(9, week_gas_dt[i]);
									stmt.setString(10, bu_plant_seq);
									stmt.setString(11, cargo_no);
									rset = stmt.executeQuery();
									if(rset.next())
									{
										rev_no = rset.getInt(1)+1;
									}
									else
									{
										rev_no = rev_no + 1;
									}
									rset.close();
									stmt.close();
									
									query1="INSERT INTO FMS_BUY_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
											+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
											+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO) "
											+ "VALUES(?,?,?,?,?,?,?,?,"
											+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
											+ "?,?,?,?,?,SYSDATE,?)"; // JD: Temporary fix to address no break
									stmt1=dbcon.prepareStatement(query1);
									stmt1.setString(1, comp_cd);
									stmt1.setString(2, counterparty_cd);
									stmt1.setString(3, agmt_no);
									stmt1.setString(4, agmt_rev_no);
									stmt1.setString(5, cont_no);
									stmt1.setString(6, cont_rev_no);
									stmt1.setInt(7, rev_no);
									stmt1.setString(8, plant_seq);
									stmt1.setString(9, trans_cd);
									stmt1.setString(10, trans_plantseq);
									stmt1.setString(11, bu_plant_seq);
									stmt1.setString(12, week_gas_dt[i]);
									stmt1.setString(13, contract_type);
									stmt1.setString(14, gen_dt);
									stmt1.setString(15, gen_time);
									stmt1.setString(16, base);
									stmt1.setString(17, gcv);
									stmt1.setString(18, ncv);
									stmt1.setString(19, qty_mmbtu);
									stmt1.setString(20, qty_scm);
									stmt1.setString(21, emp_cd);
									stmt1.setString(22, cargo_no);
									
									stmt1.executeQuery();
									
									stmt1.close();
								}
							}
						}
					}
				}
			}
			
			if(nomination_freq.equals("W")) {
				msg = "Successful! - Weekly Buyer Nomination Submitted Successfully!";
			}else if(nomination_freq.equals("M")) {
				msg = "Successful! - Monthly Buyer Nomination Submitted Successfully!";
			}else if(nomination_freq.equals("F")) {
				msg = "Successful! - Fortnightly Buyer Nomination Submitted Successfully!";
			}
			msg_type="S";
			
			url = "../purchase/frm_buy_weekly_buyer_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
					"&clearance="+clearance+"&from_dt="+from_dt+"&to_dt="+to_dt+"&cargo_no="+cargo_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
	
	private void InsertUpdateSellerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateSellerNominationDetail()";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] cargo_no = request.getParameterValues("cargo_no");
			String[] dis_cont_mapping = request.getParameterValues("dis_cont_mapping");
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					if(cargo_no[i].equals("")) 
					{
						cargo_no[i] = "0";
					}
					
					int rev_no=-1;
					String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_BUY_DAILY_SELLER_NOM "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no[i]);
					stmt.setString(2, agmt_no[i]);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd[i]);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, trans_plantseq[i]);
					stmt.setString(7, plant_seq[i]);
					stmt.setString(8, contract_type[i]);
					stmt.setString(9, gas_dt);
					stmt.setString(10, bu_plant_seq[i]);
					stmt.setString(11, cargo_no[i]);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						rev_no = rset.getInt(1)+1;
					}
					else
					{
						rev_no = rev_no + 1;
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_BUY_DAILY_SELLER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd[i]);
					stmt1.setString(3, agmt_no[i]);
					stmt1.setString(4, agmt_rev_no[i]);
					stmt1.setString(5, cont_no[i]);
					stmt1.setString(6, cont_rev_no[i]);
					stmt1.setInt(7, rev_no);
					stmt1.setString(8, plant_seq[i]);
					stmt1.setString(9, trans_cd[i]);
					stmt1.setString(10, trans_plantseq[i]);
					stmt1.setString(11, gas_dt);
					stmt1.setString(12, contract_type[i]);
					stmt1.setString(13, gen_dt);
					stmt1.setString(14, gen_time[i]);
					stmt1.setString(15, base[i]);
					stmt1.setString(16, gcv[i]);
					stmt1.setString(17, ncv[i]);
					stmt1.setString(18, qty_mmbtu[i]);
					stmt1.setString(19, qty_scm[i]);
					stmt1.setString(20, emp_cd);
					stmt1.setString(21, bu_plant_seq[i]);
					stmt1.setString(22, cargo_no[i]);
					stmt1.executeQuery();
					
					stmt1.close();
				}
			}
			
			msg = "Successful! - Seller Nomination Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/frm_buy_daily_seller_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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

	private void InsertUpdateWeeklySellerNominationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateWeeklySellerNominationDetail()";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String nomination_freq = request.getParameter("nomination_freq")==null?"":request.getParameter("nomination_freq");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
			
			String[] week_gas_dt = request.getParameterValues("week_gas_dt");
			String[] index = request.getParameterValues("index");
			String[] ivalue = request.getParameterValues("ivalue");
			
			if(week_gas_dt!=null)
			{
				for(int i=0; i<week_gas_dt.length; i++)
				{
					for(int j=1; j<=Integer.parseInt(index[i]); j++)
					{
						String bu_plant_seq=request.getParameter("bu_plant_seq"+ivalue[i]+""+j)==null?"0":request.getParameter("bu_plant_seq"+ivalue[i]+""+j);
						String[] buPl = request.getParameterValues("bu_plant_seq"+ivalue[i]+""+j); 
						if(buPl != null)
						{
							String index1 = request.getParameter("index1"+ivalue[i]+""+j)==null?"0":request.getParameter("index1"+ivalue[i]+""+j);
							for(int m=1; m<=Integer.parseInt(index1); m++)
							{
								String[] plant=request.getParameterValues("plant_seq"+ivalue[i]+""+j+""+m);
								if(plant != null)
								{
									String gen_dt = request.getParameter("gen_dt"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gen_dt"+ivalue[i]+""+j+""+m);
									String gen_time = request.getParameter("gen_time"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gen_time"+ivalue[i]+""+j+""+m);
									String base = request.getParameter("base"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("base"+ivalue[i]+""+j+""+m);
									String gcv = request.getParameter("gcv"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("gcv"+ivalue[i]+""+j+""+m);
									String ncv = request.getParameter("ncv"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("ncv"+ivalue[i]+""+j+""+m);
									String qty_mmbtu = request.getParameter("qty_mmbtu"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("qty_mmbtu"+ivalue[i]+""+j+""+m);
									String qty_scm = request.getParameter("qty_scm"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("qty_scm"+ivalue[i]+""+j+""+m);
									
									String trans_cd=request.getParameter("transporter_cd"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("transporter_cd"+ivalue[i]+""+j+""+m);
									String trans_plantseq=request.getParameter("transporter_plant_seq"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("transporter_plant_seq"+ivalue[i]+""+j+""+m);
									String plant_seq=request.getParameter("plant_seq"+ivalue[i]+""+j+""+m)==null?"":request.getParameter("plant_seq"+ivalue[i]+""+j+""+m);
									
									
									int rev_no=-1;
									String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
											+ "FROM FMS_BUY_DAILY_SELLER_NOM "
											+ "WHERE CONT_NO=? AND AGMT_NO=? "
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
											+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
											+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=?  AND CARGO_NO=?";
									stmt=dbcon.prepareStatement(query);
									stmt.setString(1, cont_no);
									stmt.setString(2, agmt_no);
									stmt.setString(3, comp_cd);
									stmt.setString(4, counterparty_cd);
									stmt.setString(5, trans_cd);
									stmt.setString(6, trans_plantseq);
									stmt.setString(7, plant_seq);
									stmt.setString(8, contract_type);
									stmt.setString(9, week_gas_dt[i]);
									stmt.setString(10, bu_plant_seq);
									stmt.setString(11, cargo_no);
									rset = stmt.executeQuery();
									if(rset.next())
									{
										rev_no = rset.getInt(1)+1;
									}
									else
									{
										rev_no = rev_no + 1;
									}
									rset.close();
									stmt.close();
									
									query1="INSERT INTO FMS_BUY_DAILY_SELLER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
											+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
											+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO) "
											+ "VALUES(?,?,?,?,?,?,?,?,"
											+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
											+ "?,?,?,?,?,SYSDATE,?)"; // JD: Temporary fix to address no break
									stmt1=dbcon.prepareStatement(query1);
									stmt1.setString(1, comp_cd);
									stmt1.setString(2, counterparty_cd);
									stmt1.setString(3, agmt_no);
									stmt1.setString(4, agmt_rev_no);
									stmt1.setString(5, cont_no);
									stmt1.setString(6, cont_rev_no);
									stmt1.setInt(7, rev_no);
									stmt1.setString(8, plant_seq);
									stmt1.setString(9, trans_cd);
									stmt1.setString(10, trans_plantseq);
									stmt1.setString(11, bu_plant_seq);
									stmt1.setString(12, week_gas_dt[i]);
									stmt1.setString(13, contract_type);
									stmt1.setString(14, gen_dt);
									stmt1.setString(15, gen_time);
									stmt1.setString(16, base);
									stmt1.setString(17, gcv);
									stmt1.setString(18, ncv);
									stmt1.setString(19, qty_mmbtu);
									stmt1.setString(20, qty_scm);
									stmt1.setString(21, emp_cd);
									stmt1.setString(22, cargo_no);
									
									stmt1.executeQuery();
									
									stmt1.close();
								}
							}
						}
					}
				}
			}
			
			if(nomination_freq.equals("W")) {
				msg = "Successful! - Weekly Seller Nomination Submitted Successfully!";
			}else if(nomination_freq.equals("M")) {
				msg = "Successful! - Monthly Seller Nomination Submitted Successfully!";
			}else if(nomination_freq.equals("F")) {
				msg = "Successful! - Fortnightly Seller Nomination Submitted Successfully!";
			}
			msg_type="S";
			
			url = "../purchase/frm_buy_weekly_seller_nom.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
					"&clearance="+clearance+"&from_dt="+from_dt+"&to_dt="+to_dt+"&cargo_no="+cargo_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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

	private void InsertUpdateDailyAllocationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateDailyAllocationDetail()";
		
		try
		{
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			String gen_dt = request.getParameter("gen_dt")==null?"":request.getParameter("gen_dt");
			
			String[] counterparty_cd=request.getParameterValues("counterparty_cd");
			String[] agmt_no=request.getParameterValues("agmt_no");
			String[] agmt_rev_no=request.getParameterValues("agmt_rev_no");
			String[] cont_no=request.getParameterValues("cont_no");
			String[] cont_rev_no=request.getParameterValues("cont_rev_no");
			String[] contract_type=request.getParameterValues("contract_type");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			String[] cargo_no = request.getParameterValues("cargo_no");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			
			if(counterparty_cd != null)
			{
				for(int i=0; i<counterparty_cd.length; i++)
				{
					if(cargo_no[i].equals("")) 
					{
						cargo_no[i] = "0";
					}
					
					int rev_no=-1;
					String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_BUY_DAILY_ALLOCATION "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no[i]);
					stmt.setString(2, agmt_no[i]);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd[i]);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, trans_plantseq[i]);
					stmt.setString(7, plant_seq[i]);
					stmt.setString(8, contract_type[i]);
					stmt.setString(9, gas_dt);
					stmt.setString(10, bu_plant_seq[i]);
					stmt.setString(11, cargo_no[i]);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						rev_no = rset.getInt(1)+1;
					}
					else
					{
						rev_no = rev_no + 1;
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_BUY_DAILY_ALLOCATION(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd[i]);
					stmt1.setString(3, agmt_no[i]);
					stmt1.setString(4, agmt_rev_no[i]);
					stmt1.setString(5, cont_no[i]);
					stmt1.setString(6, cont_rev_no[i]);
					stmt1.setInt(7, rev_no);
					stmt1.setString(8, plant_seq[i]);
					stmt1.setString(9, trans_cd[i]);
					stmt1.setString(10, trans_plantseq[i]);
					stmt1.setString(11, gas_dt);
					stmt1.setString(12, contract_type[i]);
					stmt1.setString(13, gen_dt);
					stmt1.setString(14, gen_time[i]);
					stmt1.setString(15, base[i]);
					stmt1.setString(16, gcv[i]);
					stmt1.setString(17, ncv[i]);
					stmt1.setString(18, qty_mmbtu[i]);
					stmt1.setString(19, qty_scm[i]);
					stmt1.setString(20, emp_cd);
					stmt1.setString(21, bu_plant_seq[i]);
					stmt1.setString(22, cargo_no[i]);
					stmt1.executeQuery();
					
					stmt1.close();
					
					String countpty_cd=counterparty_cd[i];
					String agmt=agmt_no[i];
					String agmt_rev=agmt_rev_no[i];
					String cont=cont_no[i];
					String cont_rev=cont_rev_no[i];
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type[i];
					String cargoNo=cargo_no[i];
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String l=index[i];
					
					String mole_mapping[] = request.getParameterValues("mole_mapping_"+l);
					String mole_seq_no[] = request.getParameterValues("mole_seq_no_"+l);
					String mole_qty_mmbtu[] = request.getParameterValues("mole_qty_mmbtu_"+l);
					String mole_qty_scm[] = request.getParameterValues("mole_qty_scm_"+l);
					
					if(mole_mapping!=null)
					{
						String dtl_category="MOL";
						for(int j=0;j<mole_mapping.length;j++)
						{
							String seqNo="";
							if(mole_seq_no[j].equals(""))
							{
								String query1 = "SELECT NVL(MAX(SEQ_NO),?) "
										+ "FROM FMS_BUY_DAILY_ALLOCATION_MM "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(1, "0");
								stmt1.setString(2, cont);
								stmt1.setString(3, agmt);
								stmt1.setString(4, comp_cd);
								stmt1.setString(5, countpty_cd);
								stmt1.setString(6, transCd);
								stmt1.setString(7, transPlantSeq);
								stmt1.setString(8, plantSeq);
								stmt1.setString(9, cont_type);
								stmt1.setString(10, gas_dt);
								stmt1.setString(11, buPlantSeq);
								stmt1.setString(12, cargoNo);
								stmt1.setString(13, dtl_category);
								rset1 = stmt1.executeQuery();
								if(rset1.next())
								{
									seqNo = ""+(rset1.getInt(1)+1);
								}
								else
								{
									seqNo="1";
								}
								rset1.close();
								stmt1.close();
							}
							else
							{
								seqNo=mole_seq_no[j];
							}
							
							int mol_rev_no=-1;
							String query1 = "SELECT NVL(MAX(NOM_REV_NO),?) "
									+ "FROM FMS_BUY_DAILY_ALLOCATION_MM "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
									+ "AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(1, "-1");
							stmt1.setString(2, cont);
							stmt1.setString(3, agmt);
							stmt1.setString(4, comp_cd);
							stmt1.setString(5, countpty_cd);
							stmt1.setString(6, transCd);
							stmt1.setString(7, transPlantSeq);
							stmt1.setString(8, plantSeq);
							stmt1.setString(9, cont_type);
							stmt1.setString(10, gas_dt);
							stmt1.setString(11, buPlantSeq);
							stmt1.setString(12, seqNo);
							stmt1.setString(13, cargoNo);
							stmt1.setString(14, dtl_category);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								mol_rev_no = rset1.getInt(1)+1;
							}
							else
							{
								mol_rev_no = rev_no + 1;
							}
							rset1.close();
							stmt1.close();
							
							String query2="INSERT INTO FMS_BUY_DAILY_ALLOCATION_MM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP) "
									+ "VALUES(?,?,?,?,?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?,?,?)";
							stmt2 = dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmt);
							stmt2.setString(4, agmt_rev);
							stmt2.setString(5, cont);
							stmt2.setString(6, cont_rev);
							stmt2.setInt(7, mol_rev_no);
							stmt2.setString(8, plantSeq);
							stmt2.setString(9, transCd);
							stmt2.setString(10, transPlantSeq);
							stmt2.setString(11, gas_dt);
							stmt2.setString(12, cont_type);
							stmt2.setString(13, gen_dt);
							stmt2.setString(14, genTime);
							stmt2.setString(15, Base);
							stmt2.setString(16, GCV);
							stmt2.setString(17, NCV);
							stmt2.setString(18, mole_qty_mmbtu[j]);
							stmt2.setString(19, mole_qty_scm[j]);
							stmt2.setString(20, emp_cd);
							stmt2.setString(21, buPlantSeq);
							stmt2.setString(22, seqNo);
							stmt2.setString(23, cargoNo);
							stmt2.setString(24, dtl_category);
							stmt2.setString(25, mole_mapping[j]);
							stmt2.executeQuery();
							
							stmt2.close();
						}
					}
				}
			}
			
			msg = "Successful! - Daily Allocation Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/frm_buy_daily_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&gas_dt="+gas_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
	
	private void InsertUpdatePeriodicAllocationDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdatePeriodicAllocationDetail()";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cargo_no = request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
			
			String[] gas_dt=request.getParameterValues("gas_dt");
			String[] gen_dt=request.getParameterValues("gen_dt");
			String[] trans_cd=request.getParameterValues("trans_cd");
			String[] trans_plantseq=request.getParameterValues("trans_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			String[] index = request.getParameterValues("index"); //INDEX of l
			
			if(gas_dt != null)
			{
				for(int i=0; i<gas_dt.length; i++)
				{
					int rev_no=-1;
					String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_BUY_DAILY_ALLOCATION "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=?  AND CARGO_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no);
					stmt.setString(2, agmt_no);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, trans_plantseq[i]);
					stmt.setString(7, plant_seq[i]);
					stmt.setString(8, contract_type);
					stmt.setString(9, gas_dt[i]);
					stmt.setString(10, bu_plant_seq[i]);
					stmt.setString(11, cargo_no);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						rev_no = rset.getInt(1)+1;
					}
					else
					{
						rev_no = rev_no + 1;
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_BUY_DAILY_ALLOCATION(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,CARGO_NO) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev_no);
					stmt1.setInt(7, rev_no);
					stmt1.setString(8, plant_seq[i]);
					stmt1.setString(9, trans_cd[i]);
					stmt1.setString(10, trans_plantseq[i]);
					stmt1.setString(11, gas_dt[i]);
					stmt1.setString(12, contract_type);
					stmt1.setString(13, gen_dt[i]);
					stmt1.setString(14, gen_time[i]);
					stmt1.setString(15, base[i]);
					stmt1.setString(16, gcv[i]);
					stmt1.setString(17, ncv[i]);
					stmt1.setString(18, qty_mmbtu[i]);
					stmt1.setString(19, qty_scm[i]);
					stmt1.setString(20, emp_cd);
					stmt1.setString(21, bu_plant_seq[i]);
					stmt1.setString(22, cargo_no);
					stmt1.executeQuery();
					
					stmt1.close();
					

					String countpty_cd=counterparty_cd;
					String agmt=agmt_no;
					String agmt_rev=agmt_rev_no;
					String cont=cont_no;
					String cont_rev=cont_rev_no;
					String transCd=trans_cd[i];
					String transPlantSeq=trans_plantseq[i];
					String plantSeq=plant_seq[i];
					String cont_type=contract_type;
					String cargoNo=cargo_no;
					String buPlantSeq=bu_plant_seq[i];
					String genTime=gen_time[i];
					String Base=base[i];
					String GCV=gcv[i];
					String NCV=ncv[i];
					String l=index[i];
					
					String mole_mapping[] = request.getParameterValues("mole_mapping_"+l);
					String mole_seq_no[] = request.getParameterValues("mole_seq_no_"+l);
					String mole_qty_mmbtu[] = request.getParameterValues("mole_qty_mmbtu_"+l);
					String mole_qty_scm[] = request.getParameterValues("mole_qty_scm_"+l);
					
					if(mole_mapping!=null)
					{
						String dtl_category="MOL";
						for(int j=0;j<mole_mapping.length;j++)
						{
							String seqNo="";
							if(mole_seq_no[j].equals(""))
							{
								String query1 = "SELECT NVL(MAX(SEQ_NO),?) "
										+ "FROM FMS_BUY_DAILY_ALLOCATION_MM "
										+ "WHERE CONT_NO=? AND AGMT_NO=? "
										+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
										+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(1, "0");
								stmt1.setString(2, cont);
								stmt1.setString(3, agmt);
								stmt1.setString(4, comp_cd);
								stmt1.setString(5, countpty_cd);
								stmt1.setString(6, transCd);
								stmt1.setString(7, transPlantSeq);
								stmt1.setString(8, plantSeq);
								stmt1.setString(9, cont_type);
								stmt1.setString(10, gas_dt[i]);
								stmt1.setString(11, buPlantSeq);
								stmt1.setString(12, cargoNo);
								stmt1.setString(13, dtl_category);
								rset1 = stmt1.executeQuery();
								if(rset1.next())
								{
									seqNo = ""+(rset1.getInt(1)+1);
								}
								else
								{
									seqNo="1";
								}
								rset1.close();
								stmt1.close();
							}
							else
							{
								seqNo=mole_seq_no[j];
							}
							
							int mol_rev_no=-1;
							String query1 = "SELECT NVL(MAX(NOM_REV_NO),?) "
									+ "FROM FMS_BUY_DAILY_ALLOCATION_MM "
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? "
									+ "AND SEQ_NO=? AND CARGO_NO=? AND DTL_CATEGORY=? ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(1, "-1");
							stmt1.setString(2, cont);
							stmt1.setString(3, agmt);
							stmt1.setString(4, comp_cd);
							stmt1.setString(5, countpty_cd);
							stmt1.setString(6, transCd);
							stmt1.setString(7, transPlantSeq);
							stmt1.setString(8, plantSeq);
							stmt1.setString(9, cont_type);
							stmt1.setString(10, gas_dt[i]);
							stmt1.setString(11, buPlantSeq);
							stmt1.setString(12, seqNo);
							stmt1.setString(13, cargoNo);
							stmt1.setString(14, dtl_category);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								mol_rev_no = rset1.getInt(1)+1;
							}
							else
							{
								mol_rev_no = rev_no + 1;
							}
							rset1.close();
							stmt1.close();
							
							String query2="INSERT INTO FMS_BUY_DAILY_ALLOCATION_MM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
									+ "TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
									+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,BU_SEQ,SEQ_NO,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP) "
									+ "VALUES(?,?,?,?,?,?,?,?,"
									+ "?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
									+ "?,?,?,?,?,SYSDATE,?,?,?,?,?)";
							stmt2 = dbcon.prepareStatement(query2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, agmt);
							stmt2.setString(4, agmt_rev);
							stmt2.setString(5, cont);
							stmt2.setString(6, cont_rev);
							stmt2.setInt(7, mol_rev_no);
							stmt2.setString(8, plantSeq);
							stmt2.setString(9, transCd);
							stmt2.setString(10, transPlantSeq);
							stmt2.setString(11, gas_dt[i]);
							stmt2.setString(12, cont_type);
							stmt2.setString(13, gen_dt[i]);
							stmt2.setString(14, genTime);
							stmt2.setString(15, Base);
							stmt2.setString(16, GCV);
							stmt2.setString(17, NCV);
							stmt2.setString(18, mole_qty_mmbtu[j]);
							stmt2.setString(19, mole_qty_scm[j]);
							stmt2.setString(20, emp_cd);
							stmt2.setString(21, buPlantSeq);
							stmt2.setString(22, seqNo);
							stmt2.setString(23, cargoNo);
							stmt2.setString(24, dtl_category);
							stmt2.setString(25, mole_mapping[j]);
							stmt2.executeQuery();
							
							stmt2.close();
						}
					}
				}
			}
			
			msg = "Successful! - Periodic Allocation Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/frm_buy_periodic_allocation.jsp?counterparty_cd="+counterparty_cd+
					"&clearance="+clearance+"&contract_type="+contract_type+"&from_date="+from_dt+"&to_date="+to_dt+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&cargo_no="+cargo_no+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
	
	private void InsertUpdateDailyAllocationDetail_BKP(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateDailyAllocationDetail_BKP()";
		
		try
		{
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			
			String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
			
			String[] trans_cd=request.getParameterValues("transporter_cd");
			String[] trans_plantseq=request.getParameterValues("transporter_plant_seq");
			String[] bu_plant_seq=request.getParameterValues("bu_plant_seq");
			String[] plant_seq=request.getParameterValues("plant_seq");
			
			String[] gen_dt = request.getParameterValues("gen_dt");
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			
			if(gen_dt != null)
			{
				for(int i=0; i<gen_dt.length; i++)
				{
					int rev_no=-1;
					String query = "SELECT NVL(MAX(NOM_REV_NO),'-1') "
							+ "FROM FMS_BUY_DAILY_ALLOCATION "
							+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no);
					stmt.setString(2, agmt_no);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, trans_cd[i]);
					stmt.setString(6, trans_plantseq[i]);
					stmt.setString(7, plant_seq[i]);
					stmt.setString(8, contract_type);
					stmt.setString(9, gas_dt);
					stmt.setString(10, bu_plant_seq[i]);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						rev_no = rset.getInt(1)+1;
					}
					else
					{
						rev_no = rev_no + 1;
					}
					rset.close();
					stmt.close();
					
					query1="INSERT INTO FMS_BUY_DAILY_ALLOCATION(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
							+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
							+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,?,?,?,?,SYSDATE)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev_no);
					stmt1.setInt(7, rev_no);
					stmt1.setString(8, plant_seq[i]);
					stmt1.setString(9, trans_cd[i]);
					stmt1.setString(10, trans_plantseq[i]);
					stmt1.setString(11, bu_plant_seq[i]);
					stmt1.setString(12, gas_dt);
					stmt1.setString(13, contract_type);
					stmt1.setString(14, gen_dt[i]);
					stmt1.setString(15, gen_time[i]);
					stmt1.setString(16, base[i]);
					stmt1.setString(17, gcv[i]);
					stmt1.setString(18, ncv[i]);
					stmt1.setString(19, qty_mmbtu[i]);
					stmt1.setString(20, qty_scm[i]);
					stmt1.setString(21, emp_cd);
					
					stmt1.executeQuery();
					
					stmt1.close();
				}
			}
			
			msg = "Successful! - Daily Allocation Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/frm_buy_daily_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&gas_dt="+gas_dt+
					"&clearance="+clearance+"&from_dt="+from_dt+"&to_dt="+to_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
	
	
	private void InsertUpdateBuyerNomToTrdRmk(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateBuyerNomToTrdRmk()";
		
		try
		{
			String report_dt = request.getParameter("report_dt")==null?"":request.getParameter("report_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			String bu_plant_seq = request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String frq_type = request.getParameter("nomination_type")==null?"":request.getParameter("nomination_type");
			String rmk_rev_no ="0";
			
			String query = "SELECT NVL(MAX(RMK_REV_NO),-1) "
					+ "FROM FMS_BUY_BUYER_NOM_RMK "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND BU_SEQ=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, plant_seq);
			stmt.setString(7, bu_plant_seq);
			stmt.setString(8, report_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				rmk_rev_no=""+(rset.getInt(1)+1);
			}
			rset.close();
			stmt.close();
			
			query1 = "INSERT INTO FMS_BUY_BUYER_NOM_RMK(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,"
					+ "RMK_REV_NO,GAS_DT,REMARKS,ENT_DT,ENT_BY) "
					+ "VALUES(?,?,?,?,?,?,?,"
					+ "?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?)";
			stmt1=dbcon.prepareStatement(query1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, agmt_no);
			stmt1.setString(4, cont_no);
			stmt1.setString(5, contract_type);
			stmt1.setString(6, plant_seq);
			stmt1.setString(7, bu_plant_seq);
			stmt1.setString(8, rmk_rev_no);
			stmt1.setString(9, report_dt);
			stmt1.setString(10, remark);
			stmt1.setString(11, emp_cd);
			stmt1.executeUpdate();
			
			stmt1.close();
			
			msg = "Successful! - Data Submitted Successfully!";
			msg_type="S";
			
			url = "../purchase/rpt_buy_buyer_nom_to_trader.jsp?msg="+msg+"&msg_type="+msg_type+"&report_dt="+report_dt+"&frq_type="+frq_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception !";
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
