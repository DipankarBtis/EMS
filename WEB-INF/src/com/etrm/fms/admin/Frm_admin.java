package com.etrm.fms.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 14/09/2022 
//Status	  		: Developing

@WebServlet("/servlet/Frm_admin")
public class Frm_admin extends HttpServlet
{
	static String db_src_file_name="Frm_admin.java"; 
	public static Connection dbcon;
	
	public static String servletName = "Frm_admin";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String queryString1 = null;
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
					
					if(option.equalsIgnoreCase("MODULE_MST"))
					{
						InsertUpdateModuleDetail(request);
					}
					else if(option.equalsIgnoreCase("GROUP_MST"))
					{
						InsertUpdateGroupDetail(request);
					}
					else if(option.equalsIgnoreCase("MENU_MST"))
					{
						InsertUpdateMenuDetail(request);
					}
					else if(option.equalsIgnoreCase("ACCESS_RIGHT_MST"))
					{
						InsertUpdateAccessRightDetail(request);
					}
					else if(option.equalsIgnoreCase("USER_MST"))
					{
						InsertUpdateUserDetail(request);
					}
					else if(option.equalsIgnoreCase("CHANGE_PASSWORD"))
					{
						ChangePassword(request);
					}
					else if(option.equalsIgnoreCase("USER_DTL"))
					{
						InsertUpdateUserDetailsStatus(request);
					}
					else if(option.equalsIgnoreCase("ACCESS_GROUP_ALLOCATION"))
					{
						InsertUpdateAccessGroupAllocation(request);
					}
					else if(option.equalsIgnoreCase("PASSWORD_POLICY"))
					{
						InsertUpdatePasswordPolicy(request);
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
		} catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdatePasswordPolicy(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		
		String function_nm="InsertUpdatePasswordPolicy()";
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String uid_min_length = request.getParameter("uid_min_length")==null?"":request.getParameter("uid_min_length");
			String uid_max_length = request.getParameter("uid_max_length")==null?"":request.getParameter("uid_max_length");
			String pid_min_length = request.getParameter("pid_min_length")==null?"":request.getParameter("pid_min_length");
			String pid_max_length = request.getParameter("pid_max_length")==null?"":request.getParameter("pid_max_length");
			String no_password_notrep = request.getParameter("no_password_notrep")==null?"":request.getParameter("no_password_notrep");
			String password_exp = request.getParameter("password_exp")==null?"":request.getParameter("password_exp");
			String rem_days = request.getParameter("rem_days")==null?"":request.getParameter("rem_days");
			String dormant_days = request.getParameter("dormant_days")==null?"":request.getParameter("dormant_days");
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String max_admn = request.getParameter("max_admn")==null?"":request.getParameter("max_admn");
			String max_sup_admn = request.getParameter("max_sup_admn")==null?"":request.getParameter("max_sup_admn");
			//String user_nm = utilBean.getEmpName(user_cd);
			
			//below variables are for configuring OTP 
			String otp_timer = request.getParameter("otp_timer")==null?"":request.getParameter("otp_timer");
			String max_otp = request.getParameter("max_otp")==null?"":request.getParameter("max_otp");
			String otp_max_length = request.getParameter("otp_max_length")==null?"":request.getParameter("otp_max_length");
			String otp_min_length = request.getParameter("otp_min_length")==null?"":request.getParameter("otp_min_length");
			
			if(!eff_dt.equals("") && !uid_min_length.equals("") && !uid_max_length.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_ADMIN_POLICY "
						+ "WHERE EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, eff_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					query = "UPDATE FMS_ADMIN_POLICY SET USR_MIN=?,USR_MAX=?,PWD_LEN_MIN=?,PWD_LEN_MAX=?,"
							+ "PREV_PWD_NOREP=?,PWD_EXP_DAYS=?,PWD_EXP_REMDR=?,DORMNT_DAYS=?,"
							+ "MAX_ADMINISTRATOR=?,MAX_ADMIN=?,MODIFY_BY=?,MODIFY_DT=SYSDATE, "
							+ "OTP_TIMER=?,MAX_OTP=?,OTP_LEN_MAX=?,OTP_LEN_MIN=?,MOD_PROFILE=? "	//Pratham Bhatt 20241025: for otp values
							+ "WHERE EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, uid_min_length);
					stmt.setString(2, uid_max_length);
					stmt.setString(3, pid_min_length);
					stmt.setString(4, pid_max_length);
					stmt.setString(5, no_password_notrep);
					stmt.setString(6, password_exp);
					stmt.setString(7, rem_days);
					stmt.setString(8, dormant_days);
					stmt.setString(9, max_admn);
					stmt.setString(10, max_sup_admn);
					stmt.setString(11, emp_cd);
					stmt.setString(12, otp_timer);
					stmt.setString(13, max_otp);
					stmt.setString(14, otp_max_length);
					stmt.setString(15, otp_min_length);
					stmt.setString(16, comp_cd);
					stmt.setString(17, eff_dt);
					stmt.executeUpdate();
					stmt.close();
					
					msg = "Successful! - Admin Policy dated "+eff_dt+" Updated!";
					msg_type="S";
					dbcon.commit();
				}
				else
				{
					query = "INSERT INTO FMS_ADMIN_POLICY(ENT_PROFILE,USR_MIN,USR_MAX,PWD_LEN_MIN,"
							+ "PWD_LEN_MAX,PREV_PWD_NOREP,PWD_EXP_DAYS,"
							+ "PWD_EXP_REMDR,DORMNT_DAYS,EFF_DT,MAX_ADMINISTRATOR,MAX_ADMIN,"
							+ "ENT_BY,ENT_DT,OTP_TIMER,MAX_OTP,OTP_LEN_MAX,OTP_LEN_MIN) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?,?,?,?) ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, uid_min_length);
					stmt.setString(3, uid_max_length);
					stmt.setString(4, pid_min_length);
					stmt.setString(5, pid_max_length);
					stmt.setString(6, no_password_notrep);
					stmt.setString(7, password_exp);
					stmt.setString(8, rem_days);
					stmt.setString(9, dormant_days);
					stmt.setString(10, eff_dt);
					stmt.setString(11, max_admn);
					stmt.setString(12, max_sup_admn);
					stmt.setString(13, emp_cd);
					stmt.setString(14, otp_timer);
					stmt.setString(15, max_otp);
					stmt.setString(16, otp_max_length);
					stmt.setString(17, otp_min_length);
					stmt.executeUpdate();
					
					stmt.close();
					
					msg = "Successful! - Admin Policy Inserted Effective from "+eff_dt+"!";
					msg_type="S";
					dbcon.commit();
				}
			}
			
			url = "../admin/frm_password_policy.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg = "Error in Exception! - Admin Policy Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e; 
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateUserDetailsStatus(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		
		String function_nm="InsertUpdateUserDetailsStatus()";
		
		String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
		String user_cd="0";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			user_cd= request.getParameter("emp_cd")==null?"0":request.getParameter("emp_cd");
			String user_nm = request.getParameter("user_nm")==null?"":request.getParameter("user_nm");
			String change_status = request.getParameter("change_status")==null?"":request.getParameter("change_status");
			String mod_type = request.getParameter("mod_type")==null?"":request.getParameter("mod_type");
					
			boolean chflg=false;
			if(mod_type.equals("lock_status")) 
			{
				if(!user_cd.trim().equals(""))
				{
					if(!user_cd.equals("0") && change_status.equals("N"))
					{
						query = "UPDATE FMS_EMP_MST SET LOCK_STATUS=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,MOD_PROFILE=? "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "Y");
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, user_cd);
						stmt.executeUpdate();
						stmt.close();
						msg = "Successful! - User "+user_nm+" Is Locked. User Activity Notification Email generated!";
						msg_type="S";
						
						//Added By HM20231106 : For Generating Mail for user get Locked
						LockedUserMailBody(comp_cd,user_nm,user_cd,"Locked");
						
						chflg = true;
					}
					else if(!user_cd.equals("0") && change_status.equals("Y"))
					{
						query = "UPDATE FMS_EMP_MST SET LOCK_STATUS = NULL, MODIFY_DT=SYSDATE, MODIFY_BY=?, INVALID_LOGIN=?,MOD_PROFILE=? "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, emp_cd);
						stmt.setString(2, "0");
						stmt.setString(3, comp_cd);
						stmt.setString(4, user_cd);
						stmt.executeUpdate();
						stmt.close();
						msg = "Successful! - User "+user_nm+" Is Unlocked. User Activity Notification Email generated!";
						msg_type="S";
									
						//Added By HM20231106 : For Generating Mail for user get Unlocked
						LockedUserMailBody(comp_cd,user_nm,user_cd,"Unlocked");
						
						chflg = true;
					}
					else
					{
						msg = "Failed! - User "+user_nm+" modification failed!";
						msg_type="E";
					}

					if(chflg)
					{
						String seq_no="1";
						query = "SELECT MAX(SEQ_NO) "
								+ "FROM FMS_EMP_MST_LOG "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							seq_no=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						query="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,ENT_PROFILE,UNIT_CD,"
								+ "REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS,MOD_PROFILE) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
								+ "MOB_NO,ENT_PROFILE,UNIT_CD,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS,MOD_PROFILE "
								+ "FROM FMS_EMP_MST "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, seq_no);
						stmt.setString(2, user_cd);
						stmt.executeQuery();
						stmt.close();
					}
					dbcon.commit();
				}
				else
				{
					msg = "Failed! - User "+user_nm+" can not be Locked!";
					msg_type="E";
				}
			}
			else if(mod_type.equals("user_status")) 
			{
				if(!user_cd.trim().equals(""))
				{
					if(!user_cd.equals("0") && change_status.equals("N"))
					{
						query = "UPDATE FMS_EMP_MST SET EMP_STATUS=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,MOD_PROFILE=? "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "Y");
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, user_cd);
						stmt.executeQuery();
						stmt.close();
						msg = "Successful! - User "+user_nm+" Is Enabled. User Activity Notification Email generated!";
						msg_type="S";
						
						//Added By HM20231120 : For Generating Mail for user get Enabled
						LockedUserMailBody(comp_cd,user_nm,user_cd,"Enabled");
						
						chflg = true;
					}
					else if(!user_cd.equals("0") && change_status.equals("Y"))
					{
						query = "UPDATE FMS_EMP_MST SET EMP_STATUS =?, MODIFY_DT=SYSDATE, MODIFY_BY=?,MOD_PROFILE=? "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "N");
						stmt.setString(2, emp_cd);
						stmt.setString(3, comp_cd);
						stmt.setString(4, user_cd);
						stmt.executeQuery();
						stmt.close();
						msg = "Successful! - User "+user_nm+" Is Disabled. User Activity Notification Email generated!";
						msg_type="S";
						
						//Added By HM202311020 : For Generating Mail for user get Disabled
						LockedUserMailBody(comp_cd,user_nm,user_cd,"Disabled");
						
						chflg = true;
					}
					else if(!user_cd.equals("0") && change_status.equals("D"))
					{
						query = "UPDATE FMS_EMP_MST SET EMP_STATUS=?, MODIFY_DT=SYSDATE, MODIFY_BY=?,IS_RECOVERED=?,RECOVERED_ON=SYSDATE,MOD_PROFILE=?  "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, "Y");
						stmt.setString(2, emp_cd);
						stmt.setString(3, "Y");
						stmt.setString(4, comp_cd);
						stmt.setString(5, user_cd);
						stmt.executeQuery();
						stmt.close();
						msg = "Successful! - User "+user_nm+" Is Recovered from Dormant. User Activity Notification Email generated!";
						msg_type="S";
						
						//Added By HM20231120 : For Generating Mail for user get Recovered from Dormant
						LockedUserMailBody(comp_cd,user_nm,user_cd,"Recovered from Dormant");
						
						chflg = true;
					}
					else
					{
						msg = "Failed! - User "+user_nm+" modification failed!";
						msg_type="E";
					}
					
					if(chflg)
					{
						String seq_no="1";
						query = "SELECT MAX(SEQ_NO) "
								+ "FROM FMS_EMP_MST_LOG "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							seq_no=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						query="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,ENT_PROFILE,UNIT_CD,"
								+ "REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS,MOD_PROFILE) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
								+ "MOB_NO,ENT_PROFILE,UNIT_CD,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,LOCK_STATUS,MOD_PROFILE "
								+ "FROM FMS_EMP_MST "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, seq_no);
						stmt.setString(2, user_cd);
						stmt.executeQuery();
						rset.close();
						stmt.close();
					}
					dbcon.commit();
				}
				else
				{
					msg = "Failed! - User "+user_nm+" can not be Locked!";
					msg_type="E";
				}
			}
			url = "../admin/rpt_user_details.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			if(opration.equals("MODIFY"))
			{
				msg = "Error in Exception! - User Detail Modification Failed!";
			}
			else
			{
				msg = "Error in Exception! - User Detail Addition Failed!";
			}			
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateModuleDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateModuleDetail()";
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		String trail_msg = "";
		
		try
		{
			String module_cd[] = request.getParameterValues("module_cd");
			String module_nm[] = request.getParameterValues("module_nm");
			String module_priority[] = request.getParameterValues("module_priority");
			String active_flag[] = request.getParameterValues("active_flag");
			
			String new_status_nm = "";
			String old_status_nm = "";
			
			if(module_cd != null)
			{
				for(int i=0; i<module_cd.length; i++)
				{
					if(!module_cd[i].equals(""))
					{
						int count=0;
						String prev_mod_cd= "";
						String prev_mod_nm = "";
						String prev_mod_prio = "";
						String prev_mod_status = "";
						
						if(active_flag[i].equals("Y")) 
						{
							new_status_nm = "Active";
						}
						else if(active_flag[i].equals("N")) 
						{
							new_status_nm = "Inactive";
						}
						
						query="SELECT COUNT(*) FROM FMS_MODULE_MST WHERE MODULE_CD=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, module_cd[i]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count = rset.getInt(1);
						}
						stmt.close();
						rset.close();	
						if(count > 0)
						{
							query1="SELECT MODULE_CD,MODULE_NAME,MODULE_PRIORITY,ACTIVE "
									+ "FROM FMS_MODULE_MST "
									+ "WHERE MODULE_CD=?";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, module_cd[i]);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								prev_mod_cd = rset1.getString(1)==null?"":rset1.getString(1);
								prev_mod_nm = rset1.getString(2)==null?"":rset1.getString(2);
								prev_mod_prio = rset1.getString(3)==null?"":rset1.getString(3);
								prev_mod_status = rset1.getString(4)==null?"":rset1.getString(4);
								
								if(prev_mod_status.equals("Y")) 
								{
									old_status_nm = "Active";
								}
								else if(prev_mod_status.equals("N")) 
								{
									old_status_nm = "Inactive";
								}
							}
							rset1.close();
							stmt1.close();
							if(!prev_mod_nm.equals(module_nm[i]) || !prev_mod_prio.equals(module_priority[i]) || !prev_mod_status.equals(active_flag[i])) 
							{
								query = "UPDATE FMS_MODULE_MST SET MODULE_NAME=?, MODULE_PRIORITY=? ,ACTIVE=?, "
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE MODULE_CD=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, module_nm[i]);
								stmt.setString(2, module_priority[i]);
								stmt.setString(3, active_flag[i]);
								stmt.setString(4, emp_cd);
								stmt.setString(5, module_cd[i]);
								stmt.executeUpdate();
								stmt.close();
								trail_msg += ""+prev_mod_nm+" Module Detail Modification Successful!";
								
								if(!prev_mod_nm.equals(module_nm[i])) 
								{
									new_value+="Name="+module_nm[i]+"#";
									old_value+="Name="+prev_mod_nm+"#";
								}
								
								if(!prev_mod_prio.equals(module_priority[i])) 
								{
									new_value+="Priority="+module_priority[i]+"#";
									old_value+="Priority="+prev_mod_prio+"#";
								}
								
								if(!prev_mod_status.equals(active_flag[i])) 
								{
									new_value+="Status="+new_status_nm+"#";
									old_value+="Status="+old_status_nm+"#";
								}
							}
						}
						else
						{
							query = "INSERT INTO FMS_MODULE_MST(MODULE_CD,MODULE_NAME,MODULE_PRIORITY,ACTIVE,ENT_BY,ENT_DT) "
									+ "VALUES (?,?,?,?,?,sysdate)";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, module_cd[i]);
							stmt.setString(2, module_nm[i]);
							stmt.setString(3, module_priority[i]);
							stmt.setString(4, active_flag[i]);
							stmt.setString(5, emp_cd);
							stmt.executeUpdate();
							stmt.close();
							trail_msg += ""+module_nm[i]+" Module Detail Insertion Successful!";
							
							new_value+="Name="+module_nm[i]+"#";
							new_value+="Priority="+module_priority[i]+"#";
							new_value+="Status="+new_status_nm+"#";
						}
						msg = "Successful! - Module Detail Addition/Modification Successful!";
						msg_type="S";
						dbcon.commit();
					}
				}
			}
			url = "../admin/frm_add_modify_module.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			msg = "Error in Exception! - Module Detail Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, trail_msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateGroupDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateGroupDetail()";
		String trail_msg="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String group_cd[] = request.getParameterValues("group_cd");
			String group_nm[] = request.getParameterValues("group_nm");
			String active_flag[] = request.getParameterValues("active_flag");
			String group_owner[] = request.getParameterValues("group_owner");
			
			String prev_group_nm = "";
			String prev_active_flag = "";
			String prev_group_owner = "";
			
			if(group_cd != null)
			{
				for(int i=0; i<group_cd.length; i++)
				{
					if(!group_cd[i].equals(""))
					{
						int count=0;

						String new_active_flag_nm = "";
						String old_active_flag_nm = "";
						String new_group_owner = "";
						
						if(active_flag[i].equals("Y"))
						{
							new_active_flag_nm = "Active";
						}
						else if(active_flag[i].equals("N"))
						{
							new_active_flag_nm = "Inactive";
						}
						
						if(group_owner[i].equals("0"))
						{
							new_group_owner = "";
						}
						else
						{
							new_group_owner = group_owner[i];
						}
						
						query="SELECT COUNT(*) "
								+ "FROM FMS_GROUP_MST "
								+ "WHERE GROUP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, group_cd[i]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count = rset.getInt(1);
						}
						rset.close();
						stmt.close();
						if(count > 0)
						{
							query1 ="SELECT GROUP_NAME,ACTIVE,GROUP_OWNER "
									+ "FROM FMS_GROUP_MST "
									+ "WHERE GROUP_CD=? ";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, group_cd[i]);
							rset1 = stmt1.executeQuery();
							while(rset1.next())
							{
								prev_group_nm = rset1.getString(1)==null?"":rset1.getString(1);
								prev_active_flag = rset1.getString(2)==null?"":rset1.getString(2);
								prev_group_owner = rset1.getString(3)==null?"":rset1.getString(3);
							}
							stmt1.close();
							rset1.close();
							
							if(prev_active_flag.equals("Y"))
							{
								old_active_flag_nm = "Active";
							}
							else if(prev_active_flag.equals("N"))
							{
								old_active_flag_nm = "Inactive";
							}
							if(!prev_group_nm.equals(group_nm[i]) || !prev_active_flag.equals(active_flag[i])|| !prev_group_owner.equals(new_group_owner))
							{
								query = "UPDATE FMS_GROUP_MST SET GROUP_NAME=?,ACTIVE=?, "
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=?,GROUP_OWNER=? "
										+ "WHERE GROUP_CD=? ";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, group_nm[i]);
								stmt.setString(2, active_flag[i]);
								stmt.setString(3, emp_cd);
								stmt.setString(4, comp_cd);
								stmt.setString(5, new_group_owner);
								stmt.setString(6, group_cd[i]);
								stmt.executeUpdate();
								stmt.close();
								trail_msg += ""+prev_group_nm+" Group Detail Modification Successful!";
								
								if(!prev_group_nm.equals(group_nm[i])) 
								{
									new_value+="Name="+group_nm[i]+"#";
									old_value+="Name="+prev_group_nm+"#";
								}
								
								if(!prev_active_flag.equals(active_flag[i])) 
								{
									new_value+="Status="+new_active_flag_nm+"#";
									old_value+="Status="+old_active_flag_nm+"#";
								}
								if(!prev_group_owner.equals(new_group_owner))
								{
									new_value+="Group Owner="+new_group_owner+"#";
									old_value+="Group Owner="+prev_group_owner+"#";
								}
							}
						}
						else
						{
							query = "INSERT INTO FMS_GROUP_MST(GROUP_CD,GROUP_NAME,ACTIVE,ENT_BY,ENT_PROFILE,ENT_DT,GROUP_OWNER) "
									+ "VALUES (?,?,?,?,?,sysdate,?)";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, group_cd[i]);
							stmt.setString(2, group_nm[i]);
							stmt.setString(3, active_flag[i]);
							stmt.setString(4, emp_cd);
							stmt.setString(5, comp_cd);
							stmt.setString(6, group_owner[i]);
							stmt.executeUpdate();
							stmt.close();
							trail_msg += ""+group_nm[i]+" Group Detail Insertion Successful!";
							
							new_value+="Name="+group_nm[i]+"#";
							new_value+="Status="+new_active_flag_nm+"#";
							new_value+="Group Owner="+new_group_owner+"#";
						}	
						msg = "Successful! - Access Group Detail Addition/Modification Successful!";
						msg_type="S";
						dbcon.commit();
					}
				}
			}
			url = "../admin/frm_add_modify_group.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			msg = "Error in Exception! - Group Detail Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateMenuDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateMenuDetail()";
		String trail_msg="";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
		
		try
		{
			String module_cd = request.getParameter("module_cd")==null?"":request.getParameter("module_cd");
			String menu_cd = request.getParameter("menu_cd")==null?"":request.getParameter("menu_cd");
			String menu_nm = request.getParameter("menu_nm")==null?"":request.getParameter("menu_nm");
			String menu_path = request.getParameter("menu_path")==null?"":request.getParameter("menu_path");
			String menu_type = request.getParameter("menu_type")==null?"":request.getParameter("menu_type");
			String group_cd = request.getParameter("group_cd")==null?"":request.getParameter("group_cd");
			String group_nm = request.getParameter("grpNm")==null?"":request.getParameter("grpNm");
			String new_group_nm = request.getParameter("new_grp_nm")==null?"":request.getParameter("new_grp_nm");
			String group_priority = request.getParameter("group_priority")==null?"":request.getParameter("group_priority");
			String status_flag = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
			String sei_central = request.getParameter("sei_central")==null?"":request.getParameter("sei_central");
			
			String prev_form_nm = "";
			String prev_sub_menu_nm = "";
			String prev_form_type = "";
			String prev_form_path = "";
			String prev_form_type_nm = "";
			String menu_type_nm = "";
			
			int grpcd=0;
			if(group_cd.equals("other"))
			{
				String gcd="0";
				String gnm=new_group_nm;
				query="SELECT DISTINCT(SUB_MENU_CD), SUB_MENU_NM FROM "
						+ "FMS_FORM_MST "
						+ "WHERE MODULE_CD=? AND UPPER(TRIM(SUB_MENU_NM)) =?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, module_cd);
				stmt.setString(2, new_group_nm.toString().toUpperCase().trim());
				rset=stmt.executeQuery();
				if(rset.next())
				{
					gcd = rset.getString(1)==null?"0":rset.getString(1);
					gnm = rset.getString(2)==null?new_group_nm:rset.getString(2);
				}
				stmt.close();
				rset.close();
				if(gcd.equals("0"))
				{
					query = "SELECT MAX(SUB_MENU_CD) "
							+ "FROM FMS_FORM_MST "
							+ "WHERE MODULE_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, module_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						grpcd = rset.getInt(1)+1;
					}
					else
					{
						grpcd+=1;
					}
					group_nm = new_group_nm;
					stmt.close();
					rset.close();
				}
				else
				{
					grpcd = Integer.parseInt(gcd);
					group_nm = gnm;
				}
				group_cd = ""+grpcd;
			}
			if(menu_type.equals("F")) 
			{
				menu_type_nm="Form";
			}
			else if (menu_type.equals("R")) 
			{
				menu_type_nm="Report";
			}
			
			if(opration.equals("MODIFY"))
			{
				if(!menu_cd.equals("") && !menu_cd.equals("0"))
				{
					query1 ="SELECT FORM_NAME,SUB_MENU_NM,FORM_TYPE,CLASSPATH "
							+ "FROM FMS_FORM_MST "
							+ "WHERE FORM_CD=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, menu_cd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						prev_form_nm = rset1.getString(1)==null?"":rset1.getString(1);
						prev_sub_menu_nm = rset1.getString(2)==null?"":rset1.getString(2);
						prev_form_type = rset1.getString(3)==null?"":rset1.getString(3);
						prev_form_path = rset1.getString(4)==null?"":rset1.getString(4);
					}
					stmt1.close();
					rset1.close();
					if(prev_form_type.equals("F")) 
					{
						prev_form_type_nm="Form";
					}
					else if (prev_form_type.equals("R")) 
					{
						prev_form_type_nm="Report";
					}
					
					query = "UPDATE FMS_FORM_MST SET FORM_NAME=?,MODULE_CD=?,CLASSPATH=?,FORM_TYPE=?,"
							+ "SUB_MENU_CD=?,SUB_MENU_NM=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,SUB_MENU_SEQ=?,FLAG=?,SEI_CENTRAL=? "
							+ "WHERE FORM_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, menu_nm);
					stmt.setString(2, module_cd);
					stmt.setString(3, menu_path);
					stmt.setString(4, menu_type);
					stmt.setString(5, group_cd);
					stmt.setString(6, group_nm);
					stmt.setString(7, emp_cd);
					stmt.setString(8, group_priority);
					stmt.setString(9, status_flag);
					stmt.setString(10, sei_central);
					stmt.setString(11, menu_cd);
					stmt.executeUpdate();
					
					trail_msg += ""+prev_form_nm+" Menu Detail Modification Successful!";
					
					if(!prev_form_nm.equals(menu_nm)) 
					{
						new_value+="Name="+menu_nm+"#";
						old_value+="Name="+prev_form_nm+"#";
					}
					
					if(!prev_sub_menu_nm.equals(group_nm)) 
					{
						new_value+="Group="+group_nm+"#";
						old_value+="Group="+prev_sub_menu_nm+"#";
					}
					
					if(!prev_form_type.equals(menu_type)) 
					{
						new_value+="Type="+menu_type_nm+"#";
						old_value+="Type="+prev_form_type_nm+"#";
					}
					if(!prev_form_path.equals(menu_path)) 
					{
						new_value+="(Path Has Been Changed)";
					}
					
					msg = "Successful! - Menu Detail Modification Successful!";
					msg_type="S";
					stmt.close();
					dbcon.commit();
				}
				else
				{
					msg = "Failed! - Menu Detail Addition Failed!";
					msg_type="E";
				}
			}
			else
			{
				int formCd=0;
				query = "SELECT MAX(FORM_CD) "
						+ "FROM FMS_FORM_MST";
				stmt=dbcon.prepareStatement(query);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					formCd=rset.getInt(1)+1;
				}
				else
				{
					formCd+=1;
				}
				stmt.close();
				rset.close();
				menu_cd = ""+formCd;
				
				query="INSERT INTO FMS_FORM_MST(FORM_CD,FORM_NAME,MODULE_CD,CLASSPATH,FORM_TYPE,FLAG,SUB_MENU_CD,SUB_MENU_NM,SUB_MENU_SEQ,ENT_BY,ENT_DT,SEI_CENTRAL) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,sysdate,?)";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, menu_cd);
				stmt.setString(2, menu_nm);
				stmt.setString(3, module_cd);
				stmt.setString(4, menu_path);
				stmt.setString(5, menu_type);
				stmt.setString(6, status_flag);
				stmt.setString(7, group_cd);
				stmt.setString(8, group_nm);
				stmt.setString(9, group_priority);
				stmt.setString(10, emp_cd);
				stmt.setString(11, sei_central);
				stmt.executeQuery();
				
				trail_msg += ""+menu_nm+" Menu Detail Insertion Successful!";
				
				new_value+="Name : "+menu_nm+", ";
				new_value+="Group : "+group_nm+", ";
				new_value+="Type : "+menu_type_nm+", ";
				
				msg = "Successful! - Menu Detail Addition Successful!";
				msg_type="S";
				stmt.close();
				dbcon.commit();
			}
			url = "../admin/frm_add_modify_menu.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			if(opration.equals("MODIFY"))
			{
				msg = "Error in Exception! - Menu Detail Modification Failed!";
			}
			else
			{
				msg = "Error in Exception! - Menu Detail Addition Failed!";
			}
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, trail_msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateAccessRightDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateAccessRightDetail()";
		String trail_msg ="";
		
		String module_cd = request.getParameter("module_cd")==null?"":request.getParameter("module_cd");
		String group_cd = request.getParameter("group_cd")==null?"":request.getParameter("group_cd");
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			String menu_cd[] = request.getParameterValues("menu_cd");
			String read_acs_flag[]=request.getParameterValues("read_acs_flag");
			String write_acs_flag[]=request.getParameterValues("write_acs_flag");
			String check_acs_flag[]=request.getParameterValues("check_acs_flag");
			String print_acs_flag[]=request.getParameterValues("print_acs_flag");
			String delete_acs_flag[]=request.getParameterValues("delete_acs_flag");
			String audit_acs_flag[]=request.getParameterValues("audit_acs_flag");
			String authorize_acs_flag[]=request.getParameterValues("authorize_acs_flag");
			String approve_acs_flag[]=request.getParameterValues("approve_acs_flag");
			String execute_acs_flag[]=request.getParameterValues("execute_acs_flag");
			
			String group_nm ="";
			String module_nm ="";
			
			String prev_read_acs_flag = "";
			String prev_write_acs_flag = "";
			String prev_check_acs_flag = "";
			String prev_print_acs_flag = "";
			String prev_delete_acs_flag= "";
			String prev_audit_acs_flag = "";
			String prev_authorize_acs_flag = "";
			String prev_approve_acs_flag = "";
			String prev_execute_acs_flag = "";
			
			query1 = "SELECT GROUP_NAME "
					+ "FROM FMS_GROUP_MST "
					+ "WHERE GROUP_CD=?";
			stmt1=dbcon.prepareStatement(query1);
			stmt1.setString(1, group_cd);
			rset = stmt1.executeQuery();
			while(rset.next()) 
			{
				group_nm = rset.getString(1)==null?"":rset.getString(1);
			}
			stmt1.close();
			rset.close();
			query2 = "SELECT MODULE_NAME "
					+ "FROM FMS_MODULE_MST "
					+ "WHERE MODULE_CD=?";
			stmt2=dbcon.prepareStatement(query2);
			stmt2.setString(1, module_cd);
			rset1 = stmt2.executeQuery();
			while(rset1.next()) 
			{
				module_nm = rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt2.close();
			
			if(menu_cd != null && !group_cd.equals("0"))
			{
				trail_msg += group_nm+" Access Right updated for:<br>";
				
				for(int i=0; i<menu_cd.length; i++)
				{
					query1="SELECT READ_ACCESS,WRITE_ACCESS,CHECK_ACCESS,"
							+ "PRINT_ACCESS,DELETE_ACCESS,AUDIT_ACCESS,"
							+ "AUTHORIZE_ACCESS,APPROVE_ACCESS,EXECUTE_ACCESS "
							+ "FROM FMS_GROUP_ACCESS "
							+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND FORM_CD=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, group_cd);
					stmt1.setString(3, menu_cd[i]);
					rset1 = stmt1.executeQuery();
					while(rset1.next()) 
					{
						prev_read_acs_flag = rset1.getString(1)==null?"":rset1.getString(1);
						prev_write_acs_flag = rset1.getString(2)==null?"":rset1.getString(2);
						prev_check_acs_flag = rset1.getString(3)==null?"":rset1.getString(3);
						prev_print_acs_flag = rset1.getString(4)==null?"":rset1.getString(4);
						prev_delete_acs_flag= rset1.getString(5)==null?"":rset1.getString(5);
						prev_audit_acs_flag = rset1.getString(6)==null?"":rset1.getString(6);
						prev_authorize_acs_flag = rset1.getString(7)==null?"":rset1.getString(7);
						prev_approve_acs_flag = rset1.getString(8)==null?"":rset1.getString(8);
						prev_execute_acs_flag = rset1.getString(9)==null?"":rset1.getString(9);
					}
					stmt1.close();
					rset1.close();
					
					String form_name="";
					
					query2 ="SELECT FORM_NAME,SUB_MENU_NM,FORM_TYPE,CLASSPATH "
							+ "FROM FMS_FORM_MST "
							+ "WHERE FORM_CD=?";
					stmt2=dbcon.prepareStatement(query2);
					stmt2.setString(1, menu_cd[i]);
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						form_name = rset2.getString(1)==null?"":rset2.getString(1);
					}
					stmt2.close();
					rset2.close();
					
					query = "DELETE FROM FMS_GROUP_ACCESS WHERE COMPANY_CD=? AND GROUP_CD=? AND FORM_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, group_cd);
					stmt.setString(3, menu_cd[i]);
					stmt.executeQuery();
					stmt.close();
					query = "INSERT INTO FMS_GROUP_ACCESS(COMPANY_CD,GROUP_CD,FORM_CD,"
							+ "READ_ACCESS,WRITE_ACCESS,CHECK_ACCESS,"
							+ "PRINT_ACCESS,DELETE_ACCESS,AUDIT_ACCESS,"
							+ "AUTHORIZE_ACCESS,APPROVE_ACCESS,EXECUTE_ACCESS) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, group_cd);
					stmt.setString(3, menu_cd[i]);
					stmt.setString(4, read_acs_flag[i]);
					stmt.setString(5, write_acs_flag[i]);
					stmt.setString(6, check_acs_flag[i]);
					stmt.setString(7, print_acs_flag[i]);
					stmt.setString(8, delete_acs_flag[i]);
					stmt.setString(9, audit_acs_flag[i]);
					stmt.setString(10, authorize_acs_flag[i]);
					stmt.setString(11, approve_acs_flag[i]);
					stmt.setString(12, execute_acs_flag[i]);
					stmt.executeQuery();
					stmt.close();
					if(!prev_approve_acs_flag.equals(approve_acs_flag[i])) 
					{
						new_value+="Approve("+approve_acs_flag[i]+").";
						old_value+="Approve("+prev_approve_acs_flag+").";
					}
					if(!prev_audit_acs_flag.equals(audit_acs_flag[i])) 
					{
						new_value+="Audit("+audit_acs_flag[i]+").";
						old_value+="Audit("+prev_audit_acs_flag+").";
					}
					if(!prev_authorize_acs_flag.equals(authorize_acs_flag[i])) 
					{
						new_value+="Authorize("+authorize_acs_flag[i]+").";
						old_value+="Authorize("+prev_authorize_acs_flag+").";
					}
					if(!prev_check_acs_flag.equals(check_acs_flag[i])) 
					{
						new_value+="Check("+check_acs_flag[i]+").";
						old_value+="Check("+prev_check_acs_flag+").";
					}
					if(!prev_delete_acs_flag.equals(delete_acs_flag[i])) 
					{
						new_value+="Delete("+delete_acs_flag[i]+").";
						old_value+="Delete("+prev_delete_acs_flag+").";
					}
					if(!prev_execute_acs_flag.equals(execute_acs_flag[i])) 
					{
						new_value+="Execute("+execute_acs_flag[i]+").";
						old_value+="Execute("+prev_execute_acs_flag+").";
					}
					if(!prev_print_acs_flag.equals(print_acs_flag[i])) 
					{
						new_value+="Print("+print_acs_flag[i]+").";
						old_value+="Print("+prev_print_acs_flag+").";
					}
					if(!prev_read_acs_flag.equals(read_acs_flag[i])) 
					{
						new_value+="Read("+read_acs_flag[i]+").";
						old_value+="Read("+prev_read_acs_flag+").";
					}
					if(!prev_write_acs_flag.equals(write_acs_flag[i])) 
					{
						new_value+="Write("+write_acs_flag[i]+").";
						old_value+="Write("+prev_write_acs_flag+").";
					}
					
					if(!prev_approve_acs_flag.equals(approve_acs_flag[i]) || 
							!prev_audit_acs_flag.equals(audit_acs_flag[i]) ||
							!prev_authorize_acs_flag.equals(authorize_acs_flag[i]) ||
							!prev_check_acs_flag.equals(check_acs_flag[i]) || 
							!prev_delete_acs_flag.equals(delete_acs_flag[i]) ||
							!prev_execute_acs_flag.equals(execute_acs_flag[i]) ||
							!prev_print_acs_flag.equals(print_acs_flag[i]) ||
							!prev_read_acs_flag.equals(read_acs_flag[i]) ||
							!prev_write_acs_flag.equals(write_acs_flag[i])
						) 
					{
						trail_msg += form_name;
						
						trail_msg+="<br>";

						new_value+="<br>";
						old_value+="<br>";
					}
					
					msg = "Successful! - "+module_nm+" Menu Access Right updated for "+group_nm+"!";
					msg_type="S";
					dbcon.commit();
				}
			}
			else
			{
				msg = "Failed! -  "+module_nm+" Menu Access Right update failed for "+group_nm+"!";
				msg_type="E";
			}
			
			url = "../admin/frm_add_modify_access_right.jsp?msg="+msg+"&msg_type="+msg_type+"&module_cd="+module_cd+"&group_cd="+group_cd+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			msg = "Error in Exception! - Access Right Detail Addition/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		//HM20240601 : No NEW message or new/old values are passed to log to due size exceeds for remars and values.
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateUserDetail(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="InsertUpdateUserDetail()";
		
		String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
		String user_cd="0";
		
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		try
		{
			user_cd= request.getParameter("user_cd")==null?"0":request.getParameter("user_cd");;
			String user_nm = request.getParameter("user_nm")==null?"":request.getParameter("user_nm");
			String user_id = request.getParameter("user_id")==null?"":request.getParameter("user_id");
			String email_id = request.getParameter("email_id")==null?"":request.getParameter("email_id");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			remark=escObj.replaceSingleQuotes(remark);
			String phone_no = request.getParameter("phone_no")==null?"":request.getParameter("phone_no");
			String mobile_no = request.getParameter("mobile_no")==null?"":request.getParameter("mobile_no");
			String default_profile = request.getParameter("default_profile")==null?"":request.getParameter("default_profile");
			
			String pwd_min_len = request.getParameter("pwd_min_len")==null?"":request.getParameter("pwd_min_len");
			String pwd_max_len = request.getParameter("pwd_max_len")==null?"":request.getParameter("pwd_max_len");
			
			boolean chflg=false;
			if(!user_nm.equals("") && !user_id.equals(""))
			{
				if(opration.equals("MODIFY"))
				{
					if(!user_cd.equals("0") && !user_cd.equals(""))
					{
						int count=0;
						query1 = "SELECT COUNT(*) "
								+ "FROM FMS_EMP_MST "
								+ "WHERE UPPER(EMAIL_ID)=? AND EMP_CD!=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(1, email_id.toUpperCase());
						stmt1.setString(2, user_cd);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							count=rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
											
						if(count==0) 
						{
							query = "UPDATE FMS_EMP_MST SET EMP_NM=?, EMAIL_ID=?, EMP_STATUS=?, REMARK=?,"
									+ "MODIFY_DT=SYSDATE, MODIFY_BY=?, PH_NO=?, MOB_NO=?,DEFAULT_PROFILE=?,MOD_PROFILE=? "
									+ "WHERE EMP_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, user_nm);
							stmt.setString(2, email_id);
							stmt.setString(3, status);
							stmt.setString(4, remark);
							stmt.setString(5, emp_cd);
							stmt.setString(6, phone_no);
							stmt.setString(7, mobile_no);
							stmt.setString(8, default_profile);
							stmt.setString(9, comp_cd);
							stmt.setString(10, user_cd);
							stmt.executeUpdate();
							stmt.close();
							
							msg = "Successful! - User "+user_nm+" updated!";
							msg_type="S";
							
							chflg = true;
							
						}
						else
						{
							msg = "Account already Exist for entered Email Id!";
							msg_type="E";
						}
					}
					else
					{
						msg = "Failed! - User "+user_nm+" modification failed!";
						msg_type="E";
					}
				}
				else // INSERT
				{
					int count=0;
					query1 = "SELECT COUNT(*) "
							+ "FROM FMS_EMP_MST "
							+ "WHERE UPPER(EMAIL_ID)=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, email_id.toUpperCase());
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					int count1=0;
					query1 = "SELECT COUNT(*) "
							+ "FROM FMS_EMP_MST "
							+ "WHERE EMP_UID=? ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, user_id);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(count1>1)
					{
						msg = "Account already Exist for entered User ID!";
						msg_type="E";
					}
					else if(count>1)
					{
						msg = "Account already Exist for entered Email ID!";
						msg_type="E";
					}
					else
					{
						query = "SELECT MAX(EMP_CD) FROM FMS_EMP_MST";
						stmt=dbcon.prepareStatement(query);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							user_cd = ""+(rset.getInt(1)+1);
						}
						else
						{
							user_cd="1";
						}
						rset.close();
						stmt.close();
						
						query = "INSERT INTO FMS_EMP_MST(EMP_CD,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,REMARK,ENT_DT,ENT_BY,ENT_PROFILE,"
								+ "PH_NO,MOB_NO,DEFAULT_PROFILE) "
								+ "VALUES(?,SYSDATE,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						stmt.setString(2, user_nm);
						stmt.setString(3, user_id);
						stmt.setString(4, email_id);
						stmt.setString(5, status);
						stmt.setString(6, remark);
						stmt.setString(7, emp_cd);
						stmt.setString(8, comp_cd);
						stmt.setString(9, phone_no);
						stmt.setString(10, mobile_no);
						stmt.setString(11, default_profile);
						stmt.executeUpdate();
						stmt.close();
						
						String gen_password = (new EncryptTest()).generatePassword(pwd_min_len);
						String password=(new EncryptTest()).encrypt(gen_password).toString();
						
						query = "INSERT INTO FMS_EMP_PASSWD_MST(EMP_CD,ENT_PROFILE,PASSWORD,LOGIN_STATUS,LAST_CHANGE,"
								+ "LOCKED_FLAG,RESET_FLAG,ENT_DT,ENT_BY,SYS_PASSWD) "
								+ "VALUES(?,?,?,?,SYSDATE,?,?,SYSDATE,?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						stmt.setString(2, comp_cd);
						stmt.setString(3, password);
						stmt.setString(4, "");
						stmt.setString(5, "N");
						stmt.setString(6, "N");
						stmt.setString(7, emp_cd);
						stmt.setString(8, "Y");
						stmt.executeUpdate();
						stmt.close();
						
						String seq_no="1";
						query = "SELECT MAX(SEQ_NO) "
								+ "FROM FMS_EMP_PASSWD_DTL "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							seq_no=""+(rset.getInt(1)+1);
						}
						rset.close();
						stmt.close();
						
						query = "INSERT INTO FMS_EMP_PASSWD_DTL(ENT_PROFILE,EMP_CD,SEQ_NO,PASSWORD,LOGIN_STATUS,LAST_CHANGE,LOCKED_FLAG,RESET_FLAG,"
								+ "EXPIRY_DATE,ENT_DT,ENT_BY) "
								+ "SELECT ENT_PROFILE,EMP_CD,?,PASSWORD,LOGIN_STATUS,LAST_CHANGE,LOCKED_FLAG,RESET_FLAG,"
								+ "EXPIRY_DATE,ENT_DT,ENT_BY "
								+ "FROM FMS_EMP_PASSWD_MST "
								+ "WHERE EMP_CD=? ";	
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, seq_no);
						stmt.setString(2, user_cd);
						stmt.executeQuery();
						stmt.close();
						
						InsertUserMailBody(request,comp_cd,user_nm,user_cd,user_id,email_id,gen_password);
						
						msg = "Successful! - User "+user_nm+" added. User Activity Notification Email generated!";
						msg_type="S";
						
						chflg = true;
					}
				}
				
				if(chflg)
				{
					String seq_no="1";
					query = "SELECT MAX(SEQ_NO) "
							+ "FROM FMS_EMP_MST_LOG "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, user_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						seq_no=""+(rset.getInt(1)+1);
					}
					rset.close();
					stmt.close();
					
					query="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,ENT_PROFILE,UNIT_CD,"
							+ "REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DEFAULT_PROFILE,MOD_PROFILE) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
							+ "MOB_NO,ENT_PROFILE,UNIT_CD,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DEFAULT_PROFILE,MOD_PROFILE "
							+ "FROM FMS_EMP_MST "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, seq_no);
					stmt.setString(2, user_cd);
					stmt.executeQuery();
					stmt.close();
				}
				dbcon.commit();
			}
			else
			{
				if(opration.equals("MODIFY"))
				{
					msg = "Failed! - User "+user_nm+" modification failed!";
				}
				else
				{
					msg = "Failed! - User "+user_nm+" addition failed!";
				}
				msg_type="E";
			}
			
			if(opration.equals("INSERT"))	
			{
				user_cd="0";
			}
			url = "../admin/frm_add_modify_user.jsp?msg="+msg+"&msg_type="+msg_type+"&opration="+opration+"&user_cd="+user_cd+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			if(opration.equals("MODIFY"))
			{
				msg = "Error in Exception! - User Detail Modification Failed!";
			}
			else
			{
				msg = "Error in Exception! - User Detail Addition Failed!";
			}
			url=CommonVariable.errorpage_url+"?e="+e; 
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void ChangePassword(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		String function_nm="ChangePassword()";
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		String isRequiredChngPwd = request.getParameter("isRequiredChngPwd")==null?"":request.getParameter("isRequiredChngPwd");
		
		try
		{
			String user_cd = request.getParameter("user_cd")==null?"0":request.getParameter("user_cd");
			String user_id = request.getParameter("user_id")==null?"":request.getParameter("user_id");
			String current_pwd = request.getParameter("current_pwd")==null?"":request.getParameter("current_pwd");
			String new_pwd = request.getParameter("new_pwd")==null?"":request.getParameter("new_pwd");
			String confirm_pwd = request.getParameter("confirm_pwd")==null?"":request.getParameter("confirm_pwd");
			String user_nm = utilBean.getEmpName(dbcon,user_cd);
			
			if(!user_cd.equals("") && !user_id.equals("") && !current_pwd.equals("") && !confirm_pwd.equals(""))
			{
				String sysdate = utilDate.getSysdate();
				String no_password_notrep = "";
				
				queryString="SELECT PREV_PWD_NOREP "
						+ "FROM FMS_ADMIN_POLICY "
						+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "ORDER BY EFF_DT DESC";
				stmt=dbcon.prepareStatement(queryString);
				stmt.setString(1, sysdate);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					no_password_notrep = rset.getString(1)==null?"":rset.getString(1);
				}
				rset.close();
				stmt.close();
				
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_CD=? AND EMP_UID=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, user_cd);
				stmt.setString(2, user_id);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					String existing_pwd="";
					query="SELECT PASSWORD "
							+ "FROM FMS_EMP_PASSWD_MST "
							+ "WHERE EMP_CD=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, user_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						existing_pwd=rset.getString(1)==null?"":rset.getString(1);
					}
					rset.close();
					stmt.close();
					
					current_pwd=(new EncryptTest()).encrypt(current_pwd).toString();
					current_pwd=(new EncryptTest()).decrypt(current_pwd).toString();
					existing_pwd=(new EncryptTest()).decrypt(existing_pwd).toString();
					
					if(current_pwd.equals(existing_pwd))
					{
						String max_seq = "0";
						
						query="SELECT MAX(SEQ_NO) "
								+ "FROM FMS_EMP_PASSWD_DTL "
								+ "WHERE EMP_CD=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							max_seq = rset.getString(1)==null?"0":rset.getString(1);
						}
						rset.close();
						stmt.close();
						
						int final_seq = Integer.parseInt(max_seq) - Integer.parseInt(no_password_notrep);
						
						count=0;
						String encryConfPwd=(new EncryptTest()).encrypt(confirm_pwd).toString();
						String temp_encryConfPwd=(new EncryptTest()).decrypt(encryConfPwd).toString();
						query="SELECT PASSWORD "
								+ "FROM FMS_EMP_PASSWD_DTL "
								+ "WHERE EMP_CD=? AND SEQ_NO >= ? "
								+ "ORDER BY SEQ_NO DESC ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, user_cd);
						stmt.setInt(2, final_seq);
						rset=stmt.executeQuery();
						while(rset.next())
						{
							String pwd = rset.getString(1)==null?"":rset.getString(1);
							pwd=(new EncryptTest()).decrypt(pwd).toString();
							if(temp_encryConfPwd.equals(pwd))
							{
								count++;	
								break;
							}
						}
						rset.close();
						stmt.close();
						
						if(count == 0)
						{
							query = "UPDATE FMS_EMP_PASSWD_MST SET PASSWORD=?,LAST_CHANGE=SYSDATE,SYS_PASSWD=? "
									+ "WHERE EMP_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, encryConfPwd);
							stmt.setString(2, "N");
							stmt.setString(3, user_cd);
							stmt.executeUpdate();
							stmt.close();
							
							String seq_no="1";
							query = "SELECT MAX(SEQ_NO) "
									+ "FROM FMS_EMP_PASSWD_DTL "
									+ "WHERE EMP_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, user_cd);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								seq_no=""+(rset.getInt(1)+1);
							}
							rset.close();
							stmt.close();
							
							query = "INSERT INTO FMS_EMP_PASSWD_DTL(EMP_CD,ENT_PROFILE,SEQ_NO,PASSWORD,LOGIN_STATUS,LAST_CHANGE,LOCKED_FLAG,RESET_FLAG,"
									+ "EXPIRY_DATE,ENT_DT,ENT_BY) "
									+ "SELECT EMP_CD,ENT_PROFILE,?,PASSWORD,LOGIN_STATUS,LAST_CHANGE,LOCKED_FLAG,RESET_FLAG,"
									+ "EXPIRY_DATE,ENT_DT,ENT_BY "
									+ "FROM FMS_EMP_PASSWD_MST "
									+ "WHERE EMP_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, seq_no);
							stmt.setString(2, user_cd);
							stmt.executeUpdate();
							stmt.close();
							
							msg = "Successful! -Password Modified for "+user_nm;
							msg_type="S";
							dbcon.commit();
						}
						else
						{
							msg = "Failed! - Repeatation of previously used Password not Allowed!";
							msg_type="E";
						}
					}
					else
					{
						msg = "Failed! - Incorrect Current Password!";
						msg_type="E";
					}
				}
				else
				{
					msg = "Failed! - User("+user_id+") does not Exist!";
					msg_type="E";
				}
			}
			else
			{
				msg = "Failed! - Password Modification Failed!";
				msg_type="E";
			}
			
			if (isRequiredChngPwd.equals("Y"))
			{
				url = "../admin/frm_requried_password_change.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url = "../admin/fms_change_password.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			msg = "Error in Exception! Password Modification Failed!";
			if (isRequiredChngPwd.equals("Y"))
			{
				url = "../admin/frm_requried_password_change.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			}
			else
			{
				url=CommonVariable.errorpage_url+"?e="+e;
			}
		}
		
		if(form_nm.equals("") || mod_nm.equals("") || mod_cd.equals("") || form_id.equals("")) // THis Is for Forced Password change page
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), "0", "Change Password","0","Login Page", old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
		else 
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	private void InsertUpdateAccessGroupAllocation(HttpServletRequest request) throws SQLException 
	{
		msg="";
		msg_type="";
		url="";
		
		new_value="";
		old_value="";
		String function_nm="InsertUpdateAccessGroupAllocation()";
		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
		
		String user_cd="0";
		try
		{
			user_cd = request.getParameter("user_cd")==null?"0":request.getParameter("user_cd");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String group_cd ="";
			if(opration.equals("Modify") || opration.equals("Delete")) 
			{
				group_cd = request.getParameter("temp_group_cd")==null?"":request.getParameter("temp_group_cd");
			}
			else 
			{
				group_cd = request.getParameter("group_cd")==null?"":request.getParameter("group_cd");
			}
			//String group_cd = request.getParameter("group_cd")==null?"":request.getParameter("group_cd");
			String group_nm = request.getParameter("group_nm")==null?"":request.getParameter("group_nm");
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt = request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String user_nm = utilBean.getEmpName(dbcon,user_cd);
			String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			
			new_value = "UserCd="+user_cd+"##GrpCd="+group_cd+"##GrpNm="+group_nm+"##FromDt="+from_dt+"##ToDt="+to_dt;
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			
			String sysdate = utilDate.getSysdate();
			
			if((!comp_cd.equals("0") && !comp_cd.equals("")) && (!user_cd.equals("0") && !user_cd.equals("")) && (!group_cd.equals("0") && !group_cd.equals("")) && !from_dt.equals("") && !to_dt.equals(""))
			{
				if(opration.equals("Delete"))
				{
					new_value="";
					old_value="";
					
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_EMP_GROUP_DTL "
							+ "WHERE COMPANY_CD=? AND GROUP_CD=? "
							+ "AND FROM_DT=TO_DATE(?,'DD/MM/YYYY') AND TO_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND EMP_CD=?";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, group_cd);
					stmt.setString(3, from_dt);
					stmt.setString(4, to_dt);
					stmt.setString(5, user_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count>0)
					{
						queryString="DELETE FROM FMS_EMP_GROUP_DTL "
								+ "WHERE COMPANY_CD=? AND GROUP_CD=? "
								+ "AND FROM_DT=TO_DATE(?,'DD/MM/YYYY') AND TO_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND EMP_CD=?";
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, group_cd);
						stmt.setString(3, from_dt);
						stmt.setString(4, to_dt);
						stmt.setString(5, user_cd);
						stmt.executeUpdate();
						stmt.close();
						dbcon.commit();
						
						msg = "Successful! - Access Group "+group_nm+" Deleted for "+user_nm;
						msg_type="S";
					}
					else
					{
						msg = "Failed! - Access Group "+group_nm+" Deletion failed for "+user_nm;
						msg_type="E";
					}
				}
				else
				{
					Vector VCURR_ADMINISTRATOR = new Vector();
					
					queryString1="SELECT GROUP_CD "
							+ "FROM FMS_EMP_GROUP_DTL "
							+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY') ";
					stmt1=dbcon.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "2");
					stmt1.setString(3, sysdate);
					stmt1.setString(4, sysdate);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VCURR_ADMINISTRATOR.add(rset1.getString(1)==null?"":rset1.getString(1));
					}
					rset1.close();
					stmt1.close();
					String max_administrators = "";
					
					queryString="SELECT MAX_ADMINISTRATOR "
							+ "FROM FMS_ADMIN_POLICY "
							+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "ORDER BY EFF_DT DESC";
					stmt1=dbcon.prepareStatement(queryString);
					stmt1.setString(1, sysdate);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						max_administrators = rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					int count=0;
					String curr_from_dt = "";
					String curr_to_dt = "";
					
					query = "SELECT TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY') "
							+ " FROM FMS_EMP_GROUP_DTL "
							+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, user_cd);
					stmt.setString(2, group_cd);
					stmt.setString(3, comp_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						curr_from_dt = rset.getString(1)==null?"":rset.getString(1);
						curr_to_dt = rset.getString(2)==null?"":rset.getString(2);
					}
					rset.close();
					stmt.close();
					query = "SELECT COUNT(*)"
							+ " FROM FMS_EMP_GROUP_DTL "
							+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=? AND SEQ_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, user_cd);
					stmt.setString(2, group_cd);
					stmt.setString(3, comp_cd);
					//stmt.setString(4, from_dt);
					//stmt.setString(5, to_dt);
					stmt.setString(4, seq_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					if(count > 0 && opration.equals("Modify"))
					{
						Vector VCURR_ADMINS = new Vector();
						
						queryString1="SELECT GROUP_CD "
								+ "FROM FMS_EMP_GROUP_DTL "
								+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY')";
						stmt1=dbcon.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, "1");
						stmt1.setString(3, sysdate);
						stmt1.setString(4, sysdate);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							VCURR_ADMINS.add(rset1.getString(1)==null?"":rset1.getString(1));
						}
						rset1.close();
						stmt1.close();
						
						String max_admins = "";
						
						queryString="SELECT MAX_ADMIN "
								+ "FROM FMS_ADMIN_POLICY "
								+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "ORDER BY EFF_DT DESC";
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, sysdate);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							max_admins = rset.getString(1)==null?"":rset.getString(1);
						}
						rset.close();
						stmt.close();
						
						if(VCURR_ADMINISTRATOR.size()>=Integer.parseInt(max_administrators) && group_cd.equals("2"))
						{
							boolean isAdministrator = false;
							
							queryString1="SELECT GROUP_CD "
									+ "FROM FMS_EMP_GROUP_DTL "
									+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? "
									+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY')";
							stmt1=dbcon.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, "2");
							stmt1.setString(3, user_cd);
							stmt1.setString(4, sysdate);
							stmt1.setString(5, sysdate);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								isAdministrator =true;
							}
							rset1.close();
							stmt1.close();
							if(isAdministrator) 
							{
								query="UPDATE FMS_EMP_GROUP_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
										+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=? AND SEQ_NO=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, from_dt);
								stmt.setString(2, to_dt);
								stmt.setString(3, emp_cd);
								stmt.setString(4, comp_cd);
								stmt.setString(5, user_cd);
								stmt.setString(6, group_cd);
								stmt.setString(7, comp_cd);
								stmt.setString(8, seq_no);
								stmt.executeUpdate();
								dbcon.commit();
								stmt.close();
								msg = "Successful! - Access Group "+group_nm+" updated for "+user_nm;
								msg_type="S";
							}
							else 
							{
								msg = "Miaximum Administrator Limit Exceeds!!";
								msg_type="E";
							}
						}
						else if(VCURR_ADMINS.size()>=Integer.parseInt(max_admins) && group_cd.equals("1"))
						{
							boolean isAdmin = false;
							
							queryString1="SELECT GROUP_CD "
									+ "FROM FMS_EMP_GROUP_DTL "
									+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND EMP_CD=? "
									+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY')";
							stmt1=dbcon.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, "1");
							stmt1.setString(3, user_cd);
							stmt1.setString(4, sysdate);
							stmt1.setString(5, sysdate);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								isAdmin =true;
							}
							rset1.close();
							stmt1.close();
							if(isAdmin) 
							{
								query="UPDATE FMS_EMP_GROUP_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
										+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=? AND SEQ_NO=?";
								stmt=dbcon.prepareStatement(query);
								stmt.setString(1, from_dt);
								stmt.setString(2, to_dt);
								stmt.setString(3, emp_cd);
								stmt.setString(4, comp_cd);
								stmt.setString(5, user_cd);
								stmt.setString(6, group_cd);
								stmt.setString(7, comp_cd);
								stmt.setString(8, seq_no);
								stmt.executeUpdate();
								dbcon.commit();
								
								stmt.close();
								msg = "Successful! - Access Group "+group_nm+" updated for "+user_nm;
								msg_type="S";
							}
							else 
							{
								msg = "Miaximum Admin Limit Exceeds!!";
								msg_type="E";
							}
						}
						else 
						{
							query="UPDATE FMS_EMP_GROUP_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
									+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,MOD_PROFILE=? "
									+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=? AND SEQ_NO=?";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, from_dt);
							stmt.setString(2, to_dt);
							stmt.setString(3, emp_cd);
							stmt.setString(4, comp_cd);
							stmt.setString(5, user_cd);
							stmt.setString(6, group_cd);
							stmt.setString(7, comp_cd);
							stmt.setString(8, seq_no);
							stmt.executeUpdate();
							dbcon.commit();
							stmt.close();
							msg = "Successful! - Access Group "+group_nm+" updated for "+user_nm;
							msg_type="S";
						}
					}
					else if(count > 0 && !opration.equals("Modify"))
					{
						msg = "Access Group "+group_nm+" Already Exist for "+user_nm;
						msg_type="E";
						
						new_value="";
					}
					else 
					{
						VCURR_ADMINISTRATOR.clear();
						Vector VCURR_ADMINS = new Vector();
						
						sysdate = utilDate.getSysdate();
						
						queryString1="SELECT GROUP_CD "
								+ "FROM FMS_EMP_GROUP_DTL "
								+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY')";
						stmt1=dbcon.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, "2");
						stmt1.setString(3, sysdate);
						stmt1.setString(4, sysdate);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							VCURR_ADMINISTRATOR.add(rset1.getString(1)==null?"":rset1.getString(1));
						}
						rset1.close();
						stmt1.close();
						queryString1="SELECT GROUP_CD "
								+ "FROM FMS_EMP_GROUP_DTL "
								+ "WHERE COMPANY_CD=? AND GROUP_CD=? AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT >=TO_DATE(?,'DD/MM/YYYY')";
						stmt1=dbcon.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, "1");
						stmt1.setString(3, sysdate);
						stmt1.setString(4, sysdate);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							VCURR_ADMINS.add(rset1.getString(1)==null?"":rset1.getString(1));
						}
						rset1.close();
						stmt1.close();
						max_administrators = "0";
						
						queryString="SELECT MAX_ADMINISTRATOR "
								+ "FROM FMS_ADMIN_POLICY "
								+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "ORDER BY EFF_DT DESC";
						stmt1=dbcon.prepareStatement(queryString);
						stmt1.setString(1, sysdate);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							max_administrators = rset1.getString(1)==null?"0":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						String max_admins = "0";
						
						queryString="SELECT MAX_ADMIN "
								+ "FROM FMS_ADMIN_POLICY "
								+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "ORDER BY EFF_DT DESC";
						stmt=dbcon.prepareStatement(queryString);
						stmt.setString(1, sysdate);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							max_admins = rset.getString(1)==null?"0":rset.getString(1);
						}
						rset.close();
						stmt.close();
						
						if(VCURR_ADMINISTRATOR.size()>=Integer.parseInt(max_administrators) && group_cd.equals("2"))
						{
							msg = "Miaximum Administrator Limit Exceeds!!";
							msg_type="E";
							
						}
						else if(VCURR_ADMINS.size()>=Integer.parseInt(max_admins) && group_cd.equals("1"))
						{
							msg = "Miaximum Admin Limit Exceeds!!";
							msg_type="E";
							
						}
						else 
						{
							seq_no="1";
							query = "SELECT MAX(SEQ_NO) "
									+ "FROM FMS_EMP_GROUP_DTL "
									+ "WHERE EMP_CD=? AND GROUP_CD=? AND COMPANY_CD=? ";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, user_cd);
							stmt.setString(2, group_cd);
							stmt.setString(3, comp_cd);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								seq_no=""+(rset.getInt(1)+1);
							}
							rset.close();
							stmt.close();
							query="INSERT INTO FMS_EMP_GROUP_DTL(COMPANY_CD,EMP_CD,GROUP_CD,FROM_DT,TO_DT,ENT_DT,ENT_BY,ENT_PROFILE,SEQ_NO) "
									+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),SYSDATE,?,?,?)";
							stmt=dbcon.prepareStatement(query);
							stmt.setString(1, comp_cd);
							stmt.setString(2, user_cd);
							stmt.setString(3, group_cd);
							stmt.setString(4, from_dt);
							stmt.setString(5, to_dt);
							stmt.setString(6, emp_cd);
							stmt.setString(7, comp_cd);
							stmt.setString(8, seq_no);
							stmt.executeUpdate();
							dbcon.commit();
							stmt.close();
							msg = "Successful! - Access Group "+group_nm+" added for "+user_nm;
							msg_type="S";
							old_value="";
						}
					}
				}
			}
			else
			{
				msg = "Failed! - Access Group Allocation/Modification Failed!";
				msg_type="E";
			}
			url = "../admin/frm_add_modify_group_allocation.jsp?msg="+msg+"&msg_type="+msg_type+"&user_cd="+user_cd+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			msg = "Error in Exception! - Access Group Allocation/Modification Failed!";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
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
		String env_context=utilBean.getAutomationKeyDetail(dbcon,"ENV_CONTEXT");
		try
		{
			String email_id="";
			
			query = "SELECT EMAIL_ID "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt1=dbcon.prepareStatement(query);
			stmt1.setString(1, user_cd);			
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				email_id = rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
			String subject=CommonVariable.app_name_sub+" "+env_context+": User "+lock_status+"!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Hi <b>"+user_nm+"</b>,<br><br>"
					+ "Your "+CommonVariable.app_name+" account is "+lock_status+",<br>";
					//+ "Please connect system admin for further assistance.";
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_lock_user = email_id+","+utilBean.getToMailReceipentList(dbcon,"0","User Activity Notification","Admin","NA","On-Event");
			String cc_mail_lock_user = utilBean.getCcMailReceipentList(dbcon,"0","User Activity Notification","Admin","NA","On-Event");
			
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
	
	private void InsertUserMailBody(HttpServletRequest request, String comp_cd,String user_nm,String user_cd,String user_id,String email_id, String password) throws Exception
	{
		String mailBody="";
		
		String SUPPORT_EMAIL_1 ="";
		String SUPPORT_EMAIL_2 ="";
		String function_nm="InsertUserMailBody()";
		String env_context = utilBean.getAutomationKeyDetail(dbcon,"ENV_CONTEXT");

		try
		{
			SUPPORT_EMAIL_1=utilBean.getKeyValueFromSrvSettingUsingKeyNm(dbcon,"SUPPORT_EMAIL_1");
			SUPPORT_EMAIL_2=utilBean.getKeyValueFromSrvSettingUsingKeyNm(dbcon,"SUPPORT_EMAIL_2");
			
			//Following Is The Application URL.
			String application_url = CommonVariable.app_protocol+"://"+request.getServerName()+":"+request.getServerPort()+request.getServletContext().getContextPath()+"/home/login.jsp";
		      
			String subject=CommonVariable.app_name_sub+" "+env_context+": New User Notification!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Hi <b>"+user_nm+"</b>,<br><br>"
					+ "Congratulations! "+CommonVariable.app_name+" </b>registration for you is completed! <br><br>"
					+ ""+CommonVariable.app_name+": <a href="+application_url+">"+application_url+"</a><br>"
					+ "<font style='font-style: italic;'>(Save above links to your Favourites for future use)</font><br><br>"
					+ "<b>User ID: </b>"+user_id+"<br>"
					+ "<b>Password: </b> Sent over separate Email <br>"
					+ "<font style='font-style: italic;'>(On your first login, please do change your password.)</font>"
					+ "<br><br>For any issues please raise your concern through Helpdesk's Incident Tracker. "
					+ "<br>You may also contact "+CommonVariable.app_name+" Support Team via email/s "+SUPPORT_EMAIL_1+" OR "+SUPPORT_EMAIL_2+".</span>";					
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
					
			
			String to_mail_new_user = email_id+","+utilBean.getToMailReceipentList(dbcon,"0","User Activity Notification","Admin","NA","On-Event");
			String cc_mail_lock_user = utilBean.getCcMailReceipentList(dbcon,"0","User Activity Notification","Admin","NA","On-Event");
			
			if(!to_mail_new_user.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_new_user, subject, mailBody, "", cc_mail_lock_user, "");
			}
			
			// Send Password in Separate Email
			subject=CommonVariable.app_name_sub+" "+env_context+": New User Password!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Hi <b>"+user_nm+"</b>,<br><br>"
					+ "Congratulations! <br><br>"
					+ "Password for your "+CommonVariable.app_name+" account : </b>"+password+" (Case sensitive)<br>"
					+ "<font style='font-style: italic;'>(On your first login, please do change your password.)</font></span>";					
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			to_mail_new_user = email_id;
			cc_mail_lock_user ="";
			
			if(!to_mail_new_user.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(comp_cd,to_mail_new_user, subject, mailBody, "", cc_mail_lock_user, "");
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
}
