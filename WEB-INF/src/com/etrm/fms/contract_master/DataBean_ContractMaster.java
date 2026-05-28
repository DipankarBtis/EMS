package com.etrm.fms.contract_master;

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

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DataBean_ContractMaster 
{
	String db_src_file_name="DataBean_ContractMaster.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt_temp;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset_temp;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	String gx="K";//AS OF NOW HARDCODED 20230913
	
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
	    			
	    			if(callFlag.equalsIgnoreCase("AGREEMENT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCustomerCounterpartyList();
	    				}
	    				else 
	    				{
	    					getFgsaCounterpartyList();
	    				}
	    				
	    				//getCustomerCounterpartyList();
	    				getCustomerPlantList();
	    				getTransporterPlantList();
	    				getBusinessPlantList();
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getAgreementDetail();
	    					getSelectedCustomerPlantList();
		    				getSelectedTransporterPlantList();
		    				getSelectedBusinessPlantList();
		    				getSelectedAgmtLiabilityDtl();
		    				getCurrentContractDtl();
		    			}
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_AGREEMENT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCustomerCounterpartyList();
	    				}
	    				else 
	    				{
	    					getFgsaCounterpartyList();
	    				}
	    				getCustomerPlantList();
	    				getTruckTransporterList();
	    				getBusinessPlantList();
	    				getFillingStationList();
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getAgreementDetail();
	    					getSelectedCustomerPlantList();
	    					getSelectedTruckTransporterList();
	    					getSelectedFillStationList();
	    					getSelectedBusinessPlantList();
	    					getSelectedAgmtLiabilityDtl();
	    					getCurrentContractDtl();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("AGREEMENT_LIST"))
	    			{
	    				getAgreementList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("AGREEMENT_BILLING_DTL"))
	    			{
	    				disp_agmt_no = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				couterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getBillingDetail();
	    				getApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST"))
	    			{
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
	    				
	    				if(contract_type.equals("X"))
	    				{
	    					String abbr="IGX";
	    					getGxCounterpartyCd(abbr);
	    					getGasExchangeBuPlantList();
	    				}
	    				getSelectedAgreementDetail();
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCustomerCounterpartyList();
	    				}
	    				else 
	    				{
	    					getSupplyCounterpartyList();
	    				}
	    				getCustomerPlantList();
	    				getTransporterPlantList();
	    				getBusinessPlantList();
	    				getSelectedAgmtLiabilityDtl();
	    				getChargeMaster();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getSupplyContractDetail();
	    					getContractSelectedCustomerPlantList();
		    				getContractSelectedTransporterPlantList();
		    				getContractSelectedBusinessPlantList();
		    				getContractSelectedGasExchangeBuPlantList();
		    				getCountContractBillingDetail();
		    				getCountSecurityDetail();
		    				getPriceChangeHistory();
		    				getSelectedContLiabilityDtl();
		    				//if(contdt_change_request_flag.equals("Y"))
		    				{
		    					getMaxAllocationInvoiceDate();
		    				}
		    				
		    				if(agmt_base.equals("D"))
		    				{
		    					getGtaContractDetail();
		    				}
		    			}
	    				else if(contract_type.equals("S") && opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getSupplyContractDetail();
	    					getContractSelectedCustomerPlantList();
		    				getContractSelectedTransporterPlantList();
		    				getContractSelectedBusinessPlantList();
	    				}
	    				
	    				is_inv_submitted=""+isInvoiceSubmitted(counterparty_cd, cont_no, agmt_no, contract_type);
	    				getAgmtBaseFormAgmt();
	    				getNominatedChk();
	    			} 
	    			else if(callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST"))
	    			{
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
	    				
	    				if(contract_type.equals("W"))
	    				{
	    					String abbr="IGX";
	    					getGxCounterpartyCd(abbr);
	    					getGasExchangeBuPlantList();
	    				}
	    				getSelectedAgreementDetail();
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCustomerCounterpartyList();
	    				}
	    				else 
	    				{
	    					getSupplyCounterpartyList();
	    				}
	    				getCustomerPlantList();
	    				getFillingStationList();
	    				getTruckTransporterList();
	    				getBusinessPlantList();
	    				getSelectedAgmtLiabilityDtl();
	    				//getChargeMaster();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getSupplyContractDetail();
	    					getContractSelectedCustomerPlantList();
	    					getContractSelectedTruckTransporterList();
	    					getContractSelectedFillingStationList();
	    					getContractSelectedBusinessPlantList();
	    					getContractSelectedGasExchangeBuPlantList();
	    					getCountContractBillingDetail();
	    					getCountSecurityDetail();
	    					getPriceChangeHistory();
	    					getSelectedContLiabilityDtl();
	    					//if(contdt_change_request_flag.equals("Y"))
	    					{
	    						getMaxAllocationInvoiceDate();
	    					}
	    					
	    					if(agmt_base.equals("D"))
	    					{
	    						getSoTsContractDetail();
	    					}
	    				}
	    				else if(contract_type.equals("F") && opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getSupplyContractDetail();
	    					getContractSelectedCustomerPlantList();
	    					getContractSelectedTruckTransporterList();
	    					getContractSelectedFillingStationList();
	    					getContractSelectedBusinessPlantList();
	    				}
	    				
	    				is_inv_submitted=""+isInvoiceSubmitted(counterparty_cd, cont_no, agmt_no, contract_type);
	    				getAgmtBaseFormAgmt();
	    				getNominatedChk();
	    			} 
	    			else if(callFlag.equalsIgnoreCase("CONTRACT_LIST") || callFlag.equalsIgnoreCase("DLNG_CONTRACT_LIST"))
	    			{
	    				getSupplyContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONTRACT_LIST_FCC") || callFlag.equalsIgnoreCase("DLNG_CONTRACT_LIST_FCC"))
	    			{
	    				getSupplyContractList_FCC();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL") || callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL_DLNG"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				couterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getCountContractBillingDetail();
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getContractBillingDetail();
	    				if(callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL")) 
	    				{
	    					getContractPlantWiseApplicableTaxes();
	    				}
	    				else if(callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL_DLNG")) 
	    				{
	    					getContractPlantWiseApplicableTaxesDlng();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_DCQ_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				couterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getContractDcqDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("NOM_CONTRACT_LIST"))
	    			{
	    				getNominationSupplyContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TCQ_MODIFICATION_REQUEST"))
	    			{
	    				String cont_typ="RLNG";
	    				getSegment();
	    				getTcqReqCustomerCounterpartyList(cont_typ);
	    				getTcqModificationRequest();
	    			}
	    			else if(callFlag.equalsIgnoreCase("AGMT_LIABILITY_CLAUSE"))
	    			{
	    				disp_agmt_no = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				couterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getAgmtLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONT_LIABILITY_CLAUSE"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				couterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getContLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONT_CLOSURE_REQUEST"))
	    			{
	    				getSegment();
	    				VSEGMENT.add("LTCORA (CN)");
	    				VSEGMENT_TYPE.add("O");
	    				VSEGMENT.add("LTCORA (Period)");
	    				VSEGMENT_TYPE.add("Q");
	    				String cont_closure_request="Closure";
	    				getClosureOrOpenReqCounterpartyList(cont_closure_request);
	    				getClosureOrReopenRequest(cont_closure_request);
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONT_REOPEN_REQUEST"))
	    			{
	    				if(segment.equals("0"))
	    				{
	    					getSegment();
	    					VSEGMENT.add("LTCORA (CN)");
		    				VSEGMENT_TYPE.add("O");
		    				VSEGMENT.add("LTCORA (Period)");
		    				VSEGMENT_TYPE.add("Q");
	    					String cont_closure_request="O";
	    					getClosureOrOpenReqCounterpartyList(cont_closure_request);
	    					getClosureOrReopenRequest(cont_closure_request);
	    				}
	    				else if(segment.equals("1"))
	    				{
	    					getAgreementSegment();
	    					getActivationAgreementCounterpartyList();
	    					getActivationAgreement();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_CONT_CLOSURE_REQUEST"))
	    			{
	    				getDlngSegment();
	    				String cont_closure_request="Closure";
	    				getClosureOrOpenReqCounterpartyList(cont_closure_request);
	    				getClosureOrReopenRequest(cont_closure_request);
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_CONT_REOPEN_REQUEST"))
	    			{
	    				getDlngSegment();
	    				String cont_closure_request="O";
    					getClosureOrOpenReqCounterpartyList(cont_closure_request);
    					getClosureOrReopenRequest(cont_closure_request);
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_TCQ_MODIFICATION_REQUEST"))
	    			{
	    				String cont_typ="DLNG";
	    				getDlngSegment();
	    				getTcqReqCustomerCounterpartyList(cont_typ);
	    				getTcqModificationRequest();
	    			}
	    			if(callFlag.equalsIgnoreCase("CONTRACT_DURATION_MODIFICATION"))
	    			{
	    				String cont_typ="RLNG";
	    				getCustomerList(cont_typ);
	    				fetchContractDurationChangeReq(cont_typ);
	    			}
	    			if(callFlag.equalsIgnoreCase("DLNG_CONTRACT_DURATION_MODIFICATION"))
	    			{
	    				String cont_typ="DLNG";
	    				getCustomerList(cont_typ);
	    				fetchContractDurationChangeReq(cont_typ);
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getCustomerList(String cont_typ)
	{
		String function_nm="getCustomerList()";

		try
		{
			int ctn=0;
			queryString = "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CHANGE_DATE_REQ=? AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			if(cont_typ.equals("RLNG"))
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, "L");
				stmt.setString(++ctn, "X");
			}
			else if(cont_typ.equals("DLNG"))
			{
				stmt.setString(++ctn, "E");
				stmt.setString(++ctn, "F");
				stmt.setString(++ctn, "W");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cust_cd = rset.getString(1)==null?"":rset.getString(1);
				String cust_nm = utilBean.getCounterpartyName(conn,cust_cd);
				
				VMST_COUNTERPARTY_CD.add(cust_cd);
				VMST_COUNTERPARTY_NM.add(cust_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}	
	}
	
	public void fetchContractDurationChangeReq(String cont_typ)
	{
		String function_nm="fetchContractDurationChangeReq()";

		try
		{
			int ctn=0;
			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,RATE, CHANGE_DATE_REQ, CHANGE_DATE_APPROVE, RATE_UNIT,CONTRACT_TYPE,"
					+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(CHANGE_START_DT,'DD/MM/YYYY'),TO_CHAR(CHANGE_END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE A.COUNTERPARTY_CD=? AND CHANGE_DATE_REQ=? AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) "
					+ "AND A.COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, counterparty_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			stmt.setString(++ctn, comp_cd);
			if(cont_typ.equals("RLNG"))
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, "L");
				stmt.setString(++ctn, "X");
			}
			else if(cont_typ.equals("DLNG"))
			{
				stmt.setString(++ctn, "E");
				stmt.setString(++ctn, "F");
				stmt.setString(++ctn, "W");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String fgsa=rset.getString(1)==null?"":rset.getString(1);
				String fgsa_rev=rset.getString(2)==null?"":rset.getString(2);
				String sn=rset.getString(3)==null?"":rset.getString(3);
				String sn_rev=rset.getString(4)==null?"":rset.getString(4);
				String signing_dt=rset.getString(5)==null?"":rset.getString(5);
				String start_dt=rset.getString(6)==null?"":rset.getString(6);
				String end_dt=rset.getString(7)==null?"":rset.getString(7);
				String rate_unit = rset.getString(12)==null?"2":rset.getString(12);
				
				String segment=cont_typ;
				String cont_type= rset.getString(13)==null?"":rset.getString(13);
				String cont_ref= rset.getString(14)==null?"":rset.getString(14);
				String trade_ref= rset.getString(15)==null?"":rset.getString(15);
				String change_start_dt= rset.getString(16)==null?"":rset.getString(16);
				String change_end_dt= rset.getString(17)==null?"":rset.getString(17);
				String cont_dtl=utilBean.NewDealMappingId(comp_cd, counterparty_cd, fgsa, fgsa_rev, sn, sn_rev, cont_type, "");
				if (cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}	
				VDEAL_MAPPING.add(cont_dtl);
				VCONT_REF_NO.add(cont_ref);
				VAGMT_NO.add(fgsa);
				VAGMT_REV_NO.add(fgsa_rev);
				VCONT_NO.add(sn);
				VCONT_REV_NO.add(sn_rev);
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				VSEGMENT.add(segment);
				VNEW_START_DT.add(change_start_dt);
				VNEW_END_DT.add(change_end_dt);
				
				String min_nom_dt="";
				String max_dt="";
				String queryString1="SELECT TO_CHAR(MIN(MIN_DATE),'DD/MM/YYYY') "
						+ "FROM (SELECT MIN(GAS_DT) MIN_DATE FROM FMS_DAILY_BUYER_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, fgsa);
				stmt1.setString(4, sn);
				stmt1.setString(5, cont_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					min_nom_dt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				String queryString2="SELECT TO_CHAR(MAX(MAX_DATE),'DD/MM/YYYY') "
						+ "FROM (SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DAILY_ALLOCATION_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "UNION ALL "
						+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG IN ('F') "
						+ "UNION ALL "
						+ "SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DAILY_TRANSPORTER_ALLOC "
						+ "WHERE COMPANY_CD=? AND SELL_CONT_MAP LIKE ? ";
				queryString2+="UNION ALL ";
				queryString2+="SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DLNG_ALLOC_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "UNION ALL "
						+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_DLNG_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG IN ('F'))";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, fgsa);
				stmt2.setString(4, sn);
				stmt2.setString(5, cont_type);
				stmt2.setString(6, comp_cd);
				stmt2.setString(7, counterparty_cd);
				stmt2.setString(8, fgsa);
				stmt2.setString(9, sn);
				stmt2.setString(10, cont_type);
				stmt2.setString(11, comp_cd);
				stmt2.setString(12, cont_map);
				stmt2.setString(13, comp_cd);
				stmt2.setString(14, counterparty_cd);
				stmt2.setString(15, fgsa);
				stmt2.setString(16, sn);
				stmt2.setString(17, cont_type);
				stmt2.setString(18, comp_cd);
				stmt2.setString(19, counterparty_cd);
				stmt2.setString(20, fgsa);
				stmt2.setString(21, sn);
				stmt2.setString(22, cont_type);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					max_dt=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();
				
				VMIN_NOM_DT.add(min_nom_dt);
				VMAX_DT.add(max_dt);
				VSIGNING_DT.add(signing_dt);
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
					+ "FROM FMS_AGMT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_PLANT B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_SUPPLY_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
					+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ) AND A.AGMT_NO=? "
					+ "AND A.CONTRACT_TYPE=?";
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
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
	
	public void getCurrentContractDtl() 
	{
		String function_nm="getCurrentContractDtl()";
		try
		{

			display_msg = "End date for Agreement can not be smaller than ongoing contract!!\n\n";
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE,"
					+ "(SELECT TO_CHAR(MAX(END_DT),'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO) "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					//+ "AND AGMT_TYPE=? "
					+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "ORDER BY END_DT DESC ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			//stmt.setString(2, agreement_type);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"0":rset.getString(6);
				String start_dt = rset.getString(7)==null?"0":rset.getString(7);
				String end_dt = rset.getString(8)==null?"0":rset.getString(8);
				String cont_type = rset.getString(13)==null?"":rset.getString(13);
				String agmt_type = rset.getString(14)==null?"":rset.getString(14);
				max_end_dt = rset.getString(15)==null?"":rset.getString(15);
				String cont_disp_name = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev, contno, cont_rev, cont_type, "");
				
				display_msg+=cont_disp_name+" ("+start_dt+"-"+end_dt+")\n";
			}
			rset.close();
			stmt.close();
		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getContLiabilityDetails() 
	{
		String function_nm="getContLiabilityDetails()";
		try
		{
			String queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,TO_CHAR(LQ_DAMG_FROM,'DD/MM/YYYY'),TO_CHAR(LQ_DAMG_TO,'DD/MM/YYYY'),TO_CHAR(TAKE_PAY_FROM,'DD/MM/YYYY'),TO_CHAR(TAKE_PAY_TO,'DD/MM/YYYY') "
					+ "FROM FMS_SUPPLY_CONT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				liab_lq_damg = rset.getString(1)==null?"":rset.getString(1);
				ld_price_per = rset.getString(2)==null?"":rset.getString(2);
				ld_promise = rset.getString(3)==null?"":rset.getString(3);
				ld_low_per = rset.getString(4)==null?"":rset.getString(4);
				ld_chk = rset.getString(5)==null?"":rset.getString(5);
				remark = rset.getString(6)==null?"":rset.getString(6);
				liab_take_pay = rset.getString(7)==null?"":rset.getString(7);
				top_price_per = rset.getString(8)==null?"":rset.getString(8);
				top_promise = rset.getString(9)==null?"":rset.getString(9);
				top_per = rset.getString(10)==null?"":rset.getString(10);
				top_chk = rset.getString(11)==null?"":rset.getString(11);
				top_obligation = rset.getString(12)==null?"":rset.getString(12);
				top_remark = rset.getString(14)==null?"":rset.getString(14);
				liab_makeup = rset.getString(15)==null?"":rset.getString(15);
				mug_price_per = rset.getString(16)==null?"":rset.getString(16);
				mug_period_per = rset.getString(17)==null?"":rset.getString(17);
				recovery_day = rset.getString(19)==null?"":rset.getString(19);
				mug_remark = rset.getString(20)==null?"":rset.getString(20);
				ld_from = rset.getString(21)==null?"":rset.getString(21);
				ld_to = rset.getString(22)==null?"":rset.getString(22);
				top_from = rset.getString(23)==null?"":rset.getString(23);
				top_to = rset.getString(24)==null?"":rset.getString(24);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getSelectedContLiabilityDtl() 
	{
		String function_nm="getSelectedContLiabilityDtl()";
		try
		{
			int liability_count=0;
			String queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_SUPPLY_CONT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				liability_count++;
				liability_lq_dmg = rset.getString(1)==null?"":rset.getString(1);
				liability_take_pay = rset.getString(2)==null?"":rset.getString(2);
				liability_makeup = rset.getString(3)==null?"":rset.getString(3);
				no_of_liability_dtl = ""+liability_count;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getSelectedAgmtLiabilityDtl() 
	{
		String function_nm="getSelectedAgmtLiabilityDtl()";
		try
		{
			int liability_count=0;
			String queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_SUPPLY_AGMT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_SUPPLY_AGMT_LIABILITY B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				liability_count++;
				liability_lq_dmg = rset.getString(1)==null?"":rset.getString(1);
				liability_take_pay = rset.getString(2)==null?"":rset.getString(2);
				liability_makeup = rset.getString(3)==null?"":rset.getString(3);
				no_of_liability_dtl = ""+liability_count;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getAgmtLiabilityDetails() 
	{
		String function_nm="getAgmtLiabilityDetails()";
		try
		{
			String queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK "
					+ "FROM FMS_SUPPLY_AGMT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_SUPPLY_AGMT_LIABILITY B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				liab_lq_damg = rset.getString(1)==null?"":rset.getString(1);
				ld_price_per = rset.getString(2)==null?"":rset.getString(2);
				ld_promise = rset.getString(3)==null?"":rset.getString(3);
				ld_low_per = rset.getString(4)==null?"":rset.getString(4);
				ld_chk = rset.getString(5)==null?"":rset.getString(5);
				remark = rset.getString(6)==null?"":rset.getString(6);
				liab_take_pay = rset.getString(7)==null?"":rset.getString(7);
				top_price_per = rset.getString(8)==null?"":rset.getString(8);
				top_promise = rset.getString(9)==null?"":rset.getString(9);
				top_per = rset.getString(10)==null?"":rset.getString(10);
				top_chk = rset.getString(11)==null?"":rset.getString(11);
				top_obligation = rset.getString(12)==null?"":rset.getString(12);
				top_remark = rset.getString(14)==null?"":rset.getString(14);
				liab_makeup = rset.getString(15)==null?"":rset.getString(15);
				mug_price_per = rset.getString(16)==null?"":rset.getString(16);
				mug_period_per = rset.getString(17)==null?"":rset.getString(17);
				recovery_day = rset.getString(19)==null?"":rset.getString(19);
				mug_remark = rset.getString(20)==null?"":rset.getString(20);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerCounterpartyList()
	{
		String function_nm="getCustomerCounterpartyList()";
		try
		{
			//utilBean.getEffectiveCustomerCounterpartyList(clearance,comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,clearance,comp_cd,"C");
			//utilBean.getAllEntityCounterpartyList(conn,clearance,comp_cd,"C");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerPlantList()
	{
		String function_nm="getCustomerPlantList()";
		try
		{
			if((contract_type.equals("S") && callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST")) || (contract_type.equals("F") && callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST")))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				if(contract_type.equals("S"))
				{
					stmt.setString(3, "F");
				}
				else if(contract_type.equals("F"))
				{
					stmt.setString(3, "D");
				}
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					
					VPLANT_NM.add(plant_abbr);
					VPLANT_ABBR.add(plant_abbr);
					VPLANT_SEQ_NO.add(plant_seq);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				//utilBean.getEffectiveCustomerPlantList(counterparty_cd, comp_cd);
				utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "C", comp_cd);
				VPLANT_NM=utilBean.getPLANT_NM();
				VPLANT_ABBR=utilBean.getPLANT_ABBR();
				VPLANT_SEQ_NO=utilBean.getPLANT_SEQ_NO();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTransporterPlantList()
	{
		String function_nm="getTransporterPlantList()";
		try
		{
			if(contract_type.equals("S") && callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST"))
			{
				String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_TRANSPTR A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "F");
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					String plant_seq = rset.getString(2)==null?"":rset.getString(2);
					VTRANS_CD.add(transCd);
					VTRANS_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, plant_seq, "R");
					VTRANS_PLANT_ABBR.add(plant_abbr);
					VTRANS_PLANT_NM.add(plant_abbr);
				}
				rset.close();
				stmt.close();
				
				String queryString1="SELECT DISTINCT TRANSPORTER_CD "
						+ "FROM FMS_AGMT_TRANSPTR A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, "F");
				stmt1.setString(4, agmt_no);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String trans_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String trans_abbr=utilBean.getCounterpartyABBR(conn,trans_cd);
					VTEMP_TRANS_CD.add(trans_cd);
					VTEMP_TRANS_ABBR.add(trans_abbr);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				utilBean.getEffectiveTransporterPlantList(conn,comp_cd);;
				VTRANS_CD=utilBean.getTRANS_CD();
				VTRANS_PLANT_NM=utilBean.getTRANS_PLANT_NM();
				VTRANS_PLANT_ABBR=utilBean.getTRANS_PLANT_ABBR();
				VTRANS_PLANT_SEQ_NO=utilBean.getTRANS_PLANT_SEQ_NO();
				
				String queryString="SELECT DISTINCT A.COUNTERPARTY_CD,A.COUNTERPARTY_ABBR "
						+ "FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND B.ENTITY=? AND B.STATUS='A' "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "R");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VTEMP_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VTEMP_TRANS_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
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

	public void getTruckTransporterList()
	{
		String function_nm="getTruckTransporterList()";
		try
		{
			if(contract_type.equals("F") && callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST"))
			{
				String queryString="SELECT TRANSPORTER_CD "
						+ "FROM FMS_AGMT_TRUCK_TRANS A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR,TRUCK_TRANS_NAME "
							+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_TRANS_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, transCd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String transAbbr = rset1.getString(2)==null?"":rset1.getString(2);
						String transName = rset1.getString(3)==null?"":rset1.getString(3);
						
						VTRUCK_TRANS_CD.add(transCd);
						VTRUCK_TRANS_ABBR.add(transAbbr);
						VTRUCK_TRANS_NM.add(transName);
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString1="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR,TRUCK_TRANS_NAME "
						+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, "Y");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String transCd = rset1.getString(1)==null?"":rset1.getString(1);
					String transAbbr = rset1.getString(2)==null?"":rset1.getString(2);
					String transName = rset1.getString(3)==null?"":rset1.getString(3);
					
					VTRUCK_TRANS_CD.add(transCd);
					VTRUCK_TRANS_ABBR.add(transAbbr);
					VTRUCK_TRANS_NM.add(transName);
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
	
	public void getBusinessPlantList()
	{
		String function_nm="getBusinessPlantList()";
		try
		{
			if(contract_type.equals("S") && callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST") || contract_type.equals("F") && callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				if(contract_type.equals("S") && callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST"))
				{
					stmt.setString(3, "F");
				}
				else if(contract_type.equals("F") && callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST"))
				{
					stmt.setString(3, "D");
				}
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
			else
			{
				utilBean.getEffectiveBusinessPlantList(conn,comp_cd);
				VBU_CD=utilBean.getBU_CD();
				VBU_PLANT_NM=utilBean.getBU_PLANT_NM();
				VBU_PLANT_ABBR=utilBean.getBU_PLANT_ABBR();
				VBU_PLANT_SEQ_NO=utilBean.getBU_PLANT_SEQ_NO();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getFillingStationList()
	{
		String function_nm="getFillingStationList()";
		try
		{
			if(contract_type.equals("F") && callFlag.equalsIgnoreCase("DLNG_CONTRACT_MST"))
			{
				String queryString="SELECT FILL_STATION_CD "
						+ "FROM FMS_AGMT_FILLING_STN "
						+ "WHERE COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = conn.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, agreement_type);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String fillStnCd = rset.getString(1)==null?"":rset.getString(1);

					String queryString1="SELECT FILL_STATION_ABBR "
							+ "FROM FMS_FILLING_STATION_MST A "
							+ "WHERE FILL_STATION_CD=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=?";
					stmt1 = conn.prepareStatement(queryString1);
					//stmt1.setString(1, comp_cd);
					stmt1.setString(1, fillStnCd);
					stmt1.setString(2, "Y");
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String fst_abbr = rset1.getString(1)==null?"":rset1.getString(1);
						
						VFILL_STATION_CD.add(fillStnCd);
						VFILL_STATION_ABBR.add(fst_abbr);
					}
					rset1.close();
					stmt1.close();
					
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT FILL_STATION_CD,FILL_STATION_ABBR "
						+ "FROM FMS_FILLING_STATION_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=?";
				stmt = conn.prepareStatement(queryString);
				//stmt.setString(1, comp_cd);
				//stmt.setString(2, counterparty_cd);
				//stmt.setString(3, "C");
				stmt.setString(1, "Y");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String fst_cd = rset.getString(1)==null?"":rset.getString(1);
					String fst_abbr = rset.getString(2)==null?"":rset.getString(2);
					
					VFILL_STATION_CD.add(fst_cd);
					VFILL_STATION_ABBR.add(fst_abbr);
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
	
	public void getAgreementDetail()
	{
		String function_nm="getAgreementDetail()";
		try
		{
			dealMapping=utilBean.NewAgmtMappingId(comp_cd,counterparty_cd, agmt_no, agmt_rev_no,agreement_type);
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(RENEWAL_DT,'DD/MM/YYYY'),"
					+ "AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
					+ "DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,AGMT_REF_NO,BILLING_FLAG,TO_CHAR(REV_DT,'DD/MM/YYYY'),"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEASUREMENT,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_CLAUSE,TERMINATE_FLAG,"
					+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEAS_CLAUSE,SPEC_CLAUSE, "
					+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'D' END, REOPEN_REQUEST_FLAG,REOPEN_APPROVAL_FLAG "
					+ "FROM FMS_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_signing_dt = rset.getString(3)==null?"":rset.getString(3);
				signing_dt=rset.getString(3)==null?"":rset.getString(3);
				
				agmt_start_dt=rset.getString(4)==null?"":rset.getString(4);
				start_dt=rset.getString(4)==null?"":rset.getString(4);
				agmt_end_dt=rset.getString(5)==null?"":rset.getString(5);
				end_dt=rset.getString(5)==null?"":rset.getString(5);
				renewal_dt=rset.getString(6)==null?"":rset.getString(6);
				agmt_base=rset.getString(7)==null?"":rset.getString(7);
				agmt_type=rset.getString(8)==null?"":rset.getString(8);
				status=rset.getString(9)==null?"":rset.getString(9);
				status_nm=""+AgmtStatusName(status);
				buy_nom_clause=rset.getString(10)==null?"":rset.getString(10);
				buy_nom_flag=rset.getString(11)==null?"":rset.getString(11);
				buy_month_nom=rset.getString(12)==null?"":rset.getString(12);
				buy_week_nom=rset.getString(13)==null?"":rset.getString(13);
				buy_daily_nom=rset.getString(14)==null?"":rset.getString(14);
				sell_nom_clause=rset.getString(15)==null?"":rset.getString(15);
				sell_nom_flag=rset.getString(16)==null?"":rset.getString(16);
				sell_month_nom=rset.getString(17)==null?"":rset.getString(17);
				sell_week_nom=rset.getString(18)==null?"":rset.getString(18);
				sell_daily_nom=rset.getString(19)==null?"":rset.getString(19);
				day_def_flag=rset.getString(20)==null?"":rset.getString(20);
				day_start_time=rset.getString(21)==null?"":rset.getString(21);
				day_end_time=rset.getString(22)==null?"":rset.getString(22);
				mdcq_flag=rset.getString(23)==null?"":rset.getString(23);
				mdcq_percentage=rset.getString(24)==null?"":rset.getString(24);
				remark=rset.getString(25)==null?"":rset.getString(25);
				String deal_ent_dt=rset.getString(26)==null?"":rset.getString(26);
				cont_name=rset.getString(27)==null?"":rset.getString(27);
				cont_ref_no=rset.getString(28)==null?"":rset.getString(28);
				bill_flag=rset.getString(29)==null?"":rset.getString(29);
				rev_dt=rset.getString(30)==null?"":rset.getString(30);
				
				buy_fortnightly_nom=rset.getString(31)==null?"":rset.getString(31);
				sell_fortnightly_nom=rset.getString(32)==null?"":rset.getString(32);
				buy_nom_cutoff_time=rset.getString(33)==null?"":rset.getString(33);
				
				messurment_flag = rset.getString(34)==null?"":rset.getString(34); 
				meas_std =rset.getString(35)==null?"":rset.getString(35);
				meas_temp = rset.getString(36)==null?"":rset.getString(36);
				pressure_min_bar = rset.getString(37)==null?"":rset.getString(37);
				pressure_max_bar = rset.getString(38)==null?"":rset.getString(38);
				off_spec_gas_flag = rset.getString(39)==null?"":rset.getString(39);
				spec_gas_eng_base = rset.getString(40)==null?"":rset.getString(40);
				spec_min_eng = rset.getString(41)==null?"":rset.getString(41);
				spec_max_eng = rset.getString(42)==null?"":rset.getString(42);
				liability_flag = rset.getString(43)==null?"":rset.getString(43);
				liability_clause = rset.getString(44)==null?"":rset.getString(44);
				billing_clause = rset.getString(45)==null?"":rset.getString(45);
				termination_flag= rset.getString(46)==null?"":rset.getString(46);
				termination_clause = rset.getString(47)==null?"":rset.getString(47);
				termination_planned = rset.getString(48)==null?"":rset.getString(48);
				termination_forced = rset.getString(49)==null?"":rset.getString(49);
				meas_clause = rset.getString(50)==null?"":rset.getString(50);
				spec_clause = rset.getString(51)==null?"":rset.getString(51);
				
				is_expired =rset.getString(52)==null?"":rset.getString(52);
				reopen_request_flg=rset.getString(53)==null?"":rset.getString(53);
				reopen_approval_flag=rset.getString(54)==null?"":rset.getString(54);
				
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedAgreementDetail()
	{
		String function_nm="getSelectedAgreementDetail()";
		try
		{
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(RENEWAL_DT,'DD/MM/YYYY'),"
					+ "AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
					+ "DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,AGMT_REF_NO,BILLING_FLAG,TO_CHAR(REV_DT,'DD/MM/YYYY'),"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEASUREMENT,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_CLAUSE,TERMINATE_FLAG,"
					+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEAS_CLAUSE,SPEC_CLAUSE "
					+ "FROM FMS_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_signing_dt = rset.getString(3)==null?"":rset.getString(3);
				agmt_start_dt=rset.getString(4)==null?"":rset.getString(4);
				agmt_end_dt=rset.getString(5)==null?"":rset.getString(5);
				agmt_base=rset.getString(7)==null?"":rset.getString(7);
				agmt_type=rset.getString(8)==null?"":rset.getString(8);
				buy_nom_clause=rset.getString(10)==null?"":rset.getString(10);
				buy_nom_flag=rset.getString(11)==null?"":rset.getString(11);
				buy_month_nom=rset.getString(12)==null?"":rset.getString(12);
				buy_week_nom=rset.getString(13)==null?"":rset.getString(13);
				buy_daily_nom=rset.getString(14)==null?"":rset.getString(14);
				sell_nom_clause=rset.getString(15)==null?"":rset.getString(15);
				sell_nom_flag=rset.getString(16)==null?"":rset.getString(16);
				sell_month_nom=rset.getString(17)==null?"":rset.getString(17);
				sell_week_nom=rset.getString(18)==null?"":rset.getString(18);
				sell_daily_nom=rset.getString(19)==null?"":rset.getString(19);
				day_def_flag=rset.getString(20)==null?"":rset.getString(20);
				day_start_time=rset.getString(21)==null?"":rset.getString(21);
				day_end_time=rset.getString(22)==null?"":rset.getString(22);
				mdcq_flag=rset.getString(23)==null?"":rset.getString(23);
				mdcq_percentage=rset.getString(24)==null?"":rset.getString(24);
				remark=rset.getString(25)==null?"":rset.getString(25);
				String deal_ent_dt=rset.getString(26)==null?"":rset.getString(26);
				cont_name=rset.getString(27)==null?"":rset.getString(27);
				bill_flag=rset.getString(29)==null?"":rset.getString(29);
				
				buy_fortnightly_nom=rset.getString(31)==null?"":rset.getString(31);
				sell_fortnightly_nom=rset.getString(32)==null?"":rset.getString(32);
				buy_nom_cutoff_time=rset.getString(33)==null?"":rset.getString(33);
				
				messurment_flag = rset.getString(34)==null?"":rset.getString(34); 
				meas_std =rset.getString(35)==null?"":rset.getString(35);
				meas_temp = rset.getString(36)==null?"":rset.getString(36);
				pressure_min_bar = rset.getString(37)==null?"":rset.getString(37);
				pressure_max_bar = rset.getString(38)==null?"":rset.getString(38);
				off_spec_gas_flag = rset.getString(39)==null?"":rset.getString(39);
				spec_gas_eng_base = rset.getString(40)==null?"":rset.getString(40);
				spec_min_eng = rset.getString(41)==null?"":rset.getString(41);
				spec_max_eng = rset.getString(42)==null?"":rset.getString(42);
				liability_flag = rset.getString(43)==null?"":rset.getString(43);
				liability_clause = rset.getString(44)==null?"":rset.getString(44);
				billing_clause = rset.getString(45)==null?"":rset.getString(45);
				termination_flag= rset.getString(46)==null?"":rset.getString(46);
				termination_clause = rset.getString(47)==null?"":rset.getString(47);
				termination_planned = rset.getString(48)==null?"":rset.getString(48);
				termination_forced = rset.getString(49)==null?"":rset.getString(49);
				meas_clause = rset.getString(50)==null?"":rset.getString(50);
				spec_clause = rset.getString(51)==null?"":rset.getString(51);
			}
			rset.close();
			stmt.close();			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedCustomerPlantList()
	{
		String function_nm="getSelectedCustomerPlantList()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_PLANT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
				VSEL_TRAD_CD.add(counterparty_cd);
				VSEL_PLANT_SEQ_NO.add(plant_seq);
				VSEL_PLANT_ABBR.add(plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedTransporterPlantList()
	{
		String function_nm="getSelectedTransporterPlantList()";
		try
		{
			String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_TRANSPTR "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transCd = rset.getString(1)==null?"":rset.getString(1);
				String plant_seq = rset.getString(2)==null?"":rset.getString(2);
				VSEL_TRANS_CD.add(transCd);
				VSEL_TRANS_PLANT_SEQ_NO.add(plant_seq);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, plant_seq, "R");
				VSEL_TRANS_PLANT_ABBR.add(plant_abbr);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSelectedFillStationList()
	{
		String function_nm="getSelectedFillStationList()";
		try
		{
			String queryString="SELECT FILL_STATION_CD "
					+ "FROM FMS_AGMT_FILLING_STN "
					+ "WHERE COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, agreement_type);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String fillStnCd = rset.getString(1)==null?"":rset.getString(1);

				String queryString1="SELECT FILL_STATION_ABBR "
						+ "FROM FMS_FILLING_STATION_MST A "
						+ "WHERE FILL_STATION_CD=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=?";
				stmt1 = conn.prepareStatement(queryString1);
				//stmt1.setString(1, comp_cd);
				stmt1.setString(1, fillStnCd);
				stmt1.setString(2, "Y");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String fst_abbr = rset1.getString(1)==null?"":rset1.getString(1);
					
					VSEL_FILL_STATION_CD.add(fillStnCd);
					VSEL_FILL_STATION_ABBR.add(fst_abbr);
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

	public void getSelectedTruckTransporterList()
	{
		String function_nm="getSelectedTruckTransporterList()";
		try
		{
			String queryString="SELECT TRANSPORTER_CD "
					+ "FROM FMS_AGMT_TRUCK_TRANS "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transCd = rset.getString(1)==null?"":rset.getString(1);
				
				String queryString1="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR "
						+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_TRANS_CD=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, "Y");
				stmt1.setString(2, transCd);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String transAbbr = rset1.getString(2)==null?"":rset1.getString(2);
					
					VSEL_TRUCK_TRANS_CD.add(transCd);
					VSEL_TRUCK_TRANS_ABBR.add(transAbbr);
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";
		try
		{
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
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
	
	public void getExchangeRateMaster()
	{
		String function_nm="getExchangeRateMaster()";
		try
		{
			
			String queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY EXC_RATE_NM";
			stmt = conn.prepareStatement(queryString);
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
			
			String queryString = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
					+ "FROM FMS_INT_RATE_MST "
					+ "WHERE FLAG=? "
					+ "ORDER BY INT_RATE_NM";
			stmt = conn.prepareStatement(queryString);
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
	
	public String AgmtStatusName(String status_flg)
	{
		String function_nm="AgmtStatusName()";
		String nm="";
		try
		{
			if(status_flg.equals("A"))
			{
				nm="Active";
			}
			else if(status_flg.equals("D"))
			{
				nm="Deactive";
			}
			else if(status_flg.equals("C"))
			{
				nm="Closed";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getAgreementList()
	{
		String function_nm="getAgreementList()";
		try
		{
			int stcount=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,STATUS,AGMT_REF_NO,"
					+ "AGMT_BASE,AGMT_TYP, "
					+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'N' END IS_EXPIRED "
					+ "FROM FMS_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,STATUS,AGMT_REF_NO,"
						+ "AGMT_BASE,AGMT_TYP, "
						+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'N' END IS_EXPIRED "
						+ "FROM FMS_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
				queryString += "AND END_DT >= SYSDATE AND STATUS NOT IN ('A') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND STATUS IN ('A') ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++stcount, comp_cd);
			stmt.setString(++stcount, agreement_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++stcount, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++stcount, to_dt);
				stmt.setString(++stcount, from_dt);
				
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, agreement_type);
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++stcount, counterparty_cd);
				}
				stmt.setString(++stcount, to_dt);
				stmt.setString(++stcount, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtNo = rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"0":rset.getString(4);
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtNo);
				VAGMT_REV_NO.add(agmt_rev);
				VSTART_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VEND_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VCONT_NAME.add(rset.getString(7)==null?"":rset.getString(7));
				String cont_status_flg=rset.getString(8)==null?"":rset.getString(8);
				VEXPIRED.add(rset.getString(12)==null?"":rset.getString(12));
				
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+AgmtStatusName(cont_status_flg));
				VCONT_REF_NO.add(rset.getString(9)==null?"":rset.getString(9));
				
				String agmt_disp_no = utilBean.NewAgmtMappingId(own_cd, countpty_cd, agmtNo, agmt_rev, agreement_type);
				VDISP_AGMT_NO.add(agmt_disp_no);
				
				String agmtBase =rset.getString(10)==null?"":rset.getString(10);
				String agmtType =rset.getString(11)==null?"":rset.getString(11);
				
				if(agmtBase.equals("X"))
				{
					VAGMT_BASE.add("Ex-Terminal");
				}
				else if(agmtBase.equals("D"))
				{
					VAGMT_BASE.add("Delivery");
				}
				else if(agmtBase.equals("B"))
				{
					VAGMT_BASE.add("Both(Ex-Terminal/Delivery)");
				}
				else
				{
					VAGMT_BASE.add("");
				}
				
				if(agmtType.equals("0"))
				{
					VAGMT_TYPE.add("Term");
				}
				else if(agmtType.equals("1"))
				{
					VAGMT_TYPE.add("Spot");
				}
				else
				{
					VAGMT_TYPE.add("");
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
	
	public void getBillingDetail()
	{
		String function_nm="getBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B WHERE "
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
					String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,"
							+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B WHERE "
							+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agreement_type);
					stmt.setString(4, agmt_no);
					stmt.setString(5, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					//stmt.setString(5, agmt_rev_no);
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
						sat_days=rset.getString(18)==null?"":rset.getString(18);
						plant_seq=rset.getString(19)==null?"":rset.getString(19);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B WHERE "
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
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
							+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
						
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
					+ "FROM FMS_AGMT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, agreement_type);
			stmt2.setString(4, agmt_no);
			stmt2.setString(5, agmt_rev_no);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT TAX_STRUCT_CD,TAX_STRUCT_DTL "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
							+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "C");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C"));
						VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VINVOICE_CATEGORY.add("Tax/Retail Invoice");			//Pratham Bhatt 20240822: for invoice category
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("C","P","S"));	//Pratham Bhatt 20240822: for invoice type
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
	
	public void getContractPlantWiseApplicableTaxesDlng()
	{
		String function_nm="getContractPlantWiseApplicableTaxesDlng()";
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
					+ "FROM FMS_SUPPLY_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, cont_no);
			stmt2.setString(4, cont_rev_no);
			stmt2.setString(5, agmt_no);
			stmt2.setString(6, agmt_rev_no);
			stmt2.setString(7, contract_type);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					
					VTAX_PLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C"));
					VTAX_PLANT_SEQ.add(plant_seq);
					VTAX_BU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VTAX_BU_PLANT_SEQ.add(bu_plant_seq);
					VINVOICE_CATEGORY.add("Tax/Retail Invoice");			//Pratham Bhatt20240822: for invoice category
					VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("C","P","S"));	//Pratham Bhatt20240822: for invoice type 
					
					String queryString3="SELECT CFORM_FLAG "
							+ "FROM FMS_SUPPLY_CFORM_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_SEQ=? "
							+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CFORM_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.BU_SEQ=B.BU_SEQ)";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, cont_rev_no);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmt_rev_no);
					stmt3.setString(7, contract_type);
					stmt3.setString(8, plant_seq);
					stmt3.setString(9, bu_plant_seq);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						VCFORM_FLAG.add(rset3.getString(1)==null?"":rset3.getString(1));
					}
					else
					{
						VCFORM_FLAG.add("0");
					}
					rset3.close();
					stmt3.close();
					
					String queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A,FMS_TAX_STRUCTURE B "
							+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? "
							+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "C");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					}
					else
					{
						VTAX_STRUCT_CD.add("-");
						VTAX_STRUCT_NM.add("-");
						VTAX_SAP_CODE.add("-");
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
					+ "FROM FMS_SUPPLY_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, cont_no);
			stmt2.setString(4, cont_rev_no);
			stmt2.setString(5, agmt_no);
			stmt2.setString(6, agmt_rev_no);
			stmt2.setString(7, contract_type);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					
					VTAX_PLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C"));
					VTAX_PLANT_SEQ.add(plant_seq);
					VTAX_BU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VTAX_BU_PLANT_SEQ.add(bu_plant_seq);
					VINVOICE_CATEGORY.add("Tax/Retail Invoice");			//Pratham Bhatt20240822: for invoice category
					VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("C","P","S"));	//Pratham Bhatt20240822: for invoice type 
					
					String queryString3="SELECT CFORM_FLAG "
							+ "FROM FMS_SUPPLY_CFORM_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_SEQ=? "
							+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CFORM_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.BU_SEQ=B.BU_SEQ)";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, cont_rev_no);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmt_rev_no);
					stmt3.setString(7, contract_type);
					stmt3.setString(8, plant_seq);
					stmt3.setString(9, bu_plant_seq);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						VCFORM_FLAG.add(rset3.getString(1)==null?"":rset3.getString(1));
					}
					else
					{
						VCFORM_FLAG.add("0");
					}
					rset3.close();
					stmt3.close();
					
					String queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A,FMS_TAX_STRUCTURE B "
							+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? "
							+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "C");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					}
					else
					{
						VTAX_STRUCT_CD.add("-");
						VTAX_STRUCT_NM.add("-");
						VTAX_SAP_CODE.add("-");
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

	public void getContractBillingDetail()
	{
		String function_nm="getContractBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, cont_no);
			stmt3.setString(5, contract_type);
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
					String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
							+ "BILLING_DAYS,EXCHG_VAL,TO_CHAR(EFF_DT,'DD/MM/YYYY'),EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_SUPPLY_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO)";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, ""+VSELECTED_PLANT_SEQ.elementAt(k));
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
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_SUPPLY_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, plant_seq);
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
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
						
						String queryString1="";
						
						if(callFlag.equalsIgnoreCase("CONTRACT_BILLING_DTL_DLNG")) 
						{
							queryString1="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
									+ "FROM FMS_DLNG_INVOICE_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
						}
						else
						{
							queryString1="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
									+ "FROM FMS_INVOICE_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
						}
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, agmt_no);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, contract_type);
						stmt1.setString(6, "F");
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
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
				if(contract_type.equals("S") || contract_type.equals("F"))
				{
					int count1=0;
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					if(contract_type.equals("S")) 
					{
						stmt3.setString(3, "F");
					}
					else if(contract_type.equals("F"))
					{
						stmt3.setString(3, "D");
					}
					stmt3.setString(4, agmt_no);
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
							String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
									+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,"
									+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO "
									+ "FROM FMS_AGMT_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_TYPE=? AND AGMT_NO=? AND PLANT_SEQ_NO=? "
									+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B "
									+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							if(contract_type.equals("S")) 
							{
								stmt.setString(3, "F");
							}
							else if(contract_type.equals("F"))
							{
								stmt.setString(3, "D");
							}
							stmt.setString(4, agmt_no);
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
								sat_days=rset.getString(18)==null?"N":rset.getString(18);
								plant_seq=rset.getString(19)==null?"":rset.getString(19);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
								
								String state_map="";
								String state_nm = "";
								String queryString2="SELECT HOLIDAY_STATE "
										+ "FROM FMS_AGMT_BILLING_DTL A "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
										+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_BILLING_DTL B WHERE "
										+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, counterparty_cd);
								stmt2.setString(3, agmt_no);
								if(contract_type.equals("S")) 
								{
									stmt2.setString(4, "F");
								}
								else if(contract_type.equals("F"))
								{
									stmt2.setString(4, "D");
								}
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
												+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
												+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
												+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
												+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
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
											plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
											plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
										+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
									
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
									+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
									+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
								
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
				else
				{
					for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='C' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
	
	public void getSupplyContractDetail()
	{
		String function_nm="getSupplyContractDetail()";
		try
		{
			contract_offset_qty=nf.format(utilBean.getContractMigrationOffSetQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, "S"));
			dealMapping=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
					+ "POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,FCC_FLAG,CONT_STATUS,IS_ALLOCATED,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
					+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_REQUEST_FLAG,TCQ_REQUEST_QTY,TCQ_SIGN,TCQ_REQUEST_CLOSE,"
					+ "PRICE_REQUEST_FLAG,CHANGE_DATE_REQ, "
					+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE, TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'),CLOSURE_REQUEST_FLAG, "
					+ "CASE WHEN TO_DATE(END_DT,'DD/MM/YYYY')<TO_DATE(SYSDATE,'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRE, "
					+ "CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					//+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			//stmt.setString(4, cont_rev_no);
			stmt.setString(4, contract_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				cont_no=rset.getString(3)==null?"":rset.getString(3);
				cont_rev_no=rset.getString(4)==null?"":rset.getString(4);
				cont_ref_no=rset.getString(5)==null?"":rset.getString(5);
				trade_ref_no=rset.getString(6)==null?"":rset.getString(6);
				signing_dt=rset.getString(7)==null?"":rset.getString(7);
				signing_time=rset.getString(8)==null?"":rset.getString(8);
				start_dt=rset.getString(9)==null?"":rset.getString(9);
				end_dt=rset.getString(10)==null?"":rset.getString(10);
				agmt_base=rset.getString(11)==null?"":rset.getString(11);
				agmt_type=rset.getString(12)==null?"":rset.getString(12);
				tcq=nf.format(rset.getDouble(13));
				dcq=nf.format(rset.getDouble(14));
				quantity_unit=rset.getString(15)==null?"1":rset.getString(15);
				rate_unit=rset.getString(17)==null?"1":rset.getString(17);
				if(rate_unit.equals("1"))
				{
					rate=nf.format(rset.getDouble(16));
				}
				else
				{
					rate=nf2.format(rset.getDouble(16));
				}
				
				post_margin=rset.getString(18)==null?"":rset.getString(18);
				transportation_charges=rset.getString(19)==null?"":rset.getString(19);
				buy_nom_flag=rset.getString(20)==null?"":rset.getString(20);
				buy_month_nom=rset.getString(21)==null?"":rset.getString(21);
				buy_week_nom=rset.getString(22)==null?"":rset.getString(22);
				buy_daily_nom=rset.getString(23)==null?"":rset.getString(23);
				sell_nom_flag=rset.getString(24)==null?"":rset.getString(24);
				sell_month_nom=rset.getString(25)==null?"":rset.getString(25);
				sell_week_nom=rset.getString(26)==null?"":rset.getString(26);
				sell_daily_nom=rset.getString(27)==null?"":rset.getString(27);
				day_def_flag=rset.getString(28)==null?"":rset.getString(28);
				day_start_time=rset.getString(29)==null?"":rset.getString(29);
				day_end_time=rset.getString(30)==null?"":rset.getString(30);
				mdcq_flag=rset.getString(31)==null?"":rset.getString(31);
				mdcq_percentage=rset.getString(32)==null?"":rset.getString(32);
				remark=rset.getString(33)==null?"":rset.getString(33);
				String deal_ent_dt=rset.getString(34)==null?"":rset.getString(34);
				cont_name=rset.getString(35)==null?"":rset.getString(35);
				fcc_flg=rset.getString(36)==null?"":rset.getString(36);
				cont_status_flg=rset.getString(37)==null?"":rset.getString(37);
				cont_status=""+ContStatusName(cont_status_flg);
				is_allocated=rset.getString(38)==null?"N":rset.getString(38);
				
				buy_fortnightly_nom=rset.getString(39)==null?"":rset.getString(39);
				sell_fortnightly_nom=rset.getString(40)==null?"":rset.getString(40);
				dda_dt=rset.getString(41)==null?"":rset.getString(41);
				dda_time=rset.getString(42)==null?"":rset.getString(42);
				
				buy_nom_cutoff_time=rset.getString(44)==null?"":rset.getString(44);
				
				txn_unit=rset.getString(45)==null?"1":rset.getString(45);
				tcq_request_flag=rset.getString(46)==null?"":rset.getString(46);
				String tcq_chg_qty=rset.getString(47)==null?"":rset.getString(47);
				if(!tcq_chg_qty.equals(""))
				{
					var_tcq=nf.format(rset.getDouble(47));
				}
				tcq_sign=rset.getString(48)==null?"":rset.getString(48);
				String tcq_req_close=rset.getString(49)==null?"":rset.getString(49);
				if(tcq_req_close.equals("Y"))
				{
					var_tcq="";
					tcq_sign="";
				}
				
				if(txn_unit.equals("1"))
				{
					txn_charges=nf.format(rset.getDouble(43));
				}
				else
				{
					txn_charges=nf2.format(rset.getDouble(43));
				}
				
				price_change_request_flag=rset.getString(50)==null?"":rset.getString(50);
				contdt_change_request_flag=rset.getString(51)==null?"":rset.getString(51);
				
				messurment_flag = rset.getString(52)==null?"":rset.getString(52); 
				meas_std =rset.getString(53)==null?"":rset.getString(53);
				meas_temp = rset.getString(54)==null?"":rset.getString(54);
				pressure_min_bar = rset.getString(55)==null?"":rset.getString(55);
				pressure_max_bar = rset.getString(56)==null?"":rset.getString(56);
				off_spec_gas_flag = rset.getString(57)==null?"":rset.getString(57);
				spec_gas_eng_base = rset.getString(58)==null?"":rset.getString(58);
				spec_min_eng = rset.getString(59)==null?"":rset.getString(59);
				spec_max_eng = rset.getString(60)==null?"":rset.getString(60);
				liability_flag = rset.getString(61)==null?"":rset.getString(61);
				liability_clause = rset.getString(62)==null?"":rset.getString(62);
				billing_flag = rset.getString(63)==null?"":rset.getString(63);
				billing_clause = rset.getString(64)==null?"":rset.getString(64);
				termination_flag= rset.getString(65)==null?"":rset.getString(65);
				termination_clause = rset.getString(66)==null?"":rset.getString(66);
				termination_planned = rset.getString(67)==null?"":rset.getString(67);
				termination_forced = rset.getString(68)==null?"":rset.getString(68);
				meas_clause = rset.getString(69)==null?"":rset.getString(69);
				spec_clause = rset.getString(70)==null?"":rset.getString(70);
				closure_eff_dt = rset.getString(71)==null?"":rset.getString(71);
				closure_request_flag = rset.getString(72)==null?"":rset.getString(72);
				is_expired = rset.getString(73)==null?"":rset.getString(73);
				balance_qty=rset.getString(74)==null?"":rset.getString(74);
				closure_note=rset.getString(75)==null?"":rset.getString(75);
				adv_adjust=rset.getString(76)==null?"":rset.getString(76);
				
				/*
			 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
				CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
				CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
				CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
				CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
				*/
				
				if(!balance_qty.equals(""))
				{
					if(Double.parseDouble(balance_qty)<0)
					{
						balance_qty=""+(Double.parseDouble(balance_qty)*-1);
						delta_tcq_sign="-";
					}
					else
					{
						delta_tcq_sign="+";
					}
				}
				else
				{
					delta_tcq_sign="";
				}
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
			}
			else
			{
				if(contract_type.equals("S"))
				{
					String queryString1="SELECT AGMT_BASE,AGMT_TYPE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
							+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ_PERCENTAGE,"
							+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEASUREMENT,"
							+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
							+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
							+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,"
							+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,MEAS_CLAUSE,SPEC_CLAUSE "
							+ "FROM FMS_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=?";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "F");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmt_base=rset1.getString(1)==null?"":rset1.getString(1);
						buy_nom_flag=rset1.getString(3)==null?"":rset1.getString(3);
						buy_month_nom=rset1.getString(4)==null?"":rset1.getString(4);
						buy_week_nom=rset1.getString(5)==null?"":rset1.getString(5);
						buy_daily_nom=rset1.getString(6)==null?"":rset1.getString(6);
						sell_nom_flag=rset1.getString(7)==null?"":rset1.getString(7);
						sell_month_nom=rset1.getString(8)==null?"":rset1.getString(8);
						sell_week_nom=rset1.getString(9)==null?"":rset1.getString(9);
						sell_daily_nom=rset1.getString(10)==null?"":rset1.getString(10);
						day_def_flag=rset1.getString(11)==null?"":rset1.getString(11);
						day_start_time=rset1.getString(12)==null?"":rset1.getString(12);
						day_end_time=rset1.getString(13)==null?"":rset1.getString(13);
						mdcq_percentage=rset1.getString(14)==null?"":rset1.getString(14);
						
						buy_fortnightly_nom=rset1.getString(15)==null?"":rset1.getString(15);
						sell_fortnightly_nom=rset1.getString(16)==null?"":rset1.getString(16);
						buy_nom_cutoff_time=rset1.getString(17)==null?"":rset1.getString(17);
						
						messurment_flag = rset1.getString(18)==null?"":rset1.getString(18); 
						meas_std =rset1.getString(19)==null?"":rset1.getString(19);
						meas_temp = rset1.getString(20)==null?"":rset1.getString(20);
						pressure_min_bar = rset1.getString(21)==null?"":rset1.getString(21);
						pressure_max_bar = rset1.getString(22)==null?"":rset1.getString(22);
						off_spec_gas_flag = rset1.getString(23)==null?"":rset1.getString(23);
						spec_gas_eng_base = rset1.getString(24)==null?"":rset1.getString(24);
						spec_min_eng = rset1.getString(25)==null?"":rset1.getString(25);
						spec_max_eng = rset1.getString(26)==null?"":rset1.getString(26);
						liability_flag = rset1.getString(27)==null?"":rset1.getString(27);
						liability_clause = rset1.getString(28)==null?"":rset1.getString(28);
						billing_flag = rset1.getString(29)==null?"":rset1.getString(29);
						billing_clause = rset1.getString(30)==null?"":rset1.getString(30);
						termination_flag= rset1.getString(31)==null?"":rset1.getString(31);
						termination_clause = rset1.getString(32)==null?"":rset1.getString(32);
						termination_planned = rset1.getString(33)==null?"":rset1.getString(33);
						termination_forced = rset1.getString(34)==null?"":rset1.getString(34);
						meas_clause = rset1.getString(35)==null?"":rset1.getString(35);
						spec_clause = rset1.getString(36)==null?"":rset1.getString(36);
					}
					rset1.close();
					stmt1.close();
				}
			}
			rset.close();
			stmt.close();
			
			supplied_qty=allocUtil.getBestSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type,start_dt,end_dt,agmt_base,"0");
			
			/*if(cont_status_flg.equals("R"))
			{
				supplied_qty="";
			}*/
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAgmtBaseFormAgmt()
	{
		String function_nm="getAgmtBaseFormAgmt()";
		try
		{
			String queryString="SELECT AGMT_BASE "
					+ "FROM FMS_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_agmt_base=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String ContStatusName(String status_flg)
	{
		String function_nm="ContStatusName()";
		String nm="";
		try
		{
			if(status_flg.equals("F"))
			{
				nm="New";
			}
			else if(status_flg.equals("P"))
			{
				nm="Pending Approval";
			}
			else if(status_flg.equals("Y"))
			{
				nm="Approved";
			}
			else if(status_flg.equals("N"))
			{
				nm="Not Approved";
			}
			else if(status_flg.equals("X"))
			{
				nm="Canceled";
			}
			else if(status_flg.equals("C"))
			{
				nm="Closed";
			}
			else if(status_flg.equals("R"))
			{
				//nm="Re-Open Requested";
				nm="Re-Opened";
			}
			else if(status_flg.equals("T"))
			{
				nm="Terminated";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getContractSelectedCustomerPlantList()
	{
		String function_nm="getContractSelectedCustomerPlantList()";
		try
		{
			if((contract_type.equals("S") || contract_type.equals("F")) && opration.equals("INSERT"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				if(contract_type.equals("S"))
				{
					stmt.setString(3, "F");
				}
				else if(contract_type.equals("F"))
				{
					stmt.setString(3, "D");
				}
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					VSEL_TRAD_CD.add(counterparty_cd);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					VSEL_PLANT_ABBR.add(plant_abbr);
					//VSEL_TRANS_CHRG.add("");
					//VSEL_MARKET_MARGIN.add("");
					//VSEL_OTH_CHRG.add("");
					
					String margeChrgVal="";
					String mergeChargDesc="";
					
					for(int i=0; i<VCHARGE_ABBR.size();i++)
					{
						String effdt="";
						String rate="";
						
						/*String hist_effdt="";
						String hist_rate="";
						
						mergeChargDesc += ""+VCHARGE_NAME.elementAt(i);
						mergeChargDesc += "~ Rate : "+hist_rate+" Effective "+hist_effdt;
						mergeChargDesc+="~";*/
						
						margeChrgVal+=!margeChrgVal.equals("")?"$$"+rate+"#"+effdt:rate+"#"+effdt;
					}
					VSEL_CHARGE_VALUE.add(margeChrgVal);
					VSEL_CHARGE_DESC.add(mergeChargDesc);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					
					//String trans_chrg = rset.getString(2)==null?"":rset.getString(2);
					//if(!trans_chrg.equals(""))
					//{
						//trans_chrg=nf2.format(Double.parseDouble(trans_chrg));
					//}
					//String market_margin = rset.getString(3)==null?"":nf2.format(rset.getDouble(3));
					//String oth_chrg = rset.getString(4)==null?"":nf2.format(rset.getDouble(4));
					
					VSEL_TRAD_CD.add(counterparty_cd);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					VSEL_PLANT_ABBR.add(plant_abbr);
					//VSEL_TRANS_CHRG.add(trans_chrg);
					//VSEL_MARKET_MARGIN.add(market_margin);
					//VSEL_OTH_CHRG.add(oth_chrg);
					
					String margeChrgVal="";
					String mergeChargDesc="";
					
					for(int i=0; i<VCHARGE_ABBR.size();i++)
					{
						String effdt="";
						String rate="";
						
						String hist_effdt="";
						String hist_rate="";
						
						int cnt=0;
						
						String queryString1="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),CHARGE_RATE "
								+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
								+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR)";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, contract_type);
						stmt1.setString(7, plant_seq);
						stmt1.setString(8, ""+VCHARGE_ABBR.elementAt(i));
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							cnt++;
							effdt=rset1.getString(1)==null?"":rset1.getString(1);
							rate=rset1.getString(2)==null?"":nf2.format(rset1.getDouble(2));
						}
						rset1.close();
						stmt1.close();
						
						margeChrgVal+=!margeChrgVal.equals("")?"$$"+rate+"#"+effdt:rate+"#"+effdt;
						
						// Here '~' used as using '\n' cause issue in passing merged string in JS.
						
						if(cnt>0)
						{
							mergeChargDesc += ""+VCHARGE_NAME.elementAt(i);
						}
						
						String queryString2="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),CHARGE_RATE "
								+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
								+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
								+ "ORDER BY EFF_DT DESC";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, cont_no);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, contract_type);
						stmt2.setString(7, plant_seq);
						stmt2.setString(8, ""+VCHARGE_ABBR.elementAt(i));
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							hist_effdt=rset2.getString(1)==null?"":rset2.getString(1);
							hist_rate=rset2.getString(2)==null?"":nf2.format(rset2.getDouble(2));
							
							mergeChargDesc += "~Rate : "+hist_rate+" Effective "+hist_effdt;
						}
						rset2.close();
						stmt2.close();
						
						mergeChargDesc+="~";
						
						if(cnt>0)
						{
							mergeChargDesc+="~";
						}
					}
					
					VSEL_CHARGE_VALUE.add(margeChrgVal);
					VSEL_CHARGE_DESC.add(mergeChargDesc);
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
	
	public void getContractSelectedTransporterPlantList()
	{
		String function_nm="getContractSelectedTransporterPlantList()";
		try
		{
			if(contract_type.equals("S") && opration.equals("INSERT"))
			{
				String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_TRANSPTR A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "F");
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					String plant_seq = rset.getString(2)==null?"":rset.getString(2);
					VSEL_TRANS_CD.add(transCd);
					VSEL_TRANS_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, plant_seq, "R");
					VSEL_TRANS_PLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					String plant_seq = rset.getString(2)==null?"":rset.getString(2);
					VSEL_TRANS_CD.add(transCd);
					VSEL_TRANS_PLANT_SEQ_NO.add(plant_seq);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, plant_seq, "R");
					VSEL_TRANS_PLANT_ABBR.add(plant_abbr);
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

	public void getContractSelectedTruckTransporterList()
	{
		String function_nm="getContractSelectedTruckTransporterList()";
		try
		{
			if(contract_type.equals("F") && opration.equals("INSERT"))
			{
				String queryString="SELECT TRANSPORTER_CD "
						+ "FROM FMS_AGMT_TRUCK_TRANS A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "D");
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR "
							+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_TRANS_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, transCd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String transAbbr = rset1.getString(2)==null?"":rset1.getString(2);
						
						VSEL_TRUCK_TRANS_CD.add(transCd);
						VSEL_TRUCK_TRANS_ABBR.add(transAbbr);
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT TRANSPORTER_CD "
						+ "FROM FMS_SUPPLY_CONT_TRUCK_TRANS "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String transCd = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT TRUCK_TRANS_CD, TRUCK_TRANS_ABBR "
							+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND TRUCK_TRANS_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, transCd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String transAbbr = rset1.getString(2)==null?"":rset1.getString(2);
						
						VSEL_TRUCK_TRANS_CD.add(transCd);
						VSEL_TRUCK_TRANS_ABBR.add(transAbbr);
					}
					rset1.close();
					stmt1.close();
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
	
	public void getContractSelectedFillingStationList()
	{
		String function_nm="getContractSelectedFillingStationList()";
		try
		{
			if(contract_type.equals("F") && opration.equals("INSERT"))
			{
				String queryString="SELECT FILL_STATION_CD "
						+ "FROM FMS_AGMT_FILLING_STN A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "D");
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String stCd = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT FILL_STATION_CD, FILL_STATION_ABBR "
							+ "FROM FMS_FILLING_STATION_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND FILL_STATION_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, stCd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String stAbbr = rset1.getString(2)==null?"":rset1.getString(2);
						
						VSEL_FILL_STATION_CD.add(stCd);
						VSEL_FILL_STATION_ABBR.add(stAbbr);
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT FILL_STATION_CD "
						+ "FROM FMS_SUPPLY_CONT_FILLING_STN "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String stCd = rset.getString(1)==null?"":rset.getString(1);
					
					String queryString1="SELECT FILL_STATION_CD, FILL_STATION_ABBR "
							+ "FROM FMS_FILLING_STATION_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=? AND FILL_STATION_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, stCd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String stAbbr = rset1.getString(2)==null?"":rset1.getString(2);
						
						VSEL_FILL_STATION_CD.add(stCd);
						VSEL_FILL_STATION_ABBR.add(stAbbr);
					}
					rset1.close();
					stmt1.close();
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
	
	public void getContractSelectedBusinessPlantList()
	{
		String function_nm="getContractSelectedBusinessPlantList()";
		try
		{
			if((contract_type.equals("S") || contract_type.equals("F")) && opration.equals("INSERT"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_BU A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				if(contract_type.equals("S"))
				{
					stmt.setString(3, "F");
				}
				else if(contract_type.equals("F"))
				{
					stmt.setString(3, "D");
				}
				stmt.setString(4, agmt_no);
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
			else
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
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
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSupplyContractList()
	{
		String function_nm="getSupplyContractList()";
		try
		{
			String temp_cont="";
			if(contract_type.equals(""))
			{
				if(callFlag.equalsIgnoreCase("DLNG_CONTRACT_LIST"))
				{
					if(clearance.equals("IGX"))
					{
						temp_cont="CONTRACT_TYPE IN ('W')";
					}
					else
					{
						temp_cont="CONTRACT_TYPE IN ('F','E')";
					}
				}
				else if(callFlag.equalsIgnoreCase("CONTRACT_LIST")) 
				{
					if(clearance.equals("IGX"))
					{
						temp_cont="CONTRACT_TYPE IN ('X')";
					}
					else
					{
						temp_cont="CONTRACT_TYPE IN ('S','L')";
					}
				}
			}
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
			if(!contract_type.equals(""))
			{
				queryString += "AND CONTRACT_TYPE=?";
			}
			else
			{
				queryString += "AND "+temp_cont+" ";
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				if(!contract_type.equals(""))
				{
					queryString += "AND CONTRACT_TYPE=?";
				}
				else
				{
					queryString += "AND "+temp_cont+" ";
				}
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS IN ('C','X','T') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS NOT IN ('C','X','T') ";
			}
			int count=0;
			String temp_qstring = queryString;
			stmt = conn.prepareStatement(temp_qstring);
			stmt.setString(++count, comp_cd);
			if(!contract_type.equals(""))
			{
				stmt.setString(++count, contract_type);
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				if(!contract_type.equals(""))
				{
					stmt.setString(++count, contract_type);
				}
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++count, counterparty_cd);
				}
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String cont=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(20)==null?"":rset.getString(20);
				
				String start_dt=rset.getString(9)==null?"":rset.getString(9);
				String end_dt=rset.getString(10)==null?"":rset.getString(10);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				
				VTCQ.add(nf.format(rset.getDouble(7)));
				VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,rset.getString(8)==null?"":rset.getString(8)));
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONT_NAME.add(rset.getString(11)==null?"":rset.getString(11));
				String cont_status_flg=rset.getString(12)==null?"":rset.getString(12);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(13)==null?"":rset.getString(13));
				
				String rateCd=rset.getString(16)==null?"":rset.getString(16);
				String rate="";
				if(rateCd.equals("1")) {
					rate=nf.format(rset.getDouble(15));
				}else {
					rate=nf2.format(rset.getDouble(15));
				}
				String rateFor = rate+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
				VRATE_FORMULA.add(rateFor);
				VIS_ALLOCATED.add(rset.getString(17)==null?"":rset.getString(17));
				
				String cont_ref=rset.getString(14)==null?"":rset.getString(14);
				String trade_ref=rset.getString(18)==null?"":rset.getString(18);
				String agmt_base=rset.getString(19)==null?"":rset.getString(19);
				
				if(cont_type.equals("X") || cont_type.equals("W"))
				{
					cont_ref=trade_ref;
				}
				
				VCONT_REF_NO.add(cont_ref);
				
				String dealMapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				if(agmt_base.equals("D"))
				{
					dealMapping=dealMapping+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				VCONTRACT_TYPE.add(cont_type);
				VDEAL_MAPPING.add(dealMapping);
				
				VSUPPLIED_MMBTU.add(""+allocUtil.getBestSupplyAllocationQty(conn, own_cd, countpty_cd, agmt, cont, cont_type,start_dt,end_dt,agmt_base,"0"));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSupplyContractList_FCC()
	{
		String function_nm="getSupplyContractList_FCC()";
		try
		{
			String temp_cont="";
			if(callFlag.equalsIgnoreCase("DLNG_CONTRACT_LIST_FCC"))
			{
				if(clearance.equals("IGX"))
				{
					temp_cont="CONTRACT_TYPE IN ('W')";
				}
				else
				{
					temp_cont="CONTRACT_TYPE IN ('F','E')";
				}
			}
			else if(callFlag.equalsIgnoreCase("CONTRACT_LIST_FCC")) 
			{
				if(clearance.equals("IGX"))
				{
					temp_cont="CONTRACT_TYPE IN ('X')";
				}
				else
				{
					temp_cont="CONTRACT_TYPE IN ('S','L')";
				}
			}
			
			int count=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND (ADV_ADJUST IS NOT NULL OR CONTRACT_TYPE IN ('X', 'W')) "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!contract_type.equals(""))
			{
				queryString += "AND CONTRACT_TYPE=?";
			}
			else
			{
				queryString += "AND "+temp_cont+" ";
			}
			if(!contract_type.equals("X") && !contract_type.equals("W"))
			{
				queryString+="AND (SELECT COUNT(*) "
						+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX=? "
						+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
						+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 ";
			}
			queryString+="AND (SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL B "
					+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 "
					+ "AND (CHANGE_DATE_REQ!=? OR CHANGE_DATE_REQ IS NULL) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE,CONTRACT_TYPE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND (ADV_ADJUST IS NOT NULL OR CONTRACT_TYPE IN ('X', 'W')) "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				if(!contract_type.equals(""))
				{
					queryString += "AND CONTRACT_TYPE=?";
				}
				else
				{
					queryString += "AND "+temp_cont+" ";
				}
				if(!contract_type.equals("X") && !contract_type.equals("W"))
				{
					queryString+="AND (SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
							+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX=? "
							+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
							+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 ";
				}
				queryString+="AND (SELECT COUNT(*) "
						+ "FROM FMS_SUPPLY_BILLING_DTL B "
						+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 "
						+ "AND (CHANGE_DATE_REQ!=? OR CHANGE_DATE_REQ IS NULL) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS IN ('C','X','T') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS NOT IN ('C','X','T') ";
			}
			String temp_qstring = queryString;
			stmt = conn.prepareStatement(temp_qstring);
			stmt.setString(++count, comp_cd);
			if(!contract_type.equals(""))
			{
				stmt.setString(++count, contract_type);
			}
			if(!contract_type.equals("X") && !contract_type.equals("W"))
			{
				stmt.setString(++count, gx);
			}
			stmt.setString(++count, "Y");
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				if(!contract_type.equals(""))
				{
					stmt.setString(++count, contract_type);
				}
				if(!contract_type.equals("X") && !contract_type.equals("W"))
				{
					stmt.setString(++count, gx);
				}
				stmt.setString(++count, "Y");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++count, counterparty_cd);
				}
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String cont=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(20)==null?"":rset.getString(20);
				
				String start_dt=rset.getString(9)==null?"":rset.getString(9);
				String end_dt=rset.getString(10)==null?"":rset.getString(10);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				
				VTCQ.add(nf.format(rset.getDouble(7)));
				VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,rset.getString(8)==null?"":rset.getString(8)));
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONT_NAME.add(rset.getString(11)==null?"":rset.getString(11));
				String cont_status_flg=rset.getString(12)==null?"":rset.getString(12);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(13)==null?"":rset.getString(13));
				
				String rateCd=rset.getString(16)==null?"":rset.getString(16);
				String rate="";
				if(rateCd.equals("1")) {
					rate=nf.format(rset.getDouble(15));
				}else {
					rate=nf2.format(rset.getDouble(15));
				}
				String rateFor = rate+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
				VRATE_FORMULA.add(rateFor);
				VIS_ALLOCATED.add(rset.getString(17)==null?"":rset.getString(17));
				
				String cont_ref=rset.getString(14)==null?"":rset.getString(14);
				String trade_ref=rset.getString(18)==null?"":rset.getString(18);
				String agmt_base=rset.getString(19)==null?"":rset.getString(19);
				
				if(cont_type.equals("X") || contract_type.equals("W"))
				{
					cont_ref=trade_ref;
				}
				
				VCONT_REF_NO.add(cont_ref);
				
				String dealMapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				if(agmt_base.equals("D"))
				{
					dealMapping=dealMapping+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				
				VCONTRACT_TYPE.add(cont_type);
				VDEAL_MAPPING.add(dealMapping);
				
				VSUPPLIED_MMBTU.add(""+allocUtil.getBestSupplyAllocationQty(conn, own_cd, countpty_cd, agmt, cont, cont_type,start_dt,end_dt,agmt_base,"0"));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNominationSupplyContractList()
	{
		String function_nm="getNominationSupplyContractList()";
		try
		{
			String from_date="";
			String to_date="";
			
			String[] split=gas_dt.split("/");
			String month=split[1];
			String year=split[2];
			
			if(nomination_freq.equals("W"))
			{
				String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, gas_dt);
				stmt.setString(2, gas_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					from_date = rset.getString(1)==null?"":rset.getString(1); 
					to_date = rset.getString(2)==null?"":rset.getString(2);
				}
				rset.close();
				stmt.close();
			}
			else if(nomination_freq.equals("M"))
			{
				from_date=""+dateUtil.getFirstDateOfMonth(month, year);
				to_date=""+dateUtil.getLastDateOfMonth(month, year);
			}
			else if(nomination_freq.equals("F"))
			{
				int count=dateUtil.getDays(gas_dt, "15/"+month+"/"+year);
				int count1=dateUtil.getDays(gas_dt, ""+dateUtil.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					from_date= "01/"+month+"/"+year;
					to_date= "15/"+month+"/"+year;
				}
				else if(count1 <= 1)
				{
					from_date= "16/"+month+"/"+year;
					to_date= ""+dateUtil.getLastDateOfMonth(month, year);
				}
			}
			else
			{
				from_date=gas_dt;
				to_date=gas_dt;
			}
			
			int count=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,CONTRACT_TYPE,AGMT_BASE "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ('S','L','X') "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
			if(nomination_freq.equals("M"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND SELLER_MONTH_NOM=? ";
				}
				else
				{
					queryString+=" AND BUYER_MONTH_NOM=? ";
				}
			}
			else if(nomination_freq.equals("W"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND SELLER_WEEK_NOM=? ";
				}
				else
				{
					queryString+=" AND BUYER_WEEK_NOM=? ";
				}
			}
			else if(nomination_freq.equals("F"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND SELLER_FORNGT_NOM=? ";
				}
				else
				{
					queryString+=" AND BUYER_FORNGT_NOM=? ";
				}
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "RATE,RATE_UNIT,IS_ALLOCATED,TRADE_REF_NO,CONTRACT_TYPE,AGMT_BASE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ('S','L','X') "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				if(nomination_freq.equals("M"))
				{
					if(nomination_type.equals("S"))
					{
						queryString+=" AND SELLER_MONTH_NOM=? ";
					}
					else
					{
						queryString+=" AND BUYER_MONTH_NOM=? ";
					}
				}
				else if(nomination_freq.equals("W"))
				{
					if(nomination_type.equals("S"))
					{
						queryString+=" AND SELLER_WEEK_NOM=? ";
					}
					else
					{
						queryString+=" AND BUYER_WEEK_NOM=? ";
					}
				}
				else if(nomination_freq.equals("F"))
				{
					if(nomination_type.equals("S"))
					{
						queryString+=" AND SELLER_FORNGT_NOM=? ";
					}
					else
					{
						queryString+=" AND BUYER_FORNGT_NOM=? ";
					}
				}
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS IN ('C','X','T') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS NOT IN ('C','X','T') ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, to_date);
			stmt.setString(++count, from_date);
			if(nomination_freq.equals("M"))
			{
				stmt.setString(++count, "Y");
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++count, "Y");
				}
				else
				{
					stmt.setString(++count, "Y");
				}*/
			}
			else if(nomination_freq.equals("W"))
			{
				stmt.setString(++count, "Y");
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++count, "Y");
				}
				else
				{
					stmt.setString(++count, "Y");
				}*/
			}
			else if(nomination_freq.equals("F"))
			{
				stmt.setString(++count, "Y");
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++count, "Y");
				}
				else
				{
					stmt.setString(++count, "Y");
				}*/
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, to_date);
				stmt.setString(++count, from_date);
				if(nomination_freq.equals("M"))
				{
					stmt.setString(++count, "Y");
				}
				else if(nomination_freq.equals("W"))
				{
					stmt.setString(++count, "Y");
				}
				else if(nomination_freq.equals("F"))
				{
					stmt.setString(++count, "Y");
				}
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String cont=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(19)==null?"":rset.getString(19);
				String agmt_base=rset.getString(20)==null?"":rset.getString(20);
				
				//String display_mapping=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String display_mapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				if(agmt_base.equals("D"))
				{
					display_mapping=display_mapping+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				VDIS_CONT_MAPPING.add(display_mapping);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(3)==null?"0":rset.getString(3));
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(rset.getString(5)==null?"0":rset.getString(5));
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				VTCQ.add(nf.format(rset.getDouble(7)));
				VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,rset.getString(8)==null?"":rset.getString(8)));
				VSTART_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VEND_DT.add(rset.getString(10)==null?"":rset.getString(10));
				VCONT_NAME.add(rset.getString(11)==null?"":rset.getString(11));
				String cont_status_flg=rset.getString(12)==null?"":rset.getString(12);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(13)==null?"":rset.getString(13));
				
				String cont_ref=rset.getString(14)==null?"":rset.getString(14);
				String trade_ref=rset.getString(18)==null?"":rset.getString(18);
				if(cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				VCONT_REF_NO.add(cont_ref);
				
				String rateCd=rset.getString(16)==null?"":rset.getString(16);
				String rate="";
				if(rateCd.equals("1")) {
					rate=nf.format(rset.getDouble(15));
				}else {
					rate=nf2.format(rset.getDouble(15));
				}
				String rateFor = rate+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
				VRATE_FORMULA.add(rateFor);
				VIS_ALLOCATED.add(rset.getString(17)==null?"":rset.getString(17));
				VAGMT_BASE.add(rset.getString(20)==null?"":rset.getString(20));
				
				VSUPPLIED_MMBTU.add(""+allocUtil.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmt, cont, cont_type,"0"));
				VCARGO_NO.add("0");
				
				VBOE_NO.add("BOE0");
			}
			rset.close();
			stmt.close();
			
			queryString="SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.AGMT_NO, A.AGMT_REV, A.CONT_NO, A.CONT_REV, NULL, 1, "
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
					+ "A.CONT_NAME,A.CONT_STATUS,A.FCC_FLAG,B.CARGO_REF,"
					+ "A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,'Y',NULL,A.CONTRACT_TYPE,A.AGMT_BASE,B.CARGO_NO,A.SUG,"
					+ "(SELECT SUM(C.ADQ_QTY) FROM FMS_LTCORA_CONT_CARGO_ADQ C WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CARGO_NO=C.CARGO_NO AND A.CONT_NO=C.CONT_NO "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE), "
					+ "B.EDQ_QTY,B.BOE_NO "
					+ "FROM FMS_LTCORA_CONT_MST A,FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			if(nomination_freq.equals("M"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND A.SELLER_MONTH_NOM=? ";
				}
				else
				{
					queryString+=" AND A.BUYER_MONTH_NOM=? ";
				}
			}
			else if(nomination_freq.equals("W"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND A.SELLER_WEEK_NOM=? ";
				}
				else
				{
					queryString+=" AND A.BUYER_WEEK_NOM=? ";
				}
			}
			else if(nomination_freq.equals("F"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND A.SELLER_FORNGT_NOM=? ";
				}
				else
				{
					queryString+=" AND A.BUYER_FORNGT_NOM=? ";
				}
			}
			/*queryString+= "GROUP BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.AGMT_NO, A.AGMT_REV, A.CONT_NO, A.CONT_REV, NULL, 1,"
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
					+ "A.CONT_NAME,A.CONT_STATUS,A.FCC_FLAG,A.CONT_REF_NO,"
					+ "A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,'Y',NULL,A.CONTRACT_TYPE,A.AGMT_BASE,B.CARGO_NO,A.SUG,B.EDQ_QTY "; 
			*/stmt = conn.prepareStatement(queryString);
			count=0;
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, "C");
			stmt.setString(++count, "Y");
			stmt.setString(++count, "A");
			stmt.setString(++count, to_date);
			stmt.setString(++count, from_date);
			if(nomination_freq.equals("M"))
			{
				stmt.setString(++count, "Y");
			}
			else if(nomination_freq.equals("W"))
			{
				stmt.setString(++count, "Y");
			}
			else if(nomination_freq.equals("F"))
			{
				stmt.setString(++count, "Y");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String cont=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(19)==null?"":rset.getString(19);
				String agmt_base=rset.getString(20)==null?"":rset.getString(20);
				String cargo_no=rset.getString(21)==null?"":rset.getString(21);
				
				//String display_mapping=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String display_mapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
				if(agmt_base.equals("D"))
				{
					display_mapping=display_mapping+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				VDIS_CONT_MAPPING.add(display_mapping);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(3)==null?"0":rset.getString(3));
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(rset.getString(5)==null?"0":rset.getString(5));
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				
				VSTART_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VEND_DT.add(rset.getString(10)==null?"":rset.getString(10));
				VCONT_NAME.add(rset.getString(11)==null?"":rset.getString(11));
				String cont_status_flg=rset.getString(12)==null?"":rset.getString(12);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(13)==null?"":rset.getString(13));
				
				String cont_ref=rset.getString(14)==null?"":rset.getString(14);
				String trade_ref=rset.getString(18)==null?"":rset.getString(18);
				if(cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				VCONT_REF_NO.add(cont_ref);
				
				String rateCd=rset.getString(16)==null?"":rset.getString(16);
				String rate="";
				if(rateCd.equals("1")) {
					rate=nf.format(rset.getDouble(15));
				}else {
					rate=nf2.format(rset.getDouble(15));
				}
				String rateFor = rate+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
				VRATE_FORMULA.add(rateFor);
				VIS_ALLOCATED.add(rset.getString(17)==null?"":rset.getString(17));
				VAGMT_BASE.add(rset.getString(20)==null?"":rset.getString(20));
				
				VSUPPLIED_MMBTU.add(""+allocUtil.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmt, cont, cont_type,"0"));
				VCARGO_NO.add(cargo_no);
				
				String sug_percent = rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				double adq_qty = rset.getDouble(23);
				double edq_qty = rset.getDouble(24);
				double tcq=0;
				String internal_map_id = cont_type+"-"+countpty_cd+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev+"-"+cargo_no;
				
				queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
						+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, "C");
				stmt1.setString(4, agmt);
				stmt1.setString(5, agmt_rev);
				stmt1.setString(6, "A");
				stmt1.setString(7, cont);
				stmt1.setString(8, cont_type);
				stmt1.setString(9, cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					sug_percent = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
				}
				rset1.close();
				stmt1.close();
				
				if(adq_qty > 0 && !sug_percent.equals(""))
				{
					tcq= adq_qty - (adq_qty * Double.parseDouble(sug_percent) / 100);
				}
				else if(edq_qty > 0 && !sug_percent.equals(""))
				{
					tcq= edq_qty - (edq_qty * Double.parseDouble(sug_percent) / 100);
				}
				
				VTCQ.add(nf.format(tcq));
				VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,rset.getString(8)==null?"":rset.getString(8)));
				
				VBOE_NO.add(rset.getString(25)==null?"":rset.getString(25));
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
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO) "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
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
	
	public void getCountSecurityDetail()
	{
		String function_nm="getCountSecurityDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, gx);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_security_dtl=""+rset.getInt(1);
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
			String sales_cont_map=contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
			
			String queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_REF_NO,A.CT_REF_NO,A.CONTRACT_TYPE,A.COUNTERPARTY_CD "
					+ "FROM FMS_GTA_CONT_MST A,FMS_GTA_CONT_MAP B "
					+ "WHERE A.COMPANY_CD=? AND B.CUSTOMER_CD=? "
					+ "AND SELL_CONT_MAP LIKE ? AND A.CONTRACT_TYPE=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_GTA_CONT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sales_cont_map);
			stmt.setString(4, "C");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				
				String cont_ref=rset.getString(5)==null?"":rset.getString(5);
				String ct_ref=rset.getString(6)==null?"":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				
				String counterpty_cd=rset.getString(8)==null?"":rset.getString(8);
				
				String counterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterpty_cd);
				
				if(gt_contract_dtl.equals(""))
				{
					gt_contract_dtl=counterpty_abbr+"-"+cont_type+""+agmt+"-"+cont+" ("+cont_ref+")";
				}
				else
				{
					gt_contract_dtl+=", "+counterpty_abbr+"-"+cont_type+""+agmt+"-"+cont+" ("+cont_ref+")";
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

	public void getSoTsContractDetail()
	{
		String function_nm="getSoTsContractDetail()";
		try
		{
			String sales_cont_map=contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
			
			String queryString="SELECT A.AGMT_NO,A.CONT_NO,A.CONT_REF_NO,A.CONTRACT_TYPE,A.COUNTERPARTY_CD "
					+ "FROM FMS_SVC_CONT_MST A,FMS_SVC_CONT_MAP B "
					+ "WHERE A.COMPANY_CD=? AND B.CUSTOMER_CD=? "
					+ "AND SELL_CONT_MAP LIKE ? AND A.CONTRACT_TYPE IN(?,?) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_SVC_CONT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
					+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sales_cont_map);
			stmt.setString(4, "M");
			stmt.setString(5, "B");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String cont=rset.getString(2)==null?"":rset.getString(2);
				
				String cont_ref=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				
				String counterpty_cd=rset.getString(5)==null?"":rset.getString(5);
				
				String counterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterpty_cd);
				
				if(gt_contract_dtl.equals(""))
				{
					gt_contract_dtl=counterpty_abbr+"-"+cont_type+""+agmt+"-"+cont+" ("+cont_ref+")";
				}
				else
				{
					gt_contract_dtl+=", "+counterpty_abbr+"-"+cont_type+""+agmt+"-"+cont+" ("+cont_ref+")";
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
	
	public void getContractDcqDetail()
	{
		String function_nm="getContractDcqDetail()";
		String sysdate = dateUtil.getSysdate();
		try
		{
			String queryString="SELECT SEQ_NO,TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),DCQ,REMARK,STATUS "
					+ "FROM FMS_SUPPLY_CONT_DCQ_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "ORDER BY FROM_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String start_dt = rset.getString(2)==null?"":rset.getString(2);
				String end_dt = rset.getString(3)==null?"":rset.getString(3);
				VSEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VFROM_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VTO_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VDCQ.add(rset.getString(4)==null?"":rset.getString(4));
				VREMARK.add(rset.getString(5)==null?"":rset.getString(5));
				VSTATUS.add(rset.getString(6)==null?"":rset.getString(6));
				
				int isExpired = dateUtil.getDays(end_dt, sysdate);
				
				if(isExpired<0)
				{
					VIS_RADIO_ENABLE.add("N");
				}
				else
				{
					VIS_RADIO_ENABLE.add("Y");
				}
			}
			rset.close();
			stmt.close();
			
			/*if(VSEQ_NO.size()==0)
			{
				VSEQ_NO.add("1");
				VFROM_DT.add("");
				VTO_DT.add("");
				VDCQ.add("");
				VREMARK.add("");
				VSTATUS.add("Y");
			}*/
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAgreementSegment()
	{
		String function_nm="getAgreementSegment()";
		try
		{
			VSEGMENT.add("FGSA");
			VSEGMENT_TYPE.add("F");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getActivationAgreementCounterpartyList()
	{
		String function_nm="getActivationAgreementCounterpartyList()";
		try
		{
			String queryString="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND REOPEN_REQUEST_FLAG=? "
					+ "AND AGMT_REV=(SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,comp_cd);
			stmt.setString(2,"Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getActivationAgreement()
	{
		String function_nm="getActivationAgreement()";
		try
		{
			/*	
			 	REOPEN_REQUEST_FLAG = 'Y'		REQUEST FOR ACTIVATION GENERATED 
			 	REOPEN_REQUEST_FLAG = 'N'		REQUEST FOR ACTIVATION REJECTED 
			 	REOPEN_REQUESST_FLAG = 'A'		REQUEST FOR ACTIVATION APPROVED 
			*/
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("FGSA");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				int ctn=0;
				int index=0;
				String queryString="SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_REF_NO, "
						+ "AGMT_BASE,TO_CHAR(REOPEN_REQUEST_DT,'DD/MM/YYYY'),  "
						+ "REOPEN_REQUEST_FLAG,AGMT_TYPE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),  "
						+ "CASE WHEN TO_DATE(END_DT,'DD/MM/YYYY')<TO_DATE(SYSDATE,'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRED  "
						+ "FROM FMS_AGMT_MST A  "
						+ "WHERE COMPANY_CD=? "
						+ "AND AGMT_REV=(SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE B.COMPANY_CD=A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.AGMT_TYPE=A.AGMT_TYPE AND B.AGMT_NO=A.AGMT_NO) "
						+ "AND AGMT_TYPE=? AND REOPEN_REQUEST_FLAG=? ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++ctn,comp_cd);
				stmt.setString(++ctn,""+VTEMP_SEGMENT_TYPE.elementAt(i));
				stmt.setString(++ctn,"Y");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn,counterparty_cd);
				}
				rset = stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmt=rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev=rset.getString(3)==null?"0":rset.getString(3);
					String start_dt =rset.getString(4)==null?"":rset.getString(4);
					String end_dt=rset.getString(5)==null?"":rset.getString(5);
					String agmt_ref=rset.getString(6)==null?"":rset.getString(6);
					String agmt_base = rset.getString(7)==null?"":rset.getString(7);
					String reopen_req_dt = rset.getString(8)==null?"":rset.getString(8);
					String reopen_req_flag = rset.getString(9)==null?"":rset.getString(9);
					String agmt_type= rset.getString(10)==null?"":rset.getString(10);
					String sign_dt = rset.getString(11)==null?"":rset.getString(11);
					
					VREMARK.add("");
					
					VCLOSURE_REQ_FLAG.add(reopen_req_flag);
					VSUPPLIED_MMBTU.add("");
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add("");
					VCONT_REV_NO.add("");
					VCONTRACT_TYPE.add(agmt_type);
					VCONTRACT_TYPE_NM.add("FGSA");
					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VCONT_REF_NO.add(agmt_ref);
					
					String dealMapping=utilBean.NewAgmtMappingId(comp_cd,countpty_cd, agmt,agmt_rev, agmt_type);
					VDIS_CONT_MAPPING.add(dealMapping);
					
					VCONT_STATUS_FLG.add("");
					VCONT_STATUS.add("");
					
					VRATE_FORMULA.add("");
					VRATE.add("");
					VRATE_UNIT.add("");
					VRATE_UNIT_NM.add("");
					VPRICE_TYPE.add("");
					VTCQ.add("");
					VCLOSURE_DT.add(reopen_req_dt);
					VTCQ_SIGN.add("");
					VVAR_TCQ_QTY.add("");
					VSIGNING_DT.add(sign_dt);
				}
				VINDEX.add(index);
				rset.close();
				stmt.close();
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			VSEGMENT.add("Supply Notice");
			VSEGMENT.add("Letter of Agreement");
			VSEGMENT.add("IGX");
			//Below added for DLNG 
			/*VSEGMENT.add("Supply Notice (DLNG)");
			VSEGMENT.add("Letter of Agreement (DLNG)");
			VSEGMENT.add("IGX (DLNG)");*/
			
			VSEGMENT_TYPE.add("S");
			VSEGMENT_TYPE.add("L");
			VSEGMENT_TYPE.add("X");
			//Below added for DLNG
			/*VSEGMENT_TYPE.add("F");
			VSEGMENT_TYPE.add("E");
			VSEGMENT_TYPE.add("W");*/
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDlngSegment()
	{
		String function_nm="getDlngSegment()";
		try
		{
			VSEGMENT.add("Supply Notice (DLNG)");
			VSEGMENT.add("Letter of Agreement (DLNG)");
			VSEGMENT.add("IGX (DLNG)");
			
			VSEGMENT_TYPE.add("F");
			VSEGMENT_TYPE.add("E");
			VSEGMENT_TYPE.add("W");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTcqReqCustomerCounterpartyList(String cont_typ)
	{
		String function_nm="getTcqReqCustomerCounterpartyList()";
		try
		{
			int ctn=0;
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND TCQ_REQUEST_FLAG=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(cont_typ.equals("DLNG")||cont_typ.equals("RLNG"))
			{
				queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			if(cont_typ.equals("DLNG"))
			{
				stmt.setString(++ctn, "F");
				stmt.setString(++ctn, "E");
				stmt.setString(++ctn, "W");
			}
			else if(cont_typ.equals("RLNG"))
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, "L");
				stmt.setString(++ctn, "X");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTcqModificationRequest()
	{
		String function_nm="getTcqModificationRequest()";
		try
		{
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("S"))
				{
					VTEMP_SEGMENT.add("Supply Notice");
				}
				else if(segmentType.equals("L"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement");
				}
				else if(segmentType.equals("X"))
				{
					VTEMP_SEGMENT.add("IGX");
				}
				else if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("Supply Notice (DLNG) ");
				}
				else if(segmentType.equals("E"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement (DLNG)");
				}
				else if(segmentType.equals("W"))
				{
					VTEMP_SEGMENT.add("IGX (DLNG)");
				}
				else
				{
					VTEMP_SEGMENT.add("");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				int index=0;
				
				String queryString="SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,CONT_REF_NO,TRADE_REF_NO,"
						+ "RATE,RATE_UNIT,TCQ_SIGN,TCQ_REQUEST_QTY,TCQ,CLOSURE_ALLOC_QTY "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND TCQ_REQUEST_FLAG=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
				stmt.setString(3, "Y");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(4, counterparty_cd);
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmt=rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev=rset.getString(3)==null?"0":rset.getString(3);
					String cont=rset.getString(4)==null?"0":rset.getString(4);
					String cont_rev=rset.getString(5)==null?"0":rset.getString(5);
					String cont_type = ""+VTEMP_SEGMENT_TYPE.elementAt(i);
					
					String cont_ref=rset.getString(9)==null?"":rset.getString(9);
					String trade_ref=rset.getString(10)==null?"":rset.getString(10);
					
					double var_tcq = rset.getDouble(14);
					double closure_mmbtu=rset.getDouble(16);
					
					String var_tcq_tooltip="";
					double upd_var_tcq=var_tcq+closure_mmbtu;
					if(Double.doubleToRawLongBits(upd_var_tcq)==Double.doubleToRawLongBits(0)&&Double.doubleToRawLongBits(closure_mmbtu)<Double.doubleToRawLongBits(0))		//if(upd_var_tcq==0 && closure_mmbtu<0)		
					{
						var_tcq_tooltip+="Contract Reopened ";
					}
					else if(Double.doubleToRawLongBits(upd_var_tcq)!=Double.doubleToRawLongBits(0) && Double.doubleToRawLongBits(closure_mmbtu)<Double.doubleToRawLongBits(0))
					{
						var_tcq_tooltip+="Contract Reopened ";
						var_tcq_tooltip+="\nDelta TCQ (MMBTU)= Delta TCQ (MMBTU) - Released MMBTU"
								+ "\n"+nf.format(var_tcq)+""+nf.format(closure_mmbtu)+"="+nf.format(upd_var_tcq);
					}
					var_tcq = Double.doubleToRawLongBits(upd_var_tcq)!=Double.doubleToRawLongBits(0)?upd_var_tcq:var_tcq;
					
					if(cont_type.equals("X"))
					{
						cont_ref=trade_ref;
					}
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
					VSTART_DT.add(rset.getString(6)==null?"":rset.getString(6));
					VEND_DT.add(rset.getString(7)==null?"":rset.getString(7));
					VCONT_REF_NO.add(cont_ref);
					
					//String dealMapping=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String dealMapping=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					VDIS_CONT_MAPPING.add(dealMapping);
					
					String cont_status_flg=rset.getString(8)==null?"":rset.getString(8);
					VCONT_STATUS_FLG.add(cont_status_flg);
					VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
					
					double rate=rset.getDouble(11);
					String rateCd=rset.getString(12)==null?"":rset.getString(12);
					String sales_price=utilBean.RateNumberFormat(rate, rateCd);
					
					String rateFor = sales_price+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
					VRATE_FORMULA.add(rateFor);
					VRATE.add(sales_price);
					VRATE_UNIT.add(rateCd);
					VRATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rateCd));
					VPRICE_TYPE.add("Fixed");
					
					VTCQ_SIGN.add(rset.getString(13)==null?"":rset.getString(13));
					//VVAR_TCQ_QTY.add(nf.format(rset.getDouble(14)));
					VVAR_TCQ_QTY.add(nf.format(var_tcq));
					VTCQ.add(nf.format(rset.getDouble(15)));
					VREMARK.add(var_tcq_tooltip);
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
	
	public void getPriceChangeHistory()
	{
		String function_nm="getPriceChangeHistory()";
		try 
		{
			//FOR DISPLAY PURPOSE
			String history="";
			String last_eff_dt="";
			int count=0;
			
			String queryString1 = "SELECT DISTINCT MODIFICATION_SEQ_NO, NEW_SALE_PRICE,TO_CHAR(NEW_PRICE_EFF_DT,'DD/MM/YYYY'),FLAG,ORI_SALE_PRICE "
					+ "FROM FMS_SUPPLY_ALLOC_REVISED "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
					+ "AND COUNTERPARTY_CD=? "
					+ "AND CONTRACT_TYPE=? ORDER BY MODIFICATION_SEQ_NO DESC";//GROUP BY NEW_SALE_PRICE,NEW_PRICE_EFF_DT";
			stmt = conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, agmt_no);
			stmt.setString(3, agmt_rev_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				double sales = rset.getDouble(2);
				double ori_sales = rset.getDouble(5);
				String dt =rset.getString(3)==null?"":rset.getString(3);
				String flag=rset.getString(4)==null?"":rset.getString(4);
				String color="";
				if(flag.equals("X")) {
					flag="Rejected";
					color="red";
				}else if(flag.equals("A")) {
					flag="Approved";
					color="green";
					count+=1;
				}else {
					flag="Requested";
					color="blue";
				}
				
				String rateLable = "($/MMBTU)";
				if(rate_unit.trim().equals("1")) {
					rateLable = "(INR/MMBTU)";
				}
				String rateFormate=""+utilBean.RateNumberFormat(sales, rate_unit);
				if(history.equals(""))
				{
					//history+=""+rateFormate+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
					history+=""+rateFormate+ rateLable+" From "+dt+" "+flag+" ";
				}
				else
				{
					//history+="<br>"+rateFormate+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
					history+="\n"+rateFormate+ rateLable+" From "+dt+" "+flag+"";
				}
			}
			rset.close();
			stmt.close();
			
			price_change_history=history;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGxCounterpartyCd(String abbr)
	{
		String function_nm="getGxCounterpartyCd()";
		try
		{
			String queryString = "SELECT COUNTERPARTY_CD "
					+ "FROM FMS_COMPANY_EXCHG_MST A "
					+ "WHERE COUNTERPARTY_ABBR=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, abbr);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				gx_counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGasExchangeBuPlantList()
	{
		String function_nm="getGasExchangeBuPlantList()";
		try
		{
			String queryString="SELECT COUNTERPARTY_CD,PLANT_NAME,PLANT_ABBR,SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS=? ";
			queryString+= "ORDER BY SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "G");
			stmt.setString(2, comp_cd);
			stmt.setString(3, gx_counterparty_cd);
			stmt.setString(4, "Y");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VGX_BU_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VGX_BU_PLANT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VGX_BU_PLANT_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VGX_BU_PLANT_SEQ_NO.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractSelectedGasExchangeBuPlantList()
	{
		String function_nm="getContractSelectedGasExchangeBuPlantList()";
		try
		{
			String queryString="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
					+ "FROM FMS_SUPPLY_CONT_GX_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, gx_counterparty_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String gx_cd = rset.getString(1)==null?"":rset.getString(1);
				String gx_bu_plant_seq = rset.getString(2)==null?"":rset.getString(2);
				
				VSEL_GX_BU_CD.add(gx_cd);
				VSEL_GX_BU_PLANT_SEQ_NO.add(gx_bu_plant_seq);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public int isInvoiceSubmitted(String countpty_cd,String cont, String agmt,String cont_type)
	{
		String function_nm="isInvoiceSubmitted()";
		int count=0;
		try
		{
			String queryString="SELECT MAX(MAX_COUNT)  "
					+ "FROM (SELECT COUNT(*) MAX_COUNT  "
					+ "FROM FMS_INVOICE_MST  "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INV_FLAG=? "
					+ "UNION ALL "
					+ "SELECT COUNT(*) MAX_COUNT "
					+ "FROM FMS_DLNG_INVOICE_MST  "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INV_FLAG=?) ";
			stmt_temp = conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, countpty_cd);
			stmt_temp.setString(3, cont);
			stmt_temp.setString(4, agmt);
			stmt_temp.setString(5, cont_type);
			stmt_temp.setString(6, "F");
			stmt_temp.setString(7, comp_cd);
			stmt_temp.setString(8, countpty_cd);
			stmt_temp.setString(9, cont);
			stmt_temp.setString(10, agmt);
			stmt_temp.setString(11, cont_type);
			stmt_temp.setString(12, "F");
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return count;
	}
	
	public void getMaxAllocationInvoiceDate()
	{
		String function_nm="getMaxAllocationInvoiceDate()";
		try
		{
			String queryString1="SELECT TO_CHAR(MIN(MIN_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MIN(GAS_DT) MIN_DATE FROM FMS_DAILY_BUYER_NOM "
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
			
			String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
			String queryString2="SELECT TO_CHAR(MAX(MAX_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DAILY_ALLOCATION_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG IN ('F') "
					+ "UNION ALL "
					+ "SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DAILY_TRANSPORTER_ALLOC "
					+ "WHERE COMPANY_CD=? AND SELL_CONT_MAP LIKE ? ";
			queryString2+="UNION ALL ";
			queryString2+="SELECT MAX(GAS_DT) MAX_DATE FROM FMS_DLNG_ALLOC_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_DLNG_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG IN ('F'))";
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
			stmt2.setString(12, cont_map);
			stmt2.setString(13, comp_cd);
			stmt2.setString(14, counterparty_cd);
			stmt2.setString(15, agmt_no);
			stmt2.setString(16, cont_no);
			stmt2.setString(17, contract_type);
			stmt2.setString(18, comp_cd);
			stmt2.setString(19, counterparty_cd);
			stmt2.setString(20, agmt_no);
			stmt2.setString(21, cont_no);
			stmt2.setString(22, contract_type);
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
	
	public void getChargeMaster()
	{
		String function_nm="getChargeMaster()";
		try
		{
			utilBean.getChargeMaster(conn);
			VCHARGE_NAME=utilBean.getCHARGE_NAME();
			VCHARGE_ABBR=utilBean.getCHARGE_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getClosureOrOpenReqCounterpartyList(String contReq)
	{
		String function_nm="getClosureOrOpenReqCounterpartyList()";
		try
		{
			/*
		 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
			CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
			CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
		 */
			int ctn =0;
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? ";
			if(contReq.equals("Closure"))
			{
				queryString+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
			}
			else
			{
				queryString+="AND CLOSURE_REQUEST_FLAG IN (?) ";
			}
			queryString+="AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="AND CONTRACT_TYPE IN (?,?,?)";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND CONTRACT_TYPE IN (?,?) ";
			if(contReq.equals("Closure"))
			{
				queryString+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
			}
			else
			{
				queryString+= "AND CLOSURE_REQUEST_FLAG IN (?) ";
			}
			queryString+= "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn,comp_cd);
			if(contReq.equals("Closure"))
			{
				stmt.setString(++ctn,"Y");
				stmt.setString(++ctn,"R");
			}
			else
			{
				stmt.setString(++ctn,contReq);
			}
			if(callFlag.equalsIgnoreCase("DLNG_CONT_CLOSURE_REQUEST") || callFlag.equalsIgnoreCase("DLNG_CONT_REOPEN_REQUEST"))
			{
				stmt.setString(++ctn,"F");
				stmt.setString(++ctn,"E");
				stmt.setString(++ctn,"W");
			}
			else
			{
				stmt.setString(++ctn,"S");
				stmt.setString(++ctn,"L");
				stmt.setString(++ctn,"X");
			}
			stmt.setString(++ctn,comp_cd);
			stmt.setString(++ctn,"C");
			stmt.setString(++ctn,"A");
			stmt.setString(++ctn,"O");
			stmt.setString(++ctn,"Q");
			if(contReq.equals("Closure"))
			{
				stmt.setString(++ctn,"Y");
				stmt.setString(++ctn,"R");
			}
			else
			{
				stmt.setString(++ctn,"O");
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getClosureOrReopenRequest(String contStat)
	{
		String function_nm="getClosureOrReopenRequest()";
		try
		{
			/*
		 	NOTE:  THE FOLLOWING IS USED AS FLAGS FOR CONTRACT CLOSE AND REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'Y'	-> CLOSURE REQUEST
			CLOSURE_REQUEST_FLAG = 'N' 	-> CLOSURE REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'A' 	-> CLOSURE REQUEST APPROVED
			CLOSURE_REQUEST_FLAG = 'O' 	-> CONTRACT REOPEN REQUEST
			CLOSURE_REQUEST_FLAG = 'X'	-> CONTRACT REOPEN REQUEST REJECTED
			CLOSURE_REQUEST_FLAG = 'R'	-> TERMINATE REQUEST GENRATED
			*/
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("S"))
				{
					VTEMP_SEGMENT.add("Supply Notice");
				}
				else if(segmentType.equals("L"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement");
				}
				else if(segmentType.equals("X"))
				{
					VTEMP_SEGMENT.add("IGX");
				}
				//added for DLNG
				else if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("Supply Notice (DLNG)");
				}
				else if(segmentType.equals("E"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement (DLNG)");
				}
				else if(segmentType.equals("W"))
				{
					VTEMP_SEGMENT.add("IGX (DLNG)");
				}
				else if(segmentType.equals("O"))
				{
					VTEMP_SEGMENT.add("LTCORA (CN)");
				}
				else if(segmentType.equals("Q"))
				{
					VTEMP_SEGMENT.add("LTCORA (Period)");
				}
				else
				{
					VTEMP_SEGMENT.add("");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				int ctn=0;
				int index=0;
				String queryString1="SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,CONT_REF_NO,TRADE_REF_NO,"
						+ "RATE,RATE_UNIT,TCQ,AGMT_BASE,TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'), "
						+ "CASE WHEN TO_DATE(END_DT,'DD/MM/YYYY')<TO_DATE(SYSDATE,'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRED,CLOSURE_ALLOC_QTY, "
						+ "CLOSURE_REQUEST_FLAG,CLOSURE_REMARK,NULL,AGMT_TYPE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? ";
				if(contStat.equals("Closure"))
				{
					queryString1+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
				}
				else
				{
					queryString1+="AND CLOSURE_REQUEST_FLAG IN (?) ";
				}
				queryString1+= "AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString1 += "AND COUNTERPARTY_CD=? ";
				}
				queryString1+="UNION ";
				queryString1+="SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,CONT_REF_NO,NULL,"
						+ "LTCORA_TARIFF,LTCORA_TARIFF_UNIT,NULL,AGMT_BASE,TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'),"
						+ "CASE WHEN TO_DATE(END_DT,'DD/MM/YYYY')<TO_DATE(SYSDATE,'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRED,CLOSURE_ALLOC_QTY,"
						+ "CLOSURE_REQUEST_FLAG,CLOSURE_REMARK,BUY_SALE,AGMT_TYPE "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? ";
				if(contStat.equals("Closure"))
				{
					queryString1+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
				}
				else
				{
					queryString1+="AND CLOSURE_REQUEST_FLAG IN (?) ";
				}
				queryString1+="AND CONTRACT_TYPE=? AND Agmt_Type=? AND BUY_SALE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString1 += "AND COUNTERPARTY_CD=? ";
				}
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(++ctn,comp_cd);
				if(contStat.equals("Closure"))
				{
					stmt1.setString(++ctn,"Y");
					stmt1.setString(++ctn,"R");
				}
				else
				{
					stmt1.setString(++ctn,contStat);
				}
				stmt1.setString(++ctn,""+VTEMP_SEGMENT_TYPE.elementAt(i));
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt1.setString(++ctn,counterparty_cd);
				}
				stmt1.setString(++ctn,comp_cd);
				if(contStat.equals("Closure"))
				{
					stmt1.setString(++ctn,"Y");
					stmt1.setString(++ctn,"R");
				}
				else
				{
					stmt1.setString(++ctn,contStat);
				}
				stmt1.setString(++ctn,""+VTEMP_SEGMENT_TYPE.elementAt(i));
				stmt1.setString(++ctn,"A");
				stmt1.setString(++ctn,"C");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt1.setString(++ctn,counterparty_cd);
				}
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String agmt=rset1.getString(2)==null?"":rset1.getString(2);
					String agmt_rev=rset1.getString(3)==null?"0":rset1.getString(3);
					String cont=rset1.getString(4)==null?"0":rset1.getString(4);
					String cont_rev=rset1.getString(5)==null?"0":rset1.getString(5);
					String cont_type = ""+VTEMP_SEGMENT_TYPE.elementAt(i);
					
					String cont_ref=rset1.getString(9)==null?"":rset1.getString(9);
					String trade_ref=rset1.getString(10)==null?"":rset1.getString(10);
					String start_dt =rset1.getString(6)==null?"":rset1.getString(6);
					String end_dt=rset1.getString(7)==null?"":rset1.getString(7);
					String agmt_base = rset1.getString(14)==null?"":rset1.getString(14);
					String tcq = nf.format(rset1.getDouble(13));
					
					String supplied = allocUtil.getBestSupplyAllocationQty(conn, comp_cd, countpty_cd, agmt, cont, cont_type,start_dt,end_dt,agmt_base,"0");
					String close_dt = rset1.getString(15)==null?"":rset1.getString(15);
					String is_expired = rset1.getString(16)==null?"":rset1.getString(16);
					String closure_qty = rset1.getString(17)==null?"0":rset1.getString(17);				
					String closure_req_flag = rset1.getString(18)==null?"":rset1.getString(18);				
					String closure_remark = rset1.getString(19)==null?"":rset1.getString(19);				
					String buy_sale = rset1.getString(20)==null?"":rset1.getString(20);
					String agmt_type = rset1.getString(21)==null?"":rset1.getString(21);
					
					VAGMT_TYPE.add(agmt_type);
					VBUY_SALE.add(buy_sale);
					if(closure_remark.equals(""))
					{
						String msg="";
						if(is_expired.equals("Y"))
						{
							msg="Closing deal due to expiration.";
						}
						VREMARK.add(msg);
					}
					else
					{
						VREMARK.add(closure_remark);
					}
					
					
					VCLOSURE_REQ_FLAG.add(closure_req_flag);
					VSUPPLIED_MMBTU.add(supplied);
					if(cont_type.equals("X"))
					{
						cont_ref=trade_ref;
					}
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
					VSTART_DT.add(rset1.getString(6)==null?"":rset1.getString(6));
					VEND_DT.add(rset1.getString(7)==null?"":rset1.getString(7));
					VCONT_REF_NO.add(cont_ref);
					
					//String dealMapping=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String dealMapping=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					/*
					 * if(agmt_base.equals("D")) {
					 * dealMapping+=" <font style='background: #a6ff4d;'>[DLV]</font>"; }
					 */
					if(agmt_base.equals("D"))
					{
						VDLV.add(" <font style='background: #a6ff4d;'>[DLV]</font>");
					}
					else
					{
						VDLV.add("");
					}
					VDIS_CONT_MAPPING.add(dealMapping);
					
					String cont_status_flg=rset1.getString(8)==null?"":rset1.getString(8);
					VCONT_STATUS_FLG.add(cont_status_flg);
					VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
					
					double rate=rset1.getDouble(11);
					String rateCd=rset1.getString(12)==null?"":rset1.getString(12);
					String sales_price=utilBean.RateNumberFormat(rate, rateCd);
					
					String rateFor = sales_price+" ("+utilBean.getRateUnitNm(conn,rateCd)+"/MMBTU)";
					VRATE_FORMULA.add(rateFor);
					VRATE.add(sales_price);
					VRATE_UNIT.add(rateCd);
					VRATE_UNIT_NM.add(""+utilBean.getRateUnitNm(conn,rateCd));
					VPRICE_TYPE.add("Fixed");
					VTCQ.add(nf.format(rset1.getDouble(13)));
					VCLOSURE_DT.add(close_dt);
					
					if(contStat.equals("O"))
					{
						String clsr_sign = Double.parseDouble(closure_qty)<0?"+":"-";
						closure_qty = Double.parseDouble(closure_qty)<0?""+(-1*Double.parseDouble(closure_qty)):closure_qty;
						VTCQ_SIGN.add(clsr_sign);
						VVAR_TCQ_QTY.add(closure_qty);
					}
					else if(contStat.equals("Closure"))
					{
						double diff = rset1.getDouble(13) - Double.parseDouble(supplied);
						String tcq_sign=diff>0?"-":"+";
						diff = diff<0?diff*-1:diff;
						VTCQ_SIGN.add(tcq_sign);
						VVAR_TCQ_QTY.add(nf.format(diff));
					}
					VSIGNING_DT.add("");
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//PB 20250830: FOR CHECKING THE NOMINATION BU_UNIT
	public void getNominatedChk()
	{
		String function_nm="getNomintatedChk()";
		try
		{
			for(int i=0;i<VBU_PLANT_SEQ_NO.size();i++)
			{
				VNOM_SEL_BU_CHK.add(checkBUNominated(""+VBU_PLANT_SEQ_NO.elementAt(i)));
			}
			for(int i=0;i<VTRANS_CD.size();i++)
			{
				//VTRANS_CD=utilBean.getTRANS_CD();
				//VTRANS_PLANT_SEQ_NO=utilBean.getTRANS_PLANT_SEQ_NO();
				VNOM_SEL_TRANS_CHK.add(checkTranspPlantNominated(""+VTRANS_CD.elementAt(i),""+VTRANS_PLANT_SEQ_NO.elementAt(i)));
			}
			for(int i=0;i<VPLANT_SEQ_NO.size();i++)
			{
				VNOM_SEL_CUST_CHK.add(checkCustPlantNominated(""+VPLANT_SEQ_NO.elementAt(i)));
			}
			//for dlng truck transporter
			for(int i=0;i<VTRUCK_TRANS_CD.size();i++)
			{
				VNOM_SEL_TRUCK_TRANS_CHK.add(checkTruckTransporterNominated(""+VTRUCK_TRANS_CD.elementAt(i)));
			}
			//for dlng filling station
			for(int i=0;i<VFILL_STATION_CD.size(); i++)
			{
				VNOM_SEL_FILL_CHK.add(checkFillingStationNominated(""+VFILL_STATION_CD.elementAt(i)));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String checkCustPlantNominated(String plant_seq)
	{
		String function_nm="checkCustPlantNominated()";
		String chk_cust_plant="";
		try
		{
			String query="";
			if(callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST"))
			{
				query="SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
						+ "FROM FMS_DAILY_BUYER_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? ";
			}
			else
			{
				query="SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
						+ "FROM FMS_DLNG_BUYER_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND CARGO_NO='0' ";
			}
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, plant_seq);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				chk_cust_plant=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_cust_plant;
	}
	
	public String checkTranspPlantNominated(String transporter_cd, String plant_seq)
	{
		String function_nm="checkTranspPlantNominated()";
		String chk_transptr="";
		try
		{
			String query="SELECT CASE WHEN TRANSPORTER_CD=? AND TRANS_SEQ=? THEN 'Y' ELSE 'N'END TRANSPORTER_UNIT "
					+ "FROM FMS_DAILY_BUYER_NOM "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND TRANSPORTER_CD=? AND TRANS_SEQ=? ";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, transporter_cd);
			stmt_temp.setString(2, plant_seq);
			stmt_temp.setString(3, comp_cd);
			stmt_temp.setString(4, counterparty_cd);
			stmt_temp.setString(5, agmt_no);
			stmt_temp.setString(6, cont_no);
			stmt_temp.setString(7, contract_type);
			stmt_temp.setString(8, transporter_cd);
			stmt_temp.setString(9, plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				chk_transptr=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_transptr;
	}
	
	public String checkBUNominated(String bu_seq)
	{
		String function_nm="checkBUNominated()";
		String chk_bu="";
		try
		{
			String query="";
			if(callFlag.equalsIgnoreCase("SUPPLY_CONTRACT_MST"))
			{
				query="SELECT CASE WHEN BU_SEQ=? THEN 'Y' ELSE 'N'END BU_UNIT "
						+ "FROM FMS_DAILY_BUYER_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND BU_SEQ=? ";
			}
			else
			{
				query="SELECT CASE WHEN BU_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
						+ "FROM FMS_DLNG_BUYER_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND BU_SEQ=? AND CARGO_NO='0' ";
			}
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, bu_seq);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, bu_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				chk_bu=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_bu;
	}
	
	public String checkTruckTransporterNominated(String trans_cd)
	{
		String function_nm="checkTruckTransporterNominated()";
		String chk_trans_nom="";
		try
		{
			String query="SELECT CASE WHEN TRUCK_TRANS_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_BUYER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? " 
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND TRUCK_TRANS_CD=? AND CARGO_NO=? ";
			query+="UNION ";
			query+="SELECT CASE WHEN TRUCK_TRANS_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? " 
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND TRUCK_TRANS_CD=? AND CARGO_NO=? ";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, trans_cd);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, trans_cd);
			stmt_temp.setString(8, "0");
			stmt_temp.setString(9, trans_cd);
			stmt_temp.setString(10, comp_cd);
			stmt_temp.setString(11, counterparty_cd);
			stmt_temp.setString(12, agmt_no);
			stmt_temp.setString(13, cont_no);
			stmt_temp.setString(14, contract_type);
			stmt_temp.setString(15, trans_cd);
			stmt_temp.setString(16, "0");
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				chk_trans_nom=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_trans_nom;
	}
	
	public String checkFillingStationNominated(String filling_station_cd)
	{
		String function_nm="checkFillingStationNominated()";
		String chk_fill_nom="";
		try
		{
			String query="SELECT CASE WHEN FILL_STATION_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_BUYER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND FILL_STATION_CD=? AND CARGO_NO=? ";
			query+="UNION ";
			query+="SELECT CASE WHEN FILL_STATION_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND FILL_STATION_CD=? AND CARGO_NO=?  ";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, filling_station_cd);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, filling_station_cd);
			stmt_temp.setString(8, "0");
			stmt_temp.setString(9, filling_station_cd);
			stmt_temp.setString(10, comp_cd);
			stmt_temp.setString(11, counterparty_cd);
			stmt_temp.setString(12, agmt_no);
			stmt_temp.setString(13, cont_no);
			stmt_temp.setString(14, contract_type);
			stmt_temp.setString(15, filling_station_cd);
			stmt_temp.setString(16, "0");
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				chk_fill_nom=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_fill_nom;
	}
	
	public void getFgsaCounterpartyList()
	{
		String function_nm="getFgsaCounterpartyList()";
		try
		{
			String queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_AGMT_MST "
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
	
	public void getSupplyCounterpartyList()
	{
		String function_nm="getSupplyCounterpartyList()";
		try
		{
			String queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_SUPPLY_CONT_MST "
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
	
	String clearance = "";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String cont_start_dt = "";
	String cont_end_dt = "";
	String agreement_type = "";
	String gas_dt="";
	String nomination_freq="";
	String nomination_type="";
	String segmentType = "";
	String segment="";
	
	String display_msg ="";
	String max_end_dt ="";
	
	String active_status="";
	String from_dt="";
	String to_dt="";
	
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setCont_end_dt(String cont_end_dt) {this.cont_end_dt = cont_end_dt;}
	public void setAgreement_type(String agreement_type) {this.agreement_type = agreement_type;}
	public void setGas_dt(String gas_dt) {this.gas_dt = gas_dt;}
	public void setNomination_freq(String nomination_freq) {this.nomination_freq = nomination_freq;}
	public void setNomination_type(String nomination_type) {this.nomination_type = nomination_type;}
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setSegment(String segment) {this.segment = segment;}
	
	public void setActive_status(String active_status) {this.active_status = active_status;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VTRANS_CD = new Vector();
	Vector VTRUCK_TRANS_CD = new Vector();
	Vector VTRUCK_TRANS_ABBR = new Vector();
	Vector VTRUCK_TRANS_NM = new Vector();
	Vector VFILL_STATION_CD = new Vector();
	Vector VFILL_STATION_ABBR = new Vector();
	Vector VTRANS_PLANT_NM = new Vector();
	Vector VTRANS_PLANT_ABBR = new Vector();
	Vector VTRANS_PLANT_SEQ_NO = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VGX_BU_CD = new Vector();
	Vector VGX_BU_PLANT_NM = new Vector();
	Vector VGX_BU_PLANT_ABBR = new Vector();
	Vector VGX_BU_PLANT_SEQ_NO = new Vector();
	Vector VBUYER_CD = new Vector();
	Vector VBUYER_ABBR = new Vector();
	Vector VBUYER_NAME = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VTCQ = new Vector();
	Vector VQTY_UNIT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VNEW_START_DT = new Vector();
	Vector VNEW_END_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRANS_CD = new Vector();
	Vector VSEL_TRANS_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRUCK_TRANS_CD = new Vector();
	Vector VSEL_TRUCK_TRANS_ABBR = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_GX_BU_CD = new Vector();
	Vector VSEL_GX_BU_PLANT_SEQ_NO = new Vector();
	Vector VTEMP_TRANS_CD = new Vector();
	Vector VTEMP_TRANS_ABBR = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VBOOKED_QTY = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VFCC_FLAG = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VMIN_ALLOC_DT = new Vector();
	Vector VMAX_ALLOC_DT = new Vector();
	Vector VUNLOADED_QTY = new Vector();
	Vector VAVAILABLE_FOR_SALE = new Vector();
	Vector VSEL_TRAD_CD = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_TRANS_PLANT_ABBR = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VSEL_FILL_STATION_CD = new Vector();
	Vector VSEL_FILL_STATION_ABBR = new Vector();
	Vector VFORMULA_ID = new Vector();
	Vector VFORMULA_NM = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VTAX_PLANT_NAME = new Vector();
	Vector VTAX_BU_PLANT_NM = new Vector();
	Vector VTAX_PLANT_SEQ = new Vector();
	Vector VTAX_BU_PLANT_SEQ = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();

	Vector VAGMT_BASE = new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VRATE_FORMULA = new Vector();
	Vector VIS_ALLOCATED = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VFROM_DT = new Vector();
	Vector VTO_DT = new Vector();
	Vector VDCQ = new Vector();
	Vector VREMARK = new Vector();
	Vector VSTATUS = new Vector();
	Vector VDEAL_MAPPING = new Vector();
	Vector VSUPPLIED_MMBTU = new Vector();
	//Vector VSEL_TRANS_CHRG = new Vector();
	//Vector VSEL_MARKET_MARGIN = new Vector();
	//Vector VSEL_OTH_CHRG = new Vector();
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VINDEX = new Vector();
	Vector VTCQ_SIGN = new Vector();
	Vector VVAR_TCQ_QTY = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VRATE_UNIT_NM = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	Vector VIS_INV_SUBMITTED = new Vector();
	Vector VMAX_DATE = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VMIN_COUNTERPARTY_EFF_DT = new Vector();
	Vector VDLV = new Vector();
	Vector VCFORM_FLAG = new Vector();
	
	Vector VDISP_AGMT_NO = new Vector();
	Vector VIS_RADIO_ENABLE = new Vector();
	
	Vector VINVOICE_TYPE = new Vector();		//Pratham Bhatt 20240822: for Invoice type
	Vector VINVOICE_CATEGORY = new Vector();	//Pratham Bhatt 20240822: for Invoice category
	
	Vector VCHARGE_ABBR = new Vector();
	Vector VCHARGE_NAME = new Vector();
	Vector VSEL_CHARGE_VALUE=new Vector();
	Vector VSEL_CHARGE_DESC=new Vector();
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VPLANT_SEQ=new Vector();
	Vector VCARGO_NO=new Vector();
	Vector VBOE_NO=new Vector();
	
	Vector VEXPIRED = new Vector();
	Vector VCLOSURE_DT = new Vector();
	Vector VCLOSURE_REQ_FLAG = new Vector();
	Vector VBUY_SALE = new Vector();
	
	Vector VMIN_NOM_DT = new Vector();
	Vector VMAX_DT = new Vector();
	
	Vector VNOM_SEL_BU_CHK = new Vector();
	Vector VNOM_SEL_TRANS_CHK = new Vector();
	Vector VNOM_SEL_CUST_CHK = new Vector();
	Vector VNOM_SEL_TRUCK_TRANS_CHK = new Vector();
	Vector VNOM_SEL_FILL_CHK = new Vector();
	
	public Vector getVNOM_SEL_BU_CHK() {return VNOM_SEL_BU_CHK;}
	public Vector getVNOM_SEL_TRANS_CHK() {return VNOM_SEL_TRANS_CHK;}
	public Vector getVNOM_SEL_CUST_CHK() {return VNOM_SEL_CUST_CHK;}
	public Vector getVNOM_SEL_TRUCK_TRANS_CHK() {return VNOM_SEL_TRUCK_TRANS_CHK;}
	public Vector getVNOM_SEL_FILL_CHK() {return VNOM_SEL_FILL_CHK;}
	
	public Vector getVBUY_SALE() {return VBUY_SALE;}
	public Vector getVEXPIRED() {return VEXPIRED;}
	public Vector getVCLOSURE_DT() {return VCLOSURE_DT;}
	public Vector getVCLOSURE_REQ_FLAG() {return VCLOSURE_REQ_FLAG;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVTRANS_CD() {return VTRANS_CD;}
	public Vector getVTRUCK_TRANS_CD() {return VTRUCK_TRANS_CD;}
	public Vector getVTRUCK_TRANS_ABBR() {return VTRUCK_TRANS_ABBR;}
	public Vector getVTRUCK_TRANS_NM() {return VTRUCK_TRANS_NM;}
	public Vector getVFILL_STATION_CD() {return VFILL_STATION_CD;}
	public Vector getVFILL_STATION_ABBR() {return VFILL_STATION_ABBR;}
	public Vector getVTRANS_PLANT_NM() {return VTRANS_PLANT_NM;}
	public Vector getVTRANS_PLANT_ABBR() {return VTRANS_PLANT_ABBR;}
	public Vector getVTRANS_PLANT_SEQ_NO() {return VTRANS_PLANT_SEQ_NO;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVGX_BU_CD() {return VGX_BU_CD;}
	public Vector getVGX_BU_PLANT_NM() {return VGX_BU_PLANT_NM;}
	public Vector getVGX_BU_PLANT_ABBR() {return VGX_BU_PLANT_ABBR;}
	public Vector getVGX_BU_PLANT_SEQ_NO() {return VGX_BU_PLANT_SEQ_NO;}
	public Vector getVBUYER_CD() {return VBUYER_CD;}
	public Vector getVBUYER_ABBR() {return VBUYER_ABBR;}
	public Vector getVBUYER_NAME() {return VBUYER_NAME;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVNEW_START_DT() {return VNEW_START_DT;}
	public Vector getVNEW_END_DT() {return VNEW_END_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_TRANS_CD() {return VSEL_TRANS_CD;}
	public Vector getVSEL_TRANS_PLANT_SEQ_NO() {return VSEL_TRANS_PLANT_SEQ_NO;}
	public Vector getVSEL_TRUCK_TRANS_CD() {return VSEL_TRUCK_TRANS_CD;}
	public Vector getVSEL_TRUCK_TRANS_ABBR() {return VSEL_TRUCK_TRANS_ABBR;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_GX_BU_CD() {return VSEL_GX_BU_CD;}
	public Vector getVSEL_GX_BU_PLANT_SEQ_NO() {return VSEL_GX_BU_PLANT_SEQ_NO;}
	public Vector getVTEMP_TRANS_CD() {return VTEMP_TRANS_CD;}
	public Vector getVTEMP_TRANS_ABBR() {return VTEMP_TRANS_ABBR;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVBOOKED_QTY() {return VBOOKED_QTY;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVFCC_FLAG() {return VFCC_FLAG;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVMIN_ALLOC_DT() {return VMIN_ALLOC_DT;}
	public Vector getVMAX_ALLOC_DT() {return VMAX_ALLOC_DT;}
	public Vector getVUNLOADED_QTY() {return VUNLOADED_QTY;}
	public Vector getVAVAILABLE_FOR_SALE() {return VAVAILABLE_FOR_SALE;}
	public Vector getVSEL_TRAD_CD() {return VSEL_TRAD_CD;}
	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_TRANS_PLANT_ABBR() {return VSEL_TRANS_PLANT_ABBR;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVSEL_FILL_STATION_CD() {return VSEL_FILL_STATION_CD;}
	public Vector getVSEL_FILL_STATION_ABBR() {return VSEL_FILL_STATION_ABBR;}
	public Vector getVFORMULA_ID() {return VFORMULA_ID;}
	public Vector getVFORMULA_NM() {return VFORMULA_NM;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVTAX_PLANT_NAME() {return VTAX_PLANT_NAME;}
	public Vector getVTAX_BU_PLANT_NM() {return VTAX_BU_PLANT_NM;}
	public Vector getVTAX_PLANT_SEQ() {return VTAX_PLANT_SEQ;}
	public Vector getVTAX_BU_PLANT_SEQ() {return VTAX_BU_PLANT_SEQ;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVRATE_FORMULA() {return VRATE_FORMULA;}
	public Vector getVIS_ALLOCATED() {return VIS_ALLOCATED;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVFROM_DT() {return VFROM_DT;}
	public Vector getVTO_DT() {return VTO_DT;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	public Vector getVSUPPLIED_MMBTU() {return VSUPPLIED_MMBTU;}
	//public Vector getVSEL_TRANS_CHRG() {return VSEL_TRANS_CHRG;}
	//public Vector getVSEL_MARKET_MARGIN() {return VSEL_MARKET_MARGIN;}
	//public Vector getVSEL_OTH_CHRG() {return VSEL_OTH_CHRG;}
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTCQ_SIGN() {return VTCQ_SIGN;}
	public Vector getVVAR_TCQ_QTY() {return VVAR_TCQ_QTY;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVRATE_UNIT_NM() {return VRATE_UNIT_NM;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	public Vector getVIS_INV_SUBMITTED() {return VIS_INV_SUBMITTED;}
	public Vector getVMAX_DATE() {return VMAX_DATE;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVMIN_COUNTERPARTY_EFF_DT() {return VMIN_COUNTERPARTY_EFF_DT;}
	
	public Vector getVDLV() {return VDLV;}
	public Vector getVCFORM_FLAG() {return VCFORM_FLAG;}
	
	public Vector getVDISP_AGMT_NO() {return VDISP_AGMT_NO;}
	public Vector getVIS_RADIO_ENABLE() {return VIS_RADIO_ENABLE;}
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}			//Pratham Bhatt 20240822:Getter function of Invoice Type
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;} 	//Pratham Bhatt 20240822: Getter function of Invoice Category
	
	public Vector getVCHARGE_ABBR() {return VCHARGE_ABBR;}
	public Vector getVCHARGE_NAME() {return VCHARGE_NAME;}
	public Vector getVSEL_CHARGE_VALUE() {return VSEL_CHARGE_VALUE;}
	public Vector getVSEL_CHARGE_DESC() {return VSEL_CHARGE_DESC;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVBOE_NO() {return VBOE_NO;}
	
	public Vector getVMAX_DT() {return VMAX_DT;}
	public Vector getVMIN_NOM_DT() {return VMIN_NOM_DT;}
	
	String min_counterparty_eff_dt = "";
	//String cont_no = "";
	//String cont_rev_no = "";
	String cont_ref_no = "";
	String trade_ref_no = "";
	String signing_dt = "";
	String signing_time = "";
	String dda_dt = "";
	String dda_time = "";
	String ent_dt = "";
	String ent_time = "";
	String start_dt = "";
	String end_dt = "";
	String agmt_agmt_base = "";
	String agmt_base = "";
	String agmt_type = "";
	String tcq = "";
	String dcq = "";
	String quantity_unit = "";
	String rate = "";
	String rate_unit = "";
	String post_margin = "";
	String transportation_charges = "";
	String txn_charges = "";
	String txn_unit = "";
	String split_flag = "";
	String split_type = "";
	String buy_nom_flag = "";
	String buy_fortnightly_nom = "";
	String buy_month_nom = "";
	String buy_week_nom = "";
	String buy_daily_nom = "";
	String buy_nom_cutoff_time = "";
	String sell_nom_flag = "";
	String sell_month_nom = "";
	String sell_fortnightly_nom = "";
	String sell_week_nom = "";
	String sell_daily_nom = "";
	String day_def_flag = "";
	String day_start_time = "";
	String day_end_time = "";
	String mdcq_flag = "";
	String mdcq_percentage = "";
	String remark = "";
	String cont_name = "";
	String contpty_abbr="";
	String fcc_flg="";
	String cont_status="";
	String cont_status_flg="";
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
	String renewal_dt="";
	String buy_nom_clause="";
	String sell_nom_clause="";
	String status="";
	String status_nm="";
	String bill_flag="";
	String rev_dt="";
	String is_allocated="N";
	String due_dt_in="";
	String exclude_sat="";
	String no_of_billing_dtl="";
	String no_of_security_dtl="";
	String eff_dt="";
	String billing_days="";
	String exchg_val="";
	String supplied_qty="";
	String gt_contract_dtl="";
	String tcq_request_flag="";
	String var_tcq="";
	String tcq_sign="";
	String price_change_request_flag="";
	String price_change_history="";
	String gx_counterparty_cd = "";
	String contract_offset_qty = "";
	String contdt_change_request_flag="";
	String is_inv_submitted="";
	String old_eff_dt="";
	String max_date="";
	String min_nom_date="";
	String balance_qty="";
	String delta_tcq_sign="";
	String closure_note="";
	
	String no_of_liability_dtl = "";
	String liab_lq_damg = "";
	String ld_price_per = "";
	String ld_promise = "";
	String ld_low_per = "";
	String ld_chk = "";
	String liab_take_pay = "";
	String top_price_per = "";
	String top_promise = "";
	String top_per = "";
	String top_chk = "";
	String top_obligation = "";
	String top_remark = "";
	String liab_makeup = "";
	String mug_price_per = "";
	String mug_period_per = "";
	String recovery_day = "";
	String mug_remark = "";
	String ld_from = "";
	String ld_to = "";
	String top_from = "";
	String top_to = "";
	
	String billing_clause = "";
	String messurment_flag = "";
	String meas_clause = "";
	String meas_std = "";
	String meas_temp = "";
	String pressure_min_bar = "";
	String pressure_max_bar = "";
	String off_spec_gas_flag = "";
	String spec_gas_eng_base = "";
	String spec_clause="";
	String spec_min_eng = "";
	String spec_max_eng = "";
	String liability_flag = "";
	String liability_clause = "";
	String liability_lq_dmg = "";
	String liability_take_pay = "";
	String liability_makeup = "";
	String termination_flag = "";
	String termination_clause = "";
	String termination_planned = "";
	String termination_forced = "";
	String adv_adjust="";
	
	String agmt_signing_dt = "";
	String agmt_start_dt = "";
	String agmt_end_dt = "";
	String dealMapping="";
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	String couterparty_abbr="";
	String display_map_id="";
	String disp_agmt_no="";
	String mapped_cont_no="";
	String contpty_name="";
	String closure_eff_dt="";
	String closure_request_flag="";
	String is_expired="";
	String reopen_request_flg="";
	String reopen_approval_flag="";
	
	public String getReopen_approval_flag() {return reopen_approval_flag;}
	public String getReopen_request_flg() {return reopen_request_flg;}
	public String getIs_expired() {return is_expired;}
	public String getClosure_request_flag() {return closure_request_flag;}
	public String getClosure_eff_dt() {return closure_eff_dt;}
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	//public String getCont_no() {return cont_no;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getTrade_ref_no() {return trade_ref_no;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getEnt_dt() {return ent_dt;}
	public String getEnt_time() {return ent_time;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getAgmt_agmt_base() {return agmt_agmt_base;}
	public String getAgmt_base() {return agmt_base;}
	public String getAgmt_type() {return agmt_type;}
	public String getTcq() {return tcq;}
	public String getDcq() {return dcq;}
	public String getQuantity_unit() {return quantity_unit;}
	public String getRate() {return rate;}
	public String getRate_unit() {return rate_unit;}
	public String getPost_margin() {return post_margin;}
	public String getTransportation_charges() {return transportation_charges;}
	public String getTxn_charges() {return txn_charges;}
	public String getTxn_unit() {return txn_unit;}
	public String getSplit_flag() {return split_flag;}
	public String getSplit_type() {return split_type;}
	public String getBuy_nom_flag() {return buy_nom_flag;}
	public String getBuy_month_nom() {return buy_month_nom;}
	public String getBuy_fortnightly_nom() {return buy_fortnightly_nom;}
	public String getBuy_week_nom() {return buy_week_nom;}
	public String getBuy_daily_nom() {return buy_daily_nom;}
	public String getBuy_nom_cutoff_time() {return buy_nom_cutoff_time;}
	public String getSell_nom_flag() {return sell_nom_flag;}
	public String getSell_month_nom() {return sell_month_nom;}
	public String getSell_fortnightly_nom() {return sell_fortnightly_nom;}
	public String getSell_week_nom() {return sell_week_nom;}
	public String getSell_daily_nom() {return sell_daily_nom;}
	public String getDay_def_flag() {return day_def_flag;}
	public String getDay_start_time() {return day_start_time;}
	public String getDay_end_time() {return day_end_time;}
	public String getMdcq_flag() {return mdcq_flag;}
	public String getMdcq_percentage() {return mdcq_percentage;}
	public String getRemark() {return remark;}
	public String getCont_name() {return cont_name;}
	public String getContpty_abbr() {return contpty_abbr;}
	public String getFcc_flg() {return fcc_flg;}
	public String getCont_status() {return cont_status;}
	public String getCont_status_flg() {return cont_status_flg;}
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
	public String getRenewal_dt() {return renewal_dt;}
	public String getBuy_nom_clause() {return buy_nom_clause;}
	public String getSell_nom_clause() {return sell_nom_clause;}
	public String getStatus() {return status;}
	public String getStatus_nm() {return status_nm;}
	public String getBill_flag() {return bill_flag;}
	public String getRev_dt() {return rev_dt;}
	public String getIs_allocated() {return is_allocated;}
	public String getDue_dt_in() {return due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
	public String getNo_of_security_dtl() {return no_of_security_dtl;}
	public String getEff_dt() {return eff_dt;}
	public String getBilling_days() {return billing_days;}
	public String getExchg_val() {return exchg_val;}
	public String getSupplied_qty() {return supplied_qty;}
	public String getGt_contract_dtl() {return gt_contract_dtl;}
	public String getTcq_request_flag() {return tcq_request_flag;}
	public String getVar_tcq() {return var_tcq;}
	public String getTcq_sign() {return tcq_sign;}
	public String getPrice_change_request_flag() {return price_change_request_flag;}
	public String getPrice_change_history() {return price_change_history;}
	public String getGx_counterparty_cd() {return gx_counterparty_cd;}
	public String getContract_offset_qty() {return contract_offset_qty;}
	public String getContdt_change_request_flag() {return contdt_change_request_flag;}
	public String getIs_inv_submitted() {return is_inv_submitted;}
	public String getOld_eff_dt() {return old_eff_dt;}
	public String getMax_date() {return max_date;}
	public String getMin_nom_date() {return min_nom_date;}
	public String getBalance_qty() {return balance_qty;}
	public String getDelta_tcq_sign() {return delta_tcq_sign;}
	public String getClosure_note() {return closure_note;}
	
	public String getNo_of_liability_dtl() {return no_of_liability_dtl;}
	public String getLiab_lq_damg() {return liab_lq_damg;}
	public String getLd_price_per() {return ld_price_per;}
	public String getLd_promise() {return ld_promise;}
	public String getLd_low_per() {return ld_low_per;}
	public String getLd_chk() {return ld_chk;}
	public String getLiab_take_pay() {return liab_take_pay;}
	public String getTop_price_per() {return top_price_per;}
	public String getTop_promise() {return top_promise;}
	public String getTop_per() {return top_per;}
	public String getTop_chk() {return top_chk;}
	public String getTop_obligation() {return top_obligation;}
	public String getTop_remark() {return top_remark;}
	public String getLiab_makeup() {return liab_makeup;}
	public String getMug_price_per() {return mug_price_per;}
	public String getMug_period_per() {return mug_period_per;}
	public String getRecovery_day() {return recovery_day;}
	public String getMug_remark() {return mug_remark;}
	
	public String getLd_from() {return ld_from;}
	public String getLd_to() {return ld_to;}
	public String getTop_from() {return top_from;}
	public String getTop_to() {return top_to;}
	
	public String getBilling_clause() {return billing_clause;}
	public String getMessurment_flag() { return messurment_flag; }
	public String getMeas_clause() { return meas_clause; }
	public String getMeas_std() { return meas_std; }
	public String getMeas_temp() { return meas_temp; }
	public String getPressure_min_bar() { return pressure_min_bar; }
	public String getPressure_max_bar() { return pressure_max_bar; }
	public String getOff_spec_gas_flag() { return off_spec_gas_flag; }
	public String getSpec_gas_eng_base() { return spec_gas_eng_base; }
	public String getSpec_clause() {return spec_clause;}
	public String getSpec_min_eng() { return spec_min_eng; }
	public String getSpec_max_eng() { return spec_max_eng; }
	public String getLiability_flag() { return liability_flag; }
	public String getLiability_clause() { return liability_clause; }
	public String getLiability_lq_dmg() { return liability_lq_dmg; }
	public String getLiability_take_pay() { return liability_take_pay; }
	public String getLiability_makeup() { return liability_makeup; }
	public String getTermination_flag() {return termination_flag;}
	public String getTermination_clause() {return termination_clause;}
	public String getTermination_planned() {return termination_planned;}
	public String getTermination_forced() {return termination_forced;}
	public String getAdv_adjust() {return adv_adjust;}
	
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
	public String getDealMapping() {return dealMapping;}
	
	public String getDisplay_msg() {return display_msg;}
	public String getMax_end_dt() {return max_end_dt;}
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
	public String getDisp_agmt_no() {return disp_agmt_no;}
	public String getDisplay_map_id() {return display_map_id;}
	public String getCouterparty_abbr() {return couterparty_abbr;}
	public String getMapped_cont_no() {return mapped_cont_no;}
	public String getContpty_name() {return contpty_name;}
}
