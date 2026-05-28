package com.etrm.fms.admin;

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
import com.etrm.fms.util.EncryptTest;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 14/09/2023 
//Status	  		: Developing

@WebServlet("/servlet/Frm_reset_password")
public class Frm_reset_password extends HttpServlet
{
	static String db_src_file_name="Frm_reset_password.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_reset_password";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String queryString1 = null;
	private static String queryString2= null;
	private static String queryString3 = null;
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
	
	public static String commonUrl_pra="";
	
	public static String pwd_max_lengh = "";
	public static String pwd_min_lengh = "";
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		String function_nm="doPost()";
		HttpSession session = request.getSession();
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
				
				option=request.getParameter("option")==null?"":request.getParameter("option");
				
				new_value="";
				old_value="";
				
				commonUrl_pra = "&u="+u;
				
				if(option.equalsIgnoreCase("FORGET_PASSWORD"))
				{
					ResetPassword(request);
				}
				else if(option.equalsIgnoreCase("RESET_PASSWORD"))
				{
					ResetPassword(request);
				}
				else if(option.equalsIgnoreCase("RELEASE_LOCK"))
				{
					ReleaseLock(request);
				}
			}
			else{
				System.out.println("db null");
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
		
		try {
		response.sendRedirect(url);
		} catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void ResetPassword(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ResetPassword()";
		msg="";
		msg_type="";
		url="";
		
		HttpSession session = request.getSession();
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		String env_context = utilBean.getAutomationKeyDetail("ENV_CONTEXT");
		
		try
		{
			String company_cd = request.getParameter("comp_cd")==null?"0":request.getParameter("comp_cd");
			String user_id = request.getParameter("inputUserID_nm")==null?"":request.getParameter("inputUserID_nm");
			String email_id = request.getParameter("inputEmail_Id")==null?"":request.getParameter("inputEmail_Id");
			String rpt_nm = request.getParameter("rpt_nm")==null?"":request.getParameter("rpt_nm");
			String emp_cd = "";
			
			email_id=email_id.toLowerCase();	//Pratham Bhatt 20240730: For solving email case-sensitivity issue
			
			if(user_id.equals(""))
			{
				queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM "
						+ "FROM FMS_EMP_MST A "
						+ "WHERE LOWER(EMAIL_ID)=? ";	//Added by Pratham for solving email case-sensitivity ussue
//						+ "WHERE EMAIL_ID=? AND COMPANY_CD=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, email_id.trim());
				rset=stmt.executeQuery();
				if(rset.next())
				{
					user_id =rset.getString(2)==null?"":rset.getString(2);
				}
			}
			
			if(!user_id.equals("")) 
			{
				queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM,A.EMAIL_ID,B.PASSWORD,A.COMPANY_CD,A.LOCK_STATUS,A.EMP_STATUS "
						+ "FROM FMS_EMP_MST A, FMS_EMP_PASSWD_MST B "
						+ "WHERE A.EMP_CD=B.EMP_CD AND A.EMP_UID=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, user_id.trim());
				rset=stmt.executeQuery();
				if(rset.next())
				{
					emp_cd = rset.getString(1)==null?"":rset.getString(1);
					String emp_nm = rset.getString(3)==null?"":rset.getString(3);
					String email = rset.getString(4)==null?"":rset.getString(4);
					String pwd = rset.getString(5)==null?"":rset.getString(5);
					comp_cd = rset.getString(6)==null?"":rset.getString(6);
					String lock_status = rset.getString(7)==null?"":rset.getString(7);
					String user_status = rset.getString(8)==null?"":rset.getString(8);
					
					//Pratham Bhatt
					email = email.toLowerCase();
					
					if(user_status.equals("Y")) 
					{
						if(!lock_status.equals("Y")) 
						{
							String sysdt = utilDate.getSysdate();
							
							queryString="SELECT USR_MIN,USR_MAX,PWD_LEN_MIN,PWD_LEN_MAX,PREV_PWD_NOREP,PWD_EXP_DAYS,PWD_EXP_REMDR,DORMNT_DAYS,MAX_ADMINISTRATOR,MAX_ADMIN "
									+ "FROM FMS_ADMIN_POLICY "
									+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "ORDER BY EFF_DT DESC";
							stmt=dbcon.prepareStatement(queryString);
							stmt.setString(1, sysdt);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								pwd_min_lengh = rset.getString(3)==null?"":rset.getString(3);
								pwd_max_lengh = rset.getString(4)==null?"":rset.getString(4);
							}
							
							String mailBody="";
							String gen_password = (new EncryptTest()).generatePassword(pwd_min_lengh);
							String encryGenPwd=(new EncryptTest()).encrypt(gen_password).toString();
							
							query = "UPDATE FMS_EMP_PASSWD_MST SET PASSWORD=?, LAST_CHANGE=SYSDATE, SYS_PASSWD=? "
									+ "WHERE EMP_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, encryGenPwd);
							stmt.setString(2, "Y");
							stmt.setString(3, emp_cd);
							stmt.executeUpdate();
							
							String subject=CommonVariable.app_name_sub+" "+env_context+": Password Reset for your account!";
							
							mailBody="<html>"
									+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
									+ "The new password for your "+CommonVariable.app_name+" account is : "
									+ "<font style='background:#00cc00' color='white'>"+gen_password+"</font>"
									+ "</span><br>";
							mailBody+=CommonVariable.mail_signature;
							mailBody+=CommonVariable.mail_disclaimer;
							mailBody+= "</html>";
														
							String mail_recipent = email;
							
							if(!mail_recipent.equals("") && !mailBody.equals(""))
							{
								mailDelv.sendMail(comp_cd,mail_recipent, subject, mailBody, "", "", "");
							}
							
							if(rpt_nm.equals("USER_DTL")) 
							{
								msg = "Email Sent with Reset Password details to "+emp_nm+"!";
								msg_type="S";
							}
							
							queryString2 = "UPDATE FMS_EMP_MST SET INVALID_LOGIN='0' "
					        		+ "WHERE EMP_UID=? ";
							stmt2=dbcon.prepareStatement(queryString2);
							stmt2.setString(1, user_id);
					        stmt2.executeUpdate();
					        
							dbcon.commit();
							
							String ip=request.getRemoteAddr();
							String audit_log_msg = "Email Sent with Reset Password details to "+emp_nm+"!";
							
							if(option.equalsIgnoreCase("FORGET_PASSWORD")) 
							{
								try
								{
									new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm,ip, "0", "Login Page","0","Login Page", old_value, new_value, audit_log_msg);  	
								}
								catch(Exception infoLogger)
								{
									new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
								}
							}
							else if (option.equalsIgnoreCase("RESET_PASSWORD")) 
							{
								try
								{
									new com.etrm.fms.util.InfoLogger().InsertInfoLogger((String)session.getAttribute("emp_cd"), comp_cd,(String)session.getAttribute("emp_nm"),ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, audit_log_msg);  	
								}
								catch(Exception infoLogger)
								{
									new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
								}
							}
						}
						else 
						{
							msg = "Your User Account is Locked, please contact system admin!";
							msg_type="E";
						}
					}
					else 
					{
						if(user_status.equals("N")) 
						{
							msg = "Your User Account is Disabled, please contact system admin!";
							msg_type="E";
						}
						else if(user_status.equals("D")) 
						{
							msg = "Your User Account is Dormant, please contact system admin!";
							msg_type="E";
						}
						else if(user_status.equals("R")) 
						{
							msg = "Your User Account is Removed, please contact system admin!";
							msg_type="E";
						}
					}
				}
				else 
				{
					msg = "Invalid UserId Entered!";
					msg_type="E";
				}
			}
			else 
			{
				msg = "Invalid Email ID Entered!";
				msg_type="E";
			}
			
			if(rpt_nm.equals("USER_DTL")) 
			{
				url = "../admin/rpt_user_details.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else 
			{
				url = "../home/login.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			
			/*if(option.equalsIgnoreCase("FORGET_PASSWORD")) 
			{
				try
				{
					new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), "0", "Login Page","0","Login Page", old_value, new_value, msg);  	
				}
				catch(Exception infoLogger)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
				}
			}
			else */
			String audit_log_msg = msg+" User Activity Notification Email generated!";
			
			if (option.equalsIgnoreCase("RESET_PASSWORD")) 
			{
				try
				{
					new com.etrm.fms.util.InfoLogger().InsertInfoLogger((String)session.getAttribute("emp_cd"), comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, audit_log_msg);  	
				}
				catch(Exception infoLogger)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
				}
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			if(option.equalsIgnoreCase("FORGET_PASSWORD")) 
			{
				msg = "Error in Exception! - Please contact System Administrator!";
				msg_type="E";
				url = "../home/login.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				msg = "Error in Exception!";
				url=CommonVariable.errorpage_url+"?e="+e;
			}
			
		}
	}

	private void ReleaseLock(HttpServletRequest request) throws SQLException 
	{
		String function_nm="ReleaseLock()";
		msg="";
		msg_type="";
		url="";
		
		String company_cd="";
		String user_id="";
		String emp_cd="";
		String emp_nm="";
		String ip=request.getRemoteAddr();
		
		try
		{
			company_cd = request.getParameter("comp_cd")==null?"0":request.getParameter("comp_cd");
			user_id = request.getParameter("inputUserID_nm")==null?"":request.getParameter("inputUserID_nm");
			String email_id = request.getParameter("inputEmail_Id")==null?"":request.getParameter("inputEmail_Id");
			
			email_id=email_id.toLowerCase();	//Pratham Bhatt 20240730: For solving email case-sensitivity issue
			
			if(!user_id.equals(""))
			{
				queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM "
						+ "FROM FMS_EMP_MST A "
						+ "WHERE EMP_UID=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, user_id);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					emp_cd =rset.getString(1)==null?"":rset.getString(1);
					emp_nm=rset.getString(3)==null?"":rset.getString(3);
				}
			}
			if(!email_id.equals(""))
			{
				queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM "
						+ "FROM FMS_EMP_MST A "
						+ "WHERE LOWER(EMAIL_ID)=? ";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, email_id.trim());
				rset=stmt.executeQuery();
				if(rset.next())
				{
					emp_cd =rset.getString(1)==null?"":rset.getString(1);
					emp_nm=rset.getString(3)==null?"":rset.getString(3);
				}
			}
			
			if(!emp_cd.equals("")) 
			{
				int lock_count=0;
				query="SELECT COUNT(*) "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_CD=? AND LOCK_STATUS=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, emp_cd);
				stmt.setString(2, "Y");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					lock_count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(lock_count > 0)
				{
					query = "UPDATE FMS_EMP_MST SET LOCK_STATUS='', MODIFY_DT=SYSDATE, MODIFY_BY=?, INVALID_LOGIN=? "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, emp_cd);
					stmt.setString(2, "0");
					stmt.setString(3, emp_cd);
					stmt.executeUpdate();
					stmt.close();
					
					String seq_no="1";
					query = "SELECT MAX(SEQ_NO) "
							+ "FROM FMS_EMP_MST_LOG "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, emp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					query="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,COMPANY_CD,UNIT_CD,"
							+ "REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
							+ "MOB_NO,COMPANY_CD,UNIT_CD,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS "
							+ "FROM FMS_EMP_MST "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, seq_no);
					stmt.setString(2, emp_cd);
					stmt.executeQuery();
					stmt.close();
					
					msg = "Self Unlock Successful!";
					msg_type="S";
					
					LockedUserMailBody(company_cd,emp_nm,emp_cd,"UnLocked");
				}
				else
				{
					msg="The User Account Self UnLocked Falied!";
					msg_type="E";
				}
			}
			else
			{
				msg="The User Account Self UnLocked Falied!";
				msg_type="E";
			}
			
			dbcon.commit();
			url = "../home/login.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg = "Error in Exception! - Please contact System Administrator!";
			msg_type="E";
			url = "../home/login.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		
		String audit_log_msg = msg+" User Activity Notification Email generated!";
		
		try
		{
			//new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, company_cd,emp_nm,ip, "0", "Login Page","0","Login Page", old_value, new_value, audit_log_msg);
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, "",emp_nm,ip, "0", "Login Page","0","Login Page", old_value, new_value, audit_log_msg);
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void LockedUserMailBody(String comp_cd,String user_nm,String user_cd,String lock_status) throws Exception
	{
		String mailBody="";
		String function_nm="LockedUserMailBody()";
		String env_context=utilBean.getAutomationKeyDetail("ENV_CONTEXT");
		try
		{
			String email_id="";
			
			query = "SELECT EMAIL_ID "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt1=dbcon.prepareStatement(query);
			stmt1.setString(1, user_cd);			
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				email_id = rset1.getString(1)==null?"":rset1.getString(1);
			}
			email_id = email_id.toLowerCase();
			String subject=CommonVariable.app_name_sub+" "+env_context+": User Self "+lock_status+"!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Hi <b>"+user_nm+"</b>,<br><br>"
					+ "Your "+CommonVariable.app_name+" account is Self "+lock_status+",<br>"
					+ "Please connect system admin for further assistance.";
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_lock_user = email_id;
			to_mail_lock_user+=","+utilBean.getToMailReceipentList("0","User Activity Notification","Admin","NA","On-Event");
			String cc_mail_lock_user ="";
			cc_mail_lock_user+= utilBean.getCcMailReceipentList("0","User Activity Notification","Admin","NA","On-Event");
			
			if(!to_mail_lock_user.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_lock_user, subject, mailBody, "", cc_mail_lock_user, "");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
}
