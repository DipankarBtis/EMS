package com.etrm.fms.mail_recipient_config;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 18/10/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_Mail_Recipient_Config")
public class Frm_Mail_Recipient_Config extends HttpServlet
{
	static String db_src_file_name="Frm_Mail_Recipient_Config.java";
	public static Connection dbcon;
	
	public static String servletName = "Frm_Mail_Recipient_Config";
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
					
					if(option.equalsIgnoreCase("MAIL_RECIPIENT_CONFIG"))
					{
						InsertUpdateEmailRecipientDetail(request);
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
				if(rset != null){try {rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
		}
		
		try {
		response.sendRedirect(url);
		}catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateEmailRecipientDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateEmailRecipientDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String operation = request.getParameter("operation")==null?"":request.getParameter("operation");
			
			String mst_module_nm = request.getParameter("mst_module_nm")==null?"":request.getParameter("mst_module_nm");
			String mst_form_nm = request.getParameter("mst_form_nm")==null?"":request.getParameter("mst_form_nm");
			String mst_company = request.getParameter("mst_company")==null?"":request.getParameter("mst_company");
			
			String generation_type = request.getParameter("generation_type")==null?"":request.getParameter("generation_type");
			String recipient_cd = request.getParameter("recipient_cd")==null?"":request.getParameter("recipient_cd");
			String frequency = request.getParameter("frequency")==null?"":request.getParameter("frequency");
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String txt_stop_mail = request.getParameter("txt_stop_mail")==null?"":request.getParameter("txt_stop_mail");
			
			String del_recipient_cd = request.getParameter("del_recipient_cd")==null?"":request.getParameter("del_recipient_cd");
			String del_seq_no = request.getParameter("del_seq_no")==null?"":request.getParameter("del_seq_no");
			
			String MON = request.getParameter("MON")==null?"N":request.getParameter("MON");
    		String TUE = request.getParameter("TUE")==null?"N":request.getParameter("TUE");
    		String WED = request.getParameter("WED")==null?"N":request.getParameter("WED");
    		String THU = request.getParameter("THU")==null?"N":request.getParameter("THU");
    		String FRI = request.getParameter("FRI")==null?"N":request.getParameter("FRI");
    		String SAT = request.getParameter("SAT")==null?"N":request.getParameter("SAT");
    		String SUN = request.getParameter("SUN")==null?"N":request.getParameter("SUN");
    		
    		String[] to_emp_cd = request.getParameterValues("to_emp_cd");
    		String[] to_emp_email = request.getParameterValues("to_emp_email");
    		String[] to_emp_flag = request.getParameterValues("to_emp_flag");
    		
    		String[] cc_emp_cd = request.getParameterValues("cc_emp_cd");
    		String[] cc_emp_email = request.getParameterValues("cc_emp_email");
    		String[] cc_emp_flag = request.getParameterValues("cc_emp_flag");
    		
    		String[] to_email_id = request.getParameterValues("to_email_id");
    		String[] cc_email_id = request.getParameterValues("cc_email_id");
    		
    		if(operation.equals("DELETE"))
    		{
    			query="DELETE FROM FMS_EMAIL_RECIPIENT_DTL "
						+ "WHERE RECIPIENTS_CD=? AND SEQ_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, del_recipient_cd);
				stmt.setString(2, del_seq_no);
    			stmt.executeUpdate();
    			stmt.close();
				
				msg = "Successful! - Data Deleted Successfully!";
    			msg_type = "S";
    		}
    		else
    		{
	    		if(operation.equals("MODIFY"))
	    		{
	    			query="UPDATE FMS_EMAIL_RECIPIENT_MST SET STOP_FLAG=?,"
	    					+ "MON=?,TUE=?,WED=?,THU=?,FRI=?,SAT=?,SUN=?, "
	    					+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
	    					+ "WHERE COMPANY_CD=? AND UPPER(MODULE_NAME)=? AND UPPER(MENU_NAME)=? AND SEQ_NO=? "
	    					+ "AND UPPER(REPORT_FREQ)=? AND UPPER(GENERATION_TYPE)=? AND RECIPIENTS_CD=? ";
	    			stmt=dbcon.prepareStatement(query);
					stmt.setString(1, txt_stop_mail);
					stmt.setString(2, MON);
					stmt.setString(3, TUE);
					stmt.setString(4, WED);
					stmt.setString(5, THU);
					stmt.setString(6, FRI);
					stmt.setString(7, SAT);
					stmt.setString(8, SUN);
					stmt.setString(9, emp_cd);
					stmt.setString(10, comp_cd);
					stmt.setString(11, mst_company);
					stmt.setString(12, mst_module_nm.toUpperCase());
					stmt.setString(13, mst_form_nm.toUpperCase());
					stmt.setString(14, seq_no);
					stmt.setString(15, frequency.toUpperCase());
					stmt.setString(16, generation_type.toUpperCase());
					stmt.setString(17, recipient_cd);
	    			stmt.executeUpdate();
	    			stmt.close();
	    			
	    			msg = "Successful! - Data Modified Successfully!";
	    			msg_type = "S";
				}
	    		else
	    		{
	    			query="SELECT MAX(RECIPIENTS_CD) "
	    					+ "FROM FMS_EMAIL_RECIPIENT_MST ";
	    			stmt=dbcon.prepareStatement(query);
	    			rset=stmt.executeQuery();
	    			if(rset.next())
	    			{
	    				recipient_cd=""+(rset.getInt(1)+1);
	    			}
	    			else
	    			{
	    				recipient_cd="1";
	    			}
	    			rset.close();
	    			stmt.close();
	    			
	    			query="INSERT INTO FMS_EMAIL_RECIPIENT_MST(COMPANY_CD,RECIPIENTS_CD,MODULE_NAME,MENU_NAME,REPORT_FREQ,GENERATION_TYPE,STOP_FLAG,"
	    					+ "MON,TUE,WED,THU,FRI,SAT,SUN,ENT_BY,ENT_DT,SEQ_NO) "
	    					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
	    			stmt=dbcon.prepareStatement(query);
					stmt.setString(1, mst_company);
					stmt.setString(2, recipient_cd);
					stmt.setString(3, mst_module_nm);
					stmt.setString(4, mst_form_nm);
					stmt.setString(5, frequency);
					stmt.setString(6, generation_type);
					stmt.setString(7, txt_stop_mail);
					stmt.setString(8, MON);
					stmt.setString(9, TUE);
					stmt.setString(10, WED);
					stmt.setString(11, THU);
					stmt.setString(12, FRI);
					stmt.setString(13, SAT);
					stmt.setString(14, SUN);
					stmt.setString(15, emp_cd);
					stmt.setString(16, seq_no);
	    			stmt.executeUpdate();
	    			stmt.close();
	    			
	    			msg = "Successful! - Data Submitted Successfully!";
	    			msg_type = "S";
	    		}
	    		
	    		query="DELETE FROM FMS_EMAIL_RECIPIENT_DTL "
						+ "WHERE RECIPIENTS_CD=? ";
	    		stmt=dbcon.prepareStatement(query);
				stmt.setString(1, recipient_cd);
				stmt.executeUpdate();
	    		stmt.close();
	    		
	    		if(to_emp_cd!=null)
				{
					for(int i=0; i<to_emp_cd.length; i++)
					{
						if(to_emp_flag[i].equals("Y"))
						{
							String dtl_seq_no="";
	    					query = "SELECT MAX(SEQ_NO) "
	    							+ "FROM FMS_EMAIL_RECIPIENT_DTL "
	    							+ "WHERE RECIPIENTS_CD=?";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					rset = stmt.executeQuery();
	    					if(rset.next())
	    					{
	    						dtl_seq_no = ""+(rset.getInt(1)+1);
	    					}
	    					else
	    					{
	    						dtl_seq_no="1";
	    					}
	    					rset.close();
	    					stmt.close();
	    					
	    					query="INSERT INTO FMS_EMAIL_RECIPIENT_DTL(RECIPIENTS_CD,SEQ_NO,TO_EMP_CD,TO_EMAIL,ENABLE_DISABLE,ENT_BY,ENT_DT) "
	    							+ "VALUES(?,?,?,?,?,?,SYSDATE)";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					stmt.setString(2, dtl_seq_no);
	    					stmt.setString(3, to_emp_cd[i]);
	    					//stmt.setString(4, to_emp_email[i]);
	    					stmt.setString(4, "");
	    					stmt.setString(5, "Y");
	    					stmt.setString(6, emp_cd);
	    					stmt.executeUpdate();
	    					stmt.close();
						}
					}
				}
	    		
	    		if(cc_emp_cd!=null)
				{
					for(int i=0; i<cc_emp_cd.length; i++)
					{
						if(cc_emp_flag[i].equals("Y"))
						{
							String dtl_seq_no="";
	    					query = "SELECT MAX(SEQ_NO) "
	    							+ "FROM FMS_EMAIL_RECIPIENT_DTL "
	    							+ "WHERE RECIPIENTS_CD=? ";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					rset = stmt.executeQuery();
	    					if(rset.next())
	    					{
	    						dtl_seq_no = ""+(rset.getInt(1)+1);
	    					}
	    					else
	    					{
	    						dtl_seq_no="1";
	    					}
	    					rset.close();
	    					stmt.close();
	    					
	    					query="INSERT INTO FMS_EMAIL_RECIPIENT_DTL(RECIPIENTS_CD,SEQ_NO,CC_EMP_CD,CC_EMAIL,ENABLE_DISABLE,ENT_BY,ENT_DT) "
	    							+ "VALUES(?,?,?,?,?,?,SYSDATE)";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					stmt.setString(2, dtl_seq_no);
	    					stmt.setString(3, cc_emp_cd[i]);
	    					//stmt.setString(4, cc_emp_email[i]);
	    					stmt.setString(4, "");
	    					stmt.setString(5, "Y");
	    					stmt.setString(6, emp_cd);
	    					stmt.executeUpdate();
	    					stmt.close();
						}
					}
				}
	    		
	    		if(to_email_id != null)
	    		{
	    			for(int i=0; i<to_email_id.length; i++)
					{
						if(!to_email_id[i].toString().trim().equals(""))
						{
							String dtl_seq_no="";
	    					query = "SELECT MAX(SEQ_NO) "
	    							+ "FROM FMS_EMAIL_RECIPIENT_DTL "
	    							+ "WHERE RECIPIENTS_CD=? ";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					rset = stmt.executeQuery();
	    					if(rset.next())
	    					{
	    						dtl_seq_no = ""+(rset.getInt(1)+1);
	    					}
	    					else
	    					{
	    						dtl_seq_no="1";
	    					}
	    					rset.close();
	    					stmt.close();
	    					
	    					query="INSERT INTO FMS_EMAIL_RECIPIENT_DTL(RECIPIENTS_CD,SEQ_NO,TO_EMP_CD,TO_EMAIL,ENABLE_DISABLE,ENT_BY,ENT_DT) "
	    							+ "VALUES(?,?,?,?,?,?,SYSDATE)";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					stmt.setString(2, dtl_seq_no);
	    					stmt.setString(3, "0");
	    					stmt.setString(4, to_email_id[i]);
	    					stmt.setString(5, "Y");
	    					stmt.setString(6, emp_cd);
	    					stmt.executeUpdate();
	    					stmt.close();
						}
					}
	    		}
	    		
	    		if(cc_email_id != null)
	    		{
	    			for(int i=0; i<cc_email_id.length; i++)
					{
						if(!cc_email_id[i].toString().trim().equals(""))
						{
							String dtl_seq_no="";
	    					query = "SELECT MAX(SEQ_NO) "
	    							+ "FROM FMS_EMAIL_RECIPIENT_DTL "
	    							+ "WHERE RECIPIENTS_CD=? ";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					rset = stmt.executeQuery();
	    					if(rset.next())
	    					{
	    						dtl_seq_no = ""+(rset.getInt(1)+1);
	    					}
	    					else
	    					{
	    						dtl_seq_no="1";
	    					}
	    					rset.close();
	    					stmt.close();
	    					
	    					query="INSERT INTO FMS_EMAIL_RECIPIENT_DTL(RECIPIENTS_CD,SEQ_NO,CC_EMP_CD,CC_EMAIL,ENABLE_DISABLE,ENT_BY,ENT_DT) "
	    							+ "VALUES(?,?,?,?,?,?,SYSDATE)";
	    					stmt=dbcon.prepareStatement(query);
	    					stmt.setString(1, recipient_cd);
	    					stmt.setString(2, dtl_seq_no);
	    					stmt.setString(3, "0");
	    					stmt.setString(4, cc_email_id[i]);
	    					stmt.setString(5, "Y");
	    					stmt.setString(6, emp_cd);
	    					stmt.executeUpdate();
	    					stmt.close();
						}
					}
	    		}
    		}
			
			url = "../mail_recipient_config/frm_mail_recipient_config.jsp?msg="+msg+"&msg_type="+msg_type+"&sel_form_nm="+mst_form_nm+"&sel_module_nm="+mst_module_nm+
					"&sel_seq_no="+seq_no+"&sel_freq="+frequency+"&sel_type="+generation_type+"&company="+mst_company+commonUrl_pra;
			
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
