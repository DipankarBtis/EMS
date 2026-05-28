package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;

public class Auto_IncidentClose 
{
	public static void main(String[] args) 
	{
		Auto_Incident_Close close = new Auto_Incident_Close();
		close.init();
	}
}

class Auto_Incident_Close 
{
	//public static String mail_font_size="x-small";
	//public static String mail_font_family="Calibri";
	
	String db_src_file_name="Auto_IncidentClose.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt13,stmt14,stmt15,stmt16,stmt_tmp,stmt_tmp1,stmt_tmp2,stmt_tmp3,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset5,rset6,rset13,rset14,rset15,rset16,rset_tmp,rset_tmp1,rset_tmp2,rset_tmp3,rset_temp,rset_temp1;
	String queryString="",queryString1="",queryString2="",queryString3="";
	String queryString4="",queryString5="",queryString6="",queryString7="";
	String queryString8="",queryString9="",queryString10="",queryString11="";
	String queryString12="",queryString13="",queryString14="",queryString15="",queryString16="";
	String queryString_temp="";
	String queryString_temp1="";
	String queryString_temp2="";

	HttpServletRequest request;
	HttpServletRequest session;

	String context ="";
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");

	Auto_UtilBean utilBean = new Auto_UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mailDelv = new Auto_MailDelivery();

	public void init()
	{
		String function_nm="init()";
		try
		{
			conn=new Auto_DB_Connection().db_conn();

			if(conn != null)  
			{
				comp_cd= "1"; //NOTE : Need To Handle This For Multiple Company Login Cases;
				emp_cd=""; 
				comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
				
				today_date = utilDate.getSysdate();
				
				to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
				String temp_cc_mail = utilBean.getCcMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
				
				if(to_mail.equals(""))
				{
					to_mail=temp_cc_mail;
				}
				if(!to_mail.equals(""))
                {
					getAutocloseIncidentDtls();
					System.out.println("Mail for Auto Close Incident List sent Successfully!!");
                }
			}
			conn = null;
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		finally
		{
			try
			{
				if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try{rset5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset6 != null){try{rset6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset13 != null){try{rset13.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset14 != null){try{rset14.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset15 != null){try{rset15.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset16 != null){try{rset16.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4!= null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt6!= null){try{stmt6.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt13!= null){try{stmt13.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt14!= null){try{stmt14.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt15!= null){try{stmt15.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt16!= null){try{stmt16.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp!= null){try{stmt_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp1!= null){try{stmt_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp2!= null){try{stmt_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_tmp3!= null){try{stmt_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_temp!= null){try{stmt_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_temp1!= null){try{stmt_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}
	
	private void getAutocloseIncidentDtls() 
	{
		String function_nm="getAutocloseIncidentDtls()";
		try
		{
			String incident_mail_dtls1="<span><table style=\"border:1px solid black;border-collapse:collapse\";"
    				+ " border=\"1\"><thead style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\"><tr><th>Incident Id</th><th>Priority</th><th>Incident Type</th>"
    				+ "<th>Incident Title</th><th>Status</th><th>Initiated By</th><th>Assigned To</th><th>Days Remaining</th></tr></thead>"
    				+ "<tbody style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			
			String incident_mail_dtls="<span><table style=\"border:1px solid black;border-collapse:collapse\";"
    				+ " border=\"1\"><thead style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\"><tr><th>Incident Id</th><th>Priority</th><th>Incident Type</th>"
    				+ "<th>Incident Title</th><th>Status</th><th>Initiated By</th><th>Assigned To</th></tr></thead>"
    				+ "<tbody style=\"font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";\">";
			
			
			int close_count=0;
			int live_wait_days=0;
			int await_cust_count=0;
			int cust_wait_days=0;
			queryString="SELECT INCIDENT_ID,INCIDENT_TYPE,INCIDENT_TITLE,PRIORITY,ASSIGN_TO,STATUS,ENT_BY,TO_CHAR(LIVE_DT,'DD/MM/YYYY'),INCIDENT_DTL,ROOT_CAUSE "
					+ "FROM FMS_INCIDENT_MST "
					+ "WHERE STATUS IN ('LIVE','Awaiting Customer')";//COMPANY_CD=? AND 
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String incident_id = rset.getString(1)==null?"":rset.getString(1);
				String incident_type = rset.getString(2)==null?"":rset.getString(2);
				String incident_title = rset.getString(3)==null?"":rset.getString(3);
				String priority = rset.getString(4)==null?"":rset.getString(4);
				String assign_to = rset.getString(5)==null?"":rset.getString(5);
				String assign_to_nm = utilBean.getEmpName(conn, assign_to);
				String status = rset.getString(6)==null?"":rset.getString(6);
				String initiated_by = rset.getString(7)==null?"":rset.getString(7);
				String initiated_by_nm = utilBean.getEmpName(conn, initiated_by);
				String live_dt = rset.getString(8)==null?"":rset.getString(8);
				String incident_dtls = rset.getString(9)==null?"":rset.getString(9);
				String root_cause = rset.getString(10)==null?"":rset.getString(10);

				if(status.equals("LIVE"))
				{					
					queryString2="SELECT KEY_VALUE "
							+ "FROM FMS_AUTOMATION_KEY "
							+ "WHERE KEY_NM=? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, "LIVE_WAIT_DAYS");
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						live_wait_days=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					int days = utilDate.getDays(today_date, live_dt);
					
					if(days>live_wait_days && live_wait_days > 0) // Auto close LIVE incidents after 10 days
					{
						incident_dtls="Auto close LIVE incident Triggred after >"+live_wait_days+" days Go LIVE";
						close_count++;
						insert_update_incident_dtl(incident_id,incident_dtls,assign_to,status,root_cause);
						incident_mail_dtls += "<tr><td align=\"center\">"+incident_id+"</td><td align=\"center\">"+priority+"</td><td align=\"center\">"+incident_type+"</td>"
								+ "<td align=\"center\">"+incident_title+"</td><td align=\"center\">"+status+"</td><td align=\"center\">"+initiated_by_nm+"</td><td align=\"center\">"+assign_to_nm+"</td></tr>";
					}
				}
				else if(status.equals("Awaiting Customer"))
				{					
					queryString2="SELECT KEY_VALUE "
							+ "FROM FMS_AUTOMATION_KEY "
							+ "WHERE KEY_NM=? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, "CUST_WAIT_DAYS");
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						cust_wait_days=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();

					queryString1="SELECT MODIFY_DT "
							+ "FROM FMS_INCIDENT_MST "
							+ "WHERE STATUS=? AND INCIDENT_ID=? ";//COMPANY_CD=? AND 
					stmt1 = conn.prepareStatement(queryString1);
					//stmt1.setString(1, comp_cd);
					stmt1.setString(1, "Awaiting Customer");
					stmt1.setString(2, incident_id);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String temp_await_cust_dt = rset1.getString(1)==null?"":rset1.getString(1);
						String await_cust_dt[] = temp_await_cust_dt.split(" ");
						String await_cust_dt1 = await_cust_dt[0] ;
						
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
   			          
 			            Date date = inputFormat.parse(await_cust_dt1);
 			            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
 			            String formated_await_dt = outputFormat.format(date);
						
 			            int remaining_days = cust_wait_days-4;
						int await_cust_days = utilDate.getDays(today_date, formated_await_dt);
						
						if(await_cust_days>cust_wait_days && cust_wait_days > 0)
						{
							incident_dtls="Auto close LIVE incident Triggred after >"+cust_wait_days+" awating days";
							close_count++;
							insert_update_incident_dtl(incident_id,incident_dtls,assign_to,status,root_cause);
							incident_mail_dtls += "<tr><td align=\"center\">"+incident_id+"</td><td align=\"center\">"+priority+"</td><td align=\"center\">"+incident_type+"</td>"
									+ "<td align=\"center\">"+incident_title+"</td><td align=\"center\">"+status+"</td><td align=\"center\">"+initiated_by_nm+"</td><td align=\"center\">"+assign_to_nm+"</td></tr>";
						}
						
						if(await_cust_days>remaining_days && await_cust_days<=cust_wait_days && cust_wait_days > 0)
						{
							await_cust_count++;
							int days_remaining = cust_wait_days-await_cust_days+1;											

							incident_mail_dtls1 += "<tr><td align=\"center\">"+incident_id+"</td><td align=\"center\">"+priority+"</td><td align=\"center\">"+incident_type+"</td>"
									+ "<td align=\"center\">"+incident_title+"</td><td align=\"center\">"+status+"</td><td align=\"center\">"+initiated_by_nm+"</td><td align=\"center\">"+assign_to_nm+"</td><td align=\"center\">"+days_remaining+"</td></tr>";
						}
					}
					rset1.close();
					stmt1.close();
				}
			}
			rset.close();
			stmt.close();
			
			incident_mail_dtls1 +="</tbody></table></span>";
			incident_mail_dtls +="</tbody></table></span>";
			
			if(close_count>0)
			{
				sendAutoCloseMail(comp_cd,comp_abbr,incident_mail_dtls,today_date);
			}
			else if(await_cust_count>0)
			{
				sendAlertMail(comp_cd,comp_abbr,incident_mail_dtls1,today_date, cust_wait_days);
			}
			
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	private void insert_update_incident_dtl(String incident_id,String incident_dtls,String assign_to, String status , String root_cause)
	{
		String function_nm="insert_update_incident_dtl()";
		try 
		{
			queryString1 = "UPDATE FMS_INCIDENT_MST SET STATUS=?,MODIFY_BY=?,MODIFY_DT=TO_DATE(?,'DD/MM/YYYY'),INCIDENT_DTL=?,MOD_PROFILE=? "
					+ "WHERE INCIDENT_ID=? ";//COMPANY_CD=? AND 
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, "Close");
			stmt1.setString(2, "0");
			stmt1.setString(3, today_date);
			stmt1.setString(4, incident_dtls);
			stmt1.setString(5, "0");
			stmt1.setString(6, incident_id);
			stmt1.executeUpdate();
			stmt1.close();
			
			
			String incident_dtl_seq_no="1";
			
			queryString3="SELECT MAX(SEQ_NO)"
					+ "FROM FMS_INCIDENT_DTL "
					+ "WHERE INCIDENT_ID=?";//COMPANY_CD=? AND 
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			stmt3.setString(1, incident_id);
			rset3 = stmt3.executeQuery();
			if(rset3.next())
			{
				incident_dtl_seq_no=""+(rset3.getInt(1)+1);
			}
			rset3.close();
			stmt3.close();		

			queryString2="INSERT INTO FMS_INCIDENT_DTL(INCIDENT_ID,SEQ_NO,INCIDENT_DTL,"
					+ "ASSIGN_TO,STATUS,ENT_BY,ENT_DT,ROOT_CAUSE,ENT_PROFILE) "
					+ "VALUES(?,?,?,"
					+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?)";
			stmt2 = conn.prepareStatement(queryString2);
			//stmt2.setString(1, comp_cd);
			stmt2.setString(1, incident_id);
			stmt2.setString(2, incident_dtl_seq_no);
			stmt2.setString(3, incident_dtls);
			stmt2.setString(4, assign_to);
			stmt2.setString(5, "Close");
			stmt2.setString(6, "0");
			stmt2.setString(7, today_date);
			stmt2.setString(8, root_cause);
			stmt2.setString(9, "0");
			stmt2.executeUpdate();
			
			stmt2.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void sendAutoCloseMail(String comp_cd, String comp_nm , String incident_mail_dtls , String today_dt)
	{
		String function_nm="sendAutoCloseMail()";
		String mailBody = "";
		try
		{
			context=utilBean.getAutomationKeyDetail(conn,"ENV_CONTEXT");
			
			String subject=CommonVariable.app_name+" "+context+": Incidents Auto Closed By the System On "+today_dt+"";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Following Incidents are Closed on "+today_dt+" By the System:<br><br>"
					+ ""+incident_mail_dtls+"<br><br>";

			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_preDef = utilBean.getToMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
			String cc_mail_preDef = utilBean.getCcMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
			
			if(to_mail_preDef.equals("")) 
			{
				to_mail_preDef = cc_mail_preDef;
				cc_mail_preDef = "";
			}	
			if(!to_mail_preDef.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_preDef, subject, mailBody, "", cc_mail_preDef, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void sendAlertMail(String comp_cd,String comp_nm , String incident_mail_dtls1 , String today_dt, int cust_wait_days)
	{
		String function_nm="sendAlertMail()";
		String mailBody = "";
		try
		{
			context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
			
			String subject=CommonVariable.app_name+" "+context+": Incidents Auto Closure Alert!";
			mailBody="<html><span style= font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"
					+ "Following Incidents approaching >"+cust_wait_days+" days Awaiting time and will be Closed automatically.<br>Please update to avoid same.<br><br>"
					+ ""+incident_mail_dtls1+"<br><br>";
			
			mailBody+=CommonVariable.mail_signature;
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			String to_mail_preDef = utilBean.getToMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
			String cc_mail_preDef = utilBean.getCcMailReceipentList(conn,comp_cd,"Incident Notification","Helpdesk","NA","On-Event");
			
			if(to_mail_preDef.equals("")) 
			{
				to_mail_preDef = cc_mail_preDef;
				cc_mail_preDef = "";
			}	
			if(!to_mail_preDef.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_preDef, subject, mailBody, "", cc_mail_preDef, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	String comp_cd="";
	String emp_cd="";
	String today_date = "";
	String comp_abbr = "";
	String to_mail="";
	
}