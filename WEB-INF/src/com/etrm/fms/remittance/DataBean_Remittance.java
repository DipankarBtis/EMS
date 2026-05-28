package com.etrm.fms.remittance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.TaxCalculator;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 29/11/2022
//Status	  		: Developing
public class DataBean_Remittance
{
	String db_src_file_name="DataBean_Remittance.java";
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt_tmp;
	PreparedStatement stmt_tmp1;
	PreparedStatement stmt_tmp2;
	PreparedStatement stmt_tmp3;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset_tmp;
	ResultSet rset_tmp1;
	ResultSet rset_tmp2;
	ResultSet rset_tmp3;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	
	double transaction_limit=5000000;
	double qty_MMBTU=0;
	int inv_index=0;
	
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
	    			if(callFlag.equalsIgnoreCase("REMITTANCE_PREPARATION_LIST"))
	    			{
	    				VREMITTANCE_LIST_ABBR.add("PROFRM_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("CD_PROV_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("PROV_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("CARGO_FINAL_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("CD_FINAL_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("FINAL_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("LTCORA_FINAL_HEAD");
	    					    				
	    				VREMITTANCE_LIST_NAME.add("Cargo Proforma Remittance");
	    				VREMITTANCE_LIST_NAME.add("Cargo Provisional Custom Duty Remittance");
	    				VREMITTANCE_LIST_NAME.add("Cargo Provisional Remittance");
	    				VREMITTANCE_LIST_NAME.add("Cargo Final Remittance");
	    				VREMITTANCE_LIST_NAME.add("Cargo Final Custom Duty Remittance");
	    				VREMITTANCE_LIST_NAME.add("Purchase Remittance");
	    				VREMITTANCE_LIST_NAME.add("LTCORA Purchase Remittance");
	    				
	    				calculatePDBondValue();
	    				
	    				if(billing_cycle.equals("0"))
	    				{
	    					forAllBillingOption();
	    				}
	    				else
	    				{
	    					getBillingCyclePeriod();
	    					
	    					inv_index=0;
	    					getProformaRemittancePreparationList("PF");
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getProformaRemittancePreparationList("CP");
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getProvisionalFinalRemittancePreparationList("P");
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getProvisionalFinalRemittancePreparationList("F");
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getProvisionalFinalRemittancePreparationList("CF");
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getRemittancePreparationList();
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getLTCORARemittancePreparationList();
	    					VINDEX.add(inv_index);
	    					
	    					getExistOtherInvoiceList();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SELLER_PAYMENT_DETAIL"))
	    			{
	    				getContractDetail();
	    				//getBuUnit();
	    				getContactPerson();
	    				getBuContactPerson();
	    				InvoiceCalculation();
	    				PartyInvoiceCalculation();
	    				storeInvoiceDataIntoHashMap();
	    				if(contract_type.equals("D") || contract_type.equals("I")) 
	    				{
	    					getTcsTdsInvDtl();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SELLER_DR_CR_NOTE"))
	    			{
	    				getApprovedInvoiceDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("INVOICE_DR_CR_NOTE_GEN"))
	    			{
	    				getBillingCyclePeriod();
	    				getTraderCounterpartyList();
	    				getApprovedInvoiceDetailForDRCRNote();
	    			}
	    			else if(callFlag.equalsIgnoreCase("F_FLOW_INVOICE"))
	    			{
	    				couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getFilterContractList();
	    				getBillingCyclePeriod();
	    				getCounterpartyList();
	    				getContractList();
	    				getPlantDetail();
	    				getAddressType();
	    				getOtherInvoiceContactPerson();
	    				getBuUnit();
	    				getBuContactPerson();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getExixtingF_FLowInvoice();
	    				}
	    				else
	    				{
	    					getF_FlowInvoice();
	    				}
	    				getInvoiceNo();
	    			}
	    			else if(callFlag.equalsIgnoreCase("F_FLOW_INVOICE_LIST"))
	    			{
	    				getF_FlowInvoiceList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("INVOICE_DETAIL"))
	    			{
	    				getInvoiceDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("FINAL_PRINT_INVOICE_PDF"))
	    			{
	    				getExistingInvoiceDataForPDFPrint();
	    				storeInvoiceDataIntoHashMap();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_INVOICE_MAIL"))
	    			{
	    				getSendInvoiceMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_SUG_REMITTANCE_PREPARATION_LIST"))
	    			{
	    				getLtcoraSugRemittancePreparationList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXISTING_CRDR_LIST"))
	    			{
	    				VREMITTANCE_LIST_NAME.add("Purchase Remittance");
	    				VREMITTANCE_LIST_NAME.add("LTCORA Purchase Remittance");
	    				
	    				for(int i=0; i<VREMITTANCE_LIST_NAME.size(); i++)
	    				{
		    				inv_index=0;
		    				String contType="'D','T','I'";
		    				if(VREMITTANCE_LIST_NAME.elementAt(i).equals("LTCORA Purchase Remittance"))
		    				{
		    					contType="'G','P'";
		    				}
	    					getCrDrPreparationList(contType);
	    					VINDEX.add(inv_index);
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_PREPARATION_LIST"))
	    			{
	    				getTraderListforCrDr();
	    				getInvoiceNoListforCrDr();
	    				getCriteriaList();
	    				getSelectedInvoiceDtl();
	    				getContactPerson();
	    				getBuContactPerson();
	    				getCRDRInvoiceDtl();
	    				getNewCRDRInvoiceDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_SEND_INVOICE_MAIL"))
	    			{
	    				getCrdrSendInvoiceMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_CHK_APRV_DTL"))
	    			{
	    				getSelectedInvoiceDtl();
	    				getCRDRInvoiceDtl();
	    				getNewCRDRInvoiceDtl();
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
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp1 != null){try{stmt_tmp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp2 != null){try{stmt_tmp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp3 != null){try{stmt_tmp3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}

	public void forAllBillingOption()
	{
		String function_nm="forAllBillingOption()";
		try
		{
			String temp_billing_cycle=billing_cycle;
			billing_cycle="10";
			getBillingCyclePeriod();
			
			inv_index=0;
			getProformaRemittancePreparationList("PF");
			VINDEX.add(inv_index);
			
			inv_index=0;
			getProformaRemittancePreparationList("CP");
			VINDEX.add(inv_index);
			
			inv_index=0;
			getProvisionalFinalRemittancePreparationList("P");
			VINDEX.add(inv_index);
			
			inv_index=0;
			getProvisionalFinalRemittancePreparationList("F");
			VINDEX.add(inv_index);
			
			inv_index=0;
			getProvisionalFinalRemittancePreparationList("CF");
			VINDEX.add(inv_index);
			
			inv_index=0;
			for(int i=1; i<=9; i++)
			{
				billing_cycle=""+i;
				getBillingCyclePeriod();
				getRemittancePreparationList();
			}
			VINDEX.add(inv_index);
			
			billing_cycle="8";
			getBillingCyclePeriod();
			getExistOtherInvoiceList();
			
			inv_index=0;
			for(int i=1; i<=9; i++)
			{
				billing_cycle=""+i;
				getBillingCyclePeriod();
				getLTCORARemittancePreparationList();
			}
			VINDEX.add(inv_index);
			
			billing_cycle=temp_billing_cycle;
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getBillingCyclePeriod()
	{
		String function_nm="getBillingCyclePeriod()";
		try
		{
			if(billing_cycle.equals("1") || billing_cycle.equals("2"))
			{
				billing_freq="F";
				if(billing_cycle.equals("1"))
				{
					billing_freq_nm="1st-Fortnight";
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="15/"+month+"/"+year;
				}
				else if(billing_cycle.equals("2"))
				{
					billing_freq_nm="2nd-Fortnight";
					period_start_dt="16/"+month+"/"+year;
					period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				}
			}
			else if(billing_cycle.equals("3") || billing_cycle.equals("4") || billing_cycle.equals("5") || billing_cycle.equals("6") || billing_cycle.equals("9"))
			{
				billing_freq="W";
				if(billing_cycle.equals("3"))
				{
					billing_freq_nm="1st-Weekly";
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="07/"+month+"/"+year;
				}
				else if(billing_cycle.equals("4"))
				{
					billing_freq_nm="2nd-Weekly";
					period_start_dt="08/"+month+"/"+year;
					period_end_dt="14/"+month+"/"+year;
				}
				else if(billing_cycle.equals("5"))
				{
					billing_freq_nm="3rd-Weekly";
					period_start_dt="15/"+month+"/"+year;
					period_end_dt="21/"+month+"/"+year;
				}
				else if(billing_cycle.equals("6"))
				{
					billing_freq_nm="4th-Weekly";
					period_start_dt="22/"+month+"/"+year;
					period_end_dt="28/"+month+"/"+year;
				}
				else if(billing_cycle.equals("9"))
				{
					billing_freq_nm="5th-Weekly";
					if(month.equals("02"))
					{
						int days=utilDate.getDays(""+utilDate.getLastDateOfMonth(month, year), ""+utilDate.getFirstDateOfMonth(month, year));
						if(days==29)
						{
							period_start_dt="29/"+month+"/"+year;
							period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
						}
						/*else
						{
							period_start_dt=""+utilDate.getLastDateOfMonth(month, year);
							period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
						}*/
					}
					else
					{
						period_start_dt="29/"+month+"/"+year;
						period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
					}
				}
			}
			else if(billing_cycle.equals("7"))
			{
				billing_freq_nm="Monthly";
				billing_freq="M";
				period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			else if(billing_cycle.equals("8"))
			{
				billing_freq="O";
				billing_freq_nm="Other";
				//period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				//period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			else if(billing_cycle.equals("10"))
			{
				billing_freq="D";
				billing_freq_nm="Delivery Period";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void calculatePDBondValue()
	{
		String function_nm="getBillingCyclePeriod()";
		try
		{
			double temp_pd_bond=0;
			queryString="SELECT SUM(PD_BOND) "
					+ "FROM FMS_CUSTOM_PD_BOND_DTL "
					+ "WHERE COMPANY_CD=? AND CAL_YEAR=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, year);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				temp_pd_bond=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			double temp_cd_value=0;
			queryString="SELECT SUM(NET_PAYABLE_AMT) "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND TO_CHAR(INVOICE_DT,'YYYY')=? "
					+ "AND CONTRACT_TYPE=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? ";
			queryString+=" UNION ALL ";
			queryString+="SELECT SUM(NET_PAYABLE_AMT) "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND TO_CHAR(INVOICE_DT,'YYYY')=? "
					+ "AND CONTRACT_TYPE=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, year);
			stmt.setString(3, "N");
			stmt.setString(4, "CP");
			stmt.setString(5, comp_cd);
			stmt.setString(6, year);
			stmt.setString(7, "N");
			stmt.setString(8, "CP");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				temp_cd_value=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			double temp_balance = temp_pd_bond - temp_cd_value;
			
			pd_bond=nf.format(temp_pd_bond);
			cd_value=nf.format(temp_cd_value);
			pd_balance=nf.format(temp_balance);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getProvisionalFinalRemittancePreparationList(String inv_flag)
	{
		String function_nm="getProvisionalFinalRemittancePreparationList()";
		try
		{
			if(billing_cycle.equals("10"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
						+ "B.CARGO_NO,TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),B.CARGO_REF,"
						+ "D.BOE_NO,D.BU_SEQ,D.PLANT_SEQ,D.ACT_BOE_QTY,F.SHIP_CD "
						+ "FROM FMS_TRADER_CN_MST A, "
							+ "FMS_TRADER_CARGO_MST B, "
							+ "FMS_BUY_CARGO_NOM C,"
							+ "FMS_BUY_CARGO_ALLOC_BOE D,"
							+ "FMS_BUY_CARGO_ALLOC F ,"
							+ "FMS_TRADER_BILLING_DTL E, "
							+ "FMS_BUY_CARGO_NOM_BOE G "
						+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND D.ACT_BOE_QTY > 0 "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=C.EXP_TO_DT AND TO_DATE(?,'DD/MM/YYYY')>=C.EXP_TO_DT "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
						+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND G.COMPANY_CD=C.COMPANY_CD AND G.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND G.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND G.CONT_NO=C.CONT_NO AND G.AGMT_NO=C.AGMT_NO AND G.AGMT_TYPE=C.AGMT_TYPE AND G.CARGO_NO=C.CARGO_NO AND G.NOM_REV=C.NOM_REV "
						+ "AND A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND A.CONTRACT_TYPE=F.CONTRACT_TYPE "
						+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.AGMT_TYPE=F.AGMT_TYPE AND B.CARGO_NO=F.CARGO_NO "
						+ "AND F.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE F.COMPANY_CD=B.COMPANY_CD "
						+ "AND F.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND F.CONT_NO=B.CONT_NO AND F.AGMT_NO=B.AGMT_NO "
						+ "AND F.CONTRACT_TYPE=B.CONTRACT_TYPE AND F.CARGO_NO=B.CARGO_NO AND F.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND D.COMPANY_CD=F.COMPANY_CD AND D.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND D.CONTRACT_TYPE=F.CONTRACT_TYPE "
						+ "AND D.CONT_NO=F.CONT_NO AND D.AGMT_TYPE=F.AGMT_TYPE AND D.AGMT_NO=F.AGMT_NO AND D.CARGO_NO=F.CARGO_NO AND D.ALLOC_REV=F.ALLOC_REV "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? "
						+ "AND D.BOE_NO=G.BOE_NO ";
				if(inv_flag.equals("P"))
				{
					queryString+="AND (SELECT COUNT(*) FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=?) > 0";
				}
				else if(inv_flag.equals("F"))
				{
					queryString+="AND D.BOE_PROVISIONAL_PRICE IS NOT NULL AND D.BOE_FINAL_PRICE IS NOT NULL "
							+ "AND D.BOE_PROVISIONAL_PRICE != D.BOE_FINAL_PRICE ";
					queryString+="AND ((SELECT COUNT(*) INV_COUNT FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) > 0 "
							+ "OR "
							+ "(SELECT COUNT(*) INV_COUNT FROM FMS_PUR_PG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) > 0)";
				}
				else if(inv_flag.equals("CF"))
				{
					queryString+="AND D.CUSTOM_DUTY=? ";
					queryString+="AND D.BOE_PROVISIONAL_PRICE IS NOT NULL AND D.BOE_FINAL_PRICE IS NOT NULL AND G.BOE_PRICE IS NOT NULL "
							+ "AND (G.BOE_PRICE != D.BOE_FINAL_PRICE OR G.BOE_QTY != D.ACT_BOE_QTY) ";
					queryString+="AND (CASE WHEN D.BOE_PROVISIONAL_PRICE != D.BOE_FINAL_PRICE THEN (SELECT SUM((SELECT COUNT(*) INV_COUNT FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) + "
							+ "(SELECT COUNT(*) INV_COUNT FROM FMS_PUR_PG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL)) FROM DUAL) ELSE "
							+ "(SELECT SUM((SELECT COUNT(*) INV_COUNT FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) + "
							+ "(SELECT COUNT(*) INV_COUNT FROM FMS_PUR_PG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL)) FROM DUAL) END) > 0 ";
					queryString+="AND ((SELECT COUNT(*) INV_COUNT FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) > 0 "
							+ "OR "
							+ "(SELECT COUNT(*) INV_COUNT FROM FMS_PUR_PG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=? AND S.PDF_INV_DTL IS NOT NULL) > 0)";
				}
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				int st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, temp_period_start_dt);
				stmt.setString(++st_count, temp_period_end_dt);
				stmt.setString(++st_count, billing_freq);
				if(inv_flag.equals("P"))
				{
					stmt.setString(++st_count, "PF");
				}
				else if(inv_flag.equals("F"))
				{
					stmt.setString(++st_count, "P");
					stmt.setString(++st_count, "P");
				}
				else if(inv_flag.equals("CF"))
				{
					stmt.setString(++st_count, "Y");
					stmt.setString(++st_count, "F");
					stmt.setString(++st_count, "F");
					stmt.setString(++st_count, "P");
					stmt.setString(++st_count, "P");
					stmt.setString(++st_count, "CP");
					stmt.setString(++st_count, "CP");
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String contno=rset.getString(3)==null?"0":rset.getString(3);
					String contrev=rset.getString(4)==null?"0":rset.getString(4);
					String agmtno=rset.getString(5)==null?"0":rset.getString(5);
					String agmtrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_type=rset.getString(7)==null?"":rset.getString(7);
					String cn_cargo_no=rset.getString(8)==null?"":rset.getString(8);
					String cargo_st_dt=rset.getString(9)==null?"":rset.getString(9);
					String cargo_end_dt=rset.getString(10)==null?"":rset.getString(10);
					String cargo_ref=rset.getString(11)==null?"":rset.getString(11);
					String boe_no=rset.getString(12)==null?"":rset.getString(12);
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					String plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
					double qtyMMBTU=rset.getDouble(15);
					String ship_cd = rset.getString(16)==null?"":rset.getString(16);
					
					String dealNo=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cn_cargo_no);
					inv_index=inv_index+1;
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cn_cargo_no);
					VBOE_NO.add(boe_no);
					VBOE_NM.add("BOE"+utilBean.PrePaddingZero(boe_no, 2));
					VSTART_DT.add(cargo_st_dt);
					VEND_DT.add(cargo_end_dt);
					VCONT_NAME.add("");
					VCONT_REF_NO.add(cargo_ref);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(dealNo);

					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(cargo_st_dt);
					VPERIOD_END_DT.add(cargo_end_dt);
					
					VALLOC_QTY.add(nf.format(qtyMMBTU));
					
					VSPLIT_FLAG.add("");
					VSPLIT_VALUE.add("");
					
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, billing_cycle, cargo_st_dt, cargo_end_dt, inv_flag, inv_title,cn_cargo_no,boe_no);
					
					VBILLING_FREQ_FLAG.add(billing_cycle);
					VBILLING_FREQ_NM.add(billing_freq_nm);
					VSHIP_NAME.add(utilBean.getShipName(conn,ship_cd));
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getProformaRemittancePreparationList(String inv_flag)
	{
		String function_nm="getProformaRemittancePreparationList()";
		try
		{
			
			if(billing_cycle.equals("10"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
						+ "B.CARGO_NO,TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),B.CARGO_REF,"
						+ "D.BOE_NO,D.BU_SEQ,D.PLANT_SEQ,D.BOE_QTY,C.SHIP_CD "
						+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B, FMS_BUY_CARGO_NOM C,FMS_BUY_CARGO_NOM_BOE D, FMS_TRADER_BILLING_DTL E "
						+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND D.BOE_QTY > 0 "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=C.EXP_TO_DT AND TO_DATE(?,'DD/MM/YYYY')>=C.EXP_TO_DT "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
						+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND D.COMPANY_CD=C.COMPANY_CD AND D.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND D.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND D.CONT_NO=C.CONT_NO AND D.AGMT_TYPE=C.AGMT_TYPE AND D.AGMT_NO=C.AGMT_NO AND D.CARGO_NO=C.CARGO_NO AND D.NOM_REV=C.NOM_REV "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.BILLING_FREQ=? ";
				if(inv_flag.equals("CP"))
				{
					queryString+="AND D.CUSTOM_DUTY=? ";
					queryString+="AND (SELECT COUNT(*) FROM FMS_PUR_SG_INV_MST S WHERE S.COMPANY_CD=A.COMPANY_CD AND S.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND S.CONT_NO=A.CONT_NO AND S.AGMT_NO=A.AGMT_NO AND S.CONTRACT_TYPE=A.CONTRACT_TYPE AND S.CARGO_NO=B.CARGO_NO AND S.BOE_NO=D.BOE_NO "
							+ "AND S.INV_FLAG=?) > 0";
				}
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "Y");
				stmt.setString(3, temp_period_start_dt);
				stmt.setString(4, temp_period_end_dt);
				stmt.setString(5, billing_freq);
				if(inv_flag.equals("CP"))
				{
					stmt.setString(6, "Y");
					stmt.setString(7, "PF");
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String contno=rset.getString(3)==null?"0":rset.getString(3);
					String contrev=rset.getString(4)==null?"0":rset.getString(4);
					String agmtno=rset.getString(5)==null?"0":rset.getString(5);
					String agmtrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_type=rset.getString(7)==null?"":rset.getString(7);
					String cn_cargo_no=rset.getString(8)==null?"":rset.getString(8);
					String cargo_st_dt=rset.getString(9)==null?"":rset.getString(9);
					String cargo_end_dt=rset.getString(10)==null?"":rset.getString(10);
					String cargo_ref=rset.getString(11)==null?"":rset.getString(11);
					String boe_no=rset.getString(12)==null?"":rset.getString(12);
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					String plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
					double qtyMMBTU=rset.getDouble(15);
					String ship_cd = rset.getString(16)==null?"":rset.getString(16);
					
					String dealNo=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cn_cargo_no);
					inv_index=inv_index+1;
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cn_cargo_no);
					VBOE_NO.add(boe_no);
					VBOE_NM.add("BOE"+utilBean.PrePaddingZero(boe_no, 2));
					VSTART_DT.add(cargo_st_dt);
					VEND_DT.add(cargo_end_dt);
					VCONT_NAME.add("");
					VCONT_REF_NO.add(cargo_ref);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(dealNo);

					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(cargo_st_dt);
					VPERIOD_END_DT.add(cargo_end_dt);
					
					VALLOC_QTY.add(nf.format(qtyMMBTU));
					
					VSPLIT_FLAG.add("");
					VSPLIT_VALUE.add("");
					
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, billing_cycle, cargo_st_dt, cargo_end_dt, inv_flag, inv_title,cn_cargo_no,boe_no);
					
					VBILLING_FREQ_FLAG.add(billing_cycle);
					VBILLING_FREQ_NM.add(billing_freq_nm);
					VSHIP_NAME.add(utilBean.getShipName(conn,ship_cd));
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	public void getInvDetailForPreparationList(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt, String inv_flag, String inv_title, String cargo_no, String boe_no)
	{
		String function_nm="getInvDetailForPreparationList()";
		try
		{
			String inv_no="";
			String inv_seq="";
			String fin_yr="";
			String sts="";
			String aprove="N";
			String check="N";
			String auth="N";
			String is_submitted="N";
			String approve_inv_flag="";
			String pdf_inv_flag="N";
			String sap_approved_flag="";
			String payment_type_flag="";
			String sys_inv_no="";
			String gen_flag="";
			String inv_dt="";
			
			queryString3="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,"
					+ "PDF_INV_DTL,SAP_APPROVAL,SYS_INV_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
			if(!inv_flag.equals("UG")) //NO NEED TO CHECK BILLING FREQ AND BILLING PERIOD FOR SUG INVOICE
			{
				queryString3+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  ";
			}
			if(cont_type.equals("N"))
			{
				queryString3+="AND CARGO_NO=? AND BOE_NO=? ";
			}
			else if(cont_type.equals("G") || cont_type.equals("P"))
			{
				queryString3+="AND CARGO_NO=? ";
			}
			int stcount=0;
			stmt_tmp=conn.prepareStatement(queryString3);
			stmt_tmp.setString(++stcount, own_cd);
			stmt_tmp.setString(++stcount, countpty_cd);
			stmt_tmp.setString(++stcount, contno);
			stmt_tmp.setString(++stcount, agmtno);
			stmt_tmp.setString(++stcount, plant_seq);
			stmt_tmp.setString(++stcount, bu_plant_seq);
			stmt_tmp.setString(++stcount, cont_type);
			stmt_tmp.setString(++stcount, inv_flag);
			if(!inv_flag.equals("UG"))
			{
				stmt_tmp.setString(++stcount, billing_cycle);
				stmt_tmp.setString(++stcount, period_start_dt);
				stmt_tmp.setString(++stcount, period_end_dt);
			}
			if(cont_type.equals("N"))
			{
				stmt_tmp.setString(++stcount, cargo_no);
				stmt_tmp.setString(++stcount, boe_no);
			}
			else if(cont_type.equals("G") || cont_type.equals("P"))
			{
				stmt_tmp.setString(++stcount, cargo_no);
			}
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				//inv_no="SG : "+(rset_tmp.getString(1)==null?"":rset_tmp.getString(1));
				inv_no=rset_tmp.getString(1)==null?"":rset_tmp.getString(1);
				String chk_flg = rset_tmp.getString(2)==null?"":rset_tmp.getString(2);
				String auth_flg = rset_tmp.getString(3)==null?"":rset_tmp.getString(3);
				String aprv_flg = rset_tmp.getString(4)==null?"":rset_tmp.getString(4);
				inv_seq = rset_tmp.getString(5)==null?"":rset_tmp.getString(5);
				fin_yr = rset_tmp.getString(6)==null?"":rset_tmp.getString(6);
				String pdf_inv_dtl = rset_tmp.getString(7)==null?"":rset_tmp.getString(7);
				sap_approved_flag=rset_tmp.getString(8)==null?"":rset_tmp.getString(8);
				sys_inv_no=rset_tmp.getString(9)==null?"":rset_tmp.getString(9);
				inv_dt=rset_tmp.getString(10)==null?"":rset_tmp.getString(10);
				
				is_submitted="Y";
				gen_flag="S";
				if(chk_flg.equals("Y"))
				{
					check="Y";
				}
				if(auth_flg.equals("Y"))
				{
					auth="Y";
				}
				if(aprv_flg.equals("A"))
				{
					aprove="Y";
					approve_inv_flag="S";
					payment_type_flag="S";
				}
				if(pdf_inv_dtl.equals("O"))
				{
					pdf_inv_flag="Y";
				}

				sts="SG : "+(""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg));
			}
			else
			{
				fin_yr = utilDate.getFinancialYear(period_end_dt);
			}
			rset_tmp.close();
			stmt_tmp.close();

			queryString4="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,"
					+ "PDF_INV_DTL,SAP_APPROVAL "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
			if(!inv_flag.equals("UG"))
			{
				queryString4+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  ";
			}
			if(cont_type.equals("N"))
			{
				queryString4+="AND CARGO_NO=? AND BOE_NO=? ";
			}
			else if(cont_type.equals("G") || cont_type.equals("P"))
			{
				queryString4+="AND CARGO_NO=? ";
			}
			stcount=0;
			stmt_tmp1=conn.prepareStatement(queryString4);
			stmt_tmp1.setString(++stcount, own_cd);
			stmt_tmp1.setString(++stcount, countpty_cd);
			stmt_tmp1.setString(++stcount, contno);
			stmt_tmp1.setString(++stcount, agmtno);
			stmt_tmp1.setString(++stcount, plant_seq);
			stmt_tmp1.setString(++stcount, bu_plant_seq);
			stmt_tmp1.setString(++stcount, cont_type);
			stmt_tmp1.setString(++stcount, inv_flag);
			if(!inv_flag.equals("UG"))
			{
				stmt_tmp1.setString(++stcount, billing_cycle);
				stmt_tmp1.setString(++stcount, period_start_dt);
				stmt_tmp1.setString(++stcount, period_end_dt);
			}
			if(cont_type.equals("N"))
			{
				stmt_tmp1.setString(++stcount, cargo_no);
				stmt_tmp1.setString(++stcount, boe_no);
			}
			else if(cont_type.equals("G") || cont_type.equals("P"))
			{
				stmt_tmp1.setString(++stcount, cargo_no);
			}
			rset_tmp1=stmt_tmp1.executeQuery();
			if(rset_tmp1.next())
			{
				/*if(!inv_no.equals(""))
				{
					inv_no+="<br>PG : "+(rset_tmp1.getString(1)==null?"":rset_tmp1.getString(1));
				}
				else
				{
					inv_no+="PG : "+(rset_tmp1.getString(1)==null?"":rset_tmp1.getString(1));
				}*/

				String chk_flg = rset_tmp1.getString(2)==null?"":rset_tmp1.getString(2);
				String auth_flg = rset_tmp1.getString(3)==null?"":rset_tmp1.getString(3);
				String aprv_flg = rset_tmp1.getString(4)==null?"":rset_tmp1.getString(4);
				String pdf_inv_dtl = rset_tmp1.getString(5)==null?"":rset_tmp1.getString(5);
				sap_approved_flag=rset_tmp1.getString(6)==null?"":rset_tmp1.getString(6);
				
				is_submitted="Y";
				gen_flag="P";
				if(chk_flg.equals("Y"))
				{
					check="Y";
				}
				if(auth_flg.equals("Y"))
				{
					auth="Y";
				}
				if(aprv_flg.equals("A"))
				{
					aprove="Y";
					approve_inv_flag="P";
					payment_type_flag="P";
				}
				if(pdf_inv_dtl.equals("O"))
				{
					pdf_inv_flag="Y";
				}
				
				if(!sts.equals(""))
				{
					sts+="<br>PG : "+(""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg));
				}
				else
				{
					sts+="PG : "+(""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg));
				}
			}
			rset_tmp1.close();
			stmt_tmp1.close();
			
			int upload_count=0;
			queryString5="SELECT COUNT(*) "
					+ "FROM FMS_PUR_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
			stmt_tmp2=conn.prepareStatement(queryString5);
			stmt_tmp2.setString(1, own_cd);
			stmt_tmp2.setString(2, cont_type);
			stmt_tmp2.setString(3, inv_seq);
			stmt_tmp2.setString(4, fin_yr);
			stmt_tmp2.setString(5, "PG_RECV");
			stmt_tmp2.setString(6, inv_flag);
			rset_tmp2=stmt_tmp2.executeQuery();
			if(rset_tmp2.next())
			{
				upload_count=rset_tmp2.getInt(1);
			}
			rset_tmp2.close();
			stmt_tmp2.close();
			
			
			queryString5="SELECT FILE_NAME,EMAIL_SENT "
					+ "FROM FMS_PUR_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
			stmt_tmp3=conn.prepareStatement(queryString5);
			stmt_tmp3.setString(1, own_cd);
			stmt_tmp3.setString(2, cont_type);
			stmt_tmp3.setString(3, inv_seq);
			stmt_tmp3.setString(4, fin_yr);
			stmt_tmp3.setString(5, inv_title);
			stmt_tmp3.setString(6, inv_flag);
			rset_tmp3=stmt_tmp3.executeQuery();
			if(rset_tmp3.next())
			{
				VUPLOADED_FILE_NAME.add(rset_tmp3.getString(1)==null?"":rset_tmp3.getString(1));
				VEMAIL_SENT.add(rset_tmp3.getString(2)==null?"":rset_tmp3.getString(2));
			}
			else
			{
				VUPLOADED_FILE_NAME.add("");
				VEMAIL_SENT.add("");
			}
			rset_tmp3.close();
			stmt_tmp3.close();

			VFILE_UPLOAD_COUNT.add(upload_count);
			
			VINVOICE_NO.add(inv_no);
			VREMITTANCE_NO.add(sys_inv_no);
			VINVOICE_SEQ.add(inv_seq);
			VFINANCIAL_YEAR.add(fin_yr);
			VSTATUS.add(sts);
			VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
			VPDF_INV_FLAG.add(pdf_inv_flag);
			VSAP_APPROVAL_FLAG.add(sap_approved_flag);
			if(payment_type_flag.equals(""))
			{
				VPAYMENT_TYPE_FLAG.add(gen_flag);
			}
			else
			{
				VPAYMENT_TYPE_FLAG.add(payment_type_flag);
			}
			
			VAPPROVE_FLAG_CHECK.add(aprove);
			VCHECK_FLAG_CHECK.add(check);
			VAUTHORIZ_FLAG_CHECK.add(auth);
			VIS_SUBMITTED.add(is_submitted);
			VINVOICE_DT.add(inv_dt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getRemittancePreparationList()
	{
		String function_nm="getRemittancePreparationList()";
		try
		{
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "C.BILLING_DAYS,A.TRADE_REF_NO,A.SPLIT_FLAG "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=?";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_end_dt);
				stmt.setString(3, temp_period_start_dt);
				stmt.setString(4, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					String billing_days=rset.getString(12)==null?"1":rset.getString(12);
					String trade_ref_no=rset.getString(13)==null?"":rset.getString(13);
					String split_flag=rset.getString(14)==null?"":rset.getString(14);
					
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}
					
					int temp_count=utilDate.getDays(cont_end_dt,temp_period_end_dt);
					if(temp_count <= 0)
					{
						temp_period_end_dt=cont_end_dt;
					}
					
					temp_count=utilDate.getDays(temp_period_start_dt,cont_st_dt);
					if(temp_count <= 1)
					{
						temp_period_start_dt=cont_st_dt;
					}
					
					int days=utilDate.getDays(temp_period_end_dt, temp_period_start_dt);
					int tot_row = days/Integer.parseInt(billing_days);
					
					if(days%2!=0)
					{
						tot_row+=1;
					}		
					
					String temp_dt = utilDate.getDate(temp_period_start_dt,"-1");
					
					for(int i=0;i<tot_row;i++)
					{
						queryString6="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') "
								+ "FROM DUAL";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, temp_dt);
						stmt6.setString(2, billing_days);
						rset6=stmt6.executeQuery();
						while(rset6.next())
						{
							String st_dt=utilDate.getDate(temp_dt,"1");
							temp_dt=rset6.getString(1);
							String end_dt = temp_dt;
							
							temp_count=utilDate.getDays(temp_dt,temp_period_end_dt);
							if(temp_count > 0)
							{
								end_dt=temp_period_end_dt;
							}
							
							Vector VTEMP_TRD_CD=new Vector();
							Vector VTEMP_PLANT_SEQ=new Vector();
							Vector VTEMP_PLANT_ABBR=new Vector();
							Vector VTEMP_SPLIT_VALUE=new Vector();
							
							queryString1="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
									+ "FROM FMS_TRADER_CONT_PLANT "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, contno);
							stmt1.setString(4, contrev);
							stmt1.setString(5, agmtno);
							stmt1.setString(6, agmtrev);
							stmt1.setString(7, cont_type);
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
								String split_value = rset1.getString(2)==null?"":rset1.getString(2);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
								
								VTEMP_TRD_CD.add(countpty_cd);
								VTEMP_PLANT_SEQ.add(plant_seq);
								VTEMP_PLANT_ABBR.add(plant_abbr);
								VTEMP_SPLIT_VALUE.add(split_value);
							}
							rset1.close();
							stmt1.close();
							
							if(split_flag.equals("Y"))
							{
								queryString2="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
										+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, countpty_cd);
								stmt2.setString(3, contno);
								stmt2.setString(4, contrev);
								stmt2.setString(5, agmtno);
								stmt2.setString(6, agmtrev);
								stmt2.setString(7, cont_type);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									String split_trd_cd = rset2.getString(1)==null?"":rset2.getString(1);
									String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
									String split_value = rset2.getString(3)==null?"":rset2.getString(3);
									String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,split_trd_cd, own_cd, plant_seq, "T");
									
									VTEMP_TRD_CD.add(split_trd_cd);
									VTEMP_PLANT_SEQ.add(plant_seq);
									VTEMP_PLANT_ABBR.add(plant_abbr);
									VTEMP_SPLIT_VALUE.add(split_value);
								}
								rset2.close();
								stmt2.close();
							}
							
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, contno);
							stmt2.setString(4, contrev);
							stmt2.setString(5, agmtno);
							stmt2.setString(6, agmtrev);
							stmt2.setString(7, cont_type);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
								String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
								
								for(int j=0;j<VTEMP_PLANT_SEQ.size();j++)
								{
									String plant_seq=""+VTEMP_PLANT_SEQ.elementAt(j);
									String plant_abbr=""+VTEMP_PLANT_ABBR.elementAt(j);
									String split_value=""+VTEMP_SPLIT_VALUE.elementAt(j);
								
									double qtyMMBTU=0;
									
									if(split_flag.equals("Y"))
									{
										qtyMMBTU=allocUtil.getPurchaseAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, bu_plant_seq, st_dt, end_dt,"0");
									
										if(qtyMMBTU>0 && !split_value.equals(""))
										{
											qtyMMBTU=(qtyMMBTU * (Double.parseDouble(""+split_value) / 100));
										}
										else
										{
											qtyMMBTU=0;
										}
									}
									else
									{
										qtyMMBTU=allocUtil.getPurchaseAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, "0",st_dt, end_dt);
									}
									
									if(qtyMMBTU>0)
									{
										String temp_countpty_cd=""+VTEMP_TRD_CD.elementAt(j);
										inv_index=inv_index+1;
										
										VCOUNTERPTY_CD.add(temp_countpty_cd);
										VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,temp_countpty_cd));
										VAGMT_NO.add(agmtno);
										VAGMT_REV_NO.add(agmtrev);
										VCONT_NO.add(contno);
										VCONT_REV_NO.add(contrev);
										VCARGO_NO.add("");
										VBOE_NO.add("");
										VBOE_NM.add("");
										VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
										VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
										VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
										VCONT_REF_NO.add(cont_ref_no);
										VCONTRACT_TYPE.add(cont_type);
										VDEAL_NO.add(deal_no);

										VPLANT_SEQ.add(plant_seq);
										VPLANT_ABBR.add(plant_abbr);
										VBU_PLANT_SEQ.add(bu_plant_seq);
										VBU_PLANT_ABBR.add(bu_plant_abbr);
										VPERIOD_START_DT.add(st_dt);
										VPERIOD_END_DT.add(end_dt);
										
										VSPLIT_FLAG.add(split_flag);
										if(split_flag.equals("Y"))
										{
											
											VSPLIT_VALUE.add(split_value);
										}
										else
										{
											VSPLIT_VALUE.add("");
										}
										
										VALLOC_QTY.add(nf.format(qtyMMBTU));
										
										String inv_flag="F";
										VINV_FLAG.add(inv_flag);
										getInvDetailForPreparationList(own_cd,temp_countpty_cd, contno, agmtno,cont_type, plant_seq, 
												bu_plant_seq, billing_cycle, st_dt, end_dt, inv_flag, inv_title,"","");

										VBILLING_FREQ_FLAG.add(billing_cycle);
										VBILLING_FREQ_NM.add(billing_freq_nm);
										
										VSHIP_NAME.add("");
									}
								}
							}
							rset2.close();
							stmt2.close();
						}
						rset6.close();
						stmt6.close();
					}
				}
				rset.close();
				stmt.close();

			}
			else
			{
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,A.SPLIT_FLAG "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=?";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, period_end_dt);
				stmt.setString(3, period_start_dt);
				stmt.setString(4, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");

					String trade_ref_no=rset.getString(12)==null?"":rset.getString(12);
					String split_flag=rset.getString(13)==null?"":rset.getString(13);
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}

					Vector VTEMP_TRD_CD=new Vector();
					Vector VTEMP_PLANT_SEQ=new Vector();
					Vector VTEMP_PLANT_ABBR=new Vector();
					Vector VTEMP_SPLIT_VALUE=new Vector();
					
					queryString1="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
							+ "FROM FMS_TRADER_CONT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, contno);
					stmt1.setString(4, contrev);
					stmt1.setString(5, agmtno);
					stmt1.setString(6, agmtrev);
					stmt1.setString(7, cont_type);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
						String split_value = rset1.getString(2)==null?"":rset1.getString(2);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
						
						VTEMP_TRD_CD.add(countpty_cd);
						VTEMP_PLANT_SEQ.add(plant_seq);
						VTEMP_PLANT_ABBR.add(plant_abbr);
						VTEMP_SPLIT_VALUE.add(split_value);
					}
					rset1.close();
					stmt1.close();
					
					
					if(split_flag.equals("Y"))
					{
						queryString2="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, contno);
						stmt2.setString(4, contrev);
						stmt2.setString(5, agmtno);
						stmt2.setString(6, agmtrev);
						stmt2.setString(7, cont_type);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							String split_trd_cd = rset2.getString(1)==null?"":rset2.getString(1);
							String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
							String split_value = rset2.getString(3)==null?"":rset2.getString(3);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,split_trd_cd, own_cd, plant_seq, "T");
							
							VTEMP_TRD_CD.add(split_trd_cd);
							VTEMP_PLANT_SEQ.add(plant_seq);
							VTEMP_PLANT_ABBR.add(plant_abbr);
							VTEMP_SPLIT_VALUE.add(split_value);
						}
						rset2.close();
						stmt2.close();
					}
					
					
					queryString2="SELECT PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_CONT_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, contno);
					stmt2.setString(4, contrev);
					stmt2.setString(5, agmtno);
					stmt2.setString(6, agmtrev);
					stmt2.setString(7, cont_type);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
						
						for(int i=0;i<VTEMP_PLANT_SEQ.size();i++)
						{
							String plant_seq=""+VTEMP_PLANT_SEQ.elementAt(i);
							String plant_abbr=""+VTEMP_PLANT_ABBR.elementAt(i);
							String split_value=""+VTEMP_SPLIT_VALUE.elementAt(i);
							
							double qtyMMBTU=0;
							
							if(split_flag.equals("Y"))
							{
								qtyMMBTU=allocUtil.getPurchaseAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, bu_plant_seq, period_start_dt, period_end_dt,"0");
								if(qtyMMBTU>0 && !split_value.equals(""))
								{
									qtyMMBTU=(qtyMMBTU * (Double.parseDouble(""+split_value) / 100));
								}
								else
								{
									qtyMMBTU=0;
								}
							}
							else
							{
								qtyMMBTU=allocUtil.getPurchaseAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, "0",period_start_dt, period_end_dt);
							}
							
							if(qtyMMBTU>0)
							{
								String temp_countpty_cd=""+VTEMP_TRD_CD.elementAt(i);
								inv_index=inv_index+1;
								
								VCOUNTERPTY_CD.add(temp_countpty_cd);
								VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,temp_countpty_cd));
								VAGMT_NO.add(agmtno);
								VAGMT_REV_NO.add(agmtrev);
								VCONT_NO.add(contno);
								VCONT_REV_NO.add(contrev);
								VCARGO_NO.add("");
								VBOE_NO.add("");
								VBOE_NM.add("");
								VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
								VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
								VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
								VCONT_REF_NO.add(cont_ref_no);
								VCONTRACT_TYPE.add(cont_type);
								VDEAL_NO.add(deal_no);

								VPLANT_SEQ.add(plant_seq);
								VPLANT_ABBR.add(plant_abbr);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								VPERIOD_START_DT.add(period_start_dt);
								VPERIOD_END_DT.add(period_end_dt);
								
								VSPLIT_FLAG.add(split_flag);
								if(split_flag.equals("Y"))
								{
									
									VSPLIT_VALUE.add(split_value);
								}
								else
								{
									VSPLIT_VALUE.add("");
								}
								
								VALLOC_QTY.add(nf.format(qtyMMBTU));
								
								String inv_flag="F";
								VINV_FLAG.add(inv_flag);
								getInvDetailForPreparationList(own_cd,temp_countpty_cd, contno, agmtno,cont_type, plant_seq, 
										bu_plant_seq, billing_cycle, period_start_dt, period_end_dt, inv_flag, inv_title,"","");
								
								VBILLING_FREQ_FLAG.add(billing_cycle);
								VBILLING_FREQ_NM.add(billing_freq_nm);
								
								VSHIP_NAME.add("");
							}
						}
					}
					rset2.close();
					stmt2.close();
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getLTCORARemittancePreparationList()
	{
		String function_nm="getLTCORARemittancePreparationList()";
		try
		{
			if(billing_cycle.equals("8"))
			{/*
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "C.BILLING_DAYS,A.TRADE_REF_NO,A.SPLIT_FLAG "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_end_dt);
				stmt.setString(3, temp_period_start_dt);
				stmt.setString(4, billing_freq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					String billing_days=rset.getString(12)==null?"1":rset.getString(12);
					String trade_ref_no=rset.getString(13)==null?"":rset.getString(13);
					String split_flag=rset.getString(14)==null?"":rset.getString(14);
					
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}
					
					int temp_count=utilDate.getDays(cont_end_dt,temp_period_end_dt);
					if(temp_count <= 0)
					{
						temp_period_end_dt=cont_end_dt;
					}
					
					temp_count=utilDate.getDays(temp_period_start_dt,cont_st_dt);
					if(temp_count <= 1)
					{
						temp_period_start_dt=cont_st_dt;
					}
					
					int days=utilDate.getDays(temp_period_end_dt, temp_period_start_dt);
					int tot_row = days/Integer.parseInt(billing_days);
					
					if(days%2!=0)
					{
						tot_row+=1;
					}		
					
					String temp_dt = utilDate.getDate(temp_period_start_dt,"-1");
					
					for(int i=0;i<tot_row;i++)
					{
						queryString6="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') "
								+ "FROM DUAL";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, temp_dt);
						stmt6.setString(2, billing_days);
						rset6=stmt6.executeQuery();
						while(rset6.next())
						{
							String st_dt=utilDate.getDate(temp_dt,"1");
							temp_dt=rset6.getString(1);
							String end_dt = temp_dt;
							
							temp_count=utilDate.getDays(temp_dt,temp_period_end_dt);
							if(temp_count > 0)
							{
								end_dt=temp_period_end_dt;
							}
							
							Vector VTEMP_TRD_CD=new Vector();
							Vector VTEMP_PLANT_SEQ=new Vector();
							Vector VTEMP_PLANT_ABBR=new Vector();
							Vector VTEMP_SPLIT_VALUE=new Vector();
							
							queryString1="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
									+ "FROM FMS_TRADER_CONT_PLANT "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, contno);
							stmt1.setString(4, contrev);
							stmt1.setString(5, agmtno);
							stmt1.setString(6, agmtrev);
							stmt1.setString(7, cont_type);
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
								String split_value = rset1.getString(2)==null?"":rset1.getString(2);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(countpty_cd, own_cd, plant_seq, "T");
								
								VTEMP_TRD_CD.add(countpty_cd);
								VTEMP_PLANT_SEQ.add(plant_seq);
								VTEMP_PLANT_ABBR.add(plant_abbr);
								VTEMP_SPLIT_VALUE.add(split_value);
							}
							rset1.close();
							stmt1.close();
							
							if(split_flag.equals("Y"))
							{
								queryString2="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
										+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, countpty_cd);
								stmt2.setString(3, contno);
								stmt2.setString(4, contrev);
								stmt2.setString(5, agmtno);
								stmt2.setString(6, agmtrev);
								stmt2.setString(7, cont_type);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									String split_trd_cd = rset2.getString(1)==null?"":rset2.getString(1);
									String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
									String split_value = rset2.getString(3)==null?"":rset2.getString(3);
									String plant_abbr=utilBean.getCounterpartyPlantABBR(split_trd_cd, own_cd, plant_seq, "T");
									
									VTEMP_TRD_CD.add(split_trd_cd);
									VTEMP_PLANT_SEQ.add(plant_seq);
									VTEMP_PLANT_ABBR.add(plant_abbr);
									VTEMP_SPLIT_VALUE.add(split_value);
								}
								rset2.close();
								stmt2.close();
							}
							
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, contno);
							stmt2.setString(4, contrev);
							stmt2.setString(5, agmtno);
							stmt2.setString(6, agmtrev);
							stmt2.setString(7, cont_type);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
								String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(own_cd, own_cd, bu_plant_seq, "B");
								
								for(int j=0;j<VTEMP_PLANT_SEQ.size();j++)
								{
									String plant_seq=""+VTEMP_PLANT_SEQ.elementAt(j);
									String plant_abbr=""+VTEMP_PLANT_ABBR.elementAt(j);
									String split_value=""+VTEMP_SPLIT_VALUE.elementAt(j);
								
									double qtyMMBTU=0;
									
									if(split_flag.equals("Y"))
									{
										qtyMMBTU=allocUtil.getPurchaseAllocationQty(own_cd, countpty_cd, agmtno, contno, cont_type, bu_plant_seq, st_dt, end_dt);
									
										if(qtyMMBTU>0 && !split_value.equals(""))
										{
											qtyMMBTU=(qtyMMBTU * (Double.parseDouble(""+split_value) / 100));
										}
										else
										{
											qtyMMBTU=0;
										}
									}
									else
									{
										qtyMMBTU=allocUtil.getPurchaseAllocationQty(own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, "0",st_dt, end_dt);
									}
									
									if(qtyMMBTU>0)
									{
										String temp_countpty_cd=""+VTEMP_TRD_CD.elementAt(j);
										inv_index=inv_index+1;
										
										VCOUNTERPTY_CD.add(temp_countpty_cd);
										VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(temp_countpty_cd, own_cd));
										VAGMT_NO.add(agmtno);
										VAGMT_REV_NO.add(agmtrev);
										VCONT_NO.add(contno);
										VCONT_REV_NO.add(contrev);
										VCARGO_NO.add("");
										VBOE_NO.add("");
										VBOE_NM.add("");
										VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
										VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
										VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
										VCONT_REF_NO.add(cont_ref_no);
										VCONTRACT_TYPE.add(cont_type);
										VDEAL_NO.add(deal_no);

										VPLANT_SEQ.add(plant_seq);
										VPLANT_ABBR.add(plant_abbr);
										VBU_PLANT_SEQ.add(bu_plant_seq);
										VBU_PLANT_ABBR.add(bu_plant_abbr);
										VPERIOD_START_DT.add(st_dt);
										VPERIOD_END_DT.add(end_dt);
										
										VSPLIT_FLAG.add(split_flag);
										if(split_flag.equals("Y"))
										{
											
											VSPLIT_VALUE.add(split_value);
										}
										else
										{
											VSPLIT_VALUE.add("");
										}
										
										VALLOC_QTY.add(nf.format(qtyMMBTU));
										
										String inv_flag="F";
										VINV_FLAG.add(inv_flag);
										getInvDetailForPreparationList(own_cd,temp_countpty_cd, contno, agmtno,cont_type, plant_seq, 
												bu_plant_seq, billing_cycle, st_dt, end_dt, inv_flag, inv_title,"","");

										VBILLING_FREQ_FLAG.add(billing_cycle);
										VBILLING_FREQ_NM.add(billing_freq_nm);
										
										VSHIP_NAME.add("");
									}
								}
							}
							rset2.close();
							stmt2.close();
						}
						rset6.close();
						stmt6.close();
					}
				}
				rset.close();
				stmt.close();
			*/
			}
			else
			{
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "
						+ ""
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND C.BILLING_FREQ=? "
						+ ""
						+ "AND (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_BUY_DAILY_ALLOCATION F "
		  				+ "WHERE F.CONT_NO=A.CONT_NO AND F.AGMT_NO=A.AGMT_NO AND F.COMPANY_CD=A.COMPANY_CD AND F.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND F.PLANT_SEQ=E.PLANT_SEQ_NO AND F.CONTRACT_TYPE=A.CONTRACT_TYPE AND F.BU_SEQ=D.PLANT_SEQ_NO AND F.CARGO_NO=B.CARGO_NO "
						+ "AND F.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND F.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B WHERE B.CONT_NO=F.CONT_NO AND B.AGMT_NO=F.AGMT_NO "
						+ "AND B.COMPANY_CD=F.COMPANY_CD AND B.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND B.TRANSPORTER_CD=F.TRANSPORTER_CD AND B.TRANS_SEQ=F.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=F.PLANT_SEQ AND B.CONTRACT_TYPE=F.CONTRACT_TYPE AND B.BU_SEQ=F.BU_SEQ AND B.GAS_DT=F.GAS_DT AND F.CARGO_NO=B.CARGO_NO)) > 0";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "T");
				stmt.setString(3, "Y");
				stmt.setString(4, "Y");
				stmt.setString(5, "L");
				stmt.setString(6, period_end_dt);
				stmt.setString(7, period_start_dt);
				stmt.setString(8, billing_freq);
				stmt.setString(9, period_start_dt);
				stmt.setString(10, period_end_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cargo_no=rset.getString(12)==null?"0":rset.getString(12);
					
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
					
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					int isGreter=utilDate.getDays(start_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					if(isGreter>1)
					{
						temp_st_dt=start_dt;
						temp_end_dt=period_end_dt;
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
					}
					
					if(utilDate.getDays(end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=end_dt;
					}
					
					double qtyMMBTU=0;
					qtyMMBTU=allocUtil.getPurchaseAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, cargo_no,temp_st_dt, temp_end_dt);
							
					if(qtyMMBTU>0)
					{
						String temp_countpty_cd=countpty_cd;
						inv_index=inv_index+1;
						
						VCOUNTERPTY_CD.add(temp_countpty_cd);
						VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,temp_countpty_cd));
						VAGMT_NO.add(agmtno);
						VAGMT_REV_NO.add(agmtrev);
						VCONT_NO.add(contno);
						VCONT_REV_NO.add(contrev);
						VCARGO_NO.add(cargo_no);
						VBOE_NO.add("");
						VBOE_NM.add("");
						VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
						VCONT_REF_NO.add(cont_ref_no);
						VCONTRACT_TYPE.add(cont_type);
						VDEAL_NO.add(deal_no);

						VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VPERIOD_START_DT.add(temp_st_dt);
						VPERIOD_END_DT.add(temp_end_dt);
						
						VSPLIT_FLAG.add("");
						VSPLIT_VALUE.add("");
						
						VALLOC_QTY.add(nf.format(qtyMMBTU));
						
						String inv_flag="F";
						VINV_FLAG.add(inv_flag);
						getInvDetailForPreparationList(own_cd,temp_countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, inv_flag, inv_title,cargo_no,"");
						
						VBILLING_FREQ_FLAG.add(billing_cycle);
						VBILLING_FREQ_NM.add(billing_freq_nm);
						
						VSHIP_NAME.add("");
					}
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getExistOtherInvoiceList()
	{
		String function_nm="getExistOtherInvoiceList()";
		try
		{
			//if(billing_cycle.equals("8"))
			//{
				period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			//}
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
					+ "ADDR_FLAG,BU_UNIT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_REF,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,"
					+ "PDF_INV_DTL,SAP_APPROVAL,CARGO_NO,FREQ,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? "; //AND FREQ=? ";
			//if(billing_cycle.equals("8"))
			//{
				queryString+=" AND INVOICE_DT >=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') ";
			//}
			//else
			//{
				//queryString+=" AND PERIOD_START_DT =TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT =TO_DATE(?,'DD/MM/YYYY') ";
			//}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			//stmt.setString(2, billing_cycle);
			stmt.setString(2, period_start_dt);
			stmt.setString(3, period_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				String entityTy="T";
				if(cont_type.equals("Y"))
				{
					entityTy="S";
				}
				else if(cont_type.equals("A"))
				{
					entityTy="V";
				}
				else if(cont_type.equals("H"))
				{
					entityTy="H";
				}
				
				String cargono=rset.getString(22)==null?"":rset.getString(22);
				String freq=rset.getString(23)==null?"":rset.getString(23);
				String inv_dt=rset.getString(24)==null?"":rset.getString(24);
				//String deal_no=cont_type+""+contno;
								
				String address_flag=rset.getString(8)==null?"":rset.getString(8);
				String plant_seq = "";
				String plant_abbr = "";
				if(address_flag.equals("R"))
				{
					plant_seq=address_flag;
					plant_abbr="Registered";
				}
				else if(address_flag.equals("C"))
				{
					plant_seq=address_flag;
					plant_abbr="Correspondence";
				}
				else if(address_flag.equals("B"))
				{
					plant_seq=address_flag;
					plant_abbr="Billing";
				}
				else if(!address_flag.equals("") && address_flag.startsWith("P"))
				{
					plant_seq=address_flag.substring(1,address_flag.length());
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
				}
				else if(!address_flag.equals("") && address_flag.startsWith("B"))
				{
					plant_seq=address_flag.substring(1,address_flag.length());
					plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,countpty_cd, own_cd, plant_seq, entityTy);
				}

				String bu_plant_seq = rset.getString(9)==null?"":rset.getString(9);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");

				String p_st_dt=rset.getString(10)==null?"":rset.getString(10);
				String p_end_dt=rset.getString(11)==null?"":rset.getString(11);

				String max_contrev="0";
				String ori_countpty_cd="0";
				queryString1="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,TRADE_REF_NO,CONT_REV,COUNTERPARTY_CD "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? "//AND COUNTERPARTY_CD='"+countpty_cd+"' " //REMOVED TO SUPPORT SPLIT CONTRACT
						+ "AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE B.COMPANY_CD=A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) ";
				queryString1+="UNION ";
				queryString1+="SELECT TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),A.CONT_NAME,B.CARGO_REF,NULL,A.CONT_REV,A.COUNTERPARTY_CD "
						+ "FROM FMS_TRADER_CN_MST A, "
							+ "FMS_TRADER_CARGO_MST B, "
							+ "FMS_BUY_CARGO_NOM C,"
							+ "FMS_BUY_CARGO_NOM_BOE G "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
						+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND C.COMPANY_CD=G.COMPANY_CD AND C.COUNTERPARTY_CD=G.COUNTERPARTY_CD AND C.CONTRACT_TYPE=G.CONTRACT_TYPE "
						+ "AND C.CONT_NO=G.CONT_NO AND C.AGMT_TYPE=G.AGMT_TYPE AND C.AGMT_NO=G.AGMT_NO AND C.CARGO_NO=G.CARGO_NO AND C.NOM_REV=G.NOM_REV ";
				queryString1+="UNION ALL ";
				queryString1+="SELECT TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,A.CONT_REF_NO,NULL,A.CONT_REV,A.COUNTERPARTY_CD "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND B.CARGO_NO=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				queryString1+="UNION ALL ";
				queryString1+="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),NULL,CONT_REF_NO,NULL,NULL,COUNTERPARTY_CD "
						+ "FROM FMS_CARGO_SVC_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? " 
						+ "AND ENTITY_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, agmtno);
				stmt1.setString(3, agmtrev);
				stmt1.setString(4, contno);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, own_cd);
				stmt1.setString(7, countpty_cd);
				stmt1.setString(8, agmtno);
				stmt1.setString(9, agmtrev);
				stmt1.setString(10, contno);
				stmt1.setString(11, cont_type);
				stmt1.setString(12, cargono);
				stmt1.setString(13, own_cd);
				stmt1.setString(14, countpty_cd);
				stmt1.setString(15, agmtno);
				stmt1.setString(16, agmtrev);
				stmt1.setString(17, contno);
				stmt1.setString(18, cont_type);
				stmt1.setString(19, cargono);
				stmt1.setString(20, "T");
				stmt1.setString(21, "L");
				stmt1.setString(22, own_cd);
				stmt1.setString(23, countpty_cd);
				stmt1.setString(24, entityTy);
				stmt1.setString(25, contno);
				stmt1.setString(26, cont_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VOTH_START_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VOTH_END_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VOTH_CONT_NAME.add(rset1.getString(3)==null?"":rset1.getString(3));
					
					String cont_ref_no=rset1.getString(4)==null?"":rset1.getString(4);
					String trade_ref_no=rset1.getString(5)==null?"":rset1.getString(5);
					max_contrev=rset1.getString(6)==null?"0":rset1.getString(6);
					ori_countpty_cd=rset1.getString(7)==null?"0":rset1.getString(7);
					
					if(cont_type.equals("I"))
					{
						cont_ref_no=trade_ref_no;
					}
					VOTH_CONT_REF_NO.add(cont_ref_no);
				}
				else
				{
					VOTH_START_DT.add("");
					VOTH_END_DT.add("");
					VOTH_CONT_NAME.add("");
					VOTH_CONT_REF_NO.add("");
				}
				rset1.close();
				stmt1.close();
				
				String deal_no=utilBean.NewDealMappingId(own_cd, ori_countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargono);
				
				VOTH_COUNTERPTY_CD.add(countpty_cd);
				VOTH_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VOTH_AGMT_NO.add(agmtno);
				VOTH_AGMT_REV_NO.add(agmtrev);
				VOTH_CONT_NO.add(contno);
				VOTH_CONT_REV_NO.add(contrev);

				VOTH_CONTRACT_TYPE.add(cont_type);
				VOTH_ENTITY.add(entityTy);
				VOTH_DEAL_NO.add(deal_no);

				VOTH_PLANT_SEQ.add(address_flag);
				VOTH_PLANT_ABBR.add(plant_abbr);
				VOTH_BU_PLANT_SEQ.add(bu_plant_seq);
				VOTH_BU_PLANT_ABBR.add(bu_plant_abbr);
				VOTH_PERIOD_START_DT.add(p_st_dt);
				VOTH_PERIOD_END_DT.add(p_end_dt);
				VOTH_INVOICE_DT.add(inv_dt);
				
				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+max_contrev;
				if(cont_type.equals("N") || cont_type.equals("G") || cont_type.equals("P")) {
					mapping_id+="-"+cargono;
				}
				VMAPPING_ID.add(mapping_id);

				String inv_no="";
				String sts="";
				String aprove="N";
				String check="N";
				String auth="N";
				String is_submitted="N";
				String approve_inv_flag="";
				String pdf_inv_flag="N";
				
				String sys_inv_no=rset.getString(12)==null?"":rset.getString(12);
				String chk_flg = rset.getString(13)==null?"":rset.getString(13);
				String auth_flg = rset.getString(14)==null?"":rset.getString(14);
				String aprv_flg = rset.getString(15)==null?"":rset.getString(15);
				String inv_ref=rset.getString(16)==null?"":rset.getString(16);
				
				String pdf_inv_dtl = rset.getString(20)==null?"":rset.getString(20);
				String sap_approved_flag = rset.getString(21)==null?"":rset.getString(21);
				
				is_submitted="Y";
				if(chk_flg.equals("Y"))
				{
					check="Y";
				}
				if(auth_flg.equals("Y"))
				{
					auth="Y";
				}
				if(aprv_flg.equals("A"))
				{
					aprove="Y";
					approve_inv_flag="S";
				}
				if(pdf_inv_dtl.equals("O"))
				{
					pdf_inv_flag="Y";
				}

				//inv_no="SG : "+(rset.getString(12)==null?"":rset.getString(12))+"<br>PG : "+(rset.getString(16)==null?"":rset.getString(16));
				inv_no=rset.getString(12)==null?"":rset.getString(12);

				sts=""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg);

				VOTH_INVOICE_NO.add(inv_no);
				VOTH_REMITTANCE_NO.add(sys_inv_no);
				VOTH_DIS_INVOICE_NO.add(inv_ref);
				VOTH_STATUS.add(sts);
				
				VOTH_APPROVE_INVOICE_FLAG.add(approve_inv_flag);
				VOTH_PDF_INV_FLAG.add(pdf_inv_flag);
				VOTH_SAP_APPROVAL_FLAG.add(sap_approved_flag);
				VOTH_PAYMENT_TYPE_FLAG.add("FF");
				
				VOTH_APPROVE_FLAG_CHECK.add(aprove);
				VOTH_CHECK_FLAG_CHECK.add(check);
				VOTH_AUTHORIZ_FLAG_CHECK.add(auth);
				VOTH_IS_SUBMITTED.add(is_submitted);
				
				String inv_seq=rset.getString(17)==null?"":rset.getString(17);
				String fin_yr=rset.getString(18)==null?"":rset.getString(18);
				String inv_type=rset.getString(19)==null?"":rset.getString(19);
				
				VOTH_INVOICE_SEQ.add(inv_seq);
				VOTH_FINANCIAL_YEAR.add(fin_yr);
				VOTH_INVOICE_TYPE.add(inv_type);

				VOTH_BILLING_FREQ_FLAG.add(freq);
				//VOTH_BILLING_FREQ_NM.add(billing_freq_nm);
				VOTH_BILLING_FREQ_NM.add("");
				
				int upload_count=0;
				queryString2="SELECT COUNT(*) "
						+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, own_cd);
				stmt2.setString(2, cont_type);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, inv_seq);
				stmt2.setString(5, fin_yr);
				stmt2.setString(6, "PG_RECV");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					upload_count=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				
				queryString3="SELECT FILE_NAME "
						+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, own_cd);
				stmt3.setString(2, cont_type);
				stmt3.setString(3, inv_type);
				stmt3.setString(4, inv_seq);
				stmt3.setString(5, fin_yr);
				stmt3.setString(6, fflow_inv_title);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					VOTH_UPLOADED_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
				}
				else
				{
					VOTH_UPLOADED_FILE_NAME.add("");
				}
				rset3.close();
				stmt3.close();
				
				VOTH_FILE_UPLOAD_COUNT.add(upload_count);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
			bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_unit, "B");
			
			energy_unit="1";
			energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
			
			if(contract_type.equals("N"))
			{
				if(inv_flag.equals("PF") || inv_flag.equals("CP"))
				{
					queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
							+ "B.CARGO_NO,TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),B.CARGO_REF,"
							+ "D.BOE_NO,D.BU_SEQ,D.PLANT_SEQ,D.BOE_QTY,D.BOE_PRICE,D.BOE_PRICE_UNIT,"
							+ "E.INVOICE_CUR_CD,E.PAYMENT_CUR_CD,E.DUE_DATE,E.EXCHNG_RATE_CD,E.DUE_DT_IN,"
							+ "E.EXCL_SAT_MAP,E.EXCHNG_RATE_CAL,E.EXCHNG_CRITERIA,NULL,NULL,E.HOLIDAY_STATE "
							+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B, FMS_BUY_CARGO_NOM C,FMS_BUY_CARGO_NOM_BOE D, FMS_TRADER_BILLING_DTL E "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.CARGO_NO=? AND D.BOE_NO=? AND E.PLANT_SEQ_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
							+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
							+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND D.COMPANY_CD=C.COMPANY_CD AND D.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND D.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND D.CONT_NO=C.CONT_NO AND D.AGMT_TYPE=C.AGMT_TYPE AND D.AGMT_NO=C.AGMT_NO AND D.CARGO_NO=C.CARGO_NO AND D.NOM_REV=C.NOM_REV "
							+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
							+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE";
				}
				else
				{	
					queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
							+ "B.CARGO_NO,TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY'),B.CARGO_REF,"
							+ "D.BOE_NO,D.BU_SEQ,D.PLANT_SEQ,D.ACT_BOE_QTY,D.BOE_PROVISIONAL_PRICE,D.BOE_PROVISIONAL_PRICE_UNIT, "
							+ "E.INVOICE_CUR_CD,E.PAYMENT_CUR_CD,E.DUE_DATE,E.EXCHNG_RATE_CD,E.DUE_DT_IN,"
							+ "E.EXCL_SAT_MAP,E.EXCHNG_RATE_CAL,E.EXCHNG_CRITERIA,D.BOE_FINAL_PRICE,D.BOE_FINAL_PRICE_UNIT,E.HOLIDAY_STATE "
							+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B, FMS_BUY_CARGO_NOM C,"
							+ "FMS_BUY_CARGO_ALLOC_BOE D,FMS_BUY_CARGO_ALLOC F, FMS_TRADER_BILLING_DTL E "
							+ ""
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.CARGO_NO=? AND D.BOE_NO=? AND E.PLANT_SEQ_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
							+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
							+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.AGMT_TYPE=F.AGMT_TYPE AND B.CARGO_NO=F.CARGO_NO "
							+ "AND F.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE F.COMPANY_CD=B.COMPANY_CD "
							+ "AND F.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND F.CONT_NO=B.CONT_NO AND F.AGMT_NO=B.AGMT_NO "
							+ "AND F.CONTRACT_TYPE=B.CONTRACT_TYPE AND F.CARGO_NO=B.CARGO_NO AND F.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND D.COMPANY_CD=F.COMPANY_CD AND D.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND D.CONTRACT_TYPE=F.CONTRACT_TYPE "
							+ "AND D.CONT_NO=F.CONT_NO AND D.AGMT_TYPE=F.AGMT_TYPE AND D.AGMT_NO=F.AGMT_NO AND D.CARGO_NO=F.CARGO_NO AND D.ALLOC_REV=F.ALLOC_REV "
							+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO "
							+ "AND A.AGMT_NO=E.AGMT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE";
				}
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "M");
				stmt.setString(4, agmt_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, cargo_no);
				stmt.setString(8, boe_no);
				stmt.setString(9, plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					cont_start_dt = rset.getString(9)==null?"":rset.getString(9);
					String cargo_ref=rset.getString(11)==null?"":rset.getString(11);
					
					deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no)+" ["+cargo_ref+"]";
					contRef=cargo_ref;
					
					boe_nm="BOE"+utilBean.PrePaddingZero(boe_no, 2);
					qty_MMBTU=rset.getDouble(15);
					
					price_cd = rset.getString(17)==null?"2":rset.getString(17);
					price = utilBean.RateNumberFormat(rset.getDouble(16), price_cd);
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					
					if(inv_flag.equals("F"))
					{
						double prov_price= rset.getDouble(16);
						double final_price= rset.getDouble(26);
						
						this.final_price= utilBean.RateNumberFormat(final_price, price_cd);
						this.profm_price= utilBean.RateNumberFormat(prov_price, price_cd);
						
						final_qty=nf.format(qty_MMBTU);
						profm_qty=nf.format(qty_MMBTU);
						
						price = utilBean.RateNumberFormat(final_price, price_cd);
						
						diff_price=utilBean.RateNumberFormat((final_price-prov_price), price_cd);
						diff_qty=nf.format(0);
					}
					
					invoice_raised_in = rset.getString(18)==null?"2":rset.getString(18);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					payment_done_in = rset.getString(19)==null?"2":rset.getString(19);
					payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
					due_days = rset.getString(20)==null?"0":rset.getString(20);
					exchng_rate_cd = rset.getString(21)==null?"":rset.getString(21);
	
					//txn_charges = nf.format(rset.getDouble(18));
					consider_due_dt_in=rset.getString(22)==null?"C":rset.getString(22);
					exclude_sat=rset.getString(23)==null?"N":rset.getString(23);
					
					exchng_rate_cal = rset.getString(24)==null?"":rset.getString(24);
					exchang_criteria = rset.getString(25)==null?"":rset.getString(25);
					holiday_state = rset.getString(28)==null?"":rset.getString(28);
				}
				else
				{
					price="0.00";
					price_cd="2";
					invoice_raised_in="2";
					//txn_charges="0.00";
	
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
	
					consider_due_dt_in="C";
					exclude_sat="";
					holiday_state="";
				}
				rset.close();
				stmt.close();
				
				if(inv_flag.equals("CP") || inv_flag.equals("CF"))
				{
					invoice_raised_in="1";
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					
					payment_done_in = "1";
					payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
				}
				
				if(inv_flag.equals("CP"))
				{
					queryString="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT "
							+ "FROM FMS_PUR_SG_INV_MST A "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CARGO_NO=? AND A.BOE_NO=? AND A.INV_FLAG=? ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, cargo_no);
					stmt.setString(7, boe_no);
					stmt.setString(8, "PF");
					rset=stmt.executeQuery();
					if(rset.next())
					{
						qty_MMBTU=rset.getDouble(1);
						price_cd = rset.getString(3)==null?"":rset.getString(3);
						price = utilBean.RateNumberFormat(rset.getDouble(2), price_cd);
						price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					}
					else
					{
						qty_MMBTU=0;
						price="0.00";
						price_cd="2";
					}
					rset.close();
					stmt.close();
				}
				
				if(inv_flag.equals("CF"))
				{
					fetchCargoInvDetailForFinalCustomDuty();
				}
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,"
						+ "C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,C.DUE_DT_IN,C.EXCL_SAT_MAP,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,"
						+ "A.SUG,C.HOLIDAY_STATE "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND C.PLANT_SEQ_NO=? ";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "T");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "L");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				stmt.setString(10, plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					cont_start_dt = rset.getString(7)==null?"":rset.getString(7);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cont_ref=rset.getString(10)==null?"":rset.getString(10);
					
					deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, contno, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
					contRef=cont_ref;
					
					price_cd = rset.getString(13)==null?"2":rset.getString(13);
					price = utilBean.RateNumberFormat(rset.getDouble(12), price_cd);
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
	
					invoice_raised_in = rset.getString(14)==null?"2":rset.getString(14);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					payment_done_in = rset.getString(15)==null?"2":rset.getString(15);
					payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
					due_days = rset.getString(16)==null?"0":rset.getString(16);
					exchng_rate_cd = rset.getString(17)==null?"":rset.getString(17);
	
					consider_due_dt_in=rset.getString(18)==null?"C":rset.getString(18);
					exclude_sat=rset.getString(19)==null?"N":rset.getString(19);
					
					exchng_rate_cal = rset.getString(20)==null?"":rset.getString(20);
					exchang_criteria = rset.getString(21)==null?"":rset.getString(21);
					sug_percentage = rset.getString(22)==null?"":nf.format(rset.getDouble(22));
					holiday_state = rset.getString(23)==null?"":rset.getString(23);
					
					qty_MMBTU=allocUtil.getPurchaseAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, cargo_no,period_start_dt, period_end_dt);
					
					queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "T");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.setString(6, "L");
					stmt1.setString(7, cont_no);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						price_cd = rset1.getString(2)==null?"2":rset1.getString(2);
						price = utilBean.RateNumberFormat(rset1.getDouble(1), price_cd);
						price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
						
						sug_percentage = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					price="0.00";
					price_cd="2";
					invoice_raised_in="2";
					
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
	
					consider_due_dt_in="C";
					exclude_sat="";
					holiday_state="";
				}
				rset.close();
				stmt.close();
				
				if(inv_flag.equals("UG")) //FOR SUG INVOICE WILL BE GENERATED ON INR ONLY
				{
					price="";
					price_cd="1";
					invoice_raised_in="1";
					
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
				}
			}	
			else
			{
				String temp_counterparty_cd="";
				String temp_cont_rev="";
	
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,A.TXN_CHARGE,C.DUE_DT_IN,"
						+ "C.EXCL_SAT_MAP,A.TRADE_REF_NO,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,A.SPLIT_FLAG,C.HOLIDAY_STATE "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? " //AND A.COUNTERPARTY_CD='"+counterparty_cd+"' "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
						+ "AND A.CONT_NO=? " //AND A.CONT_REV='"+cont_rev_no+"' "
						+ "AND A.CONTRACT_TYPE=? "
						//+ "AND A.START_DT<=TO_DATE('"+period_end_dt+"','DD/MM/YYYY') AND A.END_DT>=TO_DATE('"+period_start_dt+"','DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.CONT_NO=C.CONT_NO "//AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? ";
						//+ "AND C.BILLING_FREQ='"+billing_freq+"'";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, agmt_no);
				stmt.setString(3, agmt_rev_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, counterparty_cd);
				stmt.setString(7, plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					temp_counterparty_cd=rset.getString(2)==null?"":rset.getString(2);
					
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					temp_cont_rev=contrev;
					cont_start_dt = rset.getString(7)==null?"":rset.getString(7);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cont_ref=rset.getString(10)==null?"":rset.getString(10);
					String tradeRef=rset.getString(21)==null?"":rset.getString(21);
					
					if(cont_type.equals("I"))
					{
						contRef=tradeRef;
					}
	
					//deal_no=cont_type+""+contno+" ["+contRef+"]";
	
					deal_no=utilBean.NewDealMappingId(comp_cd, temp_counterparty_cd, agmt_no, agmt_rev_no, contno, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
					contRef=cont_ref;
					
					price_cd = rset.getString(13)==null?"2":rset.getString(13);
					price = utilBean.RateNumberFormat(rset.getDouble(12), price_cd);
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
	
					invoice_raised_in = rset.getString(14)==null?"2":rset.getString(14);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					payment_done_in = rset.getString(15)==null?"2":rset.getString(15);
					payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
					due_days = rset.getString(16)==null?"0":rset.getString(16);
					exchng_rate_cd = rset.getString(17)==null?"":rset.getString(17);
	
					txn_charges = nf.format(rset.getDouble(18));
					consider_due_dt_in=rset.getString(19)==null?"C":rset.getString(19);
					exclude_sat=rset.getString(20)==null?"N":rset.getString(20);
					
					exchng_rate_cal = rset.getString(22)==null?"":rset.getString(22);
					exchang_criteria = rset.getString(23)==null?"":rset.getString(23);
					
					cont_split_flag = rset.getString(24)==null?"":rset.getString(24);
					holiday_state = rset.getString(25)==null?"":rset.getString(25);
				}
				else
				{
					price="0.00";
					price_cd="2";
					invoice_raised_in="2";
					txn_charges="0.00";
	
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
	
					consider_due_dt_in="C";
					exclude_sat="";
					holiday_state="";
				}
				rset.close();
				stmt.close();
				
				
				if(cont_split_flag.equals("Y"))
				{
					String split_value="";
					if(temp_counterparty_cd.equals(counterparty_cd))
					{
						queryString1="SELECT SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
								+ "AND PLANT_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, temp_cont_rev);
						stmt1.setString(5, agmt_no);
						stmt1.setString(6, agmt_rev_no);
						stmt1.setString(7, contract_type);
						stmt1.setString(8, plant_seq);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							split_value = rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						queryString2="SELECT SPLIT_VALUE "
								+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
								+ "AND PLANT_SEQ_NO=? AND SPLIT_TRADER_CD=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, temp_counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, temp_cont_rev);
						stmt2.setString(5, agmt_no);
						stmt2.setString(6, agmt_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, plant_seq);
						stmt2.setString(9, counterparty_cd);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							split_value = rset2.getString(1)==null?"":rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
					}
					
					qty_MMBTU=allocUtil.getPurchaseAllocationQty(conn, comp_cd, temp_counterparty_cd, agmt_no, cont_no, contract_type, bu_unit, period_start_dt, period_end_dt,"0");
					if(qty_MMBTU>0 && !split_value.equals(""))
					{
						qty_MMBTU=(qty_MMBTU * (Double.parseDouble(""+split_value) / 100));
					}
					else
					{
						qty_MMBTU=0;
					}
				}
				else
				{
					qty_MMBTU=allocUtil.getPurchaseAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, "0",period_start_dt, period_end_dt);
				}
				
				queryString1="SELECT CHARGE_RATE,CHARGE_ABBR "
						+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
						+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
						+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev_no);
				stmt1.setString(5, cont_no);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, plant_seq);
				stmt1.setString(8, period_end_dt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String charge_abbr=rset1.getString(2)==null?"":rset1.getString(2);
					if(charge_abbr.equals("TC"))
					{
						transportation_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
					else if(charge_abbr.equals("OC"))
					{
						other_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
					else if(charge_abbr.equals("MM"))
					{
						marketing_margin=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void fetchCargoInvDetailForFinalCustomDuty()
	{
		String function_nm="fetchingCargoInvDetailForFinalCustomDuty()";
		try
		{
			double profm_alloc_qty=0;
			double profm_price=0;
			String profm_price_unit="";
			double profm_inv_amt=0;
			
			double prov_alloc_qty=0;
			double prov_price=0;
			String prov_price_unit="";
			double prov_inv_amt=0;
			
			double final_alloc_qty=0;
			double final_price=0;
			String final_price_unit="";
			double final_inv_amt=0;
			int final_isSubmitted=0;
			
			double diff_alloc_qty=0;
			double diffPrice=0;
			double diff_invAmt=0;
				
			queryString="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,INVOICE_AMT,INV_FLAG,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE "
					+ "FROM FMS_PUR_SG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CARGO_NO=? AND A.BOE_NO=? AND A.PDF_INV_DTL IS NOT NULL AND A.INV_FLAG IN ('CP','P','F') ";
			queryString+=" UNION ALL ";
			queryString+="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,INVOICE_AMT,INV_FLAG,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE "
					+ "FROM FMS_PUR_PG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CARGO_NO=? AND A.BOE_NO=? AND A.PDF_INV_DTL IS NOT NULL AND A.INV_FLAG IN ('CP','P','F') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, cargo_no);
			stmt.setString(7, boe_no);
			stmt.setString(8, comp_cd);
			stmt.setString(9, counterparty_cd);
			stmt.setString(10, agmt_no);
			stmt.setString(11, cont_no);
			stmt.setString(12, contract_type);
			stmt.setString(13, cargo_no);
			stmt.setString(14, boe_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String invFlag=rset.getString(5)==null?"":rset.getString(5);
				
				if(invFlag.equals("CP"))
				{
					profm_alloc_qty=rset.getDouble(1);
					profm_price=rset.getDouble(2);
					profm_price_unit=rset.getString(3)==null?"":rset.getString(3);
					profm_inv_amt=rset.getDouble(4);
					
					exchng_rate_cd=rset.getString(6)==null?"":rset.getString(6);
					exchang_rate_dt=rset.getString(7)==null?"":rset.getString(7);
					exchang_rate=rset.getString(8)==null?"":nf2.format(rset.getDouble(8));
				}
				else if(invFlag.equals("P"))
				{
					prov_alloc_qty=rset.getDouble(1);
					prov_price=rset.getDouble(2);
					prov_price_unit=rset.getString(3)==null?"":rset.getString(3);
					prov_inv_amt=rset.getDouble(4);
				}
				else if(invFlag.equals("F"))
				{
					final_alloc_qty=rset.getDouble(1);
					final_price=rset.getDouble(2);
					final_price_unit=rset.getString(3)==null?"":rset.getString(3);
					final_inv_amt=rset.getDouble(4);
					final_isSubmitted=1;
				}
			}
			rset.close();
			stmt.close();
			
			profm_qty=nf.format(profm_alloc_qty);
			this.profm_price=utilBean.RateNumberFormat(profm_price, profm_price_unit);
			this.profm_inv_amt=nf.format(profm_inv_amt);
			
			if(final_isSubmitted==0)
			{
				final_alloc_qty=prov_alloc_qty;
				final_price=prov_price;
				final_price_unit=prov_price_unit;
				final_inv_amt=prov_inv_amt;
			}
			
			final_qty=nf.format(final_alloc_qty);
			this.final_price=utilBean.RateNumberFormat(final_price, final_price_unit);
			//this.final_inv_amt=nf.format(prov_inv_amt+final_inv_amt);
			
			qty_MMBTU=final_alloc_qty;
			price_cd = final_price_unit;
			price = utilBean.RateNumberFormat(final_price, price_cd);
			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			
			diff_alloc_qty=final_alloc_qty-profm_alloc_qty;
			diffPrice= (final_price-profm_price);
			
			diff_qty=nf.format(diff_alloc_qty);
			diff_price=utilBean.RateNumberFormat(diffPrice, prov_price_unit);
			//diff_inv_amt=nf.format(diff_invAmt);
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getBuUnit()
	{
		String function_nm="getBuUnit()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_BU "
					+ "WHERE COMPANY_CD=? "//AND COUNTERPARTY_CD='"+counterparty_cd+"' " //REMOVED FOR SPLIT CONTRACT SUPPORT
					+ "AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			queryString+="UNION ";
			queryString+="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			queryString+="UNION ";
			queryString+="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_CARGO_SVC_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, cont_no);
			stmt.setString(3, cont_rev_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, comp_cd);
			stmt.setString(8, counterparty_cd);
			stmt.setString(9, "T");
			stmt.setString(10, "L");
			stmt.setString(11, cont_no);
			stmt.setString(12, cont_rev_no);
			stmt.setString(13, agmt_no);
			stmt.setString(14, agmt_rev_no);
			stmt.setString(15, contract_type);
			stmt.setString(16, comp_cd);
			stmt.setString(17, counterparty_cd);
			stmt.setString(18, entity);
			stmt.setString(19, cont_no);
			stmt.setString(20, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String buCd = rset.getString(1)==null?"":rset.getString(1);
				String bu_plant_seq = rset.getString(2)==null?"":rset.getString(2);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");

				VSEL_BU_CD.add(buCd);
				VSEL_BU_PLANT_SEQ_NO.add(bu_plant_seq);
				VSEL_BU_PLANT_ABBR.add(bu_plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getContactPerson()
	{
		String function_nm="getContactPerson()";
		try
		{
			queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "T");
			stmt.setString(4, "P"+plant_seq);
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCONTACT_PERSON.add(rset.getString(1)==null?"":rset.getString(1));
				VCONTACT_PERSON_CD.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getOtherInvoiceContactPerson()
	{
		String function_nm="getOtherInvoiceContactPerson()";
		try
		{
			queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			//stmt.setString(3, "T");
			stmt.setString(3, entity);
			stmt.setString(4, address_type);
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCONTACT_PERSON.add(rset.getString(1)==null?"":rset.getString(1));
				VCONTACT_PERSON_CD.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getBuContactPerson()
	{
		String function_nm="getBuContactPerson()";
		try
		{
			queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, comp_cd);
			stmt.setString(3, "B");
			stmt.setString(4, "P"+bu_unit);
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VBU_CONTACT_PERSON.add(rset.getString(1)==null?"":rset.getString(1));
				VBU_CONTACT_PERSON_CD.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void InvoiceCalculation()
	{
		String function_nm="InvoiceCalculation()";
		try
		{
			int count=0;
			if(!refresh_flg.equals("Y"))
			{
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
				if(!inv_flag.equals("UG")) {
					queryString+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(contract_type.equals("N")) {
					queryString+="AND CARGO_NO=? AND BOE_NO=? ";
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					queryString+="AND CARGO_NO=? ";
				}
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, counterparty_cd);
				stmt.setString(++stcount, cont_no);
				stmt.setString(++stcount, agmt_no);
				stmt.setString(++stcount, plant_seq);
				stmt.setString(++stcount, bu_unit);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, inv_flag);
				if(!inv_flag.equals("UG")) {
					stmt.setString(++stcount, billing_cycle);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
				}
				if(contract_type.equals("N")) {
					stmt.setString(++stcount, cargo_no);
					stmt.setString(++stcount, boe_no);
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					stmt.setString(++stcount, cargo_no);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			
			double tdsGrossAmt=0;
			double exchngRate=0;
			if(count>0)
			{
				queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
						+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
						+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,"
						+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),SYS_INV_NO,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
						+ "SUG_QTY,SUG_PERCENT,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
				if(!inv_flag.equals("UG")) {
					queryString+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(contract_type.equals("N")) {
					queryString+="AND CARGO_NO=? AND BOE_NO=? ";
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					queryString+="AND CARGO_NO=? ";
				}	
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, counterparty_cd);
				stmt.setString(++stcount, cont_no);
				stmt.setString(++stcount, agmt_no);
				stmt.setString(++stcount, plant_seq);
				stmt.setString(++stcount, bu_unit);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, inv_flag);
				if(!inv_flag.equals("UG")) {
					stmt.setString(++stcount, billing_cycle);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
				}
				if(contract_type.equals("N")) {
					stmt.setString(++stcount, cargo_no);
					stmt.setString(++stcount, boe_no);
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					stmt.setString(++stcount, cargo_no);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					sg_submission_chk=true;

					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
					invoice_seq=rset.getString(3)==null?"":rset.getString(3);
					invoice_no=rset.getString(4)==null?"":rset.getString(4);
					invoice_dt=rset.getString(5)==null?"":rset.getString(5);
					invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7)); //
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));//
					exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
					exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));//
					if(!exchang_rate.equals(""))
					{
						exchngRate=Double.parseDouble(exchang_rate);
					}
					invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
					tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));//
					tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
					tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
					invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));//
					net_payable=rset.getString(22)==null?"":nf.format(rset.getDouble(22));//
					invoice_check_flag=rset.getString(23)==null?"":rset.getString(23);
					String chk_by=rset.getString(24)==null?"":rset.getString(24);
					invoice_check_by=utilBean.getEmpName(conn,chk_by);
					invoice_check_dt=rset.getString(25)==null?"":rset.getString(25);
					if(invoice_check_flag.equals("Y"))
					{
						invoice_check_nm="Checked";
					}
					else if(invoice_check_flag.equals("N"))
					{
						invoice_check_nm="Rejected";
					}
					invoice_auth_flag=rset.getString(26)==null?"":rset.getString(26);
					String auth_by=rset.getString(27)==null?"":rset.getString(27);
					invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					invoice_auth_dt=rset.getString(28)==null?"":rset.getString(28);
					if(invoice_auth_flag.equals("Y"))
					{
						invoice_auth_nm="Authorized";
					}
					else if(invoice_auth_flag.equals("N"))
					{
						invoice_auth_nm="Rejected";
					}

					invoice_aprv_flag=rset.getString(29)==null?"":rset.getString(29);
					String aprv_by=rset.getString(30)==null?"":rset.getString(30);
					invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					invoice_aprv_dt=rset.getString(31)==null?"":rset.getString(31);
					if(invoice_aprv_flag.equals("A"))
					{
						invoice_aprv_nm="Approved";
					}
					else if(invoice_aprv_flag.equals("R"))
					{
						invoice_aprv_nm="Rejected";
					}
					
					sg_rem_gen_status=""+getInvoiceStatus(invoice_check_flag, invoice_auth_flag, invoice_aprv_flag);

					invoice_adj_sign=rset.getString(32)==null?"":rset.getString(32);
					invoice_adj_amt=rset.getString(33)==null?"":nf.format(rset.getDouble(33));//
					financial_year=rset.getString(34)==null?"":rset.getString(34);
					submitted_fiscal_yr=financial_year;
					
					tcs_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
					tcs_factor=rset.getString(36)==null?"":nf3.format(rset.getDouble(36));
					tcs_struct_cd=rset.getString(37)==null?"":rset.getString(37);
					tcs_struct_dt=rset.getString(38)==null?"":rset.getString(38);
					applicable_flag=rset.getString(39)==null?"":rset.getString(39);
					applicable_abbr=rset.getString(40)==null?"":rset.getString(40);
					
					tds_amount=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
					tds_factor=rset.getString(42)==null?"":nf3.format(rset.getDouble(42));
					tds_struct_cd=rset.getString(43)==null?"":rset.getString(43);
					tds_struct_dt=rset.getString(44)==null?"":rset.getString(44);
					
					sys_invoice_no=rset.getString(45)==null?"":rset.getString(45);
					
					cif_value=rset.getString(46)==null?"":nf.format(rset.getDouble(46));
					assessable_vlaue=rset.getString(47)==null?"":nf.format(rset.getDouble(47));
					remark=rset.getString(48)==null?"":rset.getString(48);
					diff_price=utilBean.RateNumberFormat(rset.getDouble(49), price_cd);
					cd_paid_amt=rset.getString(50)==null?"":nf.format(rset.getDouble(50));
					
					sug_qty=rset.getString(51)==null?"":nf.format(rset.getDouble(51));
					sug_percentage=rset.getString(52)==null?"":nf.format(rset.getDouble(52));
					
					energy_unit=rset.getString(53)==null?"1":rset.getString(53);
					energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
					
					transportation_charges=rset.getString(54)==null?"":nf2.format(rset.getDouble(54));
					transportation_amount=rset.getString(55)==null?"":nf.format(rset.getDouble(55));
					marketing_margin=rset.getString(56)==null?"":nf2.format(rset.getDouble(56));
					marketing_margin_amount=rset.getString(57)==null?"":nf.format(rset.getDouble(57));
					other_charges=rset.getString(58)==null?"":nf2.format(rset.getDouble(58));
					other_charges_amount=rset.getString(59)==null?"":nf.format(rset.getDouble(59));
					
					if(price_cd.equals(invoice_raised_in))
					{
						if(invoice_raised_in.equals("2"))
						{
							tdsGrossAmt= Double.parseDouble(gross_amt1) * exchngRate;
						}
						else
						{
							tdsGrossAmt= Double.parseDouble(gross_amt1);
						}
					}
					else
					{
						
						tdsGrossAmt= Double.parseDouble(gross_amt1);
					}
					
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
					if(!transportation_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
						tdsGrossAmt+=Double.parseDouble(transportation_amount);
					}
					if(!marketing_margin_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
						tdsGrossAmt+=Double.parseDouble(marketing_margin_amount);
					}
					if(!other_charges_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
						tdsGrossAmt+=Double.parseDouble(other_charges_amount);
					}
					
					if(contract_type.equals("N") && (inv_flag.equals("PF") || inv_flag.equals("P") || inv_flag.equals("F")))
					{
						
					}
					else if(inv_flag.equals("UG"))
					{
						//tax_struct_dtl=utilBean.getEntityServiceStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt,"UG");
						tax_struct_dtl=utilBean.getTaxDescr(conn,tax_struct_cd);
					}
					else
					{
						Vector temp = new Vector();
						if(inv_flag.equals("CP") || inv_flag.equals("CF"))
						{
							temp=TaxCalc.CustomTaxAmountCalculationWithInfo(conn,comp_cd, period_start_dt, gross_amt1);
							//tax_struct_dtl=utilBean.getCustomTaxStructureDtl(conn,comp_cd,period_start_dt);
						}
						else if(contract_type.equals("G") || contract_type.equals("P"))
						{
							//temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt, "SI", gross_amt1);
							temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt, "SI", gross_include_transport_tariff);
							//tax_struct_dtl=utilBean.getEntityServiceStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt,"SI");
						}
						else if(!contract_type.equals("N"))
						{
							//temp=TaxCalc.TaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit,period_start_dt, gross_amt1);
							temp=TaxCalc.TaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit,period_start_dt, gross_include_transport_tariff);
							//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt);
						}
						
						tax_struct_dtl=utilBean.getTaxDescr(conn,tax_struct_cd);
						
						if(!invoice_check_flag.equals("Y"))
						{
							tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
							tax_struct_cd = ""+temp.elementAt(1);
							tax_struct_dt = ""+temp.elementAt(2);
							tax_info = ""+temp.elementAt(3);
							//tax_struct_dtl = ""+temp.elementAt(4);
							tax_factor = ""+temp.elementAt(5);
						}
					}
					
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					Vector VTAX_BASE_AMT = new Vector();
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_PUR_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, financial_year);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, inv_flag);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
						VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					}
					rset1.close();
					stmt1.close();
					
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
					
					VSG_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					
				}
				rset.close();
				stmt.close();
			}
			else
			{
				sg_submission_chk=false;

				/*queryString="SELECT SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_RM=? AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "T");
				stmt.setString(4, "P"+plant_seq);
				stmt.setString(5, "Y");
				stmt.setString(6, "Y");
				stmt.setString(7, "Y");
				stmt.setString(8, "Y");
				stmt.setString(9, "RLNG");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				}
				rset.close();
				stmt.close();*/
				
				String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,"T","P"+plant_seq, "RM", "RLNG","Y");
				contact_person_cd=temp_contact_person_cd.equals("")?"0":temp_contact_person_cd;

				/*queryString1="SELECT SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_RM=? AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, "B");
				stmt1.setString(4, "P"+bu_unit);
				stmt1.setString(5, "Y");
				stmt1.setString(6, "Y");
				stmt1.setString(7, "Y");
				stmt1.setString(8, "Y");
				stmt1.setString(9, "RLNG");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();*/
				
				String temp_bu_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,comp_cd,"B","P"+bu_unit, "RM", "RLNG","Y");
				bu_contact_person_cd=temp_bu_contact_person_cd.equals("")?"0":temp_bu_contact_person_cd;

				if(invoice_dt.equals(""))
				{
					invoice_dt=period_end_dt;
				}
				
				if(invoice_due_dt.equals(""))
				{
					//invoice_due_dt=""+utilBean.DueDateCalculation(conn,invoice_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,state_code);
					invoice_due_dt=""+utilBean.DueDateCalculation(conn,invoice_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state);
				}

				//financial_year=utilDate.getFinancialYear(period_end_dt);
				financial_year=utilDate.getFinancialYear(invoice_dt);
				submitted_fiscal_yr=financial_year;
				
				/*String fin_yr="";
				if(!financial_year.equals(""))
				{
					String[] temp = financial_year.split("-");
					fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
				}*/
				
				if(!refresh_flg.equals("Y"))
				{
					/*String inv_seq="1";
					queryString4="SELECT MAX(INVOICE_SEQ) "
							+ "FROM FMS_PUR_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							//+ "AND COUNTERPARTY_CD='"+counterparty_cd+"' AND CONT_NO='"+cont_no+"' AND AGMT_NO='"+agmt_no+"' "
							+ "AND CONTRACT_TYPE=? AND INV_FLAG=? ";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, financial_year);
					stmt4.setString(3, contract_type);
					stmt4.setString(4, inv_flag);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						inv_seq = ""+(rset4.getInt(1)+1);
					}
					rset4.close();
					stmt4.close();
	
					invoice_seq = inv_seq;*/
				}
				else
				{
					queryString4="SELECT INVOICE_SEQ,INVOICE_NO,FINANCIAL_YEAR,SYS_INV_NO "
							+ "FROM FMS_PUR_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
					if(!inv_flag.equals("UG")) {		
						queryString4+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  ";
					}
					if(contract_type.equals("N")) {
						queryString4+="AND CARGO_NO=? AND BOE_NO=? ";
					}else if(contract_type.equals("G") || contract_type.equals("P")) {
						queryString4+="AND CARGO_NO=? ";
					}
					int stcount=0;
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(++stcount, comp_cd);
					stmt4.setString(++stcount, counterparty_cd);
					stmt4.setString(++stcount, cont_no);
					stmt4.setString(++stcount, agmt_no);
					stmt4.setString(++stcount, plant_seq);
					stmt4.setString(++stcount, bu_unit);
					stmt4.setString(++stcount, contract_type);
					stmt4.setString(++stcount, inv_flag);
					if(!inv_flag.equals("UG")) {
						stmt4.setString(++stcount, billing_cycle);
						stmt4.setString(++stcount, period_start_dt);
						stmt4.setString(++stcount, period_end_dt);
					}
					if(contract_type.equals("N")) {
						stmt4.setString(++stcount, cargo_no);
						stmt4.setString(++stcount, boe_no);
					}else if(contract_type.equals("G") || contract_type.equals("P")) {
						stmt4.setString(++stcount, cargo_no);
					}
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						invoice_seq=rset4.getString(1)==null?"":rset4.getString(1);
						invoice_no=rset4.getString(2)==null?"":rset4.getString(2);
						
						submitted_fiscal_yr=rset4.getString(3)==null?"":rset4.getString(3);
						sys_invoice_no=rset4.getString(4)==null?"":rset4.getString(4);
					}
					rset4.close();
					stmt4.close();
				}
				
				/*
				if(!invoice_seq.equals("") && !contract_type.equals(""))
				{
					String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
				
					if(contract_type.equals("N") && !cargo_no.equals(""))
					{
						if(inv_flag.equals("P") || inv_flag.equals("PF"))
						{
							sys_invoice_no=invoice_prefix+""+inv_flag+""+contract_type+"S"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
						else if(inv_flag.equals("F"))
						{
							sys_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
						else if(inv_flag.equals("CF"))
						{
							sys_invoice_no=invoice_prefix+""+contract_type+"CD"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
						else if(inv_flag.equals("CP"))
						{
							sys_invoice_no=invoice_prefix+"P"+contract_type+"CD"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
					}
					else if(contract_type.equals("G") || contract_type.equals("P"))
					{
						if(inv_flag.equals("UG"))
						{
							sys_invoice_no=invoice_prefix+""+contract_type+"LUG"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
						else
						{
							sys_invoice_no=invoice_prefix+""+contract_type+"L"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
						}
					}
					else
					{
						sys_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;	
					}
				}
				*/
				if(contract_type.equals("N") && !cargo_no.equals(""))
				{
					if(inv_flag.equals("CP") || inv_flag.equals("CF"))
					{
						if(qty_MMBTU>0)
						{
							qty_mmbtu=nf.format(qty_MMBTU);
							
							double exchng_rate=0;
							double tax=0;
							if(inv_flag.equals("CP"))
							{
								queryString6="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY'),EXCHG_RATE_CD "
										+ "FROM FMS_EXCHG_RATE_ENTRY A "
										+ "WHERE EXCHG_RATE_CD=(SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST B "
										+ "WHERE A.EXCHG_RATE_CD=B.EXC_RATE_CD AND UPPER(B.EXC_RATE_NM)=?) "
										+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
										+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt6=conn.prepareStatement(queryString6);
								stmt6.setString(1, "CUSTOM EXCHANGE RATE");
								stmt6.setString(2, invoice_dt);
								rset6=stmt6.executeQuery();
								if(rset6.next())
								{
									exchng_rate=rset6.getDouble(1);
									exchang_rate_dt=rset6.getString(2)==null?"":rset6.getString(2);
									exchng_rate_cd=rset6.getString(3)==null?"":rset6.getString(3);
								}
								rset6.close();
								stmt6.close();
							
								exchang_rate=nf2.format(exchng_rate);
								exchngRate=Double.parseDouble(exchang_rate);
							}
							else
							{
								if(!exchang_rate.equals(""))
								{
									exchng_rate=Double.parseDouble(exchang_rate);
									exchngRate=exchng_rate;
								}
							}
							
							double temp_gross_amt=qty_MMBTU * Double.parseDouble(price);
							gross_amt = nf.format(temp_gross_amt);
							
							if(price_cd.equals(invoice_raised_in))
							{
								gross_amt1 = gross_amt;
							}
							else
							{
								if(price_cd.equals("2"))
								{
									gross_amt1 = nf.format(temp_gross_amt * exchng_rate);
								}
								else
								{
									gross_amt1 = gross_amt;
								}
							}
							
							cif_value=gross_amt1;
							assessable_vlaue=gross_amt1;
							
							Vector temp = new Vector();
							temp=TaxCalc.CustomTaxAmountCalculationWithInfo(conn,comp_cd, period_start_dt, gross_amt1);
							tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
							tax_struct_cd = ""+temp.elementAt(1);
							tax_struct_dt = ""+temp.elementAt(2);
							tax_info = ""+temp.elementAt(3);
							tax_struct_dtl = ""+temp.elementAt(4);
							tax_factor = ""+temp.elementAt(5);
							VSG_MULTI_TAX_STRUCT.add(temp.elementAt(6));
							
							if(tax_struct_cd.equals(""))
							{
								tax_amt="";
								//invoice_amt = nf.format(Double.parseDouble(gross_amt1));
							}
							else
							{
								//invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
							}
							
							if(inv_flag.equals("CF"))
							{
								cd_paid_amt=profm_inv_amt;
								invoice_amt=nf.format(Double.parseDouble(tax_amt) - Double.parseDouble(cd_paid_amt));
								
							}
							else
							{
								invoice_amt=tax_amt;
							}
							applicable_flag="N";
							applicable_abbr="NA";
							
							net_payable=invoice_amt;
						}
					}
					else
					{
						if(qty_MMBTU>0)
						{
							qty_mmbtu=nf.format(qty_MMBTU);
							
							double exchng_rate=0;
							double tax=0;
							
							//exchang_rate=nf2.format(exchng_rate);
							exchang_rate="";
							
							double temp_gross_amt=qty_MMBTU * Double.parseDouble(price);
							if(inv_flag.equals("F"))
							{
								temp_gross_amt=qty_MMBTU * Double.parseDouble(diff_price);
							}
							gross_amt = nf.format(temp_gross_amt);
							gross_amt1 = gross_amt;
							
							tax_amt = "";
							tax_struct_cd = "";
							tax_struct_dt = "";
							tax_info = "";
							tax_struct_dtl = "";
							tax_factor = "";
							//VSG_MULTI_TAX_STRUCT.add(new Vector());
							
							invoice_amt = nf.format(Double.parseDouble(gross_amt1));
							
							applicable_flag="N";
							applicable_abbr="NA";
							
							net_payable=invoice_amt;
						}
					}
				}
				else if(inv_flag.equals("UG"))
				{
					qty_MMBTU=0;
					if(!allocQty.equals(""))
					{
						qty_MMBTU=Double.parseDouble(allocQty);
						qty_mmbtu=nf.format(qty_MMBTU);
					}
					if(!temp_price.equals(""))
					{
						price=utilBean.RateNumberFormat(Double.parseDouble(temp_price), price_cd);
					}
					
					if(qty_MMBTU>0 && !sug_percentage.equals("") && !price.equals(""))
					{
						sug_qty=nf.format(qty_MMBTU*Double.parseDouble(sug_percentage)/100);
						
						double temp_gross_amt=Double.parseDouble(sug_qty) * Double.parseDouble(price);
						gross_amt = nf.format(Math.round(temp_gross_amt));
						gross_amt1 = gross_amt;
					}
					else
					{
						gross_amt1="0.00";
					}
					
					Vector temp = new Vector();
					temp=TaxCalc.ServiceTaxAmountCalculationInRupeesWithInfo(conn, comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt, "UG", gross_amt1);
					tax_amt = nf.format(Math.round(Double.parseDouble(""+temp.elementAt(0))));
					tax_struct_cd = ""+temp.elementAt(1);
					tax_struct_dt = ""+temp.elementAt(2);
					tax_info = ""+temp.elementAt(3);
					tax_struct_dtl = ""+temp.elementAt(4);
					tax_factor = ""+temp.elementAt(5);
					VSG_MULTI_TAX_STRUCT.add(temp.elementAt(6));
					
					if(tax_struct_cd.equals(""))
					{
						tax_amt="";
					}
					
					invoice_amt = tax_amt;
					net_payable = invoice_amt;
				}
				else
				{
					//GET PURCHASE PRICE
					String new_price="";
					queryString5 = "SELECT DISTINCT NEW_PRICE "
							+ "FROM FMS_TRADER_CONT_PRICE_DTL A "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
							+ "AND SEQ_NO = (SELECT MAX(SEQ_NO) FROM FMS_TRADER_CONT_PRICE_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND B.EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, agmt_no);
					stmt5.setString(3, cont_no);
					stmt5.setString(4, counterparty_cd);
					stmt5.setString(5, "Y");
					stmt5.setString(6, contract_type);
					stmt5.setString(7, period_end_dt);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						new_price=rset5.getString(1)==null?"":rset5.getString(1);
						if(!new_price.equals(""))
						{
							price=utilBean.RateNumberFormat(rset5.getDouble(1), price_cd);
						}
					}
					rset5.close();
					stmt5.close();
	
					//if(Double.parseDouble(qty_mmbtu) > 0)
					if(qty_MMBTU>0)
					{
						qty_mmbtu=nf.format(qty_MMBTU);
						
						double exchng_rate=0;
						double tax=0;
						
						String exchngEffDt="";
						if(exchng_rate_cal.equals("D"))
						{
							if(exchang_criteria.equals("INV"))
							{
								exchngEffDt=invoice_dt;
							}
							else if(exchang_criteria.equals("LST"))
							{
								exchngEffDt=period_end_dt;
							}
							else if(exchang_criteria.equals("PRE"))
							{
								exchngEffDt=utilDate.getDate(period_end_dt, "-1");
							}
							else if(exchang_criteria.equals("DUE"))
							{
								exchngEffDt=invoice_due_dt;
							}
						}
						else
						{
							exchngEffDt=invoice_dt;
						}
	
						queryString6="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
								+ "FROM FMS_EXCHG_RATE_ENTRY A "
								+ "WHERE EXCHG_RATE_CD=? "
								+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
								+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, exchng_rate_cd);
						stmt6.setString(2, exchngEffDt);
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							exchng_rate=rset6.getDouble(1);
							exchang_rate_dt=rset6.getString(2)==null?"":rset6.getString(2);
						}
						rset6.close();
						stmt6.close();
						
						exchang_rate=nf2.format(exchng_rate);
						exchngRate=Double.parseDouble(exchang_rate);
						/*
						gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
	
						if(price_cd.equals("2"))
						{
							gross_amt1 = nf.format(Double.parseDouble(gross_amt) * exchng_rate);
						}
						else
						{
							gross_amt1 = gross_amt;
						}
						*/
						double tds_gross_amt=0;
						if(price_cd.equals(invoice_raised_in))
						{
							double temp_gross_amt=qty_MMBTU * Double.parseDouble(price);
							//gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
							gross_amt = nf.format(temp_gross_amt);
							gross_amt1 = gross_amt;
							
							if(invoice_raised_in.equals("2"))
							{
								tds_gross_amt= temp_gross_amt * exchng_rate;
							}
							else
							{
								tds_gross_amt= Double.parseDouble(gross_amt1);
							}
						}
						else
						{
							double temp_gross_amt=qty_MMBTU * Double.parseDouble(price);
							//gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
							gross_amt = nf.format(temp_gross_amt);
	
							if(price_cd.equals("2"))
							{
								gross_amt1 = nf.format(temp_gross_amt * exchng_rate);
							}
							else
							{
								gross_amt1 = gross_amt;
							}
							
							tds_gross_amt= Double.parseDouble(gross_amt1);
						}
	
						Vector temp = new Vector();
						if(contract_type.equals("G") || contract_type.equals("P"))
						{
							gross_include_transport_tariff=gross_amt1;
							temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt, "SI", gross_amt1);
						}
						else
						{
							gross_include_transport_tariff=gross_amt1;
							if(!transportation_charges.equals(""))
							{
								transportation_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(transportation_charges));
								tds_gross_amt+=Double.parseDouble(transportation_amount);
								if(invoice_raised_in.equals("2"))
								{
									transportation_amount=nf.format(Double.parseDouble(transportation_amount) / exchng_rate);
								}
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
								isGrossIncTriff=true;
							}
							
							if(!marketing_margin.equals(""))
							{
								marketing_margin_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(marketing_margin));
								tds_gross_amt+=Double.parseDouble(marketing_margin_amount);
								if(invoice_raised_in.equals("2"))
								{
									marketing_margin_amount=nf.format(Double.parseDouble(marketing_margin_amount) / exchng_rate);
								}
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
								isGrossIncTriff=true;
							}
							
							if(!other_charges.equals(""))
							{
								other_charges_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(other_charges));
								tds_gross_amt+=Double.parseDouble(other_charges_amount);
								if(invoice_raised_in.equals("2"))
								{
									other_charges_amount=nf.format(Double.parseDouble(other_charges_amount) / exchng_rate);
								}
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
								isGrossIncTriff=true;
							}
						
							//temp=TaxCalc.TaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit,period_start_dt, gross_amt1);
							temp=TaxCalc.TaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit,period_start_dt, gross_include_transport_tariff);
						}
						tdsGrossAmt=tds_gross_amt;
						tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
						tax_struct_cd = ""+temp.elementAt(1);
						tax_struct_dt = ""+temp.elementAt(2);
						tax_info = ""+temp.elementAt(3);
						tax_struct_dtl = ""+temp.elementAt(4);
						tax_factor = ""+temp.elementAt(5);
						VSG_MULTI_TAX_STRUCT.add(temp.elementAt(6));
						
						if(tax_struct_cd.equals(""))
						{
							tax_amt="";
							//invoice_amt = nf.format(Double.parseDouble(gross_amt1));
							invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff));
						}
						else
						{
							//invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
							invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(tax_amt));
						}
						
						if(contract_type.equals("G") || contract_type.equals("P"))
						{
							applicable_flag="N";
							applicable_abbr="TDS";
							
							//ReGas Invoice TDS will be picked up
							Vector temp_tcs = new Vector();
							//temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "SI","S",period_end_dt, gross_amt1);
							temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "SI","S",period_end_dt, gross_include_transport_tariff);

							tds_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
							tds_struct_cd = ""+temp_tcs.elementAt(1);
							tds_struct_dt = ""+temp_tcs.elementAt(2);
							//tax_info = ""+temp_tcs.elementAt(3);
							//tax_struct_dtl = ""+temp_tcs.elementAt(4);
							tds_factor = ""+temp_tcs.elementAt(5);
							
							net_payable=invoice_amt;
						}
						else
						{
							String TurnoverFlag="";
							queryString4="SELECT TURNOVER_FLAG "
									+ "FROM FMS_ENTITY_TURNOVER_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, "T");
							stmt4.setString(4, financial_year);
							stmt4.setString(5, "1");
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								TurnoverFlag=rset4.getString(1)==null?"":rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							if(TurnoverFlag.equals("Y"))
							{
								applicable_abbr="TCS";
								applicable_flag="Y";
								
								Vector temp_tcs = new Vector();
								temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "T", "TCS", "S","P",period_end_dt, invoice_amt);
		
								tcs_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
								tcs_struct_cd = ""+temp_tcs.elementAt(1);
								tcs_struct_dt = ""+temp_tcs.elementAt(2);
								//tax_info = ""+temp_tcs.elementAt(3);
								//tax_struct_dtl = ""+temp_tcs.elementAt(4);
								tcs_factor = ""+temp_tcs.elementAt(5);
								
								if(!tcs_factor.equals("") && !invoice_amt.equals(""))
								{
									net_payable=nf.format(Double.parseDouble(invoice_amt) + Double.parseDouble(tcs_amount));
									if(Double.doubleToRawLongBits(Double.parseDouble(tcs_amount))==Double.doubleToRawLongBits(0))
									{
										tcs_amount="";
									}
								}
								else
								{
									net_payable=invoice_amt;
								}
							}
							else
							{
								if(!contract_type.equals("I")) //AS DISCUSSED WITH VIJAY ON 20230725, TDS IS NOT APPLICABLE FOR IGX INVOICE
								{
									String total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, financial_year);
									
									applicable_flag="N";
									if(Double.parseDouble(total_transaction_amt)>transaction_limit)
									{
										applicable_abbr="TDS";
										
										Vector temp_tcs = new Vector();
										//temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, gross_amt1);
										temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, gross_include_transport_tariff);
			
										tds_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
										tds_struct_cd = ""+temp_tcs.elementAt(1);
										tds_struct_dt = ""+temp_tcs.elementAt(2);
										//tax_info = ""+temp_tcs.elementAt(3);
										//tax_struct_dtl = ""+temp_tcs.elementAt(4);
										tds_factor = ""+temp_tcs.elementAt(5);
									}
									else if(!gross_amt1.equals(""))
									{
										//total_transaction_amt=""+(Double.parseDouble(total_transaction_amt) + Double.parseDouble(gross_amt1));
										total_transaction_amt=""+(Double.parseDouble(total_transaction_amt) + tds_gross_amt);
										if(Double.parseDouble(total_transaction_amt)>transaction_limit)
										{
											applicable_abbr="TDS";
											
											String total_difference=nf.format(Double.parseDouble(total_transaction_amt)- transaction_limit);
											
											if(!total_difference.equals("") && invoice_raised_in.equals("2"))
											{
												total_difference = nf.format(Double.parseDouble(total_difference) / exchng_rate);
											}
											
											Vector temp_tcs = new Vector();
											temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, total_difference);
				
											tds_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
											tds_struct_cd = ""+temp_tcs.elementAt(1);
											tds_struct_dt = ""+temp_tcs.elementAt(2);
											//tax_info = ""+temp_tcs.elementAt(3);
											//tax_struct_dtl = ""+temp_tcs.elementAt(4);
											tds_factor = ""+temp_tcs.elementAt(5);
										}
										else
										{
											applicable_abbr="NA";
										}
									}
									else
									{
										applicable_abbr="NA";
									}
								}
								else
								{
									applicable_flag="N";
									applicable_abbr="NA";
								}
								net_payable=invoice_amt;
							}
						}	
						invoice_check_flag="";
						invoice_auth_flag="";
					}
				}
			}
			
			///////////////TDS RECHECK CALCULATION//////////////////////////////////////////
			if(contract_type.equals("P") || contract_type.equals("G"))
			{
				new_applicable_abbr="TDS";
				
				//ReGas Invoice TDS will be picked up
				Vector temp_tcs = new Vector();
				temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "SI","S",period_end_dt, gross_include_transport_tariff);
				
				new_tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
				new_tds_struct_cd = ""+temp_tcs.elementAt(1);
				new_tds_struct_dt = ""+temp_tcs.elementAt(2);
				//tax_info = ""+temp_tcs.elementAt(3);
				//tax_struct_dtl = ""+temp_tcs.elementAt(4);
				new_tds_factor = ""+temp_tcs.elementAt(5);
				
				if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_amt))==Double.doubleToRawLongBits(0))
				{
					new_tds_amt="";
				}
				if(!new_tds_factor.equals(""))
				{
					if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_factor))==Double.doubleToRawLongBits(0))
					{
						new_tds_factor="";
					}
				}
			}
			else
			{
				String TurnoverFlag="";
				queryString4="SELECT TURNOVER_FLAG "
						+ "FROM FMS_ENTITY_TURNOVER_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, counterparty_cd);
				stmt4.setString(3, "T");
				stmt4.setString(4, financial_year);
				stmt4.setString(5, "1");
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					TurnoverFlag=rset4.getString(1)==null?"":rset4.getString(1);
				}
				rset4.close();
				stmt4.close();
				
				if(TurnoverFlag.equals("Y"))
				{
					new_applicable_abbr="TCS";
				}
				else
				{
					if(!contract_type.equals("I")) //AS DISCUSSED WITH VIJAY ON 20230725, TDS IS NOT APPLICABLE FOR IGX INVOICE
					{
						String total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, financial_year);
						
						//applicable_flag="N";
						if(Double.parseDouble(total_transaction_amt)>transaction_limit)
						{
							new_applicable_abbr="TDS";
							
							Vector temp_tcs = new Vector();
							//temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, gross_amt1);
							temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, gross_include_transport_tariff);
							
							new_tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
							new_tds_struct_cd = ""+temp_tcs.elementAt(1);
							new_tds_struct_dt = ""+temp_tcs.elementAt(2);
							//tax_info = ""+temp_tcs.elementAt(3);
							//tax_struct_dtl = ""+temp_tcs.elementAt(4);
							new_tds_factor = ""+temp_tcs.elementAt(5);
							
							if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_amt))==Double.doubleToRawLongBits(0))
							{
								new_tds_amt="";
							}
							if(!new_tds_factor.equals(""))
							{
								if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_factor))==Double.doubleToRawLongBits(0))
								{
									new_tds_factor="";
								}
							}
						}
						else if(!gross_amt1.equals(""))
						{
							//total_transaction_amt=""+(Double.parseDouble(total_transaction_amt) + Double.parseDouble(gross_amt1));
							total_transaction_amt=""+(Double.parseDouble(total_transaction_amt) + tdsGrossAmt);
							if(Double.parseDouble(total_transaction_amt)>transaction_limit)
							{
								new_applicable_abbr="TDS";
								
								String total_difference=nf.format(Double.parseDouble(total_transaction_amt)- transaction_limit);
								if(!total_difference.equals("") && invoice_raised_in.equals("2"))
								{
									total_difference = nf.format(Double.parseDouble(total_difference) / exchngRate);
								}
								
								Vector temp_tcs = new Vector();
								
								temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "T", "TDS", "S","P",period_end_dt, total_difference);
								
								new_tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
								new_tds_struct_cd = ""+temp_tcs.elementAt(1);
								new_tds_struct_dt = ""+temp_tcs.elementAt(2);
								//tax_info = ""+temp_tcs.elementAt(3);
								//tax_struct_dtl = ""+temp_tcs.elementAt(4);
								new_tds_factor = ""+temp_tcs.elementAt(5);
								
								if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_amt))==Double.doubleToRawLongBits(0))
								{
									new_tds_amt="";
								}
								if(!new_tds_factor.equals(""))
								{
									if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_factor))==Double.doubleToRawLongBits(0))
									{
										new_tds_factor="";
									}
								}
							}
							else
							{
								new_applicable_abbr="NA";
							}
						}
						else
						{
							new_applicable_abbr="NA";
						}
					}
					else
					{
						//applicable_flag="N";
						new_applicable_abbr="NA";
					}
				}
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void PartyInvoiceCalculation()
	{
		String function_nm="PartyInvoiceCalculation()";
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),SYS_INV_NO,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
					+ "SUG_QTY,SUG_PERCENT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			if(contract_type.equals("N")) {
				queryString+="AND CARGO_NO=? AND BOE_NO=? ";
			}else if(contract_type.equals("G") || contract_type.equals("P")) {
				queryString+="AND CARGO_NO=? ";
			}
			int stcount=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++stcount, comp_cd);
			stmt.setString(++stcount, counterparty_cd);
			stmt.setString(++stcount, cont_no);
			stmt.setString(++stcount, agmt_no);
			stmt.setString(++stcount, plant_seq);
			stmt.setString(++stcount, bu_unit);
			stmt.setString(++stcount, contract_type);
			stmt.setString(++stcount, billing_cycle);
			stmt.setString(++stcount, period_start_dt);
			stmt.setString(++stcount, period_end_dt);
			stmt.setString(++stcount, inv_flag);
			if(contract_type.equals("N")) {
				stmt.setString(++stcount, cargo_no);
				stmt.setString(++stcount, boe_no);
			}else if(contract_type.equals("G") || contract_type.equals("P")) {
				stmt.setString(++stcount, cargo_no);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				pg_submission_chk=true;

				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				pg_invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				pg_invoice_no=rset.getString(4)==null?"":rset.getString(4);
				pg_invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				pg_invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				pg_qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));//
				pg_price_cd=rset.getString(9)==null?"":rset.getString(9);
				pg_price=utilBean.RateNumberFormat(rset.getDouble(8), pg_price_cd);
				pg_gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));//
				pg_exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
				pg_exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
				pg_exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));//
				pg_invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
				pg_gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
				pg_tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));//
				pg_tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				pg_tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				pg_invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));//
				pg_net_payable=rset.getString(22)==null?"":nf.format(rset.getDouble(22));//
				pg_invoice_check_flag=rset.getString(23)==null?"":rset.getString(23);
				String chk_by=rset.getString(24)==null?"":rset.getString(24);
				pg_invoice_check_by=utilBean.getEmpName(conn,chk_by);
				pg_invoice_check_dt=rset.getString(25)==null?"":rset.getString(25);
				if(pg_invoice_check_flag.equals("Y"))
				{
					pg_invoice_check_nm="Checked";
				}
				else if(pg_invoice_check_flag.equals("N"))
				{
					pg_invoice_check_nm="Rejected";
				}
				pg_invoice_auth_flag=rset.getString(26)==null?"":rset.getString(26);
				String auth_by=rset.getString(27)==null?"":rset.getString(27);
				pg_invoice_auth_by=utilBean.getEmpName(conn,auth_by);
				pg_invoice_auth_dt=rset.getString(28)==null?"":rset.getString(28);
				if(pg_invoice_auth_flag.equals("Y"))
				{
					pg_invoice_auth_nm="Authorized";
				}
				else if(pg_invoice_auth_flag.equals("N"))
				{
					pg_invoice_auth_nm="Rejected";
				}

				pg_invoice_aprv_flag=rset.getString(29)==null?"":rset.getString(29);
				String aprv_by=rset.getString(30)==null?"":rset.getString(30);
				pg_invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
				pg_invoice_aprv_dt=rset.getString(31)==null?"":rset.getString(31);
				if(pg_invoice_aprv_flag.equals("A"))
				{
					pg_invoice_aprv_nm="Approved";
				}
				else if(pg_invoice_aprv_flag.equals("R"))
				{
					pg_invoice_aprv_nm="Rejected";
				}
				
				pg_rem_gen_status=""+getInvoiceStatus(pg_invoice_check_flag, pg_invoice_auth_flag, pg_invoice_aprv_flag);

				pg_invoice_adj_sign=rset.getString(32)==null?"":rset.getString(32);
				pg_invoice_adj_amt=rset.getString(33)==null?"":nf.format(rset.getDouble(33));//
				pg_financial_year=rset.getString(34)==null?"":rset.getString(34);
				
				pg_tcs_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				pg_tcs_factor=rset.getString(36)==null?"":nf3.format(rset.getDouble(36));
				pg_tcs_struct_cd=rset.getString(37)==null?"":rset.getString(37);
				pg_tcs_struct_dt=rset.getString(38)==null?"":rset.getString(38);
				
				pg_tds_amount=rset.getString(39)==null?"":nf.format(rset.getDouble(39));
				pg_tds_factor=rset.getString(40)==null?"":nf3.format(rset.getDouble(40));
				pg_tds_struct_cd=rset.getString(41)==null?"":rset.getString(41);
				pg_tds_struct_dt=rset.getString(42)==null?"":rset.getString(42);
				
				pg_sys_invoice_no=rset.getString(43)==null?"":rset.getString(43);
				
				pg_cif_value=rset.getString(44)==null?"":nf.format(rset.getDouble(44));
				pg_assessable_vlaue=rset.getString(45)==null?"":nf.format(rset.getDouble(45));
				pg_remark=rset.getString(46)==null?"":rset.getString(46);
				pg_diff_price=utilBean.RateNumberFormat(rset.getDouble(47), pg_price_cd);
				pg_cd_paid_amt=rset.getString(48)==null?"":nf.format(rset.getDouble(48));
				
				//sug qty 49
				//sug percent 50
				
				//transportation_charges=rset.getString(51)==null?"":nf2.format(rset.getDouble(51));
				pg_transportation_amount=rset.getString(52)==null?"":nf.format(rset.getDouble(52));
				//marketing_margin=rset.getString(53)==null?"":nf2.format(rset.getDouble(53));
				pg_marketing_margin_amount=rset.getString(54)==null?"":nf.format(rset.getDouble(54));
				//other_charges=rset.getString(55)==null?"":nf2.format(rset.getDouble(55));
				pg_other_charges_amount=rset.getString(56)==null?"":nf.format(rset.getDouble(56));
				
				pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_amt1));
				if(!pg_transportation_amount.equals(""))
				{
					pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_transportation_amount));
				}
				if(!pg_marketing_margin_amount.equals(""))
				{
					pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_marketing_margin_amount));
				}
				if(!pg_other_charges_amount.equals(""))
				{
					pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_other_charges_amount));
				}
				
				if(contract_type.equals("N") && (inv_flag.equals("PF") || inv_flag.equals("P") || inv_flag.equals("F")))
				{
					
				}
				else
				{
					if(inv_flag.equals("CP") || inv_flag.equals("CF"))
					{
						//pg_tax_struct_dtl=utilBean.getCustomTaxStructureDtl(conn,comp_cd,period_start_dt);
					}
					else if(contract_type.equals("G") || contract_type.equals("P"))
					{
						//pg_tax_struct_dtl=utilBean.getEntityServiceStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt,"SI");
					}
					else if(!contract_type.equals("N"))
					{
						//pg_tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "T", plant_seq, bu_unit, period_start_dt);
					}
					
					pg_tax_struct_dtl=utilBean.getTaxDescr(conn,pg_tax_struct_cd);
				}
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_PUR_PG_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, pg_financial_year);
				stmt1.setString(4, pg_invoice_seq);
				stmt1.setString(5, inv_flag);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				
				VPG_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
			}
			else
			{
				pg_sys_invoice_no=sys_invoice_no;
				pg_invoice_seq = invoice_seq;
				pg_price_cd = price_cd;
				pg_invoice_raised_in = invoice_raised_in;
				pg_financial_year=financial_year;
				pg_tcs_factor=tcs_factor;
				pg_tcs_struct_cd=tcs_struct_cd;
				pg_tcs_struct_dt=tcs_struct_dt;
				
				pg_tds_factor=tds_factor;
				pg_tds_struct_cd=tds_struct_cd;
				pg_tds_struct_dt=tds_struct_dt;
				
				pg_tax_struct_cd=tax_struct_cd;
				pg_tax_struct_dt=tax_struct_dt;
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				if(VSG_MULTI_TAX_STRUCT.size()>0)
				{
					for(int i=0;i<VSG_MULTI_TAX_STRUCT.size();i++)
					{
						Vector temp =(Vector)((Vector)((Vector)VSG_MULTI_TAX_STRUCT.elementAt(i)));
						for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
						{
							VTAX_CODE.add(((Vector) temp.elementAt(0)).elementAt(j));
							VTAX_DESCR.add(((Vector) temp.elementAt(1)).elementAt(j));
							VTAX_AMT.add("");
							VTAX_BASE_AMT.add("");
						}
					}
				}
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				
				VPG_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Double getTransactionAmount(String comp_cd,String counterparty_cd, String financial_year)
	{
		String function_nm="getTransactionAmount()";
		double transc_fee=0;
		try
		{	
			queryString="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "D");
			stmt.setString(4, "T");
			stmt.setString(5, financial_year);
			stmt.setString(6, "A");
			stmt.setString(7, invoice_dt);
			stmt.setString(8, "F");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmtno=rset.getString(1)==null?"":rset.getString(1);
				String contno=rset.getString(2)==null?"":rset.getString(2);
				String cont_type=rset.getString(3)==null?"0":rset.getString(3);
				double inv_amt=rset.getDouble(4);
				String inv_no=rset.getString(5)==null?"":rset.getString(5);
				String inv_dt=rset.getString(6)==null?"":rset.getString(6);
				String inv_raised_in=rset.getString(7)==null?"":rset.getString(7);
				double gross_amt_inr=rset.getDouble(8);
				String exchng_rt_cd=rset.getString(9)==null?"":rset.getString(9);
				String due_dt=rset.getString(10)==null?"":rset.getString(10);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, "", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString1="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, exchng_rt_cd);
				stmt1.setString(2, due_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					exchng_rate=rset1.getDouble(1);
				}
				rset1.close();
				stmt1.close();
				
				if(inv_raised_in.equals("2"))
				{
					transc_fee+=(inv_amt * exchng_rate);
				}
				else
				{
					transc_fee+=inv_amt;
				}
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "D");
			stmt1.setString(4, "T");
			stmt1.setString(5, financial_year);
			stmt1.setString(6, "A");
			stmt1.setString(7, invoice_dt);
			stmt1.setString(8, "F");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String agmtno=rset1.getString(1)==null?"":rset1.getString(1);
				String contno=rset1.getString(2)==null?"":rset1.getString(2);
				String cont_type=rset1.getString(3)==null?"0":rset1.getString(3);
				double inv_amt=rset1.getDouble(4);
				String inv_no=rset1.getString(5)==null?"":rset1.getString(5);
				String inv_dt=rset1.getString(6)==null?"":rset1.getString(6);
				String inv_raised_in=rset1.getString(7)==null?"":rset1.getString(7);
				double gross_amt_inr=rset1.getDouble(8);
				String exchng_rt_cd=rset1.getString(9)==null?"":rset1.getString(9);
				String due_dt=rset1.getString(10)==null?"":rset1.getString(10);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, "", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString2="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, exchng_rt_cd);
				stmt2.setString(2, due_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					exchng_rate=rset2.getDouble(1);
				}
				rset2.close();
				stmt2.close();
				
				if(inv_raised_in.equals("2"))
				{
					transc_fee+=(inv_amt * exchng_rate);
				}
				else
				{
					transc_fee+=inv_amt;
				}
			}
			rset1.close();
			stmt1.close();
			
			queryString2="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT_INR,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT_USD "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?)";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "D");
			stmt2.setString(4, "T");
			stmt2.setString(5, financial_year);
			stmt2.setString(6, "A");
			stmt2.setString(7, invoice_dt);
			stmt2.setString(8, "DR");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String agmtno=rset2.getString(1)==null?"":rset2.getString(1);
				String contno=rset2.getString(2)==null?"":rset2.getString(2);
				String cont_type=rset2.getString(3)==null?"0":rset2.getString(3);
				double inv_amt=rset2.getDouble(4);
				String inv_no=rset2.getString(5)==null?"":rset2.getString(5);
				String inv_dt=rset2.getString(6)==null?"":rset2.getString(6);
				String inv_raised_in=rset2.getString(7)==null?"":rset2.getString(7);
				double gross_amt_inr=rset2.getDouble(8);
				String exchng_rt_cd=rset2.getString(9)==null?"":rset2.getString(9);
				String due_dt=rset2.getString(10)==null?"":rset2.getString(10);
				double gross_amt_usd=rset2.getDouble(11);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, "", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString3="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, exchng_rt_cd);
				stmt3.setString(2, due_dt);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					exchng_rate=rset3.getDouble(1);
				}
				rset3.close();
				stmt3.close();
				
				if(inv_raised_in.equals("2"))
				{
					transc_fee+=(inv_amt * exchng_rate);
				}
				else
				{
					transc_fee+=inv_amt;
				}
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return transc_fee;
	}

	public void storeInvoiceDataIntoHashMap()
	{
		String function_nm="storeInvoiceDataIntoHashMap()";
		try
		{
			if(inv_type.equals("P"))
			{
				invoice_data.put("InvoiceDate", pg_invoice_dt);
				invoice_data.put("InvoiceDueDate", pg_invoice_due_dt);
				invoice_data.put("InvoiceNo", pg_invoice_no);
				invoice_data.put("InvoiceSeq", pg_invoice_seq);
				invoice_data.put("InvoiceRaisedIn", pg_invoice_raised_in);
				invoice_data.put("PriceCd", pg_price_cd);
				invoice_data.put("Price", pg_price);
				invoice_data.put("Qty", pg_qty_mmbtu);
				invoice_data.put("GrossAmt", pg_gross_amt);
				invoice_data.put("ExchangRate", pg_exchang_rate);
				invoice_data.put("GrossAmt1", pg_gross_amt1);
				invoice_data.put("TaxAmt", pg_tax_amt);
				invoice_data.put("TaxStructDtl", pg_tax_struct_dtl);
				invoice_data.put("InvoiceAmt", pg_invoice_amt);
				invoice_data.put("AdjustSign", pg_invoice_adj_sign);
				invoice_data.put("AdjustAmt", pg_invoice_adj_amt);
				invoice_data.put("NetPayable", pg_net_payable);
				invoice_data.put("TcsAmt", pg_tcs_amount);
				invoice_data.put("TcsFactor", pg_tcs_factor);
				invoice_data.put("ApplicableFlag", applicable_flag);
				invoice_data.put("ApplicableAbbr", applicable_abbr);
				invoice_data.put("SysInvNo", pg_sys_invoice_no);
				
				invoice_data.put("CIFAmt", pg_cif_value);
				invoice_data.put("AssessableAmt", pg_assessable_vlaue);
				invoice_data.put("Remark", pg_remark);
				invoice_data.put("DiffPrice", pg_diff_price);
				invoice_data.put("CdPaidAmt", pg_cd_paid_amt);
				
				invoice_data.put("SUGpercent", "");
				invoice_data.put("SUGqty", "");
				
				invoice_data.put("QtyUnit", energy_unit);
				invoice_data.put("QtyUnitNm", energy_unit_nm);
				
				invoice_data.put("trans_chrg", transportation_charges);
				invoice_data.put("trans_chrg_amt", pg_transportation_amount);
				invoice_data.put("market_margin", marketing_margin);
				invoice_data.put("market_margin_amt", pg_marketing_margin_amount);
				invoice_data.put("oth_chrge", other_charges);
				invoice_data.put("oth_chrge_amt", pg_other_charges_amount);
				invoice_data.put("gross_incl_chrg", pg_gross_include_transport_tariff);
				invoice_data.put("isGrossIncTriff", ""+isGrossIncTriff);
			}
			else
			{
				invoice_data.put("InvoiceDate", invoice_dt);
				invoice_data.put("InvoiceDueDate", invoice_due_dt);
				invoice_data.put("InvoiceNo", invoice_no);
				invoice_data.put("InvoiceSeq", invoice_seq);
				invoice_data.put("InvoiceRaisedIn", invoice_raised_in);
				invoice_data.put("PriceCd", price_cd);
				invoice_data.put("Price", price);
				invoice_data.put("Qty", qty_mmbtu);
				invoice_data.put("GrossAmt", gross_amt);
				invoice_data.put("ExchangRate", exchang_rate);
				invoice_data.put("GrossAmt1", gross_amt1);
				invoice_data.put("TaxAmt", tax_amt);
				invoice_data.put("TaxStructDtl", tax_struct_dtl);
				invoice_data.put("InvoiceAmt", invoice_amt);
				invoice_data.put("AdjustSign", invoice_adj_sign);
				invoice_data.put("AdjustAmt", invoice_adj_amt);
				invoice_data.put("NetPayable", net_payable);
				invoice_data.put("TcsAmt", tcs_amount);
				invoice_data.put("TcsFactor", tcs_factor);
				invoice_data.put("ApplicableFlag", applicable_flag);
				invoice_data.put("ApplicableAbbr", applicable_abbr);
				invoice_data.put("SysInvNo", sys_invoice_no);
				invoice_data.put("CIFAmt", cif_value);
				invoice_data.put("AssessableAmt", assessable_vlaue);
				invoice_data.put("Remark", remark);
				invoice_data.put("DiffPrice", diff_price);
				invoice_data.put("CdPaidAmt", cd_paid_amt);
				invoice_data.put("SUGpercent", sug_percentage);
				invoice_data.put("SUGqty", sug_qty);
				
				invoice_data.put("QtyUnit", energy_unit);
				invoice_data.put("QtyUnitNm", energy_unit_nm);
				
				invoice_data.put("trans_chrg", transportation_charges);
				invoice_data.put("trans_chrg_amt", transportation_amount);
				invoice_data.put("market_margin", marketing_margin);
				invoice_data.put("market_margin_amt", marketing_margin_amount);
				invoice_data.put("oth_chrge", other_charges);
				invoice_data.put("oth_chrge_amt", other_charges_amount);
				invoice_data.put("gross_incl_chrg", gross_include_transport_tariff);
				invoice_data.put("isGrossIncTriff", ""+isGrossIncTriff);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getApprovedInvoiceDetail()
	{
		String function_nm="getApprovedInvoiceDetail()";
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND APPROVED_FLAG=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, contract_type);
			stmt.setString(7, billing_cycle);
			stmt.setString(8, period_start_dt);
			stmt.setString(9, period_end_dt);
			stmt.setString(10, "A");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VINVOICE_SEQ.add(rset.getString(3)==null?"":rset.getString(3));
				VINVOICE_NO.add(rset.getString(4)==null?"":rset.getString(4));
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				VINVOICE_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VINVOICE_DUE_DT.add(rset.getString(6)==null?"":rset.getString(6));
			}
			rset.close();
			stmt.close();

			queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND APPROVED_FLAG=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, cont_no);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, plant_seq);
			stmt1.setString(6, contract_type);
			stmt1.setString(7, billing_cycle);
			stmt1.setString(8, period_start_dt);
			stmt1.setString(9, period_end_dt);
			stmt1.setString(10, "A");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VINVOICE_SEQ.add(rset1.getString(3)==null?"":rset1.getString(3));
				VINVOICE_NO.add(rset1.getString(4)==null?"":rset1.getString(4));
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				VINVOICE_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
				VINVOICE_DUE_DT.add(rset1.getString(6)==null?"":rset1.getString(6));
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getApprovedInvoiceDetailForDRCRNote()
	{
		String function_nm="getApprovedInvoiceDetailForDRCRNote()";
		try
		{
			int cont=0;
			queryString="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "FREQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,GROSS_AMT,TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,SALE_AMT "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? ";
			if(!billing_cycle.equals("0"))
			{
				queryString+= "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString+= " AND APPROVED_FLAG=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, comp_cd);
			stmt.setString(++cont, counterparty_cd);
			if(!billing_cycle.equals("0"))
			{
				stmt.setString(++cont, billing_cycle);
				stmt.setString(++cont, period_start_dt);
				stmt.setString(++cont, period_end_dt);
			}
			stmt.setString(++cont, "A");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VINVOICE_SEQ.add(rset.getString(1)==null?"":rset.getString(1));
				VINVOICE_NO.add(rset.getString(2)==null?"":rset.getString(2));
				VINVOICE_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VINVOICE_DUE_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VPERIOD_START_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VPERIOD_END_DT.add(rset.getString(6)==null?"":rset.getString(6));
				String freq = rset.getString(7)==null?"":rset.getString(7);
				VFREQ.add(freq);
				VFREQ_NM.add(""+utilBean.getFreqNm(freq));
				VALLOC_QTY.add(nf.format(rset.getDouble(8)));

				VEXCHNAGE_RATE.add(nf.format(rset.getDouble(17)));

				String rate_unit = rset.getString(10)==null?"":rset.getString(10);

				String inv_raisedIn = rset.getString(18)==null?"":rset.getString(18);
				if(inv_raisedIn.equals("1"))
				{
					if(rate_unit.equals("1"))
					{
						VSALES_PRICE.add(nf.format(rset.getDouble(9)));
						VSALES_PRICE_UNIT.add(rate_unit);
						VRATE_NM.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT.add(nf.format(rset.getDouble(11)));

						VSALES_PRICE_USD.add("");
						VSALES_PRICE_UNIT_USD.add("");
						VRATE_NM_USD.add("");
						VGROSS_AMT_USD.add("");
					}
					else
					{
						VSALES_PRICE_USD.add(nf.format(rset.getDouble(9)));
						VSALES_PRICE_UNIT_USD.add(rate_unit);
						VRATE_NM_USD.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT_USD.add(nf.format(rset.getDouble(19)));

						VSALES_PRICE.add("");
						VSALES_PRICE_UNIT.add("");
						VRATE_NM.add("");
						VGROSS_AMT.add("");
					}
					VTAX_AMT.add(nf.format(rset.getDouble(12)));
					VINVOICE_AMT.add(nf.format(rset.getDouble(13)));
					VADJ_SIGN.add(rset.getString(14)==null?"":rset.getString(14));
					VADJ_AMT.add(nf.format(rset.getDouble(15)));
					VNET_PAYABLE.add(nf.format(rset.getDouble(16)));


					VTAX_AMT_USD.add("");
					VINVOICE_AMT_USD.add("");
					VADJ_SIGN_USD.add("");
					VADJ_AMT_USD.add("");
					VNET_PAYABLE_USD.add("");
				}
				else if(inv_raisedIn.equals("2"))
				{
					if(rate_unit.equals("2"))
					{
						VSALES_PRICE_USD.add(nf.format(rset.getDouble(9)));
						VSALES_PRICE_UNIT_USD.add(rate_unit);
						VRATE_NM_USD.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT_USD.add(nf.format(rset.getDouble(11)));

						VSALES_PRICE.add("");
						VSALES_PRICE_UNIT.add("");
						VRATE_NM.add("");
						VGROSS_AMT.add("");
					}
					else
					{
						VSALES_PRICE.add(nf.format(rset.getDouble(9)));
						VSALES_PRICE_UNIT.add(rate_unit);
						VRATE_NM.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT.add(nf.format(rset.getDouble(19)));

						VSALES_PRICE_USD.add("");
						VSALES_PRICE_UNIT_USD.add("");
						VRATE_NM_USD.add("");
						VGROSS_AMT_USD.add("");

					}
					VTAX_AMT_USD.add(nf.format(rset.getDouble(12)));
					VINVOICE_AMT_USD.add(nf.format(rset.getDouble(13)));
					VADJ_SIGN_USD.add(rset.getString(14)==null?"":rset.getString(14));
					VADJ_AMT_USD.add(nf.format(rset.getDouble(15)));
					VNET_PAYABLE_USD.add(nf.format(rset.getDouble(16)));

					VTAX_AMT.add("");
					VINVOICE_AMT.add("");
					VADJ_SIGN.add("");
					VADJ_AMT.add("");
					VNET_PAYABLE.add("");
				}
			}
			rset.close();
			stmt.close();

			int cont1=0;
			queryString1="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "FREQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,GROSS_AMT,TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,SALE_AMT "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? ";
			if(!billing_cycle.equals("0"))
			{
				queryString1+= "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
			}
			queryString1+= " AND APPROVED_FLAG=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(++cont1, comp_cd);
			stmt1.setString(++cont1, counterparty_cd);
			if(!billing_cycle.equals("0"))
			{
				stmt1.setString(++cont1, billing_cycle);
				stmt1.setString(++cont1, period_start_dt);
				stmt1.setString(++cont1, period_end_dt);
			}
			stmt1.setString(++cont1, "A");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VINVOICE_SEQ.add(rset1.getString(1)==null?"":rset1.getString(1));
				VINVOICE_NO.add(rset1.getString(2)==null?"":rset1.getString(2));
				VINVOICE_DT.add(rset1.getString(3)==null?"":rset1.getString(3));
				VINVOICE_DUE_DT.add(rset1.getString(4)==null?"":rset1.getString(4));
				VPERIOD_START_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
				VPERIOD_END_DT.add(rset1.getString(6)==null?"":rset1.getString(6));
				String freq = rset1.getString(7)==null?"":rset1.getString(7);
				VFREQ.add(freq);
				VFREQ_NM.add(""+utilBean.getFreqNm(freq));
				VALLOC_QTY.add(nf.format(rset1.getDouble(8)));

				VEXCHNAGE_RATE.add(nf.format(rset1.getDouble(17)));

				String rate_unit = rset1.getString(10)==null?"":rset1.getString(10);

				String inv_raisedIn = rset1.getString(18)==null?"":rset1.getString(18);
				if(inv_raisedIn.equals("1"))
				{
					if(rate_unit.equals("1"))
					{
						VSALES_PRICE.add(nf.format(rset1.getDouble(9)));
						VSALES_PRICE_UNIT.add(rate_unit);
						VRATE_NM.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT.add(nf.format(rset1.getDouble(11)));

						VSALES_PRICE_USD.add("");
						VSALES_PRICE_UNIT_USD.add("");
						VRATE_NM_USD.add("");
						VGROSS_AMT_USD.add("");
					}
					else
					{
						VSALES_PRICE_USD.add(nf.format(rset1.getDouble(9)));
						VSALES_PRICE_UNIT_USD.add(rate_unit);
						VRATE_NM_USD.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT_USD.add(nf.format(rset1.getDouble(19)));

						VSALES_PRICE.add("");
						VSALES_PRICE_UNIT.add("");
						VRATE_NM.add("");
						VGROSS_AMT.add("");
					}
					VTAX_AMT.add(nf.format(rset1.getDouble(12)));
					VINVOICE_AMT.add(nf.format(rset1.getDouble(13)));
					VADJ_SIGN.add(rset1.getString(14)==null?"":rset1.getString(14));
					VADJ_AMT.add(nf.format(rset1.getDouble(15)));
					VNET_PAYABLE.add(nf.format(rset1.getDouble(16)));


					VTAX_AMT_USD.add("");
					VINVOICE_AMT_USD.add("");
					VADJ_SIGN_USD.add("");
					VADJ_AMT_USD.add("");
					VNET_PAYABLE_USD.add("");
				}
				else if(inv_raisedIn.equals("2"))
				{
					if(rate_unit.equals("2"))
					{
						VSALES_PRICE_USD.add(nf.format(rset1.getDouble(9)));
						VSALES_PRICE_UNIT_USD.add(rate_unit);
						VRATE_NM_USD.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT_USD.add(nf.format(rset1.getDouble(11)));

						VSALES_PRICE.add("");
						VSALES_PRICE_UNIT.add("");
						VRATE_NM.add("");
						VGROSS_AMT.add("");
					}
					else
					{
						VSALES_PRICE.add(nf.format(rset1.getDouble(9)));
						VSALES_PRICE_UNIT.add(rate_unit);
						VRATE_NM.add("("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)");
						VGROSS_AMT.add(nf.format(rset1.getDouble(19)));

						VSALES_PRICE_USD.add("");
						VSALES_PRICE_UNIT_USD.add("");
						VRATE_NM_USD.add("");
						VGROSS_AMT_USD.add("");

					}
					VTAX_AMT_USD.add(nf.format(rset1.getDouble(12)));
					VINVOICE_AMT_USD.add(nf.format(rset1.getDouble(13)));
					VADJ_SIGN_USD.add(rset1.getString(14)==null?"":rset1.getString(14));
					VADJ_AMT_USD.add(nf.format(rset1.getDouble(15)));
					VNET_PAYABLE_USD.add(nf.format(rset1.getDouble(16)));

					VTAX_AMT.add("");
					VINVOICE_AMT.add("");
					VADJ_SIGN.add("");
					VADJ_AMT.add("");
					VNET_PAYABLE.add("");
				}
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTraderCounterpartyList()
	{
		String function_nm="getTraderCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTraderCounterpartyList(comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd,"T");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";
		try
		{
			/*if(entity.equals("T"))
			{
				utilBean.getEffectiveTraderCounterpartyList(comp_cd);
			}
			else if(entity.equals("V"))
			{
				utilBean.getEffectiveVesselAgentCounterpartyList(comp_cd);
			}
			else if(entity.equals("H"))
			{
				utilBean.getEffectiveCustomHouseAgentCounterpartyList(comp_cd);
			}
			else if(entity.equals("S"))
			{
				utilBean.getEffectiveSurveyorCounterpartyList(comp_cd);
			}*/
			
			utilBean.getAllEntityCounterpartyList(conn,comp_cd,entity);
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFilterContractList()
	{
		String function_nm="getFilterContractList()";
		try
		{
			if(entity.equals("T"))
			{
				VFILTER_CONT_TYPE.add("D");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("D"));
				
				VFILTER_CONT_TYPE.add("T");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("T"));
				
				VFILTER_CONT_TYPE.add("I");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("I"));
				
				VFILTER_CONT_TYPE.add("N");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("N"));
				
				VFILTER_CONT_TYPE.add("G");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("G"));
				
				VFILTER_CONT_TYPE.add("P");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("P"));
			}
			else if(entity.equals("H"))
			{
				VFILTER_CONT_TYPE.add("H");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("H"));
			}
			else if(entity.equals("S"))
			{
				VFILTER_CONT_TYPE.add("Y");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("Y"));
			}
			else if(entity.equals("V"))
			{
				VFILTER_CONT_TYPE.add("A");
				VFILTER_CONT_NAME.add(utilBean.getContractTypeName("A"));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getContractList()
	{
		String function_nm="getContractList()";
		try
		{
			queryString="SELECT COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
					+ "CONT_REF_NO,CONTRACT_TYPE,TRADE_REF_NO,NULL,COUNTERPARTY_CD,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE 'T'=? AND COMPANY_CD=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND CONTRACT_TYPE=? ";
			}
			queryString+= " UNION ";
			queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "A.CONT_REF_NO,A.CONTRACT_TYPE,A.TRADE_REF_NO,NULL,A.COUNTERPARTY_CD,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_SPLIT_PLANT B "
					+ "WHERE 'T'=? AND A.COMPANY_CD=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND B.SPLIT_TRADER_CD=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND A.SPLIT_FLAG=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= " UNION ";
			queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "B.CARGO_REF,A.CONTRACT_TYPE,NULL,G.CARGO_NO,A.COUNTERPARTY_CD,"
					+ "TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CN_MST A, "
						+ "FMS_TRADER_CARGO_MST B, "
						+ "FMS_BUY_CARGO_NOM C,"
						+ "FMS_BUY_CARGO_NOM_BOE G "
					+ "WHERE 'T'=? AND A.COMPANY_CD=? AND B.CARGO_STATUS=? AND A.COUNTERPARTY_CD=? "
					+ "AND C.EXP_FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND C.EXP_TO_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
					+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
					+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
					+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND C.COMPANY_CD=G.COMPANY_CD AND C.COUNTERPARTY_CD=G.COUNTERPARTY_CD AND C.CONTRACT_TYPE=G.CONTRACT_TYPE "
					+ "AND C.CONT_NO=G.CONT_NO AND C.AGMT_TYPE=G.AGMT_TYPE AND C.AGMT_NO=G.AGMT_NO AND C.CARGO_NO=G.CARGO_NO AND C.NOM_REV=G.NOM_REV ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= " UNION ";
			queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "A.CONT_REF_NO,A.CONTRACT_TYPE,NULL,B.CARGO_NO,A.COUNTERPARTY_CD,"
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE 'T'=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= " UNION ";
			queryString+="SELECT COMPANY_CD,NULL,NULL,CONT_NO,NULL,"
					+ "CONT_REF_NO,CONTRACT_TYPE,NULL,NULL,COUNTERPARTY_CD,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') " 
					+ "FROM FMS_CARGO_SVC_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND CONTRACT_TYPE=? ";
			}
			int st_count=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++st_count, entity);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, period_end_dt);
			stmt.setString(++st_count, period_start_dt);
			stmt.setString(++st_count, counterparty_cd);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++st_count, filter_cont_type);
			}
			stmt.setString(++st_count, entity);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, period_end_dt);
			stmt.setString(++st_count, period_start_dt);
			stmt.setString(++st_count, counterparty_cd);
			stmt.setString(++st_count, "Y");
			if(!filter_cont_type.equals("")) {
				stmt.setString(++st_count, filter_cont_type);
			}
			stmt.setString(++st_count, entity);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, "Y");
			stmt.setString(++st_count, counterparty_cd);
			stmt.setString(++st_count, period_end_dt);
			stmt.setString(++st_count, period_start_dt);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++st_count, filter_cont_type);
			}
			stmt.setString(++st_count, entity);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, counterparty_cd);
			stmt.setString(++st_count, "T");
			stmt.setString(++st_count, "Y");
			stmt.setString(++st_count, "L");
			stmt.setString(++st_count, period_end_dt);
			stmt.setString(++st_count, period_start_dt);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++st_count, filter_cont_type);
			}
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, counterparty_cd);
			stmt.setString(++st_count, entity);
			stmt.setString(++st_count, period_end_dt);
			stmt.setString(++st_count, period_start_dt);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++st_count, filter_cont_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"0":rset.getString(2);
				String agmtrev=rset.getString(3)==null?"0":rset.getString(3);
				String contno=rset.getString(4)==null?"0":rset.getString(4);
				String contrev=rset.getString(5)==null?"0":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				String trade_ref=rset.getString(8)==null?"":rset.getString(8);
				
				String cargo_no=rset.getString(9)==null?"0":rset.getString(9);
				String contpty_cd=rset.getString(10)==null?"0":rset.getString(10);
				String stdt=rset.getString(11)==null?"":rset.getString(11);
				String enddt=rset.getString(12)==null?"":rset.getString(12);
				
				if(cont_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				//String deal_no=cont_type+""+contno+" ["+cont_ref+"]";
				
				String deal_no = utilBean.NewDealMappingId(own_cd, contpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				if(!cont_ref.equals(""))
				{
					deal_no+=" ["+cont_ref+"]";
				}
				
				deal_no+=" ["+stdt+"-"+enddt+"]";

				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;
				if(cont_type.equals("N") || cont_type.equals("G") || cont_type.equals("P")) {
					mapping_id+="-"+cargo_no;
				}

				VDEAL_NO.add(deal_no);
				VMAPPING_ID.add(mapping_id);
			}
			rset.close();
			stmt.close();
			
			if(!contract_type.equals(""))
			{
				queryString="SELECT COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONT_REF_NO,CONTRACT_TYPE,TRADE_REF_NO,NULL,COUNTERPARTY_CD,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE 'T' = ? AND COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				queryString+= " UNION ";
				queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "A.CONT_REF_NO,A.CONTRACT_TYPE,A.TRADE_REF_NO,NULL,A.COUNTERPARTY_CD,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_SPLIT_PLANT B "
						+ "WHERE 'T' = ? AND A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND B.SPLIT_TRADER_CD=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND A.SPLIT_FLAG=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV ";
				queryString+= " UNION ";
				queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "B.CARGO_REF,A.CONTRACT_TYPE,NULL,G.CARGO_NO,A.COUNTERPARTY_CD,"
						+ "TO_CHAR(C.EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(C.EXP_TO_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CN_MST A, "
							+ "FMS_TRADER_CARGO_MST B, "
							+ "FMS_BUY_CARGO_NOM C,"
							+ "FMS_BUY_CARGO_NOM_BOE G "
						+ "WHERE 'T' = ? AND A.COMPANY_CD=? AND B.CARGO_STATUS=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND G.CARGO_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
						+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND C.COMPANY_CD=G.COMPANY_CD AND C.COUNTERPARTY_CD=G.COUNTERPARTY_CD AND C.CONTRACT_TYPE=G.CONTRACT_TYPE "
						+ "AND C.CONT_NO=G.CONT_NO AND C.AGMT_TYPE=G.AGMT_TYPE AND C.AGMT_NO=G.AGMT_NO AND C.CARGO_NO=G.CARGO_NO AND C.NOM_REV=G.NOM_REV ";
				queryString+= " UNION ";
				queryString+="SELECT A.COMPANY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "A.CONT_REF_NO,A.CONTRACT_TYPE,NULL,B.CARGO_NO,A.COUNTERPARTY_CD,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE 'T' = ? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND B.CARGO_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				queryString+= " UNION ";
				queryString+="SELECT COMPANY_CD,NULL,NULL,CONT_NO,NULL,"
						+ "CONT_REF_NO,CONTRACT_TYPE,NULL,NULL,COUNTERPARTY_CD,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') " 
						+ "FROM FMS_CARGO_SVC_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CONT_NO=? AND ENTITY_TYPE=? ";
				st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, entity);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, contract_type);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, agmt_no);
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, entity);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, contract_type);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, agmt_no);
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, entity);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, agmt_no);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, cargo_no);
				stmt.setString(++st_count, contract_type);
				stmt.setString(++st_count, entity);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, "T");
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, "L");
				stmt.setString(++st_count, agmt_no);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, cargo_no);
				stmt.setString(++st_count, contract_type);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, contract_type);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, entity);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmtno=rset.getString(2)==null?"0":rset.getString(2);
					String agmtrev=rset.getString(3)==null?"0":rset.getString(3);
					String contno=rset.getString(4)==null?"0":rset.getString(4);
					String contrev=rset.getString(5)==null?"0":rset.getString(5);
					String cont_ref=rset.getString(6)==null?"":rset.getString(6);
					String cont_type=rset.getString(7)==null?"":rset.getString(7);
					String trade_ref=rset.getString(8)==null?"":rset.getString(8);
					
					String cargo_no=rset.getString(9)==null?"0":rset.getString(9);
					String contpty_cd=rset.getString(10)==null?"0":rset.getString(10);
					String stdt=rset.getString(11)==null?"":rset.getString(11);
					String enddt=rset.getString(12)==null?"":rset.getString(12);
					
					if(cont_type.equals("I"))
					{
						cont_ref=trade_ref;
					}
					
					String deal_no = utilBean.NewDealMappingId(own_cd, contpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					if(!cont_ref.equals(""))
					{
						deal_no+=" ["+cont_ref+"]";
					}
					
					deal_no+=" ["+stdt+"-"+enddt+"]";
	
					String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;
					if(cont_type.equals("N") || cont_type.equals("G") || cont_type.equals("P")) {
						mapping_id+="-"+cargo_no;
					}
	
					contRef=cont_ref;
					if(!VMAPPING_ID.contains(mapping_id))
					{
						VDEAL_NO.add(deal_no);
						VMAPPING_ID.add(mapping_id);
					}
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getAddressType()
	{
		String function_nm="getAddressType()";
		try
		{
			queryString="SELECT DISTINCT ADDRESS_TYPE "
					+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
					+ "WHERE COUNTERPARTY_CD=? ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT ADDRESS_TYPE "
					+ "FROM FMS_ENTITY_ADDR_MST A "
					+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String add_type = rset.getString(1)==null?"":rset.getString(1);
				if(add_type.equals("R"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Registered");
				}
				else if(add_type.equals("C"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Correspondence");
				}
				else if(add_type.equals("B"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Billing");
				}
			}
			rset.close();
			stmt.close();

			for(int i=0; i<VPLANT_SEQ.size(); i++)
			{
				if(entity.equals("T")) {
					VADDRESS_TYPE.add("P"+VPLANT_SEQ.elementAt(i));
				}else {
					VADDRESS_TYPE.add("B"+VPLANT_SEQ.elementAt(i));	
				}
				VADDRESS_NAME.add(VPLANT_ABBR.elementAt(i));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getPlantDetail()
	{
		String function_nm="getPlantDetail()";
		try
		{
			if(entity.equals("T"))
			{
				utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "T", comp_cd);
				VPLANT_SEQ = utilBean.getPLANT_SEQ_NO();
				VPLANT_ABBR = utilBean.getPLANT_ABBR();
			}
			else
			{
				utilBean.getEffectiveCounterpartyBusinessPlantList(conn,counterparty_cd, entity, comp_cd);
				VPLANT_SEQ = utilBean.getPLANT_SEQ_NO();
				VPLANT_ABBR = utilBean.getPLANT_ABBR();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getF_FlowInvoice()
	{
		String function_nm="getF_FlowInvoice()";
		try
		{
			/*queryString="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "T");
			stmt.setString(4, address_type);
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "Y");
			stmt.setString(9, "RLNG");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
			}
			rset.close();
			stmt.close();*/
			
			String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,"T",address_type, "RM", "RLNG","Y");
			contact_person_cd=temp_contact_person_cd.equals("")?"0":temp_contact_person_cd;

			/*queryString1="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, "B");
			stmt1.setString(4, "P"+bu_unit);
			stmt1.setString(5, "Y");
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			stmt1.setString(8, "Y");
			stmt1.setString(9, "RLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();*/
			
			String temp_bu_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,comp_cd,"B","P"+bu_unit, "RM", "RLNG","Y");
			bu_contact_person_cd=temp_bu_contact_person_cd.equals("")?"0":temp_bu_contact_person_cd;
			
			//financial_year=utilDate.getFinancialYear(period_end_dt);

			/*String fin_yr="";
			if(!financial_year.equals("") && !financial_year.equals("-"))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}

			queryString3="SELECT NVL(MAX(INVOICE_SEQ),0) "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
			//HP20230915 CONTRACT_TYPE ADDED
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, financial_year);
			stmt3.setString(3, invoice_type);
			stmt3.setString(4, contract_type);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				invoice_seq=""+(rset3.getInt(1)+1);
			}
			rset3.close();
			stmt3.close();

			if(!invoice_seq.equals("") && !contract_type.equals("") && (!invoice_type.equals("") && !invoice_type.equals("0")))
			{
				String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
				
				invoice_no=invoice_prefix+""+contract_type+invoice_type+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
			}*/

			VLINE_NO.add("1");
			VLINE_DESC.add("");
			VUNIT.add("");
			VQTY.add("");
			VRATE.add("");
			VAMOUNT.add("");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getInvoiceNo()
	{
		String function_nm="getInvoiceNo()";
		try
		{
			queryString="SELECT INVOICE_NO,SYS_INV_NO "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND INV_FLAG NOT IN ('CR','DR')";
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				queryString+="AND CARGO_NO=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, bu_unit);
			//stmt.setString(7, month+"/"+year);
			//stmt.setString(8, month+"/"+year);
			stmt.setString(7, "A");
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				stmt.setString(8, cargo_no);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VLINK_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VSYS_LINK_INVOICE_NO.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();

			queryString1="SELECT INVOICE_NO,SYS_INV_NO "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND INV_FLAG NOT IN ('CR','DR')";
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				queryString1+="AND CARGO_NO=? ";
			}
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, agmt_no);
			stmt1.setString(4, cont_no);
			stmt1.setString(5, contract_type);
			stmt1.setString(6, bu_unit);
			//stmt1.setString(7, month+"/"+year);
			//stmt1.setString(8, month+"/"+year);
			stmt1.setString(7, "A");
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				stmt1.setString(8, cargo_no);
			}
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VLINK_INVOICE_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
				VSYS_LINK_INVOICE_NO.add(rset1.getString(2)==null?"":rset1.getString(2));
			}
			rset1.close();
			stmt1.close();

			queryString2="SELECT INVOICE_NO,INVOICE_REF "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=? ";
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				queryString2+="AND CARGO_NO=? ";
			}
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, agmt_no);
			stmt2.setString(4, cont_no);
			stmt2.setString(5, contract_type);
			stmt2.setString(6, bu_unit);
			//stmt2.setString(7, month+"/"+year);
			//stmt2.setString(8, month+"/"+year);
			stmt2.setString(7, "A");
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
				stmt2.setString(8, cargo_no);
			}
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				VLINK_INVOICE_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
				VSYS_LINK_INVOICE_NO.add(rset2.getString(2)==null?"":rset2.getString(2));
			}
			rset2.close();
			stmt2.close();

		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getF_FlowInvoiceList()
	{
		String function_nm="getF_FlowInvoiceList()";
		try
		{
			queryString="SELECT FINANCIAL_YEAR,INVOICE_CATEGORY,INVOICE_TYPE,INVOICE_SEQ,INVOICE_NO,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "BU_UNIT,ADDR_FLAG,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_REF,"
					+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FREQ "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, month+"/"+year);
			stmt.setString(4, month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VFINANCIAL_YEAR.add(rset.getString(1)==null?"":rset.getString(1));
				String invoice_category = rset.getString(2)==null?"":rset.getString(2);
				String invoice_type = rset.getString(3)==null?"":rset.getString(3);
				VINVOICE_TYPE.add(invoice_type);
				VINVOICE_SEQ.add(rset.getString(4)==null?"":rset.getString(4));
				VINVOICE_NO.add(rset.getString(5)==null?"":rset.getString(5));

				if(invoice_category.equals("P"))
				{
					VINVOICE_CATEGORY.add("Product");
				}
				else if(invoice_category.equals("S"))
				{
					VINVOICE_CATEGORY.add("Service");
				}
				else
				{
					VINVOICE_CATEGORY.add("");
				}

				if(invoice_type.equals("CR"))
				{
					VINVOICE_TYPE_NM.add("Credit Note");
				}
				else if(invoice_type.equals("DR"))
				{
					VINVOICE_TYPE_NM.add("Debit Note");
				}
				else if(invoice_type.equals("LP"))
				{
					VINVOICE_TYPE_NM.add("Late Payment Invoice");
				}
				else if(invoice_type.equals("OR"))
				{
					VINVOICE_TYPE_NM.add("Other");
				}
				else
				{
					VINVOICE_TYPE_NM.add("");
				}

				VPERIOD_START_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VPERIOD_END_DT.add(rset.getString(7)==null?"":rset.getString(7));

				String bu_plant_seq = rset.getString(8)==null?"":rset.getString(8);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");

				VSEL_BU_PLANT_SEQ_NO.add(bu_plant_seq);
				VSEL_BU_PLANT_ABBR.add(bu_plant_abbr);

				String add_type = rset.getString(9)==null?"":rset.getString(9);
				if(add_type.equals("R"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Registered");
				}
				else if(add_type.equals("C"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Correspondence");
				}
				else if(add_type.equals("B"))
				{
					VADDRESS_TYPE.add(add_type);
					VADDRESS_NAME.add("Billing");
				}
				else if(add_type.startsWith("P"))
				{
					VADDRESS_TYPE.add(add_type);
					String pseq = add_type.substring(1,add_type.length());
					String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, pseq, "T");
					VADDRESS_NAME.add(plant_abbr);
				}
				else
				{
					VADDRESS_TYPE.add("");
					VADDRESS_NAME.add("");
				}

				String chk_flg = rset.getString(10)==null?"":rset.getString(10);
				String auth_flg = rset.getString(11)==null?"":rset.getString(11);
				String aprv_flg = rset.getString(12)==null?"":rset.getString(12);

				VSTATUS.add(""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg));
				VINVOICE_REF.add(rset.getString(13)==null?"":rset.getString(13));

				String agmtno=rset.getString(14)==null?"0":rset.getString(14);
				String agmtrev=rset.getString(15)==null?"0":rset.getString(15);
				String contno=rset.getString(16)==null?"0":rset.getString(16);
				String contrev=rset.getString(17)==null?"0":rset.getString(17);
				String cont_type=rset.getString(18)==null?"":rset.getString(18);

				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;
				VMAPPING_ID.add(mapping_id);
				VFREQ.add(rset.getString(19)==null?"":rset.getString(19));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public String getInvoiceStatus(String chk, String auth, String app)
	{
		String function_nm="getInvoiceStatus()";
		String nm="";
		try
		{
			if(app.equals("A"))
			{
				nm="Approved";
			}
			else if(auth.equals("Y"))
			{
				nm="Authorized";
			}
			else if(chk.equals("Y"))
			{
				nm="Checked";
			}
			else
			{
				nm="Prepared";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}

	public void getExixtingF_FLowInvoice()
	{
		String function_nm="getExixtingF_FLowInvoice()";
		try
		{
			queryString="SELECT INVOICE_SEQ, INVOICE_NO, INVOICE_REF, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "INVOICE_CATEGORY, INVOICE_TYPE, "
					+ "FINANCIAL_YEAR, NUM_LINE, LINKED_INVOICE, NOTE, "
					+ "CONTACT_PERSON_CD,BU_CONTACT_PERSON_CD, "
					+ "CHECKED_FLAG, CHECKED_BY, TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "AUTHORIZED_FLAG, AUTHORIZED_BY, TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "APPROVED_FLAG, APPROVED_BY, TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "OTHER_INV_STR,GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INVOICE_RAISED_IN,AMT_WORD,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_AMT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_AMT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_TDS,ALLOC_QTY,INV_HEAD,QTY_UNIT "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_no);
			stmt.setString(4, financial);
			stmt.setString(5, invoice_type);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				submission_chk=true;

				invoice_seq=rset.getString(1)==null?"":rset.getString(1);
				invoice_no=rset.getString(2)==null?"":rset.getString(2);
				invoice_ref=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_category=rset.getString(6)==null?"":rset.getString(6);
				invoice_type=rset.getString(7)==null?"":rset.getString(7);
				financial_year=rset.getString(8)==null?"":rset.getString(8);
				num_line=rset.getString(9)==null?"":rset.getString(9);
				linked_invoice=rset.getString(10)==null?"":rset.getString(10);
				note=rset.getString(11)==null?"":rset.getString(11);
				contact_person_cd=rset.getString(12)==null?"0":rset.getString(12);
				bu_contact_person_cd=rset.getString(13)==null?"0":rset.getString(13);

				invoice_check_flag=rset.getString(14)==null?"":rset.getString(14);
				String chk_by=rset.getString(15)==null?"":rset.getString(15);
				invoice_check_by=utilBean.getEmpName(conn,chk_by);
				invoice_check_dt=rset.getString(16)==null?"":rset.getString(16);
				if(invoice_check_flag.equals("Y"))
				{
					invoice_check_nm="Checked";
				}
				else if(invoice_check_flag.equals("N"))
				{
					invoice_check_nm="Rejected";
				}
				invoice_auth_flag=rset.getString(17)==null?"":rset.getString(17);
				String auth_by=rset.getString(18)==null?"":rset.getString(18);
				invoice_auth_by=utilBean.getEmpName(conn,auth_by);
				invoice_auth_dt=rset.getString(19)==null?"":rset.getString(19);
				if(invoice_auth_flag.equals("Y"))
				{
					invoice_auth_nm="Authorized";
				}
				else if(invoice_auth_flag.equals("N"))
				{
					invoice_auth_nm="Rejected";
				}

				invoice_aprv_flag=rset.getString(20)==null?"":rset.getString(20);
				String aprv_by=rset.getString(21)==null?"":rset.getString(21);
				invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
				invoice_aprv_dt=rset.getString(22)==null?"":rset.getString(22);
				if(invoice_aprv_flag.equals("A"))
				{
					invoice_aprv_nm="Approved";
				}
				else if(invoice_aprv_flag.equals("R"))
				{
					invoice_aprv_nm="Rejected";
				}

				other_inv_str =rset.getString(23)==null?"":rset.getString(23);

				gross_amt =rset.getString(24)==null?"":nf.format(rset.getDouble(24));
				exchang_rate =rset.getString(25)==null?"":rset.getString(25);
				gross_amt1 =rset.getString(26)==null?"":nf.format(rset.getDouble(26));
				tax_amt =rset.getString(27)==null?"":nf.format(rset.getDouble(27));
				invoice_amt =rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				invoice_adj_amt =rset.getString(29)==null?"":nf.format(rset.getDouble(29));
				net_payable =rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				invoice_raised_in =rset.getString(31)==null?"":rset.getString(31);
				amount_in_word =rset.getString(32)==null?"":rset.getString(32);
				tax_struct_cd =rset.getString(33)==null?"":rset.getString(33);
				tax_struct_dt =rset.getString(34)==null?"":rset.getString(34);
				
				tds_amount =rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				tds_struct_cd =rset.getString(36)==null?"":rset.getString(36);
				tds_struct_dt =rset.getString(37)==null?"":rset.getString(37);
				
				tcs_amount =rset.getString(38)==null?"":nf.format(rset.getDouble(38));
				tcs_struct_cd =rset.getString(39)==null?"":rset.getString(39);
				tcs_struct_dt =rset.getString(40)==null?"":rset.getString(40);
				
				applicable_abbr =rset.getString(41)==null?"":rset.getString(41);
				qty_mmbtu =rset.getString(42)==null?"":nf.format(rset.getDouble(42));
				invoice_head =rset.getString(43)==null?"":rset.getString(43);
				energy_unit =rset.getString(44)==null?"1":rset.getString(44);
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				tcs_struct_info=utilBean.getTaxDescr(conn,tcs_struct_cd);
				tds_struct_info=utilBean.getTaxDescr(conn,tds_struct_cd);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				queryString4="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, contract_type);
				stmt4.setString(3, invoice_type);
				stmt4.setString(4, financial_year);
				stmt4.setString(5, invoice_seq);
				rset4=stmt4.executeQuery();
				while(rset4.next())
				{
					VTAX_CODE.add(rset4.getString(1)==null?"":rset4.getString(1));
					VTAX_DESCR.add(rset4.getString(2)==null?"":rset4.getString(2));
					VTAX_AMT.add(rset4.getString(3)==null?"":nf.format(rset4.getDouble(3)));
					VTAX_BASE_AMT.add(rset4.getString(4)==null?"":nf.format(rset4.getDouble(4)));
				}
				rset4.close();
				stmt4.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				
				VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
			}
			rset.close();
			stmt.close();


			queryString1="SELECT LINE_NO,LINE_DESC,UNIT,QTY,RATE,AMOUNT "
					+ "FROM FMS_PUR_FFLOW_INV_DTL "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND INVOICE_NO=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
					+ "AND CONTRACT_TYPE=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, inv_seq);
			stmt1.setString(3, inv_no);
			stmt1.setString(4, financial);
			stmt1.setString(5, invoice_type);
			stmt1.setString(6, contract_type);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VLINE_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
				VLINE_DESC.add(rset1.getString(2)==null?"":rset1.getString(2));
				VUNIT.add(rset1.getString(3)==null?"":rset1.getString(3));
				VQTY.add(rset1.getString(4)==null?"":rset1.getString(4));
				VRATE.add(rset1.getString(5)==null?"":rset1.getString(5));
				VAMOUNT.add(rset1.getString(6)==null?"":rset1.getString(6));
			}
			rset1.close();
			stmt1.close();

			if(VLINE_NO.size()==0)
			{
				VLINE_NO.add("1");
				VLINE_DESC.add("");
				VUNIT.add("");
				VQTY.add("");
				VRATE.add("");
				VAMOUNT.add("");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDetail()
	{
		String function_nm="getInvoiceDetail()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
			
			String countptyCd="";
			String contRev="";
			String splitValue="";
			
			String boe_number="";
			String boe_date="";
			String ship_nm="";
			if(contract_type.equals("N"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.SIGNING_DT,'DD-MON-YY'),B.CARGO_REF  "
						+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? AND A.AGMT_TYPE=? "
						+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND A.COUNTERPARTY_CD=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, "M");
				stmt.setString(4, cont_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, counterparty_cd);
				stmt.setString(7, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signing_dt=rset.getString(2)==null?"":rset.getString(2);
					cargoRef=rset.getString(3)==null?"":rset.getString(3);
				}
				rset.close();
				stmt.close();
				
				queryString="SELECT B.BOE_REF,TO_CHAR(B.BOE_DT,'DD-MON-YY'),A.SHIP_CD "
						+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_BUY_CARGO_ALLOC_BOE B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CARGO_NO=? AND B.BOE_NO=? "
						+ "AND A.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CARGO_NO=B.CARGO_NO AND A.ALLOC_REV=B.ALLOC_REV";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "M");
				stmt.setString(4, agmt_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, cargo_no);
				stmt.setString(8, boe_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					boe_number = rset.getString(1)==null?"":rset.getString(1);
					boe_date=rset.getString(2)==null?"":rset.getString(2);
					String ship_cd=rset.getString(3)==null?"":rset.getString(3);
					ship_nm=utilBean.getShipName(conn,ship_cd);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_TRADER_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_TRADER_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "M");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				top_heading_nm=""+couterpty_nm;
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.SIGNING_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "T");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "L");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signing_dt=rset.getString(2)==null?"":rset.getString(2);
					cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "L");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "T");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				top_heading_nm=""+couterpty_nm;
			}
			else
			{
				queryString="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),TRADE_REF_NO,CONT_REV,COUNTERPARTY_CD,SPLIT_FLAG  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? " //AND COUNTERPARTY_CD='"+counterparty_cd+"' "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signing_dt=rset.getString(2)==null?"":rset.getString(2);
					
					String trade_ref=rset.getString(3)==null?"":rset.getString(3);
					if(contract_type.equals("I"))
					{
						contRef=trade_ref;
					}
					
					contRev=rset.getString(4)==null?"":rset.getString(4);
					countptyCd=rset.getString(5)==null?"":rset.getString(5);
					splitValue=rset.getString(6)==null?"":rset.getString(6);
				}
				rset.close();
				stmt.close();
				
				if(splitValue.equals("Y"))
				{
					String nm=""+utilBean.getCounterpartyName(conn,countptyCd);
					top_heading_nm=""+nm;
					
					queryString1="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
							+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countptyCd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, contRev);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					stmt1.setString(7, contract_type);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String cd=rset1.getString(1)==null?"":rset1.getString(1);
						nm=""+utilBean.getCounterpartyName(conn,cd);
						if(top_heading_nm.equals(""))
						{
							top_heading_nm+=""+nm;
						}
						else
						{
							top_heading_nm+=" and "+nm;
						}
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					top_heading_nm=""+couterpty_nm;
				}
			}
			
			str_cargoname="Cargo Name - "+ship_nm+" (BOE "+boe_number+" Dated "+boe_date+")";
			if(boe_number.equals(""))
			{
				str_cargoname="Cargo Name - "+ship_nm;
			}
			
			queryString1="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "T");
			stmt1.setString(4, contact_person_cd);
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "RLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				contact_person_nm=rset1.getString(1)==null?"":rset1.getString(1);
			} 
			rset1.close();
			stmt1.close();
			
			queryString2="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, "B");
			stmt2.setString(4, bu_contact_person_cd);
			stmt2.setString(5, "P"+bu_unit);
			stmt2.setString(6, "RLNG");
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				bu_contact_person_nm=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
            bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			if(contract_type.equals("N"))
			{
				queryString5 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, counterparty_cd);
				stmt5.setString(2, "T");
				stmt5.setString(3, plant_seq);
				stmt5.setString(4, comp_cd);
				stmt5.setString(5, "1001");
				rset5=stmt5.executeQuery();
				//AS MENTIONED BY VIJAY, ONLY SHOWS PAN DETAIL FRO CARGO REMITTANCE ON 01/07/2024 OVER TEAMS CHAT
				while(rset5.next())
				{
					String no = rset5.getString(1)==null?"":rset5.getString(1);
					String dt = rset5.getString(2)==null?"":rset5.getString(2);
					String nm = rset5.getString(3)==null?"":rset5.getString(3);
					
					if(tax_info.equals(""))
					{
						tax_info+=""+nm+" : "+no;
					}
					else
					{
						tax_info+="<br>"+nm+" : "+no;
					}
				}
				rset5.close();
				stmt5.close();
				
				queryString6 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, comp_cd);
				stmt6.setString(2, "B");
				stmt6.setString(3, bu_unit);
				stmt6.setString(4, comp_cd);
				stmt6.setString(5, "1001");
				rset6=stmt6.executeQuery();
				while(rset6.next())
				{
					String no = rset6.getString(1)==null?"":rset6.getString(1);
					String dt = rset6.getString(2)==null?"":rset6.getString(2);
					String nm = rset6.getString(3)==null?"":rset6.getString(3);
					
					if(bu_tax_info.equals(""))
					{
						bu_tax_info+=""+nm+" : "+no;
					}
					else
					{
						bu_tax_info+="<br>"+nm+" : "+no;
					}
				}
				rset6.close();
				stmt6.close();
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "T", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
				
				if(contract_type.equals("G") || contract_type.equals("P"))
				{
					bu_tax_info+="<br>SAC : 999799"
							+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
							+ "<br>Place Of Supply : "+bu_plantState;
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExistingInvoiceDataForPDFPrint()
	{
		String function_nm="getExistingInvoiceDataForPDFPrint()";
		try
		{
			if(inv_type.equals("P"))
			{
				queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
						+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
						+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,SYS_INV_NO,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
						+ "SUG_PERCENT,SUG_QTY,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT "
						+ "FROM FMS_PUR_PG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? ";
				if(!inv_flag.equals("UG")) {
					queryString+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(contract_type.equals("N"))
				{
					queryString+="AND CARGO_NO=? AND BOE_NO=? ";
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					queryString+="AND CARGO_NO=? ";
				}
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, counterparty_cd);
				stmt.setString(++stcount, cont_no);
				stmt.setString(++stcount, agmt_no);
				stmt.setString(++stcount, plant_seq);
				stmt.setString(++stcount, bu_unit);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, financial_year);
				stmt.setString(++stcount, invoice_seq);
				stmt.setString(++stcount, inv_flag);
				if(!inv_flag.equals("UG")) {
					stmt.setString(++stcount, billing_cycle);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
				}
				if(contract_type.equals("N"))
				{
					stmt.setString(++stcount, cargo_no);
					stmt.setString(++stcount, boe_no);
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					stmt.setString(++stcount, cargo_no);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
					pg_invoice_seq=rset.getString(3)==null?"":rset.getString(3);
					pg_invoice_no=rset.getString(4)==null?"":rset.getString(4);
					pg_invoice_dt=rset.getString(5)==null?"":rset.getString(5);
					pg_invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
					pg_qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));//
					pg_price_cd=rset.getString(9)==null?"":rset.getString(9);
					pg_price=utilBean.RateNumberFormat(rset.getDouble(8), pg_price_cd);
					pg_gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));//
					pg_exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					pg_exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
					pg_exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));//
					pg_invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					pg_gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
					pg_tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));//
					pg_tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
					pg_tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
					pg_invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));//
					pg_net_payable=rset.getString(22)==null?"":nf.format(rset.getDouble(22));//
					pg_invoice_check_flag=rset.getString(23)==null?"":rset.getString(23);
					String chk_by=rset.getString(24)==null?"":rset.getString(24);
					pg_invoice_check_by=utilBean.getEmpName(conn,chk_by);
					pg_invoice_check_dt=rset.getString(25)==null?"":rset.getString(25);
					if(pg_invoice_check_flag.equals("Y"))
					{
						pg_invoice_check_nm="Checked";
					}
					else if(pg_invoice_check_flag.equals("N"))
					{
						pg_invoice_check_nm="Rejected";
					}
					pg_invoice_auth_flag=rset.getString(26)==null?"":rset.getString(26);
					String auth_by=rset.getString(27)==null?"":rset.getString(27);
					pg_invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					pg_invoice_auth_dt=rset.getString(28)==null?"":rset.getString(28);
					if(pg_invoice_auth_flag.equals("Y"))
					{
						pg_invoice_auth_nm="Authorized";
					}
					else if(pg_invoice_auth_flag.equals("N"))
					{
						pg_invoice_auth_nm="Rejected";
					}

					pg_invoice_aprv_flag=rset.getString(29)==null?"":rset.getString(29);
					String aprv_by=rset.getString(30)==null?"":rset.getString(30);
					pg_invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					pg_invoice_aprv_dt=rset.getString(31)==null?"":rset.getString(31);
					if(pg_invoice_aprv_flag.equals("A"))
					{
						pg_invoice_aprv_nm="Approved";
					}
					else if(pg_invoice_aprv_flag.equals("R"))
					{
						pg_invoice_aprv_nm="Rejected";
					}

					pg_invoice_adj_sign=rset.getString(32)==null?"":rset.getString(32);
					pg_invoice_adj_amt=rset.getString(33)==null?"":nf.format(rset.getDouble(33));//
					pg_financial_year=rset.getString(34)==null?"":rset.getString(34);
					
					pg_tcs_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
					pg_tcs_factor=rset.getString(36)==null?"":nf3.format(rset.getDouble(36));
					pg_tcs_struct_cd=rset.getString(37)==null?"":rset.getString(37);
					pg_tcs_struct_dt=rset.getString(38)==null?"":rset.getString(38);
					applicable_flag=rset.getString(39)==null?"":rset.getString(39);
					applicable_abbr=rset.getString(40)==null?"":rset.getString(40);
					pg_sys_invoice_no=rset.getString(41)==null?"":rset.getString(41);
					
					pg_cif_value=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					pg_assessable_vlaue=rset.getString(43)==null?"":nf.format(rset.getDouble(43));
					pg_remark=rset.getString(44)==null?"":rset.getString(44);
					pg_diff_price=utilBean.RateNumberFormat(rset.getDouble(45), pg_price_cd);
					pg_cd_paid_amt=rset.getString(46)==null?"":nf.format(rset.getDouble(46));
					
					//47
					//48
					
					energy_unit=rset.getString(49)==null?"1":rset.getString(49);
					energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
					if(energy_unit.equals("0") && energy_unit_nm.equals(""))
					{
						energy_unit_nm="Lumpsum";
					}
					
					transportation_charges=rset.getString(50)==null?"":nf2.format(rset.getDouble(50));
					pg_transportation_amount=rset.getString(51)==null?"":nf.format(rset.getDouble(51));
					marketing_margin=rset.getString(52)==null?"":nf2.format(rset.getDouble(52));
					pg_marketing_margin_amount=rset.getString(53)==null?"":nf.format(rset.getDouble(53));
					other_charges=rset.getString(54)==null?"":nf2.format(rset.getDouble(54));
					pg_other_charges_amount=rset.getString(55)==null?"":nf.format(rset.getDouble(55));
					
					pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_amt1));
					if(!pg_transportation_amount.equals(""))
					{
						pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_transportation_amount));
						isGrossIncTriff=true;
					}
					if(!pg_marketing_margin_amount.equals(""))
					{
						pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_marketing_margin_amount));
						isGrossIncTriff=true;
					}
					if(!pg_other_charges_amount.equals(""))
					{
						pg_gross_include_transport_tariff=nf.format(Double.parseDouble(pg_gross_include_transport_tariff) + Double.parseDouble(pg_other_charges_amount));
						isGrossIncTriff=true;
					}
					
					pg_tax_struct_dtl=utilBean.getTaxDescr(conn,pg_tax_struct_cd);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
						+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
						+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,SYS_INV_NO,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,"
						+ "SUG_PERCENT,SUG_QTY,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? ";
				if(!inv_flag.equals("UG")) {
					queryString+="AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
				}
				if(contract_type.equals("N"))
				{
					queryString+="AND CARGO_NO=? AND BOE_NO=? ";
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					queryString+="AND CARGO_NO=? ";
				}
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, counterparty_cd);
				stmt.setString(++stcount, cont_no);
				stmt.setString(++stcount, agmt_no);
				stmt.setString(++stcount, plant_seq);
				stmt.setString(++stcount, bu_unit);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, financial_year);
				stmt.setString(++stcount, invoice_seq);
				stmt.setString(++stcount, inv_flag);
				if(!inv_flag.equals("UG")) {
					stmt.setString(++stcount, billing_cycle);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
				}
				if(contract_type.equals("N"))
				{
					stmt.setString(++stcount, cargo_no);
					stmt.setString(++stcount, boe_no);
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					stmt.setString(++stcount, cargo_no);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
					//invoice_seq=rset.getString(3)==null?"":rset.getString(3);
					invoice_no=rset.getString(4)==null?"":rset.getString(4);
					invoice_dt=rset.getString(5)==null?"":rset.getString(5);
					invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));//
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));//
					exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
					exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));//
					invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
					tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));//
					tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
					tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
					invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));//
					net_payable=rset.getString(22)==null?"":nf.format(rset.getDouble(22));//
					invoice_check_flag=rset.getString(23)==null?"":rset.getString(23);
					String chk_by=rset.getString(24)==null?"":rset.getString(24);
					invoice_check_by=utilBean.getEmpName(conn,chk_by);
					invoice_check_dt=rset.getString(25)==null?"":rset.getString(25);
					if(invoice_check_flag.equals("Y"))
					{
						invoice_check_nm="Checked";
					}
					else if(invoice_check_flag.equals("N"))
					{
						invoice_check_nm="Rejected";
					}
					invoice_auth_flag=rset.getString(26)==null?"":rset.getString(26);
					String auth_by=rset.getString(27)==null?"":rset.getString(27);
					invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					invoice_auth_dt=rset.getString(28)==null?"":rset.getString(28);
					if(invoice_auth_flag.equals("Y"))
					{
						invoice_auth_nm="Authorized";
					}
					else if(invoice_auth_flag.equals("N"))
					{
						invoice_auth_nm="Rejected";
					}

					invoice_aprv_flag=rset.getString(29)==null?"":rset.getString(29);
					String aprv_by=rset.getString(30)==null?"":rset.getString(30);
					invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					invoice_aprv_dt=rset.getString(31)==null?"":rset.getString(31);
					if(invoice_aprv_flag.equals("A"))
					{
						invoice_aprv_nm="Approved";
					}
					else if(invoice_aprv_flag.equals("R"))
					{
						invoice_aprv_nm="Rejected";
					}

					invoice_adj_sign=rset.getString(32)==null?"":rset.getString(32);
					invoice_adj_amt=rset.getString(33)==null?"":nf.format(rset.getDouble(33));//
					//financial_year=rset.getString(34)==null?"":rset.getString(34);
					
					tcs_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
					tcs_factor=rset.getString(36)==null?"":nf3.format(rset.getDouble(36));
					tcs_struct_cd=rset.getString(37)==null?"":rset.getString(37);
					tcs_struct_dt=rset.getString(38)==null?"":rset.getString(38);
					applicable_flag=rset.getString(39)==null?"":rset.getString(39);
					applicable_abbr=rset.getString(40)==null?"":rset.getString(40);
					sys_invoice_no=rset.getString(41)==null?"":rset.getString(41);
					
					cif_value=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					assessable_vlaue=rset.getString(43)==null?"":nf.format(rset.getDouble(43));
					remark=rset.getString(44)==null?"":rset.getString(44);
					diff_price=utilBean.RateNumberFormat(rset.getDouble(45), price_cd);
					cd_paid_amt=rset.getString(46)==null?"":nf.format(rset.getDouble(46));
					
					sug_percentage=rset.getString(47)==null?"":nf.format(rset.getDouble(47));
					sug_qty=rset.getString(48)==null?"":nf.format(rset.getDouble(48));
					energy_unit=rset.getString(49)==null?"1":rset.getString(49);
					energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
					if(energy_unit.equals("0") && energy_unit_nm.equals(""))
					{
						energy_unit_nm="Lumpsum";
					}
					
					transportation_charges=rset.getString(50)==null?"":nf2.format(rset.getDouble(50));
					transportation_amount=rset.getString(51)==null?"":nf.format(rset.getDouble(51));
					marketing_margin=rset.getString(52)==null?"":nf2.format(rset.getDouble(52));
					marketing_margin_amount=rset.getString(53)==null?"":nf.format(rset.getDouble(53));
					other_charges=rset.getString(54)==null?"":nf2.format(rset.getDouble(54));
					other_charges_amount=rset.getString(55)==null?"":nf.format(rset.getDouble(55));
					
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
					if(!transportation_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
					if(!marketing_margin_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
					if(!other_charges_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
					
					tax_struct_dtl=utilBean.getTaxDescr(conn,tax_struct_cd);
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSendInvoiceMailDetail()
	{
		String function_nm="getSendInvoiceMailDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ownAddress  = rset.getString(1)==null?"":rset.getString(1);
				ownCity  = rset.getString(2)==null?"":rset.getString(2);
				ownState  = rset.getString(3)==null?"":rset.getString(3);
				ownPin  = rset.getString(4)==null?"":rset.getString(4);
				ownCountry  = rset.getString(5)==null?"":rset.getString(5);
				ownPhone  = rset.getString(6)==null?"":rset.getString(6);
				ownEmail  = rset.getString(7)==null?"":rset.getString(7);
			}
			rset.close();
			stmt.close();
			
			String bu_contact_person_cd="";
			String invoiceNo="";
			String invoiceDt="";
			String dueDate="";
			
			if(mail_inv_type.equals("F"))
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_PUR_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND FINANCIAL_YEAR=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_SEQ=? AND INVOICE_TYPE=?";
			}
			else
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') ";
				if(mail_inv_type.equals("P"))
				{
					queryString1+= "FROM FMS_PUR_PG_INV_MST ";
				}
				else
				{
					queryString1+= "FROM FMS_PUR_SG_INV_MST ";
				}
				
				queryString1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? "
						+ "AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
				if(contract_type.equals("N")) {
					queryString1+=" AND CARGO_NO=? AND BOE_NO=? ";
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					queryString1+=" AND CARGO_NO=? ";
				}
			}
			int st_count=0;
			stmt1=conn.prepareStatement(queryString1);
			if(mail_inv_type.equals("F"))
			{
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterparty_cd);
				stmt1.setString(++st_count, financial_year);
				stmt1.setString(++st_count, contract_type);
				stmt1.setString(++st_count, invoice_seq);
				stmt1.setString(++st_count, invoice_type);
			}
			else
			{
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterparty_cd);
				stmt1.setString(++st_count, cont_no);
				stmt1.setString(++st_count, agmt_no);
				stmt1.setString(++st_count, plant_seq);
				stmt1.setString(++st_count, bu_unit);
				stmt1.setString(++st_count, contract_type);
				stmt1.setString(++st_count, invoice_seq);
				stmt1.setString(++st_count, financial_year);
				stmt1.setString(++st_count, inv_flag);
				if(contract_type.equals("N")) {
					stmt1.setString(++st_count, cargo_no);
					stmt1.setString(++st_count, boe_no);
				}else if(contract_type.equals("G") || contract_type.equals("P")) {
					stmt1.setString(++st_count, cargo_no);
				}
			}
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
				invoiceNo=rset1.getString(2)==null?"":rset1.getString(2);
				dueDate=rset1.getString(3)==null?"":rset1.getString(3);
				invoiceDt=rset1.getString(4)==null?"":rset1.getString(4);
			}
			rset1.close();
			stmt1.close();
				
			String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
			String subject="";
			String type="";
				
			String to_list="";
			String cc_list="";
			if(mail_inv_type.equals("F"))
			{
				to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", plant_seq, "RM", "RLNG","Y");
				cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", plant_seq, "RM", "RLNG","N");
			}
			else
			{
				to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, "RM", "RLNG","Y");
				cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, "RM", "RLNG","N");
			}
			String tmpToList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","Y");
			to_list+=to_list.equals("")?tmpToList:","+tmpToList;
		
			String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","N");
			cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
			
			//get BCc list
			String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","B");
			
			Vector VTEMP_ATTACHMENT=new Vector();
			st_count=0;
			if(mail_inv_type.equals("F"))
			{
				queryString4="SELECT FILE_NAME "
						+ "FROM FMS_PUR_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND INV_TITLE!=? AND INVOICE_TYPE=?";
			}
			else
			{
				queryString4="SELECT FILE_NAME "
						+ "FROM FMS_PUR_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE!=? AND INV_FLAG=?";
			}
			stmt4=conn.prepareStatement(queryString4);
			if(mail_inv_type.equals("F"))
			{
				stmt4.setString(++st_count, comp_cd);
				stmt4.setString(++st_count, contract_type);
				stmt4.setString(++st_count, invoice_seq);
				stmt4.setString(++st_count, financial_year);
				stmt4.setString(++st_count, "X");
				stmt4.setString(++st_count, invoice_type);
				
			}
			else
			{
				stmt4.setString(++st_count, comp_cd);
				stmt4.setString(++st_count, contract_type);
				stmt4.setString(++st_count, invoice_seq);
				stmt4.setString(++st_count, financial_year);
				stmt4.setString(++st_count, "X");
				stmt4.setString(++st_count, inv_flag);
			}
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				VTEMP_ATTACHMENT.add(rset4.getString(1)==null?"":rset4.getString(1));
			}
			rset4.close();
			stmt4.close();
			
			String mail_keyword="Remittance Advice";
			/*HP20250408 AS PER MAIL FROM JD/VIJAY
			String mail_keyword="Purchase Remittance";
			if(inv_flag.equals("CP"))
			{
				mail_keyword="Custom Duty Challan (Provisional) Remittance";
			}
			else if(inv_flag.equals("CF"))
			{
				mail_keyword="Custom Duty Challan (Final) Remittance";
			}
			else if(inv_flag.equals("P") && contract_type.equals("N"))
			{
				mail_keyword="Cargo Purchase (Provisional) Remittance";
			}
			else if(inv_flag.equals("F") && contract_type.equals("N"))
			{
				mail_keyword="Cargo Purchase (Final) Remittance";
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				if(inv_flag.equals("UG"))
				{
					mail_keyword="LTCORA SUG Purchase Remittance";
				}
				else
				{
					mail_keyword="LTCORA Purchase Remittance";
				}
			}
			else if(contract_type.equals("Y"))
			{
				mail_keyword="Surveyor Service Remittance";
			}
			else if(contract_type.equals("A"))
			{
				mail_keyword="Vessel Agent Service Remittance";
			}
			else if(contract_type.equals("H"))
			{
				if(inv_flag.equals("P"))
				{
					mail_keyword="Custom House Agent Provisional Service Remittance";
				}
				else
				{	
					mail_keyword="Custom House Agent Service Remittance";
				}
			}
			*/
			//subject="Purchase Remittance# " +invoiceNo+ " for " +customerNm;
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
			subject=companyAbbr+"/"+mail_keyword+"/"+customerNm+"/"+invoiceNo+"/"+financial_year;

			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed "+mail_keyword+" for " +customerNm+" Invoice : "+invoiceNo
					+ " dated "+invoiceDt.replaceAll("/", "-")+"."
					+ "\nPlease note payment will be processed on or before "+dueDate.replaceAll("/", "-")
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""	
					+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";	
			
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(VTEMP_ATTACHMENT);
			if(mail_inv_type.equals("F"))
			{
				VMAIL_ATTACHMENT_PATH.add(CommonVariable.purchase_freeflow_inv_path);
			}
			else
			{
				VMAIL_ATTACHMENT_PATH.add(CommonVariable.purchase_inv_path);
			}
			VMAIL_BODY.add(mail_body);	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTcsTdsInvDtl()
	{
		String function_nm="getTcsTdsInvDtl()";
		try
		{
			double total=0;
			double gross_total=0;
			queryString="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "D");
			stmt.setString(4, "T");
			stmt.setString(5, financial_year);
			stmt.setString(6, "A");
			stmt.setString(7, invoice_dt);
			stmt.setString(8, "F");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmtno=rset.getString(1)==null?"":rset.getString(1);
				String contno=rset.getString(2)==null?"":rset.getString(2);
				String cont_type=rset.getString(3)==null?"0":rset.getString(3);
				double inv_amt=rset.getDouble(4);
				String inv_no=rset.getString(5)==null?"":rset.getString(5);
				String inv_dt=rset.getString(6)==null?"":rset.getString(6);
				String inv_raised_in=rset.getString(7)==null?"":rset.getString(7);
				double gross_amt=rset.getDouble(8);
				String exchng_rt_cd=rset.getString(9)==null?"":rset.getString(9);
				String due_dt=rset.getString(10)==null?"":rset.getString(10);
				
				String contRef="";
				String contpty_cd="";
				queryString1="SELECT CONT_REF_NO,COUNTERPARTY_CD  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "//AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				//stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					contpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, contpty_cd, agmtno, "0", contno, "0", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString2="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, exchng_rt_cd);
				stmt2.setString(2, due_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					exchng_rate=rset2.getDouble(1);
				}
				rset2.close();
				stmt2.close();
				
				VDEAL_NO.add(deal_no);
				VCONT_REF_NO.add(contRef);
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(inv_dt);
				
				if(inv_raised_in.equals("2"))
				{
					VINVOICE_AMT_USD.add(nf.format(inv_amt));
					VINVOICE_AMT.add(nf.format(inv_amt * exchng_rate));
					
					VGROSS_AMT.add(nf.format(gross_amt * exchng_rate));
					VGROSS_AMT_USD.add(nf.format(gross_amt));
				}
				else
				{
					VINVOICE_AMT.add(nf.format(inv_amt));
					VINVOICE_AMT_USD.add("");
					
					VGROSS_AMT.add(nf.format(gross_amt));
					VGROSS_AMT_USD.add("");
				}
				
				if(inv_raised_in.equals("2"))
				{
					total+=(inv_amt * exchng_rate);
					gross_total+=(gross_amt * exchng_rate);
				}
				else
				{
					total+=inv_amt;
					gross_total+=gross_amt;
				}
			}
			rset.close();
			stmt.close();
			
			queryString2="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "D");
			stmt2.setString(4, "T");
			stmt2.setString(5, financial_year);
			stmt2.setString(6, "A");
			stmt2.setString(7, invoice_dt);
			stmt2.setString(8, "F");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String agmtno=rset2.getString(1)==null?"":rset2.getString(1);
				String contno=rset2.getString(2)==null?"":rset2.getString(2);
				String cont_type=rset2.getString(3)==null?"0":rset2.getString(3);
				double inv_amt=rset2.getDouble(4);
				String inv_no=rset2.getString(5)==null?"":rset2.getString(5);
				String inv_dt=rset2.getString(6)==null?"":rset2.getString(6);
				String inv_raised_in=rset2.getString(7)==null?"":rset2.getString(7);
				double gross_amt=rset2.getDouble(8);
				String exchng_rt_cd=rset2.getString(9)==null?"":rset2.getString(9);
				String due_dt=rset2.getString(10)==null?"":rset2.getString(10);
				
				String contRef="";
				String contpty_cd="";
				queryString1="SELECT CONT_REF_NO,COUNTERPARTY_CD  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "//AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				//stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					contpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, contpty_cd, agmtno, "0", contno, "0", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString3="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, exchng_rt_cd);
				stmt3.setString(2, due_dt);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					exchng_rate=rset3.getDouble(1);
				}
				rset3.close();
				stmt3.close();
				
				VDEAL_NO.add(deal_no);
				VCONT_REF_NO.add(contRef);
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(inv_dt);
				
				if(inv_raised_in.equals("2"))
				{
					VINVOICE_AMT_USD.add(nf.format(inv_amt));
					VINVOICE_AMT.add(nf.format(inv_amt * exchng_rate));
					
					VGROSS_AMT.add(nf.format(gross_amt * exchng_rate));
					VGROSS_AMT_USD.add(nf.format(gross_amt));
				}
				else
				{
					VINVOICE_AMT.add(nf.format(inv_amt));
					VINVOICE_AMT_USD.add("");
					
					VGROSS_AMT.add(nf.format(gross_amt));
					VGROSS_AMT_USD.add("");
				}
				
				if(inv_raised_in.equals("2"))
				{
					total+=(inv_amt * exchng_rate);
					gross_total+=(gross_amt * exchng_rate);
				}
				else
				{
					total+=inv_amt;
					gross_total+=gross_amt;
				}
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT_INR,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT_USD "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?)";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, "D");
			stmt3.setString(4, "T");
			stmt3.setString(5, financial_year);
			stmt3.setString(6, "A");
			stmt3.setString(7, invoice_dt);
			stmt3.setString(8, "DR");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String agmtno=rset3.getString(1)==null?"":rset3.getString(1);
				String contno=rset3.getString(2)==null?"":rset3.getString(2);
				String cont_type=rset3.getString(3)==null?"0":rset3.getString(3);
				double inv_amt=rset3.getDouble(4);
				String inv_no=rset3.getString(5)==null?"":rset3.getString(5);
				String inv_dt=rset3.getString(6)==null?"":rset3.getString(6);
				String inv_raised_in=rset3.getString(7)==null?"":rset3.getString(7);
				double gross_amt_inr=rset3.getDouble(8);
				String exchng_rt_cd=rset3.getString(9)==null?"":rset3.getString(9);
				String due_dt=rset3.getString(10)==null?"":rset3.getString(10);
				double gross_amt_usd=rset3.getDouble(11);
				
				String contRef="";
				String contpty_cd="";
				queryString1="SELECT CONT_REF_NO,COUNTERPARTY_CD  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "//AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				//stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					contpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, contpty_cd, agmtno, "0", contno, "0", cont_type, "");
				
				double exchng_rate=0;
				//DISCUSSED WITH MAM, CONSIDER PAYMENT DUE DT FOR PICKING EXCHANGE RATE 20230725
				queryString4="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, exchng_rt_cd);
				stmt4.setString(2, due_dt);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					exchng_rate=rset4.getDouble(1);
				}
				rset4.close();
				stmt4.close();
				
				VDEAL_NO.add(deal_no);
				VCONT_REF_NO.add(contRef);
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(inv_dt);
				
				if(inv_raised_in.equals("2"))
				{
					VINVOICE_AMT_USD.add(nf.format(inv_amt));
					VINVOICE_AMT.add(nf.format(inv_amt * exchng_rate));
					
					VGROSS_AMT.add(nf.format(gross_amt_inr));
					VGROSS_AMT_USD.add(nf.format(gross_amt_usd));
				}
				else
				{
					VINVOICE_AMT.add(nf.format(inv_amt));
					VINVOICE_AMT_USD.add("");
					
					VGROSS_AMT.add(nf.format(gross_amt_inr));
					VGROSS_AMT_USD.add("");
				}
				
				if(inv_raised_in.equals("2"))
				{
					total+=(inv_amt * exchng_rate);
					gross_total+=(gross_amt_usd);
				}
				else
				{
					total+=inv_amt;
					gross_total+=gross_amt_inr;
				}
			}
			rset3.close();
			stmt3.close();
			
			total_InvoiceAmt=nf.format(total);
			total_GrossAmt=nf.format(gross_total);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraSugRemittancePreparationList()
	{
		String function_nm="getLtcoraSugRemittancePreparationList()";
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "A.CONTRACT_TYPE,B.CARGO_NO,A.CONT_REF_NO  "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_CHAR(B.QQ_DT,'MM/YYYY')=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "T");
			stmt.setString(3, "Y");
			stmt.setString(4, "Y");
			stmt.setString(5, "L");
			stmt.setString(6, month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"0":rset.getString(8);
				String contRef=rset.getString(9)==null?"":rset.getString(9);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				String countpty_nm=utilBean.getCounterpartyName(conn,countpty_cd);
				String countpty_abbr=utilBean.getCounterpartyABBR(conn,countpty_cd);
				
				VREMITTANCE_LIST_ABBR.add(deal_no);
				VREMITTANCE_LIST_NAME.add(countpty_nm+" ["+contRef+"] "+deal_no);
				
				int index=0;
				queryString1="SELECT D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.CONT_REV=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONTRACT_TYPE=? "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, "T");
				stmt1.setString(3, countpty_cd);
				stmt1.setString(4, contno);
				stmt1.setString(5, contrev);
				stmt1.setString(6, agmtno);
				stmt1.setString(7, agmtrev);
				stmt1.setString(8, "L");
				stmt1.setString(9, cont_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String bu_plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset1.getString(2)==null?"":rset1.getString(2);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(countpty_abbr);
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cargo_no);
					VBOE_NO.add("");
					VBOE_NM.add("");
					VCONT_REF_NO.add(contRef);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);

					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(temp_period_start_dt);
					VPERIOD_END_DT.add(temp_period_end_dt);
					
					
					String inv_flag="UG";
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, "", "", "", inv_flag, inv_title,cargo_no,"");
					
					VBILLING_FREQ_FLAG.add("0");
					VBILLING_FREQ_NM.add("");
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderListforCrDr()
	{
		String function_nm="getTraderListforCrDr()";
		try
		{
			queryString="SELECT DISTINCT CD "
					+ "FROM (SELECT DISTINCT COUNTERPARTY_CD AS CD "
					+ "FROM FMS_PUR_SG_INV_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
					+ "AND INV_FLAG=? AND APPROVED_FLAG='A' AND CONTRACT_TYPE IN ('D','T','I','G','P') "
					+ "AND NVL((SELECT COUNT(*) FROM FMS_PUR_SG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.SYS_INV_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.INV_FLAG IN (?,?)),0) = 0 "
					+ "UNION ALL "
					+ "SELECT DISTINCT COUNTERPARTY_CD AS CD "
					+ "FROM FMS_PUR_PG_INV_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
					+ "AND INV_FLAG=? AND APPROVED_FLAG='A' AND CONTRACT_TYPE IN ('D','T','I','G','P') "
					+ "AND NVL((SELECT COUNT(*) FROM FMS_PUR_PG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.SYS_INV_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.INV_FLAG IN (?,?)),0) = 0 )";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, month+"/"+year);
			stmt.setString(3, "F");
			stmt.setString(4, "CR");
			stmt.setString(5, "DR");
			stmt.setString(6, comp_cd);
			stmt.setString(7, month+"/"+year);
			stmt.setString(8, "F");
			stmt.setString(9, "CR");
			stmt.setString(10, "DR");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cd = rset.getString(1)==null?"":rset.getString(1);
				VCOUNTERPARTY_CD.add(cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, cd));
			}
			rset.close();
			stmt.close();
			
			if(!VCOUNTERPARTY_CD.contains(counterparty_cd) && !counterparty_cd.equals(""))
			{
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceNoListforCrDr()
	{
		String function_nm="getInvoiceNoListforCrDr()";
		try
		{
			if(operation.equals("PREPARE"))
			{
				queryString="SELECT SYS_INV_NO,INVOICE_NO "
						+ "FROM FMS_PUR_SG_INV_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
						+ "AND SYS_INV_NO IS NOT NULL AND INV_FLAG=? AND APPROVED_FLAG='A' AND CONTRACT_TYPE IN ('D','T','I','G','P') "
						+ "AND NVL((SELECT COUNT(*) FROM FMS_PUR_SG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.SYS_INV_NO AND B.INV_FLAG IN (?,?)),0) = 0 "
						//+ "ORDER BY COUNTERPARTY_CD "
						+ "UNION ALL "
						+ "SELECT SYS_INV_NO,INVOICE_NO "
						+ "FROM FMS_PUR_PG_INV_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
						+ "AND SYS_INV_NO IS NOT NULL AND INV_FLAG=? AND APPROVED_FLAG='A' AND CONTRACT_TYPE IN ('D','T','I','G','P') "
						+ "AND NVL((SELECT COUNT(*) FROM FMS_PUR_PG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.SYS_INV_NO AND B.INV_FLAG IN (?,?)),0) = 0 ";
						//+ "ORDER BY COUNTERPARTY_CD ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, month+"/"+year);
				stmt.setString(4, "F");
				stmt.setString(5, "CR");
				stmt.setString(6, "DR");
				stmt.setString(7, comp_cd);
				stmt.setString(8, counterparty_cd);
				stmt.setString(9, month+"/"+year);
				stmt.setString(10, "F");
				stmt.setString(11, "CR");
				stmt.setString(12, "DR");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VINVOICE_NO_LIST.add(rset.getString(1)==null?"":rset.getString(1));
					VINVOICE_REF.add(rset.getString(2)==null?"":rset.getString(2));
				}
				rset.close();
				stmt.close();
			}
			else
			{
				VINVOICE_NO_LIST.add(sel_inv_no);
				
				queryString1="SELECT INVOICE_NO "
						+ "FROM FMS_PUR_SG_INV_MST A "
						+ "WHERE COMPANY_CD=? AND SYS_INV_NO=? AND CONTRACT_TYPE IN ('D','T','I','G','P') "
						+ "AND REF_NO IS NULL "
						+ "UNION ALL "
						+ "SELECT INVOICE_NO "
						+ "FROM FMS_PUR_PG_INV_MST A "
						+ "WHERE COMPANY_CD=? AND SYS_INV_NO=? AND CONTRACT_TYPE IN ('D','T','I','G','P') "
						+ "AND REF_NO IS NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, sel_inv_no);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, sel_inv_no);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VINVOICE_REF.add(rset1.getString(1)==null?"":rset1.getString(1));
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCriteriaList()
	{
		String function_nm="getCriteriaList()";
		try
		{
			
			String inv_rais_in="";
			String trans_chrg="";
			String market_chrg="";
			String oth_chrg="";
			String priceCd="";
			String contType="";
			
			queryString="SELECT INVOICE_RAISED_IN,SALE_PRICE_UNIT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SYS_INV_NO=? AND CONTRACT_TYPE IN ('D','T','G','P') AND APPROVED_FLAG='A' AND INV_FLAG='F' "
					+ "UNION ALL "
					+ "SELECT INVOICE_RAISED_IN,SALE_PRICE_UNIT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SYS_INV_NO=? AND CONTRACT_TYPE IN ('D','T','G','P') AND APPROVED_FLAG='A' AND INV_FLAG='F' ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			stmt.setString(4, comp_cd);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				inv_rais_in=rset.getString(1)==null?"":rset.getString(1);
				priceCd=rset.getString(2)==null?"":rset.getString(2);
				trans_chrg=rset.getString(3)==null?"":rset.getString(3);
				market_chrg=rset.getString(4)==null?"":rset.getString(4);
				oth_chrg=rset.getString(5)==null?"":rset.getString(5);
			}
			rset.close();
			stmt.close();
			
			VCRITERIA_FLAG.add("QTY");
			VCRITERIA_NAME.add("Change in Quantity");
			VCRITERIA_HIDE.add("N");
			
			VCRITERIA_FLAG.add("PRICE");
			VCRITERIA_NAME.add("Change in Price");
			VCRITERIA_HIDE.add("N");
			
			VCRITERIA_FLAG.add("EXCHG");
			VCRITERIA_NAME.add("Change in Exchange Rate");
			VCRITERIA_HIDE.add(inv_rais_in.equals("1") && priceCd.equals("2")?"N":"Y");
			
			VCRITERIA_FLAG.add("TC");
			VCRITERIA_NAME.add("Change in Transportation Tariff");
			VCRITERIA_HIDE.add(trans_chrg.equals("")?"Y":"N");
			
			VCRITERIA_FLAG.add("MM");
			VCRITERIA_NAME.add("Change in Marketing Margin");
			VCRITERIA_HIDE.add(market_chrg.equals("")?"Y":"N");
			
			VCRITERIA_FLAG.add("OC");
			VCRITERIA_NAME.add("Change in Other Charges");
			VCRITERIA_HIDE.add(oth_chrg.equals("")?"Y":"N");
			
			VCRITERIA_FLAG.add("TAXP");
			VCRITERIA_NAME.add("Change in Tax %");
			VCRITERIA_HIDE.add("N");
				
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedInvoiceDtl()
	{
		String function_nm="getSelectedInvoiceDtl()";
		try
		{
			couterpty_abbr=utilBean.getCounterpartyABBR(conn, counterparty_cd);
			couterpty_nm=utilBean.getCounterpartyName(conn, counterparty_cd);
			String company_nm=utilBean.getCompanyName(conn, comp_cd);
			
			queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE_UNIT,INVOICE_RAISED_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,GROSS_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
					+ "SALE_AMT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,'SG',INVOICE_NO,"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),FINANCIAL_YEAR,INVOICE_SEQ,FREQ,INV_FLAG "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SYS_INV_NO=? AND APPROVED_FLAG='A' AND INV_FLAG NOT IN ('CR','DR') "
					+ "UNION ALL "
					+ "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE_UNIT,INVOICE_RAISED_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,GROSS_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
					+ "SALE_AMT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,'PG',INVOICE_NO,"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),FINANCIAL_YEAR,INVOICE_SEQ,FREQ,INV_FLAG "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SYS_INV_NO=? AND APPROVED_FLAG='A' AND INV_FLAG NOT IN ('CR','DR') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			stmt.setString(4, comp_cd);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_no=rset.getString(1)==null?"":rset.getString(1);
				agmt_rev_no=rset.getString(2)==null?"":rset.getString(2);
				cont_no=rset.getString(3)==null?"":rset.getString(3);
				cont_rev_no=rset.getString(4)==null?"":rset.getString(4);
				contract_type=rset.getString(5)==null?"":rset.getString(5);
				cargo_no=rset.getString(6)==null?"0":rset.getString(6);
				bu_unit=rset.getString(7)==null?"":rset.getString(7);
				plant_seq=rset.getString(8)==null?"":rset.getString(8);
				bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn, comp_cd, comp_cd, bu_unit, "B");
				plant_abbr=utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plant_seq, "T");
				
				period_start_dt=rset.getString(9)==null?"":rset.getString(9);
				period_end_dt=rset.getString(10)==null?"":rset.getString(10);
				
				bu_contact_person_cd=rset.getString(15)==null?"":rset.getString(15);
				contact_person_cd=rset.getString(16)==null?"":rset.getString(16);
				price_cd=rset.getString(11)==null?"":rset.getString(11);
				price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
				invoice_raised_in=rset.getString(12)==null?"":rset.getString(12);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				financial_year=rset.getString(49)==null?"":rset.getString(49);
				String main_invoice_seq=rset.getString(50)==null?"":rset.getString(50);
				billing_freq=rset.getString(51)==null?"":rset.getString(51);
				String main_invoice_flg=rset.getString(52)==null?"":rset.getString(52);
				
				exchng_rate_cd=rset.getString(22)==null?"":rset.getString(22);
				exchang_rate_dt=rset.getString(23)==null?"":rset.getString(23);
				
				main_invoice_dt=rset.getString(17)==null?"":rset.getString(17);
				main_invoice_due_dt=rset.getString(18)==null?"":rset.getString(18);
				main_qty_mmbtu=nf.format(rset.getDouble(19));
				main_price=rset.getString(20)==null?"":rset.getString(20);
				main_gross_amt1=rset.getString(21)==null?"":rset.getString(21);
				main_exchang_rate=rset.getString(24)==null?"":rset.getString(24);
				main_gross_amt=rset.getString(25)==null?"":rset.getString(25);
				main_transportation_charges=rset.getString(26)==null?"":rset.getString(26);
				main_transportation_amount=rset.getString(27)==null?"":rset.getString(27);
				main_marketing_margin=rset.getString(28)==null?"":rset.getString(28);
				main_marketing_margin_amount=rset.getString(29)==null?"":rset.getString(29);
				main_other_charges=rset.getString(30)==null?"":rset.getString(30);
				main_other_charges_amount=rset.getString(31)==null?"":rset.getString(31);
				
				main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_amt1));
				if(!main_transportation_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_transportation_amount));
					isMainGrossIncTriff=true;
				}
				if(!main_marketing_margin_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_marketing_margin_amount));
					isMainGrossIncTriff=true;
				}
				if(!main_other_charges_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_other_charges_amount));
					isMainGrossIncTriff=true;
				}
				
				main_tax_amt=rset.getString(32)==null?"":rset.getString(32);
				main_tax_struct_cd=rset.getString(33)==null?"":rset.getString(33);
				main_tax_struct_dt=rset.getString(34)==null?"":rset.getString(34);
				main_tax_struct_dtl=utilBean.getTaxDescr(conn,main_tax_struct_cd);
				
				main_invoice_amt=rset.getString(35)==null?"":rset.getString(35);
				main_net_payable=rset.getString(36)==null?"":rset.getString(36);
				sgpg_type=rset.getString(37)==null?"":rset.getString(37);
				main_invoice_ref=rset.getString(38)==null?"":rset.getString(38);
				
				main_tcs_amount=rset.getString(39)==null?"":nf.format(rset.getDouble(39));
				main_tcs_factor=rset.getString(40)==null?"":nf3.format(rset.getDouble(40));
				main_tcs_struct_cd=rset.getString(41)==null?"":rset.getString(41);
				main_tcs_struct_dt=rset.getString(42)==null?"":rset.getString(42);
				applicable_flag=rset.getString(43)==null?"":rset.getString(43);
				applicable_abbr=rset.getString(44)==null?"":rset.getString(44);
				
				main_tds_amount=rset.getString(45)==null?"":nf.format(rset.getDouble(45));
				main_tds_factor=rset.getString(46)==null?"":nf3.format(rset.getDouble(46));
				main_tds_struct_cd=rset.getString(47)==null?"":rset.getString(47);
				main_tds_struct_dt=rset.getString(48)==null?"":rset.getString(48);
				
				String signingDt="";
				split_flag="";
				split_value="";
				if(contract_type.equals("G") || contract_type.equals("P"))
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
							+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,"
							+ "C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,C.DUE_DT_IN,C.EXCL_SAT_MAP,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,"
							+ "A.SUG,C.HOLIDAY_STATE,TO_CHAR(A.SIGNING_DT,'DD-MON-YY') "
							+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
							+ "AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "T");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.setString(6, "L");
					stmt1.setString(7, cont_no);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, cargo_no);
					stmt1.setString(10, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						signingDt=rset1.getString(24)==null?"":rset1.getString(24);
						deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
						contract_ref=cont_ref;
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
							+ "A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,A.TXN_CHARGE,C.DUE_DT_IN,"
							+ "C.EXCL_SAT_MAP,A.TRADE_REF_NO,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,A.SPLIT_FLAG,C.HOLIDAY_STATE,TO_CHAR(SIGNING_DT,'DD-MON-YY') "
							+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
							+ "AND A.CONT_NO=? " 
							+ "AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev_no);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, counterparty_cd);
					stmt1.setString(7, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						signingDt=rset1.getString(26)==null?"":rset1.getString(26);
						deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
						contract_ref=cont_ref;
						
						split_flag=rset1.getString(24)==null?"":rset1.getString(24);
						
						if(split_flag.equals("Y"))
						{
							queryString2="SELECT A.SPLIT_VALUE,A.COUNTERPARTY_CD "
									+ "FROM FMS_TRADER_CONT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? AND A.COUNTERPARTY_CD=? AND A.PLANT_SEQ_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO) "
									+ "UNION ALL "
									+ "SELECT A.SPLIT_VALUE,A.COUNTERPARTY_CD "
									+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? AND A.SPLIT_TRADER_CD=? AND A.PLANT_SEQ_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.SPLIT_TRADER_CD=B.SPLIT_TRADER_CD) ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, agmt_no);
							stmt2.setString(3, agmt_rev_no);
							stmt2.setString(4, cont_no);
							stmt2.setString(5, contract_type);
							stmt2.setString(6, counterparty_cd);
							stmt2.setString(7, plant_seq);
							stmt2.setString(8, comp_cd);
							stmt2.setString(9, agmt_no);
							stmt2.setString(10, agmt_rev_no);
							stmt2.setString(11, cont_no);
							stmt2.setString(12, contract_type);
							stmt2.setString(13, counterparty_cd);
							stmt2.setString(14, plant_seq);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								split_value=rset2.getString(1)==null?"":rset2.getString(1);
								main_counterparty_cd=rset2.getString(2)==null?"":rset2.getString(2);
								

								queryString3="SELECT A.PLANT_SEQ_NO "
										+ "FROM FMS_TRADER_CONT_PLANT A "
										+ "WHERE A.COMPANY_CD=? "
										+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
										+ "AND A.CONT_NO=? "
										+ "AND A.CONTRACT_TYPE=? AND A.COUNTERPARTY_CD=? AND A.SPLIT_VALUE IS NOT NULL "
										+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
										+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
								stmt3=conn.prepareStatement(queryString3);
								stmt3.setString(1, comp_cd);
								stmt3.setString(2, agmt_no);
								stmt3.setString(3, agmt_rev_no);
								stmt3.setString(4, cont_no);
								stmt3.setString(5, contract_type);
								stmt3.setString(6, main_counterparty_cd);
								//stmt3.setString(7, plant_seq);
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									main_plant_seq=rset3.getString(1)==null?"":rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
							}
							rset2.close();
							stmt2.close();
							
							queryString4="SELECT A.SPLIT_VALUE,A.COUNTERPARTY_CD,A.PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
									+ "UNION ALL "
									+ "SELECT A.SPLIT_VALUE,A.SPLIT_TRADER_CD,A.PLANT_SEQ_NO "
									+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ) ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, agmt_no);
							stmt4.setString(3, agmt_rev_no);
							stmt4.setString(4, cont_no);
							stmt4.setString(5, contract_type);
							stmt4.setString(6, comp_cd);
							stmt4.setString(7, agmt_no);
							stmt4.setString(8, agmt_rev_no);
							stmt4.setString(9, cont_no);
							stmt4.setString(10, contract_type);
							rset4=stmt4.executeQuery();
							while(rset4.next())
							{
								String splitValue = rset4.getString(1)==null?"":rset4.getString(1);
								String countPtyCd = rset4.getString(2)==null?"":rset4.getString(2);
								String plantSeq = rset4.getString(3)==null?"":rset4.getString(3);
								
								String counterParty_abbr=utilBean.getCounterpartyABBR(conn, countPtyCd);
								String plant_nm=utilBean.getCounterpartyPlantABBR(conn, countPtyCd, comp_cd, plantSeq, "T");
								split_invoice_list+=counterParty_abbr+" : "+plant_nm+" [Split "+splitValue+"%] \n";
							}
							rset4.close();
							stmt4.close();
							
							
						}
					}
					rset1.close();
					stmt1.close();
				}
				
				info = "In respect of ";
				
				if(contract_type.equals("I"))
				{
					info+="Exchange Transaction";
				}
				else if(contract_type.equals("G") || contract_type.equals("P"))
				{
					info+="LTCORA Purchase Contract";
				}
				else
				{
					info+="Purchase Contract";
				}
				info+="("+contract_ref+") executed on "+signingDt+"";
	            		//+ "\nFor the Billing Period "+period_start_dt+" to "+period_end_dt;
				info+="<br>Between "+couterpty_nm+" and "+company_nm;
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				
				Vector VTEMP_TAX_CODE = new Vector();
				Vector VTEMP_TAX_DESCR = new Vector();
				Vector VTEMP_TAX_AMT = new Vector();
				Vector VTEMP_TAX_BASE_AMT = new Vector();
				Vector VTEMP_TAX_FACTOR = new Vector();
				
				if(sgpg_type.equals("SG"))
				{
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_PUR_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
				}
				else if(sgpg_type.equals("PG"))
				{
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_PUR_PG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, main_invoice_seq);
				stmt1.setString(5, main_invoice_flg);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
					
					VTEMP_TAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTEMP_TAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTEMP_TAX_AMT.add("");
					VTEMP_TAX_BASE_AMT.add("");
					VTEMP_TAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				Vector VTEMP_TAX_DTL_1 = new Vector();
				
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_CODE);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_DESCR);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_BASE_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_FACTOR);
				
				VMAIN_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				
				if(operation.equals("PREPARE"))
				{
					VNEW_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL_1);
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL_1);
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
	
	public void getCrDrPreparationList(String contType)
	{
		String function_nm="getCrDrPreparationList()";
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "INVOICE_NO,REF_NO,INV_FLAG,CHECKED_FLAG,APPROVED_FLAG,PDF_INV_DTL,PRINT_BY_ORI,SAP_APPROVAL,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),CRITERIA,ALLOC_QTY,SYS_INV_NO,AUTHORIZED_FLAG,'SG',INVOICE_RAISED_IN "
					+ "FROM FMS_PUR_SG_INV_MST A "
					+ "WHERE COMPANY_CD=? AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONTRACT_TYPE IN ("+contType+") AND INV_FLAG IN ('CR','DR') "
					//+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD)) "
					+ " UNION ALL "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "INVOICE_NO,REF_NO,INV_FLAG,CHECKED_FLAG,APPROVED_FLAG,PDF_INV_DTL,PRINT_BY_ORI,SAP_APPROVAL,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),CRITERIA,ALLOC_QTY,SYS_INV_NO,AUTHORIZED_FLAG,'PG',INVOICE_RAISED_IN "
					+ "FROM FMS_PUR_PG_INV_MST A "
					+ "WHERE COMPANY_CD=? AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONTRACT_TYPE IN ("+contType+") AND INV_FLAG IN ('CR','DR') "
					+ "ORDER BY COUNTERPARTY_CD,INVOICE_SEQ";
			int st_count=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, temp_period_end_dt);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, temp_period_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{	
				inv_index++;
				
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				String cargo_no=rset.getString(8)==null?"":rset.getString(8);
				String bu_unit=rset.getString(9)==null?"":rset.getString(9);
				String plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String financial_year=rset.getString(11)==null?"":rset.getString(11);
				String invoice_seq =rset.getString(12)==null?"":rset.getString(12);
				String period_start_dt = rset.getString(13)==null?"":rset.getString(13);
				String period_end_dt = rset.getString(14)==null?"":rset.getString(14);
				double qtyMMBTU = rset.getDouble(25);
				String inv_dt=rset.getString(23)==null?"":rset.getString(23);
				String criteria=rset.getString(24)==null?"":rset.getString(24);
				criteria=criteria.replace("#", "<br>");
				
				String crdr_inv_no=rset.getString(26)==null?"":rset.getString(26);
				String invoice_no=rset.getString(15)==null?"":rset.getString(15);
				String ref_no=rset.getString(16)==null?"":rset.getString(16);
				String invType=rset.getString(17)==null?"":rset.getString(17);
				
				String aprove="N";
				String check="N";
				String auth="N";
				String is_submitted="N";
				String approve_inv_flag="";
				String pdf_inv_flag="N";
				String sap_approved_flag="";
				String payment_type_flag="";
				
				String chk_flg = rset.getString(18)==null?"":rset.getString(18);
				String auth_flg = rset.getString(27)==null?"":rset.getString(27);
				String aprv_flg = rset.getString(19)==null?"":rset.getString(19);
				String pdf_inv_dtl = rset.getString(20)==null?"":rset.getString(20);
				sap_approved_flag=rset.getString(22)==null?"":rset.getString(22);
				String sgpg_type=rset.getString(28)==null?"":rset.getString(28);
				String inv_raised_in=rset.getString(29)==null?"":rset.getString(29);
				
				is_submitted="Y";
				if(chk_flg.equals("Y"))
				{
					check="Y";
				}
				if(auth_flg.equals("Y"))
				{
					auth="Y";
				}
				if(aprv_flg.equals("A"))
				{
					aprove="Y";
					approve_inv_flag="S";
					payment_type_flag="S";
				}
				if(pdf_inv_dtl.equals("O"))
				{
					pdf_inv_flag="Y";
				}
				
						
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				String cont_ref="";
				String start_dt="";
				String end_dt="";
				String cont_name="";
				String split_flag="";
				String split_value="";
				
				if(cont_type.equals("G") || cont_type.equals("P"))
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
							+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,"
							+ "C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,C.DUE_DT_IN,C.EXCL_SAT_MAP,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,"
							+ "A.SUG,C.HOLIDAY_STATE "
							+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
							+ "AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, "T");
					stmt1.setString(4, agmtno);
					stmt1.setString(5, agmtrev);
					stmt1.setString(6, "L");
					stmt1.setString(7, contno);
					stmt1.setString(8, cont_type);
					stmt1.setString(9, cargo_no);
					stmt1.setString(10, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						start_dt=rset1.getString(7)==null?"":rset1.getString(7);
						end_dt=rset1.getString(8)==null?"":rset1.getString(8);
						cont_name=rset1.getString(9)==null?"":rset1.getString(9);
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
							+ "A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,A.TXN_CHARGE,C.DUE_DT_IN,"
							+ "C.EXCL_SAT_MAP,A.TRADE_REF_NO,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,A.SPLIT_FLAG,C.HOLIDAY_STATE "
							+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
							+ "AND A.CONT_NO=? "
							+ "AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, agmtno);
					stmt1.setString(3, agmtrev);
					stmt1.setString(4, contno);
					stmt1.setString(5, cont_type);
					stmt1.setString(6, countpty_cd);
					stmt1.setString(7, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						start_dt=rset1.getString(7)==null?"":rset1.getString(7);
						end_dt=rset1.getString(8)==null?"":rset1.getString(8);
						cont_name=rset1.getString(9)==null?"":rset1.getString(9);
						split_flag=rset1.getString(24)==null?"":rset1.getString(24);
						
						if(split_flag.equals("Y"))
						{
							queryString2="SELECT A.SPLIT_VALUE "
									+ "FROM FMS_TRADER_CONT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? AND A.COUNTERPARTY_CD=? AND A.PLANT_SEQ_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO) "
									+ "UNION ALL "
									+ "SELECT A.SPLIT_VALUE "
									+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
									+ "WHERE A.COMPANY_CD=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
									+ "AND A.CONT_NO=? "
									+ "AND A.CONTRACT_TYPE=? AND A.SPLIT_TRADER_CD=? AND A.PLANT_SEQ_NO=? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.SPLIT_TRADER_CD=B.SPLIT_TRADER_CD) ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, own_cd);
							stmt2.setString(2, agmtno);
							stmt2.setString(3, agmtrev);
							stmt2.setString(4, contno);
							stmt2.setString(5, cont_type);
							stmt2.setString(6, countpty_cd);
							stmt2.setString(7, plant_seq);
							stmt2.setString(8, own_cd);
							stmt2.setString(9, agmtno);
							stmt2.setString(10, agmtrev);
							stmt2.setString(11, contno);
							stmt2.setString(12, cont_type);
							stmt2.setString(13, countpty_cd);
							stmt2.setString(14, plant_seq);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								split_value=rset2.getString(1)==null?"":rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						}
						
					}
					rset1.close();
					stmt1.close();
				}
				
				
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(contrev);
				VCARGO_NO.add("");
				VBOE_NO.add("");
				VBOE_NM.add("");
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONT_NAME.add(cont_name);
				VCONT_REF_NO.add(cont_ref);
				VCONTRACT_TYPE.add(cont_type);
				VDEAL_NO.add(deal_no);
				VINVOICE_NO.add(invoice_no);
				VINVOICE_DT.add(inv_dt);
				VINVOICE_SEQ.add(invoice_seq);
				VFINANCIAL_YEAR.add(financial_year);
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn, countpty_cd, own_cd, plant_seq, "T"));
				VBU_PLANT_SEQ.add(bu_unit);
				VBU_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn, own_cd, own_cd, bu_unit, "B"));
				
				VSPLIT_VALUE.add(split_value);
				VSPLIT_FLAG.add(split_flag);
				
				VALLOC_QTY.add(nf.format(qtyMMBTU));
				
				String inv_flag="F";
				VINV_FLAG.add(inv_flag);
				
				VCRDR_CRITERIA.add(criteria);
				VCREDIT_DEBIT_NO.add(crdr_inv_no);
				VREF_NO.add(ref_no);
				VINVOICE_TYPE.add(invType);
				VINVOICE_TYPE_NM.add(invType.equals("CR")?"Credit Note":invType.equals("DR")?"Debit Note":"");
				
				VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
				VPDF_INV_FLAG.add(pdf_inv_flag);
				VSAP_APPROVAL_FLAG.add(sap_approved_flag);
				VPAYMENT_TYPE_FLAG.add(payment_type_flag);
				
				VAPPROVE_FLAG_CHECK.add(aprove);
				VCHECK_FLAG_CHECK.add(check);
				VAUTHORIZ_FLAG_CHECK.add(auth);
				VIS_SUBMITTED.add(is_submitted);
				VSGPG_TYPE.add(sgpg_type);
				VINV_RAISED_IN.add(inv_raised_in);
				
				String invTitle="'SG','PG'";
				if(inv_title.equals("PG_RECV"))
				{
					invTitle="'PG_RECV'";
				}
				queryString6="SELECT FILE_NAME,EMAIL_SENT "
						+ "FROM FMS_PUR_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND INV_TITLE IN ("+invTitle+") AND INV_FLAG=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, invoice_seq);
				stmt6.setString(3, financial_year);
				//stmt6.setString(4, inv_title);
				stmt6.setString(4, invType);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					
					VUPLOADED_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
					VEMAIL_SENT.add(rset6.getString(2)==null?"":rset6.getString(2));
				}
				else
				{
					VUPLOADED_FILE_NAME.add("");
					VEMAIL_SENT.add("");
				}
				rset6.close();
				stmt6.close();
				
				int upload_count=0;
				queryString5="SELECT COUNT(*) "
						+ "FROM FMS_PUR_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=?";
				stmt_tmp2=conn.prepareStatement(queryString5);
				stmt_tmp2.setString(1, own_cd);
				stmt_tmp2.setString(2, cont_type);
				stmt_tmp2.setString(3, invoice_seq);
				stmt_tmp2.setString(4, financial_year);
				stmt_tmp2.setString(5, "PG_RECV");
				stmt_tmp2.setString(6, invType);
				rset_tmp2=stmt_tmp2.executeQuery();
				if(rset_tmp2.next())
				{
					upload_count=rset_tmp2.getInt(1);
				}
				rset_tmp2.close();
				stmt_tmp2.close();
				
				VFILE_UPLOAD_COUNT.add(upload_count);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCRDRInvoiceDtl()
	{
		String function_nm="getCRDRInvoiceDtl()";
		try
		{
			String tbl_nm="FMS_PUR_SG_INV_MST";
			if(sgpg_type.equals("PG"))
			{
				tbl_nm="FMS_PUR_PG_INV_MST";
			}
			queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE_UNIT,INVOICE_RAISED_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,GROSS_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
					+ "SALE_AMT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,'SG',INVOICE_NO,"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),FINANCIAL_YEAR,INVOICE_SEQ,FREQ,CRITERIA,"
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM "+tbl_nm+" "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND REF_NO=? AND INV_FLAG=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			stmt.setString(4, crdr_gen_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_contact_person_cd=rset.getString(15)==null?"":rset.getString(15);
				contact_person_cd=rset.getString(16)==null?"":rset.getString(16);
				invoice_dt=rset.getString(17)==null?"":rset.getString(17);
				invoice_due_dt=rset.getString(18)==null?"":rset.getString(18);
				qty_mmbtu=rset.getString(19)==null?"":rset.getString(19);
				price=rset.getString(20)==null?"":rset.getString(20);
				gross_amt1=rset.getString(21)==null?"":rset.getString(21);
				exchang_rate=rset.getString(24)==null?"":nf2.format(rset.getDouble(24));
				gross_amt=rset.getString(25)==null?"":rset.getString(25);
				transportation_charges=rset.getString(26)==null?"":rset.getString(26);
				transportation_amount=rset.getString(27)==null?"":rset.getString(27);
				marketing_margin=rset.getString(28)==null?"":rset.getString(28);
				marketing_margin_amount=rset.getString(29)==null?"":rset.getString(29);
				other_charges=rset.getString(30)==null?"":rset.getString(30);
				other_charges_amount=rset.getString(31)==null?"":rset.getString(31);
				
				gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
				if(!transportation_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
					isGrossIncTriff=true;
				}
				if(!marketing_margin_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
					isGrossIncTriff=true;
				}
				if(!other_charges_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
					isGrossIncTriff=true;
				}
				
				tax_amt=rset.getString(32)==null?"":rset.getString(32);
				tax_struct_cd=rset.getString(33)==null?"":rset.getString(33);
				tax_struct_dt=rset.getString(34)==null?"":rset.getString(34);
				tax_struct_dtl=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				invoice_amt=rset.getString(35)==null?"":rset.getString(35);
				net_payable=rset.getString(36)==null?"":rset.getString(36);
				invoice_ref=rset.getString(38)==null?"":rset.getString(38);
				
				tcs_amount=rset.getString(39)==null?"":nf.format(rset.getDouble(39));
				tcs_factor=rset.getString(40)==null?"":nf3.format(rset.getDouble(40));
				tcs_struct_cd=rset.getString(41)==null?"":rset.getString(41);
				tcs_struct_dt=rset.getString(42)==null?"":rset.getString(42);
				
				tds_amount=rset.getString(45)==null?"":nf.format(rset.getDouble(45));
				tds_factor=rset.getString(46)==null?"":nf3.format(rset.getDouble(46));
				tds_struct_cd=rset.getString(47)==null?"":rset.getString(47);
				tds_struct_dt=rset.getString(48)==null?"":rset.getString(48);
				financial_year=rset.getString(49)==null?"":rset.getString(49);
				criteri_formula=rset.getString(52)==null?"":rset.getString(52);
				invoice_check_flag=rset.getString(53)==null?"":rset.getString(53);
				String chk_by=rset.getString(54)==null?"":rset.getString(54);
				invoice_check_by=utilBean.getEmpName(conn,chk_by);
				invoice_check_dt=rset.getString(55)==null?"":rset.getString(55);
				if(invoice_check_flag.equals("Y"))
				{
					invoice_check_nm="Checked";
				}
				else if(invoice_check_flag.equals("N"))
				{
					invoice_check_nm="Rejected";
				}
				invoice_auth_flag=rset.getString(56)==null?"":rset.getString(56);
				String auth_by=rset.getString(57)==null?"":rset.getString(57);
				invoice_auth_by=utilBean.getEmpName(conn,auth_by);
				invoice_auth_dt=rset.getString(58)==null?"":rset.getString(58);
				if(invoice_auth_flag.equals("Y"))
				{
					invoice_auth_nm="Authorized";
				}
				else if(invoice_auth_flag.equals("N"))
				{
					invoice_auth_nm="Rejected";
				}

				invoice_aprv_flag=rset.getString(59)==null?"":rset.getString(59);
				String aprv_by=rset.getString(60)==null?"":rset.getString(60);
				invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
				invoice_aprv_dt=rset.getString(61)==null?"":rset.getString(61);
				if(invoice_aprv_flag.equals("A"))
				{
					invoice_aprv_nm="Approved";
				}
				else if(invoice_aprv_flag.equals("R"))
				{
					invoice_aprv_nm="Rejected";
				}
				
				if(activityFlag.equals("CHECK")) 
				{
					activity_value=rset.getString(53)==null?"":rset.getString(53);
				}
				else if(activityFlag.equals("AUTHORIZE")) 
				{
					activity_value=rset.getString(56)==null?"":rset.getString(56);
				}
				else if(activityFlag.equals("APPROVE")) 
				{
					activity_value=rset.getString(59)==null?"":rset.getString(59);
				}
				
				if(contract_type.equals("G") || contract_type.equals("P"))
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
							+ "A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,"
							+ "C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,C.DUE_DT_IN,C.EXCL_SAT_MAP,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,"
							+ "A.SUG,C.HOLIDAY_STATE "
							+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
							+ "AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "T");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.setString(6, "L");
					stmt1.setString(7, cont_no);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, cargo_no);
					stmt1.setString(10, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
							+ "A.RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,A.TXN_CHARGE,C.DUE_DT_IN,"
							+ "C.EXCL_SAT_MAP,A.TRADE_REF_NO,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,A.SPLIT_FLAG,C.HOLIDAY_STATE "
							+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_BILLING_DTL C "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
							+ "AND A.CONT_NO=? " 
							+ "AND A.CONTRACT_TYPE=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
							+ "AND C.COUNTERPARTY_CD=? AND C.PLANT_SEQ_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev_no);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, counterparty_cd);
					stmt1.setString(7, plant_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no)+" ["+cont_ref+"]";
					}
					rset1.close();
					stmt1.close();
				}
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				
				Vector VTEMP_TAX_CODE = new Vector();
				Vector VTEMP_TAX_DESCR = new Vector();
				Vector VTEMP_TAX_AMT = new Vector();
				Vector VTEMP_TAX_BASE_AMT = new Vector();
				Vector VTEMP_TAX_FACTOR = new Vector();
				
				if(sgpg_type.equals("SG"))
				{
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_PUR_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
				}
				else if(sgpg_type.equals("PG"))
				{
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_PUR_PG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=?";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, crdr_gen_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
					
					VTEMP_TAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTEMP_TAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTEMP_TAX_AMT.add("");
					VTEMP_TAX_BASE_AMT.add("");
					VTEMP_TAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				Vector VTEMP_TAX_DTL_1 = new Vector();
				
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_CODE);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_DESCR);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_BASE_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_FACTOR);
				
				VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				
			}
			rset.close();
			stmt.close();
			
			contact_person_nm=utilBean.getEmpName(conn, contact_person_cd);
			bu_contact_person_nm=utilBean.getEmpName(conn, bu_contact_person_cd);
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "T", counterparty_cd, plant_seq);
			tax_info=tax_info.replaceAll("\n", "<br>");
			
			bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			
			String NormalFont="";
			if(crdr_gen_type.equals("CR")) 
			{
				NormalFont="Credit Note";
			}
			else if(crdr_gen_type.equals("DR"))
			{ 
				NormalFont="Debit Note";
			}
			
			String parameter="counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cargo_no="+cargo_no+"&cont_no="+cont_no+
					"&cont_rev="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev_no+"&plant_seq="+plant_seq+
					"&bu_unit="+bu_unit+"&operation="+operation+"&invoice_seq="+invoice_seq+"&activityFlag="+activityFlag+
					"&qty_mmbtu="+qty_mmbtu+"&inv_flag="+crdr_gen_type+"&sel_inv_no="+sel_inv_no+"&crdr_no="+crdr_inv_no+"&sgpg_type="+sgpg_type;
			
			int srno=0;
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Reference Att-1 "+NormalFont+" against Invoice No : "+main_invoice_ref+" dated "+main_invoice_dt);
			VPDF_COL3.add("");
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add("");
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Gross Amount");
			VPDF_COL3.add("<a onclick=openAtt1('rpt_purchase_crdr_view_attachment1.jsp?"+parameter+"')>Att1</a>");
			VPDF_COL4.add(invoice_raised_in_nm);
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(gross_include_transport_tariff.equals("")?"":Double.doubleToRawLongBits(Double.parseDouble(gross_include_transport_tariff))==Double.doubleToRawLongBits(0)?"":nf.format(Math.abs(Double.parseDouble(gross_include_transport_tariff))));
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
			VPDF_COL3.add("");
			VPDF_COL4.add(invoice_raised_in_nm);
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(tax_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(tax_amt))));
			
			double temp_srno=srno;
			if(VMULTI_TAX_STRUCT.size()>0)
            {
        		for(int i=0;i<VMULTI_TAX_STRUCT.size();i++)
        		{
        			Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
        			
        			if(((Vector) temp.elementAt(0)).size() > 1)
        			{
        				for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
        				{
        					temp_srno=temp_srno+0.1;
    						VPDF_COL1.add(nf0.format(temp_srno));
    						VPDF_COL2.add(""+((Vector) temp.elementAt(1)).elementAt(j));
    						VPDF_COL3.add("");
    						VPDF_COL4.add(invoice_raised_in_nm);
    						VPDF_COL5.add("");
    						VPDF_COL6.add("");
    						VPDF_COL7.add(""+((Vector) temp.elementAt(2)).elementAt(j));
        				}
        			}
        		}
            }
			
			String invAmtLbl=inv_flag.equals("UG")?"Invoice Amount - GST on SUG":NormalFont+" Amount";
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add(invAmtLbl);
			VPDF_COL3.add("");
			VPDF_COL4.add(invoice_raised_in_nm);
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(invoice_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt))));
			
			if(applicable_abbr.equals("TCS"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("TCS");
				VPDF_COL3.add("");
				VPDF_COL4.add(invoice_raised_in_nm);
				VPDF_COL5.add("");
				VPDF_COL6.add(tcs_factor+"%");
				VPDF_COL7.add(tcs_amount.equals("")?"":nf.format(Math.abs(Double.parseDouble(tcs_amount))));
			}
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Net Amount Payable");
			VPDF_COL3.add("");
			VPDF_COL4.add(invoice_raised_in_nm);
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(net_payable.equals("")?"":nf.format(Math.abs(Double.parseDouble(net_payable))));
			
			reason="Change in ";
			if(!criteri_formula.equals(""))
			{
				String[] split_criteri_formula = criteri_formula.split("#");
				for(int i=0; i<split_criteri_formula.length; i++)
				{
					if(i!=0)
					{
						if((i+1) == split_criteri_formula.length)
						{
							reason+=" and ";
						}
						else
						{
							reason+=", ";
						}
					}
					
					if(split_criteri_formula[i].toString().equals("QTY"))
					{
						reason+="Quantity";
					}
					else if(split_criteri_formula[i].toString().equals("PRICE"))
					{
						reason+="Price";
					}
					else if(split_criteri_formula[i].toString().equals("EXCHG"))
					{
						reason+="Exchange Rate";
					}
					else if(split_criteri_formula[i].toString().equals("TC"))
					{
						reason+="Transportation Tariff";
					}
					else if(split_criteri_formula[i].toString().equals("MM"))
					{
						reason+="Marketing Margin";
					}
					else if(split_criteri_formula[i].toString().equals("OC"))
					{
						reason+="Other Charges";
					}
					else if(split_criteri_formula[i].toString().equals("TAXP"))
					{
						reason+="Tax %";
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNewCRDRInvoiceDtl()
	{
		String function_nm="getNewCRDRInvoiceDtl()";
		try
		{
			//if(criteri_formula.contains("QTY"))
			{
				if(split_flag.equals("Y"))
				{
					changed_qty_mmbtu=""+allocUtil.getTraderAllocationQty(conn, comp_cd, main_counterparty_cd, agmt_no, cont_no, contract_type, main_plant_seq, bu_unit, period_start_dt, period_end_dt,cargo_no);
				
					if(Double.parseDouble(changed_qty_mmbtu)>0 && !split_value.equals(""))
					{
						changed_qty_mmbtu=""+(Double.parseDouble(changed_qty_mmbtu) * (Double.parseDouble(""+split_value) / 100));
					}
					else
					{
						changed_qty_mmbtu="0";
					}
				}
				else
				{
					changed_qty_mmbtu=""+allocUtil.getTraderAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, period_start_dt, period_end_dt,cargo_no);
				}
			}
			
			queryString="SELECT ALLOC_QTY,SALE_PRICE,SALE_AMT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_CERT_FLAG,TCS_TDS,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,"
					+ "MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,FINANCIAL_YEAR "
					+ "FROM FMS_PUR_INV_CRDR_REF "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_SEQ=? AND IS_SGPG=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, sgpg_type);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(criteri_formula.contains("QTY") && operation.equals("MODIFY"))
				{
					new_qty_mmbtu=changed_qty_mmbtu;
				}
				else
				{
					new_qty_mmbtu=nf.format(rset.getDouble(1));
				}
				
				new_price=rset.getString(2)==null?"":rset.getString(2);
				new_gross_amt1=rset.getString(6)==null?"":rset.getString(6);
				new_exchang_rate=rset.getString(4)==null?"":rset.getString(4);
				new_gross_amt=rset.getString(3)==null?"":rset.getString(3);
				new_tax_amt=rset.getString(7)==null?"":rset.getString(7);
				new_tax_struct_cd=rset.getString(8)==null?"":rset.getString(8);
				new_tax_struct_dtl=utilBean.getTaxDescr(conn, new_tax_struct_cd);
				new_tax_struct_dt="";
				new_invoice_amt=rset.getString(9)==null?"":rset.getString(9);
				new_net_payable=rset.getString(10)==null?"":rset.getString(10);
				
				new_tcs_amount=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				new_tcs_factor=rset.getString(16)==null?"":nf3.format(rset.getDouble(16));
				new_tcs_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				new_tcs_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				
				new_tds_amount=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
				new_tds_factor=rset.getString(20)==null?"":nf3.format(rset.getDouble(20));
				new_tds_struct_cd=rset.getString(21)==null?"":rset.getString(21);
				new_tds_struct_dt=rset.getString(22)==null?"":rset.getString(22);
				
				new_transportation_charges=rset.getString(23)==null?"":rset.getString(23);
				new_transportation_amount=rset.getString(24)==null?"":rset.getString(24);
				new_marketing_margin=rset.getString(25)==null?"":rset.getString(25);
				new_marketing_margin_amount=rset.getString(26)==null?"":rset.getString(26);
				new_other_charges=rset.getString(27)==null?"":rset.getString(27);
				new_other_charges_amount=rset.getString(28)==null?"":rset.getString(28);
				financial_year=rset.getString(29)==null?"":rset.getString(29);
				
				new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_amt1));
				if(!new_transportation_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_transportation_amount));
					isNewGrossIncTriff=true;
				}
				if(!new_marketing_margin_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_marketing_margin_amount));
					isNewGrossIncTriff=true;
				}
				if(!new_other_charges_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_other_charges_amount));
					isNewGrossIncTriff=true;
				}
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				
				Vector VTEMP_TAX_CODE = new Vector();
				Vector VTEMP_TAX_DESCR = new Vector();
				Vector VTEMP_TAX_AMT = new Vector();
				Vector VTEMP_TAX_BASE_AMT = new Vector();
				Vector VTEMP_TAX_FACTOR = new Vector();
				
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_PUR_INV_CRDR_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND IS_SGPG=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, sgpg_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
					
					VTEMP_TAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTEMP_TAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTEMP_TAX_AMT.add("");
					VTEMP_TAX_BASE_AMT.add("");
					VTEMP_TAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				Vector VTEMP_TAX_DTL_1 = new Vector();
				
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_CODE);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_DESCR);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_BASE_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_FACTOR);
				
				VNEW_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCrdrSendInvoiceMailDetail()
	{
		String function_nm="getCrdrSendInvoiceMailDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			financial_year=utilDate.getFinancialYear(invoice_dt);
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ownAddress  = rset.getString(1)==null?"":rset.getString(1);
				ownCity  = rset.getString(2)==null?"":rset.getString(2);
				ownState  = rset.getString(3)==null?"":rset.getString(3);
				ownPin  = rset.getString(4)==null?"":rset.getString(4);
				ownCountry  = rset.getString(5)==null?"":rset.getString(5);
				ownPhone  = rset.getString(6)==null?"":rset.getString(6);
				ownEmail  = rset.getString(7)==null?"":rset.getString(7);
			}
			rset.close();
			stmt.close();
			
			String bu_contact_person_cd="";
			String invoiceNo="";
			String invoiceDt="";
			String dueDate="";
			String sysinvNo="";
			
			queryString1="SELECT BU_CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),SYS_INV_NO ";
			if(sgpg_type.equals("PG"))
			{
				queryString1+= "FROM FMS_PUR_PG_INV_MST ";
			}
			else
			{
				queryString1+= "FROM FMS_PUR_SG_INV_MST ";
			}
			
			queryString1+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_FLAG=? ";
			int st_count=0;
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(++st_count, comp_cd);
			stmt1.setString(++st_count, counterparty_cd);
			stmt1.setString(++st_count, contract_type);
			stmt1.setString(++st_count, invoice_seq);
			stmt1.setString(++st_count, financial_year);
			stmt1.setString(++st_count, crdr_gen_type);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
				invoiceNo=rset1.getString(2)==null?"":rset1.getString(2);
				dueDate=rset1.getString(3)==null?"":rset1.getString(3);
				invoiceDt=rset1.getString(4)==null?"":rset1.getString(4);
				sysinvNo=rset1.getString(5)==null?"":rset1.getString(5);
			}
			rset1.close();
			stmt1.close();
				
			String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
			String subject="";
			String type="";
				
			String to_list="";
			String cc_list="";
			
			to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, "RM", "RLNG","Y");
			cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, "RM", "RLNG","N");
			String tmpToList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","Y");
			to_list+=to_list.equals("")?tmpToList:","+tmpToList;
		
			String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","N");
			cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
			
			//get BCc list
			String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","B");
			
			Vector VTEMP_ATTACHMENT=new Vector();
			st_count=0;
			queryString4="SELECT FILE_NAME "
					+ "FROM FMS_PUR_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE!=? AND INV_FLAG=?";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(++st_count, comp_cd);
			stmt4.setString(++st_count, contract_type);
			stmt4.setString(++st_count, invoice_seq);
			stmt4.setString(++st_count, financial_year);
			stmt4.setString(++st_count, "X");
			stmt4.setString(++st_count, crdr_gen_type);
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				VTEMP_ATTACHMENT.add(rset4.getString(1)==null?"":rset4.getString(1));
			}
			rset4.close();
			stmt4.close();
			
			String crdrdtl="";
			if(crdr_gen_type.equals("CR"))
			{
				crdrdtl="Credit Note";
			}
			else if(crdr_gen_type.equals("DR"))
			{
				crdrdtl="Debit Note";
			}
			
			String mail_keyword="Remittance Advice for "+crdrdtl;
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
			subject=companyAbbr+"/"+mail_keyword+"/"+customerNm+"/"+sysinvNo+"/"+financial_year;

			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed :"+subject
					+ " dated "+invoiceDt.replaceAll("/", "-")+"."
					+ "\nPlease note payment will be processed on or before "+dueDate.replaceAll("/", "-")
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""	
					+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";	
			
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(VTEMP_ATTACHMENT);
			VMAIL_ATTACHMENT_PATH.add(CommonVariable.purchase_inv_path);
			VMAIL_BODY.add(mail_body);	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}

	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}
	String operation = "";
	public void setOperation(String operation) {this.operation = operation;}

	String month = "";
	String year = "";
	String billing_cycle = "";
	String billing_freq="";
	String period_start_dt="";
	String period_end_dt="";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String plant_seq = "";
	String bu_unit = "";
	String inv_type = "";
	String address_type = "";
	String invoice_type = "";
	String refresh_flg = "";
	String inv_seq="";
	String inv_no="";
	String financial="";
	String inv_title="";
	String file_nm="";
	String emp_cd="";
	String fflow_inv_title="";
	String mail_inv_type="";
	String cargo_no="";
	String boe_no="";
	String inv_flag="";
	String allocQty="";
	String temp_price="";
	String filter_cont_type="";
	String entity="";
	String sel_inv_no="";
	String crdr_gen_type="";
	String sgpg_type="";
	String sap_approval_flag="";
	String xmlfile_name="";
	String file_path="";
	
	String remittance_no="";
	String activityFlag="";
	String crdr_inv_no="";

	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setAddress_type(String address_type) {this.address_type = address_type;}
	public void setInvoice_type(String invoice_type) {this.invoice_type = invoice_type;}
	public void setRefresh_flg(String refresh_flg) {this.refresh_flg = refresh_flg;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setInv_no(String inv_no) {this.inv_no = inv_no;}
	public void setFinancial(String financial) {this.financial = financial;}
	public void setInv_title(String inv_title) {this.inv_title = inv_title;}
	public void setFile_nm(String file_nm) {this.file_nm = file_nm;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setFflow_inv_title(String fflow_inv_title) {this.fflow_inv_title = fflow_inv_title;}
	public void setMail_inv_type(String mail_inv_type) {this.mail_inv_type = mail_inv_type;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setBoe_no(String boe_no) {this.boe_no = boe_no;}
	public void setInv_flag(String inv_flag) {this.inv_flag = inv_flag;}
	public void setAllocQty(String allocQty) {this.allocQty = allocQty;}
	public void setTemp_price(String temp_price) {this.temp_price = temp_price;}
	public void setFilter_cont_type(String filter_cont_type) {this.filter_cont_type = filter_cont_type;}
	public void setEntity(String entity) {this.entity = entity;}
	
	public void setContact_person_cd(String contact_person_cd) {this.contact_person_cd = contact_person_cd;}
	public void setBu_contact_person_cd(String bu_contact_person_cd) {this.bu_contact_person_cd = bu_contact_person_cd;}
	public void setPrice_cd(String price_cd) {this.price_cd = price_cd;}
	public void setInvoice_raised_in(String invoice_raised_in) {this.invoice_raised_in = invoice_raised_in;}
	public void setInvoice_seq(String invoice_seq) {this.invoice_seq = invoice_seq;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	
	public void setQty_mmbtu(String qty_mmbtu) {this.qty_mmbtu = qty_mmbtu;}
	public void setInvoice_dt(String invoice_dt) {this.invoice_dt = invoice_dt;}
	public void setInvoice_due_dt(String invoice_due_dt) {this.invoice_due_dt = invoice_due_dt;}
	public void setSel_inv_no(String sel_inv_no) {this.sel_inv_no = sel_inv_no;}
	public void setCrdr_gen_type(String crdr_gen_type) {this.crdr_gen_type = crdr_gen_type;}
	public void setSgpg_type(String sgpg_type) {this.sgpg_type = sgpg_type;}
	public void setSap_approval_flag(String sap_approval_flag) {this.sap_approval_flag = sap_approval_flag;}
	public void setXmlfile_name(String xmlfile_name) {this.xmlfile_name = xmlfile_name;}
	public void setFile_path(String file_path) {this.file_path = file_path;}
	public void setRemittance_no(String remittance_no) {this.remittance_no=remittance_no;}
	public void setActivityFlag(String activityFlag) {this.activityFlag=activityFlag;}
	public void setCrdr_inv_no(String crdr_inv_no) {this.crdr_inv_no=crdr_inv_no;}
	
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	public String getPlant_seq() {return plant_seq;}
	public String getBu_unit() {return bu_unit;}
	public String getAgmt_no() {return agmt_no;}
	public String getAgmt_rev_no() {return agmt_rev_no;}
	public String getCont_no() {return cont_no;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getContract_type() {return contract_type;}
	public String getCargo_no() {return cargo_no;}
	public String getBilling_freq() {return billing_freq;}

	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_CD = new Vector();
	Vector VBU_CONTACT_PERSON = new Vector();
	Vector VBU_CONTACT_PERSON_CD = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	Vector VFREQ = new Vector();
	Vector VFREQ_NM = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VEXCHNAGE_RATE = new Vector();
	Vector VSALES_PRICE = new Vector();
	Vector VSALES_PRICE_UNIT = new Vector();
	Vector VRATE_NM = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VINVOICE_AMT = new Vector();
	Vector VADJ_SIGN = new Vector();
	Vector VADJ_AMT = new Vector();
	Vector VNET_PAYABLE = new Vector();
	Vector VSALES_PRICE_USD = new Vector();
	Vector VSALES_PRICE_UNIT_USD = new Vector();
	Vector VRATE_NM_USD = new Vector();
	Vector VGROSS_AMT_USD = new Vector();
	Vector VTAX_AMT_USD = new Vector();
	Vector VINVOICE_AMT_USD = new Vector();
	Vector VADJ_SIGN_USD = new Vector();
	Vector VADJ_AMT_USD = new Vector();
	Vector VNET_PAYABLE_USD = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VLINK_INVOICE_NO = new Vector();
	Vector VSYS_LINK_INVOICE_NO = new Vector();
	Vector VSTATUS = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VINVOICE_CATEGORY = new Vector();
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VINVOICE_REF = new Vector();
	Vector VLINE_NO = new Vector();
	Vector VLINE_DESC = new Vector();
	Vector VUNIT = new Vector();
	Vector VQTY = new Vector();
	Vector VRATE = new Vector();
	Vector VAMOUNT = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VBILLING_FREQ_NM = new Vector();
	Vector VFILE_UPLOAD_COUNT = new Vector();
	Vector VUPLOADED_FILE_NAME = new Vector();
	
	Vector VAPPROVE_FLAG_CHECK = new Vector();
	Vector VCHECK_FLAG_CHECK = new Vector();
	Vector VAUTHORIZ_FLAG_CHECK = new Vector();
	Vector VIS_SUBMITTED = new Vector();
	Vector VAPPROVE_INVOICE_FLAG = new Vector();
	Vector VPDF_INV_FLAG = new Vector();

	Vector VOTH_COUNTERPTY_CD = new Vector();
	Vector VOTH_COUNTERPTY_ABBR = new Vector();
	Vector VOTH_CONT_NO = new Vector();
	Vector VOTH_CONT_REV_NO = new Vector();
	Vector VOTH_AGMT_NO = new Vector();
	Vector VOTH_AGMT_REV_NO = new Vector();
	Vector VOTH_CONTRACT_TYPE = new Vector();
	Vector VOTH_DEAL_NO = new Vector();
	Vector VOTH_PLANT_SEQ = new Vector();
	Vector VOTH_PLANT_ABBR = new Vector();
	Vector VOTH_BU_PLANT_SEQ = new Vector();
	Vector VOTH_BU_PLANT_ABBR = new Vector();
	Vector VOTH_START_DT = new Vector();
	Vector VOTH_END_DT = new Vector();
	Vector VOTH_CONT_NAME = new Vector();
	Vector VOTH_CONT_REF_NO = new Vector();
	Vector VOTH_PERIOD_START_DT = new Vector();
	Vector VOTH_PERIOD_END_DT = new Vector();
	Vector VOTH_INVOICE_NO = new Vector();
	Vector VOTH_DIS_INVOICE_NO = new Vector();
	Vector VOTH_STATUS = new Vector();
	Vector VOTH_BILLING_FREQ_FLAG = new Vector();
	Vector VOTH_BILLING_FREQ_NM = new Vector();
	Vector VOTH_INVOICE_SEQ = new Vector();
	Vector VOTH_FINANCIAL_YEAR = new Vector();
	Vector VOTH_INVOICE_TYPE = new Vector();
	Vector VOTH_FILE_UPLOAD_COUNT = new Vector();
	Vector VOTH_UPLOADED_FILE_NAME = new Vector();
	
	Vector VOTH_APPROVE_FLAG_CHECK = new Vector();
	Vector VOTH_CHECK_FLAG_CHECK = new Vector();
	Vector VOTH_AUTHORIZ_FLAG_CHECK = new Vector();
	Vector VOTH_IS_SUBMITTED = new Vector();
	Vector VOTH_APPROVE_INVOICE_FLAG = new Vector();
	Vector VOTH_PDF_INV_FLAG = new Vector();
	Vector VOTH_ENTITY = new Vector();
	
	Vector VMAIL_FROM_LIST = new Vector();
	Vector VMAIL_TO_LIST = new Vector();
	Vector VMAIL_CC_LIST = new Vector();
	Vector VMAIL_BCC_LIST = new Vector();
	Vector VMAIL_SUBJECT = new Vector();
	Vector VMAIL_ATTACHMENT = new Vector();
	Vector VMAIL_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_BODY = new Vector();
	
	Vector VSPLIT_FLAG = new Vector();
	Vector VSPLIT_VALUE = new Vector();
	
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VOTH_SAP_APPROVAL_FLAG = new Vector();
	Vector VPAYMENT_TYPE_FLAG = new Vector();
	Vector VOTH_PAYMENT_TYPE_FLAG = new Vector();
	
	Vector VSG_MULTI_TAX_STRUCT = new Vector();
	Vector VPG_MULTI_TAX_STRUCT = new Vector();
	Vector VMULTI_TAX_STRUCT = new Vector();
	Vector VMAIN_MULTI_TAX_STRUCT = new Vector();
	Vector VNEW_MULTI_TAX_STRUCT = new Vector();
	
	Vector VREMITTANCE_LIST_ABBR = new Vector();
	Vector VREMITTANCE_LIST_NAME = new Vector();
	Vector VINDEX = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VBOE_NO = new Vector();
	Vector VBOE_NM = new Vector();
	Vector VREMITTANCE_NO = new Vector();
	Vector VOTH_REMITTANCE_NO = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VSHIP_NAME = new Vector();
	
	Vector VFILTER_CONT_TYPE = new Vector();
	Vector VFILTER_CONT_NAME = new Vector();
	Vector VINVOICE_NO_LIST = new Vector();
	Vector VCRITERIA_FLAG = new Vector();
	Vector VCRITERIA_NAME = new Vector();
	Vector VCRITERIA_HIDE = new Vector();
	
	Vector VCRDR_CRITERIA = new Vector();
	Vector VCREDIT_DEBIT_NO = new Vector();
	Vector VREF_NO = new Vector();
	Vector VSGPG_TYPE = new Vector();
	Vector VEMAIL_SENT = new Vector();
	Vector VINV_RAISED_IN = new Vector();
	
	Vector VOTH_INVOICE_DT = new Vector();
	
	Vector VPDF_COL1 = new Vector();
	Vector VPDF_COL2 = new Vector();
	Vector VPDF_COL3 = new Vector();
	Vector VPDF_COL4 = new Vector();
	Vector VPDF_COL5 = new Vector();
	Vector VPDF_COL6 = new Vector();
	Vector VPDF_COL7 = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVCONTACT_PERSON() {return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_CD() {return VCONTACT_PERSON_CD;}
	public Vector getVBU_CONTACT_PERSON() {return VBU_CONTACT_PERSON;}
	public Vector getVBU_CONTACT_PERSON_CD() {return VBU_CONTACT_PERSON_CD;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVFREQ() {return VFREQ;}
	public Vector getVFREQ_NM() {return VFREQ_NM;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVEXCHNAGE_RATE() {return VEXCHNAGE_RATE;}
	public Vector getVSALES_PRICE() {return VSALES_PRICE;}
	public Vector getVSALES_PRICE_UNIT() {return VSALES_PRICE_UNIT;}
	public Vector getVRATE_NM() {return VRATE_NM;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVADJ_SIGN() {return VADJ_SIGN;}
	public Vector getVADJ_AMT() {return VADJ_AMT;}
	public Vector getVNET_PAYABLE() {return VNET_PAYABLE;}
	public Vector getVSALES_PRICE_USD() {return VSALES_PRICE_USD;}
	public Vector getVSALES_PRICE_UNIT_USD() {return VSALES_PRICE_UNIT_USD;}
	public Vector getVRATE_NM_USD() {return VRATE_NM_USD;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVTAX_AMT_USD() {return VTAX_AMT_USD;}
	public Vector getVINVOICE_AMT_USD() {return VINVOICE_AMT_USD;}
	public Vector getVADJ_SIGN_USD() {return VADJ_SIGN_USD;}
	public Vector getVADJ_AMT_USD() {return VADJ_AMT_USD;}
	public Vector getVNET_PAYABLE_USD() {return VNET_PAYABLE_USD;}
	public Vector getVMAPPING_ID() {return VMAPPING_ID;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVLINK_INVOICE_NO() {return VLINK_INVOICE_NO;}
	public Vector getVSYS_LINK_INVOICE_NO() {return VSYS_LINK_INVOICE_NO;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;}
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVINVOICE_REF() {return VINVOICE_REF;}
	public Vector getVLINE_NO() {return VLINE_NO;}
	public Vector getVLINE_DESC() {return VLINE_DESC;}
	public Vector getVUNIT() {return VUNIT;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVAMOUNT() {return VAMOUNT;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVBILLING_FREQ_NM() {return VBILLING_FREQ_NM;}
	public Vector getVFILE_UPLOAD_COUNT() {return VFILE_UPLOAD_COUNT;}
	public Vector getVUPLOADED_FILE_NAME() {return VUPLOADED_FILE_NAME;}
	
	public Vector getVAPPROVE_FLAG_CHECK() {return VAPPROVE_FLAG_CHECK;}
	public Vector getVCHECK_FLAG_CHECK() {return VCHECK_FLAG_CHECK;}
	public Vector getVAUTHORIZ_FLAG_CHECK() {return VAUTHORIZ_FLAG_CHECK;}
	public Vector getVIS_SUBMITTED() {return VIS_SUBMITTED;}
	public Vector getVAPPROVE_INVOICE_FLAG() {return VAPPROVE_INVOICE_FLAG;}
	public Vector getVPDF_INV_FLAG() {return VPDF_INV_FLAG;}

	public Vector getVOTH_COUNTERPTY_CD() {return VOTH_COUNTERPTY_CD;}
	public Vector getVOTH_COUNTERPTY_ABBR() {return VOTH_COUNTERPTY_ABBR;}
	public Vector getVOTH_CONT_NO() {return VOTH_CONT_NO;}
	public Vector getVOTH_CONT_REV_NO() {return VOTH_CONT_REV_NO;}
	public Vector getVOTH_AGMT_NO() {return VOTH_AGMT_NO;}
	public Vector getVOTH_AGMT_REV_NO() {return VOTH_AGMT_REV_NO;}
	public Vector getVOTH_CONTRACT_TYPE() {return VOTH_CONTRACT_TYPE;}
	public Vector getVOTH_DEAL_NO() {return VOTH_DEAL_NO;}
	public Vector getVOTH_PLANT_SEQ() {return VOTH_PLANT_SEQ;}
	public Vector getVOTH_PLANT_ABBR() {return VOTH_PLANT_ABBR;}
	public Vector getVOTH_BU_PLANT_SEQ() {return VOTH_BU_PLANT_SEQ;}
	public Vector getVOTH_BU_PLANT_ABBR() {return VOTH_BU_PLANT_ABBR;}
	public Vector getVOTH_START_DT() {return VOTH_START_DT;}
	public Vector getVOTH_END_DT() {return VOTH_END_DT;}
	public Vector getVOTH_CONT_NAME() {return VOTH_CONT_NAME;}
	public Vector getVOTH_CONT_REF_NO() {return VOTH_CONT_REF_NO;}
	public Vector getVOTH_PERIOD_START_DT() {return VOTH_PERIOD_START_DT;}
	public Vector getVOTH_PERIOD_END_DT() {return VOTH_PERIOD_END_DT;}
	public Vector getVOTH_INVOICE_NO() {return VOTH_INVOICE_NO;}
	public Vector getVOTH_DIS_INVOICE_NO() {return VOTH_DIS_INVOICE_NO;}
	public Vector getVOTH_STATUS() {return VOTH_STATUS;}
	public Vector getVOTH_BILLING_FREQ_FLAG() {return VOTH_BILLING_FREQ_FLAG;}
	public Vector getVOTH_BILLING_FREQ_NM() {return VOTH_BILLING_FREQ_NM;}
	public Vector getVOTH_INVOICE_SEQ() {return VOTH_INVOICE_SEQ;}
	public Vector getVOTH_INVOICE_TYPE() {return VOTH_INVOICE_TYPE;}
	public Vector getVOTH_FINANCIAL_YEAR() {return VOTH_FINANCIAL_YEAR;}
	public Vector getVOTH_FILE_UPLOAD_COUNT() {return VOTH_FILE_UPLOAD_COUNT;}
	public Vector getVOTH_UPLOADED_FILE_NAME() {return VOTH_UPLOADED_FILE_NAME;}
	
	public Vector getVOTH_APPROVE_FLAG_CHECK() {return VOTH_APPROVE_FLAG_CHECK;}
	public Vector getVOTH_CHECK_FLAG_CHECK() {return VOTH_CHECK_FLAG_CHECK;}
	public Vector getVOTH_AUTHORIZ_FLAG_CHECK() {return VOTH_AUTHORIZ_FLAG_CHECK;}
	public Vector getVOTH_IS_SUBMITTED() {return VOTH_IS_SUBMITTED;}
	public Vector getVOTH_APPROVE_INVOICE_FLAG() {return VOTH_APPROVE_INVOICE_FLAG;}
	public Vector getVOTH_PDF_INV_FLAG() {return VOTH_PDF_INV_FLAG;}
	public Vector getVOTH_ENTITY() {return VOTH_ENTITY;}
	
	public Vector getVMAIL_FROM_LIST() {return VMAIL_FROM_LIST;}
	public Vector getVMAIL_TO_LIST() {return VMAIL_TO_LIST;}
	public Vector getVMAIL_CC_LIST() {return VMAIL_CC_LIST;}
	public Vector getVMAIL_BCC_LIST() {return VMAIL_BCC_LIST;}
	public Vector getVMAIL_SUBJECT() {return VMAIL_SUBJECT;}
	public Vector getVMAIL_ATTACHMENT() {return VMAIL_ATTACHMENT;}
	public Vector getVMAIL_ATTACHMENT_PATH() {return VMAIL_ATTACHMENT_PATH;}
	public Vector getVMAIL_BODY() {return VMAIL_BODY;}

	public Vector getVSPLIT_FLAG() {return VSPLIT_FLAG;}
	public Vector getVSPLIT_VALUE() {return VSPLIT_VALUE;}
	
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVOTH_SAP_APPROVAL_FLAG() {return VOTH_SAP_APPROVAL_FLAG;}
	public Vector getVPAYMENT_TYPE_FLAG() {return VPAYMENT_TYPE_FLAG;}
	public Vector getVOTH_PAYMENT_TYPE_FLAG() {return VOTH_PAYMENT_TYPE_FLAG;}
	
	public Vector getVSG_MULTI_TAX_STRUCT() {return VSG_MULTI_TAX_STRUCT;}
	public Vector getVPG_MULTI_TAX_STRUCT() {return VPG_MULTI_TAX_STRUCT;}
	public Vector getVMULTI_TAX_STRUCT() {return VMULTI_TAX_STRUCT;}
	public Vector getVMAIN_MULTI_TAX_STRUCT() {return VMAIN_MULTI_TAX_STRUCT;}
	public Vector getVNEW_MULTI_TAX_STRUCT() {return VNEW_MULTI_TAX_STRUCT;}
	
	public Vector getVREMITTANCE_LIST_ABBR() {return VREMITTANCE_LIST_ABBR;}
	public Vector getVREMITTANCE_LIST_NAME() {return VREMITTANCE_LIST_NAME;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVBOE_NO() {return VBOE_NO;}
	public Vector getVBOE_NM() {return VBOE_NM;}
	
	public Vector getVREMITTANCE_NO() {return VREMITTANCE_NO;}
	public Vector getVOTH_REMITTANCE_NO() {return VOTH_REMITTANCE_NO;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVSHIP_NAME() {return VSHIP_NAME;}
	
	public Vector getVFILTER_CONT_TYPE() {return VFILTER_CONT_TYPE;}
	public Vector getVFILTER_CONT_NAME() {return VFILTER_CONT_NAME;}
	public Vector getVINVOICE_NO_LIST() {return VINVOICE_NO_LIST;}
	public Vector getVCRITERIA_FLAG() {return VCRITERIA_FLAG;}
	public Vector getVCRITERIA_NAME() {return VCRITERIA_NAME;}
	public Vector getVCRITERIA_HIDE() {return VCRITERIA_HIDE;}
	
	public Vector getVCRDR_CRITERIA() {return VCRDR_CRITERIA;}
	public Vector getVCREDIT_DEBIT_NO() {return VCREDIT_DEBIT_NO;}
	public Vector getVREF_NO() {return VREF_NO;}
	public Vector getVSGPG_TYPE() {return VSGPG_TYPE;}
	public Vector getVEMAIL_SENT() {return VEMAIL_SENT;}
	public Vector getVINV_RAISED_IN() {return VINV_RAISED_IN;}
	
	public Vector getVOTH_INVOICE_DT() {return VOTH_INVOICE_DT;}
	
	public Vector getVPDF_COL1() {return VPDF_COL1;}
	public Vector getVPDF_COL2() {return VPDF_COL2;}
	public Vector getVPDF_COL3() {return VPDF_COL3;}
	public Vector getVPDF_COL4() {return VPDF_COL4;}
	public Vector getVPDF_COL5() {return VPDF_COL5;}
	public Vector getVPDF_COL6() {return VPDF_COL6;}
	public Vector getVPDF_COL7() {return VPDF_COL7;}
	
	String billing_freq_nm="";
	String couterpty_abbr="";
	String couterpty_nm="";
	String deal_no="";
	String plant_abbr="";
	String bu_plant_abbr="";
	String qty_mmbtu="";
	String price="";
	String price_cd="";
	String price_cd_nm="";
	String energy_unit="";
	String energy_unit_nm="";
	String invoice_raised_in="";
	String invoice_raised_in_nm="";
	String payment_done_in="";
	String payment_done_in_nm="";
	String due_days="";
	String exchng_rate_cd="";
	String gross_amt="";
	String gross_amt1="";
	String exchang_rate="";
	String exchang_rate_dt="";
	String cont_start_dt="";
	String tax_amt="";
	String tax_struct_cd="";
	String tax_struct_dt="";
	String tax_struct_dtl="";
	String tax_info="";
	String tax_factor="";
	String invoice_seq="";
	String invoice_no="";
	String sys_invoice_no="";
	String invoice_ref="";
	String invoice_category="";
	String num_line="1";
	String linked_invoice="";
	String invoice_amt="";
	String net_payable="";
	String contact_person_cd="0";
	String contact_person_nm="";
	String bu_contact_person_cd="0";
	String bu_contact_person_nm="";
	String invoice_dt = "";
	String invoice_due_dt = "";
	String invoice_check_flag = "";
	String invoice_check_dt = "";
	String invoice_check_by = "";
	String invoice_check_nm = "";
	String invoice_auth_flag = "";
	String invoice_auth_dt = "";
	String invoice_auth_by = "";
	String invoice_auth_nm = "";
	String invoice_aprv_flag = "";
	String invoice_aprv_dt = "";
	String invoice_aprv_by = "";
	String invoice_aprv_nm = "";
	String invoice_adj_sign = "";
	String invoice_adj_amt = "";
	String financial_year="";
	String tax_on_txn_chrg="";
	String tax_txn_cd="";
	String tax_on_txn_chrg_info="";
	String txn_tax_struct_dtl="";
	String txn_tax_eff_dt="";
	String txn_charges="";
	String txn_amount="";
	String amount_in_word="";
	String tax_struct_info="";
	String applicable_flag="";
	String applicable_abbr="";
	String tcs_factor="";
	String tcs_amount="";
	String tcs_struct_cd="";
	String tcs_struct_dt="";
	String tcs_struct_info="";
	String tds_factor="";
	String tds_amount="";
	String tds_struct_cd="";
	String tds_struct_dt="";
	String tds_struct_info="";
	String cif_value="";
	String assessable_vlaue="";
	String remark="";
	
	String pg_qty_mmbtu="";
	String pg_price="";
	String pg_price_cd="";
	String pg_invoice_raised_in="";
	String pg_exchng_rate_cd="";
	String pg_gross_amt="";
	String pg_gross_amt1="";
	String pg_exchang_rate="";
	String pg_exchang_rate_dt="";
	String pg_tax_amt="";
	String pg_tax_struct_cd="";
	String pg_tax_struct_dt="";
	String pg_tax_info="";
	String pg_tax_struct_dtl="";
	String pg_invoice_seq="";
	String pg_invoice_no="";
	String pg_sys_invoice_no="";
	String pg_invoice_amt="";
	String pg_net_payable="";
	String pg_invoice_dt = "";
	String pg_invoice_due_dt = "";
	String pg_invoice_check_flag = "";
	String pg_invoice_check_dt = "";
	String pg_invoice_check_by = "";
	String pg_invoice_check_nm = "";
	String pg_invoice_auth_flag = "";
	String pg_invoice_auth_dt = "";
	String pg_invoice_auth_by = "";
	String pg_invoice_auth_nm = "";
	String pg_invoice_aprv_flag = "";
	String pg_invoice_aprv_dt = "";
	String pg_invoice_aprv_by = "";
	String pg_invoice_aprv_nm = "";
	String pg_invoice_adj_sign = "";
	String pg_invoice_adj_amt = "";
	String pg_financial_year="";
	String pg_tax_on_txn_chrg="";
	String pg_tax_txn_cd="";
	String pg_tax_on_txn_chrg_info="";
	String pg_txn_tax_struct_dtl="";
	String pg_txn_tax_eff_dt="";
	String pg_txn_charges="";
	String pg_txn_amount="";
	String pg_tcs_factor="";
	String pg_tcs_amount="";
	String pg_tcs_struct_cd="";
	String pg_tcs_struct_dt="";
	String pg_tds_factor="";
	String pg_tds_amount="";
	String pg_tds_struct_cd="";
	String pg_tds_struct_dt="";
	String pg_cif_value="";
	String pg_assessable_vlaue="";
	String pg_remark="";

	String note="";
	String other_inv_str="";
	String consider_due_dt_in="";
	String exclude_sat="";
	String holiday_state="";
	
	String plantAddress="";
	String plantCity="";
	String plantState="";
	String plantPin="";
	String plantNm="";
	
	String bu_plantAddress="";
	String bu_plantCity="";
	String bu_plantState="";
	String bu_plantPin="";
	String bu_plantNm="";
	
	String bu_tax_info="";
	String activity_value="";
	String contRef="";
	String cargoRef="";
	String signing_dt="";
	String top_heading_nm="";
	
	String exchang_criteria="";
	String exchng_rate_cal="";
	String total_InvoiceAmt="";
	String total_GrossAmt="";
	
	String cont_split_flag="";
	String agmtSigningDt="";
	String boe_nm="";
	
	String pd_bond="";
	String cd_value="";
	String pd_balance="";
	
	String final_qty="";
	String final_price="";
	String final_inv_amt="";
	String profm_qty="";
	String profm_price="";
	String profm_inv_amt="";
	String diff_qty="";
	String diff_price="";
	String diff_inv_amt="";
	String cd_paid_amt="";
	
	String pg_diff_price="";
	String pg_cd_paid_amt="";
	
	String sug_percentage="";
	String sug_qty="";
	String str_cargoname="";
	
	String invoice_head="";
	
	String transportation_charges="";
	String transportation_amount="";
	String pg_transportation_amount="";
	String gross_include_transport_tariff="";
	String pg_gross_include_transport_tariff="";
	String marketing_margin="";
	String marketing_margin_amount="";
	String pg_marketing_margin_amount="";
	String other_charges="";
	String other_charges_amount="";
	String pg_other_charges_amount="";
	
	String submitted_fiscal_yr="";
	
	String main_invoice_dt="";
	String main_invoice_due_dt="";
	String main_qty_mmbtu="";
	String main_price="";
	String main_gross_amt="";
	String main_gross_amt1="";
	String main_exchang_rate="";
	String main_transportation_charges="";
	String main_transportation_amount="";
	String main_marketing_margin="";
	String main_marketing_margin_amount="";
	String main_other_charges="";
	String main_other_charges_amount="";
	String main_gross_include_transport_tariff="";
	String main_tax_amt="";
	String main_tax_struct_cd="";
	String main_tax_struct_dt="";
	String main_invoice_amt="";
	String main_net_payable="";
	String main_tax_struct_dtl="";
	String remark_1="";
	String main_invoice_ref="";
	String main_tcs_amount="";
	String main_tcs_factor="";
	String main_tcs_struct_cd="";
	String main_tcs_struct_dt="";
	String main_tds_amount="";
	String main_tds_factor="";
	String main_tds_struct_cd="";
	String main_tds_struct_dt="";
	
	String new_qty_mmbtu="";
	String new_price="";
	String new_gross_amt1="";
	String new_exchang_rate="";
	String new_gross_amt="";
	String new_tax_amt="";
	String new_tax_struct_cd="";
	String new_tax_struct_dt = "";
	String new_tax_struct_dtl = "";
	String new_tax_info = "";
	String new_tax_factor = "";
	String new_invoice_amt="";
	String new_net_payable="";
	String new_tcs_amount="";
	String new_tcs_factor="";
	String new_tcs_struct_cd="";
	String new_tcs_struct_dt="";
	String new_tds_amount="";
	String new_tds_factor="";
	String new_tds_struct_cd="";
	String new_tds_struct_dt="";
	String new_transportation_charges="";
	String new_transportation_amount="";
	String new_marketing_margin="";
	String new_marketing_margin_amount="";
	String new_other_charges="";
	String new_other_charges_amount="";
	String new_gross_include_transport_tariff="";
	String criteri_formula="";
	
	String counterparty_nm ="";
	String counterparty_abbr ="";
	String reason="";
	String contract_ref="";
	String info = "";
	String split_flag = "";
	String split_value = "";
	String main_counterparty_cd = "";
	String main_plant_seq = "";
	String split_invoice_list="";
	
	String new_applicable_abbr="";
	String new_tds_amt="";
	String sg_rem_gen_status="";
	String pg_rem_gen_status="";
	
	public String getBilling_freq_nm() {return billing_freq_nm;}
	public String getCouterpty_abbr() {return couterpty_abbr;}
	public String getCouterpty_nm() {return couterpty_nm;}
	public String getDeal_no() {return deal_no;}
	public String getPlant_abbr() {return plant_abbr;}
	public String getBu_plant_abbr() {return bu_plant_abbr;}
	public String getQty_mmbtu() {return qty_mmbtu;}
	public String getPrice() {return price;}
	public String getPrice_cd() {return price_cd;}
	public String getPrice_cd_nm() {return price_cd_nm;}
	public String getEnergy_unit() {return energy_unit;}
	public String getEnergy_unit_nm() {return energy_unit_nm;}
	public String getInvoice_raised_in() {return invoice_raised_in;}
	public String getInvoice_raised_in_nm() {return invoice_raised_in_nm;}
	public String getPayment_done_in() {return payment_done_in;}
	public String getPayment_done_in_nm() {return payment_done_in_nm;}
	public String getDue_days() {return due_days;}
	public String getExchng_rate_cd() {return exchng_rate_cd;}
	public String getGross_amt() {return gross_amt;}
	public String getGross_amt1() {return gross_amt1;}
	public String getExchang_rate() {return exchang_rate;}
	public String getExchang_rate_dt() {return exchang_rate_dt;}
	public String getCont_start_dt() {return cont_start_dt;}
	public String getTax_amt() {return tax_amt;}
	public String getTax_struct_cd() {return tax_struct_cd;}
	public String getTax_struct_dt() {return tax_struct_dt;}
	public String getTax_struct_dtl() {return tax_struct_dtl;}
	public String getTax_info() {return tax_info;}
	public String getTax_factor() {return tax_factor;}
	public String getInvoice_seq() {return invoice_seq;}
	public String getInvoice_no() {return invoice_no;}
	public String getSys_invoice_no() {return sys_invoice_no;}
	public String getInvoice_ref() {return invoice_ref;}
	public String getInvoice_category() {return invoice_category;}
	public String getNum_line() {return num_line;}
	public String getLinked_invoice() {return linked_invoice;}
	public String getInvoice_amt() {return invoice_amt;}
	public String getNet_payable() {return net_payable;}
	public String getContact_person_cd() {return contact_person_cd;}
	public String getContact_person_nm() {return contact_person_nm;}
	public String getBu_contact_person_cd() {return bu_contact_person_cd;}
	public String getBu_contact_person_nm() {return bu_contact_person_nm;}
	public String getInvoice_dt() {return invoice_dt;}
	public String getInvoice_due_dt() {return invoice_due_dt;}
	public String getInvoice_check_flag() {return invoice_check_flag;}
	public String getInvoice_check_dt() {return invoice_check_dt;}
	public String getInvoice_check_by() {return invoice_check_by;}
	public String getInvoice_check_nm() {return invoice_check_nm;}
	public String getInvoice_auth_flag() {return invoice_auth_flag;}
	public String getInvoice_auth_dt() {return invoice_auth_dt;}
	public String getInvoice_auth_by() {return invoice_auth_by;}
	public String getInvoice_auth_nm() {return invoice_auth_nm;}
	public String getInvoice_aprv_flag() {return invoice_aprv_flag;}
	public String getInvoice_aprv_dt() {return invoice_aprv_dt;}
	public String getInvoice_aprv_by() {return invoice_aprv_by;}
	public String getInvoice_aprv_nm() {return invoice_aprv_nm;}
	public String getInvoice_adj_sign() {return invoice_adj_sign;}
	public String getInvoice_adj_amt() {return invoice_adj_amt;}
	public String getFinancial_year() {return financial_year;}
	public String getTax_on_txn_chrg() {return tax_on_txn_chrg;}
	public String getTax_txn_cd() {return tax_txn_cd;}
	public String getTax_on_txn_chrg_info() {return tax_on_txn_chrg_info;}
	public String getTxn_tax_struct_dtl() {return txn_tax_struct_dtl;}
	public String getTxn_tax_eff_dt() {return txn_tax_eff_dt;}
	public String getTxn_charges() {return txn_charges;}
	public String getTxn_amount() {return txn_amount;}
	public String getAmount_in_word() {return amount_in_word;}
	public String getTax_struct_info() {return tax_struct_info;}
	public String getApplicable_flag() {return applicable_flag;}
	public String getApplicable_abbr() {return applicable_abbr;}
	public String getTcs_factor() {return tcs_factor;}
	public String getTcs_amount() {return tcs_amount;}
	public String getTcs_struct_cd() {return tcs_struct_cd;}
	public String getTcs_struct_dt() {return tcs_struct_dt;}
	public String getTcs_struct_info() {return tcs_struct_info;}
	public String getTds_factor() {return tds_factor;}
	public String getTds_amount() {return tds_amount;}
	public String getTds_struct_cd() {return tds_struct_cd;}
	public String getTds_struct_dt() {return tds_struct_dt;}
	public String getTds_struct_info() {return tds_struct_info;}
	public String getCif_value() {return cif_value;}
	public String getAssessable_vlaue() {return assessable_vlaue;}
	public String getRemark() {return remark;}
	
	public String getPg_qty_mmbtu() {return pg_qty_mmbtu;}
	public String getPg_price() {return pg_price;}
	public String getPg_price_cd() {return price_cd;}
	public String getPg_invoice_raised_in() {return pg_invoice_raised_in;}
	public String getPg_exchng_rate_cd() {return pg_exchng_rate_cd;}
	public String getPg_gross_amt() {return pg_gross_amt;}
	public String getPg_gross_amt1() {return pg_gross_amt1;}
	public String getPg_exchang_rate() {return pg_exchang_rate;}
	public String getPg_exchang_rate_dt() {return pg_exchang_rate_dt;}
	public String getPg_tax_amt() {return pg_tax_amt;}
	public String getPg_tax_struct_cd() {return pg_tax_struct_cd;}
	public String getPg_tax_struct_dt() {return pg_tax_struct_dt;}
	public String getPg_tax_struct_dtl() {return pg_tax_struct_dtl;}
	public String getPg_tax_info() {return pg_tax_info;}
	public String getPg_invoice_seq() {return pg_invoice_seq;}
	public String getPg_invoice_no() {return pg_invoice_no;}
	public String getPg_sys_invoice_no() {return pg_sys_invoice_no;}
	public String getPg_invoice_amt() {return pg_invoice_amt;}
	public String getPg_net_payable() {return pg_net_payable;}
	public String getPg_invoice_dt() {return pg_invoice_dt;}
	public String getPg_invoice_due_dt() {return pg_invoice_due_dt;}
	public String getPg_invoice_check_flag() {return pg_invoice_check_flag;}
	public String getPg_invoice_check_dt() {return pg_invoice_check_dt;}
	public String getPg_invoice_check_by() {return pg_invoice_check_by;}
	public String getPg_invoice_check_nm() {return pg_invoice_check_nm;}
	public String getPg_invoice_auth_flag() {return pg_invoice_auth_flag;}
	public String getPg_invoice_auth_dt() {return pg_invoice_auth_dt;}
	public String getPg_invoice_auth_by() {return pg_invoice_auth_by;}
	public String getPg_invoice_auth_nm() {return pg_invoice_auth_nm;}
	public String getPg_invoice_aprv_flag() {return pg_invoice_aprv_flag;}
	public String getPg_invoice_aprv_dt() {return pg_invoice_aprv_dt;}
	public String getPg_invoice_aprv_by() {return pg_invoice_aprv_by;}
	public String getPg_invoice_aprv_nm() {return pg_invoice_aprv_nm;}
	public String getPg_invoice_adj_sign() {return pg_invoice_adj_sign;}
	public String getPg_invoice_adj_amt() {return pg_invoice_adj_amt;}
	public String getPg_financial_year() {return pg_financial_year;}
	public String getPg_tax_on_txn_chrg() {return pg_tax_on_txn_chrg;}
	public String getPg_tax_txn_cd() {return pg_tax_txn_cd;}
	public String getPg_tax_on_txn_chrg_info() {return pg_tax_on_txn_chrg_info;}
	public String getPg_txn_tax_struct_dtl() {return pg_txn_tax_struct_dtl;}
	public String getPg_txn_tax_eff_dt() {return pg_txn_tax_eff_dt;}
	public String getPg_txn_charges() {return pg_txn_charges;}
	public String getPg_txn_amount() {return pg_txn_amount;}
	public String getPg_tcs_amount() {return pg_tcs_amount;}
	public String getPg_tcs_factor() {return pg_tcs_factor;}
	public String getPg_tcs_struct_cd() {return pg_tcs_struct_cd;}
	public String getPg_tcs_struct_dt() {return pg_tcs_struct_dt;}
	public String getPg_tds_amount() {return pg_tds_amount;}
	public String getPg_tds_factor() {return pg_tds_factor;}
	public String getPg_tds_struct_cd() {return pg_tds_struct_cd;}
	public String getPg_tds_struct_dt() {return pg_tds_struct_dt;}
	public String getPg_cif_value() {return pg_cif_value;}
	public String getPg_assessable_vlaue() {return pg_assessable_vlaue;}
	public String getPg_remark() {return pg_remark;}

	public String getNote() {return note;}
	public String getOther_inv_str() {return other_inv_str;}
	public String getConsider_due_dt_in() {return consider_due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getHoliday_state() {return holiday_state;}
	
	public String getPlantAddress() {return plantAddress;}
	public String getPlantCity() {return plantCity;}
	public String getPlantState() {return plantState;}
	public String getPlantPin() {return plantPin;}
	public String getPlantNm() {return plantNm;}
	
	public String getBu_plantAddress() {return bu_plantAddress;}
	public String getBu_plantCity() {return bu_plantCity;}
	public String getBu_plantState() {return bu_plantState;}
	public String getBu_plantPin() {return bu_plantPin;}
	public String getBu_plantNm() {return bu_plantNm;}
	
	public String getBu_tax_info() {return bu_tax_info;}
	public String getActivity_value() {return activity_value;}
	
	public String getContRef() {return contRef;}
	public String getSigning_dt() {return signing_dt;}
	public String getTop_heading_nm() {return top_heading_nm;}
	public String getCargoRef() {return cargoRef;}
	
	public String getExchang_criteria() {return exchang_criteria;}
	public String getExchng_rate_cal() {return exchng_rate_cal;}
	public String getTotal_InvoiceAmt() {return total_InvoiceAmt;}
	public String getTotal_GrossAmt() {return total_GrossAmt;}
	
	public String getCont_split_flag() {return cont_split_flag;}
	public String getAgmtSigningDt() {return agmtSigningDt;}
	public String getBoe_nm() {return boe_nm;}
	
	public String getPd_bond() {return pd_bond;}
	public String getCd_value() {return cd_value;}
	public String getPd_balance() {return pd_balance;}
	
	public String getFinal_qty() {return final_qty;}
	public String getFinal_price() {return final_price;}
	public String getFinal_inv_amt() {return final_inv_amt;}
	public String getProfm_qty() {return profm_qty;}
	public String getProfm_price() {return profm_price;}
	public String getProfm_inv_amt() {return profm_inv_amt;}
	public String getDiff_qty() {return diff_qty;}
	public String getDiff_price() {return diff_price;}
	public String getDiff_inv_amt() {return diff_inv_amt;}
	public String getCd_paid_amt() {return cd_paid_amt;}
	
	public String getPg_diff_price() {return pg_diff_price;}
	public String getPg_cd_paid_amt() {return pg_cd_paid_amt;}
	
	public String getSug_percentage() {return sug_percentage;}
	public String getSug_qty() {return sug_qty;}
	public String getStr_cargoname() {return str_cargoname;}
	
	public String getInvoice_head() {return invoice_head;}
	
	public String getTransportation_charges() {return transportation_charges;}
	public String getTransportation_amount() {return transportation_amount;}
	public String getPg_transportation_amount() {return pg_transportation_amount;}
	public String getGross_include_transport_tariff() {return gross_include_transport_tariff;}
	public String getPg_gross_include_transport_tariff() {return pg_gross_include_transport_tariff;}
	public String getMarketing_margin() {return marketing_margin;}
	public String getMarketing_margin_amount() {return marketing_margin_amount;}
	public String getPg_marketing_margin_amount() {return pg_marketing_margin_amount;}
	public String getOther_charges() {return other_charges;}
	public String getOther_charges_amount() {return other_charges_amount;}
	public String getPg_other_charges_amount() {return pg_other_charges_amount;}
	
	public String getMain_invoice_dt() {return main_invoice_dt;}
	public String getMain_invoice_due_dt() {return main_invoice_due_dt;}
	public String getMain_qty_mmbtu() {return main_qty_mmbtu;}
	public String getMain_price() {return main_price;}
	public String getMain_gross_amt() {return main_gross_amt;}
	public String getMain_gross_amt1() {return main_gross_amt1;}
	public String getMain_exchang_rate() {return main_exchang_rate;}
	public String getMain_transportation_charges() {return main_transportation_charges;}
	public String getMain_transportation_amount() {return main_transportation_amount;}
	public String getMain_marketing_margin() {return main_marketing_margin;}
	public String getMain_marketing_margin_amount() {return main_marketing_margin_amount;}
	public String getMain_other_charges() {return main_other_charges;}
	public String getMain_other_charges_amount() {return main_other_charges_amount;}
	public String getMain_gross_include_transport_tariff() {return main_gross_include_transport_tariff;}
	public String getMain_tax_amt() {return main_tax_amt;}
	public String getMain_tax_struct_cd() {return main_tax_struct_cd;}
	public String getMain_tax_struct_dt() {return main_tax_struct_dt;}
	public String getMain_invoice_amt() {return main_invoice_amt;}
	public String getMain_net_payable() {return main_net_payable;}
	public String getMain_tax_struct_dtl() {return main_tax_struct_dtl;}
	public String getRemark_1() {return remark_1;}
	public String getSgpg_type() {return sgpg_type;}
	public String getMain_invoice_ref() {return main_invoice_ref;}
	public String getMain_tcs_amount() {return main_tcs_amount;}
	public String getMain_tcs_factor() {return main_tcs_factor;}
	public String getMain_tcs_struct_cd() {return main_tcs_struct_cd;}
	public String getMain_tcs_struct_dt() {return main_tcs_struct_dt;}
	public String getMain_tds_amount() {return main_tds_amount;}
	public String getMain_tds_factor() {return main_tds_factor;}
	public String getMain_tds_struct_cd() {return main_tds_struct_cd;}
	public String getMain_tds_struct_dt() {return main_tds_struct_dt;}
	
	public String getNew_qty_mmbtu() {return new_qty_mmbtu;}
	public String getNew_price() {return new_price;}
	public String getNew_gross_amt1() {return new_gross_amt1;}
	public String getNew_exchang_rate() {return new_exchang_rate;}
	public String getNew_gross_amt() {return new_gross_amt;}
	public String getNew_tax_amt() {return new_tax_amt;}
	public String getNew_tax_struct_cd() {return new_tax_struct_cd;}
	public String getNew_tax_struct_dt() { return new_tax_struct_dt; }
	public String getNew_tax_struct_dtl() { return new_tax_struct_dtl; }
    public String getNew_tax_info() { return new_tax_info; }
    public String getNew_tax_factor() { return new_tax_factor; }
	public String getNew_invoice_amt() {return new_invoice_amt;}
	public String getNew_net_payable() {return new_net_payable;}
	public String getNew_tcs_amount() {return new_tcs_amount;}
	public String getNew_tcs_factor() {return new_tcs_factor;}
	public String getNew_tcs_struct_cd() {return new_tcs_struct_cd;}
	public String getNew_tcs_struct_dt() {return new_tcs_struct_dt;}
	public String getNew_tds_amount() {return new_tds_amount;}
	public String getNew_tds_factor() {return new_tds_factor;}
	public String getNew_tds_struct_cd() {return new_tds_struct_cd;}
	public String getNew_tds_struct_dt() {return new_tds_struct_dt;}
	public String getNew_transportation_charges() {return new_transportation_charges;}
	public String getNew_transportation_amount() {return new_transportation_amount;}
	public String getNew_marketing_margin() {return new_marketing_margin;}
	public String getNew_marketing_margin_amount() {return new_marketing_margin_amount;}
	public String getNew_other_charges() {return new_other_charges;}
	public String getNew_other_charges_amount() {return new_other_charges_amount;}
	public String getNew_gross_include_transport_tariff() {return new_gross_include_transport_tariff;}
	public String getCriteri_formula() {return criteri_formula;}
	
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getReason() {return reason;}
	public String getContract_ref() {return contract_ref;}
	public String getInfo() {return info;}
	public String getSplit_flag() {return split_flag;}
	public String getSplit_value() {return split_value;}
	public String getSplit_invoice_list() {return split_invoice_list;}
	
	public String getNew_applicable_abbr() {return new_applicable_abbr;}
	public String getNew_tds_amt() {return new_tds_amt;}
	
	public String getSubmitted_fiscal_yr() {return submitted_fiscal_yr;}
	public String getSg_rem_gen_status() {return sg_rem_gen_status;}
	public String getPg_rem_gen_status() {return pg_rem_gen_status;}
	
	HashMap<String, String> invoice_data = new HashMap<String, String>();

	public HashMap<String, String> getInvoice_data() {return invoice_data;}

	boolean submission_chk = false;
	boolean sg_submission_chk = false;
	boolean pg_submission_chk = false;
	boolean isGrossIncTriff = false;
	boolean isMainGrossIncTriff = false;
	boolean isNewGrossIncTriff = false;

	public boolean getSubmission_chk() {return submission_chk;}
	public boolean getSg_submission_chk() {return sg_submission_chk;}
	public boolean getPg_submission_chk() {return pg_submission_chk;}
	public boolean getIsGrossIncTriff() {return isGrossIncTriff;}
	public boolean getIsMainGrossIncTriff() {return isMainGrossIncTriff;}
	public boolean getIsNewGrossIncTriff() {return isNewGrossIncTriff;}
	
	String changed_qty_mmbtu = "";
    
    public String getChanged_qty_mmbtu() { return changed_qty_mmbtu; }
}
