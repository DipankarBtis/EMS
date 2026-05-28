package com.etrm.fms.credit_risk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Arth Patel
//Code Reviewed by	:  
//CR Date			: 01/09/2023 
//Status	  		: Developing

public class DB_CreditRisk_Report 
{
	String db_src_file_name="DB_CreditRisk_Report.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt01;
	PreparedStatement stmt11;
	PreparedStatement stmt12;
	PreparedStatement stmt13;
	PreparedStatement stmt14;
	PreparedStatement stmt15;
	PreparedStatement stmt16;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	PreparedStatement stmt_temp2;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset01;
	ResultSet rset11;
	ResultSet rset12;
	ResultSet rset13;
	ResultSet rset14;
	ResultSet rset15;
	ResultSet rset16;
	ResultSet rset_temp;
	ResultSet rset_temp1;
	ResultSet rset_temp2;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString11="";
	String queryString12="";
	String queryString13="";
	String queryString14="";
	String queryString15="";
	String queryString16="";
	String queryString_temp="";
	String queryString_temp1="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
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
//	    			stmt = conn.createStatement();
//	    			stmt1= conn.createStatement();
//	    			stmt2= conn.createStatement();	
//	    			stmt3= conn.createStatement();
//	    			stmt4= conn.createStatement();
//	    			stmt5= conn.createStatement();
//	    			stmt11= conn.createStatement();
//	    			stmt12= conn.createStatement();	
//	    			stmt13= conn.createStatement();
//	    			stmt14= conn.createStatement();
//	    			stmt15= conn.createStatement();
//	    			stmt16= conn.createStatement();
//	    			stmt_temp= conn.createStatement();
//	    			stmt_temp1= conn.createStatement();
	    			
	    			if(callFlag.equalsIgnoreCase("COLLATERAL_AUDIT_REPORT"))
	    			{
	    				getCollateralAuditReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SECURITY_STATUS_REPORT"))
	    			{
	    				getSecurityStatusDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("UPCOMING_SECURITY_REPORT"))
	    			{
	    				getUpcomingSecurityDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LIMIT_SUMMARY"))
	    			{
	    				getLimitSummaryDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ALL_SECURITY_REPORT"))
	    			{
	    				getAllSecurityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("WALKFORWARD_EXPOSURE_REPORT"))
	    			{
	    				getCounterpartyDetails();
	    				getAllWalkforwardExposureDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PAYMENT_RECEIPT_STATUS"))
	    			{
	    				getPaymentReceiptStatusDetails();
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
	    	if(rset0 != null){try{rset0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset01 != null){try{rset01.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset11 != null){try{rset11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset12 != null){try{rset12.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset13 != null){try{rset13.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset14 != null){try{rset14.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset15 != null){try{rset15.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset16 != null){try{rset16.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp2 != null){try{rset_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt01 != null){try{stmt01.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt11 != null){try{stmt11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt12 != null){try{stmt12.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt13 != null){try{stmt13.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt14 != null){try{stmt14.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt15 != null){try{stmt15.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt16 != null){try{stmt16.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	private void getAllWalkforwardExposureDtls() 
	{
		String function_nm="getAllWalkforwardExposureDtls()";
		try
		{
			
			String start_dt = "";
			String end_dt = "";
			String view_dt = "";
			String first_dt = "";
			String sysdt_2 = "";
			
			fetchSecuredUnsecuredDealId(from_dt);
			
			int IGXDropOffDays=3;
			int d = 0;
			int cnt=0;
			if(!report_days.equals(""))
			{
				d = Integer.parseInt(report_days) - 1;
				queryString = "SELECT TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),"
									+ "TO_CHAR(SYSDATE+?,'DD/MM/YYYY'),"
									+ "TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),"
									+ "TO_CHAR(TRUNC(TO_DATE(?,'DD/MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD')) - 1),'DD/MM/YYYY'),"
									+ "TO_CHAR(SYSDATE-2,'DD/MM/YYYY') "
									+ "FROM DUAL";
			}
			else
			{
				queryString = "select TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),"
									+ "TO_CHAR(ADD_MONTHS(SYSDATE-1,6),'DD/MM/YYYY'),"
									+ "TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),"
									+ "TO_CHAR(TRUNC(TO_DATE(?,'DD/MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD')) - 1),'DD/MM/YYYY'),"
									+ "TO_CHAR(SYSDATE-2,'DD/MM/YYYY') "
									+ "FROM DUAL";
			}
			stmt = conn.prepareStatement(queryString);
			if(!report_days.equals(""))
			{
				stmt.setInt(++cnt, d);
				stmt.setString(++cnt, from_dt);
				stmt.setString(++cnt, from_dt);
			}
			else
			{
				stmt.setString(++cnt, from_dt);
				stmt.setString(++cnt, from_dt);
			}
			rset = stmt.executeQuery();
			if(rset.next())
			{
				start_dt=rset.getString(1);
				end_dt = rset.getString(2);
				view_dt = rset.getString(3);
				first_dt = rset.getString(4);
				sysdt_2 = rset.getString(5);
				
				int days = 0;
				queryString1="SELECT TO_DATE(?,'DD/MM/YYYY') - TO_DATE(?,'DD/MM/YYYY')-1 FROM DUAL";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, start_dt);
				stmt1.setString(2, end_dt);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					days = (-1)*rset1.getInt(1)/7;
				}
				VEXPOSURE_DT.add(view_dt);
				rset1.close();
				stmt1.close();
				
				for(int i=0; i<days; i++)
				{
					queryString2="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+7,'DD/MM/YYYY'),TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+7,'DD/MM') FROM DUAL WHERE TO_DATE(?,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, start_dt);
					stmt2.setString(2, start_dt);
					stmt2.setString(3, start_dt);
					stmt2.setString(4, end_dt);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						start_dt=rset2.getString(1);
						VEXPOSURE_DT.add(start_dt);
					}
					rset2.close();
					stmt2.close();
				}
			}
			rset.close();
			stmt.close();
			
			queryString = "SELECT COUNT(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE "//COMPANY_CD=? AND "
					+ "REPORT_DT = TO_DATE(?,'DD/MM/YYYY')";
			stmt0 = conn.prepareStatement(queryString);
			//stmt0.setString(1, comp_cd);
			stmt0.setString(1, view_dt);
			rset0 = stmt0.executeQuery();
			if(rset0.next())
			{
				if(rset0.getInt(1) > 0)
				{
				}
				else
				{
					view_dt = sysdt_2;
				}
			}
			rset0.close();
			stmt0.close();
			double monthTotal = 0;
			for(int j =0; j< VEXPOSURE_DT.size(); j++)
			{
				double limit = 0; double parentLimit = 0;
				queryString ="SELECT SUM(AMT) "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND GX=? AND EFF_DT<= TO_DATE(?,'DD/MM/YYYY') AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
						+ "AND (INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?)) ";
				stmt1 = conn.prepareStatement(queryString);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, clearance);
				stmt1.setString(3, ""+VEXPOSURE_DT.elementAt(j));
				stmt1.setString(4, ""+VEXPOSURE_DT.elementAt(j));
				stmt1.setString(5, ""+VEXPOSURE_DT.elementAt(j));
				stmt1.setString(6, ""+VEXPOSURE_DT.elementAt(j));
				stmt1.setString(7, "Adjust Limit");
				stmt1.setString(8, "Adjust Usage");
				stmt1.setString(9, "Unsecured");
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					limit = rset1.getDouble(1);
					if(limit < 0)
					{
						limit = 0;
					}
					VOPENEXPOSURE.add("0.00");
				}
				rset1.close();
				stmt1.close();
				
				queryString = "SELECT DISTINCT PARENT_OWNSHIP_CD,PARENT_OWNSHIP "
						+ "FROM FMS_LIMIT_MST " 
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND GX=? AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL)";
				stmt2 = conn.prepareStatement(queryString);
				//stmt2.setString(1, comp_cd);
				stmt2.setString(1, counterparty_cd);
				stmt2.setString(2, clearance);
				stmt2.setString(3, ""+VEXPOSURE_DT.elementAt(j));
				stmt2.setString(4, ""+VEXPOSURE_DT.elementAt(j));
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					String parent_cd = rset2.getString(1)==null?"":rset2.getString(1);
					/*queryString1 ="SELECT SUM(AMT) "
							+ "FROM FMS_LIMIT_DTL "
							+ "WHERE COUNTERPARTY_CD=? AND GX=? AND EFF_DT<= TO_DATE(?,'DD/MM/YYYY') AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
							+ "AND (INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?)) ";
					stmt01 = conn.prepareStatement(queryString1);
					stmt01.setString(1, parent_cd);
					stmt01.setString(2, clearance);
					stmt01.setString(3, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(4, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(5, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(6, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(7, "Adjust Limit");
					stmt01.setString(8, "Adjust Usage");
					stmt01.setString(9, "Unsecured");
					rset01 = stmt01.executeQuery();*/
					
					queryString1 ="SELECT SUM(AMT) "
							+ "FROM FMS_LIMIT_DTL "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=? AND GX=? AND EFF_DT<= TO_DATE(?,'DD/MM/YYYY') AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
							+ "AND (INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?)) ";
					stmt01 = conn.prepareStatement(queryString1);
					//stmt1.setString(1, comp_cd);
					stmt01.setString(1, parent_cd);
					stmt01.setString(2, clearance);
					stmt01.setString(3, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(4, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(5, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(6, ""+VEXPOSURE_DT.elementAt(j));
					stmt01.setString(7, "Adjust Limit");
					stmt01.setString(8, "Adjust Usage");
					stmt01.setString(9, "Unsecured");
					rset01 = stmt01.executeQuery();
					if(rset01.next())
					{
						parentLimit = rset01.getDouble(1);
						if(parentLimit < 0 )
						{
							parentLimit = 0;
						}
						if(VPARENT_CD.size() == 0)
						{
							VPARENT_CD.add(parent_cd);
						}
						else
						{
							for(int k=0; k<VPARENT_CD.size(); k++)
							{
								if(!VPARENT_CD.elementAt(k).equals(parent_cd))
								{
									VPARENT_CD.add(parent_cd);
								}
							}
						}
					}
					rset01.close();
					stmt01.close();
				}
				rset2.close();
				stmt2.close();
				
				double total_limit = limit + parentLimit;
				VPARENT_LIMIT.add(nf.format(parentLimit));
				VLIMIT_CALCULATION.add(nf.format(total_limit));
				double countSettlementExpo = 0;
				int countExpo=0;
				int check = 0;
				int month_total_flag = 0;
				
				String prev_dealId="";
				String prev_custCd="";
				double prev_temp_IGXTotal=0;
				if(clearance.equals("I"))
				{
					monthTotal=0;
				}
				for(int x=0; x < CHILDPARENT_CD.size(); x++)
				{
					for(int a=0; a < VUNSECURED_DEAL.size(); a++)
					{
						String vunscured_deal [] = VUNSECURED_DEAL.elementAt(a).toString().split("@");
						if(clearance.equals("I"))
						{
							if(j==0)
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
										+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') AND CONTRACT_TYPE IN(?,?) ";
							}
							else
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
										+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') AND CONTRACT_TYPE IN(?,?) ";
							}
						}
						else
						{
							if(j==0)
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE " //COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE "//N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
										+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?,?,?)";
							}
							else
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE "//COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE "//N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
										+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?,?,?)";
							}
						}
						stmt3 = conn.prepareStatement(queryString);
						if(clearance.equals("I"))
						{
							if(j==0)
							{
								stmt3.setString(1, vunscured_deal[0]);
								stmt3.setString(2, ""+VUNSECURED_CUST_CD.elementAt(a));
								stmt3.setString(3, vunscured_deal[0]);
								stmt3.setString(4, view_dt);
								stmt3.setString(5, vunscured_deal[1]);
	//							stmt3.setString(3, ""+VUNSECURED_DEAL.elementAt(a));
								stmt3.setString(6, first_dt);
								stmt3.setString(7, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(8, "X");
								stmt3.setString(9, "W");
	//							//stmt3.setString(1, comp_cd);
	//							stmt3.setString(1, ""+VUNSECURED_CUST_CD.elementAt(a));
	//							//stmt3.setString(3, comp_cd);
	//							stmt3.setString(2, view_dt);
	//							stmt3.setString(3, vunscured_deal[1]);
	////							stmt3.setString(3, ""+VUNSECURED_DEAL.elementAt(a));
	//							stmt3.setString(4, first_dt);
	//							stmt3.setString(5, ""+VEXPOSURE_DT.elementAt(j));
	//							stmt3.setString(6, "X");
							}
							else
							{
								stmt3.setString(1, vunscured_deal[0]);
								stmt3.setString(2, ""+VUNSECURED_CUST_CD.elementAt(a));
								stmt3.setString(3, vunscured_deal[0]);
								stmt3.setString(4, view_dt);
								stmt3.setString(5, vunscured_deal[1]);
	//							stmt3.setString(3, ""+VUNSECURED_DEAL.elementAt(a));
								stmt3.setString(6, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(7, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(8, "X");
								stmt3.setString(9, "W");
	//							//stmt3.setString(1, comp_cd);
	//							stmt3.setString(1, ""+VUNSECURED_CUST_CD.elementAt(a));
	//							//stmt3.setString(3, comp_cd);
	//							stmt3.setString(2, view_dt);
	//							stmt3.setString(3, vunscured_deal[1]);
	////							stmt3.setString(3, ""+VUNSECURED_DEAL.elementAt(a));
	//							stmt3.setString(4, ""+VEXPOSURE_DT.elementAt(j));
	//							stmt3.setString(5, ""+VEXPOSURE_DT.elementAt(j));
	//							stmt3.setString(6, "X");
							}
						}
						else
						{
							if(j==0)
							{
								stmt3.setString(1, vunscured_deal[0]);
								//stmt3.setString(2, comp_cd);
								stmt3.setString(2, ""+CHILDPARENT_CD.elementAt(x));
								//stmt3.setString(4, comp_cd);
								stmt3.setString(3, ""+CHILDPARENT_CD.elementAt(x));
								stmt3.setString(4, vunscured_deal[0]);
								stmt3.setString(5, view_dt);
								stmt3.setString(6, vunscured_deal[1]);
	//							stmt3.setString(4, ""+VUNSECURED_DEAL.elementAt(a));
								stmt3.setString(7, first_dt);
								stmt3.setString(8, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(9, "S");
								stmt3.setString(10, "L");
								stmt3.setString(11, "E");
								stmt3.setString(12, "F");
							}
							else
							{
								stmt3.setString(1, vunscured_deal[0]);
								//stmt3.setString(2, comp_cd);
								stmt3.setString(2, ""+CHILDPARENT_CD.elementAt(x));
								//stmt3.setString(4, comp_cd);
								stmt3.setString(3, ""+CHILDPARENT_CD.elementAt(x));
								stmt3.setString(4, vunscured_deal[0]);
								stmt3.setString(5, view_dt);
								stmt3.setString(6, vunscured_deal[1]);
	//							stmt3.setString(4, ""+VUNSECURED_DEAL.elementAt(a));
								stmt3.setString(7, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(8, ""+VEXPOSURE_DT.elementAt(j));
								stmt3.setString(9, "S");
								stmt3.setString(10, "L");
								stmt3.setString(11, "E");
								stmt3.setString(12, "F");
							}
						}
						rset3 = stmt3.executeQuery();
						while(rset3.next())
						{
							check = 1;
							String deal_id = rset3.getString(2)==null?"":rset3.getString(2);
							String countpty_cd = rset3.getString(3)==null?"":rset3.getString(3);
							String price_type = rset3.getString(4)==null?"":rset3.getString(4);
							
							double cont_price = rset3.getDouble(5); 
							double fwd_price_fin = rset3.getDouble(6);
							double eff_deal_price = rset3.getDouble(7);
							String dlvdt = rset3.getString(8)==null?"":rset3.getString(8);
							String contType = rset3.getString(9)==null?"":rset3.getString(9);
							String temp_account = rset3.getString(10)==null?"":rset3.getString(10);
							String company_cd = rset3.getString(11)==null?"":rset3.getString(11);
							double total = 0;
							
							String dealNosplit[] = deal_id.split("-");
							String account = "";
							
							if(!account.equals(""))
							{
								account+=", "+temp_account;
							}
							else
							{
								account+=""+temp_account;
							}
							String agmt_no = dealNosplit[1];
							String cont_no = dealNosplit[2];
							
							String disp_deal_id = utilBean.NewDealMappingId(company_cd, countpty_cd, agmt_no, "", cont_no, "", contType, "");
							String disp_cont_type = utilBean.getContractTypeName(contType);
							double TranValue=0;
							String rate_unit="2";
							
							if(temp_account.equals("Sell"))
							{
								queryString1 = "SELECT TRANSPORTATION_CHARGE,RATE,RATE_UNIT "
										+ "FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD = ? AND AGMT_NO=? "
										+ "AND CONT_NO = ? AND CONTRACT_TYPE=? ";
							}
							else if(temp_account.equals("Buy"))
							{
								queryString1 = "SELECT TRANSPORTATION_CHARGE,RATE,RATE_UNIT "
										+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD = ? AND AGMT_NO=? "
										+ "AND CONT_NO = ? AND CONTRACT_TYPE=? ";
							}
							stmt4 = conn.prepareStatement(queryString1);
							stmt4.setString(1, company_cd);
							stmt4.setString(2, countpty_cd);
							stmt4.setString(3, agmt_no);
							stmt4.setString(4, cont_no);
							stmt4.setString(5, contType);
							rset4 = stmt4.executeQuery();
							if(rset4.next())
							{
								TranValue = rset4.getDouble(1);
								cont_price = rset4.getDouble(2);
								rate_unit = rset4.getString(3)==null?"2":rset4.getString(3);
								rate_unit = rate_unit.trim();
							}
							rset4.close();
							stmt4.close();
							
							if(price_type.equals("Fixed"))
							{
								total = rset3.getDouble(1) * cont_price;
							}
							else
							{
								if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
								{
									total = rset3.getDouble(1) * fwd_price_fin;
								}
								else
								{
									total = rset3.getDouble(1) * eff_deal_price;
								}
							}
							total = Double.parseDouble(nf.format(total));
							String ExchRateCd="";
							double AvailableExchgRate=0;
							double USDtoINR=0;
							
							if(rate_unit.equals("2"))
							{
								AvailableExchgRate=getExchangeRate(company_cd, countpty_cd, agmt_no, cont_no, contType, first_dt);
								USDtoINR = total*AvailableExchgRate;
								USDtoINR = Double.parseDouble(nf.format(USDtoINR));
							}
							else
							{
								USDtoINR = total;
								USDtoINR = Double.parseDouble(nf.format(USDtoINR));
							}
							double AvgTaxInInv = 0;
							queryString2 = "SELECT MAX(FACTOR) "
									+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A, FMS_TAX_STRUCTURE_DTL B "
									+ "WHERE A.COMPANY_CD=? AND  "
									+ "A.TAX_STRUCT_CD=B.TAX_STR_CD AND COUNTERPARTY_CD=? AND APP_DATE <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
									+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND A.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
							stmt5 = conn.prepareStatement(queryString2);
							stmt5.setString(1, company_cd);
							stmt5.setString(2, countpty_cd);
							stmt5.setString(3, ""+VEXPOSURE_DT.elementAt(j));
							stmt5.setString(4, ""+VEXPOSURE_DT.elementAt(j));
							rset5 = stmt5.executeQuery();
							if(rset5.next())
							{
								AvgTaxInInv=rset5.getDouble(1);
							}
							rset5.close();
							stmt5.close();
							
							double ApplyTax = USDtoINR * (AvgTaxInInv/100);
							ApplyTax = Double.parseDouble(nf.format(ApplyTax));
							
							double AddTransCharge = 0;
							if(TranValue > 0)
							{
								AddTransCharge = rset3.getDouble(1) * TranValue;
							}
							AddTransCharge = Double.parseDouble(nf.format(AddTransCharge));
							countSettlementExpo += USDtoINR + ApplyTax + AddTransCharge;
							double temp_IGXTotal=0;
							if(clearance.equals("I"))
							{
								if(!deal_id.equals(prev_dealId))
								{
									monthTotal += prev_temp_IGXTotal;
									TEMP_IGX_CUMTOTAL.clear();
								}
								
								TEMP_IGX_CUMTOTAL.add(""+countSettlementExpo);
								if(TEMP_IGX_CUMTOTAL.size()>=(IGXDropOffDays+1))
								{
									temp_IGXTotal = (countSettlementExpo) - (Double.parseDouble(""+TEMP_IGX_CUMTOTAL.elementAt(TEMP_IGX_CUMTOTAL.size()-(IGXDropOffDays+1))));
								}
								else
								{
									temp_IGXTotal = (countSettlementExpo);
								}
								
								prev_dealId = deal_id;
								prev_custCd = countpty_cd;
								prev_temp_IGXTotal = temp_IGXTotal;
							}
							else
							{
								if(dlvdt.substring(0,2).equals("01"))
								{
									month_total_flag = 1;
									monthTotal = 0;
									monthTotal += countSettlementExpo;
								}
								else
								{
									monthTotal += USDtoINR + ApplyTax + AddTransCharge;
								}
							}
							int count=0;
							if(VDEAL_ID.size()==0)
							{
								VDEAL_ID.add(company_cd+"@"+deal_id);
								VDISP_DEAL_ID.add(disp_deal_id);
								VDIS_CONTRACT_TYPE.add(disp_cont_type);
								VCOUNTERPARTY_CD.add(countpty_cd);
								VEXCHGRATE.add(nf2.format(AvailableExchgRate));
								VTAXRATE.add(nf.format(AvgTaxInInv));
								VBUYSELL.add("Sell");
								VTRANSVALUE.add(nf.format(TranValue));
								if(rate_unit.equals("1"))
								{
									VRATE.add(nf.format(cont_price));
								}
								else
								{
									VRATE.add(nf2.format(cont_price));
								}
								VRATE_UNIT.add(rate_unit);
							}
							for(int i=0; i < VDEAL_ID.size(); i++)
							{
								if(VDEAL_ID.elementAt(i).equals(company_cd+"@"+deal_id))
								{
									count=count+1;
								}
							}
							if(count==0)
							{
								VDEAL_ID.add(company_cd+"@"+deal_id);
								VDISP_DEAL_ID.add(disp_deal_id);
								VDIS_CONTRACT_TYPE.add(disp_cont_type);
								VCOUNTERPARTY_CD.add(countpty_cd);
								VEXCHGRATE.add(nf2.format(AvailableExchgRate));
								VTAXRATE.add(nf.format(AvgTaxInInv));
								VBUYSELL.add("Sell");
								VTRANSVALUE.add(nf.format(TranValue));
								if(rate_unit.equals("1"))
								{
									VRATE.add(nf.format(cont_price));
								}
								else
								{
									VRATE.add(nf2.format(cont_price));
								}
								VRATE_UNIT.add(rate_unit);
							}
						}
						rset3.close();
						stmt3.close();
					}
				}
				
				if(clearance.equals("I")){
					monthTotal += prev_temp_IGXTotal;
				}
				
				if(check == 0)
				{
					monthTotal=0;
				}
				if(countSettlementExpo >= 0)
				{
					VTOTAL.add(nf.format(countSettlementExpo));
				}
				else
				{
					countSettlementExpo = (-1)*(countSettlementExpo);
					VTOTAL.add(nf.format(countSettlementExpo));
				}
				
				double availableCredit = 0;
				if(monthTotal >= 0)
				{	
					VCUMULATIVEEXPOSURE.add(nf.format(monthTotal));
					VSETTLEMENTSELLCREDIT.add(nf.format(monthTotal));
					availableCredit = total_limit - monthTotal;
				}
				else
				{
					double t = (-1) * monthTotal;
					VSETTLEMENTSELLCREDIT.add(nf.format(t));
					VCUMULATIVEEXPOSURE.add(nf.format(t));
					availableCredit = total_limit - t;
				}
				if(availableCredit<0)
				{
					availableCredit=0;
				}
				VAVAILABLECREDIT.add(nf.format(availableCredit));
			}
			
			for(int j=0; j<VDEAL_ID.size();j++)
			{
				String unsecure_deal_id [] = VDEAL_ID.elementAt(j).toString().split("@");
				queryString = "SELECT COUNTERPARTY_NM FROM FMS_COUNTERPARTY_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=?";
				stmt1 = conn.prepareStatement(queryString);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, ""+VCOUNTERPARTY_CD.elementAt(j));
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					VCOUNTERPARTY_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
				}
				rset1.close();
				stmt1.close();
				
				double cumulativeTotal = 0;
				double igx_cumu_total=0;
				
				for(int i =0; i< VEXPOSURE_DT.size(); i++)
				{
					String information = "";
					double sum_grandTotal = 0;
					int countExpo=0;
					
					if(cumulativeTotal >= 0)
					{ 
						VCUSTWISESETTLEMENTSELLCREDITFORPREVDT.add(nf.format(cumulativeTotal));
					}
					else
					{
						VCUSTWISESETTLEMENTSELLCREDITFORPREVDT.add(nf.format((-1)*cumulativeTotal));
					}
					
					int check = 0;
					if(clearance.equals("I"))
					{
						if(i == 0)
						{
							queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),COMPANY_CD "
									+ "FROM FMS_MR_EXPO_EOD_DTL "
									+ "WHERE COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
									+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
									+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CONTRACT_TYPE IN(?,?)";
						}
						else
						{
							queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),COMPANY_CD "
									+ "FROM FMS_MR_EXPO_EOD_DTL "
									+ "WHERE COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
									+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
									+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CONTRACT_TYPE IN(?,?)";
						}
					}
					else
					{
						if(i == 0)
						{
							queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),COMPANY_CD "
									+ "FROM FMS_MR_EXPO_EOD_DTL "
									+ "WHERE COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
									+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
									+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CONTRACT_TYPE IN(?,?,?,?)";
						}
						else
						{
							queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),COMPANY_CD "
									+ "FROM FMS_MR_EXPO_EOD_DTL "
									+ "WHERE COMPANY_CD=? AND "
									+ "COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD=? AND "
									+ "REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
									+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CONTRACT_TYPE IN(?,?,?,?)";
						}
					}
					
					stmt2 = conn.prepareStatement(queryString);
					if(clearance.equals("I"))
					{
						if(i == 0)
						{
							stmt2.setString(1, unsecure_deal_id[0]);
							stmt2.setString(2, ""+VCOUNTERPARTY_CD.elementAt(j));
							stmt2.setString(3, unsecure_deal_id[1]);
							stmt2.setString(4, unsecure_deal_id[0]);
							stmt2.setString(5, view_dt);
							stmt2.setString(6, first_dt);
							stmt2.setString(7, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(8, "X");
							stmt2.setString(9, "W");
						}
						else
						{
							stmt2.setString(1, unsecure_deal_id[0]);
							stmt2.setString(2, ""+VCOUNTERPARTY_CD.elementAt(j));
							stmt2.setString(3,unsecure_deal_id[1]);
							stmt2.setString(4, unsecure_deal_id[0]);
							stmt2.setString(5, view_dt);
							stmt2.setString(6, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(7, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(8, "X");
							stmt2.setString(9, "W");
						}
					}
					else
					{
						if(i == 0)
						{
							stmt2.setString(1, unsecure_deal_id[0]);
							stmt2.setString(2, ""+VCOUNTERPARTY_CD.elementAt(j));
							stmt2.setString(3, unsecure_deal_id[1]);
							stmt2.setString(4, unsecure_deal_id[0]);
							stmt2.setString(5, view_dt);
							stmt2.setString(6, first_dt);
							stmt2.setString(7, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(8, "S");
							stmt2.setString(9, "L");
							stmt2.setString(10, "E");
							stmt2.setString(11, "F");
						}
						else
						{
							stmt2.setString(1, unsecure_deal_id[0]);
							stmt2.setString(2, ""+VCOUNTERPARTY_CD.elementAt(j));
							stmt2.setString(3,unsecure_deal_id[1]);
							stmt2.setString(4, unsecure_deal_id[0]);
							stmt2.setString(5, view_dt);
							stmt2.setString(6, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(7, ""+VEXPOSURE_DT.elementAt(i));
							stmt2.setString(8, "S");
							stmt2.setString(9, "L");
							stmt2.setString(10, "E");
							stmt2.setString(11, "F");
						}
					}
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						check = 1;
						String price_type = rset2.getString(4)==null?"":rset2.getString(4);
						String delv_dt = rset2.getString(8)==null?"":rset2.getString(8);
						String company_cd = rset2.getString(9)==null?"":rset2.getString(9);
						String comp_abbr=utilBean.getCompanyAbbr(conn,company_cd);
						double dcq = rset2.getDouble(1);
						
						double cont_price = rset2.getDouble(5);
						double fwd_price_fin = rset2.getDouble(6);
						double eff_deal_price = rset2.getDouble(7);
						
						double total = 0;
						information += "Date "+delv_dt+"~Legal_entity = "+comp_abbr+"~Price_Type = "+price_type+"~DCQ = "+rset2.getDouble(1)+"~";
						
						VOC_DELV_DT.add(delv_dt);
						VOC_COMP_PROFILE.add(comp_abbr);
						VOC_PRICE_TYPE.add(price_type);
						VOC_DCQ.add(dcq);
						
						if(price_type.equals("Fixed"))
						{
							cont_price = Double.parseDouble(""+VRATE.elementAt(j));
							if(VRATE_UNIT.elementAt(j).equals("1")) 
							{
								information+="Cont_Price = "+nf.format(cont_price)+"";
								VOC_CONT_PRICE.add(nf.format(cont_price));
							}
							else 
							{
								information+="Cont_Price = "+nf2.format(cont_price)+"";	
								VOC_CONT_PRICE.add(nf2.format(cont_price));
							}
							total = rset2.getDouble(1) * cont_price;
						}
						else
						{
							if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
							{
								information+="Fwd_Price_Fin_Leg = "+nf2.format(fwd_price_fin)+"";
								total = rset2.getDouble(1) * fwd_price_fin;
								VOC_CONT_PRICE.add(nf.format(fwd_price_fin));
							}
							else
							{
								information+="Eff_Deal_Price = "+nf2.format(eff_deal_price)+"";
								total = rset2.getDouble(1) * eff_deal_price;
								VOC_CONT_PRICE.add(nf.format(eff_deal_price));
							}
						}
						total = Double.parseDouble(nf.format(total));
						
						double USDtoINR = 0;
						if(VRATE_UNIT.elementAt(j).equals("1")) 
						{
							information += "~Total = "+nf.format(total)+"";
							USDtoINR = total;
							VOC_TOTAL.add(nf.format(total));
							VOC_EXCH_RATE.add("0.00");
						}
						else 
						{
							information += "~Total = "+nf.format(total)+"~ExchgRate = "+VEXCHGRATE.elementAt(j)+"";
							USDtoINR = total * Double.parseDouble(""+VEXCHGRATE.elementAt(j));
							VOC_TOTAL.add(nf.format(total));
							VOC_EXCH_RATE.add(""+VEXCHGRATE.elementAt(j));
						}
						
						USDtoINR = Double.parseDouble(nf.format(USDtoINR));
						information += "~ApplyTax = "+VTAXRATE.elementAt(j)+"";
						double ApplyTax = USDtoINR * (Double.parseDouble(""+VTAXRATE.elementAt(j))/100); 
						ApplyTax = Double.parseDouble(nf.format(ApplyTax));
						
						VOC_APPLY_TAX.add(""+VTAXRATE.elementAt(j));
						
						double TranValue = Double.parseDouble(""+VTRANSVALUE.elementAt(j));
						double AddTransCharge = 0;
						if(TranValue > 0)
						{
							AddTransCharge = rset2.getDouble(1) * TranValue;
							information += "~TransValue = "+nf.format(TranValue)+"";
							VOC_TRANS_CHARGE.add(nf.format(TranValue));
						}
						else
						{
							VOC_TRANS_CHARGE.add("0.00");
						}
						
						AddTransCharge = Double.parseDouble(nf.format(AddTransCharge));
						double grandTotal = USDtoINR + ApplyTax + AddTransCharge; 
						information += "~Grand_Total = "+nf.format(grandTotal)+" ";
						VOC_GRAND_TOTAL.add(nf.format(grandTotal));
						
						sum_grandTotal += grandTotal;
						igx_cumu_total += grandTotal;
						
						if(clearance.equals("I"))
						{
							TEMP_IGX_CUMTOTAL.add(""+igx_cumu_total);
							if(TEMP_IGX_CUMTOTAL.size()>=(IGXDropOffDays+1))
							{
								cumulativeTotal = igx_cumu_total - (Double.parseDouble(""+TEMP_IGX_CUMTOTAL.elementAt(TEMP_IGX_CUMTOTAL.size()-(IGXDropOffDays+1))));
							}
							else
							{
								cumulativeTotal = igx_cumu_total;
							}
						}
						else
						{
							if(delv_dt.substring(0,2).equals("01"))
							{
								cumulativeTotal = 0;
								cumulativeTotal += sum_grandTotal;
							}
							else
							{
								cumulativeTotal += USDtoINR + ApplyTax + AddTransCharge;
							}
						}
						information += "~Cumulative_Total = "+nf.format(cumulativeTotal)+" ";
						VOC_CUMULATIVE_TOTAL.add(nf.format(cumulativeTotal));
					}
					rset2.close();
					stmt2.close();
					
					if(check == 0)
					{
						cumulativeTotal=0;
					}
					if(cumulativeTotal >= 0)
					{
						VCUSTWISESETTLEMENTSELLCREDIT.add(nf.format(cumulativeTotal));
						
					}
					else
					{
						VCUSTWISESETTLEMENTSELLCREDIT.add(nf.format((-1)*cumulativeTotal));
						
					}
					
					VINFO.add(information);
				}
				V_EXPOSURE_DATE.addAll(VEXPOSURE_DT);
			}
			
			for(int j=0; j<VPARENT_CD.size();j++)
			{
				queryString = "SELECT COUNTERPARTY_NM FROM FMS_COUNTERPARTY_MST WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=?";
				stmt1 = conn.prepareStatement(queryString);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, ""+VPARENT_CD.elementAt(j));
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					VPARENT_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
				}
				rset1.close();
				stmt1.close();
				
				for(int i =0; i< VEXPOSURE_DT.size(); i++)
				{
					double parentLimit =0;
					queryString = "SELECT DISTINCT PARENT_OWNSHIP_CD,PARENT_OWNSHIP "
							+ "FROM FMS_LIMIT_MST " 
							+ "WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=? AND GX=? AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL)";
					stmt2 = conn.prepareStatement(queryString);
					//stmt2.setString(1, comp_cd);
					stmt2.setString(1, counterparty_cd);
					stmt2.setString(2, clearance);
					stmt2.setString(3, ""+VEXPOSURE_DT.elementAt(i));
					stmt2.setString(4, ""+VEXPOSURE_DT.elementAt(i));
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						String parent_cd = rset2.getString(1)==null?"":rset2.getString(1);
						queryString1 ="SELECT SUM(AMT) "
								+ "FROM FMS_LIMIT_DTL "
								+ "WHERE "//COMPANY_CD=? AND "
								+ "COUNTERPARTY_CD=? AND GX=? AND EFF_DT<= TO_DATE(?,'DD/MM/YYYY') AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) "
								+ "OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) AND (INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?)) ";
						stmt3 = conn.prepareStatement(queryString1);
						//stmt3.setString(1, comp_cd);
						stmt3.setString(1, parent_cd);
						stmt3.setString(2, clearance);
						stmt3.setString(3, ""+VEXPOSURE_DT.elementAt(i));
						stmt3.setString(4, ""+VEXPOSURE_DT.elementAt(i));
						stmt3.setString(5, ""+VEXPOSURE_DT.elementAt(i));
						stmt3.setString(6, ""+VEXPOSURE_DT.elementAt(i));
						stmt3.setString(7, "Adjust Limit");
						stmt3.setString(8, "Adjust Usage");
						stmt3.setString(9, "Unsecured");
						rset3 = stmt3.executeQuery();
						if(rset3.next())
						{
							parentLimit = rset3.getDouble(1);
							if(parentLimit < 0 )
							{
								parentLimit = 0;
							}
						}
						rset3.close();
						stmt3.close();
					}
					rset2.close();
					stmt2.close();
					//VPARENT_LIMIT.add(nf.format(parentLimit));
				}
			}
			countSecuredDealSettlmentSellExposure(view_dt,first_dt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void countSecuredDealSettlmentSellExposure(String view_dt,String first_dt)
	{
		String function_nm="countSecuredDealSettlmentSellExposure()";
		try
		{
			double monthTotal = 0;
			for(int j =0; j< VEXPOSURE_DT.size(); j++)
			{
				double countSettlementExpo = 0;
				int countExpo=0;
				
				int check = 0;
				int month_total_flag = 0;
				
				//for(int x=0; x < CHILDPARENT_CD.size(); x++)
				{
					for(int a=0; a < VSECURED_DEAL.size(); a++)
					{
						String vsecure_deal [] = VSECURED_DEAL.elementAt(a).toString().split("@");
						if(clearance.equals("I"))
						{
							if(j==0)
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE " //COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE " //N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?) AND COMPANY_CD=?";
							}
							else
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE " //COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE " //N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?) AND COMPANY_CD=?";
							}
						}
						else
						{
							if(j==0)
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE " //COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE " //N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?,?,?) AND COMPANY_CD=?";
							}
							else
							{
								queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
										+ "FROM FMS_MR_EXPO_EOD_DTL "
										+ "WHERE COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE " //COMPANY_CD=? AND "
										+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE " //N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
										+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
										+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
										+ "AND MAPPING_ID=? "
										+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
										+ "AND CONTRACT_TYPE IN(?,?,?,?) AND COMPANY_CD=?";
							}
						}
						stmt = conn.prepareStatement(queryString);
						if(clearance.equals("I"))
						{
							if(j==0)
							{
								//stmt.setString(1, comp_cd);
								stmt.setString(1, counterparty_cd);
								//stmt.setString(3, comp_cd);
								stmt.setString(2, counterparty_cd);
								stmt.setString(3, view_dt);
								stmt.setString(4, vsecure_deal[1]);
		//						stmt.setString(4, ""+VSECURED_DEAL.elementAt(a));
								stmt.setString(5, first_dt);
								stmt.setString(6, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(7, "X");
								stmt.setString(8, "W");
								stmt.setString(9, vsecure_deal[0]);
							}
							else
							{
								//stmt.setString(1, comp_cd);
								stmt.setString(1, counterparty_cd);
								//stmt.setString(3, comp_cd);
								stmt.setString(2, counterparty_cd);
								stmt.setString(3, view_dt);
								stmt.setString(4, vsecure_deal[1]);
		//						stmt.setString(4, ""+VSECURED_DEAL.elementAt(a));
								stmt.setString(5, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(6, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(7, "X");
								stmt.setString(8, "W");
								stmt.setString(9, vsecure_deal[0]);
							}
						}
						else
						{
							if(j==0)
							{
								//stmt.setString(1, comp_cd);
								stmt.setString(1, counterparty_cd);
								//stmt.setString(3, comp_cd);
								stmt.setString(2, counterparty_cd);
								stmt.setString(3, view_dt);
								stmt.setString(4, vsecure_deal[1]);
		//						stmt.setString(4, ""+VSECURED_DEAL.elementAt(a));
								stmt.setString(5, first_dt);
								stmt.setString(6, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(7, "S");
								stmt.setString(8, "L");
								stmt.setString(9, "E");
								stmt.setString(10, "F");
								stmt.setString(11, vsecure_deal[0]);
							}
							else
							{
								//stmt.setString(1, comp_cd);
								stmt.setString(1, counterparty_cd);
								//stmt.setString(3, comp_cd);
								stmt.setString(2, counterparty_cd);
								stmt.setString(3, view_dt);
								stmt.setString(4, vsecure_deal[1]);
		//						stmt.setString(4, ""+VSECURED_DEAL.elementAt(a));
								stmt.setString(5, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(6, ""+VEXPOSURE_DT.elementAt(j));
								stmt.setString(7, "S");
								stmt.setString(8, "L");
								stmt.setString(9, "E");
								stmt.setString(10, "F");
								stmt.setString(11, vsecure_deal[0]);
							}
						}
						rset = stmt.executeQuery();
						while(rset.next())
						{
							check = 1;
							String deal_id = rset.getString(2)==null?"":rset.getString(2);
							String countpty_cd = rset.getString(3)==null?"":rset.getString(3);
							String price_type = rset.getString(4)==null?"":rset.getString(4);
							
							double cont_price = rset.getDouble(5); 
							double fwd_price_fin = rset.getDouble(6);
							double eff_deal_price = rset.getDouble(7);
							String dlvdt = rset.getString(8)==null?"":rset.getString(8);
							String contType = rset.getString(9)==null?"":rset.getString(9);
							String temp_account = rset.getString(10)==null?"":rset.getString(10);
							String company_cd = rset.getString(11)==null?"":rset.getString(11);
							double total = 0;
							
							
							String dealNosplit[] = deal_id.split("-");
							
							String account = "";
							if(!account.equals(""))
							{
								account+=", "+temp_account;
							}
							else
							{
								account+=""+temp_account;
							}
							String agmt_no = dealNosplit[1];
							String cont_no = dealNosplit[2];
							double TranValue=0;
							String rate_unit="2";
							String disp_deal_id = utilBean.NewDealMappingId(company_cd, countpty_cd, agmt_no, "", cont_no, "", contType, "");
							String disp_cont_type=utilBean.getContractTypeName(contType);
							
							if(temp_account.equals("Sell"))
							{
								queryString1 = "SELECT TRANSPORTATION_CHARGE,RATE,RATE_UNIT "
										+ "FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD = ? AND AGMT_NO=? "
										+ "AND CONT_NO = ? AND CONTRACT_TYPE=? ";
							}
							else if(temp_account.equals("Buy"))
							{
								queryString1 = "SELECT TRANSPORTATION_CHARGE,RATE,RATE_UNIT "
										+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD = ? AND AGMT_NO=? "
										+ "AND CONT_NO = ? AND CONTRACT_TYPE=? ";
							}
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, company_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, agmt_no);
							stmt1.setString(4, cont_no);
							stmt1.setString(5, contType);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								TranValue = rset1.getDouble(1);
								cont_price = rset1.getDouble(2);
								rate_unit = rset1.getString(3)==null?"2":rset1.getString(3);
								rate_unit = rate_unit.trim();
							}
							rset1.close();
							stmt1.close();
							
							if(price_type.equals("Fixed"))
							{
								total = rset.getDouble(1) * cont_price;
							}
							else
							{
								if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
								{
									total = rset.getDouble(1) * fwd_price_fin;
								}
								else
								{
									total = rset.getDouble(1) * eff_deal_price;
								}
							}
							total = Double.parseDouble(nf.format(total));
							String ExchRateCd="";
							double AvailableExchgRate=0;
							double USDtoINR=0;
							
							if(rate_unit.equals("2"))
							{
								AvailableExchgRate=getExchangeRate(company_cd, counterparty_cd, agmt_no, cont_no, contType, first_dt);
								
								USDtoINR = total * AvailableExchgRate;
								USDtoINR = Double.parseDouble(nf.format(USDtoINR));
							}
							else
							{
								USDtoINR = total;
								USDtoINR = Double.parseDouble(nf.format(USDtoINR));
							}
							double AvgTaxInInv = 0;
							queryString2 = "SELECT MAX(FACTOR) "
									+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A, FMS_TAX_STRUCTURE_DTL B "
									+ "WHERE A.COMPANY_CD=? AND  A.TAX_STRUCT_CD=B.TAX_STR_CD AND COUNTERPARTY_CD=? AND APP_DATE <= TO_DATE(?,'DD/MM/YYYY') "
									+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
									+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND A.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, company_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, ""+VEXPOSURE_DT.elementAt(j));
							stmt2.setString(4, ""+VEXPOSURE_DT.elementAt(j));
							rset2 = stmt2.executeQuery();
							if(rset2.next())
							{
								AvgTaxInInv=rset2.getDouble(1);
							}
							rset2.close();
							stmt2.close();
							
							double ApplyTax = USDtoINR * (AvgTaxInInv/100);
							ApplyTax = Double.parseDouble(nf.format(ApplyTax));
							
							double AddTransCharge = 0;
							if(TranValue > 0)
							{
								AddTransCharge = rset.getDouble(1) * TranValue;
							}
							AddTransCharge = Double.parseDouble(nf.format(AddTransCharge));
							countSettlementExpo += USDtoINR + ApplyTax + AddTransCharge;
							
							if(dlvdt.substring(0,2).equals("01"))
							{
								month_total_flag = 1;
								monthTotal = 0;
								monthTotal += countSettlementExpo;
							}
							else
							{
								monthTotal += USDtoINR + ApplyTax + AddTransCharge;
							}
							int count=0;
							if(VSEC_DEAL_ID.size()==0)
							{
								VSEC_DEAL_ID.add(company_cd+"@"+deal_id);
								VSEC_DISP_DEAL_ID.add(disp_deal_id);
								VSEC_DIS_CONTRACT_TYPE.add(disp_cont_type);
								VSEC_COUNTERPARTY_CD.add(countpty_cd);
								VSEC_EXCHGRATE.add(nf2.format(AvailableExchgRate));
								VSEC_TAXRATE.add(nf.format(AvgTaxInInv));
								VSEC_BUYSELL.add("Sell");
								VSEC_TRANSVALUE.add(nf.format(TranValue));
								if(rate_unit.equals("1"))
								{
									VSEC_RATE.add(nf.format(cont_price));
								}
								else
								{
									VSEC_RATE.add(nf2.format(cont_price));
								}
								VSEC_RATE_UNIT.add(rate_unit);
							}
							for(int i=0; i < VSEC_DEAL_ID.size(); i++)
							{
								if(VSEC_DEAL_ID.elementAt(i).equals(company_cd+"@"+deal_id))
								{
									count=count+1;
								}
							}
							if(count==0)
							{
								VSEC_DEAL_ID.add(company_cd+"@"+deal_id);
								VSEC_DISP_DEAL_ID.add(disp_deal_id);
								VSEC_DIS_CONTRACT_TYPE.add(disp_cont_type);
								VSEC_COUNTERPARTY_CD.add(countpty_cd);
								VSEC_EXCHGRATE.add(nf2.format(AvailableExchgRate));
								VSEC_TAXRATE.add(nf.format(AvgTaxInInv));
								VSEC_BUYSELL.add("Sell");
								VSEC_TRANSVALUE.add(nf.format(TranValue));
								if(rate_unit.equals("1"))
								{
									VSEC_RATE.add(nf.format(cont_price));
								}
								else
								{
									VSEC_RATE.add(nf2.format(cont_price));
								}
								VSEC_RATE_UNIT.add(rate_unit);
							}
						}
						rset.close();
						stmt.close();
					}
				}
				
				
				if(check == 0)
				{
					monthTotal=0;
				}
				if(countSettlementExpo >= 0)
				{
					VTOTAL.add(nf.format(countSettlementExpo));
				}
				else
				{
					countSettlementExpo = (-1)*(countSettlementExpo);
					VTOTAL.add(nf.format(countSettlementExpo));
				}
				
				double availableCredit = 0;
				if(monthTotal >= 0)
				{
					VSETTLEMENTSELLEXPOSUR.add(nf.format(monthTotal));
				}
				else
				{
					double t = (-1) * monthTotal;
					VSETTLEMENTSELLEXPOSUR.add(nf.format(t));
				}
			}
			
			for(int j=0; j<VSEC_DEAL_ID.size();j++)
			{
				String sec_deal [] = VSEC_DEAL_ID.elementAt(j).toString().split("@");
				queryString = "SELECT COUNTERPARTY_NM "
						+ "FROM FMS_COUNTERPARTY_MST "
						+ "WHERE " //COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=?";
				stmt = conn.prepareStatement(queryString);
//				stmt.setString(1, comp_cd);
				stmt.setString(1, ""+VSEC_COUNTERPARTY_CD.elementAt(j));
				rset = stmt.executeQuery();
				if(rset.next())
				{
					VSEC_COUNTERPARTY_NM.add(rset.getString(1)==null?"":rset.getString(1));
				}
				rset.close();
				stmt.close();
				
				double cumulativeTotal = 0;
				for(int i =0; i< VEXPOSURE_DT.size(); i++)
				{
					String information = "";
					double sum_grandTotal = 0;
					int countExpo=0;
					
					if(cumulativeTotal >= 0)
					{ 
						VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT.add(nf.format(cumulativeTotal));
					}
					else
					{
						VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT.add(nf.format((-1)*cumulativeTotal));
					}
					
					int check = 0;
					if(i == 0)
					{
						queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
								+ "FROM FMS_MR_EXPO_EOD_DTL "
								+ "WHERE COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
								+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
								+ "AND CONTRACT_TYPE IN(?,?,?,?) AND COMPANY_CD=? ";
					}
					else
					{
						queryString = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
								+ "FROM FMS_MR_EXPO_EOD_DTL "
								+ "WHERE COUNTERPARTY_CD=? AND MAPPING_ID = ? AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
								+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY')-6 AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "
								+ "AND CONTRACT_TYPE IN(?,?,?,?) AND COMPANY_CD=? ";
					}
					stmt1 = conn.prepareStatement(queryString);
					if(i == 0)
					{
						stmt1.setString(1, ""+VSEC_COUNTERPARTY_CD.elementAt(j));
						stmt1.setString(2, sec_deal[1]);
//						stmt1.setString(2, ""+VSEC_DEAL_ID.elementAt(j));
						stmt1.setString(3, view_dt);
						stmt1.setString(4, first_dt);
						stmt1.setString(5, ""+VEXPOSURE_DT.elementAt(i));
						stmt1.setString(6, "S");
						stmt1.setString(7, "L");
						stmt1.setString(8, "E");
						stmt1.setString(9, "F");
						stmt1.setString(10, sec_deal[0]);
					}
					else
					{
						stmt1.setString(1, ""+VSEC_COUNTERPARTY_CD.elementAt(j));
						stmt1.setString(2, sec_deal[1]);
						//stmt1.setString(2, ""+VSEC_DEAL_ID.elementAt(j));
						stmt1.setString(3, view_dt);
						stmt1.setString(4, ""+VEXPOSURE_DT.elementAt(i));
						stmt1.setString(5, ""+VEXPOSURE_DT.elementAt(i));
						stmt1.setString(6, "S");
						stmt1.setString(7, "L");
						stmt1.setString(8, "E");
						stmt1.setString(9, "F");
						stmt1.setString(10, sec_deal[0]);
					}
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						check = 1;
						String price_type = rset1.getString(4)==null?"":rset1.getString(4);
						String  delv_dt = rset1.getString(8)==null?"":rset1.getString(8);
						String company_cd = rset1.getString(11)==null?"":rset1.getString(11);
						String comp_abbr=utilBean.getCompanyAbbr(conn,company_cd);
						double dcq = rset1.getDouble(1);
						
						double cont_price = rset1.getDouble(5);
						double fwd_price_fin = rset1.getDouble(6);
						double eff_deal_price = rset1.getDouble(7);
						
						double total = 0;
						information += "Date "+delv_dt+"~Legal_entity = "+comp_abbr+"~Price_Type = "+price_type+"~DCQ = "+rset1.getDouble(1)+"~";
						VEXPO_DELV_DT.add(delv_dt);
						VEXPO_COMP_PROFILE.add(comp_abbr);
						VEXPO_PRICE_TYPE.add(price_type);
						VEXPO_DCQ.add(dcq);
						
						if(price_type.equals("Fixed"))
						{
							cont_price = Double.parseDouble(""+VSEC_RATE.elementAt(j));
							if(VSEC_RATE_UNIT.elementAt(j).equals("1"))
							{
								information+="Cont_Price = "+nf.format(cont_price)+"";
								VEXPO_CONT_PRICE.add(nf.format(cont_price));
							}
							else
							{
								information+="Cont_Price = "+nf2.format(cont_price)+"";	
								VEXPO_CONT_PRICE.add(nf2.format(cont_price));
							}
							total = rset1.getDouble(1) * cont_price;
						}
						else
						{
							if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
							{
								information+="Fwd_Price_Fin_Leg = "+nf2.format(fwd_price_fin)+"";
								total = rset1.getDouble(1) * fwd_price_fin;
								VEXPO_CONT_PRICE.add(nf.format(fwd_price_fin));
							}
							else
							{
								information+="Eff_Deal_Price = "+nf2.format(eff_deal_price)+"";
								total = rset1.getDouble(1) * eff_deal_price;
								VEXPO_CONT_PRICE.add(nf.format(eff_deal_price));
							}
						}
						total = Double.parseDouble(nf.format(total));
						
						double USDtoINR = 0;
						if(VSEC_RATE_UNIT.elementAt(j).equals("1")) 
						{
							information += "~Total = "+nf.format(total)+"";
							USDtoINR = total;
							VEXPO_TOTAL.add(nf.format(total));
							VEXPO_EXCH_RATE.add("0.00");
						}
						else 
						{
							information += "~Total = "+nf.format(total)+"~ExchgRate = "+VSEC_EXCHGRATE.elementAt(j)+"";
							USDtoINR = total * Double.parseDouble(""+VSEC_EXCHGRATE.elementAt(j));
							VEXPO_TOTAL.add(nf.format(total));
							VEXPO_EXCH_RATE.add(""+VSEC_EXCHGRATE.elementAt(j));
						}
						
						USDtoINR = Double.parseDouble(nf.format(USDtoINR));
						information += "~ApplyTax = "+VSEC_TAXRATE.elementAt(j)+"";
						double ApplyTax = USDtoINR * (Double.parseDouble(""+VSEC_TAXRATE.elementAt(j))/100); 
						ApplyTax = Double.parseDouble(nf.format(ApplyTax));
						VEXPO_APPLY_TAX.add(""+VSEC_TAXRATE.elementAt(j));
						
						double TranValue = Double.parseDouble(""+VSEC_TRANSVALUE.elementAt(j));
						double AddTransCharge = 0;
						if(TranValue > 0)
						{
							AddTransCharge = rset1.getDouble(1) * TranValue;
							information += "~TransValue = "+nf.format(TranValue)+"";
							VEXPO_TRANS_CHARGE.add(nf.format(TranValue));
						}
						else
						{
							VEXPO_TRANS_CHARGE.add("0.00");
						}
						AddTransCharge = Double.parseDouble(nf.format(AddTransCharge));
						double grandTotal = USDtoINR + ApplyTax + AddTransCharge; 
						information += "~Grand_Total = "+nf.format(grandTotal)+" ";
						VEXPO_GRAND_TOTAL.add(nf.format(grandTotal));
						
						sum_grandTotal += grandTotal;
						
						if(delv_dt.substring(0,2).equals("01"))
						{
							cumulativeTotal = 0;
							cumulativeTotal += sum_grandTotal;
						}
						else
						{
							cumulativeTotal += USDtoINR + ApplyTax + AddTransCharge;
						}
						information += "~Cumulative_Total = "+nf.format(cumulativeTotal)+" ";
						VEXPO_CUMULATIVE_TOTAL.add(nf.format(cumulativeTotal));
					}
					rset1.close();
					stmt1.close();
					
					if(check == 0)
					{
						cumulativeTotal=0;
					}
					if(cumulativeTotal >= 0){ 
						VCUSTWISESETTLEMENTSELLEXPOSURE.add(nf.format(cumulativeTotal));
					}else{
						VCUSTWISESETTLEMENTSELLEXPOSURE.add(nf.format((-1)*cumulativeTotal));
					}
					
					VINFO1.add(information);
				}
				VSEC_EXPOSURE_DATE.addAll(VEXPOSURE_DT);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void fetchSecuredUnsecuredDealId(String date)
	{
		String function_nm="fetchSecuredUnsecuredDealId()";
		try
		{
			String Childparent_cd ="";
			
			queryString = "SELECT DISTINCT PARENT_OWNSHIP_CD "
					+ "FROM FMS_LIMIT_MST " 
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND GX=? AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL) ";
			//queryString += " UNION ALL ";
			stmt2 = conn.prepareStatement(queryString);
			//stmt2.setString(1, comp_cd);
			stmt2.setString(1, counterparty_cd);
			stmt2.setString(2, clearance);
			stmt2.setString(3, date);
			stmt2.setString(4, date);
			rset2 = stmt2.executeQuery();
			if(rset2.next())
			{
				Childparent_cd = rset2.getString(1)==null?"":rset2.getString(1);
				
				CHILDPARENT_CD.add(Childparent_cd);
			}
			else
			{
				queryString1 = "SELECT COUNTERPARTY_CD "
						+ "FROM FMS_LIMIT_MST " 
						+ "WHERE "//COMPANY_CD=? AND "
						+ "PARENT_OWNSHIP_CD=? AND GX=? AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL) ";
				stmt3 = conn.prepareStatement(queryString1);
				//stmt2.setString(1, comp_cd);
				stmt3.setString(1, counterparty_cd);
				stmt3.setString(2, clearance);
				stmt3.setString(3, date);
				stmt3.setString(4, date);
				rset3 = stmt3.executeQuery();
				while(rset3.next())
				{
					Childparent_cd = rset3.getString(1)==null?"":rset3.getString(1);
					CHILDPARENT_CD.add(Childparent_cd);
				}
				rset3.close();
				stmt3.close();
			}
			CHILDPARENT_CD.add(counterparty_cd);
			rset2.close();
			stmt2.close();
			
			for(int i=0; i<CHILDPARENT_CD.size(); i++) 
			{
				if(clearance.equals("I") && !counterparty_cd.equals("")) 
				{
					queryString = "SELECT MAPPING_ID,COUNTERPARTY_CD,COMPANY_CD "
							+ "FROM FMS_MR_EXPO_EOD_MST "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND CONTRACT_TYPE IN(?,?)";
					stmt = conn.prepareStatement(queryString);
					//stmt.setString(1, comp_cd);
					stmt.setString(1, date);
					stmt.setString(2, "X");
					stmt.setString(3, "W");
					rset = stmt.executeQuery();
					while(rset.next())
					{
						String map_id=rset.getString(1)==null?"":rset.getString(1);
						String company_cd = rset.getString(3)==null?"":rset.getString(3);
						VUNSECURED_DEAL.add(company_cd+"@"+map_id);
						VUNSECURED_CUST_CD.add(rset.getString(2)==null?"":rset.getString(2));
						
					}
					rset.close();
					stmt.close();
				}
				else
				{
					queryString = "SELECT MAPPING_ID,CONTRACT_TYPE,COMPANY_CD "
							+ "FROM FMS_MR_EXPO_EOD_MST "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=(SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST M WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST N WHERE "//N.COMPANY_CD=? AND M.COMPANY_CD=N.COMPANY_CD AND "
							+ "N.COUNTERPARTY_CD=? AND M.COUNTERPARTY_CD=N.COUNTERPARTY_CD)) "
							+ "AND REPORT_DT = (SELECT MAX(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT <= TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND CONTRACT_TYPE IN(?,?,?,?)";
					stmt = conn.prepareStatement(queryString);
					//stmt.setString(1, comp_cd);
					//stmt.setString(2, comp_cd);
					stmt.setString(1, ""+CHILDPARENT_CD.elementAt(i));
					//stmt.setString(4, comp_cd);
					stmt.setString(2, ""+CHILDPARENT_CD.elementAt(i));
					stmt.setString(3, date);
					stmt.setString(4, "S");
					stmt.setString(5, "L");
					stmt.setString(6, "E");
					stmt.setString(7, "F");
					rset = stmt.executeQuery();
					while(rset.next())
					{
						String map_id=rset.getString(1)==null?"":rset.getString(1);
						String contType=rset.getString(2)==null?"":rset.getString(2);
						String company_cd=rset.getString(3)==null?"":rset.getString(3);
						String split[] = map_id.split("-");
						String countPtyCd = split[0];
						String agmtNo = split[1];
						String contNo = split[2];
						
						int LCBGcount=0,OAcount=0;
						queryString1 ="SELECT COUNT(*) "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B,LOG_FMS_SECURITY_MST C  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
								+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
								+ "AND A.SEC_CATEGORY='R' AND A.SEC_TYPE IN ('LC','BG') AND C.STATUS IN ('P') "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
								+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
								+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.SEQ_NO=C.SEQ_NO "
								+ "AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX AND C.LOG_SEQ_NO='1' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, company_cd);
						stmt1.setString(2, countPtyCd);
						stmt1.setString(3, "K");
						stmt1.setString(4, agmtNo);
						stmt1.setString(5, contNo);
						stmt1.setString(6, contType);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							LCBGcount=rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						
						queryString2 ="SELECT COUNT(*) "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B,LOG_FMS_SECURITY_MST C  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
								+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
								+ "AND A.SEC_CATEGORY='R' AND A.SEC_TYPE IN ('OA','PCG') AND C.STATUS IN ('P') "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
								+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
								+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.SEQ_NO=C.SEQ_NO "
								+ "AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX AND C.LOG_SEQ_NO='1' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, company_cd);
						stmt2.setString(2, countPtyCd);
						stmt2.setString(3, "K");
						stmt2.setString(4, agmtNo);
						stmt2.setString(5, contNo);
						stmt2.setString(6, contType);
						rset2 = stmt2.executeQuery();
						if(rset2.next())
						{
							OAcount=rset2.getInt(1);
						}
						rset2.close();
						stmt2.close();
						
						if(OAcount > 0 && LCBGcount==0)
						{
							VUNSECURED_DEAL.add(company_cd+"@"+map_id);	
						}
						else if(LCBGcount > 0)
						{
							VSECURED_DEAL.add(company_cd+"@"+map_id);
						}
					}
					rset.close();
					stmt.close();
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPaymentReceiptStatusDetails()
	{
		String function_nm="getPaymentReceiptStatusDetails()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
					+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,GX,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					//+ "WHERE COMPANY_CD=? AND SEC_TYPE IN (?,?,?) AND SEC_CATEGORY=? AND STATUS IN (?,?,?) " //ONLY FOR 'In order','Dummy','Pending for amendment' 
					+ "WHERE SEC_TYPE IN (?,?,?) AND SEC_CATEGORY=? AND STATUS IN (?,?,?) " //ONLY FOR 'In order','Dummy','Pending for amendment' 
					+ "AND INORDER_HIST=? AND ISSUE_DT <= TO_DATE(?,'DD/MM/YYYY') AND EXPIRE_DT >= TO_DATE(?,'DD/MM/YYYY') ";
			queryString+= "ORDER BY ENT_DT DESC";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "LC");
			stmt.setString(2, "BG");
			stmt.setString(3, "PCG");
			stmt.setString(4, "R");
			stmt.setString(5, "O");
			stmt.setString(6, "D");
			stmt.setString(7, "A");
			stmt.setString(8, "Y");
			stmt.setString(9, rpt_dt);
			stmt.setString(10, rpt_dt);
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
				String flucuation = rset.getString(11)==null?"":rset.getString(11);
				String iss_bank_cd = rset.getString(12)==null?"":rset.getString(12);
				String iss_bank_ref = rset.getString(13)==null?"":rset.getString(13);
				String adv_bank_cd = rset.getString(14)==null?"":rset.getString(14);
				String adv_bank_ref = rset.getString(15)==null?"":rset.getString(15);
				String confirm_bank_cd = rset.getString(16)==null?"":rset.getString(16);
				String confirm_bank_ref = rset.getString(17)==null?"":rset.getString(17);
				String issue_dt = rset.getString(18)==null?"":rset.getString(18);
				String expire_dt = rset.getString(19)==null?"":rset.getString(19);
				String review_dt = rset.getString(20)==null?"":rset.getString(20);
				String tenor = rset.getString(21)==null?"":rset.getString(21);
				String remarks = rset.getString(22)==null?"":rset.getString(22);
				String variation = rset.getString(23)==null?"":rset.getString(23);
				String guarantor_cd = rset.getString(24)==null?"":rset.getString(24);
				String cancel_date = rset.getString(25)==null?"":rset.getString(25);
				String renew_date = rset.getString(26)==null?"":rset.getString(26);
				String seq_rev_no = rset.getString(27)==null?"":rset.getString(27);
				String sap_approval = rset.getString(28)==null?"":rset.getString(28);
				String sap_reversal = rset.getString(29)==null?"":rset.getString(29);
				clearance = rset.getString(30)==null?"":rset.getString(30);
				String company_cd = rset.getString(31)==null?"":rset.getString(31);
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
					String sysdate= ""+dateUtil.getSysdate();
					String[] split_sys_date = sysdate.split("/");
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
				String match_deal_no="";
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
						+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
						//+ "AND CONTRACT_TYPE IN(?,?,?) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, counterPartyCd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, sec_ref_no);
				stmt1.setString(5, seq_rev_no);
				//stmt1.setString(6, "S");
				//stmt1.setString(7, "L");
				//stmt1.setString(8, "X");
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
							//dealNo+=", "+utilBean.getCounterpartyABBR(entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							match_deal_no+=", "+company_cd+"-"+counterparty_cd+"-"+agmt+"-%-"+cont+"-%-"+cont_type+"-%";
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							//dealNo+=utilBean.getCounterpartyABBR(entityCd, comp_cd)+"-"+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							match_deal_no+=company_cd+"-"+counterparty_cd+"-"+agmt+"-%-"+cont+"-%-"+cont_type+"-%";
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
							match_deal_no+=", "+company_cd+"-"+counterparty_cd+"-"+agmt+"-%-"+cont+"-%-"+cont_type+"-%";
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							//dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							match_deal_no+=company_cd+"-"+counterparty_cd+"-"+agmt+"-%-"+cont+"-%-"+cont_type+"-%";
						}
					}
				}
				rset1.close();
				stmt1.close();
				
				if(match_deal_no.contains(",") && !match_deal_no.equals("")) 
				{
					String[] spilt_dealNo = match_deal_no.split(", ");
					String[] spilt_showdealNo = dealNo.split(", ");
					for(int i=0; i<spilt_dealNo.length;i++) 
					{
						VMUL_DEALS.add(spilt_showdealNo[i]);
						queryString2 = "SELECT INVOICE_NO,AMT_DC,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),DUE_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),CO_CODE "
								+ "FROM VIEW_RECEIVABLE_MST "
								+ "WHERE CO_CODE=? AND COUNTERPARTY_NM=? AND COUNTERPARTY_ABBR=? AND DEAL_ASSIGNMENT LIKE (?) "
								+ "AND ((DUE_AMT > 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) "
								+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) "
								+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY')))";
						// HP FMS8 part
						/*+ "WHERE CUSTOMER_ABBR='"+custAbbr+"' AND DEAL_ASSIGNMENT='"+deal_no+"' AND TO_DATE(BL_DT,'DD/MM/YYYY')>= TO_DATE('01/04/2020','DD/MM/YYYY') "
						+ "AND ((DUE_AMT > 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE('"+to_date+"','DD/MM/YYYY')) "
						+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') = TO_DATE('"+to_date+"','DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE('"+to_date+"','DD/MM/YYYY')) "
						+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE('"+to_date+"','DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') = TO_DATE('"+to_date+"','DD/MM/YYYY')))";*/
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, company_cd);
						stmt2.setString(2, counterparty_nm);
						stmt2.setString(3, counterparty_abbr);
						stmt2.setString(4, spilt_dealNo[i]);
						stmt2.setString(5, rpt_dt);
						stmt2.setString(6, rpt_dt);
						stmt2.setString(7, rpt_dt);
						stmt2.setString(8, rpt_dt);
						stmt2.setString(9, rpt_dt);
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
							
							VCO_CODE.add(rset2.getString(6)==null?"":rset2.getString(6));
							String co_abbr = utilBean.getCompanyAbbr(conn,rset2.getString(6)==null?"":rset2.getString(6));
							
							VCO_ABBR.add(co_abbr);
							
							VCOUNTERPARTY_CD.add(counterPartyCd);
							VCOUNTERPARTY_NAME.add(counterparty_nm);
							VSEC_CATEGORY.add(sec_category);
							VSEC_TYPE.add(sec_type);
							VSEC_REF_NO.add(sec_ref_no);
							VSTATUS.add(status);
							VSTATUS_NM.add(""+getStatusName(status));
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
							String fin_deal_no[]=deal_number.split("-");
							
							cont_type = fin_deal_no[5];
							agmt = fin_deal_no[1];
							cont = fin_deal_no[3];
							/*
							 * else { cont_type = Character.toString(deal_number.charAt(0)); cont =
							 * deal_number.substring(1); agmt = "0"; }
							 */
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
								
								if(con_type.equals("X") || con_type.equals("W"))
								{
									cont_ref=rset3.getString(2)==null?"":rset3.getString(2);
								}
								String agmt_base=rset3.getString(3)==null?"":rset3.getString(3);
								
								if(agmt_base.equals("D"))
								{
									spilt_dealNo[i]+="<font style='background: #a6ff4d;'>[DLV]</font>";
								}
								if(!cont_ref.equals(""))
								{
									spilt_dealNo[i]+="<br>["+cont_ref+"]";
								}
							}
							VDEAL_REF_NO.add(spilt_showdealNo[i]);
							rset3.close();
							stmt3.close();
						}
						rset2.close();
						stmt2.close();
					}
				}
				else if(!match_deal_no.equals(""))
				{
					VMUL_DEALS.add(dealNo);
					
					queryString2 = "SELECT INVOICE_NO,AMT_DC,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),DUE_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),CO_CODE "
							+ "FROM VIEW_RECEIVABLE_MST "
							+ "WHERE CO_CODE=? AND COUNTERPARTY_NM=? AND COUNTERPARTY_ABBR=? AND DEAL_ASSIGNMENT LIKE (?) "
							+ "AND ((DUE_AMT > 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) "
							+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')) "
							+ "OR (DUE_AMT <= 2 AND TO_DATE(NET_DUE_DT,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(PAY_RECV_DT,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY')))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, counterparty_nm);
					stmt2.setString(3, counterparty_abbr);
					stmt2.setString(4, match_deal_no);
					stmt2.setString(5, rpt_dt);
					stmt2.setString(6, rpt_dt);
					stmt2.setString(7, rpt_dt);
					stmt2.setString(8, rpt_dt);
					stmt2.setString(9, rpt_dt);
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
						
						VCO_CODE.add(rset2.getString(6)==null?"":rset2.getString(6));
						String co_abbr = utilBean.getCompanyAbbr(conn,rset2.getString(6)==null?"":rset2.getString(6));
						
						VCO_ABBR.add(co_abbr);
						
						VCOUNTERPARTY_CD.add(counterPartyCd);
						VCOUNTERPARTY_NAME.add(counterparty_nm);
						VSEC_CATEGORY.add(sec_category);
						VSEC_TYPE.add(sec_type);
						VSEC_REF_NO.add(sec_ref_no);
						VSTATUS.add(status);
						VSTATUS_NM.add(""+getStatusName(status));
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
						String dis_cont_mapping=""+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						
						queryString3 = "SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE,CONT_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND AGMT_NO=? AND CONT_NO=?  "
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
							
							if(con_type.equals("X") || con_type.equals("W"))
							{
								cont_ref=rset3.getString(2)==null?"":rset3.getString(2);
							}
							String agmt_base=rset3.getString(3)==null?"":rset3.getString(3);
							
							if(agmt_base.equals("D"))
							{
								dis_cont_mapping+="<font style='background: #a6ff4d;'>[DLV]</font>";
							}
							if(!cont_ref.equals(""))
							{
								dis_cont_mapping+="<br>["+cont_ref+"]";
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
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyDetails() 
	{
		String function_nm="getCounterpartyDetails()";
		try
		{
			if(clearance.equals("I"))
			{
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";//AND A.COMPANY_CD=B.COMPANY_CD) ";
				queryString+= "ORDER BY COUNTERPARTY_NM";
				
			}
			else
			{
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "// AND A.COMPANY_CD=B.COMPANY_CD) "
						+ "AND STATUS=? AND KYC=? "
						+ "ORDER BY COUNTERPARTY_NM";
			}
			stmt = conn.prepareStatement(queryString);
			if(clearance.equals("I"))
			{
				//stmt.setString(1, comp_cd);
			}
			else
			{
				//stmt.setString(1, comp_cd);
				stmt.setString(1, "Y");
				stmt.setString(2, "Y");
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VMST_COUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getAllSecurityDetails()
	{
		String function_nm="getAllSecurityDetails()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
					+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE GX=? ";
					//+ "WHERE COMPANY_CD=? AND GX=? ";
			queryString+= "ORDER BY ENT_DT DESC";
			//HP20230920 ADDED SAP_REVERSAL COLUMN	
			//HP20230919 ADDED SAP_APPROVAL COLUMN	
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, comp_cd);
			stmt.setString(1, clearance);
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
				String flucuation = rset.getString(11)==null?"":rset.getString(11);
				String iss_bank_cd = rset.getString(12)==null?"":rset.getString(12);
				String iss_bank_ref = rset.getString(13)==null?"":rset.getString(13);
				String adv_bank_cd = rset.getString(14)==null?"":rset.getString(14);
				String adv_bank_ref = rset.getString(15)==null?"":rset.getString(15);
				String confirm_bank_cd = rset.getString(16)==null?"":rset.getString(16);
				String confirm_bank_ref = rset.getString(17)==null?"":rset.getString(17);
				String issue_dt = rset.getString(18)==null?"":rset.getString(18);
				String expire_dt = rset.getString(19)==null?"":rset.getString(19);
				String review_dt = rset.getString(20)==null?"":rset.getString(20);
				String tenor = rset.getString(21)==null?"":rset.getString(21);
				String remarks = rset.getString(22)==null?"":rset.getString(22);
				String variation = rset.getString(23)==null?"":rset.getString(23);
				String guarantor_cd = rset.getString(24)==null?"":rset.getString(24);
				String cancel_date = rset.getString(25)==null?"":rset.getString(25);
				String renew_date = rset.getString(26)==null?"":rset.getString(26);
				String seq_rev_no = rset.getString(27)==null?"":rset.getString(27);
				String sap_approval = rset.getString(28)==null?"":rset.getString(28);//HP20230919
				String sap_reversal = rset.getString(29)==null?"":rset.getString(29);//HP20230920
				String company_cd = rset.getString(30)==null?"":rset.getString(30);
				String counterparty_nm = "";
				if(clearance.equals("I"))//HP20230914
				{
					counterparty_nm = ""+utilBean.getGasExchangeName(conn,counterPartyCd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd)); //HP20230914
				}
				else
				{
					counterparty_nm = ""+utilBean.getCounterpartyName(conn,counterPartyCd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				String iss_bank_nm = ""+utilBean.getBankName(conn,iss_bank_cd);
				String adv_bank_nm = ""+utilBean.getBankName(conn,adv_bank_cd);
				String confirm_bank_nm = ""+utilBean.getBankName(conn,confirm_bank_cd);
				String guarantor_nm = ""+utilBean.getCounterpartyName(conn,guarantor_cd);
				String ref_no= company_cd+"-"+sec_ref_no;
				
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
				VCOUNTERPARTY_CD.add(counterPartyCd);
				VCOUNTERPARTY_NAME.add(counterparty_nm);
				VSEC_CATEGORY.add(sec_category);
				VSEC_TYPE.add(sec_type);
				VSEC_REF_NO.add(ref_no);
				VSTATUS.add(status);
				VSTATUS_NM.add(""+getStatusName(status));
				VCURRENCY.add(currency);
				VVALUE.add(value);
				VRECEIVED_DATE.add(received_date);
				VDEAL_TYPE.add(deal_type);
				VVALUE_FLUCTUATION.add(flucuation);
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
				VREVIEW_DT.add(review_dt);
				VTENOR.add(tenor);
				VREMARK.add(remarks);
				VVALUE_VARIATION.add(variation);
				VGUARANTOR_CD.add(guarantor_cd);
				VGUARANTOR_NM.add(guarantor_nm);
				VCANCEL_DT.add(cancel_date);
				VRENEW_DT.add(renew_date);
				VSEQ_NO.add(seq_no);
				VSEQ_REV_NO.add(seq_rev_no);
				
				VSAP_APPROVAL_FLAG.add(sap_approval);//HP20230919
				VSAP_REVERSAL_FLAG.add(sap_reversal);//HP20230920
				
				String exp_val = "";
				if(!expire_dt.equals(""))
				{
					String sysdate= ""+dateUtil.getSysdate();
					String[] split_sys_date = sysdate.split("/");
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
				String disp_cont_type="";
				queryString1 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD "
						+ "FROM FMS_SECURITY_DEAL_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEC_REF_NO=?  AND SEQ_REV_NO=? AND GX=?";
				//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, counterPartyCd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, sec_ref_no);
				stmt1.setString(5, seq_rev_no);
				stmt1.setString(6, clearance);
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
					
					if(clearance.equals("I"))
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
						
						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							sel_deal_dtl+="@@"+dealDtl;
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							dealNo+=utilBean.getCounterpartyABBR(conn,entityCd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
						
						//HP20230914 ADDED COUNTERPARTY_CD IN MAPPING ID AND ADDED COUNTERPARTY_ABBR IN DEAL NO FOR DISPLAY
					}
					else
					{
						String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
						
						if(!sel_deal_dtl.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							sel_deal_dtl+="@@"+dealDtl;
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							dealNo+=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							sel_deal_dtl+=""+dealDtl;
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
				}
				VDEAL_NO.add(dealNo);
				VDEAL_DTL.add(deal_dtl);
				VDIS_CONTRACT_TYPE.add(disp_cont_type);
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
	
	private void getLimitSummaryDtls() 
	{
		String function_nm="getLimitSummaryDtls()";
		try
		{
			String sysdate = dateUtil.getSysdate();
			
			int i=0;
			queryString2 = "SELECT DISTINCT COUNTERPARTY_CD,BANK_CD FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
					+ "GX=? ";
			stmt2 = conn.prepareStatement(queryString2);
			//stmt2.setString(1, comp_cd);
			stmt2.setString(1, clearance);
			rset2 = stmt2.executeQuery();
			while(rset2.next())
			{
				i++;
				String counterpty_Cd =  rset2.getString(1)==null?"":rset2.getString(1);
				String bank_Cd =  rset2.getString(2)==null?"":rset2.getString(2);
				
				if(!counterpty_Cd.equals("0") && bank_Cd.equals("0"))
				{
					if(clearance.equals("I"))
					{
						VCOUNTERPARTY_CD.add(counterpty_Cd);
						VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,counterpty_Cd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterpty_Cd));
					}
					else
					{
						VCOUNTERPARTY_CD.add(counterpty_Cd);
						VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,counterpty_Cd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_Cd));
					}
				}
				else if(counterpty_Cd.equals("0") && !bank_Cd.equals("0"))
				{
					VCOUNTERPARTY_CD.add(bank_Cd);
					VCOUNTERPARTY_NAME.add(""+utilBean.getBankName(conn,bank_Cd));
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
				
				double available = 0.00;
				double total_limit = 0.00;
				double unsecured = 0.00;
				double temporary =0.00;
				double adjust_usage = 0.00;
				double net_usage = 0.00;
				double usage = 0.00;
				double deal_usage = 0.00;
				double pcg_usage = 0.00;
				double used = 0.00;
				
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
					deal_usage = limit_usage_calculation(counterpty_Cd);
					pcg_usage = getPCGDtls(counterpty_Cd);
					
					usage=deal_usage;
				}
				else
				{
					VUSAGE_1.add("0.00");
					VUSAGE_2.add("0.00");
				}
				
				available = total_limit + adjust_usage - usage-pcg_usage;
				net_usage = usage - adjust_usage+pcg_usage;
				
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
				VUSED.add(nf2.format(used));
				
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private double getPCGDtls(String counterpty_Cd)
	{
		String function_nm="getPCGDtls()";
		double pcg_value=0;
		try
		{
			String sysdate = dateUtil.getSysdate();
			String pcg_info="";
			if(clearance.equals("K"))
			{
				String queryString3 = "SELECT COUNTERPARTY_CD "
						+ "FROM FMS_LIMIT_MST "
						+ "WHERE PARENT_OWNSHIP_CD=? "
						+ "AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL)"
						+ "AND GX=?";
				stmt5 = conn.prepareStatement(queryString3);
				stmt5.setString(1, counterpty_Cd);
				stmt5.setString(2, sysdate);
				stmt5.setString(3, sysdate);
				stmt5.setString(4, clearance);
				rset5 = stmt5.executeQuery();
				if(rset5.next())
				{
					String contpty_cd=rset5.getString(1)==null?"":rset5.getString(1);
				
					String queryString2 ="SELECT NVL(VALUE,0),CURRENCY,SEC_TYPE,SEC_REF_NO,TO_CHAR(EXPIRE_DT,'DD/MM/YYYY'),COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,STATUS "
							+ "FROM FMS_SECURITY_MST  "
							+ "WHERE GUARANTOR_CD=? "
							+ "AND GX=? "
							+ "AND SEC_CATEGORY='R' AND SEC_TYPE IN ('PCG') AND STATUS IN ('O','C','R') "
							+ "AND ISSUE_DT<=to_date(?,'DD/MM/YYYY') AND (EXPIRE_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND (TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) ";
					stmt3 = conn.prepareStatement(queryString2);
					stmt3.setString(1, counterpty_Cd);
					stmt3.setString(2, clearance);
					stmt3.setString(3, sysdate);
					stmt3.setString(4, sysdate);
					stmt3.setString(5, sysdate);
					rset3=stmt3.executeQuery();
					while(rset3.next())
					{
						double secuVal=rset3.getDouble(1);
						double oriSecuVal=secuVal;
						String currency=rset3.getString(2)==null?"":rset3.getString(2);
						String secType=rset3.getString(3)==null?"":rset3.getString(3);
						String sec_Ref=rset3.getString(4)==null?"":rset3.getString(4);
						String company_cd=rset3.getString(6)==null?"":rset3.getString(6);
						//String contpty_cd=rset3.getString(7)==null?"":rset3.getString(7);
						String seq_no=rset3.getString(8)==null?"":rset3.getString(8);
						String status=rset3.getString(9)==null?"":rset3.getString(9);
						String company_abbr = utilBean.getCompanyAbbr(conn,company_cd);
						String secRef=company_cd+"-"+sec_Ref;
						
						String agmt_no="";
						String agmt_rev="";
						String cont_no ="";
						String cont_rev="";
						String cont_typ="";
						double split_percent=0;
						String splitPercent="";
						String deal_no="";
						
						String queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,TRADE_REF_NO,"
								+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
								+ "RATE,RATE_UNIT,TCQ,DCQ,AGMT_BASE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),COMPANY_CD "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COUNTERPARTY_CD=? AND CONTRACT_TYPE IN ('S','L') ";
						queryString+= "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
						queryString+= "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "ORDER BY START_DT";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, contpty_cd);
						stmt.setString(2, sysdate);
						stmt.setString(3, sysdate);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							agmt_no=rset.getString(2)==null?"":rset.getString(2);
							agmt_rev=rset.getString(3)==null?"":rset.getString(3);
							cont_no=rset.getString(4)==null?"":rset.getString(4);
							cont_rev=rset.getString(5)==null?"":rset.getString(5);
							cont_typ=rset.getString(6)==null?"":rset.getString(6);
							
							deal_no=utilBean.NewDealMappingId(company_cd, contpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_typ, "");
						}
						rset.close();
						stmt.close();
						
						String queryString4="SELECT SHARE_PERCENT "
								+ "FROM FMS_SECURITY_DEAL_MAP "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? AND SEC_REF_NO=? AND GX=? AND "
								+ "AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, company_cd);
						stmt4.setString(2, contpty_cd);
						stmt4.setString(3, seq_no);
						stmt4.setString(4, sec_Ref);
						stmt4.setString(5, clearance);
						stmt4.setString(6, agmt_no);
						stmt4.setString(7, cont_no);
						stmt4.setString(8, cont_typ);
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							splitPercent=rset4.getString(1)==null?"":rset4.getString(1);
							split_percent=rset4.getDouble(1);
						}
						rset4.close();
						stmt4.close();
						
						
						double availableExchgRate = getExchangeRate(company_cd, contpty_cd, agmt_no, cont_no, cont_typ, sysdate);
						
						if(pcg_info.equals(""))
						{
							pcg_info = secType+" : "+secRef;
						}
						else
						{
							pcg_info += "\n"+secType+" : "+secRef;
						}
						
						if(!splitPercent.equals(""))
						{
							secuVal=(secuVal*split_percent)/100;
							if(currency.equals("2"))
							{
								pcg_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" USD)";
							}
							else
							{
								pcg_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" INR)";
							}
						}
						
						double amount=0;
						if(currency.equals("2"))
						{
							amount=secuVal*availableExchgRate;
							pcg_info +=" = ( "+nf.format(secuVal)+" USD * "+nf2.format(availableExchgRate)+") = "+nf.format(amount)+" INR";
						}
						else
						{
							amount=secuVal;
							pcg_info +=" = "+nf.format(amount)+" INR";
						}
						pcg_value+=amount;
					}
					rset3.close();
					stmt3.close();
				}
				rset5.close();
				stmt5.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return pcg_value;
	}
	
	private double limit_usage_calculation(String countpty_Cd) 
	{
		String function_nm="limit_usage_calculation()";
		double countSettlementExpo=0;
		double usage_1 = 0;
		double usage_2 = 0;
		try
		{
			String info = "";
			String sysdate="";
			String nextSevenDayDt="";
			String yesdt="";
			String firstDtofMonth = "";
			String sysdate_2="";
			String queryString13 = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY'),TO_CHAR(SYSDATE+6,'DD/MM/YYYY'),TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),TO_CHAR(SYSDATE-2,'DD/MM/YYYY') FROM DUAL";
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
			
			firstDtofMonth=dateUtil.getFirstDateOfMonth(sysdate);
			
			String queryString14 = "SELECT COUNT(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE REPORT_DT = TO_DATE(?,'DD/MM/YYYY')";
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
			
			
			String queryString15 = "SELECT DCQ,MAPPING_ID,COUNTERPARTY_CD,PRICE_TYPE,CONT_PRICE,FWD_PRICE_FIN,EFF_RATE_USD,TO_CHAR(GAS_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BUY_SELL,COMPANY_CD "
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
				String company_cd = rset15.getString(11)==null?"":rset15.getString(11);
				
				String countptyCd = rset15.getString(3)==null?"":rset15.getString(3);
				String price_type = rset15.getString(4)==null?"":rset15.getString(4);
				double cont_price = rset15.getDouble(5); 
				double fwd_price_fin = rset15.getDouble(6);
				double eff_deal_price = rset15.getDouble(7);
				String delv_dt = rset15.getString(8)==null?"":rset15.getString(8);
				String temp_account=rset15.getString(10)==null?"":rset15.getString(10);
				String comp_abbr=utilBean.getCompanyAbbr(conn,company_cd);
				String new_deal_id = utilBean.NewDealMappingId(company_cd, countptyCd, agmt_no, "", cont_no, "", contType, "");
				
				info += "DealId "+new_deal_id+"~"+comp_abbr+"~"+delv_dt+"~"+price_type+"~"+rset15.getDouble(1)+"~";
				double total = 0;
				double total_1 =0;
				double total_2=0;
				if(price_type.equals("Fixed"))
				{
					info+=""+cont_price+"";
					total = dcq * cont_price;
				}
				else
				{
					if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
					{
						info+=""+fwd_price_fin+"";
						total = dcq * fwd_price_fin;
					}
					else
					{
						info+=""+eff_deal_price+"";
						total = dcq * eff_deal_price;
					}
				}
				
				if(company_cd.equals("1"))
				{
					if(price_type.equals("Fixed"))
					{
						total_1 = dcq * cont_price;
					}
					else
					{
						if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
						{
							total_1 = dcq * fwd_price_fin;
						}
						else
						{
							total_1 = dcq * eff_deal_price;
						}
					}
				}
				else if(company_cd.equals("2"))
				{
					if(price_type.equals("Fixed"))
					{
						total_2 = dcq * cont_price;
					}
					else
					{
						if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
						{
							total_2 = dcq * fwd_price_fin;
						}
						else
						{
							total_2 = dcq * eff_deal_price;
						}
					}
				}
					
				double availableExchgRate=getExchangeRate(company_cd, countptyCd, agmt_no, cont_no, contType, sysdate);
				
				double avgTaxInInv = 0;
				String queryString16 = "SELECT MAX(FACTOR) "
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
				
				info += "~"+total+"~"+nf.format(availableExchgRate)+"";
				double USDtoINR = total * availableExchgRate;
				info += "~"+nf.format(avgTaxInInv)+"";
				double ApplyTax = USDtoINR * (avgTaxInInv/100);
				countSettlementExpo += USDtoINR + ApplyTax;	
				
				double USDtoINR_1 = 0;
				double USDtoINR_2 = 0;
				double ApplyTax_1=0;
				double ApplyTax_2 = 0;
				
				
				if(company_cd.equals("1"))
				{
					USDtoINR_1 = total_1 * availableExchgRate;
					ApplyTax_1 = USDtoINR_1 * (avgTaxInInv/100);
					usage_1 += USDtoINR_1 + ApplyTax_1;
				}
				else if(company_cd.equals("2"))
				{
					USDtoINR_2 = total_2 * availableExchgRate;
					ApplyTax_2 = USDtoINR_2 * (avgTaxInInv/100);
					usage_2 += USDtoINR_2 + ApplyTax_2;	
				}
				
				double tt = USDtoINR + ApplyTax; 
				info += "~"+nf.format(tt)+" ";
			}
			rset15.close();
			stmt15.close();
			if(countSettlementExpo < 0)
			{
				countSettlementExpo = (-1)*(countSettlementExpo);
			}
			if(usage_1 < 0)
			{
				usage_1 = (-1)*(usage_1);
			}
			if(usage_2 < 0)
			{
				usage_2 = (-1)*(usage_2);
			}
			VUSAGE_1.add(nf.format(usage_1));
			VUSAGE_2.add(nf.format(usage_2));
			VINFO.add(info);
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return countSettlementExpo;
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
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
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
			rset_temp = stmt_temp.executeQuery();
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
			
			if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
			{
				exchangRate=Double.parseDouble(fixed_exchng_val);
			}
			else
			{
				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, date);
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchangRate=rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
			}
			
			if(Double.doubleToRawLongBits(exchangRate)==Double.doubleToRawLongBits(0)) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
			{
				String rate_nm="Shell Treasury Rate";
				
				queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp1 = conn.prepareStatement(queryString_temp);
				//stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(1, rate_nm.toUpperCase());
				rset_temp1 = stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchng_rate_cd = rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
				
				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp2 = conn.prepareStatement(queryString_temp);
				//stmt_temp2.setString(1, comp_cd);
				stmt_temp2.setString(1, exchng_rate_cd);
				stmt_temp2.setString(2, "Y");
				rset_temp2 = stmt_temp2.executeQuery();
				if(rset_temp2.next())
				{
					exchangRate = rset_temp2.getDouble(1);
				}
				rset_temp2.close();
				stmt_temp2.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return exchangRate;
	}
	
	private void getUpcomingSecurityDtls() 
	{
		String function_nm="getUpcomingSecurityDtls()";
		try
		{
			queryString1 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , DEAL_TYPE, COMPANY_CD "
					+ "FROM FMS_SECURITY_MST "
					//+ "WHERE COMPANY_CD=? AND GX=? AND (STATUS = ? OR STATUS = ? ) AND SEC_TYPE!=? AND "
					+ "WHERE GX=? AND (STATUS = ? OR STATUS = ? ) AND SEC_TYPE!=? AND "
					+ "(TO_DATE(EXPIRE_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(CANCEL_DT,'DD/MM/YYYY')>=TO_DATE(?,'dd/mm/yyyy') OR CANCEL_DT IS NULL) "
					+ "ORDER BY ENT_DT DESC";
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, company_cd);
			stmt1.setString(1, clearance);
			stmt1.setString(2, "P");
			stmt1.setString(3, "A");
			stmt1.setString(4, "OA");
			stmt1.setString(5, from_dt);
			stmt1.setString(6, from_dt);
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
				String expire_dt = rset1.getString(9)==null?"":rset1.getString(9);
				String remark = rset1.getString(10)==null?"":rset1.getString(10);
				String seq_rev_no = rset1.getString(11)==null?"":rset1.getString(11);
				String due_dt = rset1.getString(12)==null?"":rset1.getString(12);
				String deal_type = rset1.getString(13)==null?"":rset1.getString(13);
				String company_cd = rset1.getString(14)==null?"":rset1.getString(14);
				
				String ref_no = company_cd+"-"+sec_ref_no;
				VCOUNTERPARTY_CD.add(counterPartyCd);
				VSEC_TYPE.add(sec_type);
				//VREF_NO.add(sec_ref_no);
				VREF_NO.add(ref_no);
				VSTATUS.add(status);
				VSTATUS_NM.add(""+getStatusName(status));
				VVALUE.add(value);
				VEXPIRE_DT.add(expire_dt);
				VREMARK.add(remark);
				if(clearance.equals("I"))//HP20230914
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				VSEC_CATEGRY.add(sec_categry);
				VCURRENCY.add(currency);
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
				stmt2.setString(1, company_cd);
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
							dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
							dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=utilBean.getContractTypeName(cont_type);
						}
					}
					else
					{
						if(!dealNo.equals(""))
						{
							deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							dealNo+=", "+utilBean.NewDealMappingId(company_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
							disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
						}
						else
						{
							deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
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
					else if(cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("N") || cont_type.equals("T") || cont_type.equals("V") )
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
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
				status_nm="<span class='alert alert-primary'>Pending</span>";
			}
			else if(flag.equals("O"))
			{
				status_nm="<span class='alert alert-success'>In Order</span>";
			}
			else if(flag.equals("C"))
			{
				status_nm="<span class='alert alert-danger'>Cancelled</span>";
			}
			else if(flag.equals("A"))
			{
				status_nm="<span class='alert alert-secondary'>Pending For Amendment</span>";
			}
			else if(flag.equals("R"))
			{
				status_nm="<span class='alert alert-warning'>Restated</span>";			
			}
			else if(flag.equals("D"))
			{
				status_nm="<span class='alert alert-info'>Dummy</span>";			
			}
			else if(flag.equals("E"))
			{
				status_nm="<span class='alert' style='background:#8c8c8c;'>Expired</span>";			
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return status_nm;
	}

	private void getSecurityStatusDtls() 
	{
		String function_nm="getSecurityStatusDtls()";
		try
		{
			String sec_category = "";
			//queryString = "SELECT DISTINCT(SEC_CATEGORY) FROM FMS_SECURITY_MST WHERE COMPANY_CD=? ";
			queryString = "SELECT DISTINCT(SEC_CATEGORY) FROM FMS_SECURITY_MST ";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				sec_category = rset.getString(1)==null?"":rset.getString(1);
				V_SEC_CATEGORY.add(sec_category);
				
				if(sec_category.equals("R"))
				{
					VSEC_CATEGORY.add("Incoming");
				}
				else if(sec_category.equals("I"))
				{
					VSEC_CATEGORY.add("Outgoing");
				}
			}
			rset.close();
			stmt.close();
			
			String exchng_rate_cd="";
			double exchange_rate=0.00;
			
			for(int i=0; i<V_SEC_CATEGORY.size(); i++)
			{
				int index=0;
				queryString1 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , SEQ_NO , "
						+ "ISS_BANK_REF , TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , REMARKS , SEQ_REV_NO, COMPANY_CD "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE SEC_CATEGORY=? AND GX=? "
//						+ "WHERE COMPANY_CD=? AND SEC_CATEGORY=? AND GX=? "
						+ "AND TO_DATE(ISSUE_DT)<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND (TO_DATE(EXPIRE_DT)>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND (TO_DATE(CANCEL_DT)>=TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) "
						+ "AND STATUS IN(?,?,?) "
						+ "ORDER BY EXPIRE_DT ASC";
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, ""+V_SEC_CATEGORY.elementAt(i));
				stmt1.setString(2, clearance);
				stmt1.setString(3, from_dt);
				stmt1.setString(4, from_dt);
				stmt1.setString(5, from_dt);
				stmt1.setString(6, "O");
				stmt1.setString(7, "A");
				stmt1.setString(8, "D");
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
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
					String company_cd = rset1.getString(13)==null?"":rset1.getString(13);
					String ref_no = company_cd+"-"+sec_ref_no;
					
					VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,company_cd));
					VCOUNTERPARTY_CD.add(counterPartyCd);
					VSEC_TYPE.add(sec_type);
					VREF_NO.add(ref_no);
					VSTATUS.add(status);
					VSTATUS_NM.add(""+getStatusName(status));
					
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
					stmt_temp2 = conn.prepareStatement(queryString_temp);
					//stmt_temp2.setString(1, comp_cd);
					stmt_temp2.setString(1, exchng_rate_cd);
					stmt_temp2.setString(2, "Y");
					rset_temp2 = stmt_temp2.executeQuery();
					if(rset_temp2.next())
					{
						exchange_rate = rset_temp2.getDouble(1);
					}
					rset_temp2.close();
					stmt_temp2.close();
					
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
					if(clearance.equals("I"))//HP20230914
					{
						VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
					}
					else
					{
						VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
					}
					VSEC_CATEGRY.add(sec_categry);
					VCURRENCY.add(currency);
					
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
								dealNo+=", "+utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entity_cd;
								dealNo+=utilBean.getCounterpartyABBR(conn,entity_cd)+"-"+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								disp_cont_type+=utilBean.getContractTypeName(cont_type);
							}
						}
						else
						{
							if(!dealNo.equals(""))
							{
								deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
								dealNo+=", "+utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
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
					/*String previous_date = "";
					if(!expire_dt.equals(""))
					{
						queryString3 = "SELECT TO_CHAR(TO_DATE('"+expire_dt+"','DD/MM/YYYY')-1,'DD/MM/YYYY') FROM DUAL";
						rset3=stmt3.executeQuery(queryString3);
						if(rset3.next())
						{
							previous_date = rset3.getString(1)==null?"":rset3.getString(1);
						}
					}*/
					
					int days_left=dateUtil.getDays(expire_dt, from_dt)-1;
					
					VPREVIOUS_DT.add(days_left);
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCollateralAuditReport()
	{
		String function_nm="getCollateralAuditReport()";
		try
		{
			queryString="SELECT NEW_VALUE,OLD_VALUE,LOG_TIME,TO_CHAR(LOG_DT,'DD/MM/YYYY'),LOG_UID,FORM_NAME,COMPANY_CD "
					+ "FROM FMS_ALL_LOG "
					+ "WHERE FORM_NAME IN (?,?,?,?,?) "
					+ "AND NEW_VALUE IS NOT NULL ";
			if(!from_dt.equals("") && !to_dt.equals("")) {
				queryString+="AND LOG_DT >= TO_DATE(?,'DD/MM/YYYY') AND LOG_DT <= TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString+= " ORDER BY LOG_DT DESC, LOG_TIME DESC";
			//HP20230913 ADDED 'Trader Contract Master'
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "Advance Booking");
			stmt.setString(2, "Collateral Mgmt");
			stmt.setString(3, "Gas Supply Contract");
			stmt.setString(4, "Trader Contract Master");
			stmt.setString(5, "Credit Limit Mgmt/Credit Rating");
			if(!from_dt.equals("") && !to_dt.equals("")) {
				stmt.setString(6, from_dt);
				stmt.setString(7, to_dt);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String new_values = rset.getString(1)==null?"":rset.getString(1);
				String old_values = rset.getString(2)==null?"":rset.getString(2);
				String time = rset.getString(3)==null?"":rset.getString(3);
				String update_dt=rset.getString(4)==null?"":rset.getString(4);
				String update_by=rset.getString(5)==null?"":rset.getString(5);
				String form_nm=rset.getString(6)==null?"":rset.getString(6);
				String company_cd=rset.getString(7)==null?"":rset.getString(7);
				String company_abbr=utilBean.getCompanyAbbr(conn,company_cd);
				
				if(!new_values.equals(""))
				{
					String audit_type="";
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
					String entity="", rd="";
					String reversal="", old_reversal="";
					String sap_approval="", old_sap_approval="";
					
					String counterparty_name="",old_counterparty_name="";
					String bank_cd="", old_bank_cd="";
					String gx ="", old_gx="";
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
					
					String new_limit_type ="", new_limit_action=""; 
					String new_limit_amt ="", new_limit_category=""; 
					String new_limit_eff_dt ="", new_limit_exp_dt=""; 
					String new_limit_review_dt ="", new_limit_remark=""; 
					String new_limit_status =""; 
					
					String old_limit_type ="", old_limit_action=""; 
					String old_limit_amt ="", old_limit_category=""; 
					String old_limit_eff_dt ="", old_limit_exp_dt=""; 
					String old_limit_review_dt ="", old_limit_remark=""; 
					String old_limit_status =""; 
					
					if(form_nm.equals("Credit Limit Mgmt/Credit Rating")) 
					{
						audit_type="limit_and_rating";
					}
										
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
								sec_ref_no=company_cd+"-"+temp[1];
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
						if(split_New_Value[i].startsWith("DEAL_DTL="))
						{
							String temp[] = split_New_Value[i].split("DEAL_DTL=");
							if(temp.length>0)
							{
								deal_dtl=temp[1];
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
								sec_ref_no=temp[1];
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
								sec_ref_no=temp[1];
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
						counterparty_name =utilBean.getCounterpartyName(conn,cp);
						old_counterparty_name =utilBean.getCounterpartyName(conn,old_cp);
					}
					else if (gx.equals("I")) 
					{
						counterparty_name =utilBean.getGasExchangeName(conn,cp);
						old_counterparty_name =utilBean.getGasExchangeName(conn,old_cp);
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
						String security_details="";
						
						if(old_values.equals("")) 
						{
							if(form_nm.equals("Credit Limit Mgmt/Credit Rating")) 
							{
									if(entity.equals("C")) 
									{
										security_details= "Counterparty Name : "+counterparty_name+"<font style=\"color:blue\">( )</font><br>";
									}
									else if(entity.equals("B")) 
									{
										security_details="Bank Name : "+utilBean.getBankName(conn,bank_cd)+"<font style=\"color:blue\">( )</font><br>";
									}
									if(rd.equals("C")) 
									{
										security_details+="Rating Source : "+rate_source+"<font style=\"color:blue\">( )</font><br>"
												+ "Credit Rating : "+credit_rating+"<font style=\"color:blue\">( )</font><br>"
												+ "Status : "+cr_status+"<font style=\"color:blue\">( )</font><br>"
												+ "Remark : "+cr_remark+"<font style=\"color:blue\">( )</font><br>";
									}
									else if(rd.equals("P")) 
									{
										if(entity.equals("C")) 
										{
											if(gx.equals("K")) 
											{
												security_details+= "Parent Leagel Entity : "+utilBean.getCounterpartyName(conn,parent_entity)+"<font style=\"color:blue\">( )</font><br>";
											}
											else if(gx.equals("I")) 
											{
												security_details+= "Parent Leagel Entity : "+utilBean.getGasExchangeName(conn,parent_entity)+"<font style=\"color:blue\">( )</font><br>";
											}
										}
										else if(entity.equals("B")) 
										{
											security_details+= "Parent Leagel Entity : "+utilBean.getBankName(conn,parent_entity)+"<font style=\"color:blue\">( )</font><br>";
										}
										security_details+= "Percent Ownership(%) : "+ownership+"<font style=\"color:blue\">( )</font><br>"
												+ "Parent Entry Date : "+pEntrydate+"<font style=\"color:blue\">( )</font><br>"
												+ "Parent Exit Date  : "+pExitdate+"<font style=\"color:blue\">( )</font><br>"
												+ "Status : "+pcr_status+"<font style=\"color:blue\">( )</font><br>"
												+ "Remark : "+pcr_remark+"<font style=\"color:blue\">( )</font><br>";
									}
									else if(rd.equals("L"))
									{
										security_details+= "Limit Type : "+new_limit_type+"<font style=\"color:blue\">( )</font><br>"
												+ "Limit Action : "+new_limit_action+"<font style=\"color:blue\">( )</font><br>"
												+ "Amount(INR) : "+new_limit_amt+"<font style=\"color:blue\">( )</font><br>"
												+ "Effective Date : "+new_limit_eff_dt+"<font style=\"color:blue\">( )</font><br>"
												+ "Expiration Date : "+new_limit_exp_dt+"<font style=\"color:blue\">( )</font><br>"
												+ "Next Review Date : "+new_limit_review_dt+"<font style=\"color:blue\">( )</font><br>"
												+ "Status : "+new_limit_status+"<font style=\"color:blue\">( )</font><br>"
												+ "Remark : "+new_limit_remark+"<font style=\"color:blue\">( )</font><br>";
									}
								VBGCOLOR.add("#99ffcc");
								VRATINGSIZE.add("limit_rating");
							}
							else 
							{
								security_details="Counterparty Name : "+name+"<font style=\"color:blue\">( )</font><br>"
										+ "Abbreviation : "+abbr+"<font style=\"color:blue\">( )</font><br>"
										+ "Security Type : "+sec_type+"<font style=\"color:blue\">( )</font><br>"
										+ "Category : "+sec_category+"<font style=\"color:blue\">( )</font><br>"
										+ "Contract# : "+deal_no+"<font style=\"color:blue\">( )</font><br>"
										+ "Value : "+sec_value+"<font style=\"color:blue\">( )</font><br>"
										+ "Currency : "+currency+"<font style=\"color:blue\">( )</font><br>"
										+ "Recieved Date : "+received_dt+"<font style=\"color:blue\">( )</font><br>"
										+ "Status : "+status+"<font style=\"color:blue\">( )</font><br>"
										+ "Remark : "+remark+"<font style=\"color:blue\">( )</font>";
								VBGCOLOR.add("#99ffcc");
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
										security_details+="Counterparty Name : "+counterparty_name+"<font style=\"color:blue\"> ( "+old_counterparty_name+" )</font>"+"</font><br>";
									}
								}
								else if(entity.equals("B")) 
								{
									if(!bank_cd.equals(old_bank_cd))
									{
										security_details+="Bank Name : "+utilBean.getBankName(conn,bank_cd)+"<font style=\"color:blue\"> ( "+utilBean.getBankName(conn,old_bank_cd)+" )</font>"+"</font><br>";
									}
								}
								if(rd.equals("C"))
								{
									if(!rate_source.equals(old_rate_source))
									{
										security_details+="Rating Source : "+rate_source+"<font style=\"color:blue\"> ( "+old_rate_source+" )</font>"+"</font><br>";
									}
									if(!credit_rating.equals(old_credit_rating))
									{
										security_details+="Credit Rating : "+credit_rating+"<font style=\"color:blue\"> ( "+old_credit_rating+" )</font>"+"</font><br>";
									}
									if(!cr_status.equals(old_cr_status))
									{
										security_details+="Status : "+cr_status+"<font style=\"color:blue\"> ( "+old_cr_status+" )</font>"+"</font><br>";
									}
									if(!cr_remark.equals(old_cr_remark))
									{
										security_details+="Remark : "+cr_remark+"<font style=\"color:blue\"> ( "+old_cr_remark+" )</font>"+"</font><br>";
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
												security_details+="Parent Leagel Entity : "+utilBean.getCounterpartyName(conn,parent_entity)+"<font style=\"color:blue\"> ( "+utilBean.getCounterpartyName(conn,old_parent_entity)+" )</font>"+"</font><br>";
											}
										}
										else if(gx.equals("I")) 
										{
											if(!parent_entity.equals(old_parent_entity))
											{
												security_details+="Parent Leagel Entity : "+utilBean.getGasExchangeName(conn,parent_entity)+"<font style=\"color:blue\"> ( "+utilBean.getGasExchangeName(conn,old_parent_entity)+" )</font>"+"</font><br>";
											}
										}
									}
									else if(entity.equals("B")) 
									{
										if(!parent_entity.equals(old_parent_entity))
										{
											security_details+="Parent Leagel Entity : "+utilBean.getBankName(conn,parent_entity)+"<font style=\"color:blue\"> ( "+utilBean.getBankName(conn,old_parent_entity)+" )</font>"+"</font><br>";
										}
									}
									if(!ownership.equals(old_ownership))
									{
										security_details+="Percent Ownership(%) : "+ownership+"<font style=\"color:blue\"> ( "+old_ownership+" )</font>"+"</font><br>";
									}
									if(!pEntrydate.equals(old_pEntrydate))
									{
										security_details+="Parent Entry Date : "+pEntrydate+"<font style=\"color:blue\"> ( "+old_pEntrydate+" )</font>"+"</font><br>";
									}
									if(!pExitdate.equals(old_pExitdate))
									{
										security_details+="Parent Exit Date : "+pExitdate+"<font style=\"color:blue\"> ( "+old_pExitdate+" )</font>"+"</font><br>";
									}
									if(!pcr_status.equals(old_pcr_status))
									{
										security_details+="Status : "+pcr_status+"<font style=\"color:blue\"> ( "+old_pcr_status+" )</font>"+"</font><br>";
									}
									if(!pcr_remark.equals(old_pcr_remark))
									{
										security_details+="Remark : "+pcr_remark+"<font style=\"color:blue\"> ( "+old_pcr_remark+" )</font>"+"</font><br>";
									}
								}
								else if(rd.equals("L"))
								{
									if(!new_limit_type.equals(old_limit_type))
									{
										security_details+="Limit Type : "+new_limit_type+"<font style=\"color:blue\"> ( "+old_limit_type+" )</font>"+"</font><br>";
									}
									if(!new_limit_action.equals(old_limit_action))
									{
										security_details+="Limit Action : "+new_limit_action+"<font style=\"color:blue\"> ( "+old_limit_action+" )</font>"+"</font><br>";
									}
									if(!new_limit_category.equals(old_limit_category))
									{
										security_details+="Categorization : "+new_limit_category+"<font style=\"color:blue\"> ( "+old_limit_category+" )</font>"+"</font><br>";
									}
									if(!new_limit_amt.equals(old_limit_amt))
									{
										security_details+="Amount(INR) : "+new_limit_amt+"<font style=\"color:blue\"> ( "+old_limit_amt+" )</font>"+"</font><br>";
									}
									if(!new_limit_eff_dt.equals(old_limit_eff_dt))
									{
										security_details+="Effective Date : "+new_limit_eff_dt+"<font style=\"color:blue\"> ( "+old_limit_eff_dt+" )</font>"+"</font><br>";
									}
									if(!new_limit_exp_dt.equals(old_limit_exp_dt))
									{
										security_details+="Expiration Date : "+new_limit_exp_dt+"<font style=\"color:blue\"> ( "+old_limit_exp_dt+" )</font>"+"</font><br>";
									}
									if(!new_limit_review_dt.equals(old_limit_review_dt))
									{
										security_details+="Next Review Date : "+new_limit_review_dt+"<font style=\"color:blue\"> ( "+old_limit_review_dt+" )</font>"+"</font><br>";
									}
									if(!new_limit_status.equals(old_limit_status))
									{
										security_details+="Status : "+new_limit_status+"<font style=\"color:blue\"> ( "+old_limit_status+" )</font>"+"</font><br>";
									}
									if(!new_limit_remark.equals(old_limit_remark))
									{
										security_details+="Remark : "+new_limit_remark+"<font style=\"color:blue\"> ( "+old_limit_remark+" )</font>"+"</font><br>";
									}
								}
								VRATINGSIZE.add("limit_rating");
							}
							else 
							{
								if(!name.equals(old_name))
								{
									security_details+="Name : "+name+"<font style=\"color:blue\"> ( "+old_name+" )</font>"+"</font><br>";
								}
								if(!abbr.equals(old_abbr))
								{
									security_details+="Abbreviation : "+abbr+"<font style=\"color:blue\"> ( "+old_abbr+" )</font>"+"</font><br>";
								}
								if(!sec_type.equals(old_sec_type))
								{
									security_details+="Security Type : "+sec_type+"<font style=\"color:blue\"> ( "+old_sec_type+" )</font>"+"</font><br>";
								}
								if(!sec_category.equals(old_sec_category))
								{
									security_details+="Category : "+sec_category+"<font style=\"color:blue\"> ( "+old_sec_category+" )</font>"+"</font><br>";
								}
								if(!deal_type.equals(old_deal_type))
								{
									security_details+="Deal Type : "+deal_type+"<font style=\"color:blue\"> ( "+old_deal_type+" )</font>"+"</font><br>";
								}
								if(!deal_no.equals(old_deal_no))
								{
									security_details+="Contract# : "+deal_no+"<font style=\"color:blue\"> ( "+old_deal_no+" )</font>"+"</font><br>";
								}
								if(!sec_value.equals(old_sec_value))
								{
									security_details+="Value : "+sec_value+"<font style=\"color:blue\"> ( "+old_sec_value+" )</font>"+"</font><br>";
								}
								if(!currency.equals(old_currency))
								{
									security_details+="Currency : "+currency+"<font style=\"color:blue\"> ( "+old_currency+" )</font>"+"</font><br>";
								}
								if(!guarantor_nm.equals(old_guarantor_nm))
								{
									security_details+="Guarantor Name : "+guarantor_nm+"<font style=\"color:blue\"> ( "+old_guarantor_nm+" )</font>"+"</font><br>";
								}
								if(!fluctuation.equals(old_fluctuation))
								{
									security_details+="Fluctuation : "+fluctuation+"<font style=\"color:blue\"> ( "+old_fluctuation+" )</font>"+"</font><br>";
								}
								if(!variation.equals(old_variation))
								{
									security_details+="Variation : "+variation+"<font style=\"color:blue\"> ( "+old_variation+" )</font>"+"</font><br>";
								}
								if(!iss_bank_nm.equals(old_iss_bank_nm))
								{
									security_details+="Issuing Bank Name : "+iss_bank_nm+"<font style=\"color:blue\"> ( "+old_iss_bank_nm+" )</font>"+"</font><br>";
								}
								if(!iss_bank_ref.equals(old_iss_bank_ref))
								{
									security_details+="Issuing Bank Reference : "+iss_bank_ref+"<font style=\"color:blue\"> ( "+old_iss_bank_ref+" )</font>"+"</font><br>";
								}
								if(!adv_bank_nm.equals(old_adv_bank_nm))
								{
									security_details+="Advising Bank Name : "+adv_bank_nm+"<font style=\"color:blue\"> ( "+old_adv_bank_nm+" )</font>"+"</font><br>";
								}
								if(!adv_bank_ref.equals(old_adv_bank_ref))
								{
									security_details+="Advising Bank Reference  : "+adv_bank_ref+"<font style=\"color:blue\"> ( "+old_adv_bank_ref+" )</font>"+"</font><br>";
								}
								if(!conf_bank_nm.equals(old_conf_bank_nm))
								{
									security_details+="Confirming Bank Name : "+conf_bank_nm+"<font style=\"color:blue\"> ( "+old_conf_bank_nm+" )</font>"+"</font><br>";
								}
								if(!conf_bank_ref.equals(old_conf_bank_ref))
								{
									security_details+="Confirming Bank Reference : "+conf_bank_ref+"<font style=\"color:blue\"> ( "+old_conf_bank_ref+" )</font>"+"</font><br>";
								}
								if(!received_dt.equals(old_received_dt))
								{
									security_details+="Received Date : "+received_dt+"<font style=\"color:blue\"> ( "+old_received_dt+" )</font>"+"</font><br>";
								}
								if(!review_dt.equals(old_review_dt))
								{
									security_details+="Review Date : "+review_dt+"<font style=\"color:blue\"> ( "+old_review_dt+" )</font>"+"</font><br>";
								}
								if(!iss_date.equals(old_iss_date))
								{
									security_details+="Issuance Date : "+iss_date+"<font style=\"color:blue\"> ( "+old_iss_date+" )</font>"+"</font><br>";
								}
								if(!expire_dt.equals(old_expire_dt))
								{
									security_details+="Expire Date : "+expire_dt+"<font style=\"color:blue\"> ( "+old_expire_dt+" )</font>"+"</font><br>";
								}
								if(!status.equals(old_status))
								{
									security_details+="Status : "+status+"<font style=\"color:blue\"> ( "+old_status+" )</font>"+"</font><br>";
								}
								if(!tenor.equals(old_tenor))
								{
									security_details+="Tenor : "+tenor+"<font style=\"color:blue\"> ( "+old_tenor+" )</font>"+"</font><br>";
								}
								if(!remark.equals(old_remark))
								{
									security_details+="Remark : "+remark+"<font style=\"color:blue\"> ( "+old_remark+" )</font>"+"</font><br>";
								}
								if(!sap_approval.equals(old_sap_approval))
								{
									security_details+="SAP Approval : "+sap_approval+"<br>";
								}
								if(!reversal.equals(old_reversal) && !reversal.equals(""))
								{
									security_details+="Reversal : "+reversal+"<br>";
								}
								VSECURITYSIZE.add("Security");
							}
							VBGCOLOR.add("");
						}
						if(!VCOUNTERPARTY_CD.contains(cp)&&!VCOUNTERPARTY_CD.contains(bank_cd))
						{
							if(entity.equals("C") || entity.equals(""))
							{
								if(gx.equals("K")||gx.equals("")) 
								{
									VCOUNTERPARTY_CD.add(cp);
									VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,cp));
								}
								else if(gx.equals("I")) 
								{
									VCOUNTERPARTY_CD.add(cp);
									VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,cp));
								}
							}
							else if(entity.equals("B")) 
							{
								VCOUNTERPARTY_CD.add(bank_cd);
								VCOUNTERPARTY_NAME.add(""+utilBean.getBankName(conn,bank_cd));
							}						
						}
						
						if(!security_details.isEmpty())
						{
							if(!counterparty_cd.equals("0"))
							{
								if(counterparty_cd.equals(cp) || counterparty_cd.equals(bank_cd))
								{
									VSECURITYDETAILS.add(security_details);
									if(entity.equals("C") ||entity.equals("")) 
									{
										if(gx.equals("K")) 
										{
											VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
											VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
										}
										else if(gx.equals("I")) 
										{
											VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,cp));
											VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,cp));
										}
									}
									else if(entity.equals("B")) 
									{
										VCOUNTERPARTY_NM.add(""+utilBean.getBankName(conn,bank_cd));
										VCOUNTERPARTY_ABBR.add(""+utilBean.getBankABBR(conn,bank_cd));
									}
									VLAST_UPDATE_DATE.add(update_dt+"&nbsp;&nbsp;"+time);
									VLAST_UPDATE_BY.add(update_by);
									VDEAL_NO.add(deal_no);
									VREF_NO.add(sec_ref_no);
									VSEC_TYPE.add(sec_type);
									VAUDIT_TYPE.add(audit_type);
									VCO_ABBR.add(company_abbr);
								}
							}
							else
							{
								VSECURITYDETAILS.add(security_details);
								if(entity.equals("C") ||entity.equals("")) 
								{
									if(gx.equals("K") || gx.equals("")) 
									{
										VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,cp));
										VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,cp));
									}
									else if(gx.equals("I")) 
									{
										VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,cp));
										VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,cp));
									}
								}
								else if(entity.equals("B")) 
								{
									VCOUNTERPARTY_NM.add(""+utilBean.getBankName(conn,bank_cd));
									VCOUNTERPARTY_ABBR.add(""+utilBean.getBankABBR(conn,bank_cd));
								}								
								VLAST_UPDATE_DATE.add(update_dt+"&nbsp;&nbsp;"+time);
								VLAST_UPDATE_BY.add(update_by);
								VDEAL_NO.add(deal_no);
								VREF_NO.add(sec_ref_no);
								VSEC_TYPE.add(sec_type);
								VAUDIT_TYPE.add(audit_type);
								VCO_ABBR.add(company_abbr);
							}
						}
					}
				}
			}
			rset.close();
			stmt.close();
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
	
	String from_dt="";
	String to_dt="";
	String rpt_dt = "";
	String counterparty_cd="";
    String clearance = "";
    String report_days = "";
	
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setRpt_dt(String rpt_dt) {this.rpt_dt = rpt_dt;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
    public void setClearance(String clearance) {this.clearance = clearance;}
    public void setReport_days(String report_days) {this.report_days = report_days;}
	
	Vector VSECURITYDETAILS = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_NAME = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VLAST_UPDATE_DATE = new Vector();
	Vector VLAST_UPDATE_BY = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VDEAL_REF_NO = new Vector();
	Vector VREF_NO = new Vector();
	Vector VSEC_TYPE = new Vector();
	Vector VAUDIT_TYPE = new Vector();
	Vector VBGCOLOR = new Vector();
	Vector VCONT_REF = new Vector();
	
	Vector VSEC_CATEGORY = new Vector();
	Vector V_SEC_CATEGORY = new Vector();
	Vector VSTATUS = new Vector();
	Vector VVALUE = new Vector();
	Vector VVALUE_USD = new Vector();
	Vector VISS_BANK_REF = new Vector();
	Vector VEXPIRE_DT = new Vector();
	Vector VREMARK = new Vector();
	Vector VDEAL_DTL = new Vector();
	Vector VINDEX = new Vector();
	Vector VSEC_CATEGRY = new Vector();
	Vector VPREVIOUS_DT = new Vector();
	Vector VCURRENCY = new Vector();
	
	Vector VDUE_DT = new Vector();
	Vector VDEAL_TYPE = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VDIS_DT = new Vector();
	
	Vector VCREDIT_RATING = new Vector();
	Vector VAVAILABLE = new Vector();
	Vector VTOTAL_LIMIT = new Vector();
	Vector VUNSECURED = new Vector();
	Vector VTEMPORARY = new Vector();
	Vector VADJUST_USAGE = new Vector();
	Vector VUSAGE = new Vector();
	Vector VNET_USAGE = new Vector();
	Vector VUSED = new Vector();
	
	Vector VSEC_REF_NO = new Vector();
	Vector VRECEIVED_DATE = new Vector();
	Vector VVALUE_FLUCTUATION = new Vector();
	Vector VISS_BANK_CD = new Vector();
	Vector VISS_BANK_NM = new Vector();
	Vector VADV_BANK_CD = new Vector();
	Vector VADV_BANK_NM = new Vector();
	Vector VADV_BANK_REF = new Vector();
	Vector VCONFIRM_BANK_CD = new Vector();
	Vector VCONFIRM_BANK_NM = new Vector();
	Vector VCONFIRM_BANK_REF = new Vector();
	Vector VISSUE_DT = new Vector();
	Vector VREVIEW_DT = new Vector();
	Vector VVALUE_VARIATION = new Vector();
	Vector VGUARANTOR_CD = new Vector();
	Vector VGUARANTOR_NM = new Vector();
	Vector VCANCEL_DT = new Vector();
	Vector VRENEW_DT = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VSEQ_REV_NO = new Vector();
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VSAP_REVERSAL_FLAG = new Vector();
	Vector VEXP_VAL = new Vector();
	Vector VTENOR = new Vector();
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VSECURITYSIZE = new Vector();
	Vector VRATINGSIZE = new Vector();
	
	Vector VINVOICE_NO = new Vector();
	Vector VAMT_DC = new Vector();
	Vector VNET_DUE_DT = new Vector();
	Vector VDUE_AMT = new Vector();
	Vector VRECV_AMT = new Vector();
	Vector VPAYMENT_STATUS = new Vector();
	Vector VPAY_RECV_DT = new Vector();
	Vector VCO_CODE = new Vector();
	Vector VCO_ABBR = new Vector();
	
	Vector VMUL_DEALS = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VCONT_NO = new Vector();
	
	Vector VEXPOSURE_DT = new Vector();
	Vector VOPENEXPOSURE = new Vector();
	Vector VPARENT_CD = new Vector();
	Vector VLIMIT_CALCULATION = new Vector();
	Vector VCOLOR = new Vector();
	Vector TEMP_IGX_CUMTOTAL=new Vector();
	Vector VWALKFWDCAPACITYDEALID = new Vector();
	Vector VWALKFWDCAPACITYDEALDCQ = new Vector();
	Vector VWALKFWDCAPACITYDEALSTDT = new Vector();
	Vector VWALKFWDCAPACITYDEALENDDT = new Vector();
	Vector VWALKFWDCAPACITYDEALRATE = new Vector();
	Vector VWALKFWDCAPACITYCURRFLAG = new Vector();
	Vector VWALKFWDSECCAPADEALID = new Vector();
	Vector VWALKFWDSECCAPADEALDCQ = new Vector();
	Vector VWALKFWDSECCAPADEALSTDT = new Vector();
	Vector VWALKFWDSECCAPADEALENDDT = new Vector();
	Vector VWALKFWDSECCAPADEALRATE = new Vector();
	Vector VWALKFWDSECCAPACURRFLAG = new Vector();
	Vector VUNSECURED_DEAL = new Vector();
	Vector VUNSECURED_CUST_CD = new Vector();
	Vector VSECURED_DEAL = new Vector();
	Vector VDEAL_ID = new Vector();
	Vector VEXCHGRATE = new Vector();
	Vector VTAXRATE = new Vector();
	Vector VBUYSELL = new Vector();
	Vector VTRANSVALUE = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VTOTAL = new Vector();
	Vector VCUMULATIVEEXPOSURE = new Vector();
	Vector VSETTLEMENTSELLCREDIT = new Vector();
	Vector VAVAILABLECREDIT = new Vector();
	Vector VCUSTWISESETTLEMENTSELLCREDITFORPREVDT = new Vector();
	Vector VCUSTWISESETTLEMENTSELLCREDIT = new Vector();
	Vector VINFO = new Vector();
	Vector VPARENT_NM = new Vector();
	Vector VPARENT_LIMIT = new Vector();
	Vector VSEC_DEAL_ID = new Vector();
	Vector VSEC_COUNTERPARTY_CD = new Vector();
	Vector VSEC_EXCHGRATE = new Vector();
	Vector VSEC_TAXRATE = new Vector();
	Vector VSEC_BUYSELL = new Vector();
	Vector VSEC_TRANSVALUE = new Vector();
	Vector VSEC_RATE = new Vector();
	Vector VSEC_RATE_UNIT = new Vector();
	Vector VSETTLEMENTSELLEXPOSUR = new Vector();
	Vector VSEC_COUNTERPARTY_NM = new Vector();
	Vector VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT = new Vector();
	Vector VCUSTWISESETTLEMENTSELLEXPOSURE = new Vector();
	Vector VINFO1 = new Vector();
	Vector VSEC_DISP_DEAL_ID = new Vector();
	Vector VDISP_DEAL_ID = new Vector();
	
	Vector VUSAGE_1 = new Vector();
	Vector VUSAGE_2 = new Vector();
	
	Vector VOC_DELV_DT = new Vector();
	Vector VOC_COMP_PROFILE = new Vector();
	Vector VOC_PRICE_TYPE = new Vector();
	Vector VOC_DCQ = new Vector();
	Vector VOC_CONT_PRICE = new Vector();
	Vector VOC_TOTAL = new Vector();
	Vector VOC_EXCH_RATE = new Vector();
	Vector VOC_APPLY_TAX = new Vector();
	Vector VOC_TRANS_CHARGE = new Vector();
	Vector VOC_GRAND_TOTAL = new Vector();
	Vector VOC_CUMULATIVE_TOTAL = new Vector();
	Vector VEXPO_DELV_DT = new Vector();
	Vector VEXPO_COMP_PROFILE = new Vector();
	Vector VEXPO_PRICE_TYPE = new Vector();
	Vector VEXPO_DCQ = new Vector();
	Vector VEXPO_CONT_PRICE = new Vector();
	Vector VEXPO_TOTAL = new Vector();
	Vector VEXPO_EXCH_RATE = new Vector();
	Vector VEXPO_APPLY_TAX = new Vector();
	Vector VEXPO_TRANS_CHARGE = new Vector();
	Vector VEXPO_GRAND_TOTAL = new Vector();
	Vector VEXPO_CUMULATIVE_TOTAL = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	
	Vector V_EXPOSURE_DATE = new Vector();
	Vector VSEC_EXPOSURE_DATE = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	Vector VSEC_DIS_CONTRACT_TYPE = new Vector();
	
	Vector CHILDPARENT_CD = new Vector();
	Vector VSTATUS_NM = new Vector();
	
	public Vector getVLEGAL_ENTITY() {return VLEGAL_ENTITY;}
	public Vector getVSECURITYDETAILS() {return VSECURITYDETAILS;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_NAME() {return VCOUNTERPARTY_NAME;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVLAST_UPDATE_DATE() {return VLAST_UPDATE_DATE;}
	public Vector getVLAST_UPDATE_BY() {return VLAST_UPDATE_BY;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVDEAL_REF_NO() {return VDEAL_REF_NO;}
	public Vector getVREF_NO() {return VREF_NO;}
	public Vector getVSEC_TYPE() {return VSEC_TYPE;}
	public Vector getVAUDIT_TYPE() {return VAUDIT_TYPE;}
	public Vector getVBGCOLOR() {return VBGCOLOR;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	
	public Vector getVSEC_CATEGORY() {return VSEC_CATEGORY;}
	public Vector getV_SEC_CATEGORY() {return V_SEC_CATEGORY;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVVALUE() {return VVALUE;}
	public Vector getVISS_BANK_REF() {return VISS_BANK_REF;}
	public Vector getVEXPIRE_DT() {return VEXPIRE_DT;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVDEAL_DTL() {return VDEAL_DTL;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVSEC_CATEGRY() {return VSEC_CATEGRY;}
	public Vector getVPREVIOUS_DT() {return VPREVIOUS_DT;}
	public Vector getVCURRENCY() {return VCURRENCY;}
	
	public Vector getVDUE_DT() {return VDUE_DT;}
	public Vector getVDEAL_TYPE() {return VDEAL_TYPE;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVDIS_DT() {return VDIS_DT;}
	
	public Vector getVCREDIT_RATING() {return VCREDIT_RATING;}
	public Vector getVAVAILABLE() {return VAVAILABLE;}
	public Vector getVTOTAL_LIMIT() {return VTOTAL_LIMIT;}
	public Vector getVUNSECURED() {return VUNSECURED;}
	public Vector getVTEMPORARY() {return VTEMPORARY;}
	public Vector getVADJUST_USAGE() {return VADJUST_USAGE;}
	public Vector getVUSAGE() {return VUSAGE;}
	public Vector getVNET_USAGE() {return VNET_USAGE;}
	public Vector getVUSED() {return VUSED;}
	
	public Vector getVSEC_REF_NO() {return VSEC_REF_NO;}
	public Vector getVRECEIVED_DATE() {return VRECEIVED_DATE;}
	public Vector getVVALUE_FLUCTUATION() {return VVALUE_FLUCTUATION;}
	public Vector getVISS_BANK_CD() {return VISS_BANK_CD;}
	public Vector getVISS_BANK_NM() {return VISS_BANK_NM;}
	public Vector getVADV_BANK_CD() {return VADV_BANK_CD;}
	public Vector getVADV_BANK_NM() {return VADV_BANK_NM;}
	public Vector getVADV_BANK_REF() {return VADV_BANK_REF;}
	public Vector getVCONFIRM_BANK_CD() {return VCONFIRM_BANK_CD;}
	public Vector getVCONFIRM_BANK_NM() {return VCONFIRM_BANK_NM;}
	public Vector getVCONFIRM_BANK_REF() {return VCONFIRM_BANK_REF;}
	public Vector getVISSUE_DT() {return VISSUE_DT;}
	public Vector getVREVIEW_DT() {return VREVIEW_DT;}
	public Vector getVVALUE_VARIATION() {return VVALUE_VARIATION;}
	public Vector getVGUARANTOR_CD() {return VGUARANTOR_CD;}
	public Vector getVGUARANTOR_NM() {return VGUARANTOR_NM;}
	public Vector getVCANCEL_DT() {return VCANCEL_DT;}
	public Vector getVRENEW_DT() {return VRENEW_DT;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVSEQ_REV_NO() {return VSEQ_REV_NO;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVSAP_REVERSAL_FLAG() {return VSAP_REVERSAL_FLAG;}
	public Vector getVEXP_VAL() {return VEXP_VAL;}
	public Vector getVTENOR() {return VTENOR;}
	
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVAMT_DC() {return VAMT_DC;}
	public Vector getVNET_DUE_DT() {return VNET_DUE_DT;}
	public Vector getVDUE_AMT() {return VDUE_AMT;}
	public Vector getVRECV_AMT() {return VRECV_AMT;}
	public Vector getVPAYMENT_STATUS() {return VPAYMENT_STATUS;}
	public Vector getVPAY_RECV_DT() {return VPAY_RECV_DT;}
	public Vector getVCO_CODE() {return VCO_CODE;}
	public Vector getVCO_ABBR() {return VCO_ABBR;}
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVSECURITYSIZE() {return VSECURITYSIZE;}
	public Vector getVRATINGSIZE() {return VRATINGSIZE;}
	
	public Vector getVEXPOSURE_DT() {return VEXPOSURE_DT;}
	public Vector getVOPENEXPOSURE() {return VOPENEXPOSURE;}
	public Vector getVPARENT_CD() {return VPARENT_CD;}
	public Vector getVLIMIT_CALCULATION() {return VLIMIT_CALCULATION;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getTEMP_IGX_CUMTOTAL() {return TEMP_IGX_CUMTOTAL;}
	public Vector getVWALKFWDCAPACITYDEALID() {return VWALKFWDCAPACITYDEALID;}
	public Vector getVWALKFWDCAPACITYDEALDCQ() {return VWALKFWDCAPACITYDEALDCQ;}
	public Vector getVWALKFWDCAPACITYDEALSTDT() {return VWALKFWDCAPACITYDEALSTDT;}
	public Vector getVWALKFWDCAPACITYDEALENDDT() {return VWALKFWDCAPACITYDEALENDDT;}
	public Vector getVWALKFWDCAPACITYDEALRATE() {return VWALKFWDCAPACITYDEALRATE;}
	public Vector getVWALKFWDCAPACITYCURRFLAG() {return VWALKFWDCAPACITYCURRFLAG;}
	public Vector getVWALKFWDSECCAPADEALID() {return VWALKFWDSECCAPADEALID;}
	public Vector getVWALKFWDSECCAPADEALDCQ() {return VWALKFWDSECCAPADEALDCQ;}
	public Vector getVWALKFWDSECCAPADEALSTDT() {return VWALKFWDSECCAPADEALSTDT;}
	public Vector getVWALKFWDSECCAPADEALENDDT() {return VWALKFWDSECCAPADEALENDDT;}
	public Vector getVWALKFWDSECCAPADEALRATE() {return VWALKFWDSECCAPADEALRATE;}
	public Vector getVWALKFWDSECCAPACURRFLAG() {return VWALKFWDSECCAPACURRFLAG;}
	public Vector getVUNSECURED_DEAL() {return VUNSECURED_DEAL;}
	public Vector getVUNSECURED_CUST_CD() {return VUNSECURED_CUST_CD;}
	public Vector getVSECURED_DEAL() {return VSECURED_DEAL;}
	public Vector getVDEAL_ID() {return VDEAL_ID;}
	public Vector getVEXCHGRATE() {return VEXCHGRATE;}
	public Vector getVTAXRATE() {return VTAXRATE;}
	public Vector getVBUYSELL() {return VBUYSELL;}
	public Vector getVTRANSVALUE() {return VTRANSVALUE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVTOTAL() {return VTOTAL;}
	public Vector getVCUMULATIVEEXPOSURE() {return VCUMULATIVEEXPOSURE;}
	public Vector getVSETTLEMENTSELLCREDIT() {return VSETTLEMENTSELLCREDIT;}
	public Vector getVAVAILABLECREDIT() {return VAVAILABLECREDIT;}
	public Vector getVCUSTWISESETTLEMENTSELLCREDITFORPREVDT() {return VCUSTWISESETTLEMENTSELLCREDITFORPREVDT;}
	public Vector getVCUSTWISESETTLEMENTSELLCREDIT() {return VCUSTWISESETTLEMENTSELLCREDIT;}
	public Vector getVINFO() {return VINFO;}
	public Vector getVPARENT_NM() {return VPARENT_NM;}
	public Vector getVPARENT_LIMIT() {return VPARENT_LIMIT;}
	public Vector getVSEC_DEAL_ID() {return VSEC_DEAL_ID;}
	public Vector getVSEC_COUNTERPARTY_CD() {return VSEC_COUNTERPARTY_CD;}
	public Vector getVSEC_EXCHGRATE() {return VSEC_EXCHGRATE;}
	public Vector getVSEC_TAXRATE() {return VSEC_TAXRATE;}
	public Vector getVSEC_BUYSELL() {return VSEC_BUYSELL;}
	public Vector getVSEC_TRANSVALUE() {return VSEC_TRANSVALUE;}
	public Vector getVSEC_RATE() {return VSEC_RATE;}
	public Vector getVSEC_RATE_UNIT() {return VSEC_RATE_UNIT;}
	public Vector getVSETTLEMENTSELLEXPOSUR() {return VSETTLEMENTSELLEXPOSUR;}
	public Vector getVSEC_COUNTERPARTY_NM() {return VSEC_COUNTERPARTY_NM;}
	public Vector getVCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT() {return VCUSTWISESETTLEMENTSELLEXPOSUREFORPREVDT;}
	public Vector getVCUSTWISESETTLEMENTSELLEXPOSURE() {return VCUSTWISESETTLEMENTSELLEXPOSURE;}
	public Vector getVINFO1() {return VINFO1;}
	public Vector getVVALUE_USD() {return VVALUE_USD;}
	public Vector getVSEC_DISP_DEAL_ID() {return VSEC_DISP_DEAL_ID;}
	public Vector getVDISP_DEAL_ID() {return VDISP_DEAL_ID;}
	
	public Vector getVUSAGE_1() {return VUSAGE_1;}
	public Vector getVUSAGE_2() {return VUSAGE_2;}
	
	public Vector getVOC_DELV_DT() {return VOC_DELV_DT;}
	public Vector getVOC_COMP_PROFILE() {return VOC_COMP_PROFILE;}
	public Vector getVOC_PRICE_TYPE() {return VOC_PRICE_TYPE;}
	public Vector getVOC_DCQ() {return VOC_DCQ;}
	public Vector getVOC_CONT_PRICE() {return VOC_CONT_PRICE;}
	public Vector getVOC_TOTAL() {return VOC_TOTAL;}
	public Vector getVOC_EXCH_RATE() {return VOC_EXCH_RATE;}
	public Vector getVOC_APPLY_TAX() {return VOC_APPLY_TAX;}
	public Vector getVOC_TRANS_CHARGE() {return VOC_TRANS_CHARGE;}
	public Vector getVOC_GRAND_TOTAL() {return VOC_GRAND_TOTAL;}
	public Vector getVOC_CUMULATIVE_TOTAL() {return VOC_CUMULATIVE_TOTAL;}
	public Vector getVEXPO_DELV_DT() {return VEXPO_DELV_DT;}
	public Vector getVEXPO_COMP_PROFILE() {return VEXPO_COMP_PROFILE;}
	public Vector getVEXPO_PRICE_TYPE() {return VEXPO_PRICE_TYPE;}
	public Vector getVEXPO_DCQ() {return VEXPO_DCQ;}
	public Vector getVEXPO_CONT_PRICE() {return VEXPO_CONT_PRICE;}
	public Vector getVEXPO_TOTAL() {return VEXPO_TOTAL;}
	public Vector getVEXPO_EXCH_RATE() {return VEXPO_EXCH_RATE;}
	public Vector getVEXPO_APPLY_TAX() {return VEXPO_APPLY_TAX;}
	public Vector getVEXPO_TRANS_CHARGE() {return VEXPO_TRANS_CHARGE;}
	public Vector getVEXPO_GRAND_TOTAL() {return VEXPO_GRAND_TOTAL;}
	public Vector getVEXPO_CUMULATIVE_TOTAL() {return VEXPO_CUMULATIVE_TOTAL;}
	
	public Vector getV_EXPOSURE_DATE() {return V_EXPOSURE_DATE;}
	public Vector getVSEC_EXPOSURE_DATE() {return VSEC_EXPOSURE_DATE;}
	public Vector getVDIS_CONTRACT_TYPE() {return VDIS_CONTRACT_TYPE;}
	public Vector getVSEC_DIS_CONTRACT_TYPE() {return VSEC_DIS_CONTRACT_TYPE;}
	public Vector getVSTATUS_NM() {return VSTATUS_NM;}
}
