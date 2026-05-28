package com.etrm.fms.ltcora;

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

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 06/06/2024 
//Status	  		: Developing

public class DataBean_LtcoraMaster 
{
	String db_src_file_name="DataBean_LtcoraMaster.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt_temp;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset_temp;
	/*String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";*/
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	String clearance = "KYC";
	
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
	    			if(callFlag.equalsIgnoreCase("LTCORA_AGREEMENT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					if(buy_sale.equals("T"))
	    					{
	    						getTraderCounterpartyList();
	    					}
	    					else if(buy_sale.equals("C"))
	    					{
	    						getCustomerCounterpartyList();
	    					}
	    				}
	    				else 
	    				{
	    					getCounterpartywithAgmtList();
	    					getCurrentContractDtl();
	    				}
	    				getCounterpartyPlantList();
	    				getBusinessPlantList();
	    				getTransporterPlantList();
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getSelectedLtcoraAgmtLiabilityDtl();
	    					getLTCORAAgreementDetail();
	    					getSelectedTraderPlantList();
	    					getSelectedTransporterPlantList();
	    					getSelectedBusinessPlantList();
	    					getCountAgreementBillingDetail();
	    				}	
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_AGREEMENT_LIST"))
	    			{
	    				getLTCORAAgreementList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_AGREEMENT_BILLING_DTL"))
	    			{
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getLTCORAAgreementBillingDetail();
	    				getLTCORAAgreementApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CONTRACT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCounterpartywithAgmtList();
	    				}
	    				else 
	    				{
	    					getCounterpartywithContList();
	    				}
	    				getCounterpartyPlantList();
	    				getBusinessPlantList();
	    				getTransporterPlantList();
	    				getTluChargeMaster();
	    				getFillingStationList();
	    				getTruckTransporterList();
	    				
	    				getLTCORAContractDetail();
	    				getContractSelectedCounterpartyPlantList();
		    			getContractSelectedBusinessPlantList();
		    			getContractSelectedTransporterPlantList();
		    			getLtcoraCnCountBillingDetail();
	    				getLtcoraCnCountSecurityDetail();
	    				getSelectedLtcoraCnLiabilityDtl();
	    				//getAgmtBaseFormAgmt();
	    				getNominatedChk();
	    				getContractSelectedDlngCustomerPlantList();
    					getContractSelectedTruckTransporterList();
    					getContractSelectedFillingStationList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CN_BILLING_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getLtcoraCnCountBillingDetail();
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getLtcoraCnBillingDetail();
	    				getLtcoraCnApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CONTRACT_LIST"))
	    			{
	    				getLtcoraContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CN_CARGO_DTL"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				getLtcoraContTraderCounterpartyList();
	    				getLtcoraContractCargoDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CARGO_ARRIVAL_LIST"))
	    			{
	    				getAllocationCargoList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_TRADER_DTL"))
	    			{
	    				getTraderCounterpartyList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_UNLOADED_QUANT_DTL"))
	    			{
	    				getLtcoraUnloadedQuantDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CONT_CARGO_CSOC"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				getLtcoraVariableCsocDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CN_CARGO_STORAGE_DTL"))
	    			{
	    				getLtcoraCnCargoStorageDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_AGMT_LIABILITY_CLAUSE"))
	    			{
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				getLtcoraAgmtLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CN_LIABILITY_CLAUSE"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				getLtcoraCnLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CARGO_MODREQ"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				getCargoReqModDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_VARIABLE_TERIFF"))
	    			{
	    				getLtcoraVariableTeriffDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_CARGO_CLOSURE_REQUEST"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				getLtcoraClosureDtls();
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
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getLtcoraClosureDtls()
	{
		String function_nm="getLtcoraClosureDtls()";
		try
		{
			//Taking booked quantity 
			double edq=0;
			double sug=0;
			String queryString="SELECT A.EDQ_QTY,C.SUG,A.CLOSURE_REQUEST_FLAG,TO_CHAR(A.CLOSE_EFF_DT,'DD/MM/YYYY'),A.CLOSURE_ALLOC_QTY,A.CONT_REV "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST C "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? "
					+ "AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE = C.CONTRACT_TYPE  AND A.CONT_NO=C.CONT_NO ";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(1,comp_cd);
			stmt1.setString(2,counterparty_cd);
			stmt1.setString(3,agmt_no);
			stmt1.setString(4,agmt_rev_no);
			stmt1.setString(5,agmt_type);
			stmt1.setString(6,cont_no);
			stmt1.setString(7,contract_type);
			stmt1.setString(8,cargo_no);
			rset1 = stmt1.executeQuery();
			if(rset1.next())
			{
				edq = rset1.getDouble(1);
				sug = rset1.getDouble(2);
				clsr_flag = rset1.getString(3)==null?"":rset1.getString(3);
				clsr_eff_dt = rset1.getString(4)==null?"":rset1.getString(4);
				cont_rev_no = rset1.getString(6)==null?"":rset1.getString(6);
			}
			rset1.close();
			stmt1.close();
			
			//SUG if cargo sug is modified
			String queryString1="SELECT SUG FROM FMS_LTCORA_CONT_CARGO_MOD A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE = ? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND "
					+ "CARGO_NO=? AND  "
					+ "CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1,comp_cd);
			stmt1.setString(2,counterparty_cd);
			stmt1.setString(3,agmt_type);
			stmt1.setString(4,agmt_no);
			stmt1.setString(5,agmt_rev_no);
			stmt1.setString(6,contract_type);
			stmt1.setString(7,cont_no);
			//stmt1.setString(8,cont_rev_no);
			stmt1.setString(8,cargo_no);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				sug = rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
			
			int selCnt2=0;
			double adq=0;
			String queryString5 = "SELECT SUM(ADQ_QTY) "
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
			stmt5.setString(++selCnt2, comp_cd);
			stmt5.setString(++selCnt2, counterparty_cd);
			stmt5.setString(++selCnt2, "C");
			stmt5.setString(++selCnt2, agmt_type);
			stmt5.setString(++selCnt2, agmt_no);
			stmt5.setString(++selCnt2, contract_type);
			stmt5.setString(++selCnt2, cont_no);
			stmt5.setString(++selCnt2, cargo_no);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				adq=rset5.getDouble(1);
			}
			rset5.close();
			stmt5.close();
			
			double booked_qty = adq>0?(adq - (adq*sug/100)):(edq-(edq*sug/100));
			
			double supplied_qty = getLtcoraAllocationDtl(cont_no, agmt_no, counterparty_cd, contract_type, cargo_no);
			
			closure_qty =nf.format(booked_qty-supplied_qty);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public double getLtcoraAllocationDtl(String cont_no,String agmt_no,String counterparty_cd,String contract_type,String cargo_no)
	{
		String function_nm="getLtcoraAllocationDtl()";
		double supplied_qty=0;
		try
		{
			String queryString="SELECT SUM(QTY_MMBTU)  "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A  "
					+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B  "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO  "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD  "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ  "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE  "
					+ "AND B.GAS_DT=A.GAS_DT and A.CARGO_NO=B.CARGO_NO)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,cont_no);
			stmt.setString(2,agmt_no);
			stmt.setString(3,comp_cd);
			stmt.setString(4,counterparty_cd);
			stmt.setString(5,contract_type);
			stmt.setString(6,cargo_no);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				supplied_qty=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return supplied_qty;
	}
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
					+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) AND A.AGMT_NO=? "
					+ "AND A.CONTRACT_TYPE=? AND A.AGMT_TYPE=? AND A.BUY_SALE=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agreement_type);
			stmt.setString(7, buy_sale);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
					+ "FROM FMS_LTCORA_AGMT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND BUY_SALE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_PLANT B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, buy_sale);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
	
	private void getLtcoraVariableTeriffDetail() 
	{
		String function_nm="getLtcoraVariableTeriffDetail()";
		try
		{
			int cnt=0;
			String queryString="SELECT SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,TILL_END "
					+ "FROM FMS_LTCORA_CONT_STRG_CRG "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_REV=? AND CONT_REV=? "
					+ "ORDER BY FROM_DAYS";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, counterparty_cd);
			stmt.setString(++cnt, agmt_no);
			stmt.setString(++cnt, cont_no);
			stmt.setString(++cnt, contract_type);
			stmt.setString(++cnt, buy_sale);
			stmt.setString(++cnt, agreement_type);
			stmt.setString(++cnt, agmt_rev_no);
			stmt.setString(++cnt, cont_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VFROM_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VTO_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VSTORAGE_RATE.add(rset.getString(4)==null?"":rset.getString(4));
				VTILL_END.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
			
			if(VSEQ_NO.size()==0)
			{
				VSEQ_NO.add("1");
				VFROM_DT.add("");
				VTO_DT.add("");
				VSTORAGE_RATE.add("");
				VTILL_END.add("N");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getCargoReqModDtls() 
	{
		String function_nm="getCargoReqModDtls()";
		try
		{
			int count=0;
			String queryString = "SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,APPROVAL_FLAG "
					+ "FROM FMS_LTCORA_CONT_CARGO_MOD A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, counterparty_cd);
			stmt.setString(++count, buy_sale);
			stmt.setString(++count, agreement_type);
			stmt.setString(++count, agmt_no);
			stmt.setString(++count, contract_type);
			stmt.setString(++count, cont_no);
			stmt.setString(++count, cargo_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				
				ltcora_tariff = rset.getString(1)==null?"":rset.getString(1);
				ltcora_tariff_unit = rset.getString(2)==null?"":rset.getString(2);
				sug_per = rset.getDouble(3);
				approval_flag = rset.getString(4)==null?"":rset.getString(4);
				mod_count+=1;
				
				/*int count1=0;
				String queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++count1, comp_cd);
				stmt1.setString(++count1, counterparty_cd);
				stmt1.setString(++count1, buy_sale);
				stmt1.setString(++count1, agreement_type);
				stmt1.setString(++count1, agmt_no);
				stmt1.setString(++count1, contract_type);
				stmt1.setString(++count1, cont_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					ltcora_tariff_unit = rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();*/
			}
			else
			{
				int count1=0;
				String queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++count1, comp_cd);
				stmt1.setString(++count1, counterparty_cd);
				stmt1.setString(++count1, buy_sale);
				stmt1.setString(++count1, agreement_type);
				stmt1.setString(++count1, agmt_no);
				stmt1.setString(++count1, contract_type);
				stmt1.setString(++count1, cont_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					ltcora_tariff = rset1.getString(1)==null?"":rset1.getString(1);
					ltcora_tariff_unit = rset1.getString(2)==null?"":rset1.getString(2);
					sug_per = rset1.getDouble(3);
					
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

	public void getTraderCounterpartyList()
	{
		String function_nm="getTraderCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTraderCounterpartyList(clearance,comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,clearance,comp_cd,"T");
			//utilBean.getAllEntityCounterpartyList(conn,clearance,comp_cd,"T");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
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
			//utilBean.getEffectiveTraderCounterpartyList(clearance,comp_cd);
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
	
	public void getCounterpartywithAgmtList()
	{
		String function_nm="getCounterpartywithAgmtList()";
		try
		{
			String queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_LTCORA_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, buy_sale);
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
					+ "(SELECT TO_CHAR(MAX(END_DT),'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE) "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE) AND BUY_SALE=? "
					+ "ORDER BY END_DT DESC ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, agreement_type);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, agmt_no);
			stmt.setString(5, buy_sale);
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
	
	public void getCounterpartywithContList()
	{
		String function_nm="getCounterpartywithContList()";
		try
		{
			String queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_LTCORA_CONT_MST "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, buy_sale);
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
	
	public void getCounterpartyPlantList()
	{
		String function_nm="getCounterpartyPlantList()";
		try
		{
			if(callFlag.equalsIgnoreCase("LTCORA_CONTRACT_MST"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr="";
					if(buy_sale.equals("T"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					}
					else if(buy_sale.equals("C"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					}
					
					VPLANT_NM.add(plant_abbr);
					VPLANT_SEQ_NO.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				//utilBean.getEffectiveTraderPlantList(counterparty_cd, comp_cd);
				
				if(buy_sale.equals("T"))
				{
					utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "T", comp_cd);
					VPLANT_NM=utilBean.getPLANT_NM();
					VPLANT_ABBR=utilBean.getPLANT_ABBR();
					VPLANT_SEQ_NO=utilBean.getPLANT_SEQ_NO();
				}
				else if(buy_sale.equals("C"))
				{
					utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "C", comp_cd);
					VPLANT_NM=utilBean.getPLANT_NM();
					VPLANT_ABBR=utilBean.getPLANT_ABBR();
					VPLANT_SEQ_NO=utilBean.getPLANT_SEQ_NO();
				}
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
			if(callFlag.equalsIgnoreCase("LTCORA_CONTRACT_MST"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
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
	
	public void getTransporterPlantList()
	{
		String function_nm="getTransporterPlantList()";
		try
		{
			if(callFlag.equalsIgnoreCase("LTCORA_CONTRACT_MST"))
			{
				String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "A");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
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
						+ "FROM FMS_LTCORA_AGMT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=? ";
				stmt = conn.prepareStatement(queryString1);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "A");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String trans_cd=rset.getString(1)==null?"":rset.getString(1);
					String trans_abbr=utilBean.getCounterpartyABBR(conn,trans_cd);
					VTEMP_TRANS_CD.add(trans_cd);
					VTEMP_TRANS_ABBR.add(trans_abbr);
				}
				rset.close();
				stmt.close();
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
	
	public void getLTCORAAgreementDetail()
	{
		String function_nm="getLTCORAAgreementDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			if(!agmt_rev_no.equals("")) {
				String queryString="SELECT TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI') "
						+ "FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, buy_sale);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, "0");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String deal_ent_dt=rset.getString(1)==null?"":rset.getString(1);
					String split[] = deal_ent_dt.split(" ");
					ent_dt = split[0];
					ent_time = split[1];
				}
			}
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(REV_DT,'DD/MM/YYYY'),"
					+ "AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
					+ "DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
					+ "MDCQ,MDCQ_CLAUSE,MDCQ_PERCENTAGE,"
					+ "REMARK,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),AGMT_NAME,AGMT_REF_NO,BILLING_FLAG,"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,"
					+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
					+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
					+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
					+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE "
					+ "FROM FMS_LTCORA_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sale);
			stmt.setString(4, agreement_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				signing_dt=rset.getString(3)==null?"":rset.getString(3);
				start_dt=rset.getString(4)==null?"":rset.getString(4);
				end_dt=rset.getString(5)==null?"":rset.getString(5);
				rev_dt=rset.getString(6)==null?"":rset.getString(6);
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
				day_def_clause=rset.getString(21)==null?"":rset.getString(21);
				day_start_time=rset.getString(22)==null?"":rset.getString(22);
				day_end_time=rset.getString(23)==null?"":rset.getString(23);
				mdcq_flag=rset.getString(24)==null?"":rset.getString(24);
				mdcq_clause=rset.getString(25)==null?"":rset.getString(25);
				mdcq_percentage=rset.getString(26)==null?"":rset.getString(26);
				remark=rset.getString(27)==null?"":rset.getString(27);
				String deal_ent_dt=rset.getString(28)==null?"":rset.getString(28);
				//String split[] = deal_ent_dt.split(" ");
				//ent_dt = split[0];
				//ent_time = split[1];
				cont_name=rset.getString(29)==null?"":rset.getString(29);
				cont_ref_no=rset.getString(30)==null?"":rset.getString(30);
				bill_flag=rset.getString(31)==null?"":rset.getString(31);
				buy_fortnightly_nom=rset.getString(32)==null?"":rset.getString(32);
				sell_fortnightly_nom=rset.getString(33)==null?"":rset.getString(33);
				buy_nom_cutoff_time=rset.getString(34)==null?"":rset.getString(34);
				
				messurment_flag=rset.getString(35)==null?"":rset.getString(35);
				meas_clause=rset.getString(36)==null?"":rset.getString(36);
				meas_std=rset.getString(37)==null?"":rset.getString(37);
				meas_temp=rset.getString(38)==null?"":rset.getString(38);
				pressure_min_bar=rset.getString(39)==null?"":rset.getString(39);
				pressure_max_bar=rset.getString(40)==null?"":rset.getString(40);
				off_spec_gas_flag=rset.getString(41)==null?"":rset.getString(41);
				spec_clause=rset.getString(42)==null?"":rset.getString(42);
				spec_gas_eng_base=rset.getString(43)==null?"":rset.getString(43);
				spec_min_eng=rset.getString(44)==null?"":rset.getString(44);
				spec_max_eng=rset.getString(45)==null?"":rset.getString(45);
				liability_flag=rset.getString(46)==null?"":rset.getString(46);
				liability_clause=rset.getString(47)==null?"":rset.getString(47);
				billing_clause=rset.getString(48)==null?"":rset.getString(48);
				terminaton_flag=rset.getString(49)==null?"":rset.getString(49);
				termination_clause=rset.getString(50)==null?"":rset.getString(50);
				termination_planned=rset.getString(51)==null?"":rset.getString(51);
				termination_forced=rset.getString(52)==null?"":rset.getString(52);
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
	
	public void getSelectedTraderPlantList()
	{
		String function_nm="getSelectedTraderPlantList()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_AGMT_PLANT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sale);
			stmt.setString(4, agreement_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";
		try
		{
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_AGMT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sale);
			stmt.setString(4, agreement_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
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
	
	public void getSelectedTransporterPlantList()
	{
		String function_nm="getSelectedTransporterPlantList()";
		try
		{
			String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_AGMT_TRANSPTR "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, buy_sale);
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
	
	public void getCountAgreementBillingDetail()
	{
		String function_nm="getCountContractBillingDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_AGMT_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND BUY_SALE=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			stmt.setString(5, buy_sale);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_billing_dtl=""+rset.getInt(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLTCORAAgreementList()
	{
		String function_nm="getLTCORAAgreementList()";
		try
		{
			int stcount=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NAME,STATUS,AGMT_REF_NO,"
					+ "AGMT_BASE,AGMT_TYP "
					+ "FROM FMS_LTCORA_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? AND BUY_SALE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!contract_type.equals(""))
			{
				queryString += "AND AGMT_TYP=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NAME,STATUS,AGMT_REF_NO,"
						+ "AGMT_BASE,AGMT_TYP "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) "; 
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
				if(!contract_type.equals(""))
				{
					queryString += "AND AGMT_TYP=?";
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
			stmt.setString(++stcount, buy_sale);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++stcount, counterparty_cd);
			}
			if(!contract_type.equals(""))
			{
				stmt.setString(++stcount, contract_type);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++stcount, to_dt);
				stmt.setString(++stcount, from_dt);
				
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, agreement_type);
				stmt.setString(++stcount, buy_sale);
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++stcount, counterparty_cd);
				}
				if(!contract_type.equals(""))
				{
					stmt.setString(++stcount, contract_type);
				}
				stmt.setString(++stcount, to_dt);
				stmt.setString(++stcount, from_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt_no = rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev_no = rset.getString(4)==null?"0":rset.getString(4);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt_no);
				VAGMT_REV_NO.add(agmt_rev_no);
				VSTART_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VEND_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VAGMT_NAME.add(rset.getString(7)==null?"":rset.getString(7));
				String cont_status_flg=rset.getString(8)==null?"":rset.getString(8);
				VAGMT_STATUS_FLG.add(cont_status_flg);
				VAGMT_STATUS.add(""+AgmtStatusName(cont_status_flg));
				VAGMT_REF_NO.add(rset.getString(9)==null?"":rset.getString(9));
				
				String agmtBase =rset.getString(10)==null?"":rset.getString(10);
				String agmtType =rset.getString(11)==null?"":rset.getString(11);
				String disp_agmt_no = utilBean.NewAgmtMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, agreement_type);
				VDISP_AGMT_NO.add(disp_agmt_no);
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
				
				if(agmtType.equals("G"))
				{
					VAGMT_TYPE.add("CN");
				}
				else if(agmtType.equals("P"))
				{
					VAGMT_TYPE.add("Period");
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
	
	public void getLtcoraCnBillingDetail()
	{
		String function_nm="getLtcoraCnBillingDetail()";
		try
		{
			
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, cont_no);
			stmt3.setString(5, contract_type);
			stmt3.setString(6, buy_sale);
			stmt3.setString(7, agreement_type);
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
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, buy_sale);
					stmt.setString(7, agreement_type);
					stmt.setString(8, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset=stmt.executeQuery();
					if(rset.next())
					{
						billing_freq=rset.getString(1)==null?"":rset.getString(1);
						billing_flag=rset.getString(2)==null?"":rset.getString(2);
						due_date=rset.getString(3)==null?"2":rset.getString(3);
						sec_due_date=rset.getString(4)==null?"1":rset.getString(4);
						inv_currency=rset.getString(5)==null?"2":rset.getString(5);
						payment_currency=rset.getString(6)==null?"2":rset.getString(6);
						interest_rate_cd=rset.getString(7)==null?"":rset.getString(7);
						interest_cal_sign=rset.getString(8)==null?"":rset.getString(8);
						interest_cal_per=rset.getString(9)==null?"":rset.getString(9);
						exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
						exchng_cal=rset.getString(11)==null?"D":rset.getString(11);
						exchng_criteria=rset.getString(12)==null?"":rset.getString(12);
						exchng_note=rset.getString(13)==null?"":rset.getString(13);
						
						exchg_val=nf2.format(rset.getDouble(17));
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						
						billing_days=rset.getString(18)==null?"":rset.getString(18);
						
						sat_days=rset.getString(19)==null?"":rset.getString(19);
						plant_seq=rset.getString(20)==null?"":rset.getString(20);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, contract_type);
						stmt2.setString(6, buy_sale);
						stmt2.setString(7, agreement_type);
						stmt2.setString(8, plant_seq);
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
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, counterparty_cd);
								stmt4.setString(3, buy_sale);
								stmt4.setString(4, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
						
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, buy_sale);
						stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
				int count1=0;
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
						+ "AND AGMT_TYPE=? AND BUY_SALE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
						+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, agreement_type);
				stmt3.setString(5, buy_sale);
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
								+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
								+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_TYPE=? AND AGMT_NO=? AND BUY_SALE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agreement_type);
						stmt.setString(4, agmt_no);
						//stmt.setString(5, agmt_rev_no);
						stmt.setString(5, buy_sale);
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
							sat_days=rset.getString(19)==null?"N":rset.getString(19);
							plant_seq=rset.getString(20)==null?"":rset.getString(20);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
							
							exchg_val=nf2.format(rset.getDouble(17));
							billing_days = rset.getString(18)==null?"":rset.getString(18);
							
							String state_map="";
							String state_nm = "";
							String queryString2="SELECT HOLIDAY_STATE "
									+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
									+ "AND AGMT_TYPE=? AND BUY_SALE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
									+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_no);
							stmt2.setString(4, agreement_type);
							stmt2.setString(5, buy_sale);
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
											+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
											+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
											+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
											+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
											+ "AND C.SEQ_NO=B.SEQ_NO)";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, comp_cd);
									stmt4.setString(2, counterparty_cd);
									stmt4.setString(3, buy_sale);
									stmt4.setString(4, plant_seq);
									rset4=stmt4.executeQuery();
									if(rset4.next())
									{
										String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
										plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
										plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
						}
						else
						{
							String state_map="";
							String plant_abbr="";
							String state_nm="";
							String queryString4="SELECT A.TIN "
									+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
									+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
									+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
									+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
									+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
									+ "AND C.SEQ_NO=B.SEQ_NO)";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, buy_sale);
							stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
								plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
								
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, buy_sale);
						stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
	
	public void getLtcoraCnApplicableTaxes()
	{
		String function_nm="getLtcoraCnApplicableTaxes()";

		try
		{
			String sysdate = dateUtil.getSysdate();
			String periodStDt=utilBean.getFirstDtOfBillingCycle(billing_freq, "", sysdate);			
			if(dateUtil.getDays(periodStDt, cont_start_dt)<1)
			{
				periodStDt=cont_start_dt;
			}
			
			//UPDATED BY PRATHAM BHATT TO REFLECT TAX DETAIL			
			String queryString2="SELECT A.PLANT_SEQ_NO, B.PLANT_SEQ_NO  "
					+ "FROM FMS_LTCORA_CONT_BU  A, FMS_LTCORA_CONT_PLANT B "
					+ "WHERE A.COMPANY_CD=? AND B.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND B.COUNTERPARTY_CD=?  "
					+ "AND A.CONT_NO=? AND B.CONT_NO=?  "
					+ "AND A.CONT_REV=? AND B.CONT_REV=? AND A.AGMT_NO=? AND B.AGMT_NO=?  "
					+ "AND A.AGMT_REV=? AND B.AGMT_REV=? "
					+ "AND A.CONTRACT_TYPE=? AND B.CONTRACT_TYPE=? "
					+ "AND A.BUY_SALE=? AND B.BUY_SALE=?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, counterparty_cd);
			stmt2.setString(4, counterparty_cd);
			stmt2.setString(5, cont_no);
			stmt2.setString(6, cont_no);
			stmt2.setString(7, cont_rev_no);
			stmt2.setString(8, cont_rev_no);
			stmt2.setString(9, agmt_no);
			stmt2.setString(10, agmt_no);
			stmt2.setString(11, agmt_rev_no);
			stmt2.setString(12, agmt_rev_no);
			stmt2.setString(13, contract_type);
			stmt2.setString(14, contract_type);
			stmt2.setString(15, buy_sale);
			stmt2.setString(16, buy_sale);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);

				int selCnt=0;
				String queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL, B.SAP_TAX_CODE, A.INVOICE_TYPE "
						+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A, FMS_TAX_STRUCTURE B "
						+ "WHERE A.ENTITY=? AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.BU_UNIT=? AND A.INVOICE_TYPE IN ('SI', 'UG', 'ST') "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
						+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND A.INVOICE_TYPE=B.INVOICE_TYPE "
						+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.TAX_STRUCT_CD=B.TAX_STR_CD "; 
				stmt1=conn.prepareStatement(queryString1);
				if(buy_sale.equals("T"))
				{
					stmt1.setString(++selCnt, "T");
				}
				else if(buy_sale.equals("C"))
				{
					stmt1.setString(++selCnt, "C");
				}
				stmt1.setString(++selCnt, comp_cd);
				stmt1.setString(++selCnt, counterparty_cd);
				stmt1.setString(++selCnt, plant_seq);
				stmt1.setString(++selCnt, bu_plant_seq);
				//stmt1.setString(6, cont_start_dt);	
				stmt1.setString(++selCnt, periodStDt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
					if(buy_sale.equals("T"))
					{
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
					}
					else if(buy_sale.equals("C"))
					{
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C"));
					}
					VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					String invoice_cd = rset1.getString(4)==null?"":rset1.getString(4);
					
					if(buy_sale.equals("T"))
					{
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("T","S",invoice_cd));
					}
					else if(buy_sale.equals("C"))
					{
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("C","S",invoice_cd));
					}
					
					VINVOICE_CATEGORY.add("Service Invoice");
				}
				rset1.close();
				stmt1.close();
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLTCORAAgreementBillingDetail()
	{
		String function_nm="getLTCORAAgreementBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND BUY_SALE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, agreement_type);
			stmt3.setString(5, buy_sale);
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
							+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND BUY_SALE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
							+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agreement_type);
					stmt.setString(4, agmt_no);
					//stmt.setString(5, agmt_rev_no);
					stmt.setString(5, buy_sale);
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
						sat_days=rset.getString(19)==null?"N":rset.getString(19);
						plant_seq=rset.getString(20)==null?"":rset.getString(20);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
						
						exchg_val=nf2.format(rset.getDouble(17));
						billing_days = rset.getString(18)==null?"":rset.getString(18);
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_LTCORA_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND BUY_SALE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_LTCORA_AGMT_BILLING_DTL B WHERE "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.BUY_SALE=B.BUY_SALE)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agreement_type);
						stmt2.setString(5, buy_sale);
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
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
										+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
										+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
										+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
										+ "AND C.SEQ_NO=B.SEQ_NO)";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, comp_cd);
								stmt4.setString(2, counterparty_cd);
								stmt4.setString(3, buy_sale);
								stmt4.setString(4, plant_seq);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
					}
					else
					{
						String state_map="";
						String plant_abbr="";
						String state_nm="";
						String queryString4="SELECT A.TIN "
								+ "FROM FMS_STATE_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
								+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
								+ "AND C.SEQ_NO=B.SEQ_NO)";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, counterparty_cd);
						stmt4.setString(3, buy_sale);
						stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
							plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY=? "
							+ "AND B.SEQ_NO=? AND A.STATE_NM=B.PLANT_STATE "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.ENTITY=B.ENTITY "
							+ "AND C.SEQ_NO=B.SEQ_NO)";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterparty_cd);
					stmt4.setString(3, buy_sale);
					stmt4.setString(4, ""+VSELECTED_PLANT_SEQ.elementAt(k));
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String state_cd=rset4.getString(1)==null?"":rset4.getString(1);
						plant_seq=""+VSELECTED_PLANT_SEQ.elementAt(k);
						
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, buy_sale);
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
	
	public void getLTCORAAgreementApplicableTaxes()
	{
		String function_nm="getLTCORAAgreementApplicableTaxes()";
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
			
			String queryString="SELECT B.PLANT_SEQ_NO, P.PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_AGMT_BU B, FMS_LTCORA_AGMT_PLANT P "
					+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? "
					+ "AND B.AGMT_TYPE=? AND B.AGMT_NO=? AND B.AGMT_REV=? AND B.BUY_SALE=? "
					+ "AND P.COMPANY_CD=? AND P.COUNTERPARTY_CD=? "
					+ "AND P.AGMT_TYPE=? AND P.AGMT_NO=? AND P.AGMT_REV=? AND P.BUY_SALE=? "
					+ "AND B.COMPANY_CD=P.COMPANY_CD AND B.COUNTERPARTY_CD=P.COUNTERPARTY_CD "
					+ "AND B.AGMT_TYPE=P.AGMT_TYPE AND B.AGMT_NO=P.AGMT_NO AND B.AGMT_REV=P.AGMT_REV "
					+ "AND B.BUY_SALE=P.BUY_SALE";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, buy_sale);
			stmt.setString(7, comp_cd);
			stmt.setString(8, counterparty_cd);
			stmt.setString(9, agreement_type);
			stmt.setString(10, agmt_no);
			stmt.setString(11, agmt_rev_no);
			stmt.setString(12, buy_sale);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String bu_plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_seq = rset.getString(2)==null?"":rset.getString(2);
				
				int selCnt=0;
				
				String queryString1="SELECT TAX_STRUCT_CD,TAX_STRUCT_DTL,INVOICE_TYPE  " //Pratham Bhatt 20240821: added INVOICE_TYPE 
						+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? AND INVOICE_TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_SERVICE_TAX_DTL B "
						+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.INVOICE_TYPE=A.INVOICE_TYPE AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt1 = conn.prepareStatement(queryString1);
				if(buy_sale.equals("T"))
				{
					stmt1.setString(++selCnt, "T");
				}
				else if(buy_sale.equals("C"))
				{
					stmt1.setString(++selCnt, "C");
				}
				stmt1.setString(++selCnt, comp_cd);
				stmt1.setString(++selCnt, counterparty_cd);
				stmt1.setString(++selCnt, plant_seq);
				stmt1.setString(++selCnt, bu_plant_seq);
				stmt1.setString(++selCnt, "SI");
				stmt1.setString(++selCnt, periodStDt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
					
					if(buy_sale.equals("T"))
					{
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
					}
					else if(buy_sale.equals("C"))
					{
						VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C"));
					}
					
					VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					String invoice_cd = rset1.getString(3)==null?"":rset1.getString(3);		//Pratham Bhatt 20240821: for invoice_type
					if(buy_sale.equals("T"))
					{
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("T","S",invoice_cd));			//Pratham Bhatt 20240821: for invoice_type 
					}
					else if(buy_sale.equals("C"))
					{
						VINVOICE_TYPE.add(""+utilBean.getInvoiceNameByType("C","S",invoice_cd));			//Pratham Bhatt 20240821: for invoice_type 
					}
					VINVOICE_CATEGORY.add("Service Invoice");
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

	public void getLTCORAContractDetail()
	{
		String function_nm="getLTCORAContractDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			if(opration.equalsIgnoreCase("INSERT"))
			{
				String queryString="SELECT AGMT_BASE,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
						+ "SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
						+ "DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "MDCQ,MDCQ_CLAUSE,MDCQ_PERCENTAGE,BILLING_FLAG,"
						+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,"
						+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
						+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
						+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, buy_sale);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					agmt_base=rset.getString(1)==null?"":rset.getString(1);
					buy_nom_clause=rset.getString(2)==null?"":rset.getString(2);
					buy_nom_flag=rset.getString(3)==null?"":rset.getString(3);
					buy_month_nom=rset.getString(4)==null?"":rset.getString(4);
					buy_week_nom=rset.getString(5)==null?"":rset.getString(5);
					buy_daily_nom=rset.getString(6)==null?"":rset.getString(6);
					sell_nom_clause=rset.getString(7)==null?"":rset.getString(7);
					sell_nom_flag=rset.getString(8)==null?"":rset.getString(8);
					sell_month_nom=rset.getString(9)==null?"":rset.getString(9);
					sell_week_nom=rset.getString(10)==null?"":rset.getString(10);
					sell_daily_nom=rset.getString(11)==null?"":rset.getString(11);
					day_def_flag=rset.getString(12)==null?"":rset.getString(12);
					day_def_clause=rset.getString(13)==null?"":rset.getString(13);
					day_start_time=rset.getString(14)==null?"":rset.getString(14);
					day_end_time=rset.getString(15)==null?"":rset.getString(15);
					mdcq_flag=rset.getString(16)==null?"":rset.getString(16);
					mdcq_clause=rset.getString(17)==null?"":rset.getString(17);
					mdcq_percentage=rset.getString(18)==null?"":rset.getString(18);
					bill_flag=rset.getString(19)==null?"":rset.getString(19);
					buy_fortnightly_nom=rset.getString(20)==null?"":rset.getString(20);
					sell_fortnightly_nom=rset.getString(21)==null?"":rset.getString(21);
					buy_nom_cutoff_time=rset.getString(22)==null?"":rset.getString(22);
					messurment_flag=rset.getString(23)==null?"":rset.getString(23);
					meas_clause=rset.getString(24)==null?"":rset.getString(24);
					meas_std=rset.getString(25)==null?"":rset.getString(25);
					meas_temp=rset.getString(26)==null?"":rset.getString(26);
					pressure_min_bar=rset.getString(27)==null?"":rset.getString(27);
					pressure_max_bar=rset.getString(28)==null?"":rset.getString(28);
					off_spec_gas_flag=rset.getString(29)==null?"":rset.getString(23);
					spec_clause=rset.getString(30)==null?"":rset.getString(30);
					spec_gas_eng_base=rset.getString(31)==null?"":rset.getString(31);
					spec_min_eng=rset.getString(32)==null?"":rset.getString(32);
					spec_max_eng=rset.getString(33)==null?"":rset.getString(33);
					liability_flag=rset.getString(34)==null?"":rset.getString(34);
					liability_clause=rset.getString(35)==null?"":rset.getString(35);
					billing_clause=rset.getString(36)==null?"":rset.getString(36);
					terminaton_flag=rset.getString(37)==null?"":rset.getString(37);
					termination_clause=rset.getString(38)==null?"":rset.getString(38);
					termination_planned=rset.getString(39)==null?"":rset.getString(39);
					termination_forced=rset.getString(40)==null?"":rset.getString(40);
					agmt_signing_dt = rset.getString(41)==null?"":rset.getString(41);
					agmt_start_dt =rset.getString(42)==null?"":rset.getString(42);
					agmt_end_dt =rset.getString(43)==null?"":rset.getString(43);
					//fcc_flg=rset.getString(41)==null?"":rset.getString(41);
					//cont_status_flg=rset.getString(42)==null?"":rset.getString(42);
					
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT COMPANY_CD,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(REV_DT,'DD/MM/YYYY'),"
						+ "AGMT_BASE,'',CONT_STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM," /*need to add column*/
						+ "SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
						+ "DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "MDCQ,MDCQ_CLAUSE,MDCQ_PERCENTAGE,"
/*need to add column*/						+ "'',TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,CONT_REF_NO,BILLING_FLAG,"
						+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,"
						+ "MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
						+ "OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIAB_CLAUSE,BILLING_CLAUSE,"
						+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
						+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,VOL_DISCOUNT_UNIT,"
						+ "EXTEND_STORAGE,DISCOUNT_DAYS,"
						+ "STORAGE_TARIFF_MODE,STORAGE_TARIFF_UNIT,STORAGE_TARIFF,FCC_FLAG,CONT_STATUS, "
						+ "CLOSURE_REQUEST_FLAG,TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'),CLOSURE_REMARK, "
						+ "CASE WHEN END_DT<SYSDATE THEN 'Y' ELSE 'N' END,CONT_REV,ADV_ADJUST,TLU_FLAG "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? "
						//+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, buy_sale);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, cont_no);
				//stmt.setString(8, cont_rev_no);
				stmt.setString(8, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					display_map_id = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
					signing_dt=rset.getString(2)==null?"":rset.getString(2);
					signing_time=rset.getString(3)==null?"":rset.getString(3);
					start_dt=rset.getString(4)==null?"":rset.getString(4);
					end_dt=rset.getString(5)==null?"":rset.getString(5);
					rev_dt=rset.getString(6)==null?"":rset.getString(6);
					agmt_base=rset.getString(7)==null?"":rset.getString(7);
					//agmt_type=rset.getString(8)==null?"":rset.getString(8);
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
					day_def_clause=rset.getString(21)==null?"":rset.getString(21);
					day_start_time=rset.getString(22)==null?"":rset.getString(22);
					day_end_time=rset.getString(23)==null?"":rset.getString(23);
					mdcq_flag=rset.getString(24)==null?"":rset.getString(24);
					mdcq_clause=rset.getString(25)==null?"":rset.getString(25);
					mdcq_percentage=rset.getString(26)==null?"":rset.getString(26);
					//remark=rset.getString(27)==null?"":rset.getString(27);
					String deal_ent_dt=rset.getString(28)==null?"":rset.getString(28);
					String split[] = deal_ent_dt.split(" ");
					ent_dt = split[0];
					ent_time = split[1];
					cont_name=rset.getString(29)==null?"":rset.getString(29);
					cont_ref_no=rset.getString(30)==null?"":rset.getString(30);
					bill_flag=rset.getString(31)==null?"":rset.getString(31);
					buy_fortnightly_nom=rset.getString(32)==null?"":rset.getString(32);
					sell_fortnightly_nom=rset.getString(33)==null?"":rset.getString(33);
					buy_nom_cutoff_time=rset.getString(34)==null?"":rset.getString(34);
					
					messurment_flag=rset.getString(35)==null?"":rset.getString(35);
					meas_clause=rset.getString(36)==null?"":rset.getString(36);
					meas_std=rset.getString(37)==null?"":rset.getString(37);
					meas_temp=rset.getString(38)==null?"":rset.getString(38);
					pressure_min_bar=rset.getString(39)==null?"":rset.getString(39);
					pressure_max_bar=rset.getString(40)==null?"":rset.getString(40);
					off_spec_gas_flag=rset.getString(41)==null?"":rset.getString(41);
					spec_clause=rset.getString(42)==null?"":rset.getString(42);
					spec_gas_eng_base=rset.getString(43)==null?"":rset.getString(43);
					spec_min_eng=rset.getString(44)==null?"":rset.getString(44);
					spec_max_eng=rset.getString(45)==null?"":rset.getString(45);
					liability_flag=rset.getString(46)==null?"":rset.getString(46);
					liability_clause=rset.getString(47)==null?"":rset.getString(47);
					billing_clause=rset.getString(48)==null?"":rset.getString(48);
					terminaton_flag=rset.getString(49)==null?"":rset.getString(49);
					termination_clause=rset.getString(50)==null?"":rset.getString(50);
					termination_planned=rset.getString(51)==null?"":rset.getString(51);
					termination_forced=rset.getString(52)==null?"":rset.getString(52);
					
					dda_dt=rset.getString(53)==null?"":rset.getString(53);
					dda_time=rset.getString(54)==null?"":rset.getString(54);
					
					no_of_cargo=rset.getString(55)==null?"":rset.getString(55);
					ltcora_tariff=rset.getString(56)==null?"":rset.getString(56);
					ltcora_tariff_unit=rset.getString(57)==null?"":rset.getString(57);
					sug=rset.getString(58)==null?"":rset.getString(58);
					tariff_discount=rset.getString(59)==null?"":rset.getString(59);
					vol_discount=rset.getString(60)==null?"":rset.getString(60);
					vol_discount_unit=rset.getString(61)==null?"":rset.getString(61);
					extend_storage=rset.getString(62)==null?"":rset.getString(62);
					discount_days=rset.getString(63)==null?"":rset.getString(63);
					storage_tariff_mode=rset.getString(64)==null?"":rset.getString(64);
					storage_tariff_unit=rset.getString(65)==null?"":rset.getString(65);
					storage_tariff=rset.getString(66)==null?"":rset.getString(66);
					
					fcc_flg=rset.getString(67)==null?"":rset.getString(67);
					cont_status_flg=rset.getString(68)==null?"":rset.getString(68);
					clsr_flag=rset.getString(69)==null?"":rset.getString(69);
					clsr_eff_dt=rset.getString(70)==null?"":rset.getString(70);
					clsr_note=rset.getString(71)==null?"":rset.getString(71);
					is_expired=rset.getString(72)==null?"":rset.getString(72);
					cont_rev_no=rset.getString(73)==null?"":rset.getString(73);
					adv_adjust=rset.getString(74)==null?"":rset.getString(74);
					tlu_flag=rset.getString(75)==null?"N":rset.getString(75);
					
					cont_status = ""+utilBean.ContStatusName(cont_status_flg);
					
					String queryString1="SELECT TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
							+ "FROM FMS_LTCORA_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, buy_sale);
					stmt1.setString(4, agreement_type);
					stmt1.setString(5, agmt_no);
					stmt1.setString(6, agmt_rev_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmt_signing_dt = rset1.getString(1)==null?"":rset1.getString(1);
						agmt_start_dt =rset1.getString(2)==null?"":rset1.getString(2);
						agmt_end_dt =rset1.getString(3)==null?"":rset1.getString(3);
					}
					rset1.close();
					stmt1.close();
					
					queryString1 = "SELECT CASE WHEN COUNT(*)>0 THEN 'Y' ELSE 'N' END IS_CARGO_ALLOC FROM FMS_LTCORA_CONT_CARGO_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1,comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, agmt_rev_no);
					stmt1.setString(5, agreement_type);
					stmt1.setString(6, cont_no);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, buy_sale);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						is_cargo_alloc = rset1.getString(1)==null?"":rset1.getString(1);
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

	public void getContractSelectedCounterpartyPlantList()
	{
		String function_nm="getContractSelectedCounterpartyPlantList()";
		try
		{
			if(opration.equals("INSERT"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr="";
					if(buy_sale.equals("T"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					}
					else if(buy_sale.equals("C"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					}
					VSEL_TRAD_CD.add(counterparty_cd);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					VSEL_PLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, agreement_type);
				stmt.setString(9, buy_sale);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String plant_seq = rset.getString(1)==null?"":rset.getString(1);
					String plant_abbr="";
					if(buy_sale.equals("T"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
					}
					else if(buy_sale.equals("C"))
					{
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					}
					
					VSEL_TRAD_CD.add(counterparty_cd);
					VSEL_PLANT_SEQ_NO.add(plant_seq);
					VSEL_PLANT_ABBR.add(plant_abbr);
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
			if(opration.equals("INSERT"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, buy_sale);
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
						+ "FROM FMS_LTCORA_CONT_BU "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND BUY_SALE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, agreement_type);
				stmt.setString(9, buy_sale);
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
	
	public void getContractSelectedTransporterPlantList()
	{
		String function_nm="getContractSelectedTransporterPlantList()";
		try
		{
			if(opration.equals("INSERT"))
			{
				String queryString="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_AGMT_TRANSPTR A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "A");
				stmt.setString(4, agmt_no);
				stmt.setString(5, buy_sale);
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
						+ "FROM FMS_LTCORA_CONT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				stmt.setString(8, buy_sale);
				stmt.setString(9, "A");
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
	
	public void getLtcoraCnCountBillingDetail()
	{
		String function_nm="getLtcoraCnCountBillingDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_LTCORA_CONT_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, buy_sale);
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
	
	public void getLtcoraCnCountSecurityDetail()
	{
		String function_nm="getLtcoraCnCountSecurityDetail()";

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
	
	public void getLtcoraContractList()
	{
		String function_nm="getLtcoraContractList()";
		try
		{
			int selCount=0;
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,"
					+ "NO_OF_CARGO,CONTRACT_TYPE,AGMT_TYPE,BUY_SALE,SUG "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
			if(!contract_type.equals(""))
			{
				queryString += "AND CONTRACT_TYPE=?";
			}
			if(!buy_sale.equals(""))
			{
				queryString += "AND BUY_SALE=?";
			}
			if(!agreement_type.equals(""))
			{
				queryString += "AND AGMT_TYPE=?";
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,"
						+ "NO_OF_CARGO,CONTRACT_TYPE,AGMT_TYPE,BUY_SALE,SUG "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
				if(!contract_type.equals(""))
				{
					queryString += "AND CONTRACT_TYPE=?";
				}
				if(!buy_sale.equals(""))
				{
					queryString += "AND BUY_SALE=?";
				}
				if(!agreement_type.equals(""))
				{
					queryString += "AND AGMT_TYPE=?";
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
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCount, comp_cd);
			if(!contract_type.equals(""))
			{
				stmt.setString(++selCount, contract_type);
			}
			if(!buy_sale.equals(""))
			{
				stmt.setString(++selCount, buy_sale);
			}
			if(!agreement_type.equals(""))
			{
				stmt.setString(++selCount, agreement_type);
			}
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++selCount, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++selCount, to_dt);
				stmt.setString(++selCount, from_dt);
				
				stmt.setString(++selCount, comp_cd);
				if(!contract_type.equals(""))
				{
					stmt.setString(++selCount, contract_type);
				}
				if(!buy_sale.equals(""))
				{
					stmt.setString(++selCount, buy_sale);
				}
				if(!agreement_type.equals(""))
				{
					stmt.setString(++selCount, agreement_type);
				}
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					stmt.setString(++selCount, counterparty_cd);
				}
				stmt.setString(++selCount, to_dt);
				stmt.setString(++selCount, from_dt);
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
				String cont_type=rset.getString(13)==null?"":rset.getString(13);
				String agmt_type=rset.getString(14)==null?"":rset.getString(14);
				String buy_sell=rset.getString(15)==null?"":rset.getString(15);
				String sug_per=rset.getString(16)==null?"0.00":rset.getString(16);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONT_TYPE.add(cont_type);
				VAGMT_TYPE.add(agmt_type);
				VBUY_SELL.add(buy_sell);
				VSUG_PER.add(sug_per);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				String cont_status_flg=rset.getString(10)==null?"":rset.getString(10);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+utilBean.ContStatusName(cont_status_flg));
				
				String cont_ref=rset.getString(11)==null?"":rset.getString(11);
				
				VCONT_REF_NO.add(cont_ref);
				VCONT_CARGO_NO.add(rset.getString(12)==null?"":rset.getString(12));
				
				String dealMapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				VDEAL_MAPPING.add(dealMapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraContTraderCounterpartyList()
	{
		String function_nm="getLtcoraContTraderCounterpartyList()";
		try
		{
			int selCount=0;
			
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND BUY_SALE=? "; 
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCount, comp_cd);
			stmt.setString(++selCount, buy_sale);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraContractCargoDtl()
	{
		String function_nm="getLtcoraContractCargoDtl()";
		try
		{
			int no_of_cargos = Integer.parseInt(no_cargo);
			
			for(int i=0;i<no_of_cargos;i++) 
			{
				int selCnt=0;
				String cargo_no=""+(i+1);

				String cargo_disp_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
				
				VCARGO_NO.add(cargo_disp_no);
				//VCARGO_REF_NO.add(cargo_ref_no);
				
				String queryString="SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ " AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
				stmt1 = conn.prepareStatement(queryString);
				stmt1.setString(++selCnt, comp_cd);
				stmt1.setString(++selCnt, counterparty_cd);
				stmt1.setString(++selCnt, buy_sale);
				stmt1.setString(++selCnt, agmt_type);
				stmt1.setString(++selCnt, agmt_no);
				//stmt1.setString(++selCnt, agmt_rev_no); IGNORING AGMT_REV_NO
				stmt1.setString(++selCnt, contract_type);
				stmt1.setString(++selCnt, cont_no);
				//stmt1.setString(++selCnt, cont_rev_no);
				stmt1.setString(++selCnt, cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					int count=rset1.getInt(1);
					
					if(count>0)
					{
						int cnt=0;
						
						String queryString2="SELECT SHIP_CD,SUPP_CD,TO_CHAR(EDQ_FROM_DT,'DD/MM/YYYY'),TO_CHAR(EDQ_TO_DT,'DD/MM/YYYY'),TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'), "
								+ "EDQ_QTY,CSOC_QTY,BOE_QTY,BOE_NO,TO_CHAR(BOE_DT,'DD/MM/YYYY'),QQ_NO,TO_CHAR(QQ_DT,'DD/MM/YYYY'),"
								+ "STORAGE_DAYS,STORAGE_EXT_DAYS,CARGO_REF,ATTACH_LNG_CARGO,CLOSURE_REQUEST_FLAG,TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY'),CLOSURE_ALLOC_QTY "
								+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND BUY_SALE=? AND AGMT_TYPE=?"
								+ "AND AGMT_NO=? "
								+ "AND CONTRACT_TYPE=? AND CONT_NO=?"
								+ " AND CARGO_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(++cnt, comp_cd);
						stmt2.setString(++cnt, counterparty_cd);
						stmt2.setString(++cnt, buy_sale);
						stmt2.setString(++cnt, agmt_type);
						stmt2.setString(++cnt, agmt_no);
						//stmt2.setString(++cnt, agmt_rev_no);
						stmt2.setString(++cnt, contract_type);
						stmt2.setString(++cnt, cont_no);
						//stmt2.setString(++cnt, cont_rev_no);
						stmt2.setString(++cnt, cargo_no);
						rset2=stmt2.executeQuery();
						if(rset2.next()) 
						{
							String supp_cd=rset2.getString(2)==null?"":rset2.getString(2);
							String ship_cd=rset2.getString(1)==null?"":rset2.getString(1);
							String cargo_ref=rset2.getString(15)==null?"":rset2.getString(15);
							String attach_lng_cargo=rset2.getString(16)==null?"":rset2.getString(16);
							String clsr_stat=rset2.getString(17)==null?"":rset2.getString(17);
							String clsr_eff_dt=rset2.getString(18)==null?"":rset2.getString(18);
							String clsr_qty=rset2.getString(19)==null?"":rset2.getString(19);
							
							String disp_attach_lng_cargo ="";
							
							VSHIP_CD.add(ship_cd);
							VSUPP_CD.add(supp_cd);
							VCARGO_REF_NO.add(cargo_ref);
							VATTACH_LNG_CARGO.add(attach_lng_cargo);
							
							if(!attach_lng_cargo.equals("")) 
							{
								String spit_attach_lng_cargo[] =attach_lng_cargo.split("-");
								
								String split_comp_cd=spit_attach_lng_cargo[0];
								String split_cp=spit_attach_lng_cargo[1];
								String split_agmt=spit_attach_lng_cargo[3];
								String split_agmt_rev=spit_attach_lng_cargo[4];
								String split_cont=spit_attach_lng_cargo[6];
								String split_cont_rev=spit_attach_lng_cargo[7];
								String split_cont_type=spit_attach_lng_cargo[5];
								String split_cargo_no=spit_attach_lng_cargo[8];
								
								disp_attach_lng_cargo = utilBean.NewDealMappingId(split_comp_cd, split_cp, split_agmt, split_agmt_rev, split_cont, split_cont_rev, split_cont_type, split_cargo_no);
								VDISP_ATTACH_LNG_CARGO.add(disp_attach_lng_cargo);
							}
							else
							{
								VDISP_ATTACH_LNG_CARGO.add("");
							}
							
							VSHIP_NM.add(getShipName(ship_cd));
							VSUPP_NM.add(utilBean.getCounterpartyName(conn,supp_cd));
							VEDQ_FROM_DT.add(rset2.getString(3)==null?"":rset2.getString(3));
							VEDQ_TO_DT.add(rset2.getString(4)==null?"":rset2.getString(4));
							VACTUAL_RECPT_DT.add(rset2.getString(5)==null?"":rset2.getString(5));
							VEDQ_QTY.add(rset2.getString(6)==null?"0.00":rset2.getString(6));
							VCSOC_QTY.add(rset2.getString(7)==null?"0.00":rset2.getString(7));
							VBOE_QTY.add(rset2.getString(8)==null?"0.00":rset2.getString(8));
							VBOE_NO.add(rset2.getString(9)==null?"":rset2.getString(9));
							VBOE_DT.add(rset2.getString(10)==null?"":rset2.getString(10));
							VQQ_NO.add(rset2.getString(11)==null?"":rset2.getString(11));
							VQQ_DT.add(rset2.getString(12)==null?"":rset2.getString(12));
							
							VCLOSURE_FLAG.add(clsr_stat);
							VCLOSURE_DT.add(clsr_eff_dt);
							VCLOSURE_QTY.add(clsr_qty);
							
							VSTORAGE_DAYS.add(rset2.getString(13)==null?"":rset2.getString(13));
							VSTORAGE_EXT_DAYS.add(rset2.getString(14)==null?"":rset2.getString(14));

							double total_adq = getLtcoraTotalUnloadedQuantDtl(cargo_no);
							
							if(Double.doubleToRawLongBits(total_adq) == Double.doubleToRawLongBits(-1))
							{
								VTOTAL_ADQ_QTY.add("");
							}
							else 
							{
								VTOTAL_ADQ_QTY.add(total_adq);
							}
							
						}
						rset2.close();
						stmt2.close();
						
						VISCARGOADDED.add("Y");
					}
					else 
					{
						VSHIP_CD.add("");
						VSUPP_CD.add("");
						VSHIP_NM.add("");
						VSUPP_NM.add("");
						VEDQ_FROM_DT.add("");
						VEDQ_TO_DT.add("");
						VACTUAL_RECPT_DT.add("");
						VEDQ_QTY.add("0.00");
						VCSOC_QTY.add("0.00");
						VBOE_QTY.add("0.00");
						VBOE_NO.add("");
						VBOE_DT.add("");
						VQQ_NO.add("");
						VQQ_DT.add("");
						VTOTAL_ADQ_QTY.add("");
						VSTORAGE_DAYS.add("");
						VSTORAGE_EXT_DAYS.add("");
						VCARGO_REF_NO.add(cargo_disp_no);
						VATTACH_LNG_CARGO.add("");
						VDISP_ATTACH_LNG_CARGO.add("");
						VISCARGOADDED.add("N");
						VCLOSURE_QTY.add("");
						VCLOSURE_DT.add("");
						VCLOSURE_FLAG.add("");
					}
				}
				rset1.close();
				stmt1.close();
				
				int count=0;
				String queryString2="SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_CARGO_MOD A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ " AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)"
						+ "AND APPROVAL_FLAG=?";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++count, comp_cd);
				stmt2.setString(++count, counterparty_cd);
				stmt2.setString(++count, buy_sale);
				stmt2.setString(++count, agmt_type);
				stmt2.setString(++count, agmt_no);
				//stmt2.setString(++count, agmt_rev_no);
				stmt2.setString(++count, contract_type);
				stmt2.setString(++count, cont_no);
				//stmt2.setString(++count, cont_rev_no);
				stmt2.setString(++count, cargo_no);
				stmt2.setString(++count, "Y");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count1=rset2.getInt(1);
					
					if(count1>0)
					{
						int cnt=0;
						
						String queryString3 = "SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,APPROVAL_FLAG "
								+ "FROM FMS_LTCORA_CONT_CARGO_MOD A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
								+ "AND APPROVAL_FLAG=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(++cnt, comp_cd);
						stmt3.setString(++cnt, counterparty_cd);
						stmt3.setString(++cnt, buy_sale);
						stmt3.setString(++cnt, agmt_type);
						stmt3.setString(++cnt, agmt_no);
						stmt3.setString(++cnt, contract_type);
						stmt3.setString(++cnt, cont_no);
						stmt3.setString(++cnt, cargo_no);
						//stmt3.setString(++cnt, cont_rev_no);
						stmt3.setString(++cnt, "Y");
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							
							VSUG_PER.add(nf.format(Double.parseDouble(rset3.getString(3)==null?"":rset3.getString(3))));
						}
						
						rset3.close();
						stmt3.close();
						
					}
					else
					{
						VSUG_PER.add("");
					}
				}
				rset2.close();
				stmt2.close();
				
				count=0;
				String queryString4="SELECT APPROVAL_FLAG "
						+ "FROM FMS_LTCORA_CONT_CARGO_MOD A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ " AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)"
						+ " AND (APPROVAL_FLAG IS NULL OR APPROVAL_FLAG=?) ";
				stmt2 = conn.prepareStatement(queryString4);
				stmt2.setString(++count, comp_cd);
				stmt2.setString(++count, counterparty_cd);
				stmt2.setString(++count, buy_sale);
				stmt2.setString(++count, agmt_type);
				stmt2.setString(++count, agmt_no);
				//stmt2.setString(++count, agmt_rev_no);
				stmt2.setString(++count, contract_type);
				stmt2.setString(++count, cont_no);
				//stmt2.setString(++count, cont_rev_no);
				stmt2.setString(++count, cargo_no);
				stmt2.setString(++count, "N");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					String approve = rset2.getString(1)==null?"N":rset2.getString(1);
					VMOD_REQUEST_FLAG.add(approve);
				}
				else
				{
					VMOD_REQUEST_FLAG.add("");
				}
				rset2.close();
				stmt2.close();
				
				if("T".equals(buy_sale))
				{
					//GET BUYER NOMINATION DATA FOR SELECTED CONTRACT
					String queryString3="SELECT COUNT(*),"
							+ " MAX(TO_CHAR(GAS_DT,'DD/MM/YYYY'))  "
			  				+ "FROM FMS_BUY_DAILY_BUYER_NOM A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_BUYER_NOM B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
							+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, cont_no);
					stmt3.setString(2, agmt_no);
					stmt3.setString(3, comp_cd);
					stmt3.setString(4, counterparty_cd);
					stmt3.setString(5, contract_type);
					stmt3.setString(6, cargo_no);
					rset3=stmt3.executeQuery();
			  		if(rset3.next())
			  		{
			  			int count1 = rset3.getInt(1);
			  			
			  			String max_nominated_dt = rset3.getString(2)==null?"":rset3.getString(2);
			  			
			  			VCARGO_MAX_NOM_DT.add(max_nominated_dt);
			  			
			  			if(count1>0) 
			  			{
			  				VCARGO_IS_NOMINATED.add("Y");
			  			}
			  			else
				  		{
				  			VCARGO_IS_NOMINATED.add("N");
				  		}
			  		}
			  		else
			  		{
			  			VCARGO_IS_NOMINATED.add("N");
			  		}
			  		rset3.close();
			  		stmt3.close();
				}
				else if("C".equals(buy_sale))
				{
					String queryString3="SELECT COUNT(*),"
							+ " MAX(TO_CHAR(GAS_DT,'DD/MM/YYYY'))  "
			  				+ "FROM FMS_DAILY_BUYER_NOM A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, cont_no);
					stmt3.setString(2, agmt_no);
					stmt3.setString(3, comp_cd);
					stmt3.setString(4, counterparty_cd);
					stmt3.setString(5, contract_type);
					stmt3.setString(6, cargo_no);
					rset3=stmt3.executeQuery();
					if(rset3.next())
			  		{
			  			int count1 = rset3.getInt(1);
			  			
			  			String max_nominated_dt = rset3.getString(2)==null?"":rset3.getString(2);
			  			
			  			VCARGO_MAX_NOM_DT.add(max_nominated_dt);
			  			
			  			if(count1>0) 
			  			{
			  				VCARGO_IS_NOMINATED.add("Y");
			  			}
			  			else
				  		{
				  			VCARGO_IS_NOMINATED.add("N");
				  		}
			  		}
			  		else
			  		{
			  			VCARGO_IS_NOMINATED.add("N");
			  		}
					rset3.close();
					stmt3.close();
				}
				/*else
				{
					VCARGO_IS_NOMINATED.add("N");
				}*/
			}
			
			//for fetching the contract status
			String queryString4="SELECT CONT_STATUS FROM "
					+ "FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM "
					+ "FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString4);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sale);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, agmt_type);
			stmt.setString(7, cont_no);
			stmt.setString(8, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				cont_status_flg=rset.getString(1)==null?"":rset.getString(1);
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
				nm="Re-Opened";
			}
			else if(status_flg.equals("T"))
			{
				nm="Terminated";
			}
			else if(status_flg.equals(""))
			{
				nm="Pending";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public String getShipName(String ship_cd)
	{
		String function_nm="getShipName()";
		String ship_nm="";
		try
		{
			String queryString_temp="SELECT SHIP_NAME "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE SHIP_CD=? AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD)";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, ship_cd);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next()) 
			{
				ship_nm = rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return ship_nm;
	}
	
	public void getLtcoraUnloadedQuantDtl()
	{
		String function_nm="getLtcoraUnloadedQuantDtl()";
		try
		{
			int selCnt=0;
			String queryString = "SELECT ADQ_QTY,TO_CHAR(ADQ_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LTCORA_CONT_CARGO_ADQ A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_ADQ B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY ADQ_DT ASC";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(++selCnt, comp_cd);
			stmt1.setString(++selCnt, counterparty_cd);
			stmt1.setString(++selCnt, buy_sale);
			stmt1.setString(++selCnt, agmt_type);
			stmt1.setString(++selCnt, agmt_no);
			//stmt1.setString(++selCnt, agmt_rev_no);
			stmt1.setString(++selCnt, contract_type);
			stmt1.setString(++selCnt, cont_no);
			//stmt1.setString(++selCnt, cont_rev_no);
			stmt1.setString(++selCnt, cargo_no);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String adq_qty=rset1.getString(1)==null?"":rset1.getString(1);
				VADQ_QTY.add(adq_qty);
				VADQ_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
			}
			
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraVariableCsocDtl()
	{
		String function_nm="getLtcoraVariableCsocDtl()";
		try
		{
			int selCnt=0;
			
			String queryString = "SELECT CSOC_SEQ_NO,TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),CSOC,REMARK "
					+ "FROM FMS_LTCORA_CONT_CARGO_CSOC A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
					+ " AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_CSOC B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(++selCnt, comp_cd);
			stmt1.setString(++selCnt, counterparty_cd);
			stmt1.setString(++selCnt, buy_sale);
			stmt1.setString(++selCnt, agmt_type);
			stmt1.setString(++selCnt, agmt_no);
			//stmt1.setString(++selCnt, agmt_rev_no);
			stmt1.setString(++selCnt, contract_type);
			stmt1.setString(++selCnt, cont_no);
			//stmt1.setString(++selCnt, cont_rev_no);
			stmt1.setString(++selCnt, cargo_no);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String csoc_seq_no=rset1.getString(1)==null?"":rset1.getString(1);
				String csoc_from_dt=rset1.getString(2)==null?"":rset1.getString(2);
				String csoc_to_dt=rset1.getString(3)==null?"":rset1.getString(3);
				String var_csoc=rset1.getString(4)==null?"":rset1.getString(4);
				String csoc_remark=rset1.getString(5)==null?"":rset1.getString(5);
				
				VCSOC_SEQ_NO.add(csoc_seq_no);
				VCSOC_FROM_DT.add(csoc_from_dt);
				VCSOC_TO_DT.add(csoc_to_dt);
				VVAR_CSOC.add(var_csoc);
				VCSOC_REMARK.add(csoc_remark);
			}
			
			if(VCSOC_SEQ_NO.size()==0)
			{
				VCSOC_SEQ_NO.add("1");
				VCSOC_FROM_DT.add("");
				VCSOC_TO_DT.add("");
				VVAR_CSOC.add("");
				VCSOC_REMARK.add("");
			}
			
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraCnCargoStorageDtl()
	{
		String function_nm="getLtcoraCnCargoStorageDtl()";
		try
		{
			int selCnt=0;
			
			String act_rec_dt=""; //JD
			String queryString = "SELECT STORAGE_DAYS,STORAGE_EXT_DAYS,TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
					+ " AND CARGO_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(++selCnt, comp_cd);
			stmt1.setString(++selCnt, counterparty_cd);
			stmt1.setString(++selCnt, buy_sale);
			stmt1.setString(++selCnt, agmt_type);
			stmt1.setString(++selCnt, agmt_no);
			//stmt1.setString(++selCnt, agmt_rev_no);
			stmt1.setString(++selCnt, contract_type);
			stmt1.setString(++selCnt, cont_no);
			//stmt1.setString(++selCnt, cont_rev_no);
			stmt1.setString(++selCnt, cargo_no);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				storage_days=rset1.getString(1)==null?"0":rset1.getString(1);
				storage_ext_days=rset1.getString(2)==null?"0":rset1.getString(2);
				act_rec_dt=rset1.getString(3)==null?"":rset1.getString(3);
			}
						
			int cnt=0;
			String queryString1="SELECT EXTEND_STORAGE "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO)";
			stmt2 = conn.prepareStatement(queryString1);
			stmt2.setString(++cnt, comp_cd);
			stmt2.setString(++cnt, counterparty_cd);
			stmt2.setString(++cnt, buy_sale);
			stmt2.setString(++cnt, agmt_type);
			stmt2.setString(++cnt, agmt_no);
			stmt2.setString(++cnt, contract_type);
			stmt2.setString(++cnt, cont_no);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				extend_storage = rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			rset1.close();
			stmt1.close();
			
			if("T".equals(buy_sale))//Buy Side
			{
				int cont2=0;
				String queryString3="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
		  		stmt3=conn.prepareStatement(queryString3);
		  		stmt3.setString(++cont2, cont_no);
		  		stmt3.setString(++cont2, agmt_no);
		  		stmt3.setString(++cont2, comp_cd);
		  		stmt3.setString(++cont2, counterparty_cd);
		  		stmt3.setString(++cont2, contract_type);
		  		stmt3.setString(++cont2, cargo_no);
				rset3=stmt3.executeQuery();
		  		if(rset3.next())
		  		{
		  			if(rset3.getDouble(1) > 0)
		  			{
		  				allocated_qty_mmbtu=nf.format(rset3.getDouble(1));
		  			}
		  			else
		  			{
		  				allocated_qty_mmbtu="0.00";
		  			}
		  		}
		  		else
		  		{
		  			allocated_qty_mmbtu="0.00";
		  		}
		  		rset3.close();
		  		stmt3.close();
			}
			else if("C".equals(buy_sale))//Sell Side
			{
				int cont2=0;
				String queryString2="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt3 = conn.prepareStatement(queryString2);
				stmt3.setString(++cont2, cont_no);
				stmt3.setString(++cont2, agmt_no);
				stmt3.setString(++cont2, comp_cd);
				stmt3.setString(++cont2, counterparty_cd);
				stmt3.setString(++cont2, contract_type);
				stmt3.setString(++cont2, cargo_no);
				rset3=stmt3.executeQuery();
		  		if(rset3.next())
		  		{
		  			if(rset3.getDouble(1) > 0)
		  			{
		  				allocated_qty_mmbtu=nf.format(rset3.getDouble(1));
		  			}
		  			else
		  			{
		  				allocated_qty_mmbtu="0.00";
		  			}
		  		}
		  		else
		  		{
		  			allocated_qty_mmbtu="0.00";
		  		}
		  		rset3.close();
		  		stmt3.close();
		  		
		  		//JD-20250619 :: ON SPECIAL REQUEST
		  		if(!storage_ext_days.equals("") && !storage_days.equals(""))
		  		{
		  			if(Integer.parseInt(storage_ext_days)>0 && Integer.parseInt(storage_days)>0)
		  			{
		  				double tot_buyer_nom=0;
						double tot_seller_nom=0;
						double tot_alloc_qty=0;
						
		  				String init_dt = dateUtil.getDate(act_rec_dt, ""+(Integer.parseInt(storage_days)-1));
		  				int count = Integer.parseInt(storage_ext_days);
						for(int k=1; k<=count; k++)
						{
							String gas_dt=""+dateUtil.getDate(init_dt, ""+k);
							
							double buyer_nom=0;
							double seller_nom=0;
							
							queryString2="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
					  				+ "FROM FMS_DAILY_BUYER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cont_no);
							stmt2.setString(2, agmt_no);
							stmt2.setString(3, comp_cd);
							stmt2.setString(4, counterparty_cd);
							stmt2.setString(5, contract_type);
							stmt2.setString(6, gas_dt);
							stmt2.setString(7, cargo_no);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								buyer_nom=rset2.getDouble(1);
							}
							rset2.close();
							stmt2.close();
							
							queryString2="SELECT SUM(QTY_MMBTU),SUM(QTY_SCM) "
					  				+ "FROM FMS_DAILY_SELLER_NOM A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cont_no);
							stmt2.setString(2, agmt_no);
							stmt2.setString(3, comp_cd);
							stmt2.setString(4, counterparty_cd);
							stmt2.setString(5, contract_type);
							stmt2.setString(6, gas_dt);
							stmt2.setString(7, cargo_no);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								seller_nom=rset2.getDouble(1);
							}
							rset2.close();
							stmt2.close();
							
							String alloc=allocUtil.getSupplyAllocationQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt, gas_dt, cargo_no);
						
							VBUYER_NOM.add(nf.format(buyer_nom));
							VSELLER_NOM.add(nf.format(seller_nom));
							VALLOC_QTY.add(alloc);
							VGAS_DT.add(gas_dt);
							
							tot_buyer_nom+=buyer_nom;
							tot_seller_nom+=seller_nom;
							tot_alloc_qty+=Double.parseDouble(alloc);
						}
						
						total_buyer_nom=nf.format(tot_buyer_nom);
						total_seller_nom=nf.format(tot_seller_nom);
						total_alloc_qty=nf.format(tot_alloc_qty);
		  			}
		  		}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public double getLtcoraTotalUnloadedQuantDtl(String cargo_no)
	{
		String function_nm="getLtcoraTotalUnloadedQuantDtl()";
		
		double total_adq_qty=0.00;
		
		try
		{
			int selCnt2=0;
			
			VADQ_QTY.clear();
			String queryString = "SELECT ADQ_QTY,ADQ_DT "
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
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(++selCnt2, comp_cd);
			stmt1.setString(++selCnt2, counterparty_cd);
			stmt1.setString(++selCnt2, buy_sale);
			stmt1.setString(++selCnt2, agmt_type);
			stmt1.setString(++selCnt2, agmt_no);
			//stmt1.setString(++selCnt2, agmt_rev_no);
			stmt1.setString(++selCnt2, contract_type);
			stmt1.setString(++selCnt2, cont_no);
			//stmt1.setString(++selCnt2, cont_rev_no);
			stmt1.setString(++selCnt2, cargo_no);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String adq_qty=rset1.getString(1)==null?"0":rset1.getString(1);
				
				VADQ_QTY.add(adq_qty);
				VADQ_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
			}
			
			for (int i = 0; i < VADQ_QTY.size(); i++)
			{
	            total_adq_qty += Double.parseDouble(""+VADQ_QTY.get(i));
	        }
			
			if(VADQ_QTY.size()==0) 
			{
				total_adq_qty = -1; // This is used to bifurcate ADQ Data is submitted or not 
			}
			
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return total_adq_qty;
	}
	
	public void getLtcoraAgmtLiabilityDetails() 
	{
		String function_nm="getLtcoraAgmtLiabilityDetails()";
		try
		{
			String queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK "
					+ "FROM FMS_LTCORA_AGMT_LIABILITY "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND BUY_SALE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, buy_sale);
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
				makeup_liab_on = rset.getString(18)==null?"P":rset.getString(18);
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
	
	public void getSelectedLtcoraAgmtLiabilityDtl() 
	{
		String function_nm="getSelectedLtcoraAgmtLiabilityDtl()";
		try
		{
			int liability_count=0;
			
			String queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_LTCORA_AGMT_LIABILITY "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND BUY_SALE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			stmt.setString(5, buy_sale);
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
	
	public void getLtcoraCnLiabilityDetails() 
	{
		String function_nm="getLtcoraCnLiabilityDetails()";
		try
		{
			String queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK "
					+ "FROM FMS_LTCORA_CONT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, buy_sale);
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
				makeup_liab_on = rset.getString(18)==null?"P":rset.getString(18);
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
	
	public void getSelectedLtcoraCnLiabilityDtl() 
	{
		String function_nm="getSelectedLtcoraCnLiabilityDtl()";
		try
		{
			int liability_count=0;
			String queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_LTCORA_CONT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, buy_sale);
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
	
	public void getAllocationCargoList() 
	{
		String function_nm="getAllocationCargoList()";
		try
		{
			int count=0;
			String contno ="";
			String cont_type="";
			String alloc_rev = "";
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
					+ "CONTRACT_TYPE,CONT_NO,CARGO_NO,SHIP_CD,AGMT_REV,CONT_REV,ALLOC_REV,QQ_QTY_MMBTU,TO_CHAR(ACT_ARRV_DT,'DD/MM/YYYY'),QQ_QTY_MMBTU,QQ_NO,TO_CHAR(QQ_DT,'DD/MM/YYYY') "
					+ "FROM FMS_BUY_CARGO_ALLOC A "
					+ "WHERE COMPANY_CD=? ";
			queryString+="AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) "
					+ "AND QQ_QTY_MMBTU IS NOT NULL ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd =(rset.getString(2)==null?"":rset.getString(2));

				agmt_type=(rset.getString(3)==null?"":rset.getString(3));
				agmt_no=(rset.getString(4)==null?"":rset.getString(4));
				contract_type=(rset.getString(5)==null?"":rset.getString(5));
				cont_no=(rset.getString(6)==null?"":rset.getString(6));
				String cargo_no=(rset.getString(7)==null?"":rset.getString(7));
				String ship_cd =(rset.getString(8)==null?"":rset.getString(8));
				cont_type = rset.getString(5)==null?"":rset.getString(5);
				contno = rset.getString(6)==null?"":rset.getString(6);
				agmt_rev_no = rset.getString(9)==null?"":rset.getString(9);
				cont_rev_no = rset.getString(10)==null?"":rset.getString(10);
				alloc_rev=rset.getString(11)==null?"":rset.getString(11);
				String lab_qty = rset.getString(12)==null?"":rset.getString(12);
				String act_arrv_dt = rset.getString(13)==null?"":rset.getString(13);
				String qq_qty_mmbtu = rset.getString(14)==null?"":rset.getString(14);
				String qq_qty_cer_no = rset.getString(15)==null?"":rset.getString(15);
				String qq_qty_cer_dt = rset.getString(16)==null?"":rset.getString(16);
				
				String num_boe="";
				
				int num_boe_act_qty=0;
				String queryString5="SELECT COUNT(*),COUNT(ACT_BOE_QTY) "
						+ "FROM FMS_BUY_CARGO_ALLOC_BOE A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND ALLOC_REV=? AND ACT_BOE_QTY IS NOT NULL ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, cont_no);
				stmt5.setString(4, agmt_type);
				stmt5.setString(5, agmt_no);
				stmt5.setString(6, contract_type);
				stmt5.setString(7, cargo_no);
				stmt5.setString(8, alloc_rev);
				rset5=stmt5.executeQuery();
				while(rset5.next())
				{
					num_boe=rset5.getString(1);
					num_boe_act_qty=rset5.getInt(2);
				}
				rset5.close();
				stmt5.close();
				
				String num_bl="";
				String queryString6="SELECT COUNT(*) "
						+ "FROM FMS_BUY_CARGO_ALLOC_BL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND ALLOC_REV=? ";
				stmt5=conn.prepareStatement(queryString6);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, cont_no);
				stmt5.setString(4, agmt_type);
				stmt5.setString(5, agmt_no);
				stmt5.setString(6, contract_type);
				stmt5.setString(7, cargo_no);
				stmt5.setString(8, alloc_rev);
				rset5=stmt5.executeQuery();
				while(rset5.next())
				{
					num_bl=rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				String ship_name="";
				String ship_eff_dt="";
				
				String queryString3="SELECT SHIP_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_SHIP_MST "
						+ "WHERE SHIP_CD=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, ship_cd);
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					ship_name =(rset3.getString(1)==null?"":rset3.getString(1));
					ship_eff_dt=(rset3.getString(2)==null?"":rset3.getString(2));
					 
				}
				rset3.close();
				stmt3.close();
				
				int count1=0;

				String cargo_ref="";
				String mandate_conf_vol="";
				String conf_price="";
				String rate_unit="";
				String win_start_dt="";
				String win_end_dt="";
				String tolerance_per="";
				
				String queryString1="SELECT CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,"
						+ "RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TOLERANCE "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
						+ "AND AGMT_REV=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
				queryString1+="AND A.END_DT >= TO_DATE(?, 'DD/MM/YYYY') AND A.START_DT <= TO_DATE(?, 'DD/MM/YYYY') ";
				
				stmt2=conn.prepareStatement(queryString1);
				stmt2.setString(++count1, comp_cd);
				stmt2.setString(++count1, counterparty_cd);
				stmt2.setString(++count1, agmt_type);
				stmt2.setString(++count1, agmt_no);
				stmt2.setString(++count1, contract_type);
				stmt2.setString(++count1, cont_no);
				stmt2.setString(++count1, cargo_no);
				stmt2.setString(++count1, agmt_rev_no);
				stmt2.setString(++count1, from_dt);
				stmt2.setString(++count1, to_dt);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					cargo_ref=(rset2.getString(1)==null?"":rset2.getString(1));
					String cargo_status=(rset2.getString(2)==null?"":rset2.getString(2));
					mandate_conf_vol=(rset2.getString(3)==null?"":rset2.getString(3));
					conf_price = (rset2.getString(4)==null?"":rset2.getString(4));
					rate_unit=(rset2.getString(5)==null?"":rset2.getString(5));
					win_start_dt =(rset2.getString(6)==null?"":rset2.getString(6));
					win_end_dt = rset2.getString(7)==null?"":rset2.getString(7);
					tolerance_per = rset2.getString(8)==null?"":rset2.getString(8);
					
					String queryString2 = "SELECT AGMT_BASE,FCC_FLAG,CONT_REF_NO "
							+ "FROM FMS_TRADER_CN_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
							+ "AND CONT_NO=? AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
					stmt1=conn.prepareStatement(queryString2);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, cont_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmt_base =(rset1.getString(1)==null?"":rset1.getString(1));
						fcc_flg = rset1.getString(2)==null?"":rset1.getString(2);
						cont_ref_no = rset1.getString(3)==null?"":rset1.getString(3);
					}
					rset1.close();
					stmt1.close();
					
					String cont_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
					String cargo_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
					
					if(Integer.parseInt(num_boe)>0) 
					{
						VCARGO_STATUS.add(rset2.getString(2)==null?"":rset2.getString(2));
						VCONT_REF_NO.add(cont_ref_no);
						
						if(agmt_base.equals("X"))
						{
							VAGMT_BASE.add("Ex-Ship");
						}
						else if(agmt_base.equals("D"))
						{
							VAGMT_BASE.add("DES");
						}
						else
						{
							VAGMT_BASE.add("");
						}
						VTOTAL_BL_NO.add(num_bl);
						VTOTAL_BOE_NO.add(num_boe);
						VALLOC_REV_NO.add(alloc_rev);
						VFCC_FLAG.add(fcc_flg);
						VSTART_DT.add(win_start_dt);
						VEND_DT.add(win_end_dt);

						VALLOCATION_STATUS.add("Allocated");
						VALLOC_STATUS.add("Unloaded");
						
						VCONTRACT_TYPE.add(cont_type);
						VCONT_NO.add(contno);
						VCARGO_REF.add(cargo_ref);
						
						if(!mandate_conf_vol.equals(""))
						{
							VCARGO_QTY.add(utilBean.RateNumberFormat(Double.parseDouble(mandate_conf_vol),"1"));
						}
						else
						{
							VCARGO_QTY.add("");
						}
						if(!conf_price.equals(""))
						{
							VRATE.add(utilBean.RateNumberFormat(Double.parseDouble(conf_price), rate_unit));
						}
						else
						{
							VRATE.add("");
						}
						
						VRATE_UNIT.add(rate_unit);
						VCARGO_NAME.add(cargo_name);
						VCARGO_NO.add(cargo_no);
						VCONT_NAME.add(cont_name);
						VAGMT_TYPE.add(agmt_type);
						VAGMT_NO.add(agmt_no);
						VAGMT_REV_NO.add(agmt_rev_no);
						VCONT_REV_NO.add(cont_rev_no);
						
						VACT_ARRV_DT.add(act_arrv_dt);
						VQQ_QTY_MMBTU.add(qq_qty_mmbtu);
						VQQ_QTY_CER_NO.add(qq_qty_cer_no);
						VQQ_QTY_CER_DT.add(qq_qty_cer_dt);
						

						VSHIP_CD.add(ship_cd);
						VSHIP_NAME.add(ship_name);
						VSHIP_EFF_DT.add(ship_eff_dt);
						
						VBUYER_CD.add(counterparty_cd);		
						VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
					}
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			
			for(int i=0;i<VCARGO_NAME.size();i++)
			{
				int no_win_days = dateUtil.getDays(""+VEND_DT.elementAt(i),""+VSTART_DT.elementAt(i));
				
				String cargo_mapping = comp_cd+"-"+counterparty_cd+"-"+VAGMT_TYPE.elementAt(i)+"-"+VAGMT_NO.elementAt(i)+"-"+VAGMT_REV_NO.elementAt(i)+"-"+VCONTRACT_TYPE.elementAt(i)+"-"+VCONT_NO.elementAt(i)+"-"+VCONT_REV_NO.elementAt(i)+"-"+VCARGO_NO.elementAt(i);
				
				int cnt=0;
				int attachCargoCnt=0;
				boolean isAttached =false; 
				
				String queryString2="SELECT COUNT(*) "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND BUY_SALE=? AND ATTACH_LNG_CARGO=? ";
				stmt3 = conn.prepareStatement(queryString2);
				stmt3.setString(++cnt, comp_cd);
				stmt3.setString(++cnt, "T");
				stmt3.setString(++cnt, cargo_mapping);
				rset3=stmt3.executeQuery();
				if(rset3.next()) 
				{
					attachCargoCnt=rset3.getInt(1);
					if(attachCargoCnt>0)
					{
						isAttached = true;
					}
				}
				rset3.close();
				stmt3.close();
				
				if(isAttached) 
				{
					VCARGO_ISATTACHED.add("Y");
				}
				else
				{
					VCARGO_ISATTACHED.add("N");
				}
				
				VNO_WIN_DAYS.add(no_win_days); 
				VCARGO_MAPPING.add(cargo_mapping);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTluChargeMaster()
	{
		String function_nm="getTluChargeMaster()";
		try
		{
			utilBean.getTluChargeMaster(conn);
			VCHARGE_NAME=utilBean.getCHARGE_NAME();
			VCHARGE_ABBR=utilBean.getCHARGE_ABBR();
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
			String queryString="SELECT FILL_STATION_CD,FILL_STATION_ABBR "
					+ "FROM FMS_FILLING_STATION_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_FILLING_STATION_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND ACTIVE_FLAG=?";
			stmt = conn.prepareStatement(queryString);
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getNominatedChk()
	{
		String function_nm="getNomintatedChk()";
		try
		{
			for(int i=0;i<VPLANT_SEQ_NO.size();i++)
			{
				VDLNG_NOM_SEL_CUST_CHK.add(checkCustPlantNominated(""+VPLANT_SEQ_NO.elementAt(i)));
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
			query="SELECT CASE WHEN PLANT_SEQ=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_BUYER_NOM "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? ";
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
	
	public String checkTruckTransporterNominated(String trans_cd)
	{
		String function_nm="checkTruckTransporterNominated()";
		String chk_trans_nom="";
		try
		{
			String query="SELECT CASE WHEN TRUCK_TRANS_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_BUYER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? " 
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND TRUCK_TRANS_CD=? ";
			query+="UNION ";
			query+="SELECT CASE WHEN TRUCK_TRANS_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? " 
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND TRUCK_TRANS_CD=? ";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, trans_cd);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, trans_cd);
			stmt_temp.setString(8, trans_cd);
			stmt_temp.setString(9, comp_cd);
			stmt_temp.setString(10, counterparty_cd);
			stmt_temp.setString(11, agmt_no);
			stmt_temp.setString(12, cont_no);
			stmt_temp.setString(13, contract_type);
			stmt_temp.setString(14, trans_cd);
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
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND FILL_STATION_CD=? ";
			query+="UNION ";
			query+="SELECT CASE WHEN FILL_STATION_CD=? THEN 'Y' ELSE 'N' END CUSTOMER_PLANT "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND FILL_STATION_CD=? ";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(1, filling_station_cd);
			stmt_temp.setString(2, comp_cd);
			stmt_temp.setString(3, counterparty_cd);
			stmt_temp.setString(4, agmt_no);
			stmt_temp.setString(5, cont_no);
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, filling_station_cd);
			stmt_temp.setString(8, filling_station_cd);
			stmt_temp.setString(9, comp_cd);
			stmt_temp.setString(10, counterparty_cd);
			stmt_temp.setString(11, agmt_no);
			stmt_temp.setString(12, cont_no);
			stmt_temp.setString(13, contract_type);
			stmt_temp.setString(14, filling_station_cd);
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
	
	public void getContractSelectedDlngCustomerPlantList()
	{
		String function_nm="getContractSelectedCustomerPlantList()";
		try
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
				
				VSEL_DLNG_TRAD_CD.add(counterparty_cd);
				VSEL_DLNG_PLANT_SEQ_NO.add(plant_seq);
				VSEL_DLNG_PLANT_ABBR.add(plant_abbr);
				
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
							+ "FROM FMS_LTCORA_CONT_PLANT_CHRG A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ_NO=? AND CHARGE_ABBR=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_LTCORA_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
							+ "FROM FMS_LTCORA_CONT_PLANT_CHRG A "
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
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String cont_start_dt = "";
	String cont_end_dt = "";
	String agreement_type = "";
	String buy_sale = "";
	String no_cargo = "";
	String agmt_type = "";
	String cargo_no = "";
	String display_msg ="";
	String max_end_dt ="";
	String from_dt = "";
	String to_dt = "";
	String active_status = "";
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setCont_end_dt(String cont_end_dt) {this.cont_end_dt = cont_end_dt;}
	public void setAgreement_type(String agreement_type) {this.agreement_type = agreement_type;}
	public void setBuy_sale(String buy_sale) {this.buy_sale = buy_sale;}
	public void setNo_cargo(String no_cargo) {this.no_cargo = no_cargo;}
	public void setAgmt_type(String agmt_type) {this.agmt_type = agmt_type;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VTRANS_CD = new Vector();
	Vector VTRANS_PLANT_NM = new Vector();
	Vector VTRANS_PLANT_ABBR = new Vector();
	Vector VTRANS_PLANT_SEQ_NO = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRAD_CD = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VAGMT_STATUS = new Vector();
	Vector VAGMT_STATUS_FLG = new Vector();
	Vector VAGMT_REF_NO = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VSUG_PER = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VCONT_CARGO_NO = new Vector();
	Vector VDEAL_MAPPING = new Vector();

	Vector VCARGO_NO = new Vector();
	Vector VATTACH_LNG_CARGO = new Vector();
	Vector VDISP_ATTACH_LNG_CARGO = new Vector();
	Vector VCARGO_REF_NO = new Vector();
	Vector VSHIP_CD = new Vector();
	Vector VSUPP_CD = new Vector();
	Vector VSHIP_NM = new Vector();
	Vector VSUPP_NM = new Vector();
	Vector VEDQ_FROM_DT = new Vector();
	Vector VEDQ_TO_DT = new Vector();
	Vector VACTUAL_RECPT_DT = new Vector();
	Vector VEDQ_QTY = new Vector();
	Vector VCSOC_QTY = new Vector();
	Vector VBOE_QTY = new Vector();
	Vector VBOE_NO = new Vector();
	Vector VBOE_DT = new Vector();
	Vector VQQ_NO = new Vector();
	Vector VQQ_DT = new Vector();
	Vector VADQ_QTY = new Vector();
	Vector VADQ_DT = new Vector();
	Vector VTOTAL_ADQ_QTY = new Vector();
	Vector VSTORAGE_DAYS = new Vector();
	Vector VSTORAGE_EXT_DAYS = new Vector();
	
	Vector VCSOC_SEQ_NO = new Vector();
	Vector VCSOC_FROM_DT = new Vector();
	Vector VCSOC_TO_DT = new Vector();
	Vector VVAR_CSOC = new Vector();
	Vector VCSOC_REMARK = new Vector();
	
	Vector VSEQ_NO = new Vector();
	Vector VFROM_DT = new Vector();
	Vector VTO_DT = new Vector();
	Vector VSTORAGE_RATE = new Vector();
	Vector VTILL_END = new Vector();
	
	Vector VDISP_AGMT_NO = new Vector();
	Vector VINVOICE_TYPE = new Vector();		//Pratham Bhatt 20240821: for storing invoice type 
	Vector VINVOICE_CATEGORY = new Vector(); 	//Pratham Bhatt 20240822: for storing invoice category
	
	Vector VTEMP_TRANS_CD = new Vector();
	Vector VTEMP_TRANS_ABBR = new Vector();
	Vector VSEL_TRANS_CD = new Vector();
	Vector VSEL_TRANS_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRANS_PLANT_ABBR = new Vector();
	
	Vector VBUYER_CD = new Vector();
	Vector VBUYER_NAME = new Vector();
	Vector VBUYER_ABBR = new Vector();
	Vector VACT_ARRV_DT = new Vector();
	Vector VQQ_QTY_MMBTU = new Vector();
	Vector VQQ_QTY_CER_NO = new Vector();
	Vector VQQ_QTY_CER_DT = new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VSHIP_EFF_DT = new Vector();
	Vector VCARGO_STATUS = new Vector();
	Vector VTOTAL_BL_NO = new Vector();
	Vector VBL_NO = new Vector();
	Vector VTOTAL_BOE_NO = new Vector();
	Vector VALLOC_REV_NO = new Vector();
	Vector VFCC_FLAG = new Vector();
	Vector VALLOCATION_STATUS = new Vector();
	Vector VALLOC_STATUS = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VCARGO_REF = new Vector();
	Vector VCARGO_QTY = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VCARGO_NAME = new Vector();
	Vector VNO_WIN_DAYS = new Vector();
	Vector VCARGO_MAPPING = new Vector();
	Vector VCARGO_ISATTACHED = new Vector();
	Vector VCARGO_IS_NOMINATED = new Vector();
	Vector VCARGO_MAX_NOM_DT = new Vector();
	Vector VISCARGOADDED = new Vector();
	
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	
	Vector VCLOSURE_FLAG = new Vector();
	Vector VCLOSURE_DT = new Vector();
	Vector VCLOSURE_QTY = new Vector();
	Vector VMOD_REQUEST_FLAG=new Vector();
	
	Vector VBUYER_NOM = new Vector();
	Vector VSELLER_NOM = new Vector();
	Vector VALLOC_QTY=new Vector();
	Vector VGAS_DT=new Vector();
	
	Vector VTRUCK_TRANS_CD = new Vector();
	Vector VTRUCK_TRANS_ABBR = new Vector();
	Vector VTRUCK_TRANS_NM = new Vector();
	Vector VFILL_STATION_CD = new Vector();
	Vector VFILL_STATION_ABBR = new Vector();
	Vector VNOM_SEL_BU_CHK = new Vector();
	Vector VNOM_SEL_TRANS_CHK = new Vector();
	Vector VNOM_SEL_CUST_CHK = new Vector();
	Vector VNOM_SEL_TRUCK_TRANS_CHK = new Vector();
	Vector VNOM_SEL_FILL_CHK = new Vector();
	Vector VDLNG_PLANT_NM = new Vector();
	Vector VDLNG_PLANT_ABBR = new Vector();
	Vector VDLNG_PLANT_SEQ_NO = new Vector();
	Vector VDLNG_NOM_SEL_CUST_CHK = new Vector();
	Vector VCHARGE_ABBR = new Vector();
	Vector VCHARGE_NAME = new Vector();
	Vector VSEL_TRUCK_TRANS_CD = new Vector();
	Vector VSEL_TRUCK_TRANS_ABBR = new Vector();
	Vector VSEL_FILL_STATION_CD = new Vector();
	Vector VSEL_FILL_STATION_ABBR = new Vector();
	Vector VSEL_DLNG_PLANT_SEQ_NO = new Vector();
	Vector VSEL_DLNG_PLANT_ABBR = new Vector();
	Vector VSEL_DLNG_TRAD_CD = new Vector();
	Vector VSEL_CHARGE_VALUE = new Vector();
	Vector VSEL_CHARGE_DESC=new Vector();
	
	public Vector getVDLNG_PLANT_NM() {return VDLNG_PLANT_NM;}
	public Vector getVDLNG_PLANT_ABBR() {return VDLNG_PLANT_ABBR;}
	public Vector getVDLNG_PLANT_SEQ_NO() {return VDLNG_PLANT_SEQ_NO;}
	public Vector getVDLNG_NOM_SEL_CUST_CHK() {return VDLNG_NOM_SEL_CUST_CHK;}
	public Vector getVCHARGE_ABBR() {return VCHARGE_ABBR;}
	public Vector getVCHARGE_NAME() {return VCHARGE_NAME;}

	public Vector getVTRUCK_TRANS_CD() {return VTRUCK_TRANS_CD;}
	public Vector getVTRUCK_TRANS_ABBR() {return VTRUCK_TRANS_ABBR;}
	public Vector getVTRUCK_TRANS_NM() {return VTRUCK_TRANS_NM;}
	public Vector getVFILL_STATION_CD() {return VFILL_STATION_CD;}
	public Vector getVFILL_STATION_ABBR() {return VFILL_STATION_ABBR;}
	public Vector getVNOM_SEL_BU_CHK() {return VNOM_SEL_BU_CHK;}
	public Vector getVNOM_SEL_TRANS_CHK() {return VNOM_SEL_TRANS_CHK;}
	public Vector getVNOM_SEL_CUST_CHK() {return VNOM_SEL_CUST_CHK;}
	public Vector getVNOM_SEL_TRUCK_TRANS_CHK() {return VNOM_SEL_TRUCK_TRANS_CHK;}
	public Vector getVNOM_SEL_FILL_CHK() {return VNOM_SEL_FILL_CHK;}
	public Vector getVSEL_TRUCK_TRANS_CD() {return VSEL_TRUCK_TRANS_CD;}
	public Vector getVSEL_TRUCK_TRANS_ABBR() {return VSEL_TRUCK_TRANS_ABBR;}
	public Vector getVSEL_FILL_STATION_CD() {return VSEL_FILL_STATION_CD;}
	public Vector getVSEL_FILL_STATION_ABBR() {return VSEL_FILL_STATION_ABBR;}
	public Vector getVSEL_DLNG_PLANT_SEQ_NO() {return VSEL_DLNG_PLANT_SEQ_NO;}
	public Vector getVSEL_DLNG_TRAD_CD() {return VSEL_DLNG_TRAD_CD;}
	public Vector getVSEL_DLNG_PLANT_ABBR() {return VSEL_DLNG_PLANT_ABBR;}
	public Vector getVSEL_CHARGE_VALUE() {return VSEL_CHARGE_VALUE;}
	public Vector getVSEL_CHARGE_DESC() {return VSEL_CHARGE_DESC;}
	
	public Vector getVCLOSURE_FLAG() {return VCLOSURE_FLAG;}
	public Vector getVCLOSURE_DT() {return VCLOSURE_DT;}
	public Vector getVCLOSURE_QTY() {return VCLOSURE_QTY;}
	public Vector getVMOD_REQUEST_FLAG() {return VMOD_REQUEST_FLAG;}
	
	public Vector getVBUYER_CD() {return VBUYER_CD;}
	public Vector getVBUYER_NAME() {return VBUYER_NAME;}
	public Vector getVBUYER_ABBR() {return VBUYER_ABBR;}
	public Vector getVACT_ARRV_DT() {return VACT_ARRV_DT;}
	public Vector getVQQ_QTY_MMBTU() {return VQQ_QTY_MMBTU;}
	public Vector getVQQ_QTY_CER_NO() {return VQQ_QTY_CER_NO;}
	public Vector getVQQ_QTY_CER_DT() {return VQQ_QTY_CER_DT;}
	public Vector getVSHIP_NAME() {return VSHIP_NAME;}
	public Vector getVSHIP_EFF_DT() {return VSHIP_EFF_DT;}
	public Vector getVCARGO_STATUS() {return VCARGO_STATUS;}
	public Vector getVTOTAL_BL_NO() {return VTOTAL_BL_NO;}
	public Vector getVBL_NO() {return VBL_NO;}
	public Vector getVTOTAL_BOE_NO() {return VTOTAL_BOE_NO;}
	public Vector getVALLOC_REV_NO() {return VALLOC_REV_NO;}
	public Vector getVFCC_FLAG() {return VFCC_FLAG;}
	public Vector getVALLOCATION_STATUS() {return VALLOCATION_STATUS;}
	public Vector getVALLOC_STATUS() {return VALLOC_STATUS;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVCARGO_REF() {return VCARGO_REF;}
	public Vector getVCARGO_QTY() {return VCARGO_QTY;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVCARGO_NAME() {return VCARGO_NAME;}
	public Vector getVNO_WIN_DAYS() {return VNO_WIN_DAYS;}
	public Vector getVCARGO_MAPPING() {return VCARGO_MAPPING;}
	public Vector getVCARGO_ISATTACHED() {return VCARGO_ISATTACHED;}
	public Vector getVCARGO_IS_NOMINATED() {return VCARGO_IS_NOMINATED;}
	public Vector getVCARGO_MAX_NOM_DT() {return VCARGO_MAX_NOM_DT;}
	public Vector getVISCARGOADDED() {return VISCARGOADDED;}
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}			//Pratham Bhatt 20240821:Getter function for invoice type 
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}	//Pratham Bhatt 20240822:Getter function   
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVTRANS_CD() {return VTRANS_CD;}
	public Vector getVTRANS_PLANT_NM() {return VTRANS_PLANT_NM;}
	public Vector getVTRANS_PLANT_ABBR() {return VTRANS_PLANT_ABBR;}
	public Vector getVTRANS_PLANT_SEQ_NO() {return VTRANS_PLANT_SEQ_NO;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVSEL_TRAD_CD() {return VSEL_TRAD_CD;}
	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVAGMT_STATUS() {return VAGMT_STATUS;}
	public Vector getVAGMT_STATUS_FLG() {return VAGMT_STATUS_FLG;}
	public Vector getVAGMT_REF_NO() {return VAGMT_REF_NO;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVSUG_PER() {return VSUG_PER;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_TYPE() {return VCONT_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVCONT_CARGO_NO() {return VCONT_CARGO_NO;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	
	public Vector getVDISP_ATTACH_LNG_CARGO() {return VDISP_ATTACH_LNG_CARGO;}
	public Vector getVATTACH_LNG_CARGO() {return VATTACH_LNG_CARGO;}
	public Vector getVCARGO_REF_NO() {return VCARGO_REF_NO;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVSHIP_CD() {return VSHIP_CD;}
	public Vector getVSUPP_CD() {return VSUPP_CD;}
	public Vector getVSHIP_NM() {return VSHIP_NM;}
	public Vector getVSUPP_NM() {return VSUPP_NM;}
	public Vector getVEDQ_FROM_DT() {return VEDQ_FROM_DT;}
	public Vector getVEDQ_TO_DT() {return VEDQ_TO_DT;}
	public Vector getVACTUAL_RECPT_DT() {return VACTUAL_RECPT_DT;}
	public Vector getVEDQ_QTY() {return VEDQ_QTY;}
	public Vector getVCSOC_QTY() {return VCSOC_QTY;}
	public Vector getVBOE_QTY() {return VBOE_QTY;}
	public Vector getVBOE_NO() {return VBOE_NO;}
	public Vector getVBOE_DT() {return VBOE_DT;}
	public Vector getVQQ_NO() {return VQQ_NO;}
	public Vector getVQQ_DT() {return VQQ_DT;}
	public Vector getVADQ_QTY() {return VADQ_QTY;}
	public Vector getVADQ_DT() {return VADQ_DT;}
	public Vector getVTOTAL_ADQ_QTY() {return VTOTAL_ADQ_QTY;}
	public Vector getVSTORAGE_EXT_DAYS() {return VSTORAGE_EXT_DAYS;}
	public Vector getVSTORAGE_DAYS() {return VSTORAGE_DAYS;}
	
	public Vector getVCSOC_SEQ_NO() {return VCSOC_SEQ_NO;}
	public Vector getVCSOC_FROM_DT() {return VCSOC_FROM_DT;}
	public Vector getVCSOC_TO_DT() {return VCSOC_TO_DT;}
	public Vector getVVAR_CSOC() {return VVAR_CSOC;}
	public Vector getVCSOC_REMARK() {return VCSOC_REMARK;}
	
	public Vector getVFROM_DT() {return VFROM_DT;}
	public Vector getVTO_DT() {return VTO_DT;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVSTORAGE_RATE() {return VSTORAGE_RATE;}
	
	public Vector getVDISP_AGMT_NO() {return VDISP_AGMT_NO;}
	
	public Vector getVTEMP_TRANS_CD() {return VTEMP_TRANS_CD;}
	public Vector getVTEMP_TRANS_ABBR() {return VTEMP_TRANS_ABBR;}
	
	public Vector getVSEL_TRANS_CD() {return VSEL_TRANS_CD;}
	public Vector getVSEL_TRANS_PLANT_SEQ_NO() {return VSEL_TRANS_PLANT_SEQ_NO;}
	public Vector getVSEL_TRANS_PLANT_ABBR() {return VSEL_TRANS_PLANT_ABBR;}
	
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	
	public Vector getVBUYER_NOM() {return VBUYER_NOM;}
	public Vector getVSELLER_NOM() {return VSELLER_NOM;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVTILL_END() {return VTILL_END;}
	
	
	String min_counterparty_eff_dt = "";
	String contpty_abbr="";
	String signing_dt = "";
	String signing_time = "";
	String dda_dt = "";
	String dda_time = "";
	String start_dt = "";
	String end_dt = "";
	String agmt_agmt_base = "";
	String agmt_base = "";
	String status="";
	String status_nm="";
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
	String buy_nom_clause="";
	String sell_nom_clause="";
	String day_def_flag = "";
	String day_start_time = "";
	String day_end_time = "";
	String mdcq_flag = "";
	String mdcq_percentage = "";
	String remark = "";
	String ent_dt = "";
	String ent_time = "";
	String cont_name = "";
	String bill_flag="";
	String rev_dt="";
	String cont_ref_no = "";
	String cont_status="";
	String cont_status_flg="";
	
	String billing_clause="";
	String off_spec_gas_flag="";
	String spec_gas_eng_base="";
	String spec_max_eng="";
	String spec_min_eng = "";
	String messurment_flag = "";
	String meas_std = "";
	String meas_temp = "";
	String pressure_max_bar = "";
	String pressure_min_bar = "";
	String liability_flag = "";
	String liability_clause = "";
	String liability_lq_dmg = "";
	String liability_take_pay = "";
	String liability_makeup = "";
	String terminaton_flag = "";
	String termination_clause = "";
	String termination_planned = "";
	String termination_forced = "";
	String agmt_signing_dt = "";
	String agmt_start_dt = "";
	String agmt_end_dt = "";
	
	String day_def_clause="";
	String mdcq_clause="";
	String meas_clause="";
	String spec_clause="";
	
	String no_of_billing_dtl="";
	String no_of_security_dtl="";
	
	String fcc_flg="";
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
	String due_dt_in="";
	String exclude_sat="";
	String billing_days="";
	String exchg_val="";
	
	String no_of_cargo="";
	String ltcora_tariff="";
	String ltcora_tariff_unit="";
	String sug="";
	String tariff_discount="";
	String vol_discount="";
	String vol_discount_unit="";
	String tlu_flag="";
	String adv_adjust="";
	String adv_adjust_type="";
	String adv_adjust_amount="";
	String adv_adjust_unit="";
	String extend_storage="";
	String discount_days="";
	String storage_tariff_mode="";
	String storage_tariff_unit="";
	String storage_tariff="";
	String approval_flag = "";
	String allocated_qty_mmbtu="";
	String storage_days="";
	String storage_ext_days="";
	
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
	String makeup_liab_on = "";
	String recovery_day = "";
	String mug_remark = "";
	
	String display_map_id="";
	String display_agmt_id="";
	String counterparty_abbr="";
	String counterparty_name="";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	
	String closure_qty="";
	String clsr_eff_dt="";
	String clsr_flag ="";
	
	String total_buyer_nom="";
	String total_seller_nom="";
	String total_alloc_qty ="";
	String clsr_note="";
	String is_expired="";
	String is_cargo_alloc="";
	double sug_per = 0;
	int mod_count = 0;
	
	public String getIs_cargo_alloc() {return is_cargo_alloc;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getClosure_qty() {return closure_qty;}
	public String getClosure_eff_dt() {return clsr_eff_dt;}
	public String getClosure_flag() {return clsr_flag;}
	public String getClosure_note() {return clsr_note;}
	public String getIs_expired() {return is_expired;}
	
	public String getStorage_days() {return storage_days;}
	public String getStorage_ext_days() {return storage_ext_days;}
	public String getAllocated_qty_mmbtu() {return allocated_qty_mmbtu;}
	
	public String getDisplay_map_id() {return display_map_id;}
	public String getDisplay_agmt_id() {return display_agmt_id;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getCounterparty_name() {return counterparty_name;}
	
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	public String getContpty_abbr() {return contpty_abbr;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getAgmt_agmt_base() {return agmt_agmt_base;}
	public String getAgmt_base() {return agmt_base;}
	public String getAgmt_type() {return agmt_type;}
	public String getStatus() {return status;}
	public String getStatus_nm() {return status_nm;}
	public String getBuy_nom_clause() {return buy_nom_clause;}
	public String getBuy_nom_flag() {return buy_nom_flag;}
	public String getBuy_month_nom() {return buy_month_nom;}
	public String getBuy_fortnightly_nom() {return buy_fortnightly_nom;}
	public String getBuy_week_nom() {return buy_week_nom;}
	public String getBuy_daily_nom() {return buy_daily_nom;}
	public String getBuy_nom_cutoff_time() {return buy_nom_cutoff_time;}
	public String getSell_nom_clause() {return sell_nom_clause;}
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
	public String getEnt_dt() {return ent_dt;}
	public String getEnt_time() {return ent_time;}
	public String getCont_name() {return cont_name;}
	public String getBill_flag() {return bill_flag;}
	public String getRev_dt() {return rev_dt;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getCont_status() {return cont_status;}
	public String getCont_status_flg() {return cont_status_flg;}
	
	public String getBilling_clause() {return billing_clause;}
	public String getOff_spec_gas_flag() {return off_spec_gas_flag;}
	public String getSpec_gas_eng_base() {return spec_gas_eng_base;}
	public String getSpec_max_eng() {return spec_max_eng;}
	public String getSpec_min_eng() {return spec_min_eng;}
	public String getMessurment_flag() {return messurment_flag;}
	public String getMeas_std() {return meas_std;}
	public String getMeas_temp() {return meas_temp;}
	public String getPressure_max_bar() {return pressure_max_bar;}
	public String getPressure_min_bar() {return pressure_min_bar;}
	public String getLiability_flag() {return liability_flag;}
	public String getLiability_clause() {return liability_clause;}
	public String getLiability_lq_dmg() {return liability_lq_dmg;}
	public String getLiability_take_pay() {return liability_take_pay;}
	public String getLiability_makeup() {return liability_makeup;}
	public String getTerminaton_flag() {return terminaton_flag;}
	public String getTermination_clause() {return termination_clause;}
	public String getTermination_planned() {return termination_planned;}
	public String getTermination_forced() {return termination_forced;}
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
	
	public String getDay_def_clause() {return day_def_clause;}
	public String getMdcq_clause() {return mdcq_clause;}
	public String getMeas_clause() {return meas_clause;}
	public String getSpec_clause() {return spec_clause;}
	
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
	public String getNo_of_security_dtl() {return no_of_security_dtl;}
	
	public String getFcc_flg() {return fcc_flg;}
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
	public String getDue_dt_in() {return due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getBilling_days() {return billing_days;}
	public String getExchg_val() {return exchg_val;}
	
	public String getNo_of_cargo() {return no_of_cargo;}
	public String getLtcora_tariff() {return ltcora_tariff;}
	public String getLtcora_tariff_unit() {return ltcora_tariff_unit;}
	public String getSug() {return sug;}
	public String getTariff_discount() {return tariff_discount;}
	public String getVol_discount() {return vol_discount;}
	public String getVol_discount_unit() {return vol_discount_unit;}
	public String getTlu_flag() {return tlu_flag;}
	public String getAdv_adjust() {return adv_adjust;}
	public String getAdv_adjust_type() {return adv_adjust_type;}
	public String getAdv_adjust_amount() {return adv_adjust_amount;}
	public String getAdv_adjust_unit() {return adv_adjust_unit;}
	public String getExtend_storage() {return extend_storage;}
	public String getDiscount_days() {return discount_days;}
	public String getStorage_tariff_mode() {return storage_tariff_mode;}
	public String getStorage_tariff_unit() {return storage_tariff_unit;}
	public String getStorage_tariff() {return storage_tariff;}
	public String getApproval_flag() {return approval_flag;}
	
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
	public String getMakeup_liab_on() {return makeup_liab_on;}
	public String getRecovery_day() {return recovery_day;}
	public String getMug_remark() {return mug_remark;}
	
	public Double getSug_per() {return sug_per;}
	public int getMod_count() {return mod_count;}
	
	public String getDisplay_msg() {return display_msg;}
	public String getMax_end_dt() {return max_end_dt;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
	
	public String getTotal_buyer_nom() {return total_buyer_nom;}
	public String getTotal_seller_nom() {return total_seller_nom;}
	public String getTotal_alloc_qty() {return total_alloc_qty;}
}
