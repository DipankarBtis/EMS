package com.etrm.fms.credit_risk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 10/02/2023 
//Status	  		: Developing
public class DataBean_CreditRisk 
{
	String db_src_file_name="DataBean_CreditRisk.java";

	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt11;
	PreparedStatement stmt12;
	PreparedStatement stmt13;
	PreparedStatement stmt14;
	PreparedStatement stmt15;
	PreparedStatement stmt16;
	PreparedStatement stmt_temp;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset11;
	ResultSet rset12;
	ResultSet rset13;
	ResultSet rset14;
	ResultSet rset15;
	ResultSet rset16;
	ResultSet rset_temp;

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
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
			if(ds != null)
			{
				conn = ds.getConnection();
				if(conn != null)  
				{

					if(callFlag.equalsIgnoreCase("PRE-RECEIPT_SECURITY"))
					{
						getPreReceiptSecurityList();
					}
					else if(callFlag.equalsIgnoreCase("SECURITY_COLLATERAL_MANAGEMENT"))
					{
						getCounterpartyDetails();
						getBankDetails();
						getPendingSecurityDetails();
					}
					else if(callFlag.equalsIgnoreCase("EXCEED_CREDIT_DAYS_CONFIG")) //HP20230915
					{
						getCreditExceedDaysList();
					}
					else if(callFlag.equalsIgnoreCase("CONT_LIST_FOR_EXCEED_CREDIT")) //HP20230917
					{
						getCounterpartyListForExceedCredit();
						if(!counterparty_cd.equals("0") )
						{
							getContractListForExceedCredit();
							getContractList();
						}
					}
					else if(callFlag.equalsIgnoreCase("CREDIT_LIMIT_LIST")) //AP20230928  for Credit Limit/Rating
					{
						getCounterpartyDetails();
						getBankDetails();
						getCreditLimitDtls();
					}
					else if(callFlag.equalsIgnoreCase("PRE_DEAL_CREDITCHECK_REQ")) //AP20231206
					{
						getPreDealCreditCheckReq();
					}
					else if(callFlag.equalsIgnoreCase("CREDIT_RATING")) //AP20231207 for Pre deal credit check approval 
					{
						getCreditRatingDtls();
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
			if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset11 != null){try{rset11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset12 != null){try{rset12.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset13 != null){try{rset13.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset14 != null){try{rset14.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset15 != null){try{rset15.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset16 != null){try{rset16.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt11 != null){try{stmt11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt12 != null){try{stmt12.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt13 != null){try{stmt13.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt14 != null){try{stmt14.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt15 != null){try{stmt15.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt16 != null){try{stmt16.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	}

	private void getCreditRatingDtls()
	{
		String function_nm="getCreditRatingDtls()";
		try
		{
			counterparty_name = utilBean.getCounterpartyName(conn,counterparty_cd);
			String queryString="SELECT COUNTERPARTY_CD,BANK_CD,CREDIT_RATING,TO_CHAR(RATING_EFF_DT,'DD/MM/YYYY'),PARENT_OWNSHIP_CD,PARENT_OWNSHIP,REMARKS, "
					+ "STATUS,LIMIT_ID,TO_CHAR(PARENT_ENT_DT,'DD/MM/YYYY'),TO_CHAR(PARENT_EXIT_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY'),REF_NO "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
					+ "ORDER BY LIMIT_ID DESC";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, "0");
			stmt.setString(3, clearance);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				//String company_cd = rset.getString(1)==null?"":rset.getString(1);
				String counterPty_cd = rset.getString(1)==null?"":rset.getString(1);
				String bank_cd = rset.getString(2)==null?"":rset.getString(2);
				String credit_rating = rset.getString(3)==null?"":rset.getString(3);
				String rating_eff_dt = rset.getString(4)==null?"":rset.getString(4);
				String parent_ownership_cd = rset.getString(5)==null?"":rset.getString(5);
				String parent_ownership = rset.getString(6)==null?"":rset.getString(6);
				String remarks = rset.getString(7)==null?"":rset.getString(7);
				String status = rset.getString(8)==null?"":rset.getString(8);
				String limit_id = rset.getString(9)==null?"":rset.getString(9);
				String parent_ent_dt = rset.getString(10)==null?"":rset.getString(10);
				String parent_exit_dt = rset.getString(11)==null?"":rset.getString(11);
				String ent_dt = rset.getString(12)==null?"":rset.getString(12);
				String ref_no = rset.getString(13)==null?"":rset.getString(13);

				if(bank_cd.equals("0") || !counterPty_cd.equals("0"))
				{
					if(clearance.equals("I"))
					{
						VPARENT_OWENERSHIP_NAME.add(""+utilBean.getGasExchangeName(conn,parent_ownership_cd));
						VPARENT_OWENERSHIP_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,parent_ownership_cd));
					}
					else
					{
						VPARENT_OWENERSHIP_NAME.add(""+utilBean.getCounterpartyName(conn,parent_ownership_cd));
						VPARENT_OWENERSHIP_ABBR.add(""+utilBean.getCounterpartyABBR(conn,parent_ownership_cd));
					}
				}
				else if(counterPty_cd.equals("0") || !bank_cd.equals("0"))
				{
					VPARENT_OWENERSHIP_NAME.add(""+utilBean.getBankName(conn,parent_ownership_cd));
					VPARENT_OWENERSHIP_ABBR.add("");
				}

				VCOUNTERPARTY_CD.add(counterPty_cd);
				VCREDIT_RATING.add(credit_rating);
				VRATING_EFF_DT.add(rating_eff_dt);
				VPARENT_OWENERSHIP_CD.add(parent_ownership_cd);
				VPARENT_OWENERSHIP.add(parent_ownership);
				VREMARK.add(remarks);
				VSTATUS.add(status);
				VLIMIT_ID.add(limit_id);
				VPARENT_ENT_DT.add(parent_ent_dt);
				VPARENT_EXIT_DT.add(parent_exit_dt);
				VENT_DT.add(ent_dt);
				VREF_NO.add(ref_no);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	private void getPreDealCreditCheckReq()
	{
		String function_nm="getPreDealCreditCheckReq()";
		try
		{
			company_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
			String queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD ) "//AND A.COMPANY_CD=B.COMPANY_CD) "
					+ "AND STATUS=? AND KYC=? ORDER BY COUNTERPARTY_NM";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "Y");
			stmt.setString(2, "Y");
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

			String queryString1 = "SELECT COUNTERPARTY_CD,SEQ_NO,VALUE,CURRENCY,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),BUY_SELL,DLV_TERMS,SPOT_TERMS,PAYMENT_TERMS,REQUEST_ID,REQUEST_BY,"
					+ "TO_CHAR(REQUEST_DT,'DD/MM/YYYY HH12:MI:SS AM'),REQUEST_MSG,STATUS,TO_CHAR(APRV_DT,'DD/MM/YYYY HH12:MI:SS AM'),APRV_BY,APRV_MSG,SHELL_CONT "
					+ "FROM FMS_PRE_DEAL_DTL "
					//+ "WHERE COMPANY_CD=? "
					+ "ORDER BY REQUEST_ID DESC"; 
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String counterparty_cd = rset1.getString(1)==null?"":rset1.getString(1);
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NAME.add(utilBean.getCounterpartyName(conn,counterparty_cd));
				VSEQ_NO.add(rset1.getString(2)==null?"":rset1.getString(2));
				VVALUE.add(rset1.getString(3)==null?"":rset1.getString(3));

				String currency = rset1.getString(4)==null?"":rset1.getString(4);
				if(currency.equals("1"))
				{
					VCURRENCY.add("INR");
				}
				else if(currency.equals("2"))
				{
					VCURRENCY.add("USD");
				}

				VSTART_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
				VEND_DT.add(rset1.getString(6)==null?"":rset1.getString(6));
				VBUY_SELL.add(rset1.getString(7)==null?"":rset1.getString(7));
				VDLV_TERMS.add(rset1.getString(8)==null?"":rset1.getString(8));
				VSPOT_TERMS.add(rset1.getString(9)==null?"":rset1.getString(9));
				VPAYMENT_TERMS.add(rset1.getString(10)==null?"":rset1.getString(10));
				VREQ_ID.add(rset1.getString(11)==null?"":rset1.getString(11));
				String req_by = rset1.getString(12)==null?"":rset1.getString(12);
				VREQ_BY.add(utilBean.getEmpName(conn,req_by));
				VREQ_DT.add(rset1.getString(13)==null?"":rset1.getString(13));
				VREQ_REMARK.add(rset1.getString(14)==null?"":rset1.getString(14));
				String status = rset1.getString(15)==null?"":rset1.getString(15);
				//if(status.equals("P"))
				{
					VSTATUS.add(status);
				}
				VAPRV_DT.add(rset1.getString(16)==null?"":rset1.getString(16));
				String aprv_by = rset1.getString(17)==null?"":rset1.getString(17);
				VAPRV_BY.add(utilBean.getEmpName(conn,aprv_by));
				VAPRV_REMARK.add(rset1.getString(18)==null?"":rset1.getString(18));
				String cont_comp = rset1.getString(19)==null?"":rset1.getString(19);
				VCONT_COMP.add(utilBean.getCompanyAbbr(conn,cont_comp));
			}
			rset1.close();
			stmt1.close();
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
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, counterpty_Cd);
					stmt2.setString(2, clearance);
					stmt2.setString(3, sysdate);
					stmt2.setString(4, sysdate);
					stmt2.setString(5, sysdate);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						double secuVal=rset2.getDouble(1);
						double oriSecuVal=secuVal;
						String currency=rset2.getString(2)==null?"":rset2.getString(2);
						String secType=rset2.getString(3)==null?"":rset2.getString(3);
						String sec_Ref=rset2.getString(4)==null?"":rset2.getString(4);
						String company_cd=rset2.getString(6)==null?"":rset2.getString(6);
						//String contpty_cd=rset2.getString(7)==null?"":rset2.getString(7);
						String seq_no=rset2.getString(8)==null?"":rset2.getString(8);
						String status=rset2.getString(9)==null?"":rset2.getString(9);
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
						
						pcg_total_value=""+pcg_value;
						VPCG_COMP_ABBR.add(company_abbr);
						VPCG_SEC_TYPE.add(secType);
						VPCG_SEC_REF.add(secRef);
						VPCG_DEAL_NO.add(deal_no);
						VPCG_SEC_VALUE.add(nf.format(amount));
						if(status.equals("O"))
						{
							VPCG_STATUS.add("In Order");
						}
						else if(status.equals("C"))
						{
							VPCG_STATUS.add("Cancelled");
						}
						else if(status.equals("R"))
						{
							VPCG_STATUS.add("Restated");
						}
					}
					rset2.close();
					stmt2.close();
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

	private void getCreditLimitDtls() 
	{
		String function_nm="getCreditLimitDtls()";
		try
		{
			String sysDt = dateUtil.getSysdate();
			if(entity.equals("C"))
			{
				counterparty_cd = entity_cd;
				bank_cd = "0";

				if(clearance.equals("I"))
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,counterparty_cd));
				}
				else
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
				}
			}
			else if(entity.equals("B"))
			{
				counterparty_cd = "0";
				bank_cd = entity_cd;

				VCOUNTERPARTY_NAME.add(""+utilBean.getBankName(conn,bank_cd));
			}

			if(!counterparty_cd.equals("0"))
			{
				String queryString = "";
				if(clearance.equals("I"))
				{
					queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
							+ "FROM FMS_COMPANY_EXCHG_MST A "
							+ "WHERE " //COMPANY_CD=? AND " 
							+ "EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) AND COUNTERPARTY_CD NOT IN(?) ";
					queryString+= "ORDER BY COUNTERPARTY_NM";

				}
				else
				{
					queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
							+ "FROM FMS_COUNTERPARTY_MST A "
							+ "WHERE "//COMPANY_CD=? AND "
							+ "EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B "
							+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
							+ "AND STATUS=? AND KYC=? AND COUNTERPARTY_CD NOT IN(?) "
							+ "ORDER BY COUNTERPARTY_NM";
				}
				stmt = conn.prepareStatement(queryString);
				if(clearance.equals("I"))
				{
					//stmt.setString(1, comp_cd);
					stmt.setString(1, counterparty_cd);
				}
				else
				{
					//stmt.setString(1, comp_cd);
					stmt.setString(1, "Y");
					stmt.setString(2, "Y");
					stmt.setString(3, counterparty_cd);
				}
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
					VPO_COUNTERPARTY_CD.add(counterpty_cd);
					VPO_COUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VPO_COUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				}
				rset.close();
				stmt.close();
			}
			if(!bank_cd.equals("0"))
			{
				String queryString="SELECT DISTINCT BANK_CD , BANK_NAME , BANK_ABBR , BRANCH_NAME "
						+ "FROM FMS_BANK_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "BANK_CD NOT IN(?) "
						+ "ORDER BY BANK_CD";
				stmt = conn.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, bank_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					VPO_BANK_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VPO_BANK_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VPO_BANK_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
					VPO_BRANCH_NAME.add(rset.getString(4)==null?"":rset.getString(4));
				}
				rset.close();
				stmt.close();
			}

			String queryString="SELECT COUNTERPARTY_CD,BANK_CD,CREDIT_RATING,TO_CHAR(RATING_EFF_DT,'DD/MM/YYYY'),PARENT_OWNSHIP_CD,PARENT_OWNSHIP,REMARKS, "
					+ "STATUS,LIMIT_ID,TO_CHAR(PARENT_ENT_DT,'DD/MM/YYYY'),TO_CHAR(PARENT_EXIT_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY'),REF_NO "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
					+ "ORDER BY LIMIT_ID DESC";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, bank_cd);
			stmt.setString(3, clearance);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				//String company_cd = rset.getString(1)==null?"":rset.getString(1);
				String counterPty_cd = rset.getString(1)==null?"":rset.getString(1);
				String bank_cd = rset.getString(2)==null?"":rset.getString(2);
				String credit_rating = rset.getString(3)==null?"":rset.getString(3);
				String rating_eff_dt = rset.getString(4)==null?"":rset.getString(4);
				String parent_ownership_cd = rset.getString(5)==null?"":rset.getString(5);
				String parent_ownership = rset.getString(6)==null?"":rset.getString(6);
				String remarks = rset.getString(7)==null?"":rset.getString(7);
				String status = rset.getString(8)==null?"":rset.getString(8);
				String limit_id = rset.getString(9)==null?"":rset.getString(9);
				String parent_ent_dt = rset.getString(10)==null?"":rset.getString(10);
				String parent_exit_dt = rset.getString(11)==null?"":rset.getString(11);
				String ent_dt = rset.getString(12)==null?"":rset.getString(12);
				String ref_no = rset.getString(13)==null?"":rset.getString(13);

				if(rset.getRow()>1)
				{
					String queryString0 = "SELECT NVL(COUNT(*),0) FROM FMS_LIMIT_MST WHERE "//COMPANY_CD=? AND "
							+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=? AND PARENT_OWNSHIP_CD IS NOT NULL AND PARENT_EXIT_DT <= TO_DATE(?,'DD/MM/YYYY')";
					stmt0 = conn.prepareStatement(queryString0);
					//stmt0.setString(1, comp_cd);
					stmt0.setString(1, counterPty_cd);
					stmt0.setString(2, bank_cd);
					stmt0.setString(3, clearance);
					stmt0.setString(4, limit_id);
					stmt0.setString(5, sysDt);
					rset0 = stmt0.executeQuery();
					if(rset0.next())
					{
						if(rset0.getInt(1)==0)
						{
							VYESNO.add("N");
						}
						else
						{
							VYESNO.add("Y");
						}
					}
					rset0.close();
					stmt0.close();
				}
				else
				{
					VYESNO.add("N");
				}

				if(bank_cd.equals("0") || !counterPty_cd.equals("0"))
				{
					if(clearance.equals("I"))
					{
						VPARENT_OWENERSHIP_NAME.add(""+utilBean.getGasExchangeName(conn,parent_ownership_cd));
						VPARENT_OWENERSHIP_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,parent_ownership_cd));
					}
					else
					{
						VPARENT_OWENERSHIP_NAME.add(""+utilBean.getCounterpartyName(conn,parent_ownership_cd));
						VPARENT_OWENERSHIP_ABBR.add(""+utilBean.getCounterpartyABBR(conn,parent_ownership_cd));
					}
				}
				else if(counterPty_cd.equals("0") || !bank_cd.equals("0"))
				{
					VPARENT_OWENERSHIP_NAME.add(""+utilBean.getBankName(conn,parent_ownership_cd));
					VPARENT_OWENERSHIP_ABBR.add("");
				}

				VCREDIT_RATING.add(credit_rating);
				VRATING_EFF_DT.add(rating_eff_dt);
				VPARENT_OWENERSHIP_CD.add(parent_ownership_cd);
				VPARENT_OWENERSHIP.add(parent_ownership);
				VREMARK.add(remarks);
				VSTATUS.add(status);
				VLIMIT_ID.add(limit_id);
				VPARENT_ENT_DT.add(parent_ent_dt);
				VPARENT_EXIT_DT.add(parent_exit_dt);
				VENT_DT.add(ent_dt);
				VREF_NO.add(ref_no);
			}
			rset.close();
			stmt.close();

			String queryString1 = "SELECT COUNTERPARTY_CD,BANK_CD,LIMIT_ID,REF_NO "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
					+ "ORDER BY LIMIT_ID DESC";
			stmt1 = conn.prepareStatement(queryString1);
			//stmt1.setString(1, comp_cd);
			stmt1.setString(1, counterparty_cd);
			stmt1.setString(2, bank_cd);
			stmt1.setString(3, clearance);
			rset1 = stmt1.executeQuery();
			while(rset1.next())
			{
				String countptyCd =  rset1.getString(1)==null?"":rset1.getString(1);
				String bankCd =  rset1.getString(2)==null?"":rset1.getString(2);
				String limitId =  rset1.getString(3)==null?"":rset1.getString(3);
				String refNo =  rset1.getString(4)==null?"":rset1.getString(4);

				String queryString3="SELECT REF_NO,IS_ACTIVE,TO_CHAR(EXP_DT,'DD/MM/YYYY') "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=? "
						+ "AND IS_ACTIVE=? AND EXP_DT IS NOT NULL AND TO_DATE(EXP_DT,'DD/MM/YYYY') < SYSDATE ";
				stmt3 = conn.prepareStatement(queryString3);
				//stmt3.setString(1, comp_cd);
				stmt3.setString(1, countptyCd);
				stmt3.setString(2, bankCd);
				stmt3.setString(3, clearance);
				stmt3.setString(4, limitId);
				stmt3.setString(5, "Y");
				rset3 = stmt3.executeQuery();
				while(rset3.next())
				{
					String ref_no = rset3.getString(1)==null?"":rset3.getString(1);
					String is_active = rset3.getString(2)==null?"":rset3.getString(2);
					String expDt = rset3.getString(3)==null?"":rset3.getString(3);

					String sysdate= ""+dateUtil.getSysdate();
					String[] split_sys_date = sysdate.split("/");
					String splited_sysdate = split_sys_date[2]+"-"+split_sys_date[1]+"-"+split_sys_date[0];

					String[] split_exp_date = expDt.split("/");
					String splited_expdate = split_exp_date[2]+"-"+split_exp_date[1]+"-"+split_exp_date[0];

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
					Date sys_date = sdf.parse(splited_sysdate);  
					Date exp_Date = sdf.parse(splited_expdate);  

					if(sys_date.compareTo(exp_Date) > 0)   
					{  
						String queryString5="UPDATE FMS_LIMIT_DTL SET IS_ACTIVE=? "
								+ "WHERE "//COMPANY_CD=? AND "
								+ "IS_ACTIVE=? AND REF_NO=?";
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(1, "N");
						//stmt5.setString(2, comp_cd);
						stmt5.setString(2, is_active);
						stmt5.setString(3, ref_no);
						stmt5.executeUpdate();
						stmt5.close();
					}   
				}
				rset3.close();
				stmt3.close();

				String queryString2 = "SELECT REF_NO,LIMIT_TYPE,ACTION_TYPE,CATEGORY,AMT,AMT_UNIT,TO_CHAR(ENT_DT,'DD/MM/YYYY'),TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(EXP_DT,'DD/MM/YYYY') "
						+ ",TO_CHAR(INACTIVATION_DT,'DD/MM/YYYY'),TO_CHAR(REVIEW_DT,'DD/MM/YYYY'),REMARKS,IS_ACTIVE,SEQ_NO,LIMIT_ID "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? AND LIMIT_ID=? ";
				if(!limit_status.equals("0"))
				{
					queryString2+= "AND IS_ACTIVE=? ";
				}
				queryString2+= "ORDER BY LIMIT_ID DESC";
				stmt2 = conn.prepareStatement(queryString2);
				//stmt2.setString(1, comp_cd);
				stmt2.setString(1, countptyCd);
				stmt2.setString(2, bankCd);
				stmt2.setString(3, clearance);
				stmt2.setString(4, limitId);
				if(!limit_status.equals("0"))
				{
					stmt2.setString(5 , limit_status);
				}
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					String ref_no = rset2.getString(1)==null?"":rset2.getString(1);
					String limit_type = rset2.getString(2)==null?"":rset2.getString(2);
					String action_type = rset2.getString(3)==null?"":rset2.getString(3);
					String category = rset2.getString(4)==null?"":rset2.getString(4);
					String amt = rset2.getString(5)==null?"":rset2.getString(5);
					String amt_unit = rset2.getString(6)==null?"":rset2.getString(6);
					String ent_dt = rset2.getString(7)==null?"":rset2.getString(7);
					String eff_dt = rset2.getString(8)==null?"":rset2.getString(8);
					String exp_dt = rset2.getString(9)==null?"":rset2.getString(9);
					String inactivation_dt = rset2.getString(10)==null?"":rset2.getString(10);
					String review_dt = rset2.getString(11)==null?"":rset2.getString(11);
					String remark = rset2.getString(12)==null?"":rset2.getString(12);
					String status = rset2.getString(13)==null?"":rset2.getString(13);
					String seq_no = rset2.getString(14)==null?"":rset2.getString(14);
					String limit_id = rset2.getString(15)==null?"":rset2.getString(15);

					VL_REF_NO.add(ref_no);
					VL_LIMIT_TYPE.add(limit_type);
					VL_ACTION_TYPE.add(action_type);
					VL_CATEGORY.add(category);
					VL_AMT.add(amt);
					VL_ENT_DT.add(ent_dt);
					VL_EFF_DT.add(eff_dt);
					VL_EXP_DT.add(exp_dt);
					VL_INACT_DT.add(inactivation_dt);
					VL_REVIEW_DT.add(review_dt);
					VL_REMARK.add(remark);
					VL_STATUS.add(status);
					VL_SEQ_NO.add(seq_no);
					VL_LIMIT_ID.add(limit_id);

					String queryString0 = "SELECT COUNT(DUMMY) FROM DUAL WHERE TO_DATE(?,'DD/MM/YYYY') <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
					stmt0 = conn.prepareStatement(queryString0);
					stmt0.setString(1, eff_dt);
					rset0 = stmt0.executeQuery();
					if(rset0.next())
					{
						if(rset0.getInt(1) > 0)
						{
							VL_DT_FLAG.add("Y");
						}
						else
						{
							VL_DT_FLAG.add("N");
						}
					}
					rset0.close();
					stmt0.close();
				}
				rset2.close();
				stmt2.close();
			}
			rset1.close();
			stmt1.close();

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

			String queryString3 = "SELECT COUNTERPARTY_CD,BANK_CD,LIMIT_ID,REF_NO "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? AND GX=? "
					+ "ORDER BY LIMIT_ID DESC";
			stmt3 = conn.prepareStatement(queryString3);
			//stmt3.setString(1, comp_cd);
			stmt3.setString(1, counterparty_cd);
			stmt3.setString(2, bank_cd);
			stmt3.setString(3, clearance);
			rset3 = stmt3.executeQuery();
			while(rset3.next())
			{
				String counterpty_Cd =  rset3.getString(1)==null?"":rset3.getString(1);
				String bank_Cd =  rset3.getString(2)==null?"":rset3.getString(2);
				String limit_Id =  rset3.getString(3)==null?"":rset3.getString(3);
				String ref_No =  rset3.getString(4)==null?"":rset3.getString(4);

				String queryString4 = "SELECT AMT,AMT_UNIT,LIMIT_ID,LIMIT_TYPE,SEQ_NO,ACTION_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(EXP_DT,'DD/MM/YYYY'),TO_CHAR(REVIEW_DT,'DD/MM/YYYY'),CATEGORY,COUNTERPARTY_CD,BANK_CD,REMARKS "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "COUNTERPARTY_CD=? AND BANK_CD=? AND LIMIT_ID=? AND GX=? "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
						+ "((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT>=TO_DATE(?,'DD/MM/YYYY') AND INACTIVATION_DT IS NULL) OR ((EXP_DT>=TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) AND INACTIVATION_DT-1>=TO_DATE(?,'DD/MM/YYYY')))"
						+ "ORDER BY LIMIT_ID DESC";
				stmt4 = conn.prepareStatement(queryString4);
				//stmt4.setString(1, comp_cd);
				stmt4.setString(1, counterpty_Cd);
				stmt4.setString(2, bank_Cd);
				stmt4.setString(3, limit_Id);
				stmt4.setString(4, clearance);
				stmt4.setString(5, sysDt);
				stmt4.setString(6, sysDt);
				stmt4.setString(7, sysDt);
				stmt4.setString(8, sysDt);
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

				if(!counterpty_Cd.equals(""))
				{
					deal_usage = limit_usage_calculation(counterpty_Cd);
					pcg_usage = getPCGDtls(counterpty_Cd);
					
					usage=deal_usage;
				}
			}
			rset3.close();
			stmt3.close();

			available = total_limit + adjust_usage - usage-pcg_usage;
			net_usage = usage - adjust_usage+pcg_usage;
			if(total_limit > 0)
			{
				used = (usage / total_limit)*100;
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private double limit_usage_calculation(String countpty_Cd) 
	{
		String function_nm="limit_usage_calculation()";
		double countSettlementExpo=0;

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

			String queryString14 = "SELECT COUNT(REPORT_DT) FROM FMS_MR_EXPO_EOD_DTL WHERE "//COMPANY_CD=? AND "
					+ "REPORT_DT = TO_DATE(?,'DD/MM/YYYY')";
			stmt14 = conn.prepareStatement(queryString14);
			//stmt14.setString(1, comp_cd);
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

			String new_deal_id = "";
			String comp_abbr = "";
			String delv_dt = "";
			String price_type = "";
			double dcq = 0; 
			double total = 0;
			double price = 0;
			double availableExchgRate = 0;
			double avgTaxInInv = 0;
			double tt = 0;
			
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
				dcq = rset15.getDouble(1);
				String deal_id = rset15.getString(2)==null?"":rset15.getString(2);
				String contType = rset15.getString(9)==null?"":rset15.getString(9);
				String dealNosplit[] = deal_id.split("-");
				String agmt_no = dealNosplit[1];
				String cont_no = dealNosplit[2];
				String company_cd = rset15.getString(11)==null?"":rset15.getString(11);

				String countptyCd = rset15.getString(3)==null?"":rset15.getString(3);
				price_type = rset15.getString(4)==null?"":rset15.getString(4);
				double cont_price = rset15.getDouble(5); 
				double fwd_price_fin = rset15.getDouble(6);
				double eff_deal_price = rset15.getDouble(7);
				delv_dt = rset15.getString(8)==null?"":rset15.getString(8);
				String temp_account=rset15.getString(10)==null?"":rset15.getString(10);
				comp_abbr=utilBean.getCompanyAbbr(conn,company_cd);
				new_deal_id = utilBean.NewDealMappingId(company_cd, countptyCd, agmt_no, "", cont_no, "", contType, "");

				info += "DealId "+new_deal_id+"~"+comp_abbr+"~"+delv_dt+"~"+price_type+"~"+rset15.getDouble(1)+"~";
				
				if(price_type.equals("Fixed"))
				{
					info+=""+cont_price+"";
					total = dcq * cont_price;
					price=cont_price;
				}
				else
				{
					if(Double.doubleToRawLongBits(fwd_price_fin)!=Double.doubleToRawLongBits(0))
					{
						info+=""+fwd_price_fin+"";
						total = dcq * fwd_price_fin;
						price=fwd_price_fin;
					}
					else
					{
						info+=" "+eff_deal_price+"";
						total = dcq * eff_deal_price;
						price=eff_deal_price;
					}
				}

				availableExchgRate=getExchangeRate(company_cd, countptyCd, agmt_no, cont_no, contType, sysdate);

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

				tt = USDtoINR + ApplyTax; 
				info += "~"+nf.format(tt)+" ";
				
				VU_DEAL_NO.add(new_deal_id);
				VU_COMP_PROFILE.add(comp_abbr);
				VU_DELV_DT.add(delv_dt);
				VU_PRICE_TYPE.add(price_type);
				VU_DCQ.add(dcq);
				VU_PRICE.add(price);
				VU_TOTAL.add(total);
				VU_EXCH_RATE.add(nf.format(availableExchgRate));
				VU_TAX.add(nf.format(avgTaxInInv));
				VU_GRAND_TOTAL.add(nf.format(tt));
				
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

			String queryString_temp="SELECT EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,EXCHG_VAL "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND "
					+ "A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
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
				String queryString0="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
				stmt0 = conn.prepareStatement(queryString0);
				//stmt0.setString(1, comp_cd);
				stmt0.setString(1, exchng_rate_cd);
				stmt0.setString(2, date);
				rset0 = stmt0.executeQuery();
				if(rset0.next())
				{
					exchangRate=rset0.getDouble(1);
				}
				rset0.close();
				stmt0.close();
			}

			if(Double.doubleToRawLongBits(exchangRate)==Double.doubleToRawLongBits(0)) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
			{
				String rate_nm="Shell Treasury Rate";

				String queryString0="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "UPPER(EXC_RATE_NM) = ?"; 
				stmt0 = conn.prepareStatement(queryString0);
				//stmt0.setString(1, comp_cd);
				stmt0.setString(1, rate_nm.toUpperCase());
				rset0 = stmt0.executeQuery();
				if(rset0.next())
				{
					exchng_rate_cd = rset0.getString(1)==null?"":rset0.getString(1);
				}
				rset0.close();
				stmt0.close();

				String queryString="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE "//A.COMPANY_CD=B.COMPANY_CD AND "
						+ "A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt = conn.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, exchng_rate_cd);
				stmt.setString(2, "Y");
				rset = stmt.executeQuery();
				if(rset.next())
				{
					exchangRate = rset.getDouble(1);
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

		return exchangRate;
	}
	//Below class introduced By Arth Patel on 20230731  for collateral management.........
	public void getCounterpartyDetails() 
	{
		String function_nm="getCounterpartyDetails()";
		try
		{
			String queryString="";
			//HP20230914 ADDING IGX SUPPORT
			if(clearance.equals("I"))
			{
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";// AND A.COMPANY_CD=B.COMPANY_CD) ";
				queryString+= "ORDER BY COUNTERPARTY_NM";

			}
			else
			{
				/*HP20230914
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD='"+comp_cd+"' "
						+ "ORDER BY COUNTERPARTY_CD";
				 */
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "// AND A.COMPANY_CD=B.COMPANY_CD) "
						+ "AND KYC=? " //AND STATUS=?  Removed By AP20260306 - Erate Support
						+ "ORDER BY COUNTERPARTY_NM";
			}
			//HP20230914 PICKING UP WRONG COUNTERPARTY LIST FOR KYC BY ARTH, NOW CORRECTED
			stmt = conn.prepareStatement(queryString);
			if(clearance.equals("I"))
			{
				//stmt.setString(1, comp_cd);
			}
			else
			{
				//stmt.setString(1, comp_cd);
				stmt.setString(1, "Y");
				//stmt.setString(2, "Y");
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


	//Below class introduced By Arth Patel on 20230801  for Bank Details collateral management.........
	public void getBankDetails() 
	{
		String function_nm="getBankDetails()";
		try
		{
			String queryString="SELECT DISTINCT BANK_CD , BANK_NAME , BANK_ABBR , BRANCH_NAME "
					+ "FROM FMS_BANK_MST "
					//+ "WHERE COMPANY_CD=? "
					+ "ORDER BY BANK_CD";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VMST_BANK_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VMST_BANK_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VMST_BANK_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VMST_BRANCH_NAME.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Below class introduced By Arth Patel on 20230731  for collateral management.........
	public void getPendingSecurityDetails()
	{
		String function_nm="getPendingSecurityDetails()";
		try
		{	
			String systemDate=dateUtil.getSysdate();
			counterparty_status=(String) utilBean.getCounterpartyDetails(conn, counterparty_cd, systemDate).get("status");
			
			String queryString4 = "SELECT PARENT_OWNSHIP_CD,TO_CHAR(PARENT_EXIT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LIMIT_MST "
					+ "WHERE "//COMPANY_CD=? AND "
					+ "COUNTERPARTY_CD=? AND BANK_CD=? "
					+ "AND (PARENT_EXIT_DT IS NULL OR PARENT_EXIT_DT > SYSDATE) AND GX=?";
			stmt4 = conn.prepareStatement(queryString4);
			//stmt4.setString(1, comp_cd);
			stmt4.setString(1, counterparty_cd);
			stmt4.setString(2, "0");
			stmt4.setString(3, clearance);
			rset4 = stmt4.executeQuery();
			while(rset4.next())
			{
				String po_guarantor_cd = rset4.getString(1)==null?"":rset4.getString(1);
				
				
				if(!po_guarantor_cd.equals(""))
				{
					po_parent_exit_dt = rset4.getString(2)==null?"":rset4.getString(2);
					VPO_GUARANTOR_CD.add(po_guarantor_cd);
					VPO_PARENT_EXIT_DT.add(po_parent_exit_dt);
					if(clearance.equals("I"))
					{
						VPO_GUARANTOR_NAME.add(""+utilBean.getGasExchangeName(conn,po_guarantor_cd));
					}
					else
					{
						VPO_GUARANTOR_NAME.add(""+utilBean.getCounterpartyName(conn,po_guarantor_cd));
					}
				}
			}
			rset4.close();
			stmt4.close();

			if(!counterparty_cd.equals("0"))
			{
				int count =0;
				String queryString = "SELECT COUNTERPARTY_CD , SEC_CATEGORY , SEC_TYPE , SEC_REF_NO , STATUS , CURRENCY , VALUE , TO_CHAR(RECEIPT_DT,'DD/MM/YYYY') , SEQ_NO , "
						+ "DEAL_TYPE , VALUE_FLUC , ISS_BANK_CD , ISS_BANK_REF , ADV_BANK_CD , ADV_BANK_REF , CONFIRM_BANK_CD , CONFIRM_BANK_REF , TO_CHAR(ISSUE_DT,'DD/MM/YYYY') , "
						+ "TO_CHAR(EXPIRE_DT,'DD/MM/YYYY') , TO_CHAR(REVIEW_DT,'DD/MM/YYYY') , TENOR , REMARKS , VARIATION_VALUE , GUARANTOR_CD , TO_CHAR(CANCEL_DT,'DD/MM/YYYY') , "
						+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY'),SEQ_REV_NO,SAP_APPROVAL,SAP_REVERSAL,INORDER_HIST,PG_REF "
						+ "FROM FMS_SECURITY_MST "
						+ "WHERE COMPANY_CD=? AND GX=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString+= "AND COUNTERPARTY_CD=? ";
				}
				if(adv_flag.equals("ADV"))
				{
					queryString+= "AND SEC_TYPE=? ";
				}
				queryString+= "ORDER BY ENT_DT DESC";
				//HP20230920 ADDED SAP_REVERSAL COLUMN	
				//HP20230919 ADDED SAP_APPROVAL COLUMN	
				stmt = conn.prepareStatement(queryString);
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, clearance);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++count, counterparty_cd);
				}
				if(adv_flag.equals("ADV"))
				{
					stmt.setString(++count, adv_flag);
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
					flucuation = rset.getString(11)==null?"":rset.getString(11);
					iss_bank_cd = rset.getString(12)==null?"":rset.getString(12);
					iss_bank_ref = rset.getString(13)==null?"":rset.getString(13);
					adv_bank_cd = rset.getString(14)==null?"":rset.getString(14);
					adv_bank_ref = rset.getString(15)==null?"":rset.getString(15);
					confirm_bank_cd = rset.getString(16)==null?"":rset.getString(16);
					confirm_bank_ref = rset.getString(17)==null?"":rset.getString(17);
					issue_dt = rset.getString(18)==null?"":rset.getString(18);
					expire_dt = rset.getString(19)==null?"":rset.getString(19);
					review_dt = rset.getString(20)==null?"":rset.getString(20);
					tenor = rset.getString(21)==null?"":rset.getString(21);
					remarks = rset.getString(22)==null?"":rset.getString(22);
					variation = rset.getString(23)==null?"":rset.getString(23);
					guarantor_cd = rset.getString(24)==null?"":rset.getString(24);
					cancel_date = rset.getString(25)==null?"":rset.getString(25);
					String renew_date = rset.getString(26)==null?"":rset.getString(26);
					String seq_rev_no = rset.getString(27)==null?"":rset.getString(27);
					String sap_approval = rset.getString(28)==null?"":rset.getString(28);//HP20230919
					String sap_reversal = rset.getString(29)==null?"":rset.getString(29);//HP20230920

					String inorder_hist = rset.getString(30)==null?"":rset.getString(30);//AP20231104
					String adv_pg_ref = rset.getString(31)==null?"":rset.getString(31);//AP20231206
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

					VCOUNTERPARTY_CD.add(counterPartyCd);
					VCOUNTERPARTY_NAME.add(counterparty_nm);
					VSEC_CATEGORY.add(sec_category);
					VSEC_TYPE.add(sec_type);
					VSEC_REF_NO.add(sec_ref_no);
					VSTATUS.add(status);
					VSTATUS_NM.add(getStatusName(status));
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

					VINORDER_HIST.add(inorder_hist);//AP20231104
					VADV_PG_REF.add(adv_pg_ref);//AP20231206

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
					String cont_type ="";
					String sel_deal_dtl="";
					String deal_dtl = "";
					String dealNo = "";
					String deal_No = "";
					String deal_No_dtl="";
					String share_percent = "";
					String disp_cont_type="";
					String queryString1 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,ENTITY_CD,SHARE_PERCENT "
							+ "FROM FMS_SECURITY_DEAL_MAP "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEC_REF_NO=? AND SEQ_REV_NO=? AND GX=?";
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterPartyCd);
					stmt1.setString(3, seq_no);
					stmt1.setString(4, sec_ref_no);
					stmt1.setString(5, seq_rev_no);
					stmt1.setString(6, clearance);
					rset1 = stmt1.executeQuery();
					
					while(rset1.next())
					{
						String agmt = rset1.getString(1)==null?"0":rset1.getString(1);
//						String agmt = rset1.getString(1)==null?"":rset1.getString(1);
						String agmt_rev = rset1.getString(2)==null?"":rset1.getString(2);
						String cont = rset1.getString(3)==null?"":rset1.getString(3);
						String cont_rev = rset1.getString(4)==null?"":rset1.getString(4);
						cont_type = rset1.getString(5)==null?"":rset1.getString(5);
						String counterparty_cd = rset1.getString(6)==null?"":rset1.getString(6);
						String entityCd = rset1.getString(7)==null?"":rset1.getString(7);
					
						if(clearance.equals("I"))
						{
							String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
							String sharePercentage = rset1.getString(8)==null?"":rset1.getString(8);

							if(!sel_deal_dtl.equals(""))
							{
								deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
								sel_deal_dtl+="@@"+dealDtl;
								dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								share_percent+="@@"+sharePercentage;
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+entityCd;
								dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								sel_deal_dtl+=""+dealDtl;
								share_percent+=""+sharePercentage;
								disp_cont_type+=utilBean.getContractTypeName(cont_type);
							}

							//HP20230914 ADDED COUNTERPARTY_CD IN MAPPING ID AND ADDED COUNTERPARTY_ABBR IN DEAL NO FOR DISPLAY
						}
						else
						{
							String dealDtl=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
							String sharePercentage = rset1.getString(8)==null?"":rset1.getString(8);

							if(!sel_deal_dtl.equals(""))
							{
								deal_dtl+=", "+sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
								sel_deal_dtl+="@@"+dealDtl;
//								dealNo+=", "+utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
								dealNo+=", "+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								share_percent+="@@"+sharePercentage;
								disp_cont_type+=", "+utilBean.getContractTypeName(cont_type);
							}
							else
							{
								deal_dtl+=sec_ref_no+"/"+cont_type+"/"+agmt+"/"+agmt_rev+"/"+cont+"/"+cont_rev+"/"+counterparty_cd;
//								dealNo+=utilBean.getDisplayDealMapping(agmt, agmt_rev, agmt, cont_rev, cont_type);
								dealNo+=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
								sel_deal_dtl+=""+dealDtl;
								share_percent+=""+sharePercentage;
								disp_cont_type+=utilBean.getContractTypeName(cont_type);
							}

						}
					}
					rset1.close();
					stmt1.close();
					
					String buysell = "";
					if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("W") || cont_type.equals("E") || cont_type.equals("F") || cont_type.equals("B") || cont_type.equals("M"))
					{
						buysell = "Sell";
					}
					else if(cont_type.equals("P") || cont_type.equals("G") || cont_type.equals("N") || cont_type.equals("T") || cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("V"))
					{
						buysell = "Buy";
					}
					else
					{
						buysell="";
					}
					VBUY_SELL.add(buysell);

					int cnt=0;
					String sysdate = dateUtil.getSysdate();
					String queryString3 = "";
					if(clearance.equals("I"))
					{
						queryString3 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TRADE_REF_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN (?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TRADE_REF_NO "
								+ "FROM FMS_TRADER_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.TRADE_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_SUPPLY_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
								+ "AND A.CONTRACT_TYPE IN (?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.ENTITY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.TRADE_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_TRADER_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
								+ "AND A.CONTRACT_TYPE IN (?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.ENTITY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						queryString3 +="ORDER BY CONTRACT_TYPE ASC";
						//HP20231012 REMOVED DUPLICATE CONTRACTs
						//HP20230914 ADDING BUY DEALS
						//HP20230914 NO NEED TO CHECK COUNTERPARTY_CD FOR IGX CLEARANCE AND ADDED CHECKING ON CONTRACT_TYPE

					}
					else
					{
						queryString3 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_REF_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?,?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_REF_NO "
								+ "FROM FMS_TRADER_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_SUPPLY_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
								+ "AND A.CONTRACT_TYPE IN (?,?,?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_TRADER_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
								+ "AND A.CONTRACT_TYPE IN (?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						//HP20231012 REMOVED DUPLICATE CONTRACTs
						//HP20230914 ADDING BUY DEALS
						//HP20230914 ADDED CHECKING ON CONTRACT_TYPE
						
						//Pratham Bhatt for adding purchase Cargo and LTCORA deals
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_REF_NO "
								+ "FROM FMS_TRADER_CN_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_REF_NO "
								+ "FROM FMS_LTCORA_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?,?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_TRADER_CN_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? "
								+ "AND A.CONTRACT_TYPE IN (?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CN_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						queryString3+="UNION  ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_LTCORA_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? "
								+ "AND A.CONTRACT_TYPE IN (?,?,?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_LTCORA_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						
						//AP20250212 DLNG Service Deal
						
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_REF_NO "
								+ "FROM FMS_SVC_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_SVC_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
								+ "AND A.CONTRACT_TYPE IN (?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_SVC_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						queryString3 +=" UNION ";
						queryString3 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,COUNTERPARTY_CD,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),'',CONT_REF_NO "
								+ "FROM FMS_DERV_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) AND CONT_STATUS NOT IN (?,?) "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND SIGNING_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')";
						queryString3 +=" UNION ";
						queryString3 += "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.COUNTERPARTY_CD,TO_CHAR(B.SIGNING_DT,'DD/MM/YYYY'),'',B.CONT_REF_NO "
								+ "FROM FMS_SECURITY_DEAL_MAP A, FMS_DERV_CONT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? "
								+ "AND A.SEC_REF_NO=? AND A.SEQ_REV_NO=? "
								+ "AND A.CONTRACT_TYPE IN (?) AND CONT_STATUS NOT IN (?,?) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND B.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_DERV_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						queryString3 +="ORDER BY CONTRACT_TYPE ASC";

					}
					//HP20230914 DATE FORMATE CORRECTED
					//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
					stmt3 = conn.prepareStatement(queryString3);
					if(clearance.equals("I"))
					{
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, "X");
						stmt3.setString(++cnt, "W");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, "I");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, clearance);
						stmt3.setString(++cnt, "X");
						stmt3.setString(++cnt, "W");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, clearance);
						stmt3.setString(++cnt, "I");
					}
					else
					{
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "S");
						stmt3.setString(++cnt, "L");
						stmt3.setString(++cnt, "E");
						stmt3.setString(++cnt, "F");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "D");
						stmt3.setString(++cnt, "T");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, clearance);
						stmt3.setString(++cnt, "S");
						stmt3.setString(++cnt, "L");
						stmt3.setString(++cnt, "E");
						stmt3.setString(++cnt, "F");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, clearance);
						stmt3.setString(++cnt, "D");
						stmt3.setString(++cnt, "T");
						//Pratham Bhatt for CN Cargo and LTCORA deals 
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "N");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "G");
						stmt3.setString(++cnt, "P");
						stmt3.setString(++cnt, "O");
						stmt3.setString(++cnt, "Q");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, "N");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, "G");
						stmt3.setString(++cnt, "P");
						stmt3.setString(++cnt, "O");
						stmt3.setString(++cnt, "Q");
						//AP20250212 for DLNG Service Deals
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "B");
						stmt3.setString(++cnt, "M");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, clearance);
						stmt3.setString(++cnt, "B");
						stmt3.setString(++cnt, "M");
						//AP20250429 for Derivatives Deal
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, "V");
						stmt3.setString(++cnt, "X");
						stmt3.setString(++cnt, "C");
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterPartyCd);
						stmt3.setString(++cnt, seq_no);
						stmt3.setString(++cnt, sec_ref_no);
						stmt3.setString(++cnt, seq_rev_no);
						stmt3.setString(++cnt, "V");
						stmt3.setString(++cnt, "X");
						stmt3.setString(++cnt, "C");
					}
					rset3 = stmt3.executeQuery();
					while(rset3.next())
					{
						String agmt_no = rset3.getString(1)==null?"0":rset3.getString(1);
//						String agmt_no = rset3.getString(1)==null?"":rset3.getString(1);
						String agmt_rev_no = rset3.getString(2)==null?"":rset3.getString(2);
						String cont_no = rset3.getString(3)==null?"":rset3.getString(3);
						String cont_rev_no = rset3.getString(4)==null?"":rset3.getString(4);
						String cont_type_no = rset3.getString(5)==null?"":rset3.getString(5);
						String countpty_cd_no = rset3.getString(6)==null?"":rset3.getString(6);
						String start_dt = rset3.getString(7)==null?"":rset3.getString(7);
						String end_dt = rset3.getString(8)==null?"":rset3.getString(8);
						String cont_ref = rset3.getString(9)==null?"":rset3.getString(9);

						String cargo_no="";
						deal_No=utilBean.NewDealMappingId(comp_cd, countpty_cd_no, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type_no, cargo_no);
						//deal_No=comp_cd+cont_type_no+countpty_cd_no+"-"+agmt_no+"-"+cont_no;
						deal_No_dtl=sec_ref_no+"/"+cont_type_no+"/"+agmt_no+"/"+agmt_rev_no+"/"+cont_no+"/"+cont_rev_no+"/"+countpty_cd_no;
						//HP20230914 ADDED COUNTERPARTY_CD IN MAPPING ID
						
						String buy_sell = "";
						if(cont_type_no.equals("S") || cont_type_no.equals("L") || cont_type_no.equals("X") || cont_type_no.equals("O") || cont_type_no.equals("Q") || cont_type_no.equals("W") || cont_type_no.equals("E") || cont_type_no.equals("F") || cont_type_no.equals("B") || cont_type_no.equals("M"))
						{
							buy_sell = "Sell";
						}
						else if(cont_type_no.equals("P") || cont_type_no.equals("G") || cont_type_no.equals("N") || cont_type_no.equals("T") || cont_type_no.equals("D") || cont_type_no.equals("I") || cont_type.equals("V"))
						{
							buy_sell = "Buy";
						}

						VDURATION.add(start_dt+" - "+end_dt);
						VALL_DEAL_NO.add(deal_No);
						VALL_DEAL_DTL.add(deal_No_dtl);
						VALL_CP_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd_no)); //HP20230914		
						VALL_CONT_REF.add(cont_ref);
						VALL_BUY_SELL.add(buy_sell);
						VALL_CONT_TYPE.add(cont_type_no);
						VALL_CONT_TYPE_NAME.add(utilBean.getContractTypeName(cont_type_no));
					}
					
					VDEAL_NO.add(dealNo);
					VDIS_CONTRACT_TYPE.add(disp_cont_type);
					VDEAL_DTL.add(deal_dtl);
					VSEL_DEAL_DTL.add(sel_deal_dtl);
					VSHARE_PERCENT.add(share_percent);
					rset3.close();
					stmt3.close();

					String pdf_generated = "N";
					String pdf_name="";
					String queryString5 = "SELECT FILE_NAME "
							+ "FROM FMS_SECURITY_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=? AND FILE_TYPE=?";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, counterPartyCd);
					stmt5.setString(3, seq_no);
					stmt5.setString(4, seq_rev_no);
					stmt5.setString(5, clearance);
					stmt5.setString(6, "PDF");
					rset5 = stmt5.executeQuery();
					if(rset5.next())
					{
						pdf_generated = "Y";
						pdf_name = rset5.getString(1)==null?"":rset5.getString(1);
					}
					VPDF_GENERATED.add(pdf_generated);
					VPDF_FILE_NAME.add(pdf_name);

					rset5.close();
					stmt5.close();

					String pdf_rev_generated = "N";
					String rev_pdf_name="";
					String queryString6 = "SELECT FILE_NAME "
							+ "FROM FMS_SECURITY_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=? AND FILE_TYPE=?";
					stmt6 = conn.prepareStatement(queryString6);
					stmt6.setString(1, comp_cd);
					stmt6.setString(2, counterPartyCd);
					stmt6.setString(3, seq_no);
					stmt6.setString(4, seq_rev_no);
					stmt6.setString(5, clearance);
					stmt6.setString(6, "PDF_REV");
					rset6 = stmt6.executeQuery();
					if(rset6.next())
					{
						pdf_rev_generated = "Y";
						rev_pdf_name = rset6.getString(1)==null?"":rset6.getString(1);
					}
					VPDF_REV_GENERATED.add(pdf_rev_generated);
					VPDF_REV_FILE_NAME.add(rev_pdf_name);

					rset6.close();
					stmt6.close();
				}
				rset.close();
				stmt.close();

				String queryString2="";
				int cont1=0;
				queryString2 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,CONT_REF_NO,TRADE_REF_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? ";
				if(clearance.equals("I"))
				{
					queryString2 += "AND CONTRACT_TYPE IN (?,?) ";
				}
				else
				{
					queryString2 += "AND CONTRACT_TYPE IN (?,?,?,?) ";
				}
				queryString2 += "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString2 +=" UNION ";
				queryString2 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,CONT_REF_NO,TRADE_REF_NO  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? ";
				if(clearance.equals("I"))
				{
					queryString2 += "AND CONTRACT_TYPE=? ";
				}
				else
				{
					queryString2 += "AND CONTRACT_TYPE IN (?,?) ";
				}
				queryString2 += "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString2+=" UNION ";
				queryString2 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,CONT_REF_NO,''   "
						+ "FROM FMS_TRADER_CN_MST A  "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')  ";
				queryString2+=" UNION ";
				queryString2+="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,CONT_REF_NO,''    "
						+ "FROM FMS_LTCORA_CONT_MST A   "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN (?,?,?,?) AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD   "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString2+=" UNION ";
				queryString2 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,CONT_REF_NO,''  "
						+ "FROM FMS_SVC_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
					    + "AND CONTRACT_TYPE IN (?,?) "
					    + "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString2+=" UNION ";
				queryString2 += "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),'',COUNTERPARTY_CD,CONT_REF_NO,''  "
						+ "FROM FMS_DERV_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONTRACT_TYPE IN (?) AND CONT_STATUS NOT IN (?,?) "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND SIGNING_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
				queryString2 +="ORDER BY CONTRACT_TYPE ASC";
				
				
				//HP20230914 ADDED CHECKING ON CONTRACT_TYPE
				//HP20230914 DATE FORMATE CORRECTED
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++cont1, comp_cd);
				if(clearance.equals("I"))
				{
					stmt2.setString(++cont1, "X");
					stmt2.setString(++cont1, "W");
				}
				else
				{
					stmt2.setString(++cont1, "S");
					stmt2.setString(++cont1, "L");
					stmt2.setString(++cont1, "E");
					stmt2.setString(++cont1, "F");
				}
				stmt2.setString(++cont1, comp_cd);
				if(clearance.equals("I"))
				{
					stmt2.setString(++cont1, "I");
				}
				else
				{
					stmt2.setString(++cont1, "D");
					stmt2.setString(++cont1, "T");
				}
				stmt2.setString(++cont1,comp_cd);
				stmt2.setString(++cont1,"N");
				stmt2.setString(++cont1, comp_cd);
				stmt2.setString(++cont1, "G");
				stmt2.setString(++cont1, "P");
				stmt2.setString(++cont1, "O");
				stmt2.setString(++cont1, "Q");
				stmt2.setString(++cont1, comp_cd);
				stmt2.setString(++cont1, "M");
				stmt2.setString(++cont1, "B");
				stmt2.setString(++cont1, comp_cd);
				stmt2.setString(++cont1, "V");
				stmt2.setString(++cont1, "X");
				stmt2.setString(++cont1, "C");
				rset2 = stmt2.executeQuery();
				String map="";
				while(rset2.next())
				{
					String agmt = rset2.getString(1)==null?"0":rset2.getString(1);
//					String agmt = rset2.getString(1)==null?"":rset2.getString(1);
					String agmt_rev = rset2.getString(2)==null?"":rset2.getString(2);
					String cont = rset2.getString(3)==null?"":rset2.getString(3);
					String cont_rev = rset2.getString(4)==null?"":rset2.getString(4);
					String cont_type = rset2.getString(5)==null?"":rset2.getString(5);
					String start_dt = rset2.getString(6)==null?"":rset2.getString(6);
					String to_dt = rset2.getString(7)==null?"":rset2.getString(7);
					String countpty_cd = rset2.getString(8)==null?"":rset2.getString(8);
					String cont_ref = "";
					if(cont_type.equals("I") || cont_type.equals("X") || cont_type.equals("W"))
					{
						cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
					}
					else
					{
						cont_ref=rset2.getString(9)==null?"":rset2.getString(9);
					}
					
					String buy_sell="";
					if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("O") || cont_type.equals("Q") || cont_type.equals("W") || cont_type.equals("E") || cont_type.equals("F") || cont_type.equals("B") || cont_type.equals("M"))
					{
						buy_sell = "Sell";
					}
					else if(cont_type.equals("P") || cont_type.equals("G") || cont_type.equals("N") || cont_type.equals("T") || cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("V"))
					{
						buy_sell = "Buy";
					}
							

//					String dealno=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					//String dealno=comp_cd+cont_type+counterparty_cd+"-"+agmt+"-"+cont;
					String cargo_no="";
					
					String dealno=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					
					V_DEAL_NO.add(dealno);
					V_DURATION.add(start_dt+" - "+to_dt);
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					VCONTRACT_TYPE_NAME.add(utilBean.getContractTypeName(cont_type));
					VCONT_REF.add(cont_ref);
					V_BUY_SELL.add(buy_sell);

					VCONTRACT_DTL.add(countpty_cd+"-"+cont_type+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev);
					VNEW_COUNTPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				}
				rset2.close();
				stmt2.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getPreReceiptSecurityList()
	{
		String function_nm="getPreReceiptSecurityList()";
		try
		{
			counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			String queryString="SELECT A.COUNTERPARTY_CD,B.AGMT_NO,B.AGMT_REV,B.CONT_NO,B.CONT_REV,"
					+ "A.SEQ_NO,A.SEC_REF_NO,A.SEC_TYPE,A.STATUS,A.CURRENCY,A.VALUE,"
					+ "TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),TO_CHAR(A.ISSUE_DT,'DD/MM/YYYY'),TO_CHAR(A.EXPIRE_DT,'DD/MM/YYYY'),A.REMARKS,"
					+ "B.MAP_SEQ_NO,B.CONTRACT_TYPE,A.SEQ_REV_NO "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND SEC_CATEGORY=? "
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			//HP20230913 ADDED GX COLUMN IN WHERE CLAUSE
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sec_category);
			stmt.setString(4, agmt_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, gx);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmtRev=rset.getString(3)==null?"":rset.getString(3);
				String cont=rset.getString(4)==null?"":rset.getString(4);
				String contRev=rset.getString(5)==null?"":rset.getString(5);
				String contType=rset.getString(17)==null?"":rset.getString(17);

				//String deal_mapping_id=contType+agmt+"-"+agmtRev+"-"+cont+"-"+contRev;
				String deal_mapping_id=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmtRev, cont, contRev, contType, "");				
				VDEAL_MAPPING_ID.add(deal_mapping_id);

				VCOUNTERPARTY_CD.add(countpty_cd);
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmtRev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(contRev);

				VSEQ_NO.add(rset.getString(6)==null?"":rset.getString(6));
				VSECURITY_REF.add(rset.getString(7)==null?"":rset.getString(7));
				VSECURITY_TYPE.add(rset.getString(8)==null?"":rset.getString(8));

				String status=rset.getString(9)==null?"":rset.getString(9);
				VSTATUS.add(status);
				VSTATUS_NM.add(""+getStatusName(status));

				String currency=rset.getString(10)==null?"":rset.getString(10);
				VCURRENCY.add(currency);
				VCURRENCY_NM.add(""+utilBean.getRateUnitNm(conn,currency));

				VAMOUNT.add(rset.getString(11)==null?"":rset.getString(11));
				VDUE_DT.add(rset.getString(12)==null?"":rset.getString(12));
				VISSUE_DT.add(rset.getString(13)==null?"":rset.getString(13));
				VEXPIRE_DT.add(rset.getString(14)==null?"":rset.getString(14));
				VREMARK.add(rset.getString(15)==null?"":rset.getString(15));
				VMAP_SEQ_NO.add(rset.getString(16)==null?"":rset.getString(16));
				VCONTRACT_TYPE.add(contType);

				VSEQ_REV_NO.add(rset.getString(18)==null?"":rset.getString(18));

				String counterpty_name=""+utilBean.getCounterpartyName(conn,countpty_cd);
				String counterpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);

				VCOUNTERPARTY_NAME.add(counterpty_name);
				VCOUNTERPARTY_ABBR.add(counterpty_abbr);
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

	//HP20230915 FOLLOWING FUNCATION CREATED BY HP
	public void getCreditExceedDaysList()
	{
		String function_nm="getCreditExceedDaysList()";

		try
		{
			counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			String queryString="SELECT TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),STATUS,SEQ_NO,REMARK,VALUE,CURRENCY "
					+ "FROM FMS_CREDIT_EXCEED_DAYS "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "ORDER BY FROM_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VFROM_DT.add(rset.getString(1)==null?"":rset.getString(1));
				VTO_DT.add(rset.getString(2)==null?"":rset.getString(2));
				String status=rset.getString(3)==null?"":rset.getString(3);
				VSTATUS.add(status);
				VSTATUS_NM.add(getStatusName(status));
				VSEQ_NO.add(rset.getString(4)==null?"":rset.getString(4));
				VREMARK.add(rset.getString(5)==null?"":rset.getString(5));
				VVALUE.add(nf.format(rset.getDouble(6)));
				String currency=rset.getString(7)==null?"":rset.getString(7);
				VCURRENCY.add(currency);
				if(currency.equals("2"))
				{
					VCURRENCY_NM.add("USD");
				}
				else
				{
					VCURRENCY_NM.add("INR");
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

	//HP20230917 FOLLOWING FUNCATION CREATED BY HP
	public void getContractList()
	{
		String function_nm="getContractList()";
		try
		{
			if(!cont_mapp.equals(""))
			{
				String[] cont_split = cont_mapp.split("-");
				contract_type=cont_split[0];
				agmt_no=cont_split[1];
				cont_no=cont_split[2];
			}

			int count=0;
			String queryString="";

			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TRADE_REF_NO,CONTRACT_TYPE "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			if(!cont_mapp.equals(""))
			{
				queryString+="AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? ";
			}
			else
			{
				if(clearance.equals("IGX"))
				{
					queryString+="AND CONTRACT_TYPE IN ('X','W') ";
				}
				else
				{
					queryString+="AND CONTRACT_TYPE IN ('S','L','E','F') ";
				}
			}
			queryString+="ORDER BY START_DT DESC";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!cont_mapp.equals(""))
			{
				stmt.setString(++count, agmt_no);
				stmt.setString(++count, cont_no);
				stmt.setString(++count, contract_type);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String contpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				String cont=rset.getString(4)==null?"":rset.getString(4);
				String cont_rev=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				String st_dt=rset.getString(7)==null?"":rset.getString(7);
				String end_dt=rset.getString(8)==null?"":rset.getString(8);
				String cont_type=rset.getString(10)==null?"":rset.getString(10);

				if(cont_type.equals("X") || cont_type.equals("W"))
				{
					cont_ref=rset.getString(9)==null?"":rset.getString(9);
				}

				//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String dealNo=utilBean.NewDealMappingId(comp_cd, contpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				
				VCOUNTERPARTY_CD.add(contpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,contpty_cd));
				VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,contpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONT_REF_NO.add(cont_ref);
				VSTART_DT.add(st_dt);
				VEND_DT.add(end_dt);
				VDIS_CONT_MAPP.add(dealNo);
				VCONTRACT_TYPE.add(cont_type);

				int count_creditLine=0;
				String queryString1="SELECT COUNT(AGMT_NO) "
						+ "FROM FMS_CREDIT_EXCEED_DAYS "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contpty_cd);
				stmt1.setString(3, agmt);
				stmt1.setString(4, cont);
				stmt1.setString(5, cont_type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					count_creditLine=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();

				if(count_creditLine>0)
				{
					VCOUNT_CREDIT_LINE.add(count_creditLine);
				}
				else
				{
					VCOUNT_CREDIT_LINE.add("");
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

	//HP20230917 FOLLOWING FUNCATION CREATED BY HP
	public void getCounterpartyListForExceedCredit()
	{
		String function_nm="getCounterpartyListForExceedCredit()";
		try
		{
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? ";
			if(clearance.equals("IGX"))
			{
				queryString+="AND CONTRACT_TYPE IN ('X','W') ";
			}
			else
			{
				queryString+="AND CONTRACT_TYPE IN ('S','L','E','F') ";
			}
			queryString+= "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String contpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(contpty_cd);
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,contpty_cd));
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,contpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//HP20230917 FOLLOWING FUNCATION CREATED BY HP
	public void getContractListForExceedCredit()
	{
		String function_nm="getContractListForExceedCredit()";
		try
		{
			String queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "CONT_REF_NO,TRADE_REF_NO "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+= "AND COUNTERPARTY_CD=? ";
			}
			if(clearance.equals("IGX"))
			{
				queryString+="AND CONTRACT_TYPE IN ('X','W') ";
			}
			else
			{
				queryString+="AND CONTRACT_TYPE IN ('S','L','E','F') ";
			}
			queryString+= "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String agmtno=rset.getString(1)==null?"0":rset.getString(1);
				String agmtrev=rset.getString(2)==null?"0":rset.getString(2);
				String contno=rset.getString(3)==null?"0":rset.getString(3);
				String contrev=rset.getString(4)==null?"0":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);

				if(cont_type.equals("X") || cont_type.equals("W"))
				{
					cont_ref=rset.getString(7)==null?"":rset.getString(7);
				}

				String cont_map=cont_type+"-"+agmtno+"-"+contno;
				//String dealNo=utilBean.getDisplayDealMapping(agmtno, agmtrev, contno, contrev, cont_type);
				String dealNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
				if(!cont_ref.equals(""))
				{
					dealNo+=" ["+cont_ref+"]";
				}

				VCONT_MAP_LIST.add(cont_map);
				VDIS_CONT_MAP_LIST.add(dealNo);
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

	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String clearance = "";
	String adv_flag = "";
	String gx = "";
	String seq_no = "";//HP20230919
	String seq_rev_no = "";//HP20230919
	String sec_ref_no = "";//JD20230919
	String emp_cd = "";

	String counterPartyCd = "";
	String sec_category = "";
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
	//String dealNo = "";
	String cont_mapp="";//HP20230917

	String bank_cd="";
	String entity="";
	String entity_cd="";
	String limit_status="";
	String dealno = "";
	String po_parent_exit_dt="";

	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setAdv_flag(String adv_flag) {this.adv_flag = adv_flag;}
	public void setGx(String gx) {this.gx = gx;}
	public void setSec_category(String sec_category) {this.sec_category = sec_category;}
	public void setCont_mapp(String cont_mapp) {this.cont_mapp = cont_mapp;}//HP20230917
	public void setSeq_no(String seq_no) {this.seq_no = seq_no;}//HP20230919
	public void setSeq_rev_no(String seq_rev_no) {this.seq_rev_no = seq_rev_no;}//HP20230919
	public void setSec_ref_no(String sec_ref_no) {this.sec_ref_no = sec_ref_no;}//JD20230919
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}

	public void setBank_cd(String bank_cd) {this.bank_cd = bank_cd;}
	public void setEntity(String entity) {this.entity = entity;}
	public void setEntity_cd(String entity_cd) {this.entity_cd = entity_cd;}
	public void setLimit_status(String limit_status) {this.limit_status = limit_status;}

	public String getCounterPartyCd() {return counterPartyCd;}
	public String getSec_category() {return sec_category;}
	public String getSec_type() {return sec_type;}
	public String getStatus() {return status;}
	public String getCurrency() {return currency;}
	public String getValue() {return value;}
	public String getDealno() {return dealno;}
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
	public String getPo_parent_exit_dt() {return po_parent_exit_dt;}//HP20230919

	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NAME = new Vector();

	Vector VSECURITY_TYPE = new Vector();
	Vector VSECURITY_REF = new Vector();
	Vector VSTATUS = new Vector();
	Vector VSTATUS_NM = new Vector();
	Vector VCURRENCY = new Vector();
	Vector VCURRENCY_NM = new Vector();
	Vector VAMOUNT = new Vector();
	Vector VDUE_DT = new Vector();
	Vector VISSUE_DT = new Vector();
	Vector VEXPIRE_DT = new Vector();
	Vector VREMARK = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VMAP_SEQ_NO = new Vector();
	Vector VDEAL_MAPPING_ID = new Vector();

	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_NAME = new Vector();
	Vector VSEC_CATEGORY = new Vector();
	Vector VSEC_TYPE = new Vector();
	Vector VSEC_REF_NO = new Vector();
	Vector VVALUE = new Vector();
	Vector VRECEIVED_DATE = new Vector();
	Vector VMST_BANK_NM = new Vector();
	Vector VMST_BANK_CD = new Vector();
	Vector VMST_BANK_ABBR = new Vector();
	Vector VMST_BRANCH_NAME = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VISS_BANK_NM = new Vector();
	Vector VVALUE_FLUCTUATION = new Vector();
	Vector VDEAL_TYPE = new Vector();
	Vector VISS_BANK_REF = new Vector();
	Vector VADV_BANK_NM = new Vector();
	Vector VADV_BANK_REF = new Vector();
	Vector VCONFIRM_BANK_NM = new Vector();
	Vector VCONFIRM_BANK_REF = new Vector();
	Vector VREVIEW_DT = new Vector();
	Vector VTENOR = new Vector();
	Vector VVALUE_VARIATION = new Vector();
	Vector VGUARANTOR_NM = new Vector();
	Vector VGUARANTOR_CD = new Vector();
	Vector VADV_BANK_CD = new Vector();
	Vector VISS_BANK_CD = new Vector();
	Vector VCONFIRM_BANK_CD = new Vector();
	Vector VCANCEL_DT = new Vector();
	Vector VRENEW_DT = new Vector();
	Vector V_DEAL_NO = new Vector();
	Vector V_DURATION = new Vector();
	Vector VCONTRACT_DTL = new Vector();
	Vector VDEAL_DTL = new Vector();
	Vector VSEL_DEAL_DTL = new Vector();
	Vector VSEQ_REV_NO = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VALL_DEAL_DTL = new Vector();
	Vector VALL_DEAL_NO = new Vector();
	Vector VALL_CP_ABBR = new Vector(); //HP20230914
	Vector VEXP_VAL = new Vector();
	Vector VNEW_COUNTPTY_ABBR = new Vector();
	Vector VFROM_DT = new Vector();
	Vector VTO_DT = new Vector();
	Vector VSTART_DT = new Vector(); //HP20230917 
	Vector VEND_DT = new Vector(); //HP20230917
	Vector VCONT_REF_NO = new Vector(); //HP20230917
	Vector VDIS_CONT_MAPP = new Vector(); //HP20230917
	Vector VCOUNT_CREDIT_LINE = new Vector(); //HP20230917
	Vector VCONT_MAP_LIST = new  Vector();//HP20230917
	Vector VDIS_CONT_MAP_LIST = new  Vector();//HP20230917
	Vector VSAP_APPROVAL_FLAG = new  Vector();//HP20230919
	Vector VSAP_REVERSAL_FLAG = new  Vector();//HP20230920

	Vector VPARENT_OWENERSHIP_NAME = new Vector();
	Vector VPARENT_OWENERSHIP_ABBR = new Vector();
	Vector VPARENT_OWENERSHIP_CD = new Vector();
	Vector VCREDIT_RATING = new Vector();
	Vector VRATING_EFF_DT = new Vector();
	Vector VPARENT_OWENERSHIP = new Vector();
	Vector VLIMIT_ID = new Vector();
	Vector VPARENT_ENT_DT = new Vector();
	Vector VPARENT_EXIT_DT = new Vector();
	Vector VENT_DT = new Vector();
	Vector VREF_NO = new Vector();
	Vector VL_REF_NO = new Vector();
	Vector VL_LIMIT_TYPE = new Vector();
	Vector VL_ACTION_TYPE = new Vector();
	Vector VL_CATEGORY = new Vector();
	Vector VL_AMT = new Vector();
	Vector VL_ENT_DT = new Vector();
	Vector VL_EFF_DT = new Vector();
	Vector VL_EXP_DT = new Vector();
	Vector VL_INACT_DT = new Vector();
	Vector VL_REVIEW_DT = new Vector();
	Vector VL_REMARK = new Vector();
	Vector VL_STATUS = new Vector();
	Vector VL_SEQ_NO = new Vector();
	Vector VL_LIMIT_ID = new Vector();
	Vector VAVAILABLE = new Vector();
	Vector VTOTAL_LIMIT = new Vector();
	Vector VUNSECURED = new Vector();
	Vector VTEMPORARY = new Vector();
	Vector VADJUST_USAGE = new Vector();
	Vector VUSAGE = new Vector();
	Vector VNET_USAGE = new Vector();
	Vector VUSED = new Vector();
	Vector VYESNO = new Vector();

	Vector VDURATION = new Vector();
	Vector VSHARE_PERCENT = new Vector();

	Vector VL_DT_FLAG = new Vector();

	Vector VPO_COUNTERPARTY_CD = new Vector();
	Vector VPO_COUNTERPARTY_NM = new Vector();
	Vector VPO_COUNTERPARTY_ABBR = new Vector();
	Vector VPO_BANK_CD = new Vector();
	Vector VPO_BANK_NM = new Vector();
	Vector VPO_BANK_ABBR = new Vector();
	Vector VPO_BRANCH_NAME = new Vector();
	Vector VPO_GUARANTOR_CD = new Vector();
	Vector VPO_GUARANTOR_NAME = new Vector();
	Vector VPO_PARENT_EXIT_DT = new Vector();

	Vector VINORDER_HIST = new Vector();
	Vector VINFO = new Vector();
	Vector VADV_PG_REF = new Vector();
	Vector VPDF_GENERATED = new Vector();
	Vector VPDF_REV_GENERATED = new Vector();
	Vector VPDF_FILE_NAME = new Vector();
	Vector VPDF_REV_FILE_NAME = new Vector();

	Vector VBUY_SELL = new Vector();
	Vector VDLV_TERMS = new Vector();
	Vector VSPOT_TERMS = new Vector();
	Vector VPAYMENT_TERMS = new Vector();
	Vector VREQ_ID = new Vector();
	Vector VREQ_BY = new Vector();
	Vector VREQ_DT = new Vector();
	Vector VREQ_REMARK = new Vector();
	Vector VAPRV_DT = new Vector();
	Vector VAPRV_BY = new Vector();
	Vector VAPRV_REMARK = new Vector();
	Vector VCONT_COMP = new Vector();
	
	Vector VU_DEAL_NO = new Vector();
	Vector VU_COMP_PROFILE = new Vector();
	Vector VU_DELV_DT = new Vector();
	Vector VU_PRICE_TYPE = new Vector();
	Vector VU_DCQ = new Vector();
	Vector VU_PRICE = new Vector();
	Vector VU_TOTAL = new Vector();
	Vector VU_EXCH_RATE = new Vector();
	Vector VU_TAX = new Vector();
	Vector VU_GRAND_TOTAL = new Vector();
	
	Vector VPCG_SEC_REF = new Vector();
	Vector VPCG_SEC_TYPE =  new Vector();
	Vector VPCG_SEC_VALUE = new Vector();
	Vector VPCG_DEAL_NO =  new Vector();
	Vector VPCG_COMP_ABBR = new Vector();
	Vector VPCG_STATUS = new Vector();
	Vector VCONT_REF = new Vector();
	Vector V_BUY_SELL = new Vector();
	Vector VALL_CONT_REF = new Vector();
	Vector VALL_BUY_SELL = new Vector();
	Vector VALL_CONT_TYPE = new Vector();
	Vector VALL_CONT_TYPE_NAME = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();

	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NAME() {return VCONTRACT_TYPE_NAME;}

	public Vector getVSECURITY_TYPE() {return VSECURITY_TYPE;}
	public Vector getVSECURITY_REF() {return VSECURITY_REF;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVSTATUS_NM() {return VSTATUS_NM;}
	public Vector getVCURRENCY() {return VCURRENCY;}
	public Vector getVCURRENCY_NM() {return VCURRENCY_NM;}
	public Vector getVAMOUNT() {return VAMOUNT;}
	public Vector getVDUE_DT() {return VDUE_DT;}
	public Vector getVISSUE_DT() {return VISSUE_DT;}
	public Vector getVEXPIRE_DT() {return VEXPIRE_DT;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVMAP_SEQ_NO() {return VMAP_SEQ_NO;}
	public Vector getVDEAL_MAPPING_ID() {return VDEAL_MAPPING_ID;}

	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_NAME() {return VCOUNTERPARTY_NAME;}
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
	public Vector getVVALUE_FLUCTUATION() {return VVALUE_FLUCTUATION;}
	public Vector getVDEAL_TYPE() {return VDEAL_TYPE;}
	public Vector getVISS_BANK_REF() {return VISS_BANK_REF;}
	public Vector getVADV_BANK_NM() {return VADV_BANK_NM;}
	public Vector getVADV_BANK_REF() {return VADV_BANK_REF;}
	public Vector getVCONFIRM_BANK_NM() {return VCONFIRM_BANK_NM;}
	public Vector getVCONFIRM_BANK_REF() {return VCONFIRM_BANK_REF;}
	public Vector getVREVIEW_DT() {return VREVIEW_DT;}
	public Vector getVTENOR() {return VTENOR;}
	public Vector getVVALUE_VARIATION() {return VVALUE_VARIATION;}
	public Vector getVGUARANTOR_NM() {return VGUARANTOR_NM;}
	public Vector getVGUARANTOR_CD() {return VGUARANTOR_CD;}
	public Vector getVISS_BANK_CD() {return VISS_BANK_CD;}
	public Vector getVADV_BANK_CD() {return VADV_BANK_CD;}
	public Vector getVCONFIRM_BANK_CD() {return VCONFIRM_BANK_CD;}
	public Vector getVCANCEL_DT() {return VCANCEL_DT;}
	public Vector getVRENEW_DT() {return VRENEW_DT;}
	public Vector getV_DEAL_NO() {return V_DEAL_NO;}
	public Vector getV_DURATION() {return V_DURATION;}
	public Vector getVCONTRACT_DTL() {return VCONTRACT_DTL;}
	public Vector getVDEAL_DTL() {return VDEAL_DTL;}
	public Vector getVSEL_DEAL_DTL() {return VSEL_DEAL_DTL;}
	public Vector getVSEQ_REV_NO() {return VSEQ_REV_NO;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVALL_DEAL_DTL() {return VALL_DEAL_DTL;}
	public Vector getVALL_DEAL_NO() {return VALL_DEAL_NO;}
	public Vector getVALL_CP_ABBR() {return VALL_CP_ABBR;} //HP20230914
	public Vector getVEXP_VAL() {return VEXP_VAL;}
	public Vector getVNEW_COUNTPTY_ABBR() {return VNEW_COUNTPTY_ABBR;}
	public Vector getVFROM_DT() {return VFROM_DT;}
	public Vector getVTO_DT() {return VTO_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;} //HP20230917
	public Vector getVEND_DT() {return VEND_DT;} //HP20230917
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;} //HP20230917
	public Vector getVDIS_CONT_MAPP() {return VDIS_CONT_MAPP;} //HP20230917
	public Vector getVCOUNT_CREDIT_LINE() {return VCOUNT_CREDIT_LINE;} //HP20230917
	public Vector getVCONT_MAP_LIST() {return VCONT_MAP_LIST;} //HP20230917
	public Vector getVDIS_CONT_MAP_LIST() {return VDIS_CONT_MAP_LIST;} //HP20230917
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;} //HP20230919
	public Vector getVSAP_REVERSAL_FLAG() {return VSAP_REVERSAL_FLAG;} //HP20230920

	public Vector getVPARENT_OWENERSHIP_NAME() {return VPARENT_OWENERSHIP_NAME;}
	public Vector getVPARENT_OWENERSHIP_ABBR() {return VPARENT_OWENERSHIP_ABBR;}
	public Vector getVPARENT_OWENERSHIP_CD() {return VPARENT_OWENERSHIP_CD;}
	public Vector getVCREDIT_RATING() {return VCREDIT_RATING;}
	public Vector getVRATING_EFF_DT() {return VRATING_EFF_DT;}
	public Vector getVPARENT_OWENERSHIP() {return VPARENT_OWENERSHIP;}
	public Vector getVLIMIT_ID() {return VLIMIT_ID;}
	public Vector getVPARENT_ENT_DT() {return VPARENT_ENT_DT;}
	public Vector getVPARENT_EXIT_DT() {return VPARENT_EXIT_DT;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVREF_NO() {return VREF_NO;}
	public Vector getVL_REF_NO() {return VL_REF_NO;}
	public Vector getVL_LIMIT_TYPE() {return VL_LIMIT_TYPE;}
	public Vector getVL_ACTION_TYPE() {return VL_ACTION_TYPE;}
	public Vector getVL_CATEGORY() {return VL_CATEGORY;}
	public Vector getVL_AMT() {return VL_AMT;}
	public Vector getVL_ENT_DT() {return VL_ENT_DT;}
	public Vector getVL_EFF_DT() {return VL_EFF_DT;}
	public Vector getVL_EXP_DT() {return VL_EXP_DT;}
	public Vector getVL_INACT_DT() {return VL_INACT_DT;}
	public Vector getVL_REVIEW_DT() {return VL_REVIEW_DT;}
	public Vector getVL_REMARK() {return VL_REMARK;}
	public Vector getVL_STATUS() {return VL_STATUS;}
	public Vector getVL_SEQ_NO() {return VL_SEQ_NO;}
	public Vector getVL_LIMIT_ID() {return VL_LIMIT_ID;}
	public Vector getVAVAILABLE() {return VAVAILABLE;}
	public Vector getVTOTAL_LIMIT() {return VTOTAL_LIMIT;}
	public Vector getVUNSECURED() {return VUNSECURED;}
	public Vector getVTEMPORARY() {return VTEMPORARY;}
	public Vector getVADJUST_USAGE() {return VADJUST_USAGE;}
	public Vector getVUSAGE() {return VUSAGE;}
	public Vector getVNET_USAGE() {return VNET_USAGE;}
	public Vector getVUSED() {return VUSED;}
	public Vector getVYESNO() {return VYESNO;}

	public Vector getVDURATION() {return VDURATION;}
	public Vector getVSHARE_PERCENT() {return VSHARE_PERCENT;}
	public Vector getVL_DT_FLAG() {return VL_DT_FLAG;}

	public Vector getVPO_COUNTERPARTY_CD() {return VPO_COUNTERPARTY_CD;}
	public Vector getVPO_COUNTERPARTY_NM() {return VPO_COUNTERPARTY_NM;}
	public Vector getVPO_COUNTERPARTY_ABBR() {return VPO_COUNTERPARTY_ABBR;}
	public Vector getVPO_BANK_CD() {return VPO_BANK_CD;}
	public Vector getVPO_BANK_NM() {return VPO_BANK_NM;}
	public Vector getVPO_BANK_ABBR() {return VPO_BANK_ABBR;}
	public Vector getVPO_BRANCH_NAME() {return VPO_BRANCH_NAME;}
	public Vector getVPO_GUARANTOR_CD() {return VPO_GUARANTOR_CD;}
	public Vector getVPO_GUARANTOR_NAME() {return VPO_GUARANTOR_NAME;}
	public Vector getVPO_PARENT_EXIT_DT() {return VPO_PARENT_EXIT_DT;}

	public Vector getVINORDER_HIST() {return VINORDER_HIST;}
	public Vector getVINFO() {return VINFO;}
	public Vector getVADV_PG_REF() {return VADV_PG_REF;}
	public Vector getVPDF_GENERATED() {return VPDF_GENERATED;}
	public Vector getVPDF_REV_GENERATED() {return VPDF_REV_GENERATED;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	public Vector getVPDF_REV_FILE_NAME() {return VPDF_REV_FILE_NAME;}


	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVDLV_TERMS() {return VDLV_TERMS;}
	public Vector getVSPOT_TERMS() {return VSPOT_TERMS;}
	public Vector getVPAYMENT_TERMS() {return VPAYMENT_TERMS;}
	public Vector getVREQ_ID() {return VREQ_ID;}
	public Vector getVREQ_BY() {return VREQ_BY;}
	public Vector getVREQ_DT() {return VREQ_DT;}
	public Vector getVREQ_REMARK() {return VREQ_REMARK;}
	public Vector getVAPRV_DT() {return VAPRV_DT;}
	public Vector getVAPRV_BY() {return VAPRV_BY;}
	public Vector getVAPRV_REMARK() {return VAPRV_REMARK;};
	public Vector getVCONT_COMP() {return VCONT_COMP;}
	
	public Vector getVU_DEAL_NO() {return VU_DEAL_NO;}
	public Vector getVU_COMP_PROFILE() {return VU_COMP_PROFILE;}
	public Vector getVU_DELV_DT() {return VU_DELV_DT;}
	public Vector getVU_PRICE_TYPE() {return VU_PRICE_TYPE;}
	public Vector getVU_DCQ() {return VU_DCQ;}
	public Vector getVU_PRICE() {return VU_PRICE;}
	public Vector getVU_TOTAL() {return VU_TOTAL;}
	public Vector getVU_EXCH_RATE() {return VU_EXCH_RATE;}
	public Vector getVU_TAX() {return VU_TAX;}
	public Vector getVU_GRAND_TOTAL() {return VU_GRAND_TOTAL;}
	
	public Vector getVPCG_SEC_REF() {return VPCG_SEC_REF;}
	public Vector getVPCG_SEC_TYPE() {return VPCG_SEC_TYPE;}
	public Vector getVPCG_SEC_VALUE() {return VPCG_SEC_VALUE;}
	public Vector getVPCG_DEAL_NO() {return VPCG_DEAL_NO;}
	public Vector getVPCG_COMP_ABBR() {return VPCG_COMP_ABBR;}
	public Vector getVPCG_STATUS() {return VPCG_STATUS;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getV_BUY_SELL() {return V_BUY_SELL;}
	public Vector getVALL_CONT_REF() {return VALL_CONT_REF;}
	public Vector getVALL_BUY_SELL() {return VALL_BUY_SELL;}
	public Vector getVALL_CONT_TYPE() {return VALL_CONT_TYPE;}
	public Vector getVALL_CONT_TYPE_NAME() {return VALL_CONT_TYPE_NAME;}
	public Vector getVDIS_CONTRACT_TYPE() {return VDIS_CONTRACT_TYPE;}

	String counterparty_abbr="";
	String counterparty_name="";
	String company_abbr = "";
	
	String pcg_total_value="";
	String display_map_id="";
	String counterparty_status="";

	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getCounterparty_name() {return counterparty_name;}
	public String getCompany_abbr() {return company_abbr;}

	public String getPcg_total_value() {return pcg_total_value;}
	public String getDisplay_map_id() {return display_map_id;}
	public String getCounterparty_status() {return counterparty_status;}
}
