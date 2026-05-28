package com.etrm.fms.gta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
//CR Date			: 28/03/2022 
//Status	  		: Developing
public class DataBean_GtaMaster 
{
	String db_src_file_name="DataBean_GtaMaster.java";
	Connection conn;  
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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
	    			if(callFlag.equalsIgnoreCase("GTA_AGREEMENT"))
	    			{
	    				getTransporterMst();
	    				getGtaAgreementRpt();
	    				getEntryPointList();
	    				getExitPointList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTPA_AGMT"))
	    			{
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getagmtTransporterlist();
	    				}
	    				else if(opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getTransporterMst();
	    				}
	    				getEntryPointList();
	    				getExitPointList();
	    				getTransporterBuList();
	    				getBusinessPlantList();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getGtpaAgreementDetail();
	    					getCountGtpaBillingDetail();
	    					getGtaAgmtSelectedTransBuList();
	    					getGtaAgmtSelectedBuList();
	    				}
	    				getchkContBuUnit();
	    				getchkContTransBu();
	    				getchkContEntryPoint();
	    				getchkContEXITPoint();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTPA_AGMT_LIST"))
	    			{
	    				getGtpaAgmtList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_CONTRACT"))
	    			{
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getGtaContractTransporterMst();
	    				}
	    				else 
	    				{
	    					getContractTransporterMst();
	    				}
	    				getContEntryPointList();
	    				getContExitPointList();
	    				getContBusinessPlantList();
	    				getContTransporterBuList();
	    				getCustomerMst();
	    				getGtaContractDetail();
	    				getSelectedGtaAgreementDetail();
	    				getSalesContDtlForGtaContract();
	    				getGtaContractSelectedBusinessPlantList();
	    				getGtaContractSelectedTransBuList();
	    				getCountContractBillingDetail();
	    				getCountNomination();
	    				getMaxAllocationInvoiceDate();
	    				getNominatedChk();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PARKING_CONTRACT"))
	    			{
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getGtaContractTransporterMst();
	    				}
	    				else 
	    				{
	    					getContractTransporterMst();
	    				}
	    				getContEntryPointList();
	    				getContExitPointList();
	    				getContBusinessPlantList();
	    				getContTransporterBuList();
	    				getParkingContractDetail();
	    				getSelectedGtaAgreementDetail();
	    				getGtaContDtlForParkingContract();
	    				getGtaContractSelectedBusinessPlantList();
	    				getGtaContractSelectedTransBuList();
	    				getCountContractBillingDetail();
	    				getMaxAllocationInvoiceDate();
	    				getNominatedChk();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_AGREEMENT_LIST"))
	    			{
	    				getGtaAgreementDetailList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_CONTRACT_LIST"))
	    			{
	    				getGtaContractDetailList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_CONTRACT_LIST_FOR_NOM"))
	    			{
	    				getGtaContractDetailListForNom();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_CONT_LIST"))
	    			{
	    				getSalesContList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CT_CONT_LIST"))
	    			{
	    				getCTContList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTC_NOM_SCHE_ALLOC"))
	    			{
	    				getCounterpartyList();
	    				getCustomerList(); //THIS APPLICABLE FOR CUSTOMER GTC 
	    				getSelectedGtcContractDetail();
	    				getForthnightlyDateRang();
	    				if(gtc_type.equals("N"))
	    				{
	    					getGtcNominationDetail();
	    				}
	    				else if(gtc_type.equals("S"))
	    				{
	    					getGtcSchedulingDetail();
	    				}
	    				else if(gtc_type.equals("A"))
	    				{
	    					getGtcAllocationDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTC_IMBALANCE"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				getCounterpartyList();
	    				getCustomerList(); //THIS APPLICABLE FOR CUSTOMER GTC
	    				getSelectedGtcContractDetail();
	    				getForthnightlyDateRang();
	    				getGtcImbalanceReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PARKING_IMBALANCE"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				getCounterpartyList();
	    				getSelectedGtcContractDetail();
	    				getForthnightlyDateRang();
	    				getParkingImbalanceReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTC_CT_LIST_MST"))
	    			{
	    				getCTMasterList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL"))
	    			{
	    				counterparty_abbr = utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
	    				getCountContractBillingDetail();
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getContractBillingDetail();
	    				getContractPlantWiseApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTPA_BILLING_DTL"))
	    			{
	    				counterparty_abbr = utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				deal_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, "", agreement_type);
	    				getCountGtpaBillingDetail();
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getGtpaBillingDetail();
	    				getApplicableTaxes();
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getNominatedChk()
	{
		String function_nm="getNomintatedChk()";
		try
		{
			
			for (int i = 0; i < VBU_PLANT_SEQ_NO.size(); i++) 
			{
				VNOM_SEL_BU_CHK.add(checkCustPlantNominated("" + VBU_PLANT_SEQ_NO.elementAt(i),"BU"));
			}
			for(int i=0;i<VTRANS_BU_SEQ_NO.size();i++)		//for selected plant 
			{
				VNOM_SEL_TRANS_BU_CHK.add(checkTransPlantInvoice(""+VTRANS_BU_SEQ_NO.elementAt(i)));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String checkCustPlantNominated(String plant_seq, String type)
	{
		String function_nm="checkCustPlantNominated()";
		String chk_cust_plant="";
		try
		{
			String plant = "";
			if ("BU".equals(type)) 
			{
			    plant = "SELECT CASE WHEN BU_SEQ=? THEN 'Y' ELSE 'N' END BU_UNIT ";
			} 
			/*else 
			{
			    plant = "SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT ";
			}*/
			String query=plant
				+ "FROM FMS_DAILY_TRANSPORTER_NOM "
				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
				+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
			if ("BU".equals(type)) 
			{
				query += "AND BU_SEQ=? ";
			} 
			else 
			{
				//query += "AND PLANT_SEQ=? ";
			}
			String temp_query=query;
			stmt1=conn.prepareStatement(temp_query);
			stmt1.setString(1, plant_seq);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, counterparty_cd);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, cont_no);
			stmt1.setString(6, contract_type);
			if ("BU".equals(type)) 
			{
				stmt1.setString(7, plant_seq);
			}
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				chk_cust_plant=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_cust_plant;
	}
	
	public void getMaxAllocationInvoiceDate()
	{
		String function_nm="getMaxAllocationInvoiceDate()";
		try
		{
			String queryString1="SELECT TO_CHAR(MIN(MIN_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MIN(GAS_DT) MIN_DATE FROM FMS_DAILY_TRANSPORTER_NOM "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, agmt_no);
			stmt1.setString(4, cont_no);
			stmt1.setString(5, contract_type);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				min_nom_date=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
			
			String queryString2="SELECT TO_CHAR(MAX(MAX_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DAILY_TRANSPORTER_ALLOC "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, agmt_no);
			stmt2.setString(4, cont_no);
			stmt2.setString(5, contract_type);
			stmt2.setString(6, comp_cd);
			stmt2.setString(7, counterparty_cd);
			stmt2.setString(8, agmt_no);
			stmt2.setString(9, cont_no);
			stmt2.setString(10, contract_type);
			stmt2.setString(11, comp_cd);
			stmt2.setString(12, counterparty_cd);
			stmt2.setString(13, agmt_no);
			stmt2.setString(14, cont_no);
			stmt2.setString(15, contract_type);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				max_date=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractPlantWiseApplicableTaxes()
	{
		String function_nm="getContractPlantWiseApplicableTaxes()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			if(dateUtil.getDays(sysdate, cont_end_dt)>1)
			{
				sysdate=cont_end_dt;
			}
			
			String periodStDt=utilBean.getFirstDtOfBillingCycle(billing_freq, "", sysdate);			
			if(dateUtil.getDays(periodStDt, cont_start_dt)<1)
			{
				periodStDt=cont_start_dt;
			}
						
			String queryString2="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_GTA_CONT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, cont_no);
			stmt2.setString(4, agmt_no);
			stmt2.setString(5, contract_type);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_GTA_CONT_TRANS_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_GTA_CONT_TRANS_BU B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE,A.INVOICE_TYPE "
							+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A,FMS_TAX_STRUCTURE B "
							+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? "
							+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BU_SVC_TAX_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.INVOICE_TYPE=B.INVOICE_TYPE) "
							+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD ";
					if(contract_type.equals("C") || contract_type.equals("R"))
					{
						queryString1+="AND A.INVOICE_TYPE IN ('IC','TC') ";
					}
					else if(contract_type.equals("K"))
					{
						queryString1+="AND A.INVOICE_TYPE IN ('PC') ";
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "R");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String inv_type=rset1.getString(4)==null?"":rset1.getString(4);
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
						VPLANT_NAME.add(""+utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R"));
						VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VINVOICE_CATEGORY.add(""+utilBean.getInvoiceCategory("S"));
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("R","S",inv_type));
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getApplicableTaxes()
	{
		String function_nm="getApplicableTaxes()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			if(dateUtil.getDays(sysdate, cont_end_dt)>1)
			{
				sysdate=cont_end_dt;
			}
			String periodStDt=utilBean.getFirstDtOfBillingCycle(billing_freq, "", sysdate);
			
			// Addresses TAX Population for contract starting in middle of Billing Period
			if(dateUtil.getDays(periodStDt, cont_start_dt)<1) 
			{
				periodStDt=cont_start_dt;
			}
						
			String queryString2="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_AGMT_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(AGMT_REV) FROM FMS_GTA_AGMT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, agreement_type);
			stmt2.setString(4, agmt_no);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_GTA_AGMT_TRANS_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(AGMT_REV) FROM FMS_GTA_AGMT_TRANS_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT TAX_STRUCT_CD,TAX_STRUCT_DTL,INVOICE_TYPE "
							+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BU_SVC_TAX_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.INVOICE_TYPE=B.INVOICE_TYPE) ";
					if(agreement_type.equals("G"))
					{
						queryString1+="AND INVOICE_TYPE IN ('IC','TC') ";
					}
					else if(agreement_type.equals("P"))
					{
						queryString1+="AND INVOICE_TYPE IN ('PC') ";
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "R");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String inv_type=rset1.getString(3)==null?"":rset1.getString(3);
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VPLANT_NAME.add(""+utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R"));
						VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VINVOICE_CATEGORY.add(""+utilBean.getInvoiceCategory("S"));
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("R","S",inv_type));
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaAgmtSelectedTransBuList()
	{
		String function_nm="getGtaAgmtSelectedTransBuList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_AGMT_TRANS_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VSEL_TRANS_BU_SEQ_NO.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaAgmtSelectedBuList()
	{
		String function_nm="getGtaAgmtSelectedBuList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_AGMT_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(AGMT_REV) FROM FMS_GTA_AGMT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VSEL_BU_PLANT_SEQ_NO.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_TRANS_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_TRANS_BU B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
					+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
				VSELECTED_PLANT_ABBR.add(plant_abbr);
				VSELECTED_PLANT_SEQ.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedAgmtPlantlist() 
	{
		String function_nm="getSelectedAgmtPlantlist()";
		try
		{
			
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_AGMT_TRANS_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_TYPE=? "
					+ "AND A.AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_TRANS_BU B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
					+ "A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
				VSELECTED_PLANT_ABBR.add(plant_abbr);
				VSELECTED_PLANT_SEQ.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getStateMst()
	{
		String function_nm="getStateMst()";
		try
		{
			utilBean.getStateMaster(conn);
			VSTATE_CODE = utilBean.getTIN();
			VSTATE_NM = utilBean.getSTATE_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getagmtTransporterlist()
	{
		String function_nm="getagmtTransporterlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_GTA_AGMT_MST "
					+ "WHERE COMPANY_CD=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cp_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(cp_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,cp_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,cp_cd));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getTransporterMst()
	{
		String function_nm="getTransporterMst()";
		try
		{
			//utilBean.getEffectiveTransporterCounterpartyList("KYC",comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,"KYC",comp_cd,"R");
			//utilBean.getAllEntityCounterpartyList(conn,"KYC",comp_cd,"R");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractTransporterMst()
	{
		String function_nm="getContractTransporterMst()";
		try
		{
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd); 
			stmt.setString(2, agreement_type); 
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cp_cd = rset.getString(1)==null?"":rset.getString(1);
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, cp_cd, dateUtil.getSysdate()).get("status");
				
				if(cpStatus.equals("Y"))
				{
					VCOUNTERPARTY_CD.add(cp_cd);
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,cp_cd));
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,cp_cd));
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
	
	public void getGtaAgreementRpt()
	{
		String function_nm="getGtaAgreementRpt()";
		try
		{
			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd); 
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(2)==null?"":rset.getString(2));
				VAGMT_REV_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VSTART_DT.add(rset.getString(4)==null?"Y":rset.getString(4));
				VEND_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VSTATUS.add(rset.getString(6)==null?"":rset.getString(6));
				VTOTAL_QTY.add(nf.format(rset.getDouble(7)));
				VUNIT_CD.add(rset.getString(8)==null?"":rset.getString(8));
				VCALC_BASE.add(rset.getString(9)==null?"GCV":rset.getString(9));
				VAGMT_NAME.add(rset.getString(10)==null?"":rset.getString(10));
				
				String sel_entry_mapping="";
				queryString1="SELECT TRANSPORTER_CD,PLANT_SEQ "
						+ "FROM FMS_GTA_ENTRY_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmt);
				stmt1.setString(4, agmt_rev);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String entry_countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String plantSeq=rset1.getString(2)==null?"":rset1.getString(2);
					
					String mapping=entry_countpty_cd+"-"+plantSeq;
					
					if(mapping.equals(""))
					{
						sel_entry_mapping+=""+mapping;
					}
					else
					{
						sel_entry_mapping+="@@"+mapping;
					}
				}
				rset1.close();
				stmt1.close();
				
				String sel_exit_mapping="";
				queryString2="SELECT ENTITY_CD,PLANT_SEQ,ENTITY "
						+ "FROM FMS_GTA_EXIT_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, agmt);
				stmt2.setString(4, agmt_rev);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String exit_countpty_cd=rset2.getString(1)==null?"":rset2.getString(1);
					String plantSeq=rset2.getString(2)==null?"":rset2.getString(2);
					String entity=rset2.getString(3)==null?"":rset2.getString(3);
					
					String mapping=entity+"-"+exit_countpty_cd+"-"+plantSeq;
					
					if(mapping.equals(""))
					{
						sel_exit_mapping+=""+mapping;
					}
					else
					{
						sel_exit_mapping+="@@"+mapping;
					}
				}
				rset2.close();
				stmt2.close();
				
				VSEL_ENTRY_MAPPING.add(sel_entry_mapping);
				VSEL_EXIT_MAPPING.add(sel_exit_mapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtpaAgmtList()
	{
		String function_nm="getGtpaAgmtList()";
		try
		{
			int cnt=0;
			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME,AGMT_TYPE "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			if(!agreement_type.equals("") && !agreement_type.equals("0"))
			{
				queryString += "AND AGMT_TYPE=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
						+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME,AGMT_TYPE "
						+ "FROM FMS_GTA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
				if(!agreement_type.equals("") && !agreement_type.equals("0"))
				{
					queryString += "AND AGMT_TYPE=?";
				}
				queryString += "AND END_DT >= SYSDATE AND STATUS NOT IN ('Y') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND STATUS IN ('Y') ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd); 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++cnt, counterparty_cd);
			}
			if(!agreement_type.equals("") && !agreement_type.equals("0"))
			{
				stmt.setString(++cnt, agreement_type);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, from_dt);
				
				stmt.setString(++cnt, comp_cd); 
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++cnt, counterparty_cd);
				}
				if(!agreement_type.equals("") && !agreement_type.equals("0"))
				{
					stmt.setString(++cnt, agreement_type);
				}
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(2)==null?"":rset.getString(2));
				VAGMT_REV_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VSTART_DT.add(rset.getString(4)==null?"Y":rset.getString(4));
				VEND_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VSTATUS.add(rset.getString(6)==null?"":rset.getString(6));
				VTOTAL_QTY.add(nf.format(rset.getDouble(7)));
				VUNIT_CD.add(rset.getString(8)==null?"":rset.getString(8));
				VCALC_BASE.add(rset.getString(9)==null?"GCV":rset.getString(9));
				VAGMT_NAME.add(rset.getString(10)==null?"":rset.getString(10));
				VAGMT_TYPE.add(rset.getString(11)==null?"":rset.getString(11));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtpaAgreementDetail()
	{
		String function_nm="getGtpaAgreementDetail()";
		try
		{
			int cnt=0;
			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME,AGMT_TYPE "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd); 
			stmt.setString(++cnt, counterparty_cd);
			stmt.setString(++cnt, agreement_type);
			stmt.setString(++cnt, agmt_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
				agmt_no=rset.getString(2)==null?"":rset.getString(2);
				agmt_rev_no=rset.getString(3)==null?"":rset.getString(3);
				start_dt=rset.getString(4)==null?"":rset.getString(4);
				end_dt=rset.getString(5)==null?"":rset.getString(5);
				agmt_status=rset.getString(6)==null?"":rset.getString(6);
				agmt_trans_qty=rset.getString(7)==null?"":rset.getString(7);
				agmt_trans_qty_unit=rset.getString(8)==null?"":rset.getString(8);
				agmt_calc_base=rset.getString(9)==null?"":rset.getString(9);
				agmt_name=rset.getString(10)==null?"":rset.getString(10);
				agreement_type=rset.getString(11)==null?"":rset.getString(11);
				
				sel_entry_mapping="";
				queryString1="SELECT TRANSPORTER_CD,PLANT_SEQ "
						+ "FROM FMS_GTA_ENTRY_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev_no);
				stmt1.setString(5, agreement_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String entry_countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String plantSeq=rset1.getString(2)==null?"":rset1.getString(2);
					
					String mapping=entry_countpty_cd+"-"+plantSeq;
					
					if(mapping.equals(""))
					{
						sel_entry_mapping+=""+mapping;
					}
					else
					{
						sel_entry_mapping+="@@"+mapping;
					}
				}
				rset1.close();
				stmt1.close();
				
				sel_exit_mapping="";
				queryString2="SELECT ENTITY_CD,PLANT_SEQ,ENTITY "
						+ "FROM FMS_GTA_EXIT_POINT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, agmt_no);
				stmt2.setString(4, agmt_rev_no);
				stmt2.setString(5, agreement_type);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String exit_countpty_cd=rset2.getString(1)==null?"":rset2.getString(1);
					String plantSeq=rset2.getString(2)==null?"":rset2.getString(2);
					String entity=rset2.getString(3)==null?"":rset2.getString(3);
					
					String mapping=entity+"-"+exit_countpty_cd+"-"+plantSeq;
					
					if(mapping.equals(""))
					{
						sel_exit_mapping+=""+mapping;
					}
					else
					{
						sel_exit_mapping+="@@"+mapping;
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
	
	public void getEntryPointList()
	{
		String function_nm="getEntryPointList()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD,SEQ_NO,PLANT_ABBR,PLANT_NAME,STATUS "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND (SELECT COUNT(*) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND ((B.KYC=? AND B.IGX=?) OR (B.KYC=? OR B.IGX=?)) AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))) > 0 ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, comp_cd);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, "Y");
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String plantSeq=rset.getString(2)==null?"":rset.getString(2);
				String plantAbbr=rset.getString(3)==null?"":rset.getString(3);
				String plantNm=rset.getString(4)==null?"":rset.getString(4);
				
				String mapping=countpty_cd+"-"+plantSeq;
				
				VENTRY_COUNTERPTY_CD.add(countpty_cd);
				VENTRY_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VENTRY_PLANT_SEQ.add(plantSeq);
				VENTRY_PLANT_ABBR.add(plantAbbr);
				VENTRY_PLANT_NM.add(plantNm);
				VENTRY_MAPPING.add(mapping);
				VENTRY_STATUS.add(rset.getString(5)==null?"Y":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExitPointList()
	{
		String function_nm="getExitPointList()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD,SEQ_NO,PLANT_ABBR,PLANT_NAME,STATUS "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND (SELECT COUNT(*) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND ((B.KYC=? AND B.IGX=?) OR (B.KYC=? OR B.IGX=?)) AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))) > 0 ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "C");
			stmt.setString(2, comp_cd);
			stmt.setString(3, "Y");
			stmt.setString(4, "Y");
			stmt.setString(5, "Y");
			stmt.setString(6, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String plantSeq=rset.getString(2)==null?"":rset.getString(2);
				String plantAbbr=rset.getString(3)==null?"":rset.getString(3);
				String plantNm=rset.getString(4)==null?"":rset.getString(4);
				String status=rset.getString(5)==null?"Y":rset.getString(5);
				
				String mapping="C-"+countpty_cd+"-"+plantSeq;
				
				VEXIT_COUNTERPTY_CD.add(countpty_cd);
				VEXIT_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VEXIT_PLANT_SEQ.add(plantSeq);
				VEXIT_PLANT_ABBR.add(plantAbbr);
				VEXIT_PLANT_NM.add(plantNm);
				VEXIT_ENTITY.add("C");
				VEXIT_ENTITY_NM.add("Customer");
				VEXIT_MAPPING.add(mapping);
				VEXIT_STATUS.add(status);
			}
			rset.close();
			stmt.close();
			
			queryString1 = "SELECT COUNTERPARTY_CD,SEQ_NO,PLANT_ABBR,PLANT_NAME,STATUS "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND (SELECT COUNT(*) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND ((B.KYC=? AND B.IGX=?) OR (B.KYC=? OR B.IGX=?)) AND B.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))) > 0 ";
			queryString1+= "ORDER BY PLANT_NAME";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, "R");
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, "Y");
			stmt1.setString(4, "Y");
			stmt1.setString(5, "Y");
			stmt1.setString(6, "Y");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
				String plantSeq=rset1.getString(2)==null?"":rset1.getString(2);
				String plantAbbr=rset1.getString(3)==null?"":rset1.getString(3);
				String plantNm=rset1.getString(4)==null?"":rset1.getString(4);
				String status=rset1.getString(5)==null?"Y":rset1.getString(5);
				
				String mapping="R-"+countpty_cd+"-"+plantSeq;
				
				VEXIT_COUNTERPTY_CD.add(countpty_cd);
				VEXIT_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VEXIT_PLANT_SEQ.add(plantSeq);
				VEXIT_PLANT_ABBR.add(plantAbbr);
				VEXIT_PLANT_NM.add(plantNm);
				VEXIT_ENTITY.add("C");
				VEXIT_ENTITY_NM.add("Transporter");
				VEXIT_MAPPING.add(mapping);
				VEXIT_STATUS.add(status);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaAgreementDetailList()
	{
		String function_nm="getGtaAgreementDetailList()";
		try
		{
			int count=0;
			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME,AGMT_TYPE "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND STATUS='Y' "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
						+ "STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,AGMT_NAME,AGMT_TYPE "
						+ "FROM FMS_GTA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND STATUS='Y' "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
				queryString += "AND END_DT >= SYSDATE AND STATUS NOT IN ('Y') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND STATUS IN ('Y') ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, counterparty_cd);
			stmt.setString(++count, agreement_type);
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, counterparty_cd);
				stmt.setString(++count, agreement_type);
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(2)==null?"":rset.getString(2));
				VAGMT_REV_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VSTART_DT.add(rset.getString(4)==null?"Y":rset.getString(4));
				VEND_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VSTATUS.add(rset.getString(6)==null?"":rset.getString(6));
				VTOTAL_QTY.add(nf.format(rset.getDouble(7)));
				VUNIT_CD.add(rset.getString(8)==null?"":rset.getString(8));
				VCALC_BASE.add(rset.getString(9)==null?"GCV":rset.getString(9));
				VAGMT_NAME.add(rset.getString(10)==null?"":rset.getString(10));
				VAGMT_TYPE.add(rset.getString(11)==null?"":rset.getString(11));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContractDetailList()
	{
		String function_nm="getGtaContractDetailList()";
		try
		{
			int cnt=0;
			queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,CT_REF_NO,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,COUNTERPARTY_CD,AGMT_TYPE,CONTRACT_TYPE "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND COUNTERPARTY_CD=? ";
			}
			if(!contract_type.equals("0") && !contract_type.equals(""))
			{
				queryString+="AND CONTRACT_TYPE=? ";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE ";
			}
			queryString+= "ORDER BY END_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, agreement_type);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++cnt, counterparty_cd);
			}
			if(!contract_type.equals("0") && !contract_type.equals(""))
			{
				stmt.setString(++cnt, contract_type);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++cnt, to_dt);
				stmt.setString(++cnt, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_ref=rset.getString(5)==null?"":rset.getString(5);
				String ct_ref=rset.getString(6)==null?"":rset.getString(6);
				String countpty_cd=rset.getString(11)==null?"":rset.getString(11);
				String agmt_type=rset.getString(12)==null?"":rset.getString(12);
				String cont_type=rset.getString(13)==null?"":rset.getString(13);
				//String deal_no=contract_type+""+agmt+"-"+cont;
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				
				
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VAGMT_TYPE.add(agmt_type);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, countpty_cd));
				VDIS_DEAL_NO.add(deal_no);
				VCONT_REF_NO.add(cont_ref);
				VCT_REF_NO.add(ct_ref);
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				
				String entry=rset.getString(9)==null?"":rset.getString(9);
				String exit=rset.getString(10)==null?"":rset.getString(10);
				
				String entryTransCd="";
				String entryTransPlant="";
				
				String exitEntity="";
				String exitTransCd="";
				String exitTransPlant="";
				
				if(!entry.equals(""))
				{
					String[] split = entry.split("-");
					entryTransCd=split[0];
					entryTransPlant=split[1];
				}
				
				if(!exit.equals(""))
				{
					String[] split = exit.split("-");
					exitEntity=split[0];
					exitTransCd=split[1];
					exitTransPlant=split[2];
				}
				
				String entryAbbr=utilBean.getCounterpartyPlantName(conn,entryTransCd, comp_cd, entryTransPlant, "R");
				String exitAbbr=utilBean.getCounterpartyPlantName(conn,exitTransCd, comp_cd, exitTransPlant,exitEntity);
				
				VENTRY_POINT_NAME.add(entryAbbr);
				VEXIT_POINT_NAME.add(exitAbbr);
				
				String sales_cont_nm="";
				queryString1="SELECT CUSTOMER_CD,SELL_CONT_MAP "
						+ "FROM FMS_GTA_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, agmt);
				stmt1.setString(6, agmt_rev);
				stmt1.setString(7, cont_type);
				stmt1.setString(8, agmt_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					String customer_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String sales_cont_map=rset1.getString(2)==null?"":rset1.getString(2);
					
					String contType="";
					String agmtno="";
					String agmt_revno="";
					String contno="";
					String cont_revno="";
					
					String[] split=sales_cont_map.split("-");
					contType=split[0];
					agmtno=split[1];
					agmt_revno=split[2];
					contno=split[3];
					cont_revno=split[4];
					
					cont_ref="";
					queryString2="SELECT CONT_REF_NO,TRADE_REF_NO "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, customer_cd);
					stmt2.setString(3, contType);
					stmt2.setString(4, agmtno);
					stmt2.setString(5, agmt_revno);
					stmt2.setString(6, contno);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						cont_ref=rset2.getString(1)==null?"":rset2.getString(1);
						String trade_ref=rset2.getString(2)==null?"":rset2.getString(2);
						if(contType.equals("X"))
						{
							cont_ref=trade_ref;
						}
					}
					rset2.close();
					stmt2.close();
					//deal_no=utilBean.getDisplayDealMapping(agmtno, agmt_revno, contno, cont_revno, contType);
					deal_no=utilBean.NewDealMappingId(comp_cd, customer_cd, agmtno, agmt_revno, contno, cont_revno, contType, "");
					String countpty_abbr=utilBean.getCounterpartyABBR(conn,customer_cd);
					String countpty_nm=utilBean.getCounterpartyName(conn,customer_cd);
					sales_cont_nm=countpty_abbr+"-"+countpty_nm+"<br><font color='blue'>"+deal_no+"</font> ("+cont_ref+")";
				}
				rset1.close();
				stmt1.close();
				
				VLINKED_SALES_CONT.add(sales_cont_nm);
				
				String selBuPlantNm="";
				queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_GTA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, cont);
				stmt2.setString(4, cont_rev);
				stmt2.setString(5, agmt);
				stmt2.setString(6, agmt_rev);
				stmt2.setString(7, cont_type);
				stmt2.setString(8, agmt_type);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String buCd = rset2.getString(1)==null?"":rset2.getString(1);
					String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
					
					if(selBuPlantNm.equals(""))
					{
						selBuPlantNm+=""+bu_plant_abbr;
					}
					else
					{
						selBuPlantNm+=", "+bu_plant_abbr;
					}
				}
				rset2.close();
				stmt2.close();
				
				VBU_PLANT_NM.add(selBuPlantNm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContractDetailListForNom()
	{
		String function_nm="getGtaContractDetailListForNom()";
		try
		{
			int cnt=0;
			if(contract_type.equals("C"))
			{
				queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_REF_NO,A.CT_REF_NO,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),"
						+ "A.ENTRY_PT_MAPPING_ID,A.EXIT_PT_MAPPING_ID,A.COUNTERPARTY_CD "
						+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_MAP B "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.CUSTOMER_CD=? ";
						if(!active_status.equals("") && active_status.equals("N"))
						{
							queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
						}
						else 
						{
							queryString += "AND END_DT >= SYSDATE ";
						}
						queryString += "ORDER BY END_DT DESC";
			}
			else
			{
				queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,CT_REF_NO,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
						+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,COUNTERPARTY_CD "
						+ "FROM FMS_GTA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						if(!active_status.equals("") && active_status.equals("N"))
						{
							queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
						}
						else 
						{
							queryString += "AND END_DT >= SYSDATE ";
						}
						queryString += "ORDER BY END_DT DESC";
			}
			stmt=conn.prepareStatement(queryString);
			if(contract_type.equals("C"))
			{
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, contract_type);
				stmt.setString(++cnt, customer_cd);
				if(!active_status.equals("") && active_status.equals("N"))
				{
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, from_dt);
				}
			}
			else
			{
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, counterparty_cd);
				stmt.setString(++cnt, contract_type);
				if(!active_status.equals("") && active_status.equals("N"))
				{
					stmt.setString(++cnt, to_dt);
					stmt.setString(++cnt, from_dt);
				}
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_ref=rset.getString(5)==null?"":rset.getString(5);
				String ct_ref=rset.getString(6)==null?"":rset.getString(6);
				String countpty_cd=rset.getString(11)==null?"":rset.getString(11);
				//String deal_no=contract_type+""+agmt+"-"+cont;
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, contract_type, "");
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VDIS_DEAL_NO.add(deal_no);
				VCONT_REF_NO.add(cont_ref);
				VCT_REF_NO.add(ct_ref);
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				
				String entry=rset.getString(9)==null?"":rset.getString(9);
				String exit=rset.getString(10)==null?"":rset.getString(10);
				
				String entryTransCd="";
				String entryTransPlant="";
				
				String exitEntity="";
				String exitTransCd="";
				String exitTransPlant="";
				
				if(!entry.equals(""))
				{
					String[] split = entry.split("-");
					entryTransCd=split[0];
					entryTransPlant=split[1];
				}
				
				if(!exit.equals(""))
				{
					String[] split = exit.split("-");
					exitEntity=split[0];
					exitTransCd=split[1];
					exitTransPlant=split[2];
				}
				
				String entryAbbr=utilBean.getCounterpartyPlantName(conn,entryTransCd, comp_cd, entryTransPlant, "R");
				String exitAbbr=utilBean.getCounterpartyPlantName(conn,exitTransCd, comp_cd, exitTransPlant,exitEntity);
				
				VENTRY_POINT_NAME.add(entryAbbr);
				VEXIT_POINT_NAME.add(exitAbbr);
				
				String sales_cont_nm="";
				queryString1="SELECT CUSTOMER_CD,SELL_CONT_MAP "
						+ "FROM FMS_GTA_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, cont);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, agmt);
				stmt1.setString(6, agmt_rev);
				stmt1.setString(7, contract_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					String customer_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String sales_cont_map=rset1.getString(2)==null?"":rset1.getString(2);
					
					String contType="";
					String agmtno="";
					String agmt_revno="";
					String contno="";
					String cont_revno="";
					
					String[] split=sales_cont_map.split("-");
					contType=split[0];
					agmtno=split[1];
					agmt_revno=split[2];
					contno=split[3];
					cont_revno=split[4];
					
					cont_ref="";
					queryString2="SELECT CONT_REF_NO,TRADE_REF_NO "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, customer_cd);
					stmt2.setString(3, contType);
					stmt2.setString(4, agmtno);
					stmt2.setString(5, agmt_revno);
					stmt2.setString(6, contno);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						cont_ref=rset2.getString(1)==null?"":rset2.getString(1);
						String trade_ref=rset2.getString(2)==null?"":rset2.getString(2);
						if(contType.equals("X"))
						{
							cont_ref=trade_ref;
						}
					}
					rset2.close();
					stmt2.close();
					//deal_no=utilBean.getDisplayDealMapping(agmtno, agmt_revno, contno, cont_revno, contType);
					deal_no=utilBean.NewDealMappingId(comp_cd, customer_cd, agmtno, agmt_revno, contno, cont_revno, contType, "");
					String countpty_abbr=utilBean.getCounterpartyABBR(conn,customer_cd);
					String countpty_nm=utilBean.getCounterpartyName(conn,customer_cd);
					sales_cont_nm=countpty_abbr+"-"+countpty_nm+"<br><font color='blue'>"+deal_no+"</font> ("+cont_ref+")";
				}
				rset1.close();
				stmt1.close();
				
				VLINKED_SALES_CONT.add(sales_cont_nm);
				
				String selBuPlantNm="";
				queryString2="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_GTA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, cont);
				stmt2.setString(4, cont_rev);
				stmt2.setString(5, agmt);
				stmt2.setString(6, agmt_rev);
				stmt2.setString(7, contract_type);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String buCd = rset2.getString(1)==null?"":rset2.getString(1);
					String bu_plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
					
					if(selBuPlantNm.equals(""))
					{
						selBuPlantNm+=""+bu_plant_abbr;
					}
					else
					{
						selBuPlantNm+=", "+bu_plant_abbr;
					}
				}
				rset2.close();
				stmt2.close();
				
				VBU_PLANT_NM.add(selBuPlantNm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContEntryPointList()
	{
		String function_nm="getContEntryPointList()";
		try
		{
			queryString = "SELECT A.COUNTERPARTY_CD,A.SEQ_NO,A.PLANT_NAME "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A,FMS_GTA_ENTRY_POINT C "
					+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.TRANSPORTER_CD AND A.SEQ_NO=C.PLANT_SEQ "
					+ "AND C.COUNTERPARTY_CD=? AND C.AGMT_NO=? AND C.AGMT_REV=? AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, comp_cd);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String entry_countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String plantSeq=rset.getString(2)==null?"":rset.getString(2);
				
				String mapping=entry_countpty_cd+"-"+plantSeq;
				
				VENTRY_MAPPING.add(mapping);
				VENTRY_POINT_NAME.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContExitPointList()
	{
		String function_nm="getContExitPointList()";
		try
		{
			queryString="SELECT ENTITY_CD,PLANT_SEQ,ENTITY "
					+ "FROM FMS_GTA_EXIT_POINT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? ";
			if(callFlag.equals("GTA_CONTRACT"))
			{
				queryString+="AND ENTITY=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, agreement_type);
			if(callFlag.equals("GTA_CONTRACT"))
			{
				stmt.setString(6, contract_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String exit_countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String plantSeq=rset.getString(2)==null?"":rset.getString(2);
				String entity=rset.getString(3)==null?"":rset.getString(3);
				
				String mapping=entity+"-"+exit_countpty_cd+"-"+plantSeq;
				
				String exit_nm="";
				queryString1 = "SELECT PLANT_NAME "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, entity);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, exit_countpty_cd);
				stmt1.setString(4, plantSeq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					exit_nm=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VEXIT_MAPPING.add(mapping);
				VEXIT_POINT_NAME.add(exit_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}		
	}
	
	public void getContBusinessPlantList()
	{
		String function_nm="getContBusinessPlantList()";
		try
		{
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_GTA_AGMT_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_GTA_AGMT_BU B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String buCd = rset.getString(1)==null?"":rset.getString(1);
				String bu_plant_seq = rset.getString(2)==null?"":rset.getString(2);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");
				
				VBU_CD.add(buCd);
				VBU_PLANT_SEQ_NO.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VBU_PLANT_NM.add(bu_plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBusinessPlantList()
	{
		String function_nm="getBusinessPlantList()";
		try
		{
			utilBean.getEffectiveBusinessPlantList(conn,comp_cd);
			VBU_CD=utilBean.getBU_CD();
			VBU_PLANT_NM=utilBean.getBU_PLANT_NM();
			VBU_PLANT_ABBR=utilBean.getBU_PLANT_ABBR();
			VBU_PLANT_SEQ_NO=utilBean.getBU_PLANT_SEQ_NO();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContTransporterBuList()
	{
		String function_nm="getContTransporterBuList()";
		try
		{
			queryString = "SELECT A.COUNTERPARTY_CD,A.PLANT_NAME,A.PLANT_ABBR,A.SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A,FMS_GTA_AGMT_TRANS_BU C "
					+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.SEQ_NO=C.PLANT_SEQ_NO "
					+ "AND C.COUNTERPARTY_CD=? AND C.AGMT_NO=? AND C.AGMT_REV=? AND C.AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, comp_cd);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VTRANS_BU_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTRANS_BU_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VTRANS_BU_SEQ_NO.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTransporterBuList()
	{
		String function_nm="getTransporterBuList()";
		try
		{
			queryString="SELECT COUNTERPARTY_CD,PLANT_NAME,PLANT_ABBR,SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS=? ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, comp_cd);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, "Y");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				//BU_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VTRANS_BU_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTRANS_BU_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VTRANS_BU_SEQ_NO.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerMst()
	{
		String function_nm="getCustomerMst()";
		try
		{
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_BASE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "D");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String customer_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VCUSTOMER_CD.add(customer_cd);
				VCUSTOMER_NM.add(""+utilBean.getCounterpartyName(conn,customer_cd));
				VCUSTOMER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,customer_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSalesContList()
	{
		String function_nm="getSalesContList()";
		try
		{
			queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CONT_REF_NO,A.TRADE_REF_NO,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.TCQ,A.COUNTERPARTY_CD "
					+ ""
					+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_TRANSPTR C,FMS_SUPPLY_CONT_PLANT D,FMS_SUPPLY_CONT_BU E  "
					+ ""
					+ "WHERE A.COMPANY_CD=? AND A.AGMT_BASE=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)"
					+ "" //TRANSPORTER CHECKING
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONT_REV=C.CONT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND C.TRANSPORTER_CD=? AND C.PLANT_SEQ_NO=? "
					+ "" //PLANT CHECKING
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV "
					+ "AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ "AND D.PLANT_SEQ_NO=? "
					+ "" //BU CHECKING
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV "
					+ "AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
					+ "AND E.PLANT_SEQ_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "D");
			stmt.setString(3, customer_cd);
			stmt.setString(4, to_dt);
			stmt.setString(5, from_dt);
			stmt.setString(6, transporter_cd);
			stmt.setString(7, transporter_plant_seq);
			stmt.setString(8, counterparty_plant_seq);
			stmt.setString(9, bu_unit);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				String trade_ref=rset.getString(7)==null?"":rset.getString(7);
				
				String countpty_cd=rset.getString(11)==null?"":rset.getString(11);
				
				if(cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				
				//String deal_no=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, countpty_cd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VDIS_DEAL_NO.add(deal_no);
				VCONT_REF_NO.add(cont_ref);
				VSTART_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VEND_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VTCQ.add(nf.format(rset.getDouble(10)));
				
				String DelvNm="";
			  	queryString1 = "SELECT TRANSPORTER_CD,PLANT_SEQ_NO "
			  			+ "FROM FMS_SUPPLY_CONT_TRANSPTR "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt1=conn.prepareStatement(queryString1);
			  	stmt1.setString(1, comp_cd);
			  	stmt1.setString(2, agmt);
			  	stmt1.setString(3, agmt_rev);
			  	stmt1.setString(4, cont);
			  	stmt1.setString(5, cont_rev);
			  	stmt1.setString(6, customer_cd);
			  	stmt1.setString(7, cont_type);
			  	rset1=stmt1.executeQuery();
	  			while(rset1.next())
	  			{
	  				String trans_cd = rset1.getString(1)==null?"0":rset1.getString(1);
	  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
	  				if(DelvNm.equals(""))
	  				{
	  					DelvNm+=""+utilBean.getCounterpartyPlantABBR(conn,trans_cd, comp_cd, plant_seq, "R");
	  				}
	  				else
	  				{
	  					DelvNm+=", "+utilBean.getCounterpartyPlantABBR(conn,trans_cd, comp_cd, plant_seq, "R");
	  				}
	  			}
	  			rset1.close();
	  			stmt1.close();
	  			
	  			String BuNm="";
	  			queryString2 = "SELECT PLANT_SEQ_NO "
			  			+ "FROM FMS_SUPPLY_CONT_BU "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
	  			stmt2=conn.prepareStatement(queryString2);
			  	stmt2.setString(1, comp_cd);
			  	stmt2.setString(2, agmt);
			  	stmt2.setString(3, agmt_rev);
			  	stmt2.setString(4, cont);
			  	stmt2.setString(5, cont_rev);
			  	stmt2.setString(6, customer_cd);
			  	stmt2.setString(7, cont_type);
			  	rset2=stmt2.executeQuery();
	  			while(rset2.next())
	  			{
	  				String plant_seq = rset2.getString(1)==null?"0":rset2.getString(1);
	  				
	  				if(BuNm.equals(""))
	  				{
	  					BuNm+=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, plant_seq, "B");
	  				}
	  				else
	  				{
	  					BuNm+=", "+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, plant_seq, "B");
	  				}
	  			}
	  			rset2.close();
	  			stmt2.close();
	  			
	  			String CustPlantNm="";
	  			queryString3 = "SELECT PLANT_SEQ_NO "
			  			+ "FROM FMS_SUPPLY_CONT_PLANT "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
	  			stmt3=conn.prepareStatement(queryString3);
			  	stmt3.setString(1, comp_cd);
			  	stmt3.setString(2, agmt);
			  	stmt3.setString(3, agmt_rev);
			  	stmt3.setString(4, cont);
			  	stmt3.setString(5, cont_rev);
			  	stmt3.setString(6, customer_cd);
			  	stmt3.setString(7, cont_type);
			  	rset3=stmt3.executeQuery();
	  			while(rset3.next())
	  			{
	  				String plant_seq = rset3.getString(1)==null?"0":rset3.getString(1);
	  				
	  				if(CustPlantNm.equals(""))
	  				{
	  					CustPlantNm+=""+utilBean.getCounterpartyPlantABBR(conn,customer_cd, comp_cd, plant_seq, "C");
	  				}
	  				else
	  				{
	  					CustPlantNm+=", "+utilBean.getCounterpartyPlantABBR(conn,customer_cd, comp_cd, plant_seq, "C");
	  				}
	  			}
	  			rset3.close();
	  			stmt3.close();
	  			
	  			VDELV_POINT.add(DelvNm);
	  			VBU_POINT.add(BuNm);
	  			VCUST_PLANT_POINT.add(CustPlantNm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCTContList()
	{
		String function_nm="getCTContList()";
		try
		{
			queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CONT_REF_NO,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),MDQ "
					+ ""
					+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_TRANS_BU D,FMS_GTA_CONT_BU E  "
					+ ""
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.CONTRACT_TYPE IN ('C','R') "
					+ "AND A.ENTRY_PT_MAPPING_ID=? AND A.EXIT_PT_MAPPING_ID=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "" //PLANT CHECKING
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=D.AGMT_NO "
					+ "AND A.CONT_NO=D.CONT_NO AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.AGMT_TYPE=D.AGMT_TYPE "
					+ "AND D.PLANT_SEQ_NO=? "
					+ "" //BU CHECKING
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=E.AGMT_NO "
					+ "AND A.CONT_NO=E.CONT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.AGMT_TYPE=E.AGMT_TYPE "
					+ "AND E.PLANT_SEQ_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, customer_cd);
			stmt.setString(3, to_dt);
			stmt.setString(4, from_dt);
			stmt.setString(5, entry_point);
			stmt.setString(6, exit_point);
			stmt.setString(7, trans_bu_unit);
			stmt.setString(8, bu_unit);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				
				String deal_no=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VDIS_DEAL_NO.add(deal_no);
				VCONT_REF_NO.add(cont_ref);
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VTCQ.add(nf.format(rset.getDouble(9)));
				
	  			String BuNm="";
	  			queryString2 = "SELECT PLANT_SEQ_NO "
			  			+ "FROM FMS_GTA_CONT_BU "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
	  			stmt2=conn.prepareStatement(queryString2);
			  	stmt2.setString(1, comp_cd);
			  	stmt2.setString(2, agmt);
			  	stmt2.setString(3, agmt_rev);
			  	stmt2.setString(4, cont);
			  	stmt2.setString(5, cont_rev);
			  	stmt2.setString(6, customer_cd);
			  	stmt2.setString(7, cont_type);
			  	rset2=stmt2.executeQuery();
	  			while(rset2.next())
	  			{
	  				String plant_seq = rset2.getString(1)==null?"0":rset2.getString(1);
	  				
	  				if(BuNm.equals(""))
	  				{
	  					BuNm+=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, plant_seq, "B");
	  				}
	  				else
	  				{
	  					BuNm+=", "+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, plant_seq, "B");
	  				}
	  			}
	  			rset2.close();
	  			stmt2.close();
	  			
	  			String CustPlantNm="";
	  			queryString3 = "SELECT PLANT_SEQ_NO "
			  			+ "FROM FMS_GTA_CONT_TRANS_BU "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
	  			stmt3=conn.prepareStatement(queryString3);
			  	stmt3.setString(1, comp_cd);
			  	stmt3.setString(2, agmt);
			  	stmt3.setString(3, agmt_rev);
			  	stmt3.setString(4, cont);
			  	stmt3.setString(5, cont_rev);
			  	stmt3.setString(6, customer_cd);
			  	stmt3.setString(7, cont_type);
			  	rset3=stmt3.executeQuery();
	  			while(rset3.next())
	  			{
	  				String plant_seq = rset3.getString(1)==null?"0":rset3.getString(1);
	  				
	  				if(CustPlantNm.equals(""))
	  				{
	  					CustPlantNm+=""+utilBean.getCounterpartyBuPlantABBR(conn,customer_cd, comp_cd, plant_seq, "R");
	  				}
	  				else
	  				{
	  					CustPlantNm+=", "+utilBean.getCounterpartyBuPlantABBR(conn,customer_cd, comp_cd, plant_seq, "R");
	  				}
	  			}
	  			rset3.close();
	  			stmt3.close();
	  			
	  			VBU_POINT.add(BuNm);
	  			VCUST_PLANT_POINT.add(CustPlantNm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContractDetail()
	{
		String function_nm="getGtaContractDetail()";
		try
		{
			queryString="SELECT CONT_NAME,CONT_REF_NO,CT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,"
					+ "MDQ,MDQ_UNIT,RATE_UNIT,TRANSPORT_RATE,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
					+ "SIP_PAY_RATE,CALC_BASE,GCV,NCV,SIP_PAY_FREQ,CT_SEQ_NO,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agmt_no);
			stmt.setString(7, agmt_rev_no);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				cont_name =rset.getString(1)==null?"":rset.getString(1);
				cont_ref_no =rset.getString(2)==null?"":rset.getString(2);
				ct_ref_no =rset.getString(3)==null?"":rset.getString(3);
				start_dt =rset.getString(4)==null?"":rset.getString(4);
				end_dt =rset.getString(5)==null?"":rset.getString(5);
				entry_pt_mapping_id =rset.getString(6)==null?"":rset.getString(6);
				exit_pt_mapping_id =rset.getString(7)==null?"":rset.getString(7);
				mdq =rset.getString(8)==null?"":rset.getString(8);
				mdq_unit =rset.getString(9)==null?"":rset.getString(9);
				rate_unit =rset.getString(10)==null?"":rset.getString(10);
				transport_rate =rset.getString(11)==null?"":rset.getString(11);
				positive_imb_rate =rset.getString(12)==null?"":rset.getString(12);
				negetive_imb_rate =rset.getString(13)==null?"":rset.getString(13);
				unauth_overrun_rate =rset.getString(14)==null?"":rset.getString(14);
				sip_pay_rate =rset.getString(15)==null?"":rset.getString(15);
				
				calc_base=rset.getString(16)==null?"":rset.getString(16);
				gcv=rset.getString(17)==null?"9802.80":rset.getString(17);
				ncv=rset.getString(18)==null?"8831.35":rset.getString(18);
				
				sip_pay_freq=rset.getString(19)==null?"D":rset.getString(19);
				ct_seq_no=rset.getString(20)==null?"":rset.getString(20);
				sip_pay_percent=rset.getString(21)==null?"":rset.getString(21);
			}
			else
			{
				gcv="9802.80";
				ncv="8831.35";
				sip_pay_freq="D";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getParkingContractDetail()
	{
		String function_nm="getGtaContractDetail()";
		try
		{
			queryString="SELECT CONT_NAME,CONT_REF_NO,CT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,"
					+ "MDQ,MDQ_UNIT,RATE_UNIT,PARKING_RATE,"
					+ "CALC_BASE,GCV,NCV,MAX_PARK_QTY,MAX_PARK_UNIT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, contract_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				cont_name =rset.getString(1)==null?"":rset.getString(1);
				cont_ref_no =rset.getString(2)==null?"":rset.getString(2);
				ct_ref_no =rset.getString(3)==null?"":rset.getString(3);
				start_dt =rset.getString(4)==null?"":rset.getString(4);
				end_dt =rset.getString(5)==null?"":rset.getString(5);
				entry_pt_mapping_id =rset.getString(6)==null?"":rset.getString(6);
				exit_pt_mapping_id =rset.getString(7)==null?"":rset.getString(7);
				mdq =rset.getString(8)==null?"":rset.getString(8);
				mdq_unit =rset.getString(9)==null?"":rset.getString(9);
				rate_unit =rset.getString(10)==null?"":rset.getString(10);
				parking_rate =rset.getString(11)==null?"":rset.getString(11);
				calc_base=rset.getString(12)==null?"":rset.getString(12);
				gcv=rset.getString(13)==null?"9802.80":rset.getString(13);
				ncv=rset.getString(14)==null?"8831.35":rset.getString(14);
				max_park_qty=rset.getString(15)==null?"":rset.getString(15);
				max_park_qty_unit=rset.getString(16)==null?"":rset.getString(16);
			}
			else
			{
				gcv="9802.80";
				ncv="8831.35";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedGtaAgreementDetail()
	{
		String function_nm="getSelectedGtaAgreementDetail()";
		try
		{
			queryString="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CALC_BASE "
					+ "FROM FMS_GTA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_start_dt=rset.getString(1)==null?"":rset.getString(1);
				agmt_end_dt=rset.getString(2)==null?"":rset.getString(2);
				agmt_calc_base=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getGtaContractSelectedBusinessPlantList()
	{
		String function_nm="getGtaContractSelectedBusinessPlantList()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, agreement_type);
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
	
	public void getGtaContractSelectedTransBuList()
	{
		String function_nm="getGtaContractSelectedTransBuList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_TRANS_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VSEL_TRANS_BU_SEQ_NO.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCountContractBillingDetail()
	{
		String function_nm="getCountContractBillingDetail()";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND AGMT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_billing_dtl+=""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCountGtpaBillingDetail()
	{
		String function_nm="getCountGtpaBillingDetail()";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_billing_dtl+=""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCountNomination()
	{
		String function_nm="getCountNomination()";
		try
		{
			queryString="SELECT COUNT(*) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, contract_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, entry_pt_mapping_id);
			stmt.setString(7, exit_pt_mapping_id);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				nom_count=""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContDtlForParkingContract()
	{
		String function_nm="getGtaContDtlForParkingContract()";
		try
		{
			queryString="SELECT CUSTOMER_CD,SELL_CONT_MAP "
					+ "FROM FMS_GTA_CONT_MAP "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='R'";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				customer_cd=rset.getString(1)==null?"":rset.getString(1);
				sales_cont_map=rset.getString(2)==null?"":rset.getString(2);
				
				String contType="";
				String agmtno="";
				String agmt_revno="";
				String contno="";
				String cont_revno="";
				
				String[] split=sales_cont_map.split("-");
				contType=split[0];
				agmtno=split[1];
				agmt_revno=split[2];
				contno=split[3];
				cont_revno=split[4];
				
				String cont_ref="";
				queryString1="SELECT CONT_REF_NO "
						+ "FROM FMS_GTA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, customer_cd);
				stmt1.setString(3, contType);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, agmt_revno);
				stmt1.setString(6, contno);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				String deal_no=utilBean.getDisplayDealMapping(agmtno, agmt_revno, contno, cont_revno, contType);
				
				sales_cont_nm=deal_no+" ("+cont_ref+")";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSalesContDtlForGtaContract()
	{
		String function_nm="getSalesContDtlForGtaContract()";
		try
		{
			queryString="SELECT CUSTOMER_CD,SELL_CONT_MAP "
					+ "FROM FMS_GTA_CONT_MAP "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND MAPED_ENTITY_TYPE='C'";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				customer_cd=rset.getString(1)==null?"":rset.getString(1);
				sales_cont_map=rset.getString(2)==null?"":rset.getString(2);
				
				String contType="";
				String agmtno="";
				String agmt_revno="";
				String contno="";
				String cont_revno="";
				
				String[] split=sales_cont_map.split("-");
				contType=split[0];
				agmtno=split[1];
				agmt_revno=split[2];
				contno=split[3];
				cont_revno=split[4];
				
				String cont_ref="";
				queryString1="SELECT CONT_REF_NO,TRADE_REF_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, customer_cd);
				stmt1.setString(3, contType);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, agmt_revno);
				stmt1.setString(6, contno);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
					String trade_ref=rset1.getString(2)==null?"":rset1.getString(2);
					if(contType.equals("X"))
					{
						cont_ref=trade_ref;
					}
				}
				rset1.close();
				stmt1.close();
				
				//String deal_no=utilBean.getDisplayDealMapping(agmtno, agmt_revno, contno, cont_revno, contType);
				String deal_no=utilBean.NewDealMappingId(comp_cd, customer_cd, agmtno, agmt_revno, contno, cont_revno, contType,"");
				String customerAbbr=utilBean.getCounterpartyABBR(conn, customer_cd);
				sales_cont_nm=customerAbbr+" "+deal_no+" ("+cont_ref+")";
			}
			rset.close();
			stmt.close();
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
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerList()
	{
		String function_nm="getCustomerList()";
		try
		{
			queryString="SELECT DISTINCT B.CUSTOMER_CD "
					+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VCUSTOMER_CD.add(counterpty_cd);
				VCUSTOMER_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VCUSTOMER_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedGtcContractDetail()
	{
		String function_nm="getSelectedGtcContractDetail()";
		try
		{
			counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			
			queryString="SELECT CONT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "MDQ,MDQ_UNIT,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,CALC_BASE,GCV,NCV,SIP_PAY_PERCENT,SIP_PAY_FREQ,"
					+ "AGMT_REV,CONT_REV "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, contract_type);
			stmt.setString(5, agmt_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_rev_no=rset.getString(13)==null?"":rset.getString(13);
				cont_rev_no=rset.getString(14)==null?"":rset.getString(14);
				
				cont_ref_no=rset.getString(1)==null?"":rset.getString(1);
				start_dt=rset.getString(2)==null?"":rset.getString(2);
				end_dt=rset.getString(3)==null?"":rset.getString(3);
				mdq=nf.format(rset.getDouble(4));
				mdq_unit =rset.getString(5)==null?"":rset.getString(5);
				
				entry_pt_mapping_id=rset.getString(6)==null?"":rset.getString(6);
				exit_pt_mapping_id=rset.getString(7)==null?"":rset.getString(7);
				
				calc_base=rset.getString(8)==null?"":rset.getString(8);
				gcv=rset.getString(9)==null?"9802.80":rset.getString(9);
				ncv=rset.getString(10)==null?"8831.35":rset.getString(10);
				
				sip_pay_percent=rset.getString(11)==null?"":rset.getString(11);
				sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
				
				String entryTransCd="";
				String entryTransPlant="";
				
				String exitEntity="";
				String exitTransCd="";
				String exitTransPlant="";
				
				if(!entry_pt_mapping_id.equals(""))
				{
					String[] split = entry_pt_mapping_id.split("-");
					entryTransCd=split[0];
					entryTransPlant=split[1];
				}
				
				if(!exit_pt_mapping_id.equals(""))
				{
					String[] split = exit_pt_mapping_id.split("-");
					exitEntity=split[0];
					exitTransCd=split[1];
					exitTransPlant=split[2];
				}
				
				entry_pt_nm=utilBean.getCounterpartyPlantName(conn,entryTransCd, comp_cd, entryTransPlant, "R");
				exit_pt_nm=utilBean.getCounterpartyPlantName(conn,exitTransCd, comp_cd, exitTransPlant,exitEntity);
				
				display_dealNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			}
			rset.close();
			stmt.close();
			
			if(contract_type.equals("R"))
			{
				linked_sales_cont_map="0";
				parking_sell_cont_map=contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				
				queryString1="SELECT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE "
						+ "FROM FMS_GTA_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND CUSTOMER_CD=? AND SELL_CONT_MAP LIKE ? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, parking_sell_cont_map);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					String cp=rset1.getString(1)==null?"":rset1.getString(1);
					String an=rset1.getString(2)==null?"":rset1.getString(2);
					String cn=rset1.getString(3)==null?"":rset1.getString(3);
					String ct=rset1.getString(4)==null?"":rset1.getString(4);
					
					parking_cont_map=cp+"-"+ct+"-"+an+"-"+cn;
				}
				rset1.close();
				stmt1.close();
				
				parking_sell_cont_map=counterparty_cd+"-"+parking_sell_cont_map;
			}
			else
			{
				queryString1="SELECT CUSTOMER_CD,SELL_CONT_MAP "
						+ "FROM FMS_GTA_CONT_MAP "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, agmt_no);
				stmt1.setString(7, agmt_rev_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					customer_cd=rset1.getString(1)==null?"":rset1.getString(1);
					sales_cont_map=rset1.getString(2)==null?"":rset1.getString(2);
					
					linked_sales_cont_map=customer_cd+"-"+sales_cont_map;
				}
				else
				{
					linked_sales_cont_map="0";
				}
				rset1.close();
				stmt1.close();
			}
			
			queryString2="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, cont_no);
			stmt2.setString(4, cont_rev_no);
			stmt2.setString(5, contract_type);
			stmt2.setString(6, agmt_no);
			stmt2.setString(7, agmt_rev_no);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				bu_plant_seq=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getForthnightlyDateRang()
	{
		String function_nm="getForthnightlyDateRang()";
		try
		{
			int days=dateUtil.getDays(end_dt, start_dt);
			if(days >= 15)
			{
				String sysdt=dateUtil.getSysdate();
				
				int isActive=0;
				queryString="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND TO_DATE(?,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, start_dt);
				stmt.setString(2, sysdt);
				stmt.setString(3, end_dt);
				stmt.setString(4, sysdt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					isActive=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				int isExpire=0;
				queryString1="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, end_dt);
				stmt1.setString(2, sysdt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					isExpire=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				int isUpcoming=0;
				queryString2="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY') ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, start_dt);
				stmt2.setString(2, sysdt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					isUpcoming=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				
				/*System.out.println("days=="+days);
				System.out.println("isExpire=="+isExpire);
				System.out.println("isUpcoming=="+isUpcoming);
				System.out.println("isActive=="+isActive);
				System.out.println("sysdt=="+sysdt);*/
				
				if(isUpcoming==1)
				{
					sysdt=start_dt;
				}
				else if(isExpire==1)
				{
					sysdt=end_dt;
				}
				
				String month="";
				String year="";
				String day="";
				if(!sysdt.equals(""))
				{
					String[] split=sysdt.split("/");
					day=split[0];
					month=split[1];
					year=split[2];
				}
				
				String period_start_dt="";
				String period_end_dt="";
				
				if(Integer.parseInt(day) > 15)
				{
					period_start_dt="16/"+month+"/"+year;
					period_end_dt=dateUtil.getLastDateOfMonth(month, year);
				}
				else
				{
					period_start_dt="01/"+month+"/"+year;
					period_end_dt="15/"+month+"/"+year;
				}
				
				int after=0;
				queryString3="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY')";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, start_dt);
				stmt3.setString(2, period_start_dt);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					after=rset3.getInt(1);
				}
				rset3.close();
				stmt3.close();
				
				int before=0;
				queryString4="SELECT COUNT(*) FROM DUAL "
						+ "WHERE TO_DATE(?,'DD/MM/YYYY')<TO_DATE(?,'DD/MM/YYYY') ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, end_dt);
				stmt4.setString(2, period_end_dt);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					before=rset4.getInt(1);
				}
				rset4.close();
				stmt4.close();
				
				if(after==1)
				{
					period_start_dt=start_dt;
				}
				if(before==1)
				{
					period_end_dt=end_dt;
				}
				
				forthnighly_start_dt=period_start_dt;
				forthnighly_end_dt=period_end_dt;
			}
			else
			{
				forthnighly_start_dt=start_dt;
				forthnighly_end_dt=end_dt;
			}
			
			if(from_dt.equals("") && to_dt.equals(""))
			{
				from_dt=forthnighly_start_dt;
				to_dt=forthnighly_end_dt;
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtcNominationDetail()
	{
		String function_nm="getGtcNominationDetail()";
		try
		{
			String cont_map="";
			if(!linked_sales_cont_map.equals("") && linked_sales_cont_map.length()>1 && (contract_type.equals("C") || contract_type.equals("K"))) //FOR PARKING, ALSO CONTRCT MAY BE ATTCHED 
			{
				String[] split=linked_sales_cont_map.split("-");
				
				cont_map=split[0]+"-"+split[1]+"-"+split[2]+"-%-"+split[4]+"-%";
			}
			else
			{
				cont_map="0";
			}
			
			double tot_entry_mmbtu=0;
			double tot_exit_mmbtu=0;
			double tot_entry_scm=0;
			double tot_exit_scm=0;
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				String gen_dt=dateUtil.getDate(gas_dt, "-1");
				
				VGAS_DT.add(gas_dt);
				VADJ_IMBALANCE.add("");//APPLICABLE FOR ALLOC ONLY
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, gas_dt).get("status");
				String custStatus = (String) utilBean.getCounterpartyDetails(conn, customer_cd, gas_dt).get("status");
				
				if(cpStatus.equals("N") || custStatus.equals("N")) 
				{
					VIS_ACTIVE.add("N");
				}
				else 
				{
					VIS_ACTIVE.add("Y");
				}

				String entryTransCd="";
				String entryTransPlant="";
				String entryTransCdStatus = "";
						
				String exitEntity="";
				String exitTransCd="";
				String exitTransPlant="";
				String exitTransCdStatus = "";
				
				if(!entry_pt_mapping_id.equals(""))
				{
					String[] split = entry_pt_mapping_id.split("-");
					entryTransCd=split[0];
					entryTransPlant=split[1];
				}
				
				if(!exit_pt_mapping_id.equals(""))
				{
					String[] split = exit_pt_mapping_id.split("-");
					exitEntity=split[0];
					exitTransCd=split[1];
					exitTransPlant=split[2];
				}
				
				entryTransCdStatus = (String) utilBean.getCounterpartyDetails(conn, entryTransCd, gas_dt).get("status");
	  			exitTransCdStatus = (String) utilBean.getCounterpartyDetails(conn, exitTransCd, gas_dt).get("status");
				
	  			VENTRY_TRANSCD_STATUS.add(entryTransCdStatus);
	  			VEXIT_TRANSCD_STATUS.add(exitTransCdStatus);
	  			
				queryString1="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
						+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, cont_map);
				stmt1.setString(10, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VNOM_REV_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
		  			VGEN_TIME.add(rset1.getString(2)==null?"":rset1.getString(2));
		  			VBASE.add(rset1.getString(3)==null?"":rset1.getString(3));
		  			VGCV.add(rset1.getString(4)==null?"9802.80":rset1.getString(4));
		  			VNCV.add(rset1.getString(5)==null?"8831.35":rset1.getString(5));
		  			VQTY_MMBTU.add(nf3.format(rset1.getDouble(6)));
		  			VQTY_SCM.add(nf.format(rset1.getDouble(7)));
		  			VGEN_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
		  			
		  			VEXIT_BASE.add(rset1.getString(9)==null?"":rset1.getString(9));
		  			VEXIT_GCV.add(rset1.getString(10)==null?"9802.80":rset1.getString(10));
		  			VEXIT_NCV.add(rset1.getString(11)==null?"8831.35":rset1.getString(11));
		  			VEXIT_QTY_MMBTU.add(nf3.format(rset1.getDouble(12)));
		  			VEXIT_QTY_SCM.add(nf.format(rset1.getDouble(13)));
		  			
		  			VMDQ.add(nf3.format(rset1.getDouble(14)));
		  			VMDQ_UNIT.add(rset1.getString(15)==null?"1":rset1.getString(15));
		  			VNOM_COLOR.add("#99ffcc");
		  			
		  			VIS_DONE.add("Y");
		  			
		  			tot_entry_mmbtu+=rset1.getDouble(6);
		  			tot_exit_mmbtu+=rset1.getDouble(12);
		  			
		  			tot_entry_scm+=rset1.getDouble(7);
		  			tot_exit_scm+=rset1.getDouble(13);
				}
				else
				{
					String agmt="";
					String cont="";
					String contType="";
					
					if(!sales_cont_map.equals(""))
					{
						String[] split=sales_cont_map.split("-");
						contType=split[0];
						agmt=split[1];
						cont=split[3];
					}
					
					/*String entryTransCd="";
					String entryTransPlant="";
					String entryTransCdStatus = "";
							
					String exitEntity="";
					String exitTransCd="";
					String exitTransPlant="";
					String exitTransCdStatus = "";
					
					if(!entry_pt_mapping_id.equals(""))
					{
						String[] split = entry_pt_mapping_id.split("-");
						entryTransCd=split[0];
						entryTransPlant=split[1];
					}
					
					if(!exit_pt_mapping_id.equals(""))
					{
						String[] split = exit_pt_mapping_id.split("-");
						exitEntity=split[0];
						exitTransCd=split[1];
						exitTransPlant=split[2];
					}*/
					
					queryString2="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
			  				+ "FROM FMS_DAILY_SELLER_NOM A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, cont);
					stmt2.setString(2, agmt);
					stmt2.setString(3, comp_cd);
					stmt2.setString(4, customer_cd);
					stmt2.setString(5, counterparty_cd);
					stmt2.setString(6, entryTransPlant);
					stmt2.setString(7, exitTransPlant);
					stmt2.setString(8, contType);
					stmt2.setString(9, bu_plant_seq);
					stmt2.setString(10, gas_dt);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						VNOM_REV_NO.add("");
			  			VGEN_TIME.add(rset2.getString(2)==null?"":rset2.getString(2));
			  			VBASE.add(rset2.getString(3)==null?"":rset2.getString(3));
			  			VGCV.add(rset2.getString(4)==null?"9802.80":rset2.getString(4));
			  			VNCV.add(rset2.getString(5)==null?"8831.35":rset2.getString(5));
			  			VQTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
			  			VQTY_SCM.add(nf.format(rset2.getDouble(7)));
			  			VGEN_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
			  			
			  			VEXIT_BASE.add(calc_base);
			  			VEXIT_GCV.add(gcv);
			  			VEXIT_NCV.add(ncv);
			  			VEXIT_QTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
			  			VEXIT_QTY_SCM.add(nf.format(rset2.getDouble(7)));
			  			
			  			VMDQ.add(mdq);
			  			VMDQ_UNIT.add(mdq_unit);
			  			VNOM_COLOR.add("");
			  			
			  			VIS_DONE.add("Y");
			  			
			  			tot_entry_mmbtu+=rset2.getDouble(6);
			  			tot_exit_mmbtu+=rset2.getDouble(6);
			  			
			  			tot_entry_scm+=rset2.getDouble(7);
			  			tot_exit_scm+=rset2.getDouble(7);
					}
					else
					{
						VNOM_REV_NO.add("");
			  			VGEN_TIME.add("18:00");
			  						  			
			  			VQTY_MMBTU.add("");
			  			VQTY_SCM.add("");
			  			VGEN_DT.add(gen_dt);
			  			
			  			VEXIT_BASE.add(calc_base);
			  			VEXIT_GCV.add(gcv);
			  			VEXIT_NCV.add(ncv);
			  			VEXIT_QTY_MMBTU.add("");
			  			VEXIT_QTY_SCM.add("");
			  			
			  			VMDQ.add(mdq);
			  			VMDQ_UNIT.add(mdq_unit);
			  			VNOM_COLOR.add("");
			  			
			  			if(contract_type.equals("C"))
			  			{
			  				VBASE.add("GCV");
				  			VGCV.add("9802.80");
				  			VNCV.add("8831.35");
				  			
			  				VIS_DONE.add("N");
			  			}
			  			else
			  			{
			  				VBASE.add(calc_base);
				  			VGCV.add(gcv);
				  			VNCV.add(ncv);
				  			
			  				VIS_DONE.add("Y");
			  			}
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			total_entry_qty_mmbtu=nf3.format(tot_entry_mmbtu);
			total_entry_qty_scm=nf.format(tot_entry_scm);
			
			total_exit_qty_mmbtu=nf3.format(tot_exit_mmbtu);
			total_exit_qty_scm=nf.format(tot_exit_scm);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtcSchedulingDetail()
	{
		String function_nm="getGtcSchedulingDetail()";
		try
		{
			String cont_map="";
			if(!linked_sales_cont_map.equals("") && linked_sales_cont_map.length()>1 && (contract_type.equals("C") || contract_type.equals("K"))) //FOR PARKING, ALSO CONTRCT MAY BE ATTCHED 
			{
				String[] split=linked_sales_cont_map.split("-");
				
				cont_map=split[0]+"-"+split[1]+"-"+split[2]+"-%-"+split[4]+"-%";
			}
			else
			{
				cont_map="0";
			}
			
			double tot_entry_mmbtu=0;
			double tot_exit_mmbtu=0;
			double tot_entry_scm=0;
			double tot_exit_scm=0;
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				String gen_dt=dateUtil.getDate(gas_dt, "-1");
				
				VGAS_DT.add(gas_dt);
				VADJ_IMBALANCE.add("");//APPLICABLE FOR ALLOC ONLY
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, gas_dt).get("status");
				String custStatus = (String) utilBean.getCounterpartyDetails(conn, customer_cd, gas_dt).get("status");
				
				if(cpStatus.equals("N") || custStatus.equals("N")) 
				{
					VIS_ACTIVE.add("N");
				}
				else 
				{
					VIS_ACTIVE.add("Y");
				}
				
				queryString1="SELECT SCH_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
						+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, cont_map);
				stmt1.setString(10, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VNOM_REV_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
		  			VGEN_TIME.add(rset1.getString(2)==null?"":rset1.getString(2));
		  			VBASE.add(rset1.getString(3)==null?"":rset1.getString(3));
		  			VGCV.add(rset1.getString(4)==null?"9802.80":rset1.getString(4));
		  			VNCV.add(rset1.getString(5)==null?"8831.35":rset1.getString(5));
		  			VQTY_MMBTU.add(nf3.format(rset1.getDouble(6)));
		  			VQTY_SCM.add(nf.format(rset1.getDouble(7)));
		  			VGEN_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
		  			
		  			VEXIT_BASE.add(rset1.getString(9)==null?"":rset1.getString(9));
		  			VEXIT_GCV.add(rset1.getString(10)==null?"9802.80":rset1.getString(10));
		  			VEXIT_NCV.add(rset1.getString(11)==null?"8831.35":rset1.getString(11));
		  			VEXIT_QTY_MMBTU.add(nf3.format(rset1.getDouble(12)));
		  			VEXIT_QTY_SCM.add(nf.format(rset1.getDouble(13)));
		  			
		  			//VMDQ.add(nf3.format(rset1.getDouble(14)));
		  			//VMDQ_UNIT.add(rset1.getString(15)==null?"1":rset1.getString(15));
		  			VNOM_COLOR.add("#99ffcc");
		  			
		  			VIS_DONE.add("Y");
		  			
		  			tot_entry_mmbtu+=rset1.getDouble(6);
		  			tot_exit_mmbtu+=rset1.getDouble(12);
		  			
		  			tot_entry_scm+=rset1.getDouble(7);
		  			tot_exit_scm+=rset1.getDouble(13);
				}
				else
				{
					queryString2="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
							+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT "
			  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
			  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
							+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
							+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterparty_cd);
					stmt2.setString(3, contract_type);
					stmt2.setString(4, gas_dt);
					stmt2.setString(5, agmt_no);
					stmt2.setString(6, cont_no);
					stmt2.setString(7, entry_pt_mapping_id);
					stmt2.setString(8, exit_pt_mapping_id);
					stmt2.setString(9, cont_map);
					stmt2.setString(10, bu_plant_seq);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						VNOM_REV_NO.add("");
			  			VGEN_TIME.add(rset2.getString(2)==null?"":rset2.getString(2));
			  			VBASE.add(rset2.getString(3)==null?"":rset2.getString(3));
			  			VGCV.add(rset2.getString(4)==null?"9802.80":rset2.getString(4));
			  			VNCV.add(rset2.getString(5)==null?"8831.35":rset2.getString(5));
			  			VQTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
			  			VQTY_SCM.add(nf.format(rset2.getDouble(7)));
			  			VGEN_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
			  			
			  			VEXIT_BASE.add(rset2.getString(9)==null?"":rset2.getString(9));
			  			VEXIT_GCV.add(rset2.getString(10)==null?"9802.80":rset2.getString(10));
			  			VEXIT_NCV.add(rset2.getString(11)==null?"8831.35":rset2.getString(11));
			  			VEXIT_QTY_MMBTU.add(nf3.format(rset2.getDouble(12)));
			  			VEXIT_QTY_SCM.add(nf.format(rset2.getDouble(13)));
			  			
			  			//VMDQ.add(nf3.format(rset2.getDouble(14)));
			  			//VMDQ_UNIT.add(rset2.getString(15)==null?"1":rset2.getString(15));
			  			VNOM_COLOR.add("");
			  			
			  			VIS_DONE.add("Y");
			  			
			  			tot_entry_mmbtu+=rset2.getDouble(6);
			  			tot_exit_mmbtu+=rset2.getDouble(12);
			  			
			  			tot_entry_scm+=rset2.getDouble(7);
			  			tot_exit_scm+=rset2.getDouble(13);
					}
					else
					{
						VNOM_REV_NO.add("");
			  			VGEN_TIME.add("18:00");
			  			VBASE.add("GCV");
			  			VGCV.add("9802.80");
			  			VNCV.add("8831.35");
			  			VQTY_MMBTU.add("");
			  			VQTY_SCM.add("");
			  			VGEN_DT.add(gen_dt);
			  			
			  			VEXIT_BASE.add(calc_base);
			  			VEXIT_GCV.add(gcv);
			  			VEXIT_NCV.add(ncv);
			  			VEXIT_QTY_MMBTU.add("");
			  			VEXIT_QTY_SCM.add("");
			  			
			  			//VMDQ.add(mdq);
			  			//VMDQ_UNIT.add(mdq_unit);
			  			VNOM_COLOR.add("");
			  			
			  			VIS_DONE.add("N");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, contract_type);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, cont_no);
				stmt2.setString(7, entry_pt_mapping_id);
				stmt2.setString(8, exit_pt_mapping_id);
				stmt2.setString(9, cont_map);
				stmt2.setString(10, bu_plant_seq);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					VMDQ.add(nf3.format(rset2.getDouble(1)));
		  			VMDQ_UNIT.add(rset2.getString(2)==null?"1":rset2.getString(2));
				}
				else
				{
					VMDQ.add(mdq);
		  			VMDQ_UNIT.add(mdq_unit);
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			
			total_entry_qty_mmbtu=nf3.format(tot_entry_mmbtu);
			total_entry_qty_scm=nf.format(tot_entry_scm);
			
			total_exit_qty_mmbtu=nf3.format(tot_exit_mmbtu);
			total_exit_qty_scm=nf.format(tot_exit_scm);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtcAllocationDetail()
	{
		String function_nm="getGtcAllocationDetail()";
		try
		{
			String cont_map="";
			if(!linked_sales_cont_map.equals("") && linked_sales_cont_map.length()>1 && (contract_type.equals("C") || contract_type.equals("K"))) //FOR PARKING, ALSO CONTRCT MAY BE ATTCHED 
			{
				String[] split=linked_sales_cont_map.split("-");
				
				cont_map=split[0]+"-"+split[1]+"-"+split[2]+"-%-"+split[4]+"-%";
			}
			else
			{
				cont_map="0";
			}
			
			double tot_entry_mmbtu=0;
			double tot_exit_mmbtu=0;
			double tot_entry_scm=0;
			double tot_exit_scm=0;
			double tot_adj_imbalance=0;
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				String gen_dt=dateUtil.getDate(gas_dt, "-1");
				
				VGAS_DT.add(gas_dt);
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, gas_dt).get("status");
				String custStatus = (String) utilBean.getCounterpartyDetails(conn, customer_cd, gas_dt).get("status");
				
				if(cpStatus.equals("N") || custStatus.equals("N")) 
				{
					VIS_ACTIVE.add("N");
				}
				else
				{
					VIS_ACTIVE.add("Y");
				}
				
				queryString1="SELECT ALLOC_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
						+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT,ADJ_IMBALANCE "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, cont_map);
				stmt1.setString(10, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VNOM_REV_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
		  			VGEN_TIME.add(rset1.getString(2)==null?"":rset1.getString(2));
		  			VBASE.add(rset1.getString(3)==null?"":rset1.getString(3));
		  			VGCV.add(rset1.getString(4)==null?"9802.80":rset1.getString(4));
		  			VNCV.add(rset1.getString(5)==null?"8831.35":rset1.getString(5));
		  			VQTY_MMBTU.add(nf3.format(rset1.getDouble(6)));
		  			VQTY_SCM.add(nf.format(rset1.getDouble(7)));
		  			VGEN_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
		  			
		  			VEXIT_BASE.add(rset1.getString(9)==null?"":rset1.getString(9));
		  			VEXIT_GCV.add(rset1.getString(10)==null?"9802.80":rset1.getString(10));
		  			VEXIT_NCV.add(rset1.getString(11)==null?"8831.35":rset1.getString(11));
		  			VEXIT_QTY_MMBTU.add(nf3.format(rset1.getDouble(12)));
		  			VEXIT_QTY_SCM.add(nf.format(rset1.getDouble(13)));
		  			VADJ_IMBALANCE.add(rset1.getString(16)==null?"":nf.format(rset1.getDouble(16)));
		  			
		  			//VMDQ.add(nf3.format(rset1.getDouble(14)));
		  			//VMDQ_UNIT.add(rset1.getString(15)==null?"1":rset1.getString(15));
		  			VNOM_COLOR.add("#99ffcc");
		  			
		  			VIS_DONE.add("Y");
		  			
		  			tot_entry_mmbtu+=rset1.getDouble(6);
		  			tot_exit_mmbtu+=rset1.getDouble(12);
		  			
		  			tot_entry_scm+=rset1.getDouble(7);
		  			tot_exit_scm+=rset1.getDouble(13);
		  			tot_adj_imbalance+=rset1.getDouble(16);
				}
				else
				{
					VADJ_IMBALANCE.add("");
					
					if(contract_type.equals("C") || contract_type.equals("R"))
					{
						if(contract_type.equals("C"))
						{
							String agmt="";
							String cont="";
							String contType="";
							
							if(!sales_cont_map.equals(""))
							{
								String[] split=sales_cont_map.split("-");
								contType=split[0];
								agmt=split[1];
								cont=split[3];
							}
							
							String entryTransCd="";
							String entryTransPlant="";
							
							String exitEntity="";
							String exitTransCd="";
							String exitTransPlant="";
							
							if(!entry_pt_mapping_id.equals(""))
							{
								String[] split = entry_pt_mapping_id.split("-");
								entryTransCd=split[0];
								entryTransPlant=split[1];
							}
							
							if(!exit_pt_mapping_id.equals(""))
							{
								String[] split = exit_pt_mapping_id.split("-");
								exitEntity=split[0];
								exitTransCd=split[1];
								exitTransPlant=split[2];
							}
							
							queryString2="SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND TRANSPORTER_CD=? AND TRANS_SEQ=? "
									+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, cont);
							stmt2.setString(2, agmt);
							stmt2.setString(3, comp_cd);
							stmt2.setString(4, customer_cd);
							stmt2.setString(5, counterparty_cd);
							stmt2.setString(6, entryTransPlant);
							stmt2.setString(7, exitTransPlant);
							stmt2.setString(8, contType);
							stmt2.setString(9, bu_plant_seq);
							stmt2.setString(10, gas_dt);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								VNOM_REV_NO.add("");
					  			VGEN_TIME.add(rset2.getString(2)==null?"":rset2.getString(2));
					  			VBASE.add(rset2.getString(3)==null?"":rset2.getString(3));
					  			VGCV.add(rset2.getString(4)==null?"9802.80":rset2.getString(4));
					  			VNCV.add(rset2.getString(5)==null?"8831.35":rset2.getString(5));
					  			VQTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
					  			VQTY_SCM.add(nf.format(rset2.getDouble(7)));
					  			VGEN_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
					  			
					  			VNOM_COLOR.add("");
					  			
					  			tot_entry_mmbtu+=rset2.getDouble(6);
					  			tot_entry_scm+=rset2.getDouble(7);
					  		}
							else
							{
								VNOM_REV_NO.add("");
					  			VGEN_TIME.add("18:00");
					  			VBASE.add("GCV");
					  			VGCV.add("9802.80");
					  			VNCV.add("8831.35");
					  			VQTY_MMBTU.add("");
					  			VQTY_SCM.add("");
					  			VGEN_DT.add(gen_dt);
					  			
					  			VNOM_COLOR.add("");
							}
							rset2.close();
							stmt2.close();
						}
						else
						{
							String contpty_cd="";
							String agmt="";
							String cont="";
							String contType="";
							if(!parking_cont_map.equals(""))
							{
								String[] split=parking_cont_map.split("-");
								contpty_cd=split[0];
								contType=split[1];
								agmt=split[2];
								cont=split[3];
							}
							
							queryString2="SELECT ALLOC_REV_NO,GEN_TIME,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY') "
					  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
					  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
									+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
									+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
									+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, contpty_cd);
							stmt2.setString(3, contType);
							stmt2.setString(4, gas_dt);
							stmt2.setString(5, agmt);
							stmt2.setString(6, cont);
							stmt2.setString(7, entry_pt_mapping_id);
							stmt2.setString(8, exit_pt_mapping_id);
							stmt2.setString(9, parking_sell_cont_map);
							stmt2.setString(10, bu_plant_seq);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								VNOM_REV_NO.add("");
					  			VGEN_TIME.add(rset2.getString(2)==null?"":rset2.getString(2));
					  			VBASE.add(rset2.getString(3)==null?"":rset2.getString(3));
					  			VGCV.add(rset2.getString(4)==null?"9802.80":rset2.getString(4));
					  			VNCV.add(rset2.getString(5)==null?"8831.35":rset2.getString(5));
					  			VQTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
					  			VQTY_SCM.add(nf.format(rset2.getDouble(7)));
					  			VGEN_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
					  			
					  			VNOM_COLOR.add("");
					  			
					  			tot_entry_mmbtu+=rset2.getDouble(6);
					  			tot_entry_scm+=rset2.getDouble(7);
							}
							else
							{
								VNOM_REV_NO.add("");
					  			VGEN_TIME.add("18:00");
					  			VBASE.add("GCV");
					  			VGCV.add("9802.80");
					  			VNCV.add("8831.35");
					  			VQTY_MMBTU.add("");
					  			VQTY_SCM.add("");
					  			VGEN_DT.add(gen_dt);
					  			
					  			VNOM_COLOR.add("");
							}
							rset2.close();
							stmt2.close();
						}
						
						queryString3="SELECT EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT "
				  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
				  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
								+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
								+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
								+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, contract_type);
						stmt3.setString(4, gas_dt);
						stmt3.setString(5, agmt_no);
						stmt3.setString(6, cont_no);
						stmt3.setString(7, entry_pt_mapping_id);
						stmt3.setString(8, exit_pt_mapping_id);
						stmt3.setString(9, cont_map);
						stmt3.setString(10, bu_plant_seq);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							VEXIT_BASE.add(rset3.getString(1)==null?"":rset3.getString(1));
							VEXIT_GCV.add(rset3.getString(2)==null?"9802.80":rset3.getString(2));
				  			VEXIT_NCV.add(rset3.getString(3)==null?"8831.35":rset3.getString(3));
				  			VEXIT_QTY_MMBTU.add(nf3.format(rset3.getDouble(4)));
				  			VEXIT_QTY_SCM.add(nf.format(rset3.getDouble(5)));
				  			
				  			//VMDQ.add(nf3.format(rset3.getDouble(6)));
				  			//VMDQ_UNIT.add(rset3.getString(7)==null?"1":rset3.getString(7));
				  			
				  			VIS_DONE.add("Y");
				  			
				  			tot_exit_mmbtu+=rset3.getDouble(4);
				  			tot_exit_scm+=rset3.getDouble(5);
						}
						else
						{
							VEXIT_BASE.add(calc_base);
				  			VEXIT_GCV.add(gcv);
				  			VEXIT_NCV.add(ncv);
				  			VEXIT_QTY_MMBTU.add("");
				  			VEXIT_QTY_SCM.add("");
				  			
				  			//VMDQ.add(mdq);
				  			//VMDQ_UNIT.add(mdq_unit);
				  			
				  			VIS_DONE.add("N");
						}
						rset3.close();
						stmt3.close();
					}
					else
					{
						queryString2="SELECT SCH_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
								+ "EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,MDQ,MDQ_UNIT "
				  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
				  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
								+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
								+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
								+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, contract_type);
						stmt2.setString(4, gas_dt);
						stmt2.setString(5, agmt_no);
						stmt2.setString(6, cont_no);
						stmt2.setString(7, entry_pt_mapping_id);
						stmt2.setString(8, exit_pt_mapping_id);
						stmt2.setString(9, cont_map);
						stmt2.setString(10, bu_plant_seq);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							VNOM_REV_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
				  			VGEN_TIME.add(rset2.getString(2)==null?"":rset2.getString(2));
				  			VBASE.add(rset2.getString(3)==null?"":rset2.getString(3));
				  			VGCV.add(rset2.getString(4)==null?"9802.80":rset2.getString(4));
				  			VNCV.add(rset2.getString(5)==null?"8831.35":rset2.getString(5));
				  			VQTY_MMBTU.add(nf3.format(rset2.getDouble(6)));
				  			VQTY_SCM.add(nf.format(rset2.getDouble(7)));
				  			VGEN_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
				  			
				  			VEXIT_BASE.add(rset2.getString(9)==null?"":rset2.getString(9));
				  			VEXIT_GCV.add(rset2.getString(10)==null?"9802.80":rset2.getString(10));
				  			VEXIT_NCV.add(rset2.getString(11)==null?"8831.35":rset2.getString(11));
				  			VEXIT_QTY_MMBTU.add(nf3.format(rset2.getDouble(12)));
				  			VEXIT_QTY_SCM.add(nf.format(rset2.getDouble(13)));
				  			
				  			//VMDQ.add(nf3.format(rset2.getDouble(14)));
				  			//VMDQ_UNIT.add(rset2.getString(15)==null?"1":rset2.getString(15));
				  			VNOM_COLOR.add("");
				  			
				  			VIS_DONE.add("Y");
				  			
				  			tot_entry_mmbtu+=rset2.getDouble(6);
				  			tot_exit_mmbtu+=rset2.getDouble(12);
				  			
				  			tot_entry_scm+=rset2.getDouble(7);
				  			tot_exit_scm+=rset2.getDouble(13);
						}
						else
						{
							VNOM_REV_NO.add("");
				  			VGEN_TIME.add("18:00");
				  			VBASE.add("GCV");
				  			VGCV.add("9802.80");
				  			VNCV.add("8831.35");
				  			VQTY_MMBTU.add("");
				  			VQTY_SCM.add("");
				  			VGEN_DT.add(gen_dt);
				  			
				  			VEXIT_BASE.add(calc_base);
				  			VEXIT_GCV.add(gcv);
				  			VEXIT_NCV.add(ncv);
				  			VEXIT_QTY_MMBTU.add("");
				  			VEXIT_QTY_SCM.add("");
				  			
				  			//VMDQ.add(mdq);
				  			//VMDQ_UNIT.add(mdq_unit);
				  			VNOM_COLOR.add("");
				  			
				  			VIS_DONE.add("N");
						}
						rset2.close();
						stmt2.close();
					}
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, contract_type);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, cont_no);
				stmt2.setString(7, entry_pt_mapping_id);
				stmt2.setString(8, exit_pt_mapping_id);
				stmt2.setString(9, cont_map);
				stmt2.setString(10, bu_plant_seq);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					VMDQ.add(nf3.format(rset2.getDouble(1)));
		  			VMDQ_UNIT.add(rset2.getString(2)==null?"1":rset2.getString(2));
				}
				else
				{
					VMDQ.add(mdq);
		  			VMDQ_UNIT.add(mdq_unit);
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			
			total_entry_qty_mmbtu=nf3.format(tot_entry_mmbtu);
			total_entry_qty_scm=nf.format(tot_entry_scm);
			
			total_exit_qty_mmbtu=nf3.format(tot_exit_mmbtu);
			total_exit_qty_scm=nf.format(tot_exit_scm);
			
			total_adj_imbalance=nf.format(tot_adj_imbalance);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtcImbalanceReport()
	{
		String function_nm="getGtcImbalanceReport()";
		try
		{
			double cumulative_imbalance=0;
			double ship_or_pay_percentage=0;
			if(!sip_pay_percent.equals(""))
			{
				ship_or_pay_percentage=Double.parseDouble(sip_pay_percent);
			}
			
			if(mdq.equals("")) {
				mdq="0";
			}
			
			int count_day=dateUtil.getDays(from_dt, start_dt);
			
			if(count_day > 1)
			{
				queryString="SELECT SUM(((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0))) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, start_dt);
				stmt.setString(5, from_dt);
				stmt.setString(6, agmt_no);
				stmt.setString(7, cont_no);
				stmt.setString(8, entry_pt_mapping_id);
				stmt.setString(9, exit_pt_mapping_id);
				stmt.setString(10, bu_plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					cumulative_imbalance=rset.getDouble(1);
				}
				rset.close();
				stmt.close();
			}
			
			double tot_nom_entry_qty=0;
			double tot_nom_exit_qty=0;
			double tot_sch_entry_qty=0;
			double tot_sch_exit_qty=0;
			double tot_alloc_entry_qty=0;
			double tot_alloc_exit_qty=0;
			double tot_adj_imbalance=0;
			double tot_var_mdq=0;
			
			double tot_transmission_qty=0;
			double tot_chargeable_overrun=0;
			double tot_positive_imbalance=0;
			double tot_negitive_imbalance=0;
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				VGAS_DT.add(gas_dt);
				
				double nom_entry_qty=0;
				double nom_exit_qty=0;
				double sch_entry_qty=0;
				double sch_exit_qty=0;
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				double adj_imbalance=0;
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					nom_entry_qty=rset1.getDouble(1);
					nom_exit_qty=rset1.getDouble(2);
					var_mdq=rset1.getDouble(3);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, contract_type);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, cont_no);
				stmt2.setString(7, entry_pt_mapping_id);
				stmt2.setString(8, exit_pt_mapping_id);
				stmt2.setString(9, bu_plant_seq);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sch_entry_qty=rset2.getDouble(1);
					sch_exit_qty=rset2.getDouble(2);
				}
				rset2.close();
				stmt2.close();
				
				queryString3="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,ADJ_IMBALANCE "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, contract_type);
				stmt3.setString(4, gas_dt);
				stmt3.setString(5, agmt_no);
				stmt3.setString(6, cont_no);
				stmt3.setString(7, entry_pt_mapping_id);
				stmt3.setString(8, exit_pt_mapping_id);
				stmt3.setString(9, bu_plant_seq);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					alloc_entry_qty=rset3.getDouble(1);
					alloc_exit_qty=rset3.getDouble(2);
					adj_imbalance=rset3.getDouble(3);
				}
				rset3.close();
				stmt3.close();
				
				//TRANSMISSION MMBTU CALCULATION
				double transmission_qty=0;
				
				if(sip_pay_freq.equals("D")) //SHIP OR PAY = DAILY
				{
					double var_mdq_with_ship_or_pay= var_mdq * (ship_or_pay_percentage / 100);
					
					if(alloc_exit_qty >= var_mdq_with_ship_or_pay)
					{
						transmission_qty=alloc_exit_qty;
					}
					else
					{
						transmission_qty=var_mdq_with_ship_or_pay;
					}
				}
				else //SHIP OR PAY = MONTHLY
				{
					transmission_qty=alloc_exit_qty;
				}
				
				
				//DAILY IMBALANCE CALCULATION
				double daily_imbalance=alloc_entry_qty-alloc_exit_qty;
				
				//CUMULATIVE IMBALANCE CALCULATION
				cumulative_imbalance+=(daily_imbalance + adj_imbalance);
				
				/*double unauthorized_overrun=alloc_exit_qty-var_mdq;
				if(unauthorized_overrun<=0)
				{
					unauthorized_overrun=0;
				}*/
				
				//UNAUTHORIZED OVERRUN CALCULATION
				double unauthorized_overrun=0;
				if(sch_exit_qty > var_mdq)
				{
					if(alloc_exit_qty > sch_exit_qty)
					{
						unauthorized_overrun=alloc_exit_qty-sch_exit_qty;
					}
				}
				else
				{
					if(alloc_exit_qty > var_mdq)
					{
						unauthorized_overrun=alloc_exit_qty-var_mdq;
					}
				}
				
				//CHARGEABLE OVERRUN CALCULATION
				double chargeable_overrun = 0;//unauthorized_overrun - (var_mdq * 0.1);
				/*
				 * if(chargeable_overrun<=0) { chargeable_overrun=0; }
				 */
				
				if(sch_exit_qty > var_mdq)
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(1.10 *sch_exit_qty);
					}
				}
				else
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(var_mdq + (0.1 * sch_exit_qty));
					}
					else if(alloc_exit_qty > 1.10 * var_mdq)
					{
						chargeable_overrun=alloc_exit_qty-(1.10 * var_mdq);
					}
				}
				
				if(chargeable_overrun<=0) 
				{ 
					chargeable_overrun=0; 
				}
				
				//POSITIVE IMBALANCE CALCULATION
				double positive_imbalance=cumulative_imbalance - (var_mdq * 0.1);
				if(positive_imbalance<=0)
				{
					positive_imbalance=0;
				}
				
				//NEGITIVE IMBALANCE CALCULATION
				double negitive_imbalance=cumulative_imbalance + (var_mdq * 0.05);
				if(negitive_imbalance>0)
				{
					negitive_imbalance=0;
				}
				
				VMDQ.add(nf3.format(var_mdq));
				VNOM_ENTRY_QTY.add(nf3.format(nom_entry_qty));
				VNOM_EXIT_QTY.add(nf3.format(nom_exit_qty));
				VSCH_ENTRY_QTY.add(nf3.format(sch_entry_qty));
				VSCH_EXIT_QTY.add(nf3.format(sch_exit_qty));
				VALLOC_ENTRY_QTY.add(nf3.format(alloc_entry_qty));
				VALLOC_EXIT_QTY.add(nf3.format(alloc_exit_qty));
				VADJ_IMBALANCE.add(nf3.format(adj_imbalance));
				
				VTRANSMISSION_QTY.add(nf3.format(transmission_qty));
				VDAILY_IMBALANCE.add(nf3.format(daily_imbalance));
				VCUMULATIVE_IMBALANCE.add(nf3.format(cumulative_imbalance));
				VDAILY_UNAUTHORIZED_OVERRUN.add(nf3.format(unauthorized_overrun));
				VCHARGEABLE_OVERRUN.add(nf3.format(chargeable_overrun));
				VCHARGEABLE_POSITIVE_IMBALANCE.add(nf3.format(positive_imbalance));
				VCHARGEABLE_NEGETIVE_IMBALANCE.add(nf3.format(negitive_imbalance));
				
				tot_var_mdq+=var_mdq;
				tot_nom_entry_qty+=nom_entry_qty;
				tot_nom_exit_qty+=nom_exit_qty;
				tot_sch_entry_qty+=sch_entry_qty;
				tot_sch_exit_qty+=sch_exit_qty;
				tot_alloc_entry_qty+=alloc_entry_qty;
				tot_alloc_exit_qty+=alloc_exit_qty;
				tot_adj_imbalance+=adj_imbalance;
				tot_transmission_qty+=transmission_qty;
				tot_chargeable_overrun+=chargeable_overrun;
				tot_positive_imbalance+=positive_imbalance;
				tot_negitive_imbalance+=negitive_imbalance;
			}
			rset.close();
			stmt.close();
			
			total_var_mdq=nf3.format(tot_var_mdq);
			total_nom_entry_qty=nf3.format(tot_nom_entry_qty);
			total_nom_exit_qty=nf3.format(tot_nom_exit_qty);
			total_sch_entry_qty=nf3.format(tot_sch_entry_qty);
			total_sch_exit_qty=nf3.format(tot_sch_exit_qty);
			total_alloc_entry_qty=nf3.format(tot_alloc_entry_qty);
			total_alloc_exit_qty=nf3.format(tot_alloc_exit_qty);
			total_adj_imbalance=nf3.format(tot_adj_imbalance);
			total_transmission_qty=nf3.format(tot_transmission_qty);
			total_chargeable_overrun=nf3.format(tot_chargeable_overrun);
			total_positive_imbalance=nf3.format(tot_positive_imbalance);
			total_negitive_imbalance=nf3.format(tot_negitive_imbalance);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getParkingImbalanceReport()
	{
		String function_nm="getParkingImbalanceReport()";
		try
		{
			double cumulative_imbalance=0;
			double tot_cumulative_imbalance=0;
			double tot_derived_deparking=0;
			double tot_nom_entry_qty=0;
			double tot_nom_exit_qty=0;
			double tot_sch_entry_qty=0;
			double tot_sch_exit_qty=0;
			double tot_alloc_entry_qty=0;
			double tot_alloc_exit_qty=0;
			double tot_adj_imbalance=0;
			
			queryString1="SELECT SUM(((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0))) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
					+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, contract_type);
			stmt1.setString(4, from_dt);
			stmt1.setString(5, agmt_no);
			stmt1.setString(6, cont_no);
			stmt1.setString(7, entry_pt_mapping_id);
			stmt1.setString(8, exit_pt_mapping_id);
			stmt1.setString(9, bu_plant_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				cumulative_imbalance=rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				VGAS_DT.add(gas_dt);
				
				double nom_entry_qty=0;
				double nom_exit_qty=0;
				double sch_entry_qty=0;
				double sch_exit_qty=0;
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double derived_deparking=0;
				double adj_imbalance=0;
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					nom_entry_qty=rset1.getDouble(1);
					nom_exit_qty=rset1.getDouble(2);
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, contract_type);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, cont_no);
				stmt2.setString(7, entry_pt_mapping_id);
				stmt2.setString(8, exit_pt_mapping_id);
				stmt2.setString(9, bu_plant_seq);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sch_entry_qty=rset2.getDouble(1);
					sch_exit_qty=rset2.getDouble(2);
				}
				rset2.close();
				stmt2.close();
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,ADJ_IMBALANCE "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND ENTRY_PT_MAPPING_ID=? AND EXIT_PT_MAPPING_ID=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, entry_pt_mapping_id);
				stmt1.setString(8, exit_pt_mapping_id);
				stmt1.setString(9, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					alloc_entry_qty=rset1.getDouble(1);
					alloc_exit_qty=rset1.getDouble(2);
					adj_imbalance=rset1.getDouble(3);
					derived_deparking=alloc_entry_qty-alloc_exit_qty;
				}
				rset1.close();
				stmt1.close();
				
				cumulative_imbalance+=(derived_deparking+adj_imbalance);
				
				tot_cumulative_imbalance+=cumulative_imbalance;
				tot_nom_entry_qty+=nom_entry_qty;
				tot_nom_exit_qty+=nom_exit_qty;
				tot_sch_entry_qty+=sch_entry_qty;
				tot_sch_exit_qty+=sch_exit_qty;
				tot_alloc_entry_qty+=alloc_entry_qty;
				tot_alloc_exit_qty+=alloc_exit_qty;
				tot_derived_deparking+=derived_deparking;
				tot_adj_imbalance+=adj_imbalance;
				
				VNOM_ENTRY_QTY.add(nf3.format(nom_entry_qty));
				VNOM_EXIT_QTY.add(nf3.format(nom_exit_qty));
				VSCH_ENTRY_QTY.add(nf3.format(sch_entry_qty));
				VSCH_EXIT_QTY.add(nf3.format(sch_exit_qty));
				VALLOC_ENTRY_QTY.add(nf3.format(alloc_entry_qty));
				VALLOC_EXIT_QTY.add(nf3.format(alloc_exit_qty));
				VADJ_IMBALANCE.add(nf3.format(adj_imbalance));
				VDERIVED_DEPARKING.add(nf3.format(derived_deparking));
				VCUMULATIVE_IMBALANCE.add(nf3.format(cumulative_imbalance));
			}
			
			total_nom_entry_qty=nf3.format(tot_nom_entry_qty);
			total_nom_exit_qty=nf3.format(tot_nom_exit_qty);
			total_sch_entry_qty=nf3.format(tot_sch_entry_qty);
			total_sch_exit_qty=nf3.format(tot_sch_exit_qty);
			total_alloc_entry_qty=nf3.format(tot_alloc_entry_qty);
			total_alloc_exit_qty=nf3.format(tot_alloc_exit_qty);
			total_adj_imbalance=nf3.format(tot_adj_imbalance);
			total_derived_deparking=nf3.format(tot_derived_deparking);
			total_cumulative_imbalance=nf3.format(tot_cumulative_imbalance);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCTMasterList()
	{
		String function_nm="getCTMasterList()";
		try
		{
			String cont_map="";
			if(!sales_cont_id.equals("") && sales_cont_id.length()>1 && contract_type.equals("C"))
			{
				String[] split=sales_cont_id.split("-");
				
				cont_map=split[0]+"-"+customer_cd+"-"+split[1]+"-%-"+split[3]+"-%";
			}
			
			int selCnt=0;
			
			if(contract_type.equals("C"))
			{
				queryString="SELECT A.CT_REF_NO,A.UTR_NO,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.SEQ_NO "
						+ "FROM FMS_TRANSPORTER_CT_MST A,FMS_TRANS_CT_CONT_MAP B "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
						+ "AND A.ENTRY_TRANS_CD=? AND A.ENTRY_TRANS_PLANT=? "
						+ "AND A.EXIT_COUNTERPARTY=? AND A.EXIT_PLANT_SEQ=? "
						+ "AND A.START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND B.CONT_MAPPING LIKE ? AND A.BU_SEQ=? AND A.STATUS='Y' "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.ENTRY_TRANS_CD=B.ENTRY_TRANS_CD AND A.ENTRY_TRANS_PLANT=B.ENTRY_TRANS_PLANT "
						+ "AND A.EXIT_COUNTERPARTY=B.EXIT_COUNTERPARTY AND A.EXIT_PLANT_SEQ=B.EXIT_PLANT_SEQ "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.BU_SEQ=B.BU_SEQ";
			}
			else
			{
				queryString="SELECT A.CT_REF_NO,A.UTR_NO,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.SEQ_NO "
						+ "FROM FMS_TRANSPORTER_CT_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? "
						+ "AND A.ENTRY_TRANS_CD=? AND A.ENTRY_TRANS_PLANT=? "
						+ "AND A.EXIT_COUNTERPARTY=? AND A.EXIT_PLANT_SEQ=? "
						+ "AND A.START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.BU_SEQ=? AND A.STATUS='Y' ";
			}
			stmt=conn.prepareStatement(queryString);
			if(contract_type.equals("C"))
			{
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, contract_type);
				stmt.setString(++selCnt, transporter_cd);
				stmt.setString(++selCnt, transporter_plant_seq);
				stmt.setString(++selCnt, counterparty_cd);
				stmt.setString(++selCnt, counterparty_plant_seq);
				stmt.setString(++selCnt, from_dt);
				stmt.setString(++selCnt, to_dt);
				stmt.setString(++selCnt, cont_map);
				stmt.setString(++selCnt, bu_unit);
			}
			else
			{
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, contract_type);
				stmt.setString(++selCnt, transporter_cd);
				stmt.setString(++selCnt, transporter_plant_seq);
				stmt.setString(++selCnt, counterparty_cd);
				stmt.setString(++selCnt, counterparty_plant_seq);
				stmt.setString(++selCnt, from_dt);
				stmt.setString(++selCnt, to_dt);
				stmt.setString(++selCnt, bu_unit);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCT_REF_MST.add(rset.getString(1)==null?"":rset.getString(1));
				VUTR_REF_MST.add(rset.getString(2)==null?"":rset.getString(2));
				VSTART_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VEND_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VSEQ_NO.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExchangeRateMaster()
	{
		String function_nm="getExchangeRateMaster()";
		try
		{
			
			queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY EXC_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEXCHNG_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEXCHNG_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInterestRateMaster()
	{
		String function_nm="getInterestRateMaster()";
		try
		{
			
			queryString = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_INT_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY INT_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VINT_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VINT_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractBillingDetail()
	{
		String function_nm="getContractBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_GTA_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, cont_no);
			stmt3.setString(5, contract_type);
			stmt3.setString(6, agreement_type);
			rset3 = stmt3.executeQuery();
			if(rset3.next())
			{
				 count = rset3.getInt(1);
			}
			rset3.close();
			stmt3.close();
			
			if(count>0)
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
							+ "BILLING_DAYS,EXCHG_VAL,TO_CHAR(EFF_DT,'DD/MM/YYYY'),EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_GTA_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					stmt.setString(7, agreement_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						billing_freq=rset.getString(1)==null?"F":rset.getString(1);
						billing_flag=rset.getString(2)==null?"B":rset.getString(2);
						due_date=rset.getString(3)==null?"2":rset.getString(3);
						sec_due_date=rset.getString(4)==null?"1":rset.getString(4);
						inv_currency=rset.getString(5)==null?"1":rset.getString(5);
						payment_currency=rset.getString(6)==null?"1":rset.getString(6);
						interest_rate_cd=rset.getString(7)==null?"":rset.getString(7);
						interest_cal_sign=rset.getString(8)==null?"":rset.getString(8);
						interest_cal_per=rset.getString(9)==null?"":rset.getString(9);
						exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
						exchng_cal=rset.getString(11)==null?"D":rset.getString(11);
						exchng_criteria=rset.getString(12)==null?"":rset.getString(12);
						exchng_note=rset.getString(13)==null?"":rset.getString(13);
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						billing_days=rset.getString(17)==null?"":rset.getString(17);
						exchg_val=nf2.format(rset.getDouble(18));
						old_eff_dt=rset.getString(19)==null?"":rset.getString(19);
						sat_days=rset.getString(20)==null?"":rset.getString(20);
						plant_seq=rset.getString(21)==null?"":rset.getString(21);
						String plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
						
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, plant_seq);
						stmt2.setString(7, agreement_type);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							state_map=rset2.getString(1)==null?"":rset2.getString(1);
							if(!state_map.equals(""))
							{
								String[] stateMap = state_map.split("@");
								
								for(int j=0; j<stateMap.length; j++)
								{
									if(!state_nm.equals(""))
									{
										state_nm+=", <font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
									else
									{
										state_nm+="<font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
								}
							}
							else
							{
								String queryString4="SELECT A.TIN "
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, counterparty_cd);
								stmt4.setString(3, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								else
								{
									String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
									plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								rset4.close();
								stmt4.close();
							}
						}
						rset2.close();
						stmt2.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
						
						
						queryString1="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
								+ "FROM FMS_GTA_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, contract_type);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							eff_dt=rset1.getString(1)==null?"":rset1.getString(1);
							
							if(eff_dt.equals(""))
							{
								//eff_dt=cont_start_dt;
								eff_dt=old_eff_dt;
							}
							else
							{
								eff_dt=dateUtil.getDate(eff_dt, "1");
							}
						}
						else
						{
							//eff_dt=cont_start_dt;
							eff_dt=old_eff_dt;
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						else
						{
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						rset4.close();
						stmt4.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
					}
					rset.close();
					stmt.close();
				}
			}
			else
			{
				eff_dt=cont_start_dt;
				int count1=0;
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
						+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE "
						+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, agreement_type);
				rset3 = stmt3.executeQuery();
				if(rset3.next())
				{
					 count1 = rset3.getInt(1);
				}
				rset3.close();
				stmt3.close();
				
				if(count1 > 0)
				{
					for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
					{
						queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
								+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
								+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO "
								+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? "
								+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
								+ "AND A.AGMT_NO=B.AGMT_NO)";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agreement_type);
						stmt.setString(5, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset=stmt.executeQuery();
						if(rset.next())
						{
							billing_freq=rset.getString(1)==null?"F":rset.getString(1);
							billing_flag=rset.getString(2)==null?"B":rset.getString(2);
							due_date=rset.getString(3)==null?"2":rset.getString(3);
							sec_due_date=rset.getString(4)==null?"1":rset.getString(4);
							inv_currency=rset.getString(5)==null?"1":rset.getString(5);
							payment_currency=rset.getString(6)==null?"1":rset.getString(6);
							interest_rate_cd=rset.getString(7)==null?"":rset.getString(7);
							interest_cal_sign=rset.getString(8)==null?"":rset.getString(8);
							interest_cal_per=rset.getString(9)==null?"":rset.getString(9);
							exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
							exchng_cal=rset.getString(11)==null?"D":rset.getString(11);
							exchng_criteria=rset.getString(12)==null?"":rset.getString(12);
							exchng_note=rset.getString(13)==null?"":rset.getString(13);
							
							due_dt_in=rset.getString(15)==null?"":rset.getString(15);
							exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
							billing_days=rset.getString(17)==null?"":rset.getString(17);
							exchg_val=nf2.format(rset.getDouble(18));
							sat_days=rset.getString(19)==null?"":rset.getString(19);
							plant_seq=rset.getString(20)==null?"":rset.getString(20);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							
							String state_map="";
							String state_nm = "";
							String queryString2="SELECT HOLIDAY_STATE "
									+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
									+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE "
									+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_no);
							stmt2.setString(4, agreement_type);
							stmt2.setString(5, plant_seq);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								state_map=rset2.getString(1)==null?"":rset2.getString(1);
								if(!state_map.equals(""))
								{
									String[] stateMap = state_map.split("@");
									
									for(int j=0; j<stateMap.length; j++)
									{
										if(!state_nm.equals(""))
										{
											state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
										}
										else
										{
											state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
										}
									}
								}
								else
								{
									String queryString4="SELECT A.TIN "
											+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
											+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
											+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
											+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
											+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
											+ "AND C.SEQ_NO=B.SEQ_NO)";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, comp_cd);
									stmt4.setString(2, counterparty_cd);
									stmt4.setString(3, plant_seq);
									rset4=stmt4.executeQuery();
									if(rset4.next())
									{
										String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
										plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
										if(!state_map.equals(""))
										{
											state_map=state_cd;
											state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
										}
										else
										{
											state_map=state_cd;
											state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
										}
									}
									else
									{
										String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
										plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
										if(!state_map.equals(""))
										{
											state_map=state_cd;
											state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
										}
										else
										{
											state_map=state_cd;
											state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
										}
									}
									rset4.close();
									stmt4.close();
								}
							}
							rset2.close();
							stmt2.close();
							
							if(!state_map.equals(""))
							{
								if(!holiday_state.equals(""))
								{
									holiday_state+="@@"+plant_seq+"//"+state_map;
								}
								else
								{
									holiday_state+=plant_seq+"//"+state_map;
								}
								
								if(!disp_holiday_state.equals(""))
								{
									disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
								}
								else
								{
									disp_holiday_state+=plant_abbr+" - "+state_nm;
								}
							}
							VPLANT_SEQ.add(plant_seq);
							
							exchg_val=nf2.format(rset.getDouble(17));
						}
						else
						{
							String state_map="";
							String plant_abbr="";
							String state_nm="";
							String queryString4="SELECT A.TIN "
									+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
									+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
									+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
									+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
									+ "AND C.SEQ_NO=B.SEQ_NO)";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
								plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
								
								plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
								if(!state_map.equals(""))
								{
									state_map=state_cd;
									state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
								}
								else
								{
									state_map=state_cd;
									state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
								}
							}
							else
							{
								plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
								String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
								plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
								if(!state_map.equals(""))
								{
									state_map=state_cd;
									state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
								}
								else
								{
									state_map=state_cd;
									state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
								}
							}
							rset4.close();
							stmt4.close();
							
							if(!state_map.equals(""))
							{
								if(!holiday_state.equals(""))
								{
									holiday_state+="@@"+plant_seq+"//"+state_map;
								}
								else
								{
									holiday_state+=plant_seq+"//"+state_map;
								}
								
								if(!disp_holiday_state.equals(""))
								{
									disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
								}
								else
								{
									disp_holiday_state+=plant_abbr+" - "+state_nm;
								}
							}
							VPLANT_SEQ.add(plant_seq);
						}
						rset.close();
						stmt.close();
					}
				}
				else
				{
					for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						else
						{
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						rset4.close();
						stmt4.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtpaBillingDetail()
	{
		String function_nm="getGtpaBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, agreement_type);
			rset3 = stmt3.executeQuery();
			if(rset3.next())
			{
				 count = rset3.getInt(1);
			}
			rset3.close();
			stmt3.close();
			
			if(count > 0)
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
							+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
							+ "AND A.AGMT_NO=B.AGMT_NO)";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agreement_type);
					stmt.setString(5, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset=stmt.executeQuery();
					if(rset.next())
					{
						billing_freq=rset.getString(1)==null?"F":rset.getString(1);
						billing_flag=rset.getString(2)==null?"B":rset.getString(2);
						due_date=rset.getString(3)==null?"2":rset.getString(3);
						sec_due_date=rset.getString(4)==null?"1":rset.getString(4);
						inv_currency=rset.getString(5)==null?"1":rset.getString(5);
						payment_currency=rset.getString(6)==null?"1":rset.getString(6);
						interest_rate_cd=rset.getString(7)==null?"":rset.getString(7);
						interest_cal_sign=rset.getString(8)==null?"":rset.getString(8);
						interest_cal_per=rset.getString(9)==null?"":rset.getString(9);
						exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
						exchng_cal=rset.getString(11)==null?"D":rset.getString(11);
						exchng_criteria=rset.getString(12)==null?"":rset.getString(12);
						exchng_note=rset.getString(13)==null?"":rset.getString(13);
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						billing_days=rset.getString(17)==null?"":rset.getString(17);
						exchg_val=nf2.format(rset.getDouble(18));
						sat_days=rset.getString(19)==null?"":rset.getString(19);
						plant_seq=rset.getString(20)==null?"":rset.getString(20);
						String plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_GTA_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_GTA_AGMT_BILLING_DTL B WHERE "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agreement_type);
						stmt2.setString(5, plant_seq);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							state_map=rset2.getString(1)==null?"":rset2.getString(1);
							if(!state_map.equals(""))
							{
								String[] stateMap = state_map.split("@");
								
								for(int j=0; j<stateMap.length; j++)
								{
									if(!state_nm.equals(""))
									{
										state_nm+=", <font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
									else
									{
										state_nm+="<font style='background:var(--sys_data_highlight)'>"+utilBean.getStateName(conn,stateMap[j])+"</font>";
									}
								}
							}
							else
							{
								String queryString4="SELECT A.TIN "
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, counterparty_cd);
								stmt4.setString(3, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								else
								{
									String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
									plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
									if(!state_map.equals(""))
									{
										state_map=state_cd;
										state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
									else
									{
										state_map=state_cd;
										state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
									}
								}
								rset4.close();
								stmt4.close();
							}
						}
						rset2.close();
						stmt2.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
						
						exchg_val=nf2.format(rset.getDouble(17));
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						else
						{
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
							plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
							if(!state_map.equals(""))
							{
								state_map=state_cd;
								state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
							else
							{
								state_map=state_cd;
								state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
							}
						}
						rset4.close();
						stmt4.close();
						
						if(!state_map.equals(""))
						{
							if(!holiday_state.equals(""))
							{
								holiday_state+="@@"+plant_seq+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+state_map;
							}
							
							if(!disp_holiday_state.equals(""))
							{
								disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
							}
							else
							{
								disp_holiday_state+=plant_abbr+" - "+state_nm;
							}
						}
						VPLANT_SEQ.add(plant_seq);
					}
					rset.close();
					stmt.close();
				}
			}
			else
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					String state_map="";
					String plant_abbr="";
					String state_nm="";
					String queryString4="SELECT A.TIN "
							+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_BU_DTL B "
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='R' "
							+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
							+ "AND C.SEQ_NO=B.SEQ_NO)";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterparty_cd);
					stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						
						plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
						if(!state_map.equals(""))
						{
							state_map=state_cd;
							state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
						else
						{
							state_map=state_cd;
							state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
					}
					else
					{
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
						plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "R");
						if(!state_map.equals(""))
						{
							state_map=state_cd;
							state_nm+=", <font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
						else
						{
							state_map=state_cd;
							state_nm+="<font style='background:var(--temp_data_highlight)'>"+utilBean.getStateName(conn,state_cd)+"</font>";
						}
					}
					rset4.close();
					stmt4.close();
					
					if(!state_map.equals(""))
					{
						if(!holiday_state.equals(""))
						{
							holiday_state+="@@"+plant_seq+"//"+state_map;
						}
						else
						{
							holiday_state+=plant_seq+"//"+state_map;
						}
						
						if(!disp_holiday_state.equals(""))
						{
							disp_holiday_state+="<br>"+plant_abbr+" - "+state_nm;
						}
						else
						{
							disp_holiday_state+=plant_abbr+" - "+state_nm;
						}
					}
					VPLANT_SEQ.add(plant_seq);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaContractTransporterMst()
	{
		String function_nm="getGtaContractTransporterMst()";
		try
		{
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd); 
			stmt.setString(2, agreement_type); 
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cp_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(cp_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,cp_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,cp_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String checkTransPlantInvoice(String plant_seq)
	{
		String function_nm="checkTransPlantInvoice()";
		String chk_trans_plant="";
		try
		{
			String plant = "";
		    plant = "SELECT CASE WHEN TRANS_BU_UNIT=? THEN 'Y' ELSE 'N' END BU_UNIT ";
			/*else 
			{
			    plant = "SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT ";
			}*/
			String query=plant
				+ "FROM FMS_GTA_SG_INV_MST "
				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
				+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
				query += "AND TRANS_BU_UNIT=? ";
			String temp_query=query;
			stmt1=conn.prepareStatement(temp_query);
			stmt1.setString(1, plant_seq);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, counterparty_cd);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, cont_no);
			stmt1.setString(6, contract_type);
			stmt1.setString(7, plant_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				chk_trans_plant=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_trans_plant;
	}
	
	public void getchkContBuUnit()
	{
		String function_nm="getchkContBuUnit()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VCONT_SEL_BU_PLANT_SEQ_NO.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getchkContTransBu()
	{
		String function_nm="getchkContTransBu()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_TRANS_BU A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VCONT_SEL_TRANS_BU_SEQ_NO.add(plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getchkContEntryPoint()
	{
		String function_nm="getchkContEntryPoint()";
		try
		{
			queryString="SELECT DISTINCT ENTRY_PT_MAPPING_ID "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String entry_map = rset.getString(1)==null?"":rset.getString(1);
				
				VCONT_SEL_ENTRY_POINT.add(entry_map);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getchkContEXITPoint()
	{
		String function_nm="getchkContEXITPoint()";
		try
		{
			queryString="SELECT DISTINCT EXIT_PT_MAPPING_ID "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String exit_map = rset.getString(1)==null?"":rset.getString(1);
				
				VCONT_SEL_EXIT_POINT.add(exit_map);
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
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}
	
	String counterparty_cd = "";
	String customer_cd = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String cont_no ="";
	String cont_rev_no ="";
	String contract_type = "";
	String cont_start_dt = "";
	String cont_end_dt = "";
	String gtc_type = "";
	String from_dt = "";
	String to_dt = "";
	String transporter_cd = "";
	String transporter_plant_seq = "";
	String counterparty_plant_seq = "";
	String sales_cont_id = "";
	String bu_unit = "";
	String agreement_type = "";
	String entry_point = "";
	String exit_point = "";
	String trans_bu_unit = "";
	String active_status = "";
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCustomer_cd(String customer_cd) {this.customer_cd = customer_cd;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setCont_end_dt(String cont_end_dt) {this.cont_end_dt = cont_end_dt;}
	public void setGtc_type(String gtc_type) {this.gtc_type = gtc_type;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setTransporter_cd(String transporter_cd) {this.transporter_cd = transporter_cd;}
	public void setTransporter_plant_seq(String transporter_plant_seq) {this.transporter_plant_seq = transporter_plant_seq;}
	public void setCounterparty_plant_seq(String counterparty_plant_seq) {this.counterparty_plant_seq = counterparty_plant_seq;}
	public void setSales_cont_id(String sales_cont_id) {this.sales_cont_id = sales_cont_id;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setAgreement_type(String agreement_type) {this.agreement_type = agreement_type;}
	public void setEntry_point(String entry_point) {this.entry_point = entry_point;}
	public void setExit_point(String exit_point) {this.exit_point = exit_point;}
	public void setTrans_bu_unit(String trans_bu_unit) {this.trans_bu_unit = trans_bu_unit;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
	public String getCustomer_cd() {return customer_cd;}
	public String getFrom_dt() {return from_dt;}
	public String getTo_dt() {return to_dt;}
	public String getAgreement_type() {return agreement_type;}
	public String getAgmt_no() {return agmt_no;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	
	Vector VCUSTOMER_CD = new Vector();
	Vector VCUSTOMER_NM = new Vector();
	Vector VCUSTOMER_ABBR = new Vector();
	
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VTOTAL_QTY = new Vector();
	Vector VUNIT_CD = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCALC_BASE = new Vector();
	Vector VSTATUS = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VDIS_DEAL_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VTCQ = new Vector();
	Vector VCT_REF_NO = new Vector();
	Vector VLINKED_SALES_CONT = new Vector();
	
	Vector VENTRY_COUNTERPTY_CD = new Vector();
	Vector VENTRY_COUNTERPTY_ABBR = new Vector();
	Vector VENTRY_PLANT_SEQ = new Vector();
	Vector VENTRY_PLANT_ABBR = new Vector();
	Vector VENTRY_PLANT_NM = new Vector();
	Vector VENTRY_METER_SEQ = new Vector();
	Vector VENTRY_METER_REF = new Vector();
	Vector VENTRY_MAPPING = new Vector();
	Vector VENTRY_STATUS = new Vector();
	Vector VSEL_ENTRY_MAPPING = new Vector();
	Vector VENTRY_POINT_NAME = new Vector();
	
	Vector VEXIT_COUNTERPTY_CD = new Vector();
	Vector VEXIT_COUNTERPTY_ABBR = new Vector();
	Vector VEXIT_PLANT_SEQ = new Vector();
	Vector VEXIT_PLANT_ABBR = new Vector();
	Vector VEXIT_PLANT_NM = new Vector();
	Vector VEXIT_ENTITY= new Vector();
	Vector VEXIT_ENTITY_NM = new Vector();
	Vector VEXIT_MAPPING = new Vector();
	Vector VEXIT_STATUS = new Vector();
	Vector VSEL_EXIT_MAPPING = new Vector();
	Vector VEXIT_POINT_NAME = new Vector();
	
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VTRANS_BU_NM = new Vector();
	Vector VTRANS_BU_ABBR = new Vector();
	Vector VTRANS_BU_SEQ_NO = new Vector();
	
	Vector VSEL_TRANS_BU_SEQ_NO = new Vector();
	
	Vector VDELV_POINT = new Vector();
	Vector VBU_POINT = new Vector();
	Vector VCUST_PLANT_POINT = new Vector();
	
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	
	Vector VNOM_REV_NO = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VGEN_DT = new Vector();
	Vector VGEN_TIME = new Vector();
	Vector VBASE = new Vector();
	Vector VGCV = new Vector();
	Vector VNCV = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VQTY_SCM = new Vector();
	Vector VNOM_COLOR = new Vector();
	Vector VEXIT_BASE = new Vector();
	Vector VEXIT_GCV = new Vector();
	Vector VEXIT_NCV = new Vector();
	Vector VEXIT_QTY_MMBTU = new Vector();
	Vector VEXIT_QTY_SCM = new Vector();

	Vector VENTRY_TRANSCD_STATUS = new Vector();
	Vector VEXIT_TRANSCD_STATUS = new Vector();
	
	Vector VMDQ = new Vector();
	Vector VMDQ_UNIT = new Vector();

	Vector VIS_DONE = new Vector();
	Vector VIS_ACTIVE = new Vector();
	
	Vector VNOM_ENTRY_QTY = new Vector();
	Vector VNOM_EXIT_QTY = new Vector();
	Vector VSCH_ENTRY_QTY = new Vector();
	Vector VSCH_EXIT_QTY = new Vector();
	Vector VALLOC_ENTRY_QTY = new Vector();
	Vector VALLOC_EXIT_QTY = new Vector();
	
	Vector VTRANSMISSION_QTY = new Vector();
	Vector VDAILY_IMBALANCE = new Vector();
	Vector VCUMULATIVE_IMBALANCE = new Vector();
	Vector VDAILY_UNAUTHORIZED_OVERRUN = new Vector();
	Vector VCHARGEABLE_OVERRUN = new Vector();
	Vector VCHARGEABLE_POSITIVE_IMBALANCE = new Vector();
	Vector VCHARGEABLE_NEGETIVE_IMBALANCE = new Vector();
	
	Vector VCT_REF_MST = new Vector();
	Vector VUTR_REF_MST = new Vector();
	Vector VSEQ_NO = new Vector();
	
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VAGMT_TYPE = new Vector();
	
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_CATEGORY = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	Vector VADJ_IMBALANCE = new Vector();
	Vector VDERIVED_DEPARKING = new Vector();
	
	Vector VNOM_SEL_BU_CHK = new Vector();
	Vector VNOM_SEL_TRANS_BU_CHK = new Vector();
	Vector VCONT_SEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VCONT_SEL_TRANS_BU_SEQ_NO = new Vector();
	Vector VCONT_SEL_ENTRY_POINT = new Vector();
	Vector VCONT_SEL_EXIT_POINT = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	
	public Vector getVCUSTOMER_CD() {return VCUSTOMER_CD;}
	public Vector getVCUSTOMER_NM() {return VCUSTOMER_NM;}
	public Vector getVCUSTOMER_ABBR() {return VCUSTOMER_ABBR;}
	
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVTOTAL_QTY() {return VTOTAL_QTY;}
	public Vector getVUNIT_CD() {return VUNIT_CD;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCALC_BASE() {return VCALC_BASE;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVDIS_DEAL_NO() {return VDIS_DEAL_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVCT_REF_NO() {return VCT_REF_NO;}
	public Vector getVLINKED_SALES_CONT() {return VLINKED_SALES_CONT;}
	
	public Vector getVENTRY_COUNTERPTY_CD() {return VENTRY_COUNTERPTY_CD;}
	public Vector getVENTRY_COUNTERPTY_ABBR() {return VENTRY_COUNTERPTY_ABBR;}
	public Vector getVENTRY_PLANT_SEQ() {return VENTRY_PLANT_SEQ;}
	public Vector getVENTRY_PLANT_ABBR() {return VENTRY_PLANT_ABBR;}
	public Vector getVENTRY_PLANT_NM() {return VENTRY_PLANT_NM;}
	public Vector getVENTRY_METER_SEQ() {return VENTRY_METER_SEQ;}
	public Vector getVENTRY_METER_REF() {return VENTRY_METER_REF;}
	public Vector getVENTRY_MAPPING() {return VENTRY_MAPPING;}
	public Vector getVENTRY_STATUS() {return VENTRY_STATUS;}
	public Vector getVSEL_ENTRY_MAPPING() {return VSEL_ENTRY_MAPPING;}
	public Vector getVENTRY_POINT_NAME() {return VENTRY_POINT_NAME;}
	
	public Vector getVEXIT_COUNTERPTY_CD() {return VEXIT_COUNTERPTY_CD;}
	public Vector getVEXIT_COUNTERPTY_ABBR() {return VEXIT_COUNTERPTY_ABBR;}
	public Vector getVEXIT_PLANT_SEQ() {return VEXIT_PLANT_SEQ;}
	public Vector getVEXIT_PLANT_ABBR() {return VEXIT_PLANT_ABBR;}
	public Vector getVEXIT_PLANT_NM() {return VEXIT_PLANT_NM;}
	public Vector getVEXIT_ENTITY() {return VEXIT_ENTITY;}
	public Vector getVEXIT_ENTITY_NM() {return VEXIT_ENTITY_NM;}
	public Vector getVEXIT_MAPPING() {return VEXIT_MAPPING;}
	public Vector getVEXIT_STATUS() {return VEXIT_STATUS;}
	public Vector getVSEL_EXIT_MAPPING() {return VSEL_EXIT_MAPPING;}
	public Vector getVEXIT_POINT_NAME() {return VEXIT_POINT_NAME;}
	
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVTRANS_BU_NM() {return VTRANS_BU_NM;}
	public Vector getVTRANS_BU_ABBR() {return VTRANS_BU_ABBR;}
	public Vector getVTRANS_BU_SEQ_NO() {return VTRANS_BU_SEQ_NO;}
	
	public Vector getVSEL_TRANS_BU_SEQ_NO() {return VSEL_TRANS_BU_SEQ_NO;}
	
	public Vector getVDELV_POINT() {return VDELV_POINT;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVCUST_PLANT_POINT() {return VCUST_PLANT_POINT;}
	
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	
	public Vector getVNOM_REV_NO() {return VNOM_REV_NO;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVGEN_TIME() {return VGEN_TIME;}
	public Vector getVGEN_DT() {return VGEN_DT;}
	public Vector getVBASE() {return VBASE;}
	public Vector getVGCV() {return VGCV;}
	public Vector getVNCV() {return VNCV;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVQTY_SCM() {return VQTY_SCM;}
	public Vector getVNOM_COLOR() {return VNOM_COLOR;}
	public Vector getVEXIT_BASE() {return VEXIT_BASE;}
	public Vector getVEXIT_GCV() {return VEXIT_GCV;}
	public Vector getVEXIT_NCV() {return VEXIT_NCV;}
	public Vector getVEXIT_QTY_MMBTU() {return VEXIT_QTY_MMBTU;}
	public Vector getVEXIT_QTY_SCM() {return VEXIT_QTY_SCM;}

	public Vector getVENTRY_TRANSCD_STATUS() {return VENTRY_TRANSCD_STATUS;}
	public Vector getVEXIT_TRANSCD_STATUS() {return VEXIT_TRANSCD_STATUS;}
	
	public Vector getVMDQ() {return VMDQ;}
	public Vector getVMDQ_UNIT() {return VMDQ_UNIT;}
	
	public Vector getVIS_DONE() {return VIS_DONE;}
	public Vector getVIS_ACTIVE() {return VIS_ACTIVE;}
	
	public Vector getVNOM_ENTRY_QTY() {return VNOM_ENTRY_QTY;}
	public Vector getVNOM_EXIT_QTY() {return VNOM_EXIT_QTY;}
	public Vector getVSCH_ENTRY_QTY() {return VSCH_ENTRY_QTY;}
	public Vector getVSCH_EXIT_QTY() {return VSCH_EXIT_QTY;}
	public Vector getVALLOC_ENTRY_QTY() {return VALLOC_ENTRY_QTY;}
	public Vector getVALLOC_EXIT_QTY() {return VALLOC_EXIT_QTY;}
	
	public Vector getVTRANSMISSION_QTY() {return VTRANSMISSION_QTY;}
	public Vector getVDAILY_IMBALANCE() {return VDAILY_IMBALANCE;}
	public Vector getVCUMULATIVE_IMBALANCE() {return VCUMULATIVE_IMBALANCE;}
	public Vector getVDAILY_UNAUTHORIZED_OVERRUN() {return VDAILY_UNAUTHORIZED_OVERRUN;}
	public Vector getVCHARGEABLE_OVERRUN() {return VCHARGEABLE_OVERRUN;}
	public Vector getVCHARGEABLE_POSITIVE_IMBALANCE() {return VCHARGEABLE_POSITIVE_IMBALANCE;}
	public Vector getVCHARGEABLE_NEGETIVE_IMBALANCE() {return VCHARGEABLE_NEGETIVE_IMBALANCE;}
	
	public Vector getVCT_REF_MST() {return VCT_REF_MST;}
	public Vector getVUTR_REF_MST() {return VUTR_REF_MST;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	public Vector getVADJ_IMBALANCE() {return VADJ_IMBALANCE;}
	public Vector getVDERIVED_DEPARKING() {return VDERIVED_DEPARKING;}
	
	public Vector getVNOM_SEL_BU_CHK() {return VNOM_SEL_BU_CHK;}
	public Vector getVNOM_SEL_TRANS_BU_CHK() {return VNOM_SEL_TRANS_BU_CHK;}
	public Vector getVCONT_SEL_BU_PLANT_SEQ_NO() {return VCONT_SEL_BU_PLANT_SEQ_NO;}
	public Vector getVCONT_SEL_TRANS_BU_SEQ_NO() {return VCONT_SEL_TRANS_BU_SEQ_NO;}
	public Vector getVCONT_SEL_ENTRY_POINT() {return VCONT_SEL_ENTRY_POINT;}
	public Vector getVCONT_SEL_EXIT_POINT() {return VCONT_SEL_EXIT_POINT;}
	
	String cont_name ="";
	String cont_ref_no ="";
	String ct_ref_no ="";
	String start_dt ="";
	String end_dt ="";
	String entry_pt_mapping_id ="";
	String exit_pt_mapping_id ="";
	String entry_pt_nm ="";
	String exit_pt_nm ="";
	String mdq ="";
	String mdq_unit ="";
	String variable_mdq ="";
	String rate_unit ="";
	String transport_rate ="";
	String positive_imb_rate ="";
	String negetive_imb_rate ="";
	String unauth_overrun_rate ="";
	String sip_pay_rate ="";
	String sales_cont_nm="";
	String sales_cont_map="";
	String agmt_start_dt ="";
	String agmt_end_dt ="";
	String agmt_calc_base ="";
	String calc_base ="";
	String gcv ="";
	String ncv ="";
	String nom_count ="";
	String sip_pay_freq ="";
	String sip_pay_percent ="";
	String ct_seq_no ="";
	String display_dealNo ="";
	
	String forthnighly_start_dt="";
	String forthnighly_end_dt="";
	
	String bu_plant_seq="";
	String linked_sales_cont_map="";
	
	String billing_freq="F";
	String billing_flag="B";
	String due_date="2";
	String sec_due_date="1";
	String inv_currency="1";
	String payment_currency="1";
	String interest_rate_cd="";
	String interest_cal_sign="";
	String interest_cal_per="";
	String exchng_rate_cd="";
	String exchng_cal="D";
	String exchng_criteria="";
	String exchng_note="";
	String due_dt_in="";
	String exclude_sat="";
	String billing_days="";
	String exchg_val="";
	String eff_dt="";
	String old_eff_dt="";
	
	String total_nom_entry_qty="";
	String total_nom_exit_qty="";
	String total_sch_entry_qty="";
	String total_sch_exit_qty="";
	String total_alloc_entry_qty="";
	String total_alloc_exit_qty="";
	String total_var_mdq="";
	String total_transmission_qty="";
	String total_chargeable_overrun="";
	String total_positive_imbalance="";
	String total_negitive_imbalance="";
	String total_derived_deparking="";
	String total_cumulative_imbalance="";
	String total_adj_imbalance="";
	
	String total_entry_qty_mmbtu="";
	String total_exit_qty_mmbtu="";
	String total_entry_qty_scm="";
	String total_exit_qty_scm="";
	
	String no_of_billing_dtl="";
	
	String counterparty_abbr="";
	String counterparty_name="";
	String deal_map="";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	
	
	String agmt_status="";
	String agmt_trans_qty="";
	String agmt_trans_qty_unit="";
	String agmt_name="";
	String sel_entry_mapping="";
	String sel_exit_mapping="";
	
	String parking_rate="";
	String max_park_qty="";
	String max_park_qty_unit="";
	
	String parking_cont_map="";
	String parking_sell_cont_map="";
	
	String max_date="";
	String min_nom_date="";
	
	public String getContract_type() {return contract_type;}
	public String getCont_name() {return cont_name;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getCt_ref_no() {return ct_ref_no;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getEntry_pt_mapping_id() {return entry_pt_mapping_id;}
	public String getExit_pt_mapping_id() {return exit_pt_mapping_id;}
	public String getEntry_pt_nm() {return entry_pt_nm;}
	public String getExit_pt_nm() {return exit_pt_nm;}
	public String getMdq() {return mdq;}
	public String getMdq_unit() {return mdq_unit;}
	public String getVariable_mdq() {return variable_mdq;}
	public String getRate_unit() {return rate_unit;}
	public String getTransport_rate() {return transport_rate;}
	public String getPositive_imb_rate() {return positive_imb_rate;}
	public String getNegetive_imb_rate() {return negetive_imb_rate;}
	public String getUnauth_overrun_rate() {return unauth_overrun_rate;}
	public String getSip_pay_rate() {return sip_pay_rate;}
	public String getSales_cont_nm() {return sales_cont_nm;}
	public String getSales_cont_map() {return sales_cont_map;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
	public String getAgmt_calc_base() {return agmt_calc_base;}
	public String getCalc_base() {return calc_base;}
	public String getGcv() {return gcv;}
	public String getNcv() {return ncv;}
	public String getNom_count() {return nom_count;}
	public String getSip_pay_freq() {return sip_pay_freq;}
	public String getSip_pay_percent() {return sip_pay_percent;}
	public String getCt_seq_no() {return ct_seq_no;}
	public String getDisplay_dealNo() {return display_dealNo;}
	
	public String getForthnighly_start_dt() {return forthnighly_start_dt;}
	public String getForthnighly_end_dt() {return forthnighly_end_dt;}
	
	public String getBu_plant_seq() {return bu_plant_seq;}
	public String getLinked_sales_cont_map() {return linked_sales_cont_map;}
	
	public String getBilling_freq() {return billing_freq;}
	public String getBilling_flag() {return billing_flag;}
	public String getDue_date() {return due_date;}
	public String getSec_due_date() {return sec_due_date;}
	public String getInv_currency() {return inv_currency;}
	public String getPayment_currency() {return payment_currency;}
	public String getInterest_rate_cd() {return interest_rate_cd;}
	public String getInterest_cal_sign() {return interest_cal_sign;}
	public String getInterest_cal_per() {return interest_cal_per;}
	public String getExchng_rate_cd() {return exchng_rate_cd;}
	public String getExchng_cal() {return exchng_cal;}
	public String getExchng_criteria() {return exchng_criteria;}
	public String getExchng_note() {return exchng_note;}
	public String getDue_dt_in() {return due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getBilling_days() {return billing_days;}
	public String getExchg_val() {return exchg_val;}
	public String getEff_dt() {return eff_dt;}
	public String getOld_eff_dt() {return old_eff_dt;}
	
	public String getTotal_nom_entry_qty() {return total_nom_entry_qty;}
	public String getTotal_nom_exit_qty() {return total_nom_exit_qty;}
	public String getTotal_sch_entry_qty() {return total_sch_entry_qty;}
	public String getTotal_sch_exit_qty() {return total_sch_exit_qty;}
	public String getTotal_alloc_entry_qty() {return total_alloc_entry_qty;}
	public String getTotal_alloc_exit_qty() {return total_alloc_exit_qty;}
	public String getTotal_var_mdq() {return total_var_mdq;}
	public String getTotal_transmission_qty() {return total_transmission_qty;}
	public String getTotal_chargeable_overrun() {return total_chargeable_overrun;}
	public String getTotal_positive_imbalance() {return total_positive_imbalance;}
	public String getTotal_negitive_imbalance() {return total_negitive_imbalance;}
	public String getTotal_derived_deparking() {return total_derived_deparking;}
	public String getTotal_cumulative_imbalance() {return total_cumulative_imbalance;}
	public String getTotal_adj_imbalance() {return total_adj_imbalance;}
	
	public String getTotal_entry_qty_mmbtu() {return total_entry_qty_mmbtu;}
	public String getTotal_exit_qty_mmbtu() {return total_exit_qty_mmbtu;}
	public String getTotal_entry_qty_scm() {return total_entry_qty_scm;}
	public String getTotal_exit_qty_scm() {return total_exit_qty_scm;}
	
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
	
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getCounterparty_name() {return counterparty_name;}
	public String getDeal_map() {return deal_map;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
	
	public String getAgmt_status() {return agmt_status;}
	public String getAgmt_name() {return agmt_name;}
	public String getAgmt_trans_qty() {return agmt_trans_qty;}
	public String getAgmt_trans_qty_unit() {return agmt_trans_qty_unit;}
	public String getSel_entry_mapping() {return sel_entry_mapping;}
	public String getSel_exit_mapping() {return sel_exit_mapping;}
	
	public String getParking_rate() {return parking_rate;}
	public String getMax_park_qty() {return max_park_qty;}
	public String getMax_park_qty_unit() {return max_park_qty_unit;}
	
	public String getParking_cont_map() {return parking_cont_map;}
	public String getParking_sell_cont_map() {return parking_sell_cont_map;}
	
	public String getMax_date() {return max_date;}
	public String getMin_nom_date() {return min_nom_date;}
}
