package com.etrm.fms.mgmt_reports;

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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_Govt_Report")
public class Frm_Govt_Report extends HttpServlet
{
	static String db_src_file_name="Frm_Govt_Report.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_Govt_Report";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
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
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
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
					
					if(option.equalsIgnoreCase("CAP_MASTER"))
					{
						InsertCapacityDetails(request);
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
	
	private void InsertCapacityDetails(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertCapacityDetails()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String month=request.getParameter("month")==null?"":request.getParameter("month");
			String year=request.getParameter("year")==null?"":request.getParameter("year");
			String month_to=request.getParameter("month_to")==null?"":request.getParameter("month_to");
			String year_to=request.getParameter("year_to")==null?"":request.getParameter("year_to");
			String cap_month=request.getParameter("cap_month")==null?"":request.getParameter("cap_month");
			String cap_year=request.getParameter("cap_year")==null?"":request.getParameter("cap_year");
			
			String cap_type=request.getParameter("cap_type")==null?"":request.getParameter("cap_type");
			String cap_val=request.getParameter("cap_val")==null?"":request.getParameter("cap_val");
			String cap_type_nm = getCapacityNm(cap_type);
			
			String eff_dt="01/"+cap_month+"/"+cap_year;
			String monthNm= utilDate.getMonthNameMON(eff_dt);
			
			if(!cap_type.equals("") && !cap_val.equals("") && !cap_year.equals("") && !cap_month.equals(""))
			{
				int count=0;
				query="SELECT COUNT(*) "
						+ "FROM FMS_GOVT_RPT_CONST "
						+ "WHERE COMPANY_CD=? AND CAP_TYPE=? "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, cap_type);
				stmt.setString(3, eff_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count>0)
				{
					query1="UPDATE FMS_GOVT_RPT_CONST " 
							+ "SET CAP_VALUE=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
							+ "WHERE COMPANY_CD=? AND CAP_TYPE=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, cap_val);
					stmt.setString(2, emp_cd);
					stmt.setString(3, comp_cd);
					stmt.setString(4, comp_cd);
					stmt.setString(5, cap_type);
					stmt.setString(6, eff_dt);
					stmt.executeUpdate();
					
					stmt.close();
					msg = "Successful! - "+cap_type_nm+" Value Modified Successfully for the "+monthNm+"-"+cap_year+"!";
					msg_type="S";
				}
				else
				{
					query1="INSERT INTO FMS_GOVT_RPT_CONST(CAP_TYPE,CAP_VALUE,COMPANY_CD,EFF_DT,ENT_BY,ENT_DT,ENT_PROFILE) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE,?)";
					stmt=dbcon.prepareStatement(query1);
					stmt.setString(1, cap_type);
					stmt.setString(2, cap_val);
					stmt.setString(3, comp_cd);
					stmt.setString(4, eff_dt);
					stmt.setString(5, emp_cd);
					stmt.setString(6, comp_cd);
					stmt.executeUpdate();
					
					stmt.close();
					msg = "Successful! - "+cap_type_nm+" Value Inserted Successfully for the "+monthNm+"-"+cap_year+"!";
					msg_type="S";
				}
			}
			else
			{
				msg = "Failed! - "+cap_type_nm+" Value Insertion/Updation failed for the "+monthNm+"-"+cap_year+"!";
				msg_type="E";
			}
			
			url = "../mgmt_reports/frm_govt_rpt_const.jsp?month="+month+"&year="+year+"&cap_month="+cap_month+"&cap_year="+cap_year
					+"&month_to="+month_to+"&year_to="+year_to
					+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! - Insert/Update Capacity Datails Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
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
	
	public String getCapacityNm(String type)
	{
		String function_nm="getCapacityNm()";
		String cap_nm="";
		try
		{
			if(type.equals("N"))
			{
				cap_nm="Nameplate Capacity";
			}
			else if(type.equals("M"))
			{
				cap_nm="MMSCMD/Nameplate capacity";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return cap_nm;
	}
}