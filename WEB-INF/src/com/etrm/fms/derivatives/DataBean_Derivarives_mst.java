package com.etrm.fms.derivatives;

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

public class DataBean_Derivarives_mst 
{
	String db_src_file_name="DataBean_Derivarives_mst.java";

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
	    			if(callFlag.equalsIgnoreCase("HEDGE_AGREEMENT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getHedgetraderlist();
	    					
	    					//HM: Currently Agreement's contract reverse logic is not implemented as there is no End date for Hedge Contract
	    					//getCurrentContractDtl();
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
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("HEDGE_AGREEMENT_BILLING_DTL"))
	    			{
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agmt_type);
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getBillingDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("HEDGE_AGMT_LIST"))
	    			{
	    				getHedgeAgreementList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("HEDGE_CONT_MST"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				contpty_name=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, instrument_no);
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getHedgeContTraderList();
	    				}
	    				else if(opration.equalsIgnoreCase("INSERT"))
	    				{
	    					getHedgetraderlist();
	    				}
	    				getHedgeAgmtContDetails();
	    				getTraderPlantList();
	    				getBusinessPlantList();
	    				if(opration.equalsIgnoreCase("INSERT"))
	    				{
		    				getSelectedTraderPlantList();
		    				getSelectedBusinessPlantList();
	    				}
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getHedgeContractDetail();
	    					getSelectedHedgeTraderPlantList();
		    				getSelectedHedgeBusinessPlantList();
		    				getHedgeCountBillingDetail();
		    				getCountSecurityDetail();
		    				getInstrumentNoDtls();
		    				getHedgeInstrmentDtls();
		    				getProductMst();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("HEDGE_CONT_LIST"))
	    			{
	    				getHedgeContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("HEDGE_CONT_BILLING_DTL"))
	    			{
	    				display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getHedgeCountBillingDetail();
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getCnBillingDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DIRECT_EXPOSURE"))
	    			{
	    				getDirectExposureDtl();
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
	public void getDirectExposureDtl()
	{
		String function_nm="getDirectExposureDtl()";
		try
		{
			queryString="SELECT TO_CHAR(HEDGE_DT,'DD/MM/YYYY'),BALANCE_QTY "
					+ "FROM FMS_DERV_HEDGE_EXPOSURE_DTL "
					+ "WHERE COMPANY_CD=? AND FLAG='L' "
					+ "ORDER BY HEDGE_DT DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String hedge_dt=rset.getString(1)==null?"":rset.getString(1);
				String balance_qty=rset.getString(2)==null?"":rset.getString(2);
				
				VHEDGE_DT.add(hedge_dt);
				VBALANCE_QTY.add(balance_qty);
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
	
	public void getHedgetraderlist()
	{
		String function_nm="getHedgetraderlist()";
		
		try
		{
			queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_DERV_AGMT_MST "
					+ "WHERE COMPANY_CD=? ";
			if(callFlag.equalsIgnoreCase("HEDGE_CONT_MST"))
			{
				queryString+="AND END_DT >= SYSDATE";	
			}
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
	
	public void getCurrentContractDtl()
	{
		String function_nm="getCurrentContractDtl()";
		
		try
		{
			display_msg = "End date for Agreement can not be smaller than ongoing contract!!\n\n";
			
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,CONT_REF_NO,AGMT_BASE,CONTRACT_TYPE,AGMT_TYPE,"
					+ "(SELECT TO_CHAR(MAX(DEAL_CONF_DT),'DD/MM/YYYY') FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO) "
					+ "FROM FMS_DERV_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "ORDER BY DEAL_CONF_DT DESC ";
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
	
	public void getTraderPlantList()
	{
		String function_nm="getTraderPlantList()";

		try
		{
			if(callFlag.equalsIgnoreCase("HEDGE_CONT_MST"))
			{
				queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_DERV_AGMT_PLANT "
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
			if(callFlag.equalsIgnoreCase("HEDGE_CONT_MST"))
			{
				queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_DERV_AGMT_BU "
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
					+ "AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,"
					+ "TO_CHAR(REV_DT,'DD/MM/YYYY'),BILLING_FLAG,BILLING_CLAUSE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH:MM') "
					+ "FROM FMS_DERV_AGMT_MST "
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

				signing_dt = rset.getString(7)==null?"":rset.getString(7);
				start_dt =rset.getString(8)==null?"":rset.getString(8);
				end_dt =rset.getString(9)==null?"":rset.getString(9);
				status = rset.getString(10)==null?"":rset.getString(10);
				status_nm = ""+AgmtStatusName(status);

				rev_dt = rset.getString(11)==null?"":rset.getString(11);
				
				billing_flag = rset.getString(12)==null?"":rset.getString(12);
				billing_clause = rset.getString(13)==null?"":rset.getString(13);

				String agmt_ent_dt=rset.getString(14)==null?"":rset.getString(14);
				String split[] = agmt_ent_dt.split(" ");
				
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
	
	public void getSelectedTraderPlantList()
	{
		String function_nm="getSelectedTraderPlantList()";

		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_DERV_AGMT_PLANT "
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";

		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_DERV_AGMT_BU "
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
	

	public void getCountBillingDetail()
	{
		String function_nm="getCountBillingDetail()";

		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_DERV_AGMT_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "//AND AGMT_REV=?
					+ " AND AGMT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			//stmt.setString(4, agmt_rev_no);
			stmt.setString(4, agmt_type);
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
	
	public void getCountSecurityDetail()
	{
		String function_nm="getCountSecurityDetail()";

		try
		{
			String gx="K";//AS OF NOW HARDCODED 20230913
			queryString="SELECT COUNT(*),SEC_CATEGORY "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
					+ "GROUP BY SEC_CATEGORY";
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
				sec_category_value=rset.getString(2)==null?"":rset.getString(2);
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
	
	public void getSelectedAgmtPlantlist() 
	{
		String function_nm="getSelectedAgmtPlantlist()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_DERV_AGMT_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_PLANT B WHERE "
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
	
	public void getBillingDetail()
	{
		String function_nm="getBillingDetail()";

		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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
							+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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
								+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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
	
	public void getHedgeAgreementList()
	{
		String function_nm="getHedgeAgreementList()";
		
		try
		{
			int count=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,AGMT_REF_NO,AGMT_NAME, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,AGMT_TYPE "
					+ "FROM FMS_DERV_AGMT_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_DERV_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO ) ";
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,AGMT_REF_NO,AGMT_NAME, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,AGMT_TYPE "
						+ "FROM FMS_DERV_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_DERV_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO ) ";
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
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, agreement_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, agreement_type);
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
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				String agmt_ref=rset.getString(5)==null?"":rset.getString(5);
				String agmt_name=rset.getString(6)==null?"":rset.getString(6);
				String status=rset.getString(9)==null?"":rset.getString(9);
				String agmt_typ=rset.getString(10)==null?"":rset.getString(10);
				
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

				VAGMT_STATUS.add(""+AgmtStatusName(status));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getHedgeContTraderList()
	{
		String function_nm="getHedgeContTraderList()";
		
		try
		{
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_DERV_CONT_MST WHERE COMPANY_CD=?";
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
	
	public void getHedgeAgmtContDetails() 
	{
		String function_nm="getHedgeAgmtContDetails()";
		try
		{
			String queryString="SELECT COUNTERPARTY_CD,"
					+ "AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),STATUS,"
					+ "TO_CHAR(REV_DT,'DD/MM/YYYY'),BILLING_FLAG,BILLING_CLAUSE "
					+ "FROM FMS_DERV_AGMT_MST "
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
				agmt_signing_dt = rset.getString(7)==null?"":rset.getString(7);
				agmt_start_dt =rset.getString(8)==null?"":rset.getString(8);
				agmt_end_dt =rset.getString(9)==null?"":rset.getString(9);
				status = rset.getString(10)==null?"":rset.getString(10);
				status_nm = ""+AgmtStatusName(status);
				rev_dt = rset.getString(11)==null?"":rset.getString(11);
				
				billing_flag = rset.getString(12)==null?"":rset.getString(12);
				billing_clause = rset.getString(13)==null?"":rset.getString(13);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getHedgeContractDetail()
	{
		String function_nm="getHedgeContractDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
					+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH:MM'),BILLING_FLAG,BILLING_CLAUSE,TO_CHAR(REV_DT,'DD/MM/YYYY'),"
					+ "REMARKS,TO_CHAR(TRADE_DT,'DD/MM/YYYY'),TRADE_TIME,CLOSURE_REMARK "
					+ "FROM FMS_DERV_CONT_MST "
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
				agmt_type=rset.getString(3)==null?"":rset.getString(3);
				contract_type=rset.getString(6)==null?"":rset.getString(6);
				cont_no=rset.getString(7)==null?"":rset.getString(7);
				cont_rev_no=rset.getString(8)==null?"":rset.getString(8);
				cont_name=rset.getString(9)==null?"":rset.getString(9);
				cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
				cont_status_flg=rset.getString(11)==null?"":rset.getString(11);
				cont_status=""+utilBean.ContStatusName(cont_status_flg);
				dda_dt=rset.getString(12)==null?"":rset.getString(12);
				dda_time=rset.getString(13)==null?"":rset.getString(13);
				signing_dt=rset.getString(14)==null?"":rset.getString(14);
				signing_time=rset.getString(15)==null?"":rset.getString(15);
				//start_dt=rset.getString(18)==null?"":rset.getString(18);
				//end_dt=rset.getString(19)==null?"":rset.getString(19);
				
				String deal_ent_dt=rset.getString(16)==null?"":rset.getString(16);
				
				String split[] = deal_ent_dt.split(" ");
				ent_dt = split[0];
				ent_time = split[1];
				billing_flag = rset.getString(17)==null?"":rset.getString(17);
				billing_clause = rset.getString(18)==null?"":rset.getString(18);
				rev_dt = rset.getString(19)==null?"":rset.getString(19);
				remark = rset.getString(20)==null?"":rset.getString(20);
				trade_dt = rset.getString(21)==null?"":rset.getString(21);
				trade_time = rset.getString(22)==null?"":rset.getString(22);
				closure_note=rset.getString(23)==null?"":rset.getString(23);
				
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
	
	public void getSelectedHedgeTraderPlantList()
	{
		String function_nm="getSelectedHedgeTraderPlantList()";
		try
		{
			queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_DERV_CONT_PLANT "
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
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedHedgeBusinessPlantList()
	{
		String function_nm="getSelectedHedgeBusinessPlantList()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_DERV_CONT_BU "
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
	
	public void getHedgeCountBillingDetail()
	{
		String function_nm="getHedgeCountBillingDetail()";
		try
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_DERV_CONT_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			//stmt.setString(4, agmt_rev_no);
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
	
	public void getInstrumentNoDtls() 
	{
		String function_nm="getInstrumentNoDtls()";
		try
		{
			String cont_map_name=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			queryString="SELECT MAX(INSTRUMENT_NO),COUNT(INSTRUMENT_NO) "
					+ "FROM FMS_DERV_INSTRUMENT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND AGMT_REV=? AND CONT_REV=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, cont_rev_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				no_of_instrument_dtl=rset.getString(2)==null?"":rset.getString(2);
				if(rset.getInt(1) > 0)
				{
					instrument_number=""+(rset.getInt(1)+1);
					show_instru_number=""+cont_map_name+"-"+instrument_number;
				}
				else
				{
					instrument_number=""+1;
					show_instru_number=""+cont_map_name+"-"+instrument_number;
				}
			}
			else
			{
				instrument_number=""+1;
				show_instru_number=""+cont_map_name+"-"+instrument_number;
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getHedgeInstrmentDtls() 
	{
		String function_nm="getHedgeInstrmentDtls()";
		try
		{
			queryString="SELECT INSTRUMENT_NO,INSTRUMENT_TYPE,BUY_SELL,STATUS,QTY,QTY_UNIT,RATE,RATE_UNIT,"
					+ "PRODUCT_NM,CURVE_NM,PROJ_METHOD,TO_CHAR(CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),CONV_FACTOR "
					+ "FROM FMS_DERV_INSTRUMENT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND AGMT_REV=? AND CONT_REV=? AND AGMT_TYPE=? AND CONTRACT_TYPE=? "
					+ "ORDER BY INSTRUMENT_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, agmt_type);
			stmt.setString(8, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String instrument_no = rset.getString(1)==null?"":rset.getString(1);
				String instrument_type = rset.getString(2)==null?"":rset.getString(2);
				String buy_sell = rset.getString(3)==null?"":rset.getString(3);
				String instrument_status = rset.getString(4)==null?"":rset.getString(4);
				double instrument_volume = rset.getDouble(5);
				String temp_instrument_volume_unit = rset.getString(6)==null?"":rset.getString(6);
				String instrument_volume_unit = utilBean.getEnergyUnitNm(conn, temp_instrument_volume_unit);
				
				//String instrument_rate = rset.getString(7)==null?"":rset.getString(7);
				String instrument_rate_unit = rset.getString(8)==null?"":rset.getString(8);
				
				double temp_instrument_rate = rset.getDouble(7);
				String instrument_rate=""+utilBean.RateNumberFormat(temp_instrument_rate, instrument_rate_unit);
				
				String product_nm = rset.getString(9)==null?"":rset.getString(9);
				String curve_nm = rset.getString(10)==null?"":rset.getString(10);
				String proj_method = rset.getString(11)==null?"":rset.getString(11);
				String cont_dd_mm_yr = rset.getString(12)==null?"":rset.getString(12);
				
				String price_start_dt = rset.getString(13)==null?"":rset.getString(13);
				String price_end_dt = rset.getString(14)==null?"":rset.getString(14);

				String conversion_factor = rset.getString(15)==null?"":rset.getString(15);
				
				VINSTRUMENT_NO.add(instrument_no);
				VDISP_INSTRUMENT_NO.add(""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, instrument_no));
				VINSTRUMENT_TYPE.add(instrument_type);
				VINSTRUMENT_BUY_SELL.add(buy_sell);
				VINSTRUMENT_STATUS_FLG.add(instrument_status);
				if(instrument_status.equals("Y"))
				{
					VINSTRUMENT_STATUS.add("Confirmed");
				}
				else if(instrument_status.equals("N"))
				{
					VINSTRUMENT_STATUS.add("Not-Confirmed");
				}
				else if(instrument_status.equals("X"))
				{
					VINSTRUMENT_STATUS.add("Cancelled");
				}
				else
				{
					VINSTRUMENT_STATUS.add("");
				}
				
				VINSTRUMENT_QTY.add(nf.format(instrument_volume));
				VINSTRUMENT_QTY_UNIT.add(instrument_volume_unit);
				
				VINSTRUMENT_RATE.add(instrument_rate);
				VINSTRUMENT_RATE_UNIT.add(instrument_rate_unit);
				
				VPRICE_START_DT.add(price_start_dt);
				VPRICE_END_DT.add(price_end_dt);
				
				VPRODUCT_NM.add(product_nm);
				VCURVE_NM.add(curve_nm);
				VPROJ_METHOD.add(proj_method);
				VCONT_DD_MM_YR.add(cont_dd_mm_yr);
				VCONVERSION_FACTOR.add(conversion_factor);
				VCONVERSION_FACTOR_UNIT.add(instrument_volume_unit+"to MMBTU");
				
				int count=0;
				queryString1="SELECT INSTRUMENT_NO "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND AGMT_REV=? AND CONT_REV=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND INV_FLAG='F' "
						+ "ORDER BY INSTRUMENT_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, agmt_rev_no);
				stmt1.setString(6, cont_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, instrument_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VGEN_INV_INSTRUMENT_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
					count++;
				}
				else
				{
					VGEN_INV_INSTRUMENT_NO.add("");
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT INSTRUMENT_NO "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND AGMT_REV=? AND CONT_REV=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG='F' "
						+ "ORDER BY INSTRUMENT_NO";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, agmt_no);
				stmt2.setString(4, cont_no);
				stmt2.setString(5, agmt_rev_no);
				stmt2.setString(6, cont_rev_no);
				stmt2.setString(7, contract_type);
				stmt2.setString(8, instrument_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					VGEN_PDF_INV_INSTRUMENT_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
				}
				else
				{
					VGEN_PDF_INV_INSTRUMENT_NO.add("");
				}
				rset2.close();
				stmt2.close();
				
				queryString3="SELECT CRITERIA,CHECKED_FLAG "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND AGMT_REV=? AND CONT_REV=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND INV_FLAG IN ('CR','DR') "
						+ "ORDER BY INSTRUMENT_NO";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, cont_no);
				stmt3.setString(5, agmt_rev_no);
				stmt3.setString(6, cont_rev_no);
				stmt3.setString(7, contract_type);
				stmt3.setString(8, instrument_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					String checked_flg=rset3.getString(2)==null?"":rset3.getString(2);
					if(checked_flg.equals("Y"))
					{
						VCR_DR_CRITERIA.add("");
					}
					else
					{
						VCR_DR_CRITERIA.add(rset3.getString(1)==null?"":rset3.getString(1));
					}
				}
				else
				{
					VCR_DR_CRITERIA.add("");
				}
				rset3.close();
				stmt3.close();
				
				if(count>0)
				{
					is_inv_gen="Y";
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

	public void getProductMst() 
	{
		String function_nm="getProductMst()";
		try
		{
			queryString="SELECT PROD_TYPE,CURVE_TYPE "
					+ "FROM FMS_PROD_CURVE_MAP "
					+ "ORDER BY PROD_TYPE";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VPROD_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
				VCURVE_TYPE.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getHedgeContractList()
	{
		String function_nm="getHedgeContractList()";
		try
		{
			int count=0;
			queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME,"
					+ "CONT_NAME,CONT_STATUS,CONT_REF_NO,CONTRACT_TYPE "
					+ "FROM FMS_DERV_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND A.CONT_STATUS NOT IN ('C','X','T') AND SYSDATE > (SELECT MAX(C.PRICE_END_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) "
						+ "AND TO_DATE(?,'DD/MM/YYYY')>=(SELECT MIN(C.PRICE_START_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) "
						+ "AND TO_DATE(?,'DD/MM/YYYY')<=(SELECT MAX(C.PRICE_END_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) ";
				queryString +="UNION ALL "
						+"SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),SIGNING_TIME,"
						+ "CONT_NAME,CONT_STATUS,CONT_REF_NO,CONTRACT_TYPE "
						+ "FROM FMS_DERV_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "; 
				if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
				{
					queryString += "AND COUNTERPARTY_CD=?";
				}
					queryString += "AND A.CONT_STATUS IN ('C','X','T') AND SYSDATE <= (SELECT MAX(C.PRICE_END_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) "
							+ "AND TO_DATE(?,'DD/MM/YYYY')>=(SELECT MIN(C.PRICE_START_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) "
							+ "AND TO_DATE(?,'DD/MM/YYYY')<=(SELECT MAX(C.PRICE_END_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE) ";
			}
			else
			{
				queryString += "AND A.CONT_STATUS NOT IN ('C','X','T') AND SYSDATE <= (SELECT MAX(C.PRICE_END_DT) FROM FMS_DERV_INSTRUMENT_MST C WHERE A.COMPANY_CD=C.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE)";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, contract_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!active_status.equals("") && active_status.equals("N"))
			{
				stmt.setString(++count, to_dt);
				stmt.setString(++count, from_dt);
				
				stmt.setString(++count, comp_cd);
				stmt.setString(++count, contract_type);
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
				VTRADE_CONF_DT.add(rset.getString(7)==null?"":rset.getString(7));
				VTRADE_CONF_TIME.add(rset.getString(8)==null?"":rset.getString(8));
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				String cont_status_flg=rset.getString(10)==null?"":rset.getString(10);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+utilBean.ContStatusName(cont_status_flg));
				
				String cont_ref=rset.getString(11)==null?"":rset.getString(11);
				VCONT_REF_NO.add(cont_ref);
				String cont_type = rset.getString(12)==null?"":rset.getString(12);
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
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_DERV_CONT_PLANT A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
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
	
	public void getCnBillingDetail()
	{
		String function_nm="getCnBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_DERV_CONT_BILLING_DTL A "
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
							+ "FROM FMS_DERV_CONT_BILLING_DTL "
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
								+ "FROM FMS_DERV_CONT_BILLING_DTL A "
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
						+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
						+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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
								+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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
									+ "FROM FMS_DERV_AGMT_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
									+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_DERV_AGMT_BILLING_DTL B WHERE "
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

	public void setInstrument_number(String instrument_number) {this.instrument_number = instrument_number;}

	
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
	String from_dt = "";
	String to_dt = "";
	String display_msg ="";
	String max_end_dt ="";
	String cont_start_dt = "";
	String active_status = "";
	
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setInstrument_no(String instrument_no) {this.instrument_no = instrument_no;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_FULL_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	
	Vector VAGMT_REF_NO = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VAGMT_TYP = new Vector();
	Vector VAGMT_STATUS = new Vector();
	
	Vector VBUYER_CD = new Vector();
	Vector VBUYER_NAME = new Vector();
	Vector VBUYER_ABBR = new Vector();
	
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_TRANS_CD = new Vector();
	Vector VSEL_TRANS_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_TRAD_CD = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_SAP_CODE = new Vector();
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VPLANT_SEQ = new Vector();
	
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VTRADE_CONF_DT = new Vector();
	Vector VTRADE_CONF_TIME = new Vector();

	Vector VINSTRUMENT_NO = new Vector();
	Vector VDISP_INSTRUMENT_NO = new Vector();
	Vector VINSTRUMENT_TYPE = new Vector();
	Vector VINSTRUMENT_BUY_SELL = new Vector();
	Vector VINSTRUMENT_STATUS_FLG = new Vector();
	Vector VINSTRUMENT_STATUS = new Vector();
	Vector VINSTRUMENT_QTY = new Vector();
	Vector VINSTRUMENT_QTY_UNIT = new Vector();
	Vector VINSTRUMENT_RATE = new Vector();
	Vector VINSTRUMENT_RATE_UNIT = new Vector();
	Vector VINSTRUMENT_RATE_UNIT_FLG = new Vector();
	Vector VPRICE_START_DT = new Vector();
	Vector VPRICE_END_DT = new Vector();
	Vector VPRODUCT_NM = new Vector();
	Vector VCURVE_NM = new Vector();
	Vector VPROJ_METHOD = new Vector();
	Vector VCONT_DD_MM_YR = new Vector();
	Vector VCONVERSION_FACTOR = new Vector();
	Vector VCONVERSION_FACTOR_UNIT = new Vector();
	
	Vector VCONT_NAME = new Vector();
	Vector VCONT_DISP_NAME = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VCONT_REF_NO = new Vector();

	Vector VPROD_TYPE = new Vector();
	Vector VCURVE_TYPE = new Vector();
	
	Vector VHEDGE_DT = new Vector();
	Vector VBALANCE_QTY = new Vector();
	Vector VGEN_INV_INSTRUMENT_NO = new Vector();
	Vector VGEN_PDF_INV_INSTRUMENT_NO = new Vector();
	Vector VCR_DR_CRITERIA = new Vector();
	
	public Vector getVHEDGE_DT() {return VHEDGE_DT;}
	public Vector getVBALANCE_QTY() {return VBALANCE_QTY;}
	public Vector getVPROD_TYPE() {return VPROD_TYPE;}
	public Vector getVCURVE_TYPE() {return VCURVE_TYPE;}
	
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_DISP_NAME() {return VCONT_DISP_NAME;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	
	public Vector getVINSTRUMENT_NO() {return VINSTRUMENT_NO;}
	public Vector getVDISP_INSTRUMENT_NO() {return VDISP_INSTRUMENT_NO;}
	public Vector getVINSTRUMENT_TYPE() {return VINSTRUMENT_TYPE;}
	public Vector getVINSTRUMENT_BUY_SELL() {return VINSTRUMENT_BUY_SELL;}
	public Vector getVINSTRUMENT_STATUS_FLG() {return VINSTRUMENT_STATUS_FLG;}
	public Vector getVINSTRUMENT_STATUS() {return VINSTRUMENT_STATUS;}
	public Vector getVINSTRUMENT_QTY() {return VINSTRUMENT_QTY;}
	public Vector getVINSTRUMENT_QTY_UNIT() {return VINSTRUMENT_QTY_UNIT;}
	public Vector getVINSTRUMENT_RATE() {return VINSTRUMENT_RATE;}
	public Vector getVINSTRUMENT_RATE_UNIT() {return VINSTRUMENT_RATE_UNIT;}
	public Vector getVINSTRUMENT_RATE_UNIT_FLG() {return VINSTRUMENT_RATE_UNIT_FLG;}
	public Vector getVPRICE_START_DT() {return VPRICE_START_DT;}
	public Vector getVPRICE_END_DT() {return VPRICE_END_DT;}
	public Vector getVPRODUCT_NM() {return VPRODUCT_NM;}
	public Vector getVCURVE_NM() {return VCURVE_NM;}
	public Vector getVPROJ_METHOD() {return VPROJ_METHOD;}
	public Vector getVCONT_DD_MM_YR() {return VCONT_DD_MM_YR;}
	public Vector getVCONVERSION_FACTOR() {return VCONVERSION_FACTOR;}
	public Vector getVCONVERSION_FACTOR_UNIT() {return VCONVERSION_FACTOR_UNIT;}

	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_FULL_NO() {return VAGMT_FULL_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVAGMT_TYP() {return VAGMT_TYP;}
	public Vector getVAGMT_STATUS() {return VAGMT_STATUS;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVAGMT_REF_NO() {return VAGMT_REF_NO;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_TRANS_CD() {return VSEL_TRANS_CD;}
	public Vector getVSEL_TRANS_PLANT_SEQ_NO() {return VSEL_TRANS_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_TRAD_CD() {return VSEL_TRAD_CD;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	
	public Vector getVBUYER_CD() {return VBUYER_CD;}
	public Vector getVBUYER_NAME() {return VBUYER_NAME;}
	public Vector getVBUYER_ABBR() {return VBUYER_ABBR;}
	
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVTAX_SAP_CODE() {return VTAX_SAP_CODE;}
	
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVTRADE_CONF_DT() {return VTRADE_CONF_DT;}
	public Vector getVTRADE_CONF_TIME() {return VTRADE_CONF_TIME;}
	public Vector getVGEN_INV_INSTRUMENT_NO() {return VGEN_INV_INSTRUMENT_NO;}
	public Vector getVGEN_PDF_INV_INSTRUMENT_NO() {return VGEN_PDF_INV_INSTRUMENT_NO;}
	public Vector getVCR_DR_CRITERIA() {return VCR_DR_CRITERIA;}
	
	String min_counterparty_eff_dt = "";
	//String cont_no = "";
	//String cont_rev_no = "";
	String cont_ref_no = "";
	String trade_ref_no = "";
	String trade_dt = "";
	String trade_time = "";
	String signing_dt = "";
	String agmt_signing_dt = "";
	String signing_time = "";
	String dda_dt = "";
	String dda_time = "";
	String agmt_trade_dt = "";
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
	String conversion_factor = "";
	String conversion_factor_unit = "";

	String cont_name = "";
	String cont_disp_name = "";
	String contpty_abbr="";
	String contpty_name="";
	String mapped_cont_no="";
	String fcc_flg="";
	String cont_status="";

	String cont_status_flg="";
	String billing_freq="";
	String billing_flag="";
	String billing_clause = "";

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
	String sec_category_value = "";
	
	String spec_gas_clause = "";
	String measurement_clause = "";
	String demurrage_clause = "";
	String day_def_clause = "";
	
	String no_of_instrument_dtl = "";
	String instrument_number = "";
	String show_instru_number = "";
	String instrument_no = "";
	String instrument_ref = "";
	String instrument_status = "";
	String instrument_start_dt = "";
	String instrument_end_dt = "";
	String instrument_volume = "";
	String instrument_agmt_base = "";
	String instrument_price = "";
	String instrument_price_unit = "";
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
	String instrument_details_notes = "";
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
	String is_inv_gen="";
	String closure_note="";
	
	public void setCargo_no(String instrument_no) {this.instrument_no = instrument_no;}
	
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
	public String getTrade_dt() {return trade_dt;}
	public String getTrade_time() {return trade_time;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getAgmt_trade_dt() {return agmt_trade_dt;}
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
	public String getSec_category_value() {return sec_category_value;}
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

	public String getAgreement_type() {return agreement_type;}
	public String getClearance() {return clearance;}
	public String getCounterparty_cd() {return counterparty_cd;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getAgmt_no() {return agmt_no;}
	public String getAgmt_rev_no() {return agmt_rev_no;}
	public String getContract_type() {return contract_type;}
	public String getStatus() { return status; }
	public String getStatus_nm() { return status_nm; }

	public String getAgmt_ref_no() { return agmt_ref_no; }
	public String getAgmt_name() { return agmt_name; }
	public String getAgmt_typ() { return agmt_typ; }
	public String getRev_dt() { return rev_dt; }
	public String getConversion_factor() { return conversion_factor; }
	public String getConversion_factor_unit() { return conversion_factor_unit; }

	public String getDay_def_clause() {return day_def_clause;}
	public String getDemurrage_clause() {return demurrage_clause;}
	public String getMeasurement_clause() {return measurement_clause;}
	public String getSpec_gas_clause() {return spec_gas_clause;}
	public void setAgmt_type(String agmt_type) {this.agmt_type = agmt_type;}
	
	public String getNo_of_instrument_dtl() {return no_of_instrument_dtl;}
	public String getInstrument_number() {return instrument_number;}
	public String getShow_instru_number() {return show_instru_number;}
	public String getInstrument_no() {return instrument_no;}
	public String getInstrument_ref() {return instrument_ref;}
	public String getInstrument_status() {return instrument_status;}
	public String getInstrument_start_dt() {return instrument_start_dt;}
	public String getInstrument_end_dt() {return instrument_end_dt;}
	public String getInstrument_volume() {return instrument_volume;}
	public String getInstrument_agmt_base() {return instrument_agmt_base;}
	public String getInstrument_price() {return instrument_price;}
	public String getInstrument_price_unit() {return instrument_price_unit;}
	
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
	public String getInstrument_details_notes() {return instrument_details_notes;}
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
	public String getIs_inv_gen() {return is_inv_gen;}
	public String getClosure_note() {return closure_note;}
}
