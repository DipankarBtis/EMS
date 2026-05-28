package com.etrm.fms.gta;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 28/03/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_GtaMaster")
public class Frm_GtaMaster extends HttpServlet
{
	static String db_src_file_name="Frm_GtaMaster.java";
	public static  Connection dbcon;
	
	static public String servletName = "Frm_GtaMaster";
	static public String option = "";
	static public String url = ""; 
	static public String msg = "";
	static public String msg_type = "";
	static public String err_msg = "";
	
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
	
	static private ResultSet rset = null;
	static private ResultSet rset0 = null;
	static private ResultSet rset1 = null;
	static private ResultSet rset2 = null;
	static private ResultSet rset3 = null;
	static private ResultSet rset4 = null;
	
	static public String form_id = "0";
	static public String form_nm = "";
	static public String mod_cd = "0";
	static public String mod_nm = "";
	static public String u = "";
	
	static public String old_value="";
	static public String new_value="";
	
	static public String emp_cd="";
	static public String comp_cd="";
	static public String comp_abbr="";
	static public String emp_nm="";
	static public String ip="";
	
	static public String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil dateUtil = new DateUtil();
	
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
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("GTA_AGREEMENT_MST"))
					{
						InsertUpdateGtaAgreementDetail(request);
					}
					else if(option.equalsIgnoreCase("GTA_CONTRACT_MST"))
					{
						InsertUpdateGtaContractDetail(request);
					}
					else if(option.equalsIgnoreCase("PARKING_CONTRACT_MST"))
					{
						InsertUpdateParkingContractDetail(request);
					}
					else if(option.equalsIgnoreCase("GTA_NOM_SCH_ALLOC"))
					{
						InsertUpdateGtaNomSchAllocDetail(request);
					}
					else if(option.equalsIgnoreCase("GTPA_BILLING_DTL"))
					{
						InsertUpdateGtpaBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("CONTRACT_BILLING_DTL"))
					{
						InsertUpdateContractBillingDetail(request);
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
	
	private void InsertUpdateGtaAgreementDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateGtaAgreementDetail()";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String total_qty = request.getParameter("total_qty")==null?"":request.getParameter("total_qty");
			String calc_base = request.getParameter("calc_base")==null?"":request.getParameter("calc_base");
			String status_flag = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String unit_cd = request.getParameter("unit_cd")==null?"":request.getParameter("unit_cd");
			
			String[] entry_point_mapping = request.getParameterValues("entry_point_mapping");
			String[] exit_point_mapping = request.getParameterValues("exit_point_mapping");
			String[] chk_trans_bu = request.getParameterValues("chk_trans_bu");
			String tmp_chk_trans_bu = request.getParameter("tmp_chk_trans_bu")==null?"":request.getParameter("tmp_chk_trans_bu");
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			if(opration.equals("INSERT"))
			{
				query="SELECT MAX(AGMT_NO) "
						+ "FROM FMS_GTA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=?";
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
				rset.close();
				stmt.close();
				agmt_rev_no="0";
				
				String agmt_name = "";
				if(agreement_type.equals("G"))
				{
					agmt_name=comp_abbr+"-"+counterparty_abbr+"-GTA"+agmt_no+"-REV"+agmt_rev_no;
				}
				else if(agreement_type.equals("P"))
				{
					agmt_name=comp_abbr+"-"+counterparty_abbr+"-MPSA"+agmt_no+"-REV"+agmt_rev_no;
				}
						
				
				query="INSERT INTO FMS_GTA_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,"
						+ "START_DT,END_DT,STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,ENT_BY,ENT_DT,AGMT_NAME,AGMT_TYPE) "
						+ "VALUES(?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,"
						+ "?,?,SYSDATE,?,?)";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev_no);
				stmt1.setString(5, start_dt);
				stmt1.setString(6, end_dt);
				stmt1.setString(7, status_flag);
				stmt1.setString(8, total_qty);
				stmt1.setString(9, unit_cd);
				stmt1.setString(10, calc_base);
				stmt1.setString(11, emp_cd);
				stmt1.setString(12, agmt_name);
				stmt1.setString(13, agreement_type);
				stmt1.executeUpdate();
				
				msg = "Successful! - New GTA Agreement Added Successfully!";
				msg_type = "S";
				opration="MODIFY";
				stmt1.close();
				
			}
			else if(opration.equals("MODIFY"))
			{
				String agmt_name = "";
				if(agreement_type.equals("G"))
				{
					agmt_name=comp_abbr+"-"+counterparty_abbr+"-GTA"+agmt_no+"-REV"+agmt_rev_no;
				}
				else if(agreement_type.equals("P"))
				{
					agmt_name=comp_abbr+"-"+counterparty_abbr+"-MPSA"+agmt_no+"-REV"+agmt_rev_no;
				}
				
				query="UPDATE FMS_GTA_AGMT_MST SET START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),"
						+ "STATUS=?,TOT_TRANS_QTY=?,UNIT_CD=?,CALC_BASE=?,"
						+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,AGMT_NAME=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, start_dt);
				stmt1.setString(2, end_dt);
				stmt1.setString(3, status_flag);
				stmt1.setString(4, total_qty);
				stmt1.setString(5, unit_cd);
				stmt1.setString(6, calc_base);
				stmt1.setString(7, emp_cd);
				stmt1.setString(8, agmt_name);
				stmt1.setString(9, comp_cd);
				stmt1.setString(10, counterparty_cd);
				stmt1.setString(11, agmt_no);
				stmt1.setString(12, agmt_rev_no);
				stmt1.setString(13, agreement_type);
				stmt1.executeUpdate();
				
				msg = "Successful! - GTA Agreement modified Successfully!";
				msg_type = "S";
				
				stmt1.close();
			}
			
			if(entry_point_mapping!=null)
			{
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_ENTRY_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, agreement_type);
				rset=stmt.executeQuery();
				int count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_GTA_ENTRY_POINT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				for(int i=0;i<entry_point_mapping.length;i++)
				{
					String entry_countpty_cd="";
					String entry_plantSeq="";
					
					String[] split = entry_point_mapping[i].split("-");
					entry_countpty_cd=split[0];
					entry_plantSeq=split[1];
					
					query = "INSERT INTO FMS_GTA_ENTRY_POINT(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TRANSPORTER_CD, PLANT_SEQ,ENT_BY, ENT_DT,AGMT_TYPE) "
							+ "VALUES(?,?,?,?,?,?,?,SYSDATE,?) ";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, entry_countpty_cd);
					stmt1.setString(6, entry_plantSeq);
					stmt1.setString(7, emp_cd);
					stmt1.setString(8, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			
			if(exit_point_mapping!=null)
			{
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_EXIT_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, agreement_type);
				rset=stmt.executeQuery();
				int count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				if(count>0)
				{
					query = "DELETE FROM FMS_GTA_EXIT_POINT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				for(int i=0;i<exit_point_mapping.length;i++)
				{
					String exit_countpty_cd="";
					String exit_plantSeq="";
					String exit_entity="";
					
					String[] split = exit_point_mapping[i].split("-");
					exit_entity=split[0];
					exit_countpty_cd=split[1];
					exit_plantSeq=split[2];
					
					query = "INSERT INTO FMS_GTA_EXIT_POINT(COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV,ENTITY_CD, PLANT_SEQ,ENTITY,ENT_BY, ENT_DT,AGMT_TYPE) "
							+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE,?) ";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, exit_countpty_cd);
					stmt1.setString(6, exit_plantSeq);
					stmt1.setString(7, exit_entity);
					stmt1.setString(8, emp_cd);
					stmt1.setString(9, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
			}
			
			if(chk_trans_bu!=null)
			{
				int count = 0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_AGMT_TRANS_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND AGMT_TYPE=?";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, agmt_no);
				stmt0.setString(4, agmt_rev_no);
				stmt0.setString(5, agreement_type);
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
					query = "DELETE FROM FMS_GTA_AGMT_TRANS_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
			
				for(int i=0;i<chk_trans_bu.length;i++)
				{
					query = "INSERT INTO FMS_GTA_AGMT_TRANS_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, AGMT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "VALUES(?,?, ?,?, ?,?,?,SYSDATE) ";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, agreement_type);
					stmt2.setString(6, chk_trans_bu[i]);
					stmt2.setString(7, emp_cd);
					
					stmt2.executeUpdate();
					
					stmt2.close();
					
					String temp_chk_trans_bu[]=tmp_chk_trans_bu.split("@");
					for(int m=0; m<temp_chk_trans_bu.length; m++)
					{
						if(!temp_chk_trans_bu[m].equals(chk_trans_bu[i]) && !chk_trans_bu[i].equals(""))
						{
							updateGtaAgmtBillingTransBu(counterparty_cd, agmt_no, agmt_rev_no, agreement_type, temp_chk_trans_bu[m], chk_trans_bu[i], chk_trans_bu.length);
						}
					}
				 }
			}
			
			if(chk_bu_plant!=null)
			{
				int count = 0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND AGMT_TYPE=?";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, agmt_no);
				stmt0.setString(4, agmt_rev_no);
				stmt0.setString(5, agreement_type);
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
					query = "DELETE FROM FMS_GTA_AGMT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
			
				for(int i=0;i<chk_bu_plant.length;i++)
				{
					query = "INSERT INTO FMS_GTA_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, AGMT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
							+ "VALUES(?,?, ?,?, ?,?,?,SYSDATE) ";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, agreement_type);
					stmt2.setString(6, chk_bu_plant[i]);
					stmt2.setString(7, emp_cd);
					
					stmt2.executeUpdate();
					
					stmt2.close();
				 }
			}
			
			
			url = "../gta/frm_gtpa_agmt_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&agreement_type="+agreement_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateParkingContractDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateParkingContractDetail()";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String ent_dt=""+dateUtil.getSysdate();
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String ct_ref_no = request.getParameter("ct_ref_no")==null?"":request.getParameter("ct_ref_no");
			String entry_point = request.getParameter("entry_point")==null?"":request.getParameter("entry_point");
			String exit_point = request.getParameter("exit_point")==null?"":request.getParameter("exit_point");
			String mdq = request.getParameter("mdq")==null?"":request.getParameter("mdq");
			String mdq_unit_cd = request.getParameter("mdq_unit_cd")==null?"":request.getParameter("mdq_unit_cd");
			String parking_rate = request.getParameter("parking_rate")==null?"":request.getParameter("parking_rate");
			String park_rate_cd = request.getParameter("park_rate_cd")==null?"":request.getParameter("park_rate_cd");
			String max_park_qty = request.getParameter("max_park_qty")==null?"":request.getParameter("max_park_qty");
			String max_park_unit_cd = request.getParameter("max_park_unit_cd")==null?"":request.getParameter("max_park_unit_cd");
			
			String transporter_cd = request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
			String gta_cont_map = request.getParameter("gta_cont_map")==null?"":request.getParameter("gta_cont_map");
			
			String calc_base = request.getParameter("calc_base")==null?"":request.getParameter("calc_base");
			String gcv = request.getParameter("gcv")==null?"":request.getParameter("gcv");
			String ncv = request.getParameter("ncv")==null?"":request.getParameter("ncv");
			String temp_start_dt = request.getParameter("temp_start_dt")==null?"":request.getParameter("temp_start_dt");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String[] chk_trans_bu = request.getParameterValues("chk_trans_bu");
			String[] temp_chk_trans_bu = request.getParameterValues("temp_chk_trans_bu");
			
			String cont_name="";
			
			if(opration.equals("INSERT"))
			{
				
				String year = ent_dt.substring(8,ent_dt.length());
				
				int cont = Integer.parseInt(year) * 10000;
				query="SELECT NVL(MAX(CONT_NO),?) "
						+ "FROM FMS_GTA_CONT_MST "
						+ "WHERE CONT_NO LIKE ? AND COMPANY_CD=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setInt(1, cont);
				stmt.setString(2, year+"%");
				stmt.setString(3, comp_cd);
				stmt.setString(4, contract_type);
				stmt.setString(5, agreement_type);
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
			
			cont_name = counterparty_abbr+"-"+comp_abbr+"-MPSA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, agreement_type);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}	
				rset.close();
				stmt.close();
				if(rev_count > 0)
				{
					int cnt=0;
					query ="UPDATE FMS_GTA_CONT_MST SET CONT_NAME=?,CONT_REF_NO=?,CT_REF_NO=?,"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "ENTRY_PT_MAPPING_ID=?,EXIT_PT_MAPPING_ID=?,MDQ=?,MDQ_UNIT=?,"
							+ "RATE_UNIT=?,PARKING_RATE=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,CALC_BASE=?,GCV=?,NCV=?,"
							+ "MAX_PARK_QTY=?,MAX_PARK_UNIT=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++cnt, cont_name);
					stmt1.setString(++cnt, cont_ref_no);
					stmt1.setString(++cnt, ct_ref_no);
					stmt1.setString(++cnt, start_dt);
					stmt1.setString(++cnt, end_dt);
					stmt1.setString(++cnt, entry_point);
					stmt1.setString(++cnt, exit_point);
					stmt1.setString(++cnt, mdq);
					stmt1.setString(++cnt, mdq_unit_cd);
					stmt1.setString(++cnt, park_rate_cd);
					stmt1.setString(++cnt, parking_rate);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, calc_base);
					stmt1.setString(++cnt, gcv);
					stmt1.setString(++cnt, ncv);
					stmt1.setString(++cnt, max_park_qty);
					stmt1.setString(++cnt, max_park_unit_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					if(!temp_start_dt.equals(start_dt) && !start_dt.equals(""))
					{
						updateGtaParkingContractBillingEffectiveDate(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_start_dt, start_dt);
					}
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_GTA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "CONT_NAME,CONT_REF_NO,CT_REF_NO,START_DT,END_DT,"
							+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,MDQ,MDQ_UNIT,RATE_UNIT,PARKING_RATE,"
							+ "ENT_BY,ENT_DT,CALC_BASE,GCV,NCV,"
							+ "AGMT_TYPE,MAX_PARK_QTY,MAX_PARK_UNIT) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,"
							+ "?,SYSDATE,?,?,?,?,"
							+ "?,?)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, cont_name);
					stmt1.setString(++cnt, cont_ref_no);
					stmt1.setString(++cnt, ct_ref_no);
					stmt1.setString(++cnt, start_dt);
					stmt1.setString(++cnt, end_dt);
					stmt1.setString(++cnt, entry_point);
					stmt1.setString(++cnt, exit_point);
					stmt1.setString(++cnt, mdq);
					stmt1.setString(++cnt, mdq_unit_cd);
					stmt1.setString(++cnt, park_rate_cd);
					stmt1.setString(++cnt, parking_rate);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, calc_base);
					stmt1.setString(++cnt, gcv);
					stmt1.setString(++cnt, ncv);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, max_park_qty);
					stmt1.setString(++cnt, max_park_unit_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				if(!transporter_cd.equals("") && !gta_cont_map.equals(""))
				{
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_GTA_CONT_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='R'";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, agreement_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(count>0)
					{
						query="UPDATE FMS_GTA_CONT_MAP SET CUSTOMER_CD=?,SELL_CONT_MAP=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='R'";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, transporter_cd);
						stmt2.setString(2, gta_cont_map);
						stmt2.setString(3, comp_cd);
						stmt2.setString(4, counterparty_cd);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, agmt_no);
						stmt2.setString(8, agmt_rev_no);
						stmt2.setString(9, contract_type);
						stmt2.setString(10, agreement_type);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					else
					{
						
						query="INSERT INTO FMS_GTA_CONT_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT,AGMT_TYPE,MAPED_ENTITY_TYPE) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,'R')";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, transporter_cd);
						stmt2.setString(9, gta_cont_map);
						stmt2.setString(10, emp_cd);
						stmt2.setString(11, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				int count=0;
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, agreement_type);
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
					query = "DELETE FROM FMS_GTA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.setString(8, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_GTA_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE) "
								+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_bu_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//FOR TRANSPORTER BU
				count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_TRANS_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, cont_no);
				stmt0.setString(4, cont_rev_no);
				stmt0.setString(5, agmt_no);
				stmt0.setString(6, agmt_rev_no);
				stmt0.setString(7, contract_type);
				stmt0.setString(8, agreement_type);
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
					query = "DELETE FROM FMS_GTA_CONT_TRANS_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.setString(8, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_trans_bu!=null)
				{
					for(int i=0;i<chk_trans_bu.length;i++)
					{
						query = "INSERT INTO FMS_GTA_CONT_TRANS_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE) "
								+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_trans_bu[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
						
						if(!temp_chk_trans_bu[i].equals(chk_trans_bu[i]) && !chk_trans_bu[i].equals(""))
						{
							updateGtaParkingContractBillingTransBu(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_trans_bu[i], chk_trans_bu[i]);
						}
					}
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Parking Contract Added Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Parking Contract Modified Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Parking Contract Modification Failed!";
				msg_type="E";
			}
			
			url = "../gta/frm_gta_parking_cont_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agreement_type+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateGtaContractDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateGtaContractDetail()";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String ct_ref_no = request.getParameter("ct_ref_no")==null?"":request.getParameter("ct_ref_no");
			String ct_seq_no = request.getParameter("ct_seq_no")==null?"":request.getParameter("ct_seq_no");
			String entry_point = request.getParameter("entry_point")==null?"":request.getParameter("entry_point");
			String exit_point = request.getParameter("exit_point")==null?"":request.getParameter("exit_point");
			String mdq = request.getParameter("mdq")==null?"":request.getParameter("mdq");
			String mdq_unit_cd = request.getParameter("mdq_unit_cd")==null?"":request.getParameter("mdq_unit_cd");
			
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String sales_cont_map = request.getParameter("sales_cont_map")==null?"":request.getParameter("sales_cont_map");
			
			String calc_base = request.getParameter("calc_base")==null?"":request.getParameter("calc_base");
			String gcv = request.getParameter("gcv")==null?"":request.getParameter("gcv");
			String ncv = request.getParameter("ncv")==null?"":request.getParameter("ncv");
			
			String trans_rate_cd = request.getParameter("trans_rate_cd")==null?"":request.getParameter("trans_rate_cd");
			String transportation_rate = request.getParameter("transportation_rate")==null?"":request.getParameter("transportation_rate");
			String positive_imbalance_rate = request.getParameter("positive_imbalance_rate")==null?"":request.getParameter("positive_imbalance_rate");
			String negative_imbalance_rate = request.getParameter("negative_imbalance_rate")==null?"":request.getParameter("negative_imbalance_rate");
			String ship_pay_charge = request.getParameter("ship_pay_charge")==null?"":request.getParameter("ship_pay_charge");
			String sip_pay_freq = request.getParameter("sip_pay_freq")==null?"D":request.getParameter("sip_pay_freq");
			String ship_or_pay_percent = request.getParameter("ship_or_pay_percent")==null?"":request.getParameter("ship_or_pay_percent");
			
			String unauth_overrun_charge = request.getParameter("unauth_overrun_charge")==null?"":request.getParameter("unauth_overrun_charge");
			String temp_start_dt = request.getParameter("temp_start_dt")==null?"":request.getParameter("temp_start_dt");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String[] chk_trans_bu = request.getParameterValues("chk_trans_bu");
			String[] temp_chk_trans_bu = request.getParameterValues("temp_chk_trans_bu");
			
			String cont_name="";
			
			if(opration.equals("INSERT"))
			{
				cont_no="1";
				cont_rev_no="0";
				query="SELECT NVL(MAX(CONT_NO),0) "
						+ "FROM FMS_GTA_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, contract_type);
				stmt.setString(5, agreement_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					cont_no=""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
			}
			
			cont_name = counterparty_abbr+"-"+comp_abbr+"-GTA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, agreement_type);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}	
				rset.close();
				stmt.close();
				if(rev_count > 0)
				{
					query ="UPDATE FMS_GTA_CONT_MST SET CONT_NAME=?,CONT_REF_NO=?,CT_REF_NO=?,"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "ENTRY_PT_MAPPING_ID=?,EXIT_PT_MAPPING_ID=?,MDQ=?,MDQ_UNIT=?,"
							+ "RATE_UNIT=?,TRANSPORT_RATE=?,POSITIVE_IMB_RATE=?,"
							+ "NEGETIVE_IMB_RATE=?,UNAUTH_OVERRUN_RATE=?,SIP_PAY_RATE=?,"
							+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,CALC_BASE=?,GCV=?,NCV=?,SIP_PAY_FREQ=?,"
							+ "CT_SEQ_NO=?,SIP_PAY_PERCENT=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, cont_name);
					stmt1.setString(2, cont_ref_no);
					stmt1.setString(3, ct_ref_no);
					stmt1.setString(4, start_dt);
					stmt1.setString(5, end_dt);
					stmt1.setString(6, entry_point);
					stmt1.setString(7, exit_point);
					stmt1.setString(8, mdq);
					stmt1.setString(9, mdq_unit_cd);
					stmt1.setString(10, trans_rate_cd);
					stmt1.setString(11, transportation_rate);
					stmt1.setString(12, positive_imbalance_rate);
					stmt1.setString(13, negative_imbalance_rate);
					stmt1.setString(14, unauth_overrun_charge);
					stmt1.setString(15, ship_pay_charge);
					stmt1.setString(16, emp_cd);
					stmt1.setString(17, calc_base);
					stmt1.setString(18, gcv);
					stmt1.setString(19, ncv);
					stmt1.setString(20, sip_pay_freq);
					stmt1.setString(21, ct_seq_no);
					stmt1.setString(22, ship_or_pay_percent);
					stmt1.setString(23, comp_cd);
					stmt1.setString(24, counterparty_cd);
					stmt1.setString(25, cont_no);
					stmt1.setString(26, cont_rev_no);
					stmt1.setString(27, agmt_no);
					stmt1.setString(28, agmt_rev_no);
					stmt1.setString(29, contract_type);
					stmt1.setString(30, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					if(!temp_start_dt.equals(start_dt) && !start_dt.equals(""))
					{
						updateGtaParkingContractBillingEffectiveDate(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_start_dt, start_dt);
					}
				}
				else
				{
					query="INSERT INTO FMS_GTA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "CONT_NAME,CONT_REF_NO,CT_REF_NO,START_DT,END_DT,"
							+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,MDQ,MDQ_UNIT,RATE_UNIT,TRANSPORT_RATE,"
							+ "POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,SIP_PAY_RATE,ENT_BY,ENT_DT,CALC_BASE,GCV,NCV,SIP_PAY_FREQ,CT_SEQ_NO,"
							+ "SIP_PAY_PERCENT,AGMT_TYPE) "
							+ "VALUES(?,?,?,?,?,?,?,"
							+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,"
							+ "?,SYSDATE,?,?,?,?,?,"
							+ "?,?)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_rev_no);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, cont_name);
					stmt1.setString(9, cont_ref_no);
					stmt1.setString(10, ct_ref_no);
					stmt1.setString(11, start_dt);
					stmt1.setString(12, end_dt);
					stmt1.setString(13, entry_point);
					stmt1.setString(14, exit_point);
					stmt1.setString(15, mdq);
					stmt1.setString(16, mdq_unit_cd);
					stmt1.setString(17, trans_rate_cd);
					stmt1.setString(18, transportation_rate);
					stmt1.setString(19, positive_imbalance_rate);
					stmt1.setString(20, negative_imbalance_rate);
					stmt1.setString(21, unauth_overrun_charge);
					stmt1.setString(22, ship_pay_charge);
					stmt1.setString(23, emp_cd);
					stmt1.setString(24, calc_base);
					stmt1.setString(25, gcv);
					stmt1.setString(26, ncv);
					stmt1.setString(27, sip_pay_freq);
					stmt1.setString(28, ct_seq_no);
					stmt1.setString(29, ship_or_pay_percent);
					stmt1.setString(30, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				if(!customer_cd.equals("") && !sales_cont_map.equals("") && contract_type.equals("C"))
				{
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_GTA_CONT_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='C'";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev_no);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, agreement_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						count = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(count>0)
					{
						query="UPDATE FMS_GTA_CONT_MAP SET CUSTOMER_CD=?,SELL_CONT_MAP=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND CONT_REV=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? "
								+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='C'";
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
						stmt2.setString(10, agreement_type);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					else
					{
						
						query="INSERT INTO FMS_GTA_CONT_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT,AGMT_TYPE,MAPED_ENTITY_TYPE) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,'C')";
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
						stmt2.setString(11, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				int count=0;
				//BUSINESS UNIT
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, agreement_type);
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
					query = "DELETE FROM FMS_GTA_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.setString(8, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_GTA_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE) "
								+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_bu_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//FOR TRANSPORTER BU
				count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_GTA_CONT_TRANS_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, cont_no);
				stmt0.setString(4, cont_rev_no);
				stmt0.setString(5, agmt_no);
				stmt0.setString(6, agmt_rev_no);
				stmt0.setString(7, contract_type);
				stmt0.setString(8, agreement_type);
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
					query = "DELETE FROM FMS_GTA_CONT_TRANS_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
					stmt2 = dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, cont_no);
					stmt2.setString(4, cont_rev_no);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, agmt_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.setString(8, agreement_type);
					stmt2.executeUpdate();
					
					stmt2.close();
				}
				if(chk_trans_bu!=null)
				{
					for(int i=0;i<chk_trans_bu.length;i++)
					{
						query = "INSERT INTO FMS_GTA_CONT_TRANS_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, ENT_BY, ENT_DT,AGMT_TYPE) "
								+ "VALUES(?,?, ?, ?,?,?, ?,?,?,SYSDATE,?) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_trans_bu[i]);
						stmt2.setString(9, emp_cd);
						stmt2.setString(10, agreement_type);
						
						stmt2.executeUpdate();
						
						stmt2.close();
						
						if(!temp_chk_trans_bu[i].equals(chk_trans_bu[i]) && !chk_trans_bu[i].equals(""))
						{
							updateGtaParkingContractBillingTransBu(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_trans_bu[i], chk_trans_bu[i]);
						}
					 }
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New GTA Contract Added Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - GTA Contract Modified Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - GTA Contract Modification Failed!";
				msg_type="E";
			}
			
			url = "../gta/frm_gta_contract_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agreement_type+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateGtaNomSchAllocDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateGtaNomSchAllocDetail()";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String sales_cont_map=request.getParameter("sales_cont_map")==null?"":request.getParameter("sales_cont_map");
			String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String gtc_type=request.getParameter("gtc_type")==null?"":request.getParameter("gtc_type");
			String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"":request.getParameter("bu_plant_seq");
			String entry_pt_mapping_id=request.getParameter("entry_pt_mapping_id")==null?"":request.getParameter("entry_pt_mapping_id");
			String exit_pt_mapping_id=request.getParameter("exit_pt_mapping_id")==null?"":request.getParameter("exit_pt_mapping_id");
			
			String[] gas_dt = request.getParameterValues("gas_dt");
			String[] gen_dt = request.getParameterValues("gen_dt");
			String[] gen_time = request.getParameterValues("gen_time");
			String[] base = request.getParameterValues("base");
			String[] gcv = request.getParameterValues("gcv");
			String[] ncv = request.getParameterValues("ncv");
			String[] qty_mmbtu = request.getParameterValues("qty_mmbtu");
			String[] qty_scm = request.getParameterValues("qty_scm");
			
			String[] mdq = request.getParameterValues("mdq");
			String[] mdq_unit = request.getParameterValues("mdq_unit");
			
			String[] exit_base = request.getParameterValues("exit_base");
			String[] exit_gcv = request.getParameterValues("exit_gcv");
			String[] exit_ncv = request.getParameterValues("exit_ncv");
			String[] exit_qty_mmbtu = request.getParameterValues("exit_qty_mmbtu");
			String[] exit_qty_scm = request.getParameterValues("exit_qty_scm");
			String[] adjust_imb = request.getParameterValues("adjust_imb");
			
			if(gas_dt!=null)
			{
				for(int i=0; i<gas_dt.length; i++)
				{
					String cont_map="";
					if(!sales_cont_map.equals("") && sales_cont_map.length()>1 && (contract_type.equals("C") || contract_type.equals("K")))
					{
						String[] split=sales_cont_map.split("-");
						
						cont_map=split[0]+"-"+split[1]+"-"+split[2]+"-%-"+split[4]+"-%";
					}
					else
					{
						cont_map="0";
					}
					
					if(gtc_type.equals("S"))
					{	
						int rev_no=-1;
						query = "SELECT NVL(MAX(SCH_REV_NO),?) "
								+ "FROM FMS_DAILY_TRANSPORTER_SCH "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND BU_SEQ=? AND SELL_CONT_MAP LIKE ? "
								+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=?";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, "-1");
						stmt.setString(2, cont_no);
						stmt.setString(3, agmt_no);
						stmt.setString(4, comp_cd);
						stmt.setString(5, counterparty_cd);
						stmt.setString(6, contract_type);
						stmt.setString(7, gas_dt[i]);
						stmt.setString(8, bu_plant_seq);
						stmt.setString(9, cont_map);
						stmt.setString(10, entry_pt_mapping_id);
						stmt.setString(11, exit_pt_mapping_id);
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
						
						query="INSERT INTO FMS_DAILY_TRANSPORTER_SCH(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
								+ "SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,SCH_REV_NO,"
								+ "MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,"
								+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,"
								+ "BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,SYSDATE,"
								+ "?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_rev_no);
						stmt1.setString(8, sales_cont_map);
						stmt1.setString(9, gas_dt[i]);
						stmt1.setString(10, gen_dt[i]);
						stmt1.setString(11, gen_time[i]);
						stmt1.setInt(12, rev_no);
						stmt1.setString(13, mdq[i]);
						stmt1.setString(14, mdq_unit[i]);
						stmt1.setString(15, base[i]);
						stmt1.setString(16, gcv[i]);
						stmt1.setString(17, ncv[i]);
						stmt1.setString(18, qty_mmbtu[i]);
						stmt1.setString(19, qty_scm[i]);
						stmt1.setString(20, exit_base[i]);
						stmt1.setString(21, exit_gcv[i]);
						stmt1.setString(22, exit_ncv[i]);
						stmt1.setString(23, exit_qty_mmbtu[i]);
						stmt1.setString(24, exit_qty_scm[i]);
						stmt1.setString(25, emp_cd);
						stmt1.setString(26, bu_plant_seq);
						stmt1.setString(27, entry_pt_mapping_id);
						stmt1.setString(28, exit_pt_mapping_id);
						stmt1.executeUpdate();
						
						msg = "Successful! - Gas Transporter Scheduling Submitted Successfully!";
						msg_type="S";
						
						stmt1.close();
					}
					else if(gtc_type.equals("A"))
					{	
						int rev_no=-1;
						query = "SELECT NVL(MAX(ALLOC_REV_NO),?) "
								+ "FROM FMS_DAILY_TRANSPORTER_ALLOC "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND BU_SEQ=? AND SELL_CONT_MAP LIKE ? "
								+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=?";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, "-1");
						stmt.setString(2, cont_no);
						stmt.setString(3, agmt_no);
						stmt.setString(4, comp_cd);
						stmt.setString(5, counterparty_cd);
						stmt.setString(6, contract_type);
						stmt.setString(7, gas_dt[i]);
						stmt.setString(8, bu_plant_seq);
						stmt.setString(9, cont_map);
						stmt.setString(10, entry_pt_mapping_id);
						stmt.setString(11, exit_pt_mapping_id);
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
						
						
						query="INSERT INTO FMS_DAILY_TRANSPORTER_ALLOC(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
								+ "SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,ALLOC_REV_NO,"
								+ "MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,"
								+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,"
								+ "BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,ADJ_IMBALANCE) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,SYSDATE,"
								+ "?,?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_rev_no);
						stmt1.setString(8, sales_cont_map);
						stmt1.setString(9, gas_dt[i]);
						stmt1.setString(10, gen_dt[i]);
						stmt1.setString(11, gen_time[i]);
						stmt1.setInt(12, rev_no);
						stmt1.setString(13, mdq[i]);
						stmt1.setString(14, mdq_unit[i]);
						stmt1.setString(15, base[i]);
						stmt1.setString(16, gcv[i]);
						stmt1.setString(17, ncv[i]);
						stmt1.setString(18, qty_mmbtu[i]);
						stmt1.setString(19, qty_scm[i]);
						stmt1.setString(20, exit_base[i]);
						stmt1.setString(21, exit_gcv[i]);
						stmt1.setString(22, exit_ncv[i]);
						stmt1.setString(23, exit_qty_mmbtu[i]);
						stmt1.setString(24, exit_qty_scm[i]);
						stmt1.setString(25, emp_cd);
						stmt1.setString(26, bu_plant_seq);
						stmt1.setString(27, entry_pt_mapping_id);
						stmt1.setString(28, exit_pt_mapping_id);
						stmt1.setString(29, adjust_imb[i]);
						stmt1.executeUpdate();
						
						msg = "Successful! - Gas Transporter Allocation Submitted Successfully!";
						msg_type="S";
						
						stmt1.close();
					}
					else if(gtc_type.equals("N"))
					{	
						int rev_no=-1;
						query = "SELECT NVL(MAX(NOM_REV_NO),?) "
								+ "FROM FMS_DAILY_TRANSPORTER_NOM "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND BU_SEQ=? AND SELL_CONT_MAP LIKE ? "
								+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=?";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, "-1");
						stmt.setString(2, cont_no);
						stmt.setString(3, agmt_no);
						stmt.setString(4, comp_cd);
						stmt.setString(5, counterparty_cd);
						stmt.setString(6, contract_type);
						stmt.setString(7, gas_dt[i]);
						stmt.setString(8, bu_plant_seq);
						stmt.setString(9, cont_map);
						stmt.setString(10, entry_pt_mapping_id);
						stmt.setString(11, exit_pt_mapping_id);
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
						
						query="INSERT INTO FMS_DAILY_TRANSPORTER_NOM(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
								+ "SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,"
								+ "MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,"
								+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,"
								+ "BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID) "
								+ "VALUES(?,?,?,?,?,?,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,SYSDATE,"
								+ "?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_rev_no);
						stmt1.setString(8, sales_cont_map);
						stmt1.setString(9, gas_dt[i]);
						stmt1.setString(10, gen_dt[i]);
						stmt1.setString(11, gen_time[i]);
						stmt1.setInt(12, rev_no);
						stmt1.setString(13, mdq[i]);
						stmt1.setString(14, mdq_unit[i]);
						stmt1.setString(15, base[i]);
						stmt1.setString(16, gcv[i]);
						stmt1.setString(17, ncv[i]);
						stmt1.setString(18, qty_mmbtu[i]);
						stmt1.setString(19, qty_scm[i]);
						stmt1.setString(20, exit_base[i]);
						stmt1.setString(21, exit_gcv[i]);
						stmt1.setString(22, exit_ncv[i]);
						stmt1.setString(23, exit_qty_mmbtu[i]);
						stmt1.setString(24, exit_qty_scm[i]);
						stmt1.setString(25, emp_cd);
						stmt1.setString(26, bu_plant_seq);
						stmt1.setString(27, entry_pt_mapping_id);
						stmt1.setString(28, exit_pt_mapping_id);
						stmt1.executeUpdate();
						
						msg = "Successful! - Gas Transporter Nomination Submitted Successfully!";
						msg_type="S";
						
						stmt1.close();
					}
				}
			}
			
			url = "../gta/frm_gta_nom_sch_alloc.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&gtc_type="+gtc_type+
					"&contract_type="+contract_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&customer_cd="+customer_cd+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateContractBillingDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateContractBillingDetail()";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
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
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_GTA_CONT_TRANS_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_TRANS_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, cont_no);
				stmt3.setString(4, agmt_no);
				stmt3.setString(5, contract_type);
				stmt3.setString(6, agreement_type);
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
							+ "FROM FMS_GTA_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND CONTRACT_TYPE=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ_NO=? AND AGMT_TYPE=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, eff_dt);
					stmt.setString(7, plant_seq);
					stmt.setString(8, agreement_type);
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
						query="UPDATE FMS_GTA_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ_NO=? AND AGMT_TYPE=?";
						stmt1 = dbcon.prepareStatement(query);
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
						stmt1.setString(++cnt, agreement_type);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_GTA_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,"
								+ "EFF_DT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,AGMT_TYPE) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
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
						stmt1.setString(++cnt1, agreement_type);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - Data submitted Successfully!";
					msg_type = "S";
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - Data submission Failed!";
				msg_type = "E";
			}
			
			url = "../gta/frm_gta_contract_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&agreement_type="+agreement_type+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateGtpaBillingDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateGtpaBillingDetail()";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			
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
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				String queryString="SELECT PLANT_SEQ "
						+ "FROM FMS_GTA_ENTRY_POINT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_ENTRY_POINT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
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
							+ "FROM FMS_GTA_AGMT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
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
						query="UPDATE FMS_GTA_AGMT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt1 = dbcon.prepareStatement(query);
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
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_GTA_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,"
								+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,"
								+ "?,?,?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
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
						stmt1.setString(++cnt1, billing_days);
						stmt1.setString(++cnt1, exchg_val);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					
					msg = "Successful! - Data submitted Successfully!";
					msg_type = "S";
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - Data submission Failed!";
				msg_type = "E";
			}
			
			url = "../gta/frm_gtpa_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception!";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(servletName, function_nm, infoLogger);
		}
	}
	
	private void updateGtaParkingContractBillingTransBu(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String trans_bu, String new_trans_bu) throws Exception
	{
		String function_nm="updateGtaParkingContractBillingTransBu()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_GTA_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, trans_bu);
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
					+ "FROM FMS_GTA_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_trans_bu);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(billing_count > 0 && new_billing_count == 0)
			{
				query ="UPDATE FMS_GTA_BILLING_DTL A SET PLANT_SEQ_NO=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, new_trans_bu);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, trans_bu);
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
	
	private void updateGtaParkingContractBillingEffectiveDate(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String start_dt, String new_start_dt) throws Exception
	{
		String function_nm="updateGtaParkingContractBillingEffectiveDate()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_GTA_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, start_dt);
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
					+ "FROM FMS_GTA_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=?";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_start_dt);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(billing_count > 0 && new_billing_count == 0)
			{
				query ="UPDATE FMS_GTA_BILLING_DTL SET EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, new_start_dt);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, start_dt);
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
	
	private void updateGtaAgmtBillingTransBu(String counterpty_cd, String agmt_no, String agmt_rev_no, String agreement_type, String trans_bu, String new_trans_bu, int new_trans_bu_cnt) throws Exception
	{
		String function_nm="updateGtaAgmtBillingTransBu()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, trans_bu);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int new_billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, new_trans_bu);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String hol_state=utilBean.getState_TIN(dbcon, comp_cd, counterpty_cd, "C", new_trans_bu);
			if(hol_state.equals(""))
			{
				hol_state="24";
			}
			if(billing_count > 0 && new_billing_count == 0)
			{
				if(new_trans_bu_cnt==1)
				{
					query ="UPDATE FMS_GTA_AGMT_BILLING_DTL A SET PLANT_SEQ_NO=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? "
							+ "AND AGMT_NO=? "
							+ "AND AGMT_TYPE=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, new_trans_bu);
					stmt.setString(2, comp_cd);
					stmt.setString(3, counterpty_cd);
					stmt.setString(4, trans_bu);
					stmt.setString(5, agmt_no);
					stmt.setString(6, agreement_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else
				{
					query="INSERT INTO FMS_GTA_AGMT_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,? "
							+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "
							+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_GTA_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
							+ "AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.PLANT_SEQ_NO=?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, new_trans_bu);
					stmt0.setString(2, hol_state);
					stmt0.setString(3, comp_cd);
					stmt0.setString(4, counterpty_cd);
					stmt0.setString(5, agmt_no);
					stmt0.setString(6, agreement_type);
					stmt0.setString(7, trans_bu);
					stmt0.setString(8, new_trans_bu);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
			}
			else
			{
				query ="UPDATE FMS_GTA_AGMT_BILLING_DTL A SET HOLIDAY_STATE=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND AGMT_TYPE=? AND HOLIDAY_STATE IS NULL";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, hol_state);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, new_trans_bu);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agreement_type);
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
