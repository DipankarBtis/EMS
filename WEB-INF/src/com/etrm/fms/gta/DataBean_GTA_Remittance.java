package com.etrm.fms.gta;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 14/06/2023
//Status	  		: Developing
public class DataBean_GTA_Remittance 
{
	String db_src_file_name="DataBean_GTA_Remittance.java";
	
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset_temp;
	ResultSet rset_temp1;
	//String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString_temp1="";

	Vector VTEMP_INV_COMPONENT = new Vector();
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	int inv_index=0;
	int freez_count=0;
	int inv_grp_index=0;
	
	String filter_contType="";

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
	    			if(callFlag.equalsIgnoreCase("GTA_REMITTANCE_PREPARATION_LIST"))
	    			{
	    				getFiltersForInvoice();
	    				
	    				getInvoiceType();
	    				for(int i=0;i<VINVOICE_TYPE.size();i++)
	    				{
	    					inv_index=0;
	    					invoice_type=""+VINVOICE_TYPE.elementAt(i);
	    					filter_contType="'R','C'";
	    					if(invoice_type.equals("TC"))
	    					{
	    						inv_title=tc_inv_title;
	    					}
	    					else if(invoice_type.equals("IC"))
	    					{
	    						inv_title=ic_inv_title;
	    					}
	    					else if(invoice_type.equals("PC"))
	    					{
	    						filter_contType="'K'";
	    						inv_title=pc_inv_title;
	    					}
	    					
	    					if(billing_cycle.equals("0"))
		    				{
		    					forAllBillingOption();
		    				}
		    				else
		    				{
		    					getBillingCyclePeriod();
		    					getGTARemittancePreparationList();
		    				}
		    				VINV_INDEX.add(inv_index);
	    				}
	    				
	    				if(billing_cycle.equals("0"))
	    				{
	    					forAllBillingOptionFFLOW();
	    				}
	    				else
	    				{
	    					getBillingCyclePeriod();
	    					getGTAOtherInvoiceList();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("F_FLOW_INVOICE"))
	    			{
	    				getBillingCyclePeriod();
	    				getTransporterCounterpartyList();
	    				getFilterContractList();
	    				getContractList();
	    				getPlantDetail();
	    				getAddressType();
	    				//getOtherInvoiceContactPerson();
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
	    			else if(callFlag.equalsIgnoreCase("GTA_SELLER_PAYMENT_DETAIL"))
	    			{
	    				getContractDetail();
	    				getBuContactPerson();
	    				InvoiceCalculation();
	    				PartyInvoiceCalculation();
	    				//storeInvoiceDataIntoHashMap();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_INVOICE_DETAIL"))
	    			{
	    				getInvoiceDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_FINAL_PRINT_INVOICE_PDF"))
	    			{
	    				getExistingInvoiceDataForPDFPrint();
	    				storeInvoiceDataIntoHashMap();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_SEND_INVOICE_MAIL"))
	    			{
	    				getGtaSendInvoiceMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ATTACHMENT2"))
	    			{
	    				counterparty_name=utilBean.getCounterpartyName(conn,counterparty_cd);
	    				getAttachment2Detail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_REMITTANCE_FO_APPROVAL"))
	    			{
	    				getSegment();
	    				getGTAInvoiceApproval();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_SAP_XML"))
	    			{
	    				generateGTAInvoiceXML();
	    				parseSAP_XMLfile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("GENERATE_GTA_SAP_XML"))
	    			{
	    				generateGTAInvoiceXML();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PARSE_SAP_XML"))
	    			{
	    				//getSapInvoiceApprovalDetail();
	    				parseSAP_XMLfile();
	    				getPostingDetail(); //PB20250606
	    			}
	    			else if(callFlag.equalsIgnoreCase("GTA_ACCRUAL_ACCOUNTING"))
	    			{
	    				tot_accrual_amt=0;
	    				tot_accrual_mmbtu=0;
	    				tot_accrual_amt_usd=0;
	    				tot_accrual_tax_amt=0;
	    				tot_total_accrual_amt=0;
	    				
	    				String queryString="SELECT COUNT(*) "
	    						+ "FROM FMS_GTA_ACCRUAL_DTL "
	    						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
	    				stmt = conn.prepareStatement(queryString);
	    				stmt.setString(1, comp_cd);
	    				stmt.setString(2, report_dt);
	    				rset=stmt.executeQuery();
	    				if(rset.next())
	    				{
	    					freez_count=rset.getInt(1);
	    				}
	    				rset.close();
	    				stmt.close();
	    				
	    				if(freez_count>0)
	    				{
	    					getAccrualFreezedData();
	    					isFreezed="Y";
	    				}
	    				else
	    				{
	    					isFreezed="N";
		    				//if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
		    				{
		    					//getInvoiceType();
		    					getAccrualInvoiceType();
			    				for(int i=0;i<VINVOICE_TYPE.size();i++)
			    				{
			    					invoice_type=""+VINVOICE_TYPE.elementAt(i);
			    					forAllBillingOptionForAccrual();
			    				}
		    				}
		    				if(automation_flag.equals("Y"))
		    				{
		    					FreezAccrualData();
		    					if(isGenerateXML.equals("Y"))
		    					{
		    						generateGTAAccrualXML();
		    					}
		    				}
	    				}
	    				
	    				str_tot_accrual_mmbtu=nf.format(tot_accrual_mmbtu);
	    				str_tot_accrual_amt=nf.format(tot_accrual_amt);
	    				str_tot_accrual_amt_usd=nf.format(tot_accrual_amt_usd);
	    				str_accrual_tax_amt=nf.format(tot_accrual_tax_amt);
	    				str_total_accrual_amt=nf.format(tot_total_accrual_amt);
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
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}

	public void getFiltersForInvoice()
	{
		String function_nm="getFiltersForInvoice()";
		
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			String queryString="SELECT DISTINCT A.COUNTERPARTY_CD "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE A.COMPANY_CD=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, temp_period_end_dt);
			stmt.setString(3, temp_period_start_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String trans_cd=rset.getString(1)==null?"":rset.getString(1);
				VFILTER_TRANS_CD.add(trans_cd);
				VFILTER_TRANS_NAME.add(utilBean.getCounterpartyName(conn, trans_cd));
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, filter_trans_cd);
			stmt1.setString(3, temp_period_end_dt);
			stmt1.setString(4, temp_period_start_dt);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String agmt_no=rset1.getString(2)==null?"":rset1.getString(2);
				String agmt_rev=rset1.getString(3)==null?"":rset1.getString(3);
				String cont_no=rset1.getString(4)==null?"":rset1.getString(4);
				String cont_rev=rset1.getString(5)==null?"":rset1.getString(5);
				String st_dt=rset1.getString(6)==null?"":rset1.getString(6);
				String ed_dt=rset1.getString(7)==null?"":rset1.getString(7);
				String cont_ref=rset1.getString(9)==null?"":rset1.getString(9);
				String cont_type=rset1.getString(10)==null?"":rset1.getString(10);
				
				String deal_no = utilBean.NewDealMappingId(comp_cd, filter_trans_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, "");
				if(!cont_ref.equals(""))
				{
					deal_no+=" ["+cont_ref+"]";
				}
				deal_no+=" ["+st_dt+"-"+ed_dt+"]";
				
				String mapping_id=cont_type+"-"+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;

				VFILTER_DEAL_NO.add(deal_no);
				VFILTER_MAPPING_ID.add(mapping_id);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGTAOtherInvoiceList()
	{
		String function_nm="getGTAOtherInvoiceList()";
		
		try
		{

			int index=0;
			int cont=0;
			String inv_title="";
			
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
						+ "ADDR_FLAG,BU_UNIT,INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_REF,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,"
						+ "PDF_INV_DTL,SAP_APPROVAL,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_GTA_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? "
						//+ "AND FREQ=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			//stmt.setString(2, billing_cycle);
			stmt.setString(2, temp_period_start_dt);
			stmt.setString(3, temp_period_end_dt);
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
				String add_flag=rset.getString(8)==null?"":rset.getString(8);
				//String deal_no=cont_type+agmtno+"-"+contno;
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
				
				String bu_plant_seq = rset.getString(9)==null?"":rset.getString(9);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String gta_countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String gta_countpty_abbr = utilBean.getGasExchangeAbbr(conn,gta_countpty_cd); 
				
				String gta_bu_plant_seq="";
				String gta_bu_plant_abbr="";
				if(add_flag.equals("R"))
				{
					gta_bu_plant_seq=add_flag;
					gta_bu_plant_abbr="Registered";
				}
				else if(add_flag.equals("C"))
				{
					gta_bu_plant_seq=add_flag;
					gta_bu_plant_abbr="Correspondence";
				}
				else if(add_flag.equals("B"))
				{
					gta_bu_plant_seq=add_flag;
					gta_bu_plant_abbr="Billing";
				}
				else if(!add_flag.equals("") && add_flag.startsWith("B"))
				{
					gta_bu_plant_seq=add_flag.substring(1,add_flag.length());
					gta_bu_plant_abbr=utilBean.getCounterpartyBuPlantABBR(conn,countpty_cd, own_cd, gta_bu_plant_seq, "R");
				}
				
				VOTH_TRANS_BU_SEQ.add(gta_bu_plant_seq);
				VOTH_TRANS_BU_ABBR.add(gta_bu_plant_abbr);
				
				VOTH_COUNTERPTY_CD.add(countpty_cd);
				VOTH_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VOTH_GTA_COUNTERPTY_CD.add(gta_countpty_cd);
				//VOTH_GTA_COUNTERPTY_ABBR.add(gta_countpty_abbr);
				VOTH_AGMT_NO.add(agmtno);
				VOTH_AGMT_REV_NO.add(agmtrev);
				VOTH_CONT_NO.add(contno);
				VOTH_CONT_REV_NO.add(contrev);
				VOTH_CONTRACT_TYPE.add(cont_type);
				VOTH_ADDR_FLAG.add(add_flag);
				VOTH_DEAL_NO.add(deal_no);

				VOTH_BU_PLANT_SEQ.add(bu_plant_seq);
				VOTH_BU_PLANT_ABBR.add(bu_plant_abbr);
				
				String max_contrev="0";
				
				queryString1="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CT_REF_NO,CONT_REV "
						+ "FROM FMS_GTA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE B.COMPANY_CD=A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmtno);
				stmt1.setString(4, agmtrev);
				stmt1.setString(5, contno);
				stmt1.setString(6, cont_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VOTH_START_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VOTH_END_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VOTH_CONT_NAME.add(rset1.getString(3)==null?"":rset1.getString(3));
					
					String cont_ref_no=rset1.getString(4)==null?"":rset1.getString(4);
					String trade_ref_no=rset1.getString(5)==null?"":rset1.getString(5);
					max_contrev=rset1.getString(6)==null?"0":rset1.getString(6);
					
					if(cont_type.equals("I") || cont_type.equals("X"))
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
				
				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+max_contrev;
				VMAPPING_ID.add(mapping_id);
				
				String p_st_dt=rset.getString(20)==null?"":rset.getString(20);
				String p_end_dt=rset.getString(21)==null?"":rset.getString(21);
				String inv_dt=rset.getString(22)==null?"":rset.getString(22);
				VOTH_PERIOD_START_DT.add(p_st_dt);
				VOTH_PERIOD_END_DT.add(p_end_dt);
				VOTH_INVOICE_DT.add(inv_dt);
				
				VOTH_BILLING_FREQ_FLAG.add(billing_cycle);
				VOTH_BILLING_FREQ_NM.add(billing_freq_nm);
				
				String inv_no="";
				String sts="";
				String aprove="N";
				String check="N";
				String auth="N";
				String is_submitted="N";
				String approve_inv_flag="";
				String pdf_inv_flag="N";

				String chk_flg = rset.getString(11)==null?"":rset.getString(11);
				String auth_flg = rset.getString(12)==null?"":rset.getString(12);
				String aprv_flg = rset.getString(13)==null?"":rset.getString(13);
				String inv_ref=rset.getString(14)==null?"":rset.getString(14);
				
				String pdf_inv_dtl = rset.getString(18)==null?"":rset.getString(18);
				String sap_approved_flag = rset.getString(19)==null?"":rset.getString(19);
				
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

				inv_no=rset.getString(10)==null?"":rset.getString(10);

				sts=""+getInvoiceStatus(chk_flg, auth_flg, aprv_flg);

				VOTH_REMITTANCE_NO.add(inv_no);
				VOTH_INVOICE_NO.add(inv_ref);
				VOTH_STATUS.add(sts);
				
				VOTH_APPROVE_INVOICE_FLAG.add(approve_inv_flag);
				VOTH_PDF_INV_FLAG.add(pdf_inv_flag);
				VOTH_SAP_APPROVAL_FLAG.add(sap_approved_flag);
				VOTH_TYPE_FLAG.add("FF");
				
				VOTH_APPROVE_FLAG_CHECK.add(aprove);
				VOTH_CHECK_FLAG_CHECK.add(check);
				VOTH_AUTHORIZ_FLAG_CHECK.add(auth);
				VOTH_IS_SUBMITTED.add(is_submitted);
				
				String inv_seq=rset.getString(15)==null?"":rset.getString(15);
				String fin_yr=rset.getString(16)==null?"":rset.getString(16);
				String inv_type=rset.getString(17)==null?"":rset.getString(17);
				
				VOTH_INVOICE_SEQ.add(inv_seq);
				VOTH_FINANCIAL_YEAR.add(fin_yr);
				VOTH_INVOICE_TYPE.add(inv_type);

				int upload_count=0;
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_GTA_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		//+ "AND TRANS_COUNTERPARTY_CD=? "
	 	        		+ "AND INV_TITLE=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, own_cd);
				stmt3.setString(2, cont_type);
				stmt3.setString(3, inv_type);
				stmt3.setString(4, inv_seq);
				stmt3.setString(5, fin_yr);
				//stmt3.setString(6, gta_countpty_cd);
				stmt3.setString(6, "PG_RECV");
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					upload_count=rset3.getInt(1);
				}
				rset3.close();
				stmt3.close();
				
				queryString3="SELECT FILE_NAME "
						+ "FROM FMS_GTA_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		//+ "AND TRANS_COUNTERPARTY_CD=? "
	 	        		+ "AND INV_TITLE=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, own_cd);
				stmt3.setString(2, cont_type);
				stmt3.setString(3, inv_type);
				stmt3.setString(4, inv_seq);
				stmt3.setString(5, fin_yr);
				//stmt3.setString(6, gta_countpty_cd);
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
	
	public void getExixtingF_FLowInvoice()
	{
		String function_nm="getExixtingF_FLowInvoice()";
		
		try
		{
			String queryString="SELECT INVOICE_SEQ, INVOICE_NO, INVOICE_REF, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "INVOICE_CATEGORY, INVOICE_TYPE, "
					+ "FINANCIAL_YEAR, NUM_LINE, LINKED_INVOICE, NOTE, "
					+ "'',BU_CONTACT_PERSON_CD, "
					+ "CHECKED_FLAG, CHECKED_BY, TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "AUTHORIZED_FLAG, AUTHORIZED_BY, TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "APPROVED_FLAG, APPROVED_BY, TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "OTHER_INV_STR,GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INVOICE_RAISED_IN,AMT_WORD,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_AMT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_AMT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_TDS,ALLOC_QTY,SUB_INV_TYPE "
					+ "FROM FMS_GTA_FFLOW_INV_MST "
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
				//contact_person_cd=rset.getString(12)==null?"0":rset.getString(12);
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
				sub_inv_type =rset.getString(43)==null?"":rset.getString(43);
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				tcs_struct_info=utilBean.getTaxDescr(conn,tcs_struct_cd);
				tds_struct_info=utilBean.getTaxDescr(conn,tds_struct_cd);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				queryString4="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_GTA_FFLOW_INV_TAX_DTL "
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
					+ "FROM FMS_GTA_FFLOW_INV_DTL "
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
	
	public void getF_FlowInvoice()
	{
		String function_nm="getF_FlowInvoice()";
		try
		{
			/*queryString="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
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
			stmt.close();
			*/
			
			String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,"R",address_type, "RM", "RLNG","Y");
			contact_person_cd=temp_contact_person_cd.equals("")?"0":temp_contact_person_cd;

			/*queryString1="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, "B");
			stmt1.setString(4, "B"+bu_unit);//ASK
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
			
			/*queryString2 = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), -3)) "
					+ " || '-' || "
					+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), 9)) "
					+ "FROM DUAL";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, period_end_dt);
			stmt2.setString(2, period_end_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				financial_year=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();

			String fin_yr="";
			if(!financial_year.equals(""))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}

			queryString3="SELECT NVL(MAX(INVOICE_SEQ),0) FROM "
					+ "((SELECT INVOICE_SEQ FROM FMS_GTA_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? ) "
					+ "UNION ALL "
					+ "(SELECT INVOICE_SEQ FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? )) "
					+ "COMBINED";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, financial_year);
			stmt3.setString(3, invoice_type);
			stmt3.setString(4, contract_type);
			stmt3.setString(5, comp_cd);
			stmt3.setString(6, financial_year);
			stmt3.setString(7, invoice_type);
			stmt3.setString(8, contract_type);
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
			String queryString="SELECT INVOICE_NO "
					+ "FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=?";
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
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VLINK_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();

			queryString1="SELECT INVOICE_NO "
					+ "FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=?";
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
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				VLINK_INVOICE_NO.add(rset1.getString(1)==null?"":rset1.getString(1));
			}
			rset1.close();
			stmt1.close();

			queryString2="SELECT INVOICE_NO "
					+ "FROM FMS_GTA_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=?";
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
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				VLINK_INVOICE_NO.add(rset2.getString(1)==null?"":rset2.getString(1));
			}
			rset2.close();
			stmt2.close();

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
			String queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_GTA_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
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
	
	public void getOtherInvoiceContactPerson()
	{
		String function_nm="getOtherInvoiceContactPerson()";
		try
		{
			String queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "R");
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
	
	public void getAddressType()
	{
		String function_nm="getAddressType()";
		try
		{
			/*queryString="SELECT DISTINCT ADDRESS_TYPE "
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
			stmt.close();*/

			for(int i=0; i<VPLANT_SEQ.size(); i++)
			{
				VADDRESS_TYPE.add("B"+VPLANT_SEQ.elementAt(i));
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
			String queryString = "SELECT COUNTERPARTY_CD,PLANT_NAME,PLANT_ABBR,SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS='Y' ";
			queryString+= "ORDER BY PLANT_NAME";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, "R");
			stmt.setString(3, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VPLANT_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VPLANT_SEQ.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			stmt.close();
			rset.close();
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
			VFILTER_CONT_TYPE.add("C");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("C"));
			
			VFILTER_CONT_TYPE.add("R");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("R"));
			
			VFILTER_CONT_TYPE.add("K");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("K"));
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
			String queryString="SELECT COMPANY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,CONT_REF_NO,COUNTERPARTY_CD "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND CONTRACT_TYPE=? ";
			}
			int cnt=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, period_end_dt);
			stmt.setString(++cnt, period_start_dt);
			stmt.setString(++cnt, counterparty_cd);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++cnt, filter_cont_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"0":rset.getString(2);
				String agmtrev=rset.getString(3)==null?"0":rset.getString(3);
				String contno=rset.getString(4)==null?"0":rset.getString(4);
				String contrev=rset.getString(5)==null?"0":rset.getString(5);
				String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
				String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
				String cont_ref=rset.getString(9)==null?"":rset.getString(9);
				String cont_type=rset.getString(10)==null?"":rset.getString(10);
				String trade_ref=rset.getString(11)==null?"":rset.getString(11);
				String contparty_cd=rset.getString(12)==null?"":rset.getString(12);
				
				if(cont_type.equals("I"))
				{
					cont_ref=trade_ref;
				}
				
				String deal_no = utilBean.NewDealMappingId(own_cd, contparty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
				if(!cont_ref.equals(""))
				{
					deal_no+=" ["+cont_ref+"]";
				}
				deal_no+=" ["+cont_st_dt+"-"+cont_end_dt+"]";
				
				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;

				VDEAL_NO.add(deal_no);
				VMAPPING_ID.add(mapping_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTransporterCounterpartyList()
	{
		String function_nm="getTransporterCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTransporterCounterpartyList(comp_cd);
			utilBean.getAllEntityCounterpartyList(conn,comp_cd,"R");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceType()
	{
		String function_nm="getInvoiceType()";
		try
		{	
			if(filter_remittance.equals(""))
			{
				if(contract_type.equals("C") || contract_type.equals("R"))
				{
					VINVOICE_TITLE.add("GTA Transmission Charge Remittance");
					VINVOICE_TYPE.add("TC");
					
					VINVOICE_TITLE.add("GTA Imbalance Charge Remittance");
					VINVOICE_TYPE.add("IC");
				}
				else if(contract_type.equals("K"))
				{
					VINVOICE_TITLE.add("Parking Charge Remittance");
					VINVOICE_TYPE.add("PC");
				}
				else
				{
					VINVOICE_TITLE.add("GTA Transmission Charge Remittance");
					VINVOICE_TYPE.add("TC");
					
					VINVOICE_TITLE.add("GTA Imbalance Charge Remittance");
					VINVOICE_TYPE.add("IC");
					
					VINVOICE_TITLE.add("Parking Charge Remittance");
					VINVOICE_TYPE.add("PC");
				}
			}
			else if(filter_remittance.equals("TC") && (contract_type.equals("") || contract_type.equals("C") || contract_type.equals("R")))
			{
				VINVOICE_TITLE.add("GTA Transmission Charge Remittance");
				VINVOICE_TYPE.add("TC");
			}
			else if(filter_remittance.equals("IC") && (contract_type.equals("") || contract_type.equals("C") || contract_type.equals("R")))
			{
				VINVOICE_TITLE.add("GTA Imbalance Charge Remittance");
				VINVOICE_TYPE.add("IC");
			}
			else if(filter_remittance.equals("PC") && (contract_type.equals("") || contract_type.equals("K")))
			{
				VINVOICE_TITLE.add("Parking Charge Remittance");
				VINVOICE_TYPE.add("PC");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccrualInvoiceType()
	{
		String function_nm="getAccrualInvoiceType()";
		try
		{
			//if(filter_remittance.equals("TC"))
			{
				VINVOICE_TITLE.add("GTA Transmission Charge Remittance");
				VINVOICE_TYPE.add("TC");
			}
			//else if(filter_remittance.equals("IC"))
			{
				VINVOICE_TITLE.add("GTA Imbalance Charge Remittance");
				VINVOICE_TYPE.add("IC");
			}
			//else if(filter_remittance.equals("PC"))
			{
				VINVOICE_TITLE.add("Parking Charge Remittance");
				VINVOICE_TYPE.add("PC");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void forAllBillingOptionFFLOW()
	{
		String function_nm="forAllBillingOptionFFLOW()";
		try
		{
			String temp_billing_cycle=billing_cycle;
			/*for(int i=1; i<=2; i++)
			{
				billing_cycle=""+i;
				getBillingCyclePeriod();
				getGTAOtherInvoiceList();
			}*/
			
			billing_cycle="8";
			getBillingCyclePeriod();
			getGTAOtherInvoiceList();
			
			billing_cycle=temp_billing_cycle;
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void forAllBillingOption()
	{
		String function_nm="forAllBillingOption()";
		try
		{
			/*VBILLING_FREQ_FLAG.add("0");
			VBILLING_FREQ_FLAG.add("1");
			VBILLING_FREQ_FLAG.add("2");
			VBILLING_FREQ_FLAG.add("3");
			VBILLING_FREQ_FLAG.add("4");
			VBILLING_FREQ_FLAG.add("5");
			VBILLING_FREQ_FLAG.add("6");
			VBILLING_FREQ_FLAG.add("9");
			VBILLING_FREQ_FLAG.add("7");
			VBILLING_FREQ_FLAG.add("8");

			VBILLING_FREQ_FLAG.add("All");
			VBILLING_FREQ_FLAG.add("1st-Fortnight");
			VBILLING_FREQ_FLAG.add("2nd-Fortnight");
			VBILLING_FREQ_FLAG.add("1st-Weekly");
			VBILLING_FREQ_FLAG.add("2nd-Weekly");
			VBILLING_FREQ_FLAG.add("3rd-Weekly");
			VBILLING_FREQ_FLAG.add("4th-Weekly");
			VBILLING_FREQ_FLAG.add("5th-Weekly");
			VBILLING_FREQ_FLAG.add("Monthly");
			VBILLING_FREQ_FLAG.add("Other");*/

			String temp_billing_cycle=billing_cycle;
			for(int i=1; i<=8; i++)
			{
				if(i==1 || i==2 || i==8)
				{
					billing_cycle=""+i;
					getBillingCyclePeriod();
					getGTARemittancePreparationList();
					//getExistOtherInvoiceList();
				}
			}
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
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGTARemittancePreparationList()
	{
		String function_nm="getGTARemittancePreparationList()";
		try
		{
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS,C.EFF_DT,"
						+ "NVL(A.MDQ,0) MDQ,A.MDQ_UNIT,A.SIP_PAY_FREQ,NVL(A.SIP_PAY_PERCENT,0) SIP_PAY_PERCENT "
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE ";
				if(!filter_trans_cd.equals(""))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				if(!contract_type.equals("") && !cont_no.equals(""))
				{
					queryString+= "AND A.CONTRACT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? ";
				}
				else
				{
					queryString+= "AND A.CONTRACT_TYPE IN ("+filter_contType+") ";
				}
				queryString+= "ORDER BY C.EFF_DT";
				String temp_queryString = queryString;
				int stcnt=0;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++stcnt, comp_cd);
				stmt.setString(++stcnt, temp_period_end_dt);
				stmt.setString(++stcnt, temp_period_start_dt);
				stmt.setString(++stcnt, billing_freq);
				stmt.setString(++stcnt, temp_period_start_dt);
				stmt.setString(++stcnt, temp_period_end_dt);
				stmt.setString(++stcnt, temp_period_start_dt);
				if(!filter_trans_cd.equals(""))
				{
					stmt.setString(++stcnt, filter_trans_cd);
				}
				if(!contract_type.equals("") && !cont_no.equals(""))
				{
					stmt.setString(++stcnt, contract_type);
					stmt.setString(++stcnt, agmt_no);
					stmt.setString(++stcnt, agmt_rev_no);
					stmt.setString(++stcnt, cont_no);
				}
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					
					String trans_bu_seq = rset.getString(13)==null?"":rset.getString(13);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(15)==null?"":rset.getString(15);
					String billing_days=rset.getString(16)==null?"1":rset.getString(16);
					String mdq=rset.getString(18)==null?"0":rset.getString(18);
					String ship_pay_freq=rset.getString(20)==null?"":rset.getString(20);
					String ship_pay_percent=rset.getString(21)==null?"":rset.getString(21);
					
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					//System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+billing_eff_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=billing_eff_dt;
					}
					else
					{
						periodStartDate=temp_period_start_dt;
					}
					int isLower=utilDate.getDays(end_dt, temp_period_end_dt);
					if(isLower < 1)
					{
						periodEndDate=end_dt;
					}
					else
					{
						periodEndDate=temp_period_end_dt;
					}
					//System.out.println("PeriodDate="+periodStartDate+"=="+periodEndDate);
					String temp_st_dt=periodStartDate;
					String temp_end_dt=periodEndDate;
					//System.out.println("Billing Days : "+billing_days);
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					//System.out.println("temp_dt :: "+temp_dt);
					int tot_row=1;
					for(int j=0;j<tot_row;j++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						int checkMthEnd=utilDate.getDays(periodEndDate,temp_st_dt);
						
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
						}
						else
						{
							temp_end_dt = periodEndDate;
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							//System.out.println("New Eff Date : "+innerBillingEffDt);
							break;
						}
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						//System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
						
						int countDays=utilDate.getDays(end_dt, temp_end_dt);
						
						Vector TEMP_INV_COMP_ABBR = new Vector();
						Vector TEMP_INV_COMP_NM = new Vector();
						
						double qty_mmbtu=0;
						double temp_qty_mmbtu=0;
						
						double shipOrPay_qty=0;
						double neg_imb_qty=0;
						double pos_imb_qty=0;
						double unauth_run_qty=0;
						
						double parking_qty=0;
						
						String tempStartDt=""+utilDate.getFirstDateOfMonth(month, year);
						String tempEndDt=""+utilDate.getLastDateOfMonth(month, year);
						if(invoice_type.equals("TC"))
						{	
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "TP");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "SP");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
							
							if(sip_pay_freq.equals("M")) 
							{	
								if(billing_cycle.equals("2") || countDays<=1 ) //ADDED = TO SIGN
								{
									if(utilDate.getDays(end_dt, tempEndDt)<1)
									{
										tempEndDt=end_dt;
									}
									if(utilDate.getDays(tempStartDt, start_dt)<1)
									{
										tempStartDt=start_dt;
									}
								}
							}
						}
						else if(invoice_type.equals("IC"))
						{
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "NI");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "PI");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "UR");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						}
						else if(invoice_type.equals("PC"))
						{
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "PC");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						}
						
						Vector temp = getAllGtaCalculation(countpty_cd,agmtno,contno,cont_type,bu_plant_seq,start_dt,end_dt,mdq,ship_pay_freq,ship_pay_percent,temp_st_dt,temp_end_dt, tempStartDt,tempEndDt);
						
						//VTEMP_CALC.add(transmission_qty);0
						//VTEMP_CALC.add(chargable_overrun);1
						//VTEMP_CALC.add(positive_imb);2
						//VTEMP_CALC.add(negative_imb);3
						//VTEMP_CALC.add(park_qty);4
						//VTEMP_CALC.add(def_qty);5
						if(invoice_type.equals("TC"))
						{
							if(sip_pay_freq.equals("M")) 
							{	
								if(billing_cycle.equals("2") || countDays<=1 ) //ADDED = TO SIGN
								{
									shipOrPay_qty = Double.parseDouble(""+temp.elementAt(5));
								}
							}
							qty_mmbtu = Double.parseDouble(""+temp.elementAt(0));
						}
						else if(invoice_type.equals("IC"))
						{
							neg_imb_qty = Double.parseDouble(""+temp.elementAt(3));
							pos_imb_qty = Double.parseDouble(""+temp.elementAt(2));
							unauth_run_qty = Double.parseDouble(""+temp.elementAt(1));
						}
						else if(invoice_type.equals("PC"))
						{
							parking_qty = Double.parseDouble(""+temp.elementAt(4));
						}
						
						String inv_grp_index_color="";
						int k=0;
						for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
						{
							
							String invCompAbbr=""+TEMP_INV_COMP_ABBR.elementAt(i);
							double tempQty=0;
							String tempCompoNm="";
							if(invCompAbbr.contains("TP"))
							{
								tempQty+=qty_mmbtu;
								tempCompoNm=tempCompoNm.equals("")?"Transportation":tempCompoNm+"<br>Transportation";
							}
							if(invCompAbbr.contains("SP"))
							{
								tempQty+=shipOrPay_qty;
								tempCompoNm=tempCompoNm.equals("")?"Ship-or-Pay":tempCompoNm+"<br>Ship-or-Pay";
							}
							if(invCompAbbr.contains("NI"))
							{
								tempQty+=neg_imb_qty;
								tempCompoNm=tempCompoNm.equals("")?"Negative Imbalance":tempCompoNm+"<br>Negative Imbalance";
							}
							if(invCompAbbr.contains("PI"))
							{
								tempQty+=pos_imb_qty;
								tempCompoNm=tempCompoNm.equals("")?"Positive Imbalance":tempCompoNm+"<br>Positive Imbalance";
							}
							if(invCompAbbr.contains("UR"))
							{
								tempQty+=unauth_run_qty;
								tempCompoNm=tempCompoNm.equals("")?"Unauthorized Overrun":tempCompoNm+"<br>Unauthorized Overrun";
							}
							if(invCompAbbr.contains("PC"))
							{
								tempQty+=parking_qty;
								tempCompoNm=tempCompoNm.equals("")?"Parking":tempCompoNm+"<br>Parking";
							}
							
							if(tempQty > 0)
							{
								inv_index+=1;
								if(k==0)
								{
									inv_grp_index+=1;
									if(inv_grp_index%2==0)
									{
										inv_grp_index_color="#d9ffb3";
									}
								}
								
								VINV_GRP_INDEX.add(inv_grp_index);
								VINV_GRP_INDEX_ROW_ID.add(k);
								VINV_GRP_INDEX_COLOR.add(inv_grp_index_color);
								k++;
								
								VINV_COMPONENTS.add(tempCompoNm);
								VINV_COMPONENTS_ABBR.add(""+TEMP_INV_COMP_ABBR.elementAt(i));
								
								/*VQTY_MMBTU.add(nf3.format(qty_mmbtu));
								if(invoice_type.equals("IC"))
								{
									VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
								}
								else
								{
									VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
								}*/
								
								VQTY_MMBTU.add(nf3.format(tempQty));
								VTEMP_QTY_MMBTU.add(nf3.format(tempQty));
								
								VCOUNTERPTY_CD.add(countpty_cd);
								VCOUNTERPTY_ABBR.add(countpty_abbr);
								VAGMT_NO.add(agmtno);
								VAGMT_REV_NO.add(agmtrev);
								VCONT_NO.add(contno);
								VCONT_REV_NO.add(contrev);
								VSTART_DT.add(start_dt);
								VEND_DT.add(end_dt);
								VCONT_NAME.add(cont_name);
								VCONT_REF_NO.add(cont_ref_no);
								VCONTRACT_TYPE.add(cont_type);
								VDEAL_NO.add(deal_no);
			
								VTRANS_BU_SEQ.add(trans_bu_seq);
								VTRANS_BU_ABBR.add(trans_bu_abbr);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								//VPERIOD_START_DT.add(period_start_dt);
								//VPERIOD_END_DT.add(period_end_dt);
								
								VPERIOD_START_DT.add(temp_st_dt);
								VPERIOD_END_DT.add(temp_end_dt);
								
								VBILLING_FREQ_FLAG.add(billing_cycle);
								VBILLING_FREQ_NM.add(billing_freq_nm);
								
								String inv_no="";
								String sys_inv_no="";
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
								String inv_dt="";
								
								queryString3="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,"
										+ "PDF_INV_DTL,SAP_APPROVAL,SYS_INV_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
										+ "FROM FMS_GTA_SG_INV_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND FREQ=? AND INVOICE_TYPE=? "
										+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INV_COMPONENT=? ";
								stmt3=conn.prepareStatement(queryString3);
								stmt3.setString(1, own_cd);
								stmt3.setString(2, countpty_cd);
								stmt3.setString(3, contno);
								stmt3.setString(4, agmtno);
								stmt3.setString(5, trans_bu_seq);
								stmt3.setString(6, bu_plant_seq);
								stmt3.setString(7, cont_type);
								stmt3.setString(8, billing_cycle);
								stmt3.setString(9, invoice_type);
								stmt3.setString(10, temp_st_dt);
								stmt3.setString(11, temp_end_dt);
								stmt3.setString(12, invCompAbbr);
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									//inv_no="SG : "+(rset3.getString(1)==null?"":rset3.getString(1));
									inv_no=rset3.getString(1)==null?"":rset3.getString(1);
									String chk_flg = rset3.getString(2)==null?"":rset3.getString(2);
									String auth_flg = rset3.getString(3)==null?"":rset3.getString(3);
									String aprv_flg = rset3.getString(4)==null?"":rset3.getString(4);
									inv_seq = rset3.getString(5)==null?"":rset3.getString(5);
									fin_yr = rset3.getString(6)==null?"":rset3.getString(6);
									String  pdf_inv_dtl = rset3.getString(7)==null?"":rset3.getString(7);
									sys_inv_no=rset3.getString(9)==null?"":rset3.getString(9);
									inv_dt=rset3.getString(10)==null?"":rset3.getString(10);
									
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
										sap_approved_flag=rset3.getString(8)==null?"":rset3.getString(8);
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
								rset3.close();
								stmt3.close();
								
								queryString4="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,"
										+ "PDF_INV_DTL,SAP_APPROVAL "
										+ "FROM FMS_GTA_PG_INV_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND FREQ=? AND INVOICE_TYPE=? "
										+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INV_COMPONENT=? ";
								stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1, own_cd);
								stmt4.setString(2, countpty_cd);
								stmt4.setString(3, contno);
								stmt4.setString(4, agmtno);
								stmt4.setString(5, trans_bu_seq);
								stmt4.setString(6, bu_plant_seq);
								stmt4.setString(7, cont_type);
								stmt4.setString(8, billing_cycle);
								stmt4.setString(9, invoice_type);
								stmt4.setString(10, temp_st_dt);
								stmt4.setString(11, temp_end_dt);
								stmt4.setString(12, invCompAbbr);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String chk_flg = rset4.getString(2)==null?"":rset4.getString(2);
									String auth_flg = rset4.getString(3)==null?"":rset4.getString(3);
									String aprv_flg = rset4.getString(4)==null?"":rset4.getString(4);
									String pdf_inv_dtl = rset4.getString(5)==null?"":rset4.getString(5);
									
									
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
										sap_approved_flag=rset4.getString(6)==null?"":rset4.getString(6);
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
								rset4.close();
								stmt4.close();
								
								int upload_count=0;
								queryString5="SELECT COUNT(*) "
										+ "FROM FMS_GTA_INV_FILE_DTL "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt5=conn.prepareStatement(queryString5);
								stmt5.setString(1, own_cd);
								stmt5.setString(2, cont_type);
								stmt5.setString(3, invoice_type);
								stmt5.setString(4, inv_seq);
								stmt5.setString(5, fin_yr);
								stmt5.setString(6, "PG_RECV");
								rset5=stmt5.executeQuery();
								if(rset5.next())
								{
									upload_count=rset5.getInt(1);
								}
								rset5.close();
								stmt5.close();
								
								queryString6="SELECT FILE_NAME "
										+ "FROM FMS_GTA_INV_FILE_DTL "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt6=conn.prepareStatement(queryString6);
								stmt6.setString(1, own_cd);
								stmt6.setString(2, cont_type);
								stmt6.setString(3, invoice_type);
								stmt6.setString(4, inv_seq);
								stmt6.setString(5, fin_yr);
								stmt6.setString(6, inv_title);
								rset6=stmt6.executeQuery();
								if(rset6.next())
								{
									VUPLOADED_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								}
								else
								{
									VUPLOADED_FILE_NAME.add("");
								}
								rset6.close();
								stmt6.close();
		
								VFILE_UPLOAD_COUNT.add(upload_count);
								
								VREMITTANCE_NO.add(sys_inv_no);
								VINVOICE_NO.add(inv_no);
								VINVOICE_SEQ.add(inv_seq);
								VFINANCIAL_YEAR.add(fin_yr);
								VSTATUS.add(sts);
								VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
								VPDF_INV_FLAG.add(pdf_inv_flag);
								VSAP_APPROVAL_FLAG.add(sap_approved_flag);
								VTYPE_FLAG.add(payment_type_flag);
								
								VAPPROVE_FLAG_CHECK.add(aprove);
								VCHECK_FLAG_CHECK.add(check);
								VAUTHORIZ_FLAG_CHECK.add(auth);
								VIS_SUBMITTED.add(is_submitted);
								VINVOICE_DT.add(inv_dt);
							}
						}
						
						if(rem_checkMthEnd == 0)
						{
							break;
						}
					}
		
				}
				rset.close();
				stmt.close();
			}
			else
			{
				/*
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'), "
						+ "NVL(A.MDQ,0) MDQ,A.MDQ_UNIT,A.SIP_PAY_FREQ,NVL(A.SIP_PAY_PERCENT,0) SIP_PAY_PERCENT "
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE ";
				//if(!filter_trans_cd.equals(""))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				//if(!contract_type.equals("") && !cont_no.equals(""))
				{
					queryString+= "AND A.CONTRACT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? ";
				}
				//else
				{
					//queryString+= "AND A.CONTRACT_TYPE IN ("+filter_contType+") ";
				}
				*/
				String queryString = "WITH CONT_LATEST AS ( SELECT A.*, ROW_NUMBER() OVER ( PARTITION BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONT_NO, A.AGMT_NO, A.AGMT_REV, A.CONTRACT_TYPE ORDER BY A.CONT_REV DESC ) AS ROW_NUM "
								+ "FROM FMS_GTA_CONT_MST A "
								+ "WHERE A.COMPANY_CD = ? AND A.START_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.END_DT >= TO_DATE(?,'DD/MM/YYYY') ";
				if(!filter_trans_cd.equals(""))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				if(!contract_type.equals("") && !cont_no.equals(""))
				{
					queryString+= "AND A.CONTRACT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? ";
				}
				else
				{
					queryString+= "AND A.CONTRACT_TYPE IN ("+filter_contType+") ";
				}
				queryString+= " ), "
							+ "BILLING_EFF AS ( SELECT C.*, ROW_NUMBER() OVER ( PARTITION BY C.COMPANY_CD, C.COUNTERPARTY_CD, C.AGMT_NO, C.CONT_NO, C.CONTRACT_TYPE, C.PLANT_SEQ_NO ORDER BY C.EFF_DT DESC ) AS ROW_NUM "
								+ "FROM FMS_GTA_BILLING_DTL C "
								+ "WHERE C.COMPANY_CD = ? AND C.BILLING_FREQ = ? AND C.EFF_DT <= TO_DATE(?,'DD/MM/YYYY') ) "
							+ "SELECT A.COMPANY_CD, A.COUNTERPARTY_CD, A.AGMT_NO, A.AGMT_REV, A.CONT_NO, A.CONT_REV, TO_CHAR(A.START_DT, 'DD/MM/YYYY') AS START_DT, TO_CHAR(A.END_DT, 'DD/MM/YYYY') AS END_DT, "
							+ "A.CONT_NAME, A.CONT_REF_NO, A.CONTRACT_TYPE, A.SIP_PAY_FREQ, D.PLANT_SEQ_NO AS D_PLANT_SEQ_NO, E.PLANT_SEQ_NO AS E_PLANT_SEQ_NO, TO_CHAR(C.EFF_DT, 'DD/MM/YYYY') AS EFF_DT, "
							+ "NVL(A.MDQ, 0) AS MDQ, A.MDQ_UNIT, A.SIP_PAY_FREQ AS SIP_PAY_FREQ_DUP, NVL(A.SIP_PAY_PERCENT, 0) AS SIP_PAY_PERCENT "
							+ "FROM CONT_LATEST A JOIN BILLING_EFF C "
								+ "ON C.COMPANY_CD = A.COMPANY_CD AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND C.AGMT_NO = A.AGMT_NO AND C.CONT_NO = A.CONT_NO AND C.CONTRACT_TYPE = A.CONTRACT_TYPE AND C.ROW_NUM = 1 "
							+ "JOIN FMS_GTA_CONT_TRANS_BU D "
								+ "ON D.COMPANY_CD = A.COMPANY_CD AND D.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND D.AGMT_NO = A.AGMT_NO AND D.AGMT_REV = A.AGMT_REV AND D.CONT_NO = A.CONT_NO "
								+ "AND D.CONT_REV = A.CONT_REV AND D.CONTRACT_TYPE = A.CONTRACT_TYPE "
								+ "AND D.PLANT_SEQ_NO = C.PLANT_SEQ_NO "
							+ "JOIN FMS_GTA_CONT_BU E "
								+ "ON E.COMPANY_CD = A.COMPANY_CD AND E.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND E.AGMT_NO = A.AGMT_NO AND E.AGMT_REV = A.AGMT_REV AND E.CONT_NO = A.CONT_NO "
								+ "AND E.CONT_REV = A.CONT_REV AND E.CONTRACT_TYPE = A.CONTRACT_TYPE "
							+ "WHERE A.ROW_NUM = 1 ";
				String temp_queryString = queryString;
				int stcnt=0;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++stcnt, comp_cd);
				stmt.setString(++stcnt, period_end_dt);
				stmt.setString(++stcnt, period_start_dt);
				if(!filter_trans_cd.equals(""))
				{
					stmt.setString(++stcnt, filter_trans_cd);
				}
				if(!contract_type.equals("") && !cont_no.equals(""))
				{
					stmt.setString(++stcnt, contract_type);
					stmt.setString(++stcnt, agmt_no);
					stmt.setString(++stcnt, agmt_rev_no);
					stmt.setString(++stcnt, cont_no);
				}
				stmt.setString(++stcnt, comp_cd);
				stmt.setString(++stcnt, billing_freq);
				stmt.setString(++stcnt, period_end_dt);
				
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					
					String trans_bu_seq = rset.getString(13)==null?"":rset.getString(13);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(15)==null?"":rset.getString(15);
					
					String mdq=rset.getString(16)==null?"0":rset.getString(16);
					String ship_pay_freq=rset.getString(18)==null?"":rset.getString(18);
					String ship_pay_percent=rset.getString(19)==null?"":rset.getString(19);
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
								
					String temp_period_start_dt=period_start_dt;
					String temp_period_end_dt=period_end_dt;
					
					/*int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					/*if(utilDate.getDays(period_start_dt, start_dt)<1)
					{
						temp_period_start_dt=start_dt;
					}*/
					
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					if(isGreter>1)
					{
						temp_period_start_dt=billing_eff_dt;
						temp_period_end_dt=period_end_dt;
					}
					else
					{
						temp_period_start_dt=period_start_dt;
						temp_period_end_dt=period_end_dt;
					}
					
					int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					
					/*if(utilDate.getDays(end_dt, period_end_dt)<=0)
					{
						temp_period_end_dt=end_dt;
					}*/
					
					Vector TEMP_INV_COMP_ABBR = new Vector();
					Vector TEMP_INV_COMP_NM = new Vector();
					
					double qty_mmbtu=0;
					double temp_qty_mmbtu=0;
					
					double shipOrPay_qty=0;
					double neg_imb_qty=0;
					double pos_imb_qty=0;
					double unauth_run_qty=0;
					
					double parking_qty=0;
					String temp_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
					String temp_end_dt=""+utilDate.getLastDateOfMonth(month, year);
					
					if(invoice_type.equals("TC"))
					{	
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "TP");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "SP");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						if(sip_pay_freq.equals("M")) 
						{	
							
							if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
							{
								if(utilDate.getDays(end_dt, temp_end_dt)<1)
								{
									temp_end_dt=end_dt;
								}
								if(utilDate.getDays(temp_start_dt, start_dt)<1)
								{
									temp_start_dt=start_dt;
								}
								
								//qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								//temp_qty_mmbtu=getDeficiencyQty(countpty_cd, cont_type, agmtno, contno, temp_start_dt, temp_end_dt, bu_plant_seq);
								//if(temp_qty_mmbtu<0) {
									//temp_qty_mmbtu=0;
								//}
								
								//shipOrPay_qty=temp_qty_mmbtu;
								//temp_qty_mmbtu+=qty_mmbtu;
							}
							else
							{
								//qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								//temp_qty_mmbtu=qty_mmbtu;
								//shipOrPay_qty=0;
							}
						}
						else
						{
							
							//qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
							//temp_qty_mmbtu=qty_mmbtu;
							//shipOrPay_qty=0;
						}
					}
					else if(invoice_type.equals("IC"))
					{
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "NI");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "PI");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "UR");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						//getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
						//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
						//temp_qty_mmbtu=1;
						
						//neg_imb_qty=Double.parseDouble(negative_imbalance_qty);
						//pos_imb_qty=Double.parseDouble(positive_imbalance_qty);
						//unauth_run_qty=Double.parseDouble(unauthorized_overrun_qty);
					}
					else if(invoice_type.equals("PC"))
					{
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "PC");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						//parking_qty=getParkingQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
					}
					
					Vector temp = getAllGtaCalculation(countpty_cd,agmtno,contno,cont_type,bu_plant_seq,start_dt,end_dt,mdq,ship_pay_freq,ship_pay_percent,temp_period_start_dt,temp_period_end_dt, temp_start_dt,temp_end_dt);
					//VTEMP_CALC.add(transmission_qty);0
					//VTEMP_CALC.add(chargable_overrun);1
					//VTEMP_CALC.add(positive_imb);2
					//VTEMP_CALC.add(negative_imb);3
					//VTEMP_CALC.add(park_qty);4
					//VTEMP_CALC.add(def_qty);5
					if(invoice_type.equals("TC"))
					{
						if(sip_pay_freq.equals("M")) 
						{	
							if(billing_cycle.equals("2") || countDays<=1 ) //ADDED = TO SIGN
							{
								shipOrPay_qty = Double.parseDouble(""+temp.elementAt(5));
							}
						}
						qty_mmbtu = Double.parseDouble(""+temp.elementAt(0));
					}
					else if(invoice_type.equals("IC"))
					{
						neg_imb_qty = Double.parseDouble(""+temp.elementAt(3));
						pos_imb_qty = Double.parseDouble(""+temp.elementAt(2));
						unauth_run_qty = Double.parseDouble(""+temp.elementAt(1));
					}
					else if(invoice_type.equals("PC"))
					{
						parking_qty = Double.parseDouble(""+temp.elementAt(4));
					}
					
					String inv_grp_index_color="";
					int k=0;
					for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
					{
						
						String invCompAbbr=""+TEMP_INV_COMP_ABBR.elementAt(i);
						double tempQty=0;
						String tempCompoNm="";
						if(invCompAbbr.contains("TP"))
						{
							tempQty+=qty_mmbtu;
							tempCompoNm=tempCompoNm.equals("")?"Transportation":tempCompoNm+"<br>Transportation";
						}
						if(invCompAbbr.contains("SP"))
						{
							tempQty+=shipOrPay_qty;
							tempCompoNm=tempCompoNm.equals("")?"Ship-or-Pay":tempCompoNm+"<br>Ship-or-Pay";
						}
						if(invCompAbbr.contains("NI"))
						{
							tempQty+=neg_imb_qty;
							tempCompoNm=tempCompoNm.equals("")?"Negative Imbalance":tempCompoNm+"<br>Negative Imbalance";
						}
						if(invCompAbbr.contains("PI"))
						{
							tempQty+=pos_imb_qty;
							tempCompoNm=tempCompoNm.equals("")?"Positive Imbalance":tempCompoNm+"<br>Positive Imbalance";
						}
						if(invCompAbbr.contains("UR"))
						{
							tempQty+=unauth_run_qty;
							tempCompoNm=tempCompoNm.equals("")?"Unauthorized Overrun":tempCompoNm+"<br>Unauthorized Overrun";
						}
						if(invCompAbbr.contains("PC"))
						{
							tempQty+=parking_qty;
							tempCompoNm=tempCompoNm.equals("")?"Parking":tempCompoNm+"<br>Parking";
						}
						
						if(tempQty > 0)
						{
							inv_index+=1;
							if(k==0)
							{
								inv_grp_index+=1;
								if(inv_grp_index%2==0)
								{
									inv_grp_index_color="#d9ffb3";
								}
							}
							
							VINV_GRP_INDEX.add(inv_grp_index);
							VINV_GRP_INDEX_ROW_ID.add(k);
							VINV_GRP_INDEX_COLOR.add(inv_grp_index_color);
							k++;
							
							VINV_COMPONENTS.add(tempCompoNm);
							VINV_COMPONENTS_ABBR.add(""+TEMP_INV_COMP_ABBR.elementAt(i));
							
							/*VQTY_MMBTU.add(nf3.format(qty_mmbtu));
							if(invoice_type.equals("IC"))
							{
								VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
							}
							else
							{
								VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
							}*/
							
							VQTY_MMBTU.add(nf3.format(tempQty));
							VTEMP_QTY_MMBTU.add(nf3.format(tempQty));
							
							VCOUNTERPTY_CD.add(countpty_cd);
							VCOUNTERPTY_ABBR.add(countpty_abbr);
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VSTART_DT.add(start_dt);
							VEND_DT.add(end_dt);
							VCONT_NAME.add(cont_name);
							VCONT_REF_NO.add(cont_ref_no);
							VCONTRACT_TYPE.add(cont_type);
							VDEAL_NO.add(deal_no);
		
							VTRANS_BU_SEQ.add(trans_bu_seq);
							VTRANS_BU_ABBR.add(trans_bu_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							//VPERIOD_START_DT.add(period_start_dt);
							//VPERIOD_END_DT.add(period_end_dt);
							
							VPERIOD_START_DT.add(temp_period_start_dt);
							VPERIOD_END_DT.add(temp_period_end_dt);
							
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							
							String inv_no="";
							String sys_inv_no="";
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
							String inv_dt="";
							
							queryString3="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "PDF_INV_DTL,SAP_APPROVAL,SYS_INV_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
									+ "FROM FMS_GTA_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INV_COMPONENT=? ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, trans_bu_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, billing_cycle);
							stmt3.setString(9, invoice_type);
							stmt3.setString(10, temp_period_start_dt);
							stmt3.setString(11, temp_period_end_dt);
							stmt3.setString(12, invCompAbbr);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								//inv_no="SG : "+(rset3.getString(1)==null?"":rset3.getString(1));
								inv_no=rset3.getString(1)==null?"":rset3.getString(1);
								String chk_flg = rset3.getString(2)==null?"":rset3.getString(2);
								String auth_flg = rset3.getString(3)==null?"":rset3.getString(3);
								String aprv_flg = rset3.getString(4)==null?"":rset3.getString(4);
								inv_seq = rset3.getString(5)==null?"":rset3.getString(5);
								fin_yr = rset3.getString(6)==null?"":rset3.getString(6);
								String  pdf_inv_dtl = rset3.getString(7)==null?"":rset3.getString(7);
								
								sys_inv_no=rset3.getString(9)==null?"":rset3.getString(9);
								inv_dt=rset3.getString(10)==null?"":rset3.getString(10);
								
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
									
									sap_approved_flag=rset3.getString(8)==null?"":rset3.getString(8);
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
							rset3.close();
							stmt3.close();
							
							queryString4="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,"
									+ "PDF_INV_DTL,SAP_APPROVAL "
									+ "FROM FMS_GTA_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INV_COMPONENT=? ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, own_cd);
							stmt4.setString(2, countpty_cd);
							stmt4.setString(3, contno);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, trans_bu_seq);
							stmt4.setString(6, bu_plant_seq);
							stmt4.setString(7, cont_type);
							stmt4.setString(8, billing_cycle);
							stmt4.setString(9, invoice_type);
							stmt4.setString(10, temp_period_start_dt);
							stmt4.setString(11, temp_period_end_dt);
							stmt4.setString(12, invCompAbbr);
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								String chk_flg = rset4.getString(2)==null?"":rset4.getString(2);
								String auth_flg = rset4.getString(3)==null?"":rset4.getString(3);
								String aprv_flg = rset4.getString(4)==null?"":rset4.getString(4);
								String pdf_inv_dtl = rset4.getString(5)==null?"":rset4.getString(5);
								
								
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
									
									sap_approved_flag=rset4.getString(6)==null?"":rset4.getString(6);
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
							rset4.close();
							stmt4.close();
							
							int upload_count=0;
							queryString5="SELECT COUNT(*) "
									+ "FROM FMS_GTA_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, own_cd);
							stmt5.setString(2, cont_type);
							stmt5.setString(3, invoice_type);
							stmt5.setString(4, inv_seq);
							stmt5.setString(5, fin_yr);
							stmt5.setString(6, "PG_RECV");
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								upload_count=rset5.getInt(1);
							}
							rset5.close();
							stmt5.close();
							
							queryString6="SELECT FILE_NAME "
									+ "FROM FMS_GTA_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
							stmt6=conn.prepareStatement(queryString6);
							stmt6.setString(1, own_cd);
							stmt6.setString(2, cont_type);
							stmt6.setString(3, invoice_type);
							stmt6.setString(4, inv_seq);
							stmt6.setString(5, fin_yr);
							stmt6.setString(6, inv_title);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								VUPLOADED_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
							}
							else
							{
								VUPLOADED_FILE_NAME.add("");
							}
							rset6.close();
							stmt6.close();
	
							VFILE_UPLOAD_COUNT.add(upload_count);
							
							VREMITTANCE_NO.add(sys_inv_no);
							VINVOICE_NO.add(inv_no);
							VINVOICE_SEQ.add(inv_seq);
							VFINANCIAL_YEAR.add(fin_yr);
							VSTATUS.add(sts);
							VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
							VPDF_INV_FLAG.add(pdf_inv_flag);
							VSAP_APPROVAL_FLAG.add(sap_approved_flag);
							VTYPE_FLAG.add(payment_type_flag);
							
							VAPPROVE_FLAG_CHECK.add(aprove);
							VCHECK_FLAG_CHECK.add(check);
							VAUTHORIZ_FLAG_CHECK.add(auth);
							VIS_SUBMITTED.add(is_submitted);
							VINVOICE_DT.add(inv_dt);
						}
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
	
	//PB20251223: for fetching the transmission,parking and imbalance details 
	public Vector getAllGtaCalculation(String counterpty_cd, String agmt_no, String cont_no, String cont_type, String bu_seq, String cont_start_dt, String cont_end_dt,
			String default_mdq, String ship_pay_freq, String ship_pay_perc,String period_start_dt,String period_end_dt, String def_start_dt, String def_end_dt)
	{
		String function_nm="getAllGtaCalculation()";
		Vector VTEMP_CALC = new Vector();
		try
		{
			double transmission_qty=0;
			double chargable_overrun=0;
			double positive_imb=0;
			double negative_imb=0;
			double park_qty=0;
			double sip_pay_per=0;
			double mdq_sum=0;
			double alloc_exit_qty=0;
			double ship_or_pay_qty=0;
			double def_qty=0;
			int stmt_ctn=0;
			String query="WITH PARAMS AS ( "
					+ "    SELECT "
					+ "      ? AS COMP_CD, "
					+ "      ? AS COUNTERPARTY_CD, "
					+ "      ? AS CONTRACT_TYPE, "
					+ "      ? AS AGMT_NO, "
					+ "      ? AS CONT_NO, "
					+ "      ? AS BU_SEQ, "
					+ "      TO_DATE(?,'DD/MM/YYYY') AS FROM_DT, "
					+ "      TO_DATE(?,'DD/MM/YYYY') AS TO_DT, "
					+ "      ? AS DEFAULT_MDQ, "
					+ "      ? AS SHIP_PAY_FREQ, "
					+ "      ? AS SHIP_OR_PAY_PER  "
					+ "    FROM DUAL "
					+ "), "
					+ "DATE_SERIES AS ( "
					+ "    SELECT P.FROM_DT + LEVEL - 1 AS GAS_DT "
					+ "    FROM PARAMS P "
					+ "    CONNECT BY P.FROM_DT + LEVEL - 1 <= P.TO_DT "
					+ "), "
					+ "NOM_LATEST AS ( "
					+ "    SELECT "
					+ "        GAS_DT, "
					+ "        SUM(MDQ) AS MDQ "
					+ "    FROM ( "
					+ "        SELECT "
					+ "            A.GAS_DT, "
					+ "            A.MDQ, "
					+ "            ROW_NUMBER() OVER ( "
					+ "                PARTITION BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, "
					+ "                             A.AGMT_NO, A.CONT_NO, A.BU_SEQ, "
					+ "                             A.GAS_DT, A.ENTRY_PT_MAPPING_ID, "
					+ "                             A.EXIT_PT_MAPPING_ID, A.SELL_CONT_MAP "
					+ "                ORDER BY A.NOM_REV_NO DESC "
					+ "            ) RN "
					+ "        FROM FMS_DAILY_TRANSPORTER_NOM A "
					+ "        CROSS JOIN PARAMS P "
					+ "        WHERE A.COMPANY_CD = P.COMP_CD "
					+ "          AND A.COUNTERPARTY_CD = P.COUNTERPARTY_CD "
					+ "          AND A.CONTRACT_TYPE= P.CONTRACT_TYPE "
					+ "          AND A.AGMT_NO= P.AGMT_NO "
					+ "          AND A.CONT_NO= P.CONT_NO "
					+ "          AND A.BU_SEQ = P.BU_SEQ "
					+ "          AND A.GAS_DT BETWEEN P.FROM_DT AND P.TO_DT "
					+ "    ) "
					+ "    WHERE RN = 1 "
					+ "    GROUP BY GAS_DT "
					+ "), "
					+ "SCH_LATEST AS ( "
					+ "    SELECT "
					+ "        GAS_DT, "
					+ "        SUM(QTY_MMBTU)       AS SCH_ENTRY_QTY, "
					+ "        SUM(EXIT_QTY_MMBTU)  AS SCH_EXIT_QTY "
					+ "    FROM ( "
					+ "        SELECT "
					+ "            A.GAS_DT, "
					+ "            A.QTY_MMBTU, "
					+ "            A.EXIT_QTY_MMBTU, "
					+ "            ROW_NUMBER() OVER ( "
					+ "                PARTITION BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, "
					+ "                             A.AGMT_NO, A.CONT_NO, A.BU_SEQ, "
					+ "                             A.GAS_DT, A.ENTRY_PT_MAPPING_ID, "
					+ "                             A.EXIT_PT_MAPPING_ID, A.SELL_CONT_MAP "
					+ "                ORDER BY A.SCH_REV_NO DESC "
					+ "            ) RN "
					+ "        FROM FMS_DAILY_TRANSPORTER_SCH A "
					+ "        CROSS JOIN PARAMS P "
					+ "        WHERE A.COMPANY_CD      = P.COMP_CD "
					+ "          AND A.COUNTERPARTY_CD = P.COUNTERPARTY_CD "
					+ "          AND A.CONTRACT_TYPE   = P.CONTRACT_TYPE "
					+ "          AND A.AGMT_NO         = P.AGMT_NO "
					+ "          AND A.CONT_NO         = P.CONT_NO "
					+ "          AND A.BU_SEQ          = P.BU_SEQ "
					+ "          AND A.GAS_DT BETWEEN P.FROM_DT AND P.TO_DT "
					+ "    ) "
					+ "    WHERE RN = 1 "
					+ "    GROUP BY GAS_DT "
					+ "), "
					+ "ALLOC_LATEST AS ( "
					+ "    SELECT "
					+ "        GAS_DT, "
					+ "        SUM(QTY_MMBTU)       AS ALLOC_ENTRY_QTY, "
					+ "        SUM(EXIT_QTY_MMBTU)  AS ALLOC_EXIT_QTY, "
					+ "        SUM(ADJ_IMBALANCE)   AS ADJ_IMBALANCE "
					+ "    FROM ( "
					+ "        SELECT "
					+ "            A.GAS_DT, "
					+ "            A.QTY_MMBTU, "
					+ "            A.EXIT_QTY_MMBTU, "
					+ "            A.ADJ_IMBALANCE, "
					+ "            ROW_NUMBER() OVER ( "
					+ "                PARTITION BY A.COMPANY_CD, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, "
					+ "                             A.AGMT_NO, A.CONT_NO, A.BU_SEQ, "
					+ "                             A.GAS_DT, A.ENTRY_PT_MAPPING_ID, "
					+ "                             A.EXIT_PT_MAPPING_ID, A.SELL_CONT_MAP "
					+ "                ORDER BY A.ALLOC_REV_NO DESC "
					+ "            ) RN "
					+ "        FROM FMS_DAILY_TRANSPORTER_ALLOC A "
					+ "        CROSS JOIN PARAMS P "
					+ "        WHERE A.COMPANY_CD = P.COMP_CD "
					+ "          AND A.COUNTERPARTY_CD = P.COUNTERPARTY_CD "
					+ "          AND A.CONTRACT_TYPE = P.CONTRACT_TYPE "
					+ "          AND A.AGMT_NO = P.AGMT_NO "
					+ "          AND A.CONT_NO = P.CONT_NO "
					+ "          AND A.BU_SEQ = P.BU_SEQ "
					+ "          AND A.GAS_DT BETWEEN P.FROM_DT AND P.TO_DT "
					+ "    ) "
					+ "    WHERE RN = 1 "
					+ "    GROUP BY GAS_DT "
					+ "), "
					+ "BASE AS ( "
					+ "    SELECT "
					+ "        DS.GAS_DT, "
					+ "        TO_CHAR(DS.GAS_DT,'DD/MM/YYYY') AS GAS_DT_STR, "
					+ "        NVL(N.MDQ, P.DEFAULT_MDQ) AS VAR_MDQ, "
					+ "        NVL(S.SCH_ENTRY_QTY, 0) AS SCH_ENTRY_QTY, "
					+ "        NVL(S.SCH_EXIT_QTY,  0) AS SCH_EXIT_QTY, "
					+ "        NVL(A.ALLOC_ENTRY_QTY, 0) AS ALLOC_ENTRY_QTY, "
					+ "        NVL(A.ALLOC_EXIT_QTY,  0) AS ALLOC_EXIT_QTY, "
					+ "        NVL(A.ADJ_IMBALANCE,   0) AS ADJ_IMBALANCE, "
					+ "        (NVL(A.ALLOC_ENTRY_QTY,0) - NVL(A.ALLOC_EXIT_QTY,0)) AS DAILY_IMBALANCE, "
					+ "        SUM( "
					+ "            (NVL(A.ALLOC_ENTRY_QTY,0) - NVL(A.ALLOC_EXIT_QTY,0)) "
					+ "            + NVL(A.ADJ_IMBALANCE,0) "
					+ "        ) OVER ( "
					+ "            ORDER BY DS.GAS_DT "
					+ "            ROWS UNBOUNDED PRECEDING "
					+ "        ) AS CUMULATIVE_IMBALANCE, "
					+ "        CASE "
					+ "            WHEN P.SHIP_PAY_FREQ = 'D'  "
					+ "                THEN  "
					+ "                    CASE  "
					+ "                        WHEN NVL(A.ALLOC_EXIT_QTY,0) >= (NVL(N.MDQ, P.DEFAULT_MDQ) * (P.SHIP_OR_PAY_PER / 100))  "
					+ "                        THEN NVL(A.ALLOC_EXIT_QTY,0) "
					+ "                        ELSE (NVL(N.MDQ, P.DEFAULT_MDQ) * (P.SHIP_OR_PAY_PER / 100)) "
					+ "                    END "
					+ "            ELSE NVL(A.ALLOC_EXIT_QTY,0) "
					+ "        END AS TRANSMISSION_QTY, "
					+ "        P.SHIP_OR_PAY_PER AS SHIP_OR_PAY_PER "
					+ "      "
					+ "    FROM DATE_SERIES DS "
					+ "    CROSS JOIN PARAMS P "
					+ "    LEFT JOIN NOM_LATEST N ON N.GAS_DT = DS.GAS_DT "
					+ "    LEFT JOIN SCH_LATEST S ON S.GAS_DT = DS.GAS_DT "
					+ "    LEFT JOIN ALLOC_LATEST A ON A.GAS_DT = DS.GAS_DT "
					+ "), "
					+ "T AS (SELECT "
					+ "    GAS_DT_STR, "
					+ "    VAR_MDQ, "
					+ "    SCH_ENTRY_QTY, "
					+ "    SCH_EXIT_QTY, "
					+ "    ALLOC_ENTRY_QTY, "
					+ "    ALLOC_EXIT_QTY, "
					+ "    TRANSMISSION_QTY,   "
					+ "    ADJ_IMBALANCE, "
					+ "    DAILY_IMBALANCE, "
					+ "    CUMULATIVE_IMBALANCE, "
					//HP NEW LOGIC
					+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY >= VAR_MDQ THEN "
					+ "          CASE WHEN  ALLOC_EXIT_QTY > SCH_EXIT_QTY "
					+ "            THEN ALLOC_EXIT_QTY - SCH_EXIT_QTY "
					+ "          ELSE 0 END "
					+ "        ELSE CASE WHEN SCH_EXIT_QTY < VAR_MDQ THEN "
					+ "          CASE WHEN ALLOC_EXIT_QTY > SCH_EXIT_QTY "
					+ "            THEN ALLOC_EXIT_QTY - VAR_MDQ "
					+ "          ELSE 0 END "
					+ "			ELSE 0 END "
					+ "    END AS UNAUTHORIZED_OVERRUN, "
					+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY >= VAR_MDQ THEN "
					+ "            CASE WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (1.10 * SCH_EXIT_QTY) "
					+ "            ELSE 0 END "
					+ "        ELSE CASE WHEN SCH_EXIT_QTY < VAR_MDQ THEN "
					+ "          CASE WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (VAR_MDQ + 0.1 * SCH_EXIT_QTY) "
					+ "            ELSE 0 END "
					+ "			ELSE 0 END"
					+ "    END AS CHARGEABLE_OVERRUN, "
					
					//NEW LOGIC
					/*+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY != VAR_MDQ THEN "
					+ "          CASE WHEN  ALLOC_EXIT_QTY > SCH_EXIT_QTY "
					+ "            THEN ALLOC_EXIT_QTY - SCH_EXIT_QTY "
					+ "          ELSE 0 END "
					+ "        ELSE "
					+ "          CASE WHEN ALLOC_EXIT_QTY > VAR_MDQ "
					+ "            THEN ALLOC_EXIT_QTY - VAR_MDQ "
					+ "          ELSE 0 END "
					+ "    END AS UNAUTHORIZED_OVERRUN, "
					+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY != VAR_MDQ THEN "
					+ "            CASE WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (1.10 * SCH_EXIT_QTY) "
					+ "            ELSE 0 END "
					+ "        ELSE "
					+ "          CASE "
					+ "            WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (VAR_MDQ + 0.1 * SCH_EXIT_QTY) "
					+ "            WHEN ALLOC_EXIT_QTY > 1.10 * VAR_MDQ "
					+ "              THEN ALLOC_EXIT_QTY - (1.10 * VAR_MDQ) "
					+ "            ELSE 0 END "
					+ "    END AS CHARGEABLE_OVERRUN, "*/
					
					//OLD LOGIC
					/*+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY > VAR_MDQ THEN "
					+ "          CASE WHEN  ALLOC_EXIT_QTY > SCH_EXIT_QTY "
					+ "            THEN ALLOC_EXIT_QTY - SCH_EXIT_QTY "
					+ "          ELSE 0 END "
					+ "        ELSE "
					+ "          CASE WHEN ALLOC_EXIT_QTY > VAR_MDQ "
					+ "            THEN ALLOC_EXIT_QTY - VAR_MDQ "
					+ "          ELSE 0 END "
					+ "    END AS UNAUTHORIZED_OVERRUN, "
					+ "    CASE "
					+ "        WHEN SCH_EXIT_QTY > VAR_MDQ THEN "
					+ "            CASE WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (1.10 * SCH_EXIT_QTY) "
					+ "            ELSE 0 END "
					+ "        ELSE  "
					+ "          CASE "
					+ "            WHEN ALLOC_EXIT_QTY > 1.10 * SCH_EXIT_QTY "
					+ "              THEN ALLOC_EXIT_QTY - (VAR_MDQ + 0.1 * SCH_EXIT_QTY) "
					+ "            WHEN ALLOC_EXIT_QTY > 1.10 * VAR_MDQ "
					+ "              THEN ALLOC_EXIT_QTY - (1.10 * VAR_MDQ) "
					+ "            ELSE 0 END "
					+ "    END AS CHARGEABLE_OVERRUN, "*/
					
					+ "    GREATEST(CUMULATIVE_IMBALANCE - (0.1 * VAR_MDQ), 0) AS POSITIVE_IMBALANCE, "
					+ "    CASE "
					+ "        WHEN CUMULATIVE_IMBALANCE + (0.05 * VAR_MDQ) >= 0 "
					+ "            THEN 0 "
					+ "        ELSE -(CUMULATIVE_IMBALANCE + (0.05 * VAR_MDQ)) "
					+ "    END AS NEGATIVE_IMBALANCE, "
					+ "     SUM((NVL(ALLOC_ENTRY_QTY,0) - NVL(ALLOC_EXIT_QTY,0)) + NVL(ADJ_IMBALANCE,0) ) OVER ( "
					+ "  ORDER BY GAS_DT "
					+ "  ROWS UNBOUNDED PRECEDING "
					+ ")  AS PARKING_QTY, "
					+ "SHIP_OR_PAY_PER "
					+ "FROM BASE "
					+ "ORDER BY GAS_DT) "
					+ "SELECT  "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN T.TRANSMISSION_QTY "
					+ "          ELSE 0 END  "
					+ "    ) TRANSMISSION_QTY, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN T.CHARGEABLE_OVERRUN "
					+ "          ELSE 0 END "
					+ "      ) CHARGEABLE_OVERRUN, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN T.POSITIVE_IMBALANCE "
					+ "          ELSE 0 END  "
					+ "      ) POSITIVE_IMBALANCE, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY')   "
					+ "          THEN T.NEGATIVE_IMBALANCE "
					+ "          ELSE 0 END  "
					+ "      ) NEGATIVE_IMBALANCE, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN PARKING_QTY "
					+ "          ELSE 0 END  "
					+ "      )PARKING_QTY, "
					+ "  SHIP_OR_PAY_PER, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN VAR_MDQ "
					+ "          ELSE 0 END  "
					+ "      ) SUM_MDQ, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN ALLOC_EXIT_QTY "
					+ "          ELSE 0 END  "
					+ "      ) SUM_ALLOC_EXIT_QTY, "
					+ "  SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN VAR_MDQ "
					+ "          ELSE 0 END  "
					+ "      ) * SHIP_OR_PAY_PER/100 SHIP_OR_PAY_QTY, "
					+ "   SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN VAR_MDQ "
					+ "          ELSE 0 END  "
					+ "       ) * SHIP_OR_PAY_PER/100  "
					+ "       - "
					+ "    SUM( "
					+ "        CASE  "
					+ "          WHEN TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(T.GAS_DT_STR,'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
					+ "          THEN ALLOC_EXIT_QTY "
					+ "          ELSE 0 END  "
					+ "       )  DEF_QTY "
					+ "FROM T "
					+ "GROUP BY T.SHIP_OR_PAY_PER";
			stmt_temp=conn.prepareStatement(query);
			stmt_temp.setString(++stmt_ctn,comp_cd);
			stmt_temp.setString(++stmt_ctn,counterpty_cd);
			stmt_temp.setString(++stmt_ctn,cont_type);
			stmt_temp.setString(++stmt_ctn,agmt_no);
			stmt_temp.setString(++stmt_ctn,cont_no);
			stmt_temp.setString(++stmt_ctn,bu_seq);
			stmt_temp.setString(++stmt_ctn,cont_start_dt);
			stmt_temp.setString(++stmt_ctn,cont_end_dt);
			stmt_temp.setString(++stmt_ctn,default_mdq);
			stmt_temp.setString(++stmt_ctn,ship_pay_freq);
			stmt_temp.setString(++stmt_ctn,ship_pay_perc);
			stmt_temp.setString(++stmt_ctn,period_start_dt);
			stmt_temp.setString(++stmt_ctn,period_end_dt);
			stmt_temp.setString(++stmt_ctn,period_start_dt);
			stmt_temp.setString(++stmt_ctn,period_end_dt);
			stmt_temp.setString(++stmt_ctn,period_start_dt);
			stmt_temp.setString(++stmt_ctn,period_end_dt);
			stmt_temp.setString(++stmt_ctn,period_start_dt);
			stmt_temp.setString(++stmt_ctn,period_end_dt);
			stmt_temp.setString(++stmt_ctn,period_start_dt);
			stmt_temp.setString(++stmt_ctn,period_end_dt);
			stmt_temp.setString(++stmt_ctn,def_start_dt);
			stmt_temp.setString(++stmt_ctn,def_end_dt);
			stmt_temp.setString(++stmt_ctn,def_start_dt);
			stmt_temp.setString(++stmt_ctn,def_end_dt);
			stmt_temp.setString(++stmt_ctn,def_start_dt);
			stmt_temp.setString(++stmt_ctn,def_end_dt);
			stmt_temp.setString(++stmt_ctn,def_start_dt);
			stmt_temp.setString(++stmt_ctn,def_end_dt);
			stmt_temp.setString(++stmt_ctn,def_start_dt);
			stmt_temp.setString(++stmt_ctn,def_end_dt);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				transmission_qty = rset_temp.getDouble(1);
				chargable_overrun=rset_temp.getDouble(2);
				positive_imb=rset_temp.getDouble(3);
				negative_imb=rset_temp.getDouble(4);
				park_qty=rset_temp.getDouble(5);
				sip_pay_per=rset_temp.getDouble(6);
				mdq_sum=rset_temp.getDouble(7);
				alloc_exit_qty=rset_temp.getDouble(8);
				ship_or_pay_qty=rset_temp.getDouble(9);
				def_qty=rset_temp.getDouble(10);
			}
			rset_temp.close();
			stmt_temp.close();
			
			VTEMP_CALC.add(transmission_qty);
			VTEMP_CALC.add(chargable_overrun);
			VTEMP_CALC.add(positive_imb);
			VTEMP_CALC.add(negative_imb);
			VTEMP_CALC.add(park_qty);
			VTEMP_CALC.add(def_qty);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return VTEMP_CALC;
	}
	
	public void getGTARemittancePreparationList_bkp()
	{
		String function_nm="getGTARemittancePreparationList()";
		try
		{	
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS,C.EFF_DT "
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE ";
				//if(!filter_trans_cd.equals(""))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				//if(!contract_type.equals("") && !cont_no.equals(""))
				{
					queryString+= "AND A.CONTRACT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? ";
				}
				/*else
				{
					queryString+= "AND A.CONTRACT_TYPE IN ("+filter_contType+") ";
				}*/
				queryString+= "ORDER BY C.EFF_DT";
				String temp_queryString = queryString;
				int stcnt=0;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++stcnt, comp_cd);
				stmt.setString(++stcnt, temp_period_end_dt);
				stmt.setString(++stcnt, temp_period_start_dt);
				stmt.setString(++stcnt, billing_freq);
				stmt.setString(++stcnt, temp_period_start_dt);
				stmt.setString(++stcnt, temp_period_end_dt);
				stmt.setString(++stcnt, temp_period_start_dt);
				//if(!filter_trans_cd.equals(""))
				{
					stmt.setString(++stcnt, filter_trans_cd);
				}
				//if(!contract_type.equals("") && !cont_no.equals(""))
				{
					stmt.setString(++stcnt, contract_type);
					stmt.setString(++stcnt, agmt_no);
					stmt.setString(++stcnt, agmt_rev_no);
					stmt.setString(++stcnt, cont_no);
				}
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					
					String trans_bu_seq = rset.getString(13)==null?"":rset.getString(13);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(15)==null?"":rset.getString(15);
					String billing_days=rset.getString(16)==null?"1":rset.getString(16);
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+billing_eff_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=billing_eff_dt;
					}
					else
					{
						periodStartDate=temp_period_start_dt;
					}
					int isLower=utilDate.getDays(end_dt, temp_period_end_dt);
					if(isLower < 1)
					{
						periodEndDate=end_dt;
					}
					else
					{
						periodEndDate=temp_period_end_dt;
					}
					System.out.println("PeriodDate="+periodStartDate+"=="+periodEndDate);
					String temp_st_dt=periodStartDate;
					String temp_end_dt=periodEndDate;
					System.out.println("Billing Days : "+billing_days);
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					System.out.println("temp_dt :: "+temp_dt);
					int tot_row=1;
					for(int j=0;j<tot_row;j++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						int checkMthEnd=utilDate.getDays(periodEndDate,temp_st_dt);
						
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
						}
						else
						{
							temp_end_dt = periodEndDate;
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							System.out.println("New Eff Date : "+innerBillingEffDt);
							break;
						}
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
						
						int countDays=utilDate.getDays(end_dt, temp_end_dt);
						
						Vector TEMP_INV_COMP_ABBR = new Vector();
						Vector TEMP_INV_COMP_NM = new Vector();
						
						double qty_mmbtu=0;
						double temp_qty_mmbtu=0;
						
						double shipOrPay_qty=0;
						double neg_imb_qty=0;
						double pos_imb_qty=0;
						double unauth_run_qty=0;
						
						double parking_qty=0;
						
						if(invoice_type.equals("TC"))
						{	
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "TP");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "SP");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
							
							if(sip_pay_freq.equals("M")) 
							{	
								String tempStartDt=""+utilDate.getFirstDateOfMonth(month, year);
								String tempEndDt=""+utilDate.getLastDateOfMonth(month, year);
								
								if(billing_cycle.equals("2") || countDays<=1 ) //ADDED = TO SIGN
								{
									if(utilDate.getDays(end_dt, tempEndDt)<1)
									{
										tempEndDt=end_dt;
									}
									if(utilDate.getDays(tempStartDt, start_dt)<1)
									{
										tempStartDt=start_dt;
									}
									
									qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
									
									temp_qty_mmbtu=getDeficiencyQty(countpty_cd, cont_type, agmtno, contno, tempStartDt, tempEndDt, bu_plant_seq);
									if(temp_qty_mmbtu<0) {
										temp_qty_mmbtu=0;
									}
									
									shipOrPay_qty=temp_qty_mmbtu;
									//temp_qty_mmbtu+=qty_mmbtu;
								}
								else
								{
									qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
									
									//temp_qty_mmbtu=qty_mmbtu;
									shipOrPay_qty=0;
								}
							}
							else
							{
								qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
							
								//temp_qty_mmbtu=qty_mmbtu;
								shipOrPay_qty=0;
							}
						}
						else if(invoice_type.equals("IC"))
						{
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "NI");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "PI");
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "UR");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
							
							getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
							
							//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
							//temp_qty_mmbtu=1;
							
							neg_imb_qty=Double.parseDouble(negative_imbalance_qty);
							pos_imb_qty=Double.parseDouble(positive_imbalance_qty);
							unauth_run_qty=Double.parseDouble(unauthorized_overrun_qty);
						}
						else if(invoice_type.equals("PC"))
						{
							VTEMP_INV_COMPONENT.clear();
							getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_st_dt, temp_end_dt, "PC");
							TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
							
							parking_qty=getParkingQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
						}
						
						String inv_grp_index_color="";
						int k=0;
						for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
						{
							
							String invCompAbbr=""+TEMP_INV_COMP_ABBR.elementAt(i);
							double tempQty=0;
							String tempCompoNm="";
							if(invCompAbbr.contains("TP"))
							{
								tempQty+=qty_mmbtu;
								tempCompoNm=tempCompoNm.equals("")?"Transportation":tempCompoNm+"<br>Transportation";
							}
							if(invCompAbbr.contains("SP"))
							{
								tempQty+=shipOrPay_qty;
								tempCompoNm=tempCompoNm.equals("")?"Ship-or-Pay":tempCompoNm+"<br>Ship-or-Pay";
							}
							if(invCompAbbr.contains("NI"))
							{
								tempQty+=neg_imb_qty;
								tempCompoNm=tempCompoNm.equals("")?"Negative Imbalance":tempCompoNm+"<br>Negative Imbalance";
							}
							if(invCompAbbr.contains("PI"))
							{
								tempQty+=pos_imb_qty;
								tempCompoNm=tempCompoNm.equals("")?"Positive Imbalance":tempCompoNm+"<br>Positive Imbalance";
							}
							if(invCompAbbr.contains("UR"))
							{
								tempQty+=unauth_run_qty;
								tempCompoNm=tempCompoNm.equals("")?"Unauthorized Overrun":tempCompoNm+"<br>Unauthorized Overrun";
							}
							if(invCompAbbr.contains("PC"))
							{
								tempQty+=parking_qty;
								tempCompoNm=tempCompoNm.equals("")?"Parking":tempCompoNm+"<br>Parking";
							}
							
							if(tempQty > 0)
							{
								inv_index+=1;
								if(k==0)
								{
									inv_grp_index+=1;
									if(inv_grp_index%2==0)
									{
										inv_grp_index_color="#d9ffb3";
									}
								}
								
								VINV_GRP_INDEX.add(inv_grp_index);
								VINV_GRP_INDEX_ROW_ID.add(k);
								VINV_GRP_INDEX_COLOR.add(inv_grp_index_color);
								k++;
								
								VINV_COMPONENTS.add(tempCompoNm);
								VINV_COMPONENTS_ABBR.add(""+TEMP_INV_COMP_ABBR.elementAt(i));
								
								/*VQTY_MMBTU.add(nf3.format(qty_mmbtu));
								if(invoice_type.equals("IC"))
								{
									VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
								}
								else
								{
									VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
								}*/
								
								VQTY_MMBTU.add(nf3.format(tempQty));
								VTEMP_QTY_MMBTU.add(nf3.format(tempQty));
								
								VCOUNTERPTY_CD.add(countpty_cd);
								VCOUNTERPTY_ABBR.add(countpty_abbr);
								VAGMT_NO.add(agmtno);
								VAGMT_REV_NO.add(agmtrev);
								VCONT_NO.add(contno);
								VCONT_REV_NO.add(contrev);
								VSTART_DT.add(start_dt);
								VEND_DT.add(end_dt);
								VCONT_NAME.add(cont_name);
								VCONT_REF_NO.add(cont_ref_no);
								VCONTRACT_TYPE.add(cont_type);
								VDEAL_NO.add(deal_no);
			
								VTRANS_BU_SEQ.add(trans_bu_seq);
								VTRANS_BU_ABBR.add(trans_bu_abbr);
								VBU_PLANT_SEQ.add(bu_plant_seq);
								VBU_PLANT_ABBR.add(bu_plant_abbr);
								//VPERIOD_START_DT.add(period_start_dt);
								//VPERIOD_END_DT.add(period_end_dt);
								
								VPERIOD_START_DT.add(temp_st_dt);
								VPERIOD_END_DT.add(temp_end_dt);
								
								VBILLING_FREQ_FLAG.add(billing_cycle);
								VBILLING_FREQ_NM.add(billing_freq_nm);
								
								String inv_no="";
								String sys_inv_no="";
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
								
								queryString3="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,"
										+ "PDF_INV_DTL,SAP_APPROVAL,SYS_INV_NO "
										+ "FROM FMS_GTA_SG_INV_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND FREQ=? AND INVOICE_TYPE=? "
										+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INV_COMPONENT=? ";
								stmt3=conn.prepareStatement(queryString3);
								stmt3.setString(1, own_cd);
								stmt3.setString(2, countpty_cd);
								stmt3.setString(3, contno);
								stmt3.setString(4, agmtno);
								stmt3.setString(5, trans_bu_seq);
								stmt3.setString(6, bu_plant_seq);
								stmt3.setString(7, cont_type);
								stmt3.setString(8, billing_cycle);
								stmt3.setString(9, invoice_type);
								stmt3.setString(10, temp_st_dt);
								stmt3.setString(11, temp_end_dt);
								stmt3.setString(12, invCompAbbr);
								rset3=stmt3.executeQuery();
								if(rset3.next())
								{
									//inv_no="SG : "+(rset3.getString(1)==null?"":rset3.getString(1));
									inv_no=rset3.getString(1)==null?"":rset3.getString(1);
									String chk_flg = rset3.getString(2)==null?"":rset3.getString(2);
									String auth_flg = rset3.getString(3)==null?"":rset3.getString(3);
									String aprv_flg = rset3.getString(4)==null?"":rset3.getString(4);
									inv_seq = rset3.getString(5)==null?"":rset3.getString(5);
									fin_yr = rset3.getString(6)==null?"":rset3.getString(6);
									String  pdf_inv_dtl = rset3.getString(7)==null?"":rset3.getString(7);
									sys_inv_no=rset3.getString(9)==null?"":rset3.getString(9);
									
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
										sap_approved_flag=rset3.getString(8)==null?"":rset3.getString(8);
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
								rset3.close();
								stmt3.close();
								
								queryString4="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,"
										+ "PDF_INV_DTL,SAP_APPROVAL "
										+ "FROM FMS_GTA_PG_INV_MST "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
										+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND FREQ=? AND INVOICE_TYPE=? "
										+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND INV_COMPONENT=? ";
								stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1, own_cd);
								stmt4.setString(2, countpty_cd);
								stmt4.setString(3, contno);
								stmt4.setString(4, agmtno);
								stmt4.setString(5, trans_bu_seq);
								stmt4.setString(6, bu_plant_seq);
								stmt4.setString(7, cont_type);
								stmt4.setString(8, billing_cycle);
								stmt4.setString(9, invoice_type);
								stmt4.setString(10, temp_st_dt);
								stmt4.setString(11, temp_end_dt);
								stmt4.setString(12, invCompAbbr);
								rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									String chk_flg = rset4.getString(2)==null?"":rset4.getString(2);
									String auth_flg = rset4.getString(3)==null?"":rset4.getString(3);
									String aprv_flg = rset4.getString(4)==null?"":rset4.getString(4);
									String pdf_inv_dtl = rset4.getString(5)==null?"":rset4.getString(5);
									
									
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
										sap_approved_flag=rset4.getString(6)==null?"":rset4.getString(6);
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
								rset4.close();
								stmt4.close();
								
								int upload_count=0;
								queryString5="SELECT COUNT(*) "
										+ "FROM FMS_GTA_INV_FILE_DTL "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt5=conn.prepareStatement(queryString5);
								stmt5.setString(1, own_cd);
								stmt5.setString(2, cont_type);
								stmt5.setString(3, invoice_type);
								stmt5.setString(4, inv_seq);
								stmt5.setString(5, fin_yr);
								stmt5.setString(6, "PG_RECV");
								rset5=stmt5.executeQuery();
								if(rset5.next())
								{
									upload_count=rset5.getInt(1);
								}
								rset5.close();
								stmt5.close();
								
								queryString6="SELECT FILE_NAME "
										+ "FROM FMS_GTA_INV_FILE_DTL "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
										+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
								stmt6=conn.prepareStatement(queryString6);
								stmt6.setString(1, own_cd);
								stmt6.setString(2, cont_type);
								stmt6.setString(3, invoice_type);
								stmt6.setString(4, inv_seq);
								stmt6.setString(5, fin_yr);
								stmt6.setString(6, inv_title);
								rset6=stmt6.executeQuery();
								if(rset6.next())
								{
									VUPLOADED_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								}
								else
								{
									VUPLOADED_FILE_NAME.add("");
								}
								rset6.close();
								stmt6.close();
		
								VFILE_UPLOAD_COUNT.add(upload_count);
								
								VREMITTANCE_NO.add(sys_inv_no);
								VINVOICE_NO.add(inv_no);
								VINVOICE_SEQ.add(inv_seq);
								VFINANCIAL_YEAR.add(fin_yr);
								VSTATUS.add(sts);
								VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
								VPDF_INV_FLAG.add(pdf_inv_flag);
								VSAP_APPROVAL_FLAG.add(sap_approved_flag);
								VTYPE_FLAG.add(payment_type_flag);
								
								VAPPROVE_FLAG_CHECK.add(aprove);
								VCHECK_FLAG_CHECK.add(check);
								VAUTHORIZ_FLAG_CHECK.add(auth);
								VIS_SUBMITTED.add(is_submitted);
							}
						}
						
						if(rem_checkMthEnd == 0)
						{
							break;
						}
					}
		
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.SIP_PAY_FREQ,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? "
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE ";
				//if(!filter_trans_cd.equals(""))
				{
					queryString+= "AND A.COUNTERPARTY_CD=? ";
				}
				//if(!contract_type.equals("") && !cont_no.equals(""))
				{
					queryString+= "AND A.CONTRACT_TYPE=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? ";
				}
				/*else
				{
					queryString+= "AND A.CONTRACT_TYPE IN ("+filter_contType+") ";
				}*/
				String temp_queryString = queryString;
				int stcnt=0;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++stcnt, comp_cd);
				stmt.setString(++stcnt, period_end_dt);
				stmt.setString(++stcnt, period_start_dt);
				stmt.setString(++stcnt, billing_freq);
				stmt.setString(++stcnt, period_end_dt);
				//if(!filter_trans_cd.equals(""))
				{
					stmt.setString(++stcnt, filter_trans_cd);
				}
				//if(!contract_type.equals("") && !cont_no.equals(""))
				{
					stmt.setString(++stcnt, contract_type);
					stmt.setString(++stcnt, agmt_no);
					stmt.setString(++stcnt, agmt_rev_no);
					stmt.setString(++stcnt, cont_no);
				}
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					
					String trans_bu_seq = rset.getString(13)==null?"":rset.getString(13);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(14)==null?"":rset.getString(14);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(15)==null?"":rset.getString(15);
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
								
					String temp_period_start_dt=period_start_dt;
					String temp_period_end_dt=period_end_dt;
					
					/*int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					/*if(utilDate.getDays(period_start_dt, start_dt)<1)
					{
						temp_period_start_dt=start_dt;
					}*/
					
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					if(isGreter>1)
					{
						temp_period_start_dt=billing_eff_dt;
						temp_period_end_dt=period_end_dt;
					}
					else
					{
						temp_period_start_dt=period_start_dt;
						temp_period_end_dt=period_end_dt;
					}
					
					int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					
					/*if(utilDate.getDays(end_dt, period_end_dt)<=0)
					{
						temp_period_end_dt=end_dt;
					}*/
					
					Vector TEMP_INV_COMP_ABBR = new Vector();
					Vector TEMP_INV_COMP_NM = new Vector();
					
					double qty_mmbtu=0;
					double temp_qty_mmbtu=0;
					
					double shipOrPay_qty=0;
					double neg_imb_qty=0;
					double pos_imb_qty=0;
					double unauth_run_qty=0;
					
					double parking_qty=0;
					
					if(invoice_type.equals("TC"))
					{	
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "TP");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "SP");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						if(sip_pay_freq.equals("M")) 
						{	
							String temp_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
							String temp_end_dt=""+utilDate.getLastDateOfMonth(month, year);
							
							if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
							{
								if(utilDate.getDays(end_dt, temp_end_dt)<1)
								{
									temp_end_dt=end_dt;
								}
								if(utilDate.getDays(temp_start_dt, start_dt)<1)
								{
									temp_start_dt=start_dt;
								}
								
								qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								temp_qty_mmbtu=getDeficiencyQty(countpty_cd, cont_type, agmtno, contno, temp_start_dt, temp_end_dt, bu_plant_seq);
								if(temp_qty_mmbtu<0) {
									temp_qty_mmbtu=0;
								}
								
								shipOrPay_qty=temp_qty_mmbtu;
								//temp_qty_mmbtu+=qty_mmbtu;
							}
							else
							{
								qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								//temp_qty_mmbtu=qty_mmbtu;
								shipOrPay_qty=0;
							}
						}
						else
						{
							
							qty_mmbtu=getTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
							//temp_qty_mmbtu=qty_mmbtu;
							shipOrPay_qty=0;
						}
					}
					else if(invoice_type.equals("IC"))
					{
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "NI");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "PI");
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "UR");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
						//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
						//temp_qty_mmbtu=1;
						
						neg_imb_qty=Double.parseDouble(negative_imbalance_qty);
						pos_imb_qty=Double.parseDouble(positive_imbalance_qty);
						unauth_run_qty=Double.parseDouble(unauthorized_overrun_qty);
					}
					else if(invoice_type.equals("PC"))
					{
						VTEMP_INV_COMPONENT.clear();
						getInvoiceComponent(own_cd, countpty_cd, contno, agmtno, trans_bu_seq, bu_plant_seq, cont_type, billing_cycle, invoice_type, temp_period_start_dt, temp_period_end_dt, "PC");
						TEMP_INV_COMP_ABBR=VTEMP_INV_COMPONENT;
						
						parking_qty=getParkingQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
					}
					
					String inv_grp_index_color="";
					int k=0;
					for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
					{
						
						String invCompAbbr=""+TEMP_INV_COMP_ABBR.elementAt(i);
						double tempQty=0;
						String tempCompoNm="";
						if(invCompAbbr.contains("TP"))
						{
							tempQty+=qty_mmbtu;
							tempCompoNm=tempCompoNm.equals("")?"Transportation":tempCompoNm+"<br>Transportation";
						}
						if(invCompAbbr.contains("SP"))
						{
							tempQty+=shipOrPay_qty;
							tempCompoNm=tempCompoNm.equals("")?"Ship-or-Pay":tempCompoNm+"<br>Ship-or-Pay";
						}
						if(invCompAbbr.contains("NI"))
						{
							tempQty+=neg_imb_qty;
							tempCompoNm=tempCompoNm.equals("")?"Negative Imbalance":tempCompoNm+"<br>Negative Imbalance";
						}
						if(invCompAbbr.contains("PI"))
						{
							tempQty+=pos_imb_qty;
							tempCompoNm=tempCompoNm.equals("")?"Positive Imbalance":tempCompoNm+"<br>Positive Imbalance";
						}
						if(invCompAbbr.contains("UR"))
						{
							tempQty+=unauth_run_qty;
							tempCompoNm=tempCompoNm.equals("")?"Unauthorized Overrun":tempCompoNm+"<br>Unauthorized Overrun";
						}
						if(invCompAbbr.contains("PC"))
						{
							tempQty+=parking_qty;
							tempCompoNm=tempCompoNm.equals("")?"Parking":tempCompoNm+"<br>Parking";
						}
						
						if(tempQty > 0)
						{
							inv_index+=1;
							if(k==0)
							{
								inv_grp_index+=1;
								if(inv_grp_index%2==0)
								{
									inv_grp_index_color="#d9ffb3";
								}
							}
							
							VINV_GRP_INDEX.add(inv_grp_index);
							VINV_GRP_INDEX_ROW_ID.add(k);
							VINV_GRP_INDEX_COLOR.add(inv_grp_index_color);
							k++;
							
							VINV_COMPONENTS.add(tempCompoNm);
							VINV_COMPONENTS_ABBR.add(""+TEMP_INV_COMP_ABBR.elementAt(i));
							
							/*VQTY_MMBTU.add(nf3.format(qty_mmbtu));
							if(invoice_type.equals("IC"))
							{
								VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
							}
							else
							{
								VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
							}*/
							
							VQTY_MMBTU.add(nf3.format(tempQty));
							VTEMP_QTY_MMBTU.add(nf3.format(tempQty));
							
							VCOUNTERPTY_CD.add(countpty_cd);
							VCOUNTERPTY_ABBR.add(countpty_abbr);
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VSTART_DT.add(start_dt);
							VEND_DT.add(end_dt);
							VCONT_NAME.add(cont_name);
							VCONT_REF_NO.add(cont_ref_no);
							VCONTRACT_TYPE.add(cont_type);
							VDEAL_NO.add(deal_no);
		
							VTRANS_BU_SEQ.add(trans_bu_seq);
							VTRANS_BU_ABBR.add(trans_bu_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							//VPERIOD_START_DT.add(period_start_dt);
							//VPERIOD_END_DT.add(period_end_dt);
							
							VPERIOD_START_DT.add(temp_period_start_dt);
							VPERIOD_END_DT.add(temp_period_end_dt);
							
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							
							String inv_no="";
							String sys_inv_no="";
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
							
							queryString3="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,"
									+ "PDF_INV_DTL,SAP_APPROVAL,SYS_INV_NO "
									+ "FROM FMS_GTA_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INV_COMPONENT=? ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1, own_cd);
							stmt3.setString(2, countpty_cd);
							stmt3.setString(3, contno);
							stmt3.setString(4, agmtno);
							stmt3.setString(5, trans_bu_seq);
							stmt3.setString(6, bu_plant_seq);
							stmt3.setString(7, cont_type);
							stmt3.setString(8, billing_cycle);
							stmt3.setString(9, invoice_type);
							stmt3.setString(10, temp_period_start_dt);
							stmt3.setString(11, temp_period_end_dt);
							stmt3.setString(12, invCompAbbr);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								//inv_no="SG : "+(rset3.getString(1)==null?"":rset3.getString(1));
								inv_no=rset3.getString(1)==null?"":rset3.getString(1);
								String chk_flg = rset3.getString(2)==null?"":rset3.getString(2);
								String auth_flg = rset3.getString(3)==null?"":rset3.getString(3);
								String aprv_flg = rset3.getString(4)==null?"":rset3.getString(4);
								inv_seq = rset3.getString(5)==null?"":rset3.getString(5);
								fin_yr = rset3.getString(6)==null?"":rset3.getString(6);
								String  pdf_inv_dtl = rset3.getString(7)==null?"":rset3.getString(7);
								
								sys_inv_no=rset3.getString(9)==null?"":rset3.getString(9);
								
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
									
									sap_approved_flag=rset3.getString(8)==null?"":rset3.getString(8);
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
							rset3.close();
							stmt3.close();
							
							queryString4="SELECT INVOICE_NO,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,"
									+ "PDF_INV_DTL,SAP_APPROVAL "
									+ "FROM FMS_GTA_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND INV_COMPONENT=? ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, own_cd);
							stmt4.setString(2, countpty_cd);
							stmt4.setString(3, contno);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, trans_bu_seq);
							stmt4.setString(6, bu_plant_seq);
							stmt4.setString(7, cont_type);
							stmt4.setString(8, billing_cycle);
							stmt4.setString(9, invoice_type);
							stmt4.setString(10, temp_period_start_dt);
							stmt4.setString(11, temp_period_end_dt);
							stmt4.setString(12, invCompAbbr);
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								String chk_flg = rset4.getString(2)==null?"":rset4.getString(2);
								String auth_flg = rset4.getString(3)==null?"":rset4.getString(3);
								String aprv_flg = rset4.getString(4)==null?"":rset4.getString(4);
								String pdf_inv_dtl = rset4.getString(5)==null?"":rset4.getString(5);
								
								
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
									
									sap_approved_flag=rset4.getString(6)==null?"":rset4.getString(6);
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
							rset4.close();
							stmt4.close();
							
							int upload_count=0;
							queryString5="SELECT COUNT(*) "
									+ "FROM FMS_GTA_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, own_cd);
							stmt5.setString(2, cont_type);
							stmt5.setString(3, invoice_type);
							stmt5.setString(4, inv_seq);
							stmt5.setString(5, fin_yr);
							stmt5.setString(6, "PG_RECV");
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								upload_count=rset5.getInt(1);
							}
							rset5.close();
							stmt5.close();
							
							queryString6="SELECT FILE_NAME "
									+ "FROM FMS_GTA_INV_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? "
									+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=?";
							stmt6=conn.prepareStatement(queryString6);
							stmt6.setString(1, own_cd);
							stmt6.setString(2, cont_type);
							stmt6.setString(3, invoice_type);
							stmt6.setString(4, inv_seq);
							stmt6.setString(5, fin_yr);
							stmt6.setString(6, inv_title);
							rset6=stmt6.executeQuery();
							if(rset6.next())
							{
								VUPLOADED_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
							}
							else
							{
								VUPLOADED_FILE_NAME.add("");
							}
							rset6.close();
							stmt6.close();
	
							VFILE_UPLOAD_COUNT.add(upload_count);
							
							VREMITTANCE_NO.add(sys_inv_no);
							VINVOICE_NO.add(inv_no);
							VINVOICE_SEQ.add(inv_seq);
							VFINANCIAL_YEAR.add(fin_yr);
							VSTATUS.add(sts);
							VAPPROVE_INVOICE_FLAG.add(approve_inv_flag);
							VPDF_INV_FLAG.add(pdf_inv_flag);
							VSAP_APPROVAL_FLAG.add(sap_approved_flag);
							VTYPE_FLAG.add(payment_type_flag);
							
							VAPPROVE_FLAG_CHECK.add(aprove);
							VCHECK_FLAG_CHECK.add(check);
							VAUTHORIZ_FLAG_CHECK.add(auth);
							VIS_SUBMITTED.add(is_submitted);
						}
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
	
	public void getInvoiceComponent(String own_cd, String countpty_cd, String contno, String agmtno, String trans_bu_seq, String bu_plant_seq, String cont_type, 
			String billing_cycle,String inv_type, String period_start_dt, String period_end_dt, String inv_comp)
	{
		String function_nm="getInvoiceComponent()";
		
		try
		{
			String actual_inv_component="";
			queryString_temp1="SELECT INV_COMPONENT "
					+ "FROM FMS_GTA_SG_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND FREQ=? AND INVOICE_TYPE=? "
					+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_COMPONENT LIKE ? ";
			stmt_temp1=conn.prepareStatement(queryString_temp1);
			stmt_temp1.setString(1, own_cd);
			stmt_temp1.setString(2, countpty_cd);
			stmt_temp1.setString(3, contno);
			stmt_temp1.setString(4, agmtno);
			stmt_temp1.setString(5, trans_bu_seq);
			stmt_temp1.setString(6, bu_plant_seq);
			stmt_temp1.setString(7, cont_type);
			stmt_temp1.setString(8, billing_cycle);
			stmt_temp1.setString(9, inv_type);
			stmt_temp1.setString(10, period_start_dt);
			stmt_temp1.setString(11, period_end_dt);
			stmt_temp1.setString(12, "%"+inv_comp+"%");
			rset_temp1=stmt_temp1.executeQuery();
			if(rset_temp1.next())
			{
				actual_inv_component=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
			}
			else
			{
				actual_inv_component=inv_comp;
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			if(!VTEMP_INV_COMPONENT.contains(actual_inv_component))
			{
				VTEMP_INV_COMPONENT.add(actual_inv_component);
			}
			
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
	
	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,counterparty_cd, comp_cd, trans_bu_seq, "R");
			bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_unit, "B");

			String queryString="SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_REF_NO,A.CONTRACT_TYPE,"
					+ "A.TRANSPORT_RATE,A.RATE_UNIT,C.INVOICE_CUR_CD,C.PAYMENT_CUR_CD,C.DUE_DATE,"
					+ "C.EXCHNG_RATE_CD,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
					+ "A.POSITIVE_IMB_RATE,A.NEGETIVE_IMB_RATE,A.UNAUTH_OVERRUN_RATE,A.SIP_PAY_FREQ,A.SIP_PAY_RATE,C.HOLIDAY_STATE,A.PARKING_RATE "
					+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? "
					+ "AND A.CONT_NO=? AND A.CONT_REV=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.PLANT_SEQ_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, trans_bu_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String agmtno=rset.getString(1)==null?"0":rset.getString(1);
				String contno=rset.getString(3)==null?"0":rset.getString(3);
				String contrev=rset.getString(4)==null?"0":rset.getString(4);
				cont_start_dt = rset.getString(5)==null?"":rset.getString(5);
				cont_end_dt = rset.getString(6)==null?"":rset.getString(6);
				String contRef=rset.getString(7)==null?"":rset.getString(7);
				String cont_type=rset.getString(8)==null?"":rset.getString(8);
				
				//deal_no=cont_type+agmt_no+"-"+contno+" ["+contRef+"]";
				deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, contrev, cont_type, "")+" ["+contRef+"]";
				this.contRef=contRef;
				price_cd = rset.getString(10)==null?"1":rset.getString(10);
				price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);

				invoice_raised_in = rset.getString(11)==null?"1":rset.getString(11);
				invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
				payment_done_in = rset.getString(12)==null?"1":rset.getString(12);
				payment_done_in_nm=""+utilBean.getRateUnitNm(conn,payment_done_in);
				due_days = rset.getString(13)==null?"0":rset.getString(13);
				exchng_rate_cd = rset.getString(14)==null?"":rset.getString(14);

				consider_due_dt_in=rset.getString(15)==null?"C":rset.getString(15);
				exclude_sat=rset.getString(16)==null?"N":rset.getString(16);
				
				ship_pay_freq=rset.getString(20)==null?"D":rset.getString(20);
				
				if(invoice_type.equals("TC"))
				{
					if(inv_component.contains("TP"))
					{
						price = nf.format(rset.getDouble(9));
					}
					if(inv_component.contains("SP"))
					{
						ship_pay_rate=nf.format(rset.getDouble(21));
					}
				}
				else if(invoice_type.equals("PC"))
				{
					if(inv_component.contains("PC"))
					{
						parking_rate = nf.format(rset.getDouble(23));
					}
				}
				else
				{
					if(inv_component.contains("NI"))
					{
						negative_imbalance_rate = nf.format(rset.getDouble(18));
					}
					if(inv_component.contains("PI"))
					{
						positive_imbalance_rate = nf.format(rset.getDouble(17));
					}
					if(inv_component.contains("UR"))
					{
						unauthorized_overrun_rate = nf.format(rset.getDouble(19));
					}
				}
				
				holiday_state=rset.getString(22)==null?"D":rset.getString(22);
			}
			else
			{
				price="";
				price_cd="2";
				invoice_raised_in="2";

				price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);

				consider_due_dt_in="C";
				exclude_sat="";
				holiday_state="";
						
				positive_imbalance_rate="";
				negative_imbalance_rate="";
				unauthorized_overrun_rate="";
				
				parking_rate="";
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
			String queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
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
	
	public Double getParkingQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getParkingQty()";
		double qty=0;
		try
		{
			double cumulative_imbalance=0;
			String queryString="SELECT SUM((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0))+NVL(ADJ_IMBALANCE,0)) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, agmt_no);
			stmt_temp.setString(6, cont_no);
			stmt_temp.setString(7, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				cumulative_imbalance=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, from_dt);
			stmt_temp.setString(2, to_dt);
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String gas_dt=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double derived_deparking=0;
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0)) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp1=conn.prepareStatement(queryString1);
				stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(2, counterparty_cd);
				stmt_temp1.setString(3, contract_type);
				stmt_temp1.setString(4, gas_dt);
				stmt_temp1.setString(5, agmt_no);
				stmt_temp1.setString(6, cont_no);
				stmt_temp1.setString(7, bu_plant_seq);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					alloc_entry_qty=rset_temp1.getDouble(1);
					alloc_exit_qty=rset_temp1.getDouble(2);
					derived_deparking=rset_temp1.getDouble(3);
				}
				rset_temp1.close();
				stmt_temp1.close();
				
				cumulative_imbalance+=derived_deparking;
				
				qty+=cumulative_imbalance;
			}
			rset_temp.close();
			stmt_temp.close();		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}
	
	public Double getAccrualParkingQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getAccrualParkingQty()";
		double qty=0;
		try
		{
			double cumulative_imbalance=0;
			String queryString="SELECT SUM((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0))+NVL(ADJ_IMBALANCE,0)) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, agmt_no);
			stmt_temp.setString(6, cont_no);
			stmt_temp.setString(7, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				cumulative_imbalance=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, from_dt);
			stmt_temp.setString(2, to_dt);
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String gas_dt=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double derived_deparking=0;
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0)) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp1=conn.prepareStatement(queryString1);
				stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(2, counterparty_cd);
				stmt_temp1.setString(3, contract_type);
				stmt_temp1.setString(4, gas_dt);
				stmt_temp1.setString(5, agmt_no);
				stmt_temp1.setString(6, cont_no);
				stmt_temp1.setString(7, bu_plant_seq);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					alloc_entry_qty=rset_temp1.getDouble(1);
					alloc_exit_qty=rset_temp1.getDouble(2);
					derived_deparking=rset_temp1.getDouble(3);
				}
				rset_temp1.close();
				stmt_temp1.close();
				
				cumulative_imbalance+=derived_deparking;
				
				qty+=cumulative_imbalance;
			}
			rset_temp.close();
			stmt_temp.close();		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}
	
	public Double getTransmissionQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getTransmissionQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, gas_dt);
				stmt.setString(5, agmt_no);
				stmt.setString(6, cont_no);
				stmt.setString(7, bu_plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					var_mdq=rset.getDouble(1);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset.close();
				stmt.close();
				 
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
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
				stmt1.setString(7, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					alloc_entry_qty=rset1.getDouble(1);
					alloc_exit_qty=rset1.getDouble(2);
				}
				rset1.close();
				stmt1.close();
				
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
				
				qty+=transmission_qty;
			}
			rset_temp1.close();
			stmt_temp1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}
	
	public Double getDeficiencyQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getDeficiencyQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			double alloc_entry_qty=0;
			double alloc_exit_qty=0;
			double var_mdq=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					var_mdq+=rset_temp.getDouble(1);
				}
				else
				{
					var_mdq+=Double.parseDouble(mdq);
				}
				rset_temp.close();
				stmt_temp.close();
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			queryString="SELECT SUM(QTY_MMBTU),SUM(EXIT_QTY_MMBTU) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
	  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, to_dt);
			stmt_temp.setString(6, agmt_no);
			stmt_temp.setString(7, cont_no);
			stmt_temp.setString(8, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				alloc_entry_qty=rset_temp.getDouble(1);
				alloc_exit_qty=rset_temp.getDouble(2);
			}
			rset_temp.close();
			stmt_temp.close();
			
			double ship_or_pay_qty= var_mdq * (ship_or_pay_percentage/100);
			
			qty = ship_or_pay_qty - alloc_exit_qty;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}
	
	public void getImbalanceQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getImbalanceQty()";
		try
		{
			double cumulative_imbalance=0;
			double ship_or_pay_percentage=0; 
			
			String cont_start_dt="";
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY') "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
				cont_start_dt=rset_temp.getString(6)==null?"":rset_temp.getString(6);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			int count_day=utilDate.getDays(from_dt, cont_start_dt);
			if(count_day > 1)
			{
				//queryString="SELECT SUM(QTY_MMBTU-EXIT_QTY_MMBTU) "
				queryString="SELECT SUM(((NVL(QTY_MMBTU,0)-NVL(EXIT_QTY_MMBTU,0)) + NVL(ADJ_IMBALANCE,0))) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp1 = conn.prepareStatement(queryString);
				stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(2, counterparty_cd);
				stmt_temp1.setString(3, contract_type);
				stmt_temp1.setString(4, cont_start_dt);
				stmt_temp1.setString(5, from_dt);
				stmt_temp1.setString(6, agmt_no);
				stmt_temp1.setString(7, cont_no);
				stmt_temp1.setString(8, bu_plant_seq);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					cumulative_imbalance=rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
			}
			
			double tot_chargeable_overrun=0;
			double tot_positive_imbalance=0;
			double tot_negitive_imbalance=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				double nom_entry_qty=0;
				double nom_exit_qty=0;
				double sch_entry_qty=0;
				double sch_exit_qty=0;
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				double adj_imbalance=0;
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					var_mdq=rset_temp.getDouble(1);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString1);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					sch_entry_qty=rset_temp.getDouble(1);
					sch_exit_qty=rset_temp.getDouble(2);
				}
				rset_temp.close();
				stmt_temp.close();
				 
				queryString2="SELECT QTY_MMBTU,EXIT_QTY_MMBTU,ADJ_IMBALANCE "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString2);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					alloc_entry_qty=rset_temp.getDouble(1);
					alloc_exit_qty=rset_temp.getDouble(2);
					adj_imbalance=rset_temp.getDouble(3);
				}
				rset_temp.close();
				stmt_temp.close();
				
				//DAILY IMBALANCE CALCULATION
				double daily_imbalance=alloc_entry_qty-alloc_exit_qty;
				
				//CUMULATIVE IMBALANCE CALCULATION
				cumulative_imbalance+=(daily_imbalance  + adj_imbalance);
				
				/*double unauthorized_overrun=alloc_exit_qty-var_mdq;
				if(unauthorized_overrun<=0)
				{
					unauthorized_overrun=0;
				}*/
				
				//UNAUTHORIZED OVERRUN CALCULATION
				double unauthorized_overrun=0;
				/*if(sch_exit_qty > var_mdq)
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
				}*/
				
				//PB20251215: AS PER Incident# ENH 2510060 [25-12-2025 golive schedule]
				//If Scheduled qty < MDQ and Exit Point allocation > Scheduled qty then 
				//Unauthorized over run = Exit Point Allocation - Exit Point Schedule. 
				/*if(Double.doubleToRawLongBits(sch_exit_qty) != Double.doubleToRawLongBits(var_mdq))
				{
					if(alloc_exit_qty > sch_exit_qty)
					{
						unauthorized_overrun=alloc_exit_qty-sch_exit_qty;
					}
				}
				else //sch_exit_qty=var_mdq
				{
					if(alloc_exit_qty > var_mdq)
					{
						unauthorized_overrun=alloc_exit_qty-var_mdq;
					}
				}*/
				
				//As per INC#2600024 and as discussed with Vijay on 20260209, following logic has been changed
				//Detail : Calculation to be revised as shown below for 
				//SCH QTY < MDQ then overrun chargeable to be calculated as Exit point allocated qty - MDQ+10 % of SCH qty. 
				//SCH QTY = MDQ case also to be corrected as Exit point allocated qty - 110 % of MDQ 
				//SCH QTY > MDQ Case Exit point allocation qty - 110 % of SCH QTY to be used.
				if(sch_exit_qty >= var_mdq)
				{
					if(alloc_exit_qty > sch_exit_qty)
					{
						unauthorized_overrun=alloc_exit_qty-sch_exit_qty;
					}
				}
				else if(sch_exit_qty < var_mdq)
				{
					if(alloc_exit_qty > sch_exit_qty)
					{
						unauthorized_overrun=alloc_exit_qty-var_mdq;
					}
				}
				
				if(unauthorized_overrun<=0)
				{
					unauthorized_overrun=0;
				}
				
				//CHARGEABLE OVERRUN CALCULATION
				double chargeable_overrun = 0;//unauthorized_overrun - (var_mdq * 0.1);
				/*
				 * if(chargeable_overrun<=0) { chargeable_overrun=0; }
				 */
				
				/*if(sch_exit_qty > var_mdq)
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
				}*/
				
				//PB20251215: AS PER Incident# ENH 2510060 [25-12-2025 golive schedule]
				//If Scheduled qty < MDQ and Exit Point allocation > 110 % Scheduled qty then 
				//Chargeable over run = Exit Point Allocation - 110 % Exit Point Schedule. 
				/*if(Double.doubleToRawLongBits(sch_exit_qty) != Double.doubleToRawLongBits(var_mdq))
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
				}*/
				
				//As per INC#2600024 and as discussed with Vijay on 20260209, following logic has been changed
				//Detail : Calculation to be revised as shown below for 
				//SCH QTY < MDQ then overrun chargeable to be calculated as Exit point allocated qty - MDQ+10 % of SCH qty. 
				//SCH QTY = MDQ case also to be corrected as Exit point allocated qty - 110 % of MDQ 
				//SCH QTY > MDQ Case Exit point allocation qty - 110 % of SCH QTY to be used.
				
				if(sch_exit_qty >= var_mdq)
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(1.10 *sch_exit_qty);
					}
				}
				else if(sch_exit_qty < var_mdq)
				{
					if(alloc_exit_qty >  1.10 * sch_exit_qty)
					{
						chargeable_overrun=alloc_exit_qty-(var_mdq + (0.1 * sch_exit_qty));
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
				if(negitive_imbalance>=0)
				{
					negitive_imbalance=0;
				}
				else
				{
					negitive_imbalance=negitive_imbalance*(-1);
				}
				
				tot_positive_imbalance+=positive_imbalance;
				tot_negitive_imbalance+=negitive_imbalance;
				tot_chargeable_overrun+=chargeable_overrun;
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			positive_imbalance_qty=nf3.format(tot_positive_imbalance);
			negative_imbalance_qty=nf3.format(tot_negitive_imbalance);
			unauthorized_overrun_qty=nf3.format(tot_chargeable_overrun);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Double getAccrualTransmissionQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getAccrualTransmissionQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				double alloc_entry_qty=0;
				double alloc_exit_qty=0;
				double var_mdq=0;
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, gas_dt);
				stmt.setString(5, agmt_no);
				stmt.setString(6, cont_no);
				stmt.setString(7, bu_plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					var_mdq=rset.getDouble(1);
				}
				else
				{
					var_mdq=Double.parseDouble(mdq);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT COALESCE(ALLOCATION, SCHEDULING, NOMINATION, MDCQ, 0) "
						+ "FROM "
						+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) ALLOCATION, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) SCHEDULING, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) NOMINATION, "
						+ "(NVL("+var_mdq+",0)) MDCQ FROM DUAL)";
				 
				/*queryString1="SELECT QTY_MMBTU,EXIT_QTY_MMBTU "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";*/
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, cont_no);
				stmt1.setString(7, bu_plant_seq);
				stmt1.setString(8, comp_cd);
				stmt1.setString(9, counterparty_cd);
				stmt1.setString(10, contract_type);
				stmt1.setString(11, gas_dt);
				stmt1.setString(12, agmt_no);
				stmt1.setString(13, cont_no);
				stmt1.setString(14, bu_plant_seq);
				stmt1.setString(15, comp_cd);
				stmt1.setString(16, counterparty_cd);
				stmt1.setString(17, contract_type);
				stmt1.setString(18, gas_dt);
				stmt1.setString(19, agmt_no);
				stmt1.setString(20, cont_no);
				stmt1.setString(21, bu_plant_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					//alloc_entry_qty=rset1.getDouble(1);
					alloc_exit_qty=rset1.getDouble(1);
				}
				rset1.close();
				stmt1.close();
				
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
				
				qty+=transmission_qty;
			}
			rset_temp1.close();
			stmt_temp1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}

	public Double getAccrualDeficiencyQty(String counterparty_cd, String contract_type, String agmt_no, String cont_no, String from_dt, String to_dt, String bu_plant_seq)
	{
		String function_nm="getAccrualDeficiencyQty()";
		double qty=0;
		try
		{
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			double alloc_entry_qty=0;
			double alloc_exit_qty=0;
			double var_mdq=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt_temp=conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					var_mdq+=rset_temp.getDouble(1);
				}
				else
				{
					var_mdq+=Double.parseDouble(mdq);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString1="SELECT COALESCE(ALLOCATION, SCHEDULING, NOMINATION, MDCQ, 0) "
						+ "FROM "
						+ "(SELECT (SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) ALLOCATION, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_SCH A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND SCH_REV_NO=(SELECT MAX(SCH_REV_NO) FROM FMS_DAILY_TRANSPORTER_SCH B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) SCHEDULING, "
						+ "(SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) NOMINATION, "
						+ "(NVL("+var_mdq+",0)) MDCQ FROM DUAL)";
				stmt_temp=conn.prepareStatement(queryString1);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, contract_type);
				stmt_temp.setString(4, gas_dt);
				stmt_temp.setString(5, agmt_no);
				stmt_temp.setString(6, cont_no);
				stmt_temp.setString(7, bu_plant_seq);
				stmt_temp.setString(8, comp_cd);
				stmt_temp.setString(9, counterparty_cd);
				stmt_temp.setString(10, contract_type);
				stmt_temp.setString(11, gas_dt);
				stmt_temp.setString(12, agmt_no);
				stmt_temp.setString(13, cont_no);
				stmt_temp.setString(14, bu_plant_seq);
				stmt_temp.setString(15, comp_cd);
				stmt_temp.setString(16, counterparty_cd);
				stmt_temp.setString(17, contract_type);
				stmt_temp.setString(18, gas_dt);
				stmt_temp.setString(19, agmt_no);
				stmt_temp.setString(20, cont_no);
				stmt_temp.setString(21, bu_plant_seq);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					//alloc_entry_qty=rset_temp.getDouble(1);
					alloc_exit_qty+=rset_temp.getDouble(1);
				}
				rset_temp.close();
				stmt_temp.close();
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			/*queryString="SELECT SUM(QTY_MMBTU),SUM(EXIT_QTY_MMBTU) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
	  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND BU_SEQ=? "
					+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, contract_type);
			stmt_temp.setString(4, from_dt);
			stmt_temp.setString(5, to_dt);
			stmt_temp.setString(6, agmt_no);
			stmt_temp.setString(7, cont_no);
			stmt_temp.setString(8, bu_plant_seq);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				alloc_entry_qty=rset_temp.getDouble(1);
				alloc_exit_qty=rset_temp.getDouble(2);
			}
			rset_temp.close();
			stmt_temp.close();*/
			
			double ship_or_pay_qty= var_mdq * (ship_or_pay_percentage/100);
			
			qty = ship_or_pay_qty - alloc_exit_qty;
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return qty;
	}

	public void InvoiceCalculation()
	{
		String function_nm="InvoiceCalculation()";
		try
		{
			int count=0;
			if(!refresh_flg.equals("Y"))
			{
				String queryString="SELECT COUNT(*) "
						+ "FROM FMS_GTA_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fiscal_year);
				stmt.setString(3, invoice_type);
				stmt.setString(4, contract_type);
				stmt.setString(5, inv_seq);
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
				String queryString="SELECT BU_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,SALE_AMT,"
						+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
						+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
						+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,"
						+ "NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
						+ "NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,DEFICIENCY_QTY,DEFICIENCY_AMT,SIP_PAY_RATE,TRANSMISSION_AMT,"
						+ "SIP_PAY_FREQ,TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
						+ "SYS_INV_NO,SAP_APPROVAL,PARKING_QTY,PARKING_RATE,PARKING_AMT "
						+ "FROM FMS_GTA_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fiscal_year);
				stmt.setString(3, invoice_type);
				stmt.setString(4, contract_type);
				stmt.setString(5, inv_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					sg_submission_chk=true;

					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					invoice_seq=rset.getString(2)==null?"":rset.getString(2);
					invoice_no=rset.getString(3)==null?"":rset.getString(3);
					invoice_dt=rset.getString(4)==null?"":rset.getString(4);
					invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
					qty_mmbtu=rset.getString(6)==null?"":nf3.format(rset.getDouble(6));//
					price=rset.getString(7)==null?"":nf.format(rset.getDouble(7));//
					price_cd=rset.getString(8)==null?"":rset.getString(8);
					gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));//
					exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
					exchang_rate_dt=rset.getString(11)==null?"":rset.getString(11);
					exchang_rate=rset.getString(12)==null?"":nf.format(rset.getDouble(12));//
					invoice_raised_in=rset.getString(13)==null?"":rset.getString(13);
					gross_amt1=rset.getString(14)==null?"":nf.format(rset.getDouble(14));//
					tax_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
					tax_struct_cd=rset.getString(16)==null?"":rset.getString(16);
					tax_struct_dt=rset.getString(17)==null?"":rset.getString(17);
					invoice_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));//
					invoice_adj_sign=rset.getString(19)==null?"":rset.getString(19);
					invoice_adj_amt=rset.getString(20)==null?"":nf.format(rset.getDouble(20));//
					net_payable=rset.getString(21)==null?"":nf.format(rset.getDouble(21));//
					invoice_check_flag=rset.getString(22)==null?"":rset.getString(22);
					String chk_by=rset.getString(23)==null?"":rset.getString(23);
					invoice_check_by=utilBean.getEmpName(conn,chk_by);
					invoice_check_dt=rset.getString(24)==null?"":rset.getString(24);
					if(invoice_check_flag.equals("Y"))
					{
						invoice_check_nm="Checked";
					}
					else if(invoice_check_flag.equals("N"))
					{
						invoice_check_nm="Rejected";
					}
					invoice_auth_flag=rset.getString(25)==null?"":rset.getString(25);
					String auth_by=rset.getString(26)==null?"":rset.getString(26);
					invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					invoice_auth_dt=rset.getString(27)==null?"":rset.getString(27);
					if(invoice_auth_flag.equals("Y"))
					{
						invoice_auth_nm="Authorized";
					}
					else if(invoice_auth_flag.equals("N"))
					{
						invoice_auth_nm="Rejected";
					}

					invoice_aprv_flag=rset.getString(28)==null?"":rset.getString(28);
					String aprv_by=rset.getString(29)==null?"":rset.getString(29);
					invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					invoice_aprv_dt=rset.getString(30)==null?"":rset.getString(30);
					if(invoice_aprv_flag.equals("A"))
					{
						invoice_aprv_nm="Approved";
					}
					else if(invoice_aprv_flag.equals("R"))
					{
						invoice_aprv_nm="Rejected";
					}
					
					sg_rem_gen_status=""+getInvoiceStatus(invoice_check_flag, invoice_auth_flag, invoice_aprv_flag);

					financial_year=rset.getString(31)==null?"":rset.getString(31);
					submitted_fiscal_yr=financial_year;
					
					negative_imbalance_qty=rset.getString(32)==null?"":nf3.format(rset.getDouble(32));//
					positive_imbalance_qty=rset.getString(33)==null?"":nf3.format(rset.getDouble(33));//
					unauthorized_overrun_qty=rset.getString(34)==null?"":nf3.format(rset.getDouble(34));//
					positive_imbalance_rate=rset.getString(35)==null?"":nf.format(rset.getDouble(35));//
					negative_imbalance_rate=rset.getString(36)==null?"":nf.format(rset.getDouble(36));//
					unauthorized_overrun_rate=rset.getString(37)==null?"":nf.format(rset.getDouble(37));//
					
					negative_imbalance_amount=rset.getString(38)==null?"":nf.format(rset.getDouble(38));//
					positive_imbalance_amount=rset.getString(39)==null?"":nf.format(rset.getDouble(39));//
					unauthorized_imbalance_amount=rset.getString(40)==null?"":nf.format(rset.getDouble(40));//
					
					deficiency_qty=nf3.format(rset.getDouble(41));
					deficiency_amt=nf.format(rset.getDouble(42));
					ship_pay_rate=nf.format(rset.getDouble(43));
					transmission_amt=nf.format(rset.getDouble(44));
					
					ship_pay_freq=rset.getString(45)==null?"":rset.getString(45);
					
					applicable_abbr=rset.getString(46)==null?"NA":rset.getString(46);
					
					tds_amount=rset.getString(47)==null?"":nf.format(rset.getDouble(47));
					tds_factor=rset.getString(48)==null?"":nf3.format(rset.getDouble(48));
					tds_struct_cd=rset.getString(49)==null?"":rset.getString(49);
					tds_struct_dt=rset.getString(50)==null?"":rset.getString(50);
					sys_invoice_no=rset.getString(51)==null?"":rset.getString(51);
					sap_approval_flag=rset.getString(52)==null?"":rset.getString(52);
					
					parking_qty=rset.getString(53)==null?"":nf3.format(rset.getDouble(53));//
					parking_rate =rset.getString(54)==null?"":nf.format(rset.getDouble(54));//
					parking_amt=nf.format(rset.getDouble(55));

					Vector temp = new Vector();
					temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit,period_start_dt, invoice_type,gross_amt1);

					if(!invoice_check_flag.equals("Y"))
					{
						tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
						tax_struct_cd = ""+temp.elementAt(1);
						tax_struct_dt = ""+temp.elementAt(2);
						tax_info = ""+temp.elementAt(3);
						//tax_struct_dtl = ""+temp.elementAt(4);
						tax_factor = ""+temp.elementAt(5);
					}
					
					//tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit, period_start_dt,invoice_type);
					tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
					
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					Vector VTAX_BASE_AMT = new Vector();
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_GTA_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND INVOICE_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, invoice_type);
					stmt1.setString(4, financial_year);
					stmt1.setString(5, invoice_seq);
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
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_RM=? "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, comp_cd);
				stmt.setString(3, "B");
				stmt.setString(4, "P"+bu_unit);
				stmt.setString(5, "Y");
				stmt.setString(6, "Y");
				stmt.setString(7, "Y");
				stmt.setString(8, "Y");
				stmt.setString(9, "RLNG");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				}
				rset.close();
				stmt.close();*/
				
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
				
				financial_year=utilDate.getFinancialYear(invoice_dt);
				//submitted_fiscal_yr=financial_year;
				
				/*queryString2 = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), -3)) "
						+ " || '-' || "
						+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY'), 9)) "
						+ "FROM DUAL";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, period_end_dt);
				stmt2.setString(2, period_end_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					financial_year=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();*/

				/*String fin_yr="";
				if(!financial_year.equals(""))
				{
					String[] temp = financial_year.split("-");
					fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
				}*/
				
				if(!refresh_flg.equals("Y"))
				{
					/*String inv_seq="1";
					queryString3="SELECT NVL(MAX(INVOICE_SEQ),0) FROM "
							+ "((SELECT INVOICE_SEQ FROM FMS_GTA_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? ) "
							+ "UNION ALL "
							+ "(SELECT INVOICE_SEQ FROM FMS_GTA_SG_INV_MST "
							+ "WHERE COMPANY_CD=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? )) "
							+ "COMBINED";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, financial_year);
					stmt3.setString(3, invoice_type);
					stmt3.setString(4, contract_type);
					stmt3.setString(5, comp_cd);
					stmt3.setString(6, financial_year);
					stmt3.setString(7, invoice_type);
					stmt3.setString(8, contract_type);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						inv_seq = ""+(rset3.getInt(1)+1);
					}
					rset3.close();
					stmt3.close();
	
					invoice_seq = inv_seq;
					
					if(!invoice_seq.equals("") && !contract_type.equals(""))
					{
						String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
						
						sys_invoice_no=invoice_prefix+""+contract_type+invoice_type+utilBean.PrePaddingZero(invoice_seq, 4)+"/"+fin_yr;	
					}*/
				}
				else
				{
					queryString3="SELECT INVOICE_SEQ,INVOICE_NO,SYS_INV_NO,FINANCIAL_YEAR "
							+ "FROM FMS_GTA_SG_INV_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, fiscal_year);
					stmt3.setString(3, invoice_type);
					stmt3.setString(4, contract_type);
					stmt3.setString(5, inv_seq);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						invoice_seq=rset3.getString(1)==null?"":rset3.getString(1);
						invoice_no=rset3.getString(2)==null?"":rset3.getString(2);
						sys_invoice_no=rset3.getString(3)==null?"":rset3.getString(3);
						submitted_fiscal_yr=rset3.getString(4)==null?"":rset3.getString(4);
					}
					rset3.close();
					stmt3.close();
				}

				String temp_qty_mmbtu="";
				String temp_period_start_dt=period_start_dt;
				String temp_period_end_dt=period_end_dt;
				
				int countDays=utilDate.getDays(cont_end_dt, period_end_dt);
				
				if(countDays<1)
				{
					temp_period_end_dt=cont_end_dt;
				}
				if(utilDate.getDays(period_start_dt, cont_start_dt)<1)
				{
					temp_period_start_dt=cont_start_dt;
				}
				int days=0;
				
				if(invoice_type.equals("IC"))
				{
					getImbalanceQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_period_start_dt, temp_period_end_dt, bu_unit);
					
					if(inv_component.contains("NI"))
					{
						temp_qty_mmbtu=temp_qty_mmbtu.equals("")?nf3.format(Double.parseDouble(negative_imbalance_qty)):nf3.format(Double.parseDouble(temp_qty_mmbtu)+Double.parseDouble(negative_imbalance_qty));
					}
					else
					{
						negative_imbalance_qty="";
					}
					if(inv_component.contains("PI"))
					{
						temp_qty_mmbtu=temp_qty_mmbtu.equals("")?nf3.format(Double.parseDouble(positive_imbalance_qty)):nf3.format(Double.parseDouble(temp_qty_mmbtu)+Double.parseDouble(positive_imbalance_qty));
					}
					else
					{
						positive_imbalance_qty="";
					}
					if(inv_component.contains("UR"))
					{
						temp_qty_mmbtu=temp_qty_mmbtu.equals("")?nf3.format(Double.parseDouble(unauthorized_overrun_qty)):nf3.format(Double.parseDouble(temp_qty_mmbtu)+Double.parseDouble(unauthorized_overrun_qty));
					}
					else
					{
						unauthorized_overrun_qty="";
					}
				}
				else if(invoice_type.equals("PC"))
				{
					if(inv_component.contains("PC"))
					{
						double parkqty= getParkingQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_period_start_dt, temp_period_end_dt, bu_unit);
						parking_qty=nf3.format(parkqty);
						
						temp_qty_mmbtu=parking_qty;
					}
				}
				else
				{
					if(ship_pay_freq.equals("M"))
					{
						String temp_month="";
						String temp_year="";
						if(!period_end_dt.equals(""))
						{
							String[] split=period_end_dt.split("/");
							temp_month=split[1];
							temp_year=split[2];
						}
						
						String temp_start_dt=""+utilDate.getFirstDateOfMonth(temp_month, temp_year);
						String temp_end_dt=""+utilDate.getLastDateOfMonth(temp_month, temp_year);
						
						if(billing_cycle.equals("2") || countDays<=1) //ADDING = SIGN IN CONDITION
						{
							days=utilDate.getDays(cont_end_dt, temp_end_dt);
							if(days<1)
							{
								temp_end_dt=cont_end_dt;
							}
							if(utilDate.getDays(temp_start_dt, cont_start_dt)<1)
							{
								temp_start_dt=cont_start_dt;
							}
								
							if(inv_component.contains("TP"))
							{
								qty_mmbtu=nf3.format(getTransmissionQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_period_start_dt, temp_period_end_dt, bu_unit));
								temp_qty_mmbtu=temp_qty_mmbtu.equals("")?nf3.format(Double.parseDouble(qty_mmbtu)):nf3.format(Double.parseDouble(temp_qty_mmbtu)+Double.parseDouble(qty_mmbtu));
							}
							if(inv_component.contains("SP"))
							{
								double temp_def_qty=getDeficiencyQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_start_dt, temp_end_dt, bu_unit);
								if(temp_def_qty>0)
								{
									deficiency_qty=nf3.format(temp_def_qty);
								}
								else
								{
									deficiency_qty=nf3.format(0);
								}
								temp_qty_mmbtu=temp_qty_mmbtu.equals("")?nf3.format(Double.parseDouble(deficiency_qty)):nf3.format(Double.parseDouble(temp_qty_mmbtu)+Double.parseDouble(deficiency_qty));
							}
						}
						else
						{
							qty_mmbtu=nf3.format(getTransmissionQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_period_start_dt, temp_period_end_dt, bu_unit));
							temp_qty_mmbtu=qty_mmbtu;
						}
					}	
					else
					{
						if(inv_component.contains("TP"))
						{
							qty_mmbtu=nf3.format(getTransmissionQty(counterparty_cd, contract_type, agmt_no, cont_no, temp_period_start_dt, temp_period_end_dt, bu_unit));
							temp_qty_mmbtu=qty_mmbtu;
						}
					}
				}
				
				if(Double.parseDouble(temp_qty_mmbtu) > 0)
				{
					double exchng_rate=0;
					double tax=0;
					
					/* 
					queryString="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_EXCHG_RATE_ENTRY A "
							+ "WHERE COMPANY_CD='"+comp_cd+"' AND EXCHG_RATE_CD='"+exchng_rate_cd+"' "
							+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<=TO_DATE('"+invoice_dt+"','DD/MM/YYYY'))";
					rset=stmt.executeQuery(queryString);
					if(rset.next())
					{
						exchng_rate=rset.getDouble(1);
						exchang_rate_dt=rset.getString(2)==null?"":rset.getString(2);
					}
					
					exchang_rate=nf.format(exchng_rate);
					*/

					if(invoice_type.equals("IC"))
					{
						if(inv_component.contains("NI"))
						{
							negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
							gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(negative_imbalance_amount));
						}
						if(inv_component.contains("PI"))
						{
							positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
							gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(positive_imbalance_amount));
						}
						if(inv_component.contains("UR"))
						{
							unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
							gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(unauthorized_imbalance_amount));
						}	
					}
					else if(invoice_type.equals("PC"))
					{
						if(inv_component.contains("PC"))
						{
							parking_amt=nf.format(Double.parseDouble(parking_qty) * Double.parseDouble(parking_rate));
							//gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(parking_amt)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(parking_amt));
							gross_amt=nf.format(Double.parseDouble(parking_amt));
						}
					}
					else
					{
						if(ship_pay_freq.equals("M"))
						{
							if(billing_cycle.equals("2") || countDays<=1) //ADDING = SIGN IN CONDITION
							{
								if(inv_component.contains("TP"))
								{
									transmission_amt=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
									gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(transmission_amt));
								}
								if(inv_component.contains("SP"))
								{
									deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
									gross_amt=gross_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(deficiency_amt));
								}
							}
							else
							{
								gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
								transmission_amt=gross_amt;
							}
						}
						else
						{
							if(inv_component.contains("TP"))
							{
								gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
								transmission_amt=gross_amt;
							}
						}
					}

					if(price_cd.equals("2"))
					{
						gross_amt1 = nf.format(Double.parseDouble(gross_amt) * exchng_rate);
					}
					else
					{
						gross_amt1 = gross_amt;
					}

					Vector temp = new Vector();
					
					temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit,period_start_dt, invoice_type,gross_amt1);

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

					net_payable=invoice_amt;
					
					//TDS calculation - 20230803
					applicable_abbr="TDS";
					
					Vector temp_tcs = new Vector();
					temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "R", "TDS", invoice_type,"S",invoice_dt, gross_amt1);

					tds_amount = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
					tds_struct_cd = ""+temp_tcs.elementAt(1);
					tds_struct_dt = ""+temp_tcs.elementAt(2);
					//tax_info = ""+temp_tcs.elementAt(3);
					//tax_struct_dtl = ""+temp_tcs.elementAt(4);
					tds_factor = ""+temp_tcs.elementAt(5);

					invoice_check_flag="";
					invoice_auth_flag="";
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
		try
		{
			String queryString="SELECT BU_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
					+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,"
					+ "NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
					+ "NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,DEFICIENCY_QTY,DEFICIENCY_AMT,SIP_PAY_RATE,TRANSMISSION_AMT,"
					+ "SIP_PAY_FREQ,TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
					+ "SYS_INV_NO,SAP_APPROVAL,PARKING_QTY,PARKING_RATE,PARKING_AMT "
					+ "FROM FMS_GTA_PG_INV_MST "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, fiscal_year);
			stmt.setString(3, invoice_type);
			stmt.setString(4, contract_type);
			stmt.setString(5, inv_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				pg_submission_chk=true;

				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				pg_invoice_seq=rset.getString(2)==null?"":rset.getString(2);
				pg_invoice_no=rset.getString(3)==null?"":rset.getString(3);
				pg_invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				pg_invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				pg_qty_mmbtu=rset.getString(6)==null?"":nf3.format(rset.getDouble(6));//
				pg_price=rset.getString(7)==null?"":nf.format(rset.getDouble(7));//
				pg_price_cd=rset.getString(8)==null?"":rset.getString(8);
				pg_gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));//
				pg_exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
				pg_exchang_rate_dt=rset.getString(11)==null?"":rset.getString(11);
				pg_exchang_rate=rset.getString(12)==null?"":nf.format(rset.getDouble(12));//
				pg_invoice_raised_in=rset.getString(13)==null?"":rset.getString(13);
				pg_gross_amt1=rset.getString(14)==null?"":nf.format(rset.getDouble(14));//
				pg_tax_amt=rset.getString(15)==null?"":nf.format(rset.getDouble(15));//
				pg_tax_struct_cd=rset.getString(16)==null?"":rset.getString(16);
				pg_tax_struct_dt=rset.getString(17)==null?"":rset.getString(17);
				pg_invoice_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));//
				pg_invoice_adj_sign=rset.getString(19)==null?"":rset.getString(19);
				pg_invoice_adj_amt=rset.getString(20)==null?"":nf.format(rset.getDouble(20));//
				pg_net_payable=rset.getString(21)==null?"":nf.format(rset.getDouble(21));//
				pg_invoice_check_flag=rset.getString(22)==null?"":rset.getString(22);
				String chk_by=rset.getString(23)==null?"":rset.getString(23);
				pg_invoice_check_by=utilBean.getEmpName(conn,chk_by);
				pg_invoice_check_dt=rset.getString(24)==null?"":rset.getString(24);
				if(pg_invoice_check_flag.equals("Y"))
				{
					pg_invoice_check_nm="Checked";
				}
				else if(pg_invoice_check_flag.equals("N"))
				{
					pg_invoice_check_nm="Rejected";
				}
				pg_invoice_auth_flag=rset.getString(25)==null?"":rset.getString(25);
				String auth_by=rset.getString(26)==null?"":rset.getString(26);
				pg_invoice_auth_by=utilBean.getEmpName(conn,auth_by);
				pg_invoice_auth_dt=rset.getString(27)==null?"":rset.getString(27);
				if(pg_invoice_auth_flag.equals("Y"))
				{
					pg_invoice_auth_nm="Authorized";
				}
				else if(pg_invoice_auth_flag.equals("N"))
				{
					pg_invoice_auth_nm="Rejected";
				}

				pg_invoice_aprv_flag=rset.getString(28)==null?"":rset.getString(28);
				String aprv_by=rset.getString(29)==null?"":rset.getString(29);
				pg_invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
				pg_invoice_aprv_dt=rset.getString(30)==null?"":rset.getString(30);
				if(pg_invoice_aprv_flag.equals("A"))
				{
					pg_invoice_aprv_nm="Approved";
				}
				else if(pg_invoice_aprv_flag.equals("R"))
				{
					pg_invoice_aprv_nm="Rejected";
				}

				pg_rem_gen_status=""+getInvoiceStatus(pg_invoice_check_flag, pg_invoice_auth_flag, pg_invoice_aprv_flag);
				
				pg_financial_year=rset.getString(31)==null?"":rset.getString(31);
				
				pg_negative_imbalance_qty=rset.getString(32)==null?"":nf3.format(rset.getDouble(32));//
				pg_positive_imbalance_qty=rset.getString(33)==null?"":nf3.format(rset.getDouble(33));//
				pg_unauthorized_overrun_qty=rset.getString(34)==null?"":nf3.format(rset.getDouble(34));//
				pg_positive_imbalance_rate=rset.getString(35)==null?"":nf.format(rset.getDouble(35));//
				pg_negative_imbalance_rate=rset.getString(36)==null?"":nf.format(rset.getDouble(36));//
				pg_unauthorized_overrun_rate=rset.getString(37)==null?"":nf.format(rset.getDouble(37));//
				
				pg_negative_imbalance_amount=rset.getString(38)==null?"":nf.format(rset.getDouble(38));//
				pg_positive_imbalance_amount=rset.getString(39)==null?"":nf.format(rset.getDouble(39));//
				pg_unauthorized_imbalance_amount=rset.getString(40)==null?"":nf.format(rset.getDouble(40));//
				
				pg_deficiency_qty=rset.getString(41)==null?"":nf3.format(rset.getDouble(41));//
				pg_deficiency_amt=rset.getString(42)==null?"":nf.format(rset.getDouble(42));//
				pg_ship_pay_rate=rset.getString(43)==null?"":nf.format(rset.getDouble(43));//
				pg_transmission_amt=rset.getString(44)==null?"":nf.format(rset.getDouble(44));//
				
				pg_ship_pay_freq=rset.getString(45)==null?"":rset.getString(45);
				
				pg_tds_amount=rset.getString(47)==null?"":nf.format(rset.getDouble(47));
				pg_tds_factor=rset.getString(48)==null?"":nf3.format(rset.getDouble(48));
				pg_tds_struct_cd=rset.getString(49)==null?"":rset.getString(49);
				pg_tds_struct_dt=rset.getString(50)==null?"":rset.getString(50);
				pg_sys_invoice_no=rset.getString(51)==null?"":rset.getString(51);
				pg_sap_approval_flag=rset.getString(52)==null?"":rset.getString(52);
				
				pg_parking_qty=rset.getString(53)==null?"":nf3.format(rset.getDouble(53));//
				pg_parking_rate =rset.getString(54)==null?"":nf.format(rset.getDouble(54));//
				pg_parking_amt=nf.format(rset.getDouble(55));
				
				//pg_tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit, period_start_dt,invoice_type);
			
				pg_tax_struct_dtl=utilBean.getTaxDescr(conn, pg_tax_struct_cd);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_GTA_PG_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, invoice_type);
				stmt1.setString(4, pg_financial_year);
				stmt1.setString(5, pg_invoice_seq);
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
	
	public void getInvoiceDetail()
	{
		String function_nm="getInvoiceDetail()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
			
			String queryString="SELECT CONT_REF_NO  "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contRef=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(SYSDATE,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, "B");
			stmt1.setString(4, bu_contact_person_cd);
			stmt1.setString(5, "P"+bu_unit);
			stmt1.setString(6, "RLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				bu_contact_person_nm=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
            bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, "R", counterparty_cd, trans_bu_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			tax_info=utilBean.getCounterpartyBuPlantTaxInfo(conn,comp_cd, "R", counterparty_cd, trans_bu_seq);
			tax_info=tax_info.replaceAll("\n", "<br>");
			
			bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
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
				String queryString="SELECT BU_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
						+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT, "
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,"
						+ "NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
						+ "NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,DEFICIENCY_QTY,DEFICIENCY_AMT,SIP_PAY_RATE,TRANSMISSION_AMT,"
						+ "SIP_PAY_FREQ,SYS_INV_NO,PARKING_QTY,PARKING_RATE,PARKING_AMT "
						+ "FROM FMS_GTA_PG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, invoice_type);
				stmt.setString(4, contract_type);
				stmt.setString(5, invoice_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					pg_invoice_seq=rset.getString(2)==null?"":rset.getString(2);
					pg_invoice_no=rset.getString(3)==null?"":rset.getString(3);
					pg_invoice_dt=rset.getString(4)==null?"":rset.getString(4);
					pg_invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
					pg_qty_mmbtu=nf3.format(rset.getDouble(6));
					pg_price=nf.format(rset.getDouble(7));
					pg_price_cd=rset.getString(8)==null?"":rset.getString(8);
					pg_gross_amt=nf.format(rset.getDouble(9));
					pg_exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
					pg_exchang_rate_dt=rset.getString(11)==null?"":rset.getString(11);
					pg_exchang_rate=nf.format(rset.getDouble(12));
					pg_invoice_raised_in=rset.getString(13)==null?"":rset.getString(13);
					pg_gross_amt1=nf.format(rset.getDouble(14));
					pg_tax_amt=nf.format(rset.getDouble(15));
					pg_tax_struct_cd=rset.getString(16)==null?"":rset.getString(16);
					pg_tax_struct_dt=rset.getString(17)==null?"":rset.getString(17);
					pg_invoice_amt=nf.format(rset.getDouble(18));
					pg_invoice_adj_sign=rset.getString(19)==null?"":rset.getString(19);
					pg_invoice_adj_amt=nf.format(rset.getDouble(20));
					pg_net_payable=nf.format(rset.getDouble(21));
					pg_invoice_check_flag=rset.getString(22)==null?"":rset.getString(22);
					String chk_by=rset.getString(23)==null?"":rset.getString(23);
					pg_invoice_check_by=utilBean.getEmpName(conn,chk_by);
					pg_invoice_check_dt=rset.getString(24)==null?"":rset.getString(24);
					if(pg_invoice_check_flag.equals("Y"))
					{
						pg_invoice_check_nm="Checked";
					}
					else if(pg_invoice_check_flag.equals("N"))
					{
						pg_invoice_check_nm="Rejected";
					}
					pg_invoice_auth_flag=rset.getString(25)==null?"":rset.getString(25);
					String auth_by=rset.getString(26)==null?"":rset.getString(26);
					pg_invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					pg_invoice_auth_dt=rset.getString(27)==null?"":rset.getString(27);
					if(pg_invoice_auth_flag.equals("Y"))
					{
						pg_invoice_auth_nm="Authorized";
					}
					else if(pg_invoice_auth_flag.equals("N"))
					{
						pg_invoice_auth_nm="Rejected";
					}

					pg_invoice_aprv_flag=rset.getString(28)==null?"":rset.getString(28);
					String aprv_by=rset.getString(29)==null?"":rset.getString(29);
					pg_invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					pg_invoice_aprv_dt=rset.getString(30)==null?"":rset.getString(30);
					if(pg_invoice_aprv_flag.equals("A"))
					{
						pg_invoice_aprv_nm="Approved";
					}
					else if(pg_invoice_aprv_flag.equals("R"))
					{
						pg_invoice_aprv_nm="Rejected";
					}

					pg_financial_year=rset.getString(31)==null?"":rset.getString(31);
					
					pg_negative_imbalance_qty=nf3.format(rset.getDouble(32));
					pg_positive_imbalance_qty=nf3.format(rset.getDouble(33));
					pg_unauthorized_overrun_qty=nf3.format(rset.getDouble(34));
					pg_positive_imbalance_rate=nf.format(rset.getDouble(35));
					pg_negative_imbalance_rate=nf.format(rset.getDouble(36));
					pg_unauthorized_overrun_rate=nf.format(rset.getDouble(37));
					
					pg_negative_imbalance_amount=nf.format(rset.getDouble(38));
					pg_positive_imbalance_amount=nf.format(rset.getDouble(39));
					pg_unauthorized_imbalance_amount=nf.format(rset.getDouble(40));
					
					pg_deficiency_qty=nf3.format(rset.getDouble(41));
					pg_deficiency_amt=nf.format(rset.getDouble(42));
					pg_ship_pay_rate=nf.format(rset.getDouble(43));
					pg_transmission_amt=nf.format(rset.getDouble(44));
					
					pg_ship_pay_freq=rset.getString(45)==null?"":rset.getString(45);
					pg_sys_invoice_no=rset.getString(46)==null?"":rset.getString(46);
					
					pg_parking_qty=nf3.format(rset.getDouble(47));
					pg_parking_rate=nf.format(rset.getDouble(48));
					pg_parking_amt=nf.format(rset.getDouble(49));
					
					//pg_tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit, period_start_dt,invoice_type);
					
					pg_tax_struct_dtl=utilBean.getTaxDescr(conn,pg_tax_struct_cd);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT BU_CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,SALE_AMT,"
						+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
						+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
						+ "INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "CHECKED_FLAG,CHECKED_BY,TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'),AUTHORIZED_FLAG,AUTHORIZED_BY,TO_CHAR(AUTHORIZED_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ "APPROVED_FLAG,APPROVED_BY,TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,"
						+ "NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
						+ "NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,DEFICIENCY_QTY,DEFICIENCY_AMT,SIP_PAY_RATE,TRANSMISSION_AMT,"
						+ "SIP_PAY_FREQ,SYS_INV_NO,PARKING_QTY,PARKING_RATE,PARKING_AMT "
						+ "FROM FMS_GTA_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE=? AND INVOICE_SEQ=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, invoice_type);
				stmt.setString(4, contract_type);
				stmt.setString(5, invoice_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
					invoice_seq=rset.getString(2)==null?"":rset.getString(2);
					invoice_no=rset.getString(3)==null?"":rset.getString(3);
					invoice_dt=rset.getString(4)==null?"":rset.getString(4);
					invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
					qty_mmbtu=nf3.format(rset.getDouble(6));
					price=nf.format(rset.getDouble(7));
					price_cd=rset.getString(8)==null?"":rset.getString(8);
					gross_amt=nf.format(rset.getDouble(9));
					exchng_rate_cd=rset.getString(10)==null?"":rset.getString(10);
					exchang_rate_dt=rset.getString(11)==null?"":rset.getString(11);
					exchang_rate=nf.format(rset.getDouble(12));
					invoice_raised_in=rset.getString(13)==null?"":rset.getString(13);
					gross_amt1=nf.format(rset.getDouble(14));
					tax_amt=nf.format(rset.getDouble(15));
					tax_struct_cd=rset.getString(16)==null?"":rset.getString(16);
					tax_struct_dt=rset.getString(17)==null?"":rset.getString(17);
					invoice_amt=nf.format(rset.getDouble(18));
					invoice_adj_sign=rset.getString(19)==null?"":rset.getString(19);
					invoice_adj_amt=nf.format(rset.getDouble(20));
					net_payable=nf.format(rset.getDouble(21));
					invoice_check_flag=rset.getString(22)==null?"":rset.getString(22);
					String chk_by=rset.getString(23)==null?"":rset.getString(23);
					invoice_check_by=utilBean.getEmpName(conn,chk_by);
					invoice_check_dt=rset.getString(24)==null?"":rset.getString(24);
					if(invoice_check_flag.equals("Y"))
					{
						invoice_check_nm="Checked";
					}
					else if(invoice_check_flag.equals("N"))
					{
						invoice_check_nm="Rejected";
					}
					invoice_auth_flag=rset.getString(25)==null?"":rset.getString(25);
					String auth_by=rset.getString(26)==null?"":rset.getString(26);
					invoice_auth_by=utilBean.getEmpName(conn,auth_by);
					invoice_auth_dt=rset.getString(27)==null?"":rset.getString(27);
					if(invoice_auth_flag.equals("Y"))
					{
						invoice_auth_nm="Authorized";
					}
					else if(invoice_auth_flag.equals("N"))
					{
						invoice_auth_nm="Rejected";
					}

					invoice_aprv_flag=rset.getString(28)==null?"":rset.getString(28);
					String aprv_by=rset.getString(29)==null?"":rset.getString(29);
					invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
					invoice_aprv_dt=rset.getString(30)==null?"":rset.getString(30);
					if(invoice_aprv_flag.equals("A"))
					{
						invoice_aprv_nm="Approved";
					}
					else if(invoice_aprv_flag.equals("R"))
					{
						invoice_aprv_nm="Rejected";
					}

					financial_year=rset.getString(31)==null?"":rset.getString(31);
					
					negative_imbalance_qty=nf3.format(rset.getDouble(32));
					positive_imbalance_qty=nf3.format(rset.getDouble(33));
					unauthorized_overrun_qty=nf3.format(rset.getDouble(34));
					positive_imbalance_rate=nf.format(rset.getDouble(35));
					negative_imbalance_rate=nf.format(rset.getDouble(36));
					unauthorized_overrun_rate=nf.format(rset.getDouble(37));
					
					negative_imbalance_amount=nf.format(rset.getDouble(38));
					positive_imbalance_amount=nf.format(rset.getDouble(39));
					unauthorized_imbalance_amount=nf.format(rset.getDouble(40));
					
					deficiency_qty=nf3.format(rset.getDouble(41));
					deficiency_amt=nf.format(rset.getDouble(42));
					ship_pay_rate=nf.format(rset.getDouble(43));
					transmission_amt=nf.format(rset.getDouble(44));
					
					ship_pay_freq=rset.getString(45)==null?"":rset.getString(45);
					sys_invoice_no=rset.getString(46)==null?"":rset.getString(46);
					
					parking_qty=nf3.format(rset.getDouble(47));
					parking_rate=nf.format(rset.getDouble(48));
					parking_amt=nf.format(rset.getDouble(49));

					/*Vector temp = new Vector();
					temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit,period_start_dt, invoice_type,gross_amt1);

					if(!invoice_check_flag.equals("Y"))
					{
						tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
						tax_struct_cd = ""+temp.elementAt(1);
						tax_struct_dt = ""+temp.elementAt(2);
						tax_info = ""+temp.elementAt(3);
						//tax_struct_dtl = ""+temp.elementAt(4);
						tax_factor = ""+temp.elementAt(5);
					}*/
					
					//tax_struct_dtl=utilBean.getEntityBuTaxStructureDtl(conn,comp_cd, counterparty_cd, "R", trans_bu_seq, bu_unit, period_start_dt,invoice_type);
					
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
				
				invoice_data.put("NegImbQty", pg_negative_imbalance_qty);
				invoice_data.put("NegImbRate", pg_negative_imbalance_rate);
				invoice_data.put("PosImbQty", pg_positive_imbalance_qty);
				invoice_data.put("PosImbRate", pg_positive_imbalance_rate);
				invoice_data.put("UnauthQty", pg_unauthorized_overrun_qty);
				invoice_data.put("UnauthRate", pg_unauthorized_overrun_rate);
				//invoice_data.put("NegImbAmt", pg_negative_imbalance_amount);
				//invoice_data.put("PosImbAmt", pg_positive_imbalance_amount);
				//invoice_data.put("UnauthAmt", pg_unauthorized_imbalance_amount);
				invoice_data.put("NegImbAmt", "");
				invoice_data.put("PosImbAmt", "");
				invoice_data.put("UnauthAmt", "");
				
				invoice_data.put("DeficiencyQty", pg_deficiency_qty);
				//invoice_data.put("DeficiencyAmt", pg_deficiency_amt);
				invoice_data.put("DeficiencyAmt", "");
				invoice_data.put("ShipPayRate", pg_ship_pay_rate);
				invoice_data.put("ShipPayFreq", pg_ship_pay_freq);
				//invoice_data.put("TransmissionAmt", pg_transmission_amt);
				invoice_data.put("TransmissionAmt", "");
				
				invoice_data.put("BillingCycle", billing_cycle);
				invoice_data.put("SysInvNo", pg_sys_invoice_no);
				
				invoice_data.put("ParkingQty", pg_parking_qty);
				invoice_data.put("ParkingRate", pg_parking_rate);
				invoice_data.put("ParkingAmt", "");
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
				
				invoice_data.put("NegImbQty", negative_imbalance_qty);
				invoice_data.put("NegImbRate", negative_imbalance_rate);
				invoice_data.put("PosImbQty", positive_imbalance_qty);
				invoice_data.put("PosImbRate", positive_imbalance_rate);
				invoice_data.put("UnauthQty", unauthorized_overrun_qty);
				invoice_data.put("UnauthRate", unauthorized_overrun_rate);
				invoice_data.put("NegImbAmt", negative_imbalance_amount);
				invoice_data.put("PosImbAmt", positive_imbalance_amount);
				invoice_data.put("UnauthAmt", unauthorized_imbalance_amount);
				
				invoice_data.put("DeficiencyQty", deficiency_qty);
				invoice_data.put("DeficiencyAmt", deficiency_amt);
				invoice_data.put("ShipPayRate", ship_pay_rate);
				invoice_data.put("ShipPayFreq", ship_pay_freq);
				invoice_data.put("TransmissionAmt", transmission_amt);
				
				invoice_data.put("BillingCycle", billing_cycle);
				invoice_data.put("SysInvNo", sys_invoice_no);
				
				invoice_data.put("ParkingQty", parking_qty);
				invoice_data.put("ParkingRate", parking_rate);
				invoice_data.put("ParkingAmt", parking_amt);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGtaSendInvoiceMailDetail()
	{
		String function_nm="getGtaSendInvoiceMailDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			String queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
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
			
			String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
			String subject="";
			String type="";
			
			if(invoice_type.equals("TC"))
			{
				type="Transmission Charges";
			}
			else if(invoice_type.equals("IC"))
			{
				type="Imbalance Charges";
			}
			else if(invoice_type.equals("PC"))
			{
				type="Parking Charges";
			}
			
			String bu_contact_person_cd="";
			String invoiceNo="";
			String invoiceDt="";
			String dueDate="";
			
			if(mail_inv_type.equals("F"))
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,INVOICE_REF,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "OTHER_INV_STR "
						+ "FROM FMS_GTA_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND INVOICE_TYPE=?";
			}
			else
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_GTA_SG_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? "
						+ "AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_TYPE=? AND INVOICE_SEQ=? "
						+ "AND FINANCIAL_YEAR=? ";
			}
			stmt1=conn.prepareStatement(queryString1);
			if(mail_inv_type.equals("F"))
			{
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, invoice_type);
			}
			else
			{
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, trans_bu_seq);
				stmt1.setString(6, bu_unit);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, invoice_type);
				stmt1.setString(9, invoice_seq);
				stmt1.setString(10, financial_year);
			}
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
				invoiceNo=rset1.getString(2)==null?"":rset1.getString(2);
				dueDate=rset1.getString(3)==null?"":rset1.getString(3);
				invoiceDt=rset1.getString(4)==null?"":rset1.getString(4);
				
				if(mail_inv_type.equals("F"))
				{
					type=rset1.getString(5)==null?"":rset1.getString(5);
				}
			}
			rset1.close();
			stmt1.close();			
				
			//get TO list
			String to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","Y");
			
			//get CC list
			String cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","N");
			
			//get BCc list
			String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "RM", "RLNG","B");
			
			Vector VTEMP_ATTACHMENT=new Vector();
			if(mail_inv_type.equals("F"))
			{
				queryString4="SELECT FILE_NAME "
						+ "FROM FMS_GTA_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND CONTRACT_TYPE=? "
	 	        		+ "AND INVOICE_TYPE=?";
			}
			else
			{
				queryString4="SELECT FILE_NAME "
						+ "FROM FMS_GTA_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND INVOICE_TYPE=? AND INVOICE_SEQ=? "
						+ "AND FINANCIAL_YEAR=? AND INV_TITLE!=? ";
			}
			stmt4=conn.prepareStatement(queryString4);
			if(mail_inv_type.equals("F"))
			{
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, invoice_seq);
				stmt4.setString(3, financial_year);
				stmt4.setString(4, contract_type);
				stmt4.setString(5, invoice_type);
			}
			else
			{
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, contract_type);
				stmt4.setString(3, invoice_type);
				stmt4.setString(4, invoice_seq);
				stmt4.setString(5, financial_year);
				stmt4.setString(6, "X");
			}
			rset4=stmt4.executeQuery();
			while(rset4.next())
			{
				VTEMP_ATTACHMENT.add(rset4.getString(1)==null?"":rset4.getString(1));
			}
			rset4.close();
			stmt4.close();
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
			subject=companyAbbr+"/GTA "+type+" Remittance against Invoice# " +invoiceNo+ " for " +customerNm;
			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed "+type+" Remittance against Invoice# " +invoiceNo+ " for " +customerNm
					+ " dated "+invoiceDt.replaceAll("/", "-")+"."
					+ "\nPlease note payment will be processed on or before "+dueDate.replaceAll("/", "-")
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""	
					+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";
			
			
			String sub_folder2="f_flow_invoice";
	        if(invoice_type.equals("TC"))
	        {
	        	sub_folder2="transmission_invoice";
	        }
	        else if(invoice_type.equals("IC"))
	        {
	        	sub_folder2="imbalance_invoice";
	        }
	        else if(invoice_type.equals("PC"))
	        {
	        	sub_folder2="parking_invoice";
	        }
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(VTEMP_ATTACHMENT);
			VMAIL_ATTACHMENT_PATH.add(CommonVariable.gta_inv_path+""+sub_folder2+"//");
			VMAIL_BODY.add(mail_body);	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAttachment2Detail()
	{
		String function_nm="getAttachment2Detail()";
		try
		{
			String from_dt="";
			String to_dt="";
			
			String temp_month="";
			String temp_year="";
			if(!period_end_dt.equals(""))
			{
				String[] split=period_end_dt.split("/");
				temp_month=split[1];
				temp_year=split[2];
			}
			
			from_dt=""+utilDate.getFirstDateOfMonth(temp_month, temp_year);
			to_dt=""+utilDate.getLastDateOfMonth(temp_month, temp_year);
			
			double ship_or_pay_percentage=0; 
			
			String mdq="";
			String sip_pay_percent="";
			String sip_pay_freq="";
			String start_dt="";
			String end_dt="";
			
			String queryString="SELECT MDQ,MDQ_UNIT,SIP_PAY_RATE,SIP_PAY_FREQ,SIP_PAY_PERCENT,CONT_REF_NO,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_GTA_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND AGMT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, contract_type);
			stmt_temp.setString(5, agmt_no);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				mdq=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				sip_pay_freq=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				sip_pay_percent=rset_temp.getString(5)==null?"":rset_temp.getString(5);
				
				ship_pay_freq=sip_pay_freq;
				ship_pay_percent=sip_pay_percent;
				contRef=rset_temp.getString(6)==null?"":rset_temp.getString(6);
				
				start_dt=rset_temp.getString(7)==null?"":rset_temp.getString(7);
				end_dt=rset_temp.getString(8)==null?"":rset_temp.getString(8);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(utilDate.getDays(end_dt, to_dt)<1)
			{
				to_dt=end_dt;
			}
			if(utilDate.getDays(from_dt, start_dt)<1)
			{
				from_dt=start_dt;
			}
			
			if(!sip_pay_percent.equals("")) 
			{
			  ship_or_pay_percentage=Double.parseDouble(sip_pay_percent); 
			}
			  
			if(mdq.equals(""))
			{ 
				mdq="0"; 
			}
			
			double alloc_entry_qty=0;
			double alloc_exit_qty=0;
			double var_mdq=0;
			
			queryString1="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt_temp1=conn.prepareStatement(queryString1);
			stmt_temp1.setString(1, from_dt);
			stmt_temp1.setString(2, to_dt);
			rset_temp1=stmt_temp1.executeQuery();
			while(rset_temp1.next())
			{
				String gas_dt=rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				
				queryString="SELECT MDQ,MDQ_UNIT "
		  				+ "FROM FMS_DAILY_TRANSPORTER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND BU_SEQ=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_TRANSPORTER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, gas_dt);
				stmt.setString(5, agmt_no);
				stmt.setString(6, cont_no);
				stmt.setString(7, bu_unit);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					var_mdq+=rset.getDouble(1);
				}
				else
				{
					var_mdq+=Double.parseDouble(mdq);
				}
				rset.close();
				stmt.close();
			}
			rset_temp1.close();
			stmt_temp1.close();
			
			queryString="SELECT SUM(QTY_MMBTU),SUM(EXIT_QTY_MMBTU) "
	  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
	  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
	  				+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND AGMT_NO=? AND CONT_NO=? "
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
			stmt.setString(4, from_dt);
			stmt.setString(5, to_dt);
			stmt.setString(6, agmt_no);
			stmt.setString(7, cont_no);
			stmt.setString(8, bu_unit);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				alloc_entry_qty=rset.getDouble(1);
				alloc_exit_qty=rset.getDouble(2);
			}
			rset.close();
			stmt.close();
			
			double ship_or_pay_qty= var_mdq * (ship_or_pay_percentage/100);
			
			ship_pay_qty=nf3.format(ship_or_pay_qty);
			transmissionQty=nf3.format(alloc_exit_qty);
			deficiency_qty = nf3.format(ship_or_pay_qty - alloc_exit_qty);
			mdq_qty=nf3.format(var_mdq);
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
			VSEGMENT.add("Transmission Charge");
			VSEGMENT.add("Imbalance Charge");
			VSEGMENT.add("Parking Charge");
			
			VSEGMENT_TYPE.add("TC");
			VSEGMENT_TYPE.add("IC");
			VSEGMENT_TYPE.add("PC");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getGTAInvoiceApproval()
	{
		String function_nm="getGTAInvoiceApproval()";
		try
		{
			int index=0;
			for(int i=0; i<VSEGMENT_TYPE.size(); i++)
			{
				index=0;
				
				String inv_type=""+VSEGMENT_TYPE.elementAt(i);
				String cash_flow=""+VSEGMENT.elementAt(i);
				
				String queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,INVOICE_NO,"
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,"
						+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
						+ "INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SAP_APPROVAL,SYS_INV_NO "
						+ "FROM FMS_GTA_SG_INV_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PDF_INV_DTL IS NOT NULL AND SAP_APPROVAL=? AND INVOICE_TYPE=? "
						+ "AND (SELECT COUNT(*) FROM FMS_GTA_PG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ "
						+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.PDF_INV_DTL IS NOT NULL) = 0 "
						+ "ORDER BY CONT_NO";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, from_dt);
				stmt.setString(3, to_dt);
				stmt.setString(4, "Y");
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					VTYPE_FLAG.add("S");
					VTYPE_NM.add("SG");
					
					String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmt=rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
					String cont=rset.getString(4)==null?"":rset.getString(4);
					String cont_rev=rset.getString(5)==null?"":rset.getString(5);
					String cont_type=rset.getString(6)==null?"":rset.getString(6);
					String buUnit=rset.getString(7)==null?"":rset.getString(7);
					
					//String deal_no=cont_type+agmt+"-"+cont;
					String deal_no=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
					VCONTRACT_TYPE.add(cont_type);
					
					VBU_PLANT_SEQ.add(buUnit);
					VBU_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, buUnit, "B"));
					
					VDEAL_NO.add(deal_no);
					VAGMT_NO.add(agmt);
					VCONT_NO.add(cont);
					VINVOICE_NO.add(rset.getString(8)==null?"":rset.getString(8));
					VINVOICE_DT.add(rset.getString(9)==null?"":rset.getString(9));
					VINVOICE_DUE_DT.add(rset.getString(10)==null?"":rset.getString(10));
					
					VALLOC_QTY.add(rset.getString(11)==null?"":nf.format(rset.getDouble(11)));
					if(inv_type.equals("IC"))
					{
						VTXN_RATE.add("");
					}
					else
					{
						VTXN_RATE.add(rset.getString(12)==null?"":nf.format(rset.getDouble(12)));
					}
					
					String rate_unit=rset.getString(13)==null?"":rset.getString(13);
					VRATE_UNIT.add(utilBean.getRateUnitNm(conn,rate_unit));
					
					String invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					VINVOICE_RAISED_IN.add(utilBean.getRateUnitNm(conn,invoice_raised_in));
					VPAYMENT_DONE_IN.add("INR");
					
					VGROSS_AMT.add(rset.getString(15)==null?"":nf.format(rset.getDouble(15)));
					VTAX_AMT.add(rset.getString(16)==null?"":nf.format(rset.getDouble(16)));
					VINVOICE_AMT.add(rset.getString(17)==null?"":nf.format(rset.getDouble(17)));
					VADJ_SIGN.add(rset.getString(18)==null?"":rset.getString(18));
					VADJ_AMT.add(rset.getString(19)==null?"":nf.format(rset.getDouble(19)));
					
					String tcs_tds=rset.getString(21)==null?"":rset.getString(21);
					double tds_amt=rset.getDouble(22);
					double net_payable=rset.getDouble(20);
					
					VTCS_TDS.add(tcs_tds);
					VTCS_TDS_AMT.add(rset.getString(22)==null?"":nf3.format(rset.getDouble(22)));
					VTCS_TDS_FACTOR.add(rset.getString(23)==null?"":nf3.format(rset.getDouble(23)));
					VTCS_TDS_STRUCT_CD.add(rset.getString(24)==null?"":rset.getString(24));
					VTCS_TDS_EFF_DT.add(rset.getString(25)==null?"":rset.getString(25));
					
					net_payable=net_payable-tds_amt;
					VNET_PAYABLE.add(nf.format(net_payable));
					
					VINVOICE_TYPE.add(rset.getString(26)==null?"":rset.getString(26));
					VINVOICE_SEQ.add(rset.getString(27)==null?"":rset.getString(27));
					
					VFINANCIAL_YEAR.add(rset.getString(28)==null?"":rset.getString(28));
					VSAP_APPROVAL_FLAG.add(rset.getString(29)==null?"":rset.getString(29));
					VREMITTANCE_NO.add(rset.getString(30)==null?"":rset.getString(30));
					
					VCASH_FLOW.add(cash_flow);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,INVOICE_NO,"
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,TRANSPORT_RATE,RATE_UNIT,"
						+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
						+ "TCS_TDS,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
						+ "INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,SAP_APPROVAL,SYS_INV_NO "
						+ "FROM FMS_GTA_PG_INV_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PDF_INV_DTL IS NOT NULL AND SAP_APPROVAL=? AND INVOICE_TYPE=? "
						+ "AND (SELECT COUNT(*) FROM FMS_GTA_SG_INV_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ "
						+ "AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.PDF_INV_DTL IS NOT NULL) = 0 "
						+ "ORDER BY CONT_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, from_dt);
				stmt1.setString(3, to_dt);
				stmt1.setString(4, "Y");
				stmt1.setString(5, inv_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					VTYPE_FLAG.add("P");
					VTYPE_NM.add("PG");
					
					String counterpty_cd=rset1.getString(1)==null?"":rset1.getString(1);
					String agmt=rset1.getString(2)==null?"":rset1.getString(2);
					String agmt_rev=rset1.getString(3)==null?"":rset1.getString(3);
					String cont=rset1.getString(4)==null?"":rset1.getString(4);
					String cont_rev=rset1.getString(5)==null?"":rset1.getString(5);
					String cont_type=rset1.getString(6)==null?"":rset1.getString(6);
					String buUnit=rset1.getString(7)==null?"":rset1.getString(7);
					
					//String deal_no=cont_type+agmt+"-"+cont;
					String deal_no=utilBean.NewDealMappingId(comp_cd,counterpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterpty_cd));
					VCONTRACT_TYPE.add(cont_type);
					
					VBU_PLANT_SEQ.add(buUnit);
					VBU_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, buUnit, "B"));
					
					VDEAL_NO.add(deal_no);
					VAGMT_NO.add(agmt);
					VCONT_NO.add(cont);
					VINVOICE_NO.add(rset1.getString(8)==null?"":rset1.getString(8));
					VINVOICE_DT.add(rset1.getString(9)==null?"":rset1.getString(9));
					VINVOICE_DUE_DT.add(rset1.getString(10)==null?"":rset1.getString(10));
					
					VALLOC_QTY.add(rset1.getString(11)==null?"":nf.format(rset1.getDouble(11)));
					if(inv_type.equals("IC"))
					{
						VTXN_RATE.add("");
					}
					else
					{
						VTXN_RATE.add(rset1.getString(12)==null?"":nf.format(rset1.getDouble(12)));
					}
					
					String rate_unit=rset1.getString(13)==null?"":rset1.getString(13);
					VRATE_UNIT.add(utilBean.getRateUnitNm(conn,rate_unit));
					
					String invoice_raised_in=rset1.getString(14)==null?"":rset1.getString(14);
					VINVOICE_RAISED_IN.add(utilBean.getRateUnitNm(conn,invoice_raised_in));
					VPAYMENT_DONE_IN.add("INR");
					
					VGROSS_AMT.add(rset1.getString(15)==null?"":nf.format(rset1.getDouble(15)));
					VTAX_AMT.add(rset1.getString(16)==null?"":nf.format(rset1.getDouble(16)));
					VINVOICE_AMT.add(rset1.getString(17)==null?"":nf.format(rset1.getDouble(17)));
					VADJ_SIGN.add(rset1.getString(18)==null?"":rset1.getString(18));
					VADJ_AMT.add(rset1.getString(19)==null?"":nf.format(rset1.getDouble(19)));
					
					String tcs_tds=rset1.getString(21)==null?"":rset1.getString(21);
					double tds_amt=rset1.getDouble(22);
					double net_payable=rset1.getDouble(20);
					
					VTCS_TDS.add(tcs_tds);
					VTCS_TDS_AMT.add(rset1.getString(22)==null?"":nf3.format(rset1.getDouble(22)));
					VTCS_TDS_FACTOR.add(rset1.getString(23)==null?"":nf3.format(rset1.getDouble(23)));
					VTCS_TDS_STRUCT_CD.add(rset1.getString(24)==null?"":rset1.getString(24));
					VTCS_TDS_EFF_DT.add(rset1.getString(25)==null?"":rset1.getString(25));
					
					net_payable=net_payable-tds_amt;
					VNET_PAYABLE.add(nf.format(net_payable));
					
					VINVOICE_TYPE.add(rset1.getString(26)==null?"":rset1.getString(26));
					VINVOICE_SEQ.add(rset1.getString(27)==null?"":rset1.getString(27));
					
					VFINANCIAL_YEAR.add(rset1.getString(28)==null?"":rset1.getString(28));
					VSAP_APPROVAL_FLAG.add(rset1.getString(29)==null?"":rset1.getString(29));
					VREMITTANCE_NO.add(rset1.getString(30)==null?"":rset1.getString(30));
					
					VCASH_FLOW.add(cash_flow);
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
			
			//free flow part 
			queryString2="SELECT COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,BU_UNIT,INVOICE_SEQ,INVOICE_REF,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
					+ "FREQ,ALLOC_QTY,'','','',GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "EXCHG_RATE_VALUE,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT, 'Month'),"
					+ "CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG,TCS_CERT_FLAG,TCS_AMT,'',AGMT_NO,FINANCIAL_YEAR,SAP_EXCHNG_RATE,SAP_APPROVAL,"
					+ "TDS_AMT,'',TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),TCS_TDS,INVOICE_TYPE,GROSS_AMT_USD,INVOICE_CATEGORY,INVOICE_NO "
					+ "FROM FMS_GTA_FFLOW_INV_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') AND SAP_APPROVAL=? "
					+ "AND PDF_INV_DTL IS NOT NULL "
					+ "ORDER BY CONT_NO";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, from_dt);
			stmt2.setString(3, to_dt);
			stmt2.setString(4, "Y");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				
				VTYPE_FLAG.add("F");
				VTYPE_NM.add("FFLOW");
				V_TYPE_NM.add("FFLOW");
				
				String counterpty_cd = rset2.getString(1)==null?"":rset2.getString(1);
				String contno=rset2.getString(2)==null?"0":rset2.getString(2);
				String agmtno=rset2.getString(32)==null?"0":rset2.getString(32);
				String financial_year=rset2.getString(33)==null?"":rset2.getString(33);
				String sap_exchng_rate=rset2.getString(34)==null?"":rset2.getString(34);
				String sap_approval_flag=rset2.getString(35)==null?"":rset2.getString(35);
				String cont_type=rset2.getString(3)==null?"":rset2.getString(3);
				String bu_seq=rset2.getString(4)==null?"":rset2.getString(4);
				//String deal_no=cont_type+agmtno+"-"+contno;
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, "");
				
				String inv_chk_flg=rset2.getString(25)==null?"":rset2.getString(25);
				String inv_auth_flg=rset2.getString(26)==null?"":rset2.getString(26);
				String inv_app_flg=rset2.getString(27)==null?"":rset2.getString(27);
				
				//VCONTRACT_TYPE.add(segmentType);
				VFINANCIAL_YEAR.add(financial_year);
				VSAP_APPROVAL_FLAG.add(sap_approval_flag);
				
				VCOUNTERPTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterpty_cd));
				VCONT_NO.add(contno);
				VDEAL_NO.add(deal_no);
				VBU_PLANT_SEQ.add(bu_seq);
				VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
				
				VINVOICE_SEQ.add(rset2.getString(5)==null?"":rset2.getString(5));
				VINVOICE_NO.add(rset2.getString(6)==null?"":rset2.getString(6));
				VINVOICE_DT.add(rset2.getString(7)==null?"":rset2.getString(7));
				VINVOICE_DUE_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
				VPAYMENT_DONE_IN.add("INR");
				
				String period_st_dt = rset2.getString(9)==null?"":rset2.getString(9);
				String period_end_dt = rset2.getString(10)==null?"":rset2.getString(10);
				VPERIOD_START_DT.add(period_st_dt);
				VPERIOD_END_DT.add(period_end_dt);
				//11
				VALLOC_QTY.add(nf.format(rset2.getDouble(12)));
				String rate = ""+rset2.getDouble(13);
				String rate_unit = rset2.getString(14)==null?"":rset2.getString(14);
				//VSALES_PRICE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
				//VSALES_PRICE_UNIT.add(""+utilBean.getRateUnitNm(rate_unit));
				
				//VSALE_AMT.add(nf.format(rset2.getDouble(15)));
				VADJ_SIGN.add(rset2.getString(19)==null?"":rset2.getString(19));
				
				//VEXCHNAGE_RATE.add(rset2.getString(22)==null?"":nf.format(rset2.getDouble(22)));
				//VEXCHNAGE_RATE_DATE.add(rset2.getString(23)==null?"":rset2.getString(23));
				String invoice_raised_in=rset2.getString(24)==null?"":rset2.getString(24);
				VINVOICE_RAISED_IN.add(utilBean.getRateUnitNm(conn,invoice_raised_in));
						
				double temp_gross_amt = rset2.getDouble(16); 	
				double temp_tax_amt = rset2.getDouble(17);	
				double temp_invoice_amt = rset2.getDouble(18); 	
				double temp_tcs_tds_amt = 0;
				double temp_adj_amt = rset2.getDouble(20);
				double temp_net_payable = rset2.getDouble(21);
				double temp_exchang_rate = rset2.getDouble(22);
				
				String tcs_tds_cert=rset2.getString(29)==null?"":rset2.getString(29);
				String tcs_tds=rset2.getString(40)==null?"":rset2.getString(40);
				if(tcs_tds.equals("TCS"))
				{
					VTCS_TDS.add(tcs_tds);
				
					if(invoice_raised_in.equals("2"))
					{
						//VTCS_TDS_AMT_USD.add(rset2.getString(30)==null?"":nf3.format(rset2.getDouble(30)));
						VTCS_TDS_AMT.add(rset2.getString(30)==null?"":nf3.format(rset2.getDouble(30) * temp_exchang_rate));
					}
					else
					{
						VTCS_TDS_AMT.add(rset2.getString(30)==null?"":nf3.format(rset2.getDouble(30)));
						//VTCS_TDS_AMT_USD.add("");
					}
					VTCS_TDS_FACTOR.add(rset2.getString(31)==null?"":nf3.format(rset2.getDouble(31)));
					VTCS_TDS_STRUCT_CD.add("");
					VTCS_TDS_EFF_DT.add("");
					//VTCS_TDS_DONE.add("Y");
				}
				else if(tcs_tds.equals("TDS"))
				{
					VTCS_TDS.add(tcs_tds);
					
					String tds_amt=rset2.getString(36)==null?"":nf3.format(rset2.getDouble(36));
					if(invoice_raised_in.equals("2"))
					{
						//VTCS_TDS_AMT_USD.add(rset2.getString(36)==null?"":nf3.format(rset2.getDouble(36)));
						VTCS_TDS_AMT.add(rset2.getString(36)==null?"":nf3.format(rset2.getDouble(36) * temp_exchang_rate));
					}
					else
					{
						VTCS_TDS_AMT.add(rset2.getString(36)==null?"":nf3.format(rset2.getDouble(36)));
						//VTCS_TDS_AMT_USD.add("");
					}
					String tds_factor=rset2.getString(37)==null?"":nf3.format(rset2.getDouble(37));
					VTCS_TDS_FACTOR.add(tds_factor);
					VTCS_TDS_STRUCT_CD.add(rset2.getString(38)==null?"":rset2.getString(38));
					VTCS_TDS_EFF_DT.add(rset2.getString(39)==null?"":rset2.getString(39));
					//VTCS_TDS_DONE.add("Y");
					
					if(!tds_amt.equals(""))
					{
						temp_net_payable=temp_net_payable-Double.parseDouble(tds_amt);
					}
				}
				else
				{
					VTCS_TDS.add("");
					
					VTCS_TDS_AMT.add("");
					//VTCS_TDS_AMT_USD.add("");
					VTCS_TDS_FACTOR.add(rset2.getString(31)==null?"":nf3.format(rset2.getDouble(31)));
					VTCS_TDS_STRUCT_CD.add("");
					VTCS_TDS_EFF_DT.add("");
					//VTCS_TDS_DONE.add("Y");
				}
				
				String gross_amt = ""; 	
				String tax_amt ="";	
				String invoice_amt = ""; 	
				String tcs_tds_amt = "";
				String adj_amt = "";
				String net_payable ="";
				
				String gross_amt_usd = ""; 	
				String tax_amt_usd = "";	
				String invoice_amt_usd = ""; 	
				String tcs_tds_amt_usd = "";
				String adj_amt_usd = "";
				String net_payable_usd = "";
				
				
				if(invoice_raised_in.equals("2"))
				{
					/*VGROSS_AMT_USD.add(rset2.getString(42)==null?"":nf.format(rset2.getDouble(42)));
					VTAX_AMT_USD.add(rset2.getString(17)==null?"":nf.format(rset2.getDouble(17)));
					VINVOICE_AMT_USD.add(rset2.getString(18)==null?"":nf.format(rset2.getDouble(18)));
					VADJ_AMT_USD.add(rset2.getString(20)==null?"":nf.format(rset2.getDouble(20)));
					VNET_PAYABLE_USD.add(nf.format(temp_net_payable));*/
					
					if(temp_exchang_rate>0)
					{
						VGROSS_AMT.add(rset2.getString(42)==null?"":nf.format(rset2.getDouble(42) * temp_exchang_rate));
						VTAX_AMT.add(rset2.getString(17)==null?"":nf.format(rset2.getDouble(17) * temp_exchang_rate));
						VINVOICE_AMT.add(rset2.getString(18)==null?"":nf.format(rset2.getDouble(18) * temp_exchang_rate));
						VADJ_AMT.add(rset2.getString(20)==null?"":nf.format(rset2.getDouble(20) * temp_exchang_rate));
						VNET_PAYABLE.add(nf.format(temp_net_payable * temp_exchang_rate));
					}
					else
					{
						VGROSS_AMT.add("");
						VTAX_AMT.add("");
						VINVOICE_AMT.add("");
						VADJ_AMT.add("");
						VNET_PAYABLE.add("");
					}	
					
					//VEXCHNAGE_RATE.add(rset2.getString(22)==null?"":nf2.format(rset2.getDouble(22)));
					//VSAP_EXCHANG_FLAG.add("Y");
				}
				else
				{
					VGROSS_AMT.add(rset2.getString(16)==null?"":nf.format(rset2.getDouble(16)));
					VTAX_AMT.add(rset2.getString(17)==null?"":nf.format(rset2.getDouble(17)));
					VINVOICE_AMT.add(rset2.getString(18)==null?"":nf.format(rset2.getDouble(18)));
					VADJ_AMT.add(rset2.getString(20)==null?"":nf.format(rset2.getDouble(20)));
					VNET_PAYABLE.add(nf.format(temp_net_payable));
					
					/*VEXCHNAGE_RATE.add(rset2.getString(22)==null?"":nf2.format(rset2.getDouble(22)));
					VSAP_EXCHANG_FLAG.add(sap_exchng_rate);
					
					VGROSS_AMT_USD.add("");
					VTAX_AMT_USD.add("");
					VINVOICE_AMT_USD.add("");
					VADJ_AMT_USD.add("");
					VNET_PAYABLE_USD.add("");*/
				}
				
				String inv_typ= rset2.getString(41)==null?"":rset2.getString(41);
				VINVOICE_TYPE.add(inv_typ);
				
				String inv_cetegory=rset2.getString(43)==null?"":rset2.getString(43);
				String cashflow="Free Flow";
				/*if(inv_type.equals("LP"))
		    	{
					cashflow="Interest";
		    	}
		    	else if(inv_type.equals("CR") || inv_type.equals("CCR"))
		    	{
		    		//cash_flow="Brokerage/Commission";
		    		cashflow="Commodity";
		    	}
		    	else if(inv_cetegory.equals("P"))
				{
		    		cashflow="Commodity";
				}
				else if(inv_cetegory.equals("S"))
				{
					cashflow="Service";
				}*/
				VCASH_FLOW.add(cashflow);
				VREMITTANCE_NO.add(rset2.getString(44)==null?"":rset2.getString(44));
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateGTAInvoiceXML()
	{
		String function_nm="generateGTAInvoiceXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			
			String accountingPeriodMonth=split[1];
			String accountingPeriodYear=split[2];
			String productionPeriodMonth="";
			String productionPeriodYear="";
			String docHeaderText="";
			String refNum="";
			String exchangeRate="";
			String account=utilBean.getCounterpartySAPcode(conn,counterparty_cd);
			String taxCode="";
			String monthNm="";
			String paymentDueDt="";
			
			String netPayable="";
			String taxStructCd="";
			String taxStructDt="";
			String cont_no="";
			String agmt_no="";
			String gross_amt="";
			String invoiceAmt="";
			String qty="";
			String tcs_tds="";
			String tcsStructCd="";
			String tcsStructDt="";
			String tdsStructCd="";
			String tdsStructDt="";
			String tcs_amt="";
			String tds_amt="";
			String tax_amt="";
			
			String fms_MessageId="";
			String buUnit="";
			
			String plant_seq="";
		    String plantAddress="";
		    String plantCity="";
		    String plantState="";
		    String plantPin="";
		    String plantNm="";
		    
		    String sys_inv_no="";
		    String tds_factor="";
		    
		    String sub_inv_type="";
		    String documentDate="";
			String postingDate="";
			
			String invDt="";
		    
		    if(type_flag.equals("FF"))
			{

		    	String queryString="SELECT TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),BU_UNIT,INVOICE_REF,EXCHG_RATE_VALUE,NET_PAYABLE_AMT,"
						+ "TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),CONT_NO,GROSS_AMT_INR,ALLOC_QTY,TCS_TDS,"
						+ "TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
						+ "TCS_AMT,TDS_AMT,TAX_AMT,INVOICE_RAISED_IN,GROSS_AMT_USD,TO_CHAR(DUE_DT,'DD/MM/YYYY'),INVOICE_AMT,"
						+ "INVOICE_CATEGORY,INVOICE_NO,SUB_INV_TYPE,TO_CHAR(APPROVED_DT,'DD/MM/YYYY') "
						+ "FROM FMS_GTA_FFLOW_INV_MST A "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND COUNTERPARTY_CD=? "
						+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=?";
		    	stmt=conn.prepareStatement(queryString);
		    	stmt.setString(1, comp_cd);
		    	stmt.setString(2, financial_year);
		    	stmt.setString(3, counterparty_cd);//Trans counterparty
		    	stmt.setString(4, invoice_seq);
		    	stmt.setString(5, contract_type);
		    	stmt.setString(6, invoice_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String period_end=rset.getString(1)==null?"":rset.getString(1);
					invDt=period_end;
					if(!period_end.equals(""))
					{
						String[] temp_split=period_end.split("/");
						productionPeriodMonth=temp_split[1];
						productionPeriodYear=temp_split[2];	
						
						documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					monthNm=""+utilDate.getShortMonthName(period_end);
					
					buUnit=rset.getString(2)==null?"":rset.getString(2);
					
					String buStateNm="";
					String buAbbr="";
					queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "B");
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, buUnit);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
						buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					docHeaderText=buStateNm+"/"+buAbbr+" - BU";
					
					refNum=rset.getString(3)==null?"":rset.getString(3);
					exchangeRate=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
					netPayable=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
					
					taxStructCd=rset.getString(6)==null?"":rset.getString(6);
					taxStructDt=rset.getString(7)==null?"":rset.getString(7);
					
					taxCode=utilBean.getTaxSAPcode(conn,taxStructCd);
					cont_no=rset.getString(8)==null?"":rset.getString(8);
					gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
					qty=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
					tcs_tds=rset.getString(11)==null?"":rset.getString(11);
					
					tcsStructCd=rset.getString(12)==null?"":rset.getString(12);
					tcsStructDt=rset.getString(13)==null?"":rset.getString(13);
					tdsStructCd=rset.getString(14)==null?"":rset.getString(14);
					tdsStructDt=rset.getString(15)==null?"":rset.getString(15);
					
					tcs_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
					tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
					
					String inv_raised_in=rset.getString(19)==null?"":rset.getString(19);
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						netPayable=rset.getString(5)==null?"":nf.format(rset.getDouble(5) * Double.parseDouble(exchangeRate));
						
						gross_amt=rset.getString(20)==null?"":nf.format(rset.getDouble(20) * Double.parseDouble(exchangeRate));
						
						tcs_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18) * Double.parseDouble(exchangeRate));
					}
					
					paymentDueDt=rset.getString(21)==null?"":rset.getString(21);
					if(!paymentDueDt.equals(""))
					{
						String splitPayDt[]=paymentDueDt.split("/");
						paymentDueDt=splitPayDt[2]+""+splitPayDt[1]+""+splitPayDt[0];
					}
					
					invoiceAmt=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
					
					//plant_seq=rset.getString(23)==null?"":rset.getString(23);
					
					String inv_category=rset.getString(23)==null?"":rset.getString(23);
					if(inv_category.equals("P"))
					{
						//cash_flow="Commodity";
					}
					else if(inv_category.equals("S"))
					{
						//cash_flow="Service";
					}
					sys_inv_no=rset.getString(24)==null?"":rset.getString(24);
					sub_inv_type=rset.getString(25)==null?"":rset.getString(25);
					
					String approve_dt=rset.getString(26)==null?"":rset.getString(26);
					if(!approve_dt.equals(""))
					{
						String[] temp_split=approve_dt.split("/");
						postingDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					queryString2="SELECT FACTOR "
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
					
					queryString3="SELECT FACTOR "
							+ "FROM FMS_TAX_STRUCTURE_DTL A "
							+ "WHERE TAX_STR_CD=? ";
							//+ "AND APP_DATE=TO_DATE(?,'DD/MM/YYYY') ";
					stmt3=conn.prepareStatement(queryString3);
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
				rset.close();
				stmt.close();
			
			}
			else
			{
				String queryString="";
				if(type_flag.equals("S"))
				{
					queryString="SELECT TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_UNIT,INVOICE_NO,EXCHG_RATE_VALUE,NET_PAYABLE_AMT,"
							+ "TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),CONT_NO,GROSS_AMT,ALLOC_QTY,TCS_TDS,"
							+ "?,?,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
							+ "?,TDS_AMT,TAX_AMT,INVOICE_RAISED_IN,TO_CHAR(DUE_DT,'DD/MM/YYYY'),INVOICE_AMT,TRANS_BU_UNIT,AGMT_NO,"
							+ "SYS_INV_NO,TDS_FACTOR,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,"
							+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),PARKING_QTY "
							+ "FROM FMS_GTA_SG_INV_MST A "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
				}
				else if(type_flag.equals("P"))
				{
					queryString="SELECT TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_UNIT,INVOICE_NO,EXCHG_RATE_VALUE,NET_PAYABLE_AMT,"
							+ "TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),CONT_NO,GROSS_AMT,ALLOC_QTY,TCS_TDS,"
							+ "?,?,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
							+ "?,TDS_AMT,TAX_AMT,INVOICE_RAISED_IN,TO_CHAR(DUE_DT,'DD/MM/YYYY'),INVOICE_AMT,TRANS_BU_UNIT,AGMT_NO,"
							+ "SYS_INV_NO,TDS_FACTOR,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,"
							+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),PARKING_QTY "
							+ "FROM FMS_GTA_PG_INV_MST A "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=? AND INVOICE_TYPE=? ";
				}
				stmt=conn.prepareStatement(queryString);
				if(type_flag.equals("S"))
				{
					stmt.setString(1, "");
					stmt.setString(2, "");
					stmt.setString(3, "");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
				}
				else if(type_flag.equals("P"))
				{
					stmt.setString(1, "");
					stmt.setString(2, "");
					stmt.setString(3, "");
					stmt.setString(4, comp_cd);
					stmt.setString(5, financial_year);
					stmt.setString(6, invoice_seq);
					stmt.setString(7, contract_type);
					stmt.setString(8, invoice_type);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String period_end=rset.getString(1)==null?"":rset.getString(1);
					if(!period_end.equals(""))
					{
						String[] temp_split=period_end.split("/");
						productionPeriodMonth=temp_split[1];
						productionPeriodYear=temp_split[2];		
					}
					monthNm=""+utilDate.getShortMonthName(period_end);
					
					buUnit=rset.getString(2)==null?"":rset.getString(2);
					
					String buStateNm="";
					String buAbbr="";
					queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "B");
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, buUnit);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
						buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					docHeaderText=buStateNm+"/"+buAbbr+" - BU";
					
					refNum=rset.getString(3)==null?"":rset.getString(3);
					exchangeRate=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
					netPayable=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
					
					taxStructCd=rset.getString(6)==null?"":rset.getString(6);
					taxStructDt=rset.getString(7)==null?"":rset.getString(7);
					
					taxCode=utilBean.getTaxSAPcode(conn,taxStructCd);
					cont_no=rset.getString(8)==null?"":rset.getString(8);
					gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
					//qty=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
					tcs_tds=rset.getString(11)==null?"":rset.getString(11);
					
					tcsStructCd=rset.getString(12)==null?"":rset.getString(12);
					tcsStructDt=rset.getString(13)==null?"":rset.getString(13);
					tdsStructCd=rset.getString(14)==null?"":rset.getString(14);
					tdsStructDt=rset.getString(15)==null?"":rset.getString(15);
					
					tcs_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
					tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
					
					String inv_raised_in=rset.getString(19)==null?"":rset.getString(19);
					if(inv_raised_in.equals("2") && !exchangeRate.equals(""))
					{
						netPayable=rset.getString(5)==null?"":nf.format(rset.getDouble(5) * Double.parseDouble(exchangeRate));
						
						gross_amt=rset.getString(9)==null?"":nf.format(rset.getDouble(9) * Double.parseDouble(exchangeRate));
						
						tcs_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16) * Double.parseDouble(exchangeRate));
						tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17) * Double.parseDouble(exchangeRate));
						tax_amt=rset.getString(18)==null?"":nf.format(rset.getDouble(18) * Double.parseDouble(exchangeRate));
					}
					
					paymentDueDt=rset.getString(20)==null?"":rset.getString(20);
					if(!paymentDueDt.equals(""))
					{
						String splitPayDt[]=paymentDueDt.split("/");
						paymentDueDt=splitPayDt[2]+""+splitPayDt[1]+""+splitPayDt[0];
					}
					invoiceAmt=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
					plant_seq=rset.getString(22)==null?"":rset.getString(22);
					agmt_no=rset.getString(23)==null?"":rset.getString(23);
					sys_inv_no=rset.getString(24)==null?"":rset.getString(24);
					tds_factor=rset.getString(25)==null?"":nf.format(rset.getDouble(25));
					
					if(invoice_type.equals("IC"))
					{
						qty=nf3.format(rset.getDouble(27)+rset.getDouble(28)+rset.getDouble(29));
					}
					else if(invoice_type.equals("TC"))
					{
						qty=nf3.format(rset.getDouble(10)+rset.getDouble(26));
					}
					else if(invoice_type.equals("PC"))
					{
						qty=nf3.format(rset.getDouble(32));
					}
					else
					{
						qty="0.000";
					}
					
					String invoiceDt=rset.getString(30)==null?"":rset.getString(30);
					invDt=invoiceDt;
					if(!invoiceDt.equals(""))
					{
						String[] temp_split=invoiceDt.split("/");
						documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					String approve_dt=rset.getString(31)==null?"":rset.getString(31);
					if(!approve_dt.equals(""))
					{
						String[] temp_split=approve_dt.split("/");
						postingDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
				}
				rset.close();
				stmt.close();
			}
		    
			HashMap plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, "R", counterparty_cd, plant_seq);
	        plantAddress=""+plant_detail.get("plant_address");
	        plantCity=""+plant_detail.get("plant_city");
	        plantState=""+plant_detail.get("plant_state");
	        plantPin=""+plant_detail.get("plant_pin");
	        plantNm=""+plant_detail.get("plant_name");
			
			String state_code=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", buUnit);
			
			//String assignmentNo="R"+counterparty_cd+contract_type+agmt_no+"-"+cont_no;
			String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
					
	    	String UserID = ""+utilBean.getUserName(conn,emp_cd);
	    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "R", contract_type, invDt);
	    	
	    	fms_MessageId=sys_inv_no;
	    	fms_MessageId=fms_MessageId.replaceAll("/", "-");
	    	
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		    Document doc = docBuilder.newDocument();
		    
		    //root fmsng
		    Element fmsng = doc.createElement("EmsSAPApMessage");
		    doc.appendChild(fmsng);

		    //root elements
		    Element Header = doc.createElement("Header");
		    fmsng.appendChild(Header);
		    Element Invoice = doc.createElement("Invoice");
		    fmsng.appendChild(Invoice);
		    
		    //Header elements
		    Element MessageId = doc.createElement("MessageId");
		    Element Scope = doc.createElement("Scope");
		    Element DateTimeStamp = doc.createElement("DateTimeStamp");
		    Element DataSource = doc.createElement("DataSource");
		    
		    Header.appendChild(MessageId);
		    Header.appendChild(Scope);
		    Header.appendChild(DateTimeStamp);
		    Header.appendChild(DataSource);
		    
		    MessageId.appendChild(doc.createTextNode(fms_MessageId));
		    Scope.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
		    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
		    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
		    
		    //Invoice elements
		    Element InvoiceHeader = doc.createElement("InvoiceHeader");
		    Invoice.appendChild(InvoiceHeader);
		    
		    Element BusinessActivity = doc.createElement("BusinessActivity");
		    Element DocumentType = doc.createElement("DocumentType");
		    Element DocumentDate = doc.createElement("DocumentDate");
		    Element PostingDate = doc.createElement("PostingDate");
		    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
		    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
		    Element InternalLegalEntity = doc.createElement("InternalLegalEntity");
		    Element DocHeaderText = doc.createElement("DocHeaderText");
		    Element RefNum = doc.createElement("RefNum");
		    Element EmsRefNum = doc.createElement("EmsRefNum");
		    Element Currency = doc.createElement("Currency");
		    Element LocalCurrency = doc.createElement("LocalCurrency");
		    Element ExchangeRate = doc.createElement("ExchangeRate");
		    Element CalculateTax = doc.createElement("CalculateTax");
		    Element TranslationDate = doc.createElement("TranslationDate");
		    Element TradingPartnerBusinessArea = doc.createElement("TradingPartnerBusinessArea");
		    Element AddressLine1 = doc.createElement("AddressLine");
		    Element AddressLine2 = doc.createElement("AddressLine");
		    Element AddressLine3 = doc.createElement("AddressLine");
		    Element AddressLine4 = doc.createElement("AddressLine");
		    Element AddressLine5 = doc.createElement("AddressLine");
		    Element AddressLine6 = doc.createElement("AddressLine");
		    Element AddressLine7 = doc.createElement("AddressLine");
		    Element AddressLine8 = doc.createElement("AddressLine");
		    Element UserName = doc.createElement("UserName");
		    
		    //InvoiceHeader element
		    InvoiceHeader.appendChild(BusinessActivity);
		    InvoiceHeader.appendChild(DocumentType);
		    InvoiceHeader.appendChild(DocumentDate);
		    InvoiceHeader.appendChild(PostingDate);
		    InvoiceHeader.appendChild(AccountingPeriodMonth);
		    InvoiceHeader.appendChild(AccountingPeriodYear);
		    InvoiceHeader.appendChild(InternalLegalEntity);
		    InvoiceHeader.appendChild(DocHeaderText);
		    InvoiceHeader.appendChild(RefNum);
		    InvoiceHeader.appendChild(EmsRefNum);
		    InvoiceHeader.appendChild(Currency);
		    InvoiceHeader.appendChild(LocalCurrency);
		    InvoiceHeader.appendChild(ExchangeRate);
		    InvoiceHeader.appendChild(CalculateTax);
		    InvoiceHeader.appendChild(TranslationDate);
		    InvoiceHeader.appendChild(TradingPartnerBusinessArea);
		    InvoiceHeader.appendChild(AddressLine1);
		    InvoiceHeader.appendChild(AddressLine2);
		    InvoiceHeader.appendChild(AddressLine3);
		    InvoiceHeader.appendChild(AddressLine4);
		    InvoiceHeader.appendChild(AddressLine5);
		    InvoiceHeader.appendChild(AddressLine6);
		    InvoiceHeader.appendChild(AddressLine7);
		    InvoiceHeader.appendChild(AddressLine8);
		    InvoiceHeader.appendChild(UserName);
		    
		    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    DocumentType.appendChild(doc.createTextNode("X1"));
		    //DocumentDate.appendChild(doc.createTextNode(xml_sysdate));
		    //PostingDate.appendChild(doc.createTextNode(xml_sysdate));
		    DocumentDate.appendChild(doc.createTextNode(documentDate));
		    PostingDate.appendChild(doc.createTextNode(postingDate));
		    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
		    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
		    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); //NEED TO CHEACK FOR COMPANY BASE LOGIN JD-20230728
		    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
		    RefNum.appendChild(doc.createTextNode(refNum));
		    //EmsRefNum.appendChild(doc.createTextNode(refNum));
		    EmsRefNum.appendChild(doc.createTextNode(sys_inv_no));
		    Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
		    
		    AddressLine1.appendChild(doc.createTextNode(plantNm));
		    AddressLine2.appendChild(doc.createTextNode(plantAddress));
		    AddressLine3.appendChild(doc.createTextNode(plantState));
		    AddressLine4.appendChild(doc.createTextNode(plantCity));
		    AddressLine5.appendChild(doc.createTextNode(plantPin));
		    
		    UserName.appendChild(doc.createTextNode(UserID));
		    
		    int i=0;
		    if(!netPayable.equals(""))
		    {
		    	//since tds is not deducted from ne_payable
		    	//as per vijay's mail 20230904
		    	/*if(tcs_tds.equals("TDS") && !tds_amt.equals("")) //AS PER VIJAY MAIL ON 19-10-2023 TO ADDRESS SAP LIMITATION FOR TDS
		    	{
		    		netPayable=nf.format(Double.parseDouble(netPayable) - Double.parseDouble(tds_amt));
		    	}*/
		    	String sign = "-";
		    	String pk = "31";
		    	if(invoice_type.equals("CR") || invoice_type.equals("CCR"))
		    	{
		    		pk = "21";
		    		sign = "";
		    	}
		    	else if(invoice_type.equals("LP") && sub_inv_type.equals("CR"))
		    	{
		    		pk = "21";
		    		sign = "";
		    	}	
		    	////
		    	
		    	i+=1;
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    	
		    	Element VendorId  = doc.createElement("VendorId");
		    	Element LineSeqNo = doc.createElement("LineSeqNo");
			    Element PostingKey = doc.createElement("PostingKey");
			   // Element Account = doc.createElement("Account");
			    Element TransactionType = doc.createElement("TransactionType");
			    Element CurrencyAmount = doc.createElement("CurrencyAmount");
			    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");
			    Element TaxCode = doc.createElement("TaxCode");
			    Element BusinessArea = doc.createElement("BusinessArea");
			    Element ItemText = doc.createElement("ItemText");
			    Element Volume = doc.createElement("Volume");
			    Element VolumeUnit = doc.createElement("VolumeUnit");
			    Element ReferenceKey1 = doc.createElement("ReferenceKey1");
			    Element ReferenceKey2 = doc.createElement("ReferenceKey2");
			    Element ProductionPeriod = doc.createElement("ProductionPeriod");
			    Element AssignmentNumber = doc.createElement("AssignmentNumber");
			    Element PaymentTerms = doc.createElement("PaymentTerms");
			    Element PaymentDueDate = doc.createElement("PaymentDueDate");
			    Element Material = doc.createElement("Material");
			    
		    	// InvoiceDetail elements
			    InvoiceDetail.appendChild(VendorId);
			    InvoiceDetail.appendChild(LineSeqNo);
			    InvoiceDetail.appendChild(PostingKey);
			    //InvoiceDetail.appendChild(Account);
			    InvoiceDetail.appendChild(TransactionType);
			    InvoiceDetail.appendChild(CurrencyAmount);
			    InvoiceDetail.appendChild(LocalCurrencyAmount);
			    InvoiceDetail.appendChild(TaxCode);
			    InvoiceDetail.appendChild(BusinessArea);
			    InvoiceDetail.appendChild(ItemText);
			    InvoiceDetail.appendChild(Volume);
			    InvoiceDetail.appendChild(VolumeUnit);
			    InvoiceDetail.appendChild(ReferenceKey1);
			    InvoiceDetail.appendChild(ReferenceKey2);
			    InvoiceDetail.appendChild(ProductionPeriod);
			    InvoiceDetail.appendChild(AssignmentNumber);
			    InvoiceDetail.appendChild(PaymentTerms);
			    InvoiceDetail.appendChild(PaymentDueDate);
			    InvoiceDetail.appendChild(Material);
			    
			    VendorId.appendChild(doc.createTextNode(account));
		    	LineSeqNo.appendChild(doc.createTextNode(""+i));
		    	PostingKey.appendChild(doc.createTextNode(pk));
		    	//Account.appendChild(doc.createTextNode(account)); // Will use VendorId to Send SAP Code
		    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+netPayable));
		    	TaxCode.appendChild(doc.createTextNode(taxCode));
		    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
		    	ItemText.appendChild(doc.createTextNode(sys_inv_no));

		    	Volume.appendChild(doc.createTextNode(qty));
		    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
		    	
		    	//ReferenceKey1.appendChild(doc.createTextNode(counterparty_abbr+"-"+contract_type+cont_no));
		    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
		    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
		    	
		    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
		    	AssignmentNumber.appendChild(doc.createTextNode(sys_inv_no));
		    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt)); //AS PER SUNIDHI MAIL 16/08/2023
		    }
		    
		    if(!gross_amt.equals(""))
		    {
		    	String sign = "";
		    	String pk = "40";
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
		    	
		    	i+=1;
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    	
		    	Element LineSeqNo = doc.createElement("LineSeqNo");
			    Element PostingKey = doc.createElement("PostingKey");
			    Element Account = doc.createElement("Account");
			    Element TransactionType = doc.createElement("TransactionType");
			    Element CurrencyAmount = doc.createElement("CurrencyAmount");
			    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");
			    Element TaxCode = doc.createElement("TaxCode");
			    Element BusinessArea = doc.createElement("BusinessArea");
			    Element ItemText = doc.createElement("ItemText");
			    Element Volume = doc.createElement("Volume");
			    Element VolumeUnit = doc.createElement("VolumeUnit");
			    Element ReferenceKey1 = doc.createElement("ReferenceKey1");
			    Element ReferenceKey2 = doc.createElement("ReferenceKey2");
			    Element ProductionPeriod = doc.createElement("ProductionPeriod");
			    Element AssignmentNumber = doc.createElement("AssignmentNumber");
			    Element PaymentTerms = doc.createElement("PaymentTerms");
			    Element PaymentDueDate = doc.createElement("PaymentDueDate");
			    Element Material = doc.createElement("Material");
			    
		    	// InvoiceDetail elements
			    InvoiceDetail.appendChild(LineSeqNo);
			    InvoiceDetail.appendChild(PostingKey);
			    InvoiceDetail.appendChild(Account);
			    InvoiceDetail.appendChild(TransactionType);
			    InvoiceDetail.appendChild(CurrencyAmount);
			    InvoiceDetail.appendChild(LocalCurrencyAmount);
			    InvoiceDetail.appendChild(TaxCode);
			    InvoiceDetail.appendChild(BusinessArea);
			    InvoiceDetail.appendChild(ItemText);
			    InvoiceDetail.appendChild(Volume);
			    InvoiceDetail.appendChild(VolumeUnit);
			    InvoiceDetail.appendChild(ReferenceKey1);
			    InvoiceDetail.appendChild(ReferenceKey2);
			    InvoiceDetail.appendChild(ProductionPeriod);
			    InvoiceDetail.appendChild(AssignmentNumber);
			    InvoiceDetail.appendChild(PaymentTerms);
			    InvoiceDetail.appendChild(PaymentDueDate);
			    InvoiceDetail.appendChild(Material);
			    
		    	LineSeqNo.appendChild(doc.createTextNode(""+i));
		    	PostingKey.appendChild(doc.createTextNode(pk));
		    	
		    	String tempAccount="";
		    	String itemText="Transport Services";
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
		    	else if(sub_inv_type.equals("PAR") || invoice_type.equals("PC"))
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
		    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); //IG OR NG
		    	//
		    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+gross_amt));
		    	TaxCode.appendChild(doc.createTextNode(taxCode));
		    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
		    	ItemText.appendChild(doc.createTextNode(itemText+" "+monthNm+" "+productionPeriodYear));
		    	
		    	Volume.appendChild(doc.createTextNode(qty));
		    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
		    	
		    	//ReferenceKey1.appendChild(doc.createTextNode(counterparty_abbr+"-"+contract_type+cont_no));
		    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
		    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
		    	
		    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
		    	//AssignmentNumber.appendChild(doc.createTextNode("Commodity "+monthNm+"-"+accountingPeriodYear));
		    	AssignmentNumber.appendChild(doc.createTextNode(sys_inv_no));
		    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt)); //AS PER SUNIDHI MAIL 16/08/2023
		    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("3344036", 18))); //NATURAL GAS MATERIAL CODE
		    }
		    
		    if(!tcs_tds.equals("") && !tcs_tds.equals("NA"))
		    {
		    	i+=1;
		    	
		    	String gl_code="";
		    	String TcsTdscode="";
		    	String amt="";
		    	String pk="";
		    	String taxBase="";
		    	String sign="";
		    	if(tcs_tds.equals("TCS"))
		    	{
		    		gl_code=utilBean.getTaxGLcode(conn,tcsStructCd);
		    		TcsTdscode=utilBean.getTaxSAPcode(conn,tcsStructCd);
		    		amt=tcs_amt;
		    		pk="40";
		    		//taxBase=invoiceAmt;
		    		
		    		/*if(!tcs_amt.equals("") && !tcs_factor.equals(""))
		    		{
		    			taxBase=nf.format((Double.parseDouble(tcs_amt)*100)/Double.parseDouble(tcs_factor));
		    		}*/
		    		
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
		    	}
		    	else if(tcs_tds.equals("TDS"))
		    	{
		    		gl_code=utilBean.getTaxGLcode(conn,tdsStructCd);
		    		TcsTdscode=utilBean.getTaxSAPcode(conn,tdsStructCd);
		    		amt=tds_amt;
		    		pk="50";
		    		sign="-";
		    		//taxBase=gross_amt;
		    		
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
		    	}
		    	
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    			    
			    Element LineSeqNo = doc.createElement("LineSeqNo");
			    Element PostingKey = doc.createElement("PostingKey");
			    Element Account = doc.createElement("Account");
			    Element LineInd = doc.createElement("LineInd");
			    Element TaxAmount = doc.createElement("TaxAmount");
			    Element TaxAmountLocal = doc.createElement("TaxAmountLocal");
			    Element TaxCode = doc.createElement("TaxCode");
			    Element BusinessArea = doc.createElement("BusinessArea");
			    Element TaxType = doc.createElement("TaxType");
			    Element TaxBase = doc.createElement("TaxBase");
			    		    
		    	// InvoiceDetail elements
			    InvoiceDetail.appendChild(LineSeqNo);
			    InvoiceDetail.appendChild(PostingKey);
			    InvoiceDetail.appendChild(Account);
			    InvoiceDetail.appendChild(LineInd);
			    InvoiceDetail.appendChild(TaxAmount);
			    InvoiceDetail.appendChild(TaxAmountLocal);
			    InvoiceDetail.appendChild(TaxCode);
			    InvoiceDetail.appendChild(BusinessArea);
			    InvoiceDetail.appendChild(TaxType);
			    InvoiceDetail.appendChild(TaxBase); //AS PER SUNIDHI MAIL 16/08/2023
			    				    
		    	LineSeqNo.appendChild(doc.createTextNode(""+i));
		    	PostingKey.appendChild(doc.createTextNode(pk));
		    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
		    	LineInd.appendChild(doc.createTextNode(tcs_tds)); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
		    	TaxCode.appendChild(doc.createTextNode(TcsTdscode));
		    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
		    	TaxType.appendChild(doc.createTextNode("V")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	TaxBase.appendChild(doc.createTextNode(taxBase)); //AS PER SUNIDHI MAIL 16/08/2023
		    }
		    
		    if(type_flag.equals("S"))
			{
		    	queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_GTA_SG_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
			}
	    	else if(type_flag.equals("P"))
			{
	    		queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
	    				+ "FROM FMS_GTA_PG_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
				}
	    	else if(type_flag.equals("FF"))
			{
	    		queryString2="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
	    				+ "FROM FMS_GTA_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND INVOICE_SEQ=? AND CONTRACT_TYPE=?";
			}
		    stmt2=conn.prepareStatement(queryString2);
		    if(type_flag.equals("S"))
			{
		    	stmt2.setString(1, comp_cd);
		    	stmt2.setString(2, financial_year);
		    	stmt2.setString(3, invoice_type);
		    	stmt2.setString(4, invoice_seq);
		    	stmt2.setString(5, contract_type);
			}
		    else if(type_flag.equals("P"))
			{
		    	stmt2.setString(1, comp_cd);
		    	stmt2.setString(2, financial_year);
		    	stmt2.setString(3, invoice_type);
		    	stmt2.setString(4, invoice_seq);
		    	stmt2.setString(5, contract_type);
			}
		    else if(type_flag.equals("FF"))
			{
		    	stmt2.setString(1, comp_cd);
		    	stmt2.setString(2, financial_year);
		    	stmt2.setString(3, invoice_type);
		    	stmt2.setString(4, invoice_seq);
		    	stmt2.setString(5, contract_type);
			}
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
			    	//if(Double.parseDouble(tax_amt)>0) AS DISCUSSED WITH VIJAY, SEND ZERO TAX 
			    	{
			    		String pk="40";
				    	String sign="";
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
			    		
				    	i+=1;
				    	
				    	String gl_code="";
				    	String tax_sap_code="";
				    	String amt=taxAmt;
				    	gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
				    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
				    	
				    	if(Double.parseDouble(amt) > 0)
				    	{
					    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
					    	Invoice.appendChild(InvoiceDetail);
					    	
					    	Element LineSeqNo = doc.createElement("LineSeqNo");
						    Element PostingKey = doc.createElement("PostingKey");
						    Element Account = doc.createElement("Account");
						    Element LineInd = doc.createElement("LineInd");
						    Element TaxAmount = doc.createElement("TaxAmount");
						    Element TaxAmountLocal = doc.createElement("TaxAmountLocal");
						    Element TaxCode = doc.createElement("TaxCode");
						    Element BusinessArea = doc.createElement("BusinessArea");
						    Element TaxType = doc.createElement("TaxType");		
						    Element TaxBase = doc.createElement("TaxBase");
						    
					    	// InvoiceDetail elements
						    InvoiceDetail.appendChild(LineSeqNo);
						    InvoiceDetail.appendChild(PostingKey);
						    InvoiceDetail.appendChild(Account);
						    InvoiceDetail.appendChild(LineInd);
						    InvoiceDetail.appendChild(TaxAmount);
						    InvoiceDetail.appendChild(TaxAmountLocal);
						    InvoiceDetail.appendChild(TaxCode);
						    InvoiceDetail.appendChild(BusinessArea);
						    InvoiceDetail.appendChild(TaxType);
						    InvoiceDetail.appendChild(TaxBase); //AS PER SUNIDHI MAIL 16/08/2023
						    				    
					    	LineSeqNo.appendChild(doc.createTextNode(""+i));
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
					    	LineInd.appendChild(doc.createTextNode("T")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
					    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
					    	TaxCode.appendChild(doc.createTextNode(tax_sap_code));
					    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
					    	TaxType.appendChild(doc.createTextNode("V")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
					    	TaxBase.appendChild(doc.createTextNode(taxBaseAmt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	}
			    	}
			    }
			}
			rset2.close();
			stmt2.close();
		    
		    
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
			
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    //AR_<CompanyCD><Entity-Type><Counterparty_cd><Inv-key><InvSEQ#><ContType>_<financialYear>_datetime.xml JD - 20230729
		    String xmlFileNm="";
		    String datetime="";
		    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");		    
		    
		    if(!fms_MessageId.equals(""))
		    {
		    	if(sap_approval_flag.equals("Y"))
		        {
		    		xmlFileNm="AP_"+fms_MessageId+"_"+datetime+".xml";
		        }
		    	else
		    	{
		    		xmlFileNm="AP_"+fms_MessageId+".xml";
		    	}
		    }
		    
		    if(!xmlFileNm.equals(""))
		    {
		    	String appPath = request.getServletContext().getRealPath("");
	        	
	        	String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				File MainDir = new File(appPath+File.separator+main_folder);
		        if(!MainDir.exists()) 
		        {
		        	MainDir.mkdir();
		        }
		        String sub_folder=""+CommonVariable.sap_xml;
		        if(!sap_approval_flag.equals("Y"))
		        {
		        	sub_folder=""+CommonVariable.temp_sap_xml;
		        }
				File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
		        if(!SubDir.exists()) 
		        {
		        	SubDir.mkdir();
		        }
		        
			    StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
			    transformer.transform(source, result);
			    
			    xmlfile_name=xmlFileNm;
			    
			    if(sap_approval_flag.equals("Y"))
		        {
			    	File fileExi = new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+xmlFileNm);
			    	if(fileExi.exists()) 
			        {
			    		int count=0;
			    		String queryString="SELECT COUNT(*) "
				        		+ "FROM FMS_GTA_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
				        		+ "AND INV_TITLE=? AND INVOICE_TYPE=?";
				        stmt3=conn.prepareStatement(queryString);
				        stmt3.setString(1, comp_cd);
				        stmt3.setString(2, contract_type);
				        stmt3.setString(3, invoice_seq);
				        stmt3.setString(4, financial_year);
				        stmt3.setString(5, "X");
				        stmt3.setString(6, invoice_type);
				        rset3=stmt3.executeQuery();
				        if(rset3.next())
				        {
				        	count=rset3.getInt(1);
				        }
				        rset3.close();
				        stmt3.close();
				        
				        if(count > 0)
				        {
				        	queryString4="UPDATE FMS_GTA_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				 	        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
				 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
				 	        		+ "AND INV_TITLE=? AND INVOICE_TYPE=?";
				        	stmt4=conn.prepareStatement(queryString4);
				        	stmt4.setString(1, xmlFileNm);
						    stmt4.setString(2, emp_cd);
					        stmt4.setString(3, comp_cd);
					        stmt4.setString(4, contract_type);
					        stmt4.setString(5, invoice_seq);
					        stmt4.setString(6, financial_year);
					        stmt4.setString(7, "X");
					        stmt4.setString(8, invoice_type);
				        	stmt4.executeUpdate();
				        	
				        	stmt4.close();
				        }
				        else
				        {
				        	queryString4="INSERT INTO FMS_GTA_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT,INVOICE_TYPE) "
				        			+ "VALUES(?,?,?,?,?,"
				        			+ "?,?,SYSDATE,?)";
				        	stmt4=conn.prepareStatement(queryString4);
					        stmt4.setString(1, comp_cd);
					        stmt4.setString(2, contract_type);
					        stmt4.setString(3, invoice_seq);
					        stmt4.setString(4, financial_year);
					        stmt4.setString(5, "X");
					        stmt4.setString(6, xmlFileNm);
						    stmt4.setString(7, emp_cd);
					        stmt4.setString(8, invoice_type);
				        	stmt4.executeUpdate();
				        	
				        	stmt4.close();
				        }
			        }
		        }
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void parseSAP_XMLfile()
	{
		String function_nm="parseSAP_XMLfile()";
		try
		{
			counterparty_nm=utilBean.getCounterpartyName(conn,counterparty_cd);
			counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			
			if(xmlfile_name.equals(""))
			{
				String fms_MessageId="";
			    
				String queryString="SELECT FILE_NAME "
		        		+ "FROM FMS_GTA_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		        		+ "AND INV_TITLE=? AND INVOICE_TYPE=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
		        stmt.setString(2, contract_type);
		        stmt.setString(3, invoice_seq);
		        stmt.setString(4, financial_year);
		        stmt.setString(5, "X");
		        stmt.setString(6, invoice_type);
		        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	xmlfile_name=rset.getString(1)==null?"":rset.getString(1);
		        }
		        rset.close();
		        stmt.close();
			}
			
			//System.out.println(file_path+xmlfile_name);
			if(!xmlfile_name.equals(""))
			{
				String final_path=file_path+File.separator+xmlfile_name;
				
				if(sap_approval_flag.equals("Y"))
				{
					File fXmlFile = new File(file_path+File.separator+xmlfile_name);
					if(fXmlFile.exists())
					{
						final_path=file_path+File.separator+xmlfile_name;
					}
					else
					{
						String new_path=file_path+File.separator+CommonVariable.sap_xml_success+File.separator+xmlfile_name;
						final_path=new_path;
					}
				}
				
				File file = new File(final_path);
				if(file.exists())
				{
					//System.out.println("Created file is in folder");
					
				    DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
				    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    Document doc = dBuilder.parse(file);
				    
				    doc.getDocumentElement().normalize();
				    
				    zeroTotal="0.00";
				    
					NodeList nList = doc.getElementsByTagName("EmsSAPApMessage");
					if(nList.getLength() <= 0)
					{
						nList = doc.getElementsByTagName("FmsngSAPApMessage");
					}
					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						Node nNode = nList.item(temp);
						Element eElement = (Element) nNode;
						
						NodeList nodes = eElement.getChildNodes();
						for(int i=0; i<nodes.getLength(); i++)
						{
							Node node = nodes.item(i);
							String childTag = node.getNodeName();
							//System.out.println(childTag);
							if(childTag.equalsIgnoreCase("Header"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									if(childTag1.equalsIgnoreCase("MessageId"))
									{
										documentNo=nodes1.item(j).getTextContent();
									}
								}
							}
							else if(childTag.equalsIgnoreCase("Invoice"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									//System.out.println("childTag1="+childTag1);
									if(childTag1.equalsIgnoreCase("InvoiceHeader"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();
										
										for(int k=0; k<nodes2.getLength(); k++)
										{
											Node node2 = nodes2.item(k);
											String childTag2 = node2.getNodeName();
											
											//System.out.println("childTag2="+childTag2);
											if(childTag2.equals("DocumentType"))
											{
												//System.out.println(k+""+nodes2.item(k).getTextContent());
												documentType=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("DocumentDate"))
											{
												documentDate=nodes2.item(k).getTextContent();											
											}
											else if(childTag2.equals("DocumentNo"))
											{
												//documentNo=nodes2.item(k).getTextContent();											
											}
											else if(childTag2.equals("PostingDate")) 
											{
												postingDate=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("AccountingPeriodMonth")) 
											{
												accountingPeriodMonth=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("AccountingPeriodYear")) 
											{
												accountingPeriodYear=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("InternalLegalEntity")) 
											{
												headerCompanyCode=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("DocHeaderText")) 
											{
												docHeaderText=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("RefNum"))
											{
												refNum=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Currency")) 
											{
												currency=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("LocalCurrency")) {}
											else if(childTag2.equals("ExchangeRate")) {}
											else if(childTag2.equals("CalculateTax")) {}
											else if(childTag2.equals("TranslationDate")) {}
											else if(childTag2.equals("TradingPartnerBusinessArea")) {}
										}
									}	
									else if(childTag1.equalsIgnoreCase("InvoiceDetail"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();
										
										String lineseqno= "";
										String postingkey= "";
										String account= "";
										String currencyamount= "";
										String businessarea= "";
										String itemtext= "";
										String taxCode= "";
										
										for(int k=0; k<nodes2.getLength(); k++)
										{
											Node node2 = nodes2.item(k);
											String childTag2 = node2.getNodeName();
											
											//System.out.println("childTag2="+childTag2);
											if(childTag2.equals("LineSeqNo")) 
											{
												lineseqno=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("PostingKey")) 
											{
												postingkey=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Account") || childTag2.equals("VendorId")) 
											{
												account=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("TransactionType")) {}
											else if(childTag2.equals("CurrencyAmount") || childTag2.equals("TaxAmount")) 
											{
												currencyamount=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("LocalCurrencyAmount")) {}
											else if(childTag2.equals("BusinessArea")) 
											{
												businessarea=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("ItemText") || childTag2.equals("LineInd")) 
											{
												itemtext=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("TaxCode")) 
											{
												taxCode=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Volume")) {}
											else if(childTag2.equals("VolumeUnit")) {}
											else if(childTag2.equals("ReferenceKey1")) {}
											else if(childTag2.equals("ReferenceKey2")) {}
											else if(childTag2.equals("ProductionPeriod")) {}
											else if(childTag2.equals("AssignmentNumber")) {}
										}
										
										if(!itemtext.equals("TDS")) //AS PER VIJAY MAIL ON 19-10-2023 TO ADDRESS SAP LIMITATION FOR TDS
										{
											if(!currencyamount.equals(""))
											{
												zeroTotal=nf.format(Double.parseDouble(zeroTotal)+Double.parseDouble(currencyamount));
											}
										}
										
										if(itemtext.equals("T") || itemtext.equals("TDS") || itemtext.equals("TCS"))
										{
											itemtext=itemtext+" ["+taxCode+"]";
										}
										
										VLINESEQNO.add(lineseqno);
										VPOSTINGKEY.add(postingkey);
										VACCOUNT.add(account);
										VCURRENCYAMOUNT.add(currencyamount);
										VBUSINESSAREA.add(businessarea);
										VITEMTEXT.add(itemtext);
										VSHORTTEXT.add(utilBean.getGLdesc(conn, ""+utilBean.RemovePrePaddingZero(account)));
									}	
								}	
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void forAllBillingOptionForAccrual()
	{
		String function_nm="forAllBillingOptionForAccrual()";
		try
		{
			if(!report_dt.equals(""))
			{
				String split_dt[]=report_dt.split("/");
				month=split_dt[1];
				year=split_dt[2];
				
				report_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				report_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			
			//if(!month.equals("00"))
			{
				/*if(!month.equals("") && !year.equals(""))
				{
					String temp_billing_cycle=billing_cycle;
					for(int i=1; i<=9; i++)
					{
						billing_cycle=""+i;
						getBillingCyclePeriod();
						getAccrualList();
					}
					billing_cycle=temp_billing_cycle;
				}*/
			}
			//else
			{
				if(!month.equals("") && !year.equals(""))
				{
				 	int temp_month=Integer.parseInt(month);
						
					for(int j=1;j<=temp_month;j++)
					{
						if(j<=9)
						{
							month="0"+j;
						}
						else
						{
							month=""+j;
						}
						if(!month.equals("") && !year.equals(""))
						{
							String temp_billing_cycle=billing_cycle;
							for(int i=1; i<=8; i++)
							{
								if(i==1 || i==2 || i==8)
								{
									billing_cycle=""+i;
									getBillingCyclePeriod();
									getAccrualList();
								}
							}
							billing_cycle=temp_billing_cycle;
						}
					}
				}
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccrualList()
	{
		String function_nm="getAccrualList()";
		try
		{
			Vector TEMP_INV_COMP_ABBR = new Vector();
			Vector TEMP_INV_COMP_NM = new Vector();
			
			String filterContTyp="'R','C'"; 
			if(invoice_type.equals("TC"))
			{
				TEMP_INV_COMP_NM.add("Transportation");
				TEMP_INV_COMP_NM.add("Ship-or-Pay");
				
				TEMP_INV_COMP_ABBR.add("TP");
				TEMP_INV_COMP_ABBR.add("SP");
			}
			else if(invoice_type.equals("IC"))
			{
				TEMP_INV_COMP_NM.add("Negative Imbalance");
				TEMP_INV_COMP_NM.add("Positive Imbalance");
				TEMP_INV_COMP_NM.add("Unauthorized Overrun");
				
				TEMP_INV_COMP_ABBR.add("NI");
				TEMP_INV_COMP_ABBR.add("PI");
				TEMP_INV_COMP_ABBR.add("UR");	
			}
			else if(invoice_type.equals("PC"))
			{
				TEMP_INV_COMP_NM.add("Parking Charge");
				
				TEMP_INV_COMP_ABBR.add("PC");
				
				filterContTyp="'K'";
			}
			
			int cont=0;
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				String queryString="SELECT * "
						+ "FROM ("
							+ "SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY') AS START_DT_STR,TO_CHAR(A.END_DT,'DD/MM/YYYY') AS END_DT_STR,A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
							+ "A.SIP_PAY_FREQ AS SIP_PAY_FREQ_1,C.INVOICE_CUR_CD,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,A.TRANSPORT_RATE,A.RATE_UNIT,"
							+ "A.POSITIVE_IMB_RATE,A.NEGETIVE_IMB_RATE,A.UNAUTH_OVERRUN_RATE,A.SIP_PAY_FREQ AS SIP_PAY_FREQ_2,A.SIP_PAY_RATE,"
							+ "D.PLANT_SEQ_NO AS TRANS_BU,E.PLANT_SEQ_NO AS BU_PLANT_SEQ,TO_CHAR(C.EFF_DT,'DD/MM/YYYY') AS EFF_DT_STR,C.BILLING_DAYS,C.EFF_DT,C.HOLIDAY_STATE,A.PARKING_RATE,A.START_DT AS CONT_START_DT,A.END_DT AS CONT_END_DT "
							+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
							//+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.PLANT_SEQ_NO=D.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
							+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
							+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
							+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
							+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
							+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
							+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
							+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
							+ ""
							+ "AND A.CONTRACT_TYPE IN ("+filterContTyp+") "
							+ "ORDER BY C.EFF_DT "
						+ ") ZZ "
						+ "WHERE CONT_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND CONT_END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				String temp_queryString = queryString;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, temp_period_end_dt);
				stmt.setString(++cont, temp_period_start_dt);
				stmt.setString(++cont, billing_freq);
				stmt.setString(++cont, temp_period_start_dt);
				stmt.setString(++cont, temp_period_end_dt);
				stmt.setString(++cont, temp_period_start_dt);
				stmt.setString(++cont, report_end_dt);
				stmt.setString(++cont, report_start_dt);
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					String invoice_raise_in=rset.getString(13)==null?"":rset.getString(13);
					String due_days=rset.getString(14)==null?"":rset.getString(14);
					String consider_due_dt_in=rset.getString(15)==null?"":rset.getString(15);
					String exclude_sat=rset.getString(16)==null?"":rset.getString(16);
					
					String price = nf.format(rset.getDouble(17));
					String price_cd = rset.getString(18)==null?"1":rset.getString(18);
									
					String positive_imbalance_rate = nf.format(rset.getDouble(19));
					String negative_imbalance_rate = nf.format(rset.getDouble(20));
					String unauthorized_overrun_rate = nf.format(rset.getDouble(21));
					
					String ship_pay_freq=rset.getString(20)==null?"D":rset.getString(22);
					String ship_pay_rate=nf.format(rset.getDouble(23));
					
					String trans_bu_seq = rset.getString(24)==null?"":rset.getString(24);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(25)==null?"":rset.getString(25);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String billing_eff_dt=rset.getString(26)==null?"":rset.getString(26);
					String billing_days=rset.getString(27)==null?"1":rset.getString(27);
					
					String holiday_state=rset.getString(28)==null?"":rset.getString(28);
					String parkingRate=nf.format(rset.getDouble(29));
					
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					String fin_yr = utilDate.getFinancialYear(temp_period_end_dt);
					
					System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+billing_eff_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=billing_eff_dt;
					}
					else
					{
						periodStartDate=temp_period_start_dt;
					}
					int isLower=utilDate.getDays(end_dt, temp_period_end_dt);
					if(isLower < 1)
					{
						periodEndDate=end_dt;
					}
					else
					{
						periodEndDate=temp_period_end_dt;
					}
					System.out.println("PeriodDate="+periodStartDate+"=="+periodEndDate);
					String temp_st_dt=periodStartDate;
					String temp_end_dt=periodEndDate;
					System.out.println("Billing Days : "+billing_days);
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					System.out.println("temp_dt :: "+temp_dt);
					int tot_row=1;
					for(int j=0;j<tot_row;j++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						int checkMthEnd=utilDate.getDays(periodEndDate,temp_st_dt);
						
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
						}
						else
						{
							temp_end_dt = periodEndDate;
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_GTA_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_GTA_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, own_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmtno);
						stmt1.setString(4, contno);
						stmt1.setString(5, cont_type);
						stmt1.setString(6, temp_end_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							innerBillingEffDt=rset1.getString(1)==null?"":rset1.getString(1);
							innerBillingDays=rset1.getString(2)==null?"1":rset1.getString(2);
							innerBillingFreq=rset1.getString(3)==null?"":rset1.getString(3);
						}
						rset1.close();
						stmt1.close();
						
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							System.out.println("New Eff Date : "+innerBillingEffDt);
							break;
						}
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
						
						int countDays=utilDate.getDays(end_dt, temp_end_dt);
						
						for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
						{
							int isInvExist=0;
							String invComponent=""+TEMP_INV_COMP_ABBR.elementAt(i);
							
							queryString4="SELECT COUNT(*) "
									+ "FROM FMS_GTA_SG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, own_cd);
							stmt4.setString(2, countpty_cd);
							stmt4.setString(3, contno);
							stmt4.setString(4, agmtno);
							stmt4.setString(5, trans_bu_seq);
							stmt4.setString(6, bu_plant_seq);
							stmt4.setString(7, cont_type);
							stmt4.setString(8, billing_cycle);
							stmt4.setString(9, invoice_type);
							stmt4.setString(10, temp_st_dt);
							stmt4.setString(11, temp_end_dt);
							stmt4.setString(12, "%"+invComponent+"%");
							rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								isInvExist+=rset4.getInt(1);
							}
							rset4.close();
							stmt4.close();
							
							queryString5="SELECT COUNT(*) "
									+ "FROM FMS_GTA_PG_INV_MST "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
									+ "AND FREQ=? AND INVOICE_TYPE=? "
									+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, own_cd);
							stmt5.setString(2, countpty_cd);
							stmt5.setString(3, contno);
							stmt5.setString(4, agmtno);
							stmt5.setString(5, trans_bu_seq);
							stmt5.setString(6, bu_plant_seq);
							stmt5.setString(7, cont_type);
							stmt5.setString(8, billing_cycle);
							stmt5.setString(9, invoice_type);
							stmt5.setString(10, temp_st_dt);
							stmt5.setString(11, temp_end_dt);
							stmt5.setString(12, "%"+invComponent+"%");
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								isInvExist+=rset5.getInt(1);
							}
							rset5.close();
							stmt5.close();
							
							if(isInvExist==0)
							{
								double qty_mmbtu=0;
								double temp_qty_mmbtu=0;
								
								String cashflow="";
								int days=0;
								
								String accrual_amt="";
								if(invoice_type.equals("TC"))
								{
									cashflow="Transmission Charge";
									if (sip_pay_freq.equals("M")) 
									{	
										String tempStart_dt=""+utilDate.getFirstDateOfMonth(month, year);
										String tempEnd_dt=""+utilDate.getLastDateOfMonth(month, year);
										
										days=utilDate.getDays(end_dt, tempEnd_dt);
										
										if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
										{
											if(days<1)
											{
												tempEnd_dt=end_dt;
											}
											if(utilDate.getDays(tempStart_dt, start_dt)<1)
											{
												tempStart_dt=start_dt;
											}
											
											if(invComponent.contains("TP"))
											{
												qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
												temp_qty_mmbtu += qty_mmbtu;
												
												String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
												accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
											}
											if(invComponent.contains("SP"))
											{
												cashflow="Ship or Pay";
												double temp_def_qty=getAccrualDeficiencyQty(countpty_cd, cont_type, agmtno, contno, tempStart_dt, tempEnd_dt, bu_plant_seq);
												if(temp_def_qty<0)
												{
													temp_def_qty=0;
												}
												deficiency_qty=nf.format(temp_def_qty);
												
												temp_qty_mmbtu+=temp_def_qty;
												
												String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
												accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
											}
										}
										else
										{
											if(invComponent.contains("TP"))
											{
												qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
												temp_qty_mmbtu+=qty_mmbtu;
												
												accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
											}
										}
									}
									else
									{
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
											temp_qty_mmbtu+=qty_mmbtu;
											
											accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
										}
									}
								}
								else if(invoice_type.equals("IC"))
								{
									cashflow="Imbalance Charge";
									getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
									
									//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
									//temp_qty_mmbtu=1;
									
									if(invComponent.contains("NI"))
									{
										temp_qty_mmbtu+=Double.parseDouble(negative_imbalance_qty);
										
										String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
									}
									if(invComponent.contains("PI"))
									{
										temp_qty_mmbtu+=Double.parseDouble(positive_imbalance_qty);
										
										String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
									}
									if(invComponent.contains("UR"))
									{
										temp_qty_mmbtu+=Double.parseDouble(unauthorized_overrun_qty);
										
										String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
										accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
									}
								}
								else if(invoice_type.equals("PC"))
								{
									cashflow="Parking Charge";
									if(invComponent.contains("PC"))
									{
										qty_mmbtu = getAccrualParkingQty(countpty_cd, cont_type, agmtno, contno, temp_st_dt, temp_end_dt, bu_plant_seq);
										temp_qty_mmbtu=qty_mmbtu;
										
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(parkingRate));
									}
								}
								
								if(temp_qty_mmbtu > 0 && !accrual_amt.equals(""))
								{
									if(Double.parseDouble(accrual_amt) > 0)
									{
										VINV_COMPONENTS.add(invComponent);
										VQTY_MMBTU.add(nf3.format(qty_mmbtu));
										if(invoice_type.equals("IC"))
										{
											VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
										}
										else
										{
											VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
										}
										VCOUNTERPTY_CD.add(countpty_cd);
										VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
										VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
										VAGMT_NO.add(agmtno);
										VAGMT_REV_NO.add(agmtrev);
										VCONT_NO.add(contno);
										VCONT_REV_NO.add(contrev);
										VSTART_DT.add(start_dt);
										VEND_DT.add(end_dt);
										VCONT_REF_NO.add(cont_ref_no);
										VCONTRACT_TYPE.add(cont_type);
										VDIS_CONT_MAPPING.add(deal_no);
					
										VTRANS_BU_SEQ.add(trans_bu_seq);
										VTRANS_BU_ABBR.add(trans_bu_abbr);
										VBU_PLANT_SEQ.add(bu_plant_seq);
										VBU_PLANT_ABBR.add(bu_plant_abbr);
										VPERIOD_START_DT.add(temp_st_dt);
										VPERIOD_END_DT.add(temp_end_dt);
										
										VBILLING_FREQ_NM.add(billing_freq_nm);
										VBILLING_FREQ_FLAG.add(billing_cycle);
										
										VFINANCIAL_YEAR.add(fin_yr);
										
										VINVOICE_DUE_DT.add(""+utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
										VPRODUCTION_MONTH.add(period_end_dt.substring(3,temp_end_dt.length()));
										
										VCASH_FLOW_NM.add(cashflow);
										VCASH_FLOW.add(invoice_type);
										
										VINVOICE_RAISED_IN.add(invoice_raise_in);
										VACCRUAL_QTY.add(nf.format(temp_qty_mmbtu));
										
										VACCRUAL_AMT.add(accrual_amt);
										VGROSS_AMT.add(accrual_amt);
										
										tot_accrual_mmbtu+=temp_qty_mmbtu;
										tot_accrual_amt+=Double.parseDouble(accrual_amt);
										
										Vector temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(conn, comp_cd, countpty_cd, "R", trans_bu_seq, bu_plant_seq,temp_st_dt, invoice_type,accrual_amt);
										
										Set<String> ALLOWED = new HashSet<>(Arrays.asList("CGST", "SGST","IGST"));
										String taxStruct=""+temp.elementAt(4);
										double tax_amt=Double.parseDouble(""+temp.elementAt(0));
										List<String> taxes= Arrays.stream(taxStruct.split(","))
												.map(String::trim)          
												.filter(s -> !s.isEmpty())
												.map(s -> s.split("\\s+")[0]) 
												.filter(name -> !ALLOWED.contains(name.toUpperCase()))
												.collect(Collectors.toList());
										if(taxes.isEmpty() && !taxStruct.equals(""))
										{
											VTAX_STRUCT_CD.add(""+temp.elementAt(1));
											VTAX_STRUCT_DTL.add(taxStruct);
											VTAX_AMT.add(nf.format(tax_amt));
											VTAX_INFO.add(""+temp.elementAt(3));
											VMULTI_TAX_STRUCT.add(temp.elementAt(6));
											VTOTAL_ACCRUAL_AMT.add(nf.format(Double.parseDouble(accrual_amt)+tax_amt));
											
											tot_accrual_tax_amt+=tax_amt;
											tot_total_accrual_amt+=(Double.parseDouble(accrual_amt)+tax_amt);
										}
										else
										{
											VTAX_STRUCT_CD.add(""+temp.elementAt(1));
											VTAX_STRUCT_DTL.add(taxStruct);
											VTAX_AMT.add("");
											VTAX_INFO.add("");
											VMULTI_TAX_STRUCT.add("");
											VTOTAL_ACCRUAL_AMT.add(nf.format(Double.parseDouble(accrual_amt)));
											
											tot_total_accrual_amt+=(Double.parseDouble(accrual_amt));
										}
									}
								}
							}
						}
						
						if(rem_checkMthEnd == 0)
						{
							break;
						}
					}
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT * "
						+ "FROM ("
							+ "SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
							+ "TO_CHAR(A.START_DT,'DD/MM/YYYY') AS START_DT_STR,TO_CHAR(A.END_DT,'DD/MM/YYYY') AS END_DT_STR,A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
							+ "A.SIP_PAY_FREQ AS SIP_PAY_FREQ_1,C.INVOICE_CUR_CD,C.DUE_DATE,C.DUE_DT_IN,C.EXCL_SAT_MAP,"
							+ "A.TRANSPORT_RATE,A.RATE_UNIT,"
							+ "A.POSITIVE_IMB_RATE,A.NEGETIVE_IMB_RATE,A.UNAUTH_OVERRUN_RATE,A.SIP_PAY_FREQ AS SIP_PAY_FREQ_2,A.SIP_PAY_RATE,TO_CHAR(C.EFF_DT,'DD/MM/YYYY') AS EFF_DT_STR,"
							+ "C.HOLIDAY_STATE,D.PLANT_SEQ_NO AS TRANS_BU,E.PLANT_SEQ_NO AS BU_PLANT_SEQ,A.PARKING_RATE,A.START_DT AS CONT_START_DT,A.END_DT AS CONT_END_DT "		
							+ "FROM FMS_GTA_CONT_MST A, FMS_GTA_BILLING_DTL C, FMS_GTA_CONT_TRANS_BU D, FMS_GTA_CONT_BU E "
							+ "WHERE A.COMPANY_CD=? "
							+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
							//+ "AND (A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_GTA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
							+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
							+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_GTA_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
							+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
							+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
							+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
							+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
							+ ""
							+ "AND A.CONTRACT_TYPE IN ("+filterContTyp+") "
						+ ") ZZ "
						+ "WHERE CONT_START_DT<=TO_DATE(?,'DD/MM/YYYY') AND CONT_END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				String temp_queryString = queryString;
				stmt=conn.prepareStatement(temp_queryString);
				stmt.setString(++cont, comp_cd);
				stmt.setString(++cont, period_end_dt);
				stmt.setString(++cont, period_start_dt);
				stmt.setString(++cont, billing_freq);
				stmt.setString(++cont, period_end_dt);
				stmt.setString(++cont, report_end_dt);
				stmt.setString(++cont, report_start_dt);
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_name=rset.getString(9)==null?"":rset.getString(9);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String sip_pay_freq=rset.getString(12)==null?"D":rset.getString(12);
					String invoice_raise_in=rset.getString(13)==null?"":rset.getString(13);
					String due_days=rset.getString(14)==null?"":rset.getString(14);
					String consider_due_dt_in=rset.getString(15)==null?"":rset.getString(15);
					String exclude_sat=rset.getString(16)==null?"":rset.getString(16);
					
					String price = nf.format(rset.getDouble(17));
					String price_cd = rset.getString(18)==null?"1":rset.getString(18);
									
					String positive_imbalance_rate = nf.format(rset.getDouble(19));
					String negative_imbalance_rate = nf.format(rset.getDouble(20));
					String unauthorized_overrun_rate = nf.format(rset.getDouble(21));
					
					String ship_pay_freq=rset.getString(20)==null?"D":rset.getString(22);
					String ship_pay_rate=nf.format(rset.getDouble(23));
					
					String billing_eff_dt=rset.getString(24)==null?"":rset.getString(24);
					String holiday_state=rset.getString(25)==null?"":rset.getString(25);
					
					String trans_bu_seq = rset.getString(26)==null?"":rset.getString(26);
					String trans_bu_abbr=utilBean.getCounterpartyBuABBR(conn,countpty_cd, own_cd, trans_bu_seq, "R");
					
					String bu_plant_seq = rset.getString(27)==null?"":rset.getString(27);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String parkingRate=nf.format(rset.getDouble(28));
					
					//String deal_no=cont_type+agmtno+"-"+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					String fin_yr = utilDate.getFinancialYear(period_end_dt);
					
					String temp_period_start_dt=period_start_dt;
					String temp_period_end_dt=period_end_dt;
					
					/*int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					/*if(utilDate.getDays(period_start_dt, start_dt)<1)
					{
						temp_period_start_dt=start_dt;
					}*/
					
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					if(isGreter>1)
					{
						temp_period_start_dt=billing_eff_dt;
						temp_period_end_dt=period_end_dt;
					}
					else
					{
						temp_period_start_dt=period_start_dt;
						temp_period_end_dt=period_end_dt;
					}
					
					int countDays=utilDate.getDays(end_dt, period_end_dt);
					
					if(countDays<1)
					{
						temp_period_end_dt=end_dt;
					}
					
					/*if(utilDate.getDays(end_dt, period_end_dt)<=0)
					{
						temp_period_end_dt=end_dt;
					}*/
					
					for(int i=0; i<TEMP_INV_COMP_ABBR.size(); i++)
					{
						int isInvExist=0;
						String invComponent=""+TEMP_INV_COMP_ABBR.elementAt(i);
						
						queryString4="SELECT COUNT(*) "
								+ "FROM FMS_GTA_SG_INV_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND INVOICE_TYPE=? "
								+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
						stmt4=conn.prepareStatement(queryString4);
						stmt4.setString(1, own_cd);
						stmt4.setString(2, countpty_cd);
						stmt4.setString(3, contno);
						stmt4.setString(4, agmtno);
						stmt4.setString(5, trans_bu_seq);
						stmt4.setString(6, bu_plant_seq);
						stmt4.setString(7, cont_type);
						stmt4.setString(8, billing_cycle);
						stmt4.setString(9, invoice_type);
						//stmt4.setString(10, period_start_dt);
						//stmt4.setString(11, period_end_dt);
						stmt4.setString(10, temp_period_start_dt);
						stmt4.setString(11, temp_period_end_dt);
						stmt4.setString(12, "%"+invComponent+"%");
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							isInvExist+=rset4.getInt(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString5="SELECT COUNT(*) "
								+ "FROM FMS_GTA_PG_INV_MST "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND TRANS_BU_UNIT=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
								+ "AND FREQ=? AND INVOICE_TYPE=? "
								+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND PDF_INV_DTL IS NOT NULL AND INV_COMPONENT LIKE ? ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, own_cd);
						stmt5.setString(2, countpty_cd);
						stmt5.setString(3, contno);
						stmt5.setString(4, agmtno);
						stmt5.setString(5, trans_bu_seq);
						stmt5.setString(6, bu_plant_seq);
						stmt5.setString(7, cont_type);
						stmt5.setString(8, billing_cycle);
						stmt5.setString(9, invoice_type);
						//stmt5.setString(10, period_start_dt);
						//stmt5.setString(11, period_end_dt);
						stmt5.setString(10, temp_period_start_dt);
						stmt5.setString(11, temp_period_end_dt);
						stmt5.setString(12, "%"+invComponent+"%");
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							isInvExist+=rset5.getInt(1);
						}
						rset5.close();
						stmt5.close();
					
						if(isInvExist==0)
						{
							double qty_mmbtu=0;
							double temp_qty_mmbtu=0;
							
							String cashflow="";
							int days=0;
							
							String accrual_amt="";
							if(invoice_type.equals("TC"))
							{
								cashflow="Transmission Charge";
								if (sip_pay_freq.equals("M")) 
								{	
									String temp_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
									String temp_end_dt=""+utilDate.getLastDateOfMonth(month, year);
									
									days=utilDate.getDays(end_dt, temp_end_dt);
									
									if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
									{
										if(days<1)
										{
											temp_end_dt=end_dt;
										}
										if(utilDate.getDays(temp_start_dt, start_dt)<1)
										{
											temp_start_dt=start_dt;
										}
										
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
											temp_qty_mmbtu += qty_mmbtu;
											
											String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
										}
										if(invComponent.contains("SP"))
										{
											cashflow="Ship or Pay";
											double temp_def_qty=getAccrualDeficiencyQty(countpty_cd, cont_type, agmtno, contno, temp_start_dt, temp_end_dt, bu_plant_seq);
											if(temp_def_qty<0)
											{
												temp_def_qty=0;
											}
											deficiency_qty=nf.format(temp_def_qty);
											
											temp_qty_mmbtu+=temp_def_qty;
											
											String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
										}
									}
									else
									{
										if(invComponent.contains("TP"))
										{
											qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
											temp_qty_mmbtu+=qty_mmbtu;
											
											accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
										}
									}
								}
								else
								{
									if(invComponent.contains("TP"))
									{
										qty_mmbtu=getAccrualTransmissionQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
										temp_qty_mmbtu+=qty_mmbtu;
										
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
									}
								}
							}
							else if(invoice_type.equals("IC"))
							{
								cashflow="Imbalance Charge";
								getImbalanceQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
								
								//temp_qty_mmbtu=Double.parseDouble(positive_imbalance_qty) + Double.parseDouble(negative_imbalance_qty) + Double.parseDouble(unauthorized_overrun_qty);
								//temp_qty_mmbtu=1;
								
								if(invComponent.contains("NI"))
								{
									temp_qty_mmbtu+=Double.parseDouble(negative_imbalance_qty);
									
									String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
								}
								if(invComponent.contains("PI"))
								{
									temp_qty_mmbtu+=Double.parseDouble(positive_imbalance_qty);
									
									String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
								}
								if(invComponent.contains("UR"))
								{
									temp_qty_mmbtu+=Double.parseDouble(unauthorized_overrun_qty);
									
									String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
								}
							}
							else if(invoice_type.equals("PC"))
							{
								cashflow="Parking Charge";
								if(invComponent.contains("PC"))
								{
									qty_mmbtu = getAccrualParkingQty(countpty_cd, cont_type, agmtno, contno, temp_period_start_dt, temp_period_end_dt, bu_plant_seq);
									temp_qty_mmbtu=qty_mmbtu;
									
									accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(parkingRate));
								}
							}
							
							/*
							//String accrual_amt="";
							if(invoice_type.equals("IC"))
							{
								String positive_imbalance_amount=nf.format(Double.parseDouble(positive_imbalance_qty) * Double.parseDouble(positive_imbalance_rate));
								String negative_imbalance_amount=nf.format(Double.parseDouble(negative_imbalance_qty) * Double.parseDouble(negative_imbalance_rate));
								String unauthorized_imbalance_amount=nf.format(Double.parseDouble(unauthorized_overrun_qty) * Double.parseDouble(unauthorized_overrun_rate));
								
								//accrual_amt=nf.format(Double.parseDouble(positive_imbalance_amount) + Double.parseDouble(negative_imbalance_amount) + Double.parseDouble(unauthorized_imbalance_amount));
								
								if(invComponent.contains("NI"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(negative_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(negative_imbalance_amount));
								}
								if(invComponent.contains("PI"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(positive_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(positive_imbalance_amount));
								}
								if(invComponent.contains("UR"))
								{
									accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(unauthorized_imbalance_amount)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(unauthorized_imbalance_amount));
								}
							}
							else
							{
								if (sip_pay_freq.equals("M")) 
								{
									if(billing_cycle.equals("2") || countDays<=1) //ADDED = TO SIGN
									{
										String transmission_amt=nf.format(qty_mmbtu * Double.parseDouble(price));
										String deficiency_amt=nf.format(Double.parseDouble(deficiency_qty) * Double.parseDouble(ship_pay_rate));
										
										//accrual_amt=nf.format(Double.parseDouble(transmission_amt) + Double.parseDouble(deficiency_amt));
										
										if(invComponent.contains("TP"))
										{
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(transmission_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(transmission_amt));
										}
										if(invComponent.contains("SP"))
										{
											accrual_amt=accrual_amt.equals("")?nf.format(Double.parseDouble(deficiency_amt)):nf.format(Double.parseDouble(accrual_amt) + Double.parseDouble(deficiency_amt));
										}
									}
									else
									{
										accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));
									}
								}
								else
								{
									accrual_amt = nf.format(qty_mmbtu * Double.parseDouble(price));	
								}
							}*/
							
							if(temp_qty_mmbtu > 0 && !accrual_amt.equals(""))
							{
								if(Double.parseDouble(accrual_amt) > 0)
								{
									VINV_COMPONENTS.add(invComponent);
									VQTY_MMBTU.add(nf3.format(qty_mmbtu));
									if(invoice_type.equals("IC"))
									{
										VTEMP_QTY_MMBTU.add(nf3.format(temp_qty_mmbtu));
									}
									else
									{
										VTEMP_QTY_MMBTU.add(nf3.format(qty_mmbtu));
									}
									VCOUNTERPTY_CD.add(countpty_cd);
									VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
									VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
									VAGMT_NO.add(agmtno);
									VAGMT_REV_NO.add(agmtrev);
									VCONT_NO.add(contno);
									VCONT_REV_NO.add(contrev);
									VSTART_DT.add(start_dt);
									VEND_DT.add(end_dt);
									VCONT_REF_NO.add(cont_ref_no);
									VCONTRACT_TYPE.add(cont_type);
									VDIS_CONT_MAPPING.add(deal_no);
				
									VTRANS_BU_SEQ.add(trans_bu_seq);
									VTRANS_BU_ABBR.add(trans_bu_abbr);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									VPERIOD_START_DT.add(temp_period_start_dt);
									VPERIOD_END_DT.add(temp_period_end_dt);
									
									VBILLING_FREQ_NM.add(billing_freq_nm);
									VBILLING_FREQ_FLAG.add(billing_cycle);
									
									VFINANCIAL_YEAR.add(fin_yr);
									
									VINVOICE_DUE_DT.add(""+utilBean.DueDateCalculation(conn,temp_period_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
									VPRODUCTION_MONTH.add(period_end_dt.substring(3,temp_period_end_dt.length()));
									
									VCASH_FLOW_NM.add(cashflow);
									VCASH_FLOW.add(invoice_type);
									
									VINVOICE_RAISED_IN.add(invoice_raise_in);
									VACCRUAL_QTY.add(nf.format(temp_qty_mmbtu));
									
									VACCRUAL_AMT.add(accrual_amt);
									VGROSS_AMT.add(accrual_amt);
									
									tot_accrual_mmbtu+=temp_qty_mmbtu;
									tot_accrual_amt+=Double.parseDouble(accrual_amt);
									
									Vector temp=TaxCalc.BuServiceTaxAmountCalculationWithInfo(conn, comp_cd, countpty_cd, "R", trans_bu_seq, bu_plant_seq,temp_period_start_dt, invoice_type,accrual_amt);
									
									Set<String> ALLOWED = new HashSet<>(Arrays.asList("CGST", "SGST","IGST"));
									String taxStruct=""+temp.elementAt(4);
									double tax_amt=Double.parseDouble(""+temp.elementAt(0));
									
									List<String> taxes= Arrays.stream(taxStruct.split(","))
											.map(String::trim)          
											.filter(s -> !s.isEmpty())
											.map(s -> s.split("\\s+")[0]) 
											.filter(name -> !ALLOWED.contains(name.toUpperCase()))
											.collect(Collectors.toList());
									if(taxes.isEmpty() && !taxStruct.equals(""))
									{
										VTAX_STRUCT_CD.add(""+temp.elementAt(1));
										VTAX_STRUCT_DTL.add(taxStruct);
										VTAX_AMT.add(nf.format(tax_amt));
										VTAX_INFO.add(""+temp.elementAt(3));
										VMULTI_TAX_STRUCT.add(temp.elementAt(6));
										VTOTAL_ACCRUAL_AMT.add(nf.format(Double.parseDouble(accrual_amt)+tax_amt));
										
										tot_accrual_tax_amt+=tax_amt;
										tot_total_accrual_amt+=(Double.parseDouble(accrual_amt)+tax_amt);
									}
									else
									{
										VTAX_STRUCT_CD.add(""+temp.elementAt(1));
										VTAX_STRUCT_DTL.add(taxStruct);
										VTAX_AMT.add("");
										VTAX_INFO.add("");
										VMULTI_TAX_STRUCT.add("");
										VTOTAL_ACCRUAL_AMT.add(nf.format(Double.parseDouble(accrual_amt)));
										
										tot_total_accrual_amt+=(Double.parseDouble(accrual_amt));
									}
								}
							}
						}
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
	
	public void FreezAccrualData()
	{
		String function_nm="FreezAccrualData()";
		try
		{
			int count=0;
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+= "AND COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(3, counterparty_cd);
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(count>0)
			{
				queryString1="DELETE FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					queryString1+= "AND COUNTERPARTY_CD=? ";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					stmt1.setString(3, counterparty_cd);
				}
				stmt1.executeUpdate();
				
				stmt1.close();
				
				queryString1="DELETE FROM FMS_GTA_ACCRUAL_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					queryString1+= "AND COUNTERPARTY_CD=? ";
				}
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					stmt1.setString(3, counterparty_cd);
				}
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				queryString1="INSERT INTO FMS_GTA_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BU_UNIT,TRANS_BU_UNIT,CASH_FLOW,"
						+ "INVOICE_DUE_DT,PROD_MONTH,FREQ,"
						+ "PERIOD_START_DT,PERIOD_END_DT,"
						+ "ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,CONT_REF_NO,CONT_START_DT,CONT_END_DT,INV_COMPONENT,TAX_STRUCT_CD,TAX_AMT,INVOICE_AMT) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
				stmt1.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
				stmt1.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt1.setString(6, ""+VAGMT_REV_NO.elementAt(i));
				stmt1.setString(7, ""+VCONT_NO.elementAt(i));
				stmt1.setString(8, ""+VCONT_REV_NO.elementAt(i));
				stmt1.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
				stmt1.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
				stmt1.setString(11, ""+VTRANS_BU_SEQ.elementAt(i));
				stmt1.setString(12, ""+VCASH_FLOW.elementAt(i));
				stmt1.setString(13, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt1.setString(14, prod_month);
				stmt1.setString(15, ""+VBILLING_FREQ_FLAG.elementAt(i));
				stmt1.setString(16, ""+VPERIOD_START_DT.elementAt(i));
				stmt1.setString(17, ""+VPERIOD_END_DT.elementAt(i));
				stmt1.setString(18, ""+VACCRUAL_QTY.elementAt(i));
				stmt1.setString(19, ""+VACCRUAL_AMT.elementAt(i));
				stmt1.setString(20, "");
				stmt1.setString(21, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt1.setString(22, ""+VGROSS_AMT.elementAt(i));
				stmt1.setString(23, ""+VCONT_REF_NO.elementAt(i));
				stmt1.setString(24, ""+VSTART_DT.elementAt(i));
				stmt1.setString(25, ""+VEND_DT.elementAt(i));
				stmt1.setString(26, ""+VINV_COMPONENTS.elementAt(i));
				stmt1.setString(27, ""+VTAX_STRUCT_CD.elementAt(i));
				stmt1.setString(28, ""+VTAX_AMT.elementAt(i));
				stmt1.setString(29, ""+VTOTAL_ACCRUAL_AMT.elementAt(i));
				stmt1.executeUpdate();
				
				stmt1.close();
				
				if(!VMULTI_TAX_STRUCT.elementAt(i).equals(""))
				{
					Vector temp =(Vector)((Vector)((Vector)VMULTI_TAX_STRUCT.elementAt(i)));
					for(int j=0;j<((Vector) temp.elementAt(0)).size(); j++)
					{
						String sub_tax_code=""+((Vector) temp.elementAt(0)).elementAt(j);
						String sub_tax_struct=""+((Vector) temp.elementAt(1)).elementAt(j);
						String sub_tax_amt=""+((Vector) temp.elementAt(2)).elementAt(j);
						String sub_tax_base_amt=""+((Vector) temp.elementAt(3)).elementAt(j);
						
						String queryString2="INSERT INTO FMS_GTA_ACCRUAL_TAX_DTL(COMPANY_CD, REPORT_DT, COUNTERPARTY_CD, FINANCIAL_YEAR, "
								+ "AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, BU_UNIT, TRANS_BU_UNIT, "
								+ "PERIOD_START_DT, PERIOD_END_DT, CASH_FLOW, INV_COMPONENT, "
								+ "TAX_STRUCT_CD, TAX_CODE, TAX_DESCR, TAX_AMT, TAX_BASE_AMT) "//, ENT_BY, ENT_DT) "
								+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
								+ "?,?,?,?,?)";//,?,SYSDATE)";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, report_dt);
						stmt2.setString(3, ""+VCOUNTERPTY_CD.elementAt(i));
						stmt2.setString(4, ""+VFINANCIAL_YEAR.elementAt(i));
						stmt2.setString(5, ""+VAGMT_NO.elementAt(i));
						stmt2.setString(6, ""+VAGMT_REV_NO.elementAt(i));
						stmt2.setString(7, ""+VCONT_NO.elementAt(i));
						stmt2.setString(8, ""+VCONT_REV_NO.elementAt(i));
						stmt2.setString(9, ""+VCONTRACT_TYPE.elementAt(i));
						stmt2.setString(10, ""+VBU_PLANT_SEQ.elementAt(i));
						stmt2.setString(11, ""+VTRANS_BU_SEQ.elementAt(i));
						stmt2.setString(12, ""+VPERIOD_START_DT.elementAt(i));
						stmt2.setString(13, ""+VPERIOD_END_DT.elementAt(i));
						stmt2.setString(14, ""+VCASH_FLOW.elementAt(i));
						stmt2.setString(15, ""+VINV_COMPONENTS.elementAt(i));
						stmt2.setString(16, ""+VTAX_STRUCT_CD.elementAt(i));
						stmt2.setString(17, sub_tax_code);
						stmt2.setString(18, sub_tax_struct);
						stmt2.setString(19, sub_tax_amt);
						stmt2.setString(20, sub_tax_base_amt);
						//stmt2.setString(22, ""+VEXCHNG_RATE_CD.elementAt(i));
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
			}
			conn.commit();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateGTAAccrualXML()
	{
		String function_nm="generateGTAAccrualXML()";
		try
		{
			String sysdate=utilDate.getSysdate();
			String sysdateWithTime=utilDate.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];
			
			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];
			
			String counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			
			String fms_MessageId = "";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			
			String documentDate="";
			if(!report_dt.equals(""))
			{
				String[] temp_split=report_dt.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	
				
				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}
			
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,TRANS_BU_UNIT,BU_UNIT,"
					+ "FINANCIAL_YEAR,CASH_FLOW,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,INV_COMPONENT "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmtno=rset.getString(2)==null?"":rset.getString(2);
				String contno=rset.getString(3)==null?"":rset.getString(3);
				String cont_type=rset.getString(4)==null?"":rset.getString(4);
				String trans_bu_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String cashFlow=rset.getString(8)==null?"":rset.getString(8);
				String prod_month=rset.getString(9)==null?"":rset.getString(9);
				String bill_freq=rset.getString(10)==null?"":rset.getString(10);
				String invComponent=rset.getString(11)==null?"":rset.getString(11);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "B");
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, buSeq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				docHeaderText=buStateNm+"/"+buAbbr+" - BU";
				
				String xml_seq="1";
				String xml_num="";
				queryString2="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, accountingPeriodMonth+"/"+accountingPeriodYear);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					xml_seq=""+(rset2.getInt(1)+1);
				}
				rset2.close();
				stmt2.close();
				xml_num=invoice_prefix+"R"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
		    	
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    //root fmsng
			    //Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage"); //AS PER VIJAY MAIL ON 20260221
			    Element fmsng = doc.createElement("EmsSAPApMessage");
			    doc.appendChild(fmsng);
		
			    //root elements
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    //Element Subledger = doc.createElement("Subledger"); //AS PER VIJAY MAIL ON 20260221
			    Element Subledger = doc.createElement("Invoice");
			    fmsng.appendChild(Subledger);
			    
			    //Header elements
			    Element MessageId = doc.createElement("MessageId");
			    Element Scope = doc.createElement("Scope");
			    Element DateTimeStamp = doc.createElement("DateTimeStamp");
			    Element DataSource = doc.createElement("DataSource");
			    
			    Header.appendChild(MessageId);
			    Header.appendChild(Scope);
			    Header.appendChild(DateTimeStamp);
			    Header.appendChild(DataSource);
			    
			    MessageId.appendChild(doc.createTextNode(fms_MessageId));
			    Scope.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    //Subledger elements
			    //Element SubledgerHeader = doc.createElement("SubledgerHeader"); //AS PER VIJAY MAIL ON 20260221
			    Element SubledgerHeader = doc.createElement("InvoiceHeader");
			    Subledger.appendChild(SubledgerHeader);
			    
			    Element BusinessActivity = doc.createElement("BusinessActivity");
			    Element DocumentType = doc.createElement("DocumentType"); 
			    Element DocumentDate = doc.createElement("DocumentDate"); 
			    Element PostingDate = doc.createElement("PostingDate"); 
			    Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			    Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			    Element InternalLegalEntity = doc.createElement("InternalLegalEntity"); 
			    Element DocHeaderText = doc.createElement("DocHeaderText"); 
			    Element RefNum = doc.createElement("RefNum"); 
			    Element EmsRefNum = doc.createElement("EmsRefNum"); 
			    Element Currency = doc.createElement("Currency"); 
			    Element LocalCurrency = doc.createElement("LocalCurrency"); 
			    Element TranslationDate = doc.createElement("TranslationDate");

			    
			    //SubledgerHeader element
			    SubledgerHeader.appendChild(BusinessActivity);
			    SubledgerHeader.appendChild(DocumentType);
			    SubledgerHeader.appendChild(DocumentDate);
			    SubledgerHeader.appendChild(PostingDate);
			    SubledgerHeader.appendChild(AccountingPeriodMonth);
			    SubledgerHeader.appendChild(AccountingPeriodYear);
			    SubledgerHeader.appendChild(InternalLegalEntity);
			    SubledgerHeader.appendChild(DocHeaderText);
			    SubledgerHeader.appendChild(RefNum);
			    SubledgerHeader.appendChild(EmsRefNum);
			    SubledgerHeader.appendChild(Currency);
			    SubledgerHeader.appendChild(LocalCurrency);
			    SubledgerHeader.appendChild(TranslationDate);
			    
			    BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    DocumentType.appendChild(doc.createTextNode("X3"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
			    Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
				
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
		    	
		    	String tempAccount="";
		    	String tempAccount2="";//For even line seq
		    	String pk2="40";
		    	//String pk="50"; //For even line seq
		    	String pk="31"; //For even line seq
		    	String sign = "-";
		    	String sign2 = "";
		    	
		    	tempAccount=account; // 05-09-2023: Counterparty SAP CODE Will hit for both NG and IG 
		    	if(cashFlow.equals("IC"))
		    	{
			    	//INCIDENT#2310112 DIVAY HAS ASKED TO PASS GL CODE 6318400	Pipeline Imbalance  
			    	tempAccount2="6318400";
		    	}
		    	else if(cashFlow.equals("PC"))
		    	{
		    		tempAccount2="6320405"; //AS PER VIJAY'S MSG ON TEAMS 20250730
		    	}
		    	else
		    	{
		    		if(countpty_category.equals("Group"))
			    	{
			    		//tempAccount=account; // Counterparty SAP CODE
			    		tempAccount2="6301400";
			    	}
			    	else
			    	{
			    		//tempAccount="3180720"; // PURCHASE ETRM ACCRUALS
			    		//pk="50";
			    		tempAccount2="6300400";
			    	}
		    	}
		    	
		    	
		    	String transBuNm=utilBean.getCounterpartyBuABBR(conn,counterpty_cd, comp_cd, trans_bu_seq, "R");
		    	
		    	//String assignmentNo="R"+counterpty_cd+cont_type+agmtno+"-"+contno;
		    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, "");
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "R", cont_type, report_dt);
		    			
		    	int i=0;
				queryString4="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,CASH_FLOW,INVOICE_AMT,"
						+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TAX_STRUCT_CD "
						+ "FROM FMS_GTA_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND FREQ=? AND INV_COMPONENT=? ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, report_dt);
				stmt4.setString(3, prod_month);
				stmt4.setString(4, counterpty_cd);
				stmt4.setString(5, agmtno);
				stmt4.setString(6, contno);
				stmt4.setString(7, cont_type);
				stmt4.setString(8, trans_bu_seq);
				stmt4.setString(9, buSeq);
				stmt4.setString(10, financialYear);
				stmt4.setString(11, cashFlow);
				stmt4.setString(12, bill_freq);
				stmt4.setString(13, invComponent);
				rset4=stmt4.executeQuery();
				while(rset4.next())
				{
					String invoice_dueDt=rset4.getString(1)==null?"":rset4.getString(1);
					String accrual_qty=nf.format(rset4.getDouble(2));
					//3
					String grossAmt=nf.format(rset4.getDouble(4));
					//String cashFlow=rset4.getString(5)==null?"":rset4.getString(5);
					String invoiceAmt=nf.format(rset4.getDouble(6));
					
					String periodSt_dt=rset4.getString(7)==null?"":rset4.getString(7);
					String periodEnd_dt=rset4.getString(8)==null?"":rset4.getString(8);
					String taxStructCd=rset4.getString(9)==null?"":rset4.getString(9);
					
					String monthNm = utilDate.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	String itemText2="Transport Services "+monthNm+" "+yearNm;
			    	if(cashFlow.equals("PC"))
			    	{
			    		itemText2="Parking Services "+monthNm+" "+yearNm;
			    	}
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					String taxCode="";
					int isTaxApp=0;
					queryString5="SELECT COUNT(*) "
							+ "FROM FMS_GTA_ACCRUAL_TAX_DTL "
							+ "WHERE COMPANY_CD=? "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND INV_COMPONENT=? ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, report_dt);
					stmt5.setString(3, periodSt_dt);
					stmt5.setString(4, periodEnd_dt);
					stmt5.setString(5, counterpty_cd);
					stmt5.setString(6, agmtno);
					stmt5.setString(7, contno);
					stmt5.setString(8, cont_type);
					stmt5.setString(9, trans_bu_seq);
					stmt5.setString(10, buSeq);
					stmt5.setString(11, financialYear);
					stmt5.setString(12, cashFlow);
					stmt5.setString(13, invComponent);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						isTaxApp=rset5.getInt(1);
					}
					rset5.close();
					stmt5.close();
					
					if(isTaxApp > 0)
					{
						taxCode=utilBean.getTaxSAPcode(conn,taxStructCd);
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	//Element SubledgerEntry = doc.createElement("SubledgerEntry"); //AS PER VIJAY MAIL ON 20260221
				    	Element SubledgerEntry = doc.createElement("InvoiceDetail");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId  = doc.createElement("VendorId");//
				    	Element LineSeqNo = doc.createElement("LineSeqNo");//
					    Element PostingKey = doc.createElement("PostingKey");//
					    Element Account = doc.createElement("Account");//
					    Element CurrencyAmount = doc.createElement("CurrencyAmount"); //
					    Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");//
					    Element Material = doc.createElement("Material");//
					    Element BusinessArea = doc.createElement("BusinessArea");//
					    Element ItemText = doc.createElement("ItemText");//
					    Element Volume = doc.createElement("Volume");//
					    Element VolumeUnit = doc.createElement("VolumeUnit");//
					    Element ReferenceKey1 = doc.createElement("ReferenceKey1");//
					    Element ReferenceKey2 = doc.createElement("ReferenceKey2");//
					    Element ProductionPeriod = doc.createElement("ProductionPeriod");//
					    Element AssignmentNumber = doc.createElement("AssignmentNumber");//
					    Element PaymentTerms = doc.createElement("PaymentTerms");//
					    Element PaymentBlock = doc.createElement("PaymentBlock");//
					    Element PaymentDueDate = doc.createElement("PaymentDueDate");//
					    Element Plant = doc.createElement("Plant");//
					    Element TaxCode = doc.createElement("TaxCode");
					    
				    	// SubledgerEntry elements
					    SubledgerEntry.appendChild(VendorId);//
					    SubledgerEntry.appendChild(LineSeqNo);//
					    SubledgerEntry.appendChild(PostingKey);//
					    SubledgerEntry.appendChild(Account);//
					    SubledgerEntry.appendChild(CurrencyAmount);//
					    SubledgerEntry.appendChild(LocalCurrencyAmount);//--
					    SubledgerEntry.appendChild(Material);//
					    SubledgerEntry.appendChild(BusinessArea);//
					    SubledgerEntry.appendChild(ItemText);//
					    SubledgerEntry.appendChild(Volume);//
					    SubledgerEntry.appendChild(VolumeUnit);//
					    SubledgerEntry.appendChild(ReferenceKey1);//
					    SubledgerEntry.appendChild(ReferenceKey2);
					    SubledgerEntry.appendChild(ProductionPeriod);//
					    SubledgerEntry.appendChild(AssignmentNumber);//
					    SubledgerEntry.appendChild(PaymentTerms);//
					    SubledgerEntry.appendChild(PaymentBlock);//
					    SubledgerEntry.appendChild(PaymentDueDate);//
					    SubledgerEntry.appendChild(Plant);	
					    SubledgerEntry.appendChild(TaxCode);
					   
					    if (i%2 == 0) 
					    {
					    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	PostingKey.appendChild(doc.createTextNode(pk2));
					    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText2));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
					    }
					    else
					    {
					    	VendorId.appendChild(doc.createTextNode(tempAccount));
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	//Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10)));
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	//CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+invoiceAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("3344036", 18))); //NATURAL GAS MATERIAL CODE
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(transBuNm+" - Plant"));
				    	TaxCode.appendChild(doc.createTextNode(taxCode));
					}
					
					queryString5="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_GTA_ACCRUAL_TAX_DTL "
							+ "WHERE COMPANY_CD=? "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND INV_COMPONENT=? ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, report_dt);
					stmt5.setString(3, periodSt_dt);
					stmt5.setString(4, periodEnd_dt);
					stmt5.setString(5, counterpty_cd);
					stmt5.setString(6, agmtno);
					stmt5.setString(7, contno);
					stmt5.setString(8, cont_type);
					stmt5.setString(9, trans_bu_seq);
					stmt5.setString(10, buSeq);
					stmt5.setString(11, financialYear);
					stmt5.setString(12, cashFlow);
					stmt5.setString(13, invComponent);
					rset5=stmt5.executeQuery();
					while(rset5.next())
					{
						String tax_code=rset5.getString(1)==null?"":rset5.getString(1);
						String taxStrctCd=rset5.getString(2)==null?"":rset5.getString(2);
						String taxAmt=rset5.getString(3)==null?"":nf.format(rset5.getDouble(3));
						String taxBaseAmt=rset5.getString(4)==null?"":nf.format(rset5.getDouble(4));
						if(taxBaseAmt.equals(""))
						{
							taxBaseAmt=grossAmt;
						}
						
						if(!taxAmt.equals(""))
					    {
							//if(Double.parseDouble(tax_amt)>0) AS DISCUSSED WITH VIJAY, SEND ZERO TAX 
					    	{
					    		pk="40";
						    	sign="";
					    		i+=1;
						    	
						    	String gl_code="";
						    	String tax_sap_code="";
						    	String amt=taxAmt;
						    	gl_code=utilBean.getTaxGLcode(conn,taxStrctCd, tax_code);
						    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStrctCd, tax_code);
						    	
						    	if(Double.parseDouble(amt) > 0)
						    	{
							    	//Element SubledgerEntry = doc.createElement("SubledgerEntry"); //AS PER VIJAY MAIL ON 20260221
							    	Element SubledgerEntry = doc.createElement("InvoiceDetail");
							    	Subledger.appendChild(SubledgerEntry);
							    	
							    	Element LineSeqNo = doc.createElement("LineSeqNo");
								    Element PostingKey = doc.createElement("PostingKey");
								    Element Account = doc.createElement("Account");
								    Element LineInd = doc.createElement("LineInd");
								    Element TaxAmount = doc.createElement("TaxAmount");
								    Element TaxAmountLocal = doc.createElement("TaxAmountLocal");
								    Element TaxCode = doc.createElement("TaxCode");
								    Element BusinessArea = doc.createElement("BusinessArea");
								    Element TaxType = doc.createElement("TaxType");
								    Element TaxBase = doc.createElement("TaxBase");
								    
								    // SubledgerEntry elements
								    SubledgerEntry.appendChild(LineSeqNo);
								    SubledgerEntry.appendChild(PostingKey);
								    SubledgerEntry.appendChild(Account);
								    SubledgerEntry.appendChild(LineInd);
								    SubledgerEntry.appendChild(TaxAmount);
								    SubledgerEntry.appendChild(TaxAmountLocal);
								    SubledgerEntry.appendChild(TaxCode);
								    SubledgerEntry.appendChild(BusinessArea);
								    SubledgerEntry.appendChild(TaxType);
								    SubledgerEntry.appendChild(TaxBase); //AS PER SUNIDHI MAIL 16/08/2023
								    
								    LineSeqNo.appendChild(doc.createTextNode(""+i));
							    	PostingKey.appendChild(doc.createTextNode(pk));
							    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
							    	LineInd.appendChild(doc.createTextNode("T")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
							    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
							    	TaxCode.appendChild(doc.createTextNode(tax_sap_code));
							    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
							    	TaxType.appendChild(doc.createTextNode("V")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
							    	TaxBase.appendChild(doc.createTextNode(taxBaseAmt)); //AS PER SUNIDHI MAIL 16/08/2023
						    	}
					    	}
					    }
					}
					rset5.close();
					stmt5.close();
				}
				rset4.close();
				stmt4.close();
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
				transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");

				Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(doc);
			    
			    String xmlFileNm="";
			    String datetime="";
			    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
			    
			    if(!fms_MessageId.equals(""))
			    {
			    	//xmlFileNm="APGL_"+fms_MessageId+"_"+datetime+".xml"; //AS PER VIJAY MAIL ON 20260221
			    	xmlFileNm="AP_"+fms_MessageId+"_"+datetime+".xml";
			    }
				
			    if(!xmlFileNm.equals(""))
			    {
			    	String appPath = request.getServletContext().getRealPath("");
		        	
		        	String main_folder="";
					if(!comp_cd.equals(""))
					{
						main_folder=CommonVariable.work_dir+comp_cd;
					}
					File MainDir = new File(appPath+File.separator+main_folder);
			        if(!MainDir.exists()) 
			        {
			        	MainDir.mkdir();
			        }
			        String sub_folder=""+CommonVariable.sap_xml;
			        File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
			        if(!SubDir.exists()) 
			        {
			        	SubDir.mkdir();
			        }
			        
				    StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
				    transformer.transform(source, result);
				    
				    xmlfile_name=xmlFileNm;
				    
				    if(!xml_seq.equals("") && !xml_num.equals(""))
					{
						queryString5="UPDATE FMS_GTA_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND TRANS_BU_UNIT=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND CASH_FLOW=? AND FREQ=? AND INV_COMPONENT=? ";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, xml_seq);
						stmt5.setString(2, xml_num);
						stmt5.setString(3, comp_cd);
						stmt5.setString(4, report_dt);
						stmt5.setString(5, prod_month);
						stmt5.setString(6, counterpty_cd);
						stmt5.setString(7, agmtno);
						stmt5.setString(8, contno);
						stmt5.setString(9, cont_type);
						stmt5.setString(10, trans_bu_seq);
						stmt5.setString(11, buSeq);
						stmt5.setString(12, financialYear);
						stmt5.setString(13, cashFlow);
						stmt5.setString(14,bill_freq);
						stmt5.setString(15,invComponent);
						stmt5.executeUpdate();
						conn.commit();
						
						stmt5.close();
					}
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
	
	public void getAccrualFreezedData()
	{
		String function_nm="getAccrualFreezedData()";
		try
		{
			String queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "BU_UNIT,BU_STATE_TIN,TRANS_BU_UNIT,TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,CONT_REF_NO,CASH_FLOW,"
					+ "TO_CHAR(CONT_START_DT,'DD/MM/YYYY'),TO_CHAR(CONT_END_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TAX_STRUCT_CD,TAX_AMT,INVOICE_AMT "
					+ "FROM FMS_GTA_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals(""))
			{
				queryString+="AND COUNTERPARTY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			if(!counterparty_cd.equals(""))
			{
				stmt.setString(3, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String financial_year=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type=rset.getString(7)==null?"":rset.getString(7);
				String bu_plant_seq=rset.getString(8)==null?"":rset.getString(8);
				String state_code=rset.getString(9)==null?"":rset.getString(9);
				String plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String prod_month_dt=rset.getString(12)==null?"":rset.getString(12);
				String prod_month="";
				if(!prod_month_dt.equals(""))
				{
					String[] temp=prod_month_dt.split("/");
					prod_month=temp[1]+"/"+temp[2];
				}
				String freq = rset.getString(13)==null?"":rset.getString(13);
				
				//String deal_no=cont_type+agmtno+"-"+contno;
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
				
				//VBU_STATE_TIN.add(state_code); not in used							
				VFINANCIAL_YEAR.add(financial_year);
				VCOUNTERPTY_CD.add(countpty_cd);
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(contrev);
				VCONTRACT_TYPE.add(cont_type);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VSTART_DT.add(rset.getString(26)==null?"":rset.getString(26));
				VEND_DT.add(rset.getString(27)==null?"":rset.getString(27));
				VDIS_CONT_MAPPING.add(deal_no);
				VCONT_REF_NO.add(rset.getString(24)==null?"":rset.getString(24));
				VTRANS_BU_SEQ.add(plant_seq);
				VTRANS_BU_ABBR.add(""+utilBean.getCounterpartyBuPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "R"));
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
				VPERIOD_START_DT.add(rset.getString(14)==null?"":rset.getString(14));
				VPERIOD_END_DT.add(rset.getString(15)==null?"":rset.getString(15));
				VBILLING_FREQ_FLAG.add(freq);
				VBILLING_FREQ_NM.add(utilBean.getBillingFreqNm(freq));
				VPRODUCTION_MONTH.add(prod_month);
				VINVOICE_DUE_DT.add(rset.getString(11)==null?"":rset.getString(11));
				
				String cash_flow=rset.getString(25)==null?"":rset.getString(25);
				String cashflow="";
				if(cash_flow.equals("IC"))
				{
					cashflow="Imbalance Charge";
				}
				else if(cash_flow.equals("TC"))
				{
					cashflow="Transmission Charge";
				}
				else if(cash_flow.equals("PC"))
				{
					cashflow="Parking Charge";
				}
				VCASH_FLOW_NM.add(cashflow);
				VCASH_FLOW.add(rset.getString(25)==null?"":rset.getString(25));
				
				VACCRUAL_QTY.add(rset.getString(16)==null?"":nf.format(rset.getDouble(16)));
				VACCRUAL_AMT.add(rset.getString(17)==null?"":nf.format(rset.getDouble(17)));
				
				String price_unit=rset.getString(18)==null?"":rset.getString(18);

				String exchng_rate_cd=rset.getString(19)==null?"":rset.getString(19);
				String exchng_rate_dt=rset.getString(20)==null?"":rset.getString(20);
				String exchng_rate=rset.getString(21)==null?"":nf.format(rset.getDouble(21));
				String invoice_raise_in=rset.getString(22)==null?"":rset.getString(22);
				
				//VSALES_PRICE_CD.add(price_unit);
				//VSALES_PRICE_NM.add(utilBean.getRateUnitNm(price_unit));
				VINVOICE_RAISED_IN.add(invoice_raise_in);
				
				/*if(price_unit.equals("2"))
				{
					VEXCHNG_RATE.add(exchng_rate);
					VEXCHNG_RATE_CD.add(exchng_rate_cd);
					VEXCHNG_RATE_DT.add(exchng_rate_dt);
				}
				else
				{
					VEXCHNG_RATE.add("");
					VEXCHNG_RATE_CD.add("");
					VEXCHNG_RATE_DT.add("");
				}*/
				VGROSS_AMT.add(rset.getString(23)==null?"":nf.format(rset.getDouble(23)));
				
				tot_accrual_amt+=rset.getDouble(23);
				tot_accrual_mmbtu+=rset.getDouble(16);
				
				eodProcessDoneOn=rset.getString(28)==null?"":rset.getString(28);
				
				String taxStructCd=rset.getString(29)==null?"":rset.getString(29);
				VTAX_STRUCT_CD.add(taxStructCd);
				VTAX_AMT.add(rset.getString(30)==null?"":nf.format(rset.getDouble(30)));
				VTAX_STRUCT_DTL.add(utilBean.getTaxDescr(conn, taxStructCd));
				VTAX_INFO.add("");
				VTOTAL_ACCRUAL_AMT.add(rset.getString(31)==null?"":nf.format(rset.getDouble(31)));
				
				tot_accrual_tax_amt+=rset.getDouble(30);
				tot_total_accrual_amt+=rset.getDouble(31);
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//PB20250606: For Fetching Sap Posting details 
	public void getPostingDetail()
	{
		String function_nm="getPostingDetail()";
		try
		{
			String queryString="SELECT MSG_STATUS,DOC_NO,STATUS_MSG,TO_CHAR(TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS'),'DD-MM-YYYY HH24:MI:SS') "
					+ "FROM FMS_SAP_ACK_DTL A "
					+ "WHERE COMPANY_CD=? AND FMS_REF=? "
					+ "AND TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS')=(SELECT MAX(TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS')) "
					+ "FROM FMS_SAP_ACK_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.FMS_REF=B.FMS_REF) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, remittance_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				sap_msg_status=rset.getString(1)==null?"":rset.getString(1);
				sap_doc_no=rset.getString(2)==null?"":rset.getString(2);
				sap_ack_msg=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
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
	
	String month = "";
	String year = "";
	String billing_cycle = "";
	String billing_freq="";
	String period_start_dt="";
	String period_end_dt="";
	String counterparty_cd = "";
	String trans_counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String plant_seq = "";
	String trans_bu_seq = "";
	String bu_unit = "";
	String inv_title="";
	String ic_inv_title="";
	String tc_inv_title="";
	String pc_inv_title="";
	String fflow_inv_title="";
	String refresh_flg = "";
	String invoice_type = "";
	String inv_type = "";
	String file_nm="";
	String emp_cd="";
	String mail_inv_type="";
	String from_dt = "";
	String to_dt = "";
	String type_flag ="";
	String sap_approval_flag="";
	String xmlfile_name="";
	String file_path="";
	String report_dt="";
	String automation_flag="";
	String isGenerateXML="";
	String cont_mapp="";
	String address_type = "";
	String inv_component = "";
	String fiscal_year = "";
	String filter_cont_type="";
	String filter_trans_cd="";
	String filter_remittance="";
	String report_start_dt="";
	String report_end_dt="";
	
	String sap_msg_status="";
	String sap_doc_no="";
	String sap_ack_msg="";
	String remittance_no="";
	
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setTrans_counterparty_cd(String trans_counterparty_cd) {this.trans_counterparty_cd = trans_counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setTrans_bu_seq(String trans_bu_seq) {this.trans_bu_seq = trans_bu_seq;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setInv_title(String inv_title) {this.inv_title = inv_title;}
	public void setIc_inv_title(String ic_inv_title) {this.ic_inv_title = ic_inv_title;}
	public void setPc_inv_title(String pc_inv_title) {this.pc_inv_title = pc_inv_title;}
	public void setTc_inv_title(String tc_inv_title) {this.tc_inv_title = tc_inv_title;}
	public void setFflow_inv_title(String fflow_inv_title) {this.fflow_inv_title = fflow_inv_title;}
	public void setRefresh_flg(String refresh_flg) {this.refresh_flg = refresh_flg;}
	public void setInvoice_type(String invoice_type) {this.invoice_type = invoice_type;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setFile_nm(String file_nm) {this.file_nm = file_nm;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setMail_inv_type(String mail_inv_type) {this.mail_inv_type = mail_inv_type;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setType_flag(String type_flag) {this.type_flag = type_flag;}
	public void setSap_approval_flag(String sap_approval_flag) {this.sap_approval_flag = sap_approval_flag;}
	public void setXmlfile_name(String xmlfile_name) {this.xmlfile_name = xmlfile_name;}
	public void setFile_path(String file_path) {this.file_path = file_path;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setAutomation_flag(String automation_flag) {this.automation_flag = automation_flag;}
	public void setIsGenerateXML(String isGenerateXML) {this.isGenerateXML = isGenerateXML;}
	public void setCont_mapp(String cont_mapp) {this.cont_mapp = cont_mapp;}
	public void setAddress_type(String address_type) {this.address_type = address_type;}	
	public void setQty_mmbtu(String qty_mmbtu) {this.qty_mmbtu = qty_mmbtu;}
	public void setBu_contact_person_cd(String bu_contact_person_cd) {this.bu_contact_person_cd = bu_contact_person_cd;}
	public void setPrice_cd(String price_cd) {this.price_cd = price_cd;}
	public void setInvoice_raised_in(String invoice_raised_in) {this.invoice_raised_in = invoice_raised_in;}
	public void setInvoice_seq(String invoice_seq) {this.invoice_seq = invoice_seq;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	public void setInvoice_no(String invoice_no) {this.invoice_no = invoice_no;}
	public void setInv_component(String inv_component) {this.inv_component = inv_component;}
	public void setFiscal_year(String fiscal_year) {this.fiscal_year = fiscal_year;}
	public void setFilter_cont_type(String filter_cont_type) {this.filter_cont_type = filter_cont_type;}
	public void setFilter_trans_cd(String filter_trans_cd) {this.filter_trans_cd = filter_trans_cd;}
	public void setFilter_remittance(String filter_remittance) {this.filter_remittance = filter_remittance;}
	
	public void setInvoice_dt(String invoice_dt) {this.invoice_dt = invoice_dt;}
	public void setInvoice_due_dt(String invoice_due_dt) {this.invoice_due_dt = invoice_due_dt;}
	
	public void setRemittance_no(String remittance_no) {this.remittance_no=remittance_no;}

	public String getSap_doc_no() {return sap_doc_no;}
	public String getSap_msg_status() {return sap_msg_status;}
	public String geSap_ack_msg() {return sap_ack_msg;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VINVOICE_TITLE = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VTRANS_BU_SEQ = new Vector();
	Vector VTRANS_BU_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VBILLING_FREQ_NM = new Vector();
	
	Vector VINVOICE_NO = new Vector();
	Vector VREMITTANCE_NO = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VFILE_UPLOAD_COUNT = new Vector();
	Vector VUPLOADED_FILE_NAME = new Vector();
	Vector VSTATUS = new Vector();
	Vector VAPPROVE_INVOICE_FLAG = new Vector();
	Vector VPDF_INV_FLAG = new Vector();
	Vector VAPPROVE_FLAG_CHECK = new Vector();
	Vector VCHECK_FLAG_CHECK = new Vector();
	Vector VAUTHORIZ_FLAG_CHECK = new Vector();
	Vector VIS_SUBMITTED = new Vector();
	Vector VINV_COMPONENTS = new Vector();
	Vector VINV_COMPONENTS_ABBR = new Vector();
	
	Vector VOTH_COUNTERPTY_CD = new Vector();
	Vector VOTH_GTA_COUNTERPTY_CD = new Vector();
	Vector VOTH_COUNTERPTY_ABBR = new Vector();
	Vector VOTH_COUNTERPTY_NM = new Vector();
	Vector VOTH_CONT_NO = new Vector();
	Vector VOTH_CONT_REV_NO = new Vector();
	Vector VOTH_AGMT_NO = new Vector();
	Vector VOTH_AGMT_REV_NO = new Vector();
	Vector VOTH_START_DT = new Vector();
	Vector VOTH_END_DT = new Vector();
	Vector VOTH_CONT_NAME = new Vector();
	Vector VOTH_CONT_REF_NO = new Vector();
	Vector VOTH_CONTRACT_TYPE = new Vector();
	Vector VOTH_ADDR_FLAG = new Vector();
	Vector VOTH_TRANS_BU_SEQ = new Vector();
	Vector VOTH_TRANS_BU_ABBR = new Vector();
	Vector VOTH_BU_PLANT_SEQ = new Vector();
	Vector VOTH_BU_PLANT_ABBR = new Vector();
	Vector VOTH_DEAL_NO = new Vector();
	Vector VOTH_PERIOD_START_DT = new Vector();
	Vector VOTH_PERIOD_END_DT = new Vector();
	Vector VOTH_BILLING_FREQ_FLAG = new Vector();
	Vector VOTH_BILLING_FREQ_NM = new Vector();
	
	Vector VOTH_INVOICE_NO = new Vector();
	Vector VOTH_REMITTANCE_NO = new Vector();
	Vector VOTH_INVOICE_SEQ = new Vector();
	Vector VOTH_FINANCIAL_YEAR = new Vector();
	Vector VOTH_INVOICE_TYPE = new Vector();
	Vector VOTH_FILE_UPLOAD_COUNT = new Vector();
	Vector VOTH_UPLOADED_FILE_NAME = new Vector();
	Vector VOTH_STATUS = new Vector();
	Vector VOTH_APPROVE_INVOICE_FLAG = new Vector();
	Vector VOTH_PDF_INV_FLAG = new Vector();
	Vector VOTH_APPROVE_FLAG_CHECK = new Vector();
	Vector VOTH_CHECK_FLAG_CHECK = new Vector();
	Vector VOTH_AUTHORIZ_FLAG_CHECK = new Vector();
	Vector VOTH_IS_SUBMITTED = new Vector();
	Vector VOTH_TYPE_FLAG = new Vector();
	Vector VOTH_SAP_APPROVAL_FLAG = new Vector();
	Vector VBU_CONTACT_PERSON = new Vector();
	Vector VBU_CONTACT_PERSON_CD = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VTEMP_QTY_MMBTU = new Vector();
	
	Vector VOTH_QTY_MMBTU=new Vector();
	Vector VOTH_TEMP_QTY_MMBTU=new Vector();
	Vector VINV_INDEX = new Vector();
	
	Vector VMAIL_FROM_LIST = new Vector();
	Vector VMAIL_TO_LIST = new Vector();
	Vector VMAIL_CC_LIST = new Vector();
	Vector VMAIL_BCC_LIST = new Vector();
	Vector VMAIL_SUBJECT = new Vector();
	Vector VMAIL_ATTACHMENT = new Vector();
	Vector VMAIL_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_BODY = new Vector();
	
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VINVOICE_RAISED_IN = new Vector();
	Vector VPAYMENT_DONE_IN = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VINVOICE_AMT = new Vector();
	Vector VADJ_SIGN = new Vector();
	Vector VADJ_AMT = new Vector();
	Vector VNET_PAYABLE = new Vector();
	Vector VTCS_TDS = new Vector();
	Vector VTCS_TDS_AMT = new Vector();
	Vector VTCS_TDS_FACTOR = new Vector();
	Vector VTCS_TDS_STRUCT_CD = new Vector();
	Vector VTCS_TDS_EFF_DT = new Vector();
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VINDEX = new Vector();
	Vector VTYPE_FLAG = new Vector();
	Vector VTYPE_NM = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VTXN_RATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_CD = new Vector();
	Vector VLINK_INVOICE_NO = new Vector();
	// For SAP XML
	Vector VLINESEQNO = new Vector();
	Vector VPOSTINGKEY = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VCURRENCYAMOUNT = new Vector();
	Vector VBUSINESSAREA = new Vector();
	Vector VITEMTEXT = new Vector();
	Vector VSHORTTEXT = new Vector();
	
	Vector VACCRUAL_QTY = new Vector();
	Vector VACCRUAL_AMT = new Vector();
	Vector VPRODUCTION_MONTH = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VCONT_MAP_LIST = new Vector();
	Vector VDIS_CONT_MAP_LIST = new Vector();
	
	Vector VCASH_FLOW_NM = new Vector();
	Vector VCASH_FLOW = new Vector();
	
	Vector VSG_MULTI_TAX_STRUCT = new Vector();
	Vector VPG_MULTI_TAX_STRUCT = new Vector();
	Vector VMULTI_TAX_STRUCT = new Vector();
	
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector V_TYPE_NM = new Vector();
	
	Vector VLINE_NO = new Vector();
	Vector VLINE_DESC = new Vector();
	Vector VUNIT = new Vector();
	Vector VQTY = new Vector();
	Vector VRATE = new Vector();
	Vector VAMOUNT = new Vector();
	
	Vector VINV_GRP_INDEX = new Vector();
	Vector VINV_GRP_INDEX_ROW_ID = new Vector();
	Vector VINV_GRP_INDEX_COLOR = new Vector();
	Vector VFILTER_CONT_TYPE = new Vector();
	Vector VFILTER_CONT_NAME = new Vector();
	
	Vector VFILTER_TRANS_CD = new Vector();
	Vector VFILTER_TRANS_NAME = new Vector();
	Vector VFILTER_DEAL_NO = new Vector();
	Vector VFILTER_MAPPING_ID = new Vector();
	
	Vector VTAX_STRUCT_DTL = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_INFO = new Vector();
	Vector VTOTAL_ACCRUAL_AMT = new Vector();
	
	Vector VOTH_INVOICE_DT = new Vector();
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVINVOICE_TITLE() {return VINVOICE_TITLE;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVTRANS_BU_SEQ() {return VTRANS_BU_SEQ;}
	public Vector getVTRANS_BU_ABBR() {return VTRANS_BU_ABBR;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVBILLING_FREQ_NM() {return VBILLING_FREQ_NM;}
	
	public Vector getVLINE_NO() {return VLINE_NO;}
	public Vector getVLINE_DESC() {return VLINE_DESC;}
	public Vector getVUNIT() {return VUNIT;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVAMOUNT() {return VAMOUNT;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVREMITTANCE_NO() {return VREMITTANCE_NO;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVFILE_UPLOAD_COUNT() {return VFILE_UPLOAD_COUNT;}
	public Vector getVUPLOADED_FILE_NAME() {return VUPLOADED_FILE_NAME;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVAPPROVE_INVOICE_FLAG() {return VAPPROVE_INVOICE_FLAG;}
	public Vector getVPDF_INV_FLAG() {return VPDF_INV_FLAG;}
	public Vector getVAPPROVE_FLAG_CHECK() {return VAPPROVE_FLAG_CHECK;}
	public Vector getVCHECK_FLAG_CHECK() {return VCHECK_FLAG_CHECK;}
	public Vector getVAUTHORIZ_FLAG_CHECK() {return VAUTHORIZ_FLAG_CHECK;}
	public Vector getVIS_SUBMITTED() {return VIS_SUBMITTED;}
	public Vector getVINV_COMPONENTS() {return VINV_COMPONENTS;}
	public Vector getVINV_COMPONENTS_ABBR() {return VINV_COMPONENTS_ABBR;}
	
	public Vector getVOTH_COUNTERPTY_CD() {return VOTH_COUNTERPTY_CD;}
	public Vector getVOTH_GTA_COUNTERPTY_CD() {return VOTH_GTA_COUNTERPTY_CD;}
	public Vector getVOTH_COUNTERPTY_ABBR() {return VOTH_COUNTERPTY_ABBR;}
	public Vector getVOTH_COUNTERPTY_NM() {return VOTH_COUNTERPTY_NM;}
	public Vector getVOTH_CONT_NO() {return VOTH_CONT_NO;}
	public Vector getVOTH_CONT_REV_NO() {return VOTH_CONT_REV_NO;}
	public Vector getVOTH_AGMT_NO() {return VOTH_AGMT_NO;}
	public Vector getVOTH_AGMT_REV_NO() {return VOTH_AGMT_REV_NO;}
	public Vector getVOTH_START_DT() {return VOTH_START_DT;}
	public Vector getVOTH_END_DT() {return VOTH_END_DT;}
	public Vector getVOTH_CONT_NAME() {return VOTH_CONT_NAME;}
	public Vector getVOTH_CONT_REF_NO() {return VOTH_CONT_REF_NO;}
	public Vector getVOTH_CONTRACT_TYPE() {return VOTH_CONTRACT_TYPE;}
	public Vector getVOTH_ADDR_FLAG() {return VOTH_ADDR_FLAG;}
	public Vector getVOTH_TRANS_BU_SEQ() {return VOTH_TRANS_BU_SEQ;}
	public Vector getVOTH_TRANS_BU_ABBR() {return VOTH_TRANS_BU_ABBR;}
	public Vector getVOTH_BU_PLANT_SEQ() {return VOTH_BU_PLANT_SEQ;}
	public Vector getVOTH_BU_PLANT_ABBR() {return VOTH_BU_PLANT_ABBR;}
	public Vector getVOTH_DEAL_NO() {return VOTH_DEAL_NO;}
	public Vector getVOTH_PERIOD_START_DT() {return VOTH_PERIOD_START_DT;}
	public Vector getVOTH_PERIOD_END_DT() {return VOTH_PERIOD_END_DT;}
	public Vector getVOTH_BILLING_FREQ_FLAG() {return VOTH_BILLING_FREQ_FLAG;}
	public Vector getVOTH_BILLING_FREQ_NM() {return VOTH_BILLING_FREQ_NM;}
	
	public Vector getVOTH_INVOICE_NO() {return VOTH_INVOICE_NO;}
	public Vector getVOTH_REMITTANCE_NO() {return VOTH_REMITTANCE_NO;}
	public Vector getVOTH_INVOICE_SEQ() {return VOTH_INVOICE_SEQ;}
	public Vector getVOTH_FINANCIAL_YEAR() {return VOTH_FINANCIAL_YEAR;}
	public Vector getVOTH_INVOICE_TYPE() {return VOTH_INVOICE_TYPE;}
	public Vector getVOTH_FILE_UPLOAD_COUNT() {return VOTH_FILE_UPLOAD_COUNT;}
	public Vector getVOTH_UPLOADED_FILE_NAME() {return VOTH_UPLOADED_FILE_NAME;}
	public Vector getVOTH_STATUS() {return VOTH_STATUS;}
	public Vector getVOTH_APPROVE_INVOICE_FLAG() {return VOTH_APPROVE_INVOICE_FLAG;}
	public Vector getVOTH_PDF_INV_FLAG() {return VOTH_PDF_INV_FLAG;}
	public Vector getVOTH_APPROVE_FLAG_CHECK() {return VOTH_APPROVE_FLAG_CHECK;}
	public Vector getVOTH_CHECK_FLAG_CHECK() {return VOTH_CHECK_FLAG_CHECK;}
	public Vector getVOTH_AUTHORIZ_FLAG_CHECK() {return VOTH_AUTHORIZ_FLAG_CHECK;}
	public Vector getVOTH_IS_SUBMITTED() {return VOTH_IS_SUBMITTED;}
	
	public Vector getVOTH_TYPE_FLAG() {return VOTH_TYPE_FLAG;}
	public Vector getVOTH_SAP_APPROVAL_FLAG() {return VOTH_SAP_APPROVAL_FLAG;}
	public Vector getVBU_CONTACT_PERSON() {return VBU_CONTACT_PERSON;}
	public Vector getVBU_CONTACT_PERSON_CD() {return VBU_CONTACT_PERSON_CD;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVTEMP_QTY_MMBTU() {return VTEMP_QTY_MMBTU;}
	
	public Vector getVOTH_QTY_MMBTU() {return VOTH_QTY_MMBTU;}
	public Vector getVOTH_TEMP_QTY_MMBTU() {return VOTH_TEMP_QTY_MMBTU;}
	public Vector getVINV_INDEX() {return VINV_INDEX;}
	
	public Vector getVMAIL_FROM_LIST() {return VMAIL_FROM_LIST;}
	public Vector getVMAIL_TO_LIST() {return VMAIL_TO_LIST;}
	public Vector getVMAIL_CC_LIST() {return VMAIL_CC_LIST;}
	public Vector getVMAIL_BCC_LIST() {return VMAIL_BCC_LIST;}
	public Vector getVMAIL_SUBJECT() {return VMAIL_SUBJECT;}
	public Vector getVMAIL_ATTACHMENT() {return VMAIL_ATTACHMENT;}
	public Vector getVMAIL_ATTACHMENT_PATH() {return VMAIL_ATTACHMENT_PATH;}
	public Vector getVMAIL_BODY() {return VMAIL_BODY;}
	
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVINVOICE_RAISED_IN() {return VINVOICE_RAISED_IN;}
	public Vector getVPAYMENT_DONE_IN() {return VPAYMENT_DONE_IN;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVADJ_SIGN() {return VADJ_SIGN;}
	public Vector getVADJ_AMT() {return VADJ_AMT;}
	public Vector getVNET_PAYABLE() {return VNET_PAYABLE;}
	public Vector getVTCS_TDS() {return VTCS_TDS;}
	public Vector getVTCS_TDS_AMT() {return VTCS_TDS_AMT;}
	public Vector getVTCS_TDS_FACTOR() {return VTCS_TDS_FACTOR;}
	public Vector getVTCS_TDS_STRUCT_CD() {return VTCS_TDS_STRUCT_CD;}
	public Vector getVTCS_TDS_EFF_DT() {return VTCS_TDS_EFF_DT;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTYPE_FLAG() {return VTYPE_FLAG;}
	public Vector getVTYPE_NM() {return VTYPE_NM;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVTXN_RATE() {return VTXN_RATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVCONTACT_PERSON() {return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_CD() {return VCONTACT_PERSON_CD;}
	public Vector getVLINK_INVOICE_NO() {return VLINK_INVOICE_NO;}
	public Vector getVLINESEQNO() {return VLINESEQNO;}
	public Vector getVPOSTINGKEY() {return VPOSTINGKEY;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVCURRENCYAMOUNT() {return VCURRENCYAMOUNT;}
	public Vector getVBUSINESSAREA() {return VBUSINESSAREA;}
	public Vector getVITEMTEXT() {return VITEMTEXT;}
	public Vector getVSHORTTEXT() {return VSHORTTEXT;}
	
	public Vector getVACCRUAL_QTY() {return VACCRUAL_QTY;}
	public Vector getVACCRUAL_AMT() {return VACCRUAL_AMT;}
	public Vector getVPRODUCTION_MONTH() {return VPRODUCTION_MONTH;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVCONT_MAP_LIST() {return VCONT_MAP_LIST;}
	public Vector getVDIS_CONT_MAP_LIST() {return VDIS_CONT_MAP_LIST;}
	
	public Vector getVCASH_FLOW_NM() {return VCASH_FLOW_NM;}
	public Vector getVCASH_FLOW() {return VCASH_FLOW;}
	
	public Vector getVSG_MULTI_TAX_STRUCT() {return VSG_MULTI_TAX_STRUCT;}
	public Vector getVPG_MULTI_TAX_STRUCT() {return VPG_MULTI_TAX_STRUCT;}
	public Vector getVMULTI_TAX_STRUCT() {return VMULTI_TAX_STRUCT;}
	
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVMAPPING_ID() {return VMAPPING_ID;}
	public Vector getV_TYPE_NM() {return V_TYPE_NM;}
	
	public Vector getVINV_GRP_INDEX() {return VINV_GRP_INDEX;}
	public Vector getVINV_GRP_INDEX_ROW_ID() {return VINV_GRP_INDEX_ROW_ID;}
	public Vector getVINV_GRP_INDEX_COLOR() {return VINV_GRP_INDEX_COLOR;}
	public Vector getVFILTER_CONT_TYPE() {return VFILTER_CONT_TYPE;}
	public Vector getVFILTER_CONT_NAME() {return VFILTER_CONT_NAME;}
	
	public Vector getVFILTER_TRANS_CD() {return VFILTER_TRANS_CD;}
	public Vector getVFILTER_TRANS_NAME() {return VFILTER_TRANS_NAME;}
	public Vector getVFILTER_DEAL_NO() {return VFILTER_DEAL_NO;}
	public Vector getVFILTER_MAPPING_ID() {return VFILTER_MAPPING_ID;}
	
	public Vector getVTAX_STRUCT_DTL() {return VTAX_STRUCT_DTL;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_INFO() {return VTAX_INFO;}
	public Vector getVTOTAL_ACCRUAL_AMT() {return VTOTAL_ACCRUAL_AMT;}
	
	public Vector getVOTH_INVOICE_DT() {return VOTH_INVOICE_DT;}
	
	String billing_freq_nm="";
	String couterpty_abbr="";
	String couterpty_nm="";
	String deal_no="";
	String plant_abbr="";
	String bu_plant_abbr="";
	String trans_bu_abbr="";
	String qty_mmbtu="";
	String price="";
	String price_cd="";
	String price_cd_nm="";
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
	String cont_end_dt="";
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
	String linked_invoice="0";
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
	String txn_amount="";
	String amount_in_word="";
	String tax_struct_info="";
	String negative_imbalance_qty="";
	String positive_imbalance_qty="";
	String unauthorized_overrun_qty="";
	String negative_imbalance_rate="";
	String positive_imbalance_rate="";
	String unauthorized_overrun_rate="";
	String positive_imbalance_amount="";
	String negative_imbalance_amount="";
	String unauthorized_imbalance_amount="";
	String ship_pay_rate="";
	String deficiency_qty="";
	String deficiency_amt="";
	String transmission_amt="";
	String mdq_qty="";
	String transmissionQty="";
	String ship_pay_percent="";
	String ship_pay_qty="";
	String ship_pay_freq="";
	String applicable_flag="";
	String applicable_abbr="";
	String tds_factor="";
	String tds_amount="";
	String tds_struct_cd="";
	String tds_struct_dt="";
	String tds_struct_info="";
	String parking_rate="";
	String parking_qty="";
	String parking_amt="";
	
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
	String pg_negative_imbalance_qty="";
	String pg_positive_imbalance_qty="";
	String pg_unauthorized_overrun_qty="";
	String pg_negative_imbalance_rate="";
	String pg_positive_imbalance_rate="";
	String pg_unauthorized_overrun_rate="";
	String pg_positive_imbalance_amount="";
	String pg_negative_imbalance_amount="";
	String pg_unauthorized_imbalance_amount="";
	String pg_ship_pay_rate="";
	String pg_deficiency_qty="";
	String pg_deficiency_amt="";
	String pg_transmission_amt="";
	String pg_ship_pay_freq="";
	String pg_tds_factor="";
	String pg_tds_amount="";
	String pg_tds_struct_cd="";
	String pg_tds_struct_dt="";
	String pg_sap_approval_flag="";
	String pg_parking_rate="";
	String pg_parking_qty="";
	String pg_parking_amt="";
	
	String inv_seq="";
	String inv_no="";
	String financial="";
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
	String signing_dt="";
	
	// SAP XML Variables
	String documentType="";
	String documentDate="";
	String documentNo="";
	String postingDate="";
	String accountingPeriodMonth="";
	String accountingPeriodYear="";
	String headerCompanyCode="";
	String docHeaderText="";
	String refNum ="";
	String currency="";
	
	String counterparty_nm="";
	String counterparty_abbr="";
	
	String zeroTotal ="";
	String isFreezed="";
	String eodProcessDoneOn = "";
	
	String tcs_factor="";
	String tcs_amount="";
	String tcs_struct_cd="";
	String tcs_struct_dt="";
	String tcs_struct_info="";
	
	String sub_inv_type="";
	String counterparty_name="";
	
	String submitted_fiscal_yr="";
	String sg_rem_gen_status="";
	String pg_rem_gen_status="";
	
	public String getBilling_freq_nm() {return billing_freq_nm;}
	public String getCouterpty_abbr() {return couterpty_abbr;}
	public String getCouterpty_nm() {return couterpty_nm;}
	public String getDeal_no() {return deal_no;}
	public String getPlant_abbr() {return plant_abbr;}
	public String getBu_plant_abbr() {return bu_plant_abbr;}
	public String getTrans_bu_abbr() {return trans_bu_abbr;}
	public String getQty_mmbtu() {return qty_mmbtu;}
	public String getPrice() {return price;}
	public String getPrice_cd() {return price_cd;}
	public String getPrice_cd_nm() {return price_cd_nm;}
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
	public String getCont_end_dt() {return cont_end_dt;}
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
	public String getTxn_amount() {return txn_amount;}
	public String getAmount_in_word() {return amount_in_word;}
	public String getTax_struct_info() {return tax_struct_info;}
	public String getNegative_imbalance_qty() {return negative_imbalance_qty;}
	public String getPositive_imbalance_qty() {return positive_imbalance_qty;}
	public String getUnauthorized_overrun_qty() {return unauthorized_overrun_qty;}
	public String getNegative_imbalance_rate() {return negative_imbalance_rate;}
	public String getPositive_imbalance_rate() {return positive_imbalance_rate;}
	public String getUnauthorized_overrun_rate() {return unauthorized_overrun_rate;}
	public String getPositive_imbalance_amount() {return positive_imbalance_amount;}
	public String getNegative_imbalance_amount() {return negative_imbalance_amount;}
	public String getUnauthorized_imbalance_amount() {return unauthorized_imbalance_amount;}
	public String getShip_pay_rate() {return ship_pay_rate;}
	public String getDeficiency_qty() {return deficiency_qty;}
	public String getDeficiency_amt() {return deficiency_amt;}
	public String getTransmission_amt() {return transmission_amt;}
	public String getMdq_qty() {return mdq_qty;}
	public String getTransmissionQty() {return transmissionQty;}
	public String getShip_pay_percent() {return ship_pay_percent;}
	public String getShip_pay_qty() {return ship_pay_qty;}
	public String getShip_pay_freq() {return ship_pay_freq;}
	public String getApplicable_flag() {return applicable_flag;}
	public String getApplicable_abbr() {return applicable_abbr;}
	public String getTds_factor() {return tds_factor;}
	public String getTds_amount() {return tds_amount;}
	public String getTds_struct_cd() {return tds_struct_cd;}
	public String getTds_struct_dt() {return tds_struct_dt;}
	public String getTds_struct_info() {return tds_struct_info;}
	public String getSap_approval_flag() {return sap_approval_flag;}
	public String getParking_rate() {return parking_rate;}
	public String getParking_qty() {return parking_qty;}
	public String getParking_amt() {return parking_amt;}
	
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
	public String getPg_negative_imbalance_qty() {return pg_negative_imbalance_qty;}
	public String getPg_positive_imbalance_qty() {return pg_positive_imbalance_qty;}
	public String getPg_unauthorized_overrun_qty() {return pg_unauthorized_overrun_qty;}
	public String getPg_negative_imbalance_rate() {return pg_negative_imbalance_rate;}
	public String getPg_positive_imbalance_rate() {return pg_positive_imbalance_rate;}
	public String getPg_unauthorized_overrun_rate() {return pg_unauthorized_overrun_rate;}
	public String getPg_positive_imbalance_amount() {return pg_positive_imbalance_amount;}
	public String getPg_negative_imbalance_amount() {return pg_negative_imbalance_amount;}
	public String getPg_unauthorized_imbalance_amount() {return pg_unauthorized_imbalance_amount;}
	public String getPg_ship_pay_rate() {return pg_ship_pay_rate;}
	public String getPg_deficiency_qty() {return pg_deficiency_qty;}
	public String getPg_deficiency_amt() {return pg_deficiency_amt;}
	public String getPg_transmission_amt() {return pg_transmission_amt;}
	public String getPg_ship_pay_freq() {return pg_ship_pay_freq;}
	public String getPg_tds_amount() {return pg_tds_amount;}
	public String getPg_tds_factor() {return pg_tds_factor;}
	public String getPg_tds_struct_cd() {return pg_tds_struct_cd;}
	public String getPg_tds_struct_dt() {return pg_tds_struct_dt;}
	public String getPg_sap_approval_flag() {return pg_sap_approval_flag;}
	public String getPg_parking_rate() {return pg_parking_rate;}
	public String getPg_parking_qty() {return pg_parking_qty;}
	public String getPg_parking_amt() {return pg_parking_amt;}
	
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setInv_no(String inv_no) {this.inv_no = inv_no;}
	public void setFinancial(String financial) {this.financial = financial;}
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
	
	public String getDocumentType() {return documentType;}
	public String getDocumentDate() {return documentDate;}
	public String getDocumentNo() {return documentNo;}
	public String getPostingDate() {return postingDate;}
	public String getAccountingPeriodMonth() {return accountingPeriodMonth;}
	public String getAccountingPeriodYear() {return accountingPeriodYear;}
	public String getHeaderCompanyCode() {return headerCompanyCode;}
	public String getDocHeaderText() {return docHeaderText;}
	public String getRefNum() {return refNum;}
	public String getCurrency() {return currency;}
	
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	
	public String getTcs_factor() {return tcs_factor;}
	public String getTcs_amount() {return tcs_amount;}
	public String getTcs_struct_cd() {return tcs_struct_cd;}
	public String getTcs_struct_dt() {return tcs_struct_dt;}
	public String getTcs_struct_info() {return tcs_struct_info;}
	public String getXmlfile_name() {return xmlfile_name;}
	public String getZeroTotal() {return zeroTotal;}
	public String getIsFreezed() {return isFreezed;}
	public String getEodProcessDoneOn() {return eodProcessDoneOn;}
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	
	public String getSub_inv_type() {return sub_inv_type;}
	public String getCounterparty_name() {return counterparty_name;}
	
	public String getSubmitted_fiscal_yr() {return submitted_fiscal_yr;}
	public String getSg_rem_gen_status() {return sg_rem_gen_status;}
	public String getPg_rem_gen_status() {return pg_rem_gen_status;}
	
	boolean submission_chk = false;
	boolean sg_submission_chk = false;
	boolean pg_submission_chk = false;

	public boolean getSubmission_chk() {return submission_chk;}
	public boolean getSg_submission_chk() {return sg_submission_chk;}
	public boolean getPg_submission_chk() {return pg_submission_chk;}
	
	HashMap<String, String> invoice_data = new HashMap<String, String>();

	public HashMap<String, String> getInvoice_data() {return invoice_data;}
	
	double tot_accrual_mmbtu=0;
	double tot_accrual_amt=0;
	double tot_accrual_amt_usd=0;
	double tot_accrual_tax_amt=0;
	double tot_total_accrual_amt=0;
	
	String str_tot_accrual_mmbtu="";
	String str_tot_accrual_amt="";
	String str_tot_accrual_amt_usd="";
	String str_accrual_tax_amt="";
	String str_total_accrual_amt="";
	
	public String getStr_tot_accrual_mmbtu() {
		return str_tot_accrual_mmbtu;
	}

	public String getStr_tot_accrual_amt() {
		return str_tot_accrual_amt;
	}
	
	public String getStr_tot_accrual_amt_usd() {
		return str_tot_accrual_amt_usd;
	}
	
	public String getStr_accrual_tax_amt() {
		return str_accrual_tax_amt;
	}

	public String getStr_total_accrual_amt() {
		return str_total_accrual_amt;
	}

}
