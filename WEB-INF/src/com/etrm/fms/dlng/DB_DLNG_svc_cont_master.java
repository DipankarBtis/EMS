package com.etrm.fms.dlng;

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

//Coded By          : Arth Patel
//Code Reviewed by	:  
//CR Date			: 01/02/2025
//Status	  		: Developing

public class DB_DLNG_svc_cont_master 
{
	String db_src_file_name="DB_DLNG_svc_cont_master.java";
	
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
	UtilBean_DLNG utilBean_dlng = new UtilBean_DLNG();
	DateUtil dateUtil = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	String clearance = "KYC";
	
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
	    			if(callFlag.equalsIgnoreCase("TMSA_SVC_AGMT"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				if(opration.equals("INSERT")) 
	    				{
	    					getCustomerCounterpartyList();
	    				}
	    				else
	    				{
	    					getCounterpartywithAgmtList();
	    				}
	    				getCounterpartyPlantList();
	    				getBusinessPlantList();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getTMSAAgreementDetail();
	    					getSelectedCustomerPlantList();
	    					getSelectedBusinessPlantList();
	    					getCountAgmtBillingDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("TMSA_AGREEMENT_LIST"))
	    			{
	    				getTMSAAgreementList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TMSA_BILLING_DTL"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				display_agmt_id=utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);
	    				getStateMst();
	    				getSelectedAgmtPlantlist();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getTMSAAgreementBillingDetail();
	    				//getTMSAAgreementApplicableTaxes(); Applicable tax part pending for INVOICE_TYPE for TMSA
	    			}
	    			else if(callFlag.equalsIgnoreCase("SO_SVC_CONT"))
	    			{
	    				contpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				contpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
	    				mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
	    				
	    				getSelectedAgreementDetail();
	    				getCustomerCounterpartyList();
	    				getCounterpartyPlantList();
	    				getBusinessPlantList();
	    				getFillingMasterList();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getSOContractDetail();
	    					getCountContBillingDetail();
	    					getCountSecurityDetail();
	    					getSelectedContBusinessPlantList();
	    					getSalesContDtlForServiceContract();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SVC_CONTRACT_LIST"))
	    			{
	    				getSvcContractList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SVC_CONTRACT_BILLING_DTL"))
	    			{
	    				contpty_abbr = utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				mapped_cont_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
	    				getStateMst();
	    				getSelectedContPlantlist();
	    				getCountContBillingDetail();
	    				getExchangeRateMaster();
	    				getInterestRateMaster();
	    				getSvcContractBillingDetail();
	    				//getSvcContractApplicableTaxes();  Applicable tax part pending for INVOICE_TYPE for Service Order
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_CONT_LIST"))
	    			{
	    				getSalesContList();
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
	
	public void getSelectedContPlantlist() 
	{
		String function_nm="getSelectedContPlantlist()";
		try
		{
			String queryString="SELECT A.PLANT_SEQ_NO "
					+ "FROM FMS_SVC_CONT_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
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
	
	public void getSelectedAgmtPlantlist() 
	{
		String function_nm="getSelectedAgmtPlantlist()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_SVC_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_PLANT B WHERE "
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
	
	public void getSalesContDtlForServiceContract()
	{
		String function_nm="getSalesContDtlForServiceContract()";
		try
		{
			String queryString="SELECT CUSTOMER_CD,SELL_CONT_MAP "
					+ "FROM FMS_SVC_CONT_MAP "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
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
				String queryString1="SELECT CONT_REF_NO,TRADE_REF_NO "
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
				
				String deal_no=utilBean.NewDealMappingId(comp_cd,customer_cd,agmtno, agmt_revno, contno, cont_revno, contType,"");
				
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
	
	public void getSalesContList()
	{
		String function_nm="getSalesContList()";
		try
		{
			int selCnt =0;
			
			String queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CONT_REF_NO,A.TRADE_REF_NO,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),TCQ "
					+ ""
					+ "FROM FMS_SUPPLY_CONT_MST A,FMS_SUPPLY_CONT_PLANT D,FMS_SUPPLY_CONT_BU E  "
					+ ""
					+ "WHERE A.COMPANY_CD=? AND A.AGMT_BASE=? AND A.COUNTERPARTY_CD=? AND A.CONTRACT_TYPE IN (?,?,?) "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)"
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
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, "D");
			stmt.setString(++selCnt, counterparty_cd);
			stmt.setString(++selCnt, "F");
			stmt.setString(++selCnt, "E");
			stmt.setString(++selCnt, "W");
			stmt.setString(++selCnt, to_dt);
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, counterparty_plant_seq);
			stmt.setString(++selCnt, bu_unit);
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
				
				if(cont_type.equals("W"))
				{
					cont_ref=trade_ref;
				}
				
				String deal_no=utilBean.NewDealMappingId(comp_cd,counterparty_cd,agmt, agmt_rev, cont, cont_rev, cont_type,"");
				
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
				String queryString1 = "SELECT FILL_STATION_CD "
			  			+ "FROM FMS_SUPPLY_CONT_FILLING_STN "
			  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
			  			+ "AND CONT_NO=? AND CONT_REV=? "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt1=conn.prepareStatement(queryString1);
			  	stmt1.setString(1, comp_cd);
			  	stmt1.setString(2, agmt);
			  	stmt1.setString(3, agmt_rev);
			  	stmt1.setString(4, cont);
			  	stmt1.setString(5, cont_rev);
			  	stmt1.setString(6, counterparty_cd);
			  	stmt1.setString(7, cont_type);
			  	rset1=stmt1.executeQuery();
	  			while(rset1.next())
	  			{
	  				String fillCd = rset1.getString(1)==null?"0":rset1.getString(1);
	  				if(DelvNm.equals(""))
	  				{
	  					DelvNm+=""+utilBean_dlng.getFillStationName(conn,fillCd);
	  				}
	  				else
	  				{
	  					DelvNm+=", "+utilBean_dlng.getFillStationName(conn,fillCd);
	  				}
	  			}
	  			rset1.close();
	  			stmt1.close();
				
	  			String BuNm="";
	  			String queryString2 = "SELECT PLANT_SEQ_NO "
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
			  	stmt2.setString(6, counterparty_cd);
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
	  			String queryString3 = "SELECT PLANT_SEQ_NO "
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
			  	stmt3.setString(6, counterparty_cd);
			  	stmt3.setString(7, cont_type);
			  	rset3=stmt3.executeQuery();
	  			while(rset3.next())
	  			{
	  				String plant_seq = rset3.getString(1)==null?"0":rset3.getString(1);
	  				
	  				if(CustPlantNm.equals(""))
	  				{
	  					CustPlantNm+=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
	  				}
	  				else
	  				{
	  					CustPlantNm+=", "+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
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
	
	public void getSvcContractBillingDetail()
	{
		String function_nm="getSvcContractBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_SVC_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? "
					//+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_BILLING_DTL B "
					//+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					//+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SVC_CONT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
							+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_SVC_CONT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SVC_CONT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
						
						exchg_val=nf2.format(rset.getDouble(17));
						
						due_dt_in=rset.getString(15)==null?"":rset.getString(15);
						exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
						
						billing_days=rset.getString(18)==null?"":rset.getString(18);
						
						sat_days=rset.getString(19)==null?"":rset.getString(19);
						plant_seq=rset.getString(20)==null?"":rset.getString(20);
						old_eff_dt=rset.getString(21)==null?"":rset.getString(21);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_SVC_CONT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SVC_CONT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
						
						queryString1="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
								+ "FROM FMS_DLNG_SVC_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INV_FLAG=? ";
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
				if(contract_type.equals("B"))
				{
					int count1=0;
					 queryString3="SELECT COUNT(*) "
							+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND AGMT_TYPE=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
							String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
									+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,"
									+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
									+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_TYPE=? AND AGMT_NO=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
									+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, agreement_type);
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
								
								exchg_val=nf2.format(rset.getDouble(17));
								billing_days = rset.getString(18)==null?"":rset.getString(18);
								
								sat_days=rset.getString(19)==null?"":rset.getString(19);
								plant_seq=rset.getString(20)==null?"":rset.getString(20);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
								
								String state_map="";
								String state_nm = "";
								String queryString2="SELECT HOLIDAY_STATE "
										+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
										+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
	
	/*
	 * HM20251015 : Before New Effective Date Changes.
	 * public void getSvcContractBillingDetail()
	{
		String function_nm="getSvcContractBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_SVC_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_BILLING_DTL B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
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
			
			if(count>0)
			{
				for(int k=0; k<VSELECTED_PLANT_SEQ.size(); k++)
				{
					String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
							+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_SVC_CONT_BILLING_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, contract_type);
					stmt.setString(6, ""+VSELECTED_PLANT_SEQ.elementAt(k));
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
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_SVC_CONT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
								+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SVC_CONT_BILLING_DTL B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
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
				if(contract_type.equals("B"))
				{
					int count1=0;
					 queryString3="SELECT COUNT(*) "
							+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
							+ "AND AGMT_TYPE=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
							String queryString="SELECT BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
									+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,"
									+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
									+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_TYPE=? AND AGMT_NO=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
									+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, agreement_type);
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
								
								exchg_val=nf2.format(rset.getDouble(17));
								billing_days = rset.getString(18)==null?"":rset.getString(18);
								
								sat_days=rset.getString(19)==null?"":rset.getString(19);
								plant_seq=rset.getString(20)==null?"":rset.getString(20);
								String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
								
								String state_map="";
								String state_nm = "";
								String queryString2="SELECT HOLIDAY_STATE "
										+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
										+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
	}*/
	
	public void getSelectedContBusinessPlantList() 
	{
		String function_nm="getSelectedContBusinessPlantList()";
		try
		{
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_SVC_CONT_BU "
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSvcContractList()
	{
		String function_nm="getSvcContractList()";
		try
		{
			String temp_cont="";
			if(contract_type.equals(""))
			{
				temp_cont="CONTRACT_TYPE IN ('B','M')";
			}
			
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
					+ "IS_ALLOCATED,CONTRACT_TYPE "
					+ "FROM FMS_SVC_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SVC_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_STATUS,FCC_FLAG,CONT_REF_NO,"
						+ "IS_ALLOCATED,CONTRACT_TYPE "
						+ "FROM FMS_SVC_CONT_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SVC_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
			String temp_qString=queryString;
			stmt = conn.prepareStatement(temp_qString);
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
				String cont_type=rset.getString(14)==null?"":rset.getString(14);
				
				String start_dt=rset.getString(7)==null?"":rset.getString(7);
				String end_dt=rset.getString(8)==null?"":rset.getString(8);
				
				VBUYER_CD.add(countpty_cd);
				VBUYER_NAME.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				String cont_status_flg=rset.getString(10)==null?"":rset.getString(10);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
				VFCC_FLAG.add(rset.getString(11)==null?"":rset.getString(11));
				
				VIS_ALLOCATED.add(rset.getString(13)==null?"":rset.getString(13));
				
				String cont_ref=rset.getString(12)==null?"":rset.getString(12);
				VCONT_REF_NO.add(cont_ref);
				
				String dealMapping=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				VCONTRACT_TYPE.add(cont_type);
				VDEAL_MAPPING.add(dealMapping);
				
				//VSUPPLIED_MMBTU.add(""+allocUtil.getBestSupplyAllocationQty(conn, own_cd, countpty_cd, agmt, cont, cont_type,start_dt,end_dt,agmt_base));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFillingMasterList() 
	{
		String function_nm="getFillingMasterList()";
		try
		{
			String queryString = "SELECT FILL_STATION_CD,FILL_STATION_NAME,FILL_STATION_ABBR "
					+ "FROM FMS_FILLING_STATION_MST "
					+ "WHERE "
					//+ "COMPANY_CD=? AND "
					+ "ACTIVE_FLAG=?";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			stmt.setString(1, "Y");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String fill_station_cd = rset.getString(1)==null?"":rset.getString(1);
				String fill_station_nm = rset.getString(2)==null?"":rset.getString(2);
				String fill_station_abbr = rset.getString(3)==null?"":rset.getString(3);
				
				VFILL_STATION_CD.add(fill_station_cd);
				VFILL_STATION_NM.add(fill_station_nm);
				VFILL_STATION_ABBR.add(fill_station_abbr);
			}
			stmt.close();
			rset.close();
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
					+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "//AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO ";//AND A.GX=B.GX";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			//stmt.setString(6, gx);
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
	
	public void getSOContractDetail() 
	{
		String function_nm="getSupplyContractDetail()";
		try
		{
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONT_REV,CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
					+ "SIGNING_TIME,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),DCQ,"
					+ "DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DAY_DEF_CLAUSE,MMCQ_FLAG,MMCQ_PERCENTAGE,MMCQ_CLAUSE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),CONT_NAME,FCC_FLAG,CONT_STATUS,IS_ALLOCATED,"
					+ "TO_CHAR(DDA_DT,'DD/MM/YYYY'),DDA_TIME,CHANGE_DATE_REQ, "
					+ "BILLING_FLAG,BILLING_CLAUSE,FILL_STATION_CD,PLANT_SEQ_NO,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,LAYOVER_CHARGE_INR,"
					+ "LAYOVER_HRS,TRANSPORT_MGMT_CHARGE,TRANSPORT_MGMT_UNIT,TO_CHAR(EFF_DT,'DD/MM/YYYY'),QTY_OPTION,QTY_OPTION_FIRM,QTY_OPTION_RE "
					+ "FROM FMS_SVC_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt = conn.prepareStatement(queryString);
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
				dealMapping=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
				//cont_no=rset.getString(3)==null?"":rset.getString(3);
				//cont_rev_no=rset.getString(4)==null?"":rset.getString(4);
				cont_ref_no=rset.getString(5)==null?"":rset.getString(5);
				signing_dt=rset.getString(6)==null?"":rset.getString(6);
				signing_time=rset.getString(7)==null?"":rset.getString(7);
				start_dt=rset.getString(8)==null?"":rset.getString(8);
				end_dt=rset.getString(9)==null?"":rset.getString(9);
				dcq=rset.getString(10)==null?"":rset.getString(10);
				day_def_flag=rset.getString(11)==null?"":rset.getString(11);
				day_start_time=rset.getString(12)==null?"":rset.getString(12);
				day_end_time=rset.getString(13)==null?"":rset.getString(13);
				day_def_clause=rset.getString(14)==null?"":rset.getString(14);
				mmcq_flag=rset.getString(15)==null?"":rset.getString(15);
				mmcq_percentage=rset.getString(16)==null?"":rset.getString(16);
				mmcq_clause=rset.getString(17)==null?"":rset.getString(17);
				String deal_ent_dt=rset.getString(18)==null?"":rset.getString(18);
				cont_name=rset.getString(19)==null?"":rset.getString(19);
				fcc_flg=rset.getString(20)==null?"":rset.getString(20);
				cont_status_flg=rset.getString(21)==null?"":rset.getString(21);
				cont_status=""+ContStatusName(cont_status_flg);
				is_allocated=rset.getString(22)==null?"N":rset.getString(22);
				dda_dt=rset.getString(23)==null?"":rset.getString(23);
				dda_time=rset.getString(24)==null?"":rset.getString(24);
				contdt_change_request_flag=rset.getString(25)==null?"":rset.getString(25);
				billing_flag = rset.getString(26)==null?"":rset.getString(26);
				billing_clause = rset.getString(27)==null?"":rset.getString(27);
				
				fill_station_cd = rset.getString(28)==null?"":rset.getString(28);
				plant_seq_no = rset.getString(29)==null?"":rset.getString(29);
				alw_laytime_hrs = rset.getString(30)==null?"":rset.getString(30);
				alw_laytime_min = rset.getString(31)==null?"":rset.getString(31);
				layover_charge_inr = rset.getString(32)==null?"":rset.getString(32);
				layover_hrs = rset.getString(33)==null?"":rset.getString(33);
				transport_mgmt_charge = rset.getString(34)==null?"":rset.getString(34);
				transport_mgmt_unit = rset.getString(35)==null?"":rset.getString(35);
				transport_mgmt_eff_dt = rset.getString(36)==null?"":rset.getString(36);
				qty_opt = rset.getString(37)==null?"":rset.getString(37);
				qty_opt_firm = rset.getString(38)==null?"":rset.getString(38);
				qty_opt_re = rset.getString(39)==null?"":rset.getString(39);
				
				
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
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TO_CHAR(REV_DT,'DD/MM/YYYY'),"
					+ "STATUS,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
					+ "MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),AGMT_NAME,AGMT_REF_NO,BILLING_FLAG,"
					+ "BILLING_CLAUSE "
					+ "FROM FMS_AGMT_SVC_MST "
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
				agmt_signing_dt=rset.getString(3)==null?"":rset.getString(3);
				agmt_start_dt=rset.getString(4)==null?"":rset.getString(4);
				agmt_end_dt=rset.getString(5)==null?"":rset.getString(5);
				rev_dt=rset.getString(6)==null?"":rset.getString(6);
				status=rset.getString(7)==null?"":rset.getString(7);
				status_nm=""+AgmtStatusName(status);
				day_def_flag=rset.getString(8)==null?"":rset.getString(8);
				day_def_clause=rset.getString(9)==null?"":rset.getString(9);
				day_start_time=rset.getString(10)==null?"":rset.getString(10);
				day_end_time=rset.getString(11)==null?"":rset.getString(11);
				mmcq_flag=rset.getString(12)==null?"":rset.getString(12);
				mmcq_clause=rset.getString(13)==null?"":rset.getString(13);
				mmcq_percentage=rset.getString(14)==null?"":rset.getString(14);
				String deal_ent_dt=rset.getString(15)==null?"":rset.getString(15);
				cont_name=rset.getString(16)==null?"":rset.getString(16);
				//cont_ref_no=rset.getString(17)==null?"":rset.getString(17);
				bill_flag=rset.getString(18)==null?"":rset.getString(18);
				billing_clause=rset.getString(19)==null?"":rset.getString(19);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTMSAAgreementBillingDetail()
	{
		String function_nm="getTMSAAgreementBillingDetail()";
		try
		{
			int count=0;
			String queryString3="SELECT COUNT(*) "
					+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND AGMT_TYPE=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
							+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO "
							+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
							+ "A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO)";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agreement_type);
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
						
						exchg_val=nf2.format(rset.getDouble(17));
						billing_days = rset.getString(18)==null?"":rset.getString(18);
						
						sat_days=rset.getString(19)==null?"":rset.getString(19);
						plant_seq=rset.getString(20)==null?"":rset.getString(20);
						String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
						
						String state_map="";
						String state_nm = "";
						String queryString2="SELECT HOLIDAY_STATE "
								+ "FROM FMS_AGMT_SVC_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND AGMT_TYPE=? AND PLANT_SEQ_NO=? AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_AGMT_SVC_BILLING_DTL B WHERE "
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
	
	public void getTMSAAgreementList()
	{
		String function_nm="getTMSAAgreementList()";
		try
		{
			int count=0;
			String queryString="SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NAME,STATUS,AGMT_REF_NO,"
					+ "AGMT_TYPE "
					+ "FROM FMS_AGMT_SVC_MST A "
					+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
					+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_SVC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "; 
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=?";
			}
			/*
			 * if(!contract_type.equals("")) { queryString += "AND AGMT_TYPE=?"; }
			 */
			if(!active_status.equals("") && active_status.equals("N"))
			{
				queryString += "AND END_DT < SYSDATE AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				queryString +="UNION ALL "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_NAME,STATUS,AGMT_REF_NO,"
						+ "AGMT_TYPE "
						+ "FROM FMS_AGMT_SVC_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_SVC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, agreement_type);
			if(!counterparty_cd.equals("") && !counterparty_cd.equals("0"))
			{
				stmt.setString(++count, counterparty_cd);
			}
			/*
			 * if(!contract_type.equals("")) { stmt.setString(4, contract_type); }
			 */
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
				String agmtType =rset.getString(10)==null?"":rset.getString(10);
				String disp_agmt_no = utilBean.NewAgmtMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, agreement_type);
				VDISP_AGMT_NO.add(disp_agmt_no);
				
				if(agmtType.equals("T"))
				{
					VAGMT_TYPE.add("DLNG Transport Service");
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
	
	public void getCountContBillingDetail()
	{
		String function_nm="getCountContBillingDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_SVC_CONT_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SVC_CONT_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO)";
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
	
	public void getCountAgmtBillingDetail()
	{
		String function_nm="getCountAgmtBillingDetail()";
		try
		{
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_AGMT_SVC_BILLING_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "
					+ "AND AGMT_TYPE=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agreement_type);
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
	
	public void getSelectedBusinessPlantList()
	{
		String function_nm="getSelectedBusinessPlantList()";
		try
		{
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_SVC_BU "
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
	
	public void getSelectedCustomerPlantList()
	{
		String function_nm="getSelectedCustomerPlantList()";
		try
		{
			String queryString="SELECT PLANT_SEQ_NO "
					+ "FROM FMS_AGMT_SVC_PLANT "
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
				VSEL_CUST_CD.add(counterparty_cd);
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
	
	public void getTMSAAgreementDetail()
	{
		String function_nm="getTMSAAgreementDetail()";
		try
		{
			min_counterparty_eff_dt = utilBean.getMinEffectiveDateOfCounterparty(conn,counterparty_cd);
			
			if(!agmt_rev_no.equals("")) {
				String queryString="SELECT TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI') "
						+ "FROM FMS_AGMT_SVC_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agreement_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, "0");
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
					+ "STATUS,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
					+ "MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI'),AGMT_NAME,AGMT_REF_NO,BILLING_FLAG,"
					+ "BILLING_CLAUSE "
					+ "FROM FMS_AGMT_SVC_MST "
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
				signing_dt=rset.getString(3)==null?"":rset.getString(3);
				start_dt=rset.getString(4)==null?"":rset.getString(4);
				end_dt=rset.getString(5)==null?"":rset.getString(5);
				rev_dt=rset.getString(6)==null?"":rset.getString(6);
				status=rset.getString(7)==null?"":rset.getString(7);
				status_nm=""+AgmtStatusName(status);
				day_def_flag=rset.getString(8)==null?"":rset.getString(8);
				day_def_clause=rset.getString(9)==null?"":rset.getString(9);
				day_start_time=rset.getString(10)==null?"":rset.getString(10);
				day_end_time=rset.getString(11)==null?"":rset.getString(11);
				mmcq_flag=rset.getString(12)==null?"":rset.getString(12);
				mmcq_clause=rset.getString(13)==null?"":rset.getString(13);
				mmcq_percentage=rset.getString(14)==null?"":rset.getString(14);
				String deal_ent_dt=rset.getString(15)==null?"":rset.getString(15);
				//String split[] = deal_ent_dt.split(" ");
				//ent_dt = split[0];
				//ent_time = split[1];
				cont_name=rset.getString(16)==null?"":rset.getString(16);
				cont_ref_no=rset.getString(17)==null?"":rset.getString(17);
				bill_flag=rset.getString(18)==null?"":rset.getString(18);
				billing_clause=rset.getString(19)==null?"":rset.getString(19);
			}
			rset.close();
			stmt.close();
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
			if(contract_type.equals("B") && callFlag.equalsIgnoreCase("SO_SVC_CONT"))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_SVC_PLANT "
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
					String plant_abbr="";
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
					
					VPLANT_NM.add(plant_abbr);
					VPLANT_SEQ_NO.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
				}
				rset.close();
				stmt.close();
			}
			else
			{
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
	
	public void getBusinessPlantList()
	{
		String function_nm="getBusinessPlantList()";
		try
		{
			if(contract_type.equals("B") && callFlag.equalsIgnoreCase("SO_SVC_CONT"))
			{
				String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_AGMT_SVC_BU "
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
	
	public void getCustomerCounterpartyList()
	{
		String function_nm="getCustomerCounterpartyList()";
		try
		{
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
					+ "FROM FMS_AGMT_SVC_MST "
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
	String from_dt = "";
	String to_dt = "";
	String counterparty_plant_seq = "";
	String bu_unit = "";
	String active_status="";
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setCont_end_dt(String cont_end_dt) {this.cont_end_dt = cont_end_dt;}
	public void setAgreement_type(String agreement_type) {this.agreement_type = agreement_type;}
	
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setCounterparty_plant_seq(String counterparty_plant_seq) {this.counterparty_plant_seq = counterparty_plant_seq;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setActive_status(String active_status) {this.active_status = active_status;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VSEL_CUST_CD = new Vector();
	Vector VSEL_PLANT_ABBR = new Vector();
	Vector VSEL_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VAGMT_STATUS = new Vector();
	Vector VAGMT_STATUS_FLG = new Vector();
	Vector VAGMT_REF_NO = new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VDISP_AGMT_NO = new Vector();
	Vector VAGMT_NAME = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VINT_RATE_CD = new Vector();
	Vector VINT_RATE_NM = new Vector();
	Vector VFILL_STATION_CD = new Vector();
	Vector VFILL_STATION_NM = new Vector();
	Vector VFILL_STATION_ABBR = new Vector();
	Vector VBUYER_CD = new Vector();
	Vector VBUYER_NAME = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VFCC_FLAG = new Vector();
	Vector VIS_ALLOCATED = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VDEAL_MAPPING = new Vector();
	Vector VBU_POINT = new Vector();
	Vector VCUST_PLANT_POINT = new Vector();
	Vector VDIS_DEAL_NO = new Vector();
	Vector VTCQ = new Vector();
	Vector VDELV_POINT = new Vector();
	
	Vector VSELECTED_PLANT_SEQ = new Vector();
	Vector VSELECTED_PLANT_ABBR = new Vector();
	Vector VSTATE_NM=new Vector();
	Vector VSTATE_CODE=new Vector();
	Vector VPLANT_SEQ=new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	public Vector getVSEL_CUST_CD() {return VSEL_CUST_CD;}
	public Vector getVSEL_PLANT_ABBR() {return VSEL_PLANT_ABBR;}
	public Vector getVSEL_PLANT_SEQ_NO() {return VSEL_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVAGMT_STATUS() {return VAGMT_STATUS;}
	public Vector getVAGMT_STATUS_FLG() {return VAGMT_STATUS_FLG;}
	public Vector getVAGMT_REF_NO() {return VAGMT_REF_NO;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVAGMT_NAME() {return VAGMT_NAME;}
	public Vector getVDISP_AGMT_NO() {return VDISP_AGMT_NO;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVINT_RATE_CD() {return VINT_RATE_CD;}
	public Vector getVINT_RATE_NM() {return VINT_RATE_NM;}
	public Vector getVFILL_STATION_CD() {return VFILL_STATION_CD;}
	public Vector getVFILL_STATION_NM() {return VFILL_STATION_NM;}
	public Vector getVFILL_STATION_ABBR() {return VFILL_STATION_ABBR;}
	public Vector getVBUYER_CD() {return VBUYER_CD;}
	public Vector getVBUYER_NAME() {return VBUYER_NAME;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVFCC_FLAG() {return VFCC_FLAG;}
	public Vector getVIS_ALLOCATED() {return VIS_ALLOCATED;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVCUST_PLANT_POINT() {return VCUST_PLANT_POINT;}
	public Vector getVDIS_DEAL_NO() {return VDIS_DEAL_NO;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVDELV_POINT() {return VDELV_POINT;}
	
	public Vector getVSELECTED_PLANT_SEQ() {return VSELECTED_PLANT_SEQ;}
	public Vector getVSELECTED_PLANT_ABBR() {return VSELECTED_PLANT_ABBR;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	
	String contpty_abbr="";
	String contpty_nm="";
	String mapped_cont_no="";
	String min_counterparty_eff_dt = "";
	String ent_dt = "";
	String ent_time = "";
	String signing_dt = "";
	String signing_time = "";
	String start_dt = "";
	String end_dt = "";
	String status="";
	String status_nm="";
	String day_def_flag = "";
	String day_start_time = "";
	String day_end_time = "";
	String mmcq_flag = "";
	String mmcq_percentage = "";
	String mmcq_clause="";
	String cont_name = "";
	String bill_flag="";
	String rev_dt="";
	String billing_clause="";
	String day_def_clause="";
	String cont_ref_no="";
	String no_of_billing_dtl="";
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
	String eff_dt="";
	String old_eff_dt="";
	String exchg_val="";
	String agmt_signing_dt="";
	String agmt_start_dt="";
	String agmt_end_dt="";
	String fcc_flg="";
	String cont_status_flg="";
	String cont_status="";
	String is_allocated="N";
	String dda_dt="";
	String dda_time="";
	String contdt_change_request_flag="";
	String dealMapping="";
	String no_of_security_dtl="";
	String dcq="";
	String fill_station_cd="";
	String plant_seq_no="";
	String alw_laytime_hrs="";
	String alw_laytime_min="";
	String layover_charge_inr="";
	String layover_hrs="";
	String transport_mgmt_charge="";
	String transport_mgmt_unit="";
	String transport_mgmt_eff_dt="";
	String qty_opt="";
	String qty_opt_firm="";
	String qty_opt_re="";
	String customer_cd="";
	String sales_cont_map="";
	String sales_cont_nm="";
	String display_agmt_id="";
	
	String sat_days="";
	String plant_seq="";
	String holiday_state="";
	String disp_holiday_state="";
	
	public String getContpty_abbr() {return contpty_abbr;}
	public String getContpty_nm() {return contpty_nm;}
	public String getMapped_cont_no() {return mapped_cont_no;}
	public String getMin_counterparty_eff_dt() {return min_counterparty_eff_dt;}
	public String getEnt_dt() {return ent_dt;}
	public String getEnt_time() {return ent_time;}
	public String getSigning_dt() {return signing_dt;}
	public String getSigning_time() {return signing_time;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getStatus() {return status;}
	public String getStatus_nm() {return status_nm;}
	public String getDay_def_flag() {return day_def_flag;}
	public String getDay_start_time() {return day_start_time;}
	public String getDay_end_time() {return day_end_time;}
	public String getMmcq_flag() {return mmcq_flag;}
	public String getMmcq_percentage() {return mmcq_percentage;}
	public String getCont_name() {return cont_name;}
	public String getBill_flag() {return bill_flag;}
	public String getRev_dt() {return rev_dt;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getBilling_clause() {return billing_clause;}
	public String getDay_def_clause() {return day_def_clause;}
	public String getMmcq_clause() {return mmcq_clause;}
	public String getNo_of_billing_dtl() {return no_of_billing_dtl;}
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
	public String getEff_dt() {return eff_dt;}
	public String getOld_eff_dt() {return old_eff_dt;}
	public String getExchg_val() {return exchg_val;}
	public String getAgmt_signing_dt() {return agmt_signing_dt;}
	public String getAgmt_start_dt() {return agmt_start_dt;}
	public String getAgmt_end_dt() {return agmt_end_dt;}
	public String getFcc_flg() {return fcc_flg;}
	public String getCont_status_flg() {return cont_status_flg;}
	public String getCont_status() {return cont_status;}
	public String getIs_allocated() {return is_allocated;}
	public String getDda_dt() {return dda_dt;}
	public String getDda_time() {return dda_time;}
	public String getContdt_change_request_flag() {return contdt_change_request_flag;}
	public String getDealMapping() {return dealMapping;}
	public String getNo_of_security_dtl() {return no_of_security_dtl;}
	public String getDcq() {return dcq;}
	public String getFill_station_cd() {return fill_station_cd;}
	public String getPlant_seq_no() {return plant_seq_no;}
	public String getAlw_laytime_hrs() {return alw_laytime_hrs;}
	public String getAlw_laytime_min() {return alw_laytime_min;}
	public String getLayover_charge_inr() {return layover_charge_inr;}
	public String getLayover_hrs() {return layover_hrs;}
	public String getTransport_mgmt_charge() {return transport_mgmt_charge;}
	public String getTransport_mgmt_unit() {return transport_mgmt_unit;}
	public String getTransport_mgmt_eff_dt() {return transport_mgmt_eff_dt;}
	public String getQty_opt() {return qty_opt;}
	public String getQty_opt_firm() {return qty_opt_firm;}
	public String getQty_opt_re() {return qty_opt_re;}
	public String getCustomer_cd() {return customer_cd;}
	public String getSales_cont_map() {return sales_cont_map;}
	public String getSales_cont_nm() {return sales_cont_nm;}
	public String getDisplay_agmt_id() {return display_agmt_id;}
	
	public String getSat_days() {return sat_days;}
	public String getPlant_seq() {return plant_seq;}
	public String getHoliday_state() {return holiday_state;}
	public String getDisp_holiday_state() {return disp_holiday_state;}
}
