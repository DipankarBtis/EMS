package com.etrm.fms.cargo;

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

public class DataBean_Cargo_mst 
{
	String db_src_file_name="DataBean_Cargo_mst.java";

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
	    			if(callFlag.equalsIgnoreCase("MSPA_AGREEMENT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getMSPAtraderlist();
	    					getCurrentContractDtl();
	    				}
	    				else if(opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getTraderCounterpartyList();
	    				}
	    				getTraderPlantList();
	    				getBusinessPlantList();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getTraderAgreementDetail();
	    					getSelectedTraderPlantList();
		    				getSelectedBusinessPlantList();
		    				getCountBillingDetail();
		    				getCountSecurityDetail();
		    				getPriceChangeHistory();
		    				getSelectedMspaLiabilityDtl();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("MSP_AGMT_LIST"))
	    			{
	    				getMSPAgreement_List();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MSP_AGREEMENT_BILLING_DTL"))
	    			{
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getBillingDetail();
	    				getApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CN_AGREEMENT_LIST"))
	    			{
	    				getMSPAgreement_List();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONFIRM_NOTICE_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getCNtraderlist();
	    				}
	    				else if(opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getContractMSPAtraderlist();
	    				}
	    				getCnDetail();
	    				getTraderPlantList();
	    				getBusinessPlantList();
	    				if(opration.equalsIgnoreCase("INSERT"))
	    				{
		    				getSelectedTraderPlantList();
		    				getSelectedBusinessPlantList();
	    				}
	    				getSelectedMspaLiabilityDtl();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getCnContractDetail();
	    					getSelectedCnTraderPlantList();
		    				getSelectedCnBusinessPlantList();
		    				getCnCountBillingDetail();
		    				getCountSecurityDetail();
		    				getCargoNoDtls();
		    				GetCnCargoDtls();
		    				getSelectedCnLiabilityDtl();
	    				}
	    				getAgmtBaseFormAgmt();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CN_CONT_LIST_FCC"))
	    			{
	    				getfccCnContList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CN_CONT_LIST"))
	    			{
	    				getCnContList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CN_BILLING_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getCnCountBillingDetail();
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getCnBillingDetail();
	    				getCnApplicableTaxes();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CARGO_NOMINATION"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				getCNtraderlist();
	    				if(!ship_cd.equals("")) 
	    				{
	    					getShipMst();
	    				}
	    				getCountryList();
						if(!cargo_number.equals("") && !confirm_no.equals("")) 
	    				{
	    					getCargoList();
	    					getContractDetail();
	    				}
						
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getCargoNominationDetails();
	    					getCargoBLDetails();
	    					getCargoBOEDetails();
	    					getCargoBLLinkedBOEDetails();
	    					getSelectedCnTraderPlantList();
		    				getSelectedCnBusinessPlantList();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("CARGO_NOMINATION_SHIP_DTL"))
	    			{
	    				getShipMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("NOM_CARGO_LIST"))
	    			{
	    				getCargoList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MSPA_LIABILITY_CLAUSE"))
	    			{
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getMspaLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CN_LIABILITY_CLAUSE"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getCnLiabilityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CARGO_ARRIVAL_LIST"))
	    			{
	    				getAllocationCargoList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CARGO_ARRIVAL"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getArrivaltraderlist();
	    				getSelectedCnTraderPlantList();
	    				getSelectedCnBusinessPlantList();
	    				if(!counterparty_cd.equals("") && !cargo_no.equals(""))
	    				{
	    					getCnCargoDtls();
	    				}
	    				getCountryList();
	    				if(!allocation_status.equals("Pending"))
	    				{
	    					getCargoArrivalDtl();
	    				}
	    				getAllocatedCargoBLDetails();
    					getAllocatedCargoBOEDetails();
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
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
					+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND A.AGMT_NO=? "
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
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
					+ "FROM FMS_TRADER_AGMT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_PLANT B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_type);
			stmt.setString(4, agmt_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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

	private void getCnCargoDtls() 
	{
		String function_nm="getCnCargoDtls()";
		try
		{
			int count1=0;
			queryString="SELECT CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,"
					+ "RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TOLERANCE "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
					+ "AND AGMT_REV=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
			stmt2=conn.prepareStatement(queryString);
			stmt2.setString(++count1, comp_cd);
			stmt2.setString(++count1, counterparty_cd);
			stmt2.setString(++count1, agmt_type);
			stmt2.setString(++count1, agmt_no);
			stmt2.setString(++count1, contract_type);
			stmt2.setString(++count1, cont_no);
			stmt2.setString(++count1, cargo_no);
			stmt2.setString(++count1, agmt_rev_no);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				
				cargo_ref=(rset2.getString(1)==null?"":rset2.getString(1));
				mandate_conf_vol=(rset2.getString(3)==null?"":rset2.getString(3));
				conf_price = (rset2.getString(4)==null?"":rset2.getString(4));
				rate_unit=(rset2.getString(5)==null?"":rset2.getString(5));
				win_start_dt =(rset2.getString(6)==null?"":rset2.getString(6));
				win_end_dt = rset2.getString(7)==null?"":rset2.getString(7);
				tolerance_per = rset2.getString(8)==null?"":rset2.getString(8);
				
				queryString1 = "SELECT AGMT_BASE,FCC_FLAG,CONT_REF_NO "
						+ "FROM FMS_TRADER_CN_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
						+ "AND CONT_NO=? AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref_no = rset1.getString(3)==null?"":rset1.getString(3);
				}
				rset1.close();
				stmt1.close();
				
				queryString4="SELECT TO_CHAR(EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(EXP_TO_DT,'DD/MM/YYYY') "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO)";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, counterparty_cd);
				stmt4.setString(3, cont_no);
				stmt4.setString(4, agmt_type);
				stmt4.setString(5, agmt_no);
				stmt4.setString(6, contract_type);
				stmt4.setString(7, cargo_no);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					expected_start_dt = rset4.getString(1)==null?"":rset4.getString(1);
					expected_end_dt = rset4.getString(2)==null?"":rset4.getString(2);
				}
				rset4.close();
				stmt4.close();
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}


	private void getCargoArrivalDtl() 
	{
		String function_nm="getCargoArrivalDtl()";
		try
		{
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,SHIP_CD,TO_CHAR(ACT_ARRV_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(BOOKED_DT,'DD/MM/YYYY'),BOOKED_TIME,TO_CHAR(FLOAT_DT,'DD/MM/YYYY'),FLOAT_TIME,TO_CHAR(PILOT_ON_BOARD_DT,'DD/MM/YYYY'),PILOT_ON_BOARD_TIME,TO_CHAR(UNLOAD_ARM_CON_DT,'DD/MM/YYYY'),UNLOAD_ARM_CON_TIME,TO_CHAR(UNLOAD_ARM_DIS_CON_DT,'DD/MM/YYYY'),"
					+ "UNLOAD_ARM_DIS_CON_TIME,TO_CHAR(DISCHARGE_DT,'DD/MM/YYYY'),DISCHARGE_TIME,REMARK,QQ_NO,TO_CHAR(QQ_DT,'DD/MM/YYYY'),QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,QQ_REMARK,ACT_QTY_MMBTU,"
					+ "TO_CHAR(ALL_FAST_DT,'DD/MM/YYYY'),ALL_FAST_TIME,TO_CHAR(CUSTOME_CLEARANCE_START_DT,'DD/MM/YYYY'),CUSTOME_CLEARANCE_START_TIME,TO_CHAR(CUSTOME_CLEARANCE_END_DT,'DD/MM/YYYY'),CUSTOME_CLEARANCE_END_TIME "
					+ "FROM FMS_BUY_CARGO_ALLOC A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_NO=? AND CARGO_NO=? AND SHIP_CD=? "
					+ "AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, cont_no);
			stmt.setString(7, cargo_no);
			stmt.setString(8, ship_cd);
			//stmt.setString(9, agmt_rev_no);
			//stmt.setString(10, cont_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_type=rset.getString(3)==null?"":rset.getString(3);
				agmt_no=rset.getString(4)==null?"":rset.getString(4);
				agmt_rev_no=rset.getString(5)==null?"":rset.getString(5);
				contract_type=rset.getString(6)==null?"":rset.getString(6);
				cont_no=rset.getString(7)==null?"":rset.getString(7);
				cont_rev_no=rset.getString(8)==null?"":rset.getString(8);
				cargo_no=rset.getString(9)==null?"":rset.getString(9);
				alloc_rev=rset.getString(10)==null?"":rset.getString(10);
				ship_cd=rset.getString(11)==null?"":rset.getString(11);
				act_arrv_dt=rset.getString(12)==null?"":rset.getString(12);
				booked_dt=rset.getString(13)==null?"":rset.getString(13);
				booked_time=rset.getString(14)==null?"":rset.getString(14);
				float_dt=rset.getString(15)==null?"":rset.getString(15);
				float_time=rset.getString(16)==null?"":rset.getString(16);
				pob_dt=rset.getString(17)==null?"":rset.getString(17);
				pob_time=rset.getString(18)==null?"":rset.getString(18);
				uac_dt=rset.getString(19)==null?"":rset.getString(19);
				uac_time=rset.getString(20)==null?"":rset.getString(20);
				uadc_dt=rset.getString(21)==null?"":rset.getString(21);
				uadc_time=rset.getString(22)==null?"":rset.getString(22);
				departure_dt=rset.getString(23)==null?"":rset.getString(23);
				departure_time=rset.getString(24)==null?"":rset.getString(24);
				cargo_details_notes=rset.getString(25)==null?"":rset.getString(25);
				qq_cer_no=rset.getString(26)==null?"":rset.getString(26);
				qq_cer_dt=rset.getString(27)==null?"":rset.getString(27);
				String temp_net_unloaded_qunt= rset.getString(28)==null?"":rset.getString(28);
				if(!temp_net_unloaded_qunt.equals(""))
				{
					net_unloaded_qunt=utilBean.RateNumberFormat(Double.parseDouble(temp_net_unloaded_qunt),"1");
				}
				
				String temp_net_unloaded_qunt_mt=rset.getString(29)==null?"":rset.getString(29);
				if(!temp_net_unloaded_qunt_mt.equals(""))
				{
					net_unloaded_qunt_mt=utilBean.RateNumberFormat(Double.parseDouble(temp_net_unloaded_qunt_mt),"1");
				}
				
				String temp_net_unloaded_qunt_m3=rset.getString(30)==null?"":rset.getString(30);
				if(!temp_net_unloaded_qunt_m3.equals(""))
				{
					net_unloaded_qunt_m3=utilBean.RateNumberFormat(Double.parseDouble(temp_net_unloaded_qunt_m3),"1");
				}
				
				String temp_dens_material= rset.getString(31)==null?"":rset.getString(31);
				if(!temp_dens_material.equals(""))
				{
					dens_material=utilBean.RateNumberFormat(Double.parseDouble(temp_dens_material),"1");
				}
				
				String temp_qq_ghv= rset.getString(32)==null?"":rset.getString(32);
				if(!temp_qq_ghv.equals(""))
				{
					qq_ghv=utilBean.RateNumberFormat(Double.parseDouble(temp_qq_ghv), "2");
				}
				String temp_qq_gcv= rset.getString(33)==null?"":rset.getString(33);
				if(!temp_qq_gcv.equals(""))
				{
					qq_gcv=utilBean.RateNumberFormat(Double.parseDouble(temp_qq_gcv), "2");
				}
				app_lab_notes=rset.getString(34)==null?"":rset.getString(34);
				
				String temp_expected_qunt=rset.getString(35)==null?"":rset.getString(35);
				if(!temp_expected_qunt.equals(""))
				{
					expected_qunt=utilBean.RateNumberFormat(Double.parseDouble(temp_expected_qunt),"1");
				}
				
				all_fast_dt = rset.getString(36)==null?"":rset.getString(36);
				all_fast_time = rset.getString(37)==null?"":rset.getString(37);
				custom_start_dt = rset.getString(38)==null?"":rset.getString(38);
				custom_start_time = rset.getString(39)==null?"":rset.getString(39);
				custom_end_dt = rset.getString(40)==null?"":rset.getString(40);
				custom_end_time = rset.getString(41)==null?"":rset.getString(41);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getAllocationCargoList() 
	{
		String function_nm="getAllocationCargoList()";
		try
		{
			int count=0;
			String contno ="";
			String cont_type="";
			String alloc_rev = "";
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
					+ "CONTRACT_TYPE,CONT_NO,CARGO_NO,SHIP_CD,AGMT_REV,CONT_REV,ALLOC_REV,QQ_QTY_MMBTU "
					+ "FROM FMS_BUY_CARGO_ALLOC A "
					+ "WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND COUNTERPARTY_CD=? ";
			}
			queryString+="AND ALLOC_REV=(SELECT MAX(ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++count, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd =(rset.getString(2)==null?"":rset.getString(2));
				VBUYER_CD.add(counterparty_cd);		
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
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
				
				int num_boe_act_qty=0;
				queryString5="SELECT COUNT(*),COUNT(ACT_BOE_QTY) "
						+ "FROM FMS_BUY_CARGO_ALLOC_BOE A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND ALLOC_REV=? ";
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
				
				queryString5="SELECT COUNT(*) "
						+ "FROM FMS_BUY_CARGO_ALLOC_BL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND ALLOC_REV=? ";
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
					num_bl=rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				queryString3="SELECT SHIP_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
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
				
				VSHIP_CD.add(ship_cd);
				VSHIP_NAME.add(ship_name);
				VSHIP_EFF_DT.add(ship_eff_dt);
				
				
				int count1=0;
				//String cargo_ref="";
				
				queryString="SELECT CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,"
						+ "RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TOLERANCE "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
						+ "AND AGMT_REV=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
				stmt2=conn.prepareStatement(queryString);
				stmt2.setString(++count1, comp_cd);
				stmt2.setString(++count1, counterparty_cd);
				stmt2.setString(++count1, agmt_type);
				stmt2.setString(++count1, agmt_no);
				stmt2.setString(++count1, contract_type);
				stmt2.setString(++count1, cont_no);
				stmt2.setString(++count1, cargo_no);
				stmt2.setString(++count1, agmt_rev_no);
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
					
					
					VCARGO_STATUS.add(rset2.getString(2)==null?"":rset2.getString(2));
					
					queryString1 = "SELECT AGMT_BASE,FCC_FLAG,CONT_REF_NO "
							+ "FROM FMS_TRADER_CN_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
							+ "AND CONT_NO=? AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
					stmt1=conn.prepareStatement(queryString1);
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
				}
				rset2.close();
				stmt2.close();
				String cont_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
				String cargo_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
				
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
				if(lab_qty.equals("") || num_boe_act_qty==0)
				{
					alloc_status = "Custom";
					VALLOCATION_STATUS.add("Custom");
					VALLOC_STATUS.add("Custom");
				}
				else
				{
					alloc_status = "Allocated";
					VALLOCATION_STATUS.add("Allocated");
					VALLOC_STATUS.add("Unloaded");
				}
				
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
				
				queryString4="SELECT TO_CHAR(EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(EXP_TO_DT,'DD/MM/YYYY') "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO)";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, counterparty_cd);
				stmt4.setString(3, cont_no);
				stmt4.setString(4, agmt_type);
				stmt4.setString(5, agmt_no);
				stmt4.setString(6, contract_type);
				stmt4.setString(7, cargo_no);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					expected_start_dt = rset4.getString(1)==null?"":rset4.getString(1);
					expected_end_dt = rset4.getString(2)==null?"":rset4.getString(2);
				}
				rset4.close();
				stmt4.close();
			}
			rset.close();
			stmt.close();
			
			
			count=0;
			contno ="";
			cont_type="";
			String nom_rev = "";
			queryString4="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
					+ "CONTRACT_TYPE,CONT_NO,CARGO_NO,SHIP_CD,AGMT_REV,CONT_REV,NOM_REV,TO_CHAR(EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(EXP_TO_DT,'DD/MM/YYYY') "
					+ "FROM FMS_BUY_CARGO_NOM A "
					+ "WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString4+="AND COUNTERPARTY_CD=? ";
			}
			queryString4+="AND NOM_REV=(SELECT MAX(NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) ";
			queryString4 += "AND NOT EXISTS (SELECT 1 FROM FMS_BUY_CARGO_ALLOC "
		            + "WHERE FMS_BUY_CARGO_ALLOC.COMPANY_CD = A.COMPANY_CD "
		            + "AND FMS_BUY_CARGO_ALLOC.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
		            + "AND FMS_BUY_CARGO_ALLOC.AGMT_TYPE = A.AGMT_TYPE "
		            + "AND FMS_BUY_CARGO_ALLOC.AGMT_NO = A.AGMT_NO "
		            + "AND FMS_BUY_CARGO_ALLOC.CONTRACT_TYPE = A.CONTRACT_TYPE "
		            + "AND FMS_BUY_CARGO_ALLOC.CONT_NO = A.CONT_NO "
		            + "AND FMS_BUY_CARGO_ALLOC.CARGO_NO = A.CARGO_NO)";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(++count, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt4.setString(++count, counterparty_cd);
			}
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				String counterparty_cd =(rset4.getString(2)==null?"":rset4.getString(2));
				VBUYER_CD.add(counterparty_cd);		
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
				agmt_type=(rset4.getString(3)==null?"":rset4.getString(3));
				agmt_no=(rset4.getString(4)==null?"":rset4.getString(4));
				contract_type=(rset4.getString(5)==null?"":rset4.getString(5));
				cont_no=(rset4.getString(6)==null?"":rset4.getString(6));
				String cargo_no=(rset4.getString(7)==null?"":rset4.getString(7));
				String ship_cd =(rset4.getString(8)==null?"":rset4.getString(8));
				cont_type = rset4.getString(5)==null?"":rset4.getString(5);
				contno = rset4.getString(6)==null?"":rset4.getString(6);
				agmt_rev_no = rset4.getString(9)==null?"":rset4.getString(9);
				cont_rev_no = rset4.getString(10)==null?"":rset4.getString(10);
				nom_rev=rset4.getString(11)==null?"":rset4.getString(11);
				expected_start_dt = rset4.getString(12)==null?"":rset4.getString(12);
				expected_end_dt = rset4.getString(13)==null?"":rset4.getString(13);

				int blNumber=0;
				queryString5="SELECT COUNT(*) "
						+ "FROM FMS_BUY_CARGO_NOM_BL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND NOM_REV=?";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, cont_no);
				stmt5.setString(4, agmt_type);
				stmt5.setString(5, agmt_no);
				stmt5.setString(6, contract_type);
				stmt5.setString(7, cargo_no);
				stmt5.setString(8, nom_rev);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					num_bl=rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				int boeNumber=0;
				queryString5="SELECT COUNT(*)"
						+ "FROM FMS_BUY_CARGO_NOM_BOE A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV=?";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, counterparty_cd);
				stmt5.setString(3, cont_no);
				stmt5.setString(4, agmt_type);
				stmt5.setString(5, agmt_no);
				stmt5.setString(6, contract_type);
				stmt5.setString(7, cargo_no);
				stmt5.setString(8, nom_rev);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					num_boe=rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				VBL_CNT.add(blNumber);
				VBOE_CNT.add(boeNumber);
				
				queryString3="SELECT SHIP_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
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
				
				VSHIP_CD.add(ship_cd);
				VSHIP_NAME.add(ship_name);
				VSHIP_EFF_DT.add(ship_eff_dt);
				
				
				int count1=0;
				//String cargo_ref="";
				queryString="SELECT CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,"
						+ "RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),tolerance "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
						+ "AND AGMT_REV=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
				
				stmt2=conn.prepareStatement(queryString);
				stmt2.setString(++count1, comp_cd);
				stmt2.setString(++count1, counterparty_cd);
				stmt2.setString(++count1, agmt_type);
				stmt2.setString(++count1, agmt_no);
				stmt2.setString(++count1, contract_type);
				stmt2.setString(++count1, cont_no);
				stmt2.setString(++count1, cargo_no);
				stmt2.setString(++count1, agmt_rev_no);
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
					
					
					VCARGO_STATUS.add(rset2.getString(2)==null?"":rset2.getString(2));
					
					queryString1 = "SELECT AGMT_BASE,FCC_FLAG,CONT_REF_NO "
							+ "FROM FMS_TRADER_CN_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
							+ "AND CONT_NO=? AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
					stmt1=conn.prepareStatement(queryString1);
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
				}
				rset2.close();
				stmt2.close();
				String cont_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
				String cargo_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
				
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
				VALLOC_REV_NO.add(nom_rev);
				VFCC_FLAG.add(fcc_flg);
				VSTART_DT.add(win_start_dt);
				VEND_DT.add(win_end_dt);
				alloc_status = "Pending";
				VALLOCATION_STATUS.add("Pending");
				VALLOC_STATUS.add("Expected");
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
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void getCnLiabilityDetails() 
	{
		String function_nm="getCnLiabilityDetails()";
		try
		{
			queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK "
					+ "FROM FMS_TRADER_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
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
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getSelectedCnLiabilityDtl() 
	{
		String function_nm="getSelectedCnLiabilityDtl()";
		try
		{
			int liability_count=0;
			queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_TRADER_LIABILITY "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, agmt_type);
			stmt.setString(6, cont_no);
			stmt.setString(7, contract_type);
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
	
	private void getSelectedMspaLiabilityDtl() 
	{
		String function_nm="getSelectedMspaLiabilityDtl()";
		try
		{
			int liability_count=0;
			queryString = "SELECT LIAB_LQ_DAMG,LIAB_TAKE_PAY,LIAB_MAKEUP "
					+ "FROM FMS_TRADER_AGMT_LIABILITY "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
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

	private void getMspaLiabilityDetails() 
	{
		String function_nm="getMspaLiabilityDetails()";
		try
		{
			queryString="SELECT LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
					+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK "
					+ "FROM FMS_TRADER_AGMT_LIABILITY A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? "
					+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_LIABILITY B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
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

	public void getAgmtBaseFormAgmt()
	{
		String function_nm="getAgmtBaseFormAgmt()";
		try
		{
			queryString="SELECT AGMT_BASE "
					+ "FROM FMS_TRADER_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "M");
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
	
	public void getCountryList()
	{
		String function_nm="getCountryList()";
		try
		{
			queryString = "SELECT COUNTRY_CODE,COUNTRY_NM,ISO_CODE "
					+ "FROM FMS_COUNTRY_MST "
					+ "ORDER BY COUNTRY_NM ";
			stmt = conn.prepareStatement(queryString);
			rset= stmt.executeQuery();
			while(rset.next())
			{
				VCOUNTRY_CODE.add(rset.getString(1)==null?"":rset.getString(1));
				VCOUNTRY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCOUNTRY_ISO.add(rset.getString(3)==null?"":rset.getString(3));
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void GetCnCargoDtls() 
	{
		String function_nm="GetCnCargoDtls()";
		try
		{
			queryString="SELECT CARGO_NO,CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TOLERANCE "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO) "
					+ "ORDER BY CARGO_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				cargo_no = rset.getString(1)==null?"":rset.getString(1);
				cargo_ref = rset.getString(2)==null?"":rset.getString(2);
				cargo_status = rset.getString(3)==null?"":rset.getString(3);
				double cargo_volume = rset.getDouble(4);
				cargo_price_unit = rset.getString(6)==null?"":rset.getString(6);
				cargo_start_dt = rset.getString(7)==null?"":rset.getString(7);
				cargo_end_dt = rset.getString(8)==null?"":rset.getString(8);
				String cargo_tolenrance_per = rset.getString(9)==null?"":rset.getString(9);
				
				double temp_cargo_price = rset.getDouble(5);
				cargo_price=""+utilBean.RateNumberFormat(temp_cargo_price, cargo_price_unit);
				
				VCARGO_NO.add(cargo_no);
				VDISP_CARGO_NO.add(""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no));
				VCARGO_REF.add(cargo_ref);
				VCARGO_STATUS_FLG.add(cargo_status);
				if(cargo_status.equals("Y"))
				{
					VCARGO_STATUS.add("Confirmed");
				}
				else if(cargo_status.equals("N"))
				{
					VCARGO_STATUS.add("Not-Confirmed");
				}
				else if(cargo_status.equals("X"))
				{
					VCARGO_STATUS.add("Cancelled");
				}
				else
				{
					VCARGO_STATUS.add("");
				}
				
				VCARGO_QTY.add(nf.format(cargo_volume));
				VCARGO_PRICE.add(cargo_price);
				VCARGO_PRICE_UNIT_FLG.add(cargo_price_unit);
				
				if(cargo_price_unit.equals("1"))
				{
					VCARGO_PRICE_UNIT.add("INR/MMBTU");
				}
				else if(cargo_price_unit.equals("2"))
				{
					VCARGO_PRICE_UNIT.add("USD/MMBTU");
				}
				
				VCARGO_START_DT.add(cargo_start_dt);
				VCARGO_END_DT.add(cargo_end_dt);
				if(!cargo_tolenrance_per.equals(""))
				{
					VCARGO_TOLERANCE_PER.add(nf.format(Double.parseDouble(cargo_tolenrance_per)));
				}
				else
				{
					VCARGO_TOLERANCE_PER.add("");
				}
				int count =0;
				
				queryString1="SELECT CARGO_NO "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CARGO_NO=B.CARGO_NO) "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++count, comp_cd);
				stmt1.setString(++count, counterparty_cd);
				stmt1.setString(++count, agmt_type);
				stmt1.setString(++count, agmt_no);
				stmt1.setString(++count, contract_type);
				stmt1.setString(++count, cont_no);
				stmt1.setString(++count, cargo_no);	
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VIS_CARGO_NOMINATED.add("Y");
				}
				else 
				{
					VIS_CARGO_NOMINATED.add("N");
				}
				rset1.close();
				stmt1.close();
				
				int count1 =0;
				
				queryString2 = "SELECT CARGO_NO "
						+ "FROM FMS_BUY_CARGO_ALLOC "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(++count1, comp_cd);
				stmt2.setString(++count1, counterparty_cd);
				stmt2.setString(++count1, agmt_type);
				stmt2.setString(++count1, agmt_no);
				stmt2.setString(++count1, contract_type);
				stmt2.setString(++count1, cont_no);
				stmt2.setString(++count1, cargo_no);
				rset2=stmt2.executeQuery();
				if(rset2.next()) 
				{
					VIS_CARGO_ALLOCATED.add("Y");
				}
				else 
				{
					VIS_CARGO_ALLOCATED.add("N");
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

	private void getCargoNoDtls() 
	{
		String function_nm="getCargoNoDtls()";
		try
		{
			String cont_map_name=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			queryString="SELECT MAX(CARGO_NO),COUNT(CARGO_NO) "
					+ "FROM FMS_TRADER_CARGO_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CARGO_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO)";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_cargo_dtl=rset.getString(2)==null?"":rset.getString(2);
				if(rset.getInt(1) > 0)
				{
					cargo_number=""+(rset.getInt(1)+1);
					show_cargo_number=""+cont_map_name+"-"+cargo_number;
				}
				else
				{
					cargo_number=""+1;
					show_cargo_number=""+cont_map_name+"-"+cargo_number;
				}
			}
			else
			{
				cargo_number=""+1;
				show_cargo_number=""+cont_map_name+"-"+cargo_number;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}

	public void getCnCountBillingDetail()
	{
		String function_nm="getCnCountBillingDetail()";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, contract_type);
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
	
	public void getCnContractDetail()
	{
		String function_nm="getCnContractDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
					+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY HH:MM'),DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
					+ "DEMURRAGE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,"
					+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,TO_CHAR(REV_DT,'DD/MM/YYYY'),FCC_FLAG "
					+ "FROM FMS_TRADER_CN_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND AGMT_REV=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, agmt_no);
			stmt.setString(7, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				agmt_typ=rset.getString(7)==null?"":rset.getString(7);
				agmt_base=rset.getString(6)==null?"":rset.getString(6);
				agmt_type=rset.getString(3)==null?"":rset.getString(3);
				contract_type=rset.getString(8)==null?"":rset.getString(8);
				cont_no=rset.getString(9)==null?"":rset.getString(9);
				cont_rev_no=rset.getString(10)==null?"":rset.getString(10);
				cont_name=rset.getString(11)==null?"":rset.getString(11);
				cont_ref_no=rset.getString(12)==null?"":rset.getString(12);
				cont_status_flg=rset.getString(13)==null?"":rset.getString(13);
				cont_status=""+ContStatusName(cont_status_flg);
				dda_dt=rset.getString(14)==null?"":rset.getString(14);
				dda_time=rset.getString(15)==null?"":rset.getString(15);
				signing_dt=rset.getString(16)==null?"":rset.getString(16);
				signing_time=rset.getString(17)==null?"":rset.getString(17);
				start_dt=rset.getString(18)==null?"":rset.getString(18);
				end_dt=rset.getString(19)==null?"":rset.getString(19);
				String deal_ent_dt=rset.getString(20)==null?"":rset.getString(20);
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
				day_def_flag=rset.getString(21)==null?"":rset.getString(21);
				day_start_time=rset.getString(22)==null?"":rset.getString(22);
				day_end_time=rset.getString(23)==null?"":rset.getString(23);
				demurrage=rset.getString(24)==null?"":rset.getString(24);
				double temp_demu_rate = rset.getDouble(25);
				demurrage_rate_unit = rset.getString(26)==null?"":rset.getString(26);
				demurrage_rate = ""+utilBean.RateNumberFormat(temp_demu_rate, demurrage_rate_unit);
				alw_laytime_hrs = rset.getString(27)==null?"":rset.getString(27);
				alw_laytime_mns = rset.getString(28)==null?"":rset.getString(28);
				messurment_flag = rset.getString(29)==null?"":rset.getString(29); 
				meas_std =rset.getString(30)==null?"":rset.getString(30);
				meas_temp = rset.getString(31)==null?"":rset.getString(31);
				pressure_min_bar = rset.getString(32)==null?"":rset.getString(32);
				pressure_max_bar = rset.getString(33)==null?"":rset.getString(33);
				off_spec_gas_flag = rset.getString(34)==null?"":rset.getString(34);
				spec_gas_eng_base = rset.getString(35)==null?"":rset.getString(35);
				spec_min_eng = rset.getString(36)==null?"":rset.getString(36);
				spec_max_eng = rset.getString(37)==null?"":rset.getString(37);
				liability_flag = rset.getString(38)==null?"":rset.getString(38);
				liability_clause = rset.getString(39)==null?"":rset.getString(39);
				billing_flag = rset.getString(40)==null?"":rset.getString(40);
				billing_clause = rset.getString(41)==null?"":rset.getString(41);
				termination_flag= rset.getString(42)==null?"":rset.getString(42);
				termination_clause = rset.getString(43)==null?"":rset.getString(43);
				termination_planned = rset.getString(44)==null?"":rset.getString(44);
				termination_forced = rset.getString(45)==null?"":rset.getString(45);
				day_def_clause = rset.getString(46)==null?"":rset.getString(46);
				demurrage_clause = rset.getString(47)==null?"":rset.getString(47);
				measurement_clause = rset.getString(48)==null?"":rset.getString(48);
				spec_gas_clause = rset.getString(49)==null?"":rset.getString(49);
				rev_dt = rset.getString(50)==null?"":rset.getString(50);
				fcc_flg=rset.getString(51)==null?"":rset.getString(51);
				
				String comp_abbr= utilBean.getCompanyAbbr(conn,comp_cd);
				String buyer_abbr = utilBean.getCounterpartyABBR(conn,counterparty_cd);
				cont_disp_name =  utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
				
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCnContList()
	{
		String function_nm="getCnContList()";
		try
		{
			int stcount=0;
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE "
					+ "FROM FMS_TRADER_CN_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString+="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE "
						+ "FROM FMS_TRADER_CN_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
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
			stmt.setString(++stcount, comp_cd);
			stmt.setString(++stcount, contract_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++stcount, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++stcount, to_dt);
				stmt.setString(++stcount, from_dt);
				
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, contract_type);
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
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"0":rset.getString(6);
				String comp_abbr= utilBean.getCompanyAbbr(conn,own_cd);
				String buyer_abbr = utilBean.getCounterpartyABBR(conn,countpty_cd);
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(contno);
				
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				String cont_status_flg=rset.getString(10)==null?"":rset.getString(10);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				
				String cont_ref=rset.getString(11)==null?"":rset.getString(11);
				VCONT_REF_NO.add(cont_ref);
				VAGMT_BASE.add(rset.getString(12)==null?"":rset.getString(12));
				String cont_type = rset.getString(13)==null?"":rset.getString(13);
				String agmt_type = rset.getString(14)==null?"":rset.getString(14);
				cont_disp_name = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev, contno, cont_rev, cont_type, "");
				
				VCONT_DISP_NAME.add(cont_disp_name);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getfccCnContList()
	{
		String function_nm="getfccCnContList()";
		try
		{
			int st_count=0;
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE "
					+ "FROM FMS_TRADER_CN_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND (SELECT COUNT(*) "
					+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX='K' "
					+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
					+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 "
					+ "AND (SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL B "
					+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 "
					+ "AND (SELECT COUNT(*) "
					+ "FROM FMS_TRADER_CARGO_MST B "
					+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV AND B.CONT_REV=A.CONT_REV "
					+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE "
						+ "FROM FMS_TRADER_CN_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND (SELECT COUNT(*) "
						+ "FROM FMS_SECURITY_MST C,FMS_SECURITY_DEAL_MAP B "
						+ "WHERE C.COMPANY_CD=A.COMPANY_CD AND C.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND C.GX='K' "
						+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.SEQ_NO=B.SEQ_NO "
						+ "AND C.SEQ_REV_NO=B.SEQ_REV_NO AND C.GX=B.GX) > 0 "
						+ "AND (SELECT COUNT(*) "
						+ "FROM FMS_TRADER_BILLING_DTL B "
						+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 "
						+ "AND (SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CARGO_MST B "
						+ "WHERE B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV AND B.CONT_REV=A.CONT_REV "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) > 0 ";
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
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, contract_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++st_count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++st_count, to_dt);
				stmt.setString(++st_count, from_dt);
				
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, contract_type);
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
				String cont_rev = rset.getString(6)==null?"0":rset.getString(6);
				String comp_abbr= utilBean.getCompanyAbbr(conn,own_cd);
				String buyer_abbr = utilBean.getCounterpartyABBR(conn,countpty_cd);
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VCONT_NO.add(contno);
				
				VCONT_REV_NO.add(rset.getString(6)==null?"0":rset.getString(6));
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				String cont_status_flg=rset.getString(10)==null?"":rset.getString(10);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				
				String cont_ref=rset.getString(11)==null?"":rset.getString(11);
				VCONT_REF_NO.add(cont_ref);
				VAGMT_BASE.add(rset.getString(12)==null?"":rset.getString(12));
				String cont_type = rset.getString(13)==null?"":rset.getString(13);
				String agmt_type = rset.getString(14)==null?"":rset.getString(14);
				cont_disp_name = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmt_rev, contno, cont_rev, cont_type, "");
				
				VCONT_DISP_NAME.add(cont_disp_name);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getCnDetail() 
	{
		String function_nm="getCnDetail()";
		try
		{
			queryString="SELECT COUNTERPARTY_CD,"
					+ "AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,REMARK,"
					+ "TO_CHAR(REV_DT,'DD/MM/YYYY'),DAY_DEF,DAY_START_TIME,"
					+ "DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,"
					+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DEMURRAGE_CLAUSE,MEAS_CLAUSE,SPEC_CLAUSE,DAY_DEF_CLAUSE "
					+ "FROM FMS_TRADER_AGMT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
	
				counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				agmt_type = rset.getString(2)==null?"":rset.getString(2);
				agmt_no = rset.getString(3)==null?"":rset.getString(3);
				agmt_rev_no = rset.getString(4)==null?"":rset.getString(4); 
				agmt_name = rset.getString(5)==null?"":rset.getString(5);
				agmt_ref_no =  rset.getString(6)==null?"":rset.getString(6);
				agmt_base = rset.getString(7)==null?"":rset.getString(7);
				agmt_typ = rset.getString(8)==null?"":rset.getString(8);
				agmt_signing_dt = rset.getString(9)==null?"":rset.getString(9);
				agmt_start_dt =rset.getString(10)==null?"":rset.getString(10);
				agmt_end_dt =rset.getString(11)==null?"":rset.getString(11);
				status = rset.getString(12)==null?"":rset.getString(12);
				status_nm = ""+AgmtStatusName(status);
				remark = rset.getString(13)==null?"":rset.getString(13);
				rev_dt = rset.getString(14)==null?"":rset.getString(14);
				day_def_flag = rset.getString(15)==null?"":rset.getString(15);
				day_start_time = rset.getString(16)==null?"":rset.getString(16);
				day_end_time = rset.getString(17)==null?"":rset.getString(17);
				
				double temp_demu_rate = rset.getDouble(18);
				demurrage_rate_unit = rset.getString(19)==null?"":rset.getString(19);
				demurrage_rate = ""+utilBean.RateNumberFormat(temp_demu_rate, demurrage_rate_unit);
				alw_laytime_hrs = rset.getString(20)==null?"":rset.getString(20);
				alw_laytime_mns = rset.getString(21)==null?"":rset.getString(21);
				messurment_flag = rset.getString(22)==null?"":rset.getString(22); 
				meas_std =rset.getString(23)==null?"":rset.getString(23);
				meas_temp = rset.getString(24)==null?"":rset.getString(24);
				pressure_min_bar = rset.getString(25)==null?"":rset.getString(25);
				pressure_max_bar = rset.getString(26)==null?"":rset.getString(26);
				off_spec_gas_flag = rset.getString(27)==null?"":rset.getString(27);
				spec_gas_eng_base = rset.getString(28)==null?"":rset.getString(28);
				spec_min_eng = rset.getString(29)==null?"":rset.getString(29);
				spec_max_eng = rset.getString(30)==null?"":rset.getString(30);
				liability_flag = rset.getString(31)==null?"":rset.getString(31);
				liability_clause = rset.getString(32)==null?"":rset.getString(32);
				billing_flag = rset.getString(33)==null?"":rset.getString(33);
				billing_clause = rset.getString(34)==null?"":rset.getString(34);
				termination_flag= rset.getString(35)==null?"":rset.getString(35);
				termination_clause = rset.getString(36)==null?"":rset.getString(36);
				termination_planned = rset.getString(37)==null?"":rset.getString(37);
				termination_forced = rset.getString(38)==null?"":rset.getString(38);
				demurrage = rset.getString(39)==null?"":rset.getString(39);
				demurrage_clause = rset.getString(40)==null?"":rset.getString(40);
				measurement_clause = rset.getString(41)==null?"":rset.getString(41);
				spec_gas_clause = rset.getString(42)==null?"":rset.getString(42);
				day_def_clause = rset.getString(43)==null?"":rset.getString(43);
			}
			rset.close();
			stmt.close();
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
			
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE,"
					+ "(SELECT TO_CHAR(MAX(END_DT),'DD/MM/YYYY') FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO) "
					+ "FROM FMS_TRADER_CN_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "ORDER BY END_DT DESC ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, agreement_type);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, agmt_no);
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
	
	public void getMSPAtraderlist()
	{
		String function_nm="getMSPAtraderlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_TRADER_AGMT_MST "
					+ "WHERE COMPANY_CD=? ";
			if(callFlag.equalsIgnoreCase("CONFIRM_NOTICE_MST"))
			{
				queryString+="AND END_DT >= SYSDATE";	
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cp_cd = rset.getString(1)==null?"":rset.getString(1);
				
				String cpStatus = (String) utilBean.getCounterpartyDetails(conn, cp_cd, dateUtil.getSysdate()).get("status");

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
	
	public void getContractMSPAtraderlist()
	{
		String function_nm="getContractMSPAtraderlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_TRADER_AGMT_MST "
					+ "WHERE COMPANY_CD=? ";
			if(callFlag.equalsIgnoreCase("CONFIRM_NOTICE_MST"))
			{
				queryString+="AND END_DT >= SYSDATE";	
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
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
	
	public void getCNtraderlist()
	{
		String function_nm="getCNtraderlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_TRADER_CN_MST WHERE COMPANY_CD=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getArrivaltraderlist()
	{
		String function_nm="getArrivaltraderlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
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
			//utilBean.getAllEntityCounterpartyList(conn,clearance,comp_cd,"T");
			utilBean.getEffectiveEntityCounterpartyList(conn,clearance, comp_cd, "T");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
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
			if(callFlag.equalsIgnoreCase("CONFIRM_NOTICE_MST"))
			{
				queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_AGMT_PLANT "
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
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
				utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "T", comp_cd);
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
	
	public void getBusinessPlantList()
	{
		String function_nm="getBusinessPlantList()";

		try
		{
			if(callFlag.equalsIgnoreCase("CONFIRM_NOTICE_MST"))
			{
				queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_AGMT_BU "
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
	
	public void getTraderAgreementDetail()
	{
		String function_nm="getTraderAgreementDetail()";

		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			queryString="SELECT COUNTERPARTY_CD,"
					+ "AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,REMARK,"
					+ "TO_CHAR(REV_DT,'DD/MM/YYYY'),DAY_DEF,DAY_START_TIME,"
					+ "DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,"
					+ "MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,"
					+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DEMURRAGE_CLAUSE,MEAS_CLAUSE,SPEC_CLAUSE,DAY_DEF_CLAUSE,TO_CHAR(ENT_DT,'DD/MM/YYYY HH:MM') "
					+ "FROM FMS_TRADER_AGMT_MST "
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
				counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				agmt_type = rset.getString(2)==null?"":rset.getString(2);
				agmt_no = rset.getString(3)==null?"":rset.getString(3);
				agmt_rev_no = rset.getString(4)==null?"":rset.getString(4); 
				agmt_name = rset.getString(5)==null?"":rset.getString(5);
				agmt_ref_no =  rset.getString(6)==null?"":rset.getString(6);
				agmt_base = rset.getString(7)==null?"":rset.getString(7);
				agmt_typ = rset.getString(8)==null?"":rset.getString(8);
				signing_dt = rset.getString(9)==null?"":rset.getString(9);
				start_dt =rset.getString(10)==null?"":rset.getString(10);
				end_dt =rset.getString(11)==null?"":rset.getString(11);
				status = rset.getString(12)==null?"":rset.getString(12);
				status_nm = ""+AgmtStatusName(status);
				remark = rset.getString(13)==null?"":rset.getString(13);
				rev_dt = rset.getString(14)==null?"":rset.getString(14);
				day_def_flag = rset.getString(15)==null?"":rset.getString(15);
				day_start_time = rset.getString(16)==null?"":rset.getString(16);
				day_end_time = rset.getString(17)==null?"":rset.getString(17);
				demurrage_rate = rset.getString(18)==null?"":rset.getString(18);
				demurrage_rate_unit = rset.getString(19)==null?"":rset.getString(19);
				alw_laytime_hrs = rset.getString(20)==null?"":rset.getString(20);
				alw_laytime_mns = rset.getString(21)==null?"":rset.getString(21);
				messurment_flag = rset.getString(22)==null?"":rset.getString(22); 
				meas_std =rset.getString(23)==null?"":rset.getString(23);
				meas_temp = rset.getString(24)==null?"":rset.getString(24);
				pressure_min_bar = rset.getString(25)==null?"":rset.getString(25);
				pressure_max_bar = rset.getString(26)==null?"":rset.getString(26);
				off_spec_gas_flag = rset.getString(27)==null?"":rset.getString(27);
				spec_gas_eng_base = rset.getString(28)==null?"":rset.getString(28);
				spec_min_eng = rset.getString(29)==null?"":rset.getString(29);
				spec_max_eng = rset.getString(30)==null?"":rset.getString(30);
				liability_flag = rset.getString(31)==null?"":rset.getString(31);
				liability_clause = rset.getString(32)==null?"":rset.getString(32);
				billing_flag = rset.getString(33)==null?"":rset.getString(33);
				billing_clause = rset.getString(34)==null?"":rset.getString(34);
				termination_flag= rset.getString(35)==null?"":rset.getString(35);
				termination_clause = rset.getString(36)==null?"":rset.getString(36);
				termination_planned = rset.getString(37)==null?"":rset.getString(37);
				termination_forced = rset.getString(38)==null?"":rset.getString(38);
				demurrage = rset.getString(39)==null?"":rset.getString(39);
				demurrage_clause = rset.getString(40)==null?"":rset.getString(40);
				meas_clause = rset.getString(41)==null?"":rset.getString(41);
				spec_clause = rset.getString(42)==null?"":rset.getString(42);
				day_def_clause = rset.getString(43)==null?"":rset.getString(43);
				String agmt_ent_dt=rset.getString(44)==null?"":rset.getString(44);
				String split[] = agmt_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
			}
			rset.close();
			stmt.close();
			
			//GET ALLOCATED QTY
			//received_qty=""+allocUtil.getPurchaseAllocationQty(comp_cd, counterparty_cd, agmt_no, cont_no, contract_type);
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
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,STATUS,AGMT_REF_NO,"
					+ "AGMT_BASE,AGMT_TYP "
					+ "FROM FMS_TRADER_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, agreement_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(3, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(rset.getString(3)==null?"0":rset.getString(3));
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VSTART_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VEND_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VCONT_NAME.add(rset.getString(7)==null?"":rset.getString(7));
				String cont_status_flg=rset.getString(8)==null?"":rset.getString(8);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+AgmtStatusName(cont_status_flg));
				VCONT_REF_NO.add(rset.getString(9)==null?"":rset.getString(9));
				
				String agmtBase =rset.getString(10)==null?"":rset.getString(10);
				String agmtType =rset.getString(11)==null?"":rset.getString(11);
				
				
				if(agmtBase.equals("X"))
				{
					VAGMT_BASE.add("Ex-Ship");
				}
				else if(agmtBase.equals("D"))
				{
					VAGMT_BASE.add("DES");
				}
				else if(agmtBase.equals("B"))
				{
					VAGMT_BASE.add("Both(Ex-Ship/DES)");
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
	
	public void getMSPAgreement_List()
	{
		String function_nm="getMSPAgreement_List()";
		
		try
		{
			int stcount = 0;
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,AGMT_REF_NO,AGMT_NAME, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYP,STATUS,AGMT_TYPE "
					+ "FROM FMS_TRADER_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_TRADER_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO ) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,AGMT_REF_NO,AGMT_NAME, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,AGMT_TYP,STATUS,AGMT_TYPE "
						+ "FROM FMS_TRADER_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_TRADER_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO ) ";
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
				queryString += "AND END_DT >= SYSDATE AND STATUS NOT IN ('A') AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
			}
			else 
			{
				queryString += "AND END_DT >= SYSDATE AND STATUS IN ('A') ";
			}
			stmt=conn.prepareStatement(queryString);
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
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				String agmt_ref=rset.getString(5)==null?"":rset.getString(5);
				String agmt_name=rset.getString(6)==null?"":rset.getString(6);
				String agmt_base=rset.getString(9)==null?"":rset.getString(9);
				String agmt_type=rset.getString(10)==null?"0":rset.getString(10);
				String status=rset.getString(11)==null?"":rset.getString(11);
				String agmt_typ=rset.getString(12)==null?"":rset.getString(12);
				
				String agmt_full_no = utilBean.NewAgmtMappingId(own_cd, countpty_cd, agmtno, agmt_rev, agmt_typ);
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_FULL_NO.add(agmt_full_no);
				VAGMT_REV_NO.add(rset.getString(4)==null?"0":rset.getString(4));
				VAGMT_REF_NO.add(agmt_ref);
				VAGMT_NAME.add(agmt_name);
				VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VAGMT_BASE_FLG.add(agmt_base);
				if(agmt_base.equals("D"))
				{
					VAGMT_BASE.add("DES");
				}
				else if(agmt_base.equals("X"))
				{
					VAGMT_BASE.add("Ex-Ship");
				}
				else if(agmt_base.equals("B"))
				{
					VAGMT_BASE.add("Ex-Ship/DES");
				}		
								
				if(agmt_type.equals("0"))
				{
					VAGMT_TYP.add("Term");
				}
				else if(agmt_type.equals("1"))
				{
					VAGMT_TYP.add("Spot");
				}
				else
				{
					VAGMT_TYP.add("");
				}
				VAGMT_TYPE_FLG.add(agmt_type);				
				if(status.equals("A"))
				{
					VAGMT_STATUS.add("Active");
				}
				else
				{
					VAGMT_STATUS.add("Inactive");
				}
				VAGMT_TYPE.add(agmt_typ);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedCnTraderPlantList()
	{
		String function_nm="getSelectedCnTraderPlantList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO,SPLIT_VALUE "
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
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_AGMT_PLANT "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agreement_type);
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
	
	public void getSelectedCnBusinessPlantList()
	{
		String function_nm="getSelectedCnBusinessPlantList()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";

		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_TRADER_AGMT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? ";
			stmt=conn.prepareStatement(queryString);
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
	
	
	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";

		try
		{
			queryString="SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
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
	
	public void getCnBillingDetail()
	{
		String function_nm="getCnBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
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
					queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,"
							+ "BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO  "
							+ "FROM FMS_TRADER_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev_no);
					stmt.setString(5, cont_no);
					stmt.setString(6, contract_type);
					stmt.setString(7, ""+VSELECTED_PLANT_SEQ.elementAt(k));
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
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						billing_days=rset.getString(17)==null?"":rset.getString(17);
						
						sat_days=rset.getString(18)==null?"":rset.getString(18);
						plant_seq=rset.getString(19)==null?"":rset.getString(19);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_TRADER_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
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
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
						+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
						+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
						+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, agmt_type);
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
								+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,EXCL_SAT_MAP,PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO )";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_type);
						stmt.setString(5, ""+VSELECTED_PLANT_SEQ.elementAt(k));
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
							
							due_dt_in=rset.getString(15)==null?"":rset.getString(15);
							exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
							sat_days=rset.getString(17)==null?"N":rset.getString(17);
							plant_seq=rset.getString(18)==null?"":rset.getString(18);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
							
							String state_map="";
							String state_nm = "";
							String queryString2="SELECT HOLIDAY_STATE "
									+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
									+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
									+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, agmt_no);
							stmt2.setString(4, agmt_type);
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
											+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
										plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
										plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
									+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
								
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
								plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
	
	public void getBillingDetail()
	{
		String function_nm="getBillingDetail()";

		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
					+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, agmt_no);
			stmt3.setString(4, agmt_type);
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
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
							+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO )";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_type);
					stmt.setString(5, ""+VSELECTED_PLANT_SEQ.elementAt(k));
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
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						sat_days=rset.getString(17)==null?"N":rset.getString(17);
						plant_seq=rset.getString(18)==null?"":rset.getString(18);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B WHERE "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_type);
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
										+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
									plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
								+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
							
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
							plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
							+ "WHERE B.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.ENTITY='T' "
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
						
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
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
		//Added by Pratham Bhatt for optimization 
		try
		{
			queryString2="SELECT  A.PLANT_SEQ_NO, B.PLANT_SEQ_NO  "
					+ "FROM FMS_TRADER_AGMT_BU A, FMS_TRADER_AGMT_PLANT B "
					+ "WHERE A.COMPANY_CD=? AND B.COMPANY_CD=?  AND A.COUNTERPARTY_CD=? AND B.COUNTERPARTY_CD=? "
					+ "AND A.AGMT_NO=? AND B.AGMT_NO=? AND A.AGMT_REV=? AND B.AGMT_REV=? "
					+ "AND A.AGMT_TYPE=? AND B.AGMT_TYPE=?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, counterparty_cd);
			stmt2.setString(4, counterparty_cd);
			stmt2.setString(5, agmt_no);
			stmt2.setString(6, agmt_no);
			stmt2.setString(7, agmt_rev_no);
			stmt2.setString(8, agmt_rev_no);
			stmt2.setString(9, agmt_type);
			stmt2.setString(10, agmt_type);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
				queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE "
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
				stmt1.setString(6, cont_start_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
					VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
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
	
	public void getCnApplicableTaxes()
	{
		String function_nm="getCnApplicableTaxes()";

		try
		{
			//Added by Pratham Bhatt for optimization 
			queryString2="SELECT A.PLANT_SEQ_NO, B.PLANT_SEQ_NO  "
					+ "FROM FMS_TRADER_CONT_BU  A, FMS_TRADER_CONT_PLANT B "
					+ "WHERE A.COMPANY_CD=? AND B.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND B.COUNTERPARTY_CD=?  "
					+ "AND A.CONT_NO=? AND B.CONT_NO=?  "
					+ "AND A.CONT_REV=? AND B.CONT_REV=? AND A.AGMT_NO=? AND B.AGMT_NO=?  "
					+ "AND A.AGMT_REV=? AND B.AGMT_REV=? "
					+ "AND A.CONTRACT_TYPE=? AND A.CONTRACT_TYPE=? ";
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
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
				String plant_seq = rset2.getString(2)==null?"":rset2.getString(2);
				queryString1="SELECT A.TAX_STRUCT_CD,A.TAX_STRUCT_DTL,B.SAP_TAX_CODE "
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
				stmt1.setString(6, cont_start_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VTAX_STRUCT_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_STRUCT_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_SAP_CODE.add(rset1.getString(3)==null?"":rset1.getString(3));
					VPLANT_NAME.add(""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
					VBU_PLANT_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
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
	
	
	public void getCountBillingDetail()
	{
		String function_nm="getCountBillingDetail()";

		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_AGMT_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ " AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_type);
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
			queryString="SELECT COUNT(*) "
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
			
			queryString1 = "SELECT NEW_PRICE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),FLAG "
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
	
	public void getCargoList()
	{
		String function_nm="getCargoList()";
		try
		{
			int str_cnt=0;
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,"
					+ "B.CARGO_NO,B.CARGO_REF,B.CARGO_STATUS,B.CARGO_QTY,B.RATE,B.RATE_UNIT,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.CONT_REF_NO,B.TOLERANCE "
					+ "FROM FMS_TRADER_CN_MST A,FMS_TRADER_CARGO_MST B "
					+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND A.FCC_FLAG=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV ";
			
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			if(!cargo_number.equals(""))
			{
				queryString+="AND B.CARGO_NO=? ";
			}
			if(!confirm_no.equals(""))
			{
				queryString+="AND B.CONT_NO=? ";
			}
			if(!contract_type.equals(""))
			{
				queryString+="AND B.CONTRACT_TYPE=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++str_cnt, comp_cd);
			stmt.setString(++str_cnt, "Y");
			stmt.setString(++str_cnt, "Y");

			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++str_cnt, counterparty_cd);
			}
			if(!cargo_number.equals(""))
			{
				stmt.setString(++str_cnt, cargo_number);
			}
			if(!confirm_no.equals(""))
			{
				stmt.setString(++str_cnt, confirm_no);
			}
			if(!contract_type.equals(""))
			{
				stmt.setString(++str_cnt, contract_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd =(rset.getString(2)==null?"":rset.getString(2));
				agmt_type=(rset.getString(3)==null?"":rset.getString(3));
				agmt_no=(rset.getString(4)==null?"":rset.getString(4));
				
				contract_type=(rset.getString(6)==null?"":rset.getString(6));
				cont_no=(rset.getString(7)==null?"":rset.getString(7));
				
				String cargo_no=(rset.getString(9)==null?"":rset.getString(9));				
				cargo_ref=(rset.getString(10)==null?"":rset.getString(10));
				String cargo_status=(rset.getString(11)==null?"":rset.getString(11));
				mandate_conf_vol=(rset.getString(12)==null?"":rset.getString(12));
				conf_price = (rset.getString(13)==null?"":rset.getString(13));
				rate_unit=(rset.getString(14)==null?"":rset.getString(14));
				win_start_dt =(rset.getString(15)==null?"":rset.getString(15));
				win_end_dt = rset.getString(16)==null?"":rset.getString(16);
				agmt_base=(rset.getString(17)==null?"":rset.getString(17));
				String cn_ref = rset.getString(18)==null?"":rset.getString(18);
				mandate_conf_vol_tol=(rset.getString(19)==null?"":rset.getString(19));
				
				String cont_name = utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, "", cont_no, "", contract_type, "");
				String cargo_name = utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);

				VCONTPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterpty_cd));
				VAGMT_TYPE.add(agmt_type);
				VAGMT_NO.add(agmt_no);
				VCONTRACT_TYPE.add(contract_type);
				VCONT_NO.add(cont_no);
				VCARGO_NO.add(cargo_no);
				VDISP_CARGO_NO.add(cargo_name);					
				VCONT_DISP_NAME.add(cont_name);
				VCARGO_REF.add(cargo_ref);
				VCARGO_STATUS.add(cargo_status);
				VCARGO_QTY.add(utilBean.RateNumberFormat(Double.parseDouble(mandate_conf_vol),"1"));
				VRATE.add(utilBean.RateNumberFormat(Double.parseDouble(conf_price), rate_unit));
				VRATE_UNIT.add(rate_unit);
				VSTART_DT.add(win_start_dt);
				VEND_DT.add(win_end_dt);
				
				VAGMT_BASE.add(agmt_base);
				
				if(agmt_base.equals("D"))
				{
					VAGMT_BASE_NM.add("DES");
				}
				else if(agmt_base.equals("X"))
				{
					VAGMT_BASE_NM.add("Ex-Ship");
				}
				else if(agmt_base.equals("B"))
				{
					VAGMT_BASE_NM.add("Ex-Ship/DES");
				}	
				VCONT_REF_NO.add(cn_ref);
				
				//Decide Nomination status Based on entry available @FMS_BUY_CARGO_NOM
				int str_cnt2=0;
				queryString2="SELECT COMPANY_CD,"
						+ "COUNTERPARTY_CD,"
						+ "AGMT_TYPE,"
						+ "AGMT_NO,"
						+ "CONTRACT_TYPE,"
						+ "CONT_NO,"
						+ "CARGO_NO,"
						+ "SHIP_CD,"
						+ "NOM_REV,"
						+ "NUM_BL, NUM_BOE "
						+ "FROM FMS_BUY_CARGO_NOM A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CARGO_NO=B.CARGO_NO) "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(++str_cnt2, comp_cd);
				stmt2.setString(++str_cnt2, counterpty_cd);
				stmt2.setString(++str_cnt2, agmt_type);
				stmt2.setString(++str_cnt2, agmt_no);
				stmt2.setString(++str_cnt2, contract_type);
				stmt2.setString(++str_cnt2, cont_no);
				stmt2.setString(++str_cnt2, cargo_no);				
				 
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					String ship_cd =(rset2.getString(8)==null?"":rset2.getString(8));
					VNOMINATION_STATUS.add("Nominated");
					
					nom_status="Nominated";
					nom_revision=rset2.getString(9)==null?"":rset2.getString(9);
					
					String bl_num=(rset2.getString(10)==null?"":rset2.getString(10));
					String boe_num=(rset2.getString(11)==null?"":rset2.getString(11));
					
					VTOTAL_BL_NO.add(bl_num);
					VTOTAL_BOE_NO.add(boe_num);
					
					VNOMINATION_REV_NO.add(nom_revision);
					
					//Get Ship info 
					queryString3="SELECT SHIP_NAME "
							+ "FROM FMS_SHIP_MST A "
							+ "WHERE SHIP_CD=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) ";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, ship_cd);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						String ship_name =(rset3.getString(1)==null?"":rset3.getString(1));						 
						VSHIP_CD.add(ship_cd);
						VSHIP_NAME.add(ship_name);
					}
					else 
					{
						VSHIP_CD.add("");
						VSHIP_NAME.add("");
					}	
					rset3.close();
					stmt3.close();
				} 
				else
				{
					VNOMINATION_STATUS.add("Pending");
					nom_status="Pending";
					VSHIP_CD.add("");
					VSHIP_NAME.add("");
					VNOMINATION_REV_NO.add("");
					VTOTAL_BL_NO.add("");
					VTOTAL_BOE_NO.add("");
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
		
	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			
			int count=0;
			
			queryString="SELECT COMPANY_CD,"
					+ "COUNTERPARTY_CD,"
					+ "AGMT_TYPE,"
					+ "AGMT_NO,"
					+ "AGMT_REV,"
					+ "AGMT_BASE,"
					+ "AGMT_TYP,"
					+ "CONTRACT_TYPE,"
					+ "CONT_NO,"
					+ "CONT_REV,"
					+ "CONT_NAME,"
					+ "CONT_REF_NO,"
					+ "CONT_STATUS,"
					+ "DEMURRAGE_RATE,"
					+ "DEMURRAGE_RATE_UNIT,"
					+ "ALW_LAYTIME_HRS,"
					+ "ALW_LAYTIME_MNS "
					+ "FROM FMS_TRADER_CN_MST "
					+ "WHERE COMPANY_CD=? AND CONT_NO=? AND COUNTERPARTY_CD=? AND AGMT_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, cont_no);
			stmt.setString(++count, counterparty_cd);
			stmt.setString(++count, agmt_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd =(rset.getString(2)==null?"":rset.getString(2));
				agmt_type=(rset.getString(3)==null?"":rset.getString(3));
				agmt_no=(rset.getString(4)==null?"":rset.getString(4));
				agmt_rev_no=(rset.getString(5)==null?"":rset.getString(5));
				agmt_base=(rset.getString(6)==null?"":rset.getString(6));
				agmt_typ=(rset.getString(7)==null?"":rset.getString(7));
				contract_type=(rset.getString(8)==null?"":rset.getString(8));
				cont_no=(rset.getString(9)==null?"":rset.getString(9));
				cont_rev_no=(rset.getString(10)==null?"":rset.getString(10));
				//cont_name = (rset.getString(11)==null?"":rset.getString(11));
				cont_ref_no=(rset.getString(12)==null?"":rset.getString(12));
				cont_status =(rset.getString(13)==null?"":rset.getString(13));
				demu_rate =(rset.getString(14)==null?"":rset.getString(14));
				demu_rate_unit =(rset.getString(15)==null?"":rset.getString(15));
				allowed_lay_time_hrs =(rset.getString(16)==null?"":rset.getString(16));
				allowed_lay_time_min =(rset.getString(17)==null?"":rset.getString(17));
				cont_name = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getShipMst()
	{
		String function_nm="getShipMst()";
		try
		{
			int count =0;
			
			queryString="SELECT SHIP_CD,SHIP_NAME,SHIP_CALL_SIGN,SHIP_FLAG,SHIP_IMO_NO,SHIP_CLASS_SOC,INMARSAT_NO,"
					+ "SHIP_OWNER_NAME,SHIP_OPERATOR_NAME,SHIP_FAX_NO,SHIP_TELEX_NO,SHIP_EMAIL,GROSS_TONNAGE,"
					+ "CARGO_CAPACITY,VOLUME_UNIT,PERCENTAGE_CAPACITY,SHIP_ITEM,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(MODIFY_DT,'DD/MM/YYYY'),MODIFY_BY "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) ";
			if(!ship_cd.equals(""))
			{
				queryString+= "AND SHIP_CD=? ";
			}
			queryString+= "ORDER BY SHIP_CD DESC ";
			stmt=conn.prepareStatement(queryString);
			if(!ship_cd.equals(""))
			{
				stmt.setString(++count, ship_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				ship_cd =(rset.getString(1)==null?"":rset.getString(1));
				ship_name =(rset.getString(2)==null?"":rset.getString(2));
				ship_call_sign=(rset.getString(3)==null?"":rset.getString(3));
				ship_flag=(rset.getString(4)==null?"":rset.getString(4));
				ship_imo_no=(rset.getString(5)==null?"":rset.getString(5));
				ship_class_soc=(rset.getString(6)==null?"":rset.getString(6));
				inmarsat_no=(rset.getString(7)==null?"":rset.getString(7));
				ship_owner_name=(rset.getString(8)==null?"":rset.getString(8));
				ship_operator_name=(rset.getString(9)==null?"":rset.getString(9));
				ship_fax_no=(rset.getString(10)==null?"":rset.getString(10));
				ship_telex_no=(rset.getString(11)==null?"":rset.getString(11));
				ship_email=(rset.getString(12)==null?"":rset.getString(12));
				gross_tonnage=(rset.getString(13)==null?"":rset.getString(13));
				cargo_capacity=(rset.getString(14)==null?"":rset.getString(14));
				volume_unit=(rset.getString(15)==null?"":rset.getString(15));
				percentage_capacity=(rset.getString(16)==null?"":rset.getString(16));
				ship_item=(rset.getString(17)==null?"":rset.getString(17));
				 
				VSHIP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSHIP_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VSHIP_CALL_SIGN.add(rset.getString(3)==null?"":rset.getString(3));
				VSHIP_FLAG.add(rset.getString(4)==null?"":rset.getString(4));
				VSHIP_IMO_NO.add(rset.getString(5)==null?"":rset.getString(5));
				VSHIP_CLASS_SOC.add(rset.getString(6)==null?"":rset.getString(6));
				VINMARSAT_NO.add(rset.getString(7)==null?"":rset.getString(7));
				VSHIP_OWNER_NAME.add(rset.getString(8)==null?"":rset.getString(8));
				VSHIP_OPERATOR_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VSHIP_FAX_NO.add(rset.getString(10)==null?"":rset.getString(10));
				VSHIP_TELEX_NO.add(rset.getString(11)==null?"":rset.getString(11));
				VSHIP_EMAIL.add(rset.getString(12)==null?"":rset.getString(12));
				VGROSS_TONNAGE.add(rset.getString(13)==null?"":rset.getString(13));
				VCARGO_CAPACITY.add(rset.getString(14)==null?"":rset.getString(14));
				VVOLUME_UNIT.add(rset.getString(15)==null?"":rset.getString(15));
				VPERCENTAGE_CAPACITY.add(rset.getString(16)==null?"":rset.getString(16));
				VSHIP_ITEM.add(rset.getString(17)==null?"":rset.getString(17));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoNominationDetails()
	{
		String function_nm="getCargoNominationDetails()";
		try
		{
			queryString="SELECT COUNTRY_ORIGIN,LOAD_PORT,REMARK,NUM_BL,NUM_BOE,"
					+ "LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,LIQUEFAC_REMARK,SPLIT_BOL,"
					+ "TO_CHAR(EXP_FROM_DT,'DD/MM/YYYY'),TO_CHAR(EXP_TO_DT,'DD/MM/YYYY'),"
					+ "LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT "
					+ "FROM FMS_BUY_CARGO_NOM A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?  "
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				country_origin = rset.getString(1)==null?"0":rset.getString(1);
				load_port = rset.getString(2)==null?"":rset.getString(2);
				remark = rset.getString(3)==null?"":rset.getString(3);
				num_bl = rset.getString(4)==null?"0":rset.getString(4);
				num_boe = rset.getString(5)==null?"0":rset.getString(5);
				liquefac_plant = rset.getString(6)==null?"":rset.getString(6);
				liquefac_country = rset.getString(7)==null?"":rset.getString(7);
				liquefac_promotor = rset.getString(8)==null?"":rset.getString(8);
				liquefac_remark = rset.getString(9)==null?"":rset.getString(9);
				split_bol = rset.getString(10)==null?"":rset.getString(10);
				man_req_from_dt = rset.getString(11)==null?"":rset.getString(11);
				man_req_to_dt = rset.getString(12)==null?"":rset.getString(12);
				
				surveyor_cont_dtl = rset.getString(13)==null?"":rset.getString(13);
				cha_cont_dtl = rset.getString(14)==null?"":rset.getString(14);
				vessel_cont_dtl = rset.getString(15)==null?"":rset.getString(15);
				
				//String cont_disp_no=svc_comp_cd+svc_contract_type+svc_counterparty_cd+"-"+svc_cont_no;
				String sur_comp_cd = "";
				String sur_contract_type = "";
				String sur_counterparty_cd = "";
				String sur_cont_no = "";
				
				String cha_comp_cd = "";
				String cha_contract_type = "";
				String cha_counterparty_cd = "";
				String cha_cont_no = "";
				
				String vessel_comp_cd = "";
				String vessel_contract_type = "";
				String vessel_counterparty_cd = "";
				String vessel_cont_no = "";
				
				if(!surveyor_cont_dtl.equals("")) 
				{
					String[] sur_parts = surveyor_cont_dtl.split("-");
					
			        sur_comp_cd = comp_cd;
			        sur_counterparty_cd = sur_parts[0];
			        sur_contract_type = sur_parts[1];
			        sur_cont_no = sur_parts[2];
			        
				}
				
				if(!cha_cont_dtl.equals("")) 
				{
					String[] cha_parts = cha_cont_dtl.split("-");

				    cha_comp_cd = comp_cd;
				    cha_counterparty_cd = cha_parts[0];
				    cha_contract_type = cha_parts[1];
				    cha_cont_no = cha_parts[2];
				}
				
				if(!vessel_cont_dtl.equals("")) 
				{
					String[] vessel_parts = vessel_cont_dtl.split("-");
				    
				    vessel_comp_cd = comp_cd;
				    vessel_counterparty_cd = vessel_parts[0];
				    vessel_contract_type = vessel_parts[1];
				    vessel_cont_no = vessel_parts[2];
				    
				}
		      
				disp_surveyor_cont_dtl = getDispalySvcContName(sur_comp_cd,sur_counterparty_cd,"S",sur_cont_no,"Y",surveyor_cont_dtl);
				disp_cha_cont_dtl = getDispalySvcContName(cha_comp_cd,cha_counterparty_cd,"H",cha_cont_no,"H",cha_cont_dtl);
				disp_vessel_cont_dtl = getDispalySvcContName(vessel_comp_cd,vessel_counterparty_cd,"V",vessel_cont_no,"A",vessel_cont_dtl);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getDispalySvcContName(String comp_cd, String counterparty_cd, String entiy_role, String cont_no, String cont_type,
			String cont_full_no)
	{
		String function_nm="getDispalySvcContName()";
		
		String dispContName="";
		
		try 
		{
			int count=0;
			String cont_mapping="";
			queryString1="SELECT CONT_REF_NO "
					+ "FROM FMS_CARGO_SVC_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(++count, comp_cd);
			stmt1.setString(++count, counterparty_cd);
			stmt1.setString(++count, entiy_role);
			stmt1.setString(++count, cont_no);
			stmt1.setString(++count, cont_type);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
				String counterparty_nm = utilBean.getCounterpartyName(conn,counterparty_cd);
				
				//cont_full_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", cont_type, "");
				//dispContName = counterparty_nm+"-"+cont_full_no+" ("+cont_ref_no+")";
				cont_mapping=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", cont_no, "", cont_type, "");
				dispContName = counterparty_nm+"-"+cont_mapping+" ("+cont_ref_no+")";
			}
			rset1.close();
			stmt1.close();
		} 
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
        return dispContName;
    }
	
	
	public void getCargoBLDetails()
	{
		String function_nm="getCargoBLDetails()";
		try
		{
			int blNumber=0;
			
			//Focus on Nomination Rev only : No need to check Agreement/Contract Revision
			
			queryString="SELECT BL_NO,BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,BL_QTY_MT,BL_QTY_SCM "
					+ "FROM FMS_BUY_CARGO_NOM_BL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY BL_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				blNumber++;
				String bl_number = "BL"+(utilBean.PrePaddingZero(rset.getString(1)==null?"":rset.getString(1),2));
				
				VBL_NO.add(bl_number);
				double bl_qty = rset.getDouble(2);
				double bl_mt = rset.getDouble(6);
				double bl_scm = rset.getDouble(7);
				VBL_QTY.add(utilBean.RateNumberFormat(bl_qty,"1"));
				VBL_QTY_UNIT.add(rset.getString(3)==null?"":rset.getString(3));
				
				double rate = rset.getDouble(4);
				String rate_unit = rset.getString(5)==null?"":rset.getString(5);
				VBL_PRICE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VBL_PRICE_UNIT.add(rate_unit);
				
				VBL_QTY_MT.add(rset.getString(6)==null?"":rset.getString(6));
				VBL_QTY_SCM.add(rset.getString(7)==null?"":rset.getString(7));
				
				num_bl=""+blNumber;
				
				totalBlMMBTU += bl_qty;
				totalBlMT += bl_mt;
				totalBlSCM += bl_scm;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllocatedCargoBLDetails()
	{
		String function_nm="getAllocatedCargoBLDetails()";
		try
		{
			int blNumber=0;
			queryString="SELECT BL_NO,BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,NOM_REV,BL_QTY_MT,BL_QTY_SCM "
					+ "FROM FMS_BUY_CARGO_NOM_BL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY BL_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				blNumber++;
				
				String bl_no =rset.getString(1)==null?"":rset.getString(1);
				String disp_bl_number = "BL"+(utilBean.PrePaddingZero(bl_no,2));
				VBL_NO.add(bl_no);
				VDISP_BL_NO.add(disp_bl_number);
				double bl_qty = rset.getDouble(2);
				VBL_QTY.add(utilBean.RateNumberFormat(bl_qty,"1"));
				VBL_QTY_UNIT.add(rset.getString(3)==null?"":rset.getString(3));
				
				double bl_exp_qty_mt = rset.getDouble(7);
				double bl_exp_qty_scm = rset.getDouble(8);
				
				if(Double.doubleToRawLongBits(bl_exp_qty_mt)!=Double.doubleToRawLongBits(0))
				{
					VBL_EXP_MT.add(utilBean.RateNumberFormat(bl_exp_qty_mt,"1"));
				}
				else
				{
					VBL_EXP_MT.add("");
				}
				
				if(Double.doubleToRawLongBits(bl_exp_qty_scm)!=Double.doubleToRawLongBits(0))
				{
					VBL_EXP_SCM.add(utilBean.RateNumberFormat(bl_exp_qty_scm,"1"));
				}
				else
				{
					VBL_EXP_SCM.add("");
				}
				
				double rate = rset.getDouble(4);
				String rate_unit = rset.getString(5)==null?"":rset.getString(5);
				VBL_PRICE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VBL_PRICE_UNIT.add(rate_unit);
				VNOMINATION_REV_NO.add(rset.getString(6)==null?"":rset.getString(6));
				
				
				// Check if allocation BL already exist for this
				queryString1="SELECT BL_NO,BL_REF,TO_CHAR(BL_DT,'DD/MM/YYYY'),IMPORT_DEPT_SNO,IMPORT_CD,TO_CHAR(ENDORSE_DT,'DD/MM/YYYY'),REMARK "
						+ "FROM FMS_BUY_CARGO_ALLOC_BL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND BL_NO=? "
						+ "AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BL B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) "
						+ "ORDER BY BL_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_type);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, cargo_number);
				stmt1.setString(8, bl_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					
					VBL_REF.add(rset1.getString(2)==null?"":rset1.getString(2));
					VBL_DT.add(rset1.getString(3)==null?"":rset1.getString(3));
					VBL_IMPORT_DEPT_SNO.add(rset1.getString(4)==null?"":rset1.getString(4));
					VBL_IMPORT_CD.add(rset1.getString(5)==null?"":rset1.getString(5));
					VBL_ENDORSE_DT.add(rset1.getString(6)==null?"":rset1.getString(6));
					VBL_REMARK.add(rset1.getString(7)==null?"":rset1.getString(7));
				}
				else
				{
					VBL_REF.add("");
					VBL_DT.add("");
					VBL_IMPORT_DEPT_SNO.add("");
					VBL_IMPORT_CD.add("");
					VBL_ENDORSE_DT.add("");
					VBL_REMARK.add("");
				}
				rset1.close();
				stmt1.close();
				
				num_bl=""+blNumber;
				
				totalBlMMBTU += bl_qty;
				totalBlMT += bl_exp_qty_mt;
				totalBlSCM += bl_exp_qty_scm;
			}
			rset.close();
			stmt.close();
			
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoBOEDetails()
	{
		String function_nm="getCargoBOEDetails()";
		try
		{
			int boeNumber=0;
			
			//Focus on Nomination Rev only : No need to check Agreement/Contract Revision
			
			queryString="SELECT BOE_NO,BU_SEQ,PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM "
					+ "FROM FMS_BUY_CARGO_NOM_BOE A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY BOE_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				boeNumber++;
				
				String boe_number = "BOE"+(utilBean.PrePaddingZero(rset.getString(1)==null?"":rset.getString(1),2));
				VBOE_NO.add(boe_number);
				
				VBOE_BU_SEQ.add(rset.getString(2)==null?"":rset.getString(2));
				String bu_seq = rset.getString(2)==null?"":rset.getString(2);
				VBOE_BU_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
				
				VBOE_PLANT_SEQ.add(rset.getString(3)==null?"":rset.getString(3));
				String plant_seq = rset.getString(3)==null?"":rset.getString(3);
				VBOE_PLANT_NM.add(utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "T"));
				VBOE_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
				
				double boe_qty = rset.getDouble(4);
				double boe_mt = rset.getDouble(11);
				double boe_scm = rset.getDouble(12);
				VBOE_QTY.add(utilBean.RateNumberFormat(boe_qty,"1"));
				
				VBOE_QTY_UNIT.add(rset.getString(5)==null?"":rset.getString(5));
				
				double rate = rset.getDouble(6);
				String rate_unit = rset.getString(7)==null?"":rset.getString(7);
				VBOE_PRICE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VBOE_PRICE_UNIT.add(rate_unit);
				
				VBOE_CUSTOM_DUTY.add(rset.getString(8)==null?"":rset.getString(8));
				VBOE_LOAD_PORT.add(rset.getString(9)==null?"":rset.getString(9));
				
				VBOE_LINKED_BL.add(rset.getString(10)==null?"":rset.getString(10));
				
				VBOE_QTY_MT.add(rset.getString(11)==null?"":rset.getString(11));
				VBOE_QTY_SCM.add(rset.getString(12)==null?"":rset.getString(12));
				
				num_boe=""+boeNumber;
				
				totalBOEMMBTU += boe_qty;
				totalBOEMT += boe_mt;
				totalBOESCM += boe_scm;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllocatedCargoBOEDetails()
	{
		String function_nm="getAllocatedCargoBOEDetails()";
		try
		{
			int boeNumber=0;
			queryString="SELECT BOE_NO,BU_SEQ,PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,NOM_REV,CUSTOM_DUTY,LOAD_PORT,BOE_QTY_MT,BOE_QTY_SCM,LINKED_BL  "
					+ "FROM FMS_BUY_CARGO_NOM_BOE A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY BOE_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				boeNumber++;
				
				String boe_no =rset.getString(1)==null?"":rset.getString(1);
				String disp_boe_number = "BOE"+(utilBean.PrePaddingZero(boe_no,2));
				VBOE_NO.add(boe_no);
				VDISP_BOE_NO.add(disp_boe_number);
				VBOE_BU_SEQ.add(rset.getString(2)==null?"":rset.getString(2));
				String bu_seq = rset.getString(2)==null?"":rset.getString(2);
				VBOE_BU_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
				VBOE_PLANT_SEQ.add(rset.getString(3)==null?"":rset.getString(3));
				String plant_seq = rset.getString(3)==null?"":rset.getString(3);
				VBOE_PLANT_NM.add(utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "T"));
				VBOE_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T"));
				double boe_qty = rset.getDouble(4);
				VBOE_QTY.add(utilBean.RateNumberFormat(boe_qty,"1"));
				VBOE_QTY_UNIT.add(rset.getString(5)==null?"":rset.getString(5));
				double rate = rset.getDouble(6);
				String rate_unit = rset.getString(7)==null?"":rset.getString(7);
				VBOE_PRICE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VBOE_PRICE_UNIT.add(rate_unit);
				VNOMINATION_REV_NO.add(rset.getString(8)==null?"":rset.getString(8));
				double boe_mt = rset.getDouble(11);
				if(Double.doubleToRawLongBits(boe_mt)!=Double.doubleToRawLongBits(0))
				{
					VBOE_QTY_MT.add(utilBean.RateNumberFormat(boe_mt,"1"));
				}
				else
				{
					VBOE_QTY_MT.add("");
				}
				double boe_scm = rset.getDouble(12);
				if(Double.doubleToRawLongBits(boe_scm)!=Double.doubleToRawLongBits(0))
				{
					VBOE_QTY_SCM.add(utilBean.RateNumberFormat(boe_scm,"1"));
				}
				else
				{
					VBOE_QTY_SCM.add("");
				}
				String tmp_linked_bl = rset.getString(13)==null?"":rset.getString(13);
				String linked_bl = tmp_linked_bl.replace("@",", BL");
				if(!linked_bl.equals(""))
				{
					VLINKED_BL.add("BL"+linked_bl);
				}
				else
				{
					VLINKED_BL.add("");
				}
				
				
				double boe_act_qty=0;
				double boe_act_qty_mt=0;
				double boe_act_qty_scm=0;
				queryString1="SELECT BOE_NO,BU_SEQ,PLANT_SEQ,BOE_REF,TO_CHAR(BOE_DT,'DD/MM/YYYY'),"
						+ "ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,ACT_QTY_SCM,CUSTOM_DUTY,LOAD_PORT,"
						+ "BOE_PROVISIONAL_PRICE,BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,BOE_FINAL_PRICE_UNIT "
						+ "FROM FMS_BUY_CARGO_ALLOC_BOE A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND BOE_NO=? "
						+ "AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) "
						+ "ORDER BY BOE_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_type);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, cargo_number);
				stmt1.setString(8, boe_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VBOE_REF.add(rset1.getString(4)==null?"":rset1.getString(4));
					VBOE_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
					
					boe_act_qty = rset1.getDouble(6);
					if(Double.doubleToRawLongBits(boe_act_qty)!=Double.doubleToRawLongBits(0))
					{
						VBOE_ACT_QTY.add(utilBean.RateNumberFormat(boe_act_qty,"1"));
					}
					else
					{
						VBOE_ACT_QTY.add("");
					}
					
					VBOE_ACT_QTY_UNIT.add(rset1.getString(7)==null?"":rset1.getString(7));
					
					boe_act_qty_mt = rset1.getDouble(8);
					if(Double.doubleToRawLongBits(boe_act_qty_mt)!=Double.doubleToRawLongBits(0))
					{
						VBOE_ACT_QTY_MT.add(utilBean.RateNumberFormat(boe_act_qty_mt,"1"));
					}
					else
					{
						VBOE_ACT_QTY_MT.add("");
					}
					
					boe_act_qty_scm = rset1.getDouble(9);
					if(Double.doubleToRawLongBits(boe_act_qty_scm)!=Double.doubleToRawLongBits(0))
					{
						VBOE_ACT_QTY_SCM.add(utilBean.RateNumberFormat(boe_act_qty_scm,"1"));
					}
					else
					{
						VBOE_ACT_QTY_SCM.add("");
					}
					VBOE_CUSTOM_DUTY.add(rset1.getString(10)==null?"":rset1.getString(10));
					VBOE_LOAD_PORT.add(rset1.getString(11)==null?"":rset1.getString(11));
					
					double provisional_rate = rset1.getDouble(12);
					String provisional_rate_unit = rset1.getString(13)==null?"":rset1.getString(13);
					
					if(Double.doubleToRawLongBits(provisional_rate)!=Double.doubleToRawLongBits(0))
					{
						VBOE_PROVISIONAL_PRICE.add(utilBean.RateNumberFormat(provisional_rate, provisional_rate_unit));
					}
					else
					{
						VBOE_PROVISIONAL_PRICE.add("");
					}
					
					VBOE_PROVISIONAL_PRICE_UNIT.add(provisional_rate_unit);
					
					double final_rate = rset1.getDouble(14);
					String final_rate_unit = rset1.getString(15)==null?"":rset1.getString(15);
					
					if(Double.doubleToRawLongBits(final_rate)!=Double.doubleToRawLongBits(0))
					{
						VBOE_FINAL_PRICE.add(utilBean.RateNumberFormat(final_rate, final_rate_unit));
					}
					else
					{
						VBOE_FINAL_PRICE.add("");
					}
					
					VBOE_FINAL_PRICE_UNIT.add(final_rate_unit);
				}
				else
				{
					VBOE_REF.add("");
					VBOE_DT.add("");
					VBOE_ACT_QTY.add("");
					VBOE_ACT_QTY_UNIT.add("");
					VBOE_ACT_QTY_MT.add("");
					VBOE_ACT_QTY_SCM.add("");
					VBOE_PROVISIONAL_PRICE.add("");
					VBOE_PROVISIONAL_PRICE_UNIT.add("");
					VBOE_FINAL_PRICE.add("");
					VBOE_FINAL_PRICE_UNIT.add("");
					
					
					VBOE_CUSTOM_DUTY.add(rset.getString(9)==null?"":rset.getString(9));
					VBOE_LOAD_PORT.add(rset.getString(10)==null?"":rset.getString(10));
					
					boe_act_qty=0;
					boe_act_qty_mt=0;
					boe_act_qty_scm=0;
				}
				rset1.close();
				stmt1.close();
				
				num_boe=""+boeNumber;
				
				totalBOEMMBTU += boe_qty;
				totalBOEMT += boe_mt;
				totalBOESCM += boe_scm;
				
				totalActBOEMMBTU += boe_act_qty;
				totalActBOEMT += boe_act_qty_mt;
				totalActBOESCM += boe_act_qty_scm;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoBLLinkedBOEDetails()
	{
		String function_nm="getCargoBLDetails()";
		try
		{
			int blNumber=0;
			
			Vector VTOTAL_BL_NO = new Vector();
			Vector VBL_NO_EXIST_BOE = new Vector();
			Vector<String> VBL_DUP_AVAIL_BL_NO = new Vector();
			
			queryString="SELECT BL_NO "
					+ "FROM FMS_BUY_CARGO_NOM_BL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
					+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
					+ "ORDER BY BL_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			stmt.setString(7, cargo_number);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				blNumber++;
				String bl_number = (utilBean.PrePaddingZero(rset.getString(1)==null?"":rset.getString(1),2));
				
				VTOTAL_BL_NO.add(bl_number);
				
				String boe_number = "";
				
				queryString1="SELECT LINKED_BL, BOE_NO "
						+ "FROM FMS_BUY_CARGO_NOM_BOE A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
						+ "ORDER BY BOE_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_type);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, cargo_number);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String linked_bl_list = rset1.getString(1)==null?"":rset1.getString(1);
					boe_number = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(linked_bl_list.contains(bl_number)) 
					{
						VBL_NO_EXIST_BOE.add("Y");
					}
					else 
					{
						VBL_NO_EXIST_BOE.add("N");
					}
				}
				
				if(!VBL_NO_EXIST_BOE.contains("Y"))
				{
					VBL_DUP_AVAIL_BL_NO.add("BL"+bl_number);
					
					for (String element : VBL_DUP_AVAIL_BL_NO)
					{
						if (!VBL_AVAIL_FOR_ALL.contains(element))
						{
							VBL_AVAIL_FOR_ALL.add(element);
			            }
			        }
				}

				VBL_NO_EXIST_BOE.clear();
				
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
	String agreement_type = "";
	public void setAgreement_type(String agreement_type) {this.agreement_type = agreement_type;}
	String confirm_no = "";
	public void setConfirm_no(String confirm_no) {this.confirm_no = confirm_no;}

	public void setCargo_number(String cargo_number) {this.cargo_number = cargo_number;}

	
	String clearance = "";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String agmt_ref_no = "";
	String agmt_name = "";
	String agmt_typ = "";
	String contract_type = "";
	String cont_start_dt = "";
	String segment="";
	String gas_dt="";
	String nomination_freq="";
	String nomination_type="";
	String segmentType = "";
	String from_dt = "";
	String to_dt = "";
	String display_msg ="";
	String max_end_dt ="";
	
	String ship_cd="";
	String ship_name ="";
	String ship_eff_dt ="";
	String ship_call_sign="";
	String ship_flag="";
	String ship_imo_no="";
	String ship_class_soc="";
	String inmarsat_no="";
	String ship_owner_name="";
	String ship_operator_name="";
	String ship_fax_no="";
	String ship_telex_no="";
	String ship_email="";
	String gross_tonnage="";
	String cargo_capacity="";
	String volume_unit="";
	String percentage_capacity="";
	String ship_item="";
	String allocation_status = "";
	String country_org="";
	String active_status = "";
	
	double totalBlMMBTU = 0.00;
	double totalBlMT = 0.00;
	double totalBlSCM = 0.00;
	
	double totalBOEMMBTU = 0.00;
	double totalBOEMT = 0.00;
	double totalBOESCM = 0.00;
	
	double totalActBOEMMBTU = 0.00;
	double totalActBOEMT = 0.00;
	double totalActBOESCM = 0.00;
	
	public void setShip_cd(String ship_cd) {this.ship_cd = ship_cd;}
	public void setCountry_org(String country_org) {this.country_org = country_org;}
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
	public void setAllocation_status(String allocation_status) {this.allocation_status = allocation_status;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
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
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_FULL_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	
	Vector VBL_AVAIL_FOR_ALL = new Vector();
	Vector VBL_ACC_LIST = new Vector();
	
	Vector VAGMT_REF_NO = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VAGMT_TYP = new Vector();
	Vector VAGMT_STATUS = new Vector();
	
	Vector VTCQ = new Vector();
	Vector VQTY_UNIT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_DISP_NAME = new Vector();
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
	Vector VNOMINATION_STATUS = new Vector();
	Vector VNOMINATION_REV_NO = new Vector();
	Vector VALLOCATION_STATUS = new Vector();
	Vector VALLOC_STATUS = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VBOOKED_QTY = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VFCC_FLAG = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VAGMT_BASE_NM = new Vector();
	Vector VCONTPARTY_CD = new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VAGMT_TYPE_FLG = new Vector();
	Vector VAGMT_BASE_FLG = new Vector();
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
	Vector VBU_POINT=new Vector();
	Vector VTRADER_PLANT=new Vector();
	Vector VSEL_SPLIT_VALUE=new Vector();
	Vector VOTH_SEL_SPLIT_VALUE=new Vector();
	Vector VOTH_SEL_TRAD_CD = new Vector();
	Vector VOTH_SEL_PLANT_SEQ_NO = new Vector();
	Vector VOTH_SEL_PLANT_ABBR = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	
	Vector VCARGO_NAME = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VDISP_CARGO_NO = new Vector();
	Vector VCARGO_REF = new Vector();
	Vector VCARGO_STATUS = new Vector();
	Vector VCARGO_STATUS_FLG = new Vector();
	Vector VCARGO_QTY = new Vector();
	Vector VCARGO_PRICE = new Vector();
	Vector VCARGO_PRICE_UNIT = new Vector();
	Vector VCARGO_PRICE_UNIT_FLG = new Vector();
	Vector VCARGO_START_DT = new Vector();
	Vector VCARGO_END_DT = new Vector();
	Vector VCARGO_AGMT_BASE = new Vector();
	Vector VCARGO_TOLERANCE_PER = new Vector();

	Vector VIS_CARGO_NOMINATED = new Vector();
	Vector VIS_CARGO_ALLOCATED = new Vector();
	
	Vector VSHIP_CD = new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VSHIP_EFF_DT = new Vector();
	Vector VSHIP_CALL_SIGN = new Vector();
	Vector VSHIP_FLAG = new Vector();
	Vector VSHIP_IMO_NO = new Vector();
	Vector VSHIP_CLASS_SOC = new Vector();
	Vector VINMARSAT_NO = new Vector();
	Vector VSHIP_OWNER_NAME = new Vector();
	Vector VSHIP_OPERATOR_NAME = new Vector();
	Vector VSHIP_FAX_NO = new Vector();
	Vector VSHIP_TELEX_NO = new Vector();
	Vector VSHIP_EMAIL = new Vector();
	Vector VGROSS_TONNAGE = new Vector();
	Vector VCARGO_CAPACITY = new Vector();
	Vector VVOLUME_UNIT = new Vector();
	Vector VPERCENTAGE_CAPACITY = new Vector();
	Vector VSHIP_ITEM = new Vector();
	
	Vector VCOUNTRY_NM = new Vector();
	Vector VCOUNTRY_ISO = new Vector();
	Vector VCOUNTRY_CODE = new Vector();
	Vector VCONF_VOL = new Vector();
	Vector VCONF_PRICE = new Vector();
	Vector VCONF_PRICE_UNIT = new Vector();
	
	Vector VTOTAL_BL_NO = new Vector();
	Vector VBL_NO = new Vector();
	Vector VDISP_BL_NO=new Vector();
	Vector VBL_QTY = new Vector();
	Vector VBL_LOADED_QTY =  new Vector();
	Vector VBL_QTY_UNIT = new Vector();
	Vector VBL_PRICE = new Vector();
	Vector VBL_PRICE_UNIT = new Vector();
	Vector VBL_REF = new Vector();
	Vector VBL_DT = new Vector();
	Vector VBL_IMPORT_DEPT_SNO = new Vector();
	Vector VBL_IMPORT_CD = new Vector();
	Vector VBL_ENDORSE_DT = new Vector();
	Vector VBL_QTY_MT = new Vector();
	Vector VBL_QTY_SCM = new Vector();
	Vector VBL_REMARK = new Vector();
	Vector VBL_CNT = new Vector();
	Vector VBL_EXP_SCM = new Vector();
	Vector VBL_EXP_MT = new Vector();
	
	Vector VTOTAL_BOE_NO = new Vector();
	Vector VBOE_NO = new Vector();
	Vector VDISP_BOE_NO = new Vector();
	Vector VBOE_QTY = new Vector();
	Vector VBOE_QTY_UNIT = new Vector();
	Vector VBOE_PRICE = new Vector();
	Vector VBOE_PRICE_UNIT = new Vector();
	Vector VBOE_BU_SEQ = new Vector();
	Vector VBOE_BU_ABBR = new Vector();
	Vector VBOE_PLANT_SEQ = new Vector();
	Vector VBOE_PLANT_NM = new Vector();
	Vector VBOE_PLANT_ABBR = new Vector();
	Vector VBOE_REF = new Vector();
	Vector VBOE_DT = new Vector();
	Vector VBOE_QTY_MT = new Vector();
	Vector VBOE_QTY_SCM = new Vector();
	Vector VBOE_ACT_QTY = new Vector();
	Vector VBOE_ACT_QTY_UNIT = new Vector();
	Vector VBOE_ACT_QTY_MT = new Vector();
	Vector VBOE_ACT_QTY_SCM = new Vector();
	Vector VBOE_CUSTOM_DUTY = new Vector();
	Vector VBOE_LOAD_PORT = new Vector();
	Vector VBOE_LINKED_BL = new Vector();
	Vector VBOE_CNT = new Vector();
	Vector VLINKED_BL = new Vector();
	Vector VBOE_PROVISIONAL_PRICE = new Vector();
	Vector VBOE_PROVISIONAL_PRICE_UNIT = new Vector();
	Vector VBOE_FINAL_PRICE = new Vector();
	Vector VBOE_FINAL_PRICE_UNIT = new Vector();
	
	Vector VALLOC_REV_NO = new Vector();
	
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	
	public Vector getVTOTAL_BL_NO() {return VTOTAL_BL_NO;}
	public Vector getVBL_NO() {return VBL_NO;}
	public Vector getVDISP_BL_NO() {return VDISP_BL_NO;}
	public Vector getVBL_QTY() {return VBL_QTY;}
	public Vector getVBL_LOADED_QTY() {return VBL_LOADED_QTY;}
	public Vector getVBL_QTY_UNIT() {return VBL_QTY_UNIT;}
	public Vector getVBL_PRICE() {return VBL_PRICE;}
	public Vector getVBL_PRICE_UNIT() {return VBL_PRICE_UNIT;}
	public Vector getVBL_REF() {return VBL_REF;}
	public Vector getVBL_DT() {return VBL_DT;}
	public Vector getVBL_IMPORT_DEPT_SNO() {return VBL_IMPORT_DEPT_SNO;}
	public Vector getVBL_IMPORT_CD() {return VBL_IMPORT_CD;}
	public Vector getVBL_ENDORSE_DT() {return VBL_ENDORSE_DT;}
	public Vector getVBL_QTY_MT() {return VBL_QTY_MT;}
	public Vector getVBL_QTY_SCM() {return VBL_QTY_SCM;}
	public Vector getVBL_REMARK() {return VBL_REMARK;}
	public Vector getVBL_CNT() {return VBL_CNT;}
	public Vector getVBL_AVAIL_FOR_ALL() {return VBL_AVAIL_FOR_ALL;}
	public Vector getVBL_ACC_LIST() {return VBL_ACC_LIST;}
	public Vector getVBL_EXP_SCM() {return VBL_EXP_SCM;}
	public Vector getVBL_EXP_MT() {return VBL_EXP_MT;}
	
	public Vector getVTOTAL_BOE_NO() {return VTOTAL_BOE_NO;}
	public Vector getVBOE_BU_ABBR() {return VBOE_BU_ABBR;}
	public Vector getVBOE_PLANT_NM() {return VBOE_PLANT_NM;}
	public Vector getVBOE_PLANT_ABBR() {return VBOE_PLANT_ABBR;}
	public Vector getVBOE_REF() {return VBOE_REF;}
	public Vector getVBOE_DT() {return VBOE_DT;}
	public Vector getVBOE_QTY_MT() {return VBOE_QTY_MT;}
	public Vector getVBOE_QTY_SCM() {return VBOE_QTY_SCM;}
	public Vector getVBOE_ACT_QTY() {return VBOE_ACT_QTY;}
	public Vector getVBOE_ACT_QTY_UNIT() {return VBOE_ACT_QTY_UNIT;}
	public Vector getVBOE_ACT_QTY_MT() {return VBOE_ACT_QTY_MT;}
	public Vector getVBOE_ACT_QTY_SCM() {return VBOE_ACT_QTY_SCM;}
	public Vector getVBOE_CUSTOM_DUTY() {return VBOE_CUSTOM_DUTY;}
	public Vector getVBOE_LOAD_PORT() {return VBOE_LOAD_PORT;}
	public Vector getVBOE_LINKED_BL() {return VBOE_LINKED_BL;}
	public Vector getVBOE_NO() {return VBOE_NO;}
	public Vector getVDISP_BOE_NO() {return VDISP_BOE_NO;}
	public Vector getVBOE_QTY() {return VBOE_QTY;}
	public Vector getVBOE_QTY_UNIT() {return VBOE_QTY_UNIT;}
	public Vector getVBOE_PRICE() {return VBOE_PRICE;}
	public Vector getVBOE_PRICE_UNIT() {return VBOE_PRICE_UNIT;}
	public Vector getVBOE_BU_SEQ() {return VBOE_BU_SEQ;}
	public Vector getVBOE_PLANT_SEQ() {return VBOE_PLANT_SEQ;}
	public Vector getVBOE_CNT() {return VBOE_CNT;}
	public Vector getVLINKED_BL() {return VLINKED_BL;}
	
	public Vector getVBOE_PROVISIONAL_PRICE() {return VBOE_PROVISIONAL_PRICE;}
	public Vector getVBOE_PROVISIONAL_PRICE_UNIT() {return VBOE_PROVISIONAL_PRICE_UNIT;}
	public Vector getVBOE_FINAL_PRICE() {return VBOE_FINAL_PRICE;}
	public Vector getVBOE_FINAL_PRICE_UNIT() {return VBOE_FINAL_PRICE_UNIT;}
	
	public Vector getVSHIP_CD() {return VSHIP_CD;}
	public Vector getVSHIP_NAME() {return VSHIP_NAME;}
	public Vector getVSHIP_EFF_DT() {return VSHIP_EFF_DT;}
	public Vector getVSHIP_CALL_SIGN() {return VSHIP_CALL_SIGN;}
	public Vector getVSHIP_FLAG() {return VSHIP_FLAG;}
	public Vector getVSHIP_IMO_NO() {return VSHIP_IMO_NO;}
	public Vector getVSHIP_CLASS_SOC() {return VSHIP_CLASS_SOC;}
	public Vector getVINMARSAT_NO() {return VINMARSAT_NO;}
	public Vector getVSHIP_OWNER_NAME() {return VSHIP_OWNER_NAME;}
	public Vector getVSHIP_OPERATOR_NAME() {return VSHIP_OPERATOR_NAME;}
	public Vector getVSHIP_FAX_NO() {return VSHIP_FAX_NO;}
	public Vector getVSHIP_TELEX_NO() {return VSHIP_TELEX_NO;}
	public Vector getVSHIP_EMAIL() {return VSHIP_EMAIL;}
	public Vector getVGROSS_TONNAGE() {return VGROSS_TONNAGE;}
	public Vector getVCARGO_CAPACITY() {return VCARGO_CAPACITY;}
	public Vector getVVOLUME_UNIT() {return VVOLUME_UNIT;}
	public Vector getVPERCENTAGE_CAPACITY() {return VPERCENTAGE_CAPACITY;}
	public Vector getVSHIP_ITEM() {return VSHIP_ITEM;}
	
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
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_FULL_NO() {return VAGMT_FULL_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVAGMT_TYP() {return VAGMT_TYP;}
	public Vector getVAGMT_STATUS() {return VAGMT_STATUS;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVAGMT_REF_NO() {return VAGMT_REF_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_DISP_NAME() {return VCONT_DISP_NAME;}
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
	public Vector getVNOMINATION_STATUS() {return VNOMINATION_STATUS;}
	public Vector getVNOMINATION_REV_NO() {return VNOMINATION_REV_NO;}
	public Vector getVALLOCATION_STATUS() {return VALLOCATION_STATUS;}
	public Vector getVALLOC_STATUS() {return VALLOC_STATUS;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVBOOKED_QTY() {return VBOOKED_QTY;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVFCC_FLAG() {return VFCC_FLAG;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVAGMT_BASE_NM() {return VAGMT_BASE_NM;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVAGMT_TYPE_FLG() {return VAGMT_TYPE_FLG;}
	public Vector getVAGMT_BASE_FLG() {return VAGMT_BASE_FLG;}
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
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVTRADER_PLANT() {return VTRADER_PLANT;}
	public Vector getVSEL_SPLIT_VALUE() {return VSEL_SPLIT_VALUE;}
	public Vector getVOTH_SEL_SPLIT_VALUE() {return VOTH_SEL_SPLIT_VALUE;}
	public Vector getVOTH_SEL_TRAD_CD() {return VOTH_SEL_TRAD_CD;}
	public Vector getVOTH_SEL_PLANT_SEQ_NO() {return VOTH_SEL_PLANT_SEQ_NO;}
	public Vector getVOTH_SEL_PLANT_ABBR() {return VOTH_SEL_PLANT_ABBR;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	
	public Vector getVCARGO_NAME() {return VCARGO_NAME;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVDISP_CARGO_NO() {return VDISP_CARGO_NO;}
	public Vector getVCARGO_REF() {return VCARGO_REF;}
	public Vector getVCARGO_STATUS() {return VCARGO_STATUS;}
	public Vector getVCARGO_STATUS_FLG() {return VCARGO_STATUS_FLG;}
	public Vector getVCARGO_QTY() {return VCARGO_QTY;}
	public Vector getVCARGO_PRICE() {return VCARGO_PRICE;}
	public Vector getVCARGO_PRICE_UNIT() {return VCARGO_PRICE_UNIT;}
	public Vector getVCARGO_PRICE_UNIT_FLG() {return VCARGO_PRICE_UNIT_FLG;}
	public Vector getVCARGO_START_DT() {return VCARGO_START_DT;}
	public Vector getVCARGO_END_DT() {return VCARGO_END_DT;}
	public Vector getVCARGO_AGMT_BASE() {return VCARGO_AGMT_BASE;}
	public Vector getVCARGO_TOLERANCE_PER() {return VCARGO_TOLERANCE_PER;}
	
	public Vector getVIS_CARGO_NOMINATED() {return VIS_CARGO_NOMINATED;}
	public Vector getVIS_CARGO_ALLOCATED() {return VIS_CARGO_ALLOCATED;}
	
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVCOUNTRY_ISO() {return VCOUNTRY_ISO;}
	public Vector getVCOUNTRY_CODE() {return VCOUNTRY_CODE;}
	
	public Vector getVCONF_VOL() {return VCONF_VOL;}
	public Vector getVCONF_PRICE() {return VCONF_PRICE;}
	public Vector getVCONF_PRICE_UNIT() {return VCONF_PRICE_UNIT;}
	
	public Vector getVCONTPARTY_CD() {return VCONTPARTY_CD;}
	public Vector getVALLOC_REV_NO() {return VALLOC_REV_NO;}
	
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	
	String min_counterparty_eff_dt = "";
	//String cont_no = "";
	//String cont_rev_no = "";
	String cont_ref_no = "";
	String trade_ref_no = "";
	String signing_dt = "";
	String agmt_signing_dt = "";
	String signing_time = "";
	String dda_dt = "";
	String dda_time = "";
	String ent_dt = "";
	String ent_time = "";
	String start_dt = "";
	String end_dt = "";
	String agmt_start_dt = "";
	String agmt_end_dt = "";
	String agmt_base = "";
	String agmt_type = "";
	String status="";
	String status_nm="";
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
	String rev_dt = "";
	String demurrage = "";
	String demurrage_rate = "";
	String demurrage_rate_unit = "";
	String alw_laytime_hrs = "";
	String alw_laytime_mns = "";
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
	String cont_name = "";
	String cont_disp_name = "";
	String contpty_abbr="";
	String contpty_name="";
	String mapped_cont_no="";
	String fcc_flg="";
	String cont_status="";
	String demu_rate="";
	String demu_rate_unit="";
	String allowed_lay_time_hrs="";
	String allowed_lay_time_min="";
	String cont_status_flg="";
	String billing_freq="";
	String billing_flag="";
	String billing_clause = "";
	String termination_flag = "";
	String termination_clause = "";
	String termination_planned = "";
	String termination_forced = "";
	String due_date="";
	String sec_due_date="";
	String inv_currency="";
	String payment_currency="";
	String interest_rate_cd="";
	String interest_cal_sign="";
	String interest_cal_per="";
	String exchng_rate_cd="";
	String exchng_cal="";
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
	
	String spec_gas_clause = "";
	String measurement_clause = "";
	String demurrage_clause = "";
	String day_def_clause = "";
	
	String no_of_cargo_dtl = "";
	String cargo_number = "";
	String show_cargo_number = "";
	String cargo_no = "";
	String cargo_ref = "";
	String cargo_status = "";
	String cargo_start_dt = "";
	String cargo_end_dt = "";
	String cargo_volume = "";
	String cargo_agmt_base = "";
	String cargo_price = "";
	String cargo_price_unit = "";
	String agmt_agmt_base = "";
	
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
	
	String nom_revision ="";
	String nom_status ="";
	String mandate_conf_vol ="";
	String mandate_conf_vol_tol ="";
	String conf_price ="";
	String win_start_dt ="";
	String win_end_dt ="";
	String man_req_from_dt = "";
	String man_req_to_dt = "";
	String tolerance_per = "";
	
	String country_origin ="";
	String load_port ="";
	String num_bl ="";
	String num_boe ="";
	String liquefac_plant ="";
	String liquefac_country ="";
	String liquefac_promotor ="";
	String liquefac_remark ="";
	String split_bol ="";
	String cha_cont_dtl = "";
	String vessel_cont_dtl = "";
	String surveyor_cont_dtl = "";
	String disp_cha_cont_dtl = "";
	String disp_vessel_cont_dtl = "";
	String disp_surveyor_cont_dtl = "";
	
	String alloc_rev = "";
	String act_arrv_dt = "";
	String booked_dt = "";
	String booked_time = "";
	String float_dt = "";
	String float_time = "";
	String pob_dt = "";
	String pob_time = "";
	String uac_dt = "";
	String uac_time = "";
	String uadc_dt = "";
	String uadc_time = "";
	String departure_dt = "";
	String departure_time = "";
	String all_fast_dt = "";
	String all_fast_time =  "";
	String custom_start_dt = "";
	String custom_start_time = "";
	String custom_end_dt = "";
	String custom_end_time = "";
	String cargo_details_notes = "";
	String qq_cer_no = "";
	String qq_cer_dt = "";
	String net_unloaded_qunt = "";
	String net_unloaded_qunt_mt = "";
	String net_unloaded_qunt_m3 = "";
	String dens_material = "";
	String qq_ghv = "";
	String qq_gcv = "";
	String app_lab_notes = "";
	String expected_qunt = "";
	String alloc_status = "";
	
	String expected_start_dt = "";
	String expected_end_dt = "";
	String display_map_id = "";
	String counterparty_abbr = "";
	String display_agmt_id = "";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	
	public String getMandate_conf_vol() {return mandate_conf_vol;}
	public String getMandate_conf_vol_tol() {return mandate_conf_vol_tol;}
	public String getNom_status() {return nom_status;}
	public String getNom_revision() {return nom_revision;}
	public String getConf_price() {return conf_price;}
	public String getWin_start_dt() {return win_start_dt;}
	public String getWin_end_dt() {return win_end_dt;}
	public String getTolerance_per() {return tolerance_per;}
	public String getCountry_origin() {return country_origin;}
	public String getLoad_port() {return load_port;}
	public String getNum_bl() {return num_bl;}
	public String getNum_boe() {return num_boe;}
	public String getLiquefac_plant() {return liquefac_plant;}
	public String getLiquefac_country() {return liquefac_country;}
	public String getLiquefac_promotor() {return liquefac_promotor;}
	public String getLiquefac_remark() {return liquefac_remark;}
	public String getSplit_bol() {return split_bol;}
	public String getCha_cont_dtl() {return cha_cont_dtl;}
	public String getVessel_cont_dtl() {return vessel_cont_dtl;}
	public String getSurveyor_cont_dtl() {return surveyor_cont_dtl;}
	public String getDisp_cha_cont_dtl() {return disp_cha_cont_dtl;}
	public String getDisp_vessel_cont_dtl() {return disp_vessel_cont_dtl;}
	public String getDisp_surveyor_cont_dtl() {return disp_surveyor_cont_dtl;}
	public String getDisplay_msg() {return display_msg;}
	public String getMax_end_dt() {return max_end_dt;}
	
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	//public String getCont_no() {return cont_no;}
	//public String getCont_rev_no() {return cont_rev_no;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getTrade_ref_no() {return trade_ref_no;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getEnt_dt() {return ent_dt;}
	public String getEnt_time() {return ent_time;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
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
	public String getCont_disp_name() {return cont_disp_name;}
	public String getContpty_abbr() {return contpty_abbr;}
	public String getContpty_name() {return contpty_name;}
	public String getMapped_cont_no() {return mapped_cont_no;}
	public String getFcc_flg() {return fcc_flg;}
	public String getCont_status() {return cont_status;}
	public String getDemu_rate() {return demu_rate;}
	public String getDemu_rate_unit() {return demu_rate_unit;}
	public String getAllowed_lay_time_hrs() {return allowed_lay_time_hrs;}
	public String getAllowed_lay_time_min() {return allowed_lay_time_min;}
	public String getCont_status_flg() {return cont_status_flg;}
	public String getBilling_freq() {return billing_freq;}
	public String getBilling_flag() {return billing_flag;}
	public String getBilling_clause() {return billing_clause;}
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
	public String getAgmt_agmt_base() {return agmt_agmt_base;}
	
	double totalQty=0;
	double UnloadedtotalQty=0;
	double AvailabletotalQty=0;
	public Double getTotalQty() {return totalQty;}
	public Double getUnloadedtotalQty() {return UnloadedtotalQty;}
	public Double getAvailabletotalQty() {return AvailabletotalQty;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}

	public String getAgreement_type() {return agreement_type;}
	public String getClearance() {return clearance;}
	public String getCounterparty_cd() {return counterparty_cd;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getAgmt_no() {return agmt_no;}
	public String getAgmt_rev_no() {return agmt_rev_no;}
	public String getContract_type() {return contract_type;}
	public String getCont_start_dt() {return cont_start_dt;}
	public String getStatus() { return status; }
	public String getStatus_nm() { return status_nm; }
	public String getDemurrage() { return demurrage; }
	public String getDemurrage_rate() { return demurrage_rate; }
	public String getDemurrage_rate_unit() { return demurrage_rate_unit; }
	public String getAlw_laytime_hrs() { return alw_laytime_hrs; }
	public String getAlw_laytime_mns() { return alw_laytime_mns; }
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
	public String getAgmt_ref_no() { return agmt_ref_no; }
	public String getAgmt_name() { return agmt_name; }
	public String getAgmt_typ() { return agmt_typ; }
	public String getRev_dt() { return rev_dt; }
	public String getTermination_flag() {return termination_flag;}
	public String getTermination_clause() {return termination_clause;}
	public String getTermination_planned() {return termination_planned;}
	public String getTermination_forced() {return termination_forced;}
	public String getDay_def_clause() {return day_def_clause;}
	public String getDemurrage_clause() {return demurrage_clause;}
	public String getMeasurement_clause() {return measurement_clause;}
	public String getSpec_gas_clause() {return spec_gas_clause;}
	public void setAgmt_type(String agmt_type) {this.agmt_type = agmt_type;}
	
	public String getNo_of_cargo_dtl() {return no_of_cargo_dtl;}
	public String getCargo_number() {return cargo_number;}
	public String getShow_cargo_number() {return show_cargo_number;}
	public String getCargo_no() {return cargo_no;}
	public String getCargo_ref() {return cargo_ref;}
	public String getCargo_status() {return cargo_status;}
	public String getCargo_start_dt() {return cargo_start_dt;}
	public String getCargo_end_dt() {return cargo_end_dt;}
	public String getCargo_volume() {return cargo_volume;}
	public String getCargo_agmt_base() {return cargo_agmt_base;}
	public String getCargo_price() {return cargo_price;}
	public String getCargo_price_unit() {return cargo_price_unit;}
	
	public String getShip_cd() {return ship_cd;}
	public String getShip_name() {return ship_name;}
	public String getShip_call_sign() {return ship_call_sign;}
	public String getShip_flag() {return ship_flag;}
	public String getShip_imo_no() {return ship_imo_no;}
	public String getShip_class_soc() {return ship_class_soc;}
	public String getShip_owner_name() {return ship_owner_name;}
	public String getShip_operator_name() {return ship_operator_name;}
	public String getShip_fax_no() {return ship_fax_no;}
	public String getShip_telex_no() {return ship_telex_no;}
	public String getShip_email() {return ship_email;}
	public String getShip_item() {return ship_item;}
	public String getInmarsat_no() {return inmarsat_no;}
	public String getGross_tonnage() {return gross_tonnage;}
	public String getCargo_capacity() {return cargo_capacity;}
	public String getVolume_unit() {return volume_unit;}
	public String getPercentage_capacity() {return percentage_capacity;}
	
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
	
	public String getAlloc_rev() {return alloc_rev;}
	public String getAct_arrv_dt() {return act_arrv_dt;}
	public String getBooked_dt() {return booked_dt;}
	public String getBooked_time() {return booked_time;}
	public String getFloat_dt() {return float_dt;}
	public String getFloat_time() {return float_time;}
	public String getPob_dt() {return pob_dt;}
	public String getPob_time() {return pob_time;}
	public String getUac_dt() {return uac_dt;}
	public String getUac_time() {return uac_time;}
	public String getUadc_dt() {return uadc_dt;}
	public String getUadc_time() {return uadc_time;}
	public String getDeparture_dt() {return departure_dt;}
	public String getDeparture_time() {return departure_time;}
	public String getAll_fast_dt() {return all_fast_dt;}
	public String getAll_fast_time() {return all_fast_time;}
	public String getCustom_start_dt() {return custom_start_dt;}
	public String getCustom_start_time() {return custom_start_time;}
	public String getCustom_end_dt() {return custom_end_dt;}
	public String getCustom_end_time() {return custom_end_time;}
	public String getCargo_details_notes() {return cargo_details_notes;}
	public String getQq_cer_no() {return qq_cer_no;}
	public String getQq_cer_dt() {return qq_cer_dt;}
	public String getNet_unloaded_qunt() {return net_unloaded_qunt;}
	public String getNet_unloaded_qunt_mt() {return net_unloaded_qunt_mt;}
	public String getNet_unloaded_qunt_m3() {return net_unloaded_qunt_m3;}
	public String getDens_material() {return dens_material;}
	public String getQq_ghv() {return qq_ghv;}
	public String getQq_gcv() {return qq_gcv;}
	public String getApp_lab_notes() {return app_lab_notes;}
	public String getExpected_qunt() {return expected_qunt;}
	public String getAlloc_status() {return alloc_status;}
	public double getTotalBlMMBTU() {return totalBlMMBTU;}
	public double getTotalBlMT() {return totalBlMT;}
	public double getTotalBlSCM() {return totalBlSCM;}
	public double getTotalBOEMMBTU() {return totalBOEMMBTU;}
	public double getTotalBOEMT() {return totalBOEMT;}
	public double getTotalBOESCM() {return totalBOESCM;}
	public double getTotalActBOEMMBTU() {return totalActBOEMMBTU;}
	public double getTotalActBOEMT() {return totalActBOEMT;}
	public double getTotalActBOESCM() {return totalActBOESCM;}
	
	public String getExpected_end_dt() {return expected_end_dt;}
	public String getExpected_start_dt() {return expected_start_dt;}
	public String getMan_req_to_dt() {return man_req_to_dt;}
	public String getMan_req_from_dt() {return man_req_from_dt;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getDisplay_map_id() {return display_map_id;}
	public String getDisplay_agmt_id() {return display_agmt_id;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
}
