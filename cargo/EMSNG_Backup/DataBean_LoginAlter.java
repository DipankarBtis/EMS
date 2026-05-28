package com.etrm.fms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 12/09/2022 
//Status	  		: Developing

public class DataBean_LoginAlter 
{
	String db_src_file_name="DataBean_LoginAlter.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	String query="";
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	MailDelivery mailDelv = new MailDelivery();
	
	public void init()
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
	    	if(initContext == null)
	    	{
	    		throw new Exception("Boom - No Context");
	    	}
	    	
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null) 
	    	{
	    		conn = ds.getConnection();       
	    		if(conn != null)  
	    		{
	    			if(callFlag.equalsIgnoreCase("LOGIN_PAGE"))
	    			{
	    				CompanyMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LOGIN_CHECK"))
	    			{
	    				LoginCheck();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LOCK_USER"))
	    			{
	    				LockUser();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENTITY_DETAIL"))
	    			{
	    				EntityDetail();
	    			}
	    		}
	    		
	    		conn.close();
    			conn = null;
	    	}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void LockUser() 
	{
		String function_nm="LockUser()";
		try 
		{
			//System.out.println(emplo_cd);
			
			queryString="SELECT A.LOCK_STATUS,A.EMP_STATUS "
					+ "FROM FMS_EMP_MST A, FMS_EMP_PASSWD_MST B "
					+ "WHERE A.EMP_CD=B.EMP_CD AND A.EMP_UID=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, username);
			ResultSet rset=stmt.executeQuery();
			if(rset.next())
			{
				String lock_status = rset.getString(1)==null?"":rset.getString(1);
				String emp_status = rset.getString(2)==null?"":rset.getString(2);
				
				if(emp_status.equals("Y")) 
				{
					if(!lock_status.equals("Y")) 
					{
						Timestamp ts = new Timestamp(System.currentTimeMillis());
				        String tsString = ts.toString().substring(0, 19);
				        String tsDate = tsString.substring(0, 10);
				        String time = tsString.substring(11, 19);
				        
				        String seq_no1="1";
				        queryString5 = "SELECT INVALID_LOGIN "
				        		+ "FROM FMS_EMP_MST "
								+ "WHERE EMP_CD=? ";
				        stmt3=conn.prepareStatement(queryString5);
				        stmt3.setString(1, emplo_cd);
						ResultSet rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							seq_no1=""+(rset3.getInt(1)+1);
							
							if(Integer.parseInt(seq_no1)>3) 
							{
								seq_no1 = "1";
							}
						}
						rset3.close();
						stmt3.close();
						
						/*queryString3 = "INSERT INTO FMS_ALL_LOG(LOG_DT, LOG_TIME, LOG_UID, LOG_MACH_ID, REMARKS, EMP_CD, COMPANY_CD,FORM_CD, FORM_NAME,"
				        		+ "MODULE_CD,MODULE_NAME,OLD_VALUE, NEW_VALUE) "
				        		+ "VALUES(to_date(?,'yyyy-mm-dd'), ?, ?, ?, ?, ?, ?,?, ?,"
				        		+ "?,?,?,?)";
						stmt2=conn.prepareStatement(queryString3);
						stmt2.setString(1, tsDate);
						stmt2.setString(2, time);
						stmt2.setString(3, emplo_nm);
						stmt2.setString(4, ip);
						stmt2.setString(5, "Invalid Username or Password");
						stmt2.setString(6, emplo_cd);
						stmt2.setString(7, company_cd);
						stmt2.setString(8, "0");
						stmt2.setString(9, "Login Page");
						stmt2.setString(10, "0");
						stmt2.setString(11, "Login Page");
						stmt2.setString(12, "");
						stmt2.setString(13, "");
				        stmt2.executeUpdate();
				        stmt2.close();
				        */
						
				        queryString1 = "UPDATE FMS_EMP_MST SET INVALID_LOGIN=?, INVALID_LOGIN_ON=SYSDATE "
				        		+ "WHERE EMP_CD=? ";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, seq_no1);
				        stmt1.setString(2, emplo_cd);
				        stmt1.executeUpdate();
				        stmt1.close();
					}
				}
			}
			rset.close();
			stmt.close();
	        
			queryString2 ="SELECT INVALID_LOGIN "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, emplo_cd);
			ResultSet rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String invalid_login_count =rset2.getString(1)==null?"":rset2.getString(1);
				VLOGIN_ATTAMPT.add(invalid_login_count);
			}
			rset2.close();
			stmt2.close();
			
			if(VLOGIN_ATTAMPT.elementAt(0).equals("3"))
			{
				queryString4 = "UPDATE FMS_EMP_MST SET LOCK_STATUS=? "
						+ "WHERE EMP_CD=? ";
				stmt=conn.prepareStatement(queryString4);
				stmt.setString(1, "Y");
				stmt.setString(2, emplo_cd);
				stmt.executeUpdate();
				stmt.close();
				
				String seq_no="1";
				query = "SELECT MAX(SEQ_NO) "
						+ "FROM FMS_EMP_MST_LOG "
						+ "WHERE EMP_CD=? ";
				stmt1=conn.prepareStatement(query);
				stmt1.setString(1, emplo_cd);
				ResultSet rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					seq_no=""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
				
				query="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,COMPANY_CD,UNIT_CD,"
						+ "REMARK,MODIFY_DT,MODIFY_BY,LOCK_STATUS) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
						+ "MOB_NO,COMPANY_CD,UNIT_CD,REMARK,SYSDATE,?,LOCK_STATUS "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_CD=? ";
				stmt=conn.prepareStatement(query);
				stmt.setString(1, seq_no);
				stmt.setString(2, emplo_cd);
				stmt.setString(3, emplo_cd);
				stmt.executeQuery();
				stmt.close();
				
				//Added By HM20231106 : For Generating Mail for user get Locked
				LockedUserMailBody(company_cd,utilBean.getEmpName(emplo_cd),emplo_cd,"Locked");
			}
			
			conn.commit();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void LoginCheck()
	{
		String function_nm="LoginCheck()";
		try
		{
			queryString="SELECT A.EMP_CD,A.EMP_UID,A.EMP_NM,A.EMAIL_ID,B.PASSWORD,A.COMPANY_CD,A.LOCK_STATUS,A.EMP_STATUS "
					+ "FROM FMS_EMP_MST A, FMS_EMP_PASSWD_MST B "
					+ "WHERE A.EMP_CD=B.EMP_CD AND A.EMP_UID=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, username);
			ResultSet rset=stmt.executeQuery();
			if(rset.next())
			{
				emp_cd = rset.getString(1)==null?"":rset.getString(1);
				emp_nm = rset.getString(3)==null?"":rset.getString(3);
				email = rset.getString(4)==null?"":rset.getString(4);
				String pwd = rset.getString(5)==null?"":rset.getString(5);
				//comp_cd = rset.getString(6)==null?"":rset.getString(6);
				String lock_status = rset.getString(7)==null?"":rset.getString(7);
				String emp_status = rset.getString(8)==null?"":rset.getString(8);
				
				if(emp_status.equals("Y")) 
				{
					if(!lock_status.equals("Y")) 
					{
						StringBuffer stringbuffer1 = (new EncryptTest()).encrypt(password);
						pwd = (new EncryptTest()).decrypt(pwd).toString();
						password = (new EncryptTest()).decrypt(stringbuffer1.toString()).toString();
						
						if(password.equals(pwd))
						{
							login = true;
							
							queryString2 = "UPDATE FMS_EMP_MST SET INVALID_LOGIN=?, VALID_LOGIN_ON=SYSDATE "
					        		+ "WHERE EMP_UID=? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, "0");
							stmt2.setString(2, username);
					        stmt2.executeUpdate();
					        stmt2.close();
					        
					        String isPassSysGen = "";
					        String isPassExp = "";
					        
					        String sysdt = utilDate.getSysdate();
					        
					        queryString3 = "SELECT SYS_PASSWD,TO_CHAR(LAST_CHANGE,'DD/MM/YYYY') "
					        		+ "FROM FMS_EMP_PASSWD_MST "
					        		+ "WHERE EMP_CD=? ";
					        stmt3 = conn.prepareStatement(queryString3);
					        stmt3.setString(1,emp_cd);
					        ResultSet rset3=stmt3.executeQuery();
							if(rset3.next()) 
							{
								isPassSysGen = rset3.getString(1)==null?"":rset3.getString(1);
								String last_change_dt = rset3.getString(2)==null?"":rset3.getString(2);
								
								passChangeDays = utilDate.getDays(sysdt,last_change_dt);
								
								String sysdate = utilDate.getSysdate();
								int pass_expDays = 0;
								
								String queryString="SELECT PWD_EXP_DAYS "
										+ "FROM FMS_ADMIN_POLICY "
										+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
										+ "ORDER BY EFF_DT DESC";
								stmt=conn.prepareStatement(queryString);
								stmt.setString(1, sysdate);
								ResultSet rset4=stmt.executeQuery();
								if(rset4.next())
								{
									pass_expDays = Integer.parseInt(rset4.getString(1)==null?"":rset4.getString(1));
								}
								rset4.close();
								stmt.close();
								
								if(passChangeDays>pass_expDays) 
								{
									isPassExp="Y";
								}
								
							}
							rset3.close();
					        stmt3.close();
					        
					        if(isPassSysGen.equals("Y") || isPassExp.equals("Y"))
					        {
					        	passChangeReq = true;
					        }
						}
					}
					else 
					{
						locked = true;
					}
				}
				else 
				{
					if(emp_status.equals("N")) 
					{
						disabled = true;
					}
					else if(emp_status.equals("D")) 
					{
						dormant = true;
					}
					else if(emp_status.equals("R")) 
					{
						removed = true;
					}
				}
			}
			else
			{
				incorrUserNm = true;
			}
			
			String sysdate = utilDate.getSysdate();
			
			queryString="SELECT PWD_EXP_DAYS, PWD_EXP_REMDR  "
					+ "FROM FMS_ADMIN_POLICY "
					+ "WHERE EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY EFF_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sysdate);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				exp_days = rset.getString(1)==null?"":rset.getString(1);
				rem_days = rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void EntityDetail()
	{
		String function_nm="EntityDetail()";
		try
		{
			queryString1="SELECT COMPANY_ABBR,COMPANY_NM,COMPANY_LOGO "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			ResultSet rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				comp_cd=company_cd;
				comp_abbr=rset1.getString(1)==null?"":rset1.getString(1);
				comp_nm=rset1.getString(2)==null?"":rset1.getString(2);
				comp_logo=rset1.getString(3)==null?"":rset1.getString(3);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void CompanyMaster()
	{
		utilBean.getEffectiveCompanyOwnerList();
		VCOMPANY_CD=utilBean.getCOUNTERPARTY_CD();
		VCOMPANY_NM=utilBean.getCOUNTERPARTY_NM();
		VCOMPANY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
	}

	private void LockedUserMailBody(String comp_cd,String user_nm,String user_cd,String lock_status) throws Exception
	{
		String function_nm="LockedUserMailBody()";
		String mailBody="";
		String env_context = utilBean.getAutomationKeyDetail("ENV_CONTEXT");
		try
		{
			String email_id="";
			
			query = "SELECT EMAIL_ID "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt1=conn.prepareStatement(query);
			stmt1.setString(1, user_cd);
			ResultSet rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				email_id = rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
			
			String subject=CommonVariable.app_name_sub+" "+env_context+": "+lock_status+"!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Hi <b>"+user_nm+"</b>,<br><br>"
					+ "Your "+CommonVariable.app_name+" account has been "+lock_status+",<br>"
					+ "Please connect system admin for further assistance.";
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_lock_user = email_id+","+utilBean.getToMailReceipentList("0","User Activity Notification","Admin","NA","On-Event");
			String cc_mail_lock_user = utilBean.getCcMailReceipentList("0","User Activity Notification","Admin","NA","On-Event");

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
	
	int count=0;
	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	
	String username = "";
	public void setUsername(String username) {this.username = username;}
	String password = "";
	public void setPassword(String password) {this.password = password;}
	String company_cd = "";
	public void setCompany_cd(String company_cd) {this.company_cd = company_cd;}
	String ip = "";
	public void setIp(String ip) {this.ip = ip;}
	String emplo_cd = "";
	public void setEmplo_cd(String emplo_cd) {this.emplo_cd = emplo_cd;}
	String emplo_nm = "";
	public void setEmplo_nm(String emplo_nm) {this.emplo_nm = emplo_nm;}
	
	boolean login = false;
	boolean locked = false;
	boolean disabled = false;
	boolean dormant = false;
	boolean removed = false;
	boolean passChangeReq = false;
	boolean incorrUserNm = false;
	
	public Boolean getLogin() {return login;}
	public Boolean getLocked() {return locked;}
	public Boolean getDisabled() {return disabled;}
	public Boolean getDormant() {return dormant;}
	public Boolean getRemoved() {return removed;}
	public Boolean getPassChangeReq() {return passChangeReq;}
	public Boolean getIncorrUserNm() {return incorrUserNm;}
	
	int passChangeDays = 0;
	public Integer getPassChangeDays() {return passChangeDays;}
	
	String emp_cd = "";
	String emp_nm = "";
	String comp_cd = "";
	String comp_abbr = "";
	String comp_nm = "";
	String email = "";
	String comp_logo = "";
	
	String exp_days = "";
	String rem_days = "";
	
	public String getEmp_nm() {return emp_nm;}
	public String getEmp_cd() {return emp_cd;}
	public String getComp_cd() {return comp_cd;}
	public String getComp_abbr() {return comp_abbr;}
	public String getComp_nm() {return comp_nm;}
	public String getEmail() {return email;}
	public String getComp_logo() {return comp_logo;}
	
	public String getExp_days() {return exp_days;}
	public String getRem_days() {return rem_days;}
	
	Vector VCOMPANY_CD = new Vector();
	Vector VCOMPANY_NM = new Vector();
	Vector VCOMPANY_ABBR = new Vector();
	
	Vector VLOGIN_ATTAMPT = new Vector();
	
	public Vector getVCOMPANY_CD() {return VCOMPANY_CD;}
	public Vector getVCOMPANY_NM() {return VCOMPANY_NM;}
	public Vector getVCOMPANY_ABBR() {return VCOMPANY_ABBR;}
	
	public Vector getVLOGIN_ATTAMPT() {return VLOGIN_ATTAMPT;}
}
