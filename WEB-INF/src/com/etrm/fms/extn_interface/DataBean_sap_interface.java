package com.etrm.fms.extn_interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DataBean_sap_interface
{
	String db_src_file_name="DataBean_sap_interface.java";
	
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt_1;
	PreparedStatement temp_stmt;
	PreparedStatement prep_stmt;
	PreparedStatement stmt_temp;

	PreparedStatement stmtement;
	PreparedStatement stmtement1;
	PreparedStatement stmtement_1;
	PreparedStatement stmtement_11;
	
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset_1;
	ResultSet temp_rset;
	ResultSet temp_rset1;
	ResultSet rset_temp;
	
	ResultSet resultset;
	ResultSet resultset1;
	ResultSet resultset_1;
	ResultSet resultset_11;

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
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
	    			if(callFlag.equalsIgnoreCase("SAP_RECON_RPT"))
	    			{
	    				firstDtOfPrevMonth = utilDate.getFirstDateOfPreviousMonth(from_dt);
	    				String temp_dt = utilDate.getFirstDateOfPreviousMonth(to_dt);
	    				lastDtOfPrevMonth = utilDate.getLastDateOfMonth(temp_dt);
	    				
	    				getGlList();
	    				getSapReconDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SAP_INTERFACE_STATUS"))
	    			{
	    				getCompany_Dtl();	
	    				getSAP_Interface();
	    				getSAP_Interface_Type();

	    				getSAP_ACC_Interface();
	    				getSAP_ACC_Interface_Type();

	    				getSAP_MTM_Interface();
	    				getSAP_MTM_Interface_Type();
	    				
	    				getSAP_Receivable();		
	    				getSAP_Payable();		
	    				getSAP_Advance();		
	    				
	    				getACC_SAP_Receivable();		
	    				getACC_SAP_Payable();		

	    				getSAP_MTM_Receivable();
	    				getSAP_MTM_Payable();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SAP_BUSINESS_AREA_DEAL_MAP"))
	    			{
	    				getInvoiceTypeDetail();
	    				getBusinessAreaCodeDealMapList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SAP_INCOMING_DTL"))
	    			{
	    				getSapIncomingDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SAP_RECON_CONSOLIDATE_RPT"))
	    			{
	    				to_dt=utilDate.getLastDateOfMonth(month, year);
	    				from_dt=utilDate.getFirstDateOfMonth(to_dt);
	    				
	    				firstDtOfPrevMonth = utilDate.getFirstDateOfPreviousMonth(from_dt);
	    				//lastDtOfPrevMonth = utilDate.getLastDateOfMonth(firstDtOfPrevMonth);
	    				String temp_dt = utilDate.getFirstDateOfPreviousMonth(to_dt);
	    				lastDtOfPrevMonth = utilDate.getLastDateOfMonth(temp_dt);
	    				
	    				getSapSaleReconDetails();
	    				getSapDLNGReconDetails();		//for dlng
	    				getSapPurReconDetails();
	    				getSapDervReconDetails();		//for derivatives
	    				getSapTransporterDetails();
	    				getSapGxReconDetails();
	    				fetchSapDumpData();
	    				getSAPReconConsolidateRpt();
	    				getConsolidateEurDtl();
	    				
	    				amt_sum=nf.format(amtSum);
	    				amt_usd_sum=nf.format(amtUsdSum);
	    				sap_sum=nf.format(sapsum);
	    				sap_sum_usd=nf.format(sapSumUsd);
	    				delta_sap_sum=nf.format(deltasapsum);
	    				abs_delta_sap_sum=nf.format(absDeltaSapSum);
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
	    	if(rset_1 != null){try{rset_1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(temp_rset != null){try{temp_rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(resultset != null){try{resultset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(resultset1 != null){try{resultset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(resultset_1 != null){try{resultset_1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(resultset_11 != null){try{resultset_11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(temp_rset1 != null){try{temp_rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}

	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmt_1 != null){try{stmt_1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmtement != null){try{stmtement.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmtement1 != null){try{stmtement1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmtement_1 != null){try{stmtement_1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmtement_11 != null){try{stmtement_11.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(prep_stmt != null){try{prep_stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(temp_stmt != null){try{temp_stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getInvoiceTypeDetail()
	{
		String function_nm="getInvoiceTypeDetail()";
		
		try 
		{
			if(entity_role.equals("C"))
			{
				VCONTRACT_TYPE_MST.add("S");
				VCONTRACT_TYPE_MST.add("L");
				VCONTRACT_TYPE_MST.add("X");
				VCONTRACT_TYPE_MST.add("Q");
				VCONTRACT_TYPE_MST.add("O");
				VCONTRACT_TYPE_MST.add("E");
				VCONTRACT_TYPE_MST.add("F");
				VCONTRACT_TYPE_MST.add("W");
				VCONTRACT_TYPE_MST.add("B");
				VCONTRACT_TYPE_MST.add("M");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("S"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("L"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("X"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("Q"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("O"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("E"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("F"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("W"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("B"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("M"));
			}
			else if(entity_role.equals("T"))
			{
				VCONTRACT_TYPE_MST.add("D");
				VCONTRACT_TYPE_MST.add("I");
				VCONTRACT_TYPE_MST.add("T");
				VCONTRACT_TYPE_MST.add("N");
				VCONTRACT_TYPE_MST.add("G");
				VCONTRACT_TYPE_MST.add("P");
				VCONTRACT_TYPE_MST.add("V");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("D"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("I"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("T"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("N"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("G"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("P"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("V"));
			}
			else if(entity_role.equals("R"))
			{
				VCONTRACT_TYPE_MST.add("C");
				VCONTRACT_TYPE_MST.add("R");
				VCONTRACT_TYPE_MST.add("K");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("C"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("R"));
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("K"));
			}
			else if(entity_role.equals("V"))
			{
				VCONTRACT_TYPE_MST.add("A");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("A"));
			}
			else if(entity_role.equals("H"))
			{
				VCONTRACT_TYPE_MST.add("H");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("H"));
			}
			else if(entity_role.equals("S"))
			{
				VCONTRACT_TYPE_MST.add("Y");
				
				VCONTRACT_TYPE_NM_MST.add(utilBean.getContractTypeName("Y"));
			}
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBusinessAreaCodeDealMapList()
	{
		String function_nm="getBusinessAreaCodeDealMapList()";
		try 
		{
			String queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTRACT_TYPE,BA_CODE,BA_DECR "
					+ "FROM FMS_SAP_BA_CODE_DTL A "
					+ "WHERE COMPANY_CD=? AND ENTITY=? "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SAP_BA_CODE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, entity_role);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				VEFF_DT.add(rset.getString(1)==null?"":rset.getString(1));
				
				String invType=rset.getString(2)==null?"":rset.getString(2);
				VCONTRACT_TYPE.add(invType);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(invType));
				VBA_CODE.add(rset.getString(3)==null?"":rset.getString(3));
				VBA_DESC.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_gta_dtl() 
	{
		String function_nm="getSAP_interface_gta_dtl()";
		
		try 
		{
			int index =0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(TRANS_BU_UNIT),'SG' "
					+ "FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(TRANS_BU_UNIT),'PG' "
					+ "FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,ADDR_FLAG,'FFLOW' "
					+ "FROM FMS_GTA_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, from_dt);
			stmt.setString(4, eff_to_dt);
			stmt.setString(5, comp_cd);
			stmt.setString(6, "Y");
			stmt.setString(7, from_dt);
			stmt.setString(8, eff_to_dt);
			stmt.setString(9, comp_cd);
			stmt.setString(10, "Y");
			stmt.setString(11, from_dt);
			stmt.setString(12, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
				String inv_approved_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_by = rset.getString(16)==null?"":rset.getString(16);
				String inv_approved_flag = rset.getString(17)==null?"":rset.getString(17);
				String temp_inv_trans_bu_unit = rset.getString(18)==null?"":rset.getString(18);
				String cont_map =inv_cont_type+inv_agmt_no+"-"+inv_cont_no;
				String inv_typ = rset.getString(19)==null?"":rset.getString(19);
				String inv_trans_bu_unit="";
				if(inv_typ.equals("FFLOW"))
				{
					inv_trans_bu_unit = temp_inv_trans_bu_unit.substring(1,temp_inv_trans_bu_unit.length());
				}
				else
				{
					inv_trans_bu_unit=temp_inv_trans_bu_unit;
				}
				
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				String plant_abbr=utilBean.getCounterpartyBuABBR(conn,inv_counterparty_cd, comp_cd, inv_trans_bu_unit, "R");
				
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg = "";
				String doc_no="";
				String company_code = "";
				String fiscal_yr = "";
				String msg_status="";
				String description="";
				
				String queryStringTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt2=conn.prepareStatement(queryStringTemp);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_invoice_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count_dtl= rset2.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{ 
							index++;
							
							fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_code=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							
							V_BU_ABBR.add(bu_plant_abbr);			
							V_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							V_FMS_REF.add(fms_ref);
							V_POST_STATUS.add(post_status);
							V_POST_DT.add(post_dt);
							V_POST_TIME.add(post_time);
							V_IDOC_NO.add(idoc_no);
							V_IDOC_STATUS.add(idoc_status);
							V_STATUS_MSG.add(status_msg);
							V_DOC_NO.add(doc_no);
							V_COMPANY_CODE.add(company_code);
							V_FISCAL_YR.add(fiscal_yr);
							V_MSG_STATUS.add(msg_status);
							
							V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							V_DUE_DT.add(inv_due_dt);
							V_APPROVE_DT.add(inv_approved_dt);
							V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
							V_APPROVED_FLAG.add(inv_approved_flag);
							V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
							V_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
						
					}
					else 
					{
						index++;
						
						 fms_ref=inv_invoice_no;
						 post_status="";
						 post_dt="";
						 post_time="";
						 idoc_no="";
						 idoc_status="";
						 status_msg = "";
						 doc_no="";
						 company_code = "";
						 fiscal_yr = "";
						 msg_status="";
						 description="<font style=\"color:blue\">Pending</font>";
						 
						 V_BU_ABBR.add(bu_plant_abbr);			
						 V_PLANT_ABBR.add(plant_abbr);
						 
						 V_FMS_REF.add(fms_ref);
						 V_POST_STATUS.add(post_status);
						 V_POST_DT.add(post_dt);
						 V_POST_TIME.add(post_time);
						 V_IDOC_NO.add(idoc_no);
						 V_IDOC_STATUS.add(idoc_status);
						 V_STATUS_MSG.add(status_msg);
						 V_DOC_NO.add(doc_no);
						 V_COMPANY_CODE.add(company_code);
						 V_FISCAL_YR.add(fiscal_yr);
						 V_MSG_STATUS.add(msg_status);
							
						 V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						 V_DUE_DT.add(inv_due_dt);
						 V_APPROVE_DT.add(inv_approved_dt);
						 V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
						 V_APPROVED_FLAG.add(inv_approved_flag);
						 V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
						 V_SAP_POSTED.add(description);
					}
					
				}
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			VINDEX.add(index);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_gx_dtl() 
	{
		String function_nm="getSAP_interface_gx_dtl()";
		try 
		{
			int index = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(GX_BU_UNIT),GX_COUNTERPARTY_CD "
					+ "FROM FMS_GX_TXN_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') ";
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(GX_BU_UNIT),GX_COUNTERPARTY_CD "
					+ "FROM FMS_GX_TXN_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') ";
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(GX_BU_UNIT),GX_COUNTERPARTY_CD "
					+ "FROM FMS_GX_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, from_dt);
			stmt.setString(4, eff_to_dt);
			stmt.setString(5, comp_cd);
			stmt.setString(6, "Y");
			stmt.setString(7, from_dt);
			stmt.setString(8, eff_to_dt);
			stmt.setString(9, comp_cd);
			stmt.setString(10, "Y");
			stmt.setString(11, from_dt);
			stmt.setString(12, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
				String inv_approved_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_by = rset.getString(16)==null?"":rset.getString(16);
				String inv_approved_flag = rset.getString(17)==null?"":rset.getString(17);
				String inv_gx_bu_unit = rset.getString(18)==null?"":rset.getString(18);
				String gx_countpty_cd = rset.getString(19)==null?"":rset.getString(19);
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, "");
				//String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				String gx_bu_plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,gx_countpty_cd, comp_cd, inv_gx_bu_unit, "G");
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg = "";
				String doc_no="";
				String company_code = "";
				String fiscal_yr = "";
				String msg_status="";
				String description="";
				
				String queryStringTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt2=conn.prepareStatement(queryStringTemp);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_invoice_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count_dtl=rset2.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{ 
							index++;
							
							fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_code=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							
							V_BU_ABBR.add(bu_plant_abbr);
							V_PLANT_ABBR.add(gx_bu_plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							V_FMS_REF.add(fms_ref);
							V_POST_STATUS.add(post_status);
							V_POST_DT.add(post_dt);
							V_POST_TIME.add(post_time);
							V_IDOC_NO.add(idoc_no);
							V_IDOC_STATUS.add(idoc_status);
							V_STATUS_MSG.add(status_msg);
							V_DOC_NO.add(doc_no);
							V_COMPANY_CODE.add(company_code);
							V_FISCAL_YR.add(fiscal_yr);
							V_MSG_STATUS.add(msg_status);
							
							V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							V_DUE_DT.add(inv_due_dt);
							V_APPROVE_DT.add(inv_approved_dt);
							V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
							V_APPROVED_FLAG.add(inv_approved_flag);
							V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
							V_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else 
					{
						index++;
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg = "";
						doc_no="";
						company_code = "";
						fiscal_yr = "";
						msg_status="";
						description="<font style=\"color:blue\">Pending</font>";
						
						V_FMS_REF.add(fms_ref);
						V_POST_STATUS.add(post_status);
						V_POST_DT.add(post_dt);
						V_POST_TIME.add(post_time);
						V_IDOC_NO.add(idoc_no);
						V_IDOC_STATUS.add(idoc_status);
						V_STATUS_MSG.add(status_msg);
						V_DOC_NO.add(doc_no);
						V_COMPANY_CODE.add(company_code);
						V_FISCAL_YR.add(fiscal_yr);
						V_MSG_STATUS.add(msg_status);
						
						V_BU_ABBR.add(bu_plant_abbr);
						V_PLANT_ABBR.add(gx_bu_plant_abbr);
						
						V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						V_DUE_DT.add(inv_due_dt);
						V_APPROVE_DT.add(inv_approved_dt);
						V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
						V_APPROVED_FLAG.add(inv_approved_flag);
						V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
						V_SAP_POSTED.add(description);
					}
			
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			VINDEX.add(index);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_purchase_dtl(String type) 
	{
		String function_nm = "getSAP_interface_purchase_dtl()";
		String eff_to_dt =utilDate.getDate(to_dt, "1");
		try
		{
			String inv_cont_type="";
			String inv_custom_duty="";
			
			int ctn=0;
			int index = 0;
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),CARGO_NO,BOE_NO,INV_FLAG "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			if(type.equals("Purchase"))
			{
				queryString+="AND (INV_FLAG IN ('P','PF','F','FFLOW')) AND CONTRACT_TYPE NOT IN ('G','P','Y','A','H') ";
			}
			else if(type.equals("LTCORA (Buy)"))
			{
				queryString+="AND CONTRACT_TYPE IN ('G','P') ";				
			}
			else if(type.equals("Custom Duty"))
			{
				queryString+="AND (INV_FLAG IN ('CF','CP','CD')) ";			
			}
			else if(type.equals("Surveyor Service"))
			{
				queryString+="AND CONTRACT_TYPE='Y' ";		
			}
			else if(type.equals("Vessel Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE='A' ";		
			}
			else if(type.equals("Custom House Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE='H' AND INV_FLAG IN ('F','P') ";	
			}
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "SYS_INV_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),CARGO_NO,BOE_NO,INV_FLAG "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			if(type.equals("Purchase"))
			{
				queryString+="AND (INV_FLAG IN ('P','PF','F','FFLOW')) AND CONTRACT_TYPE NOT IN ('G','P','Y','A','H') ";
			}
			else if(type.equals("LTCORA (Buy)"))
			{
				queryString+="AND CONTRACT_TYPE IN ('G','P') ";				
			}
			else if(type.equals("Custom Duty"))
			{
				queryString+="AND (INV_FLAG IN ('CF','CP','CD')) ";			
			}
			else if(type.equals("Surveyor Service"))
			{
				queryString+="AND CONTRACT_TYPE='Y' ";		
			}
			else if(type.equals("Vessel Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE='A' ";		
			}
			else if(type.equals("Custom House Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE='H' AND INV_FLAG IN ('F','P') ";	
			}
			queryString +=" UNION ";
			queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),APPROVED_BY,APPROVED_FLAG,ADDR_FLAG,CARGO_NO,0,"
					+ "CASE WHEN INV_HEAD = 'CD' THEN 'CD' ELSE 'FFLOW' END AS INV_HEAD "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			if(type.equals("Purchase"))
			{
				queryString+="AND (INV_HEAD !='CD' OR INV_HEAD IS NULL) AND CONTRACT_TYPE NOT IN ('G','P','Y','A','H') "; //JD 
			}
			else if(type.equals("LTCORA (Buy)"))
			{
				queryString+="AND CONTRACT_TYPE IN ('G','P') ";
			}
			else if(type.equals("Custom Duty"))
			{
				queryString+="AND (INV_HEAD = 'CD') AND CONTRACT_TYPE IN ('N') ";
			}
			else if(type.equals("Surveyor Service"))
			{
				queryString+="AND CONTRACT_TYPE IN ('Y') ";
			}
			else if(type.equals("Vessel Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE IN ('A') ";
			}
			else if(type.equals("Custom House Agent Service"))
			{
				queryString+="AND CONTRACT_TYPE IN ('H') ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, eff_to_dt);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, eff_to_dt);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
				String inv_approved_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_by = rset.getString(16)==null?"":rset.getString(16);
				String inv_approved_flag = rset.getString(17)==null?"":rset.getString(17);
				String inv_plant_seq = rset.getString(18)==null?"":rset.getString(18);
				
				String inv_cargo_no = rset.getString(19)==null?"":rset.getString(19);
				String inv_boe_no = rset.getString(20)==null?"":rset.getString(20);
				
				String inv_flag = rset.getString(21)==null?"":rset.getString(21);
				
				 if(inv_cont_type.equals("N") && (inv_flag.equals("CF") || inv_flag.equals("CP") || inv_flag.equals("CD"))) 
				 {
					 inv_custom_duty="Y";
				 } 
				 else
				 {
					 inv_custom_duty="N";
				 }
				 
				 if(inv_plant_seq.equals("R")) 
				 {
					inv_plant_seq="";
				 }
				 else if(inv_plant_seq.startsWith("P"))
				 {
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				 }
				 else if(inv_plant_seq.startsWith("B"))
				 {
					 inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				 }
				 String plant_abbr="";
				 if(inv_cont_type.equals("Y") || inv_cont_type.equals("A") || inv_cont_type.equals("H"))
				 {
					 String entity = inv_cont_type.equals("Y") ? "S": inv_cont_type.equals("A") ? "V" : "H";
					 plant_abbr = utilBean.getCounterpartyBuPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, entity);
					 
				 }
				 else
				 {
					 plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "T");
				 }
				 String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
					
				 String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, "");
				// String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				 
				 String bu_region = "";
					
				 String queryStringTemp = "SELECT COUNT(*) "
						 + "FROM FMS_SAP_ACK_DTL "
						 + "WHERE COMPANY_CD=? AND FMS_REF=? ";
				 stmt2=conn.prepareStatement(queryStringTemp);
				 stmt2.setString(1, comp_cd);
				 stmt2.setString(2, inv_invoice_no);
				 rset2=stmt2.executeQuery();
				 if(rset2.next())
				 {
					 int count_dtl=rset2.getInt(1);
					 String fms_ref="";
					 String post_status="";
					 String post_dt="";
					 String post_time="";
					 String idoc_no="";
					 String idoc_status="";
					 String status_msg = "";
					 String doc_no="";
					 String company_code = "";
					 String fiscal_yr = "";
					 String msg_status="";
					 String description="";
					 if(count_dtl>0)
					 {
						 String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
									+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
									+ "FROM FMS_SAP_ACK_DTL "
									+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						 stmt1=conn.prepareStatement(queryString1);
						 stmt1.setString(1, comp_cd);
						 stmt1.setString(2, inv_invoice_no);
						 rset1=stmt1.executeQuery();
						 
						 
						 while(rset1.next())
						 {
							 index++;
							 fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							 post_status=rset1.getString(2)==null?"":rset1.getString(2);
							 post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							 post_time=rset1.getString(4)==null?"":rset1.getString(4);
							 idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							 idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							 status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							 doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							 company_code=rset1.getString(9)==null?"":rset1.getString(9);
							 fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							 msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							 
							 if(inv_custom_duty.equals("Y")) 
							 {
								 V_PLANT_ABBR.add("Indian Customs");
								 V_BU_ABBR.add(bu_plant_abbr);
							 }
							 else 
							 {
								 V_PLANT_ABBR.add(plant_abbr);
								 V_BU_ABBR.add(bu_plant_abbr);
							 }	
							 
							 if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							 {
								 char a = idoc_status.charAt(0);
								 if(Character.isDigit(a)) 
								 {
									 description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								 }
								 else 
								 {
									 description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								 }
							 }
							 else 
							 {
								 description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							 }
							 V_FMS_REF.add(fms_ref);
							 V_POST_STATUS.add(post_status);
							 V_POST_DT.add(post_dt);
							 V_POST_TIME.add(post_time);
							 V_IDOC_NO.add(idoc_no);
							 V_IDOC_STATUS.add(idoc_status);
							 V_STATUS_MSG.add(status_msg);
							 V_DOC_NO.add(doc_no);
							 V_COMPANY_CODE.add(company_code);
							 V_FISCAL_YR.add(fiscal_yr);
							 V_MSG_STATUS.add(msg_status);
								
							 V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, inv_cargo_no));
							 V_DUE_DT.add(inv_due_dt);
							 V_APPROVE_DT.add(inv_approved_dt);
							 V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
							 V_APPROVED_FLAG.add(inv_approved_flag);
							 V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
							 V_SAP_POSTED.add(description);
						 }
						 stmt1.close();
						 rset1.close();
					 }
					 else 
					 {
						 index++;
						 fms_ref = inv_invoice_no;
						 post_status="";
						 post_dt="";
						 post_time="";
						 idoc_no="";
						 idoc_status="";
						 status_msg="";
						 doc_no="";
						 company_code="";
						 fiscal_yr="";
						 msg_status="";
						 description="<font style=\"color:blue\">Pending</font>";
						 if(inv_custom_duty.equals("Y")) 
						 {
							 V_PLANT_ABBR.add("Indian Customs");
							 V_BU_ABBR.add(bu_plant_abbr);
						 }
						 else 
						 {
							 V_PLANT_ABBR.add(plant_abbr);
							 V_BU_ABBR.add(bu_plant_abbr);
						 }	
						 
						 V_FMS_REF.add(fms_ref);
						 V_POST_STATUS.add(post_status);
						 V_POST_DT.add(post_dt);
						 V_POST_TIME.add(post_time);
						 V_IDOC_NO.add(idoc_no);
						 V_IDOC_STATUS.add(idoc_status);
						 V_STATUS_MSG.add(status_msg);
						 V_DOC_NO.add(doc_no);
						 V_COMPANY_CODE.add(company_code);
						 V_FISCAL_YR.add(fiscal_yr);
						 V_MSG_STATUS.add(msg_status);
							
						 V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, inv_cargo_no));
						 V_DUE_DT.add(inv_due_dt);
						 V_APPROVE_DT.add(inv_approved_dt);
						 V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
						 V_APPROVED_FLAG.add(inv_approved_flag);
						 V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
						 V_SAP_POSTED.add(description);
					 }
				 }
				 stmt2.close();
				 rset2.close();
				 
				
			}
			rset.close();
			stmt.close();
			
			VINDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getACC_SAP_interface_gta_dtl() 
	{
		String function_nm="getSAP_interface_gta_dtl()";
		
		try 
		{
			int index =0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(TRANS_BU_UNIT),TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
					//INVOICE_DT as '' and INVOICE_NO as XML_NUM and INV_STATUS as '' 
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
			
				String inv_approved_dt = rset.getString(16)==null?"":rset.getString(16);
				String inv_approved_by = "System";
				String inv_approved_flag = "";
				String temp_inv_trans_bu_unit = rset.getString(15)==null?"":rset.getString(15);
				String cont_map =inv_cont_type+inv_agmt_no+"-"+inv_cont_no;
				String inv_trans_bu_unit="";
					
				inv_trans_bu_unit=temp_inv_trans_bu_unit;
				
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				String plant_abbr=utilBean.getCounterpartyBuABBR(conn,inv_counterparty_cd, comp_cd, inv_trans_bu_unit, "R");
				
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg = "";
				String doc_no="";
				String company_code = "";
				String fiscal_yr = "";
				String msg_status="";
				String description="";
				
				String queryStringTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt2=conn.prepareStatement(queryStringTemp);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_invoice_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count_dtl= rset2.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{ 
							index++;
							
							fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_code=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							
							VACC_BU_ABBR.add(bu_plant_abbr);			
							VACC_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							VACC_FMS_REF.add(fms_ref);
							VACC_POST_STATUS.add(post_status);
							VACC_POST_DT.add(post_dt);
							VACC_POST_TIME.add(post_time);
							VACC_IDOC_NO.add(idoc_no);
							VACC_IDOC_STATUS.add(idoc_status);
							VACC_STATUS_MSG.add(status_msg);
							VACC_DOC_NO.add(doc_no);
							VACC_COMPANY_CODE.add(company_code);
							VACC_FISCAL_YR.add(fiscal_yr);
							VACC_MSG_STATUS.add(msg_status);
							
							VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							VACC_DUE_DT.add(inv_due_dt);
							VACC_APPROVE_DT.add(inv_approved_dt);
							VACC_APPROVED_BY.add((inv_approved_by));
							VACC_APPROVED_FLAG.add(inv_approved_flag);
							VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							VACC_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
						
					}
					else 
					{
						index++;
						
						 fms_ref=inv_invoice_no;
						 post_status="";
						 post_dt="";
						 post_time="";
						 idoc_no="";
						 idoc_status="";
						 status_msg = "";
						 doc_no="";
						 company_code = "";
						 fiscal_yr = "";
						 msg_status="";
						 description="<font style=\"color:blue\">Pending</font>";
						 
						 VACC_BU_ABBR.add(bu_plant_abbr);			
						 VACC_PLANT_ABBR.add(plant_abbr);
						 
						 VACC_FMS_REF.add(fms_ref);
						 VACC_POST_STATUS.add(post_status);
						 VACC_POST_DT.add(post_dt);
						 VACC_POST_TIME.add(post_time);
						 VACC_IDOC_NO.add(idoc_no);
						 VACC_IDOC_STATUS.add(idoc_status);
						 VACC_STATUS_MSG.add(status_msg);
						 VACC_DOC_NO.add(doc_no);
						 VACC_COMPANY_CODE.add(company_code);
						 VACC_FISCAL_YR.add(fiscal_yr);
						 VACC_MSG_STATUS.add(msg_status);
							
						 VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						 VACC_DUE_DT.add(inv_due_dt);
						 VACC_APPROVE_DT.add(inv_approved_dt);
						 VACC_APPROVED_BY.add((inv_approved_by));
						 VACC_APPROVED_FLAG.add(inv_approved_flag);
						 VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
						 VACC_SAP_POSTED.add(description);
					}
					
				}
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			VACC_INDEX.add(index);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getACC_SAP_interface_gx_dtl() 
	{
		String function_nm="getSAP_interface_gx_dtl()";
		try 
		{
			int index = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(REPORT_DT,'DD/MM/YYYY'),TO_CHAR(GX_BU_UNIT),GX_COUNTERPARTY_CD "
					//INVOICE_DT as '' and INVOICE_NO as XML_NUM and INV_STATUS as '' 
					+ "FROM FMS_GX_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, eff_to_dt);
			rset=stmt.executeQuery();
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
				String inv_approved_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_by = "System";
				String inv_approved_flag = "";
				String inv_gx_bu_unit = rset.getString(16)==null?"":rset.getString(16);
				String gx_countpty_cd = rset.getString(17)==null?"":rset.getString(17);
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, "");
				//String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				String gx_bu_plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,gx_countpty_cd, comp_cd, inv_gx_bu_unit, "G");
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg = "";
				String doc_no="";
				String company_code = "";
				String fiscal_yr = "";
				String msg_status="";
				String description="";
				
				String queryStringTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt2=conn.prepareStatement(queryStringTemp);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_invoice_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count_dtl=rset2.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{ 
							index++;
							
							fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_code=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							
							VACC_BU_ABBR.add(bu_plant_abbr);
							VACC_PLANT_ABBR.add(gx_bu_plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							VACC_FMS_REF.add(fms_ref);
							VACC_POST_STATUS.add(post_status);
							VACC_POST_DT.add(post_dt);
							VACC_POST_TIME.add(post_time);
							VACC_IDOC_NO.add(idoc_no);
							VACC_IDOC_STATUS.add(idoc_status);
							VACC_STATUS_MSG.add(status_msg);
							VACC_DOC_NO.add(doc_no);
							VACC_COMPANY_CODE.add(company_code);
							VACC_FISCAL_YR.add(fiscal_yr);
							VACC_MSG_STATUS.add(msg_status);
							
							VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							VACC_DUE_DT.add(inv_due_dt);
							VACC_APPROVE_DT.add(inv_approved_dt);
							VACC_APPROVED_BY.add((inv_approved_by));
							VACC_APPROVED_FLAG.add(inv_approved_flag);
							VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							VACC_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else 
					{
						index++;
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg = "";
						doc_no="";
						company_code = "";
						fiscal_yr = "";
						msg_status="";
						description="<font style=\"color:blue\">Pending</font>";
						
						VACC_FMS_REF.add(fms_ref);
						VACC_POST_STATUS.add(post_status);
						VACC_POST_DT.add(post_dt);
						VACC_POST_TIME.add(post_time);
						VACC_IDOC_NO.add(idoc_no);
						VACC_IDOC_STATUS.add(idoc_status);
						VACC_STATUS_MSG.add(status_msg);
						VACC_DOC_NO.add(doc_no);
						VACC_COMPANY_CODE.add(company_code);
						VACC_FISCAL_YR.add(fiscal_yr);
						VACC_MSG_STATUS.add(msg_status);
						
						VACC_BU_ABBR.add(bu_plant_abbr);
						VACC_PLANT_ABBR.add(gx_bu_plant_abbr);
						
						VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						VACC_DUE_DT.add(inv_due_dt);
						VACC_APPROVE_DT.add(inv_approved_dt);
						VACC_APPROVED_BY.add((inv_approved_by));
						VACC_APPROVED_FLAG.add(inv_approved_flag);
						VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
						VACC_SAP_POSTED.add(description);
					}
			
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			VACC_INDEX.add(index);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getACC_SAP_interface_purchase_dtl(String type) 
	{
		String function_nm = "getACC_SAP_interface_purchase_dtl()";
		String eff_to_dt =utilDate.getDate(to_dt, "1");
		try
		{
			//Accrual Payable
			
			String inv_cont_type="";
			String inv_custom_duty="";
			
			int ctn=0;
			int index = 0;
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(REPORT_DT,'DD/MM/YYYY'),TO_CHAR(PLANT_SEQ), "
					+ "CARGO_NO, BOE_NO, INV_FLAG "
					//INVOICE_DT as '' and INVOICE_NO as XML_NUM and INV_STATUS as '' 
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
			if(type.equals("Purchase"))
			{
				queryString+="AND (INV_FLAG IN ('P','F')) AND CONTRACT_TYPE IN ('D','I','N') ";
			}
			else if(type.equals("LTCORA (Buy)"))
			{
				queryString+="AND CONTRACT_TYPE IN ('G','P') ";				
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_invoice_no = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_dt = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_amt = rset.getString(11)==null?"":rset.getString(11);
				String inv_net_pay_amt = rset.getString(12)==null?"":rset.getString(12);
				
				String inv_invoice_status = rset.getString(13)==null?"":rset.getString(13);
				String inv_due_dt = rset.getString(14)==null?"":rset.getString(14);
				String inv_approved_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_by = "System";
				String inv_approved_flag = "";
				String inv_plant_seq = rset.getString(16)==null?"":rset.getString(16);
				
				String inv_cargo_no = rset.getString(17)==null?"":rset.getString(17);
				String inv_boe_no = rset.getString(18)==null?"":rset.getString(18);
				
				String INV_FLAG = rset.getString(19)==null?"":rset.getString(19);
				
				if(inv_cont_type.equals("N") && (INV_FLAG.equals("CF") || INV_FLAG.equals("CP") || INV_FLAG.equals("CD"))) 
				{
					inv_custom_duty="Y";
				} 
				else
				{
					inv_custom_duty="N";
				}
				
				if(inv_plant_seq.equals("R")) 
				{
					inv_plant_seq="";
				}
				else if(inv_plant_seq.startsWith("P"))
				{
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				}
				else if(inv_plant_seq.startsWith("B"))
				{
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				}
				String plant_abbr="";
				if(inv_cont_type.equals("Y") || inv_cont_type.equals("A") || inv_cont_type.equals("H"))
				{
					String entity = inv_cont_type.equals("Y") ? "S": inv_cont_type.equals("A") ? "V" : "H";
					plant_abbr = utilBean.getCounterpartyBuPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, entity);
				}
				else
				{
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "T");
				}
				
				
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, "");
				//String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				 
				 String bu_region = "";
					
				 String queryStringTemp = "SELECT COUNT(*) "
						 + "FROM FMS_SAP_ACK_DTL "
						 + "WHERE COMPANY_CD=? AND FMS_REF=? ";
				 stmt2=conn.prepareStatement(queryStringTemp);
				 stmt2.setString(1, comp_cd);
				 stmt2.setString(2, inv_invoice_no);
				 rset2=stmt2.executeQuery();
				 if(rset2.next())
				 {
					 int count_dtl=rset2.getInt(1);
					 String fms_ref="";
					 String post_status="";
					 String post_dt="";
					 String post_time="";
					 String idoc_no="";
					 String idoc_status="";
					 String status_msg = "";
					 String doc_no="";
					 String company_code = "";
					 String fiscal_yr = "";
					 String msg_status="";
					 String description="";
					 if(count_dtl>0)
					 {
						 String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
									+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
									+ "FROM FMS_SAP_ACK_DTL "
									+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						 stmt1=conn.prepareStatement(queryString1);
						 stmt1.setString(1, comp_cd);
						 stmt1.setString(2, inv_invoice_no);
						 rset1=stmt1.executeQuery();
						 
						 
						 while(rset1.next())
						 {
							 index++;
							 fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							 post_status=rset1.getString(2)==null?"":rset1.getString(2);
							 post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							 post_time=rset1.getString(4)==null?"":rset1.getString(4);
							 idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							 idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							 status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							 doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							 company_code=rset1.getString(9)==null?"":rset1.getString(9);
							 fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							 msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							 
							 if(inv_custom_duty.equals("Y")) 
							 {
								 VACC_PLANT_ABBR.add("Indian Customs");
								 VACC_BU_ABBR.add(bu_plant_abbr);
							 }
							 else 
							 {
								 VACC_PLANT_ABBR.add(plant_abbr);
								 VACC_BU_ABBR.add(bu_plant_abbr);
							 }	
							 
							 if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							 {
								 char a = idoc_status.charAt(0);
								 if(Character.isDigit(a)) 
								 {
									 description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								 }
								 else 
								 {
									 description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								 }
							 }
							 else 
							 {
								 description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							 }
							 VACC_FMS_REF.add(fms_ref);
							 VACC_POST_STATUS.add(post_status);
							 VACC_POST_DT.add(post_dt);
							 VACC_POST_TIME.add(post_time);
							 VACC_IDOC_NO.add(idoc_no);
							 VACC_IDOC_STATUS.add(idoc_status);
							 VACC_STATUS_MSG.add(status_msg);
							 VACC_DOC_NO.add(doc_no);
							 VACC_COMPANY_CODE.add(company_code);
							 VACC_FISCAL_YR.add(fiscal_yr);
							 VACC_MSG_STATUS.add(msg_status);
								
							 VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, inv_cargo_no));
							 VACC_DUE_DT.add(inv_due_dt);
							 VACC_APPROVE_DT.add(inv_approved_dt);
							 VACC_APPROVED_BY.add((inv_approved_by));
							 VACC_APPROVED_FLAG.add(inv_approved_flag);
							 VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							 VACC_SAP_POSTED.add(description);
						 }
						 stmt1.close();
						 rset1.close();
					 }
					 else 
					 {
						 index++;
						 fms_ref = inv_invoice_no;
						 post_status="";
						 post_dt="";
						 post_time="";
						 idoc_no="";
						 idoc_status="";
						 status_msg="";
						 doc_no="";
						 company_code="";
						 fiscal_yr="";
						 msg_status="";
						 description="<font style=\"color:blue\">Pending</font>";
						 if(inv_custom_duty.equals("Y")) 
						 {
							 VACC_PLANT_ABBR.add("Indian Customs");
							 VACC_BU_ABBR.add(bu_plant_abbr);
						 }
						 else 
						 {
							 VACC_PLANT_ABBR.add(plant_abbr);
							 VACC_BU_ABBR.add(bu_plant_abbr);
						 }	
						 
						 VACC_FMS_REF.add(fms_ref);
						 VACC_POST_STATUS.add(post_status);
						 VACC_POST_DT.add(post_dt);
						 VACC_POST_TIME.add(post_time);
						 VACC_IDOC_NO.add(idoc_no);
						 VACC_IDOC_STATUS.add(idoc_status);
						 VACC_STATUS_MSG.add(status_msg);
						 VACC_DOC_NO.add(doc_no);
						 VACC_COMPANY_CODE.add(company_code);
						 VACC_FISCAL_YR.add(fiscal_yr);
						 VACC_MSG_STATUS.add(msg_status);
							
						 VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, inv_cargo_no));
						 VACC_DUE_DT.add(inv_due_dt);
						 VACC_APPROVE_DT.add(inv_approved_dt);
						 VACC_APPROVED_BY.add((inv_approved_by));
						 VACC_APPROVED_FLAG.add(inv_approved_flag);
						 VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
						 VACC_SAP_POSTED.add(description);
					 }
				 }
				 stmt2.close();
				 rset2.close();
				 
				
			}
			rset.close();
			stmt.close();
			
			VACC_INDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getACC_SAP_Receivable()
	{
		String function_nm = "getACC_SAP_Receivable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VACC_TITLE.size();i++)
			{
				if(VACC_TITLE.elementAt(i).toString().equals("RLNG (SN | LOA | IGX)") || 
						VACC_TITLE.elementAt(i).toString().equals("DLNG (SN | LOA | IGX)") || 
						VACC_TITLE.elementAt(i).toString().equals("LTCORA (Sell)") || 
						VACC_TITLE.elementAt(i).toString().equals("Derivatives (Invoice)") 
						)
				{
					if(VACC_TITLE.elementAt(i).equals("RLNG (SN | LOA | IGX)"))
					{
						getACC_SAP_interface_sell_dtl("RLNG"); 
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("DLNG (SN | LOA | IGX)"))
					{
						//getACC_SAP_interface_sell_dtl("DLNG");
						count++;
						VACC_INDEX.add("0");
					}
					else if(VACC_TITLE.elementAt(i).equals("LTCORA (Sell)"))
					{
						getACC_SAP_interface_sell_dtl("LTCORA (Sell)"); 
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("Derivatives (Invoice)"))
					{
						getACC_SAP_interface_derivatives_dtl("I"); 
						count++;
					}
				}
			}
			VACC_SAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getACC_SAP_Payable()
	{
		String function_nm = "getACC_SAP_Payable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VACC_TITLE.size();i++)
			{
				if(!VACC_TITLE.elementAt(i).toString().equals("RLNG (SN | LOA | IGX)") || 
						!VACC_TITLE.elementAt(i).toString().equals("DLNG (SN | LOA | IGX)") || 
						!VACC_TITLE.elementAt(i).toString().equals("LTCORA (Sell)") || 
						!VACC_TITLE.elementAt(i).toString().equals("Derivatives (Invoice)") ||
						!VACC_TITLE.elementAt(i).toString().equals("IGX Advance")||
						!VACC_TITLE.elementAt(i).toString().equals("KYC Advance")
						)
				{
					if(VACC_TITLE.elementAt(i).equals("Purchase"))
					{
						getACC_SAP_interface_purchase_dtl("Purchase"); 
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("GX"))
					{
						getACC_SAP_interface_gx_dtl();
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("GTA"))
					{
						getACC_SAP_interface_gta_dtl();
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("LTCORA (Buy)"))
					{
						getACC_SAP_interface_purchase_dtl("LTCORA (Buy)"); 
						count++;
					}
					else if(VACC_TITLE.elementAt(i).equals("Derivatives (Remittance)"))
					{
						getACC_SAP_interface_derivatives_dtl("R"); 
						count++;
					}
				}
			}
			VACC_SAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_MTM_Payable()
	{
		String function_nm = "getSAP_MTM_Payable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VMTM_TITLE.size();i++)
			{
				if(VMTM_TITLE.elementAt(i).toString().equals("Derivatives (Remittance)")) 
				{
					getMTM_SAP_interface_derv_dtl("R");
					count++;
				}
			}
			VMTM_SAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSAP_MTM_Receivable()
	{
		String function_nm = "getSAP_MTM_Receivable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VMTM_TITLE.size();i++)
			{
				if(VMTM_TITLE.elementAt(i).toString().equals("Derivatives (Invoice)"))
				{
					getMTM_SAP_interface_derv_dtl("I");
					count++;
				}
			}
			VMTM_SAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getMTM_SAP_interface_derv_dtl(String type) 
	{
		String function_nm="getMTM_SAP_interface_derv_dtl()";
		try 
		{
			//MTM Derivatives
			
			int index = 0;
			int count = 0;
			int selCnt1 = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			int selCnt = 0;
			
			String queryString="";
			
			queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
					+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PLANT_SEQ),TO_CHAR(REPORT_DT,'DD/MM/YYYY'),INSTRUMENT_NO "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
			queryString+="AND CONTRACT_TYPE IN ('V') AND INV_TYPE = ? "
					+ "AND IS_MTM =? ";
			String temp_query = queryString;
			stmt=conn.prepareStatement(temp_query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, eff_to_dt);
			stmt.setString(4, type);
			stmt.setString(5, "Y");
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_bu_state = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_no = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_dt = rset.getString(11)==null?"":rset.getString(11);
				String inv_invoice_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_net_pay_amt = rset.getString(13)==null?"":rset.getString(13);
				String inv_invoice_status = rset.getString(14)==null?"":rset.getString(14);
				String inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_plant_seq = rset.getString(16)==null?"":rset.getString(16);
				String report_date = rset.getString(17)==null?"":rset.getString(17);
				String cargo_instrment_no = rset.getString(18)==null?"":rset.getString(18);
				
				double totalNetPay = 0.0;
				
				if (inv_net_pay_amt != null && !inv_net_pay_amt.isEmpty()) 
				{
					double amount = Double.parseDouble(inv_net_pay_amt);
					totalNetPay += amount;
			    }
				
				if(inv_plant_seq.equals("R")) 
				{
					inv_plant_seq="";
				}
				else if(inv_plant_seq.startsWith("P"))
				{
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				}
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "T");
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				String cont_map = "";
				
				if(cont_map.equals(""))
				{
					cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
				}
				else
				{
					cont_map += " ,"+utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
				}
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg="";
				String doc_no="";
				String company_cd = "";
				String fiscal_yr = "";
				String msg_status = "";
				String description="";
				
				String queryTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt3=conn.prepareStatement(queryTemp);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, inv_invoice_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					int count_dtl = rset3.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						
						while(rset1.next())
						{ 
							index++;					
							fms_ref=rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_cd=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status= rset1.getString(11)==null?"":rset1.getString(11);
							
							
							VMTM_BU_ABBR.add(bu_plant_abbr);
							VMTM_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							VMTM_FMS_REF.add(fms_ref);
							VMTM_POST_STATUS.add(post_status);
							VMTM_POST_DT.add(post_dt);
							VMTM_POST_TIME.add(post_time);
							VMTM_IDOC_NO.add(idoc_no);
							VMTM_IDOC_STATUS.add(idoc_status);
							VMTM_STATUS_MSG.add(status_msg);
							VMTM_DOC_NO.add(doc_no);
							VMTM_COMPANY_CODE.add(company_cd);
							VMTM_FISCAL_YR.add(fiscal_yr);
							VMTM_MSG_STATUS.add(msg_status);
							VMTM_CONT_NO.add(cont_map);
							//VMTM_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							VMTM_DUE_DT.add(inv_due_dt);
							VMTM_APPROVE_DT.add(report_date);
							VMTM_APPROVED_BY.add("System");
							VMTM_APPROVED_FLAG.add("");//Temporary Null
							VMTM_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							VMTM_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						index++;
						
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg="";
						doc_no="";
						company_cd="";
						fiscal_yr="";
						msg_status= "";
						description="<font style=\"color:blue\">Pending</font>";
						
						VMTM_FMS_REF.add(fms_ref);
						VMTM_POST_STATUS.add(post_status);
						VMTM_POST_DT.add(post_dt);
						VMTM_POST_TIME.add(post_time);
						VMTM_IDOC_NO.add(idoc_no);
						VMTM_IDOC_STATUS.add(idoc_status);
						VMTM_STATUS_MSG.add(status_msg);
						VMTM_DOC_NO.add(doc_no);
						VMTM_COMPANY_CODE.add(company_cd);
						VMTM_FISCAL_YR.add(fiscal_yr);
						VMTM_MSG_STATUS.add(msg_status);
						VMTM_CONT_NO.add(cont_map);
						//VMTM_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						VMTM_DUE_DT.add(inv_due_dt);
						VMTM_APPROVE_DT.add(report_date);
						VMTM_APPROVED_BY.add("System");//By Default
						VMTM_APPROVED_FLAG.add("");//Temporary Null
						VMTM_NET_PAYABLE_AMT.add(inv_net_pay_amt);
						VMTM_SAP_POSTED.add(description);
						VMTM_BU_ABBR.add(bu_plant_abbr);
						VMTM_PLANT_ABBR.add(plant_abbr);
					}
				}
				rset3.close();
				stmt3.close();
			}
			rset.close();
			stmt.close();

			VMTM_INDEX.add(index);
			//count++;
			//VMTM_SAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getACC_SAP_interface_sell_dtl(String type) 
	{
		String function_nm="getACC_SAP_interface_sell_dtl()";
		try 
		{
			//Accrual Sales
			
			int index = 0;
			int count = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="";
			
			if(type.equals("RLNG") || type.equals("LTCORA (Sell)"))
			{
				queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PLANT_SEQ),TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
						//INVOICE_DT as '' and INVOICE_NO as XML_NUM and INV_STATUS as '' 
						+ "FROM FMS_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND XML_NUM IS NOT NULL "
						+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
				if(type.equals("RLNG"))
				{
					queryString+="AND CONTRACT_TYPE IN ('S','L','X') ";
				}
				else if(type.equals("LTCORA (Sell)"))
				{
					queryString+="AND CONTRACT_TYPE IN ('O','Q') ";				
				}
			}
			else if(type.equals("DLNG"))
			{
				//HM : DLNG Accrual pending, need to be handled after finalization;
				
				/*queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PLANT_SEQ),TO_CHAR(REPORT_DT,'DD/MM/YYYY') "
						//INVOICE_DT as '' and INVOICE_NO as XML_NUM and INV_STATUS as '' 
						+ "FROM FMS_DLNG_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND XML_NUM IS NOT NULL "
						+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";*/
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, eff_to_dt);
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_bu_state = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_no = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_dt = rset.getString(11)==null?"":rset.getString(11);
				String inv_invoice_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_net_pay_amt = rset.getString(13)==null?"":rset.getString(13);
				String inv_invoice_status = rset.getString(14)==null?"":rset.getString(14);
				String inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_plant_seq = rset.getString(16)==null?"":rset.getString(16);
				String report_date = rset.getString(17)==null?"":rset.getString(17);
				
				if(inv_plant_seq.equals("R")) 
				{
					inv_plant_seq="";
				}
				else if(inv_plant_seq.startsWith("P"))
				{
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				}
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "C");
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, "");
				//String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg="";
				String doc_no="";
				String company_cd = "";
				String fiscal_yr = "";
				String msg_status = "";
				String description="";
			
				String queryTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt3=conn.prepareStatement(queryTemp);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, inv_invoice_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					int count_dtl = rset3.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						
						while(rset1.next())
						{ 
							index++;					
							fms_ref=rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_cd=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status= rset1.getString(11)==null?"":rset1.getString(11);
							
							
							VACC_BU_ABBR.add(bu_plant_abbr);
							VACC_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							VACC_FMS_REF.add(fms_ref);
							VACC_POST_STATUS.add(post_status);
							VACC_POST_DT.add(post_dt);
							VACC_POST_TIME.add(post_time);
							VACC_IDOC_NO.add(idoc_no);
							VACC_IDOC_STATUS.add(idoc_status);
							VACC_STATUS_MSG.add(status_msg);
							VACC_DOC_NO.add(doc_no);
							VACC_COMPANY_CODE.add(company_cd);
							VACC_FISCAL_YR.add(fiscal_yr);
							VACC_MSG_STATUS.add(msg_status);
							VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							VACC_DUE_DT.add(inv_due_dt);
							VACC_APPROVE_DT.add(report_date);
							VACC_APPROVED_BY.add("System");
							VACC_APPROVED_FLAG.add("");//Temporary Null
							VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							VACC_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						index++;
						
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg="";
						doc_no="";
						company_cd="";
						fiscal_yr="";
						msg_status= "";
						description="<font style=\"color:blue\">Pending</font>";
						
						VACC_FMS_REF.add(fms_ref);
						VACC_POST_STATUS.add(post_status);
						VACC_POST_DT.add(post_dt);
						VACC_POST_TIME.add(post_time);
						VACC_IDOC_NO.add(idoc_no);
						VACC_IDOC_STATUS.add(idoc_status);
						VACC_STATUS_MSG.add(status_msg);
						VACC_DOC_NO.add(doc_no);
						VACC_COMPANY_CODE.add(company_cd);
						VACC_FISCAL_YR.add(fiscal_yr);
						VACC_MSG_STATUS.add(msg_status);
						VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						VACC_DUE_DT.add(inv_due_dt);
						VACC_APPROVE_DT.add(report_date);
						VACC_APPROVED_BY.add("System");//By Default
						VACC_APPROVED_FLAG.add("");//Temporary Null
						VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
						VACC_SAP_POSTED.add(description);
						VACC_BU_ABBR.add(bu_plant_abbr);
						VACC_PLANT_ABBR.add(plant_abbr);
					}
				}
				rset3.close();
				stmt3.close();
				
			}
			rset.close();
			stmt.close();
			VACC_INDEX.add(index);
			//count++;
			//VACC_SAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getACC_SAP_interface_derivatives_dtl(String type) 
	{
		String function_nm="getACC_SAP_interface_derivatives_dtl()";
		try 
		{
			//Accrual Derivatives
			
			int index = 0;
			int count = 0;
			int selCnt1 = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString2="";
			queryString2="SELECT DISTINCT XML_NUM "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND XML_NUM IS NOT NULL "
					+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_TYPE=? AND IS_MTM=? ";
			String temp_query2 = queryString2;
			stmt2=conn.prepareStatement(temp_query2);
			stmt2.setString(++selCnt1, comp_cd);
			stmt2.setString(++selCnt1, from_dt);
			stmt2.setString(++selCnt1, eff_to_dt);
			stmt2.setString(++selCnt1, type);
			stmt2.setString(++selCnt1, "N");
			rset2=stmt2.executeQuery();
			while(rset2.next()) 
			{
				String mst_inv_no=rset2.getString(1)==null?"":rset2.getString(1);;
				
				int selCnt = 0;
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg="";
				String doc_no="";
				String company_cd = "";
				String fiscal_yr = "";
				String msg_status = "";
				String description="";
				
				String inv_counterparty_cd = "";
				String inv_fins_year = "";
				String inv_agmt_no =  "";
				String inv_agmt_rev_no =  "";
				String inv_cont_no = "";
				String inv_cont_rev =  "";
				String inv_cont_type =  "";
				String inv_bu_unit =  "";
				String inv_bu_state = "";
				String inv_invoice_no =  "";
				String inv_invoice_dt =  "";
				String inv_invoice_amt = "";
				String inv_net_pay_amt = "";
				double totalNetPay = 0.0;
				
				String inv_invoice_status = "";
				String inv_due_dt = "";
				String inv_approved_dt = "";
				String inv_approved_by = "";
				String inv_approved_flag = "";
				String inv_plant_seq = "";
				String report_date =  "";
				String cargo_instrment_no =  "";
				
				String plant_abbr="";
				String bu_plant_abbr="";
				
				String cont_map = "";
				
				String queryString="";
				
				queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "XML_NUM,'',ACCRUAL_AMT,GROSS_AMT,'',TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PLANT_SEQ),TO_CHAR(REPORT_DT,'DD/MM/YYYY'),INSTRUMENT_NO "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND XML_NUM IS NOT NULL "
						+ "AND REPORT_DT >= TO_DATE(?,'DD/MM/YYYY') AND REPORT_DT < TO_DATE(?,'DD/MM/YYYY') ";
				queryString+="AND CONTRACT_TYPE IN ('V') AND INV_TYPE = ? "
						+ "AND IS_MTM =? AND XML_NUM=?";
				String temp_query = queryString;
				stmt=conn.prepareStatement(temp_query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, from_dt);
				stmt.setString(3, eff_to_dt);
				stmt.setString(4, type);
				stmt.setString(5, "N");
				stmt.setString(6, mst_inv_no);
				rset=stmt.executeQuery();
				while(rset.next()) 
				{
					inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
					inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
					inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
					inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
					inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
					inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
					inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
					inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
					inv_bu_state = rset.getString(9)==null?"":rset.getString(9);
					inv_invoice_no = rset.getString(10)==null?"":rset.getString(10);
					inv_invoice_dt = rset.getString(11)==null?"":rset.getString(11);
					inv_invoice_amt = rset.getString(12)==null?"":rset.getString(12);
					inv_net_pay_amt = rset.getString(13)==null?"":rset.getString(13);
					inv_invoice_status = rset.getString(14)==null?"":rset.getString(14);
					inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
					inv_plant_seq = rset.getString(16)==null?"":rset.getString(16);
					report_date = rset.getString(17)==null?"":rset.getString(17);
					cargo_instrment_no = rset.getString(18)==null?"":rset.getString(18);
					
					if (inv_net_pay_amt != null && !inv_net_pay_amt.isEmpty()) 
					{
						double amount = Double.parseDouble(inv_net_pay_amt);
						totalNetPay += amount;
				    }
					
					if(inv_plant_seq.equals("R")) 
					{
						inv_plant_seq="";
					}
					else if(inv_plant_seq.startsWith("P"))
					{
						inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
					}
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "T");
					bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
					
					if(cont_map.equals(""))
					{
						cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
					}
					else
					{
						cont_map += " ,"+utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
					}
					
					/*String fms_ref="";
					String post_status="";
					String post_dt="";
					String post_time="";
					String idoc_no="";
					String idoc_status="";
					String status_msg="";
					String doc_no="";
					String company_cd = "";
					String fiscal_yr = "";
					String msg_status = "";
					String description="";*/
					
					String queryTemp = "SELECT COUNT(*) "
							+ "FROM FMS_SAP_ACK_DTL "
							+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
					stmt3=conn.prepareStatement(queryTemp);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, inv_invoice_no);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						int count_dtl = rset3.getInt(1);
						if(count_dtl>0)
						{
							String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
									+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
									+ "FROM FMS_SAP_ACK_DTL "
									+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, inv_invoice_no);
							rset1=stmt1.executeQuery();
							
							while(rset1.next())
							{ 
								index++;					
								fms_ref=rset1.getString(1)==null?"":rset1.getString(1);
								post_status=rset1.getString(2)==null?"":rset1.getString(2);
								post_dt=rset1.getString(3)==null?"":rset1.getString(3);
								post_time=rset1.getString(4)==null?"":rset1.getString(4);
								idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
								idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
								status_msg=rset1.getString(7)==null?"":rset1.getString(7);
								doc_no=rset1.getString(8)==null?"":rset1.getString(8);
								company_cd=rset1.getString(9)==null?"":rset1.getString(9);
								fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
								msg_status= rset1.getString(11)==null?"":rset1.getString(11);
								
								
								VACC_BU_ABBR.add(bu_plant_abbr);
								VACC_PLANT_ABBR.add(plant_abbr);
								
								if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
								{
									char a = idoc_status.charAt(0);
									if(Character.isDigit(a)) 
									{
										description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
									}
									else 
									{
										description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
									}
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
								VACC_FMS_REF.add(fms_ref);
								VACC_POST_STATUS.add(post_status);
								VACC_POST_DT.add(post_dt);
								VACC_POST_TIME.add(post_time);
								VACC_IDOC_NO.add(idoc_no);
								VACC_IDOC_STATUS.add(idoc_status);
								VACC_STATUS_MSG.add(status_msg);
								VACC_DOC_NO.add(doc_no);
								VACC_COMPANY_CODE.add(company_cd);
								VACC_FISCAL_YR.add(fiscal_yr);
								VACC_MSG_STATUS.add(msg_status);
								VACC_CONT_NO.add(cont_map);
								//VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
								VACC_DUE_DT.add(inv_due_dt);
								VACC_APPROVE_DT.add(report_date);
								VACC_APPROVED_BY.add("System");
								VACC_APPROVED_FLAG.add("");//Temporary Null
								VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
								VACC_SAP_POSTED.add(description);
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							index++;
							
							fms_ref=inv_invoice_no;
							post_status="";
							post_dt="";
							post_time="";
							idoc_no="";
							idoc_status="";
							status_msg="";
							doc_no="";
							company_cd="";
							fiscal_yr="";
							msg_status= "";
							description="<font style=\"color:blue\">Pending</font>";
							
							VACC_FMS_REF.add(fms_ref);
							VACC_POST_STATUS.add(post_status);
							VACC_POST_DT.add(post_dt);
							VACC_POST_TIME.add(post_time);
							VACC_IDOC_NO.add(idoc_no);
							VACC_IDOC_STATUS.add(idoc_status);
							VACC_STATUS_MSG.add(status_msg);
							VACC_DOC_NO.add(doc_no);
							VACC_COMPANY_CODE.add(company_cd);
							VACC_FISCAL_YR.add(fiscal_yr);
							VACC_MSG_STATUS.add(msg_status);
							VACC_CONT_NO.add(cont_map);
							//VACC_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							VACC_DUE_DT.add(inv_due_dt);
							VACC_APPROVE_DT.add(report_date);
							VACC_APPROVED_BY.add("System");//By Default
							VACC_APPROVED_FLAG.add("");//Temporary Null
							VACC_NET_PAYABLE_AMT.add(inv_net_pay_amt);
							VACC_SAP_POSTED.add(description);
							VACC_BU_ABBR.add(bu_plant_abbr);
							VACC_PLANT_ABBR.add(plant_abbr);
						}
					}
					rset3.close();
					stmt3.close();
				}
				rset.close();
				stmt.close();
			}
			rset2.close();
			stmt2.close();

			VACC_INDEX.add(index);
			//count++;
			//VACC_SAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_adv_dtl(String gx) 
	{
		String function_nm="getSAP_interface_adv_dtl()";
		try 
		{
			int index = 0;
			int count = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="SELECT A.COUNTERPARTY_CD,C.SEC_INT_REF,B.AGMT_NO,B.AGMT_REV,B.CONT_NO,B.CONT_REV,B.CONTRACT_TYPE," //BU_UNIT
					+ "TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.APRV_DT,'DD/MM/YYYY'),A.APRV_BY,A.STATUS,A.VALUE,A.CURRENCY,B.ENTITY_CD " 
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.GX=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.FILE_TYPE=? AND A.SEC_TYPE=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.GX=B.GX AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.GX=C.GX AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO ";
			queryString +=" UNION ";
			queryString +="SELECT A.COUNTERPARTY_CD,C.SEC_INT_REF,B.AGMT_NO,B.AGMT_REV,B.CONT_NO,B.CONT_REV,B.CONTRACT_TYPE," //BU_UNIT
					+ "TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.APRV_DT,'DD/MM/YYYY'),A.APRV_BY,A.STATUS,A.VALUE,A.CURRENCY,B.ENTITY_CD " 
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.GX=? AND A.SAP_REVERSAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_REVERSAL_DT,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(A.SAP_REVERSAL_DT,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND C.FILE_TYPE=? AND A.SEC_TYPE=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.GX=B.GX AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND A.GX=C.GX AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gx);
			stmt.setString(3, "Y");
			stmt.setString(4, from_dt);
			stmt.setString(5, eff_to_dt);
			stmt.setString(6, "PDF");
			stmt.setString(7, "ADV");
			stmt.setString(8, comp_cd);
			stmt.setString(9, gx);
			stmt.setString(10, "Y");
			stmt.setString(11, from_dt);
			stmt.setString(12, eff_to_dt);
			stmt.setString(13, "PDF_REV");
			stmt.setString(14, "ADV");
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String sec_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String sec_internal_ref = rset.getString(2)==null?"":rset.getString(2);
				String sec_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String sec_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String sec_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String sec_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String sec_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String sec_due_dt = rset.getString(8)==null?"":rset.getString(8);
				String sec_approved_dt = rset.getString(9)==null?"":rset.getString(9);
				String sec_approved_by = rset.getString(10)==null?"":rset.getString(10);
				String sec_approved_flag = rset.getString(11)==null?"":rset.getString(11);
				String sec_amt = rset.getString(12)==null?"":rset.getString(12);
				String sec_amt_unit = rset.getString(13)==null?"":rset.getString(13);
				String sec_entity_cd = rset.getString(14)==null?"":rset.getString(14);
				String sec_gx_bu_seq_no = "";
				String sec_bu_seq_no="";
				
				String queryString1="";

				if(gx.equals("K"))
				{
					int countQ1=0;
					if(sec_cont_type.equals("D") || sec_cont_type.equals("I") || sec_cont_type.equals("T"))
					{
						queryString1 = "SELECT X.PLANT_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT B.PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_PLANT B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=?  "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) X, "
								+ "(SELECT B.PLANT_SEQ_NO  "
								+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_BU B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) Y ";
					}
					else if(sec_cont_type.equals("N"))
					{
						queryString1 = "SELECT X.PLANT_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT B.PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CONT_PLANT B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=?  "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) X, "
								+ "(SELECT B.PLANT_SEQ_NO  "
								+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CONT_BU B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) Y ";
					}
					else if(sec_cont_type.equals("S") || sec_cont_type.equals("L") || sec_cont_type.equals("X") ||
							sec_cont_type.equals("F") || sec_cont_type.equals("E") || sec_cont_type.equals("W"))
					{
						queryString1 = "SELECT X.PLANT_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT B.PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_PLANT B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) X, "
								+ "(SELECT B.PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_BU B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV  "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV  "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) Y ";
					}
					else if(sec_cont_type.equals("G") || sec_cont_type.equals("P") || sec_cont_type.equals("O") || sec_cont_type.equals("Q"))
					{
						queryString1 = "SELECT X.PLANT_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT CP.PLANT_SEQ_NO "
							    + "FROM FMS_LTCORA_CONT_MST CM, FMS_LTCORA_CONT_PLANT CP "
							    + "WHERE CM.COMPANY_CD=? AND CM.COUNTERPARTY_CD=? "
							    + "AND CM.AGMT_NO=? AND CM.CONT_NO=? AND CM.CONTRACT_TYPE=? AND CM.BUY_SALE=? "
							    + "AND CM.CONT_REV=(SELECT MAX(CM2.CONT_REV) FROM FMS_LTCORA_CONT_MST CM2 "
							    + "WHERE CM.COMPANY_CD=CM2.COMPANY_CD AND CM.COUNTERPARTY_CD=CM2.COUNTERPARTY_CD "
							    + "AND CM.AGMT_NO=CM2.AGMT_NO AND CM.AGMT_REV=CM2.AGMT_REV AND CM.CONT_NO=CM2.CONT_NO "
							    + "AND CM.CONTRACT_TYPE=CM2.CONTRACT_TYPE AND CM.BUY_SALE=CM2.BUY_SALE) "
							    + "AND CM.COMPANY_CD=CP.COMPANY_CD AND CM.COUNTERPARTY_CD=CP.COUNTERPARTY_CD "
							    + "AND CM.AGMT_NO=CP.AGMT_NO AND CM.AGMT_REV=CP.AGMT_REV "
							    + "AND CM.CONT_NO=CP.CONT_NO AND CM.CONT_REV=CP.CONT_REV "
							    + "AND CM.CONTRACT_TYPE=CP.CONTRACT_TYPE) X, "
							    + "(SELECT CB.PLANT_SEQ_NO "
							    + "FROM FMS_LTCORA_CONT_MST CM, FMS_LTCORA_CONT_BU CB "
							    + "WHERE CM.COMPANY_CD=? AND CM.COUNTERPARTY_CD=? "
							    + "AND CM.AGMT_NO=? AND CM.CONT_NO=? AND CM.CONTRACT_TYPE=? AND CM.BUY_SALE=? "
							    + "AND CM.CONT_REV=(SELECT MAX(CM2.CONT_REV) FROM FMS_LTCORA_CONT_MST CM2 "
							    + "WHERE CM.COMPANY_CD=CM2.COMPANY_CD AND CM.COUNTERPARTY_CD=CM2.COUNTERPARTY_CD "
							    + "AND CM.AGMT_NO=CM2.AGMT_NO AND CM.AGMT_REV=CM2.AGMT_REV AND CM.CONT_NO=CM2.CONT_NO "
							    + "AND CM.CONTRACT_TYPE=CM2.CONTRACT_TYPE AND CM.BUY_SALE=CM2.BUY_SALE) "
							    + "AND CM.COMPANY_CD=CB.COMPANY_CD AND CM.COUNTERPARTY_CD=CB.COUNTERPARTY_CD "
							    + "AND CM.AGMT_NO=CB.AGMT_NO AND CM.AGMT_REV=CB.AGMT_REV "
							    + "AND CM.CONT_NO=CB.CONT_NO AND CM.CONT_REV=CB.CONT_REV "
							    + "AND CM.CONTRACT_TYPE=CB.CONTRACT_TYPE) Y";
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++countQ1, comp_cd);
					stmt1.setString(++countQ1, sec_counterparty_cd);
					//stmt1.setString(++countQ1, sec_entity_cd);
					stmt1.setString(++countQ1, sec_agmt_no);
					stmt1.setString(++countQ1, sec_cont_no);
					stmt1.setString(++countQ1, sec_cont_type);
					if(sec_cont_type.equals("G") || sec_cont_type.equals("P")) 
					{
						stmt1.setString(++countQ1, "T");
					}
					else if(sec_cont_type.equals("O") || sec_cont_type.equals("Q")) 
					{
						stmt1.setString(++countQ1, "C");
					}
					stmt1.setString(++countQ1, comp_cd);
					stmt1.setString(++countQ1, sec_counterparty_cd);
					//stmt1.setString(++countQ1, sec_entity_cd);
					stmt1.setString(++countQ1, sec_agmt_no);
					stmt1.setString(++countQ1, sec_cont_no);
					stmt1.setString(++countQ1, sec_cont_type);
					if(sec_cont_type.equals("G") || sec_cont_type.equals("P")) 
					{
						stmt1.setString(++countQ1, "T");
					}
					else if(sec_cont_type.equals("O") || sec_cont_type.equals("Q")) 
					{
						stmt1.setString(++countQ1, "C");
					}
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						sec_gx_bu_seq_no=rset1.getString(1)==null?"":rset1.getString(1);
						sec_bu_seq_no=rset1.getString(2)==null?"":rset1.getString(2);
					}
					stmt1.close();
					rset1.close();
				}
				else
				{
					if(sec_cont_type.equals("I"))
					{
						queryString1 = "SELECT X.GX_BU_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT B.GX_BU_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_GX_BU B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=?  "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) X, "
								+ "(SELECT B.PLANT_SEQ_NO  "
								+ "FROM FMS_TRADER_CONT_MST A, FMS_TRADER_CONT_BU B  "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO  "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) Y ";
					}
					else
					{
						queryString1 = "SELECT X.GX_BU_SEQ_NO, Y.PLANT_SEQ_NO FROM "
								+ "(SELECT B.GX_BU_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_GX_BU B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) X, "
								+ "(SELECT B.PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_CONT_BU B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV  "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV  "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) Y ";
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, sec_entity_cd);
					stmt1.setString(3, sec_agmt_no);
					stmt1.setString(4, sec_cont_no);
					stmt1.setString(5, sec_cont_type);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, sec_entity_cd);
					stmt1.setString(8, sec_agmt_no);
					stmt1.setString(9, sec_cont_no);
					stmt1.setString(10, sec_cont_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						sec_gx_bu_seq_no=rset1.getString(1)==null?"":rset1.getString(1);
						sec_bu_seq_no=rset1.getString(2)==null?"":rset1.getString(2);
					}
					stmt1.close();
					rset1.close();
				}
				
				String gx_bu_plant_abbr="";
				
				if(gx.equals("I"))
				{
					gx_bu_plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,sec_counterparty_cd, comp_cd, sec_gx_bu_seq_no, "G");
				}
				else
				{
					if(sec_cont_type.equals("S") || sec_cont_type.equals("L") || sec_cont_type.equals("X") ||
							sec_cont_type.equals("F") || sec_cont_type.equals("E") || sec_cont_type.equals("W")||
							sec_cont_type.equals("O") || sec_cont_type.equals("Q"))
					{
						gx_bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,sec_counterparty_cd, comp_cd, sec_gx_bu_seq_no, "C");
					}
					else if(sec_cont_type.equals("G") || sec_cont_type.equals("P") || sec_cont_type.equals("D") || sec_cont_type.equals("I") || sec_cont_type.equals("N") || sec_cont_type.equals("T"))
					{
						gx_bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,sec_counterparty_cd, comp_cd, sec_gx_bu_seq_no, "T");
					}
				}
				String entity_abbr=utilBean.getCounterpartyABBR(conn,sec_entity_cd);
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, sec_counterparty_cd, sec_agmt_no, sec_agmt_rev_no, sec_cont_no, sec_cont_rev, sec_cont_type, "");
				//String cont_map = utilBean.getDisplayDealMapping(sec_agmt_no, sec_agmt_rev_no, sec_cont_no, sec_cont_rev, sec_cont_type);
				cont_map=entity_abbr+"-"+cont_map; // Reflecting Entity COunterparty for IGX Advance
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, sec_bu_seq_no, "B");
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg = "";
				String doc_no="";
				String company_code = "";
				String fiscal_yr = "";
				String msg_status="";
				String description="";
				
				String queryStringTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt2=conn.prepareStatement(queryStringTemp);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, sec_internal_ref);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					int count_dtl=rset2.getInt(1);
					if(count_dtl>0)
					{
						queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, sec_internal_ref);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{ 
							index++;
							fms_ref = rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_code=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status=rset1.getString(11)==null?"":rset1.getString(11);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							V_FMS_REF.add(fms_ref);
							V_POST_STATUS.add(post_status);
							V_POST_DT.add(post_dt);
							V_POST_TIME.add(post_time);
							V_IDOC_NO.add(idoc_no);
							V_IDOC_STATUS.add(idoc_status);
							V_STATUS_MSG.add(status_msg);
							V_DOC_NO.add(doc_no);
							V_COMPANY_CODE.add(company_code);
							V_FISCAL_YR.add(fiscal_yr);
							V_MSG_STATUS.add(msg_status);
							
							V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, sec_counterparty_cd, sec_agmt_no, sec_agmt_rev_no, sec_cont_no, sec_cont_rev, sec_cont_type, ""));
							V_DUE_DT.add(sec_due_dt);
							V_APPROVE_DT.add(sec_approved_dt);
							V_APPROVED_BY.add(utilBean.getEmpName(conn,sec_approved_by));
							V_APPROVED_FLAG.add(sec_approved_flag);
							V_NET_PAYABLE_AMT.add(sec_amt);
							V_SAP_POSTED.add(description);
							
							V_BU_ABBR.add(bu_plant_abbr);
							V_PLANT_ABBR.add(gx_bu_plant_abbr);
						}
						rset1.close();
						stmt1.close();
					}
					else 
					{
						index++;
						fms_ref=sec_internal_ref;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg = "";
						doc_no="";
						company_code = "";
						fiscal_yr = "";
						msg_status="";
						description="<font style=\"color:blue\">Pending</font>";
						
						V_FMS_REF.add(fms_ref);
						V_POST_STATUS.add(post_status);
						V_POST_DT.add(post_dt);
						V_POST_TIME.add(post_time);
						V_IDOC_NO.add(idoc_no);
						V_IDOC_STATUS.add(idoc_status);
						V_STATUS_MSG.add(status_msg);
						V_DOC_NO.add(doc_no);
						V_COMPANY_CODE.add(company_code);
						V_FISCAL_YR.add(fiscal_yr);
						V_MSG_STATUS.add(msg_status);
						
						V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, sec_counterparty_cd, sec_agmt_no, sec_agmt_rev_no, sec_cont_no, sec_cont_rev, sec_cont_type, ""));
						V_DUE_DT.add(sec_due_dt);
						V_APPROVE_DT.add(sec_approved_dt);
						V_APPROVED_BY.add(utilBean.getEmpName(conn,sec_approved_by));
						V_APPROVED_FLAG.add(sec_approved_flag);
						V_NET_PAYABLE_AMT.add(sec_amt);
						V_SAP_POSTED.add(description);
						V_BU_ABBR.add(bu_plant_abbr);
						V_PLANT_ABBR.add(gx_bu_plant_abbr);
					}
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
			VINDEX.add(index);
			//count++;
			//VSAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_Receivable()
	{
		String function_nm = "getSAP_Receivable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VTITLE.size();i++)
			{
				if(VTITLE.elementAt(i).toString().equals("RLNG (SN | LOA | IGX)") ||
						VTITLE.elementAt(i).toString().equals("DLNG (SN | LOA | IGX | TLU | TMS)") ||
						VTITLE.elementAt(i).toString().equals("LTCORA (Sell)") ||
						VTITLE.elementAt(i).toString().equals("Derivatives (Invoice)")
						)
				{
					if(VTITLE.elementAt(i).equals("RLNG (SN | LOA | IGX)"))
					{
						getSAP_interface_sell_dtl("RLNG"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("DLNG (SN | LOA | IGX | TLU | TMS)"))
					{
						getSAP_interface_sell_dtl("DLNG");
						count++;
					}
					else if(VTITLE.elementAt(i).equals("LTCORA (Sell)"))
					{
						getSAP_interface_sell_dtl("LTCORA (Sell)");
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Derivatives (Invoice)"))
					{
						getSAP_interface_derivatives_dtl("RECV");
						count++;
					}
				}
			}
			
			VSAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSAP_Payable()
	{
		String function_nm = "getSAP_Payable()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VTITLE.size();i++)
			{
				if(!VTITLE.elementAt(i).toString().equals("RLNG (SN | LOA | IGX)") || 
						!VTITLE.elementAt(i).toString().equals("DLNG (SN | LOA | IGX | TLU | TMS)") || 
						!VTITLE.elementAt(i).toString().equals("LTCORA (Sell)") || 
						!VTITLE.elementAt(i).toString().equals("Derivatives (Invoice)") || 
						!VTITLE.elementAt(i).toString().equals("IGX Advance")||
						!VTITLE.elementAt(i).toString().equals("KYC Advance")
						)
				{
					if(VTITLE.elementAt(i).equals("Purchase"))
					{
						getSAP_interface_purchase_dtl("Purchase"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("GX"))
					{
						getSAP_interface_gx_dtl();
						count++;
					}
					else if(VTITLE.elementAt(i).equals("GTA"))
					{
						getSAP_interface_gta_dtl();
						count++;
					}
					else if(VTITLE.elementAt(i).equals("LTCORA (Buy)"))
					{
						getSAP_interface_purchase_dtl("LTCORA (Buy)"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Custom Duty"))
					{
						getSAP_interface_purchase_dtl("Custom Duty"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Surveyor Service"))
					{
						getSAP_interface_purchase_dtl("Surveyor Service"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Vessel Agent Service"))
					{
						getSAP_interface_purchase_dtl("Vessel Agent Service"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Custom House Agent Service"))
					{
						getSAP_interface_purchase_dtl("Custom House Agent Service"); 
						count++;
					}
					else if(VTITLE.elementAt(i).equals("Derivatives (Remittance)"))
					{
						getSAP_interface_derivatives_dtl("PAY");
						count++;
					}
				}
			}
			VSAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_Advance()
	{
		String function_nm = "getSAP_Advance()";
		try
		{
			String query = "";
			int count = 0;
			for(int i=0;i<VTITLE.size();i++)
			{
				if(VTITLE.elementAt(i).toString().equals("KYC Advance") || 
						VTITLE.elementAt(i).toString().equals("IGX Advance")
						)
				{
					if(VTITLE.elementAt(i).equals("IGX Advance"))
					{
						getSAP_interface_adv_dtl("I");	
						count++;
					}
					else if(VTITLE.elementAt(i).equals("KYC Advance"))
					{
						getSAP_interface_adv_dtl("K");
						count++;
					}
				}
			}
			VSAP_INDEX.add(count);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_sell_dtl(String type) 
	{
		String function_nm="getSAP_interface_sell_dtl()";
		try 
		{
			int index = 0;
			int count = 0;
			int selCnt = 0;
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString="";
			if(type.equals("RLNG") || type.equals("LTCORA (Sell)")) 
			{
				queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
						+ "APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),CARGO_NO "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
						+ "AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
						+ "AND FIN_SYS IS NULL ";
				if(type.equals("RLNG"))
				{
					queryString+=" AND CONTRACT_TYPE IN ('S','L','X') ";
				}
				else if(type.equals("LTCORA (Sell)"))
				{
					queryString+="AND CONTRACT_TYPE IN ('O','Q') ";				
				}
				
				if(type.equals("RLNG"))
				{
					queryString +=" UNION ";
					queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
							+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
							+ "APPROVED_BY,APPROVED_FLAG,ADDR_FLAG,CARGO_NO "
							+ "FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
							+ "AND FIN_SYS IS NULL ";
				}
			}
			else if (type.equals("DLNG")) 
			{
				queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
						+ "APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),'' "
						+ "FROM FMS_DLNG_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
						+ "AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
				queryString +=" UNION ";
				queryString += "SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
						+ "APPROVED_BY,APPROVED_FLAG,ADDR_FLAG,'' "
						+ "FROM FMS_DLNG_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
				queryString +=" UNION ";
				queryString +="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN, "
						+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'), "
						+ "APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),'' "
						+ "FROM FMS_DLNG_SVC_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') AND FIN_SYS IS NULL ";
			}
			String temp_query = queryString;
			stmt=conn.prepareStatement(temp_query);
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, "Y");
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, eff_to_dt);
			if(!type.equals("LTCORA (Sell)")) 
			{
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, "Y");
				stmt.setString(++selCnt, from_dt);
				stmt.setString(++selCnt, eff_to_dt);
			}
			if(type.equals("DLNG"))
			{
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, "Y");
				stmt.setString(++selCnt, from_dt);
				stmt.setString(++selCnt, eff_to_dt);
			}
			rset=stmt.executeQuery();
			while(rset.next()) 
			{
				String inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
				String inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
				String inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
				String inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
				String inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
				String inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
				String inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
				String inv_bu_state = rset.getString(9)==null?"":rset.getString(9);
				String inv_invoice_no = rset.getString(10)==null?"":rset.getString(10);
				String inv_invoice_dt = rset.getString(11)==null?"":rset.getString(11);
				String inv_invoice_amt = rset.getString(12)==null?"":rset.getString(12);
				String inv_net_pay_amt = rset.getString(13)==null?"":rset.getString(13);
				String inv_invoice_status = rset.getString(14)==null?"":rset.getString(14);
				String inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
				String inv_approved_dt = rset.getString(16)==null?"":rset.getString(16);
				String inv_approved_by = rset.getString(17)==null?"":rset.getString(17);
				String inv_approved_flag = rset.getString(18)==null?"":rset.getString(18);
				String inv_plant_seq = rset.getString(19)==null?"":rset.getString(19);
				String cargo_instrment_no = rset.getString(12)==null?"":rset.getString(12);
				
				if(inv_plant_seq.equals("R")) 
				{
					inv_plant_seq="";
				}
				else if(inv_plant_seq.startsWith("P"))
				{
					inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
				}
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "C");
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
				
				String cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
				//String cont_map = utilBean.getDisplayDealMapping(inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type);
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg="";
				String doc_no="";
				String company_cd = "";
				String fiscal_yr = "";
				String msg_status = "";
				String description="";
			
				String queryTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt3=conn.prepareStatement(queryTemp);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, inv_invoice_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					int count_dtl = rset3.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						
						while(rset1.next())
						{ 
							index++;					
							fms_ref=rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_cd=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status= rset1.getString(11)==null?"":rset1.getString(11);
							
							
							V_BU_ABBR.add(bu_plant_abbr);
							V_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							V_FMS_REF.add(fms_ref);
							V_POST_STATUS.add(post_status);
							V_POST_DT.add(post_dt);
							V_POST_TIME.add(post_time);
							V_IDOC_NO.add(idoc_no);
							V_IDOC_STATUS.add(idoc_status);
							V_STATUS_MSG.add(status_msg);
							V_DOC_NO.add(doc_no);
							V_COMPANY_CODE.add(company_cd);
							V_FISCAL_YR.add(fiscal_yr);
							V_MSG_STATUS.add(msg_status);
							V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
							V_DUE_DT.add(inv_due_dt);
							V_APPROVE_DT.add(inv_approved_dt);
							V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
							V_APPROVED_FLAG.add(inv_approved_flag);
							V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
							V_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						index++;
						
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg="";
						doc_no="";
						company_cd="";
						fiscal_yr="";
						msg_status= "";
						description="<font style=\"color:blue\">Pending</font>";
						
						V_FMS_REF.add(fms_ref);
						V_POST_STATUS.add(post_status);
						V_POST_DT.add(post_dt);
						V_POST_TIME.add(post_time);
						V_IDOC_NO.add(idoc_no);
						V_IDOC_STATUS.add(idoc_status);
						V_STATUS_MSG.add(status_msg);
						V_DOC_NO.add(doc_no);
						V_COMPANY_CODE.add(company_cd);
						V_FISCAL_YR.add(fiscal_yr);
						V_MSG_STATUS.add(msg_status);
						V_CONT_NO.add(utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, ""));
						V_DUE_DT.add(inv_due_dt);
						V_APPROVE_DT.add(inv_approved_dt);
						V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
						V_APPROVED_FLAG.add(inv_approved_flag);
						V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
						V_SAP_POSTED.add(description);
						V_BU_ABBR.add(bu_plant_abbr);
						V_PLANT_ABBR.add(plant_abbr);
					}
				}
				rset3.close();
				stmt3.close();
				
			}
			rset.close();
			stmt.close();
			VINDEX.add(index);
			//count++;
			//VSAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_interface_derivatives_dtl(String type) 
	{
		String function_nm="getSAP_interface_derivatives_dtl()";
		try 
		{
			int index = 0;
			int count = 0;
			int selCnt1 = 0;
			
			String eff_to_dt =utilDate.getDate(to_dt, "1");
			
			String queryString2="";
			queryString2="SELECT DISTINCT INVOICE_NO "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
					+ "AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_TYPE=? AND FIN_SYS IS NULL ";
			String temp_query2 = queryString2;
			stmt2=conn.prepareStatement(temp_query2);
			stmt2.setString(++selCnt1, comp_cd);
			stmt2.setString(++selCnt1, "Y");
			stmt2.setString(++selCnt1, from_dt);
			stmt2.setString(++selCnt1, eff_to_dt);
			if(type.equals("RECV")) 
			{
				stmt2.setString(++selCnt1, "I");
			}
			else if(type.equals("PAY")) 
			{
				stmt2.setString(++selCnt1, "R");
			}
			rset2=stmt2.executeQuery();
			while(rset2.next()) 
			{
				String mst_inv_no=rset2.getString(1)==null?"":rset2.getString(1);;
				
				int selCnt = 0;
				
				String fms_ref="";
				String post_status="";
				String post_dt="";
				String post_time="";
				String idoc_no="";
				String idoc_status="";
				String status_msg="";
				String doc_no="";
				String company_cd = "";
				String fiscal_yr = "";
				String msg_status = "";
				String description="";
				
				String inv_counterparty_cd = "";
				String inv_fins_year = "";
				String inv_agmt_no =  "";
				String inv_agmt_rev_no =  "";
				String inv_cont_no = "";
				String inv_cont_rev =  "";
				String inv_cont_type =  "";
				String inv_bu_unit =  "";
				String inv_bu_state = "";
				String inv_invoice_no =  "";
				String inv_invoice_dt =  "";
				String inv_invoice_amt = "";
				String inv_net_pay_amt = "";
				double totalNetPay = 0.0;
				
				String inv_invoice_status = "";
				String inv_due_dt = "";
				String inv_approved_dt = "";
				String inv_approved_by = "";
				String inv_approved_flag = "";
				String inv_plant_seq = "";
				String cargo_instrment_no =  "";
				
				String plant_abbr="";
				String bu_plant_abbr="";
				
				String cont_map = "";
				
				String queryString="";
				queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
						+ "INVOICE_NO,INVOICE_DT,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),"
						+ "APPROVED_BY,APPROVED_FLAG,TO_CHAR(PLANT_SEQ),INSTRUMENT_NO "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
						+ "AND SAP_APPROVED_DT >= TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVED_DT < TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_TYPE=? AND INVOICE_NO=? AND FIN_SYS IS NULL ";
				String temp_query = queryString;
				stmt=conn.prepareStatement(temp_query);
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, "Y");
				stmt.setString(++selCnt, from_dt);
				stmt.setString(++selCnt, eff_to_dt);
				if(type.equals("RECV")) 
				{
					stmt.setString(++selCnt, "I");
				}
				else if(type.equals("PAY")) 
				{
					stmt.setString(++selCnt, "R");
				}
				stmt.setString(++selCnt, mst_inv_no);
				rset=stmt.executeQuery();
				while(rset.next()) 
				{
					inv_counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
					inv_fins_year = rset.getString(2)==null?"":rset.getString(2);
					inv_agmt_no = rset.getString(3)==null?"":rset.getString(3);
					inv_agmt_rev_no = rset.getString(4)==null?"":rset.getString(4);
					inv_cont_no = rset.getString(5)==null?"":rset.getString(5);
					inv_cont_rev = rset.getString(6)==null?"":rset.getString(6);
					inv_cont_type = rset.getString(7)==null?"":rset.getString(7);
					inv_bu_unit = rset.getString(8)==null?"":rset.getString(8);
					inv_bu_state = rset.getString(9)==null?"":rset.getString(9);
					inv_invoice_no = rset.getString(10)==null?"":rset.getString(10);
					inv_invoice_dt = rset.getString(11)==null?"":rset.getString(11);
					inv_invoice_amt = rset.getString(12)==null?"":rset.getString(12);
					inv_net_pay_amt = rset.getString(13)==null?"":rset.getString(13);
					inv_invoice_status = rset.getString(14)==null?"":rset.getString(14);
					inv_due_dt = rset.getString(15)==null?"":rset.getString(15);
					inv_approved_dt = rset.getString(16)==null?"":rset.getString(16);
					inv_approved_by = rset.getString(17)==null?"":rset.getString(17);
					inv_approved_flag = rset.getString(18)==null?"":rset.getString(18);
					inv_plant_seq = rset.getString(19)==null?"":rset.getString(19);
					cargo_instrment_no = rset.getString(20)==null?"":rset.getString(20);
					
					if (inv_net_pay_amt != null && !inv_net_pay_amt.isEmpty()) 
					{
						double amount = Double.parseDouble(inv_net_pay_amt);
						totalNetPay += amount;
				    }
					
					if(inv_plant_seq.equals("R")) 
					{
						inv_plant_seq="";
					}
					else if(inv_plant_seq.startsWith("P"))
					{
						inv_plant_seq=inv_plant_seq.substring(1, inv_plant_seq.length());
					}
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,inv_counterparty_cd, comp_cd, inv_plant_seq, "T");
					bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, inv_bu_unit, "B");
					
					if(cont_map.equals(""))
					{
						cont_map = utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
					}
					else
					{
						cont_map += " ,"+utilBean.NewDealMappingId(comp_cd, inv_counterparty_cd, inv_agmt_no, inv_agmt_rev_no, inv_cont_no, inv_cont_rev, inv_cont_type, cargo_instrment_no);
					}
				}
				rset.close();
				stmt.close();
				
				inv_net_pay_amt = String.valueOf(totalNetPay);
				
				String queryTemp = "SELECT COUNT(*) "
						+ "FROM FMS_SAP_ACK_DTL "
						+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
				stmt3=conn.prepareStatement(queryTemp);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, inv_invoice_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					int count_dtl = rset3.getInt(1);
					if(count_dtl>0)
					{
						String queryString1 = "SELECT FMS_REF,POST_STATUS,TO_CHAR(POST_DT,'DD/MM/YYYY'),POST_TIME,IDOC_NO,IDOC_STATUS,STATUS_MSG,"
								+ "DOC_NO,COMPANY_CODE,FISCAL_YR,MSG_STATUS "
								+ "FROM FMS_SAP_ACK_DTL "
								+ "WHERE COMPANY_CD=? AND FMS_REF=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, inv_invoice_no);
						rset1=stmt1.executeQuery();
						
						while(rset1.next())
						{ 
							index++;					
							fms_ref=rset1.getString(1)==null?"":rset1.getString(1);
							post_status=rset1.getString(2)==null?"":rset1.getString(2);
							post_dt=rset1.getString(3)==null?"":rset1.getString(3);
							post_time=rset1.getString(4)==null?"":rset1.getString(4);
							idoc_no=rset1.getString(5)==null?"":rset1.getString(5);
							idoc_status=rset1.getString(6)==null?"":rset1.getString(6);
							status_msg=rset1.getString(7)==null?"":rset1.getString(7);
							doc_no=rset1.getString(8)==null?"":rset1.getString(8);
							company_cd=rset1.getString(9)==null?"":rset1.getString(9);
							fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
							msg_status= rset1.getString(11)==null?"":rset1.getString(11);
							
							
							V_BU_ABBR.add(bu_plant_abbr);
							V_PLANT_ABBR.add(plant_abbr);
							
							if(!idoc_status.isEmpty() || !idoc_status.equals("")) 
							{
								char a = idoc_status.charAt(0);
								if(Character.isDigit(a)) 
								{
									description = utilBean.getSapDesc(conn, idoc_status, "Inbound");
								}
								else 
								{
									description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
								}
							}
							else 
							{
								description="<font style=\"color:red\">Unknown Inbound Status Code</font>";
							}
							V_FMS_REF.add(fms_ref);
							V_POST_STATUS.add(post_status);
							V_POST_DT.add(post_dt);
							V_POST_TIME.add(post_time);
							V_IDOC_NO.add(idoc_no);
							V_IDOC_STATUS.add(idoc_status);
							V_STATUS_MSG.add(status_msg);
							V_DOC_NO.add(doc_no);
							V_COMPANY_CODE.add(company_cd);
							V_FISCAL_YR.add(fiscal_yr);
							V_MSG_STATUS.add(msg_status);
							V_CONT_NO.add(cont_map);
							V_DUE_DT.add(inv_due_dt);
							V_APPROVE_DT.add(inv_approved_dt);
							V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
							V_APPROVED_FLAG.add(inv_approved_flag);
							V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
							V_SAP_POSTED.add(description);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						index++;
						
						fms_ref=inv_invoice_no;
						post_status="";
						post_dt="";
						post_time="";
						idoc_no="";
						idoc_status="";
						status_msg="";
						doc_no="";
						company_cd="";
						fiscal_yr="";
						msg_status= "";
						description="<font style=\"color:blue\">Pending</font>";
						
						V_FMS_REF.add(fms_ref);
						V_POST_STATUS.add(post_status);
						V_POST_DT.add(post_dt);
						V_POST_TIME.add(post_time);
						V_IDOC_NO.add(idoc_no);
						V_IDOC_STATUS.add(idoc_status);
						V_STATUS_MSG.add(status_msg);
						V_DOC_NO.add(doc_no);
						V_COMPANY_CODE.add(company_cd);
						V_FISCAL_YR.add(fiscal_yr);
						V_MSG_STATUS.add(msg_status);
						V_CONT_NO.add(cont_map);
						V_DUE_DT.add(inv_due_dt);
						V_APPROVE_DT.add(inv_approved_dt);
						V_APPROVED_BY.add(utilBean.getEmpName(conn,inv_approved_by));
						V_APPROVED_FLAG.add(inv_approved_flag);
						V_NET_PAYABLE_AMT.add(inv_net_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(inv_net_pay_amt))));
						V_SAP_POSTED.add(description);
						V_BU_ABBR.add(bu_plant_abbr);
						V_PLANT_ABBR.add(plant_abbr);
					}
				}
				rset3.close();
				stmt3.close();
			}
			rset2.close();
			stmt2.close();
			
			VINDEX.add(index);
			//count++;
			//VSAP_INDEX.add(count);
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_Interface()
	{
		String function_nm = "getSAP_Interface()";
		try
		{
			VSAP_INTERFACE_DISPLAY.add("Receivable");
			VSAP_INTERFACE_DISPLAY.add("Payable");
			VSAP_INTERFACE_DISPLAY.add("Advance");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_Interface_Type()
	{
		String function_nm = "getSAP_Interface_Type()";
		try
		{
			VTITLE.add("RLNG (SN | LOA | IGX)");
			VTITLE.add("DLNG (SN | LOA | IGX | TLU | TMS)");
			VTITLE.add("LTCORA (Sell)");
			VTITLE.add("Derivatives (Invoice)");
			
			VTITLE.add("Purchase");
			VTITLE.add("GX");
			VTITLE.add("GTA");
			VTITLE.add("LTCORA (Buy)");
			VTITLE.add("Custom Duty");
			VTITLE.add("Derivatives (Remittance)");
			VTITLE.add("Surveyor Service");
			VTITLE.add("Vessel Agent Service");
			VTITLE.add("Custom House Agent Service");
			VTITLE.add("IGX Advance");
			VTITLE.add("KYC Advance");
			// Ensure that the order should be maintain as per above for data fetching.
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_ACC_Interface()
	{
		String function_nm = "getSAP_ACC_Interface()";
		try
		{
			VACC_SAP_INTERFACE_DISPLAY.add("Receivable");
			VACC_SAP_INTERFACE_DISPLAY.add("Payable");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_ACC_Interface_Type()
	{
		String function_nm = "getSAP_ACC_Interface_Type()";
		try
		{
			VACC_TITLE.add("RLNG (SN | LOA | IGX)");
			VACC_TITLE.add("DLNG (SN | LOA | IGX)");
			VACC_TITLE.add("LTCORA (Sell)");
			VACC_TITLE.add("Derivatives (Invoice)");
			
			VACC_TITLE.add("Purchase");
			VACC_TITLE.add("GX");
			VACC_TITLE.add("GTA");
			VACC_TITLE.add("LTCORA (Buy)");
			VACC_TITLE.add("Derivatives (Remittance)");
			//VACC_TITLE.add("Custom Duty");
			//VACC_TITLE.add("Surveyor Service");
			//VACC_TITLE.add("Vessel Agent Service");
			//VACC_TITLE.add("Custom House Agent Service");
			// Ensure that the order should be maintain as per above for data fetching.
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_MTM_Interface()
	{
		String function_nm = "getSAP_MTM_Interface()";
		try
		{
			VMTM_SAP_INTERFACE_DISPLAY.add("Receivable");
			VMTM_SAP_INTERFACE_DISPLAY.add("Payable");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAP_MTM_Interface_Type()
	{
		String function_nm = "getSAP_MTM_Interface_Type()";
		try
		{
			VMTM_TITLE.add("Derivatives (Invoice)");
			
			VMTM_TITLE.add("Derivatives (Remittance)");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompany_Dtl()
	{
		String function_nm = "getCompany_Dtl()";
		bu_region = "";
		try
		{
			String queryString2 = "SELECT SAP_CODE FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_CD = ?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{ 
				bu_region = rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGlList()
	{
		String function_nm="getGlList()";
		try
		{
			String queryString="SELECT GL_CODE,GL_DECR "
					+ "FROM FMS_SAP_ACCOUNT_GL_MST";
			stmt1=conn.prepareStatement(queryString);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String gl_cd=rset1.getString(1)==null?"":rset1.getString(1);
				String gl_desc=rset1.getString(2)==null?"":rset1.getString(2);
				VMST_GL_CD.add(gl_cd);
				VMST_GL_DESC.add(gl_desc);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapReconDetails()
	{
		String function_nm="getSapReconDetails()";
		try
		{
			getSapCounterpartyList();
			if(entity_role.equals("C"))
			{
				getSapSaleReconDetails();
				getSapDLNGReconDetails();	//for dlng
			}
			if(entity_role.equals("T")||entity_role.equals("V")||entity_role.equals("S")||entity_role.equals("H"))
			{
				getSapPurReconDetails();
				if(entity_role.equals("T"))
				{
					getSapDervReconDetails();	//for derivatives
				}
			}
			if(entity_role.equals("R"))
			{
				getSapTransporterDetails();
			}
			if(entity_role.equals("G"))
			{
				getSapGxReconDetails();
			}
			if(entity_role.equals("0"))
			{
				getSapSaleReconDetails();
				getSapDLNGReconDetails();		//for dlng
				getSapPurReconDetails();
				getSapDervReconDetails();		//for derivatives
				getSapTransporterDetails();
				getSapGxReconDetails();
			}
			fetchSapDumpData();
			amt_sum=nf.format(amtSum);
			qty_sum=nf.format(qtySum);
			amt_usd_sum=nf.format(amtUsdSum);
			sap_sum=nf.format(sapsum);
			delta_sap_sum=nf.format(deltasapsum);
			sap_sum_usd=nf.format(sapSumUsd);
			delta_sap_sum_usd=nf.format(deltaSapSumUsd);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapCounterpartyList()
	{
		String function_nm="getSapCounterpartyList()";
		try
		{
			String sales_customer="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_INVOICE_MST WHERE COMPANY_CD=? "
					+ "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL "
					+ "UNION "
					+ "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD=? "
					+ "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL "
					+ "UNION "
					+ "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_ACCRUAL_DTL WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "	
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "UNION "
					+ "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_ACCRUAL_DTL WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "	
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			
			//pb 20250823 FOR DLNG 
			String dlng_customer = "SELECT DISTINCT A.COUNTERPARTY_CD "
					+ "FROM FMS_DLNG_INVOICE_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND A.FIN_SYS IS NULL ";
			dlng_customer+=" UNION ";
			dlng_customer+="SELECT DISTINCT A.COUNTERPARTY_CD "
					+ "FROM FMS_DLNG_FFLOW_INV_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			dlng_customer+=" UNION ";
			dlng_customer+="SELECT  DISTINCT A.COUNTERPARTY_CD "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			
			String queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_PUR_SG_INV_MST "
					+ "WHERE COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+= "AND CONTRACT_TYPE=? ";
			}
			queryString+= "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_PUR_PG_INV_MST "
					+ "WHERE COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+= "AND CONTRACT_TYPE=? ";
			}
			queryString+= "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_PUR_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+= "AND CONTRACT_TYPE=? ";
			}
			queryString+= "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+= "AND CONTRACT_TYPE=? ";
			}
			queryString+= "AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			
			queryString+="UNION ";
			queryString+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+= "AND CONTRACT_TYPE=? ";
			}
			queryString+= "AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			
			//for derivative
			String derivative="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			derivative+=" UNION ";
			derivative+="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND IS_MTM NOT IN ('Y') AND XML_NUM IS NOT NULL ";
			derivative+=" UNION ";
			derivative+="SELECT DISTINCT COUNTERPARTY_CD "			//for accrual reversal
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND IS_MTM NOT IN ('Y') AND XML_NUM IS NOT NULL ";
			derivative+=" UNION ";
			derivative+="SELECT DISTINCT COUNTERPARTY_CD "	//for MTM Accrual
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND IS_MTM  IN ('Y') AND XML_NUM IS NOT NULL ";
			derivative+=" UNION ";
			derivative+="SELECT DISTINCT COUNTERPARTY_CD "	//for MTM Accrual Reversal
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND IS_MTM  IN ('Y') AND XML_NUM IS NOT NULL ";
			
			
			//transporter
			String transporter="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			transporter+="UNION ";
			transporter+="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			transporter+="UNION ";
			transporter+="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_GTA_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND FIN_SYS IS NULL ";
			transporter+="UNION ";
			transporter+="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			transporter+="UNION ";
			transporter+="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			
			//GX
			String gx_str="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GX_TXN_SG_INV_MST  "
					+ "WHERE COMPANY_CD=?  "
					+ "AND SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			gx_str+="UNION ";
			gx_str+= "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GX_TXN_PG_INV_MST  "
					+ "WHERE COMPANY_CD=?  "
					+ "AND SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			gx_str+="UNION ";
			gx_str+= "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GX_FFLOW_INV_MST  "
					+ "WHERE COMPANY_CD=?  "
					+ "AND SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			gx_str+="UNION ";
			gx_str+= "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GX_ACCRUAL_DTL   "
					+ "WHERE COMPANY_CD=?  "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')";
			gx_str+="UNION ";
			gx_str+= "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_GX_ACCRUAL_DTL   "
					+ "WHERE COMPANY_CD=?  "
					+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')";
			
			if(entity_role.equals("0"))
			{
				int ctn=0;
				String query = sales_customer+" UNION "+dlng_customer+" UNION "+queryString+" UNION "+derivative+" UNION "+transporter+" UNION "+gx_str;
				String temp_query = query;
				stmt = conn.prepareStatement(temp_query);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//dlng
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				//dlng fflow
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				//dlng_svc
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				//purchase
				stmt.setString(++ctn, comp_cd);
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//derivative
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				//accrual
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);		//for accrual reversal
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				stmt.setString(++ctn, comp_cd);		//for MTM accrual
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);		//for MTM accrual reversal
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//transporter
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//GX
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				}
				rset.close();
				stmt.close();
			}
			else if(entity_role.equals("C"))
			{
				int ctn=0;
				String cust_list = sales_customer+" UNION "+dlng_customer;
				String final_cust_list=cust_list;
				stmt = conn.prepareStatement(final_cust_list);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//dlng
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				//dlng fflow
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				//dlng svc
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn,to_dt);
				stmt.setString(++ctn,from_dt);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				}
				rset.close();
				stmt.close();
			}
			else if(entity_role.equals("T") ||entity_role.equals("V")||entity_role.equals("S")||entity_role.equals("H"))
			{
				int ctn=0;
				//FOR TRADER ENTITY TYPE CONTRACT TYPE CAN'T BE A,Y,H 
				String cont_type="";
			    if(entity_role.equals("V"))
		    	{
			    	cont_type="A";
		    	}
		    	else if(entity_role.equals("S"))
		    	{
		    		cont_type="Y";
		    	}
		    	else if(entity_role.equals("H"))
		    	{
		    		cont_type="H";
		    	}
			    
			    //combining the derivative and purchase for trader 
			    String merged_query=queryString;
			    if(entity_role.equals("T"))
			    {
			    	merged_query+=" UNION "+derivative;
			    }
			    String temp_merged_query=merged_query;
			    
				stmt = conn.prepareStatement(temp_merged_query);
				stmt.setString(++ctn, comp_cd);
				if(entity_role.equals("T"))
				{
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "A");
					stmt.setString(++ctn, "H");
				}
				else
				{
					stmt.setString(++ctn, cont_type);
				}
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				if(entity_role.equals("T"))
				{
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "A");
					stmt.setString(++ctn, "H");
				}
				else
				{
					stmt.setString(++ctn, cont_type);
				}
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				if(entity_role.equals("T"))
				{
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "A");
					stmt.setString(++ctn, "H");
				}
				else
				{
					stmt.setString(++ctn, cont_type);
				}
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				if(entity_role.equals("T"))
				{
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "A");
					stmt.setString(++ctn, "H");
				}
				else
				{
					stmt.setString(++ctn, cont_type);
				}
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				if(entity_role.equals("T"))
				{
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, "A");
					stmt.setString(++ctn, "H");
				}
				else
				{
					stmt.setString(++ctn, cont_type);
				}
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				//for derivative
				if(entity_role.equals("T"))
			    {
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, "Y");
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, comp_cd);
					stmt.setString(++ctn, lastDtOfPrevMonth);
					stmt.setString(++ctn, firstDtOfPrevMonth);
					stmt.setString(++ctn, comp_cd);		//for MTM accrual
					stmt.setString(++ctn, to_dt);
					stmt.setString(++ctn, from_dt);
					stmt.setString(++ctn, comp_cd);		//for MTM Accrual Reversal
					stmt.setString(++ctn, lastDtOfPrevMonth);
					stmt.setString(++ctn, firstDtOfPrevMonth);
			    }
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				}
				rset.close();
				stmt.close();
			}
			else if(entity_role.equals("R"))
			{
				int ctn=0;
				stmt = conn.prepareStatement(transporter);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				}
				rset.close();
				stmt.close();
			}
			else if(entity_role.equals("G"))
			{
				int ctn=0;
				stmt = conn.prepareStatement(gx_str);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
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
	
	public void getSapSaleReconDetails()
	{
		String function_nm="getSapSaleReconDetails()";
		try
		{
			String counterpty_cd="";
			String invoice_no="";
			String fin_year="";
			String invoice_seq="";
			String bu_state_tin="";
			String gross_amt="";
			String transportation_amount="";
			String marketing_margin_amount="";
			String other_charges_amount="";
			String inv_raised_in="";
			String exchangeRate="";
			String type_flag="";
			String invoice_type="";
			String sub_inv_type="";
			String period_start_dt="";
			String period_end_dt="";
			String tcs_tds="";
			String tcsStructCd="";
			String tdsStructCd="";
			String tcs_amt="";
			String tds_amt="";
			String tax_amt="";
			String agmt_no="";
			String agmt_rev_no="";
			String cont_no="";
			String cont_rev_no="";
			String cont_type="";
			String accountingPeriodYear="";
			String accountingPeriodMonth="";
			String netPayable="";
			String taxStructCd="";
			String documentNo="";
			String sap_approved_by="";
			String documentDate="";
			String postingDate="";
			String alloc_qty="";
			String amt="";
			String approve_dt="";
			String inv_category="";
			String cash_flow="";
			String inv_dt="";
			String prod_month="";
			String monthNm="";
			String productionPeriodMonth="";
			String productionPeriodYear="";
			String cargo_no="";
			String extn_mapping="";
			String inv_flag="";
			String criteri_formula="";
			String imb_amt="";
			String imb_qty="";
			String ship_or_pay_amt="";
			String ship_or_pay_qty="";
			String ovrun_amt="";
			String ovrun_qty="";
			
			int ctn=0;
			String queryString="SELECT A.COUNTERPARTY_CD,A.INVOICE_NO,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
					+ "A.BU_STATE_TIN,A.INVOICE_SEQ,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,NULL,A.NET_PAYABLE_AMT,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,A.TRANSPORTATION_AMOUNT,A.MARKET_MARGIN_AMT,A.OTHER_CHARGES_AMT, "
					+ "'INV',A.TCS_TDS,A.TAX_STRUCT_CD,A.TDS_STRUCT_CD,A.TCS_STRUCT_CD,A.SAP_APPROVED_BY,A.ALLOC_QTY,A.TCS_AMT,A.TDS_GROSS_AMT,A.TAX_AMT,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),NULL, "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),NULL,NULL,NULL,A.CARGO_NO,A.INV_FLAG, "
					+ "A.CRITERIA,A.IMB_AMT,A.IMB_QTY,A.SHIPAY_AMT,A.SHIPAY_QTY,A.OVRUN_AMT,A.OVRUN_QTY "
					+ "FROM FMS_INVOICE_MST A WHERE A.COMPANY_CD=? "
					+ "AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.INVOICE_NO,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
					+ "A.BU_STATE_TIN,A.INVOICE_SEQ,A.FINANCIAL_YEAR,A.INVOICE_TYPE,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_INR,A.SUB_INV_TYPE,A.NET_PAYABLE_AMT,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,NULL,NULL,NULL,'FFLOW',A.TCS_TDS,A.TAX_STRUCT_CD, "
					+ "A.TDS_STRUCT_CD,A.TCS_STRUCT_CD,A.SAP_APPROVED_BY,A.ALLOC_QTY,A.TCS_AMT,A.TDS_GROSS_AMT,A.TAX_AMT,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.INVOICE_CATEGORY, "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),NULL,NULL,A.GROSS_AMT_USD,A.CARGO_NO,NULL,"
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL  "
					+ "FROM FMS_FFLOW_INV_MST A WHERE A.COMPANY_CD=? "
					+ "AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//For Accural
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.XML_NUM,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_STATE_TIN,A.XML_SEQ,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,NULL,NULL,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,NULL,NULL,NULL,'ACCRUAL',NULL,NULL,NULL,NULL,NULL,A.ACCRUAL_QTY,NULL, "
					+ "NULL,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),NULL,TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),NULL,A.CARGO_NO,NULL, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL  "
					+ "FROM FMS_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//For Accrual Reversal
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.XML_NUM,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_STATE_TIN,A.XML_SEQ,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,NULL,NULL,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,NULL,NULL,NULL,'ACCRUAL_REVERSAL',NULL,NULL,NULL,NULL,NULL,A.ACCRUAL_QTY,NULL, "
					+ "NULL,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),NULL,TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),NULL,A.CARGO_NO,NULL, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL  "
					+ "FROM FMS_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			//FOR REVERSAL
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, lastDtOfPrevMonth);
			stmt.setString(++ctn, firstDtOfPrevMonth);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				extn_mapping="";
				amt="";
				
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				invoice_no=rset.getString(2)==null?"":rset.getString(2);
				agmt_no=rset.getString(3)==null?"":rset.getString(3);
				agmt_rev_no=rset.getString(4)==null?"":rset.getString(4);
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				cont_rev_no=rset.getString(6)==null?"":rset.getString(6);
				cont_type=rset.getString(7)==null?"":rset.getString(7);
				bu_state_tin=rset.getString(8)==null?"":rset.getString(8);
				invoice_seq=rset.getString(9)==null?"":rset.getString(9);
				fin_year=rset.getString(10)==null?"":rset.getString(10);
				invoice_type=rset.getString(11)==null?"":rset.getString(11);
				period_start_dt=rset.getString(12)==null?"":rset.getString(12);
				period_end_dt=rset.getString(13)==null?"":rset.getString(13);
				gross_amt=rset.getString(14)==null?"":rset.getString(14);
				sub_inv_type=rset.getString(15)==null?"":rset.getString(15);
				netPayable=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				inv_raised_in=rset.getString(17)==null?"":rset.getString(17);
				exchangeRate=rset.getString(18)==null?"":rset.getString(18);
				transportation_amount=rset.getString(19)==null?"":rset.getString(19);
				marketing_margin_amount=rset.getString(20)==null?"":rset.getString(20);
				other_charges_amount=rset.getString(21)==null?"":rset.getString(21);
				type_flag=rset.getString(22)==null?"":rset.getString(22);
				tcs_tds=rset.getString(23)==null?"":rset.getString(23);
				taxStructCd=rset.getString(24)==null?"":rset.getString(24);
				tdsStructCd=rset.getString(25)==null?"":rset.getString(25);
				tcsStructCd=rset.getString(26)==null?"":rset.getString(26);
				sap_approved_by=rset.getString(27)==null?"":rset.getString(27);
				alloc_qty = rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				tcs_amt = rset.getString(29)==null?"":nf.format(rset.getDouble(29));
				tds_amt = rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				tax_amt = rset.getString(31)==null?"":nf.format(rset.getDouble(31));
				approve_dt = rset.getString(32)==null?"":rset.getString(32);
				inv_category = rset.getString(33)==null?"":rset.getString(33);
				inv_dt = rset.getString(34)==null?"":rset.getString(34);
				prod_month = rset.getString(36)==null?"":rset.getString(36);
				cargo_no = rset.getString(38)==null?"":rset.getString(38);
				inv_flag = rset.getString(39)==null?"":rset.getString(39);
				criteri_formula=rset.getString(40)==null?"":rset.getString(40);
				imb_amt=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
				imb_qty=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
				ship_or_pay_amt=rset.getString(43)==null?"":nf.format(rset.getDouble(43));
				ship_or_pay_qty=rset.getString(44)==null?"":nf.format(rset.getDouble(44));
				ovrun_amt=rset.getString(45)==null?"":nf.format(rset.getDouble(45));
				ovrun_qty=rset.getString(46)==null?"":nf.format(rset.getDouble(46));
				
				if(!period_end_dt.equals(""))
				{
					String[] temp_split=period_end_dt.split("/");
					productionPeriodMonth=temp_split[1];
					productionPeriodYear=temp_split[2];		
				}
				monthNm=""+utilDate.getShortMonthName(period_end_dt);
				
				String co_cd = utilBean.getCompanySAPcode(conn, comp_cd);
				
				String [] app_dt_split = approve_dt.split("/");
				String app_dt = app_dt_split[2]+app_dt_split[1]+app_dt_split[0];
				accountingPeriodMonth=app_dt_split[1];
				accountingPeriodYear=app_dt_split[2];
				
				String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
				
				String disp_deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, cargo_no);
				
				//documentNo=invoice_no.replaceAll("/", "-");
				documentNo=getSapDocNo(invoice_no);
				extn_mapping=invoice_no+"@";
				
				if(type_flag.equals("ACCRUAL") ||type_flag.equals("ACCRUAL_REVERSAL"))
				{
					documentDate = app_dt_split[2]+app_dt_split[1]+app_dt_split[0];
				}
				else
				{
					String [] temp_inv_dt = inv_dt.split("/");
					documentDate= temp_inv_dt[2]+temp_inv_dt[1]+temp_inv_dt[0];
				}
				
				String sap_val="";
				String delta_sap_val="";
				
				app_dt=type_flag.equals("ACCRUAL_REVERSAL")?to_dt.split("/")[2]+to_dt.split("/")[1]+"20":app_dt;		//Accural reversal posting date should be 20th of Accounting period 
				
				if(inv_flag.equals("CR") || inv_flag.equals("DR"))
				{
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						netPayable=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchangeRate));
						gross_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14) * Double.parseDouble(exchangeRate));
						tcs_amt=rset.getString(29)==null?"":nf.format(rset.getDouble(29) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(31)==null?"":nf.format(rset.getDouble(31) * Double.parseDouble(exchangeRate));
					}
					
					cash_flow="Commodity";
					/*if(contract_type.equals("O") || contract_type.equals("Q")) {
						cash_flow="Re-Gas Capacity";
					}*/
					
					if(inv_flag.equals("CR"))
					{
						cash_flow="Credit Note";
					}
					else if(inv_flag.equals("DR"))
					{
						cash_flow="Debit Note";
					}
					
					if(criteri_formula.contains("IMB") || criteri_formula.contains("SHIP") || criteri_formula.contains("UNAUTH"))
					{
						if(!imb_amt.equals(""))
					    {
							String sign = "-";
					    	String pk = "50";
					    	if(Double.parseDouble(imb_amt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		imb_amt=nf.format(Math.abs(Double.parseDouble(imb_amt)));
					    	}
					    	
					    	String tempAccount="";
					    	String itemText="";
					    		
					    	tempAccount="6318400";
					    	itemText="Imbalance Charges";
					    	
					    	VCOUNTERPARTY_CD.add(counterpty_cd);
							VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
							VCO_CD.add(co_cd);
							VDEAL_MAP.add(disp_deal_map);
							VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
							VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
							VPERIOD_START_DT.add(period_start_dt);
							VPERIOD_END_DT.add(period_end_dt);
							VINVOICE_NO.add(invoice_no);
							VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
							VGL_DESC.add(""+utilBean.getGLdesc(conn, tempAccount));
							VCURRENCY.add("INR");
							VDOC_TYPE.add("X2");
							VDOC_NO.add(documentNo);
							VPK.add(pk);
							VTRANS_TYPE.add("Sales Commodity");
							VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
							VDOC_DT.add(documentDate);
							VPOST_DT.add(app_dt);
							VALLOC_QTY.add(imb_qty);
							qtySum+=Double.parseDouble(imb_qty.equals("")?"0.00":imb_qty);
							VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
							VAMT.add(sign+""+imb_amt);
							VUSD_AMT.add("");
							sap_val=getSAP_Doc_Value(co_cd,tempAccount,app_dt,invoice_no,"1");
							VSAP_VALUE_INR.add(sap_val);
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(imb_amt.equals("")?"0":sign+""+imb_amt) - Double.parseDouble(sap_val));
							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							VSAP_VALUE_USD.add("");
							VDELTA_SAP_VALUE_USD.add("");
							VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
							VIS_SAP_DOC_DATA.add("");
							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							amtSum+=Double.parseDouble(imb_qty.equals("")?"0":sign+""+imb_amt);
							extn_mapping+=","+tempAccount;
					    }
						
						if(!ovrun_amt.equals(""))
					    {
							String sign = "-";
					    	String pk = "50";
					    	if(Double.parseDouble(ovrun_amt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		ovrun_amt=nf.format(Math.abs(Double.parseDouble(ovrun_amt)));
					    	}
					    	String tempAccount="";
					    	String itemText="";
					    		
					    	tempAccount="6318400";
					    	itemText="Unauthorized Overrun Charges";
					    	
					    	VCOUNTERPARTY_CD.add(counterpty_cd);
							VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
							VCO_CD.add(co_cd);
							VDEAL_MAP.add(disp_deal_map);
							VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
							VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
							VPERIOD_START_DT.add(period_start_dt);
							VPERIOD_END_DT.add(period_end_dt);
							VINVOICE_NO.add(invoice_no);
							VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
							VGL_DESC.add(""+utilBean.getGLdesc(conn, tempAccount));
							VCURRENCY.add("INR");
							VDOC_TYPE.add("X2");
							VDOC_NO.add(documentNo);
							VPK.add(pk);
							VTRANS_TYPE.add("Sales Commodity");
							VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
							VDOC_DT.add(documentDate);
							VPOST_DT.add(app_dt);
							VALLOC_QTY.add(ovrun_qty);
							qtySum+=Double.parseDouble(ovrun_qty.equals("")?"0.00":ovrun_qty);
							VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
							VAMT.add(sign+""+ovrun_amt);
							VUSD_AMT.add("");
							sap_val=getSAP_Doc_Value(co_cd,tempAccount,app_dt,invoice_no,"1");
							VSAP_VALUE_INR.add(sap_val);
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(ovrun_amt.equals("")?"0":sign+""+ovrun_amt) - Double.parseDouble(sap_val));
							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							VSAP_VALUE_USD.add("");
							VDELTA_SAP_VALUE_USD.add("");
							VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
							VIS_SAP_DOC_DATA.add("");
							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							amtSum+=Double.parseDouble(ovrun_amt.equals("")?"0":sign+""+ovrun_amt);
							extn_mapping+=","+tempAccount;
					    }
						
						if(!ship_or_pay_amt.equals(""))
					    {
							String sign = "-";
					    	String pk = "50";
					    	if(Double.parseDouble(ship_or_pay_amt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		ship_or_pay_amt=nf.format(Math.abs(Double.parseDouble(ship_or_pay_amt)));
					    	}
					    	
					    	String tempAccount="";
					    	String itemText="";
					    	if(countpty_category.equals("Group"))
					    	{
					    		tempAccount="6001400";
					    	}
					    	else
					    	{
					    		tempAccount="6000400";
					    	}
					    	itemText="Ship or Pay Charges";
					    	
					    	VCOUNTERPARTY_CD.add(counterpty_cd);
							VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
							VCO_CD.add(co_cd);
							VDEAL_MAP.add(disp_deal_map);
							VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
							VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
							VPERIOD_START_DT.add(period_start_dt);
							VPERIOD_END_DT.add(period_end_dt);
							VINVOICE_NO.add(invoice_no);
							VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
							VGL_DESC.add(""+utilBean.getGLdesc(conn, tempAccount));
							VCURRENCY.add("INR");
							VDOC_TYPE.add("X2");
							VDOC_NO.add(documentNo);
							VPK.add(pk);
							VTRANS_TYPE.add("Sales Commodity");
							VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
							VDOC_DT.add(documentDate);
							VPOST_DT.add(app_dt);
							VALLOC_QTY.add(ship_or_pay_qty);
							qtySum+=Double.parseDouble(ship_or_pay_qty.equals("")?"0.00":ship_or_pay_qty);
							VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
							VAMT.add(sign+""+ship_or_pay_amt);
							VUSD_AMT.add("");
							sap_val=getSAP_Doc_Value(co_cd,tempAccount,app_dt,invoice_no,"1");
							VSAP_VALUE_INR.add(sap_val);
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(ship_or_pay_amt.equals("")?"0":sign+""+ship_or_pay_amt) - Double.parseDouble(sap_val));
							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							VSAP_VALUE_USD.add("");
							VDELTA_SAP_VALUE_USD.add("");
							VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
							VIS_SAP_DOC_DATA.add("");
							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							amtSum+=Double.parseDouble(ship_or_pay_amt.equals("")?"0":sign+""+ship_or_pay_amt);
							extn_mapping+=","+tempAccount;
					    }
					}
					else
					{
						 if(!inv_flag.equals("UG") && !invoice_type.equals("UG") && !sub_inv_type.equals("UG"))
						 {	
							 if(!gross_amt.equals(""))
							 {
								 String sign = "-";
								 String pk = "50";
								 if(Double.parseDouble(gross_amt) < 0)
								 {
						    		pk = "40";
						    		sign = "";
						    		gross_amt=nf.format(Math.abs(Double.parseDouble(gross_amt)));
								 }
								 String tempAccount="";
								 String itemText="";
						    		
								 if(sub_inv_type.equals("IMB"))
								 {
						    		tempAccount="6318400";
						    		itemText="Imbalance Charges";
								 }	
								 else if(sub_inv_type.equals("ST")) //FOR STORAGE
								 {
						    		//tempAccount="6320405";
						    		
						    		//AS PER VIJAY'S MAIL ON 25/06/2025
						    		if(countpty_category.equals("Group"))
							    	{
							    		tempAccount="6851550";
							    	}
							    	else
							    	{
							    		tempAccount="6850550";
							    	}
							    	itemText="LNG Storage Charge";
								 }
								 else if(sub_inv_type.equals("DI"))
								 {
						    		/* commented due to sonarqube findings
						    		if(countpty_category.equals("Group"))
							    	{
							    		//tempAccount="6850310"; 20250730
						    			tempAccount="6250023"; //AS PER VIJAY'S MSG ON TEAMS 20250730
							    	}
							    	else*/
							    	{
							    		//tempAccount="6850320"; 20250730
							    		tempAccount="6250023"; //AS PER VIJAY'S MSG ON TEAMS 20250730
							    	}
						    		
						    		if(inv_flag.equals("CR"))
						    		{
						    			itemText="Deficiency Reversal";
						    		}
						    		else 
						    		{
						    			itemText="Deficiency";
						    		}
								 }
								 else if(cont_type.equals("O") || cont_type.equals("Q")) // invoice_type SI will covered here
								 { 
						    		if(countpty_category.equals("Group"))
							    	{
							    		tempAccount="6000450";
							    	}
							    	else
							    	{
							    		tempAccount="6000500";
							    	}
						    		itemText=cash_flow;
								 }
								 else if(inv_flag.equals("CR"))
								 {
						    		if(countpty_category.equals("Group"))
							    	{
							    		tempAccount="6001400";
							    	}
							    	else
							    	{
							    		tempAccount="6000400";
							    	}
						    		//itemText="Commodity";
						    		itemText=cash_flow;
								 }	
								 else 
								 {
						    		if(countpty_category.equals("Group"))
							    	{
							    		tempAccount="6001400";
							    		itemText=cash_flow;
							    	}
							    	else
							    	{
							    		tempAccount="6000400";
							    		itemText=cash_flow;
							    	}
								 }
								 VCOUNTERPARTY_CD.add(counterpty_cd);
								 VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
								 VCO_CD.add(co_cd);
								 VDEAL_MAP.add(disp_deal_map);
								 VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
								 VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
								 VPERIOD_START_DT.add(period_start_dt);
								 VPERIOD_END_DT.add(period_end_dt);
								 VINVOICE_NO.add(invoice_no);
								 VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
								 VGL_DESC.add(""+utilBean.getGLdesc(conn, tempAccount));
								 VCURRENCY.add("INR");
								 VDOC_TYPE.add("X2");
								 VDOC_NO.add(documentNo);
								 VPK.add(pk);
								 VTRANS_TYPE.add("Sales Commodity");
								 VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
								 VDOC_DT.add(documentDate);
								 VPOST_DT.add(app_dt);
								 VALLOC_QTY.add(alloc_qty);
								 qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
								 VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
								 VAMT.add(sign+""+gross_amt);
								 VUSD_AMT.add("");
								 sap_val=getSAP_Doc_Value(co_cd,tempAccount,app_dt,invoice_no,"1");
								 VSAP_VALUE_INR.add(sap_val);
								 delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
								 VDELTA_SAP_VALUE_INR.add(delta_sap_val);
								 VSAP_VALUE_USD.add("");
								 VDELTA_SAP_VALUE_USD.add("");
								 VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
								 VIS_SAP_DOC_DATA.add("");
								 sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
								 deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
								 amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
								 extn_mapping+=","+tempAccount;
							 }
						 }
					}
					int stcount=0;
			    	String queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
			    	stmt1 = conn.prepareStatement(queryString1);
			    	stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, fin_year);
					stmt1.setString(++stcount, invoice_seq);
					stmt1.setString(++stcount, bu_state_tin);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
						String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
						String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
						String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
						
						if(!taxAmt.equals(""))
						{
							String sign = "-";
					    	String pk = "50";
					    	if(Double.parseDouble(taxAmt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		taxAmt=nf.format(Math.abs(Double.parseDouble(taxAmt)));
					    	}
					    	
					    	String gl_code="";
					    	String tax_sap_code="";
					    	amt=taxAmt;
					    	gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
					    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
					    	
					    	if(Double.parseDouble(amt) > 0)
					    	{
					    		extn_mapping+=","+gl_code;
					    		if(tax_flag.equals("Y"))
								{
					    			VCOUNTERPARTY_CD.add(counterpty_cd);
					    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
					    			VCO_CD.add(co_cd);
					    			VDEAL_MAP.add(disp_deal_map);
					    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
					    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
					    			VPERIOD_START_DT.add(period_start_dt);
					    			VPERIOD_END_DT.add(period_end_dt);
					    			VINVOICE_NO.add(invoice_no);
					    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
					    			VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
					    			VCURRENCY.add("INR");
					    			VDOC_TYPE.add("X2");
					    			VDOC_NO.add(documentNo);
					    			VPK.add(pk);
					    			VTRANS_TYPE.add("Sales Commodity");
					    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
					    			VDOC_DT.add(documentDate);
					    			VPOST_DT.add(app_dt);
					    			VALLOC_QTY.add("");
					    			VITEMTEXT.add("T ["+tax_sap_code+"]");
					    			VAMT.add(sign+""+amt);
					    			VUSD_AMT.add("");
					    			sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
					    			VSAP_VALUE_INR.add(sap_val);
					    			delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
					    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
					    			VSAP_VALUE_USD.add("");
					    			VDELTA_SAP_VALUE_USD.add("");
					    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
					    			VIS_SAP_DOC_DATA.add("");
					    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
								}
					    	}	
						}
					}
					rset1.close();
					stmt1.close();
					
					if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
				    {
						String gl_code="";
				    	String TcsTdscode="";
				    	amt="";
				    	String pk="";
				    	String taxBase="";
				    	String sign="";
				    	if(tcs_tds.equals("TCS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
				    		TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
				    		amt=tcs_amt;
				    		
				    		sign = "-";
					    	pk = "50";
					    	if(Double.parseDouble(tcs_amt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		tcs_amt=nf.format(Math.abs(Double.parseDouble(tcs_amt)));
					    	}
					    	extn_mapping+=","+gl_code;
					    	
					    	if(tcs_flag.equals("Y"))
							{
					    		VCOUNTERPARTY_CD.add(counterpty_cd);
					    		VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
					    		VCO_CD.add(co_cd);
					    		VDEAL_MAP.add(disp_deal_map);
					    		VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
					    		VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
					    		VPERIOD_START_DT.add(period_start_dt);
					    		VPERIOD_END_DT.add(period_end_dt);
					    		VINVOICE_NO.add(invoice_no);
					    		VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
					    		VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
					    		VCURRENCY.add("INR");
					    		VDOC_TYPE.add("X2");
					    		VDOC_NO.add(documentNo);
					    		VPK.add(pk);
					    		VTRANS_TYPE.add("Sales Commodity");
					    		VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
					    		VDOC_DT.add(documentDate);
					    		VPOST_DT.add(app_dt);
					    		VALLOC_QTY.add("");
					    		VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
					    		VAMT.add(sign+""+amt);
					    		VUSD_AMT.add("");
					    		sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
					    		VSAP_VALUE_INR.add(sap_val);
					    		delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
					    		VDELTA_SAP_VALUE_INR.add(delta_sap_val);
					    		VSAP_VALUE_USD.add("");
					    		VDELTA_SAP_VALUE_USD.add("");
					    		VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
					    		VIS_SAP_DOC_DATA.add("");
					    		sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					    		deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					    		amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
							}
				    	}
				    	else if(tcs_tds.equals("TDS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
				    		TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
				    		amt=tds_amt;
				    		//taxBase=gross_amt; //THIS WILL BE INCORRECT WHEN TRANSACTION ABOUT TO CROSS 50L
				    		
				    		pk="40";
				    		//sign = "";
				    		if(inv_flag.equals("CR"))
					    	{
					    		pk = "50";
					    		//sign = "-";
					    	}
				    		extn_mapping+=","+gl_code;
				    		
				    		if(tds_flag.equals("Y"))
							{
				    			VCOUNTERPARTY_CD.add(counterpty_cd);
				    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    			VCO_CD.add(co_cd);
				    			VDEAL_MAP.add(disp_deal_map);
				    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				    			VPERIOD_START_DT.add(period_start_dt);
				    			VPERIOD_END_DT.add(period_end_dt);
				    			VINVOICE_NO.add(invoice_no);
				    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
				    			VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
				    			VCURRENCY.add("INR");
				    			VDOC_TYPE.add("X2");
				    			VDOC_NO.add(documentNo);
				    			VPK.add(pk);
				    			VTRANS_TYPE.add("Sales Commodity");
				    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
				    			VDOC_DT.add(documentDate);
				    			VPOST_DT.add(app_dt);
				    			VALLOC_QTY.add("");
				    			VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
				    			VAMT.add(sign+""+amt);
				    			VUSD_AMT.add("");
				    			sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
				    			VSAP_VALUE_INR.add(sap_val);
				    			delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
				    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    			VSAP_VALUE_USD.add("");
				    			VDELTA_SAP_VALUE_USD.add("");
				    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				    			VIS_SAP_DOC_DATA.add("");
				    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
				    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
				    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
							}
				    	}
				    }
				}
				else
				{
					if(type_flag.equals("FFLOW"))
					{
						if(inv_category.equals("P"))
						{
							cash_flow="Commodity";
						}
						else if(inv_category.equals("S"))
						{
							cash_flow="Service";
						}
					}
					else if(type_flag.equals("INV"))
					{
						cash_flow="Commodity";
					}
					else if(type_flag.equals("ACCRUAL") || type_flag.equals("ACCRUAL_REVERSAL"))
					{
						cash_flow=rset.getString(35)==null?"":rset.getString(35);
					}
					
					if(!transportation_amount.equals("") && !gross_amt.equals(""))
					{
						gross_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(transportation_amount));
					}
					
					if(!marketing_margin_amount.equals("") && !gross_amt.equals(""))
					{
						gross_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(marketing_margin_amount));
					}
					
					if(!other_charges_amount.equals("") && !gross_amt.equals(""))
					{
						gross_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(other_charges_amount));
					}
					
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						netPayable=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchangeRate));
						
						if(type_flag.equals("FFLOW"))
						{
							gross_amt=rset.getString(37)==null?"":nf.format(rset.getDouble(37) * Double.parseDouble(exchangeRate));
						}
						else
						{
							gross_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14) * Double.parseDouble(exchangeRate));
						}
						tcs_amt=rset.getString(29)==null?"":nf.format(rset.getDouble(29) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(31)==null?"":nf.format(rset.getDouble(31) * Double.parseDouble(exchangeRate));
					}
					
					String pk="";
					
					String sign = "";
					/*// For Removing first Line of invoice 
					 if(!netPayable.equals(""))
					{
						pk = "01";
						if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
						{
							pk = "11";
							sign = "-";
						}
					}
					
					VCO_CD.add(co_cd);
					VDEAL_MAP.add(disp_deal_map);
					VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
					VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_NO.add(invoice_no);
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
					VCURRENCY.add("INR");
					VDOC_TYPE.add("X2");		//Need to Discuss for Accural
					VDOC_NO.add(documentNo);
					VITEMTEXT.add(invoice_no);
					VGL_CD.add("");
					VGL_DESC.add("");
					VPK.add(pk);
					VTRANS_TYPE.add("Sales Commodity");
					VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by));
					VDOC_DT.add(documentDate);
					VPOST_DT.add(app_dt);
					VALLOC_QTY.add(alloc_qty);
					VAMT.add(sign+""+netPayable);*/
					
					String tempAccount="";
					String itemText="";
					
					if(!inv_flag.equals("UG") && !invoice_type.equals("UG") && !sub_inv_type.equals("UG"))
					{
						if(!gross_amt.equals(""))
						{
							sign = "-";
							pk = "50";
							if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
							{
								pk = "40";
								sign = "";
							}
							if(invoice_type.equals("LP") || sub_inv_type.equals("LP") || inv_flag.equals("LP"))
							{
								tempAccount="8240050";
								itemText="Interest";
							}
							else 
							{
								if(sub_inv_type.equals("IMB"))
								{
									tempAccount="6318400";
									itemText="Imbalance Charges";
								}
								else if(inv_flag.equals("ST") || invoice_type.equals("ST") || sub_inv_type.equals("ST")) //FOR STORAGE
								{
									//tempAccount="6320405";
								
									//AS PER VIJAY'S MAIL ON 25/06/2025
									if(countpty_category.equals("Group"))
									{
										tempAccount="6851550";
									}
									else
									{
										tempAccount="6850550";
									}
									itemText="LNG Storage Charge";
								}
								else if(invoice_type.equals("DI") || sub_inv_type.equals("DI"))
								{
									/* commented due to sonarqube findings
						    		if(countpty_category.equals("Group"))
							   		{
							   			//tempAccount="6850310"; 20250730
						    			tempAccount="6250023"; //AS PER VIJAY'S MSG ON TEAMS 20250730
							   		}
							   		else*/
									{
										//tempAccount="6850320"; 20250730
										tempAccount="6250023"; //AS PER VIJAY'S MSG ON TEAMS 20250730
									}
							
									if(invoice_type.equals("CR"))
									{
										itemText="Deficiency Reversal";
									}
									else 
									{
										itemText="Deficiency";
									}
								}
								else if(cont_type.equals("O") || cont_type.equals("Q")) // invoice_type SI will covered here
								{	 
									if(countpty_category.equals("Group"))
									{
										tempAccount="6000450";
									}
									else
									{
										tempAccount="6000500";
									}
									itemText=cash_flow;
								}
								else if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
								{
									//tempAccount="7220300";
									//tempAccount="6000400";
									//itemText="Brokerage/Commission";
									if(countpty_category.equals("Group"))
									{
										tempAccount="6001400";
									}
									else
									{
										tempAccount="6000400";
									}
									itemText="Commodity";
								}
								else 
								{
									if(countpty_category.equals("Group"))
									{
										tempAccount="6001400";
										itemText=cash_flow;
									}
									else
									{
										tempAccount="6000400";
										itemText=cash_flow;
									}
								}
							}
							
							if(Double.doubleToRawLongBits(Double.parseDouble(gross_amt))==Double.doubleToRawLongBits(0))
							{
								sign="";
							}
						}
						
						if(type_flag.equals("ACCRUAL") || type_flag.equals("ACCRUAL_REVERSAL"))
						{
							String [] temp_split = prod_month.split("/");
							monthNm=utilDate.getShortMonthName(prod_month);
							//itemText="Commodity "+temp_split[1] +" "+temp_split[2];
							itemText="Commodity "+monthNm+" "+temp_split[2];
						}
						
						if(type_flag.equals("ACCRUAL_REVERSAL"))
						{
							sign=sign.equals("")?"-":"";
							pk=pk.equals("50")?"40":"50";
						}
						
						VCO_CD.add(co_cd);
						VDEAL_MAP.add(disp_deal_map);
						VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
						VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
						VPERIOD_START_DT.add(period_start_dt);
						VPERIOD_END_DT.add(period_end_dt);
						VINVOICE_NO.add(invoice_no);
						VCOUNTERPARTY_CD.add(counterpty_cd);
						VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
						VCURRENCY.add("INR");
						if(type_flag.equals("ACCRUAL") || type_flag.equals("ACCRUAL_REVERSAL"))
						{
							VDOC_TYPE.add("X4");		
						}
						else
						{
							VDOC_TYPE.add("X2");		
						}
						VDOC_NO.add(documentNo);
						VAMT.add(sign+""+gross_amt);
						VUSD_AMT.add("");
						amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
						VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
						VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
						VPK.add(pk);
						if(type_flag.equals("ACCRUAL_REVERSAL"))
						{
							VTRANS_TYPE.add("Sales Accrual Reversal");
						}
						else if(type_flag.equals("ACCRUAL"))
						{
							VTRANS_TYPE.add("Sales Accrual");
						}
						else
						{
							VTRANS_TYPE.add("Sales Commodity");
						}
						VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
						VDOC_DT.add(documentDate);
						VPOST_DT.add(app_dt);
						VALLOC_QTY.add(alloc_qty);
						//qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
						qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
						if(type_flag.equals("ACCRUAL") || type_flag.equals("ACCRUAL_REVERSAL"))
						{
							VITEMTEXT.add(itemText);
						}
						else
						{
							VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
						}
						
						sap_val=getSAP_Doc_Value(co_cd,tempAccount,app_dt,invoice_no,"1");
						VSAP_VALUE_INR.add(sap_val);
						delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
						VDELTA_SAP_VALUE_INR.add(delta_sap_val);
						VSAP_VALUE_USD.add("");
						VDELTA_SAP_VALUE_USD.add("");
						VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						VIS_SAP_DOC_DATA.add("");
						sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
						deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						extn_mapping+=","+tempAccount;
					}
					
					if(!type_flag.equals("ACCRUAL") && !type_flag.equals("ACCRUAL_REVERSAL"))
					{
						//if(tax_flag.equals("Y"))
						{
							int stcount=0;
							if(type_flag.equals("FFLOW"))
							{
								queryString="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_FFLOW_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
										+ "AND INVOICE_TYPE=?";
							}
							else
							{
								queryString="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
							}
							stmt1 = conn.prepareStatement(queryString);
							if(type_flag.equals("FFLOW"))
							{
								stmt1.setString(++stcount, comp_cd);
								stmt1.setString(++stcount, fin_year);
								stmt1.setString(++stcount, invoice_seq);
								stmt1.setString(++stcount, bu_state_tin);
								stmt1.setString(++stcount, invoice_type);
							}
							else
							{
								stmt1.setString(++stcount, comp_cd);
								stmt1.setString(++stcount, fin_year);
								stmt1.setString(++stcount, invoice_seq);
								stmt1.setString(++stcount, bu_state_tin);
							}
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
								String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
								String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
								if(!taxAmt.equals(""))
								{
									pk="50";
									sign="-";
									if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
									{
										pk = "40";
										sign = "";
									}
									String gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
									String tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
									extn_mapping+=","+gl_code;
									
									if(tax_flag.equals("Y"))
									{
										VCOUNTERPARTY_CD.add(counterpty_cd);
										VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
										VCO_CD.add(co_cd);
										VDEAL_MAP.add(disp_deal_map);
										VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
										VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
										VPERIOD_START_DT.add(period_start_dt);
										VPERIOD_END_DT.add(period_end_dt);
										VINVOICE_NO.add(invoice_no);
										VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
										VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
										VCURRENCY.add("INR");
										VDOC_TYPE.add("X2");
										VDOC_NO.add(documentNo);
										VPK.add(pk);
										VTRANS_TYPE.add("Sales Commodity");
										VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
										VDOC_DT.add(documentDate);
										VPOST_DT.add(app_dt);
										//VALLOC_QTY.add(alloc_qty); 		//AS PER FEEDBACK 20250516 
										VALLOC_QTY.add(""); 
										VITEMTEXT.add("T ["+tax_sap_code+"]");
										VAMT.add(sign+""+taxAmt);
										VUSD_AMT.add("");
										sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
										VSAP_VALUE_INR.add(sap_val);
										delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(taxAmt.equals("")?"0":sign+""+taxAmt) - Double.parseDouble(sap_val));
										VDELTA_SAP_VALUE_INR.add(delta_sap_val);
										VSAP_VALUE_USD.add("");
										VDELTA_SAP_VALUE_USD.add("");
										VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
										VIS_SAP_DOC_DATA.add("");
										sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
										deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
										//amtSum+=Double.parseDouble(sign+""+taxAmt);
										amtSum+=Double.parseDouble(taxAmt.equals("")?"0":sign+""+taxAmt);
									}
									
								}
							}
							rset1.close();
							stmt1.close();
						}
						
						if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
						{
							String gl_code="";
							String TcsTdscode="";
							pk="";
							String taxBase="";
							if(tcs_tds.equals("TCS"))
							{
								gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
								extn_mapping+=","+gl_code;
								if(tcs_flag.equals("Y"))
								{
									TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
									amt=tcs_amt;
									pk="50";
									sign="-";
									if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
									{
										pk = "40";
										sign = "";
									}
									VCOUNTERPARTY_CD.add(counterpty_cd);
									VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
									VCO_CD.add(co_cd);
									VDEAL_MAP.add(disp_deal_map);
									VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
									VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
									VPERIOD_START_DT.add(period_start_dt);
									VPERIOD_END_DT.add(period_end_dt);
									VINVOICE_NO.add(invoice_no);
									VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
									VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
									VCURRENCY.add("INR");
									VDOC_TYPE.add("X2");
									VDOC_NO.add(documentNo);
									VPK.add(pk);
									VTRANS_TYPE.add("Sales Commodity");
									VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
									VDOC_DT.add(documentDate);
									VPOST_DT.add(app_dt);
									//VALLOC_QTY.add(alloc_qty);
									VALLOC_QTY.add("");
									VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
									VAMT.add(sign+""+amt);
									VUSD_AMT.add("");
									sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
									VSAP_VALUE_INR.add(sap_val);
									delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
									VDELTA_SAP_VALUE_INR.add(delta_sap_val);
									VSAP_VALUE_USD.add("");
									VDELTA_SAP_VALUE_USD.add("");
									VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
									VIS_SAP_DOC_DATA.add("");
									sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
									deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
									//amtSum+=Double.parseDouble(sign+""+amt);
									amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
								}
							}
							else if(tcs_tds.equals("TDS"))
							{
								gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
								extn_mapping+=","+gl_code;
								if(tds_flag.equals("Y"))
								{
									TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
									amt=tds_amt;
									pk="40";
									if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
									{
										pk = "50";
										sign = "-";
									}
									VCOUNTERPARTY_CD.add(counterpty_cd);
									VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
									VCO_CD.add(co_cd);
									VDEAL_MAP.add(disp_deal_map);
									VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
									VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
									VPERIOD_START_DT.add(period_start_dt);
									VPERIOD_END_DT.add(period_end_dt);
									VINVOICE_NO.add(invoice_no);
									VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
									VGL_DESC.add(""+utilBean.getGLdesc(conn, gl_code));
									VCURRENCY.add("INR");
									VDOC_TYPE.add("X2");
									VDOC_NO.add(documentNo);
									VPK.add(pk);
									VTRANS_TYPE.add("Sales Commodity");
									VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
									VDOC_DT.add(documentDate);
									VPOST_DT.add(app_dt);
									//VALLOC_QTY.add(alloc_qty);
									VALLOC_QTY.add("");
									VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
									VAMT.add(sign+""+amt);
									VUSD_AMT.add("");
									sap_val=getSAP_Doc_Value(co_cd,gl_code,app_dt,invoice_no,"1");
									VSAP_VALUE_INR.add(sap_val);
									delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
									VDELTA_SAP_VALUE_INR.add(delta_sap_val);
									VSAP_VALUE_USD.add("");
									VDELTA_SAP_VALUE_USD.add("");
									VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
									VIS_SAP_DOC_DATA.add("");
									sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
									deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
									//amtSum+=Double.parseDouble(sign+""+amt);
									amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
								}
							}
						}
					}
				}
				addSAPRecord(counterpty_cd,agmt_no,agmt_rev_no,"",cont_no,cont_rev_no,cont_type,cargo_no,co_cd,invoice_no,app_dt,period_start_dt,period_end_dt,  extn_mapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapPurReconDetails()
	{
		String function_nm="getSapPurReconDetails()";
		try
		{
			int ctn=0;
			String co_cd="";
			String counterpty_cd="";
			String agmt_no="";
			String agmt_rev="";
			String cont_no="";
			String cont_rev="";
			String cont_type="";
			String cargo_no="";
			String invoice_no="";
			String approved_dt="";
			String accountPeriodMonth="";
			String accountPeriodYear="";
			String period_start_dt="";
			String period_end_dt="";
			String inv_flag="";
			String inv_head="";
			String gross_amt="";
			String net_payable="";
			String documentNo="";
			String cash_flow="";
			String boeno="";
			String ship_name="";
			String productionPeriodMonth="";
			String productionPeriodYear="";
			String monthNm="";
			String sap_approved_by="";
			String inv_dt="";
			String documentDate="";
			String postDate="";
			String alloc_qty="";
			String tcs_tds="";
			String exchangeRate="";
			String inv_raised_in="";
			String tcs_amt="";
			String tds_amt="";
			String tax_amt="";
			String tcsStructCd="";
			String tdsStructCd="";
			String fin_yr="";
			String invoice_seq="";
			String taxStructCd="";
			String inv_type="";
			String gen_type="";
			String inv_category="";
			String accrual_amt="";
			String prod_month="";
			String extn_mapping="";
			String plant_seq="";
			String bu_unit="";
			String inv_amt="";
			String criteria="";
			String ref_no="";
			
			String contType="";
		    if(entity_role.equals("V"))
	    	{
		    	contType="A";
	    	}
	    	else if(entity_role.equals("S"))
	    	{
	    		contType="Y";
	    	}
	    	else if(entity_role.equals("H"))
	    	{
	    		contType="H";
	    	}
			
			String queryString="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CARGO_NO, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.INV_FLAG, "
					+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
					+ "A.NET_PAYABLE_AMT,A.BOE_NO,A.SAP_APPROVED_BY, "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.ALLOC_QTY,A.TCS_TDS,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,  "
					+ "A.TCS_AMT,A.TDS_AMT,A.TAX_AMT,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD, "
					+ "NULL,NULL,'SG',NULL,NULL,NULL,NULL,NULL,PLANT_SEQ,BU_UNIT,INVOICE_AMT,REF_NO,CRITERIA  "
					+ "FROM FMS_PUR_SG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND A.CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			else if(entity_role.equals("0"))
			{
				//queryString+= "AND A.CONTRACT_TYPE IN (?,?,?) ";
			}
			else
			{
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= "AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CARGO_NO, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.INV_FLAG, "
					+ "(NVL(A.GROSS_AMT,0) + NVL(A.TRANSPORTATION_AMOUNT,0) + NVL(A.MARKET_MARGIN_AMT,0) + NVL(A.OTHER_CHARGES_AMT,0)),"
					+ "A.NET_PAYABLE_AMT,A.BOE_NO,A.SAP_APPROVED_BY, "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.ALLOC_QTY,A.TCS_TDS,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE, "
					+ "A.TCS_AMT,A.TDS_AMT,A.TAX_AMT,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD, "
					+ "NULL,NULL,'PG',NULL,NULL,NULL,NULL,NULL,PLANT_SEQ,BU_UNIT,INVOICE_AMT,REF_NO,CRITERIA  "
					+ "FROM FMS_PUR_PG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND A.CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			
			else if (entity_role.equals("0")) 
			{
				//queryString += "AND A.CONTRACT_TYPE IN (?,?,?) ";
			}
			 
			else
			{
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
				queryString+= "AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CARGO_NO, "
					+ "A.INVOICE_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,A.GROSS_AMT_INR,A.NET_PAYABLE_AMT,NULL,A.SAP_APPROVED_BY, "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.ALLOC_QTY,A.TCS_TDS,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,A.TCS_AMT, "
					+ "A.TDS_AMT,A.TAX_AMT,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD, "
					+ "A.INVOICE_TYPE,A.INV_HEAD,'FFLOW',A.INVOICE_CATEGORY,NULL,NULL,NULL,A.GROSS_AMT_USD,NULL,BU_UNIT,INVOICE_AMT,NULL,NULL "
					+ "FROM FMS_PUR_FFLOW_INV_MST A "
					+ "WHERE A.COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND A.CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			
			else if (entity_role.equals("0")) 
			{
				//queryString += "AND A.CONTRACT_TYPE IN (?,?,?) ";
			}
			 
			else
			{
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
				queryString+= "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//ACCRUAL
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CARGO_NO,"
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.INV_FLAG,A.GROSS_AMT,NULL,A.BOE_NO,NULL,"
					+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.ACCRUAL_QTY,NULL,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,NULL,"
					+ "NULL,NULL,NULL,NULL,A.FINANCIAL_YEAR,NULL,NULL,"
					+ "NULL,NULL,'ACCRUAL',NULL,A.ACCRUAL_AMT,A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),NULL,PLANT_SEQ,BU_UNIT,INVOICE_AMT,NULL,NULL "
					+ "FROM FMS_TRADER_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND A.CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			
			else if (entity_role.equals("0")) 
			{
				//queryString += "AND A.CONTRACT_TYPE IN (?,?,?) ";
			}
			 
			else
			{
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//ACCRUAL REVERSAL
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,A.CARGO_NO,"
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.INV_FLAG,A.GROSS_AMT,NULL,A.BOE_NO,NULL,"
					+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.ACCRUAL_QTY,NULL,A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,NULL,"
					+ "NULL,NULL,NULL,NULL,A.FINANCIAL_YEAR,NULL,NULL,"
					+ "NULL,NULL,'ACCRUAL_REVERSAL',NULL,A.ACCRUAL_AMT,A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),NULL,PLANT_SEQ,BU_UNIT,INVOICE_AMT,NULL,NULL "
					+ "FROM FMS_TRADER_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? ";
			if(entity_role.equals("T"))
			{
				queryString+= "AND A.CONTRACT_TYPE NOT IN (?,?,?) ";
			}
			
			else if (entity_role.equals("0")) 
			{
				//queryString += "AND A.CONTRACT_TYPE IN (?,?,?) ";
			}
			
			else
			{
				queryString+="AND A.CONTRACT_TYPE=? ";
			}
			queryString+= "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			if(entity_role.equals("T"))
			{
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, "A");
				stmt.setString(++ctn, "H");
			}
			else if(entity_role.equals("0"))
			{
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
			}
			else
			{
				stmt.setString(++ctn, contType);
			}
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			if(entity_role.equals("T"))
			{
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, "A");
				stmt.setString(++ctn, "H");
			}
			else if(entity_role.equals("0"))
			{
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
			}
			else
			{
				stmt.setString(++ctn, contType);
			}
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			if(entity_role.equals("T"))
			{
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, "A");
				stmt.setString(++ctn, "H");
			}
			else if(entity_role.equals("0"))
			{
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
			}
			else
			{
				stmt.setString(++ctn, contType);
			}
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			if(entity_role.equals("T"))
			{
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, "A");
				stmt.setString(++ctn, "H");
			}
			else if(entity_role.equals("0"))
			{
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
			}
			else
			{
				stmt.setString(++ctn, contType);
			}
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			//for reversal
			stmt.setString(++ctn, comp_cd);
			if(entity_role.equals("T"))
			{
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, "A");
				stmt.setString(++ctn, "H");
			}
			else if(entity_role.equals("0"))
			{
				//stmt.setString(++ctn, "Y");
				//stmt.setString(++ctn, "A");
				//stmt.setString(++ctn, "H");
			}
			else
			{
				stmt.setString(++ctn, contType);
			}
			stmt.setString(++ctn, lastDtOfPrevMonth);
			stmt.setString(++ctn, firstDtOfPrevMonth);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				extn_mapping="";
				
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				agmt_no=rset.getString(2)==null?"":rset.getString(2);
				agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				cont_no=rset.getString(4)==null?"":rset.getString(4);
				cont_rev=rset.getString(5)==null?"":rset.getString(5);
				cont_type=rset.getString(6)==null?"":rset.getString(6);
				cargo_no=rset.getString(7)==null?"":rset.getString(7);
				invoice_no=rset.getString(8)==null?"":rset.getString(8);
				approved_dt=rset.getString(9)==null?"":rset.getString(9);
				period_start_dt=rset.getString(10)==null?"":rset.getString(10);
				period_end_dt=rset.getString(11)==null?"":rset.getString(11);
				inv_flag=rset.getString(12)==null?"":rset.getString(12);
				gross_amt=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				net_payable=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				boeno=rset.getString(15)==null?"":rset.getString(15);
				sap_approved_by=rset.getString(16)==null?"":rset.getString(16);
				inv_dt=rset.getString(17)==null?"":rset.getString(17);
				alloc_qty=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
				tcs_tds=rset.getString(19)==null?"":rset.getString(19);
				//inv_raised_in=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				inv_raised_in=rset.getString(20)==null?"":rset.getString(20);
				exchangeRate=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
				tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23));
				tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24));
				tcsStructCd=rset.getString(25)==null?"":rset.getString(25);
				tdsStructCd=rset.getString(26)==null?"":rset.getString(26);
				fin_yr=rset.getString(27)==null?"":rset.getString(27);
				invoice_seq=rset.getString(28)==null?"":rset.getString(28);
				taxStructCd=rset.getString(29)==null?"":rset.getString(29);
				inv_type=rset.getString(30)==null?"":rset.getString(30);
				inv_head=rset.getString(31)==null?"":rset.getString(31);
				gen_type=rset.getString(32)==null?"":rset.getString(32);
				inv_category=rset.getString(33)==null?"":rset.getString(33);
				accrual_amt=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				cash_flow=rset.getString(35)==null?"":rset.getString(35);
				prod_month=rset.getString(36)==null?"":rset.getString(36);
				String gross_amt_usd=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				plant_seq=rset.getString(38)==null?"":rset.getString(38);
				bu_unit=rset.getString(39)==null?"":rset.getString(39);
				inv_amt=rset.getString(40)==null?"":nf.format(rset.getDouble(40));
				ref_no=rset.getString(41)==null?"":rset.getString(41);
				criteria=rset.getString(42)==null?"":rset.getString(42);
				
				co_cd = utilBean.getCompanySAPcode(conn, comp_cd);
				String deal_map = utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
				//String message_id = invoice_no.replaceAll("/", "-");
				String [] appr_dt = approved_dt.split("/");
				accountPeriodMonth=appr_dt[1];
				accountPeriodYear=appr_dt[2];
				String countpty_category = utilBean.getCounterpartyCategory(conn, counterpty_cd);
				//documentNo=invoice_no.replaceAll("/", "-");
				documentNo=getSapDocNo(invoice_no);
				String [] inv_dt_split = inv_dt.split("/");
				documentDate=inv_dt_split[2]+inv_dt_split[1]+inv_dt_split[0];
				postDate=appr_dt[2]+appr_dt[1]+appr_dt[0];
				
				extn_mapping=invoice_no+"@";
				
				String boe_number="";
				String boe_date="";
				if (cont_type.equals("N")) 
				{
					String queryString1 = "SELECT B.BOE_REF,TO_CHAR(B.BOE_DT,'DD-MON-YY'),A.SHIP_CD "
							+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_BUY_CARGO_ALLOC_BOE B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND A.CARGO_NO=? AND B.BOE_NO=? "
							+ "AND A.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CARGO_NO=B.CARGO_NO AND A.ALLOC_REV=B.ALLOC_REV";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, "M");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, cont_type);
					stmt1.setString(7, cargo_no);
					stmt1.setString(8, boeno);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) 
					{
						boe_number = rset.getString(1)==null?"":rset.getString(1);
						boe_date=rset.getString(2)==null?"":rset.getString(2);
						String ship_cd = rset1.getString(3) == null ? "" : rset1.getString(3);
						ship_name = utilBean.getShipName(conn, ship_cd);
					}
					rset1.close();
					stmt1.close();
					
					double profm_alloc_qty=0;
					double final_alloc_qty=0;
					if(inv_flag.equals("CF"))
					{
						String query="SELECT ALLOC_QTY,INV_FLAG "
								+ "FROM FMS_PUR_SG_INV_MST A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CARGO_NO=? AND A.BOE_NO=? AND A.INV_FLAG IN ('CP','P','F') AND A.PDF_INV_DTL IS NOT NULL  ";
						query+="UNION ALL ";
						query+="SELECT ALLOC_QTY,INV_FLAG "
								+ "FROM FMS_PUR_PG_INV_MST A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CARGO_NO=? AND A.BOE_NO=? AND A.INV_FLAG IN ('CP','P','F') AND A.PDF_INV_DTL IS NOT NULL ";
						stmt5=conn.prepareStatement(query);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, counterpty_cd);
						stmt5.setString(3, agmt_no);
						stmt5.setString(4, cont_no);
						stmt5.setString(5, cont_type);
						stmt5.setString(6, cargo_no);
						stmt5.setString(7, boe_number);
						stmt5.setString(8, comp_cd);
						stmt5.setString(9, counterpty_cd);
						stmt5.setString(10, agmt_no);
						stmt5.setString(11, cont_no);
						stmt5.setString(12, cont_type);
						stmt5.setString(13, cargo_no);
						stmt5.setString(14, boe_number);
						rset5=stmt5.executeQuery();
						while(rset5.next())
						{
							String invFlag=rset5.getString(2)==null?"":rset5.getString(2);
							if(invFlag.equals("CP"))
							{
								profm_alloc_qty=rset5.getDouble(1);
							}
						}
						rset5.close();
						stmt5.close();
						if(!alloc_qty.equals(""))
						{
							alloc_qty=nf.format(Double.parseDouble(alloc_qty)-profm_alloc_qty);
						}
					}
					else if(inv_flag.equals("F"))
					{
						alloc_qty=nf.format(0);
					}
				}
				
				String tempAccount="";
		    	String itemText="";
		    	String sign = "";
		    	String pk ="";
				String curr_type="INR";
				String doc_type="";
		    	if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
		    	{
		    		documentDate=postDate;
		    		postDate=gen_type.equals("ACCRUAL_REVERSAL")?to_dt.split("/")[2]+to_dt.split("/")[1]+"20":postDate;	//Accural reversal posting date should be 20th of Accounting period 
		    		doc_type="X3";
		    		
		    		monthNm = utilDate.getShortMonthName(prod_month);
		    		String monthId="";
					String yearNm = "";
					pk="40";
					
					if(cont_type.equals("G") || cont_type.equals("P"))
			    	{
			    		tempAccount="7340450";
			    	}
			    	else if(cont_type.equals("A"))
			    	{
			    		tempAccount="7241060";
			    	}
			    	else if(cont_type.equals("Y"))
			    	{
			    		tempAccount="7242060";
			    	}
			    	else if(cont_type.equals("H"))
			    	{
			    		tempAccount="6340200"; //AS PER VIJAY'S FEEDBACK ON 20241010 
			    	}
			    	else
			    	{
				    	if(countpty_category.equals("Group"))
				    	{
				    		//tempAccount=account; // Counterparty SAP CODE
				    		tempAccount="6301400";
				    	}
				    	else
				    	{
				    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
				    		//String pk="50";// PURCHASE ETRM ACCRUALS 
				    		tempAccount="6300400";
				    	}
			    	}
					
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
			    	itemText=cash_flow+" "+monthNm+" "+yearNm;
			    	
		    		if(cont_type.equals("N"))
				    {
		    			curr_type="USD";
		    			gross_amt=accrual_amt;
				    }
		    		else
		    		{
		    			curr_type="INR";
		    		}
		    		
		    		if(gen_type.equals("ACCRUAL_REVERSAL"))
		    		{
		    			pk=pk.equals("50")?"40":"50";
		    		}
		    	}
		    	else
		    	{
		    		doc_type="X1";
		    		if(gen_type.equals("FFLOW"))
					{
						if(inv_category.equals("P"))
						{
							cash_flow="Commodity";
						}
						else if(inv_category.equals("S"))
						{
							cash_flow="Service";
						}
					}
					else
					{
						cash_flow="Purchase Commodity";
						if(cont_type.equals("G") || cont_type.equals("P")) 
						{
							cash_flow="Capacity";
						}
					}
		    		
		    		if(!period_end_dt.equals(""))
					{
						String[] temp_split=period_end_dt.split("/");
						productionPeriodMonth=temp_split[1];
						productionPeriodYear=temp_split[2];		
					}
					monthNm=""+utilDate.getShortMonthName(period_end_dt);
		    		
		    		if(cont_type.equals("N") && (inv_flag.equals("F") || inv_flag.equals("P")))
				    {
		    			curr_type="USD";
				    }
				    else if(cont_type.equals("N") && inv_head.equals("N"))
				    {
				    	curr_type="USD";
				    }
				    else
				    {
				    	curr_type="INR"; 
				    }
		    		
		    		if(inv_raised_in.equals("2") && cont_type.equals("N"))
					{
		    			if(gen_type.equals("FFLOW"))
						{
		    				gross_amt=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
						}
					}
		    		
		    		if(inv_raised_in.equals("2") && !exchangeRate.equals("") && !cont_type.equals("N"))
					{
						net_payable=rset.getString(14)==null?"":nf.format(rset.getDouble(14) * Double.parseDouble(exchangeRate));
						
						if(gen_type.equals("FFLOW"))
						{
							gross_amt=rset.getString(37)==null?"":nf.format(rset.getDouble(37) * Double.parseDouble(exchangeRate));
						}
						else
						{
							gross_amt=rset.getString(13)==null?"":nf.format(rset.getDouble(13) * Double.parseDouble(exchangeRate));
						}
						tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24) * Double.parseDouble(exchangeRate));
					}
					
					if(inv_raised_in.equals("2"))
				    {
					    double temp_net_amt=0;
					    double temp_diff=0;
					    double temp_gross=0;
					    if(!net_payable.equals(""))
					    {
					    	temp_net_amt=Double.parseDouble(net_payable);
					    }
					    if(!gross_amt.equals(""))
					    {
					    	 temp_gross = Double.parseDouble(gross_amt);
					    }
					    
					    /*if(!tax_amt.equals(""))
					    {
					    	 temp_gross = temp_gross + Double.parseDouble(tax_amt);
					    }*/
					    String queryString1="";
					    if(gen_type.equals("SG"))
						{
					    	queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						}
				    	else if(gen_type.equals("PG"))
						{
				    		queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
						}
				    	else if(gen_type.equals("FFLOW"))
						{
				    		queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
						}
					    stmt1=conn.prepareStatement(queryString1);
					    stmt1.setString(1, comp_cd);
					    stmt1.setString(2, fin_yr);
					    stmt1.setString(3, invoice_seq);
					    stmt1.setString(4, cont_type);
					    if(gen_type.equals("FFLOW"))
					    {
					    	stmt1.setString(5, inv_type);
					    }
					    else
					    {
					    	stmt1.setString(5, inv_flag);
					    }
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
							
							if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
							{
								taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchangeRate));
							}
							
							if(!taxAmt.equals(""))
							{
								temp_gross = temp_gross + Double.parseDouble(taxAmt);
							}
						}
					    rset1.close();
					    stmt1.close();
					    
					    if(tcs_tds.equals("TCS") && !tcs_amt.equals(""))
				    	{
					    	temp_gross = temp_gross + Double.parseDouble(tcs_amt);
					    }
				    	
				    	temp_diff = temp_net_amt - temp_gross;
					    if(temp_diff <= 0.05 && temp_diff > 0)
					    {
					    	net_payable = nf.format(Double.parseDouble(net_payable) - (temp_diff));
					    }
				    	else if(temp_diff >= -0.05 && temp_diff < 0)
				    	{
				    		net_payable = nf.format(Double.parseDouble(net_payable) + (temp_diff * -1));
					    }
				    }
					if(!inv_flag.equals("UG") && !inv_head.equals("LUG"))
			    	{
			    		if(inv_flag.equals("CP") || inv_flag.equals("CF") || inv_head.equals("CD"))
					    {
					    	gross_amt=net_payable;
					    }
			    		if(!gross_amt.equals("") && !gross_amt.equals("0.00"))
					    {
			    			sign = "";
					    	pk = "40";
					    	if(inv_type.equals("CR") || inv_type.equals("CCR"))
					    	{
					    		pk = "50";
					    		sign = "-";
					    	}
					    	else if(cont_type.equals("N") && (inv_flag.equals("F") || inv_flag.equals("CF")) && Double.parseDouble(gross_amt) < 0)
					    	{
					    		pk = "50";
					    		sign = "";
					    	}
			    			
					    	if(inv_flag.equals("CR")||inv_flag.equals("DR"))
					    	{
					    		if(Double.parseDouble(gross_amt) < 0)
						    	{
						    		pk = "50";
						    		sign = "-";
						    		gross_amt=nf.format(Math.abs(Double.parseDouble(gross_amt)));
						    	}
					    	}
					    	
			    			if(inv_type.equals("LP") || inv_head.equals("LP"))
					    	{
					    		tempAccount="8226000"; //20241023
					    		itemText="Interest";
					    	}
					    	else if(inv_head.equals("IMB"))
					    	{
					    		tempAccount="6318400";
					    		itemText="Commodity";
					    	}
					    	else if(inv_head.equals("DMR"))
					    	{
					    		tempAccount="7225900";
					    		itemText="Demurrage";
					    	}
					    	else if(inv_head.equals("ST"))
					    	{
					    		tempAccount="6320405";
					    		itemText="Commodity";
					    	}
					    	else if(cont_type.equals("A"))
					    	{
					    		tempAccount="7241060";
					    		itemText="Vessel Agent Service Fee";
					    		cash_flow="Service";
					    	}
					    	else if(cont_type.equals("Y"))
					    	{
					    		tempAccount="7242060";
					    		itemText="Surveyor Service Fee";
					    		cash_flow="Service";
					    	}
					    	else if(cont_type.equals("H"))
					    	{
					    		//tempAccount="7241060";
					    		tempAccount="6340200"; //AS PER VIJAY'S FEEDBACK ON 20241010 
					    		itemText="Custom House Agent Service Fee";
					    		cash_flow="Service";
					    	}
					    	else if(cont_type.equals("G") || cont_type.equals("P"))
					    	{
					    		//tempAccount="7340450";
					    		if(countpty_category.equals("Group"))
					    		{
					    			tempAccount="7340450";
					    		}
					    		else
					    		{
					    			tempAccount="7340550";
					    		}
					    		itemText=cash_flow;
					    	}
					    	else if(inv_type.equals("CR") || inv_type.equals("CCR"))
					    	{
					    		if(inv_head.equals("CD")) 
					    		{
					    			tempAccount="7220700";
						    		itemText="Custom Duty";
					    		}
					    		else 
					    		{
						    		//tempAccount="7220300";
						    		//itemText="Brokerage/Commission";
						    		itemText="Commodity";
						    		if(countpty_category.equals("Group"))
							    	{
							    		tempAccount="6301400";
							    	}
							    	else
							    	{
							    		tempAccount="6300400";
							    	}
					    		}
					    	}
					    	else if(inv_flag.equals("CP") || inv_flag.equals("CF") || inv_head.equals("CD"))
					    	{
					    		tempAccount="7220700";
					    		if(inv_head.equals("CD")) 
					    		{
					    			itemText="Custom Duty";
					    		}
					    		else
					    		{
					    		 itemText="BOE "+boe_number+" Dated "+boe_date+"("+ship_name+")";
					    		}
					    	}
					    	else
					    	{	
					    		if(cont_type.equals("N"))
					    		{
					    			itemText="Commodity ("+ship_name+")";
					    		}
					    		else
					    		{
					    			itemText=cash_flow;
					    		}
						    	if(countpty_category.equals("Group"))
						    	{
						    		tempAccount="6301400";
						    	}
						    	else
						    	{
						    		tempAccount="6300400";
						    	}
					    	}
					    }
			    		itemText+=" "+monthNm+" "+productionPeriodYear;
			    	}
		    	}
		    	if(!inv_flag.equals("UG") && !inv_head.equals("LUG") && !gross_amt.equals("0.00"))
		    	{
		    		if(gen_type.equals("ACCRUAL_REVERSAL"))
		    		{
		    			sign=sign.equals("-")?"":"-";
		    		}
		    		VCOUNTERPARTY_CD.add(counterpty_cd);
		    		VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    		VCO_CD.add(co_cd);
		    		VDEAL_MAP.add(deal_map);
		    		VINVOICE_NO.add(invoice_no);
		    		VACCOUNT_PERIOD_YR.add(accountPeriodYear);
		    		VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
		    		VPERIOD_START_DT.add(period_start_dt);
		    		VPERIOD_END_DT.add(period_end_dt);
		    		VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
		    		VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
		    		VCURRENCY.add(curr_type);
		    		VDOC_TYPE.add(doc_type);		
		    		VPK.add(pk);
		    		VDOC_NO.add(documentNo);
		    		if(gen_type.equals("ACCRUAL_REVERSAL"))
		    		{
		    			VTRANS_TYPE.add("Purchase Accrual Reversal");
		    		}
		    		else if(gen_type.equals("ACCRUAL"))
		    		{
		    			VTRANS_TYPE.add("Purchase Accrual");
		    		}
		    		else
		    		{
		    			VTRANS_TYPE.add(cash_flow);
		    		}
		    		VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
		    		VDOC_DT.add(documentDate);
		    		VPOST_DT.add(postDate);
		    		VALLOC_QTY.add(alloc_qty);
		    		qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
		    		VITEMTEXT.add(itemText);
		    		VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    		VIS_SAP_DOC_DATA.add("");
		    		if(curr_type.equals("USD"))
		    		{
		    			VUSD_AMT.add(sign+""+gross_amt);
		    			VAMT.add("");
		    			amtUsdSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
		    			VSAP_VALUE_INR.add("");
		    			VDELTA_SAP_VALUE_INR.add("");
		    			String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDate,invoice_no,"2");
		    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
		    			VSAP_VALUE_USD.add(sap_val);
						VDELTA_SAP_VALUE_USD.add(delta_sap_val);
						sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
						deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    		}
		    		else
		    		{
		    			VAMT.add(sign+""+gross_amt);
		    			VUSD_AMT.add("");
		    			//amtSum+=Double.parseDouble(sign+""+gross_amt);
		    			amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
		    			String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDate,invoice_no,"1");
		    			VSAP_VALUE_INR.add(sap_val);
		    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
		    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    			VSAP_VALUE_USD.add("");
						VDELTA_SAP_VALUE_USD.add("");
		    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    		}
		    		extn_mapping+=tempAccount+",";
		    	}
				
				if(!inv_flag.equals("CP") && !inv_flag.equals("CF") && !inv_head.equals("CD") && !gen_type.equals("ACCRUAL")&& !gen_type.equals("ACCRUAL_REVERSAL"))
			    {
					if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
				    {
				    	String gl_code="";
				    	String TcsTdscode="";
				    	String amt="";
				    	sign="";
				    	pk="";
				    	
				    	if(tcs_tds.equals("TCS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
				    		extn_mapping+=gl_code+",";
				    		
				    		if(tcs_flag.equals("Y"))
				    		{
				    			TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
				    			amt=tcs_amt;
				    			
				    			if(inv_flag.equals("CR")||inv_flag.equals("DR"))
				    			{
				    				sign = "";
							    	pk = "50";
							    	
							    	if(Double.parseDouble(tcs_amt) < 0)
							    	{
							    		pk = "40";
							    		sign = "-";
							    		amt=nf.format(Math.abs(Double.parseDouble(tcs_amt)));
							    	}
						    	}
				    			else
				    			{
				    				pk="40";
				    				
				    				if(inv_type.equals("CR") || inv_type.equals("CCR"))
				    				{
				    					pk = "50";
				    					sign = "-";
				    				}
				    			}
				    			
				    			VCOUNTERPARTY_CD.add(counterpty_cd);
				    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    			VCO_CD.add(co_cd);
				    			VDEAL_MAP.add(deal_map);
				    			VINVOICE_NO.add(invoice_no);
				    			VACCOUNT_PERIOD_YR.add(accountPeriodYear);
				    			VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
				    			VPERIOD_START_DT.add(period_start_dt);
				    			VPERIOD_END_DT.add(period_end_dt);
				    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
				    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
				    			VCURRENCY.add(curr_type);
				    			VDOC_TYPE.add(doc_type);		
				    			VPK.add(pk);
				    			VDOC_NO.add(documentNo);
				    			VTRANS_TYPE.add(cash_flow);
				    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
				    			VDOC_DT.add(documentDate);
				    			VPOST_DT.add(postDate);
				    			//VALLOC_QTY.add(alloc_qty);
				    			VALLOC_QTY.add("");
				    			VITEMTEXT.add(tcs_tds +" ["+TcsTdscode+"]");
				    			VAMT.add(sign+""+amt);
				    			VUSD_AMT.add("");
				    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
				    			VSAP_VALUE_INR.add(sap_val);
				    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
				    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    			VSAP_VALUE_USD.add("");
				    			VDELTA_SAP_VALUE_USD.add("");
				    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				    			VIS_SAP_DOC_DATA.add("");
				    			//amtSum+=Double.parseDouble(sign+""+amt);
				    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
				    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
				    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
				    		}
				    	}
				    	else if(tcs_tds.equals("TDS") && !tds_amt.equals("0.00"))
				    	{
			    			gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
			    			extn_mapping+=gl_code;
			    			
			    			if(tds_flag.equals("Y"))
			    			{
			    				TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
			    				amt=tds_amt;
			    				
		    					pk="50";
		    					
		    					sign="-";
		    					if(inv_type.equals("CR") || inv_type.equals("CCR")||inv_flag.equals("CR"))
		    					{
		    						pk = "40";
		    						sign = "";
		    						amt=nf.format(Math.abs(Double.parseDouble(tds_amt)));
		    					}
			    				
			    				VCOUNTERPARTY_CD.add(counterpty_cd);
			    				VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
			    				VCO_CD.add(co_cd);
			    				VDEAL_MAP.add(deal_map);
			    				VINVOICE_NO.add(invoice_no);
			    				VACCOUNT_PERIOD_YR.add(accountPeriodYear);
			    				VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
			    				VPERIOD_START_DT.add(period_start_dt);
			    				VPERIOD_END_DT.add(period_end_dt);
			    				VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
			    				VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
			    				VCURRENCY.add(curr_type);
			    				VDOC_TYPE.add(doc_type);		
			    				VPK.add(pk);
			    				VDOC_NO.add(documentNo);
			    				VTRANS_TYPE.add(cash_flow);
			    				VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
			    				VDOC_DT.add(documentDate);
			    				VPOST_DT.add(postDate);
			    				//VALLOC_QTY.add(alloc_qty);
			    				VALLOC_QTY.add("");
			    				VITEMTEXT.add(tcs_tds +" ["+TcsTdscode+"]");
			    				VAMT.add(sign+""+amt);
			    				VUSD_AMT.add("");
			    				String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
			    				VSAP_VALUE_INR.add(sap_val);
			    				String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
			    				VDELTA_SAP_VALUE_INR.add(delta_sap_val);
			    				VSAP_VALUE_USD.add("");
			    				VDELTA_SAP_VALUE_USD.add("");
			    				VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
			    				VIS_SAP_DOC_DATA.add("");
			    				//amtSum+=Double.parseDouble(sign+""+amt);
			    				amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
			    				sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
			    				deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
			    			}
				    	}
				    }
					//if(tax_flag.equals("Y"))		
					{
						if(criteria.equals("TAXP"))
						{
							//Main Invoice TAX Details - feedback By Vijay 26/02/2026
				    		String tmp_queryString="";
				    		if(gen_type.equals("SG"))
							{
					    		tmp_queryString="SELECT FINANCIAL_YEAR,INVOICE_SEQ,INV_FLAG "
					    				+ "FROM FMS_PUR_SG_INV_MST "
					    				+ "WHERE COMPANY_CD=? AND SYS_INV_NO=? ";
							}
					    	else if(gen_type.equals("PG"))
							{
					    		tmp_queryString="SELECT FINANCIAL_YEAR,INVOICE_SEQ,INV_FLAG "
					    				+ "FROM FMS_PUR_PG_INV_MST "
					    				+ "WHERE COMPANY_CD=? AND SYS_INV_NO=? ";
							}
				    		stmt6=conn.prepareStatement(tmp_queryString);
				    		stmt6.setString(1, comp_cd);
				    		stmt6.setString(2, ref_no);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								String main_fin_year=rset6.getString(1)==null?"":rset6.getString(1);
								String main_invoice_seq=rset6.getString(2)==null?"":rset6.getString(2);
								String main_inv_flag=rset6.getString(3)==null?"":rset6.getString(3);
								
								String query1="";
								if(gen_type.equals("SG"))
								{
							    	query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
											+ "FROM FMS_PUR_SG_INV_TAX_DTL "
											+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
											+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
								}
						    	else if(gen_type.equals("PG"))
								{
						    		query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
											+ "FROM FMS_PUR_PG_INV_TAX_DTL "
											+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
											+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
								}
							    stmt1=conn.prepareStatement(query1);
							    stmt1.setString(1, comp_cd);
							    stmt1.setString(2, main_fin_year);
							    stmt1.setString(3, main_invoice_seq);
							    stmt1.setString(4, cont_type);
						    	stmt1.setString(5, main_inv_flag);
								rset1=stmt1.executeQuery();
								while(rset1.next())
								{
									String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
									String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
									String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
									String taxAmt_USD=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
									String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
									
									if(!taxAmt.equals(""))
								    {
										if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
										{
											taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchangeRate));
										}
										
										pk = "50";
							    		sign = "-";
							    		taxAmt=nf.format(Math.abs(Double.parseDouble(taxAmt)));
							    		taxAmt_USD=nf.format(Math.abs(Double.parseDouble(taxAmt_USD)));
							    		
							    		String gl_code="";
								    	String tax_sap_code="";
								    	String amt=taxAmt;
								    	
								    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
								    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
								    	extn_mapping+=","+gl_code;
								    	
								    	if(tax_flag.equals("Y"))
										{
								    		if(Double.parseDouble(amt) > 0)
								    		{
								    			VCOUNTERPARTY_CD.add(counterpty_cd);
								    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
								    			VCO_CD.add(co_cd);
								    			VDEAL_MAP.add(deal_map);
								    			VINVOICE_NO.add(invoice_no);
								    			VACCOUNT_PERIOD_YR.add(accountPeriodYear);
								    			VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
								    			VPERIOD_START_DT.add(period_start_dt);
								    			VPERIOD_END_DT.add(period_end_dt);
								    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
								    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
								    			VCURRENCY.add(curr_type);
								    			VDOC_TYPE.add(doc_type);		
								    			VPK.add(pk);
								    			VDOC_NO.add(documentNo);
								    			VTRANS_TYPE.add(cash_flow);
								    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
								    			VDOC_DT.add(documentDate);
								    			VPOST_DT.add(postDate);
								    			//VALLOC_QTY.add(alloc_qty);
								    			VALLOC_QTY.add("");
								    			VITEMTEXT.add("T" +" ["+tax_sap_code+"]");
								    			VAMT.add(sign+""+amt);
								    			VUSD_AMT.add("");
								    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
								    			VSAP_VALUE_INR.add(sap_val);
								    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
								    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
								    			VSAP_VALUE_USD.add("");
								    			VDELTA_SAP_VALUE_USD.add("");
								    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
								    			VIS_SAP_DOC_DATA.add("");
								    			//amtSum+=Double.parseDouble(sign+""+amt);
								    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
								    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
								    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
								    		}
										}
								    }
								}
								rset1.close();
								stmt1.close();
							}
							rset6.close();
							stmt6.close();
							
							//NEW Invoice TAX Details - feedback By Vijay 26/02/2026
							String query1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_INV_CRDR_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=? AND IS_SGPG=?";
							stmt1=conn.prepareStatement(query1);
						    stmt1.setString(1, comp_cd);
						    stmt1.setString(2, fin_yr);
						    stmt1.setString(3, invoice_seq);
						    stmt1.setString(4, cont_type);
					    	stmt1.setString(5, inv_flag);
					    	stmt1.setString(6, gen_type);
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
								String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
								String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								String taxAmt_USD=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
								
								if(!taxAmt.equals(""))
							    {
									if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
									{
										taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchangeRate));
									}
									
									sign = "";
							    	pk = "40";
						    		taxAmt=nf.format(Math.abs(Double.parseDouble(taxAmt)));
						    		taxAmt_USD=nf.format(Math.abs(Double.parseDouble(taxAmt_USD)));
							    	
							    	String gl_code="";
							    	String tax_sap_code="";
							    	String amt=taxAmt;
							    	
							    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
							    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
							    	
							    	extn_mapping+=","+gl_code;
							    	
							    	if(tax_flag.equals("Y"))
							    	{
							    		if(Double.parseDouble(amt) > 0)
								    	{
							    			VCOUNTERPARTY_CD.add(counterpty_cd);
							    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
							    			VCO_CD.add(co_cd);
							    			VDEAL_MAP.add(deal_map);
							    			VINVOICE_NO.add(invoice_no);
							    			VACCOUNT_PERIOD_YR.add(accountPeriodYear);
							    			VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
							    			VPERIOD_START_DT.add(period_start_dt);
							    			VPERIOD_END_DT.add(period_end_dt);
							    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
							    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
							    			VCURRENCY.add(curr_type);
							    			VDOC_TYPE.add(doc_type);		
							    			VPK.add(pk);
							    			VDOC_NO.add(documentNo);
							    			VTRANS_TYPE.add(cash_flow);
							    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
							    			VDOC_DT.add(documentDate);
							    			VPOST_DT.add(postDate);
							    			//VALLOC_QTY.add(alloc_qty);
							    			VALLOC_QTY.add("");
							    			VITEMTEXT.add("T" +" ["+tax_sap_code+"]");
							    			VAMT.add(sign+""+amt);
							    			VUSD_AMT.add("");
							    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
							    			VSAP_VALUE_INR.add(sap_val);
							    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
							    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							    			VSAP_VALUE_USD.add("");
							    			VDELTA_SAP_VALUE_USD.add("");
							    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
							    			VIS_SAP_DOC_DATA.add("");
							    			//amtSum+=Double.parseDouble(sign+""+amt);
							    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
							    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							    		}
							    	}
							    }
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							String queryString2="";
							if(gen_type.equals("SG"))
							{
								queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_SG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
							}
							else if(gen_type.equals("PG"))
							{
								queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_PG_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INV_FLAG=?";
							}
							else if(gen_type.equals("FFLOW"))
							{
								queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
										+ "FROM FMS_PUR_FFLOW_INV_TAX_DTL "
										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
										+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
							}
							stmt1 = conn.prepareStatement(queryString2);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, fin_yr);
							stmt1.setString(3, invoice_seq);
							stmt1.setString(4, cont_type);
							if(gen_type.equals("FFLOW"))
							{
								stmt1.setString(5, inv_type);
							}
							else
							{
								stmt1.setString(5, inv_flag);
							}
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
								String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
								String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
								if(taxBaseAmt.equals(""))
								{
									taxBaseAmt=gross_amt;
								}
								else
								{
									if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
									{
										taxBaseAmt=nf.format(Double.parseDouble(taxBaseAmt) * Double.parseDouble(exchangeRate));
									}
								}
								if(!taxAmt.equals(""))
								{
									if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
									{
										taxAmt=nf.format(Double.parseDouble(taxAmt) * Double.parseDouble(exchangeRate));
									}
									pk="40";
									sign="";
									
									if (inv_type.equals("CR") || inv_type.equals("CCR")) 
									{
										pk = "50";
										sign = "-";
									}
									
									if(inv_flag.equals("CR")||inv_flag.equals("DR"))
									{
										if(Double.parseDouble(taxAmt) < 0)
										{
											pk = "50";
											sign = "-";
											taxAmt=nf.format(Math.abs(Double.parseDouble(taxAmt)));
										}
									}
									
									String gl_code="";
									String tax_sap_code="";
									String amt=taxAmt;
									gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
									tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
									extn_mapping+=","+gl_code;
									
									if(tax_flag.equals("Y"))
									{
										VCOUNTERPARTY_CD.add(counterpty_cd);
										VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
										VCO_CD.add(co_cd);
										VDEAL_MAP.add(deal_map);
										VINVOICE_NO.add(invoice_no);
										VACCOUNT_PERIOD_YR.add(accountPeriodYear);
										VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
										VPERIOD_START_DT.add(period_start_dt);
										VPERIOD_END_DT.add(period_end_dt);
										VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
										VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
										VCURRENCY.add(curr_type);
										VDOC_TYPE.add(doc_type);		
										VPK.add(pk);
										VDOC_NO.add(documentNo);
										VTRANS_TYPE.add(cash_flow);
										VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
										VDOC_DT.add(documentDate);
										VPOST_DT.add(postDate);
										//VALLOC_QTY.add(alloc_qty);
										VALLOC_QTY.add("");
										VITEMTEXT.add("T" +" ["+tax_sap_code+"]");
										VAMT.add(sign+""+amt);
										VUSD_AMT.add("");
										String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
										VSAP_VALUE_INR.add(sap_val);
										String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
										VDELTA_SAP_VALUE_INR.add(delta_sap_val);
										VSAP_VALUE_USD.add("");
										VDELTA_SAP_VALUE_USD.add("");
										VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
										VIS_SAP_DOC_DATA.add("");
										//amtSum+=Double.parseDouble(sign+""+amt);
										amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
										sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
										deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
									}
								}
							}
							rset1.close();
							stmt1.close();
						}
					}
			    }
				if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
				{
					if(tax_flag.equals("Y"))
					{
						String queryString5="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
								+ "FROM FMS_TRADER_ACCRUAL_TAX_DTL "
								+ "WHERE COMPANY_CD=? "
								//+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')  "
								+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND CARGO_NO=? AND BOE_NO=? AND INV_FLAG=? ";
						int count=0;
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(++count, comp_cd);
						/*if(gen_type.equals("ACCRUAL"))
						{
							stmt5.setString(++count, to_dt);
							stmt5.setString(++count, from_dt);
						}
						else
						{
							stmt5.setString(++count, lastDtOfPrevMonth);
							stmt5.setString(++count, firstDtOfPrevMonth);
						}*/
						stmt5.setString(++count, approved_dt);
						stmt5.setString(++count, period_start_dt);
						stmt5.setString(++count, period_end_dt);
						stmt5.setString(++count, counterpty_cd);
						stmt5.setString(++count, agmt_no);
						stmt5.setString(++count, cont_no);
						stmt5.setString(++count, cont_type);
						stmt5.setString(++count, plant_seq);
						stmt5.setString(++count, bu_unit);
						stmt5.setString(++count, fin_yr);
						stmt5.setString(++count, cargo_no);
						stmt5.setString(++count, boeno);
						stmt5.setString(++count, inv_flag);
						rset5=stmt5.executeQuery();
						while(rset5.next())
						{
							String tax_code=rset5.getString(1)==null?"":rset5.getString(1);
							String taxStrctCd=rset5.getString(2)==null?"":rset5.getString(2);
							String taxAmt=rset5.getString(3)==null?"":nf.format(rset5.getDouble(3));
							String taxBaseAmt=rset5.getString(4)==null?"":nf.format(rset5.getDouble(4));
							if(taxBaseAmt.equals(""))
							{
								taxBaseAmt=gross_amt;
							}
							
							if(!taxAmt.equals(""))
						    {
								//if(Double.parseDouble(tax_amt)>0) AS DISCUSSED WITH VIJAY, SEND ZERO TAX 
						    	{
						    		pk="40";
							    	sign="";
							    	
							    	String gl_code="";
							    	String tax_sap_code="";
							    	String amt=taxAmt;
							    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
							    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
							    	
							    	extn_mapping+=","+gl_code;
							    	
							    	if(Double.parseDouble(amt) > 0)
							    	{
										VCOUNTERPARTY_CD.add(counterpty_cd);
										VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
										VCO_CD.add(co_cd);
										VDEAL_MAP.add(deal_map);
										VINVOICE_NO.add(invoice_no);
										VACCOUNT_PERIOD_YR.add(accountPeriodYear);
										VACCOUNT_PERIOD_MONTH.add(accountPeriodMonth);
										VPERIOD_START_DT.add(period_start_dt);
										VPERIOD_END_DT.add(period_end_dt);
										VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
										VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
										VCURRENCY.add(curr_type);
										VDOC_TYPE.add(doc_type);		
										VPK.add(pk);
										VDOC_NO.add(documentNo);
										if(gen_type.equals("ACCRUAL_REVERSAL"))
							    		{
							    			VTRANS_TYPE.add("Purchase Accrual Reversal");
							    		}
							    		else if(gen_type.equals("ACCRUAL"))
							    		{
							    			VTRANS_TYPE.add("Purchase Accrual");
							    		}
							    		else
							    		{
							    			VTRANS_TYPE.add(cash_flow);
							    		}
										VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
										VDOC_DT.add(documentDate);
										VPOST_DT.add(postDate);
										//VALLOC_QTY.add(alloc_qty);
										VALLOC_QTY.add("");
										VITEMTEXT.add("T" +" ["+tax_sap_code+"]");
										if(curr_type.equals("USD"))
							    		{
											VUSD_AMT.add(sign+""+amt);
											VAMT.add("");
											String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"2");
											VSAP_VALUE_INR.add("");
											String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
											VDELTA_SAP_VALUE_INR.add("");
											VDELTA_SAP_VALUE_USD.add(delta_sap_val);
											VSAP_VALUE_USD.add(sap_val);
											sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
											deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							    		}
										else
										{
											VUSD_AMT.add("");
											VAMT.add(sign+""+amt);
											String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDate,invoice_no,"1");
											VSAP_VALUE_INR.add(sap_val);
											String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
											VDELTA_SAP_VALUE_INR.add(delta_sap_val);
											VDELTA_SAP_VALUE_USD.add("");
											VSAP_VALUE_USD.add("");
											sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
											deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
										}
										amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
										VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
										VIS_SAP_DOC_DATA.add("");
										//amtSum+=Double.parseDouble(sign+""+amt);
									}
						    	}
						    }
						}
						rset5.close();
						stmt5.close();
					}
				}
				
				addSAPRecord(counterpty_cd,agmt_no,agmt_rev,"",cont_no,cont_rev,cont_type,cargo_no,co_cd,invoice_no,postDate,period_start_dt,period_end_dt,  extn_mapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapTransporterDetails()
	{
		String function_nm="getSapTransporterDetails()";
		try
		{
			int ctn=0;
			String co_cd="";
			String counterpty_cd="";
			String agmt_no="";
			String agmt_rev="";
			String cont_no="";
			String cont_rev="";
			String cont_type="";
			String deal_map="";
			String invoice_no="";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			String approve_dt="";
			String period_start_dt="";
			String period_end_dt="";
			String gross_amt="";
			String net_payable="";
			String exchangeRate="";
			String inv_raised_in="";
			String invoice_type="";
			String productionPeriodMonth="";
			String productionPeriodYear="";
			String monthNm="";
			String sap_approve_by="";
			String inv_dt="";
			String alloc_qty="";
			String tcs_tds="";
			String tcsStructCd="";
			String tdsStructCd="";
			String invAmt="";
			String tcs_amt="";
			String tds_amt="";
			String tax_amt="";
			String tds_factor="";
			String fin_yr="";
			String invoice_seq="";
			String taxStructCd="";
			String gen_type="";
			String sub_inv_type="";
			String cash_flow="";
			String prod_month="";
			String buSeq="";
			String trans_bu_seq="";
			String invComponent="";
			
			String extn_mapping="";
			
			String queryString="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT, A.NET_PAYABLE_AMT,A.EXCHG_RATE_VALUE,A.INVOICE_RAISED_IN,A.INVOICE_TYPE,A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.ALLOC_QTY,A.TCS_TDS,NULL,A.TDS_STRUCT_CD,NULL,A.TDS_AMT,A.TAX_AMT,A.TDS_FACTOR,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD,'SG',NULL, "
					+ "NULL,NULL,NULL, "
					+ "A.TRANS_BU_UNIT,A.BU_UNIT,A.INV_COMPONENT  "
					+ "FROM FMS_GTA_SG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT, A.NET_PAYABLE_AMT,A.EXCHG_RATE_VALUE,A.INVOICE_RAISED_IN,A.INVOICE_TYPE,A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.ALLOC_QTY,A.TCS_TDS,NULL,A.TDS_STRUCT_CD,NULL,A.TDS_AMT,A.TAX_AMT,A.TDS_FACTOR,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD,'PG',NULL, "
					+ "NULL,NULL,NULL,"
					+ "A.TRANS_BU_UNIT,A.BU_UNIT,A.INV_COMPONENT  "
					+ "FROM FMS_GTA_PG_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.INVOICE_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_INR,A.NET_PAYABLE_AMT,A.EXCHG_RATE_VALUE,A.INVOICE_RAISED_IN,A.INVOICE_TYPE,A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.ALLOC_QTY,A.TCS_TDS,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD,A.TCS_AMT,A.TDS_AMT,A.TAX_AMT,NULL,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.TAX_STRUCT_CD,'FFLOW',A.GROSS_AMT_USD, "
					+ "A.SUB_INV_TYPE,NULL,NULL, "
					+ "NULL,A.BU_UNIT,NULL "
					+ "FROM FMS_GTA_FFLOW_INV_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? " 
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//FOR ACCRUAL
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,NULL,A.EXCHG_RATE_VALUE,A.INVOICE_RAISED_IN,NULL,NULL,TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.ACCRUAL_QTY, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.FINANCIAL_YEAR,NULL,NULL,'ACCRUAL',NULL,NULL,A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'), "
					+ "A.TRANS_BU_UNIT,A.BU_UNIT,A.INV_COMPONENT "
					+ "FROM FMS_GTA_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//FOR ACCRUAL REVERSAL
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,NULL,A.EXCHG_RATE_VALUE,A.INVOICE_RAISED_IN,NULL,NULL,TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.ACCRUAL_QTY, "
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.FINANCIAL_YEAR,NULL,NULL,'ACCRUAL_REVERSAL',NULL,NULL,A.CASH_FLOW,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'), "
					+ "A.TRANS_BU_UNIT,A.BU_UNIT,A.INV_COMPONENT "
					+ "FROM FMS_GTA_ACCRUAL_DTL A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, lastDtOfPrevMonth);
			stmt.setString(++ctn, firstDtOfPrevMonth);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				extn_mapping="";
				
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				agmt_no=rset.getString(2)==null?"":rset.getString(2);
				agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				cont_no=rset.getString(4)==null?"":rset.getString(4);
				cont_rev=rset.getString(5)==null?"":rset.getString(5);
				cont_type=rset.getString(6)==null?"":rset.getString(6);
				invoice_no=rset.getString(7)==null?"":rset.getString(7);
				approve_dt=rset.getString(8)==null?"":rset.getString(8);
				period_start_dt=rset.getString(9)==null?"":rset.getString(9);
				period_end_dt=rset.getString(10)==null?"":rset.getString(10);
				gross_amt=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				net_payable=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				exchangeRate=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				inv_raised_in=rset.getString(14)==null?"":rset.getString(14);
				invoice_type=rset.getString(15)==null?"":rset.getString(15);
				sap_approve_by=rset.getString(16)==null?"":rset.getString(16);
				inv_dt=rset.getString(17)==null?"":rset.getString(17);
				alloc_qty=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
				tcs_tds=rset.getString(19)==null?"":rset.getString(19);
				tcsStructCd=rset.getString(20)==null?"":rset.getString(20);
				tdsStructCd=rset.getString(21)==null?"":rset.getString(21);
				tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23));
				tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24));
				tds_factor=rset.getString(25)==null?"":nf.format(rset.getDouble(25));
				fin_yr=rset.getString(26)==null?"":rset.getString(26);
				invoice_seq=rset.getString(27)==null?"":rset.getString(27);
				taxStructCd=rset.getString(28)==null?"":rset.getString(28);
				gen_type=rset.getString(29)==null?"":rset.getString(29);
				sub_inv_type=rset.getString(31)==null?"":rset.getString(31);
				cash_flow=rset.getString(32)==null?"":rset.getString(32);
				prod_month=rset.getString(33)==null?"":rset.getString(33);
				trans_bu_seq=rset.getString(34)==null?"":rset.getString(34);
				buSeq=rset.getString(35)==null?"":rset.getString(35);
				invComponent=rset.getString(36)==null?"":rset.getString(36);
				
				co_cd=utilBean.getCompanySAPcode(conn, comp_cd);
				deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, "");
				String countpty_category=utilBean.getCounterpartyCategory(conn, counterpty_cd);
				String [] apr_dt= approve_dt.split("/");
				accountingPeriodMonth=apr_dt[1];
				accountingPeriodYear=apr_dt[2];
				//String doc_no=invoice_no.replaceAll("/", "-");
				String doc_no=getSapDocNo(invoice_no);
				extn_mapping=invoice_no+"@";
				
				String [] invDt = inv_dt.split("/");
				String doc_dt = invDt[2]+invDt[1]+invDt[0];
				String postDt=apr_dt[2]+apr_dt[1]+apr_dt[0];
				
				postDt=gen_type.equals("ACCRUAL_REVERSAL")?to_dt.split("/")[2]+to_dt.split("/")[1]+"20":postDt;	//Accural reversal posting date should be 20th of Accounting period 
				
				if(!period_end_dt.equals(""))
				{
					String[] temp_split=period_end_dt.split("/");
					productionPeriodMonth=temp_split[1];
					productionPeriodYear=temp_split[2];		
				}
				monthNm=""+utilDate.getShortMonthName(period_end_dt);
				
				String tempAccount="";
				String itemText="";
				String pk="";
				String sign="";
				if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
				{
					pk="40";
					monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
			    	itemText="Transportation Services "+monthNm+" "+yearNm;
			    	
			    	doc_dt=apr_dt[2]+""+apr_dt[1]+""+apr_dt[0];
			    	
					if(cash_flow.equals("IC"))
			    	{
				    	//INCIDENT#2310112 DIVAY HAS ASKED TO PASS GL CODE 6318400	Pipeline Imbalance  
				    	tempAccount="6318400";
			    	}
					else if(cash_flow.equals("PC"))
			    	{
			    		tempAccount="6320405"; //AS PER VIJAY'S MSG ON TEAMS 20250730
			    	}
					else
			    	{
			    		if(countpty_category.equals("Group"))
				    	{
				    		//tempAccount=account; // Counterparty SAP CODE
				    		tempAccount="6301400";
				    	}
				    	else
				    	{
				    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
				    		//pk="50";
				    		tempAccount="6300400";
				    	}
			    	}
					String trans_type="Transportation Accrual";
					if(gen_type.equals("ACCRUAL_REVERSAL"))
					{
						sign=sign.equals("")?"-":"";
						pk=pk.equals("50")?"40":"50";
						trans_type+=" Reversal";
					}
					
					VCOUNTERPARTY_CD.add(counterpty_cd);
			    	VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
			    	VCO_CD.add(co_cd);
			    	VDEAL_MAP.add(deal_map);
			    	VINVOICE_NO.add(invoice_no);
			    	VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
			    	VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
			    	VPERIOD_START_DT.add(period_start_dt);
			    	VPERIOD_END_DT.add(period_end_dt);
			    	VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
			    	VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
			    	VCURRENCY.add("INR");
			    	VDOC_TYPE.add("X3");		
			    	VPK.add(pk);
			    	VDOC_NO.add(doc_no);
			    	VTRANS_TYPE.add(trans_type);
			    	VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
			    	VDOC_DT.add(doc_dt);
			    	VPOST_DT.add(postDt);
			    	VALLOC_QTY.add(alloc_qty);
			    	qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
			    	VITEMTEXT.add(itemText);
			    	VAMT.add(sign+""+gross_amt);
			    	VUSD_AMT.add("");
			    	String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
			    	VSAP_VALUE_INR.add(sap_val);
			    	String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
			    	VDELTA_SAP_VALUE_INR.add(delta_sap_val);
			    	VSAP_VALUE_USD.add("");
					VDELTA_SAP_VALUE_USD.add("");
					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
					VIS_SAP_DOC_DATA.add("");
					//amtSum+=Double.parseDouble(sign+""+gross_amt);
			    	amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
			    	sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					extn_mapping+=","+tempAccount;
				}
				else
				{
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						net_payable=rset.getString(12)==null?"":nf.format(rset.getDouble(12) * Double.parseDouble(exchangeRate));
						if(gen_type.equals("FFLOW"))
						{
							gross_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30) * Double.parseDouble(exchangeRate));
						}
						else
						{
							gross_amt=rset.getString(11)==null?"":nf.format(rset.getDouble(11) * Double.parseDouble(exchangeRate));
						}
						tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24) * Double.parseDouble(exchangeRate));
					}
					
					if(!gross_amt.equals(""))
				    {
						sign = "";
				    	pk = "40";
				    	if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
				    	{
				    		pk = "50";
				    		sign = "-";
				    	}
				    	else if(invoice_type.equals("LP") && sub_inv_type.equals("CR"))
				    	{
				    		pk = "50";
				    		sign = "-";
				    	}
				    	
				    	tempAccount="";
				    	itemText="Transportation Services";
				    	if(invoice_type.equals("LP") || sub_inv_type.equals("LP"))
				    	{
				    		//tempAccount="8240050"; 20241023
				    		tempAccount="8226000"; //20241023
				    		itemText="Interest";
				    		if(invoice_type.equals("CR"))
				    		{
				    			itemText+=" Reversal";
				    		}
				    	} 
				    	else if(sub_inv_type.equals("IMB"))
				    	{
				    		tempAccount="6318400";
				    		itemText="Imbalance Charge";
				    	}
				    	else if(sub_inv_type.equals("PAR") || invoice_type.equals("PC"))		//PB 20250923: FIX FOR PARKING GL CODE ISSUE AS PER DIVYA'S MAIL 		 
				    	{
				    		tempAccount="6320405";
				    		itemText="Parking Charge";
				    	}
				    	else if(invoice_type.equals("IC"))
				    	{
				    		tempAccount="6318400"; //PIPELINE IMBALANCE GL CODE
				    	}
				    	else
				    	{
				    		tempAccount="6300400"; // NON Group Transmission Charge
				    	}
				    	
				    	VCOUNTERPARTY_CD.add(counterpty_cd);
				    	VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    	VCO_CD.add(co_cd);
				    	VDEAL_MAP.add(deal_map);
				    	VINVOICE_NO.add(invoice_no);
				    	VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				    	VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				    	VPERIOD_START_DT.add(period_start_dt);
				    	VPERIOD_END_DT.add(period_end_dt);
				    	VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
				    	VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
				    	VCURRENCY.add("INR");
				    	VDOC_TYPE.add("X1");		
				    	VPK.add(pk);
				    	VDOC_NO.add(doc_no);
				    	VTRANS_TYPE.add(itemText);
				    	VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
				    	VDOC_DT.add(doc_dt);
				    	VPOST_DT.add(postDt);
				    	VALLOC_QTY.add(alloc_qty);
				    	qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
				    	VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
				    	VAMT.add(sign+""+gross_amt);
				    	VUSD_AMT.add("");
				    	String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
				    	VSAP_VALUE_INR.add(sap_val);
				    	String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
				    	VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    	VSAP_VALUE_USD.add("");
						VDELTA_SAP_VALUE_USD.add("");
						VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						VIS_SAP_DOC_DATA.add("");
						//amtSum+=Double.parseDouble(sign+""+gross_amt);
				    	amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
				    	sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
						deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						extn_mapping+=","+tempAccount;
				    }

					if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
				    {
						String gl_code="";
				    	String TcsTdscode="";
				    	String amt="";
				    	pk="";
				    	String taxBase="";
				    	sign="";
				    	
				    	if(tcs_tds.equals("TCS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
				    		extn_mapping+=","+gl_code;
				    		if(tcs_flag.equals("Y"))
				    		{
				    			TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
				    			amt=tcs_amt;
				    			pk="40";
				    			
				    			if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
				    			{
				    				pk = "50";
				    				sign = "-";
				    			}
				    			else if(invoice_type.equals("LP") && sub_inv_type.equals("CR"))
				    			{
				    				pk = "50";
				    				sign = "-";
				    			}
				    			VCOUNTERPARTY_CD.add(counterpty_cd);
				    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    			VCO_CD.add(co_cd);
				    			VDEAL_MAP.add(deal_map);
				    			VINVOICE_NO.add(invoice_no);
				    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				    			VPERIOD_START_DT.add(period_start_dt);
				    			VPERIOD_END_DT.add(period_end_dt);
				    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
				    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
				    			VCURRENCY.add("INR");
				    			VDOC_TYPE.add("X1");		
				    			VPK.add(pk);
				    			VDOC_NO.add(doc_no);
				    			VTRANS_TYPE.add("Transportation Services");
				    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
				    			VDOC_DT.add(doc_dt);
				    			VPOST_DT.add(postDt);
				    			//VALLOC_QTY.add(alloc_qty);
				    			VALLOC_QTY.add("");
				    			VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"] ");
				    			VAMT.add(sign+""+amt);
				    			VUSD_AMT.add("");
				    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
				    			VSAP_VALUE_INR.add(sap_val);
				    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
				    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    			VSAP_VALUE_USD.add("");
				    			VDELTA_SAP_VALUE_USD.add("");
				    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				    			VIS_SAP_DOC_DATA.add("");
				    			//amtSum+=Double.parseDouble(sign+""+amt);
				    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
				    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
				    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
				    		}
				    	}
				    	else if(tcs_tds.equals("TDS"))
				    	{
			    			gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
			    			extn_mapping+=","+gl_code;
			    			if(tds_flag.equals("Y"))
			    			{
			    				TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
			    				amt=tds_amt;
			    				pk="50";
			    				sign="-";
			    				if(!tds_amt.equals("") && !tds_factor.equals(""))
			    				{
			    					taxBase=nf.format((Double.parseDouble(tds_amt)*100)/Double.parseDouble(tds_factor));
			    				}
			    				
			    				if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
			    				{
			    					pk = "40";
			    					sign = "";
			    				}
			    				else if(invoice_type.equals("LP") && sub_inv_type.equals("CR"))
			    				{
			    					pk = "40";
			    					sign = "";
			    				}
			    				VCOUNTERPARTY_CD.add(counterpty_cd);
			    				VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
			    				VCO_CD.add(co_cd);
			    				VDEAL_MAP.add(deal_map);
			    				VINVOICE_NO.add(invoice_no);
			    				VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
			    				VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
			    				VPERIOD_START_DT.add(period_start_dt);
			    				VPERIOD_END_DT.add(period_end_dt);
			    				VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
			    				VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
			    				VCURRENCY.add("INR");
			    				VDOC_TYPE.add("X1");		
			    				VPK.add(pk);
			    				VDOC_NO.add(doc_no);
			    				VTRANS_TYPE.add("Transportation Services");
			    				VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
			    				VDOC_DT.add(doc_dt);
			    				VPOST_DT.add(postDt);
			    				//VALLOC_QTY.add(alloc_qty);
			    				VALLOC_QTY.add("");
			    				VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"] ");
			    				VAMT.add(sign+""+amt);
			    				VUSD_AMT.add("");
			    				String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
			    				VSAP_VALUE_INR.add(sap_val);
			    				String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
			    				VDELTA_SAP_VALUE_INR.add(delta_sap_val);
			    				VSAP_VALUE_USD.add("");
			    				VDELTA_SAP_VALUE_USD.add("");
			    				VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
			    				VIS_SAP_DOC_DATA.add("");
			    				//amtSum+=Double.parseDouble(sign+""+amt);
			    				amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
			    				sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
			    				deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
			    			}
				    	}
				    }
					//if(tax_flag.equals("Y"))
					{
						String queryString2="";
						if(gen_type.equals("SG"))
						{
							queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
							
						}
						else if(gen_type.equals("PG"))
						{
							queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
						}
						else if(gen_type.equals("FFLOW"))
						{
							queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GTA_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
						}
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, fin_yr);
						stmt2.setString(3, invoice_type);
						stmt2.setString(4, invoice_seq);
						stmt2.setString(5, cont_type);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							String tax_code=rset2.getString(1)==null?"":rset2.getString(1);
							String taxStrctCd=rset2.getString(2)==null?"":rset2.getString(2);
							String taxAmt=rset2.getString(3)==null?"":nf.format(rset2.getDouble(3));
							String taxBaseAmt=rset2.getString(4)==null?"":nf.format(rset2.getDouble(4));
							if(taxBaseAmt.equals(""))
							{
								taxBaseAmt=gross_amt;
							}
							if(!taxAmt.equals(""))
							{
								pk="40";
								sign="";
								if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
								{
									pk = "50";
									sign = "-";
								}
								else if(invoice_type.equals("LP") && sub_inv_type.equals("CR"))
								{
									pk = "50";
									sign = "-";
								}
								String gl_code="";
								String tax_sap_code="";
								String amt=taxAmt;
								gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
								extn_mapping+=","+gl_code;
								if(tax_flag.equals("Y"))
								{
									tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
									
									VCOUNTERPARTY_CD.add(counterpty_cd);
									VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
									VCO_CD.add(co_cd);
									VDEAL_MAP.add(deal_map);
									VINVOICE_NO.add(invoice_no);
									VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
									VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
									VPERIOD_START_DT.add(period_start_dt);
									VPERIOD_END_DT.add(period_end_dt);
									VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
									VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
									VCURRENCY.add("INR");
									VDOC_TYPE.add("X1");		
									VPK.add(pk);
									VDOC_NO.add(doc_no);
									VTRANS_TYPE.add("Transportation Services");
									VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
									VDOC_DT.add(doc_dt);
									VPOST_DT.add(postDt);
									//VALLOC_QTY.add(alloc_qty);
									VALLOC_QTY.add("");
									VITEMTEXT.add("T"+" ["+tax_sap_code+"] ");
									VAMT.add(sign+""+amt);
									VUSD_AMT.add("");
									String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
									VSAP_VALUE_INR.add(sap_val);
									String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
									VDELTA_SAP_VALUE_INR.add(delta_sap_val);
									VSAP_VALUE_USD.add("");
									VDELTA_SAP_VALUE_USD.add("");
									VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
									VIS_SAP_DOC_DATA.add("");
									//amtSum+=Double.parseDouble(sign+""+amt);
									amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
									sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
									deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
								}
							}
						}
						rset2.close();
						stmt2.close();
					}
				}
				if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
				{
					String queryString5="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_GTA_ACCRUAL_TAX_DTL "
							+ "WHERE COMPANY_CD=? "
							//+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND INV_COMPONENT=? ";
					int stmt_ctn=0;
					stmt1=conn.prepareStatement(queryString5);
					stmt1.setString(++stmt_ctn, comp_cd);
					/*if(gen_type.equals("ACCRUAL"))
					{
						stmt1.setString(++stmt_ctn, to_dt);
						stmt1.setString(++stmt_ctn, from_dt);
					}
					else
					{
						stmt1.setString(++stmt_ctn, lastDtOfPrevMonth);
						stmt1.setString(++stmt_ctn, firstDtOfPrevMonth);
					}*/
					stmt1.setString(++stmt_ctn, approve_dt);
					stmt1.setString(++stmt_ctn, period_start_dt);
					stmt1.setString(++stmt_ctn, period_end_dt);
					stmt1.setString(++stmt_ctn, counterpty_cd);
					stmt1.setString(++stmt_ctn, agmt_no);
					stmt1.setString(++stmt_ctn, cont_no);
					stmt1.setString(++stmt_ctn, cont_type);
					stmt1.setString(++stmt_ctn, trans_bu_seq);
					stmt1.setString(++stmt_ctn, buSeq);
					stmt1.setString(++stmt_ctn, fin_yr);
					stmt1.setString(++stmt_ctn, cash_flow);
					stmt1.setString(++stmt_ctn, invComponent);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
						String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
						String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
						String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
						
						if(!taxAmt.equals(""))
					    {
							//if(Double.parseDouble(tax_amt)>0) AS DISCUSSED WITH VIJAY, SEND ZERO TAX 
					    	{
					    		pk="40";
						    	sign="";
						    	
						    	String gl_code="";
						    	String tax_sap_code="";
						    	String amt=taxAmt;
						    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
						    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
						    	
						    	extn_mapping+=","+gl_code;
						    	
						    	if(tax_flag.equals("Y"))
								{
						    		if(Double.parseDouble(amt) > 0)
						    		{
						    			VCOUNTERPARTY_CD.add(counterpty_cd);
						    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
						    			VCO_CD.add(co_cd);
						    			VDEAL_MAP.add(deal_map);
						    			VINVOICE_NO.add(invoice_no);
						    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
						    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
						    			VPERIOD_START_DT.add(period_start_dt);
						    			VPERIOD_END_DT.add(period_end_dt);
						    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
						    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
						    			VCURRENCY.add("INR");
						    			VDOC_TYPE.add("X3");		
						    			VPK.add(pk);
						    			VDOC_NO.add(doc_no);
						    			VTRANS_TYPE.add("Transportation Services");
						    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
						    			VDOC_DT.add(doc_dt);
						    			VPOST_DT.add(postDt);
						    			//VALLOC_QTY.add(alloc_qty);
						    			VALLOC_QTY.add("");
						    			VITEMTEXT.add("T"+" ["+tax_sap_code+"] ");
						    			VAMT.add(sign+""+amt);
						    			VUSD_AMT.add("");
						    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
						    			VSAP_VALUE_INR.add(sap_val);
						    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
						    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
						    			VSAP_VALUE_USD.add("");
						    			VDELTA_SAP_VALUE_USD.add("");
						    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						    			VIS_SAP_DOC_DATA.add("");
						    			//amtSum+=Double.parseDouble(sign+""+amt);
						    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
						    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
						    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						    		}
								}
					    	}
					    }
					}
					rset1.close();
					stmt1.close();
				}
				
				addSAPRecord(counterpty_cd,agmt_no,agmt_rev,"",cont_no,cont_rev,cont_type,"",co_cd,invoice_no,postDt,period_start_dt,period_end_dt,  extn_mapping);
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapGxReconDetails()
	{
		String function_nm="getSapGxReconDetails()";
		try
		{
			int ctn=0;
			String counterpty_cd="";
			String agmt_no="";
			String agmt_rev="";
			String cont_no="";
			String cont_rev="";
			String cont_type="";
			String co_cd="";
			String invoice_no="";
			String approve_dt="";
			String accountingPeriodYear="";
			String accountingPeriodMonth="";
			String period_start_dt="";
			String period_end_dt="";
			String exchangeRate="";
			String netPayable="";
			String gross_amt="";
			String alloc_qty="";
			String tcs_tds="";
			String tcsStructCd="";
			String tcs_amt="";
			String tdsStructCd="";
			String tds_amt="";
			String tax_amt="";
			String inv_raised_in="";
			String tds_factor="";
			String fin_yr="";
			String gen_type="";
			String invoice_dt="";
			String invoice_type="";
			String invoice_seq="";
			String taxStructCd="";
			String taxCode="";
			String tcs_factor="";
			
			String gl_code="";
			String pk="";
			String doc_no="";
			String sap_approve_by="";
			String doc_dt="";
			String postDt="";
			String tax_sap_code="";
			String sign="";
			String amt="";
			String itemText="";
			String prod_month="";
			String gx_counterparty_cd="";
			String gx_bu_seq="";
			String buSeq="";
			
			String extn_mapping="";
			
			String queryString="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT,A.NET_PAYABLE_AMT,A.ALLOC_QTY,A.TCS_TDS,NULL,NULL, " //NULL1: for TCS_STRUCT_CD NULL2: for TCS Amount
					+ "A.TDS_STRUCT_CD,A.TDS_AMT,A.TAX_AMT,A.INVOICE_RAISED_IN,A.TDS_FACTOR,A.SAP_APPROVED_BY,A.FINANCIAL_YEAR,'SG', "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.INVOICE_TYPE,A.INVOICE_SEQ,A.TAX_STRUCT_CD,NULL,NULL,A.GX_COUNTERPARTY_CD, "
					+ "A.GX_BU_UNIT,A.BU_UNIT "
					+ "FROM FMS_GX_TXN_SG_INV_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.SYS_INV_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT,A.NET_PAYABLE_AMT,A.ALLOC_QTY,A.TCS_TDS,NULL,NULL, "  //NULL1: for TCS_STRUCT_CD NULL2: for TCS Amount\r\n"
					+ "A.TDS_STRUCT_CD,A.TDS_AMT,A.TAX_AMT,A.INVOICE_RAISED_IN,A.TDS_FACTOR,A.SAP_APPROVED_BY,A.FINANCIAL_YEAR,'PG', "
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.INVOICE_TYPE,A.INVOICE_SEQ,A.TAX_STRUCT_CD,NULL,NULL,A.GX_COUNTERPARTY_CD, "
					+ "A.GX_BU_UNIT,A.BU_UNIT "
					+ "FROM FMS_GX_TXN_PG_INV_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,  "
					+ "A.INVOICE_NO,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.NET_PAYABLE_AMT,A.ALLOC_QTY,A.TCS_TDS,A.TCS_STRUCT_CD,A.TCS_AMT, "
					+ "A.TDS_STRUCT_CD,A.TDS_AMT,A.TAX_AMT,A.INVOICE_RAISED_IN,NULL,A.SAP_APPROVED_BY,A.FINANCIAL_YEAR,'FFLOW', "    //--NULL FOR TDS_FACTOR_CD 
					+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.INVOICE_TYPE,A.INVOICE_SEQ,A.TAX_STRUCT_CD,A.GROSS_AMT_USD,NULL,A.GX_COUNTERPARTY_CD, "
					+ "A.GX_BU_UNIT,A.BU_UNIT "
					+ "FROM FMS_GX_FFLOW_INV_MST A  "
					+ "WHERE COMPANY_CD=?  "
					+ "AND SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT,NULL,A.ACCRUAL_QTY,NULL,NULL,NULL, "
					+ "NULL,NULL,NULL,A.INVOICE_RAISED_IN,NULL,NULL,A.FINANCIAL_YEAR,'ACCRUAL', "
					+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),NULL,NULL,NULL,NULL,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),A.GX_COUNTERPARTY_CD, "
					+ "A.GX_BU_UNIT,A.BU_UNIT "
					+ "FROM FMS_GX_ACCRUAL_DTL A "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			queryString+="UNION ";
			queryString+="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.XML_NUM,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT,NULL,A.ACCRUAL_QTY,NULL,NULL,NULL, "
					+ "NULL,NULL,NULL,A.INVOICE_RAISED_IN,NULL,NULL,A.FINANCIAL_YEAR,'ACCRUAL_REVERSAL', "
					+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),NULL,NULL,NULL,NULL,TO_CHAR(A.PROD_MONTH,'DD/MM/YYYY'),A.GX_COUNTERPARTY_CD, "
					+ "A.GX_BU_UNIT,A.BU_UNIT "
					+ "FROM FMS_GX_ACCRUAL_DTL A "
					+ "WHERE COMPANY_CD=? "
					+ "AND XML_NUM IS NOT NULL "
					+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, lastDtOfPrevMonth);
			stmt.setString(++ctn, firstDtOfPrevMonth);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				extn_mapping="";
				
				counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
				agmt_no = rset.getString(2)==null?"":rset.getString(2);
				agmt_rev= rset.getString(3)==null?"":rset.getString(3);
				cont_no = rset.getString(4)==null?"":rset.getString(4);
				cont_rev= rset.getString(5)==null?"":rset.getString(5);
				cont_type=rset.getString(6)==null?"":rset.getString(6);
				invoice_no=rset.getString(7)==null?"":rset.getString(7);
				approve_dt=rset.getString(8)==null?"":rset.getString(8);
				exchangeRate=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				netPayable=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				alloc_qty=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				tcs_tds=rset.getString(13)==null?"":rset.getString(13);
				tcsStructCd=rset.getString(14)==null?"":rset.getString(14);
				tcs_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				tdsStructCd=rset.getString(16)==null?"":rset.getString(16);
				tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
				inv_raised_in=rset.getString(19)==null?"":rset.getString(19);
				tds_factor=rset.getString(20)==null?"":rset.getString(20);
				sap_approve_by=rset.getString(21)==null?"":rset.getString(21);
				fin_yr=rset.getString(22)==null?"":rset.getString(22);
				gen_type=rset.getString(23)==null?"":rset.getString(23);
				invoice_dt=rset.getString(24)==null?"":rset.getString(24);
				invoice_type=rset.getString(25)==null?"":rset.getString(25);
				invoice_seq=rset.getString(26)==null?"":rset.getString(26);
				taxStructCd=rset.getString(27)==null?"":rset.getString(27);
				//28 line needs to skip as it is for Gross amount in USD
				prod_month=rset.getString(29)==null?"":rset.getString(29);
				gx_counterparty_cd=rset.getString(30)==null?"":rset.getString(30);
				gx_bu_seq=rset.getString(31)==null?"":rset.getString(31);
				buSeq=rset.getString(32)==null?"":rset.getString(32);
				
				co_cd=utilBean.getCompanySAPcode(conn, comp_cd);
				String deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, "");
				String [] apr_dt = approve_dt.split("/");
				accountingPeriodMonth=apr_dt[1];
				accountingPeriodYear=apr_dt[2];
				String period [] = getContPeriodDetails(counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type).split("--");
				period_start_dt=period[0];
				period_end_dt=period[1];
				String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
				postDt=apr_dt[2]+apr_dt[1]+apr_dt[0];		
				String [] inv_dt=invoice_dt.split("/");
				doc_dt=inv_dt[2]+inv_dt[1]+inv_dt[0];
				taxCode=utilBean.getTaxSAPcode(conn,taxStructCd);
				doc_no=getSapDocNo(invoice_no);
				extn_mapping=invoice_no+"@";
				postDt=gen_type.equals("ACCRUAL_REVERSAL")?to_dt.split("/")[2]+to_dt.split("/")[1]+"20":postDt;	//Accural reversal posting date should be 20th of Accounting period 
				
				if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
				{
					String tempAccount="";
					doc_dt=apr_dt[2]+apr_dt[1]+apr_dt[0];
					pk="40";
					sign="";
					
					if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount="6301400";
			    	}
			    	else
			    	{
			    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
			    		//pk="50"
			    		tempAccount="6300400";
			    	}
			    	
			    	tempAccount="7220300";//Brokerage
					
			    	String monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
			    	
			    	itemText="Brokerage/Commission "+monthNm+" "+yearNm;
			    	
			    	String trans_type="Brokerage/Commission Accrual";
			    	if(gen_type.equals("ACCRUAL_REVERSAL"))
			    	{
			    		pk=pk.equals("50")?"40":"50";
			    		sign=sign.equals("")?"-":"";
			    		trans_type+=" Reversal";
			    	}
			    	
					VCOUNTERPARTY_CD.add(counterpty_cd);
			    	VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
			    	VCO_CD.add(co_cd);
			    	VDEAL_MAP.add(deal_map);
			    	VINVOICE_NO.add(invoice_no);
			    	VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
			    	VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
			    	VPERIOD_START_DT.add(period_start_dt);
			    	VPERIOD_END_DT.add(period_end_dt);
			    	VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
			    	VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
			    	VCURRENCY.add("INR");
			    	VDOC_TYPE.add("X3");		
			    	VPK.add(pk);
			    	VDOC_NO.add(doc_no);	 
			    	VTRANS_TYPE.add(trans_type);
			    	VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
			    	VDOC_DT.add(doc_dt);
			    	VPOST_DT.add(postDt);
			    	VALLOC_QTY.add(alloc_qty);
			    	qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
			    	VITEMTEXT.add(itemText);
			    	VAMT.add(sign+""+gross_amt);
			    	VUSD_AMT.add("");
			    	String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
			    	VSAP_VALUE_INR.add(sap_val);
			    	String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
			    	VDELTA_SAP_VALUE_INR.add(delta_sap_val);
			    	VSAP_VALUE_USD.add("");
					VDELTA_SAP_VALUE_USD.add("");
					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
					VIS_SAP_DOC_DATA.add("");
					//amtSum+=Double.parseDouble(sign+""+gross_amt);
			    	amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
			    	sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					extn_mapping+=","+tempAccount;
				}
				else
				{
					if(gen_type.equals("FFLOW"))
					{
						String queryString2="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? ";
								//+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') ";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, tcsStructCd);
						//stmt2.setString(2, tcsStructDt);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							tcs_factor=rset2.getString(1)==null?"":nf.format(rset2.getDouble(1));
						}
						rset2.close();
						stmt2.close();
						
						queryString2="SELECT FACTOR "
								+ "FROM FMS_TAX_STRUCTURE_DTL A "
								+ "WHERE TAX_STR_CD=? ";
								//+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') ";
						stmt3=conn.prepareStatement(queryString2);
						stmt3.setString(1, tdsStructCd);
						//stmt3.setString(2, tdsStructDt);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							tds_factor=rset3.getString(1)==null?"":nf.format(rset3.getDouble(1));
						}
						rset3.close();
						stmt3.close();
					}
					
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						netPayable=rset.getString(11)==null?"":nf.format(rset.getDouble(11) * Double.parseDouble(exchangeRate));
						if(gen_type.equals("FFLOW"))
						{
							gross_amt=rset.getString(28)==null?"":nf.format(rset.getDouble(28) * Double.parseDouble(exchangeRate));
						}
						else
						{
							gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10) * Double.parseDouble(exchangeRate));
						}
						tcs_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18) * Double.parseDouble(exchangeRate));
					}
					if(!gross_amt.equals(""))
				    {
				    	sign = "";
				    	pk = "40";
				    	if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
				    	{
				    		pk = "50";
				    		sign = "-";
				    	}
				    	
				    	String tempAccount="7220300";
				    	itemText="Brokerage/Commission";
				    	if(invoice_type.equals("LP"))
				    	{
				    		tempAccount="8240050";
				    		itemText="Interest";
				    	}
				    	
				    	VCOUNTERPARTY_CD.add(counterpty_cd);
				    	VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    	VCO_CD.add(co_cd);
				    	VDEAL_MAP.add(deal_map);
				    	VINVOICE_NO.add(invoice_no);
				    	VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				    	VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				    	VPERIOD_START_DT.add(period_start_dt);
				    	VPERIOD_END_DT.add(period_end_dt);
				    	VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
				    	VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
				    	VCURRENCY.add("INR");
				    	VDOC_TYPE.add("X1");		
				    	VPK.add(pk);
				    	VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
				    	VTRANS_TYPE.add(itemText);
				    	VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
				    	VDOC_DT.add(doc_dt);
				    	VPOST_DT.add(postDt);
				    	VALLOC_QTY.add(alloc_qty);
				    	qtySum+=Double.parseDouble(alloc_qty.equals("")?"0.00":alloc_qty);
				    	VITEMTEXT.add(itemText);
				    	VAMT.add(sign+""+gross_amt);
				    	VUSD_AMT.add("");
				    	String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
				    	VSAP_VALUE_INR.add(sap_val);
				    	String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
				    	VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    	VSAP_VALUE_USD.add("");
						VDELTA_SAP_VALUE_USD.add("");
						VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						VIS_SAP_DOC_DATA.add("");
						//amtSum+=Double.parseDouble(sign+""+gross_amt);
				    	amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
				    	sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
						deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						extn_mapping+=","+tempAccount;
				    }
					if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
				    {
						String TcsTdscode="";
						String taxBase="";
						if(tcs_tds.equals("TCS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
				    		extn_mapping+=","+gl_code;
				    		if(tcs_flag.equals("Y"))
				    		{
				    			TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
				    			amt=tcs_amt;
				    			pk="40";
				    			
				    			if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
				    			{
				    				pk = "50";
				    				sign = "-";
				    			}
				    			VCOUNTERPARTY_CD.add(counterpty_cd);
				    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
				    			VCO_CD.add(co_cd);
				    			VDEAL_MAP.add(deal_map);
				    			VINVOICE_NO.add(invoice_no);
				    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				    			VPERIOD_START_DT.add(period_start_dt);
				    			VPERIOD_END_DT.add(period_end_dt);
				    			VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
				    			VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
				    			VCURRENCY.add("INR");
				    			VDOC_TYPE.add("X1");		
				    			VPK.add(pk);
				    			VDOC_NO.add(doc_no);		
				    			VTRANS_TYPE.add("Brokerage/Commission");
				    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
				    			VDOC_DT.add(doc_dt);
				    			VPOST_DT.add(postDt);
				    			//VALLOC_QTY.add(alloc_qty);
				    			VALLOC_QTY.add("");
				    			VITEMTEXT.add(tcs_tds);
				    			VAMT.add(sign+""+amt);
				    			VUSD_AMT.add("");
				    			String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
				    			VSAP_VALUE_INR.add(sap_val);
				    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
				    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
				    			VSAP_VALUE_USD.add("");
				    			VDELTA_SAP_VALUE_USD.add("");
				    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
				    			VIS_SAP_DOC_DATA.add("");
				    			//amtSum+=Double.parseDouble(sign+""+amt);
				    			amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
				    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
				    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
				    		}
				    	}
						else if(tcs_tds.equals("TDS"))
				    	{
							gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
							extn_mapping+=","+gl_code;
							if(tds_flag.equals("Y"))
							{
								TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
								amt=tds_amt;
								pk="50";
								sign="-";
								if(!tds_amt.equals("") && !tds_factor.equals(""))
								{
									taxBase=nf.format((Double.parseDouble(tds_amt)*100)/Double.parseDouble(tds_factor));
								}
								
								if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
								{
									pk = "40";
									sign = "";
								}
								VCOUNTERPARTY_CD.add(counterpty_cd);
								VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
								VCO_CD.add(co_cd);
								VDEAL_MAP.add(deal_map);
								VINVOICE_NO.add(invoice_no);
								VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
								VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
								VPERIOD_START_DT.add(period_start_dt);
								VPERIOD_END_DT.add(period_end_dt);
								VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
								VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
								VCURRENCY.add("INR");
								VDOC_TYPE.add("X1");		
								VPK.add(pk);
								VDOC_NO.add(doc_no);		 
								VTRANS_TYPE.add("Brokerage/Commission");
								VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
								VDOC_DT.add(doc_dt);
								VPOST_DT.add(postDt);
								//VALLOC_QTY.add(alloc_qty);
								VALLOC_QTY.add("");
								VITEMTEXT.add(tcs_tds +" ["+TcsTdscode+"]");
								VAMT.add(sign+""+amt);
								VUSD_AMT.add("");
								String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
								VSAP_VALUE_INR.add(sap_val);
								String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
								VDELTA_SAP_VALUE_INR.add(delta_sap_val);
								VSAP_VALUE_USD.add("");
								VDELTA_SAP_VALUE_USD.add("");
								VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
								VIS_SAP_DOC_DATA.add("");
								//amtSum+=Double.parseDouble(sign+""+amt);
								amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
								sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
								deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							}
				    	}
				    }
					//if(tax_flag.equals("Y"))
					{
						String queryString3="";
						if(gen_type.equals("SG"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GX_TXN_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
						}
						else if(gen_type.equals("PG"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GX_TXN_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? ";
						}
						else if(gen_type.equals("FFLOW"))
						{
							queryString3="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_GX_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND GX_COUNTERPARTY_CD=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
						}
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, fin_yr);
						stmt3.setString(3, gx_counterparty_cd);		
						stmt3.setString(4, invoice_type);
						stmt3.setString(5, invoice_seq);
						stmt3.setString(6, cont_type);
						rset3=stmt3.executeQuery();
						while(rset3.next())
						{
							String tax_code=rset3.getString(1)==null?"":rset3.getString(1);
							String taxStrctCd=rset3.getString(2)==null?"":rset3.getString(2);
							String taxAmt=rset3.getString(3)==null?"":nf.format(rset3.getDouble(3));
							String taxBaseAmt=rset3.getString(4)==null?"":nf.format(rset3.getDouble(4));
							if(taxBaseAmt.equals(""))
							{
								taxBaseAmt=gross_amt;
							}
							if(!taxAmt.equals(""))
							{
								pk="40";
								sign="";
								if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
								{
									pk = "50";
									sign = "-";
								}
								amt=taxAmt;
								gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
								extn_mapping+=","+gl_code;
								if(tax_flag.equals("Y"))
								{
									tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
									VCOUNTERPARTY_CD.add(counterpty_cd);
									VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
									VCO_CD.add(co_cd);
									VDEAL_MAP.add(deal_map);
									VINVOICE_NO.add(invoice_no);
									VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
									VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
									VPERIOD_START_DT.add(period_start_dt);
									VPERIOD_END_DT.add(period_end_dt);
									VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
									VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
									VCURRENCY.add("INR");
									VDOC_TYPE.add("X1");		
									VPK.add(pk);
									VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
									VTRANS_TYPE.add("Brokerage/Commission");
									VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
									VDOC_DT.add(doc_dt);
									VPOST_DT.add(postDt);
									//VALLOC_QTY.add(alloc_qty);
									VALLOC_QTY.add("");
									VITEMTEXT.add("T"+" ["+tax_sap_code+"]");
									VAMT.add(sign+""+amt);
									VUSD_AMT.add("");
									String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
									VSAP_VALUE_INR.add(sap_val);
									String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
									VDELTA_SAP_VALUE_INR.add(delta_sap_val);
									VSAP_VALUE_USD.add("");
									VDELTA_SAP_VALUE_USD.add("");
									VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
									VIS_SAP_DOC_DATA.add("");
									//amtSum+=Double.parseDouble(sign+""+amt)
									amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
									sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
									deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
								}
							}
						}
						rset3.close();
						stmt3.close();
					}
				}
				if(gen_type.equals("ACCRUAL") || gen_type.equals("ACCRUAL_REVERSAL"))
				{
					String queryString5="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_GX_ACCRUAL_TAX_DTL "
							+ "WHERE COMPANY_CD=? "
							//+ "AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')  "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND GX_BU_UNIT=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND GX_COUNTERPARTY_CD=?";
					int stmt_ctn=0;
					stmt1=conn.prepareStatement(queryString5);
					stmt1.setString(++stmt_ctn, comp_cd);
					/*if(gen_type.equals("ACCRUAL"))
					{
						stmt1.setString(++stmt_ctn, to_dt);
						stmt1.setString(++stmt_ctn, from_dt);
					}
					else
					{
						stmt1.setString(++stmt_ctn, lastDtOfPrevMonth);
						stmt1.setString(++stmt_ctn, firstDtOfPrevMonth);
					}*/
					stmt1.setString(++stmt_ctn, approve_dt);
					stmt1.setString(++stmt_ctn, counterpty_cd);
					stmt1.setString(++stmt_ctn, agmt_no);
					stmt1.setString(++stmt_ctn, cont_no);
					stmt1.setString(++stmt_ctn, cont_type);
					stmt1.setString(++stmt_ctn, gx_bu_seq);
					stmt1.setString(++stmt_ctn, buSeq);
					stmt1.setString(++stmt_ctn, fin_yr);
					stmt1.setString(++stmt_ctn, gx_counterparty_cd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
						String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
						String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
						String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
						
						if(!taxAmt.equals(""))
					    {
							//if(Double.parseDouble(tax_amt)>0) AS DISCUSSED WITH VIJAY, SEND ZERO TAX 
					    	{
					    		pk="40";
						    	sign="";
						    	
						    	gl_code="";
						    	tax_sap_code="";
						    	amt=taxAmt;
						    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
						    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
						    	extn_mapping+=","+gl_code;
								if(tax_flag.equals("Y"))
								{
									if(Double.parseDouble(amt) > 0)
									{
										VCOUNTERPARTY_CD.add(counterpty_cd);
										VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
										VCO_CD.add(co_cd);
										VDEAL_MAP.add(deal_map);
										VINVOICE_NO.add(invoice_no);
										VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
										VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
										VPERIOD_START_DT.add(period_start_dt);
										VPERIOD_END_DT.add(period_end_dt);
										VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
										VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
										VCURRENCY.add("INR");
										VDOC_TYPE.add("X3");		
										VPK.add(pk);
										VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
										VTRANS_TYPE.add("Brokerage/Commission");
										VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
										VDOC_DT.add(doc_dt);
										VPOST_DT.add(postDt);
										//VALLOC_QTY.add(alloc_qty);
										VALLOC_QTY.add("");
										VITEMTEXT.add("T"+" ["+tax_sap_code+"]");
										VAMT.add(sign+""+amt);
										VUSD_AMT.add("");
										String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
										VSAP_VALUE_INR.add(sap_val);
										String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
										VDELTA_SAP_VALUE_INR.add(delta_sap_val);
										VSAP_VALUE_USD.add("");
										VDELTA_SAP_VALUE_USD.add("");
										VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
										VIS_SAP_DOC_DATA.add("");
										//amtSum+=Double.parseDouble(sign+""+amt)
										amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
										sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
										deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
									}
								}
					    	}
					    }
					}
					rset1.close();
					stmt1.close();
				}
				
				addSAPRecord(counterpty_cd,agmt_no,agmt_rev,"",cont_no,cont_rev,cont_type,"",co_cd,invoice_no,postDt,period_start_dt,period_end_dt,  extn_mapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//PB 20250722: for generating the derivative Recon Report
	public void getSapDervReconDetails()
	{
		String function_nm="getSapDervReconDetails()";
		try
		{
			//Since the derivative type contains both Invoice and remittance 
			VDERV_INV_TYPE.add("I");
			VDERV_INV_TYPE.add("R");
			
			//for(int i=0;i<VDERV_INV_TYPE.size();i++)
			{
				String counterpty_cd="";
				String agmt_no="";
				String cont_no="";
				String cont_type="";
				String instrument_no="";
				String invoice_no="";
				String inv_seq="";
				String invoice_ref="";
				String invoice_dt="";
				String inv_type="";
				String periodStartDt="";
				String periodEndDt="";
				String bu_unit="";
				String net_payable_amt="";
				String alloc_qty="";
				String invoice_amt="";
				String inv_raised_in="";
				String fin_yr="";
				String sap_approved_by="";
				String sap_approved_dt="";
				String bu_state_tin="";
				String cashflow="";
				String gen_type="";
				
				String extn_mapping="";
				
				int ctn=0;
				String queryString1="SELECT A.COUNTERPARTY_CD, A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,A.INSTRUMENT_NO,A.INVOICE_NO,A.INVOICE_SEQ, A.INVOICE_REF_NO, TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.INV_TYPE, "
						+ "TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),A.BU_UNIT,A.NET_PAYABLE_AMT,A.ALLOC_QTY,A.INVOICE_AMT,A.INVOICE_RAISED_IN,A.FINANCIAL_YEAR, "
						+ "A.SAP_APPROVED_BY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),BU_STATE_TIN,NULL,'DERV' "
						+ "FROM FMS_DERV_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
						//+ "AND INV_TYPE=? "
						+ "AND SAP_APPROVAL=? "
						+ "AND FIN_SYS IS NULL ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+="AND A.COUNTERPARTY_CD=? ";
				}
				queryString1+=" UNION ";
				queryString1+="SELECT  A.COUNTERPARTY_CD, A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,A.INSTRUMENT_NO,A.XML_NUM,A.XML_SEQ, NULL,"
						+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.INV_TYPE, TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "A.BU_UNIT,A.ACCRUAL_AMT,A.ACCRUAL_QTY,A.GROSS_AMT,A.INVOICE_RAISED_IN,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.BU_STATE_TIN,A.CASH_FLOW,'DERV_ACRUAL' "
						+ "FROM FMS_DERV_ACCRUAL_DTL A "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.XML_NUM IS NOT NULL "
						+ "AND IS_MTM NOT IN ('Y') "
						+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
						//+ "AND INV_TYPE=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+="AND A.COUNTERPARTY_CD=? ";
				}
				//for accrual reversal
				queryString1+=" UNION ";
				queryString1+="SELECT  A.COUNTERPARTY_CD, A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,A.INSTRUMENT_NO,A.XML_NUM,A.XML_SEQ, NULL,"
						+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.INV_TYPE, TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "A.BU_UNIT,A.ACCRUAL_AMT,A.ACCRUAL_QTY,A.GROSS_AMT,A.INVOICE_RAISED_IN,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.BU_STATE_TIN,A.CASH_FLOW,'DERV_ACRUAL_REVERSAL' "
						+ "FROM FMS_DERV_ACCRUAL_DTL A "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.XML_NUM IS NOT NULL "
						+ "AND IS_MTM NOT IN ('Y') "
						+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
				//+ "AND INV_TYPE=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+="AND A.COUNTERPARTY_CD=? ";
				}
				//for MTM Report
				queryString1+=" UNION ";
				queryString1+="SELECT  A.COUNTERPARTY_CD, A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,A.INSTRUMENT_NO,A.XML_NUM,A.XML_SEQ, NULL,"
						+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.INV_TYPE, TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "A.BU_UNIT,A.ACCRUAL_AMT,A.ACCRUAL_QTY,A.GROSS_AMT,A.INVOICE_RAISED_IN,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.BU_STATE_TIN,A.CASH_FLOW,'MTM_DERV_ACRUAL' "
						+ "FROM FMS_DERV_ACCRUAL_DTL A "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.XML_NUM IS NOT NULL "
						+ "AND IS_MTM  IN ('Y') "
						+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
				//+ "AND INV_TYPE=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+="AND A.COUNTERPARTY_CD=? ";
				}
				//for MTM Reversal
				queryString1+=" UNION ";
				queryString1+="SELECT  A.COUNTERPARTY_CD, A.AGMT_NO,A.CONT_NO,A.CONTRACT_TYPE,A.INSTRUMENT_NO,A.XML_NUM,A.XML_SEQ, NULL,"
						+ "TO_CHAR(A.INVOICE_DUE_DT,'DD/MM/YYYY'),A.INV_TYPE, TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "A.BU_UNIT,A.ACCRUAL_AMT,A.ACCRUAL_QTY,A.GROSS_AMT,A.INVOICE_RAISED_IN,A.FINANCIAL_YEAR,NULL,TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),A.BU_STATE_TIN,A.CASH_FLOW,'MTM_DERV_ACRUAL_REVERSAL' "
						+ "FROM FMS_DERV_ACCRUAL_DTL A "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.XML_NUM IS NOT NULL "
						+ "AND IS_MTM  IN ('Y') "
						+ "AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.REPORT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
				//+ "AND INV_TYPE=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+="AND A.COUNTERPARTY_CD=? ";
				}
				stmt=conn.prepareStatement(queryString1);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				//stmt.setString(++ctn, ""+VDERV_INV_TYPE.elementAt(i));
				stmt.setString(++ctn, "Y");
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn, counterparty_cd);
				}
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn, counterparty_cd);
				}
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn, counterparty_cd);
				}
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, from_dt);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn, counterparty_cd);
				}
				//for MTM reversal
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, lastDtOfPrevMonth);
				stmt.setString(++ctn, firstDtOfPrevMonth);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(++ctn, counterparty_cd);
				}
				
				//stmt.setString(++ctn, ""+VDERV_INV_TYPE.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					extn_mapping="";
					
					counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
					agmt_no=rset.getString(2)==null?"":rset.getString(2);
					cont_no=rset.getString(3)==null?"":rset.getString(3);
					cont_type=rset.getString(4)==null?"":rset.getString(4);
					instrument_no=rset.getString(5)==null?"":rset.getString(5);
					invoice_no=rset.getString(6)==null?"":rset.getString(6);
					inv_seq=rset.getString(7)==null?"":rset.getString(7);
					invoice_ref=rset.getString(8)==null?"":rset.getString(8);
					invoice_dt=rset.getString(9)==null?"":rset.getString(9);
					inv_type=rset.getString(10)==null?"":rset.getString(10);
					periodStartDt=rset.getString(11)==null?"":rset.getString(11);
					periodEndDt=rset.getString(12)==null?"":rset.getString(12);
					bu_unit=rset.getString(13)==null?"":rset.getString(13);
					net_payable_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
					alloc_qty=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
					invoice_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					inv_raised_in=rset.getString(17)==null?"":rset.getString(17);
					fin_yr=rset.getString(18)==null?"":rset.getString(18);
					sap_approved_by=rset.getString(19)==null?"":rset.getString(19);
					sap_approved_dt=rset.getString(20)==null?"":rset.getString(20);
					bu_state_tin=rset.getString(21)==null?"":rset.getString(21);
					cashflow=rset.getString(22)==null?"":rset.getString(22);
					gen_type=rset.getString(23)==null?"":rset.getString(23);
					
					extn_mapping+=invoice_no+"@";
					
					String co_cd=utilBean.getCompanySAPcode(conn, comp_cd);
					String deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, "", cont_no, "", cont_type, instrument_no);
					String accountingPeriodYear = sap_approved_dt.split("/")[2];
					String accountingPeriodMonth=sap_approved_dt.split("/")[1];
					String tempAccount="6841000";
					if(gen_type.equals("MTM_DERV_ACRUAL") || gen_type.equals("MTM_DERV_ACRUAL_REVERSAL"))
					{
						tempAccount="6815000";
					}
					//tempAccount2="6841000";
					String doc_no=getSapDocNo(invoice_no);
					String doc_dt=invoice_dt.split("/")[2]+invoice_dt.split("/")[1]+invoice_dt.split("/")[0];
					String postDt=sap_approved_dt.split("/")[2]+sap_approved_dt.split("/")[1]+sap_approved_dt.split("/")[0];
					String itemText="Swaps IG";
					String monthNm=""+utilDate.getShortMonthName(periodEndDt);
					String productionPeriodYear=periodEndDt.split("/")[2];
					
					postDt=gen_type.equals("DERV_ACRUAL_REVERSAL")||gen_type.equals("MTM_DERV_ACRUAL_REVERSAL")?to_dt.split("/")[2]+to_dt.split("/")[1]+"20":postDt;	//Accural reversal posting date should be 20th of Accounting period 
					
					String cont_ref="";
					String queryString2="SELECT A.CONT_REF_NO "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterpty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, instrument_no);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						cont_ref=rset2.getString(1)==null?"0":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					
					if(!net_payable_amt.equals(""))
					{
						String sign="-";
						String pk = "50";
				    	String net_payable=""+net_payable_amt;
				    	if(Double.parseDouble(net_payable)<0)
				    	{
				    		pk = "40";
				    		sign = "";
				    		net_payable = net_payable.replace("-", "");
				    	}
				    	
						VCOUNTERPARTY_CD.add(counterpty_cd);
						VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
						VCO_CD.add(co_cd);
						
						VDEAL_MAP.add(deal_map);
						VINVOICE_NO.add(invoice_no);
						VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
						VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
						VPERIOD_START_DT.add(periodStartDt);
						VPERIOD_END_DT.add(periodEndDt);
						VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
						VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
						VCURRENCY.add("USD");
						VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						VIS_SAP_DOC_DATA.add("");
						if(gen_type.equals("DERV"))
						{
							VDOC_TYPE.add(inv_type.equals("R")?"X1":"X2");		
							VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear+" ("+cont_ref+")");
							VUSD_AMT.add(sign+""+net_payable);
							VTRANS_TYPE.add("Commodity");
							String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"2");
							String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(net_payable.equals("")?"0":sign+""+net_payable) - Double.parseDouble(sap_val));
							VSAP_VALUE_USD.add(sap_val);
							VDELTA_SAP_VALUE_USD.add(delta_sap_val);
							//amtSum+=Double.parseDouble(sign+""+amt)
							amtUsdSum+=Double.parseDouble(net_payable.equals("")?"0":sign+""+net_payable);
							sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						}
						else
						{
							String trans_type = "Commodity Accrual";
							itemText="Swaps & CFDs";
							if(gen_type.equals("MTM_DERV_ACRUAL") || gen_type.equals("MTM_DERV_ACRUAL_REVERSAL"))
							{
								itemText="M2M Swaps/CFDs";
								trans_type = "Commodity MTM Accrual";
								VDOC_TYPE.add("X5");
							}
							else
							{
								VDOC_TYPE.add(inv_type.equals("R")?"X3":"X4");
							}
							if(inv_type.equals("I"))
							{
						    	sign = "-";
						    	pk="50";
							}
				    		else
				    		{
						    	sign = "";
						    	pk="40";
				    		}
							
							if(!invoice_amt.equals(""))
							{
								if(Double.parseDouble(invoice_amt)<0)
								{
									invoice_amt=nf.format(Double.parseDouble(invoice_amt)*-1);
								}
							}
							if(gen_type.equals("DERV_ACRUAL_REVERSAL") || gen_type.equals("MTM_DERV_ACRUAL_REVERSAL"))
					    	{
					    		pk=pk.equals("50")?"40":"50";
					    		sign=sign.equals("")?"-":"";
					    		trans_type+=" Reversal";
					    	}
							
							VITEMTEXT.add(itemText);
							VTRANS_TYPE.add(trans_type);
							VUSD_AMT.add(sign+""+invoice_amt);
							String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"2");
							String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(invoice_amt.equals("")?"0":sign+""+invoice_amt) - Double.parseDouble(sap_val));
							VSAP_VALUE_USD.add(sap_val);
							VDELTA_SAP_VALUE_USD.add(delta_sap_val);
							//amtSum+=Double.parseDouble(sign+""+amt)
							amtUsdSum+=Double.parseDouble(net_payable.equals("")?"0":sign+""+net_payable);
							sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
						}
						VPK.add(pk);
						VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
						VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approved_by).equals("")?"System":utilBean.getEmpName(conn, sap_approved_by));
						VDOC_DT.add(doc_dt);
						VPOST_DT.add(postDt);
						VALLOC_QTY.add(alloc_qty);
						qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
						//VALLOC_QTY.add("");
						VAMT.add("");
						VSAP_VALUE_INR.add("");
						VDELTA_SAP_VALUE_INR.add("");
						extn_mapping+=","+tempAccount;
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
	
	//PB 20250822: for generating the DLNG Recon report  
	public void getSapDLNGReconDetails()
	{
		String function_nm="getSapDLNGReconDetails()";
		try
		{
			int ctn=0;
			String counterpty_cd="";
			String invoice_no="";
			String agmt_no="";
			String agmt_rev="";
			String cont_no="";
			String cont_rev="";
			String cont_type="";
			String bu_state_tin="";
			String inv_seq="";
			String fin_year="";
			String periodStartDt="";
			String periodEndDt="";
			String gross_amt="";
			String net_payable_amt="";
			String inv_raised_in="";
			String exchng_rate_value="";
			String gen_type="";
			String tax_struct_cd="";
			String tcs_tds="";
			String tcs_struct_cd="";
			String tds_struct_cd="";
			String tcs_amt="";
			String tds_amt="";
			String tax_amt="";
			String alloc_qty="";
			String sap_approve_dt="";
			String sap_approve_by="";
			String invoice_dt="";
			String cash_flow="";
			String gross_amt_usd="";
			String invoice_type="";
			String invoice_category="";
			String sub_inv_type="";
			String inv_flag="";
			String qty_unit="";
			
			String extn_mapping="";
			
			//DLNG SG Invoice
			String queryString="SELECT  A.COUNTERPARTY_CD,A.INVOICE_NO,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_STATE_TIN,A.INVOICE_SEQ, A.FINANCIAL_YEAR,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.NET_PAYABLE_AMT, A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,'DLNG_INV',A.TAX_STRUCT_CD,A.TCS_TDS,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD, "
					+ "A.TCS_AMT,A.TDS_GROSS_AMT,A.TAX_AMT,A.ALLOC_QTY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "NULL,NULL,NULL,NULL,A.INV_FLAG,NULL "
					+ "FROM FMS_DLNG_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//dlng fflow inVOICE
			queryString+=" UNION ";
			queryString+="SELECT  A.COUNTERPARTY_CD,A.INVOICE_NO,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,  "
					+ "A.BU_STATE_TIN,A.INVOICE_SEQ, A.FINANCIAL_YEAR,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),  "
					+ "A.GROSS_AMT_INR,A.NET_PAYABLE_AMT, A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,'DLNG_FFLOW_INV',A.TAX_STRUCT_CD,A.TCS_TDS,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD,  "
					+ "A.TCS_AMT,A.TDS_GROSS_AMT,A.TAX_AMT,A.ALLOC_QTY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT_USD,A.INVOICE_TYPE,A.INVOICE_CATEGORY,A.SUB_INV_TYPE,NULL,NULL  "
					+ "FROM FMS_DLNG_FFLOW_INV_MST A  "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=?  "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			//dlng service inVOICE
			queryString+=" UNION ";
			queryString+="SELECT  A.COUNTERPARTY_CD,A.INVOICE_NO,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE, "
					+ "A.BU_STATE_TIN,A.INVOICE_SEQ, A.FINANCIAL_YEAR,TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.NET_PAYABLE_AMT, A.INVOICE_RAISED_IN,A.EXCHG_RATE_VALUE,'DLNG_SVC_INV',A.TAX_STRUCT_CD,A.TCS_TDS,A.TCS_STRUCT_CD,A.TDS_STRUCT_CD, "
					+ "A.TCS_AMT,A.TDS_GROSS_AMT,A.TAX_AMT,A.QTY,TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),A.SAP_APPROVED_BY,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "NULL,NULL,NULL,NULL,A.INV_FLAG,A.QTY_UNIT "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? "
					+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.FIN_SYS IS NULL ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(++ctn, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				extn_mapping="";
				
				counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				invoice_no=rset.getString(2)==null?"":rset.getString(2);
				agmt_no=rset.getString(3)==null?"":rset.getString(3);
				agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				cont_rev=rset.getString(6)==null?"":rset.getString(6);
				cont_type=rset.getString(7)==null?"":rset.getString(7);
				bu_state_tin=rset.getString(8)==null?"":rset.getString(8);
				inv_seq=rset.getString(9)==null?"":rset.getString(9);
				fin_year=rset.getString(10)==null?"":rset.getString(10);
				periodStartDt=rset.getString(11)==null?"":rset.getString(11);
				periodEndDt=rset.getString(12)==null?"":rset.getString(12);
				gross_amt=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				net_payable_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				inv_raised_in=rset.getString(15)==null?"":rset.getString(15);
				exchng_rate_value=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				gen_type=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_cd=rset.getString(18)==null?"":rset.getString(18);
				tcs_tds=rset.getString(19)==null?"":rset.getString(19);
				tcs_struct_cd=rset.getString(20)==null?"":rset.getString(20);
				tds_struct_cd=rset.getString(21)==null?"":rset.getString(21);
				tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23));
				tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24));
				alloc_qty=rset.getString(25)==null?"":nf.format(rset.getDouble(25));
				sap_approve_dt=rset.getString(26)==null?"":rset.getString(26);
				sap_approve_by=rset.getString(27)==null?"":rset.getString(27);
				invoice_dt=rset.getString(28)==null?"":rset.getString(28);
				gross_amt_usd=rset.getString(29)==null?"":nf.format(rset.getDouble(29));
				invoice_type=rset.getString(30)==null?"":rset.getString(30);
				invoice_category=rset.getString(31)==null?"":rset.getString(31);
				sub_inv_type=rset.getString(32)==null?"":rset.getString(32);
				inv_flag=rset.getString(33)==null?"":rset.getString(33);
				qty_unit=rset.getString(34)==null?"":rset.getString(34);

				if(gen_type.equals("DLNG_SVC_INV"))
				{
					//AS Per Vijay's Feedback mail on 19/11/2025 : For Lump sum qty should be zero
					alloc_qty=rset.getString(25)==null?"":qty_unit.equals("0")?"0":nf.format(rset.getDouble(25));
				}
				
				String co_cd=utilBean.getCompanySAPcode(conn, comp_cd);
				String deal_map=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, "");
				String accountingPeriodYear = sap_approve_dt.split("/")[2];
				String accountingPeriodMonth=sap_approve_dt.split("/")[1];
				String monthNm=""+utilDate.getShortMonthName(periodEndDt);
				String productionPeriodMonth=periodEndDt.split("/")[1];
				String productionPeriodYear=periodEndDt.split("/")[2];	
				String postDt=sap_approve_dt.split("/")[2]+sap_approve_dt.split("/")[1]+sap_approve_dt.split("/")[0];
				String doc_no=getSapDocNo(invoice_no);
				String doc_dt=invoice_dt.split("/")[2]+invoice_dt.split("/")[1]+invoice_dt.split("/")[0];
				
				extn_mapping+=invoice_no+"@";
				
				String sign = "-";
		    	String pk = "50";
				String tempAccount="";
		    	String itemText="";
		    	String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
		    	
		    	if(inv_flag.equals("CR") || inv_flag.equals("DR"))
		    	{
		    		if(inv_raised_in.equals("2") && !exchng_rate_value.equals(""))
					{
		    			net_payable_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14) * Double.parseDouble(exchng_rate_value));
						
		    			gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9) * Double.parseDouble(exchng_rate_value));
						
		    			tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22) * Double.parseDouble(exchng_rate_value));
		    			tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23) * Double.parseDouble(exchng_rate_value));
		    			tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24) * Double.parseDouble(exchng_rate_value));
					} 
		    		
		    		if(inv_flag.equals("CR"))
					{
						cash_flow="Credit Note";
					}
					else if(inv_flag.equals("DR"))
					{
						cash_flow="Debit Note";
					}
		    		
		    		if(!gross_amt.equals(""))
				    {
		    			sign = "-";
				    	pk = "50";
				    	
				    	if(Double.parseDouble(gross_amt) < 0)
				    	{
				    		pk = "40";
				    		sign = "";
				    		gross_amt=nf.format(Math.abs(Double.parseDouble(gross_amt)));
				    	}
				    	
				    	tempAccount="";
				    	itemText="";
				    	if(invoice_type.equals("LP") || inv_flag.equals("LP"))
				    	{
				    		tempAccount="8240050";
				    		itemText="Interest";
				    	}
				    	else 
				    	{
				    		if(sub_inv_type.equals("IMB"))
					    	{
					    		tempAccount="6318400";
					    		itemText=cash_flow;
					    	}
				    		else if(inv_flag.equals("ST")) //FOR STORAGE
					    	{
					    		//tempAccount="6320405";
					    		
					    		//AS PER VIJAY'S MAIL ON 25/06/2025
					    		if(countpty_category.equals("Group"))
						    	{
						    		tempAccount="6851550";
						    	}
						    	else
						    	{
						    		tempAccount="6850550";
						    	}
						    	itemText="LNG Storage Charge";
					    	}
				    		else if(cont_type.equals("O") || cont_type.equals("Q"))
					    	{
					    		if(countpty_category.equals("Group"))
						    	{
						    		tempAccount="6000450";
						    	}
						    	else
						    	{
						    		tempAccount="6000500";
						    	}
					    		itemText=cash_flow;
					    	}
				    		else if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
					    	{
					    		//tempAccount="7220300";
					    		//tempAccount="6000400";
					    		//itemText="Brokerage/Commission";
					    		if(countpty_category.equals("Group"))
						    	{
						    		tempAccount="6001400";
						    	}
						    	else
						    	{
						    		tempAccount="6000400";
						    	}
					    		//itemText="Commodity";
					    		itemText=cash_flow;
					    	}
				    		else 
					    	{
					    		if(countpty_category.equals("Group"))
						    	{
						    		tempAccount="6001400";
						    		itemText=cash_flow;
						    	}
						    	else
						    	{
						    		tempAccount="6000400";
						    		itemText=cash_flow;
						    	}
					    	}
				    	}
				    	
				    	VCOUNTERPARTY_CD.add(counterpty_cd);
		    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    			VCO_CD.add(co_cd);
		    			VDEAL_MAP.add(deal_map);
		    			VINVOICE_NO.add(invoice_no);
		    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    			VPERIOD_START_DT.add(periodStartDt);
		    			VPERIOD_END_DT.add(periodEndDt);
		    			VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
		    			VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
		    			VCURRENCY.add("INR");
		    			VDOC_TYPE.add("X2");
		    			if(gen_type.equals("DLNG_SVC_INV"))
		    			{
		    				String prodPeriodYear=productionPeriodYear.substring(2);
		    				VITEMTEXT.add(itemText+" "+monthNm+" "+prodPeriodYear);
		    			}
		    			else
		    			{
		    				VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
		    			}
		    			VTRANS_TYPE.add(cash_flow);
		    			VPK.add(pk);
		    			VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    			VDOC_DT.add(doc_dt);
		    			VPOST_DT.add(postDt);
		    			VALLOC_QTY.add(alloc_qty);
		    			VAMT.add(sign+""+gross_amt);
		    			VUSD_AMT.add("");
		    			qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    			String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
		    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
		    			VSAP_VALUE_USD.add("");
		    			VDELTA_SAP_VALUE_USD.add("");
		    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    			VIS_SAP_DOC_DATA.add("");
		    			amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
		    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    			VSAP_VALUE_INR.add(sap_val);
		    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    			extn_mapping+=","+tempAccount;
				    	
				    }
		    		
		    		int stcount=0;
				    
			    	String queryString1="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_DLNG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=?";
				    stmt1 = conn.prepareStatement(queryString1);
			    	stmt1.setString(++stcount, comp_cd);
					stmt1.setString(++stcount, fin_year);
					stmt1.setString(++stcount, inv_seq);
					stmt1.setString(++stcount, bu_state_tin);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
						String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
						String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
						String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
						
						if(!taxAmt.equals(""))
						{
							sign = "-";
					    	pk = "50";
					    	if(Double.parseDouble(taxAmt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		taxAmt=nf.format(Math.abs(Double.parseDouble(taxAmt)));
					    	}
					    	
					    	String gl_code="";
					    	String tax_sap_code="";
					    	String amt=taxAmt;
					    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
					    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
					    	
					    	if(Double.parseDouble(amt) > 0)
					    	{
	    						extn_mapping+=","+gl_code;
	    						if(tax_flag.equals("Y"))
	    						{
	    							tax_sap_code=utilBean.getTaxSAPcode(conn,tax_struct_cd, tax_code);
	    							VCOUNTERPARTY_CD.add(counterpty_cd);
	    							VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
	    							VCO_CD.add(co_cd);
	    							VDEAL_MAP.add(deal_map);
	    							VINVOICE_NO.add(invoice_no);
	    							VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
	    							VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
	    							VPERIOD_START_DT.add(periodStartDt);
	    							VPERIOD_END_DT.add(periodEndDt);
	    							VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
	    							VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
	    							VCURRENCY.add("INR");
	    							VDOC_TYPE.add("X2");		
	    							VITEMTEXT.add("T "+" ["+tax_sap_code+"]");
	    							VTRANS_TYPE.add(cash_flow);
	    							VPK.add(pk);
	    							VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
	    							VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
	    							VDOC_DT.add(doc_dt);
	    							VPOST_DT.add(postDt);
	    							VALLOC_QTY.add("");
	    							//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
	    							VAMT.add(sign+""+amt);
	    							VUSD_AMT.add("");
	    							String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
	    							String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
	    							VSAP_VALUE_USD.add("");
	    							VDELTA_SAP_VALUE_USD.add("");
	    							VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
	    							VIS_SAP_DOC_DATA.add("");
	    							//amtSum+=Double.parseDouble(sign+""+amt)
	    							amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
	    							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
	    							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
	    							VSAP_VALUE_INR.add(sap_val);
	    							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
	    						}
					    	}
						}
					}
		    		
					if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
				    {
						String gl_code="";
				    	String TcsTdscode="";
				    	String amt="";
				    	pk="";
				    	String taxBase="";
				    	sign="";
				    	
				    	if(tcs_tds.equals("TCS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tcs_struct_cd);
				    		TcsTdscode=utilBean.getTaxSAPcode(conn,tcs_struct_cd);
				    		amt=tcs_amt;
				    		
				    		sign = "-";
					    	pk = "50";
					    	if(Double.parseDouble(tcs_amt) < 0)
					    	{
					    		pk = "40";
					    		sign = "";
					    		tcs_amt=nf.format(Math.abs(Double.parseDouble(tcs_amt)));
					    	}
					    	
					    	extn_mapping+=","+gl_code;
		    				if(tcs_flag.equals("Y"))
		    				{
		    					VCOUNTERPARTY_CD.add(counterpty_cd);
		    					VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    					VCO_CD.add(co_cd);
		    					VDEAL_MAP.add(deal_map);
		    					VINVOICE_NO.add(invoice_no);
		    					VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    					VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    					VPERIOD_START_DT.add(periodStartDt);
		    					VPERIOD_END_DT.add(periodEndDt);
		    					VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
		    					VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
		    					VCURRENCY.add("INR");
		    					VDOC_TYPE.add("X2");		
		    					VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
		    					VTRANS_TYPE.add(cash_flow);
		    					VPK.add(pk);
		    					VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    					VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    					VDOC_DT.add(doc_dt);
		    					VPOST_DT.add(postDt);
		    					VALLOC_QTY.add("");
		    					//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    					VAMT.add(sign+""+amt);
		    					VUSD_AMT.add("");
		    					String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
		    					String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
		    					VSAP_VALUE_USD.add("");
		    					VDELTA_SAP_VALUE_USD.add("");
		    					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    					VIS_SAP_DOC_DATA.add("");
		    					//amtSum+=Double.parseDouble(sign+""+amt)
		    					amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
		    					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    					VSAP_VALUE_INR.add(sap_val);
		    					VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    				}
				    	}
				    	else if(tcs_tds.equals("TDS"))
				    	{
				    		gl_code=utilBean.getTaxGLcode(conn,tds_struct_cd);
				    		TcsTdscode=utilBean.getTaxSAPcode(conn,tds_struct_cd);
				    		amt=tds_amt;
				    	
				    		pk="40";
				    		//sign = "";
				    		if(inv_flag.equals("CR"))
					    	{
					    		pk = "50";
					    		//sign = "-";
					    	}
				    		extn_mapping+=","+gl_code;
		    				if(tds_flag.equals("Y"))
		    				{
		    					VCOUNTERPARTY_CD.add(counterpty_cd);
		    					VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    					VCO_CD.add(co_cd);
		    					VDEAL_MAP.add(deal_map);
		    					VINVOICE_NO.add(invoice_no);
		    					VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    					VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    					VPERIOD_START_DT.add(periodStartDt);
		    					VPERIOD_END_DT.add(periodEndDt);
		    					VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
		    					VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
		    					VCURRENCY.add("INR");
		    					VDOC_TYPE.add("X2");		
		    					VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
		    					VTRANS_TYPE.add(cash_flow);
		    					VPK.add(pk);
		    					VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    					VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    					VDOC_DT.add(doc_dt);
		    					VPOST_DT.add(postDt);
		    					VALLOC_QTY.add("");
		    					//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    					VAMT.add(sign+""+amt);
		    					VUSD_AMT.add("");
		    					String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
		    					String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
		    					VSAP_VALUE_USD.add("");
		    					VDELTA_SAP_VALUE_USD.add("");
		    					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    					VIS_SAP_DOC_DATA.add("");
		    					//amtSum+=Double.parseDouble(sign+""+amt)
		    					amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
		    					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    					VSAP_VALUE_INR.add(sap_val);
		    					VDELTA_SAP_VALUE_INR.add(delta_sap_val);			
		    				}
				    	}
				    }
		    	}
		    	else
		    	{
		    		if(inv_raised_in.equals("2") && !exchng_rate_value.equals(""))
		    		{
		    			net_payable_amt=rset.getString(14)==null?"":nf.format(rset.getDouble(14) * Double.parseDouble(exchng_rate_value));
		    			
		    			if(gen_type.equals("DLNG_FFLOW_INV"))
		    			{
		    				gross_amt=rset.getString(29)==null?"":nf.format(rset.getDouble(29) * Double.parseDouble(exchng_rate_value));
		    			}
		    			else
		    			{
		    				gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9) * Double.parseDouble(exchng_rate_value));
		    			}
		    			
		    			tcs_amt=rset.getString(22)==null?"":nf.format(rset.getDouble(22) * Double.parseDouble(exchng_rate_value));
		    			tds_amt=rset.getString(23)==null?"":nf.format(rset.getDouble(23) * Double.parseDouble(exchng_rate_value));
		    			tax_amt=rset.getString(24)==null?"":nf.format(rset.getDouble(24) * Double.parseDouble(exchng_rate_value));
		    		}
		    		
		    		if(invoice_category.equals("P"))
		    		{
		    			cash_flow="Commodity";
		    		}
		    		else if(invoice_category.equals("S"))
		    		{
		    			cash_flow="Service";
		    		}
		    		else
		    		{
		    			cash_flow="Commodity";
		    			if(cont_type.equals("O") || cont_type.equals("Q")) 
		    			{
		    				cash_flow="Re-Gas Capacity";
		    			}
		    		}
		    		
		    		if(!gross_amt.equals(""))
		    		{
		    			sign = "-";
		    			pk = "50";
		    			if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    			{
		    				pk = "40";
		    				sign = "";
		    			}
		    			
		    			tempAccount="";
		    			itemText="";
		    			if(invoice_type.equals("LP") || inv_flag.equals("LP"))
		    			{
		    				tempAccount="8240050";
		    				itemText="Interest";
		    			}
		    			else 
		    			{
		    				/*if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
				    	{
				    		//tempAccount="7220300";
				    		//tempAccount="6000400";
				    		//itemText="Brokerage/Commission";
				    		itemText="Commodity";
				    	}
				    	else
				    	{	
				    		itemText=cash_flow;
				    	}*/
		    				
		    				if(sub_inv_type.equals("IMB"))
		    				{
		    					tempAccount="6318400";
		    					itemText=cash_flow;
		    				}
		    				else if(inv_flag.equals("ST")) //FOR STORAGE
		    				{
		    					//tempAccount="6320405";
		    					
		    					//AS PER VIJAY'S MAIL ON 25/06/2025
		    					if(countpty_category.equals("Group"))
		    					{
		    						tempAccount="6851550";
		    					}
		    					else
		    					{
		    						tempAccount="6850550";
		    					}
		    					itemText="LNG Storage Charge";
		    				}
		    				else if(cont_type.equals("O") || cont_type.equals("Q"))
		    				{
		    					if(countpty_category.equals("Group"))
		    					{
		    						tempAccount="6000450";
		    					}
		    					else
		    					{
		    						tempAccount="6000500";
		    					}
		    					itemText=cash_flow;
		    				}
		    				else if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    				{
		    					//tempAccount="7220300";
		    					//tempAccount="6000400";
		    					//itemText="Brokerage/Commission";
		    					if(countpty_category.equals("Group"))
		    					{
		    						tempAccount="6001400";
		    					}
		    					else
		    					{
		    						tempAccount="6000400";
		    					}
		    					itemText="Commodity";
		    				}
		    				else if(cont_type.equals("B") || cont_type.equals("M"))
		    				{
		    					if(countpty_category.equals("Group"))
		    					{
		    						tempAccount="6850319";
		    						itemText=cash_flow;
		    					}
		    					else
		    					{
		    						tempAccount="6850320";
		    						itemText=cash_flow;
		    					}
		    				}
		    				else 
		    				{
		    					if(countpty_category.equals("Group"))
		    					{
		    						tempAccount="6001400";
		    						itemText=cash_flow;
		    					}
		    					else
		    					{
		    						tempAccount="6000400";
		    						itemText=cash_flow;
		    					}
		    				}
		    			}
		    			
		    			VCOUNTERPARTY_CD.add(counterpty_cd);
		    			VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    			VCO_CD.add(co_cd);
		    			VDEAL_MAP.add(deal_map);
		    			VINVOICE_NO.add(invoice_no);
		    			VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    			VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    			VPERIOD_START_DT.add(periodStartDt);
		    			VPERIOD_END_DT.add(periodEndDt);
		    			VGL_CD.add(utilBean.PrePaddingZero(tempAccount, 10));
		    			VGL_DESC.add(utilBean.getGLdesc(conn, tempAccount));
		    			VCURRENCY.add("INR");
		    			VDOC_TYPE.add("X2");
		    			if(gen_type.equals("DLNG_SVC_INV"))
		    			{
		    				String prodPeriodYear=productionPeriodYear.substring(2);
		    				VITEMTEXT.add(itemText+" "+monthNm+" "+prodPeriodYear);
		    			}
		    			else
		    			{
		    				VITEMTEXT.add(itemText+" "+monthNm+" "+productionPeriodYear);
		    			}
		    			VTRANS_TYPE.add(cash_flow);
		    			VPK.add(pk);
		    			VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    			VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    			VDOC_DT.add(doc_dt);
		    			VPOST_DT.add(postDt);
		    			VALLOC_QTY.add(alloc_qty);
		    			VAMT.add(sign+""+gross_amt);
		    			VUSD_AMT.add("");
		    			qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    			String sap_val=getSAP_Doc_Value(co_cd,tempAccount,postDt,invoice_no,"1");
		    			String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt) - Double.parseDouble(sap_val));
		    			VSAP_VALUE_USD.add("");
		    			VDELTA_SAP_VALUE_USD.add("");
		    			VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    			VIS_SAP_DOC_DATA.add("");
		    			amtSum+=Double.parseDouble(gross_amt.equals("")?"0":sign+""+gross_amt);
		    			sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    			deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    			VSAP_VALUE_INR.add(sap_val);
		    			VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    			extn_mapping+=","+tempAccount;
		    		}
		    		
		    		//if(tax_flag.equals("Y"))
		    		{
		    			int stcount=0;
		    			String queryString2="";
		    			if(gen_type.equals("DLNG_FFLOW_INV"))
		    			{
		    				queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
		    						+ "FROM FMS_DLNG_FFLOW_INV_TAX_DTL "
		    						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
		    						+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
		    						+ "AND INVOICE_TYPE=?";
		    			}
		    			else if(gen_type.equals("DLNG_INV"))
		    			{
		    				queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
		    						+ "FROM FMS_DLNG_INV_TAX_DTL "
		    						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
		    						+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
		    			}
		    			else if(gen_type.equals("DLNG_SVC_INV"))
		    			{
		    				queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
		    						+ "FROM FMS_DLNG_SVC_INV_TAX_DTL "
		    						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
		    						+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? ";
		    			}
		    			stmt1 = conn.prepareStatement(queryString2);
		    			if(gen_type.equals("DLNG_FFLOW_INV"))
		    			{
		    				stmt1.setString(++stcount, comp_cd);
		    				stmt1.setString(++stcount, fin_year);
		    				stmt1.setString(++stcount, inv_seq);
		    				stmt1.setString(++stcount, bu_state_tin);
		    				stmt1.setString(++stcount, invoice_type);
		    			}
		    			else if(gen_type.equals("DLNG_INV"))
		    			{
		    				stmt1.setString(++stcount, comp_cd);
		    				stmt1.setString(++stcount, fin_year);
		    				stmt1.setString(++stcount, inv_seq);
		    				stmt1.setString(++stcount, bu_state_tin);
		    			}
		    			else if(gen_type.equals("DLNG_SVC_INV"))
		    			{
		    				stmt1.setString(++stcount, comp_cd);
		    				stmt1.setString(++stcount, fin_year);
		    				stmt1.setString(++stcount, inv_seq);
		    				stmt1.setString(++stcount, bu_state_tin);
		    			}
		    			rset1=stmt1.executeQuery();
		    			while(rset1.next())
		    			{
		    				String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
		    				String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
		    				String taxAmt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
		    				String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
		    				
		    				if(!taxAmt.equals(""))
		    				{
		    					pk="50";
		    					sign="-";
		    					if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    					{
		    						pk = "40";
		    						sign = "";
		    					}
		    					
		    					String gl_code="";
		    					String tax_sap_code="";
		    					String amt=taxAmt;
		    					
		    					if(Double.parseDouble(amt) > 0)
		    					{
		    						gl_code=utilBean.getTaxGLcode(conn,tax_struct_cd, tax_code);
		    						extn_mapping+=","+gl_code;
		    						tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
		    						if(tax_flag.equals("Y"))
		    						{
		    							tax_sap_code=utilBean.getTaxSAPcode(conn,tax_struct_cd, tax_code);
		    							VCOUNTERPARTY_CD.add(counterpty_cd);
		    							VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    							VCO_CD.add(co_cd);
		    							VDEAL_MAP.add(deal_map);
		    							VINVOICE_NO.add(invoice_no);
		    							VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    							VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    							VPERIOD_START_DT.add(periodStartDt);
		    							VPERIOD_END_DT.add(periodEndDt);
		    							VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
		    							VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
		    							VCURRENCY.add("INR");
		    							VDOC_TYPE.add("X2");		
		    							VITEMTEXT.add("T "+" ["+tax_sap_code+"]");
		    							VTRANS_TYPE.add(cash_flow);
		    							VPK.add(pk);
		    							VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    							VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    							VDOC_DT.add(doc_dt);
		    							VPOST_DT.add(postDt);
		    							VALLOC_QTY.add("");
		    							//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    							VAMT.add(sign+""+amt);
		    							VUSD_AMT.add("");
		    							String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
		    							String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
		    							VSAP_VALUE_USD.add("");
		    							VDELTA_SAP_VALUE_USD.add("");
		    							VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    							VIS_SAP_DOC_DATA.add("");
		    							//amtSum+=Double.parseDouble(sign+""+amt)
		    							amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
		    							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    							VSAP_VALUE_INR.add(sap_val);
		    							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    						}
		    					}
		    				}
		    			}
		    			rset1.close();
		    			stmt1.close();
		    		}
		    		
		    		if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
		    		{
		    			String gl_code="";
		    			String TcsTdscode="";
		    			String amt="";
		    			pk="";
		    			String taxBase="";
		    			sign="";
		    			if(tcs_tds.equals("TCS"))
		    			{
		    				gl_code=utilBean.getTaxGLcode(conn,tcs_struct_cd);
		    				extn_mapping+=","+gl_code;
		    				if(tcs_flag.equals("Y"))
		    				{
		    					TcsTdscode=utilBean.getTaxSAPcode(conn,tcs_struct_cd);
		    					amt=tcs_amt;
		    					pk="50";
		    					//taxBase=invoiceAmt;
		    					
		    					/*if(!tcs_amt.equals("") && !tcs_factor.equals(""))
							{
								taxBase=nf.format((Double.parseDouble(tcs_amt)*100)/Double.parseDouble(tcs_factor));
							}*/
		    					
		    					sign="-";
		    					if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    					{
		    						pk = "40";
		    						sign = "";
		    					}
		    					VCOUNTERPARTY_CD.add(counterpty_cd);
		    					VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    					VCO_CD.add(co_cd);
		    					VDEAL_MAP.add(deal_map);
		    					VINVOICE_NO.add(invoice_no);
		    					VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    					VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    					VPERIOD_START_DT.add(periodStartDt);
		    					VPERIOD_END_DT.add(periodEndDt);
		    					VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
		    					VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
		    					VCURRENCY.add("INR");
		    					VDOC_TYPE.add("X2");		
		    					VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
		    					VTRANS_TYPE.add(cash_flow);
		    					VPK.add(pk);
		    					VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    					VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    					VDOC_DT.add(doc_dt);
		    					VPOST_DT.add(postDt);
		    					VALLOC_QTY.add("");
		    					//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    					VAMT.add(sign+""+amt);
		    					VUSD_AMT.add("");
		    					String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
		    					String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
		    					VSAP_VALUE_USD.add("");
		    					VDELTA_SAP_VALUE_USD.add("");
		    					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    					VIS_SAP_DOC_DATA.add("");
		    					//amtSum+=Double.parseDouble(sign+""+amt)
		    					amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
		    					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    					VSAP_VALUE_INR.add(sap_val);
		    					VDELTA_SAP_VALUE_INR.add(delta_sap_val);
		    				}
		    			}
		    			else if(tcs_tds.equals("TDS"))
		    			{
		    				gl_code=utilBean.getTaxGLcode(conn,tds_struct_cd);
		    				extn_mapping+=","+gl_code;
		    				if(tds_flag.equals("Y"))
		    				{
		    					TcsTdscode=utilBean.getTaxSAPcode(conn,tds_struct_cd);
		    					amt=tds_amt;
		    					pk="40";
		    					
		    					if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    					{
		    						pk = "50";
		    						sign = "-";
		    					}
		    					VCOUNTERPARTY_CD.add(counterpty_cd);
		    					VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
		    					VCO_CD.add(co_cd);
		    					VDEAL_MAP.add(deal_map);
		    					VINVOICE_NO.add(invoice_no);
		    					VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
		    					VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
		    					VPERIOD_START_DT.add(periodStartDt);
		    					VPERIOD_END_DT.add(periodEndDt);
		    					VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
		    					VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
		    					VCURRENCY.add("INR");
		    					VDOC_TYPE.add("X2");		
		    					VITEMTEXT.add(tcs_tds+" ["+TcsTdscode+"]");
		    					VTRANS_TYPE.add(cash_flow);
		    					VPK.add(pk);
		    					VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
		    					VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
		    					VDOC_DT.add(doc_dt);
		    					VPOST_DT.add(postDt);
		    					VALLOC_QTY.add("");
		    					//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
		    					VAMT.add(sign+""+amt);
		    					VUSD_AMT.add("");
		    					String sap_val=getSAP_Doc_Value(co_cd,gl_code,postDt,invoice_no,"1");
		    					String delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble(amt.equals("")?"0":sign+""+amt) - Double.parseDouble(sap_val));
		    					VSAP_VALUE_USD.add("");
		    					VDELTA_SAP_VALUE_USD.add("");
		    					VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
		    					VIS_SAP_DOC_DATA.add("");
		    					//amtSum+=Double.parseDouble(sign+""+amt)
		    					amtSum+=Double.parseDouble(amt.equals("")?"0":sign+""+amt);
		    					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
		    					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
		    					VSAP_VALUE_INR.add(sap_val);
		    					VDELTA_SAP_VALUE_INR.add(delta_sap_val);					 
		    				}
		    			}
		    		}
		    	}
				
				addSAPRecord(counterpty_cd,agmt_no,agmt_rev,"",cont_no,cont_rev,cont_type,"",co_cd,invoice_no,postDt,periodStartDt,periodEndDt,  extn_mapping);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getContPeriodDetails(String counterparty_cd, String agmt_no, String agmt_rev, String cont_no, String cont_rev,String cont_type)
	{
		String function_nm="getContPeriodDetails()";
		String period="";
		try
		{
			String queryString1="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? "
					+ "UNION "
					+ "SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, agmt_no);
			stmt1.setString(4, agmt_rev);
			stmt1.setString(5, cont_no);
			stmt1.setString(6, cont_rev);
			stmt1.setString(7, cont_type);
			stmt1.setString(8, comp_cd);
			stmt1.setString(9, counterparty_cd);
			stmt1.setString(10, agmt_no);
			stmt1.setString(11, agmt_rev);
			stmt1.setString(12, cont_no);
			stmt1.setString(13, cont_rev);
			stmt1.setString(14, cont_type);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				String st_dt=rset1.getString(1)==null?"":rset1.getString(1);
				String end_dt=rset1.getString(2)==null?"":rset1.getString(2);
				period=st_dt+"--"+end_dt;
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return period;
	}
	
	public String getSapDocNo(String ref)
	{
		String function_nm="getSapDocNo()";
		String doc_no="";
		try
		{
			String query="SELECT DOC_NO "
					+ "FROM FMS_SAP_ACK_DTL "
					+ "WHERE COMPANY_CD=? AND FMS_REF=?"; // AND MSG_STATUS=? ";
			stmt4=conn.prepareStatement(query);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, ref);
			//stmt4.setString(3, "S");
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				doc_no=rset4.getString(1)==null?"":rset4.getString(1);
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return doc_no;
	}

	public void getSapIncomingDetails()
	{
		String function_nm="getSapIncomingDetails()";
		try
		{
			String spliFromDt[] = from_dt.split("/");
			String spliToDt[] = to_dt.split("/");
			
			String fromDttoNum = spliFromDt[2]+spliFromDt[1]+spliFromDt[0];
			String toDttoNum = spliToDt[2]+spliToDt[1]+spliToDt[0];
			
			String query="SELECT BUKRS, "
					+ "BELNR, "
					+ "GJAHR, "
					+ "BLDAT, "
					+ "SGTXT, "
					+ "SHKZG, "
					+ "WRBTR, "
					+ "WAERS, "
					+ "DMBTR, "
					+ "HWAER, "
					+ "ZUKEY, "
					+ "ZDTYP, "
					+ "HKONT, "
					+ "AUGBL, "
					+ "XBLNR, "
					+ "RSTGR, "
					+ "AUGDT, "
					+ "BLART, "
					+ "ZUONR, "
					+ "KUNNR,"
					+ "NAME "
					+ "FROM FMS_SAP_INCOMING_DTL "
					+ "WHERE BUKRS= (SELECT SAP_CODE FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_CD=?) "
					+ "AND BLDAT<=? AND BLDAT>=?"; 
			stmt4=conn.prepareStatement(query);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, toDttoNum);
			stmt4.setString(3, fromDttoNum);
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				String company_code = rset4.getString(1)==null?"":rset4.getString(1);
				String document_number = rset4.getString(2)==null?"":rset4.getString(2);
				String fiscal_year = rset4.getString(3)==null?"":rset4.getString(3);
				String document_date = rset4.getString(4)==null?"":rset4.getString(4);
				String text = rset4.getString(5)==null?"":rset4.getString(5);
				String debit_credit = rset4.getString(6)==null?"":rset4.getString(6);
				String amount = rset4.getString(7)==null?"":rset4.getString(7);
				String currency = rset4.getString(8)==null?"":rset4.getString(8);
				String amt_local_curr = rset4.getString(9)==null?"":rset4.getString(9);
				String loacl_curr = rset4.getString(10)==null?"":rset4.getString(10);
				String unique_code = rset4.getString(11)==null?"":rset4.getString(11);
				String transaction = rset4.getString(12)==null?"":rset4.getString(12);
				String general_ledger = rset4.getString(13)==null?"":rset4.getString(13);
				String clearing_document = rset4.getString(14)==null?"":rset4.getString(14);
				String reference = rset4.getString(15)==null?"":rset4.getString(15);
				String reason_code = rset4.getString(16)==null?"":rset4.getString(16);
				String clearing_document_date = rset4.getString(17)==null?"":rset4.getString(17);
				String document_type = rset4.getString(18)==null?"":rset4.getString(18);
				String assignment_number = rset4.getString(19)==null?"":rset4.getString(19);
				String customer_code = rset4.getString(20)==null?"":rset4.getString(20);
				String customer_name = rset4.getString(21)==null?"":rset4.getString(21);
				
				VBUKRS.add(company_code);
				VKUNNR.add(customer_code);
				VBELNR.add(document_number);
				VGJAHR.add(fiscal_year);
				VBLDAT.add(document_date);
				VSGTXT.add(text);
				if(debit_credit.equals("H")) 
				{
					VSHKZG.add("Credit");
				}
				else//debit_credit => "S"
				{
					VSHKZG.add("Debit");
				}
				VWRBTR.add(amount);
				VWAERS.add(currency);
				VDMBTR.add(amt_local_curr);
				VHWAER.add(loacl_curr);
				VZUKEY.add(unique_code);
				VZDTYP.add(transaction);
				VHKONT.add(general_ledger);
				VAUGBL.add(clearing_document);
				VXBLNR.add(reference);
				VRSTGR.add(reason_code);
				VAUGDT.add(clearing_document_date);
				VBLART.add(document_type);
				VZUONR.add(assignment_number);
				VNAME.add(customer_name);
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getSAP_Doc_Value(String co_cd,String gl_cd,String post_dt,String ref_no, String rate_cd)
	{
		String sap_value="";
		String function_nm="getSAP_Doc_Value()";
		try
		{
			/*
			 	If rate is in USD then consider Amount in loc. currency 
			 	If rate is in INR then consider Amount in loc. currency 2.
			*/
			double sap_val_sum=0;
			int loop_count=0;
			String select_query="";
			if(rate_cd.equals("1"))		
			{
				select_query="SELECT LC_AMT2 ";
			}
			else
			{
				select_query="SELECT LC_AMT ";
			}
			String query=select_query+",COCD||'@'||DOC_NO||'@'||GL_CODE||'@'||TO_CHAR(POST_DT,'DD/MM/YYYY')||'@'||ITM "+" FROM FMS_SAP_PIVOT_DTL "
					+ "WHERE COCD=? AND TRIM(GL_CODE)=?  AND "
					+ "TO_DATE(TO_CHAR(POST_DT,'YYYYMMDD'),'YYYYMMDD')=TO_DATE(?,'YYYYMMDD') AND (TRIM(REFERENCE_NO)=? OR TRIM(ASSIGNMENT)=?) "; 
			String temp_query=query;
			stmt4=conn.prepareStatement(temp_query);
			stmt4.setString(1, co_cd.trim());
			stmt4.setString(2, gl_cd.trim());
			stmt4.setString(3, post_dt.trim());
			stmt4.setString(4, ref_no.trim());
			stmt4.setString(5, ref_no.trim());
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				loop_count+=1;
				sap_val_sum=rset4.getDouble(1);
				//sap_value=rset4.getString(1)==null?"":nf.format(rset4.getDouble(1));
				if(ext_gl_mapping.equals(""))
				{
					ext_gl_mapping+="'"+(rset4.getString(2)==null?"":rset4.getString(2))+"'";
				}
				else
				{
					ext_gl_mapping+=","+"'"+(rset4.getString(2)==null?"":rset4.getString(2))+"'";
				}
			}
			rset4.close();
			stmt4.close();
			if(loop_count>0)
			{
				sap_value=nf.format(sap_val_sum);
			}
			else
			{
				sap_value="";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return sap_value;
	}
	
	//PB 20250901 FOR ADDING THE SAP RECORD FROM THE DUMP FILE TO THE RECON REPORT 
	public void addSAPRecord(String counterpty_cd, String agmt_no, String agmt_rev, String agmt_type, String cont_no, String cont_rev, String cont_type,String cargo_no, String cocd, String ref_no, String post_dt,String period_st_dt, String period_end_dt, String mapping)
	{
		String function_nm="addSAPRecord()";
		try
		{
			String co_cd="";
			String doc_no="";
			String postdt="";
			String pk="";
			String doc_type="";
			String refno="";
			String asgnmt_no="";
			String doc_dt="";
			String lc_curr="";
			String lc_amt="";
			String lc_curr2="";
			String lc_amt2="";
			String gl_code="";
			String dc_curr="";
			String dc_amt="";
			
			int ctn=0;
			if(!mapping.equals(""))
			{
				String inv_ref = mapping.split("@")[0];
				String gl_ref = mapping.split("@")[1];
				
				String query="SELECT COMPANY_CD, COCD, DOC_NO,GL_CODE,TO_CHAR(POST_DT,'DD/MM/YYYY'),POSTING_KEY, "
						+ "DOC_TYPE,REFERENCE_NO,ASSIGNMENT,TO_CHAR(DOCUMENT_DT,'DD/MM/YYYY'),LC_CURR,LC_AMT, "
						+ "LC_CURR2,LC_AMT2,DC_CURR,DC_AMT,COCD||'@'||DOC_NO||'@'||GL_CODE||'@'||TO_CHAR(POST_DT,'DD/MM/YYYY')||'@'||ITM "
						+ "FROM FMS_SAP_PIVOT_DTL "
						+ "WHERE COCD=? AND (TRIM(REFERENCE_NO)=? OR TRIM(ASSIGNMENT)=?) AND TO_DATE(TO_CHAR(POST_DT,'YYYYMMDD'),'YYYYMMDD')=TO_DATE(?,'YYYYMMDD')";
				stmt4 = conn.prepareStatement(query);
				stmt4.setString(1, cocd.trim());
				stmt4.setString(2, ref_no.trim());
				stmt4.setString(3, ref_no.trim());
				stmt4.setString(4, post_dt.trim());
				rset4 = stmt4.executeQuery();
				while(rset4.next())
				{
					co_cd=rset4.getString(2)==null?"":rset4.getString(2);
					doc_no=rset4.getString(3)==null?"":rset4.getString(3);
					gl_code=rset4.getString(4)==null?"":rset4.getString(4);
					postdt=rset4.getString(5)==null?"":rset4.getString(5);
					pk=rset4.getString(6)==null?"":rset4.getString(6);
					doc_type=rset4.getString(7)==null?"":rset4.getString(7);
					refno=rset4.getString(8)==null?"":rset4.getString(8);
					asgnmt_no=rset4.getString(9)==null?"":rset4.getString(9);
					doc_dt=rset4.getString(10)==null?"":rset4.getString(10);
					lc_curr=rset4.getString(11)==null?"":rset4.getString(11);
					lc_amt=rset4.getString(12)==null?"":nf.format(rset4.getDouble(12));
					lc_curr2=rset4.getString(13)==null?"":rset4.getString(13);
					lc_amt2=rset4.getString(14)==null?"":nf.format(rset4.getDouble(14));
					dc_curr=rset4.getString(15)==null?"":rset4.getString(15);
					dc_amt=rset4.getString(16)==null?"":nf.format(rset.getDouble(16));
					
					String deal_map = utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
					String accountingPeriodYear = postdt.split("/")[2];
					String accountingPeriodMonth = postdt.split("/")[1];
					doc_dt = doc_dt.split("/")[2]+doc_dt.split("/")[1]+doc_dt.split("/")[0];
					postdt = postdt.split("/")[2]+postdt.split("/")[1]+postdt.split("/")[0];
					if(!gl_ref.contains(gl_code))
					{
						if(ext_gl_mapping.equals(""))
						{
							ext_gl_mapping+="'"+(rset4.getString(17)==null?"":rset4.getString(17))+"'";
						}
						else
						{
							ext_gl_mapping+=","+"'"+(rset4.getString(17)==null?"":rset4.getString(17))+"'";
						}
						
						String curr=utilBean.getRateUnitNm(conn, dc_curr);
						
						VCOUNTERPARTY_CD.add(counterpty_cd);
						VVENDOR_CD.add(utilBean.getCounterpartySAPcode(conn, counterpty_cd));
						VCO_CD.add(co_cd);
						VDEAL_MAP.add(deal_map);
						VINVOICE_NO.add(ref_no);
						VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
						VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
						VPERIOD_START_DT.add(period_st_dt);		
						VPERIOD_END_DT.add(period_end_dt);	
						VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
						VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
						VCURRENCY.add(curr);
						VDOC_TYPE.add(doc_type);		
						VITEMTEXT.add("");
						VTRANS_TYPE.add("");
						VPK.add(pk);
						VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
						//VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
						VSAP_APPROVED_BY.add("");
						VDOC_DT.add(doc_dt);
						VPOST_DT.add(postdt);
						VALLOC_QTY.add("");
						//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
						VAMT.add("");
						VUSD_AMT.add("");
						String sap_val="";
						String delta_sap_val="";
						if(dc_curr.equals("1"))
						{
							sap_val=lc_amt2;
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
							VSAP_VALUE_INR.add(sap_val);
							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							VSAP_VALUE_USD.add("");
							VDELTA_SAP_VALUE_USD.add("");
						}
						else if(dc_curr.equals("2"))
						{
							sap_val=lc_amt;
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
							VSAP_VALUE_USD.add(sap_val);
							VDELTA_SAP_VALUE_USD.add(delta_sap_val);
							sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							VSAP_VALUE_INR.add("");
							VDELTA_SAP_VALUE_INR.add("");
						}
						else 	//PB 20250911: AS PER VIJAY FEEDBACK FOR HANDLING THE DATA WHICH HAS CURRENCY TYPE OTHER THAN USD OR INR. 
						{
							sap_val=lc_amt2;
							delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
							VSAP_VALUE_INR.add(sap_val);
							VDELTA_SAP_VALUE_INR.add(delta_sap_val);
							sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
							deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
							VSAP_VALUE_USD.add("");
							VDELTA_SAP_VALUE_USD.add("");
						}
						VVENDOR_NM.add(""+utilBean.getCounterpartyName(conn, counterpty_cd));
						VIS_SAP_DOC_DATA.add("Y");
					}
				}
				rset4.close();
				stmt4.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//pb 20250905: FOR ADDING THE SAP DUMP DATA WHICH ARE NOT PRESENT IN EMS
	public void fetchSapDumpData()
	{
		String function_nm="fetchSapDumpData()";
		try
		{
			String cocd=utilBean.getCompanySAPcode(conn, comp_cd);
			
			String co_cd="";
			String doc_no="";
			String postdt="";
			String pk="";
			String doc_type="";
			String refno="";
			String asgnmt_no="";
			String doc_dt="";
			String lc_curr="";
			String lc_amt="";
			String lc_curr2="";
			String lc_amt2="";
			String gl_code="";
			String dc_curr="";
			String dc_amt="";
			
			String query="SELECT COMPANY_CD, COCD, DOC_NO,GL_CODE,TO_CHAR(POST_DT,'DD/MM/YYYY'),POSTING_KEY, "
					+ "DOC_TYPE,REFERENCE_NO,ASSIGNMENT,TO_CHAR(DOCUMENT_DT,'DD/MM/YYYY'),LC_CURR,LC_AMT, "
					+ "LC_CURR2,LC_AMT2,DC_CURR,DC_AMT "
					+ "FROM FMS_SAP_PIVOT_DTL "
					+ "WHERE COCD=? "
					+ "AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(!ext_gl_mapping.equals(""))
			{
				query+= "AND COCD||'@'||DOC_NO||'@'||GL_CODE||'@'||TO_CHAR(POST_DT,'DD/MM/YYYY')||'@'||ITM NOT IN ("+ext_gl_mapping+") ";
			}
			String temp_query=query;
			stmt4=conn.prepareStatement(temp_query);
			stmt4.setString(1, cocd);
			stmt4.setString(2, to_dt);
			stmt4.setString(3, from_dt);
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				co_cd=rset4.getString(2)==null?"":rset4.getString(2);
				doc_no=rset4.getString(3)==null?"":rset4.getString(3);
				gl_code=rset4.getString(4)==null?"":rset4.getString(4);
				postdt=rset4.getString(5)==null?"":rset4.getString(5);
				pk=rset4.getString(6)==null?"":rset4.getString(6);
				doc_type=rset4.getString(7)==null?"":rset4.getString(7);
				refno=rset4.getString(8)==null?"":rset4.getString(8);
				asgnmt_no=rset4.getString(9)==null?"":rset4.getString(9);
				doc_dt=rset4.getString(10)==null?"":rset4.getString(10);
				lc_curr=rset4.getString(11)==null?"":rset4.getString(11);
				lc_amt=rset4.getString(12)==null?"":nf.format(rset4.getDouble(12));
				lc_curr2=rset4.getString(13)==null?"":rset4.getString(13);
				lc_amt2=rset4.getString(14)==null?"":nf.format(rset4.getDouble(14));
				dc_curr= rset4.getString(15)==null?"":rset4.getString(15);
				dc_amt=rset4.getString(16)==null?"":nf.format(rset4.getDouble(16));
				
				String deal_map ="";
				String accountingPeriodYear = postdt.split("/")[2];
				String accountingPeriodMonth = postdt.split("/")[1];
				doc_dt = doc_dt.split("/")[2]+doc_dt.split("/")[1]+doc_dt.split("/")[0];
				postdt = postdt.split("/")[2]+postdt.split("/")[1]+postdt.split("/")[0];
				
				String curr=utilBean.getRateUnitNm(conn, dc_curr);
				
				VCOUNTERPARTY_CD.add("");
				VVENDOR_CD.add("");
				VCO_CD.add(co_cd);
				VDEAL_MAP.add(deal_map);
				VINVOICE_NO.add("");
				VACCOUNT_PERIOD_YR.add(accountingPeriodYear);
				VACCOUNT_PERIOD_MONTH.add(accountingPeriodMonth);
				VPERIOD_START_DT.add("");		
				VPERIOD_END_DT.add("");	
				VGL_CD.add(utilBean.PrePaddingZero(gl_code, 10));
				VGL_DESC.add(utilBean.getGLdesc(conn, gl_code));
				VDOC_TYPE.add(doc_type);		
				VITEMTEXT.add("");
				VTRANS_TYPE.add("");
				VPK.add(pk);
				VDOC_NO.add(doc_no);		//null for now but needs to link with sap ack details 
				//VSAP_APPROVED_BY.add(utilBean.getEmpName(conn, sap_approve_by).equals("")?"System":utilBean.getEmpName(conn, sap_approve_by));
				VSAP_APPROVED_BY.add("");
				VDOC_DT.add(doc_dt);
				VPOST_DT.add(postdt);
				VALLOC_QTY.add("");
				//qtySum+=alloc_qty.equals("")?0:Double.parseDouble(alloc_qty);
				VAMT.add("");
				VUSD_AMT.add("");
				
				VIS_SAP_DOC_DATA.add("Y");
				VVENDOR_NM.add("");

				String sap_val="";
				String delta_sap_val="";
				if(dc_curr.equals("1"))
				{
					sap_val=lc_amt2;
					delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
					VSAP_VALUE_INR.add(sap_val);
					VDELTA_SAP_VALUE_INR.add(delta_sap_val);
					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					VSAP_VALUE_USD.add("");
					VDELTA_SAP_VALUE_USD.add("");
					VCURRENCY.add(curr);
				}
				else if(dc_curr.equals("2"))
				{
					sap_val=lc_amt;
					delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
					VSAP_VALUE_USD.add(sap_val);
					VDELTA_SAP_VALUE_USD.add(delta_sap_val);
					sapSumUsd+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					deltaSapSumUsd+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					VSAP_VALUE_INR.add("");
					VDELTA_SAP_VALUE_INR.add("");
					VCURRENCY.add(curr);
				}
				else 	//PB 20250911: AS PER VIJAY FEEDBACK FOR HANDLING THE DATA WHICH HAS CURRENCY TYPE OTHER THAN USD OR INR.
				{
					sap_val=lc_amt2;
					delta_sap_val = sap_val.equals("")?"":nf.format(Double.parseDouble("0") - Double.parseDouble(sap_val));
					VSAP_VALUE_INR.add(sap_val);
					VDELTA_SAP_VALUE_INR.add(delta_sap_val);
					sapsum+=sap_val.equals("")?0:Double.parseDouble(sap_val);
					deltasapsum+=delta_sap_val.equals("")?0:Double.parseDouble(delta_sap_val);
					VSAP_VALUE_USD.add("");
					VDELTA_SAP_VALUE_USD.add("");
					VCURRENCY.add(utilBean.getRateUnitNm(conn, lc_curr2));
				}
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//PB 20250902: for SAP Recon Consolidate report
	public void getSAPReconConsolidateRpt()
	{
		String function_nm="getSAPReconConsolidateRpt()";
		try
		{
			sapsum=0;
			sapSumUsd=0;
			String query="SELECT GL_CODE, GL_DECR "
					+ "FROM FMS_SAP_ACCOUNT_GL_MST ";
			//stmt=conn.prepareStatement(query);
			//rset=stmt.executeQuery();
			//while(rset.next())
			Set distinctSet = new LinkedHashSet(VGL_CD);
			Vector distinctVector = new Vector(distinctSet);
			for(int d=0;d<distinctVector.size();d++)
			{
				//String gl_cd=rset.getString(1)==null?"":rset.getString(1);
				String gl_cd=""+distinctVector.elementAt(d);
				gl_cd=utilBean.RemovePrePaddingZero(gl_cd);
				String mapping ="";
				String period =from_dt.split("/")[2]+from_dt.split("/")[1];
				for(int i=0;i<VGL_CD.size();i++)
				{
					if(utilBean.PrePaddingZero(gl_cd, 10).equals(VGL_CD.elementAt(i)))
					{
						String temp_map = gl_cd + "@@" + VCURRENCY.elementAt(i);
						if (mapping.equals("")) 
						{
							mapping += temp_map;
						} 
						else 
						{
							if (!mapping.equals(temp_map)) 
							{
								mapping += "," + temp_map;
							}
						}
					}
				}
				if(mapping.contains("INR"))
				{
					VCON_COCD.add(""+utilBean.getCompanySAPcode(conn, comp_cd));
					VCON_PERIOD.add(period);
					VCON_GL.add(utilBean.PrePaddingZero(gl_cd, 10));
					VCON_GL_DECR.add(""+utilBean.getGLdesc(conn, gl_cd));
					VCON_CURR.add("INR");
					VCON_YEAR.add(from_dt.split("/")[2]);
					VCON_MONTH.add(from_dt.split("/")[1]);
				}
				if(mapping.contains("USD"))
				{
					VCON_COCD.add(""+utilBean.getCompanySAPcode(conn, comp_cd));
					VCON_PERIOD.add(period);
					VCON_GL.add(utilBean.PrePaddingZero(gl_cd, 10));
					VCON_GL_DECR.add(""+utilBean.getGLdesc(conn, gl_cd));
					VCON_CURR.add("USD");
					VCON_YEAR.add(from_dt.split("/")[2]);
					VCON_MONTH.add(from_dt.split("/")[1]);
				}

			}
			//rset.close();
			//stmt.close();
			
			for (int i = 0; i < VCON_GL.size(); i++) 
			{
				double amt=0;
				double usd_amt=0;
				for (int j = 0; j < VGL_CD.size(); j++) 
				{
					if (VCON_GL.elementAt(i).equals(VGL_CD.elementAt(j))) 
					{
						if (VCON_CURR.elementAt(i).equals("INR")) 
						{
							if(!VAMT.elementAt(j).equals(""))
							{
								amt+=Double.parseDouble(VAMT.elementAt(j).toString());
							}
						} 
						else 
						{
							if(!VUSD_AMT.elementAt(j).equals(""))
							{
								usd_amt+=Double.parseDouble(VUSD_AMT.elementAt(j).toString());
							}
						}
					}
				}
				if(VCON_CURR.elementAt(i).equals("INR"))
				{
					VCON_EMS_AMT.add(nf.format(amt));
					VCON_EMS_AMT_USD.add("");
					String sap_amt = getConGLValue(""+utilBean.RemovePrePaddingZero(""+VCON_GL.elementAt(i)),""+VCON_CURR.elementAt(i));
					sap_amt=sap_amt.equals("")?"":nf.format(Double.parseDouble(sap_amt));
					sapsum+=sap_amt.equals("")?Double.parseDouble("0"):Double.parseDouble(sap_amt);
					VCON_SAP_AMT.add(sap_amt);
					VCON_SAP_AMT_USD.add("");
					String diff_amt=nf.format(amt- Double.parseDouble(sap_amt.equals("")?"0":nf.format(Double.parseDouble(sap_amt))));
					VCON_DIFF_AMT.add(diff_amt);
					VCON_ABS_DIFF_AMT.add(nf.format(Double.parseDouble(diff_amt)<0?(Double.parseDouble(diff_amt)*-1):Double.parseDouble(diff_amt)));
				}
				else
				{
					VCON_EMS_AMT_USD.add(nf.format(usd_amt));
					VCON_EMS_AMT.add("");
					String sap_amt = getConGLValue(""+utilBean.RemovePrePaddingZero(""+VCON_GL.elementAt(i)),""+VCON_CURR.elementAt(i));
					sap_amt=sap_amt.equals("")?"":nf.format(Double.parseDouble(sap_amt));
					sapSumUsd+=sap_amt.equals("")?Double.parseDouble("0"):Double.parseDouble(sap_amt);
					VCON_SAP_AMT_USD.add(sap_amt);
					VCON_SAP_AMT.add("");
					String diff_amt=nf.format(usd_amt- Double.parseDouble(sap_amt.equals("")?"0":nf.format(Double.parseDouble(sap_amt))));
					VCON_DIFF_AMT.add(diff_amt);
					VCON_ABS_DIFF_AMT.add(nf.format(Double.parseDouble(diff_amt)<0?(Double.parseDouble(diff_amt)*-1):Double.parseDouble(diff_amt)));
				}
				
			}
			//for totalizer
			amtSum=0;
			amtUsdSum=0;
			deltasapsum=0;
			absDeltaSapSum=0;
			for(int i=0;i<VCON_EMS_AMT.size();i++)
			{
				if(!VCON_EMS_AMT.elementAt(i).equals(""))
				{
					amtSum+=Double.parseDouble(VCON_EMS_AMT.elementAt(i).toString());
				}
				if(!VCON_EMS_AMT_USD.elementAt(i).equals(""))
				{
					amtUsdSum+=Double.parseDouble(VCON_EMS_AMT_USD.elementAt(i).toString());
				}
				if(!VCON_DIFF_AMT.elementAt(i).equals(""))
				{
					deltasapsum+=Double.parseDouble(VCON_DIFF_AMT.elementAt(i).toString());
				}
				if(!VCON_ABS_DIFF_AMT.elementAt(i).equals(""))
				{
					absDeltaSapSum+=Double.parseDouble(VCON_ABS_DIFF_AMT.elementAt(i).toString());
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getConGLValue(String gl,String rate_cd)
	{
		String function_nm="getConGLValue()";
		String val="";
		try
		{
			String co_cd=utilBean.getCompanySAPcode(conn, comp_cd);
			String select_query="";
			if(rate_cd.equals("INR"))
			{
				select_query="SELECT SUM(LC_AMT2) ";
			}
			else if(rate_cd.equals("USD"))
			{
				select_query="SELECT SUM(LC_AMT) ";
			}
				
			String query=select_query+" FROM FMS_SAP_PIVOT_DTL "
					+ "WHERE COCD=? AND GL_CODE=? "
					+ "AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') ";
			if(rate_cd.equals("INR"))
			{
				query+="AND DC_CURR NOT IN 2  ";
			}
			else if(rate_cd.equals("USD"))
			{
				query+="AND DC_CURR IN 2  ";
			}
			
			String temp_query=query;
			stmt6=conn.prepareStatement(temp_query);
			stmt6.setString(1, co_cd);
			stmt6.setString(2, gl);
			stmt6.setString(3, to_dt);
			stmt6.setString(4, from_dt);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				val=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return val;
	}
	
	public void getConsolidateEurDtl()
	{
		String function_nm="getConsolidateEurDtl()";
		try
		{
			String cocd=utilBean.getCompanySAPcode(conn, comp_cd);
			
			String query="SELECT COCD,GL_CODE,'',TO_CHAR(POST_DT,'DD/MM/YYYY'),SUM(LC_AMT2) FROM FMS_SAP_PIVOT_DTL "
					+ "WHERE COCD=? "
					+ "AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(POST_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND DC_CURR IS NULL "
					+ "GROUP BY COCD,GL_CODE,POST_DT ";
			stmt5=conn.prepareStatement(query);
			stmt5.setString(1, cocd);
			stmt5.setString(2, to_dt);
			stmt5.setString(3, from_dt);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String co_cd=rset5.getString(1)==null?"":rset5.getString(1);
				String gl_cd=rset5.getString(2)==null?"":rset5.getString(2);
				String curr=rset5.getString(3)==null?"":rset5.getString(3);
				String post_dt=rset5.getString(4)==null?"":rset5.getString(4);
				String sap_val=rset5.getString(5)==null?"":nf.format(rset5.getDouble(5));
				
				String period =from_dt.split("/")[2]+from_dt.split("/")[1];
				
				VCON_COCD.add(""+utilBean.getCompanySAPcode(conn, comp_cd));
				VCON_PERIOD.add(period);
				VCON_GL.add(utilBean.PrePaddingZero(gl_cd, 10));
				VCON_GL_DECR.add(""+utilBean.getGLdesc(conn, gl_cd));
				VCON_CURR.add(curr);
				VCON_YEAR.add(from_dt.split("/")[2]);
				VCON_MONTH.add(from_dt.split("/")[1]);
				

				VCON_EMS_AMT.add("");
				VCON_EMS_AMT_USD.add("");
				sapsum+=sap_val.equals("")?Double.parseDouble("0"):Double.parseDouble(sap_val);
				VCON_SAP_AMT.add(sap_val);
				VCON_SAP_AMT_USD.add("");
				String diff_amt=nf.format(0- Double.parseDouble(sap_val.equals("")?"0":nf.format(Double.parseDouble(sap_val))));
				VCON_DIFF_AMT.add(diff_amt);
				VCON_ABS_DIFF_AMT.add(nf.format(Double.parseDouble(diff_amt)<0?(Double.parseDouble(diff_amt)*-1):Double.parseDouble(diff_amt)));
				
			}
			rset5.close();
			stmt5.close();
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
	String from_dt = "";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt = "";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	String counterparty_cd = "";
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	String entity_role="";
	public void setEntity_role(String entity_role) {this.entity_role=entity_role;}
	String gl_cd="";
	public void setGl_cd(String gl_cd) {this.gl_cd=gl_cd;}
	String tds_flag="";
	public void setTds_flag(String tds_flag) {this.tds_flag=tds_flag;}
	String tcs_flag="";
	public void setTcs_flag(String tcs_flag) {this.tcs_flag=tcs_flag;}
	String tax_flag="";
	public void setTax_flag(String tax_flag) {this.tax_flag=tax_flag;}
	String month="";
	public void setMonth(String month) {this.month=month;}
	String year="";
	public void setYear(String year) {this.year=year;}
	
	String bu_region = "";
	public String getBu_Region() {return bu_region;}
	String comp_abbr = "";
	public String getComp_abbr() {return comp_abbr;}
	
	double amtUsdSum=0;
	double amtSum=0;
	double qtySum=0;
	double sapsum=0;
	double deltasapsum=0;
	double sapSumUsd=0;
	double deltaSapSumUsd=0;
	double absDeltaSapSum=0;
	
	String amt_usd_sum="";
	public String getAmt_usd_sum() {return amt_usd_sum;}
	String amt_sum="";
	public String getAmt_sum() {return amt_sum;}
	String qty_sum="";
	public String getQty_sum() {return qty_sum;}
	String sap_sum="";
	public String getSap_sum() {return sap_sum;}
	String delta_sap_sum="";
	public String getDelta_Sap_Sum() {return delta_sap_sum;}
	String sap_sum_usd="";
	public String getSap_Sum_Usd() {return sap_sum_usd;}
	String delta_sap_sum_usd="";
	public String getDelta_Sap_Sum_Usd() {return delta_sap_sum_usd;}
	String abs_delta_sap_sum="";
	public String getAbs_delta_sap_sum() {return abs_delta_sap_sum;}
	
	String firstDtOfPrevMonth = "";
	String lastDtOfPrevMonth = "";
	String ext_gl_mapping="";	
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	
	//PB 20250503 for SAP RECON REPORT
	Vector VMST_GL_DESC = new Vector();
	Vector VMST_GL_CD = new Vector();
	
	Vector VCO_CD = new Vector();
	Vector VDEAL_MAP = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VACCOUNT_PERIOD_MONTH = new Vector();
	Vector VACCOUNT_PERIOD_YR = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VGL_CD = new Vector();	//for storing GL  code
	Vector VGL_DESC = new Vector();
	Vector VVENDOR_CD = new Vector();
	Vector VCURRENCY = new Vector();
	Vector VDOC_TYPE = new Vector();
	Vector VDOC_NO = new Vector();
	Vector VPK = new Vector();
	Vector VITEMTEXT = new Vector();
	Vector VTRANS_TYPE = new Vector();
	Vector VSAP_APPROVED_BY = new Vector();
	Vector VDOC_DT = new Vector();
	Vector VPOST_DT = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VAMT = new Vector();
	Vector VUSD_AMT = new Vector();
	Vector VSAP_VALUE_INR = new Vector();
	Vector VSAP_VALUE_USD = new Vector();
	Vector VDELTA_SAP_VALUE_INR = new Vector();
	Vector VDELTA_SAP_VALUE_USD = new Vector();
	Vector VVENDOR_NM = new Vector();
	Vector VDERV_INV_TYPE=new Vector();
	Vector VIS_SAP_DOC_DATA = new Vector();
	
	Vector VCON_GL = new Vector();
	Vector VCON_CURR = new Vector();
	Vector VCON_COCD = new Vector();
	Vector VCON_PERIOD = new Vector();
	Vector VCON_GL_DECR = new Vector();
	Vector VCON_YEAR = new Vector();
	Vector VCON_MONTH = new Vector();
	Vector VCON_EMS_AMT = new Vector();
	Vector VCON_EMS_AMT_USD = new Vector();
	Vector VCON_SAP_AMT = new Vector();
	Vector VCON_SAP_AMT_USD = new Vector();
	Vector VCON_DIFF_AMT = new Vector();
	Vector VCON_ABS_DIFF_AMT = new Vector();
	
	Vector V_BU_ABBR = new Vector();
	Vector V_PLANT_ABBR = new Vector();
	Vector V_FMS_REF = new Vector();
	Vector V_POST_STATUS = new Vector();
	Vector V_POST_DT = new Vector();
	Vector V_POST_TIME = new Vector();
	Vector V_IDOC_NO = new Vector();
	Vector V_IDOC_STATUS = new Vector();
	Vector V_STATUS_MSG = new Vector();
	Vector V_DOC_NO = new Vector();
	Vector V_COMPANY_CODE = new Vector();
	Vector V_FISCAL_YR = new Vector();
	Vector V_MSG_STATUS = new Vector();
	Vector V_CONT_NO = new Vector();
	Vector V_DUE_DT = new Vector();
	Vector V_APPROVE_DT = new Vector();
	Vector V_APPROVED_BY = new Vector();
	Vector V_APPROVED_FLAG = new Vector();
	Vector V_NET_PAYABLE_AMT = new Vector();
	Vector V_SAP_POSTED = new Vector();
	Vector VINDEX = new Vector();
	Vector VSAP_INDEX = new Vector();
	
	Vector VACC_BU_ABBR = new Vector();
	Vector VACC_PLANT_ABBR = new Vector();
	Vector VACC_FMS_REF = new Vector();
	Vector VACC_POST_STATUS = new Vector();
	Vector VACC_POST_DT = new Vector();
	Vector VACC_POST_TIME = new Vector();
	Vector VACC_IDOC_NO = new Vector();
	Vector VACC_IDOC_STATUS = new Vector();
	Vector VACC_STATUS_MSG = new Vector();
	Vector VACC_DOC_NO = new Vector();
	Vector VACC_COMPANY_CODE = new Vector();
	Vector VACC_FISCAL_YR = new Vector();
	Vector VACC_MSG_STATUS = new Vector();
	Vector VACC_CONT_NO = new Vector();
	Vector VACC_DUE_DT = new Vector();
	Vector VACC_APPROVE_DT = new Vector();
	Vector VACC_APPROVED_BY = new Vector();
	Vector VACC_APPROVED_FLAG = new Vector();
	Vector VACC_NET_PAYABLE_AMT = new Vector();
	Vector VACC_SAP_POSTED = new Vector();
	Vector VACC_INDEX = new Vector();
	Vector VACC_SAP_INDEX = new Vector();
	
	Vector VMTM_BU_ABBR = new Vector();
	Vector VMTM_PLANT_ABBR = new Vector();
	Vector VMTM_FMS_REF = new Vector();
	Vector VMTM_POST_STATUS = new Vector();
	Vector VMTM_POST_DT = new Vector();
	Vector VMTM_POST_TIME = new Vector();
	Vector VMTM_IDOC_NO = new Vector();
	Vector VMTM_IDOC_STATUS = new Vector();
	Vector VMTM_STATUS_MSG = new Vector();
	Vector VMTM_DOC_NO = new Vector();
	Vector VMTM_COMPANY_CODE = new Vector();
	Vector VMTM_FISCAL_YR = new Vector();
	Vector VMTM_MSG_STATUS = new Vector();
	Vector VMTM_CONT_NO = new Vector();
	Vector VMTM_DUE_DT = new Vector();
	Vector VMTM_APPROVE_DT = new Vector();
	Vector VMTM_APPROVED_BY = new Vector();
	Vector VMTM_APPROVED_FLAG = new Vector();
	Vector VMTM_NET_PAYABLE_AMT = new Vector();
	Vector VMTM_SAP_POSTED = new Vector();
	Vector VMTM_INDEX = new Vector();
	Vector VMTM_SAP_INDEX = new Vector();
	Vector VMTM_SAP_INTERFACE_DISPLAY = new Vector();
	Vector VMTM_TITLE = new Vector();
	
	Vector VSAP_INTERFACE_DISPLAY = new Vector();
	Vector VTITLE = new Vector();
	Vector VACC_SAP_INTERFACE_DISPLAY = new Vector();
	Vector VACC_TITLE = new Vector();
	
	Vector VCONTRACT_TYPE_MST = new Vector();
	Vector VCONTRACT_TYPE_NM_MST = new Vector();
	
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VBA_CODE = new Vector();
	Vector VBA_DESC = new Vector();
	

	Vector VBUKRS = new Vector();
	Vector VKUNNR = new Vector();
	Vector VBELNR = new Vector();
	Vector VGJAHR = new Vector();
	Vector VBLDAT = new Vector();
	Vector VSGTXT = new Vector();
	Vector VSHKZG = new Vector();
	Vector VWRBTR = new Vector();
	Vector VWAERS = new Vector();
	Vector VDMBTR = new Vector();
	Vector VHWAER = new Vector();
	Vector VZUKEY = new Vector();
	Vector VZDTYP = new Vector();
	Vector VHKONT = new Vector();
	Vector VAUGBL = new Vector();
	Vector VXBLNR = new Vector();
	Vector VRSTGR = new Vector();
	Vector VAUGDT = new Vector();
	Vector VBLART = new Vector();
	Vector VZUONR = new Vector();
	Vector VNAME = new Vector();
	
	public Vector getVBUKRS() { return VBUKRS; }
	public Vector getVKUNNR() { return VKUNNR; }
	public Vector getVBELNR() { return VBELNR; }
	public Vector getVGJAHR() { return VGJAHR; }
	public Vector getVBLDAT() { return VBLDAT; }
	public Vector getVSGTXT() { return VSGTXT; }
	public Vector getVSHKZG() { return VSHKZG; }
	public Vector getVWRBTR() { return VWRBTR; }
	public Vector getVWAERS() { return VWAERS; }
	public Vector getVDMBTR() { return VDMBTR; }
	public Vector getVHWAER() { return VHWAER; }
	public Vector getVZUKEY() { return VZUKEY; }
	public Vector getVZDTYP() { return VZDTYP; }
	public Vector getVHKONT() { return VHKONT; }
	public Vector getVAUGBL() { return VAUGBL; }
	public Vector getVXBLNR() { return VXBLNR; }
	public Vector getVRSTGR() { return VRSTGR; }
	public Vector getVAUGDT() { return VAUGDT; }
	public Vector getVBLART() { return VBLART; }
	public Vector getVZUONR() { return VZUONR; }
	public Vector getVNAME() { return VNAME; }
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	
	public Vector getVCOUNTERPARTY_CD(){return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM(){return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR(){return VCOUNTERPARTY_ABBR;}
	
	public Vector getVMST_GL_CD() {return VMST_GL_CD;}
	public Vector getVMST_GL_DESC() {return VMST_GL_DESC;}
	
	public Vector getVCO_CD() {return VCO_CD;}
	public Vector getVDEAL_MAP() {return VDEAL_MAP;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVACCOUNT_PERIOD_MONTH() {return VACCOUNT_PERIOD_MONTH;}
	public Vector getVACCOUNT_PERIOD_YR() {return VACCOUNT_PERIOD_YR;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVGL_CD() {return VGL_CD;}
	public Vector getVGL_DESC() {return VGL_DESC;}
	public Vector getVVENDOR_CD() {return VVENDOR_CD;}
	public Vector getVCURRENCY() {return VCURRENCY;}
	public Vector getVDOC_TYPE() {return VDOC_TYPE;}
	public Vector getVDOC_NO() {return VDOC_NO;}
	public Vector getVPK() {return VPK;}
	public Vector getVITEMTEXT() {return VITEMTEXT;}
	public Vector getVTRANS_TYPE() {return VTRANS_TYPE;}
	public Vector getVSAP_APPROVED_BY() {return VSAP_APPROVED_BY;}
	public Vector getVDOC_DT() {return VDOC_DT;}
	public Vector getVPOST_DT() {return VPOST_DT;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVAMT() {return VAMT;}
	public Vector getVUSD_AMT() {return VUSD_AMT;}
	public Vector getVSAP_VALUE_INR() {return VSAP_VALUE_INR;}
	public Vector getVSAP_VALUE_USD() {return VSAP_VALUE_USD;}
	public Vector getVDELTA_SAP_VALUE_INR() {return VDELTA_SAP_VALUE_INR;}
	public Vector getVDELTA_SAP_VALUE_USD() {return VDELTA_SAP_VALUE_USD;}
	public Vector getVVENDOR_NM() {return VVENDOR_NM;}
	public Vector getVIS_SAP_DOC_DATA() {return VIS_SAP_DOC_DATA;}
	
	public Vector getVCON_GL() {return VCON_GL;};
	public Vector getVCON_CURR() {return VCON_CURR;}
	public Vector getVCON_COCD() {return VCON_COCD;}
	public Vector getVCON_PERIOD() {return VCON_PERIOD;}
	public Vector getVCON_GL_DECR() {return VCON_GL_DECR;}
	public Vector getVCON_YEAR() {return VCON_YEAR;}
	public Vector getVCON_MONTH() {return VCON_MONTH;}
	public Vector getVCON_EMS_AMT() {return VCON_EMS_AMT;}
	public Vector getVCON_EMS_AMT_USD() {return VCON_EMS_AMT_USD;}
	public Vector getVCON_SAP_AMT() {return VCON_SAP_AMT;}
	public Vector getVCON_SAP_AMT_USD() {return VCON_SAP_AMT_USD;}
	public Vector getVCON_DIFF_AMT() {return VCON_DIFF_AMT;}
	public Vector getVCON_ABS_DIFF_AMT() {return VCON_ABS_DIFF_AMT;}
	
	public Vector getV_BU_ABBR(){return V_BU_ABBR;}
	public Vector getV_PLANT_ABBR(){return V_PLANT_ABBR; }
	public Vector getV_FMS_REF(){return V_FMS_REF;}
	public Vector getV_POST_STATUS(){return V_POST_STATUS;}
	public Vector getV_POST_DT(){return V_POST_DT;}
	public Vector getV_POST_TIME(){return V_POST_TIME; }
	public Vector getV_IDOC_NO(){return V_IDOC_NO;}
	public Vector getV_IDOC_STATUS(){return V_IDOC_STATUS;}
	public Vector getV_STATUS_MSG(){return V_STATUS_MSG;}
	public Vector getV_DOC_NO(){return V_DOC_NO;}
	public Vector getV_COMPANY_CODE(){return V_COMPANY_CODE;}
	public Vector getV_FISCAL_YR(){return V_FISCAL_YR;}
	public Vector getV_MSG_STATUS(){return V_MSG_STATUS;}
	public Vector getV_CONT_NO(){return V_CONT_NO;}
	public Vector getV_DUE_DT(){return V_DUE_DT;}
	public Vector getV_APPROVE_DT(){return V_APPROVE_DT;}
	public Vector getV_APPROVED_BY(){return V_APPROVED_BY;}
	public Vector getV_APPROVED_FLAG(){return V_APPROVED_FLAG;}
	public Vector getV_NET_PAYABLE_AMT(){return V_NET_PAYABLE_AMT;}
	public Vector getV_SAP_POSTED(){return V_SAP_POSTED;}
	public Vector getVINDEX(){return VINDEX;}
	public Vector getVSAP_INDEX() {return VSAP_INDEX;}
	
	public Vector getVACC_BU_ABBR(){return VACC_BU_ABBR;}
	public Vector getVACC_PLANT_ABBR(){return VACC_PLANT_ABBR; }
	public Vector getVACC_FMS_REF(){return VACC_FMS_REF;}
	public Vector getVACC_POST_STATUS(){return VACC_POST_STATUS;}
	public Vector getVACC_POST_DT(){return VACC_POST_DT;}
	public Vector getVACC_POST_TIME(){return VACC_POST_TIME; }
	public Vector getVACC_IDOC_NO(){return VACC_IDOC_NO;}
	public Vector getVACC_IDOC_STATUS(){return VACC_IDOC_STATUS;}
	public Vector getVACC_STATUS_MSG(){return VACC_STATUS_MSG;}
	public Vector getVACC_DOC_NO(){return VACC_DOC_NO;}
	public Vector getVACC_COMPANY_CODE(){return VACC_COMPANY_CODE;}
	public Vector getVACC_FISCAL_YR(){return VACC_FISCAL_YR;}
	public Vector getVACC_MSG_STATUS(){return VACC_MSG_STATUS;}
	public Vector getVACC_CONT_NO(){return VACC_CONT_NO;}
	public Vector getVACC_DUE_DT(){return VACC_DUE_DT;}
	public Vector getVACC_APPROVE_DT(){return VACC_APPROVE_DT;}
	public Vector getVACC_APPROVED_BY(){return VACC_APPROVED_BY;}
	public Vector getVACC_APPROVED_FLAG(){return VACC_APPROVED_FLAG;}
	public Vector getVACC_NET_PAYABLE_AMT(){return VACC_NET_PAYABLE_AMT;}
	public Vector getVACC_SAP_POSTED(){return VACC_SAP_POSTED;}
	public Vector getVACC_INDEX(){return VACC_INDEX;}
	public Vector getVACC_SAP_INDEX() {return VACC_SAP_INDEX;}
	
	public Vector getVMTM_BU_ABBR(){return VMTM_BU_ABBR;}
	public Vector getVMTM_PLANT_ABBR(){return VMTM_PLANT_ABBR; }
	public Vector getVMTM_FMS_REF(){return VMTM_FMS_REF;}
	public Vector getVMTM_POST_STATUS(){return VMTM_POST_STATUS;}
	public Vector getVMTM_POST_DT(){return VMTM_POST_DT;}
	public Vector getVMTM_POST_TIME(){return VMTM_POST_TIME; }
	public Vector getVMTM_IDOC_NO(){return VMTM_IDOC_NO;}
	public Vector getVMTM_IDOC_STATUS(){return VMTM_IDOC_STATUS;}
	public Vector getVMTM_STATUS_MSG(){return VMTM_STATUS_MSG;}
	public Vector getVMTM_DOC_NO(){return VMTM_DOC_NO;}
	public Vector getVMTM_COMPANY_CODE(){return VMTM_COMPANY_CODE;}
	public Vector getVMTM_FISCAL_YR(){return VMTM_FISCAL_YR;}
	public Vector getVMTM_MSG_STATUS(){return VMTM_MSG_STATUS;}
	public Vector getVMTM_CONT_NO(){return VMTM_CONT_NO;}
	public Vector getVMTM_DUE_DT(){return VMTM_DUE_DT;}
	public Vector getVMTM_APPROVE_DT(){return VMTM_APPROVE_DT;}
	public Vector getVMTM_APPROVED_BY(){return VMTM_APPROVED_BY;}
	public Vector getVMTM_APPROVED_FLAG(){return VMTM_APPROVED_FLAG;}
	public Vector getVMTM_NET_PAYABLE_AMT(){return VMTM_NET_PAYABLE_AMT;}
	public Vector getVMTM_SAP_POSTED(){return VMTM_SAP_POSTED;}
	public Vector getVMTM_INDEX(){return VMTM_INDEX;}
	public Vector getVMTM_SAP_INDEX() {return VMTM_SAP_INDEX;}
	public Vector getVMTM_SAP_INTERFACE_DISPLAY() {return VMTM_SAP_INTERFACE_DISPLAY;}
	public Vector getVMTM_TITLE() {return VMTM_TITLE;}
	
	public Vector getVSAP_INTERFACE_DISPLAY() {return VSAP_INTERFACE_DISPLAY;}
	public Vector getVTITLE() {return VTITLE;}
	public Vector getVACC_SAP_INTERFACE_DISPLAY() {return VACC_SAP_INTERFACE_DISPLAY;}
	public Vector getVACC_TITLE() {return VACC_TITLE;}
	
	public Vector getVCONTRACT_TYPE_MST() {return VCONTRACT_TYPE_MST;}
	public Vector getVCONTRACT_TYPE_NM_MST() {return VCONTRACT_TYPE_NM_MST;}
	
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVBA_CODE() {return VBA_CODE;}
	public Vector getVBA_DESC() {return VBA_DESC;}
}