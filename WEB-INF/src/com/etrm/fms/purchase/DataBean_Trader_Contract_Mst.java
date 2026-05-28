package com.etrm.fms.purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 11/10/2022 
//Status	  		: Developing
public class DataBean_Trader_Contract_Mst 
{
	String db_src_file_name="DataBean_Trader_Contract_Mst.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
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
	    			if(callFlag.equalsIgnoreCase("TRADER_CONT_MST"))
	    			{
	    				contpty_name = ""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				
	    				if(contract_type.equals("I"))
	    				{
	    					String abbr="IGX";
	    					getGxCounterpartyCd(abbr);
	    					getGasExchangeBuPlantList();
	    				}
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getTraderCounterpartyList();
	    				}
	    				else 
	    				{
	    					getPurchaseContCounterpartyList();
	    				}
	    				getOtherTraderCounterpartyList();
	    				getTraderPlantList();
	    				getOtherTraderPlantList();
	    				//getTransporterPlantList();
	    				getBusinessPlantList();
	    				getChargeMaster();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getTraderContractDetail();
	    					getSelectedTraderPlantList();
	    					getSelectedOtherTraderPlantList();
		    				getSelectedTransporterPlantList();
		    				getSelectedBusinessPlantList();
		    				getContractSelectedGasExchangeBuPlantList();
		    				getLinkTransporterList();
		    				getCountBillingDetail();
		    				getCountSecurityDetail();
		    				getPriceChangeHistory();
		    				getSelectedcontLiabilityDtl();
		    				getMaxAllocationInvoiceDate();
	    				}
	    				getNominatedChk();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRADER_CONTRACT_DCQ_DTL"))
	    			{
	    				counterparty_abbr= utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				getContractDcqDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRADER_CONT_LIST"))
	    			{
	    				getTraderContList();
	    				
	    				if(clearance.equals("KYC"))
	    				{
	    					getTraderCargoList();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRADER_CONT_LIST_FCC"))
	    			{
	    				getTraderContList_FCC();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PURCHASE_SUMMARY"))
		    		{
	    				setDisplaySegment();
		    			getPurchaseSummary();
		    			getCounterpartyList();
		    		}
	    			else if(callFlag.equalsIgnoreCase("TRADER_BILLING_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getCountBillingDetail();
	    				getStateMst();
	    				getSelectedTraderContPlantList();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getBillingDetail();
	    				getApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("NOM_TRADER_CONT_LIST"))
	    			{
	    				getNominationTraderContList();
	    				getNominationTraderCargoList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRADER_CONT_LIABILITY_CLAUSE"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getTraderContLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CLOSURE_REQUEST"))
	    			{
	    				String action = "closure_terminate";
	    				getSegment();
	    				getClosure_Reopen_CounterpartyList(action);
	    				getClosure_Reopen_Dtls(action);
	    			}
	    			else if(callFlag.equalsIgnoreCase("REOPEN_REQUEST"))
	    			{
	    				String action = "reopen";
	    				getSegment();
	    				getClosure_Reopen_CounterpartyList(action);
	    				getClosure_Reopen_Dtls(action);
	    			}
	    			if(callFlag.equalsIgnoreCase("CONTRACT_DURATION_MODIFICATION"))
	    			{
	    				getTraderList();
	    				fetchContractDurationChangeReq();
	    			}
	    			if(callFlag.equalsIgnoreCase("PUR_CTR_MST"))		//PB 20260311- for CTR 
	    			{
	    				getCtrTraderList();
	    				getProductMst();
	    				getPurCtrList();
	    				getActiveContractListForCTR();
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getMaxAllocationInvoiceDate()
	{
		String function_nm="getMaxAllocationInvoiceDate()";
		try
		{
			String queryString1="SELECT TO_CHAR(MIN(MIN_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MIN(GAS_DT) MIN_DATE FROM FMS_BUY_DAILY_BUYER_NOM "
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
			
			int cnt=0;
			String queryString2="SELECT TO_CHAR(MAX(MAX_DATE),'DD/MM/YYYY') "
					+ "FROM (SELECT MAX(GAS_DT) MAX_DATE FROM FMS_BUY_DAILY_ALLOCATION "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
			if(split_flag.equals("Y"))
			{
				for(int i=0; i<VOTH_SEL_TRAD_CD.size(); i++)
				{
					queryString2+= "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "UNION ALL "
					+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
				}
			}
			
			queryString2+= ") ";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(++cnt, comp_cd);
			stmt2.setString(++cnt, counterparty_cd);
			stmt2.setString(++cnt, agmt_no);
			stmt2.setString(++cnt, cont_no);
			stmt2.setString(++cnt, contract_type);
			stmt2.setString(++cnt, comp_cd);
			stmt2.setString(++cnt, counterparty_cd);
			stmt2.setString(++cnt, agmt_no);
			stmt2.setString(++cnt, cont_no);
			stmt2.setString(++cnt, contract_type);
			stmt2.setString(++cnt, comp_cd);
			stmt2.setString(++cnt, counterparty_cd);
			stmt2.setString(++cnt, agmt_no);
			stmt2.setString(++cnt, cont_no);
			stmt2.setString(++cnt, contract_type);
			if(split_flag.equals("Y"))
			{
				for(int i=0; i<VOTH_SEL_TRAD_CD.size(); i++)
				{
					stmt2.setString(++cnt, comp_cd);
					stmt2.setString(++cnt, ""+VOTH_SEL_TRAD_CD.elementAt(i));
					stmt2.setString(++cnt, agmt_no);
					stmt2.setString(++cnt, cont_no);
					stmt2.setString(++cnt, contract_type);
					stmt2.setString(++cnt, comp_cd);
					stmt2.setString(++cnt, ""+VOTH_SEL_TRAD_CD.elementAt(i));
					stmt2.setString(++cnt, agmt_no);
					stmt2.setString(++cnt, cont_no);
					stmt2.setString(++cnt, contract_type);
				}
			}
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
	
	public void getTraderList()
	{
		String function_nm="getTraderList()";

		try
		{
			int ctn=0;
			queryString = "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CHANGE_DATE_REQ=? AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			stmt.setString(++ctn, "D");
			stmt.setString(++ctn, "T");
			stmt.setString(++ctn, "I");
				
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
	
	public void fetchContractDurationChangeReq()
	{
		String function_nm="fetchContractDurationChangeReq()";

		try
		{
			int ctn=0;
			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,RATE, CHANGE_DATE_REQ, CHANGE_DATE_APPROVE, RATE_UNIT,CONTRACT_TYPE,"
					+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(CHANGE_START_DT,'DD/MM/YYYY'),TO_CHAR(CHANGE_END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE A.COUNTERPARTY_CD=? AND CHANGE_DATE_REQ=? AND (CHANGE_DATE_APPROVE=? OR CHANGE_DATE_APPROVE IS NULL) "
					+ "AND A.COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, counterparty_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "D");
			stmt.setString(++ctn, "T");
			stmt.setString(++ctn, "I");
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
				
				String cont_type= rset.getString(13)==null?"":rset.getString(13);
				String cont_ref= rset.getString(14)==null?"":rset.getString(14);
				String trade_ref= rset.getString(15)==null?"":rset.getString(15);
				String change_start_dt= rset.getString(16)==null?"":rset.getString(16);
				String change_end_dt= rset.getString(17)==null?"":rset.getString(17);
				String cont_dtl=utilBean.NewDealMappingId(comp_cd, counterparty_cd, fgsa, fgsa_rev, sn, sn_rev, cont_type, "");
				if (cont_type.equals("I"))
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
				VNEW_START_DT.add(change_start_dt);
				VNEW_END_DT.add(change_end_dt);
				
				String min_nom_dt="";
				String max_dt="";
				String queryString1="SELECT TO_CHAR(MIN(MIN_DATE),'DD/MM/YYYY') "
						+ "FROM (SELECT MIN(GAS_DT) MIN_DATE FROM FMS_BUY_DAILY_BUYER_NOM "
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
						+ "FROM (SELECT MAX(GAS_DT) MAX_DATE FROM FMS_BUY_DAILY_ALLOCATION "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "UNION ALL "
						+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "UNION ALL "
						+ "SELECT MAX(PERIOD_END_DT) MAX_DATE FROM FMS_PUR_PG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?) ";
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
				stmt2.setString(12, counterparty_cd);
				stmt2.setString(13, fgsa);
				stmt2.setString(14, sn);
				stmt2.setString(15, cont_type);
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
	
	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			VDISPLAY_SEGMENT.add("Domestic NG");
			VDISPLAY_SEGMENT.add("In-Tank LNG/RLNG");
			VDISPLAY_SEGMENT.add("IGX");
			VDISPLAY_SEGMENT.add("Ltcora (CN)");
			VDISPLAY_SEGMENT.add("Ltcora (Period)");
			
			VDISPLAY_SEGMENT_TYP.add("D");
			VDISPLAY_SEGMENT_TYP.add("T");
			VDISPLAY_SEGMENT_TYP.add("I");
			VDISPLAY_SEGMENT_TYP.add("G");
			VDISPLAY_SEGMENT_TYP.add("P");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getClosure_Reopen_CounterpartyList(String action)
	{
		String function_nm="getClosure_Reopen_CounterpartyList()";
		try
		{
			int  ctn=0;
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(action.equals("closure_terminate"))
			{
				queryString+="AND CLOSURE_REQUEST_FLAG IN (?,?) ";
			}
			else if(action.equals("reopen"))
			{
				queryString+="AND CLOSURE_REQUEST_FLAG IN (?) ";
			}
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND CONTRACT_TYPE IN (?,?) ";
			if(action.equals("closure_terminate"))
			{
				queryString+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
			}
			else if(action.equals("reopen"))
			{
				queryString+= "AND CLOSURE_REQUEST_FLAG IN (?) ";
			}
			queryString+= "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn,comp_cd);
			if(action.equals("closure_terminate"))
			{
				stmt.setString(++ctn,"Y");
				stmt.setString(++ctn,"R");
			}
			else if(action.equals("reopen"))
			{
				stmt.setString(++ctn,"O");
			}
			stmt.setString(++ctn,comp_cd);
			stmt.setString(++ctn,"T");
			stmt.setString(++ctn,"L");
			stmt.setString(++ctn,"G");
			stmt.setString(++ctn,"P");
			if(action.equals("closure_terminate"))
			{
				stmt.setString(++ctn,"Y");
				stmt.setString(++ctn,"R");
			}
			else if(action.equals("reopen"))
			{
				stmt.setString(++ctn,"O");
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getClosure_Reopen_Dtls(String action)
	{
		String function_nm="getClosure_Reopen_Dtls()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("D"))
				{
					VTEMP_SEGMENT.add("Domestic NG");
				}
				else if(segmentType.equals("T"))
				{
					VTEMP_SEGMENT.add("In-Tank LNG/RLNG");
				}
				else if(segmentType.equals("I"))
				{
					VTEMP_SEGMENT.add("IGX");
				}
				else if(segmentType.equals("G"))
				{
					VTEMP_SEGMENT.add("LTCORA (CN)");
				}
				else if(segmentType.equals("P"))
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
				VTEMP_SEGMENT=VDISPLAY_SEGMENT;
				VTEMP_SEGMENT_TYPE=VDISPLAY_SEGMENT_TYP;
			}
			for(int i=0;i<VTEMP_SEGMENT.size();i++)
			{
				int index=0,ctn=0;
				String queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
						+ "CONT_REF_NO,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,TCQ,CLOSURE_REQUEST_FLAG, "
						+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'N' END,CLOSURE_REMARK,NULL,AGMT_TYPE,TO_CHAR(A.CLOSE_EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				if(action.equals("closure_terminate"))
				{
					queryString+="AND CLOSURE_REQUEST_FLAG IN (?,?) ";
				}
				else if(action.equals("reopen"))
				{
					queryString+="AND CLOSURE_REQUEST_FLAG IN (?) ";
				}
				if(!counterparty_cd.equals("0"))
				{
					queryString+="AND COUNTERPARTY_CD=? ";
				}
				queryString+="UNION ";
				queryString+="SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,CONTRACT_TYPE,"
						+ "CONT_REF_NO,NULL,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),LTCORA_TARIFF,LTCORA_TARIFF_UNIT,NULL,CLOSURE_REQUEST_FLAG,"
						+ "CASE WHEN TO_DATE(END_DT,'DD/MM/YYYY')<TO_DATE(SYSDATE,'DD/MM/YYYY') THEN 'Y' ELSE 'N' END,"
						+ "CLOSURE_REMARK,BUY_SALE,AGMT_TYPE,TO_CHAR(A.CLOSE_EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? ";
				if(action.equals("closure_terminate"))
				{
					queryString+= "AND CLOSURE_REQUEST_FLAG IN (?,?) ";
				}
				else if(action.equals("reopen"))
				{
					queryString+="AND CLOSURE_REQUEST_FLAG IN (?) ";
				}
				queryString+="AND CONTRACT_TYPE=? AND Agmt_Type=? AND BUY_SALE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn,""+VTEMP_SEGMENT_TYPE.elementAt(i));
				if(action.equals("closure_terminate"))
				{
					stmt.setString(++ctn,"Y");
					stmt.setString(++ctn, "R");
				}
				else if(action.equals("reopen"))
				{
					stmt.setString(++ctn, "O");
				}
				
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn,counterparty_cd);
				}
				stmt.setString(++ctn,comp_cd);
				if(action.equals("closure_terminate"))
				{
					stmt.setString(++ctn,"Y");
					stmt.setString(++ctn,"R");
				}
				else if(action.equals("reopen"))
				{
					stmt.setString(++ctn,"O");
				}
				stmt.setString(++ctn,""+VTEMP_SEGMENT_TYPE.elementAt(i));
				stmt.setString(++ctn,"L");
				stmt.setString(++ctn,"T");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn,counterparty_cd);
				}
				rset = stmt.executeQuery();
				while(rset.next())
				{
					index++;
					String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
					String agmt_no = rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev = rset.getString(3)==null?"":rset.getString(3);
					String cont_no = rset.getString(4)==null?"":rset.getString(4);
					String cont_rev = rset.getString(5)==null?"":rset.getString(5);
					String cont_type = rset.getString(6)==null?"":rset.getString(6);
					String cont_ref_no = rset.getString(7)==null?"":rset.getString(7);
					String trade_ref_no = rset.getString(8)==null?"":rset.getString(8);
					String start_dt = rset.getString(9)==null?"":rset.getString(9);
					String end_dt = rset.getString(10)==null?"":rset.getString(10);
					double rate = rset.getDouble(11);
					String rate_unit = rset.getString(12)==null?"":rset.getString(12);
					double tcq = rset.getDouble(13);
					String closure_req_flag = rset.getString(14)==null?"":rset.getString(14);
					String is_expired = rset.getString(15)==null?"":rset.getString(15);
					
					String closure_note="";
					if(action.equals("reopen"))
					{
						closure_note=rset.getString(16)==null?"":rset.getString(16);
					}
					else if(action.equals("closure_terminate"))
					{
						if(is_expired.contentEquals("Y"))
						{
							closure_note="Closing deal due to expiration.";
						}
					}
					String buy_sale = rset.getString(17)==null?"":rset.getString(17);
					String agmt_type = rset.getString(18)==null?"":rset.getString(18);
					String close_eff_dt = rset.getString(19)==null?sysdate:rset.getString(19);
					
					VAGMT_TYPE.add(agmt_type);
					VBUY_SALE.add(buy_sale);
					VCLOSURE_NOTE.add(closure_note);
					VCLOSEURE_EFF_DT.add(close_eff_dt);
					
					String deal_map = utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cargo_no);
					String received_qty=""+allocUtil.getPurchaseAllocationQty(conn,comp_cd, counterpty_cd, agmt_no, cont_no, cont_type,cargo_no);
					
					double bal= tcq-Double.parseDouble(received_qty);
					String sign = bal<0?"-":"+";
					bal = bal<0?-1*bal:bal;
					
					if(cont_type.equals("D") || cont_type.equals("T"))
					{
						VCONT_REF_NO.add(cont_ref_no);
					}
					else
					{
						VCONT_REF_NO.add(trade_ref_no);
					}
					
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
					VDEAL_MAP.add(deal_map);
					VAGMT_NO.add(agmt_no);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont_no);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VTCQ.add(nf.format(tcq));
					VRATE.add(nf.format(rate));
					VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,rate_unit));
					VRECEIVED_QTY.add(received_qty);
					VBALANCE_SIGN.add(sign);
					VBALANCE_QTY.add(bal);
					VPRICE_TYPE.add("Fixed");
					VCLOSURE_REQUEST_FLG.add(closure_req_flag);
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
	
	public void getSelectedTraderContPlantList()
	{
		String function_nm="getSelectedTraderContPlantList()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
				VSELECTED_PLANT_COUNTPTY_CD.add(counterparty_cd);
				VSELECTED_PLANT_ABBR.add(plant_abbr);
				VSELECTED_PLANT_SEQ.add(plant_seq);
				
			}
			rset.close();
			stmt.close();
			
			
			String queryString1="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, cont_no);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, contract_type);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String countpty_cd = rset1.getString(1)==null?"":rset1.getString(1);
				String plant_seq = rset1.getString(2)==null?"":rset1.getString(2);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
				VSELECTED_PLANT_COUNTPTY_CD.add(countpty_cd);
				VSELECTED_PLANT_ABBR.add(plant_abbr);
				VSELECTED_PLANT_SEQ.add(plant_seq);
			}
			rset1.close();
			stmt1.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getSelectedcontLiabilityDtl() 
	{
		String function_nm="getSelectedCnLiabilityDtl()";
		try
		{
			int liability_count=0;
			String queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_TRADER_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
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
	private void getTraderContLiabilityDetails() 
	{
		String function_nm="getTraderContLiabilityDetails()";
		try
		{
			String queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,TO_CHAR(LQ_DAMG_FROM,'DD/MM/YYYY'),TO_CHAR(LQ_DAMG_TO,'DD/MM/YYYY'),TO_CHAR(TAKE_PAY_FROM,'DD/MM/YYYY'),TO_CHAR(TAKE_PAY_TO,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
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
	
	public void getTraderCounterpartyList()
	{
		String function_nm="getTraderCounterpartyList()";
		try
		{
			//utilBean.getAllEntityCounterpartyList(conn,clearance,comp_cd,"T");
			utilBean.getEffectiveEntityCounterpartyList(conn,clearance,comp_cd,"T");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherTraderCounterpartyList()
	{
		String function_nm="getOtherTraderCounterpartyList()";
		try
		{
			String queryString = "SELECT A.COUNTERPARTY_CD,A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR "
					+ "FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND A.COUNTERPARTY_CD!=? AND B.ENTITY='T' AND B.STATUS='A' "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND A.STATUS='Y' "
					+ "ORDER BY COUNTERPARTY_NM";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VOTH_COUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VOTH_COUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VOTH_COUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderPlantList()
	{
		String function_nm="getTraderPlantList()";
		try
		{
			//utilBean.getEffectiveTraderPlantList(counterparty_cd, comp_cd);
			utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "T", comp_cd);
			VPLANT_NM=utilBean.getPLANT_NM();
			VPLANT_ABBR=utilBean.getPLANT_ABBR();
			VPLANT_SEQ_NO=utilBean.getPLANT_SEQ_NO();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherTraderPlantList()
	{
		String function_nm="getOtherTraderPlantList()";
		try
		{
			for(int i=0;i<VOTH_COUNTERPARTY_CD.size();i++)
			{
				int index=0;
				String queryString = "SELECT PLANT_NAME,PLANT_ABBR,SEQ_NO "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
						+ "AND STATUS=? "
						+ "ORDER BY PLANT_NAME";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, ""+VOTH_COUNTERPARTY_CD.elementAt(i));
				stmt.setString(2, "T");
				stmt.setString(3, comp_cd);
				stmt.setString(4, "Y");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					VOTH_PLANT_NM.add(rset.getString(1)==null?"":rset.getString(1));
					VOTH_PLANT_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
					VOTH_PLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
					VOTH_CONTPTY_NM.add(""+VOTH_COUNTERPARTY_CD.elementAt(i));
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
	
	public void getTransporterPlantList()
	{
		//THIS FUNCTION IS NOT USE BUT IT IS COMMENTED IF IN FUTURE MIGHT BE NEEDED
		String function_nm="getTransporterPlantList()";
		try
		{
			/*utilBean.getEffectiveTransporterPlantList(comp_cd);;
			VTRANS_CD=utilBean.getTRANS_CD();
			VTRANS_PLANT_NM=utilBean.getTRANS_PLANT_NM();
			VTRANS_PLANT_ABBR=utilBean.getTRANS_PLANT_ABBR();
			VTRANS_PLANT_SEQ_NO=utilBean.getTRANS_PLANT_SEQ_NO();
			
			queryString="SELECT DISTINCT COUNTERPARTY_CD,COUNTERPARTY_ABBR "
					+ "FROM FMS_COUNTERPARTY_MST WHERE TRANSPORTER=? AND COMPANY_CD=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VTEMP_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VTEMP_TRANS_ABBR.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();*/
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
	
	public void getTraderContractDetail()
	{
		String function_nm="getTraderContractDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			dealMapping=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYPE,TCQ,DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
					+ "POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,FCC_FLAG,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
					+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,TXN_UNIT,"
					+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DAY_DEF_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE "
					+ ",CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'),CLOSURE_REMARK, "		//PB20250418
					+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'N' END IS_EXPIRED,CHANGE_DATE_REQ "	//PB20250418
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					//+ "AND CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND AGMT_REV=? AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
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
				//cont_no=rset.getString(3)==null?"":rset.getString(3);
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
				split_flag=rset.getString(20)==null?"":rset.getString(20);
				split_type=rset.getString(21)==null?"":rset.getString(21);
				buy_nom_flag=rset.getString(22)==null?"":rset.getString(22);
				buy_month_nom=rset.getString(23)==null?"":rset.getString(23);
				buy_week_nom=rset.getString(24)==null?"":rset.getString(24);
				buy_daily_nom=rset.getString(25)==null?"":rset.getString(25);
				sell_nom_flag=rset.getString(26)==null?"":rset.getString(26);
				sell_month_nom=rset.getString(27)==null?"":rset.getString(27);
				sell_week_nom=rset.getString(28)==null?"":rset.getString(28);
				sell_daily_nom=rset.getString(29)==null?"":rset.getString(29);
				day_def_flag=rset.getString(30)==null?"":rset.getString(30);
				day_start_time=rset.getString(31)==null?"":rset.getString(31);
				day_end_time=rset.getString(32)==null?"":rset.getString(32);
				mdcq_flag=rset.getString(33)==null?"":rset.getString(33);
				mdcq_percentage=rset.getString(34)==null?"":rset.getString(34);
				remark=rset.getString(35)==null?"":rset.getString(35);
				String deal_ent_dt=rset.getString(36)==null?"":rset.getString(36);
				cont_name=rset.getString(37)==null?"":rset.getString(37);
				fcc_flg=rset.getString(38)==null?"":rset.getString(38);
				cont_status_flg=rset.getString(39)==null?"":rset.getString(39);
				cont_status=""+ContStatusName(cont_status_flg);
				txn_charges=rset.getString(40)==null?"":rset.getString(40);
				
				buy_fortnightly_nom=rset.getString(41)==null?"":rset.getString(41);
				sell_fortnightly_nom=rset.getString(42)==null?"":rset.getString(42);
				dda_dt=rset.getString(43)==null?"":rset.getString(43);
				dda_time=rset.getString(44)==null?"":rset.getString(44);
				
				txn_unit=rset.getString(45)==null?"1":rset.getString(45);
				if(txn_unit.equals("1"))
				{
					txn_charges=nf.format(rset.getDouble(40));
				}
				else
				{
					txn_charges=nf2.format(rset.getDouble(40));
				}
				
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
				
				messurment_flag = rset.getString(46)==null?"":rset.getString(46); 
				meas_std =rset.getString(47)==null?"":rset.getString(47);
				meas_temp = rset.getString(48)==null?"":rset.getString(48);
				pressure_min_bar = rset.getString(49)==null?"":rset.getString(49);
				pressure_max_bar = rset.getString(50)==null?"":rset.getString(50);
				off_spec_gas_flag = rset.getString(51)==null?"":rset.getString(51);
				spec_gas_eng_base = rset.getString(52)==null?"":rset.getString(52);
				spec_min_eng = rset.getString(53)==null?"":rset.getString(53);
				spec_max_eng = rset.getString(54)==null?"":rset.getString(54);
				liability_flag = rset.getString(55)==null?"":rset.getString(55);
				liability_clause = rset.getString(56)==null?"":rset.getString(56);
				billing_flag = rset.getString(57)==null?"":rset.getString(57);
				billing_clause = rset.getString(58)==null?"":rset.getString(58);
				termination_flag= rset.getString(59)==null?"":rset.getString(59);
				termination_clause = rset.getString(60)==null?"":rset.getString(60);
				termination_planned = rset.getString(61)==null?"":rset.getString(61);
				termination_forced = rset.getString(62)==null?"":rset.getString(62);
				day_def_clause = rset.getString(63)==null?"":rset.getString(63);
				measurement_clause = rset.getString(64)==null?"":rset.getString(64);
				spec_gas_clause = rset.getString(65)==null?"":rset.getString(65);
				closure_req_flag = rset.getString(66)==null?"":rset.getString(66);
				closure_mmbtu = rset.getString(67)==null?"":rset.getString(67);
				closure_eff_dt = rset.getString(68)==null?"":rset.getString(68);
				closure_note = rset.getString(69)==null?"":rset.getString(69);
				is_expired = rset.getString(70)==null?"":rset.getString(70);
				contdt_change_request_flag = rset.getString(71)==null?"":rset.getString(71);
				
				int nom_count=0;
				String queryString1="SELECT COUNT(*) FROM FMS_BUY_DAILY_BUYER_NOM A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_BUY_DAILY_BUYER_NOM B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1,comp_cd);
				stmt1.setString(2,counterparty_cd);
				stmt1.setString(3,agmt_no);
				stmt1.setString(4,agmt_rev_no);
				stmt1.setString(5,cont_no);
				stmt1.setString(6,contract_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					nom_count=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
			
				is_nominated = nom_count>0?"Y":"N";
			}
			rset.close();
			stmt.close();
			
			//GET ALLOCATED QTY
			received_qty=""+allocUtil.getPurchaseAllocationQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type,cargo_no);
			
			//for getting delta tcq sign
			if(!closure_mmbtu.equals(""))
			{
				delta_tcq_sign = Double.parseDouble(closure_mmbtu)<0?"-":"+";
				closure_mmbtu = ""+(Double.parseDouble(closure_mmbtu)<0?(-1*Double.parseDouble(closure_mmbtu)):closure_mmbtu);
			}
			
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
			else if(status_flg.equals("X"))//in inactive(irrespect of date)
			{
				nm="Cancel";
			}
			else if(status_flg.equals("C"))//in inactive(irrespect of date)
			{
				nm="Closed";
			}
			else if(status_flg.equals("R"))
			{
				nm="Re-Opened";
			}
			else if(status_flg.equals("T"))//in inactive(irrespect of date)
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
	
	public void getContractDcqDetail()
	{
		String function_nm="getContractDcqDetail()";
		String sysdate = dateUtil.getSysdate();
		try
		{
			String queryString="SELECT SEQ_NO,TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),DCQ,REMARK,STATUS "
					+ "FROM FMS_TRADER_CONT_DCQ_DTL "
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
	
	public void getTraderContList()
	{
		String function_nm="getTraderContList()";
		try
		{
			/*if(!contract_type.equals("I")) 
			{
				contract_type = "D";
			}*/
			
			String temp_cont="";
			if(contract_type.equals(""))
			{
				if(clearance.equals("IGX"))
				{
					temp_cont="CONTRACT_TYPE IN ('I') ";
				}
				else
				{
					temp_cont="CONTRACT_TYPE IN ('D','T') ";
				}
			}
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "TRADE_REF_NO,CONTRACT_TYPE "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)  "; 
			if(!contract_type.equals(""))
			{
				queryString += "AND CONTRACT_TYPE=? ";
			}
			else
			{
				//queryString += "AND ? ";
				queryString += "AND " +temp_cont ;		//Pratham Bhatt 20250204: For solving invalid relational operator error
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString+="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "TRADE_REF_NO,CONTRACT_TYPE "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)  "; 
				if(!contract_type.equals(""))
				{
					queryString += "AND CONTRACT_TYPE=? ";
				}
				else
				{
					//queryString += "AND ? ";
					queryString += "AND " +temp_cont ;		//Pratham Bhatt 20250204: For solving invalid relational operator error
				}
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS IN ('C','X','T') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND CONT_STATUS NOT IN ('C','X','T') ";
			}
			String temp_query=queryString;
			stmt=conn.prepareStatement(temp_query);
			int st_count=0;
			stmt.setString(++st_count, comp_cd);
			if(!contract_type.equals(""))
			{
				stmt.setString(++st_count, contract_type);
			}
//			else
//			{
//				stmt.setString(++st_count, temp_cont);
//			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++st_count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++st_count, to_dt);
				stmt.setString(++st_count, from_dt);
				
				stmt.setString(++st_count, comp_cd);
				if(!contract_type.equals(""))
				{
					stmt.setString(++st_count, contract_type);
				}
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++st_count, counterparty_cd);
				}
				stmt.setString(++st_count, to_dt);
				stmt.setString(++st_count, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"0":rset.getString(6);
				
				String cont_type=rset.getString(16)==null?"":rset.getString(16);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(contno);
				
				//String disp_cont_no = own_cd+contract_type+countpty_cd+"-"+agmtno+"-"+contno;
				String disp_cont_no = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmt_rev, contno, cont_rev, cont_type, "");
				
				VDISP_CONT_NO.add(disp_cont_no);
				VCONT_REV_NO.add(cont_rev);
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
				String trade_ref=rset.getString(15)==null?"":rset.getString(15);
				if(contract_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				VCONT_REF_NO.add(cont_ref);
				
				VRECEIVED_QTY.add(""+allocUtil.getPurchaseAllocationQty(conn,own_cd, countpty_cd, agmtno, contno, cont_type,cargo_no));
				VCONT_CARGO.add("isCont");
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				VCARGO_NO.add("0");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderCargoList()
	{
		String function_nm="getTraderCargoList()";
		try
		{
			int selCnt= 0;
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,"
					+ "CARGO_NO,CARGO_REF,TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'),EDQ_QTY,CSOC_QTY,SUM(CSOC_QTY),"
					+ "TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'),(COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)),"
					+ "CONTRACT_TYPE "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? "
					+ "AND BUY_SALE=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV ";
					if(!active_status.equals("") && active_status.equals("N"))
					{
						queryString += "AND NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1)) < SYSDATE "
									+ "AND A.ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1))>= TO_DATE(?,'DD/MM/YYYY') ";
					}
					else 
					{
						queryString += "AND NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1)) >= SYSDATE ";
					}
					queryString += "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			queryString += " GROUP BY COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,"
			+ "CARGO_NO, CARGO_REF, ACTUAL_RECPT_DT, EDQ_QTY, CSOC_QTY,ACTUAL_RECPT_DT,STORAGE_DAYS,STORAGE_EXT_DAYS,CONTRACT_TYPE "
			+ "ORDER BY CONT_NO ASC "; 
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, "T");
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++selCnt, to_dt);
				stmt.setString(++selCnt, from_dt);
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++selCnt, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev_no=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev_no=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(15)==null?"0":rset.getString(15);
				String cargo_no = rset.getString(7)==null?"":rset.getString(7);
				String cargo_ref_no = rset.getString(8)==null?"":rset.getString(8);
				
				String disp_cont_no = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmt_rev_no, contno, cont_rev_no, cont_type, cargo_no);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(contno);
				VDISP_CONT_NO.add(disp_cont_no);
				
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				
				VTCQ.add(nf.format(rset.getDouble(12)));
				VQTY_UNIT.add("");
				
				VSTART_DT.add(rset.getString(13)==null?"":rset.getString(13));
				String end_days = rset.getString(14)==null?"":rset.getString(14);
				String end_dt = dateUtil.getDate(rset.getString(13)==null?"":rset.getString(13), end_days);
				VEND_DT.add(end_dt);
				
				String cont_status_flg="";
				VFCC_FLAG.add("");
				
				String cont_ref="";
				String trade_ref="";
				if(contract_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				
				String recev_qty = "";
				
				String queryString4="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1, contno);
				stmt4.setString(2, agmtno);
				stmt4.setString(3, own_cd);
				stmt4.setString(4, countpty_cd);
				stmt4.setString(5, cont_type);
				stmt4.setString(6, cargo_no);
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					recev_qty=nf.format(rset4.getDouble(1));
				}
				stmt4.close();
				rset4.close();
				
				VRECEIVED_QTY.add(""+recev_qty);
				VCARGO_NO.add(cargo_no);
				VCONT_CARGO.add("isCargo");
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE,CONT_STATUS,CONT_NAME "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, own_cd);
				stmt5.setString(2, countpty_cd);
				stmt5.setString(3, agmtno);
				stmt5.setString(4, agmt_rev_no);
				stmt5.setString(5, cont_type);
				stmt5.setString(6, contno);
				rset5=stmt5.executeQuery();
		  		if(rset5.next())
		  		{
		  			VCONT_REF_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
		  			
		  			String mcsoc_per = rset5.getString(2)==null?"":rset5.getString(2);
		  			String cont_status = rset5.getString(3)==null?"":rset5.getString(3);
		  			String contract_name = rset5.getString(4)==null?"":rset5.getString(4);
		  			
		  			VCONT_NAME.add(contract_name);
		  			VCONT_STATUS_FLG.add(cont_status);
					VCONT_STATUS.add(""+ContStatusName(cont_status));
		  		}
		  		else 
		  		{
		  			VCONT_REF_NO.add("");
		  			VCONT_STATUS_FLG.add("");
					VCONT_STATUS.add("");
		  		}
		  		rset5.close();
				stmt5.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTraderContList_FCC()
	{
		String function_nm="getTraderContList_FCC()";
		try
		{
			String temp_cont="";
			if(contract_type.equals(""))
			{
				if(clearance.equals("IGX"))
				{
					temp_cont="CONTRACT_TYPE IN ('I')";
				}
				else
				{
					temp_cont="CONTRACT_TYPE IN ('D','T')";
				}
			}
			
			int cont=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "TRADE_REF_NO,CONTRACT_TYPE "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
			if(!contract_type.equals(""))
			{
				queryString += "AND CONTRACT_TYPE=?";
			}
			else
			{
				queryString += "AND ? ";
			}
			if(!contract_type.equals("I"))
			{
				queryString+="AND (SELECT COUNT(*) "
						+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX=? "
						+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
						+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 ";
			}
			queryString+="AND (SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL B "
					+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 "
					+ "AND (CHANGE_DATE_REQ!=? OR CHANGE_DATE_REQ IS NULL) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString+="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "TRADE_REF_NO,CONTRACT_TYPE "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				if(!contract_type.equals(""))
				{
					queryString += "AND CONTRACT_TYPE=?";
				}
				else
				{
					queryString += "AND ? ";
				}
				if(!contract_type.equals("I"))
				{
					queryString+="AND (SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
							+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX=? "
							+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
							+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 ";
				}
				queryString+="AND (SELECT COUNT(*) "
						+ "FROM FMS_TRADER_BILLING_DTL B "
						+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
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
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, comp_cd);
			if(!contract_type.equals(""))
			{
				stmt.setString(++cont, contract_type);
			}
			else
			{
				stmt.setString(++cont, temp_cont);
			}
			if(!contract_type.equals("I"))
			{
				stmt.setString(++cont, gx);
			}
			stmt.setString(++cont, "Y");
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++cont, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++cont, to_dt);
				stmt.setString(++cont, from_dt);
				
				stmt.setString(++cont, comp_cd);
				if(!contract_type.equals(""))
				{
					stmt.setString(++cont, contract_type);
				}
				else
				{
					stmt.setString(++cont, temp_cont);
				}
				if(!contract_type.equals("I"))
				{
					stmt.setString(++cont, gx);
				}
				stmt.setString(++cont, "Y");
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++cont, counterparty_cd);
				}
				stmt.setString(++cont, to_dt);
				stmt.setString(++cont, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String agmt_rev = rset.getString(4)==null?"0":rset.getString(4);
				String cont_rev_no = rset.getString(6)==null?"0":rset.getString(6);
				
				String cont_type=rset.getString(16)==null?"":rset.getString(16);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(contno);
				String disp_cont_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev, contno, cont_rev_no, cont_type, "");
				VDISP_CONT_NO.add(disp_cont_no);
				VCONT_REV_NO.add(cont_rev_no);
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
				String trade_ref=rset.getString(15)==null?"":rset.getString(15);
				if(contract_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				VCONT_REF_NO.add(cont_ref);
				
				VRECEIVED_QTY.add(""+allocUtil.getPurchaseAllocationQty(conn,own_cd, countpty_cd, agmtno, contno, cont_type,cargo_no));
				VCONT_CARGO.add("isCont");
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				VCARGO_NO.add("0");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNominationTraderContList()
	{
		String function_nm="getNominationTraderContList()";
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
				stmt=conn.prepareStatement(queryString);
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
			
			int cont=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "TRADE_REF_NO,CONTRACT_TYPE "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
				queryString += "UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "TRADE_REF_NO,CONTRACT_TYPE "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, comp_cd);
			stmt.setString(++cont, to_date);
			stmt.setString(++cont, from_date);
			if(nomination_freq.equals("M"))
			{
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++cont, "Y");
				}
				else
				{
					stmt.setString(++cont, "Y");
				}*/
				
				stmt.setString(++cont, "Y");
			}
			else if(nomination_freq.equals("W"))
			{
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++cont, "Y");
				}
				else
				{
					stmt.setString(++cont, "Y");
				}*/
				stmt.setString(++cont, "Y");
			}
			else if(nomination_freq.equals("F"))
			{
				/*if(nomination_type.equals("S"))
				{
					stmt.setString(++cont, "Y");
				}
				else
				{
					stmt.setString(++cont, "Y");
				}*/
				stmt.setString(++cont, "Y");
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++cont, to_dt);
				stmt.setString(++cont, from_dt);
				
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, to_date);
				stmt.setString(++cont, from_date);
				if(nomination_freq.equals("M"))
				{
					stmt.setString(++cont, "Y");
				}
				else if(nomination_freq.equals("W"))
				{
					stmt.setString(++cont, "Y");
				}
				else if(nomination_freq.equals("F"))
				{
					stmt.setString(++cont, "Y");
				}
				stmt.setString(++cont, to_dt);
				stmt.setString(++cont, from_dt);
			}
			
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev_no=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev_no=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(16)==null?"":rset.getString(16);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmt_rev_no);
				
				//String disp_cont_no = own_cd+cont_type+countpty_cd+"-"+agmtno+"-"+contno;
				String disp_cont_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev_no, contno, cont_rev_no, cont_type, "");
				VDISP_CONT_NO.add(disp_cont_no);
				
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(cont_rev_no);
				VCONTRACT_TYPE.add(cont_type);
				VTCQ.add(nf.format(rset.getDouble(7)));
				VQTY_UNIT.add(""+utilBean.getEnergyUnitNm(conn,rset.getString(8)==null?"":rset.getString(8)));
				VSTART_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VEND_DT.add(rset.getString(10)==null?"":rset.getString(10));
				VCONT_NAME.add(rset.getString(11)==null?"":rset.getString(11));
				String cont_status_flg=rset.getString(12)==null?"":rset.getString(12);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(13)==null?"":rset.getString(13));
				
				VCARGO_NO.add("0");
				
				String cont_ref=rset.getString(14)==null?"":rset.getString(14);
				String trade_ref=rset.getString(15)==null?"":rset.getString(15);
				if(cont_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				VCONT_REF_NO.add(cont_ref);
				
				VRECEIVED_QTY.add(""+allocUtil.getPurchaseAllocationQty(conn,own_cd, countpty_cd, agmtno, contno, cont_type,cargo_no));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNominationTraderCargoList()
	{
		String function_nm="getNominationTraderCargoList()";
		try
		{
			String from_date="";
			String to_date="";
			
			String[] split=gas_dt.split("/");
			String month=split[1];
			String year=split[2];
			
			if(nomination_freq.equals("W"))
			{
				String queryString = "SELECT TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY'),'DD/MM/YYYY') , "
						+ "TO_CHAR(NEXT_DAY(TO_DATE(?,'DD/MM/YYYY')-7,'MONDAY')+6,'DD/MM/YYYY') FROM DUAL";
				stmt=conn.prepareStatement(queryString);
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
			int cont=0;
			String queryString="SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.AGMT_NO, A.AGMT_REV, A.CONT_NO, A.CONT_REV,"
					+ "A.CARGO_NO,A.CARGO_REF,TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'),A.EDQ_QTY,A.CSOC_QTY,SUM(A.CSOC_QTY),"
					+ "TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'),(COALESCE(A.STORAGE_EXT_DAYS, 0) + COALESCE(A.STORAGE_DAYS-1, 0)) AS STORAGE_DAYS,"
					+ "A.CONTRACT_TYPE "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "JOIN "
					+ "FMS_LTCORA_CONT_MST M "
					+ "ON A.COMPANY_CD = M.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD = M.COUNTERPARTY_CD "
					+ "AND A.CONT_NO = M.CONT_NO "
					+ "AND A.AGMT_NO = M.AGMT_NO "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.BUY_SALE=? "
					+ "AND A.ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.ACTUAL_RECPT_DT + (COALESCE(A.STORAGE_EXT_DAYS, 0) + COALESCE(A.STORAGE_DAYS-1, 0)) >= TO_DATE(?,'DD/MM/YYYY') "					
					+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO " //AND A.AGMT_REV=B.AGMT_REV 
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			
			if(nomination_freq.equals("M"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND M.SELLER_MONTH_NOM=? ";
				}
				else
				{
					queryString+=" AND M.BUYER_MONTH_NOM=? ";
				}
			}
			else if(nomination_freq.equals("W"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND M.SELLER_WEEK_NOM=? ";
				}
				else
				{
					queryString+=" AND M.BUYER_WEEK_NOM=? ";
				}
			}
			else if(nomination_freq.equals("F"))
			{
				if(nomination_type.equals("S"))
				{
					queryString+=" AND M.SELLER_FORNGT_NOM=? ";
				}
				else
				{
					queryString+=" AND M.BUYER_FORNGT_NOM=? ";
				}
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND M.END_DT < SYSDATE AND M.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND M.END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND M.END_DT >= SYSDATE ";
			}
			queryString += "GROUP BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.AGMT_NO, A.AGMT_REV, A.CONT_NO, A.CONT_REV,"
			+ "A.CARGO_NO, A.CARGO_REF, A.ACTUAL_RECPT_DT, A.EDQ_QTY, A.CSOC_QTY, A.ACTUAL_RECPT_DT, A.STORAGE_EXT_DAYS,A.STORAGE_DAYS, A.CONTRACT_TYPE "
			+ "ORDER BY A.CONT_NO ASC "; 
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, comp_cd);
			stmt.setString(++cont, "T");
			stmt.setString(++cont, to_date);
			stmt.setString(++cont, from_date);
			if(nomination_freq.equals("M"))
			{
				stmt.setString(++cont, "Y");
			}
			else if(nomination_freq.equals("W"))
			{
				stmt.setString(++cont, "Y");
			}
			else if(nomination_freq.equals("F"))
			{
				stmt.setString(++cont, "Y");
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++cont, to_dt);
				stmt.setString(++cont, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev_no=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev_no=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(15)==null?"0":rset.getString(15);
				String cargo_no = rset.getString(7)==null?"":rset.getString(7);
				String cargo_ref_no = rset.getString(8)==null?"":rset.getString(8);
				String disp_cont_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev_no, contno, cont_rev_no, cont_type, cargo_no);

				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VBUYER_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(contno);
				VDISP_CONT_NO.add(disp_cont_no);
				
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				
				VTCQ.add(nf.format(rset.getDouble(12)));
				VQTY_UNIT.add("");
				
				VSTART_DT.add(rset.getString(13)==null?"":rset.getString(13)); // Actual Receipt Date
				String end_days = rset.getString(14)==null?"":rset.getString(14);

				String end_dt = dateUtil.getDate(rset.getString(13)==null?"":rset.getString(13), end_days); // Final Date including Storage days
				VEND_DT.add(end_dt);
				
				String cont_status_flg="";
				VFCC_FLAG.add("");
				
				String cont_ref="";
				String trade_ref="";
				if(contract_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				
				String recev_qty = "";
				
				String queryString4="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1, contno);
				stmt4.setString(2, agmtno);
				stmt4.setString(3, own_cd);
				stmt4.setString(4, countpty_cd);
				stmt4.setString(5, cont_type);
				stmt4.setString(6, cargo_no);
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					recev_qty=nf.format(rset4.getDouble(1));
				}
				stmt4.close();
				rset4.close();
				
				VRECEIVED_QTY.add(""+recev_qty);
				VCARGO_NO.add(cargo_no);
				VCONT_CARGO.add("isCargo");
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				String queryString5 = "SELECT CONT_REF_NO,MDCQ_PERCENTAGE,CONT_STATUS,CONT_NAME "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, own_cd);
				stmt5.setString(2, countpty_cd);
				stmt5.setString(3, agmtno);
				stmt5.setString(4, agmt_rev_no);
				stmt5.setString(5, cont_type);
				stmt5.setString(6, contno);
				rset5=stmt5.executeQuery();
		  		if(rset5.next())
		  		{
		  			VCONT_REF_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
		  			
		  			String mcsoc_per = rset5.getString(2)==null?"":rset5.getString(2);
		  			String cont_status = rset5.getString(3)==null?"":rset5.getString(3);
		  			String contract_name = rset5.getString(4)==null?"":rset5.getString(4);
		  			
		  			VCONT_NAME.add(contract_name);
		  			VCONT_STATUS_FLG.add(cont_status);
					VCONT_STATUS.add(""+ContStatusName(cont_status));
		  		}
		  		else 
		  		{
		  			VCONT_REF_NO.add("");
		  			VCONT_STATUS_FLG.add("");
					VCONT_STATUS.add("");
		  		}
		  		rset5.close();
				stmt5.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedTraderPlantList()
	{
		String function_nm="getSelectedTraderPlantList()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
					+ "FROM FMS_TRADER_CONT_PLANT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
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
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
				VSEL_TRAD_CD.add(counterparty_cd);
				VSEL_PLANT_SEQ_NO.add(plant_seq);
				VSEL_PLANT_ABBR.add(plant_abbr);
				VSEL_SPLIT_VALUE.add(rset.getString(2)==null?"":rset.getString(2));
				
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
							+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
							+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
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
						
						mergeChargDesc += "~ Rate : "+hist_rate+" Effective "+hist_effdt;
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedOtherTraderPlantList()
	{
		String function_nm="getSelectedOtherTraderPlantList()";
		try
		{
			String queryString="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
					+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
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
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				String plant_seq = rset.getString(2)==null?"":rset.getString(2);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
				VOTH_SEL_TRAD_CD.add(countpty_cd);
				VOTH_SEL_PLANT_SEQ_NO.add(plant_seq);
				VOTH_SEL_PLANT_ABBR.add(plant_abbr);
				VOTH_SEL_SPLIT_VALUE.add(rset.getString(3)==null?"":rset.getString(3));
				
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
							+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
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
					
					if(cnt>0)
					{
						mergeChargDesc += ""+VCHARGE_NAME.elementAt(i);
					}
					
					// Here '~' used as using '\n' cause issue in passing merged string in JS.
					
					String queryString2="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),CHARGE_RATE "
							+ "FROM FMS_TRADER_CONT_PLANT_CHRG A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
							+ "ORDER BY EFF_DT DESC";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
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
						
						mergeChargDesc += "~ Rate : "+hist_rate+" Effective "+hist_effdt;
					}
					rset2.close();
					stmt2.close();
					
					mergeChargDesc+="~";
					
					if(cnt>0)
					{
						mergeChargDesc+="~";
					}
				}
				
				VOTH_SEL_CHARGE_VALUE.add(margeChrgVal);
				VOTH_SEL_CHARGE_DESC.add(mergeChargDesc);
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
					+ "FROM FMS_TRADER_CONT_TRANSPTR "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
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
					+ "FROM FMS_TRADER_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLinkTransporterList()
	{
		String function_nm="getLinkTransporterList()";
		try
		{
			String queryString="SELECT FORMULA_ID "
					+ "FROM FMS_TRADER_CONT_LINK_TRANS "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String formula_id = rset.getString(1)==null?"":rset.getString(1);
				if(!formula_id.equals(""))
				{
					String tmp[] = formula_id.split("-");
					String bu_plant_seq = tmp[0];
					String trd_cd=tmp[1];
					String plant_seq = tmp[2];
					String transCd = tmp[3];
					String trans_plant_seq = tmp[4];
					
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,trd_cd, comp_cd, plant_seq, "T");
					String trans_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, trans_plant_seq, "R");
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
					
					String formula_nm = bu_plant_abbr +" : "+plant_abbr+" - "+trans_plant_abbr;
					
					VFORMULA_ID.add(formula_id);
					VFORMULA_NM.add(formula_nm);
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
	
	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";
		try
		{
			String queryString="SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? ";
			//Added by Pratham Bhatt for Confirmation Notice
			queryString+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CARGO_MST "
					+ "WHERE COMPANY_CD=? ";
			//Pratham Bhatt 20240802: for selecting LTCORA traders 
			queryString+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD  "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL  "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				
				VMST_COUNTERPARTY_CD.add(countpty_cd);
				VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void setDisplaySegment()
	{
		String function_nm="setDisplaySegment()";
		try
		{
			//Segment N : Added by Pratham Bhatt for CN Cargo 20240619 
			//Segment L : Added by Pratham Bhatt for LTCORA 20240802
			if(segment.equals("D") || segment.equals("I")||segment .equals("N")||segment.equals("L")||segment.equals("T"))
			{
				VDISPLAY_SEGMENT_TYP.add(segment);
				if(segment.equals("D") || segment.equals("I")||segment.equals("T"))
				{
					VDISPLAY_SEGMENT.add(""+getSegmentNm(segment)+" Purchase");
				}
				else
				{
					VDISPLAY_SEGMENT.add(""+getSegmentNm(segment));
				}
			}
			else
			{
				VDISPLAY_SEGMENT_TYP.add("D");
				VDISPLAY_SEGMENT.add(""+getSegmentNm("D")+" Purchase");
				
				VDISPLAY_SEGMENT_TYP.add("T");
				VDISPLAY_SEGMENT.add(""+getSegmentNm("T")+" Purchase");
				
				VDISPLAY_SEGMENT_TYP.add("I");
				VDISPLAY_SEGMENT.add(""+getSegmentNm("I")+" Purchase");
		
				VDISPLAY_SEGMENT_TYP.add("N");
				VDISPLAY_SEGMENT.add(""+getSegmentNm("N")+"");
				
				//Pratham Bhatt:20240802 for LTCORA
				VDISPLAY_SEGMENT_TYP.add("L");
				VDISPLAY_SEGMENT.add(""+getSegmentNm("L")+"");
				
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Added by Pratham Bhatt 240627
	public void getCargoSummary(String segment) 
	{
		String function_nm="getCargoSummary()";
		try
		{
			double nTotalQty=0;
			double nUnloadedTotalQty=0;
			int index=0;
			double total_balance=0;
			String queryString="SELECT COMPANY_CD,"
					+ "COUNTERPARTY_CD,"
					+ "AGMT_TYPE,"
					+ "AGMT_NO,"
					+ "CONTRACT_TYPE,"
					+ "CONT_NO,"
					+ "CARGO_NO,"
					+ "CARGO_REF,"
					+ "CARGO_STATUS,"
					+ "CARGO_QTY,"
					+ "RATE,"
					+ "RATE_UNIT,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'), AGMT_REV, CONT_REV "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)  ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+=" AND COUNTERPARTY_CD=? ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2,segment);
			stmt.setString(3, to_dt);
			stmt.setString(4, from_dt);
			
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(5, counterparty_cd);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String compCd = (rset.getString(1)==null?"":rset.getString(1));
				String counterparty_cd =(rset.getString(2)==null?"":rset.getString(2));
				agmt_type=(rset.getString(3)==null?"":rset.getString(3));
				agmt_no=(rset.getString(4)==null?"":rset.getString(4));
				String agmt_rev =(rset.getString(15)==null?"":rset.getString(15));
				contract_type=(rset.getString(5)==null?"":rset.getString(5));
				cont_no=(rset.getString(6)==null?"":rset.getString(6));
				String cargo_no=(rset.getString(7)==null?"":rset.getString(7));
				String cargo_ref=(rset.getString(8)==null?"":rset.getString(8));
				double booked_qty=rset.getDouble(10);
				VBOOKED_QTY.add(nf.format(rset.getDouble(10)));
				conf_price = (rset.getString(11)==null?"":rset.getString(11));
				rate_unit=(rset.getString(12)==null?"":rset.getString(12));
				win_start_dt =(rset.getString(13)==null?"":rset.getString(13));
				win_end_dt = rset.getString(14)==null?"":rset.getString(14);
				String stat = rset.getString(9)==null?"":rset.getString(9);
				String cont_name = comp_cd+contract_type+counterparty_cd+"-"+agmt_no+"-"+cont_no;
				
				String counterparty_status=""+getCounterpartyStatus(counterparty_cd).get("status");
				VCOUNTERPARTY_STATUS.add(counterparty_status);
				String status_eff_dt=""+getCounterpartyStatus(counterparty_cd).get("eff_dt");
				VSTATUS_EFF_DT.add(status_eff_dt);
				
				VCOUNTERPARTY_CD.add(rset.getString(2)==null?"":rset.getString(2));
				//VCONTPARTY_CD.add(rset.getString(2)==null?"":rset.getString(2));
				VAGMT_TYPE.add(rset.getString(3)==null?"":rset.getString(3));
				VAGMT_NO.add(rset.getString(4)==null?"":rset.getString(4));
				VCONTRACT_TYPE.add(rset.getString(5)==null?"":rset.getString(5));
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(contract_type));
				VCONT_NO.add(rset.getString(6)==null?"":rset.getString(6));
				VCARGO_NO.add(cargo_no);
				VCARGO_REF.add(rset.getString(8)==null?"":rset.getString(8));
				String cont_rev = rset.getString(16)==null?"":rset.getString(16);
				VCONT_REV_NO.add(cont_rev);
				VPRICE_TYPE.add("Fixed");
				VCONT_REF_NO.add(cargo_ref);
				nTotalQty+=rset.getDouble(10);
				String cargo_name = utilBean.NewDealMappingId(comp_cd,counterparty_cd,agmt_no,agmt_rev,cont_no,cont_rev,contract_type,cargo_no);
				VCARGO_NAME.add(cargo_name);
				
				double unldl_qty=0;
				//for allocation start and end date and unloaded MMBTU
				String queryString3 = "SELECT TO_CHAR(A.ACT_ARRV_DT,'DD/MM/YY'), TO_CHAR(A.QQ_DT,'DD/MM/YY'), A.ACT_QTY_MMBTU FROM FMS_BUY_CARGO_ALLOC A "
						+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? "
						+ "AND A.CONTRACT_TYPE=? AND A.CONT_NO=? AND A.CONT_REV=? AND A.CARGO_NO=?  "
						+ "AND A.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
						+ "WHERE  A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV)";
				
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, compCd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_type);
				stmt3.setString(4, agmt_no);
				stmt3.setString(5, agmt_rev);
				stmt3.setString(6, contract_type);
				stmt3.setString(7, cont_no);
				stmt3.setString(8, cont_rev);
				stmt3.setString(9, cargo_no);
				rset3 = stmt3.executeQuery();
				if(rset3.next())
				{
					unldl_qty=rset3.getDouble(3);
					VMIN_ALLOC_DT.add(rset3.getString(1)==null?"":rset3.getString(1));
					VMAX_ALLOC_DT.add(rset3.getString(2)==null?"":rset3.getString(2));
					VUNLOADED_QTY.add(nf.format(rset3.getDouble(3)));
					nUnloadedTotalQty+=rset3.getDouble(3);
				}
				else
				{
					VMIN_ALLOC_DT.add("");
					VMAX_ALLOC_DT.add("");
					VUNLOADED_QTY.add("0.00");
				}
				
				if(stat.equals("Y"))
				{
					VCONT_STATUS.add("Confirmed");
				}
				else if(stat.equals("X"))
				{
					VCONT_STATUS.add("Cancelled");
				}
				else if(stat.equals("N"))
				{
					VCONT_STATUS.add("Not-Confirmed");
				}
				else
				{
					VCONT_STATUS.add("");
				}
				
				if(rate_unit.equals("1"))
				{
					VRATE.add(nf.format(rset.getDouble(11)));
					VRATE_UNIT.add("INR");
				}
				else
				{
					VRATE.add(nf.format(rset.getDouble(11)));
					VRATE_UNIT.add("USD");
				}
				
				VSTART_DT.add(rset.getString(13)==null?"":rset.getString(13));
				VEND_DT.add(rset.getString(14)==null?"":rset.getString(14));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
				
				//for business unit 
				String BuUnit = "";
				String queryString1 = "SELECT DISTINCT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_BU "
						+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND COUNTERPARTY_CD=? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, compCd);
				stmt1.setString(2, agmt_no);
				stmt1.setString(3, agmt_rev);
				stmt1.setString(4, counterparty_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String ownercd = rset1.getString(1)==null?"0":rset1.getString(1);
		  			String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
		  			if(BuUnit.equals(""))
		  			{
		  				BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, compCd, plant_seq, "B");
		  			}
		  			else
		  			{
		  				BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, compCd, plant_seq, "B");
		  			}
				}
				VBU_POINT.add(BuUnit);
				rset1.close();
	  			stmt1.close();

	  			//for trader plant
				String traderPlant = "";
				String queryString2 = "SELECT DISTINCT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND COUNTERPARTY_CD=? ";
				stmt2 =  conn.prepareStatement(queryString2);
			  	stmt2.setString(1, compCd);
			  	stmt2.setString(2, agmt_no);
			  	stmt2.setString(3, agmt_rev);
			  	stmt2.setString(4, counterparty_cd);
			  	rset2=stmt2.executeQuery();
		  		while(rset2.next())
		  		{
		  			String plant_seq = rset2.getString(1)==null?"0":rset2.getString(1);
		  			if(traderPlant.equals(""))
		  			{
		  				traderPlant+=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, compCd, plant_seq, "T");
		  			}
		  			else
		  			{
		  				traderPlant+=","+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, compCd, plant_seq, "T");
		  			}	
		  		}
		  		VTRADER_PLANT.add(traderPlant);
		  		rset2.close();
	  			stmt2.close();
	  			double balance_qty=booked_qty-unldl_qty;
	  			total_balance+=balance_qty;
				VBALANCE_QTY.add(utilBean.RateNumberFormat(balance_qty,"1"));
				VBAL_INFO.add(utilBean.RateNumberFormat(booked_qty,"1")+"-"+utilBean.RateNumberFormat(unldl_qty,"1")+"="+utilBean.RateNumberFormat(balance_qty,"1"));
			}
			VTOTAL_QUANTITY.add(utilBean.RateNumberFormat(nTotalQty,"1"));
			VTOTAL_UNLOADED_QUANTITY.add(utilBean.RateNumberFormat(nUnloadedTotalQty,"1"));
			VTOTAL_BALANCE.add(utilBean.RateNumberFormat(total_balance,"1"));
			VINDEX.add(index);
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Pratham Bhatt 20240802: For LTCORA summary
	public void getLTCORA_Summary(String segment)
	{
		String function_nm="getLTCORA_Summary()";
		try
		{
			double lTotalQty=0;
			double lUnloadedTotalQty=0;
			double balTotalQty=0;
			balTotalQty_str="";
			bookedToolTip = "Booked qty\n= IF (ADQ >= 0) {ADQ MMBTU -(ADQ MMBTU*SUG%)}\nELSE {EDQ MMBTU -(EDQ MMBTU*SUG%}";
			int index=0;
			
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CARGO_REF, TO_CHAR(A.EDQ_FROM_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR(A.EDQ_TO_DT,'DD/MM/YYYY'),A.AGMT_TYPE, A.AGMT_REV, A.AGMT_NO, A.CONT_NO, A.CONT_REV, A.CONTRACT_TYPE,EDQ_QTY,       "
					+ "TO_CHAR(A.QQ_DT,'DD/MM/YYYY'), A.CARGO_NO, TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), C.SUG, C.CONT_REF_NO, C.CONT_STATUS, C.LTCORA_TARIFF, C.LTCORA_TARIFF_UNIT,  "
					+ "NVL((A.STORAGE_DAYS-1),0),A.STORAGE_EXT_DAYS, C.EXTEND_STORAGE "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST C  "
					+ "WHERE A.COMPANY_CD=? AND A.AGMT_TYPE=?"
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
					+ "NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1) )>= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE = C.CONTRACT_TYPE  AND A.CONT_NO=C.CONT_NO  "
					+ "AND A.CONT_REV=C.CONT_REV" ;
			if(!counterparty_cd.equals("0"))
			{
				queryString+=" AND A.COUNTERPARTY_CD=?  ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, segment);
			stmt.setString(3, to_dt);
			stmt.setString(4, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(5, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String own_cd = rset.getString(1)==null?"":rset.getString(1);
				String coutpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String cargo_ref = rset.getString(3)==null?"":rset.getString(3);
				String edq_from_dt = rset.getString(4)==null?"":rset.getString(4);
				String edq_to_dt = rset.getString(5)==null?"":rset.getString(5);
				String agmt_typ = rset.getString(6)==null?"":rset.getString(6);
				String agmt_rev = rset.getString(7)==null?"":rset.getString(7);
				String agmt_no = rset.getString(8)==null?"":rset.getString(8);
				String cont_no = rset.getString(9)==null?"":rset.getString(9);
				String cont_rev = rset.getString(10)==null?"":rset.getString(10);
				String cont_tpy = rset.getString(11)==null?"":rset.getString(11);
				String edq_qty = rset.getString(12)==null?"":rset.getString(12);
				String qq_dt = rset.getString(13)==null?"":rset.getString(13);
				String cargo_no = rset.getString(14)==null?"":rset.getString(14);
				String actual_recpt_dt = rset.getString(15)==null?"":rset.getString(15);
				String sug_per = rset.getString(16)==null?"":rset.getString(16);
				double sug = rset.getDouble(16);
  				String cont_ref = rset.getString(17)==null?"":rset.getString(17);
  				String status_flag = rset.getString(18)==null?"":rset.getString(18);
  				double rate = rset.getDouble(19);
  				String price_unit = rset.getString(20)==null?"":rset.getString(20);
  				String storage_days = rset.getString(21)==null?"":rset.getString(21);
  				String storage_ext_days = rset.getString(22)==null?"":rset.getString(22);
  				String extend_storage = rset.getString(23)==null?"":rset.getString(23);
  				
  				String storage_end_dt = dateUtil.getDate(actual_recpt_dt, storage_days);
  				String storage_ext_dt = dateUtil.getDate(storage_end_dt, storage_ext_days);
  				
  				//From dt as Actual Recept date and to dt as storage end date or storage_ext_dt
  				String cargo_to_dt = !storage_ext_days.equals("")?storage_ext_dt:storage_end_dt;
  				
				String cont_mapping = utilBean.NewDealMappingId(own_cd,coutpty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_tpy,cargo_no);	//Contract-mapping
				
				String counterparty_status=""+getCounterpartyStatus(coutpty_cd).get("status");
				VCOUNTERPARTY_STATUS.add(counterparty_status);
				String status_eff_dt=""+getCounterpartyStatus(coutpty_cd).get("eff_dt");
				VSTATUS_EFF_DT.add(status_eff_dt);
				
				//SUG if cargo sug is modified
				String queryString1="SELECT SUG FROM FMS_LTCORA_CONT_CARGO_MOD "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE = ? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND "
						+ "CARGO_NO=?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1,own_cd);
				stmt1.setString(2,coutpty_cd);
				stmt1.setString(3,agmt_typ);
				stmt1.setString(4,agmt_no);
				stmt1.setString(5,agmt_rev);
				stmt1.setString(6,cont_tpy);
				stmt1.setString(7,cont_no);
				stmt1.setString(8,cont_rev);
				stmt1.setString(9,cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					sug_per = rset1.getString(1)==null?"":rset1.getString(1);
					sug = rset1.getDouble(1);
				}
				
				rset1.close();
				stmt1.close();
				
				//for finding ADQ
				int selCnt2=0;
				double adq=0;
				int adq_count = 0;
				String adq_str="";
				String queryString5 = "SELECT ADQ_QTY,ADQ_DT "
						+ "FROM FMS_LTCORA_CONT_CARGO_ADQ A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ " AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_ADQ B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)"
						+ "ORDER BY ADQ_DT ASC";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(++selCnt2, own_cd);
				stmt5.setString(++selCnt2, coutpty_cd);
				stmt5.setString(++selCnt2, "T");
				stmt5.setString(++selCnt2, agmt_typ);
				stmt5.setString(++selCnt2, agmt_no);
				stmt5.setString(++selCnt2, cont_tpy);
				stmt5.setString(++selCnt2, cont_no);
				stmt5.setString(++selCnt2, cargo_no);
				rset5=stmt5.executeQuery();
				while(rset5.next())
				{
					adq_str = rset5.getString(1)==null?"":rset5.getString(1);
					if(!adq_str.equals(""))
					{
						adq+= Double.parseDouble(adq_str);
						adq_count++;		// for checking whether adq is configured or not, if not then adq_count = 0  
					}
				}

				rset5.close();
				stmt5.close();
				
				if(adq_count==0)
				{
					adq = -1;		//for checking whether adq is configured or not, if not configured then adq = -1 
				}
				
				double booked_qty = adq>=0?(adq - (adq*sug/100)):(Double.parseDouble(edq_qty)- ((Double.parseDouble(edq_qty)*sug/100)));
				lTotalQty+=booked_qty;
				lTotalQty=Double.parseDouble(nf.format(lTotalQty));
  				
  				String booked_info = adq>=0?(adq+"- ("+adq+"*"+sug_per+"%)"):(edq_qty+"- ("+edq_qty+"*"+sug_per+"%)");
				//for owner business unit 
				String BuUnit="";
				String QueryString1 = "SELECT COMPANY_CD,PLANT_SEQ_NO  "
			  			+ "FROM FMS_LTCORA_CONT_BU  "
			  			+ "WHERE COMPANY_CD=?  AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?  "
			  			+ "AND CONT_NO=? AND CONT_REV=?  "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt1=conn.prepareStatement(QueryString1);
			  	stmt1.setString(1, own_cd);
			  	stmt1.setString(2, agmt_typ);
			  	stmt1.setString(3, agmt_no);
			  	stmt1.setString(4, agmt_rev);
			  	stmt1.setString(5, cont_no);
			  	stmt1.setString(6, cont_rev);
			  	stmt1.setString(7, coutpty_cd);
			  	stmt1.setString(8, cont_tpy);
			  	rset1=stmt1.executeQuery();
	  			while(rset1.next())
	  			{
	  				String ownercd = rset1.getString(1)==null?"0":rset1.getString(1);
	  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
	  				if(BuUnit.equals(""))
	  				{
	  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
	  				}
	  				else
	  				{
	  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
	  				}
	  			}
	  			rset1.close();
	  			stmt1.close();
				
	  			//for trader selected plant 
	  			String TraderPlant="";
	  			String queryString2 = "SELECT PLANT_SEQ_NO  "
			  			+ "FROM FMS_LTCORA_CONT_PLANT  "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
			  			+ "AND CONT_NO=? AND CONT_REV=?  "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt2=conn.prepareStatement(queryString2);
			  	stmt2.setString(1, own_cd);
			  	stmt2.setString(2, agmt_no);
			  	stmt2.setString(3, agmt_rev);
			  	stmt2.setString(4, agmt_typ);
			  	stmt2.setString(5, cont_no);
			  	stmt2.setString(6, cont_rev);
			  	stmt2.setString(7, coutpty_cd);
			  	stmt2.setString(8, cont_tpy);
			  	rset2=stmt2.executeQuery();
	  			while(rset2.next())
	  			{
	  				String plant_seq = rset2.getString(1)==null?"0":rset2.getString(1);
	  				if(TraderPlant.equals(""))
	  				{
	  					TraderPlant+=""+utilBean.getCounterpartyPlantABBR(conn,coutpty_cd, own_cd, plant_seq, "T");
	  				}
	  				else
	  				{
	  					TraderPlant+=","+utilBean.getCounterpartyPlantABBR(conn,coutpty_cd, own_cd, plant_seq, "T");
	  				}
	  			}
	  			rset2.close();
	  			stmt2.close();
	  			
	  			//for allocation details
	  			String alloc_start_dt="";
	  			String alloc_end_dt  = "";
	  			double unldl = 0;
	  			//PB20250103: FOR GETTING THE REGASSIFIED QUANTITY ACCORDING TO DATE FILTER 
	  			String query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "		//less than to date 
						//+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') "		//greater than from date 
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
	  			stmt4 = conn.prepareStatement(query);
	  			stmt4.setString(1,cont_no );
	  			stmt4.setString(2,agmt_no );
	  			stmt4.setString(3,own_cd);
	  			stmt4.setString(4,coutpty_cd);
	  			stmt4.setString(5,cont_tpy);
	  			stmt4.setString(6,cargo_no);
	  			stmt4.setString(7,to_dt);
	  			//stmt4.setString(8,from_dt);
	  			rset4 = stmt4.executeQuery();
	  			if(rset4.next())
				{
	  				unldl = rset4.getDouble(1);
	  				lUnloadedTotalQty+=unldl;
	  				lUnloadedTotalQty=Double.parseDouble(nf.format(lUnloadedTotalQty));
				}
	  			rset4.close();
	  			stmt4.close();
	  			
	  			
	  			String queryString4="SELECT TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY'),SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
	  			stmt4 = conn.prepareStatement(queryString4);
	  			stmt4.setString(1,cont_no );
	  			stmt4.setString(2,agmt_no );
	  			stmt4.setString(3,own_cd);
	  			stmt4.setString(4,coutpty_cd);
	  			stmt4.setString(5,cont_tpy);
	  			stmt4.setString(6,cargo_no);
	  			rset4 = stmt4.executeQuery();
	  			if(rset4.next())
				{
	  				alloc_start_dt = rset4.getString(1)==null?"":rset4.getString(1);
	  				alloc_end_dt = rset4.getString(2)==null?"":rset4.getString(2);
//	  				unldl = rset4.getDouble(3);
//	  				lUnloadedTotalQty+=unldl;
//	  				lUnloadedTotalQty=Double.parseDouble(nf.format(lUnloadedTotalQty));
				}
	  			rset4.close();
	  			stmt4.close();
	  			if(price_unit.equals("1"))
	  			{	  					
	  				VRATE_UNIT.add("INR");
	  			}
	  			else if(price_unit.equals("2"))
	  			{	  					
	  				VRATE_UNIT.add("USD");
	  			}
	  			
	  			double balance_qty = booked_qty-unldl;
	  			balTotalQty+=balance_qty;
	  			balTotalQty=Double.parseDouble(nf.format(balTotalQty));
	  			String bal = utilBean.RateNumberFormat(balance_qty,"1");
	  			String bal_info = ""+nf.format(booked_qty)+"-"+""+nf.format(unldl);
	  			
	  			VBAL_INFO.add(bal_info);
	  			VCONT_STATUS_FLG.add(status_flag);
	  			VCONT_STATUS.add(""+ContStatusName(status_flag));
	  			VBOOKED_QTY.add(nf.format(booked_qty));
	  			VCONT_REF_NO.add(cont_ref);
	  			VRATE.add(nf.format(rate));
				VCOUNTERPARTY_CD.add(coutpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,coutpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,coutpty_cd));
				VCONT_NO.add(cont_no);
				VCONT_REV_NO.add(cont_rev);
				VSTART_DT.add(actual_recpt_dt);
				VEND_DT.add(cargo_to_dt);
				VPRICE_TYPE.add("Fixed");
				VAGMT_NO.add(agmt_no);
				VAGMT_REV_NO.add(agmt_rev);
				VCONTRACT_TYPE.add(cont_mapping);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_tpy));
				VMIN_ALLOC_DT.add(alloc_start_dt);	
				VMAX_ALLOC_DT.add(alloc_end_dt);
				VUNLOADED_QTY.add(nf.format(unldl));
				VTRADER_PLANT.add(TraderPlant);
				VBU_POINT.add(BuUnit);
				VBALANCE_QTY.add(bal);		//Added for column Balance qty 
				VBOOKED_INFO.add(booked_info);
				
			}
			VTOTAL_QUANTITY.add(utilBean.RateNumberFormat(lTotalQty,"1"));
			VTOTAL_UNLOADED_QUANTITY.add(utilBean.RateNumberFormat(lUnloadedTotalQty,"1"));
			//System.out.println("The count for iteration "+segment+" is: "+utilBean.RateNumberFormat(lTotalQty,"1"));
			balTotalQty_str = utilBean.RateNumberFormat(balTotalQty,"1");
			VTOTAL_BALANCE.add(utilBean.RateNumberFormat(balTotalQty,"1"));
			VINDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPurchaseSummary()
	{
		String function_nm="getPurchaseSummary()";
		try
		{
			totalQty=0;
			UnloadedtotalQty=0;
			AvailabletotalQty=0;
			int index=0;
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
			}
			else
			{
				VTEMP_SEGMENT_TYPE=VDISPLAY_SEGMENT_TYP;
			}
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				double unldl_count = 0;
				double count = 0;
				double total_balance=0;
				if(VTEMP_SEGMENT_TYPE.elementAt(i).toString().equals("N"))	//Pratham Bhatt: for CN Cargo
				{
					getCargoSummary(VTEMP_SEGMENT_TYPE.elementAt(i).toString()); 
				}
				//Else-if part added by Pratham Bhatt: 20240802 for LTCORA
				
				 else if(VTEMP_SEGMENT_TYPE.elementAt(i).toString().equals("L"))
				 {
				 	getLTCORA_Summary(VTEMP_SEGMENT_TYPE.elementAt(i).toString()); 
				 }
				 
				else
				{	
					index=0;
					String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONT_REV, TCQ, QUANTITY_UNIT, "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
							+ "CONT_NAME,RATE,RATE_UNIT,CONT_STATUS,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
							+ "CONT_REF_NO,TRADE_REF_NO "
							+ "FROM FMS_TRADER_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					if(!counterparty_cd.equals("0"))
					{
						queryString+=" AND COUNTERPARTY_CD=? ";
					}
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
					stmt.setString(3, to_dt);
					stmt.setString(4, from_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt.setString(5, counterparty_cd);
						
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						index+=1;
						
						String own_cd=rset.getString(1)==null?"":rset.getString(1);
						String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
						String contNo=rset.getString(3)==null?"0":rset.getString(3);
						String contRev=rset.getString(4)==null?"0":rset.getString(4);
						String agmtNo=rset.getString(13)==null?"0":rset.getString(13);
						String agmtRev=rset.getString(14)==null?"0":rset.getString(14);
						String contract_type=rset.getString(15)==null?"0":rset.getString(15);
						String cont_ref=rset.getString(16)==null?"":rset.getString(16);
						String trade_ref=rset.getString(17)==null?"":rset.getString(17);
						double booked_qty = rset.getDouble(5);
						if(contract_type.equals("I"))
						{
							cont_ref=trade_ref;
						}
						
						String counterparty_status=""+getCounterpartyStatus(countpty_cd).get("status");
						VCOUNTERPARTY_STATUS.add(counterparty_status);
						String status_eff_dt=""+getCounterpartyStatus(countpty_cd).get("eff_dt");
						VSTATUS_EFF_DT.add(status_eff_dt);
						
						VCONT_REF_NO.add(cont_ref);
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VCONT_NO.add(contNo);
						VCONT_REV_NO.add(contRev);
						VBOOKED_QTY.add(nf.format(rset.getDouble(5)));
						VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
						String rate_unit = rset.getString(11)==null?"1":rset.getString(11);
						if(rate_unit.equals("1"))
						{
							VRATE.add(nf.format(rset.getDouble(10)));
							VRATE_UNIT.add("INR");
						}
						else
						{
							VRATE.add(nf2.format(rset.getDouble(10)));
							VRATE_UNIT.add("USD");
						}
						VPRICE_TYPE.add("Fixed");
						String status_flg = rset.getString(12)==null?"":rset.getString(12);
						VCONT_STATUS_FLG.add(status_flg);
						VCONT_STATUS.add(""+ContStatusName(status_flg));
						VAGMT_NO.add(agmtNo);
						VAGMT_REV_NO.add(agmtRev);
						String cont_map = utilBean.NewDealMappingId(comp_cd,countpty_cd, agmtNo, agmtRev, contNo, contRev, contract_type, cargo_no);	//Contract-mapping
						VCONTRACT_TYPE.add(cont_map);
//						VCONTRACT_TYPE.add(contract_type);
						if(contract_type.equals("D"))
						{
							VCONTRACT_TYPE_NM.add("Domestic NG");
						}
						else if(contract_type.equals("I"))
						{
							VCONTRACT_TYPE_NM.add("IGX");
						}
						else if(contract_type.equals("T"))
						{
							VCONTRACT_TYPE_NM.add("In Tank LNG/RLNG Purchase");
						}
						else
						{
							VCONTRACT_TYPE_NM.add("");
						}
						if(contract_type.equals("D"))
						{
							count+= rset.getDouble(5);
						}
						else if(contract_type.equals("I"))
						{
							count+= rset.getDouble(5);
						}
						else if(contract_type.equals("T"))
						{
							count+= rset.getDouble(5);
						}
						totalQty += rset.getDouble(5);
						
						String BuUnit="";
						String queryString1 = "SELECT COMPANY_CD,PLANT_SEQ_NO "
					  			+ "FROM FMS_TRADER_CONT_BU "
					  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					  			+ "AND CONT_NO=? AND CONT_REV=? "
					  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
					  	stmt1=conn.prepareStatement(queryString1);
					  	stmt1.setString(1, own_cd);
					  	stmt1.setString(2, agmtNo);
					  	stmt1.setString(3, agmtRev);
					  	stmt1.setString(4, contNo);
					  	stmt1.setString(5, contRev);
					  	stmt1.setString(6, countpty_cd);
					  	stmt1.setString(7, contract_type);
					  	rset1=stmt1.executeQuery();
			  			while(rset1.next())
			  			{
			  				String ownercd = rset1.getString(1)==null?"0":rset1.getString(1);
			  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
			  				if(BuUnit.equals(""))
			  				{
			  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
			  				}
			  				else
			  				{
			  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
			  				}
			  			}
			  			VBU_POINT.add(BuUnit);
			  			rset1.close();
			  			stmt1.close();
			  			
			  			String TraderPlant="";
			  			String queryString2 = "SELECT PLANT_SEQ_NO "
					  			+ "FROM FMS_TRADER_CONT_PLANT "
					  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					  			+ "AND CONT_NO=? AND CONT_REV=? "
					  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
					  	stmt2=conn.prepareStatement(queryString2);
					  	stmt2.setString(1, own_cd);
					  	stmt2.setString(2, agmtNo);
					  	stmt2.setString(3, agmtRev);
					  	stmt2.setString(4, contNo);
					  	stmt2.setString(5, contRev);
					  	stmt2.setString(6, countpty_cd);
					  	stmt2.setString(7, contract_type);
					  	rset2=stmt2.executeQuery();
			  			while(rset2.next())
			  			{
			  				String plant_seq = rset2.getString(1)==null?"0":rset2.getString(1);
			  				if(TraderPlant.equals(""))
			  				{
			  					TraderPlant+=""+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
			  				}
			  				else
			  				{
			  					TraderPlant+=","+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
			  				}
			  			}
			  			rset2.close();
			  			stmt2.close();
			  			VTRADER_PLANT.add(TraderPlant);
			  			
			  			double unldl_qty = 0;
						//PB20250103: FOR MATCHING THE UNLOADED QUANTITY WITH DATE FILTER 
			  			String query="SELECT SUM(QTY_MMBTU) "
			  					+ "FROM FMS_BUY_DAILY_ALLOCATION A  "
			  					+ "WHERE CONT_NO=? AND AGMT_NO=?  "
			  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?"
			  					+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY')"		//less than to date 
			  					//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY')"		//greater than from date 
			  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B  "
			  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO  "
			  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD  "
			  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ  "
			  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE  "
			  					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
			  			stmt3=conn.prepareStatement(query);
						stmt3.setString(1, contNo);
						stmt3.setString(2, agmtNo);
						stmt3.setString(3, own_cd);
						stmt3.setString(4, countpty_cd);
						stmt3.setString(5, contract_type);
						stmt3.setString(6, to_dt);
						//stmt3.setString(7, from_dt);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							unldl_qty=rset3.getDouble(1);
							VUNLOADED_QTY.add(nf.format(rset3.getDouble(1)));
							VAVAILABLE_FOR_SALE.add(nf.format(rset3.getDouble(1)));
							
							UnloadedtotalQty+=rset3.getDouble(1);
							AvailabletotalQty+=rset3.getDouble(1);
							
							unldl_count+=rset3.getDouble(1);
						}
						else
						{
							VUNLOADED_QTY.add("0.00");
							VAVAILABLE_FOR_SALE.add("0.00");
						}
						rset3.close();
						stmt3.close();
						
			  			String queryString3="SELECT TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY'),SUM(QTY_MMBTU) "
				  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, contNo);
						stmt3.setString(2, agmtNo);
						stmt3.setString(3, own_cd);
						stmt3.setString(4, countpty_cd);
						stmt3.setString(5, contract_type);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							VMIN_ALLOC_DT.add(rset3.getString(1)==null?"":rset3.getString(1));
							VMAX_ALLOC_DT.add(rset3.getString(2)==null?"":rset3.getString(2));
							/*VUNLOADED_QTY.add(nf.format(rset3.getDouble(3)));
							VAVAILABLE_FOR_SALE.add(nf.format(rset3.getDouble(3)));
							
							UnloadedtotalQty+=rset3.getDouble(3);
							AvailabletotalQty+=rset3.getDouble(3);
							
							if(contract_type.equals("D"))
							{
								unldl_count+=rset3.getDouble(3);
							}
							else if(contract_type.equals("T"))
							{
								unldl_count+=rset3.getDouble(3);
							}
							else
							{
								unldl_count+=rset3.getDouble(3);
							}*/
						}
						else
						{
							VMIN_ALLOC_DT.add("");
							VMAX_ALLOC_DT.add("");
							//VUNLOADED_QTY.add("0.00");
							//VAVAILABLE_FOR_SALE.add("0.00");
						}
						rset3.close();
						stmt3.close();
						
						double balance_qty=booked_qty-unldl_qty;
						total_balance+=balance_qty;
						VBALANCE_QTY.add(utilBean.RateNumberFormat(balance_qty,"1"));
						VBAL_INFO.add(utilBean.RateNumberFormat(booked_qty,"1")+"-"+utilBean.RateNumberFormat(unldl_qty,"1")+"="+utilBean.RateNumberFormat(balance_qty,"1"));
					}
					
					VTOTAL_QUANTITY.add(utilBean.RateNumberFormat(count,"1"));
					VTOTAL_UNLOADED_QUANTITY.add(utilBean.RateNumberFormat(unldl_count,"1"));
					VTOTAL_BALANCE.add(utilBean.RateNumberFormat(total_balance,"1"));
					VINDEX.add(index);
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
	
	public void getExchangeRateMaster()
	{
		String function_nm="getExchangeRateMaster()";
		try
		{
			
			String queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG "
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
			
			String queryString = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
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
	
	public void getBillingDetail()
	{
		String function_nm="getBillingDetail()";
		try
		{
			String queryString5 = "SELECT SPLIT_FLAG "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, counterparty_cd);
			stmt5.setString(3, agmt_no);
			stmt5.setString(4, cont_no);
			stmt5.setString(5, contract_type);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				split_flag = rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
			
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? ";
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
							+ "BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,SPLIT_FLAG,COUNTERPARTY_CD "
							+ "FROM FMS_TRADER_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k));
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
						sat_days=rset.getString(18)==null?"":rset.getString(18);
						plant_seq=rset.getString(19)==null?"":rset.getString(19);
						//split_flag=rset.getString(20)==null?"":rset.getString(20);
						contpty_cd=rset.getString(21)==null?"":rset.getString(21);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
						
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_TRADER_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k));
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
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, contpty_cd);
								stmt4.setString(3, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
								holiday_state+="@@"+plant_seq+"//"+contpty_cd+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+contpty_cd+"//"+state_map;
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
						VCONTPTY_CD.add(contpty_cd);
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k));
						stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							contpty_cd = ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k);
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
							contpty_cd = ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k);
							String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
								holiday_state+="@@"+plant_seq+"//"+contpty_cd+"//"+state_map;
							}
							else
							{
								holiday_state+=plant_seq+"//"+contpty_cd+"//"+state_map;
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
						VCONTPTY_CD.add(contpty_cd);
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
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
							+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
							+ "AND C.SEQ_NO=B.SEQ_NO)";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k));
					stmt4.setString(3, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						contpty_cd = ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k);
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
						contpty_cd = ""+VSELECTED_PLANT_COUNTPTY_CD.elementAt(k);
						String state_cd="24"; //In Case of Selected Plant Have State Outside The INDIA, Consider Gujarat as Default.
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,contpty_cd, comp_cd, plant_seq, "T");
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
							holiday_state+="@@"+plant_seq+"//"+contpty_cd+"//"+state_map;
						}
						else
						{
							holiday_state+=plant_seq+"//"+contpty_cd+"//"+state_map;
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
					VCONTPTY_CD.add(contpty_cd);
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
			String queryString2="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt2=conn.prepareStatement(queryString2);
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
						+ "FROM FMS_TRADER_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=conn.prepareStatement(queryString);
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
					
					String sysdate = dateUtil.getSysdate();
					String periodStDt=utilBean.getFirstDtOfBillingCycle(billing_freq, "", sysdate);			
					if(dateUtil.getDays(periodStDt, cont_start_dt)<1)
					{
						periodStDt=cont_start_dt;
					}

					String queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A, FMS_TAX_STRUCTURE B "
							+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? "
							+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
							+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, "T");
					stmt1.setString(2, comp_cd);
					stmt1.setString(3, counterparty_cd);
					stmt1.setString(4, plant_seq);
					stmt1.setString(5, bu_plant_seq);
					stmt1.setString(6, periodStDt);
					//stmt1.setString(6, cont_start_dt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
						VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("T", "P", "S"));			//Pratham Bhatt 20240820: for invoice type
						VINVOICE_CATEGORY.add("Tax/Retail Invoice");						//Pratham Bhatt 20240822: for invoice category
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
	
	public String getSegmentNm(String seg_ty)
	{
		String function_nm="getSegmentNm()";
		String nm="";
		try
		{
			if(seg_ty.equals("D"))
			{
				nm="Domestic NG";
			}
			else if(seg_ty.equals("I"))
			{
				nm="IGX";
			}
			//added by Pratham Bhatt 20240619 for C.N 
			else if(seg_ty.equals("N"))
			{
				nm="LNG Cargo";
			}
			//Pratham Bhatt 20240802 for LTCORA
			else if(seg_ty.equals("L"))
			{
				nm="LTCORA";
			}
			//Pratham Bhatt 20241129 for In Tank
			else if(seg_ty.equals("T"))
			{
				nm="IN Tank LNG/RLNG";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getCountBillingDetail()
	{
		String function_nm="getCountBillingDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL "
					+ "WHERE COMPANY_CD=? "//AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			//stmt.setString(2, counterparty_cd);
			stmt.setString(2, agmt_no);
			stmt.setString(3, cont_no);
			stmt.setString(4, contract_type);
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
			String gx="K";//AS OF NOW HARDCODED 20230913
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			stmt=conn.prepareStatement(queryString);
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
	
	public void getPriceChangeHistory()
	{
		String function_nm="getPriceChangeHistory()";
		try 
		{
			//FOR DISPLAY PURPOSE
			String history="";
			
			String queryString1 = "SELECT NEW_PRICE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),FLAG "
					+ "FROM FMS_TRADER_CONT_PRICE_DTL "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO = ? "
					+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE=? "
					+ "ORDER BY SEQ_NO DESC";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, agmt_no);
			stmt.setString(3, cont_no);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				double sales = rset.getDouble(1);
				String dt =rset.getString(2)==null?"":rset.getString(2);
				String flag=rset.getString(3)==null?"":rset.getString(3);
				String color="";
				if(flag.equals("X")) 
				{
					flag="Rejected";
					color="red";
				}
				else if(flag.equals("Y")) 
				{
					flag="Approved";
					color="green";
				}
				else 
				{
					flag="Requested";
					color="blue";
				}
				
				String rateLable = "($/MMBTU)";
				if(rate_unit.trim().equals("1")) 
				{
					rateLable = "(INR/MMBTU)";
				}
				String rateFormate=""+utilBean.RateNumberFormat(sales, rate_unit);
				if(history.equals(""))
				{
					history+=""+rateFormate+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
				}
				else
				{
					history+="<br>"+rateFormate+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
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
			stmt=conn.prepareStatement(queryString);
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
			stmt=conn.prepareStatement(queryString);
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
					+ "FROM FMS_TRADER_CONT_GX_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND GX_COUNTERPARTY_CD=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, gx_counterparty_cd);
			rset=stmt.executeQuery();
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
	
	//PB FOR CHECKING THE NOMINATION DETAILS
	public void getNominatedChk()
	{
		String function_nm="getNomintatedChk()";
		try
		{
			
			for (int i = 0; i < VBU_PLANT_SEQ_NO.size(); i++) 
			{
				VNOM_SEL_BU_CHK.add(checkCustPlantNominated("" + VBU_PLANT_SEQ_NO.elementAt(i),"BU"));
			}
			for(int i=0;i<VPLANT_SEQ_NO.size();i++)		//for selected plant 
			{
				VNOM_SEL_TRADER_CHK.add(checkCustPlantNominated(""+VPLANT_SEQ_NO.elementAt(i),""));
			}
			if(split_flag.equals("Y"))
			{
				for(int i=0;i<VOTH_PLANT_SEQ_NO.size();i++)		
				{
					VNOM_SEL_OTH_TRADER_CHK.add(checkCustSplitPlantNominated(""+VOTH_PLANT_SEQ_NO.elementAt(i),""+VOTH_CONTPTY_NM.elementAt(i)));
				}
			}
			else
			{
				for(int i=0;i<VOTH_PLANT_SEQ_NO.size();i++)		
				{
					VNOM_SEL_OTH_TRADER_CHK.add("");
				}
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
			String plant;
			if ("BU".equals(type)) 
			{
			    plant = "SELECT CASE WHEN BU_SEQ=? THEN 'Y' ELSE 'N' END BU_UNIT ";
			} 
			else 
			{
			    plant = "SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT ";
			}
			String query=plant
				+ "FROM FMS_BUY_DAILY_BUYER_NOM "
				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
				+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? ";
			if ("BU".equals(type)) 
			{
				query += "AND BU_SEQ=? ";
			} 
			else 
			{
				query += "AND PLANT_SEQ=? ";
			}
			String temp_query=query;
			stmt5=conn.prepareStatement(temp_query);
			stmt5.setString(1, plant_seq);
			stmt5.setString(2, comp_cd);
			stmt5.setString(3, counterparty_cd);
			stmt5.setString(4, agmt_no);
			stmt5.setString(5, cont_no);
			stmt5.setString(6, contract_type);
			stmt5.setString(7, plant_seq);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				chk_cust_plant=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_cust_plant;
	}
	
	public String checkCustSplitPlantNominated(String plant_seq, String oth_trader_cd)
	{
		String function_nm="checkCustSplitPlantNominated()";
		String chk_cust_plant="";
		try
		{
			if(split_flag.equals("Y"))
			{
				String query="SELECT FLAG "
					+ "FROM (SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END FLAG "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
					+ "UNION ALL "
					+ "SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END FLAG "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ=?) ";
				stmt5=conn.prepareStatement(query);
				stmt5.setString(1, plant_seq);
				stmt5.setString(2, comp_cd);
				stmt5.setString(3, oth_trader_cd);
				stmt5.setString(4, agmt_no);
				stmt5.setString(5, cont_no);
				stmt5.setString(6, contract_type);
				stmt5.setString(7, plant_seq);
				stmt5.setString(8, plant_seq);
				stmt5.setString(9, comp_cd);
				stmt5.setString(10, oth_trader_cd);
				stmt5.setString(11, agmt_no);
				stmt5.setString(12, cont_no);
				stmt5.setString(13, contract_type);
				stmt5.setString(14, plant_seq);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					chk_cust_plant=rset5.getString(1)==null?"":rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return chk_cust_plant;
	}
	
	public void getPurchaseContCounterpartyList()
	{
		String function_nm="getPurchaseContCounterpartyList()";
		try
		{
			String queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_TRADER_CONT_MST "
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
	
	//PB 20260223: for fetching the latest status of the counterparty
	public Map<String, Object> getCounterpartyStatus(String counterparty_cd)
	{
		Map<String, Object> counterpartyStatus = new HashMap<>();
		String function_nm="getCounterpartyStatus()";
		String status="";
		String eff_dt="";
		try
		{
			String query = "SELECT STATUS,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE COUNTERPARTY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
					+ "ORDER BY COUNTERPARTY_NM ";
			stmt5=conn.prepareStatement(query);
			stmt5.setString(1, counterparty_cd);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				status=rset5.getString(1)==null?"":rset5.getString(1);
				eff_dt=rset5.getString(2)==null?"":rset5.getString(2);
			}
			rset5.close();
			stmt5.close();
			
			counterpartyStatus.put("status", status);
			counterpartyStatus.put("eff_dt", eff_dt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return counterpartyStatus;
	}
	
	//PB 20260311 for CTR reference
	public void getCtrTraderList()
	{
		String function_nm="getCtrTraderList()";
		try
		{
			utilBean.getAllEntityCounterpartyList(conn,comp_cd,"T");
			VMST_COUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VMST_COUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VMST_COUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
			/*String query="SELECT DISTINCT COUNTERPARTY_CD  "
					+ "FROM FMS_TRADER_CONT_MST  "
					+ "WHERE COMPANY_CD=? AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ " "
					+ "UNION "
					+ "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CN_MST "
					+ "WHERE COMPANY_CD=? AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ " "
					+ "UNION "
					+ "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_LTCORA_CONT_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND BUY_SALE='T' AND AGMT_TYPE='L' "
					+ "AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ " ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String trader_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VMST_COUNTERPARTY_CD.add(trader_cd);
				VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, trader_cd));
				VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, trader_cd));
			}
			rset.close();
			stmt.close();*/
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPurCtrList()
	{
		String function_nm="getPurCtrList()";
		try
		{
			int row_count=0;
			String query="SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,CARGO_NO,CTR_REF, "
					+ "TO_CHAR(EFF_DT,'DD/MM/YYYY'),PROD_CD,MOLE_CD,PLANT_SEQ_NO,BU_SEQ, "
					+ "(SELECT COUNT(*) FROM FMS_BUY_CTR_MST B WHERE A.CTR_REF=B.CTR_REF) CTR_COUNT "
					+ "FROM FMS_BUY_CTR_MST A "
					+ "WHERE COMPANY_CD=? ";
					//+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BUY_CTR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					//+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					//+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.CTR_REF=B.CTR_REF "
					//+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_SEQ=B.BU_SEQ  AND B.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
					//+ "AND EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			//if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				query+="AND COUNTERPARTY_CD=? ";
			}
			query+="ORDER BY CTR_REF,EFF_DT DESC ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			//stmt.setString(2, from_dt);
			//stmt.setString(3, to_dt);
			//if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				row_count++;
				String company_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt_no=rset.getString(3)==null?"":rset.getString(3);
				String cont_no=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cargo_no=rset.getString(6)==null?"":rset.getString(6);
				String ctr_ref=rset.getString(7)==null?"":rset.getString(7);
				String eff_dt=rset.getString(8)==null?"":rset.getString(8);
				String prod_cd=rset.getString(9)==null?"":rset.getString(9);
				String mole_cd=rset.getString(10)==null?"":rset.getString(10);
				String plant_seq=rset.getString(11)==null?"":rset.getString(11);
				String bu_seq=rset.getString(12)==null?"":rset.getString(12);
				int ctr_count=rset.getInt(13);
				
				if(row_count==ctr_count)
				{
					VINDEX.add(ctr_count);
					row_count=0;
				}
				
				String pur_deal_map = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt_no, "", cont_no, "", cont_type, cargo_no);
				String mole_nm=getMoleculeNm(prod_cd,mole_cd);
				String prod_nm=getProductABBR(prod_cd);
				
				String cont_ref=getCtrContDtls(countpty_cd, agmt_no,cont_no,cont_type,cargo_no);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, countpty_cd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, countpty_cd));
				VAGMT_NO.add(agmt_no);
				VCONT_NO.add(cont_no);
				VCONTRACT_TYPE.add(cont_type);
				VCARGO_NO.add(cargo_no);
				VDEAL_MAP.add(pur_deal_map);
				VEFF_DT.add(eff_dt);
				VPROD_CD.add(prod_cd);
				VMOLE_CD.add(mole_cd);
				VPLANT_SEQ_NO.add(plant_seq);
				VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn, countpty_cd, comp_cd, plant_seq, "T"));
				VPLANT_NAME.add(""+utilBean.getCounterpartyPlantName(conn, countpty_cd, comp_cd, plant_seq, "T"));
				VBU_PLANT_SEQ_NO.add(bu_seq);
				VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn, comp_cd, comp_cd, bu_seq, "B"));
				VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantName(conn, comp_cd, comp_cd, bu_seq, "B"));
				VMOLECULE_NM.add(mole_nm);
				VPRODUCT_NM.add(prod_nm);
				VCTR_REF.add(ctr_ref);
				VCONT_REF_NO.add(cont_ref);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getActiveContractListForCTR()
	{
		String function_nm="getActiveContractListForCTR()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			cpStatus = (String) utilBean.getCounterpartyDetails(conn, counterparty_cd, sysdate).get("status");
			
			String query="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,NULL CARGO_NO "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO  "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "UNION ALL "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
					+ "AND NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1))>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) "
					+ "UNION ALL "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND END_DT>=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO  "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, comp_cd);
			stmt.setString(6, counterparty_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String company_cd=rset.getString(1)==null?"":rset.getString(1);
				String counterpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt_no=rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_no=rset.getString(5)==null?"":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				String cargo_no=rset.getString(8)==null?"0":rset.getString(8);
				
				String disp_deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
				
				String cont_ref=getCtrContDtls(counterpty_cd,agmt_no,cont_no,cont_type,cargo_no);
				
				//for plant
				String query1="SELECT DISTINCT A.PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_PLANT C "
						+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO) "
						+ "UNION "
						+ "SELECT DISTINCT A.PLANT_SEQ_NO  "
						+ "FROM FMS_LTCORA_CONT_PLANT A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
						+ "AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_LTCORA_CONT_PLANT C "
						+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
						+ "AND A.AGMT_TYPE=C.AGMT_TYPE AND A.BUY_SALE=C.BUY_SALE)  ";
				stmt1=conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterpty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, counterpty_cd);
				stmt1.setString(8, agmt_no);
				stmt1.setString(9, cont_no);
				stmt1.setString(10, cont_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
					VCTR_PLANT_SEQ_NO.add(plant_seq);
					VCTR_PLANT_NM.add(""+utilBean.getCounterpartyPlantName(conn, counterpty_cd, comp_cd, plant_seq, "T"));
					VCTR_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn, counterpty_cd, comp_cd, plant_seq, "T"));
				}
				rset1.close();
				stmt1.close();
				
				//for bu
				String query2="SELECT DISTINCT A.PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_TRADER_CONT_BU C "
						+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO  "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO) "
						+ "UNION "
						+ "SELECT DISTINCT A.PLANT_SEQ_NO  "
						+ "FROM FMS_LTCORA_CONT_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
						+ "AND CONT_REV=(SELECT MAX(C.CONT_REV) FROM FMS_LTCORA_CONT_BU C "
						+ "WHERE A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_NO=C.AGMT_NO  "
						+ "AND A.CONT_NO=C.CONT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
						+ "AND A.AGMT_TYPE=C.AGMT_TYPE AND A.BUY_SALE=C.BUY_SALE) ";
				stmt1=conn.prepareStatement(query2);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterpty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, counterpty_cd);
				stmt1.setString(8, agmt_no);
				stmt1.setString(9, cont_no);
				stmt1.setString(10, cont_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
					VCTR_BU_PLANT_SEQ_NO.add(plant_seq);
					VCTR_BU_PLANT_NM.add(""+utilBean.getCounterpartyPlantName(conn, comp_cd, comp_cd, plant_seq, "B"));
					VCTR_BU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn, comp_cd, comp_cd, plant_seq, "B"));
				}
				rset1.close();
				stmt1.close();
				
				VCTR_COUNTERPARTY_CD.add(counterpty_cd);
				VCTR_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				VCTR_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterpty_cd));
				VCTR_CONT_NO.add(cont_no);
				VCTR_AGMT_NO.add(agmt_no);
				VCTR_CONT_TYPE.add(cont_type);
				VCTR_DISP_CONT_NO.add(disp_deal_map);
				VCTR_CARGO_NO.add(cargo_no);
				VCTR_CONT_REF_NO.add(cont_ref);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getProductMst()
	{
		String function_nm="getProductMst()";
		try
		{
			String query="SELECT PROD_CD,PROD_ABBR,PROD_NM "
					+ "FROM FMS_PRODUCT_MST "
					+ "WHERE PROD_FLAG=? ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String prod_cd=rset.getString(1)==null?"":rset.getString(1);
				String prod_abbr=rset.getString(2)==null?"":rset.getString(2);
				String prod_nm=rset.getString(3)==null?"":rset.getString(3);
				//String mole_abbr=rset.getString(4)==null?"":rset.getString(4);
				
				VCTR_PROD_CD.add(prod_cd);
				VCTR_PROD_ABBR.add(prod_abbr);
				//VCTR_PROD_ABBR.add(getProductABBR(prod_cd));
				//VCTR_MOLE_CD.add(mole_cd);
				//VCTR_MOLE_NM.add(mole_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getMoleculeNm(String prod_cd,String mole_cd)
	{
		String function_nm="getMoleculeNm()";
		String nm="";
		try
		{
			String query="SELECT MOLE_ABBR "
					+ "FROM FMS_PRODUCT_MOLECULE_MST "
					+ "WHERE PROD_CD=? AND MOLE_CD=? AND MOLE_FLAG=?";
			stmt5=conn.prepareCall(query);
			stmt5.setString(1, prod_cd);
			stmt5.setString(2, mole_cd);
			stmt5.setString(3, "Y");
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				nm=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public String getProductABBR(String prod_cd)
	{
		String function_nm="getProductABBR()";
		String nm="";
		try
		{
			String query="SELECT PROD_ABBR "
					+ "FROM FMS_PRODUCT_MST "
					+ "WHERE PROD_CD=? AND PROD_FLAG=? ";
			stmt5=conn.prepareCall(query);
			stmt5.setString(1, prod_cd);
			stmt5.setString(2, "Y");
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				nm=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public String getCtrContDtls(String countpty_Cd, String agmt, String cont, String cont_type, String cargo_no)
	{
		String function_nm="getCtrContDtls()";
		String cont_dtl="";
		try
		{
			int stmt_ctn=0;
			String query="SELECT CONT_REF_NO "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM  FMS_TRADER_CONT_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_rEV AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "UNION "
					+ "SELECT CARGO_REF  "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE='M' "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) "
					+ "UNION "
					+ "SELECT CARGO_REF "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE='T' AND AGMT_TYPE='L' "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			stmt5=conn.prepareStatement(query);
			stmt5.setString(++stmt_ctn, comp_cd);
			stmt5.setString(++stmt_ctn, countpty_Cd);
			stmt5.setString(++stmt_ctn, agmt);
			stmt5.setString(++stmt_ctn, cont);
			stmt5.setString(++stmt_ctn, cont_type);
			stmt5.setString(++stmt_ctn, comp_cd);
			stmt5.setString(++stmt_ctn, countpty_Cd);
			stmt5.setString(++stmt_ctn, agmt);
			stmt5.setString(++stmt_ctn, cont);
			stmt5.setString(++stmt_ctn, cont_type);
			stmt5.setString(++stmt_ctn, cargo_no);
			stmt5.setString(++stmt_ctn, comp_cd);
			stmt5.setString(++stmt_ctn, countpty_Cd);
			stmt5.setString(++stmt_ctn, agmt);
			stmt5.setString(++stmt_ctn, cont);
			stmt5.setString(++stmt_ctn, cont_type);
			stmt5.setString(++stmt_ctn, cargo_no);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				cont_dtl=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return cont_dtl;
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
	String cargo_no = "0";
	String cont_start_dt = "";
	String segment="";
	String gas_dt="";
	String nomination_freq="";
	String nomination_type="";
	String segmentType = "";
	String from_dt = "";
	String to_dt = "";
	String active_status = "";
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setSegment(String segment) {this.segment = segment;}
	public void setGas_dt(String gas_dt) {this.gas_dt = gas_dt;}
	public void setNomination_freq(String nomination_freq) {this.nomination_freq = nomination_freq;}
	public void setNomination_type(String nomination_type) {this.nomination_type = nomination_type;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
	Vector VOTH_CONTPTY_NM = new Vector();
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VOTH_COUNTERPARTY_CD = new Vector();
	Vector VOTH_COUNTERPARTY_NM = new Vector();
	Vector VOTH_COUNTERPARTY_ABBR = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VOTH_PLANT_NM = new Vector();
	Vector VOTH_PLANT_ABBR = new Vector();
	Vector VOTH_PLANT_SEQ_NO = new Vector();
	Vector VTRANS_CD = new Vector();
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
	Vector VBUYER_NAME = new Vector();
	Vector VBUYER_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VDISP_CONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VTCQ = new Vector();
	Vector VQTY_UNIT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRANS_CD = new Vector();
	Vector VSEL_TRANS_PLANT_SEQ_NO = new Vector();
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
	Vector VFORMULA_ID = new Vector();
	Vector VFORMULA_NM = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VDISPLAY_SEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VDISPLAY_SEGMENT_TYP = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VINDEX = new Vector();
	Vector VRECEIVED_QTY = new Vector();
	Vector VCONT_CARGO = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VDEAL_MAP =new Vector();
	
	Vector VBU_POINT=new Vector();
	Vector VTRADER_PLANT=new Vector();
	Vector VSEL_SPLIT_VALUE=new Vector();
	Vector VSEL_CHARGE_VALUE=new Vector();
	Vector VSEL_CHARGE_DESC=new Vector();
	Vector VOTH_SEL_CHARGE_VALUE=new Vector();
	Vector VOTH_SEL_CHARGE_DESC=new Vector();
	Vector VOTH_SEL_SPLIT_VALUE=new Vector();
	Vector VOTH_SEL_TRAD_CD = new Vector();
	Vector VOTH_SEL_PLANT_SEQ_NO = new Vector();
	Vector VOTH_SEL_PLANT_ABBR = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	//Added by Pratham Bhatt for CN Cargo
	Vector VCARGO_NAME =new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VCARGO_REF = new Vector();
	Vector VBALANCE_QTY = new Vector();		//Pratham Bhatt 20240910: for adding data for column balance_qty
	Vector VINVOICE_TYPE = new Vector(); //Pratham Bhatt20240820: For adding Invoice Type 
	Vector VINVOICE_CATEGORY = new Vector(); //Pratham Bhatt 20240822: For adding Invoice Category
	Vector VBAL_INFO = new Vector();
	Vector VBOOKED_INFO = new Vector();
	Vector VTOTAL_BALANCE = new Vector(); 
	Vector VBALANCE_SIGN = new Vector();
	Vector VCLOSURE_REQUEST_FLG = new Vector(); 
	Vector VCLOSURE_NOTE = new Vector();
	Vector VCLOSEURE_EFF_DT = new Vector();
	Vector VBUY_SALE = new Vector();
	
	Vector VCHARGE_ABBR = new Vector();
	Vector VCHARGE_NAME = new Vector();
	
	Vector VTOTAL_QUANTITY = new Vector();
	Vector VTOTAL_UNLOADED_QUANTITY = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VFROM_DT = new Vector();
	Vector VTO_DT = new Vector();
	Vector VDCQ = new Vector();
	Vector VREMARK = new Vector();
	Vector VSTATUS = new Vector();
	
	Vector VIS_RADIO_ENABLE = new Vector();
	
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VPLANT_SEQ=new Vector();
	Vector VCONTPTY_CD=new Vector();
	Vector VSELECTED_PLANT_COUNTPTY_CD=new Vector();
	Vector VDEAL_MAPPING=new Vector();
	Vector VNEW_START_DT=new Vector();
	Vector VNEW_END_DT=new Vector();
	
	Vector VMIN_NOM_DT = new Vector();
	Vector VMAX_DT = new Vector();
	Vector VSIGNING_DT = new Vector();
	
	Vector VNOM_SEL_BU_CHK = new Vector();
	Vector VNOM_SEL_OTH_TRADER_CHK = new Vector();
	Vector VNOM_SEL_TRADER_CHK =new Vector();
	
	Vector VCOUNTERPARTY_STATUS = new Vector();
	Vector VSTATUS_EFF_DT = new Vector();
	
	//
	Vector VEFF_DT = new Vector();
	Vector VMOLE_CD = new Vector();
	Vector VPROD_CD = new Vector();
	Vector VMOLECULE_NM = new Vector();
	Vector VCTR_REF = new Vector();
	
	Vector VCTR_COUNTERPARTY_CD = new Vector();
	Vector VCTR_COUNTERPARTY_NM = new Vector();
	Vector VCTR_COUNTERPARTY_ABBR = new Vector();
	Vector VCTR_PLANT_NM = new Vector();
	Vector VCTR_PLANT_ABBR = new Vector();
	Vector VCTR_PLANT_SEQ_NO = new Vector();
	Vector VCTR_BU_PLANT_NM = new Vector();
	Vector VCTR_BU_PLANT_ABBR = new Vector();
	Vector VCTR_BU_PLANT_SEQ_NO = new Vector();
	Vector VCTR_CONT_NO = new Vector();
	Vector VCTR_AGMT_NO = new Vector();
	Vector VCTR_DISP_CONT_NO = new Vector();
	Vector VCTR_CARGO_NO = new Vector();
	Vector VCTR_CONT_TYPE = new Vector();
	Vector VCTR_PROD_CD = new Vector();
	Vector VCTR_MOLE_CD = new Vector();
	Vector VCTR_MOLE_NM = new Vector();
	Vector VCTR_CONT_REF_NO = new Vector();
	Vector VCTR_PROD_ABBR = new Vector();
	Vector VPRODUCT_NM = new Vector();
	
	public Vector getVBUY_SALE() {return VBUY_SALE;}
	public Vector getVCLOSEURE_EFF_DT() {return VCLOSEURE_EFF_DT;}
	public Vector getVCLOSURE_NOTE() {return VCLOSURE_NOTE;}
	public Vector getVCLOSURE_REQUEST_FLG() {return VCLOSURE_REQUEST_FLG;}
	public Vector getVBALANCE_SIGN() {return VBALANCE_SIGN;}
	public Vector getVTOTAL_BALANCE() {return VTOTAL_BALANCE;}
	public Vector getVBOOKED_INFO() {return VBOOKED_INFO;}
	public Vector getVBAL_INFO() {return VBAL_INFO;}
	public Vector getVBALANCE_QTY() {return VBALANCE_QTY;}		//Pratham BHatt 20240910
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}			//Pratham Bhatt20240820: For  Invoice Type 
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}	//Pratham Bhatt20240822: For Invoice Category
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVOTH_COUNTERPARTY_CD() {return VOTH_COUNTERPARTY_CD;}
	public Vector getVOTH_COUNTERPARTY_NM() {return VOTH_COUNTERPARTY_NM;}
	public Vector getVOTH_COUNTERPARTY_ABBR() {return VOTH_COUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVOTH_PLANT_NM() {return VOTH_PLANT_NM;}
	public Vector getVOTH_PLANT_ABBR() {return VOTH_PLANT_ABBR;}
	public Vector getVOTH_PLANT_SEQ_NO() {return VOTH_PLANT_SEQ_NO;}
	public Vector getVTRANS_CD() {return VTRANS_CD;}
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
	public Vector getVBUYER_NAME() {return VBUYER_NAME;}
	public Vector getVBUYER_ABBR() {return VBUYER_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVDISP_CONT_NO() {return VDISP_CONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_TRANS_CD() {return VSEL_TRANS_CD;}
	public Vector getVSEL_TRANS_PLANT_SEQ_NO() {return VSEL_TRANS_PLANT_SEQ_NO;}
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
	public Vector getVFORMULA_ID() {return VFORMULA_ID;}
	public Vector getVFORMULA_NM() {return VFORMULA_NM;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVDISPLAY_SEGMENT() {return VDISPLAY_SEGMENT;}
	public Vector getVDISPLAY_SEGMENT_TYP() {return VDISPLAY_SEGMENT_TYP;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVRECEIVED_QTY() {return VRECEIVED_QTY;}
	public Vector getVCONT_CARGO() {return VCONT_CARGO;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVTRADER_PLANT() {return VTRADER_PLANT;}
	public Vector getVSEL_SPLIT_VALUE() {return VSEL_SPLIT_VALUE;}
	public Vector getVSEL_CHARGE_VALUE() {return VSEL_CHARGE_VALUE;}
	public Vector getVSEL_CHARGE_DESC() {return VSEL_CHARGE_DESC;}
	public Vector getVOTH_SEL_CHARGE_VALUE() {return VOTH_SEL_CHARGE_VALUE;}
	public Vector getVOTH_SEL_CHARGE_DESC() {return VOTH_SEL_CHARGE_DESC;}
	public Vector getVOTH_SEL_SPLIT_VALUE() {return VOTH_SEL_SPLIT_VALUE;}
	public Vector getVOTH_SEL_TRAD_CD() {return VOTH_SEL_TRAD_CD;}
	public Vector getVOTH_SEL_PLANT_SEQ_NO() {return VOTH_SEL_PLANT_SEQ_NO;}
	public Vector getVOTH_SEL_PLANT_ABBR() {return VOTH_SEL_PLANT_ABBR;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	//Added by Pratham Bhatt for CN Cargo
	public Vector getVCARGO_NAME() {return VCARGO_NAME;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVCARGO_REF() {return VCARGO_REF;}
	
	public Vector getVCHARGE_ABBR() {return VCHARGE_ABBR;}
	public Vector getVCHARGE_NAME() {return VCHARGE_NAME;}
	
	public Vector getVTOTAL_QUANTITY() {return VTOTAL_QUANTITY;}
	public Vector getVTOTAL_UNLOADED_QUANTITY() {return VTOTAL_UNLOADED_QUANTITY;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVFROM_DT() {return VFROM_DT;}
	public Vector getVTO_DT() {return VTO_DT;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVSTATUS() {return VSTATUS;}
	
	public Vector getVIS_RADIO_ENABLE() {return VIS_RADIO_ENABLE;}
	
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVCONTPTY_CD() {return VCONTPTY_CD;}
	public Vector getVSELECTED_PLANT_COUNTPTY_CD() {return VSELECTED_PLANT_COUNTPTY_CD;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	public Vector getVNEW_START_DT() {return VNEW_START_DT;}
	public Vector getVNEW_END_DT() {return VNEW_END_DT;}
	
	public Vector getVMAX_DT() {return VMAX_DT;}
	public Vector getVMIN_NOM_DT() {return VMIN_NOM_DT;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	
	public Vector getVNOM_SEL_BU_CHK() {return VNOM_SEL_BU_CHK;}
	public Vector getVNOM_SEL_OTH_TRADER_CHK() {return VNOM_SEL_OTH_TRADER_CHK;}
	public Vector getVNOM_SEL_TRADER_CHK() {return VNOM_SEL_TRADER_CHK;}
	
	public Vector getVCOUNTERPARTY_STATUS() {return VCOUNTERPARTY_STATUS;}
	public Vector getVSTATUS_EFF_DT() {return VSTATUS_EFF_DT;}
	
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVMOLE_CD() {return VMOLE_CD;}
	public Vector getVPROD_CD() {return VPROD_CD;}
	public Vector getVMOLECULE_NM() {return VMOLECULE_NM;}
	public Vector getVCTR_REF() {return VCTR_REF;}
	
	public Vector getVCTR_COUNTERPARTY_CD(){return VCTR_COUNTERPARTY_CD; }
	public Vector getVCTR_COUNTERPARTY_NM(){return VCTR_COUNTERPARTY_NM;}
	public Vector getVCTR_COUNTERPARTY_ABBR(){return VCTR_COUNTERPARTY_ABBR;}
	public Vector getVCTR_PLANT_NM(){return VCTR_PLANT_NM;}
	public Vector getVCTR_PLANT_ABBR(){return VCTR_PLANT_ABBR;}
	public Vector getVCTR_PLANT_SEQ_NO(){return VCTR_PLANT_SEQ_NO;}
	public Vector getVCTR_BU_PLANT_NM(){return VCTR_BU_PLANT_NM;}
	public Vector getVCTR_BU_PLANT_ABBR(){return VCTR_BU_PLANT_ABBR;}
	public Vector getVCTR_BU_PLANT_SEQ_NO(){return VCTR_BU_PLANT_SEQ_NO;}
	public Vector getVCTR_CONT_NO(){return VCTR_CONT_NO;}
	public Vector getVCTR_AGMT_NO(){return VCTR_AGMT_NO;}
	public Vector getVCTR_DISP_CONT_NO(){return VCTR_DISP_CONT_NO;}
	public Vector getVCTR_CARGO_NO(){return VCTR_CARGO_NO;}
	public Vector getVCTR_CONT_TYPE(){return VCTR_CONT_TYPE;}
	public Vector getVCTR_PROD_CD(){return VCTR_PROD_CD;}
	public Vector getVCTR_MOLE_CD(){return VCTR_MOLE_CD;}
	public Vector getVCTR_MOLE_NM(){return VCTR_MOLE_NM;}
	public Vector getVCTR_CONT_REF_NO(){return VCTR_CONT_REF_NO;}
	public Vector getVCTR_PROD_ABBR(){return VCTR_PROD_ABBR;}
	public Vector getVPRODUCT_NM(){return VPRODUCT_NM;}
	
	String min_counterparty_eff_dt = "";
	//String cont_no = "";
	//String cont_rev_no = "";
	String conf_price ="";	//added by pratham
	String win_start_dt ="";	//added by Pratham
	String win_end_dt ="";		//added by Pratham
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
	String buy_month_nom = "";
	String buy_fortnightly_nom = "";
	String buy_week_nom = "";
	String buy_daily_nom = "";
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
	String due_dt_in="";
	String exclude_sat="";
	String no_of_billing_dtl="";
	String no_of_security_dtl="";
	String billing_days="";
	String received_qty="";
	String price_change_history="";
	String gx_counterparty_cd = "";
	String balTotalQty_str = "";	//Pratham Bhatt20240802: For LTCORA
	String bookedToolTip = "";		//Pratham Bhatt20240802: Tool-tip for LTCORA 
	
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
	String liability_lq_dmg = "";
	String liability_take_pay = "";
	String liability_makeup = "";
	String ld_from = "";
	String ld_to = "";
	String top_from = "";
	String top_to = "";
	
	
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
	String billing_clause = "";
	String termination_flag = "";
	String termination_clause = "";
	String termination_planned = "";
	String termination_forced = "";
	String spec_gas_clause = "";
	String measurement_clause = "";
	String day_def_clause = "";
	
	String dealMapping="";
	String display_map_id="";
	String counterparty_abbr="";
	String contpty_name="";
	String mapped_cont_no="";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	String contpty_cd="";
	
	String closure_req_flag="";		//PB20250418
	String closure_mmbtu="";		//PB20250418
	String closure_eff_dt="";		//PB20250418
	String closure_note="";			//PB20250418
	String is_expired="";
	String is_nominated="";
	String delta_tcq_sign="";
	String contdt_change_request_flag="";
	String max_date="";
	String min_nom_date="";
	
	String cpStatus="";
	
	public String getDelta_tcq_sign() {return delta_tcq_sign;}		//PB20250418
	public String getClosure_req_flag() {return closure_req_flag;}		//PB20250418
	public String getClosure_mmbtu() {return closure_mmbtu;}			//PB20250418
	public String getClosure_eff_dt() {return closure_eff_dt;}			//PB20250418
	public String getClosure_note() {return closure_note;}				//PB20250418
	public String getIs_expired() {return is_expired;} 					//PB20250418
	public String getIs_nominated() {return is_nominated;}				
	
	public String getBalTotalQty_str() {return balTotalQty_str;}	//For LTCORA Balance Quantity  
	public String getBookedToolTip() {return bookedToolTip;}		//For LTCORA Booked Tool Tip formula
	
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	//public String getCont_no() {return cont_no;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getConf_price() {return conf_price;}			//added by Pratham
	public String getWin_start_dt() {return win_start_dt;}		//added by Pratham 
	public String getWin_end_dt() {return win_end_dt;}			//added by Pratham
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
	public String getDue_dt_in() {return due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
	public String getNo_of_security_dtl() {return no_of_security_dtl;}
	public String getBilling_days() {return billing_days;}
	public String getReceived_qty() {return received_qty;}
	public String getPrice_change_history() {return price_change_history;}
	public String getGx_counterparty_cd() {return gx_counterparty_cd;}
	
	public String getDealMapping() {return dealMapping;}
	public String getDisplay_map_id() {return display_map_id;}
	public String getCounterparty_abbr() {return counterparty_abbr;}

	public String getcpStatus() {return cpStatus;}
	
	double totalQty=0;
	double UnloadedtotalQty=0;
	double AvailabletotalQty=0;

	public Double getTotalQty() {return totalQty;}
	public Double getUnloadedtotalQty() {return UnloadedtotalQty;}
	public Double getAvailabletotalQty() {return AvailabletotalQty;}
	
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
	public String getLiability_lq_dmg() { return liability_lq_dmg; }
	public String getLiability_take_pay() { return liability_take_pay; }
	public String getLiability_makeup() { return liability_makeup; }
	public String getLd_from() {return ld_from;}
	public String getLd_to() { return ld_to; }
	public String getTop_from() { return top_from; }
	public String getTop_to() { return top_to; }
	
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
	public String getTermination_flag() {return termination_flag;}
	public String getTermination_clause() {return termination_clause;}
	public String getTermination_planned() {return termination_planned;}
	public String getTermination_forced() {return termination_forced;}
	public String getDay_def_clause() {return day_def_clause;}
	public String getMeasurement_clause() {return measurement_clause;}
	public String getSpec_gas_clause() {return spec_gas_clause;}
	public String getBilling_clause() {return billing_clause;}
	public String getContpty_name() {return contpty_name;}
	public String getMapped_cont_no() {return mapped_cont_no;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
	public String getContpty_cd() {return contpty_cd;}
	public String getContdt_change_request_flag() {return contdt_change_request_flag;}
	public String getMax_date() {return max_date;}
	public String getMin_nom_date() {return min_nom_date;}
	
	public Vector getVSEGMENT_TYPE() {
		return VSEGMENT_TYPE;
	}

	public Vector getVTEMP_SEGMENT_TYPE() {
		return VTEMP_SEGMENT_TYPE;
	}
	public Vector getVTEMP_SEGMENT()
	{
		return VTEMP_SEGMENT;
	}
	public Vector getVDEAL_MAP()
	{
		return VDEAL_MAP;
	}
}
