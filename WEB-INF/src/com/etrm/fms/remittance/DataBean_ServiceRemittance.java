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
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 04/09/2024
//Status	  		: Developing
public class DataBean_ServiceRemittance 
{
	String db_src_file_name="DataBean_ServiceRemittance.java";
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
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	double transaction_limit=100000;
	double invoice_limit=30000;
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
	    				VREMITTANCE_LIST_ABBR.add("SUR_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("VESS_HEAD");
	    				VREMITTANCE_LIST_ABBR.add("CHA_PROV_HEAD"); // JD
	    				VREMITTANCE_LIST_ABBR.add("CHA_HEAD"); //JD
	    					    				
	    				VREMITTANCE_LIST_NAME.add("Surveyor Service Remittance");
	    				VREMITTANCE_LIST_NAME.add("Vessel Agent Service Remittance");
	    				VREMITTANCE_LIST_NAME.add("Custom House Agenet Provisional Service Remittance");// JD
	    				VREMITTANCE_LIST_NAME.add("Custom House Agent Service Remittance");//JD
	    				
	    				inv_index=0;
    					getServiceRemittancePreparationList("S","Y","F");
    					VINDEX.add(inv_index);
    					
    					inv_index=0;
    					getServiceRemittancePreparationList("V","A","F");
    					VINDEX.add(inv_index);
    					
    					inv_index=0;
    					getServiceRemittancePreparationList("H","H","P");
    					VINDEX.add(inv_index);
    					
    					inv_index=0;
    					getServiceRemittancePreparationList("H","H","F");
    					VINDEX.add(inv_index);
	    			}
	    			else if(callFlag.equalsIgnoreCase("SERVICE_PAYMENT_DETAIL"))
	    			{
	    				getContactPerson();
	    				getBuContactPerson();
	    				getServiceContractDetail();
	    				InvoiceCalculation();
	    				PartyInvoiceCalculation();
	    				getTcsTdsInvDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("INVOICE_DETAIL"))
	    			{
	    				getInvoiceDetail();
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
	
	public void getServiceRemittancePreparationList(String entity,String contType, String inv_flag)
	{
		String function_nm="getServiceRemittancePreparationList()";
		try
		{
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CONT_NO,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_REF_NO,"
					+ "B.PLANT_SEQ_NO,C.PLANT_SEQ_NO,D.BILLING_FREQ "
					+ "FROM FMS_CARGO_SVC_CONT_MST A, FMS_CARGO_SVC_CONT_BU B, FMS_CARGO_SVC_CONT_SVC_BU C, FMS_CARGO_SVC_BILLING_DTL D "
					+ "WHERE A.COMPANY_CD=? AND A.ENTITY_TYPE=? AND A.CONTRACT_TYPE=? "
					+ "AND A.START_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.END_DT >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.ENTITY_TYPE=B.ENTITY_TYPE AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
					+ "AND A.ENTITY_TYPE=C.ENTITY_TYPE AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO "
					+ "AND A.ENTITY_TYPE=D.ENTITY_TYPE AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO ";
			if(!counterparty_cd.equals("")) 
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, entity);
			stmt.setString(3, contType);
			stmt.setString(4, to_dt);
			stmt.setString(5, from_dt);
			if(!counterparty_cd.equals("")) 
			{
				stmt.setString(6, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{	
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"0":rset.getString(3);
				String cont_st_dt=rset.getString(4)==null?"":rset.getString(4);
				String cont_end_dt=rset.getString(5)==null?"":rset.getString(5);
				String cont_ref_no=rset.getString(6)==null?"":rset.getString(6);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, "0", "0", contno, "0", contType, "");
				String service_cont_mapping=countpty_cd+"-"+contType+"-"+contno;
				
				String bu_plant_seq = rset.getString(7)==null?"":rset.getString(7);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String plant_bu_seq = rset.getString(8)==null?"":rset.getString(8);
				String plant_bu_abbr=utilBean.getCounterpartyBuPlantABBR(conn,countpty_cd, own_cd, plant_bu_seq, entity);
				
				String billingFreq=rset.getString(9)==null?"":rset.getString(9);
				
				String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
				
				queryString1="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
						+ "B.CARGO_NO,B.CARGO_REF,SUM(G.ACT_BOE_QTY),A.AGMT_TYPE,TO_CHAR(F.ACT_ARRV_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CN_MST A, "
							+ "FMS_TRADER_CARGO_MST B, "
							+ "FMS_BUY_CARGO_NOM C,"
							+ "FMS_BUY_CARGO_ALLOC F,"
							+ "FMS_BUY_CARGO_ALLOC_BOE G "
						+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? ";
				if(contType.equals("Y"))
				{
					queryString1+= "AND C.LINKED_SURVEYOR_CONT=? ";
				}
				else if(contType.equals("A"))
				{
					queryString1+= "AND C.LINKED_VAGENT_CONT=? ";
				}
				else
				{
					queryString1+= "AND C.LINKED_CHAGENT_CONT=? ";
				}	
				queryString1+= "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONTRACT_TYPE=C.CONTRACT_TYPE "
						+ "AND A.CONT_NO=C.CONT_NO AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_TYPE=C.AGMT_TYPE AND B.CARGO_NO=C.CARGO_NO "
						+ "AND C.NOM_REV = (SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
						+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND A.CONTRACT_TYPE=F.CONTRACT_TYPE "
						+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.AGMT_TYPE=F.AGMT_TYPE AND B.CARGO_NO=F.CARGO_NO "
						+ "AND F.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE F.COMPANY_CD=B.COMPANY_CD "
						+ "AND F.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND F.CONT_NO=B.CONT_NO AND F.AGMT_NO=B.AGMT_NO "
						+ "AND F.CONTRACT_TYPE=B.CONTRACT_TYPE AND F.CARGO_NO=B.CARGO_NO AND F.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND G.COMPANY_CD=F.COMPANY_CD AND G.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND G.CONTRACT_TYPE=F.CONTRACT_TYPE "
						+ "AND G.CONT_NO=F.CONT_NO AND G.AGMT_NO=F.AGMT_NO AND G.AGMT_TYPE=F.AGMT_TYPE AND G.CARGO_NO=F.CARGO_NO AND G.ALLOC_REV=F.ALLOC_REV "
						+ "AND F.QQ_DT >= TO_DATE(?,'DD/MM/YYYY') AND F.QQ_DT <= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND F.QQ_QTY_MMBTU IS NOT NULL AND G.BU_SEQ=? AND G.ACT_BOE_QTY > 0 "
						+ "GROUP BY A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,B.CARGO_NO,B.CARGO_REF,A.AGMT_TYPE,TO_CHAR(F.ACT_ARRV_DT,'DD/MM/YYYY') ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "Y");
				stmt1.setString(3, service_cont_mapping);
				stmt1.setString(4, cont_st_dt);
				stmt1.setString(5, cont_end_dt);
				stmt1.setString(6, bu_plant_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					inv_index++;
					
					String cargo_countpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String cargo_contno=rset1.getString(2)==null?"":rset1.getString(2);
					String cargo_cont_rev=rset1.getString(3)==null?"":rset1.getString(3);
					String cargo_agmtno=rset1.getString(4)==null?"":rset1.getString(4);
					String cargo_agmt_rev=rset1.getString(5)==null?"":rset1.getString(5);
					String cargo_cont_type=rset1.getString(6)==null?"":rset1.getString(6);
					String cargoNo=rset1.getString(7)==null?"":rset1.getString(7);
					String cargoRef=rset1.getString(8)==null?"":rset1.getString(8);
					
					String cargo_agmt_type=rset1.getString(10)==null?"":rset1.getString(10);
					String cargo_arrival_dt=rset1.getString(11)==null?"":rset1.getString(11);
					
					String cargo_deal_no=utilBean.NewDealMappingId(own_cd, cargo_countpty_cd, cargo_agmtno, cargo_agmt_rev, cargo_contno, cargo_cont_rev, cargo_cont_type, cargoNo);
					
					String mapping_id=cargo_countpty_cd+"-"+cargo_agmt_type+"-"+cargo_agmtno+"-"+cargo_cont_type+"-"+cargo_contno+"-"+cargoNo;
					
					VMAPPING_ID.add(mapping_id);
					VCARGO_DEAL_NO.add(cargo_deal_no);
					VCARGO_REF_NO.add(cargoRef);
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(countpty_abbr);
					VENTITY.add(entity);
					VAGMT_NO.add("0");
					VAGMT_REV_NO.add("0");
					VCONT_NO.add(contno);
					VCONT_REV_NO.add("0");
					//VCARGO_NO.add("");
					//VBOE_NO.add("");
					//VBOE_NM.add("");
					VSTART_DT.add(cont_st_dt);
					VEND_DT.add(cont_end_dt);
					VCONT_REF_NO.add(cont_ref_no);
					VCONTRACT_TYPE.add(contType);
					VDEAL_NO.add(deal_no);

					VPLANT_BU_SEQ.add(plant_bu_seq);
					VPLANT_BU_ABBR.add(plant_bu_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					//VPERIOD_START_DT.add(cont_st_dt);
					//VPERIOD_END_DT.add(cont_end_dt);
					VPERIOD_START_DT.add(cargo_arrival_dt);
					VPERIOD_END_DT.add(cargo_arrival_dt);
					
					VBILLING_FREQ_FLAG.add("10");
					VBILLING_FREQ_NM.add("Delivery Period");
					
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, "0",contType, plant_bu_seq, 
							bu_plant_seq, "10", cargo_arrival_dt, cargo_arrival_dt, inv_flag, inv_title,"","",mapping_id);
				
					VINV_FLAG.add(inv_flag);
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
	
	public void getInvDetailForPreparationList(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt, String inv_flag, String inv_title, String cargo_no, String boe_no,String mapping_id)
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
			
			queryString3="SELECT A.INVOICE_NO,A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,A.INVOICE_SEQ,A.FINANCIAL_YEAR,"
					+ "A.PDF_INV_DTL,A.SAP_APPROVAL,A.SYS_INV_NO "
					+ "FROM FMS_PUR_SG_INV_MST A, FMS_PUR_INV_MERGE_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? AND A.INV_FLAG=? "
					+ "AND A.FREQ=? AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INV_FLAG=B.INV_FLAG "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND B.CONT_MAPPING=? ";
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
			stmt_tmp.setString(++stcount, billing_cycle);
			stmt_tmp.setString(++stcount, period_start_dt);
			stmt_tmp.setString(++stcount, period_end_dt);
			stmt_tmp.setString(++stcount, mapping_id);
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				inv_no=rset_tmp.getString(1)==null?"":rset_tmp.getString(1);
				String chk_flg = rset_tmp.getString(2)==null?"":rset_tmp.getString(2);
				String auth_flg = rset_tmp.getString(3)==null?"":rset_tmp.getString(3);
				String aprv_flg = rset_tmp.getString(4)==null?"":rset_tmp.getString(4);
				inv_seq = rset_tmp.getString(5)==null?"":rset_tmp.getString(5);
				fin_yr = rset_tmp.getString(6)==null?"":rset_tmp.getString(6);
				String pdf_inv_dtl = rset_tmp.getString(7)==null?"":rset_tmp.getString(7);
				sap_approved_flag=rset_tmp.getString(8)==null?"":rset_tmp.getString(8);
				sys_inv_no=rset_tmp.getString(9)==null?"":rset_tmp.getString(9);
				
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

				sts="SG : "+(""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg));
			}
			else
			{
				fin_yr = utilDate.getFinancialYear(period_end_dt);
			}
			rset_tmp.close();
			stmt_tmp.close();
			
			queryString4="SELECT A.INVOICE_NO,A.CHECKED_FLAG,A.AUTHORIZED_FLAG,A.APPROVED_FLAG,"
					+ "A.PDF_INV_DTL,A.SAP_APPROVAL "
					+ "FROM FMS_PUR_PG_INV_MST A, FMS_PUR_INV_MERGE_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? AND A.INV_FLAG=? "
					+ "AND A.FREQ=? AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.INV_FLAG=B.INV_FLAG "
					+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ "
					+ "AND B.CONT_MAPPING=? ";
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
			stmt_tmp1.setString(++stcount, billing_cycle);
			stmt_tmp1.setString(++stcount, period_start_dt);
			stmt_tmp1.setString(++stcount, period_end_dt);
			stmt_tmp1.setString(++stcount, mapping_id);
			rset_tmp1=stmt_tmp1.executeQuery();
			if(rset_tmp1.next())
			{
				String chk_flg = rset_tmp1.getString(2)==null?"":rset_tmp1.getString(2);
				String auth_flg = rset_tmp1.getString(3)==null?"":rset_tmp1.getString(3);
				String aprv_flg = rset_tmp1.getString(4)==null?"":rset_tmp1.getString(4);
				String pdf_inv_dtl = rset_tmp1.getString(5)==null?"":rset_tmp1.getString(5);
				sap_approved_flag=rset_tmp1.getString(6)==null?"":rset_tmp1.getString(6);
				
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
			
			
			queryString5="SELECT FILE_NAME "
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
			}
			else
			{
				VUPLOADED_FILE_NAME.add("");
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
			VPAYMENT_TYPE_FLAG.add(payment_type_flag);
			
			VAPPROVE_FLAG_CHECK.add(aprove);
			VCHECK_FLAG_CHECK.add(check);
			VAUTHORIZ_FLAG_CHECK.add(auth);
			VIS_SUBMITTED.add(is_submitted);
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
			stmt.setString(3, entity);
			stmt.setString(4, "B"+plant_seq);
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
	
	public void getServiceContractDetail()
	{
		String function_nm="getServiceContractDetail()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, entity);
			bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_unit, "B");
			
			if(entity.equals("S"))
			{
				entity_nm="Surveyor";
			}
			else if(entity.equals("V"))
			{
				entity_nm="Vessel Agent";
			}
			else if(entity.equals("H"))
			{
				entity_nm="Custom House Agent";
			}
			
			queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.START_DT,'DD/MM/YYYY'),A.FINAL_SVC_RATE,A.FINAL_SVC_RATE_UNIT1,A.FINAL_SVC_RATE_UNIT2,"
					+ "C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,C.EXCHNG_RATE_CD,C.DUE_DT_IN,"
					+ "C.EXCL_SAT_MAP,C.EXCHNG_RATE_CAL,C.EXCHNG_CRITERIA,"
					+ "A.PROV_SVC_RATE, A.PROV_SVC_RATE_UNIT1, A.PROV_SVC_RATE_UNIT2,C.HOLIDAY_STATE "
					+ "FROM FMS_CARGO_SVC_CONT_MST A, FMS_CARGO_SVC_BILLING_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.ENTITY_TYPE=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO AND A.ENTITY_TYPE=C.ENTITY_TYPE "
					+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.PLANT_SEQ_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, entity);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, plant_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String cont_ref=rset.getString(1)==null?"":rset.getString(1);
				cont_start_dt=rset.getString(2)==null?"":rset.getString(2);
				
				deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "")+" ["+cont_ref+"]";
				contRef=cont_ref;
				invoice_raised_in = rset.getString(6)==null?"2":rset.getString(6);
				invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
				payment_done_in = rset.getString(7)==null?"2":rset.getString(7);
				payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
				due_days = rset.getString(8)==null?"0":rset.getString(8);
				exchng_rate_cd = rset.getString(9)==null?"":rset.getString(9);
				consider_due_dt_in=rset.getString(10)==null?"C":rset.getString(10);
				exclude_sat=rset.getString(11)==null?"N":rset.getString(11);
				exchng_rate_cal = rset.getString(12)==null?"":rset.getString(12);
				exchang_criteria = rset.getString(13)==null?"":rset.getString(13);	
				
				if(entity.equals("H") && inv_flag.equals("P"))
				{
					price_cd = rset.getString(15)==null?"2":rset.getString(15);
					price = utilBean.RateNumberFormat(rset.getDouble(14), price_cd);					
					energy_unit=rset.getString(16)==null?"":rset.getString(16);					
				}
				else
				{
					price_cd = rset.getString(4)==null?"2":rset.getString(4);
					price = utilBean.RateNumberFormat(rset.getDouble(3), price_cd);									
					energy_unit=rset.getString(5)==null?"":rset.getString(5);									
				}	
				price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);	
				if(energy_unit.equals("0") && energy_unit_nm.equals("")) {
					energy_unit_nm="Lumpsum";
				}
				
				holiday_state=rset.getString(17)==null?"":rset.getString(17);
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
				
				energy_unit="1";
				energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
			}
			rset.close();
			stmt.close();
			
			String cargoCountptyCd="";
			String cargoContNo="";
			String cargoAgmtNo="";
			String cargoAgmtTyp="";
			String cargoContTyp="";
			String cargoNo="";
			if(!mapping_id.equals(""))
			{
				String[] temp=mapping_id.split("-");
				cargoCountptyCd=temp[0];
				cargoAgmtTyp=temp[1];
				cargoAgmtNo=temp[2];
				cargoContTyp=temp[3];
				cargoContNo=temp[4];
				cargoNo=temp[5];
			}
			
			queryString="SELECT A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,"
					+ "B.CARGO_NO,B.CARGO_REF,SUM(G.ACT_BOE_QTY),SUM(G.ACT_QTY_MT),SUM(G.ACT_QTY_SCM) "
					+ "FROM FMS_TRADER_CN_MST A, "
						+ "FMS_TRADER_CARGO_MST B, "
						+ "FMS_BUY_CARGO_NOM C,"
						+ "FMS_BUY_CARGO_ALLOC F,"
						+ "FMS_BUY_CARGO_ALLOC_BOE G "
					+ "WHERE A.COMPANY_CD=? AND B.CARGO_STATUS=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.AGMT_NO=? "
					+ "AND A.AGMT_TYPE=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? AND G.BU_SEQ=? "
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
					+ "AND A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND A.CONTRACT_TYPE=F.CONTRACT_TYPE "
					+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.AGMT_TYPE=F.AGMT_TYPE AND B.CARGO_NO=F.CARGO_NO "
					+ "AND F.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE F.COMPANY_CD=B.COMPANY_CD "
					+ "AND F.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND F.CONT_NO=B.CONT_NO AND F.AGMT_NO=B.AGMT_NO "
					+ "AND F.CONTRACT_TYPE=B.CONTRACT_TYPE AND F.CARGO_NO=B.CARGO_NO AND F.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND G.COMPANY_CD=F.COMPANY_CD AND G.COUNTERPARTY_CD=F.COUNTERPARTY_CD AND G.CONTRACT_TYPE=F.CONTRACT_TYPE "
					+ "AND G.CONT_NO=F.CONT_NO AND G.AGMT_NO=F.AGMT_NO AND G.AGMT_TYPE=F.AGMT_TYPE AND G.CARGO_NO=F.CARGO_NO AND G.ALLOC_REV=F.ALLOC_REV "
					+ "GROUP BY A.COUNTERPARTY_CD,A.CONT_NO,A.CONT_REV,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,B.CARGO_NO,B.CARGO_REF,A.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, cargoCountptyCd);
			stmt.setString(4, cargoContNo);
			stmt.setString(5, cargoAgmtNo);
			stmt.setString(6, cargoAgmtTyp);
			stmt.setString(7, cargoContTyp);
			stmt.setString(8, cargoNo);
			stmt.setString(9, bu_unit);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(energy_unit.equals("1")) {//MMBTU
					qty_MMBTU=rset.getDouble(9);
				}else if(energy_unit.equals("5")) {//MT
					qty_MMBTU=rset.getDouble(10);
				}else if(energy_unit.equals("3")) {//SCM
					qty_MMBTU=rset.getDouble(11);
				}else if(energy_unit.equals("0")) {//Lumpsum
					qty_MMBTU=1;
				}
				
				qty_mmbtu=nf.format(qty_MMBTU);
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
			String inv_type="";
			if(contract_type.equals("Y")) 
			{
				inv_type="SF";
			}
			else if(contract_type.equals("A")) 
			{
				inv_type="VA";
			}
			else if(contract_type.equals("H")) 
			{
				inv_type="CH";
			}
			int count=0;
			if(!refresh_flg.equals("Y"))
			{
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND CONTRACT_TYPE=? AND INV_FLAG=? AND INVOICE_SEQ=? ";
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, fiscal_year);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, inv_flag);
				stmt.setString(++stcount, inv_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			if(count>0)
			{
				queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
						+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
						+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TCS_CERT_FLAG,TCS_TDS,"
						+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),SYS_INV_NO,QTY_UNIT "
						+ "FROM FMS_PUR_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND CONTRACT_TYPE=? AND INV_FLAG=? AND INVOICE_SEQ=? ";
				int stcount=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, fiscal_year);
				stmt.setString(++stcount, contract_type);
				stmt.setString(++stcount, inv_flag);
				stmt.setString(++stcount, inv_seq);
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
					energy_unit=rset.getString(46)==null?"1":rset.getString(46);
					energy_unit_nm=utilBean.getEnergyUnitNm(conn,energy_unit);
					if(energy_unit.equals("0") && energy_unit_nm.equals("")) {
						energy_unit_nm="Lumpsum";
					}
					
					tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, entity, plant_seq, bu_unit, period_start_dt,inv_type);
					
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
				stmt.setString(3, entity);
				stmt.setString(4, "B"+plant_seq);
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
				stmt.close();
				*/
				
				String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,entity,"B"+plant_seq, "RM", "RLNG","Y");
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
				
				/*String state_abbr="";
				String state_code="";
				queryString2="SELECT TIN,STATE_CODE "
						+ "FROM FMS_STATE_MST "
						+ "WHERE STATE_NM=(SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND SEQ_NO=? AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')))";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, "B");
				stmt2.setString(4, bu_unit);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					state_code=rset2.getString(1)==null?"":rset2.getString(1);
					state_abbr=rset2.getString(2)==null?"":rset2.getString(2);
				}
				rset2.close();
				stmt2.close();*/
				
				String sysdate=utilDate.getSysdate();
				
				if(invoice_dt.equals(""))
				{
					//invoice_dt=period_end_dt;
					invoice_dt=sysdate;
				}
				
				if(invoice_due_dt.equals(""))
				{
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
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND CONTRACT_TYPE=? AND INV_FLAG=? AND INVOICE_SEQ=? ";
					int stcount=0;
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(++stcount, comp_cd);
					stmt4.setString(++stcount, fiscal_year);
					stmt4.setString(++stcount, contract_type);
					stmt4.setString(++stcount, inv_flag);
					stmt4.setString(++stcount, inv_seq);
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
				
				/*if(!invoice_seq.equals("") && !contract_type.equals(""))
				{
					String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
					if (entity.equals("H") && inv_flag.equals("P"))
					{	
						sys_invoice_no=invoice_prefix+"P"+contract_type+"F"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;
					}
					else
					{	
						sys_invoice_no=invoice_prefix+""+contract_type+"F"+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;	
					}	
				}*/
				
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
					
					double tds_gross_amt=0;
					double temp_gross_amt=qty_MMBTU * Double.parseDouble(price);
					gross_amt = nf.format(temp_gross_amt);
					
					if(price_cd.equals(invoice_raised_in))
					{	
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
					temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, entity, plant_seq, bu_unit, period_start_dt, inv_type, gross_amt1);
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
						invoice_amt = nf.format(Double.parseDouble(gross_amt1));
					}
					else
					{
						invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
					}
										
					String total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, contract_type, financial_year);
					String difference="";
					
					applicable_flag="N";
					applicable_abbr="NA";
					if(Double.parseDouble(total_transaction_amt)>transaction_limit)
					{
						applicable_abbr="TDS";
						difference=gross_amt1;
					}
					else if((Double.parseDouble(total_transaction_amt) + tds_gross_amt) > transaction_limit) 
					{
						applicable_abbr="TDS";	
						difference=gross_amt1;
					}
					else if(tds_gross_amt > invoice_limit) 
					{
						applicable_abbr="TDS";	
						difference=gross_amt1;
					}
					
					if(applicable_abbr.equals("TDS")) 
					{
						//TDS will be picked up
						Vector temp_tcs = new Vector();
						temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, entity, "TDS", inv_type,"S",period_end_dt, difference);
	
						tds_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
						tds_struct_cd = ""+temp_tcs.elementAt(1);
						tds_struct_dt = ""+temp_tcs.elementAt(2);
						//tax_info = ""+temp_tcs.elementAt(3);
						//tax_struct_dtl = ""+temp_tcs.elementAt(4);
						tds_factor = ""+temp_tcs.elementAt(5);
					}
					
					net_payable=invoice_amt;
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void PartyInvoiceCalculation()
	{
		String function_nm="PartyInvoiceCalculation()";
		String inv_type="";
		if(contract_type.equals("Y")) {
			inv_type="SF";
		}else if(contract_type.equals("A")) {
			inv_type="VA";
		}
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),ADJUST_SIGN,ADJUST_AMT,FINANCIAL_YEAR,"
					+ "TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),SYS_INV_NO "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND CONTRACT_TYPE=? AND INV_FLAG=? AND INVOICE_SEQ=? ";
			int stcount=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++stcount, comp_cd);
			stmt.setString(++stcount, fiscal_year);
			stmt.setString(++stcount, contract_type);
			stmt.setString(++stcount, inv_flag);
			stmt.setString(++stcount, inv_seq);
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
				
				pg_tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, entity, plant_seq, bu_unit, period_start_dt,inv_type);
					
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
	
	public Double getTransactionAmount(String comp_cd,String counterparty_cd, String contract_type, String financial_year)
	{
		String function_nm="getTransactionAmount()";
		double transc_fee=0;
		try
		{	
			queryString="SELECT INVOICE_AMT,INVOICE_RAISED_IN,EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			queryString+="UNION ALL ";
			queryString+="SELECT INVOICE_AMT,INVOICE_RAISED_IN,EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			queryString+="UNION ALL ";
			queryString+="SELECT INVOICE_AMT,INVOICE_RAISED_IN,EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, contract_type);
			stmt.setString(4, financial_year);
			stmt.setString(5, "A");
			stmt.setString(6, invoice_dt);
			stmt.setString(7, "F");
			stmt.setString(8, comp_cd);
			stmt.setString(9, counterparty_cd);
			stmt.setString(10, contract_type);
			stmt.setString(11, financial_year);
			stmt.setString(12, "A");
			stmt.setString(13, invoice_dt);
			stmt.setString(14, "F");
			stmt.setString(15, comp_cd);
			stmt.setString(16, counterparty_cd);
			stmt.setString(17, contract_type);
			stmt.setString(18, financial_year);
			stmt.setString(19, "A");
			stmt.setString(20, invoice_dt);
			stmt.setString(21, "DR");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				double inv_amt=rset.getDouble(1);
				String inv_raised_in=rset.getString(2)==null?"":rset.getString(2);
				String exchng_rt_cd=rset.getString(3)==null?"":rset.getString(3);
				String due_dt=rset.getString(4)==null?"":rset.getString(4);
				
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
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return transc_fee;
	}

	public void getTcsTdsInvDtl()
	{
		String function_nm="getTcsTdsInvDtl()";
		try
		{
			double total=0;
			double gross_total=0;
			queryString="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),NULL,'SG' "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			queryString+="UNION ALL ";
			queryString+="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),NULL,'PG' "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG=? ";
			queryString+="UNION ALL ";
			queryString+="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,GROSS_AMT_INR,"
					+ "EXCHG_RATE_CD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT_USD,'FF' "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?)";
			//NO NEED TO CONSIDER IGX INVOICE - 20230725
			//JD20230724 INVOICE_DT ADDED
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, contract_type);
			stmt.setString(4, financial_year);
			stmt.setString(5, "A");
			stmt.setString(6, invoice_dt);
			stmt.setString(7, "F");
			stmt.setString(8, comp_cd);
			stmt.setString(9, counterparty_cd);
			stmt.setString(10, contract_type);
			stmt.setString(11, financial_year);
			stmt.setString(12, "A");
			stmt.setString(13, invoice_dt);
			stmt.setString(14, "F");
			stmt.setString(15, comp_cd);
			stmt.setString(16, counterparty_cd);
			stmt.setString(17, contract_type);
			stmt.setString(18, financial_year);
			stmt.setString(19, "A");
			stmt.setString(20, invoice_dt);
			stmt.setString(21, "DR");
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
				double gross_amt_usd=rset.getDouble(11);
				String tbl_typ=rset.getString(12)==null?"":rset.getString(12);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, "", "", contno, "", cont_type, "");
				
				String contRef="";
				queryString1="SELECT CONT_REF_NO  "
						+ "FROM FMS_CARGO_SVC_CONT_MST  "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND COUNTERPARTY_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
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
					if(tbl_typ.equals("FF"))
					{
						gross_total+=(gross_amt_usd);
					}
					else
					{
						gross_total+=(gross_amt * exchng_rate);
					}
				}
				else
				{
					total+=inv_amt;
					gross_total+=gross_amt;
				}
			}
			rset.close();
			stmt.close();
			
			total_InvoiceAmt=nf.format(total);
			total_GrossAmt=nf.format(gross_total);
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
			
			queryString="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY') "
					+ "FROM FMS_CARGO_SVC_CONT_MST "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND ENTITY_TYPE=? "
					+ "AND CONT_NO=? AND COUNTERPARTY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			stmt.setString(3, entity);
			stmt.setString(4, cont_no);
			stmt.setString(5, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contRef=rset.getString(1)==null?"":rset.getString(1);
				signing_dt=rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();
				
			top_heading_nm=""+couterpty_nm;
			
			String cargoCountptyCd="";
			String cargoContNo="";
			String cargoAgmtNo="";
			String cargoAgmtTyp="";
			String cargoContTyp="";
			String cargoNo="";
			if(!mapping_id.equals(""))
			{
				String[] temp=mapping_id.split("-");
				cargoCountptyCd=temp[0];
				cargoAgmtTyp=temp[1];
				cargoAgmtNo=temp[2];
				cargoContTyp=temp[3];
				cargoContNo=temp[4];
				cargoNo=temp[5];
			}
			
			String ship_nm="";
			String arrival_dt="";
			queryString="SELECT TO_CHAR(A.ACT_ARRV_DT,'DD-MON-YY'),A.SHIP_CD "
					+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_BUY_CARGO_ALLOC_BOE B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CARGO_NO=? "
					+ "AND A.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CARGO_NO=B.CARGO_NO AND A.ALLOC_REV=B.ALLOC_REV";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, cargoCountptyCd);
			stmt.setString(3, cargoAgmtTyp);
			stmt.setString(4, cargoAgmtNo);
			stmt.setString(5, cargoContNo);
			stmt.setString(6, cargoContTyp);
			stmt.setString(7, cargoNo);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				arrival_dt=rset.getString(1)==null?"":rset.getString(1);
				String ship_cd=rset.getString(2)==null?"":rset.getString(2);
				ship_nm=utilBean.getShipName(conn,ship_cd);
			}
			rset.close();
			stmt.close();
			
			str_cargoname=ship_nm+" Dated "+arrival_dt;
			
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
			stmt1.setString(3, entity);
			stmt1.setString(4, contact_person_cd);
			stmt1.setString(5, "B"+plant_seq);
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
			
			HashMap plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, entity, counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			queryString5 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
					+ "FROM FMS_COUNTERPARTY_BU_TAX A, FMS_GOVT_STAT_TAX B "
					+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
					+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
					+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, counterparty_cd);
			stmt5.setString(2, entity);
			stmt5.setString(3, plant_seq);
			stmt5.setString(4, comp_cd);
			stmt5.setString(5, "1003");
			rset5=stmt5.executeQuery();
			//AS MENTIONED BY VIJAY, ONLY SHOWS GST DETAIL FRO CARGO SUPPORT REMITTANCE ON 09/09/2024 		
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
			stmt6.setString(5, "1003");
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
	String from_dt = "";
	String to_dt = "";
	String billing_cycle = "";
	String billing_freq="";
	String period_start_dt="";
	String period_end_dt="";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String plant_seq = "";
	String bu_unit = "";
	String refresh_flg = "";
	String inv_flag="";
	String entity="";
	String mapping_id="";
	String inv_type="";
	String inv_title="";
	String fiscal_year="";
	String inv_seq="";
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setRefresh_flg(String refresh_flg) {this.refresh_flg = refresh_flg;}
	public void setInvoice_dt(String invoice_dt) {this.invoice_dt = invoice_dt;}
	public void setInvoice_due_dt(String invoice_due_dt) {this.invoice_due_dt = invoice_due_dt;}
	public void setInv_flag(String inv_flag) {this.inv_flag = inv_flag;}
	public void setEntity(String entity) {this.entity = entity;}
	public void setMapping_id(String mapping_id) {this.mapping_id = mapping_id;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setInv_title(String inv_title) {this.inv_title = inv_title;}
	public void setFiscal_year(String fiscal_year) {this.fiscal_year = fiscal_year;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	
	public void setContact_person_cd(String contact_person_cd) {this.contact_person_cd = contact_person_cd;}
	public void setBu_contact_person_cd(String bu_contact_person_cd) {this.bu_contact_person_cd = bu_contact_person_cd;}
	public void setPrice_cd(String price_cd) {this.price_cd = price_cd;}
	public void setInvoice_raised_in(String invoice_raised_in) {this.invoice_raised_in = invoice_raised_in;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VREMITTANCE_LIST_ABBR = new Vector();
	Vector VREMITTANCE_LIST_NAME = new Vector();
	Vector VINDEX = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VENTITY = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VPLANT_BU_SEQ = new Vector();
	Vector VPLANT_BU_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VBILLING_FREQ_NM = new Vector();
	Vector VCARGO_DEAL_NO = new Vector();
	Vector VCARGO_REF_NO = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	Vector VREMITTANCE_NO = new Vector();
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VSTATUS = new Vector();
	Vector VFILE_UPLOAD_COUNT = new Vector();
	Vector VUPLOADED_FILE_NAME = new Vector();
	Vector VAPPROVE_FLAG_CHECK = new Vector();
	Vector VCHECK_FLAG_CHECK = new Vector();
	Vector VAUTHORIZ_FLAG_CHECK = new Vector();
	Vector VIS_SUBMITTED = new Vector();
	Vector VAPPROVE_INVOICE_FLAG = new Vector();
	Vector VPDF_INV_FLAG = new Vector();
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VPAYMENT_TYPE_FLAG = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_CD = new Vector();
	Vector VBU_CONTACT_PERSON = new Vector();
	Vector VBU_CONTACT_PERSON_CD = new Vector();
	Vector VSG_MULTI_TAX_STRUCT = new Vector();
	Vector VPG_MULTI_TAX_STRUCT = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VGROSS_AMT_USD = new Vector();
	Vector VINVOICE_AMT = new Vector();
	Vector VINVOICE_AMT_USD = new Vector();
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVREMITTANCE_LIST_ABBR() {return VREMITTANCE_LIST_ABBR;}
	public Vector getVREMITTANCE_LIST_NAME() {return VREMITTANCE_LIST_NAME;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVENTITY() {return VENTITY;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVPLANT_BU_SEQ() {return VPLANT_BU_SEQ;}
	public Vector getVPLANT_BU_ABBR() {return VPLANT_BU_ABBR;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVBILLING_FREQ_NM() {return VBILLING_FREQ_NM;}
	public Vector getVCARGO_DEAL_NO() {return VCARGO_DEAL_NO;}
	public Vector getVCARGO_REF_NO() {return VCARGO_REF_NO;}
	public Vector getVMAPPING_ID() {return VMAPPING_ID;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVREMITTANCE_NO() {return VREMITTANCE_NO;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVFILE_UPLOAD_COUNT() {return VFILE_UPLOAD_COUNT;}
	public Vector getVUPLOADED_FILE_NAME() {return VUPLOADED_FILE_NAME;}
	public Vector getVAPPROVE_FLAG_CHECK() {return VAPPROVE_FLAG_CHECK;}
	public Vector getVCHECK_FLAG_CHECK() {return VCHECK_FLAG_CHECK;}
	public Vector getVAUTHORIZ_FLAG_CHECK() {return VAUTHORIZ_FLAG_CHECK;}
	public Vector getVIS_SUBMITTED() {return VIS_SUBMITTED;}
	public Vector getVAPPROVE_INVOICE_FLAG() {return VAPPROVE_INVOICE_FLAG;}
	public Vector getVPDF_INV_FLAG() {return VPDF_INV_FLAG;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVPAYMENT_TYPE_FLAG() {return VPAYMENT_TYPE_FLAG;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVCONTACT_PERSON() {return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_CD() {return VCONTACT_PERSON_CD;}
	public Vector getVBU_CONTACT_PERSON() {return VBU_CONTACT_PERSON;}
	public Vector getVBU_CONTACT_PERSON_CD() {return VBU_CONTACT_PERSON_CD;}
	public Vector getVSG_MULTI_TAX_STRUCT() {return VSG_MULTI_TAX_STRUCT;}
	public Vector getVPG_MULTI_TAX_STRUCT() {return VPG_MULTI_TAX_STRUCT;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVINVOICE_AMT_USD() {return VINVOICE_AMT_USD;}
	
	String entity_nm="";
	String couterpty_abbr="";
	String couterpty_nm="";
	String deal_no="";
	String plant_abbr="";
	String bu_plant_abbr="";
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
	String consider_due_dt_in="";
	String exclude_sat="";
	String holiday_state="";
	String exchang_criteria="";
	String exchng_rate_cal="";
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
	String qty_mmbtu="";
	String invoice_amt="";
	String net_payable="";
	
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
	String pg_tcs_factor="";
	String pg_tcs_amount="";
	String pg_tcs_struct_cd="";
	String pg_tcs_struct_dt="";
	String pg_tds_factor="";
	String pg_tds_amount="";
	String pg_tds_struct_cd="";
	String pg_tds_struct_dt="";
	
	String total_InvoiceAmt="";
	String total_GrossAmt="";
	
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
	String signing_dt="";
	String top_heading_nm="";
	String str_cargoname="";
	
	String submitted_fiscal_yr="";
	String sg_rem_gen_status="";
	String pg_rem_gen_status="";
	
	public String getEntity_nm() {return entity_nm;}
	public String getCouterpty_abbr() {return couterpty_abbr;}
	public String getCouterpty_nm() {return couterpty_nm;}
	public String getDeal_no() {return deal_no;}
	public String getPlant_abbr() {return plant_abbr;}
	public String getBu_plant_abbr() {return bu_plant_abbr;}
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
	public String getConsider_due_dt_in() {return consider_due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getHoliday_state() {return holiday_state;}
	public String getExchang_criteria() {return exchang_criteria;}
	public String getExchng_rate_cal() {return exchng_rate_cal;}
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
	public String getQty_mmbtu() {return qty_mmbtu;}
	public String getInvoice_amt() {return invoice_amt;}
	public String getNet_payable() {return net_payable;}
	
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
	public String getPg_tcs_amount() {return pg_tcs_amount;}
	public String getPg_tcs_factor() {return pg_tcs_factor;}
	public String getPg_tcs_struct_cd() {return pg_tcs_struct_cd;}
	public String getPg_tcs_struct_dt() {return pg_tcs_struct_dt;}
	public String getPg_tds_amount() {return pg_tds_amount;}
	public String getPg_tds_factor() {return pg_tds_factor;}
	public String getPg_tds_struct_cd() {return pg_tds_struct_cd;}
	public String getPg_tds_struct_dt() {return pg_tds_struct_dt;}
	
	public String getTotal_InvoiceAmt() {return total_InvoiceAmt;}
	public String getTotal_GrossAmt() {return total_GrossAmt;}
	
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
	public String getStr_cargoname() {return str_cargoname;}
	
	public String getSubmitted_fiscal_yr() {return submitted_fiscal_yr;}
	public String getSg_rem_gen_status() {return sg_rem_gen_status;}
	public String getPg_rem_gen_status() {return pg_rem_gen_status;}
	
	boolean submission_chk = false;
	boolean sg_submission_chk = false;
	boolean pg_submission_chk = false;

	public boolean getSubmission_chk() {return submission_chk;}
	public boolean getSg_submission_chk() {return sg_submission_chk;}
	public boolean getPg_submission_chk() {return pg_submission_chk;}
}
