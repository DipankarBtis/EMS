package com.etrm.fms.mail_recipient_config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 27/01/2023 
//Status	  		: Developing
public class DataBean_Mail_Recipient_Config 
{
	String db_src_file_name="DataBean_Mail_Recipient_Config.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	PreparedStatement stmt_temp2;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset_temp;
	ResultSet rset_temp1;
	ResultSet rset_temp2;
	String query="";
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString_temp1="";
	String queryString_temp2="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
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
	    			if(callFlag.equalsIgnoreCase("MAIL_RECIPIENT_CONFIG"))
	    			{
	    				if(!comp_cd.equals(""))
	    				{
	    					getMasterDetail();
	    				}
	    				getEmailSupportDetail();
	    				getEmailScheduleDetail();
	    				getToEmailRecipintList();
	    				getCcEmailRecipintList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MAIL_RECIPIENT_DTL"))
	    			{
	    				getMasterDetail();
	    				getEmailDetails();
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
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp2 != null){try{rset_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getMasterDetail()
	{
		String function_nm="getMasterDetail()";
		try
		{
			queryString="SELECT DISTINCT(MODULE_NAME) "
					+ "FROM FMS_EMAIL_SUPPORT_MST ";
			if(comp_cd.equals("0"))
			{
				queryString+="WHERE SEI_CENTRAL='Y' ";
			}
			else
			{
				queryString+="WHERE SEI_CENTRAL IS NULL ";
			}
			queryString+= "ORDER BY MODULE_NAME";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMODULE_NM_MST.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			queryString="SELECT DISTINCT(MENU_NAME) "
					+ "FROM FMS_EMAIL_SUPPORT_MST "
					+ "WHERE MODULE_NAME=? ORDER BY MENU_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sel_module_nm);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VFORM_NM_MST.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEmailDetails()
	{
		String function_nm="getEmailDetails()";
		try
		{
			query="SELECT DISTINCT(MODULE_NAME) "
					+ "FROM FMS_EMAIL_SUPPORT_MST ";
			if(comp_cd.equals("0"))
			{
				query+="WHERE SEI_CENTRAL='Y' ";
				if(!sel_module_nm.equals("0")) 
				{
					query+= "AND UPPER(MODULE_NAME)=? ";
				}
			}
			else
			{
				query+="WHERE SEI_CENTRAL IS NULL  ";
				if(!sel_module_nm.equals("0")) 
				{
					query+= "AND UPPER(MODULE_NAME)=? ";
				}
			}
			query+=" ORDER BY MODULE_NAME";
			stmt_temp=conn.prepareStatement(query);
				
			if(!sel_module_nm.equals("0")) 
			{
				stmt_temp.setString(1, sel_module_nm.toUpperCase());
			}
			
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String module_nm = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				String menu_nm = "";
				
				VMODULE_NM.add(module_nm);
				
				int index=0;
				
				queryString = "SELECT MODULE_NAME,MENU_NAME,REPORT_FREQ,GENERATION_TYPE,SUPPORT_FLAG,SEQ_NO "
						+ "FROM FMS_EMAIL_SUPPORT_MST "
						+ "WHERE UPPER(MODULE_NAME)=? "
						+ "ORDER BY SEQ_NO";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, module_nm.toUpperCase());
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					String mod_nm = rset.getString(1)==null?"":rset.getString(1);
					menu_nm = rset.getString(2)==null?"":rset.getString(2);
					String rpt_freq = rset.getString(3)==null?"":rset.getString(3);
					String gen_ty = rset.getString(4)==null?"":rset.getString(4);
					
					VSUP_MODULE_NM.add(mod_nm);
					VSUP_MENU_NM.add(menu_nm);
					VSUP_RPT_FREQ.add(rpt_freq);
					VSUP_GEN_TYPE.add(gen_ty);
					
					String support_flag=rset.getString(5)==null?"N":rset.getString(5);
					VSUPPORT_FLAG.add(support_flag);
					if(support_flag.equals("N"))
					{
						VSUPPORT_FLAG_NM.add("Not Supported");
					}
					else
					{
						VSUPPORT_FLAG_NM.add("Supported");
					}
					
					VSUP_SEQ_NO.add(rset.getString(6)==null?"":rset.getString(6));
					
					queryString1="SELECT STOP_FLAG "
							+ "FROM FMS_EMAIL_RECIPIENT_MST "
							+ "WHERE COMPANY_CD=? AND UPPER(MODULE_NAME)=? "
							+ "AND UPPER(MENU_NAME)=? AND UPPER(REPORT_FREQ)=? "
							+ "AND UPPER(GENERATION_TYPE)=? "
							+ "ORDER BY SEQ_NO";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, mod_nm.toUpperCase());
					stmt1.setString(3, menu_nm.toUpperCase());
					stmt1.setString(4, rpt_freq.toUpperCase());
					stmt1.setString(5, gen_ty.toUpperCase());
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String status=rset1.getString(1)==null?"N":rset1.getString(1);
						VSTATUS.add(status);
						
						if(status.equals("N"))
						{
							VSTATUS_NM.add("Active");
							VSTATUS_COLOR.add("#a6ff4d");
						}
						else
						{
							VSTATUS_NM.add("In-Active");
							VSTATUS_COLOR.add("red");
						}
					}
					else
					{
						VSTATUS.add("N");
						VSTATUS_NM.add("Active");
						VSTATUS_COLOR.add("#a6ff4d");
					}
					rset1.close();
					stmt1.close();
					
					/* JD HP :: Fix for Multiple Freq Support not coming in list
					if(sel_type.equals(""))
					{
						if(VSUP_MODULE_NM.size()>0)
						{
							sel_seq_no=VSUP_SEQ_NO.elementAt(0).toString();
						}
					}*/
					
					sel_seq_no=rset.getString(6)==null?"":rset.getString(6); //JD HP
					
					String days_string="";
					
					queryString2 = "SELECT RECIPIENTS_CD,MODULE_NAME,MENU_NAME,REPORT_FREQ,GENERATION_TYPE,"
							+ "STOP_FLAG,MON,TUE,WED,THU,FRI,SAT,SUN,SEQ_NO "
							+ "FROM FMS_EMAIL_RECIPIENT_MST "
							+ "WHERE COMPANY_CD=? AND UPPER(MODULE_NAME)=? "
							+ "AND UPPER(MENU_NAME)=? AND SEQ_NO=? "
							+ "AND UPPER(REPORT_FREQ)=? AND UPPER(GENERATION_TYPE)=? "
							+ "ORDER BY SEQ_NO";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, module_nm.toUpperCase());
					stmt2.setString(3, menu_nm.toUpperCase());
					stmt2.setString(4, sel_seq_no);
					stmt2.setString(5, rpt_freq.toUpperCase());
					stmt2.setString(6, gen_ty.toUpperCase());
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{									
						RECIPIENT_CD = rset2.getString(1)==null?"":rset2.getString(1);
						MODULE_NM = rset2.getString(2)==null?"":rset2.getString(2);
						MENU_NM = rset2.getString(3)==null?"":rset2.getString(3);
						REPORT_FREQ = rset2.getString(4)==null?"":rset2.getString(4);
						GENERATION_TYPE = rset2.getString(5)==null?"":rset2.getString(5);
						STOP_FLAG = rset2.getString(6)==null?"N":rset2.getString(6);
						MON = rset2.getString(7)==null?"N":rset2.getString(7);
						TUE = rset2.getString(8)==null?"N":rset2.getString(8);
						WED = rset2.getString(9)==null?"N":rset2.getString(9);
						THU = rset2.getString(10)==null?"N":rset2.getString(10);
						FRI = rset2.getString(11)==null?"N":rset2.getString(11);
						SAT = rset2.getString(12)==null?"N":rset2.getString(12);
						SUN = rset2.getString(13)==null?"N":rset2.getString(13);
						SEQ_NO = rset2.getString(14)==null?"":rset2.getString(14);
						
						VGENERATION_TYPE.add(GENERATION_TYPE);
						String reciepent_cd = RECIPIENT_CD;
						
						days_string+="( ";
						
						if(!MON.equals("N")) 
						{
							days_string+="Mon.";
						}
						if(!TUE.equals("N")) 
						{
							days_string+="Tue.";
						}
						if(!WED.equals("N")) 
						{
							days_string+="Wed.";
						}
						if(!THU.equals("N")) 
						{
							days_string+="Thu.";
						}
						if(!FRI.equals("N")) 
						{
							days_string+="Fri.";
						}
						if(!SAT.equals("N")) 
						{
							days_string+="Sat.";
						}
						if(!SUN.equals("N")) 
						{
							days_string+="Sun.";
						}
						days_string+=" )";
						
						VFREQ_IN_DAYS.add(days_string);
						
						getToEmailReciepentUsersList(reciepent_cd);
						getCcEmailReciepentUsersList(reciepent_cd);
					}
					else 
					{
						VGENERATION_TYPE.add("");
						
						VTO_EMAIL.add("");
						VTO_USER_NM.add("");
						
						VCC_EMAIL.add("");
						VCC_USER_NM.add("");
						
						VFREQ_IN_DAYS.add("NA");
					}
					rset2.close();
					stmt2.close();
				}
				rset.close();
				stmt.close();
				VINDEX.add(index);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getToEmailReciepentUsersList(String reciepent_cd)
	{
		String function_nm="getToEmailReciepentUsersList()";
		try
		{
			String to_users_name = "";
			String to_users_email = "";
			
			queryString3="SELECT TO_EMAIL,SEQ_NO,TO_EMP_CD "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND TO_EMP_CD IS NOT NULL "
					+ "ORDER BY SEQ_NO";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, reciepent_cd);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String to_user_cd = rset3.getString(3)==null?"":rset3.getString(3);
				
				queryString4="SELECT EMP_STATUS,EMAIL_ID "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_STATUS=? AND EMP_CD = ?";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, "Y");
				stmt4.setString(2, to_user_cd);
				rset4=stmt4.executeQuery();
				while(rset4.next())
				{
					VTO_EMP_STATUS.add(rset4.getString(1)==null?"":rset4.getString(1));
					
					if(to_users_name.isEmpty()) 
					{
						to_users_name=utilBean.getEmpName(conn,(rset3.getString(3)==null?"":rset3.getString(3)));
					}
					else 
					{
						to_users_name+="<br>"+utilBean.getEmpName(conn,(rset3.getString(3)==null?"":rset3.getString(3)));
					}
					
//					String email =rset3.getString(1)==null?"":rset3.getString(1);
					String email =rset4.getString(2)==null?"":rset4.getString(2);
					
					if(to_users_email.isEmpty()) 
					{
						to_users_email=""+email+"";
					}
					else 
					{
//						to_users_email+="<br>"+""+(rset3.getString(1)==null?"":rset3.getString(1))+"";
						to_users_email+="<br>"+""+(email)+"";
					}

					VTO_SEQ_NO.add(rset3.getString(2)==null?"":rset3.getString(2));
					VTO_USER_CD.add(rset3.getString(3)==null?"":rset3.getString(3));
				}
				rset4.close();
				stmt4.close();
			}
			rset3.close();
			stmt3.close();
			
			
			queryString_temp2="SELECT TO_EMAIL,SEQ_NO "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND TO_EMP_CD=?";
			stmt_temp2=conn.prepareStatement(queryString_temp2);
			stmt_temp2.setString(1, reciepent_cd);
			stmt_temp2.setString(2, "0");
			rset_temp2=stmt_temp2.executeQuery();
			while(rset_temp2.next())
			{
				String extra_to_email =rset_temp2.getString(1)==null?"":rset_temp2.getString(1);
				to_users_email+="<br>"+extra_to_email+"";
			}
			rset_temp2.close();
			stmt_temp2.close();
			
			VTO_EMAIL.add(to_users_email);
			VTO_USER_NM.add(to_users_name);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCcEmailReciepentUsersList(String reciepent_cd)
	{
		String function_nm="getCcEmailReciepentUsersList()";
		try
		{
			String cc_users_name = "";
			String cc_users_email = "";
			
			queryString5="SELECT CC_EMAIL,SEQ_NO,CC_EMP_CD "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND CC_EMP_CD IS NOT NULL "
					+ "ORDER BY SEQ_NO";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, reciepent_cd);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String cc_user_cd = rset5.getString(3)==null?"":rset5.getString(3);
				
				queryString6="SELECT EMP_STATUS,EMAIL_ID "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_STATUS=? AND EMP_CD = ?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, "Y");
				stmt6.setString(2, cc_user_cd);
				rset6=stmt6.executeQuery();
				while(rset6.next())
				{
					VCC_EMP_STATUS.add(rset6.getString(1)==null?"":rset6.getString(1));
					
					if(cc_users_name.isEmpty()) 
					{
						cc_users_name=utilBean.getEmpName(conn,(rset5.getString(3)==null?"":rset5.getString(3)));
					}
					else 
					{
						cc_users_name+="<br>"+utilBean.getEmpName(conn,(rset5.getString(3)==null?"":rset5.getString(3)));
					}
					
//					String email =rset5.getString(1)==null?"":rset5.getString(1);
					String email =rset6.getString(2)==null?"":rset6.getString(2);
					
					if(cc_users_email.isEmpty()) 
					{
						cc_users_email = ""+email+"";
					}
					else 
					{
						cc_users_email+="<br>"+""+(email)+"";
					}
					
					VCC_SEQ_NO.add(rset5.getString(2)==null?"":rset5.getString(2));
					VCC_USER_CD.add(rset5.getString(3)==null?"":rset5.getString(3));
				}
				rset6.close();
				stmt6.close();
			}
			rset5.close();
			stmt5.close();
			
			queryString_temp1="SELECT CC_EMAIL,SEQ_NO "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND CC_EMP_CD=?";
			stmt_temp1=conn.prepareStatement(queryString_temp1);
			stmt_temp1.setString(1, reciepent_cd);
			stmt_temp1.setString(2, "0");
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String extra_cc_email=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				cc_users_email+="<br>"+extra_cc_email+"";
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			VCC_EMAIL.add(cc_users_email);
			VCC_USER_NM.add(cc_users_name);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEmailSupportDetail()
	{
		String function_nm="getEmailSupportDetail()";
		try
		{
			queryString = "SELECT MODULE_NAME,MENU_NAME,REPORT_FREQ,GENERATION_TYPE,SUPPORT_FLAG,SEQ_NO "
					+ "FROM FMS_EMAIL_SUPPORT_MST "
					+ "WHERE UPPER(MODULE_NAME)=? AND UPPER(MENU_NAME)=? "
					+ "ORDER BY SEQ_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, sel_module_nm.toUpperCase());
			stmt.setString(2, sel_form_nm.toUpperCase());
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String mod_nm = rset.getString(1)==null?"":rset.getString(1);
				String menu_nm = rset.getString(2)==null?"":rset.getString(2);
				String rpt_freq = rset.getString(3)==null?"":rset.getString(3);
				String gen_ty = rset.getString(4)==null?"":rset.getString(4);
				
				VSUP_MODULE_NM.add(mod_nm);
				VSUP_MENU_NM.add(menu_nm);
				VSUP_RPT_FREQ.add(rpt_freq);
				VSUP_GEN_TYPE.add(gen_ty);
				
				String support_flag=rset.getString(5)==null?"N":rset.getString(5);
				VSUPPORT_FLAG.add(support_flag);
				if(support_flag.equals("N"))
				{
					VSUPPORT_FLAG_NM.add("Not Supported");
				}
				else
				{
					VSUPPORT_FLAG_NM.add("Supported");
				}
				
				VSUP_SEQ_NO.add(rset.getString(6)==null?"":rset.getString(6));
				
				queryString1="SELECT STOP_FLAG "
						+ "FROM FMS_EMAIL_RECIPIENT_MST "
						+ "WHERE COMPANY_CD=? AND UPPER(MODULE_NAME)=? "
						+ "AND UPPER(MENU_NAME)=? AND UPPER(REPORT_FREQ)=? "
						+ "AND UPPER(GENERATION_TYPE)=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, mod_nm.toUpperCase());
				stmt1.setString(3, menu_nm.toUpperCase());
				stmt1.setString(4, rpt_freq.toUpperCase());
				stmt1.setString(5, gen_ty.toUpperCase());
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					String status=rset1.getString(1)==null?"N":rset1.getString(1);
					VSTATUS.add(status);
					
					if(status.equals("N"))
					{
						VSTATUS_NM.add("Active");
						VSTATUS_COLOR.add("#a6ff4d");
					}
					else
					{
						VSTATUS_NM.add("In-Active");
						VSTATUS_COLOR.add("red");
					}
				}
				else
				{
					VSTATUS.add("N");
					VSTATUS_NM.add("Active");
					VSTATUS_COLOR.add("#a6ff4d");
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEmailScheduleDetail()
	{
		String function_nm="getEmailScheduleDetail()";
		try
		{
			if(sel_type.equals(""))
			{
				if(VSUP_MODULE_NM.size()>0)
				{
					sel_type=VSUP_GEN_TYPE.elementAt(0).toString();
					sel_freq=VSUP_RPT_FREQ.elementAt(0).toString();
					sel_seq_no=VSUP_SEQ_NO.elementAt(0).toString();
				}
			}
			
			queryString = "SELECT RECIPIENTS_CD,MODULE_NAME,MENU_NAME,REPORT_FREQ,GENERATION_TYPE,"
					+ "STOP_FLAG,MON,TUE,WED,THU,FRI,SAT,SUN,SEQ_NO "
					+ "FROM FMS_EMAIL_RECIPIENT_MST "
					+ "WHERE COMPANY_CD=? AND UPPER(MODULE_NAME)=? "
					+ "AND UPPER(MENU_NAME)=? AND SEQ_NO=? "
					+ "AND UPPER(REPORT_FREQ)=? AND UPPER(GENERATION_TYPE)=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, sel_module_nm.toUpperCase());
			stmt.setString(3, sel_form_nm.toUpperCase());
			stmt.setString(4, sel_seq_no);
			stmt.setString(5, sel_freq.toUpperCase());
			stmt.setString(6, sel_type.toUpperCase());
			rset=stmt.executeQuery();
			if(rset.next())
			{
				RECIPIENT_CD = rset.getString(1)==null?"":rset.getString(1);
				MODULE_NM = rset.getString(2)==null?"":rset.getString(2);
				MENU_NM = rset.getString(3)==null?"":rset.getString(3);
				REPORT_FREQ = rset.getString(4)==null?"":rset.getString(4);
				GENERATION_TYPE = rset.getString(5)==null?"":rset.getString(5);
				STOP_FLAG = rset.getString(6)==null?"N":rset.getString(6);
				MON = rset.getString(7)==null?"N":rset.getString(7);
				TUE = rset.getString(8)==null?"N":rset.getString(8);
				WED = rset.getString(9)==null?"N":rset.getString(9);
				THU = rset.getString(10)==null?"N":rset.getString(10);
				FRI = rset.getString(11)==null?"N":rset.getString(11);
				SAT = rset.getString(12)==null?"N":rset.getString(12);
				SUN = rset.getString(13)==null?"N":rset.getString(13);
				SEQ_NO = rset.getString(14)==null?"":rset.getString(14);
			}
			else
			{
				REPORT_FREQ= sel_freq;
				GENERATION_TYPE = sel_type;
				SEQ_NO=sel_seq_no;
				STOP_FLAG="N";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getToEmailRecipintList()
	{
		String function_nm="getToEmailRecipintList()";
		try
		{
			queryString="SELECT EMP_CD,EMP_NM,EMAIL_ID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=? "
					+ "AND EMP_CD IN (SELECT DISTINCT(TO_EMP_CD) FROM FMS_EMAIL_RECIPIENT_DTL B "
					+ "WHERE B.RECIPIENTS_CD=? AND B.ENABLE_DISABLE=? AND B.TO_EMP_CD IS NOT NULL AND TO_EMP_CD != 0)"
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, RECIPIENT_CD);
			stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEMP_EMAIL.add(rset.getString(3)==null?"":rset.getString(3));
				VEMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VEMP_STATUS_NM.add("Enabled");
				VEMP_EXIST.add("Y");
			}
			stmt.close();
			rset.close();
			
			queryString="SELECT EMP_CD,EMP_NM,EMAIL_ID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=? "
					+ "AND EMP_CD NOT IN (SELECT DISTINCT(TO_EMP_CD) FROM FMS_EMAIL_RECIPIENT_DTL B "
					+ "WHERE B.RECIPIENTS_CD=? AND B.ENABLE_DISABLE=? AND B.TO_EMP_CD IS NOT NULL AND TO_EMP_CD != 0)"
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, RECIPIENT_CD);
			stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEMP_EMAIL.add(rset.getString(3)==null?"":rset.getString(3));
				VEMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VEMP_STATUS_NM.add("Enabled");
				VEMP_EXIST.add("N");
			}
			stmt.close();
			rset.close();
			
			queryString="SELECT TO_EMAIL,SEQ_NO "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND TO_EMP_CD=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, RECIPIENT_CD);
			stmt.setString(2, "0");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VTO_EMAIL.add(rset.getString(1)==null?"":rset.getString(1));
				VTO_SEQ_NO.add(rset.getString(2)==null?"":rset.getString(2));
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCcEmailRecipintList()
	{
		String function_nm="getCcEmailRecipintList()";
		try
		{
			queryString="SELECT EMP_CD,EMP_NM,EMAIL_ID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=? "
					+ "AND EMP_CD IN (SELECT DISTINCT(CC_EMP_CD) FROM FMS_EMAIL_RECIPIENT_DTL B "
					+ "WHERE B.RECIPIENTS_CD=? AND B.ENABLE_DISABLE=? AND B.CC_EMP_CD IS NOT NULL AND CC_EMP_CD != 0) "
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, RECIPIENT_CD);
			stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCC_EMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCC_EMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCC_EMP_EMAIL.add(rset.getString(3)==null?"":rset.getString(3));
				VCC_EMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VCC_EMP_STATUS_NM.add("Enabled");
				VCC_EMP_EXIST.add("Y");
			}
			stmt.close();
			rset.close();
			
			queryString="SELECT EMP_CD,EMP_NM,EMAIL_ID,EMP_STATUS "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_STATUS=? "
					+ "AND EMP_CD NOT IN (SELECT DISTINCT(CC_EMP_CD) FROM FMS_EMAIL_RECIPIENT_DTL B "
					+ "WHERE B.RECIPIENTS_CD=? AND B.ENABLE_DISABLE=? AND B.CC_EMP_CD IS NOT NULL AND CC_EMP_CD != 0)"
					+ "ORDER BY EMP_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, RECIPIENT_CD);
			stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCC_EMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCC_EMP_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCC_EMP_EMAIL.add(rset.getString(3)==null?"":rset.getString(3));
				VCC_EMP_STATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VCC_EMP_STATUS_NM.add("Enabled");
				VCC_EMP_EXIST.add("N");
			}
			stmt.close();
			rset.close();
			
			queryString="SELECT CC_EMAIL,SEQ_NO "
					+ "FROM FMS_EMAIL_RECIPIENT_DTL "
					+ "WHERE RECIPIENTS_CD=? AND CC_EMP_CD=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, RECIPIENT_CD);
			stmt.setString(2, "0");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCC_EMAIL.add(rset.getString(1)==null?"":rset.getString(1));
				VCC_SEQ_NO.add(rset.getString(2)==null?"":rset.getString(2));
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String sel_module_nm="";
	String sel_form_nm="";
	String sel_freq="";
	String sel_type="";
	String sel_seq_no="";
	
	public void setSel_module_nm(String sel_module_nm) {this.sel_module_nm = sel_module_nm;}
	public void setSel_form_nm(String sel_form_nm) {this.sel_form_nm = sel_form_nm;}
	public void setSel_freq(String sel_freq) {this.sel_freq = sel_freq;}
	public void setSel_type(String sel_type) {this.sel_type = sel_type;}
	public void setSel_seq_no(String sel_seq_no) {this.sel_seq_no = sel_seq_no;}
	
	Vector VMODULE_NM_MST = new Vector();
	Vector VFORM_NM_MST = new Vector();
	
	Vector VMODULE_NM = new Vector();
	
	Vector VSUP_MENU_NM = new Vector();
	Vector VSUP_MODULE_NM = new Vector();
	Vector VSUP_RPT_FREQ = new Vector();
	Vector VSUP_GEN_TYPE = new Vector();
	Vector VSUPPORT_FLAG = new Vector();
	Vector VSUPPORT_FLAG_NM = new Vector();
	Vector VSUP_SEQ_NO = new Vector();
	Vector VSTATUS = new Vector();
	Vector VSTATUS_NM = new Vector();
	Vector VSTATUS_COLOR = new Vector();
	Vector VINDEX = new Vector();
	Vector VGENERATION_TYPE = new Vector();
	Vector VFREQ_IN_DAYS = new Vector();
	
	Vector VEMP_CD = new Vector();
	Vector VEMP_NM = new Vector();
	Vector VEMP_EMAIL = new Vector();
	Vector VEMP_STATUS = new Vector();
	Vector VEMP_STATUS_NM = new Vector();
	Vector VEMP_EXIST= new Vector();
	
	Vector VTO_EMAIL = new Vector();
	Vector VTO_SEQ_NO = new Vector();
	Vector VTO_EMP_STATUS = new Vector();
	
	Vector VCC_EMP_CD = new Vector();
	Vector VCC_EMP_NM = new Vector();
	Vector VCC_EMP_EMAIL = new Vector();
	Vector VCC_EMP_STATUS = new Vector();
	Vector VCC_EMP_STATUS_NM = new Vector();
	Vector VCC_EMP_EXIST= new Vector();
	
	Vector VCC_EMAIL = new Vector();
	Vector VCC_SEQ_NO = new Vector();
	
	Vector VCC_USER_CD = new Vector();
	Vector VTO_USER_CD	= new Vector();
	Vector VCC_USER_NM = new Vector();
	Vector VTO_USER_NM	= new Vector();
	
	public Vector getVCC_USER_CD() {return VCC_USER_CD;}
	public Vector getVTO_USER_CD() {return VTO_USER_CD;}
	public Vector getVCC_USER_NM() {return VCC_USER_NM;}
	public Vector getVTO_USER_NM() {return VTO_USER_NM;}
	
	public Vector getVMODULE_NM_MST() {return VMODULE_NM_MST;}
	public Vector getVFORM_NM_MST() {return VFORM_NM_MST;}
	public Vector getVMODULE_NM() {return VMODULE_NM;}
	public Vector getVSUP_MENU_NM(){return VSUP_MENU_NM;}
	public Vector getVSUP_MODULE_NM(){return VSUP_MODULE_NM;}
	public Vector getVSUP_RPT_FREQ(){return VSUP_RPT_FREQ;}
	public Vector getVSUP_GEN_TYPE(){return VSUP_GEN_TYPE;}
	public Vector getVSUPPORT_FLAG(){return VSUPPORT_FLAG;}
	public Vector getVSUPPORT_FLAG_NM(){return VSUPPORT_FLAG_NM;}
	public Vector getVSUP_SEQ_NO(){return VSUP_SEQ_NO;}
	public Vector getVSTATUS(){return VSTATUS;}
	public Vector getVSTATUS_NM(){return VSTATUS_NM;}
	public Vector getVSTATUS_COLOR(){return VSTATUS_COLOR;}
	public Vector getVINDEX(){return VINDEX;}
	public Vector getVGENERATION_TYPE(){return VGENERATION_TYPE;}
	public Vector getVFREQ_IN_DAYS(){return VFREQ_IN_DAYS;}
	
	public Vector getVEMP_CD(){return VEMP_CD;}
	public Vector getVEMP_NM(){return VEMP_NM;}
	public Vector getVEMP_EMAIL(){return VEMP_EMAIL;}
	public Vector getVEMP_STATUS(){return VEMP_STATUS;}
	public Vector getVEMP_STATUS_NM(){return VEMP_STATUS_NM;}
	public Vector getVEMP_EXIST(){return VEMP_EXIST;}
	
	public Vector getVTO_EMAIL(){return VTO_EMAIL;}
	public Vector getVTO_SEQ_NO(){return VTO_SEQ_NO;}
	
	public Vector getVTO_EMP_STATUS(){return VTO_EMP_STATUS;}
	
	public Vector getVCC_EMP_CD(){return VCC_EMP_CD;}
	public Vector getVCC_EMP_NM(){return VCC_EMP_NM;}
	public Vector getVCC_EMP_EMAIL(){return VCC_EMP_EMAIL;}
	public Vector getVCC_EMP_STATUS(){return VCC_EMP_STATUS;}
	public Vector getVCC_EMP_STATUS_NM(){return VCC_EMP_STATUS_NM;}
	public Vector getVCC_EMP_EXIST(){return VCC_EMP_EXIST;}
	
	public Vector getVCC_EMAIL(){return VCC_EMAIL;}
	public Vector getVCC_SEQ_NO(){return VCC_SEQ_NO;}
	
	String RECIPIENT_CD = "";
	String MODULE_NM = "";
	String MENU_NM = "";
	String REPORT_FREQ = "";
	String GENERATION_TYPE = "";
	String STOP_FLAG = "";
	String MON = "";
	String TUE = "";
	String WED = "";
	String THU = "";
	String FRI = "";
	String SAT = "";
	String SUN = "";
	String SEQ_NO = "";
	
	public String getRECIPIENT_CD() {return RECIPIENT_CD;}
	public String getMODULE_NM() {return MODULE_NM;}
	public String getMENU_NM() {return MENU_NM;}
	public String getREPORT_FREQ() {return REPORT_FREQ;}
	public String getGENERATION_TYPE() {return GENERATION_TYPE;}
	public String getSTOP_FLAG() {return STOP_FLAG;}
	public String getMON() {return MON;}
	public String getTUE() {return TUE;}
	public String getWED() {return WED;}
	public String getTHU() {return THU;}
	public String getFRI() {return FRI;}
	public String getSAT() {return SAT;}
	public String getSUN() {return SUN;}
	public String getSEQ_NO() {return SEQ_NO;}
}
