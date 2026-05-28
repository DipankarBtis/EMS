package automation;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Auto_Mark_Domant_User 
{
	public static void main(String[] args) 
	{
		Auto_Dormant_User  adu = new Auto_Dormant_User();
		adu.init();
	}
}

class Auto_Dormant_User
{
	String db_src_file_name="Auto_Mark_Domant_User.java";

	public static String mail_font_size="x-small";
	public static String mail_font_family="Calibri";
	
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt_tmp,stmt_tmp1,stmt_tmp2,stmt_tmp3,stmt_tmp4,stmt_tmp5,stmt_prep;
    ResultSet rset,rset1,rset2,rset3,rset4,rset5,rset_tmp,rset_tmp1,rset_tmp2,rset_tmp3,rset_tmp4,rset_tmp5;
	String query="",query1="",queryString="",queryString1="",queryString2="",queryString3="";
	String queryString4="",queryString5="";
	String queryString_temp="",queryString_temp1="",queryString_temp2="",queryString_temp3="",queryString_temp4="";
    
    HttpServletRequest request;
    HttpServletRequest session;
   
    String context ="";
    
    NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	Auto_UtilBean utilBean = new Auto_UtilBean();
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
	             
	            to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Dormant Notification","Admin","NA","On-Event");
                
                if(!to_mail.equals(""))
                {
                	getDormant_User_Dtl();
                	System.out.println("Mail for Dormant User List sent Successfull!!");
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
    	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp4 != null){try{rset_tmp4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(rset_tmp5 != null){try{rset_tmp5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt4!= null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp!= null){try{stmt_tmp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp1!= null){try{stmt_tmp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp2!= null){try{stmt_tmp2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp3!= null){try{stmt_tmp3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp4!= null){try{stmt_tmp4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_tmp5!= null){try{stmt_tmp5.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
    	    	if(stmt_prep!= null){try{stmt_prep.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}    	    	
    	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
        	}
        	catch(Exception e)
        	{
        		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        	}
        }
	}
    
    public void getDormant_User_Dtl() 
    {
    	String function_nm="getDormant_User_Dtl()";
    	Vector emails = new Vector();
    	Vector uids = new Vector();
    	Vector V_days = new Vector();
  
    	try
		{
			String ip = ""+InetAddress.getLocalHost().getHostAddress();
    		String sysdate="";
    		queryString1 = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') FROM DUAL";
            stmt1 = conn.prepareStatement(queryString1);
            rset1 = stmt1.executeQuery();
            if(rset1.next())
            {
            	sysdate = rset1.getString(1)==null?"":rset1.getString(1);  
            }
    		
    		String emp_nm_dtl="<span><table style=\"border:1px solid black;border-collapse:collapse\";"
    				+ " border=\"1\"><thead style=\"font-size:"+mail_font_size+";font-family:"+mail_font_family+";\"><tr><th>User Name</th><th>User Id</th><th>Last Login On</th></tr></thead>"
    				+ "<tbody style=\"font-size:"+mail_font_size+";font-family:"+mail_font_family+";\">";
    		
    		int dormnt_user_count=0;
    		
    		queryString="SELECT TO_CHAR(VALID_LOGIN_ON,'DD/MM/YYYY'), EMP_CD,EMP_NM,EMP_UID,EMAIL_ID,"
    				+ "TO_CHAR(RECOVERED_ON,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_EMP_MST "
					+ "WHERE COMPANY_CD=? AND EMP_CD!=? AND EMP_STATUS='Y'";	//PRATHAM BHATT 20240805: for considering Active Users only.

    		stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "0");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String last_valid_login_on = rset.getString(1)==null?"":rset.getString(1);
				String emp_cd = rset.getString(2)==null?"":rset.getString(2);
				String emp_nm = rset.getString(3)==null?"":rset.getString(3);
				String emp_uid = rset.getString(4)==null?"":rset.getString(4);
				String email_id = rset.getString(5)==null?"":rset.getString(5);
				String dormant_recoverd_dt = rset.getString(6)==null?"":rset.getString(6);
				String ent_dt = rset.getString(7)==null?"":rset.getString(7);
				
				String consideration_dt = getConsidarationDate(dormant_recoverd_dt, last_valid_login_on, ent_dt);				
				int diffrance = 0;
				boolean dormant = false;
				
				if(!consideration_dt.equals("")) 
				{
					String[] split_consideration_dt = consideration_dt.split("/");
		            String y_consideration_dt = split_consideration_dt[2];
		            String m_consideration_dt = split_consideration_dt[1];
		            String d_consideration_dt = split_consideration_dt[0];
		            
		            String sdf_consideration_dt = y_consideration_dt+m_consideration_dt+d_consideration_dt;
		            
		            String[] split_sysdate = sysdate.split("/");
		            String y_sysdate = split_sysdate[2];
		            String m_sysdate = split_sysdate[1];
		            String d_sysdate = split_sysdate[0];
		            
		            int y_diff = Integer.parseInt(y_sysdate)-Integer.parseInt(y_consideration_dt);
		            int m_diff = Integer.parseInt(m_sysdate)-Integer.parseInt(m_consideration_dt);
		            int d_diff = Integer.parseInt(d_sysdate)-Integer.parseInt(d_consideration_dt);
					
		             diffrance = ((y_diff*365)+((m_diff*30)+d_diff));
				}
				else 
				{
					dormant = true;
				}
	            
				int dormant_days= 0;
				
				String queryString="SELECT DORMNT_DAYS "
						+ "FROM FMS_ADMIN_POLICY "
						+ "WHERE COMPANY_CD=? AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "ORDER BY EFF_DT DESC";
				
				stmt_prep=conn.prepareStatement(queryString);
				
				stmt_prep.setString(1, comp_cd);
				stmt_prep.setString(2, sysdate);
				rset4=stmt_prep.executeQuery();
				if(rset4.next())
				{
					dormant_days = Integer.parseInt(rset4.getString(1)==null?"":rset4.getString(1));
				}
				rset4.close();
				stmt_prep.close();
	            
				//Added by Pratham Bhatt for storing the remainder email details.
				if((diffrance-dormant_days == -1) || (diffrance-dormant_days == -2) || (diffrance-dormant_days == -3))
				{
					int days = (diffrance-dormant_days)*(-1);
					emails.add(email_id);
					uids.add(emp_uid);
					V_days.add(""+days);
				}
				
	            if(diffrance>=dormant_days) 
	            {
	            	dormant = true;
	            }
	           
	            if(dormant)
	            {
	            	dormnt_user_count++;
	            	
	            	queryString2 = "UPDATE FMS_EMP_MST SET EMP_STATUS=?,IS_RECOVERED=?, MODIFY_DT=SYSDATE "
	            			+ "WHERE EMP_CD=? AND COMPANY_CD=? AND EMP_CD!=? ";

	            	stmt2=conn.prepareStatement(queryString2);
	            	stmt2.setString(1, "D");
	            	stmt2.setString(2, "N");
	            	stmt2.setString(3, emp_cd);
	            	stmt2.setString(4, comp_cd);
					stmt2.setString(5, "0");
					stmt2.executeUpdate();
	            						
	            	String seq_no="1";
	            	queryString3 = "SELECT MAX(SEQ_NO) FROM FMS_EMP_MST_LOG WHERE EMP_CD=? AND COMPANY_CD=?";
	 				stmt3 = conn.prepareStatement(queryString3);
	 				stmt3.setString(1, emp_cd);
	 				stmt3.setString(2, comp_cd);
	 				rset3 = stmt3.executeQuery();
	 				
	 				if(rset3.next())
	 				{
	 					seq_no=""+(rset3.getInt(1)+1);
	 				}

	 				queryString4="INSERT INTO FMS_EMP_MST_LOG(EMP_CD,SEQ_NO,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,MOB_NO,COMPANY_CD,UNIT_CD,"
	 						+ "REMARK,MODIFY_DT,MODIFY_BY,LOCK_STATUS) SELECT EMP_CD,?,EFF_DT,EMP_NM,EMP_UID,EMAIL_ID,EMP_STATUS,PH_NO,"
	 						+ "MOB_NO,COMPANY_CD,UNIT_CD,REMARK,SYSDATE,?,LOCK_STATUS "
	 						+ "FROM FMS_EMP_MST "
	 						+ "WHERE EMP_CD=? AND COMPANY_CD=? AND EMP_CD!=? ";
	 				
	 				stmt4=conn.prepareStatement(queryString4);
	 				stmt4.setString(1, seq_no);
	 				stmt4.setString(2, "0");
	 				stmt4.setString(3, emp_cd);
	 				stmt4.setString(4, comp_cd);
	 				stmt4.setString(5, "0");
	 				stmt4.executeUpdate();
					
	 				emp_nm_dtl += "<tr><td>"+emp_nm+"</td><td>"+emp_uid+"</td><td align=\"center\">"+last_valid_login_on+"</td></tr>";
	            
	 				String dormant_audit_msg = emp_nm+" marked Dormant!";
	 				try
	 				{
	 					new automation.Auto_InfoLogger().InsertInfoLogger("0", comp_cd,"System", ip, "0", "Auto Dormant","0","Auto Dormant", "", "", dormant_audit_msg);  	
	 				}
	 				catch(Exception infoLogger)
	 				{
	 					new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
	 				}
	            }
			}
			emp_nm_dtl +="</tbody></table></span>";
			
			//Added by Pratham Bhatt for sending the details of user dormant details to the function.
			if(V_days.size()!=0 && uids.size()!=0 && emails.size()!=0)
			{
				sendDormantAlert(comp_cd,comp_abbr,emails, uids,V_days);
			}
			
			String msg = dormnt_user_count+" users marked Dormant!";
			if(dormnt_user_count>0) 
			{
				msg += " Dormant Notification Email Generated!";
				//Added By HM20231106 : For Generating Mail for user get Dormant
				DormantUserMailBody(comp_cd,comp_abbr,emp_nm_dtl,"Dormant", sysdate, dormnt_user_count);
			}
						
			try
			{
				new automation.Auto_InfoLogger().InsertInfoLogger("0", comp_cd,"System", ip, "0", "Auto Dormant","0","Auto Dormant", "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    public static String getConsidarationDate(String recover_dt, String login_dt, String ent_dt) 
    {
    	String function_nm="getConsidarationDate()";
    	LocalDate maxDate = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String dateStr : new String[]{recover_dt, login_dt, ent_dt}) 
        {
        	try 
        	{
        		LocalDate date = LocalDate.parse(dateStr, formatter);
        		if (maxDate == null || date.isAfter(maxDate))
        		{
        			maxDate = date;
        		}
        	}
        	catch (Exception e) 
        	{
        		//HM written below to handle : recover_dt or login_dt can be Null
        		//System.out.println("Invalid date format or Date might be Null: " + dateStr + ".");
        	}
        }

        if (maxDate != null)
        {
        	return maxDate.format(formatter);
        }
        else
        {
        	return "";
        }
    }
    
    //Added by Pratham Bhatt for Sending Alert Email to individual users.
    private void sendDormantAlert(String comp_cd,String comp_nm,Vector V_email, Vector emp_nm, Vector days) throws Exception
    {
    	String function_nm = "sendDormantAlert()";
    	String mailBody="";
    	try
    	{

			context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
			
			String subject=comp_nm+" FMSng "+context+": Account Dormant Alert!";
    		String cc_mail_preDef = utilBean.getCcMailReceipentList(conn,comp_cd,"Dormant Notification","Admin","NA","On-Event");
    		for(int i =0; i<V_email.size();i++)
    		{	
    			mailBody="<html><span style= font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>"
    					+ "Dear FMSng User,"
    					+"<br><br> Your Account (<b>"+emp_nm.elementAt(i)+"</b>) will go dormant in "+days.elementAt(i)+" days!"
    					+"<br>Please login to avoid same."
    					+"<br><br>";
    			
    			mailBody+="<br><font style='font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>Please maintain confidentiality."
    					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
    					+ "</span></html>";
    			String email = V_email.elementAt(i).toString();
    			if(!email.equals("") && !mailBody.equals(""))
    			{
    				mailDelv.sendMail(conn,email, subject, mailBody, "", cc_mail_preDef, "");
    			}
    		}
    		
    	}
    	catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
    }
    
    private void DormantUserMailBody(String comp_cd,String comp_nm,String user_nm_list,String emp_status,String sysdate, int dormnt_user_count) throws Exception
	{
    	String function_nm="DormantUserMailBody()";
		String mailBody="";
		try
		{
			context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
			
			String subject=comp_nm+" FMSng "+context+": "+dormnt_user_count+" Users Marked Dormant on "+sysdate+"";
			mailBody="<html><span style= font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>"
					+ "Following "+dormnt_user_count+" users are marked dormant on "+sysdate+" :<br><br>"
					+ ""+user_nm_list+"<br><br>";

			mailBody+="<br><font style='font-size:"+mail_font_size+";font-family:"+mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</span></html>";
			
			String to_mail_preDef = utilBean.getToMailReceipentList(conn,comp_cd,"Dormant Notification","Admin","NA","On-Event"); 
			String cc_mail_preDef = utilBean.getCcMailReceipentList(conn,comp_cd,"Dormant Notification","Admin","NA","On-Event");
						
			if(!to_mail_preDef.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_preDef, subject, mailBody, "", cc_mail_preDef, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}

    String from_mail="";
    String to_mail="";
    String comp_cd= "";
    String comp_abbr= "";
    String emp_cd="";
}
