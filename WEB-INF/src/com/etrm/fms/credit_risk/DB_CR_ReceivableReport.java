package com.etrm.fms.credit_risk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 18/09/2023 
//Status	  		: Developing

public class DB_CR_ReceivableReport 
{
	String db_src_file_name="DB_CR_ReceivableReport.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	PreparedStatement stmt_temp2;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset_temp;
	ResultSet rset_temp1;
	ResultSet rset_temp2;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString_temp="";
	String queryString_temp1="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###,###,###,##0.00");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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
//	    			stmt_temp= conn.createStatement();
//	    			stmt_temp1= conn.createStatement();
	    			
	    			if(callFlag.equalsIgnoreCase("RECEIVABLE_REPORT"))
	    			{
	    				getReceivableReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("KPI_REPORT"))
	    			{
	    				getKPIReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BANK_LIMIT_EXPOSURE"))
	    			{
	    				getBankLimitExposure();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GEM_REPORT"))
	    			{
	    				getGemReport();
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
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp2 != null){try{rset_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getGemReport() 
	{
		String function_nm="getGemReport()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD,TRADING_ENTITY,COUNTERPARTY_ABBR,COUNTERPARTY_NAME,LEGAL_PARENT,ULTIMATE_LEGAL_PARENT,S_P_RATING,"
					+ "MOODY_RATING,INTERNAL_RATING,FINAL_RATING,LIMIT_CATEGORY,LIMIT_VALUE,ACCOUNT_RECEIVABLE,ACCOUNT_PAYABLE,UNBILLED_RECEIVABLE,"
					+ "UNBILLED_PAYABLE,CURRENT_MONTH_UNDELIVERED,FORWARD_MTM,GROSS_EXPOSURE,CASH_COLLATERAL,LC_AMOUNT,OTHER_COLLATERAL,NET_EXPOSURE,"
					+ "CURRENCY,MARKET_TYPE,INDUSTRY,DEAL_TYPE "
					+ "FROM REPORT_GEM ";
					//+ "WHERE TRADING_ENTITY=? ";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, utilBean.getCompanyAbbr(comp_cd));
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VINDUSTRY.add(rset.getString(26)==null?"":rset.getString(26));
				VMARKET_TYPE.add(rset.getString(25)==null?"":rset.getString(25));
				VCURRENCY.add(rset.getString(24)==null?"":rset.getString(24));
				VNET_EXPOSURE.add(rset.getString(23)==null?"":rset.getString(23));
				VOTHER_COLLATERAL.add(rset.getString(22)==null?"":rset.getString(22));
				VLC_AMOUNT.add(rset.getString(21)==null?"":rset.getString(21));
				VCASH_COLLATERAL.add(rset.getString(20)==null?"":rset.getString(20));
				VGROSS_EXPOSURE.add(rset.getString(19)==null?"":rset.getString(19));
				VFORWARD_MTM.add(rset.getString(18)==null?"":rset.getString(18));
				VCURRENT_MONTH_UNDELIVERED.add(rset.getString(17)==null?"":rset.getString(17));
				VUNBILLED_PAYABLE.add(rset.getString(16)==null?"":rset.getString(16));
				VUNBILLED_RECEIVABLE.add(rset.getString(15)==null?"":rset.getString(15));
				VACCOUNT_PAYABLE.add(rset.getString(14)==null?"":rset.getString(14));
				VACCOUNT_RECEIVABLE.add(rset.getString(13)==null?"":rset.getString(13));
				VLIMIT_VALUE.add(rset.getString(12)==null?"":rset.getString(12));
				VLIMIT_CATEGORY.add(rset.getString(11)==null?"":rset.getString(11));
				VFINAL_RATING.add(rset.getString(10)==null?"":rset.getString(10));
				VINTERNAL_RATING.add(rset.getString(9)==null?"":rset.getString(9));
				VMOODY_RATING.add(rset.getString(8)==null?"":rset.getString(8));
				VS_P_RATING.add(rset.getString(7)==null?"":rset.getString(7));
				VULTIMATE_LEGAL_PARENT.add(rset.getString(6)==null?"":rset.getString(6));
				VLEGAL_PARENT.add(rset.getString(5)==null?"":rset.getString(5));
				VCOUNTERPARTY_NAME.add(rset.getString(4)==null?"":rset.getString(4));
				VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VTRADING_ENTITY.add(rset.getString(2)==null?"":rset.getString(2));
				VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VDEAL_TYPE.add(rset.getString(27)==null?"":rset.getString(27));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBankLimitExposure() 
	{
		String function_nm="getBankLimitExposure()";
		try
		{
			bank_nm = utilBean.getBankName(conn,bankCd);
			queryString = "SELECT COUNTERPARTY_CD, BANK_CD, GX "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "BANK_CD != ? ";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "0");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String bank_cd = rset.getString(2)==null?"":rset.getString(2);
				String bank_abbr = utilBean.getBankABBR(conn,bank_cd);
				String bank_name = utilBean.getBankName(conn,bank_cd);
				clearance = rset.getString(3)==null?"":rset.getString(3);
				
				
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
				
				//For Bank Limit & Availability INR to USD using "Shell Treasury Rate"
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
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
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
				stmt1.setString(3, report_dt);
				stmt1.setString(4, report_dt);
				stmt1.setString(5, report_dt);
				stmt1.setString(6, report_dt);
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
				stmt4.setString(5, report_dt);
				stmt4.setString(6, report_dt);
				stmt4.setString(7, report_dt);
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
					
					//VBANK_EXPOSURE_INR.add(nf.format(amt));
					//VBANK_EXPOSURE_USD.add(nf.format(amt/AvailableExchgRate));
					
					double USDtoINR = 0; 
					double INRtoUSD = 0;
					String contType="";

					if(currency.equals("2"))// For USD
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
				stmt2.setString(5, report_dt);
				stmt2.setString(6, report_dt);
				stmt2.setString(7, report_dt);
				stmt2.setString(8, "R");
				stmt2.setString(9, "O");
				stmt2.setString(10, "C");
				stmt2.setString(11, "R");
				stmt2.setString(12, "D");
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					counterPartyCd = rset2.getString(1)==null?"":rset2.getString(1);
					sec_category = rset2.getString(2)==null?"":rset2.getString(2);
					sec_type = rset2.getString(3)==null?"":rset2.getString(3);
					String sec_ref_no =  rset2.getString(4)==null?"":rset2.getString(4);
				    status = rset2.getString(5)==null?"":rset2.getString(5);
					currency = rset2.getString(6)==null?"":rset2.getString(6);
					value = rset2.getString(7)==null?"":rset2.getString(7);
					received_date = rset2.getString(8)==null?"":rset2.getString(8);
					String seq_no = rset2.getString(9)==null?"":rset2.getString(9);
					deal_type = rset2.getString(10)==null?"":rset2.getString(10);
					iss_bank_cd = rset2.getString(12)==null?"":rset2.getString(12);
					iss_bank_ref = rset2.getString(13)==null?"":rset2.getString(13);
					adv_bank_cd = rset2.getString(14)==null?"":rset2.getString(14);
					adv_bank_ref = rset2.getString(15)==null?"":rset2.getString(15);
					confirm_bank_cd = rset2.getString(16)==null?"":rset2.getString(16);
					confirm_bank_ref = rset2.getString(17)==null?"":rset2.getString(17);
					issue_dt = rset2.getString(18)==null?"":rset2.getString(18);
					expire_dt = rset2.getString(19)==null?"":rset2.getString(19);
					remarks = rset2.getString(22)==null?"":rset2.getString(22);
					cancel_date = rset2.getString(25)==null?"":rset2.getString(25);
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
					String guarantor_nm = ""+utilBean.getCounterpartyName(conn,guarantor_cd);
					
					VSEC_CATEGORY.add(sec_category);
					VSEC_TYPE.add(sec_type);
					VSEC_REF_NO.add(sec_ref_no);
					VSTATUS.add(status);
					VSTATUS_NM.add(""+getStatusName(status));
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
					String disp_cont_type ="";
					
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
						//exchange_rate = getExchangeRate(comp_cd,counterPartyCd,agmt,cont,cont_type,report_dt);
					}
					rset3.close();
					stmt3.close();

					queryString5 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENTITY_CD, 0 "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? ";
					queryString5 +=" UNION ";
					queryString5 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD, 0 "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO ) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
					queryString5 +=" UNION ";
					queryString5 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD, 0 "
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
					stmt_temp2 = conn.prepareStatement(queryString5);
					stmt_temp2.setString(1, company_cd);
					stmt_temp2.setString(2, counterPartyCd);
					stmt_temp2.setString(3, seq_no);
					stmt_temp2.setString(4, sec_ref_no);
					stmt_temp2.setString(5, seq_rev_no);
					stmt_temp2.setString(6, company_cd);
					stmt_temp2.setString(7, company_cd);
					stmt_temp2.setString(8, company_cd);
					rset_temp2 = stmt_temp2.executeQuery();
					while(rset_temp2.next())
					{
						String agmt_no = rset_temp2.getString(1)==null?"":rset_temp2.getString(1);
						String agmt_rev_no = rset_temp2.getString(2)==null?"":rset_temp2.getString(2);
						String cont_no = rset_temp2.getString(3)==null?"":rset_temp2.getString(3);
						String cont_rev_no = rset_temp2.getString(4)==null?"":rset_temp2.getString(4);
						cont_type = rset_temp2.getString(5)==null?"":rset_temp2.getString(5);
						String countpty_cd_no = rset_temp2.getString(6)==null?"":rset_temp2.getString(6);
						String cargo_no = rset_temp2.getString(7)==null?"":rset_temp2.getString(7);
						
						deal_No=utilBean.NewDealMappingId(company_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
						deal_No_dtl=sec_ref_no+"/"+cont_type+"/"+agmt_no+"/"+agmt_rev_no+"/"+cont_no+"/"+cont_rev_no+"/"+countpty_cd_no;
						//disp_cont_type=utilBean.getContractTypeName(cont_type);
						//exchange_rate = getExchangeRate(comp_cd,counterPartyCd,agmt_no,cont_no,cont_type,report_dt);
						//VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));
					}
					rset_temp2.close();
					stmt_temp2.close();
					
					//Exchange Rate DEFAULT 'Shell Treasury Rate' As per Discussed
					
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
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getKPIReport() 
	{
		String function_nm="getKPIReport()";
		try
		{
			//For Incoming/Outgoing 
			queryString = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
					+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,GX,COMPANY_CD "
					+ "FROM LOG_FMS_SECURITY_MST A "
//					+ "WHERE COMPANY_CD=? AND SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
					+ "WHERE SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
					+ "AND RECEIPT_DT >= TO_DATE(?,'DD/MM/YYYY') AND RECEIPT_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND LOG_SEQ_NO = (SELECT MAX(D.LOG_SEQ_NO) FROM LOG_FMS_SECURITY_MST D WHERE D.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.GX=D.GX AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.STATUS=D.STATUS) ";
			if(filter_security_type.equals("INC"))
			{
				queryString+= "AND A.SEC_CATEGORY=? ";
			}
			else if(filter_security_type.equals("OUT"))
			{
				queryString+= "AND A.SEC_CATEGORY=? ";
			}
			queryString+= "ORDER BY ENT_DT DESC";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "OA");
			stmt.setString(2, "ADV");
			stmt.setString(3, "O");
			stmt.setString(4, "C");
			stmt.setString(5, from_dt);
			stmt.setString(6, to_dt);
			if(filter_security_type.equals("INC"))
			{
				stmt.setString(7, "R");
			}
			else if(filter_security_type.equals("OUT"))
			{
				stmt.setString(7, "I");
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				counterPartyCd = rset.getString(1)==null?"":rset.getString(1);
				sec_category = rset.getString(2)==null?"":rset.getString(2);
				sec_type = rset.getString(3)==null?"":rset.getString(3);
				String sec_ref_no =  rset.getString(4)==null?"":rset.getString(4);
			    status = rset.getString(5)==null?"":rset.getString(5);
				currency = rset.getString(6)==null?"":rset.getString(6);
				value = rset.getString(7)==null?"":rset.getString(7);
				received_date = rset.getString(8)==null?"":rset.getString(8);
				String seq_no = rset.getString(9)==null?"":rset.getString(9);
				deal_type = rset.getString(10)==null?"":rset.getString(10);
				iss_bank_cd = rset.getString(12)==null?"":rset.getString(12);
				iss_bank_ref = rset.getString(13)==null?"":rset.getString(13);
				adv_bank_cd = rset.getString(14)==null?"":rset.getString(14);
				adv_bank_ref = rset.getString(15)==null?"":rset.getString(15);
				confirm_bank_cd = rset.getString(16)==null?"":rset.getString(16);
				confirm_bank_ref = rset.getString(17)==null?"":rset.getString(17);
				issue_dt = rset.getString(18)==null?"":rset.getString(18);
				expire_dt = rset.getString(19)==null?"":rset.getString(19);
				remarks = rset.getString(22)==null?"":rset.getString(22);
				cancel_date = rset.getString(25)==null?"":rset.getString(25);
				String seq_rev_no = rset.getString(27)==null?"":rset.getString(27);
				String gx = rset.getString(30)==null?"":rset.getString(30);
				String company_cd = rset.getString(31)==null?"":rset.getString(31);
				
				String counterparty_nm = "";
				if(gx.equals("I"))
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd)); //HP20230914
				}
				else
				{
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				String iss_bank_nm = ""+utilBean.getBankName(conn,iss_bank_cd);
				String adv_bank_nm = ""+utilBean.getBankName(conn,adv_bank_cd);
				String confirm_bank_nm = ""+utilBean.getBankName(conn,confirm_bank_cd);
				String guarantor_nm = ""+utilBean.getCounterpartyName(conn,guarantor_cd);
				String ref_no = company_cd+"-"+sec_ref_no;
				
				VCOUNTERPARTY_CD.add(counterPartyCd);
				//VCOUNTERPARTY_NM.add(counterparty_nm);
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
					//VVALUE_USD.add(Double.parseDouble(value)/exchange_rate);
				}
				else 
				{
					VVALUE_USD.add(value);
					VVALUE.add("");
					//VVALUE.add(Double.parseDouble(value)*exchange_rate);
				}
				
				String sel_deal_dtl="";
				String deal_dtl = "";
				String dealNo = "";
				String disp_cont_type = "";
				String deal_No = "";
				String deal_No_dtl="";
				//double exchange_rate = 0.00;
				
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
					
					double exchange_rate = getExchangeRate(company_cd, counterparty_cd, agmt, cont, cont_type,dateUtil.getSysdate());
					VEXCHANGE_RATE.add(exchange_rate);
					
					if(gx_sec.equals("I"))
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
					
					deal_No=utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type);
					deal_No_dtl=sec_ref_no+"/"+cont_type+"/"+agmt_no+"/"+agmt_rev_no+"/"+cont_no+"/"+cont_rev_no+"/"+countpty_cd_no;
					
					double exchange_rate = getExchangeRate(company_cd, counterPartyCd, agmt_no, cont_no, cont_type,dateUtil.getSysdate());
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
			
			//For Security Summary
			queryString5 = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE  , STATUS , CURRENCY , VALUE , "
					+ "CONFIRM_BANK_CD,COMPANY_CD "
					+ "FROM LOG_FMS_SECURITY_MST A "					
					+ "WHERE SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
//					+ "WHERE COMPANY_CD=? AND SEC_TYPE NOT IN(?,?) AND STATUS IN (?,?) "
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
			stmt4.setString(5, from_dt);
			stmt4.setString(6, to_dt);
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
				VDOUBLE_STATUS.add(status_sum);
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
					if(status.equalsIgnoreCase("O") && !confirm_bankCd.equalsIgnoreCase(""))
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
					if(status.equalsIgnoreCase("C"))
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
					if(status.equalsIgnoreCase("O"))
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
					if(status.equalsIgnoreCase("O") && !confirm_bankCd.equalsIgnoreCase(""))
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
					if(status.equalsIgnoreCase("C"))
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
					if(status.equalsIgnoreCase("O"))
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
			
			//================================================//
			inBGConfCount = String.valueOf(inBG_Conf_count);
			inBGConfAmt = nf2.format(inBG_Conf_amt);
			inBGConfAmtUsd = nf2.format(inBG_Conf_amt_USD);
			
			inBGCanCount = String.valueOf(inBG_Can_count);
			inBGCanAmt = nf2.format(inBG_Can_amt);
			inBGCanAmtUsd = nf2.format(inBG_Can_amt_USD);
			
			inBGAdvCount = String.valueOf(inBG_Adv_count);
			inBGAdvAmt = nf2.format(inBG_Adv_amt);
			inBGAdvAmtUsd = nf2.format(inBG_Adv_amt_USD);
			//==================================================//
			
			inPCGConfCount = String.valueOf(inPCG_Conf_count);
			inPCGConfAmt = nf2.format(inPCG_Conf_amt);
			inPCGConfAmtUsd = nf2.format(inPCG_Conf_amt_USD);
			
			inPCGCanCount = String.valueOf(inPCG_Can_count);
			inPCGCanAmt = nf2.format(inPCG_Can_amt);
			inPCGCanAmtUsd = nf2.format(inPCG_Can_amt_USD);
			
			inPCGAdvCount = String.valueOf(inPCG_Adv_count);
			inPCGAdvAmt = nf2.format(inPCG_Adv_amt);
			inPCGAdvAmtUsd = nf2.format(inPCG_Adv_amt_USD);
			
			//##################################################//
			
			outLCConfCount = String.valueOf(outLC_Conf_count);
			outLCConfAmt = nf2.format(outLC_Conf_amt);
			outLCConfAmtUsd = nf2.format(outLC_Conf_amt_USD);
			
			outLCCanCount = String.valueOf(outLC_Can_count);
			outLCCanAmt = nf2.format(outLC_Can_amt);
			outLCCanAmtUsd = nf2.format(outLC_Can_amt_USD);
			
			outLCAdvCount = String.valueOf(outLC_Adv_count);
			outLCAdvAmt = nf2.format(outLC_Adv_amt);
			outLCAdvAmtUsd = nf2.format(outLC_Adv_amt_USD);
			
			//================================================//
			outBGConfCount = String.valueOf(outBG_Conf_count);
			outBGConfAmt = nf2.format(outBG_Conf_amt);
			outBGConfAmtUsd = nf2.format(outBG_Conf_amt_USD);
			
			outBGCanCount = String.valueOf(outBG_Can_count);
			outBGCanAmt = nf2.format(outBG_Can_amt);
			outBGCanAmtUsd = nf2.format(outBG_Can_amt_USD);
			
			outBGAdvCount = String.valueOf(outBG_Adv_count);
			outBGAdvAmt = nf2.format(outBG_Adv_amt);
			outBGAdvAmtUsd = nf2.format(outBG_Adv_amt_USD);
			//==================================================//
			
			outPCGConfCount = String.valueOf(outPCG_Conf_count);
			outPCGConfAmt = nf2.format(outPCG_Conf_amt);
			outPCGConfAmtUsd = nf2.format(outPCG_Conf_amt_USD);
			
			outPCGCanCount = String.valueOf(outPCG_Can_count);
			outPCGCanAmt = nf2.format(outPCG_Can_amt);
			outPCGCanAmtUsd = nf2.format(outPCG_Can_amt_USD);
			
			outPCGAdvCount = String.valueOf(outPCG_Adv_count);
			outPCGAdvAmt = nf2.format(outPCG_Adv_amt);
			outPCGAdvAmtUsd = nf2.format(outPCG_Adv_amt_USD);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getReceivableReport() 
	{
		String function_nm="getReceivableReport()";
		try
		{
			double exchgRate = 0; 
			String exchg_rate_cd = "";
			String exchg_rate_nm = "";

			queryString = "SELECT COUNTERPARTY_NM, COUNTERPARTY_ABBR, COLLECTION_CATEGORY,COUNTERPARTY_CATEGORY,BUSINESS,"
					+ "LEGAL_ENTITY,DOC_NO,INVOICE_NO,REF_K1,REF_K2,REF_K3,DEAL_ASSIGNMENT,CONT_TYPE,TEXT,"
					+ "BA,TO_CHAR(NET_DUE_DT,'DD/MM/YYYY'),AMT_DC,AMT_INR,INV_TYPE,DESK_NAME,RES_COLLECTION_PRTY,RTL_GPL_TRADER,"
					+ "CLRNG_DOC,CLRNG_DT,WBS_PNL,TO_CHAR(BL_DT,'DD/MM/YYYY'),INVOICE_TYPE,CATEGORY,CURRANCY,AMT_USD,ARREARS_DAYS,"
					+ "AGING,STATUS,OVERDUE_COZ,DUE_AMT,CO_CODE "
					+ "FROM REPORT_RECEIVABLE "
					//+ "WHERE CO_CODE=? "
					+ "ORDER BY TO_DATE(BL_DT,'DD/MM/YYYY') DESC";//WHERE TO_DATE(BL_DT,'DD/MM/YYYY')<= TO_DATE('01/04/2020','DD/MM/YYYY')
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next()) 
			{
				VCOUNTERPARTY_NM.add(rset.getString(1)==null?"":rset.getString(1));
				VCOUNTERPARTY_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
				VCOLL_CATEGORY.add(rset.getString(3)==null?"":rset.getString(3));
				VCOUNTERPARTY_CATEGORY.add(rset.getString(4)==null?"":rset.getString(4));
				VBUSINESS.add(rset.getString(5)==null?"":rset.getString(5));
				VLEGAL_ENTITY.add(rset.getString(6)==null?"":rset.getString(6));
				VDOC_NO.add(rset.getString(7)==null?"":rset.getString(7));
				String invoice_no = ""+rset.getString(8)==null?"":rset.getString(8);
				VINVOICE_NO.add(rset.getString(8)==null?"":rset.getString(8));
				VREF_K1.add(rset.getString(9)==null?"":rset.getString(9));
				VREF_K2.add(rset.getString(10)==null?"":rset.getString(10));
				VREF_K3.add(rset.getString(11)==null?"":rset.getString(11));
				
				String deal_map_id =  rset.getString(12)==null?"":rset.getString(12);
				String[] split_map_id = deal_map_id.split("-");
				
				String comp_cd=split_map_id[0];
				String counterparty_cd=split_map_id[1];
				String agmt_no=split_map_id[2];
				String agmt_rev_no=split_map_id[3];
				String cont_no=split_map_id[4];
				String cont_rev_no=split_map_id[5];
				String contract_type=split_map_id[6];
				String cargo=split_map_id[7];
				
				String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo);
				VDEAL_ASSIGNMENT.add(mapped_cont_no);
				
				VCONT_TYPE.add(rset.getString(13)==null?"":rset.getString(13));
				VTEXT.add(rset.getString(14)==null?"":rset.getString(14));
				VBA.add(rset.getString(15)==null?"":rset.getString(15));
				VNET_DUE_DT.add(rset.getString(16)==null?"":rset.getString(16));
				VAMT_DC.add(rset.getString(17)==null?"":rset.getString(17));
				
				VAMT_INR.add(rset.getString(18)==null?"":nf2.format(rset.getDouble(18)));
				VINV_TYPE.add(rset.getString(19)==null?"":rset.getString(19));
				VDESK_NAME.add(rset.getString(20)==null?"":rset.getString(20));
				VRES_COLLECTION_PRTY.add(rset.getString(21)==null?"":rset.getString(21));
				VRTL_GPL_TRADER.add(rset.getString(22)==null?"":rset.getString(22));
				VCLRNG_DOC.add(rset.getString(23)==null?"":rset.getString(23));
				VCLRNG_DT.add(rset.getString(24)==null?"":rset.getString(24));
				VWBS_PNL.add(rset.getString(25)==null?"":rset.getString(25));
				VBL_DT.add(rset.getString(26)==null?"":rset.getString(26));
				VINVOICE_TYPE.add(rset.getString(27)==null?"":rset.getString(27));
				VCATEGORY.add(rset.getString(28)==null?"":rset.getString(28));
				VCURRANCY.add(rset.getString(29)==null?"":rset.getString(29));
				
				VAMT_USD.add(rset.getString(30)==null?"":nf2.format(rset.getDouble(30)));
				VARREARS_DAYS.add(rset.getString(31)==null?"":rset.getString(31));
				VAGING.add(rset.getString(32)==null?"":rset.getString(32));
				VSTATUS.add(rset.getString(33)==null?"":rset.getString(33));
				VOVERDUE_COZ.add(rset.getString(34)==null?"":rset.getString(34));
				
				String company_cd=rset.getString(36)==null?"":rset.getString(36);
				VCO_CODE.add(rset.getString(36)==null?"":rset.getString(36));
				
				VCO_ABBR.add(utilBean.getCompanyAbbr(conn,rset.getString(36)==null?"":rset.getString(36)));
				
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
				
					VDUE_AMT.add(nf2.format(rset.getDouble(35)));
					
					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset.getDouble(35)/exchgRate));	
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
				stmt2 = conn.prepareStatement(queryString1);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, invoice_no);
				rset2 = stmt2.executeQuery();
				
				while(rset2.next())
				{
					exchgRate = rset2.getDouble(1);
					
					VDUE_AMT.add(nf2.format(rset.getDouble(35)));
					
					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset2.close();
				stmt2.close();

				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_DLNG_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt3 = conn.prepareStatement(queryString1);
				stmt3.setString(1, company_cd);
				stmt3.setString(2, invoice_no);
				rset3 = stmt3.executeQuery();
				
				while(rset3.next())
				{
					exchgRate = rset3.getDouble(1);
					
					VDUE_AMT.add(nf2.format(rset.getDouble(35)));
					
					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset3.close();
				stmt3.close();
				
				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_DLNG_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt2 = conn.prepareStatement(queryString1);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, invoice_no);
				rset2 = stmt2.executeQuery();
				
				while(rset2.next())
				{
					exchgRate = rset2.getDouble(1);
					
					VDUE_AMT.add(nf2.format(rset.getDouble(35)));
					
					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset2.close();
				stmt2.close();
				
				queryString1 = "SELECT EXCHG_RATE_VALUE "
						+ "FROM FMS_DLNG_SVC_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_NO =? AND PDF_INV_DTL IS NOT NULL ";
				stmt2 = conn.prepareStatement(queryString1);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, invoice_no);
				rset2 = stmt2.executeQuery();
				
				while(rset2.next())
				{
					exchgRate = rset2.getDouble(1);
					
					VDUE_AMT.add(nf2.format(rset.getDouble(35)));
					
					if(exchgRate > 0)
					{
						VDUE_AMT_USD.add(nf2.format(rset.getDouble(35)/exchgRate));	
					}
					else
					{
						VDUE_AMT_USD.add("");
					}
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
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
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String from_dt ="";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt ="";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	String report_dt ="";
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	String bankCd ="";
	public void setBankCd(String bankCd) {this.bankCd = bankCd;}

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
	
	public String getInLCConfCount() {return inLCConfCount;}
	public String getInLCConfAmt() {return inLCConfAmt;}
	public String getInLCConfAmtUsd() {return inLCConfAmtUsd;}
	public String getInLCCanCount() {return inLCCanCount;}
	public String getInLCCanAmt() {return inLCCanAmt;}
	public String getInLCCanAmtUsd() {return inLCCanAmtUsd;}
	public String getInLCAdvCount() {return inLCAdvCount;}
	public String getInLCAdvAmt() {return inLCAdvAmt;}
	public String getInLCAdvAmtUsd() {return inLCAdvAmtUsd;}
	public String getInBGConfCount() {return inBGConfCount;}
	public String getInBGConfAmt() {return inBGConfAmt;}
	public String getInBGConfAmtUsd() {return inBGConfAmtUsd;}
	public String getInBGCanCount() {return inBGCanCount;}
	public String getInBGCanAmt() {return inBGCanAmt;}
	public String getInBGCanAmtUsd() {return inBGCanAmtUsd;}
	public String getInBGAdvCount() {return inBGAdvCount;}
	public String getInBGAdvAmt() {return inBGAdvAmt;}
	public String getInBGAdvAmtUsd() {return inBGAdvAmtUsd;}
	public String getInPCGConfCount() {return inPCGConfCount;}
	public String getInPCGConfAmt() {return inPCGConfAmt;}
	public String getInPCGConfAmtUsd() {return inPCGConfAmtUsd;}
	public String getInPCGCanCount() {return inPCGCanCount;}
	public String getInPCGCanAmt() {return inPCGCanAmt;}
	public String getInPCGCanAmtUsd() {return inPCGCanAmtUsd;}
	public String getInPCGAdvCount() {return inPCGAdvCount;}
	public String getInPCGAdvAmt() {return inPCGAdvAmt;}
	public String getInPCGAdvAmtUsd() {return inPCGAdvAmtUsd;}
	
	public String getOutLCConfCount() {return outLCConfCount;}
	public String getOutLCConfAmt() {return outLCConfAmt;}
	public String getOutLCConfAmtUsd() {return outLCConfAmtUsd;}
	public String getOutLCCanCount() {return outLCCanCount;}
	public String getOutLCCanAmt() {return outLCCanAmt;}
	public String getOutLCCanAmtUsd() {return outLCCanAmtUsd;}
	public String getOutLCAdvCount() {return outLCAdvCount;}
	public String getOutLCAdvAmt() {return outLCAdvAmt;}
	public String getOutLCAdvAmtUsd() {return outLCAdvAmtUsd;}
	public String getOutBGConfCount() {return outBGConfCount;}
	public String getOutBGConfAmt() {return outBGConfAmt;}
	public String getOutBGConfAmtUsd() {return outBGConfAmtUsd;}
	public String getOutBGCanCount() {return outBGCanCount;}
	public String getOutBGCanAmt() {return outBGCanAmt;}
	public String getOutBGCanAmtUsd() {return outBGCanAmtUsd;}
	public String getOutBGAdvCount() {return outBGAdvCount;}
	public String getOutBGAdvAmt() {return outBGAdvAmt;}
	public String getOutBGAdvAmtUsd() {return outBGAdvAmtUsd;}
	public String getOutPCGConfCount() {return outPCGConfCount;}
	public String getOutPCGConfAmt() {return outPCGConfAmt;}
	public String getOutPCGConfAmtUsd() {return outPCGConfAmtUsd;}
	public String getOutPCGCanCount() {return outPCGCanCount;}
	public String getOutPCGCanAmt() {return outPCGCanAmt;}
	public String getOutPCGCanAmtUsd() {return outPCGCanAmtUsd;}
	public String getOutPCGAdvCount() {return outPCGAdvCount;}
	public String getOutPCGAdvAmt() {return outPCGAdvAmt;}
	public String getOutPCGAdvAmtUsd() {return outPCGAdvAmtUsd;}
	
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String clearance = "";
	String adv_flag = "";
	String gx = "";
	String seq_no = "";
	String seq_rev_no = "";
	String sec_ref_no = "";
	String bank_nm = "";
	
	String counterPartyCd = "";
	String sec_category = "";
	String filter_security_type="";
	String sec_type = "";
	String status = "";
	String currency = "";
	String value = "";
	String received_date = "";
	String deal_type = "";
	String flucuation = "";
	String iss_bank_cd = "";
	String iss_bank_ref = "";
	String adv_bank_cd = "";
	String adv_bank_ref = "";
	String confirm_bank_cd = "";
	String confirm_bank_ref = "";
	String issue_dt = "";
	String expire_dt = "";
	String review_dt = "";
	String tenor = "";
	String remarks = "";
	String variation = "";
	String guarantor_cd = "";
	String cancel_date = "";
	String counterparty_nm = "";
	String iss_bank_nm = "";
	String adv_bank_nm = "";
	String confirm_bank_nm = "";
	String guarantor_nm = "";
	String cont_mapp="";
	String sap_approval_flag="";
	String xmlfile_name="";
	String file_path="";
	String isReversal="";
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFilter_security_type(String filter_security_type) {this.filter_security_type = filter_security_type;}
	
	public String getCounterPartyCd() {return counterPartyCd;}
	public String getSec_category() {return sec_category;}
	public String getSec_type() {return sec_type;}
	public String getStatus() {return status;}
	public String getCurrency() {return currency;}
	public String getValue() {return value;}
	public String getReceived_date() {return received_date;}
	public String getDeal_type() {return deal_type;}
	public String getFlucuation() {return flucuation;}
	public String getIss_bank_cd() {return iss_bank_cd;}
	public String getIss_bank_ref() {return iss_bank_ref;}
	public String getAdv_bank_cd() {return adv_bank_cd;}
	public String getAdv_bank_ref() {return adv_bank_ref;}
	public String getConfirm_bank_cd() {return confirm_bank_cd;}
	public String getConfirm_bank_ref() {return confirm_bank_ref;}
	public String getIssue_dt() {return issue_dt;}
	public String getExpire_dt() {return expire_dt;}
	public String getReview_dt() {return review_dt;}
	public String getTenor() {return tenor;}
	public String getRemarks() {return remarks;}
	public String getVariation() {return variation;}
	public String getGuarantor_cd() {return guarantor_cd;}
	public String getCancel_date() {return cancel_date;}
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getIss_bank_nm() {return iss_bank_nm;}
	public String getAdv_bank_nm() {return adv_bank_nm;}
	public String getConfirm_bank_nm() {return confirm_bank_nm;}
	public String getGuarantor_nm() {return guarantor_nm;}
	public String getBank_nm() {return bank_nm;}
	
	Vector VEXPOSURE_INR = new Vector();
	Vector VEXPOSURE_USD = new Vector();
	Vector VAVAILABILITY_INR = new Vector();
	Vector VAVAILABILITY_USD= new Vector();
	Vector VBANK_EXPOSURE_INR = new Vector();
	Vector VBANK_EXPOSURE_USD = new Vector();
	Vector VBANK_AVAILABILITY_INR = new Vector();
	Vector VBANK_AVAILABILITY_USD= new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOLL_CATEGORY = new Vector();
	Vector VCATEGORY = new Vector();
	Vector VBUSINESS = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	Vector VDOC_NO = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VREF_K1 = new Vector();
	Vector VREF_K2 = new Vector();
	Vector VREF_K3 = new Vector();
	Vector VDEAL_ASSIGNMENT = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VTEXT = new Vector();
	Vector VBA = new Vector();
	Vector VNET_DUE_DT = new Vector();
	Vector VAMT_DC = new Vector();
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
	Vector VCOUNTERPARTY_CATEGORY = new Vector();
	Vector VCURRANCY = new Vector();
	Vector VAMT_USD = new Vector();
	Vector VOVERDUE_COZ = new Vector();
	Vector VSTATUS = new Vector();
	Vector VAGING = new Vector();
	Vector VARREARS_DAYS = new Vector();
	Vector VDUE_AMT_USD = new Vector();
	Vector VDUE_AMT = new Vector();
	Vector VCO_CODE = new Vector();
	Vector VCO_ABBR = new Vector();

	Vector VDOUBLE_VALUE = new Vector();
	Vector VDOUBLE_VALUE_USD = new Vector();
	Vector VDOUBLE_COUNTERPARTY_CD = new Vector();
	Vector VDOUBLE_SEC_CATEGORY = new Vector();
	Vector VDOUBLE_STATUS = new Vector();
	Vector VDOUBLE_SEC_TYPE = new Vector();
	Vector VDOUBLE_CONFIRM_BANK_CD = new Vector();
	
	Vector VSEC_CATEGORY = new Vector();
	Vector VSEC_TYPE = new Vector();
	Vector VSEC_REF_NO = new Vector();
	Vector VVALUE = new Vector();
	Vector VVALUE_USD = new Vector();
	Vector VRECEIVED_DATE = new Vector();
	Vector VMST_BANK_NM = new Vector();
	Vector VMST_BANK_CD = new Vector();
	Vector VMST_BANK_ABBR = new Vector();
	Vector VMST_BRANCH_NAME = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VEXCHANGE_RATE = new Vector();
	Vector VISS_BANK_NM = new Vector();
	Vector VDEAL_TYPE = new Vector();
	Vector VISS_BANK_REF = new Vector();
	Vector VADV_BANK_NM = new Vector();
	Vector VADV_BANK_REF = new Vector();
	Vector VCONFIRM_BANK_NM = new Vector();
	Vector VCONFIRM_BANK_REF = new Vector();
	Vector VADV_BANK_CD = new Vector();
	Vector VISS_BANK_CD = new Vector();
	Vector VCONFIRM_BANK_CD = new Vector();
	Vector VCANCEL_DT = new Vector();
	Vector V_DEAL_NO = new Vector();
	Vector VEXP_VAL = new Vector();
	Vector VFROM_DT = new Vector();
	Vector VTO_DT = new Vector();
	Vector VISSUE_DT = new Vector();
	Vector VEXPIRE_DT = new Vector();
	Vector VREMARK = new Vector();
	
	Vector VBANK_LIMIT = new Vector();
	Vector VBANK_LIMIT_USD = new Vector();
	Vector VTOTAL_LIMIT = new Vector();
	Vector VUNSECURED = new Vector();
	Vector VTEMPORARY = new Vector();
	Vector VADJUST_USAGE = new Vector();
	Vector VUSAGE = new Vector();
	Vector VNET_USAGE = new Vector();
	Vector VUSED = new Vector();
	
	Vector VINDUSTRY = new Vector();
	Vector VMARKET_TYPE = new Vector();
	Vector VCURRENCY = new Vector();
	Vector VNET_EXPOSURE = new Vector();
	Vector VOTHER_COLLATERAL = new Vector();
	Vector VLC_AMOUNT = new Vector();
	Vector VCASH_COLLATERAL = new Vector();
	Vector VGROSS_EXPOSURE = new Vector();
	Vector VFORWARD_MTM = new Vector();
	Vector VCURRENT_MONTH_UNDELIVERED = new Vector();
	Vector VUNBILLED_PAYABLE = new Vector();
	Vector VUNBILLED_RECEIVABLE = new Vector();
	Vector VACCOUNT_PAYABLE = new Vector();
	Vector VACCOUNT_RECEIVABLE = new Vector();
	Vector VLIMIT_VALUE = new Vector();
	Vector VLIMIT_CATEGORY = new Vector();
	Vector VFINAL_RATING = new Vector();
	Vector VINTERNAL_RATING = new Vector();
	Vector VMOODY_RATING = new Vector();
	Vector VS_P_RATING = new Vector();
	Vector VULTIMATE_LEGAL_PARENT = new Vector();
	Vector VLEGAL_PARENT = new Vector();
	Vector VCOUNTERPARTY_NAME = new Vector();
	Vector VTRADING_ENTITY = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	
	Vector VSTATUS_NM = new Vector();
	
	public Vector getVEXPOSURE_INR() {return VEXPOSURE_INR;}
	public Vector getVEXPOSURE_USD() {return VEXPOSURE_USD;}
	public Vector getVAVAILABILITY_INR() {return VAVAILABILITY_INR;}
	public Vector getVAVAILABILITY_USD() {return VAVAILABILITY_USD;}
	
	public Vector getVBANK_EXPOSURE_INR() {return VBANK_EXPOSURE_INR;}
	public Vector getVBANK_EXPOSURE_USD() {return VBANK_EXPOSURE_USD;}
	public Vector getVBANK_AVAILABILITY_INR() {return VBANK_AVAILABILITY_INR;}
	public Vector getVBANK_AVAILABILITY_USD() {return VBANK_AVAILABILITY_USD;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOLL_CATEGORY() {return VCOLL_CATEGORY;}
	public Vector getVCATEGORY() {return VCATEGORY;}
	public Vector getVBUSINESS() {return VBUSINESS;}
	public Vector getVLEGAL_ENTITY() {return VLEGAL_ENTITY;}
	public Vector getVDOC_NO() {return VDOC_NO;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVVALUE_USD() {return VVALUE_USD;}
	public Vector getVEXCHANGE_RATE() {return VEXCHANGE_RATE;}
	public Vector getVREF_K1() {return VREF_K1;}
	public Vector getVREF_K2() {return VREF_K2;}
	public Vector getVREF_K3() {return VREF_K3;}
	public Vector getVDEAL_ASSIGNMENT() {return VDEAL_ASSIGNMENT;}
	public Vector getVCONT_TYPE() {return VCONT_TYPE;}
	public Vector getVTEXT() {return VTEXT;}
	public Vector getVBA() {return VBA;}
	public Vector getVNET_DUE_DT() {return VNET_DUE_DT;}
	public Vector getVAMT_DC() {return VAMT_DC;	}
	public Vector getVAMT_INR() {return VAMT_INR;}
	public Vector getVINV_TYPE() {return VINV_TYPE;}
	public Vector getVDESK_NAME() {return VDESK_NAME;}
	public Vector getVRES_COLLECTION_PRTY() {return VRES_COLLECTION_PRTY;}
	public Vector getVRTL_GPL_TRADER() {return VRTL_GPL_TRADER;}
	public Vector getVCLRNG_DOC() {return VCLRNG_DOC;}
	public Vector getVCLRNG_DT() {return VCLRNG_DT;}
	public Vector getVWBS_PNL() {return VWBS_PNL;}
	public Vector getVBL_DT() {return VBL_DT;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVCOUNTERPARTY_CATEGORY() {return VCOUNTERPARTY_CATEGORY;	}
	public Vector getVCURRANCY() {return VCURRANCY;}
	public Vector getVAMT_USD() {return VAMT_USD;}
	public Vector getVOVERDUE_COZ() {return VOVERDUE_COZ;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVAGING() {return VAGING;}
	public Vector getVARREARS_DAYS() {return VARREARS_DAYS;	}
	public Vector getVDUE_AMT_USD() {return VDUE_AMT_USD;}
	public Vector getVDUE_AMT() {return VDUE_AMT;}
	public Vector getVCO_CODE() {return VCO_CODE;}
	public Vector getVCO_ABBR() {return VCO_ABBR;}

	public Vector getVSEC_CATEGORY() {return VSEC_CATEGORY;}
	public Vector getVSEC_TYPE() {return VSEC_TYPE;}
	public Vector getVSEC_REF_NO() {return VSEC_REF_NO;}
	public Vector getVVALUE() {return VVALUE;}
	public Vector getVRECEIVED_DATE() {return VRECEIVED_DATE;}
	public Vector getVMST_BANK_CD() {return VMST_BANK_CD;}
	public Vector getVMST_BANK_NM() {return VMST_BANK_NM;}
	public Vector getVMST_BANK_ABBR() {return VMST_BANK_ABBR;}
	public Vector getVMST_BRANCH_NAME() {return VMST_BRANCH_NAME;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVISS_BANK_NM() {return VISS_BANK_NM;}
	public Vector getVDEAL_TYPE() {return VDEAL_TYPE;}
	public Vector getVISS_BANK_REF() {return VISS_BANK_REF;}
	public Vector getVADV_BANK_NM() {return VADV_BANK_NM;}
	public Vector getVADV_BANK_REF() {return VADV_BANK_REF;}
	public Vector getVCONFIRM_BANK_NM() {return VCONFIRM_BANK_NM;}
	public Vector getVCONFIRM_BANK_REF() {return VCONFIRM_BANK_REF;}
	public Vector getVISS_BANK_CD() {return VISS_BANK_CD;}
	public Vector getVADV_BANK_CD() {return VADV_BANK_CD;}
	public Vector getVCONFIRM_BANK_CD() {return VCONFIRM_BANK_CD;}
	public Vector getVCANCEL_DT() {return VCANCEL_DT;}
	public Vector getVEXP_VAL() {return VEXP_VAL;}
	public Vector getVFROM_DT() {return VFROM_DT;}
	public Vector getVTO_DT() {return VTO_DT;}
	public Vector getVISSUE_DT() {return VISSUE_DT;}
	public Vector getVEXPIRE_DT() {return VEXPIRE_DT;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVDOUBLE_VALUE() {return VDOUBLE_VALUE;}
	public Vector getVDOUBLE_VALUE_USD() {return VDOUBLE_VALUE_USD;	}
	public Vector getVDOUBLE_COUNTERPARTY_CD() {return VDOUBLE_COUNTERPARTY_CD;	}
	public Vector getVDOUBLE_SEC_CATEGORY() {return VDOUBLE_SEC_CATEGORY;	}
	public Vector getVDOUBLE_CONFIRM_BANK_CD() {return VDOUBLE_CONFIRM_BANK_CD;	}
	public Vector getVDOUBLE_SEC_TYPE() {return VDOUBLE_SEC_TYPE;	}
	public Vector getVDOUBLE_STATUS() {return VDOUBLE_STATUS;	}
	public Vector getVBANK_LIMIT() {return VBANK_LIMIT;}
	public Vector getVBANK_LIMIT_USD() {return VBANK_LIMIT_USD;}
	public Vector getVTOTAL_LIMIT() {return VTOTAL_LIMIT;}
	public Vector getVUNSECURED() {return VUNSECURED;}
	public Vector getVTEMPORARY() {return VTEMPORARY;}
	public Vector getVADJUST_USAGE() {return VADJUST_USAGE;}
	public Vector getVUSAGE() {return VUSAGE;}
	public Vector getVNET_USAGE() {return VNET_USAGE;}
	public Vector getVUSED() {return VUSED;}
	
	public Vector getVLIMIT_VALUE() {return VLIMIT_VALUE;}
	public Vector getVACCOUNT_RECEIVABLE() {return VACCOUNT_RECEIVABLE;}
	public Vector getVACCOUNT_PAYABLE() {return VACCOUNT_PAYABLE;}
	public Vector getVUNBILLED_RECEIVABLE() {return VUNBILLED_RECEIVABLE;}
	public Vector getVUNBILLED_PAYABLE() {return VUNBILLED_PAYABLE;}
	public Vector getVCURRENT_MONTH_UNDELIVERED() {return VCURRENT_MONTH_UNDELIVERED;}
	public Vector getVFORWARD_MTM() {return VFORWARD_MTM;}
	public Vector getVGROSS_EXPOSURE() {return VGROSS_EXPOSURE;}
	public Vector getVCASH_COLLATERAL() {return VCASH_COLLATERAL;}
	public Vector getVLC_AMOUNT() {return VLC_AMOUNT;}
	public Vector getVOTHER_COLLATERAL() {return VOTHER_COLLATERAL;}
	public Vector getVNET_EXPOSURE() {return VNET_EXPOSURE;}
	public Vector getVCURRENCY() {return VCURRENCY;}
	public Vector getVMARKET_TYPE() {return VMARKET_TYPE;}
	public Vector getVINDUSTRY() {return VINDUSTRY;}
	public Vector getVTRADING_ENTITY() {return VTRADING_ENTITY;}
	public Vector getVCOUNTERPARTY_NAME() {return VCOUNTERPARTY_NAME;}
	public Vector getVLEGAL_PARENT() {return VLEGAL_PARENT;}
	public Vector getVULTIMATE_LEGAL_PARENT() {return VULTIMATE_LEGAL_PARENT;}
	public Vector getVS_P_RATING() {return VS_P_RATING;}
	public Vector getVMOODY_RATING() {return VMOODY_RATING;}
	public Vector getVINTERNAL_RATING() {return VINTERNAL_RATING;}
	public Vector getVFINAL_RATING() {return VFINAL_RATING;}
	public Vector getVLIMIT_CATEGORY() {return VLIMIT_CATEGORY;}
	public Vector getVDIS_CONTRACT_TYPE() {return VDIS_CONTRACT_TYPE;}
	
	public Vector getVSTATUS_NM() {return VSTATUS_NM;}
}
