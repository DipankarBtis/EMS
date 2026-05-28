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
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Jayasri Dhar
//Code Reviewed by	: Harsh Patel 
//CR Date			: 22/05/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_MapMaster")
public class Frm_MapMaster extends HttpServlet
{
	static String db_src_file_name="Frm_MapMaster.java";
	public static  Connection dbcon;
	
	static public String servletName = "Frm_MapMaster";
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
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	
	static private ResultSet rset = null;
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
					
					if(option.equalsIgnoreCase("MAP_CUSTOMER_CODE_MST"))
					{
						InsertUpdateCustomerCodeDetail(request);
					}
					else if (option.equalsIgnoreCase("MAP_CT_UTR_MST"))
					{
						InsertUpdateCTandUTRDetail(request);
					}
					else if (option.equalsIgnoreCase("MAP_CT_CONT_MAP"))
					{
						InsertUpdateCtContMapping(request);
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
	
	private void InsertUpdateCustomerCodeDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateCustomerCodeDetail()";
		try
		{
			String transporter_cd = request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
			String transporter_abbr = ""+utilBean.getCounterpartyABBR(dbcon,transporter_cd);
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String customer_plant_seq=request.getParameter("customer_plant_seq")==null?"":request.getParameter("customer_plant_seq");
			String customer_code = request.getParameter("customer_code")==null?"":request.getParameter("customer_code");
			String effective_dt = request.getParameter("effective_dt")==null?"":request.getParameter("effective_dt");
			String status_flag = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			//String unit_cd = request.getParameter("unit_cd")==null?"":request.getParameter("unit_cd");
			
			if(opration.equals("INSERT"))
			{
				int count = 0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_TRANSPORTER_CUST_CD "
						+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? "
						+ "AND COUNTERPARTY_CD=? AND PLANT_SEQ=? "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
				stmt = dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, transporter_cd);
				stmt.setString(3, counterparty_cd);
				stmt.setString(4, customer_plant_seq);
				stmt.setString(5, effective_dt);
				rset=stmt.executeQuery();			
				if(rset.next()) 
				{
					count=rset.getInt(1);
				} 
				rset.close();
				stmt.close();
				
				if(count == 0)
				{				
					query="INSERT INTO FMS_TRANSPORTER_CUST_CD (COMPANY_CD, TRANSPORTER_CD, COUNTERPARTY_CD, PLANT_SEQ, "
							+ "CUSTOMER_CODE, EFF_DT, STATUS, ENT_BY, ENT_DT) "
							+ "VALUES(?,?,?,?,"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, transporter_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, customer_plant_seq);
					stmt1.setString(5, customer_code);
					stmt1.setString(6, effective_dt);
					stmt1.setString(7, status_flag);
					stmt1.setString(8, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - New Transporter Assigned Customer Code Added Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Transporter Assigned Customer Code already exists in System!";
					msg_type = "E";
				}	
			}
			else if(opration.equals("MODIFY"))
			{				
				query="UPDATE FMS_TRANSPORTER_CUST_CD SET CUSTOMER_CODE=?,"
						+ "STATUS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE  "
						+ "WHERE COMPANY_CD=? AND TRANSPORTER_CD=? "
						+ "AND COUNTERPARTY_CD=? AND PLANT_SEQ=? "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, customer_code);
				stmt.setString(2, status_flag);
				stmt.setString(3, emp_cd);
				stmt.setString(4, comp_cd);
				stmt.setString(5, transporter_cd);
				stmt.setString(6, counterparty_cd);
				stmt.setString(7, customer_plant_seq);
				stmt.setString(8, effective_dt);
				stmt.executeUpdate();
				
				stmt.close();
				
				msg = "Successful! - Transporter Assigned Customer Code modified Successfully!";
				msg_type = "S";
			}
			
			url = "../gta/frm_transporter_cust_cd_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
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
	

	private void InsertUpdateCTandUTRDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateCTandUTRDetail()";
		try
		{
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			String transporter_cd = request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
			String transporter_abbr = ""+utilBean.getCounterpartyABBR(dbcon,transporter_cd);
			String transporter_plant_seq=request.getParameter("transporter_plant_seq")==null?"":request.getParameter("transporter_plant_seq");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String counterparty_plant_seq=request.getParameter("counterparty_plant_seq")==null?"":request.getParameter("counterparty_plant_seq");
			String ct_ref = request.getParameter("ct_ref")==null?"":request.getParameter("ct_ref");
			String utr = request.getParameter("utr")==null?"":request.getParameter("utr");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String status_flag = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			
			if(opration.equals("INSERT"))
			{
				int count = 0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_TRANSPORTER_CT_MST "
						+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
						+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
						+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? AND BU_SEQ=? "
						+ "AND CT_REF_NO=? ";
				stmt = dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, transporter_cd);
				stmt.setString(3, transporter_plant_seq);
				stmt.setString(4, contract_type);
				stmt.setString(5, counterparty_cd);
				stmt.setString(6, counterparty_plant_seq);
				stmt.setString(7, bu_unit);
				stmt.setString(8, ct_ref);
				rset=stmt.executeQuery();			
				if(rset.next()) 
				{
					count=rset.getInt(1);
				} 
				rset.close();
				stmt.close();
				
				queryString="SELECT NVL(MAX(SEQ_NO),0) "
						+ "FROM FMS_TRANSPORTER_CT_MST "
						+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
						+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
						+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? AND BU_SEQ=? ";
				stmt1 = dbcon.prepareStatement(queryString);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, transporter_cd);
				stmt1.setString(3, transporter_plant_seq);
				stmt1.setString(4, contract_type);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, counterparty_plant_seq);
				stmt1.setString(7, bu_unit);
				rset1=stmt1.executeQuery();			
				if(rset1.next()) 
				{
					seq_no=""+(rset1.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset1.close();
				stmt1.close();
				
				if(count == 0)
				{				
					query="INSERT INTO FMS_TRANSPORTER_CT_MST (COMPANY_CD, ENTRY_TRANS_CD, ENTRY_TRANS_PLANT, CONTRACT_TYPE, "
							+ "EXIT_COUNTERPARTY, EXIT_PLANT_SEQ, CT_REF_NO, UTR_NO, START_DT, END_DT, STATUS, ENT_BY, ENT_DT,SEQ_NO,BU_SEQ) "
							+ "VALUES(?,?,?,?,"
							+ "?,?,?, ?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,?,SYSDATE,?,?)";
					stmt2= dbcon.prepareStatement(query);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, transporter_cd);
					stmt2.setString(3, transporter_plant_seq);
					stmt2.setString(4, contract_type);
					stmt2.setString(5, counterparty_cd);
					stmt2.setString(6, counterparty_plant_seq);
					stmt2.setString(7, ct_ref);
					stmt2.setString(8, utr);
					stmt2.setString(9, start_dt);
					stmt2.setString(10, end_dt);
					stmt2.setString(11, status_flag);
					stmt2.setString(12, emp_cd);
					stmt2.setString(13, seq_no);
					stmt2.setString(14, bu_unit);
					stmt2.executeUpdate();
					
					stmt2.close();
					
					msg = "Successful! - New Transporter CT and UTR Added Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Transporter CT and UTR already exists in System!";
					msg_type = "E";
				}	
			}
			else if(opration.equals("MODIFY"))
			{				
				int count = 0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_TRANSPORTER_CT_MST "
						+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
						+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
						+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? AND BU_SEQ=? "
						+ "AND CT_REF_NO=? AND SEQ_NO!=? ";
				stmt = dbcon.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, transporter_cd);
				stmt.setString(3, transporter_plant_seq);
				stmt.setString(4, contract_type);
				stmt.setString(5, counterparty_cd);
				stmt.setString(6, counterparty_plant_seq);
				stmt.setString(7, bu_unit);
				stmt.setString(8, ct_ref);
				stmt.setString(9, seq_no);
				rset=stmt.executeQuery();			
				if(rset.next()) 
				{
					count=rset.getInt(1);
				} 
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					query="UPDATE FMS_TRANSPORTER_CT_MST SET CT_REF_NO=?, UTR_NO=?, "
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "STATUS=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
							+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
							+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? AND BU_SEQ=? "
							+ "AND SEQ_NO=? ";
					stmt2= dbcon.prepareStatement(query);
					stmt2.setString(1, ct_ref);
					stmt2.setString(2, utr);
					stmt2.setString(3, start_dt);
					stmt2.setString(4, end_dt);
					stmt2.setString(5, status_flag);
					stmt2.setString(6, emp_cd);
					stmt2.setString(7, comp_cd);
					stmt2.setString(8, transporter_cd);
					stmt2.setString(9, transporter_plant_seq);
					stmt2.setString(10, contract_type);
					stmt2.setString(11, counterparty_cd);
					stmt2.setString(12, counterparty_plant_seq);
					stmt2.setString(13, bu_unit);
					stmt2.setString(14, seq_no);
					stmt2.executeUpdate();
					
					stmt2.close();
					
					msg = "Successful! - Transporter CT and UTR modified Successfully!";
					msg_type = "S";
				}
				else
				{
					msg = "Transporter CT and UTR already exists in System!";
					msg_type = "E";
				}
			}
			
			url = "../gta/frm_transporter_ct_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
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
	
	private void InsertUpdateCtContMapping(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateCtContMapping()";
		try
		{
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String transporter_cd = request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
			String transporter_plant_seq = request.getParameter("transporter_plant_seq")==null?"":request.getParameter("transporter_plant_seq");
			String counterparty_plant_seq = request.getParameter("counterparty_plant_seq")==null?"":request.getParameter("counterparty_plant_seq");
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
			
			String[] cont_mapping=request.getParameterValues("cont_mapping");
			
			if(cont_mapping != null)
			{
				query = "DELETE FROM FMS_TRANS_CT_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND ENTRY_TRANS_CD=? "
						+ "AND ENTRY_TRANS_PLANT=? AND CONTRACT_TYPE=? "
						+ "AND EXIT_COUNTERPARTY=? AND EXIT_PLANT_SEQ=? "
						+ "AND SEQ_NO=? AND BU_SEQ=? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, transporter_cd);
				stmt.setString(3, transporter_plant_seq);
				stmt.setString(4, contract_type);
				stmt.setString(5, counterparty_cd);
				stmt.setString(6, counterparty_plant_seq);
				stmt.setString(7, seq_no);
				stmt.setString(8, bu_unit);
				stmt.executeUpdate();
				
				stmt.close();
				
				for(int i=0;i<cont_mapping.length;i++)
				{
					query="INSERT INTO FMS_TRANS_CT_CONT_MAP (COMPANY_CD, ENTRY_TRANS_CD, ENTRY_TRANS_PLANT, CONTRACT_TYPE, "
							+ "EXIT_COUNTERPARTY, EXIT_PLANT_SEQ, ENT_BY, ENT_DT,SEQ_NO,CONT_MAPPING,BU_SEQ) "
							+ "VALUES(?,?,?,?,"
							+ "?,?,?,SYSDATE,?,?,?)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, transporter_cd);
					stmt1.setString(3, transporter_plant_seq);
					stmt1.setString(4, contract_type);
					stmt1.setString(5, counterparty_cd);
					stmt1.setString(6, counterparty_plant_seq);
					stmt1.setString(7, emp_cd);
					stmt1.setString(8, seq_no);
					stmt1.setString(9, cont_mapping[i]);
					stmt1.setString(10, bu_unit);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - Transporter CT Contract Attachment Submitted Successfully!";
					msg_type = "S";
				}
			}
			
			url = "../gta/rpt_ct_sales_cont_list.jsp?counterparty_cd="+counterparty_cd+"&transporter_cd="+transporter_cd+
					"&transporter_plant_seq="+transporter_plant_seq+"&counterparty_plant_seq="+counterparty_plant_seq+
					"&start_dt="+start_dt+"&end_dt="+end_dt+"&contract_type="+contract_type+"&seq_no="+seq_no+"&bu_unit="+bu_unit+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			
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
}
