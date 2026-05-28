package automation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Arth Patel
//Code Reviewed by	:  
//CR Date			: 20/09/2023 
//Status	  		: Developing
//Modified By		: Harsh Maheta 20231211

public class Auto_CreditRisk_Emails 
{
	public static void main(String[] args) 
	{
		String report_param=args.length>0?args[0]:"";
		System.out.println(report_param);
		Auto_CreditRisk_Reports cram = new Auto_CreditRisk_Reports();
		cram.init(report_param);
	}
}

class Auto_CreditRisk_Reports
{
	String db_src_file_name="Auto_CreditRisk_Emails.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt13,stmt14,stmt15,stmt16,stmt_tmp,stmt_tmp1,stmt_tmp2,stmt_tmp3,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset5,rset6,rset13,rset14,rset15,rset16,rset_tmp,rset_tmp1,rset_tmp2,rset_tmp3,rset_temp,rset_temp1;
	String queryString="",queryString1="",queryString2="",queryString3="";
	String queryString4="",queryString5="",queryString6="",queryString7="";
	String queryString8="",queryString9="",queryString10="",queryString11="",queryString12="",queryString13="",queryString14="",queryString15="",queryString16="";
	String queryString_temp="";
	String queryString_temp1="";
	String queryString_temp2="";


	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");

	//Auto_UtilBean utilBean = new Auto_UtilBean();
	UtilBean utilBean= new UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mailDelv = new Auto_MailDelivery();

	public void init(String report_param)
	{
		String function_nm="init()";
		try
		{
			conn=new Auto_DB_Connection().db_conn();

			if(conn != null)  
			{
				String query = "SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE EFF_DT<SYSDATE AND STATUS=?";
				stmt=conn.prepareStatement(query);
				stmt.setString(1, "Y");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VCOMP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				}
				rset.close();
				stmt.close();
				
				
				comp_cd= "0"; //NOTE : As for SEI Central Level;
				emp_cd=""; 

				//Added By HM20231212
				//comp_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
				context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
				daily_file_path=utilBean.getAutomationKeyDetail(conn, "DAILY_RPT_PATH");
				
				String main_folder=CommonVariable.work_dir;
				File MainDir = new File(File.separator+main_folder);
		        if(!MainDir.exists()) 
		        {
		        	MainDir.mkdir();
		        }
		        
				String savePath = main_folder+File.separator+"daily_reports";
				File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		            fileSaveDir.mkdir();
		        }

				today_date = utilDate.getSysdate();
				yes_date = utilDate.getPreviousDate();

				lastdtofmonth=utilDate.getLastDateOfMonth(today_date);
				firstdtofmonth=utilDate.getFirstDateOfMonth(today_date);

				queryString2 = "SELECT TO_CHAR(SYSDATE,'DY') FROM DUAL";
				stmt2=conn.prepareStatement(queryString2);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					short_day_nm = rset2.getString(1)==null?"":rset2.getString(1);  
				}
				rset2.close();
				stmt2.close();

				int day = 4;
				int count=0;

				for(int i=0; i < day; i++)
				{
					queryString_temp = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
					stmt_tmp=conn.prepareStatement(queryString_temp);
					stmt_tmp.setString(1, firstdtofmonth);
					stmt_tmp.setInt(2, i);
					rset_tmp=stmt_tmp.executeQuery();
					if(rset_tmp.next())
					{
						String loopSysdate = rset_tmp.getString(1);
						nextfiveworkingdt = loopSysdate;

						String checkHoliday = "SELECT COUNT(*) FROM FMS_CURVE_HOLIDAY_CALND WHERE HOLIDAY_DT = TO_DATE(?,'DD/MM/YYYY') AND STATUS=?";
						stmt_tmp1=conn.prepareStatement(checkHoliday);
						stmt_tmp1.setString(1, loopSysdate);
						stmt_tmp1.setString(2, "Y");
						rset_tmp1=stmt_tmp1.executeQuery();
						if(rset_tmp1.next())
						{
							int holidayCount = rset_tmp1.getInt(1);

							if(holidayCount >= 1)
							{
								day = day + 1;
							}
							else
							{
								queryString_temp1 = "SELECT COUNT(*),TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD/MM/YYYY') FROM DUAL "
										+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DY') IN (?) OR "
										+ "TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DY') IN (?)";
								stmt_tmp2=conn.prepareStatement(queryString_temp1);
								stmt_tmp2.setString(1, loopSysdate);
								stmt_tmp2.setString(2, loopSysdate);
								stmt_tmp2.setString(3, "SUN");
								stmt_tmp2.setString(4, loopSysdate);
								stmt_tmp2.setString(5, "SAT");
								rset_tmp2=stmt_tmp2.executeQuery();
								if(rset_tmp2.next())
								{
									count = rset_tmp2.getInt(1);
									if(count == 1)
									{
										day = day + 1; 
									}
								}
								rset_tmp.close();
								stmt_tmp.close();
							}
						}
						rset_tmp1.close();
						stmt_tmp1.close();
					}
					rset_tmp.close();
					stmt_tmp.close();
				}

				if(report_param.equals("ReceivableReport"))
				{
					// For (WEEKLY) Receivable report ..         
					doClear();
	
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Weekly","Auto"); 
					if(!to_mail.equals(""))
					{
						getReceivableReport();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR RECEIVABLE REPORT (WEEKLY)");
					}
				
					// For (MONTHLY) Receivable report ..
					if(nextfiveworkingdt.equals(today_date))
					{
						doClear();
	
						to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Monthly","Auto"); 
						if(!to_mail.equals(""))
						{
							getMonthlyReceivableReport();
						}
						else
						{
							System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR RECEIVABLE REPORT (MONTHLY)");
						}
					}
					else
					{
						System.out.println("Monthly Receivable REPORT NOT EXECUTED AS IF CONDITION NOT MATCHED..........");
					}
				}
				else if(report_param.equals("ExpireSecurity"))
				{
					checkForSecurityExpiry();
					System.out.println("Function to Expire Security Exicuted!");
				}
				else
				{
					
					// For (DAILY) Audit report .. 
					
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Audit Report","Risk Mgmt","Daily","Auto"); 
					if(!to_mail.equals(""))
					{
						fetchAuditReport();
						
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR AUDIT REPORT (DAILY) FOR"+comp_abbr);
					}
	    			
					// For (DAILY) IGX Margining report .. 
					
						
					doClear();
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"IGX Margining Report","Risk Mgmt","Daily","Auto");
					if(!to_mail.equals(""))
					{
						String[] files_nm= new String[VCOMP_CD.size()];
						for(int i=0; i<VCOMP_CD.size(); i++)
						{
							
							doClear();
							filename="";
							comp_cd=""+VCOMP_CD.elementAt(i);
							comp_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
							getIGXContractListforMargin();
							getIgxContractListforMarginforSevenDay();
							getDateWiseSum();
							getIGXContractListforMarginMailBody();
							
							files_nm[i]=filename;
							
						}
						
						String to_mail_list = utilBean.getToMailReceipentList(conn,"0","IGX Margining Report","Risk Mgmt","Daily","Auto");
						String cc_mail_list= utilBean.getCcMailReceipentList(conn,"0","IGX Margining Report","Risk Mgmt","Daily","Auto");
						if(!to_mail_list.equals("") && !mailBody.equals(""))
						{
							mailDelv.sendMailWithMultipleAttachment(conn,"0",to_mail_list, subject, mailBody, files_nm, cc_mail_list, "");
							System.out.println("IGX Margining Report Mail Done(Daily).....");
						}
						
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR IGX MARGINING REPORT (DAILY)");
					}
					
					// For (WEEKLY) Upcoming Security report ..         
					doClear();

					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Upcoming Security Report","Risk Mgmt","Weekly","Auto"); 
					if(!to_mail.equals(""))
					{
						getUpcomingSecurityDtls();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR UPCOMING SECURITY REPORT (WEEKLY)");
					}
					
					// For (DAILY) Outgoing Security Status report ..         
					doClear();

					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Outgoing Security Status Report","Risk Mgmt","Daily","Auto");
					cc_mail= utilBean.getCcMailReceipentList(conn,comp_cd,"Outgoing Security Status Report","Risk Mgmt","Daily","Auto");
					mail_msg = "Outgoing Security Status Report Mail Done(Daily).....";
					if(!to_mail.equals(""))
					{
						getOutgoingSecurityStatusDtls();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR OUTGOING SECURITY STATUS REPORT (DAILY)");
					}
				
					// For (WEEKLY) Outgoing Security Status report ..         
					doClear();

					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Outgoing Security Status Report","Risk Mgmt","Weekly","Auto"); 
					cc_mail= utilBean.getCcMailReceipentList(conn,comp_cd,"Outgoing Security Status Report","Risk Mgmt","Weekly","Auto");
					mail_msg = "Outgoing Security Status Report Mail Done(Weekly).....";
					if(!to_mail.equals(""))
					{
						getOutgoingSecurityStatusDtls();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR OUTGOING SECURITY STATUS REPORT (WEEKLY)");
					}
				
					// For (DAILY) Incoming Security Status report ..         
					doClear();

					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Incoming Security Status Report","Risk Mgmt","Daily","Auto");
					if(!to_mail.equals(""))
					{
						getIncomingSecurityStatusDtls();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR INCOMING SECURITY STATUS REPORT (DAILY)");
					}
					
					// For (DAILY) Payment Receipt Status report ..         
					doClear();

					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Payment Receipt Status Report","Risk Mgmt","Daily","Auto"); 
					if(!to_mail.equals(""))
					{
						getPaymentReceiptStatusDetails();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR PAYMENT RECEIPT STATUS REPORT (DAILY)");
					}

					// For (DAILY) Bank Limit Exposure Report ..         
					doClear();
	
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Bank Limit and Exposure Report","Risk Mgmt","Daily","Auto"); 
					if(!to_mail.equals(""))
					{
						getBankLimitExposure();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR BANK LIMIT EXPOSURE REPORT (DAILY)");
					}
	
					// For (DAILY) Exceed Credit Report ..         
					doClear();
	
					/*to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Exceed Credit Report","Risk Mgmt","Daily","Auto"); 
					if(!to_mail.equals(""))
					{
						isCreditExceedRpt="Y";
						getExpoxureHeading();
						getActiveSellContractInformation();
						getChargeMaster();
						ExposureCalculation();
						CR_LimitCalculation();
						CR_IGX_Suummary();
						getExccedCreditReason();
						getExeedCreditReportMailBody();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR EXCEED CREDIT REPORT (DAILY)");
					}*/
	
					// For (MONTHLY) KPI Report ..  
					if(lastdtofmonth.equals(today_date))
					{
						doClear();
	
						to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"KPI Report","Risk Mgmt","Monthly","Auto"); 
						if(!to_mail.equals(""))
						{
							getKPIReport();
						}
						else
						{
							System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR KPI REPORT (MONTHLY)");
						}
					}					
					else 
					{ 
						System.out.println("(MONTHLY)KPI REPORT NOT EXECUTED, AS THIS GETS GENERATED AT END OF MONTH"); 
					}
	
					// For (DAILY) Limit Summary Report ..         
					doClear();
	
					to_mail = utilBean.getToMailReceipentList(conn,comp_cd,"Limit Summary Report","Risk Mgmt","Daily","Auto"); 
					if(!to_mail.equals(""))
					{
						getLimitSummaryDtls();
					}
					else
					{
						System.out.println("RECIPIENT LIST IS NOT AVAILABLE FOR LIMIT SUMMARY REPORT (DAILY)");
					}
				}
	
				System.out.println("ALL FUNCTION EXECUTED!!");
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
	
	private void checkForSecurityExpiry() 
	{
		String function_nm="checkForSecurityExpiry()";
		try
		{
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,GX,SEQ_REV_NO "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE EXPIRE_DT<SYSDATE AND STATUS='O' AND APRV_DT IS NOT NULL";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String company_cd=rset.getString(1)==null?"":rset.getString(1);
				String countPty_cd=rset.getString(2)==null?"":rset.getString(2);
				String seq_no=rset.getString(3)==null?"":rset.getString(3);
				String sec_ref=rset.getString(4)==null?"":rset.getString(4);
				String gx=rset.getString(5)==null?"":rset.getString(5);
				String seq_rev_no=rset.getString(6)==null?"":rset.getString(6);
				
				String query="UPDATE FMS_SECURITY_MST SET STATUS='E',MODIFY_DT=SYSDATE,MODIFY_BY='0' "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? AND SEC_REF_NO=? AND GX=? AND SEQ_REV_NO=?";
				stmt1=conn.prepareStatement(query);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, countPty_cd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, sec_ref);
				stmt1.setString(5, gx);
				stmt1.setString(6, seq_rev_no);
				stmt1.executeQuery();
				stmt1.close();
				
				String dtl_seqNo1 = "";
				String query1 = "SELECT MAX(NVL(LOG_SEQ_NO,1)+1) "
						+ "FROM LOG_FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEQ_REV_NO=? AND GX=?";
				//JD20231012 REMOVED SEC_TYPE IN WHERE CLAUSE
				stmt1=conn.prepareStatement(query1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, countPty_cd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, seq_rev_no);
				stmt1.setString(5, gx);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					dtl_seqNo1 = rset1.getString(1)==null?"1":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				String query2 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, LOG_ENT_DT, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT) "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, ?, SYSDATE, SEC_REF_NO, SEC_CATEGORY, "
						+ "SEC_TYPE, DEAL_TYPE, GUARANTOR_CD, CURRENCY, VARIATION_VALUE, VALUE, VALUE_FLUC, ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, "
						+ "ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT, TENOR, REVIEW_DT, "
						+ "STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, GX, SEQ_REV_NO, SAP_APPROVAL, "
						+ "SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
				stmt2=conn.prepareStatement(query2);
				stmt2.setString(1, dtl_seqNo1);
				stmt2.setString(2, company_cd);
				stmt2.setString(3, countPty_cd);
				stmt2.setString(4, seq_no);
				stmt2.setString(5, seq_rev_no);
				stmt2.setString(6, gx);
				stmt2.executeQuery();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getLimitSummaryDtls() 
	{
		String function_nm="getLimitSummaryDtls()";
		String mailBody = "";
		try
		{
			String sysdate = today_date;

			queryString2 = "SELECT DISTINCT COUNTERPARTY_CD,BANK_CD,GX FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
					+ "GX=? ";
			stmt2 = conn.prepareStatement(queryString2);
			//stmt2.setString(1, comp_cd);
			stmt2.setString(1, "K");
			rset2 = stmt2.executeQuery();
			while(rset2.next())
			{
				String counterpty_Cd =  rset2.getString(1)==null?"":rset2.getString(1);
				String bank_Cd =  rset2.getString(2)==null?"":rset2.getString(2);
				String clearance = rset2.getString(3)==null?"":rset2.getString(3);

				if(!counterpty_Cd.equals("0") && bank_Cd.equals("0"))
				{
					if(clearance.equals("I"))
					{
						VCOUNTERPARTY_CD.add(counterpty_Cd);
						VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterpty_Cd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterpty_Cd));
					}
					else
					{
						VCOUNTERPARTY_CD.add(counterpty_Cd);
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_Cd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_Cd));
					}
				}
				else if(counterpty_Cd.equals("0") && !bank_Cd.equals("0"))
				{
					VCOUNTERPARTY_CD.add(bank_Cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getBankName(conn,bank_Cd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getBankABBR(conn,bank_Cd));
				}

				String credit_rating = "";
				queryString3 = "SELECT CREDIT_RATING "
						+ "FROM FMS_LIMIT_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
						+ "AND LIMIT_ID=(SELECT MAX(LIMIT_ID) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=?) ";
				stmt3 = conn.prepareStatement(queryString3);
				//stmt3.setString(1, comp_cd);
				stmt3.setString(1, counterpty_Cd);
				stmt3.setString(2, bank_Cd);
				stmt3.setString(3, clearance);
				//stmt3.setString(5, comp_cd);
				stmt3.setString(4, counterpty_Cd);
				stmt3.setString(5, bank_Cd);
				stmt3.setString(6, clearance);
				rset3 = stmt3.executeQuery();
				if(rset3.next())
				{
					credit_rating = rset3.getString(1)==null?"":rset3.getString(1);
					VCREDIT_RATING.add(credit_rating);
				}
				rset3.close();
				stmt3.close();

				double available = 0;
				double total_limit = 0;
				double unsecured = 0;
				double temporary =0;
				double adjust_usage = 0;
				double net_usage = 0;
				double usage = 0;
				double used = 0;
				queryString4 = "SELECT AMT,AMT_UNIT,LIMIT_ID,LIMIT_TYPE,SEQ_NO,ACTION_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(EXP_DT,'DD/MM/YYYY'),TO_CHAR(REVIEW_DT,'DD/MM/YYYY'),CATEGORY,COUNTERPARTY_CD,BANK_CD,REMARKS "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
						+ "((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT>=TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR ((EXP_DT>=TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) AND INACTIVATION_DT-1>=TO_DATE(?,'DD/MM/YYYY')))";
				stmt4 = conn.prepareStatement(queryString4);
				//stmt4.setString(1, comp_cd);
				stmt4.setString(1, counterpty_Cd);
				stmt4.setString(2, bank_Cd);
				stmt4.setString(3, clearance);
				stmt4.setString(4, sysdate);
				stmt4.setString(5, sysdate);
				stmt4.setString(6, sysdate);
				stmt4.setString(7, sysdate);
				rset4 = stmt4.executeQuery();
				while(rset4.next())
				{
					String action_type = rset4.getString(6)==null?"":rset4.getString(6);
					String limit_type = rset4.getString(4)==null?"":rset4.getString(4);
					double amt = Double.parseDouble(rset4.getString(1)==null?"0":rset4.getString(1));

					if(!limit_type.equals("Unsecured") && action_type.equals("Adjust Usage"))
					{
						adjust_usage = adjust_usage + amt;
					}
					if(limit_type.equals("Unsecured") && action_type.equals("Adjust Limit"))
					{
						unsecured = unsecured + amt;
					}
					if(limit_type.equals("Temporary") && action_type.equals("Adjust Limit"))
					{
						temporary = temporary + amt;
					}
					if(action_type.equals("Adjust Limit"))
					{
						total_limit = total_limit + amt;
					}
				}
				rset4.close();
				stmt4.close();

				if(!counterpty_Cd.equals("") && !counterpty_Cd.equals("0") && bank_Cd.equals("0"))
				{
					usage = limit_usage_calculation(counterpty_Cd);
				}

				available = total_limit + adjust_usage - usage;
				net_usage = usage - adjust_usage;

				if(total_limit > 0)
				{
					used = (usage*100 )/ total_limit;
				}
				else
				{
					used = 0;
				}

				VAVAILABLE.add(nf.format(available));
				VTOTAL_LIMIT.add(nf.format(total_limit));
				VUNSECURED.add(nf.format(unsecured));
				VTEMPORARY.add(nf.format(temporary));
				VADJUST_USAGE.add(nf.format(adjust_usage));
				VUSAGE.add(nf.format(usage));
				VNET_USAGE.add(nf.format(net_usage));
				VUSED.add(nf.format(used));
			}
			rset2.close();
			stmt2.close();

			queryString5 = "SELECT DISTINCT COUNTERPARTY_CD,BANK_CD,GX FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
					+ "GX=? ";
			stmt5 = conn.prepareStatement(queryString5);
			//stmt5.setString(1, comp_cd);
			stmt5.setString(1, "I");
			rset5 = stmt5.executeQuery();
			while(rset5.next())
			{
				String counterpty_Cd =  rset5.getString(1)==null?"":rset5.getString(1);
				String bank_Cd =  rset5.getString(2)==null?"":rset5.getString(2);
				String clearance = rset5.getString(3)==null?"":rset5.getString(3);

				if(!counterpty_Cd.equals("0") && bank_Cd.equals("0"))
				{
					if(clearance.equals("I"))
					{
						V_COUNTERPARTY_CD.add(counterpty_Cd);
						V_COUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterpty_Cd));
						V_COUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterpty_Cd));
					}
					else
					{
						V_COUNTERPARTY_CD.add(counterpty_Cd);
						V_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_Cd));
						V_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_Cd));
					}
				}
				else if(counterpty_Cd.equals("0") && !bank_Cd.equals("0"))
				{
					V_COUNTERPARTY_CD.add(bank_Cd);
					V_COUNTERPARTY_NM.add(""+utilBean.getBankName(conn,bank_Cd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getBankABBR(conn,bank_Cd));
				}

				String credit_rating = "";
				queryString3 = "SELECT CREDIT_RATING "
						+ "FROM FMS_LIMIT_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
						+ "AND LIMIT_ID=(SELECT MAX(LIMIT_ID) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=?) ";
				stmt3 = conn.prepareStatement(queryString3);
				//stmt3.setString(1, comp_cd);
				stmt3.setString(1, counterpty_Cd);
				stmt3.setString(2, bank_Cd);
				stmt3.setString(3, clearance);
				//stmt3.setString(5, comp_cd);
				stmt3.setString(4, counterpty_Cd);
				stmt3.setString(5, bank_Cd);
				stmt3.setString(6, clearance);
				rset3 = stmt3.executeQuery();
				if(rset3.next())
				{
					credit_rating = rset3.getString(1)==null?"":rset3.getString(1);
					V_CREDIT_RATING.add(credit_rating);
				}
				rset3.close();
				stmt3.close();

				double available = 0;
				double total_limit = 0;
				double unsecured = 0;
				double temporary =0;
				double adjust_usage = 0;
				double net_usage = 0;
				double usage = 0;
				double used = 0;

				queryString4 = "SELECT AMT,AMT_UNIT,LIMIT_ID,LIMIT_TYPE,SEQ_NO,ACTION_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(EXP_DT,'DD/MM/YYYY'),TO_CHAR(REVIEW_DT,'DD/MM/YYYY'),CATEGORY,COUNTERPARTY_CD,BANK_CD,REMARKS "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
						+ "((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT>=TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR ((EXP_DT>=TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) AND INACTIVATION_DT-1>=TO_DATE(?,'DD/MM/YYYY')))";
				stmt4 = conn.prepareStatement(queryString4);
				//stmt4.setString(1, comp_cd);
				stmt4.setString(1, counterpty_Cd);
				stmt4.setString(2, bank_Cd);
				stmt4.setString(3, clearance);
				stmt4.setString(4, sysdate);
				stmt4.setString(5, sysdate);
				stmt4.setString(6, sysdate);
				stmt4.setString(7, sysdate);
				rset4 = stmt4.executeQuery();
				while(rset4.next())
				{
					String action_type = rset4.getString(6)==null?"":rset4.getString(6);
					String limit_type = rset4.getString(4)==null?"":rset4.getString(4);
					double amt = Double.parseDouble(rset4.getString(1)==null?"0":rset4.getString(1));

					if(!limit_type.equals("Unsecured") && action_type.equals("Adjust Usage"))
					{
						adjust_usage = adjust_usage + amt;
					}
					if(limit_type.equals("Unsecured") && action_type.equals("Adjust Limit"))
					{
						unsecured = unsecured + amt;
					}
					if(limit_type.equals("Temporary") && action_type.equals("Adjust Limit"))
					{
						temporary = temporary + amt;
					}
					if(action_type.equals("Adjust Limit"))
					{
						total_limit = total_limit + amt;
					}
				}
				rset4.close();
				stmt4.close();

				if(!counterpty_Cd.equals("") && !counterpty_Cd.equals("0") && bank_Cd.equals("0"))
				{
					usage = limit_usage_calculation(counterpty_Cd);
				}

				available = total_limit + adjust_usage - usage;
				net_usage = usage - adjust_usage;

				if(total_limit > 0)
				{
					used = (usage*100 )/ total_limit;
				}
				else
				{
					used = 0;
				}

				V_AVAILABLE.add(nf.format(available));
				V_TOTAL_LIMIT.add(nf.format(total_limit));
				V_UNSECURED.add(nf.format(unsecured));
				V_TEMPORARY.add(nf.format(temporary));
				V_ADJUST_USAGE.add(nf.format(adjust_usage));
				V_USAGE.add(nf.format(usage));
				V_NET_USAGE.add(nf.format(net_usage));
				V_USED.add(nf.format(used));
			}
			rset5.close();
			stmt5.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Limit_Summary_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Limit_Summary_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Limit Summary Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Limit Summary Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Limit Summary Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("KYC Limit Summary Report");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12));

			HSSFRow rowhead1 = sheet.createRow((short)1);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Counterparty/Bank Name");  
			rowhead1.createCell((short) 2).setCellValue("Counterparty/Bank ABBR");  
			rowhead1.createCell((short) 3).setCellValue("Credit rating");
			rowhead1.createCell((short) 4).setCellValue("Available limit (INR)");
			rowhead1.createCell((short) 5).setCellValue("Net Usage (INR)");
			rowhead1.createCell((short) 6).setCellValue("Usage (INR)"); 
			rowhead1.createCell((short) 7).setCellValue("Total limit (INR)"); 
			rowhead1.createCell((short) 8).setCellValue("Unsecured limit (INR)");
			rowhead1.createCell((short) 9).setCellValue("Temporary limit (INR)");
			rowhead1.createCell((short) 10).setCellValue("Usage Adjustment (INR)");
			rowhead1.createCell((short) 11).setCellValue("% Used");
			rowhead1.createCell((short) 12).setCellValue("Counterparty/Bank CD");

			int index = 0;
			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					index++;

					HSSFRow row = sheet.createRow((short)i+2);  
					row.createCell((short) 0).setCellValue(j);
					row.createCell((short) 1).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_ABBR.elementAt(i));
					row.createCell((short) 3).setCellValue(""+VCREDIT_RATING.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VAVAILABLE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VNET_USAGE.elementAt(i));  
					row.createCell((short) 6).setCellValue(""+VUSAGE.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VTOTAL_LIMIT.elementAt(i));
					row.createCell((short) 8).setCellValue(""+VUNSECURED.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VTEMPORARY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VADJUST_USAGE.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VUSED.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VCOUNTERPARTY_CD.elementAt(i));  
					VINDEX.add(index);
				}
			}
			else
			{
				index++;
				VINDEX.add(index);
			}

			HSSFRow row_head = sheet.createRow((short)VINDEX.size()+3);
			row_head.createCell((short) 0).setCellValue("IGX Limit Summary Report");
			sheet.addMergedRegion(new CellRangeAddress(VINDEX.size()+3,VINDEX.size()+3,0,12));

			HSSFRow rowhead = sheet.createRow((short)VINDEX.size()+4); 
			rowhead.createCell((short) 0).setCellValue("Sr#");
			rowhead.createCell((short) 1).setCellValue("Counterparty/Bank Name");  
			rowhead.createCell((short) 2).setCellValue("Counterparty/Bank ABBR");  
			rowhead.createCell((short) 3).setCellValue("Credit rating");
			rowhead.createCell((short) 4).setCellValue("Available limit (INR)");
			rowhead.createCell((short) 5).setCellValue("Net Usage (INR)");
			rowhead.createCell((short) 6).setCellValue("Usage (INR)"); 
			rowhead.createCell((short) 7).setCellValue("Total limit (INR)"); 
			rowhead.createCell((short) 8).setCellValue("Unsecured limit (INR)");
			rowhead.createCell((short) 9).setCellValue("Temporary limit (INR)");
			rowhead.createCell((short) 10).setCellValue("Usage Adjustment (INR)");
			rowhead.createCell((short) 11).setCellValue("% Used");
			rowhead.createCell((short) 12).setCellValue("Counterparty/Bank CD");

			if(V_COUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < V_COUNTERPARTY_NM.size(); i++ )
				{
					j++;

					HSSFRow row = sheet.createRow((short)VINDEX.size()+i+5);
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+V_COUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+V_COUNTERPARTY_ABBR.elementAt(i));
					row.createCell((short) 3).setCellValue(""+V_CREDIT_RATING.elementAt(i));
					row.createCell((short) 4).setCellValue(""+V_AVAILABLE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+V_NET_USAGE.elementAt(i));  
					row.createCell((short) 6).setCellValue(""+V_USAGE.elementAt(i));
					row.createCell((short) 7).setCellValue(""+V_TOTAL_LIMIT.elementAt(i));
					row.createCell((short) 8).setCellValue(""+V_UNSECURED.elementAt(i));
					row.createCell((short) 9).setCellValue(""+V_TEMPORARY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+V_ADJUST_USAGE.elementAt(i));
					row.createCell((short) 11).setCellValue(""+V_USED.elementAt(i));
					row.createCell((short) 12).setCellValue(""+V_COUNTERPARTY_CD.elementAt(i));
				}
			}

			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 12; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Limit Summary Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Limit Summary Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Limit Summary Report Mail Done(Daily).....");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private double limit_usage_calculation(String countpty_Cd) 
	{
		double countSettlementExpo=0;
		String function_nm="limit_usage_calculation()";
		try
		{
			String info = "";
			String sysdate="";
			String nextSevenDayDt="";
			String yesdt="";
			String firstDtofMonth = "";
			String sysdate_2="";
			queryString13 = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY'),TO_CHAR(SYSDATE+6,'DD/MM/YYYY'),TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),TO_CHAR(SYSDATE-2,'DD/MM/YYYY') FROM DUAL";
			stmt13 = conn.prepareStatement(queryString13);
			rset13 = stmt13.executeQuery();
			if(rset13.next())
			{
				sysdate = rset13.getString(1)==null?"":rset13.getString(1);
				nextSevenDayDt = rset13.getString(2)==null?"":rset13.getString(2);
				yesdt = rset13.getString(3)==null?"":rset13.getString(3);
				sysdate_2 = rset13.getString(4)==null?"":rset13.getString(4);
			}
			rset13.close();
			stmt13.close();
			firstDtofMonth=utilDate.getFirstDateOfMonth(sysdate);

			queryString14 = "SELECT COUNT(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT = TO_DATE(?,'DD/MM/YYYY')";
			stmt14 = conn.prepareStatement(queryString14);
			stmt14.setString(1, yesdt);
			rset14 = stmt14.executeQuery();
			if(rset14.next())
			{
				if(rset14.getInt(1) > 0)
				{
				}
				else
				{
					yesdt = sysdate_2;
				}
			}
			rset14.close();
			stmt14.close();

			queryString15 = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
					+ "FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(A.EFF_DT) FROM FMS_COUNTERPARTY_MST A WHERE "//A.COMPANY_CD=? AND "
					+ "A.COUNTERPARTY_CD=?)) AND "
					+ "REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY')";
			stmt15 = conn.prepareStatement(queryString15);
			//stmt15.setString(1, comp_cd);
			//stmt15.setString(2, comp_cd);
			stmt15.setString(1, countpty_Cd);
			//stmt15.setString(4, comp_cd);
			stmt15.setString(2, countpty_Cd);
			stmt15.setString(3, yesdt);
			stmt15.setString(4, firstDtofMonth);
			stmt15.setString(5, sysdate);
			rset15 = stmt15.executeQuery();
			while(rset15.next())
			{
				double dcq = rset15.getDouble(1);
				String deal_id = rset15.getString(2)==null?"":rset15.getString(2);
				String contType = rset15.getString(9)==null?"":rset15.getString(9);
				String dealNosplit[] = deal_id.split("-");
				String agmt_no = dealNosplit[1];
				String cont_no = dealNosplit[2];

				String countptyCd = rset15.getString(3)==null?"":rset15.getString(3);
				String price_type = rset15.getString(4)==null?"":rset15.getString(4);
				double cont_price = rset15.getDouble(5); 
				double fwd_price_fin = rset15.getDouble(6);
				double eff_deal_price = rset15.getDouble(7);
				String delv_dt = rset15.getString(8)==null?"":rset15.getString(8);
				String temp_account=rset15.getString(10)==null?"":rset15.getString(10);
				String company_cd = rset15.getString(11)==null?"":rset15.getString(11);

				info += "DealId "+deal_id+"~"+delv_dt+"~Price_Type = "+price_type+"~DCQ = "+rset15.getDouble(1)+"~";
				double total = 0;
				if(price_type.equals("Fixed"))
				{
					info+="Cont_Price = "+cont_price+"";
					total = dcq * cont_price;
				}
				else
				{
					if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
					{
						info+="Fwd_Price_Fin_Leg = "+fwd_price_fin+"";
						total = dcq * fwd_price_fin;
					}
					else
					{
						info+="Eff_Deal_Price = "+eff_deal_price+"";
						total = dcq * eff_deal_price;
					}
				}

				double availableExchgRate=getExchangeRate(company_cd, countptyCd, agmt_no, cont_no, contType, sysdate);

				double avgTaxInInv = 0;
				queryString16 = "SELECT MAX(FACTOR) "
						+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A, FMS_TAX_STRUCTURE_DTL B "
						+ "WHERE A.COMPANY_CD=? AND  "
						+ "A.TAX_STRUCT_CD=B.TAX_STR_CD AND COUNTERPARTY_CD=? AND APP_DATE <= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
						+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND A.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
				stmt16 = conn.prepareStatement(queryString16);
				stmt16.setString(1, company_cd);
				stmt16.setString(2, countptyCd);
				stmt16.setString(3, sysdate);
				stmt16.setString(4, sysdate);
				rset16 = stmt16.executeQuery();
				if(rset16.next())
				{
					avgTaxInInv=rset16.getDouble(1);
				}
				rset16.close();
				stmt16.close();

				info += "~Total = "+total+"~ExchgRate = "+nf.format(availableExchgRate)+"";
				double USDtoINR = total * availableExchgRate;
				info += "~ApplyTax = "+nf.format(avgTaxInInv)+"";
				double ApplyTax = USDtoINR * (avgTaxInInv/100);
				countSettlementExpo += USDtoINR + ApplyTax;	

				double tt = USDtoINR + ApplyTax; 
				info += "~Grand_Total = "+nf.format(tt)+" ";
			}
			rset15.close();
			stmt15.close();
			if(countSettlementExpo < 0)
			{
				countSettlementExpo = (-1)*(countSettlementExpo);
			}
			VINFO.add(info);
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return countSettlementExpo;
	}

	public void getKPIReport() 
	{
		String function_nm="getKPIReport()";
		String mailBody="";
		try
		{
			String split[] = today_date.split("/");
			String start_dt = "01/"+split[1]+"/"+split[2];
			String end_dt = today_date; 

			String splited_start_date = split[2]+split[1]+"01";
			String splited_end_date = split[2]+split[1]+split[0];

			//String start_dt = "01/10/2023";
			//String end_dt = "10/10/2023";

			queryString = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
					+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,GX,COMPANY_CD "
					+ "FROM LOG_FMS_SECURITY_MST A "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
					+ "AND RECEIPT_DT >= TO_DATE(?,'DD/MM/YYYY') AND RECEIPT_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND LOG_SEQ_NO = (SELECT MAX(D.LOG_SEQ_NO) FROM LOG_FMS_SECURITY_MST D WHERE D.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.GX=D.GX AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.STATUS=D.STATUS) ";

			queryString+= "ORDER BY ENT_DT DESC";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "OA");
			stmt.setString(2, "ADV");
			stmt.setString(3, "O");
			stmt.setString(4, "C");
			stmt.setString(5, start_dt);
			stmt.setString(6, end_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterPartyCd = rset.getString(1)==null?"":rset.getString(1);
				String sec_category = rset.getString(2)==null?"":rset.getString(2);
				String sec_type = rset.getString(3)==null?"":rset.getString(3);
				String sec_ref_no =  rset.getString(4)==null?"":rset.getString(4);
				String status = rset.getString(5)==null?"":rset.getString(5);
				String currency = rset.getString(6)==null?"":rset.getString(6);
				String value = rset.getString(7)==null?"":rset.getString(7);
				String received_date = rset.getString(8)==null?"":rset.getString(8);
				String seq_no = rset.getString(9)==null?"":rset.getString(9);
				String deal_type = rset.getString(10)==null?"":rset.getString(10);
				String iss_bank_cd = rset.getString(12)==null?"":rset.getString(12);
				String iss_bank_ref = rset.getString(13)==null?"":rset.getString(13);
				String adv_bank_cd = rset.getString(14)==null?"":rset.getString(14);
				String adv_bank_ref = rset.getString(15)==null?"":rset.getString(15);
				String confirm_bank_cd = rset.getString(16)==null?"":rset.getString(16);
				String confirm_bank_ref = rset.getString(17)==null?"":rset.getString(17);
				String issue_dt = rset.getString(18)==null?"":rset.getString(18);
				String expire_dt = rset.getString(19)==null?"":rset.getString(19);
				String remarks = rset.getString(22)==null?"":rset.getString(22);
				String cancel_date = rset.getString(25)==null?"":rset.getString(25);
				String seq_rev_no = rset.getString(27)==null?"":rset.getString(27);
				String gx = rset.getString(30)==null?"":rset.getString(30);
				String company_cd = rset.getString(31)==null?"":rset.getString(31);
				String counterparty_nm = "";
				if(gx.equals("I"))
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				String iss_bank_nm = ""+utilBean.getBankName(conn,iss_bank_cd);
				String adv_bank_nm = ""+utilBean.getBankName(conn,adv_bank_cd);
				String confirm_bank_nm = ""+utilBean.getBankName(conn,confirm_bank_cd);
				String ref_no = company_cd+"-"+sec_ref_no;

				VCOUNTERPARTY_CD.add(counterPartyCd);
				VSEC_CATEGORY.add(sec_category);
				VSEC_TYPE.add(sec_type);
				VSEC_REF_NO.add(ref_no);
				VSTATUS.add(getStatusName(status));
				VRECEIVED_DATE.add(received_date);
				VDEAL_TYPE.add(deal_type);
				VISS_BANK_CD.add(iss_bank_cd);
				VISS_BANK_NM.add(iss_bank_nm);
				VISS_BANK_REF.add(iss_bank_ref);
				VADV_BANK_CD.add(adv_bank_cd);
				VADV_BANK_NM.add(adv_bank_nm);
				VADV_BANK_REF.add(adv_bank_ref);
				VCONFIRM_BANK_CD.add(confirm_bank_cd);
				VCONFIRM_BANK_NM.add(confirm_bank_nm);
				VCONFIRM_BANK_REF.add(confirm_bank_ref);
				VISSUE_DT.add(issue_dt);
				VEXPIRE_DT.add(expire_dt);
				VREMARK.add(remarks);
				VCANCEL_DT.add(cancel_date);
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				if(currency.equals("1"))
				{
					VVALUE.add(value);
					VVALUE_USD.add("");
				}
				else 
				{
					VVALUE_USD.add(value);
					VVALUE.add("");
				}

				String sel_deal_dtl="";
				String deal_dtl = "";
				String dealNo = "";
				String deal_No = "";
				String deal_No_dtl="";
				String disp_cont_type="";

				queryString1 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD,GX "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, counterPartyCd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, sec_ref_no);
				stmt1.setString(5, seq_rev_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String agmt = rset1.getString(1)==null?"":rset1.getString(1);
					String agmt_rev = rset1.getString(2)==null?"":rset1.getString(2);
					String cont = rset1.getString(3)==null?"":rset1.getString(3);
					String cont_rev = rset1.getString(4)==null?"":rset1.getString(4);
					String cont_type = rset1.getString(5)==null?"":rset1.getString(5);
					String counterparty_cd = rset1.getString(6)==null?"":rset1.getString(6);
					String entityCd = rset1.getString(7)==null?"":rset1.getString(7);
					String gx_sec = rset1.getString(8)==null?"":rset1.getString(8);

					double exchange_rate = getExchangeRate(company_cd, counterparty_cd, agmt, cont, cont_type,today_date);
					VEXCHANGE_RATE.add(exchange_rate);

					if(gx_sec.equals("I"))
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;

						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							sel_deal_dtl+="@@"+dealDtl;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;

						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							sel_deal_dtl+="@@"+dealDtl;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				rset1.close();
				stmt1.close();

				queryString3 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				queryString3 +=" UNION ";
				queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString3 +=" UNION ";
				queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, company_cd);
				stmt3.setString(2, counterPartyCd);
				stmt3.setString(3, seq_no);
				stmt3.setString(4, sec_ref_no);
				stmt3.setString(5, seq_rev_no);
				stmt3.setString(6, company_cd);
				stmt3.setString(7, company_cd);
				rset3 = stmt3.executeQuery();
				while(rset3.next())
				{
					String agmt_no = rset3.getString(1)==null?"":rset3.getString(1);
					String agmt_rev_no = rset3.getString(2)==null?"":rset3.getString(2);
					String cont_no = rset3.getString(3)==null?"":rset3.getString(3);
					String cont_rev_no = rset3.getString(4)==null?"":rset3.getString(4);
					String cont_type = rset3.getString(5)==null?"":rset3.getString(5);
					String countpty_cd_no = rset3.getString(6)==null?"":rset3.getString(6);

					//deal_No=utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type);
					deal_No=utilBean.NewDealMappingId(company_cd, countpty_cd_no, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
					deal_No_dtl=sec_ref_no+"/"+cont_type+"/"+agmt_no+"/"+agmt_rev_no+"/"+cont_no+"/"+cont_rev_no+"/"+countpty_cd_no;

					double exchange_rate = getExchangeRate(company_cd, counterPartyCd, agmt_no, cont_no, cont_type,today_date);
					VEXCHANGE_RATE.add(exchange_rate);
				}
				VDEAL_NO.add(dealNo);
				VDIS_CONTRACT_TYPE.add(disp_cont_type);
				VCURRANCY.add(currency);

				rset3.close();
				stmt3.close();
			}
			rset.close();
			stmt.close();

			queryString5 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE  , STATUS , CURRENCY , VALUE , "
					+ "CONFIRM_BANK_CD,COMPANY_CD "
					+ "FROM LOG_FMS_SECURITY_MST A "					
					+ "WHERE "//COMPANY_CD=? AND "
					+ "SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
					+ "AND RECEIPT_DT >= TO_DATE(?,'DD/MM/YYYY') AND RECEIPT_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND LOG_SEQ_NO = (SELECT MAX(D.LOG_SEQ_NO) FROM LOG_FMS_SECURITY_MST D WHERE D.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.GX=D.GX AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.STATUS=D.STATUS) ";
			queryString5+= "ORDER BY ENT_DT DESC";
			stmt4 = conn.prepareStatement(queryString5);
			//stmt4.setString(1, comp_cd);
			stmt4.setString(1, "OA");
			stmt4.setString(2, "ADV");
			stmt4.setString(3, "O");
			stmt4.setString(4, "C");
			stmt4.setString(5, start_dt);
			stmt4.setString(6, end_dt);
			rset4 = stmt4.executeQuery();
			while(rset4.next())
			{
				String Counterparty_sum = rset4.getString(1)==null?"":rset4.getString(1);
				String sec_category_sum = rset4.getString(2)==null?"":rset4.getString(2);
				String security_type_sum = rset4.getString(3)==null?"":rset4.getString(3);
				String status_sum = rset4.getString(4)==null?"":rset4.getString(4);
				String value_sum = rset4.getString(6)==null?"":rset4.getString(6);
				String currency_sum = rset4.getString(5)==null?"":rset4.getString(5);
				String confirm_bankCd_sum = rset4.getString(7)==null?"":rset4.getString(7);
				String company_cd = rset4.getString(8)==null?"":rset4.getString(8);

				VDOUBLE_COUNTERPARTY_CD.add(Counterparty_sum);
				VDOUBLE_SEC_CATEGORY.add(sec_category_sum);
				VDOUBLE_STATUS.add(getStatusName(status_sum));
				VDOUBLE_SEC_TYPE.add(security_type_sum);
				VDOUBLE_CONFIRM_BANK_CD.add(confirm_bankCd_sum);

				if(currency_sum.equals("1"))
				{
					VDOUBLE_VALUE.add(value_sum);
					VDOUBLE_VALUE_USD.add("");
				}
				else 
				{
					VDOUBLE_VALUE_USD.add(value_sum);
					VDOUBLE_VALUE.add("");
				}
			}
			rset4.close();
			stmt4.close();

			//For Incoming
			double inLC_Conf_amt_USD = 0; double inLC_Conf_amt = 0; int inLC_Conf_count = 0; 
			double inLC_Can_amt_USD = 0; double inLC_Can_amt = 0; int inLC_Can_count = 0; 
			double inLC_Adv_amt_USD= 0; double inLC_Adv_amt = 0; int inLC_Adv_count = 0;

			double inBG_Conf_amt_USD = 0; double inBG_Conf_amt = 0; int inBG_Conf_count = 0; 
			double inBG_Can_amt_USD = 0; double inBG_Can_amt = 0; int inBG_Can_count = 0; 
			double inBG_Adv_amt_USD= 0; double inBG_Adv_amt = 0; int inBG_Adv_count = 0;

			double inPCG_Conf_amt_USD = 0; double inPCG_Conf_amt = 0; int inPCG_Conf_count = 0; 
			double inPCG_Can_amt_USD = 0; double inPCG_Can_amt = 0; int inPCG_Can_count = 0; 
			double inPCG_Adv_amt_USD= 0; double inPCG_Adv_amt = 0; int inPCG_Adv_count = 0;

			//For Out Going
			double outLC_Conf_amt_USD = 0; double outLC_Conf_amt = 0; int outLC_Conf_count = 0; 
			double outLC_Can_amt_USD = 0; double outLC_Can_amt = 0; int outLC_Can_count = 0; 
			double outLC_Adv_amt_USD= 0; double outLC_Adv_amt = 0; int outLC_Adv_count = 0;

			double outBG_Conf_amt_USD = 0; double outBG_Conf_amt = 0; int outBG_Conf_count = 0; 
			double outBG_Can_amt_USD = 0; double outBG_Can_amt = 0; int outBG_Can_count = 0; 
			double outBG_Adv_amt_USD= 0; double outBG_Adv_amt = 0; int outBG_Adv_count = 0;

			double outPCG_Conf_amt_USD = 0; double outPCG_Conf_amt = 0; int outPCG_Conf_count = 0; 
			double outPCG_Can_amt_USD = 0; double outPCG_Can_amt = 0; int outPCG_Can_count = 0; 
			double outPCG_Adv_amt_USD= 0; double outPCG_Adv_amt = 0; int outPCG_Adv_count = 0;

			double INR_Value =0.00;
			double USD_Value =0.00;

			for(int i=0; i < VDOUBLE_COUNTERPARTY_CD.size(); i++)
			{
				String sec_category = ""+VDOUBLE_SEC_CATEGORY.elementAt(i);
				String status = ""+VDOUBLE_STATUS.elementAt(i);
				String security_type = ""+VDOUBLE_SEC_TYPE.elementAt(i);
				String confirm_bankCd = ""+VDOUBLE_CONFIRM_BANK_CD.elementAt(i);

				if(!VDOUBLE_VALUE.elementAt(i).equals("")) 
				{
					INR_Value = Double.parseDouble(""+VDOUBLE_VALUE.elementAt(i));
					USD_Value = Double.parseDouble(""+VDOUBLE_VALUE.elementAt(i))/Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i));
				}
				else if(!VDOUBLE_VALUE_USD.elementAt(i).equals(""))
				{
					INR_Value = Double.parseDouble(""+VDOUBLE_VALUE_USD.elementAt(i))*Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i));
					USD_Value = Double.parseDouble(""+VDOUBLE_VALUE_USD.elementAt(i));
				}
				else if(VDOUBLE_VALUE.elementAt(i).equals("") && VDOUBLE_VALUE_USD.elementAt(i).equals(""))
				{
					INR_Value = 0.00;
					USD_Value = 0.00;
				}

				if(sec_category.equalsIgnoreCase("R"))
				{
					if(status.equalsIgnoreCase("In Order") && !confirm_bankCd.equalsIgnoreCase(""))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{
							inLC_Conf_count += 1;
							inLC_Conf_amt += INR_Value;
							inLC_Conf_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							inBG_Conf_count += 1;
							inBG_Conf_amt += INR_Value;
							inBG_Conf_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							inPCG_Conf_count += 1;
							inPCG_Conf_amt += INR_Value;
							inPCG_Conf_amt_USD += USD_Value;
						}
					}
					if(status.equalsIgnoreCase("Cancelled"))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{
							inLC_Can_count += 1;
							inLC_Can_amt += INR_Value;
							inLC_Can_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							inBG_Can_count += 1;
							inBG_Can_amt += INR_Value;
							inBG_Can_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							inPCG_Can_count += 1;
							inPCG_Can_amt += INR_Value;
							inPCG_Can_amt_USD += USD_Value;
						}
					}
					if(status.equalsIgnoreCase("In Order"))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{

							inLC_Adv_count += 1;
							inLC_Adv_amt += INR_Value;
							inLC_Adv_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							inBG_Adv_count += 1;
							inBG_Adv_amt += INR_Value;
							inBG_Adv_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							inPCG_Adv_count += 1;
							inPCG_Adv_amt += INR_Value;
							inPCG_Adv_amt_USD += USD_Value;
						}
					}
				}
				else if(sec_category.equalsIgnoreCase("I"))
				{
					if(status.equalsIgnoreCase("In Order") && !confirm_bankCd.equalsIgnoreCase(""))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{

							outLC_Conf_count += 1;
							outLC_Conf_amt += INR_Value;
							outLC_Conf_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							outBG_Conf_count += 1;
							outBG_Conf_amt += INR_Value;
							outBG_Conf_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							outPCG_Conf_count += 1;
							outPCG_Conf_amt += INR_Value;
							outPCG_Conf_amt_USD += USD_Value;
						}
					}
					if(status.equalsIgnoreCase("Cancelled"))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{

							outLC_Can_count += 1;
							outLC_Can_amt += INR_Value;
							outLC_Can_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							outBG_Can_count += 1;
							outBG_Can_amt += INR_Value;
							outBG_Can_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							outPCG_Can_count += 1;
							outPCG_Can_amt += INR_Value;
							outPCG_Can_amt_USD += USD_Value;
						}
					}
					if(status.equalsIgnoreCase("In Order"))
					{
						if(security_type.equalsIgnoreCase("LC"))
						{

							outLC_Adv_count += 1;
							outLC_Adv_amt += INR_Value;
							outLC_Adv_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("BG"))
						{
							outBG_Adv_count += 1;
							outBG_Adv_amt += INR_Value;
							outBG_Adv_amt_USD += USD_Value;
						}
						else if(security_type.equalsIgnoreCase("PCG"))
						{
							outPCG_Adv_count += 1;
							outPCG_Adv_amt += INR_Value;
							outPCG_Adv_amt_USD += USD_Value;
						}
					}
				}
			}
			inLCConfCount = String.valueOf(inLC_Conf_count);
			inLCConfAmt = nf2.format(inLC_Conf_amt);
			inLCConfAmtUsd = nf2.format(inLC_Conf_amt_USD);

			inLCCanCount = String.valueOf(inLC_Can_count);
			inLCCanAmt = nf2.format(inLC_Can_amt);
			inLCCanAmtUsd = nf2.format(inLC_Can_amt_USD);

			inLCAdvCount = String.valueOf(inLC_Adv_count);
			inLCAdvAmt = nf2.format(inLC_Adv_amt);
			inLCAdvAmtUsd = nf2.format(inLC_Adv_amt_USD);

			inBGConfCount = String.valueOf(inBG_Conf_count);
			inBGConfAmt = nf2.format(inBG_Conf_amt);
			inBGConfAmtUsd = nf2.format(inBG_Conf_amt_USD);

			inBGCanCount = String.valueOf(inBG_Can_count);
			inBGCanAmt = nf2.format(inBG_Can_amt);
			inBGCanAmtUsd = nf2.format(inBG_Can_amt_USD);

			inBGAdvCount = String.valueOf(inBG_Adv_count);
			inBGAdvAmt = nf2.format(inBG_Adv_amt);
			inBGAdvAmtUsd = nf2.format(inBG_Adv_amt_USD);

			inPCGConfCount = String.valueOf(inPCG_Conf_count);
			inPCGConfAmt = nf2.format(inPCG_Conf_amt);
			inPCGConfAmtUsd = nf2.format(inPCG_Conf_amt_USD);

			inPCGCanCount = String.valueOf(inPCG_Can_count);
			inPCGCanAmt = nf2.format(inPCG_Can_amt);
			inPCGCanAmtUsd = nf2.format(inPCG_Can_amt_USD);

			inPCGAdvCount = String.valueOf(inPCG_Adv_count);
			inPCGAdvAmt = nf2.format(inPCG_Adv_amt);
			inPCGAdvAmtUsd = nf2.format(inPCG_Adv_amt_USD);

			outLCConfCount = String.valueOf(outLC_Conf_count);
			outLCConfAmt = nf2.format(outLC_Conf_amt);
			outLCConfAmtUsd = nf2.format(outLC_Conf_amt_USD);

			outLCCanCount = String.valueOf(outLC_Can_count);
			outLCCanAmt = nf2.format(outLC_Can_amt);
			outLCCanAmtUsd = nf2.format(outLC_Can_amt_USD);

			outLCAdvCount = String.valueOf(outLC_Adv_count);
			outLCAdvAmt = nf2.format(outLC_Adv_amt);
			outLCAdvAmtUsd = nf2.format(outLC_Adv_amt_USD);

			outBGConfCount = String.valueOf(outBG_Conf_count);
			outBGConfAmt = nf2.format(outBG_Conf_amt);
			outBGConfAmtUsd = nf2.format(outBG_Conf_amt_USD);

			outBGCanCount = String.valueOf(outBG_Can_count);
			outBGCanAmt = nf2.format(outBG_Can_amt);
			outBGCanAmtUsd = nf2.format(outBG_Can_amt_USD);

			outBGAdvCount = String.valueOf(outBG_Adv_count);
			outBGAdvAmt = nf2.format(outBG_Adv_amt);
			outBGAdvAmtUsd = nf2.format(outBG_Adv_amt_USD);

			outPCGConfCount = String.valueOf(outPCG_Conf_count);
			outPCGConfAmt = nf2.format(outPCG_Conf_amt);
			outPCGConfAmtUsd = nf2.format(outPCG_Conf_amt_USD);

			outPCGCanCount = String.valueOf(outPCG_Can_count);
			outPCGCanAmt = nf2.format(outPCG_Can_amt);
			outPCGCanAmtUsd = nf2.format(outPCG_Can_amt_USD);

			outPCGAdvCount = String.valueOf(outPCG_Adv_count);
			outPCGAdvAmt = nf2.format(outPCG_Adv_amt);
			outPCGAdvAmtUsd = nf2.format(outPCG_Adv_amt_USD);

			String split_sysdate[] = start_dt.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String split_yesdate[] = end_dt.split("/");
			String splited_yesdate = split_yesdate[2]+split_yesdate[1]+split_yesdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-KPI_Report_"+splited_sysdate+"_"+splited_yesdate+".xls";
			}
			else
			{
				filename = daily_file_path+"KPI_Report_"+splited_sysdate+"_"+splited_yesdate+".xls";
			}

			String subject=comp_abbr+" EMS "+context+": Credit Risk KPI Report dated from "+start_dt+" to "+end_dt;

			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("KPI_Report"+" ("+splited_start_date+"-"+splited_end_date+" )");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk KPI Report dated "+start_dt+" to "+end_dt+" attached.";

			HSSFRow rowhead = sheet.createRow((short)0); 
			rowhead.createCell((short) 0).setCellValue("INCOMING SECURITY SUMMARY");
			rowhead.createCell((short) 11).setCellValue("OUTGOING SECURITY SUMMARY"); 

			HSSFRow rowhead_1 = sheet.createRow((short)1);
			rowhead_1.createCell((short) 1).setCellValue("ADVISED");  
			rowhead_1.createCell((short) 4).setCellValue("CONFIRMED");
			rowhead_1.createCell((short) 7).setCellValue("CANCELLED");

			rowhead_1.createCell((short) 12).setCellValue("ADVISED");  
			rowhead_1.createCell((short) 15).setCellValue("CONFIRMED");
			rowhead_1.createCell((short) 18).setCellValue("CANCELLED");

			HSSFRow rowhead_2 = sheet.createRow((short)2);
			rowhead_2.createCell((short) 1).setCellValue("COUNT");  
			rowhead_2.createCell((short) 2).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 3).setCellValue("VALUE (USD)");
			rowhead_2.createCell((short) 4).setCellValue("COUNT");  
			rowhead_2.createCell((short) 5).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 6).setCellValue("VALUE (USD)");
			rowhead_2.createCell((short) 7).setCellValue("COUNT");  
			rowhead_2.createCell((short) 8).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 9).setCellValue("VALUE (USD)");

			rowhead_2.createCell((short) 12).setCellValue("COUNT");  
			rowhead_2.createCell((short) 13).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 14).setCellValue("VALUE (USD)");
			rowhead_2.createCell((short) 15).setCellValue("COUNT");  
			rowhead_2.createCell((short) 16).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 17).setCellValue("VALUE (USD)");
			rowhead_2.createCell((short) 18).setCellValue("COUNT");  
			rowhead_2.createCell((short) 19).setCellValue("VALUE (INR)");
			rowhead_2.createCell((short) 20).setCellValue("VALUE (USD)");

			HSSFRow rowhead_3 = sheet.createRow((short)3);
			rowhead_3.createCell((short) 0).setCellValue("LC");
			rowhead_3.createCell((short) 1).setCellValue(inLCAdvCount);
			rowhead_3.createCell((short) 2).setCellValue(inLCAdvAmt);
			rowhead_3.createCell((short) 3).setCellValue(inLCAdvAmtUsd);
			rowhead_3.createCell((short) 4).setCellValue(inLCConfCount);
			rowhead_3.createCell((short) 5).setCellValue(inLCConfAmt);
			rowhead_3.createCell((short) 6).setCellValue(inLCConfAmtUsd);
			rowhead_3.createCell((short) 7).setCellValue(inLCCanCount);
			rowhead_3.createCell((short) 8).setCellValue(inLCCanAmt);
			rowhead_3.createCell((short) 9).setCellValue(inLCCanAmtUsd);

			rowhead_3.createCell((short) 11).setCellValue("LC");
			rowhead_3.createCell((short) 12).setCellValue(outLCAdvCount);
			rowhead_3.createCell((short) 13).setCellValue(outLCAdvAmt);
			rowhead_3.createCell((short) 14).setCellValue(outLCAdvAmtUsd);
			rowhead_3.createCell((short) 15).setCellValue(outLCConfCount);
			rowhead_3.createCell((short) 16).setCellValue(outLCConfAmt);
			rowhead_3.createCell((short) 17).setCellValue(outLCConfAmtUsd);
			rowhead_3.createCell((short) 18).setCellValue(outLCCanCount);
			rowhead_3.createCell((short) 19).setCellValue(outLCCanAmt);
			rowhead_3.createCell((short) 20).setCellValue(outLCCanAmtUsd);

			HSSFRow rowhead_4 = sheet.createRow((short)4);
			rowhead_4.createCell((short) 0).setCellValue("BG");
			rowhead_4.createCell((short) 1).setCellValue(inBGAdvCount);
			rowhead_4.createCell((short) 2).setCellValue(inBGAdvAmt);
			rowhead_4.createCell((short) 3).setCellValue(inBGAdvAmtUsd);
			rowhead_4.createCell((short) 4).setCellValue(inBGConfCount);
			rowhead_4.createCell((short) 5).setCellValue(inBGConfAmt);
			rowhead_4.createCell((short) 6).setCellValue(inBGConfAmtUsd);
			rowhead_4.createCell((short) 7).setCellValue(inBGCanCount);
			rowhead_4.createCell((short) 8).setCellValue(inBGCanAmt);
			rowhead_4.createCell((short) 9).setCellValue(inBGCanAmtUsd);

			rowhead_4.createCell((short) 11).setCellValue("BG");
			rowhead_4.createCell((short) 12).setCellValue(outBGAdvCount);
			rowhead_4.createCell((short) 13).setCellValue(outBGAdvAmt);
			rowhead_4.createCell((short) 14).setCellValue(outBGAdvAmtUsd);
			rowhead_4.createCell((short) 15).setCellValue(outBGConfCount);
			rowhead_4.createCell((short) 16).setCellValue(outBGConfAmt);
			rowhead_4.createCell((short) 17).setCellValue(outBGConfAmtUsd);
			rowhead_4.createCell((short) 18).setCellValue(outBGCanCount);
			rowhead_4.createCell((short) 19).setCellValue(outBGCanAmt);
			rowhead_4.createCell((short) 20).setCellValue(outBGCanAmtUsd);

			HSSFRow rowhead_5 = sheet.createRow((short) 5);
			rowhead_5.createCell((short) 0).setCellValue("PCG");
			rowhead_5.createCell((short) 1).setCellValue(inPCGAdvCount);
			rowhead_5.createCell((short) 2).setCellValue(inPCGAdvAmt);
			rowhead_5.createCell((short) 3).setCellValue(inPCGAdvAmtUsd);
			rowhead_5.createCell((short) 4).setCellValue(inPCGConfCount);
			rowhead_5.createCell((short) 5).setCellValue(inPCGConfAmt);
			rowhead_5.createCell((short) 6).setCellValue(inPCGConfAmtUsd);
			rowhead_5.createCell((short) 7).setCellValue(inPCGCanCount);
			rowhead_5.createCell((short) 8).setCellValue(inPCGCanAmt);
			rowhead_5.createCell((short) 9).setCellValue(inPCGCanAmtUsd);

			rowhead_5.createCell((short) 11).setCellValue("PCG");
			rowhead_5.createCell((short) 12).setCellValue(outPCGAdvCount);
			rowhead_5.createCell((short) 13).setCellValue(outPCGAdvAmt);
			rowhead_5.createCell((short) 14).setCellValue(outPCGAdvAmtUsd);
			rowhead_5.createCell((short) 15).setCellValue(outPCGConfCount);
			rowhead_5.createCell((short) 16).setCellValue(outPCGConfAmt);
			rowhead_5.createCell((short) 17).setCellValue(outPCGConfAmtUsd);
			rowhead_5.createCell((short) 18).setCellValue(outPCGCanCount);
			rowhead_5.createCell((short) 19).setCellValue(outPCGCanAmt);
			rowhead_5.createCell((short) 20).setCellValue(outPCGCanAmtUsd);

			HSSFRow rowhead_6 = sheet.createRow((short)7); 
			rowhead_6.createCell((short) 0).setCellValue("Sr#");
			rowhead_6.createCell((short) 1).setCellValue("Legal Entity"); 
			rowhead_6.createCell((short) 2).setCellValue("Counterparty");  
			rowhead_6.createCell((short) 3).setCellValue("Security Type");
			rowhead_6.createCell((short) 4).setCellValue("Incoming/ Outgoing");
			rowhead_6.createCell((short) 5).setCellValue("Contract type"); 
			rowhead_6.createCell((short) 6).setCellValue("Deal Ref#"); 
			rowhead_6.createCell((short) 7).setCellValue("Security Ref#"); 
			rowhead_6.createCell((short) 8).setCellValue("Status"); 
			rowhead_6.createCell((short) 9).setCellValue("Security Value(INR)");
			rowhead_6.createCell((short) 10).setCellValue("Security Value(USD)");
			rowhead_6.createCell((short) 11).setCellValue("Received Date");
			rowhead_6.createCell((short) 12).setCellValue("Issued Date");  
			rowhead_6.createCell((short) 13).setCellValue("Expire Date");
			rowhead_6.createCell((short) 14).setCellValue("Cancellation Date");
			rowhead_6.createCell((short) 15).setCellValue("Issuing Bank name");
			rowhead_6.createCell((short) 16).setCellValue("Issuing Bank's Reference"); 
			rowhead_6.createCell((short) 17).setCellValue("Advising Bank Name"); 
			rowhead_6.createCell((short) 18).setCellValue("Advising Bank's Reference"); 
			rowhead_6.createCell((short) 19).setCellValue("Confirming Bank Name");
			rowhead_6.createCell((short) 20).setCellValue("Confirming Bank's Reference");
			rowhead_6.createCell((short) 21).setCellValue("Remarks");

			int k=0;
			for(int i=0; i<VCOUNTERPARTY_NM.size(); i++)
			{

				String value_inr = "";
				if(!VVALUE.elementAt(i).equals(""))
				{
					value_inr = nf2.format(Double.parseDouble(""+VVALUE.elementAt(i)));
				}
				else
				{
					value_inr= nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i))*Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)));
				}

				String value_usd = "";
				if(!VVALUE_USD.elementAt(i).equals(""))
				{
					value_usd = nf2.format(Double.parseDouble(""+VVALUE_USD.elementAt(i)));
				}
				else
				{
					value_usd = nf2.format(Double.parseDouble(""+VVALUE.elementAt(i))/Double.parseDouble(""+VEXCHANGE_RATE.elementAt(i)));
				}

				k++;
				HSSFRow row = sheet.createRow((short)i+8);  
				row.createCell((short) 0).setCellValue(k);
				row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY.elementAt(i));  
				row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
				row.createCell((short) 3).setCellValue(""+VSEC_TYPE.elementAt(i));  
				row.createCell((short) 4).setCellValue(""+VSEC_CATEGORY.elementAt(i));
				row.createCell((short) 5).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(i));
				row.createCell((short) 6).setCellValue(""+VDEAL_NO.elementAt(i));
				row.createCell((short) 7).setCellValue(""+VSEC_REF_NO.elementAt(i));  
				row.createCell((short) 8).setCellValue(""+VSTATUS.elementAt(i));
				row.createCell((short) 9).setCellValue(""+value_inr);
				row.createCell((short) 10).setCellValue(""+value_usd);
				row.createCell((short) 11).setCellValue(""+VRECEIVED_DATE.elementAt(i));
				row.createCell((short) 12).setCellValue(""+VISSUE_DT.elementAt(i));
				row.createCell((short) 13).setCellValue(""+VEXPIRE_DT.elementAt(i));
				row.createCell((short) 14).setCellValue(""+VCANCEL_DT.elementAt(i));
				row.createCell((short) 15).setCellValue(""+VISS_BANK_NM.elementAt(i));  
				row.createCell((short) 16).setCellValue(""+VISS_BANK_REF.elementAt(i));
				row.createCell((short) 17).setCellValue(""+VADV_BANK_NM.elementAt(i));
				row.createCell((short) 18).setCellValue(""+VADV_BANK_REF.elementAt(i));  
				row.createCell((short) 19).setCellValue(""+VCONFIRM_BANK_NM.elementAt(i));
				row.createCell((short) 20).setCellValue(""+VCONFIRM_BANK_REF.elementAt(i));
				row.createCell((short) 21).setCellValue(""+VREMARK.elementAt(i));

			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 21; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"KPI Report","Risk Mgmt","Monthly","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"KPI Report","Risk Mgmt","Monthly","Auto");
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("KPI Report Mail Done(Monthly).....");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getBankLimitExposure() 
	{
		String function_nm="getBankLimitExposure()";
		String mailBody = "";
		try
		{
			queryString6 = "SELECT COUNTERPARTY_CD, BANK_CD, GX "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "BANK_CD != ? ";
			stmt6 = conn.prepareStatement(queryString6);
			//stmt6.setString(1, comp_cd);
			stmt6.setString(1, "0");
			rset6 = stmt6.executeQuery();
			while(rset6.next())
			{
				String counterparty_cd = rset6.getString(1)==null?"":rset6.getString(1);
				String bank_cd = rset6.getString(2)==null?"":rset6.getString(2);
				String bank_abbr = utilBean.getBankABBR(conn,bank_cd);
				String bank_name = utilBean.getBankName(conn,bank_cd);
				String clearance = rset6.getString(3)==null?"":rset6.getString(3);

				VMST_BANK_CD.add(bank_cd);
				VMST_BANK_ABBR.add(bank_abbr);
				VMST_BANK_NM.add(bank_name);

				String agmt ="";
				String agmt_rev = "";
				String cont ="";
				String cont_rev = "";
				String cont_type = "";
				String entityCd = "";

				String exchng_rate_cd = "";

				double exchange_rate = 0.00;
				double available = 0;
				double total_limit = 0;
				double unsecured = 0;
				double temporary =0;
				double adjust_usage = 0;
				double net_usage = 0;
				double usage = 0;
				double used = 0;

				String rate_nm="Shell Treasury Rate";

				queryString1="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, rate_nm.toUpperCase());
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					exchng_rate_cd = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();

				queryString1="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, exchng_rate_cd);
				stmt1.setString(2, "Y");
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					exchange_rate = rset1.getDouble(1);
				}
				rset1.close();
				stmt1.close();

				String curr = "";
				queryString1 = "SELECT AMT,AMT_UNIT,LIMIT_ID,LIMIT_TYPE,SEQ_NO,ACTION_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(EXP_DT,'DD/MM/YYYY'),TO_CHAR(REVIEW_DT,'DD/MM/YYYY'),CATEGORY,COUNTERPARTY_CD,BANK_CD,REMARKS,TO_CHAR(INACTIVATION_DT,'DD/MM/YYYY'),AMT_UNIT "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "BANK_CD=? AND GX=? AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT>=TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) "
						+ "OR (EXP_DT>=TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) AND ((INACTIVATION_DT-1>=TO_DATE(?,'DD/MM/YYYY'))))";
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, bank_cd);
				stmt1.setString(2, clearance);
				stmt1.setString(3, today_date);
				stmt1.setString(4, today_date);
				stmt1.setString(5, today_date);
				stmt1.setString(6, today_date);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String action_type = rset1.getString(6)==null?"":rset1.getString(6);
					String limit_type = rset1.getString(4)==null?"":rset1.getString(4);
					curr = rset1.getString(15)==null?"":rset1.getString(15);
					double amt = Double.parseDouble(rset1.getString(1)==null?"0":rset1.getString(1));

					if(!limit_type.equals("Unsecured") && action_type.equals("Adjust Usage"))
					{
						adjust_usage = adjust_usage + amt;
					}
					if(limit_type.equals("Unsecured") && action_type.equals("Adjust Limit"))
					{
						unsecured = unsecured + amt;
					}
					if(limit_type.equals("Temporary") && action_type.equals("Adjust Limit"))
					{
						temporary = temporary + amt;
					}
					if(action_type.equals("Adjust Limit"))
					{
						total_limit = total_limit + amt;
					}
				}
				rset1.close();
				stmt1.close();

				if(curr.equals("1")) 
				{
					available = total_limit + adjust_usage - usage;
					VBANK_LIMIT.add(nf.format(available));
					VBANK_LIMIT_USD.add(nf.format(available/exchange_rate));
				}
				else
				{
					available = (total_limit + adjust_usage - usage)*exchange_rate;
					VBANK_LIMIT.add(nf.format(available));
					VBANK_LIMIT_USD.add(nf.format(available/exchange_rate));
				}

				double Limit_USD = 0;
				double exchgRate = 0; 
				double expo_inr = 0; 
				double expo_inr_usd = 0;
				double temp_available = available; 
				double available_usd = 0;

				int sec_count=0;
				String clearance_gx ="";

				queryString4 = "SELECT COUNTERPARTY_CD,VALUE,TO_CHAR(ISSUE_DT,'DD/MM/YYYY'),SEC_REF_NO,CURRENCY,GX "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "((ISS_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD = ?) "
						+ "OR (ISS_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD IS NULL AND ISS_BANK_CD = ?)) "
						+ "AND SEC_TYPE IN (?,?) AND ISSUE_DT <= TO_DATE(?,'DD/MM/YYYY') AND (EXPIRE_DT >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND (TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) "
						+ "AND SEC_CATEGORY=? "
						+ "AND ((STATUS IN (?,?,?) AND APRV_DT IS NOT NULL) OR (STATUS = ? AND APRV_DT IS NULL))";
				stmt4 = conn.prepareStatement(queryString4);
				//stmt4.setString(1, comp_cd);
				stmt4.setString(1, bank_cd);
				stmt4.setString(2, bank_cd);
				stmt4.setString(3, "LC");
				stmt4.setString(4, "BG");
				stmt4.setString(5, today_date);
				stmt4.setString(6, today_date);
				stmt4.setString(7, today_date);
				stmt4.setString(8, "R");
				stmt4.setString(9, "O");
				stmt4.setString(10, "C");
				stmt4.setString(11, "R");
				stmt4.setString(12, "D");
				rset4 = stmt4.executeQuery();
				while(rset4.next())
				{
					sec_count+=1;
					String contCd = rset4.getString(1)==null?"":rset4.getString(1);
					String value = rset4.getString(2)==null?"":rset4.getString(2);
					String issuing_dt = rset4.getString(3)==null?"":rset4.getString(3);
					double AvailableExchgRate = exchange_rate;

					String currency = rset4.getString(5)==null?"":rset4.getString(5);
					double amt = rset4.getDouble(2);

					clearance_gx = rset4.getString(6)==null?"":rset4.getString(6);

					double USDtoINR = 0; 
					double INRtoUSD = 0;
					String contType="";

					if(currency.equals("2"))
					{
						USDtoINR = amt * AvailableExchgRate;
						expo_inr += USDtoINR;
						expo_inr_usd += amt;
						temp_available = temp_available - USDtoINR;
					}
					else
					{
						expo_inr += rset4.getDouble(2);
						temp_available = temp_available - amt;
						if(AvailableExchgRate > 0)
						{
							expo_inr_usd += amt / AvailableExchgRate;
						}
						else
						{
							if(exchgRate > 0)
							{
								expo_inr_usd += amt / exchgRate;
							}
						}
					}

					if(AvailableExchgRate > 0)
					{
						available_usd = temp_available / AvailableExchgRate;
					}
					else
					{
						if(exchgRate > 0)
						{
							available_usd = temp_available / exchgRate;
						}
					}

					double availability = available - expo_inr;
					if(sec_count == 0) 
					{
						available_usd = Limit_USD;
					}
				}
				rset4.close();
				stmt4.close();

				double availability = available - expo_inr;
				if(sec_count == 0) 
				{
					available_usd = Limit_USD;
				}

				VEXPOSURE_INR.add(nf.format(expo_inr));
				VEXPOSURE_USD.add(nf.format(expo_inr_usd));
				VAVAILABILITY_INR.add(nf.format(availability));
				VAVAILABILITY_USD.add(nf.format(available_usd));

				double bank_expo_inr = 0; 
				int index=0;
				queryString2 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
						+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
						+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
						+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,GX,COMPANY_CD "
						+ "FROM FMS_SECURITY_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "((ISS_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD = ?) "
						+ "OR (ISS_BANK_CD IS NOT NULL AND CONFIRM_BANK_CD IS NULL AND ISS_BANK_CD = ?)) "
						+ "AND SEC_TYPE IN (?,?) AND ISSUE_DT <= TO_DATE(?,'DD/MM/YYYY') AND (EXPIRE_DT >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND (TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) "
						+ "AND SEC_CATEGORY=? "
						+ "AND ((STATUS IN (?,?,?) AND APRV_DT IS NOT NULL) OR (STATUS = ? AND APRV_DT IS NULL))";
				stmt2 = conn.prepareStatement(queryString2);
				//stmt2.setString(1, comp_cd);
				stmt2.setString(1, bank_cd);
				stmt2.setString(2, bank_cd);
				stmt2.setString(3, "LC");
				stmt2.setString(4, "BG");
				stmt2.setString(5, today_date);
				stmt2.setString(6, today_date);
				stmt2.setString(7, today_date);
				stmt2.setString(8, "R");
				stmt2.setString(9, "O");
				stmt2.setString(10, "C");
				stmt2.setString(11, "R");
				stmt2.setString(12, "D");
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					index++;
					String counterPartyCd = rset2.getString(1)==null?"":rset2.getString(1);
					String sec_category = rset2.getString(2)==null?"":rset2.getString(2);
					String sec_type = rset2.getString(3)==null?"":rset2.getString(3);
					String sec_ref_no =  rset2.getString(4)==null?"":rset2.getString(4);
					String status = rset2.getString(5)==null?"":rset2.getString(5);
					String currency = rset2.getString(6)==null?"":rset2.getString(6);
					String value = rset2.getString(7)==null?"":rset2.getString(7);
					String received_date = rset2.getString(8)==null?"":rset2.getString(8);
					String seq_no = rset2.getString(9)==null?"":rset2.getString(9);
					String deal_type = rset2.getString(10)==null?"":rset2.getString(10);
					String iss_bank_cd = rset2.getString(12)==null?"":rset2.getString(12);
					String iss_bank_ref = rset2.getString(13)==null?"":rset2.getString(13);
					String adv_bank_cd = rset2.getString(14)==null?"":rset2.getString(14);
					String adv_bank_ref = rset2.getString(15)==null?"":rset2.getString(15);
					String confirm_bank_cd = rset2.getString(16)==null?"":rset2.getString(16);
					String confirm_bank_ref = rset2.getString(17)==null?"":rset2.getString(17);
					String issue_dt = rset2.getString(18)==null?"":rset2.getString(18);
					String expire_dt = rset2.getString(19)==null?"":rset2.getString(19);
					String remarks = rset2.getString(22)==null?"":rset2.getString(22);
					String cancel_date = rset2.getString(25)==null?"":rset2.getString(25);
					String seq_rev_no = rset2.getString(27)==null?"":rset2.getString(27);
					String gx = rset2.getString(30)==null?"":rset2.getString(30);
					String company_cd = rset2.getString(31)==null?"":rset2.getString(31);
					String counterparty_nm = "";
					double amt=Double.parseDouble(value);

					if(currency.equals("1")) 
					{
						bank_expo_inr+=amt;
					}
					else 
					{
						bank_expo_inr+=amt*exchange_rate;
					}

					if(gx.equals("I"))
					{
						VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
					}
					else
					{
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
					}
					String iss_bank_nm = ""+utilBean.getBankName(conn,iss_bank_cd);
					String adv_bank_nm = ""+utilBean.getBankName(conn,adv_bank_cd);
					String confirm_bank_nm = ""+utilBean.getBankName(conn,confirm_bank_cd);

					String ref_no = company_cd+"-"+sec_ref_no;
					VSEC_CATEGORY.add(sec_category);
					VSEC_TYPE.add(sec_type);
					VSEC_REF_NO.add(ref_no);
					if(status.equals("P"))
					{
						VSTATUS.add("Pending");
					}
					else if(status.equals("O"))
					{
						VSTATUS.add("In Order");
					}
					else if(status.equals("C"))
					{
						VSTATUS.add("Cancelled");
					}
					else if(status.equals("A"))
					{
						VSTATUS.add("Pending For Amendment");
					}
					else if(status.equals("R"))
					{
						VSTATUS.add("Restated");
					}
					else if(status.equals("D"))
					{
						VSTATUS.add("Dummy");
					}
					VRECEIVED_DATE.add(received_date);
					VDEAL_TYPE.add(deal_type);
					VISS_BANK_CD.add(iss_bank_cd);
					VISS_BANK_NM.add(iss_bank_nm);
					VISS_BANK_REF.add(iss_bank_ref);
					VADV_BANK_CD.add(adv_bank_cd);
					VADV_BANK_NM.add(adv_bank_nm);
					VADV_BANK_REF.add(adv_bank_ref);
					VCONFIRM_BANK_CD.add(confirm_bank_cd);
					VCONFIRM_BANK_NM.add(confirm_bank_nm);
					VCONFIRM_BANK_REF.add(confirm_bank_ref);
					VISSUE_DT.add(issue_dt);
					VEXPIRE_DT.add(expire_dt);
					VREMARK.add(remarks);
					VCANCEL_DT.add(cancel_date);

					String sel_deal_dtl="";
					String deal_dtl = "";
					String dealNo = "";
					String deal_No = "";
					String deal_No_dtl="";
					String disp_cont_type="";

					queryString3 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD,GX "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEC_REF_NO=?  AND SEQ_REV_NO=? ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, company_cd);
					stmt3.setString(2, counterPartyCd);
					stmt3.setString(3, seq_no);
					stmt3.setString(4, sec_ref_no);
					stmt3.setString(5, seq_rev_no);
					rset3 = stmt3.executeQuery();
					while(rset3.next())
					{
						agmt = rset3.getString(1)==null?"":rset3.getString(1);
						agmt_rev = rset3.getString(2)==null?"":rset3.getString(2);
						cont = rset3.getString(3)==null?"":rset3.getString(3);
						cont_rev = rset3.getString(4)==null?"":rset3.getString(4);
						cont_type = rset3.getString(5)==null?"":rset3.getString(5);
						counterparty_cd = rset3.getString(6)==null?"":rset3.getString(6);
						entityCd = rset3.getString(7)==null?"":rset3.getString(7);
						String gx_sec = rset3.getString(8)==null?"":rset3.getString(8);

						if(gx_sec.equals("I"))
						{
							String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;

							if(!sel_deal_dtl.equals(""))
							{
								deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
								sel_deal_dtl+="@@"+dealDtl;
								//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
								//dealNo+=utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								dealNo+=utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								sel_deal_dtl+=""+dealDtl;
								disp_cont_type+=utilBean.getContractTypeName(cont_type);
							}
						}
						else
						{
							String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;

							if(!sel_deal_dtl.equals(""))
							{
								deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
								sel_deal_dtl+="@@"+dealDtl;
								//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
								//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								sel_deal_dtl+=""+dealDtl;
								disp_cont_type+=utilBean.getContractTypeName(cont_type);
							}
						}
					}
					rset3.close();
					stmt3.close();

					queryString5 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENTITY_CD,0 "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
					queryString5 +=" UNION ";
					queryString5 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,0 "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
					queryString5 +=" UNION ";
					queryString5 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,0 "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
					queryString5 +=" UNION ";
					queryString5 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD, CARGO_NO "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
							+ "WHERE COMPANY_CD=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND EDQ_TO_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, company_cd);
					stmt5.setString(2, counterPartyCd);
					stmt5.setString(3, seq_no);
					stmt5.setString(4, sec_ref_no);
					stmt5.setString(5, seq_rev_no);
					stmt5.setString(6, company_cd);
					stmt5.setString(7, company_cd);
					stmt5.setString(8, company_cd);
					rset5 = stmt5.executeQuery();
					while(rset5.next())
					{
						String agmt_no = rset5.getString(1)==null?"":rset5.getString(1);
						String agmt_rev_no = rset5.getString(2)==null?"":rset5.getString(2);
						String cont_no = rset5.getString(3)==null?"":rset5.getString(3);
						String cont_rev_no = rset5.getString(4)==null?"":rset5.getString(4);
						cont_type = rset5.getString(5)==null?"":rset5.getString(5);
						String countpty_cd_no = rset5.getString(6)==null?"":rset5.getString(6);

						//deal_No=utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type);
						deal_No=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, "");
						deal_No_dtl=sec_ref_no+"/"+cont_type+"/"+agmt_no+"/"+agmt_rev_no+"/"+cont_no+"/"+cont_rev_no+"/"+countpty_cd_no;

					}
					rset5.close();
					stmt5.close();

					VDEAL_NO.add(dealNo);
					VDIS_CONTRACT_TYPE.add(disp_cont_type);
					VCO_ABBR.add(utilBean.getCompanyAbbr(conn,company_cd));
					if(currency.equals("1")) 
					{
						VBANK_EXPOSURE_INR.add(nf.format(amt));
						VBANK_EXPOSURE_USD.add(nf.format(amt/exchange_rate));

						availability = available - bank_expo_inr;
						if(sec_count == 0) 
						{
							available_usd = Limit_USD;
						}
						VBANK_AVAILABILITY_INR.add(nf.format(availability));
						VBANK_AVAILABILITY_USD.add(nf.format(availability/exchange_rate));
					}
					else 
					{
						VBANK_EXPOSURE_INR.add(nf.format(amt*exchange_rate));
						VBANK_EXPOSURE_USD.add(nf.format(amt));

						availability = (available - bank_expo_inr)/exchange_rate;
						if(sec_count == 0) 
						{
							available_usd = Limit_USD;
						}

						VBANK_AVAILABILITY_INR.add(nf.format(availability*exchange_rate));
						VBANK_AVAILABILITY_USD.add(nf.format(availability));
					}
				}
				rset2.close();
				stmt2.close();
				VINDEX.add(index);
			}
			rset6.close();
			stmt6.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Bank_Limit_Exposure_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Bank_Limit_Exposure_Report_"+splited_sysdate+".xls";
			}
			String subject=comp_abbr+" EMS "+context+": Credit Risk Bank Limit Exposure Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Bank Limit Exposure Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Bank Limit Exposure Report dated "+today_date+" attached.";

			HSSFRow rowhead = sheet.createRow((short)0); 
			rowhead.createCell((short) 0).setCellValue("Sr#"); 
			rowhead.createCell((short) 1).setCellValue("BANK NAME");  
			rowhead.createCell((short) 2).setCellValue("BANK LIMIT (INR)");  
			rowhead.createCell((short) 3).setCellValue("BANK LIMIT (USD)");
			rowhead.createCell((short) 4).setCellValue("LEGAL ENTITY");
			rowhead.createCell((short) 5).setCellValue("CUSTOMER NAME");
			rowhead.createCell((short) 6).setCellValue("Contract Type"); 
			rowhead.createCell((short) 7).setCellValue("DEAL NO#"); 
			rowhead.createCell((short) 8).setCellValue("SECURITY REF#"); 
			rowhead.createCell((short) 9).setCellValue("ISSUING BANK NAME");
			rowhead.createCell((short) 10).setCellValue("CONFIRMING BANK NAME");
			rowhead.createCell((short) 11).setCellValue("ISSUING DATE");
			rowhead.createCell((short) 12).setCellValue("EXPIRY DATE");
			rowhead.createCell((short) 13).setCellValue("EXPOSURE (INR)");
			rowhead.createCell((short) 14).setCellValue("EXPOSURE (USD)");
			rowhead.createCell((short) 15).setCellValue("AVAILABILITY (INR)");
			rowhead.createCell((short) 16).setCellValue("AVAILABILITY (USD)");
			rowhead.createCell((short) 17).setCellValue("STATUS");
			rowhead.createCell((short) 18).setCellValue("CANCELLATION /RESTATE DATE");


			int j=0; int rowspan=0; int temp=0;
			for(int i=0; i<VMST_BANK_NM.size(); i++)
			{
				int index = (int)VINDEX.elementAt(i);
				if(index == 0)
				{
					rowspan = 2;
				}
				else
				{
					rowspan = index+1;
				}
				temp = temp +1;

				HSSFRow row = sheet.createRow((short)temp);
				row.createCell((short) 0).setCellValue(i+1);
				row.createCell((short) 1).setCellValue(""+VMST_BANK_NM.elementAt(i));  
				row.createCell((short) 2).setCellValue(""+VBANK_LIMIT.elementAt(i));  
				row.createCell((short) 3).setCellValue(""+VBANK_LIMIT_USD.elementAt(i));
				row.createCell((short) 13).setCellValue(""+VEXPOSURE_INR.elementAt(i));  
				row.createCell((short) 14).setCellValue(""+VEXPOSURE_USD.elementAt(i));
				row.createCell((short) 15).setCellValue(""+VAVAILABILITY_INR.elementAt(i));
				row.createCell((short) 16).setCellValue(""+VAVAILABILITY_USD.elementAt(i));
				int k=-1;
				if(index != 0)
				{
					for(; j<VCOUNTERPARTY_NM.size(); j++)
					{
						k=k+1;
						if(k==index)
						{
							break;
						}
						else
						{
							temp = temp + 1;
							HSSFRow row1 = sheet.createRow((short)temp);  

							row1.createCell((short) 4).setCellValue(""+VCO_ABBR.elementAt(j));  
							row1.createCell((short) 5).setCellValue(""+VCOUNTERPARTY_NM.elementAt(j));  
							row1.createCell((short) 6).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(j));
							row1.createCell((short) 7).setCellValue(""+VDEAL_NO.elementAt(j));
							row1.createCell((short) 8).setCellValue(""+VSEC_REF_NO.elementAt(j));
							row1.createCell((short) 9).setCellValue(""+VISS_BANK_NM.elementAt(j));
							row1.createCell((short) 10).setCellValue(""+VCONFIRM_BANK_NM.elementAt(j));
							row1.createCell((short) 11).setCellValue(""+VISSUE_DT.elementAt(j));
							row1.createCell((short) 12).setCellValue(""+VEXPIRE_DT.elementAt(j));
							row1.createCell((short) 13).setCellValue(""+VBANK_EXPOSURE_INR.elementAt(j));  
							row1.createCell((short) 14).setCellValue(""+VBANK_EXPOSURE_USD.elementAt(j));
							row1.createCell((short) 15).setCellValue(""+VBANK_AVAILABILITY_INR.elementAt(j));
							row1.createCell((short) 16).setCellValue(""+VBANK_AVAILABILITY_USD.elementAt(j));
							row1.createCell((short) 17).setCellValue(""+VSTATUS.elementAt(j));
							row1.createCell((short) 18).setCellValue(""+VCANCEL_DT.elementAt(j));
						}
					}
				}
				else
				{
					temp = temp + 1;
					HSSFRow row1 = sheet.createRow((short)temp);  

					row1.createCell((short) 4).setCellValue("");
					row1.createCell((short) 5).setCellValue("");
					row1.createCell((short) 6).setCellValue("");  
					row1.createCell((short) 7).setCellValue("");
					row1.createCell((short) 8).setCellValue("");
					row1.createCell((short) 9).setCellValue("");
					row1.createCell((short) 10).setCellValue("");
					row1.createCell((short) 11).setCellValue("");
					row1.createCell((short) 12).setCellValue("");
					row1.createCell((short) 13).setCellValue("0.00");  
					row1.createCell((short) 14).setCellValue("0.0000");
					row1.createCell((short) 15).setCellValue("0.00");
					row1.createCell((short) 16).setCellValue("0.0000");
					row1.createCell((short) 17).setCellValue("");
					row1.createCell((short) 18).setCellValue("");
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 17; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Bank Limit and Exposure Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Bank Limit and Exposure Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Bank Limit and Exposure Report Mail Done(Daily).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getReceivableReport() 
	{
		String function_nm="getReceivableReport()";
		String mailBody = "";
		try
		{
			double exchgRate = 0; 
			String exchg_rate_cd = "";
			String exchg_rate_nm = "";

			queryString3 = "SELECT COUNTERPARTY_NM, COUNTERPARTY_ABBR, COLLECTION_CATEGORY,COUNTERPARTY_CATEGORY,BUSINESS,"
					+ "LEGAL_ENTITY,DOC_NO,INVOICE_NO,REF_K1,REF_K2,REF_K3,DEAL_ASSIGNMENT,CONT_TYPE,TEXT,"
					+ "BA,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),AMT_DC,AMT_INR,INV_TYPE,DESK_NAME,RES_COLLECTION_PRTY,RTL_GPL_TRADER,"
					+ "CLRNG_DOC,CLRNG_DT,WBS_PNL,TO_CHAR(BL_DT,'DD/MM/YYYY'),INVOICE_TYPE,CATEGORY,CURRANCY,AMT_USD,ARREARS_DAYS,"
					+ "AGING,STATUS,OVERDUE_COZ,DUE_AMT,CO_CODE "
					+ "FROM REPORT_RECEIVABLE "
					//+ "WHERE CO_CODE=? "
					+ "ORDER BY TO_DATE(BL_DT,'DD/MM/YYYY') DESC";
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			rset3 = stmt3.executeQuery();
			while(rset3.next()) 
			{
				VCOUNTERPARTY_NM.add((rset3.getString(1)==null?"":rset3.getString(1)).trim());
				VCOUNTERPARTY_ABBR.add(rset3.getString(2)==null?"":rset3.getString(2));
				VCOLL_CATEGORY.add(rset3.getString(3)==null?"":rset3.getString(3));
				VCOUNTERPARTY_CATEGORY.add(rset3.getString(4)==null?"":rset3.getString(4));
				VBUSINESS.add(rset3.getString(5)==null?"":rset3.getString(5));
				VLEGAL_ENTITY.add(rset3.getString(6)==null?"":rset3.getString(6));
				VDOC_NO.add(rset3.getString(7)==null?"":rset3.getString(7));
				String invoice_no = ""+rset3.getString(8)==null?"":rset3.getString(8);
				VINVOICE_NO.add(rset3.getString(8)==null?"":rset3.getString(8));
				VREF_K1.add(rset3.getString(9)==null?"":rset3.getString(9));
				VREF_K2.add(rset3.getString(10)==null?"":rset3.getString(10));
				VREF_K3.add(rset3.getString(11)==null?"":rset3.getString(11));
				VDEAL_ASSIGNMENT.add(rset3.getString(12)==null?"":rset3.getString(12));
				VCONT_TYPE.add(rset3.getString(13)==null?"":rset3.getString(13));
				VTEXT.add(rset3.getString(14)==null?"":rset3.getString(14));
				VBA.add(rset3.getString(15)==null?"":rset3.getString(15));
				VNET_DUE_DT.add(rset3.getString(16)==null?"":rset3.getString(16));
				VAMT_DC.add(rset3.getString(17)==null?"":rset3.getString(17));

				VAMT_INR.add(nf2.format(Double.doubleToRawLongBits(rset3.getDouble(18))==Double.doubleToRawLongBits(0)?"":rset3.getDouble(18)));
				VINV_TYPE.add(rset3.getString(19)==null?"":rset3.getString(19));
				VDESK_NAME.add(rset3.getString(20)==null?"":rset3.getString(20));
				VRES_COLLECTION_PRTY.add(rset3.getString(21)==null?"":rset3.getString(21));
				VRTL_GPL_TRADER.add(rset3.getString(22)==null?"":rset3.getString(22));
				VCLRNG_DOC.add(rset3.getString(23)==null?"":rset3.getString(23));
				VCLRNG_DT.add(rset3.getString(24)==null?"":rset3.getString(24));
				VWBS_PNL.add(rset3.getString(25)==null?"":rset3.getString(25));
				VBL_DT.add(rset3.getString(26)==null?"":rset3.getString(26));
				VINVOICE_TYPE.add(rset3.getString(27)==null?"":rset3.getString(27));
				VCATEGORY.add(rset3.getString(28)==null?"":rset3.getString(28));
				VCURRANCY.add(rset3.getString(29)==null?"":rset3.getString(29));

				VAMT_USD.add(Double.doubleToRawLongBits(rset3.getDouble(30))==Double.doubleToRawLongBits(0)?"":rset3.getDouble(30));
				VARREARS_DAYS.add(rset3.getString(31)==null?"":rset3.getString(31));
				VAGING.add(rset3.getString(32)==null?"":rset3.getString(32));
				VSTATUS.add(rset3.getString(33)==null?"":rset3.getString(33));
				VOVERDUE_COZ.add(rset3.getString(34)==null?"":rset3.getString(34));
				
				String company_cd=rset3.getString(36)==null?"":rset3.getString(36);
				VCO_CODE.add(rset3.getString(36)==null?"":rset3.getString(36));

				VCO_ABBR.add(utilBean.getCompanyAbbr(conn,rset3.getString(36)==null?"":rset3.getString(36)));

				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, invoice_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					exchgRate = rset1.getDouble(1);

					VDUE_AMT.add(nf2.format(rset3.getDouble(35)));

					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset3.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset1.close();
				stmt1.close();

				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, invoice_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					exchgRate = rset1.getDouble(1);

					VDUE_AMT.add(nf2.format(rset3.getDouble(35)));

					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset3.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset1.close();
				stmt1.close();
			}
			rset3.close();
			stmt3.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Receivable_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Receivable_Report_"+splited_sysdate+".xls";
			}
			String subject=comp_abbr+" EMS "+context+": Credit Risk Receivable Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Receivable Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Receivable Report dated "+today_date+" attached.";

			HSSFRow rowhead1 = sheet.createRow((short)0);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("CO Code");  
			rowhead1.createCell((short) 2).setCellValue("Customer ABBR");  
			rowhead1.createCell((short) 3).setCellValue("Customer Name");
			rowhead1.createCell((short) 4).setCellValue("Collection Category");
			rowhead1.createCell((short) 5).setCellValue("Customer Category");
			rowhead1.createCell((short) 6).setCellValue("Type of Invoice"); 
			rowhead1.createCell((short) 7).setCellValue("Category"); 
			rowhead1.createCell((short) 8).setCellValue("Business");
			rowhead1.createCell((short) 9).setCellValue("Legel Entity");
			rowhead1.createCell((short) 10).setCellValue("Doc. No.");
			rowhead1.createCell((short) 11).setCellValue("Ref");
			rowhead1.createCell((short) 12).setCellValue("Ref. Key 1");
			rowhead1.createCell((short) 13).setCellValue("Ref. Key 2");
			rowhead1.createCell((short) 14).setCellValue("Ref. Key 3");  
			rowhead1.createCell((short) 15).setCellValue("Assignment");  
			rowhead1.createCell((short) 16).setCellValue("Type");
			rowhead1.createCell((short) 17).setCellValue("Text");
			rowhead1.createCell((short) 18).setCellValue("BA");
			rowhead1.createCell((short) 19).setCellValue("Net Due Date"); 
			rowhead1.createCell((short) 20).setCellValue("Amount In DC"); 
			rowhead1.createCell((short) 21).setCellValue("Currency");
			rowhead1.createCell((short) 22).setCellValue("Amount In Loc. Cur.");
			rowhead1.createCell((short) 23).setCellValue("Amount In USD");
			rowhead1.createCell((short) 24).setCellValue("Due Amount INR");
			rowhead1.createCell((short) 25).setCellValue("Due Amount USD");
			rowhead1.createCell((short) 26).setCellValue("Arrers(Days)");
			rowhead1.createCell((short) 27).setCellValue("Inv Type");  
			rowhead1.createCell((short) 28).setCellValue("Ageing");
			rowhead1.createCell((short) 29).setCellValue("Desk Name");
			rowhead1.createCell((short) 30).setCellValue("Responsible Collection Party");
			rowhead1.createCell((short) 31).setCellValue("RTL/GPL/Trader Name"); 
			rowhead1.createCell((short) 32).setCellValue("Status"); 
			rowhead1.createCell((short) 33).setCellValue("CLRNG Doc.");
			rowhead1.createCell((short) 34).setCellValue("Clearing Date");
			rowhead1.createCell((short) 35).setCellValue("WBS Element P&L Item");
			rowhead1.createCell((short) 36).setCellValue("B/L Date");
			rowhead1.createCell((short) 37).setCellValue("Reason For Overdue");

			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					HSSFRow row = sheet.createRow((short)i+1);  
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+VCO_ABBR.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_ABBR.elementAt(i));
					row.createCell((short) 3).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VCOLL_CATEGORY.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VCOUNTERPARTY_CATEGORY.elementAt(i));  
					row.createCell((short) 6).setCellValue(""+VINVOICE_TYPE.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VCATEGORY.elementAt(i));
					row.createCell((short) 8).setCellValue(""+VBUSINESS.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VLEGAL_ENTITY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VDOC_NO.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VINVOICE_NO.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VREF_K1.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VREF_K2.elementAt(i));
					row.createCell((short) 14).setCellValue(""+VREF_K3.elementAt(i));  
					row.createCell((short) 15).setCellValue(""+VDEAL_ASSIGNMENT.elementAt(i));
					row.createCell((short) 16).setCellValue(""+VCONT_TYPE.elementAt(i));
					row.createCell((short) 17).setCellValue(""+VTEXT.elementAt(i));
					row.createCell((short) 18).setCellValue(""+VBA.elementAt(i));  
					row.createCell((short) 19).setCellValue(""+VNET_DUE_DT.elementAt(i));
					row.createCell((short) 20).setCellValue(""+VAMT_DC.elementAt(i));
					row.createCell((short) 21).setCellValue(""+VCURRANCY.elementAt(i));
					row.createCell((short) 22).setCellValue(""+VAMT_INR.elementAt(i));
					row.createCell((short) 23).setCellValue(""+VAMT_USD.elementAt(i));
					row.createCell((short) 24).setCellValue(""+VDUE_AMT.elementAt(i));
					row.createCell((short) 25).setCellValue(""+VDUE_AMT_USD.elementAt(i));
					row.createCell((short) 26).setCellValue(""+VARREARS_DAYS.elementAt(i));
					row.createCell((short) 27).setCellValue(""+VINV_TYPE.elementAt(i));  
					row.createCell((short) 28).setCellValue(""+VAGING.elementAt(i));
					row.createCell((short) 29).setCellValue(""+VDESK_NAME.elementAt(i));
					row.createCell((short) 30).setCellValue(""+VRES_COLLECTION_PRTY.elementAt(i));
					row.createCell((short) 31).setCellValue(""+VRTL_GPL_TRADER.elementAt(i));  
					row.createCell((short) 32).setCellValue(""+VSTATUS.elementAt(i));
					row.createCell((short) 33).setCellValue(""+VCLRNG_DOC.elementAt(i));
					row.createCell((short) 34).setCellValue(""+VCLRNG_DT.elementAt(i));
					row.createCell((short) 35).setCellValue(""+VWBS_PNL.elementAt(i));
					row.createCell((short) 36).setCellValue(""+VBL_DT.elementAt(i));
					row.createCell((short) 37).setCellValue(""+VOVERDUE_COZ.elementAt(i));
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 38; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Weekly","Auto");
			String cc_mail_list= (utilBean.getCcMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Weekly","Auto")).trim();

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list.trim(), subject, mailBody, filename, cc_mail_list.trim(), "");
				System.out.println("Receivable Report Mail Done(Weekly).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getMonthlyReceivableReport()
	{
		String function_nm="getMonthlyReceivableReport()";
		String mailBody = "";
		try
		{
			String split[] = today_date.split("/");
			String start_dt = "01/"+split[1]+"/"+split[2];

			double exchgRate = 0; 
			String exchg_rate_cd = "";
			String exchg_rate_nm = "";

			queryString3 = "SELECT COUNTERPARTY_NM, COUNTERPARTY_ABBR, COLLECTION_CATEGORY,COUNTERPARTY_CATEGORY,BUSINESS,"
					+ "LEGAL_ENTITY,DOC_NO,INVOICE_NO,REF_K1,REF_K2,REF_K3,DEAL_ASSIGNMENT,CONT_TYPE,TEXT,"
					+ "BA,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),AMT_DC,AMT_INR,INV_TYPE,DESK_NAME,RES_COLLECTION_PRTY,RTL_GPL_TRADER,"
					+ "CLRNG_DOC,CLRNG_DT,WBS_PNL,TO_CHAR(BL_DT,'DD/MM/YYYY'),INVOICE_TYPE,CATEGORY,CURRANCY,AMT_USD,ARREARS_DAYS,"
					+ "AGING,STATUS,OVERDUE_COZ,DUE_AMT,CO_CODE "
					+ "FROM REPORT_RECEIVABLE "
					+ "WHERE "//CO_CODE=? AND "
					+ "TO_DATE(NET_DUE_DT,'DD/MM/YYYY')<= TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(BL_DT,'DD/MM/YYYY') DESC";
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			stmt3.setString(1, lastdtofmonth);
			rset3 = stmt3.executeQuery();
			while(rset3.next()) 
			{
				VCOUNTERPARTY_NM.add(rset3.getString(1)==null?"":rset3.getString(1));
				VCOUNTERPARTY_ABBR.add(rset3.getString(2)==null?"":rset3.getString(2));
				VCOLL_CATEGORY.add(rset3.getString(3)==null?"":rset3.getString(3));
				VCOUNTERPARTY_CATEGORY.add(rset3.getString(4)==null?"":rset3.getString(4));
				VBUSINESS.add(rset3.getString(5)==null?"":rset3.getString(5));
				VLEGAL_ENTITY.add(rset3.getString(6)==null?"":rset3.getString(6));
				VDOC_NO.add(rset3.getString(7)==null?"":rset3.getString(7));
				String invoice_no = ""+rset3.getString(8)==null?"":rset3.getString(8);
				VINVOICE_NO.add(rset3.getString(8)==null?"":rset3.getString(8));
				VREF_K1.add(rset3.getString(9)==null?"":rset3.getString(9));
				VREF_K2.add(rset3.getString(10)==null?"":rset3.getString(10));
				VREF_K3.add(rset3.getString(11)==null?"":rset3.getString(11));
				VDEAL_ASSIGNMENT.add(rset3.getString(12)==null?"":rset3.getString(12));
				VCONT_TYPE.add(rset3.getString(13)==null?"":rset3.getString(13));
				VTEXT.add(rset3.getString(14)==null?"":rset3.getString(14));
				VBA.add(rset3.getString(15)==null?"":rset3.getString(15));
				VNET_DUE_DT.add(rset3.getString(16)==null?"":rset3.getString(16));
				VAMT_DC.add(rset3.getString(17)==null?"":rset3.getString(17));

				VAMT_INR.add(nf2.format(Double.doubleToRawLongBits(rset3.getDouble(18))==Double.doubleToRawLongBits(0)?"":rset3.getDouble(18)));
				VINV_TYPE.add(rset3.getString(19)==null?"":rset3.getString(19));
				VDESK_NAME.add(rset3.getString(20)==null?"":rset3.getString(20));
				VRES_COLLECTION_PRTY.add(rset3.getString(21)==null?"":rset3.getString(21));
				VRTL_GPL_TRADER.add(rset3.getString(22)==null?"":rset3.getString(22));
				VCLRNG_DOC.add(rset3.getString(23)==null?"":rset3.getString(23));
				VCLRNG_DT.add(rset3.getString(24)==null?"":rset3.getString(24));
				VWBS_PNL.add(rset3.getString(25)==null?"":rset3.getString(25));
				VBL_DT.add(rset3.getString(26)==null?"":rset3.getString(26));
				VINVOICE_TYPE.add(rset3.getString(27)==null?"":rset3.getString(27));
				VCATEGORY.add(rset3.getString(28)==null?"":rset3.getString(28));
				VCURRANCY.add(rset3.getString(29)==null?"":rset3.getString(29));

				VAMT_USD.add(Double.doubleToRawLongBits(rset3.getDouble(30))==Double.doubleToRawLongBits(0)?"":rset3.getDouble(30));
				VARREARS_DAYS.add(rset3.getString(31)==null?"":rset3.getString(31));
				VAGING.add(rset3.getString(32)==null?"":rset3.getString(32));
				VSTATUS.add(rset3.getString(33)==null?"":rset3.getString(33));
				VOVERDUE_COZ.add(rset3.getString(34)==null?"":rset3.getString(34));
				String company_cd=rset3.getString(36)==null?"":rset3.getString(36);
				VCO_CODE.add(rset3.getString(36)==null?"":rset3.getString(36));

				VCO_ABBR.add(utilBean.getCompanyAbbr(conn,rset3.getString(36)==null?"":rset3.getString(36)));

				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, invoice_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					exchgRate = rset1.getDouble(1);

					VDUE_AMT.add(nf2.format(rset3.getDouble(35)));

					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset3.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset1.close();
				stmt1.close();

				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, invoice_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					exchgRate = rset1.getDouble(1);

					VDUE_AMT.add(nf2.format(rset3.getDouble(35)));

					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset3.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset1.close();
				stmt1.close();
			}
			rset3.close();
			stmt3.close();

			String split_sysdate[] = lastdtofmonth.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Monthly_Receivable_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Monthly_Receivable_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Monthly Receivable Report dated "+lastdtofmonth;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Monthly Receivable Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Monthly Receivable Report dated "+lastdtofmonth+" attached.";

			HSSFRow rowhead1 = sheet.createRow((short)0);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("CO Code");  
			rowhead1.createCell((short) 2).setCellValue("Customer ABBR");  
			rowhead1.createCell((short) 3).setCellValue("Customer Name");
			rowhead1.createCell((short) 4).setCellValue("Collection Category");
			rowhead1.createCell((short) 5).setCellValue("Customer Category");
			rowhead1.createCell((short) 6).setCellValue("Type of Invoice"); 
			rowhead1.createCell((short) 7).setCellValue("Category"); 
			rowhead1.createCell((short) 8).setCellValue("Business");
			rowhead1.createCell((short) 9).setCellValue("Legel Entity");
			rowhead1.createCell((short) 10).setCellValue("Doc. No.");
			rowhead1.createCell((short) 11).setCellValue("Ref");
			rowhead1.createCell((short) 12).setCellValue("Ref. Key 1");
			rowhead1.createCell((short) 13).setCellValue("Ref. Key 2");
			rowhead1.createCell((short) 14).setCellValue("Ref. Key 3");  
			rowhead1.createCell((short) 15).setCellValue("Assignment");  
			rowhead1.createCell((short) 16).setCellValue("Type");
			rowhead1.createCell((short) 17).setCellValue("Text");
			rowhead1.createCell((short) 18).setCellValue("BA");
			rowhead1.createCell((short) 19).setCellValue("Net Due Date"); 
			rowhead1.createCell((short) 20).setCellValue("Amount In DC"); 
			rowhead1.createCell((short) 21).setCellValue("Currency");
			rowhead1.createCell((short) 22).setCellValue("Amount In Loc. Cur.");
			rowhead1.createCell((short) 23).setCellValue("Amount In USD");
			rowhead1.createCell((short) 24).setCellValue("Due Amount INR");
			rowhead1.createCell((short) 25).setCellValue("Due Amount USD");
			rowhead1.createCell((short) 26).setCellValue("Arrers(Days)");
			rowhead1.createCell((short) 27).setCellValue("Inv Type");  
			rowhead1.createCell((short) 28).setCellValue("Ageing");
			rowhead1.createCell((short) 29).setCellValue("Desk Name");
			rowhead1.createCell((short) 30).setCellValue("Responsible Collection Party");
			rowhead1.createCell((short) 31).setCellValue("RTL/GPL/Trader Name"); 
			rowhead1.createCell((short) 32).setCellValue("Status"); 
			rowhead1.createCell((short) 33).setCellValue("CLRNG Doc.");
			rowhead1.createCell((short) 34).setCellValue("Clearing Date");
			rowhead1.createCell((short) 35).setCellValue("WBS Element P&L Item");
			rowhead1.createCell((short) 36).setCellValue("B/L Date");
			rowhead1.createCell((short) 37).setCellValue("Reason For Overdue");

			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					HSSFRow row = sheet.createRow((short)i+1);  
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+VCO_ABBR.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_ABBR.elementAt(i));
					row.createCell((short) 3).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VCOLL_CATEGORY.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VCOUNTERPARTY_CATEGORY.elementAt(i));  
					row.createCell((short) 6).setCellValue(""+VINVOICE_TYPE.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VCATEGORY.elementAt(i));
					row.createCell((short) 8).setCellValue(""+VBUSINESS.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VLEGAL_ENTITY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VDOC_NO.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VINVOICE_NO.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VREF_K1.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VREF_K2.elementAt(i));
					row.createCell((short) 14).setCellValue(""+VREF_K3.elementAt(i));  
					row.createCell((short) 15).setCellValue(""+VDEAL_ASSIGNMENT.elementAt(i));
					row.createCell((short) 16).setCellValue(""+VCONT_TYPE.elementAt(i));
					row.createCell((short) 17).setCellValue(""+VTEXT.elementAt(i));
					row.createCell((short) 18).setCellValue(""+VBA.elementAt(i));  
					row.createCell((short) 19).setCellValue(""+VNET_DUE_DT.elementAt(i));
					row.createCell((short) 20).setCellValue(""+VAMT_DC.elementAt(i));
					row.createCell((short) 21).setCellValue(""+VCURRANCY.elementAt(i));
					row.createCell((short) 22).setCellValue(""+VAMT_INR.elementAt(i));
					row.createCell((short) 23).setCellValue(""+VAMT_USD.elementAt(i));
					row.createCell((short) 24).setCellValue(""+VDUE_AMT.elementAt(i));
					row.createCell((short) 25).setCellValue(""+VDUE_AMT_USD.elementAt(i));
					row.createCell((short) 26).setCellValue(""+VARREARS_DAYS.elementAt(i));
					row.createCell((short) 27).setCellValue(""+VINV_TYPE.elementAt(i));  
					row.createCell((short) 28).setCellValue(""+VAGING.elementAt(i));
					row.createCell((short) 29).setCellValue(""+VDESK_NAME.elementAt(i));
					row.createCell((short) 30).setCellValue(""+VRES_COLLECTION_PRTY.elementAt(i));
					row.createCell((short) 31).setCellValue(""+VRTL_GPL_TRADER.elementAt(i));  
					row.createCell((short) 32).setCellValue(""+VSTATUS.elementAt(i));
					row.createCell((short) 33).setCellValue(""+VCLRNG_DOC.elementAt(i));
					row.createCell((short) 34).setCellValue(""+VCLRNG_DT.elementAt(i));
					row.createCell((short) 35).setCellValue(""+VWBS_PNL.elementAt(i));
					row.createCell((short) 36).setCellValue(""+VBL_DT.elementAt(i));
					row.createCell((short) 37).setCellValue(""+VOVERDUE_COZ.elementAt(i));
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 38; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Monthly","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Receivable Report","Risk Mgmt","Monthly","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Receivable Report Mail Done(Monthly).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getPaymentReceiptStatusDetails()
	{
		String function_nm="getPaymentReceiptStatusDetails()";
		String mailBody = "";
		try
		{
			queryString4 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
					+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,GX,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "SEC_TYPE IN (?,?,?) AND SEC_CATEGORY=? AND STATUS IN (?,?,?) " //ONLY FOR 'In order','Dummy','Pending for amendment' 
					+ "AND INORDER_HIST=? AND ISSUE_DT <= TO_DATE(?,'DD/MM/YYYY') AND EXPIRE_DT >= TO_DATE(?,'DD/MM/YYYY') ";
			queryString4+= "ORDER BY ENT_DT DESC";
			stmt4 = conn.prepareStatement(queryString4);
			//stmt4.setString(1, comp_cd);
			stmt4.setString(1, "LC");
			stmt4.setString(2, "BG");
			stmt4.setString(3, "PCG");
			stmt4.setString(4, "R");
			stmt4.setString(5, "O");
			stmt4.setString(6, "D");
			stmt4.setString(7, "A");
			stmt4.setString(8, "Y");
			stmt4.setString(9, today_date);
			stmt4.setString(10, today_date);
			rset4 = stmt4.executeQuery();
			while(rset4.next())
			{
				String counterPartyCd = rset4.getString(1)==null?"":rset4.getString(1);
				String sec_category = rset4.getString(2)==null?"":rset4.getString(2);
				String sec_type = rset4.getString(3)==null?"":rset4.getString(3);
				String sec_ref_no =  rset4.getString(4)==null?"":rset4.getString(4);
				String status = rset4.getString(5)==null?"":rset4.getString(5);
				String currency = rset4.getString(6)==null?"":rset4.getString(6);
				String value = rset4.getString(7)==null?"":rset4.getString(7);
				String received_date = rset4.getString(8)==null?"":rset4.getString(8);
				String seq_no = rset4.getString(9)==null?"":rset4.getString(9);
				String deal_type = rset4.getString(10)==null?"":rset4.getString(10);
				String flucuation = rset4.getString(11)==null?"":rset4.getString(11);
				String iss_bank_cd = rset4.getString(12)==null?"":rset4.getString(12);
				String iss_bank_ref = rset4.getString(13)==null?"":rset4.getString(13);
				String adv_bank_cd = rset4.getString(14)==null?"":rset4.getString(14);
				String adv_bank_ref = rset4.getString(15)==null?"":rset4.getString(15);
				String confirm_bank_cd = rset4.getString(16)==null?"":rset4.getString(16);
				String confirm_bank_ref = rset4.getString(17)==null?"":rset4.getString(17);
				String issue_dt = rset4.getString(18)==null?"":rset4.getString(18);
				String expire_dt = rset4.getString(19)==null?"":rset4.getString(19);
				String review_dt = rset4.getString(20)==null?"":rset4.getString(20);
				String tenor = rset4.getString(21)==null?"":rset4.getString(21);
				String remarks = rset4.getString(22)==null?"":rset4.getString(22);
				String variation = rset4.getString(23)==null?"":rset4.getString(23);
				String guarantor_cd = rset4.getString(24)==null?"":rset4.getString(24);
				String cancel_date = rset4.getString(25)==null?"":rset4.getString(25);
				String renew_date = rset4.getString(26)==null?"":rset4.getString(26);
				String seq_rev_no = rset4.getString(27)==null?"":rset4.getString(27);
				String sap_approval = rset4.getString(28)==null?"":rset4.getString(28);
				String sap_reversal = rset4.getString(29)==null?"":rset4.getString(29);
				String clearance = rset4.getString(30)==null?"":rset4.getString(30);
				String company_cd = rset4.getString(31)==null?"":rset4.getString(31);
				String counterparty_nm = "";
				String counterparty_abbr = "";
				if(clearance.equals("I"))
				{
					counterparty_nm = ""+utilBean.getGasExchangeName(conn,counterPartyCd);
					counterparty_abbr = ""+utilBean.getGasExchangeAbbr(conn,counterPartyCd);

					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					counterparty_nm = ""+utilBean.getCounterpartyName(conn,counterPartyCd);
					counterparty_abbr = ""+utilBean.getCounterpartyABBR(conn,counterPartyCd);

					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				String iss_bank_nm = ""+utilBean.getBankName(conn,iss_bank_cd);
				String adv_bank_nm = ""+utilBean.getBankName(conn,adv_bank_cd);
				String confirm_bank_nm = ""+utilBean.getBankName(conn,confirm_bank_cd);
				String guarantor_nm = ""+utilBean.getCounterpartyName(conn,guarantor_cd);

				String exp_val = "";
				if(!expire_dt.equals(""))
				{
					String[] split_sys_date = today_date.split("/");
					String splited_sysdate = split_sys_date[2]+"-"+split_sys_date[1]+"-"+split_sys_date[0];

					String[] split_exp_date = expire_dt.split("/");
					String splited_expdate = split_exp_date[2]+"-"+split_exp_date[1]+"-"+split_exp_date[0];

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
					Date sys_date = sdf.parse(splited_sysdate);  
					Date exp_Date = sdf.parse(splited_expdate);  
					if(sys_date.compareTo(exp_Date) > 0)   
					{  
						exp_val="Y";
					}   
					else if(sys_date.compareTo(exp_Date) < 0 || sys_date.compareTo(exp_Date) == 0)   
					{  
						exp_val=""; 
					}   
				}  
				VEXP_VAL.add(exp_val);

				String sel_deal_dtl="";
				String deal_dtl = "";
				String dealNo = "";
				String deal_No = "";
				String deal_No_dtl="";
				String agmt = "";
				String cont_type ="";
				String agmt_rev ="";
				String cont = "";
				String cont_rev ="";
				String counterparty_cd = "";
				String entityCd = "";

				queryString1 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND CONTRACT_TYPE IN(?,?,?) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, counterPartyCd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, sec_ref_no);
				stmt1.setString(5, seq_rev_no);
				stmt1.setString(6, "S");
				stmt1.setString(7, "L");
				stmt1.setString(8, "X");
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					agmt = rset1.getString(1)==null?"":rset1.getString(1);
					agmt_rev = rset1.getString(2)==null?"":rset1.getString(2);
					cont = rset1.getString(3)==null?"":rset1.getString(3);
					cont_rev = rset1.getString(4)==null?"":rset1.getString(4);
					cont_type = rset1.getString(5)==null?"":rset1.getString(5);
					counterparty_cd = rset1.getString(6)==null?"":rset1.getString(6);
					entityCd = rset1.getString(7)==null?"":rset1.getString(7);

					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_REV_NO.add(cont_rev);
					VCONT_TYPE.add(cont_type);
					VCONT_NO.add(cont);

					if(clearance.equals("I"))
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;

						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							sel_deal_dtl+="@@"+dealDtl;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
						}
					}
					else
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;

						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							sel_deal_dtl+="@@"+dealDtl;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
						}
					}
				}
				rset1.close();
				stmt1.close();

				if(dealNo.contains(",") && !dealNo.equals("")) 
				{
					String[] spilt_dealNo = dealNo.split(", ");

					for(int i=0; i<spilt_dealNo.length;i++) 
					{
						VMUL_DEALS.add(spilt_dealNo[i]);

						queryString2 = "SELECT INVOICE_NO,AMT_DC,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),DUE_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),CO_CODE "
								+ "FROM VIEW_RECEIVABLE_MST "
								+ "WHERE CO_CODE=? AND COUNTERPARTY_NM=? AND COUNTERPARTY_ABBR=? AND DEAL_ASSIGNMENT=? "
								+ "AND ((DUE_AMT > 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')))";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, company_cd);
						stmt2.setString(2, counterparty_nm);
						stmt2.setString(3, counterparty_abbr);
						stmt2.setString(4, spilt_dealNo[i]);
						stmt2.setString(5, today_date);
						stmt2.setString(6, today_date);
						stmt2.setString(7, today_date);
						stmt2.setString(8, today_date);
						stmt2.setString(9, today_date);
						rset2 = stmt2.executeQuery();
						if(rset2.next())
						{
							double amt_dc = rset2.getDouble(2);
							double due_amt = rset2.getDouble(4);
							double recv_amt = amt_dc-due_amt;

							VINVOICE_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
							VAMT_DC.add(nf.format(amt_dc));
							VNET_DUE_DT.add(rset2.getString(3)==null?"":rset2.getString(3));
							VDUE_AMT.add(nf.format(due_amt));
							VRECV_AMT.add(nf.format(recv_amt));
							VPAY_RECV_DT.add(rset2.getString(5)==null?"":rset2.getString(5));

							if(due_amt>2) 
							{
								VPAYMENT_STATUS.add("Not Received");
							}
							else
							{
								VPAYMENT_STATUS.add("Received");
							}
							String co_cd=rset2.getString(6)==null?"":rset2.getString(6);
							VCO_CODE.add(rset2.getString(6)==null?"":rset2.getString(6));
							String co_abbr = utilBean.getCompanyAbbr(conn,rset2.getString(6)==null?"":rset2.getString(6));

							VCO_ABBR.add(co_abbr);
							String ref_no = co_cd+"-"+sec_ref_no;
							VCOUNTERPARTY_CD.add(counterPartyCd);
							VCOUNTERPARTY_NM.add(counterparty_nm);
							VSEC_CATEGORY.add(sec_category);
							VSEC_TYPE.add(sec_type);
							VSEC_REF_NO.add(ref_no);
							if(status.equals("P"))
							{
								VSTATUS.add("Pending");
							}
							else if(status.equals("O"))
							{
								VSTATUS.add("In Order");
							}
							else if(status.equals("C"))
							{
								VSTATUS.add("Cancelled");
							}
							else if(status.equals("A"))
							{
								VSTATUS.add("Pending For Amendment");
							}
							else if(status.equals("R"))
							{
								VSTATUS.add("Restated");
							}
							else if(status.equals("D"))
							{
								VSTATUS.add("Dummy");
							}
							else if(status.equals("E"))
							{
								VSTATUS.add("Expired");
							}
							VCURRENCY.add(currency);
							VVALUE.add(value);
							VRECEIVED_DATE.add(received_date);
							VDEAL_TYPE.add(deal_type);
							VVALUE_FLUCTUATION.add(flucuation);
							VISSUE_DT.add(issue_dt);
							VEXPIRE_DT.add(expire_dt);
							VREMARK.add(remarks);
							VCANCEL_DT.add(cancel_date);
							VRENEW_DT.add(renew_date);
							VDEAL_DTL.add(deal_dtl);

							String deal_number_arr[] = spilt_dealNo[i].split("-",2);
							String deal_number=deal_number_arr[1];
							
							if(deal_number.startsWith("S"))
							{
								cont_type = Character.toString(deal_number.charAt(0));
								agmt = Character.toString(deal_number.charAt(1));
								cont = Character.toString(deal_number.charAt(3));
							}
							else
							{
								cont_type = Character.toString(deal_number.charAt(0));
								cont = deal_number.substring(1);
								agmt = "0";
							}

							queryString3 = "SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE,CONT_NO "
									+ "FROM FMS_SUPPLY_CONT_MST A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
									+ "AND AGMT_NO=? AND CONT_NO=? "
									+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, company_cd);
							stmt3.setString(2, counterPartyCd);
							stmt3.setString(3, cont_type);
							stmt3.setString(4, agmt);
							stmt3.setString(5, cont);
							rset3 = stmt3.executeQuery();
							while(rset3.next())
							{
								String cont_ref=rset3.getString(1)==null?"":rset3.getString(1);
								String con_type =rset3.getString(4)==null?"":rset3.getString(4);

								if(con_type.equals("X"))
								{
									cont_ref=rset3.getString(2)==null?"":rset3.getString(2);
								}
								String agmt_base=rset3.getString(3)==null?"":rset3.getString(3);

								if(agmt_base.equals("D"))
								{
									spilt_dealNo[i]+=" [DLV]";
								}
								if(!cont_ref.equals(""))
								{
									spilt_dealNo[i]+=" ["+cont_ref+"]";
								}
							}
							VDEAL_REF_NO.add(spilt_dealNo[i]);
							rset3.close();
							stmt3.close();
						}
						rset2.close();
						stmt2.close();
					}
				}
				else if(!dealNo.equals(""))
				{
					VMUL_DEALS.add(dealNo);

					queryString2 = "SELECT INVOICE_NO,AMT_DC,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),DUE_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),CO_CODE "
							+ "FROM VIEW_RECEIVABLE_MST "
							+ "WHERE CO_CODE=? AND COUNTERPARTY_NM=? AND COUNTERPARTY_ABBR=? AND DEAL_ASSIGNMENT=? "
							+ "AND ((DUE_AMT > 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') "
							+ "AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
							+ "AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, counterparty_nm);
					stmt2.setString(3, counterparty_abbr);
					stmt2.setString(4, dealNo);
					stmt2.setString(5, today_date);
					stmt2.setString(6, today_date);
					stmt2.setString(7, today_date);
					stmt2.setString(8, today_date);
					stmt2.setString(9, today_date);
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						double amt_dc = rset2.getDouble(2);
						double due_amt = rset2.getDouble(4);
						double recv_amt = amt_dc-due_amt;

						VINVOICE_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
						VAMT_DC.add(nf.format(amt_dc));
						VNET_DUE_DT.add(rset2.getString(3)==null?"":rset2.getString(3));
						VDUE_AMT.add(nf.format(due_amt));
						VRECV_AMT.add(nf.format(recv_amt));
						VPAY_RECV_DT.add(rset2.getString(5)==null?"":rset2.getString(5));

						if(due_amt>2) 
						{
							VPAYMENT_STATUS.add("Not Received");
						}
						else
						{
							VPAYMENT_STATUS.add("Received");
						}

						String co_cd=rset2.getString(6)==null?"":rset2.getString(6);
						VCO_CODE.add(rset2.getString(6)==null?"":rset2.getString(6));
						String co_abbr = utilBean.getCompanyAbbr(conn,rset2.getString(6)==null?"":rset2.getString(6));

						VCO_ABBR.add(co_abbr);
						String ref_no = co_cd+"-"+sec_ref_no;
						VCOUNTERPARTY_CD.add(counterPartyCd);
						VCOUNTERPARTY_NM.add(counterparty_nm);
						VSEC_CATEGORY.add(sec_category);
						VSEC_TYPE.add(sec_type);
						VSEC_REF_NO.add(ref_no);
						if(status.equals("P"))
						{
							VSTATUS.add("Pending");
						}
						else if(status.equals("O"))
						{
							VSTATUS.add("In Order");
						}
						else if(status.equals("C"))
						{
							VSTATUS.add("Cancelled");
						}
						else if(status.equals("A"))
						{
							VSTATUS.add("Pending For Amendment");
						}
						else if(status.equals("R"))
						{
							VSTATUS.add("Restated");
						}
						else if(status.equals("D"))
						{
							VSTATUS.add("Dummy");
						}
						else if(status.equals("E"))
						{
							VSTATUS.add("Expired");
						}
						VCURRENCY.add(currency);
						VVALUE.add(value);
						VRECEIVED_DATE.add(received_date);
						VDEAL_TYPE.add(deal_type);
						VVALUE_FLUCTUATION.add(flucuation);
						VISSUE_DT.add(issue_dt);
						VEXPIRE_DT.add(expire_dt);
						VREMARK.add(remarks);
						VCANCEL_DT.add(cancel_date);
						VRENEW_DT.add(renew_date);
						VDEAL_DTL.add(deal_dtl);

						//String dis_cont_mapping=""+utilBean.getDisplayDealMapping(""+agmt,""+agmt_rev, ""+cont, ""+cont_rev, ""+cont_type);
						String dis_cont_mapping=""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");

						queryString3 = "SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE,CONT_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND AGMT_REV=? AND CONT_REV=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, company_cd);
						stmt3.setString(2, counterPartyCd);
						stmt3.setString(3, cont_type);
						stmt3.setString(4, agmt);
						stmt3.setString(5, cont);
						stmt3.setString(6, agmt_rev);
						stmt3.setString(7, cont_rev);
						rset3 = stmt3.executeQuery();
						while(rset3.next())
						{
							String cont_ref=rset3.getString(1)==null?"":rset3.getString(1);
							String con_type =rset3.getString(4)==null?"":rset3.getString(4);

							if(con_type.equals("X"))
							{
								cont_ref=rset3.getString(2)==null?"":rset3.getString(2);
							}
							String agmt_base=rset3.getString(3)==null?"":rset3.getString(3);

							if(agmt_base.equals("D"))
							{
								dis_cont_mapping+=" [DLV]";
							}
							if(!cont_ref.equals(""))
							{
								dis_cont_mapping+=" ["+cont_ref+"]";
							}
						}
						VDEAL_REF_NO.add(dis_cont_mapping);
						rset3.close();
						stmt3.close();
					}
					rset2.close();
					stmt2.close();
				}
			}
			rset4.close();
			stmt4.close();


			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Payment_Receipt_Status_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Payment_Receipt_Status_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Payment Receipt Status Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Payment Receipt Status Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Payment Receipt Status Report dated "+today_date+" attached.";

			HSSFRow rowhead1 = sheet.createRow((short)0);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Entity");  
			rowhead1.createCell((short) 2).setCellValue("Customer Name");  
			rowhead1.createCell((short) 3).setCellValue("Security Type");
			rowhead1.createCell((short) 4).setCellValue("Security Ref#");
			rowhead1.createCell((short) 5).setCellValue("Deal Ref#");
			rowhead1.createCell((short) 6).setCellValue("Invoice No."); 
			rowhead1.createCell((short) 7).setCellValue("Invoice Amount(INR)"); 
			rowhead1.createCell((short) 8).setCellValue("Invoice Due Date");
			rowhead1.createCell((short) 9).setCellValue("Received Amount(INR)");
			rowhead1.createCell((short) 10).setCellValue("Payment Received Date");
			rowhead1.createCell((short) 11).setCellValue("Payment Status");
			rowhead1.createCell((short) 12).setCellValue("Security Expiry Date");
			rowhead1.createCell((short) 13).setCellValue("Security Status");

			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;

					HSSFRow row = sheet.createRow((short)i+1);  
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+VCO_ABBR.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));
					row.createCell((short) 3).setCellValue(""+VSEC_TYPE.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VSEC_REF_NO.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VDEAL_REF_NO.elementAt(i));  
					row.createCell((short) 6).setCellValue(""+VINVOICE_NO.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VAMT_DC.elementAt(i));
					row.createCell((short) 8).setCellValue(""+VNET_DUE_DT.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VRECV_AMT.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VPAY_RECV_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VPAYMENT_STATUS.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VEXPIRE_DT.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VSTATUS.elementAt(i));
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 14; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Payment Receipt Status Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Payment Receipt Status Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Payment Receipt Status Report Mail Done(Daily).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getIncomingSecurityStatusDtls() 
	{
		String function_nm="getIncomingSecurityStatusDtls()";
		String mailBody = "";
		try
		{
			String exchng_rate_cd="";
			double exchange_rate=0.00;

			queryString1 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "ISS_BANK_REF , TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , GX,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND SEC_CATEGORY=? "
					+ "AND TO_DATE(ISSUE_DT) <=TO_DATE(?,'dd/mm/yyyy')  "
					+ "AND (TO_DATE(EXPIRE_DT)>=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(CANCEL_DT) >=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL)) "
					+ "AND STATUS IN(?,?,?) "
					+ "ORDER BY EXPIRE_DT ASC";
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt1.setString(1, "K");
			stmt1.setString(2, "R");
			stmt1.setString(3, today_date);
			stmt1.setString(4, today_date);
			stmt1.setString(5, today_date);
			stmt1.setString(6, "O");
			stmt1.setString(7, "A");
			stmt1.setString(8, "D");
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String counterPartyCd = rset1.getString(1)==null?"":rset1.getString(1);
				String sec_categry = rset1.getString(2)==null?"":rset1.getString(2);
				String sec_type = rset1.getString(3)==null?"":rset1.getString(3);
				String sec_ref_no = rset1.getString(4)==null?"":rset1.getString(4);
				String status = rset1.getString(5)==null?"":rset1.getString(5);
				String currency = rset1.getString(6)==null?"":rset1.getString(6);
				String value = rset1.getString(7)==null?"":rset1.getString(7);
				String seq_no = rset1.getString(8)==null?"":rset1.getString(8);
				String iss_bank_ref = rset1.getString(9)==null?"":rset1.getString(9);
				String expire_dt = rset1.getString(10)==null?"":rset1.getString(10);
				String remark = rset1.getString(11)==null?"":rset1.getString(11);
				String seq_rev_no = rset1.getString(12)==null?"":rset1.getString(12);
				String clearance = rset1.getString(13)==null?"":rset1.getString(13);
				String company_cd = rset1.getString(14)==null?"":rset1.getString(14);
				String ref_no = company_cd+"-"+sec_ref_no;

				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				VCOUNTERPARTY_CD.add(counterPartyCd);
				VSEC_TYPE.add(sec_type);
				VSEC_REF_NO.add(ref_no);

				if(status.equals("P"))
				{
					VSTATUS.add("Pending");
				}
				else if(status.equals("O"))
				{
					VSTATUS.add("In Order");
				}
				else if(status.equals("C"))
				{
					VSTATUS.add("Cancelled");
				}
				else if(status.equals("A"))
				{
					VSTATUS.add("Pending For Amendment");
				}
				else if(status.equals("R"))
				{
					VSTATUS.add("Restated");
				}
				else if(status.equals("D"))
				{
					VSTATUS.add("Dummy");
				}
				else if(status.equals("E"))
				{
					VSTATUS.add("Expired");
				}
				String rate_nm="Shell Treasury Rate";

				queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp = conn.prepareStatement(queryString_temp);
				//stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				rset_temp.close();
				stmt_temp.close();

				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND TO_DATE(B.EFF_DT) <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchange_rate = rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();

				if(currency.equals("1")) 
				{
					VVALUE.add(nf.format(Double.parseDouble(value)));
					VVALUE_USD.add(nf.format(Double.parseDouble(value)/exchange_rate));
				}
				else 
				{
					VVALUE.add(nf.format(Double.parseDouble(value)*exchange_rate));
					VVALUE_USD.add(nf.format(Double.parseDouble(value)));
				}

				VISS_BANK_REF.add(iss_bank_ref);
				VEXPIRE_DT.add(expire_dt);
				VREMARK.add(remark);
				if(clearance.equals("I"))
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				VSEC_CATEGRY.add(sec_categry);

				if(currency.equals("1"))
				{
					VCURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					VCURRENCY.add("USD");
				}

				String deal_dtl = "";
				String dealNo = "";
				String entity_cd = "";
				String disp_cont_type="";
				queryString2 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, counterPartyCd);
				stmt2.setString(3, seq_no);
				stmt2.setString(4, sec_ref_no);
				stmt2.setString(5, seq_rev_no);
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					String agmt = rset2.getString(1)==null?"":rset2.getString(1);
					String agmt_rev = rset2.getString(2)==null?"":rset2.getString(2);
					String cont = rset2.getString(3)==null?"":rset2.getString(3);
					String cont_rev = rset2.getString(4)==null?"":rset2.getString(4);
					String cont_type = rset2.getString(5)==null?"":rset2.getString(5);
					String counterparty_cd = rset2.getString(6)==null?"":rset2.getString(6);
					entity_cd = rset2.getString(7)==null?"":rset2.getString(7);

					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				VDEAL_NO.add(dealNo);
				VDIS_CONTRACT_TYPE.add(disp_cont_type);
				VDEAL_DTL.add(deal_dtl);
				rset2.close();
				stmt2.close();

				int days_left=utilDate.getDays(expire_dt, today_date)-1;
				VPREVIOUS_DT.add(days_left);
			}
			rset1.close();
			stmt1.close();

			queryString4 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "ISS_BANK_REF , TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , GX,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND SEC_CATEGORY=? "
					+ "AND TO_DATE(ISSUE_DT) <=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(EXPIRE_DT)>=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(CANCEL_DT) >=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL)) "
					+ "AND STATUS IN(?,?,?) "
					+ "ORDER BY EXPIRE_DT ASC";
			stmt4 = conn.prepareStatement(queryString4);
			//stmt4.setString(1, comp_cd);
			stmt4.setString(1, "I");
			stmt4.setString(2, "R");
			stmt4.setString(3, today_date);
			stmt4.setString(4, today_date);
			stmt4.setString(5, today_date);
			stmt4.setString(6, "O");
			stmt4.setString(7, "A");
			stmt4.setString(8, "D");
			rset4 = stmt4.executeQuery();
			while(rset4.next())
			{
				String counterPartyCd = rset4.getString(1)==null?"":rset4.getString(1);
				String sec_categry = rset4.getString(2)==null?"":rset4.getString(2);
				String sec_type = rset4.getString(3)==null?"":rset4.getString(3);
				String sec_ref_no = rset4.getString(4)==null?"":rset4.getString(4);
				String status = rset4.getString(5)==null?"":rset4.getString(5);
				String currency = rset4.getString(6)==null?"":rset4.getString(6);
				String value = rset4.getString(7)==null?"":rset4.getString(7);
				String seq_no = rset4.getString(8)==null?"":rset4.getString(8);
				String iss_bank_ref = rset4.getString(9)==null?"":rset4.getString(9);
				String expire_dt = rset4.getString(10)==null?"":rset4.getString(10);
				String remark = rset4.getString(11)==null?"":rset4.getString(11);
				String seq_rev_no = rset4.getString(12)==null?"":rset4.getString(12);
				String clearance = rset4.getString(13)==null?"":rset4.getString(13);
				String company_cd = rset4.getString(14)==null?"":rset4.getString(14);
				String ref_no = company_cd+"-"+sec_ref_no;

				V_LEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				V_COUNTERPARTY_CD.add(counterPartyCd);
				V_SEC_TYPE.add(sec_type);
				V_SEC_REF_NO.add(ref_no);

				if(status.equals("P"))
				{
					V_STATUS.add("Pending");
				}
				else if(status.equals("O"))
				{
					V_STATUS.add("In Order");
				}
				else if(status.equals("C"))
				{
					V_STATUS.add("Cancelled");
				}
				else if(status.equals("A"))
				{
					V_STATUS.add("Pending For Amendment");
				}
				else if(status.equals("R"))
				{
					V_STATUS.add("Restated");
				}
				else if(status.equals("D"))
				{
					V_STATUS.add("Dummy");
				}
				else if(status.equals("E"))
				{
					VSTATUS.add("Expired");
				}

				String rate_nm="Shell Treasury Rate";

				queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp = conn.prepareStatement(queryString_temp);
				//stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				rset_temp.close();
				stmt_temp.close();

				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND TO_DATE(B.EFF_DT) <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchange_rate = rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();

				if(currency.equals("1")) 
				{
					V_VALUE.add(nf.format(Double.parseDouble(value)));
					V_VALUE_USD.add(nf.format(Double.parseDouble(value)/exchange_rate));
				}
				else 
				{
					V_VALUE.add(nf.format(Double.parseDouble(value)*exchange_rate));
					V_VALUE_USD.add(nf.format(Double.parseDouble(value)));
				}

				V_ISS_BANK_REF.add(iss_bank_ref);
				V_EXPIRE_DT.add(expire_dt);
				V_REMARK.add(remark);
				if(clearance.equals("I"))
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				V_SEC_CATEGRY.add(sec_categry);

				if(currency.equals("1"))
				{
					V_CURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					V_CURRENCY.add("USD");
				}

				String deal_dtl = "";
				String dealNo = "";
				String entity_cd = "";
				String disp_cont_type="";
				queryString5 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, counterPartyCd);
				stmt5.setString(3, seq_no);
				stmt5.setString(4, sec_ref_no);
				stmt5.setString(5, seq_rev_no);
				rset5 = stmt5.executeQuery();
				while(rset5.next())
				{
					String agmt = rset5.getString(1)==null?"":rset5.getString(1);
					String agmt_rev = rset5.getString(2)==null?"":rset5.getString(2);
					String cont = rset5.getString(3)==null?"":rset5.getString(3);
					String cont_rev = rset5.getString(4)==null?"":rset5.getString(4);
					String cont_type = rset5.getString(5)==null?"":rset5.getString(5);
					String counterparty_cd = rset5.getString(6)==null?"":rset5.getString(6);
					entity_cd = rset5.getString(7)==null?"":rset5.getString(7);

					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				rset5.close();
				stmt5.close();

				V_DEAL_NO.add(dealNo);
				V_DIS_CONTRACT_TYPE.add(disp_cont_type);
				V_DEAL_DTL.add(deal_dtl);

				int days_left=utilDate.getDays(expire_dt, today_date)-1;
				V_PREVIOUS_DT.add(days_left);
			}
			rset4.close();
			stmt4.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename ="";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Incoming_Security_Status_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Incoming_Security_Status_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Incoming Security Status Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Incoming Security Status Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Incoming Security Status Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("KYC Incoming Security Status Report");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));

			HSSFRow rowhead1 = sheet.createRow((short)1);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Legal Entity");
			rowhead1.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead1.createCell((short) 3).setCellValue("Issuing Bank Ref#");  
			rowhead1.createCell((short) 4).setCellValue("Contract Type");
			rowhead1.createCell((short) 5).setCellValue("Contract#");
			rowhead1.createCell((short) 6).setCellValue("Security Ref#");
			rowhead1.createCell((short) 7).setCellValue("Security Type");
			rowhead1.createCell((short) 8).setCellValue("Security Value(INR)"); 
			rowhead1.createCell((short) 9).setCellValue("Security Value(USD)"); 
			rowhead1.createCell((short) 10).setCellValue("Day Before Expiry Date");
			rowhead1.createCell((short) 11).setCellValue("Expiry Date");
			rowhead1.createCell((short) 12).setCellValue("Status");
			rowhead1.createCell((short) 13).setCellValue("Remarks");
			rowhead1.createCell((short) 14).setCellValue("Form C");

			int index = 0;
			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					index++;

					HSSFRow row = sheet.createRow((short)i+2);  
					row.createCell((short) 0).setCellValue(j);
					row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+VISS_BANK_REF.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VDEAL_NO.elementAt(i));
					row.createCell((short) 6).setCellValue(""+VSEC_REF_NO.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VSEC_TYPE.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+VVALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VVALUE_USD.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VPREVIOUS_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VEXPIRE_DT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VSTATUS.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VREMARK.elementAt(i));
					row.createCell((short) 14).setCellValue("");
					VINDEX.add(index);
				}
			}
			else
			{
				index++;
				VINDEX.add(index);
			}

			HSSFRow row_head = sheet.createRow((short)VINDEX.size()+3);
			row_head.createCell((short) 0).setCellValue("IGX Incoming Security Status Report");
			sheet.addMergedRegion(new CellRangeAddress(VINDEX.size()+3,VINDEX.size()+3,0,13));

			HSSFRow rowhead = sheet.createRow((short)VINDEX.size()+4); 
			rowhead.createCell((short) 0).setCellValue("Sr#");
			rowhead.createCell((short) 1).setCellValue("Legal Entity");
			rowhead.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead.createCell((short) 3).setCellValue("Issuing Bank Ref#");  
			rowhead.createCell((short) 4).setCellValue("Contract Type");
			rowhead.createCell((short) 5).setCellValue("Contract#");
			rowhead.createCell((short) 6).setCellValue("Security Ref#");
			rowhead.createCell((short) 7).setCellValue("Security Type");
			rowhead.createCell((short) 8).setCellValue("Security Value(INR)"); 
			rowhead.createCell((short) 9).setCellValue("Security Value(USD)"); 
			rowhead.createCell((short) 10).setCellValue("Day Before Expiry Date");
			rowhead.createCell((short) 11).setCellValue("Expiry Date");
			rowhead.createCell((short) 12).setCellValue("Status");
			rowhead.createCell((short) 13).setCellValue("Remarks");
			rowhead.createCell((short) 14).setCellValue("Form C");

			if(V_COUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < V_COUNTERPARTY_NM.size(); i++ )
				{
					j++;

					HSSFRow row = sheet.createRow((short)VINDEX.size()+i+5);
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+V_LEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+V_COUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+V_ISS_BANK_REF.elementAt(i));
					row.createCell((short) 4).setCellValue(""+V_DIS_CONTRACT_TYPE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+V_DEAL_NO.elementAt(i));
					row.createCell((short) 6).setCellValue(""+V_SEC_REF_NO.elementAt(i));
					row.createCell((short) 7).setCellValue(""+V_SEC_TYPE.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+V_VALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+V_VALUE_USD.elementAt(i));
					row.createCell((short) 10).setCellValue(""+V_PREVIOUS_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+V_EXPIRE_DT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+V_STATUS.elementAt(i));
					row.createCell((short) 13).setCellValue(""+V_REMARK.elementAt(i));
					row.createCell((short) 14).setCellValue("");
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 12; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Incoming Security Status Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Incoming Security Status Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Incoming Security Status Report Mail Done(Daily).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getOutgoingSecurityStatusDtls() 
	{
		String function_nm="getOutgoingSecurityStatusDtls()";
		String mailBody = "";
		try
		{
			String exchng_rate_cd="";
			double exchange_rate=0.00;

			queryString1 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "ISS_BANK_REF , TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , GX, COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND SEC_CATEGORY=? "
					+ "AND TO_DATE(ISSUE_DT) <=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(EXPIRE_DT)>=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(CANCEL_DT) >=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL)) "
					+ "AND STATUS IN(?,?,?) "
					+ "ORDER BY EXPIRE_DT ASC";
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt1.setString(1, "K");
			stmt1.setString(2, "I");
			stmt1.setString(3, today_date);
			stmt1.setString(4, today_date);
			stmt1.setString(5, today_date);
			stmt1.setString(6, "O");
			stmt1.setString(7, "A");
			stmt1.setString(8, "D");
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String counterPartyCd = rset1.getString(1)==null?"":rset1.getString(1);
				String sec_categry = rset1.getString(2)==null?"":rset1.getString(2);
				String sec_type = rset1.getString(3)==null?"":rset1.getString(3);
				String sec_ref_no = rset1.getString(4)==null?"":rset1.getString(4);
				String status = rset1.getString(5)==null?"":rset1.getString(5);
				String currency = rset1.getString(6)==null?"":rset1.getString(6);
				String value = rset1.getString(7)==null?"":rset1.getString(7);
				String seq_no = rset1.getString(8)==null?"":rset1.getString(8);
				String iss_bank_ref = rset1.getString(9)==null?"":rset1.getString(9);
				String expire_dt = rset1.getString(10)==null?"":rset1.getString(10);
				String remark = rset1.getString(11)==null?"":rset1.getString(11);
				String seq_rev_no = rset1.getString(12)==null?"":rset1.getString(12);
				String clearance = rset1.getString(13)==null?"":rset1.getString(13);
				String company_cd = rset1.getString(14)==null?"":rset1.getString(14);
				String ref_no = company_cd+"-"+sec_ref_no;

				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				VCOUNTERPARTY_CD.add(counterPartyCd);
				VSEC_TYPE.add(sec_type);
				VSEC_REF_NO.add(ref_no);

				if(status.equals("P"))
				{
					VSTATUS.add("Pending");
				}
				else if(status.equals("O"))
				{
					VSTATUS.add("In Order");
				}
				else if(status.equals("C"))
				{
					VSTATUS.add("Cancelled");
				}
				else if(status.equals("A"))
				{
					VSTATUS.add("Pending For Amendment");
				}
				else if(status.equals("R"))
				{
					VSTATUS.add("Restated");
				}
				else if(status.equals("D"))
				{
					VSTATUS.add("Dummy");
				}
				else if(status.equals("E"))
				{
					VSTATUS.add("Expired");
				}

				String rate_nm="Shell Treasury Rate";

				queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp = conn.prepareStatement(queryString_temp);
				//stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				rset_temp.close();
				stmt_temp.close();

				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND TO_DATE(B.EFF_DT) <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchange_rate = rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();

				if(currency.equals("1")) 
				{
					VVALUE.add(nf.format(Double.parseDouble(value)));
					VVALUE_USD.add(nf.format(Double.parseDouble(value)/exchange_rate));
				}
				else 
				{
					VVALUE.add(nf.format(Double.parseDouble(value)*exchange_rate));
					VVALUE_USD.add(nf.format(Double.parseDouble(value)));
				}

				VISS_BANK_REF.add(iss_bank_ref);
				VEXPIRE_DT.add(expire_dt);
				VREMARK.add(remark);
				if(clearance.equals("I"))
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				VSEC_CATEGRY.add(sec_categry);

				if(currency.equals("1"))
				{
					VCURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					VCURRENCY.add("USD");
				}

				String deal_dtl = "";
				String dealNo = "";
				String entity_cd = "";
				String disp_cont_type="";
				queryString2 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, counterPartyCd);
				stmt2.setString(3, seq_no);
				stmt2.setString(4, sec_ref_no);
				stmt2.setString(5, seq_rev_no);
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					String agmt = rset2.getString(1)==null?"":rset2.getString(1);
					String agmt_rev = rset2.getString(2)==null?"":rset2.getString(2);
					String cont = rset2.getString(3)==null?"":rset2.getString(3);
					String cont_rev = rset2.getString(4)==null?"":rset2.getString(4);
					String cont_type = rset2.getString(5)==null?"":rset2.getString(5);
					String counterparty_cd = rset2.getString(6)==null?"":rset2.getString(6);
					entity_cd = rset2.getString(7)==null?"":rset2.getString(7);

					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				rset2.close();
				stmt2.close();

				VDEAL_NO.add(dealNo);
				VDIS_CONTRACT_TYPE.add(disp_cont_type);
				VDEAL_DTL.add(deal_dtl);

				int days_left=utilDate.getDays(expire_dt, today_date)-1;
				VPREVIOUS_DT.add(days_left);
			}
			rset1.close();
			stmt1.close();

			queryString4 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "ISS_BANK_REF , TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , GX, COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND SEC_CATEGORY=? "
					+ "AND TO_DATE(ISSUE_DT) <=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(EXPIRE_DT)>=TO_DATE(?,'dd/mm/yyyy') "
					+ "AND (TO_DATE(CANCEL_DT) >=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL)) "
					+ "AND STATUS IN(?,?,?) "
					+ "ORDER BY EXPIRE_DT ASC";
			stmt4 = conn.prepareStatement(queryString4);
			//stmt4.setString(1, comp_cd);
			stmt4.setString(1, "I");
			stmt4.setString(2, "I");
			stmt4.setString(3, today_date);
			stmt4.setString(4, today_date);
			stmt4.setString(5, today_date);
			stmt4.setString(6, "O");
			stmt4.setString(7, "A");
			stmt4.setString(8, "D");
			rset4 = stmt4.executeQuery();
			while(rset4.next())
			{
				String counterPartyCd = rset4.getString(1)==null?"":rset4.getString(1);
				String sec_categry = rset4.getString(2)==null?"":rset4.getString(2);
				String sec_type = rset4.getString(3)==null?"":rset4.getString(3);
				String sec_ref_no = rset4.getString(4)==null?"":rset4.getString(4);
				String status = rset4.getString(5)==null?"":rset4.getString(5);
				String currency = rset4.getString(6)==null?"":rset4.getString(6);
				String value = rset4.getString(7)==null?"":rset4.getString(7);
				String seq_no = rset4.getString(8)==null?"":rset4.getString(8);
				String iss_bank_ref = rset4.getString(9)==null?"":rset4.getString(9);
				String expire_dt = rset4.getString(10)==null?"":rset4.getString(10);
				String remark = rset4.getString(11)==null?"":rset4.getString(11);
				String seq_rev_no = rset4.getString(12)==null?"":rset4.getString(12);
				String clearance = rset4.getString(13)==null?"":rset4.getString(13);
				String company_cd = rset4.getString(14)==null?"":rset4.getString(14);
				String ref_no = company_cd+"-"+sec_ref_no;

				V_LEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				V_COUNTERPARTY_CD.add(counterPartyCd);
				V_SEC_TYPE.add(sec_type);
				V_SEC_REF_NO.add(ref_no);

				if(status.equals("P"))
				{
					V_STATUS.add("Pending");
				}
				else if(status.equals("O"))
				{
					V_STATUS.add("In Order");
				}
				else if(status.equals("C"))
				{
					V_STATUS.add("Cancelled");
				}
				else if(status.equals("A"))
				{
					V_STATUS.add("Pending For Amendment");
				}
				else if(status.equals("R"))
				{
					V_STATUS.add("Restated");
				}
				else if(status.equals("D"))
				{
					V_STATUS.add("Dummy");
				}
				else if(status.equals("E"))
				{
					VSTATUS.add("Expired");
				}

				String rate_nm="Shell Treasury Rate";

				queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp = conn.prepareStatement(queryString_temp);
				//stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				rset_temp.close();
				stmt_temp.close();

				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND TO_DATE(B.EFF_DT) <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchange_rate = rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();

				if(currency.equals("1")) 
				{
					V_VALUE.add(nf.format(Double.parseDouble(value)));
					V_VALUE_USD.add(nf.format(Double.parseDouble(value)/exchange_rate));
				}
				else 
				{
					V_VALUE.add(nf.format(Double.parseDouble(value)*exchange_rate));
					V_VALUE_USD.add(nf.format(Double.parseDouble(value)));
				}

				V_ISS_BANK_REF.add(iss_bank_ref);
				V_EXPIRE_DT.add(expire_dt);
				V_REMARK.add(remark);
				if(clearance.equals("I"))
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				V_SEC_CATEGRY.add(sec_categry);

				if(currency.equals("1"))
				{
					V_CURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					V_CURRENCY.add("USD");
				}

				String deal_dtl = "";
				String dealNo = "";
				String entity_cd = "";
				String disp_cont_type="";
				queryString5 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, counterPartyCd);
				stmt5.setString(3, seq_no);
				stmt5.setString(4, sec_ref_no);
				stmt5.setString(5, seq_rev_no);
				rset5 = stmt5.executeQuery();
				while(rset5.next())
				{
					String agmt = rset5.getString(1)==null?"":rset5.getString(1);
					String agmt_rev = rset5.getString(2)==null?"":rset5.getString(2);
					String cont = rset5.getString(3)==null?"":rset5.getString(3);
					String cont_rev = rset5.getString(4)==null?"":rset5.getString(4);
					String cont_type = rset5.getString(5)==null?"":rset5.getString(5);
					String counterparty_cd = rset5.getString(6)==null?"":rset5.getString(6);
					entity_cd = rset5.getString(7)==null?"":rset5.getString(7);

					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				rset5.close();
				stmt5.close();

				V_DEAL_NO.add(dealNo);
				V_DIS_CONTRACT_TYPE.add(disp_cont_type);
				V_DEAL_DTL.add(deal_dtl);

				int days_left=utilDate.getDays(expire_dt, today_date)-1;

				V_PREVIOUS_DT.add(days_left);
			}
			rset4.close();
			stmt4.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Outgoing_Security_Status_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Outgoing_Security_Status_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Outgoing Security Status Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Outgoing Security Status Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Outgoing Security Status Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("KYC Outgoing Security Status Report");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));

			HSSFRow rowhead1 = sheet.createRow((short)1);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Legal Entity");
			rowhead1.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead1.createCell((short) 3).setCellValue("Issuing Bank Ref#");  
			rowhead1.createCell((short) 4).setCellValue("Contract Type");
			rowhead1.createCell((short) 5).setCellValue("Contract#");
			rowhead1.createCell((short) 6).setCellValue("Security Ref#");
			rowhead1.createCell((short) 7).setCellValue("Security Type");
			rowhead1.createCell((short) 8).setCellValue("Security Value(INR)"); 
			rowhead1.createCell((short) 9).setCellValue("Security Value(USD)"); 
			rowhead1.createCell((short) 10).setCellValue("Day Before Expiry Date");
			rowhead1.createCell((short) 11).setCellValue("Expiry Date");
			rowhead1.createCell((short) 12).setCellValue("Status");
			rowhead1.createCell((short) 13).setCellValue("Remarks");
			rowhead1.createCell((short) 14).setCellValue("Form C");

			int index = 0;
			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					index++;

					HSSFRow row = sheet.createRow((short)i+2);  
					row.createCell((short) 0).setCellValue(j); 
					row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+VISS_BANK_REF.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VDEAL_NO.elementAt(i));
					row.createCell((short) 6).setCellValue(""+VSEC_REF_NO.elementAt(i));
					row.createCell((short) 7).setCellValue(""+VSEC_TYPE.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+VVALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VVALUE_USD.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VPREVIOUS_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VEXPIRE_DT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VSTATUS.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VREMARK.elementAt(i));
					row.createCell((short) 14).setCellValue("");
					VINDEX.add(index);
				}
			}
			else
			{
				index++;
				VINDEX.add(index);
			}

			HSSFRow row_head = sheet.createRow((short)VINDEX.size()+3);
			row_head.createCell((short) 0).setCellValue("IGX Outgoing Security Status Report");
			sheet.addMergedRegion(new CellRangeAddress(VINDEX.size()+3,VINDEX.size()+3,0,13));

			HSSFRow rowhead = sheet.createRow((short)VINDEX.size()+4); 
			rowhead.createCell((short) 0).setCellValue("Sr#");
			rowhead.createCell((short) 1).setCellValue("Legal Entity");
			rowhead.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead.createCell((short) 3).setCellValue("Issuing Bank Ref#");  
			rowhead.createCell((short) 4).setCellValue("Contract Type");
			rowhead.createCell((short) 5).setCellValue("Contract#");
			rowhead.createCell((short) 6).setCellValue("Security Ref#");
			rowhead.createCell((short) 7).setCellValue("Security Type");
			rowhead.createCell((short) 8).setCellValue("Security Value(INR)"); 
			rowhead.createCell((short) 9).setCellValue("Security Value(USD)"); 
			rowhead.createCell((short) 10).setCellValue("Day Before Expiry Date");
			rowhead.createCell((short) 11).setCellValue("Expiry Date");
			rowhead.createCell((short) 12).setCellValue("Status");
			rowhead.createCell((short) 13).setCellValue("Remarks");
			rowhead.createCell((short) 14).setCellValue("Form C");

			if(V_COUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < V_COUNTERPARTY_NM.size(); i++ )
				{
					j++;

					HSSFRow row = sheet.createRow((short)VINDEX.size()+i+5);
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+V_LEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+V_COUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+V_ISS_BANK_REF.elementAt(i));
					row.createCell((short) 4).setCellValue(""+V_DIS_CONTRACT_TYPE.elementAt(i));
					row.createCell((short) 5).setCellValue(""+V_DEAL_NO.elementAt(i));
					row.createCell((short) 6).setCellValue(""+V_SEC_REF_NO.elementAt(i));
					row.createCell((short) 7).setCellValue(""+V_SEC_TYPE.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+V_VALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+V_VALUE_USD.elementAt(i));
					row.createCell((short) 10).setCellValue(""+V_PREVIOUS_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+V_EXPIRE_DT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+V_STATUS.elementAt(i));
					row.createCell((short) 13).setCellValue(""+V_REMARK.elementAt(i));
					row.createCell((short) 14).setCellValue("");
				}
			}

			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 13; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			if(!to_mail.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail, subject, mailBody, filename, cc_mail, "");
				System.out.println(mail_msg);
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getUpcomingSecurityDtls() 
	{
		String function_nm="getUpcomingSecurityDtls()";
		String mailBody="";
		try
		{
			queryString1 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , DEAL_TYPE, GX, COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND (STATUS = ? OR STATUS = ? ) AND SEC_TYPE!=? AND "
					+ "(TO_DATE(EXPIRE_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(CANCEL_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL) "
					+ "ORDER BY ENT_DT DESC";
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt1.setString(1, "K");
			stmt1.setString(2, "P");
			stmt1.setString(3, "A");
			stmt1.setString(4, "OA");
			stmt1.setString(5, today_date);
			stmt1.setString(6, today_date);
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String counterPartyCd = rset1.getString(1)==null?"":rset1.getString(1);
				String sec_categry = rset1.getString(2)==null?"":rset1.getString(2);
				String sec_type = rset1.getString(3)==null?"":rset1.getString(3);
				String sec_ref_no = rset1.getString(4)==null?"":rset1.getString(4);
				String status = rset1.getString(5)==null?"":rset1.getString(5);
				String currency = rset1.getString(6)==null?"1":rset1.getString(6);
				String value = rset1.getString(7)==null?"":rset1.getString(7);
				String seq_no = rset1.getString(8)==null?"":rset1.getString(8);
				String expire_dt = rset1.getString(9)==null?"":rset1.getString(9);
				String remark = rset1.getString(10)==null?"":rset1.getString(10);
				String seq_rev_no = rset1.getString(11)==null?"":rset1.getString(11);
				String due_dt = rset1.getString(12)==null?"":rset1.getString(12);
				String deal_type = rset1.getString(13)==null?"":rset1.getString(13);
				String clearance = rset1.getString(14)==null?"":rset1.getString(14);
				String company_cd = rset1.getString(15)==null?"":rset1.getString(15);

				String ref_no = company_cd+"-"+sec_ref_no;
				VCOUNTERPARTY_CD.add(counterPartyCd);
				VSEC_TYPE.add(sec_type);
				VSEC_REF_NO.add(ref_no);
				if(status.equals("P"))
				{
					VSTATUS.add("Pending");
				}
				else if(status.equals("A"))
				{
					VSTATUS.add("Pending For Amendment");
				}
				VVALUE.add(value);
				VEXPIRE_DT.add(expire_dt);
				VREMARK.add(remark);
				if(clearance.equals("I"))
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
					VCLEARANCE.add("IGX");
				}
				else
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
					VCLEARANCE.add("KYC");
				}
				VSEC_CATEGRY.add(sec_categry);
				if(currency.equals("1"))
				{
					VCURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					VCURRENCY.add("USD");
				}

				VDUE_DT.add(due_dt);
				VDEAL_TYPE.add(deal_type);
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));

				String agmt = "";
				String agmt_rev = "";
				String cont = "";
				String cont_rev = "";
				String cont_type = "";
				String disp_cont_type="";
				String disDt = "";
				String countpty_cd = "";
				String entity_cd = "";
				String dis_dt="";
				String account = "";
				String temp_account = "";
				String deal_dtl = "";
				String dealNo = "";
				queryString2 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=?  AND SEQ_REV_NO=? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterPartyCd);
				stmt2.setString(3, seq_no);
				stmt2.setString(4, sec_ref_no);
				stmt2.setString(5, seq_rev_no);
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					agmt = rset2.getString(1)==null?"":rset2.getString(1);
					agmt_rev = rset2.getString(2)==null?"":rset2.getString(2);
					cont = rset2.getString(3)==null?"":rset2.getString(3);
					cont_rev = rset2.getString(4)==null?"":rset2.getString(4);
					cont_type = rset2.getString(5)==null?"":rset2.getString(5);
					countpty_cd = rset2.getString(6)==null?"":rset2.getString(6);
					entity_cd = rset2.getString(7)==null?"":rset2.getString(7);


					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterPartyCd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterPartyCd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}

					if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("W") || cont_type.equals("E") || cont_type.equals("F") || cont_type.equals("B") || cont_type.equals("M"))
					{
						temp_account = "Sell";
						if(!account.equals(""))
						{
							account+=", Sell";
						}
						else
						{
							account+="Sell";
						}
					}
					else if(cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("N") || cont_type.equals("T") || cont_type.equals("V"))
					{
						temp_account="Buy";
						if(!account.equals(""))
						{
							account+=", Buy";
						}
						else
						{
							account+="Buy";
						}
					}

					int cnt=0;
					if(temp_account.equals("Sell"))
					{
						queryString="SELECT TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND COUNTERPARTY_CD=? ";
					}
					else if(temp_account.equals("Buy"))
					{
						queryString="SELECT TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
								+ "FROM FMS_TRADER_CONT_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND COUNTERPARTY_CD=? ";
					}
					stmt = conn.prepareStatement(queryString);
					stmt.setString(++cnt, company_cd);
					stmt.setString(++cnt, agmt);
					stmt.setString(++cnt, agmt_rev);
					stmt.setString(++cnt, cont);
					stmt.setString(++cnt, cont_rev);
					stmt.setString(++cnt, cont_type);
					if(cont_type.equals("I") || cont_type.equals("X"))
					{
						stmt.setString(++cnt, entity_cd);
					}
					else
					{
						stmt.setString(++cnt, countpty_cd);
					}
					rset = stmt.executeQuery();
					while(rset.next())
					{
						String temp_dis_dt=rset.getString(1)==null?"":rset.getString(1);

						if(!disDt.equals(""))
						{
							disDt+=", "+temp_dis_dt;
						}
						else 
						{
							disDt+=temp_dis_dt;
						}
					}
					rset.close();
					stmt.close();
				}
				VDEAL_NO.add(dealNo);
				VDIS_CONTRACT_TYPE.add(disp_cont_type);
				VDEAL_DTL.add(deal_dtl);
				VDIS_DT.add(disDt);
				VACCOUNT.add(account);

				rset2.close();
				stmt2.close();
			}
			rset1.close();
			stmt1.close();

			queryString3 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , DEAL_TYPE, GX,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "GX=? AND (STATUS = ? OR STATUS = ? ) AND SEC_TYPE!=? AND "
					+ "(TO_DATE(EXPIRE_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(CANCEL_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL) "
					+ "ORDER BY ENT_DT DESC";
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			stmt3.setString(1, "I");
			stmt3.setString(2, "P");
			stmt3.setString(3, "A");
			stmt3.setString(4, "OA");
			stmt3.setString(5, today_date);
			stmt3.setString(6, today_date);
			rset3 = stmt3.executeQuery();
			while(rset3.next())
			{
				String counterPartyCd = rset3.getString(1)==null?"":rset3.getString(1);
				String sec_categry = rset3.getString(2)==null?"":rset3.getString(2);
				String sec_type = rset3.getString(3)==null?"":rset3.getString(3);
				String sec_ref_no = rset3.getString(4)==null?"":rset3.getString(4);
				String status = rset3.getString(5)==null?"":rset3.getString(5);
				String currency = rset3.getString(6)==null?"1":rset3.getString(6);
				String value = rset3.getString(7)==null?"":rset3.getString(7);
				String seq_no = rset3.getString(8)==null?"":rset3.getString(8);
				String expire_dt = rset3.getString(9)==null?"":rset3.getString(9);
				String remark = rset3.getString(10)==null?"":rset3.getString(10);
				String seq_rev_no = rset3.getString(11)==null?"":rset3.getString(11);
				String due_dt = rset3.getString(12)==null?"":rset3.getString(12);
				String deal_type = rset3.getString(13)==null?"":rset3.getString(13);
				String clearance = rset3.getString(14)==null?"":rset3.getString(14);
				String company_cd = rset3.getString(15)==null?"":rset3.getString(15);

				String ref_no = company_cd+"-"+sec_ref_no;
				V_COUNTERPARTY_CD.add(counterPartyCd);
				V_SEC_TYPE.add(sec_type);
				V_SEC_REF_NO.add(ref_no);
				if(status.equals("P"))
				{
					V_STATUS.add("Pending");
				}
				else if(status.equals("A"))
				{
					V_STATUS.add("Pending For Amendment");
				}
				V_VALUE.add(value);
				V_EXPIRE_DT.add(expire_dt);
				V_REMARK.add(remark);
				if(clearance.equals("I"))
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					V_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					V_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				V_SEC_CATEGRY.add(sec_categry);
				if(currency.equals("1"))
				{
					V_CURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					V_CURRENCY.add("USD");
				}

				V_DUE_DT.add(due_dt);
				V_DEAL_TYPE.add(deal_type);
				V_LEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));

				String agmt = "";
				String agmt_rev = "";
				String cont = "";
				String cont_rev = "";
				String cont_type = "";
				String disp_cont_type="";
				String disDt = "";
				String countpty_cd = "";
				String entity_cd = "";
				String dis_dt="";
				String account = "";
				String temp_account = "";
				String deal_dtl = "";
				String dealNo = "";
				queryString4 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=?  AND SEQ_REV_NO=? ";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1, company_cd);
				stmt4.setString(2, counterPartyCd);
				stmt4.setString(3, seq_no);
				stmt4.setString(4, sec_ref_no);
				stmt4.setString(5, seq_rev_no);
				rset4 = stmt4.executeQuery();
				while(rset4.next())
				{
					agmt = rset4.getString(1)==null?"":rset4.getString(1);
					agmt_rev = rset4.getString(2)==null?"":rset4.getString(2);
					cont = rset4.getString(3)==null?"":rset4.getString(3);
					cont_rev = rset4.getString(4)==null?"":rset4.getString(4);
					cont_type = rset4.getString(5)==null?"":rset4.getString(5);
					countpty_cd = rset4.getString(6)==null?"":rset4.getString(6);
					entity_cd = rset4.getString(7)==null?"":rset4.getString(7);


					if(clearance.equals("I"))
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							//dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterPartyCd;
							//dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterPartyCd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}

					if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("W") || cont_type.equals("E") || cont_type.equals("F") || cont_type.equals("B") || cont_type.equals("M"))
					{
						temp_account = "Sell";
						if(!account.equals(""))
						{
							account+=", Sell";
						}
						else
						{
							account+="Sell";
						}
					}
					else if(cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("N") || cont_type.equals("T") || cont_type.equals("V"))
					{
						temp_account="Buy";
						if(!account.equals(""))
						{
							account+=", Buy";
						}
						else
						{
							account+="Buy";
						}
					}

					if(temp_account.equals("Sell"))
					{
						queryString="SELECT TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND COUNTERPARTY_CD=? ";
					}
					else if(temp_account.equals("Buy"))
					{
						queryString="SELECT TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
								+ "FROM FMS_TRADER_CONT_MST A "
								+ "WHERE COMPANY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? "
								+ "AND COUNTERPARTY_CD=? ";
					}
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, agmt);
					stmt.setString(3, agmt_rev);
					stmt.setString(4, cont);
					stmt.setString(5, cont_rev);
					stmt.setString(6, cont_type);
					if(cont_type.equals("I") || cont_type.equals("X"))
					{
						stmt.setString(7, entity_cd);
					}
					else
					{
						stmt.setString(7, countpty_cd);
					}
					rset = stmt.executeQuery();
					while(rset.next())
					{
						String temp_dis_dt=rset.getString(1)==null?"":rset.getString(1);

						if(!disDt.equals(""))
						{
							disDt+=", "+temp_dis_dt;
						}
						else 
						{
							disDt+=temp_dis_dt;
						}
					}
					rset.close();
					stmt.close();
				}
				V_DEAL_NO.add(dealNo);
				V_DIS_CONTRACT_TYPE.add(disp_cont_type);
				V_DEAL_DTL.add(deal_dtl);
				V_DIS_DT.add(disDt);
				V_ACCOUNT.add(account);

				rset4.close();
				stmt4.close();
			}
			rset3.close();
			stmt3.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Upcoming_Security_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Upcoming_Security_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Upcoming Security Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Upcoming Security Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Upcoming Security Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("KYC Upcoming Security Report");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));

			HSSFRow rowhead1 = sheet.createRow((short)1);
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Legal Entity"); 
			rowhead1.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead1.createCell((short) 3).setCellValue("Security Type");  
			rowhead1.createCell((short) 4).setCellValue("Security Ref#");
			rowhead1.createCell((short) 5).setCellValue("Deal Type");
			rowhead1.createCell((short) 6).setCellValue("Contract Type");
			rowhead1.createCell((short) 7).setCellValue("Contract#");
			rowhead1.createCell((short) 8).setCellValue("Security Value"); 
			rowhead1.createCell((short) 9).setCellValue("Currency"); 
			rowhead1.createCell((short) 10).setCellValue("Security Due Date");
			rowhead1.createCell((short) 11).setCellValue("Buy/Sell");
			rowhead1.createCell((short) 12).setCellValue("Discharge Date");
			rowhead1.createCell((short) 13).setCellValue("Status");
			rowhead1.createCell((short) 14).setCellValue("Remarks");

			int index = 0;
			if(VCOUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < VCOUNTERPARTY_NM.size(); i++ )
				{
					j++;
					index++;
					HSSFRow row = sheet.createRow((short)i+2);  
					row.createCell((short) 0).setCellValue(j);
					row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+VSEC_TYPE.elementAt(i));
					row.createCell((short) 4).setCellValue(""+VSEC_REF_NO.elementAt(i));
					row.createCell((short) 5).setCellValue(""+VDEAL_TYPE.elementAt(i));
					row.createCell((short) 6).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(i));  
					row.createCell((short) 7).setCellValue(""+VDEAL_NO.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+VVALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+VCURRENCY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+VDUE_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+VACCOUNT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+VDIS_DT.elementAt(i));
					row.createCell((short) 13).setCellValue(""+VSTATUS.elementAt(i));
					row.createCell((short) 14).setCellValue(""+VREMARK.elementAt(i));
					VINDEX.add(index);
				}
			}
			else
			{
				index++;
				VINDEX.add(index);
			}

			HSSFRow row_head = sheet.createRow((short)VINDEX.size()+3);
			row_head.createCell((short) 0).setCellValue("IGX Upcoming Security Report");
			sheet.addMergedRegion(new CellRangeAddress(VINDEX.size()+3,VINDEX.size()+3,0,13));

			HSSFRow rowhead = sheet.createRow((short)VINDEX.size()+4);
			rowhead.createCell((short) 0).setCellValue("Sr#");
			rowhead.createCell((short) 1).setCellValue("Legal Entity"); 
			rowhead.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead.createCell((short) 3).setCellValue("Security Type");  
			rowhead.createCell((short) 4).setCellValue("Security Ref#");
			rowhead.createCell((short) 5).setCellValue("Deal Type");
			rowhead.createCell((short) 6).setCellValue("Contract Type");
			rowhead.createCell((short) 7).setCellValue("Contract#");
			rowhead.createCell((short) 8).setCellValue("Security Value"); 
			rowhead.createCell((short) 9).setCellValue("Currency"); 
			rowhead.createCell((short) 10).setCellValue("Security Due Date");
			rowhead.createCell((short) 11).setCellValue("Buy/Sell");
			rowhead.createCell((short) 12).setCellValue("Discharge Date");
			rowhead.createCell((short) 13).setCellValue("Status");
			rowhead.createCell((short) 14).setCellValue("Remarks");

			if(V_COUNTERPARTY_NM.size() > 0)
			{
				int j=0;
				for(int i = 0; i < V_COUNTERPARTY_NM.size(); i++ )
				{
					j++;

					HSSFRow row = sheet.createRow((short)VINDEX.size()+i+5);
					row.createCell((short) 0).setCellValue(j);  
					row.createCell((short) 1).setCellValue(""+V_LEGAL_ENTITY.elementAt(i));  
					row.createCell((short) 2).setCellValue(""+V_COUNTERPARTY_NM.elementAt(i));  
					row.createCell((short) 3).setCellValue(""+V_SEC_TYPE.elementAt(i));
					row.createCell((short) 4).setCellValue(""+V_SEC_REF_NO.elementAt(i));
					row.createCell((short) 5).setCellValue(""+V_DEAL_TYPE.elementAt(i));
					row.createCell((short) 6).setCellValue(""+V_DIS_CONTRACT_TYPE.elementAt(i));  
					row.createCell((short) 7).setCellValue(""+V_DEAL_NO.elementAt(i));  
					row.createCell((short) 8).setCellValue(""+V_VALUE.elementAt(i));
					row.createCell((short) 9).setCellValue(""+V_CURRENCY.elementAt(i));
					row.createCell((short) 10).setCellValue(""+V_DUE_DT.elementAt(i));
					row.createCell((short) 11).setCellValue(""+V_ACCOUNT.elementAt(i));
					row.createCell((short) 12).setCellValue(""+V_DIS_DT.elementAt(i));
					row.createCell((short) 13).setCellValue(""+V_STATUS.elementAt(i));
					row.createCell((short) 14).setCellValue(""+V_REMARK.elementAt(i));
				}
			}

			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 13; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Upcoming Security Report","Risk Mgmt","Weekly","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Upcoming Security Report","Risk Mgmt","Weekly","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Upcoming Security Report Mail Done(Weekly).....");
			}

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getIGXContractListforMargin() 
	{
		String function_nm="getIGXContractListforMargin()";
		try
		{
			queryString ="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TCQ, "
					+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),ENT_BY, "
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),DCQ,AGMT_BASE,POST_MARGIN,COMPANY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND "
					+ "CONTRACT_TYPE=? AND A.START_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.END_DT >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "X");
			stmt.setString(3, today_date);
			stmt.setString(4, today_date);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String start_dt = rset.getString(1)==null?"":rset.getString(1);
				String end_dt = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String counterpty_cd = rset.getString(8)==null?"":rset.getString(8);
				String tcq = rset.getString(9)==null?"":rset.getString(9);
				String cont_ref_no = rset.getString(10)==null?"":rset.getString(10);
				String trade_ref_no = rset.getString(11)==null?"":rset.getString(11);
				String signing_dt = rset.getString(12)==null?"":rset.getString(12);
				double rate = rset.getDouble(13);
				String rate_unit = rset.getString(14)==null?"":rset.getString(14);

				String fcc_by = rset.getString(15)==null?"":rset.getString(15);
				String fcc_dt = rset.getString(16)==null?"":rset.getString(16);
				String ent_by = rset.getString(17)==null?"":rset.getString(17);
				String ent_dt = rset.getString(18)==null?"":rset.getString(18);
				String dcq = rset.getString(19)==null?"":rset.getString(19);
				String agmt_base = rset.getString(20)==null?"":rset.getString(20);
				String post_margin = rset.getString(21)==null?"":rset.getString(21);
				String company_cd = rset.getString(22)==null?"":rset.getString(22);

				String contPriceMapping=counterpty_cd+"-"+agmt+"-"+cont;
				double exchngRate=getExchangeRate(company_cd, counterpty_cd, agmt, cont, cont_type, today_date);
				if(rate_unit.equals("2"))
				{
					if(exchngRate>0)
					{
						rate=rate*exchngRate;
					}
				}

				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONTRACT_TYPE.add(cont_type);
				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VTCQ.add(tcq);
				VDCQ.add(dcq);
				VCONT_REF_NO.add(cont_ref_no);
				VTRADE_REF_NO.add(trade_ref_no);
				VSIGNING_DT.add(signing_dt);
				VRATE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VRATE_UNIT.add(utilBean.getRateUnitNm(conn,rate_unit));
				VFCC_BY.add(fcc_by);
				VFCC_DT.add(fcc_dt);
				VAGMT_BASE.add(agmt_base);
				VPOST_MARGIN.add(post_margin);
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));

				//String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String displayDealNum=utilBean.NewDealMappingId(company_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				if(agmt_base.equals("D"))
				{
					displayDealNum+=" [DLV]";
				}
				VDISPLAY_DEAL_MAP.add(displayDealNum);
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));

				VACCOUNT.add("SELL");

				String price_type = "";
				queryString2="SELECT PRICE_TYPE "
						+ "FROM FMS_CONT_PRICE_DTL "
						+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, contPriceMapping);
				stmt2.setString(3, cont_type);
				stmt2.setString(4, today_date);
				stmt2.setString(5, today_date);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					price_type=rset2.getString(1)==null?"":rset2.getString(1);
				}
				if(price_type.equals("F"))
				{
					VPRICE_TYPE.add("FIXED : Variable");
				}
				else
				{
					VPRICE_TYPE.add("FIXED");
				}
				rset2.close();
				stmt2.close();

				String tax="";

				queryString4="SELECT TAX_STR_CD "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_TAX_STRUCT_DTL B "
						+ "WHERE "//A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? "
						+ "AND B.PLANT_SEQ_NO IN (SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND CONTRACT_TYPE=? ) "
						+ "AND B.BU_UNIT IN (SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND CONTRACT_TYPE=? ) "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "GROUP BY A.TAX_STR_CD, A.APP_DATE";
				stmt4 = conn.prepareStatement(queryString4);
				//stmt4.setString(1, comp_cd);
				stmt4.setString(1, "C");
				stmt4.setString(2, counterpty_cd);
				stmt4.setString(3, company_cd);
				stmt4.setString(4, counterpty_cd);
				stmt4.setString(5, agmt);
				stmt4.setString(6, agmt_rev);
				stmt4.setString(7, cont);
				stmt4.setString(8, cont_rev);
				stmt4.setString(9, cont_type);
				stmt4.setString(10, company_cd);
				stmt4.setString(11, counterpty_cd);
				stmt4.setString(12, agmt);
				stmt4.setString(13, agmt_rev);
				stmt4.setString(14, cont);
				stmt4.setString(15, cont_rev);
				stmt4.setString(16, cont_type);
				stmt4.setString(17, today_date);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					String tax_structure_cd=rset4.getString(1);

					queryString_temp="SELECT DESCR "
							+ "FROM FMS_TAX_STRUCTURE "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "TAX_STR_CD=?";
					stmt6 = conn.prepareStatement(queryString_temp);
					//stmt6.setString(1, comp_cd);
					stmt6.setString(1, tax_structure_cd);
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						tax=rset6.getString(1)==null?"":rset6.getString(1);
					}
					rset6.close();
					stmt6.close();
				}
				VTAX.add(tax);
				rset4.close();
				stmt4.close();
			}
			rset.close();
			stmt.close();

			queryString1 ="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TCQ, "
					+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),ENT_BY, "
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),DCQ,AGMT_BASE,POST_MARGIN,COMPANY_CD "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND "
					+ "CONTRACT_TYPE=? AND START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);	
			stmt1.setString(2, "I");	
			stmt1.setString(3, today_date);	
			stmt1.setString(4, today_date);	
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String start_dt = rset1.getString(1)==null?"":rset1.getString(1);
				String end_dt = rset1.getString(2)==null?"":rset1.getString(2);
				String agmt = rset1.getString(3)==null?"":rset1.getString(3);
				String agmt_rev = rset1.getString(4)==null?"":rset1.getString(4);
				String cont = rset1.getString(5)==null?"":rset1.getString(5);
				String cont_rev = rset1.getString(6)==null?"":rset1.getString(6);
				String cont_type = rset1.getString(7)==null?"":rset1.getString(7);
				String counterpty_cd = rset1.getString(8)==null?"":rset1.getString(8);
				String tcq = rset1.getString(9)==null?"":rset1.getString(9);
				String cont_ref_no = rset1.getString(10)==null?"":rset1.getString(10);
				String trade_ref_no = rset1.getString(11)==null?"":rset1.getString(11);
				String signing_dt = rset1.getString(12)==null?"":rset1.getString(12);
				double rate = rset1.getDouble(13);
				String rate_unit = rset1.getString(14)==null?"":rset1.getString(14);
				String  company_cd = rset1.getString(22)==null?"":rset1.getString(22);

				double exchngRate=getExchangeRate(company_cd, counterpty_cd, agmt, cont, cont_type, today_date);
				if(rate_unit.equals("2"))
				{
					if(exchngRate>0)
					{
						rate=rate*exchngRate;
					}
				}
				String fcc_by = rset1.getString(15)==null?"":rset1.getString(15);
				String fcc_dt = rset1.getString(16)==null?"":rset1.getString(16);
				String ent_by = rset1.getString(17)==null?"":rset1.getString(17);
				String ent_dt = rset1.getString(18)==null?"":rset1.getString(18);
				String dcq = rset1.getString(19)==null?"":rset1.getString(19);
				String agmt_base = rset1.getString(20)==null?"":rset1.getString(20);
				String post_margin = rset1.getString(21)==null?"":rset1.getString(21);

				String contPriceMapping=counterpty_cd+"-"+agmt+"-"+cont;

				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONTRACT_TYPE.add(cont_type);
				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_cd));
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VTCQ.add(tcq);
				VDCQ.add(dcq);
				VCONT_REF_NO.add(cont_ref_no);
				VTRADE_REF_NO.add(trade_ref_no);
				VSIGNING_DT.add(signing_dt);
				VRATE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VRATE_UNIT.add(utilBean.getRateUnitNm(conn,rate_unit));
				VFCC_BY.add(fcc_by);
				VFCC_DT.add(fcc_dt);
				VAGMT_BASE.add(agmt_base);
				VPOST_MARGIN.add(post_margin);
				VLEGAL_ENTITY.add(utilBean.getCompanyAbbr(conn,company_cd));

				//String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String displayDealNum=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				if(agmt_base.equals("D"))
				{
					displayDealNum+=" [DLV]";
				}
				VDISPLAY_DEAL_MAP.add(displayDealNum);
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));

				VACCOUNT.add("BUY");

				String price_type = "";
				queryString3="SELECT PRICE_TYPE "
						+ "FROM FMS_CONT_PRICE_DTL "
						+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, company_cd);
				stmt3.setString(2, contPriceMapping);
				stmt3.setString(3, cont_type);
				stmt3.setString(4, today_date);
				stmt3.setString(5, today_date);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					price_type=rset3.getString(1)==null?"":rset3.getString(1);
				}
				if(price_type.equals("F"))
				{
					VPRICE_TYPE.add("FIXED : Variable");
				}
				else
				{
					VPRICE_TYPE.add("FIXED");
				}
				rset3.close();
				stmt3.close();

				String tax=""; 
				queryString5="SELECT MAX(SUM(DISTINCT A.FACTOR)) "
						+ "FROM FMS_TAX_STRUCTURE_DTL A, FMS_ENTITY_TAX_STRUCT_DTL B "
						+ "WHERE "//A.COMPANY_CD=? AND A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.TAX_STR_CD=B.TAX_STRUCT_CD "//AND A.APP_DATE=B.TAX_STRUCT_DT "
						+ "AND B.ENTITY=? AND B.COUNTERPARTY_CD=? "
						+ "AND B.PLANT_SEQ_NO IN (SELECT PLANT_SEQ_NO FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND CONTRACT_TYPE=? ) "
						+ "AND B.BU_UNIT IN (SELECT PLANT_SEQ_NO FROM FMS_TRADER_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND CONTRACT_TYPE=? ) "
						+ "AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL C "
						+ "WHERE C.ENTITY=B.ENTITY AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND C.BU_UNIT=B.BU_UNIT AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "GROUP BY A.TAX_STR_CD, A.APP_DATE";
				stmt5 = conn.prepareStatement(queryString5);
				//stmt5.setString(1, comp_cd);
				stmt5.setString(1, "C");
				stmt5.setString(2, counterpty_cd);
				stmt5.setString(3, company_cd);
				stmt5.setString(4, counterpty_cd);
				stmt5.setString(5, agmt);
				stmt5.setString(6, agmt_rev);
				stmt5.setString(7, cont);
				stmt5.setString(8, cont_rev);
				stmt5.setString(9, cont_type);
				stmt5.setString(10, company_cd);
				stmt5.setString(11, counterpty_cd);
				stmt5.setString(12, agmt);
				stmt5.setString(13, agmt_rev);
				stmt5.setString(14, cont);
				stmt5.setString(15, cont_rev);
				stmt5.setString(16, cont_type);
				stmt5.setString(17, today_date);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					String tax_structure_cd=rset5.getString(1)==null?"":rset5.getString(1);

					queryString3="SELECT DESCR "
							+ "FROM FMS_TAX_STRUCTURE "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "TAX_STR_CD=?";
					stmt3 = conn.prepareStatement(queryString3);
					//stmt3.setString(1, comp_cd);
					stmt3.setString(1, tax_structure_cd);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						tax=rset3.getString(1)==null?"":rset3.getString(1);
					}
					rset3.close();
					stmt3.close();
				}
				VTAX.add(tax);
				rset5.close();
				stmt5.close();
			}
			rset1.close();
			stmt1.close();

			for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
			{
				String date="",date1="";
				date = nextDaydate(""+VEND_DT.elementAt(i),"3");

				VBG_DROPOFF_DT.add(date);

				double post_trade_margin = Double.parseDouble(""+VPOST_MARGIN.elementAt(i));
				double tcq = Double.parseDouble(""+VTCQ.elementAt(i));
				double sales_rate = Double.parseDouble(""+VRATE.elementAt(i));

				double trade_value = tcq * sales_rate;
				double post_margin_value = (trade_value * post_trade_margin) / 100;
				VTRADE_VALUE.add(nf.format(trade_value));
				VPOST_TRADE_MARGIN.add(nf.format(post_margin_value));
			}
			bg_value=nf.format(getSecurityValue(today_date));
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getIgxContractListforMarginforSevenDay() 
	{
		String function_nm="getIgxContractListforMarginforSevenDay()";
		try
		{
			HashMap<String, String> dealPraSell = new HashMap<String, String>();
			HashMap<String, String> dealPraBuy = new HashMap<String, String>();

			for(int i=-7;i<8;i++)
			{
				String report_dt="";
				queryString = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, today_date);
				stmt.setInt(2, i);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					report_dt=rset.getString(1)==null?"":rset.getString(1);
				}
				VTEMP_REPORT_DT.add(report_dt);
				rset.close();
				stmt.close();

				queryString ="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TCQ, "
						+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),ENT_BY, "
						+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),DCQ,AGMT_BASE,POST_MARGIN,COMPANY_CD "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND "
						+ "CONTRACT_TYPE=? AND A.START_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.END_DT+3 >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "X");
				stmt.setString(3, today_date);
				stmt.setString(4, today_date);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					VREPORT_DT.add(report_dt);
					String start_dt = rset.getString(1)==null?"":rset.getString(1);
					String end_dt = rset.getString(2)==null?"":rset.getString(2);
					String agmt = rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
					String cont = rset.getString(5)==null?"":rset.getString(5);
					String cont_rev = rset.getString(6)==null?"":rset.getString(6);
					String cont_type = rset.getString(7)==null?"":rset.getString(7);
					String counterpty_cd = rset.getString(8)==null?"":rset.getString(8);
					String tcq = rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no = rset.getString(10)==null?"":rset.getString(10);
					String trade_ref_no = rset.getString(11)==null?"":rset.getString(11);
					String signing_dt = rset.getString(12)==null?"":rset.getString(12);
					double rate = rset.getDouble(13);
					String rate_unit = rset.getString(14)==null?"":rset.getString(14);

					
					String fcc_by = rset.getString(15)==null?"":rset.getString(15);
					String fcc_dt = rset.getString(16)==null?"":rset.getString(16);
					String ent_by = rset.getString(17)==null?"":rset.getString(17);
					String ent_dt = rset.getString(18)==null?"":rset.getString(18);
					String dcq = rset.getString(19)==null?"":rset.getString(19);
					String agmt_base = rset.getString(20)==null?"":rset.getString(20);
					String post_margin = rset.getString(21)==null?"":rset.getString(21);
					String company_cd = rset.getString(22)==null?"":rset.getString(22);

					double exchngRate=getExchangeRate(company_cd, counterpty_cd, agmt, cont, cont_type, today_date);
					if(rate_unit.equals("2"))
					{
						if(exchngRate>0)
						{
							rate=rate*exchngRate;
						}
					}

					String contPriceMapping=counterpty_cd+"-"+agmt+"-"+cont;
					//String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String displayDealNum=utilBean.NewDealMappingId(company_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");

					if(agmt_base.equals("D"))
					{
						displayDealNum+="[DLV]";
					}
					String cabbr = ""+utilBean.getCounterpartyABBR(conn,counterpty_cd);

					dealPraSell.put(counterpty_cd+"-"+displayDealNum,displayDealNum);
					V_DISPLAY_DEAL_MAP.add(displayDealNum);
					V_COUNTERPARTY_CD.add(counterpty_cd);
					V_TCQ.add(tcq);
					V_RATE.add(utilBean.RateNumberFormat(rate, rate_unit));
					V_POST_MARGIN.add(post_margin);
					V_ACCOUNT.add("SELL");
				}
				rset.close();
				stmt.close();

				queryString1 ="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TCQ, "
						+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),ENT_BY, "
						+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),DCQ,AGMT_BASE,POST_MARGIN,COMPANY_CD "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND "
						+ "CONTRACT_TYPE=? AND START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT+3 >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "I");
				stmt1.setString(3, today_date);
				stmt1.setString(4, today_date);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					VREPORT_DT.add(report_dt);
					String start_dt = rset1.getString(1)==null?"":rset1.getString(1);
					String end_dt = rset1.getString(2)==null?"":rset1.getString(2);
					String agmt = rset1.getString(3)==null?"":rset1.getString(3);
					String agmt_rev = rset1.getString(4)==null?"":rset1.getString(4);
					String cont = rset1.getString(5)==null?"":rset1.getString(5);
					String cont_rev = rset1.getString(6)==null?"":rset1.getString(6);
					String cont_type = rset1.getString(7)==null?"":rset1.getString(7);
					String counterpty_cd = rset1.getString(8)==null?"":rset1.getString(8);
					String tcq = rset1.getString(9)==null?"":rset1.getString(9);
					String cont_ref_no = rset1.getString(10)==null?"":rset1.getString(10);
					String trade_ref_no = rset1.getString(11)==null?"":rset1.getString(11);
					String signing_dt = rset1.getString(12)==null?"":rset1.getString(12);
					double rate = rset1.getDouble(13);
					String rate_unit = rset1.getString(14)==null?"":rset1.getString(14);
					String company_cd = rset1.getString(22)==null?"":rset1.getString(22);

					double exchngRate=getExchangeRate(company_cd, counterpty_cd, agmt, cont, cont_type, today_date);
					if(rate_unit.equals("2"))
					{
						if(exchngRate>0)
						{
							rate=rate*exchngRate;
						}
					}
					String fcc_by = rset1.getString(15)==null?"":rset1.getString(15);
					String fcc_dt = rset1.getString(16)==null?"":rset1.getString(16);
					String ent_by = rset1.getString(17)==null?"":rset1.getString(17);
					String ent_dt = rset1.getString(18)==null?"":rset1.getString(18);
					String dcq = rset1.getString(19)==null?"":rset1.getString(19);
					String agmt_base = rset1.getString(20)==null?"":rset1.getString(20);
					String post_margin = rset1.getString(21)==null?"":rset1.getString(21);

					String contPriceMapping=counterpty_cd+"-"+agmt+"-"+cont;
					//String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String displayDealNum=utilBean.NewDealMappingId(company_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");

					if(agmt_base.equals("D"))
					{
						displayDealNum+="[DLV]";
					}
					String cabbr = ""+utilBean.getCounterpartyABBR(conn,counterpty_cd);
					V_DISPLAY_DEAL_MAP.add(displayDealNum);
					dealPraBuy.put(counterpty_cd+"-"+displayDealNum,displayDealNum);
					V_COUNTERPARTY_CD.add(counterpty_cd);
					V_TCQ.add(tcq);
					V_RATE.add(utilBean.RateNumberFormat(rate, rate_unit));
					V_POST_MARGIN.add(post_margin);
					V_ACCOUNT.add("BUY");
				}
				rset1.close();
				stmt1.close();


				for(int j=0;j<V_COUNTERPARTY_CD.size();j++)
				{
					double post_trade_margin = Double.parseDouble(""+V_POST_MARGIN.elementAt(j));
					double tcq = Double.parseDouble(""+V_TCQ.elementAt(j));
					double sales_rate = Double.parseDouble(""+V_RATE.elementAt(j));

					double trade_value = tcq * sales_rate;
					double post_margin_value = (trade_value * post_trade_margin) / 100;
					VTEMP_MARGIN_USED.add(nf.format(post_margin_value));
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void getDateWiseSum() 
	{
		String function_nm="getDateWiseSum()";
		try
		{
			for(int i=0; i<VTEMP_REPORT_DT.size(); i++)
			{
				double security_val = getSecurityValue(""+VTEMP_REPORT_DT.elementAt(i));
				double margin_used = 0;

				for(int j=0; j<VREPORT_DT.size(); j++)
				{
					if(VTEMP_REPORT_DT.elementAt(i).equals(VREPORT_DT.elementAt(j)))
					{
						margin_used += Double.parseDouble(""+VTEMP_MARGIN_USED.elementAt(j));
					}
				}
				VBG_VALUE.add(nf.format(security_val));
				VMARGIN_USED.add(nf.format(margin_used));
				VMARGIN_AVAIL.add(nf.format(security_val - margin_used));
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getIGXContractListforMarginMailBody() 
	{
		String function_nm="getIGXContractListforMarginMailBody()";
		//String mailBody="";
		try
		{

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-IGX_Margining_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"IGX_Margining_Report_"+splited_sysdate+".xls";
			}

			subject=" EMS "+context+": Credit Risk IGX Margining Report dated "+today_date;

			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("IGX Margining Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk IGX Margining Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("BG Reference");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));
			row_head1.createCell((short) 9).setCellValue(bg_ref_no);
			sheet.addMergedRegion(new CellRangeAddress(0,0,9,18));

			HSSFRow row_head2 = sheet.createRow((short)1);
			row_head2.createCell((short) 0).setCellValue("BG Validity");
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,8));
			row_head2.createCell((short) 9).setCellValue(bg_validity);
			sheet.addMergedRegion(new CellRangeAddress(1,1,9,18));

			HSSFRow row_head3 = sheet.createRow((short)2);
			row_head3.createCell((short) 0).setCellValue("BG Value");
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,8));
			row_head3.createCell((short) 9).setCellValue(bg_value);
			sheet.addMergedRegion(new CellRangeAddress(2,2,9,18));

			HSSFRow row_head4 = sheet.createRow((short)3);
			row_head4.createCell((short) 0).setCellValue("Margin Used As on "+today_date);
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,8));
			row_head4.createCell((short) 9).setCellValue(""+VMARGIN_USED.elementAt(7));
			sheet.addMergedRegion(new CellRangeAddress(3,3,9,18));

			HSSFRow row_head5 = sheet.createRow((short)4);
			row_head5.createCell((short) 0).setCellValue("Margin Available As on "+today_date);
			sheet.addMergedRegion(new CellRangeAddress(4,4,0,8));
			row_head5.createCell((short) 9).setCellValue(""+VMARGIN_AVAIL.elementAt(7));
			sheet.addMergedRegion(new CellRangeAddress(4,4,9,18));

			HSSFRow rowhead1 = sheet.createRow((short)5); 
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Legal Entity"); 
			rowhead1.createCell((short) 2).setCellValue("Counterparty Name");  
			rowhead1.createCell((short) 3).setCellValue("Contract Type");  
			rowhead1.createCell((short) 4).setCellValue("Contract#");  
			rowhead1.createCell((short) 5).setCellValue("Trade Ref#");
			rowhead1.createCell((short) 6).setCellValue("Contract Ref#");
			rowhead1.createCell((short) 7).setCellValue("Buy/Sell");
			rowhead1.createCell((short) 8).setCellValue("Deal Date"); 
			rowhead1.createCell((short) 9).setCellValue("Payment Due Date"); 
			rowhead1.createCell((short) 10).setCellValue("Start Date - End Date");
			rowhead1.createCell((short) 11).setCellValue("Price Type");
			rowhead1.createCell((short) 12).setCellValue("Sales Rate (INR)");
			rowhead1.createCell((short) 13).setCellValue("Tax(%)");
			rowhead1.createCell((short) 14).setCellValue("Post Trade Margin (%)");
			rowhead1.createCell((short) 15).setCellValue("Total Volume(MMBTU)");
			rowhead1.createCell((short) 16).setCellValue("BG Dropeoff Date");
			rowhead1.createCell((short) 17).setCellValue("Trade Value(INR)");
			rowhead1.createCell((short) 18).setCellValue("Post Trade Margin(INR)");

			int k=0;
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				k++;
				HSSFRow row = sheet.createRow((short)i+6);  
				row.createCell((short) 0).setCellValue(k); 
				row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY.elementAt(i)); 
				row.createCell((short) 2).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));  
				row.createCell((short) 3).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(i));
				row.createCell((short) 4).setCellValue(""+VDISPLAY_DEAL_MAP.elementAt(i));
				row.createCell((short) 5).setCellValue(""+VTRADE_REF_NO.elementAt(i));
				row.createCell((short) 6).setCellValue(""+VCONT_REF_NO.elementAt(i));
				row.createCell((short) 7).setCellValue(""+VACCOUNT.elementAt(i));  
				row.createCell((short) 8).setCellValue(""+VSIGNING_DT.elementAt(i));
				row.createCell((short) 9).setCellValue("D+2");  
				row.createCell((short) 10).setCellValue(""+VSTART_DT.elementAt(i));  
				row.createCell((short) 11).setCellValue(""+VPRICE_TYPE.elementAt(i));
				row.createCell((short) 12).setCellValue(""+VRATE.elementAt(i));
				row.createCell((short) 13).setCellValue(""+VTAX.elementAt(i));
				row.createCell((short) 14).setCellValue(""+VPOST_MARGIN.elementAt(i));  
				row.createCell((short) 15).setCellValue(""+VTCQ.elementAt(i));
				row.createCell((short) 16).setCellValue(""+VBG_DROPOFF_DT.elementAt(i));
				row.createCell((short) 17).setCellValue(""+VTRADE_VALUE.elementAt(i));  
				row.createCell((short) 18).setCellValue(""+VPOST_TRADE_MARGIN.elementAt(i));

			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 18; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	

	public void fetchAuditReport()
	{
		String function_nm="fetchAuditReport()";
		String mailBody="";
		try
		{	

			String new_values = "";
			String old_values = "";
			String time = "";
			String update_dt = "";
			String update_by = "";
			String form_nm = "";
			String company_cd = "";
			String company_name="";

			String cp="",old_cp="";
			String name="",old_name="";
			String abbr="",old_abbr="";
			String sec_type="", old_sec_type="";
			String sec_category="" , old_sec_category="";
			String deal_type="" , old_deal_type="";
			String deal_no="" , old_deal_no="";
			String sec_value="" , old_sec_value="";
			String currency="" , old_currency="";
			String fluctuation="" , old_fluctuation="";
			String variation="" , old_variation="";
			String iss_bank_cd="" , old_iss_bank_cd="";
			String iss_bank_nm="" , old_iss_bank_nm="";
			String iss_bank_ref="" , old_iss_bank_ref="";
			String adv_bank_cd="" , old_adv_bank_cd="";
			String adv_bank_nm="" , old_adv_bank_nm="";
			String adv_bank_ref="" , old_adv_bank_ref="";
			String conf_bank_cd="" , old_conf_bank_cd="";
			String conf_bank_nm="" , old_conf_bank_nm="";
			String conf_bank_ref="" , old_conf_bank_ref="";
			String received_dt="" , old_received_dt="";
			String review_dt="" , old_review_dt="";
			String iss_date="" , old_iss_date="";
			String expire_dt="" , old_expire_dt="";
			String status="" , old_status="";
			String tenor="" , old_tenor="";
			String remark="" , old_remark="";
			String guarantor_cd="" , old_guarantor_cd="";
			String guarantor_nm="" , old_guarantor_nm="";
			String sec_ref_no="" , old_sec_ref_no="";
			String deal_dtl="";
			String operation = "";
			String security_details="", security_dtls="";
			String gx="", old_gx="";
			String reversal="", old_reversal="";
			String sap_approval="", old_sap_approval="";

			String counterparty_name="",old_counterparty_name="";
			String bank_cd="", old_bank_cd="";
			String rate_source="", old_rate_source="";
			String credit_rating ="", old_credit_rating=""; 
			String cr_status="", old_cr_status="";
			String cr_remark ="", old_cr_remark="";
			String parent_entity="", old_parent_entity="";
			String ownership ="", old_ownership=""; 
			String pEntrydate="", old_pEntrydate="";
			String pExitdate ="", old_pExitdate=""; 
			String pcr_status="", old_pcr_status="";
			String pcr_remark ="", old_pcr_remark="";
			String entity="", rd="";
			String new_limit_type ="", new_limit_action=""; 
			String new_limit_amt ="", new_limit_category=""; 
			String new_limit_eff_dt ="", new_limit_exp_dt=""; 
			String new_limit_review_dt ="", new_limit_remark=""; 
			String new_limit_status =""; 
			String limit_ref_no = "";
			String old_limit_type ="", old_limit_action=""; 
			String old_limit_amt ="", old_limit_category=""; 
			String old_limit_eff_dt ="", old_limit_exp_dt=""; 
			String old_limit_review_dt ="", old_limit_remark=""; 
			String old_limit_status =""; 

			queryString2="SELECT NEW_VALUE,OLD_VALUE,LOG_TIME,TO_CHAR(LOG_DT,'DD/MM/YYYY'),LOG_UID,FORM_NAME,COMPANY_CD "
					+ "FROM FMS_ALL_LOG "
					+ "WHERE FORM_NAME IN (?,?,?,?,?) "
					+ "AND NEW_VALUE IS NOT NULL ";
			if(!today_date.equals("")) 
			{
				queryString2+="AND LOG_DT >= TO_DATE(?,'DD/MM/YYYY') AND LOG_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString2+= " ORDER BY LOG_DT DESC, LOG_TIME DESC";
			stmt2=conn.prepareStatement(queryString2);
			//stmt2.setString(1, comp_cd);
			stmt2.setString(1, "Advance Booking");
			stmt2.setString(2, "Collateral Mgmt");
			stmt2.setString(3, "Gas Supply Contract");
			stmt2.setString(4, "Trader Contract Master");
			stmt2.setString(5, "Credit Limit Mgmt/Credit Rating");
			if(!today_date.equals("")) 
			{
				stmt2.setString(6, today_date);
				stmt2.setString(7, today_date);
			}
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				new_values = rset2.getString(1)==null?"":rset2.getString(1);
				old_values = rset2.getString(2)==null?"":rset2.getString(2);
				time = rset2.getString(3)==null?"":rset2.getString(3);
				update_dt=rset2.getString(4)==null?"":rset2.getString(4);
				update_by=rset2.getString(5)==null?"":rset2.getString(5);
				form_nm=rset2.getString(6)==null?"":rset2.getString(6);
				company_cd=rset2.getString(7)==null?"":rset2.getString(7);
				company_name=utilBean.getCompanyAbbr(conn, company_cd);

				String audit_type="";

				if(form_nm.equals("Credit Limit Mgmt/Credit Rating")) 
				{
					audit_type="limit_and_rating";
				}

				if(!new_values.equals(""))
				{	
					cp="";old_cp="";
					name="";old_name="";
					abbr="";old_abbr="";
					sec_type=""; old_sec_type="";
					sec_ref_no="" ; old_sec_ref_no="";
					security_details=""; security_dtls="";
					deal_no="" ; old_deal_no="";

					String split_New_Value[] = new_values.split("#");
					for(int i=0; i<split_New_Value.length; i++)
					{
						if(split_New_Value[i].startsWith("CP="))
						{
							String temp[] = split_New_Value[i].split("CP=");
							if(temp.length>0)
							{
								cp=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("NAME="))
						{
							String temp[] = split_New_Value[i].split("NAME=");
							if(temp.length>0)
							{
								name=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ABBR="))
						{
							String temp[] = split_New_Value[i].split("ABBR=");
							if(temp.length>0)
							{
								abbr=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SEC_TYPE="))
						{
							String temp[] = split_New_Value[i].split("SEC_TYPE=");
							if(temp.length>0)
							{
								sec_type=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SEC_CATEGORY="))
						{
							String temp[] = split_New_Value[i].split("SEC_CATEGORY=");
							if(temp.length>0)
							{
								sec_category=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DEAL_TYPE="))
						{
							String temp[] = split_New_Value[i].split("DEAL_TYPE=");
							if(temp.length>0)
							{
								deal_type=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DEAL_NO="))
						{
							String temp[] = split_New_Value[i].split("DEAL_NO=");
							if(temp.length>0)
							{
								deal_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("VALUE="))
						{
							String temp[] = split_New_Value[i].split("VALUE=");
							if(temp.length>0)
							{
								sec_value=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CURRENCY="))
						{
							String temp[] = split_New_Value[i].split("CURRENCY=");
							if(temp.length>0)
							{
								currency=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("FLUCTUATION="))
						{
							String temp[] = split_New_Value[i].split("FLUCTUATION=");
							if(temp.length>0)
							{
								fluctuation=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("VARIATION="))
						{
							String temp[] = split_New_Value[i].split("VARIATION=");
							if(temp.length>0)
							{
								variation=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ISS_BANK_CD="))
						{
							String temp[] = split_New_Value[i].split("ISS_BANK_CD=");
							if(temp.length>0)
							{
								iss_bank_cd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ISS_BANK_NAME="))
						{
							String temp[] = split_New_Value[i].split("ISS_BANK_NAME=");
							if(temp.length>0)
							{
								iss_bank_nm=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ISS_BANK_REF="))
						{
							String temp[] = split_New_Value[i].split("ISS_BANK_REF=");
							if(temp.length>0)
							{
								iss_bank_ref=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ADV_BANK_CD="))
						{
							String temp[] = split_New_Value[i].split("ADV_BANK_CD=");
							if(temp.length>0)
							{
								adv_bank_cd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ADV_BANK_NAME"))
						{
							String temp[] = split_New_Value[i].split("ADV_BANK_NAME=");
							if(temp.length>0)
							{
								adv_bank_nm=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ADV_BANK_REF="))
						{
							String temp[] = split_New_Value[i].split("ADV_BANK_REF=");
							if(temp.length>0)
							{
								adv_bank_ref=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONF_BANK_CD="))
						{
							String temp[] = split_New_Value[i].split("CONF_BANK_CD=");
							if(temp.length>0)
							{
								conf_bank_cd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONF_BANK_NAME="))
						{
							String temp[] = split_New_Value[i].split("CONF_BANK_NAME=");
							if(temp.length>0)
							{
								conf_bank_nm=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CONF_BANK_REF="))
						{
							String temp[] = split_New_Value[i].split("CONF_BANK_REF=");
							if(temp.length>0)
							{
								conf_bank_ref=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("RECIEVED_DT="))
						{
							String temp[] = split_New_Value[i].split("RECIEVED_DT=");
							if(temp.length>0)
							{
								received_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("REVIEW_DT="))
						{
							String temp[] = split_New_Value[i].split("REVIEW_DT=");
							if(temp.length>0)
							{
								review_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ISSUANCE_DT="))
						{
							String temp[] = split_New_Value[i].split("ISSUANCE_DT=");
							if(temp.length>0)
							{
								iss_date=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("EXPIRY_DT="))
						{
							String temp[] = split_New_Value[i].split("EXPIRY_DT=");
							if(temp.length>0)
							{
								expire_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("STATUS="))
						{
							String temp[] = split_New_Value[i].split("STATUS=");
							if(temp.length>0)
							{
								status=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("TENOR="))
						{
							String temp[] = split_New_Value[i].split("TENOR=");
							if(temp.length>0)
							{
								tenor=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("REMARK="))
						{
							String temp[] = split_New_Value[i].split("REMARK=");
							if(temp.length>0)
							{
								remark=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GUARANTOR_CD="))
						{
							String temp[] = split_New_Value[i].split("GUARANTOR_CD=");
							if(temp.length>0)
							{
								guarantor_cd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GUARANTOR_NM="))
						{
							String temp[] = split_New_Value[i].split("GUARANTOR_NM=");
							if(temp.length>0)
							{
								guarantor_nm=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SEC_REF_NO="))
						{
							String temp[] = split_New_Value[i].split("SEC_REF_NO=");
							if(temp.length>0)
							{
								sec_ref_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("DEAL_DTL="))
						{
							String temp[] = split_New_Value[i].split("DEAL_DTL=");
							if(temp.length>0)
							{
								deal_dtl=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GX="))
						{
							String temp[] = split_New_Value[i].split("GX=");
							if(temp.length>0)
							{
								gx=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("REVERSAL="))
						{
							String temp[] = split_New_Value[i].split("REVERSAL=");
							if(temp.length>0)
							{
								reversal=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("SAP_APPROVAL="))
						{
							String temp[] = split_New_Value[i].split("SAP_APPROVAL=");
							if(temp.length>0)
							{
								sap_approval=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("BANK_CD="))
						{
							String temp[] = split_New_Value[i].split("BANK_CD=");
							if(temp.length>0)
							{
								bank_cd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("GX="))
						{
							String temp[] = split_New_Value[i].split("GX=");
							if(temp.length>0)
							{
								gx=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("RATE_SOURCE="))
						{
							String temp[] = split_New_Value[i].split("RATE_SOURCE=");
							if(temp.length>0)
							{
								rate_source=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CREDIT_RATING="))
						{
							String temp[] = split_New_Value[i].split("CREDIT_RATING=");
							if(temp.length>0)
							{
								credit_rating=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CR_STATUS="))
						{
							String temp[] = split_New_Value[i].split("CR_STATUS=");
							if(temp.length>0)
							{
								cr_status=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("CR_REMARK="))
						{
							String temp[] = split_New_Value[i].split("CR_REMARK=");
							if(temp.length>0)
							{
								cr_remark=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("PARENT_ENTITY="))
						{
							String temp[] = split_New_Value[i].split("PARENT_ENTITY=");
							if(temp.length>0)
							{
								parent_entity=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("OWNERSHIP="))
						{
							String temp[] = split_New_Value[i].split("OWNERSHIP=");
							if(temp.length>0)
							{
								ownership=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("PENTRYDATE="))
						{
							String temp[] = split_New_Value[i].split("PENTRYDATE=");
							if(temp.length>0)
							{
								pEntrydate=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("PEXITDATE="))
						{
							String temp[] = split_New_Value[i].split("PEXITDATE=");
							if(temp.length>0)
							{
								pExitdate=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("PCR_STATUS="))
						{
							String temp[] = split_New_Value[i].split("PCR_STATUS=");
							if(temp.length>0)
							{
								pcr_status=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("PCR_REMARK="))
						{
							String temp[] = split_New_Value[i].split("PCR_REMARK=");
							if(temp.length>0)
							{
								pcr_remark=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("REF_NO="))
						{
							String temp[] = split_New_Value[i].split("REF_NO=");
							if(temp.length>0)
							{
								limit_ref_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("ENTITY="))
						{
							String temp[] = split_New_Value[i].split("ENTITY=");
							if(temp.length>0)
							{
								entity=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("RD="))
						{
							String temp[] = split_New_Value[i].split("RD=");
							if(temp.length>0)
							{
								rd=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_REF_NO="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_REF_NO=");
							if(temp.length>0)
							{
								limit_ref_no=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_TYPE="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_TYPE=");
							if(temp.length>0)
							{
								new_limit_type=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_ACTION="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_ACTION=");
							if(temp.length>0)
							{
								new_limit_action=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_CATE="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_CATE=");
							if(temp.length>0)
							{
								new_limit_category=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_AMOUNT="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_AMOUNT=");
							if(temp.length>0)
							{
								new_limit_amt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("EFF_DT="))
						{
							String temp[] = split_New_Value[i].split("EFF_DT=");
							if(temp.length>0)
							{
								new_limit_eff_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("EXP_DT="))
						{
							String temp[] = split_New_Value[i].split("EXP_DT=");
							if(temp.length>0)
							{
								new_limit_exp_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("REVIEW_DT="))
						{
							String temp[] = split_New_Value[i].split("REVIEW_DT=");
							if(temp.length>0)
							{
								new_limit_review_dt=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_STATUS="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_STATUS=");
							if(temp.length>0)
							{
								new_limit_status=temp[1];
							}
						}
						if(split_New_Value[i].startsWith("LIMIT_REMARKS="))
						{
							String temp[] = split_New_Value[i].split("LIMIT_REMARKS=");
							if(temp.length>0)
							{
								new_limit_remark=temp[1];
							}
						}
					}
					if(!old_values.equals(""))
					{
						String split_Old_Value[] = old_values.split("#");
						for(int i=0; i<split_Old_Value.length; i++)
						{
							if(split_Old_Value[i].startsWith("CP="))
							{
								String temp[] = split_Old_Value[i].split("CP=");
								if(temp.length>0)
								{
									old_cp=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("NAME="))
							{
								String temp[] = split_Old_Value[i].split("NAME=");
								if(temp.length>0)
								{
									old_name=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ABBR="))
							{
								String temp[] = split_Old_Value[i].split("ABBR=");
								if(temp.length>0)
								{
									old_abbr=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SEC_TYPE="))
							{
								String temp[] = split_Old_Value[i].split("SEC_TYPE=");
								if(temp.length>0)
								{
									old_sec_type=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SEC_CATEGORY="))
							{
								String temp[] = split_Old_Value[i].split("SEC_CATEGORY=");
								if(temp.length>0)
								{
									old_sec_category=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("DEAL_TYPE="))
							{
								String temp[] = split_Old_Value[i].split("DEAL_TYPE=");
								if(temp.length>0)
								{
									old_deal_type=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("DEAL_NO="))
							{
								String temp[] = split_Old_Value[i].split("DEAL_NO=");
								if(temp.length>0)
								{
									old_deal_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("VALUE="))
							{
								String temp[] = split_Old_Value[i].split("VALUE=");
								if(temp.length>0)
								{
									old_sec_value=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CURRENCY="))
							{
								String temp[] = split_Old_Value[i].split("CURRENCY=");
								if(temp.length>0)
								{
									old_currency=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("FLUCTUATION="))
							{
								String temp[] = split_Old_Value[i].split("FLUCTUATION=");
								if(temp.length>0)
								{
									old_fluctuation=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("VARIATION="))
							{
								String temp[] = split_Old_Value[i].split("VARIATION=");
								if(temp.length>0)
								{
									old_variation=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ISS_BANK_CD="))
							{
								String temp[] = split_Old_Value[i].split("ISS_BANK_CD=");
								if(temp.length>0)
								{
									old_iss_bank_cd=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ISS_BANK_NAME="))
							{
								String temp[] = split_Old_Value[i].split("ISS_BANK_NAME=");
								if(temp.length>0)
								{
									old_iss_bank_nm=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ISS_BANK_REF="))
							{
								String temp[] = split_Old_Value[i].split("ISS_BANK_REF=");
								if(temp.length>0)
								{
									old_iss_bank_ref=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ADV_BANK_CD="))
							{
								String temp[] = split_Old_Value[i].split("ADV_BANK_CD=");
								if(temp.length>0)
								{
									old_adv_bank_cd=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ADV_BANK_NAME"))
							{
								String temp[] = split_Old_Value[i].split("ADV_BANK_NAME=");
								if(temp.length>0)
								{
									old_adv_bank_nm=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ADV_BANK_REF="))
							{
								String temp[] = split_Old_Value[i].split("ADV_BANK_REF=");
								if(temp.length>0)
								{
									old_adv_bank_ref=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONF_BANK_CD="))
							{
								String temp[] = split_Old_Value[i].split("CONF_BANK_CD=");
								if(temp.length>0)
								{
									old_conf_bank_cd=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONF_BANK_NAME="))
							{
								String temp[] = split_Old_Value[i].split("CONF_BANK_NAME=");
								if(temp.length>0)
								{
									old_conf_bank_nm=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CONF_BANK_REF="))
							{
								String temp[] = split_Old_Value[i].split("CONF_BANK_REF=");
								if(temp.length>0)
								{
									old_conf_bank_ref=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("RECIEVED_DT="))
							{
								String temp[] = split_Old_Value[i].split("RECIEVED_DT=");
								if(temp.length>0)
								{
									old_received_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("REVIEW_DT="))
							{
								String temp[] = split_Old_Value[i].split("REVIEW_DT=");
								if(temp.length>0)
								{
									old_review_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("ISSUANCE_DT="))
							{
								String temp[] = split_Old_Value[i].split("ISSUANCE_DT=");
								if(temp.length>0)
								{
									old_iss_date=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("EXPIRY_DT="))
							{
								String temp[] = split_Old_Value[i].split("EXPIRY_DT=");
								if(temp.length>0)
								{
									old_expire_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("STATUS="))
							{
								String temp[] = split_Old_Value[i].split("STATUS=");
								if(temp.length>0)
								{
									old_status=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("TENOR="))
							{
								String temp[] = split_Old_Value[i].split("TENOR=");
								if(temp.length>0)
								{
									old_tenor=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("REMARK="))
							{
								String temp[] = split_Old_Value[i].split("REMARK=");
								if(temp.length>0)
								{
									old_remark=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GUARANTOR_CD="))
							{
								String temp[] = split_Old_Value[i].split("GUARANTOR_CD=");
								if(temp.length>0)
								{
									old_guarantor_cd=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GUARANTOR_NM="))
							{
								String temp[] = split_Old_Value[i].split("GUARANTOR_NM=");
								if(temp.length>0)
								{
									old_guarantor_nm=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SEC_REF_NO="))
							{
								String temp[] = split_Old_Value[i].split("SEC_REF_NO=");
								if(temp.length>0)
								{
									old_sec_ref_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GX="))
							{
								String temp[] = split_Old_Value[i].split("GX=");
								if(temp.length>0)
								{
									old_gx=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("REVERSAL="))
							{
								String temp[] = split_Old_Value[i].split("REVERSAL=");
								if(temp.length>0)
								{
									old_reversal=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("SAP_APPROVAL="))
							{
								String temp[] = split_Old_Value[i].split("SAP_APPROVAL=");
								if(temp.length>0)
								{
									old_sap_approval=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("BANK_CD="))
							{
								String temp[] = split_Old_Value[i].split("BANK_CD=");
								if(temp.length>0)
								{
									old_bank_cd=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("GX="))
							{
								String temp[] = split_Old_Value[i].split("GX=");
								if(temp.length>0)
								{
									old_gx=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("RATE_SOURCE="))
							{
								String temp[] = split_Old_Value[i].split("RATE_SOURCE=");
								if(temp.length>0)
								{
									old_rate_source=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CREDIT_RATING="))
							{
								String temp[] = split_Old_Value[i].split("CREDIT_RATING=");
								if(temp.length>0)
								{
									old_credit_rating=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CR_STATUS="))
							{
								String temp[] = split_Old_Value[i].split("CR_STATUS=");
								if(temp.length>0)
								{
									old_cr_status=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("CR_REMARK="))
							{
								String temp[] = split_Old_Value[i].split("CR_REMARK=");
								if(temp.length>0)
								{
									old_cr_remark=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("PARENT_ENTITY="))
							{
								String temp[] = split_Old_Value[i].split("PARENT_ENTITY=");
								if(temp.length>0)
								{
									old_parent_entity=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("OWNERSHIP="))
							{
								String temp[] = split_Old_Value[i].split("OWNERSHIP=");
								if(temp.length>0)
								{
									old_ownership=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("PENTRYDATE="))
							{
								String temp[] = split_Old_Value[i].split("PENTRYDATE=");
								if(temp.length>0)
								{
									old_pEntrydate=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("PEXITDATE="))
							{
								String temp[] = split_Old_Value[i].split("PEXITDATE=");
								if(temp.length>0)
								{
									old_pExitdate=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("PCR_STATUS="))
							{
								String temp[] = split_Old_Value[i].split("PCR_STATUS=");
								if(temp.length>0)
								{
									old_pcr_status=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("PCR_REMARK="))
							{
								String temp[] = split_Old_Value[i].split("PCR_REMARK=");
								if(temp.length>0)
								{
									old_pcr_remark=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("REF_NO="))
							{
								String temp[] = split_Old_Value[i].split("REF_NO=");
								if(temp.length>0)
								{
									old_sec_ref_no=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_TYPE="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_TYPE=");
								if(temp.length>0)
								{
									old_limit_type=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_ACTION="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_ACTION=");
								if(temp.length>0)
								{
									old_limit_action=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_CATE="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_CATE=");
								if(temp.length>0)
								{
									old_limit_category=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_AMOUNT="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_AMOUNT=");
								if(temp.length>0)
								{
									old_limit_amt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("EFF_DT="))
							{
								String temp[] = split_Old_Value[i].split("EFF_DT=");
								if(temp.length>0)
								{
									old_limit_eff_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("EXP_DT="))
							{
								String temp[] = split_Old_Value[i].split("EXP_DT=");
								if(temp.length>0)
								{
									old_limit_exp_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("REVIEW_DT="))
							{
								String temp[] = split_Old_Value[i].split("REVIEW_DT=");
								if(temp.length>0)
								{
									old_limit_review_dt=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_STATUS="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_STATUS=");
								if(temp.length>0)
								{
									old_limit_status=temp[1];
								}
							}
							if(split_Old_Value[i].startsWith("LIMIT_REMARKS="))
							{
								String temp[] = split_Old_Value[i].split("LIMIT_REMARKS=");
								if(temp.length>0)
								{
									old_limit_remark=temp[1];
								}
							}
						}
					}

					if(gx.equals("K")) 
					{

						counterparty_name = utilBean.getCounterpartyName(conn,cp);
						old_counterparty_name = utilBean.getCounterpartyName(conn,old_cp);
					}
					else if (gx.equals("I")) 
					{
						counterparty_name = utilBean.getGasExchangeName(conn,cp);
						old_counterparty_name = utilBean.getGasExchangeName(conn,old_cp);
					}

					if(cr_status.equals("Y"))
					{
						cr_status="Authorized";
					}
					else if(cr_status.equals("N"))
					{
						cr_status="Unauthorized";
					}

					if(old_cr_status.equals("Y"))
					{
						old_cr_status="Authorized";
					}
					else if(old_cr_status.equals("N"))
					{
						old_cr_status="Unauthorized";
					}

					if(pcr_status.equals("Y"))
					{
						pcr_status="Authorized";
					}
					else if(pcr_status.equals("N"))
					{
						pcr_status="Unauthorized";
					}
					if(old_pcr_status.equals("Y"))
					{
						old_pcr_status="Authorized";
					}
					else if(old_pcr_status.equals("N"))
					{
						old_pcr_status="Unauthorized";
					}

					if(status.equals("A"))
					{
						status="Pending for Amendment";
					}
					else if(status.equals("P"))
					{
						status="Pending";
					}
					else if(status.equals("O"))
					{
						status="In Order";
					}
					else if(status.equals("C"))
					{
						status="Cancelled";
					}
					else if(status.equals("R"))
					{
						status="Restated";
					}
					else if(status.equals("E"))
					{
						status="Expired";
					}

					if(old_status.equals("A"))
					{
						old_status="Pending for Amendment";
					}
					else if(old_status.equals("P"))
					{
						old_status="Pending";
					}
					else if(old_status.equals("O"))
					{
						old_status="In Order";
					}
					else if(old_status.equals("C"))
					{
						old_status="Cancelled";
					}
					else if(old_status.equals("R"))
					{
						old_status="Restated";
					}
					else if(old_status.equals("E"))
					{
						old_status="Expired";
					}

					if(sec_category.equals("R"))
					{
						sec_category="Incoming";
					}
					else if(sec_category.equals("I"))
					{
						sec_category="Outgoing";
					}

					if(old_sec_category.equals("R"))
					{
						old_sec_category="Incoming";
					}
					else if(old_sec_category.equals("I"))
					{
						old_sec_category="Outgoing";
					}

					if(currency.equals("1"))
					{
						currency="INR";
					}
					else if(currency.equals("2"))
					{
						currency="USD";
					}
					if(old_currency.equals("1"))
					{
						old_currency="INR";
					}
					else if(old_currency.equals("2"))
					{
						old_currency="USD";
					}

					if(new_limit_status.equals("Y"))
					{
						new_limit_status="Active";
					}
					else if(new_limit_status.equals("N"))
					{
						new_limit_status="Inactive";
					}
					if(old_limit_status.equals("Y"))
					{
						old_limit_status="Active";
					}
					else if(old_limit_status.equals("N"))
					{
						old_limit_status="Inactive";
					}

					if(!cp.equals(""))
					{
						if(old_values.equals("")) 
						{
							if(form_nm.equals("Credit Limit Mgmt/Credit Rating")) 
							{
								if(entity.equals("C")) 
								{
									security_dtls= "Counterparty Name : "+counterparty_name+" ( )\n";
								}
								else if(entity.equals("B")) 
								{
									security_dtls="Bank Name : "+utilBean.getBankName(conn,bank_cd)+" ( )\n";
								}
								if(rd.equals("C")) 
								{
									security_dtls+="Rating Source : "+rate_source+" ( )\n"
											+ "Credit Rating : "+credit_rating+" ( )\n"
											+ "Status : "+cr_status+" ( )\n"
											+ "Remark : "+cr_remark+" ( )\n";
								}
								else if(rd.equals("P")) 
								{
									if(entity.equals("C")) 
									{
										if(gx.equals("K")) 
										{
											security_dtls+= "Parent Leagel Entity : "+utilBean.getCounterpartyName(conn,parent_entity)+" ( )\n";
										}
										else if(gx.equals("I")) 
										{
											security_dtls+= "Parent Leagel Entity : "+utilBean.getGasExchangeName(conn,parent_entity)+" ( )\n";
										}
									}
									else if(entity.equals("B")) 
									{
										security_dtls+= "Parent Leagel Entity : "+utilBean.getBankName(conn,parent_entity)+" ( )\n";
									}
									security_dtls+= "Percent Ownership(%) : "+ownership+" ( )\n"
											+ "Parent Entry Date : "+pEntrydate+" ( )\n"
											+ "Parent Exit Date  : "+pExitdate+" ( )\n"
											+ "Status : "+pcr_status+" ( )\n"
											+ "Remark : "+pcr_remark+" ( )\n";
								}
								else if(rd.equals("L"))
								{
									security_dtls+= "Limit Type : "+new_limit_type+" ( )\n"
											+ "Limit Action : "+new_limit_action+" ( )\n"
											+ "Amount(INR) : "+new_limit_amt+" ( )\n"
											+ "Effective Date : "+new_limit_eff_dt+" ( )\n"
											+ "Expiration Date : "+new_limit_exp_dt+" ( )\n"
											+ "Next Review Date : "+new_limit_review_dt+" ( )\n"
											+ "Status : "+new_limit_status+" ( )\n"
											+ "Remark : "+new_limit_remark+" ( )\n";
								}
								VRATINGSIZE.add("limit_rating");
							}
							else 
							{
								security_details="Counterparty Name : "+name+" ( )\n"
										+ "Abbreviation : "+abbr+" ( )\n"
										+ "Security Type : "+sec_type+" ( )\n"
										+ "Category : "+sec_category+" ( )\n"
										+ "Contract# : "+deal_no+" ( )\n"
										+ "Value : "+sec_value+" ( )\n"
										+ "Currency : "+currency+" ( )\n"
										+ "Recieved Date : "+received_dt+" ( )\n"
										+ "Status : "+status+" ( )\n"
										+ "Remark : "+remark+" ( )\n";
								VSECURITYSIZE.add("Security");
							}
						}
						else
						{
							if(form_nm.equals("Credit Limit Mgmt/Credit Rating")) 
							{
								if(entity.equals("C")) 
								{
									if(!cp.equals(old_cp))
									{
										security_dtls+="Counterparty Name : "+counterparty_name+"( "+old_counterparty_name+" )\n";
									}
								}
								else if(entity.equals("B")) 
								{
									if(!bank_cd.equals(old_bank_cd))
									{
										security_dtls+="Bank Name : "+utilBean.getBankName(conn,bank_cd)+"( "+utilBean.getBankName(conn,old_bank_cd)+" )\n";
									}
								}
								if(rd.equals("C"))
								{
									if(!rate_source.equals(old_rate_source))
									{
										security_dtls+="Rating Source : "+rate_source+" ( "+old_rate_source+" )\n";
									}
									if(!credit_rating.equals(old_credit_rating))
									{
										security_dtls+="Credit Rating : "+credit_rating+"( "+old_credit_rating+" )\n";
									}
									if(!cr_status.equals(old_cr_status))
									{
										security_dtls+="Status : "+cr_status+"( "+old_cr_status+" )\n";
									}
									if(!cr_remark.equals(old_cr_remark))
									{
										security_dtls+="Remark : "+cr_remark+"( "+old_cr_remark+" )\n";
									}
								}
								else if(rd.equals("P"))
								{
									if(entity.equals("C")) 
									{
										if(gx.equals("K")) 
										{
											if(!parent_entity.equals(old_parent_entity))
											{
												security_dtls+="Parent Leagel Entity : "+utilBean.getCounterpartyName(conn,parent_entity)+"( "+utilBean.getCounterpartyName(conn,old_parent_entity)+" )\n";
											}
										}
										else if(gx.equals("I")) 
										{
											if(!parent_entity.equals(old_parent_entity))
											{
												security_dtls+="Parent Leagel Entity : "+utilBean.getGasExchangeName(conn,parent_entity)+"( "+utilBean.getGasExchangeName(conn,old_parent_entity)+" )\n";
											}
										}
									}
									else if(entity.equals("B")) 
									{
										if(!parent_entity.equals(old_parent_entity))
										{
											security_dtls+="Parent Leagel Entity : "+utilBean.getBankName(conn,parent_entity)+"( "+utilBean.getBankName(conn,old_parent_entity)+" )\n";
										}
									}
									if(!ownership.equals(old_ownership))
									{
										security_dtls+="Percent Ownership(%) : "+ownership+"( "+old_ownership+" )\n";
									}
									if(!pEntrydate.equals(old_pEntrydate))
									{
										security_dtls+="Parent Entry Date : "+pEntrydate+"( "+old_pEntrydate+" )\n";
									}
									if(!pExitdate.equals(old_pExitdate))
									{
										security_dtls+="Parent Exit Date : "+pExitdate+"( "+old_pExitdate+" )\n";
									}
									if(!pcr_status.equals(old_pcr_status))
									{
										security_dtls+="Status : "+pcr_status+"( "+old_pcr_status+" )\n";
									}
									if(!pcr_remark.equals(old_pcr_remark))
									{
										security_dtls+="Remark : "+pcr_remark+"( "+old_pcr_remark+" )\n";
									}
								}
								else if(rd.equals("L"))
								{
									if(!new_limit_type.equals(old_limit_type))
									{
										security_dtls+="Limit Type : "+new_limit_type+"( "+old_limit_type+" )\n";
									}
									if(!new_limit_action.equals(old_limit_action))
									{
										security_dtls+="Limit Action : "+new_limit_action+"( "+old_limit_action+" )\n";
									}
									if(!new_limit_category.equals(old_limit_category))
									{
										security_dtls+="Categorization : "+new_limit_category+"( "+old_limit_category+" )\n";
									}
									if(!new_limit_amt.equals(old_limit_amt))
									{
										security_dtls+="Amount(INR) : "+new_limit_amt+"( "+old_limit_amt+" )\n";
									}
									if(!new_limit_eff_dt.equals(old_limit_eff_dt))
									{
										security_dtls+="Effective Date : "+new_limit_eff_dt+"( "+old_limit_eff_dt+" )\n";
									}
									if(!new_limit_exp_dt.equals(old_limit_exp_dt))
									{
										security_dtls+="Expiration Date : "+new_limit_exp_dt+"( "+old_limit_exp_dt+" )\n";
									}
									if(!new_limit_review_dt.equals(old_limit_review_dt))
									{
										security_dtls+="Next Review Date : "+new_limit_review_dt+"( "+old_limit_review_dt+" )\n";
									}
									if(!new_limit_status.equals(old_limit_status))
									{
										security_dtls+="Status : "+new_limit_status+" ( "+old_limit_status+" )\n";
									}
									if(!new_limit_remark.equals(old_limit_remark))
									{
										security_dtls+="Remark : "+new_limit_remark+"( "+old_limit_remark+" )\n";
									}
								}
								VRATINGSIZE.add("limit_rating");
							}
							else
							{
								if(!name.equals(old_name))
								{
									security_details+="Name : "+name+"( "+old_name+" )\n";
								}
								if(!abbr.equals(old_abbr))
								{
									security_details+="Abbreviation : "+abbr+"( "+old_abbr+" )\n";
								}
								if(!sec_type.equals(old_sec_type))
								{
									security_details+="Security Type : "+sec_type+"( "+old_sec_type+" )\n";
								}
								if(!sec_category.equals(old_sec_category))
								{
									security_details+="Category : "+sec_category+"( "+old_sec_category+" )\n";
								}
								if(!deal_type.equals(old_deal_type))
								{
									security_details+="Deal Type : "+deal_type+"( "+old_deal_type+" )\n";
								}
								if(!deal_no.equals(old_deal_no))
								{
									security_details+="Contract# : "+deal_no+"( "+old_deal_no+" )\n";
								}
								if(!sec_value.equals(old_sec_value))
								{
									security_details+="Value : "+sec_value+"( "+old_sec_value+" )\n";
								}
								if(!currency.equals(old_currency))
								{
									security_details+="Currency : "+currency+"( "+old_currency+" )\n";
								}
								if(!guarantor_nm.equals(old_guarantor_nm))
								{
									security_details+="Guarantor Name : "+guarantor_nm+"( "+old_guarantor_nm+" )\n";
								}
								if(!fluctuation.equals(old_fluctuation))
								{
									security_details+="Fluctuation : "+fluctuation+"( "+old_fluctuation+" )\n";
								}
								if(!variation.equals(old_variation))
								{
									security_details+="Variation : "+variation+"( "+old_variation+" )\n";
								}
								if(!iss_bank_nm.equals(old_iss_bank_nm))
								{
									security_details+="Issuing Bank Name : "+iss_bank_nm+"( "+old_iss_bank_nm+" )\n";
								}
								if(!iss_bank_ref.equals(old_iss_bank_ref))
								{
									security_details+="Issuing Bank Reference : "+iss_bank_ref+"( "+old_iss_bank_ref+" )\n";
								}
								if(!adv_bank_nm.equals(old_adv_bank_nm))
								{
									security_details+="Advising Bank Name : "+adv_bank_nm+"( "+old_adv_bank_nm+" )\n";
								}
								if(!adv_bank_ref.equals(old_adv_bank_ref))
								{
									security_details+="Advising Bank Reference  : "+adv_bank_ref+"( "+old_adv_bank_ref+" )\n";
								}
								if(!conf_bank_nm.equals(old_conf_bank_nm))
								{
									security_details+="Confirming Bank Name : "+conf_bank_nm+"( "+old_conf_bank_nm+" )\n";
								}
								if(!conf_bank_ref.equals(old_conf_bank_ref))
								{
									security_details+="Confirming Bank Reference : "+conf_bank_ref+"( "+old_conf_bank_ref+" )\n";
								}
								if(!received_dt.equals(old_received_dt))
								{
									security_details+="Received Date : "+received_dt+"( "+old_received_dt+" )\n";
								}
								if(!review_dt.equals(old_review_dt))
								{
									security_details+="Review Date : "+review_dt+"( "+old_review_dt+" )\n";
								}
								if(!iss_date.equals(old_iss_date))
								{
									security_details+="Issuance Date : "+iss_date+"( "+old_iss_date+" )\n";
								}
								if(!expire_dt.equals(old_expire_dt))
								{
									security_details+="Expire Date : "+expire_dt+"( "+old_expire_dt+" )\n";
								}
								if(!status.equals(old_status))
								{
									security_details+="Status : "+status+"( "+old_status+" )\n";
								}
								if(!tenor.equals(old_tenor))
								{
									security_details+="Tenor : "+tenor+"( "+old_tenor+" )\n";
								}
								if(!remark.equals(old_remark))
								{
									security_details+="Remark : "+remark+"( "+old_remark+" )\n";
								}
								if(!sap_approval.equals(old_sap_approval))
								{
									security_details+="SAP Approval : "+sap_approval+"( "+old_sap_approval+" )\n";
								}
								if(!reversal.equals(old_reversal) && !reversal.equals(""))
								{
									security_details+="Reversal : "+reversal+"( "+old_reversal+" )\n";
								}
								VSECURITYSIZE.add("Security");
							}
						}

						if(form_nm.equals("Credit Limit Mgmt/Credit Rating") && !security_dtls.isEmpty()) 
						{
							VUPDATED_LIM_ON.add(update_dt+" "+time);
							VUPDATED_LIM_BY.add(update_by);
							if(entity.equals("C"))
							{
								VCOUNTPTY_NAME.add(counterparty_name);
								if(gx.equals("K")) 
								{
									VCOUNTPTY_ABBR.add(utilBean.getCounterpartyABBR(conn,cp));
								}
								else if(gx.equals("I"))
								{
									VCOUNTPTY_ABBR.add(utilBean.getGasExchangeAbbr(conn,cp));
								}
							}
							else if(entity.equals("B"))
							{
								VCOUNTPTY_NAME.add(utilBean.getBankName(conn,bank_cd));
								VCOUNTPTY_ABBR.add(utilBean.getBankABBR(conn,bank_cd));
							}
							VLIMIT_REF.add(limit_ref_no);
							VLIMIT_DTLS.add(security_dtls);
							VAUDIT_TYPE.add(audit_type);
							VCOMP_ABBR.add(company_name);
						}
						else if(!form_nm.equals("Credit Limit Mgmt/Credit Rating") && !security_details.isEmpty())
						{
							VUPDATED_SEC_ON.add(update_dt+" "+time);
							VUPDATED_SEC_BY.add(update_by);
							VSECURITYDETAILS.add(security_details);
							VCOUNTERPARTY_NM.add(name);
							VCOUNTERPARTY_ABBR.add(abbr);
							VSEC_TYPE.add(sec_type);
							VSEC_REF_NO.add(sec_ref_no);
							VDEAL_NO.add(deal_no);
							VAUDIT_TYPE.add(audit_type);
							VCOMP_ABBR.add(company_name);
						}
					}
				}
			}
			rset2.close();
			stmt2.close();

			String split_sysdate[] = today_date.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = "";
			if(!comp_abbr.equals(""))
			{
				filename = daily_file_path+comp_abbr+"-Audit_Report_"+splited_sysdate+".xls";
			}
			else
			{
				filename = daily_file_path+"Audit_Report_"+splited_sysdate+".xls";
			}
			
			String subject=comp_abbr+" EMS "+context+": Credit Risk Audit Report dated "+today_date;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Audit Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find EMS Credit Risk Audit Report dated "+today_date+" attached.";

			HSSFRow row_head1 = sheet.createRow((short)0);
			row_head1.createCell((short) 0).setCellValue("Security Audit Report");
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));

			HSSFRow rowhead1 = sheet.createRow((short)1); 
			rowhead1.createCell((short) 0).setCellValue("Sr#");
			rowhead1.createCell((short) 1).setCellValue("Legal Entity");
			rowhead1.createCell((short) 2).setCellValue("Last Update On");  
			rowhead1.createCell((short) 3).setCellValue("Last Update By");  
			rowhead1.createCell((short) 4).setCellValue("Counterparty Name");
			rowhead1.createCell((short) 5).setCellValue("Counterparty ABBR");
			rowhead1.createCell((short) 6).setCellValue("Security Type");
			rowhead1.createCell((short) 7).setCellValue("Security Ref#"); 
			rowhead1.createCell((short) 8).setCellValue("Contract#"); 
			rowhead1.createCell((short) 9).setCellValue("Change Type"); 

			int index = 0;
			if(VSECURITYDETAILS.size() > 0 && VSECURITYSIZE.size()>0)
			{
				int j=0;
				for(int i = 0; i < VSECURITYDETAILS.size(); i++ )
				{
					if (!VSEC_REF_NO.elementAt(i).equals(""))
					{
						j++;
						index++;

						HSSFRow row = sheet.createRow((short)i+2);  
						row.createCell((short) 0).setCellValue(j);
						row.createCell((short) 1).setCellValue(""+VCOMP_ABBR.elementAt(i));
						row.createCell((short) 2).setCellValue(""+VUPDATED_SEC_ON.elementAt(i));  
						row.createCell((short) 3).setCellValue(""+VUPDATED_SEC_BY.elementAt(i));
						row.createCell((short) 4).setCellValue(""+VCOUNTERPARTY_NM.elementAt(i));
						row.createCell((short) 5).setCellValue(""+VCOUNTERPARTY_ABBR.elementAt(i));
						row.createCell((short) 6).setCellValue(""+VSEC_TYPE.elementAt(i));  
						row.createCell((short) 7).setCellValue(""+VSEC_REF_NO.elementAt(i));
						row.createCell((short) 8).setCellValue(""+VDEAL_NO.elementAt(i));
						row.createCell((short) 9).setCellValue(""+VSECURITYDETAILS.elementAt(i));

						VINDEX.add(index);
					}
				}
			}
			else
			{
				index++;
				VINDEX.add(index);
			}

			HSSFRow row_head = sheet.createRow((short)VINDEX.size()+3);
			row_head.createCell((short) 0).setCellValue("Credit Rating/Credit Limit Audit Report");
			sheet.addMergedRegion(new CellRangeAddress(VINDEX.size()+3,VINDEX.size()+3,0,7));

			HSSFRow rowhead = sheet.createRow((short)VINDEX.size()+4); 
			rowhead.createCell((short) 0).setCellValue("Sr#");
			rowhead.createCell((short) 1).setCellValue("Legal Entity");
			rowhead.createCell((short) 2).setCellValue("Last Update On");  
			rowhead.createCell((short) 3).setCellValue("Last Update By");  
			rowhead.createCell((short) 4).setCellValue("Counterparty/Bank Name");
			rowhead.createCell((short) 5).setCellValue("Counterparty/Bank ABBR");
			rowhead.createCell((short) 6).setCellValue("Credit Rating/Limit Ref#");
			rowhead.createCell((short) 7).setCellValue("Change type"); 

			if(VLIMIT_DTLS.size() > 0 && VRATINGSIZE.size()>0)
			{
				int j=0;
				for(int i = 0; i < VLIMIT_DTLS.size(); i++ )
				{
					if (!VLIMIT_REF.elementAt(i).equals(""))
					{
						j++;

						HSSFRow row = sheet.createRow((short)VINDEX.size()+i+5);  
						row.createCell((short) 0).setCellValue(j);  
						row.createCell((short) 1).setCellValue(""+VCOMP_ABBR.elementAt(i));
						row.createCell((short) 2).setCellValue(""+VUPDATED_LIM_ON.elementAt(i));  
						row.createCell((short) 3).setCellValue(""+VUPDATED_LIM_BY.elementAt(i));
						row.createCell((short) 4).setCellValue(""+VCOUNTPTY_NAME.elementAt(i));
						row.createCell((short) 5).setCellValue(""+VCOUNTPTY_ABBR.elementAt(i));
						row.createCell((short) 6).setCellValue(""+VLIMIT_REF.elementAt(i));  
						row.createCell((short) 7).setCellValue(""+VLIMIT_DTLS.elementAt(i));
					}
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 9; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,comp_cd,"Audit Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,comp_cd,"Audit Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mailDelv.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Audit Report Mail Done(Daily).....");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public String getStatusName(String flag)
	{
		String function_nm="getStatusName()";
		String status_nm="";
		try
		{
			if(flag.equals("P"))
			{
				status_nm="Pending";
			}
			else if(flag.equals("O"))
			{
				status_nm="In Order";
			}
			else if(flag.equals("C"))
			{
				status_nm="Cancelled";
			}
			else if(flag.equals("A"))
			{
				status_nm="Pending For Amendment";
			}
			else if(flag.equals("R"))
			{
				status_nm="Restated";			
			}
			else if(flag.equals("D"))
			{
				status_nm="Dummy";			
			}
			else if(flag.equals("E"))
			{
				status_nm="Expired";			
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return status_nm;
	}

	private double getExchangeRateforDate(String date) 
	{
		String function_nm="getExchangeRateforDate()";
		double exchgRate=0;
		try
		{
			String exchg_rate_cd = "",exchg_rate_nm = "";
			String rate_nm="Shell Treasury Rate";
			queryString = "SELECT EXC_RATE_NM,EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE "//COMPANY_CD=? AND "
					+ "UPPER(EXC_RATE_NM) = ?"; 
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, rate_nm.toUpperCase());
			rset=stmt.executeQuery();
			if(rset.next())
			{
				exchg_rate_nm = rset.getString(1)==null?"N.A":rset.getString(1);
				exchg_rate_cd = rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();

			queryString1 = "SELECT EXCHG_VAL FROM FMS_EXCHG_RATE_ENTRY WHERE EXCHG_RATE_CD=? "
					+ "AND EFF_DT =(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY WHERE EXCHG_RATE_CD=?  AND EFF_DT <= to_date(?,'dd/mm/yyyy')) AND FLAG=?";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, exchg_rate_cd);
			stmt1.setString(2, exchg_rate_cd);
			stmt1.setString(3, date);
			stmt1.setString(4, "Y");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				exchgRate = rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return exchgRate;
	}

	public Double getExchangeRate(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date)
	{
		String function_nm="getExchangeRate()";
		double exchangRate=0;
		try
		{
			String exchng_rate_cd="";
			String exchang_criteria="";
			String exchng_rate_cal="";
			String fixed_exchng_val="";

			queryString_temp="SELECT EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,"
					+ "EXCHG_VAL "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, agmt_no);
			stmt_temp.setString(4, cont_no);
			stmt_temp.setString(5, contract_type);
			stmt_temp.setString(6, date);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				exchang_criteria = rset_temp.getString(2)==null?"":rset_temp.getString(2);
				exchng_rate_cal = rset_temp.getString(3)==null?"":rset_temp.getString(3);

				fixed_exchng_val=nf2.format(rset_temp.getDouble(4));
			}
			else
			{	
				fixed_exchng_val=nf2.format(0);
			}
			rset_temp.close();
			stmt_temp.close();

			if(exchng_rate_cd.equals("0")) 
			{
				exchangRate=Double.parseDouble(fixed_exchng_val);
			}
			else
			{
				queryString_temp1="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
				stmt_temp1 = conn.prepareStatement(queryString_temp1);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, date);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchangRate=rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
			}

			if(Double.doubleToRawLongBits(exchangRate)==Double.doubleToRawLongBits(0)) 
			{
				String rate_nm="Shell Treasury Rate";

				queryString_temp1="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?";
				stmt_temp1 = conn.prepareStatement(queryString_temp1);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, rate_nm.toUpperCase());
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchng_rate_cd = rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				}
				rset_temp1.close();
				stmt_temp1.close();

				queryString="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp = conn.prepareStatement(queryString);
				//stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(1, exchng_rate_cd);
				stmt_temp.setString(2, "Y");
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchangRate = rset_temp.getDouble(1);
				}
				rset_temp.close();
				stmt_temp.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return exchangRate;
	}

	double billedQty=0;
	String billedAmtInfo="";
	public double getAccountReceivable(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, String reportDt)
	{
		String function_nm="getAccountReceivable()";
		double billed_amt=0;
		billedQty=0;
		billedAmtInfo="";
		try
		{
			double total_gross_amt=0;
			double total_tax_amt=0;
			double total_tcs_amt=0;
			double total_tds_amt=0;
			double total_pay_recev_amt=0;
			double total_invoice_amt=0;
			double total_net_payable=0;
			double total_qty=0;
			double total_short_amt=0;

			billedAmtInfo+="<thead>";
			billedAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			billedAmtInfo+="<th align='center' colspan='13'>Invoice Details till "+reportDt+"</th>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="<tr>";
			billedAmtInfo+="<th align='center'>Invoice Date</th>";
			billedAmtInfo+="<th align='center'>Invoice#</th>";
			billedAmtInfo+="<th align='center'>MMBTU</th>";
			billedAmtInfo+="<th align='center'>Gross Amount</th>";
			billedAmtInfo+="<th align='center'>Tax</th>";
			billedAmtInfo+="<th align='center'>Invoice Amount</th>";
			billedAmtInfo+="<th align='center'>TCS(+)</th>";
			billedAmtInfo+="<th align='center'>TDS(-)</th>";
			billedAmtInfo+="<th align='center'>Net Amount</th>";
			billedAmtInfo+="<th align='center'>Due Date</th>";
			billedAmtInfo+="<th align='center'>Payment Date</th>";
			billedAmtInfo+="<th align='center'>Payment Recv. Amount</th>";
			billedAmtInfo+="<th align='center'>Short Amount</th>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="</thead>";
			billedAmtInfo+="<tbody>";
			queryString="SELECT (NVL(GROSS_AMT,0) + NVL(TRANSPORTATION_AMOUNT,0) + NVL(MARKET_MARGIN_AMT,0) + NVL(OTHER_CHARGES_AMT,0)),"
					+ "TAX_AMT,TCS_AMT,TDS_GROSS_AMT,PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,ALLOC_QTY,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_NO,"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PDF_INV_DTL IS NOT NULL "
					+ "ORDER BY INVOICE_DT DESC "; 
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, reportDt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				billedAmtInfo+="<tr>";
				double gross_amt=rset.getDouble(1);
				double tax_amt=rset.getDouble(2);
				double tcs_amt=rset.getDouble(3);
				double tds_amt=rset.getDouble(4);
				double pay_recev_amt=rset.getDouble(5);
				String pay_recev_dt=rset.getString(6)==null?"":rset.getString(6);
				double invoice_amt=rset.getDouble(7);
				double net_payable=rset.getDouble(8);
				double qty=rset.getDouble(9);
				String inv_dt=rset.getString(10)==null?"":rset.getString(10);
				String inv_no=rset.getString(11)==null?"":rset.getString(11);
				String due_dt=rset.getString(12)==null?"":rset.getString(12);
				billedQty+=qty;

				net_payable=net_payable-tds_amt;

				total_gross_amt+=gross_amt;
				total_tax_amt+=tax_amt;
				total_tcs_amt+=tcs_amt;
				total_tds_amt+=tds_amt;
				total_pay_recev_amt+=pay_recev_amt;
				total_invoice_amt+=invoice_amt;
				total_net_payable+=net_payable;
				total_qty+=qty;

				billedAmtInfo+="<td align='center'>"+inv_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+inv_no+"</td>";

				billedAmtInfo+="<td align='right'>"+nf.format(qty)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(gross_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tax_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(invoice_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tcs_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tds_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(net_payable)+"</td>";
				billedAmtInfo+="<td align='center'>"+due_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+pay_recev_dt+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(pay_recev_amt)+"</td>";			

				double due_amt=0;
				double short_amt=0;
				int pay_recev_count=utilDate.getDays(pay_recev_dt, reportDt);

				if(pay_recev_count<=1 && !pay_recev_dt.equals(""))
				{
					due_amt=(gross_amt) - ((pay_recev_amt+tds_amt) - (tax_amt + tcs_amt));
					short_amt=net_payable - pay_recev_amt;
				}
				else
				{
					due_amt=gross_amt;
					short_amt=net_payable;
				}

				total_short_amt+=short_amt;
				billedAmtInfo+="<td align='right'>"+nf.format(short_amt)+"</td>";

				if(due_amt<=CommonVariable.receivable_tolerance)
				{
					due_amt=0;
				}

				billed_amt+=due_amt;

				billedAmtInfo+="</tr>";
			}
			rset.close();
			stmt.close();

			queryString="SELECT GROSS_AMT_INR,TAX_AMT,TCS_AMT,TDS_GROSS_AMT,PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,INVOICE_TYPE,NET_PAYABLE_AMT,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_NO,"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PDF_INV_DTL IS NOT NULL AND INVOICE_TYPE IN (?,?,?,?)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, reportDt);
			stmt.setString(7, "CDR");
			stmt.setString(8, "DR");
			stmt.setString(9, "LP");
			stmt.setString(10, "S");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				billedAmtInfo+="<tr>";
				double gross_amt=rset.getDouble(1);
				double tax_amt=rset.getDouble(2);
				double tcs_amt=rset.getDouble(3);
				double tds_amt=rset.getDouble(4);
				double pay_recev_amt=rset.getDouble(5);
				String pay_recev_dt=rset.getString(6)==null?"":rset.getString(6);
				double invoice_amt=rset.getDouble(7);
				String invoice_type=rset.getString(8)==null?"":rset.getString(8);
				double net_payable=rset.getDouble(9);
				String inv_dt=rset.getString(10)==null?"":rset.getString(10);
				String inv_no=rset.getString(11)==null?"":rset.getString(11);
				String due_dt=rset.getString(12)==null?"":rset.getString(12);
				double ff_qty=rset.getDouble(13);

				net_payable=net_payable-tds_amt;

				total_gross_amt+=gross_amt;
				total_tax_amt+=tax_amt;
				total_tcs_amt+=tcs_amt;
				total_tds_amt+=tds_amt;
				total_pay_recev_amt+=pay_recev_amt;
				total_invoice_amt+=invoice_amt;
				total_net_payable+=net_payable;
				total_qty+=ff_qty;

				billedAmtInfo+="<td align='center'>"+inv_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+inv_no+"</td>";

				billedAmtInfo+="<td align='right'>"+nf.format(ff_qty)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(gross_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tax_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(invoice_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tcs_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tds_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(net_payable)+"</td>";
				billedAmtInfo+="<td align='center'>"+due_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+pay_recev_dt+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(pay_recev_amt)+"</td>";			

				double due_amt=0;
				double short_amt=0;

				int pay_recev_count=utilDate.getDays(pay_recev_dt, reportDt);

				if(pay_recev_count<=1 && !pay_recev_dt.equals(""))
				{
					due_amt=(gross_amt) - ((pay_recev_amt+tds_amt) - (tax_amt + tcs_amt));
					short_amt=net_payable - pay_recev_amt;
				}
				else
				{
					due_amt=gross_amt;
					short_amt=net_payable;
				}

				total_short_amt+=short_amt;
				billedAmtInfo+="<td align='right'>"+nf.format(short_amt)+"</td>";

				if(due_amt<=CommonVariable.receivable_tolerance)
				{
					due_amt=0;
				}
				billed_amt+=due_amt;
				billedAmtInfo+="</tr>";
			}
			rset.close();
			stmt.close();

			billedAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			billedAmtInfo+="<td colspan='2' align='right'>Total : </td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_qty)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_gross_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tax_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_invoice_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tcs_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tds_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_net_payable)+"</td>";
			billedAmtInfo+="<td align='center' colspan='2'></td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_pay_recev_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_short_amt)+"</td>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="<tr style='font-weight:bold;'>";
			billedAmtInfo+="<td colspan='13' align='center' style='color:green;'>"
					+ "Account Receivable = (Gross Amount) - ((Pay Received Amount + TDS Amount) - (Tax Amount + TCS Amount)) = "+nf.format(billed_amt)+"</td>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="</tbody>";
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return billed_amt;
	}

	double unbilledQty=0;
	String unbilledAmtInfo="";
	public double getUnbilledReceivable(String comp_cd, String counterparty_cd, String agmt_no, String agmt_rev, String cont_no, String cont_rev, String cont_type, 
			String agmt_base,String reportDt, String cont_st_dt, String cont_end_dt,String rate, String rate_unit)
	{
		String function_nm="getUnbilledReceivable()";
		double unbilled_amt=0;
		unbilledQty=0;
		unbilledAmtInfo="";
		try
		{
			String previousDate=utilDate.getDate(reportDt, "-1");
			int isExpire=utilDate.getDays(cont_end_dt, previousDate);
			if(isExpire<1)
			{
				previousDate=cont_end_dt;
			}


			String headInfo="Unbilled Amount Calculated From "+cont_st_dt+" to "+previousDate;

			unbilledAmtInfo+="<thead>";
			unbilledAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<th colspan='10' align='right'>"+headInfo+"</th>";
			}
			else
			{
				unbilledAmtInfo+="<th colspan='9' align='right'>"+headInfo+"</th>";
			}
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="<tr>";
			unbilledAmtInfo+="<th>Gas Day</th>";
			unbilledAmtInfo+="<th>Plant</th>";
			unbilledAmtInfo+="<th>Price Rate<br>("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)</th>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<th>Exchange Rate</th>";
			}
			unbilledAmtInfo+="<th>Unbilled MMBTU</th>";
			unbilledAmtInfo+="<th>Amount</th>";
			unbilledAmtInfo+="<th>Transportation Charge</th>";
			unbilledAmtInfo+="<th>Marketing Margin</th>";
			unbilledAmtInfo+="<th>Other Charge</th>";
			unbilledAmtInfo+="<th>Unbilled Amount</th>";
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="</thead>";
			unbilledAmtInfo+="<tbody>";
			double total_charges=0;
			double total_amt=0;
			double total_trans_amt=0;
			double total_mktmrg_amt=0;
			double total_chngoth_amt=0;
			queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(GAS_DT,'DD/MM/YYYY'),PLANT_SEQ "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "AND (SELECT COUNT(*) FROM FMS_INVOICE_MST B WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_UNIT=A.BU_SEQ "
					+ "AND B.PERIOD_START_DT<=A.GAS_DT AND B.PERIOD_END_DT>=A.GAS_DT AND B.INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND B.PDF_INV_DTL IS NOT NULL) = 0 "
					+ "GROUP BY GAS_DT,PLANT_SEQ "
					+ "ORDER BY GAS_DT ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, cont_no);
			stmt.setString(2, agmt_no);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, cont_type);
			stmt.setString(6, cont_st_dt);
			stmt.setString(7, previousDate);
			stmt.setString(8, reportDt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				unbilledAmtInfo+="<tr>";
				double qty=rset.getDouble(1);
				String gas_dt=rset.getString(2)==null?"":rset.getString(2);
				String plant_seq=rset.getString(3)==null?"":rset.getString(3);
				unbilledAmtInfo+="<td align='center'>"+gas_dt+"</td>";
				unbilledAmtInfo+="<td align='center'>"+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C")+"</td>";

				unbilledQty+=qty;

				String new_price=getSalesRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
				if(new_price.equals(""))
				{
					new_price=rate;
				}
				unbilledAmtInfo+="<td align='right'>"+new_price+"</td>";

				double amt=(qty * Double.parseDouble(new_price));

				if(rate_unit.equals("2"))
				{
					double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
					unbilledAmtInfo+="<td align='right'>"+nf2.format(exchng_rate)+"</td>";
					total_amt += amt * exchng_rate;
				}
				else
				{
					total_amt+=amt;
				}
				unbilledAmtInfo+="<td align='right'>"+nf.format(qty)+"</td>";
				unbilledAmtInfo+="<td align='right'>"+nf.format(amt)+"</td>";

				double temp_total_charges=0;
				Vector charges = getOtherCharges(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, plant_seq,gas_dt);
				if(!charges.elementAt(0).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(0)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_trans_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(0)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				if(!charges.elementAt(1).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(1)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_mktmrg_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(1)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				if(!charges.elementAt(2).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(2)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_chngoth_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(2)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				unbilledAmtInfo+="<td align='right'>"+nf.format(amt+temp_total_charges)+"</td>";
				unbilledAmtInfo+="</tr>";
			}
			rset.close();
			stmt.close();
			unbilled_amt=total_amt+total_charges;

			unbilledAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<td colspan='4' align='right'>Total : </td>";
			}
			else
			{
				unbilledAmtInfo+="<td colspan='3' align='right'>Total : </td>";
			}
			unbilledAmtInfo+="<td align='right'>"+nf.format(unbilledQty)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_trans_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_mktmrg_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_chngoth_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(unbilled_amt)+"</td>";
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="</tbody>";


		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return unbilled_amt;
	}

	double undeliveredQty=0;
	String undeliveredAmtInfo="";
	public double getUndeliveredCurrentMonth(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, 
			String agmt_base,String reportDt, String cont_st_dt,String cont_end_dt,String rate, String rate_unit,String dcq, String tcq, String dlvQty)
	{
		String function_nm="getUndeliveredCurrentMonth()";
		double undelivered_amt=0;
		undeliveredQty=0;
		undeliveredAmtInfo="";
		try
		{
			if(utilDate.getDays(reportDt, cont_end_dt)<=1) 
			{
				String lastDateOfMonth = utilDate.getLastDateOfMonth(reportDt);
				int contExpiry = utilDate.getDays(lastDateOfMonth, cont_end_dt);
				if(contExpiry > 0)
				{
					lastDateOfMonth = cont_end_dt;
				}
				int no_of_days = utilDate.getDays(lastDateOfMonth, reportDt);


				double temp_dcq = 0;
				double total_UndeliveredQty =0;

				double amt=0;
				if(!rate.equals("") && !dcq.equals(""))
				{
					if(Double.parseDouble(dlvQty)<=Double.parseDouble(tcq))
					{
						total_UndeliveredQty = Double.parseDouble(dcq) * no_of_days;
						String tot_dlvQty=nf.format(total_UndeliveredQty+Double.parseDouble(dlvQty));

						if(Double.parseDouble(tot_dlvQty)>Double.parseDouble(tcq))
						{
							total_UndeliveredQty=Double.parseDouble(tcq) - Double.parseDouble(dlvQty);
						}

						undeliveredQty=total_UndeliveredQty;
						amt=total_UndeliveredQty*Double.parseDouble(rate);
						undeliveredAmtInfo+="TCQ : "+tcq+" DCQ : "+dcq;
						undeliveredAmtInfo+="\nDelivered MMBTU : "+dlvQty;
						undeliveredAmtInfo+="\n\nForward Notional Current Month Calculated From "+reportDt+" to "+lastDateOfMonth;
						undeliveredAmtInfo+="\n\nForward Notional Current Month MMBTU : "+nf.format(undeliveredQty);
						undeliveredAmtInfo+="\nPrice Rate : "+rate+" "+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU";

						if(rate_unit.equals("2"))
						{
							double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, lastDateOfMonth);
							undelivered_amt = amt * exchng_rate;
							undeliveredAmtInfo+="\nExchage Rate : "+nf.format(exchng_rate);
						}
						else
						{
							undelivered_amt=amt;
						}
						undeliveredAmtInfo+="\n\nForward Notional Current Month : "+nf.format(undelivered_amt);
					}
				}				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return undelivered_amt;
	}

	String fwdNotionalAmtInfo="";
	public double getForwardNotionalNextMonth(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, 
			String agmt_base, String reportDt, String cont_st_dt,String cont_end_dt,String rate, String rate_unit,String dcq,String tcq, String dlvQty)
	{
		String function_nm="getForwardNotionalNextMonth()";
		double fwdNotionalAmt=0;
		fwdNotionalAmtInfo="";
		try
		{
			String lastDateOfMonth = utilDate.getLastDateOfMonth(reportDt);
			String FirstDtOfNextMonth = utilDate.getDate(lastDateOfMonth, "1");
			int contExpiry = utilDate.getDays(cont_end_dt, FirstDtOfNextMonth);
			String lastDateOfNextMonth = utilDate.getLastDateOfMonth(FirstDtOfNextMonth);

			if(contExpiry > 0)
			{
				int contExpiry_2 = utilDate.getDays(lastDateOfNextMonth, cont_end_dt);
				if(contExpiry_2 > 0)
				{
					lastDateOfNextMonth=cont_end_dt;
				}
				int no_of_days =utilDate.getDays(lastDateOfNextMonth, FirstDtOfNextMonth);

				double temp_dcq = 0;
				double total_UndeliveredQty =0;

				double amt=0;
				if(!rate.equals("") && !dcq.equals(""))
				{
					if(Double.parseDouble(dlvQty)<=Double.parseDouble(tcq))
					{
						total_UndeliveredQty = Double.parseDouble(dcq) * no_of_days;
						String tot_dlvQty=nf.format(total_UndeliveredQty+Double.parseDouble(dlvQty));

						if(Double.parseDouble(tot_dlvQty)>Double.parseDouble(tcq))
						{
							total_UndeliveredQty=Double.parseDouble(tcq) - Double.parseDouble(dlvQty);
						}

						amt=total_UndeliveredQty*Double.parseDouble(rate);

						fwdNotionalAmtInfo+="TCQ : "+tcq+" DCQ : "+dcq;
						fwdNotionalAmtInfo+="\nDelivered MMBTU + Forward Notional Current Month MMBTU : "+dlvQty;
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month Calculated From "+FirstDtOfNextMonth+" to "+lastDateOfNextMonth;
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month MMBTU : "+nf.format(total_UndeliveredQty);
						fwdNotionalAmtInfo+="\nPrice Rate : "+rate+" "+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU";

						if(rate_unit.equals("2"))
						{
							double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, lastDateOfNextMonth);
							fwdNotionalAmt = amt * exchng_rate;
							fwdNotionalAmtInfo+="\nExchage Rate : "+nf.format(exchng_rate);
						}
						else
						{
							fwdNotionalAmt=amt;
						}
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month : "+nf.format(fwdNotionalAmt);
					}
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return fwdNotionalAmt;
	}

	public String nextDaydate(String date1, String days)
	{
		String function_nm="nextDaydate()";
		String date="";
		try
		{
			queryString="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY') + ?,'DD/MM/YYYY') FROM DUAL";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, date1);
			stmt.setString(2, days);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				date=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return date;
	}

	private double getSecurityValue(String date) 
	{
		String function_nm="getSecurityValue()";
		double security_value=0;
		try
		{
			double ExchgRate = getExchangeRateforDate(date);

			queryString ="SELECT NVL(VALUE,0),CURRENCY,SEC_TYPE,SEC_REF_NO,TO_CHAR(EXPIRE_DT,'DD/MM/YYYY'),COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE COMPANY_CD=? AND "
					+ "GX=? AND ISSUE_DT<=to_date(?,'DD/MM/YYYY') AND(EXPIRE_DT>=TO_DATE(?,'DD/MM/YYYY') AND "
					+ "(TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL))";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "I");
			stmt.setString(3, date);
			stmt.setString(4, date);
			stmt.setString(5, date);
			rset = stmt.executeQuery();					
			while(rset.next())
			{
				String currency= rset.getString(2)==null?"":rset.getString(2);
				String sec_type = rset.getString(3)==null?"":rset.getString(3); 
				String ref_no = rset.getString(4)==null?"":rset.getString(4); 
				String exp_dt = rset.getString(5)==null?"":rset.getString(5);
				String company_cd = rset.getString(6)==null?"":rset.getString(6);
				if(currency.equals("2"))
				{
					security_value+=rset.getDouble(1)*ExchgRate;
				}
				else
				{
					security_value+=rset.getDouble(1);
				}

				bg_ref_no = ref_no;
				bg_validity = exp_dt;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return security_value;
	}

	public String getSalesRate(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, String date)
	{
		String function_nm="getSalesRate()";
		String price="";
		try
		{
			queryString_temp = "SELECT DISTINCT NEW_SALE_PRICE "
					+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
					+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, agmt_no);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, counterparty_cd);
			stmt_temp.setString(5, "A");
			stmt_temp.setString(6, cont_type);
			stmt_temp.setString(7, date);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return price;
	}

	public Vector getOtherCharges(String comp_cd, String counterparty_cd, String agmt_no, String agmt_rev_no, String cont_no, String cont_rev_no, String contract_type, String plant_seq, String date) 
	{
		Vector chrg = new Vector();
		String function_nm = "getOtherCharges()";

		try 
		{
			for(int i=0;i<VCHARGE_ABBR.size();i++)
			{
				String chrg_abbr = ""+VCHARGE_ABBR.elementAt(i);

				String rate = "";
				queryString_temp = "SELECT A.CHARGE_RATE "
						+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_ABBR=? "
						+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO "
						+ "AND A.CHARGE_ABBR=B.CHARGE_ABBR AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				try (PreparedStatement stmt_temp = conn.prepareStatement(queryString_temp)) 
				{
					stmt_temp.setString(1, comp_cd);
					stmt_temp.setString(2, counterparty_cd);
					stmt_temp.setString(3, cont_no);
					stmt_temp.setString(4, agmt_no);
					stmt_temp.setString(5, agmt_rev_no);
					stmt_temp.setString(6, contract_type);
					stmt_temp.setString(7, plant_seq);
					stmt_temp.setString(8, chrg_abbr);
					stmt_temp.setString(9, date);
					try (ResultSet rset_temp = stmt_temp.executeQuery()) 
					{
						if (rset_temp.next()) 
						{
							rate = rset_temp.getString(1) == null ? "" : nf.format(rset_temp.getDouble(1));
						}
					}
				}
				chrg.add(rate);  // Add the rate to the vector
			}
		} 
		catch (SQLException e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return chrg;
	}

	public void doClear()
	{
		VRATINGSIZE.clear();
		VSECURITYSIZE.clear();
		VSECURITYDETAILS.clear();
		VSEC_REF_NO.clear();
		VUPDATED_ON.clear();
		VUPDATED_BY.clear();
		VUPDATED_SEC_ON.clear();
		VUPDATED_SEC_BY.clear();
		VUPDATED_LIM_ON.clear();
		VUPDATED_LIM_BY.clear();
		VCOUNTERPARTY_NM.clear();
		VCOUNTERPARTY_ABBR.clear();
		VSEC_TYPE.clear();
		VDEAL_NO.clear();
		VAUDIT_TYPE.clear();
		VCOUNTPTY_NAME.clear();
		VCOUNTPTY_ABBR.clear();
		VLIMIT_REF.clear();
		VLIMIT_DTLS.clear();
		VINDEX.clear();
		VSTART_DT.clear();
		VEND_DT.clear();
		VCONTRACT_TYPE.clear();
		VCOUNTERPARTY_CD.clear();
		VTCQ.clear();
		VDCQ.clear();
		VCONT_REF_NO.clear();
		VTRADE_REF_NO.clear();
		VSIGNING_DT.clear();
		VRATE.clear();
		VRATE_UNIT.clear();
		VFCC_BY.clear();
		VFCC_DT.clear();
		VAGMT_BASE.clear();
		VPOST_MARGIN.clear();
		VDISPLAY_DEAL_MAP.clear();
		VACCOUNT.clear();
		VPRICE_TYPE.clear();
		VTAX.clear();
		VBG_DROPOFF_DT.clear();
		VTRADE_VALUE.clear();
		VPOST_TRADE_MARGIN.clear();
		V_START_DT.clear();
		V_END_DT.clear();
		V_CONTRACT_TYPE.clear();
		V_COUNTERPARTY_CD.clear();
		V_TCQ.clear();
		V_DCQ.clear();
		V_CONT_REF_NO.clear();
		V_TRADE_REF_NO.clear();
		V_SIGNING_DT.clear();
		V_RATE.clear();
		V_RATE_UNIT.clear();
		V_FCC_BY.clear();
		V_FCC_DT.clear();
		V_AGMT_BASE.clear();
		V_POST_MARGIN.clear();
		V_DISPLAY_DEAL_MAP.clear();
		V_ACCOUNT.clear();
		V_PRICE_TYPE.clear();
		V_TAX.clear();
		V_BG_DROPOFF_DT.clear();
		V_TRADE_VALUE.clear();
		V_POST_TRADE_MARGIN.clear();
		VTEMP_REPORT_DT.clear();
		VREPORT_DT.clear();
		VTEMP_MARGIN_USED.clear();
		VSTATUS.clear();
		VVALUE.clear();
		VEXPIRE_DT.clear();
		VREMARK.clear();
		VSEC_CATEGRY.clear();
		VCURRENCY.clear();
		VDUE_DT.clear();
		VDEAL_TYPE.clear();
		VDEAL_DTL.clear();
		VDIS_DT.clear();
		VCLEARANCE.clear();
		V_SEC_CATEGORY.clear();
		VSEC_CATEGORY.clear();
		VISS_BANK_REF.clear();
		VPREVIOUS_DT.clear();
		VEXP_VAL.clear();
		VAGMT_NO.clear();
		VAGMT_REV_NO.clear();
		VCONT_REV_NO.clear();
		VCONT_TYPE.clear();
		VCONT_NO.clear();
		VMUL_DEALS.clear();
		VINVOICE_NO.clear();
		VAMT_DC.clear();
		VNET_DUE_DT.clear();
		VDUE_AMT.clear();
		VRECV_AMT.clear();
		VPAY_RECV_DT.clear();
		VPAYMENT_STATUS.clear();
		VCO_CODE.clear();
		VCO_ABBR.clear();
		VRECEIVED_DATE.clear();
		VVALUE_FLUCTUATION.clear();
		VISSUE_DT.clear();
		VCANCEL_DT.clear();
		VRENEW_DT.clear();
		VDEAL_REF_NO.clear();
		VCOLL_CATEGORY.clear();
		VCOUNTERPARTY_CATEGORY.clear();
		VBUSINESS.clear();
		VLEGAL_ENTITY.clear();
		V_LEGAL_ENTITY.clear();
		VDOC_NO.clear();
		VREF_K1.clear();
		VREF_K2.clear();
		VREF_K3.clear();
		VDEAL_ASSIGNMENT.clear();
		VTEXT.clear();
		VBA.clear();
		VAMT_INR.clear();
		VINV_TYPE.clear();
		VDESK_NAME.clear();
		VRES_COLLECTION_PRTY.clear();
		VRTL_GPL_TRADER.clear();
		VCLRNG_DOC.clear();
		VCLRNG_DT.clear();
		VWBS_PNL.clear();
		VBL_DT.clear();
		VINVOICE_TYPE.clear();
		VCATEGORY.clear();
		VCURRANCY.clear();
		VAMT_USD.clear();
		VARREARS_DAYS.clear();
		VAGING.clear();
		VOVERDUE_COZ.clear();
		VDUE_AMT_USD.clear();
		VMST_BANK_CD.clear();
		VMST_BANK_ABBR.clear();
		VMST_BANK_NM.clear();
		VBANK_LIMIT_USD.clear();
		VBANK_LIMIT.clear();
		VEXPOSURE_INR.clear();
		VEXPOSURE_USD.clear();
		VAVAILABILITY_INR.clear();
		VAVAILABILITY_USD.clear();
		VISS_BANK_CD.clear();
		VISS_BANK_NM.clear();
		VADV_BANK_CD.clear();
		VADV_BANK_NM.clear();
		VADV_BANK_REF.clear();
		VCONFIRM_BANK_CD.clear();
		VCONFIRM_BANK_NM.clear();
		VCONFIRM_BANK_REF.clear();
		VBANK_EXPOSURE_INR.clear();
		VBANK_EXPOSURE_USD.clear();
		VBANK_AVAILABILITY_INR.clear();
		VBANK_AVAILABILITY_USD.clear();
		VEXPOSURE_HEADING.clear();
		VEXPOSURE_HEADING_FLAG.clear();
		VGX.clear();
		VHEADING_INFO.clear();
		VRATE_UNIT_NM.clear();
		VCOLLATERAL_INFO.clear();
		VBILLED_AMT.clear();
		VBILLED_QTY.clear();
		VBILLED_AMT_INFO.clear();
		VUNBILLED_AMT.clear();
		VUNBILLED_AMT_INFO.clear();
		VUNBILLED_CURRENT_MONTH.clear();
		VUNBILLED_CURRENT_MONTH_INFO.clear();
		VFORWARD_NOTIONAL.clear();
		VFORWARD_NOTIONAL_INFO.clear();
		VGROSS_EXPOSURE.clear();
		VGROSS_EXPOSURE_INFO.clear();
		VGROSS_EXPOSURE_TAX.clear();
		VGROSS_EXPOSURE_TAX_INFO.clear();
		VCOLLATERAL_VALUE.clear();
		VNET_EXPOSURE.clear();
		VNET_EXPOSURE_INFO.clear();
		VEXCHG_RATE.clear();
		VPARENT_CD.clear();
		VLIMIT_PARENT_FLAG.clear();
		VPARENT_LIMIT_VALUE.clear();
		VLIMIT_VALUE_LINKED.clear();
		VCREDIT_EXCEED_BALANCE.clear();
		VCONSUMED_LIMIT.clear();
		VCREDIT_EXCEED.clear();
		VCREDIT_EXCEED_INFO.clear();
		VLIMIT_VALUE_LINKED_COMP.clear();
		VLIMIT.clear();
		VNET_EXPOSURE_USD.clear();
		VCREDIT_EXCEED_USD.clear();
		VCREDIT_EXCEED_REASON.clear();
		VCREDIT_EXCEED_REASON_FLAG.clear();
		VMAILINDEX.clear();
		VVALUE_USD.clear();
		VEXCHANGE_RATE.clear();
		VDOUBLE_COUNTERPARTY_CD.clear();
		VDOUBLE_SEC_CATEGORY.clear();
		VDOUBLE_STATUS.clear();
		VDOUBLE_SEC_TYPE.clear();
		VDOUBLE_CONFIRM_BANK_CD.clear();
		VDOUBLE_VALUE.clear();
		VDOUBLE_VALUE_USD.clear();
		V_SEC_TYPE.clear();
		V_SEC_REF_NO.clear();
		V_STATUS.clear();
		V_VALUE.clear();
		V_VALUE_USD.clear();
		V_ISS_BANK_REF.clear();
		V_EXPIRE_DT.clear();
		V_REMARK.clear();
		V_COUNTERPARTY_NM.clear();
		V_COUNTERPARTY_ABBR.clear();
		V_SEC_CATEGRY.clear();
		V_CURRENCY.clear();
		V_DEAL_NO.clear();
		V_DEAL_DTL.clear();
		V_PREVIOUS_DT.clear();
		V_FORM_C.clear();
		VFORM_C.clear();
		V_PREVIOUS_DT.clear();
		VCREDIT_RATING.clear();
		VAVAILABLE.clear();
		VTOTAL_LIMIT.clear();
		VUNSECURED.clear();
		VTEMPORARY.clear();
		VADJUST_USAGE.clear();
		VUSAGE.clear();
		VNET_USAGE.clear();
		VUSED.clear();
		V_CREDIT_RATING.clear();
		V_AVAILABLE.clear();
		V_TOTAL_LIMIT.clear();
		V_UNSECURED.clear();
		V_TEMPORARY.clear();
		V_ADJUST_USAGE.clear();
		V_USAGE.clear();
		V_NET_USAGE.clear();
		V_USED.clear();
		V_DUE_DT.clear();
		V_DEAL_TYPE.clear();
		V_DIS_DT.clear();
		VMARGIN_AVAIL.clear();
		VMARGIN_USED.clear();
		VBG_VALUE.clear();
		VDIS_CONTRACT_TYPE.clear();
		V_DIS_CONTRACT_TYPE.clear();

		to_mail="";
		cc_mail="";
		mail_msg="";
		total_igx_ac_rece = "0.00";
		total_igx_unbilled_rece = "0.00";
		total_igx_delv_curr_mth = "0.00";
		total_igx_fwd_not = "0.00";
		total_igx_gross_expo = "0.00";
		total_igx_gross_expo_incl_tax = "0.00";
		total_igx_net_expo = "0.00";
		total_igx_limit = "0.00";
		total_igx_cr_exceed = "0.00";
		total_igx_cr_exceed_usd = "0.00";
		total_igx_net_expo_usd = "0.00";
		bg_ref_no = "";
		bg_validity = "";
		comp_cd="0";
		comp_abbr="";

		inLCConfCount = "";
		inLCConfAmt = "";
		inLCConfAmtUsd = "";
		inLCCanCount = "";
		inLCCanAmt = "";
		inLCCanAmtUsd = "";
		inLCAdvCount = "";
		inLCAdvAmt = "";
		inLCAdvAmtUsd = "";
		inBGConfCount = "";
		inBGConfAmt = "";
		inBGConfAmtUsd = "";
		inBGCanCount = "";
		inBGCanAmt = "";
		inBGCanAmtUsd = "";
		inBGAdvCount = "";
		inBGAdvAmt = "";
		inBGAdvAmtUsd = "";
		inPCGConfCount = "";
		inPCGConfAmt = "";
		inPCGConfAmtUsd = "";
		inPCGCanCount = "";
		inPCGCanAmt = "";
		inPCGCanAmtUsd = "";
		inPCGAdvCount = "";
		inPCGAdvAmt = "";
		inPCGAdvAmtUsd = "";

		outLCConfCount = "";
		outLCConfAmt = "";
		outLCConfAmtUsd = "";
		outLCCanCount = "";
		outLCCanAmt = "";
		outLCCanAmtUsd = "";
		outLCAdvCount = "";
		outLCAdvAmt = "";
		outLCAdvAmtUsd = "";
		outBGConfCount = "";
		outBGConfAmt = "";
		outBGConfAmtUsd = "";
		outBGCanCount = "";
		outBGCanAmt = "";
		outBGCanAmtUsd = "";
		outBGAdvCount = "";
		outBGAdvAmt = "";
		outBGAdvAmtUsd = "";
		outPCGConfCount = "";
		outPCGConfAmt = "";
		outPCGConfAmtUsd = "";
		outPCGCanCount = "";
		outPCGCanAmt = "";
		outPCGCanAmtUsd = "";
		outPCGAdvCount = "";
		outPCGAdvAmt = "";
		outPCGAdvAmtUsd = "";
	}

	String filename="";
	String subject="";
	String mailBody="";
	String from_mail="";
	String to_mail="";
	String cc_mail = "";
	String mail_msg = "";
	String from_pwd="";
	String dbline="";
	String username="";
	String password="";
	String encrypted="";
	String user="";
	String pass="";
	String today_date="";
	String yes_date = "";
	String short_day_nm = "";
	String lastdtofmonth = "";
	String firstdtofmonth = "";
	String nextfiveworkingdt = "";
	String comp_cd= "";
	String comp_abbr= "";
	String emp_cd="";
	String context = "";
	String daily_file_path = "";
	String file_path = "";
	String host="mail.barodainformatics.com";
	String smtp_port = "";
	String smtp_auth = "";
	String bg_value = "";
	String bg_validity = "";
	String bg_ref_no = "";
	String isCreditExceedRpt="";

	double margin_used = 0;
	String margine_used = "";
	String margin_avail = "";

	HashMap<String, Double> limit_value = new HashMap<String, Double>();
	HashMap<String, Double> parent_limit_value = new HashMap<String, Double>();
	HashMap<String,Double> temp_limit_info = new HashMap<String,Double>();
	String total_igx_ac_rece = "0.00";
	String total_igx_unbilled_rece = "0.00";
	String total_igx_delv_curr_mth = "0.00";
	String total_igx_fwd_not = "0.00";
	String total_igx_gross_expo = "0.00";
	String total_igx_gross_expo_incl_tax = "0.00";
	String total_igx_net_expo = "0.00";
	String total_igx_limit = "0.00";
	String total_igx_cr_exceed = "0.00";
	String total_igx_cr_exceed_usd = "0.00";
	String total_igx_net_expo_usd = "0.00";

	String inLCConfCount = "";
	String inLCConfAmt = "";
	String inLCConfAmtUsd = "";
	String inLCCanCount = "";
	String inLCCanAmt = "";
	String inLCCanAmtUsd = "";
	String inLCAdvCount = "";
	String inLCAdvAmt = "";
	String inLCAdvAmtUsd = "";
	String inBGConfCount = "";
	String inBGConfAmt = "";
	String inBGConfAmtUsd = "";
	String inBGCanCount = "";
	String inBGCanAmt = "";
	String inBGCanAmtUsd = "";
	String inBGAdvCount = "";
	String inBGAdvAmt = "";
	String inBGAdvAmtUsd = "";
	String inPCGConfCount = "";
	String inPCGConfAmt = "";
	String inPCGConfAmtUsd = "";
	String inPCGCanCount = "";
	String inPCGCanAmt = "";
	String inPCGCanAmtUsd = "";
	String inPCGAdvCount = "";
	String inPCGAdvAmt = "";
	String inPCGAdvAmtUsd = "";

	String outLCConfCount = "";
	String outLCConfAmt = "";
	String outLCConfAmtUsd = "";
	String outLCCanCount = "";
	String outLCCanAmt = "";
	String outLCCanAmtUsd = "";
	String outLCAdvCount = "";
	String outLCAdvAmt = "";
	String outLCAdvAmtUsd = "";
	String outBGConfCount = "";
	String outBGConfAmt = "";
	String outBGConfAmtUsd = "";
	String outBGCanCount = "";
	String outBGCanAmt = "";
	String outBGCanAmtUsd = "";
	String outBGAdvCount = "";
	String outBGAdvAmt = "";
	String outBGAdvAmtUsd = "";
	String outPCGConfCount = "";
	String outPCGConfAmt = "";
	String outPCGConfAmtUsd = "";
	String outPCGCanCount = "";
	String outPCGCanAmt = "";
	String outPCGCanAmtUsd = "";
	String outPCGAdvCount = "";
	String outPCGAdvAmt = "";
	String outPCGAdvAmtUsd = "";

	Vector VSECURITYSIZE = new Vector();
	Vector VRATINGSIZE =  new Vector();
	Vector VSECURITYDETAILS = new Vector();
	Vector VSEC_REF_NO =  new Vector();
	Vector VUPDATED_ON = new Vector();
	Vector VUPDATED_BY = new Vector();
	Vector VUPDATED_SEC_ON = new Vector();
	Vector VUPDATED_SEC_BY = new Vector();
	Vector VUPDATED_LIM_ON = new Vector();
	Vector VUPDATED_LIM_BY = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VSEC_TYPE = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VAUDIT_TYPE = new Vector();
	Vector VCOUNTPTY_NAME = new Vector();
	Vector VCOUNTPTY_ABBR = new Vector();
	Vector VLIMIT_REF = new Vector();
	Vector VLIMIT_DTLS = new Vector();
	Vector VINDEX = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VTCQ = new Vector();
	Vector VDCQ = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VTRADE_REF_NO = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VFCC_BY = new Vector();
	Vector VFCC_DT = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VPOST_MARGIN = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VTAX = new Vector();
	Vector VBG_DROPOFF_DT = new Vector();
	Vector VTRADE_VALUE = new Vector();
	Vector VPOST_TRADE_MARGIN = new Vector();
	Vector V_START_DT = new Vector();
	Vector V_END_DT = new Vector();
	Vector V_CONTRACT_TYPE = new Vector();
	Vector V_COUNTERPARTY_CD = new Vector();
	Vector V_TCQ = new Vector();
	Vector V_DCQ = new Vector();
	Vector V_CONT_REF_NO = new Vector();
	Vector V_TRADE_REF_NO = new Vector();
	Vector V_SIGNING_DT = new Vector();
	Vector V_RATE = new Vector();
	Vector V_RATE_UNIT = new Vector();
	Vector V_FCC_BY = new Vector();
	Vector V_FCC_DT = new Vector();
	Vector V_AGMT_BASE = new Vector();
	Vector V_POST_MARGIN = new Vector();
	Vector V_DISPLAY_DEAL_MAP = new Vector();
	Vector V_ACCOUNT = new Vector();
	Vector V_PRICE_TYPE = new Vector();
	Vector V_TAX = new Vector();
	Vector V_BG_DROPOFF_DT = new Vector();
	Vector V_TRADE_VALUE = new Vector();
	Vector V_POST_TRADE_MARGIN = new Vector();
	Vector VTEMP_REPORT_DT = new Vector();
	Vector VREPORT_DT = new Vector();
	Vector VTEMP_MARGIN_USED = new Vector();
	Vector VSTATUS = new Vector();
	Vector VVALUE = new Vector();
	Vector VEXPIRE_DT = new Vector();
	Vector VREMARK = new Vector();
	Vector VSEC_CATEGRY = new Vector();
	Vector VCURRENCY = new Vector();
	Vector VDUE_DT = new Vector();
	Vector VDEAL_TYPE = new Vector();
	Vector VDEAL_DTL = new Vector();
	Vector VDIS_DT = new Vector();
	Vector VCLEARANCE = new Vector();
	Vector V_SEC_CATEGORY = new Vector();
	Vector VSEC_CATEGORY = new Vector();
	Vector VISS_BANK_REF = new Vector();
	Vector VPREVIOUS_DT = new Vector();
	Vector VEXP_VAL = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VMUL_DEALS = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VAMT_DC = new Vector();
	Vector VNET_DUE_DT = new Vector();
	Vector VDUE_AMT = new Vector();
	Vector VRECV_AMT = new Vector();
	Vector VPAY_RECV_DT = new Vector();
	Vector VPAYMENT_STATUS = new Vector();
	Vector VCO_CODE = new Vector();
	Vector VCO_ABBR = new Vector();
	Vector VRECEIVED_DATE = new Vector();
	Vector VVALUE_FLUCTUATION = new Vector();
	Vector VISSUE_DT = new Vector();
	Vector VCANCEL_DT = new Vector();
	Vector VRENEW_DT = new Vector();
	Vector VDEAL_REF_NO = new Vector();
	Vector VCOLL_CATEGORY = new Vector();
	Vector VCOUNTERPARTY_CATEGORY = new Vector();
	Vector VBUSINESS = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	Vector VDOC_NO = new Vector();
	Vector VREF_K1 = new Vector();
	Vector VREF_K2 = new Vector();
	Vector VREF_K3 = new Vector();
	Vector VDEAL_ASSIGNMENT = new Vector();
	Vector VTEXT = new Vector();
	Vector VBA = new Vector();
	Vector VAMT_INR = new Vector();
	Vector VINV_TYPE = new Vector();
	Vector VDESK_NAME = new Vector();
	Vector VRES_COLLECTION_PRTY = new Vector();
	Vector VRTL_GPL_TRADER = new Vector();
	Vector VCLRNG_DOC = new Vector();
	Vector VCLRNG_DT = new Vector();
	Vector VWBS_PNL = new Vector();
	Vector VBL_DT = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VCATEGORY = new Vector();
	Vector VCURRANCY = new Vector();
	Vector VAMT_USD = new Vector();
	Vector VARREARS_DAYS = new Vector();
	Vector VAGING = new Vector();
	Vector VOVERDUE_COZ = new Vector();
	Vector VDUE_AMT_USD = new Vector();
	Vector VMST_BANK_CD = new Vector();
	Vector VMST_BANK_ABBR = new Vector();
	Vector VMST_BANK_NM = new Vector();
	Vector VBANK_LIMIT_USD = new Vector();
	Vector VBANK_LIMIT = new Vector();
	Vector VEXPOSURE_INR = new Vector();
	Vector VEXPOSURE_USD = new Vector();
	Vector VAVAILABILITY_INR = new Vector();
	Vector VAVAILABILITY_USD = new Vector();
	Vector VISS_BANK_CD = new Vector();
	Vector VISS_BANK_NM = new Vector();
	Vector VADV_BANK_CD = new Vector();
	Vector VADV_BANK_NM = new Vector();
	Vector VADV_BANK_REF = new Vector();
	Vector VCONFIRM_BANK_CD = new Vector();
	Vector VCONFIRM_BANK_NM = new Vector();
	Vector VCONFIRM_BANK_REF = new Vector();
	Vector VBANK_EXPOSURE_INR = new Vector();
	Vector VBANK_EXPOSURE_USD = new Vector();
	Vector VBANK_AVAILABILITY_INR = new Vector();
	Vector VBANK_AVAILABILITY_USD = new Vector();
	Vector VEXPOSURE_HEADING = new Vector();
	Vector VEXPOSURE_HEADING_FLAG = new Vector();
	Vector VGX = new Vector();
	Vector VHEADING_INFO = new Vector();
	Vector VRATE_UNIT_NM = new Vector();
	Vector VCOLLATERAL_INFO = new Vector();
	Vector VBILLED_AMT = new Vector();
	Vector VBILLED_QTY = new Vector();
	Vector VBILLED_AMT_INFO = new Vector();
	Vector VUNBILLED_AMT = new Vector();
	Vector VUNBILLED_AMT_INFO = new Vector();
	Vector VUNBILLED_CURRENT_MONTH = new Vector();
	Vector VUNBILLED_CURRENT_MONTH_INFO = new Vector();
	Vector VFORWARD_NOTIONAL = new Vector();
	Vector VFORWARD_NOTIONAL_INFO = new Vector();
	Vector VGROSS_EXPOSURE = new Vector();
	Vector VGROSS_EXPOSURE_INFO = new Vector();
	Vector VGROSS_EXPOSURE_TAX = new Vector();
	Vector VGROSS_EXPOSURE_TAX_INFO = new Vector();
	Vector VCOLLATERAL_VALUE = new Vector();
	Vector VNET_EXPOSURE = new Vector();
	Vector VNET_EXPOSURE_INFO = new Vector();
	Vector VEXCHG_RATE = new Vector();
	Vector VPARENT_CD = new Vector();
	Vector VLIMIT_PARENT_FLAG = new Vector();
	Vector VPARENT_LIMIT_VALUE = new Vector();
	Vector VLIMIT_VALUE_LINKED = new Vector();
	Vector VCREDIT_EXCEED_BALANCE = new Vector();
	Vector VCONSUMED_LIMIT = new Vector();
	Vector VCREDIT_EXCEED = new Vector();
	Vector VCREDIT_EXCEED_INFO = new Vector();
	Vector VLIMIT_VALUE_LINKED_COMP = new Vector();
	Vector VLIMIT = new Vector();
	Vector VNET_EXPOSURE_USD = new Vector();
	Vector VCREDIT_EXCEED_USD = new Vector();
	Vector VCREDIT_EXCEED_REASON = new Vector();
	Vector VCREDIT_EXCEED_REASON_FLAG = new Vector();
	Vector VMAILINDEX = new Vector();
	Vector VVALUE_USD = new Vector();
	Vector VEXCHANGE_RATE = new Vector();
	Vector VDOUBLE_COUNTERPARTY_CD = new Vector();
	Vector VDOUBLE_SEC_CATEGORY = new Vector();
	Vector VDOUBLE_STATUS = new Vector();
	Vector VDOUBLE_SEC_TYPE = new Vector();
	Vector VDOUBLE_CONFIRM_BANK_CD = new Vector();
	Vector VDOUBLE_VALUE = new Vector();
	Vector VDOUBLE_VALUE_USD = new Vector();
	Vector V_SEC_TYPE = new Vector();
	Vector V_SEC_REF_NO = new Vector();
	Vector V_STATUS = new Vector();
	Vector V_VALUE = new Vector();
	Vector V_VALUE_USD = new Vector();
	Vector V_ISS_BANK_REF = new Vector();
	Vector V_EXPIRE_DT = new Vector();
	Vector V_REMARK = new Vector();
	Vector V_COUNTERPARTY_NM = new Vector();
	Vector V_COUNTERPARTY_ABBR = new Vector();
	Vector V_SEC_CATEGRY = new Vector();
	Vector V_CURRENCY = new Vector();
	Vector V_DEAL_NO = new Vector();
	Vector V_DEAL_DTL = new Vector();
	Vector V_PREVIOUS_DT = new Vector();
	Vector V_FORM_C = new Vector();
	Vector VFORM_C = new Vector();
	Vector VCREDIT_RATING = new Vector();
	Vector VAVAILABLE = new Vector();
	Vector VTOTAL_LIMIT = new Vector();
	Vector VUNSECURED = new Vector();
	Vector VTEMPORARY = new Vector();
	Vector VADJUST_USAGE = new Vector();
	Vector VUSAGE = new Vector();
	Vector VNET_USAGE = new Vector();
	Vector VUSED = new Vector();
	Vector V_CREDIT_RATING = new Vector();
	Vector V_AVAILABLE = new Vector();
	Vector V_TOTAL_LIMIT = new Vector();
	Vector V_UNSECURED = new Vector();
	Vector V_TEMPORARY = new Vector();
	Vector V_ADJUST_USAGE = new Vector();
	Vector V_USAGE = new Vector();
	Vector V_NET_USAGE = new Vector();
	Vector V_USED = new Vector();
	Vector V_DEAL_TYPE = new Vector();
	Vector V_DUE_DT = new Vector();
	Vector V_DIS_DT = new Vector();
	Vector VMARGIN_AVAIL = new Vector();
	Vector VMARGIN_USED = new Vector();
	Vector VBG_VALUE = new Vector();
	Vector VINFO = new Vector();
	
	Vector VCOMP_CD = new Vector();
	Vector VCOMP_ABBR = new Vector();
	Vector V_LEGAL_ENTITY = new Vector();
	
	Vector VCHARGE_ABBR = new Vector();
	Vector VCHARGE_NAME = new Vector();
	Vector VLEGAL_ENTITY_ABBR = new Vector();
	
	Vector VLC_AMT = new Vector();
	Vector VOTH_COLLAT = new Vector();
	Vector VCASH_COLLATERAL_INFO = new Vector();
	Vector VPCG_INFO = new Vector();
	Vector VCASH_COLLATERAL_VALUE = new Vector();
	Vector VPCG_VALUE = new Vector();
	Vector VGX_COUNTERPARTY_CD = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	Vector V_DIS_CONTRACT_TYPE = new Vector();
}